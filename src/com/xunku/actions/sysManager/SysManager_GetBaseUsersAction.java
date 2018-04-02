package com.xunku.actions.sysManager;

import com.xunku.actions.ActionBase;
import com.xunku.dao.base.UserDao;
import com.xunku.daoImpl.base.UserDaoImpl;
import com.xunku.dto.PagerDTO;
import com.xunku.dto.sysManager.SysManager_BaseUserDTO;
import com.xunku.pojo.base.Pager;
import com.xunku.pojo.base.User;
import com.xunku.utils.Pagefile;
/*
 * SysManager_GetBaseUsersAction 系统管理获取用户列表
 * @author: hjian
 */
public class SysManager_GetBaseUsersAction extends ActionBase {

	@Override
	public Object doAction() {

		UserDao dao = new UserDaoImpl();
		PagerDTO pagerdto = new PagerDTO();
		pagerdto.pageIndex = Integer.parseInt(this.get("pageIndex"));
		pagerdto.pageSize = Integer.parseInt(this.get("pageSize"));
		User user = this.getUser().getBaseUser();
		
		Pagefile<User> list = dao.queryByAll(pagerdto, user.getId());
		Pagefile<SysManager_BaseUserDTO> mtfile = new Pagefile<SysManager_BaseUserDTO>();

		for(User item : list.getRows()){
			SysManager_BaseUserDTO sbu = new SysManager_BaseUserDTO();
			sbu.setCustomid(item.getCustomID());
			sbu.setId(item.getId());
			if(item.isAdmin())sbu.setIsadminstr("是");else sbu.setIsadminstr("否");
			if(item.getEmail()==null){
				item.setEmail("");
			}
			sbu.setMail(item.getEmail());
			if(item.getNickName()==null){
				item.setNickName("");
			}
			sbu.setNickname(item.getNickName());
			if(item.getRole() != null){
				sbu.setRoleid(item.getRole().getId());
				sbu.setRolename(item.getRole().getName());
			}
			
			if(item.getTel()==null){
				item.setTel("");
			}
			sbu.setTele(item.getTel());
			sbu.setToken(item.getToken());
			sbu.setUsername(item.getUserName());
			
			if(item.getCheckid() == 0){
				sbu.setCheck("无");
				sbu.setIscheck("否");
			}else{
				User _user = dao.queryByid(item.getCheckid());
				sbu.setIscheck("是");
				sbu.setCheck(_user.getNickName());
			}
			mtfile.getRows().add(sbu);
		}
		
		mtfile.setRealcount(list.getRealcount());
		mtfile.setErr(list.getErr());
		return mtfile;
	}

}
