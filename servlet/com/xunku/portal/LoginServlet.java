package com.xunku.portal;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.xunku.app.AppContext;
import com.xunku.app.Utility;
import com.xunku.app.enums.LoginStatus;
import com.xunku.app.manager.UserSessionManager;
import com.xunku.app.model.LoginResult;
import com.xunku.app.model.UserSession;
import com.xunku.dto.login.LoginInfoDTO;
import com.xunku.pojo.base.User;

public class LoginServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 324218081720258476L;

	@Override
	public void service(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		UserSessionManager manager = AppContext.getInstance().getSessionManager();

		UserSession session = manager.getSession(request);
		if (session == null) {
			System.out.print("Session无效！");
			return;
		}
		String remberMe = request.getParameter("rem");

		LoginResult result = null;

		response.setCharacterEncoding("UTF-8");
		PrintWriter writer = response.getWriter();

		try {

			result = manager.login(request);

			if (result.getStatus() == LoginStatus.SUCESS) {
				session.setUser(result.getUserInfo());
				session.setLogined(true);//登录成功
				session.setIp(request.getRemoteAddr());
				// 如果需要记住我，则往cookie里面写用户名和token
				if (remberMe.equals("1")) {
					User user = result.getUserInfo().getBaseUser();
					manager.remeberMe(request, response, user);
				}
			}

		} catch (Exception ex) {
			result = LoginResult.getErrException(ex);
		}
		LoginInfoDTO loginInfoInfo = new LoginInfoDTO();
		loginInfoInfo.message = result.getMessage();
		loginInfoInfo.status = result.getStatus();
		loginInfoInfo.times = result.getTimes();
		writer.print(Utility.toJSON(loginInfoInfo));
		writer.flush();
		writer.close();

	}

}
