package com.xunku.app.monitor;

import java.util.List;

import com.xunku.app.AppContext;
import com.xunku.app.crawl.Crawler;
import com.xunku.app.db.AccountManager;
import com.xunku.app.enums.Platform;
import com.xunku.app.handlers.AccountPostHandler;
import com.xunku.app.handlers.maccounts.RealtimeHandler;
import com.xunku.app.interfaces.IAccount;
import com.xunku.app.interfaces.ITweetStore;
import com.xunku.app.manager.CustomManager;
import com.xunku.app.model.ApiException;
import com.xunku.app.model.AppAuth;
import com.xunku.app.model.Pooling;
import com.xunku.app.result.Result;
import com.xunku.app.stores.MAccountStore;
import com.xunku.constant.PortalCST;
import com.xunku.pojo.base.CrawlTask;
import com.xunku.pojo.base.User;

/**
 * 帐号监测
 * 
 * @author wujian
 * @created on Jun 9, 2014 2:45:49 PM
 */
public class AccountMonitor implements IMonitor {

	@Override
	public int getMonitorType() {
		return 1;
	}

	ITweetStore _store;

	@Override
	public ITweetStore getStore(AppContext context) {
		if (_store == null) {
			Pooling pool = context.getPooling(this.getPooling(), true);
			AccountManager manager = context.getAccountManager();
			_store = new MAccountStore(this, pool, manager);
			_store.RegHandler(new AccountPostHandler());
			_store.RegHandler(new RealtimeHandler());
		}
		return _store;
	}

	@Override
	public long getTimestamp() {
		return this.timestamp;
	}

	@Override
	public String getToken() {
		return PortalCST.MONITOR_ACCOUNT_PERFIX + this.getMonitorId();
	}

	public void landing(User user) {
		IAccount acc;
		try {
			acc = this.getMAccount(user);
			if (acc == null) {
				return;
			}
			Crawler crawler = Crawler.getInstance();
			List<CrawlTask> tasks = crawler.createTasks(this);
			crawler.submit(tasks);
			this.timestamp = System.currentTimeMillis();
		} catch (ApiException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 通过监测ID获得监测的帐号基本信息
	 * 
	 * @param monitorId
	 * @return
	 * @throws ApiException
	 */
	private IAccount getMAccount(User user) throws ApiException {

		AccountManager manager = AppContext.getInstance().getAccountManager();
		Result<IAccount> acc = manager.accountGetByUcode(this.uid, platform);

		if (acc == null) {
			AppAuth auth = CustomManager.getInstance()
					.getAuthByDefaultStrategy(user, platform);
			Result<IAccount> rst = manager.accountGetByUcodeOnline(uid,
					platform, user, auth);
			if (rst.getErrCode() == 0) {
				return rst.getData();
			}
		}
		return null;
	}

	private long timestamp;
	private int id;
	private int userid; // 用户id
	private String nick; // 昵称
	private String uid; // uid
	private String tail; // 讯库的影子帐号
	private Platform platform; // 平台 1新浪2腾讯5人民
	private int status; // 1=停止、2=暂停、3=运行、4=已删除
	private float original; // 原创率
	private float repostsperday;// 平均每日转发
	private float commentperday;// 平均每日评论
	private String dbserver; // server
	int customId;// 客户编号

	int weibos;
	int fans;
	int friends;

	public int getMonitorId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getUserid() {
		return userid;
	}

	public void setUserid(int userid) {
		this.userid = userid;
	}

	public String getNick() {
		return nick;
	}

	public void setNick(String nick) {
		this.nick = nick;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getTail() {
		return tail;
	}

	public void setTail(String tail) {
		this.tail = tail;
	}

	public Platform getPlatform() {
		return platform;
	}

	public void setPlatform(Platform platform) {
		this.platform = platform;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public float getOriginal() {
		return original;
	}

	public void setOriginal(float original) {
		this.original = original;
	}

	public float getRepostsperday() {
		return repostsperday;
	}

	public void setRepostsperday(float repostsperday) {
		this.repostsperday = repostsperday;
	}

	public float getCommentperday() {
		return commentperday;
	}

	public void setCommentperday(float commentperday) {
		this.commentperday = commentperday;
	}

	public String getPooling() {
		return dbserver;
	}

	public void setDbserver(String dbserver) {
		this.dbserver = dbserver;
	}

	public String getTableName() {
		return PortalCST.MONITOR_ACCOUNT_PERFIX + this.id;
	}

	public void setTable(String table) {

	}

	public int getCustomId() {
		return customId;
	}

	public void setCustomId(int customId) {
		this.customId = customId;
	}

	public int getWeibos() {
		return weibos;
	}

	public void setWeibos(int weibos) {
		this.weibos = weibos;
	}

	public int getFans() {
		return fans;
	}

	public void setFans(int fans) {
		this.fans = fans;
	}

	public int getFriends() {
		return friends;
	}

	public void setFriends(int friends) {
		this.friends = friends;
	}

}
