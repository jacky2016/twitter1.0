package com.xunku.app.jobs;

import java.io.IOException;
import java.util.List;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xunku.app.AppContext;
import com.xunku.app.controller.BusinessController;
import com.xunku.app.controller.WarningController;
import com.xunku.app.monitor.EventMonitor;
import com.xunku.dao.event.EventDao;
import com.xunku.daoImpl.event.EventDaoImpl;
import com.xunku.pojo.base.Custom;
import com.xunku.utils.PropertiesUtils;

public class BusinessJob implements Job {

	static final String JOB_NAME = "job.name.business";
	static final String JOB_GROUP = "job.group.business";
	static final String JOB_TRIGGER = "job.trigger.business";
	static final String JOB_DESC = "商业API任务";
	static int REPEAT_SECONDS = 60 * 5;// 1分钟更新一次
	static boolean running = false;

	static Logger LOG = LoggerFactory.getLogger(BusinessJob.class);

	@Override
	public void execute(JobExecutionContext context)
			throws JobExecutionException {
		if (running) {
			return;
		}

		running = true;

		try {

			AppContext app = (AppContext) context.getJobDetail()
					.getJobDataMap().get("context");
			List<Custom> customs = app.getCustomManager().getCustoms();
			BusinessController controller = new BusinessController();

			EventDao _eventDAO = new EventDaoImpl();
			for (Custom custom : customs) {

				// 获取客户的商业API的使用策略
				EventMonitor event = null;
				controller.search(custom, event);
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// 这里需要读取每个客户的事件信息，然后刷新每个客户的微博信息
	}

	public static Scheduler buildJob(AppContext context)
			throws SchedulerException {
		return JobFactory.buildJob(context, BusinessJob.class, JOB_NAME,
				JOB_GROUP, JOB_TRIGGER, REPEAT_SECONDS);
	}

}
