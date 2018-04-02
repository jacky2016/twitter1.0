package com.xunku.actions.spreadAnalysis;

import java.text.ParseException;

import com.xunku.actions.ActionBase;
import com.xunku.app.Utility;
import com.xunku.app.enums.Platform;
import com.xunku.app.helpers.DateHelper;
import com.xunku.app.interfaces.IAccount;
import com.xunku.app.monitor.WeiboMonitor;
import com.xunku.app.result.Result;
import com.xunku.constant.PortalCST;
import com.xunku.dao.monitor.MWeiBoDao;
import com.xunku.daoImpl.monitor.MWeiBoDaoImpl;
import com.xunku.dto.AccountDTO;
import com.xunku.dto.MyPostDTO;
import com.xunku.dto.PagerDTO;
import com.xunku.dto.task.TaskRefcontent;
import com.xunku.dto.task.TaskTwitterVO;
import com.xunku.pojo.base.Account;
import com.xunku.pojo.base.User;
import com.xunku.utils.AppServerProxy;
import com.xunku.utils.DateUtils;
import com.xunku.utils.Pagefile;
import com.xunku.utils.StringUtils;

// 进入传播分享模块 获取传播列表数据
public class SpreadAnalysisGetListAction extends ActionBase {
	MWeiBoDao mWeiBoDao = new MWeiBoDaoImpl();

	@Override
	public Pagefile<MyPostDTO> doAction() {
		String pageIndex = this.get("pageIndex");
		String pageSize = this.get("pageSize");
		User user = this.getUser().getBaseUser();

		PagerDTO pagerDTO = new PagerDTO();
		pagerDTO.pageIndex = Integer.parseInt(pageIndex);
		pagerDTO.pageSize = Integer.parseInt(pageSize);
		Pagefile<WeiboMonitor> lst = mWeiBoDao.queryWeiboList(pagerDTO, user
				.getCustomID());

		// 转换成前端需要的集合
		Pagefile<MyPostDTO> collection = new Pagefile<MyPostDTO>();
		if (lst != null) {
			for (WeiboMonitor vo : lst.getRows()) {
				MyPostDTO myPostDTO = new MyPostDTO();
				Platform _Platform = vo.getPlatform(); // 媒体平台枚举
				Result<IAccount> rst = AppServerProxy.getAccountByUcode(vo
						.getUcode(), _Platform, this.getUser().getBaseUser());

				AccountDTO accountDTO = new AccountDTO();
				accountDTO.ucode = vo.getUcode(); // 用户头像ucode
				accountDTO.twitterType = Utility.getPlatform(_Platform);// 账号平台类型
				if (rst.getErrCode() == 0) {
					IAccount account = rst.getData();
					accountDTO = new AccountDTO();
					accountDTO.uid = account.getUcode();
					if (_Platform == Platform.Tencent
							&& account.getHead() != null) {
						accountDTO.imgurl = PortalCST.IMAGE_PATH_PERFIX
								+ account.getHead();// .getProfileImageUrl();
						accountDTO.imgurlBig = PortalCST.IMAGE_PATH_PERFIX
								+ account.getLargeHead();
					} else {
						accountDTO.imgurl = account.getHead();
						accountDTO.imgurlBig = account.getLargeHead();
					}
					accountDTO.ucode = account.getUcode();
					accountDTO.name = account.getName();
					accountDTO.friends = account.getFriends(); // 关注
					accountDTO.weibos = account.getWeibos(); // 发布微博数
					accountDTO.followers = account.getFollowers();// 粉丝数
					accountDTO.summany = account.getDescription();
					// accountDTO.isAjax = false; // 前台不用在异步加载
				}
				myPostDTO.id = vo.getMonitorId();// 分析id
				myPostDTO.account = accountDTO; // 微博账号
				myPostDTO.commentCount = vo.getComments(); // 评论数
				myPostDTO.repostCount = vo.getReposts(); // 转发数
				myPostDTO.text = vo.getText(); // 内容
				myPostDTO.analysisTime = DateHelper
						.formatDate(vo.getAnayTime()); // 分析时间
				myPostDTO.createtime = DateHelper.formatDate(vo
						.getPublishTime()); // 发布时间

				myPostDTO.url = vo.getUrl();// url链接
				myPostDTO.item = null;
				myPostDTO.isAnalysised = vo.isAnalysised();
				collection.getRows().add(myPostDTO);
			}
			collection.setRealcount(lst.getRealcount());
		}
		return collection;
	}

}
