package com.xunku.pojo.base;

import java.util.ArrayList;
import java.util.List;
//地域信息by wanghui
public class Province {
    private String province;
    private List<City> cityList = new ArrayList<City>();
    public String getProvince() {
        return province;
    }
    public void setProvince(String province) {
        this.province = province;
    }
    public List<City> getCityList() {
        return cityList;
    }
    public void setCityList(List<City> cityList) {
        this.cityList = cityList;
    }
}
