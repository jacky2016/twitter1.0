package com.xunku.actions.pushservices;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.xunku.actions.ActionBase;
import com.xunku.dao.push.PushDao;
import com.xunku.daoImpl.push.PushDaoImpl;
import com.xunku.pojo.base.User;
import com.xunku.pojo.push.Subscriber;
import com.xunku.utils.DateUtils;

/**
 * 预警服务--微博推送action类
 * 
 * @author shangwei 微博推送中新建一条推送任务保存到数据库中去的action类
 */
public class PushServicesAddAction extends ActionBase {

	PushDao pushdao = new PushDaoImpl();

	@Override
	public Object doAction() {

		String queryStr = this.get("queryConditions");
		User user = this.getUser().getBaseUser();
		int userid = user.getId();
		/**
		 * Author ShangWei * Description 添加一条推送服务信息
		 */
		if (queryStr.equals("save")) {
			Subscriber sr = new Subscriber();
			sr.setName(this.get("taskName"));
			sr.setCreator(userid);
			sr.setType(Integer.parseInt(this.get("taskTypes")));
			sr.setSendMail(true);
			String mcontent = (String) this.get("mcontent");
			sr.setCustomid(user.getCustomID());
			List<Integer> tasks = new ArrayList<Integer>();
			String[] mcs = mcontent.split(",");
			for (String every : mcs) {
				tasks.add(Integer.parseInt(every));
			}
			if (Integer.parseInt(this.get("taskTypes")) == 0) {
				sr.setTasks(tasks);
			} else {
				sr.setEvents(tasks);
			}
			String[] remans = this.get("reman").split(";");
			List<String> mans = Arrays.asList(remans);
			sr.setContacts(mans);
			sr.setTopN(Integer.parseInt(this.get("sendmailsnu")));
			sr.setPeriodCount(Integer.parseInt(this.get("timenum")));
			sr.setPeriodType(Integer.parseInt(this.get("timetps")));
			//sr.setFirstRunTime(this.get("timings"));
			sr.setFirstRunTime(DateUtils.stringToDate("yyyy-MM-dd HH:mm:ss",this.get("timings")));
			sr.setStop(true);
			pushdao.insert(sr);
			return "保存成功";
		}
		return null;
	}
}
