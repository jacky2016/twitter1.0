package com.xunku.app.monitor;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xunku.app.AppContext;
import com.xunku.app.Fetcher;
import com.xunku.app.db.AccountManager;
import com.xunku.app.enums.Platform;
import com.xunku.app.handlers.AccountPostHandler;
import com.xunku.app.handlers.customs.MentionPostHandler; //import com.xunku.app.handlers.customs.NavieCountHandler;
import com.xunku.app.handlers.customs.NavieCountHandler;
import com.xunku.app.handlers.customs.RepostOrganizationHandler;
import com.xunku.app.interfaces.ITweetStore;
import com.xunku.app.manager.CustomManager;
import com.xunku.app.model.ApiException;
import com.xunku.app.model.AppAccount;
import com.xunku.app.model.AppAuth;
import com.xunku.app.model.Pooling;
import com.xunku.app.stores.MCustomStore;
import com.xunku.constant.PortalCST;
import com.xunku.dao.base.AccountDao;
import com.xunku.dao.base.OrganizationsDao;
import com.xunku.dao.office.NavieDao;
import com.xunku.daoImpl.base.AccountDaoImpl;
import com.xunku.daoImpl.base.OrganizationsDaoImpl;
import com.xunku.daoImpl.office.NavieDaoImpl;
import com.xunku.pojo.base.Custom;
import com.xunku.pojo.base.Organization;
import com.xunku.pojo.office.NavieAccount;

/**
 * 客户监测实体-该监视器保存客户的所有绑定官微的相关微博信息
 * 
 * @author wujian
 * @created on Jul 15, 2014 2:02:26 PM
 */
public class CustomMonitor implements IMonitor {

	private static final Logger LOG = LoggerFactory
			.getLogger(CustomMonitor.class);

	NavieDao ndao = new NavieDaoImpl();
	AccountDao dao = new AccountDaoImpl();

	@Override
	public int getMonitorType() {
		return 2;
	}

	ITweetStore store;

	@Override
	public ITweetStore getStore(AppContext context) {
		if (this.store == null) {
			Pooling pool = context.getPooling(this.getPooling(), true);

			AccountManager manager = AccountManager.getInstance(context);
			store = new MCustomStore(this, pool, manager);
			// 注册博主处理器
			store.RegHandler(new AccountPostHandler());
			// 注册mention处理器
			store.RegHandler(new MentionPostHandler(context, ndao
					.getNavieAccount(this.getMonitorId())));
			// 注册转发机构处理器
			store.RegHandler(new RepostOrganizationHandler(this.getOrgList()));
			
			// 注册网评员处理器
			store.RegHandler(new NavieCountHandler(this.getOrgList()));
		}
		return store;
	}

	public List<Organization> getOrgList() {
		OrganizationsDao oDAO = new OrganizationsDaoImpl();
		return oDAO.queryOrganizationList(this.getMonitorId());
	}

	public List<NavieAccount> getNavieList() {
		return ndao.getNavieAccount(this.getMonitorId());
	}

	@Override
	public String getToken() {
		return PortalCST.MONITOR_OFFICAL_PERFIX + this.getMonitorId();
	}

	@Override
	public long getTimestamp() {
		return this.timestamp;
	}

	public void landing(AccountManager accountManager, MCustomStore store,
			boolean onlyFirstPage) throws ApiException {

		CustomManager manager = CustomManager.getInstance();

		LOG.info("本企业绑定[" + custom.getAccounts().size() + "]个帐号!");

		Fetcher fetcher = new Fetcher();
		for (AppAccount acc : custom.getAccounts()) {
			AppAuth auth = acc.getAuth(manager.getMainApp(acc.getPlatform())
					.getId());

			LOG.info("开始落地[" + acc.getName() + "]的微博列表 -start");
			fetcher.fetchHomePosts(store, auth, onlyFirstPage);
			LOG.info("开始落地[" + acc.getName() + "]的微博列表 -end");

			LOG.info("开始落地[" + acc.getName() + "]的提到我的 -start");
			fetcher.fetchMentions(store, auth, onlyFirstPage);
			LOG.info("开始落地[" + acc.getName() + "]的提到我的 -end");

			LOG.info("开始落地[" + acc.getName() + "]的评论我的/我评论的 -start");
			fetcher.fetchComments(store, auth, onlyFirstPage);
			LOG.info("开始落地[" + acc.getName() + "]的评论我的/我评论的 -end");

			if (!onlyFirstPage) {
				LOG.info("开始落地[" + acc.getName() + "]的粉丝 -start");
				fetcher.fetchFollowerList(auth, acc, accountManager);
				LOG.info("开始落地[" + acc.getName() + "]的粉丝 -end");
			}
		}

		this.timestamp = System.currentTimeMillis();
	}

	long timestamp;
	String dbserver;
	Custom custom;

	public Custom getCustom() {
		return custom;
	}

	public int getCustomId() {
		return this.custom.getId();
	}

	/**
	 * 客户编号
	 */
	@Override
	public int getMonitorId() {
		return this.custom.getId();
	}

	@Override
	public String getPooling() {
		return dbserver;
	}

	@Override
	public String getTableName() {
		return PortalCST.MONITOR_OFFICAL_PERFIX + this.custom.getId();
	}

	public CustomMonitor(Custom custom) {
		this.custom = custom;
		this.dbserver = custom.getDbserver();
	}

	@Override
	public Platform getPlatform() {
		// TODO Auto-generated method stub
		return null;
	}

	public static void main(String[] args) {
		String s = "DE9DF084BC1191C4659BB5110FAB122E";
		System.out.println(s.length());

	}

}
