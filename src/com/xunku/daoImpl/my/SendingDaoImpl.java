package com.xunku.daoImpl.my;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.xunku.app.Utility;
import com.xunku.app.enums.PostType;
import com.xunku.constant.PortalCST;
import com.xunku.constant.SendCountEnum;
import com.xunku.constant.SendStatusEnum;
import com.xunku.constant.WeiboType;
import com.xunku.dao.my.SendingDao;
import com.xunku.dto.PagerDTO;
import com.xunku.dto.office.PostDealDTO;
import com.xunku.dto.office.SendInfoDTO;
import com.xunku.pojo.base.Pager;
import com.xunku.pojo.my.Sender;
import com.xunku.pojo.my.Sending;
import com.xunku.utils.DatabaseUtils;
import com.xunku.utils.Pagefile;

public class SendingDaoImpl implements SendingDao {

	@Override
	public boolean checkSending(long sid, int approved, int auditor) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		boolean isCheck = false;
		try {
			conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME)
					.getConnection();
			String sql = "UPDATE My_Sending SET approved=?,auditor=? WHERE id=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, approved);
			pstmt.setInt(2, auditor);
			pstmt.setLong(3, sid);
			int flag = pstmt.executeUpdate();
			if (flag > 0) {
				isCheck = true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			isCheck = false;
		} finally {
			this.CloseStatus(pstmt, conn);
		}
		return isCheck;
	}

	@Override
	public boolean deleteBySId(Long sid) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		boolean isDelete = false;
		try {
			conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME)
					.getConnection();
			conn.setAutoCommit(false);
			String sql = "DELETE FROM My_Sender WHERE sid=? "
					+ "DELETE FROM My_Sending WHERE id=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, sid);
			pstmt.setLong(2, sid);
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
	public long insert(Sending send) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		PreparedStatement pstmt2 = null;
		ResultSet rs = null;
		long sid = 0;
		try {
			conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME)
					.getConnection();
			conn.setAutoCommit(false);
			String sql = "INSERT INTO My_Sending (text,submit,sent,images,userid,type,sourceid,flag,approved,auditor,orginalid) VALUES (?,?,?,?,?,?,?,?,?,?,?)";
			pstmt = conn.prepareStatement(sql,
					PreparedStatement.RETURN_GENERATED_KEYS);
			pstmt.setString(1, send.getText());
			pstmt.setTimestamp(2, new Timestamp(new Date().getTime()));
			pstmt.setLong(3, send.getSent());
			pstmt.setString(4, send.getImages());
			pstmt.setInt(5, send.getUserid());
			pstmt.setInt(6, send.getType());
			pstmt.setString(7, send.getSourceid());
			pstmt.setBoolean(8, send.isFlag());
			pstmt.setInt(9, send.getApproved());
			pstmt.setInt(10, send.getAuditor());
			pstmt.setString(11, send.getOrgId());
			pstmt.execute();
			rs = pstmt.getGeneratedKeys();
			if (rs.next()) {
				sid = rs.getLong(1);
				for (Sender s : send.getSendList()) {
					String csql = "INSERT INTO My_Sender(sid,tid,platform,uid,status)VALUES(?,?,?,?,?)";
					pstmt2 = conn.prepareStatement(csql);
					pstmt2.setLong(1, sid);
					pstmt2.setString(2, s.getTid());
					pstmt2.setInt(3, s.getPlatform());
					pstmt2.setString(4, s.getUid());
					pstmt2.setInt(5, 1);
					pstmt2.execute();
				}
			}
			conn.commit();
		} catch (SQLException e) {
			e.printStackTrace();
			try {
				conn.rollback();
				sid = 0;
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		} finally {
			this.CloseStatus(pstmt, conn);
		}
		return sid;
	}

	@Override
	public Pagefile<Sending> queryListByStatus(PagerDTO dto, int userId,
			SendStatusEnum status) {
		Pagefile<Sending> pagefile = null;
		Connection conn = null;
		CallableStatement cstmt = null;
		ResultSet rs = null;
		ResultSet rs2 = null;
		String whereSql = null;
		switch (status) {
		case ApprovedToMe:
			// 先要找到当前客户下面有哪些用户的checkid是我，得到一个用户列表
			// 然后显示这些用户发布的微博内容中approved=1的记录
			whereSql = "  where approved=1 and userid in (select id from Base_Users where checkid="
					+ userId
					+ ") "
					+ "  and   id   in (select sid from My_Sender where status=1)";
			break;
		case AllSend:
			// 1、找到当前用户所在的客户
			// 2、找到这个客户下面所有用户列表
			// 3、找到这个用户列表里面所有approved=2的记录，这只是找到了审核通过的
			// 4、审核通过的找到了以后再找这些发布内容对应的Sender里面的Stauts为2的
			// 由于发布的帐号可能由于种种原因发布失败，所以我建议只统计到第3步。
			// 在显示的时候，需要给每个帐号上面标识帐号的发送状态，也就是在Sender列表里面需要显示Status的状态
			// 可以用一个（对勾=发送成功，闹钟=未发布，红叹号=发布失败）
			whereSql = " where sent=0 and  userid in (select ID from Base_Users where CustomID = (select CustomID from Base_Users where ID="
					+ userId + ")) and approved=2";
			break;
		case ApprovedFail:
			whereSql = " where userid="
					+ userId
					+ " and approved=3"
					+ "  and   id   in(select sid from My_Sender where status=3)";
			break;
		case MySend:
			whereSql = " where userid=" + userId + " and approved=2";
			break;
		case AllSendIng:
			// 先找到我的客户，再找到客户下的用户，然后找到这些用户审批通过的微博
			whereSql = " where userid in (select ID from Base_Users where CustomID = (select CustomID from Base_Users where ID="
					+ userId
					+ ")) and approved in (0,2) and sent != 0"
					+ "  and   id   in(select sid from My_Sender where status=1)";
			;
			break;
		default:
			break;
		}
		try {
			conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME)
					.getConnection();
			cstmt = conn.prepareCall("{call SupesoftPage(?,?,?,?,?,?,?)}");
			cstmt.setString(1, "My_Sending");
			cstmt.setString(2, "*");
			cstmt.setInt(3, dto.pageSize);
			cstmt.setInt(4, dto.pageIndex);
			cstmt.setString(5, whereSql);
			cstmt.setString(6, "submit");
			cstmt.setInt(7, 1);
			cstmt.execute();
			pagefile = new Pagefile<Sending>();
			rs = cstmt.getResultSet();
			while (rs.next()) {
				
				Sending send = this.getSending(rs);
				/*
				List<Sender> list = this
						.getSenderList(conn, send.getId(), null); 
				*/
				List<Sender> list =null;
				if (status == SendStatusEnum.AllSendIng) {
					list = this.getSenderList(conn, send.getId(), "1");
				} else if (status == SendStatusEnum.ApprovedFail) {
					list = this.getSenderList(conn, send.getId(), "3");
				} else if (status == SendStatusEnum.ApprovedToMe) {
					list = this.getSenderList(conn, send.getId(), "1");
				}
				if (list != null && list.size() != 0) {
					send.setSendList(list);
					pagefile.getRows().add(send);
					send.setFirstSent(getfirstSend(list));
				}
			}
			if (cstmt.getMoreResults()) {
				rs2 = cstmt.getResultSet();
				if (rs2.next()) {
				    	//pagefile.setRealcount(pagefile.getRows().size());
					     pagefile.setRealcount(rs2.getInt(2));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.CloseStatus(rs2, rs, cstmt, conn);
		}
		return pagefile;
	}

	private long getfirstSend(List<Sender> senders) {
		long created = 0;

		if (senders != null && senders.size() > 0) {
			created = senders.get(0).getCreated();
		}

		for (Sender s : senders) {
			if (s.getCreated() != 0 && s.getCreated() < created) {
				created = s.getCreated();
			}
		}
		return created;
	}

	private List<Sender> getSenderList(Connection conn, long sid, String status) {

		String sql = "select * from My_Sender a left  join Base_Accounts b on a.uid = b.uid where a.sid=?";
		// 	String sql = "select * from My_Sender where sid=?";
		if (!Utility.isNullOrEmpty(status)) {
			sql += " and a.status in(" + status + ")";
		}

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<Sender> list = new ArrayList<Sender>();
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, sid);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				list.add(this.getSender(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.CloseStatus(rs, pstmt, null);
		}
		return list;
	}

	private List<Sender> getSenderList(Connection conn, long sid) {

		// 找到sender里面sid发送失败的和未发送的或者尝试发送次数之内的
		String sql = "select a.*,b.name  from My_Sender a inner join Base_Accounts b on a.uid = b.uid where a.sid=? and (a.status =1 or a.status =3) and a.tries<"
				+ Sending.MAX_TRIES;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<Sender> list = new ArrayList<Sender>();
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, sid);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				list.add(this.getSender(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.CloseStatus(rs, pstmt, null);
		}
		return list;
	}

	private Sender getSender(ResultSet rs) throws SQLException {
		Sender sender = new Sender();
		sender.setId(rs.getInt("id"));
		sender.setSid(rs.getLong("sid"));
		sender.setPlatform(rs.getInt("platform"));
		sender.setUid(rs.getString("uid"));
		sender.setStatus(rs.getInt("status"));
		sender.setTries(rs.getInt("tries"));
		sender.setCreated(rs.getLong("created"));
		sender.setName(rs.getString("name"));
		return sender;
	}

	@Override
	public boolean update(String text, String images, long sid, int approved,
			long sent) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		boolean isUpdate = false;
		try {
			conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME)
					.getConnection();
			String sql = "UPDATE My_Sending SET text=?,images=?,submit=?,approved=?  ,sent=?    WHERE   id=? "
					+ "UPDATE My_Sender SET status=1 WHERE sid=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, text);
			pstmt.setString(2, images);
			pstmt.setTimestamp(3, new Timestamp(new Date().getTime()));
			pstmt.setLong(4, approved);
			pstmt.setLong(5, sent);
			pstmt.setLong(6, sid);
			pstmt.setLong(7, sid);
			int flag = pstmt.executeUpdate();
			if (flag > 0) {
				isUpdate = true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			isUpdate = false;
		} finally {
			this.CloseStatus(pstmt, conn);
		}
		return isUpdate;
	}

	@Override
	public Pagefile<SendInfoDTO> querySendInfo(PagerDTO dto, int customId,
			String startTime, String endTime) {
		Pagefile<SendInfoDTO> pagefile = null;
		Connection conn = null;
		CallableStatement cstmt = null;
		ResultSet rs = null;
		ResultSet rs2 = null;
		try {
			conn = DatabaseUtils.cpdsMap.get("base").getConnection();
			cstmt = conn.prepareCall("{call SupesoftPage(?,?,?,?,?,?,?)}");
			cstmt.setString(1, "Base_Users");
			cstmt.setString(2, "*");
			cstmt.setInt(3, dto.pageSize);
			cstmt.setInt(4, dto.pageIndex);
			cstmt.setString(5, "where CustomID=" + customId);
			cstmt.setString(6, "ID");
			cstmt.setInt(7, 0);
			cstmt.execute();
			pagefile = new Pagefile<SendInfoDTO>();
			rs = cstmt.getResultSet();
			while (rs.next()) {
				SendInfoDTO info = new SendInfoDTO();
				int userid = rs.getInt("ID");
				info.setId(userid);
				info.setName(rs.getString("NickName"));
				info.setSubcount(this.querySubCount(conn, userid, startTime,
						endTime));
				info.setSendcount(this.querySendCount(conn, userid, startTime,
						endTime));
				pagefile.getRows().add(info);
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

	private int querySubCount(Connection conn, int userid, String startTime,
			String endTime) {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int count = 0;
		try {
			String sql = "select COUNT(id) as subcount from My_Sending where userid=? and submit between ? and ? and type=1";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, userid);
			pstmt.setString(2, startTime);
			pstmt.setString(3, endTime);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				count = rs.getInt("subcount");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.CloseStatus(rs, pstmt, null);
		}
		return count;
	}

	private int querySendCount(Connection conn, int userid, String startTime,
			String endTime) {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int count = 0;
		try {
			//String sql = "select COUNT(id) as sendcount from My_Sending where userid=? and submit between ? and ? and type=1 and id in (select sid from My_Sender where status=2)";
			String sql = "select COUNT(id) as sendcount from My_Sending where userid=? and submit between ? and ? and type=1 and  id   not   in (select sid from My_Sender where status=3)";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, userid);
			pstmt.setString(2, startTime);
			pstmt.setString(3, endTime);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				count = rs.getInt("sendcount");
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
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return count;
	}

	public void updateOrgID(long sid, String orgID) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		try {
			conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME)
					.getConnection();
			String sql = "UPDATE My_Sending SET orginalid=? WHERE id=? ";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, orgID);
			pstmt.setLong(2, sid);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.CloseStatus(pstmt, conn);
		}
	}

	@Override
	public boolean updateSendStatus(long sendingid, int status, long senderid,
			String tid, int tries, long created) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		boolean isCheck = false;
		try {
			conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME)
					.getConnection();
			String sql = "UPDATE My_Sender SET status=?,tid=?,tries=?,created=? WHERE id=? ";
			// + "UPDATE My_Sending SET approved=2 WHERE id=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, status);
			pstmt.setString(2, tid);
			pstmt.setInt(3, tries);
			pstmt.setLong(4, created);
			pstmt.setLong(5, senderid);
			int flag = pstmt.executeUpdate();
			if (flag > 0) {
				isCheck = true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			isCheck = false;
		} finally {
			this.CloseStatus(pstmt, conn);
		}
		return isCheck;
	}

	@Override
	public Pagefile<Sending> querySubmitInfo(Pager pager, String startTime,
			String endTime, int userid, SendCountEnum senum) {
		String whereSql = "";
		switch (senum) {
		case submit:
			whereSql = "where type=1 and submit between '" + startTime
					+ "' and '" + endTime + "' and userid=" + userid;
			break;
		case accept:
			whereSql = "where type=1 and submit between '" + startTime
					+ "' and '" + endTime + "' and userid=" + userid 
				//	+ " and status=2";
					+" and id not in(select sid from My_Sender where status=3)";
			break;
		default:
			break;
		}
		Connection conn = null;
		CallableStatement cstmt = null;
		ResultSet rs = null;
		ResultSet rs2 = null;
		Pagefile<Sending> pagefile = null;
		try {
			conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME)
					.getConnection();
			cstmt = conn.prepareCall("{call SupesoftPage(?,?,?,?,?,?,?)}");
			cstmt.setString(1, "My_Sending");
			cstmt.setString(2, "*");
			cstmt.setInt(3, pager.getPageSize());
			cstmt.setInt(4, pager.getPageIndex());
			cstmt.setString(5, whereSql);
			cstmt.setString(6, "submit");
			cstmt.setInt(7, 1);
			cstmt.execute();
			pagefile = new Pagefile<Sending>();
			rs = cstmt.getResultSet();
			while (rs.next()) {
				Sending send = this.getSending(rs);
				if (senum == SendCountEnum.accept) {
					send.setSendList(this.getSenderList(conn, send.getId(), "1,2"));
				} else if (senum == SendCountEnum.submit) {
					send.setSendList(this.getSenderList(conn, send.getId(), "1,2,3"));
				} 
				pagefile.getRows().add(send);
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
	public Sending querySendById(long sid) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Sending send = null;
		try {
			conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME)
					.getConnection();
			String sql = "select * from My_Sending where id=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, sid);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				send = this.getSending(rs);
				send.setSendList(this.getSenderList(conn, sid));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.CloseStatus(rs, pstmt, conn);
		}
		return send;
	}

	private Sending getSending(ResultSet rs) throws SQLException {
		Sending send = new Sending();
		send.setId(rs.getLong("id"));
		send.setText(rs.getString("text"));
		send.setImages(rs.getString("images"));
		long sent = rs.getLong("sent");
		/*
		if (sent == 0) {
			send.setSent(new Date().getTime());
		} else {
			send.setSent(sent);
		}
		*/
		send.setSent(sent);
		send.setSubmit(rs.getString("submit"));
		send.setUserid(rs.getInt("userid"));
		send.setType(rs.getInt("type"));
		send.setSourceid(rs.getString("sourceid"));
		send.setFlag(rs.getBoolean("flag"));
		send.setApproved(rs.getInt("approved"));
		send.setAuditor(rs.getInt("auditor"));
		send.setOrgId(rs.getString("orginalid"));
		return send;
	}

	@Override
	public Pagefile<Sending> queryListByStatus(PagerDTO dto, int userid,
			int customid, PostType type, String startTime, String endTime,
			SendStatusEnum status) {
		Pagefile<Sending> pagefile = null;
		Connection conn = null;
		CallableStatement cstmt = null;
		ResultSet rs = null;
		ResultSet rs2 = null;
		String whereSql = null;
		switch (status) {
		case ApprovedToMe:
			// 先要找到当前客户下面有哪些用户的checkid是我，得到一个用户列表
			// 然后显示这些用户发布的微博内容中approved=1的记录
			// whereSql = "where approved=1 and userid in (select ID from
			// Base_Users where CustomID=(select CustomID from Base_Users where
			// checkid="+userid+"))";
			break;
		case AllSend:
			// 1、找到当前用户所在的客户
			// 2、找到这个客户下面所有用户列表
			// 3、找到这个用户列表里面所有approved=2的记录，这只是找到了审核通过的
			// 4、审核通过的找到了以后再找这些发布内容对应的Sender里面的Stauts为2的
			// 由于发布的帐号可能由于种种原因发布失败，所以我建议只统计到第3步。
			// 在显示的时候，需要给每个帐号上面标识帐号的发送状态，也就是在Sender列表里面需要显示Status的状态
			// 可以用一个（对勾=发送成功，闹钟=未发布，红叹号=发布失败）
			if (userid == -1) {
				whereSql = "where userid in (select ID from Base_Users where CustomID = "
						+ customid
						+ ") and approved in (0,2) and submit between '"
						+ startTime
						+ "' and '"
						+ endTime
						+ "' and type="
						+ Utility.getPostType(type)
						+ "  and id  in(select sid from My_Sender where status=2)";
				;
			} else {
				whereSql = "where userid ="
						+ userid
						+ " and approved in (0,2) and submit between '"
						+ startTime
						+ "' and '"
						+ endTime
						+ "' and type="
						+ Utility.getPostType(type)
						+ "  and id  in(select sid from My_Sender where status=2)";
			}
			break;
		case ApprovedFail:
			// whereSql = "where userid=" + userid + " and approved=3";
			break;
		case MySend:
			whereSql = "where userid="
					+ userid
					+ "  and approved in (0,2) and submit between '"
					+ startTime
					+ "'  and '"
					+ endTime
					+ "' and type="
					+ +Utility.getPostType(type)
					+ "  and id not in(select sid from My_Sender where status=1)";
			break;
		case AllSendIng:
			// 先找到我的客户，再找到客户下的用户，然后找到这些用户审批通过的微博
			// whereSql = "where userid in (select ID from Base_Users where
			// CustomID = (select CustomID from Base_Users where ID="+userid+"))
			// and approved=2 and sent != 0";
			break;
		default:
			break;
		}
		try {
			conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME)
					.getConnection();
			cstmt = conn.prepareCall("{call SupesoftPage(?,?,?,?,?,?,?)}");
			cstmt.setString(1, "My_Sending");
			cstmt.setString(2, "*");
			cstmt.setInt(3, dto.pageSize);
			cstmt.setInt(4, dto.pageIndex);
			// String where = "where userid=" + userid + "and submit between '"
			// + startTime + "' and '" + endTime + "' and type="
			// + Utility.getPostType(type);
			cstmt.setString(5, whereSql);
			cstmt.setString(6, "submit");
			cstmt.setInt(7, 1);
			cstmt.execute();
			pagefile = new Pagefile<Sending>();
			rs = cstmt.getResultSet();
			while (rs.next()) {
				Sending send = this.getSending(rs);
				List<Sender> sendList = null;
				if (status == SendStatusEnum.MySend) {

					sendList = this.getSenderList(conn, send.getId(), "2,3");
				} else if (status == SendStatusEnum.AllSend) {
					sendList = this.getSenderList(conn, send.getId(), "2");
				}
				if (sendList != null && sendList.size() != 0) {
					send.setSendList(sendList);
					pagefile.getRows().add(send);
					send.setFirstSent(getfirstSend(sendList));
				}
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
			this.CloseStatus(rs2, rs, cstmt, conn);
		}
		return pagefile;
	}

	@Override
	public boolean getApprovedCheck(int userid) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		boolean isCheck = false;
		try {
			conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME)
					.getConnection();
			String sql = "select checkid from Base_Users where ID=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, userid);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				int checkid = rs.getInt("checkid");
				if (checkid > 0) {
					isCheck = true;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.CloseStatus(rs, pstmt, conn);
		}
		return isCheck;
	}

	private void CloseStatus(ResultSet rs, PreparedStatement pstmt,
			Connection conn) {
		this.CloseStatus(null, null, rs, null, pstmt, conn);
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
	public Pagefile<PostDealDTO> querySendDeal(Pager pager, String startTime,
			String endTime, String uid, int customId) {
		Connection conn = null;
		CallableStatement cstmt = null;
		ResultSet rs = null;
		ResultSet rs2 = null;
		Pagefile<PostDealDTO> pagefile = null;
		try {
			conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME)
					.getConnection();
			cstmt = conn.prepareCall("{call SupesoftPage(?,?,?,?,?,?,?)}");
			cstmt.setString(1, "Base_Users");
			cstmt.setString(2, "*");
			cstmt.setInt(3, pager.getPageSize());
			cstmt.setInt(4, pager.getPageIndex());
			cstmt.setString(5, "where CustomID=" + customId);
			cstmt.setString(6, "ID");
			cstmt.setInt(7, 0);
			cstmt.execute();
			pagefile = new Pagefile<PostDealDTO>();
			rs = cstmt.getResultSet();
			while (rs.next()) {
				int userid = rs.getInt("ID");
				String name = rs.getString("NickName");
				int comment = this.getCommentByUserid(conn, userid, 3,
						startTime, endTime, uid);
				int repost = this.getCommentByUserid(conn, userid, 2,
						startTime, endTime, uid);
				PostDealDTO dto = new PostDealDTO();
				dto.setId(userid);
				dto.setUseName(name);
				dto.setReposts(repost);
				dto.setComments(comment);
				pagefile.getRows().add(dto);
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

	private int getCommentByUserid(Connection conn, int userid, int type,
			String startTime, String endTime, String uid) {
		String sql = "select COUNT(id) as count from My_Sending where userid=? and type=? and submit between ? and ?"
				+ " and id in (select sid from My_Sender where uid=?  and status=2) ";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int count = 0;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, userid);
			pstmt.setInt(2, type);
			pstmt.setString(3, startTime);
			pstmt.setString(4, endTime);
			pstmt.setString(5, uid);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				count = rs.getInt("count");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.CloseStatus(rs, pstmt, null);
		}
		return count;
	}

	@Override
	public Pagefile<Sending> queryCommentList(Pager pager, String startTime,
			String endTime, String uid, int userid, WeiboType type) {
		int wtype = 0;
		switch (type) {
		case RepostPost:
			wtype = 2;
			break;
		case CommentPost:
			wtype = 3;
			break;
		default:
			break;
		}
		String whereSql = "where userid=" + userid + " and type=" + wtype
				+ " and submit between '" + startTime + "' and '" + endTime
				+ "'" + "and id in (select sid from My_Sender where uid='"
				+ uid + "' and status=2)";
		Connection conn = null;
		CallableStatement cstmt = null;
		ResultSet rs = null;
		ResultSet rs2 = null;
		Pagefile<Sending> pagefile = null;
		try {
			conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME)
					.getConnection();
			cstmt = conn.prepareCall("{call SupesoftPage(?,?,?,?,?,?,?)}");
			cstmt.setString(1, "My_Sending");
			cstmt.setString(2, "*");
			cstmt.setInt(3, pager.getPageSize());
			cstmt.setInt(4, pager.getPageIndex());
			cstmt.setString(5, whereSql);
			cstmt.setString(6, "id");
			cstmt.setInt(7, 1);
			cstmt.execute();
			pagefile = new Pagefile<Sending>();
			rs = cstmt.getResultSet();
			while (rs.next()) {
				Sending send = this.getSending(rs);
				send.setSendList(this
						.getSenderList(conn, send.getId(), "1,2,3"));
				pagefile.getRows().add(send);
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
	public boolean updateApprovedFail(int checkid, int userid,int checkuserid){
		Connection conn = null;
		CallableStatement cstmt = null;
		boolean isCheck = false;
		try {
			conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME)
					.getConnection();
			//cstmt = conn.prepareCall("{call sp_Sending_updateApprovedFail(?,?)}");
			cstmt = conn.prepareCall("{call sp_Sending_updateApprovedFailPublicManage(?,?,?,?)}"); 
			cstmt.setInt(1, checkid);
			cstmt.setInt(2, userid);
			cstmt.setInt(3, checkuserid);
			cstmt.registerOutParameter(4,java.sql.Types.INTEGER );
			cstmt.execute();
			int flag=cstmt.getInt(4);
			if (flag > 0) {
				isCheck = true;
			} 
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.CloseStatus(cstmt, conn);
		}
		return isCheck;
	}

	@Override
	public List<Sending> queryAllUnSending() {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<Sending> list = null;
		try {
			conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME)
					.getConnection();
			// 用户如果删除或者锁定了，则不能用这个用户发送
			String sql = "select * from My_Sending where approved in (0,2) and id in (select sid from My_Sender where (status =1 or status =3) and tries<"
					+ Sending.MAX_TRIES
					+ ") and userid in (select id from Base_Users where IsEnabled=1)";
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			list = new ArrayList<Sending>();
			while (rs.next()) {
				Sending sending = this.getSending(rs);
				sending.setSendList(this.getSenderList(conn, sending.getId()));
				list.add(sending);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.CloseStatus(rs, pstmt, conn);
		}
		return list;
	}
}
