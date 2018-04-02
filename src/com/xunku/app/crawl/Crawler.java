package com.xunku.app.crawl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import weibo4j.org.json.JSONArray;
import weibo4j.org.json.JSONException;
import weibo4j.org.json.JSONObject;

import com.xunku.app.AppContext;
import com.xunku.app.Utility;
import com.xunku.app.enums.Platform;
import com.xunku.app.interfaces.IAccount;
import com.xunku.app.interfaces.ITweet;
import com.xunku.app.interfaces.ITweetStore;
import com.xunku.app.model.AppAccount;
import com.xunku.app.model.accounts.AccountFactory;
import com.xunku.app.model.tweets.TweetFactory;
import com.xunku.app.monitor.AccountMonitor;
import com.xunku.app.monitor.CustomMonitor;
import com.xunku.app.monitor.IMonitor;
import com.xunku.dao.account.AccountDao;
import com.xunku.dao.base.CrawlTaskDao;
import com.xunku.daoImpl.account.AccountDaoImpl;
import com.xunku.daoImpl.base.CrawlTaskDaoImpl;
import com.xunku.pojo.base.CrawlTask;
import com.xunku.pojo.base.Custom;
import com.xunku.service.WeiboWebService;

/**
 * 爬虫服务
 * 
 * @author wujian
 * @created on Jul 29, 2014 10:56:37 AM
 */
public class Crawler {

	final int ONE_HOUR = 1000 * 60 * 60;
	private static Logger LOG = LoggerFactory.getLogger(Crawler.class);

	static Crawler _crawler;

	/**
	 * 获得一个唯一的爬虫实例
	 * 
	 * @return
	 */
	public synchronized static Crawler getInstance() {
		if (_crawler == null) {
			_crawler = new Crawler();
		}
		return _crawler;
	}

	/**
	 * 提交一组爬虫任务
	 * 
	 * @param tasks
	 */
	public void submit(List<CrawlTask> tasks) {
		for (CrawlTask task : tasks) {
			this.submit(task);
		}
	}

	/**
	 * 提交一个爬虫任务
	 * 
	 * @param task
	 */
	public void submit(CrawlTask task) {
		if (task.getExecuted() == 0) {
			int domainid = task.getDomainid();
			String homeUrl = task.getHomeUrl();
			int crawltype = task.getType();
			String token = task.getToken();

			String[] eName = { "domainid", "homeUrl", "crawltype", "token" };
			String[] param = { String.valueOf(domainid), homeUrl,
					String.valueOf(crawltype), token };
			try {
				String status = WeiboWebService.getServiceTemplateMethod(eName,
						"submitUserTask", param);
				if (status.equals("ok")) {
					task.setExecuted(System.currentTimeMillis());
					CrawlTaskDao dao = new CrawlTaskDaoImpl();
					dao.update(task);

				} else {
					LOG.info("任务[" + task.getId() + "]提交失败，下次尝试重新提交.");
				}
			} catch (IOException ex) {
				LOG.error("提交爬虫任务失败", ex);
			}
		}
	}

	/**
	 * 获得当前客户下所有创建时间超过指定时间但是未超过过期时间的任务（过期时间默认是距离现在30分钟之内的任务）
	 * 
	 * @param monitor
	 *            监视器
	 * @param intervalMins
	 *            间隔多少分钟的
	 * @return
	 */
	public List<CrawlTask> getTasks(IMonitor monitor, int intervalMins) {

		CrawlTaskDao dao = new CrawlTaskDaoImpl();

		// 比这个时间小的就可以取数据了
		long created = System.currentTimeMillis()
				- (intervalMins * (1000 * 60));

		// 超过30分钟的也不要
		long expried = System.currentTimeMillis() - (ONE_HOUR * 24);

		// 获取未取结果的任务创建时间30分钟之内，10分钟之后的
		List<CrawlTask> tasks = dao.queryUnAcquiredTasks(
				monitor.getMonitorId(), monitor.getMonitorType(), created,
				expried);

		return tasks;
	}

	/**
	 * 根据ID获得Task对象
	 * 
	 * @param crawlTaskId
	 * @return
	 */
	public CrawlTask getTask(int crawlTaskId) {
		CrawlTaskDao dao = new CrawlTaskDaoImpl();
		CrawlTask task = dao.queryById(crawlTaskId);
		return task;
	}

	/**
	 * 从爬虫任务上接收结果
	 * 
	 * @param context
	 * @param task
	 * @param monitor
	 * @return
	 */
	public ItemAccount reviceResult(CrawlTask task, boolean local) {

		if (local) {
			return this._reviceResult(task);
		} else {
			if (task.getAcquired() == 0) {
				try {
					String json = WeiboWebService.getServiceTemplateMethod(
							new String[] { "token" }, "getUserTaskResult",
							new String[] { task.getToken() });

					// 有返回结果才更新状态，否则不更新状态
					if (!Utility.isNullOrEmpty(json)) {
						task.setAcquired(System.currentTimeMillis());
						CrawlTaskDao dao = new CrawlTaskDaoImpl();
						// 这里只能是一个JS对象，不能是一个数组，所以把两边的括号[]去掉
						json = json.substring(1, json.length() - 1);
						task.setResult(json);
						dao.update(task);
						return this._reviceResult(task);
					}
					return null;
				} catch (IOException ex) {
					LOG.error("爬虫数据落地失败", ex);
					return null;
				}
			}
		}
		return null;
	}

	/**
	 * 将结果集落地
	 * 
	 * @param context
	 * @param task
	 * @param monitor
	 */
	public void fetch(AppContext context, CrawlTask task,
			AccountMonitor monitor, boolean local) {
		ItemAccount result = this.reviceResult(task, local);

		if (result != null) {
			AccountDao dao = new AccountDaoImpl();
			dao.updateMonitorCnt(monitor.getMonitorId(),
					result.getWeiboCount(), result.getFansCount(), result
							.getFollowCount());

			// 落地微博数据
			ITweetStore store = monitor.getStore(context);
			if (store == null) {
				LOG.info("\t未获得监测对象的存储...");
				return;
			}

			List<ItemTweet> tweets = result.getTweets();

			LOG.info("\t更新微博，总计:" + tweets.size());
			for (ItemTweet tweet : tweets) {
				if (!Utility.isNullOrEmpty(tweet.getTransferUrl())) {
					//System.out.println("有原微博");
				}
				ITweet t = TweetFactory.createTweet(tweet, store);
				store.put(t);
			}
			store.flushHandlers();

			LOG.info("\t更新粉丝趋势表，粉丝数:" + result.getFansCount());

			// 更新当日增量粉丝
			dao.updateRealtime(store.getPool(), monitor.getMonitorId(), System
					.currentTimeMillis(), result.getFansCount());


			// 更新微博原创率和每日平均微博数量，由于更新频率低放到HomeJOB里面
			
			// 落地帐号关系数据
			List<IAccount> followers = convert(result.getFollowers());
			context.getAccountManager().followerCreate(result.getUid(),
					Utility.getPlatform(result.getDomainid()), followers);
		} else {
			LOG.info(monitor.getNick() + "的任务[" + task.getId() + "]未取得结果.");
		}
	}

	/**
	 * 获得爬虫的结果集
	 * 
	 * @param task
	 * @return
	 */
	public ItemAccount _reviceResult(CrawlTask task) {

		if (Utility.isNullOrEmpty(task.getResult())) {
			LOG.info("该任务没有返回任何数据...");
			return null;
		}
		ItemAccount result = null;

		// 目前没有1，只有2,3
		if (task.getType() == 2) {
			result = this.getFollowersResult(task);
		}
		if (task.getType() == 3) {
			result = this.getTweetResult(task);
		}

		return result;
	}

	public List<CrawlTask> createTasks(CustomMonitor monitor) {
		List<CrawlTask> tasks = new ArrayList<CrawlTask>();
		Custom custom = monitor.getCustom();
		for (AppAccount acc : custom.getAccounts()) {
			tasks.add(this.createTask(monitor, acc, CrawlerType.follower));
			// tasks.add(this.createTask(monitor, acc, CrawlerType.following));
			tasks.add(this.createTask(monitor, acc, CrawlerType.tweet));
		}
		return tasks;

	}

	public List<CrawlTask> createTasks(AccountMonitor monitor) {

		List<CrawlTask> tasks = new ArrayList<CrawlTask>();
		// 创建一个粉丝任务
		CrawlTask taskFollower = this.createTask(monitor, CrawlerType.follower);
		tasks.add(taskFollower);

		// 创建一个关注任务
		// CrawlTask taskFollowing =
		// this.createTask(monitor,CrawlerType.following);
		// tasks.add(taskFollowing);

		// 创建一个微博任务
		CrawlTask taskTweet = this.createTask(monitor, CrawlerType.tweet);
		tasks.add(taskTweet);
		return tasks;
	}

	private CrawlTask createTask(CustomMonitor monitor, AppAccount acc, int mode) {

		// 任务关键字：homeurl,domainid,monitorid,monitorType
		// 如果已经有这个任务并且未获取数据，则返回这条任务即可
		CrawlTask task = new CrawlTask();
		task.setAcquired(0);
		task.setCreated(System.currentTimeMillis());
		task.setCustomId(monitor.getCustomId());
		task.setDomainid(this.getDomainId(acc.getPlatform()));
		task.setExecuted(0);
		String key = "";
		if (acc.getPlatform() == Platform.Sina) {
			key = acc.getUid();
		} else if (acc.getPlatform() == Platform.Tencent) {
			key = acc.getName();
		}
		task.setHomeUrl(this.getHomeUrl(acc.getPlatform(), key));
		task.setMonitorid(monitor.getMonitorId());
		task.setTimezone(monitor.getTimestamp());
		task.setToken(Utility.getToken());
		task.setType(mode);
		task.setMonitorType(monitor.getMonitorType());
		CrawlTaskDao dao = new CrawlTaskDaoImpl();
		dao.insert(task);
		this.submit(task);
		return task;
	}

	private CrawlTask createTask(AccountMonitor monitor, int mode) {
		CrawlTask task = new CrawlTask();

		task.setAcquired(0);
		task.setCreated(System.currentTimeMillis());
		task.setCustomId(monitor.getCustomId());
		task.setDomainid(this.getDomainId(monitor.getPlatform()));
		task.setExecuted(0);
		String key = "";
		if (monitor.getPlatform() == Platform.Sina) {
			key = monitor.getUid();
		} else if (monitor.getPlatform() == Platform.Tencent) {
			key = monitor.getNick();
		}
		task.setHomeUrl(this.getHomeUrl(monitor.getPlatform(), key));
		task.setMonitorid(monitor.getMonitorId());
		task.setMonitorType(monitor.getMonitorType());
		task.setTimezone(monitor.getTimestamp());
		task.setToken(Utility.getToken());
		task.setType(mode);
		CrawlTaskDao dao = new CrawlTaskDaoImpl();// null;
		dao.insert(task);
		this.submit(task);
		return task;
	}

	/**
	 * json2object
	 * 
	 * @param json
	 * @param dataElName
	 * @return
	 */
	private ItemAccount accountFromJSON(String json, String dataElName) {
		try {
			// json = json.substring(1, json.length() - 1);
			JSONObject jsonObject = new JSONObject(json);
			JSONObject homeUser = jsonObject.getJSONObject("homeUser");
			JSONArray dataList = jsonObject.getJSONArray(dataElName);
			ItemAccount result = ItemAccount.create(homeUser);
			if (result != null) {
				for (int i = 0; i < dataList.length(); i++) {
					ItemAccount acc = ItemAccount.create(dataList
							.getJSONObject(i));
					if (dataElName.equals("fansList")) {
						result.getFollowers().add(acc);
					} else if (dataElName.equals("followsList")) {
						result.getFollowings().add(acc);
					}
				}
			}
			return result;

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	private int getDomainId(Platform platform) {
		if (platform == Platform.Sina)
			return 1;
		else if (platform == Platform.Tencent)
			return 2;
		else if (platform == Platform.Renmin)
			return 5;

		return 0;
	}

	private String getHomeUrl(Platform platform, String key) {

		if (platform == Platform.Sina) {
			return "http://weibo.com/" + key;
		} else if (platform == Platform.Tencent) {
			return "http://t.qq.com/" + key;
		}

		return "";
	}

	/**
	 * 根据ItemAccount获得一组IAccount对象
	 * 
	 * @param accs
	 * @return
	 */
	private List<IAccount> convert(List<ItemAccount> accs) {
		List<IAccount> result = new ArrayList<IAccount>();
		for (ItemAccount acc : accs) {
			IAccount account = AccountFactory.createAccount(acc);
			result.add(account);
		}
		return result;
	}

	/**
	 * 获得粉丝结果集
	 * 
	 * @param task
	 * @return
	 */
	private ItemAccount getFollowersResult(CrawlTask task) {
		if (!Utility.isNullOrEmpty(task.getResult())) {
			if (task.getType() == 2) {
				ItemAccount followers = this.accountFromJSON(task.getResult(),
						"fansList");
				return followers;
			}
		}
		return null;
	}

	/**
	 * 获得微博结果集
	 * 
	 * @param task
	 * @return
	 */
	private ItemAccount getTweetResult(CrawlTask task) {
		if (!Utility.isNullOrEmpty(task.getResult())) {
			if (task.getType() == 3) {
				try {
					JSONObject jsonObject = new JSONObject(task.getResult());
					JSONObject userObj = jsonObject.getJSONObject("user");
					if (userObj == null) {
						return null;
					}
					ItemAccount result = ItemAccount.create(userObj);
					JSONArray dataList = jsonObject.getJSONArray("dataList");
					for (int i = 0; i < dataList.length(); i++) {
						result.getTweets().add(
								ItemTweet.create(dataList.getJSONObject(i)));
					}
					return result;
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}

}