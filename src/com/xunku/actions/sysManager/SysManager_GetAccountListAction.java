package com.xunku.actions.sysManager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.xunku.actions.ActionBase;
import com.xunku.app.Utility;
import com.xunku.app.enums.Platform;
import com.xunku.app.helpers.DateHelper;
import com.xunku.app.interfaces.IAccount;
import com.xunku.app.result.Result;
import com.xunku.dao.base.AccountAuthsDao;
import com.xunku.dao.base.AccountDao;
import com.xunku.daoImpl.base.AccountAuthsDaoImpl;
import com.xunku.daoImpl.base.AccountDaoImpl;
import com.xunku.dto.MyPostDTO;
import com.xunku.dto.PagerDTO;
import com.xunku.dto.base.WeiboAccountDTO;
import com.xunku.dto.sysManager.SysManager_AccountDTO;
import com.xunku.pojo.base.Account;
import com.xunku.pojo.base.AccountAuths;
import com.xunku.pojo.base.AccountInfo;
import com.xunku.pojo.base.Pager;
import com.xunku.pojo.base.User;
import com.xunku.utils.AppServerProxy;
import com.xunku.utils.DateUtils;
import com.xunku.utils.Pagefile;

/*
 * SysManager_GetAccountListAction 系统管理获取绑定账号列表
 * @author: hjian
 */
public class SysManager_GetAccountListAction extends ActionBase {

	@Override
	public Object doAction() {

		Pagefile<SysManager_AccountDTO> mtfile = new Pagefile<SysManager_AccountDTO>();
		AccountAuthsDao dao = new AccountAuthsDaoImpl();

		Pager pager = new Pager();
		pager.setPageIndex(Integer.parseInt(this.get("pageIndex")));
		pager.setPageSize(Integer.parseInt(this.get("pageSize")));
		User user = this.getUser().getBaseUser();

		Pagefile<AccountAuths> list = dao.queryPagefile(pager, user.getId()); // 获取授权集合

		for (AccountAuths item : list.getRows()) {
			SysManager_AccountDTO dt = new SysManager_AccountDTO();
			dt.setId(item.getId());
			dt.setUid(item.getUid());

			int day = 0;
			if (item.getAuthTime() != null) {
				int cycle = DateUtils.expireDay(item.getExpiresin()); // 授权周期
				String stime = DateHelper.formatDBTime(item.getAuthTime()); // 授权日期
				String etime = DateUtils.addDay(stime, cycle); // 授权结束日期

				SimpleDateFormat sdf = new SimpleDateFormat(
						"yyyy-MM-dd HH:mm:ss");
				Date beforDate = null, nowDate = null;
				String nowtime = "";

				try {
					nowtime = DateUtils.nowDateFormat("yyyy-MM-dd HH:mm:ss");// 当前日期
					beforDate = sdf.parse(etime);
					nowDate = sdf.parse(nowtime);
				} catch (ParseException e) {
					e.printStackTrace();
				}

				if (nowDate.getTime() < beforDate.getTime()) {
					day = (int) ((beforDate.getTime() - nowDate.getTime()) / 1000 / 60 / 60 / 24) + 1;
					dt.setTwitterState("有效帐号");
					dt.setLiveDay(String.valueOf(day));
				} else {
					dt.setTwitterState("无效帐号");
					dt.setLiveDay("0");
				}
			} else {
				dt.setTwitterState("无效账号");
				dt.setLiveDay("0");
			}

			if (Utility.getPlatform(item.getPlatform()).equals(Platform.Sina)) {
				dt.setWbType("新浪微博");
			} else if (Utility.getPlatform(item.getPlatform()).equals(
					Platform.Tencent)) {
				dt.setWbType("腾讯微博");
			} else if (Utility.getPlatform(item.getPlatform()).equals(
					Platform.Renmin)) {
				dt.setWbType("人民微博");
				dt.setTwitterState("有效帐号");
				dt.setLiveDay("永久");
			} else {
				dt.setWbType("未知");
			}

			dt.setNickName(item.getName());
			/*
			Result<IAccount> account = AppServerProxy.getAccountByUcode(item
					.getUcode(), Utility.getPlatform(item.getPlatform()), user);
			if (account.getErrCode() == 0) {
				dt.setLoginName(account.getData().getName());
				dt.setNickName(account.getData().getDisplayName());
			} else {
				dt.setLoginName("");
				dt.setNickName("");
			}
			*/
			
			mtfile.getRows().add(dt);
		}

		mtfile.setRealcount(list.getRealcount());
		mtfile.setErr(list.getErr());
		return mtfile;
	}

}
