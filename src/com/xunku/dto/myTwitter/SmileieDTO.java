package com.xunku.dto.myTwitter;

import com.xunku.dto.IDTO;

public class SmileieDTO implements IDTO {
	public int id;
	public String name; // 表情的名称
	public String fileName;// 表情路径Path不包含根，表情的根定义在配置文件中例如：$/smile/haha.png，其中$是根目录的占位符
	public String sina;
	public String qq;
	public String renmin;

}