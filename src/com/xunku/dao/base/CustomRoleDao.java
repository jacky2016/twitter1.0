package com.xunku.dao.base;

import java.util.List;

import com.xunku.dto.sysManager.RolePermission;
import com.xunku.dto.sysManager.ModuleCode;
import com.xunku.pojo.base.CustomRole;
import com.xunku.pojo.base.PermissionInRole;

public interface CustomRoleDao {
    
    /**
     * 功能描述<角色删除时，先判断用户维护里的用户有没有该角色，即已被使用的角色不能删除>
     * @author wanghui
     * @param  void	
     * @return boolean
     * @version twitter 1.0
     * @date Aug 19, 20145:56:11 PM
     */
    public boolean checkRoleIsUser(int customid,int roleid);
    /**
     * 功能描述<角色权限维护[添加客户角色]>
     * @author wanghui
     * @param  void	
     * @return void
     * @version twitter 1.0
     * @date May 7, 201411:20:50 AM
     */
    public void insert(String name,int customId);
    /**
     * 功能描述<角色权限维护[删除客户角色]>
     * @author wanghui
     * @param  void	
     * @return void
     * @version twitter 1.0
     * @date May 7, 20141:09:46 PM
     */
    public void deleteByRid(int id);
    /**
     * 功能描述<角色权限维护[修改客户角色]>
     * @author wanghui
     * @param  void	
     * @return void
     * @version twitter 1.0
     * @date May 7, 20141:10:34 PM
     */
    public void update(int id,String name);
    /**
     * 功能描述<角色权限维护[通过客户编号获取客户角色]>
     * @author wanghui
     * @param  void	
     * @return List<CustomRole>
     * @version twitter 1.0
     * @date May 7, 20141:07:57 PM
     */
    public List<CustomRole> queryByUid(int cid);
    /**
     * 功能描述<角色权限维护[通过客户角色编号获取权限id]>
     * @author wanghui
     * @param  void	
     * @return List<PermissionInRole>
     * @version twitter 1.0
     * @date May 19, 20146:09:31 PM
     */
    public List<PermissionInRole> queryByRoleID(int roleID);
    /**
     * 功能描述<给指定的角色授权>
     * @author wanghui
     * @param  void	
     * @return void
     * @version twitter 1.0
     * @date May 20, 20144:01:24 PM
     */
    public void insertPermission(RolePermission role);
    /**
     * 功能描述<获取权限列表【角色权限维护】>
     * @author wanghui
     * @param  void	
     * @return List<ModuleCode>
     * @version twitter 1.0
     * @date May 21, 20145:38:49 PM
     */
    public List<ModuleCode> getRolePermissionList();
    /**
     * 功能描述<判断角色是否存在【角色权限维护】>
     * @author wanghui
     * @param  void	
     * @return boolean
     * @version twitter 1.0
     * @date Aug 6, 20144:29:08 PM
     */
    public boolean checkIsExsit(int customid,String name);
    /**
     * 功能描述<判断该客户下是否有角色>
     * @author wanghui
     * @param  void	
     * @return boolean
     * @version twitter 1.0
     * @date Aug 20, 20148:16:07 PM
     */
    public boolean checkIsExsit(int customid);
}
