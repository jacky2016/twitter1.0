package com.xunku.actions.pubSentiment;

import java.util.ArrayList;
import java.util.List;

import com.xunku.actions.ActionBase;
import com.xunku.app.Utility;
import com.xunku.app.enums.Platform;
import com.xunku.app.helpers.DateHelper;
import com.xunku.app.interfaces.IAccount;
import com.xunku.app.interfaces.ITweet;
import com.xunku.app.result.Result;
import com.xunku.constant.PortalCST;
import com.xunku.dto.AccountDTO;
import com.xunku.dto.RepostDTO;
import com.xunku.dto.task.TaskTwitterVO;
import com.xunku.pojo.base.Pager;
import com.xunku.pojo.base.User;
import com.xunku.utils.AppServerProxy;
import com.xunku.utils.Pagefile;

/*
 * 舆情处理模块，获取转发列表
 * @author sunao
 */
public class DealGetRepostListAction extends ActionBase {

	@Override
	public Pagefile<RepostDTO> doAction() {

		String url = this.get("url");
		String pageIndex = this.get("pageIndex");
		String pageSize = this.get("pageSize");

		Pager pager = new Pager();
		pager.setPageIndex(Integer.parseInt(pageIndex));
		pager.setPageSize(Integer.parseInt(pageSize));

		User user = this.getUser().getBaseUser();

		Pagefile<RepostDTO> reposts = new Pagefile<RepostDTO>();
		Result<Pagefile<ITweet>> result = AppServerProxy.getRetweetsByUrl(url,
				pager, user);
		if (result.getErrCode() == 0) {
			Pagefile<ITweet> repostList = result.getData();
			if (repostList != null) {
				List<ITweet> lst = repostList.getRows();
				for (ITweet taskVo : lst) {
					RepostDTO repost = new RepostDTO();

					Platform platform = taskVo.getPlatform();

					IAccount account = taskVo.getAuthor();
					AccountDTO accountDto = new AccountDTO();
					if (account != null) {
						if (account.getHead() != null
								&& account.getPlatform() == Platform.Tencent) {
							accountDto.imgurl = PortalCST.IMAGE_PATH_PERFIX
									+ account.getHead();
						} else {
							accountDto.imgurl = account.getHead();
						}
						accountDto.name = account.getName();
						accountDto.uid = account.getUcode();
						accountDto.isAjax = false;
					}
					repost.account = accountDto;
					repost.text = taskVo.getText();
					repost.createTime = DateHelper.formatDate(taskVo
							.getCreated());
					reposts.getRows().add(repost);
				}
			}
			reposts.setRealcount(repostList.getRealcount());
			reposts.setErr(repostList.getErr());
		}
		return reposts;

	}

}
