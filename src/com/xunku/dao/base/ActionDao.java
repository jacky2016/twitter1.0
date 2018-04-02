package com.xunku.dao.base;

import java.util.List;

import com.xunku.pojo.base.BaseAction;

public interface ActionDao {
    /**
     * 功能描述<获取所有定义的Action类>
     * @author wanghui
     * @param  void	
     * @return List<BaseAction>
     * @version twitter 1.0
     * @date Apr 15, 20142:51:13 PM
     */
    public List<BaseAction> queryBaseActions();
}
