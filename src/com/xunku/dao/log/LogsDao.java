package com.xunku.dao.log;

import com.xunku.pojo.log.LogsEntity;

public interface LogsDao {
	/*
	 * 功能描述<保存日志信息>
     * @author sunao
     */
    public void insertLogs(LogsEntity log);
}
