package com.xunku.actions;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.xunku.app.model.UserInfo;
import com.xunku.cache.CacheManager;
import com.xunku.constant.PortalCST;
import com.xunku.dao.log.LogsDao;
import com.xunku.daoImpl.log.LogsImpl;
import com.xunku.dto.ErrorDTO;
import com.xunku.pojo.base.Custom;
import com.xunku.pojo.base.User;
import com.xunku.pojo.log.LogsEntity;
import com.xunku.portal.controller.ActionLogController;
import com.xunku.portal.controller.PermissionController;

/**
 * 业务逻辑代理
 * <p>
 * 负责为业务逻辑提供额外的检查功能，并且调用业务逻辑
 * 
 * @author wujian
 * @created on Jun 5, 2014 4:02:21 PM
 */
public class ActionProxy {

	private ActionContext _context;
	private IAction _action;
	private String _actionName;

	private static final Logger LOG = LoggerFactory
			.getLogger(ActionProxy.class);

	public ActionContext getContext() {
		return _context;
	}

	private UserInfo getUser() {
		UserInfo objUser = this._context.getSession().getUser();
		if (objUser != null) {
			return objUser;
		}
		return null;
	}

	public ActionProxy(ActionContext ctx) {

		this._context = ctx;

		_actionName = this._context.getActionName();

		if (_actionName != null) {
			_action = ActionFactory.getInstance().getAction(_actionName);

			if (_action != null) {
				_action.init(this._context);
			}
		}
	}

	public String executeAction() {

		// 查找Action的日志信息
		// 查找Action的权限信息
		ErrorDTO error = new ErrorDTO();
		error.code = 0;
		if (_action == null) {
			LOG.error("不可识别的 Action :[" + this._actionName + "].");
			error.err = "不可识别的 Action :[" + this._actionName + "].";
			return this.ToJSON(error);
		}

		ActionLogController alController = new ActionLogController();
		error.err = "未知的错误。";
		String result = "";

		UserInfo userInfo = this.getUser();

		if (userInfo == null) {
			error.err = "用户信息无效，请重新登录！";
			error.code = 1;
			return this.ToJSON(error);
		}

		if (alController.IsInLogger(_action)) {

			alController.ActionLogStart();

			PermissionController pController = new PermissionController(
					userInfo);

			// modify by tengsx
			if (pController.HasActionAuthority(userInfo, _action.getName())) {

				com.xunku.cache.Cache cache = CacheManager.getCacheInfo(PortalCST.ActionModuleCacheKey);
				Map<String,String[]> map = (Map<String,String[]>) cache.getValue(); 
				
				// sunao modify
				LogsDao logsDao = new LogsImpl();
				User user = userInfo.getBaseUser();
				Custom custom = userInfo.getCustom();
				LogsEntity logsEntity = new LogsEntity();
				logsEntity.setModuleCode(map.get(_actionName)[0]); // 目前没判断，可能引起错误
				logsEntity.setModuleClass(map.get(_actionName)[1]); // 目前没判断，可能引起错误
				logsEntity.setUserID(user.getId());
				logsEntity.setUserName(user.getUserName());
				logsEntity.setCustomID(user.getCustomID());
				logsEntity.setCustomName(custom.getName());
				logsEntity.setIp(getContext().getSession().getIp());
				logsEntity.setActionName(_actionName);

				String param = "";
				Map<String, String[]> params = getContext().getParameters();

				for (Map.Entry<String, String[]> entry : params.entrySet()) {
					String[] values = entry.getValue();
					String value;
					if ((values == null) || (values.length < 1)) {
						param = "name: " + entry.getKey() + ", value: 没传参数|";
					} else {
						param = "name: " + entry.getKey() + ", value: "
								+ values[0] + "|";
					}
				}
				param = "[" + param + "]";

				try {
					logsEntity.setState(1);
					logsEntity.setLogsContent("参数:" + param + ",访问成功");
					logsDao.insertLogs(logsEntity);

					Object dto = _action.doAction();
					result = this.ToJSON(dto);
				} catch (Exception ex) {
					error.err = "调用Aciont[" + _action.getName() + "]异常:"
							+ ex.getMessage();

					logsEntity.setState(0);
					logsEntity.setLogsContent("参数:" + param + ",访问失败 \r\n getMessage:"+ex.getMessage()
							+ "\r\n getStackTrace:" + ex.getStackTrace());
					logsDao.insertLogs(logsEntity);

					result = this.ToJSON(error);
				}
			} else {
				error.err = "访问拒绝.";
			}

			alController.ActionLogEnd();

		} else {
			error.err = "未注册日志不可调用.";
		}
		return result;

	}
 
	private String ToJSON(Object dto) {
		Gson gson = new Gson();
		return gson.toJson(dto);
	}

}
