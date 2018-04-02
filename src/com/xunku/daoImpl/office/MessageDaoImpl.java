package com.xunku.daoImpl.office;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.xunku.constant.PortalCST;
import com.xunku.dao.office.MessageDao;
import com.xunku.dto.office.MessageDTO;
import com.xunku.dto.office.MessageVO;
import com.xunku.pojo.office.Message;
import com.xunku.utils.DatabaseUtils;
import com.xunku.utils.Pagefile;

public class MessageDaoImpl implements MessageDao {

	@Override
	public void insert(MessageDTO dto) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		PreparedStatement pstmt2 = null;
		ResultSet rs = null;
		try {
			String msgSql = "INSERT INTO Office_Messages (Type,Message,Source,Rank,Userid) VALUES (?,?,?,?,?)";
			String revSql = "INSERT INTO Office_MessageReceivers (MsgID,Receiver,IsHandle) VALUES (?,?,?)";
			conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME)
					.getConnection();
			conn.setAutoCommit(false);
			pstmt = conn.prepareStatement(msgSql,
					PreparedStatement.RETURN_GENERATED_KEYS);
			pstmt.setInt(1, dto.getType());
			pstmt.setString(2, dto.getMessage());
			pstmt.setString(3, dto.getSource());
			pstmt.setInt(4, dto.getRank());
			pstmt.setInt(5, dto.getUserid());
			pstmt.execute();
			rs = pstmt.getGeneratedKeys();
			if (rs.next()) {
				int msgId = rs.getInt(1);
				if (dto.getReceiver().length == 0) {
					return;
				}
				for (int i = 0; i < dto.getReceiver().length; i++) {
					pstmt2 = conn.prepareStatement(revSql);
					pstmt2.setInt(1, msgId);
					pstmt2.setInt(2, dto.getReceiver()[i]);
					pstmt2.setBoolean(3, true);
					pstmt2.execute();
				}
			}
			conn.commit();
		} catch (SQLException e) {
			e.printStackTrace();
			try {
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (pstmt2 != null) {
					pstmt2.close();
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
	}

	@Override
	public Pagefile<MessageVO> queryReceiveMsg(MessageVO vo) {
		Connection conn = null;
		CallableStatement cstmt = null;
		ResultSet rs = null;
		ResultSet rs2 = null;
		Pagefile<MessageVO> pagefile = null;
		String whereSql = null;
		switch (vo.getStatus()) {
		case 0:
			whereSql = "where Status=0 and Receiver=" + vo.getUserId();
			break;
		case 1:
			whereSql = "where Status=1 and Receiver=" + vo.getUserId();
			break;
		default:
			whereSql = "where Receiver=" + vo.getUserId();
			break;
		}
		try {
			conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME)
					.getConnection();
			cstmt = conn.prepareCall("{call SupesoftPage(?,?,?,?,?,?,?)}");
			cstmt.setString(1, "vi_Message_queryReceiveMsg");
			cstmt.setString(2, "*");
			cstmt.setInt(3, vo.getPageSize());
			cstmt.setInt(4, vo.getPageIndex());
			cstmt.setString(5, whereSql);
			cstmt.setString(6, "ID");
			cstmt.setInt(7, 1);
			cstmt.execute();
			pagefile = new Pagefile<MessageVO>();
			rs = cstmt.getResultSet();
			while (rs.next()) {
				MessageVO mv = new MessageVO();
				mv.setId(rs.getInt("ID"));
				mv.setMessage(rs.getString("Message"));
				if (rs.getInt("Userid") == 0) {
					mv.setReceiver("系统");
				} else {
					mv.setReceiver(queryUserName(conn, rs.getInt("Userid")));
				}
				mv.setRank(rs.getInt("Rank"));
				mv.setStatus(rs.getInt("Status"));
				mv.setReceiveTime(rs.getString("SentTme"));
				pagefile.getRows().add(mv);
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
			try {
				if (rs2 != null) {
					rs2.close();
				}
				if (rs != null) {
					rs.close();
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
		return pagefile;
	}

	private String queryUserName(Connection conn, int userId) {
		String sql = "select NickName from Base_Users where ID=?";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String result = null;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, userId);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				result = rs.getString("NickName");
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
		return result;
	}

	@Override
	public int queryByCount(int userId, int stutus) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int count = 0;
		try {
			conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME)
					.getConnection();
			String sql = "select COUNT(ID) as count from Office_MessageReceivers where Receiver=? and Status=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, userId);
			pstmt.setInt(2, stutus);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				count = rs.getInt("count");
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
		return count;
	}

	@Override
	public String queryMessageByID(int id) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String result = null;
		try {
			conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME)
					.getConnection();
			String sql = "SELECT Message FROM Office_Messages WHERE ID=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, id);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				result = rs.getString("Message");
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
		return result;
	}

	@Override
	public void updateStatus(int msgId, int userId) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		try {
			conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME)
					.getConnection();
			String sql = "UPDATE Office_MessageReceivers SET Status=1 WHERE MsgID=? and Receiver=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, msgId);
			pstmt.setInt(2, userId);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
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
	}

	@Override
	public void deleteByID(int id) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		try {
			conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME)
					.getConnection();
			String sql = "DELETE FROM Office_MessageReceivers WHERE MsgID=? "
					+ "DELETE FROM Office_Messages WHERE ID=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, id);
			pstmt.setInt(2, id);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
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
	}

}
