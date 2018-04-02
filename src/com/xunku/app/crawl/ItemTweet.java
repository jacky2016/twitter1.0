package com.xunku.app.crawl;

import weibo4j.org.json.JSONException;
import weibo4j.org.json.JSONObject;

/**
 * 爬虫的微博结构定义，该结构和讯库的API一致
 * 
 * @author wujian
 * @created on Sep 3, 2014 4:03:05 PM
 */
public class ItemTweet {
	String mid;
	String uid;
	String ucode;
	String uname;
	String domain;
	String domainName;
	String postDate;
	String content;
	int comtCount;
	int relayCount;
	String url;
	String relayContent;
	boolean verify;
	String imageUrl;
	String transferUrl;
	ItemTweet source;
	int Appsource;

	public String getMid() {
		return mid;
	}

	public void setMid(String mid) {
		this.mid = mid;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getUcode() {
		return ucode;
	}

	public void setUcode(String ucode) {
		this.ucode = ucode;
	}

	public String getUname() {
		return uname;
	}

	public void setUname(String uname) {
		this.uname = uname;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public String getDomainName() {
		return domainName;
	}

	public void setDomainName(String domainName) {
		this.domainName = domainName;
	}

	public String getPostDate() {
		return postDate;
	}

	public void setPostDate(String postDate) {
		this.postDate = postDate;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getComtCount() {
		return comtCount;
	}

	public void setComtCount(int comtCount) {
		this.comtCount = comtCount;
	}

	public int getRelayCount() {
		return relayCount;
	}

	public void setRelayCount(int relayCount) {
		this.relayCount = relayCount;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getRelayContent() {
		return relayContent;
	}

	public void setRelayContent(String relayContent) {
		this.relayContent = relayContent;
	}

	public boolean isVerify() {
		return verify;
	}

	public void setVerify(boolean verify) {
		this.verify = verify;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getTransferUrl() {
		return transferUrl;
	}

	public void setTransferUrl(String transferUrl) {
		this.transferUrl = transferUrl;
	}

	public static ItemTweet create(JSONObject obj) throws JSONException {
		ItemTweet tweet = new ItemTweet();

		tweet.setComtCount(obj.getInt("comtCount"));
		tweet.setRelayCount(obj.getInt("relayCount"));
		tweet.setContent(obj.getString("content"));
		tweet.setDomain(obj.getString("domain"));
		tweet.setDomainName(obj.getString("domainName"));
		tweet.setImageUrl(obj.getString("imageUrl"));
		tweet.setMid(obj.getString("mid"));
		tweet.setPostDate(obj.getString("postDate"));
		tweet.setRelayContent(obj.getString("relayContent"));
		tweet.setTransferUrl(obj.getString("transferUrl"));
		tweet.setUcode(obj.getString("ucode"));
		tweet.setUid(obj.getString("uid"));
		tweet.setUname(obj.getString("uname"));
		tweet.setUrl(obj.getString("url"));
		tweet.setVerify(obj.getBoolean("verify"));
		tweet.setAppsource(obj.getInt("appSource"));

		JSONObject jsonSource = obj.getJSONObject("source");
		if (jsonSource != null) {
			ItemTweet source = ItemTweet.create(jsonSource);
			tweet.setSource(source);
		}

		return tweet;

	}

	public ItemTweet getSource() {
		return source;
	}

	public void setSource(ItemTweet source) {
		this.source = source;
	}

	public int getAppsource() {
		return Appsource;
	}

	public void setAppsource(int appsource) {
		Appsource = appsource;
	}
}
