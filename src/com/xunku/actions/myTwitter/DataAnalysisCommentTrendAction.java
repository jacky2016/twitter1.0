package com.xunku.actions.myTwitter;

import java.util.Date;
import java.util.Map;

import com.xunku.actions.ActionBase;
import com.xunku.app.enums.Platform;
import com.xunku.app.helpers.DateHelper;
import com.xunku.dto.LineDTO;
import com.xunku.dto.LineDataDTO;
import com.xunku.pojo.base.User;
import com.xunku.utils.AppServerProxy;
import com.xunku.utils.DateUtils;

/*
 * 数据分析模块 趋势分析 评论
 * @author sunao
 */
public class DataAnalysisCommentTrendAction extends ActionBase {

	@Override
	public Object doAction() {
		String startTime = this.get("startTime") + " 00:00:00";
		String endTime = this.get("endTime") + " 23:59:59";
		String uid = this.get("uid");
		String platform = this.get("platform");
		String dayCount = this.get("dayCount");
		User user = this.getUser().getBaseUser();
		Date startDate = DateUtils.stringToDate("yyyy-MM-dd HH:mm:ss",
				startTime);
		Date endDate = DateUtils.stringToDate("yyyy-MM-dd HH:mm:ss", endTime);
		int count = Integer.parseInt(dayCount);
		Platform _platform = Platform.Sina;
		if (platform.equals("1")) {
			_platform = Platform.Sina;
		} else if (platform.equals("2")) {
			_platform = Platform.Tencent;
		} else if (platform.equals("5")) {
			_platform = Platform.Renmin;
		}
		Map<Long, Integer> map = AppServerProxy.getViewCommentTrend(uid,
				_platform, startDate.getTime(), endDate.getTime(), user);

		// 组织图表结构
		LineDTO line = new LineDTO();
		line.title = "按日期统计查询";
		LineDataDTO series = new LineDataDTO();
		series.name = "评论";
		line.series.add(series);
		SetSeries(startTime, count, line);
		if (map != null) {
			for (Map.Entry<Long, Integer> entry : map.entrySet()) {
				Date date = new Date(entry.getKey());
				String dateStr = DateHelper.formatDateChar(date);

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
