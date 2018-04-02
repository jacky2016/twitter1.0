package com.xunku.daoImpl.office;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.xunku.app.interfaces.ITweet;
import com.xunku.app.model.tweets.TweetFactory;
import com.xunku.constant.PortalCST;
import com.xunku.dao.office.NavieDao;
import com.xunku.dto.PagerDTO;
import com.xunku.dto.myTwitter.WeiboPostDTO;
import com.xunku.dto.office.NavieCountDTO;
import com.xunku.pojo.base.Organization;
import com.xunku.pojo.office.Navie;
import com.xunku.pojo.office.NavieAccount;
import com.xunku.utils.DatabaseUtils;
import com.xunku.utils.Pagefile;

public class NavieDaoImpl implements NavieDao {

    @Override
    public int insert(NavieAccount na) {
	Connection conn = null;
	CallableStatement cstmt = null;
	int nid = 0;
	try {
	    conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME)
		    .getConnection();
	    cstmt = conn.prepareCall("{call sp_Navie_insert(?,?,?,?,?,?)}");
	    cstmt.setString(1, na.getNavie().getName());
	    cstmt.setInt(2, na.getNavie().getCustomId());
	    cstmt.setString(3, na.getUid());
	    cstmt.setInt(4, na.getPlatform());
	    cstmt.setString(5, na.getDisplayName());
	    cstmt.registerOutParameter(6, java.sql.Types.INTEGER);
	    cstmt.execute();
	    nid = cstmt.getInt(6);
	} catch (SQLException e) {
	    e.printStackTrace();
	} finally {
	    this.CloseStatus(cstmt, conn);
	}
	return nid;
    }

    @Override
    public boolean deleteById(int nid,String uid) {
	Connection conn = null;
	CallableStatement cstmt = null;
	boolean isDelete = false;
	try {
	    conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME)
		    .getConnection();
	    cstmt = conn.prepareCall("{call sp_Navie_deleteById(?,?)}");
	    cstmt.setInt(1, nid);
	    cstmt.setString(2, uid);
	    int flag = cstmt.executeUpdate();
	    if (flag > 0) {
		isDelete = true;
	    }
	    conn.commit();
	} catch (SQLException e) {
	    e.printStackTrace();
	} finally {
	    this.CloseStatus(cstmt, conn);
	}
	return isDelete;
    }

    @Override
    public Pagefile<WeiboPostDTO> queryByTextCount(PagerDTO dto, String uid,
	    Date startTime, Date endTime, int customId) {
	Connection conn = null;
	CallableStatement cstmt = null;
	ResultSet rs = null;
	ResultSet rs2 = null;
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	String stime = sdf.format(startTime);
	String etime = sdf.format(endTime);
	Pagefile<WeiboPostDTO> pagefile = null;
	try {
	    conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME)
		    .getConnection();
	    cstmt = conn.prepareCall("{call SupesoftPage(?,?,?,?,?,?,?)}");
	    cstmt.setString(1, "My_Posts");
	    cstmt.setString(2, "*");
	    cstmt.setInt(3, dto.pageSize);
	    cstmt.setInt(4, dto.pageIndex);
	    cstmt.setString(5, " where uid=" + uid + " and created between '"
		    + stime + "' and '" + etime + "'");
	    cstmt.setString(6, "ID");
	    cstmt.setInt(7, 0);
	    cstmt.execute();
	    pagefile = new Pagefile<WeiboPostDTO>();
	    rs = cstmt.getResultSet();
	    while (rs.next()) {
		WeiboPostDTO wpdto = new WeiboPostDTO();
		wpdto.setId(rs.getInt("id"));
		wpdto.setTid(rs.getString("tid"));
		wpdto.setText(rs.getString("text"));
		wpdto.setCreateTime(rs.getString("created"));
		wpdto.setRepostCount(rs.getInt("reposts"));
		wpdto.setCommentCount(rs.getInt("comments"));
		wpdto.setUcode(rs.getString("ucode"));
		wpdto.setPlatform(rs.getInt("platform"));
		wpdto.setUid(rs.getString("uid"));
		wpdto.setOrgCount(queryByAID(conn, rs.getString("source"),
			stime, etime, customId));
		pagefile.getRows().add(wpdto);
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
	    this.CloseStatus(rs2, rs, cstmt, conn);
	}
	return pagefile;
    }

    private int queryByAID(Connection conn, String source, String startTime,
	    String endTime, int customid) {
	String sql = "select COUNT(ID) as orgcount from Base_Organizations where CustomID=? and Uid in (select uid from My_Posts where tid=? and created between ? and ?)";
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	int orgCount = 0;
	try {
	    pstmt = conn.prepareStatement(sql);
	    pstmt.setInt(1, customid);
	    pstmt.setString(2, source);
	    pstmt.setString(3, startTime);
	    pstmt.setString(4, endTime);
	    rs = pstmt.executeQuery();
	    if (rs.next()) {
		orgCount = rs.getInt("orgcount");
	    }
	} catch (SQLException e) {
	    e.printStackTrace();
	} finally {
	    this.CloseStatus(rs, pstmt, null);
	}
	return orgCount;
    }

    @Override
    public Pagefile<NavieCountDTO> queryByNavieCount(PagerDTO dto,
	    String startTime, String endTime, int customId) {
	Connection conn = null;
	CallableStatement cstmt = null;
	ResultSet rs = null;
	ResultSet rs2 = null;
	Pagefile<NavieCountDTO> pagefile = null;
	try {
	    conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME)
		    .getConnection();
	    cstmt = conn.prepareCall("{call SupesoftPage(?,?,?,?,?,?,?)}");
	    cstmt.setString(1, "Office_Navies");
	    cstmt.setString(2, "*");
	    cstmt.setInt(3, dto.pageSize);
	    cstmt.setInt(4, dto.pageIndex);
	    cstmt.setString(5, " where CustomID=" + customId);
	    cstmt.setString(6, "ID");
	    cstmt.setInt(7, 0);
	    cstmt.execute();
	    pagefile = new Pagefile<NavieCountDTO>();
	    rs = cstmt.getResultSet();
	    while (rs.next()) {
		NavieCountDTO ncdto = new NavieCountDTO();
		int id = rs.getInt("ID");
		ncdto.setId(id);
		ncdto.setName(rs.getString("Name"));
		int acount = queryNavieAccount(conn, id);
		int wcount = queryNavieWeibo(conn, id, startTime, endTime);
		ncdto.setAcount(acount);
		ncdto.setWcount(wcount);
		pagefile.getRows().add(ncdto);
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
	    this.CloseStatus(rs2, rs, cstmt, conn);
	}
	return pagefile;
    }

    private int queryNavieWeibo(Connection conn, int nid, String startTime,
	    String endTime) {
	String sql = "select Uid,Platform from Office_Navies_Account where NavieID=?";
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	PreparedStatement pstmt2 = null;
	ResultSet rs2 = null;
	int total = 0;
	try {
	    pstmt = conn.prepareStatement(sql);
	    pstmt.setInt(1, nid);
	    rs = pstmt.executeQuery();
	    while (rs.next()) {
		String uid = rs.getString("Uid");
		int platform = rs.getInt("Platform");
		String sql2 = "select COUNT(id) as count from My_Posts where uid=? and platform=? and type in (2,3) and created between ? and ?";
		pstmt2 = conn.prepareStatement(sql2);
		pstmt2.setString(1, uid);
		pstmt2.setInt(2, platform);
		pstmt2.setString(3, startTime);
		pstmt2.setString(4, endTime);
		rs2 = pstmt2.executeQuery();
		if (rs2.next()) {
		    total += rs2.getInt("count");
		}
	    }
	} catch (SQLException e) {
	    e.printStackTrace();
	} finally {
	    this.CloseStatus(rs, pstmt, null);
	    this.CloseStatus(rs2, pstmt2, null);
	}
	return total;
    }

    private int queryNavieAccount(Connection conn, int nid) {
	String sql = "select COUNT(ID) as count from Office_Navies_Account where NavieID=?";
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	int count = 0;
	try {
	    pstmt = conn.prepareStatement(sql);
	    pstmt.setInt(1, nid);
	    rs = pstmt.executeQuery();
	    if (rs.next()) {
		count = rs.getInt("count");
	    }
	} catch (SQLException e) {
	    e.printStackTrace();
	} finally {
	    this.CloseStatus(rs, pstmt, null);
	}
	return count;
    }

    @Override
    public boolean updateById(NavieAccount na) {
	Connection conn = null;
	PreparedStatement pstmt = null;
	boolean isUpdate = false;
	try {
	    conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME)
		    .getConnection();
	    String sql = "UPDATE Office_Navies SET CustomID=?,Name=? WHERE ID=? "
		    + "UPDATE Office_Navies_Account SET NavieID=?,Uid=?,Platform=?,DisplayName=? WHERE ID=?";
	    pstmt = conn.prepareStatement(sql);
	    pstmt.setInt(1, na.getNavie().getCustomId());
	    pstmt.setString(2, na.getNavie().getName());
	    pstmt.setInt(3, na.getNavie().getId());
	    pstmt.setInt(4, na.getNavie().getId());
	    pstmt.setString(5, na.getUid());
	    pstmt.setInt(6, na.getPlatform());
	    pstmt.setString(7, na.getDisplayName());
	    int flag = pstmt.executeUpdate();
	    if (flag > 0) {
		isUpdate = true;
	    }
	} catch (SQLException e) {
	    e.printStackTrace();
	} finally {
	    this.CloseStatus(pstmt, conn);
	}
	return isUpdate;
    }

    @Override
    public Pagefile<NavieAccount> queryNavieAccount(PagerDTO dto, int nid) {
	Connection conn = null;
	CallableStatement cstmt = null;
	ResultSet rs = null;
	ResultSet rs2 = null;
	Pagefile<NavieAccount> pagefile = null;
	try {
	    conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME)
		    .getConnection();
	    cstmt = conn.prepareCall("{call SupesoftPage(?,?,?,?,?,?,?)}");
	    cstmt.setString(1, "Office_Navies_Account");
	    cstmt.setString(2, "*");
	    cstmt.setInt(3, dto.pageSize);
	    cstmt.setInt(4, dto.pageIndex);
	    cstmt.setString(5, " where NavieID=" + nid);
	    cstmt.setString(6, "ID");
	    cstmt.setInt(7, 0);
	    cstmt.execute();
	    pagefile = new Pagefile<NavieAccount>();
	    rs = cstmt.getResultSet();
	    while (rs.next()) {
		NavieAccount na = new NavieAccount();
		na.setId(rs.getInt("id"));
		na.setUid(rs.getString("uid"));
		na.setPlatform(rs.getInt("platform"));
		na.setDisplayName(rs.getString("DisplayName"));
		pagefile.getRows().add(na);
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
	    this.CloseStatus(rs2, rs, cstmt, conn);
	}
	return pagefile;
    }

    private void CloseStatus(ResultSet rs, PreparedStatement pstmt,
	    Connection conn) {
	this.CloseStatus(null, null, rs, null, pstmt, conn);
    }

    private void CloseStatus(PreparedStatement pstmt, Connection conn) {
	this.CloseStatus(null, pstmt, conn);
    }

    private void CloseStatus(ResultSet rs2, ResultSet rs,
	    CallableStatement cstmt, Connection conn) {
	this.CloseStatus(null, rs2, rs, cstmt, null, conn);
    }

    private void CloseStatus(ResultSet rs3, ResultSet rs2, ResultSet rs,
	    CallableStatement cstmt, PreparedStatement pstmt, Connection conn) {

	try {
	    if (rs3 != null) {
		rs3.close();
	    }
	    if (rs2 != null) {
		rs2.close();
	    }
	    if (rs != null) {
		rs.close();
	    }
	    if (pstmt != null) {
		pstmt.close();
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

    @Override
    public Pagefile<ITweet> queryNavieToMe(PagerDTO dto, int navieId,
	    String startTime, String endTime) {
	Connection conn = null;
	CallableStatement cstmt = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	ResultSet rs2 = null;
	ResultSet rs3 = null;
	Pagefile<ITweet> pagefile = null;
	try {
	    conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME)
		    .getConnection();
	    String sql = "select Uid,Platform from Office_Navies_Account where NavieID=?";
	    pstmt = conn.prepareStatement(sql);
	    pstmt.setInt(1, navieId);
	    rs = pstmt.executeQuery();
	    while (rs.next()) {
		String uid = rs.getString("Uid");
		int platform = rs.getInt("Platform");

		cstmt = conn.prepareCall("{call SupesoftPage(?,?,?,?,?,?,?)}");
		cstmt.setString(1, "My_Posts");
		cstmt.setString(2, "*");
		cstmt.setInt(3, dto.pageSize);
		cstmt.setInt(4, dto.pageIndex);
		cstmt.setString(5, " where Uid='" + uid + "' and platform="
			+ platform + " and created between '" + startTime
			+ "' and '" + endTime + "'");
		cstmt.setString(6, "ID");
		cstmt.setInt(7, 0);
		cstmt.execute();
		pagefile = new Pagefile<ITweet>();
		rs2 = cstmt.getResultSet();
		while (rs2.next()) {
		    ITweet p = TweetFactory.createTweet(rs);
		    p.setSource(this.getITweet(conn, p.getOriginalTweet().getTid()));
		    pagefile.getRows().add(p);
		}
		if (cstmt.getMoreResults()) {
		    rs3 = cstmt.getResultSet();
		    if (rs3.next()) {
			pagefile.setRealcount(rs3.getInt("RecordCount"));
		    }
		}
	    }
	} catch (SQLException e) {
	    e.printStackTrace();
	} finally {
	    this.CloseStatus(rs3, rs2, rs, cstmt, pstmt, conn);
	}
	return pagefile;
    }

    private ITweet getITweet(Connection conn, String source) {
	String sql = "select * from My_Posts where tid=?";
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	ITweet tweet = null;
	try {
	    pstmt = conn.prepareStatement(sql);
	    pstmt.setString(1, source);
	    rs = pstmt.executeQuery();
	    while (rs.next()) {
		tweet = TweetFactory.createTweet(rs);
	    }
	} catch (SQLException e) {
	    e.printStackTrace();
	} finally {
	    this.CloseStatus(rs, pstmt, null);
	}
	return tweet;
    }

    @Override
    public Pagefile<Organization> queryOrganization(PagerDTO dto, String tid,
	    String startTime, String endTime, int customId) {
	Connection conn = null;
	CallableStatement cstmt = null;
	ResultSet rs = null;
	ResultSet rs2 = null;
	Pagefile<Organization> pagefile = null;
	try {
	    conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME)
		    .getConnection();
	    cstmt = conn.prepareCall("{call SupesoftPage(?,?,?,?,?,?,?)}");
	    cstmt.setString(1, "Base_Organizations");
	    cstmt.setString(2, "*");
	    cstmt.setInt(3, dto.pageSize);
	    cstmt.setInt(4, dto.pageIndex);
	    cstmt.setString(5, "where CustomID=" + customId
		    + " and Uid in (select uid from My_Posts where tid='" + tid
		    + "' and created between '" + startTime + "' and '"
		    + endTime + "')");
	    cstmt.setString(6, "ID");
	    cstmt.setInt(7, 0);
	    cstmt.execute();
	    pagefile = new Pagefile<Organization>();
	    rs = cstmt.getResultSet();
	    while (rs.next()) {
		Organization org = new Organization();
		org.setId(rs.getInt("ID"));
		org.setCustomID(rs.getInt("CustomID"));
		org.setName(rs.getString("Name"));
		org.setPlatform(rs.getInt("Platform"));
		org.setUid(rs.getString("Uid"));
		pagefile.getRows().add(org);
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
	    this.CloseStatus(rs2, rs, cstmt, conn);
	}
	return pagefile;
    }

    @Override
    public Pagefile<NavieAccount> queryNavieList(PagerDTO dto, int customId) {
	Connection conn = null;
	CallableStatement cstmt = null;
	ResultSet rs = null;
	ResultSet rs2 = null;
	Pagefile<NavieAccount> pagefile = null;
	try {
	    conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME)
		    .getConnection();
	    cstmt = conn.prepareCall("{call SupesoftPage(?,?,?,?,?,?,?)}");
	    cstmt.setString(1, "vi_Navies_queryByNavieCount");
	    cstmt.setString(2, "*");
	    cstmt.setInt(3, dto.pageSize);
	    cstmt.setInt(4, dto.pageIndex);
	    cstmt.setString(5, " where CustomID=" + customId);
	    cstmt.setString(6, "ID");
	    cstmt.setInt(7, 0);
	    cstmt.execute();
	    pagefile = new Pagefile<NavieAccount>();
	    rs = cstmt.getResultSet();
	    while (rs.next()) {
		pagefile.getRows().add(this.getNavieAccount(rs));
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
	    this.CloseStatus(rs2, rs, cstmt, conn);
	}
	return pagefile;
    }

    private NavieAccount getNavieAccount(ResultSet rs) throws SQLException {
	NavieAccount na = new NavieAccount();
	Navie navie = new Navie();
	navie.setId(rs.getInt("NavieID"));
	navie.setName(rs.getString("Name"));
	navie.setCustomId(rs.getInt("CustomID"));
	na.setNavie(navie);
	na.setUid(rs.getString("Uid"));
	na.setPlatform(rs.getInt("Platform"));
	na.setDisplayName(rs.getString("DisplayName"));
	return na;
    }

    @Override
    public boolean checkIsExsit(int customid,int platform, String displayname) {
	Connection conn = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	boolean isExsit = false;
	try {
	    conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME)
		    .getConnection();
	    String sql = "select a.ID from Office_Navies a join Office_Navies_Account b on a.ID=b.NavieID where b.Platform=? and b.DisplayName=? and a.CustomID=?";
	    pstmt = conn.prepareStatement(sql);
	    pstmt.setInt(1, platform);
	    pstmt.setString(2, displayname);
	    pstmt.setInt(3, customid);
	    rs = pstmt.executeQuery();
	    if (rs.next()) {
		isExsit = true;
	    }
	} catch (SQLException e) {
	    e.printStackTrace();
	} finally {
	    this.CloseStatus(pstmt, conn);
	}
	return isExsit;
    }

    @Override
    public List<NavieAccount> queryNavieList(String navice, int customid) {
	Connection conn = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	List<NavieAccount> list = new ArrayList<NavieAccount>();
	try {
	    conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME)
		    .getConnection();
	    String sql = "select * from Office_Navies_Account where NavieID in (select ID from Office_Navies where Name=? and CustomID=?)";
	    pstmt = conn.prepareStatement(sql);
	    pstmt.setString(1, navice);
	    pstmt.setInt(2, customid);
	    rs = pstmt.executeQuery();
	    while (rs.next()) {
		NavieAccount na = new NavieAccount();
		Navie navie = new Navie();
		navie.setId(rs.getInt("NavieID"));
		navie.setName(navice);
		na.setNavie(navie);
		na.setId(rs.getInt("ID"));
		na.setUid(rs.getString("Uid"));
		na.setPlatform(rs.getInt("Platform"));
		na.setDisplayName(rs.getString("DisplayName"));
		list.add(na);
	    }
	} catch (SQLException e) {
	    e.printStackTrace();
	} finally {
	    this.CloseStatus(rs, pstmt, conn);
	}
	return list;
    }

    @Override
    public List<NavieAccount> getNavieAccount(int customid) {
	Connection conn = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	List<NavieAccount> list = new ArrayList<NavieAccount>();
	try {
	    conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME)
		    .getConnection();
	    String sql = "select a.ID,a.Name,b.Uid,b.Platform,b.DisplayName from Office_Navies a inner join Office_Navies_Account b on a.ID=b.NavieID where a.CustomID=?";
	    pstmt = conn.prepareStatement(sql);
	    pstmt.setInt(1, customid);
	    rs = pstmt.executeQuery();
	    while (rs.next()) {
		NavieAccount na = new NavieAccount();
		Navie navie = new Navie();
		navie.setId(rs.getInt("ID"));
		navie.setName(rs.getString("Name"));
		na.setNavie(navie);
		na.setUid(rs.getString("Uid"));
		na.setPlatform(rs.getInt("Platform"));
		na.setDisplayName(rs.getString("DisplayName"));
		list.add(na);
	    }
	} catch (SQLException e) {
	    e.printStackTrace();
	} finally {
	    this.CloseStatus(rs, pstmt, conn);
	}
	return list;
    }

    @Override
    public Navie queryNavieById(int navieid) {
	Connection conn = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	Navie navie = null;
	try {
	    conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME)
		    .getConnection();
	    String sql = "select * from Office_Navies where id=?";
	    pstmt = conn.prepareStatement(sql);
	    pstmt.setInt(1, navieid);
	    rs = pstmt.executeQuery();
	    while (rs.next()) {
		navie = new Navie();
		navie.setId(rs.getInt("ID"));
		navie.setName(rs.getString("Name"));
		navie.setCustomId(rs.getInt("CustomID"));
	    }
	} catch (SQLException e) {
	    e.printStackTrace();
	} finally {
	    this.CloseStatus(rs, pstmt, conn);
	}
	return navie;
    }
}
