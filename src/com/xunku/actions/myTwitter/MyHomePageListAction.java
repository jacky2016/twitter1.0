package com.xunku.actions.myTwitter;

import java.util.Map;

import com.xunku.actions.ActionBase;
import com.xunku.app.Utility;
import com.xunku.app.enums.FilterPostType;
import com.xunku.app.enums.Platform;
import com.xunku.app.helpers.DateHelper;
import com.xunku.app.interfaces.ITweet;

import com.xunku.constant.PortalCST;
import com.xunku.dao.my.PostDao;
import com.xunku.daoImpl.my.PostDaoImpl;
import com.xunku.dto.AccountDTO;
import com.xunku.dto.CommentDTO;
import com.xunku.dto.MyPostDTO;
import com.xunku.dto.PagerDTO;
import com.xunku.dto.RepostDTO;
import com.xunku.pojo.base.Post;
import com.xunku.pojo.base.User;
import com.xunku.utils.AppServerProxy;
import com.xunku.utils.Pagefile;

/*
 * 获取我的首页列表信息
 * @author sunao
 */
public class MyHomePageListAction extends ActionBase {

	PostDao postDao = new PostDaoImpl();

	public Pagefile<MyPostDTO> doAction() {

		String pageIndex = this.get("pageIndex");
		String pageSize = this.get("pageSize");
		String lstType = this.get("loadListType");
		String keyword = this.get("keyword");
		String accountid = this.get("accountid");

		String uid = this.get("uid");

		PagerDTO pager = new PagerDTO();
		pager.pageIndex = Integer.parseInt(pageIndex);
		pager.pageSize = Integer.parseInt(pageSize);
		int acid = Integer.parseInt(accountid);
		int _type = Integer.parseInt(lstType);
		FilterPostType type;
		switch (_type) {
		case 0:
			type = FilterPostType.All;
			break;
		case 1:
			type = FilterPostType.Creative;
			break;
		case 2:
			type = FilterPostType.Image;
			break;
		default:
			type = FilterPostType.All;
			break;
		}

		// TODO 需要从AppServer获得 modify by wujian
		User user = this.getUser().getBaseUser();
		Pagefile<ITweet> lst = AppServerProxy.getMyPost(user, uid, keyword,
				type, pager.pageIndex, pager.pageSize, Platform.Sina);

		// 转成前台用的
		Pagefile<MyPostDTO> collection = new Pagefile<MyPostDTO>();
		for (ITweet post : lst.getRows()) {

			MyPostDTO entity = new MyPostDTO();
			entity.account.uid = post.getUid();
			entity.account.twitterType = Utility
					.getPlatform(post.getPlatform());
			// 账号
			if (post.getAuthor() != null) {
				// entity.account.isAjax = false; // 前台不用在异步加载
				entity.account.uid = post.getAuthor().getUcode();
				entity.account.ucode = post.getAuthor().getUcode();
				entity.account.twitterType = Utility.getPlatform(post
						.getAuthor().getPlatform());
				// 如果类型是腾讯，并且头像路径不为null
				if (post.getPlatform() == Platform.Tencent
						&& post.getAuthor().getHead() != null) {
					entity.account.imgurl = post.getAuthor().getHead();// PortalCST.IMAGE_PATH_PERFIX
					entity.account.imgurlBig = post.getAuthor().getLargeHead();// PortalCST.IMAGE_PATH_PERFIX
				} else {
					entity.account.imgurl = post.getAuthor().getHead();
					entity.account.imgurlBig = post.getAuthor().getLargeHead();

				}
				if (post.getAuthor().getName() != null) {
					entity.account.name = post.getAuthor().getName();
				}
				entity.account.url = post.getAuthor().getHomeUrl();
				entity.account.friends = post.getAuthor().getFriends(); // 关注
				entity.account.weibos = post.getAuthor().getWeibos(); // 发布微博数
				entity.account.followers = post.getAuthor().getFollowers();// 粉丝数
				entity.account.summany = post.getAuthor().getDescription();
			}

			// 图片
			if (post.getImages() != null) {
				for (String imgPath : post.getImages()) {
					if (post.getPlatform() == Platform.Tencent) {
						entity.postImage.add(PortalCST.IMAGE_PATH_PERFIX
								+ imgPath);
					} else {
						entity.postImage.add(imgPath);
					}
				}
			}
			entity.id = (int) post.getId();// PostID
			entity.tid = post.getTid();
			entity.url = post.getUrl(); // 链接
			entity.text = post.getText(); // 内容
			// 时间
			entity.createtime = DateHelper.formatDate(post.getCreated());// .getCreateTime();
			// 来源
			entity.source = post.getFrom().getName();

			entity.repostCount = post.getReposts();// .getRepostCount(); // 转发数
			entity.commentCount = post.getComments();// .getCommentCount();
			// 评论数
			entity.item = null;
			// 转发类型
			if (post.getOriginalTweet() != null) {
				entity.item = new MyPostDTO();
				ITweet subPost = post.getOriginalTweet();// post.getRepostList();
				// 账号
				AccountDTO adto = new AccountDTO();
				// adto.id = (int) subPost.getAuthor().getId();
				entity.item.account.twitterType = Utility.getPlatform(post
						.getPlatform());
				// 获取图片路径
				if (subPost.getPlatform() == Platform.Tencent
						&& subPost.getAuthor().getHead() != null) {
					adto.imgurl = PortalCST.IMAGE_PATH_PERFIX
							+ subPost.getAuthor().getHead();
					adto.imgurlBig = PortalCST.IMAGE_PATH_PERFIX
							+ subPost.getAuthor().getLargeHead();
				} else {
					adto.imgurl = subPost.getAuthor().getHead();
					adto.imgurlBig = subPost.getAuthor().getLargeHead();
				}
				if (subPost.getAuthor().getName() != null) {
					adto.name = subPost.getAuthor().getName();
				}
				entity.item.account = adto;

				entity.item.id = (int) subPost.getId();
				entity.item.tid = subPost.getTid();
				entity.item.text = subPost.getText();
				entity.item.createtime = DateHelper.formatDate(subPost
						.getCreated());// .getCreateTime();
				entity.item.url = subPost.getUrl();
				entity.item.source = subPost.getFrom().getName();// subPost.getSource();
				entity.item.repostCount = subPost.getReposts();// .getRepostCount();
				entity.item.commentCount = subPost.getComments();// .getCommentCount();

				// 图片
				if (subPost.getImages() != null) {
					for (String imgPath : subPost.getImages()) {
						if (subPost.getPlatform() == Platform.Tencent) {
							entity.item.postImage
									.add(PortalCST.IMAGE_PATH_PERFIX + imgPath);
						} else {
							entity.item.postImage.add(imgPath);
						}
					}
				}
			}
			collection.getRows().add(entity);
		}
		collection.setRealcount(lst.getRealcount());
		collection.setErr(lst.getErr());

		return collection;
	}

}
