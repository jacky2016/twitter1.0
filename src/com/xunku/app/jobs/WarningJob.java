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
import com.xunku.app.controller.WarningController;
import com.xunku.app.enums.Platform;
import com.xunku.app.model.AppAuth;
import com.xunku.pojo.base.Custom;
import com.xunku.pojo.office.AccountWarn;
import com.xunku.pojo.office.EventWarn;
import com.xunku.pojo.office.WeiboWarn;

/**
 * 预警服务
 * 
 * @author wujian
 * @created on Sep 11, 2014 6:20:57 PM
 */
public class WarningJob implements Job {

	static final String JOB_NAME = "job.name.warning";
	static final String JOB_GROUP = "job.group.warning";
	static final String JOB_TRIGGER = "job.trigger.warning";
	static final String JOB_DESC = "预警服务";
	static int REPEAT_SECONDS = 60 * 30;// 10分钟跑一次
	static boolean running;
	static Logger LOG = LoggerFactory.getLogger(WarningJob.class);

	public static void setRepeat(int repeat) {
		REPEAT_SECONDS = repeat * 60;
	}

	@Override
	public void execute(JobExecutionContext context)
			throws JobExecutionException {
		if (running) {
			return;
		}

		try {
			running = true;

			// 推送任务，应该在HomeJob里面跑，这里只跑推送
			// HomeJob里面跑完数据后，记录到一个表里面，然后由推送服务推送出去
			// 1、 先更新推送任务的状态
			// 2、 获得待推动的推送任务
			// 3、 根据推送的条件获得要推送的数据
			// 4、 根据推送人列表逐个推动数据
			AppContext app = (AppContext) context.getJobDetail()
					.getJobDataMap().get("context");
			List<Custom> customs = app.getCustomManager().getCustoms();
			WarningController controller = new WarningController();
			for (Custom custom : customs) {
				customWarning(app, controller, custom);
			}
		} catch (Exception ex) {
			LOG.error("[预警JOB]预警任务异常!", ex);
		}

		running = false;
	}

	private void customWarning(AppContext app, WarningController controller,
			Custom custom) {
		LOG.info("[预警JOB]开始分析客户[" + custom.getName() + "]的预警任务...");

		warnAccount(app, controller, custom);

		warnEvent(app, controller, custom);

		warnWeibo(app, controller, custom);
		LOG.info("[预警JOB]客户预警同步结束.");
	}

	private void warnWeibo(AppContext app, WarningController controller,
			Custom custom) {
		// 获得授权
		LOG.info("\t分析该客户的微博预警任务...");
		AppAuth auth = app.getCustomManager().getAuthByCustom(Platform.Sina,
				custom);

		if (auth != null) {
			// 获得微博预警的内容
			List<WeiboWarn> wwList = controller.getWeiboWarning(custom.getId());
			for (WeiboWarn warn : wwList) {
				if (warn.isRunning()) {
					controller.makeWarnMessage(warn, auth, app);
				}
			}
		} else {
			LOG.info("\t该客户无可用授权，无法分析微博预警");
		}
	}

	private void warnEvent(AppContext app, WarningController controller,
			Custom custom) {
		// 获得事件预警的内容
		LOG.info("\t分析该客户的事件预警任务...");
		List<EventWarn> ewList = controller.getEventWarning(custom.getId());
		for (EventWarn warn : ewList) {
			if (warn.isRunning()) {
				controller.makeWarnMessage(warn, app);
			}
		}
	}

	private void warnAccount(AppContext app, WarningController controller,
			Custom custom) {
		// 获得帐号预警内容
		LOG.info("\t分析该客户的帐号预警任务...");
		List<AccountWarn> awList = controller.getAccountWarning(custom.getId());
		for (AccountWarn warn : awList) {
			if (warn.isRunning()) {
				controller.makeWarnMessage(warn, app);
			}
		}
	}

	public static Scheduler buildJob(AppContext context)
			throws SchedulerException {

		return JobFactory.buildJob(context, WarningJob.class, JOB_NAME,
				JOB_GROUP, JOB_TRIGGER, REPEAT_SECONDS);
	}

	public static void main(String[] args) {
		AppContext context = AppContext.getInstance();
		context.init();
		WarningController controller = new WarningController();

		Custom custom = context.getCustomManager().getCustom(10);

		WarningJob job = new WarningJob();
		job.customWarning(context, controller, custom);

		return;
	}

}
