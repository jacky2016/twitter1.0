package com.xunku.dao.my;

import java.util.List;

public interface MyAccountDao {
    /**
     * 功能描述<给账号授权>
     * @author wanghui
     * @param  void	
     * @return boolean
     * @version twitter 1.0
     * @date Aug 8, 20144:34:05 PM
     */
    public boolean insert(int customid,int userid,List<String> uidList);
}
