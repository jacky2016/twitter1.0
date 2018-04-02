package com.xunku.daoImpl.office;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.xunku.app.Utility;
import com.xunku.app.helpers.DbHelper;
import com.xunku.app.interfaces.IFiller;
import com.xunku.app.model.Query;
import com.xunku.constant.PortalCST;
import com.xunku.dao.office.WeiboWarnDao;
import com.xunku.dto.pushservices.WarnServiceListDTO;
import com.xunku.dto.pushservices.WarnServicesDTO;
import com.xunku.pojo.base.Pager;
import com.xunku.pojo.office.WeiboWarn;
import com.xunku.utils.DatabaseUtils;
import com.xunku.utils.Pagefile;

public class WeiboWarnDaoImpl implements WeiboWarnDao, IFiller<WeiboWarn> {

	public int pointer = 0;

	@Override
	public int getCanenlWeiboWarnServices(String tid, int customid) {
		pointer = 0;
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<Integer> list = new ArrayList<Integer>();
		try {
			conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME)
					.getConnection();
			String sql = "select  id  from Office_Weibo_Warning where tid=? and customid=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, tid);
			pstmt.setInt(2, customid);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				list.add(rs.getInt("id"));
			}
			/*
			 * boolean flag= this.deleteWeiBoWarning(conn, tid, customid);
			 * if(flag){ return 0; } flag=this.deleteWarnList(conn,list);
			 * if(flag){ return 0; } return 1;
			 */
			List<WarnServicesDTO> wsdtos = checkWeiBoIfOccuer(conn, list);

			// flag 为false为删除成功 true 则为失败
			boolean flag = this.deleteWeiboWarningAndWarnList(conn, tid,
					customid, list, wsdtos);
			if (flag) {
				return 0;
			} else {
				// 说明是假删除
				if (pointer == 500) {
					return pointer;
				}
				return 1;

			}
		} catch (SQLException e) {
			e.printStackTrace();
			return 0;
		} finally {
			Utility.closeConnection(conn, pstmt, rs);
		}
	}

	/**
	 * 删除前判断当前的是否是已经发生的微博预警or 未发生的的微博信息
	 */
	private List<WarnServicesDTO> checkWeiBoIfOccuer(Connection conn,
			List<Integer> list) {
		PreparedStatement pstmt2 = null;
		ResultSet rs = null;
		List<WarnServicesDTO> wsdtos = new ArrayList<WarnServicesDTO>();
		String sql = "select    warnType, status   from  dbo.Office_WarnList   where warnid=?";
		for (int every : list) {
			try {
				pstmt2 = conn.prepareStatement(sql);
				pstmt2.setInt(1, every);
				rs = pstmt2.executeQuery();
				while (rs.next()) {
					WarnServicesDTO dto = new WarnServicesDTO();
					dto.warnType = rs.getInt("warnType");
					dto.warnStatus = rs.getInt("status");
					wsdtos.add(dto);
					//break;
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} // for
		return wsdtos;
	}

	/**
	 * 删除 Office_Weibo_Warning表的 行 根据 customid 和tid 和删除 Office_WarnList表的 行
	 * 根据warnid
	 */
	private boolean deleteWeiboWarningAndWarnList(Connection conn, String tid,
			int customid, List<Integer> list, List<WarnServicesDTO> wsdtos) {
		PreparedStatement pstmt2 = null;
		String sql = "delete    from Office_Weibo_Warning  where tid=? and customid=? "
				+ "\r\n";
		String sql2 = "delete    from    Office_WarnList    where   warnid=";
		String isDeleteSQL = "update  Office_Weibo_Warning   set isDelete=1  ,  isrunning =0    where  tid=?  and customid=? "
				+ "\r\n"; // 已发生的微博预警假删除
		String  SQLSpeaical="";
		boolean flag = true;
		//获取当前的 weibo的信息的id  --- 也就是获取Office_Weibo_Warning 的id
		int id= list.get(0);
		if(wsdtos.size()==0){
				// String sql3 = "";
				// sql3 += sql2 + list.get(i) + "\r\n";
				// sql = sql + sql3;
				flag = true;
				try {
					conn.setAutoCommit(false);
					pstmt2 = conn.prepareStatement(sql);
					pstmt2.setString(1, tid);
					pstmt2.setInt(2, customid);
					int tt = pstmt2.executeUpdate();
					if (tt > 0) {
						flag = false;
					}
					conn.commit();
				} catch (SQLException e) {
					// e.printStackTrace();
					try {
						conn.rollback();
						conn.setAutoCommit(true);// 设置提交方式为默认方式
						flag = true;
					} catch (SQLException e1) {
						e1.printStackTrace();
					}
				} finally {
					Utility.closeConnection(null, pstmt2, null);
				}
		}
		//  Office_WarnList 说明有数据了
		else{
		 for(WarnServicesDTO  dto  :wsdtos  ){
				if (dto.warnType == 3 && dto.warnStatus == 1) {
					try {
						flag = true;
						pstmt2 = conn.prepareStatement(isDeleteSQL);
						pstmt2.setString(1, tid);
						pstmt2.setInt(2, customid);
						int tt = pstmt2.executeUpdate();
						if (tt > 0) {
							flag = false;
							pointer = 500;
						}
					} catch (SQLException e) {
						// e.printStackTrace();
						flag = true;
					}

				} // if
				// 未发生的微博信息
				else {
					//判断也有可能是一部分已经发生预警 
					//isFlags 为false    为 存在  未发生预警里消息的帖子也存在已经发生预警消息里
					boolean  isFlags= checkAllWarnStatus(conn, id);
					if(!isFlags){
							//删除此条已经发生预警的,也有未发生预警微博中的所有未发生预警的消息
						deleteWarnAndUnWarn(conn ,id);
					}  // isFlags == false 
					else if(isFlags){
						String sql3 = "";
						sql3 += sql2 +id  + "\r\n";
						sql = sql + sql3;
						flag = true;
						try {
							conn.setAutoCommit(false);
							pstmt2 = conn.prepareStatement(sql);
							pstmt2.setString(1, tid);
							pstmt2.setInt(2, customid);
							int tt = pstmt2.executeUpdate();
							if (tt > 0) {
								flag = false;
								break;
							}
							conn.commit();
						} catch (SQLException e) {
							// e.printStackTrace();
							try {
								conn.rollback();
								conn.setAutoCommit(true);// 设置提交方式为默认方式
								flag = true;
								break;
							} catch (SQLException e1) {
								e1.printStackTrace();
								break;
							}
						}
					}  //else if     isFlags=true 
				} // else
    		 }	  // for
			Utility.closeConnection(null, pstmt2, null);
		}  // Office_WarnList 说明有数据了结束
		
		
		/*
		 WarnServicesDTO dto = null;
		  if (wsdtos.size() != 0) {
			dto = wsdtos.get(0);
		}
		*/
		//-----------------------
		/*
		for (int i = 0; i < list.size(); i++) {
			WarnServicesDTO dto =null;
			  if (wsdtos.size() != 0) {
					dto = wsdtos.get(i);
				}
			// 已经发生的微博预警

			if (dto != null) {
				if (dto.warnType == 3 && dto.warnStatus == 1) {
					try {
						flag = true;
						pstmt2 = conn.prepareStatement(isDeleteSQL);
						pstmt2.setString(1, tid);
						pstmt2.setInt(2, customid);
						int tt = pstmt2.executeUpdate();
						if (tt > 0) {
							flag = false;
							pointer = 500;
						}
						break;
					} catch (SQLException e) {
						// e.printStackTrace();
						flag = true;
						break;
					} finally {
						Utility.closeConnection(null, pstmt2, null);
					}

				} // if
				// 未发生的微博信息
				else {
					String sql3 = "";
					sql3 += sql2 + list.get(i) + "\r\n";
					sql = sql + sql3;
					flag = true;
					try {
						conn.setAutoCommit(false);
						pstmt2 = conn.prepareStatement(sql);
						pstmt2.setString(1, tid);
						pstmt2.setInt(2, customid);
						int tt = pstmt2.executeUpdate();
						if (tt > 0) {
							flag = false;
						}
						conn.commit();
					} catch (SQLException e) {
						// e.printStackTrace();
						try {
							conn.rollback();
							conn.setAutoCommit(true);// 设置提交方式为默认方式
							flag = true;
						} catch (SQLException e1) {
							e1.printStackTrace();
						}
					} finally {
						Utility.closeConnection(null, pstmt2, null);
					}

				} // else
			}
			// dto= =null
			else {
				// String sql3 = "";
				// sql3 += sql2 + list.get(i) + "\r\n";
				// sql = sql + sql3;
				flag = true;
				try {
					conn.setAutoCommit(false);
					pstmt2 = conn.prepareStatement(sql);
					pstmt2.setString(1, tid);
					pstmt2.setInt(2, customid);
					int tt = pstmt2.executeUpdate();
					if (tt > 0) {
						flag = false;
					}
					conn.commit();
				} catch (SQLException e) {
					// e.printStackTrace();
					try {
						conn.rollback();
						conn.setAutoCommit(true);// 设置提交方式为默认方式
						flag = true;
					} catch (SQLException e1) {
						e1.printStackTrace();
					}
				} finally {
					Utility.closeConnection(null, pstmt2, null);
				}

			} // dto==null 结束

		} // for

		*/
		
		/*
		 * for (int every : list){ sql3+=sql2+every+"\r\n"; } sql=sql+sql3; try {
		 * conn.setAutoCommit(false); pstmt2 = conn.prepareStatement(sql);
		 * pstmt2.setString(1, tid); pstmt2.setInt(2, customid); int
		 * tt=pstmt2.executeUpdate(); if(tt>0){ flag=false; } conn.commit(); }
		 * catch (SQLException e) { e.printStackTrace(); try { conn.rollback();
		 * conn.setAutoCommit(true);//设置提交方式为默认方式 flag=true; } catch
		 * (SQLException e1) { e1.printStackTrace(); } } finally {
		 * Utility.closeConnection(null, pstmt2, null); }
		 */

		return flag;
	}

	
	/**
	 * 删除 Office_WarnList表的  同时具有已经发生预警的和未发生预警的微博中的未发生预警的全部消息
	 */
		private    void   deleteWarnAndUnWarn(Connection conn ,  int  warnid){
			String  sql ="delete    from    Office_WarnList    where   warnid=? and  status=0";
			 PreparedStatement pstmt2 = null;
				try {
					pstmt2 = conn.prepareStatement(sql);
					pstmt2.setInt(1, warnid);
				    pstmt2.executeUpdate();
				} catch (SQLException e) {
					e.printStackTrace();
				}
				finally {
					Utility.closeConnection(null, pstmt2, null);
				}
		}
	
	/**
	 * 判断 Office_WarnList表的      是不是有一部分已经发生预警 一部分不发生预警
	 * , int warnType  == 3, int status== 1 
	 *  flag 为false  即为存在 已经发生的
	 */
	
	  private  boolean     checkAllWarnStatus(Connection conn ,  int  warnid){
		  PreparedStatement pstmt2 = null;
		  ResultSet rs = null;
		  boolean flag=true;
		  String sql = "select * from Office_WarnList where warnid=?  and  status=1";
			try {
				pstmt2 = conn.prepareStatement(sql);
				pstmt2.setInt(1, warnid);
				rs = pstmt2.executeQuery();
				while (rs.next()) {
				       flag=false;
					   break;
				}

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			finally {
				Utility.closeConnection(null, pstmt2, rs);
			}
		  
		  return  flag;
	  }
	
	
	/**
	 * 
	 * 
	 * 删除 Office_Weibo_Warning表的 行 根据 customid 和tid
	 */
	private boolean deleteWeiBoWarning(Connection conn, String tid, int customid) {
		PreparedStatement pstmt2 = null;
		try {
			String sql = "delete    from Office_Weibo_Warning  where tid=? and customid=?";
			pstmt2 = conn.prepareStatement(sql);
			pstmt2.setString(1, tid);
			pstmt2.setInt(2, customid);
			return pstmt2.execute();

		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		} finally {
			Utility.closeConnection(null, pstmt2, null);
		}
	}

	/**
	 * 删除 Office_WarnList表的 行 根据warnid
	 */
	private boolean deleteWarnList(Connection conn, List<Integer> list) {
		PreparedStatement pstmt3 = null;
		try {
			String sql = "delete    from    Office_WarnList    where   warnid=?";
			pstmt3 = conn.prepareStatement(sql);
			for (int every : list) {
				pstmt3.setInt(1, every);
				pstmt3.execute();
			}
			return false;
		} catch (SQLException e) {
			e.printStackTrace();
			return true;
		} finally {
			Utility.closeConnection(null, pstmt3, null);
		}
	}

	@Override
	public int getCurrentWeiboWarnCount(int customid) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int count = 0;
		try {
			conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME)
					.getConnection();
			String sql = "SELECT COUNT(id) as count FROM   Office_Weibo_Warning  WHERE customid=?  and  isDelete=0";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, customid);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				count = rs.getInt("count");
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
		return count;
	}

	@Override
	public boolean deleteById(int id) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		boolean isDelete = false;
		try {
			conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME)
					.getConnection();
			String sql = "delete from Office_Weibo_Warning where id=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, id);
			int flag = pstmt.executeUpdate();
			if (flag > 0) {
				isDelete = true;
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

	@Override
	public WeiboWarn query(int customid, String tid) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		WeiboWarn result = null;
		try {
			conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME)
					.getConnection();
			String sql = "select * from Office_Weibo_Warning where tid=? and customid=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, tid);
			pstmt.setInt(2, customid);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				result = this.fill(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			Utility.closeConnection(conn, pstmt, rs);
		}
		return result;
	}

	@Override
	public int insert(WeiboWarn warn) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int wid = 0;
		try {
			conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME)
					.getConnection();
			String sql = "insert into Office_Weibo_Warning (tid,platform,comment,repost,receiver,type,customid,endtime,name,text,currepost,curcoment) values (?,?,?,?,?,?,?,?,?,?,?,?)";
			pstmt = conn.prepareStatement(sql,
					PreparedStatement.RETURN_GENERATED_KEYS);
			pstmt.setString(1, warn.getTid());
			pstmt.setInt(2, Utility.getPlatform(warn.getPlatform()));
			pstmt.setInt(3, warn.getComment());
			pstmt.setInt(4, warn.getRepost());
			pstmt.setString(5, warn.getReceiver());
			pstmt.setString(6, warn.getType());
			pstmt.setInt(7, warn.getCustomid());
			pstmt.setTimestamp(8, new java.sql.Timestamp(warn.getTime()));
			pstmt.setString(9, warn.getAuthor());
			pstmt.setString(10, warn.getText());
			pstmt.setInt(11, warn.getCurrepost());
			pstmt.setInt(12, warn.getCurcomment());
			pstmt.execute();
			rs = pstmt.getGeneratedKeys();
			if (rs.next()) {
				wid = rs.getInt(1);
				warn.setId(wid);
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
		return wid;
	}

	@Override
	public List<WeiboWarn> queryWeiboWarnList(int customid) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<WeiboWarn> list = new ArrayList<WeiboWarn>();
		try {
			conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME)
					.getConnection();
			String sql = "select * from Office_Weibo_Warning where customid=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, customid);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				list.add(this.fill(rs));
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
		return list;
	}

	@Override
	public boolean update(WeiboWarn warn) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		boolean isUpdate = false;
		try {
			conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME)
					.getConnection();
			String sql = "update Office_Weibo_Warning set tid=?,platform=?,comment=?,repost=?,receiver=?,type=?,customid=?,created=?, isDelete=? , isrunning=?    ,  rHappen=?  , cHappen=?    where id=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, warn.getTid());
			pstmt.setInt(2, Utility.getPlatform(warn.getPlatform()));
			pstmt.setInt(3, warn.getComment());
			pstmt.setInt(4, warn.getRepost());
			pstmt.setString(5, warn.getReceiver());
			pstmt.setString(6, warn.getType());
			pstmt.setInt(7, warn.getCustomid());
			pstmt.setTimestamp(8, new Timestamp(new Date().getTime()));
			pstmt.setBoolean(9, warn.isDelete());
			pstmt.setBoolean(10, true);
			pstmt.setBoolean(11, false);
			pstmt.setBoolean(12, false);
			pstmt.setInt(13, warn.getId());
			int flag = pstmt.executeUpdate();
			if (flag > 0) {
				isUpdate = true;
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
		return isUpdate;
	}

	@Override
	public Map<String, String[]> checkWarn(int customId, List<String> tids) {
		String cmdText = "select id,tid,isDelete  from Office_Weibo_Warning where customid=?";

		StringBuilder sb = new StringBuilder();
		if (tids != null && tids.size() > 0) {
			sb.append(" and tid in ('" + tids.get(0));
			for (int i = 1; i < tids.size(); i++) {
				sb.append("','");
				sb.append(tids.get(i));
			}
			sb.append("')");
		}

		cmdText += sb.toString();

		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Map<String, String[]> result = new HashMap<String, String[]>();
		try {
			conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME)
					.getConnection();
			pstmt = conn.prepareStatement(cmdText);
			pstmt.setInt(1, customId);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				String[] strs = new String[2];
				strs[0] = rs.getInt("id") + "";
				if (!rs.getBoolean("isDelete")) {
					strs[1] = "0";
				} else {
					strs[1] = "1";
				}
				result.put(rs.getString("tid"), strs);
			}

			for (String tid : tids) {
				if (!result.containsKey(tid)) {
					// result.put(tid, 0);
					String[] strs = new String[2];
					strs[0] = "0";
					strs[1] = "0";
					result.put(tid, strs);
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			Utility.closeConnection(conn, pstmt, rs);
		}
		return result;

	}

	@Override
	public Pagefile<WeiboWarn> queryWeiboWarnList(int customid, Pager pager) {
		Query query = new Query();
		query.setTableName("Office_Weibo_Warning");
		query.setFields("*");
		query.setPageIndex(pager.getPageIndex());
		query.setPageSize(pager.getPageSize());
		query.setSortField("created");
		query.setWhere(" where customid = " + customid);
		return DbHelper.executePager(query, this);
	}

	@Override
	public WeiboWarn fill(ResultSet rs) throws SQLException {
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
		warn.setTime(rs.getTimestamp("endtime").getTime());
		warn.setRunning(rs.getBoolean("isrunning"));
		warn.setCommentHappen(rs.getBoolean("cHappen"));
		warn.setRepostHappen(rs.getBoolean("rHappen"));
		return warn;
	}

	@Override
	public void changeRunning(String tid, int customid, boolean running) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		try {
			conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME)
					.getConnection();
			String sql = "update Office_Weibo_Warning set isrunning=? where tid=? and customid=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setBoolean(1, running);
			pstmt.setString(2, tid);
			pstmt.setInt(3, customid);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			Utility.closeConnection(conn, pstmt, null);
		}
	}

	@Override
	public void setWeiboCommentFlag(int id, boolean flag) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		try {
			conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME)
					.getConnection();
			String sql = "update Office_Weibo_Warning set chappen=? where id=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setBoolean(1, flag);
			pstmt.setInt(2, id);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			Utility.closeConnection(conn, pstmt, null);
		}

	}

	@Override
	public void setWeiboRepostFlag(int id, boolean flag) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		try {
			conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME)
					.getConnection();
			String sql = "update Office_Weibo_Warning set rhappen=? where id=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setBoolean(1, flag);
			pstmt.setInt(2, id);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			Utility.closeConnection(conn, pstmt, null);
		}
	} 
	
		
	/**
	 * 预警服务--预警通知中的  删除某一条消息的方法
	 * @param   id   Office_WarnList表的id
	 */
	public   int   deleteWarnNofity(int id){
		 int  flag=0;
		 Connection conn = null;
		 PreparedStatement   pstmt=null;
		 String sql="delete  from    Office_WarnList   where  id=?";
		 try {
			conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME)
				.getConnection();
			pstmt=conn.prepareStatement(sql);
			pstmt.setInt(1, id);
			flag=pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			Utility.closeConnection(conn, pstmt, null);
		}
		 return  flag;
	} 
	
	
	
	/**********************以下是预警列表模块的*************************/
	
	
	public   Pagefile<WeiboWarn>  queryWarnWeiboShowList(int customid,Pager  pager){
		Connection conn = null;
		CallableStatement cstmt = null;
		Pagefile<WeiboWarn> pagefile=new Pagefile<WeiboWarn>();
		List<WeiboWarn>  list=new ArrayList<WeiboWarn>();
		ResultSet rs=null;
		ResultSet rs2=null;
		try {
			conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME)
					.getConnection();
			//String sql = "select  id ,    comment, repost,endtime, name ,text , currepost,  curcoment,isDelete   Office_Weibo_Warning  where  customid=?  ";
			//String  whereSql="select  id ,    comment, repost,endtime, name ,text , currepost,  curcoment,isDelete   Office_Weibo_Warning  where  customid="+customid;
			String  whereSql="   where  customid="+customid+"  and   isDelete=0" ;
			cstmt = conn.prepareCall("{call SupesoftPage(?,?,?,?,?,?,?)}");
			cstmt.setString(1, "Office_Weibo_Warning");
			cstmt.setString(2, "*");
			cstmt.setInt(3, pager.getPageSize());
			cstmt.setInt(4, pager.getPageIndex());
			cstmt.setString(5, whereSql);
			cstmt.setString(6, "id");
			cstmt.setInt(7, 1);
			cstmt.execute();
			rs=cstmt.getResultSet();
			while(rs.next()){
					  WeiboWarn  warn=new WeiboWarn();
					  warn.setId(rs.getInt("id"));
					  warn.setComment(rs.getInt("comment"));
					  warn.setRepost(rs.getInt("repost"));
					  warn.setEndTime(rs.getString("endtime").substring(0,   rs.getString("endtime").lastIndexOf(".")));
					  warn.setAuthor(rs.getString("name"));
					  warn.setText(rs.getString("text"));
					  warn.setCurrepost(rs.getInt("currepost"));
					  warn.setCurcomment(rs.getInt("curcoment"));
					  warn.setDelete(rs.getBoolean("isDelete"));
					  list.add(warn);
			}
			pagefile.setRows(list);
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
		return  pagefile ;
	}
	
	
	/**
	 * 预警服务--预警列表中的修改某一条预警设置得回来的数据的方法
	 *  @param   id  
	 */
	public  WeiboWarn  modifyWarningList(int  id){
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		WeiboWarn warn =new WeiboWarn();
		try {
			conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME)
					.getConnection();
			//String sql = "select * from Office_Weibo_Warning where customid=?";
			String sql = "select * from Office_Weibo_Warning where  id=?   and    isDelete=0";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, id);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				warn.setId(id);
				warn.setComment(rs.getInt("comment"));
				warn.setRepost(rs.getInt("repost"));
				warn.setEndTime(rs.getString("endtime").substring(0,rs.getString("endtime").lastIndexOf(" ")));
				warn.setReceiver(rs.getString("receiver"));
				warn.setType(rs.getString("type"));
				warn.setDelete(rs.getBoolean("isDelete"));
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
		return   warn;
	}
	
	
	/**
	 * 预警服务--预警列表中的修改某一条预警设置的方法
	 *  @param   WeiboWarn warn
	 *  @return    1 为修改成功 0 则为修改失败
	 */
    public    int  updateWarnList(WeiboWarn warn){
    	  int flag=0;
  		Connection conn = null;
		PreparedStatement pstmt = null;
		try {
			conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME)
					.getConnection();
			String sql = "update Office_Weibo_Warning  set  comment=?  ,repost=?, receiver=? ,type=?,  endtime=?   ,  rHappen=?  , cHappen=?    where  id=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, warn.getComment());
			pstmt.setInt(2, warn.getRepost());
			pstmt.setString(3, warn.getReceiver());
			pstmt.setString(4, warn.getType());
			pstmt.setString(5, warn.getEndTime());
			pstmt.setBoolean(6, false);
			pstmt.setBoolean(7, false);
			pstmt.setInt(8, warn.getId());
			flag=pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			Utility.closeConnection(conn, pstmt, null);
		}
    	  return    flag;
    }
	
    
	/**
	 *  预警服务--预警列表的删除事件
	 * */
     public  int  deleteWeiboWarnList(int id, int customid){
    	 int flag=0;
 		Connection conn = null;
 		PreparedStatement pstmt = null;
 		ResultSet rs = null;
 		List<Integer> list = new ArrayList<Integer>();
 		try {
 			conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME)
 					.getConnection();
 		 boolean  bool=	checkUnOrWarn(conn,id,customid);
 		 /**
 		  * bool  == true   已经发生的 可能全部都发生了 也有可能一部分发生 ，一部分为发生
 		  * bool== false 一定是全部未发生
 		 */
 		 //已经发生的 可能全部都发生了 也有可能一部分发生 ，一部分为发生
 		 if(bool){
 			  flag=deleteWarnAlready(conn,id,customid);
 		 }
 		 //一定是全部为发生的
 		 else {
 			 flag=deleteUNWarn(conn,id,customid);
 		  } // else 
 		} catch (SQLException e) {
 			e.printStackTrace();
 		} finally {
 			 try {
				if(conn!=null){
					 conn.close();
				 }
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
 		}
    	 return flag;
     }
	
     
     /**
      * 预警服务--预警列表的删除全部已经发生的消息以及微博信息 
      * 或者 一部分已经发生 一部分未发生 以及微博信息
      * isrunning=0 是表示关闭  isrunning=1 表示开启
      * @param  id  Office_Weibo_Warning 的id
      * */
     public  int  deleteWarnAlready(Connection conn ,int id,int customid){
    	 String isDeleteSQL = "update  Office_Weibo_Warning   set isDelete=1  ,  isrunning =0    where  id=?  ";  // and  customid=?
    	 String isDeleteUNWarnInfoSQL ="delete    from    Office_WarnList    where   warnid=?  and  status=0  and  customid=?";
    	 PreparedStatement pstmt2 = null;
    	 int flag=0;
    	 try {
			pstmt2=conn.prepareStatement(isDeleteSQL);
			pstmt2.setInt(1, id);
			int  flagOne= pstmt2.executeUpdate();
			pstmt2=conn.prepareStatement(isDeleteUNWarnInfoSQL);
			pstmt2.setInt(1, id);
			pstmt2.setInt(2, customid);
			int  flagTwo= pstmt2.executeUpdate();
			flag=flagOne+flagTwo;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			try {
				if(pstmt2!=null){
					pstmt2.close();
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
    	 return flag;
     }
     
     
     
     /**
      * 预警服务--预警列表的删除全部未发生的消息以及微博信息
      * @param  id  Office_Weibo_Warning 的id
      * */
     	public    int    deleteUNWarn(Connection conn ,int id,int customid){
     			int flag=0;
     			PreparedStatement pstmt2 = null;
     			String sql = "delete    from Office_Weibo_Warning  where   id=?  ";
     			String sql2 = "delete    from    Office_WarnList    where   warnid=?   and  customid=?";
     			try {
     				conn.setAutoCommit(false);
     				pstmt2=conn.prepareStatement(sql);
     				pstmt2.setInt(1, id);
     				//pstmt2.setInt(2, customid);
     				int  flagOne=pstmt2.executeUpdate();
     				pstmt2=conn.prepareStatement(sql2);
     				pstmt2.setInt(1, id);
     				pstmt2.setInt(2, customid);
     				int flagTwo=pstmt2.executeUpdate();
     				conn.commit();
     				flag=flagOne+flagTwo;
				} catch (SQLException e) {
					try {
						conn.rollback();
						conn.setAutoCommit(true);// 设置提交方式为默认方式
						flag = 0;
					} catch (SQLException e1) {
						e1.printStackTrace();
					}
				}finally {
					Utility.closeConnection(null, pstmt2, null);
				}
     			return flag;
     	}
     
     
	
     /**
      * 预警服务--预警列表的判断删除此微博是否已经发生预警消息了,
      * @return  false 未发生预警消息， true 已发生预警消息
      * */
     public     boolean      checkUnOrWarn(Connection conn, int id,int customid){
    	    boolean  bool=false;
    		PreparedStatement pstmt2 = null;
    		ResultSet rs = null;
    		String sql = "select    id   from  dbo.Office_WarnList   where warnid=?   and customid=?    and  warnType=3  and status=1 ";
    			try {
    				pstmt2 = conn.prepareStatement(sql);
    				pstmt2.setInt(1, id);
    				pstmt2.setInt(2, customid);
    				rs = pstmt2.executeQuery();
    				while (rs.next()) {
    					bool=true;
    					break;
    				}
    			} catch (SQLException e) {
    				// TODO Auto-generated catch block
    				e.printStackTrace();
    			}finally {
    	 			try {
    	 				if(rs!=null){
    	 					rs.close();
    	 				}
						if(pstmt2!=null){
							pstmt2.close();
						}
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
    	 		}
     	  return bool;
     }
     
     
	
	private void CloseStatus(ResultSet rs2, ResultSet rs,
			CallableStatement cstmt, Connection conn) {
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
	
}
