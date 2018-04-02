package com.xunku.actions.myTwitter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.xunku.actions.ActionBase;
import com.xunku.app.enums.Platform;
import com.xunku.app.enums.PostType;
import com.xunku.app.interfaces.ITweet;
import com.xunku.constant.PortalCST;
import com.xunku.constant.SendStatusEnum;
import com.xunku.dao.base.AccountDao;
import com.xunku.dao.base.UserDao;
import com.xunku.dao.my.SendingDao;
import com.xunku.daoImpl.base.AccountDaoImpl;
import com.xunku.daoImpl.base.UserDaoImpl;
import com.xunku.daoImpl.my.SendingDaoImpl;
import com.xunku.dto.PagerDTO;
import com.xunku.dto.myTwitter.PublishManagerDTO;
import com.xunku.dto.myTwitter.PublishManagerRepostDTO;
import com.xunku.dto.myTwitter.SendingDTO;
import com.xunku.dto.myTwitter.Temporary;
import com.xunku.dto.pushservices.PushUserDTO;
import com.xunku.pojo.base.AccountInfo;
import com.xunku.pojo.base.User;
import com.xunku.pojo.my.Sender;
import com.xunku.pojo.my.Sending;
import com.xunku.utils.DateUtils;
import com.xunku.utils.Pagefile;
import com.xunku.utils.PropertiesUtils;

/**
 * 我的微博--发布管理action类
 * @author shangwei
 *发布管理列表展示的action类
 */
public class MyPublicManageAction extends ActionBase {

	@Override
	public Object doAction() {
		// TODO Auto-generated method stub
		UserDao userDao = new UserDaoImpl();
		AccountDao accountDao = new AccountDaoImpl();
		SendingDao dao = new SendingDaoImpl();

		int userid = this.getUser().getBaseUser().getId();
		int customId=this.getUser().getBaseUser().getCustomID();
		String queryStr = this.get("queryConditions");
		User user = this.getUser().getBaseUser();
		//发布成功  发布失败的 两个模版
		String  failHTML="<span class=\"manageSend_style manageSend_fail\"></span>";
		String  successHTML="<span class=\"manageSend_style manageSend_sucess\"></span>";
		String  sendingHTML="<span class=\"manageSend_style manageSend_ding\"></span>";
		
		/**Author  ShangWei
		 * * Description 发布管理列表中所有已发布的提交人的数组
		 */
		if(queryStr.equals("queryBySumbitersList")){
			List<User> pushUsers=  userDao.queryUserByCid(customId);
			 if(pushUsers!=null){
				 return pushUsers;
			 }
			  return "";
		}
		
		
		/**Author  ShangWei
		 * * Description 发布管理列表展示
		 */
		if (queryStr.equals("queryListAll")) {
			//转化前台的pagefile对象
			Pagefile<PublishManagerDTO> pf2 = new Pagefile<PublishManagerDTO>();

			Pagefile<Sending> pf = new Pagefile<Sending>();
			int type = Integer.parseInt(this.get("selectStatues"));
			PagerDTO pager = new PagerDTO();
			pager.pageIndex = Integer.parseInt(this.get("pageIndex"));
			pager.pageSize = Integer.parseInt(this.get("pageSize"));
			String stime=this.get("stime")+" 00:00:00";
			String  etime=this.get("etime")+" 23:59:59";
			int  weiboType=Integer.parseInt(this.get("selOne"));
			int   selUserid=  Integer.parseInt(this.get("selTwo"));
			//我的发布
			PostType ptt=PostType.Creative;
			if (type == 0) {
				//我的发布
				//PostType ptt;
				//原创
				if(weiboType==0){
					ptt=PostType.Creative;
				}
				//转发
				else if(weiboType==1){
					ptt=PostType.Repost;
				}
				//评论
				else {
					ptt=PostType.Comment;
				}
				pf = dao.queryListByStatus(pager, userid,customId, ptt, stime, etime, 	SendStatusEnum.MySend);
			} else if (type == 1) {
				//待我审核
				pf = dao.queryListByStatus(pager, userid,
						SendStatusEnum.ApprovedToMe);
			} else if (type == 2) {
				//定时发布
				pf = dao.queryListByStatus(pager, userid,
						SendStatusEnum.AllSendIng);
			} else if (type == 3) {
				//所有已发布
				//PostType ptt;
				//原创
				if(weiboType==0){
					ptt=PostType.Creative;
				}
				//转发
				else if(weiboType==1){
					ptt=PostType.Repost;
				}
				//评论
				else {
					ptt=PostType.Comment;
				}
				pf = dao.queryListByStatus(pager,selUserid ,customId, ptt, stime, etime, SendStatusEnum.AllSend);
			} else {
				//审核失败
				pf = dao.queryListByStatus(pager, userid,
						SendStatusEnum.ApprovedFail);
			}
			/*
			 *开始封装到pf2中去
			 * 
			 */

			//pf2.setErr(pf.getErr());
			
			if(pf!=null){
			 pf2.setRealcount(pf.getRealcount());
			 List<Sending> sds = pf.getRows();
			 
		  for (Sending sd : sds) {
				PublishManagerDTO pm = new PublishManagerDTO();
				long ins = sd.getId();
				pm.ids = String.valueOf(ins);
				List<Sender> tes = sd.getSendList();
				pm.weiboAccountHTML = "";
				
				//审核人集合
				List<String>  adtors=new ArrayList<String>();
				//审核人ID集合
				List<Integer>  adids=new ArrayList<Integer>();
				//boolean flagEnd = false;
			
				//查找审核人
				pm.auditorInt = sd.getAuditor();
				User us = userDao.queryByUid(pm.auditorInt);
				if (us != null) {
					//pm.auditor = us.getUserName();
					pm.auditor = us.getNickName();
				} else {
					pm.auditor = "无";
				}
				
				int weiboAccListNum=0;
				
			if(tes!=null){
				for (int i = 0; i < tes.size(); i++) {
					Sender tey = tes.get(i);
					AccountInfo accInfo = accountDao.queryByUid(tey.getUid());
					
					if(accInfo!=null){
						//flagEnd=true;
					//	break;
						String htmlName = accInfo.getName();
						  if(accInfo.getName().length()>6){
							  htmlName=htmlName.substring(0,6);
						  }
						int pNum = tey.getPlatform();
						
						String HTML="";
						//微博发送的状态  成功  or 失败  1=未发送、2=发送成功、3=发送失败
						int   sendStaus=tey.getStatus();
						if(sendStaus==2){
							HTML=successHTML;
						}else if (sendStaus==3){
							HTML=failHTML;
						}else {
							HTML=sendingHTML;
						}
						switch (pNum) {
						case 1:
							pm.weiboAccountHTML += "<div class=\"send_photo pm_All com_fLeft\"      pmUID=\""+tey.getUid()+"\"  pmPlatform=\"1\">  "+HTML+"   </div><span class=\"send_zhao\">"
									+ htmlName + "</span>";
							break;
						case 2:
							pm.weiboAccountHTML += "<div class=\"send_photo2 pm_All  com_fLeft\"     pmUID=\""+tey.getUid()+"\"  pmPlatform=\"2\"> "+HTML+"  </div><span class=\"send_zhao\">"
									+ htmlName + "</span>";
							break;
						case 5:
							pm.weiboAccountHTML += "<div class=\"send_photo3  pm_All com_fLeft\"     pmUID=\""+tey.getUid()+"\"  pmPlatform=\"5\">   "+HTML+"</div><span class=\"send_zhao\">"
									+ htmlName + "</span>"; 
							break;
						default:
							pm.weiboAccountHTML += "<div class=\"send_photo  pm_All  com_fLeft\"    pmUID=\""+tey.getUid()+"\"  pmPlatform=\"1\"> "+HTML+"    </div><span class=\"send_zhao\">"
									+ htmlName + "</span>";
							break;
						}
						weiboAccListNum++;
					}  //if
					
				} //for 结束
				
			}
				
			//if (flagEnd)
		//	continue;
			
			
				//判断是否多于css指定的(最多显示3个还是5个帐号的),
				//多余(3个或者5个)的 要展示 + 展开按钮 ， 如果低于的 则不显示
				//if(tes.size()>5){
				//if(tes.size()>5){
				if(weiboAccListNum>5){	
				pm.weiboAccountHTML+="</div><span class=\"send_showMore\">+</span>";
				}else{
					pm.weiboAccountHTML+="</div>";
				}
				
				pm.text = sd.getText();
				if (sd.getImages() != null) {
					String[] imagas = sd.getImages().split(",");
					if(!imagas[0].equals("")){
						for (int i = 0; i < imagas.length; i++) {
							pm.text += "<img  src=\""
						//	+   PortalCST.IMAGE_PATH_PERFIX
							+ imagas[i]
									+ "\" width=\"100\" height=\"100\"/>";
						
						}
					}
				}
				//截取提交时间的字符串
				pm.submitTime = sd.getSubmit().substring(0,sd.getSubmit().lastIndexOf(":"));
				
				if(sd.getSendList()!=null){
						if(sd.getSendList().size()>0){
							int  pp =sd.getSendList().get(0).getPlatform();
							Platform  m;
						    if(pp==1){
						    	m=Platform.Sina;
						    }else if(pp==2){
						    	m=Platform.Tencent;
						    }else if(pp==5){
						    	m=Platform.Renmin;
						    }else {
						    	m=Platform.Sina;
						    }
						    
						    ITweet  weet =null;
						    if(sd.getType()==1){
						    	//  没有阴影
						    }
						    else if(sd.getType() == 2){
						    	// 有阴影，原帖
						    	weet = sd.getPost(user, sd.getSourceid(), m);// sd.getOrgId(), 
						    }
						    else{
						    	// 有阴影，引用
						    	String s = "";
						    	weet = sd.getPost(user, sd.getSourceid(), m);
						    }
						    //ITweet  weet=sd.getPost(this.getUser().getBaseUser(),  sd.getSourceid(),m );
						    
							if(weet!=null){
								PublishManagerRepostDTO    rdt=new PublishManagerRepostDTO();
								 rdt.repostName=weet.getAuthor().getDisplayName();
								 rdt.repostTime =DateUtils.formatDateString( weet.getCreated());
								 rdt.repostText=weet.getText();
								 rdt.repostNum=weet.getReposts();
								 rdt.commentNum=weet.getComments();
								 List<String>  mgs=weet.getImages();
								 if(mgs!=null){
									 rdt.Images=mgs;
								 }  //images 集合判断是否为null
								pm.pmr=rdt;
							}  //阴影部分结束 转发原帖
						}
				}

				
				//发送时间
				//long sendtm=sd.getSent(); 立即发布用的
				//long sendtm=sd.getFirstSent(); 定时发布用 
				long sendtm=0;
				if(sd.getSent()!=0){
					sendtm=sd.getSent();
				}else{
					sendtm=sd.getFirstSent();
				}
				
				//定时发送
				if(sendtm!=0){
					Date dateOld = new Date(sendtm);
					String date = "";
					try {
						date = new SimpleDateFormat("yyyy-MM-dd HH:mm")
								.format(dateOld);
					} catch (RuntimeException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					pm.sentTime = "发布时间：</span><span>"+date;
				}
				//为0的  为立即发送
				else {
					pm.sentTime ="发布时间：未发送成功</span><span>";
				}

				
				//查找修改人
				User uu = userDao.queryByUid(sd.getUserid());
				if (uu != null) {
					//pm.mofityers = uu.getUserName();
					pm.mofityers =uu.getNickName();
				} else {
					pm.mofityers = "无";
				}
				
				
				/*
				//查找审核人
				pm.auditorInt = sd.getAuditor();
				User us = userDao.queryByUid(pm.auditorInt);
				if (us != null) {
					pm.auditor = us.getUserName();
				} else {
					pm.auditor = "无";
				}
				*/
				
				try {
					//--------------------
					if (type == 0 || type == 4) {
						pm.mofityers = pm.auditor;
						pm.changeType = "审核人";
						if(pm.mofityers.equals("无")){
							pm.mofityers="<span></span>";
							pm.changeType="";
						}else{
							pm.mofityers="<span>"+pm.mofityers+"</span>";
						}
					} 
					else	if(type==3){
						pm.changeType = "提交人";
						if(!pm.auditor.equals("无")){
						  pm.mofityers="<span>"+pm.mofityers+"</span><span>审核人</span><span>"+pm.auditor+"</span>";
						}else{
							pm.mofityers="<span>"+pm.mofityers+"</span>";
						}
					}else {
						pm.changeType = "提交人";
						pm.mofityers="<span>"+pm.mofityers+"</span>";
					}
				} catch (Exception e) {
					e.printStackTrace();
					//	continue;
				}
				pf2.getRows().add(pm);
			}  //第一个for 结束
		}
			//判断报错的情况下，需要给前台显示无数据的现象，
			//不让前台一直loading状态
			if(pf2.getRows().size()!=pf.getRows().size()){
				 pf2.setRealcount(pf2.getRows().size());
				 System.out.println(pf2.getRows().size());
			}
			
			return pf2;
		}

		return null;
	}

}
