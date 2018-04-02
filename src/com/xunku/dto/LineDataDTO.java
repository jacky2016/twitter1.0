package com.xunku.dto;

import java.util.ArrayList;
import java.util.List;

/*
 * 柱状图和线性图数据DTO
 * @author sunao
 */
public class LineDataDTO {
	//Y坐标节点名称(单个)
	public String name;
	//数据
	public List<Double> data = new ArrayList<Double>();
}
