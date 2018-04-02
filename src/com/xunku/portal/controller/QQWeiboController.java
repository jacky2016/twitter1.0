package com.xunku.portal.controller;

import com.tencent.weibo.api.FriendsAPI;
import com.tencent.weibo.api.PrivateAPI;
import com.tencent.weibo.api.StatusesAPI;
import com.tencent.weibo.api.TAPI;
import com.tencent.weibo.api.UserAPI;
import com.tencent.weibo.oauthv2.OAuthV2;
import com.xunku.pojo.base.User;
import com.xunku.constant.QQParamType;
/**
 * 腾讯微博api的实现
 * @author wanghui
 * @version twitter1.0
 */
public class QQWeiboController extends StatusesContollerBase{
    private static OAuthV2 oAuth=new OAuthV2();
    
    public QQWeiboController(){
	oAuth.setClientId("801202576");
        oAuth.setClientSecret("469edecb80f7eadf843ff81cd095250c");
        oAuth.setRedirectUri("http://xunku.org/");
        oAuth.setAccessToken("277b10dc103e7e791c6d1b4435a419e5");
        oAuth.setOpenid("4F21DDCCF9FFE589F053BB4DA07A35E2");
        oAuth.setOpenkey("73229960849F017B546AD5E374EACC8D");
        oAuth.setExpiresIn("604800");
    }
    
    @Override
    public String messageGet(User user) {
	PrivateAPI privateAPI= new PrivateAPI(oAuth.getOauthVersion());
	String result = null;
	try {
	    result = privateAPI.recv(oAuth, QQParamType.FORMAT,QQParamType.PAGEFLAG,QQParamType.PAGETIME,QQParamType.REQNUM,QQParamType.LASTID,QQParamType.CONTENTTYPE);
	} catch (Exception e) {
	    e.printStackTrace();
	}
	return result;
    }

    @Override
    public String weiboFriendCreate(User user, String friendIds) {
	FriendsAPI friendsAPI= new FriendsAPI(oAuth.getOauthVersion());
	String result = null;
	try {
	    result = friendsAPI.add(oAuth, QQParamType.FORMAT, friendIds, QQParamType.FOPENIDS);
	} catch (Exception e) {
	    e.printStackTrace();
	}
	return result;
    }

    @Override
    public void weiboFriendRemove(User user, String friendId) {
	FriendsAPI friendsAPI= new FriendsAPI(oAuth.getOauthVersion());
	try {
	    friendsAPI.del(oAuth, QQParamType.FORMAT, friendId, QQParamType.FOPENIDS);
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    @Override
    public String weiboGetAt(User user) {
	StatusesAPI statusesAPI= new StatusesAPI(oAuth.getOauthVersion());
	String result = null;
	try {
	    result = statusesAPI.mentionsTimeline(oAuth, QQParamType.FORMAT, QQParamType.PAGEFLAG, QQParamType.PAGETIME, QQParamType.REQNUM, QQParamType.LASTID, QQParamType.Type, QQParamType.CONTENTTYPE);
	} catch (Exception e) {
	    e.printStackTrace();
	}
	return result;
    }

    @Override
    public String weiboGetAtMe(User user) {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public String weiboGetCommentList(User user, String wbId) {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public String weiboGetCommentMineList(User user) {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public String weiboGetFollowerList(User user, String screen_name) {
	FriendsAPI friendsAPI= new FriendsAPI(oAuth.getOauthVersion());
	String result = null;
	try {
	    result = friendsAPI.fanslist(oAuth, QQParamType.FORMAT, QQParamType.REQNUM, QQParamType.STARTINDEX, QQParamType.MODE, QQParamType.INSTALL);
	} catch (Exception e) {
	    e.printStackTrace();
	}
	return result;
    }

    @Override
    public String weiboGetFriendList(User user, String uid) {
	FriendsAPI friendsAPI= new FriendsAPI(oAuth.getOauthVersion());
	String result = null;
	try {
	    result = friendsAPI.userIdollist(oAuth, QQParamType.FORMAT, QQParamType.REQNUM, QQParamType.STARTINDEX, "WongJacky", QQParamType.FOPENIDS, QQParamType.INSTALL);
	} catch (Exception e) {
	    e.printStackTrace();
	}
	return result;
    }

    @Override
    public String weiboGetMyCommentList(User user) {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public String weiboGetNewest(User user, String wbId) {
	StatusesAPI statusesAPI= new StatusesAPI(oAuth.getOauthVersion());
	String result = null;
	try {
	    result = statusesAPI.homeTimeline(oAuth, QQParamType.FORMAT, QQParamType.PAGEFLAG, QQParamType.PAGETIME, QQParamType.REQNUM, QQParamType.Type, QQParamType.CONTENTTYPE);
	} catch (Exception e) {
	    e.printStackTrace();
	}
	return result;
    }

    @Override
    public String weiboGetPost(User user) {
	StatusesAPI statusesAPI= new StatusesAPI(oAuth.getOauthVersion());
	String result = null;
	try {
	    result = statusesAPI.broadcastTimeline(oAuth, QQParamType.FORMAT, QQParamType.PAGEFLAG, QQParamType.PAGETIME, QQParamType.REQNUM, QQParamType.LASTID, QQParamType.Type, QQParamType.CONTENTTYPE);
	} catch (Exception e) {
	    e.printStackTrace();
	}
	return result;
    }

    @Override
    public String weiboGetRRCount(User user, String wbId) {
	TAPI tAPI=new TAPI(oAuth.getOauthVersion());
	String result = null;
	try {
	    result = tAPI.reCount(oAuth, QQParamType.FORMAT, wbId, QQParamType.FLAG);
	} catch (Exception e) {
	    e.printStackTrace();
	}
	return result;
    }

    @Override
    public String weiboGetRepost(User user, String wbId) {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public String weiboGetStatus(User user, String wbId) {
	TAPI tAPI=new TAPI(oAuth.getOauthVersion());
	String result = null;
	try {
	    result = tAPI.show(oAuth, QQParamType.FORMAT, wbId);
	} catch (Exception e) {
	    e.printStackTrace();
	}
	return result;
    }

    @Override
    public String weiboGetSummary(User user, String ids) {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public String weiboGetUserInfo(User user, String uid) {
	UserAPI userAPI= new UserAPI(oAuth.getOauthVersion());
	String result = null;
	try {
	    result = userAPI.info(oAuth, QQParamType.FORMAT);
	} catch (Exception e) {
	    e.printStackTrace();
	}
	return result;
    }

    @Override
    public String weiboPost(User user, String status) {
	TAPI tAPI=new TAPI(oAuth.getOauthVersion());
	String result = null;
	try {
	    result = tAPI.add(oAuth, QQParamType.FORMAT, status, QQParamType.CLIENTIP, QQParamType.LONGITUDE, QQParamType.LATITUDE, QQParamType.SYNCFLAG);
	} catch (Exception e) {
	    e.printStackTrace();
	}
	return result;
    }

    @Override
    public String weiboPostComment(User user, String wbId, String text) {
	TAPI tAPI=new TAPI(oAuth.getOauthVersion());
	String result = null;
	try {
	    result = tAPI.comment(oAuth, QQParamType.FORMAT, text, QQParamType.CLIENTIP, QQParamType.LONGITUDE, QQParamType.LATITUDE, wbId);
	} catch (Exception e) {
	    e.printStackTrace();
	}
	return result;
    }

    @Override
    public String weiboRemove(User user, String wbId) {
	TAPI tAPI=new TAPI(oAuth.getOauthVersion());
	String result = null;
	try {
	    result = tAPI.del(oAuth, QQParamType.FORMAT, wbId);
	} catch (Exception e) {
	    e.printStackTrace();
	}
	return result;
    }

    @Override
    public String weiboRemoveComment(User user, String wbId) {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public String weiboReplyComment(User user, String text, String weiboId,
	    String cid) {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public String weiboRepost(User user, String wbId) {
	TAPI tAPI=new TAPI(oAuth.getOauthVersion());
	String result = null;
	try {
	    result = tAPI.reAdd(oAuth, QQParamType.FORMAT, "", QQParamType.CLIENTIP, QQParamType.LONGITUDE, QQParamType.LATITUDE, wbId);
	} catch (Exception e) {
	    e.printStackTrace();
	}
	return result;
    }
    public static void main(String[] args) {
	QQWeiboController weibo = new QQWeiboController();
	String resutlString = weibo.weiboGetFollowerList(null, null);
	System.out.println(resutlString);
    }
}
