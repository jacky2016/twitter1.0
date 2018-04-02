package com.xunku.dao.task;

import java.util.List;
import java.util.Map;

import com.xunku.app.enums.Platform;
import com.xunku.app.interfaces.ITweet;
import com.xunku.constant.DealStatusEnum;
import com.xunku.dto.PagerDTO;
import com.xunku.pojo.task.Collection;
import com.xunku.utils.Pagefile;

public interface CollectionDao {
    /**
     * 功能描述<收藏一条微博信息>
     * @author wanghui
     * @param  void	
     * @return >0插入成功，=0插入失败，=-1已经存在
     * @version twitter 1.0
     * @date Apr 25, 20149:23:55 AM
     */
    public int insert(Collection c);
    /**
     * 功能描述<更新状态【舆情处理】>
     * @author wanghui
     * @param  status【0=待处理，1=已处理，2=已忽略】	
     * @return boolean
     * @version twitter 1.0
     * @date Jun 17, 20145:30:30 PM
     */
    public boolean updateStatus(int cid,int status);
    /**
     * 功能描述<舆情处理：待处理、已处理、已忽略>
     * @author wanghui
     * @param  status枚举【0=待处理，1=已处理，2=已忽略】，startTime开始时间，endTime结束时间，platform
     * @return Pagefile<Post>
     * @version twitter 1.0
     * @date Jun 17, 20145:56:45 PM
     */
    public Pagefile<Collection> queryWeiboDeal(DealStatusEnum status,String startTime,String endTime,Platform platform,int gid,PagerDTO dto);
    /**
     * 功能描述<通过tid和platform获取一条收藏的微博>
     * @author wanghui
     * @param  void	
     * @return Post
     * @version twitter 1.0
     * @date Jun 27, 201411:30:21 AM
     */
    public ITweet queryCollectionPost(String tid,int platform);
    /**
     * 功能描述<删除收藏>
     * @author wanghui
     * @param  void	
     * @return boolean
     * @version twitter 1.0
     * @date Jun 17, 201411:45:20 AM
     */
    public boolean deleteByCid(int cid);
    /**
     * 功能描述<检查收集微博的状态>
     * @author wanghui
     * @param  void	
     * @return Map<String,Boolean>
     * @version twitter 1.0
     * @date Jul 4, 201410:37:51 AM
     */
    public Map<String, Integer> checkCollectionPostStatus(List<String> tids,int customid);
    /**
     * 功能描述<获取当前客户下的收集的数量>
     * @author wanghui
     * @param  void	
     * @return int
     * @version twitter 1.0
     * @date Jul 9, 20149:29:48 AM
     */
    public int queryCollectionTotal(int customId);
    /**
     * 功能描述<判断收集微博的状态已处理或已忽略不能取消收集>
     * @author wanghui
     * @param  void	
     * @return boolean
     * @version twitter 1.0
     * @date Sep 1, 20143:14:51 PM
     */
    public boolean checkIsDealStatus(int customid,long postid);
}
