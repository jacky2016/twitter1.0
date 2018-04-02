package com.xunku.actions.myTwitter;

import java.util.ArrayList;
import java.util.List;

import com.xunku.actions.ActionBase;
import com.xunku.app.Utility;
import com.xunku.app.enums.Platform;
import com.xunku.app.helpers.DateHelper;
import com.xunku.app.interfaces.IAccount;
import com.xunku.app.interfaces.ITweet;
import com.xunku.app.model.PostsWrapper;
import com.xunku.app.result.Result;
import com.xunku.dto.AccountDTO;
import com.xunku.dto.CommentDTO;
import com.xunku.dto.RepostDTO;
import com.xunku.pojo.base.Pager;
import com.xunku.pojo.base.Post;
import com.xunku.pojo.base.User;
import com.xunku.utils.AppServerProxy;
import com.xunku.utils.Pagefile;

/*
 * 我的首页模块，获取评论列表
 * @author sunao
 */
public class MyHomeGetCommentAction extends ActionBase {

	@Override
	public Pagefile<CommentDTO> doAction() {
		// 评论
		String uid = this.get("uid"); // 微博账号uid
		String tid = this.get("tid"); // 微博账号tid
		String _platform = this.get("platform");

		String pageIndex = this.get("pageIndex");
		String pageSize = this.get("pageSize");

		Platform platform = Utility.getPlatform(Integer.parseInt(_platform)); // 媒体平台枚举

		Pager pager = new Pager();
		pager.setPageIndex(Integer.parseInt(pageIndex));
		pager.setPageSize(Integer.parseInt(pageSize));

		User user = this.getUser().getBaseUser();

		Result<Pagefile<ITweet>> result = AppServerProxy
				.myHomePageViewComments(uid, tid, platform, pager, user);
		
		Pagefile<CommentDTO> collection = new Pagefile<CommentDTO>();
		
		if (result.getErrCode() == 0) {
			Pagefile<ITweet> reposts = result.getData();

			if (reposts != null) {
				for (ITweet report : reposts.getRows()) {

					CommentDTO commentDto = new CommentDTO();
					AccountDTO reportAccount = new AccountDTO();
					reportAccount.isAjax = false;
					if (report.getAuthor() != null) {
						reportAccount.uid = report.getAuthor().getUcode();
						reportAccount.name = report.getAuthor().getName();
						if (report.getAuthor().getHead() != null) {
							reportAccount.imgurl = report.getAuthor().getHead();
						}
					}

					commentDto.comment = report.getText();
					commentDto.createTime = DateHelper.formatDate(report
							.getCreated());
					commentDto.account = reportAccount;
					collection.getRows().add(commentDto);
				}
			}
			collection.setRealcount(reposts.getRealcount());
			collection.setErr(reposts.getErr());
		}
		return collection;
	}

}
