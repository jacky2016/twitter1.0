package com.xunku.dao.base;

import com.xunku.app.model.App;

public interface AppDao {
    /**
     * 功能描述<通过appkey获取微博应用程序的基本信息>
     * @author wanghui
     * @param  void	
     * @return App
     * @version twitter 1.0
     * @date Jul 7, 20146:06:03 PM
     */
    public App queryByAppkey(String appkey);
    
    public App queryById(int appid);
    
}
