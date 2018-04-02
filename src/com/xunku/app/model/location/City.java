package com.xunku.app.model.location;

/**
 * 城市对象
 * 
 * @author thriller
 * 
 */
public class City {

	private City() {
	}

	public City(int provinceId, int code, String name) {
		this.code = code;
		this.name = name;
		this.provinceId = provinceId;
	}

	int code;
	String name;
	int provinceId;

	public int getProvince() {
		return this.provinceId;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public static City getEmpty() {
		City city = new City();
		city.setCode(0);
		city.provinceId = 0;
		city.setName("未知城市");
		return city;
	}
}
