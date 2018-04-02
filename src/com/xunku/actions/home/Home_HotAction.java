package com.xunku.actions.home;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.xunku.actions.ActionBase;
import com.xunku.app.Utility;
import com.xunku.app.enums.Platform;
import com.xunku.dao.home.HotDao;
import com.xunku.daoImpl.home.HotDaoImpl;
import com.xunku.pojo.home.Hot;
import com.xunku.utils.AppServerProxy;
import com.xunku.utils.DateUtils;

/*
 * 获取首页热点信息
 * @author sunao
 */
public class Home_HotAction extends ActionBase {

	@Override
	public List<Hot> doAction() {

		String type = this.get("type");
		int twitterType = 0;
		twitterType = Integer.parseInt(type);
		Platform platform = Utility.getPlatform(twitterType);
		String nowtime = "";
		List<Hot> infos = new ArrayList<Hot>();
		try {
			nowtime = DateUtils.nowDateFormat("yyyy-MM-dd 00:00:00");
			Date nowTime = DateUtils.stringToDate("yyyy-MM-dd 00:00:00",
					nowtime);
			infos = AppServerProxy.viewHomeHots(platform, nowTime);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}// 当前日期

		return infos;
	}

}