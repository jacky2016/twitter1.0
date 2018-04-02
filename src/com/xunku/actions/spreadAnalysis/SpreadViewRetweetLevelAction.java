package com.xunku.actions.spreadAnalysis;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.xunku.actions.ActionBase;
import com.xunku.app.Utility;
import com.xunku.app.interfaces.IAccount;
import com.xunku.app.model.KV;
import com.xunku.app.result.spread.RetweetLevelResult;
import com.xunku.dto.AccountDTO;
import com.xunku.dto.spreadAnalysis.RetweetLevelDTO;
import com.xunku.pojo.base.Account;
import com.xunku.utils.AppServerProxy;

//获取转发层级 数据
public class SpreadViewRetweetLevelAction extends ActionBase {

	@Override
	public List<RetweetLevelDTO> doAction() {
		String monitorid = this.get("monitorid");
		List<RetweetLevelDTO> lst = new ArrayList<RetweetLevelDTO>();
		List<RetweetLevelResult> results = AppServerProxy
				.spreadViewRetweetLevel(Integer.parseInt(monitorid));
		if (results != null) {
			for (RetweetLevelResult result : results) {
				RetweetLevelDTO entity = new RetweetLevelDTO();
				entity.level = result.getLevel();
				entity.personNum = result.getTweets();
				entity.followers = result.getFollowers();
				if (result.getSuperMans() != null) {
					for (KV<IAccount, Integer> entry : result.getSuperMans()) {
						AccountDTO account = new AccountDTO();
						if (entry.getKey().getHead() != null) {
							account.imgurl = entry.getKey().getHead();
						}
						account.name = entry.getKey().getName();
						account.friends = entry.getValue(); // 那关注属性装转发数
						account.ucode = entry.getKey().getUcode();
						account.twitterType = Utility.getPlatform(entry
								.getKey().getPlatform());
						entity.accounts.add(account);
					}
				}
				lst.add(entity);
			}
		}
		return lst;
	}

}
