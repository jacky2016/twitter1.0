package com.xunku.actions.spreadAnalysis;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.xunku.actions.ActionBase;
import com.xunku.app.enums.PostType;
import com.xunku.app.helpers.DateHelper;
import com.xunku.dto.LineDTO;
import com.xunku.dto.LineDataDTO;
import com.xunku.utils.AppServerProxy;
import com.xunku.utils.DateUtils;

//获取监测对象的趋势分析 转发结果 
public class SpreadViewTrendReportAction extends ActionBase {

	@Override
	public LineDTO doAction() {
		String monitorid = this.get("monitorid");
		String startTime = this.get("startTime");
		String endTime = this.get("endTime");

		PostType postType = PostType.Repost;
		Date startDate = DateUtils.stringToDate("yyyy-MM-dd", startTime);
		Date endDate = DateUtils.stringToDate("yyyy-MM-dd", endTime);
		Map<Long, Integer> mapRepost = AppServerProxy.spreadViewTrend(Integer
				.parseInt(monitorid), postType, startDate, endDate);

		// 计算步长
		int tickInterval = 1;
		if (mapRepost != null && mapRepost.size() > 0) {
			tickInterval = mapRepost.size() / 7;
		}
		// 组织图表结构
		LineDTO line = new LineDTO();
		line.title = "转发趋势分析";
		line.tickInterval = tickInterval;
		LineDataDTO series = new LineDataDTO();
		series.name = "转发";
		if (mapRepost != null) {
			for (Map.Entry<Long, Integer> entry : mapRepost.entrySet()) {
				line.categories.add(DateHelper.formatDateChar2(new Date(entry
						.getKey())));
				
				
				series.data
						.add(Double.parseDouble(entry.getValue().toString()));
			}
		}
		line.series.add(series);
		return line;
	}
}
