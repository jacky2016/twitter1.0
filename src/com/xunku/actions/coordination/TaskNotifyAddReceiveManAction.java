package com.xunku.actions.coordination;

import java.util.ArrayList;
import java.util.List;

import com.xunku.actions.ActionBase;
import com.xunku.dao.base.UserDao;
import com.xunku.daoImpl.base.UserDaoImpl;
import com.xunku.dto.coordination.TaskNotifyDTO;
import com.xunku.dto.pushservices.PushUserDTO;

/**
 *协同办公--任务通知action类
 * @author shangwei
 *  此类是任务通知上添加按钮出来弹出层上面添加接受人按钮action
 */

public class TaskNotifyAddReceiveManAction   extends  ActionBase {
			
	UserDao  userDao =new UserDaoImpl();
	
	@Override
	public Object doAction() {
	
		/**Author  ShangWei
		 * * Description  添加弹出层中根据当前用户拥有的权限而显示的接受人列表
		 */
			int userid=this.getUser().getBaseUser().getId();
			List<TaskNotifyDTO>  ll=new ArrayList<TaskNotifyDTO>();
			List<PushUserDTO>   pushusers= userDao.queryUserByUID(userid);
			if(pushusers!=null){
				for(PushUserDTO  push :pushusers ){
					TaskNotifyDTO  t=new TaskNotifyDTO();
					  t.id=push.getId();
					  t.sendName=push.getUseName();
					  t.totifyContent=push.getEmail();
					  ll.add(t);
				}
				
			}
			return ll;
		
	}
 
}
