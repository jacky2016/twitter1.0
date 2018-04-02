package com.xunku.dao.event;

import java.util.List;

import com.xunku.pojo.base.Post;
import com.xunku.pojo.event.KeyPoint;

public interface KeyPointTopDao {
    /**
     * 功能描述<获得当前事件里面转发数最多的几个微博【内容分析】>
     * @author wanghui
     * @param  void
     * @return List<Post>
     * @version twitter 1.0
     * @date Jun 11, 20141:57:25 PM
     */
    public List<Post> queryKeyPointTop(int eventId);
    /**
     * 功能描述<添加关键点【用户分析】>
     * @author wanghui
     * @param  void	
     * @return void
     * @version twitter 1.0
     * @date Jun 13, 20142:39:38 PM
     */
    public void insertKeyPoint(KeyPoint point);
    /**
     * 功能描述<按eventId删除>
     * @author wanghui
     * @param  void	
     * @return boolean
     * @version twitter 1.0
     * @date Jun 13, 20143:56:06 PM
     */
    public boolean deleteByEvent(int eventId);
}
