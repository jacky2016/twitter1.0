package com.xunku.ObjectModel;

import java.util.Map;
//客户级别缓存类by wanghui
public class CustomCache {
    private Map<Integer, Integer> useMap;//用户
    private Map<Integer,Integer> ps; //客户配置信息
    public Map<Integer, Integer> getUseMap() {
        return useMap;
    }

    public void setUseMap(Map<Integer, Integer> useMap) {
        this.useMap = useMap;
    }

    
    public Map<Integer, Integer> getPs() {
        return ps;
    }

    public void setPs(Map<Integer, Integer> ps) {
        this.ps = ps;
    }
}
