package com.xunku.dto.accountMonitor;

/**
 * 帐号监控--数据列表类
 * @author shaoqun
 *
 */
public class AccountListDTO {

	private int id;
	private String accountname;  		//帐号昵称
	private int weibonum;    			//微博数
	private int fansnum;  				//粉丝数
	private int gznum;			    	//关注数 	
	private float weiborate;			//微博原创率		
	private float effectavgnum;			//平均每日微博数
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getAccountname() {
		return accountname;
	}
	public void setAccountname(String accountname) {
		this.accountname = accountname;
	}
	public int getWeibonum() {
		return weibonum;
	}
	public void setWeibonum(int weibonum) {
		this.weibonum = weibonum;
	}
	public int getFansnum() {
		return fansnum;
	}
	public void setFansnum(int fansnum) {
		this.fansnum = fansnum;
	}
	
	public int getGznum() {
		return gznum;
	}
	public void setGznum(int gznum) {
		this.gznum = gznum;
	}
	public float getWeiborate() {
		return weiborate;
	}
	public void setWeiborate(float weiborate) {
		this.weiborate = weiborate;
	}
	public float getEffectavgnum() {
		return effectavgnum;
	}
	public void setEffectavgnum(float effectavgnum) {
		this.effectavgnum = effectavgnum;
	}
	
	
	
	
}
