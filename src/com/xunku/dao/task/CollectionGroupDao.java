package com.xunku.dao.task;

import java.util.List;

import com.xunku.pojo.task.CollectionGroup;

public interface CollectionGroupDao {
    /**
     * 功能描述<查询收藏夹的分类【收藏分组】>
     * @author wanghui
     * @param  userId当前系统用户	
     * @return List<CollctionGroup>
     * @version twitter 1.0
     * @date Apr 24, 20143:40:14 PM
     */
    public List<CollectionGroup> queryByAll(int userId);
    /**
     * 功能描述<添加收藏夹的分类【收藏分组】>
     * @author wanghui
     * @param  void	
     * @return void
     * @version twitter 1.0
     * @date Apr 24, 20143:51:08 PM
     */
    public CollectionGroup insert(CollectionGroup cg);
    /**
     * 功能描述<更新分类信息【收藏分组】>
     * @author wanghui
     * @param  void	
     * @return void
     * @version twitter 1.0
     * @date Apr 24, 20144:19:18 PM
     */
    public boolean updateByID(CollectionGroup cg);
    /**
     * 功能描述<删除收藏分组【收藏分组】>
     * @author wanghui
     * @param  void	
     * @return void
     * @version twitter 1.0
     * @date May 29, 201411:28:34 AM
     */
    public boolean deleteGroup(int gid);
    /**
     * 功能描述<检查组名是否存在>
     * @author wanghui
     * @param  void	
     * @return boolean
     * @version twitter 1.0
     * @date Jun 30, 201410:18:33 AM
     */
    public boolean checkGroupIsExist(int customId,String groupName);
}
