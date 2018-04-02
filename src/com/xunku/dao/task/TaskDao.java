package com.xunku.dao.task;

import java.util.List;

import com.xunku.dto.pubSentiment.TaskGroupsDTO;
import com.xunku.pojo.task.Group;
import com.xunku.pojo.task.Rubbish;
import com.xunku.pojo.task.Task;

public interface TaskDao {

	List<Task> queryTasksByCustomId(int customid);
	
	List<Rubbish> getRubbishList(int taskid);

	String getRubbishWords(int taskid);
	
	String queryTaskWord(int taskid);
	
	List<Group> queryTopGroup(int customid);

	/**
	 * 功能描述<任务管理：获取一级菜单数量>
	 * 
	 * @author wanghui
	 * @param void
	 * @return int
	 * @version twitter 1.0
	 * @date Aug 19, 20146:07:00 PM
	 */
	public int getTaskByParent(int customid);

	/**
	 * 功能描述<获取分组下的任务>
	 * 
	 * @author wanghui
	 * @param void
	 * @return List<TaskGroupsDTO>
	 * @version twitter 1.0
	 * @date Apr 17, 20144:43:34 PM
	 */
	public List<TaskGroupsDTO> queryByAll(int userId);

	/**
	 * 功能描述<添加一级任务组>
	 * 
	 * @author wanghui
	 * @param void
	 * @return void
	 * @version twitter 1.0
	 * @date Apr 23, 20149:23:46 AM
	 */
	public void insertParent(String groupName, int customId);

	/**
	 * 功能描述<添加二级任务组>
	 * 
	 * @author wanghui
	 * @param void
	 * @return void
	 * @version twitter 1.0
	 * @date Apr 23, 201410:10:16 AM
	 */
	public void insertChildren(Group group);

	/**
	 * 功能描述<在组下添加任务>
	 * 
	 * @author wanghui
	 * @param ids[用逗号分隔的垃圾词组id]
	 * @return void
	 * @version twitter 1.0
	 * @date Apr 23, 201410:12:12 AM
	 */
	public void insertTask(Task task, String ids);

	/**
	 * 功能描述<将类的排序向下移一位>
	 * 
	 * @author wanghui
	 * @param void
	 * @return void
	 * @version twitter 1.0
	 * @date May 4, 20149:42:05 AM
	 */
	public void parentDown(int gid);

	/**
	 * 功能描述<将类的排序向上移一位>
	 * 
	 * @author wanghui
	 * @param void
	 * @return void
	 * @version twitter 1.0
	 * @date May 4, 20149:42:57 AM
	 */
	public void parentUp(int gid);

	/**
	 * 功能描述<更新组名>
	 * 
	 * @author wanghui
	 * @param void
	 * @return void
	 * @version twitter 1.0
	 * @date May 4, 201410:58:21 AM
	 */
	public void updateParent(int id, String name);

	/**
	 * 功能描述<更新类名>
	 * 
	 * @author wanghui
	 * @param void
	 * @return void
	 * @version twitter 1.0
	 * @date May 4, 20141:54:45 PM
	 */
	public void updateChildren(int id, String name, int pid);

	/**
	 * 功能描述<更新任务名称>
	 * 
	 * @author wanghui
	 * @param void
	 * @return void
	 * @version twitter 1.0
	 * @date May 4, 20141:55:09 PM
	 */
	public void updateTask(int id, String name, int gid);

	/**
	 * 功能描述<删除任务组>
	 * 
	 * @author wanghui
	 * @param void
	 * @return void
	 * @version twitter 1.0
	 * @date May 4, 201411:38:08 AM
	 */
	public void deleteByParent(int pid, int customid);

	/**
	 * 功能描述<删除任务分类>
	 * 
	 * @author wanghui
	 * @param void
	 * @return void
	 * @version twitter 1.0
	 * @date May 4, 201411:40:09 AM
	 */
	public void deleteByChildren(int cid, int userId);

	/**
	 * 功能描述<删除任务>
	 * 
	 * @author wanghui
	 * @param void
	 * @return void
	 * @version twitter 1.0
	 * @date May 4, 201411:41:46 AM
	 */
	public void deleteByTask(int tid, int userId);

	/**
	 * 功能描述<获取一二级菜单>
	 * 
	 * @author wanghui
	 * @param void
	 * @return List<Group>
	 * @version twitter 1.0
	 * @date May 4, 20145:05:05 PM
	 */
	public List<Group> queryByParent(int pid, int customId);

	/**
	 * 功能描述<获取任务>
	 * 
	 * @author wanghui
	 * @param void
	 * @return List<Group>
	 * @version twitter 1.0
	 * @date May 4, 20145:05:05 PM
	 */
	public List<Task> queryTaskByChildren(int cid, int customId);

	/**
	 * 功能描述<删除任务【系统设置】>
	 * 
	 * @author wanghui
	 * @param void
	 * @return void
	 * @version twitter 1.0
	 * @date May 21, 20143:48:59 PM
	 */
	public void deleteByTask(int taskId);

	/**
	 * 功能描述<更新任务信息【系统设置】>
	 * 
	 * @author wanghui
	 * @param rubbishID【垃圾词组id】
	 * @return void
	 * @version twitter 1.0
	 * @date May 21, 20143:50:10 PM
	 */
	public void updateTask(Task task);

	/**
	 * 功能描述<获取一个分组>
	 * 
	 * @author wanghui
	 * @param void
	 * @return Group
	 * @version twitter 1.0
	 * @date Jul 17, 20143:24:58 PM
	 */
	public Group queryGroupById(int gid);

	/**
	 * 功能描述<判断一级菜单名称是否存在>
	 * 
	 * @author wanghui
	 * @param void
	 * @return boolean
	 * @version twitter 1.0
	 * @date Aug 6, 20143:07:47 PM
	 */
	public boolean pnameIsExsit(String name, int customId);

	/**
	 * 功能描述<判断二级菜单名称是否存在>
	 * 
	 * @author wanghui
	 * @param void
	 * @return boolean
	 * @version twitter 1.0
	 * @date Aug 6, 20143:08:36 PM
	 */
	public boolean cnameIsExsit(String name, int pid);

	/**
	 * 功能描述<判断任务是否已存在>
	 * 
	 * @author wanghui
	 * @param void
	 * @return boolean
	 * @version twitter 1.0
	 * @date Aug 6, 20144:35:21 PM
	 */
	public boolean tnameIsExsit(String name, int customid);

	/**
	 * 功能描述<通过taskid获取任务>
	 * 
	 * @author wanghui
	 * @param void
	 * @return Task
	 * @version twitter 1.0
	 * @date Aug 7, 20149:41:22 AM
	 */
	public Task queryTaskById(int taskid);

	/**
	 * 功能描述<当前客户下的任务数量>
	 * 
	 * @author wanghui
	 * @param void
	 * @return int
	 * @version twitter 1.0
	 * @date Aug 12, 201411:26:48 AM
	 */
	public int getTaskListCount(int customid); 
	
	
	/**
	 * 功能描述<当前客户下的任务数量>
	 * 
	 * @author shangwei
	 * @param  
	 * @return int
	 * @version twitter 1.0
	 * @date 2014年10月10日 20:19:20
	 */
	public   int   checkNameRepeat(String sql);
	
	
}
