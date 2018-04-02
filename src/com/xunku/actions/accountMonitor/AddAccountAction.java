package com.xunku.actions.accountMonitor;

import java.util.ArrayList;
import java.util.List;

import com.xunku.actions.ActionBase;
import com.xunku.app.enums.Platform;
import com.xunku.app.interfaces.IAccount;
import com.xunku.app.monitor.AccountMonitor;
import com.xunku.app.result.Result;
import com.xunku.constant.PortalCST;
import com.xunku.dao.account.AccountDao;
import com.xunku.daoImpl.account.AccountDaoImpl;
import com.xunku.dto.accountMonitor.AccountListDTO;
import com.xunku.pojo.base.User;
import com.xunku.portal.controller.PermissionController;
import com.xunku.utils.AppServerProxy;

/**
 * 帐号监控--添加帐号监控信息
 * @author shaoqun
 *
 */
public class AddAccountAction extends ActionBase {

	@Override
	public Object doAction() {
		
		
		int method = Integer.parseInt(this.get("method"));
		User _user = this.getUser().getBaseUser();
		AccountDao accDAO = new AccountDaoImpl();
		int type = Integer.parseInt(this.get("type"));
		
		Platform platform = Platform.Sina;
		if(type == 1){
			platform = Platform.Tencent;
		}else if(type == 2){
			platform = Platform.Renmin;
		}
		if(method == 1){
			String textarea = this.get("textarea");
			//有一验证
			boolean isflag = accDAO.checkIsExsit(_user.getCustomID(), textarea);
			if(!isflag){
				Result<IAccount> result= AppServerProxy.getAccountByNameOnline(textarea, platform,_user);
				IAccount acc = result.getData();
				if(acc != null){
					AccountMonitor account = new AccountMonitor();
					account.setUserid(_user.getId());
					account.setNick(acc.getDisplayName());
					account.setUid(acc.getUcode());
					account.setCustomId(_user.getCustomID());
					account.setPlatform(platform);
					
					boolean flag = accDAO.insert(account);
					if(!flag){
						return 1;
					}else{
						return -1;
					}
				}else{
					if(result.getMessage().equals("用户不存在")){
						return 2;
					}else{
						return 4;
					}
					
				}
			}else{
				return 3;
			}
		}else if(method == 2){
			PermissionController controll = new PermissionController(this.getUser());
			int maxCount = controll.GetCustomConfigValue(this.getUser(),
					PortalCST.weibo_monitor_account_count);
			int accnum = accDAO.getPeriodAccountCount(_user.getId(),platform);
			System.out.println(accnum+"---"+maxCount);
			if(accnum >= maxCount){
				return -1;
			}
		}
		return "true";
	}

}
