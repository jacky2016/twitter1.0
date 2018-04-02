package com.xunku.daoImpl.task;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.xunku.app.AppContext;
import com.xunku.app.Utility;
import com.xunku.app.manager.TaskManager;
import com.xunku.constant.PortalCST;
import com.xunku.dao.task.TaskDao;
import com.xunku.dto.pubSentiment.TaskDTO;
import com.xunku.dto.pubSentiment.TaskGroupsDTO;
import com.xunku.pojo.base.Custom;
import com.xunku.pojo.task.Group;
import com.xunku.pojo.task.Rubbish;
import com.xunku.pojo.task.Task;
import com.xunku.utils.DatabaseUtils;

public class TaskDaoImpl implements TaskDao {
 
	@Override
	public int checkNameRepeat(String sql) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME)
					.getConnection();
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			int i=0;
			while (rs.next()) {
				i++;
			}
			if(i>0){
				return i;
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.CloseStatus(rs, pstmt, conn);
		}
		
		
		return 0;
	}

	@Override
	public List<TaskGroupsDTO> queryByAll(int userId) {
		Connection conn = null;
		CallableStatement cstmt = null;
		ResultSet rs = null;
		ResultSet rs2 = null;
		ResultSet rs3 = null;
		List<TaskGroupsDTO> list = null;
		try {
			conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME)
					.getConnection();
			cstmt = conn.prepareCall("{call sp_TaskGroup_queryByAll(?)}");
			cstmt.setInt(1, userId);
			cstmt.execute();
			rs = cstmt.getResultSet();
			list = new ArrayList<TaskGroupsDTO>();
			Map<String, TaskGroupsDTO> map = new HashMap<String, TaskGroupsDTO>();
			Map<String, TaskGroupsDTO> map2 = new HashMap<String, TaskGroupsDTO>();
			while (rs.next()) {
				TaskGroupsDTO dto = new TaskGroupsDTO();
				dto.setId(rs.getInt("ID"));
				dto.setGroupname(rs.getString("GroupName"));
				list.add(dto);
				map.put(String.valueOf(rs.getInt("ID")), dto);
			}
			if (cstmt.getMoreResults()) {
				rs2 = cstmt.getResultSet();
				while (rs2.next()) {
					TaskGroupsDTO dto = new TaskGroupsDTO();
					int id = rs2.getInt("ID");
					String groupName = rs2.getString("GroupName");
					int pid = rs2.getInt("ParentID");
					dto.setId(id);
					dto.setGroupname(groupName);
					map.get(String.valueOf(pid)).getGroups().add(dto);
					map2.put(String.valueOf(id), dto);
				}
			}
			if (cstmt.getMoreResults()) {
				rs3 = cstmt.getResultSet();
				while (rs3.next()) {
					TaskDTO task = new TaskDTO();
					task.setId(rs3.getInt("ID"));
					int gid = rs3.getInt("GroupID");
					task.setGroupid(gid);
					task.setName(rs3.getString("Name"));
					task.setKeywords(rs3.getString("Keywords"));
					task.setSearchTime(rs3.getInt("SearchTime"));
					map2.get(String.valueOf(gid)).getTasks().add(task);
				}
			}
			map = null;
			map2 = null;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.CloseStatus(rs3, rs2, rs, cstmt, conn);
		}
		return list;
	}

	@Override
	public void insertChildren(Group group) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		try {
			conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME)
					.getConnection();
			String sql = "INSERT INTO Task_Groups (GroupName,ParentID,CustomID,Sort) select ?,?,?,case when max(Sort) is null then 1 else max(Sort)+1 end from Task_Groups where CustomID=? and ParentID=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, group.getGroupName());
			pstmt.setInt(2, group.getParentID());
			pstmt.setInt(3, group.getCustomID());
			pstmt.setInt(4, group.getCustomID());
			pstmt.setInt(5, group.getParentID());
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.CloseStatus(pstmt, conn);
		}
	}

	@Override
	public void insertParent(String groupName, int customId) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		try {
			conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME)
					.getConnection();
			String sql = "INSERT INTO Task_Groups (GroupName,ParentID,CustomID,Sort) select ?,-1,?,case when max(Sort) is null then 1 else max(Sort)+1 end from Task_Groups where CustomID=? and ParentID=-1";
			if (customId != 0) {
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, groupName);
				pstmt.setInt(2, customId);
				pstmt.setInt(3, customId);
				pstmt.execute();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.CloseStatus(pstmt, conn);
		}
	}

	@Override
	public void insertTask(Task task, String ids) {
		Connection conn = null;
		CallableStatement cstmt = null;
		PreparedStatement pstmt = null;
		try {
			conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME)
					.getConnection();
			conn.setAutoCommit(false);
			cstmt = conn
					.prepareCall("{call sp_Task_insertTask(?,?,?,?,?,?,?)}");
			cstmt.setInt(1, task.getGroupID());
			cstmt.setString(2, task.getName());
			cstmt.setString(3, task.getKeywords());
			cstmt.setInt(4, task.getCreator());
			cstmt.setInt(5, task.getCustomID());
			cstmt.setInt(6, task.getSearchTime());
			cstmt.registerOutParameter(7, java.sql.Types.INTEGER);
			cstmt.execute();
			int taskid = cstmt.getInt(7);
			if (taskid > 0) {
				if (ids != "" || ids.length() != 0) {
					String[] rids = ids.split(",");
					for (int i = 0; i < rids.length; i++) {
						String sql = "INSERT INTO Task_TaskRubbish (TaskID,RubbishID) VALUES (?,?)";
						pstmt = conn.prepareStatement(sql);
						pstmt.setInt(1, taskid);
						pstmt.setInt(2, Integer.valueOf(rids[i]));
						pstmt.execute();
					}
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
			this.CloseStatus(null, null, null, cstmt, pstmt, conn);
		}
	}

	@Override
	public void parentDown(int gid) {
		Connection conn = null;
		CallableStatement cstmt = null;
		try {
			conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME)
					.getConnection();
			cstmt = conn.prepareCall("{call Task_Catlogs_ParntDown(?)}");
			cstmt.setInt(1, gid);
			cstmt.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.CloseStatus(cstmt, conn);
		}
	}

	@Override
	public void parentUp(int gid) {
		Connection conn = null;
		CallableStatement cstmt = null;
		try {
			conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME)
					.getConnection();
			cstmt = conn.prepareCall("{call Task_Catlogs_ParntUp(?)}");
			cstmt.setInt(1, gid);
			cstmt.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.CloseStatus(cstmt, conn);
		}
	}

	@Override
	public void updateParent(int id, String name) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		try {
			conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME)
					.getConnection();
			String sql = "UPDATE Task_Groups SET GroupName=? WHERE ID=? and ParentID=-1";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, name);
			pstmt.setInt(2, id);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.CloseStatus(pstmt, conn);
		}
	}

	@Override
	public void deleteByChildren(int cid, int userId) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		try {
			conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME)
					.getConnection();
			String sql = "delete from Task_Tasks where GroupID=? and CustomID = (select CustomID from Base_Users where ID=?) "
					+ "delete from Task_Groups where ID=? and CustomID = (select CustomID from Base_Users where ID=?) "
					+ "delete from Task_SubscriberTasks where TaskID = ? ";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, cid);
			pstmt.setInt(2, userId);
			pstmt.setInt(3, cid);
			pstmt.setInt(4, userId);
			pstmt.setInt(5, cid);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.CloseStatus(pstmt, conn);
		}
	}

	@Override
	public void deleteByParent(int pid, int customid) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		try {
			conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME)
					.getConnection();
			String sql = "delete from Task_Tasks where GroupID in (select ID from Task_Groups where ParentID=?) and CustomID=? "
					+ "delete from Task_Groups where ParentID=? and CustomID = ? "
					+ "delete from Task_Groups where ID=? and CustomID = ? "
					+ "delete from Task_SubscriberTasks where TaskID = ? ";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, pid);
			pstmt.setInt(2, customid);
			pstmt.setInt(3, pid);
			pstmt.setInt(4, customid);
			pstmt.setInt(5, pid);
			pstmt.setInt(6, customid);
			pstmt.setInt(7, pid);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.CloseStatus(pstmt, conn);
		}

	}

	@Override
	public void deleteByTask(int tid, int userId) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		try {
			conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME)
					.getConnection();
			String sql = "delete from Task_Tasks where GroupID=? and CustomID = (select CustomID from Base_Users where ID=?)";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, tid);
			pstmt.setInt(2, userId);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.CloseStatus(pstmt, conn);
		}
	}

	@Override
	public void updateChildren(int id, String name, int pid) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		try {
			conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME)
					.getConnection();
			String sql = "UPDATE Task_Groups SET GroupName=?,ParentID=? WHERE ID=? and ParentID!=-1";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, name);
			pstmt.setInt(2, pid);
			pstmt.setInt(3, id);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.CloseStatus(pstmt, conn);
		}
	}

	@Override
	public void updateTask(int id, String name, int gid) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		try {
			conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME)
					.getConnection();
			String sql = "UPDATE Task_Tasks SET Name=?,GroupID=? WHERE ID=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, name);
			pstmt.setInt(2, gid);
			pstmt.setInt(3, id);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.CloseStatus(pstmt, conn);
		}
	}

	@Override
	public List<Group> queryByParent(int pid, int customId) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<Group> list = new ArrayList<Group>();
		try {
			conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME)
					.getConnection();
			String sql = "SELECT ID,GroupName,ParentID,CustomID FROM Task_Groups WHERE ParentID=? AND CustomID=? ORDER BY Sort";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, pid);
			pstmt.setInt(2, customId);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				list.add(this.getGroup(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.CloseStatus(rs, pstmt, conn);
		}
		return list;
	}

	@Override
	public List<Task> queryTaskByChildren(int pid, int customId) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<Task> list = new ArrayList<Task>();
		try {
			conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME)
					.getConnection();
			String sql = "SELECT * FROM Task_Tasks WHERE GroupID=? AND CustomID=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, pid);
			pstmt.setInt(2, customId);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				list.add(this.getTask(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.CloseStatus(rs, pstmt, conn);
		}
		return list;
	}

	private Task getTask(ResultSet rs) throws SQLException {
		Task task = new Task();
		task.setId(rs.getInt("ID"));
		task.setGroupID(rs.getInt("GroupID"));
		task.setKeywords(rs.getString("Keywords"));
		task.setSearchTime(rs.getInt("SearchTime"));
		task.setName(rs.getString("Name"));
		task.setCreator(rs.getInt("Creator"));
		task.setCustomID(rs.getInt("CustomID"));
		task.setSearchTime(rs.getInt("SearchTime"));
		task.setCreateTime(rs.getString("CreateTime"));
		return task;
	}

	@Override
	public void deleteByTask(int taskId) {
		Connection conn = null;
		CallableStatement cstmt = null;
		try {
			conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME)
					.getConnection();
			cstmt = conn.prepareCall("{call sp_Task_deleteByTask(?)}");
			cstmt.setInt(1, taskId);
			cstmt.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.CloseStatus(cstmt, conn);
		}
	}

	@Override
	public void updateTask(Task task) {
		Connection conn = null;
		CallableStatement cstmt = null;
		PreparedStatement pstmt = null;
		try {
			conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME)
					.getConnection();
			conn.setAutoCommit(false);
			cstmt = conn.prepareCall("{call sp_Task_updateTask(?,?,?,?,?)}");
			cstmt.setInt(1, task.getGroupID());
			cstmt.setString(2, task.getName());
			cstmt.setString(3, task.getKeywords());
			cstmt.setInt(4, task.getSearchTime());
			cstmt.setInt(5, task.getId());
			cstmt.executeUpdate();
			List<Rubbish> list = task.getRubbishList();
			for (Rubbish rub : list) {
				String sql = "INSERT INTO Task_TaskRubbish (TaskID,RubbishID) VALUES (?,?)";
				pstmt = conn.prepareStatement(sql);
				pstmt.setInt(1, task.getId());
				pstmt.setInt(2, rub.getId());
				pstmt.execute();
			}
			conn.commit();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.CloseStatus(null, null, null, cstmt, pstmt, conn);
		}
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
			CallableStatement cstmt, Connection conn) {
		this.CloseStatus(rs3, rs2, rs, cstmt, null, conn);
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

	private Group getGroup(ResultSet rs) throws SQLException {
		Group group = new Group();
		group.setId(rs.getInt("ID"));
		group.setGroupName(rs.getString("GroupName"));
		group.setParentID(rs.getInt("ParentID"));
		group.setCustomID(rs.getInt("CustomID"));
		return group;
	}

	@Override
	public Group queryGroupById(int gid) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Group group = null;
		try {
			conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME)
					.getConnection();
			String sql = "SELECT ID,GroupName,ParentID,CustomID FROM Task_Groups WHERE ID=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, gid);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				group = this.getGroup(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.CloseStatus(rs, pstmt, conn);
		}
		return group;
	}

	@Override
	public boolean pnameIsExsit(String name, int customid) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		boolean isExsit = false;
		try {
			conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME)
					.getConnection();
			String sql = "select ID from Task_Groups where ParentID=-1 and CustomID=? and GroupName=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, customid);
			pstmt.setString(2, name);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				isExsit = true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.CloseStatus(rs, pstmt, conn);
		}
		return isExsit;
	}

	@Override
	public boolean cnameIsExsit(String name, int pid) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		boolean isExsit = false;
		try {
			conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME)
					.getConnection();
			String sql = "select ID from Task_Groups where ParentID=? and GroupName=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, pid);
			pstmt.setString(2, name);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				isExsit = true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.CloseStatus(rs, pstmt, conn);
		}
		return isExsit;
	}

	@Override
	public boolean tnameIsExsit(String name, int customid) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		boolean isExsit = false;
		try {
			conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME)
					.getConnection();
			String sql = "select ID from Task_Tasks where CustomID=? and Name=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, customid);
			pstmt.setString(2, name);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				isExsit = true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.CloseStatus(rs, pstmt, conn);
		}
		return isExsit;
	}

	@Override
	public Task queryTaskById(int taskid) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Task task = null;
		try {
			conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME)
					.getConnection();
			String sql = "select * from Task_Tasks where ID=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, taskid);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				task = this.getTask(rs);

			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.CloseStatus(rs, pstmt, conn);
		}

		if (task != null) {
			task.setRubbishList(this.getRubbishList(taskid));
		}
		return task;
	}

	@Override
	public List<Rubbish> getRubbishList(int taskid) {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Connection conn = null;
		List<Rubbish> list = new ArrayList<Rubbish>();
		try {
			conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME)
					.getConnection();
			String sql = "select * from Task_Rubbish a join Task_TaskRubbish b on a.ID=b.RubbishID where b.TaskID=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, taskid);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				list.add(this.getRubbish(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			Utility.closeConnection(conn, pstmt, rs);
		}
		return list;
	}

	private Rubbish getRubbish(ResultSet rs) throws SQLException {
		Rubbish rub = new Rubbish();
		rub.setId(rs.getInt("ID"));
		rub.setCustomID(rs.getInt("CustomID"));
		rub.setGroupName(rs.getString("GroupName"));
		rub.setRubbishWords(rs.getString("RubbishWords"));
		return rub;
	}

	@Override
	public int getTaskListCount(int customid) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int count = 0;
		try {
			conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME)
					.getConnection();
			String sql = "select COUNT(ID) as count from Task_Tasks where CustomID=?";
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
	public int getTaskByParent(int customid) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int count = 0;
		try {
			conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME)
					.getConnection();
			String sql = "select COUNT(ID) as count from Task_Groups where CustomID=? and ParentID=-1";
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
	public List<Task> queryTasksByCustomId(int customid) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<Task> tasks = new ArrayList<Task>();
		try {
			conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME)
					.getConnection();
			String sql = "select a.*,b.GroupName from Task_Tasks a inner join Task_Groups b on a.GroupID=b.ID where b.CustomID = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, customid);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				Task task = this.getTask(rs);
				tasks.add(task);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.CloseStatus(rs, pstmt, conn);
		}
		return tasks;
	}

	public static void main(String[] args) {
		// 测试死锁

		AppContext context = AppContext.getInstance();
		context.init();
		TaskDao taskDAO = new TaskDaoImpl();
		List<Custom> customs = context.getCustomManager().getCustoms();
		TaskManager manager = TaskManager.getInstance();
		int i = 0;
		for (Custom custom : customs) {
			List<Task> tasks = manager.getTasksByCustomID(custom.getId());
			System.out.print("1");
			taskDAO.queryTasksByCustomId(custom.getId());
			System.out.print("2");
			taskDAO.queryTasksByCustomId(custom.getId());
			System.out.print("3");
			taskDAO.queryTasksByCustomId(custom.getId());
			System.out.print("4");
			for (Task task : tasks) {
				i++;
				System.out.println(i + "->" + task.getName() + ":"
						+ taskDAO.queryTaskWord(task.getId()));
			}
		}
	}

	@Override
	public String getRubbishWords(int taskid) {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuffer buf = null;
		String result = null;
		Connection conn = null;
		String sql = "select b.RubbishWords from Task_TaskRubbish a join Task_Rubbish b on a.RubbishID=b.ID where a.TaskID=?";
		try {
			conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME)
					.getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, taskid);
			rs = pstmt.executeQuery();
			buf = new StringBuffer();
			buf.append(" AND -(");
			while (rs.next()) {
				String rword = rs.getString("RubbishWords");
				String[] words = rword.split(" ");
				for (String word : words) {
					buf.append("content:\"" + word + "\" OR content1:\"" + word
							+ "\") AND -(");
				}
			}
			buf.append(")");
			if (buf.toString().equals(" AND -()")) {
				result = null;
			} else {
				result = buf.toString();
				result = result.substring(0, result.lastIndexOf("AND"));
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
	public String queryTaskWord(int taskId) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String result = "";
		try {
			conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME)
					.getConnection();
			String sql = "SELECT Keywords FROM Task_Tasks WHERE ID=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, taskId);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				result = rs.getString("Keywords");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			Utility.closeConnection(conn, pstmt, rs);
		}
		return result;
	}

	@Override
	public List<Group> queryTopGroup(int customId) {
		return this.queryByParent(-1, customId);
	}
}
