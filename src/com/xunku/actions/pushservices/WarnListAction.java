package com.xunku.actions.pushservices;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import com.xunku.actions.ActionBase;
import com.xunku.app.controller.WarningController;
import com.xunku.dto.pushservices.WarnServiceListDTO;
import com.xunku.pojo.base.Pager;
import com.xunku.pojo.office.WeiboWarn;
import com.xunku.utils.Pagefile;

/**
 * 预警服务--预警列表的 列表展示action类
 * @author shangwei
 *
 */

public class WarnListAction  extends   ActionBase{

	@Override
	public Object doAction() {
			String  queryStr=this.get("queryConditions");
			int customid=this.getUser().getCustom().getId();
			int userid=this.getUser().getBaseUser().getId();
			
			//预警列表的展示列表的方法
			if(queryStr.equals("warnservicesWeiboShowList")){
				int pageIndex = Integer.parseInt(this.get("pageIndex"));
				int pageSize = Integer.parseInt(this.get("pageSize"));
				Pager  pager=new  Pager();
				pager.setPageIndex(pageIndex);
				pager.setPageSize(pageSize);
				 return  queryWarnWeiboShowList(customid,pager);
		}
			
			// 修改其中某一个的预警设置 
			else if (queryStr.equals("ModifyWarningList")){
				  int id =Integer.parseInt(this.get("alertID"));
				 return      modifyWarningList(id);
			}  
			
			
			
		return null;
	}
	
	
	/**
	 **  预警列表中的修改 其中一条的方法
	 * */
	public   WarnServiceListDTO  modifyWarningList(int id){
            WarningController  controller =new WarningController();
            WeiboWarn  warn=  controller.modifyWarningList(id);
            WarnServiceListDTO  dto=new  WarnServiceListDTO();
             dto.id=warn.getId();
             dto.warnRepost=warn.getRepost();
             dto.warnComment=warn.getComment();
             dto.recevier=warn.getReceiver();
             dto.revetype=warn.getType();
             dto.endTime=warn.getEndTime();
             dto.isDelete=0;
             if(warn.isDelete()){
            	 dto.isDelete=1;
             }
			return dto ;
	}
	
	
	
	
	//预警列表的展示列表的方法
	 public   Pagefile<WarnServiceListDTO>    queryWarnWeiboShowList(int customid,Pager  pager){
		 Pagefile<WarnServiceListDTO> pagefile =new Pagefile<WarnServiceListDTO>();
		         WarningController  controller =new WarningController();
		         Pagefile<WeiboWarn> pf= controller.queryWarnWeiboShowList(customid,pager);
		      	 pagefile.setRealcount(pf.getRealcount());
		      	 List<WeiboWarn>  warns=pf.getRows();
		      	 List<WarnServiceListDTO>  list=new ArrayList<WarnServiceListDTO>();
		      	 for(WeiboWarn  warn : warns){
		      		WarnServiceListDTO  dto=new WarnServiceListDTO();
		      		  dto.id=(warn.getId());
		      		  dto.nickName=(warn.getAuthor());
		      		  dto.text=(warn.getText());
		      		  dto.warnRepost=(warn.getRepost());
		      		  dto.warnComment=(warn.getComment());
		      		  dto.curRepost=(warn.getCurrepost());
		      		  dto.curComment=(warn.getCurcomment());
		      	      dto.endTime=(warn.getEndTime().substring(0,warn.getEndTime().lastIndexOf(" "))); 
		      	       list.add(dto);
		      	 }  //for 
		      	 pagefile.setRows(list);
        		 return pagefile;
	 }
	 

}
