package com.xunku.portal;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.google.gson.internal.Streams;
import com.xunku.app.manager.UserSessionManager;
import com.xunku.app.model.UserInfo;
import com.xunku.utils.DateUtils;
import com.xunku.utils.PropertiesUtils;

public class UploadifyServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@SuppressWarnings( { "deprecation", "deprecation", "unchecked" })
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse resp)
			throws ServletException, IOException {
		// 设置接收的编码格式
		request.setCharacterEncoding("UTF-8");
		Date date = new Date();// 获取当前时间
		SimpleDateFormat sdfFileName = new SimpleDateFormat("yyyyMMddHHmmss");
		SimpleDateFormat sdfFolder = new SimpleDateFormat("yyMM");
		String newfileName = sdfFileName.format(date);// 文件名称
		String fileRealPath = "";// 文件存放真实地址

		String fileRealResistPath = "";// 文件存放真实相对路径

		// 名称 界面编码 必须 和request 保存一致..否则乱码
		String name = request.getParameter("name");

		String firstFileName = "";
		// 获得容器中上传文件夹所在的物理路径
		String savePath = this.getServletConfig().getServletContext()
				.getRealPath("/")
				+ "uploads\\" + newfileName + "\\";
		System.out.println("路径" + savePath + "; name:" + name);
		File file = new File(savePath);
		if (!file.isDirectory()) {
			file.mkdirs();
		}

		try {
			DiskFileItemFactory fac = new DiskFileItemFactory();
			ServletFileUpload upload = new ServletFileUpload(fac);
			upload.setHeaderEncoding("UTF-8");
			// 获取多个上传文件
			List fileList = fileList = upload.parseRequest(request);
			// 遍历上传文件写入磁盘
			Iterator it = fileList.iterator();
			while (it.hasNext()) {
				Object obit = it.next();
				if (obit instanceof DiskFileItem) {
					System.out.println("xxxxxxxxxxxxx");
					DiskFileItem item = (DiskFileItem) obit;

					// 如果item是文件上传表单域
					// 获得文件名及路径
					String fileName = item.getName();
					if (fileName != null) {
						firstFileName = item.getName().substring(
								item.getName().lastIndexOf("\\") + 1);
						String formatName = firstFileName
								.substring(firstFileName.lastIndexOf("."));// 获取文件后缀名
						fileRealPath = savePath + newfileName + formatName;// 文件存放真实地址

						BufferedInputStream in = new BufferedInputStream(item
								.getInputStream());// 获得文件输入流
						BufferedOutputStream outStream = new BufferedOutputStream(
								new FileOutputStream(new File(fileRealPath)));// 获得文件输出流

						// 上传成功，则插入数据库
						if (new File(fileRealPath).exists()) {
							// 虚拟路径赋值
							fileRealResistPath = sdfFolder.format(date)
									+ "/"
									+ fileRealPath.substring(fileRealPath
											.lastIndexOf("\\") + 1);
							// 保存到数据库
							System.out.println("保存到数据库:");
							System.out.println("name:" + name);
							System.out.println("虚拟路径:" + fileRealResistPath);
						}

					}
				}
			}
		} catch (org.apache.commons.fileupload.FileUploadException ex) {
			ex.printStackTrace();
			System.out.println("没有上传文件");
			return;
		}
		resp.getWriter().write("1");
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		req.setCharacterEncoding("UTF-8"); // 从request中取时, 以UTF-8编码解析
		// String savePath = this.getServletConfig().getServletContext()
		// .getRealPath("");
		String clientID = req.getParameter("cid");
		UserInfo userInfo = UserSessionManager.getInstance().getUserInfo(clientID);
		int Userid = userInfo.getBaseUser().getId();
		
		String savePath = PropertiesUtils.getString("config",
				"userWeiboImageWritePath")
				+ File.separator + Userid + File.separator;
		

		File f1 = new File(savePath);
		System.out.println(savePath);
		if (!f1.exists()) {
			f1.mkdirs();
		}
		DiskFileItemFactory fac = new DiskFileItemFactory();
		ServletFileUpload upload = new ServletFileUpload(fac);
		upload.setHeaderEncoding("utf-8");
		List fileList = null;
		try {
			fileList = upload.parseRequest(req);
		} catch (FileUploadException ex) {
			return;
		}
		Iterator<FileItem> it = fileList.iterator();
		String name = "";
		String extName = "";
		String sizeTip = "";
		int imageID = 0;
		while (it.hasNext()) {
			FileItem item = it.next();
			if (!item.isFormField()) {
				name = item.getName();
				long size = item.getSize();
				if (size > 1024 * 1024 * 5) {
					sizeTip = "图片容量大小超出指定范围，请重新上传图片";
					break;
				}
				String type = item.getContentType();
				if (name == null || name.trim().equals("")) {
					continue;
				}
				// 扩展名格式：
				if (name.lastIndexOf(".") >= 0) {
					extName = name.substring(name.lastIndexOf("."));
				}
				File file = null;
				do {
					// 生成文件名：
					name = String.valueOf(DateUtils.getImageNameCode());
					file = new File(savePath + name + extName);

				} while (file.exists());
				File saveFile = new File(savePath + name + extName);
				try {
					item.write(saveFile);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		if (sizeTip != "")
			resp.getWriter().print("sizeError");
		else
			resp.getWriter().print(
					"$/" + Userid + "/" + name + extName + "," + name);
	}

}
