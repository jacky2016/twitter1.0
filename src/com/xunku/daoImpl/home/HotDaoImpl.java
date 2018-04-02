package com.xunku.daoImpl.home;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.xunku.constant.PortalCST;
import com.xunku.dao.home.HotDao;
import com.xunku.pojo.home.Hot;
import com.xunku.utils.DatabaseUtils;

public class HotDaoImpl implements HotDao {

    @Override
    public void insert(Hot hot) {
	Connection conn = null;
	PreparedStatement pstmt = null;
	try {
	    conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME).getConnection();
	    String sql = "INSERT INTO Home_Hots (Num,Text,Link,Color,Date,Platform) VALUES (?,?,?,?,?,?)";
	    pstmt = conn.prepareStatement(sql);
	    pstmt.setInt(1, hot.getNum());
	    pstmt.setString(2, hot.getText());
	    pstmt.setString(3, hot.getLink());
	    pstmt.setInt(4, hot.getColor());
	    pstmt.setString(5, hot.getDate());
	    pstmt.setInt(6, hot.getPlatform());
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
    public List<Hot> queryByPlatform(int platform, Date date) {
	Connection conn = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	List<Hot> list = new ArrayList<Hot>();
	try {
	    conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME).getConnection();
	    String sql = "SELECT * FROM Home_Hots WHERE Platform=? AND Date=? ";
	    pstmt = conn.prepareStatement(sql);
	    pstmt.setInt(1, platform);
	    pstmt.setDate(2, new java.sql.Date(date.getTime()));
	    rs = pstmt.executeQuery();
	    while(rs.next()){
		list.add(this.getHot(rs));
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
    public void update(Hot hot) {
	Connection conn = null;
	PreparedStatement pstmt = null;
	try {
	    conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME).getConnection();
	    String sql = "UPDATE Home_Hots SET Num=?,Text=?,Link=?,Color=?,Date=?,Platform=? WHERE ID=?";
	    pstmt = conn.prepareStatement(sql);
	    pstmt.setInt(1, hot.getNum());
	    pstmt.setString(2, hot.getText());
	    pstmt.setString(3, hot.getLink());
	    pstmt.setInt(4, hot.getColor());
	    pstmt.setString(5, hot.getDate());
	    pstmt.setInt(6, hot.getPlatform());
	    pstmt.setInt(7, hot.getId());
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
    private Hot getHot(ResultSet rs) throws SQLException{
	Hot hot = new Hot();
	hot.setId(rs.getInt("ID"));
	hot.setNum(rs.getInt("Num"));
	hot.setText(rs.getString("Text"));
	hot.setLink(rs.getString("Link"));
	hot.setColor(rs.getInt("Color"));
	hot.setDate(rs.getString("Date"));
	hot.setPlatform(rs.getInt("Platform"));
	return hot;
    }
    // @Override
    public List<Hot> queryByPlatform(int platform) {
	Connection conn = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	List<Hot> list = new ArrayList<Hot>();
	try {
	    conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME)
		    .getConnection();
	    pstmt = conn
		    .prepareStatement("SELECT ID,Num,Text,Link,Color,Date,Platform FROM Home_Hots WHERE Platform=?");
	    pstmt.setInt(1, platform);
	    rs = pstmt.executeQuery();
	    while (rs.next()) {
		list.add(this.getHot(rs));
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
	return list;
    }

}
