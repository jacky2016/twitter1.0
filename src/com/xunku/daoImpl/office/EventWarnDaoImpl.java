package com.xunku.daoImpl.office;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.xunku.app.Utility;
import com.xunku.app.interfaces.IFiller;
import com.xunku.constant.PortalCST;
import com.xunku.dao.office.EventWarnDao;
import com.xunku.pojo.base.Pager;
import com.xunku.pojo.office.EventWarn;
import com.xunku.utils.DatabaseUtils;
import com.xunku.utils.Pagefile;

public class EventWarnDaoImpl implements EventWarnDao, IFiller<EventWarn> {

	@Override
	public boolean deleteById(int ewid) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		boolean isDelete = false;
		try {
			conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME)
					.getConnection();
			String sql = "delete Office_Event_Warning where id=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, ewid);
			int flag = pstmt.executeUpdate();
			if (flag > 0) {
				isDelete = true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			Utility.closeConnection(conn, pstmt, null);
		}
		return isDelete;
	}

	@Override
	public int insert(EventWarn ew) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int ewid = 0;
		try {
			conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME)
					.getConnection();
			String sql = "insert into Office_Event_Warning (eventid,weibo,receiver,type,customid) values (?,?,?,?,?)";
			pstmt = conn.prepareStatement(sql,
					PreparedStatement.RETURN_GENERATED_KEYS);
			pstmt.setInt(1, ew.getEventid());
			pstmt.setInt(2, ew.getWeibo());
			pstmt.setString(3, ew.getReceiver());
			pstmt.setString(4, ew.getType());
			pstmt.setInt(5, ew.getCustomid());
			pstmt.execute();
			rs = pstmt.getGeneratedKeys();
			if (rs.next()) {
				ewid = rs.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			Utility.closeConnection(conn, pstmt, rs);
		}
		return ewid;
	}

	@Override
	public List<EventWarn> queryEventWarnList(int customid) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<EventWarn> list = new ArrayList<EventWarn>();
		try {
			conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME)
					.getConnection();
			String sql = "select * from Office_Event_Warning where customid=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, customid);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				list.add(this.fill(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			Utility.closeConnection(conn, pstmt, rs);
		}
		return list;
	}

	@Override
	public boolean update(EventWarn ew) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		boolean isUpdate = false;
		try {
			conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME)
					.getConnection();
			String sql = "update Office_Event_Warning set weibo=?,receiver=?,type=?,customid=? where id=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, ew.getWeibo());
			pstmt.setString(2, ew.getReceiver());
			pstmt.setString(3, ew.getType());
			pstmt.setInt(4, ew.getCustomid());
			pstmt.setInt(5, ew.getId());
			int flag = pstmt.executeUpdate();
			if (flag > 0) {
				isUpdate = true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			Utility.closeConnection(conn, pstmt, null);
		}
		return isUpdate;
	}

	@Override
	public EventWarn queryEventByEventid(int eventid) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		EventWarn ew = new EventWarn();
		try {
			conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME)
					.getConnection();
			String sql = "select * from Office_Event_Warning where eventid=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, eventid);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				ew = this.fill(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			Utility.closeConnection(conn, pstmt, rs);
		}
		return ew;
	}

	// 获取事件监控的名字
	public String getEventName(int eventid) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "select  name from Event_List where  id=?";
		String name = "";
		try {
			conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME)
					.getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, eventid);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				name = rs.getString("name");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			Utility.closeConnection(conn, pstmt, rs);
		}
		return name;
	}

	@Override
	public Pagefile<EventWarn> queryEventWarnList(int customid, Pager pager) {
		Connection conn = null;
		CallableStatement cstmt = null;
		ResultSet rs = null;
		ResultSet rs2 = null;
		// EventWarn ew = new EventWarn();
		Pagefile<EventWarn> pagefile = new Pagefile<EventWarn>();
		// String whereSql=" left join Event_List as eventTable on
		// eventTable.id=warnTable.eventid ";
		String whereSql = "where warnTable.eventid  in (select  eventTable.id  from  Event_List as  eventTable )  ";
		try {
			conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME)
					.getConnection();
			cstmt = conn.prepareCall("{call SupesoftPage(?,?,?,?,?,?,?)}");
			cstmt.setString(1, "Office_Event_Warning as warnTable ");
			cstmt.setString(2, "*");
			cstmt.setInt(3, pager.getPageSize());
			cstmt.setInt(4, pager.getPageIndex());
			cstmt.setString(5, whereSql);
			cstmt.setString(6, "created");
			cstmt.setInt(7, 1);
			cstmt.execute();
			rs = cstmt.getResultSet();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			while (rs.next()) {
				EventWarn ew = new EventWarn();
				ew.setId(rs.getInt("id"));
				ew.setEventid(rs.getInt("eventid"));
				ew.setWeibo(rs.getInt("weibo"));
				ew.setReceiver("receiver");
				ew.setType(rs.getString("type"));
				ew.setCustomid(rs.getInt("customid"));
				Date date = new Date();
				try {
					date = sdf.parse(rs.getString("created"));
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				ew.setCreated(date);
				ew.setEventName(this.getEventName(ew.getEventid()));
				pagefile.getRows().add(ew);
			}
			if (cstmt.getMoreResults()) {
				rs2 = cstmt.getResultSet();
				if (rs2.next()) {
					pagefile.setRealcount(rs2.getInt(2));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			Utility.closeConnection(conn, cstmt, rs);

		}
		return pagefile;

		/*
		 * Query query = new Query();
		 * query.setTableName("Office_Event_Warning"); query.setFields("*");
		 * query.setPageIndex(pager.getPageIndex());
		 * query.setPageSize(pager.getPageSize());
		 * query.setSortField("created"); query.setWhere(" where customid = " +
		 * customid); return DbHelper.executePager(query, this);
		 */
	}

	public void warnInfoCloseStatus(ResultSet rs3, ResultSet rs2, ResultSet rs,
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
	public EventWarn fill(ResultSet rs) throws SQLException {
		EventWarn ew = new EventWarn();
		ew.setId(rs.getInt("id"));
		ew.setEventid(rs.getInt("eventid"));
		ew.setCustomid(rs.getInt("customid"));
		ew.setReceiver(rs.getString("receiver"));
		ew.setWeibo(rs.getInt("weibo"));
		ew.setType(rs.getString("type"));
		ew.setCreated(new Date(rs.getTimestamp("created").getTime()));
		ew.setRunning(rs.getBoolean("isrunning"));
		ew.setHappen(rs.getBoolean("happen"));
		return ew;
	}

	@Override
	public void changeRunning(int eventid, int customid, boolean running) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		try {
			conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME)
					.getConnection();
			String sql = "update Office_Event_Warning set isrunning=? where eventid=? and customid=?";
			pstmt = conn.prepareStatement(sql);

			pstmt.setBoolean(1, running);
			pstmt.setInt(2, eventid);
			pstmt.setInt(3, customid);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			Utility.closeConnection(conn, pstmt, null);
		}
	}

}
