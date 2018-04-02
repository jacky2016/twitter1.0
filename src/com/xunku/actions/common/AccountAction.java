package com.xunku.actions.common;

import java.util.Map;

import com.xunku.actions.ActionBase;
import com.xunku.dto.AccountDTO;

public class AccountAction extends ActionBase {

	private static final String ID = "accountid";
	
	@Override
	public Object doAction() {
		// TODO Auto-gnerated method stub
		Map<String, String> params = this.getParameters();
		String AccountID = params.get(ID);
		
		AccountDTO dto = new AccountDTO();
	
		return dto;
	}

}
