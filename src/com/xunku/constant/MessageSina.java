package com.xunku.constant;

import java.util.HashMap;
import java.util.Map;

import weibo4j.model.WeiboException;

public class MessageSina {

	public static final Map<Integer, String> msg;

	static {
		msg = new HashMap<Integer, String>();

		msg.put(20101, "不存在的微博");
		msg.put(20201, "不存在的微博评论");
		msg.put(20003, "用户不存在");
	}

	public static String getMessage(int errorcode, String error) {
		if (msg.containsKey(errorcode)) {
			return msg.get(errorcode);
		}
		return error;
	}

	public static String getMessage(WeiboException ex) {
		return getMessage(ex.getErrorCode(), ex.getError());
	}
}
