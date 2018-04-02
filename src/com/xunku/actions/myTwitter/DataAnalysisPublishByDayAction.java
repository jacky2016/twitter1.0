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
 * 数据分析模块 发布统计 按时间段获取数据 
 * @author sunao
 */
public class DataAnalysisPublishByDayAction extends ActionBase {

	@Override
	public LineDTO doAction() {
		String startTime = this.get("startTime") + " 00:00:00";
		String endTime = this.get("endTime") + " 23:59:59";
		String uid = this.get("uid");
		String platform = this.get("platform");
		User user = this.getUser().getBaseUser();
		Date startDate = DateUtils.stringToDate("yyyy-MM-dd HH:mm:ss",
				startTime);
		Date endDate = DateUtils.stringToDate("yyyy-MM-dd HH:mm:ss", endTime);
		PostType type = PostType.Creative;
		Platform _platform = Platform.Sina;
		if (platform.equals("1")) {
			_platform = Platform.Sina;
		} else if (platform.equals("2")) {
			_platform = Platform.Tencent;
		} else if (platform.equals("5")) {
			_platform = Platform.Renmin;
		}
		Map<Integer, PublishCountResult> maps = AppServerProxy
				.officialPublishByDay(uid, _platform, startDate.getTime(),
						endDate.getTime(), type, user);

		// 组织图表结构
		LineDTO line = new LineDTO();
		line.title = "按时间统计查询";
		LineDataDTO series = new LineDataDTO();
		series.name = "原帖+转发";
		line.series.add(series);
		SetSeries(line);
		if (maps != null) {
			for (Map.Entry<Integer, PublishCountResult> entry : maps.entrySet()) {
				String hour = entry.getKey().toString() + "点";

				for (int i = 0; i < line.categories.size(); i++) {
					if (line.categories.get(i).equals(hour)) {
						series.data.set(i, series.data.get(i)
								+ Double.parseDouble(String.valueOf(entry
										.getValue().getTweets()
										+ entry.getValue().getRetweets())));
						break;
					}
				}
			}
		}

		return line;
	}

	// 获取两日期之间所有日期
	private void SetSeries(LineDTO line) {

		for (int i = 0; i <= 23; i++) {

			line.categories.add(i + "点");
			line.series.get(0).data.add(0.0);
		}
	}

}
