package com.xunku.actions.spreadAnalysis;

import java.math.BigDecimal;

import com.xunku.actions.ActionBase;
import com.xunku.app.enums.PostType;
import com.xunku.dto.PieDTO;
import com.xunku.dto.PieDataItemDTO;
import com.xunku.utils.AppServerProxy;

// 传播分析 用户分析 转发态度分析
public class SpreadViewRetweetAttitudeAction extends ActionBase {

	@Override
	public PieDTO doAction() {
		String monitorid = this.get("monitorid");

		double[] arrays = AppServerProxy.viewRetweetAttitude(Integer
				.parseInt(monitorid));
		double repost1 = 0.0;
		double repost2 = 0.0;
		double count = 0;
		if (arrays != null && arrays.length == 2) {
			count = arrays[0] + arrays[1];
			if (arrays[0] != 0.0) {
				repost1 = arrays[0] / count;
				repost1 = getValue(repost1);
			}
			if (arrays[1] != 0.0) {
				repost2 = arrays[1] / count;
				repost2 = getValue(repost2);
			}
		}
		PieDTO pieDto = GetPieDTO(repost1, repost2);
		return pieDto;
	}

	// 保留4位
	private double getValue(double vipValue) {
		BigDecimal bg = new BigDecimal(vipValue);
		vipValue = bg.setScale(4, BigDecimal.ROUND_HALF_UP).doubleValue();
		return vipValue;
	}

	// 粉丝统计 男女比例拼图结构
	private PieDTO GetPieDTO(double repost1, double repost2) {
		PieDTO pieDto = new PieDTO();
		pieDto.title = "";
		PieDataItemDTO nEntity = new PieDataItemDTO();
		nEntity.name = "直接转发";
		nEntity.y = repost1;

		PieDataItemDTO vEntity = new PieDataItemDTO();
		vEntity.name = "带有内容转发";
		vEntity.y = repost2;

		pieDto.series.data.add(nEntity);
		pieDto.series.data.add(vEntity);

		return pieDto;
	}

}
