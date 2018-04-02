package com.xunku.dao.base;

import java.util.List;

import com.xunku.pojo.base.Custom;
public interface CustomDao {
    /**
     * 功能描述<添加客户信息>
     * @author wanghui
     * @param  void	
     * @return void
     * @version twitter 1.0
     * @date Apr 15, 20143:45:20 PM
     */
    public void insertCustom(Custom c);
    /**
     * 功能描述<删除客户信息>
     * @author wanghui
     * @param  void	
     * @return boolean
     * @version twitter 1.0
     * @date Apr 15, 20143:46:10 PM
     */
    public boolean deleteById(int cid);
    /**
     * 功能描述<更新客户信息>
     * @author wanghui
     * @param  void	
     * @return boolean
     * @version twitter 1.0
     * @date Apr 15, 20143:48:08 PM
     */
    public Custom queryById(int cid);
    /**
     * 功能描述<获取首页加载的banner>
     * @author wanghui
     * @param  void	
     * @return String
     * @version twitter 1.0
     * @date May 12, 20144:05:24 PM
     */
    public String queryByUid(int uid);
    
    /**
     * 返回所有现在可用的客户信息
     * @return
     */
    public List<Custom> queryAll();
}
