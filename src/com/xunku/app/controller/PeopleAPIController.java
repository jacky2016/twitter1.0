package com.xunku.app.controller;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xunku.app.Utility;
import com.xunku.app.model.UploadImage;
import com.xunku.app.model.people.PUser;
import com.xunku.app.model.people.Pweet;
import com.xunku.app.model.tweets.TweetFactory;
import com.xunku.dao.base.AccountAuthsDao;
import com.xunku.daoImpl.base.AccountAuthsDaoImpl;

public class PeopleAPIController {

	static Logger LOG = LoggerFactory.getLogger(PeopleAPIController.class);

	public PUser getPeopleUser(String peopleId) {
		AccountAuthsDao dao = new AccountAuthsDaoImpl();
		return dao.queryPeopleUser(Long.parseLong(peopleId));
	}

	/**
	 * 发送微博接口
	 */
	final String api_send_url = "http://t.people.com.cn/statuses/send.action";

	/**
	 * 专属DES加密KEY
	 */
	final String key = "$(2)jfjb|LIVE@";

	public Pweet sendTweet(String username, String password, String message,
			UploadImage image) {
		try {
			String u = Utility.encrypt(username, key);
			String p = Utility.encrypt(password, key);

			String text = URLEncoder.encode(message, "GBK");

			Map<String, String> textMap = new HashMap<String, String>();
			textMap.put("userName", u);
			textMap.put("password", p);
			textMap.put("message", text);
			textMap.put("sourceCode", "0");
			textMap.put("appKey", "2005240712");
			StringBuilder sb = new StringBuilder();
			for (Map.Entry<String, String> e : textMap.entrySet()) {
				sb.append(e.getKey() + "=" + e.getValue() + "&");
			}
			String r = formUpload(api_send_url + "?" + sb.toString(), image);

			Pweet tweet = TweetFactory.createPeopleTweet(r);

			return tweet;

		} catch (Exception e) {
			LOG.error("人民网微博发送失败～", e);
			return null;
		}

	}

	/**
	 * 上传图片
	 * 
	 * @param urlStr
	 * @param textMap
	 * @param uploadImage
	 * @return
	 */
	public String formUpload(String urlStr, UploadImage uploadImage) {
		String res = "";
		HttpURLConnection conn = null;
		String BOUNDARY = "---------------------------123821742118716"; // boundary就是request头和上传文件内容的分隔符
		try {
			URL url = new URL(urlStr);
			conn = (HttpURLConnection) url.openConnection();
			conn.setConnectTimeout(5000);
			conn.setReadTimeout(30000);
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setUseCaches(false);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Connection", "Keep-Alive");
			conn
					.setRequestProperty("User-Agent",
							"Mozilla/5.0 (Windows; U; Windows NT 6.1; zh-CN; rv:1.9.2.6)");
			conn.setRequestProperty("Content-Type",
					"multipart/form-data; boundary=" + BOUNDARY);

			OutputStream out = new DataOutputStream(conn.getOutputStream());

			// file
			if (uploadImage != null) {
				StringBuffer strBuf = new StringBuffer();
				String contentType = "image/png";
				String filename = "c:\\weibo.png";
				strBuf.append("\r\n").append("--").append(BOUNDARY).append(
						"\r\n");
				strBuf
						.append("Content-Disposition: form-data; name=\"uploadPic"
								+ "\"; filename=\"" + filename + "\"\r\n");
				strBuf.append("Content-Type:" + contentType + "\r\n\r\n");

				out.write(strBuf.toString().getBytes());
				out.write(uploadImage.getContent());

				byte[] endData = ("\r\n--" + BOUNDARY + "--\r\n").getBytes();
				out.write(endData);
				out.flush();
				out.close();
			}
			// 读取返回数据
			StringBuffer strBuf = new StringBuffer();
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					conn.getInputStream(), "utf-8"));
			String line = null;
			while ((line = reader.readLine()) != null) {
				strBuf.append(line).append("\n");
			}
			res = strBuf.toString();
			reader.close();
			reader = null;
		} catch (Exception e) {
			System.out.println("发送POST请求出错。" + urlStr);
			e.printStackTrace();
		} finally {
			if (conn != null) {
				conn.disconnect();
				conn = null;
			}
		}
		return res;
	}

	public static void main(String[] args) {

		PeopleAPIController controller = new PeopleAPIController();
		controller.sendTweet("bjmkstkj", "2005621org", "发微博测试，图片X3!.", null);

	}

}
