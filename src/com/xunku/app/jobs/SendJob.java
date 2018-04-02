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
import com.xunku.app.controller.SendingController;
import com.xunku.app.interfaces.ITweet;
import com.xunku.app.monitor.CustomMonitor;
import com.xunku.app.result.Result;
import com.xunku.dao.my.SendingDao;
import com.xunku.daoImpl.base.UserDaoImpl;
import com.xunku.daoImpl.my.SendingDaoImpl;
import com.xunku.pojo.base.Custom;
import com.xunku.pojo.base.User;
import com.xunku.pojo.my.Sending;

/**
 * 发送JOB，该JOB负责发送所有的定时发送微博，和已经过期的未发送成功的微博
 * 
 * @author wujian
 * @created on Aug 21, 2014 4:06:14 PM
 */
public class SendJob implements Job {

	public static String JOB_NAME = "send job";
	public static String JOB_GROUP = "send_job_group";
	public static String JOB_TRIGGER = "send_job_trigger";

	static int REPEAT_SECONDS = 60;
	private static Logger LOG = LoggerFactory.getLogger(SendJob.class);
	static boolean _running = false;
	static int cnt = 1;

	public static void setRepeat(int repeat) {
		REPEAT_SECONDS = repeat * 60;
	}

	@Override
	public void execute(JobExecutionContext context)
			throws JobExecutionException {
		if (_running) {
			// LOG.info("上一轮[定时发送微博]JOB还在运行，等待中...");
			return;
		}
		AppContext appContext = (AppContext) context.getJobDetail()
				.getJobDataMap().get("context");

		_running = true;
		LOG.info("[定时发送微博]开始运行,第" + cnt + "次");

		try {
			SendingController controller = new SendingController(appContext);
			// 获取Sending里面待发送的微博列表
			SendingDao dao = new SendingDaoImpl();
			List<Sending> sendList = dao.queryAllUnSending();
			LOG.info("[定时发送微博]本轮计划发送:" + sendList.size() + "条微博/转发/评论");
			for (Sending sending : sendList) {
				this.send(sending, controller, appContext);
			}
			LOG.info("[定时发送微博]结束");

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		cnt++;
		_running = false;

	}

	private void send(Sending sending, SendingController controller,
			AppContext context) {
		LOG.info("[定时发送微博]本次计划由" + sending.getSendList().size() + "个账号发布["
				+ sending.getText() + "]");
		if (sending.getSent() <= System.currentTimeMillis()) {
			User user = new UserDaoImpl().queryByUid(sending.getUserid());
			if (user == null) {
				return;
			}
			List<Result<ITweet>> rst = controller.sendTweet(sending, user, 5);
			putTweet(context, user, rst);
			// 这里要入库到Custom库里面去
		}
	}

	private void putTweet(AppContext context, User user,
			List<Result<ITweet>> rst) {
		if (rst != null) {
			// 转发的原始微博如果当前客户未关注，则需要入库
			Custom custom = context.getCustomManager().getCustom(
					user.getCustomID());
			if (custom != null) {
				CustomMonitor monitor = custom.getMonitor();
				for (Result<ITweet> r : rst) {
					if (r.getErrCode() == 0) {
						monitor.getStore(context).put(r.getData());
					}
				}
			}
		}
	}

	public static Scheduler buildJob(AppContext context)
			throws SchedulerException {
		return JobFactory.buildJob(context, SendJob.class, SendJob.JOB_NAME,
				SendJob.JOB_GROUP, SendJob.JOB_TRIGGER, SendJob.REPEAT_SECONDS);
	}

	public static void main(String[] args) {
		AppContext context = AppContext.getInstance();
		context.init();

		SendingDao dao = new SendingDaoImpl();
		Sending sending = dao.querySendById(2324);

		SendJob job = new SendJob();
		job.send(sending, new SendingController(context), context);
	}
}
