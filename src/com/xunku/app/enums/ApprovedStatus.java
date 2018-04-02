package com.xunku.app.enums;

/**
 * 审核状态 
 * @author wujian
 * @created on Aug 14, 2014 3:15:56 PM
 */
public interface ApprovedStatus {

	/**
	 * 不需要审核
	 */
	public final static int None = 0;
	/**
	 * 待审核
	 */
	public final static int Pending = 1;
	/**
	 * 审核成功
	 */
	public final static int Sucessed = 2;
	/**
	 * 审核失败
	 */
	public final static int Failed = 3;
	

}
