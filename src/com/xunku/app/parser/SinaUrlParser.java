package com.xunku.app.parser;

import com.xunku.app.Utility;
import com.xunku.app.enums.Platform;
import com.xunku.app.interfaces.ITweetUrlParser;
import com.xunku.app.model.TweetUrl;

/**
 * 新浪url微博分析器
 * 
 * @author wujian
 * @created on Jul 9, 2014 4:04:20 PM
 */
public class SinaUrlParser implements ITweetUrlParser {

	String url;

	public SinaUrlParser(String url) {
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
		// 找到微博的id
		result.setTid(Utility.convertMid2Id(url.substring(index, url.length())));
		result.setPlatform(Platform.Sina);

		int beginIndex = url.lastIndexOf('/', index - 2) + 1;
		int endIndex = index - 1;
		result.setUcode(url.substring(beginIndex, endIndex));
		// 找到用户id
		return result;

	}
	
	public static void main(String[] args){
		
		SinaUrlParser p = new SinaUrlParser("http://weibo.com/1665256992/BcwvQtQC1?ref=home");
		p.parse();
		
	}
}
