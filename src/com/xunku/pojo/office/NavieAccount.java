package com.xunku.pojo.office;

public class NavieAccount {
    private int id;
    private String uid;
    private int platform;
    private String displayName;
    private Navie navie;
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
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
    public String getDisplayName() {
        return displayName;
    }
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
    public Navie getNavie() {
        return navie;
    }
    public void setNavie(Navie navie) {
        this.navie = navie;
    }
}
