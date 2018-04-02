package com.xunku.daoImpl.base;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.xunku.ObjectModel.AuthorityItem;
import com.xunku.ObjectModel.AuthorityType;
import com.xunku.ObjectModel.PermissionStatus;
import com.xunku.constant.PortalCST;
import com.xunku.dao.base.AuthorityDao;
import com.xunku.utils.DatabaseUtils;

public class AuthorityDaoImpl implements AuthorityDao {

	@Override
	public List<AuthorityItem> GetAuthority(int userID, AuthorityType type,
			Boolean isAdmin) {
		if (isAdmin)
			return GetAuthorityForAdmin(userID, type);
		else
			return GetAuthorityForUser(userID, type);
	}

	// 普通用户的
	private List<AuthorityItem> GetAuthorityForUser(int userID,
			AuthorityType type) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<AuthorityItem> menus = null;
		try {
			conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME)
					.getConnection();
			pstmt = conn.prepareCall("{call sp_nv_Get_Base_User(?,?)}");
			pstmt.setInt(1, userID);
			pstmt.setInt(2, type.value());
			rs = pstmt.executeQuery();
			menus = new ArrayList<AuthorityItem>();
			if (type == AuthorityType.menu) {
				while (rs.next()) {
					String code = rs.getString("Code");
					if(code.equals("")){
						code = rs.getString("ModuleCode");
					}
					
					//添加主菜单，如果不存在就添加
					String mainCode = rs.getString("ModuleCode");
					boolean isExist = false;
					for(AuthorityItem item :menus){
						if(item.getCode().equals(mainCode)){
							isExist=true;
							break;
						}
					}
					if(!isExist) {
						AuthorityItem ba = new AuthorityItem();
						ba.setCode(mainCode);
						ba.setPmi(PermissionStatus.Allow);
						menus.add(ba);
					}
					
					//添加子菜单
					AuthorityItem ba = new AuthorityItem();
					ba.setCode(code);
					ba.setPmi(PermissionStatus.Allow);
					menus.add(ba);
				}
			}else {
				while (rs.next()) {
					String code = rs.getString("UIECode");
					if(!code.equals("")){
						AuthorityItem ba = new AuthorityItem();
						ba.setCode(code);
						ba.setPmi(PermissionStatus.Allow);
						menus.add(ba);
					}
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
		return menus;
	}

	
	// 管理员的
	private List<AuthorityItem> GetAuthorityForAdmin(int userID,
			AuthorityType type) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<AuthorityItem> menus = null;
		try {
			conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME)
					.getConnection();
			pstmt = conn.prepareCall("{call sp_nv_Get_Base_Authority(?,?)}");
			pstmt.setInt(1, userID);
			pstmt.setInt(2, type.value());
			rs = pstmt.executeQuery();
			menus = new ArrayList<AuthorityItem>();
			while (rs.next()) {
				AuthorityItem ba = new AuthorityItem();
				ba.setCode(rs.getString("Code"));
				ba.setPmi(PermissionStatus.Allow);
				menus.add(ba);
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
		return menus;
	}

	public List<AuthorityItem> GetAllAuthorityCode() {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<AuthorityItem> menus = null;
		try {
			conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME)
					.getConnection();
			pstmt = conn.prepareCall("{call sp_nv_Get_Base_Authority(?,?)}");
			pstmt.setInt(1, -1);
			pstmt.setInt(2, AuthorityType.all.value());
			pstmt.execute();// .executeQuery();

			rs = pstmt.getResultSet();
			menus = new ArrayList<AuthorityItem>();
			while (rs.next()) {
				AuthorityItem ba = new AuthorityItem();
				ba.setCode(rs.getString("Code"));
				ba.setPmi(PermissionStatus.Allow);
				menus.add(ba);
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
		return menus;
	}

	@Override
	public Map<String, String[]> GetAllPmiHtml() {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Map<String, String[]> pHtml = null;
		try {
			conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME)
					.getConnection();
			pstmt = conn
					.prepareStatement("select PmiID,Html,NoPmiHtml from Base_UIElementHtml");
			rs = pstmt.executeQuery();
			pHtml = new HashMap<String, String[]>();
			while (rs.next()) {
				String PmiID = rs.getString("PmiID");
				String Html = rs.getString("Html");
				String NoPmiHtml = rs.getString("NoPmiHtml");
				if (!pHtml.containsKey(PmiID))
					pHtml.put(PmiID, new String[] { Html, NoPmiHtml.trim() });
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
		return pHtml;
	}

	public Map<String, Integer> GetBase_Custom_Config() {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Map<String, Integer> pHtml = null;
		try {
			conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME)
					.getConnection();
			pstmt = conn
					.prepareStatement("select ID,Name from dbo.Base_Custom_Config");
			rs = pstmt.executeQuery();
			pHtml = new HashMap<String, Integer>();
			while (rs.next()) {
				int ID = rs.getInt("ID");
				String Name = rs.getString("Name");
				if (!pHtml.containsKey(Name))
					pHtml.put(Name, ID);
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
		return pHtml;
	}

	public Map<Integer, Integer> GetBase_Custom_Profile(int customID) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Map<Integer, Integer> pHtml = null;
		try {
			conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME)
					.getConnection();
			pstmt = conn
					.prepareStatement("select ConfID,Value from dbo.Base_Custom_Profiles where CustomID = ?");
			pstmt.setInt(1, customID);
			rs = pstmt.executeQuery();
			pHtml = new HashMap<Integer, Integer>();
			while (rs.next()) {
				int ConfID = rs.getInt("ConfID");
				int Value = rs.getInt("Value");
				if (!pHtml.containsKey(ConfID))
					pHtml.put(ConfID, Value);
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
		return pHtml;
	}
}
