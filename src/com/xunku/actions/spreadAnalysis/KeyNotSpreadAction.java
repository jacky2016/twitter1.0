package com.xunku.actions.spreadAnalysis;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.xunku.actions.ActionBase;
import com.xunku.app.enums.SortKeyMan;
import com.xunku.app.helpers.DateHelper;
import com.xunku.app.result.spread.RetweetStatisResult;
import com.xunku.dto.spreadAnalysis.RetweetKeyDTO;
import com.xunku.utils.AppServerProxy;

//关键用户未传播
public class KeyNotSpreadAction extends ActionBase {

	@Override
	public  List<RetweetKeyDTO> doAction() {
		String type = this.get("type");
		String monitorid = this.get("monitorid");

		SortKeyMan sortKeyMan = SortKeyMan.Fans;
		if (type.equals("0")) {
			sortKeyMan = SortKeyMan.Fans; // 粉丝，0
		} else if (type.equals("1")) {
			sortKeyMan = SortKeyMan.Vip; // 认证，1
		}
		List<RetweetKeyDTO> lst = new ArrayList<RetweetKeyDTO>();
		List<RetweetStatisResult> results = AppServerProxy
				.spreadViewRetweetUnspread(Integer.parseInt(monitorid),
						sortKeyMan);
		if (results != null) {
			int id = 1;
			for (RetweetStatisResult result : results) {
				RetweetKeyDTO entity = new RetweetKeyDTO();
				entity.id = id;
				entity.name = result.getName();
				entity.location = result.getLocation();
				entity.followes = result.getFollowes();
				entity.userType = result.isVip() == true ? "认证用户" : "普通用户";
				entity.retweets = result.getRetweets();
				entity.retweetTime = DateHelper.formatDate(new Date(result
						.getRetweetTime()));
				lst.add(entity);
				id++;
			}
		}

		return lst;
	}

}
