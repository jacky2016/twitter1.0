package com.xunku.actions.pushservices;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.xunku.actions.ActionBase;
import com.xunku.app.parser.TweetUrlParserFactory;
import com.xunku.pojo.base.User;
import com.xunku.utils.AppServerProxy;

public class WarnServicesStateAction extends ActionBase {

	@Override
	public Map<String, String[]> doAction() {
		String tid = this.get("urls");
		String[] urlsArray = tid.split(",");
		List<String> urlLst = new ArrayList<String>();
		User user = this.getUser().getBaseUser();

		for (String url : urlsArray) {
			urlLst.add(url);
		}
		
		Map<String, String[]> map = AppServerProxy.getWarningStatus(urlLst, user
				.getCustomID());
		return map;
	}
}
