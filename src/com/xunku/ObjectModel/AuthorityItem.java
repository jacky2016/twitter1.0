package com.xunku.ObjectModel;

public class AuthorityItem {
	private String Code;//编码
	private PermissionStatus Pmi;//权限
	
	public PermissionStatus getPmi() {
		return Pmi;
	}
	public void setPmi(PermissionStatus pmi) {
		Pmi = pmi;
	}
	public String getCode() {
		return Code;
	}
	public void setCode(String code) {
		Code = code;
	}
}
