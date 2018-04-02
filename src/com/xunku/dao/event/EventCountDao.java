package com.xunku.dao.event;

import com.xunku.pojo.event.EventCount;

public interface EventCountDao {
    /**
     * 功能描述<添加性别分布【用户分析】>
     * @author wanghui
     * @param  void	
     * @return void
     * @version twitter 1.0
     * @date Jun 13, 20143:06:30 PM
     */
    public void insert(EventCount count);
    /**
     * 功能描述<按eventId删除>
     * @author wanghui
     * @param  void	
     * @return boolean
     * @version twitter 1.0
     * @date Jun 13, 20143:48:28 PM
     */
    public boolean deleteByEvent(int eventId);
}
