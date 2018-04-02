package com.xunku.dao.my;

import java.util.Date;
import java.util.List;

import com.xunku.app.model.Pooling;
import com.xunku.pojo.my.Publish;

public interface PublishDao {
    /**
     * 功能描述<添加我的微博发布统计>
     * @author wanghui
     * @param  void	
     * @return boolean
     * @version twitter 1.0
     * @date Jun 23, 20144:11:09 PM
     */
    public boolean insert(Publish p);
    /**
     * 功能描述<删除我的微博发布统计>
     * @author wanghui
     * @param  void	
     * @return boolean
     * @version twitter 1.0
     * @date Jun 23, 20144:12:05 PM
     */
    public boolean deleteById(int pid);
    /**
     * 功能描述<更新我的微博发布统计>
     * @author wanghui
     * @param  void	
     * @return boolean
     * @version twitter 1.0
     * @date Jun 23, 20144:12:32 PM
     */
    public boolean update(Publish p);
    /**
     * 功能描述<用户微博的发布统计【发布统计】>
     * @author wanghui
     * @param  void	
     * @return List<Publish>
     * @version twitter 1.0
     * @date Jun 23, 20144:12:55 PM
     */
    public List<Publish> queryPubLishList(Date startTime,Date endTime,String uid,Pooling pool);
}
