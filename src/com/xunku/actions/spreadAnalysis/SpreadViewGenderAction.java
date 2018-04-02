package com.xunku.actions.spreadAnalysis;

import java.math.BigDecimal;

import com.xunku.actions.ActionBase;
import com.xunku.app.enums.PostType;
import com.xunku.app.result.GenderResult;
import com.xunku.app.result.VipResult;
import com.xunku.dto.PieDTO;
import com.xunku.dto.PieDataItemDTO;
import com.xunku.utils.AppServerProxy;

//传播分析 用户分析 性别比例
public class SpreadViewGenderAction extends ActionBase {

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
		GenderResult result = AppServerProxy.spreadViewGender(Integer
				.parseInt(monitorid), postType);

		// 计算比例
		double man = 0.0;
		double woman = 0.0;
		double other = 0.0;

		double count = 0;
		if (result != null) {
			count = result.getFemales() + result.getMales()
					+ result.getOthers();
			if (result.getMales() != 0.0) {
				man = result.getMales() / count;
				man = getValue(man);
			}
			if (result.getFemales() != 0.0) {
				woman = result.getFemales() / count;
				woman = getValue(woman);
			}
			if (result.getOthers() != 0.0) {
				other = result.getOthers() / count;
				other = getValue(other);
			}
		}

		PieDTO pieDto = GetexPieDTO(man, woman, other);
		return pieDto;
	}

	// 保留4位
	private double getValue(double vipValue) {

		BigDecimal bg = new BigDecimal(vipValue);
		vipValue = bg.setScale(4, BigDecimal.ROUND_HALF_UP).doubleValue();
		return vipValue;
	}

	// 粉丝统计 男女比例拼图结构
	private PieDTO GetexPieDTO(double man, double woman, double other) {
		PieDTO pieDto = new PieDTO();
		pieDto.title = "";
		PieDataItemDTO nEntity = new PieDataItemDTO();
		nEntity.name = "男";
		nEntity.y = man;

		PieDataItemDTO vEntity = new PieDataItemDTO();
		vEntity.name = "女";
		vEntity.y = woman;

		PieDataItemDTO oEntity = new PieDataItemDTO();
		oEntity.name = "其他";
		oEntity.y = other;

		pieDto.series.data.add(nEntity);
		pieDto.series.data.add(vEntity);
		pieDto.series.data.add(oEntity);

		return pieDto;
	}

}
