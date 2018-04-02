package com.xunku.app.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xunku.app.AppContext;
import com.xunku.app.enums.Platform;
import com.xunku.app.interfaces.ITweet;
import com.xunku.app.manager.TaskManager;
import com.xunku.app.model.SubscriberEvent;
import com.xunku.app.model.SubscriberTask;
import com.xunku.app.monitor.EventMonitor;
import com.xunku.app.stores.MEventStore;
import com.xunku.dao.base.CustomDao;
import com.xunku.dao.event.EventDao;
import com.xunku.dao.push.PushDao;
import com.xunku.dao.task.TaskDao;
import com.xunku.daoImpl.base.CustomDaoImpl;
import com.xunku.daoImpl.event.EventDaoImpl;
import com.xunku.daoImpl.push.PushDaoImpl;
import com.xunku.daoImpl.task.TaskDaoImpl;
import com.xunku.pojo.push.Subscriber;
import com.xunku.pojo.task.Task;
import com.xunku.utils.URLUtils;

/**
 * 订阅推送控制器
 * 
 * @author wujian
 * @created on Sep 16, 2014 8:15:18 PM
 */
public class SubscriberController {

	static Logger LOG = LoggerFactory.getLogger(SubscriberController.class);

	private void addPusher(Subscriber subscriber, List<ITweet> tweets) {
		PushDao pushDAO = new PushDaoImpl();
		CustomDao customDAO = new CustomDaoImpl();
		StringBuilder emails = new StringBuilder();

		String customName = customDAO.queryById(subscriber.getCustomid())
				.getName();

		List<String> emailList = pushDAO.queryEmails(subscriber.getId());

		if (emailList.size() > 0) {
			emails.append(emailList.get(0));
		}
		for (int i = 1; i < emailList.size(); i++) {
			emails.append(";");
			emails.append(emailList.get(i));
		}

		StringBuilder sb = new StringBuilder();
		sb.append("[");

		if (tweets.size() > 0) {
			sb.append(tweets.get(0).toJSON());
		}
		for (int i = 1; i < tweets.size(); i++) {
			ITweet tweet = tweets.get(i);
			sb.append(",");
			sb.append(tweet.toJSON());
		}
		sb.append("]");

		if (sb.length() <= 0) {
			LOG.info("该订阅任务未设置接收人，无法发送邮件。");
			return;
		}

		if (tweets.size() <= 0) {
			LOG.info("该订阅没有产生内容，不发送。");
			return;
		}

		int id = pushDAO.addPusher(customName, subscriber.getId(), sb
				.toString(), emails.toString());

		if (id == 0) {
			LOG.info("该订阅未生成发送项。");
			return;
		}
		try {
			LOG.info("发送订阅[" + id + "]内容!");
			URLUtils.sendNet4Push(subscriber.getCustomid(), subscriber
					.getCreator(), id);
		} catch (IOException e) {
			LOG.error("该订阅未发送成功！");
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 产生订阅数据
	 * 
	 * @param subscriber
	 */
	public void generateData(Subscriber subscriber, AppContext context) {

		TaskDao taskDAO = new TaskDaoImpl();
		EventDao eventDAO = new EventDaoImpl();
		PushDao pushDAO = new PushDaoImpl();
		TaskManager manager = TaskManager.getInstance();
		// 获得订阅的任务
		if (subscriber.getType() == 0) {
			// 日常推送
			// 获得日常任务
			List<SubscriberTask> taskIds = pushDAO.queryTasks(subscriber
					.getId());
			for (SubscriberTask subTask : taskIds) {
				Task task = taskDAO.queryTaskById(subTask.getTaskid());
				if (task != null) {
					// 这里只取得了10条
					List<ITweet> tweets = filterTweets(subscriber, pushDAO,
							manager, subTask, task);

					if (tweets != null && tweets.size() > 0) {
						// 入库待发送
						this.addPusher(subscriber, tweets);
						// 这里异步的所以需要保存最新的CT
						pushDAO.updateSubTaskLastCT(subTask.getSubid(), subTask
								.getTaskid(), new Date());
					}
				}
			}

		}

		if (subscriber.getType() == 1) {
			// 事件推送
			List<SubscriberEvent> eventIds = pushDAO.queryEvents(subscriber
					.getId());
			for (SubscriberEvent subEvent : eventIds) {
				EventMonitor em = eventDAO.queryEventByEId(subEvent
						.getEventid());
				if (em != null) {
					MEventStore store = ((MEventStore) em.getStore(context));
					// 只取前10条
					List<ITweet> tweets = store.queryNewestTopNTweets(
							subscriber.getTopN(), subEvent.getLast());

					if (tweets != null && tweets.size() > 0) {
						// 入库待发送
						this.addPusher(subscriber, tweets);

						// 这里异步的所以需要保存最新的CT
						Date lastCT = store.executeLastCT();
						if (lastCT != null) {
							pushDAO.updateSubEventLastCT(subEvent.getSubid(),
									subEvent.getEventid(), lastCT);
						}
					}
				}
			}
		}

	}

	/**
	 * 搜索任务并且过滤旧数据
	 * 
	 * @param subscriber
	 * @param pushDAO
	 * @param manager
	 * @param subTask
	 * @param task
	 * @return
	 */
	private List<ITweet> filterTweets(Subscriber subscriber, PushDao pushDAO,
			TaskManager manager, SubscriberTask subTask, Task task) {

		List<ITweet> tweets = manager.searchTaskTopN(task, Platform.UnKnow,
				subscriber.getTopN(), subTask.getLast());

		List<String> tids = pushDAO.queryPushTaskHis(subTask.getSubid(),
				subTask.getTaskid());

		List<ITweet> result = new ArrayList<ITweet>();
		for (ITweet t : tweets) {
			if (!this.isOldTid(t.getTid(), tids)) {
				result.add(t);
			}
		}

		return result;
	}

	private boolean isOldTid(String tid, List<String> oldTids) {
		for (String t : oldTids) {
			if (tid.equals(t)) {
				return true;
			}
		}
		return false;
	}

	// private Date getLastCT(List<ITweet> tweets){
	//		
	// Date result = null;
	//		
	// if(tweets!=null && tweets.size()>0){
	// result = tweets.get(0);
	// for(ITweet t:tweets){
	// result
	// }
	// }
	//		
	// return result;
	//		
	// }
}
