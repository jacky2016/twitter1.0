package com.xunku.utils;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.xunku.app.Utility;
import com.xunku.app.enums.Platform;
import com.xunku.app.helpers.SourceHelper;
import com.xunku.constant.MessageRank;
import com.xunku.constant.MessageStatus;
import com.xunku.dto.task.TaskTwitterVO;

/**
 * 项目中使用的string串的特殊处理
 * 
 * @author wanghui
 * 
 */
public class StringUtils {
	/**
	 * 功能描述<解析引用原微博>
	 * 
	 * @author wanghui
	 * @param void
	 * @return TaskRefcontent
	 * @version twitter 1.0
	 * @throws IOException
	 * @throws NumberFormatException
	 * @date Jul 1, 201411:44:43 AM
	 */
	public static TaskTwitterVO parseRefcontent(String refcontent,
			String ucode, String url, Platform platform)
			throws NumberFormatException, IOException {
		if (Utility.isNullOrEmpty(refcontent)) {
			return null;
		}
		TaskTwitterVO ttvo = new TaskTwitterVO();
		// String refcontent = vo.getRefcontent();
		if (refcontent == null || refcontent.length() == 0) {
			return null;
		} else {
			refcontent = refcontent.replace("<span class=\"highlight\">", "")
					.replace("</span>", "");
		}
		String repostComment = "[\\u4e00-\\u9fa5]{2}\\(([^\\)]*)\\)\\|[\\u4e00-\\u9fa5]{2}\\(([^\\)]*)\\)";
		String publishdateReg = "[0-9]{4}-[0-9]{2}-[0-9]{2} [0-9]{2}:[0-9]{2}";
		// 来源
		String appsource = "";
		if (refcontent.contains(" 来自")) {
			appsource = refcontent.substring(refcontent.lastIndexOf(" 来自"),
					refcontent.length()).trim();
		}
		ttvo.setAppsource(SourceHelper.addSource(Utility.getPlatform(platform), appsource, "")
				.getId());
		// 评论和转发
		int comment = 0;
		int reposts = 0;
		Pattern rcp = Pattern.compile(repostComment);
		Matcher rcm = rcp.matcher(refcontent);
		if (rcm.find()) {
			String rctext = rcm.group();
			String rtext = rctext.substring(0, rctext.indexOf("|"));
			String ctext = rctext.substring(rctext.indexOf("|"));
			rtext = rtext.substring(rtext.indexOf("(") + 1, rtext.indexOf(")"));
			ctext = ctext.substring(ctext.indexOf("(") + 1, ctext.indexOf(")"));
			if (rtext != "" | rtext.length() != 0) {
				reposts = Integer.valueOf(rtext);
			}
			if (ctext != "" | ctext.length() != 0) {
				comment = Integer.valueOf(ctext);
			}
		}
		ttvo.setReplycount(reposts);
		ttvo.setComtcount(comment);
		// 发布时间
		String publishdate = "";
		Pattern pp = Pattern.compile(publishdateReg);
		Matcher pm = pp.matcher(refcontent);
		if (pm.find()) {
			publishdate = pm.group();
		}
		ttvo.setPublishdate(publishdate);
		// 正文
		String content = "";
		if (publishdate == "" || publishdate.length() == 0) {
			content = refcontent.substring(refcontent.indexOf(":") + 1,
					refcontent.length()).trim();
		} else {
			content = refcontent.substring(refcontent.indexOf(":") + 1,
					refcontent.indexOf(publishdate)).trim();
		}
		ttvo.setContent(content);
		// 作者
		String author = refcontent.substring(1, refcontent.indexOf(":"));
		ttvo.setAuthor(author);
		ttvo.setAuthorcode(ucode);
		// ttvo.setContentid(vo.getContentid());
		// ttvo.setNumofsameContent(vo.getNumofsameContent());
		// ttvo.setRefcontent(vo.getRefcontent());
		// ttvo.setSentiment(vo.getSentiment());
		ttvo.setUrl(url);
		// ttvo.setImgurl(vo.getImgurl());
		ttvo.setSourceID(Utility.getPlatform(platform));
		ttvo.setTransferurl(null);
		return ttvo;
	}

	public static void main(String[] args) throws NumberFormatException, IOException {
		// String
		// refcontent="@广东南海金控:#企业动态#为支持金融高新区环境提升工程建设，确保建设资金如期到位，我司积极与多家金融机构沟通，力增获取最优惠的融资方案。今天上午，渤海银行佛山分行及广州审批中心负责人到我司实地了解项目情况，参观了承业大厦、承创大厦、承展大厦及拟抵押地块，加速落实融资方案。
		// 2014-07-08 12:02:09 转发(15)|评论(16) 来自360安全浏览器";
		String refcontent="明星粉丝团:明星粉丝团: 【柯震东二度转学 仍有工作邀约】柯震东吸毒风波后动向受关注，之前媒体曝他从文化大学体育系转学，在东南科大已经念了一学期资管，而后有同学爆料，称柯震东一学期只上过一次课，期末考都只出现了10分钟。如今开学两周仍未见柯踪影，工作人员透露因不堪其扰，柯震东已再度转学。#八卦# 2014-09-29 00:58:03 全部转播和评论(12)";
				
		TaskTwitterVO vo= StringUtils.parseRefcontent(refcontent, "ucode", "url", Platform.Tencent);
		System.out.println(vo);
	}

	/**
	 * 功能描述<任务通知状态>
	 * 
	 * @author wanghui
	 * @param void
	 * @return String
	 * @version twitter 1.0
	 * @date Apr 28, 201411:30:55 AM
	 */
	public static String messageStatus(int status) {
		String result = null;
		switch (status) {
		case 0:
			result = MessageStatus.NOREAD;
			break;
		case 1:
			result = MessageStatus.READ;
			break;
		default:
			result = MessageStatus.NOREAD;
			break;
		}
		return result;
	}

	/**
	 * 功能描述<任务通知优先级>
	 * 
	 * @author wanghui
	 * @param void
	 * @return String
	 * @version twitter 1.0
	 * @date Apr 23, 20145:27:22 PM
	 */
	public static String messageRanK(int rank) {
		String result = null;
		switch (rank) {
		case 0:
			result = MessageRank.LOW;
			break;
		case 1:
			result = MessageRank.MIDDLE;
			break;
		case 2:
			result = MessageRank.HIGHT;
			break;
		case 3:
			result = MessageRank.URGENCY;
			break;
		default:
			result = MessageRank.LOW;
			break;
		}
		return result;
	}

	/**
	 * 将int型数组转换成中间带有,号的string串
	 * 
	 * @param arr
	 *            int数组
	 * @return 带有,号的String串
	 */
	public static String arrayToString(int[] arr) {
		StringBuffer sb = new StringBuffer();
		for (int i : arr) {
			sb.append(String.valueOf(i)).append(",");
		}
		sb.deleteCharAt(sb.length() - 1);
		return sb.toString();
	}

	/**
	 * 根据要求长度，将搜索引擎返回的内容截取
	 * 
	 * @param content
	 *            搜索引擎返回的内容
	 * @param str_length
	 *            要求的长度
	 * @return 截取后的字符串
	 */
	public static String subContent(String content, int str_length) {
		// 如果传入字符串小于或者等于定义长度，则直接返回
		if (str_length >= content.length()) {
			return content;
		}
		// 定义需要截取的开始标签
		String begin_label = "<span class=\"highlight\">";
		// 定义需要截取的结束标签
		String end_label = "</span>";
		// 定义开始标签的长度
		int begin_label_length = "<span class=\"highlight\">".length();
		// 定义结束标签的长度
		int end_label_length = "</span>".length();
		// 根据要求长度截取的字符串
		String beginStr = content.substring(0, str_length);
		// 根据要求长度截取后的字符串
		String endStr = content.substring(str_length, content.length());
		// 根据要求长度截取前的字符串中获得一下"<span class=\"highlight\">"的位置
		int begin_span = content.indexOf(begin_label, str_length
				- begin_label_length);
		// 定义返回参数
		String result = null;
		// 获取开始标签在要求长度截取的字符串中的位置
		int beginLength = beginStr.lastIndexOf(begin_label);
		// 获取结束标签在要求长度截取的字符串中的位置
		int endLength = beginStr.lastIndexOf(end_label);
		// 如果长度小于程序限定的长度，那么将字符串截取的标签前
		if (begin_span > 0 && begin_span < str_length) {
			result = content.substring(0, begin_span);
			// 如果开始标签的位置大于结束标签的位置那么获得剩下的结束标签
		} else if (beginLength > endLength) {
			// 根据要求长度截取后的字符串中获得一下"</span>"
			int end_span = endStr.indexOf(end_label_length);
			// 如果长度大于0说明在剩下的字符串中此标签是完整的，直接截取到标签结束后将剩下的字符串拼接到之前截取的字符串中，
			// 否则在原有的字符串基础之上重新截取，截取的长度按照要求的数加上"</span>"标签的长度后根据最后一个"</span>"的长度来截取
			if (end_span > 0) {
				String authorStr = endStr.substring(0, end_span
						+ end_label_length);
				result = beginLength + authorStr;
			} else {
				beginStr = content.substring(0, str_length + end_label_length);
				result = beginStr.substring(0, beginStr.lastIndexOf(end_label)
						+ end_label_length);
			}
			// 直接返回截取串
		} else {
			result = beginStr;
		}
		return result;
	}

	/**
	 * 功能: 把文本编码为Html代码,由于函数返回
	 * 
	 * @param string
	 *            需要进行转换的string
	 * @return 转换后的String
	 */
	public static String htmEncode(String s) {
		StringBuffer stringbuffer = new StringBuffer();
		int j = s.length();
		for (int i = 0; i < j; i++) {
			char c = s.charAt(i);
			switch (c) {
			case 60:
				stringbuffer.append("&lt;");
				break;
			case 62:
				stringbuffer.append("&gt;");
				break;
			case 38:
				stringbuffer.append("&amp;");
				break;
			case 34:
				stringbuffer.append("&quot;");
				break;
			case 169:
				stringbuffer.append("&copy;");
				break;
			case 174:
				stringbuffer.append("&reg;");
				break;
			case 165:
				stringbuffer.append("&yen;");
				break;
			case 8364:
				stringbuffer.append("&euro;");
				break;
			case 8482:
				stringbuffer.append("&#153;");
				break;
			case 13:
				if (i < j - 1 && s.charAt(i + 1) == 10) {
					stringbuffer.append("<br>");
					i++;
				}
				break;
			case 32:
				if (i < j - 1 && s.charAt(i + 1) == ' ') {
					stringbuffer.append(" &nbsp;");
					i++;
					break;
				}
			default:
				stringbuffer.append(c);
				break;
			}
		}
		return new String(stringbuffer.toString());
	}

	/**
	 * 功能描述<搜索时间刻钟>
	 * 
	 * @author wanghui
	 * @param void
	 * @return String
	 * @version twitter 1.0
	 * @date Jul 1, 20142:48:16 PM
	 */
	public static String getQuarter(String strPublishDate) {
		int hourofdate = Integer.parseInt(strPublishDate.substring(8, 10));
		int minutes = Integer.parseInt(strPublishDate.substring(10, 12));
		int quarter = hourofdate * 4;
		String quarterstr = strPublishDate.substring(0, 8);
		if (minutes < 15) {
			quarter += 1;
		} else if (minutes < 30) {
			quarter += 2;
		} else if (minutes < 45) {
			quarter += 3;
		} else {
			quarter += 4;
		}
		if (quarter > 96) {
			quarter = 96;
		}
		if (quarter < 10) {
			quarterstr = quarterstr + "0" + quarter;
		} else {
			quarterstr = quarterstr + quarter;
		}
		return quarterstr;
	}

}
