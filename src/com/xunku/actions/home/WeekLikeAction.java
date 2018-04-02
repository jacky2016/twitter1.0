package com.xunku.actions.home;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.xunku.actions.ActionBase;
import com.xunku.app.helpers.DateHelper;
import com.xunku.dto.LineDTO;
import com.xunku.dto.LineDataDTO;
import com.xunku.dto.task.TaskCntDTO;
import com.xunku.pojo.base.User;
import com.xunku.pojo.home.FansTrend;
import com.xunku.utils.AppServerProxy;
import com.xunku.utils.DateUtils;

/*
 * 首页模块最近七天粉丝趋势图表数据
 * @author sunao
 */
public class WeekLikeAction extends ActionBase {

	@Override
	public Object doAction() {

		User user = this.getUser().getBaseUser();
		int customid = user.getCustomID();
		String startTimeStr = "", endTimeStr = "";
		LineDTO lineDto = new LineDTO();

		try {
			endTimeStr = DateUtils.nowDateFormat("yyyy-MM-dd 00:00:00");
			startTimeStr = DateUtils.addDay(endTimeStr, -6);
			Date startTime = DateUtils.stringToDate("yyyy-MM-dd 00:00:00",
					startTimeStr);
			Date endTime = DateUtils.stringToDate("yyyy-MM-dd 00:00:00",
					endTimeStr);
			Map<String, List<FansTrend>> maps = AppServerProxy
					.viewHomeFansTrend(customid, startTime, endTime);

			// 组织结构
			lineDto.title = "七天粉丝分析";
			boolean on = true;
			if (maps != null) {
				for (Map.Entry<String, List<FansTrend>> entry : maps.entrySet()) {
					List<FansTrend> lst = entry.getValue();
					LineDataDTO series = new LineDataDTO();
					series.name = entry.getKey();
					if (on) {
						for (FansTrend trend : lst) {
							String dateStr = DateHelper.formatDateChar(trend
									.getCreated());
							lineDto.categories.add(dateStr);
							series.data.add(Double.parseDouble(String
									.valueOf(trend.getFans())));
							
						}
					} else {
						for (FansTrend trend : lst) {
							series.data.add(Double.parseDouble(String
									.valueOf(trend.getFans())));
						}
					}
					lineDto.series.add(series);
					on = false;
				}
			}

		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return lineDto;

	}
}
