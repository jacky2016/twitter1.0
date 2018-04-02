package com.xunku.dao.base;

import com.xunku.pojo.base.Term;

public interface TermDao {
    /**
     * 功能描述<添加>
     * @author wanghui
     * @param  void	
     * @return boolean
     * @version twitter 1.0
     * @date Jun 26, 20144:14:38 PM
     */
    public boolean insert(Term t);
    /**
     * 功能描述<按token删除>
     * @author wanghui
     * @param  void	
     * @return boolean
     * @version twitter 1.0
     * @date Jun 26, 20144:14:53 PM
     */
    public boolean deleteByToken(String token);
}
