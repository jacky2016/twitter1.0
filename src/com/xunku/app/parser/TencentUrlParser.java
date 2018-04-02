package com.xunku.app.parser;

import com.xunku.app.Utility;
import com.xunku.app.enums.Platform;
import com.xunku.app.interfaces.ITweetUrlParser;
import com.xunku.app.model.TweetUrl;

public class TencentUrlParser implements ITweetUrlParser {

	String url;

	public TencentUrlParser(String url) {
		this.url = url;
	}

	@Override
	public TweetUrl parse() {
		if (Utility.isNullOrEmpty(url)) {
			return null;
		}
		if (url.indexOf('?') != -1) {
			url = url.substring(0, url.indexOf('?'));
		}
		int index = url.lastIndexOf('/') + 1;
		TweetUrl result = new TweetUrl(url);
		// 获得微博id
		result.setTid(url.substring(index, url.length()));
		result.setPlatform(Platform.Tencent);
		// 无法获得uid
		
		return result;
	}

	public static void main(String[] args) {

		SinaUrlParser p = new SinaUrlParser(
				"http://t.qq.com/p/t/415157036605338");
		p.parse();

	}

}
