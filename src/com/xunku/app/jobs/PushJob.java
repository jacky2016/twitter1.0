package com.xunku.app.jobs;

import java.util.List;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xunku.app.AppContext;
import com.xunku.app.controller.MessageController;
import com.xunku.app.controller.SubscriberController;
import com.xunku.app.manager.TaskManager;
import com.xunku.dao.base.AccountDao;
import com.xunku.dao.home.HomeFansDao;
import com.xunku.dao.push.PushDao;
import com.xunku.daoImpl.base.AccountDaoImpl;
import com.xunku.daoImpl.home.HomeFansDaoImpl;
import com.xunku.daoImpl.push.PushDaoImpl;
import com.xunku.pojo.base.Custom;
import com.xunku.pojo.push.Subscriber;

public class PushJob implements Job {

	static final String JOB_NAME = "job.name.push";
	static final String JOB_GROUP = "job.group.push";
	static final String JOB_TRIGGER = "job.trigger.push";
	static final String JOB_DESC = "推送任务";
	static int REPEAT_SECONDS = 60 * 1;// 1分钟更新一次
	static boolean running = false;

	static Logger LOG = LoggerFactory.getLogger(PushJob.class);

	@Override
	public void execute(JobExecutionContext context)
			throws JobExecutionException {

		if (running) {
			return;
		}

		running = true;
		// 一周舆情统计
		// 1、获得所有的任务
		// 2、查询每个任务的平台数据
		// 3、记录
		HomeFansDao homeDAO = new HomeFansDaoImpl();
		PushDao pushDAO = new PushDaoImpl();
		AccountDao accountDAO = new AccountDaoImpl();
		SubscriberController subController = new SubscriberController();
		MessageController messageController = new MessageController();
		AppContext app = (AppContext) context.getJobDetail().getJobDataMap()
				.get("context");
		List<Custom> customs = app.getCustomManager().getCustoms();
		TaskManager manager = TaskManager.getInstance();
		for (Custom custom : customs) {
			customPusher(homeDAO, pushDAO, accountDAO, subController,
					messageController, app, manager, custom, false);
		}

		running = false;

	}

	private void customPusher(HomeFansDao homeDAO, PushDao pushDAO,
			AccountDao accountDAO, SubscriberController subController,
			MessageController messageController, AppContext app,
			TaskManager manager, Custom custom, boolean test) {

		// 推送最小单位是一天
		LOG.info("[推送JOB]开始同步订阅信息...");
		List<Subscriber> list = pushDAO.querySubscriberByCustomId(custom
				.getId());

		LOG.info("[推送JOB]该客户有[" + list.size() + "]个订阅");
		for (Subscriber s : list) {
			if (test) {
				LOG.info("\t为订阅[" + s.getName() + "]生成数据,请稍后...");
				subController.generateData(s, app);
				LOG.info("\t为订阅[" + s.getName() + "]生成数据 --ok ");
			} else {
				// 先更新推送任务状态
				LOG.info("\t检查该订阅[" + s.getName() + "]的状态...");
				if (!s.isStop()) {
					// 第一次执行的时间到了则生成数据
					if (s.getFirstRunTime().getTime() <= System
							.currentTimeMillis()) {
						// 检查今天是否已经有推送了如果有了就不在产生数据了
						if (pushDAO.queryPushStatus(s) == 0) {
							LOG.info("\t为订阅[" + s.getName() + "]生成数据,请稍后...");
							subController.generateData(s, app);
						}
					}
				}
			}
		}
		LOG.info("[推送JOB]订阅同步完成.");

	}

	public static Scheduler buildJob(AppContext context)
			throws SchedulerException {
		return JobFactory.buildJob(context, PushJob.class, JOB_NAME, JOB_GROUP,
				JOB_TRIGGER, REPEAT_SECONDS);
	}

	public static void main(String[] args) {
		HomeFansDao homeDAO = new HomeFansDaoImpl();
		PushDao pushDAO = new PushDaoImpl();
		AccountDao accountDAO = new AccountDaoImpl();
		SubscriberController subController = new SubscriberController();
		MessageController messageController = new MessageController();
		AppContext context = AppContext.getInstance();
		context.init();
		Custom custom = context.getCustomManager().getCustom(29);
		TaskManager manager = TaskManager.getInstance();

		PushJob job = new PushJob();
		job.customPusher(homeDAO, pushDAO, accountDAO, subController,
				messageController, context, manager, custom, true);

	}

}
