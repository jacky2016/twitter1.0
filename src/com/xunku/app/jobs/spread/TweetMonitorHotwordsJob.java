package com.xunku.app.jobs.spread;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xunku.app.AppContext;
import com.xunku.app.jobs.JobFactory;

/**
 * 传播分析-计算热门关键词的任务
 * 
 * @author wujian
 * @created on Jul 1, 2014 11:18:04 AM
 */
public class TweetMonitorHotwordsJob implements Job {

	private static final String JOB_NAME = "job.name.spread.hotwords";
	private static final String JOB_GROUP = "job.group.spread.hotwords";
	static final String JOB_TRIGGER = "job.trigger.spread.hotwords";
	static final String JOB_DESC = "传播分析";
	static final int REPEAT_SECONDS = 60 * 1;// 1分钟跑一次
	private static Logger LOG = LoggerFactory
			.getLogger(TweetMonitorHotwordsJob.class);

	@Override
	public void execute(JobExecutionContext context)
			throws JobExecutionException {
		// TODO Auto-generated method stub

		LOG.info("...");
	}

	public static Scheduler buildJob(AppContext context)
			throws SchedulerException {
		return JobFactory.buildJob(context, TweetMonitorAnalysisJob.class,
				JOB_NAME, JOB_GROUP, JOB_TRIGGER, REPEAT_SECONDS);
	}
}
