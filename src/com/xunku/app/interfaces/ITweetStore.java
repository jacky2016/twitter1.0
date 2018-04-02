package com.xunku.app.interfaces;

import com.xunku.app.db.AccountManager;
import com.xunku.app.enums.Platform;
import com.xunku.app.enums.PutResult;
import com.xunku.app.model.Pooling;
import com.xunku.app.monitor.IMonitor;

/**
 * 抽象一个微博存储
 * 
 * @author wujian
 * @created on Jul 24, 2014 10:25:11 AM
 */
public interface ITweetStore {

	void init();

	/**
	 * 在这个存储上注册业务处理器
	 * 
	 * @param handler
	 */
	void RegHandler(IPostHandler handler);

	/**
	 * 将handler里面的缓存数据推入存储中
	 */
	void flushHandlers();

	/**
	 * 获得当前存储上的监视对象
	 * 
	 * @return
	 */
	IMonitor getMonitor();

	/**
	 * 获得当前存储上的连接池对象
	 * 
	 * @return
	 */
	Pooling getPool();

	/**
	 * 获得当前存储上关联的用户帐号存储
	 * 
	 * @return
	 */
	AccountManager getAccountManager();

	/**
	 * 入库微博，返回入库的结果
	 * 
	 * @param post
	 * @return
	 */
	PutResult put(ITweet post);

	/**
	 * 根据入库结果来调用业务处理器
	 * 
	 * @param post
	 * @param putResult
	 */
	// void processHandlers(Post post, PutResult putResult);
	ITweet executePostQuery(String tid, Platform platform);
}
