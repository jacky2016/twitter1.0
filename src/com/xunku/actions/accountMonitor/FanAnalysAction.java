package com.xunku.actions.accountMonitor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.xunku.actions.ActionBase;
import com.xunku.app.controller.AccountController;
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
import com.xunku.utils.AppServerProxy;

/**
 * 帐号监控--获取粉丝分析信息
 * @author shaoqun
 *
 */
public class FanAnalysAction extends ActionBase{

	@Override
	public Object doAction() {
		// TODO Auto-generated method stub
		
		int method = Integer.parseInt(this.get("method"));
		int id = Integer.parseInt(this.get("id"));
		
		FansResult result = AppServerProxy.MAccountViewFans(id);
		AccountController control = new AccountController();
		PieDTO pieDto = new PieDTO();
		/**1:认证  2：男女 3：用户粉丝数 4：地域分析  5：粉丝最多排行**/
		
		if(method == 1){
			VipResult vip = result.getVip();
			double[] okvip = control.MAccountVip(vip);
			pieDto = control.GetVipPieDTO(okvip[0], okvip[1]); 
			return pieDto;
		}else if(method == 2){
			GenderResult gender = result.getGrender();
			double[] gen = control.MAccountGender(gender);
			pieDto = control.GetexPieDTO(gen[0], gen[1], gen[2]);
			return pieDto;
		}else if(method == 3){
			int[] counts = result.getFollowersNums();
			return control.GetFansColumn(counts);
		}else if(method == 4){
			Map<String, Integer> maps = result.getLocations();	
			return control.GetAreaColumn(maps);
			
		}else if(method == 5){
			List<IAccount> maps = result.getSupermans();
			return control.GetFanslist(maps);
		}
		
		
		return "true";
	}

}
