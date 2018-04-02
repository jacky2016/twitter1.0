package com.xunku.actions.eventMonitor;

import com.xunku.actions.ActionBase;
import com.xunku.dao.office.EventWarnDao;
import com.xunku.daoImpl.office.EventWarnDaoImpl;
import com.xunku.pojo.base.User;
import com.xunku.pojo.office.EventWarn;

/***
 * 事件监控预警
 *
 * @author shaoqun
 * @created Aug 18, 2014
 */
public class WarnEventAction extends ActionBase {

	/*********************************************
	 * 不再用了
	 */
	
	@Override
	public Object doAction() {
		
		EventWarnDao warnDao = new EventWarnDaoImpl();
		User _user = this.getUser().getBaseUser();
		
		int method = Integer.parseInt(this.get("method"));
		EventWarn warn = new EventWarn();
		if(method == 1){								//根据id取得预警信息
			int id = Integer.parseInt(this.get("id"));
			warn = warnDao.queryEventByEventid(id);
			
			if(warn.getReceiver() == null){
				warn.setReceiver("");
			}
			if(warn.getType() == null){
				warn.setType("");
			}
			return warn;
		}else if(method == 2){							//添加预警信息
			int eventid = Integer.parseInt(this.get("eventid"));
			int weibnum = Integer.parseInt(this.get("weibnum"));
			String receiver = this.get("receiver");
			String send = this.get("send");
			warn.setEventid(eventid);
			warn.setWeibo(weibnum);
			warn.setReceiver(receiver);
			warn.setType(send);
			warn.setCustomid(_user.getCustomID());
			
			int _int = warnDao.insert(warn);
			if(_int == 0){
				return -1;
			}
		}else if(method == 3){							//根据id编辑预警信息
			int id = Integer.parseInt(this.get("id"));
			int eventid = Integer.parseInt(this.get("eventid"));
			int weibnum = Integer.parseInt(this.get("weibnum"));
			String receiver = this.get("receiver");
			String send = this.get("send");
			
			warn.setId(id);
			warn.setEventid(eventid);
			warn.setWeibo(weibnum);
			warn.setReceiver(receiver);
			warn.setType(send);
			warn.setCustomid(_user.getCustomID());
			
			boolean flag = warnDao.update(warn);
			if(flag == false){
				return -1;
			}					
		}else if(method == 4){
			int id = Integer.parseInt(this.get("id"));
			boolean flag = warnDao.deleteById(id);
			if(!flag){
				return -1;
			}
		}
		
		return "true";
	}

}
