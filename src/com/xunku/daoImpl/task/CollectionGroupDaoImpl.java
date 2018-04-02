package com.xunku.daoImpl.task;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.xunku.constant.PortalCST;
import com.xunku.dao.task.CollectionGroupDao;
import com.xunku.pojo.task.CollectionGroup;
import com.xunku.utils.DatabaseUtils;

public class CollectionGroupDaoImpl implements CollectionGroupDao {

    @Override
    public List<CollectionGroup> queryByAll(int userId) {
	Connection conn = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	List<CollectionGroup> list = null;
	try {
	    conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME).getConnection();
	    String sql = "SELECT ID,GroupName,CustomID FROM Base_Collection_Group WHERE CustomID = (SELECT CustomID FROM Base_Users WHERE ID=?)";
	    pstmt = conn.prepareStatement(sql);
	    pstmt.setInt(1, userId);
	    rs = pstmt.executeQuery();
	    list = new ArrayList<CollectionGroup>();
	    while(rs.next()){
		CollectionGroup cg = new CollectionGroup();
		cg.setId(rs.getInt("ID"));
		cg.setGroupName(rs.getString("GroupName"));
		cg.setCustomId(rs.getInt("CustomID"));
		list.add(cg);
	    }
	} catch (SQLException e) {
	    e.printStackTrace();
	}finally {
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
	return list;
    }

    @Override
    public CollectionGroup insert(CollectionGroup cg) {
	Connection conn = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	try {
	    conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME).getConnection();
	    String sql = "INSERT INTO Base_Collection_Group (GroupName,CustomID) VALUES (?,?)";
	    pstmt = conn.prepareStatement(sql,PreparedStatement.RETURN_GENERATED_KEYS);
	    pstmt.setString(1, cg.getGroupName());
	    pstmt.setInt(2, cg.getCustomId());
	    pstmt.execute();
	    rs = pstmt.getGeneratedKeys();
	    if(rs.next()){
		int gid = rs.getInt(1);
		cg.setId(gid);
	    }
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
	return cg;
    }

    @Override
    public boolean updateByID(CollectionGroup cg) {
	Connection conn = null;
	PreparedStatement pstmt = null;
	boolean isUpdate = false;
	try {
	    conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME).getConnection();
	    String sql = "UPDATE Base_Collection_Group SET GroupName=? WHERE ID=?";
	    pstmt = conn.prepareStatement(sql);
	    pstmt.setString(1, cg.getGroupName());
	    pstmt.setInt(2, cg.getId());
	    int flag = pstmt.executeUpdate();
	    if(flag > 0){
		isUpdate = true;
	    }
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
	return isUpdate;
    }

    @Override
    public boolean deleteGroup(int gid) {
	Connection conn = null;
	PreparedStatement pstmt = null;
	boolean isDelete = false;
	try {
	    conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME).getConnection();
	    conn.setAutoCommit(false);
	    String sql = "DELETE FROM Base_Collection_Posts WHERE id IN (SELECT PostID FROM Base_Collections WHERE GroupID=?) " 
		+"DELETE FROM Base_Collections WHERE GroupID=? "
		+"DELETE FROM Base_Collection_Group WHERE ID=?";
	    pstmt = conn.prepareStatement(sql);
	    pstmt.setInt(1, gid);
	    pstmt.setInt(2, gid);
	    pstmt.setInt(3, gid);
	    int flag = pstmt.executeUpdate();
	    if(flag > 0){
		isDelete = true;
	    }
	    conn.commit();
	} catch (SQLException e) {
	    e.printStackTrace();
	    try {
		conn.rollback();
	    } catch (SQLException e1) {
		e1.printStackTrace();
	    }
	}finally {
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
	return isDelete;
    }

    @Override
    public boolean checkGroupIsExist(int customId,String groupName) {
	Connection conn = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	boolean isCheck = false;
	try {
	    conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME).getConnection();
	    String sql = "SELECT * FROM Base_Collection_Group WHERE GroupName=? AND CustomID=?";
	    pstmt = conn.prepareStatement(sql);
	    pstmt.setString(1, groupName);
	    pstmt.setInt(2, customId);
	    rs = pstmt.executeQuery();
	    if(rs.next()){
		isCheck = true;
	    }
	} catch (SQLException e) {
	    e.printStackTrace();
	}finally{
	    try {
		if(rs != null){
		    rs.close();
		}
		if(pstmt != null){
		    pstmt.close();
		}
		if(conn != null){
		    conn.close();
		}
	    } catch (SQLException e) {
		e.printStackTrace();
	    }
	}
	return isCheck;
    }
    
}
