package com.xunku.dto.myTwitter;

import java.util.ArrayList;
import java.util.List;

//发布微博by wanghui
public class SendingDTO {
    private Long sid; //发布Id
    private int userid;   //谁提交的
    private List<Temporary> temporary = new ArrayList<Temporary>(); //博主唯一标示
    private String text;  //提交的内容
    private String submit;//提交的时间
    private long sent;  //发送时间 如果是0则立即发送，否则是按照指定时间的long格式发送
    private String images;//图片列表
    private int approved; //审核状态 0、不需要审核，可直接发送的 1、需要审核，待审核的，不能发送 2、需要审核，审核通过的，可以发送的 3、需要审核，审核失败的，不能发送的
    private int auditor;  //审核人 
    private int type;     //类型 1 原创，2转发，3评论

    public int getUserid() {
        return userid;
    }
    public void setUserid(int userid) {
        this.userid = userid;
    }

    public String getText() {
        return text;
    }
    public void setText(String text) {
        this.text = text;
    }
    public String getSubmit() {
        return submit;
    }
    public void setSubmit(String submit) {
        this.submit = submit;
    }
    public long getSent() {
        return sent;
    }
    public void setSent(long sent) {
        this.sent = sent;
    }
    public String getImages() {
        return images;
    }
    public void setImages(String images) {
        this.images = images;
    }
    public int getApproved() {
        return approved;
    }
    public void setApproved(int approved) {
        this.approved = approved;
    }
    public int getAuditor() {
        return auditor;
    }
    public void setAuditor(int auditor) {
        this.auditor = auditor;
    }
    public int getType() {
        return type;
    }
    public void setType(int type) {
        this.type = type;
    }
    public List<Temporary> getTemporary() {
        return temporary;
    }
    public void setTemporary(List<Temporary> temporary) {
        this.temporary = temporary;
    }
    public Long getSid() {
        return sid;
    }
    public void setSid(Long sid) {
        this.sid = sid;
    }

}
