//用户维护
define(['portal'], function (context) {
    //定义变量
    var key = 'userManager',
          $key, //当前的作用域
          contentDiv,
          addBtn,
          addAlertDiv,
          editAlertDiv,
          UserTable,
          UserInfo;
    
    //初始化方法，写自己模块的业务
    function Init() {
    	//addBtnBind(addBtn,addAlertDiv,UserInfo);
    	NavigationInit();
    	TableDataBind();
    }

     //导航条初始化方法,
	function NavigationInit() {
		navigationBars.Empty()
			.Add(key, 'NavToMP', '系统管理')
			.Add(key, 'NavToMP_yhwh', '用户维护')[oL]();
	} 
    
    //展示用户列表
    function TableDataBind(){
    	   var pager = $('#allPager', $key);
    	   UserTable.add(pager).html('');
    	   UserTable[TB]({
    	    	width: 1000,
             	trHeight: 24, 	  //表格默认高
             	pager: pager,
             	pageSize: 15, 	  //显示条数
             	isPager: TE, 	  //是否分页
             	isCheckBox: FE,  //是否有多选按钮
             	id:'_m1',
             	rows: 'rows',
    		 	width:1000,
    	  		param:{action:'sysManager.GetBaseUsers'},
    	   		columns:[{width:'5%',caption:'序号',field:'id',type:'number'},
    	 			 	{width:'8%',caption:'姓名',field:'nickname'},
    	 			 	{width:'18%',caption:'E-Mail',field:'mail'},
    	 			 	{width:'12%',caption:'手机',field:'tele'},
    	 			 	{width:'16%',caption:'登录帐号',field:'username'},
    	 			 	{width:'9%',caption:'角色',field:'rolename'},
    	 			 	{width:'5%',caption:'审核',field:'ischeck'},
    	 			 	{width:'8%',caption:'审核人',field:'check'},
    	 				{width:'',caption: '操作', field: '', type: 'html', template: ''}],
    	 	
    	 	onComplete: function (This, refresh, data) { 

    	 		var add_power = cuser.Templates['yhwh_insert_1'];		//取得添加权限
    			var delete_power = cuser.Templates['yhwh_delete_1'];	//取得删除权限
				var upadte_power = cuser.Templates['yhwh_update_1'];	//取得修改权限
				var reset_power = cuser.Templates['yhwh_reset_1'];		//取得重置权限
				var shouq_power = cuser.Templates['shsz_empower_1'];	//取得授权权限
				var shenh_power = cuser.Templates['shsz_examine_1'];	//取得审核权限

				This[FD]('.t2')[EH](function (){
					var _email=$[TM]($(this)[PT]()[FD]('.t2')[TX]());
					$(this).html(_email.substring(0,18));
				});
				This[FD]('.t4')[EH](function (){
					var _acname=$[TM]($(this)[PT]()[FD]('.t4')[TX]());
					$(this).html(_acname.substring(0,16));
				});
				This[FD]('.t5')[EH](function (){
					var _role=$[TM]($(this)[PT]()[FD]('.t5')[TX]());
					$(this).html(_role.substring(0,5));
				});
				
				var editBtn = $(".event_edit",This), delBtn = $(".event_del",This);
				//判断添加权限
				if( add_power != UN && add_power != ""){
					addBtn[EV](CK,function(){
						$(this)[AJ]({
					    	param:{action:'sysManager.InsertBaseUser',method:2},
					    	success:function(data){
					    		// -1：到达添加次数，限制提示，0：插入失败，数据库错误提示，1：添加成功
								if(data == -1){
									$[MB]({ content: '添加失败,添加已到达上限!', type: 1});
								}else{
									 addBtnBind(0,'添加用户');//添加
								}
					    	}
						});
					});
				}
				This[FD]('.t8')[EH](function (){
					
					var index=This[FD]('.t8')[ID]($(this));
				    var id = data.rows[index].id;	//取得每行id
					
					//判断修改权限
					if( upadte_power != UN && upadte_power != ""){
						var upTemp = $[FO](upadte_power,id);
				        $(this)[AP](upTemp);	
				         	
				        //编辑
						$('#userManager_edit'+id,$key)[EV](CK,function(){
			    			addBtnBind(id,'编辑用户');
						});
					}
					
					//判断删除权限
					if( delete_power != UN && delete_power != ""){	
						var delTemp = $[FO](delete_power,id);
				        $(this)[AP](delTemp);	
				         	
				       //删除
						$('#userManager_del'+id,$key)[EV](CK,function(){
			    			$[MB]({
								content: '您确定要删除吗？',
								type: 4,
								onAffirm: function (state) { 
									if(state == true){
										This[AJ]({
											param: {action: 'sysManager.DeleteBaseUser', id: id},
											success: function(data){
												TableDataBind();//重新加载列表
											}
										});
									}
								}
							});
						});//删除结束
					}
					
					//判断重置权限
					if( reset_power != UN && reset_power != ""){	
						    var email=$[TM]($(this)[PT]()[FD]('.t2')[TX]());//取得每行email
					        var name=$[TM]($(this)[PT]()[FD]('.t1')[TX]());//取得每行名字
					        var temp = $[FO](reset_power,id);
					        $(this)[AP](temp);	
					         	
					        //重置
							$('#userManager_set'+id,$key)[EV](CK,function(){
				    			$[MB]({
									content: '您确定要重置密码吗？',
									type: 4,
									onAffirm: function (state) { 
										if(state == true){
											This[AJ]({
												param: {action: 'sysManager.ResetBaseUser',id: id,name:name,email:email},
												success: function(data){
													TableDataBind();//重新加载列表
													if(data != -1){
														$[MB]({ content: data, type: 1});
													}
												}
											});
										}
									}
								});
							});	
					}
					
					//判断授权权限
					if( shouq_power != UN && shouq_power != ""){
						 var _name=$[TM]($(this)[PT]()[FD]('.t1')[TX]());
									
					     var delTemp = $[FO](shouq_power,id);
					     $(this)[AP](delTemp);	
					        
					     $('#approveSetting_editsq'+id,$key)[EV](CK,function(){
					        	EditBtnBindsq(id,"账号授权",_name);
					     });
					}
					
					//判断审核权限
					if( shenh_power != UN && shenh_power != ""){
						 var temp = $[FO](shenh_power,id);
					     $(this)[AP](temp);	
					        					        
					     $('#approveSetting_editsh'+id,$key)[EV](CK,function(){
					       	EditBtnBindsh(id,"账号审核");
					     });
					}
				});
				
    	 	}
    	 });
    	
    }
    
    //添加编辑操作
    function addBtnBind(id,title){
    		$[WW]({
    			css: { "width": "500px", "height": "auto" },
    			title: title,
    			id: 'userManager_add',
    			content:addAlertDiv,
    			event:'no',
    			onLoad:function(div,close){
    				
    				var nicknameinput = $("#userManager_nickname",div),mailinput = $("#userManager_mail",div),teleinput = $("#userManager_tele",div),
    					usernameinput = $("#userManager_username",div),passwordinput = $("#userManager_password",div),rolesel=$("#userManager_role",div),
    					ck_name="",test = 0;

    				if(id != 0){
    					$("#csmm_name",div).hide();
    					$(div)[AJ]({
    							param:{action:'sysManager.UpdateBaseUser',id:id,method: 2},
    							success:function(data){
    								
									usernameinput.val("")[AT]("readonly","readonly");
    								nicknameinput.val(data.nickname);
			    					mailinput.val(data.mail);
			    					teleinput.val(data.tele);
			    					usernameinput.val(data.username);
			    					passwordinput.val(data.token);
			    					test = data.id;
			    					ck_name = usernameinput.val();
			    					RoleSelDataBind(rolesel,data.customid);
    							}
    					});
    				}else{
    					usernameinput.removeAttr("readonly");
    					nicknameinput.val("");
    					mailinput.val("");
    					teleinput.val("");
    					usernameinput.val("");
    					passwordinput.val("");
    					RoleSelDataBind(rolesel,id);
    				}
    	
    				$(".in_MsetCok",div)[EV](CK,function(){
    					
    					$("#name_name",div).html('');
    					$("#emal_name",div).html('');
    					$("#tel_name",div).html('');
    					$("#dlzh_name",div).html('');
    					$("#pass_name",div).html('');
    					$("#role_name",div).html('');
    					var email = $.trim(mailinput.val()),
    						nickName = $.trim(nicknameinput.val()),tel = $.trim(teleinput.val()),
    						rid = $.trim(rolesel.val()),password = $.trim(passwordinput.val()),
    						userName = $.trim(usernameinput.val());
    					if(nickName == ''){
    						$("#name_name",div).show();
	    					$("#name_name",div)[AP]("* 请输入姓名");
    					}else if(nickName != ''){
    						var reg = /^[a-zA-Z0-9\u4e00-\u9fa5]+$/;
    						if(!$.regNorm3(nickName)){
    							$("#name_name",div).show();
	    						$("#name_name",div)[AP]("* 姓名不能包含无效字符");
    						}else{
    							if(email == ''){
    								$("#name_name",div).hide();
		    						$("#emal_name",div).show();
			    					$("#emal_name",div)[AP]("* 请输入E-mail");
    							}else if(email != ''){
    								if(!$.regEmail(email)){
		    							$("#emal_name",div).show();
			    						$("#emal_name",div)[AP]("* E-mail格式不正确");
		    						}else{
		    							if(!$.regLength(email,30)){
			    							$("#emal_name",div).show();
			    							$("#emal_name",div)[AP]("* E-mail长度最大为30");
			    						}else if(tel == ''){
				    						$("#emal_name",div).hide();
				    						$("#tel_name",div).show();
					    					$("#tel_name",div)[AP]("* 请输入11有效手机号码");
				    					}else if(tel != ''){
				    						if(!$.regMobile(tel)){
				    							$("#tel_name",div).show();
			    								$("#tel_name",div)[AP]("* 请输入11位有效手机号码");
				    						}else if(userName == ''){
					    						$("#tel_name",div).hide();
					    						$("#dlzh_name",div).show();
						    					$("#dlzh_name",div)[AP]("* 请输入登录帐号");
					    					}else if(userName != ''){
					    						if(!$.regNorm2(userName)){
					    							$("#dlzh_name",div).show();
						    						$("#dlzh_name",div)[AP]("* 登录帐号不能包含中文和特殊字符");
					    						}else{
					    							div[AJ]({
														param: {action: 'sysManager.UpdateBaseUser',username: userName,method:3},
															success: function(data){
																if(data == true){
																	if(test != 0){
																		if(ck_name != userName){
																			$("#dlzh_name",div).show();
								    										$("#dlzh_name",div)[AP]("* 登录帐号已存在，请重新输入");
																		}else {
												    						$("#pass_name",div).hide();
													    						div[AJ]({
														    							param:{action:'sysManager.UpdateBaseUser',gid:test,email:email,nickName:nickName,tel:tel,userName:userName,rid:rid,method: 1},
														    							success:function(data){
														    								TableDataBind();
														    								close();
														    							}
														    				});
														    			}
																	}else{
																		$("#dlzh_name",div).show();
								    									$("#dlzh_name",div)[AP]("* 登录帐号已存在，请重新输入");
																	}
																}else if(password == ''){
										    						$("#dlzh_name",div).hide();
										    						$("#pass_name",div).show();
											    					$("#pass_name",div)[AP]("* 密码不能为空");
										    					}else if(password != ''){
												    				var reg = /^[A-Za-z0-9]+$/;
												    				if(!reg.test(password)){
												    					$("#pass_name",div).show();
													    				$("#pass_name",div)[AP]("* 密码仅支持数字和字母");
												    				}else if(password.length < 6){
												    					$("#pass_name",div).show();
													    				$("#pass_name",div)[AP]("* 密码最小长度为6位");
												    				}else if(password.length >20){
												    						$("#pass_name",div).show();
													    					$("#pass_name",div)[AP]("* 密码最大长度为20位");
												    				}else if(rid == ''){
											    						$("#pass_name",div).hide();
											    						$("#role_name",div).show();
												    					$("#role_name",div)[AP]("* 角色不能为空，清先添加角色");
											    					}else{
											    						$("#role_name",div).hide();
											    						if(test != 0){
											    							div[AJ]({
														    						param:{action:'sysManager.UpdateBaseUser',gid:test,email:email,nickName:nickName,tel:tel,userName:userName,rid:rid,method: 1},
														    						success:function(data){
														    							TableDataBind();
														    							close();
														    						}
														    				});
											    						}else{
												    						div[AJ]({
													    							param:{action:'sysManager.InsertBaseUser',email:email,nickName:nickName,tel:tel,rid:rid,password:password,userName:userName,method:1},
													    							success:function(data){
													    								TableDataBind();
													    								close();
													    							}
													    					});
												    					}
											    					}
												    			}
															}
													});
					    						}
					    					}
				    					}
			    					}
    							}
    						}
    					}
    				});
    				//取消
    				$(".in_MsetCno",div)[EV](CK,function(){
    					close();
    				});
    			}
    		});
    }
    
    function RoleSelDataBind(sel,selectid){//匹配角色
    	sel.html("");
    	sel[AJ]({
    		param:{action:'sysManager.GetCustomRoles'},
    		success:function(data){
    			var RoleOption ='<option value="{0}" {2} >{1}</option>';
    			$.each(data,function(i,item){
    				if(item.id == selectid){
    					sel.append($[FO](RoleOption,item.id,item.name,'selected="selected"'));
    				}else{
    					sel.append($[FO](RoleOption,item.id,item.name,''));
    				}
    			});
    		}
    	});
    }
   
    //用户授权帐号
    function EditBtnBindsq(id,title,_name){
    		$[WW]({
    			css: { "width": "500px", "height": "auto" },
    			title: title,
    			content:shouquanDiv,
    			onLoad:function(div,close){
    				var saveBtn = $(".in_MsetCok",div),cancelBtn = $(".in_MsetCno",div),
    				approve_usersel = $("#approve_usersel",div),check_ui = $("#check_ui",div),
    				check_account = $("#check_account",div),test = 0,flag="";
    				$(div)[AJ]({
    					param:{action:'sysManager.UpdateApproves',id:id,method: 2},
    					success:function(data){
			    			approve_usersel.val(_name);
							ApproveData_ui(check_ui,data);
    					}
    				});

    				saveBtn[EV](CK,function(){//保存
    					check_account.html('');
     					var _ui="",platform="",i=0,j=0;	
						$("#check_ui input")[EH](function(){
						    if($(this)[0].checked){
						    	i++;
						    	if(i==1){
						    		_ui = $(this).attr("value");
						    	}else{
						    		_ui +=","+$(this).attr("value");
						    	}
						    	platform = $(this).attr("platform");
						    	if(platform == "Sina"){
						    		j++;
						    	}
							}
						});
						if(i == 0){
							check_account.show();
							check_account[AP]("* 请选择授权帐号!");
						}else if(j == 0){
							check_account.show();
							check_account[AP]("* 至少分配一个新浪帐号,若无新浪帐号请先授权!");
						}else{
							check_account.hide();
	     					div[AJ]({
						    	param:{action:'sysManager.UpdateApproves',id:id,_ui:_ui,method: 1},
						    	success:function(data){
						    		TableDataBind();
						    		close();
						    	}
						    });
					    }
    				});
    				cancelBtn[EV](CK,function(){//取消
    					close();
    				});
    			}
    		});
    }
	
	 //用户审核
    function EditBtnBindsh(id,title){
    		$[WW]({
    			css: { "width": "500px", "height": "auto" },
    			title: title,
    			content:shenheDIV,
    			onLoad:function(div,close){
    				var saveBtn = $(".in_MsetCok",div),cancelBtn = $(".in_MsetCno",div),
    				check_shr = $("#check_shr",div),approve_shr = $("#approve_shr",div),
    				_isnot = $("#isnot",div),test = 0,flag="";
	
    				_isnot[EV](CK,function(){
			    		flag= _isnot[AT]("class");							//获取样式
			    		if(flag == 'push_type com_fLeft check_max push_type1'){	//点击对样式进行切换
			    			_isnot[RC]("push_type1"); 				//删除样式
			          		_isnot[AC]("push_type2");				//添加样式
			          		check_shr.hide();
			    		}else{
			    			_isnot[RC]("push_type2"); 				//删除样式
			          		_isnot[AC]("push_type1");				//添加样式
			          		check_shr.show();
			    		}
			        });

					$(div)[AJ]({
    					param:{action:'sysManager.UpdateTheUser',id:id,method: 2},
    					success:function(data){
								if(data.checkid != 0){
			    					_isnot[RC]("push_type2"); 				//删除样式
			          				_isnot[AC]("push_type1");				//添加样式
			    					check_shr.show();
			    				}
								ApproveData_shr(approve_shr,data.checkid);
    					}
    				});
					
    				saveBtn[EV](CK,function(){//保存
					
     					var _shr = "";
					    if(_isnot[AT]("class") == "push_type com_fLeft check_max push_type1"){
					    	_shr = approve_shr.val();
					    }else{
					    	_shr = 0;
					    }
     					div[AJ]({
					    	param:{action:'sysManager.UpdateTheUser',id:id,_shr:_shr,method: 1},
					    	success:function(data){
					    		if(data == true){
					    			TableDataBind();
					    			close();
					    		}
					    	}
					    });
    				});
    				cancelBtn[EV](CK,function(){//取消
    					close();
    				});
    			}
    		});
    }
	
    function ApproveData_shr(sel,selectid){//匹配审核人
    	sel.html("");
    	sel[AJ]({
    		param:{action:'sysManager.GetTheUser'},
    		success:function(data){
    			var RoleOption ='<option value="{0}" {2} >{1}</option>';
    			$.each(data,function(i,item){
    				if(item.id == selectid){
    					sel[AP]($[FO](RoleOption,item.id,item.useName,'selected="selected"'));
    				}else{
    					sel[AP]($[FO](RoleOption,item.id,item.useName,''));
    				}
    			});
    		}
    	});
    }
    
    function ApproveData_ui(sel,uids){//匹配授权帐号
    	sel.html("");
    	sel[AJ]({
    		param:{action:'sysManager.GetAtApproves'},
    		success:function(data){//alert($.toJson(data));
    			var html_1 ='<li><input type="checkbox" id="check_num_{0}" value="{1}" platform="{4}"/><label for={1} class="check_Lcount mynum_type1" title="{3}">{2}</label></li>';
    			var html_2 ='<li><input type="checkbox" id="check_num_{0}" value="{1}" platform="{4}"/><label for={1} class="check_Lcount mynum_type2" title="{3}">{2}</label></li>';
    			var html_5 ='<li><input type="checkbox" id="check_num_{0}" value="{1}" platform="{4}"/><label for={1} class="check_Lcount mynum_type5" title="{3}">{2}</label></li>';

    			$.each(data,function(i,item){
    				var _name = item.name.substring(0,6)
    				if(item.platform == 'Sina'){
    					sel[AP]($[FO](html_1,item.id,item.uid,_name,item.name,item.platform));
    				}else if(item.platform == 'Tencent'){
    					sel[AP]($[FO](html_2,item.id,item.uid,_name,item.name,item.platform));
    				}else{
    					sel[AP]($[FO](html_5,item.id,item.uid,_name,item.name,item.platform));
    				}
    				for (var i = 0; i < uids.length; i++){
    					if(item.uid == uids[i].uid){
    						$("#check_num_"+item.id).attr("checked",true);
    					}
    				}
    			});
    		}
    	});
    }
   
    return {
    	IsLoad: FE,
        OnLoad: function (_ContentDiv) {
            var t = this;
            contentDiv = _ContentDiv;
            $('<div />').load('modules/sysManager/sysManager.userManager.htm', function () {
            	base = context.Base;
            	cuser=context.CurrentUser;
            	contentDiv[AP](base.FormatPmi($(this).html()));
            	navigationBars = context.NavigationBars;
            	
            	t.IsLoad = TE;
                $key = $('#' + key);
                addBtn = $("#userManager_addBtn");//<h4>账号授权</h4><div class="div_style">
                addAlertDiv = '<div class="giveMe_do sys_addUsers"><div class="div_style"><label class="task_name">姓名：</label><input id="userManager_nickname" type="text" maxlength="4" class="task_count task_Cwdith1"/></div><div style="margin-left: 100px;float: left; color: red; font-size: 12px; display: block;"><span class="com_none" id="name_name"></span></div><div class="com_clear"></div><div class="div_style"><label class="task_name">E-mail：</label><input id="userManager_mail" type="text" maxlength="30" class="task_count task_Cwdith1"/></div><div style="margin-left: 100px;float: left; color: red; font-size: 12px; display: block;"><span class="com_none" id="emal_name"></span></div><div class="com_clear"></div><div class="div_style"><label class="task_name">手机：</label><input id="userManager_tele" type="text" maxlength="11" class="task_count task_Cwdith1"/></div><div style="margin-left: 100px;float: left; color: red; font-size: 12px; display: block;"><span class="com_none" id="tel_name"></span></div><div class="com_clear"></div><div class="div_style"><label class="task_name">登录账号：</label><input id="userManager_username" type="text" readonly="" maxlength="30" class="task_count task_Cwdith1"/></div><div style="margin-left: 100px;float: left; color: red; font-size: 12px; display: block;"><span class="com_none" id="dlzh_name"></span></div><div class="com_clear"></div><div class="div_style" id="csmm_name"><label class="task_name">初始密码：</label><input id="userManager_password" type="password" maxlength="20" class="task_count task_Cwdith1"/></div><div style="margin-left: 100px;float: left; color: red; font-size: 12px; display: block;"><span class="com_none" id="pass_name"></span></div><div class="com_clear"></div><div class="div_style"><label class="task_name">角色：</label><select id="userManager_role" class="task_count task_Cwdith1"></select></div><div style="margin-left: 100px;float: left; color: red; font-size: 12px; display: block;"><span class="com_none" id="role_name"></span></div><div class="com_clear"></div><div class="push_ok"><div class="in_MsetCno">取&nbsp;&nbsp;消</div><div class="in_MsetCok">保&nbsp;&nbsp;存</div></div><div class="com_clear"></div></div>';
                shouquanDiv = '<div class="giveMe_do add_check"><div class="box_count" ><label class="com_fLeft">用&nbsp;&nbsp;户：</label> <input name="T_place" readonly="readonly" class="task_count task_Cwdith1 pt_select" id="approve_usersel" /></div><div  style="margin-left: 50px;margin-top: 10px; float: left; color: red; font-size: 12px; display: block;"><span class="com_none" id="check_account"></span></div> <div class="com_clear"></div><div class="">请为用户分配可使用的微博帐号，至少分配一个新浪帐号。</div> <div class="com_clear"></div><div><ul class="check_list" id="check_ui"></ul></div><div class="com_clear"></div><div class="push_ok"><div class="in_MsetCno">取&nbsp;&nbsp;消</div><div class="in_MsetCok">确&nbsp;&nbsp;定</div></div><div class="com_clear"></div></div>';
                shenheDIV = '<div class="giveMe_do add_check"><div class="box_count" ><h4 style="margin-top:10px;">审核管理</h4><div class="div_style"><label class="com_fLeft">是否审核：</label> <span class="push_type com_fLeft check_max push_type2" id="isnot"></span></div><div class="com_clear"></div><div class="div_style com_none" id="check_shr"><label class="com_fLeft">审核人：</label><select name="T_place" class="task_count task_Cwdith1 pt_select" id="approve_shr"></select></div></div><div class="com_clear"></div><div class="push_ok"><div class="in_MsetCno">取&nbsp;&nbsp;消</div><div class="in_MsetCok">确&nbsp;&nbsp;定</div></div><div class="com_clear"></div></div>';
                UserTable = $("#userManager_table",$key);
                Init();
            });
            return t;
        },
        //初始化方法
        NavToMP: function () {
            Init();
        },
        //显示模块方法
        Show:function() {
        	$key.show();
        	Init();
        }
    };
});