package com.xunku.actions.pubSentiment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.xunku.actions.ActionBase;
import com.xunku.app.model.TweetStatus;
import com.xunku.utils.AppServerProxy;

/*
 * 舆情展示模块，获取收集状态
 * @author sunao
 */
public class GetCollectStateAction extends ActionBase {

	@Override
	public Map<String, TweetStatus> doAction() {
		int customId = this.getUser().getBaseUser().getCustomID();
		
		String urls = this.get("urls");
		
		List<String> list = new ArrayList<String>();
		Map<String, TweetStatus> map = new HashMap<String, TweetStatus>();
		
		if (!urls.equals("")) {
			String urlArray[] = urls.split(",");
			for (String url : urlArray) {
				list.add(url);
			}
			map = AppServerProxy.getCollectionStatus(list, customId);
		}
		//0：表示未收集，>0：已收集
		return map;
	}

}
