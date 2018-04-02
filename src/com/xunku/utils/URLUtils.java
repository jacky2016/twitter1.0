package com.xunku.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketException;
import java.net.URL;
import java.net.URLConnection;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.xunku.app.Utility;

/**
 * 访问http请求
 * 
 * @author wanghui
 * 
 */
public class URLUtils {
	private static final Log log = LogFactory.getLog(URLUtils.class);

	/**
	 * 根据指定url请求某一地址，获得响应
	 * 
	 * @param url2
	 *            需要请求的地址
	 * @return 响应的字符串
	 * @throws IOException
	 */
	public static String getResponseStr(String url2, String param) {
		StringBuffer sb = new StringBuffer();
		try {
			URL url = new URL(url2);// 生成url对象
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();// 打开url连接
			conn.setRequestProperty("Content-Type", "text/html; charset=UTF-8");
			conn.setRequestMethod("POST");
			conn.setDoOutput(true);
			conn.setDoInput(true);
			OutputStream out = conn.getOutputStream();
			out.write(param.getBytes("UTF-8"));
			out.close();
			BufferedReader br = new BufferedReader(new InputStreamReader(conn
					.getInputStream(), "UTF-8"));
			String line = null;
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}
			br.close();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return sb.toString();
	}

	/**
	 * 如果sendType = 1 发邮件 <p/> 如果sendType = 3 发短信
	 * 
	 * @param sendType
	 * @param tel
	 * @param email
	 * @param title
	 * @param text
	 * @return
	 * @throws IOException
	 */
	public static String sendNet(int sendType, String tel, String email,
			String title, String text) throws IOException {
		StringBuffer sb = new StringBuffer();
		String url_str = null;
		try {
			String param = "";

			if (sendType == 1) {
				param = "FunType=13&SendType=1&Account=" + email + "&Title="
						+ java.net.URLEncoder.encode(title, "utf-8") + "&Text="
						+ java.net.URLEncoder.encode(text, "utf-8");
			}
			if (sendType == 3) {
				param = "FunType=13&SendType=3&Account=" + tel
						+ "&Title=&Text="
						+ java.net.URLEncoder.encode(text, "utf-8");
			}
			if (Utility.isNullOrEmpty(param)) {
				url_str = PropertiesUtils.getString("config", "netUrl") + "?"
						+ param;
				URL url = new URL(url_str);// 生成url对象
				URLConnection urlConnection = url.openConnection();// 打开url连接
				urlConnection.setConnectTimeout(10 * 1000 * 60);
				urlConnection.setReadTimeout(10 * 1000 * 60);
				log
						.info("====================开始向.net发送信息请求=====================");
				log.info("====================发送请求的URL=" + url_str);
				BufferedReader br = new BufferedReader(new InputStreamReader(
						urlConnection.getInputStream(), "UTF-8"));
				String line = null;
				while ((line = br.readLine()) != null) {
					sb.append(line);
				}
				br.close();
			}

		} catch (SocketException e) {

		} catch (IOException e) {
			log.error("不能连接到 url:" + url_str);
			throw e;
		}
		log.info("========================.net响应请求完成===================");
		log.info("========================响应请求返回的结果是" + sb.toString());
		return sb.toString();
	}

	/**
	 * 向.net发送信息请求
	 * 
	 * @param text发送内容,count
	 *            发送请求的次数
	 * @param id
	 *            对象操作的id
	 * @return "true" or "false" or 生成文件的名称
	 * @throws IOException
	 */
	public static String sendNet(int customid, int userid, String email,
			String title, String text, int count) throws IOException {
		StringBuffer sb = new StringBuffer();
		String url_str = null;
		try {
			String param = "CustomID=" + customid + "&UserID=" + userid
					+ "&FunType=13&SendType=1&Account=" + email + "&Title="
					+ java.net.URLEncoder.encode(title, "utf-8") + "&Text="
					+ java.net.URLEncoder.encode(text, "utf-8");
			url_str = PropertiesUtils.getString("config", "netUrl") + "?"
					+ param;
			URL url = new URL(url_str);// 生成url对象
			URLConnection urlConnection = url.openConnection();// 打开url连接
			urlConnection.setConnectTimeout(10 * 1000 * 60);
			urlConnection.setReadTimeout(10 * 1000 * 60);
			log.info("====================开始向.net发送信息请求=====================");
			log.info("====================发送请求的URL=" + url_str);
			BufferedReader br = new BufferedReader(new InputStreamReader(
					urlConnection.getInputStream(), "UTF-8"));
			String line = null;
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}
			br.close();
		} catch (SocketException e) {
			// 重新连接次数超过2次则直接返回false,为了防止并发做的处理
			if (count == 2) {
				return "false";
			} else {
				try {
					Thread.sleep(5);
				} catch (InterruptedException e1) {
					log.error("sleep err!");
				}
				log.info("==============第二次向.net发送信息请求===================");
				// 如果发生链接失败,则重新发送请求
				sb.append(URLUtils.sendNet(customid, userid, email, title,
						text, count + 1));
				log.info("=============第二次请求完成============================");
			}
		} catch (IOException e) {
			log.error("不能连接到 url:" + url_str);
			throw e;
		}
		log.info("========================.net响应请求完成===================");
		log.info("========================响应请求返回的结果是" + sb.toString());
		return sb.toString();
	}

	public static String sendNet4Push(int customid, int userid, int id)
			throws IOException {
		StringBuffer sb = new StringBuffer();
		String url_str = null;
		try {
			String param = "CustomID=" + customid + "&UserID=" + userid
					+ "&FunType=20&SendType=1&ID=" + id;
			url_str = PropertiesUtils.getString("config", "netUrl") + "?"
					+ param;
			URL url = new URL(url_str);// 生成url对象
			URLConnection urlConnection = url.openConnection();// 打开url连接
			urlConnection.setConnectTimeout(10 * 1000 * 60);
			urlConnection.setReadTimeout(10 * 1000 * 60);
			log.info("====================开始向.net发送信息请求=====================");
			log.info("====================发送请求的URL=" + url_str);
			BufferedReader br = new BufferedReader(new InputStreamReader(
					urlConnection.getInputStream(), "UTF-8"));
			String line = null;
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}
			br.close();
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (IOException e) {
			log.error("不能连接到 url:" + url_str);
			throw e;
		}
		log.info("========================.net响应请求完成===================");
		log.info("========================响应请求返回的结果是" + sb.toString());
		return sb.toString();
	}

	/**
	 * 根据指定url请求某一地址，获得响应
	 * 
	 * @param url2
	 *            需要请求的地址
	 * @return 响应的字符串
	 * @throws IOException
	 */
	public static String getResponseStr(String url2) {
		StringBuffer sb = new StringBuffer();
		try {
			URL url = new URL(url2);// 生成url对象
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();// 打开url连接
			BufferedReader br = new BufferedReader(new InputStreamReader(conn
					.getInputStream(), "UTF-8"));
			String line = null;
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}
			br.close();
		} catch (MalformedURLException e) {
			return "";
		} catch (IOException e) {
			return "";
		}
		return sb.toString();
	}
}
