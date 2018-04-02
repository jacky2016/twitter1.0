package com.xunku.app.model.location;

import java.util.HashMap;
import java.util.Map;

public class Province {
	String name;
	int code;
	Map<Integer, City> cities;

	final static Province _empty;
	static {
		_empty = new Province(0, "未知城市");
	}

	public static Province Empty() {
		return _empty;
	}

	public Province(int code, String name) {
		this.cities = new HashMap<Integer, City>();
		this.code = code;
		this.name = name;
	}

	public String getName() {
		return this.name;
	}

	public int getCode() {
		return this.code;
	}

	public Map<Integer, City> getCities() {
		return this.cities;
	}
}
