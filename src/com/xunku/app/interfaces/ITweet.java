package com.xunku.app.interfaces;

import java.util.List;

import com.xunku.app.enums.Platform;
import com.xunku.app.enums.PostType;
import com.xunku.app.model.TweetFrom;

/**
 * 微博对象接口定义
 * 
 * @author wujian
 * @created on Aug 8, 2014 3:11:21 PM
 */
public interface ITweet {

	long getId();

	String getUid();

	/**
	 * 获得这个微博创建的人的关键字
	 * 
	 * @return
	 */
	String getUcode();

	/**
	 * 获得这个微博的平台
	 * 
	 * @return
	 */
	Platform getPlatform();

	/**
	 * 获得微博的文本内容
	 * 
	 * @return
	 */
	String getText();

	/**
	 * 如果有作者获得微博的作者
	 * 
	 * @return
	 */
	IAccount getAuthor();

	void setAuthor(IAccount acc);
	void setStore(ITweetStore store);

	/**
	 * 返回当前微博/评论引用的对象
	 * 
	 * @return
	 */
	ITweet getSource();

	void setSource(ITweet source);


	/**
	 * 获得原始微博的ID
	 * <p>
	 * 如果是转发，则和source的tid一致
	 * <p>
	 * 如果是评论，则和source的tid一致，如果是评论的评论则指向原始微博，source的tid指向评论
	 * 
	 * @return
	 */
	ITweet getOriginalTweet();

	/**
	 * 获得当前微博的ID
	 * 
	 * @return
	 */
	String getTid();

	/**
	 * 获得帖子的创建时间
	 * 
	 * @return
	 */
	long getCreated();

	/**
	 * 获得帖子类型
	 * 
	 * @return
	 */
	PostType getType();

	/**
	 * 帖子里面的图片列表
	 * 
	 * @return
	 */
	List<String> getImages();

	/**
	 * 获得转发数
	 * 
	 * @return
	 */
	int getReposts();

	/**
	 * 
	 * @return
	 */
	int getComments();

	/**
	 * 微博的层级，如果是原创为0，转发则通过text获得转发层级
	 * 
	 * @return
	 */
	int getLayer();

	/**
	 * 获得来源，描述这个微博是从哪里发布的（Iphone或者某个应用）
	 * 
	 * @return
	 */
	TweetFrom getFrom();

	/**
	 * 获得当前微博的地址
	 * 
	 * @return
	 */
	String getUrl();

	/**
	 * 获得当前微博的分类信息
	 * 
	 * @return
	 */
	String getHomeTimeline();
	
	String toJSON();
}
