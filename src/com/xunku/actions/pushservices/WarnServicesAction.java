package com.xunku.actions.pushservices;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;

import com.xunku.actions.ActionBase;
import com.xunku.app.Utility;
import com.xunku.app.controller.WarningController;
import com.xunku.app.enums.Platform;
import com.xunku.app.enums.WarnStatusEnum;
import com.xunku.app.helpers.DateHelper;
import com.xunku.app.interfaces.IAccount;
import com.xunku.app.interfaces.ITweet;
import com.xunku.app.result.Result;
import com.xunku.constant.PortalCST;
import com.xunku.constant.WarnStatus;
import com.xunku.constant.WarnType;
import com.xunku.dao.office.WarnListDao;
import com.xunku.daoImpl.office.WarnListDaoImpl;
import com.xunku.dto.AccountDTO;
import com.xunku.dto.MyPostDTO;
import com.xunku.dto.pushservices.WarnServicesDTO;
import com.xunku.pojo.base.Pager;
import com.xunku.pojo.base.User;
import com.xunku.pojo.office.AccountWarn;
import com.xunku.pojo.office.EventWarn;
import com.xunku.pojo.office.WarnElement;
import com.xunku.pojo.office.WeiboWarn;
import com.xunku.utils.AppServerProxy;
import com.xunku.utils.Pagefile;


/**
 * 预警服务--预警通知 列表展示action类
 * @author shangwei
 *
 */
public class WarnServicesAction   extends   ActionBase {
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	WarningController warncontro=new WarningController();
	
	/**
	* 预警信息的帐号监控列表方法
	*/
	 	public   Pagefile<WarnServicesDTO>   getAccountWarning(Pagefile<WarnElement>  warnElementPagefile,int pageIndex){
	 			Pagefile<WarnServicesDTO>  pagefile= new Pagefile<WarnServicesDTO>();
	 			if(warnElementPagefile!=null){
	 				pagefile.setRealcount(warnElementPagefile.getRealcount());
	 				List<WarnElement>  warns=warnElementPagefile.getRows();
	 				for(int i=0;i<warns.size();i++){
	 					WarnServicesDTO  dto=new WarnServicesDTO();
	 					  dto.autoId=(pageIndex-1)*10+(i+1);
				 		  dto.id=warns.get(i).getId();
				 		  dto.readed=warns.get(i).getReaded();
	 					  //dto.platformStr=acctWarns.get(i).getAccid()+"新浪";
				 		 //dto.platformStr="新浪";
	 					  //dto.weiboTitleName="[ "+acctWarns.get(i).getAccid()+" ]";
	 					  //dto.weiboTitleName="[ "+acctWarns.get(i).getGroupName()+" ]";
				 		 dto.weiboTitleName=warns.get(i).getText();
	 					  //dto.differentLanguages="帐号博主在微博中提到了您设置的关键词:"+ acctWarns.get(i).getKeyword();
	 					  dto.warnServicesType="NoshowKeys com_none";
	 					  pagefile.getRows().add(dto);
	 				} // for 	List<AccountWarn>   结束
	 				
	 			} // accWarnPagefile !=null
		
	 	   return  pagefile;
	 }
	 	
	 
			/**
	 		*事件监控列表
			*/
	 				
		public   Pagefile<WarnServicesDTO>	getEventWarning(Pagefile<WarnElement>  warnElementPagefile ,int pageIndex){
				Pagefile<WarnServicesDTO>  pagefile= new Pagefile<WarnServicesDTO>();
				if(warnElementPagefile!=null){
					pagefile.setRealcount(warnElementPagefile.getRealcount());
					List<WarnElement>  eventwarns=warnElementPagefile.getRows();
					for(int i=0;i<eventwarns.size();i++){
						WarnServicesDTO  dto=new WarnServicesDTO();
	 					  dto.autoId=(pageIndex-1)*10+(i+1);
				 		  dto.id=eventwarns.get(i).getId();
				 		  dto.readed=eventwarns.get(i).getReaded();
				 		  //dto.platformStr=eventwarns.get(i).getEventid()+"新浪";
					 	  //dto.platformStr="新浪";
					 	  //dto.weiboTitleName="\""+eventwarns.get(i).getEventName()+"\"";
				 		 dto.weiboTitleName=eventwarns.get(i).getText();
					 	  //dto.differentLanguages="事件发生预警，微博数量已经超过"+eventwarns.get(i).getWeibo();
					 	  dto.warnServicesType="NoshowEvent com_none";
					 	  pagefile.getRows().add(dto);
					} // for
					
				} //eventWarnPagefile !=null
				
				return pagefile;
		}
	 		
		
		/**
		 * 微博预警列表
		 * */
		public      Pagefile<WarnServicesDTO>     getWeiboWarning(Pagefile<WarnElement>  warnElementPagefile,int pageIndex,User  curUser){
			          Pagefile<WarnServicesDTO>  pagefile= new Pagefile<WarnServicesDTO>();
			          if(warnElementPagefile!=null){
			        	    pagefile.setRealcount(warnElementPagefile.getRealcount());
			        	     List<WarnElement>  weiBoWarns=warnElementPagefile.getRows();
			        	     for(int i=0;i<weiBoWarns.size();i++){
			        	    	 WarnServicesDTO  dto=new WarnServicesDTO();
			 					  dto.autoId=(pageIndex-1)*10+(i+1);
						 		  dto.id=weiBoWarns.get(i).getId();
						 		  dto.readed=weiBoWarns.get(i).getReaded();
						 		  String strs=weiBoWarns.get(i).getText();
						 		  dto.weiboTitleName=strs;
						 		  /*
						 		  if(strs.length()>15){
							 		  dto.weiboTitleName=strs.substring(0, 15);
						 		  }else {
						 			  dto.weiboTitleName=strs;
						 		  }
						 		  */
							 	  dto.warnServicesType="";
							 	  dto.postDTO=	 getWeiboALLInformation(weiBoWarns.get(i).getId(),curUser);
							 	  
							 	  pagefile.getRows().add(dto); 
			        	     } // for
			          } //  warnElementPagefile!=null
			
			         return pagefile;
		}
		
		/**
		 * 微博信息列表
		 * */
		 public     Pagefile<WarnServicesDTO>    getWeiBoInfo(Pagefile<WarnElement>  warnElementPagefile,int pageIndex,User  curUser){
		                      Pagefile<WarnServicesDTO>  pagefile= new Pagefile<WarnServicesDTO>();
		                      if(warnElementPagefile!=null){
		                    	    pagefile.setRealcount(warnElementPagefile.getRealcount());
		                    	    List<WarnElement> weiboInfos=warnElementPagefile.getRows();
		                    	    for(int i=0;i<weiboInfos.size();i++){
		                    	     	 WarnServicesDTO  dto=new WarnServicesDTO();
					 					  dto.autoId=(pageIndex-1)*10+(i+1);
								 		  dto.id=weiboInfos.get(i).getId();
								 		  dto.readed=weiboInfos.get(i).getReaded();
								 		  String strs=weiboInfos.get(i).getText();
								 		  dto.weiboTitleName=strs;
								 		  /*
								 		  if(strs.length()>15){
									 		  dto.weiboTitleName=strs.substring(0, 15);
								 		  }else {
								 			  dto.weiboTitleName=strs;
								 		  }
								 		  */
									 	  dto.warnServicesType="";
									 	  dto.postDTO=getWeiboALLInformation(weiboInfos.get(i).getId(),curUser);
									 	  pagefile.getRows().add(dto); 
		                    	    	
		                    	    } //for 结束
		                      } //warnElementPagefile!=null
		                      
		                      return pagefile;
		 }
		 
		 
		 /**
		  *  已发生预警 --- 全部的展示列表(也包括只显示帐号预警 or  微博预警 or 事件预警)
		  * */
		 public     Pagefile<WarnServicesDTO>      getALLWarning(Pagefile<WarnElement>  warnElementPagefile,int pageIndex,User  curUser){
			         Pagefile<WarnServicesDTO>  pagefile= new Pagefile<WarnServicesDTO>();
	                 if(warnElementPagefile!=null){
                 	    pagefile.setRealcount(warnElementPagefile.getRealcount());
                 	    List<WarnElement> weiboAll=warnElementPagefile.getRows();
                 	    for(int i=0;i<weiboAll.size();i++){
                 	     	 WarnServicesDTO  dto=new WarnServicesDTO();
			 					  dto.autoId=(pageIndex-1)*10+(i+1);
						 		  dto.id=weiboAll.get(i).getId();
						 		  dto.readed=weiboAll.get(i).getReaded();
						 		  //判断是微博预警 或帐号预警 或  事件预警
						 		   int  type=weiboAll.get(i).getWarnType();
						 		   dto.weiboTitleName=weiboAll.get(i).getText();
						 		   dto.postDTO=null;
						 		   //帐号预警 1   
						 		  if(type==1){
						 			 // dto.weiboTitleName=weiboAll.get(i).getText();
						 			//  dto.warnServicesType="NoshowKeys com_none";
						 		  }
						 		  //事件预警2 
						 		  else if(type==2){
						 			// dto.weiboTitleName=weiboAll.get(i).getText();
						 			// dto.warnServicesType="NoshowEvent com_none";
						 		  }
						 		  //微博预警 3
						 		  else if(type==3) {
							 		  /*
							 		   String strs=weiboAll.get(i).getText();
							 		  dto.weiboTitleName=strs;
							 		  if(strs.length()>15){
								 		  dto.weiboTitleName=strs.substring(0, 15);
							 		  }else {
							 			  dto.weiboTitleName=strs;
							 		  }
							 		  dto.warnServicesType="";
							 		  */
								 	  dto.postDTO=getWeiboALLInformation(weiboAll.get(i).getId(),curUser);
						 		  }
							 	  pagefile.getRows().add(dto); 
                 	    } //for 结束
                   } //warnElementPagefile!=null
			         return  pagefile;
		 }
		 
		 
		
		 /**
		  * 微博预警 或 微博信息里面的div 的对象
		  * (就是小眼睛里的内容)
		  * */
		 public  MyPostDTO  getWeiboALLInformation(int warnlistid,User curUser){
			 WarningController  controller=new WarningController();
			 WeiboWarn weiboWarn=controller .getWeiboWarnByMessageID(warnlistid);
			   ITweet  weet=  AppServerProxy.getWeiboByWarn(weiboWarn);
			   
				MyPostDTO mpd = new MyPostDTO();
	 				  if(weet!=null){
			 	           //------------------开始有微博信息的那个里面的div  微博信息or微博预警 结束
							//mpd.account.uid= p.getUid();
							if(weet.getAuthor()!=null){
							    mpd.account.uid= weet.getAuthor().getUcode();
								mpd.account.ucode=weet.getAuthor().getUcode();
								mpd.account.twitterType= Utility.getPlatform(weet.getAuthor().getPlatform());
								// 如果类型是腾讯，并且头像路径不为null
								if (weet.getPlatform() == Platform.Tencent
										&& weet.getAuthor().getHead() != null) {
									mpd.account.imgurl = PortalCST.IMAGE_PATH_PERFIX
											+ weet.getAuthor().getHead();
									mpd.account.imgurlBig = PortalCST.IMAGE_PATH_PERFIX
									+ weet.getAuthor().getLargeHead();
								}else{
									mpd.account.imgurl = weet.getAuthor().getHead();
									mpd.account.imgurlBig = weet.getAuthor().getLargeHead();
								}
								
								mpd.account.url = weet.getAuthor().getHomeUrl();
								if (weet.getAuthor().getName() != null) {
									mpd.account.name = weet.getAuthor().getName();
								}
								mpd.account.friends = weet.getAuthor().getFriends(); // 关注
								mpd.account.weibos = weet.getAuthor().getWeibos(); // 发布微博数
								mpd.account.followers = weet.getAuthor().getFollowers();// 粉丝数

								mpd.account.summany = weet.getAuthor().getDescription();//.replace(mpd.account.name+"：", "");
								//mpd.account.isAjax = false;
							} //getAuthor结束
							
							// 图片
							if (weet.getImages() != null) {
								for (String imgPath : weet.getImages()) {
									if (weet.getPlatform() == Platform.Tencent) {
										mpd.postImage.add(PortalCST.IMAGE_PATH_PERFIX
												+ imgPath);
									} else {
										mpd.postImage.add(imgPath);
									}
								}
							}
							if (weet.getSource() == null) {
								mpd.isCreative = true;
							} else {
								mpd.isCreative = false;
							}
							mpd.id = (int)weet.getId();// PostID
							mpd.tid = weet.getTid();
							mpd.url = weet.getUrl(); // 链接
							System.out.println(mpd.url+"   ----------" );
							mpd.text = weet.getText(); // 内容
							// 时间
							mpd.createtime = DateHelper.formatDate(weet.getCreated());// .getCreateTime();
							// 来源
							if(weet.getFrom()!=null){
								mpd.source =weet.getFrom().getName();// Utility.getFrom(p.getFrom().getId());
							}else{
								mpd.source ="未知来源";
							}
							
							mpd.repostCount = weet.getReposts();// .getRepostCount(); // 转发数
							mpd.commentCount = weet.getComments();// .getCommentCount();
							// 评论数
							mpd.item = null;
							// 转发类型
							if (weet.getSource() != null) {
								mpd.item = new MyPostDTO();
								ITweet subPost = weet.getSource();// post.getRepostList();
								// 账号
								AccountDTO adto = new AccountDTO();
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
								//mpd.item.account.name=adto.name;
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
							
							}   //微博信息or微博预警 结束  
	 				 } //weet 不为null
			   
			 return mpd;
		 }
		
		 
	@Override
	public Object doAction() {
			String queryStr=this.get("queryConditions");
			WarnListDao  warnListDao=new WarnListDaoImpl();
			int userid=this.getUser().getBaseUser().getId();
			int customid=this.getUser().getCustom().getId();
			User  curUser= this.getUser().getBaseUser();
			
			
			/**
			 *   前台的点击小眼睛，如果之前是未读的，则调
			 *   此方法，将更新数据库 readed状态为1 即为已读
			 * */
			if(queryStr.equals("readWarnList")){
				 int id =Integer.parseInt(this.get("ids"));
				 int flag=	warncontro.updateReadWarnList(userid, id);
				return flag;
			}
			
			/**
			 * 删除 一条微博通知消息的方法
			 * */
			if(queryStr.equals("deleteWarnNofity")){
				 int   warnID=Integer.parseInt(this.get("warnID"));
				return    warncontro.deleteWarnNofity(warnID) ;
			}
			
			
			
			
			/**
			 * 预先加载的
			 * */
			if(queryStr.equals("readWarnList33333333333")){
				String [] ids=this.get("ids").split(",");
				 if(ids.length>0){
					 if(ids.length==1){
						 if(!ids[0].equals("")){
							int []  intIds=new int[ids.length];
							for(int i=0;i<ids.length;i++){
							    intIds[i]=Integer.parseInt(ids[i]);
							}
							 int  num= warncontro.readWarnList(userid,intIds);
							return    num;  //"已经度过当前页的所有未读的预警的信息个数";
						 }else{
							return 0; 
						 }
					 }
					 // 大于1 的
					 else{
							int []  intIds=new int[ids.length];
							for(int i=0;i<ids.length;i++){
							    intIds[i]=Integer.parseInt(ids[i]);
							}
							 int  num= warncontro.readWarnList(userid,intIds);
							return    num;  //"已经度过当前页的所有未读的预警的信息个数";
					 }
					 
				 } // 大于0
				return   0;  // "没有没有读过的预警服务";
			}
			
			/**
			 * 预警信息的首页展示列表
			 */
			if(queryStr.equals("warnservicesShowList")){
				Pagefile<WarnServicesDTO>  pagefile= new Pagefile<WarnServicesDTO>();
				String  stimes=this.get("stimes")+" 00:00:00";
				String  etimes=this.get("etimes")+" 23:59:59";
				 //int    selectOne=Integer.parseInt(this.get("selectOne"));
				int  selectTwo=Integer.parseInt(this.get("selectTwo"));
				int pageIndex = Integer.parseInt(this.get("pageIndex"));
				int pageSize = Integer.parseInt(this.get("pageSize"));
			    //bellFlag   0   点击铃铛出来的列表  1 非点击铃铛出来的列表
				int bellFlag=Integer.parseInt(this.get("bellFlag"));
				
				Pager  pager=new  Pager();
				pager.setPageIndex(pageIndex);
				pager.setPageSize(pageSize);
				Date startDate=new Date();
				Date endDate=new Date();
				try
				{
					startDate = sdf.parse(stimes);
					endDate=sdf.parse(etimes);
				}
				catch (ParseException e)
				{
					System.out.println("转换日期失败");
				}
				
				WarnStatusEnum status=WarnStatusEnum.Warned; //1  已发生  0 未发生
				//if(selectOne==1){
			//		status=WarnStatusEnum.Warned;     
				//}
				
				WarnType  warnType=WarnType.ALL;  //0 全部  1 帐号预警 2微博预警 3事件预警   (0,3) 微博信息
				//为 非点击铃铛出来的列表
				if(bellFlag==1){
				 if(selectTwo==0){
					Pagefile<WarnElement>  warnElementPagefile=  warncontro.getWarns(curUser, pager, startDate, endDate, warnType, status);
					  return 	getALLWarning(warnElementPagefile,pageIndex,curUser);
				}
				//帐号预警
				else if(selectTwo==1){
					warnType=WarnType.AccountWarn;
					//Pagefile<AccountWarn>	 accWarnPagefile=	warncontro.getAccountWarning(customid , pager, startDate, endDate, status);
					Pagefile<WarnElement>  warnElementPagefile=  warncontro.getWarns(curUser, pager, startDate, endDate, warnType, status);
					//return 	getAccountWarning(warnElementPagefile,pageIndex);
					  return 	getALLWarning(warnElementPagefile,pageIndex,curUser);
				}
				//微博预警
				else if(selectTwo==2){
					warnType=WarnType.WeiboWarn;
					Pagefile<WarnElement>  warnElementPagefile=warncontro.getWarns(curUser, pager, startDate, endDate, warnType, status);
					//return  getWeiboWarning(warnElementPagefile,pageIndex,curUser);
					  return 	getALLWarning(warnElementPagefile,pageIndex,curUser);
				}
				//事件预警
				else if(selectTwo==3){
					warnType=WarnType.EventWarn;
					//Pagefile<EventWarn> eventWarnPagefile= warncontro.getEventWarning(customid, pager, startDate, endDate, status);
					Pagefile<WarnElement>  warnElementPagefile =warncontro.getWarns(curUser, pager, startDate, endDate, warnType, status);
					//return  getEventWarning(warnElementPagefile,pageIndex);
					  return 	getALLWarning(warnElementPagefile,pageIndex,curUser);
				}
				//微博信息
				else if(selectTwo==4){
					warnType=WarnType.WeiboInfo;
					Pagefile<WarnElement>  warnElementPagefile =warncontro.getWarns(curUser, pager, startDate, endDate, warnType, status);
					return   getWeiBoInfo(warnElementPagefile,pageIndex,curUser);
				}
			}
				//点击铃铛 bellFlag==0
			 else if(bellFlag==0){
				 Pagefile<WarnElement>  warnElementPagefile = warncontro.getWarns(curUser, pager);
				  return 	getALLWarning(warnElementPagefile,pageIndex,curUser);
			}
				
				//----------------------------
				 Pagefile<WarnElement>   pf=	null;//warnListDao.queryWarnListPagefile(pager, customid, startDate, endDate, warnType, status);
				 if(pf!=null){
					 	pagefile.setRealcount(pf.getRealcount());
					 	List<WarnElement>  warns=pf.getRows();
					 	for(int i=0;i<warns.size();i++){
					 		WarnServicesDTO    dto=new WarnServicesDTO();
					 		  dto.autoId=(pageIndex-1)*10+(i+1);
					 		  dto.id=warns.get(i).getId();
					 		  Platform  platform=Platform.Sina;//warns.get(i).getPlatform();
					 		  if(platform==Platform.Sina){
					 			  dto.platformStr="新浪";
					 		  }else if(platform==Platform.Tencent){
					 			 dto.platformStr="腾讯";
					 		  }else if(platform==Platform.Renmin){
					 			 dto.platformStr="人民";
					 		  }
					 		   int  type=warns.get(i).getWarnType();
					 
					 		   Result<ITweet> result=   AppServerProxy.getPostByTid("", Platform.Sina,curUser );
					 		   if(result!=null){
					 			   if(result.getErrCode()==0){
					 				  ITweet   weet= result.getData();
					 				  if(weet!=null){
							 		  if(type==1 ||type==2){
							 				IAccount acc=  weet.getAuthor();
							 			    if(acc!=null){
							 			    	 dto.weiboTitleName="["+acc.getName()+"]";
							 			    }else{
							 			    	 dto.weiboTitleName="此帐号不存在";	
							 			    }
							 			    dto.postDTO=null;
							 		  }else {
							 	           if(weet.getText().length()>15){
							 	        	  dto.weiboTitleName=weet.getText().substring(0,15)+"...";
							 	           }else{
							 	        	  dto.weiboTitleName=weet.getText();
							 	           }
							 	           //------------------开始有微博信息的那个里面的div  微博信息or微博预警 结束

											MyPostDTO mpd = new MyPostDTO();
											//mpd.account.uid= p.getUid();
											if(weet.getAuthor()!=null){
											    mpd.account.uid= weet.getAuthor().getUcode();
												mpd.account.ucode=weet.getAuthor().getUcode();
												mpd.account.twitterType= Utility.getPlatform(weet.getAuthor().getPlatform());
												// 如果类型是腾讯，并且头像路径不为null
												if (weet.getPlatform() == Platform.Tencent
														&& weet.getAuthor().getHead() != null) {
													mpd.account.imgurl = PortalCST.IMAGE_PATH_PERFIX
															+ weet.getAuthor().getHead();
													mpd.account.imgurlBig = PortalCST.IMAGE_PATH_PERFIX
													+ weet.getAuthor().getLargeHead();
												}else{
													mpd.account.imgurl = weet.getAuthor().getHead();
													mpd.account.imgurlBig = weet.getAuthor().getLargeHead();
												}
												
												mpd.account.url = weet.getAuthor().getHomeUrl();
												if (weet.getAuthor().getName() != null) {
													mpd.account.name = weet.getAuthor().getName();
												}
												mpd.account.friends = weet.getAuthor().getFriends(); // 关注
												mpd.account.weibos = weet.getAuthor().getWeibos(); // 发布微博数
												mpd.account.followers = weet.getAuthor().getFollowers();// 粉丝数

												mpd.account.summany = weet.getAuthor().getDescription();//.replace(mpd.account.name+"：", "");
												//mpd.account.isAjax = false;
											} //getAuthor结束
											
											// 图片
											if (weet.getImages() != null) {
												for (String imgPath : weet.getImages()) {
													if (weet.getPlatform() == Platform.Tencent) {
														mpd.postImage.add(PortalCST.IMAGE_PATH_PERFIX
																+ imgPath);
													} else {
														mpd.postImage.add(imgPath);
													}
												}
											}
											if (weet.getSource() == null) {
												mpd.isCreative = true;
											} else {
												mpd.isCreative = false;
											}
											mpd.id = (int)weet.getId();// PostID
											mpd.tid = weet.getTid();
											mpd.url = weet.getUrl(); // 链接
											System.out.println(mpd.url+"   ----------" );
											mpd.text = weet.getText(); // 内容
											// 时间
											mpd.createtime = DateHelper.formatDate(weet.getCreated());// .getCreateTime();
											// 来源
											if(weet.getFrom()!=null){
												mpd.source =weet.getFrom().getName();// Utility.getFrom(p.getFrom().getId());
											}else{
												mpd.source ="未知来源";
											}
											
											mpd.repostCount = weet.getReposts();// .getRepostCount(); // 转发数
											mpd.commentCount = weet.getComments();// .getCommentCount();
											// 评论数
											mpd.item = null;
											// 转发类型
											if (weet.getSource() != null) {
												mpd.item = new MyPostDTO();
												ITweet subPost = weet.getSource();// post.getRepostList();
												// 账号
												AccountDTO adto = new AccountDTO();
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
											
											}   //微博信息or微博预警 结束
							 		  } //else  结束  
					 				 } //weet 不为null
					 				  else {
					 					 dto.postDTO=null;
					 					 dto.weiboTitleName="此帐号或此微博已经不存在";
					 				  }
					 			   } // code ==0
					 			  else {
					 				     dto.postDTO=null;
					 					 dto.weiboTitleName="此帐号或此微博已经不存在";
					 				  }
					 		   } //result 不为null   
					 		   dto.differentLanguages=warns.get(i).getText();
					 		   
					 		   if(type==1||type==2){
					 			   dto.warnServicesType="";
					 		   }else  {
					 			  dto.warnServicesType="Noshow";
					 		   }
					 		   pagefile.getRows().add(dto);
					 	} //for结束
					 
				 }
		//----------------------------------------------		
				
				/**
				// 假数据
				pagefile.setRealcount(30);
				for(int i=0;i<10;i++){
					WarnServicesDTO    dto=new WarnServicesDTO();
					
					 dto.autoId=(pageIndex-1)*10+(i+1);
					 dto.id=new Random(100).nextInt();
					 if(i%2==0&&i!=9){
						 dto.platformStr="新浪";
						 dto.weiboTitleName="[爱情保卫战]";
						 dto.differentLanguages="账号博主在微博中提到了您设置的关键词";
						 dto.warnServicesType="";
					 }else  if(i%2==1&&i!=9){
						 dto.platformStr="腾讯";
						 dto.weiboTitleName=" “MM”";
						 dto.differentLanguages="事件发生预警，微博数量已经超过100";
						 dto.warnServicesType="warnservicesHideEye";
					 }else{
						 dto.platformStr="新浪";
						 dto.weiboTitleName="微博“iphone6马上已经在大陆发售...”";
						 dto.differentLanguages="发生预警，转发/评论已超过110。";
						 dto.warnServicesType="";
					 }
					 pagefile.getRows().add(dto);
				 } //for 
				 	return  pagefile;
				// 假数据结束
				*/
				return  pagefile;
			}
			
			
			
			
			
			
			
			
			
			
			
			
			
			//--------------------------------备份
			if(queryStr.equals("warnservicesShowList2")){
				String stime=this.get("stimes")+" 00:00:00";
				String etime=this.get("etimes")+" 23:59:59";
				int   condtion=Integer.parseInt(this.get("cond"));   //0 全部  1 帐号预警 2 微博预警 3 事件预警
				int pi= Integer.parseInt(this.get("pageIndex"));
				int pz=Integer.parseInt(this.get("pageSize"));
				//调后台指定的dao的方法
				//+++++++++++++++++++++++++++++++++++++++
				
				Pagefile<WarnServicesDTO>  pagefile=new Pagefile<WarnServicesDTO>();
				
				//+++++++++++++++++++++++++++++++++++++++
				
				
				//*****************
				 pagefile.setRealcount(33);
				 for(int i=0;i<10;i++){
					 WarnServicesDTO  dto=new WarnServicesDTO();
					  dto.id=22;
					  dto.autoId=(pi-1)*10+(i+1);
					 if(i%2==0){
						 dto.weiboAccountName="爱情保卫战";
						 dto.platformStr="新浪";
					 } else{
						 dto.platformStr="腾讯";
						 dto.weiboAccountName="Twiter";
					 }
					 
					 dto.text="你好，屌丝。哈哈，Left  4 Dead 2 非常好玩啊，是吗我的小伙伴们？屌丝,哈哈哈, 小伙伴，伙伴";
					 dto.ymd="2014-08-31";
					 dto.hm="23:11";
				 	 dto.fileds="屌丝 小伙伴";   // 屌丝
					 //dto.fileds="Left 4 Dead 2";
				 	 dto.tid="3744843475492303";   //tid 帖子ID
				 	 //dto.url="http://www.baidu.com";
				 	 
				 	 dto.platform=1;
				 	 
					 pagefile.getRows().add(dto);
				 }
				//*************************
				return pagefile;
			}
			
		return null;
	}

}
