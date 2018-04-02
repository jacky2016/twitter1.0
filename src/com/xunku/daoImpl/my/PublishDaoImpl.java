package com.xunku.daoImpl.my;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.xunku.app.helpers.DateHelper;
import com.xunku.app.model.Pooling;
import com.xunku.constant.PortalCST;
import com.xunku.dao.my.PublishDao;
import com.xunku.pojo.my.Publish;
import com.xunku.utils.DatabaseUtils;

public class PublishDaoImpl implements PublishDao {

    @Override
    public boolean deleteById(int pid) {
	Connection conn = null;
	PreparedStatement pstmt = null;
	boolean isDelete = false;
	try {
	    conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME).getConnection();
	    String sql = "DELETE FROM My_Publish_An WHERE id=?";
	    pstmt = conn.prepareStatement(sql);
	    pstmt.setInt(1, pid);
	    isDelete = pstmt.execute();
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

    @Override
    public boolean insert(Publish p) {
	Connection conn = null;
	PreparedStatement pstmt = null;
	boolean isInsert = false;
	try {
	    conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME).getConnection();
	    String sql = "INSERT INTO My_Publish_An(sampling,uid,platform,weibos,followers,friends) VALUES(?,?,?,?,?,?)";
	    pstmt = conn.prepareStatement(sql);
	    pstmt.setTimestamp(1, DateHelper.getSqlTime(p.getSampling()));
	    pstmt.setString(2, p.getUid());
	    pstmt.setInt(3, p.getPlatform());
	    pstmt.setInt(4, p.getWeibos());
	    pstmt.setInt(5, p.getFollowers());
	    pstmt.setInt(6, p.getFriends());
	    isInsert = pstmt.execute();
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
	return isInsert;
    }

    @Override
    public List<Publish> queryPubLishList(Date startTime,Date endTime,String uid,Pooling pool) {
	Connection conn = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	List<Publish> list = new ArrayList<Publish>();
	try {
	    conn = pool.getConnection();
	    String sql = "select * from My_Publish_An where uid=? and sampling between ? and ?";
	    pstmt = conn.prepareStatement(sql);
	    pstmt.setString(1, uid);
	    pstmt.setTimestamp(2, DateHelper.getSqlTime(startTime));
	    pstmt.setTimestamp(3, DateHelper.getSqlTime(endTime));
	    rs = pstmt.executeQuery();
	    while(rs.next()){
		Publish pub = new Publish();
		pub.setId(rs.getInt("id"));
		pub.setSampling((java.util.Date)rs.getTimestamp("sampling"));
		pub.setUid(rs.getString("uid"));
		pub.setPlatform(rs.getInt("platform"));
		pub.setWeibos(rs.getInt("weibos"));
		pub.setFollowers(rs.getInt("followers"));
		pub.setFriends(rs.getInt("friends"));
		list.add(pub);
	    }
	} catch (SQLException e) {
	    e.printStackTrace();
	} finally {
	    this.CloseStatus(rs, pstmt, conn);
	}
	return list;
    }

    @Override
    public boolean update(Publish p) {
	Connection conn = null;
	PreparedStatement pstmt = null;
	boolean isUpdate = false;
	try {
	    conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME).getConnection();
	    String sql = "UPDATE My_Publish_An SET sampling=?,uid=?,platform=?,weibos=?,followers=?,friends=? WHERE id=?";
	    pstmt = conn.prepareStatement(sql);
	    pstmt.setDate(1, (java.sql.Date)p.getSampling());
	    pstmt.setString(2, p.getUid());
	    pstmt.setInt(3, p.getPlatform());
	    pstmt.setInt(4, p.getWeibos());
	    pstmt.setInt(5, p.getFollowers());
	    pstmt.setInt(6, p.getFriends());
	    pstmt.setInt(7, p.getId());
	    int flag = pstmt.executeUpdate();
	    if(flag > 0){
		isUpdate = true;
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
	return isUpdate;
    }

    private void CloseStatus(ResultSet rs, PreparedStatement pstmt,Connection conn){
	this.CloseStatus(null,null,rs,null,pstmt,conn);
    }

    private void CloseStatus(ResultSet rs3, ResultSet rs2, ResultSet rs,
	    CallableStatement cstmt,PreparedStatement pstmt, Connection conn) {

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
}
