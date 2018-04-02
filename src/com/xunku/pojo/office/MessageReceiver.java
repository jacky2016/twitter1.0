package com.xunku.pojo.office;

public class MessageReceiver {
    private int id;
    private Message message;//消息编号
    private String receiver;//接收者
    private boolean isHandle;//是否处理，该状态的意义是根据Message的Type状态决定的
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public Message getMessage() {
        return message;
    }
    public void setMessage(Message message) {
        this.message = message;
    }
    public String getReceiver() {
        return receiver;
    }
    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }
    public boolean isHandle() {
        return isHandle;
    }
    public void setHandle(boolean isHandle) {
        this.isHandle = isHandle;
    }
}
