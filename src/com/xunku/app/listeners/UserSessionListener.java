package com.xunku.app.listeners;

import java.util.HashMap;
import java.util.Map;

import com.xunku.ObjectModel.CustomCache;
import com.xunku.app.interfaces.IUserSessionListener;
import com.xunku.app.model.UserInfo;
import com.xunku.app.model.UserSessionEvent;
import com.xunku.cache.Cache;
import com.xunku.cache.CacheManager;
import com.xunku.constant.PortalCST;
import com.xunku.pojo.base.User;

/**
 * 用户会话监听器实现
 * 
 * @author wujian
 * @created on Jul 11, 2014 3:48:58 PM
 */
public class UserSessionListener implements IUserSessionListener {

	@Override
	public void sessionDestroyed(UserSessionEvent event) {
		UserInfo info = event.getSession().getUser();
		if (info != null) {
			int customId = info.getBaseUser().getCustomID();
			int userId = info.getBaseUser().getId();
			String cacheKey = PortalCST.customCacheKeyString.replace("{0}",
					String.valueOf(customId));
			// 从custom-{ID}.value移除UserID
			Cache cache = CacheManager.getCacheInfo(cacheKey);
			if (cache != null) {
				CustomCache co = (CustomCache) cache.getValue();
				if (co.getUseMap().containsKey(userId)) {
					int v = co.getUseMap().get(userId);
					if (v <= 1)
						co.getUseMap().remove(userId);
					else
						co.getUseMap().put(userId, v - 1);
				}
				// 如果当前客户下没有用户 就移除该客户的缓存
				if (co.getUseMap().size() == 0) {
					CacheManager.clearOnly(cacheKey);
				}
			}
		}
	}

	@Override
	public void userCreated(UserInfo user) {
		User info = user.getBaseUser();
		if (info != null) {
			int customId = info.getCustomID();
			int userId = info.getId();
			// 获取该客户缓存的key
			String cacheKey = PortalCST.customCacheKeyString.replace("{0}",
					String.valueOf(customId));
			// 检查 CustomKey-{0} 是否存在
			Cache cache = CacheManager.getCacheInfo(cacheKey);
			if (cache == null) {
				// 如果不存在
				CustomCache co = new CustomCache();
				Map<Integer, Integer> useMap = new HashMap<Integer, Integer>();
				useMap.put(userId, 1);
				co.setUseMap(useMap);
				// modify sunao
				com.xunku.cache.Cache c = new com.xunku.cache.Cache(cacheKey,
						co, 365 * 24 * 60 * 1000, false);
				CacheManager.putCache(cacheKey, c);
			} else {
				// 如果已经存在
				CustomCache co = (CustomCache) cache.getValue();
				if (co.getUseMap().containsKey(userId)) {
					int v = co.getUseMap().get(userId);
					co.getUseMap().put(userId, v + 1);
				} else {
					co.getUseMap().put(userId, 1);
				}
				// set value
				cache.setValue(co);
			}
		}
	}

	@Override
	public void userRemoved(UserInfo user) {

		if (user == null) {
			return;
		}
		User info = user.getBaseUser();
		if (info != null) {
			int customId = info.getCustomID();
			int userId = info.getId();
			String cacheKey = PortalCST.customCacheKeyString.replace("{0}",
					String.valueOf(customId));
			// 从custom-{ID}.value移除UserID
			Cache cache = CacheManager.getCacheInfo(cacheKey);
			if (cache != null) {
				CustomCache co = (CustomCache) cache.getValue();
				if (co.getUseMap().containsKey(userId)) {
					int v = co.getUseMap().get(userId);
					if (v <= 1)
						co.getUseMap().remove(userId);
					else
						co.getUseMap().put(userId, v - 1);
				}
				// 如果当前客户下没有用户 就移除该客户的缓存
				if (co.getUseMap().size() == 0) {
					CacheManager.clearOnly(cacheKey);
				}
			}
		}
	}

}
