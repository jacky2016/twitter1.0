package com.xunku.actions.myTwitter;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.xunku.actions.ActionBase;
import com.xunku.app.enums.Platform;
import com.xunku.app.enums.PostType;
import com.xunku.app.helpers.DateHelper;
import com.xunku.dto.LineDTO;
import com.xunku.dto.LineDataDTO;
import com.xunku.pojo.base.User;
import com.xunku.utils.AppServerProxy;
import com.xunku.utils.DateUtils;

/*
 * 数据分析模块 @我统计 
 * @author sunao
 */
public class DataAnalysisMentionAction extends ActionBase {

	@Override
	public LineDTO doAction() {
		String startTime = this.get("startTime") + " 00:00:00";
		String endTime = this.get("endTime") + " 23:59:59";
		String uid = this.get("uid");
		String platform = this.get("platform");
		String dayCount = this.get("dayCount");
		User user = this.getUser().getBaseUser();
		Date startDate = DateUtils.stringToDate("yyyy-MM-dd HH:mm:ss",
				startTime);
		Date endDate = DateUtils.stringToDate("yyyy-MM-dd HH:mm:ss", endTime);
		PostType type = PostType.Creative;
		Platform _platform = Platform.Sina;
		int count = Integer.parseInt(dayCount);
		if (platform.equals("1")) {
			_platform = Platform.Sina;
		} else if (platform.equals("2")) {
			_platform = Platform.Tencent;
		} else if (platform.equals("5")) {
			_platform = Platform.Renmin;
		}

		// 获取转发数据
		type = PostType.All;
		Map<Long, Integer> maps = AppServerProxy.officialMention(uid,
				_platform, startDate.getTime(), endDate.getTime(), type, user);

		// 组织图表结构
		LineDTO line = new LineDTO();
		line.title = "按日期统计查询";
		LineDataDTO series = new LineDataDTO();
		series.name = "@我统计";
		line.series.add(series);
		SetSeries(startTime, count, line);
		if (maps != null) {
			for (Map.Entry<Long, Integer> entry : maps.entrySet()) {
				String dateStr = DateHelper.formatDateChar(new Date(entry
						.getKey()));
				for (int i = 0; i < line.categories.size(); i++) {
					if (line.categories.get(i).equals(dateStr)) {
						series.data.set(i, series.data.get(i)
								+ Double.parseDouble(entry.getValue()
										.toString()));
						break;
					}
				}
			}
		}

		return line;
	}

	// 获取两日期之间所有日期
	private void SetSeries(String startTime, int count, LineDTO line) {

		for (int i = 0; i <= count; i++) {
			String dateStr = DateUtils.addDay2(startTime, i);
			Date date = DateUtils.stringToDate("yyyy-MM-dd", dateStr);
			line.categories.add(DateHelper.formatDateChar(date));
			line.series.get(0).data.add(0.0);
		}
	}
}
