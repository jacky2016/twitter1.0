package com.xunku.app.jobs.spread;

import java.util.List;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xunku.app.AppContext;
import com.xunku.app.jobs.JobFactory;
import com.xunku.app.monitor.WeiboMonitor;
import com.xunku.dao.base.UserDao;
import com.xunku.dao.monitor.MWeiBoDao;
import com.xunku.daoImpl.base.UserDaoImpl;
import com.xunku.daoImpl.monitor.MWeiBoDaoImpl;
import com.xunku.pojo.base.User;

/**
 * 传播分析JOB
 * 
 * @author wujian
 * @created on Aug 27, 2014 8:55:45 PM
 */
public class TweetMonitorAnalysisJob implements Job {

	private static Logger LOG = LoggerFactory
			.getLogger(TweetMonitorAnalysisJob.class);

	private static final String JOB_NAME = "job.name.spread.analysis";
	private static final String JOB_GROUP = "job.group.spread.analysis";
	static final String JOB_TRIGGER = "job.trigger.spread.analysis";
	static final String JOB_DESC = "传播分析";
	static int REPEAT_SECONDS = 60 * 10;// 1分钟跑一次
	static boolean running = false;
	static String current_spread = "传播分析";

	public static void setRepeat(int repeat) {
		REPEAT_SECONDS = repeat * 60;
	}

	@Override
	public void execute(JobExecutionContext context)
			throws JobExecutionException {

		if (running) {
			LOG.info("上一轮[" + current_spread + "]JOB还在运行，等待中...");
			return;
		}

		try {
			running = true;
			AppContext aContext = (AppContext) context.getJobDetail()
					.getJobDataMap().get("context");

			MWeiBoDao weiboDAO = new MWeiBoDaoImpl();
			UserDao userDAO = new UserDaoImpl();
			List<WeiboMonitor> monitors = weiboDAO.queryWaitAnalysis();
			LOG.info("[传播分析JOB]本次需要分析" + monitors.size() + "个传播分析列表");

			// 系统里面应该建立一个内置客户，这个客户提供公共微博的爬取
			// 现在暂时使用10这个客户
			User user = userDAO.queryAdmin(10);
			for (WeiboMonitor c : monitors) {
				
				LOG.info("\t使用客户管理员[" + user.getUserName() + "]传播分析:["
						+ c.getTid() + "]");
				current_spread = "传播分析:" + c.getTid();
				aContext.getMWeiboManager().landing(c, user);
			}
			
			LOG.info("[传播分析JOB]分析结束.");

		} catch (Exception ex) {
			LOG.error("[传播分析JOB]分析传播分析失败");
		}
		running = false;
	}

	public static Scheduler buildJob(AppContext context)
			throws SchedulerException {
		return JobFactory.buildJob(context, TweetMonitorAnalysisJob.class,
				JOB_NAME, JOB_GROUP, JOB_TRIGGER, REPEAT_SECONDS);
	}

}
