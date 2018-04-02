package com.xunku.dao.base;

import java.util.List;

import com.xunku.pojo.base.BaseModule;

public interface ModuleDao {
    /**
     * 功能描述<获取所有模块的信息>
     * @author wanghui
     * @param  void	
     * @return List<BaseModule>
     * @version twitter 1.0
     * @date Apr 15, 20142:56:12 PM
     */
    public List<BaseModule> queryByAll();
}
