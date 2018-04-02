package com.xunku.utils;

import com.xunku.app.enums.Platform;
import com.xunku.constant.FiltrateEnum;

/**
 * 拼接向搜索引擎传入的参数
 * 
 * @author wanghui
 * 
 */
public class Search_QueryStringUtils {
	/**
	 * 将参数转换成查询串
	 * 
	 * @param queryString
	 *            查询串
	 * @param negValue
	 *            性质(negValue): 0负面 1正面 2全/部
	 * @param newscatalog
	 *            内容分类(newscatalog),用','号分隔
	 * @param sources
	 *            数据源分类,用','号分隔其中分为8种类型： 商业经济(bussinesscatalog),
	 *            地区分类(regioncatalog), 综合(comprehensivecatalog),
	 *            电脑网络(computercatalog), 生活服务(lifecatalog),
	 *            教育文化(educationcatalog), 休闲娱乐(entertainmentcatalog),
	 *            博客论坛(bbscatalog)
	 * @param startTime
	 *            搜索开始时间(daydate:[20111123 TO 20111123])
	 * @param endTime
	 *            搜索结束时间
	 * @param sourcesIds
	 *            如果搜索信息源不包含则要在此处拼接参数(-SourceID)
	 * @param author
	 *            微博作者
	 * @param authorcode
	 *            微博作者编码
	 * @return String 组好的串
	 */
	public static String makeSearchQueryString(String queryString,
			String startTime, String endTime, FiltrateEnum type,
			Platform platform) {
		String result = null;
		StringBuffer sb = new StringBuffer();
		if (queryString != null && queryString.trim().length() > 0) {
			sb.append("(" + queryString + ")");
		}
		// 将时间格式转换
		sb.append(" AND (daydate:[" + startTime + " TO " + endTime + "])");
		switch (type) {
		case All:
			break;
		case Original:
			sb.append(" AND transferurl:\"\"");
			break;
		case Reference:
			sb.append(" AND -transferurl:\"\"");
			break;
		default:
			break;
		}
		switch (platform) {
		case Sina:
			sb.append(" AND SourceID:1");
			break;
		case Tencent:
			sb.append(" AND SourceID:2");
			break;
		case Renmin:
			sb.append(" AND SourceID:5");
			break;
		case UnKnow:
			sb.append(" AND (SourceID:1 OR SourceID:2 OR SourceID:5)");
			break;
		default:
			sb.append(" AND (SourceID:1 OR SourceID:2 OR SourceID:5)");
			break;
		}
		result = sb.toString();
		
		// 如果没有querystring,那么去掉第一个AND
		if (queryString == null || queryString.trim().length() == 0) {
			result = result.substring(result.indexOf("AND") + "AND".length(),
					result.length());
			result = result.trim();
		}
		System.out.println(result);
		return result;
	}

	/**
	 * 简单查询将查询标题拼成queryString
	 * 
	 * @param searchTextType
	 *            文本查询类型
	 * @param title
	 *            标题
	 * @return String queryString
	 */
	public static String makeSearchQueryString(String query, String startTime,
			String endTime, int platform) {
		String result = null;
		StringBuffer sb = new StringBuffer();
		if (query != null && query.trim().length() > 0) {
			// 这里不需要最外面的括号
			// sb.append("("+query+")");
			sb.append(query);
		}
		sb.append(" AND (fetchtime:[" + startTime + " TO " + endTime
				+ "]) AND SourceID:" + platform);
		// 将时间格式转换

		result = sb.toString();
		// 如果没有querystring,那么去掉第一个AND
		if (query == null || query.trim().length() == 0) {
			result = result.substring(result.indexOf("AND") + "AND".length(),
					result.length());
			result = result.trim();
		}
		return result;
	}

	/**
	 * 将高级查询的queryString转换成title
	 * 
	 * @param queryString
	 * @return title
	 */
	public static String makeTitle(String queryString) {
		StringBuffer sb = new StringBuffer();
		int a1 = queryString.indexOf("(") + 1;
		int a2 = queryString.indexOf(")", a1) + 1;
		int a3 = queryString.indexOf("(", a2) + 1;
		int a4 = queryString.indexOf(")", a3) + 1;
		int a5 = queryString.indexOf("(", a4) + 1;
		int a6 = queryString.indexOf(")", a5) + 1;
		String str1 = null, str2 = null, str3 = null;
		if (a1 > 0 && a2 > 0) {
			str1 = queryString.substring(a1, a2 - 1);
			int start = str1.indexOf("\"") + 1;
			str1 = str1.substring(start, str1.indexOf("\"", start));
		}
		if (a3 > 0 && a4 > 0) {
			str2 = queryString.substring(a3, a4 - 1);
			int start = str2.indexOf("\"") + 1;
			str2 = str2.substring(start, str2.indexOf("\"", start));
		}
		if (a5 > 0 && a6 > 0) {
			str3 = queryString.substring(a5, a6 - 1);
			int start = str3.indexOf("\"") + 1;
			str3 = str3.substring(start, str3.indexOf("\"", start));
		}
		if (str1 != null) {
			sb.append(str1 + ";");
		}
		if (str2 != null) {
			sb.append(str2 + ";");
		}
		if (str3 != null) {
			sb.append(str3 + ";");
		}
		return sb.toString();
	}
}
