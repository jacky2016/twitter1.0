package com.xunku.portal;

import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 模板处理器
 * @author wujian
 * @created on Jun 5, 2014 4:00:03 PM
 */
public class TemplateServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws UnsupportedEncodingException {
		
		String templateName = "moduleName";
		
		// 通过Module获得Template的内容
		
		// 然后根据权限系统获得最终的输出
		
	}
}
