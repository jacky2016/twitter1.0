package com.xunku.dao.my;

import java.util.List;
import java.util.Map;

import com.xunku.app.enums.Platform;
import com.xunku.app.interfaces.ITweet;
import com.xunku.constant.IdentityType;
import com.xunku.constant.SendTypeByMeEnum;
import com.xunku.constant.WeiboType;
import com.xunku.dto.PagerDTO;
import com.xunku.dto.myTwitter.DataAnalyzeDTO;
import com.xunku.utils.Pagefile;
public interface PostDao{
    /**
     * 功能描述<获取微博内容>
     * @author wanghui
     * @param  void	
     * @return String
     * @version twitter 1.0
     * @date Jul 21, 20142:41:13 PM
     */
    public ITweet queryWeiboById(int id);
    /**
     * 功能描述<微博处理统计详细【按转发和评论】>
     * @author wanghui
     * @param  微博类型 1-原创发表 2-转发 3-评论	
     * @return Pagefile<Post>
     * @version twitter 1.0
     * @date Jul 16, 20144:12:39 PM
     */
    public Pagefile<ITweet> queryRepostsPost(WeiboType type,String startTime,String endTime,String ucode,PagerDTO dto);
    /**
     * 功能描述<@提到我的微博【我的微博】>
     * @author wanghui
     * @param  void	
     * @return Pagefile<Post>
     * @version twitter 1.0
     * @date Jun 12, 201410:40:28 AM
     */
    public Pagefile<ITweet> queryAtMePost(PagerDTO dto,IdentityType type,Platform form,String searchkey);
    /**
     * 功能描述<收到的评论和发出的评论【我的评论】>
     * @author wanghui
     * @param  void	
     * @return Pagefile<Post>
     * @version twitter 1.0
     * @date May 23, 20142:37:28 PM
     */
    public Pagefile<ITweet> commentPostByMe(PagerDTO dto,String ucode,SendTypeByMeEnum sendType);
    /**
     * 功能描述<删除评论【我的评论】>
     * @author wanghui
     * @param  tid微博id,微博类型 1 新浪，2 腾讯，5 人民	
     * @return void
     * @version twitter 1.0
     * @date May 27, 20142:33:20 PM
     */
    public void deleteCommentPost(String tid,int platform);
    /**
     * 功能描述<微博处理统计【考核管理】>
     * @author wanghui
     * @param  void	
     * @return Pagefile<PostDealDTO>
     * @version twitter 1.0
     * @date Jun 9, 20144:44:07 PM
     */
    //public Pagefile<PostDealDTO> queryPostDeal(PagerDTO dto,String startTime,String endTime,String ucode,int customId);
    /**
     * 功能描述<@我的统计【数据分析】>
     * @author wanghui
     * @param  void	
     * @return List<DataAnalyzeDTO>
     * @version twitter 1.0
     * @date Jun 16, 20146:14:07 PM
     */
    public List<DataAnalyzeDTO> queryDataByAtMe(String startTime,String endTime,int aid,int day);
    
////////////////////////////////////////////////////////////////////////////////////
    /**
     * 功能描述<删除发布微博>
     * @author wanghui
     * @param  void	
     * @return void
     * @version twitter 1.0
     * @date Apr 16, 20146:13:28 PM
     */
    public void deleteById(int pid);
    /**
     * 功能描述<审核微博>
     * @author wanghui
     * @param  void	
     * @return void
     * @version twitter 1.0
     * @date Apr 25, 201411:22:45 AM
     */
    public void checkPost(int postId,int approved);
    /*******************************************数据分析开始****************************************/
    /**
     * 功能描述<用户微博的发布统计[日期分布]>
     * @author wanghui
     * @param  startTime,endTime,aid[分析的账号],day[多少天]	
     * @return List<DataAnalyzeDTO>
     * @version twitter 1.0
     * @date Apr 17, 20143:50:06 PM
     */
    public List<DataAnalyzeDTO> queryDataByPTime(String startTime,String endTime,String uid,int day);
    /**
     * 功能描述<用户微博的发布统计[时间段分布]>
     * @author wanghui
     * @param  void	
     * @return 
     * @version twitter 1.0
     * @date Apr 30, 20143:16:59 PM
     */
    public Map<String, List<Integer>> queryDataByTimeQuantum(String startTime,String endTime,String uid);
    /**
     * 功能描述<用户微博的趋势分析[按转发数分析]>
     * @author wanghui
     * @param  startTime,endTime,uid[分析的账号]	
     * @return List<DataAnalyzeDTO>
     * @version twitter 1.0
     * @date May 4, 20143:03:04 PM
     */
    public List<DataAnalyzeDTO> queryDataByRepostAnalyze(String startTime,String endTime,String uid,int day);
    /**
     * 功能描述<用户微博的趋势分析[按粉丝数分析]>
     * @author wanghui
     * @param  void	
     * @return List<DataAnalyzeDTO>
     * @version twitter 1.0
     * @date May 4, 20146:02:18 PM
     */
    //public List<DataAnalyzeDTO> queryDataByFollowAnalyze(String startTime,String endTime,String uid,int day);
    
    /*******************************************数据分析结束****************************************/
}
