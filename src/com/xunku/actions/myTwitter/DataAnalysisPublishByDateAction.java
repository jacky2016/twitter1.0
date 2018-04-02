package com.xunku.actions.myTwitter;

import java.util.Date;
import java.util.Map;

import com.xunku.actions.ActionBase;
import com.xunku.app.enums.Platform;
import com.xunku.app.enums.PostType;
import com.xunku.app.helpers.DateHelper;
import com.xunku.app.result.PublishCountResult;
import com.xunku.dto.LineDTO;
import com.xunku.dto.LineDataDTO;
import com.xunku.pojo.base.User;
import com.xunku.utils.AppServerProxy;
import com.xunku.utils.DateUtils;

/*
 * 数据分析模块 发布统计 按时间获取数据 
 * @author sunao
 */
public class DataAnalysisPublishByDateAction extends ActionBase {

	@Override
	public LineDTO doAction() {
		String startTime = this.get("startTime") + " 00:00:00";
		String endTime = this.get("endTime") + " 23:59:59";
		String uid = this.get("uid");
		String dayCount = this.get("dayCount");
		String platform = this.get("platform");
		User user = this.getUser().getBaseUser();
		Date startDate = DateUtils.stringToDate("yyyy-MM-dd HH:mm:ss",
				startTime);
		Date endDate = DateUtils.stringToDate("yyyy-MM-dd HH:mm:ss", endTime);
		int count = Integer.parseInt(dayCount);
		PostType type = PostType.Creative;
		Platform _platform = Platform.Sina;
		if (platform.equals("1")) {
			_platform = Platform.Sina;
		} else if (platform.equals("2")) {
			_platform = Platform.Tencent;
		} else if (platform.equals("5")) {
			_platform = Platform.Renmin;
		}
		Map<Long, PublishCountResult> maps = AppServerProxy
				.officialPublishByDate(uid, _platform, startDate.getTime(),
						endDate.getTime(), type, user);

		// 组织图表结构
		LineDTO line = new LineDTO();
		LineDataDTO seriesCreative = new LineDataDTO();
		seriesCreative.name = "原帖";
		LineDataDTO seriesRepost = new LineDataDTO();
		seriesRepost.name = "转发";
		line.title = "按日期统计查询";
		line.series.add(seriesCreative);
		line.series.add(seriesRepost);

		SetSeries(startTime, count, line);
		if (maps != null) {
			for (Map.Entry<Long, PublishCountResult> entry : maps.entrySet()) {
				Date date = new Date(entry.getKey());
				String dateStr = DateHelper.formatDateChar(date);

				for (int i = 0; i < line.categories.size(); i++) {
					if (line.categories.get(i).equals(dateStr)) {
						seriesCreative.data.set(i, seriesCreative.data.get(i)
								+ Double.parseDouble(String.valueOf(entry
										.getValue().getTweets())));

						seriesRepost.data.set(i, seriesRepost.data.get(i)
								+ Double.parseDouble(String.valueOf(entry
										.getValue().getRetweets())));
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
			line.series.get(1).data.add(0.0);
		}
	}

}