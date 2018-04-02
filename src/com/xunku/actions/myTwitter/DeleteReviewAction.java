package com.xunku.actions.myTwitter;

import com.xunku.actions.ActionBase;
import com.xunku.app.enums.Platform;
import com.xunku.app.interfaces.ITweet;
import com.xunku.app.result.Result;
import com.xunku.dao.my.PostDao;
import com.xunku.daoImpl.my.PostDaoImpl;
import com.xunku.pojo.base.User;
import com.xunku.utils.AppServerProxy;

/*******************************************************************************
 * <功能描述>我的评论-删除收到的评论
 * 
 * @author shaoqun
 * 
 */
public class DeleteReviewAction extends ActionBase {

	@Override
	public Object doAction() {

		User _user = this.getUser().getBaseUser();
		PostDao pdao = new PostDaoImpl();

		int platform = Integer.parseInt(this.get("platform"));
		String tid = this.get("tid");
		String uid = this.get("uid");
		String warn = "-1";

		Result<ITweet> result = AppServerProxy.deletePost(uid, Platform.Sina,
				tid, _user);
		if (result.getErrCode() == 0) {
			pdao.deleteCommentPost(tid, platform);
		}else{
			warn = "删除目标不存在!";
		}

		return warn;
	}

}
