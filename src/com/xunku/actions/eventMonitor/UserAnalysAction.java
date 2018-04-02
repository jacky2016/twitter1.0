package com.xunku.actions.eventMonitor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.xunku.actions.ActionBase;
import com.xunku.app.enums.EventMonitorEnum;
import com.xunku.app.enums.Platform;
import com.xunku.app.interfaces.IAccount;
import com.xunku.app.monitor.EventMonitor;
import com.xunku.app.result.GenderResult;
import com.xunku.app.result.VipResult;
import com.xunku.app.result.event.MEventUserRegResult;
import com.xunku.cache.Cache;
import com.xunku.cache.CacheManager;
import com.xunku.constant.PortalCST;
import com.xunku.dao.event.DataAnalysisDao;
import com.xunku.daoImpl.event.DataAnalysisDaoImpl;
import com.xunku.daoImpl.event.EventDaoImpl;
import com.xunku.dto.LineDTO;
import com.xunku.dto.LineDataDTO;
import com.xunku.dto.PieDTO;
import com.xunku.dto.PieDataItemDTO;
import com.xunku.dto.eventMonitor.GenderDTO;
import com.xunku.dto.eventMonitor.ProvCityDTO;
import com.xunku.dto.eventMonitor.UserAnalytListDTO;
import com.xunku.pojo.base.City;
import com.xunku.pojo.base.User;
import com.xunku.utils.AppServerProxy;
import com.xunku.utils.Pagefile;
/**
 * 事件监控--用户分析信息
 * @author shaoqun
 *
 */
public class UserAnalysAction extends ActionBase {
	
		PieDTO pieDto = new PieDTO();
		
	@Override
	@SuppressWarnings("unused")
	public Object doAction() {
		
		Pagefile<UserAnalytListDTO> mtfile = new Pagefile<UserAnalytListDTO>();
		DataAnalysisDao analyDAO = new DataAnalysisDaoImpl();
		
		String charType = this.get("charType");
		int eid = Integer.parseInt(this.get("eid"));
		EventMonitor event = new EventDaoImpl().queryEventByEId(eid);
		String str1 = "", str2 = "",str3 = "";

		if(charType.equals(EventMonitorEnum.User_scale.toString())){			//男女认证比例
			
			int _males = 0,_females = 0,_other = 0;
			GenderResult gender = null;
			try {
				gender = AppServerProxy.viewGrender(event);
			} catch (RuntimeException e) {
				e.printStackTrace();
			}
			GenderDTO dto = null;
			if(gender != null){
				dto = new GenderDTO();
				dto.setMales(gender.getMales());
				dto.setFemales(gender.getFemales());
				dto.setOthers(gender.getOthers());
				dto.setMname("男");
				dto.setWname("女");
				dto.setOname("其他");
			}
			return getPieDTO(dto);
			
		}else if(charType.equals(EventMonitorEnum.Renz_scale.toString())){	//认证比例统计图
			
			int vip = 0,novip = 0;
			VipResult vipst = null;
			try {
				vipst = AppServerProxy.viewVip(event);
			} catch (RuntimeException e) {
				e.printStackTrace();
			}
			if(vipst != null){
				vip = vipst.getVipCnt();
				novip = vipst.getNoVipCnt();
			}
			
			return getPieDTOVip(vip, novip);
			
		}else if(charType.equals(EventMonitorEnum.Source_spread.toString())){ //来源分布统计图

			Map<String, Integer> map = null;
			try {
				map = AppServerProxy.viewForms(event);
			} catch (RuntimeException e) {
				e.printStackTrace();
			}
		
			return getPieDTOSource(map);

		}else if(charType.equals(EventMonitorEnum.Enroll_spread.toString())){ //注册时间分布统计图
			
			MEventUserRegResult mevent = null;
			try {
				mevent = AppServerProxy.viewReg(event);
			} catch (RuntimeException e) {
				e.printStackTrace();
			}
			if(mevent != null){
				
			}
			
			return getPieDTOReg(mevent);
			
		}else if(charType.equals(EventMonitorEnum.Terrain_analy.toString())){     //地域分析统计图

			//TODO 貌似还有问题
			Map<String, Integer> map = null;
			try {
				map = AppServerProxy.viewLocation(event);
			} catch (RuntimeException e) {
				e.printStackTrace();
			}
			
			return getColumnDate(map);
			
		}else if(charType.equals(EventMonitorEnum.KeyuserList.toString())){     //关键用户列表 top10 

			int type = Integer.parseInt(this.get("type"));
			Platform fiter = Platform.Sina;
			if(type == 1){
				fiter = Platform.Tencent;
			}else if(type == 2){
				fiter = Platform.Renmin;
			}
			User user = this.getUser().getBaseUser();
			
			Map<IAccount, Integer> map = null;
			try {
				map = AppServerProxy.viewSuperman(event);
			} catch (RuntimeException e) {
				e.printStackTrace();
			}
			
			//博主帐号信息
			for(IAccount acc : map.keySet()){
				
				UserAnalytListDTO ht = new UserAnalytListDTO();
				
				if(acc != null){
					if (acc.getPlatform() == Platform.Tencent && acc.getHead() != null) {		
						ht.imageHead = PortalCST.IMAGE_PATH_PERFIX + acc.getHead();
						ht.account.imgurlBig = PortalCST.IMAGE_PATH_PERFIX + acc.getHead();
					}else{
						ht.imageHead = acc.getHead();
						ht.account.imgurlBig = acc.getHead();
					}
					if(acc.getPlatform() == Platform.Tencent){
						ht.account.twitterType = 2;
					}else{
						ht.account.twitterType = 1;
					}
					ht.account.name = acc.getName();			 //名称
					ht.account.ucode = acc.getUcode();
					ht.account.url = acc.getHomeUrl();
					ht.account.weibos = acc.getWeibos(); 		 //微博数
					ht.account.followers =  acc.getFollowers(); //粉丝数
					ht.account.friends = acc.getFriends();	 	 //关注数
					ht.account.summany = acc.getDescription();	 //简介
					ht.account.isAjax = true;
					
					ht.nickname = acc.getDisplayName();
	
					if(acc.isVerified()){			//认证
						ht.authentication = "已认证";
					}else{
						ht.authentication = "未认证";
					}
					ht.fansnum = acc.getFollowers();
					ht.area = acc.getLocation();
					
					mtfile.getRows().add(ht);
				}
			}
			Collections.sort(mtfile.getRows(),new compareKey<UserAnalytListDTO>());
			mtfile.setRealcount(map.size());

			return mtfile;
		}
		
		return null;
	}

	private class compareKey<T extends UserAnalytListDTO> implements
		Comparator<T> {
	
		@Override
		public int compare(UserAnalytListDTO o1, UserAnalytListDTO o2) {
			return o2.fansnum - o1.fansnum;
		}
	}
	
	//男女比例
	private PieDTO getPieDTO(GenderDTO dto) {

		if(dto != null){
			double count = dto.getMales()+dto.getFemales()+dto.getOthers();
			double mancount = dto.getMales()/count,
			   	   wmancount = dto.getFemales()/count,
				   othercount = dto.getOthers()/count;
			BigDecimal _d1 = new BigDecimal(mancount);  
			BigDecimal _d2 = new BigDecimal(wmancount); 
			BigDecimal _d3 = new BigDecimal(othercount); 
			pieDto.title = "";
			PieDataItemDTO v1 = new PieDataItemDTO();
			v1.name = dto.getMname();
			v1.y = _d1.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();//保留三位小数
	
			PieDataItemDTO v2 = new PieDataItemDTO();
			v2.name = dto.getWname();
			v2.y = _d2.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();//保留三位小数 
			
			PieDataItemDTO v3 = new PieDataItemDTO();
			v3.name = dto.getOname();
			v3.y = _d3.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();//保留三位小数 
			
			pieDto.series.data.add(v1);
			pieDto.series.data.add(v2);
			pieDto.series.data.add(v3);
		}else{
			pieDto.test = -2;
		}
		return pieDto;
	}
	
	//认证比例 
	private PieDTO getPieDTOVip(int d1,int d2){
		
		if((d1+d2) != 0){
			double vipcount = (double)d1/(double)(d1+d2),
				   novipcount = (double)d2/(double)(d1+d2);
			BigDecimal _d1 = new BigDecimal(vipcount);  
			BigDecimal _d2 = new BigDecimal(novipcount); 
			pieDto.title = "";
			PieDataItemDTO v1 = new PieDataItemDTO();
			v1.name = "认证用户";
			v1.y = _d1.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();//保留三位小数
			
			PieDataItemDTO v2 = new PieDataItemDTO();
			v2.name = "普通用户";
			v2.y = _d2.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();//保留三位小数
	
			pieDto.series.data.add(v1);
			pieDto.series.data.add(v2);
		}else{
			pieDto.test = -2;
		}
		return pieDto;
	}
	
	//来源分布
	private PieDTO getPieDTOSource(Map<String,Integer> maps){
		
		if(maps != null){
			if(maps.size() != 0){
				Iterator<Entry<String, Integer>> iter = maps.entrySet().iterator();
				while (iter.hasNext()) {
					Entry<String, Integer> en = iter.next();
					PieDataItemDTO cdto = new PieDataItemDTO();
					cdto.name = en.getKey();
					cdto.y = en.getValue();
					pieDto.series.data.add(cdto);
				}
			}else{
				pieDto.test = -2;
			}
		}else{
			pieDto.test = -2;
		}
		return pieDto;
	}
	//注册时间分布
	private PieDTO getPieDTOReg(MEventUserRegResult reg){
		
		if(reg != null){
			int count = reg.getHalfYears()+reg.getOneYears()+reg.getTwoYears()+reg.getMoreYears();
			double halfcount = (double)reg.getHalfYears()/(double)(count),
				   onecount = (double)reg.getOneYears()/(double)(count),
				   twocount = (double)reg.getTwoYears()/(double)(count),
				   morecount = (double)reg.getMoreYears()/(double)(count);
			
			BigDecimal _d1 = new BigDecimal(halfcount);  
			BigDecimal _d2 = new BigDecimal(onecount); 
			BigDecimal _d3 = new BigDecimal(twocount); 
			BigDecimal _d4 = new BigDecimal(morecount); 
			
			pieDto.title = "";
			PieDataItemDTO v1 = new PieDataItemDTO();
			v1.name = "半年以内";
			v1.y = _d1.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
			
			PieDataItemDTO v2 = new PieDataItemDTO();
			v2.name = "半年-1年";
			v2.y = _d2.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
			
			PieDataItemDTO v3 = new PieDataItemDTO();
			v3.name = "1-2年";
			v3.y = _d3.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
			
			PieDataItemDTO v4 = new PieDataItemDTO();
			v4.name = "2年以上";
			v4.y = _d4.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
			
			pieDto.series.data.add(v1);
			pieDto.series.data.add(v2);
			pieDto.series.data.add(v3);
			pieDto.series.data.add(v4);
		}else{
			pieDto.test = -2;
		}
		
		return pieDto;
	}
	
	//组织成图表Line对象(柱状图)
	private LineDTO getColumnDate(Map<String,Integer> maps) {
		
		LineDTO column = new LineDTO();
		LineDataDTO dataDTO = new LineDataDTO();
		dataDTO.name = "发布";				// 添加顶部标题
		column.title = "地域分布统计查询";
		
		if(maps != null){
			if(maps.size() != 0){
				Iterator<Entry<String, Integer>> iter = maps.entrySet().iterator();
				int code=0,total=0,value=0;
				String city="";double bili=0;
				
				while (iter.hasNext()) {
					Entry<String, Integer> en = iter.next();
					city = en.getKey();
					value = en.getValue();
					column.categories.add(city);
					dataDTO.data.add(Double.valueOf(value));
					System.out.println(city+"----------"+value);
				}
				
//				for(EventLocal local : list){
//					
//					code = local.getCity();
//					total = local.getTotal();
//					value = local.getValue();
//					ProvCityDTO pc = getProvCity(code);
//					city = pc.city;
//					column.categories.add(city);			// 添加x坐标数据
//					
//					bili = (float)value/(total)*100;		
//					dataDTO.data.add(Double.valueOf(bili));	// 添加y坐标数据
//				}
				column.series.add(dataDTO);
			}else{
				column.test = -2;
			}
		}else{
			column.test = -2;
		}
		return column;
	}
	
	//获取省 城市
	private ProvCityDTO getProvCity(int code){
		
		Cache cache = CacheManager.getCacheInfo(PortalCST.BaseCityCacheKey);
		Map<String,List<City>> map = (HashMap<String, List<City>>) cache.getValue();
		
		Iterator iter = map.keySet().iterator();
		List<City> list = new ArrayList<City>();
		String prov="",city="";
		int code_2 = 0;
		
		while(iter.hasNext()){
			prov= (String) iter.next(); 
			list = (List<City>)map.get(prov);

			for(City c : list){
				
				code_2 = c.getCode();
				if(code== code_2){
					city = c.getCity();
					break;
				}
			}
			if(code == code_2){
				break;
			}else{
				prov = "";
			}
		}
		ProvCityDTO pc = new ProvCityDTO();
		pc.prov = prov;
		pc.city = city;
		return pc;
	}
}
