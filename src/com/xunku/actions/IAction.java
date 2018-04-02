package com.xunku.actions;

/**
 * 业务处理器接口定义
 * @author wujian
 * @created on Jun 5, 2014 4:03:04 PM
 */
public interface IAction {
	
	public static final String EMPTY_ACTION = "emptyAction";
	public static final String ACTION = "action";
	
	
	//<T> T doAction();这个和下面是一样的...
	Object doAction();
	
	ActionContext getContext();

	void init(ActionContext context);

	String getName();
}
