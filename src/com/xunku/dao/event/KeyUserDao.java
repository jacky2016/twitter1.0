package com.xunku.dao.event;

import com.xunku.pojo.event.KeyUser;

public interface KeyUserDao {
    /**
     * 功能描述<添加关键用户【用户分析】>
     * @author wanghui
     * @param  void	
     * @return void
     * @version twitter 1.0
     * @date Jun 13, 20142:41:22 PM
     */
    public void insertKeyUser(KeyUser user);
    /**
     * 功能描述<按eventId删除>
     * @author wanghui
     * @param  void	
     * @return boolean
     * @version twitter 1.0
     * @date Jun 13, 20143:58:26 PM
     */
    public boolean deleteByEvent(int eventId);
}
