package com.xunku.daoImpl.base;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.xunku.cache.Cache;
import com.xunku.cache.CacheManager;
import com.xunku.constant.PortalCST;
import com.xunku.dao.base.ActionDao;
import com.xunku.pojo.base.BaseAction;
import com.xunku.utils.DatabaseUtils;

public class ActionDaoImpl implements ActionDao {

    @SuppressWarnings("unchecked")
    @Override
    public List<BaseAction> queryBaseActions() {
	Cache cache = CacheManager.getCacheInfo(PortalCST.GetActionCacheKey);
	if (cache != null) {
	    List<BaseAction> list = (List<BaseAction>) cache.getValue();
	    if (list != null || list.size() != 0) {
		return list;
	    }
	}
	Connection conn = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	List<BaseAction> actions = null;
	try {
	    conn = DatabaseUtils.cpdsMap.get(PortalCST.POOLED_BASE_NAME)
		    .getConnection();
	    pstmt = conn
		    .prepareStatement("select ID,ModuleCode,Code,Description,Author,Class from Base_Actions where ModuleCode in (select Code from Base_Modules)");
	    rs = pstmt.executeQuery();
	    actions = new ArrayList<BaseAction>();
	    Cache c = new Cache();
	    while (rs.next()) {
		BaseAction ba = new BaseAction();
		ba.setId(rs.getInt("ID"));
		ba.setModuleCode(rs.getString("ModuleCode"));
		ba.setCode(rs.getString("Code"));
		ba.setDescription(rs.getString("Description"));
		ba.setAuthor(rs.getString("Author"));
		ba.setClazz(rs.getString("Class"));
		actions.add(ba);
	    }
	    c.setKey(PortalCST.GetActionCacheKey);
	    c.setValue(actions);
	    CacheManager.putCache(PortalCST.GetActionCacheKey, c);
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
	return actions;
    }

}
