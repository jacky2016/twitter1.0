package com.xunku.portal;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.xunku.app.Utility;
import com.xunku.app.controller.PeopleAPIController;
import com.xunku.app.controller.SysManagerController;
import com.xunku.app.enums.Platform;
import com.xunku.app.model.ApiToken;
import com.xunku.app.model.App;
import com.xunku.app.model.people.PUser;
import com.xunku.app.model.people.Pweet;
import com.xunku.dao.base.AccountAuthsDao;
import com.xunku.daoImpl.base.AccountAuthsDaoImpl;
import com.xunku.dto.sysManager.PeopleDTO;
import com.xunku.dto.sysManager.PeopleUser;
import com.xunku.pojo.base.User;
import com.xunku.utils.AppServerProxy;

/*******************************************************************************
 * 帐号授权
 * 
 * @author shaoqun
 * @created Oct 17, 2014
 */
public class AuthCallbackServlet extends HttpServlet {

	private static final long serialVersionUID = 8697947307739459933L;

	@Override
	public void service(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		String path = request.getServletPath();
		String[] state = request.getParameter("state").split(",");
		String strPlatform = state[0];
		String userid = state[1];
		String uid = state[2];
		String pass = state[3];
		String pid = state[4];

		Platform platform = Platform.UnKnow;

		if (!Utility.isNullOrEmpty(strPlatform)) {
			if (strPlatform.equalsIgnoreCase("1")) {
				platform = Platform.Sina;
			} else if (strPlatform.equalsIgnoreCase("2")) {
				platform = Platform.Tencent;
			} else if (strPlatform.equalsIgnoreCase("5")) {
				platform = Platform.Renmin;
			}
		}

		App app = AppServerProxy.getMainApp(platform);
		if (path.equalsIgnoreCase("/auth.action")) {
			// 绑定授权
			if (platform == Platform.Sina) {
				String code = request.getParameter("code");
				SysManagerController.getAuthor(code, app, platform, userid,
						uid, response);

			} else if (platform == Platform.Tencent) {
				String code = request.getParameter("code");
				ApiToken apitoken = AppServerProxy.getTokenByCode(code, app);
				
				System.out.println(apitoken+"---"+code);
				
			} else if (platform == Platform.Renmin) {
				response.setCharacterEncoding("UTF-8");
				PrintWriter writer = response.getWriter();
				AccountAuthsDao authDAO = new AccountAuthsDaoImpl();
				PeopleDTO people = new PeopleDTO();
				PeopleUser puser = new PeopleUser();
				String sjm = SysManagerController.GetRandCode();
				String username = uid, password = pass, message = "帐号 "
						+ username + " 进行人民微博授权绑定..." + sjm;
				/** *******************发布人民微博消息****************************** */
				PeopleAPIController controller = new PeopleAPIController();
				Pweet pweet = controller.sendTweet(username, password, message,
						null);

				int typenum = 0;
				if (pweet != null) {
					// boolean flag = authDAO.checkAccountBind(
					// Integer.parseInt(userid), username);
					boolean flag = authDAO.checkAccountBind(username);
					if (!flag) {
						people.setCustomId(Integer.parseInt(userid));
						people.setUserid(Integer.parseInt(pid));
						people.setUsername(username);
						people.setPassword(password);
						if (pweet.getUser() != null) {
							PUser user = pweet.getUser();
							puser.setPuserId(user.getUserId());
							puser.setNickName(user.getNickName());
							puser.setLocation(user.getLocation());
							puser.setGender(user.getGender());
							puser.setHeadUrl(user.getHeadUrl());
							puser.setPersonUrl(user.getPersonUrl());
							people.setUser(puser);
						}
						boolean tflag = authDAO.insertPeople(people);
						if (tflag) {
							typenum = 2;
						}
					} else {
						typenum = 1;
					}
				}
				writer.print(Utility.toJSON(typenum));
				writer.flush();
				writer.close();
			}

		}

		if (path.equalsIgnoreCase("/unauth.action")) {
			// 取消授权
		}
	}

}
