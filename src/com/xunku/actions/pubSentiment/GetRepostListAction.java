package com.xunku.actions.pubSentiment;

import com.xunku.actions.ActionBase;
import com.xunku.app.helpers.DateHelper;
import com.xunku.app.interfaces.ITweet;
import com.xunku.app.result.Result;
import com.xunku.dto.AccountDTO;
import com.xunku.dto.RepostDTO;
import com.xunku.pojo.base.Pager;
import com.xunku.pojo.base.User;
import com.xunku.utils.AppServerProxy;
import com.xunku.utils.Pagefile;

/*
 * 舆情展示模块，获取转发列表
 * @author sunao
 */
public class GetRepostListAction extends ActionBase {

	@Override
	public Pagefile<RepostDTO> doAction() {

		String url = this.get("url");
		String uid = this.get("uid");
		String pageIndex = this.get("pageIndex");
		String pageSize = this.get("pageSize");

		Pager pager = new Pager();
		pager.setPageIndex(Integer.parseInt(pageIndex));
		pager.setPageSize(Integer.parseInt(pageSize));

		User user = this.getUser().getBaseUser();

		Result<Pagefile<ITweet>> result = AppServerProxy.viewReposts(uid, url,
				pager, user);


		Pagefile<RepostDTO> reposts = new Pagefile<RepostDTO>();
		if (result.getErrCode() == 0) {
			Pagefile<ITweet> tweets = result.getData();

			if (tweets != null) {
				for (ITweet report : tweets.getRows()) {

					RepostDTO reportdto = new RepostDTO();
					AccountDTO reportAccount = new AccountDTO();
					reportAccount.isAjax = false;
					if (report.getAuthor() != null) {
						reportAccount.uid = report.getAuthor().getUcode();
						reportAccount.name = report.getAuthor().getName();
						if (report.getAuthor().getHead() != null) {
							reportAccount.imgurl = report.getAuthor().getHead();
						}
					}

					reportdto.text = report.getText();
					reportdto.createTime = DateHelper.formatDate(report
							.getCreated());
					reportdto.account = reportAccount;
					reposts.getRows().add(reportdto);
				}

				reposts.setRealcount(tweets.getRealcount());
				reposts.setErr(tweets.getErr());
			} 
		}

		return reposts;
	}

}
