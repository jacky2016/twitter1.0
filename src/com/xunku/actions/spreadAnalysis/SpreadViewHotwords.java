package com.xunku.actions.spreadAnalysis;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.xunku.actions.ActionBase;
import com.xunku.dto.CloudWordDataDTO;
import com.xunku.utils.AppServerProxy;

// sunao 获取传播分析 词云
public class SpreadViewHotwords extends ActionBase {

	@Override
	public List<CloudWordDataDTO> doAction() {

		String monitorid = this.get("monitorid");
		Map<String, Float> hotwords = AppServerProxy.viewHotwords(Integer
				.parseInt(monitorid));
		List<CloudWordDataDTO> lst = new ArrayList<CloudWordDataDTO>();
		for (Map.Entry<String, Float> entry : hotwords.entrySet()) {
			CloudWordDataDTO entity = new CloudWordDataDTO();
			entity.text = entry.getKey();
			entity.weight = entry.getValue();
			lst.add(entity);
		}

		return lst;
	}
}
