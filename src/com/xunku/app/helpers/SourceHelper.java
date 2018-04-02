package com.xunku.app.helpers;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xunku.app.model.TweetFrom;
import com.xunku.service.WeiboWebService;

/**
 * 微博来源帮助类
 * 
 * @author wujian
 * @created on Sep 4, 2014 9:51:05 AM
 */
public class SourceHelper {

	static Map<Integer, TweetFrom> idIndex;

	/**
	 * 该索引用来检查当前的名称是否已经在缓存里有，如果缓存里有则说明远程服务器上也有
	 * <p>
	 * 远程服务器上有则不再调用Add方法了
	 */
	static Map<String, Integer> nameIndex;

	private static final Logger LOG = LoggerFactory
			.getLogger(SourceHelper.class);
	static {
		idIndex = new HashMap<Integer, TweetFrom>();
		nameIndex = new HashMap<String, Integer>();
	}

	/**
	 * JSON {"appsource":"","appurl":""} 转换类
	 * 
	 * @author wujian
	 * @created on Sep 4, 2014 10:55:43 AM
	 */
	class XKSource {
		String appsource;
		String appurl;

		public String getAppsource() {
			return appsource;
		}

		public void setAppsource(String appsource) {
			this.appsource = appsource;
		}

		public String getAppurl() {
			return appurl;
		}

		public void setAppurl(String appurl) {
			this.appurl = appurl;
		}
	}

	static String getFromKey(TweetFrom from) {
		return from.getName() + from.getUrl();
	}

	public static TweetFrom getSource(int sourceid) {
		if (!idIndex.containsKey(sourceid)) {
			try {
				String json = WeiboWebService.getWeiboAppSource(sourceid);
				Type type_twitter = new TypeToken<XKSource>() {
				}.getType();
				XKSource source = new Gson().fromJson(json, type_twitter);
				TweetFrom from = new TweetFrom();

				from.setId(sourceid);
				if (sourceid == 0) {
					from.setName("未知来源");
					from.setUrl("http://www.xunku.org/help?src=" + sourceid);
				} else {
					from.setName(source.getAppsource());
					from.setUrl(source.getAppurl());
				}
				idIndex.put(sourceid, from);
				nameIndex.put(getFromKey(from), sourceid);
			} catch (IOException e) {
				LOG.error("无法获得指定源的信息", e);
			}
		}

		if (idIndex.containsKey(sourceid)) {
			return idIndex.get(sourceid);
		}

		return TweetFrom.Empty();
	}

	/**
	 * 添加一个来源，如果缓存里没有则不添加
	 * 
	 * @param domainid
	 *            1、新浪2、腾讯、3人民
	 * @param name
	 *            来源名称
	 * @param url
	 *            来源URL
	 */
	public static TweetFrom addSource(int domainid, String name, String url) {
		try {
			String key = name + url;
			if (!nameIndex.containsKey(key)) {
				int sourceid = WeiboWebService.addWeiboAppSource(String
						.valueOf(domainid), name, url);
				TweetFrom from = new TweetFrom();
				from.setId(sourceid);
				from.setName(name);
				from.setUrl(url);
				idIndex.put(sourceid, from);
				nameIndex.put(getFromKey(from), sourceid);
			}

			// 用名称索引反查结果
			return idIndex.get(nameIndex.get(key));
		} catch (IOException e) {
			LOG.error("添加来源[" + name + "]失败", e);
		}
		return TweetFrom.Empty();
	}
}
