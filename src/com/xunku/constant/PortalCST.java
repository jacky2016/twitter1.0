package com.xunku.constant;

/**
 * 门户常量类
 * 
 * @author wujian
 * @created on Jun 5, 2014 3:54:55 PM
 */
@SuppressWarnings("deprecation")
public interface PortalCST {
	public static final String AuthorityMetaCacheKey = "PmiCode";
	public static final String PermissionListCacheKey = "PmiList";// 加载权限列表缓存key
	public static final String PmiIDHtmlCacheKey = "PmiHtml"; // 加载权限Html模板
	public static final String BaseCityCacheKey = "BaseCityList";// 加载地域
	public static final String customCacheKeyString = "CustomKey-{0}";// 客户级缓存
	public static final String Base_CustomConfigCacheKey = "BCConfig";// 配置信息元数据缓存
	public static final String Base_CustomProfileCacheKey = "BCProfile";// 客户配置数据缓存
	public static final String AppSourceCacheKey = "AppSource";// 加载来源信息
	public static final String GetCityCacheKey = "GetCityByCode";// 通过code获取地域
	public static final String SystemModuleCacheKey = "SystemModule";// 加载系统模块
	public static final String GetActionCacheKey = "GetAction"; // 加载所有Action
	/**
	 *  Action 到 Module 的全局Cache
	 */
	public static final String ActionModuleCacheKey = "ACML"; 

	// ***************客户配置参数 begin *****************
	// 微博帐号绑定数
	public static final String weibo_account_binds = "weibo.account.binds";
	// 微博子帐号数
	public static final String weibo_account_childs = "weibo.account.childs";
	// 舆情监测任务数
	public static final String weibo_task_count = "weibo.task.count";
	// 事件监测数
	public static final String weibo_event_count = "weibo.event.count";
	// 传播分析数
	public static final String weibo_disseminate_count = "weibo.disseminate.count";
	// 监测帐号数量
	public static final String weibo_monitor_account_count = "weibo.monitor.account.count";
	// 官V分析(粉丝数量)
	public static final String weibo_mine_follow_count = "weibo.mine.follow.count";
	// 帐号分析(粉丝数量)
	public static final String weibo_account_follow_count = "weibo.account.follow.count";
	// 任务优先级
	public static final String weibo_task_priority = "weibo.task.priority";
	// 事件数据及时刷新
	public static final String weibo_event_refresh = "weibo.event.refresh";
	// 微博预警数
	public static final String weibo_alert_count = "weibo.alert.count";
	// ***************客户配置参数 end *****************

	// for api params
	public static final String UID = "uid";
	/**
	 * 官微微博存储的表名
	 */
	public static final String MONITOR_MY_POST_TABLE = "My_Posts";

	/**
	 * 官微相关微博的存储前缀
	 */
	public static final String MONITOR_OFFICAL_PERFIX = "tweets_custom_";
	/**
	 * 事件监测微博存储表前缀
	 */
	public static final String MONITOR_EVENT_PERFIX = "tweets_event_";
	/**
	 * 帐号监测微博存储表前缀
	 */
	public static final String MONITOR_ACCOUNT_PERFIX = "tweets_account_";
	/**
	 * 微博监测微博存储表前缀
	 */
	public static final String MONITOR_WEIBO_PERFIX = "tweets_weibo_";

	public static final String SUCESS = "sucess";

	/**
	 * 官微应用的ID，目前为1
	 */
	public static final int OFFICAL_APP_ID = 1;

	/**
	 * xk_weibo基本库连接池名称
	 */
	public static final String POOLED_BASE_NAME = "base";
	public static final String POOLED_USER_NAME = "users";
	public static final String POOLED_LOG_NAME = "weibolog";

	/**
	 * mq服务器地址mqservice.address.ip
	 */
	public static final String MQ_SERVER_ADDRESS = "mqservice.address.ip";

	/**
	 * 配置文件名称
	 */
	public static final String CONFIG_FILE_NAME = "config";

	/**
	 * 配置文件里的新浪的主应用ID的Key
	 */
	public static final String SINA_APP_KEY_NAME = "sina_client_id";

	/**
	 * 配置文件里的腾讯的主应用ID的Key
	 */
	public static final String TENC_APP_KEY_NAME = "tencent_client_id";

	/**
	 * 该帐号没有在系统内注册，无法获得微博信息！
	 */
	public static final String ERROR_ACCOUNT_NOT_REG = "该帐号没有在系统内注册，无法获得微博信息！";
	/**
	 * 该帐号没有授权信息，无法获得微博信息！
	 */
	public static final String ERROR_ACCOUNT_NOT_AUTH = "该帐号没有授权信息，无法获得微博信息！";

	/**
	 * 新浪微博地址模式
	 */
	public static final String PATTERN_SINA_URL = "^http://(e\\.)?weibo\\.com/[0-9a-zA-Z]+/[0-9a-zA-Z]+";

	// http://t.people.com.cn/314966/77811717
	public static final String PATTERN_RENMIN_URL = "^http://t.people.com.cn/[0-9]+/[0-9]+";
	/**
	 * 腾讯微博地址模式
	 */
	public static final String PATTERN_TENCENT_URL = "^http://t.qq.com/p/t/[0-9]+";

	public static final String TIME_FIRST_SECOND = "00:00:00";
	public static final String TIME_LAST_SECOND = "23:59:59";

	/**
	 * 微博日期格式yyyy-MM-dd HH:mm
	 */
	public static final String FORMET_WEIBO_TIME = "yyyy-MM-dd HH:mm";

	/**
	 * 讯库搜索支持的日期格式yyyyMMddHHmmss
	 */
	public static final String FORMAT_XUNKU_SEARCH_TIME = "yyyyMMddHHmmss";

	/**
	 * 只取到年月日的日期格式yyyyMMdd
	 */
	public static final String FORMAT_QUARTER_TIME = "yyyyMMdd";
	/**
	 * 密码空token
	 */
	public static final String PASSWORD_TOKEN_EMPTY = "##########";

	/**
	 * base62编码基数
	 */
	public final static String STRING_62_KEYS = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

	/**
	 * 图片显示的前缀
	 */
	public final static String IMAGE_PATH_PERFIX = "http://res.xunku.org/img/a.axd?url=";

	/**
	 * 一月的时间
	 */
	public final static long ONE_MONTH = 30 * 24 * 60 * 1000;

	/**
	 * 未知来源
	 */
	public final static String FORM_UNKNOW = "未知来源";

	public final static String[] EMPTY_REPOST = new String[] { "转发微博", "轉發微博" };

}
