package com.xunku.constant;

/**
 * 定义常量
 * 
 * @author wanghui
 */
public interface QQParamType {
	public static final String FORMAT = "json";// 返回数据的格式（json或xml）
	public static final String PAGEFLAG = "0"; // 分页标识（0：第一页，1：向下翻页，2:向上翻页）
	public static final String PAGETIME = "0";// 本页起始时间（第一页：填0，向上翻页：填上一次请求返回的第一条记录时间，向下翻页：填上一次请求返回的最后一条记录时间）
	public static final String REQNUM = "5";// 每次请求记录的条数（1-20条）
	public static final String LASTID = "0";// 用于翻页，和pagetime配合使用（第一页：填0，向上翻页：填上一次请求返回的第一条记录id，向下翻页：填上一次请求返回的最后一条记录id）
	public static final String CONTENTTYPE = "0";// 内容过滤。0-所有类型，1-带文本，2-带链接，4-带图片，8-带视频，0x10-带音频，0x40-带电话录音(IVR语音微博)，默认为0
	public static final String FOPENIDS = "";// 你需要读取的用户openid列表，用下划线“_”隔开
	public static final String Type = "0";// 拉取类型(需填写十进制数字)0x1：原创发表，0x2：转载，0x8：回复，0x10：空回，0x20：提及，0x40：评论。如需拉取多个类型请使用|，如(0x1|0x2)得到3，则type=3即可，填零表示拉取所有类型。
	public static final String STARTINDEX = "0";// 起始位置（第一页填0，继续向下翻页：填【reqnum*（page-1）】）
	public static final String MODE = "0";// 获取模式，默认为0mode=0，新粉丝在前，只能拉取1000个
											// mode=1，最多可拉取一万粉丝，暂不支持排序
	public static final String INSTALL = "0";// 过滤安装应用好友（可选）0-不考虑该参数，1-获取已安装应用好友，2-获取未安装应用好友
	public static final String FLAG = "2";// 类型标识，0－获取转播计数，1－获取点评计数 2－两者都获取
	public static final String CLIENTIP = "127.0.0.1";// 用户ip（必须正确填写用户侧真实ip，不能为内网ip及以127或255开头的ip，以分析用户所在地）
	public static final String LONGITUDE = "";// 经度，为实数，如113.421234（最多支持10位有效数字，可以填空）
	public static final String LATITUDE = "";// 纬度，为实数，如22.354231（最多支持10位有效数字，可以填空）
	public static final String SYNCFLAG = "";// 微博同步到空间分享标记（可选，0-同步，1-不同步，默认为0），目前仅支持OAuth1.0鉴权方式

}
