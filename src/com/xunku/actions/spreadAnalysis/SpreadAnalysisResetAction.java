package com.xunku.actions.spreadAnalysis;

import com.xunku.actions.ActionBase;
import com.xunku.pojo.base.User;
import com.xunku.utils.AppServerProxy;

// 重新分析
public class SpreadAnalysisResetAction extends ActionBase {

	@Override
	public Boolean doAction() {
		User user = this.getUser().getBaseUser();
		String monitorid = this.get("monitorid");
		return AppServerProxy.anaylsisSpreadNow(user, Integer.parseInt(monitorid));
	}

}
