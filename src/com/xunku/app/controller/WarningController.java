package com.xunku.app.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xunku.app.AppContext;
import com.xunku.app.Utility;
import com.xunku.app.enums.WarnStatusEnum;
import com.xunku.app.interfaces.ITweet;
import com.xunku.app.model.ApiException;
import com.xunku.app.model.AppAuth;
import com.xunku.app.monitor.AccountMonitor;
import com.xunku.app.monitor.CustomMonitor;
import com.xunku.app.monitor.EventMonitor;
import com.xunku.app.result.Result;
import com.xunku.app.stores.MAccountStore;
import com.xunku.app.stores.MCustomStore;
import com.xunku.app.stores.MEventStore;
import com.xunku.constant.WarnType;
import com.xunku.dao.event.EventDao;
import com.xunku.dao.monitor.MAccountDao;
import com.xunku.dao.office.AccountWarnDao;
import com.xunku.dao.office.EventWarnDao;
import com.xunku.dao.office.WarnListDao;
import com.xunku.dao.office.WeiboWarnDao;
import com.xunku.daoImpl.base.UserDaoImpl;
import com.xunku.daoImpl.event.EventDaoImpl;
import com.xunku.daoImpl.monitor.MAccountDaoImpl;
import com.xunku.daoImpl.office.AccountWarnDaoImpl;
import com.xunku.daoImpl.office.EventWarnDaoImpl;
import com.xunku.daoImpl.office.WarnListDaoImpl;
import com.xunku.daoImpl.office.WeiboWarnDaoImpl;
import com.xunku.pojo.base.Custom;
import com.xunku.pojo.base.Pager;
import com.xunku.pojo.base.User;
import com.xunku.pojo.office.AccountWarn;
import com.xunku.pojo.office.EventWarn;
import com.xunku.pojo.office.WarnElement;
import com.xunku.pojo.office.WeiboWarn;
import com.xunku.utils.Pagefile;
import com.xunku.utils.URLUtils;

/**
 * 预警控制器
 * 
 * @author wujian
 * @created on Sep 17, 2014 9:41:24 AM
 */
public class WarningController {

	static Logger LOG = LoggerFactory.getLogger(WarningController.class);

	/**
	 * 添加一个帐号预警,如果添加失败返回null,否则返回添加成功的帐号预警
	 * 
	 * @param warn
	 * @return
	 */
	public AccountWarn addAccountWarning(AccountWarn warn) {
		AccountWarnDao dao = new AccountWarnDaoImpl();
		int wid = dao.insert(warn);
		if (wid == 0)
			return null;

		warn.setId(wid);
		return warn;
	}

	/**
	 * 添加一个微博预警,如果添加失败返回null,否则返回添加成功的微博预警
	 * 
	 * @param warn
	 * @return -1已经存在、-2该帖已经被删除、0数据库未插入、>0插入成功
	 * 
	 */
	public int addWeiboWarning(WeiboWarn warn, User user) {
		WeiboWarnDao dao = new WeiboWarnDaoImpl();
		if (dao.query(warn.getCustomid(), warn.getTid()) == null) {

			// 检查一下tid是否存在，如果存在就返回，否则返回null

			Result<ITweet> tweet = AppContext.getInstance()
					.getMOfficialManager().tweetGetByTid(warn.getTid(),
							warn.getPlatform(), user);

			if (tweet.getErrCode() != 0) {
				return -2;
			}

			int wid = dao.insert(warn);
			if (wid == 0)
				return 0;
			warn.setId(wid);
			return wid;
		}
		return -1;
	}

	/**
	 * 添加一个微博预警,(此处为假删除下的) 已经发生的预警重新update下
	 * 
	 * @param warn
	 */

	// 之前存在的，是已经发生预警的(假删除) 需要update下
	public int updateAlreadyWarn(WeiboWarn warn) {
		WeiboWarnDao weiboDao = new WeiboWarnDaoImpl();
		boolean flag = weiboDao.update(warn);
		if (flag) {
			return 1;
		} else {
			return 0;
		}
	}

	/**
	 * 添加一个事件预警,如果添加失败返回null,否则返回添加成功的事件预警
	 * 
	 * @param warn
	 * @return
	 */
	public EventWarn addEventWarning(EventWarn warn) {
		EventWarnDao dao = new EventWarnDaoImpl();
		int wid = dao.insert(warn);
		if (wid == 0)
			return null;
		warn.setId(wid);
		return warn;
	}

	/**
	 * 获得指定用户的帐号监测列表
	 * 
	 * @param user
	 * @param pager
	 * @return
	 */
	public List<AccountWarn> getAccountWarning(int customid) {
		AccountWarnDao dao = new AccountWarnDaoImpl();
		List<AccountWarn> result = dao.queryAccountWarnList(customid);

		return result;
	}

	public void changeAccountWarningStatus(int accountid, int customid,
			boolean isRunning) {
		AccountWarnDao dao = new AccountWarnDaoImpl();
		dao.changeRunning(accountid, customid, isRunning);
	}

	public void changeWeiboWarningStatus(String tid, int customid,
			boolean isRunning) {
		WeiboWarnDao dao = new WeiboWarnDaoImpl();
		dao.changeRunning(tid, customid, isRunning);
	}

	public void changeEventWarningStatus(int eventId, int customid,
			boolean isRunning) {
		EventWarnDao dao = new EventWarnDaoImpl();
		dao.changeRunning(eventId, customid, isRunning);
	}

	/**
	 * 获得指定用户的微博监测列表
	 * 
	 * @param user
	 * @param pager
	 * @return
	 */
	public List<WeiboWarn> getWeiboWarning(int customid) {
		WeiboWarnDao dao = new WeiboWarnDaoImpl();
		List<WeiboWarn> result = dao.queryWeiboWarnList(customid);
		return result;
	}

	/**
	 * 根据帐号预警生成一条警告消息，生成到Warn_List里面去
	 * 
	 * @param warn
	 * @return
	 */
	public void makeWarnMessage(AccountWarn warn, AppContext context) {

		// 获得事件监测器
		int accid = warn.getAccid();
		// 如果已经发生了，则不再产生消息
		if (!warn.isRunning()) {
			LOG.info("帐号预警已经发过消息，不再发送消息。");
			return;
		}
		String[] revicerList = warn.getReceiver().split(",");

		MAccountDao dao = new MAccountDaoImpl();
		AccountMonitor monitor = dao.queryMAccountByID(accid);

		if (monitor == null) {
			return;
		}
		MAccountStore store = (MAccountStore) monitor.getStore(context);

		List<ITweet> list = store.queryWarning(warn.getKeyword().split(";"),
				warn.getSinceid());

		int sinceId = store.executeMaxId();
		AccountWarnDao awDAO = new AccountWarnDaoImpl();
		awDAO.updateSinceId(warn.getId(), sinceId);
		for (ITweet tweet : list) {
			for (String reciver : revicerList) {
				this.insertAccountWarn(warn, "新浪-帐号“" + monitor.getNick()
						+ "”发生预警，发表微博“" + cutText(tweet.getText()) + "”", 1,
						true, reciver);
			}
		}

	}

	private String cutText(String text) {
		if (text.length() > 20) {
			text = text.substring(0, 20);
		}
		return text;
	}

	private void insertAccountWarn(AccountWarn warn, String msg, int warnType,
			boolean status, String reciver) {
		WarnListDao warnListDAO = new WarnListDaoImpl();
		int ir = Integer.parseInt(reciver);
		sendWarnMessage(warn.getId(), warn.getCustomid(), msg, warnType,
				status, warnListDAO, ir, warn.getType());
	}

	public void makeWarnMessage(WeiboWarn warn, AppAuth auth, AppContext context) {

		// 获得预警微博的转发列表和回复列表

		String tid = warn.getTid();

		// 按照需求，当两个预警都发生时则不再预警
		if (warn.isCommentHappen() && warn.isRepostHappen()) {
			return;
		}

		if (warn.getTime() < System.currentTimeMillis()) {
			LOG.info("微博预警已经超过结束时间，不再产生预警信息。");
			return;
		}

		String[] revicerList = warn.getReceiver().split(",");

		// 这条微博入客户库
		Custom custom = context.getCustomManager()
				.getCustom(warn.getCustomid());
		if (custom == null) {
			return;
		}
		ITweet tweet = null;
		try {
			tweet = auth.tweetGet(tid);
		} catch (ApiException e) {
			LOG.error("获取微博内容出错[" + e.getError() + "]...");
			return;
		}
		if (tweet != null) {

			CustomMonitor monitor = custom.getMonitor();
			MCustomStore store = (MCustomStore) monitor.getStore(context);

			store.put(tweet);
			store.flushHandlers();

			String text = tweet.getText();
			if (text.length() > 20) {
				text = text.substring(0, 20) + "...";
			}

			for (String reciver : revicerList) {
				if (!Utility.isNullOrEmpty(reciver)) {
					this.insertWeiboWarn(tweet, warn, reciver);
				}
			}

		}

	}

	public void makeWarnMessage(EventWarn warn, AppContext context) {

		// 去预警事件里面找有没有出现指定的关键字
		int weibos = warn.getWeibo();

		// 获得事件监测器
		int eventId = warn.getEventid();
		if (!warn.isRunning()) {
			LOG.info("事件预警已经发过消息，不再发送消息。");
			return;
		}
		String[] revicerList = warn.getReceiver().split(",");

		EventDao dao = new EventDaoImpl();
		EventMonitor monitor = dao.queryEventByEId(eventId);

		if (monitor == null) {
			return;
		}
		MEventStore store = (MEventStore) monitor.getStore(context);

		int cnt = store.count(monitor.getStartTime().getTime(), monitor
				.getEndTime().getTime());
		if (cnt > weibos) {
			String warnMsg = "新浪-事件“" + monitor.getName() + "”发生预警，微博数量已经超过"
					+ weibos + ",达到" + cnt;
			for (String reciver : revicerList) {
				if (!Utility.isNullOrEmpty(reciver)) {
					this.insertEventWarn(warn, warnMsg, 2, true, reciver);
				}
			}
		}
	}

	private void insertEventWarn(EventWarn warn, String msg, int warnType,
			boolean status, String reciver) {
		WarnListDao warnListDAO = new WarnListDaoImpl();
		int ir = Integer.parseInt(reciver);
		sendWarnMessage(warn.getId(), warn.getCustomid(), msg, warnType,
				status, warnListDAO, ir, warn.getType());

		// TODO 这里还需要根据类型判断是否需要发邮件通知
		// URLUtils.sendNet(customid, userid, email, title, text, count);
		// 设置预警停止标志，事件预警代码到这里了一定是发生了预警的
		EventWarnDao dao = new EventWarnDaoImpl();
		dao.changeRunning(warn.getEventid(), warn.getCustomid(), false);
	}

	private void insertWeiboWarn(ITweet tweet, WeiboWarn warn, String reciver) {

		boolean overloadRetweet = tweet.getReposts() >= warn.getRepost();
		boolean overloadComment = tweet.getComments() >= warn.getComment();
		String text = cutText(tweet.getText());

		WarnListDao warnListDAO = new WarnListDaoImpl();
		int ir = Integer.parseInt(reciver);
		if (!warn.isCommentHappen() && warn.getComment() > 0 && overloadComment) {
			// 发生评论预警
			if (!warn.isCommentHappen()) {
				// LOG.info("发生评论预警");
				sendWarnMessage(warn.getId(), warn.getCustomid(), "新浪-微博“"
						+ text + "”发生预警,评论数[" + tweet.getComments() + "]超过了阀值["
						+ warn.getComment() + "]", 3, true, warnListDAO, ir,
						warn.getType());
			}
		}

		if (!warn.isRepostHappen() && warn.getRepost() > 0 && overloadRetweet) {
			// 发生转发预警
			if (!warn.isRepostHappen()) {
				// LOG.info("发生转发预警");
				sendWarnMessage(warn.getId(), warn.getCustomid(), "新浪-微博“"
						+ text + "”发生预警,转发数[" + tweet.getReposts() + "]超过了阀值["
						+ warn.getRepost() + "]", 3, true, warnListDAO, ir,
						warn.getType());
			}
		}

		// 同步转发预警和评论预警
		WeiboWarnDao dao = new WeiboWarnDaoImpl();
		if (overloadRetweet) {
			dao.setWeiboRepostFlag(warn.getId(), true);
		}
		if (overloadComment) {
			dao.setWeiboCommentFlag(warn.getId(), true);
		}
	}

	public void unhappenWeiboWarn(WeiboWarn warn, String sb,
			WarnListDao warnListDAO, int ir) {
		// 未发生预警
		WarnElement we = warnListDAO.queryUnwarnWeiboElement(warn.getId(), ir);
		if (we == null) {
			sendWarnMessage(warn.getId(), warn.getCustomid(), sb.toString(), 3,
					false, warnListDAO, ir, warn.getType());
		}
	}

	private void sendWarnMessage(int warnId, int customid, String msg,
			int warnType, boolean status, WarnListDao warnListDAO, int ir,
			String type) {

		List<Integer> types = this.getSendType(type);

		User reciver = new UserDaoImpl().queryByid(ir);
		// 按类型发送消息
		for (int t : types) {
			if (t == 0) {
				sendSysMessage(warnId, customid, msg, warnType, status,
						warnListDAO, ir);
			}

			if (t == 1) {

				this.sendMailMessage(reciver, msg);
			}

			if (t == 2) {
				this.sendTelMessage(reciver, msg);
			}
		}

	}

	private void sendMailMessage(User reciver, String msg) {
		try {
			LOG.info("给[" + reciver.getEmail() + "]发邮件.");
			URLUtils.sendNet(1, null, reciver.getEmail(), msg, msg);
		} catch (IOException e) {
			LOG.info("给[" + reciver.getEmail() + "]发邮件失败.");
		}
	}

	private void sendTelMessage(User reciver, String msg) {
		try {
			LOG.info("给[" + reciver.getTel() + "]发短信.");
			URLUtils.sendNet(3, reciver.getTel(), null, msg, msg);
		} catch (IOException e) {
			LOG.info("给[" + reciver.getTel() + "]发短信失败.");
		}
	}

	/**
	 * 发送系统消息
	 * 
	 * @param warnId
	 * @param customid
	 * @param msg
	 * @param warnType
	 * @param status
	 * @param warnListDAO
	 * @param ir
	 */
	private void sendSysMessage(int warnId, int customid, String msg,
			int warnType, boolean status, WarnListDao warnListDAO, int ir) {
		LOG.info("给[" + ir + "]发站内信息...");
		WarnElement el = new WarnElement();
		el.setCreated(new Date());
		el.setCustomid(customid);
		if (status)
			el.setStatus(1);// 预警
		else
			el.setStatus(0);// 未预警
		el.setText(msg);
		el.setWarnType(warnType);// 1 账号预警，2 事件预警，3 微博预警 4微博信息
		el.setReciver(ir);
		el.setWarnId(warnId);
		warnListDAO.insert(el);
	}

	private List<Integer> getSendType(String type) {
		String[] types = type.split(",");
		List<Integer> result = new ArrayList<Integer>();

		for (String t : types) {
			result.add(Integer.parseInt(t));
		}
		return result;

	}

	/**
	 * 读列表
	 * 
	 * @param userid
	 */
	public int readWarnList(int userid, int[] ids) {
		WarnListDao warnListDAO = new WarnListDaoImpl();
		return warnListDAO.readWarnList(userid, ids);
	}

	/**
	 * 前台的点击小眼睛，如果之前是未读的，则调 此方法，将更新数据库 readed状态为1 即为已读
	 */
	public int updateReadWarnList(int userid, int id) {
		WarnListDao warnListDAO = new WarnListDaoImpl();
		return warnListDAO.updateReadWarnList(userid, id);
	}

	/**
	 * 读取当前用户未读的预警消息数量
	 * 
	 * @param userid
	 * @return
	 */
	public int getUnreadWarnListCount(int userid) {
		WarnListDao warnListDAO = new WarnListDaoImpl();
		return warnListDAO.getUnreadWarnListCount(userid);
	}

	/**
	 * 获得指定用户的事件监测列表
	 * 
	 * @param user
	 * @param pager
	 * @return
	 */
	public List<EventWarn> getEventWarning(int customid) {
		EventWarnDao dao = new EventWarnDaoImpl();
		List<EventWarn> result = dao.queryEventWarnList(customid);
		return result;
	}

	/**
	 * 获得预警产生的消息
	 * 
	 * @param user
	 * @param pager
	 * @param start
	 * @param end
	 * @param warnStatus
	 * @return
	 */
	public Pagefile<WarnElement> getWarns(User user, Pager pager, Date start,
			Date end, WarnType type, WarnStatusEnum warnStatus) {
		WarnListDao dao = new WarnListDaoImpl();
		return dao.queryWarnListPagefile(pager, user.getId(), start, end, type,
				warnStatus);

	}

	public Pagefile<WarnElement> getWarns(User user, Pager pager) {
		WarnListDao dao = new WarnListDaoImpl();
		return dao.queryWarnListPagefile(pager, user.getId());

	}

	/**
	 * 根据消息ID获得微博预警详细内容
	 * 
	 * @param warnListId
	 * @return
	 */
	public WeiboWarn getWeiboWarnByMessageID(int warnListId) {
		WarnListDao dao = new WarnListDaoImpl();

		return dao.queryWeiboWarn(warnListId);
	}

	/**
	 * 根据客户建立的微博预警的个数
	 * 
	 * @param
	 * @return -1 已经超出设置的最大微博预警个数
	 */

	public int getCurrentWeiboWarnCount(int customid) {
		WeiboWarnDao dao = new WeiboWarnDaoImpl();
		return dao.getCurrentWeiboWarnCount(customid);
	}

	/**
	 * 预警服务--预警信息中的取消预警的方法
	 * 
	 * @param
	 * @return 0 是取消预警失败 1 是取消预警成功
	 */
	public int getCanenlWeiboWarnServices(String tid, int customid) {
		WeiboWarnDao dao = new WeiboWarnDaoImpl();
		return dao.getCanenlWeiboWarnServices(tid, customid);
	}

	/** ************************预警列表的所有方法******************************* */

	/**
	 * 预警服务--预警列表中的删除某一行数据的方法
	 */
	public int deleteWeiboWarnList(int id, int customid) {
		WeiboWarnDao dao = new WeiboWarnDaoImpl();
		return dao.deleteWeiboWarnList(id, customid);
	}

	/**
	 * 预警服务--预警列表中的列表展示方法
	 */
	public Pagefile<WeiboWarn> queryWarnWeiboShowList(int customid, Pager pager) {
		WeiboWarnDao dao = new WeiboWarnDaoImpl();
		return dao.queryWarnWeiboShowList(customid, pager);
	}

	/**
	 * 预警服务--预警列表中的修改某一条预警设置得回来的这条数据方法
	 */
	public WeiboWarn modifyWarningList(int id) {
		WeiboWarnDao dao = new WeiboWarnDaoImpl();
		return dao.modifyWarningList(id);
	}

	/**
	 * 预警服务--预警列表中的更改数据的方法
	 */
	public int updateWarnList(WeiboWarn warn) {
		WeiboWarnDao dao = new WeiboWarnDaoImpl();
		return dao.updateWarnList(warn);
	}
	
	public   int   deleteWarnNofity(int id){
		WeiboWarnDao dao = new WeiboWarnDaoImpl();
		return  dao.deleteWarnNofity(id);
	}
	

}
