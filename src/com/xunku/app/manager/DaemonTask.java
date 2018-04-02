package com.xunku.app.manager;

import java.util.TimerTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DaemonTask extends TimerTask {

	private static final Logger LOG = LoggerFactory.getLogger(DaemonTask.class);

	@Override
	public void run() {
		LOG.info("recycle...");
		UserSessionManager.getInstance().recycle();
	}

}
