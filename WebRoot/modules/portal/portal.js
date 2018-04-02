//主模块，入口模块
define(['jquery'], function ($) {
    //定义模块变量
    var key = 'portal',
          $key,
          
          navigationBars, base, currentUser, twitterEnum, mediator, collect,
          portal,
          
          messageBox,
          warningBox,
          
          
          logo,
          userName,
          gotoTaskNotify,
          taskNotifyCount,
          gotoAlertInfo,
          alertInfoCount,
          loadMenu,

		  banner, //标志图片对象
          contentDiv, //内容区DIv
          
          timer,

		  programDiv = $('#program');
		  
	//private method
	function Init() {
		GetUserPmiTemplate();
		LoadUser();
		ProjectGlobal();
		GetMessageNotify();
		RepeatMessageNotify();
		GotoAlertInfoModule();
		
	}
	
	// 触发账号授权绑定功能
	function TriggerBindAccount() {
		var isAdmin = currentUser.UserInfo.isAdmin;
		// 管理员
		if (isAdmin) {
			$(BY)[AJ]({
				param: { action: 'sysManager.GetAccount', pageIndex: 1, pageSize: 99999 },
				success: function (data) {
					AdminBindAccount(data);
				}
			});
		} else { // 普通用户
			$(BY)[AJ]({
			   param: { action: 'sysManager.GetMethodAccount' },
			   success: function(data) {
				   var array = [];
				   if(data != null) {
					   for(var i=0;i<data.length;i++){
					   		array.push({id: data[i].id, "uid": data[i].uid, "name": data[i].name, "type": data[i].type,isExpire: data[i].flag});
					   }
				   }
				   UserBindAccount(array);
				}
			});
		}
	}
	
	// 管理员处理账号分支
	function AdminBindAccount (data) {
		// 一个账号都没有
		if (data.realcount == 0) {
			mediator.PortalToAccountBinding(TE);
		} else {
			var expireCount = 0;
			$[EH](data.rows, function(i, row) {
				if(row.liveDay == 0) {
					expireCount += 1;
				}
			});
			
			// 有一个或者全都到期
			if(expireCount > 0) {
				$[MB]({
					content: '您有 ' + expireCount + ' 个账号授权到期,请将帐号延长授权后使用', 
					type: 4,
					confirmText: '确定',
	        		cancelText: '取消',
					onAffirm: function (state) { 
						if(state) {
							mediator.PortalToAccountBinding(FE);
						}
					}
				});
			} else { // 都没到期
			
			}
		}
	}
	
    // 普通处理账号分支
	function UserBindAccount(data) {
		// 一个账号都没有
		if (data[LN] == 0) {
			// 有绑定账号菜单权限
			if(IsBindAccount()) {
				$[MB]({
					content: '请选授权账号', 
					type: 4,
					confirmText: '确定',
            		cancelText: '',
					onAffirm: function (state) { 
						mediator.PortalToAccountBinding(FE);
					}
				});
			} else { // 没有绑定账号菜单权限
				$[MB]({
					content: '请联系管理员授权账号后在登陆', 
					type: 4,
					confirmText: '确定',
            		cancelText: '',
					onAffirm: function (state) { 
						WD.location.href = loginPath;
					}
				});
			}
		} else {
			var expireCount = 0;
			$[EH](data, function(i, row) {
				if(row.isExpire) {
					expireCount +=1;
				}
			});
			
			// 有一个或者全都到期
			if(expireCount > 0) {
				// 有绑定账号菜单权限
				if(IsBindAccount()) {
					$[MB]({
						content: '您有 ' + expireCount + ' 个账号授权到期,请将帐号延长授权后使用', 
						type: 4,
						confirmText: '确定',
	            		cancelText: '取消',
						onAffirm: function (state) { 
							if(state) {
								mediator.PortalToAccountBinding(FE);
							}
						}
					});
				} else { // 没有绑定账号菜单权限
					$[MB]({
						content: '请联系管理员处理授权到期账号', 
						type: 4,
						confirmText: '确定',
	            		cancelText: '',
						onAffirm: function (state) { 
							
						}
					});
				}
			} else { // 都没到期

			}
		}
	}
	
	function IsBindAccount () {
		if(('.loadMenu', $key)[FD]('#accountBindingItem')[LN] > 0) {
			return TE;
		}
		return FE;
	}
	
	function GetUserPmiTemplate() {
		$(BY)[AJ]({
			param: { action: 'portal.userPmiTemplate' },
			success: function(data) {
				currentUser.Templates = data;
			}
		});
	}
	
	//微博项目全局方法
	function ProjectGlobal() {
		//开启返回顶部功能
	 	$.returnTop({color: '#23c1cc'});
	 	var RegisterHiddenPanel='RegisterHiddenPanel';
	 	base[RegisterHiddenPanel]({ triggerKey: 'userInfo', panelKey: '#userSet' })
				 [RegisterHiddenPanel]({ triggerKey: 'datePickerInput', panelKey: '.datePickAll' })
				 [RegisterHiddenPanel]({ triggerKey: 'currentAccountInfo', panelKey: '#currentAccountPanel' })
				 [RegisterHiddenPanel]({ triggerKey: 'currentAccountInfo', panelKey: '#myRe_1' })
				 [RegisterHiddenPanel]({ triggerKey: 'currentAccountInfo', panelKey: '#talkMe_DrowDown' })
				 [RegisterHiddenPanel]({ triggerKey: 'currentAccountInfo', panelKey: '#da_aa' })
				 [RegisterHiddenPanel]({ triggerKey: 'selectAccount', panelKey: '#accountPanel' })
				 [RegisterHiddenPanel]({ triggerKey: 'selectFace', panelKey: '#facePanel' })
				 [RegisterHiddenPanel]({ triggerKey: 'pulldownSelect', panelKey: '.list' });
				 

		gotoAlertInfo.quake({
			width: 8,
            height: 0,
            speed: 150
		});
		gotoTaskNotify.quake({
			width: 0,
            height: 8,
            speed: 150
		});
	}
	
	// 检查事件任务通知和预警信息模块顶部权限
	function ChecktPmi () {
		if($('#taskNotifyItem', $key)[LN] == 0) {
			gotoTaskNotify[RM]();
		}
		
		if($('#alertInfoItem')[LN] == 0) {
			gotoAlertInfo[RM]();
		}
	}
	
	//读取用户信息方法
	function LoadUser() {
		currentUser.GetUserInfo(function() {
			var user = currentUser.UserInfo;
			SetUser(user);
			LoadMenu(function() {
				OnClick();
				ChecktPmi();
				TriggerBindAccount();
			});
		});
	}
	
	//设置用户信息方法
	function SetUser(userInfo) {
		var path = userInfo.logoPath[RP]('$/', logoImagePath);
		//设置当前登录用户logo
		logo.css('background', 'url(' + path + ') no-repeat');
		userName.text(userInfo.userName);
	}

	//获取协同办公任务通知
	function GetMessageNotify() {
		messageBox.GetCount(function(messageCount, alertCount) {
			var oldMessageCount = parseInt(taskNotifyCount.text()),
				   oldAlertCount = parseInt(alertInfoCount.text());
			
			//运动
			if(messageCount > oldMessageCount) {
				gotoTaskNotify.quake({play: TE});
			}
			if(alertCount > oldAlertCount) {
				gotoAlertInfo.quake({play: TE});
			}
			//5秒后停止运动
			SM(function () {
				gotoTaskNotify.quake({play: FE});
				gotoAlertInfo.quake({play: FE});
			}, 5000);
			taskNotifyCount.text(messageCount);
			alertInfoCount.text(alertCount);
			GotoTaskNotifyModule();
		});
	}
	
	function RepeatMessageNotify() {
		setInterval(function(){
			GetMessageNotify();
		}, 180000); //三分钟更新一次
	}
	
	//进入协同办公任务通知模块
	function GotoTaskNotifyModule() {
		gotoTaskNotify[EV](CK, function() {
			mediator.PortalToTaskNotify();
		});
	}
	
	//进入预警服务的预警信息模块
	function GotoAlertInfoModule() {
		gotoAlertInfo[EV](CK, function () {
			mediator.PortalToAlertInfo();
		});
	}
	//用户设置
	function UserSet(userName) {
		OpenUserSet();
	}
	
	function UserHelp() {
		alert('帮助说明');
	}
	
	function UserExit() {
		$(BY)[AJ]({
			servletName: 'logout.action',
			dataType: 'text',
			success: function(data) {
				//$.alertParam(data, typeof data);
				if (data == '1') {
					WD.location.href = loginPath;
				} else {
					$[MB]({content: '退出失败，请与管理员联系', type: 2});
				}
			}
		});
	}
	
	//打开用户设置窗体
	function OpenUserSet() {
		var user = currentUser.UserInfo,
			   settingTemplate = '<div class="giveMe_do person_set"><h3>帐号：{0}</h3><ul class="p_setList"><li><label for="personPhone">电&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;话：</label><input type="text" value="{1}" id="personPhone" maxLength="11" /></li><li><label for="personEmail">邮&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;箱：</label><input type="text" value="{2}" id="personEmail"/></li><li class="per_changePassWord" id="editPwd">修改密码？</li></ul><div class="com_clear"></div><ul class="chage_password p_setList com_none" id="pwdEdit"><li><label for="oldPwd">原始密码：</label><input type="password" value="" id="oldPwd"/></li><li><label for="newPwd">新&nbsp;密&nbsp;&nbsp;码：</label><input type="password" value="" id="newPwd" maxlength="20" /></li><li><label for="aginPwd">确认密码：</label><input type="password" value="" id="aginPwd" maxlength="20" /></li></ul><div class="com_clear"></div><div class="push_ok"><div class="com_fLeft" id="tip" style=" color:red;"></div><div class="in_MsetCno" id="personCancel">取&nbsp;&nbsp;消</div><div class="in_MsetCok" id="personOK">确&nbsp;&nbsp;定</div></div></div>';
		$[WW]({
	        css: { "width": "420px", "height": "auto"},
	        title: '设置',
	        content: $[FO](settingTemplate,userName.text(), user.phone, user.email),
	        id: '_userSet',
	        onLoad: function(div) {
	        	SettingOption(div);
	        }
		});
	}
	
	//设置用户信息操作
	function SettingOption(div) {
		function close() { 
			personPhone.val('');
		    personEmail.val('');
	        oldPwd.val('');
	        newPwd.val('');
	        aginPwd.val('');
		 	pwdEdit.hide();
		 	$[RM]({ obj: '#_userSet'});
		 	$[RM]({obj: '#' + JLY });
		} 
		var editTip = $('#editPwd', div),
			   personPhone = $('#personPhone', div),
			   personEmail = $('#personEmail', div),
			   oldPwd = $('#oldPwd', div), //原始密码
			   newPwd = $('#newPwd', div), //新密码
			   aginPwd = $('#aginPwd', div), //确认密码
			   pwdEdit = $('#pwdEdit', div), //显示修改密码Div
			   personOK = $('#personOK', div), //确认
			   personCancel = $('#personCancel', div); //取消
			   
		editTip[EV](CK, function() {
			pwdEdit[TG]();
		 });
		 personCancel[EV](CK, function() {
		 	close();
		 });
		 personOK[EV](CK, function() {
		 	UserRegExp(personPhone, personEmail, oldPwd, newPwd, aginPwd, pwdEdit, function(phone, email, pwd) {
		 			$(BY)[AJ]({
						param: { action: 'portal.userSetting', phone: phone, email: email, userPwd: pwd },
						success: function(data) {
							if(data) {
								$('#tip').text('');
								currentUser.UserInfo.phone = phone;
								currentUser.UserInfo.email = email;
								close();
							} else {
								$('#tip').text('设置失败');
							}
						}
		 			});
		 	});
		 });
	}
	
	//用户信息设置验证方法
	function UserRegExp(personPhone, personEmail, oldPwd, newPwd, aginPwd, pwdEdit, fn) {
		var tip = $('#tip'), flg = TE;
		tip.text('');
		var personPhoneVal= $[TM](personPhone.val());
		if (personPhoneVal == '') { tip.text('请输入手机号'); flg = FE;return;  }
		if (!$.regMobile(personPhoneVal))  { tip.text('请输入正确的手机号'); flg = FE; return; }
		
		var personEmailVal = $[TM](personEmail.val());
		if (personEmailVal == '') { tip.text('请输入邮箱'); flg =FE;return;  }
		if (!$.regEmail(personEmailVal))  { tip.text('请输入正确的邮箱'); flg = FE; return; }
		
		
		//验证密码
		if(pwdEdit.is(':' + VB)) {
			var oldPwdVal = $[TM](oldPwd.val());
			if (oldPwdVal == '') { tip.text('请输入原密码'); return;  }
			flg = FE;
			$(BY)[AJ]({
				param: { action: 'portal.checkPwd', userPwd: oldPwdVal },
				success: function(checked) {
					if (!checked) {
						 tip.text('原密码输入不正确'); return; 
					} else {
						var newPwdVal = $[TM](newPwd.val());
						if (newPwdVal == '') { tip.text('请输入新密码'); return; }
						if(newPwdVal[LN] < 5 ) { tip.text('新密码长度不能小于5'); return; }
						if(!$.regNorm5(newPwdVal)) { tip.text('新密码只能输入字母和数字'); return;  }
						var aginPwdVal = $[TM](aginPwd.val());
						if (aginPwdVal == '') { tip.text('请输入确认密码');return;  }
						if(aginPwdVal[LN] < 5 ) { tip.text('确认密码长度不能小于5'); return; }
						if(!$.regNorm5(aginPwdVal)) { tip.text('确认密码只能输入字母和数字'); return;  }
						if (newPwdVal != aginPwdVal) { tip.text('两次输入的密码不一致'); return;  }
						
						fn(personPhoneVal, personEmailVal, aginPwdVal);
					}
					
				}
			});
		}
		
		if (flg === FE || flg == UN) return; //模仿阻止冒泡功能
		
		fn(personPhoneVal, personEmailVal, '');
	}

	
	//菜单点击事件加载各个子模块
    function OnClick() {
        var t = this
        	   mainChildren = $('.mainChildren', $key),//带有子节点的菜单对象集合
        	   mainNode = $('.mainNode', $key), //不带子节点的菜单对象集合
        	   subNode = $('.subNode', $key);
        	   
        mainChildren[EV](CK, function() {
        	var $this = $(this);
        	 	   moduleKey = $this[AT]('id')[RP]('Item', '');
        	if(RegExpHomePageLoadComplate()) {
	        	if(moduleKey == 'sysManager' || RegExpAccount()) {
		        	mainChildren.not($this)[NT]().hide();
					mainChildren.not($this)[FD]('.arrows')[AT]('class','arrows sec_tipBack');
					var number = $this[NT]().is(':'+VB) == TE? '': '2';
		        	$this[NT]()[STG](500);
		        	$this[FD]('.arrows')[AT]('class', 'arrows sec_tipBack' + number);
		        	mainChildren[PT]()[RC]('in_liBack');
		        	$this[PT]()[AC]('in_liBack');
		        	SetMenuNodeClass(mainNode);
		        	subNode[RC]('sec_navColor');
	        	}
        	}
        });
        
        mainNode[EV](CK, function() {
        	var $this = $(this),
        	 	   moduleKey = $this[AT]('id')[RP]('Item', '');
	 	   	if(moduleKey =='homePage' || RegExpHomePageLoadComplate()) {
	 	   		if(moduleKey =='homePage' || RegExpAccount()) {
		        	SetMenuNodeClass(mainNode, $this);
		        	//修改带子节点的菜单样式
		        	mainChildren[NT]().hide();
		        	mainChildren[PT]()[RC]('in_liBack');
		        	mainChildren[FD]('.arrows')[AT]('class', 'arrows sec_tipBack');
		        	//修改子节点菜单样式
		        	subNode[RC]('sec_navColor');
					mediator.MenuReflectionMethod(moduleKey);
	 	   		}
	 	   	}
        }).eq(0)[TR](CK);
        
        function SetMenuNodeClass (menuNodes, This) {
        	menuNodes[PT]()[RC]('in_liBack');
        	if(This != null)
        		This[PT]()[AC]('in_liBack');
        }
        
        subNode[EV](CK, function() {
        	var $this = $(this),
        	 	   moduleKey = $this[AT]('id')[RP]('Item', '');
	 	   	if(RegExpHomePageLoadComplate()) {
	 	   		if(moduleKey == 'accountBinding' || moduleKey == 'approveSetting' ||
	 	   			moduleKey == 'authorityManager' || moduleKey == 'taskManager' ||
	 	   			moduleKey == 'userManager' || RegExpAccount()) {
		        	//修改子节点菜单样式
		        	subNode[RC]('sec_navColor');
		        	$this[AC]('sec_navColor');
		            mediator.MenuReflectionMethod(moduleKey);
	 	   		}
	 	   	}
         });

        //显示设置面板特效
        $('#userInfo', $key)[EV](CK, function() {
        	var panel = $('#userSet', $key);
        	panel[TG]()[FD]('li')[EV](CK, function() {
    			var $this = $(this),
    				   index = panel[FD]('li').index($this);
    		     switch (index) {
    		     	case 0: UserSet(); return;
    		     	case 1: UserHelp(); return;
    		     	case 2: UserExit(); return;
    		     }
    		});
    		return FE;
        });
        return t;
    }
    
    //读取菜单信息
    function LoadMenu(fn) {
    	$(BY)[AJ]({
			param: { action: 'portal.menu'},
			success: function(data) {
				DrawMenu(data);
				fn();
			}
    	});
    }
    //画菜单信息 data: 数据结构
    function DrawMenu(data) {
    	//0:code,1:name,2:主还是子集(mainNode\mainChildren),3: 是否有子集的箭头(arrows sec_tipBack),4:子集内容
    	var liTemplate = '<li class="menu_one"><a id="{0}Item" class="{2}"><span class="li_title li_{0}"></span><span class="{3}">{1}</span> </a>{4}</li>',
			   html = '';
		$[EH](data, function(i, dataRow) {
			if(dataRow.module[LN] > 0) {
				var subHtml = DrawSubMenu(dataRow);
				html += $[FO](liTemplate, dataRow.code, dataRow.caption, 'mainChildren', 'arrows sec_tipBack', subHtml);
			} else {
				html += $[FO](liTemplate, dataRow.code, dataRow.caption, 'mainNode', '', '');
			}
		});
		loadMenu.html(html);
    }
    
    function DrawSubMenu(data) {
    	//0:code, 1:name
    	var subTemplate = '<dd id="{0}Item" class="subNode">{1}</dd>',
    		   html = '<dl class="com_none">';
    	$[EH](data.module, function(i, dataRow) {
    		html += $[FO](subTemplate, dataRow.code, dataRow.caption);
    	});
    	return html + '</dl>';
    }
    
    //首页是否加载完成方法
    function RegExpHomePageLoadComplate () {
    	if(!portal.isClick) {
    		$[MB]({
				content: '请查看是否分配 首页展示 模块或首页展示模块还没加载完', 
				type: 2
			});
			return FE;
    	}
    	return TE;
    }
    
    //是否分配账号
    function RegExpAccount() {
    	if(currentUser.Accounts[LN] == 0 ) {
    		$[MB]({
				content: '请先到 帐号绑定 模块分配帐号', 
				type: 2
			});
    		return FE;
    	}
    	return TE;
    }

    function FactoryMethod() {
        return {
            NavigationBars: null,
            Base: null,
            CurrentUser: null,
            Enum: null,
            Mediator: null,
            Collect: null,
            Panel: [],
            isClick: FE, //是否可以点击其他模块(如果没设置首页展示模块或首页展示不加载完不让点击)
            Init: function (_navigationBars, _base, _currentUser, _twitterEnum, _mediator, _collect) {
                var t = this;
                t.NavigationBars = navigationBars = _navigationBars;
                t.Base = base = _base;
                t.CurrentUser = currentUser = _currentUser;
                t.Enum = twitterEnum = _twitterEnum;
                t.Mediator = mediator = _mediator;
                t.Collect = collect = _collect;
                messageBox = currentUser.MessageBox;
                warningBox = currentUser.WarningBox;
                portal = t;
                t.OnLoad();
                return t;
            },
            OnLoad: function () {
                var t = this;
               
                //应该异步读取html，目前没实现
                programDiv.load('modules/portal/portal.htm', function () {
                    $key = $('#' + key);
                    contentDiv = $('#contentBox', $key);
                    
                    //初始化导航条模块
                	navigationBars.Init(programDiv);
                    t.Mediator.SetContentDiv(contentDiv); //定义个模块读取html的主div对象
			
					logo = $('#logo', $key); //logo图像Div
          			userName = $('#userName', $key); //显示用户名称Div
					gotoTaskNotify = $('#gotoTaskNotify', $key); //进入任务通知Div
                    taskNotifyCount = $('#taskNotifyCount', $key); //任务通知未读数Span
                    gotoAlertInfo = $('#gotoAlertInfo', $key); //进入预警通知Div
                    alertInfoCount = $('#alertInfoCount', $key); //预警通知未读数Span
                    loadMenu = $('#loadMenu', $key); //加载菜单ul对象
                    timer = null;
					Init();
                });
                return t;
            },
            //递减消息通知数
		 	DecreaseMessageCount: function() {
				messageBox.DecreaseCount(taskNotifyCount);
			},
			//加1消息通知数
		 	AddMessageCount: function() {
				messageBox.AddCount(taskNotifyCount);
			},
			WarningDecreaseCount: function(count) {
				warningBox.DecreaseCount(alertInfoCount, count);
			},
			//隐藏面板方法
			HiddenTo: function() {
				$.hiddenTo(this.Panel);
			}
    	}
	}
    return new FactoryMethod();
});