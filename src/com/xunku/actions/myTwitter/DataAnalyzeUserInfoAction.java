package com.xunku.actions.myTwitter;

import java.util.List;
import java.util.Map;

import com.xunku.actions.ActionBase;
import com.xunku.app.Utility;
import com.xunku.app.enums.Platform;
import com.xunku.app.interfaces.IAccount;
import com.xunku.app.result.Result;
import com.xunku.dao.base.AccountDao;
import com.xunku.daoImpl.base.AccountDaoImpl;
import com.xunku.dto.AccountDTO;
import com.xunku.dto.myTwitter.DataAnalyzeUserInfoDTO;
import com.xunku.pojo.base.Account;
import com.xunku.pojo.base.AccountInfo;
import com.xunku.utils.AppServerProxy;

/*
 * 获取传播分析模块的用户信息
 * @author sunao
 */
public class DataAnalyzeUserInfoAction extends ActionBase {
	// dataAnalyze.userInfo
	AccountDao weiboAccountDao = new AccountDaoImpl();

	@Override
	public DataAnalyzeUserInfoDTO doAction() {
		// TODO Auto-generated method stub

		// modify by wujian
		String accountid = this.get("accountid");
		String platform = this.get("platform"); // 媒体平台
		Boolean isV = false;

		AccountDTO userInfo = new AccountDTO();
		userInfo.isAjax = false;

		Platform _platform = Utility.getPlatform(Integer.parseInt(platform));

		// 用实时的方法 modify by wujian
		Result<IAccount> result = AppServerProxy.getAccountByUcodeOnline(
				accountid, _platform, this.getUser().getBaseUser());

		if (result.getErrCode() == 0) {
			IAccount account = result.getData();
			if (account != null) {
				userInfo.imgurlBig = account.getHead();// .getProfileImageUrl();
				userInfo.name = account.getName();
				userInfo.friends = account.getFriends();// .getFriendCount();
				userInfo.followers = account.getFollowers();// .getFollowCount();
				userInfo.weibos = account.getWeibos();// .getStatusCount();
				userInfo.summany = account.getDescription();
				userInfo.area = account.getLocation();
				isV = account.isVerified();
			}
		}

		DataAnalyzeUserInfoDTO entity = new DataAnalyzeUserInfoDTO();
		// 获取活跃度和影响力方法
		entity.userLiveness = 899;
		entity.userInfluence = 799;
		entity.account = userInfo;
		entity.isV = isV;
		return entity;
	}

}
