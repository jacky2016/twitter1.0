package com.xunku.actions.spreadAnalysis;

import java.math.BigDecimal;
import java.util.Map;

import com.xunku.actions.ActionBase;
import com.xunku.app.enums.PostType;
import com.xunku.dto.PieDTO;
import com.xunku.dto.PieDataItemDTO;
import com.xunku.utils.AppServerProxy;

//传播分析 用户分析 来源比例
public class SpreadViewFromsNumAction extends ActionBase {

	@Override
	public PieDTO doAction() {
		String monitorid = this.get("monitorid");
		String type = this.get("type");

		PostType postType = PostType.All;
		if (type.equals("0")) {
			postType = PostType.All; // 全部,0
		} else if (type.equals("1")) {
			postType = PostType.Repost; // 转发,1
		} else if (type.equals("2")) {
			postType = PostType.Comment; // 评论,2
		}

		Map<String, Integer> result = AppServerProxy.spreadViewFromsNum(Integer
				.parseInt(monitorid), postType);

		double count = 0;
		PieDTO pieDto = new PieDTO();
		if (result != null) {
			for (Map.Entry<String, Integer> entry : result.entrySet()) {
				count += entry.getValue();
			}
			// 组织结构
			pieDto.title = "";
			for (Map.Entry<String, Integer> entry : result.entrySet()) {
				PieDataItemDTO entity = new PieDataItemDTO();
				entity.name = entry.getKey();
				double value = entry.getValue();
				value = value / count;
				value = getValue(value);
				entity.y = value / count;
				pieDto.series.data.add(entity);
			}
		}

		return pieDto;
	}

	// 保留1位
	private double getValue(double vipValue) {
		BigDecimal bg = new BigDecimal(vipValue);
		vipValue = bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
		return vipValue;
	}
}
