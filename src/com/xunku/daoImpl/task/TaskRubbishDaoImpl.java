package com.xunku.daoImpl.task;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.xunku.constant.PortalCST;
import com.xunku.dao.task.TaskRubbishDao;
import com.xunku.dto.task.RubbishDTO;
import com.xunku.pojo.task.Rubbish;
import com.xunku.utils.DatabaseUtils;

public class TaskRubbishDaoImpl implements TaskRubbishDao {

    @Override
    public void insert(Rubbish r) {
	Connection conn = null;
	PreparedStatement pstmt = null;
	try {
	    conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME)
		    .getConnection();
	    String sql = "INSERT INTO Task_Rubbish (CustomID,GroupName,RubbishWords) VALUES (?,?,?)";
	    pstmt = conn.prepareStatement(sql);
	    pstmt.setInt(1, r.getCustomID());
	    pstmt.setString(2, r.getGroupName());
	    pstmt.setString(3, r.getRubbishWords());
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
    public List<RubbishDTO> queryByCustom(int customId) {
	Connection conn = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	List<RubbishDTO> list = new ArrayList<RubbishDTO>();
	try {
	    conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME)
		    .getConnection();
	    String sql = "SELECT ID,GroupName,RubbishWords FROM Task_Rubbish WHERE CustomID=?";
	    pstmt = conn.prepareStatement(sql);
	    pstmt.setInt(1, customId);
	    rs = pstmt.executeQuery();
	    while (rs.next()) {
		RubbishDTO dto = new RubbishDTO();
		dto.setId(rs.getInt("ID"));
		dto.setGroupName(rs.getString("GroupName"));
		dto.setRubbishWords(rs.getString("RubbishWords"));
		list.add(dto);
	    }
	} catch (SQLException e) {
	    e.printStackTrace();
	} finally {
	    try {
		if (pstmt != null) {
		    pstmt.close();
		}
		if (rs != null) {
		    rs.close();
		}
		if (conn != null) {
		    conn.close();
		}
	    } catch (SQLException e) {
		e.printStackTrace();
	    }
	}
	return list;
    }

    @Override
    public void update(Rubbish r) {
	Connection conn = null;
	PreparedStatement pstmt = null;
	try {
	    conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME)
		    .getConnection();
	    String sql = "UPDATE Task_Rubbish SET GroupName=?,RubbishWords=? WHERE ID=?";
	    pstmt = conn.prepareStatement(sql);
	    pstmt.setString(1, r.getGroupName());
	    pstmt.setString(2, r.getRubbishWords());
	    pstmt.setInt(3, r.getId());
	    pstmt.executeUpdate();
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
    public void deleteByGID(int gid) {
	Connection conn = null;
	PreparedStatement pstmt = null;
	try {
	    conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME)
		    .getConnection();
	    String sql = "DELETE FROM Task_Rubbish WHERE ID=?";
	    pstmt = conn.prepareStatement(sql);
	    pstmt.setInt(1, gid);
	    pstmt.executeUpdate();
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
    public boolean checkIsExsit(int customid, String group) {
	Connection conn = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	boolean isExsit = false;
	try {
	    conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME)
		    .getConnection();
	    String sql = "select ID from Task_Rubbish where CustomID=? and GroupName=?";
	    pstmt = conn.prepareStatement(sql);
	    pstmt.setInt(1, customid);
	    pstmt.setString(2, group);
	    rs = pstmt.executeQuery();
	    if (rs.next()) {
		isExsit = true;
	    }
	} catch (SQLException e) {
	    e.printStackTrace();
	} finally {
	    try {
		if (pstmt != null) {
		    pstmt.close();
		}
		if (rs != null) {
		    rs.close();
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

    @Override
    public int getRubbishGroupCount(int customid) {
	Connection conn = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	int count = 0;
	try {
	    conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME)
		    .getConnection();
	    String sql = "select COUNT(ID) as count from Task_Rubbish where CustomID=?";
	    pstmt = conn.prepareStatement(sql);
	    pstmt.setInt(1, customid);
	    rs = pstmt.executeQuery();
	    if (rs.next()) {
		count = rs.getInt("count");
	    }
	} catch (SQLException e) {
	    e.printStackTrace();
	} finally {
	    try {
		if (pstmt != null) {
		    pstmt.close();
		}
		if (rs != null) {
		    rs.close();
		}
		if (conn != null) {
		    conn.close();
		}
	    } catch (SQLException e) {
		e.printStackTrace();
	    }
	}
	return count;
    }

}
