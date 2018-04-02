package com.xunku.dto;

import java.util.ArrayList;
import java.util.List;

/*
 * 柱状图和线性图DTO
 * @author sunao
 */
public class LineDTO {
	// X坐标单位节点集合
	public List<String> categories = new ArrayList<String>();
	// 图表左侧竖式标题
	public String text;
	// 图表顶部标题
	public String title;
	// 图表X轴步长
	public int tickInterval = 1;
	// 数据
	public List<LineDataDTO> series = new ArrayList<LineDataDTO>();
	
	public int test;//判断标准
}
