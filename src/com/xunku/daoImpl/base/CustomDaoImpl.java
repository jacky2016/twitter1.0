package com.xunku.daoImpl.base;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.xunku.app.Utility;
import com.xunku.app.helpers.DateHelper;
import com.xunku.constant.PortalCST;
import com.xunku.dao.base.CustomDao;
import com.xunku.pojo.base.Custom;
import com.xunku.utils.DatabaseUtils;

public class CustomDaoImpl implements CustomDao {

	@Override
	public boolean deleteById(int cid) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		boolean isDel = false;
		try {
			conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME)
					.getConnection();
			pstmt = conn.prepareStatement("DELETE FROM Custom WHERE ID=?");
			pstmt.setInt(1, cid);
			int flag = pstmt.executeUpdate();
			if (flag > 0) {
				return isDel = true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			Utility.closeConnection(conn, pstmt, null);
		}
		return isDel;
	}

	@Override
	public void insertCustom(Custom c) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		try {
			conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME)
					.getConnection();
			pstmt = conn
					.prepareStatement("INSERT INTO Base_Customs (Name,Code,Contact,Tele,Address,BannerAddress,ExpirationDate,ERPID) VALUES(?,?,?,?,?,?,?,?)");
			pstmt.setString(1, c.getName());
			pstmt.setString(2, c.getCode());
			pstmt.setString(3, c.getContact());
			pstmt.setString(4, c.getTel());
			pstmt.setString(5, c.getAddress());
			pstmt.setString(6, c.getBannerAddress());
			pstmt.setTimestamp(7, DateHelper.getSqlTime(c.getExpirationDate()));
			pstmt.setInt(8, c.getErpID());
			pstmt.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			Utility.closeConnection(conn, pstmt, null);
		}
	}

	@Override
	public Custom queryById(int cid) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Custom cm = null;
		try {
			conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME)
					.getConnection();
			pstmt = conn
					.prepareStatement("SELECT a.ID,a.Name,a.Code,a.Contact,a.Tele,a.Address,a.BannerAddress,a.CreateTime,a.IsEnabled,a.ExpirationDate,a.ERPID,b.name as dbname FROM Base_Customs a JOIN Base_DBServer b ON a.dbserver=b.id WHERE a.ID=?");
			pstmt.setInt(1, cid);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				cm = this.getCustom(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			Utility.closeConnection(conn, pstmt, rs);
		}
		return cm;
	}

	private Custom getCustom(ResultSet rs) throws SQLException {
		Custom cm = new Custom();
		cm.setId(rs.getInt("ID"));
		cm.setName(rs.getString("Name"));
		cm.setCode(rs.getString("Code"));
		cm.setContact(rs.getString("Contact"));
		cm.setTel(rs.getString("Tele"));
		cm.setAddress(rs.getString("Address"));
		cm.setBannerAddress(rs.getString("BannerAddress"));
		cm
				.setExpirationDate((java.util.Date) rs
						.getTimestamp("ExpirationDate"));
		cm.setErpID(rs.getInt("ERPID"));
		cm.setCreateTime((java.util.Date) rs.getTimestamp("CreateTime"));
		cm.setEnabled(rs.getBoolean("IsEnabled"));
		cm.setDbserver(rs.getString("dbname"));
		return cm;
	}

	@Override
	public String queryByUid(int uid) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String result = null;
		try {
			conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME)
					.getConnection();
			String sql = "select a.BannerAddress from Base_Customs a join Base_Users b on a.ID=b.CustomID where b.ID=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, uid);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				result = rs.getString("BannerAddress");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			Utility.closeConnection(conn, pstmt, rs);
		}
		return result;
	}

	@Override
	public List<Custom> queryAll() {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<Custom> list = new ArrayList<Custom>();
		try {
			conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME)
					.getConnection();
			pstmt = conn
					.prepareStatement("SELECT a.ID,a.Name,a.Code,a.Contact,a.Tele,a.Address,a.BannerAddress,a.CreateTime,a.IsEnabled,a.ExpirationDate,a.ERPID,b.name as dbname FROM Base_Customs a JOIN Base_DBServer b ON a.dbserver=b.id WHERE a.IsEnabled=1");
			rs = pstmt.executeQuery();
			while (rs.next()) {
				list.add(this.getCustom(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			Utility.closeConnection(conn, pstmt, rs);
		}
		return list;
	}

}
