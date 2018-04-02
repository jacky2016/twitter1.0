package com.xunku.portal.controller;

import java.util.List;

import weibo4j.Comments;
import weibo4j.Friendships;
import weibo4j.Timeline;
import weibo4j.Users;
import weibo4j.model.Comment;
import weibo4j.model.CommentWapper;
import weibo4j.model.Status;
import weibo4j.model.StatusWapper;
import weibo4j.model.UserWapper;
import weibo4j.model.WeiboException;
import weibo4j.org.json.JSONArray;

import com.google.gson.Gson;
import com.xunku.pojo.base.User;
/**
 * 新浪微博api的实现
 * @author wanghui
 * @version twitter1.0
 */
public class WeiboController extends StatusesContollerBase{
    private static String access_token = "2.006OtBjCbZB5CE28d6e284082HuKQE";
    @Override
    public String messageGet(User user) {
	Friendships fm = new Friendships();
	fm.client.setToken(access_token);

	return null;
    }
    
    @Override
    public String weiboFriendCreate(User user, String friendIds) {
	Friendships fm = new Friendships();
	fm.client.setToken(access_token);
	try {
		fm.createFriendshipsById(String.valueOf(friendIds));
	    } catch (WeiboException e) {
		e.printStackTrace();
	    }
	return null;
    }

    @Override
    public void weiboFriendRemove(User user, String friendId) {
	String access_token = null;//user.gatAuthStrings()[0];
	Friendships fm = new Friendships();
	fm.client.setToken(access_token);
	try {
	    fm.destroyFriendshipsDestroyById(String.valueOf(friendId));
	} catch (WeiboException e) {
	    e.printStackTrace();
	}
    }

    @Override
    public String weiboGetAt(User user) {
	Timeline tm = new Timeline();
	tm.client.setToken(access_token);
	String result = null;
	try {
	    StatusWapper status = tm.getMentions();
	    List<Status> sList = status.getStatuses();
	    java.lang.reflect.Type type = new com.google.gson.reflect.TypeToken<List<Status>>(){}.getType();
	    result = new Gson().toJson(sList,type);
	} catch (WeiboException e) {
	    e.printStackTrace();
	}
	return result;
    }

    @Override
    public String weiboGetAtMe(User user) {
	Comments cm = new Comments();
	cm.client.setToken(access_token);
	String result = null;
	try {
	    CommentWapper comment = cm.getCommentToMe();
	    List<Comment> comments = comment.getComments();
	    java.lang.reflect.Type type = new com.google.gson.reflect.TypeToken<List<Comment>>(){}.getType();
	    result = new Gson().toJson(comments,type);
	} catch (WeiboException e) {
	    e.printStackTrace();
	}
	return result;
    }

    @Override
    public String weiboGetCommentList(User user, String wbId) {
	Comments cm =new Comments();
	cm.client.setToken(access_token);
	String result = null;
	try {
	    CommentWapper comment = cm.getCommentById(wbId);
	    List<Comment> comments = comment.getComments();
	    java.lang.reflect.Type type = new com.google.gson.reflect.TypeToken<List<Comment>>(){}.getType();
	    result = new Gson().toJson(comments,type);
	} catch (WeiboException e) {
	    e.printStackTrace();
	}
	return result;
    }

    @Override
    public String weiboGetCommentMineList(User user) {
	Comments cm = new Comments();
	cm.client.setToken(access_token);
	String result = null;
	try {
	    CommentWapper comment = cm.getCommentTimeline();
	    List<Comment> comments = comment.getComments();
	    java.lang.reflect.Type type = new com.google.gson.reflect.TypeToken<List<Comment>>(){}.getType();
	    result = new Gson().toJson(comments,type);
	} catch (WeiboException e) {
	    e.printStackTrace();
	}
	return result;
    }

    @Override
    public String weiboGetFollowerList(User user,String screen_name) {
	Friendships fm = new Friendships();
	fm.client.setToken(access_token);
	String result = null;
	try {
	    UserWapper wapper = fm.getFollowersByName(screen_name);
	    List<weibo4j.model.User> users = wapper.getUsers();
	    java.lang.reflect.Type type = new com.google.gson.reflect.TypeToken<List<weibo4j.model.User>>(){}.getType();
	    result = new Gson().toJson(users,type);
	} catch (WeiboException e) {
	    e.printStackTrace();
	}
	return result;
    }

    @Override
    public String weiboGetFriendList(User user,String uid) {
	Friendships fm = new Friendships();
	fm.client.setToken(access_token);
	String result = null;
	try {
	    UserWapper wapper = fm.getFriendsByID(uid);
	    List<weibo4j.model.User> users = wapper.getUsers();
	    java.lang.reflect.Type type = new com.google.gson.reflect.TypeToken<List<weibo4j.model.User>>(){}.getType();
	    result = new Gson().toJson(users,type);
	} catch (WeiboException e) {
	    e.printStackTrace();
	}
	return result;
    }

    @Override
    public String weiboGetMyCommentList(User user) {
	Comments cm = new Comments();
	cm.client.setToken(access_token);
	String result = null;
	try {
	    CommentWapper comment = cm.getCommentByMe();
	    List<Comment> comments = comment.getComments();
	    java.lang.reflect.Type type = new com.google.gson.reflect.TypeToken<List<Comment>>(){}.getType();
	    result = new Gson().toJson(comments,type);
	} catch (WeiboException e) {
	    e.printStackTrace();
	}
	return result;
    }

    @Override
    public String weiboGetNewest(User user, String wbId) {
	Timeline tm = new Timeline();
	tm.client.setToken(access_token);
	String result = null;
	try {
	    StatusWapper wapper = tm.getHomeTimeline();
	    List<Status> sList = wapper.getStatuses();
	    java.lang.reflect.Type type = new com.google.gson.reflect.TypeToken<List<Status>>(){}.getType();
	    result = new Gson().toJson(sList,type);
	} catch (WeiboException e) {
	    e.printStackTrace();
	}
	return result;
    }

    @Override
    public String weiboGetPost(User user) {
	Timeline tm = new Timeline();
	tm.client.setToken(access_token);
	String result = null;
	try {
	    StatusWapper status = tm.getUserTimeline();
	    List<Status> sList = status.getStatuses();
	    java.lang.reflect.Type type = new com.google.gson.reflect.TypeToken<List<Status>>(){}.getType();
	    result = new Gson().toJson(sList,type);
	} catch (WeiboException e) {
	    e.printStackTrace();
	}
	return result;
    }

    @Override
    public String weiboGetRRCount(User user, String wbId) {
	Timeline tm = new Timeline();
	tm.client.setToken(access_token);
	String result = null;
	try {
	    JSONArray arr = tm.getRRCount(wbId);
	    java.lang.reflect.Type type = new com.google.gson.reflect.TypeToken<JSONArray>(){}.getType();
	    result = new Gson().toJson(arr,type);
	} catch (WeiboException e) {
	    e.printStackTrace();
	}
	return result;
    }

    @Override
    public String weiboGetRepost(User user, String wbId) {
	Timeline tm = new Timeline();
	tm.client.setToken(access_token);
	String result = null;
	try {
	    StatusWapper wapper = tm.getRepostTimeline(wbId);
	    List<Status> sList = wapper.getStatuses();
	    java.lang.reflect.Type type = new com.google.gson.reflect.TypeToken<List<Status>>(){}.getType();
	    result = new Gson().toJson(sList,type);
	} catch (WeiboException e) {
	    e.printStackTrace();
	}
	return result;
    }

    @Override
    public String weiboGetStatus(User user, String wbId) {
	Timeline tm = new Timeline();
	tm.client.setToken(access_token);
	String result = null;
	try {
	    Status status = tm.showStatus(wbId);
	    java.lang.reflect.Type type = new com.google.gson.reflect.TypeToken<Status>(){}.getType();
	    result = new Gson().toJson(status,type);
	} catch (WeiboException e) {
	    e.printStackTrace();
	}
	return result;
    }

    @Override
    public String weiboGetSummary(User user,String uids) {
	Users um = new Users();
	um.client.setToken(access_token);
	String result = null;
	try {
	    JSONArray arr = um.getUserCount(uids);
	    java.lang.reflect.Type type = new com.google.gson.reflect.TypeToken<JSONArray>(){}.getType();
	    result = new Gson().toJson(arr,type);
	} catch (WeiboException e) {
	    e.printStackTrace();
	}
	return result;
    }

    @Override
    public String weiboGetUserInfo(User user,String uid) {
	Users um = new Users();
	um.client.setToken(access_token);
	String result = null;
	try {
	    weibo4j.model.User u = um.showUserById(uid);
	    java.lang.reflect.Type type = new com.google.gson.reflect.TypeToken<weibo4j.model.User>(){}.getType();
	    result = new Gson().toJson(u,type);
	} catch (WeiboException e) {
	    e.printStackTrace();
	}
	return result;
    }

    @Override
    public String weiboPost(User user, String status) {
	Timeline tm = new Timeline();
	tm.client.setToken(access_token);
	String result = null;
	try {
	    Status s = tm.UpdateStatus(status);
	    java.lang.reflect.Type type = new com.google.gson.reflect.TypeToken<Status>(){}.getType();
	    result = new Gson().toJson(s,type);
	} catch (WeiboException e) {
	    e.printStackTrace();
	}
	return result;
    }

    @Override
    public String weiboPostComment(User user, String wbId, String text) {
	Comments cm = new Comments();
	cm.client.setToken(access_token);
	String result = null;
	try {
	    Comment comment = cm.createComment(text, wbId);
	    java.lang.reflect.Type type = new com.google.gson.reflect.TypeToken<Comment>(){}.getType();
	    result = new Gson().toJson(comment,type);
	} catch (WeiboException e) {
	    e.printStackTrace();
	}
	return result;
    }

    @Override
    public String weiboRemove(User user, String wbId) {
	Timeline tm = new Timeline();
	tm.client.setToken(access_token);
	String result = null;
	try {
	    Status status = tm.Destroy(wbId);
	    java.lang.reflect.Type type = new com.google.gson.reflect.TypeToken<Status>(){}.getType();
	    result = new Gson().toJson(status,type);
	} catch (WeiboException e) {
	    e.printStackTrace();
	}
	return result;
    }

    @Override
    public String weiboRemoveComment(User user, String wbId){
	Comments cm = new Comments();
	cm.client.setToken(access_token);
	String result = null;
	try {
	    Comment comment = cm.destroyComment(wbId);
	    java.lang.reflect.Type type = new com.google.gson.reflect.TypeToken<Comment>(){}.getType();
	    result = new Gson().toJson(comment,type);
	} catch (WeiboException e) {
	    e.printStackTrace();
	}
	return result;
    }

    @Override
    public String weiboReplyComment(User user, String text, String wbId,String cid) {
	Comments cm = new Comments();
	cm.client.setToken(access_token);
	String result = null;
	try {
	    Comment comment = cm.replyComment(cid, wbId, text);
	    java.lang.reflect.Type type = new com.google.gson.reflect.TypeToken<Comment>(){}.getType();
	    result = new Gson().toJson(comment,type);
	} catch (WeiboException e) {
	    e.printStackTrace();
	}
	return result;
    }

    @Override
    public String weiboRepost(User user,String wbId) {
	Timeline tm = new Timeline();
	tm.client.setToken(access_token);
	String result = null;
	try {
	    Status status = tm.Repost(wbId);
	    java.lang.reflect.Type type = new com.google.gson.reflect.TypeToken<Status>(){}.getType();
	    result = new Gson().toJson(status,type);
	} catch (WeiboException e) {
	    e.printStackTrace();
	}
	return result;
    }
    public static void main(String[] args) {
	WeiboController weibo = new WeiboController();
	weibo.weiboGetPost(null);
    }
}
