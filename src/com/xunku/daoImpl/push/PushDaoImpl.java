package com.xunku.daoImpl.push;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.xunku.app.Utility;
import com.xunku.app.helpers.DateHelper;
import com.xunku.app.model.SubscriberEvent;
import com.xunku.app.model.SubscriberTask;
import com.xunku.constant.PortalCST;
import com.xunku.dao.push.PushDao;
import com.xunku.dto.PagerDTO;
import com.xunku.dto.pushservices.EventDTO;
import com.xunku.dto.pushservices.SubscriberDTO;
import com.xunku.pojo.push.Subscriber;
import com.xunku.pojo.push.ContactInfo;
import com.xunku.pojo.task.Task;
import com.xunku.utils.DatabaseUtils;
import com.xunku.utils.DateUtils;
import com.xunku.utils.Pagefile;

public class PushDaoImpl implements PushDao {

	@Override
	public void insert(Subscriber sub) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		PreparedStatement pstmt2 = null;
		PreparedStatement pstmt3 = null;
		ResultSet rs = null;
		try {
			conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME)
					.getConnection();
			conn.setAutoCommit(false);
			String sql = "INSERT INTO Task_Subscriber (Name,Type,creator,IsSendMail,FirstRunTime,TopN,PeriodType,PeriodCount,IsStop,customid) VALUES (?,?,?,?,?,?,?,?,?,?)";
			pstmt = conn.prepareStatement(sql,
					PreparedStatement.RETURN_GENERATED_KEYS);
			pstmt.setString(1, sub.getName());
			pstmt.setInt(2, sub.getType());
			pstmt.setInt(3, sub.getCreator());
			pstmt.setBoolean(4, sub.isSendMail());
			pstmt.setTimestamp(5,
					new Timestamp(sub.getFirstRunTime().getTime()));
			pstmt.setInt(6, sub.getTopN());
			pstmt.setInt(7, sub.getPeriodType());
			pstmt.setInt(8, sub.getPeriodCount());
			pstmt.setBoolean(9, sub.isStop());
			pstmt.setInt(10, sub.getCustomid());
			pstmt.executeUpdate();
			rs = pstmt.getGeneratedKeys();
			if (rs.next()) {
				int subID = rs.getInt(1);
				String sqlcontact = "INSERT INTO Task_SubscriberContact (SubscriberID,ContactEmail,ContactID) VALUES (?,?,?)";
				String sqltask = "INSERT INTO Task_SubscriberTasks (SubscriberID,TaskID) VALUES (?,?)";
				String sqlEvent = "INSERT INTO Task_SubscriberEvent (SubscriberID,EventID) VALUES (?,?)";
				// 保存联系人
				for (String contactID : sub.getContacts()) {
					pstmt2 = conn.prepareStatement(sqlcontact);
					pstmt2.setInt(1, subID);
					if (contactID.contains("@")) {
						pstmt2.setString(2, contactID);
						pstmt2.setInt(3, -1);
					} else {
						pstmt2.setString(2, "");
						pstmt2.setInt(3, Integer.parseInt(contactID));
					}
					pstmt2.execute();
				}
				// 保存任务
				if (sub.getType() == 0) {
					for (int taskId : sub.getTasks()) {
						pstmt3 = conn.prepareStatement(sqltask);
						pstmt3.setInt(1, subID);
						pstmt3.setInt(2, taskId);
						pstmt3.execute();
					}
				}
				// 保存事件
				if (sub.getType() == 1) {
					for (int eventId : sub.getEvents()) {
						pstmt3 = conn.prepareStatement(sqlEvent);
						pstmt3.setInt(1, subID);
						pstmt3.setInt(2, eventId);
						pstmt3.execute();
					}
				}
			}
			conn.commit();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.CloseStatus(rs, pstmt3, null);
			this.CloseStatus(null, pstmt2, pstmt, null, conn);
		}
	}

	@Override
	public boolean deleteByID(int subId) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		boolean isDelete = false;
		try {
			conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME)
					.getConnection();
			conn.setAutoCommit(false);
			String sql = "DELETE FROM Task_SubscriberContact WHERE SubscriberID=? "
					+ "DELETE FROM Task_SubscriberTasks WHERE SubscriberID=? "
					+ "DELETE FROM Task_SubscriberEvent WHERE SubscriberID=? "
					+ "DELETE FROM Task_Subscriber WHERE ID=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, subId);
			pstmt.setInt(2, subId);
			pstmt.setInt(3, subId);
			pstmt.setInt(4, subId);
			int flag = pstmt.executeUpdate();
			if (flag > 0) {
				isDelete = true;
			}
			conn.commit();
		} catch (SQLException e) {
			e.printStackTrace();
			try {
				conn.rollback();
				isDelete = false;
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		} finally {
			this.CloseStatus(pstmt, conn);
		}
		return isDelete;
	}

	@Override
	public Pagefile<Subscriber> queryByUserId(int customid, PagerDTO dto) {
		Connection conn = null;
		CallableStatement cstmt = null;
		ResultSet rs = null;
		ResultSet rs2 = null;
		Pagefile<Subscriber> pagefile = null;
		try {
			conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME)
					.getConnection();
			cstmt = conn.prepareCall("{call SupesoftPage(?,?,?,?,?,?,?)}");
			cstmt.setString(1, "Task_Subscriber");
			cstmt.setString(2, "ID,Name,Type,IsStop");
			cstmt.setInt(3, dto.pageSize);
			cstmt.setInt(4, dto.pageIndex);
			cstmt.setString(5, " where CustomID=" + customid);
			cstmt.setString(6, "ID");
			cstmt.setInt(7, 0);
			cstmt.execute();
			pagefile = new Pagefile<Subscriber>();
			rs = cstmt.getResultSet();
			while (rs.next()) {
				Subscriber sub = new Subscriber();
				sub.setId(rs.getInt("ID"));
				sub.setName(rs.getString("Name"));
				sub.setType(rs.getInt("Type"));
				sub.setStop(rs.getBoolean("IsStop"));
				pagefile.getRows().add(sub);
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
			this.CloseStatus(null, rs2, rs, cstmt, null, conn);
		}
		return pagefile;
	}

	@Override
	public void updateStatus(int subId, boolean isStop) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		try {
			conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME)
					.getConnection();
			String sql = "UPDATE Task_Subscriber SET IsStop=?,LastExecuteTime=? WHERE ID=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setBoolean(1, isStop);
			pstmt.setString(2, DateUtils.nowDateFormat("yyyy-MM-dd HH:mm:ss"));
			pstmt.setInt(3, subId);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		} finally {
			this.CloseStatus(pstmt, conn);
		}
	}

	@Override
	public void updateSubscriber(Subscriber sub) {
		Connection conn = null;
		CallableStatement cstmt = null;
		PreparedStatement pstmt = null;
		PreparedStatement pstmt2 = null;
		PreparedStatement pstmt3 = null;
		try {
			conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME)
					.getConnection();
			cstmt = conn
					.prepareCall("{call sp_push_updateSubscriber(?,?,?,?,?,?,?,?,?)}");
			cstmt.setInt(1, sub.getId());
			cstmt.setString(2, sub.getName());
			cstmt.setInt(3, sub.getType());
			cstmt.setBoolean(4, sub.isSendMail());
			cstmt.setTimestamp(5,
					new Timestamp(sub.getFirstRunTime().getTime()));
			cstmt.setInt(6, sub.getTopN());
			cstmt.setInt(7, sub.getPeriodType());
			cstmt.setInt(8, sub.getPeriodCount());
			cstmt.setBoolean(9, sub.isStop());
			cstmt.execute();
			String sqlcontact = "INSERT INTO Task_SubscriberContact (SubscriberID,ContactEmail,ContactID) VALUES (?,?,?)";
			String sqltask = "INSERT INTO Task_SubscriberTasks (SubscriberID,TaskID) VALUES (?,?)";
			String sqlEvent = "INSERT INTO Task_SubscriberEvent (SubscriberID,EventID) VALUES (?,?)";
			// 保存联系人
			for (String contactID : sub.getContacts()) {
				pstmt = conn.prepareStatement(sqlcontact);
				pstmt.setInt(1, sub.getId());
				if (contactID.contains("@")) {
					pstmt.setString(2, contactID);
					pstmt.setInt(3, -1);
				} else {
					pstmt.setString(2, "");
					pstmt.setInt(3, Integer.parseInt(contactID));
				}
				pstmt.execute();
			}
			// 保存任务
			if (sub.getType() == 0) {
				for (int taskId : sub.getTasks()) {
					this.executeSql(conn, pstmt, sqltask, sub.getId(), taskId);
				}
			}
			// 保存事件
			if (sub.getType() == 1) {
				for (int eventId : sub.getEvents()) {
					this.executeSql(conn, pstmt3, sqlEvent, sub.getId(),
							eventId);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.CloseStatus(pstmt3, pstmt2, pstmt, cstmt, conn);
		}
	}

	private void executeSql(Connection conn, PreparedStatement pstmt,
			String sql, int param1, int param2) throws SQLException {
		pstmt = conn.prepareStatement(sql);
		pstmt.setInt(1, param1);
		pstmt.setInt(2, param2);
		pstmt.execute();
	}

	@Override
	public SubscriberDTO queryBySubID(int subId) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		SubscriberDTO sub = null;
		try {
			conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME)
					.getConnection();
			// 判断当前推送的下是否存在有接受人
			String revicerSQL = "select * from dbo.Task_SubscriberContact where SubscriberID=?";
			// 存在接受人执行的sql语句
			String sql = "SELECT * FROM vi_push_queryBySubID WHERE ID=?";
			// 不存在接受人执行的sql语句
			String sqlnot = "select * from  dbo.Task_Subscriber where ID=?";
			List<ContactInfo> contacts = new ArrayList<ContactInfo>();
			pstmt = conn.prepareStatement(revicerSQL);
			pstmt.setInt(1, subId);
			rs = pstmt.executeQuery();
			int cout = 0;
			while (rs.next()) {
				cout++;
				break;
			}
			// 说明存在接受人
			if (cout > 0) {
				pstmt = conn.prepareStatement(sql);
				pstmt.setInt(1, subId);
				rs = pstmt.executeQuery();
				sub = new SubscriberDTO();
				while (rs.next()) {
					sub.setId(subId);
					sub.setName(rs.getString("Name"));
					sub.setType(rs.getInt("Type"));
					sub.setSendMail(rs.getBoolean("IsSendMail"));
					sub.setFirstRunTime(DateUtils.StringDateFormat(rs
							.getString("FirstRunTime")));
					sub.setTopN(rs.getInt("TopN"));
					sub.setPeriodType(rs.getInt("PeriodType"));
					sub.setPeriodCount(rs.getInt("PeriodCount"));
					int contactID = rs.getInt("ContactID");
					String contactEmail = rs.getString("ContactEmail");
					// email
					if (contactID == -1 && contactEmail.length() != 0) {
						ContactInfo info = new ContactInfo();
						info.setEmail(contactEmail);
						info.setContactId(-1);
						contacts.add(info);
					}
					// contact user
					if (contactID != -1 && contactEmail.length() == 0) {
						contacts.add(queryUserBySubID(conn, contactID));
					}
				}
			}
			// 说明不存在接受人
			else {
				pstmt = conn.prepareStatement(sqlnot);
				pstmt.setInt(1, subId);
				rs = pstmt.executeQuery();
				sub = new SubscriberDTO();
				while (rs.next()) {
					sub.setId(subId);
					sub.setName(rs.getString("Name"));
					sub.setType(rs.getInt("Type"));
					sub.setSendMail(rs.getBoolean("IsSendMail"));
					sub.setFirstRunTime(DateUtils.StringDateFormat(rs
							.getString("FirstRunTime")));
					sub.setTopN(rs.getInt("TopN"));
					sub.setPeriodType(rs.getInt("PeriodType"));
					sub.setPeriodCount(rs.getInt("PeriodCount"));
				}
			} // ------------

			// 获取事件
			sub.setEventList(queryEventBySubID(conn, subId));
			// 获取任务
			sub.setTaskList(queryTaskBySubID(conn, subId));
			// 获取接受人
			sub.setContacts(contacts);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		} finally {
			this.CloseStatus(rs, pstmt, conn);
		}
		return sub;
	}

	private ContactInfo queryUserBySubID(Connection conn, int useId) {
		PreparedStatement pstmt = null;
		String sql = "SELECT ID,NickName,Email FROM Base_Users WHERE ID=?";
		ResultSet rs = null;
		ContactInfo info = null;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, useId);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				info = new ContactInfo();
				info.setContactId(rs.getInt("ID"));
				info.setUserName(rs.getString("NickName"));
				info.setEmail(rs.getString("Email"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.CloseStatus(rs, pstmt, null);
		}
		return info;
	}

	private List<Task> queryTaskBySubID(Connection conn, int subID) {
		PreparedStatement pstmt = null;
		String sql = "SELECT ID,Name FROM Task_Tasks WHERE ID in (SELECT TaskID FROM Task_SubscriberTasks WHERE SubscriberID=?)";
		ResultSet rs = null;
		List<Task> tasks = new ArrayList<Task>();
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, subID);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				Task task = new Task();
				task.setId(rs.getInt("ID"));
				task.setName(rs.getString("Name"));
				tasks.add(task);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.CloseStatus(rs, pstmt, null);
		}
		return tasks;
	}

	private List<EventDTO> queryEventBySubID(Connection conn, int subID) {
		PreparedStatement pstmt = null;
		String sql = "SELECT id,name FROM Event_List WHERE id in (SELECT EventID FROM Task_SubscriberEvent WHERE SubscriberID=?)";
		ResultSet rs = null;
		List<EventDTO> evenList = new ArrayList<EventDTO>();
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, subID);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				EventDTO dto = new EventDTO();
				dto.setId(rs.getInt("id"));
				dto.setName(rs.getString("name"));
				evenList.add(dto);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.CloseStatus(rs, pstmt, null);
		}
		return evenList;
	}

	private void CloseStatus(ResultSet rs, PreparedStatement pstmt,
			Connection conn) {
		this.CloseStatus(null, null, rs, null, pstmt, conn);
	}

	private void CloseStatus(PreparedStatement pstmt, Connection conn) {
		this.CloseStatus(null, pstmt, conn);
	}

	private void CloseStatus(PreparedStatement pstmt3,
			PreparedStatement pstmt2, PreparedStatement pstmt,
			CallableStatement cstmt, Connection conn) {
		try {
			if (pstmt3 != null) {
				pstmt3.close();
			}
			if (pstmt2 != null) {
				pstmt2.close();
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
	public boolean checkSubscriberStatus(int subId) {
		Connection conn = null;
		CallableStatement cstmt = null;
		boolean isStop = false;
		// boolean isFlag = false;
		try {
			conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME)
					.getConnection();
			cstmt = conn
					.prepareCall("{call sp_Subscriber_updateSubscriber(?,?,?)}");
			cstmt.setInt(1, subId);
			cstmt.registerOutParameter(2, java.sql.Types.BOOLEAN);
			cstmt.registerOutParameter(3, java.sql.Types.BOOLEAN);
			cstmt.execute();
			// isFlag = cstmt.getBoolean(2);
			isStop = !cstmt.getBoolean(3);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.CloseStatus(cstmt, conn);
		}
		return isStop;
	}

	private Subscriber getSubscriber(ResultSet rs) throws SQLException {
		Subscriber s = new Subscriber();
		s.setCreator(rs.getInt("creator"));
		s.setCustomid(rs.getInt("customid"));
		Timestamp frt = rs.getTimestamp("FirstRunTime");
		if (frt != null) {
			s.setFirstRunTime(new Date(frt.getTime()));
		}
		Timestamp let = rs.getTimestamp("LastExecuteTime");
		if (let != null) {
			s.setExecutedTime(new Date(let.getTime()));
		}
		s.setId(rs.getInt("id"));
		s.setName(rs.getString("name"));
		s.setPeriodCount(rs.getInt("PeriodCount"));
		s.setPeriodType(rs.getInt("PeriodType"));
		s.setSendMail(rs.getBoolean("IsSendMail"));
		s.setStop(!rs.getBoolean("IsStop"));
		s.setTopN(rs.getInt("TopN"));
		s.setType(rs.getInt("Type"));
		return s;
	}

	@Override
	public List<Subscriber> querySubscriberByCustomId(int customid) {
		PreparedStatement pstmt = null;
		Connection conn = null;
		String sql = "SELECT * FROM Task_Subscriber WHERE customid=?";
		ResultSet rs = null;
		List<Subscriber> result = new ArrayList<Subscriber>();
		try {
			conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME)
					.getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, customid);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				result.add(this.getSubscriber(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			Utility.closeConnection(conn, pstmt, rs);
		}
		return result;
	}

	@Override
	public int addPusher(String customName, int sid, String text,
			String emailList) {
		PreparedStatement pstmt = null;
		Connection conn = null;
		String sql = "Insert into Task_Scbscriber_Push(sid,created,text,emailList,customName)values(?,?,?,?,?)";
		ResultSet rs = null;
		int result = 0;
		try {
			conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME)
					.getConnection();
			pstmt = conn.prepareStatement(sql,
					PreparedStatement.RETURN_GENERATED_KEYS);
			pstmt.setInt(1, sid);
			pstmt.setLong(2, new Date().getTime());
			pstmt.setString(3, text);
			pstmt.setString(4, emailList);
			pstmt.setString(5, customName);
			pstmt.executeUpdate();
			rs = pstmt.getGeneratedKeys();
			if (rs.next()) {
				result = rs.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			Utility.closeConnection(conn, pstmt, rs);
		}
		return result;
	}

	@Override
	public Subscriber queryById(int subscriberId) {
		PreparedStatement pstmt = null;
		Connection conn = null;
		String sql = "SELECT * FROM Task_Subscriber WHERE id=?";
		ResultSet rs = null;
		Subscriber result = null;
		try {
			conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME)
					.getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, subscriberId);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				result = this.getSubscriber(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.CloseStatus(rs, pstmt, conn);
		}
		return result;
	}

	@Override
	public boolean existsPusher(int sid, String tid, int platform) {
		PreparedStatement pstmt = null;
		Connection conn = null;
		String sql = "SELECT sid FROM Task_Scbscriber_Push WHERE sid=? and tid=? and platform=?";
		ResultSet rs = null;
		boolean result = false;
		try {
			conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME)
					.getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, sid);
			pstmt.setString(2, tid);
			pstmt.setInt(3, platform);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				result = true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.CloseStatus(rs, pstmt, conn);
		}
		return result;
	}

	@Override
	public List<SubscriberTask> queryTasks(int subId) {
		PreparedStatement pstmt = null;
		Connection conn = null;
		String sql = "select * from Task_SubscriberTasks where SubscriberId=?";
		ResultSet rs = null;
		List<SubscriberTask> result = new ArrayList<SubscriberTask>();
		try {
			conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME)
					.getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, subId);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				SubscriberTask task = new SubscriberTask();
				task.setSubid(subId);
				task.setTaskid(rs.getInt("taskid"));
				task.setLast(rs.getTimestamp("lastCT"));
				result.add(task);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.CloseStatus(rs, pstmt, conn);
		}
		return result;
	}

	@Override
	public List<SubscriberEvent> queryEvents(int subId) {
		PreparedStatement pstmt = null;
		Connection conn = null;
		String sql = "select * from Task_SubscriberEvent where SubscriberId=?";
		ResultSet rs = null;
		List<SubscriberEvent> result = new ArrayList<SubscriberEvent>();
		try {
			conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME)
					.getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, subId);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				SubscriberEvent event = new SubscriberEvent();
				event.setEventid(rs.getInt("EventId"));
				event.setSubid(subId);
				event.setLast(rs.getTimestamp("lastCT"));
				result.add(event);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.CloseStatus(rs, pstmt, conn);
		}
		return result;
	}

	@Override
	public List<String> queryEmails(int subId) {
		PreparedStatement pstmt = null;
		Connection conn = null;
		String sql = "select a.ContactID,a.ContactEmail,b.Email from Task_SubscriberContact a left join Base_Users b on a.ContactID =b.ID where a.SubscriberID=?";
		ResultSet rs = null;
		List<String> result = new ArrayList<String>();
		try {
			conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME)
					.getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, subId);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				if (rs.getInt(1) == -1) {
					result.add(rs.getString("ContactEmail"));
				} else {
					result.add(rs.getString("Email"));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.CloseStatus(rs, pstmt, conn);
		}
		return result;
	}

	@Override
	public int queryPushStatus(Subscriber sub) {

		int subid = sub.getId();
		int type = sub.getPeriodType();
		int pcount = sub.getPeriodCount();
		long[] times = DateHelper.calScale4Push(type, pcount);
		String cmdText = "select COUNT(id) from Task_Scbscriber_Push where sid=? and created<=? and created>=?";
		PreparedStatement pstmt = null;
		Connection conn = null;
		ResultSet rs = null;
		int result = 0;
		try {
			conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME)
					.getConnection();
			pstmt = conn.prepareStatement(cmdText);
			pstmt.setInt(1, subid);
			pstmt.setLong(2, times[1]);
			pstmt.setLong(3, times[0]);

			rs = pstmt.executeQuery();
			if (rs.next()) {
				result = rs.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.CloseStatus(rs, pstmt, conn);
		}
		return result;
	}

	@Override
	public void updateSubEventLastCT(int subid, int eventid, Date date) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		try {
			conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME)
					.getConnection();
			String sql = "Update Task_SubscriberEvent set lastCT = ? where SubscriberID=? and EventID = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setTimestamp(1, new Timestamp(date.getTime()));
			pstmt.setInt(2, subid);
			pstmt.setInt(3, eventid);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			Utility.closeConnection(conn, pstmt, null);
		}
	}

	@Override
	public void updateSubTaskLastCT(int subid, int taskid, Date date) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		try {
			conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME)
					.getConnection();
			String sql = "Update Task_SubscriberTasks set lastCT = ? where SubscriberID=? and TaskID = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setTimestamp(1, new Timestamp(date.getTime()));
			pstmt.setInt(2, subid);
			pstmt.setInt(3, taskid);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			Utility.closeConnection(conn, pstmt, null);
		}
	}

	@Override
	public void insertPushTaskHis(int subid, int taskid, String tid,
			Date pushTime) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		try {
			conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME)
					.getConnection();
			String sql = "insert into Task_SubscriberTasks_Push_His (subid,taskid,tid,push_time)values(?,?,?,?)";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, subid);
			pstmt.setInt(2, taskid);
			pstmt.setString(3, tid);
			pstmt.setTimestamp(4, new Timestamp(pushTime.getTime()));
			pstmt.executeUpdate();
		} catch (SQLException e) {
			// e.printStackTrace();
		} finally {
			Utility.closeConnection(conn, pstmt, null);
		}

	}

	@Override
	public List<String> queryPushTaskHis(int subid, int taskid) {

		PreparedStatement pstmt = null;
		Connection conn = null;
		ResultSet rs = null;
		List<String> result = new ArrayList<String>();
		try {
			conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME)
					.getConnection();
			String cmdText = "select push_tid from Task_SubscriberTasks_Push_His where subid=? and taskid=?";
			pstmt = conn.prepareStatement(cmdText);
			pstmt.setInt(1, subid);
			pstmt.setInt(2, taskid);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				result.add(rs.getString(1));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.CloseStatus(rs, pstmt, conn);
		}
		return result;
	}
}
