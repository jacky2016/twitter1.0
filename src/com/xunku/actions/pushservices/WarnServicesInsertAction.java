package com.xunku.actions.pushservices;

import java.util.List;

import com.xunku.actions.ActionBase;
import com.xunku.app.controller.WarningController;
import com.xunku.app.enums.Platform;
import com.xunku.app.monitor.EventMonitor;
import com.xunku.app.parser.TweetUrlParserFactory;
import com.xunku.constant.PortalCST;
import com.xunku.dao.account.AccountDao;
import com.xunku.dao.event.EventDao;
import com.xunku.dao.office.AccountWarnDao;
import com.xunku.dao.office.EventWarnDao;
import com.xunku.dao.office.WeiboWarnDao;
import com.xunku.daoImpl.account.AccountDaoImpl;
import com.xunku.daoImpl.event.EventDaoImpl;
import com.xunku.daoImpl.office.AccountWarnDaoImpl;
import com.xunku.daoImpl.office.EventWarnDaoImpl;
import com.xunku.daoImpl.office.WeiboWarnDaoImpl;
import com.xunku.dto.eventMonitor.EventDTO;
import com.xunku.pojo.base.User;
import com.xunku.pojo.office.AccountWarn;
import com.xunku.pojo.office.EventWarn;
import com.xunku.pojo.office.WeiboWarn;
import com.xunku.portal.controller.PermissionController;
import com.xunku.utils.DateUtils;

/**
 * 预警服务--添加预警 (插入预警信息的)action类
 * 
 * @author shangwei
 * 
 */
public class WarnServicesInsertAction extends ActionBase {
	WeiboWarnDao warnDao = new WeiboWarnDaoImpl();

	@Override
	public Object doAction() {

		int moduleType = Integer.parseInt(this.get("moduleType"));
		if (moduleType == 0) {
			String  aler=this.get("alertid");
			if(aler==null){
				aler="0";
			} 
			int alertid = Integer.parseInt(aler);
			int  isDelete=0;
			if(this.get("isdelete")!=null){
				isDelete=Integer.parseInt(this.get("isdelete"));
			}

			// 添加预警 
			if (alertid == 0) {
				String  author=this.get("author");
				String text=this.get("text");
				int currepost=Integer.parseInt(this.get("currepost"));
				int  curcomment=Integer.parseInt(this.get("curcomment"));
				User user = this.getUser().getBaseUser();
				
				int customid = this.getUser().getCustom().getId();
				// 在这判断预警个数 -3表示预警最大数,-2微博删除,-1数据库微博存在,0数据库错误,>0成功
				PermissionController controll = new PermissionController(this.getUser());
				int maxCount = controll.GetCustomConfigValue(this.getUser(),
						PortalCST.weibo_alert_count);
				WarningController warnControll= new WarningController();
				int currentCount =  warnControll.getCurrentWeiboWarnCount (customid);
				if(currentCount>=maxCount){
					return -3;
				}

				String receiver = this.get("nameids");
				String type = this.get("typeids");
				int comment = Integer.parseInt(this.get("commentNum"));
				int repost = Integer.parseInt(this.get("repostNum"));
				String time = this.get("time") + " 23:59:59";

				WeiboWarn warn = new WeiboWarn();
				
				warn.setCurrepost(currepost);
				warn.setCurcomment(curcomment);
				warn.setComment(comment);
				warn.setAuthor(author);
				warn.setText(text);
				warn.setRepost(repost);
				warn.setType(type);
				warn.setReceiver(receiver);
				warn.setCustomid(customid);
				int p = Integer.parseInt(this.get("platform"));
				Platform pf = Platform.Sina;
				if (p == 2) {
					pf = Platform.Tencent;
				} else if (p == 5) {
					pf = Platform.Renmin;
				}
				warn.setPlatform(pf);
				String tid = this.get("tid");
				if (tid.startsWith("http://")) {
					tid = TweetUrlParserFactory.createTweetUrl(tid).getTid();
				}
				warn.setTid(tid);
				warn.setTime(DateUtils.dateStringToInteger(time));
				WarningController controller = new WarningController();
				return controller.addWeiboWarning(warn, user);
			}
			// 删除预警
			else if (alertid > 0&&isDelete==0) {
				//boolean flag = warnDao.deleteById(alertid);
				 WarningController controller = new WarningController();
				 String tid = this.get("tid");
				  int flags= controller.getCanenlWeiboWarnServices(tid,this.getUser().getCustom().getId());
				  return flags;
				//int i = flag ? 1 : 0;
				 // flag ==500 代表是假删除 需要alertid 保留
				//return i;
			}
			//已经发生的预警 重新update下
			else if(alertid>0&&isDelete==1){
				
				int customid = this.getUser().getCustom().getId();
				// 在这判断预警个数 -3表示预警最大数,-2微博删除,-1数据库微博存在,0数据库错误,>0成功
				PermissionController controll = new PermissionController(this.getUser());
				int maxCount = controll.GetCustomConfigValue(this.getUser(),
						PortalCST.weibo_alert_count);
				WarningController warnControll= new WarningController();
				int currentCount =  warnControll.getCurrentWeiboWarnCount (customid);
				if(currentCount>=maxCount){
					return -3;
				}

				String receiver = this.get("nameids");
				String type = this.get("typeids");
				int comment = Integer.parseInt(this.get("commentNum"));
				int repost = Integer.parseInt(this.get("repostNum"));
				String time = this.get("time") + " 23:59:59";

				WeiboWarn warn = new WeiboWarn();
				warn.setId(alertid);
				warn.setComment(comment);
				warn.setRepost(repost);
				warn.setType(type);
				warn.setReceiver(receiver);
				warn.setCustomid(customid);
				int p = Integer.parseInt(this.get("platform"));
				Platform pf = Platform.Sina;
				if (p == 2) {
					pf = Platform.Tencent;
				} else if (p == 5) {
					pf = Platform.Renmin;
				}
				warn.setPlatform(pf);
				String tid = this.get("tid");
				if (tid.startsWith("http://")) {
					tid = TweetUrlParserFactory.createTweetUrl(tid).getTid();
				}
				warn.setTid(tid);
				warn.setTime(DateUtils.dateStringToInteger(time));
				warn.setDelete(false)	;
				//-------
				 WarningController controller = new WarningController();
			  return	 controller.updateAlreadyWarn(warn);
				
			}
			return 0;
		} else if (moduleType == 1) {//事件预警
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
		} else if (moduleType == 2) {//帐号预警
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
		}
		
		
		//预警信息中 的取消预警的事件
		//return   0 是取消预警失败 1 是取消预警成功
		else if(moduleType==-1){   
			int alertid = Integer.parseInt(this.get("alertid"));
			User user = this.getUser().getBaseUser();
			int  isDelete=Integer.parseInt(this.get("isdelete"));
			// 删除预警
	   if (alertid > 0&&isDelete==0) {
				//boolean flag = warnDao.deleteById(alertid);
		        String tid=this.get("yujingTid");
				 WarningController controller = new WarningController();
				  int flags= controller.getCanenlWeiboWarnServices(tid,this.getUser().getCustom().getId());
				  return flags;
				//int i = flag ? 1 : 0;
				//return i;
			}
			//已经发生的预警 重新update下
			else if(alertid>0&&isDelete==1){
				
				int customid = this.getUser().getCustom().getId();
				// 在这判断预警个数 -3表示预警最大数,-2微博删除,-1数据库微博存在,0数据库错误,>0成功
				PermissionController controll = new PermissionController(this.getUser());
				int maxCount = controll.GetCustomConfigValue(this.getUser(),
						PortalCST.weibo_alert_count);
				WarningController warnControll= new WarningController();
				int currentCount =  warnControll.getCurrentWeiboWarnCount (customid);
				if(currentCount>=maxCount){
					return -3;
				}

				String receiver = this.get("nameids");
				String type = this.get("typeids");
				int comment = Integer.parseInt(this.get("commentNum"));
				int repost = Integer.parseInt(this.get("repostNum"));
				String time = this.get("time") + " 23:59:59";

				WeiboWarn warn = new WeiboWarn();
				warn.setComment(comment);
				warn.setRepost(repost);
				warn.setType(type);
				warn.setReceiver(receiver);
				warn.setCustomid(customid);
				int p = Integer.parseInt(this.get("platform"));
				Platform pf = Platform.Sina;
				if (p == 2) {
					pf = Platform.Tencent;
				} else if (p == 5) {
					pf = Platform.Renmin;
				}
				warn.setPlatform(pf);
				String tid = this.get("tid");
				if (tid.startsWith("http://")) {
					tid = TweetUrlParserFactory.createTweetUrl(tid).getTid();
				}
				warn.setTid(tid);
				warn.setTime(DateUtils.dateStringToInteger(time));
				warn.setDelete(false)	;
				//-------
				 WarningController controller = new WarningController();
				 controller.updateAlreadyWarn(warn);
				
			}
			
			//?????????
		/* 
	   String tid=this.get("yujingTid");
		 WarningController controller = new WarningController();
		  int flag= controller.getCanenlWeiboWarnServices(tid,this.getUser().getCustom().getId());
				return flag;
		*/
		
		}
		
		/**
		 *  预警列表的修改 事件
		 *   return  1 为 修改成功 0 为修改失败
		 * */
		else if(moduleType==3){
			   int repostNum=Integer.parseInt(this.get("repostNum"));
			   int commentNum=Integer.parseInt(this.get("commentNum"));
			   String  nameids=this.get("nameids");
			   String  typeids=this.get("typeids");
			   String  endTime=this.get("time")+" 23:59:59";
			   int  id =Integer.parseInt(this.get("alertid"));
			   //int setRepost=Integer.parseInt(this.get("setRepost"));
			   //int setComment=Integer.parseInt(this.get("setComment"));
			   WeiboWarn  warn = new WeiboWarn();
			   warn.setId(id);
			   //warn.setRHappen(0);
			   //warn.setCHappen(0);
			   warn.setRepost(repostNum);
			   warn.setComment(commentNum);
			   warn.setReceiver(nameids);
			   warn.setType(typeids);
			   warn.setEndTime(endTime);
			   WarningController controller = new WarningController();
			   int flag=controller.updateWarnList(warn);
			   return  flag;
		}

		/**
		 *  预警列表的删除事件
		 *  @return  flag >0 代表删除成功   flag=0 则删除失败
		 * */
		else if(moduleType==4){
			 int flag=0;
			 int id=Integer.parseInt(this.get("deleteID"));
			 WarningController controller = new WarningController();
		     flag= controller.deleteWeiboWarnList(id,this.getUser().getCustom().getId());
			 return  flag;
		}
		
		return 0;
	}

	
}
