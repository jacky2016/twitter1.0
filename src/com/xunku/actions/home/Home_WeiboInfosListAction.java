package com.xunku.actions.home;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import com.xunku.actions.ActionBase;
import com.xunku.app.Utility;
import com.xunku.app.helpers.DateHelper;
import com.xunku.app.interfaces.IAccount;
import com.xunku.app.result.BeTrendResult;
import com.xunku.app.result.Result;
import com.xunku.dao.base.AccountDao;
import com.xunku.dao.home.WeiboInfoDao;
import com.xunku.daoImpl.base.AccountDaoImpl;
import com.xunku.daoImpl.home.WeiboInfoDaoImpl;
import com.xunku.dto.home.WeiboInfosListItemDTO;
import com.xunku.pojo.base.Account;
import com.xunku.pojo.base.AccountInfo;
import com.xunku.pojo.home.WeiboInfo;
import com.xunku.utils.AppServerProxy;
import com.xunku.utils.DateUtils;

/*
 * 获取首页模块用户块信息
 * @author sunao
 */
public class Home_WeiboInfosListAction extends ActionBase {

	WeiboInfoDao weiboInfoDao = new WeiboInfoDaoImpl();
	AccountDao weiboAccountDao = new AccountDaoImpl();

	public Home_WeiboInfosListAction() {
	}

	@Override
	public Result<List<WeiboInfosListItemDTO>> doAction() {
		// TODO Auto-generated method stub

		/*
		 * 首页获取块以及账号业务 1.根据UserID获取账号的块信息 2.根据账号ID批量获取账号信息
		 */

		int userid = this.getUser().getBaseUser().getId();

		List<WeiboInfo> infos = weiboInfoDao.queryByUserID(userid);
		List<AccountInfo> weiboAccountinfos = getweiboAccounts(infos);
		String errorUser = "";
		// 组装前端使用的DTO对象
		List<WeiboInfosListItemDTO> lst = new ArrayList<WeiboInfosListItemDTO>();
		Result<List<WeiboInfosListItemDTO>> results = new Result<List<WeiboInfosListItemDTO>>();
		for (WeiboInfo info : infos) {
			for (AccountInfo aInfo : weiboAccountinfos) {
				if (info.getAccountID() == aInfo.getId()) {
					Result<IAccount> result = AppServerProxy
							.getAccountByUcodeOnline(aInfo.getUid(), aInfo
									.getPlatform(), this.getUser()
									.getBaseUser());
					if (result.getErrCode() == 0) {
						IAccount acc = result.getData();
						WeiboInfosListItemDTO entity = new WeiboInfosListItemDTO();

						// 验证账号是否过期
						int cycle = DateUtils.expireDay(aInfo.getExpiresin()); // 授权周期
						String stime = DateHelper.formatDBTime(aInfo
								.getAuthTime()); // 授权日期
						String etime = DateUtils.addDay(stime, cycle) + ":00"; // 授权结束日期

						SimpleDateFormat sdf = new SimpleDateFormat(
								"yyyy-MM-dd HH:mm:ss");
						Date beforDate = null, nowDate = null;
						String nowtime = "";

						try {
							nowtime = DateUtils
									.nowDateFormat("yyyy-MM-dd HH:mm:ss");// 当前日期
							beforDate = sdf.parse(etime);
							nowDate = sdf.parse(nowtime);
						} catch (ParseException e) {
							e.printStackTrace();
						}

						if (nowDate.getTime() < beforDate.getTime()) {
							entity.setExpire(false);
						} else {
							entity.setExpire(true);
						}

						entity.setId(info.getId());
						entity.setUid(acc.getUcode());
						entity.setAccountid(info.getAccountID());
						entity.setClosed(info.isClosed());
						entity.setExpand(info.isExpand());
						entity
								.setType(Utility.getPlatform(aInfo
										.getPlatform()));
						entity.setName(aInfo.getName());
						// 如果是认证用户则需要调用API获得趋势信息，否则获得普通信息
						if (aInfo.isVerify()) {
							BeTrendResult btr = AppServerProxy
									.getAccountBeTrend(aInfo.getUid(), aInfo
											.getPlatform(), this.getUser()
											.getBaseUser());
							if (btr != null) {
								entity.setStatusCount(btr.getStatuses_count());// 微博数
								entity.setTranspond(btr.getRepost_count());// 转发数
								entity.setComment(btr.getComments_count());// 评论
								entity.setLike(btr.getFollowers_count());// ;粉丝
								entity.setAttention(btr.getFriends_count());// 关注
							}
						} else {
							entity.setStatusCount(acc.getWeibos());// 微博数
							entity.setTranspond(0);// 转发数
							entity.setComment(0);// 评论
							entity.setLike(acc.getFollowers());// ;粉丝
							entity.setAttention(acc.getFriends());// 关注
						}

						lst.add(entity);
						results.setData(lst);
						break;
					} else {
						errorUser += aInfo.getName() + " ";
						results.setErrCode(1);
						results.setError(errorUser);
					}
				}
			}
		}

		return results;
	}

	private List<AccountInfo> getweiboAccounts(List<WeiboInfo> infos) {
		int[] accounts = new int[infos.size()];
		for (int i = 0; i < infos.size(); i++) {
			accounts[i] = infos.get(i).getAccountID();
		}
		List<AccountInfo> accInfos = new ArrayList<AccountInfo>();
		if (accounts.length > 0)
			accInfos = weiboAccountDao.queryById(accounts);
		return accInfos;

	}

}
