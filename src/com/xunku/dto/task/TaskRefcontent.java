package com.xunku.dto.task;
//解析任务搜索的引用原微博内容by wanghui
public class TaskRefcontent {
    private String author; //作者
    private String content;//微博内容
    private int reposts;   //转发数
    private int comment;   //评论数
    private String publishdate;//发布时间
    private String appsource; //来源
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public int getReposts() {
        return reposts;
    }
    public void setReposts(int reposts) {
        this.reposts = reposts;
    }
    public int getComment() {
        return comment;
    }
    public void setComment(int comment) {
        this.comment = comment;
    }
    public String getPublishdate() {
        return publishdate;
    }
    public void setPublishdate(String publishdate) {
        this.publishdate = publishdate;
    }
    public String getAppsource() {
        return appsource;
    }
    public void setAppsource(String appsource) {
        this.appsource = appsource;
    }
    public String getAuthor() {
        return author;
    }
    public void setAuthor(String author) {
        this.author = author;
    }
    
}
