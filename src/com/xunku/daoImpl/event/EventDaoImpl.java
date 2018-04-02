package com.xunku.daoImpl.event;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import com.xunku.app.Utility;
import com.xunku.app.enums.Platform;
import com.xunku.app.enums.WordCloudEnum;
import com.xunku.app.helpers.DateHelper;
import com.xunku.app.monitor.EventMonitor;
import com.xunku.constant.PortalCST;
import com.xunku.dao.event.EventDao;
import com.xunku.dto.pushservices.EventDTO;
import com.xunku.pojo.base.Pager;
import com.xunku.utils.DatabaseUtils;
import com.xunku.utils.Pagefile;

public class EventDaoImpl implements EventDao {

	@Override
	public void insert(EventMonitor e) {
		Connection conn = null;
		CallableStatement cstmt = null;
		try {
			conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME)
					.getConnection();
			cstmt = conn
					.prepareCall("{call sp_EventList_insert(?,?,?,?,?,?,?,?,?,?)}");
			cstmt.setString(1, e.getName());
			cstmt.setString(2, e.getKeywords());
			cstmt.setString(3, e.getNotkeywords());
			cstmt.setInt(4, e.getLocation());
			// modify by wujian
			cstmt.setTimestamp(5, new java.sql.Timestamp(e.getStartTime()
					.getTime()));
			cstmt.setTimestamp(6, new java.sql.Timestamp(e.getEndTime()
					.getTime()));
			cstmt.setInt(7, e.getCreator());
			cstmt.setInt(8, e.getCustomID());
			cstmt.setInt(9, Utility.getPlatform(e.getPlatform()));

			cstmt.registerOutParameter(10, java.sql.Types.INTEGER);

			cstmt.execute();

			int id = cstmt.getInt(10);

			if (id != 0) {
				e.setId(id);
			}

		} catch (SQLException ex) {
			ex.printStackTrace();
		} finally {
			this.CloseStatus(cstmt, conn);
		}
	}

	@Override
	public void update(EventMonitor e) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		try {
			conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME)
					.getConnection();
			String sql = "UPDATE Event_List SET name=?,keywords=?,notkeywords=?,location=?,StartTime=?,EndTime=? WHERE ID=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, e.getName());
			pstmt.setString(2, e.getKeywords());
			pstmt.setString(3, e.getNotkeywords());
			pstmt.setInt(4, e.getLocation());
			// modify by wujian
			pstmt.setTimestamp(5, new java.sql.Timestamp(e.getStartTime()
					.getTime()));
			pstmt.setTimestamp(6, new java.sql.Timestamp(e.getEndTime()
					.getTime()));
			pstmt.setInt(7, e.getMonitorId());
			pstmt.executeUpdate();
		} catch (SQLException e1) {
			e1.printStackTrace();
		} finally {
			this.CloseStatus(pstmt, conn);
		}
	}

	@Override
	public void deleteByEId(int eid) {
		Connection conn = null;
		CallableStatement cstmt = null;
		try {
			conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME)
					.getConnection();
			cstmt = conn.prepareCall("{call sp_Event_deleteByEId(?)}");
			cstmt.setInt(1, eid);
			cstmt.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.CloseStatus(cstmt, conn);
		}
	}

	@Override
	public List<EventDTO> queryByPush(int userid) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<EventDTO> list = new ArrayList<EventDTO>();
		try {
			conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME)
					.getConnection();
			pstmt = conn
					.prepareStatement("SELECT id,name ,platform  FROM Event_List WHERE customID = (select CustomID from Base_Users where ID=?)");
			pstmt.setInt(1, userid);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				EventDTO dto = new EventDTO();
				dto.setId(rs.getInt("id"));
				dto.setName(rs.getString("name"));
				dto.setPlatform(rs.getInt("platform"));
				list.add(dto);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.CloseStatus(rs, pstmt, conn);
		}
		return list;
	}

	@Override
	public Pagefile<EventMonitor> queryEventList(Pager dto, int userid,
			Platform platform) {
		Connection conn = null;
		CallableStatement cstmt = null;
		ResultSet rs = null;
		ResultSet rs2 = null;
		Pagefile<EventMonitor> pagefile = null;
		try {
			conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME)
					.getConnection();
			cstmt = conn.prepareCall("{call SupesoftPage(?,?,?,?,?,?,?)}");
			cstmt.setString(1, "vi_Eventlist_queryEventList");
			cstmt.setString(2, "*");
			cstmt.setInt(3, dto.getPageSize());
			cstmt.setInt(4, dto.getPageIndex());
			cstmt.setString(5,
					"where customID=(select CustomID from Base_Users where id="
							+ userid + ") and platform="
							+ Utility.getPlatform(platform));
			cstmt.setString(6, "id");
			cstmt.setInt(7, 1);
			cstmt.execute();
			pagefile = new Pagefile<EventMonitor>();
			rs = cstmt.getResultSet();
			while (rs.next()) {
				pagefile.getRows().add(this.getEventMonitor(rs));
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
	public EventMonitor queryEventByEId(int eid) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		EventMonitor event = null;
		try {
			conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME)
					.getConnection();
			// change Event List to vi_Eventlist_queryEventList by wujian
			String sql = "SELECT * FROM vi_Eventlist_queryEventList WHERE id=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, eid);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				event = this.getEventMonitor(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.CloseStatus(rs, pstmt, conn);
		}
		return event;
	}

	@Override
	public List<EventMonitor> queryAvailableEventList() {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<EventMonitor> eventList = new ArrayList<EventMonitor>();
		try {
			conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME)
					.getConnection();
			String sql = "SELECT a.id,a.name,a.keywords,a.notkeywords,a.location,a.startTime,a.endTime,a.creator,a.customID,a.platform,a.tab,a.fetchTime,"
					+ "b.server,b.name as poolname,b.id as poolid FROM "
					+ "Event_List a JOIN Base_DBServer b ON a.dbserver=b.id where a.endTime>GETDATE()";
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				eventList.add(this.getEventMonitor(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.CloseStatus(rs, pstmt, conn);
		}
		return eventList;
	}

	@Override
	public List<EventMonitor> queryEventList() {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<EventMonitor> eventList = new ArrayList<EventMonitor>();
		try {
			conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME)
					.getConnection();
			String sql = "SELECT a.id,a.name,a.keywords,a.notkeywords,a.location,a.startTime,a.endTime,a.creator,a.customID,a.platform,a.tab,a.fetchTime,b.server,b.name as poolname FROM Event_List a JOIN Base_DBServer b ON a.dbserver=b.id";
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				eventList.add(this.getEventMonitor(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.CloseStatus(rs, pstmt, conn);
		}
		return eventList;
	}

	private EventMonitor getEventMonitor(ResultSet rs) throws SQLException {
		EventMonitor event = new EventMonitor();
		event.setId(rs.getInt("id"));
		event.setName(rs.getString("name"));
		event.setKeywords(rs.getString("keywords"));
		event.setNotkeywords(rs.getString("notkeywords"));
		event.setLocation(rs.getInt("location"));
		if (rs.getTimestamp("startTime") == null) {
			event.setStartTime(null);
		} else {
			event.setStartTime(DateHelper.getSqlTime(rs
					.getTimestamp("startTime")));
		}
		if (rs.getTimestamp("endTime") == null) {
			event.setEndTime(null);
		} else {
			event.setEndTime(DateHelper.getSqlTime(rs.getTimestamp("endTime")));
		}
		event.setCreator(rs.getInt("creator"));
		event.setCustomID(rs.getInt("customID"));
		event.setPlatform(Utility.getPlatform(rs.getInt("platform")));
		event.setDbserver(rs.getString("poolname"));
		if (rs.getTimestamp("fetchTime") == null) {
			event.setFetchTime(null);
		} else {
			event.setFetchTime(DateHelper.getSqlTime(rs
					.getTimestamp("fetchTime")));
		}
		event.setPoolId(rs.getInt("poolid"));
		return event;
	}

	@Override
	public boolean updateFetchTime(int eid, String fetchTime) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		boolean isUpdate = false;
		try {
			conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME)
					.getConnection();
			String sql = "UPDATE Event_List SET fetchTime=? WHERE id=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, fetchTime);
			pstmt.setInt(2, eid);
			int flag = pstmt.executeUpdate();
			if (flag > 0) {
				isUpdate = true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.CloseStatus(pstmt, conn);
		}
		return isUpdate;
	}

	@Override
	public int queryEventListTotal(int customId) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int count = 0;
		try {
			conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME)
					.getConnection();
			String sql = "SELECT COUNT(id) as count FROM Event_List WHERE customID=?";
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
	public int getPeriodEventCount(int customid) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int count = 0;
		try {
			conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME)
					.getConnection();
			String sql = "select COUNT(ID) as count from Event_List where customID=? and endTime >= GETDATE()";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, customid);
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

	@Override
	public int getPeriodEventCountById(int customid, int id) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int count = 0;
		try {
			conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME)
					.getConnection();
			String sql = "select COUNT(ID) as count from Event_List where customID=? and id = ? and endTime >= GETDATE()";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, customid);
			pstmt.setInt(2, id);
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

	@Override
	public void addWordCloudTask(int monitorid, int poolid, String sourceTable,
			WordCloudEnum type) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		try {
			conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME)
					.getConnection();
			String sql = "INSERT INTO Base_WordCloud_Job (poolid,sourcetable,monitorid,targettable)values(?,?,?,?)";
			pstmt = conn.prepareStatement(sql);

			pstmt.setInt(1, poolid);
			pstmt.setString(2, sourceTable);
			pstmt.setInt(3, monitorid);
			if (type == WordCloudEnum.Event) {
				pstmt.setString(4, "Event_HotWord_Statis");
			}
			if (type == WordCloudEnum.Spread) {
				pstmt.setString(4, "Spread_HotWord_Statis");
			}
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.CloseStatus(pstmt, conn);
		}

	}

}
