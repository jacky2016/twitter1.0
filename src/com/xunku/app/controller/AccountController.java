package com.xunku.app.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.xunku.app.interfaces.IAccount;
import com.xunku.app.model.App;
import com.xunku.app.model.AppAuth;
import com.xunku.app.result.GenderResult;
import com.xunku.app.result.VipResult;
import com.xunku.cache.Cache;
import com.xunku.cache.CacheManager;
import com.xunku.constant.PortalCST;
import com.xunku.dao.base.AccountDao;
import com.xunku.daoImpl.base.AccountDaoImpl;
import com.xunku.dto.AccountDTO;
import com.xunku.dto.LineDTO;
import com.xunku.dto.LineDataDTO;
import com.xunku.dto.PieDTO;
import com.xunku.dto.PieDataItemDTO;
import com.xunku.dto.myTwitter.DataAnalyzeFansDTO;
import com.xunku.pojo.base.AccountInfo;

public class AccountController {

	public AppAuth getAuth(AccountInfo info, App app) {
		return null;
	}

	public void updateFansLoaded(int accid) {
		AccountDao dao = new AccountDaoImpl();
		dao.updateFansLoaded(accid);

	}

	/********************男女比例业务***********************************/
	//保留2位  Author shaoqun
	public double getValue(double vipValue) {

		BigDecimal bg = new BigDecimal(vipValue);
		vipValue = bg.setScale(4, BigDecimal.ROUND_HALF_UP).doubleValue();
		return vipValue;
	}

	//男女比例拼图结构  Author shaoqun
	public PieDTO GetexPieDTO(double man, double woman, double other) {
		PieDTO pieDto = new PieDTO();
		pieDto.title = "";
		PieDataItemDTO nEntity = new PieDataItemDTO();
		nEntity.name = "男";
		nEntity.y = man;

		PieDataItemDTO vEntity = new PieDataItemDTO();
		vEntity.name = "女";
		vEntity.y = woman;

		PieDataItemDTO oEntity = new PieDataItemDTO();
		oEntity.name = "其他";
		oEntity.y = other;

		pieDto.series.data.add(nEntity);
		pieDto.series.data.add(vEntity);
		pieDto.series.data.add(oEntity);
		if(man != 0 || woman != 0 || other != 0){
			pieDto.test = 1;
		}

		return pieDto;
	}
	
	//转换男女其他比例 Author shaoqun
	public double[] MAccountGender(GenderResult gender){
		
		double[] result = new double[3];
		// 计算比例
		double man = 0.0;
		double woman = 0.0;
		double other = 0.0;

		double count = 0;
		if (result != null) {
			count = gender.getFemales() + gender.getMales()
					+ gender.getOthers();
			if (gender.getMales() != 0.0) {
				man = gender.getMales() / count;
				result[0] = getValue(man);
			}
			if (gender.getFemales() != 0.0) {
				woman = gender.getFemales() / count;
				result[1] = getValue(woman);
			}
			if (gender.getOthers() != 0.0) {
				other = gender.getOthers() / count;
				result[2] = getValue(other);
			}
		}
		
		return result;
	}
	
	
	// 粉丝统计 认证比例拼图结构 Author shaoqun
	public PieDTO GetVipPieDTO(double vip, double noVip) {
		PieDTO pieDto = new PieDTO();
		pieDto.title = "";
		PieDataItemDTO nEntity = new PieDataItemDTO();
		nEntity.name = "认证用户";
		nEntity.y = vip;

		PieDataItemDTO vEntity = new PieDataItemDTO();
		vEntity.name = "普通用户";
		vEntity.y = noVip;

		pieDto.series.data.add(nEntity);
		pieDto.series.data.add(vEntity);
		if(vip != 0 || noVip !=0){
			pieDto.test = 1;
		}
		return pieDto;
	}
	
	//转换认证比例 Author shaoqun
	public double[] MAccountVip(VipResult viprest){
		double[] result = new double[2];
		double vip = 0.0;
		double noVip = 0.0;
		double count = 0;
		if (viprest != null) {
			count = viprest.getVipCnt() + viprest.getNoVipCnt();
			if (viprest.getVipCnt() != 0) {
				vip = viprest.getVipCnt() / count;
				result[0] = getValue(vip);
			}
			if (viprest.getNoVipCnt() != 0) {
				noVip = viprest.getNoVipCnt() / count;
				result[1] = getValue(noVip);
			}
		}
		return result;
	}
	
	//地域分析转换 Author shaoqun
	public LineDTO GetAreaColumn(Map<String, Integer> maps){
		// 组织结构
		LineDTO line = new LineDTO();
		line.title = "地域分析";
		line.tickInterval = 0;
		LineDataDTO series = new LineDataDTO();
		series.name = "地域";
		if (maps != null) {
			int index = 0;
			Integer otherCount = 0;
			for (Map.Entry<String, Integer> entry : maps.entrySet()) {
				if (index < 19) {
					// 从缓存获取城市信息
					String cityName = "";
					Cache cache = CacheManager.getCacheInfo(PortalCST.GetCityCacheKey);
					Map<Integer, String> citys = (HashMap<Integer, String>) cache.getValue();
					if (citys.containsKey(entry.getKey())) {
						cityName = citys.get(entry.getKey());
					}
					if (!cityName.equals("")) {
						line.categories.add(cityName);
					} else {
						line.categories.add(entry.getKey());
					}
					series.data.add(Double.parseDouble(entry.getValue()
							.toString()));
					if(entry.getValue() != 0){
						line.test = 1;
					}
				} else {
					otherCount += entry.getValue();
					if(otherCount != 0){
						line.test = 1;
					}
				}
				index++;
			}
			if (index > 19) {
				line.categories.add("其他");
				series.data.add(Double.parseDouble(otherCount.toString()));
			}
		}
		line.series.add(series);
		return line;
		
	}
	
	
	//用户粉丝数转换
	public LineDTO GetFansColumn(int[] counts){
		// 组织结构
		LineDTO line = new LineDTO();
		line.categories.add("0-100");
		line.categories.add("100-1千");
		line.categories.add("1千-1万");
		line.categories.add("1万-10万");
		line.categories.add("10万-100万");
		line.title = "用户粉丝数分析";
		line.tickInterval = 0;
		LineDataDTO series = new LineDataDTO();
		series.name = "粉丝";
		if (counts != null) {
			for (Integer count : counts) {
				series.data.add(Double.parseDouble(count.toString()));
				if(count != 0){
					line.test = 1;
				}
			}
			line.series.add(series);
		}
		return line;
	}
	
	//粉丝最多用户排行转换
	public DataAnalyzeFansDTO GetFanslist(List<IAccount> maps){
		// 组织结构
		int index = 1;
		DataAnalyzeFansDTO fans = new DataAnalyzeFansDTO();
		List<AccountDTO> lstAccount = new ArrayList<AccountDTO>();
		
		for (IAccount account : maps) {
			AccountDTO accountDTO = new AccountDTO();
			accountDTO.id = index;
			accountDTO.imgurl = account.getHead();
			accountDTO.name = account.getName();
			accountDTO.weibos = account.getFollowers();
			lstAccount.add(accountDTO);
			index++;
		}
		fans.supermans = lstAccount;
		return fans;
	}
}
