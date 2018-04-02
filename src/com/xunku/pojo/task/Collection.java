package com.xunku.pojo.task;

import com.xunku.app.interfaces.ITweet;
//微博收藏by wanghui
public class Collection {
    private int id;
    private int groupID;      //分类编号
    private int postId;       //微博id
    private int creator;      //谁收集的
    private String createTime;//什么时候收集的
    private int status;       //0=待处理，1=已处理，2=已忽略，3=已放弃 
    private int processor;    //处理人，这条信息是被谁处理了，NULL为未被任何人处理过
    private ITweet post;        //收藏的微博信息
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public int getCreator() {
        return creator;
    }
    public void setCreator(int creator) {
        this.creator = creator;
    }
    public String getCreateTime() {
        return createTime;
    }
    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
    public int getStatus() {
        return status;
    }
    public void setStatus(int status) {
        this.status = status;
    }
    public int getProcessor() {
        return processor;
    }
    public void setProcessor(int processor) {
        this.processor = processor;
    }

    public int getGroupID() {
        return groupID;
    }
    public void setGroupID(int groupID) {
        this.groupID = groupID;
    }
    public int getPostId() {
        return postId;
    }
    public void setPostId(int postId) {
        this.postId = postId;
    }
    public ITweet getPost() {
        return post;
    }
    public void setPost(ITweet post) {
        this.post = post;
    }
}
