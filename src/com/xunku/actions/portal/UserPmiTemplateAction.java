package com.xunku.actions.portal;

import java.util.Map;

import com.xunku.actions.ActionBase;
import com.xunku.app.model.UserInfo;
import com.xunku.portal.controller.PermissionController;
public class UserPmiTemplateAction extends ActionBase{

	
	@Override
	public Map doAction() {
		UserInfo user =this.getUser();
		PermissionController controller = new PermissionController(user);
		Map map =controller.GetPmiIDHtml(user);
		return map;
	} 
	
}


