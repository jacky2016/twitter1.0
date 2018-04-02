package com.xunku.dto.myTwitter;

import java.util.List;

public class RegionAnalyzeDTO {

    private int totalCount;               //总数量  
    private List<RegionAnalyze> regionList;//地域分析
    
    public class RegionAnalyze{
	private int count;   //数量
	private String region;//地域
	public int getCount() {
	    return count;
	}
	public void setCount(int count) {
	    this.count = count;
	}
	public String getRegion() {
	    return region;
	}
	public void setRegion(String region) {
	    this.region = region;
	}
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public List<RegionAnalyze> getRegionList() {
        return regionList;
    }

    public void setRegionList(List<RegionAnalyze> regionList) {
        this.regionList = regionList;
    }
}
