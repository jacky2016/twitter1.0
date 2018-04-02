package com.xunku.app.model.location;

import java.util.HashMap;
import java.util.Map;

/**
 * 国家信息
 * 
 * @author wujian
 * @created on Aug 26, 2014 3:18:45 PM
 */
public class Country {

	String name;
	String code;
	Map<Integer, Province> provinces;

	public Country(String code, String name) {
		this.provinces = new HashMap<Integer, Province>();
		this.code = code;
		this.name = name;
	}

	public Map<Integer, Province> getProvinces() {
		return this.provinces;
	}

	public String getName() {
		return this.name;
	}

	public String getCode() {
		return this.code;
	}
}
