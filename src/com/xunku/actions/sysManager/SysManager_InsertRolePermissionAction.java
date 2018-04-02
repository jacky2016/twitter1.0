package com.xunku.actions.sysManager;

import java.util.ArrayList;
import java.util.List;

import weibo4j.org.json.JSONArray;
import weibo4j.org.json.JSONException;
import weibo4j.org.json.JSONObject;

import com.xunku.actions.ActionBase;
import com.xunku.dao.base.CustomRoleDao;
import com.xunku.daoImpl.base.CustomRoleDaoImpl;
import com.xunku.dto.sysManager.RolePermission;
import com.xunku.pojo.base.PermissionInRole;
/*
 * SysManager_InsertRolePermissionAction 系统管理插入角色权限
 * @author: hjian
 */
public class SysManager_InsertRolePermissionAction extends ActionBase {

	@Override
	@SuppressWarnings("unused")
	public Object doAction() {
		String result_menu = this.get("result_menu");
		int rid = Integer.parseInt(this.get("rid"));
		
		CustomRoleDao dao = new CustomRoleDaoImpl();
		RolePermission role = new RolePermission();
		List<PermissionInRole> plist = new ArrayList<PermissionInRole>();
		

		JSONArray menu_json = null,mode_json=null,ui_json=null,ui_json2=null;
		try {
			menu_json = new JSONArray(result_menu);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		int menu_id = 0,mode_id = 0;
		String menu_value = "",mode_value="",ui_value="",ui_code="",ui_value2="",ui_code2="",_mode="",_ui="",_ui2="";
		JSONObject menu_obj =null,mode_obj=null,ui_obj=null,ui_obj2=null;
		for(int i=0;i<menu_json.length();i++){//取得一级菜单
			
			try {
				menu_obj = menu_json.getJSONObject(i);
				menu_id = Integer.parseInt(menu_obj.getString("id"));
				menu_value = menu_obj.getString("value");
				_mode = menu_obj.getString("mode");

				if(!_mode.equals("[]")){
					mode_json = new JSONArray(_mode);
					
					for(int j=0;j<mode_json.length();j++){
						mode_obj = mode_json.getJSONObject(j);
						mode_id = Integer.parseInt(mode_obj.getString("id"));
						mode_value = mode_obj.getString("value");
						_ui = mode_obj.getString("ui");
						
						
						if(!_ui.equals("[]")){
							ui_json = new JSONArray(_ui);
							for(int z=0;z<ui_json.length();z++){
								ui_obj = ui_json.getJSONObject(z);
								ui_code = ui_obj.getString("id");
								ui_value = ui_obj.getString("value");
								
								PermissionInRole per = new PermissionInRole();
								per.setMcode(menu_value);
								per.setCode(mode_value);
								per.setUicode(ui_value);
								
								plist.add(per);
							}
						}else{
							PermissionInRole per = new PermissionInRole();
							per.setMcode(menu_value);
							per.setCode(mode_value);
							per.setUicode(ui_value);
							per.setUicode("");
							plist.add(per);
						}
					}
				}else{
					
					_ui2 = menu_obj.getString("mode2");
					if(!_ui2.equals("[]")){
						ui_json2 = new JSONArray(_ui2);
						for(int w=0;w<ui_json2.length();w++){
							ui_obj2 = ui_json2.getJSONObject(w);
							ui_code2 = ui_obj2.getString("id");
							ui_value2 = ui_obj2.getString("value");
							
							PermissionInRole per = new PermissionInRole();
							per.setMcode(menu_value);
							per.setCode("");
							per.setUicode(ui_value2);
							plist.add(per);
						}
					}else{
						PermissionInRole per = new PermissionInRole();
						per.setMcode(menu_value);
						per.setCode("");
						per.setUicode("");
						plist.add(per);
					}
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			
		}
		
		role.setRoleId(rid);
		role.setPermissRole(plist);	
		dao.insertPermission(role);
		return "true";
	}

	private JSONObject JSONObject(String _mode) {
		return null;
	}

}
