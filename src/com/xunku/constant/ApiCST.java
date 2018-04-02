package com.xunku.constant;

import java.util.Map;

/**
 * API相关常量定义
 * 
 * @author wujian
 * @created on Jun 26, 2014 6:03:51 PM
 */
public interface ApiCST {
	/**
	 * 单页返回的记录条数，默认为50
	 */
	public static final int COUNT_PERPAGE_DEFAULT = 50;

	/**
	 * 新浪Reports timeline每页获取的最大数量 200
	 */
	public static final int COUNT_REPOST_SINA = 200;

	/**
	 * 腾讯re_list每页返回最大数量 100
	 */
	public static final int COUNT_REPOST_TENCENT = 100;

	/**
	 * 新浪comment timeline每页获取的最大数量 200
	 */
	public static final int COUNT_COMMENT_SINA = 200;

	/**
	 * 调用讯库微博搜索每页返回的数据量 200
	 */
	public static final int COUNT_WEIBO_XUNKU = 200;

	/**
	 * 若指定此参数，则返回ID比since_id大的微博（即比since_id时间晚的微博），默认为0。
	 */
	public static final String PAGEFLAG_SINCE = "since_id";

	/**
	 * 本页起始时间，与pageflag、twitterid共同使用，实现翻页功能
	 * <p>
	 * （第一页：填0，向上翻页：填上一次请求返回的第一条记录时间，向下翻页：填上一次请求返回的最后一条记录时间）
	 */
	public static final String PAGEFLAG_TENCENT_PAGETIME = "pagetime";

	/**
	 * 微博id，与pageflag、pagetime共同使用，实现翻页功能
	 * <p>
	 * （第1页填0，继续向下翻页，填上一次请求返回的最后一条记录id）
	 */
	public static final String PAGEFLAG_TENCENT_TWEETER = "twitterid";

	/**
	 * 返回结果的页码，默认为1
	 */
	public static final int PAGEINDEX_DEFAULT = 1;

	public static final String REPOST_ONLY_SINA = "转发微博";

	/**
	 * 获取当前用户微博(hometimeline)的key
	 */
	public static final String KEY_USERHOMETIMELINE = "post.user.home.timeline";

	/**
	 * 获取当前用户微博(usertimeline)的key
	 */
	public static final String KEY_USERTIMELINE = "post.user.timeline";

	/**
	 * 刷新Mention的key
	 */
	public static final String KEY_MENTION = "post.mention.timeline";

	/**
	 * 评论刷新Key
	 */
	public static final String KEY_COMMENTS = "comments.timeline";

	public static final String KEY_COMMENT_TOME = "comments.to.me";

	public static final String KEY_COMMENT_BYME = "comments.by.me";

	public static final String KEY_MENTION_COMMENT = "mention.comment";
	public static final String KEY_MENTION_TWEET = "mention.tweet";

	/**
	 * 刷新Account的时间key
	 */
	public static final String KEY_ACCOUNT = "user.show";

	/**
	 * 授权过期错误码
	 */
	public static final int ERROR_AUTH_EXPIRED = 408;
	
	
	


}
