package com.xunku.actions.spreadAnalysis;

import com.xunku.actions.ActionBase;
import com.xunku.app.enums.AnaylisisStatus;
import com.xunku.pojo.base.User;
import com.xunku.utils.AppServerProxy;

// 分析数据后，检测是否分析完毕
public class SpreadAnalysisGetStateAction  extends ActionBase {

	@Override
	public AnaylisisStatus doAction() {
		User user = this.getUser().getBaseUser();
		String id = this.get("id");
		AnaylisisStatus status =  AppServerProxy.getAnaylisisStatus(user, Integer.parseInt(id));
		return status;
	}
}
