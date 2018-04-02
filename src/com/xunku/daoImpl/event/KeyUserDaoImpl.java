package com.xunku.daoImpl.event;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.xunku.constant.PortalCST;
import com.xunku.dao.event.KeyUserDao;
import com.xunku.pojo.event.KeyUser;
import com.xunku.utils.DatabaseUtils;

public class KeyUserDaoImpl implements KeyUserDao{

    @Override
    public void insertKeyUser(KeyUser user) {
	Connection conn = null;
	PreparedStatement pstmt = null;
	try {
	    conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME).getConnection();
	    String sql = "INSERT INTO Event_KeyUser_An (eventId,ucode,followers,weibos,friends) VALUES (?,?,?,?,?)";
	    pstmt = conn.prepareStatement(sql);
	    pstmt.setInt(1, user.getEventId());
	    pstmt.setString(2, user.getUcode());
	    pstmt.setInt(3, user.getFollowers());
	    pstmt.setInt(4, user.getWeibos());
	    pstmt.setInt(5, user.getFriends());
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
	    String sql = "DELETE FROM Event_KeyUser_An WHERE eventId=?";
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
