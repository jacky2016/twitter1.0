package com.xunku.daoImpl.office;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;

import com.xunku.app.Utility;
import com.xunku.app.enums.WarnStatusEnum;
import com.xunku.app.helpers.DateHelper;
import com.xunku.constant.PortalCST;
import com.xunku.constant.WarnType;
import com.xunku.dao.office.WarnListDao;
import com.xunku.pojo.base.Pager;
import com.xunku.pojo.office.WarnElement;
import com.xunku.pojo.office.WeiboWarn;
import com.xunku.utils.DatabaseUtils;
import com.xunku.utils.Pagefile;

public class WarnListDaoImpl implements WarnListDao {

	@Override
	public int getUnreadWarnListCount(int userid) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int result = 0;
		try {
			conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME)
					.getConnection();
			String sql = "select count(id) from Office_WarnList where receiver=?  and  readed=0";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, userid);
			pstmt.execute();
			rs = pstmt.getResultSet();
			if (rs.next()) {
				result = rs.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			Utility.closeConnection(conn, pstmt, null);
		}
		return result;
	}

	@Override
	public int readWarnList(int userid, int[] ids) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		try {
			conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME)
					.getConnection();
			StringBuilder strIds = new StringBuilder();
			if (ids != null && ids.length > 0) {
				strIds.append(ids[0]);
				for (int i = 1; i < ids.length; i++) {
					strIds.append(",");
					strIds.append(ids[i]);
				}
			}

			String sql = "Update Office_WarnList set readed = 1 where receiver=?";
			if (strIds.length() > 0) {
				sql += " and id in (" + strIds + ")";
			}
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, userid);
			return pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			return 0;
		} finally {
			Utility.closeConnection(conn, pstmt, null);
		}
	}
	
	/**
	 * 更新数据库的某一条的未读到已读状态
	 * @param  id    Office_WarnList表中的id
	 * @author shangwei  
	 * */
	public  	int updateReadWarnList(int userid, int id){
		Connection conn = null;
		PreparedStatement pstmt = null;
		int flag=0;
		try {
			conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME)
					.getConnection();

			String sql = "Update Office_WarnList set readed = 1 where  id=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, id);
			flag= pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			Utility.closeConnection(conn, pstmt, null);
		}
		return  flag;
	}
	
	

	public WeiboWarn queryWeiboWarn(int warnListId) {

		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		WeiboWarn result = null;
		try {
			conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME)
					.getConnection();
			String cmdText = "select b.* from Office_WarnList a inner join Office_Weibo_Warning b on a.warnid=b.id where a.warnType=3 and a.id=?";
			pstmt = conn.prepareStatement(cmdText);
			pstmt.setInt(1, warnListId);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				result = this.getWeiboWarn(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			Utility.closeConnection(conn, pstmt, rs);
		}
		return result;
	}

	@Override
	public boolean insert(WarnElement wl) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		boolean isInsert = false;
		try {
			conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME)
					.getConnection();
			String sql = "insert into Office_WarnList(text,warnType,status,customid,created,receiver,warnid) values(?,?,?,?,?,?,?)";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, wl.getText());
			pstmt.setInt(2, wl.getWarnType());
			pstmt.setInt(3, wl.getStatus());
			pstmt.setInt(4, wl.getCustomid());
			pstmt.setTimestamp(5, new Timestamp(new Date().getTime()));
			pstmt.setInt(6, wl.getReciver());
			pstmt.setInt(7, wl.getWarnId());
			int flag = pstmt.executeUpdate();
			if (flag > 0) {
				isInsert = true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			Utility.closeConnection(conn, pstmt, null);
		}
		return isInsert;
	}

	private WarnElement read(ResultSet rs) throws SQLException {
		WarnElement wl = new WarnElement();
		wl.setId(rs.getInt("id"));
		wl.setText(rs.getString("text"));
		wl.setWarnType(rs.getInt("warnType"));
		wl.setStatus(rs.getInt("status"));
		wl.setCustomid(rs.getInt("customid"));
		wl.setReciver(rs.getInt("receiver"));
		wl.setReaded(rs.getInt("readed"));
		// wl.setTid(rs.getString("tid"));
		// wl.setPlatform(Utility.getPlatform(rs.getInt("platform")));
		wl.setCreated(new Date(rs.getTimestamp("created").getTime()));
		return wl;

	}

	public WeiboWarn getWeiboWarn(ResultSet rs) throws SQLException {
		WeiboWarn warn = new WeiboWarn();
		warn.setId(rs.getInt("id"));
		warn.setTid(rs.getString("tid"));
		warn.setPlatform(Utility.getPlatform(rs.getInt("platform")));
		warn.setComment(rs.getInt("comment"));
		warn.setRepost(rs.getInt("repost"));
		warn.setReceiver(rs.getString("receiver"));
		warn.setType(rs.getString("type"));
		warn.setCustomid(rs.getInt("customid"));
		warn.setCreated(new Date(rs.getTimestamp("created").getTime()));
		return warn;
	}
	
	

	@Override
	public Pagefile<WarnElement> queryWarnListPagefile(Pager pager, int userid,
			Date startDate, Date endDate, WarnType type, WarnStatusEnum status) {

		String strStart = DateHelper.formatDBTime(startDate);
		String strEnd = DateHelper.formatDBTime(endDate);
		String sqlWhere = "";
		switch (status) {
		case Warned:
			sqlWhere = " where receiver=" + userid + " and created between '"
					+ strStart + "' and '" + strEnd + "'and status=1"; // and
			// readed=0
			break;
		case Unwarn:
			sqlWhere = " where receiver=" + userid + " and created between '"
					+ strStart + "' and '" + strEnd + "'  and status=0"; // and
			// readed=0
			break;
		default:
			break;
		}
		switch (type) {
		case AccountWarn:
			sqlWhere += " and warnType=1";
			break;
		case EventWarn:
			sqlWhere += " and warnType=2";
			break;
		case WeiboWarn:
			sqlWhere += " and warnType=3";
			break;
		case ALL:
			break;
		default:
			break;
		}

		Connection conn = null;
		CallableStatement cstmt = null;
		ResultSet rs = null;
		ResultSet rs2 = null;
		Pagefile<WarnElement> pagefile = null;
		try {
			conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME)
					.getConnection();
			cstmt = conn.prepareCall("{call SupesoftPage(?,?,?,?,?,?,?)}");
			cstmt.setString(1, "Office_WarnList");
			cstmt.setString(2, "*");
			cstmt.setInt(3, pager.getPageSize());
			cstmt.setInt(4, pager.getPageIndex());
			cstmt.setString(5, sqlWhere);
			cstmt.setString(6, "created");
			cstmt.setInt(7, 1);
			cstmt.execute();
			pagefile = new Pagefile<WarnElement>();
			rs = cstmt.getResultSet();
			while (rs.next()) {
				pagefile.getRows().add(this.read(rs));
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
			Utility.closeConnection(conn, cstmt, rs);
		}
		return pagefile;
	}

	@Override
	public Pagefile<WarnElement> queryWarnListPagefile(Pager pager, int userid) {
		Connection conn = null;
		CallableStatement cstmt = null;
		ResultSet rs = null;
		ResultSet rs2 = null;
		Pagefile<WarnElement> pagefile = null;
		try {
			conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME)
					.getConnection();
			cstmt = conn.prepareCall("{call SupesoftPage(?,?,?,?,?,?,?)}");
			cstmt.setString(1, "Office_WarnList");
			cstmt.setString(2, "*");
			cstmt.setInt(3, pager.getPageSize());
			cstmt.setInt(4, pager.getPageIndex());
			cstmt
					.setString(5, " where receiver =" + userid
							+ " and   status=1");
			cstmt.setString(6, "created");
			cstmt.setInt(7, 1);
			cstmt.execute();
			pagefile = new Pagefile<WarnElement>();
			rs = cstmt.getResultSet();
			while (rs.next()) {
				pagefile.getRows().add(this.read(rs));
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
			Utility.closeConnection(conn, cstmt, rs);
		}
		return pagefile;
	}
	
	@Override
	public WarnElement queryUnwarnWeiboElement(int weibowarnId, int reciver) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		WarnElement result = null;
		try {
			conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME)
					.getConnection();
			String cmdText = "select a.* from Office_WarnList a inner join Office_Weibo_Warning b on a.warnid=b.id "
					+ "where a.warnType=3 and b.id=? and a.receiver=? and a.status=0";
			pstmt = conn.prepareStatement(cmdText);
			pstmt.setInt(1, weibowarnId);
			pstmt.setInt(2, reciver);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				result = this.read(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			Utility.closeConnection(conn, pstmt, rs);
		}
		return result;
	}

}
