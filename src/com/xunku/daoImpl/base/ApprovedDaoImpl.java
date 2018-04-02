package com.xunku.daoImpl.base;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.xunku.constant.PortalCST;
import com.xunku.dao.base.ApprovedDao;
import com.xunku.dto.PagerDTO;
import com.xunku.dto.sysManager.ApprovedDTO;
import com.xunku.pojo.base.Approved;
import com.xunku.pojo.base.Pager;
import com.xunku.utils.DatabaseUtils;
import com.xunku.utils.Pagefile;


public class ApprovedDaoImpl implements ApprovedDao {

    @Override
    public void delete(int appid) {
	Connection conn = null;
	PreparedStatement pstmt = null;
	try {
	    conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME)
		    .getConnection();
	    String sql = "delete from Base_Account_Approved where approvedid=? "
		    + "delete from Base_Approved where id=?";
	    pstmt = conn.prepareStatement(sql);
	    pstmt.setInt(1, appid);
	    pstmt.setInt(2, appid);
	    pstmt.execute();
	} catch (SQLException e) {
	    e.printStackTrace();
	} finally {
	    try {
		if (pstmt != null) {
		    pstmt.close();
		}
		if (conn != null) {
		    conn.close();
		}
	    } catch (SQLException e) {
		e.printStackTrace();
	    }
	}
    }

    @Override
    public void insert(Approved a) {
	Connection conn = null;
	PreparedStatement pstmt = null;
	PreparedStatement pstmt2 = null;
	ResultSet rs = null;
	try {
	    conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME)
		    .getConnection();
	    String sql = "insert into Base_Approved (userid,checkid,isCheck) values (?,?,?)";
	    pstmt = conn.prepareStatement(sql,
		    PreparedStatement.RETURN_GENERATED_KEYS);
	    pstmt.setInt(1, a.getUserid());
	    pstmt.setInt(2, a.getCheckid());
	    pstmt.setBoolean(3, a.isCheck());
	    pstmt.executeUpdate();
	    rs = pstmt.getGeneratedKeys();
	    if (rs.next()) {
		int appid = rs.getInt(1);
		for (String uid : a.getUids()) {
		    String sql2 = "insert into Base_Account_Approved(uid,approvedid) values (?,?)";
		    pstmt2 = conn.prepareStatement(sql2);
		    pstmt2.setString(1, uid);
		    pstmt2.setInt(2, appid);
		    pstmt2.execute();
		}
	    }
	} catch (SQLException e) {
	    e.printStackTrace();
	} finally {
	    try {
		if (rs != null) {
		    rs.close();
		}
		if (pstmt2 != null) {
		    pstmt2.close();
		}
		if (pstmt != null) {
		    pstmt.close();
		}
		if (conn != null) {
		    conn.close();
		}
	    } catch (SQLException e) {
		e.printStackTrace();
	    }
	}
    }

    @Override
    public void update(Approved a) {
	Connection conn = null;
	PreparedStatement pstmt = null;
	PreparedStatement pstmt2 = null;
	try {
	    conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME)
		    .getConnection();
	    String sql = "delete from Base_Account_Approved where approvedid=? "
		    + "update Base_Approved set userid=?,checkid=?,isCheck=? where id=?";
	    // 删除审核账号和修改审核设置
	    pstmt = conn.prepareStatement(sql);
	    pstmt.setInt(1, a.getId());
	    pstmt.setInt(2, a.getUserid());
	    pstmt.setInt(3, a.getCheckid());
	    pstmt.setBoolean(4, a.isCheck());
	    pstmt.setInt(5, a.getId());
	    pstmt.executeUpdate();
	    // 添加需要审核的账号
	    for (String uid : a.getUids()) {
		String sql2 = "insert into Base_Account_Approved(uid,approvedid) values (?,?)";
		pstmt2 = conn.prepareStatement(sql2);
		pstmt2.setString(1, uid);
		pstmt2.setInt(2, a.getId());
		pstmt2.execute();
	    }
	} catch (SQLException e) {
	    e.printStackTrace();
	} finally {
	    try {
		if (pstmt2 != null) {
		    pstmt2.close();
		}
		if (pstmt != null) {
		    pstmt.close();
		}
		if (conn != null) {
		    conn.close();
		}
	    } catch (SQLException e) {
		e.printStackTrace();
	    }
	}
    }

    @Override
    public Pagefile<ApprovedDTO> queryByUserId(Pager dto, int userid) {
	Connection conn = null;
	CallableStatement cstmt = null;
	Pagefile<ApprovedDTO> pagefile = null;
	ResultSet rs = null;
	ResultSet rs2 = null;
	try {
	    conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME)
		    .getConnection();
	    cstmt = conn.prepareCall("{call SupesoftPage(?,?,?,?,?,?,?)}");
	    cstmt.setString(1, "vi_Approved_queryByUserId");
	    cstmt.setString(2, "id,NickName,UserName,isCheck,checkid,CustomID");
	    cstmt.setInt(3, dto.getPageSize());
	    cstmt.setInt(4, dto.getPageIndex());
	    cstmt
		    .setString(
			    5,
			    "where CustomID=(select CustomID from Base_Users where ID="
				    + userid
				    + ") group by id,NickName,UserName,isCheck,checkid,CustomID");
	    cstmt.setString(6, "id");
	    cstmt.setInt(7, 0);
	    cstmt.execute();
	    pagefile = new Pagefile<ApprovedDTO>();
	    rs = cstmt.getResultSet();
	    ApprovedDTO appdto = null;
	    while (rs.next()) {
		appdto = new ApprovedDTO();
		appdto.setAppid(rs.getInt("id"));
		appdto.setUserNick(rs.getString("NickName"));
		appdto.setUserName(rs.getString("UserName"));
		int checkid = rs.getInt("checkid");
		appdto.setCheck(this.getCheckName(conn, checkid));
		appdto.setCheck(rs.getBoolean("isCheck"));
		pagefile.getRows().add(appdto);
	    }
	    if (cstmt.getMoreResults()) {
		rs2 = cstmt.getResultSet();
		if (rs2.next()) {
		    pagefile.setRealcount(rs2.getInt("RecordCount"));
		}
	    }
	} catch (SQLException e) {
	    e.printStackTrace();
	} finally {
	    try {
		if (rs2 != null) {
		    rs2.close();
		}
		if (rs != null) {
		    rs.close();
		}
		if (cstmt != null) {
		    cstmt.close();
		}
		if (conn != null) {
		    conn.close();
		}
	    } catch (SQLException e) {
		e.printStackTrace();
	    }
	}
	return pagefile;
    }

    private String getCheckName(Connection conn, int checkid) {
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	String check = null;
	try {
	    String sql = "select NickName from Base_Users where ID =?";
	    pstmt = conn.prepareStatement(sql);
	    pstmt.setInt(1, checkid);
	    rs = pstmt.executeQuery();
	    while (rs.next()) {
		check = rs.getString("NickName");
	    }
	} catch (SQLException e) {
	    e.printStackTrace();
	} finally {
	    try {
		if (rs != null) {
		    rs.close();
		}
		if (pstmt != null) {
		    pstmt.close();
		}
	    } catch (SQLException e) {
		e.printStackTrace();
	    }
	}
	return check;
    }

    @Override
    public Approved queryById(int id) {
	Connection conn = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	Approved app = null;
	try {
	    conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME)
		    .getConnection();
	    String sql = "select id,isCheck,checkid,CustomID,userid,uid from vi_Approved_queryByUserId where id=?";
	    pstmt = conn.prepareStatement(sql);
	    pstmt.setInt(1, id);
	    rs = pstmt.executeQuery();
	    List<String> uids = new ArrayList<String>();
	    while (rs.next()) {
		app = new Approved();
		app.setId(rs.getInt("id"));
		app.setUserid(rs.getInt("userid"));
		app.setCheckid(rs.getInt("checkid"));
		app.setCheck(rs.getBoolean("isCheck"));
		uids.add(rs.getString("uid"));
	    }
	    app.setUids(uids);
	} catch (SQLException e) {
	    e.printStackTrace();
	} finally {
	    try {
		if (rs != null) {
		    rs.close();
		}
		if (pstmt != null) {
		    pstmt.close();
		}
		if (conn != null) {
		    conn.close();
		}
	    } catch (SQLException e) {
		e.printStackTrace();
	    }
	}
	return app;
    }

    @Override
    public boolean checkIsExsit(int userid, String uid) {
	Connection conn = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	boolean isExsit = false;
	try {
	    conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME)
		    .getConnection();
	    String sql = "select id from Base_Approved a join Base_Account_Approved b on a.id=b.approvedid where a.userid=? and b.uid=?";
	    pstmt = conn.prepareStatement(sql);
	    pstmt.setInt(1, userid);
	    pstmt.setString(2, uid);
	    rs = pstmt.executeQuery();
	    if (rs.next()) {
		isExsit = true;
	    }
	} catch (SQLException e) {
	    e.printStackTrace();
	} finally {
	    try {
		if (rs != null) {
		    rs.close();
		}
		if (pstmt != null) {
		    pstmt.close();
		}
		if (conn != null) {
		    conn.close();
		}
	    } catch (SQLException e) {
		e.printStackTrace();
	    }
	}
	return isExsit;
    }
}
