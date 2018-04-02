package com.xunku.dao.base;

import com.xunku.dto.PagerDTO;
import com.xunku.dto.sysManager.ApprovedDTO;
import com.xunku.pojo.base.Approved;
import com.xunku.pojo.base.Pager;
import com.xunku.utils.Pagefile;

public interface ApprovedDao {
    /**
     * 功能描述<添加审核账号【审核设置】>
     * @author wanghui
     * @param  void	
     * @return void
     * @version twitter 1.0
     * @date May 28, 20149:35:21 AM
     */
    public void insert(Approved a);
    /**
     * 功能描述<删除审核账号【审核设置】>
     * @author wanghui
     * @param  void	
     * @return void
     * @version twitter 1.0
     * @date May 28, 20149:38:18 AM
     */
    public void delete(int appid);
    /**
     * 功能描述<修改审核账号【审核设置】>
     * @author wanghui
     * @param  void	
     * @return void
     * @version twitter 1.0
     * @date May 28, 20149:39:01 AM
     */
    public void update(Approved a);
    /**
     * 功能描述<获取审核账号>
     * @author wanghui
     * @param  void	
     * @return Approved
     * @version twitter 1.0
     * @date Aug 5, 20141:59:47 PM
     */
    public Approved queryById(int id);
    /**
     * 功能描述<获取审核列表【审核设置】>
     * @author wanghui
     * @param  void	
     * @return Pagefile<ApprovedDTO>
     * @version twitter 1.0
     * @date May 28, 201410:42:26 AM
     */
    public Pagefile<ApprovedDTO> queryByUserId(Pager dto,int userid);
    /**
     * 功能描述<账号授权判重>
     * @author wanghui
     * @param  void	
     * @return boolean
     * @version twitter 1.0
     * @date Aug 6, 20144:18:46 PM
     */
    public boolean checkIsExsit(int userid,String uid);
}
