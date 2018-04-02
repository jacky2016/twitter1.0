package com.xunku.pojo.my;

//@我的by wanghui
public class Mention {
    private int id;
    private String tid;
    private String uid;
    private int platform;
    private boolean vip;
    private boolean friend;
    private boolean navies;
    private long created;
    private String author;
    private String text;
    private boolean hasImg;
    private int type;
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getTid() {
        return tid;
    }
    public void setTid(String tid) {
        this.tid = tid;
    }
    public String getUid() {
        return uid;
    }
    public void setUid(String uid) {
        this.uid = uid;
    }
    public int getPlatform() {
        return platform;
    }
    public void setPlatform(int platform) {
        this.platform = platform;
    }

    public boolean isVip() {
        return vip;
    }
    public void setVip(boolean vip) {
        this.vip = vip;
    }
    public boolean isFriend() {
        return friend;
    }
    public void setFriend(boolean friend) {
        this.friend = friend;
    }
    public boolean isNavies() {
        return navies;
    }
    public void setNavies(boolean navies) {
        this.navies = navies;
    }
    public long getCreated() {
        return created;
    }
    public void setCreated(long created) {
        this.created = created;
    }
    public String getAuthor() {
        return author;
    }
    public void setAuthor(String author) {
        this.author = author;
    }
    public String getText() {
        return text;
    }
    public void setText(String text) {
        this.text = text;
    }
    public boolean isHasImg() {
        return hasImg;
    }
    public void setHasImg(boolean hasImg) {
        this.hasImg = hasImg;
    }
    public int getType() {
        return type;
    }
    public void setType(int type) {
        this.type = type;
    }
    
}
