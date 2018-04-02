package com.xunku.dto.spreadAnalysis;

import java.util.ArrayList;
import java.util.List;

import com.xunku.dto.AccountDTO;


public class RetweetLevelDTO {
	public int level;// 当前的层级
	public int personNum;// 当前这个层级多少人
	public int followers;// 当前这个层级覆盖了多少人
	public List<AccountDTO> accounts = new ArrayList<AccountDTO>(); //账号信息map
}
