package com.xunku.actions.myTwitter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.xunku.actions.ActionBase;
import com.xunku.app.Utility;
import com.xunku.app.enums.Platform;
import com.xunku.app.interfaces.IAccount;
import com.xunku.app.result.FansResult;
import com.xunku.app.result.GenderResult;
import com.xunku.app.result.VipResult;
import com.xunku.cache.Cache;
import com.xunku.cache.CacheManager;
import com.xunku.constant.PortalCST;
import com.xunku.dto.AccountDTO;
import com.xunku.dto.LineDTO;
import com.xunku.dto.LineDataDTO;
import com.xunku.dto.PieDTO;
import com.xunku.dto.PieDataItemDTO;
import com.xunku.dto.myTwitter.DataAnalyzeFansDTO;
import com.xunku.utils.AppServerProxy;

/*
 * 数据分析模块 粉丝页面 所有图表数据
 * @author sunao
 */
public class DataAnalysisFansAction extends ActionBase {

	@Override
	public DataAnalyzeFansDTO doAction() {
		String ucode = this.get("ucode");
		String _platform = this.get("platform");
		Platform platform = Utility.getPlatform(Integer.parseInt(_platform));
		DataAnalyzeFansDTO fans = new DataAnalyzeFansDTO();
		FansResult result = AppServerProxy.viewFans(ucode, platform);

		// 认证比例
		if (result.getVip() != null) {
			VipResult vipResult = result.getVip();
			double vip = 0.0;
			double noVip = 0.0;
			double count = 0;
			if (result != null) {
				count = vipResult.getVipCnt() + vipResult.getNoVipCnt();
				if (vipResult.getVipCnt() != 0) {
					vip = vipResult.getVipCnt() / count;
					vip = getValue(vip);
				}
				if (vipResult.getNoVipCnt() != 0) {
					noVip = vipResult.getNoVipCnt() / count;
					noVip = getValue(noVip);
				}
			}
			fans.vip = GetexPieDTO(vip, noVip);
		}

		// 用户粉丝数
		if (result.getFollowersNums() != null) {
			int fansArray[] = result.getFollowersNums();
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
			// fix bug by wujian
			if (fansArray != null) {
				for (Integer count : fansArray) {
					
					series.data.add(Double.parseDouble(count.toString()));
				}
				line.series.add(series);
			}
			//return line;
			fans.fans = line;
		}

		// 男女性别比例
		if (result.getGrender() != null) {
			GenderResult grenderResult = result.getGrender();
			double man = 0.0;
			double woman = 0.0;
			double other = 0.0;

			double count = 0;
			if (result != null) {
				count = grenderResult.getFemales() + grenderResult.getMales()
						+ grenderResult.getOthers();
				if (grenderResult.getMales() != 0.0) {
					man = grenderResult.getMales() / count;
					man = getValue(man);
				}
				if (grenderResult.getFemales() != 0.0) {
					woman = grenderResult.getFemales() / count;
					woman = getValue(woman);
				}
				if (grenderResult.getOthers() != 0.0) {
					other = grenderResult.getOthers() / count;
					other = getValue(other);
				}
			}

			fans.sex = GetexPieDTO(man, woman, other);
		}

		// 粉丝互动排行
		if (result.getSupermans() != null) {
			int index = 1;
			List<AccountDTO> lstAccount = new ArrayList<AccountDTO>();
			List<IAccount> map = result.getSupermans();
			for (IAccount account : map) {
				AccountDTO accountDTO = new AccountDTO();
				accountDTO.id = index;
				accountDTO.imgurl = account.getHead();
				accountDTO.name = account.getName();
				accountDTO.weibos = account.getFollowers();
				lstAccount.add(accountDTO);
				index++;
			}
			fans.supermans = lstAccount;
		}

		// 地域排行
		if (result.getLocations() != null) {
			Map<String, Integer> map = result.getLocations();
			LineDTO line = new LineDTO();
			line.title = "地域分析";
			line.tickInterval = 0;
			LineDataDTO series = new LineDataDTO();
			series.name = "地域";
			int index = 0;
			Integer otherCount = 0;
			for (Map.Entry<String, Integer> entry : map.entrySet()) {
				if (index < 19) {
					// 从缓存获取城市信息
					String cityName = "";
					Cache cache = CacheManager
							.getCacheInfo(PortalCST.GetCityCacheKey);
					Map<Integer, String> citys = (HashMap<Integer, String>) cache
							.getValue();
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
				} else {
					otherCount += entry.getValue();
				}
				index++;
			}
			if (index > 19) {
				line.categories.add("其他");
				series.data.add(Double.parseDouble(otherCount.toString()));
			}
			line.series.add(series);
			fans.location = line;
		}

		return fans;
	}

	// 保留4位
	private double getValue(double vipValue) {

		BigDecimal bg = new BigDecimal(vipValue);
		vipValue = bg.setScale(4, BigDecimal.ROUND_HALF_UP).doubleValue();
		return vipValue;
	}

	// 认证比例拼图结构
	private PieDTO GetexPieDTO(double vip, double noVip) {
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

		return pieDto;
	}

	// 男女比例拼图结构
	private PieDTO GetexPieDTO(double man, double woman, double other) {
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

		return pieDto;
	}
}
