package com.xunku.app.interfaces;

import com.xunku.app.enums.PutResult;
import com.xunku.app.stores.TweetStore;

/**
 * 帖子处理器,当帖子内容发生变更的时候调用该接口
 * 
 * @author wujian
 * @created on Jun 11, 2014 10:46:30 AM
 */
public interface IPostHandler extends IHandler {

	/**
	 * handler初始化方法，该方法在注册Handler之后被调用
	 * 
	 * @param db
	 */
	void initialize(TweetStore db);

	/**
	 * 入库后Post的处理器，Post被插入到了StoreDB里面
	 * 
	 * @param post
	 *            已经入库的Post
	 * @param pr
	 *            入库的Post是以什么方式入库的
	 * @param db
	 *            上面Post入库的存储库
	 */
	void processPost(ITweet tweet, PutResult pr, TweetStore db);

	/**
	 * 将处理器中的数据写入存储中，推入数据后应该将处理器的缓存清空，否则会返回推入数据
	 * 
	 * @param db
	 */
	void flush(TweetStore db);

}
