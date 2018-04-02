package com.xunku.constant;

/**
 * 
 * @author wujian
 * @created on Aug 14, 2014 3:01:06 PM
 */
public enum SendStatusEnum {
    /**
     * 我发布的，只显示我发布成功的/和失败的
     */
    MySend,       //我的发布
    /**
     * 待我审核的，只显示需要我审核的
     */
    ApprovedToMe, //待我审核
    /**
     * 定时发布的，只显示审核过的还没有发布的，企业内部的所有人的
     */
    AllSendIng,   //定时发布
    /**
     * 所有已经发布的，企业内部所有已经发布成功的微博、评论
     */
    AllSend,      //所有已发布
    /**
     * 审核失败，我提交的微博\评论审核失败了的
     */
    ApprovedFail  //审核失败
}
