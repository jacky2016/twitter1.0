package com.xunku.app.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.xunku.app.model.TweetStatus;
import com.xunku.app.model.TweetUrl;
import com.xunku.app.parser.TweetUrlParserFactory;
import com.xunku.dao.monitor.MWeiBoDao;
import com.xunku.dao.office.WeiboWarnDao;
import com.xunku.dao.task.CollectionDao;
import com.xunku.daoImpl.monitor.MWeiBoDaoImpl;
import com.xunku.daoImpl.office.WeiboWarnDaoImpl;
import com.xunku.daoImpl.task.CollectionDaoImpl;

/**
 * 收集管理器（收藏夹）
 * 
 * @author wujian
 * @created on Jul 14, 2014 3:10:35 PM
 */
public class FavoriteManager {

	CollectionDao _collectionDAO;
	MWeiBoDao _mWeiboDAO;
	WeiboWarnDao _weiboWarnDAO;
	static FavoriteManager _manager;

	public synchronized static FavoriteManager getInstance() {
		if (_manager == null) {
			// 这里应该从配置文件里面读取策略和监听器
			_manager = new FavoriteManager();
		}
		return _manager;
	}

	private FavoriteManager() {
		this._collectionDAO = new CollectionDaoImpl();
		this._mWeiboDAO = new MWeiBoDaoImpl();
		this._weiboWarnDAO = new WeiboWarnDaoImpl();
	}

	public Map<String, String[]> getTweetStatus4Warn(List<String> urls,
			int customid) {
		Map<String, String> mapping = new HashMap<String, String>();
		List<String> tids = this.parseTids(urls, mapping);

		if (tids != null) {
			Map<String, String[]> warnings = this._getWeiboWarnings(customid,
					tids);
			Map<String, String[]> result = new HashMap<String, String[]>();
			for (Map.Entry<String, String[]> e : warnings.entrySet()) {
				result.put(mapping.get(e.getKey()), e.getValue());
			}

			return warnings;
		}
		return null;
	}

	/**
	 * 根据一组url获取这组url是否已经被收集，如果已经收集则value保存的是收集id
	 * 
	 * @param urls
	 * @return
	 */
	public Map<String, TweetStatus> getTweetStatus(List<String> urls,
			int customid) {

		Map<String, String> mapping = new HashMap<String, String>();
		List<String> tids = this.parseTids(urls, mapping);

		if (tids != null) {

			Map<String, Integer> collects = this
					._getCollections(tids, customid);
			Map<String, Integer> monitors = this._getMonitors(tids, customid);

			Map<String, TweetStatus> result = new HashMap<String, TweetStatus>();
			for (Map.Entry<String, Integer> e : collects.entrySet()) {
				TweetStatus status = new TweetStatus();
				status.setCollectID(e.getValue());
				status.setMoniterID(monitors.get(e.getKey()));
				// status.setWarningID(warnings.get(e.getKey()));
				// 从映射里面得到tid对应的原始key
				String key = mapping.get(e.getKey());
				result.put(key, status);
			}
			return result;
		}
		return null;
	}

	private List<String> parseTids(List<String> inputStr,
			Map<String, String> mapping) {
		if (inputStr != null && inputStr.size() > 0) {
			String firstEl = inputStr.get(0);

			if (firstEl.startsWith("http")) {
				// 传的是url
				List<String> tids = new ArrayList<String>();
				for (String u : inputStr) {
					TweetUrl url = TweetUrlParserFactory.createTweetUrl(u);
					tids.add(url.getTid());
					mapping.put(url.getTid(), u);
				}
				return tids;
			} else {
				// 传的是tid
				for (String u : inputStr) {
					mapping.put(u, u);
				}
			}
			return inputStr;
		}
		return null;
	}

	private Map<String, Integer> _getCollections(List<String> tids, int customid) {
		return this._collectionDAO.checkCollectionPostStatus(tids, customid);
	}

	private Map<String, Integer> _getMonitors(List<String> tids, int customid) {
		return this._mWeiboDAO.checkWeiboList(tids, customid);
	}

	private Map<String, String[]> _getWeiboWarnings(int customid,
			List<String> tids) {
		return this._weiboWarnDAO.checkWarn(customid, tids);
	}
}
