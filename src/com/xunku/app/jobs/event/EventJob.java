package com.xunku.app.jobs.event;

import java.util.List;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xunku.app.AppContext;
import com.xunku.app.db.AccountDB;
import com.xunku.app.jobs.JobFactory;
import com.xunku.app.monitor.EventMonitor;
import com.xunku.app.stores.MEventStore;
import com.xunku.dao.event.EventDao;
import com.xunku.daoImpl.event.EventDaoImpl;

/**
 * 刷新事件数据的任务
 * 
 * @author wujian
 * @created on Jun 30, 2014 11:47:32 AM
 */
public class EventJob implements Job {

	/**
	 * 10分钟一次，这个应该是有个最小扫描时间，同时如果原来的任务没完成，则等待直到完成
	 */
	static int REPEAT_SECONDS = 60 * 10;
	static Logger LOG = LoggerFactory.getLogger(EventJob.class);
	static String JOB_NAME = "event data job";
	static String JOB_GROUP = "event_job_group";
	static String JOB_TRIGGER = "event_fetch_trigger";

	static boolean running = false;
	static int cnt = 1;

	public static void setRepeat(int repeat) {
		REPEAT_SECONDS = repeat * 60;
	}
	
	@Override
	public void execute(JobExecutionContext context)
			throws JobExecutionException {
		if (running) {
			LOG.info("[事件JOB]上一轮[事件数据落地]结束，等待结束...");
		}

		AppContext appContext = (AppContext) context.getJobDetail()
				.getJobDataMap().get("context");

		LOG.info("[事件JOB]事件数据同步任务开始执行第" + cnt + "次");
		EventDao _eventDAO = new EventDaoImpl();
		running = true;
		try {
			cnt++;
			
			// for test
			//EventMonitor m = _eventDAO.queryEventByEId(176);
			//LOG.info("正在处理[" + m.getName() + "]事件...");
			/*
			AccountDB db = appContext.getAccountManager().getDB(
					m.getPlatform());
			MEventStore store = (MEventStore) m.getStore(appContext);
			m.landing(db, store, null);
			*/
		
			List<EventMonitor> list = _eventDAO.queryAvailableEventList();
			for (EventMonitor event : list) {
				LOG.info("[事件JOB]正在处理[" + event.getName() + "]事件...");
				AccountDB db = appContext.getAccountManager().getDB(
						event.getPlatform());
				MEventStore store = (MEventStore) event.getStore(appContext);
				event.landing(db, store, null);
				LOG.info("[事件JOB]处理事件[" + event.getName() + "]完毕");
			}
			

		} catch (Exception ex) {
			LOG.error("[事件JOB]事件处理出错...", ex);
		}
		// LOG.info("[事件数据落地]结束");
		running = false;
	}

	/**
	 * 创建刷新事件数据的任务
	 * 
	 * @throws Exception
	 */
	public static Scheduler buildJob(AppContext context)
			throws SchedulerException {

		return JobFactory.buildJob(context, EventJob.class, JOB_NAME,
				JOB_GROUP, JOB_TRIGGER, REPEAT_SECONDS);
	}

}
