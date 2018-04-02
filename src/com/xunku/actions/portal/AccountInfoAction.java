package com.xunku.actions.portal;

import com.xunku.actions.ActionBase;
import com.xunku.app.Utility;
import com.xunku.app.enums.Platform;
import com.xunku.app.interfaces.IAccount;
import com.xunku.constant.PortalCST;
import com.xunku.dto.AccountDTO;
import com.xunku.pojo.base.Account;
import com.xunku.utils.AppServerProxy;

/*
 * 所有模块列表，获取微博账号信息
 * @author sunao
 */
public class AccountInfoAction extends ActionBase {

	@Override
	public Object doAction() {
		String platform = this.get("platform");
		String ucode = this.get("ucode");
		Platform _Platform = Utility.getPlatform(Integer.parseInt(platform)); // 媒体平台枚举
		
		if(_Platform == Platform.Tencent){
			return new AccountDTO();
		}
		
		IAccount account = AppServerProxy.getAccountByUcodeOnline4Tip(ucode,
				_Platform, this.getUser().getBaseUser()).getData();

		// 组织前台使用微博账号DTO
		AccountDTO accountDTO = new AccountDTO();
		accountDTO.ucode = ucode; // 用户头像ucode
		accountDTO.twitterType = Integer.parseInt(platform);
		if (account != null) {

			accountDTO = new AccountDTO();
			accountDTO.url = account.getHomeUrl();
			accountDTO.uid = account.getUcode();
			if (_Platform == Platform.Tencent && account.getHead() != null) {
				accountDTO.imgurlBig = PortalCST.IMAGE_PATH_PERFIX
						+ account.getLargeHead();
				accountDTO.imgurl = PortalCST.IMAGE_PATH_PERFIX
						+ account.getHead();
			} else {
				accountDTO.imgurl = account.getHead();
			}
			accountDTO.imgurlBig = account.getLargeHead();
			accountDTO.name = account.getName();
			accountDTO.friends = account.getFriends(); // 关注
			accountDTO.weibos = account.getWeibos(); // 发布微博数
			accountDTO.followers = account.getFollowers();// 粉丝数
			accountDTO.summany = account.getDescription();
			accountDTO.isAjax = false; // 前台不用在异步加载
		}
		return accountDTO;
	}

	
}
