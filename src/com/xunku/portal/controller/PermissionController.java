package com.xunku.portal.controller;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Hashtable;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xunku.ObjectModel.AuthorityItem;
import com.xunku.ObjectModel.AuthorityType;
import com.xunku.ObjectModel.CustomCache;
import com.xunku.app.model.UserInfo;
import com.xunku.dao.base.AuthorityDao;
import com.xunku.dao.base.CustomRoleDao;
import com.xunku.dao.base.UserDao;
import com.xunku.daoImpl.base.AuthorityDaoImpl;
import com.xunku.daoImpl.base.CustomRoleDaoImpl;
import com.xunku.daoImpl.base.UserDaoImpl;
import com.xunku.pojo.base.PermissionInRole;
import com.xunku.cache.*;
import com.xunku.constant.*;

// 权限控制器
@SuppressWarnings("unchecked")
public class PermissionController {
	private static final Logger loger = LoggerFactory
			.getLogger(PermissionController.class);
	private final static Object obj_lock = new Object();

	public static void initMeta() {
		initCustom_Config();// 加载客户配置信息元数据
		initAuthorityMeta();// 加载权限元数据
		initPimIDHtmlMeta();// 加载UI权限Html模板元数据
	}

	private static void initCustom_Config() {
		if (CacheManager.getCacheInfo(PortalCST.Base_CustomConfigCacheKey) == null) {
			synchronized (obj_lock) {
				if (CacheManager
						.getCacheInfo(PortalCST.Base_CustomConfigCacheKey) == null) {
					AuthorityDao sv = new AuthorityDaoImpl();
					Map<String, Integer> hash = sv.GetBase_Custom_Config();
					com.xunku.cache.Cache c = new com.xunku.cache.Cache(
							PortalCST.Base_CustomConfigCacheKey, hash,
							365 * 24 * 60 * 1000, false);
					CacheManager.putCache(PortalCST.Base_CustomConfigCacheKey,
							c);
				}
			}
		}
	}

	private static void initPimIDHtmlMeta() {
		if (CacheManager.getCacheInfo(PortalCST.PmiIDHtmlCacheKey) == null) {
			synchronized (obj_lock) {
				if (CacheManager.getCacheInfo(PortalCST.PmiIDHtmlCacheKey) == null) {
					AuthorityDao sv = new AuthorityDaoImpl();
					Map hash = sv.GetAllPmiHtml();
					com.xunku.cache.Cache c = new com.xunku.cache.Cache(
							PortalCST.PmiIDHtmlCacheKey, hash,
							365 * 24 * 60 * 1000, false);
					CacheManager.putCache(PortalCST.PmiIDHtmlCacheKey, c);
				}
			}
		}
	}

	private static void initAuthorityMeta() {
		if (CacheManager.getCacheInfo(PortalCST.AuthorityMetaCacheKey) == null) {
			synchronized (obj_lock) {
				if (CacheManager.getCacheInfo(PortalCST.AuthorityMetaCacheKey) == null) {
					AuthorityDao sv = new AuthorityDaoImpl();
					List<AuthorityItem> items = sv.GetAllAuthorityCode();
					Hashtable hash = new Hashtable();
					for (AuthorityItem item : items) {
						hash.put(item.getCode(), "");
					}
					com.xunku.cache.Cache c = new com.xunku.cache.Cache(
							PortalCST.AuthorityMetaCacheKey, hash,
							365 * 24 * 60 * 1000, false);
					CacheManager.putCache(PortalCST.AuthorityMetaCacheKey, c);
				}
			}
		}
	}

	private void initCustomProfile(int customID, String cacheKey) {
		AuthorityDao sv = new AuthorityDaoImpl();
		Map<Integer, Integer> ps = sv.GetBase_Custom_Profile(customID);
		((CustomCache) CacheManager.getCacheInfo(cacheKey).getValue())
				.setPs(ps);
	}

	private void initPmi(UserInfo user) {
		if (user.getPermissions() == null) {
			AuthorityDao sv = new AuthorityDaoImpl();
			com.xunku.pojo.base.User _user = new UserDaoImpl().queryAdmin(user
					.getBaseUser().getCustomID());
			UserInfo userNew = new UserInfo(_user);// 初始一个新的adminuser
			List<AuthorityItem> menus = null;
			List<AuthorityItem> uis = null;
			List<AuthorityItem> actions = null;
			if (user.isAdmin()) {
				menus = sv.GetAuthority(user.getBaseUser().getId(),
						AuthorityType.menu, user.isAdmin());
				uis = sv.GetAuthority(user.getBaseUser().getId(),
						AuthorityType.ui, user.isAdmin());
				actions = sv.GetAuthority(user.getBaseUser().getId(),
						AuthorityType.action, user.isAdmin());
			} else {
				menus = sv.GetAuthority(userNew.getBaseUser().getId(),
						AuthorityType.menu, userNew.isAdmin());
				uis = sv.GetAuthority(userNew.getBaseUser().getId(),
						AuthorityType.ui, userNew.isAdmin());
				actions = sv.GetAuthority(userNew.getBaseUser().getId(),
						AuthorityType.action, userNew.isAdmin());
			}

			Hashtable<AuthorityType, Hashtable<String, Boolean>> hash = new Hashtable<AuthorityType, Hashtable<String, Boolean>>();
			user.setPermissions(hash);
			// 不是管理员
			if (!user.isAdmin()) {
				List<AuthorityItem> margeMenu = new ArrayList<AuthorityItem>();
				List<AuthorityItem> margeUI = new ArrayList<AuthorityItem>();

				UserDao userDao = new UserDaoImpl();

				// 当前用户id获取用户角色id
				int roleid = userDao.queryByUserid(user.getBaseUser().getId())
						.getRole().getCustomId();
				CustomRoleDao dao = new CustomRoleDaoImpl();
				List<PermissionInRole> rolelist = dao.queryByRoleID(roleid);

				// marge menu
				for (AuthorityItem authorityItem : menus) {
					for (PermissionInRole roleItem : rolelist) {
						if (authorityItem.getCode().equals(roleItem.getMcode())) {
							margeMenu.add(authorityItem);
						}
						if (authorityItem.getCode().equals(roleItem.getCode())) {
							margeMenu.add(authorityItem);
						}
					
					}
				}
				
				for (AuthorityItem authorityItem : uis){
					for (PermissionInRole roleItem : rolelist) {
						if (authorityItem.getCode()
								.equals(roleItem.getUicode())) {
							margeUI.add(authorityItem);
							break;
						}
					}
				}

				AppendPmi(user, AuthorityType.menu, margeMenu);
				AppendPmi(user, AuthorityType.ui, margeUI);
				AppendPmi(user, AuthorityType.action, actions);
			} else {

				AppendPmi(user, AuthorityType.menu, menus);
				AppendPmi(user, AuthorityType.ui, uis);
				AppendPmi(user, AuthorityType.action, actions);
			}

		}
	}

	private void AppendPmi(UserInfo user, AuthorityType t,
			List<AuthorityItem> items) {
		if (user.getPermissions().get(t) == null) {
			Hashtable<String, Boolean> hash = new Hashtable<String, Boolean>();

			for (AuthorityItem item : items) {
				if (!hash.containsKey(item.getCode()))
					hash.put(item.getCode(), true);
			}
			user.getPermissions().put(t, hash);
		}
	}

	public PermissionController(UserInfo user) {
		// 加个锁
		if (user.getPermissions() == null) {
			synchronized (obj_lock) {
				if (user.getPermissions() == null) {
					initPmi(user);
				}
			}
		}
		int customId = user.getBaseUser().getCustomID();
		String cacheKey = PortalCST.customCacheKeyString.replace("{0}", String
				.valueOf(customId));

		if (((CustomCache) CacheManager.getCacheInfo(cacheKey).getValue())
				.getPs() == null) {
			synchronized (obj_lock) {
				if (((CustomCache) CacheManager.getCacheInfo(cacheKey)
						.getValue()).getPs() == null) {
					initCustomProfile(customId, cacheKey);
				}
			}
		}
	}

	// 获取某个客户配置信息的值

	public int GetCustomConfigValue(UserInfo user, String configName) {
		Map<String, Integer> t = (Map<String, Integer>) CacheManager
				.getCacheInfo(PortalCST.Base_CustomConfigCacheKey).getValue();

		int confid = t.get(configName);
		int customId = user.getBaseUser().getCustomID();
		String cacheKey = PortalCST.customCacheKeyString.replace("{0}", String
				.valueOf(customId));

		Map<Integer, Integer> map = ((CustomCache) CacheManager.getCacheInfo(
				cacheKey).getValue()).getPs();
		if (!map.containsKey(confid)) {
			return 0;
		} else {
			return map.get(confid);
		}
	}

	// 判断是否有菜单权限
	public boolean HasMenuAuthority(UserInfo user, String menuCode) {
		return user.getPermissions().get(AuthorityType.menu).containsKey(
				menuCode);
	}

	// 判断是否有UI权限
	public boolean HasUIAuthority(UserInfo user, String uiCode) {
		Hashtable t = (Hashtable) CacheManager.getCacheInfo(
				PortalCST.AuthorityMetaCacheKey).getValue();
		if (!t.containsKey(uiCode))
			return true;
		return user.getPermissions().get(AuthorityType.ui).containsKey(uiCode);
	}

	// 判断是否有Action权限
	public boolean HasActionAuthority(UserInfo user, String actionCode) {
		Hashtable t = (Hashtable) CacheManager.getCacheInfo(
				PortalCST.AuthorityMetaCacheKey).getValue();
		if (!t.containsKey(actionCode))
			return true;
		return user.getPermissions().get(AuthorityType.action).containsKey(
				actionCode);
	}

	// 获取用户的PmiID，Html模板
	public Map GetPmiIDHtml(UserInfo user) {
		try {
			Map t = (Map) CacheManager
					.getCacheInfo(PortalCST.PmiIDHtmlCacheKey).getValue();
			Map<String, String> u = new HashMap<String, String>();
			Set set = t.keySet();
			for (Iterator iter = set.iterator(); iter.hasNext();) {
				String key = (String) iter.next();
				String[] values = (String[]) t.get(key);
				if (values[1].length() > 0)
					u.put(key, values[1]);
				if (HasUIAuthority(user, key.substring(0, key.lastIndexOf('_')))) {
					u.put(key, values[0]);
				}
			}
			return u;
		} catch (Exception e) {
			// TODO: handle exception
			loger.error(e.getMessage());
			loger.error(e.getStackTrace().toString());
			return null;
		}

	}
}
