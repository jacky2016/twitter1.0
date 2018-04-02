package com.xunku.dao.base;

import java.util.List;
import java.util.Map;
import com.xunku.ObjectModel.AuthorityType;
import com.xunku.ObjectModel.AuthorityItem;


public interface AuthorityDao { 
	 public List<AuthorityItem> GetAuthority(int userID,AuthorityType type,Boolean isAdmin);
	 public List<AuthorityItem> GetAllAuthorityCode();
	 public Map<String,String[]> GetAllPmiHtml();	 
	 public Map<String,Integer>  GetBase_Custom_Config();
	 public Map<Integer,Integer> GetBase_Custom_Profile(int customID);
}
