package com.xunku.actions.accountMonitor;

import java.util.List;

import com.xunku.actions.ActionBase;
import com.xunku.dao.account.AccountDao;
import com.xunku.dao.office.AccountWarnDao;
import com.xunku.daoImpl.account.AccountDaoImpl;
import com.xunku.daoImpl.office.AccountWarnDaoImpl;
import com.xunku.pojo.base.User;
import com.xunku.pojo.office.AccountWarn;

/***
 * 帐号监控预警
 *
 * @author shaoqun
 * @created Aug 18, 2014
 */
public class WarnAccountAction extends ActionBase{

	/*********************************************
	 * 不再用了
	 */
	
	
	@Override
	public Object doAction() {
		
		AccountWarn warn = new AccountWarn();
		AccountDao accDAO = new AccountDaoImpl();
		AccountWarnDao warnDAO = new AccountWarnDaoImpl();
		User _user = this.getUser().getBaseUser();
		
		int method = Integer.parseInt(this.get("method"));
		
		if(method == 1){										//获取预警信息
			int accid = Integer.parseInt(this.get("accid"));
			warn = accDAO.queryAccountById(accid);
			if(warn.getGroupName() == null){
				warn.setGroupName("");
			}
			if(warn.getReceiver() == null){
				warn.setReceiver("");
			}
			if(warn.getType() == null){
				warn.setType("");
			}
			return warn;
		}else if(method == 2){									//添加预警服务
			int accid = Integer.parseInt(this.get("accid"));
			String type = this.get("type");
			String topname = this.get("topname");
			String keyword = this.get("keyzu");
			String receiver = this.get("jsr");
			
			warn.setAccid(accid);
			warn.setGroupName(topname);
			warn.setKeyword(keyword);
			warn.setReceiver(receiver);
			warn.setType(type);
			warn.setCustomid(_user.getCustomID());
			
			int _int = warnDAO.insert(warn);
			if(_int == 0){
				return - 1;
			}
		}else if(method == 3){									//获取参照列表
			List<AccountWarn> list = warnDAO.queryAccountWarnList(_user.getCustomID());
			
			return list;
		}else if(method == 4){									//编辑预警
			int accid = Integer.parseInt(this.get("accid"));
			int id = Integer.parseInt(this.get("id"));
			String type = this.get("type");
			String topname = this.get("topname");
			String keyword = this.get("keyzu");
			String receiver = this.get("jsr");
			
			warn.setId(id);
			warn.setAccid(accid);
			warn.setGroupName(topname);
			warn.setKeyword(keyword);
			warn.setReceiver(receiver);
			warn.setType(type);
			warn.setCustomid(_user.getCustomID());
			
			boolean flag = warnDAO.update(warn);
			if(!flag){
				return -1;
			}
			
		}else if(method == 5){									//根据Id获取参照数据
			int id = Integer.parseInt(this.get("id"));
			warn = warnDAO.queryAccountWarnById(id);
			
			return warn;
		}else if(method == 6){									//取消预警
			int id = Integer.parseInt(this.get("id"));
			boolean flag = warnDAO.deleteById(id);
			if(!flag){
				return -1;
			}
		}
		
		return "true";
	}

}
