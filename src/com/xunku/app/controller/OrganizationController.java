package com.xunku.app.controller;

import java.util.Date;

import com.xunku.app.AppContext;
import com.xunku.app.Utility;
import com.xunku.app.enums.PostType;
import com.xunku.app.interfaces.ITweet;
import com.xunku.app.model.Query;
import com.xunku.app.monitor.CustomMonitor;
import com.xunku.app.stores.MCustomStore;
import com.xunku.dao.base.OrganizationsDao;
import com.xunku.daoImpl.base.OrganizationsDaoImpl;
import com.xunku.pojo.base.Organization;
import com.xunku.pojo.base.Pager;
import com.xunku.utils.Pagefile;

public class OrganizationController {

	/**
	 * 获得组织机构列表
	 * 
	 * @param context
	 * @param customid
	 * @param pager
	 * @param start
	 * @param end
	 * @param uid
	 * @return
	 */
	public Pagefile<Organization> queryOrgList(AppContext context,
			int customid, Pager pager, Date start, Date end, String uid) {

		// 先查组织机构库
		OrganizationsDao dao = new OrganizationsDaoImpl();

		Pagefile<Organization> orgList = dao.queryOrganizationList(customid,
				pager);

		// 再查询统计库
		CustomMonitor monitor = context.getCustomManager().getCustom(customid)
				.getMonitor();

		MCustomStore store = (MCustomStore) monitor.getStore(context);

		for (Organization org : orgList.getRows()) {
			int[] tmp = store.queryOrganization(org.getUid(), start, end, uid);
			org.setRetweets(tmp[0]);
			org.setComments(tmp[1]);
		}

		return orgList;
	}

	/**
	 * 获得组织机构明细
	 * 
	 * @param context
	 * @param customid
	 * @param pager
	 * @param start
	 * @param end
	 * @param orgUid
	 * @return
	 */
	public Pagefile<ITweet> queryOrgDetail(AppContext context, int customid,
			Pager pager, Date start, Date end, String orgUid, PostType type) {

		CustomMonitor monitor = context.getCustomManager().getCustom(customid)
				.getMonitor();

		MCustomStore store = (MCustomStore) monitor.getStore(context);

		Query query = new Query();
		query.setFields("*");
		query.setTableName(store.getMonitor().getTableName());
		query.setPageIndex(pager.getPageIndex());
		query.setPageSize(pager.getPageSize());
		query.setWhere(" where ucode = '" + orgUid + "' and created<="
				+ end.getTime() + " and created>=" + start.getTime()
				+ " and type =" + Utility.getPostType(type));
		query.setSortField("created");
		query.setAsc(true);

		return store.executePostsQueryWithPager(query);

	}

}
