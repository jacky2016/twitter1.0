package com.xunku.dao.office;

import com.xunku.dto.office.MessageDTO;
import com.xunku.dto.office.MessageVO;
import com.xunku.pojo.office.Message;
import com.xunku.utils.Pagefile;

public interface MessageDao {
    /**
     * 功能描述<添加通知>
     * @author wanghui
     * @param  void	
     * @return void
     * @version twitter 1.0
     * @date Apr 23, 20141:35:47 PM
     */
    public void insert(MessageDTO dto);
    /**
     * 功能描述<查询任务通知列表>
     * @author wanghui
     * @param  void	
     * @return Pagefile<MessageVO>
     * @version twitter 1.0
     * @date Apr 23, 20143:31:45 PM
     */
    public Pagefile<MessageVO> queryReceiveMsg(MessageVO vo);
    /**
     * 功能描述<任务消息通知数>
     * @author wanghui
     * @param  void	
     * @return int
     * @version twitter 1.0
     * @date May 12, 201411:09:58 AM
     */
    public int queryByCount(int userId,int stutus);
    /**
     * 功能描述<查询任务通知内容>
     * @author wanghui
     * @param  void	
     * @return String
     * @version twitter 1.0
     * @date May 19, 20144:44:58 PM
     */
    public String queryMessageByID(int msgId);
    /**
     * 功能描述<更新已读\未读状态>
     * @author wanghui
     * @param  msgId[消息编号]、userId[系统当前登录的用户]	
     * @return void
     * @version twitter 1.0
     * @date May 20, 201410:26:44 AM
     */
    public void updateStatus(int msgId,int userId);
    /**
     * 功能描述<删除消息通知【任务通知】>
     * @author wanghui
     * @param  void	
     * @return void
     * @version twitter 1.0
     * @date May 20, 20144:24:26 PM
     */
    public void deleteByID(int id);
}
