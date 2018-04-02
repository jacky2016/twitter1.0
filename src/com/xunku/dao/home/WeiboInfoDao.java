package com.xunku.dao.home;

import java.util.List;

import com.xunku.pojo.home.WeiboInfo;

public interface WeiboInfoDao {
    /**
     * 功能描述<根据用户查询首页微博信息>
     * @author wanghui
     * @param  void	
     * @return List<WeiboInfo>
     * @version twitter 1.0
     * @date Apr 17, 20146:01:34 PM
     */
    public List<WeiboInfo> queryByUserID(int userId);
    
    /**
     * 功能描述<根据账号获取首页微博信息>
     * @author wanghui
     * @param  void	
     * @return WeiboInfo
     * @version twitter 1.0
     * @date Apr 18, 20149:43:49 AM
     */
    public WeiboInfo queryByAID(int aid);
    /**
     * 功能描述<首页是否关闭【首页展示】>
     * @author wanghui
     * @param  void	
     * @return void
     * @version twitter 1.0
     * @date May 21, 20143:28:05 PM
     */
    public void updateClose(int id,boolean isClose);
    /**
     * 功能描述<首页是否展开【首页展示】>
     * @author wanghui
     * @param  void	
     * @return void
     * @version twitter 1.0
     * @date May 21, 20143:28:57 PM
     */
    public void updateExpand(int id,boolean isExpand);
}
