package com.xunku.dao.base;

import java.util.List;

import com.xunku.pojo.base.Smileie;

public interface SmileieDao {
    /**
     * 功能描述<获取所有表情>
     * @author wanghui
     * @param  void	
     * @return List<Smileie>
     * @version twitter 1.0
     * @date Apr 17, 20141:53:00 PM
     */
    public List<Smileie> queryByAll();
}
