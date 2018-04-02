package com.xunku.daoImpl.base;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.xunku.app.Utility;
import com.xunku.app.model.App;
import com.xunku.constant.PortalCST;
import com.xunku.dao.base.AppDao;
import com.xunku.utils.DatabaseUtils;

public class AppDaoImpl implements AppDao {

    @Override
    public App queryById(int appid) {
	Connection conn = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	App app = null;
	try {
	    conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME)
		    .getConnection();
	    String sql = "SELECT * FROM Base_Apps WHERE id=?";
	    pstmt = conn.prepareStatement(sql);
	    pstmt.setInt(1, appid);
	    rs = pstmt.executeQuery();
	    while (rs.next()) {
		app = this.getApp(rs);
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
	return app;
    }
    private App getApp(ResultSet rs) throws SQLException{
	App app = new App();
	app.setId(rs.getInt("id"));
	app.setPlatform(Utility.getPlatform(rs.getInt("platform")));
	app.setName(rs.getString("appName"));
	app.setKey(rs.getString("appkey"));
	app.setSecret(rs.getString("appSecret"));
	app.setType(Utility.getAppType(rs.getInt("type")));
	app.setCallbackUrl(rs.getString("callbackUrl"));
	return app;
    }
    @Override
    public App queryByAppkey(String appkey) {
	Connection conn = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	App app = null;
	try {
	    conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME)
		    .getConnection();
	    String sql = "SELECT * FROM Base_Apps WHERE appKey=?";
	    pstmt = conn.prepareStatement(sql);
	    pstmt.setString(1, appkey);
	    rs = pstmt.executeQuery();
	    while (rs.next()) {
		app = this.getApp(rs);
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
	return app;
    }

}
