package com.xunku.actions.coordination;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import com.xunku.actions.ActionBase;
import com.xunku.app.Utility;
import com.xunku.app.enums.Platform;
import com.xunku.app.helpers.DateHelper;
import com.xunku.app.interfaces.IAccount;
import com.xunku.app.interfaces.ITweet;
import com.xunku.app.model.PostsWrapper;
import com.xunku.app.result.NavieCountResult;
import com.xunku.app.result.RepostOrganizationResult;
import com.xunku.app.result.Result;
import com.xunku.app.result.WeiboTextResult;
import com.xunku.constant.PortalCST;
import com.xunku.constant.SendCountEnum;
import com.xunku.constant.WeiboType;
import com.xunku.dao.base.AccountDao;
import com.xunku.dao.base.UserDao;
import com.xunku.dao.my.PostDao;
import com.xunku.dao.my.SendingDao;
import com.xunku.dao.office.NavieDao;
import com.xunku.daoImpl.base.AccountDaoImpl;
import com.xunku.daoImpl.base.UserDaoImpl;
import com.xunku.daoImpl.my.PostDaoImpl;
import com.xunku.daoImpl.my.SendingDaoImpl;
import com.xunku.daoImpl.office.NavieDaoImpl;
import com.xunku.dto.AccountDTO;
import com.xunku.dto.MyPostDTO;
import com.xunku.dto.PagerDTO;
import com.xunku.dto.coordination.DropdownBoxDataDTO;
import com.xunku.dto.coordination.ExamineManagerDTO;
import com.xunku.dto.coordination.OrganizationDTO;
import com.xunku.dto.myTwitter.PublishManagerDTO;
import com.xunku.dto.myTwitter.PublishManagerRepostDTO;
import com.xunku.dto.myTwitter.SendingDTO;
import com.xunku.dto.myTwitter.WeiboPostDTO;
import com.xunku.dto.office.NavieCountDTO;
import com.xunku.dto.office.PostDealDTO;
import com.xunku.dto.office.SendInfoDTO;
import com.xunku.dto.sysManager.AccountVO;
import com.xunku.enums.common.LoadListType;
import com.xunku.pojo.base.Account;
import com.xunku.pojo.base.AccountInfo;
import com.xunku.pojo.base.Pager;
import com.xunku.pojo.base.Post;
import com.xunku.pojo.base.User;
import com.xunku.pojo.my.Sender;
import com.xunku.pojo.my.Sending;
import com.xunku.pojo.office.Navie;
import com.xunku.pojo.office.NavieAccount;
import com.xunku.utils.AppServerProxy;
import com.xunku.utils.DateUtils;
import com.xunku.utils.Pagefile;

/**
 * 协同办公--考核管理action类 所有页面不带权限的 还有 所有页面的列表
 * 
 * @author shangwei
 * 
 */

public class ExamineManagerAction extends ActionBase {

	AccountDao dao = new AccountDaoImpl();
	NavieDao navieDao = new NavieDaoImpl();
	PostDao postDao = new PostDaoImpl();
	UserDao userDao = new UserDaoImpl();
	SendingDao sendDao = new SendingDaoImpl();

	//获取下拉框里面的值
	public    List<DropdownBoxDataDTO>   getDropDownBox( int userid){
		List<AccountVO> list = dao.queryAccountByUid(userid);
		List<DropdownBoxDataDTO> list2 = new ArrayList<DropdownBoxDataDTO>();
		for (AccountVO vo : list) {
			DropdownBoxDataDTO dbd = new DropdownBoxDataDTO();
			dbd.name = vo.getName();
			dbd.type = vo.getType();
			dbd.uid = vo.getUid();
			list2.add(dbd);
		}
		return list2;
	}
	
	// ***************************************************所有微博发布统计模块的开始
	 /**
	  *      微博发布统计的列表展示
	  * */
		 public     Pagefile<ExamineManagerDTO>   getReleasecountList( PagerDTO pagedto,int customId,String stime,String etime){
				
			 Pagefile<SendInfoDTO> pf = sendDao.querySendInfo(pagedto, customId,	stime, etime);
				Pagefile<ExamineManagerDTO> pagefile = new Pagefile<ExamineManagerDTO>();
				if (pf != null) {
					pagefile.setRealcount(pf.getRealcount());
					List<SendInfoDTO> sends = pf.getRows();
					for (SendInfoDTO dto : sends) {
						ExamineManagerDTO em = new ExamineManagerDTO();
						em.id = dto.getId();
						em.submitName = dto.getName();
						em.submitNum = dto.getSubcount();
						em.adoptNum = dto.getSendcount();
						if(em.submitNum==0&&em.adoptNum==0){
						}else{
							pagefile.getRows().add(em);
						}
					}
					if(pagefile.getRows().size()<pf.getRealcount()){
						pagefile.setRealcount(pagefile.getRows().size());
					}
				}
				return pagefile;
		 }
		 
		 /**
		  * 微博发布统计的提交和采纳的列表
		  * (微博发布统计列表点击其中一行的提交或者采纳进入 内部的展示列表页面)
		 */
		 public    Pagefile<PublishManagerDTO>       getReleaseSumbitNumList(  AccountDao accountDao, UserDao  userDao,Pager pagedto, int type,  int cuid, String stime,String etime,
				 String  pointer, String  uid , int  curids){
				Pagefile<PublishManagerDTO> pf2 = new Pagefile<PublishManagerDTO>();
				
				Pagefile<Sending> pf = new Pagefile<Sending>();
				if(pointer.equals("relaseSumbitNum")){ 
					//type来区分--- 采纳 3 和提交0 的 
					if(type==3){
						pf = sendDao.querySubmitInfo(pagedto, stime,etime, cuid, SendCountEnum.accept);
					}else if(type==0){
						pf = sendDao.querySubmitInfo(pagedto, stime,etime, cuid, SendCountEnum.submit);
					}
				}else if(pointer.equals("dealwithRePostAll")){
					pf=sendDao.queryCommentList(pagedto, stime,etime, uid, curids, WeiboType.RepostPost);
				} else if(pointer.equals("dealwithCommentsAll")){
					pf=sendDao.queryCommentList(pagedto, stime, etime, uid, curids, WeiboType.CommentPost);
					
				}
				
				if (pf != null) {
					pf2.setRealcount(pf.getRealcount());
					List<Sending> sds = pf.getRows();

					 for (Sending sd : sds) {
						PublishManagerDTO pm = new PublishManagerDTO();
						long ins = sd.getId();
						pm.ids = String.valueOf(ins);
						List<Sender> tes = sd.getSendList();
						pm.weiboAccountHTML = "";
						/*查找审核人
						pm.auditorInt = sd.getAuditor();
						User us = userDao.queryByUid(pm.auditorInt);
						if (us != null) {
							// pm.auditor = us.getUserName();
							pm.auditor = us.getNickName();
						} else {
							pm.auditor = "无";
						}
						*/
						 int weiboAccListNum=0;
						for (int i = 0; i < tes.size(); i++) {
							Sender tey = tes.get(i);
							AccountInfo accInfo = accountDao.queryByUid(tey
									.getUid());
							if (accInfo != null) {
							String htmlName = accInfo.getName();
							// accInfo.getName().substring(0,6);
							if (accInfo.getName().length() > 6) {
								htmlName = htmlName.substring(0, 6);
							}
							int pNum = tey.getPlatform();

							// 发布成功 发布失败的 两个模版
							String failHTML = "<span class=\"manageSend_style manageSend_fail\"></span>";
							String successHTML = "<span class=\"manageSend_style manageSend_sucess\"></span>";
							String sendingHTML = "<span class=\"manageSend_style manageSend_ding\"></span>";
							String HTML = "";
							// 微博发送的状态 成功 or 失败 1=未发送、2=发送成功、3=发送失败
							int sendStaus = tey.getStatus();
							if (sendStaus == 2) {
								HTML = successHTML;
							} else if (sendStaus == 3) {
								HTML = failHTML;
							} else {
								HTML = sendingHTML;
							}
							switch (pNum) {
							case 1:
								pm.weiboAccountHTML += "<div class=\"send_photo pm_All com_fLeft\"      pmUID=\""
										+ tey.getUid()
										+ "\"  pmPlatform=\"1\">  "
										+ HTML
										+ "</div><span class=\"send_zhao\">"
										+ htmlName + "</span>";
								break;
							case 2:
								pm.weiboAccountHTML += "<div class=\"send_photo2 pm_All com_fLeft\"     pmUID=\""
										+ tey.getUid()
										+ "\"  pmPlatform=\"2\">  "
										+ HTML
										+ "</div><span class=\"send_zhao\">"
										+ htmlName + "</span>";
								break;
							case 5:
								pm.weiboAccountHTML += "<div class=\"send_photo3  pm_All com_fLeft\"     pmUID=\""
										+ tey.getUid()
										+ "\"  pmPlatform=\"5\"> "
										+ HTML
										+ "</div><span class=\"send_zhao\">"
										+ htmlName + "</span>";
								break;
							default:
								pm.weiboAccountHTML += "<div class=\"send_photo  pm_All com_fLeft\"    pmUID=\""
										+ tey.getUid()
										+ "\"  pmPlatform=\"1\">  "
										+ HTML
										+ "</div><span class=\"send_zhao\">"
										+ htmlName + "</span>";
								break;
							}
							weiboAccListNum++;
						}  //acctInfo不为null
						} // for 结束

							
						// 判断是否多于css指定的(最多显示3个还是5个帐号的),
						// 多余(3个或者5个)的 要展示 + 展开按钮 ， 如果低于的 则不显示
						// if(tes.size()>5){
						//if (tes.size() > 5) {
						if(weiboAccListNum>5){
							pm.weiboAccountHTML += "</div><span class=\"send_showMore\">+</span>";
						} else {
							pm.weiboAccountHTML += "</div>";
						}
						pm.text = sd.getText();
						if (sd.getImages() != null) {
							String[] imagas = sd.getImages().split(",");
							if (!imagas[0].equals("")) {
								for (int i = 0; i < imagas.length; i++) {
									pm.text += "<img  src=\""
											// + PortalCST.IMAGE_PATH_PERFIX
											+ imagas[i]
											+ "\" width=\"100\" height=\"100\" />";
								}
							}
						}

						// 截取提交时间的字符串
						pm.submitTime = sd.getSubmit().substring(0,
								sd.getSubmit().lastIndexOf(":"));
						// pm.submitTime = sd.getSubmit();

						if (sd.getSendList() != null) {
							if (sd.getSendList().size() > 0) {
								int pp = sd.getSendList().get(0).getPlatform();
								Platform m;
								if (pp == 1) {
									m = Platform.Sina;
								} else if (pp == 2) {
									m = Platform.Tencent;
								} else if (pp == 5) {
									m = Platform.Renmin;
								} else {
									m = Platform.Sina;
								}
								ITweet weet = null;
								if(pointer.equals("relaseSumbitNum")|| pointer.equals("dealwithCommentsAll")){
									weet = sd.getPost(this.getUser()
											.getBaseUser(), sd.getSourceid(), m);
								}else if(pointer.equals("dealwithRePostAll")){
								 weet = sd.getPost(this.getUser()
											.getBaseUser(), sd.getOrgId(), m); // sd.getSourceid()
								}
								if (weet != null) {
									PublishManagerRepostDTO rdt = new PublishManagerRepostDTO();
									rdt.repostName = weet.getAuthor()
											.getDisplayName();
									rdt.repostTime = DateUtils
											.formatDateString(weet.getCreated());
									rdt.repostText = weet.getText();
									rdt.repostNum = weet.getReposts();
									rdt.commentNum = weet.getComments();
									List<String> mgs = weet.getImages();
									if (mgs != null) {
										rdt.Images = mgs;
									} // images 集合判断是否为null
									pm.pmr = rdt;
								} // 阴影部分结束 转发原帖
							}
						}

						// 发送时间
						long sendtm = sd.getSent();
						// 定时发送
						if (sendtm != 0) {
							Date dateOld = new Date(sendtm);
							String date = "";
							try {
								date = new SimpleDateFormat("yyyy-MM-dd HH:mm")
										.format(dateOld);
							} catch (RuntimeException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							pm.sentTime = "发布时间：</span><span>" + date;
						}
						// 为0的 为立即发送
						else {
							pm.sentTime = "发布时间：</span><span>";
						}

						// 查找修改人
						User uu = userDao.queryByUid(sd.getUserid());
						if (uu != null) {
							pm.mofityers = uu.getNickName();
						} else {
							pm.mofityers = "无";
						}

						/*
						 * if (type == 0) { pm.mofityers = pm.auditor; pm.changeType =
						 * "审核人"; if(pm.mofityers.equals("无")){ pm.mofityers="<span></span>";
						 * pm.changeType=""; }else{ pm.mofityers="<span>"+pm.mofityers+"</span>"; } }
						 * if(type==3){ pm.changeType = "提交人";
						 * if(!pm.auditor.equals("无")){ pm.mofityers="<span>"+pm.mofityers+"</span><span>审核人</span><span>"+pm.auditor+"</span>";
						 * }else{ pm.mofityers="<span>"+pm.mofityers+"</span>"; } }
						 */
						pm.changeType = "提交人";
						pm.mofityers = "<span>" + pm.mofityers + "</span>";
						pf2.getRows().add(pm);

					}// for结束
				}

				if (pf2.getRows().size() != pf.getRows().size()) {
					pf2.setRealcount(pf2.getRows().size());
					System.out.println(pf2.getRows().size());
				}
				return pf2;
		 }
	
	// ***************************************************所有微博发布统计模块的结束
		 
		 
   // ***************************************************所有微博处理统计开始
		  public      Pagefile<ExamineManagerDTO>          getDealWithCountList(SendingDao sendDao,Pager  pagedto,String uid, int customId, String starttime,String endtime ){
				Pagefile<PostDealDTO> pf = sendDao.querySendDeal(pagedto,
						starttime, endtime, uid, customId);
				Pagefile<ExamineManagerDTO> pagefile = new Pagefile<ExamineManagerDTO>();
				if (pf != null) {
					pagefile.setRealcount(pf.getRealcount());
					List<PostDealDTO> lt = pf.getRows();
					for (PostDealDTO dto : lt) {
						ExamineManagerDTO em = new ExamineManagerDTO();
						em.id = dto.getId();
						em.dealName = dto.getUseName();
						em.comments = dto.getComments();
						em.reposts = dto.getReposts();
						if(em.comments==0&&em.reposts==0){
						}else{
							pagefile.getRows().add(em);
						}
					}
					if(pagefile.getRows().size()<pf.getRealcount()){
						pagefile.setRealcount(pagefile.getRows().size());
					}
					
				}
				return pagefile;
		  }
		 
			/**
			 * 获取微博处理统计列表中的转发数的详细信息
			 */
		  
		   public   Pagefile<PublishManagerDTO>        getDealWithRePostAll( AccountDao  accountDao,UserDao  userDao,Pager pager, int type, int cuid, String stime,String etime, String uid , int curuserid){
			   // 取的和 微博发布的提交和采纳类似
			   	return  getReleaseSumbitNumList(accountDao, userDao, pager, type, cuid, stime, etime, "dealwithRePostAll", uid, curuserid);
		   }
		  
			/**
			 * 获取微博处理统计列表中的评论数的详细信息
			 */
		   public   Pagefile<PublishManagerDTO>     getDealWithCommentsAll(AccountDao  accountDao,UserDao  userDao,Pager pager, int type, int cuid, String stime,String etime, String uid , int curuserid){
			   // 取的和 微博发布的提交和采纳类似
			   return   getReleaseSumbitNumList(accountDao, userDao, pager, type, cuid, stime, etime, "dealwithCommentsAll", uid, curuserid);
		   }
		 
  // ***************************************************所有微博处理统计结束	 
		 
  //***************************************************所有网评员统计开始
		   	
		   public        Pagefile<ExamineManagerDTO>           getCommentatorscountList(User uu, long  starttime, long endtime, Pager pg){
				Pagefile<NavieCountResult> pf = AppServerProxy.queryNavieCount(uu, pg, starttime, endtime);
				Pagefile<ExamineManagerDTO> pagefile = new Pagefile<ExamineManagerDTO>();
				if (pf != null) {
					pagefile.setRealcount(pf.getRealcount());
					List<NavieCountResult> lt = pf.getRows();
					for (NavieCountResult ncdto : lt) {
						ExamineManagerDTO em = new ExamineManagerDTO();
						em.id = ncdto.getNavieid();
						Navie navie = navieDao.queryNavieById(ncdto.getNavieid());
						if (navie != null) {
							em.commentatorsName = navie.getName();
						}
						em.commentatorsAccountNum = ncdto.getUids();
						em.totalRepostNum = ncdto.getTotal();
						pagefile.getRows().add(em);
					}
				}
				return pagefile;
		   }
		   
		   public     Pagefile<ExamineManagerDTO>      getCommentatorsAlertTable( NavieDao   navieDao, PagerDTO pagedto,int customId){
				Pagefile<NavieAccount> pf = navieDao.queryNavieList(pagedto,
						customId);
				Pagefile<ExamineManagerDTO> pagefile = new Pagefile<ExamineManagerDTO>();
				if (pf != null) {
					pagefile.setRealcount(pf.getRealcount());
					pagefile.setErr(pf.getErr());
					List<NavieAccount> lt = pf.getRows();
					for (NavieAccount na : lt) {
						ExamineManagerDTO em = new ExamineManagerDTO();
						// em.id=na.getId();
						em.id = na.getNavie().getId();
						em.comtorsEveryOneName = na.getNavie().getName();
						em.comtorsAllUID = na.getUid();
						em.comtorsAccName = na.getDisplayName();
						if (na.getPlatform() == 1) {
							em.comtorsAccPlatform = "新浪";
						} else if (na.getPlatform() == 2) {
							em.comtorsAccPlatform = "腾讯";
						} else if (na.getPlatform() == 5) {
							em.comtorsAccPlatform = "人民";
						} else {
							em.comtorsAccPlatform = "新浪";
						}
						pagefile.getRows().add(em);
					}// for
				}
				return pagefile;
		   }
		   
		   public     	List<ExamineManagerDTO>          getCommentatorsList(String  name ,int customId){
				List<NavieAccount> nas = navieDao.queryNavieList(name, customId);
				List<ExamineManagerDTO> ems = new ArrayList<ExamineManagerDTO>();
				if (nas != null) {
					for (NavieAccount na : nas) {
						ExamineManagerDTO em = new ExamineManagerDTO();
						em.id = na.getNavie().getId();
						em.comtorsUID = na.getUid();
						em.personName = na.getNavie().getName();
						if (na.getDisplayName().length() > 9) {
							em.accName = na.getDisplayName().substring(0, 9);
						} else {
							em.accName = na.getDisplayName();
						}
						if (na.getPlatform() == 1) {
							em.platType = "新浪";
						} else if (na.getPlatform() == 2) {
							em.platType = "腾讯";
						} else if (na.getPlatform() == 5) {
							em.platType = "人民";
						} else {
							em.platType = "新浪";
						}
						ems.add(em);
					} // for结束
				}
				return ems;
		   }
		   
		   public    Pagefile<MyPostDTO>          getCommentatorsToMeNum(User  usr, Pager  pg, int nid,long sti,long  eti, WeiboType wt){

				Pagefile<ITweet> posts = AppServerProxy.queryNavieRCResult(usr, pg, nid, sti, eti, wt);
				Pagefile<MyPostDTO> collection = new Pagefile<MyPostDTO>();
				if (posts != null) {
					collection.setRealcount(posts.getRealcount());
					collection.setErr(posts.getErr());
					List<ITweet> lt = posts.getRows();

					for (ITweet post : lt) {
						if (post != null) {
							MyPostDTO entity = new MyPostDTO();
							// 账号
							if (post.getAuthor() != null) {
								entity.account.uid = post.getAuthor().getUcode();
								entity.account.ucode = post.getAuthor().getUcode();

								entity.account.twitterType = Utility
										.getPlatform(post.getAuthor().getPlatform());
								// 如果类型是腾讯，并且头像路径不为null
								if (post.getPlatform() == Platform.Tencent
										&& post.getAuthor().getHead() != null) {
									entity.account.imgurl = PortalCST.IMAGE_PATH_PERFIX
											+ post.getAuthor().getHead();
									entity.account.imgurlBig = PortalCST.IMAGE_PATH_PERFIX
											+ post.getAuthor().getLargeHead();
								} else {
									entity.account.imgurl = post.getAuthor()
											.getHead();
									entity.account.imgurlBig = post.getAuthor()
											.getLargeHead();
								}

								entity.account.url = post.getAuthor().getHomeUrl();
								if (post.getAuthor().getName() != null) {
									entity.account.name = post.getAuthor()
											.getName();
								}

								entity.account.friends = post.getAuthor()
										.getFriends(); // 关注
								entity.account.weibos = post.getAuthor()
										.getWeibos(); // 发布微博数
								entity.account.followers = post.getAuthor()
										.getFollowers();// 粉丝数
								entity.account.summany = post.getAuthor()
										.getDescription();// .replace(entity.account.name+"：",
															// "");
								// entity.account.isAjax=false;
							}

							// 图片
							/*
							 * if (post.getImages() != null) { for (String imgPath :
							 * post.getImages()) {
							 * entity.postImage.add(PortalCST.IMAGE_PATH_PERFIX +
							 * imgPath); } }
							 */
							if (post.getImages() != null) {
								for (String imgPath : post.getImages()) {
									if (post.getPlatform() == Platform.Tencent) {
										entity.postImage
												.add(PortalCST.IMAGE_PATH_PERFIX
														+ imgPath);
									} else {
										entity.postImage.add(imgPath);
									}
								}
							}
							if (post.getSource() == null) {
								entity.isCreative = true;
							} else {
								entity.isCreative = false;
							}
							entity.id = (int) post.getId();// PostID
							entity.tid = post.getTid();
							entity.url = post.getUrl(); // 链接

							entity.text = post.getText().replace(
									entity.account.name + "：", ""); // 内容
							entity.createtime = DateHelper.formatDate(post
									.getCreated());// .getCreateTime();
							// 时间
							entity.source = Utility.getFrom(post.getFrom().getId());
							// 来源
							entity.repostCount = post.getReposts();// .getRepostCount();
							// 转发数
							entity.commentCount = post.getComments();// .getCommentCount();
							// 评论数
							entity.item = null;
							// 转发类型
							if (post.getSource() != null) {
								entity.item = new MyPostDTO();
								ITweet subPost = post.getSource();// post.getRepostList();
								// 账号
								AccountDTO adto = new AccountDTO();
								// adto.id = (int) subPost.getAuthor().getId();

								// 获取图片路径
								/*
								 * if (subPost.getPlatform() == Platform.Tencent &&
								 * subPost.getAuthor().getHead() != null) {
								 * adto.imgurl = PortalCST.IMAGE_PATH_PERFIX +
								 * subPost.getAuthor().getHead(); } adto.name =
								 * subPost.getAuthor().getName();
								 */
								if (subPost.getPlatform() == Platform.Tencent
										&& subPost.getAuthor().getHead() != null) {
									adto.imgurl = PortalCST.IMAGE_PATH_PERFIX
											+ subPost.getAuthor().getHead();
									adto.imgurlBig = PortalCST.IMAGE_PATH_PERFIX
											+ subPost.getAuthor().getLargeHead();
								} else {
									adto.imgurl = subPost.getAuthor().getHead();
									adto.imgurlBig = subPost.getAuthor()
											.getLargeHead();
								}
								if (subPost.getAuthor().getName() != null) {
									adto.name = subPost.getAuthor().getName();
								}

								entity.item.account = adto;

								entity.item.id = (int) subPost.getId(); 
								entity.item.tid = subPost.getTid();
								entity.item.text = subPost.getText();
								entity.item.url = subPost.getUrl();
								if (wt == WeiboType.CommentPost) {
									entity.url = entity.item.url; // 评论链接
								}

								entity.item.createtime = DateHelper
										.formatDate(subPost.getCreated());// .getCreateTime();
								entity.item.source = Utility.getFrom(subPost
										.getFrom().getId());// subPost.getSource();
								entity.item.repostCount = subPost.getReposts();// .getRepostCount();
								entity.item.commentCount = subPost.getComments();// .getCommentCount();

								// 图片
								if (subPost.getImages() != null) {
									for (String imgPath : subPost.getImages()) {
										if (subPost.getPlatform() == Platform.Tencent) {
											entity.item.postImage
													.add(PortalCST.IMAGE_PATH_PERFIX
															+ imgPath);
										} else {
											entity.item.postImage.add(imgPath);
										}
									}
								}

							}
							collection.getRows().add(entity);
						}// for结束
					}
				}

				return collection;

			   
		   }
		   
  // ***************************************************所有网评员统计结束 
		   
		   
		   
			// *****************************************************所有微博内容统计开始
		      //微博内容统计的列表
		   	 public         Pagefile<ExamineManagerDTO>      getContentcountList(User usr,Pager pg, long sti,long eti, String uid ){

					Pagefile<WeiboTextResult> pf = AppServerProxy.queryTweetCountDetail(usr, pg,	sti, eti, uid);
					Pagefile<ExamineManagerDTO> pagefile = new Pagefile<ExamineManagerDTO>();
					if (pf != null) {
						pagefile.setRealcount(pf.getRealcount());
						List<WeiboTextResult> tr = pf.getRows();
						for (WeiboTextResult w : tr) {
							ExamineManagerDTO em = new ExamineManagerDTO();
							em.tid = w.getTid();
							String contentTxt = w.getText();
							if (contentTxt.length() > 30) {
								em.content = contentTxt.substring(0, 30) + "...";
							} else {
								em.content = contentTxt;
							}
							em.date = DateUtils.formatDateString(w.getCreated());
							em.forwardNum = w.getReposts();
							em.reviewNum = w.getComments();
							em.organizationNum = w.getOrgrans();
							pagefile.getRows().add(em);
						} // for
					} //  
					return pagefile;
		   	 }

		   	 
		   	 public       Pagefile<MyPostDTO>     getFormatNumList(String uid, String  tid, Platform platform, Pager  pag, User  usr){
				Result<Pagefile<ITweet>> result = AppServerProxy.myHomePageViewRetweets(uid, tid, platform, pag, usr);
				Pagefile<MyPostDTO> collection = new Pagefile<MyPostDTO>();
				if (result != null) {
					if (result.getErrCode() == 0) {
						Pagefile<ITweet> pw = result.getData();
						if (pw != null) {
							// 转成前台用的
							List<ITweet> posts = pw.getRows();
							if (posts != null) {
								collection.setRealcount(pw.getRealcount());
								collection.setErr(pw.getErr());
								for (ITweet post : posts) {
									MyPostDTO entity = new MyPostDTO();
									// 账号
									if (post.getAuthor() != null) {
										entity.account.uid = post.getAuthor()
												.getUcode();
										entity.account.ucode = post.getAuthor()
												.getUcode();
										entity.account.twitterType = Utility.getPlatform(post.getAuthor().getPlatform());
										// 如果类型是腾讯，并且头像路径不为null
										if (post.getPlatform() == Platform.Tencent
												&& post.getAuthor().getHead() != null) {
											entity.account.imgurl = PortalCST.IMAGE_PATH_PERFIX
													+ post.getAuthor().getHead();
											entity.account.imgurlBig = PortalCST.IMAGE_PATH_PERFIX
													+ post.getAuthor()
															.getLargeHead();
										} else {
											entity.account.imgurl = post
													.getAuthor().getHead();
											entity.account.imgurlBig = post
													.getAuthor().getLargeHead();
										}

										entity.account.url = post.getAuthor()
												.getHomeUrl();
										if (post.getAuthor().getName() != null) {
											entity.account.name = post.getAuthor()
													.getName();
										}

										// entity.account.isAjax = false;
										entity.account.friends = post.getAuthor()
												.getFriends(); // 关注
										entity.account.weibos = post.getAuthor()
												.getWeibos(); // 发布微博数
										entity.account.followers = post.getAuthor()
												.getFollowers();// 粉丝数
										entity.account.summany = post.getAuthor()
												.getDescription();
									}

									// 图片
									if (post.getImages() != null) {
										for (String imgPath : post.getImages()) {
											entity.postImage
													.add(PortalCST.IMAGE_PATH_PERFIX
															+ imgPath);
										}
									}
									if (post.getSource() == null) {
										entity.isCreative = true;
									} else {
										entity.isCreative = false;
									}
									entity.id = (int) post.getId();// PostID
									entity.tid = post.getTid();
									entity.url = post.getUrl(); // 链接
									entity.text = post.getText().replace(
											entity.account.name + "：", ""); // 内容
									entity.createtime = DateHelper.formatDate(post
											.getCreated());// .getCreateTime();
									if (post.getFrom() != null) {
										entity.source = post.getFrom().getName();// Utility.getFrom(p.getFrom().getId());
									} else {
										entity.source = "未知来源";
									}

									// 来源
									entity.repostCount = post.getReposts();// .getRepostCount();
																			// //
																			// 转发数
									entity.commentCount = post.getComments();// .getCommentCount();
									// 评论数
									entity.item = null;
									// 转发类型
									if (post.getSource() != null) {
										entity.item = new MyPostDTO();
										ITweet subPost = post.getSource();// post.getRepostList();
										// 账号
										AccountDTO adto = new AccountDTO();
										// adto.id = (int)
										// subPost.getAuthor().getId();

										// 获取图片路径
										if (subPost.getPlatform() == Platform.Tencent
												&& subPost.getAuthor().getHead() != null) {
											adto.imgurl = PortalCST.IMAGE_PATH_PERFIX
													+ subPost.getAuthor().getHead();
										}
										adto.name = subPost.getAuthor().getName();
										entity.item.account = adto;

										entity.item.id = (int) subPost.getId();
										entity.item.tid = subPost.getTid();
										entity.item.url = subPost.getUrl();
										entity.item.text = subPost.getText();
										entity.item.createtime = DateHelper
												.formatDate(subPost.getCreated());// .getCreateTime();
										if (subPost.getFrom() != null) {
											entity.item.source = subPost.getFrom()
													.getName();
										} else {
											entity.item.source = "未知来源";
										}
										entity.item.repostCount = subPost
												.getReposts();// .getRepostCount();
										entity.item.commentCount = subPost
												.getComments();// .getCommentCount();
										// 图片
										if (subPost.getImages() != null) {
											for (String imgPath : subPost
													.getImages()) {
												if (subPost.getPlatform() == Platform.Tencent) {
													entity.item.postImage
															.add(PortalCST.IMAGE_PATH_PERFIX
																	+ imgPath);
												} else {
													entity.item.postImage
															.add(imgPath);
												}
											}
										}
									}
									collection.getRows().add(entity);
								}
							}

						} // if
					}
				}
				return collection;
		    	
		    }

		   	 
		   	 
		   	 public      Pagefile<ExamineManagerDTO>       getRepostsPageNumList(String uid,String tid,Platform  platform,Pager pag, User usr){
					Pagefile<ExamineManagerDTO> pagefile = new Pagefile<ExamineManagerDTO>();
					Result<Pagefile<ITweet>> result = AppServerProxy.myHomePageViewComments(uid, tid, platform, pag, usr);
					if (result != null) {
						if (result.getErrCode() == 0) {
							Pagefile<ITweet> pf = result.getData();
							if (pf != null) {
								pagefile.setRealcount(pf.getRealcount());
								pagefile.setErr(pf.getErr());
								List<ITweet> posts = pf.getRows();
								for (ITweet post : posts) {
									ExamineManagerDTO mpt = new ExamineManagerDTO();
									IAccount acc = post.getAuthor();// resa.getData();
									if (acc != null) {
										if (acc.getPlatform() == Platform.Tencent
												&& acc.getHead() != null) {// 微博头像
											mpt.account.imgurl = PortalCST.IMAGE_PATH_PERFIX
													+ acc.getHead();
											mpt.account.imgurlBig = PortalCST.IMAGE_PATH_PERFIX
													+ acc.getHead();
										} else {
											mpt.account.imgurl = acc.getHead();
											mpt.account.imgurlBig = acc.getHead();
										}
										mpt.account.name = acc.getName(); // 微博帐号名称
										mpt.account.url = acc.getHomeUrl();
										mpt.account.ucode = acc.getUcode();
										mpt.account.weibos = acc.getWeibos(); // 微博数
										mpt.account.followers = acc.getFollowers(); // 粉丝数
										mpt.account.friends = acc.getFriends(); // 关注数
										mpt.account.summany = acc.getDescription(); // 微博简介
										// mpt.account.isAjax = false;
									}
									// }// code 0
									// }
									if (post.getSource() != null) {
										mpt.posttext = post.getSource().getText(); // 微博内容
									} else {
										mpt.posttext = "";
									}
									if (post.getOriginalTweet() != null) {
										mpt.sourceID = post.getOriginalTweet().getTid(); // 原始微博ID
										mpt.comtorsURL = post.getOriginalTweet()
												.getUrl(); // 微博内容或者评论链接
									} else if (post.getSource() != null) {
										mpt.comtorsURL = post.getSource().getUrl();
									}
									mpt.dealwithCommentsID = post.getTid();
									mpt.content = "：" + post.getText(); // 评论内容
									mpt.createtime = DateHelper.formatDate(post
											.getCreated());// 发布时间
									// Utility.getFrom(post.getFrom().getId()); //来源

									if (post.getFrom() != null)
										mpt.source = post.getFrom().getName();
									else {
										mpt.source = "未知来源";
									}
									pagefile.getRows().add(mpt);

								} // for 结束
							}

						} // code 0 正确
					}
					return pagefile;

		   	 }
		   	 
		   	 public  	Pagefile<OrganizationDTO>          getForwardOrgNumList(User  usr, Pager pg, String  nid){
					Pagefile<RepostOrganizationResult> pf = AppServerProxy.queryRODetail(usr, pg, nid);
					Pagefile<OrganizationDTO> pagefile = new Pagefile<OrganizationDTO>();
					if (pf != null) {
						pagefile.setRealcount(pf.getRealcount());
						List<RepostOrganizationResult> Navs = pf.getRows();
						for (RepostOrganizationResult na : Navs) {
							OrganizationDTO odto = new OrganizationDTO();
							odto.orgName = na.getName();
							Platform plat = na.getPlatform();
							if (plat == Platform.Sina) {
								odto.platform = 1;
							} else if (plat == Platform.Tencent) {
								odto.platform = 2;
							} else if (plat == Platform.Renmin) {
								odto.platform = 5;
							} else {
								odto.platform = 1;
							}
							pagefile.getRows().add(odto);
						}
					}
					return pagefile;
		   	 }
		   	 
		   	 public     ExamineManagerDTO    getLookAllContent( User  usr , String tid, Platform  platform){
					ITweet it = AppServerProxy.queryITweetByTid(usr, tid, platform);
					ExamineManagerDTO dto = new ExamineManagerDTO();
					dto.content = it.getText();
					List<String> imgs = it.getImages();
					// themes/modules/images/noPhone.gif
					if (imgs != null) {
						for (String img : imgs) {
							dto.imageUrls.add(img);
						}
					}
					return dto;
		   	 }
		   	 
		   
			// *****************************************************所有微博内容统计结束
		   
	@Override
	public Object doAction() {
		String queryStr = this.get("tempList");
		int userid = this.getUser().getBaseUser().getId();
		int customId = this.getUser().getBaseUser().getCustomID();
		PagerDTO pagedto = new PagerDTO();
		Pager  pager=new Pager();
		if(this.get("pageIndex")!=null &&this.get("pageSize")!=null){
			pagedto.pageIndex = Integer.parseInt(this.get("pageIndex"));
			pagedto.pageSize = Integer.parseInt(this.get("pageSize"));
			pager.setPageIndex( Integer.parseInt(this.get("pageIndex")));
			pager.setPageSize(Integer.parseInt(this.get("pageSize")));
		}
		String stime="";
		String  etime="";
		if(this.get("starttime")!=null && this.get("endtime")!=null){
			 stime = this.get("starttime") + " 00:00:00";
			 etime = this.get("endtime") + " 23:59:59";
		}
	   User  baseUser=this.getUser().getBaseUser();
		
		if (queryStr.equals("dropdownBoxData")) {
			  return  getDropDownBox(userid);
		}
		//发布统计列表
		else if (queryStr.equals("releasecount")) {
			  return    getReleasecountList(pagedto,customId,stime,etime);
		}
		//发布统计列表中的提交 or 采纳内部列表
		else 	if (queryStr.equals("relaseSumbitNum")) {
			  int type = Integer.parseInt(this.get("type"));
			  int cuid = Integer.parseInt(this.get("cuid"));
			  return  getReleaseSumbitNumList(dao, userDao,pager,type,cuid,stime,etime,"relaseSumbitNum","",-1);
		}
		//处理统计列表
		else if(queryStr.equals("dealwithcount")){
			  String uid = this.get("UID");
			  return getDealWithCountList(sendDao,pager ,uid,customId,stime,etime);
		}
		//处理统计列表中的转发数内部页面
		else if(queryStr.equals("dealwithRePostAll")){
			int type = Integer.parseInt(this.get("type"));
			String uid = this.get("uid");
			int curuserid = Integer.parseInt(this.get("curuserid"));
			// 微博平台类型
			int platforms = Integer.parseInt(this.get("platformType"));
			return   getDealWithRePostAll(dao, userDao,pager,type,-1,stime,etime,uid,curuserid);
		}
		//处理统计列表中的评论数内部页面
		else if(queryStr.equals("dealwithCommentsAll")){
			int type = Integer.parseInt(this.get("type"));
			String uid = this.get("uid");
			// 微博平台类型
			int platforms = Integer.parseInt(this.get("platformType"));
			int curuserid = Integer.parseInt(this.get("curuserid"));
			return  getDealWithCommentsAll(dao, userDao,pager,type,-1,stime,etime,uid,curuserid);
		}
		//网评员列表
		else  if(queryStr.equals("commentatorscount")){
			long starttime = DateUtils.dateStringToInteger(stime);
			long endtime = DateUtils.dateStringToInteger(etime);
			return  getCommentatorscountList(baseUser,starttime,endtime,pager);
		}
		//全部网评员的那个超级连接按钮 获取网评员统计模块的添加按钮所弹出的层的表格列表
		else if(queryStr.equals("commentatorsAlertTable")){
			return  getCommentatorsAlertTable(navieDao,pagedto,customId);
		}
		//添加网评员统计模块的添加按钮所弹出的层的当前输入的 网评员下的用管理的微博帐号的列表
		else if(queryStr.equals("CommentatorsAddList")){
			String name = this.get("name");
			return   getCommentatorsList(name,customId);
		}
		// 微博网评员统计中-----网评员账号个数(显示的是此帐号下弹出层修改)事件 t2 class
		else if(queryStr.equals("everyModityCommtors")){
			String name = this.get("name");
			return  getCommentatorsList(name,customId);
		}
		//获取网评员统计列表中的--总转评到我的个数的table列表
		else if(queryStr.equals("commentatorsToMeNum")){
			long starttime = DateUtils.dateStringToInteger(stime);
			long endtime = DateUtils.dateStringToInteger(etime);
			int nid = Integer.parseInt(this.get("nid"));
			int ecselValue = Integer.parseInt(this.get("selectValueEC")); //选中的是转发 0还是评论1
			WeiboType wt;
			if (ecselValue == 0) {
				wt = WeiboType.RepostPost;
			} else {
				wt = WeiboType.CommentPost;
			}
			return  getCommentatorsToMeNum(baseUser,  pager,nid,starttime,endtime,wt);
		}
		//获取微博内容统计列表
		else if(queryStr.equals("contentcount")){
			String uid = this.get("UID");
			long sti = DateUtils.dateStringToInteger(stime);
			long eti = DateUtils.dateStringToInteger(etime);
			return   getContentcountList( baseUser,pager,sti,eti,uid);
		}
		//微博内容统计中的转发数获取的信息页面
		else if(queryStr.equals("formatNumPage")){
			String uid = this.get("uid");
			String tid = this.get("tid");
			int plat = Integer.parseInt(this.get("platform"));
			Platform platform;
			if (plat == 1) {
				platform = Platform.Sina;
			} else if (plat == 2) {
				platform = Platform.Tencent;
			} else if (plat == 5) {
				platform = Platform.Renmin;
			} else {
				platform = Platform.Sina;
			}
			int total = Integer.parseInt(this.get("thisNum"));
			Pager pag = new Pager();
			pag.setPageIndex(Integer.parseInt(this.get("pageIndex")));
			pag.setPageSize(Integer.parseInt(this.get("pageSize")));
			pag.setRealCnt(total);
			return   getFormatNumList(uid, tid, platform, pag, baseUser);
		}
		//微博内容统计中的评论数获取的信息页面
		else if(queryStr.equals("RepostsPageNum")){
			String uid = this.get("uid");
			String tid = this.get("tid");
			int plat = Integer.parseInt(this.get("platform"));
			Platform platform;
			if (plat == 1) {
				platform = Platform.Sina;
			} else if (plat == 2) {
				platform = Platform.Tencent;
			} else if (plat == 5) {
				platform = Platform.Renmin;
			} else {
				platform = Platform.Sina;
			}

			int total = Integer.parseInt(this.get("thisNum"));
			Pager pag = new Pager();
			pag.setPageIndex(Integer.parseInt(this.get("pageIndex")));
			pag.setPageSize(Integer.parseInt(this.get("pageSize")));
			pag.setRealCnt(total);
			return  getRepostsPageNumList(uid, tid, platform, pag, baseUser);
		}
		//微博内容统计中的转发机构数的信息列表
		else if(queryStr.equals("forwardOrgNum")){
			// nid 就是tid
			String nid = this.get("NID");
			return   getForwardOrgNumList(baseUser, pager,nid );
		}
		//微博内容统计列表中点击内容显示具体详情的业务逻辑
		else if(queryStr.equals("lookAllContent")){
			String tid = this.get("contentID");
			int plat = Integer.parseInt(this.get("platform"));
			Platform platform;
			if (plat == 1) {
				platform = Platform.Sina;
			} else if (plat == 2) {
				platform = Platform.Tencent;
			} else if (plat == 5) {
				platform = Platform.Renmin;
			} else {
				platform = Platform.Sina;
			}
			return     getLookAllContent(baseUser,tid,platform);
		}

			return null;
	}

	
	
}
