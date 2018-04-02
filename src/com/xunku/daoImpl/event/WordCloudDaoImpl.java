package com.xunku.daoImpl.event;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.xunku.constant.PortalCST;
import com.xunku.dao.event.WordCloudDao;
import com.xunku.pojo.event.WordCloud;
import com.xunku.utils.DatabaseUtils;

public class WordCloudDaoImpl implements WordCloudDao{

    @Override
    public void insert(WordCloud wc) {
	Connection conn = null;
	PreparedStatement pstmt = null;
	try {
	    conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME).getConnection();
	    String sql = "INSERT INTO Event_WordCloud (eventId,keyword,count) VALUES (?,?,?)";
	    pstmt = conn.prepareStatement(sql);
	    pstmt.setInt(1, wc.getEventId());
	    pstmt.setString(2, wc.getKeyword());
	    pstmt.setInt(3, wc.getCount());
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
    public List<WordCloud> queryWordCloudList(int eventId) {
	Connection conn = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	List<WordCloud> list = new ArrayList<WordCloud>();
	try {
	    conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME).getConnection();
	    String sql = "SELECT top 20 *  FROM Event_WordCloud WHERE eventId=? ORDER BY count DESC";
	    pstmt = conn.prepareStatement(sql);
	    pstmt.setInt(1, eventId);
	    rs = pstmt.executeQuery();
	    while(rs.next()){
		WordCloud wc = new WordCloud();
		wc.setId(rs.getInt("id"));
		wc.setEventId(rs.getInt("eventId"));
		wc.setKeyword(rs.getString("keyword"));
		wc.setCount(rs.getInt("count"));
		wc.setLastupdated(rs.getString("lastupdated"));
		list.add(wc);
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
    public boolean deleteByEvent(int eventId) {
	Connection conn = null;
	PreparedStatement pstmt = null;
	boolean isDelete = false;
	try {
	    conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME).getConnection();
	    String sql = "DELETE FROM Event_WordCloud WHERE eventId=?";
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
