package com.xunku.actions.home;

import java.util.Map;

import com.xunku.actions.ActionBase;
import com.xunku.dao.base.AccountDao;
import com.xunku.daoImpl.base.AccountDaoImpl;
import com.xunku.dto.IDTO;
import com.xunku.dto.home.WeiboInfosDataDTO;
import com.xunku.pojo.base.Account;
public class Home_WeiboInfosDataByApiAction extends ActionBase {

	AccountDao weiboAccountDao = new AccountDaoImpl();

	public Home_WeiboInfosDataByApiAction() {
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public WeiboInfosDataDTO doAction() {
		// TODO Auto-generated method stub
		Map<String, String> params = this.getParameters();
		String account = this.get("accountid");// params.get("accountid");
		//应该从API读取数据
		//transpond:转发,comment:评论,like:粉丝,attention:关注
		WeiboInfosDataDTO entity = new WeiboInfosDataDTO();
		entity.setStatusCount(111);
		entity.setTranspond(222);
		entity.setComment(333);
		entity.setLike(444);
		entity.setAttention(555);
		
		//假数据，需要修改
		int userid = this.getUser().getBaseUser().getId();
		
		int accountID =0;
		if(!account.equals(""))
	    	accountID = Integer.parseInt(account);
		//Account info = new Account();
		//info.setId(accountID);
		//info.setWeibos(entity.getStatusCount());
		//info.setRepostCount(entity.getTranspond());
		//info.setCommentCount(entity.getComment());
		//info.setFollowCount(entity.getLike());
		//info.setFriendCount(entity.getAttention());
		//weiboAccountDao.updateByCID(info);//根据微博账号更新
		return entity;
	}
	
}
