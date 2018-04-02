package com.xunku.actions.sysManager;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.xunku.actions.ActionBase;
import com.xunku.app.controller.SysManagerController;
import com.xunku.dao.base.UserDao;
import com.xunku.daoImpl.base.UserDaoImpl;
import com.xunku.pojo.base.User;
import com.xunku.utils.URLUtils;

/***
 * 发送邮件--进行密码重置
 *
 * @author shaoqun
 * @created Aug 7, 2014
 */
public class SysManager_ResetBaseUserAction extends ActionBase {

	@Override
	public Object doAction() {
	
		User _user = this.getUser().getBaseUser();
		int id = Integer.parseInt(this.get("id"));
		String _username = this.get("name");
		String _emailUrl = this.get("email");
		String _pass = SysManagerController.GetRandCode();
		String _content = "尊敬的用户"+_username+"，您好：<br/>您在讯库-云微方中的登录密码已经被重置，新密码为："+_pass+"，请牢记。<br/>任何问题欢迎您致电或邮件联系客服。";
		String _title = "重置密码提示";
		String message = "";
		try {
			message = URLUtils.sendNet(_user.getCustomID(), _user.getId(), _emailUrl,_title, _content, 0);
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println(message);
		boolean flag = false;String warn = "";
		if(message.equals("true")){
			//得到随机密码 并重置
			UserDao dao = new UserDaoImpl();
			flag = dao.resetPWD(id, _pass);
			if(flag == true){
				warn = "-1";
			}else{
				warn = "密码重置失败，请重新重置!";
			}
		}else{
			warn = "密码重置失败，请重新重置!";
		}
		
		return warn;
	}
	
	
	

}
