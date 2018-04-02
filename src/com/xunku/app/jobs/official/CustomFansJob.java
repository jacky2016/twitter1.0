package com.xunku.app.jobs.official;

import java.util.List;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xunku.app.AppContext; //import com.xunku.app.crawl.Crawler;
import com.xunku.app.Fetcher;
import com.xunku.app.jobs.JobFactory; //import com.xunku.app.monitor.CustomMonitor;
import com.xunku.app.model.ApiException;
import com.xunku.app.model.AppAccount;
import com.xunku.app.model.AppAuth;
import com.xunku.app.stores.MCustomStore;
import com.xunku.pojo.base.Custom;

/**
 * 最近7天粉丝数据，该任务负责完成客户粉丝的爬取
 * 
 * @author wujian
 * @created on Aug 5, 2014 2:30:56 PM
 */
public class CustomFansJob implements Job {

	private static Logger LOG = LoggerFactory.getLogger(CustomFansJob.class);

	private static final String JOB_NAME = "job.name.custom.fans";
	private static final String JOB_GROUP = "job.group.custom.fans";
	static final String JOB_TRIGGER = "job.trigger.custom.fans";
	static final String JOB_DESC = "定时获取客户帐号的粉丝任务";
	static int REPEAT_SECONDS = 60 * 20;// 10分钟跑一次
	static boolean running = false;

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
			AppContext app = (AppContext) context.getJobDetail()
					.getJobDataMap().get("context");
			List<Custom> customs = app.getCustomManager().getCustoms();
			for (Custom custom : customs) {
				fetchCustom(app, custom);
			}
		} catch (Exception ex) {
			LOG.error("任务同步出错...", ex);
			running = false;
		}
		running = false;
	}

	private void fetchCustom(AppContext context, Custom custom) {
		Fetcher fetcher = new Fetcher();
		long s = System.currentTimeMillis();
		// LOG.info("正在提交客户[" + custom.getName() + "]的同步任务...");
		// 该落地过程会获取客户绑定的每个微博帐号的微博列表
		// 提到我的微博列表，发给我的评论等，网评员统计处理器绑定在该监视器上

		LOG.info("[客户同步JOB]开始同步客户[" + custom.getName() + "]的微博数据...");

		MCustomStore store = context.getMOfficialManager().getStore(custom);
		int accountCnt = 0;
		if (custom.getAccounts() != null) {
			accountCnt = custom.getAccounts().size();
		}
		LOG.info("[客户同步JOB]该客户总计需要同步[" + accountCnt + "]个帐号...");
		if (custom.getAccounts() != null) {
			for (AppAccount acc : custom.getAccounts()) {
				LOG.info("\t正在同步帐号[" + acc.getName() + "]的基本信息");
				AppAuth auth = acc.getAuth(context.getCustomManager()
						.getMainApp(acc.getPlatform()).getId());
				if (auth == null) {
					LOG.info("\t\t该帐号没有可用授权，无法同步...");
					break;
				}
				try {
					LOG.info("\t\t同步[" + acc.getName() + "]发布的微博列表...");
					fetcher.fetchUserPosts(store, auth, false);
					LOG.info("\t\t同步提到[" + acc.getName() + "]的微博/评论列表...");
					fetcher.fetchMentions(store, auth, false);
					LOG.info("\t\t同步[" + acc.getName() + "]发出的评论，和发给["
							+ acc.getName() + "]的评论列表...");
					fetcher.fetchComments(store, auth, false);
					LOG.info("\t\t同步[" + acc.getName() + "]的粉丝...");
					fetcher.fetchFollowerList(auth, acc, context
							.getAccountManager());

				} catch (ApiException e) {
					LOG.error("\t\t同步帐号异常-->" + e.getError());
				}
				LOG.info("\t同步帐号[" + acc.getName() + "]的基本信息完成.");
			}
		}
		LOG.info("[客户同步JOB]客户[" + custom.getName() + "]的帐号同步完成，耗时"
				+ (System.currentTimeMillis() - s) / 1000 + "s");
	}

	public static Scheduler buildJob(AppContext context)
			throws SchedulerException {
		return JobFactory.buildJob(context, CustomFansJob.class, JOB_NAME,
				JOB_GROUP, JOB_TRIGGER, REPEAT_SECONDS);
	}

	public static void main(String[] args) {
		AppContext context = AppContext.getInstance();
		context.init();
		Custom custom = context.getCustomManager().getCustom(10);
		CustomFansJob job = new CustomFansJob();
		job.fetchCustom(context, custom);

	}

}
