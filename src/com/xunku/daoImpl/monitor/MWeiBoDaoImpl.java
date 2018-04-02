package com.xunku.daoImpl.monitor;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.xunku.app.Utility;
import com.xunku.app.helpers.DateHelper;
import com.xunku.app.monitor.WeiboMonitor;
import com.xunku.constant.PortalCST;
import com.xunku.dao.monitor.MWeiBoDao;
import com.xunku.dto.PagerDTO;
import com.xunku.utils.DatabaseUtils;
import com.xunku.utils.Pagefile;

public class MWeiBoDaoImpl implements MWeiBoDao {

	@Override
	public WeiboMonitor queryMWeiboById(int weiboid) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		WeiboMonitor wm = null;
		try {
			conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME)
					.getConnection();
			String sql = "SELECT * FROM vi_weibolist_custom WHERE id=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, weiboid);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				wm = getWeiboMonitor(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.CloseStatus(rs, pstmt, conn);
		}
		return wm;
	}

	private WeiboMonitor getWeiboMonitor(ResultSet rs) throws SQLException {
		WeiboMonitor mw = new WeiboMonitor();
		mw.setId(rs.getInt("id"));
		mw.setTid(rs.getString("tid"));
		mw.setCustomid(rs.getInt("customId"));
		mw.setUrl(rs.getString("url"));
		mw.setUcode(rs.getString("ucode"));
		mw.setText(rs.getString("text"));
		mw.setReposts(rs.getInt("reposts"));
		mw.setComments(rs.getInt("comments"));
		mw.setAnayTime((java.util.Date) rs.getTimestamp("anayTime"));
		mw.setPlatform(Utility.getPlatform(rs.getInt("platform")));
		mw.setDbserver(rs.getString("name"));// change by wujian
		mw.setTable(rs.getString("tab"));
		mw
				.setPublishTime(DateHelper.getSqlTime(rs
						.getTimestamp("publishTime")));
		mw.setAnalysised(rs.getBoolean("analysised"));
		mw.setTries(rs.getInt("tries"));// add by wujian 
		mw.setPoolId(rs.getInt("dbserver"));// add by wujian
		return mw;
	}

	@Override
	public boolean deleteById(int weiboid, int customid) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		boolean isDelete = false;
		try {
			conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME)
					.getConnection();
			String sql = "DELETE FROM Weibo_Custom WHERE weiboId=? AND customId=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, weiboid);
			pstmt.setInt(2, customid);
			int flag = pstmt.executeUpdate();
			if (flag > 0) {
				isDelete = true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.CloseStatus(pstmt, conn);
		}
		return isDelete;
	}

	@Override
	public int insert(WeiboMonitor mw) {
		Connection conn = null;
		CallableStatement cstmt = null;
		int mwId = 0;
		try {
			conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME)
					.getConnection();
			cstmt = conn
					.prepareCall("{call sp_Weibolist_insert(?,?,?,?,?,?,?,?,?,?,?)}");
			cstmt.setString(1, mw.getTid());
			cstmt.setInt(2, mw.getCustomid());
			cstmt.setString(3, mw.getUrl());
			cstmt.setString(4, mw.getUcode());
			cstmt.setString(5, mw.getText());
			cstmt.setInt(6, mw.getReposts());
			cstmt.setInt(7, mw.getComments());
			cstmt.setInt(8, Utility.getPlatform(mw.getPlatform()));
			cstmt.setTimestamp(9, DateHelper.getSqlTime(mw.getPublishTime()));
			cstmt.setBoolean(10, mw.isAnalysised());
			cstmt.registerOutParameter(11, java.sql.Types.INTEGER);
			cstmt.execute();
			mwId = cstmt.getInt(11);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.CloseStatus(cstmt, conn);
		}
		return mwId;
	}

	@Override
	public List<WeiboMonitor> queryWaitAnalysis() {
		Connection conn = null;
		CallableStatement cstmt = null;
		ResultSet rs = null;
		ResultSet rs2 = null;
		List<WeiboMonitor> pagefile = null;
		try {
			conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME)
					.getConnection();
			cstmt = conn
					.prepareCall("select a.*,b.name from Weibo_List a inner join Base_DBServer b on a.dbserver=b.id where a.analysised=0");
			cstmt.execute();
			rs = cstmt.getResultSet();
			pagefile = new ArrayList<WeiboMonitor>();
			while (rs.next()) {
				WeiboMonitor mw = new WeiboMonitor();
				mw.setId(rs.getInt("id"));
				mw.setTid(rs.getString("tid"));
				//mw.setCustomid(rs.getInt("customId")); 分析时不需要这个字段
				mw.setUrl(rs.getString("url"));
				mw.setUcode(rs.getString("ucode"));
				mw.setText(rs.getString("text"));
				mw.setReposts(rs.getInt("reposts"));
				mw.setComments(rs.getInt("comments"));
				mw.setAnayTime((java.util.Date) rs.getTimestamp("anayTime"));
				mw.setPlatform(Utility.getPlatform(rs.getInt("platform")));
				mw.setDbserver(rs.getString("name"));// change by wujian
				//mw.setTable(rs.getString("tab"));
				mw
						.setPublishTime(DateHelper.getSqlTime(rs
								.getTimestamp("publishTime")));
				mw.setAnalysised(rs.getBoolean("analysised"));
				mw.setTries(rs.getInt("tries"));// add by wujian 
				pagefile.add(mw);
			}
			return pagefile;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.CloseStatus(rs2, rs, cstmt, conn);
		}
		return pagefile;
	}

	@Override
	public Pagefile<WeiboMonitor> queryWeiboList(PagerDTO dto, int customId) {
		Connection conn = null;
		CallableStatement cstmt = null;
		ResultSet rs = null;
		ResultSet rs2 = null;
		Pagefile<WeiboMonitor> pagefile = null;
		try {
			conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME)
					.getConnection();
			cstmt = conn.prepareCall("{call SupesoftPage(?,?,?,?,?,?,?)}");
			cstmt.setString(1, "vi_weibolist_custom");
			cstmt.setString(2, "*");
			cstmt.setInt(3, dto.pageSize);
			cstmt.setInt(4, dto.pageIndex);
			cstmt.setString(5, "where customId=" + customId);
			cstmt.setString(6, "id");
			cstmt.setInt(7, 1);
			cstmt.execute();
			rs = cstmt.getResultSet();
			pagefile = new Pagefile<WeiboMonitor>();
			while (rs.next()) {
				pagefile.getRows().add(this.getWeiboMonitor(rs));
			}
			if (cstmt.getMoreResults()) {
				rs2 = cstmt.getResultSet();
				if (rs2.next()) {
					pagefile.setRealcount(rs2.getInt("RecordCount"));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.CloseStatus(rs2, rs, cstmt, conn);
		}
		return pagefile;
	}

	@Override
	public List<WeiboMonitor> queryMWeiboList() {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<WeiboMonitor> list = new ArrayList<WeiboMonitor>();
		try {
			conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME)
					.getConnection();
			String sql = "SELECT * FROM vi_weibolist_custom";
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				list.add(this.getWeiboMonitor(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.CloseStatus(rs, pstmt, conn);
		}
		return list;
	}

	@Override
	public WeiboMonitor queryMWeiboByTid(String tid, int platform) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		WeiboMonitor mw = null;
		try {
			conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME)
					.getConnection();
			String sql = "SELECT * FROM vi_weibolist_custom WHERE tid=? AND platform=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, tid);
			pstmt.setInt(2, platform);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				mw = this.getWeiboMonitor(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.CloseStatus(rs, pstmt, conn);
		}
		return mw;
	}

	@Override
	public boolean checkUrlIsExists(String url, int customId) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		boolean isExists = false;
		try {
			conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME)
					.getConnection();
			String sql = "SELECT id FROM vi_weibolist_custom WHERE url=? AND customId=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, url);
			pstmt.setInt(2, customId);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				int id = rs.getInt("id");
				if (id > 0) {
					isExists = true;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.CloseStatus(rs, pstmt, conn);
		}
		return isExists;
	}

	@Override
	public Map<String, Integer> checkWeiboList(List<String> tids, int customId) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Map<String, Integer> map = new HashMap<String, Integer>();
		try {
			conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME)
					.getConnection();
			String sql = "SELECT id FROM vi_weibolist_custom WHERE customId=? AND tid=?";
			for (String tid : tids) {
				pstmt = conn.prepareStatement(sql);
				pstmt.setInt(1, customId);
				pstmt.setString(2, tid);
				rs = pstmt.executeQuery();
				while (rs.next()) {
					int cid = rs.getInt("id");
					map.put(tid, cid);
				}
			}
			for (String tid : tids) {
				if (!map.containsKey(tid)) {
					map.put(tid, 0);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.CloseStatus(rs, pstmt, conn);
		}
		return map;
	}

	@Override
	public int queryWeiboListTotal(int customId) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int count = 0;
		try {
			conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME)
					.getConnection();
			String sql = "SELECT COUNT(id) as count FROM vi_weibolist_custom WHERE customId=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, customId);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				count = rs.getInt("count");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.CloseStatus(rs, pstmt, conn);
		}
		return count;
	}

	private void CloseStatus(ResultSet rs, PreparedStatement pstmt,
			Connection conn) {
		this.CloseStatus(null, null, rs, null, pstmt, conn);
	}

	private void CloseStatus(CallableStatement cstmt, Connection conn) {
		this.CloseStatus(null, null, cstmt, conn);
	}

	private void CloseStatus(PreparedStatement pstmt, Connection conn) {
		this.CloseStatus(null, pstmt, conn);
	}

	private void CloseStatus(ResultSet rs2, ResultSet rs,
			CallableStatement cstmt, Connection conn) {
		this.CloseStatus(null, rs2, rs, cstmt, null, conn);
	}

	private void CloseStatus(ResultSet rs3, ResultSet rs2, ResultSet rs,
			CallableStatement cstmt, PreparedStatement pstmt, Connection conn) {

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

	@Override
	public boolean updateMWeiboById(int weiboid, boolean analysised) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		boolean isUpdate = false;
		try {
			conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME)
					.getConnection();
			String sql = "update Weibo_List set analysised=? where id=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setBoolean(1, analysised);
			pstmt.setInt(2, weiboid);
			int flag = pstmt.executeUpdate();
			if (flag > 0) {
				isUpdate = true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.CloseStatus(null, pstmt, conn);
		}
		return isUpdate;
	}
}
