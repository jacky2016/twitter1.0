package com.xunku.actions.pubSentiment;

import com.xunku.actions.ActionBase;
import com.xunku.app.Utility;
import com.xunku.app.enums.Platform;
import com.xunku.app.helpers.DateHelper;
import com.xunku.app.interfaces.IAccount;
import com.xunku.app.interfaces.ITweet;
import com.xunku.constant.FiltrateEnum;
import com.xunku.constant.PortalCST;
import com.xunku.constant.TimeSortEnum;
import com.xunku.dao.task.TaskDao;
import com.xunku.daoImpl.task.TaskDaoImpl;
import com.xunku.dto.AccountDTO;
import com.xunku.dto.MyPostDTO;
import com.xunku.dto.PagerDTO;
import com.xunku.dto.task.TaskSearchDTO;
import com.xunku.pojo.task.Task;
import com.xunku.service.TaskSearchService;
import com.xunku.utils.AppServerProxy;
import com.xunku.utils.Pagefile;

/* 
 * WeiBoListAction 微博舆情获取微博信息集合
 * @author: hjian
 */
public class WeiBoListAction extends ActionBase {
	TaskSearchService taskSearchService = new TaskSearchService();
	TaskDao taskDao = new TaskDaoImpl();

	@Override
	public Object doAction() {

		String taskid = this.get("taskid");
		String startTime = this.get("startTime");
		String endTime = this.get("endTime");
		String screen = this.get("screen"); // 筛选
		String timeSort = this.get("timeSort"); // 排序
		String platform = this.get("platform"); // 媒体平台
		String pageIndex = this.get("pageIndex");
		String pageSize = this.get("pageSize");

		TaskSearchDTO taskSearchDTO = new TaskSearchDTO();
		taskSearchDTO.taskId = Integer.parseInt(taskid);
		taskSearchDTO.startTime = startTime;
		taskSearchDTO.endTime = endTime;
		FiltrateEnum filtrateEnum = FiltrateEnum.All;
		if (screen.equals("0")) {
			filtrateEnum = FiltrateEnum.All;
		} else if (screen.equals("1")) {
			filtrateEnum = FiltrateEnum.Original;
		} else if (screen.equals("2")) {
			filtrateEnum = FiltrateEnum.Reference;
		}
		taskSearchDTO.type = filtrateEnum; // 筛选枚举类型

		TimeSortEnum timeSortEnum = TimeSortEnum.Time;
		if (timeSort.equals("0")) {
			timeSortEnum = TimeSortEnum.Time;
		} else if (timeSort.equals("1")) {
			timeSortEnum = TimeSortEnum.Similarity;
		} else if (timeSort.equals("2")) {
			timeSortEnum = TimeSortEnum.Transpond;
		} else if (timeSort.equals("3")) {
			timeSortEnum = TimeSortEnum.Comment;
		}
		taskSearchDTO.timeSort = timeSortEnum; // 排序枚举

		taskSearchDTO.platform = Utility
				.getPlatform(Integer.parseInt(platform)); // 媒体平台枚举

		PagerDTO pagerDTO = new PagerDTO();
		pagerDTO.pageIndex = Integer.parseInt(pageIndex);
		pagerDTO.pageSize = Integer.parseInt(pageSize);
		taskSearchDTO.pagerDTO = pagerDTO; // 分页对象

		// UserInfo user = this.getUser();
		Task task = taskDao.queryTaskById(Integer.parseInt(taskid));
		if (task == null) {
			Pagefile<MyPostDTO> collectionError = new Pagefile<MyPostDTO>();
			collectionError.setErr("该任务不存在，刷新后从新查看");
			return collectionError; 
		}
		Pagefile<ITweet> lst = AppServerProxy.searchTask(taskSearchDTO);

		// 转换成前端需要的集合
		Pagefile<MyPostDTO> collection = new Pagefile<MyPostDTO>();
		if (lst != null) {
			for (ITweet vo : lst.getRows()) {
				MyPostDTO myPostDTO = new MyPostDTO();
				Platform _Platform = vo.getPlatform();// Utility.getPlatform(vo.getSourceID());
				// // 媒体平台枚举
				IAccount account = vo.getAuthor();

				AccountDTO accountDTO = new AccountDTO();
				accountDTO.twitterType = Utility.getPlatform(_Platform);
				if (account != null) {
					accountDTO.ucode = account.getUcode(); // 用户头像ucode
					accountDTO.twitterType = Utility.getPlatform(account
							.getPlatform());// 账号平台类型
					accountDTO.uid = account.getUcode();
					if (account.getHead() != null) {
						if (_Platform == Platform.Tencent) {
							accountDTO.imgurl = account.getHead();
						} else {
							accountDTO.imgurl = account.getHead();
						}
					}
					accountDTO.name = account.getDisplayName();
					accountDTO.url = account.getHomeUrl();
					accountDTO.friends = account.getFriends(); // 关注
					accountDTO.weibos = account.getWeibos(); // 发布微博数
					accountDTO.followers = account.getFollowers();// 粉丝数
					accountDTO.summany = account.getDescription();
					accountDTO.isAjax = true; // 前台不用在异步加载
				}
				if (_Platform == Platform.Tencent
						|| _Platform == Platform.Renmin) {
					accountDTO.isAjax = false; // 前台不用在异步加载
				}
				if (vo.getSource() == null) {
					myPostDTO.isCreative = true;
				} else {
					myPostDTO.isCreative = false;
				}
				myPostDTO.account = accountDTO; // 微博账号
				myPostDTO.commentCount = vo.getComments();// getComtcount();
				// // 评论数
				myPostDTO.repostCount = vo.getReposts();// .getReplycount(); //
				// 转发数
				myPostDTO.text = vo.getText();// .getContent(); // 内容

				String name = myPostDTO.account.name + "：";
				int index = myPostDTO.text.indexOf(name);
				if (_Platform == Platform.Sina && index > -1) {
					myPostDTO.text = myPostDTO.text.substring(index + 1);
				}
				myPostDTO.createtime = DateHelper.formatDate(vo.getCreated());// .getPublishdate();//
				// 发布时间
				myPostDTO.url = vo.getUrl();// url链接
				myPostDTO.tid = vo.getTid();// 获取tid

				myPostDTO.source = vo.getFrom().getName();// 发布平台（来源）
				myPostDTO.sourceID = vo.getFrom().getId();// .getAppsource();
				// // 来源ID
				// myPostDTO.postImage = vo.getImgurl();// 图片集合
				myPostDTO.item = null;
				if (vo.getSource() != null) {
					MyPostDTO subPostDTO = new MyPostDTO();
					// TaskTwitterVO entity = null;
					try {
						// entity = StringUtils.parseRefcontent(vo);
						AccountDTO subAccountDTO = new AccountDTO();
						subAccountDTO.twitterType = Utility
								.getPlatform(_Platform);
						if (vo.getSource().getAuthor() != null) {

							subAccountDTO.name = vo.getSource().getAuthor()
									.getName();// entity.getAuthor();// 名称

						}
						subPostDTO.account = subAccountDTO;
						subPostDTO.text = vo.getSource().getText();// .getContent();
						// // 原帖内容

						subPostDTO.source = vo.getSource().getFrom().getName();// 发布平台（来源）
						subPostDTO.createtime = DateHelper.formatDate(vo
								.getSource().getCreated());// entity.getPublishdate();//
						// 原帖发布时间
						subPostDTO.commentCount = vo.getSource().getComments();// entity.getComtcount();
						// //
						// 原帖评论数
						subPostDTO.repostCount = vo.getSource().getReposts();// entity.getReplycount();
						// //
						// 原帖转发数
						subPostDTO.url = vo.getSource().getUrl();
						myPostDTO.item = subPostDTO;
					} catch (NumberFormatException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}

				collection.getRows().add(myPostDTO);
			}
			collection.setRealcount(lst.getRealcount());
		}
		return collection;
	}
}
