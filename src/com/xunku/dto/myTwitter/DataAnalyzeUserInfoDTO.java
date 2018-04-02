package com.xunku.dto.myTwitter;

import com.xunku.dto.AccountDTO;

/**
 * 我的微博数据分析当前账号信息对象
 * @author sunao
 *
 */
public class DataAnalyzeUserInfoDTO {
	public AccountDTO account=new AccountDTO();
	public int userLiveness; //活跃度
	public int userInfluence; //影响力
	public Boolean isV;//是否为大V用户
}
