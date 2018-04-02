package com.xunku.actions.eventMonitor;

import com.xunku.actions.ActionBase;
import com.xunku.app.monitor.EventMonitor;
import com.xunku.dao.event.EventDao;
import com.xunku.daoImpl.event.EventDaoImpl;
import com.xunku.dto.eventMonitor.EventDTO;
import com.xunku.dto.eventMonitor.EventListDTO;
import com.xunku.utils.Pagefile;

/**
 * 事件监控--根据id删除事件列表信息
 * @author shaoqun
 * 
 */
public class DeleteEventAction extends ActionBase {

	@Override
	public Object doAction() {
		
		EventDao eventDAO = new EventDaoImpl(); 
		Pagefile<EventListDTO> mtfile = new Pagefile<EventListDTO>();
		
		int id = Integer.parseInt(this.get("id"));
		eventDAO.deleteByEId(id);
		
		return mtfile;
	}

}
