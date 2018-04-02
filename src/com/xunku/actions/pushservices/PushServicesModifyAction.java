package com.xunku.actions.pushservices;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.xunku.actions.ActionBase;
import com.xunku.dao.push.PushDao;
import com.xunku.daoImpl.push.PushDaoImpl;
import com.xunku.pojo.push.Subscriber;
import com.xunku.utils.DateUtils;

/**
 * 预警服务--微博推送action类
 * @author shangwei
 *微博推送中新修改一条推送任务保存到数据库中去的action类
 */
public class PushServicesModifyAction  extends   ActionBase {

	  PushDao  pushdao= new PushDaoImpl();
	@Override
	public Object doAction() {
		int userid=this.getUser().getBaseUser().getId();
		 String  queryStr=this.get("queryConditions");
			/**Author  ShangWei
			 * * Description 修改返回这个对象然后在保存到数据库中
			 */
		 if(queryStr.equals("update")){
				Subscriber  sr=new Subscriber();
		   		sr.setId(Integer.parseInt(this.get("tkID")));
		   		 sr.setName( this.get("taskName"));
		   		 sr.setCreator(userid);
		   		 sr.setType( Integer.parseInt(this.get("taskTypes")));
		   		 sr.setSendMail(true);
		   		 String  mcontent=(String)this.get("mcontent");
		   		 List<Integer> tasks=new ArrayList<Integer>();
		   		    String []  mcs=mcontent.split(",");
		   		  if(mcs.length!=0){
		   		    for(String every  : mcs){
		   		    	 tasks.add(Integer.parseInt(every));
		   		    }
		  		}
		   		    if(Integer.parseInt(this.get("taskTypes"))==0){
		   	   		    sr.setTasks(tasks);
		   		    }else{
		   	   		    sr.setEvents(tasks);
		   		    }
		   		       String []  remans=this.get("reman").split(";");
		   		      List<String> mans= Arrays.asList(remans);
		   		      sr.setContacts(mans);
		   		      sr.setTopN(Integer.parseInt(this.get("sendmailsnu")));
		   		      sr.setPeriodCount(  Integer.parseInt(this.get("timenum")));
		   		      sr.setPeriodType(Integer.parseInt(this.get("timetps")));
		   		      //sr.setFirstRunTime(this.get("timings"));
		   		      sr.setFirstRunTime(DateUtils.stringToDate("yyyy-MM-dd HH:mm:ss",this.get("timings")));
		   		      //修改的话 不管之前是否推送开启还是关闭， 只要修改都要开启推送
		   		      sr.setStop(true);
		   		      pushdao.updateSubscriber(sr);
			 return "修改成功";
		 }
		return null;
	}
}
