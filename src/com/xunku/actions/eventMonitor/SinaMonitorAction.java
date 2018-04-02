package com.xunku.actions.eventMonitor;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

import com.xunku.actions.ActionBase;
import com.xunku.app.Utility;
import com.xunku.app.enums.EventMonitorEnum;
import com.xunku.app.enums.Platform;
import com.xunku.app.helpers.DateHelper;
import com.xunku.app.interfaces.ITweet;
import com.xunku.app.monitor.EventMonitor;
import com.xunku.app.result.event.MEventCountResult;
import com.xunku.constant.FiltrateEnum;
import com.xunku.constant.PortalCST;
import com.xunku.constant.TimeSortEnum;
import com.xunku.dao.event.EventDao;
import com.xunku.daoImpl.event.EventDaoImpl;
import com.xunku.dto.AccountDTO;
import com.xunku.dto.CommentDTO;
import com.xunku.dto.MyPostDTO;
import com.xunku.dto.PagerDTO;
import com.xunku.dto.RepostDTO;
import com.xunku.dto.eventMonitor.EventDTO;
import com.xunku.dto.eventMonitor.EventListDTO;
import com.xunku.pojo.base.Account;
import com.xunku.pojo.base.Pager;
import com.xunku.pojo.base.Post;
import com.xunku.pojo.base.User;
import com.xunku.utils.AppServerProxy;
import com.xunku.utils.Pagefile;

/**
 * 事件监控--列表信息
 * 
 * @author shaoqun
 * 
 */
public class SinaMonitorAction extends ActionBase {

	

	@Override
	public Object doAction() {
		
		int type = Integer.parseInt(this.get("type"));
		int method = Integer.parseInt(this.get("method"));
		User user = this.getUser().getBaseUser();
		String pageIndex = this.get("pageIndex");
		String pageSize = this.get("pageSize");
		EventDao eventDAO = new EventDaoImpl();
		Pagefile<EventListDTO> mtfile = new Pagefile<EventListDTO>();
		Pager pager = new Pager();

		if (method == 1) { // 获取监控事件列表

			pager.setPageIndex(Integer.parseInt(pageIndex));
			pager.setPageSize(Integer.parseInt(pageSize));

			Pagefile<EventMonitor> evtfile = new Pagefile<EventMonitor>();
			Platform platform = Platform.Sina;
			if (type == 1) {
				platform = Platform.Tencent;
			}
			evtfile = eventDAO.queryEventList(pager, user.getId(),platform);
			// modify by wujian for count
			// 一次把当前页的计数内容全部查出来下面赋值
			Map<Integer, MEventCountResult> maps = null;
			try {
				maps = AppServerProxy.getEventCount(evtfile.getRows());
			} catch (RuntimeException e) {
				e.printStackTrace();
			}

			for (EventMonitor est : evtfile.getRows()) {

				EventListDTO event = new EventListDTO();

				String starTime = DateHelper.formatDBTime(est.getStartTime());
				String endTime = DateHelper.formatDBTime(est.getEndTime());

				event.setId(est.getMonitorId());
				event.setTopicname(est.getName());
				event.setMonitorStarttime(starTime);
				event.setMonitorEndtime(endTime);

				if (maps != null) {

					MEventCountResult me = maps.get(event.getId());
					if (me != null) {
						event.setTodaynum(me.getToday());
						event.setYesterdaynum(me.getYesterday());
						event.setAllnum(me.getAll());
					}
				}

				starTime = DateHelper.formatDate2(est.getStartTime());
				endTime = DateHelper.formatDate2(est.getEndTime());
				event.setMonitorStarttime(starTime);
				event.setMonitorEndtime(endTime);
				mtfile.getRows().add(event);

			}

			mtfile.setRealcount(evtfile.getRealcount());
			mtfile.setErr(evtfile.getErr());

			return mtfile;

		} else if (method == 2) { // 事件详情

			String uid = String.valueOf(user.getId());
			pager.setPageIndex(Integer.parseInt(pageIndex));
			pager.setPageSize(Integer.parseInt(pageSize));

			String text = this.get("text");
			String screen = this.get("screen"); // 筛选
			String timeSort = this.get("timeSort"); // 排序
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			Date startDate = null;
			Date endDate = null;

			try {
				startDate = sdf.parse(this.get("startDate"));
				endDate = sdf.parse(this.get("endDate"));
			} catch (ParseException e) {
				e.printStackTrace();
			}
			int eid = Integer.parseInt(this.get("eid"));
			EventMonitor event = eventDAO.queryEventByEId(eid);

			Platform fiter = Platform.Renmin;
			if (type == 0) {
				fiter = Platform.Sina;
			} else if (type == 1) {
				fiter = Platform.Tencent;
			} else {
				fiter = Platform.Renmin;
			}

			FiltrateEnum filtrateEnum = FiltrateEnum.All; // 筛选枚举类型
			if (screen.equals("0")) {
				filtrateEnum = FiltrateEnum.All;
			} else if (screen.equals("1")) {
				filtrateEnum = FiltrateEnum.Original;
			} else if (screen.equals("2")) {
				filtrateEnum = FiltrateEnum.Reference;
			}

			TimeSortEnum timeSortEnum = TimeSortEnum.Time; // 排序枚举
			if (timeSort.equals("0")) {
				timeSortEnum = TimeSortEnum.Time;
			} else if (timeSort.equals("1")) {
				timeSortEnum = TimeSortEnum.Similarity;
			} else if (timeSort.equals("2")) {
				timeSortEnum = TimeSortEnum.Transpond;
			} else if (timeSort.equals("3")) {
				timeSortEnum = TimeSortEnum.Comment;
			}

			Pagefile<ITweet> list = AppServerProxy.getEventPosts(event, text,
					startDate, endDate, filtrateEnum, timeSortEnum, pager);
			Pagefile<MyPostDTO> collection = new Pagefile<MyPostDTO>();

			if (list != null) {
				for (ITweet post : list.getRows()) {

					MyPostDTO entity = new MyPostDTO();
					if(post.getPlatform() == Platform.Tencent){
						entity.account.twitterType = 2;
					}else{
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
		}else if(method == 3){
			
			EventMonitor event = new EventMonitor();
			int eid = Integer.parseInt(this.get("data_id"));
			
			event = eventDAO.queryEventByEId(eid);
			EventDTO edto = new EventDTO();

			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			int code = event.getLocation();
			
			edto.setId(event.getMonitorId());
			edto.setName(event.getName());
			edto.setKeywords(event.getKeywords());
			edto.setNotkeywords(event.getNotkeywords());
			edto.setLocation(event.getLocation());
			edto.setStartTime(sdf.format(event.getStartTime()));
			edto.setEndTime(sdf.format(event.getEndTime()));
			edto.setCreator(event.getCreator());
			edto.setCustomID(event.getCustomID());
			edto.setPlatform(Utility.getPlatform(event.getPlatform()));
			return edto;
		}else if(method == 4){
			int eid = Integer.parseInt(this.get("id"));
			EventMonitor event = eventDAO.queryEventByEId(eid);
			EventDTO edto = new EventDTO();
			if (event == null) {
				edto.setErr("该事件不存在，刷新后重新查看!");
				return edto; 
			}
		}
		return "true";
	}

}
