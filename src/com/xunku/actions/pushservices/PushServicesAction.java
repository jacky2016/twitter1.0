package com.xunku.actions.pushservices;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.xunku.actions.ActionBase;
import com.xunku.dao.base.UserDao;
import com.xunku.dao.event.EventDao;
import com.xunku.dao.push.PushDao;
import com.xunku.dao.task.TaskDao;
import com.xunku.daoImpl.base.UserDaoImpl;
import com.xunku.daoImpl.event.EventDaoImpl;
import com.xunku.daoImpl.push.PushDaoImpl;
import com.xunku.daoImpl.task.TaskDaoImpl;
import com.xunku.dto.PagerDTO;
import com.xunku.dto.TaskNameDTO;
import com.xunku.dto.pubSentiment.TaskDTO;
import com.xunku.dto.pubSentiment.TaskGroupsDTO;
import com.xunku.dto.pushservices.EventDTO;
import com.xunku.dto.pushservices.PushServicesDTO;
import com.xunku.dto.pushservices.PushUserDTO;
import com.xunku.dto.pushservices.SubscriberDTO;
import com.xunku.pojo.push.Subscriber;
import com.xunku.utils.Pagefile;

/**
 * 预警服务--微博推送action类
 * 
 * @author shangwei
 * 
 */
public class PushServicesAction extends ActionBase {

	PushDao pushdao = new PushDaoImpl();
	TaskDao taskDao = new TaskDaoImpl();
	UserDao userDao = new UserDaoImpl();
	EventDao eventDao = new EventDaoImpl();

	@Override
	public Object doAction() {
		
		String queryStr = this.get("queryConditions");
		int userid = this.getUser().getBaseUser().getId();
		// modify by wujian
		int customid = this.getUser().getBaseUser().getCustomID();// .getCustom().getId();
		
		/**
		 * Author ShangWei * Description 接受人通讯录调用方法
		 */
		if (queryStr.equals("recevieManMethod")) {
			List<PushServicesDTO> li = new ArrayList<PushServicesDTO>();
			List<PushUserDTO> dtos = userDao.queryUserByUID(userid);
			if (dtos != null) {
				for (PushUserDTO dto : dtos) {
					PushServicesDTO pp = new PushServicesDTO();
					pp.id = dto.getId();
					pp.pushservicesName = dto.getUseName();
					pp.pushservicesMails = dto.getEmail();
					li.add(pp);
				}// for结束
			}
			return li;
		}
		
		
		/**
		 * Author ShangWei * Description 推送服务中的检测推送任务名字(包括更新or新建)
		 * 在同一企业级下不能重复
		 * return    flag  的值 大于0 的代表重复了，  等于0 表示没有重复的
		 */
		if(queryStr.equals("checkNameRepeat")){
				String   taskName=this.get("pushTaskName");
				int   taskID=Integer.parseInt(this.get("pushTaskID"));
			   // taskID ==0 即为添加的    taskID>0即为 修改的
				String  insertSQL="select ID from dbo.Task_Subscriber where customid = "+customid+" and Name =N'"+taskName+"'";
				String  updateSQL="select ID from dbo.Task_Subscriber where customid =  "+customid+"   and ID !=    "+taskID+"  and Name =N'"+taskName+"'";
			  //flag  ==0  表示不重复的   flag>0  代表重复了
				if(taskID==0){    
				 int flag=  taskDao.checkNameRepeat(insertSQL);
				 return flag;
			  }else{
				  int flag= taskDao.checkNameRepeat(updateSQL);
				  return flag;
			  }
		}
		

		// 推送服务中的检测内容添加按钮的逻辑
		/**
		 * Author ShangWei * Description 推送服务中的检测内容添加按钮的逻辑 日常监控--->任务
		 */
		if (queryStr.equals("work")) {
			List<TaskNameDTO> list = new ArrayList<TaskNameDTO>();
			List<TaskGroupsDTO> catlogs = taskDao.queryByAll(userid);
			if (catlogs != null) {
				for (TaskGroupsDTO catlog : catlogs) {
					TaskNameDTO tnd = new TaskNameDTO();
					tnd.id = catlog.getId();
					tnd.name = catlog.getGroupname();
					List<TaskGroupsDTO> groups = catlog.getGroups();
					for (TaskGroupsDTO group : groups) {
						TaskNameDTO groupdto = new TaskNameDTO();
						groupdto.id = group.getId();
						groupdto.name = group.getGroupname();
						List<TaskDTO> taskdtos = group.getTasks();
						for (TaskDTO taskdto : taskdtos) {
							TaskNameDTO task = new TaskNameDTO();
							task.id = taskdto.getId();
							task.name = taskdto.getName();
							groupdto.catlogs.add(task);
						} // for 任务结束
						tnd.catlogs.add(groupdto);
					} // for 组结束
					list.add(tnd);
				} // for 类结束
			}
			return list;
		}

		/**
		 * Author ShangWei * Description 推送服务中的检测内容添加按钮的逻辑 事件监控--->逻辑
		 */
		if (queryStr.equals("handle")) {
			List<TaskNameDTO> list = new ArrayList<TaskNameDTO>();
			List<EventDTO> eventdtos = eventDao.queryByPush(userid);
			if (eventdtos != null) {
				for (EventDTO e : eventdtos) {
					TaskNameDTO tnd = new TaskNameDTO();
					tnd.id = e.getId();
					tnd.name = e.getName();
					int  gg=e.getPlatform();
					if(gg==1){
						tnd.platformNames="新浪";
					}else if(gg==2){
						tnd.platformNames="腾讯";
					}else {
						tnd.platformNames="人民";
					}
					list.add(tnd);
				}
			}
			return list;
		}

		/*
		 * if(false){ //BIG 1 类 TaskNameDTO tnd=new TaskNameDTO(); tnd.id=1;
		 * tnd.name="世界"; //small 1 组 TaskNameDTO tndx1=new TaskNameDTO();
		 * tndx1.id=2; tndx1.name="中国"; //ss1 TaskNameDTO tndxx1=new
		 * TaskNameDTO(); tndxx1.id=5; tndxx1.name="河南"; //ss2 TaskNameDTO
		 * tndxx2=new TaskNameDTO(); tndxx2.id=6; tndxx2.name="北京";
		 * tndx1.catlogs.add(tndxx1); tndx1.catlogs.add(tndxx2); //s2
		 * TaskNameDTO tndx2=new TaskNameDTO(); tndx2.id=3; tndx2.name="美国";
		 * //ss3 TaskNameDTO tndxx3=new TaskNameDTO(); tndxx3.id=7;
		 * tndxx3.name="弗罗里达"; //ss4 TaskNameDTO tndxx4=new TaskNameDTO();
		 * tndxx4.id=8; tndxx4.name="加利福尼亚"; tndx2.catlogs.add(tndxx3);
		 * tndx2.catlogs.add(tndxx4); //s3 TaskNameDTO tndx3=new TaskNameDTO();
		 * tndx3.id=4; tndx3.name="韩国"; tnd.catlogs.add(tndx1);
		 * tnd.catlogs.add(tndx2); tnd.catlogs.add(tndx3);
		 * 
		 * 
		 * //BIG 2 类 TaskNameDTO tndtnd=new TaskNameDTO(); tndtnd.id=9;
		 * tndtnd.name="文化"; //s1 TaskNameDTO tndtndx1=new TaskNameDTO();
		 * tndtndx1.id=10; tndtndx1.name="唱歌"; //ss1 TaskNameDTO tndtndxx1=new
		 * TaskNameDTO(); tndtndxx1.id=14; tndtndxx1.name="KTV";
		 * tndtndx1.catlogs.add(tndtndxx1); //ss2 TaskNameDTO tndtndxx2=new
		 * TaskNameDTO(); tndtndxx2.id=15; tndtndxx2.name="VCD";
		 * tndtndx1.catlogs.add(tndtndxx2);
		 * 
		 * //s2 TaskNameDTO tndtndx2=new TaskNameDTO(); tndtndx2.id=11;
		 * tndtndx2.name="跳舞"; //sss3 TaskNameDTO tndtndxx3=new TaskNameDTO();
		 * tndtndxx3.id=16; tndtndxx3.name="钢管舞";
		 * tndtndx2.catlogs.add(tndtndxx3);
		 * 
		 * 
		 * tndtnd.catlogs.add(tndtndx1); tndtnd.catlogs.add(tndtndx2); List<TaskNameDTO>
		 * ll =new ArrayList<TaskNameDTO>(); ll.add(tnd); ll.add(tndtnd);
		 * return ll; }
		 */

		/**
		 * Author ShangWei * Description 推送服务首页列表展示
		 */
		if (queryStr.equals("queryAll")) {
			PagerDTO pd = new PagerDTO();
			pd.pageIndex = Integer.parseInt(this.get("pageIndex"));
			pd.pageSize = Integer.parseInt(this.get("pageSize"));
			Pagefile<Subscriber> pf = pushdao.queryByUserId(customid, pd);
			// 转化前台
			Pagefile<PushServicesDTO> spf = new Pagefile<PushServicesDTO>();
			if (pf != null) {
				List<Subscriber> subs = pf.getRows();
				spf.setRealcount(pf.getRealcount());
				for (Subscriber sub : subs) {
					PushServicesDTO pushServicesDTO = new PushServicesDTO();
					pushServicesDTO.id = sub.getId();
					pushServicesDTO.pushservicesName = sub.getName();
					pushServicesDTO.kinds = sub.getType();
					if (sub.isStop()) {
						pushServicesDTO.pushStatus = 1;
					} else {
						pushServicesDTO.pushStatus = 0;
					}
					spf.getRows().add(pushServicesDTO);
				}
			}
			return spf;
		}

		/**
		 * Author ShangWei * Description 修改返回这个对象
		 */
		if (queryStr.equals("ModifyID")) {
			SubscriberDTO sdto = pushdao.queryBySubID(Integer.parseInt(this
					.get("subID")));
			return sdto;
		}

		/**
		 * Author ShangWei * Description 判断 是否接受人为空或者任务为空，两者只要其中一个为空，就不能让其推送
		 * // 0   是都不为空  反而则为1
		 */
		if (queryStr.equals("checkSubscriberStatus")) {
			int subId = Integer.parseInt(this.get("subId"));
			boolean flag = pushdao.checkSubscriberStatus(subId);
			if (flag) {
				return 0;
			} else {
				return 1;
			}
		}

		return null;
	}

}
