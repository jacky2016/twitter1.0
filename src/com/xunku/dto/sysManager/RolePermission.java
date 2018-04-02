package com.xunku.dto.sysManager;

import java.util.ArrayList;
import java.util.List;

import com.xunku.pojo.base.PermissionInRole;
//角色权限维护 by wanghui
public class RolePermission {
    private int roleId;                 //角色id
    private List<PermissionInRole> permissRole = new ArrayList<PermissionInRole>();//菜单列表
   
    
    public int getRoleId() {
        return roleId;
    }
    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }
    public List<PermissionInRole> getPermissRole() {
        return permissRole;
    }
    public void setPermissRole(List<PermissionInRole> permissRole) {
        this.permissRole = permissRole;
    }

}
