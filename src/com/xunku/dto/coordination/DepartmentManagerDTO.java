package com.xunku.dto.coordination;

public class DepartmentManagerDTO {

	/**
	 * 协同办公--机构管理DTO
	 * @author shangwei
	 *
	 */
	public  int id;
    public  String  departmentName;//机构名称
    public  int  weiboNum;//转发我的数量
    public  int  toMeNum;//评论我的数量  
    public  String orguid;// 机构账号(也就是机构的uid)
    
    public int  weiboType;//微博平台
}
