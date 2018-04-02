package com.xunku.portal;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.xunku.app.AppContext;
import com.xunku.app.Utility;
import com.xunku.app.enums.Platform;
import com.xunku.app.interfaces.IAccount;
import com.xunku.app.model.AppAuth;
import com.xunku.app.result.Result;
import com.xunku.constant.PortalCST;
import com.xunku.daoImpl.base.UserDaoImpl;
import com.xunku.dto.ResultDTO;
import com.xunku.pojo.base.User;

/**
 * 分析处理器
 * <p>
 * 页面上的分析结果都由该处理器提供
 * <p>
 * 该处理器通过pathInfo来决定调用哪个分析服务
 * <p>
 * 所以路径非常重要，路径代表了业务标识
 * 
 * @author wujian
 * @created on Jun 5, 2014 3:57:50 PM
 */
public class ConsoleServlet extends HttpServlet {

	private static final long serialVersionUID = 1667264240169419172L;

	private static final AppContext appContext = AppContext.getInstance();

	@Override
	public void service(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		// servlet map is /data/
		User user = new UserDaoImpl().queryByUid(6);
		String pathInfo = request.getServletPath();

		String id = request.getParameter("id");
		ResultDTO result = new ResultDTO();
		result.error = PortalCST.SUCESS;

		// 返回指定用户的详细信息
		if (pathInfo.equals("/user/show")) {
			if (!Utility.isNullOrEmpty(id)) {

				String plat = request.getParameter("platform");
				Platform platform = Platform.UnKnow;
				if (plat.equalsIgnoreCase("sina")) {
					platform = Platform.Sina;
				}
				if (plat.equalsIgnoreCase("tencent")) {
					platform = Platform.Tencent;
				}

				if (plat == "renmin") {
					platform = Platform.Renmin;
				}

				String byName = request.getParameter("byname");
				AppAuth auth = appContext.getCustomManager()
						.getAuthByDefaultStrategy(user, platform);
				if (byName.equals("true")) {

					IAccount account = appContext.getAccountManager()
							.accountGetByName(id, platform);
					result.data = account.ToJson();
				} else {
					// Account account = appContext.getAccountManager()
					// .accountGetByUidOnline(id, platform, user, auth);

					Result<IAccount> account = appContext.getAccountManager()
							.accountGetByUcodeOnline(id, platform, user, auth);
					result.data = Utility.toJSON(account);
				}

			}
		}

		// 下面写其他的服务

		String data = new Gson().toJson(result);

		response.setCharacterEncoding("UTF-8");
		try {
			PrintWriter writer = response.getWriter();

			writer.print(data);

			writer.flush();
			writer.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

}
