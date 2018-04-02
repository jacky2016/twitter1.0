package com.xunku.app.result;
/**
 * 功能描述：网评员统计，网评员，网评员账号的个数
 * 总转评到我的个数 
 * @author wanghui
 */
public class NavieCountResult {
    private int customid;   //客户编号
    private int navieid;    //网评员id
    private int uids;       //网评员账号的个数
    private int total;      //总转评到我的个数
    
    public int getUids() {
        return uids;
    }
    public void setUids(int uids) {
        this.uids = uids;
    }
    public int getTotal() {
        return total;
    }
    public void setTotal(int total) {
        this.total = total;
    }
    public int getCustomid() {
        return customid;
    }
    public void setCustomid(int customid) {
        this.customid = customid;
    }
    public int getNavieid() {
        return navieid;
    }
    public void setNavieid(int navieid) {
        this.navieid = navieid;
    }
    
}
