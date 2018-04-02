package com.xunku.app.jobs.home;

import java.util.List;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xunku.app.AppContext;
import com.xunku.app.Utility;
import com.xunku.app.controller.MessageController;
import com.xunku.app.controller.SubscriberController;
import com.xunku.app.enums.Platform;
import com.xunku.app.interfaces.IAccount;
import com.xunku.app.jobs.JobFactory;
import com.xunku.app.manager.TaskManager;
import com.xunku.app.model.AppAccount;
import com.xunku.app.model.AppAuth;
import com.xunku.app.result.Result;
import com.xunku.dao.base.AccountDao;
import com.xunku.dao.home.HomeFansDao;
import com.xunku.dao.push.PushDao;
import com.xunku.daoImpl.base.AccountDaoImpl;
import com.xunku.daoImpl.home.HomeFansDaoImpl;
import com.xunku.daoImpl.push.PushDaoImpl;
import com.xunku.pojo.base.Custom;
import com.xunku.pojo.push.Subscriber;
import com.xunku.pojo.task.Group;
import com.xunku.pojo.task.Task;

public class HomeJob implements Job {

	static final String JOB_NAME = "job.name.home";
	static final String JOB_GROUP = "job.group.home";
	static final String JOB_TRIGGER = "job.trigger.home";
	static final String JOB_DESC = "首页任务";
	static int REPEAT_SECONDS = 60 * 60 * 1;// 6小时更新一次
	static boolean running = false;

	static Logger LOG = LoggerFactory.getLogger(HomeJob.class);

	public static void setRepeat(int repeat) {
		REPEAT_SECONDS = repeat * 60;
	}

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
		// 创建fans任务
		List<Custom> customs = app.getCustomManager().getCustoms();
		TaskManager manager = TaskManager.getInstance();
		for (Custom custom : customs) {
			customHome(homeDAO, pushDAO, accountDAO, subController,
					messageController, app, manager, custom);
		}

		running = false;
	}

	private void customHome(HomeFansDao homeDAO, PushDao pushDAO,
			AccountDao accountDAO, SubscriberController subController,
			MessageController messageController, AppContext app,
			TaskManager manager, Custom custom) {
		LOG.info("[首页JOB]开始同步客户[" + custom.getName() + "]的首页统计信息...");
		List<Task> tasks = manager.getTasksByCustomID(custom.getId());
		LOG.info("\t总计需要统计[" + tasks.size() + "]个任务,请稍候...");
		List<Group> groups = manager.getTopGroups(custom.getId());
		int sinaCnt = 0;
		int qqCnt = 0;
		int rmCnt = 0;

		for (Task task : tasks) {
			LOG.info("\t\t统计任务[" + task.getName() + "]");
			int s = manager.searchTask4Count(task, Platform.Sina);
			sinaCnt += s;
			int q = manager.searchTask4Count(task, Platform.Tencent);
			qqCnt += q;
			int r = manager.searchTask4Count(task, Platform.Renmin);
			rmCnt += r;

			for (Group group : groups) {
				if (group.getSubGroupIds().contains(task.getGroupID())) {
					group.setSina(group.getSina() + s);
					group.setQq(group.getQq() + q);
					group.setRenmin(group.getRenmin() + r);
				}
			}
		}

		// 推送最小单位是一天
		LOG.info("\t开始同步订阅信息...");
		List<Subscriber> list = pushDAO.querySubscriberByCustomId(custom
				.getId());

		LOG.info("[首页JOB]该客户有[" + list.size() + "]个订阅");
		for (Subscriber s : list) {
			// 先更新推送任务状态
			LOG.info("\t\t检查该订阅[" + s.getName() + "]的状态...");
			if (!s.isStop()) {
				// 第一次执行的时间到了则生成数据
				if (s.getFirstRunTime().getTime() <= System.currentTimeMillis()) {
					// 检查今天是否已经有推送了如果有了就不在产生数据了
					if (pushDAO.queryPushStatus(s) == 0) {
						LOG.info("\t\t为订阅[" + s.getName() + "]生成数据,请稍后...");
						subController.generateData(s, app);
					}
				}
			}
		}

		// 更新首页的任务统计信息
		LOG.info("\t更新首页的任务统计信息");
		homeDAO.updateTaskStatis(custom.getId(), System.currentTimeMillis(),
				sinaCnt, qqCnt, rmCnt);

		for (Group g : groups) {
			homeDAO
					.updateCategories(custom.getId(), g.getId(), System
							.currentTimeMillis(), g.getSina(), g.getQq(), g
							.getRenmin());
		}

		LOG.info("\t任务统计结束");

		// 检查帐号授权是否快要过期了...
		LOG.info("\t检查帐号的授权是否快要过期...");
		for (AppAccount acc : custom.getAccounts()) {
			long days = accountDAO.remainAuth(acc.getAccountId());
			// 提前一天提醒...
			if (days <= 1) {
				messageController.sendAccountExpired(custom.getId(), acc
						.getName(), days, false);
			}
		}
		
		// 更新微博原创率和每日平均微博数量，由于更新频率低放到HomeJOB里面
		LOG.info("\t同步该客户下帐号监测的微博原创率和每日微博数量...");

		LOG.info("\t开始同步帐号粉丝趋势信息...");
		for (AppAccount acc : custom.getAccounts()) {
			AppAuth auth = app.getCustomManager().getAuthByCustom(
					acc.getPlatform(), custom);
			if (auth == null) {
				LOG.info("[首页JOB]帐号没有授权信息，或者授权信息已经过期，无法同步信息！");
				break;
			} else {
				Result<IAccount> account = app.getAccountManager()
						.accountGetByUcodeOnline(acc.getUcode(),
								acc.getPlatform(), auth);

				if (account.getErrCode() == 0) {
					IAccount a = account.getData();
					homeDAO.updateFanTrend(a.getUcode(), Utility.getPlatform(a
							.getPlatform()), System.currentTimeMillis(), a
							.getWeibos(), a.getFollowers(), a.getFriends());
				}
			}
		}
		LOG.info("\t同步帐号粉丝趋势信息结束啦...");
		LOG.info("[首页JOB]客户[" + custom.getName() + "]首页统计信息同步结束...");
		LOG.info("\n\n");
	}

	public static Scheduler buildJob(AppContext context)
			throws SchedulerException {
		return JobFactory.buildJob(context, HomeJob.class, JOB_NAME, JOB_GROUP,
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
		Custom custom = context.getCustomManager().getCustom(10);
		TaskManager manager = TaskManager.getInstance();

		HomeJob job = new HomeJob();
		job.customHome(homeDAO, pushDAO, accountDAO, subController,
				messageController, context, manager, custom);

	}
}
