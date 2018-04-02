package com.xunku.app.controller;

import java.io.IOException;
import java.util.List;

import com.xunku.app.monitor.EventMonitor;
import com.xunku.pojo.base.Custom;
import com.xunku.utils.PropertiesUtils;

import weibo4j.model.Status;

public class BusinessController {

	static String url;

	static {
		try {
			url = PropertiesUtils.getString("config", "job.business.server");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public List<Status> search(Custom custom, EventMonitor event) {

		return null;
	}

}
