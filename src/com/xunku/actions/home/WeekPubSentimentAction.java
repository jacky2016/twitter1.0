package com.xunku.actions.home;

import java.text.ParseException;
import java.util.Date;
import java.util.Map;

import com.xunku.actions.ActionBase;
import com.xunku.app.helpers.DateHelper;
import com.xunku.dto.LineDTO;
import com.xunku.dto.LineDataDTO;
import com.xunku.dto.task.TaskCntDTO;
import com.xunku.pojo.base.User;
import com.xunku.utils.AppServerProxy;
import com.xunku.utils.DateUtils;

/*
 * 首页模块一周舆情监测统计图表数据
 * @author sunao
 */
public class WeekPubSentimentAction extends ActionBase {

	@Override
	public LineDTO doAction() {

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
			Map<Long, TaskCntDTO> maps = AppServerProxy.viewTaskCount(customid,
					startTime, endTime);

			// 组织结构
			lineDto.title = "一周统计";
			LineDataDTO seriesSina = new LineDataDTO();
			seriesSina.name = "新浪";
			LineDataDTO seriesQQ = new LineDataDTO();
			seriesQQ.name = "腾讯";
			LineDataDTO seriesRenMin = new LineDataDTO();
			seriesRenMin.name = "人民";

			if (maps != null) {
				for (Map.Entry<Long, TaskCntDTO> entry : maps.entrySet()) {
					Date date = new Date(entry.getKey());
					String dateStr = DateHelper.formatDateChar(date);
					lineDto.categories.add(dateStr);

					seriesSina.data.add(Double.parseDouble(String.valueOf(entry
							.getValue().sinaCnt)));

					seriesQQ.data.add(Double.parseDouble(String.valueOf(entry
							.getValue().tencentCnt)));

					seriesRenMin.data.add(Double.parseDouble(String
							.valueOf(entry.getValue().renminCnt)));
				}
			}
			lineDto.series.add(seriesSina);
			lineDto.series.add(seriesQQ);
			lineDto.series.add(seriesRenMin);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return lineDto;
	}

}
