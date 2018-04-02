package com.xunku.actions.spreadAnalysis;

import com.xunku.actions.ActionBase;
import com.xunku.app.AppContext;
import com.xunku.app.Utility;
import com.xunku.app.enums.PostType;
import com.xunku.app.enums.WordCloudEnum;
import com.xunku.app.interfaces.ITweet;
import com.xunku.app.manager.PoolManager;
import com.xunku.app.model.Pooling;
import com.xunku.app.monitor.WeiboMonitor;
import com.xunku.app.result.Result;
import com.xunku.app.result.SpreadResult;
import com.xunku.constant.PortalCST;
import com.xunku.dao.event.EventDao;
import com.xunku.dao.monitor.MWeiBoDao;
import com.xunku.daoImpl.event.EventDaoImpl;
import com.xunku.daoImpl.monitor.MWeiBoDaoImpl;
import com.xunku.pojo.base.Post;
import com.xunku.pojo.base.User;
import com.xunku.portal.controller.PermissionController;
import com.xunku.utils.AppServerProxy;

// 监控和取消监控
public class SpreadAnalysisInsertAction extends ActionBase {

	MWeiBoDao mWeiBoDao = new MWeiBoDaoImpl();

	@Override
	public Integer doAction() {
		String methodName = this.get("methodName");
		if (methodName.equals("save")) {
			return insert();
		} else {
			return delete();
		}
	}

	// 插入
	private Integer insert() {
		int state = 0; // -4:
		// 用户被封，-3：该贴类型不是原帖，不能分析，-2,:已经存在，-1：到达添加次数，限制提示，0：插入失败，数据库错误提示，>0：添加成功
		String url = this.get("url");
		int userid = this.getUser().getBaseUser().getId();
		int customid = this.getUser().getBaseUser().getCustomID();
		PermissionController controll = new PermissionController(this.getUser());
		int maxCount = controll.GetCustomConfigValue(this.getUser(),
				PortalCST.weibo_disseminate_count);
		int currentCount = mWeiBoDao.queryWeiboListTotal(customid);
		if (currentCount >= maxCount) {
			return -1;
		}
		if (url.indexOf("?") > -1) {
			url = url.substring(0, url.indexOf("?"));
		}
		boolean isExist = mWeiBoDao.checkUrlIsExists(url, customid);
		// modify by wujian
		User user = this.getUser().getBaseUser();
		if (!isExist) {
			// 如果不存在先让它存在
			Result<ITweet> result = AppServerProxy.getPostByUrl(url, user);
			if (result.getErrCode() == 0) {
				// 获得微博信息
				ITweet post = result.getData();
				if (post != null) {
					if (post.getType() == PostType.Creative) {
						// 添加监测项
						WeiboMonitor mw = WeiboMonitor.create(post, customid);

						int id = this.mWeiBoDao.insert(mw);
						if (id > 0) {
							state = id;
							addWordCloud(id);
							// 立刻分析结果（会执行远程调用来获得最新的数据）
							// AppServerProxy.anaylsisSpreadNow(user, id, post);
						}
					} else {
						state = -3;// 该条微博不是原帖
					}
				} else {
					state = 0;
				}
			} else {
				state = -4;
			}
		} else {
			state = -2;// 已经存在
		}
		return state;
	}

	private void addWordCloud(int id) {
		// add by wujian for wordcloud
		WeiboMonitor wm1 = this.mWeiBoDao.queryMWeiboById(id);
		EventDao dao = new EventDaoImpl();
		dao.addWordCloudTask(wm1.getMonitorId(), wm1.getPoolId(), wm1
				.getTableName(), WordCloudEnum.Spread);
	}

	// 删除
	private Integer delete() {
		int state = 0;// 0：删除失败，数据库错误提示，>0：删除成功
		String id = this.get("spreadid");
		User ueser = this.getUser().getBaseUser();
		Boolean flg = mWeiBoDao.deleteById(Integer.parseInt(id), ueser
				.getCustomID());
		if (flg) {
			state = 1;
		}
		return state;
	}
}
