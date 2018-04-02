package com.xunku.actions.spreadAnalysis;

import java.util.Map;

import com.xunku.actions.ActionBase;
import com.xunku.app.enums.PostType;
import com.xunku.dto.LineDTO;
import com.xunku.dto.LineDataDTO;
import com.xunku.utils.AppServerProxy;

// 用户分析页签 用户粉丝数分析 数据
public class SpreadViewFansHistogramAction extends ActionBase {

	@Override
	public LineDTO doAction() {

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
		int[] arrays = AppServerProxy.spreadViewFansHistogram(Integer
				.parseInt(monitorid), postType);

		// 组织结构
		LineDTO line = new LineDTO();
		line.categories.add("0-100");
		line.categories.add("100-1千");
		line.categories.add("1千-1万");
		line.categories.add("1万-10万");
		line.categories.add("10万-100万");
		line.title = "用户粉丝数分析";
		line.tickInterval = 0;
		LineDataDTO series = new LineDataDTO();
		series.name = "粉丝";
		if (arrays != null) {
			for (Integer count : arrays) {
				
				series.data.add(Double.parseDouble(count.toString()));
			}
			line.series.add(series);
		}
		return line;
	}

}
