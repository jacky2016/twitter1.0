package com.xunku.daoImpl.task;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.xunku.app.helpers.DateHelper;
import com.xunku.constant.PortalCST;
import com.xunku.dao.task.TaskCountDao;
import com.xunku.pojo.task.TaskCount;
import com.xunku.utils.DatabaseUtils;

public class TaskCountDaoImpl implements TaskCountDao {

    @Override
    public void delete(int taskCntId) {
	Connection conn = null;
	PreparedStatement pstmt = null;
	try {
	    conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME).getConnection();
	    String sql = "DELETE FROM Task_Count_An WHERE id=?";
	    pstmt = conn.prepareStatement(sql);
	    pstmt.setInt(1, taskCntId);
	    pstmt.execute();
	} catch (SQLException e) {
	    e.printStackTrace();
	}finally{
	    try {
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
    }

    @Override
    public void insert(TaskCount taskCnt) {
	Connection conn = null;
	PreparedStatement pstmt = null;
	try {
	    conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME).getConnection();
	    String sql = "INSERT INTO Task_Count_An (customId,sina,tencent,renmin,created,updated) VALUES (?,?,?,?,?,?)";
	    pstmt = conn.prepareStatement(sql);
	    pstmt.setInt(1, taskCnt.getCustomId());
	    pstmt.setInt(2, taskCnt.getSina());
	    pstmt.setInt(3, taskCnt.getTencent());
	    pstmt.setInt(4, taskCnt.getRenmin());
	    pstmt.setTimestamp(5, DateHelper.getSqlTime(taskCnt.getCreated()));
	    pstmt.setTimestamp(6, DateHelper.getSqlTime(taskCnt.getUpdated()));
	    pstmt.execute();
	} catch (SQLException e) {
	    e.printStackTrace();
	}finally{
	    try {
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
    }

    @Override
    public TaskCount queryCountByCustomID(int customId, Date created) {
	Connection conn = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	TaskCount count = null;
	try {
	    conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME).getConnection();
	    String sql = "SELECT * FROM Task_Count_An WHERE customId=? AND created=?";
	    pstmt = conn.prepareStatement(sql);
	    pstmt.setInt(1, customId);
	    pstmt.setTimestamp(2, DateHelper.getSqlTime(created));
	    rs = pstmt.executeQuery();
	    while(rs.next()){
		count = this.getTaskCount(rs);
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
	return count;
    }
    private TaskCount getTaskCount(ResultSet rs) throws SQLException{
	TaskCount count = new TaskCount();
	count.setId(rs.getInt("id"));
	count.setCustomId(rs.getInt("customId"));
	count.setSina(rs.getInt("sina"));
	count.setTencent(rs.getInt("tencent"));
	count.setRenmin(rs.getInt("renmin"));
	count.setCreated(DateHelper.getSqlTime(rs.getDate("created")));
	count.setUpdated(DateHelper.getSqlTime(rs.getTimestamp("updated")));
	return count;
    }
    @Override
    public TaskCount queryCountByID(int cntId) {
	Connection conn = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	TaskCount count = null;
	try {
	    conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME).getConnection();
	    String sql = "SELECT * FROM Task_Count_An WHERE id=?";
	    pstmt = conn.prepareStatement(sql);
	    pstmt.setInt(1, cntId);
	    rs = pstmt.executeQuery();
	    while(rs.next()){
		count = this.getTaskCount(rs);
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
	return count;
    }

    @Override
    public List<TaskCount> queryCountsByCustomID(int customId, Date start,
	    Date end) {
	Connection conn = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	List<TaskCount> list = new ArrayList<TaskCount>();
	try {
	    conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME).getConnection();
	    String sql = "SELECT * FROM Task_Count_An WHERE customId=? AND created between ? AND ?";
	    pstmt = conn.prepareStatement(sql);
	    pstmt.setInt(1, customId);
	    pstmt.setTimestamp(2, DateHelper.getSqlTime(start));
	    pstmt.setTimestamp(3, DateHelper.getSqlTime(end));
	    rs = pstmt.executeQuery();
	    while(rs.next()){
		list.add(this.getTaskCount(rs));
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
	return list;
    }

    @Override
    public void update(TaskCount taskCnt) {
	Connection conn = null;
	PreparedStatement pstmt = null;
	try {
	    conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME).getConnection();
	    String sql = "UPDATE Task_Count_An SET customId=?,sina=?,tencent=?,renmin=?,updated=? WHERE id=?";
	    pstmt = conn.prepareStatement(sql);
	    pstmt.setInt(1, taskCnt.getCustomId());
	    pstmt.setInt(2, taskCnt.getSina());
	    pstmt.setInt(3, taskCnt.getTencent());
	    pstmt.setInt(4, taskCnt.getRenmin());
	    pstmt.setTimestamp(5, DateHelper.getSqlTime(taskCnt.getUpdated()));
	    pstmt.setInt(6, taskCnt.getId());
	    pstmt.executeUpdate();
	} catch (SQLException e) {
	    e.printStackTrace();
	}finally{
	    try {
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
    }

}
