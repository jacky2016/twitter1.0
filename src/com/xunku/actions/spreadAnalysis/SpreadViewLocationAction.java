package com.xunku.actions.spreadAnalysis;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.xunku.actions.ActionBase;
import com.xunku.app.enums.PostType;
import com.xunku.app.helpers.DateHelper;
import com.xunku.app.result.VipResult;
import com.xunku.cache.Cache;
import com.xunku.cache.CacheManager;
import com.xunku.constant.PortalCST;
import com.xunku.dto.LineDTO;
import com.xunku.dto.LineDataDTO;
import com.xunku.utils.AppServerProxy;

//传播分析 用户分析 地域分析
public class SpreadViewLocationAction extends ActionBase {

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
		Map<String, Integer> maps = AppServerProxy.viewLocation(Integer
				.parseInt(monitorid), postType);

		// 组织结构
		LineDTO line = new LineDTO();
		line.title = "地域分析";
		line.tickInterval = 0;
		LineDataDTO series = new LineDataDTO();
		series.name = "地域";
		if (maps != null) {
			int index = 0;
			Integer otherCount = 0;
			for (Map.Entry<String, Integer> entry : maps.entrySet()) {
				if (index < 19) {
					// 从缓存获取城市信息
					String cityName = "";
					Cache cache = CacheManager
							.getCacheInfo(PortalCST.GetCityCacheKey);
					Map<Integer, String> citys = (HashMap<Integer, String>) cache
							.getValue();
					if (citys.containsKey(entry.getKey())) {
						cityName = citys.get(entry.getKey());
					}
					if (!cityName.equals("")) {
						line.categories.add(cityName);
					} else {
						line.categories.add(entry.getKey());
					}
					series.data.add(Double.parseDouble(entry.getValue()
							.toString()));
				} else {
					otherCount += entry.getValue();
				}
				index++;
			}
			if (index > 19) {
				line.categories.add("其他");
				series.data.add(Double.parseDouble(otherCount.toString()));
			}
		}
		line.series.add(series);
		return line;
	}

}
