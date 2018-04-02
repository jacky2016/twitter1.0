package com.xunku.portal;

import java.io.IOException;
import java.util.Timer;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.xunku.app.manager.DaemonTask;

public class DaemonServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	final int period = 1000 * 60 * 1;// 1分钟一次
	Timer timer;

	public DaemonServlet() {
		timer = new Timer();
	}

	public void init() throws ServletException {
		timer.schedule(new DaemonTask(), 1000, period);
	}

	public void doGet(HttpServletRequest httpservletrequest,
			HttpServletResponse httpservletresponse) throws ServletException,
			IOException {
	}

	public void destory() {
		timer.cancel();
	}
}
