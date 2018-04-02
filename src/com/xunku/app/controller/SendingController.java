package com.xunku.app.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xunku.app.AppContext;
import com.xunku.app.Utility;
import com.xunku.app.enums.Platform;
import com.xunku.app.interfaces.ITweet;
import com.xunku.app.manager.CustomManager;
import com.xunku.app.model.ApiException;
import com.xunku.app.model.AppAuth;
import com.xunku.app.model.UploadImage;
import com.xunku.app.model.people.PUser;
import com.xunku.app.model.people.Pweet;
import com.xunku.app.model.tweets.TweetFactory;
import com.xunku.app.result.Result;
import com.xunku.app.stores.MCustomStore;
import com.xunku.dao.my.SendingDao;
import com.xunku.daoImpl.my.SendingDaoImpl;
import com.xunku.pojo.base.Custom;
import com.xunku.pojo.base.User;
import com.xunku.pojo.my.Sender;
import com.xunku.pojo.my.Sending;
import com.xunku.utils.ImageUtil;

/**
 * 发布微博控制器
 * 
 * @author wujian
 * @created on Oct 23, 2014 3:01:21 PM
 */
public class SendingController {

	private static final Logger LOG = LoggerFactory
			.getLogger(SendingController.class);

	private AppContext _context;

	public SendingController(AppContext context) {
		this._context = context;
	}

	/**
	 * 提交要发送的微博
	 * 
	 * @param sending
	 */
	public Result<Sending> submitSending(Sending sending, User user) {

		Result<Sending> result = new Result<Sending>();
		if (Utility.isNullOrEmpty(sending.getText())) {
			result.setError(500, "不能发送没有文本的微博/评论!");
			return result;
		}

		if (sending.getSendList() == null || sending.getSendList().size() == 0) {
			result.setError(500, "没有发送者的微博/评论,无法发送!");
			return result;
		}

		// 转发或者评论时要检查原始微博是否存在
		if (!Utility.isNullOrEmpty(sending.getSourceid())) {
			// 需要把原始微博采集回来
			Custom custom = _context.getCustomManager().getCustom(
					user.getCustomID());

			Sender sender = sending.getSendList().get(0);
			// 更新sourceID
			Result<ITweet> rst = _context.getMOfficialManager().tweetGetByTid(
					sending.getSourceid(),
					Utility.getPlatform(sender.getPlatform()), user);
			if (rst.getErrCode() == 0) {
				MCustomStore store = (MCustomStore) custom.getMonitor()
						.getStore(_context);
				store.put(rst.getData());
				this.insertSending(sending, user);
				result.setData(sending);
				// 不需要审核的需要发送通知消息
				if (sending.getApproved() == 0) {
					sendAttentionMessage(sending, user, _context);
				}

			} else {
				// 这个值如果有内容则说明是转发或者评论，引用微博不再了是不能提交成功的
				result.setError(rst.getErrCode(), rst.getMessage());
			}

		} else {
			this.insertSending(sending, user);
			result.setData(sending);
			// 不需要审核的需要发送通知消息
			if (sending.getApproved() == 0) {
				sendAttentionMessage(sending, user, _context);
			}
		}
		return result;
	}

	public List<Result<ITweet>> sendTweet(Sending sending, User user,
			int delaySeconds) {
		SendingDao dao = new SendingDaoImpl();
		if (user == null) {
			return null;
		}
		Custom custom = _context.getCustomManager().getCustom(
				user.getCustomID());
		MCustomStore store = (MCustomStore) custom.getMonitor().getStore(
				_context);
		List<Result<ITweet>> result = new ArrayList<Result<ITweet>>();
		for (Sender sender : sending.getSendList()) {
			String uid = sender.getUid();
			String text = sending.getText();
			Platform platform = Utility.getPlatform(sender.getPlatform());

			if (Utility.isNullOrEmpty(sending.getText()) || user == null) {
				LOG.info("该微博没有提供文本，所以无法发送!");
				dao.updateSendStatus(sending.getId(), 3, sender.getId(), "0",
						sender.getTries() + 1, 0);
			} else {
				UploadImage image = null;
				if (sending.getImages() != null) {
					String[] images = sending.getImages().split(",");
					if (images.length > 0 && !images[0].isEmpty()) {
						image = ImageUtil.getImages(images[0]);
					}
				}

				Result<ITweet> rst = new Result<ITweet>();
				if (sender.getTries() < Sending.MAX_TRIES) {
					ITweet tweet = null;
					try {
						Thread.sleep(delaySeconds * 1000);
						tweet = _sendTweet(sending, user, dao, store, uid,
								text, platform, image, tweet);
						int status = 3;
						long created = 0;
						String tid = "0";
						if (tweet != null) {
							status = 2;
							tid = tweet.getTid();
							// 更新该客户的tweet信息,目前只更新Sina的
							if (tweet.getPlatform() == Platform.Sina) {
								store.put(tweet);
							}
							created = tweet.getCreated();
							LOG.info("[" + uid + "]发送[" + text + "]成功！");
						}
						rst.setData(tweet);

						dao.updateSendStatus(sending.getId(), status, sender
								.getId(), tid, sender.getTries() + 1, created);
					} catch (ApiException e) {
						LOG.error("ID:" + sending.getId() + ",用[" + uid
								+ "]发送[" + text + "]失败！-->" + e.getError());
						dao.updateSendStatus(sending.getId(), 3,
								sender.getId(), null, sender.getTries() + 1, 0);
						rst.setError(e.getErrorCode(), e.getError());
					} catch (Exception e) {
						LOG.error("休息错了...", e);
					}
				} else {
					LOG.info("发送了超过3次还没成功");
					rst.setError(500, "发送超过指定的次数");
				}
				result.add(rst);
			}
		}
		return result;
	}

	private ITweet _sendTweet(Sending sending, User user, SendingDao dao,
			MCustomStore store, String uid, String text, Platform platform,
			UploadImage image, ITweet tweet) throws ApiException {
		if (sending.getType() == 1) {
			// 原创
			tweet = this._tweetCreate(uid, platform, text, image, user);
		} else if (sending.getType() == 2) {
			// 转发，转发的orgId和sourceid是一样的
			tweet = this._retweetCreate(sending.getSourceid(), uid, platform,
					text, user, sending.isFlag());
			if (tweet != null) {
				dao.updateOrgID(sending.getId(), tweet.getOriginalTweet()
						.getTid());
			}

		} else if (sending.getType() == 3) {
			// 回复必须要有原始微博的ID
			if (!Utility.isNullOrEmpty(sending.getSourceid())) {
				if (!Utility.isNullOrEmpty(sending.getOrgId())) {
					// 回复评论
					tweet = this._commentReply(uid, sending.getOrgId(), sending
							.getSourceid(), text, platform, user);
				} else {
					// 评论微博
					ITweet source = store.queryITweetByTid(sending
							.getSourceid(), platform);
					tweet = this._commentCreate(uid, platform, sending
							.getSourceid(), user, text, sending.isFlag(),
							source);

				}
			}
		}
		return tweet;
	}

	private CustomManager getCustomManager() {
		return _context.getCustomManager();
	}

	/**
	 * 创建评论
	 * 
	 * @param uid
	 *            用哪个用户创建评论
	 * @param platform
	 *            哪个平台
	 * @param tid
	 *            要评论哪个微博
	 * @param user
	 *            系统用户
	 * @param text
	 *            评论的内容
	 * @param isRetweet
	 *            是否同时转发
	 * @return
	 * @throws ApiException
	 */
	private ITweet _commentCreate(String uid, Platform platform, String tid,
			User user, String text, boolean isRetweet, ITweet source)
			throws ApiException {
		AppAuth auth = this.getCustomManager()
				.getAuthByUid(user, uid, platform);

		if (auth == null) {
			LOG.info("未找到授权信息，无法创建评论！");
			return null;
		}

		ITweet tweet = auth.commentCreate(tid, text);

		// 评论带转发，转发需要连接上引用微博的内容
		String sourceText = "";
		if (source != null && source.getSource() != null) {
			sourceText = "//@" + source.getAuthor().getName() + ":"
					+ source.getText();
		}

		if (isRetweet) {
			auth.retweetCreate(tid, text + sourceText, false);
		}

		return tweet;
	}

	private ITweet _commentReply(String uid, String tid, String cid,
			String text, Platform platform, User user) throws ApiException {
		AppAuth auth = this.getCustomManager()
				.getAuthByUid(user, uid, platform);

		if (auth == null) {
			LOG.info("未找到授权信息，无法创建评论！");
			return null;
		}

		ITweet tweet = auth.commentReply(tid, cid, text);

		return tweet;
	}

	/**
	 * 转发一条微博
	 * 
	 * @param tid
	 *            原始微博的tid
	 * @param uid
	 *            用哪个用户转发
	 * @param platform
	 *            转发的平台
	 * @param text
	 *            转发文本
	 * @param user
	 *            哪个系统用户
	 * @param isComment
	 *            是否同时评论
	 * @return
	 * @throws ApiException
	 */
	private ITweet _retweetCreate(String tid, String uid, Platform platform,
			String text, User user, boolean isComment) throws ApiException {
		AppAuth auth = this.getCustomManager()
				.getAuthByUid(user, uid, platform);

		if (auth == null) {
			LOG.info("未找到授权信息，无法转发微博！");
			return null;
		}

		ITweet post = auth.retweetCreate(tid, text, isComment);

		return post;
	}

	/**
	 * 发布一个微博
	 * 
	 * @param uid
	 *            用哪个用户发
	 * @param platform
	 *            在哪个平台发
	 * @param text
	 *            发什么
	 * @param image
	 *            发的图片是什么
	 * @param user
	 *            用哪个用户发的
	 * @return 创建的新微博
	 * @throws ApiException
	 */
	private ITweet _tweetCreate(String uid, Platform platform, String text,
			UploadImage image, User user) throws ApiException {

		if (platform == Platform.Sina) {
			AppAuth auth = this.getCustomManager().getAuthByUid(user, uid,
					platform);

			if (auth == null) {
				LOG.info("未找到授权信息，无法发布微博！");
				return null;
			}

			if (auth.getTimes() >= 60) {
				LOG.info("该授权本小时调用已经超过60次，无法再发送微博!");
				return null;
			}

			ITweet post = auth.tweetCreate(text, image);

			return post;
		}

		if (platform == Platform.Renmin) {
			PeopleAPIController controller = new PeopleAPIController();
			PUser pUser = controller.getPeopleUser(uid);
			if (pUser != null) {
				Pweet pweet = controller.sendTweet(pUser.getLoginName(), pUser
						.getPassword(), text, image);
				return TweetFactory.createTweet(pweet);
			}
		}

		if (platform == Platform.Tencent) {
			// 腾讯发送逻辑
		}

		return null;
	}

	private void insertSending(Sending sending, User user) {
		SendingDao sendingDao = new SendingDaoImpl();
		long sid = sendingDao.insert(sending);
		sending.setId(sid);

		// 产生审核消息 modify by tengsx
		if (sending.getApproved() == 1) {
			// 需要审核的才发审核消息,add by wujian
			new MessageController().sendApproveMessage(user);
		}
	}

	private void sendAttentionMessage(Sending sending, User user,
			AppContext _context) {
		// 获得管理员
		int adminId = _context.getCustomManager().getAdmin(user.getCustomID())
				.getId();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		String sendTime = format.format(new Date());

		if (sending.getSent() != 0) {
			sendTime = format.format(new Date(sending.getSent()));
		}
		StringBuilder sb = new StringBuilder();
		if (sending.getSendList().size() > 0) {
			sb.append(sending.getSendList().get(0).getName());
		}
		for (int i = 1; i < sending.getSendList().size(); i++) {
			sb.append(",");
			sb.append(sending.getSendList().get(i).getName());
		}

		new MessageController().sendAttentionMessage(user.getNickName()
				+ "在新浪平台上使用[" + sb.toString() + "]帐号[" + sendTime
				+ "]时间发布了一条微博", sending.getUserid(), adminId);
	}

}
