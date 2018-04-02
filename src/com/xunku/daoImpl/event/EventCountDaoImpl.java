package com.xunku.daoImpl.event;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.xunku.constant.PortalCST;
import com.xunku.dao.event.EventCountDao;
import com.xunku.pojo.event.EventCount;
import com.xunku.utils.DatabaseUtils;

public class EventCountDaoImpl implements EventCountDao {

    @Override
    public void insert(EventCount count) {
	Connection conn = null;
	PreparedStatement pstmt = null;
	try {
	    conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME).getConnection();
	    String sql = "INSERT INTO Event_Count_An (eventId,posts,reposts,females,males,unsex,vip) VALUES(?,?,?,?,?,?,?)";
	    pstmt = conn.prepareStatement(sql);
	    pstmt.setInt(1, count.getEventId());
	    pstmt.setInt(2, count.getPosts());
	    pstmt.setInt(3, count.getReposts());
	    pstmt.setInt(4, count.getFemales());
	    pstmt.setInt(5, count.getMales());
	    pstmt.setInt(6, count.getUnsex());
	    pstmt.setInt(7, count.getVip());
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
    public boolean deleteByEvent(int eventId) {
	Connection conn = null;
	PreparedStatement pstmt = null;
	boolean isDelete = false;
	try {
	    conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME).getConnection();
	    String sql = "DELETE FROM Event_Count_An WHERE eventId=?";
	    pstmt = conn.prepareStatement(sql);
	    pstmt.setInt(1, eventId);
	    int flag = pstmt.executeUpdate();
	    if(flag > 0){
		isDelete = true;
	    }else{
		isDelete = false;
	    }
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
	return isDelete;
    }

}
