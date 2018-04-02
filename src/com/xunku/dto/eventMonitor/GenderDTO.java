package com.xunku.dto.eventMonitor;


/***
 * 事件-用户分析-男女比例
 *
 * @author shaoqun
 * @created Aug 14, 2014
 */
public class GenderDTO {

	private double females;// 女
	private double males;// 男
	private double others;// 其它
	
	private String mname;
	private String wname;
	private String oname;
	
	
	public double getFemales() {
		return females;
	}
	public void setFemales(double females) {
		this.females = females;
	}
	public double getMales() {
		return males;
	}
	public void setMales(double males) {
		this.males = males;
	}
	public double getOthers() {
		return others;
	}
	public void setOthers(double others) {
		this.others = others;
	}
	public void setOthers(int others) {
		this.others = others;
	}
	public String getMname() {
		return mname;
	}
	public void setMname(String mname) {
		this.mname = mname;
	}
	public String getWname() {
		return wname;
	}
	public void setWname(String wname) {
		this.wname = wname;
	}
	public String getOname() {
		return oname;
	}
	public void setOname(String oname) {
		this.oname = oname;
	}
	
}
