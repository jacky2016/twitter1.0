package com.xunku.actions.myTwitter;

import com.xunku.actions.ActionBase;
import com.xunku.app.Utility;
import com.xunku.app.enums.FilterCommentOrient;
import com.xunku.app.enums.Platform;
import com.xunku.app.helpers.DateHelper;
import com.xunku.app.interfaces.IAccount;
import com.xunku.app.interfaces.ITweet;
import com.xunku.constant.PortalCST;
import com.xunku.dao.my.PostDao;
import com.xunku.daoImpl.my.PostDaoImpl;
import com.xunku.dto.myTwitter.MyReviewDTO;
import com.xunku.pojo.base.Account;
import com.xunku.pojo.base.Pager;
import com.xunku.pojo.base.Post;
import com.xunku.pojo.base.User;
import com.xunku.utils.AppServerProxy;
import com.xunku.utils.Pagefile;

/**
 * <功能描述>我的评论-获取收到和发出的评论列表
 * @author shaoqun
 *
 */
public class MyReviewListAction extends ActionBase {

	PostDao pstDAO = new PostDaoImpl();
	
	@Override
	public Pagefile<MyReviewDTO> doAction() {
		
		
		int type = Integer.parseInt(this.get("type"));	//标签切换编号
		String ucode = this.get("uid");					//博主标识
		String pageIndex = this.get("pageIndex");
		String pageSize = this.get("pageSize");
		// modify by wujian
		Pager pager = new Pager();
		Pagefile<MyReviewDTO> mtfile = new Pagefile<MyReviewDTO>();
		
		pager.setPageIndex(Integer.parseInt(pageIndex));
		pager.setPageSize(Integer.parseInt(pageSize));

		FilterCommentOrient filter = FilterCommentOrient.By_Me;//发出的评论
		if(type == 0){
			filter = FilterCommentOrient.To_Me;				   //收到的评论
		}
	
		User user = this.getUser().getBaseUser();
		Pagefile<ITweet> plist = null;
		try {
			if(ucode != null){
				plist = AppServerProxy.getComments(user,ucode,Platform.Sina, filter, pager);
			}
		} catch (RuntimeException e) {
			e.printStackTrace();
		}

		if(plist != null){
		//转换
			for(ITweet pct : plist.getRows()){
				
				MyReviewDTO mpt = new MyReviewDTO();
				IAccount acc = null ;
					
				if(type != 0 && pct.getSource() != null){//发出的评论
					acc = pct.getSource().getAuthor();
				}else {
					acc= pct.getAuthor();
				}
	
				if(acc != null){
					if (acc.getPlatform() == Platform.Tencent && acc.getHead() != null) {//微博头像
						mpt.account.imgurl = PortalCST.IMAGE_PATH_PERFIX + acc.getHead();
						mpt.account.imgurlBig = PortalCST.IMAGE_PATH_PERFIX + acc.getHead();
					}else{
						mpt.account.imgurl = acc.getHead();
						mpt.account.imgurlBig = acc.getHead();
					}
					mpt.account.name = acc.getName();		     //微博帐号名称
					mpt.account.url = acc.getHomeUrl();
					mpt.account.ucode = acc.getUcode();
					mpt.account.weibos = acc.getWeibos(); 		 //微博数
					mpt.account.followers = acc.getFollowers();  //粉丝数
					mpt.account.friends = acc.getFriends();	 	 //关注数
					mpt.account.summany = acc.getDescription();	 //微博简介	
					mpt.account.isAjax = false;
				}
	
				mpt.id = pct.getTid();//转发或者评论微博Id
				if(pct.getOriginalTweet() != null){
					mpt.orginalid = pct.getOriginalTweet().getTid();// 原始微博ID 
					mpt.url = pct.getOriginalTweet().getUrl();		//微博内容或者评论链接
				}else if(pct.getSource() != null){
					mpt.url = pct.getSource().getUrl();
				}
				if(pct.getSource() != null){
					mpt.posttext = pct.getSource().getText();		//微博内容
				}else{
					mpt.posttext = "";
				}
				if(type == 0){
					mpt.content = ": "+pct.getText();				//评论内容
				}else{
					mpt.content = pct.getText();					
				}
				mpt.createtime = DateHelper.formatDate(pct.getCreated());//发布时间
				mpt.source = pct.getFrom().getName();  //来源
	
				mtfile.getRows().add(mpt);
			}
			mtfile.setRealcount(plist.getRealcount());
			mtfile.setErr(plist.getErr());
		}
		return mtfile;	
	}

}
