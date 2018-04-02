package com.xunku.actions.portal;

import com.xunku.actions.ActionBase;
import com.xunku.dao.base.CustomDao;
import com.xunku.dao.base.UserDao;
import com.xunku.daoImpl.base.CustomDaoImpl;
import com.xunku.daoImpl.base.UserDaoImpl;
import com.xunku.dto.ErrorDTO;
import com.xunku.dto.portal.UserInfoDTO;
import com.xunku.pojo.base.User;

/*
 * portal模块，获取用户信息
 * @author sunao
 */
public class UserInfoAction extends ActionBase {
	UserDao userDao = new UserDaoImpl();
	CustomDao customDao = new CustomDaoImpl();

	@Override
	public Object doAction() {
		// TODO Auto-generated method stub

		int userid = this.getUser().getBaseUser().getId();
		User user = userDao.queryByUid(userid);

		if (user == null) {
			ErrorDTO error = new ErrorDTO();
			error.code = 1;
			error.err = "用户被删除，自动退出登录";
			return error;
		}
		UserInfoDTO entity = new UserInfoDTO();
		entity.userName = user.getNickName();
		entity.phone = user.getTel();
		entity.email = user.getEmail();
		entity.logoPath = customDao.queryByUid(userid);
		entity.isAdmin = user.isAdmin();

		return entity;
	}

}
