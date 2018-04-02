package com.xunku.daoImpl.base;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.xunku.constant.PortalCST;
import com.xunku.dao.base.ModuleDao;
import com.xunku.pojo.base.BaseModule;
import com.xunku.utils.DatabaseUtils;

public class ModuleDaoImpl implements ModuleDao {

    @Override
    public List<BaseModule> queryByAll() {
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
	return modules;
    }
 
}
