package com.xunku.app.manager;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xunku.app.AppContext;
import com.xunku.app.Utility;
import com.xunku.app.db.AccountManager;
import com.xunku.app.enums.Platform;
import com.xunku.app.helpers.DateHelper;
import com.xunku.app.interfaces.ITweet;
import com.xunku.app.model.Pooling;
import com.xunku.app.model.tweets.TweetFactory;
import com.xunku.dao.base.AccountDao;
import com.xunku.dao.home.HomeFansDao;
import com.xunku.dao.home.HotDao;
import com.xunku.dao.task.TaskDao;
import com.xunku.daoImpl.base.AccountDaoImpl;
import com.xunku.daoImpl.home.HomeFansDaoImpl;
import com.xunku.daoImpl.home.HotDaoImpl;
import com.xunku.daoImpl.task.TaskDaoImpl;
import com.xunku.dto.task.TaskCntDTO;
import com.xunku.dto.task.TaskSearchDTO;
import com.xunku.dto.task.TaskTwitterVO;
import com.xunku.pojo.base.AccountInfo;
import com.xunku.pojo.base.Pager;
import com.xunku.pojo.home.FansTrend;
import com.xunku.pojo.home.Hot;
import com.xunku.pojo.task.Group;
import com.xunku.pojo.task.Task;
import com.xunku.pojo.task.TaskCount;
import com.xunku.service.TaskSearchService;
import com.xunku.utils.Pagefile;

/**
 * 舆情管理器
 * 
 * @author wujian
 * @created on Aug 5, 2014 2:55:06 PM
 */
public class TaskManager {
	static TaskManager _manager;
	private static final Logger LOG = LoggerFactory
			.getLogger(TaskManager.class);

	public synchronized static TaskManager getInstance() {
		if (_manager == null) {
			// 这里应该从配置文件里面读取策略和监听器
			_manager = new TaskManager();
		}
		return _manager;
	}

	/**
	 * 获得任务统计
	 * 
	 * @param customId
	 * @param start
	 * @param end
	 * @param pool
	 * @return
	 */
	public Map<Long, TaskCntDTO> viewTaskCount(int customId, Date start,
			Date end, Pooling pool) {

		HomeFansDao dao = new HomeFansDaoImpl();

		List<TaskCount> tcs = dao.queryCountsByCustomID(customId, start, end);

		if (tcs == null || tcs.size() == 0) {
			LOG.info("无统计数据稍后再试");
			return null;
		}
		Map<Long, TaskCntDTO> result = new TreeMap<Long, TaskCntDTO>();
		for (TaskCount tc : tcs) {
			TaskCntDTO dto = new TaskCntDTO();
			dto.renminCnt = tc.getRenmin();
			dto.sinaCnt = tc.getSina();
			dto.tencentCnt = tc.getTencent();
			result.put(tc.getCreated().getTime(), dto);
		}
		return result;
	}

	/**
	 * 获得当天的舆情类别分析
	 * 
	 * @param customId
	 * @param pool
	 * @return
	 */
	public Map<String, Integer> viewTaskGroupByPlatform(int customId,
			Pooling pool) {
		HomeFansDao dao = new HomeFansDaoImpl();

		Date now = new Date();

		Map<String, Integer> tcs = dao.queryCategoriesByCustomID(customId, now,
				now);
		return tcs;
	}

	/**
	 * 返回首页当前客户的粉丝趋势 Map<帐号名->对应的该帐号指定时间段的粉丝变化>
	 * 
	 * @param customId
	 * @param start
	 * @param end
	 */
	public Map<String, List<FansTrend>> viewHomeFansTrend(int customId,
			Date start, Date end) {
		HomeFansDao dao = new HomeFansDaoImpl();

		// 先找到这个客户下的所有帐号
		// CustomManager manager = CustomManager.getInstance();
		// Custom custom = manager.getCustom(customId);
		AccountDao adao = new AccountDaoImpl();
		List<AccountInfo> list = adao.queryByCustomId(customId);

		Map<String, List<FansTrend>> result = new HashMap<String, List<FansTrend>>();
		for (AccountInfo info : list) {
			List<FansTrend> lst = dao.queryFansTrends(info.getUcode(), info
					.getPlatform(), start, end);
			result.put(info.getName(), lst);
		}

		return result;
	}

	/**
	 * 返回首页的热词列表
	 * 
	 * @param platform
	 * @param date
	 * @return
	 */
	public List<Hot> viewHomeHots(Platform platform, Date date) {
		HotDao dao = new HotDaoImpl();

		List<Hot> hots = dao.queryByPlatform(Utility.getPlatform(platform),
				date);

		return hots;
	}

	/**
	 * 搜索任务
	 * 
	 * @param taskSearchDTO
	 */
	public Pagefile<ITweet> searchTask(TaskSearchDTO taskSearchDTO) {
		TaskSearchService taskSearchService = new TaskSearchService();

		Pagefile<TaskTwitterVO> lst = null;

		try {
			lst = taskSearchService.taskSearchService(taskSearchDTO);
		} catch (Exception ex) {
			return null;
		}
		if (lst != null && lst.getRows() != null) {
			Pagefile<ITweet> result = new Pagefile<ITweet>();
			AccountManager manager = AppContext.getInstance()
					.getAccountManager();
			// 要入库作者
			for (TaskTwitterVO vo : lst.getRows()) {
				ITweet post = TweetFactory.createTweet(vo);
				manager.updateTweetAccount(post);
				if (post != null)
					result.getRows().add(post);
			}
			result.setRealcount(lst.getRealcount());
			result.setErr(lst.getErr());
			return result;
		}

		return null;
	}

	/**
	 * 根据任务统计当天的舆情数量
	 * 
	 * @param task
	 * @return
	 */
	public int searchTask4Count(Task task, Platform platform) {

		TaskSearchService taskSearchService = new TaskSearchService();
		Pagefile<TaskTwitterVO> lst = new Pagefile<TaskTwitterVO>();

		try {
			// 这个只获得总数，所以pager(1,1)
			lst = taskSearchService.taskSearchService(task, platform, task
					.getKeywords()
					+ task.getRubbishString(), Pager.createPager(1, 1),
					new Date(DateHelper.formatTodayFirst()));

			if (lst == null)
				return 0;
			else
				return lst.getRealcount();
		} catch (Exception ex) {
			return 0;
		}
	}

	public List<ITweet> searchTaskTopN(Task task, Platform platform, int topN,
			Date lastCT) {
		TaskSearchService taskSearchService = new TaskSearchService();
		Pagefile<TaskTwitterVO> lst = new Pagefile<TaskTwitterVO>();
		Pagefile<ITweet> result = new Pagefile<ITweet>();

		try {
			String queryString = task.getKeywords() + task.getRubbishString();

			queryString += "";

			Pager pager = new Pager();
			pager.setPageIndex(1);
			pager.setPageSize(topN);
			lst = taskSearchService.taskSearchService(task, platform,
					queryString, pager, lastCT);

			if (lst != null && lst.getRows() != null
					&& lst.getRows().size() > 0) {

				for (TaskTwitterVO vo : lst.getRows()) {
					result.getRows().add(TweetFactory.createTweet(vo));
				}
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return result.getRows();
	}

	public List<Task> getTasksByCustomID(int customId) {
		TaskDao dao = new TaskDaoImpl();

		List<Task> tasks = dao.queryTasksByCustomId(customId);

		for (Task task : tasks) {
			task.setRubbishList(dao.getRubbishList(task.getId()));
		}

		return tasks;
	}

	/**
	 * 获得任务的顶层分组
	 * 
	 * @param customId
	 * @return
	 */
	public List<Group> getTopGroups(int customId) {
		TaskDao taskDAO = new TaskDaoImpl();

		List<Group> groups = taskDAO.queryTopGroup(customId);

		for (Group group : groups) {
			List<Group> subGroups = taskDAO.queryByParent(group.getId(),
					customId);
			for (Group g : subGroups) {
				group.getSubGroupIds().add(g.getId());
			}
		}
		return groups;
	}

	public Task getTaskById(int taskid) {
		TaskDao dao = new TaskDaoImpl();

		Task task = dao.queryTaskById(taskid);

		task.setRubbishList(dao.getRubbishList(task.getId()));

		return task;
	}

}
