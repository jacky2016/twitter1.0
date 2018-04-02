package com.xunku.actions.myTwitter;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.xunku.actions.ActionBase;
import com.xunku.app.Utility;
import com.xunku.app.enums.FilterAccountType;
import com.xunku.app.enums.FilterPostType;
import com.xunku.app.enums.Platform;
import com.xunku.app.helpers.DateHelper;
import com.xunku.app.interfaces.ITweet;
import com.xunku.constant.PortalCST;
import com.xunku.dto.AccountDTO;
import com.xunku.dto.MyPostDTO;
import com.xunku.pojo.base.Account;
import com.xunku.pojo.base.Pager;
import com.xunku.pojo.base.Post;
import com.xunku.pojo.base.User;
import com.xunku.service.WeiboWebService;
import com.xunku.utils.AppServerProxy;
import com.xunku.utils.DateUtils;
import com.xunku.utils.Pagefile;

/**
 * 我的微博--@提到我的action类
 * 
 * @author shangwei
 * @提到我的查询列表的action类
 */
public class TalkMeListAction extends ActionBase {

	@Override
	public Object doAction() {
		// 获取当前用户的ID
		User user = this.getUser().getBaseUser();
		int userid = user.getId();
		String queryStr=this.get("queryConditions");
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
		
		/**
		 * 我的微博--@提到我的查询列表
		 * @author shangwei 
		 */
		if (queryStr.equals("talkMeList")) {
			int type = Integer.parseInt(this.get("Platform"));
			int userType = Integer.parseInt(this.get("userType"));
			int weiboType = Integer.parseInt(this.get("weiboType"));
			String uid = this.get("uid");
			String stime ="";
			String etime="";
			if(this.get("starttime").equals("")){
				Date dBefore = new Date();
				Calendar calendar = Calendar.getInstance(); //得到日历
				calendar.setTime(new Date());//把当前时间赋给日历
				calendar.add(Calendar.DAY_OF_MONTH, -6);  //设置为前一周
				dBefore = calendar.getTime();   //得到前一周的时间

				//SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd"); //设置时间格式
				stime= df.format(dBefore)+ " 00:00:00";;    //格式化前一天
			}else{
				stime = this.get("starttime") + " 00:00:00";
			}
			if(this.get("endtime").equals("")){
			//	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
				etime=df.format(new Date()) + " 23:59:59";;// new Date()为获取当前系统时间
			}else{
				etime=this.get("endtime") + " 23:59:59";
			}
			//String stime = this.get("starttime") + " 00:00:00";
			//String etime = this.get("endtime") + " 23:59:59";
			Platform plat;
			FilterAccountType accFilter;
			FilterPostType postFilter;

			if (type == 1) {
				plat = Platform.Sina;
			} else if (type == 2) {
				plat = Platform.Tencent;
			} else if (type == 3) {
				plat = Platform.Renmin;
			} else {
				plat = Platform.UnKnow;
			}

			if (userType == 0) {
				accFilter = FilterAccountType.All;
			} else if (userType == 1) {
				accFilter = FilterAccountType.Vip;
			} else if (userType == 2) {
				accFilter = FilterAccountType.Friend;
			} else {
				accFilter = FilterAccountType.Navies;
			}

			if (weiboType == 0) {
				postFilter = FilterPostType.All;
			} else if (weiboType == 1) {
				postFilter = FilterPostType.Creative;
			} else if (weiboType == 2) {
				postFilter = FilterPostType.Repost;
			} else {
				postFilter = FilterPostType.Image;
			}
			int pi = Integer.parseInt(this.get("pageIndex"));
			int ps = Integer.parseInt(this.get("pageSize"));
			Pager page = Pager.createPager(pi, ps);
			long start =DateUtils.dateStringToInteger(stime);
			long end =DateUtils.dateStringToInteger(etime);

			Pagefile<ITweet> pagefile = AppServerProxy.getMentions(user, uid,
					plat, start, end, "", accFilter, postFilter, page);
			Pagefile<MyPostDTO> mypts = new Pagefile<MyPostDTO>();
			if (pagefile != null) {
				mypts.setRealcount(pagefile.getRealcount());
				List<ITweet> pts = pagefile.getRows();
				// 开始转化自己的DTO
				for (ITweet p : pts) {
					MyPostDTO mpd = new MyPostDTO();
					//mpd.account.uid= p.getUid();
					if(p.getAuthor()!=null){
					    mpd.account.uid= p.getAuthor().getUcode();
						mpd.account.ucode=p.getAuthor().getUcode();
						mpd.account.twitterType= Utility.getPlatform(p.getAuthor().getPlatform());
						// 如果类型是腾讯，并且头像路径不为null
						if (p.getPlatform() == Platform.Tencent
								&& p.getAuthor().getHead() != null) {
							mpd.account.imgurl = PortalCST.IMAGE_PATH_PERFIX
									+ p.getAuthor().getHead();
							mpd.account.imgurlBig = PortalCST.IMAGE_PATH_PERFIX
							+ p.getAuthor().getLargeHead();
						}else{
							mpd.account.imgurl = p.getAuthor().getHead();
							mpd.account.imgurlBig = p.getAuthor().getLargeHead();
						}
						
						mpd.account.url = p.getAuthor().getHomeUrl();
						if (p.getAuthor().getName() != null) {
							mpd.account.name = p.getAuthor().getName();
						}
						mpd.account.friends = p.getAuthor().getFriends(); // 关注
						mpd.account.weibos = p.getAuthor().getWeibos(); // 发布微博数
						mpd.account.followers = p.getAuthor().getFollowers();// 粉丝数

						mpd.account.summany = p.getAuthor().getDescription();//.replace(mpd.account.name+"：", "");
						//mpd.account.isAjax = false;
					} //getAuthor结束
					
					// 图片
					if (p.getImages() != null) {
						for (String imgPath : p.getImages()) {
							if (p.getPlatform() == Platform.Tencent) {
								mpd.postImage.add(PortalCST.IMAGE_PATH_PERFIX
										+ imgPath);
							} else {
								mpd.postImage.add(imgPath);
							}
						}
					}
					if (p.getSource() == null) {
						mpd.isCreative = true;
					} else {
						mpd.isCreative = false;
					}
					mpd.id = (int) p.getId();// PostID
					mpd.tid = p.getTid();
					mpd.url = p.getUrl(); // 链接
					System.out.println(mpd.url+"   ----------" );
					mpd.text = p.getText(); // 内容
					// 时间
					mpd.createtime = DateHelper.formatDate(p.getCreated());// .getCreateTime();
					// 来源
					if(p.getFrom()!=null){
						mpd.source =p.getFrom().getName();// Utility.getFrom(p.getFrom().getId());
					}else{
						mpd.source ="未知来源";
					}
					
					mpd.repostCount = p.getReposts();// .getRepostCount(); // 转发数
					mpd.commentCount = p.getComments();// .getCommentCount();
					// 评论数
					mpd.item = null;
					// 转发类型
					if (p.getSource() != null) {
						mpd.item = new MyPostDTO();
						ITweet subPost = p.getSource();// post.getRepostList();
						// 账号
						AccountDTO adto = new AccountDTO();
						//adto.id = (int) subPost.getAuthor().getId();
						
						//获取图片路径
						/*
						if (subPost.getPlatform() == Platform.Tencent
								&& subPost.getAuthor().getHead() != null) {
							adto.imgurl = PortalCST.IMAGE_PATH_PERFIX
									+ subPost.getAuthor().getHead();
						}
						adto.name = subPost.getAuthor().getName();
						*/
						if (subPost.getPlatform() == Platform.Tencent
								&& subPost.getAuthor().getHead() != null) {
							adto.imgurl = PortalCST.IMAGE_PATH_PERFIX
									+ subPost.getAuthor().getHead();
							adto.imgurlBig = PortalCST.IMAGE_PATH_PERFIX
									+ subPost.getAuthor().getLargeHead();
						} else {
							adto.imgurl = subPost.getAuthor().getHead();
							adto.imgurlBig = subPost.getAuthor().getLargeHead();
						}
						if (subPost.getAuthor().getName() != null) {
							adto.name = subPost.getAuthor().getName();
						}
						
						mpd.item.account = adto;
                        mpd.item.url=subPost.getUrl();
						mpd.item.id = (int) subPost.getId();
						mpd.item.tid = subPost.getTid();
						mpd.item.text = subPost.getText().replace(mpd.account.name+"：", "");
						mpd.item.createtime = DateHelper.formatDate(subPost
								.getCreated());// .getCreateTime();
						if(subPost.getFrom()!=null){
							mpd.item.source =subPost.getFrom().getName();// Utility.getFrom(subPost.getFrom().getId());// subPost.getSource();
						}
						else{
							mpd.item.source ="未知来源";
						}
						mpd.item.repostCount = subPost.getReposts();// .getRepostCount();
						mpd.item.commentCount = subPost.getComments();// .getCommentCount();
						
						// 图片
						if (subPost.getImages() != null) {
							for (String imgPath : subPost.getImages()) {
								if (subPost.getPlatform() == Platform.Tencent) {
									mpd.item.postImage
											.add(PortalCST.IMAGE_PATH_PERFIX + imgPath);
								} else {
									mpd.item.postImage.add(imgPath);
								}
							}
						}
					
					}
					
					
					/********
					mpd.text = p.getText();
					SimpleDateFormat sdf = new SimpleDateFormat(
							"yyyy-MM-dd  HH:mm");
					String str = sdf.format(p.getCreated());
					mpd.createtime = str;
					Platform pform = p.getPlatform();
					/*
					 * if(pform==Platform.Sina){
					 * mpd.url="http://weibo.com/"+p.getTid(); }else
					 * if(pform==Platform.Tencent){
					 * mpd.url="http://t.qq.com/p/t/"+p.getTid(); }else
					 * if(pform==Platform.Renmin){
					 * mpd.url="http://t.people.com.cn/"+ p.getTid(); }
					 /
					mpd.url = p.getUrl() + p.getTid();
					int form = p.getFrom();
					try {
						String sour = WeiboWebService.getWeiboAppSource(form);
						mpd.source = sour;
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					// Account ac= AppServerProxy.getAccount(p.getUid(), pform,
					// false);
					// 转化成我的
					AccountDTO ad = new AccountDTO();
					// ad.name=ac.getName();
					// ad.imgurl=ac.getHead();
					mpd.account = ad;
					// 里面的post对象
					MyPostDTO xiaop1 = new MyPostDTO();
					AccountDTO xiaoad = new AccountDTO();
					Post xp = p.getSource();
					if (xp != null) {
						xiaop1.text = xp.getText();
						SimpleDateFormat sdf1 = new SimpleDateFormat(
								"yyyy-MM-dd  HH:mm");
						String str1 = sdf1.format(xp.getCreated());
						xiaop1.createtime = str1;
						int form1 = xp.getFrom();
						try {
							String sour = WeiboWebService
									.getWeiboAppSource(form1);
							xiaop1.source = sour;
						} catch (IOException e) {
							e.printStackTrace();
						}
						xiaop1.repostCount = xp.getReposts();
						xiaop1.commentCount = xp.getComments();
						// Account aa= AppServerProxy.getAccount(xp.getUid(),
						// pform, false);
						// xiaoad.name=aa.getName();
						xiaop1.account = xiaoad;
						mpd.item = xiaop1;
					}// 添加
			*/
					
				mypts.getRows().add(mpd);
				}
			}
			return mypts;
		}
		

		if (false) {
			Pagefile<MyPostDTO> lst = new Pagefile<MyPostDTO>();
			// 1
			MyPostDTO p1 = new MyPostDTO();
			p1.text = "C罗一定拿下拜仁啊！支持你";
			p1.url = ("http://www.baidu.com");
			p1.createtime = "2014-05-01";
			p1.source = "新浪微博";

			AccountDTO ad = new AccountDTO();
			ad.name = ("C罗");
			ad.imgurl = "themes/modules/images/photo.gif";
			p1.account = (ad);

			MyPostDTO xiaop1 = new MyPostDTO();
			AccountDTO xiaoad = new AccountDTO();
			xiaoad.name = "穆帅222";
			xiaop1.account = xiaoad;
			xiaop1.text = "哈哈====";
			xiaop1.url = ("http://www.baidu.com");
			xiaop1.createtime = "2014-05-01";
			xiaop1.source = "新浪微博";
			xiaop1.repostCount = 888;
			xiaop1.commentCount = 999;
			p1.item = xiaop1;

			lst.getRows().add(p1);

			// 2
			MyPostDTO p2 = new MyPostDTO();
			p2.text = ("罗本不让皇马拿下比分");
			p2.url = ("http://www.baidu.com");
			p2.createtime = ("2014-05-02");
			p2.source = ("人民微博");

			AccountDTO ad2 = new AccountDTO();
			ad2.name = ("梅西");
			ad2.imgurl = "themes/modules/images/photo.gif";
			p2.account = (ad2);
			// 222
			MyPostDTO xiaop2 = new MyPostDTO();
			AccountDTO xiaoad2 = new AccountDTO();
			xiaoad2.name = "瓜迪奥拉";
			xiaop2.account = xiaoad2;
			xiaop2.text = "哈哈====+++";
			xiaop2.url = ("http://www.baidu.com");
			xiaop2.createtime = "2014-05-01";
			xiaop2.source = "新浪微博";
			xiaop2.repostCount = 888;
			xiaop2.commentCount = 999;
			p2.item = xiaop2;

			// 2
			lst.getRows().add(p2);

			lst.setRealcount(133);
			// 假数据结束
			return lst;
		}
		return null;
	}

}
