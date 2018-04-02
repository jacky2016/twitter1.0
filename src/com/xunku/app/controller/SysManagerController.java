package com.xunku.app.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.httpclient.Cookie;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.cookie.CookieSpec;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.impl.cookie.BasicClientCookie2;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.ExecutionContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

import com.xunku.app.Utility;
import com.xunku.app.enums.Platform;
import com.xunku.app.interfaces.IAccount;
import com.xunku.app.model.ApiToken;
import com.xunku.app.model.App;
import com.xunku.app.model.UserInfo;
import com.xunku.cache.Cache;
import com.xunku.cache.CacheManager;
import com.xunku.constant.PortalCST;
import com.xunku.dao.base.CustomRoleDao;
import com.xunku.daoImpl.base.AccountAuthsDaoImpl;
import com.xunku.daoImpl.base.AccountDaoImpl;
import com.xunku.daoImpl.base.CustomRoleDaoImpl;
import com.xunku.daoImpl.base.UserDaoImpl;
import com.xunku.dto.sysManager.CustomPermissionInRoleDTO;
import com.xunku.dto.sysManager.GroupDTO;
import com.xunku.dto.sysManager.ModuleCode;
import com.xunku.dto.sysManager.ModuleGroupDTO;
import com.xunku.dto.sysManager.PeopleDTO;
import com.xunku.dto.sysManager.UIElementCode;
import com.xunku.pojo.base.AccountAuths;
import com.xunku.pojo.base.AccountInfo;
import com.xunku.pojo.base.PermissionInRole;
import com.xunku.pojo.base.User;
import com.xunku.portal.controller.PermissionController;
import com.xunku.utils.AppServerProxy;

public class SysManagerController {
	
	
	//生成随机生成6位随机码
	public static  String GetRandCode(){
		
		 String[] beforeShuffle = new String[] { "0", "1","2", "3", "4", "5", "6", "7",  
	                "8", "9", "a", "b", "c", "d", "e", "f", "g", "h", "i", "j",  
	                "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v",  
	                "w", "x", "y", "z" };  
		 List list = Arrays.asList(beforeShuffle);  
	     Collections.shuffle(list);  
	     StringBuilder sb = new StringBuilder();  
	     for (int i = 0; i < list.size(); i++) {  
	         sb.append(list.get(i));  
	     }  
	     String afterShuffle = sb.toString();  
	     String result = afterShuffle.substring(3, 9);  
	     return result;  
		 
	}
	
	
	/**
	 * 加载权限菜单
	 * return shaoqun
	 * @param user
	 * @return
	 */
	public static List<ModuleGroupDTO> GetAdminAuth(UserInfo user){
		PermissionController controller = new PermissionController(user);
		List<ModuleGroupDTO> modelist = new ArrayList<ModuleGroupDTO>();
		Cache cache = CacheManager.getCacheInfo(PortalCST.PermissionListCacheKey);
		List<ModuleCode> mclist = (List<ModuleCode>) cache.getValue();

		for(ModuleCode item : mclist){
			boolean flag = controller.HasMenuAuthority(user, item.getMcode());
			if(!flag) continue;
			//加载一级菜单
			ModuleGroupDTO model = new ModuleGroupDTO();
			model.setId(item.getPid());
			model.setCode(item.getMcode());
			model.setName(item.getDesc());

			//加载二级菜单 
			List<ModuleCode> mlist = item.getModuleList();
			List<GroupDTO> grouplist = new ArrayList<GroupDTO>();
			
			if(item.getModuleList().size() == 0){//判断二级是否有权限
				List<UIElementCode> uelist2 = new ArrayList<UIElementCode>();
				GroupDTO grp = new GroupDTO();
				for(UIElementCode uitem : item.getUicode()){
					boolean flag2 = controller.HasUIAuthority(user, uitem.getEcode());
					if(!flag2) continue;

					UIElementCode pp = new UIElementCode();
					pp.setModelCode(uitem.getModelCode());
					pp.setDesc(uitem.getDesc());
					pp.setEcode(uitem.getEcode());
					uelist2.add(pp);
				}
				grp.setId(0);
				grp.setName("");
				grp.setCode("");
				grp.setUicodelist(uelist2);
				grouplist.add(grp);	
			}else{
				for(ModuleCode mitem : mlist){
					boolean flag1 = controller.HasMenuAuthority(user, mitem.getMcode());
					if(!flag1) continue;
					GroupDTO sp = new GroupDTO();
					sp.setId(mitem.getPid());
					sp.setName(mitem.getDesc());
					sp.setCode(mitem.getMcode());			
					List<UIElementCode> uelist = new ArrayList<UIElementCode>();
					
					//加载ui列表
					for(UIElementCode uitem : mitem.getUicode()){
						boolean flag2 = controller.HasUIAuthority(user, uitem.getEcode());
						if(!flag2) continue;
	
						UIElementCode p = new UIElementCode();
						p.setModelCode(uitem.getModelCode());
						p.setDesc(uitem.getDesc());
						p.setEcode(uitem.getEcode());
						uelist.add(p);
					}
					sp.setUicodelist(uelist);
					grouplist.add(sp);	
				}
			}
			model.setGrouplist(grouplist);
			modelist.add(model);
		}
		return modelist;
	}
	
	
	/**
	 * 封装加载权限菜单
	 * return shaoqun
	 * @param user
	 * @return
	 */
	public static List<ModuleGroupDTO> Convert(List<CustomPermissionInRoleDTO> list,UserInfo user){

		//转换list,封装成ModuleCode
		PermissionController controller = new PermissionController(user);
		List<ModuleGroupDTO> modelist = new ArrayList<ModuleGroupDTO>();
		Cache cache = CacheManager.getCacheInfo(PortalCST.PermissionListCacheKey);
		List<ModuleCode> mclist = (List<ModuleCode>) cache.getValue();

		for(ModuleCode item : mclist){

			ModuleGroupDTO model = new ModuleGroupDTO();
			for(CustomPermissionInRoleDTO per : list){
				//加载一级菜单
				if(item.getMcode().equals(per.getMcode())){
					model.setId(item.getPid());
					model.setCode(item.getMcode());
					model.setName(item.getDesc());
					break;
				}
			}

			//加载二级菜单 
			List<ModuleCode> mlist = item.getModuleList();
			List<GroupDTO> grouplist = new ArrayList<GroupDTO>();
			
			if(item.getModuleList().size() == 0){//判断二级是否有权限
				List<UIElementCode> uelist2 = new ArrayList<UIElementCode>();
				GroupDTO grp = new GroupDTO();
				for(UIElementCode uitem : item.getUicode()){

					UIElementCode pp = new UIElementCode();
					for(CustomPermissionInRoleDTO per2 : list){
						if(uitem.getEcode().equals(per2.getUicode())){
							pp.setModelCode(uitem.getModelCode());
							pp.setDesc(uitem.getDesc());
							pp.setEcode(uitem.getEcode());
							uelist2.add(pp);
							break;
						}
					}
					
				}
				grp.setId(0);
				grp.setName("");
				grp.setCode("");
				grp.setUicodelist(uelist2);
				grouplist.add(grp);	
			}else{
				for(ModuleCode mitem : mlist){

					GroupDTO sp = new GroupDTO();
					for(CustomPermissionInRoleDTO per3 : list){
						if(mitem.getMcode().equals(per3.getCode())){
							sp.setId(mitem.getPid());
							sp.setName(mitem.getDesc());
							sp.setCode(mitem.getMcode());		
							break;
						}
					}
					List<UIElementCode> uelist = new ArrayList<UIElementCode>();
					
					//加载ui列表
					for(UIElementCode uitemc : mitem.getUicode()){

						UIElementCode p = new UIElementCode();
						for(CustomPermissionInRoleDTO per4 : list){
							if(uitemc.getEcode().equals(per4.getUicode())){
								p.setModelCode(uitemc.getModelCode());
								p.setDesc(uitemc.getDesc());
								p.setEcode(uitemc.getEcode());
								uelist.add(p);
								break;
							}
						}
						
					}
					if(sp.getId() != 0){
						sp.setUicodelist(uelist);
						grouplist.add(sp);	
					}
				}
			}
			model.setGrouplist(grouplist);
			modelist.add(model);
		}
		return modelist;
	}
	
	
	/**
	 * 帐号绑定授权，根据返回值来执行不同的操作
	 * return shaoqun
	 * @param user
	 * @return
	 */
	public static void getAuthor(String code,App app,Platform platform,String userid,String uid,HttpServletResponse response) throws IOException{
		if (code != null) {
			ApiToken apitoken = AppServerProxy
					.getTokenByCode(code, app);

			if (apitoken == null) {
				response.sendRedirect("back-error.html");
			}
			boolean boud = new AccountDaoImpl().checkAccountBind(
					apitoken.getUid(), platform);
			if (apitoken != null) {
				if (SysManagerController.saveAuth(apitoken, Integer.parseInt(userid),
						app, uid) == 1) {
					response.sendRedirect("back-back.html");
				} else if (SysManagerController.saveAuth(apitoken, Integer
						.parseInt(userid), app, uid) == 2) {
					response.sendRedirect("back-warn.html");
				} else if (SysManagerController.saveAuth(apitoken, Integer
						.parseInt(userid), app, uid) == 3) {
					response.sendRedirect("back-repeat.html");
				} else {
					response.sendRedirect("back-error.html");
				}
			} else {
				response.sendRedirect("back-error.html");
			}

		} else {
			response.sendRedirect("back-cancel.html");
		}
		
	}

	/**
	 * 帐号绑定授权
	 * return shaoqun
	 * @param user
	 * @return
	 */
	public static int saveAuth(ApiToken apiToken, int userid, App app, String uid) {

		User user = new UserDaoImpl().queryByUid(userid);
		int customId = user.getCustomID();
		AccountInfo ac = new AccountInfo();
		ac.setCustomId(customId);
		// 获得账户名称
		IAccount acc = AppServerProxy.getAccountByToken(apiToken.getToken(),
				apiToken.getUid(), app.getPlatform());
		// 同步更新帐号库，否则首页无法使用
		AppServerProxy.updateAccount(acc);
		AccountAuths auth = new AccountAuths();
		int str = 0, s;
		if (acc != null) {
			ac.setCreator(userid);
			ac.setCustomId(customId);
			ac.setUcode(acc.getUcode());
			ac.setUid(apiToken.getUid());
			ac.setPlatform(app.getPlatform());
			ac.setName(acc.getName());
			ac.setVerfiy(acc.isVerified());
			auth.setAccountId(ac.getId());
			auth.setAppId(app.getId());
			auth.setAuthTime(new Date());// DateHelper.formatDBTime(new
			// Date())
			auth.setCustomId(customId);
			auth.setExpiresin(apiToken.getExpiresIn());
			auth.setPlatform(Utility.getPlatform(app.getPlatform()));
			auth.setToken(apiToken.getToken());
			auth.setUcode(acc.getUcode());
			auth.setUid(acc.getUcode());
			str = 1;
		}

		if (str != 0) {
			if (uid.equals("add")) {// 添加
				if (new AccountDaoImpl().checkAccountBind(apiToken.getUid(),
						app.getPlatform()) == false) {
					new AccountDaoImpl().insert(ac, auth, userid);
					AppServerProxy.updateToken(user, ac, auth);
					s = 1;
				} else {
					s = 3;
				}
			} else if (uid.equals(apiToken.getUid())) {// 延长
				new AccountDaoImpl().insert(ac, auth, userid);// 更新本地数据库信息
				AppServerProxy.updateToken(user, ac, auth);
				s = 1;
			} else {
				s = 2;
			}
		} else {
			s = 4;
		}
		return s;
	}


}
