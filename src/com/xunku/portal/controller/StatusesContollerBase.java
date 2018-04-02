package com.xunku.portal.controller;



import com.xunku.pojo.base.User;
/**
 * 三大微博的基类
 * @author wanghui
 * @version twitter1.0
 */
public abstract class StatusesContollerBase {
    /**
     * 功能描述<获得用户的粉丝消息>
     * @author wanghui
     * @param  user<博主> 	
     * @return String
     * @see 微博API
     * @version twitter 1.0
     * @date Mar 31, 2014
     */
    public abstract String messageGet(User user);
    /**
     * 功能描述<获得用户最新微博>
     * @author wanghui
     * @param  user<博主>,wbId<微博>	
     * @return String
     * @see 微博API
     * @version twitter 1.0
     * @date Mar 31, 2014
     */
    public abstract String weiboGetNewest(User user,String wbId);
    /**
     * 功能描述<获得用户发布的微博>
     * @author wanghui
     * @param  user<博主>,statuses<微博列表>	
     * @return String
     * @see 微博API
     * @version twitter 1.0
     * @date Mar 31, 2014
     */
    public abstract String weiboGetPost(User user);
    /**
     * 功能描述<获得微博的转发微博>
     * @author wanghui
     * @param  user<博主>,wbId<微博>,statuses<微博列表>	
     * @return String
     * @see 微博API
     * @version twitter 1.0
     * @date Mar 31, 2014
     */
    public abstract String weiboGetRepost(User user,String wbId);
    /**
     * 功能描述<获得@我的微博>
     * @author wanghui
     * @param  user<博主>,infos<微博列表>		
     * @return String
     * @see 微博API
     * @version twitter 1.0
     * @date Mar 31, 2014
     */
    public abstract String weiboGetAt(User user);
    /**
     * 功能描述<获得一条微博信息>
     * @author wanghui
     * @param  user<博主>,wbId<微博>	
     * @return String
     * @see 微博API
     * @version twitter 1.0
     * @date Mar 31, 2014
     */
    public abstract String weiboGetStatus(User user,String wbId);
    /**
     * 功能描述<获得微博的转发数和回复数>
     * @author wanghui
     * @param  user<博主>,wbId<微博>	
     * @return String
     * @see 微博API
     * @version twitter 1.0
     * @date Mar 31, 2014
     */
    public abstract String weiboGetRRCount(User user,String wbId);
    /**
     * 功能描述<转发微博>
     * @author wanghui
     * @param  user<博主>,type<回复类型>,wbId<微博>	
     * @return String
     * @see 微博API
     * @version twitter 1.0
     * @date Mar 31, 2014
     */
    public abstract String weiboRepost(User user,String wbId);
    /**
     * 功能描述<删除微博>
     * @author wanghui
     * @param  user<博主>,wbId<微博>	
     * @return String
     * @see 微博API
     * @version twitter 1.0
     * @date Mar 31, 2014
     */
    public abstract String weiboRemove(User user,String wbId);
    /**
     * 功能描述<发布微博>
     * @author wanghui
     * @param  user<博主>,status<微博>	
     * @return String
     * @see 微博API
     * @version twitter 1.0
     * @date Mar 31, 2014
     */
    public abstract String weiboPost(User user,String status);
    /**
     * 功能描述<获得微博的评论列表>
     * @author wanghui
     * @param  user<博主>,wbId<微博>		
     * @return String
     * @see 微博API
     * @version twitter 1.0
     * @date Mar 31, 2014
     */
    public abstract String weiboGetCommentList(User user,String wbId);
    /**
     * 功能描述<获得我发出的评论>
     * @author wanghui
     * @param  user<博主>	
     * @return String
     * @see 微博API
     * @version twitter 1.0
     * @date Mar 31, 2014
     */
    public abstract String weiboGetMyCommentList(User user);
    /**
     * 功能描述<获取用户发送及收到的评论列表>
     * @author wanghui
     * @param  user<博主>	
     * @return String
     * @see 微博API
     * @version twitter 1.0
     * @date Mar 31, 2014
     */
    public abstract String weiboGetCommentMineList(User user);
    /**
     * 功能描述<获取@到我的评论>
     * @author wanghui
     * @param  user<博主>	
     * @return String
     * @see 微博API
     * @version twitter 1.0
     * @date Mar 31, 2014
     */
    public abstract String weiboGetAtMe(User user);
    /**
     * 功能描述<评论微博>
     * @author wanghui
     * @param user<博主>,wbId<微博>,text<评论内容>	
     * @return String
     * @see 微博API
     * @version twitter 1.0
     * @date Mar 31, 2014
     */
    public abstract String weiboPostComment(User user,String wbId,String text);
    /**
     * 功能描述<删除评论>
     * @author wanghui
     * @param  user<博主>,wbId<微博>	
     * @return String
     * @see 微博API
     * @version twitter 1.0
     * @date Mar 31, 2014
     */
    public abstract String weiboRemoveComment(User user,String wbId);
    /**
     * 功能描述<回复评论>
     * @author wanghui
     * @param  user<博主>,wbId<微博>,text<评论内容>	
     * @return void
     * @see 微博API
     * @version twitter 1.0
     * @date Mar 31, 2014
     */
    public abstract String weiboReplyComment(User user,String text,String weiboId,String cid);
    /**
     * 功能描述<获得用户基本信息>
     * @author wanghui
     * @param  user<博主>	
     * @return String
     * @see 微博API
     * @version twitter 1.0
     * @date Mar 31, 2014
     */
    public abstract String weiboGetUserInfo(User user,String uid);
    /**
     * 功能描述<批量获得粉丝数/关注数/微博数>
     * @author wanghui
     * @param  user<博主>	
     * @return String
     * @see 微博API
     * @version twitter 1.0
     * @date Mar 31, 2014
     */
    public abstract String weiboGetSummary(User user,String ids);
    /**
     * 功能描述<获得用户关注列表>
     * @author wanghui
     * @param  user<博主>	
     * @return String
     * @see 微博API
     * @version twitter 1.0
     * @date Mar 31, 2014
     */
    public abstract String weiboGetFriendList(User user,String uid);
    /**
     * 功能描述<获得用户粉丝列表>
     * @author wanghui
     * @param  user<博主>	
     * @return String
     * @see 微博API
     * @version twitter 1.0
     * @date Mar 31, 2014
     */
    public abstract String weiboGetFollowerList(User user,String screen_name);
    /**
     * 功能描述<关注微博用户>
     * @author wanghui
     * @param  user<博主>,friendIds<关注用户列表>	
     * @return String
     * @see 微博API
     * @version twitter 1.0
     * @date Mar 31, 2014
     */
    public abstract String weiboFriendCreate(User user,String friendIds);
    /**
     * 功能描述<取消关注微博用户>
     * @author wanghui
     * @param  user<博主>,friendId<关注de用户>		
     * @return void
     * @see 微博API
     * @version twitter 1.0
     * @date Mar 31, 2014
     */
    public abstract void weiboFriendRemove(User user,String friendId);
    
}
