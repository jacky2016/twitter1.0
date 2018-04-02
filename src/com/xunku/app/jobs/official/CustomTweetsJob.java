package com.xunku.app.jobs.official;

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
import com.xunku.app.monitor.TweetOfficalManager;
import com.xunku.pojo.base.Custom;

/**
 * 暂时未使用
 * @author wujian
 * @created on Oct 10, 2014 5:51:30 PM
 */
public class CustomTweetsJob implements Job {

	private static Logger LOG = LoggerFactory.getLogger(CustomTweetsJob.class);

	private static final String JOB_NAME = "job.name.custom.tweets";
	private static final String JOB_GROUP = "job.group.custom.tweets";
	static final String JOB_TRIGGER = "job.trigger.custom.tweets";
	static final String JOB_DESC = "定时获取客户帐号的API任务";
	static final int REPEAT_SECONDS = 60 * 10;// 10分钟跑一次
	static boolean running = false;

	@Override
	public void execute(JobExecutionContext context)
			throws JobExecutionException {

		if (running) {
			LOG.info("上一轮[" + JOB_DESC + "]JOB还在运行，等待中...");
			return;
		}

		try {
			running = true;
			AppContext app = (AppContext) context.getJobDetail()
					.getJobDataMap().get("context");

			TweetOfficalManager manager = app.getMOfficialManager();
			List<Custom> customs = app.getCustomManager().getCustoms();
			LOG.info("本次需要同步" + customs.size() + "个客户的微博信息");
			for (Custom c : customs) {
				LOG.info("正在同步客户[" + c.getName() + "]的官微帐号信息...");
				manager.landing(c, false);
				Thread.sleep(1000 * 60);
			}

		} catch (Exception ex) {
			LOG.error("同步失败了... :-(", ex);
		}
		running = false;
	}

	public static Scheduler buildJob(AppContext context)
			throws SchedulerException {
		return JobFactory.buildJob(context, CustomTweetsJob.class, JOB_NAME,
				JOB_GROUP, JOB_TRIGGER, REPEAT_SECONDS);
	}

}
