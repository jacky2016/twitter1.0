package com.xunku.actions.pubSentiment;

import com.xunku.actions.ActionBase;
import com.xunku.app.Utility;
import com.xunku.app.enums.Platform;
import com.xunku.app.interfaces.ITweet;
import com.xunku.app.result.Result;
import com.xunku.constant.PortalCST;
import com.xunku.dao.monitor.MWeiBoDao;
import com.xunku.dao.task.CollectionDao;
import com.xunku.daoImpl.monitor.MWeiBoDaoImpl;
import com.xunku.daoImpl.task.CollectionDaoImpl;
import com.xunku.pojo.base.Post;
import com.xunku.pojo.base.User;
import com.xunku.pojo.task.Collection;
import com.xunku.portal.controller.PermissionController;
import com.xunku.utils.AppServerProxy;

/*
 * 舆情展示模块（其他模块的收集也用此action），收集与取消收集
 * @author sunao
 */
public class CollectInsertAndDeleteAction extends ActionBase {
	CollectionDao collectionDao = new CollectionDaoImpl();

	@Override
	public Integer doAction() {

		String methodName = this.get("methodName");

		// 收集
		if (methodName.equals("save")) {
			String collectType = this.get("collectType"); // 从哪个模块收集，0：我的首页进入收集，1：舆情展示进入收集
			String twitterid = this.get("twitterid"); // 微博id
			String _platform = this.get("platform"); // 媒体平台
			String groupid = this.get("groupid"); // 收集组id
			String url = this.get("url"); // url

			// Platform platform =
			// Utility.getPlatform(Integer.parseInt(_platform));

			return InsertCollect(Integer.parseInt(collectType), twitterid,
					Integer.parseInt(_platform), Integer.parseInt(groupid), url);
			// 取消收集
		} else {
			String collectid = this.get("collectid"); // 收集id
			return DeleteCollect(Integer.parseInt(collectid));
		}
	}

	// 取消收集
	private int DeleteCollect(int collectid) {
		User user = this.getUser().getBaseUser();
		// 删除数据库1:成功，0:数据库失败,-1:不能删除，在已处理和忽略里了
		boolean isDelete = collectionDao.checkIsDealStatus(user.getCustomID(),
				collectid);
		if (!isDelete) {
			Boolean flg = collectionDao.deleteByCid(collectid);
			if (flg) {
				return 1;
			} else {
				return 0;
			}
		} else {
			return -1;
		}

	}

	// 收集
	private int InsertCollect(int collectType, String twitterid, int platform,
			int groupid, String url) {
		User user = this.getUser().getBaseUser();

		// 根据不同的模块调用不同的获取Post对象的方法
		ITweet post = null;
		// 舆情展示模块收集，调用API服务
		if (collectType == 1) {
			Result<ITweet> result = AppServerProxy.getPostByUrl(url, user);
			if (result.getErrCode() == 0) {
				post = result.getData();
			}

			// 我的首页块收集，调用数据库服务
		} else {
			Result<ITweet> result = AppServerProxy.getPostByTid(twitterid,
					Utility.getPlatform(platform), user);
			if (result.getErrCode() == 0) {
				post = result.getData();
			}
		}

		if (post == null) {
			return 0;
		}

		Collection collection = new Collection();
		collection.setCreator(user.getId());
		collection.setGroupID(groupid);
		collection.setPost(post);
		collection.setStatus(0); // 默认待处理状态
		int i = collectionDao.insert(collection);
		// 0:插入失败，-1:存在：>0:成功, -2:到达上限
		return i;
	}
}
