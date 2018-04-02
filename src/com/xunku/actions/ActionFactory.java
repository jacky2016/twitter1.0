package com.xunku.actions;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xunku.cache.CacheManager;
import com.xunku.constant.PortalCST;
import com.xunku.dao.base.ActionDao;
import com.xunku.daoImpl.base.ActionDaoImpl;
import com.xunku.pojo.base.BaseAction;

/**
 * 业务处理器工厂
 * <p>
 * 该工厂将根据具体的ActionName来创建具体的Action对象，完成业务逻辑
 * 
 * @author wujian
 * @created on Jun 5, 2014 4:01:36 PM
 */
public class ActionFactory {

	private ActionFactory() {
	}

	public static final Logger LOG = LoggerFactory
			.getLogger(ActionFactory.class);
	private static ActionFactory _factory;
	private final HashMap<String, Class<?>> CACHE = new HashMap<String, Class<?>>();

	public static synchronized ActionFactory getInstance() {

		if (_factory == null) {
			_factory = new ActionFactory();
			_factory.Init();
		}

		return _factory;

	}

	private void Init() {
		if (_factory.CACHE.size() == 0) {
			ActionDao service = new ActionDaoImpl();

			List<BaseAction> list = service.queryBaseActions();
			Map<String, String[]> actionModule = new HashMap<String, String[]>();
			for (BaseAction act : list) {
				try {
					String clazzName = act.getClazz();
					_factory.CACHE.put(act.getCode(), Class.forName(clazzName));
					
					actionModule.put(act.getCode(), new String[] {
							act.getModuleCode(), clazzName });
				} catch (Exception ex) {
					LOG.error("初始化" + act.getClazz() + "时出错");
				}
			}
			// sunao 添加action到module的缓存
			com.xunku.cache.Cache c = new com.xunku.cache.Cache(
					PortalCST.ActionModuleCacheKey, actionModule,
					365 * 24 * 60 * 1000, false);
			CacheManager.putCache(PortalCST.ActionModuleCacheKey, c);
		}
	}

	public IAction getAction(String actionName) {

		// TODO 这里还有优化的余地么？
		Class<?> clazz = this.CACHE.get(actionName);
		if (clazz == null)
			return null;
		IAction action = null;
		try {
			action = (IAction) clazz.newInstance();
		} catch (InstantiationException e) {
			LOG.error(e.getMessage());
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			LOG.error(e.getMessage());
			e.printStackTrace();
		}

		return action;
	}

}
