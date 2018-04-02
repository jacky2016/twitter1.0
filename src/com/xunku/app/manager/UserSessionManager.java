package com.xunku.app.manager;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.xunku.app.Utility;
import com.xunku.app.controller.UserController;
import com.xunku.app.enums.LoginStatus;
import com.xunku.app.interfaces.IEliminatedStrategy;
import com.xunku.app.interfaces.IUserSessionListener;
import com.xunku.app.listeners.UserSessionListener;
import com.xunku.app.model.LoginResult;
import com.xunku.app.model.UserInfo;
import com.xunku.app.model.UserSession;
import com.xunku.app.model.UserSessionEvent;
import com.xunku.app.strategy.DefaultSessionStrategy;
import com.xunku.constant.PortalCST;
import com.xunku.constant.SessionCST;
import com.xunku.dao.base.CustomDao;
import com.xunku.dao.base.UserDao;
import com.xunku.daoImpl.base.CustomDaoImpl;
import com.xunku.daoImpl.base.UserDaoImpl;
import com.xunku.pojo.base.Custom;
import com.xunku.pojo.base.User;
import com.xunku.utils.PropertiesUtils;

/**
 * 用户会话管理器，用来管理用户的登录状态
 * 
 * @author wujian
 * @created on Jul 10, 2014 11:46:46 AM
 */
public class UserSessionManager {

	private static final Logger LOG = LoggerFactory
			.getLogger(UserSessionManager.class);

	final int ONE_MIN = 60 * 1000;
	private Map<String, UserSession> _sessions;
	IEliminatedStrategy _strategy;
	UserDao _userDAO;
	CustomDao _customDAO;
	static UserSessionManager _manager;
	private List<IUserSessionListener> listeners;

	long lastCheckTime;

	int session_expired = 20;
	int session_recycle = 5;

	/**
	 * Session过期时间
	 * 
	 * @return
	 */
	public int getExpried() {
		return this.session_expired;
	}

	public synchronized static UserSessionManager getInstance() {
		if (_manager == null) {
			// 这里应该从配置文件里面读取策略和监听器
			_manager = new UserSessionManager(new DefaultSessionStrategy());
			IUserSessionListener listener = new UserSessionListener();
			_manager.addListener(listener);
		}
		return _manager;
	}

	public void load() {
		// 从会话表里读取数据，装载到userList;

		// 目前是全部失效的策略，这里不再装载会话
	}

	public void addListener(IUserSessionListener listener) {
		this.listeners.add(listener);
	}

	/**
	 * 移除Session
	 * 
	 * @param session
	 */
	public void remove(UserSession session) {
		this._sessions.remove(session.getClientID());
	}

	private UserSessionManager(IEliminatedStrategy strategy) {
		this._sessions = new HashMap<String, UserSession>();
		this._strategy = strategy;
		this.listeners = new ArrayList<IUserSessionListener>();

		// 最后一次检查Session的时间
		this.lastCheckTime = System.currentTimeMillis();
		this._userDAO = new UserDaoImpl();
		this._customDAO = new CustomDaoImpl();

		try {
			this.session_recycle = Integer.parseInt(PropertiesUtils.getString(
					"config", "portal.session.recycle"))
					* ONE_MIN;
			this.session_expired = Integer.parseInt(PropertiesUtils.getString(
					"config", "portal.session.expired"))
					* ONE_MIN;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private boolean needRecycle() {
		return System.currentTimeMillis() - (this.session_recycle) > this.lastCheckTime;
	}

	/**
	 * 垃圾回收
	 */
	public void recycle() {
		if (this.needRecycle()) {
			// LOG.info("开始回收超时的帐号！");
			synchronized (this) {
				List<String> removedIndexs = new ArrayList<String>();
				// 搜集看看有哪些Session过期了
				for (Map.Entry<String, UserSession> e : this._sessions
						.entrySet()) {
					UserSession session = e.getValue();
					if (session != null) {
						if (session.isExpire()) {
							// Session失效时调用
							removedIndexs.add(e.getKey());
							LOG.info("准备回收->" + e.getKey());
						}
					}
				}

				// for test
				for (Map.Entry<String, UserSession> e : this._sessions
						.entrySet()) {
					System.out.println(e.getValue());
				}

				// 从Session管理器中移除
				for (int i = 0; i < removedIndexs.size(); i++) {
					UserSession session = this._sessions.get(removedIndexs
							.get(i));
					if (session != null) {
						this.fireSessionDestroyedEvent(session);
						session.invalidate();
						session = null;
					}
				}
			}
		}
	}

	/**
	 * 通过当前的请求获得当前用户的会话，没有则创建一个
	 * 
	 * @param request
	 * @return
	 */
	public UserSession getSession(HttpServletRequest request) {
		String clientId = this.getClient(request);
		return this.getSession(clientId);
	}

	/**
	 * 获得指定用户已经登录的会话列表
	 * 
	 * @param userid
	 * @return
	 */
	public List<UserSession> getLoginSession(int userid) {
		List<UserSession> result = new ArrayList<UserSession>();
		for (UserSession s : this._sessions.values()) {
			if (s.getUser() != null && s.isLogined()) {
				result.add(s);
			}
		}
		return result;
	}

	/**
	 * 获得当前请求的用户信息
	 * 
	 * @param request
	 * @return
	 */
	public UserInfo getUserInfo(HttpServletRequest request) {
		String clientId = this.getClient(request);

		return this.getUser(clientId);
	}

	/**
	 * 获得当前请求的用户信息
	 * 
	 * @param clientID
	 * @return
	 */
	public UserInfo getUserInfo(String clientID) {
		return this.getUser(clientID);
	}

	/**
	 * 获得当前客户端登录的次数
	 * 
	 * @param clientId
	 * @return
	 */
	public int getLoginTimes(String clientId) {
		UserController userController = new UserController();
		return userController.getLoginTimes(clientId);
	}

	/**
	 * 获得当前用户登录的次数
	 * 
	 * @param request
	 * @return
	 */
	public int getLoginTimes(HttpServletRequest request) {
		return this.getLoginTimes(this.getClient(request));
	}

	/**
	 * 
	 * 登录系统
	 * 
	 * @param username
	 *            用户名
	 * @param password
	 *            密码
	 * @param isVode
	 *            是否需要验证vocode
	 * @param vode
	 *            要验证的vcode
	 * @param inputVode
	 *            输入的vcode
	 * @param times
	 *            尝试的次数
	 * @return
	 */
	public LoginResult login(HttpServletRequest request) {

		String clientId = this.getClient(request);
		String username = request.getParameter("un");
		String password = request.getParameter("pwd");
		String inputVode = request.getParameter("v");

		int currTimes = this.getLoginTimes(clientId);
		boolean isVode = currTimes > 3;
		String vode = this.getVode(request);

		LOG.info("开始登录，登录客户地址{},客户标识:{}", request.getRemoteAddr(), clientId);
		LoginResult result = null;
		int sysTimes = 10;// 系统允许登录的最大次数，这个值应该存数据库或者配置里面读取
		// TODO 这里从数据库获得该用户的登录次数

		UserController controller = new UserController();
		controller.addLoginTimes(clientId);

		if (currTimes > sysTimes) {
			// TODO 锁定帐号，请联系管理员
			result = LoginResult.getUserLocked();
		}
		if (!isVode) {
			result = login(clientId, username, password);
		} else {
			if (vode == null) {
				result = LoginResult.getErrVode();
			} else {
				if (!vode.equalsIgnoreCase(inputVode)) {
					result = LoginResult.getErrVode();
				} else {
					result = login(clientId, username, password);
				}
			}
		}

		if (result.getStatus() == LoginStatus.SUCESS) {
			controller.clearLoginTimes(clientId);
			currTimes = 0;
		}

		if (result != null) {
			result.setTimes(currTimes);
		}

		return result;
	}

	/**
	 * @param request
	 * @param response
	 */
	public boolean Logout(HttpServletRequest request,
			HttpServletResponse response) {

		UserSession session = this.getSession(request);
		this.fireUserRemovedEvent(session.getUser());

		LOG.info("注销帐号:{}", session.getUser().getBaseUser());
		session.invalidate();

		Cookie ckUsername, ckSessionid;
		ckUsername = new Cookie(SessionCST.LOGIN_USERNAME, null);
		ckUsername.setMaxAge(0);
		response.addCookie(ckUsername);

		ckSessionid = new Cookie(SessionCST.LOGIN_TOKEN, null);
		ckSessionid.setMaxAge(0);
		response.addCookie(ckSessionid);

		// 如果有其他的清理逻辑则在这里写

		session = null;
		return true;

	}

	/**
	 * 随机生成一个客户端标识
	 * 
	 * @return
	 */
	public void setClientID(HttpServletRequest request,
			HttpServletResponse response) {
		String clientId = this.getClientID(request);
		if (Utility.isNullOrEmpty(clientId)) {
			clientId = Utility.genClientID();
			Cookie clientCookie = new Cookie(SessionCST.COOKIE_CLIENT_ID,
					clientId);
			clientCookie.setMaxAge(60 * 60 * 24 * 700);// 保存700天...
			response.addCookie(clientCookie);
		}
	}

	public String getClientID(HttpServletRequest request) {
		// 检查请求里有没有clientID
		Cookie[] cookies = request.getCookies();
		return this.getCookie(cookies, SessionCST.COOKIE_CLIENT_ID);
	}

	/**
	 * 记住我登录信息两周吧
	 * 
	 * @param request
	 * @param response
	 * @param user
	 */
	public void remeberMe(HttpServletRequest request,
			HttpServletResponse response, User user) {
		Cookie ckUsername, ckSessionid;
		ckUsername = new Cookie(SessionCST.LOGIN_USERNAME, user.getUserName());
		ckUsername.setMaxAge(SessionCST.TWO_WEEKS); // 设置Cookie有效期为14天
		response.addCookie(ckUsername);

		ckSessionid = new Cookie(SessionCST.LOGIN_TOKEN, user.getToken());
		ckSessionid.setMaxAge(SessionCST.TWO_WEEKS);
		response.addCookie(ckSessionid);
	}

	/**
	 * 只验证用户名密码的登录过程
	 * 
	 * @param username
	 * @param password
	 * @return
	 */
	public LoginResult login(String clientId, String username, String password) {
		return loginWithToken(clientId, username, Utility.MD5(password));
	}

	public LoginResult loginWithToken(String clientId, String username,
			String token) {

		if (Utility.isNullOrEmpty(username) || Utility.isNullOrEmpty(token)) {
			return LoginResult.getErrUserName();
		}

		User user = _userDAO.getUserByName(username);

		if (user == null) {
			return LoginResult.getErrUserName();
		}
		if (user.getToken().equalsIgnoreCase(token)) {
			UserInfo info = new UserInfo(user);

			int cid = info.getBaseUser().getCustomID();

			Custom custom = CustomManager.getInstance().getCustom(cid);

			if (!custom.isEnabled()) {
				return LoginResult.getCustomInvaild();
			}

			if (custom.getExpirationDate().getTime() < System
					.currentTimeMillis()) {
				return LoginResult.getCustomExpired();
			}

			int cnt = this.onlineCnt(username);
			if (cnt >= 2) {
				return LoginResult.getTooManyOnline();
			}
			info.setCustom(custom);

			this.fireUserCreatedEvent(info);

			LoginResult result = LoginResult.get(info);
			UserSession session = this.getSession(clientId);
			session.setUser(result.getUserInfo());
			
			return result;
		} else {
			return LoginResult.getErrUserName();
		}
	}

	/**
	 * 获得当前这个用户有几个在线
	 * 
	 * @param username
	 * @return
	 */
	private int onlineCnt(String username) {
		int result = 0;
		for (UserSession session : this._sessions.values()) {
			UserInfo user = session.getUser();
			if (user != null) {
				if (user.getBaseUser().getUserName().equals(username)) {
					result++;
				}
			}
		}
		return result;
	}

	/**
	 * 从当前的请求的cookie里获得登录的用户名
	 * 
	 * @param request
	 * @return
	 */
	public String getUserNameFromCookie(HttpServletRequest request) {
		return this.getCookie(request.getCookies(), SessionCST.LOGIN_USERNAME);
	}

	/**
	 * 从当前请求的cookie里面获得登录的Token
	 * 
	 * @param request
	 * @return
	 */
	public String getUserTokenFromCookie(HttpServletRequest request) {
		return this.getCookie(request.getCookies(), SessionCST.LOGIN_TOKEN);
	}

	/**
	 * 获得和当前请求相关的验证码
	 * 
	 * @param request
	 * @return
	 */
	public String getVode(HttpServletRequest request) {
		UserSession session = this.getSession(this.getClient(request));

		if (session != null) {
			return session.getVcode();
		}

		return null;
	}

	/**
	 * 检查当前请求是否已经登录
	 * 
	 * @param request
	 * @return
	 */
	public boolean needLogin(HttpServletRequest request) {
		UserSession session = this.getSession(this.getClient(request));
		if (session == null)
			return true;
		else {
			return !session.isLogined();
		}
	}

	String getToken(String password) {
		if (Utility.isNullOrEmpty(password)) {
			return PortalCST.PASSWORD_TOKEN_EMPTY;
		}
		return Utility.MD5(password);
	}

	String getClient(HttpServletRequest request) {
		return this
				.getCookie(request.getCookies(), SessionCST.COOKIE_CLIENT_ID);
	}

	String getCookie(Cookie[] cookies, String cookieName) {
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals(cookieName)) {
					return cookie.getValue();
				}
			}
		}
		return null;
	}

	/**
	 * 获得当前客户端对应的用户
	 * 
	 * @param clientId
	 * @return
	 */
	UserInfo getUser(String clientId) {
		UserSession session = this.getSession(clientId);
		if (session != null)
			return this.getSession(clientId).getUser();
		return null;
	}

	/**
	 * 获得当前客户端的会话对象，如果没有则创建一个
	 * 
	 * @param clientId
	 * @return
	 */
	UserSession getSession(String clientId) {
		UserSession session = this._sessions.get(clientId);
		if (session == null) {
			session = new UserSession(this);
			session.setCreated(System.currentTimeMillis());
			session.setClientID(clientId);
			this._sessions.put(clientId, session);
		}
		session.access();
		return session;
	}

	/**
	 * 登录成功创建用户时触发
	 * 
	 * @param session
	 */
	void fireUserCreatedEvent(UserInfo user) {
		for (IUserSessionListener li : this.listeners) {
			li.userCreated(user);
		}
	}

	/**
	 * 注销用户是触发
	 * 
	 * @param session
	 */
	void fireUserRemovedEvent(UserInfo user) {
		for (IUserSessionListener li : this.listeners) {
			li.userRemoved(user);
		}
	}

	/**
	 * 销毁Session时触发
	 * 
	 * @param session
	 */
	void fireSessionDestroyedEvent(UserSession session) {
		for (IUserSessionListener li : this.listeners) {
			li.sessionDestroyed(new UserSessionEvent(session));
		}
	}

	public static void main(String[] args) {

	}

}
