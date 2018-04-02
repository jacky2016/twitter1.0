package com.xunku.app.handlers.spreads;

import java.util.List;

import com.xunku.app.AppContext;
import com.xunku.app.enums.PutResult;
import com.xunku.app.helpers.DbHelper;
import com.xunku.app.interfaces.IAccount;
import com.xunku.app.interfaces.IPostHandler;
import com.xunku.app.interfaces.ITweet;
import com.xunku.app.monitor.WeiboMonitor;
import com.xunku.app.stores.MSpreadStore;
import com.xunku.app.stores.TweetStore;
import com.xunku.dao.monitor.MWeiBoDao;
import com.xunku.daoImpl.monitor.MWeiBoDaoImpl;

/**
 * 用户来源分析
 * 
 * @author wujian
 * @created on Jul 22, 2014 7:58:42 PM
 */
public class SUserXFromHandler implements IPostHandler {

	
	/**
	 * 传播分析 - 用户分析 - 转发/评论来源分析
	 */
	public static final String SQL_SPREAD_USER_FROM = "spreads/spread.users.xfrom.sql";

	
	@Override
	public void initialize(TweetStore db) {

	}

	@Override
	public void processPost(ITweet post, PutResult pr, TweetStore storeDB) {
		if (pr == PutResult.Add) {
			IAccount author = post.getAuthor();
			if (author != null) {
				int fromId = 0;
				if (post.getFrom() != null) {
					fromId = post.getFrom().getId();
				}
				DbHelper.incSpreadStatis("sys_inc_Spread_Users_From_Statis",
						storeDB.getMonitorID(), post.getType(), fromId, storeDB
								.getPool());
			}
		}

	}

	@Override
	public String getName() {
		return this.getClass().getName();
	}

	public static void main(String[] args) {
		AppContext context = AppContext.getInstance();
		context.init();

		MWeiBoDao dao = new MWeiBoDaoImpl();
		WeiboMonitor monitor = dao.queryMWeiboById(184);

		MSpreadStore store = (MSpreadStore) monitor.getStore(context);

		List<ITweet> list = store.queryAll();
		SUserXFromHandler handler = new SUserXFromHandler();

		for (ITweet t : list) {
			handler.processPost(t, PutResult.Add, store);
		}

	}

	@Override
	public void flush(TweetStore db) {
		// TODO Auto-generated method stub
		
	}

}
