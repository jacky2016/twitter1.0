package com.xunku.pojo.base;

import java.util.ArrayList;
import java.util.List;

import com.xunku.app.Utility;
import com.xunku.app.enums.Platform;
import com.xunku.app.enums.PostType;
import com.xunku.app.interfaces.IAccount;
import com.xunku.app.interfaces.ITweet;
import com.xunku.app.interfaces.ITweetStore;
import com.xunku.app.model.TweetFrom;
import com.xunku.app.result.Result;
import com.xunku.dto.TweetDTO;

/**
 * 微博结构定义
 * 
 * @author wujian
 * @created on Jun 5, 2014 3:56:02 PM
 */
public class Post implements ITweet {

	long id;
	Platform platform;
	TweetFrom from;// 来源，此值由穆德宝API提供
	String tid;
	PostType type;
	String text;
	long created;//dateadd(s, convert(bigint, created) / 1000, convert(datetime, '1-1-1970 00:00:00'))
	// String uid;
	String ucode;
	int reposts;
	int comments;
	ITweet source;// 源微博
	String contentid;
	List<String> images;
	String url;
	int layer;// 该微博的层级，原创的级别都是0，默认也是0
	List<Post> repostList = new ArrayList<Post>();// 最新的转发列表
	List<Post> commentList = new ArrayList<Post>();// 最新的评论列表

	String sourceId;
	boolean fromXK;// 这个post是否来自xunku

	IAccount author;
	String _homeTimeline;// 微博所属哪个人的Hometimeline

	public Post() {
		this.images = new ArrayList<String>();
	}

	ITweetStore _store;

	public void setStore(ITweetStore store) {
		this._store = store;
	}

	/**
	 * 获得当前微博的作者信息
	 * 
	 * @return
	 */
	public IAccount getAuthor() {
		if (this.author == null && !Utility.isNullOrEmpty(this.ucode)
				&& _store != null) {
			Result<IAccount> acc = this._store.getAccountManager()
					.accountGetByUcode(this.ucode, this.platform);
			if (acc.getErrCode() == 0) {
				this.author = acc.getData();
			}
		}

		return this.author;
	}

	/**
	 * 设置当前帖子的作者信息，同时设置该对象的uid/ucode/url信息
	 * 
	 * @param acc
	 */
	public void setAuthor(IAccount acc) {
		this.author = acc;
		if (acc != null) {
			this.ucode = acc.getUcode();
		}
	}

	boolean st = true;

	public ITweet getSource() {
		if (st && !Utility.isNullOrEmpty(this.sourceId) && this._store != null) {
			source = this._store.executePostQuery(this.sourceId, this.platform);
			st = false;
		}
		return source;
	}

	public void setSource(ITweet source) {
		this.source = source;
	}

	boolean ot = true;

	public ITweet getOriginalTweet() {
		if (ot && !Utility.isNullOrEmpty(this.origTweetId)
				&& this._store != null) {
			this.originalTweet = this._store.executePostQuery(this.origTweetId,
					this.platform);
			ot = false;
		}
		return this.originalTweet;
	}

	public void setOriginalTweet(ITweet tweet) {
		this.originalTweet = tweet;
	}

	/**
	 * 复制一个帖子信息
	 * 
	 * @param src
	 */
	public void copy(Post src) {
		this.setAuthor(src.author);
		this.comments = src.comments;
		this.reposts = src.reposts;
		this.created = src.created;
		this.from = src.from;
		this.images = src.images;
		this.layer = src.layer;
		this.source = src.source;
		this.text = src.text;
		this.tid = src.tid;
		this.type = src.type;
	}

	/**
	 * 获得当前微博最近的转发微博列表
	 * 
	 * 
	 * @return
	 */
	public List<Post> getRepostList() {
		return this.repostList;
	}

	public List<Post> getCommentList() {
		return this.commentList;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Platform getPlatform() {
		return platform;
	}

	public void setPlatform(Platform platform) {
		this.platform = platform;
	}

	public void setHomeTimeline(String timeline) {
		this._homeTimeline = timeline;
	}

	public String getHomeTimeline() {
		return this._homeTimeline;
	}

	public String getTid() {
		return tid;
	}

	public void setTid(String tid) {
		this.tid = tid;
	}

	public PostType getType() {
		return type;
	}

	public void setType(PostType type) {
		this.type = type;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public long getCreated() {
		return created;
	}

	public void setCreated(long created) {
		this.created = created;
	}

	public TweetFrom getFrom() {
		return from;
	}

	public void setFrom(TweetFrom from) {
		this.from = from;
	}

	public String getUcode() {
		return ucode;
	}

	public void setUcode(String ucode) {
		this.ucode = ucode;
	}

	public int getReposts() {
		return reposts;
	}

	public void setReposts(int reposts) {
		this.reposts = reposts;
	}

	public int getComments() {
		return comments;
	}

	public void setComments(int comments) {
		this.comments = comments;
	}

	public String getContentid() {
		return contentid;
	}

	public void setContentid(String contentid) {
		this.contentid = contentid;
	}

	public List<String> getImages() {
		return images;
	}

	public void setImages(List<String> images) {
		this.images = images;
	}

	public String getUrl() {
		if (Utility.isNullOrEmpty(url)) {
			url = Utility.getPostUrl(this);
		}
		return url;
	}

	public int getLayer() {
		return layer;
	}

	public void setLayer(int layer) {
		this.layer = layer;
	}

	public String toString() {
		StringBuilder buf = new StringBuilder();
		buf.append("Platform: " + this.platform + "\n");
		buf.append("TID: " + this.tid + "\n");
		buf.append("Type: " + this.type + "\n");
		buf.append("Text: " + this.text + "\n");
		buf.append("Url: " + this.url + "\n");
		buf.append("Comments: " + this.comments + "\n");
		buf.append("Reposts: " + this.reposts + "\n");
		buf.append("Layer: " + this.layer + "\n");
		buf.append("Source: " + this.source + "\n");
		// buf.append("Author:" + this.author);
		return buf.toString();
	}

	ITweet originalTweet;
	String origTweetId;

	/**
	 * 设置原始微博的ID
	 * 
	 * @param id
	 */
	public void setOrgTweetId(String id) {
		this.origTweetId = id;
	}

	/**
	 * 设置引用对象的ID
	 * 
	 * @param sourceId
	 */
	public void setSourceId(String sourceId) {
		this.sourceId = sourceId;
	}

	// modify by wanghui
	public String getSourceId() {
		return sourceId;
	}

	public boolean isfromXK() {
		return fromXK;
	}

	public void setfromXK(boolean xk) {
		fromXK = xk;
	}

	@Override
	public String getUid() {
		return this.ucode;
	}

	@Override
	public String toJSON() {
		TweetDTO dto = new TweetDTO(this);

		if (this.getSource() != null) {
			dto.source = new TweetDTO(this.getSource());
		} else {
			dto.source = null;
		}

		return Utility.toJSON(dto);
	}
}
