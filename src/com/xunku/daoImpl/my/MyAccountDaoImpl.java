package com.xunku.daoImpl.my;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import com.xunku.constant.PortalCST;
import com.xunku.dao.my.MyAccountDao;
import com.xunku.utils.DatabaseUtils;

public class MyAccountDaoImpl implements MyAccountDao {

    @Override
    public boolean insert(int customid,int userid, List<String> uidList) {
	Connection conn = null;
	CallableStatement cstmt = null;
	PreparedStatement pstmt = null;
	boolean isInsert = false;
	try {
	    conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME)
		    .getConnection();
	    conn.setAutoCommit(false);
	    String sql = "delete from My_Account where userId=?";
	    pstmt = conn.prepareStatement(sql);
	    pstmt.setInt(1, userid);
	    pstmt.execute();
	    for (String uid : uidList) {
		cstmt = conn.prepareCall("{call sp_MyAccount_insert(?,?,?)}");
		cstmt.setInt(1, userid);
		cstmt.setString(2, uid);
		cstmt.setInt(3, customid);
		cstmt.execute();
	    }
	    isInsert = true;
	    conn.commit();
	} catch (SQLException e) {
	    e.printStackTrace();
	    try {
		conn.rollback();
		isInsert = false;
	    } catch (SQLException e1) {
		e1.printStackTrace();
	    }
	} finally {
	    try {
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
	return isInsert;
    }

}
