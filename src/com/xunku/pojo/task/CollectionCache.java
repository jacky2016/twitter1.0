package com.xunku.pojo.task;

import java.util.List;
//微博收集缓存结构by wanghui
public class CollectionCache {
    private List<Integer> userList;//用户id
    private List<String>  urlList; //微博url
    public List<Integer> getUserList() {
        return userList;
    }
    public void setUserList(List<Integer> userList) {
        this.userList = userList;
    }
    public List<String> getUrlList() {
        return urlList;
    }
    public void setUrlList(List<String> urlList) {
        this.urlList = urlList;
    }
    
}
