package com.xunku.daoImpl.home;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.xunku.constant.PortalCST;
import com.xunku.dao.home.WeiboInfoDao;
import com.xunku.pojo.home.WeiboInfo;
import com.xunku.utils.DatabaseUtils;

public class WeiboInfoDaoImpl implements WeiboInfoDao {

    @Override
    public List<WeiboInfo> queryByUserID(int userId) {
	Connection conn = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	List<WeiboInfo> list = new ArrayList<WeiboInfo>();
	try {
	    conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME).getConnection();
	    String sql = "SELECT id,accountId,closed,position,expand FROM My_Account WHERE userId=? order by position";
	    pstmt = conn.prepareStatement(sql);
	    pstmt.setInt(1, userId);
	    rs = pstmt.executeQuery();
	    while (rs.next()) {
		WeiboInfo wi = new WeiboInfo();
		wi.setId(rs.getInt("id"));
		wi.setAccountID(rs.getInt("accountId"));
		wi.setUserID(userId);
		wi.setClosed(rs.getBoolean("closed"));
		wi.setPosition(rs.getInt("position"));
		wi.setExpand(rs.getBoolean("expand"));
		list.add(wi);
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
    public WeiboInfo queryByAID(int aid) {
	Connection conn = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	WeiboInfo wi = null;
	try {
	    conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME).getConnection();
	    String sql = "SELECT id,accountId,closed,position,expand FROM My_Account WHERE ID=?";
	    pstmt = conn.prepareStatement(sql);
	    pstmt.setInt(1, aid);
	    rs = pstmt.executeQuery();
	    while (rs.next()) {
		wi = new WeiboInfo();
		wi.setId(rs.getInt("id"));
		wi.setAccountID(rs.getInt("accountId"));
		//wi.setUserID(rs.getInt());
		wi.setClosed(rs.getBoolean("closed"));
		wi.setPosition(rs.getInt("position"));
		wi.setExpand(rs.getBoolean("expand"));
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
	return wi;
    }

    @Override
    public void updateClose(int id, boolean isClose) {
	Connection conn = null;
	PreparedStatement pstmt = null;
	try {
	    conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME).getConnection();
	    String sql = "UPDATE My_Account SET closed=? WHERE id=?";
	    pstmt = conn.prepareStatement(sql);
	    pstmt.setBoolean(1, isClose);
	    pstmt.setInt(2, id);
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

    @Override
    public void updateExpand(int id, boolean isExpand) {
	Connection conn = null;
	PreparedStatement pstmt = null;
	try {
	    conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME).getConnection();
	    String sql = "UPDATE My_Account SET expand=? WHERE id=?";
	    pstmt = conn.prepareStatement(sql);
	    pstmt.setBoolean(1, isExpand);
	    pstmt.setInt(2, id);
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
