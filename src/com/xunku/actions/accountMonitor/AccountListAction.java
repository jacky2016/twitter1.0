package com.xunku.actions.accountMonitor;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.xunku.actions.ActionBase;
import com.xunku.app.enums.AccountEnums;
import com.xunku.app.enums.Platform;
import com.xunku.app.helpers.DateHelper;
import com.xunku.app.interfaces.IAccount;
import com.xunku.app.interfaces.ITweet;
import com.xunku.app.monitor.AccountMonitor;
import com.xunku.app.result.Result;
import com.xunku.constant.PortalCST;
import com.xunku.dao.account.AccountDao;
import com.xunku.dao.office.AccountWarnDao;
import com.xunku.daoImpl.account.AccountDaoImpl;
import com.xunku.daoImpl.office.AccountWarnDaoImpl;
import com.xunku.dto.AccountDTO;
import com.xunku.dto.CommentDTO;
import com.xunku.dto.MyPostDTO;
import com.xunku.dto.PagerDTO;
import com.xunku.dto.RepostDTO;
import com.xunku.dto.accountMonitor.AccountListDTO;
import com.xunku.dto.eventMonitor.EventDTO;
import com.xunku.dto.eventMonitor.EventListDTO;
import com.xunku.pojo.base.Pager;
import com.xunku.pojo.base.User;
import com.xunku.utils.AppServerProxy;
import com.xunku.utils.Pagefile;

/**
 * 帐号监控--获取帐号监测列表
 * 
 * @author shaoqun
 * 
 */
public class AccountListAction extends ActionBase {

	@Override
	public Object doAction() {
		// TODO Auto-generated method stub

		Pagefile<AccountListDTO> mtfile = new Pagefile<AccountListDTO>();

		int type = Integer.parseInt(this.get("type"));
		int method = Integer.parseInt(this.get("method"));
		User user = this.getUser().getBaseUser();
		String pageIndex = this.get("pageIndex");
		String pageSize = this.get("pageSize");

		Pager pager = new Pager();
		AccountDao accDao = new AccountDaoImpl();

		if (method == 1) {// Accountlist

			pager.setPageIndex(Integer.parseInt(pageIndex));
			pager.setPageSize(Integer.parseInt(pageSize));
			Pagefile<AccountMonitor> accfile = new Pagefile<AccountMonitor>();
			Platform platform = Platform.Sina;
			if (type == 1) {
				platform = Platform.Tencent;
			}
			accfile = accDao.queryAccountList(pager, user.getId(), platform);
			for (AccountMonitor acc : accfile.getRows()) {
				AccountListDTO actdto = new AccountListDTO();

				actdto.setId(acc.getMonitorId());
				actdto.setAccountname(acc.getNick());
				actdto.setWeibonum(acc.getWeibos());
				actdto.setFansnum(acc.getFans());
				actdto.setGznum(acc.getFriends());
				mtfile.getRows().add(actdto);

			}

			mtfile.setRealcount(accfile.getRealcount());
			mtfile.setErr(accfile.getErr());
			return mtfile;

		} else if (method == 2) {// wbdetails

			pager.setPageIndex(Integer.parseInt(pageIndex));
			pager.setPageSize(Integer.parseInt(pageSize));
			int accoundid = Integer.parseInt(this.get("accoundid"));
			String text = this.get("text");
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			Date startDate = null;
			Date endDate = null;

			try {
				startDate = sdf.parse(this.get("startDate"));
				endDate = sdf.parse(this.get("endDate"));
			} catch (ParseException e) {
				e.printStackTrace();
			}

			Pagefile<ITweet> list = AppServerProxy.tweetsRealtimeDetail(text,
					accoundid, startDate, endDate, pager);
			Pagefile<MyPostDTO> collection = new Pagefile<MyPostDTO>();
			if (list != null) {
				for (ITweet post : list.getRows()) {

					MyPostDTO entity = new MyPostDTO();
					if (post.getPlatform() == Platform.Tencent) {
						entity.account.twitterType = 2;
					} else {
						entity.account.twitterType = 1;
					}
					if (post.getAuthor() != null) {
						// 如果类型是腾讯，并且头像路径不为null
						if (post.getPlatform() == Platform.Tencent
								&& post.getAuthor().getHead() != null) {
							entity.account.imgurl = PortalCST.IMAGE_PATH_PERFIX
									+ post.getAuthor().getHead();
							entity.account.imgurlBig = PortalCST.IMAGE_PATH_PERFIX
									+ post.getAuthor().getLargeHead();
						} else {
							entity.account.imgurl = post.getAuthor().getHead();
							entity.account.imgurlBig = post.getAuthor()
									.getLargeHead();
						}
						entity.account.imgurl = post.getAuthor().getHead();

						entity.account.name = post.getAuthor().getDisplayName(); // 名称
						entity.account.url = post.getAuthor().getHomeUrl();
						entity.account.weibos = post.getAuthor().getWeibos(); // 微博数
						entity.account.followers = post.getAuthor()
								.getFollowers(); // 粉丝数
						entity.account.friends = post.getAuthor().getFriends(); // 关注数
						entity.account.summany = post.getAuthor()
								.getDescription(); // 简介
						// entity.account.isAjax = false;
					}

					// // 图片
					// if (post.getImages() != null) {
					// for (String imgPath : post.getImages()) {
					// entity.postImage.add(PortalCST.IMAGE_PATH_PERFIX +
					// imgPath);
					// }
					// }

					entity.id = (int) post.getId(); // PostID
					entity.url = post.getUrl(); // 链接
					entity.tid = post.getTid();
					entity.text = post.getText(); // 内容
					if (post.getSource() == null) {
						entity.isCreative = true;
					} else {
						entity.isCreative = false;
						int index = entity.text.indexOf("：");
						if (post.getPlatform() == Platform.Sina && index > -1) {
							entity.text = entity.text.substring(index + 1);
						}
					}

					entity.createtime = DateHelper
							.formatDate(post.getCreated());// 时间
					if (post.getFrom() != null) {
						entity.source = post.getFrom().getName(); // 来源
					}

					entity.repostCount = post.getReposts(); // 转发数
					entity.commentCount = post.getComments(); // 评论数
					entity.account.ucode = post.getUcode(); // 用户头像ucode

					if (post.getSource() != null) {// 来源
						entity.item = new MyPostDTO();
						ITweet subPost = post.getSource();
						// 账号
						AccountDTO adto = new AccountDTO();

						// 获取图片路径
						// if (subPost.getPlatform() == Platform.Tencent &&
						// subPost.getAuthor().getHead() != null) {
						// adto.imgurl = PortalCST.IMAGE_PATH_PERFIX +
						// subPost.getAuthor().getHead();
						// }else{
						// adto.imgurl = subPost.getAuthor().getHead();
						// }
						if (subPost.getAuthor() != null) {
							adto.name = subPost.getAuthor().getName();
							entity.item.account = adto;
						}
						entity.item.id = (int) subPost.getId();
						entity.item.text = subPost.getText();
						entity.item.url = subPost.getUrl();
						entity.item.createtime = DateHelper.formatDate(subPost
								.getCreated());
						entity.item.source = subPost.getFrom().getName();// Utility.getFrom(subPost.getFrom().getId());
						entity.item.repostCount = subPost.getReposts();
						entity.item.commentCount = subPost.getComments();

					}
					collection.getRows().add(entity);
				}
			}
			collection.setRealcount(list.getRealcount());
			collection.setErr(list.getErr());
			return collection;
		}else if(method == 3){									//预判断
			String textarea = this.get("textarea");
			AccountDao accDAO = new AccountDaoImpl();
			//有一验证
			boolean isflag = accDAO.checkIsAccount(user.getCustomID(),user.getId(), textarea);
			EventDTO edto = new EventDTO();
			if(!isflag){
				edto.setErr("该事件不存在，刷新后重新查看!");
				return edto; 
			}
		}

		return "true";
	}
}
