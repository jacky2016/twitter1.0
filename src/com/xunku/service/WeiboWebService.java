package com.xunku.service;

import java.io.IOException;

import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;
import org.apache.axiom.om.OMNamespace;
import org.apache.axis2.addressing.EndpointReference;
import org.apache.axis2.client.Options;
import org.apache.axis2.client.ServiceClient;

import com.xunku.utils.PropertiesUtils;

/**
 * 该类包装讯库提供的服务，不做缓存处理，缓存在调用端实现
 * 
 * @author wujian
 * @created on Sep 4, 2014 11:26:01 AM
 */
public class WeiboWebService {

	public static void main(String[] args) {

		for (int i = 1; i < 1000; i++) {
			try {
				String name = WeiboWebService.getWeiboAppSource(i);
				System.out.println(name);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}

	/**
	 * 功能描述<获取微博来源>
	 * 
	 * @author wanghui
	 * @param appsource
	 * @return long
	 * @version twitter 1.0
	 * @throws IOException
	 * @throws NumberFormatException
	 * @date Jun 12, 20144:10:21 PM
	 */
	public static int addWeiboAppSource(String domainid, String appsource,
			String url) throws NumberFormatException, IOException {
		String[] eName = { "domainid", "appsource", "appurl" };
		String[] param = { domainid, appsource, url };
		int sourceid = Integer.parseInt(WeiboWebService
				.getServiceTemplateMethod(eName, "addWeiboAppSource", param));
		return sourceid;
	}

	/**
	 * 功能描述<根据id获取来源名称>
	 * 
	 * @author wanghui
	 * @param void
	 * @return OMElement
	 * @version twitter 1.0
	 * @throws IOException
	 * @date Jun 12, 20143:07:30 PM
	 */
	public static String getWeiboAppSource(int sourceid) throws IOException {
		String[] eName = { "sourceid" };
		String[] param = { String.valueOf(sourceid) };
		String source = WeiboWebService.getServiceTemplateMethod(eName,
				"getWeiboAppSource", param);
		return source;

	}

	/**
	 * 功能描述<调用WebService的模板方法>
	 * 
	 * @author wanghui
	 * @param void
	 * @return String
	 * @version twitter 1.0
	 * @throws IOException
	 * @date Jun 12, 20144:47:24 PM
	 */
	public static String getServiceTemplateMethod(String[] eName, String mName,
			String[] params) throws IOException {
		String servceAddress = PropertiesUtils.getString("config",
				"webServiceAddress");
		EndpointReference endpointReference = new EndpointReference(
				servceAddress);
		OMFactory fac = OMAbstractFactory.getOMFactory();
		OMNamespace omNs = fac.createOMNamespace(
				"http://service.web.weibo.xunku.org", "xsd");

		OMElement method = fac.createOMElement(mName, omNs);
		for (int i = 0; i < params.length; i++) {
			String element = eName[i];
			String param = params[i];
			OMElement value = fac.createOMElement(element, omNs);
			value.addChild(fac.createOMText(value, param));
			method.addChild(value);
		}
		Options options = new Options();
		options
				.setAction("http://agent.xunku.org:8080/axis2/services/WeiboService/getWeiboAppSource"); // 此处对应于@WebMethod(action
		options.setTo(endpointReference);
		ServiceClient sender = new ServiceClient();
		sender.setOptions(options);
		OMElement ome = sender.sendReceive(method);
		String result = null;
		if (ome != null) {
			result = ome.getFirstElement().getText();
		}
		return result;
	}
}
