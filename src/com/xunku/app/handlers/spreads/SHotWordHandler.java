package com.xunku.app.handlers.spreads;

import com.xunku.app.enums.PutResult;
import com.xunku.app.interfaces.IPostHandler;
import com.xunku.app.interfaces.ITweet;
import com.xunku.app.stores.TweetStore;

public class SHotWordHandler implements IPostHandler {

	
	/**
	 * 热门关键词【事件用户分析】
	 */
	public static final String SQL_SPREAD_HOTWORD = "spreads/spread.hotword.sql";
	
	@Override
	public void flush(TweetStore db) {
		// TODO Auto-generated method stub

	}

	@Override
	public void initialize(TweetStore db) {
		// TODO Auto-generated method stub

	}

	@Override
	public void processPost(ITweet tweet, PutResult pr, TweetStore db) {
		// TODO Auto-generated method stub

	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

}
