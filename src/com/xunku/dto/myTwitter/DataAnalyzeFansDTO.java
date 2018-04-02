package com.xunku.dto.myTwitter;

import java.util.List;
import java.util.Map;

import com.xunku.app.interfaces.IAccount;
import com.xunku.dto.AccountDTO;
import com.xunku.dto.LineDTO;
import com.xunku.dto.PieDTO;

/**
 * 我的微博数据分析 粉丝模块所有图表
 * 
 * @author sunao
 * 
 */
public class DataAnalyzeFansDTO {
	public PieDTO vip; // 认证比例(饼图)
	public LineDTO fans; // 用户粉丝数(柱状图)
	public PieDTO sex; // 性别比例(饼图)
	public LineDTO location; // 地域(柱状图)
	public List<AccountDTO> supermans; //粉丝最大用户排行(列表)
}
