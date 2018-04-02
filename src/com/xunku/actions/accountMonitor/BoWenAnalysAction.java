package com.xunku.actions.accountMonitor;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.xunku.actions.ActionBase;
import com.xunku.app.enums.Platform;
import com.xunku.app.interfaces.ITweet;
import com.xunku.dto.accountMonitor.HotweiboListDTO;
import com.xunku.utils.AppServerProxy;
import com.xunku.utils.Pagefile;

/**
 * 帐号监控--获取博文分析信息
 * @author shaoqun
 *
 */
public class BoWenAnalysAction extends ActionBase {

	@Override
	public Object doAction() {

		Pagefile<HotweiboListDTO> mtfile = new Pagefile<HotweiboListDTO>();
		
		int type = Integer.parseInt(this.get("type"));
		int id = Integer.parseInt(this.get("id"));
		Platform fiter = Platform.Sina;
		if(type == 1){
			fiter = Platform.Tencent;
		}else if(type == 2){
			fiter = Platform.Renmin;
		}
		
		List<ITweet> list = AppServerProxy.MAccountViewTop10(id);
		//博主帐号信息
		for(ITweet itweet : list){
			
			HotweiboListDTO ht = new HotweiboListDTO();
			ht.id = (int) itweet.getId();
			if(itweet.getAuthor() !=null){
				ht.wbname = itweet.getAuthor().getDisplayName();
			}else{
				ht.wbname = "";
			}
			ht.createdate = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(itweet.getCreated());
			ht.zfnum = itweet.getReposts();
			ht.plnum = itweet.getComments();
			ht.text = itweet.getText();
			ht.url = itweet.getUrl();
			
//			if(itweet.getAuthor() != null){
//				if (itweet.getPlatform() == Platform.Tencent && itweet.getAuthor().getHead() != null) {			
//					ht.imageHead = PortalCST.IMAGE_PATH_PERFIX + itweet.getAuthor().getHead();
//					ht.account.imgurlBig = PortalCST.IMAGE_PATH_PERFIX + itweet.getAuthor().getHead();
//				}else{
//					ht.imageHead = itweet.getAuthor().getHead();
//					ht.account.imgurlBig = itweet.getAuthor().getHead();
//				}
//				
//				ht.account.name = itweet.getAuthor().getName();			  //名称
//				ht.account.ucode = itweet.getAuthor().getUcode();
//				ht.account.url = itweet.getAuthor().getHomeUrl();
//				ht.account.weibos = itweet.getAuthor().getWeibos(); 	  //微博数
//				ht.account.followers = itweet.getAuthor().getFollowers(); //粉丝数
//				ht.account.friends = itweet.getAuthor().getFriends();	  //关注数
//				ht.account.summany = itweet.getAuthor().getDescription(); //简介
//				ht.account.isAjax = true;
//			}
			mtfile.getRows().add(ht);
		}
		Collections.sort(mtfile.getRows(),new compareKey<HotweiboListDTO>());
		mtfile.setRealcount(list.size());
		return mtfile;
	}

	//排序
	private class compareKey<T extends HotweiboListDTO> implements
	Comparator<T> {

		@Override
		public int compare(HotweiboListDTO o1, HotweiboListDTO o2) {
			return o2.zfnum - o1.zfnum;
		}
	}
}
