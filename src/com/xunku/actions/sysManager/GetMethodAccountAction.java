package com.xunku.actions.sysManager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.xunku.actions.ActionBase;
import com.xunku.app.helpers.DateHelper;
import com.xunku.daoImpl.base.AccountDaoImpl;
import com.xunku.dto.sysManager.AccountVO;
import com.xunku.pojo.base.User;
import com.xunku.utils.DateUtils;
/***
 * 获取帐号
 * @author shaoqun
 *
 */
public class GetMethodAccountAction extends ActionBase {

	@Override
	public Object doAction() {
		// TODO Auto-generated method stub
		
		User _user = this.getUser().getBaseUser();
		List<AccountVO> list = new AccountDaoImpl().queryAccountByUid(_user.getId());
		List<AccountVO> mflist = new ArrayList<AccountVO>();
		for(AccountVO vo :list) {
			
			AccountVO acc = new AccountVO();
			if(vo.getType() == 1){
				int cycle = DateUtils.expireDay(vo.getExpiresin()); // 授权周期
				String stime = DateHelper.formatDBTime(vo.getAuthTime()); // 授权日期
				String etime = DateUtils.addDay(stime, cycle); // 授权结束日期
	
				SimpleDateFormat sdf = new SimpleDateFormat(
						"yyyy-MM-dd HH:mm:ss");
				Date beforDate = null, nowDate = null;
				String nowtime = "";
	
				try {
					nowtime = DateUtils.nowDateFormat("yyyy-MM-dd HH:mm:ss");// 当前日期
					beforDate = sdf.parse(etime);
					nowDate = sdf.parse(nowtime);
				} catch (ParseException e) {
					e.printStackTrace();
				}
	
				if (nowDate.getTime() < beforDate.getTime()) {
					acc.setFlag(false);
				} else {
					acc.setFlag(true);
				}
				
			}else if(vo.getType() == 5){
				acc.setFlag(false);
			}
			acc.setId(vo.getId());
			acc.setName(vo.getName());
			acc.setUid(vo.getUid());
			acc.setType(vo.getType());
			mflist.add(acc);
		} 
		return mflist;
	}

}
