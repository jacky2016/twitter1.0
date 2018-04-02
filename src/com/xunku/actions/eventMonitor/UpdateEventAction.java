package com.xunku.actions.eventMonitor;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.xunku.actions.ActionBase;
import com.xunku.app.Utility;
import com.xunku.app.enums.EventMonitorEnum;
import com.xunku.app.enums.Platform;
import com.xunku.app.helpers.DateHelper;
import com.xunku.app.monitor.EventMonitor;
import com.xunku.cache.Cache;
import com.xunku.cache.CacheManager;
import com.xunku.constant.PortalCST;
import com.xunku.dao.event.EventDao;
import com.xunku.daoImpl.event.EventDaoImpl;
import com.xunku.dto.eventMonitor.EventDTO;
import com.xunku.dto.eventMonitor.EventListDTO;
import com.xunku.pojo.base.City;
import com.xunku.pojo.base.User;
import com.xunku.portal.controller.PermissionController;
import com.xunku.utils.Pagefile;

/**
 * 事件监控--修改事件列表信息
 * @author shaoqun
 * 
 */
public class UpdateEventAction extends ActionBase {

	@Override
	public Object doAction() {
		
		EventDao eventDAO = new EventDaoImpl(); 
		EventMonitor event = new EventMonitor();
		Pagefile<EventListDTO> mtfile = new Pagefile<EventListDTO>();
		User user = this.getUser().getBaseUser();

		int method = Integer.parseInt(this.get("method"));
		
		if(method == 1){		//获取要修改的数据
			
			int eid = Integer.parseInt(this.get("data_id"));
			event = eventDAO.queryEventByEId(eid);
			EventDTO edto = new EventDTO();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			int code = event.getLocation();

			edto.setId(event.getMonitorId());
			edto.setName(event.getName());
			edto.setKeywords(event.getKeywords());
			edto.setNotkeywords(event.getNotkeywords());
			edto.setLocation(event.getLocation());
			edto.setStartTime(sdf.format(event.getStartTime()));
			edto.setEndTime(sdf.format(event.getEndTime()));
			edto.setCreator(event.getCreator());
			edto.setCustomID(event.getCustomID());
			edto.setPlatform(Utility.getPlatform(event.getPlatform()));
			return edto;
			
		}else if(method == 2){		//修改数据

			String topicname = this.get("topicname");
			String date_y = this.get("date_y");
			String date_n = this.get("date_n");
			String jk_dateStart = this.get("jk_dateStart")+" 00:00:00";
			String jk_dateEnd = this.get("jk_dateEnd")+" 23:59:59";
			int eid = Integer.parseInt(this.get("data_id"));
			int code = Integer.parseInt(this.get("code"));
			
			event.setId(eid);
			event.setName(topicname);
			event.setKeywords(date_y);
			event.setNotkeywords(date_n);
			event.setLocation(code);   				//地点
			// modify by wujian for event startdate
			try {
				event.setStartTime(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(jk_dateStart));
				event.setEndTime(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(jk_dateEnd));
			} catch (ParseException e) {
				e.printStackTrace();
			}
			event.setCreator(user.getId());     			//当前用户ID
			event.setCustomID(user.getCustomID());  		//用户所属的企业id

			PermissionController controll = new PermissionController(this.getUser());
			int maxCount = controll.GetCustomConfigValue(this.getUser(),
					PortalCST.weibo_event_count);
			int eventcount = eventDAO.getPeriodEventCount(user.getCustomID());
			int bycount =  eventDAO.getPeriodEventCountById(user.getCustomID(),eid);//修改是否在有效期内

			if(bycount > 0){
				eventDAO.update(event);				//修改事件
				return event;
			}else{
				if(eventcount >= maxCount){
					return -1;
				}else{
					eventDAO.update(event);				//修改事件
					return event;
				}
			}
			
		}else if(method == 10){
			PermissionController controll = new PermissionController(this.getUser());
			int maxCount = controll.GetCustomConfigValue(this.getUser(),
					PortalCST.weibo_event_count);
			int eventcount = eventDAO.getPeriodEventCount(user.getCustomID());
			
			if(eventcount >= maxCount){
				return -1;
			}
		}
		return "true";
	}

}
