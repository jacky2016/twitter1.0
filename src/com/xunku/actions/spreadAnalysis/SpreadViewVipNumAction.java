package com.xunku.actions.spreadAnalysis;

import java.math.BigDecimal;

import com.xunku.actions.ActionBase;
import com.xunku.app.enums.PostType;
import com.xunku.app.enums.SortKeyMan;
import com.xunku.app.result.VipResult;
import com.xunku.dto.PieDTO;
import com.xunku.dto.PieDataItemDTO;
import com.xunku.utils.AppServerProxy;

// 传播分析 用户分析 认证比例
public class SpreadViewVipNumAction extends ActionBase {

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
		VipResult result = AppServerProxy.spreadViewVipNum(Integer
				.parseInt(monitorid), postType);

		// 计算比例
		// double man = 0.0;
		// double woman = 0.0;
		// double other=0.0;
		double vip = 0.0;
		double noVip = 0.0;
		double count = 0;
		if (result != null) {
			count = result.getVipCnt() + result.getNoVipCnt();
			if (result.getVipCnt() != 0) {
				vip = result.getVipCnt() / count;
				vip = getValue(vip);
			}
			if (result.getNoVipCnt() != 0) {
				noVip = result.getNoVipCnt() / count;
				noVip = getValue(noVip);
			}
		}
		PieDTO pieDto = GetexPieDTO(vip, noVip);
		return pieDto;
	}

	// 保留4位
	private double getValue(double vipValue) {
		BigDecimal bg = new BigDecimal(vipValue);
		vipValue = bg.setScale(4, BigDecimal.ROUND_HALF_UP).doubleValue();
		return vipValue;
	}

	// 粉丝统计 认证比例拼图结构
	private PieDTO GetexPieDTO(double vip, double noVip) {
		PieDTO pieDto = new PieDTO();
		pieDto.title = "";
		PieDataItemDTO nEntity = new PieDataItemDTO();
		nEntity.name = "认证用户";
		nEntity.y = vip;

		PieDataItemDTO vEntity = new PieDataItemDTO();
		vEntity.name = "普通用户";
		vEntity.y = noVip;

		pieDto.series.data.add(nEntity);
		pieDto.series.data.add(vEntity);

		return pieDto;
	}
}
