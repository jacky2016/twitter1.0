package com.xunku.utils;

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
import com.xunku.dto.sysManager.ModuleCode;
import com.xunku.dto.sysManager.UIElementCode;
import com.xunku.pojo.base.BaseModule;
import com.xunku.pojo.base.City;
import com.xunku.pojo.base.Province;
import com.xunku.pojo.task.CollectionCache;

//加载缓存 by wanghui
public class WebCache {

    public static void initSystemModule(){
	Connection conn = null;
	CallableStatement cstmt = null;
	ResultSet rs = null;
	ResultSet rs2 = null;
	List<BaseModule> modules = new ArrayList<BaseModule>();
	try {
	    conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME).getConnection();
	    cstmt = conn.prepareCall("{call sp_BaseModule_queryByAll()}");
	    cstmt.execute();
	    rs = cstmt.getResultSet();
	    Map<String, BaseModule> map = new HashMap<String, BaseModule>();
	    Cache cache = new Cache();
	    while (rs.next()) {
		BaseModule bm = new BaseModule();
		int id = rs.getInt("ID");
		bm.setId(id);
		bm.setCode(rs.getString("Code"));
		bm.setCaption(rs.getString("Caption"));
		bm.setParent(rs.getInt("Parent"));
		modules.add(bm);
		map.put(String.valueOf(id), bm);
	    }
	    if(cstmt.getMoreResults()){
		rs2 = cstmt.getResultSet();
		while(rs2.next()){
		    BaseModule bm = new BaseModule();
		    bm.setId(rs2.getInt("ID"));
		    bm.setCode(rs2.getString("Code"));
		    bm.setCaption(rs2.getString("Caption"));
		    int pid = rs2.getInt("Parent");
		    bm.setParent(pid);
		    map.get(String.valueOf(pid)).getModule().add(bm);
		}
	    }
	    cache.setKey(PortalCST.SystemModuleCacheKey);
	    cache.setValue(modules);
	    CacheManager.putCache(PortalCST.SystemModuleCacheKey, cache);
	    map = null;
	} catch (SQLException e) {
	    e.printStackTrace();
	}finally{
	    try {
		if(rs2 != null){
		    rs2.close();
		}
		if(rs != null){
		    rs.close();
		}
		if(cstmt != null){
		    cstmt.close();
		}
		if(conn != null){
		    conn.close();
		}
	    } catch (SQLException e) {
		e.printStackTrace();
	    }
	}
    }
    
    public static void initRolePermission() {
	Connection conn = null;
	CallableStatement cstmt = null;
	ResultSet rs = null;
	ResultSet rs2 = null;
	ResultSet rs3 = null;
	List<ModuleCode> list = new ArrayList<ModuleCode>();
	try {
	    conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME).getConnection();
	    cstmt = conn
		    .prepareCall("{call sp_CustomRole_getRolePermissionList()}");
	    cstmt.execute();
	    Map<String, ModuleCode> map = new HashMap<String, ModuleCode>();
	    Map<String, ModuleCode> map2 = new HashMap<String, ModuleCode>();
	    Map<String, ModuleCode> map3 = new HashMap<String, ModuleCode>();
	    Cache cache = new Cache();
	    rs = cstmt.getResultSet();
	    while (rs.next()) {
		ModuleCode mc = new ModuleCode();
		int pid = rs.getInt("ID");
		mc.setPid(pid);
		mc.setMcode(rs.getString("Code"));
		mc.setDesc(rs.getString("Caption"));
		list.add(mc);
		map.put(String.valueOf(pid), mc);
		map3.put(rs.getString("Code"), mc);
	    }
	    if(cstmt.getMoreResults()){
		rs2 = cstmt.getResultSet();
		while(rs2.next()){
		    ModuleCode mc = new ModuleCode();
		    int pid = rs2.getInt("Parent");
		    mc.setPid(pid);
		    String code = rs2.getString("Code");
		    mc.setMcode(code);
		    mc.setDesc(rs2.getString("Caption"));
		    map.get(String.valueOf(pid)).getModuleList().add(mc);
		    map2.put(code, mc);
		}
	    }
	    if (cstmt.getMoreResults()) {
		rs3 = cstmt.getResultSet();
		while (rs3.next()) {
		    UIElementCode ecode = new UIElementCode();
		    ecode.setEcode(rs3.getString("Code"));
		    ecode.setDesc(rs3.getString("Title"));
		    String mcode = rs3.getString("ModuleCode");
		    ecode.setModelCode(mcode);
		    ModuleCode mc = map2.get(mcode);
		    if(mc != null){
			mc.getUicode().add(ecode);
		    }
		    ModuleCode mc2 = map3.get(mcode);
		    if(mc2 != null){
			mc2.getUicode().add(ecode);
		    }
		}
	    }
	    // 将权限列表放入缓存
	    cache.setKey(PortalCST.PermissionListCacheKey);
	    cache.setValue(list);
	    CacheManager.putCache(PortalCST.PermissionListCacheKey, cache);
	    map = null;
	    map2 = null;
	} catch (SQLException e) {
	    e.printStackTrace();
	} finally {
	    try {
		if (cstmt != null) {
		    cstmt.close();
		}
		if (rs != null) {
		    rs.close();
		}
		if (rs2 != null) {
		    rs2.close();
		}
		if (rs3 != null) {
		    rs3.close();
		}
		if (conn != null) {
		    conn.close();
		}
	    } catch (SQLException e) {
		e.printStackTrace();
	    }
	}
    }

    public static void initLocation() {
	Connection conn = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	try {
	    conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME).getConnection();
	    pstmt = conn
		    .prepareStatement("select Code,City,Province from Base_City");
	    rs = pstmt.executeQuery();
	    Map<String, List<City>> map = new HashMap<String, List<City>>();
	    Map<Integer, String> cityMap = new HashMap<Integer, String>();
	    Cache cache = new Cache();
	    while (rs.next()) {
		Province pro = new Province();
		String provience = rs.getString("Province");
		pro.setProvince(provience);
		if (map.containsKey(provience)) {
		    City city = new City();
		    int code = rs.getInt("Code");
		    String cityName = rs.getString("City");
		    city.setCode(code);
		    city.setCity(cityName);
		    cityMap.put(code, cityName);
		    map.get(provience).add(city);
		} else {
		    List<City> cityList = new ArrayList<City>();
		    City city = new City();
		    int code = rs.getInt("Code");
		    String cityName = rs.getString("City");
		    city.setCode(code);
		    city.setCity(cityName);
		    cityList.add(city);
		    
		    cityMap.put(code, cityName);
		    map.put(provience, cityList);
		}
	    }
	    //code-city缓存
	    Cache cacheCity = new Cache();
	    cacheCity.setKey(PortalCST.GetCityCacheKey);
	    cacheCity.setValue(cityMap);
	    CacheManager.putCache(PortalCST.GetCityCacheKey, cacheCity);
	    // 将地域列表放入缓存
	    cache.setKey(PortalCST.BaseCityCacheKey);
	    cache.setValue(map);
	    CacheManager.putCache(PortalCST.BaseCityCacheKey, cache);
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
    }

    public static void initCollectionCache() {
	Connection conn = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	try {

	    conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME)
		    .getConnection();
	    pstmt = conn
		    .prepareStatement("SELECT * FROM vi_collectioncache WHERE CustomID=(SELECT CustomID FROM Base_Users WHERE ID=?)");
	    rs = pstmt.executeQuery();
	    Map<String, CollectionCache> map = new HashMap<String, CollectionCache>();
	    Cache cache = new Cache();
	    while (rs.next()) {

	    }
	    // 将权限列表放入缓存
	    cache.setKey(PortalCST.BaseCityCacheKey);
	    cache.setValue(map);
	    CacheManager.putCache(PortalCST.BaseCityCacheKey, cache);
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
    }

    public static int initWeiboAnalsysCache(int userId) {
	Connection conn = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	int count = 0;
	try {
	    conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME)
		    .getConnection();
	    String sql = "SELECT COUNT(id) as count FROM Weibo_List WHERE userId=?";
	    pstmt = conn.prepareStatement(sql);
	    pstmt.setInt(1, userId);
	    rs = pstmt.executeQuery();
	    while (rs.next()) {
		count = rs.getInt("count");
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
	return count;
    }
}
