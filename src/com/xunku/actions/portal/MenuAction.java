package com.xunku.actions.portal;

import java.util.ArrayList;
import java.util.List;

import com.xunku.actions.ActionBase;
import com.xunku.app.model.UserInfo;
import com.xunku.cache.Cache;
import com.xunku.cache.CacheManager;
import com.xunku.constant.PortalCST;
import com.xunku.dao.base.ModuleDao;
import com.xunku.daoImpl.base.ModuleDaoImpl;
import com.xunku.pojo.base.BaseModule;
import com.xunku.portal.controller.PermissionController;

/*
 * portal模块，获取菜单信息
 * @author sunao
 */
public class MenuAction extends ActionBase {

	ModuleDao moduleDao = new ModuleDaoImpl();

	@Override
	public List<BaseModule> doAction() {
		// TODO Auto-generated method stub
		Cache cache = CacheManager.getCacheInfo(PortalCST.SystemModuleCacheKey);
		List<BaseModule> lst = (List<BaseModule>) cache.getValue();
		List<BaseModule> newLst = new ArrayList<BaseModule>();
		UserInfo user = this.getUser();
		// 验证菜单权限
		PermissionController permissionController = new PermissionController(
				user);
		for (BaseModule module : lst) {
			String code = module.getCode();
			if (permissionController.HasMenuAuthority(user, code)) {

				BaseModule newModule = new BaseModule();
				newModule.setCaption(module.getCaption());
				newModule.setCode(module.getCode());
				newModule.setDescription(module.getDescription());
				newModule.setId(module.getId());
				newModule.setIndes(module.getIndes());
				newModule.setModule(new ArrayList<BaseModule>());
				newModule.setParent(module.getParent());
				newModule.setTemplate(module.getTemplate());

				List<BaseModule> subMenus = module.getModule();
				for (BaseModule subMenu : subMenus) {
					String subCode = subMenu.getCode();
					if (permissionController.HasMenuAuthority(user, subCode)) {
						newModule.getModule().add(subMenu);
					}
				}

				newLst.add(newModule);
			}
		}
		return newLst;
	}
}
