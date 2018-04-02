package com.xunku.app.handlers.events;

import com.xunku.app.enums.PutResult;
import com.xunku.app.interfaces.IPostHandler;
import com.xunku.app.interfaces.ITweet; //import com.xunku.app.model.Pooling;
import com.xunku.app.stores.TweetStore;

public class EHotWordHandler implements IPostHandler {

	/**
	 * 热门关键词【事件用户分析】
	 */
	public static final String SQL_EVENT_HOTWORD = "event/event.hotword.sql";

	@Override
	public void processPost(ITweet post, PutResult pr, TweetStore storeDB) {
		if (pr == PutResult.Add) {
			// int eventid = storeDB.getMonitorID();
			// Pooling pool = storeDB.getPool();
			// to do it
		}
	}

	@Override
	public String getName() {
		return this.getClass().getName();
	}

	@Override
	public void initialize(TweetStore db) {
		// TODO Auto-generated method stub

	}

	@Override
	public void flush(TweetStore db) {
		// TODO Auto-generated method stub

	}

}
