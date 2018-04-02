package com.xunku.pojo.weibo;

import java.util.List;

/**
 * 转发和评论趋势描述
 * 每个时间区域包含多少作者发布
 * @author wujian
 * @created on Jul 21, 2014 4:06:06 PM
 */
public class RepostTrend {

	long timeZone;//时间区域
	
	List<String> ucodes;//ucode

	public long getTimeZone() {
		return timeZone;
	}

	public void setTimeZone(long timeZone) {
		this.timeZone = timeZone;
	}

	public List<String> getUcodes() {
		return ucodes;
	}

	public void setUcodes(List<String> ucodes) {
		this.ucodes = ucodes;
	}
	
}
