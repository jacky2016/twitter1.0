package com.xunku.app.parser;

import java.util.regex.Pattern;

import com.xunku.app.interfaces.ITweetUrlParser;
import com.xunku.app.model.TweetUrl;
import com.xunku.constant.PortalCST;

public class TweetUrlParserFactory {

	private TweetUrlParserFactory() {
	}

	private static String normalizeUrl(String url) {
		int index = url.indexOf('?');

		if (index != -1) {
			url = url.substring(0, index);
		}

		index = url.indexOf('#');
		if (index != -1) {
			url = url.substring(0, index);
		}
		return url;
	}

	private static ITweetUrlParser createParser(String url) {

		url = normalizeUrl(url);
		Pattern regSina = Pattern.compile(PortalCST.PATTERN_SINA_URL,
				Pattern.CASE_INSENSITIVE);
		Pattern regTencent = Pattern.compile(PortalCST.PATTERN_TENCENT_URL,
				Pattern.CASE_INSENSITIVE);
		Pattern regRenmin = Pattern.compile(PortalCST.PATTERN_RENMIN_URL,
				Pattern.CASE_INSENSITIVE);

		if (regSina.matcher(url).find()) {
			return new SinaUrlParser(url);
		}

		if (regTencent.matcher(url).find()) {
			return new TencentUrlParser(url);
		}
		
		if(regRenmin.matcher(url).find()){
			return new RenminUrlParser(url);
		}
		return new DefaultUrlParser();
	}

	public static TweetUrl createTweetUrl(String url) {
		ITweetUrlParser parse = createParser(url);
		return parse.parse();

	}

	public static void main(String[] args) {
		//^http://t.people.com.cn/[0-9]+/[0-9]+
		//new TweetUrlParserFactory().createParser("http://e.weibo.com/1665256992/BcwvQtQC1?ref=home");
		//new TweetUrlParserFactory().createParser("http://e.weibo.com/1665256992/BcwvQtQC1##?");
	}
}
