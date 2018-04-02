package com.xunku.app.result.event;

import java.util.Map;

import com.xunku.app.interfaces.ITweet;
import com.xunku.app.result.Result;
import com.xunku.app.result.RetweetResult;

/**
 * 
 * 事件内容分析结果
 * 
 * @author wujian
 * @created on Jul 3, 2014 9:52:14 AM
 */
public class MEventContentResult extends Result {

	RetweetResult retweetCnt;

	Map<ITweet, Integer> superRetweet;

	Map<String, Float> hotwords;

	public RetweetResult getRetweetCnt() {
		return retweetCnt;
	}

	public void setRetweetCnt(RetweetResult retweetCnt) {
		this.retweetCnt = retweetCnt;
	}

	public Map<ITweet, Integer> getSuperRetweet() {
		return superRetweet;
	}

	public void setSuperRetweet(Map<ITweet, Integer> superRetweet) {
		this.superRetweet = superRetweet;
	}

	public Map<String, Float> getHotwords() {
		return hotwords;
	}

	public void setHotwords(Map<String, Float> hotwords) {
		this.hotwords = hotwords;
	}

}
