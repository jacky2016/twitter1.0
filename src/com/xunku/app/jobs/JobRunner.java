package com.xunku.app.jobs;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xunku.app.AppContext;
import com.xunku.app.jobs.account.AccountMonitorJob;
import com.xunku.app.jobs.event.EventJob;
import com.xunku.app.jobs.home.HomeJob;
import com.xunku.app.jobs.official.CustomFansJob;
import com.xunku.app.jobs.spread.TweetMonitorAnalysisJob;
import com.xunku.utils.PropertiesUtils;

/**
 * 任务运行主程序
 * 
 * @author wujian
 * @created on Jun 30, 2014 11:51:31 AM
 */
public class JobRunner {

	static Logger log = LoggerFactory.getLogger(JobRunner.class);

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			JobRunner runner = new JobRunner();
			AppContext context = AppContext.getInstance();
			context.init();

			System.out.println("请选择任务编号:");
			System.out.println("<0> 微博发送JOB");
			System.out.println("\t-- 运行所有的JOB");
			System.out.println("<1> 微博发送JOB");
			System.out.println("\t-- 负责发送已经被提交的微博");
			System.out.println("<2> 事件数据同步JOB");
			System.out.println("\t-- 负责同步所有客户定义的事件数据");
			System.out.println("<3> 官微数据同步JOB");
			System.out.println("\t-- 负责同步官微的微博数据（发，转，评，@我）以及粉丝数据");
			System.out.println("<4> 传播分析JOB");
			System.out.println("\t-- 负责同步客户需要分析的传播分析微博");
			System.out.println("<5> 预警JOB");
			System.out.println("\t-- 负责预警，每10分钟轮询一次预警");
			System.out.println("<6> 帐号分析JOB");
			System.out.println("\t-- 负责产生帐号数据，该帐号数据全部由讯库产生");
			System.out.println("<7> 首页分析JOB");
			System.out.println("\t-- 负责首页4个模块的分析以及验证绑定帐号是否过期的提醒");
			System.out.println("<8> 推送JOB");
			System.out.println("\t-- 产生推送数据和验证绑定帐号是否过期的提醒");
			System.out.println("<9> 商业接口JOB");
			System.out.println("\t-- 跑商业数据接口专用");

			runner.initProperties();

			int jobNum = 0;
			BufferedReader br = new BufferedReader(new InputStreamReader(
					System.in));
			System.out.println("输入的任务编号：");
			jobNum = Integer.parseInt(br.readLine());
			/*
			 * int port = 9000 + jobNum; Server server = new Server(port);
			 * server.start(); System.out.println("该任务的监听端口为:" + port);
			 */
			runner.runJobs(jobNum, context);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void initProperties() {
		try {
			String sr = PropertiesUtils
					.getString("config", "job.sender.repeat");
			String wr = PropertiesUtils.getString("config",
					"job.warning.repeat");
			String ar = PropertiesUtils.getString("config",
					"job.account.repeat");
			String er = PropertiesUtils.getString("config", "job.event.repeat");
			String hr = PropertiesUtils.getString("config", "job.home.repeat");
			String cr = PropertiesUtils
					.getString("config", "job.custom.repeat");
			String spr = PropertiesUtils.getString("config",
					"job.spread.repeat");
			String ppr = PropertiesUtils.getString("config", "job.push.repeat");

			SendJob.setRepeat(Integer.parseInt(sr));
			WarningJob.setRepeat(Integer.parseInt(wr));
			AccountMonitorJob.setRepeat(Integer.parseInt(ar));
			EventJob.setRepeat(Integer.parseInt(er));
			HomeJob.setRepeat(Integer.parseInt(hr));
			CustomFansJob.setRepeat(Integer.parseInt(cr));
			TweetMonitorAnalysisJob.setRepeat(Integer.parseInt(spr));
			PushJob.REPEAT_SECONDS = Integer.parseInt(ppr);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private int runJobs(int jobNum, AppContext context) throws Exception {
		switch (jobNum) {
		case 0: {
			SendJob.buildJob(context).start();
			EventJob.buildJob(context).start();
			CustomFansJob.buildJob(context).start();
			TweetMonitorAnalysisJob.buildJob(context).start();
			WarningJob.buildJob(context).start();
			AccountMonitorJob.buildJob(context).start();
			HomeJob.buildJob(context).start();
			PushJob.buildJob(context).start();
			BusinessJob.buildJob(context).start();
			break;
		}
		case 1: {
			SendJob.buildJob(context).start();
			break;
		}
		case 2: {
			EventJob.buildJob(context).start();
			break;
		}
		case 3: {
			CustomFansJob.buildJob(context).start();
			break;
		}
		case 4: {
			TweetMonitorAnalysisJob.buildJob(context).start();
			break;
		}
		case 5: {
			WarningJob.buildJob(context).start();
			break;
		}
		case 6: {
			AccountMonitorJob.buildJob(context).start();
			break;
		}
		case 7: {
			HomeJob.buildJob(context).start();
			break;
		}
		case 8: {
			PushJob.buildJob(context).start();
			break;
		}
		case 9:{
			BusinessJob.buildJob(context).start();
		}
		default: {
			log.info("其它JOB还未实现...");
			return -1;
		}
		}
		return 0;

	}
}
