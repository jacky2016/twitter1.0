package com.xunku.portal;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.xunku.app.enums.Platform;
import com.xunku.app.model.ApiToken;
import com.xunku.dao.base.AccountDao;
import com.xunku.dao.base.UserDao;
import com.xunku.daoImpl.base.AccountDaoImpl;
import com.xunku.daoImpl.base.UserDaoImpl;
import com.xunku.pojo.base.Account;
import com.xunku.pojo.base.AccountInfo;
import com.xunku.pojo.base.User;
/*
 * AccountServlet 授权代理
 * @author hjian
 */
public class AccountServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2526890896516268297L;

	/**
	 * Constructor of the object.
	 */
	public AccountServlet() {
		super();
	}

	/**
	 * Destruction of the servlet. <br>
	 */
	public void destroy() {
		super.destroy(); // Just puts "destroy" string in log
		// Put your code here
	}

	/**
	 * The doGet method of the servlet. <br>
	 * 
	 * This method is called when a form has its tag value method equals to get.
	 * 
	 * @param request
	 *            the request send by the client to the server
	 * @param response
	 *            the response send by the server to the client
	 * @throws ServletException
	 *             if an error occurred
	 * @throws IOException
	 *             if an error occurred
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html");
		request.setCharacterEncoding("UTF-8");
		String code = request.getParameter("code");
		String[] state = request.getParameter("state").split(",");
		String platformStr = state[0];
		int platform = Integer.valueOf(platformStr);
		int userid = Integer.valueOf(state[1]);
		UserDao dao1 = new UserDaoImpl();
		User user = dao1.queryByUid(userid);
		int customId = user.getCustomID();
		AccountDao dao = new AccountDaoImpl();
		AccountInfo ac = new AccountInfo();
		ac.setCustomId(customId);
		Platform p = com.xunku.app.Utility.getPlatform(platform);
		// 通过code获得token
		ApiToken apitoken = null;//AppServerProxy.getTokenByCode(code, p);
		String uid = apitoken.getUid();
		String token = apitoken.getToken();
		

		// 获得账户名称
		Account ana = null; //com.xunku.app.AppServer.getInstance().AccountGetByUid4RT(uid,p);
		ac.setCreator(customId);
		ac.setUcode(uid);
		ac.setPlatform(p);
		ac.setName(ana.getName());
		// insert
		//dao.insert(ac, userid);
		response.sendRedirect("back-back.html");
	}

	/**
	 * The doPost method of the servlet. <br>
	 * 
	 * This method is called when a form has its tag value method equals to
	 * post.
	 * 
	 * @param request
	 *            the request send by the client to the server
	 * @param response
	 *            the response send by the server to the client
	 * @throws ServletException
	 *             if an error occurred
	 * @throws IOException
	 *             if an error occurred
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		doGet(request, response);
	}

	/**
	 * Initialization of the servlet. <br>
	 * 
	 * @throws ServletException
	 *             if an error occurs
	 */
	public void init() throws ServletException {
		// Put your code here
	}

}
