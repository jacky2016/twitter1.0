package com.xunku.app;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import java.util.UUID;
import java.util.Map.Entry;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

import sun.misc.BASE64Decoder;

import com.google.gson.Gson;
import com.xunku.app.enums.AppType;
import com.xunku.app.enums.GenderEnum;
import com.xunku.app.enums.Platform;
import com.xunku.app.enums.PostType;
import com.xunku.app.interfaces.ITweet;
import com.xunku.app.model.AppAccount;
import com.xunku.app.model.AppException;
import com.xunku.constant.PortalCST;

/**
 * 和微博相关的工具集
 * 
 * @author wujian
 * @created on Jun 5, 2014 3:53:47 PM
 */
public class Utility {

	

	private final static String DES = "DES";

	/**
	 * Description 根据键值进行加密
	 * 
	 * @param data
	 * @param key
	 *            加密键byte数组
	 * @return
	 * @throws Exception
	 */
	public static String encrypt(String data, String key) throws Exception {
		byte[] bt = encrypt(data.getBytes(), key.getBytes());
		String strs = bytesToHexString(bt);// new String(bt);// new
		// BASE64Encoder().encode(bt);
		return strs;
	}

	/**
	 * Description 根据键值进行解密
	 * 
	 * @param data
	 * @param key
	 *            加密键byte数组
	 * @return
	 * @throws IOException
	 * @throws Exception
	 */
	public static String decrypt(String data, String key) throws IOException,
			Exception {
		if (data == null)
			return null;
		BASE64Decoder decoder = new BASE64Decoder();
		byte[] buf = decoder.decodeBuffer(data);
		byte[] bt = decrypt(buf, key.getBytes());
		return new String(bt);
	}

	/**
	 * Description 根据键值进行加密
	 * 
	 * @param data
	 * @param key
	 *            加密键byte数组
	 * @return
	 * @throws Exception
	 */
	private static byte[] encrypt(byte[] data, byte[] key) throws Exception {
		// 生成一个可信任的随机数源
		SecureRandom sr = new SecureRandom();

		// 从原始密钥数据创建DESKeySpec对象
		DESKeySpec dks = new DESKeySpec(key);

		// 创建一个密钥工厂，然后用它把DESKeySpec转换成SecretKey对象
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES);
		SecretKey securekey = keyFactory.generateSecret(dks);

		// Cipher对象实际完成加密操作
		Cipher cipher = Cipher.getInstance(DES);

		// 用密钥初始化Cipher对象
		cipher.init(Cipher.ENCRYPT_MODE, securekey, sr);

		return cipher.doFinal(data);
	}

	public static String bytesToHexString(byte[] src) {
		StringBuilder stringBuilder = new StringBuilder("");
		if (src == null || src.length <= 0) {
			return null;
		}
		for (int i = 0; i < src.length; i++) {
			int v = src[i] & 0xFF;
			String hv = Integer.toHexString(v);
			if (hv.length() < 2) {
				stringBuilder.append(0);
			}
			stringBuilder.append(hv);
		}
		return stringBuilder.toString();
	}

	/**
	 * Description 根据键值进行解密
	 * 
	 * @param data
	 * @param key
	 *            加密键byte数组
	 * @return
	 * @throws Exception
	 */
	private static byte[] decrypt(byte[] data, byte[] key) throws Exception {
		// 生成一个可信任的随机数源
		SecureRandom sr = new SecureRandom();

		// 从原始密钥数据创建DESKeySpec对象
		DESKeySpec dks = new DESKeySpec(key);

		// 创建一个密钥工厂，然后用它把DESKeySpec转换成SecretKey对象
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES);
		SecretKey securekey = keyFactory.generateSecret(dks);

		// Cipher对象实际完成解密操作
		Cipher cipher = Cipher.getInstance(DES);

		// 用密钥初始化Cipher对象
		cipher.init(Cipher.DECRYPT_MODE, securekey, sr);

		return cipher.doFinal(data);
	}

	@SuppressWarnings("unchecked")
	public static <K extends Comparable, V extends Comparable> Map<K, V> sortByKeys(
			Map<K, V> map) {
		List<K> keys = new LinkedList<K>(map.keySet());
		Collections.sort(keys);

		Map<K, V> sortedMap = new LinkedHashMap<K, V>();
		for (K key : keys) {
			sortedMap.put(key, map.get(key));
		}

		return sortedMap;
	}

	@SuppressWarnings("unchecked")
	public static <K, V extends Comparable> Map<K, V> sortByValues(Map<K, V> map) {
		List<Map.Entry<K, V>> entries = new LinkedList<Map.Entry<K, V>>(map
				.entrySet());

		Collections.sort(entries, new Comparator<Map.Entry<K, V>>() {

			@Override
			public int compare(Entry<K, V> o1, Entry<K, V> o2) {
				return o1.getValue().compareTo(o2.getValue());
			}
		});

		Map<K, V> sortedMap = new LinkedHashMap<K, V>();

		for (Map.Entry<K, V> entry : entries) {
			sortedMap.put(entry.getKey(), entry.getValue());
		}

		return sortedMap;
	}

	public static void closeConnection(Connection conn,
			PreparedStatement pstmt, ResultSet rs) {
		try {
			if (rs != null) {
				rs.close();
			}
			if (pstmt != null) {
				pstmt.close();
			}

			if (conn != null) {
				conn.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void closeConnection(Connection conn,
			PreparedStatement pstmt, ResultSet rs, ResultSet rs1) {
		try {
			if (rs1 != null) {
				rs1.close();
			}
			if (rs != null) {
				rs.close();
			}
			if (pstmt != null) {
				pstmt.close();
			}

			if (conn != null) {
				conn.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static String getMetaQuery(String keyword, boolean isNot) {
		if (isNot) {
			return "-(content:\"" + keyword + "\" OR content1:\"" + keyword
					+ "\")";
		} else {
			return "(content:\"" + keyword + "\" OR content1:\"" + keyword
					+ "\")";
		}
	}

	public static String get16Code(String code) {
		while (code.length() <= 6)
			code = "0" + code;
		return code;
	}

	/**
	 * 将指定的对象序列化为json格式的字符串
	 * 
	 * @param dto
	 * @return
	 */
	public static String toJSON(Object dto) {
		Gson gson = new Gson();
		return gson.toJson(dto);
	}

	/**
	 * 通过from描述获得from对应的标识
	 * 
	 * @param from
	 * @return
	 */
	public static int xunkuFromGet(String from) {
		return 0;
		/*
		 * try {
		 * 
		 * return (int) WeiboWebService.addWeiboAppSource(from); } catch
		 * (NumberFormatException e) { // TODO Auto-generated catch block
		 * e.printStackTrace(); } catch (IOException e) { // TODO Auto-generated
		 * catch block e.printStackTrace(); } return 0;
		 */
	}

	public static String xunkuFromNameGet(int from) {
		return "讯库舆情";
	}

	/**
	 * 通过city代码获得city的名称
	 * 
	 * @param xkcityCode
	 * @return
	 */
	public static String xunkuCityNameGet(int xkcityCode) {
		return String.valueOf(xkcityCode);
	}

	/**
	 * 通过平台的city代码获得xunku的城市代码，统一处理城市代码
	 * 
	 * @param platformCity
	 * @param platform
	 * @return
	 */
	public static int xunkuCity(int platformCity, Platform platform) {
		// TODO 需要调用服务
		return platformCity;
	}

	/**
	 * 格式化当前性别显示1=男，2=女，3=未知
	 * 
	 * @param gender
	 * @return
	 */
	public static String getGender(int gender) {
		if (gender == 1)
			return "男";
		if (gender == 2)
			return "女";
		return "未知";
	}

	/**
	 * 通过性别描述获得性别标识
	 * 
	 * @param gender
	 * @return
	 */
	public static int getGender(String gender) {
		if (gender.equals("男")) {
			return 1;
		} else if (gender.equals("女")) {
			return 2;
		}
		return 3;
	}

	/**
	 * 通过from标识获得from描述
	 * 
	 * @param from
	 * @return
	 */
	public static String getFrom(int from) {
		/*
		 * try { if (from == 0) return PortalCST.FORM_UNKNOW; String result =
		 * WeiboWebService.getWeiboAppSource(from); return result; } catch
		 * (IOException e) { e.printStackTrace(); }
		 */
		return PortalCST.FORM_UNKNOW;
	}

	/**
	 * 检查当前字符串是否是null或者为空串
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isNullOrEmpty(String str) {

		if (str == null) {
			return true;
		}

		if (str.length() == 0)
			return true;

		return false;
	}

	/**
	 * 判断当前字符串是否全部是数字
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isNumber(String str) {

		// 空串不是数字
		if (isNullOrEmpty(str)) {
			return false;
		}

		return str.matches("\\d+");
	}

	private static Map<String, SimpleDateFormat> _formatMap = new HashMap<String, SimpleDateFormat>();

	public static Date parseDate(String str, String format) throws AppException {
		if (str == null || "".equals(str)) {
			return null;
		}
		SimpleDateFormat sdf = _formatMap.get(format);
		if (null == sdf) {
			sdf = new SimpleDateFormat(format, Locale.ENGLISH);
			sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
			_formatMap.put(format, sdf);
		}
		try {
			synchronized (sdf) {
				return sdf.parse(str);
			}
		} catch (ParseException pe) {
			throw new AppException("Unexpected format(" + str + ")");
		}
	}

	/**
	 * 根据微博类型获标识得类型枚举
	 * 
	 * @param type
	 * @return
	 */
	public static PostType getPostType(int type) {
		if (type == 2)
			return PostType.Repost;
		if (type == 3)
			return PostType.Comment;

		return PostType.Creative;

	}

	/**
	 * 根据微博类型枚举获得类型标识
	 * 
	 * @param type
	 * @return
	 */
	public static int getPostType(PostType type) {
		int result = 0;
		switch (type) {
		case Creative:
			result = 1;
			break;
		case Repost:
			result = 2;
			break;
		case Comment:
			result = 3;
			break;
		default:
			result = 1;
			break;
		}
		return result;
	}

	/**
	 * 根据应用类型标识获得应用类型枚举
	 * 
	 * @param type
	 * @return
	 */
	public static AppType getAppType(int type) {
		if (type == 1)
			return AppType.Normal;
		return AppType.Crawler;
	}

	/**
	 * 根据应用类型枚举获得类型的标识
	 * 
	 * @param type
	 * @return
	 */
	public static int getAppType(AppType type) {
		if (type == AppType.Normal)
			return 1;
		return 2;
	}

	/**
	 * 获得post对应的url
	 * 
	 * @param post
	 * @return
	 */
	public static String getPostUrl(ITweet post) {
		if (post.getPlatform() == Platform.Sina) {
			if (!Utility.isNullOrEmpty(post.getUcode())) {
				return "http://weibo.com/" + post.getUcode() + "/"
						+ Utility.convertId2Mid(post.getTid());
			}
		}

		if (post.getPlatform() == Platform.Tencent) {
			return "http://t.qq.com/p/t/" + post.getTid();
		}

		if (post.getPlatform() == Platform.Renmin) {
			return "http://t.people.com.cn/" + post.getUcode() + "/"
					+ post.getTid();
		}
		return "";
	}

	public static String getAccountUrl(AppAccount appAcc) {
		if (appAcc.getPlatform() == Platform.Sina) {
			return "http://weibo.com/" + appAcc.getUcode();
		}

		if (appAcc.getPlatform() == Platform.Tencent) {
			return "http://t.qq.com/" + appAcc.getName();
		}

		return "";
	}

	/**
	 * 通过平台标识获得平台枚举
	 * 
	 * @param form
	 * @return
	 */
	public static Platform getPlatform(int form) {
		if (form == 1)
			return Platform.Sina;

		if (form == 2)
			return Platform.Tencent;

		if (form == 5)
			return Platform.Renmin;

		return Platform.UnKnow;
	}

	/**
	 * 通过平台枚举获得平台标识
	 * 
	 * @param platform
	 * @return
	 */
	public static int getPlatform(Platform platform) {
		int result = 0;
		switch (platform) {
		case Sina:
			result = 1;
			break;
		case Tencent:
			result = 2;
			break;
		case Renmin:
			result = 5;
			break;
		case UnKnow:
			break;
		default:
			result = 1;
			break;
		}
		return result;
	}

	/**
	 * 通过平台枚举获得平台标识
	 * 
	 * @param platform
	 * @return
	 */
	public static int getSexform(GenderEnum gender) {
		int result = 0;
		switch (gender) {
		case Male:
			result = 1;
			break;
		case Famale:
			result = 2;
			break;
		case Unknow:
			break;
		default:
			result = 1;
			break;
		}
		return result;
	}

	/**
	 * 将图片字符串用逗号分隔分割成列表
	 * 
	 * @param images
	 * @return
	 */
	public static List<String> getImageList(String images) {
		if (!isNullOrEmpty(images)) {
			String[] ss = images.split(",");
			List<String> result = new ArrayList<String>(Arrays.asList(ss));
			return result;
		}
		return null;
	}

	/**
	 * 将标签用逗号分隔成标签列表
	 * 
	 * @param tags
	 * @return
	 */
	public static List<String> getTags(String tags) {
		if (!isNullOrEmpty(tags)) {
			String[] ss = tags.split(",");
			List<String> result = new ArrayList<String>(Arrays.asList(ss));
			return result;
		}
		return null;
	}

	/**
	 * 通过标签列表获得标签字符串
	 * 
	 * @param tags
	 * @return
	 */
	public static String getTags(List<String> tags) {
		if (tags == null || tags.size() == 0) {
			return "";
		}
		StringBuffer buf = new StringBuffer();
		buf.append(tags.get(0));
		for (int i = 1; i < tags.size(); i++) {
			buf.append("," + tags.get(i));
		}
		return buf.toString().substring(0, buf.toString().length() - 1);
	}

	/**
	 * 通过图片列表获得以逗号分隔的图片字符串
	 * 
	 * @param images
	 * @return
	 */
	public static String getImageList(List<String> images) {
		if (images == null || images.size() == 0) {
			return null;
		}
		StringBuffer buf = new StringBuffer();
		buf.append(images.get(0));
		for (int i = 1; i < images.size(); i++) {
			buf.append("," + images.get(i));
		}
		return buf.toString();
	}

	/**
	 * Member cache 解压处理
	 * 
	 * @param buf
	 * @return
	 * @throws IOException
	 */
	public static byte[] unGzip(byte[] buf) throws IOException {
		GZIPInputStream gzi = null;
		ByteArrayOutputStream bos = null;
		try {
			gzi = new GZIPInputStream(new ByteArrayInputStream(buf));
			bos = new ByteArrayOutputStream(buf.length);
			int count = 0;
			byte[] tmp = new byte[2048];
			while ((count = gzi.read(tmp)) != -1) {
				bos.write(tmp, 0, count);
			}
			buf = bos.toByteArray();
		} finally {
			if (bos != null) {
				bos.flush();
				bos.close();
			}
			if (gzi != null)
				gzi.close();
		}
		return buf;
	}

	/**
	 * Member cache 压缩处理
	 * 
	 * @param val
	 * @return
	 * @throws IOException
	 */
	public static byte[] gzip(byte[] val) throws IOException {
		ByteArrayOutputStream bos = new ByteArrayOutputStream(val.length);
		GZIPOutputStream gos = null;
		try {
			gos = new GZIPOutputStream(bos);
			gos.write(val, 0, val.length);
			gos.finish();
			gos.flush();
			bos.flush();
			val = bos.toByteArray();
		} finally {
			if (gos != null)
				gos.close();
			if (bos != null)
				bos.close();
		}
		return val;
	}

	private static String encode10To62(long int10) {
		String s62 = "";
		int r = 0;
		while (int10 != 0) {
			r = (int) (int10 % 62);
			s62 = PortalCST.STRING_62_KEYS.charAt(r) + s62;
			int10 = (long) Math.floor(int10 / 62.0);
		}
		while (s62.length() < 4) {
			s62 = "0" + s62;
		}
		return s62;
	}

	private static long encode62To10(String str62) {
		long i10 = 0;
		for (int i = 0; i < str62.length(); i++) {
			double n = str62.length() - i - 1;
			i10 += (long) (PortalCST.STRING_62_KEYS.indexOf(str62.charAt(i)) * Math
					.pow(62, n));
		}
		return i10;
	}

	/**
	 * 新浪的mid转换成id
	 * 
	 * @param strMid
	 * @return
	 */
	public static String convertMid2Id(String strMid) {
		String id = "";
		for (int i = strMid.length() - 4; i > -4; i = i - 4) {
			int offset = i < 0 ? 0 : i;
			int len = i < 0 ? strMid.length() % 4 : 4;
			String str = (new Long(encode62To10(strMid.substring(offset, offset
					+ len)))).toString();

			if (offset > 0) {
				str = padLeft(str, 7, '0');
			}

			id = str + id;
		}
		return id;
	}

	/**
	 * 新浪的id转换成mid
	 * 
	 * @param strId
	 * @return
	 */
	public static String convertId2Mid(String strId) {
		String mid = "";
		for (int i = strId.length() - 7; i > -7; i = i - 7) {
			int offset = i < 0 ? 0 : i;
			int len = i < 0 ? strId.length() % 7 : 7;
			String str = encode10To62(Long.parseLong(strId.substring(offset,
					offset + len)));
			mid = str + mid;
		}
		return mid.substring(3);
	}

	private static String padLeft(String string, int totalWidth,
			char paddingChar) {
		StringBuilder sb = new StringBuilder("");

		while (sb.length() + string.length() < totalWidth) {
			sb.append(paddingChar);
		}

		sb.append(string);
		return sb.toString();
	}

	/**
	 * 生成一个随机的ClientID
	 * 
	 * @return
	 */
	public static String genClientID() {
		UUID uid = UUID.randomUUID();
		String str = uid.toString();
		String result = str.substring(0, 8) + str.substring(9, 13)
				+ str.substring(14, 18) + str.substring(19, 23)
				+ str.substring(24);
		return result.toUpperCase();
	}

	/**
	 * 随机获得一个Token
	 * 
	 * @return
	 */
	public static String getToken() {
		return genClientID();
	}

	/**
	 * 计算sourceStr的md5码
	 * 
	 * @param sourceStr
	 * @return
	 */
	public static String MD5(String sourceStr) {
		String result = "";
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(sourceStr.getBytes());
			byte b[] = md.digest();
			int i;
			StringBuffer buf = new StringBuffer("");
			for (int offset = 0; offset < b.length; offset++) {
				i = b[offset];
				if (i < 0)
					i += 256;
				if (i < 16)
					buf.append("0");
				buf.append(Integer.toHexString(i));
			}
			result = buf.toString();
		} catch (NoSuchAlgorithmException e) {
			System.out.println(e);
		}
		return result;
	}

	public static String join(List<String> list, String split) {
		StringBuilder sb = new StringBuilder();
		if (list == null)
			return "";
		if (list.size() == 0)
			return "";
		sb.append(list.get(0));
		for (int i = 1; i < list.size(); i++) {
			sb.append(split);
			sb.append(list.get(i));
		}
		return sb.toString();
	}

	public static void main(String[] args) {
		String id = Utility.convertMid2Id("BodbzFcYQ");
		String mid = Utility.convertId2Mid("3755392737635686");
		System.out.println(mid);
		System.out.println(id);

		System.out.println(System.currentTimeMillis());
		System.out.println(System.currentTimeMillis());
		System.out.println(System.currentTimeMillis());
		System.out.println(System.currentTimeMillis());
		System.out.println(System.currentTimeMillis());
		System.out.println(System.currentTimeMillis());
		System.out.println(System.currentTimeMillis());
		System.out.println(System.currentTimeMillis());

		try {

			Date start = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss")
					.parse("2014-09-01 00:00:01");
			Date end = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss")
					.parse("2014-09-30 23:59:59");

			System.out.println(start.getTime());
			System.out.println(end.getTime());

			
			

		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println("id: " + id + " mid: " + mid);

	}

}
