package com.xunku.daoImpl.base;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.xunku.cache.Cache;
import com.xunku.cache.CacheManager;
import com.xunku.constant.PortalCST;
import com.xunku.dao.base.CustomRoleDao;
import com.xunku.dto.sysManager.RolePermission;
import com.xunku.dto.sysManager.ModuleCode;
import com.xunku.dto.sysManager.UIElementCode;
import com.xunku.pojo.base.CustomRole;
import com.xunku.pojo.base.PermissionInRole;
import com.xunku.utils.DatabaseUtils;

public class CustomRoleDaoImpl implements CustomRoleDao {

    @Override
    public void insert(String Name, int CustomID) {
	Connection conn = null;
	PreparedStatement pstmt = null;
	try {
	    conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME)
		    .getConnection();
	    String sql = "INSERT INTO Base_Custom_Roles(Name,CustomID) VALUES (?,?)";
	    pstmt = conn.prepareStatement(sql);
	    pstmt.setString(1, Name);
	    pstmt.setInt(2, CustomID);
	    pstmt.execute();
	} catch (SQLException e) {
	    e.printStackTrace();
	} finally {
	    try {
		if (pstmt != null) {
		    pstmt.close();
		}
		if (conn != null) {
		    conn.close();
		}
	    } catch (SQLException e) {
		e.printStackTrace();
	    }
	}

    }

    @Override
    public void deleteByRid(int id) {
	Connection conn = null;
	PreparedStatement pstmt = null;
	try {
	    conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME)
		    .getConnection();
	    String sql = "DELETE FROM Base_Custom_Roles WHERE ID=? "
		    + "DELETE FROM Base_CustomRole_User WHERE CustomRoleId=?";
	    pstmt = conn.prepareStatement(sql);
	    pstmt.setInt(1, id);
	    pstmt.setInt(2, id);
	    pstmt.execute();
	} catch (SQLException e) {
	    e.printStackTrace();
	} finally {
	    try {
		if (pstmt != null) {
		    pstmt.close();
		}
		if (conn != null) {
		    conn.close();
		}
	    } catch (SQLException e) {
		e.printStackTrace();
	    }
	}
    }

    @Override
    public List<CustomRole> queryByUid(int cid) {
	Connection conn = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	List<CustomRole> list = new ArrayList<CustomRole>();
	try {
	    conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME)
		    .getConnection();
	    String sql = "SELECT ID,Name FROM Base_Custom_Roles WHERE CustomID=?";
	    pstmt = conn.prepareStatement(sql);
	    pstmt.setInt(1, cid);
	    rs = pstmt.executeQuery();
	    while (rs.next()) {
		CustomRole role = new CustomRole();
		int rid = rs.getInt("ID");
		role.setId(rid);
		role.setName(rs.getString("Name"));
		role.setCustomId(cid);
		role.setPermission(queryByRoleID(conn, rid));
		list.add(role);
	    }
	} catch (SQLException e) {
	    e.printStackTrace();
	} finally {
	    try {
		if (rs != null) {
		    rs.close();
		}
		if (pstmt != null) {
		    pstmt.close();
		}
		if (conn != null) {
		    conn.close();
		}
	    } catch (SQLException e) {
		e.printStackTrace();
	    }
	}
	return list;
    }

    private Map<String, List<String>> queryByRoleID(Connection conn, int id) {
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	Map<String, List<String>> map = new HashMap<String, List<String>>();
	String sql = "SELECT ModuleCode,UIECode FROM Base_Custom_PermissionInRole WHERE RoleID=?";
	try {
	    pstmt = conn.prepareStatement(sql);
	    pstmt.setInt(1, id);
	    rs = pstmt.executeQuery();
	    while (rs.next()) {
		String mcode = rs.getString("ModuleCode");
		String ucode = rs.getString("UIECode");
		if (map.containsKey(mcode)) {
		    map.get(mcode).add(ucode);
		} else {
		    map.put(mcode, new ArrayList<String>());
		}
	    }
	} catch (SQLException e) {
	    e.printStackTrace();
	} finally {
	    try {
		if (rs != null) {
		    rs.close();
		}
		if (pstmt != null) {
		    pstmt.close();
		}
	    } catch (SQLException e) {
		e.printStackTrace();
	    }
	}
	return map;
    }

    @Override
    public void update(int id, String name) {
	Connection conn = null;
	PreparedStatement pstmt = null;
	try {
	    conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME)
		    .getConnection();
	    String sql = "UPDATE Base_Custom_Roles SET Name=? WHERE ID=?";
	    pstmt = conn.prepareStatement(sql);
	    pstmt.setString(1, name);
	    pstmt.setInt(2, id);
	    pstmt.executeUpdate();
	} catch (SQLException e) {
	    e.printStackTrace();
	} finally {
	    try {
		if (pstmt != null) {
		    pstmt.close();
		}
		if (conn != null) {
		    conn.close();
		}
	    } catch (SQLException e) {
		e.printStackTrace();
	    }
	}
    }

    @Override
    public List<PermissionInRole> queryByRoleID(int roleID) {
	Connection conn = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	List<PermissionInRole> list = new ArrayList<PermissionInRole>();
	String sql = "SELECT ModuleCode,Code,UIECode FROM Base_Custom_PermissionInRole WHERE RoleID=?";
	try {
	    conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME)
		    .getConnection();
	    pstmt = conn.prepareStatement(sql);
	    pstmt.setInt(1, roleID);
	    rs = pstmt.executeQuery();
	    while (rs.next()) {
		PermissionInRole permissRole = new PermissionInRole();
		permissRole.setMcode(rs.getString("ModuleCode"));
		permissRole.setCode(rs.getString("Code"));
		permissRole.setUicode(rs.getString("UIECode"));
		list.add(permissRole);
	    }
	} catch (SQLException e) {
	    e.printStackTrace();
	} finally {
	    try {
		if (rs != null) {
		    rs.close();
		}
		if (pstmt != null) {
		    pstmt.close();
		}
		if (conn != null) {
		    conn.close();
		}
	    } catch (SQLException e) {
		e.printStackTrace();
	    }
	}
	return list;
    }

    @Override
    public void insertPermission(RolePermission role) {
	Connection conn = null;
	PreparedStatement pstmt = null;
	PreparedStatement pstmt2 = null;
	try {
	    conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME)
		    .getConnection();
	    String delSql = "delete from Base_Custom_PermissionInRole where RoleID=?";
	    String insertSql = "insert into Base_Custom_PermissionInRole(RoleID,ModuleCode,Code,UIECode) values (?,?,?,?)";
	    conn.setAutoCommit(false);
	    // 根据角色删除授权
	    pstmt = conn.prepareStatement(delSql);
	    pstmt.setInt(1, role.getRoleId());
	    pstmt.executeUpdate();

	    // 添加授权
	    for (int i = 0; i < role.getPermissRole().size(); i++) {
		int roleId = role.getRoleId();
		PermissionInRole permissRole = role.getPermissRole().get(i);
		pstmt2 = conn.prepareStatement(insertSql);
		pstmt2.setInt(1, roleId);
		pstmt2.setString(2, permissRole.getMcode());
		pstmt2.setString(3, permissRole.getCode());
		pstmt2.setString(4, permissRole.getUicode());
		pstmt2.execute();
	    }
	    conn.commit();
	} catch (SQLException e) {
	    e.printStackTrace();
	    try {
		conn.rollback();
	    } catch (SQLException e1) {
		e1.printStackTrace();
	    }
	} finally {
	    try {
		if (pstmt != null) {
		    pstmt.close();
		}
		if (pstmt2 != null) {
		    pstmt2.close();
		}
		if (conn != null) {
		    conn.close();
		}
	    } catch (SQLException e) {
		e.printStackTrace();
	    }
	}
    }

    @Override
    public List<ModuleCode> getRolePermissionList() {
	Connection conn = null;
	CallableStatement cstmt = null;
	ResultSet rs = null;
	ResultSet rs2 = null;
	List<ModuleCode> list = new ArrayList<ModuleCode>();
	try {
	    conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME)
		    .getConnection();
	    cstmt = conn
		    .prepareCall("{call sp_CustomRole_getRolePermissionList()}");
	    cstmt.execute();
	    Map<String, ModuleCode> map = new HashMap<String, ModuleCode>();
	    Cache cache = new Cache();
	    rs = cstmt.getResultSet();
	    while (rs.next()) {
		ModuleCode mc = new ModuleCode();
		String code = rs.getString("Code");
		mc.setMcode(code);
		mc.setDesc(rs.getString("Caption"));
		map.put(code, mc);
		list.add(mc);
	    }
	    if (cstmt.getMoreResults()) {
		rs2 = cstmt.getResultSet();
		while (rs2.next()) {
		    UIElementCode ecode = new UIElementCode();
		    ecode.setEcode(rs.getString("Code"));
		    ecode.setDesc(rs.getString("Title"));
		    String mcode = rs.getString("ModuleCode");
		    map.get(mcode).getUicode().add(ecode);
		}
	    }
	    // 将权限列表放入缓存
	    cache.setKey("PermissionList");
	    cache.setValue(list);
	    CacheManager.putCache("ModuleCode", cache);
	} catch (SQLException e) {
	    e.printStackTrace();
	} finally {
	    try {
		if (rs != null) {
		    rs.close();
		}
		if (rs2 != null) {
		    rs2.close();
		}
		if (cstmt != null) {
		    cstmt.close();
		}
		if (conn != null) {
		    conn.close();
		}
	    } catch (SQLException e) {
		e.printStackTrace();
	    }
	}
	return list;
    }

    @Override
    public boolean checkIsExsit(int customid, String name) {
	Connection conn = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	boolean isExsit = false;
	try {
	    conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME)
		    .getConnection();
	    String sql = "select ID from Base_Custom_Roles where CustomID=? and Name=?";
	    pstmt = conn.prepareStatement(sql);
	    pstmt.setInt(1, customid);
	    pstmt.setString(2, name);
	    rs = pstmt.executeQuery();
	    if (rs.next()) {
		isExsit = true;
	    }
	} catch (SQLException e) {
	    e.printStackTrace();
	} finally {
	    try {
		if (rs != null) {
		    rs.close();
		}
		if (pstmt != null) {
		    pstmt.close();
		}
		if (conn != null) {
		    conn.close();
		}
	    } catch (SQLException e) {
		e.printStackTrace();
	    }
	}
	return isExsit;
    }

    @Override
    public boolean checkRoleIsUser(int customid, int roleid) {
	Connection conn = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	boolean isUser = false;
	try {
	    conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME)
		    .getConnection();
	    String sql = "select COUNT(ID) as count from vi_BaseUser_queryByAll where CustomID=? and CustomRoleId=?  and IsEnabled=1";
	    pstmt = conn.prepareStatement(sql);
	    pstmt.setInt(1, customid);
	    pstmt.setInt(2, roleid);
	    rs = pstmt.executeQuery();
	    if (rs.next()) {
		int count = rs.getInt("count");
		if (count > 0) {
		    isUser = true;
		}
	    }
	} catch (SQLException e) {
	    e.printStackTrace();
	} finally {
	    try {
		if (rs != null) {
		    rs.close();
		}
		if (pstmt != null) {
		    pstmt.close();
		}
		if (conn != null) {
		    conn.close();
		}
	    } catch (SQLException e) {
		e.printStackTrace();
	    }
	}
	return isUser;
    }

    @Override
    public boolean checkIsExsit(int customid) {
	Connection conn = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	boolean isExsit = false;
	try {
	    conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME)
		    .getConnection();
	    String sql = "select COUNT(ID) as count from Base_Custom_Roles where CustomID=?";
	    pstmt = conn.prepareStatement(sql);
	    pstmt.setInt(1, customid);
	    rs = pstmt.executeQuery();
	    if (rs.next()) {
		if(rs.getInt("count") > 0){
		    isExsit = true;
		}
	    }
	} catch (SQLException e) {
	    e.printStackTrace();
	} finally {
	    try {
		if (rs != null) {
		    rs.close();
		}
		if (pstmt != null) {
		    pstmt.close();
		}
		if (conn != null) {
		    conn.close();
		}
	    } catch (SQLException e) {
		e.printStackTrace();
	    }
	}
	return isExsit;
    }

}
