package com.xunku.portal;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.xunku.actions.ActionContext;
import com.xunku.actions.ActionProxy;
import com.xunku.app.AppContext;
import com.xunku.app.manager.UserSessionManager;
import com.xunku.app.model.UserSession;

/**
 * 业务逻辑基本处理器
 * <p>
 * 所有的业务逻辑均通过该处理器完成
 * 
 * @author wujian
 * @created on Jun 5, 2014 3:57:14 PM
 */
public class ActionServlet extends HttpServlet {

	// 所有的业务逻辑都走这个servlet

	private static final long serialVersionUID = 862870379088833394L;

	@Override
	public void service(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		AppContext appContext = AppContext.getInstance();
		ActionProxy proxy = new ActionProxy(this.createContext(request,
				appContext));

		this.setEncoding(request, response);

		try {
			PrintWriter writer = response.getWriter();

			writer.print(proxy.executeAction());

			writer.flush();
			writer.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	private ActionContext createContext(HttpServletRequest request,
			AppContext context) {
		UserSession session = context.getSessionManager().getSession(request);
		ActionContext ctx = new ActionContext(session);
		ctx.setParameters(request.getParameterMap());
		return ctx;
	}

	private void setEncoding(HttpServletRequest request,
			HttpServletResponse response) {
		try {
			request.setCharacterEncoding("UTF-8");
			response.setCharacterEncoding("UTF-8");
		} catch (Exception e) {
			// log exception
		}
	}
}
