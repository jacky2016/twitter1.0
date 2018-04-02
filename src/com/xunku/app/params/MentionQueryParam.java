package com.xunku.app.params;

import com.xunku.app.enums.FilterAccountType;
import com.xunku.app.enums.FilterPostType;
import com.xunku.app.enums.Platform;
import com.xunku.app.enums.PostType;
import com.xunku.pojo.base.Pager;
import com.xunku.pojo.base.User;

/**
 * 提到我的查询参数
 * 
 * @author wujian
 * @created on Jul 25, 2014 5:44:21 PM
 */
public class MentionQueryParam {

	User user;
	String uid;
	Platform platform;
	long start;
	long end;
	String text;
	FilterAccountType accFilter;
	FilterPostType postFilter;
	Pager pager;
	PostType type;

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public Platform getPlatform() {
		return platform;
	}

	public void setPlatform(Platform platform) {
		this.platform = platform;
	}

	public long getStart() {
		return start;
	}

	public void setStart(long start) {
		this.start = start;
	}

	public long getEnd() {
		return end;
	}

	public void setEnd(long end) {
		this.end = end;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public FilterAccountType getAccFilter() {
		return accFilter;
	}

	public void setAccFilter(FilterAccountType accFilter) {
		this.accFilter = accFilter;
	}

	public FilterPostType getPostFilter() {
		return postFilter;
	}

	public void setPostFilter(FilterPostType postFilter) {
		this.postFilter = postFilter;
	}

	public Pager getPager() {
		return pager;
	}

	public void setPager(Pager pager) {
		this.pager = pager;
	}

	public PostType getType() {
		return type;
	}

	public void setType(PostType type) {
		this.type = type;
	}
}
