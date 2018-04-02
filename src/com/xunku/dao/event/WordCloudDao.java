package com.xunku.dao.event;

import java.util.List;

import com.xunku.pojo.event.WordCloud;

public interface WordCloudDao {
    /**
     * 功能描述<保存热门关键词【事件分析】>
     * @author wanghui
     * @param  void	
     * @return void
     * @version twitter 1.0
     * @date Jun 10, 20146:35:16 PM
     */
    public void insert(WordCloud wc);
    /**
     * 功能描述<获取热门关键词【内容分析】>
     * @author wanghui
     * @param  void	
     * @return List<WordCloud>
     * @version twitter 1.0
     * @date Jun 10, 20146:36:06 PM
     */
    public List<WordCloud> queryWordCloudList(int eventId);
    /**
     * 功能描述<按eventId删除>
     * @author wanghui
     * @param  void	
     * @return boolean
     * @version twitter 1.0
     * @date Jun 13, 20143:59:48 PM
     */
    public boolean deleteByEvent(int eventId);
}
