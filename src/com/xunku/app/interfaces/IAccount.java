package com.xunku.app.interfaces;

import java.util.List;
import java.util.Map;

import com.xunku.app.enums.GenderEnum;
import com.xunku.app.enums.Platform;
import com.xunku.app.model.location.City;

/**
 * 帐号接口定义
 * 
 * @author wujian
 * @created on Aug 8, 2014 2:21:41 PM
 */
public interface IAccount {

	// ========================== 兼容留下的字段 =========================
	//String getUid();

	// ================================================================

	int getLevel();
	/**
	 * 帐号是否是讯库系统出来的帐号
	 * 
	 * @return
	 */
	boolean isXunku();

	/**
	 * 获得帐号的关键字，新浪是UID，腾讯是Name
	 * 
	 * @return
	 */
	String getUcode();

	/**
	 * 当前帐号的名称
	 * 
	 * @return
	 */
	String getName();

	/**
	 * 当前帐号的显示名
	 * 
	 * @return
	 */
	String getDisplayName();

	/**
	 * 获得帐号所在的平台
	 * 
	 * @return
	 */
	Platform getPlatform();

	/**
	 * 获得当前帐号的URL
	 * 
	 * @return
	 */
	String getHomeUrl();

	/**
	 * 获得发微博的数量
	 * 
	 * @return
	 */
	int getWeibos();

	// int getTweets();

	/**
	 * 获得粉丝数量
	 * 
	 * @return
	 */
	int getFollowers();

	/**
	 * 获得关注数量
	 * 
	 * @return
	 */
	// int getFollowings();
	int getFriends();

	/**
	 * 获得头像的URL
	 * 
	 * @return
	 */
	String getHead();

	/**
	 * 获得大头像地址，腾讯这个地址和head一样
	 * 
	 * @return
	 */
	String getLargeHead();

	/**
	 * 获得帐号的描述信息
	 * 
	 * @return
	 */
	String getDescription();

	/**
	 * 将当前帐号序列化到Map里面去
	 * 
	 * @return
	 */
	Map<String, String> toHashMap();

	/**
	 * 为帐号设置时间戳
	 * 
	 * @param timestamp
	 */
	void setTimestamp(long timestamp);

	/**
	 * 获得当前帐号的时间戳
	 * 
	 * @return
	 */
	long getTimestamp();

	/**
	 * 将帐号序列化为JSON字符串
	 * 
	 * @return
	 */
	String ToJson();

	/**
	 * 当前帐号是否认证
	 * 
	 * @return
	 */
	boolean isVerified();

	/**
	 * 获得性别
	 * 
	 * @return
	 */
	GenderEnum getGender();

	/**
	 * 获得城市id
	 * 
	 * @return
	 */
	City getCity();

	/**
	 * 帐号创建时间
	 * 
	 * @return
	 */
	long getCreated();

	/**
	 * 获得用户的区域
	 * 
	 * @return
	 */
	String getLocation();

	/**
	 * 获得帐号的标签集合
	 * 
	 * @return
	 */
	List<String> getTags();

}
