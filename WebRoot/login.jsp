<%@ page language="java" pageEncoding="utf-8"%>
<%@page import="com.xunku.app.manager.*"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";

	int times = UserSessionManager.getInstance().getLoginTimes(request);

	// 设置客户端的标识
	UserSessionManager.getInstance().setClientID(request, response);
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<base href="<%=basePath%>" />
		<title>云微方社交媒体管理平台</title>
		<link rel="shortcut icon" href="themes/modules/images/weibo.ico"></link>

		<style type="text/css">
* {
	margin: 0px;
	padding: 0px;
	font-family: Microsoft YaHei, Helvetica, sans-serif;
	font-size: 12px;
}

* :focus {
	outline: none;
}

.login_clear {
	clear: both
}

.login_none {
	display: none
}

.com_fLeft {
	float: left
}

body {
	background-color: #f3f3f3;
	position: relative
}

#top {
	width: 100%;
	height: 10px;
	background-color: #1b9ba4;
}

#logo {
	width: 100%;
	height: 100px;
	background-color: #f3f3f3;
}

#logo .logo {
	width: 1000px;
	padding-left: 20px;
	height: 116px;
	margin: 0 auto;
	background: url(themes/modules/images/login_03.gif) no-repeat;
}

#banner {
	width: 100%;
	height: 410px;
	background: url(themes/modules/images/banerBack.jpg) no-repeat center
}

#banner .banner {
	width: 1000px;
	height: 406px;
	padding: 4px;
	margin: 0 auto;
}

#banner .bannerLeft {
	width: 621px;
	height: 406px;
	overflow: hidden;
	float: left;
	position: relative
}

.bannerLeft .control_button {
	position: absolute;
	left: 0;
	bottom: 0;
	height: 25px;
	line-height: 25px;
}

.control_button .span_style {
	float: left;
}

#banner .bannerRight {
	width: 300px;
	height: 100%;
	float: right;
}

.bannerRight .loginStyle {
	width: 100%;
	height: 40px;
}

.bannerRight .login {
	width: 298px;
	padding-left: 42px;
	height: 330px;
	padding-top: 30px;
	background: url(themes/modules/images/login_09.gif) no-repeat
}

.login .table_style {
	width: 215px;
}

.login .td_style {
	width: 215px;
	margin-top: 10px;
	height: 35px;
}

.td_style .img {
	border: #eee solid 1px
}

.login .td_styleBack {
	padding-left: 10px;
	background: url(themes/modules/images/loginButton.gif) no-repeat;
}

.login .td_styleBack:hover {
	background: url(themes/modules/images/loginButton.gif) no-repeat 0 -39px
		;
}

.login .inputStyle {
	width: 195px;
	padding: 10px 5px;
	background: none;
	border: 0
}

.yzmIput {
	width: 103px;
	height: 32px;
	line-height: 32px;
	background-color: #fff;
	border: #eee solid 1px
}

#zd_login {
	cursor: pointer
}

.zd_loginSel {
	margin: 10px 5px 0 0; *
	margin: 6px 2px 0 -3px;
}

.zd_loginSel2 {
	margin-top: 8px;
	color: #eee
}

.change_pic {
	heght: 20px;
	line-height: 20px;
	font-size: 12px;
	color: #E6E6E6
}

.change_pic:hover {
	color: #fff;
	cursor: pointer
}

.change_tip {
	color: #f00;
	margin-top: 3px;
}

.login .start_login {
	background: url(themes/modules/images/loginButton.gif) no-repeat 0 -81px
		;
}

.login .start_login:hover {
	background: url(themes/modules/images/loginButton.gif) no-repeat 0
		-119px;
	cursor: pointer
}

.passw_back {
	width: 100%;
	height: 25px;
	line-height: 25px;
	text-align: center;
	color: #eee
}

.passw_back:hover {
	color: #fff
}

#foot {
	width: 100%;
	height: 30px;
	line-height: 30px;
	color: #E6E6E6;
	font-size: 12px;
	text-align: center;
	background-color: #19a3ad;
	position: absolute;
	left: 0;
	bottom: 0
}
</style>

		<meta http-equiv="x-dns-prefetch-control" content="on" />
		<meta http-equiv="Content-Type" content="text/html; charset=utf8" />
		<meta http-equiv="pragma" content="no-cache" />
		<meta http-equiv="cache-control" content="no-cache" />
		<meta http-equiv="expires" content="0" />
		<meta http-equiv="keywords" content="讯库微博系统,讯库舆情,讯库微博,舆情微博" />
		<meta http-equiv="description" content="讯库微博系统登录" />

		<script type="text/javascript">
			var xkPath = "<%=path%>/",
				  indexPath = "<%=path + "/index.jsp"%>";
		</script>
		<script type="text/javascript" src="scripts/lib/jquery-1.9.1.min.js"></script>
		<script type="text/javascript" src="scripts/plugin/jquery.tool.js"></script>
		<script type="text/javascript" src="scripts/plugin/jquery.cookie.js"></script>
		<script type="text/javascript">
		$(function() {
			var times = <%=times%>,
				  userNameInput = $('#userName'), //用户名称
				  userName = $[TM](userNameInput.val()),
				  userPwdInput = $('#userPwd'), //用户密码
				  pingCodeInput = $('#pingCode'), //验证码输入框
				  imgCode = $('#imgCode'), //验证码图片对象
				  changeImage = $('#changeImage'), //切换验证吗span对象
				  loginStateTip = $('#loginStateTip'), //登陆状态提示信息对象
				  trCode = $('#trCode'), //验证tr行对象
				  isAutoLogin = $('#isAutoLogin'), //是否自动登陆
				  loginBtn = $('#loginBtn'); //登陆按钮
				  
		  	var userNameTip = '用户名',
		  		  userPwdTip = '密码';
				  
		    userNameInput.txt({text: userNameTip });
		    userPwdInput.txt({text: userPwdTip });
		    
	    	var wh=$(document).height();
  			$("body").css("height",wh);
				  
		   	userNameInput.focus();
			//判断是否点击过是自动登陆
			if(userName != '' && $.cookie(userName)) {
				isAutoLogin[0][CD] = TE;
			}
			isAutoLogin[EV](CK, function () {
				loginStateTip.text('');
				if(userName != '') {
					//保存到cookie60天
					$.cookie(userName, 1, { 
						path: '/', 
						expires: 60 
					}); 
					this.value = 1;
				} else {
					loginStateTip.text('请先输入用户名后，自动登陆才能管用');
				}
			});
		   
			//是否有验证码
			if(times > 3) {
				trCode.show();
			} else {
				trCode.hide();
			}
			if(times == 11) {
				loginStateTip.text('账号已被锁定,一小时后在登陆');
				loginBtn[RM]();
			}
			//切换验证码
			changeImage[EV](CK, function() {
				loginStateTip.text('');
				imgCode[0].src = 'vode.jsp?r=' + getCurrentTime();
			});
			//登陆
			var loginTimes=0;
			loginBtn[EV](CK, function() {
				loginStateTip.text('');
				//验证
				if(repExp()) {
					//alert($.toJson({un: $[TM](userNameInput.val()), pwd: $[TM](userPwdInput.val()), v: $[TM](pingCodeInput.val()), rem: isAutoLogin[0].value}));
					$(BY)[AJ]({
						servletName: 'login.action',
						param: {un: $[TM](userNameInput.val()), pwd: $[TM](userPwdInput.val()), v: $[TM](pingCodeInput.val()), rem: isAutoLogin[0].value},
						success: function(data) {
							loginTimes = data.times;
							//$.alertParam($.toJson(data), data.status, data.status == 'SUCESS', typeof data.status);
							if(data.status == 'SUCESS') {
								WD.location.href = indexPath;
							} else {
								if(data.times == 10) {
									loginStateTip.text('账号已被锁定,一小时后在登陆');
									loginBtn[RM]();
								} else {
									if(data.times >= 3) {
										$('#trCode').show();
										changeImage[TR](CK);
									} else {
										$('#trCode').hide();
									}
									$('#loginStateTip').show().text(data.message);
								}
							}
						}
					});
				}
			})
			
			//用户名称输入框回车事件
			userNameInput[EV]('focus', function() {
				var $this = $(this);
				if ($this.val() == '' || $this.val() == userNameTip) $this.val('').css('color', '#000');
				$this[EV](KU, function(event) {
					if(event.which == 13) {
						userPwdInput[TR]('focus');
					}
				});
			});
			
			//密码输入框回车事件
			userPwdInput[EV]('focus', function() {
				var $this = $(this);
				if ($this.val() == '' || $this.val() == userPwdTip) $this.val('').css('color', '#000');
				$this[EV](KU, function(event) {
					if(event.which == 13) {
						loginBtn[TR](CK);
					}
				});
			});
			
			//获取当前时间
			function getCurrentTime () {
				var myDate = new Date();  
				return myDate.getTime();
			}
			
			//登陆默认验证
			function repExp() {
				var userName = $[TM](userNameInput.val()),
					  userPwd = $[TM](userPwdInput.val()),
					  pingCode = $[TM](pingCodeInput.val());
			    userName = userName[RP](userNameTip, '');
			    userPwd = userPwd[RP](userPwdTip, '');
				if (userName == '' ) {
					loginStateTip.text('用户名不能为空');
					return FE;
				}
				if (userPwd == '' ) {
					loginStateTip.text('用户密码不能为空');
					return FE;
				}
				if (trCode.is(':' + VB) && pingCode == '') {
					loginStateTip.text('验证码不能为空');
					return FE;
				}
				return TE;
			}
		});
		
		$(function(){
		  var wh=$(document).height();
		  $("body").css("height",wh);
		  //tupianqiehuan
		   var len=$(".bannerLeft").find("img").length;
		   for(i=0;i<len;i++){
			 $(".control_button").append("<span class='span_style'></span>") ;
		   }
		   var index=1;
		   var int;
		   function showSys(num)  //图片切换函数
		   {
		    $(".bannerLeft img").eq(num).fadeIn(1000);
		    $(".bannerLeft img").eq(num).siblings("img").hide();
		   }
		   function ziDong()  //自切换
		   {
		    if(index==len)
		    {
		     index=0;
		    }
		    showSys(index);
		    index=index+1;
		   }
		   int=setInterval(ziDong,6000);
		   $(".control_button .span_style").click(function() //点击切换
		   {
		    var index_num=$(".control_button>span").index(this);
		    showSys(index_num);
		    index=index_num+1;  //改变全局变量值便鼠标移候能够衔接
		   }); 
		  $(".bannerLeft img").hover(function(){
			 clearInterval(int);
		  },function(){
			  int=setInterval(ziDong,6000);
		  });
		})
		</script>
	</head>

	<body id="loginBody">
		<!--top-->
		<div id="top"></div>
		<!--logo-->
		<div id="logo">
			<div class="logo"></div>
		</div>
		<!--banner-->
		<div id="banner">
			<div class="banner">
				<div class="bannerLeft">
					<img src="themes/modules/images/login_06.jpg" width="621"
						height="406" />
					<img src="themes/modules/images/login_07.jpg" width="621"
						height="406" class="login_none" />
					<img src="themes/modules/images/login_08.jpg" width="621"
						height="406" class="login_none" />
					<div class="control_button"></div>
				</div>
				<div class="bannerRight">
					<div class="loginStyle"></div>
					<div class="login">
						<table class="table_style">
							<tr>
								<td>
									<div class="td_style td_styleBack">
										<input id="userName" value="" type="text" class="inputStyle" />
									</div>
								</td>
							</tr>
							<tr>
								<td>
									<div class="td_style td_styleBack">
										<input id="userPwd" value="" type="password"
											class="inputStyle" />
									</div>
								</td>
							</tr>
							<tr>
								<td>
									<input type="checkbox" name="rem" id="isAutoLogin" value="0"
										class="zd_loginSel com_fLeft" />
									<label for="isAutoLogin" class="com_fLeft zd_loginSel2">
										自动登录
									</label>
									<div class="login_clear"></div>
								</td>
							</tr>
							<tr id="trCode">
								<td>
									<div class="td_style">
										<input id="pingCode" type="text" class="com_fLeft yzmIput" />
										<img id="imgCode" class="com_fLeft img" width="107"
											height="32" action-type="btn_change_verifycode"
											node-type="verifycode_image" src="vode.jsp?r=randomNum" />
										<div class="login_clear"></div>
									</div>
									<span id="changeImage" class="change_pic com_fLeft">看不清,换张图片</span>
								</td>
							</tr>
							<tr>
								<td>
									<div class="change_tip" id="loginStateTip">

									</div>
									<div id="loginBtn" class="td_style start_login">
									</div>
								</td>
							</tr>
						</table>
					</div>
				</div>
			</div>
			<div class="login_clear"></div>
		</div>
		<!--foot-->
		<div id="yuliu"></div>
		<div id="foot">
			Copyright © MaxTech 2011. All Rights Reserved
			&nbsp;&nbsp;京ICP备11002306号-1&nbsp;&nbsp; 京公海网安备110108001970号
		</div>
	</body>
</html>