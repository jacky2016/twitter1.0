package com.xunku.app.result;

import com.xunku.app.result.spread.RetweetStatisResult;
import com.xunku.app.result.spread.RetweetLevelResult;
import com.xunku.app.result.spread.UserResult;

/**
 * 传播分析结果
 * 
 * @author wujian
 * @created on Jul 16, 2014 3:56:32 PM
 */
public class SpreadResult extends Result {

	RetweetStatisResult keyUserResult;
	RetweetLevelResult retweetLevelResult;
	TrendAccountResult trendResult;
	UserResult userResult;
}
