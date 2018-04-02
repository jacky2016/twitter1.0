package com.xunku.actions.eventMonitor;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.xunku.actions.ActionBase;
import com.xunku.app.Utility;
import com.xunku.app.enums.EventMonitorEnum;
import com.xunku.app.enums.Platform;
import com.xunku.app.interfaces.ITweet;
import com.xunku.app.monitor.EventMonitor;
import com.xunku.app.result.RetweetResult;
import com.xunku.constant.PortalCST;
import com.xunku.dao.event.DataAnalysisDao;
import com.xunku.dao.event.KeyPointTopDao;
import com.xunku.dao.event.WordCloudDao;
import com.xunku.daoImpl.event.DataAnalysisDaoImpl;
import com.xunku.daoImpl.event.EventDaoImpl;
import com.xunku.daoImpl.event.KeyPointTopDaoImpl;
import com.xunku.daoImpl.event.WordCloudDaoImpl;
import com.xunku.dto.CloudWordDataDTO;
import com.xunku.dto.PieDTO;
import com.xunku.dto.PieDataItemDTO;
import com.xunku.dto.eventMonitor.CloudWordDTO;
import com.xunku.dto.eventMonitor.ContentanalysDTO;
import com.xunku.pojo.event.WordCloud;
import com.xunku.utils.AppServerProxy;
import com.xunku.utils.Pagefile;

/**
 * 事件监控--内容分析信息
 * 
 * @author shaoqun
 * 
 */
public class ContentAnalysAction extends ActionBase {

	@Override
	public Object doAction() {
		DataAnalysisDao dataDAO = new DataAnalysisDaoImpl();
		KeyPointTopDao keyDAO = new KeyPointTopDaoImpl();
		WordCloudDao wordDAO = new WordCloudDaoImpl();
		
		String charType = this.get("charType");
		int eid = Integer.parseInt(this.get("eid"));
		Pagefile<ContentanalysDTO> mtfile = new Pagefile<ContentanalysDTO>();
		
		WordCloud word = new WordCloud();
		EventMonitor event = new EventDaoImpl().queryEventByEId(eid);
		
		if (charType.equals(EventMonitorEnum.The_forward.toString())) { // 原创/转发统计
			RetweetResult yc = null;
			try { 
				yc = AppServerProxy.viewRetweetCnt(event);
				
			} catch (RuntimeException e) {
				e.printStackTrace();
			}

			return GetPieDTO(yc);

		} else if (charType.equals(EventMonitorEnum.Hot_keywords.toString())) { // 热门关键词统计

			Map<String, Float> map = null;
			try {
				map = AppServerProxy.viewHotwords(event);
			} catch (RuntimeException e) {
				e.printStackTrace();
			}
 
			return GetCloudWordDTO(map);
			

		} else if (charType.equals(EventMonitorEnum.The_keylist.toString())) { // 关键观点列表
			Map<ITweet, Integer> plist = null;
			try {
				plist = AppServerProxy.viewSuperTweet(event);
			} catch (RuntimeException e) {
				e.printStackTrace();
			}
			
			if(plist != null){
				for (ITweet pt : plist.keySet()) {
	
					ContentanalysDTO cdto = new ContentanalysDTO();
					cdto.id = pt.getId();
					cdto.text = pt.getText();
					cdto.zfnum = pt.getReposts();
					cdto.createtime = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(pt.getCreated());
					if(pt.getPlatform() == Platform.Tencent){
						cdto.account.twitterType = 2;
					}else{
						cdto.account.twitterType = 1;
					}
					if (pt.getAuthor() != null) {
						
						if (pt.getPlatform() == Platform.Tencent && pt.getAuthor().getHead() != null) {
							cdto.imageHead = PortalCST.IMAGE_PATH_PERFIX + pt.getAuthor().getHead();
							cdto.account.imgurlBig = PortalCST.IMAGE_PATH_PERFIX + pt.getAuthor().getHead();
						}else{
							cdto.imageHead = pt.getAuthor().getHead();
							cdto.account.imgurlBig = pt.getAuthor().getHead();
						}
						
						cdto.account.name = pt.getAuthor().getDisplayName();			 //名称
						cdto.account.ucode = pt.getAuthor().getUcode();
						cdto.account.url = pt.getAuthor().getHomeUrl();
						cdto.account.weibos = pt.getAuthor().getWeibos(); 		 //微博数
						cdto.account.followers =  pt.getAuthor().getFollowers(); //粉丝数
						cdto.account.friends = pt.getAuthor().getFriends();	 	 //关注数
						cdto.account.summany = pt.getAuthor().getDescription();	 //简介
						cdto.account.isAjax = true;
						cdto.imageHead = pt.getAuthor().getHead();
						if (pt.getAuthor().isVerified() == true) { // 认证
							cdto.authentication = "是";
						} else {
							cdto.authentication = "否";
						}
						cdto.nickname = pt.getAuthor().getDisplayName();
					}else{
						cdto.nickname = "";
						cdto.authentication = "否";
					}
					mtfile.getRows().add(cdto);
				}
				// 排序 add by wujian
				Collections.sort(mtfile.getRows(),new compareKey<ContentanalysDTO>());
				mtfile.setRealcount(plist.size());
			}
			return mtfile;
		}
		return null;
	}

	private class compareKey<T extends ContentanalysDTO> implements
			Comparator<T> {

		@Override
		public int compare(ContentanalysDTO o1, ContentanalysDTO o2) {
			return o2.zfnum - o1.zfnum;
		}
	}

	// 原创转发饼图
	private PieDTO GetPieDTO(RetweetResult rest) {

		PieDTO pieDto = new PieDTO();
		pieDto.title = "";
		PieDataItemDTO v1 = new PieDataItemDTO();
		PieDataItemDTO v2 = new PieDataItemDTO();
		if(rest != null){
			int y_num = rest.getTweetCnt(),z_num = rest.getRetweetCnt(),h_num = rest.getCommentCnt();
			if((y_num+z_num+h_num) != 0){
				double yc = (double)y_num/(double)(y_num+z_num+h_num),
					   zf = (double)z_num/(double)(y_num+z_num+h_num);
				BigDecimal _yc = new BigDecimal(yc);  
				BigDecimal _zf = new BigDecimal(zf);  
				v1.name = "原创";
				v1.y = _yc.setScale(4,BigDecimal.ROUND_HALF_UP).doubleValue();
				v2.name = "转发";  
				v2.y = _zf.setScale(4,BigDecimal.ROUND_HALF_UP).doubleValue();  
				pieDto.series.data.add(v1);
				pieDto.series.data.add(v2);
			}else{
				pieDto.test = -2;
			}
		}else{
			pieDto.test = -2;
		}
		
		return pieDto;
	}

	// 热门关键词统计 已废弃
	private List<CloudWordDataDTO> GetCloudWordDTO1(List<WordCloud> listWd) {

		List<CloudWordDataDTO> list = new ArrayList<CloudWordDataDTO>();

		for (WordCloud wDto : listWd) {
			CloudWordDataDTO cdto = new CloudWordDataDTO();
			cdto.text = wDto.getKeyword();
			cdto.weight = wDto.getCount();
			list.add(cdto);
		}
		return list;
	}
	// 热门关键词统计
	private CloudWordDTO GetCloudWordDTO(Map<String,Float> maps) {

		CloudWordDTO cloud = new CloudWordDTO();
		String key = "";
		int weight = 0;
		if (maps != null) {
			if(maps.size() != 0){
				Iterator<Entry<String, Float>> iter = maps.entrySet().iterator();
				while (iter.hasNext()) {
					Entry<String, Float> en = iter.next();
					CloudWordDataDTO cdto = new CloudWordDataDTO();
					cdto.text = en.getKey();
					cdto.weight = en.getValue();
					cloud.series.add(cdto);
				}
			}else{
				cloud.test = -1;
			}
		}else{
			cloud.test = -1;
		}
		
		return cloud;
	}

}
