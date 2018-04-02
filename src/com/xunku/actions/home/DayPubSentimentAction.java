package com.xunku.actions.home;

import java.math.BigDecimal;
import java.util.Map;

import com.xunku.actions.ActionBase;
import com.xunku.app.enums.Platform;
import com.xunku.dto.PieDTO;
import com.xunku.dto.PieDataItemDTO;
import com.xunku.dto.task.TaskCntDTO;
import com.xunku.pojo.base.User;
import com.xunku.utils.AppServerProxy;

/*
 * 首页模块今日舆情类别分析图表数据
 * @author sunao
 */
public class DayPubSentimentAction extends ActionBase {

	@Override
	public PieDTO doAction() {
		User user = this.getUser().getBaseUser();
		int customid = user.getCustomID();
		Map<String, Integer> maps = AppServerProxy
				.viewTaskGroupByPlatform(customid);

		PieDTO pieDto = new PieDTO();
		// 组织结构
		if (maps != null) {
			int count = 0;
			for (Map.Entry<String, Integer> entry : maps.entrySet()) {
				count += entry.getValue();
			}
			for (Map.Entry<String, Integer> entry : maps.entrySet()) {

				PieDataItemDTO nEntity = new PieDataItemDTO();
				nEntity.name = entry.getKey();
				double d = entry.getValue();
				double v = d / count;
				v = getValue(v);
				nEntity.y = v;
				pieDto.series.data.add(nEntity);

			}
		}
		return pieDto;
	}

	// 保留1位
	private double getValue(double vipValue) {
		BigDecimal bg = new BigDecimal(vipValue);
		vipValue = bg.setScale(4, BigDecimal.ROUND_HALF_UP).doubleValue();
		return vipValue;
	}
}
