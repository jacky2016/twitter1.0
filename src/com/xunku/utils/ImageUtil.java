package com.xunku.utils;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageOutputStream;
import javax.imageio.stream.MemoryCacheImageOutputStream;

import com.xunku.app.model.ImageStruct;
import com.xunku.app.model.UploadImage;

public class ImageUtil {

	static String IMG_SERVER;
	static {
		try {
			IMG_SERVER = PropertiesUtils.getString("config",
					"userWeiboImagePath");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static UploadImage getImages(String url) {
		UploadImage image = new UploadImage();
		String imgName = url.substring(url.lastIndexOf("/") + 1);
		image.setName(imgName);
		// 把文件存在一个字节数组中
		byte[] filea = getImageByUrl(url);
		image.setContent(filea);
		return image;
	}

	public static UploadImage getImages(String[] urls) {
		UploadImage image = new UploadImage();
		image.setName("pic");

		for (int i = 0; i < urls.length; i++) {
			if (urls[i].contains("$")) {
				urls[i] = urls[i].replace("$", IMG_SERVER);
			}
		}
		image.setContent(yPic(urls));
		return image;
	}

	public static byte[] getImageByUrl(String strUrl) {
		try {

			if (strUrl.contains("$")) {
				strUrl = strUrl.replace("$", IMG_SERVER);
			}
			URL url = new URL(strUrl);

			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setConnectTimeout(5 * 1000);
			InputStream inStream = conn.getInputStream();// 通过输入流获取图片数据
			byte[] btImg = readInputStream(inStream);// 得到图片的二进制数据
			return btImg;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;

	}

	public static byte[] readInputStream(InputStream inStream) throws Exception {
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len = 0;
		while ((len = inStream.read(buffer)) != -1) {
			outStream.write(buffer, 0, len);
		}
		inStream.close();
		return outStream.toByteArray();
	}

	public static byte[] yPic(String[] imageUrls) {

		try {

			if (imageUrls == null)
				return null;

			if (imageUrls.length <= 0)
				return null;

			List<ImageStruct> imageList = new ArrayList<ImageStruct>();
			for (int i = 0; i < imageUrls.length; i++) {
				// File fileOne = new File(imageUrls[i]);
				BufferedImage buffer = ImageIO.read(new URL(imageUrls[i]));
				ImageStruct image = new ImageStruct();
				image.setWidth(buffer.getWidth());
				image.setHeight(buffer.getHeight());

				int[] imageArray = new int[image.getWidth() * image.getHeight()];

				image.setImage(buffer.getRGB(0, 0, image.getWidth(), image
						.getHeight(), imageArray, 0, image.getWidth()));
				imageList.add(image);

			}

			int width = findMaxWidth(imageList);
			int height = calcMaxHeight(imageList);
			// 生成新图片
			BufferedImage imageResult = new BufferedImage(width, height,
					BufferedImage.TYPE_INT_RGB);
			for (int i = 0; i < imageList.size(); i++) {
				ImageStruct image = imageList.get(i);
				imageResult.setRGB(0, getStartY(i, imageList),
						image.getWidth(), image.getHeight(), image.getImage(),
						0, image.getWidth());
			}
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ImageOutputStream stream = new MemoryCacheImageOutputStream(baos);
			// File outFile = new File("D:\\out.png");
			ImageIO.write(imageResult, "png", stream);// 写流
			// ImageIO.write(imageResult, "png", outFile);// 写文件

			return baos.toByteArray();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private static int getStartY(int index, List<ImageStruct> images) {
		if (index == 0) {
			return 0;
		} else {
			int height = 0;
			for (int i = 0; i < index; i++) {
				height += images.get(i).getHeight();
			}
			return height;
		}

	}

	private static int calcMaxHeight(List<ImageStruct> images) {
		if (images.size() <= 0)
			return 0;

		int result = 0;
		for (int i = 0; i < images.size(); i++) {
			result += images.get(i).getHeight();
		}
		return result;
	}

	private static int findMaxWidth(List<ImageStruct> images) {
		if (images.size() <= 0)
			return 0;

		int result = images.get(0).getWidth();
		for (int i = 1; i < images.size(); i++) {
			if (result < images.get(i).getWidth()) {
				result = images.get(i).getWidth();
			}
		}

		return result;
	}
}