package com.xunku.portal;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.xunku.app.AppContext;
import com.xunku.app.manager.UserSessionManager;

public class LogoutServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5832181585573769267L;

	@Override
	public void service(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		response.setCharacterEncoding("UTF-8");
		PrintWriter writer = response.getWriter();
		if (AppContext.getInstance().getSessionManager().Logout(request,
				response)) {
			writer.print("1");
		} else {
			writer.print("0");
		}

		writer.flush();
		writer.close();
	}
}
