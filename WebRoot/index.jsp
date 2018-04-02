<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="com.xunku.utils.*"%>
<%@page import="com.xunku.dto.app.*"%>
<%@page import="com.xunku.app.*"%>
<%@page import="com.xunku.app.manager.*"%>
<%@page import="com.xunku.app.enums.*"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	String logoImagePath = PropertiesUtils.getString("config",
			"logoImagePath");
	String faceImagePath = PropertiesUtils.getString("config",
			"faceImagePath");
	String userWeiboImagePath = PropertiesUtils.getString("config",
			"userWeiboImagePath");
	String sinaAppJSON = Utility.toJSON(AppDTO.get(AppServerProxy
			.getMainApp(Platform.Sina)));
	String tencAppJSON = Utility.toJSON(AppDTO.get(AppServerProxy
			.getMainApp(Platform.Tencent)));

	// 设置客户端的标识
	UserSessionManager.getInstance().setClientID(request, response);

	String javascript_min = PropertiesUtils.getString("config",
			"javascript_min");
	String javascript_version = PropertiesUtils.getString("config",
			"javascript_version");

	// 判断是否压缩版本还是不压缩版本，版本改变后自动清除客户端缓存
	String dataMain = "";
	String src = "";
	if (javascript_min.equals("0")) {
		src = "";
		dataMain = "modules/program";
	} else {
		dataMain = "";
		src = "modules/program.js?rnd = " + javascript_version;
	}
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<base href="<%=basePath%>" />
		<title>云微方社交媒体管理平台</title>
		
		<link rel="stylesheet" href="themes/modules/main.css" type="text/css" />
		<link rel="shortcut icon" href="themes/modules/images/weibo.ico"></link>
		
		<meta http-equiv="pragma" content="no-cache" />
		<meta http-equiv="cache-control" content="no-cache" />
		<meta http-equiv="expires" content="0" />
		<meta http-equiv="keywords" content="keyword1,keyword2,keyword3" />
		<meta http-equiv="description" content="This is my page" />
		<meta http-equiv="X-UA-Compatible" content="IE=EmulateIE8" />


		<script type="text/javascript">
			var xkPath ="<%=path%>/";
			var basePath = "<%=basePath%>";
			var loginPath = "<%=path + "/login.jsp"%>"
			var logoImagePath = "<%=logoImagePath%>";
			var faceImagePath ="<%=faceImagePath%>";
			var userWeiboImagePath = "<%=userWeiboImagePath%>";
			var $app = <%=sinaAppJSON%>;
			var $tapp= <%=tencAppJSON%>;
			
			
			var javascript_min = "<%=javascript_min%>";
			var javascript_version = "<%=javascript_version%>";
			var dataMain = "<%=dataMain%>";
			var src = "<%=src%>";
			
		</script>
		<script data-main="<%=dataMain %>" src="scripts/lib/require-2.1.2.min.js"></script>
		<script type="text/javascript" src="<%=src %>"></script>

	</head>

	<body id="program">
		<br />
		<br />
	</body>
</html>
