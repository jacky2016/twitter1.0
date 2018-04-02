package com.xunku.app.helpers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import weibo4j.org.json.JSONArray;
import weibo4j.org.json.JSONObject;

import com.xunku.app.Utility;
import com.xunku.app.enums.Platform;
import com.xunku.app.model.location.City;
import com.xunku.app.model.location.Country;
import com.xunku.app.model.location.Province;

/**
 * 区域帮助类，该类会装载新浪和腾讯的区域信息保存在内存中使用，目前腾讯的无数据
 * 
 * @author wujian
 * @created on Aug 26, 2014 5:47:03 PM
 */
public class LocationHelper {

	static final List<Country> _sinaCountries;
	static final List<Country> _tencentCountries;

	static {
		_sinaCountries = new ArrayList<Country>();

		try {
			Country chinese = new Country("1", "中国");
			_sinaCountries.add(chinese);
			String json = DbHelper.getResource("province.sina.json");
			if (!Utility.isNullOrEmpty(json)) {
				JSONObject obj = new JSONObject(json);
				JSONArray array = obj.getJSONArray("provinces");
				if (array != null && array.length() > 0) {
					for (int i = 0; i < array.length(); i++) {
						JSONObject jp = array.getJSONObject(i);
						Province p = new Province(jp.getInt("id"), jp
								.getString("name"));
						chinese.getProvinces().put(p.getCode(), p);

						JSONArray jcities = jp.getJSONArray("citys");
						if (jcities != null && jcities.length() > 0) {
							for (int j = 0; j < jcities.length(); j++) {
								JSONObject jc = jcities.getJSONObject(j);
								String name = jc.names().getString(0);
								City city = new City(p.getCode(), Integer
										.parseInt(name), jc.getString(name));
								p.getCities().put(city.getCode(), city);
							}
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		_tencentCountries = new ArrayList<Country>();
		// 腾讯的暂时不装载
	}

	public static City getCity(String location, Platform platform) {
		if (platform == Platform.Sina) {
			getSinaCity(location);
		}

		if (Platform.Tencent == platform) {

		}

		return null;
	}

	public static Province getProvinceName(int provinceCode, Platform platform) {

		if (platform == Platform.Sina) {
			return getSinaProvince(provinceCode);
		}

		return Province.Empty();
	}

	public static City getCity(int provinceCode, int cityCode, Platform platform) {

		if (platform == Platform.Sina) {
			return getSinaCity(provinceCode, cityCode);
		} else if (platform == Platform.Tencent) {
			return City.getEmpty();
		}
		return null;
	}

	private static City getSinaCity(int pCode, int cCode) {
		// 得到中国
		Country c = _sinaCountries.get(0);

		Province p = c.getProvinces().get(pCode);

		if (p != null) {
			City city = p.getCities().get(cCode);
			if (city != null) {
				return city;
			}
		}
		return City.getEmpty();
	}

	private static Province getSinaProvince(int pCode) {
		// 得到中国
		Country c = _sinaCountries.get(0);

		Province p = c.getProvinces().get(pCode);
		if (p != null)
			return p;
		else
			return Province.Empty();
	}

	private static City getSinaCity(String location) {
		// 得到中国
		Country c = _sinaCountries.get(0);

		String[] sl = location.split(" ");

		if (sl.length != 2) {
			return City.getEmpty();
		}

		Province p = null;
		for (Map.Entry<Integer, Province> e : c.getProvinces().entrySet()) {
			if (e.getValue().getName().equalsIgnoreCase(sl[0])) {
				p = e.getValue();
				break;
			}
		}

		if (p != null) {
			for (Map.Entry<Integer, City> e : p.getCities().entrySet()) {
				if (e.getValue().getName().equals(sl[1])) {
					return e.getValue();
				}
			}
		}
		return City.getEmpty();
	}
}
