package com.xunku.actions.sysManager;

import java.util.ArrayList;
import java.util.List;

import com.xunku.actions.ActionBase;
import com.xunku.dao.base.ApprovedDao;
import com.xunku.daoImpl.base.ApprovedDaoImpl;
import com.xunku.dto.sysManager.ApprovedDTO;
import com.xunku.dto.sysManager.SysManager_ApproveDTO;
import com.xunku.pojo.base.Pager;
import com.xunku.pojo.base.User;
import com.xunku.utils.Pagefile;
import com.xunku.dto.PagerDTO;

/*
 * SysManager_GetApproveListAction 系统管理获取审核信息列表
 * @author: hjian
 */
public class SysManager_GetApproveListAction extends ActionBase {

	@Override
	public Object doAction() {
		
		ApprovedDao dao = new ApprovedDaoImpl();

		Pager pager = new Pager();
		pager.setPageIndex(Integer.parseInt(this.get("pageIndex")));
		pager.setPageSize(Integer.parseInt(this.get("pageSize")));
		User user = this.getUser().getBaseUser();

		Pagefile<ApprovedDTO> list = dao.queryByUserId(pager, user.getId());
		Pagefile<SysManager_ApproveDTO> mtfile = new Pagefile<SysManager_ApproveDTO>();

		String option = "";
		for(ApprovedDTO item : list.getRows()){
			SysManager_ApproveDTO ditem = new SysManager_ApproveDTO();
			ditem.setAppid(item.getAppid());
			ditem.setUserNick(item.getUserNick());
			ditem.setUserName(item.getUserName());
			if(item.getCheck() == null){
				ditem.setCheck("无");
			}else{
				ditem.setCheck(item.getCheck());
			}
			if(item.isCheck() == true){
				ditem.setIsCheck("是");
			}else{
				ditem.setIsCheck("否");
			}
			mtfile.getRows().add(ditem);
		}

		mtfile.setRealcount(list.getRows().size());
		mtfile.setErr(list.getErr());

		return mtfile;
	}

}
