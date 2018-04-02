package com.xunku.actions.pubSentiment;

import java.io.IOException;

import com.xunku.actions.ActionBase;
import com.xunku.app.Utility;
import com.xunku.app.enums.Platform;
import com.xunku.app.enums.PostType;
import com.xunku.app.interfaces.IAccount;
import com.xunku.app.interfaces.ITweet;
import com.xunku.app.result.Result;
import com.xunku.constant.DealStatusEnum;
import com.xunku.constant.PortalCST;
import com.xunku.dao.task.CollectionDao;
import com.xunku.daoImpl.task.CollectionDaoImpl;
import com.xunku.dto.AccountDTO;
import com.xunku.dto.CommentDTO;
import com.xunku.dto.MyPostDTO;
import com.xunku.dto.PagerDTO;
import com.xunku.dto.RepostDTO;
import com.xunku.dto.task.TaskTwitterVO;
import com.xunku.pojo.base.Account;
import com.xunku.pojo.base.Post;
import com.xunku.pojo.task.Collection;
import com.xunku.service.WeiboWebService;
import com.xunku.utils.AppServerProxy;
import com.xunku.utils.DateUtils;
import com.xunku.utils.Pagefile;
import com.xunku.utils.StringUtils;

/*
 * TaskCollectionListAction 微博舆情获取任务的舆情信息集合
 * @author: hjian
 */
public class TaskCollectionListAction extends ActionBase {

	CollectionDao collectionDao = new CollectionDaoImpl();

	@Override
	public Object doAction() {
		// TODO Auto-generated method stub

		String startTime = this.get("startTime");
		String endTime = this.get("endTime");
		String type = this.get("type");
		String platform = this.get("platform");
		String groupid = this.get("groupid");
		String pageIndex = this.get("pageIndex");
		String pageSize = this.get("pageSize");

		// 转换DealStatusEnum枚举
		DealStatusEnum dealStatusEnum = DealStatusEnum.DEALLING;
		if (type.equals("0")) {
			dealStatusEnum = DealStatusEnum.DEALLING;
		} else if (type.equals("1")) {
			dealStatusEnum = DealStatusEnum.DEALLED;
		} else if (type.equals("2")) {
			dealStatusEnum = DealStatusEnum.NODEAL;
		}

		// 转换平台
		Platform platformEnum = Utility.getPlatform(Integer.parseInt(platform)); // 媒体平台枚举

		// 组织分页对象
		PagerDTO pagerDTO = new PagerDTO();
		pagerDTO.pageIndex = Integer.parseInt(pageIndex);
		pagerDTO.pageSize = Integer.parseInt(pageSize);

		Pagefile<Collection> lst = collectionDao.queryWeiboDeal(dealStatusEnum,
				startTime, endTime, platformEnum, Integer.parseInt(groupid),
				pagerDTO);

		// 转换前端使用DTO
		Pagefile<MyPostDTO> collection = new Pagefile<MyPostDTO>();
		if (lst != null) {
			for (Collection vo : lst.getRows()) {
				MyPostDTO myPostDTO = new MyPostDTO();
				ITweet post = vo.getPost();
				if (post != null) {
					IAccount account = post.getAuthor();

					AccountDTO accountDTO = new AccountDTO();

					accountDTO.ucode = post.getUcode(); // 用户头像ucode
					accountDTO.twitterType = Utility.getPlatform(post
							.getPlatform());// 账号平台类型

					if (account != null) {
						if (account.getHead() != null
								&& account.getPlatform() == Platform.Tencent) {
							accountDTO.imgurl = PortalCST.IMAGE_PATH_PERFIX
									+ account.getHead();
						} else {
							accountDTO.imgurl = account.getHead();
						}
						accountDTO.uid = account.getUcode();
						accountDTO.url=account.getHomeUrl();
						accountDTO.name = account.getName();
						accountDTO.friends = account.getFriends(); // 关注
						accountDTO.weibos = account.getWeibos(); // 发布微博数
						accountDTO.followers = account.getFollowers();// 粉丝数
						accountDTO.summany = account.getDescription();
						accountDTO.isAjax = false; // 前台不用在异步加载
					}
					if (post.getType() == PostType.Creative) {
						myPostDTO.isCreative = true;
					} else {
						myPostDTO.isCreative = false;
					}

					// 图片
					if (post.getImages() != null) {
						for (String imgPath : post.getImages()) {
							if (post.getPlatform() == Platform.Tencent) {
								myPostDTO.postImage
										.add(PortalCST.IMAGE_PATH_PERFIX
												+ imgPath);
							} else {
								myPostDTO.postImage.add(imgPath);
							}
						}
					}

					myPostDTO.id = vo.getId(); // 收集ID
					myPostDTO.account = accountDTO; // 微博账号
					myPostDTO.commentCount = post.getComments(); // 评论数
					myPostDTO.repostCount = post.getReposts(); // 转发数
					myPostDTO.text = post.getText(); // 内容
					myPostDTO.createtime = DateUtils.formatDateString(post
							.getCreated());// 发布时间
					myPostDTO.url = post.getUrl();// url链接
					myPostDTO.tid = post.getTid();
					String sourceName = post.getFrom().getName();

					myPostDTO.source = sourceName;// 发布平台（来源）
					myPostDTO.sourceID = Long.parseLong(String.valueOf(post
							.getFrom().getId())); // 来源ID
					// myPostDTO.postImage = vo.getImgurl();// 图片集合
					myPostDTO.item = null;
					if (post.getSource() != null) {
						ITweet subPost = post.getSource();
						IAccount subAccount = subPost.getAuthor();
						MyPostDTO subPostDTO = new MyPostDTO();
						try {
							AccountDTO subAccountDTO = new AccountDTO();
							if (subAccount != null) {
								subAccountDTO.name = subAccount.getDisplayName();
							}
							subPostDTO.account = subAccountDTO;

							// 图片
							if (subPost.getImages() != null) {
								for (String imgPath : subPost.getImages()) {
									if (subPost.getPlatform() == Platform.Tencent) {
										subPostDTO.postImage
												.add(PortalCST.IMAGE_PATH_PERFIX
														+ imgPath);
									} else {
										subPostDTO.postImage.add(imgPath);
									}
								}
							}
							subPostDTO.text = subPost.getText(); // 原帖内容
							String subSourceName = subPost.getFrom().getName();
							subPostDTO.source = subSourceName;// 发布平台（来源）
							subPostDTO.createtime = DateUtils
									.formatDateString(subPost.getCreated());// 原帖发布时间
							subPostDTO.commentCount = subPost.getComments(); // 原帖评论数
							subPostDTO.repostCount = subPost.getReposts(); // 原帖转发数
							subPostDTO.url = subPost.getUrl();
							myPostDTO.item = subPostDTO;
						} catch (NumberFormatException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}
				}
				collection.getRows().add(myPostDTO);
			}
			collection.setRealcount(lst.getRealcount());
		}
		return collection;
	}

}
