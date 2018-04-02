package com.xunku.actions.eventMonitor;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import com.xunku.actions.ActionBase;
import com.xunku.app.AppContext;
import com.xunku.app.enums.Platform;
import com.xunku.app.enums.WordCloudEnum;
import com.xunku.app.manager.PoolManager;
import com.xunku.app.model.Pooling;
import com.xunku.app.monitor.EventMonitor;
import com.xunku.constant.PortalCST;
import com.xunku.dao.event.EventDao;
import com.xunku.daoImpl.event.EventDaoImpl;
import com.xunku.pojo.base.User;
import com.xunku.portal.controller.PermissionController;

/**
 * 事件监控--增加事件列表信息
 * 
 * @author shaoqun
 */

public class AddEventAction extends ActionBase {

	@Override
	public Object doAction() {

		EventDao eventDAO = new EventDaoImpl();
		EventMonitor event = new EventMonitor();
		User user = this.getUser().getBaseUser();// 当前用户信息
		int method = Integer.parseInt(this.get("method"));

		if (method == 1) {
			String type = this.get("type");
			int code = Integer.parseInt(this.get("code"));
			String topicname = this.get("topicname");
			String date_y = this.get("date_y");
			String date_n = this.get("date_n");
			String jk_dateStart = this.get("jk_dateStart") + " 00:00:00";
			String jk_dateEnd = this.get("jk_dateEnd") + " 23:59:59";

			event.setName(topicname);
			event.setKeywords(date_y);
			event.setNotkeywords(date_n);
			event.setLocation(code); // 地点
			try {
				event.setStartTime(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss")
						.parse(jk_dateStart));
				event.setEndTime(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss")
						.parse(jk_dateEnd));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			event.setCreator(user.getId()); // 当前用户ID
			event.setCustomID(user.getCustomID()); // 用户所属的企业id

			if (type.equals("0")) {
				event.setPlatform(Platform.Sina); // 平台类型：1新浪2腾讯5人民
			} else if (type.equals("1")) {
				event.setPlatform(Platform.Tencent);
			} else {
				event.setPlatform(Platform.Renmin);
			}

			eventDAO.insert(event); // 添加事件

			// add by wujian for wordcloud
			event = eventDAO.queryEventByEId(event.getMonitorId());
			eventDAO.addWordCloudTask(event.getMonitorId(), event.getPoolId(),
					event.getTableName(), WordCloudEnum.Event);
			return event;
		} else if (method == 2) {
			PermissionController controll = new PermissionController(this
					.getUser());
			int maxCount = controll.GetCustomConfigValue(this.getUser(),
					PortalCST.weibo_event_count);
			int eventcount = eventDAO.getPeriodEventCount(user.getCustomID());
			if (eventcount >= maxCount) {
				return -1;
			}
		}
		return "true";
	}
}
