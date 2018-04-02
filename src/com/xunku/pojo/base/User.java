package com.xunku.pojo.base;

public class User {
    private int id;         //用户编号
    private int customID;   //用户所属的企业
    private String userName;//用户名
    private String nickName;//昵称
    private String token;   //用户密码的票据
    private String email;   //邮箱
    private String tel;     //电话
    private boolean isAdmin;//是否是企业管理员
    private int checkid;    //审核人
    private CustomRole role ;//= new Role();
  
    public User(){
    	this.role = new CustomRole();
    }
    
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public int getCustomID() {
        return customID;
    }
    public void setCustomID(int customID) {
        this.customID = customID;
    }
    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    public String getNickName() {
        return nickName;
    }
    public void setNickName(String nickName) {
        this.nickName = nickName;
    }
    public String getToken() {
        return token;
    }
    public void setToken(String token) {
        this.token = token;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getTel() {
        return tel;
    }
    public void setTel(String tel) {
        this.tel = tel;
    }
    public boolean isAdmin() {
        return isAdmin;
    }
    public void setAdmin(boolean isAdmin) {
        this.isAdmin = isAdmin;
    }
    public CustomRole getRole() {
        return role;
    }
    public void setRole(CustomRole role) {
        this.role = role;
    }

    public int getCheckid() {
        return checkid;
    }

    public void setCheckid(int checkid) {
        this.checkid = checkid;
    }
    
}
