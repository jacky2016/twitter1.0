package com.xunku.daoImpl.event;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import com.xunku.constant.PortalCST;
import com.xunku.dao.event.KeyPointTopDao;
import com.xunku.pojo.base.Post;
import com.xunku.pojo.event.KeyPoint;
import com.xunku.utils.DatabaseUtils;

public class KeyPointTopDaoImpl implements KeyPointTopDao {

	@Override
	public List<Post> queryKeyPointTop(int eventId) {
	    return null;
		/*Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		PreparedStatement pstmt2 = null;
		ResultSet rs2 = null;
		Connection dbcon = null;
		PreparedStatement pstmt3 = null;
		ResultSet rs3 = null;
		List<Post> postList = new ArrayList<Post>();
		try {
			conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME)
					.getConnection();
			// 获取top5的事件关键点
			String sql = "select top 5 * from Event_KeyPoint_Top100 where eventId=? order by reposts desc";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, eventId);
			rs = pstmt.executeQuery();
			StringBuffer buf = new StringBuffer();
			while (rs.next()) {
				buf.append(rs.getString("tid") + ",");
			}
			String tid = null;
			if (buf.length() != 0) {
				tid = buf.toString().substring(0, buf.toString().length() - 1);
			}
			// 获取事件服务器地址和表名
			String esql = "SELECT a.tab,b.server FROM Event_List a JOIN Base_DBServer b ON a.dbserver=b.id WHERE a.id=?";
			pstmt2 = conn.prepareStatement(esql);
			pstmt2.setInt(1, eventId);
			rs2 = pstmt2.executeQuery();
			while (rs2.next()) {
				String table = rs2.getString("tab");
				String dbserver = rs2.getString("server");
				// 获取top5微博的信息
				dbcon = DatabaseUtils.getConnection(dbserver);
				String dbsql = null;
				if (tid == null) {
					dbsql = "select * from " + table + " where tid in (0)";
				} else {
					dbsql = "select * from " + table + " where tid in (" + tid
							+ ")";
				}
				pstmt3 = dbcon.prepareStatement(dbsql);
				rs3 = pstmt3.executeQuery();
				while (rs3.next()) {
					Post p = new Post();
					p.setComments(rs3.getInt("comments"));
					// TODO 这里只能取得日期无法取得时间 需要修改
					p.setCreated(rs3.getDate("created").getTime());
					p.setFrom(Utility.xunkuFromGet(rs3.getString("xfrom")));
					p.setId(rs3.getInt("id"));
					p.setImages(Utility.getImageList(rs3.getString("images")));
					p.setLayer(rs3.getInt("layer"));
					p.setPlatform(Utility.getPlatform(rs3.getInt("platform")));
					p.setReposts(rs3.getInt("reposts"));
					if (Utility.isNullOrEmpty(rs3.getString("source"))) {
						// 转发源只有一个Tid，输出时通过tid来构造转发源对象
						p.setSourceId(rs3.getString("source"));
					}
					p.setText(rs3.getString("text"));
					p.setTid(rs3.getString("tid"));
					p.setType(Utility.getPostType(rs3.getInt("type")));
					p.setUcode(rs3.getString("ucode"));
					p.setUid(rs3.getString("uid"));
					//p.setUrl(rs3.getString("url"));
					postList.add(p);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs3 != null) {
					rs3.close();
				}
				if (pstmt3 != null) {
					pstmt3.close();
				}
				if (dbcon != null) {
					dbcon.close();
				}
				if (rs2 != null) {
					rs2.close();
				}
				if (pstmt2 != null) {
					pstmt2.close();
				}
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
		return postList;*/
	}

	@Override
	public void insertKeyPoint(KeyPoint point) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		try {
			conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME)
					.getConnection();
			String sql = "INSERT INTO Event_KeyPoint_Top100 (eventId,tid,reposts) VALUES (?,?,?)";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, point.getEventId());
			pstmt.setString(2, point.getTid());
			pstmt.setInt(3, point.getReposts());
			pstmt.execute();
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
	public boolean deleteByEvent(int eventId) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		boolean isDelete = false;
		try {
			conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME)
					.getConnection();
			String sql = "DELETE FROM Event_KeyPoint_Top100 WHERE eventId=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, eventId);
			int flag = pstmt.executeUpdate();
			if (flag > 0) {
				isDelete = true;
			} else {
				isDelete = false;
			}
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
		return isDelete;
	}
}
