package com.xunku.dao.office;

import java.util.Date;
import java.util.List;

import com.xunku.app.interfaces.ITweet;
import com.xunku.dto.PagerDTO;
import com.xunku.dto.myTwitter.WeiboPostDTO;
import com.xunku.dto.office.NavieCountDTO;
import com.xunku.pojo.base.Organization;
import com.xunku.pojo.office.Navie;
import com.xunku.pojo.office.NavieAccount;
import com.xunku.utils.Pagefile;

public interface NavieDao {
    /**
     * 功能描述<获取客户下的网评员账号>
     * @author wanghui
     * @param  void	
     * @return List<NavieAccount>
     * @version twitter 1.0
     * @date Aug 20, 20148:22:30 PM
     */
    public List<NavieAccount> getNavieAccount(int customid);
    /**
     * 功能描述<获取网评员>
     * @author wanghui
     * @param  void	
     * @return Navie
     * @version twitter 1.0
     * @date Aug 28, 201410:24:26 AM
     */
    public Navie queryNavieById(int navieid);
    /**
     * 功能描述<添加网评员信息>
     * @author wanghui
     * @param  void	
     * @return void
     * @version twitter 1.0
     * @date Apr 18, 20146:22:33 PM
     */
    public int insert(NavieAccount na);
    /**
     * 功能描述<删除网评员>
     * @author wanghui
     * @param  void	
     * @return void
     * @version twitter 1.0
     * @date Apr 21, 20149:27:11 AM
     */
    public boolean deleteById(int nid,String uid);
    /**
     * 功能描述<获取网评员列表>
     * @author wanghui
     * @param  void	
     * @return List<Navie>
     * @version twitter 1.0
     * @date Jul 29, 20144:28:45 PM
     */
    public Pagefile<NavieAccount> queryNavieList(PagerDTO dto,int customId);
    /**
     * 功能描述<更新网评员信息>
     * @author wanghui
     * @param  void	
     * @return boolean
     * @version twitter 1.0
     * @date Jul 14, 20149:55:32 AM
     */
    public boolean updateById(NavieAccount na);
    /**
     * 功能描述<检查是否存在>
     * @author wanghui
     * @param  void	
     * @return boolean
     * @version twitter 1.0
     * @date Jul 29,20145:32:53 PM
     */
    public boolean checkIsExsit(int customid,int platform,String displayname);
    /**
     * 功能描述<微博内容统计列表【考核管理】>
     * @author wanghui
     * @param  void	
     * @return Pagefile<WeiboPostDTO>
     * @version twitter 1.0
     * @date Apr 21, 201411:06:08 AM
     */
    public Pagefile<WeiboPostDTO> queryByTextCount(PagerDTO dto,String uid,Date startTime,Date endTime,int customId);
    /**
     * 功能描述<微博内容统计转发机构反查列表【考核管理】>
     * @author wanghui
     * @param  void	
     * @return Pagefile<Organization>
     * @version twitter 1.0
     * @date Jul 28, 20146:40:29 PM
     */
    public Pagefile<Organization> queryOrganization(PagerDTO dto,String tid,String startTime,String endTime,int customId);
    /**
     * 功能描述<网评员统计列表【考核管理】>
     * @author wanghui
     * @param  void	
     * @return Pagefile<NavieCountDTO>
     * @version twitter 1.0
     * @date Jul 10, 20141:50:51 PM
     */
    public Pagefile<NavieCountDTO> queryByNavieCount(PagerDTO dto,String startTime,String endTime,int customId);
    /**
     * 功能描述<网评员统计反查总转评到我的个数【网评员统计】>
     * @author wanghui
     * @param  void	
     * @return Pagefile<NavieAccount>
     * @version twitter 1.0
     * @date Jul 28, 201411:02:17 AM
     */
    public Pagefile<ITweet> queryNavieToMe(PagerDTO dto,int navieId,String startTime,String endTime);
    /**
     * 功能描述<网评员统计反查网评员账号的个数【网评员统计】>
     * @author wanghui
     * @param  void	
     * @return Pagefile<NavieAccount>
     * @version twitter 1.0
     * @date Jul 17, 20142:03:02 PM
     */
    public Pagefile<NavieAccount> queryNavieAccount(PagerDTO dto,int nid);
    /**
     * 功能描述<获取当前网评员下的所有账号>
     * @author wanghui
     * @param  void	
     * @return List<NavieAccount>
     * @version twitter 1.0
     * @date Aug 8, 20142:32:55 PM
     */
    public List<NavieAccount> queryNavieList(String navice,int customid);

}
