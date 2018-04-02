package com.xunku.app.result;

import java.util.ArrayList;
import java.util.List;

import com.xunku.pojo.base.Post;

/**
 * 微博趋势，给定时间点产生的微博列表
 * 
 * @author wujian
 * @created on Jul 21, 2014 6:54:01 PM
 */
public class TrendTweetResult extends Result {
	/**
	 * 时间区域
	 */
	long _timeZone;
	int _cnt;
	/**
	 * 在这个时间区域内有多少帐号发布了内容
	 */
	List<Post> _tweets = new ArrayList<Post>();

	public TrendTweetResult(long timezone) {
		this._timeZone = timezone;
	}

	public void addTweet(Post acc) {
		this._tweets.add(acc);
	}

	/**
	 * 该时间区域发布数量
	 * 
	 * @return
	 */
	public int getNum() {
		return this._cnt;
	}

	public long getTimeZone(){
		return this._timeZone;
	}
}
