package com.xunku.actions.coordination;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.xunku.actions.ActionBase;
import com.xunku.app.Utility;
import com.xunku.app.enums.Platform;
import com.xunku.app.enums.PostType;
import com.xunku.app.helpers.DateHelper;
import com.xunku.app.interfaces.ITweet;
import com.xunku.constant.PortalCST;
import com.xunku.constant.WeiboType;
import com.xunku.dao.base.AccountDao;
import com.xunku.dao.base.OrganizationsDao;
import com.xunku.daoImpl.base.AccountDaoImpl;
import com.xunku.daoImpl.base.OrganizationsDaoImpl;
import com.xunku.dto.AccountDTO;
import com.xunku.dto.MyPostDTO;
import com.xunku.dto.PagerDTO;
import com.xunku.dto.coordination.DepartmentManagerDTO;
import com.xunku.dto.coordination.DropdownBoxDataDTO;
import com.xunku.dto.sysManager.AccountVO;
import com.xunku.pojo.base.Organization;
import com.xunku.pojo.base.Pager;
import com.xunku.pojo.base.User;
import com.xunku.utils.AppServerProxy;
import com.xunku.utils.DateUtils;
import com.xunku.utils.Pagefile;

/**
 * 协同办公--机构管理action类
 * @author shangwei
 *协同办公--机构管理列表查看功能action类
 */
public class DepartmentManagerAction  extends ActionBase{

	OrganizationsDao  dao=new OrganizationsDaoImpl();
	
	@Override
	public Object doAction() {
		// TODO Auto-generated method stub
		String queryStr=this.get("queryConditions");
		if(queryStr.equals("queryAll")){
			/**Author  ShangWei
			 * * Description  协同办公下的机构管理查询列表功能
			 */
		      int userid=this.getUser().getBaseUser().getId();
		      int customid=this.getUser().getCustom().getId();
			  int pi=Integer.parseInt(this.get("pageIndex"));
			  int pz=Integer.parseInt(this.get("pageSize"));
			  int  plat=Integer.parseInt(this.get("KindType"));
			 // String  stime=this.get("startTime")+" 00:00:00";
			  //String  etime=this.get("endTime")+" 23:59:59";
			//  long stime=DateUtils.dateStringToInteger(this.get("startTime")+" 00:00:00");
			 // long etime=DateUtils.dateStringToInteger(this.get("endTime")+" 23:59:59");
			  
			  // DateUtils.stringToDate("yyyy-MM-dd 00:00:00",nowtime);
			  Date  stime= DateUtils.stringToDate("yyyy-MM-dd HH:mm:ss",this.get("startTime")+" 00:00:00");
			  Date  etime=DateUtils.stringToDate("yyyy-MM-dd HH:mm:ss",this.get("endTime")+" 23:59:59");
			  
			  
			  User  user=this.getUser().getBaseUser();
			  
			  int sqlPlat=1;
			  if(plat==1){
				  sqlPlat=2;
			  }else if(plat==2){
				  sqlPlat=5;
			  }
			  String currUid=this.get("uid");
			  Pager  pager= new Pager();
			   pager.setPageIndex(pi);
			   pager.setPageSize(pz);

			  Pagefile<Organization>  pfO =AppServerProxy.orgList(customid, pager, stime, etime, currUid);
			  
			  //转换
			  Pagefile<DepartmentManagerDTO>  pf=new Pagefile<DepartmentManagerDTO>();
			  if(pfO!=null){
			  pf.setRealcount(pfO.getRealcount());
			  List<Organization>  orgs=pfO.getRows();
			  for(Organization  org : orgs){
				  DepartmentManagerDTO  dm=new DepartmentManagerDTO();
				  dm.id=org.getId();
				  dm.departmentName=org.getName();
				  dm.orguid=org.getUid();
				  //String  uid=org.getUid();
				//  dm.weiboNum= dao.queryWeibos(uid,sqlPlat,stime,etime);
				  dm.weiboNum=org.getRetweets();
					  //AppServerProxy.queryOrganWeibos(user, uid, stime, etime);
				  //dm.toMeNum=dao.queryMyMentions(uid,sqlPlat,stime,etime);
				  dm.toMeNum=org.getComments();
					  //AppServerProxy.queryMyMentions(user, uid, stime, etime);
				  pf.getRows().add(dm);
			  }     
		}
              return  pf;
			/*
				Pagefile<DepartmentManagerDTO>  pf=new Pagefile<DepartmentManagerDTO>();
			    pf.setRealcount(100);
			  //1
			    DepartmentManagerDTO  dm=new DepartmentManagerDTO();
			    dm.id=1;
			    dm.departmentName="皇马NB";
			    dm.weiboNum=10000;
			    dm.toMeNum=999999;
			    pf.getRows().add(dm);
			    //2
			    DepartmentManagerDTO  dm2=new DepartmentManagerDTO();
			    dm2.id=2;
			    dm2.departmentName="巴萨NB";
			    dm2.weiboNum=10000;
			    dm2.toMeNum=999999;
			    pf.getRows().add(dm2);
			    //3
			    DepartmentManagerDTO  dm3=new DepartmentManagerDTO();
			    dm3.id=3;
			    dm3.departmentName="巴萨NB";
			    dm3.weiboNum=10000;
			    dm3.toMeNum=999999;
			    pf.getRows().add(dm3);
			   
		return pf;
		*/
		
	}
		if(queryStr.equals("keyUpSearch")){
			 System.out.println("----------------------------------------");
				 System.out.println(this.get("pt_select"));
				 System.out.println("===========================");
				 
				    DepartmentManagerDTO  dm1=new DepartmentManagerDTO();
				    dm1.id=1;
				    dm1.departmentName="巴萨NB";
				    DepartmentManagerDTO  dm2=new DepartmentManagerDTO();
				    dm2.id=2;
				    dm2.departmentName="恒大NB";
				    DepartmentManagerDTO  dm3=new DepartmentManagerDTO();
				    dm3.id=3;
				    dm3.departmentName="皇马NB";
				    List<DepartmentManagerDTO>  ll=new  ArrayList<DepartmentManagerDTO>();
				     ll.add(dm1);
				     ll.add(dm2);
				     ll.add(dm3);
				    return  ll;
		} 
		
		//获取下拉框里面的值
		if(queryStr.equals("dropdownBoxData")){
			int userid = this.getUser().getBaseUser().getId();
			AccountDao accdao = new AccountDaoImpl();
			List<AccountVO> list = accdao.queryAccountByUid(userid);
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
		
		/**
		 * 点击展示列表中的 转发我的 or 评论我的 然后进去显示的页面的列表
		 * @param  postType   0 为转发我的   1 为评论我的
		 * */
		if(queryStr.equals("departManerInto")){ 
			      int customid=this.getUser().getCustom().getId();
				  int pi=Integer.parseInt(this.get("pageIndex"));
				  int pz=Integer.parseInt(this.get("pageSize"));
				  Pager  pager= new Pager();
				   pager.setPageIndex(pi);
				   pager.setPageSize(pz);
				   Date  stime= DateUtils.stringToDate("yyyy-MM-dd HH:mm:ss",this.get("startTime")+" 00:00:00");
				   Date  etime=DateUtils.stringToDate("yyyy-MM-dd HH:mm:ss",this.get("endTime")+" 23:59:59");
				  
				  int postType=Integer.parseInt(this.get("postType"));
				  PostType type=PostType.Repost;
				  if(postType==1){
						type=PostType.Comment;
					}
				  String orgUid=this.get("orguid");
				  Pagefile<MyPostDTO>  collection=new Pagefile<MyPostDTO>();
				  Pagefile<ITweet>  posts=AppServerProxy.orgDetail(customid, pager, stime, etime, orgUid, type);
				  
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
								//entity.source = Utility.getFrom(post.getFrom().getId());   来源已废弃
								if (post.getFrom() != null) {
									entity.source = post.getFrom().getName();// Utility.getFrom(p.getFrom().getId());
								} else {
									entity.source = "未知来源";
								}
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
									if (type==PostType.Comment) {
										entity.url = entity.item.url; // 评论链接
									}

									entity.item.createtime = DateHelper
											.formatDate(subPost.getCreated());// .getCreateTime();
									//entity.item.source = Utility.getFrom(subPost.getFrom().getId());// subPost.getSource();  已经废弃
									if (subPost.getFrom() != null) {
										entity.item.source = subPost.getFrom()
												.getName();
									} else {
										entity.item.source = "未知来源";
									}
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
		
		
		return  null;
	}
}
