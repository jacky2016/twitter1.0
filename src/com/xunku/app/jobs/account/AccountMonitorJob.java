package com.xunku.app.jobs.account;

import java.util.List;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xunku.app.AppContext;
import com.xunku.app.crawl.Crawler;
import com.xunku.app.helpers.DateHelper;
import com.xunku.app.jobs.JobFactory;
import com.xunku.app.monitor.AccountMonitor;
import com.xunku.dao.account.AccountDao;
import com.xunku.daoImpl.account.AccountDaoImpl;
import com.xunku.pojo.base.CrawlTask;
import com.xunku.pojo.base.Pager;
import com.xunku.utils.Pagefile;

/**
 * 该任务将所有监测帐号的的粉丝数据落地到帐号库中
 * 
 * @author wujian
 * @created on Aug 5, 2014 2:45:12 PM
 */
public class AccountMonitorJob implements Job {

	static final String JOB_NAME = "job.name.monitor.account.fans";
	static final String JOB_GROUP = "job.group.monitor.account.fans";
	static final String JOB_TRIGGER = "job.trigger.monitor.account.fans";
	static final String JOB_DESC = "帐号分析_粉丝分析";
	static int REPEAT_SECONDS = 60 * 60;// 60分钟跑一次

	private static Logger LOG = LoggerFactory
			.getLogger(AccountMonitorJob.class);

	public static void setRepeat(int repeat) {
		REPEAT_SECONDS = repeat * 60;
	}

	@Override
	public void execute(JobExecutionContext context)
			throws JobExecutionException {

		AppContext appContext = (AppContext) context.getJobDetail()
				.getJobDataMap().get("context");

		AccountDao accountDAO = new AccountDaoImpl();

		Pager pager = Pager.createPager(1, 200);
		Pagefile<AccountMonitor> accounts = accountDAO.queryAccountList(pager);

		// 清除7days之前的任务
		accountDAO.clear(System.currentTimeMillis()
				+ (DateHelper.ONE_MONTH * 3));

		if (accounts != null) {
			int pageCount = (accounts.getRealcount() / 200) + 1;
			LOG.info("[帐号分析]本次需要同步[" + accounts.getRealcount() + "]个监测帐号分["
					+ pageCount + "]页爬取...");
			this.fetch(accounts.getRows(), appContext);
			for (int i = 2; i < pageCount; i++) {
				pager.setPageIndex(i);
				accounts = accountDAO.queryAccountList(pager);
				if (accounts != null) {
					this.fetch(accounts.getRows(), appContext);
				}
			}
		}

		LOG.info("[帐号分析]同步结束.");

	}

	private void fetch(AccountMonitor monitor, AppContext context,
			Crawler crawler, CrawlTask testTask) {
		LOG.info("[帐号分析]正在同步帐号[" + monitor.getNick() + "]...");
		if (testTask != null) {
			LOG.info("测试任务...");
			crawler.fetch(context, testTask, monitor, true);
		} else {
			// 提交任务
			LOG.info("[帐号分析]提交监测对象的新任务...");
			crawler.submit(crawler.createTasks(monitor));
			LOG.info("[帐号分析]获得监测对象上次的任务数据...");
			List<CrawlTask> tasks = crawler.getTasks(monitor, 10);

			for (CrawlTask task : tasks) {
				crawler.fetch(context, task, monitor, false);
			}

			LOG.info("[帐号分析]同步帐号[" + monitor.getNick() + "] --ok");
		}
	}

	private void fetch(List<AccountMonitor> accounts, AppContext context) {
		// 首先调用API批量获得指定一批用户的微博列表
		Crawler crawler = Crawler.getInstance();
		// 获得落地信息
		for (AccountMonitor monitor : accounts) {
			this.fetch(monitor, context, crawler, null);
		}
	}

	public static Scheduler buildJob(AppContext context)
			throws SchedulerException {

		return JobFactory.buildJob(context, AccountMonitorJob.class, JOB_NAME,
				JOB_GROUP, JOB_TRIGGER, REPEAT_SECONDS);
	}

	public static void main(String[] args) {
		AppContext context = AppContext.getInstance();
		context.init();
		AccountDao accountDAO = new AccountDaoImpl();

		// accountDAO.clear(System.currentTimeMillis() + DateHelper.ONE_MONTH);

		AccountMonitor monitor = accountDAO.queryAccountMonitorById(37);
		AccountMonitorJob job = new AccountMonitorJob();
		Crawler crawler = Crawler.getInstance();
		// CrawlTask task = crawler.getTask(22286);
		// job.fetch(monitor, context, crawler, task);
		CrawlTask task = crawler.getTask(22295);
		job.fetch(monitor, context, crawler, task);
	}

}
