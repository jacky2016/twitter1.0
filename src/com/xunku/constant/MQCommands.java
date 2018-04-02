package com.xunku.constant;

/**
 * MQ 命令常量定义
 * @author wujian
 * @created on Jun 26, 2014 5:44:40 PM
 */
public interface MQCommands {
	/**
	 * 帐号消息队列名称
	 */
	public static final String NAME_ACCOUNT = "Queue_Account_Channel";
	
	/**
	 * 帐号粉丝消息队列
	 */
	public static final String NAME_FOLLOWER = "Queue_Followers_Channel";
	/**
	 * 事件消息队列名称
	 */
	public static final String NAME_EVENT = "Queue_Event_Channel";

	/**
	 * 传播分析（微博分析）消息队列，该队列负责抓取微博的转发和评论内容
	 */
	public static final String NAME_TWEET = "Queue_Tweet_Channel";

	/**
	 * 增加命令
	 */
	public static final String SQL_ADD = "+:";

	/**
	 * 删除命令
	 */
	public static final String SQL_REMOVE = "-:";

	/**
	 * 抓取转发命令
	 */
	public static final String FETCH_TWEET_RETWEET = "fetch_retweet:";
	/**
	 * 抓取评论命令
	 */
	public static final String FETCH_TWEET_COMMENT = "fetch_comment:";
	
	/**
	 * 抓取事件命令
	 */
	public static final String FETCH_EVENT_DATA = "fetch_event:";
	
}
