package com.xunku.app.handlers.maccounts;

import java.util.HashMap;
import java.util.Map;

import com.xunku.app.enums.PostType;
import com.xunku.app.enums.PutResult;
import com.xunku.app.helpers.DateHelper;
import com.xunku.app.interfaces.IPostHandler;
import com.xunku.app.interfaces.ITweet;
import com.xunku.app.stores.TweetStore;
import com.xunku.dao.account.AccountDao;
import com.xunku.daoImpl.account.AccountDaoImpl;

/**
 * 按天产生的统计数据
 * 
 * @author wujian
 * @created on Sep 25, 2014 9:37:18 AM
 */
public class RealtimeHandler implements IPostHandler {

	public final static String initHandlerSQL = "maccount/account.realtime.sql";

	Map<Long, Integer> weibos;
	Map<Long, Integer> retweets;

	public RealtimeHandler() {
		this.retweets = new HashMap<Long, Integer>();
		this.weibos = new HashMap<Long, Integer>();
	}

	private void inc(long created, Map<Long, Integer> data) {
		long timezone = DateHelper.getTimezoneDay(created);
		if (!data.containsKey(timezone)) {
			data.put(timezone, 0);
		}
		data.put(timezone, data.get(timezone) + 1);
	}

	@Override
	public void flush(TweetStore db) {

		AccountDao dao = new AccountDaoImpl();
		for (Map.Entry<Long, Integer> e : weibos.entrySet()) {
			dao.updateRealtime(db.getPool(), db.getMonitorID(), e.getKey(), e
					.getValue(), 0);
		}

		for (Map.Entry<Long, Integer> e : retweets.entrySet()) {
			dao.updateRealtime(db.getPool(), db.getMonitorID(), e.getKey(), 0,
					e.getValue());
		}

		this.retweets.clear();
		this.weibos.clear();

	}

	@Override
	public void initialize(TweetStore db) {

	}

	@Override
	public void processPost(ITweet tweet, PutResult pr, TweetStore db) {

		if (pr == PutResult.Add) {
			if (tweet.getType() == PostType.Creative) {
				this.inc(tweet.getCreated(), this.weibos);
			} else if (tweet.getType() == PostType.Repost) {
				this.inc(tweet.getCreated(), this.retweets);
			}
		}
	}

	@Override
	public String getName() {
		return this.getClass().getName();
	}

}
