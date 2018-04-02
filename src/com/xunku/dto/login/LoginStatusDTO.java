package com.xunku.dto.login;


import java.util.Map;

import com.xunku.dto.IDTO;

public class LoginStatusDTO implements IDTO{

	public String username;
	public String message;
	public int status;
	public Integer[] Attributes;	
	public Map<String,Integer> Atts;
}
