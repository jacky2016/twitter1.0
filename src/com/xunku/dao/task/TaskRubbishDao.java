package com.xunku.dao.task;

import java.util.List;

import com.xunku.dto.task.RubbishDTO;
import com.xunku.pojo.task.Rubbish;
public interface TaskRubbishDao {
    /**
     * 功能描述<添加垃圾词组>
     * @author wanghui
     * @param  void	
     * @return void
     * @version twitterr 1.0
     * @date May 19, 201410:21:16 AM
     */
    public void insert(Rubbish r);
    /**
     * 功能描述<删除垃圾词组>
     * @author wanghui
     * @param  void	
     * @return void
     * @version twitter 1.0
     * @date May 19, 201410:39:09 AM
     */
    public void deleteByGID(int gid);
    /**
     * 功能描述<更新垃圾词>
     * @author wanghui
     * @param  void	
     * @return void
     * @version twitter 1.0
     * @date May 19, 201410:23:16 AM
     */
    public void update(Rubbish r);
    /**
     * 功能描述<获取客户下的垃圾词组>
     * @author wanghui
     * @param  void	
     * @return List<Rubbish>
     * @version twitter 1.0
     * @date May 19, 201410:24:10 AM
     */
    public List<RubbishDTO> queryByCustom(int customId);
    /**
     * 功能描述<判断垃圾词组名称是否重复>
     * @author wanghui
     * @param  void	
     * @return boolean
     * @version twitter 1.0
     * @date Aug 21, 20142:56:40 PM
     */
    public boolean checkIsExsit(int customid,String group);
    /**
     * 功能描述<获取垃圾词组的数量>
     * @author wanghui
     * @param  void	
     * @return int
     * @version twitter 1.0
     * @date Aug 21, 20142:59:10 PM
     */
    public int getRubbishGroupCount(int customid);
    
}
