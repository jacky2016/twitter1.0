package com.xunku.app.parser;

import com.xunku.app.Utility;
import com.xunku.app.enums.Platform;
import com.xunku.app.interfaces.ITweetUrlParser;
import com.xunku.app.model.TweetUrl;

public class RenminUrlParser implements ITweetUrlParser {

	String url;

	public RenminUrlParser(String _url) {
		this.url = _url;
	}

	@Override
	public TweetUrl parse() {
		if (Utility.isNullOrEmpty(url)) {
			return null;
		}
		if (url.indexOf('?') != -1) {
			url = url.substring(0, url.indexOf('?'));
		}

		String part = url.replace("http://t.people.com.cn/", "");
		TweetUrl result = new TweetUrl(url);
		// 获得微博id
		String[] ps = part.split("/");
		result.setTid(ps[1]);
		result.setUcode(ps[0]);
		result.setPlatform(Platform.Renmin);
		return result;
	}

}
