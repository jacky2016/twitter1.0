package com.xunku.portal;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.xunku.app.AppContext;
import com.xunku.app.enums.LoginStatus;
import com.xunku.app.manager.UserSessionManager;
import com.xunku.app.model.LoginResult;
import com.xunku.constant.SessionCST;

public class LoginFilter implements Filter {

	@Override
	public void doFilter(ServletRequest req, ServletResponse resp,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) resp;

		UserSessionManager manager = AppContext.getInstance()
				.getSessionManager();

		if (manager.needLogin(request)) {

			String username = manager.getUserNameFromCookie(request);
			String token = manager.getUserTokenFromCookie(request);
			String clientId = manager.getClientID(request);
			LoginResult loginResult = manager.loginWithToken(clientId,
					username, token);

			if (loginResult.getStatus() == LoginStatus.SUCESS) {

				manager.getSession(request).setUser(loginResult.getUserInfo());
				request.getSession().setAttribute(SessionCST.USER_OBJECT,
						loginResult.getUserInfo());

				chain.doFilter(request, response);
			} else {
				// 需要登录但是没有自动登录成功，则重定向到登录页面
				response.sendRedirect(request.getContextPath() + "/login.jsp");
			}
		} else {
			chain.doFilter(request, response);
		}

	}

	@Override
	public void destroy() {

	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {

	}

}
