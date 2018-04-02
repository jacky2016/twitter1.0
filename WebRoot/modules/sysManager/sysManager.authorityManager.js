//角色权限维护
define(['portal'], function (context) {
    //定义变量
    var key = 'authorityManager',
          $key, //当前的作用域
          contentDiv,
          RolesUL,
          RolesLi,
          PermissionTab,
          PermissionTr,
          Permissions,
          ModuleList,
          SaveBtn,
          AddBtn,
          AlertDiv,
          ids=[],
          RoleInput;


    //初始化方法，写自己模块的业务
    function Init() {
    	NavigationInit();
    	RolesDataBind();
    	//SaveBtnEven();
    }
     //导航条初始化方法,
	function NavigationInit () {
		navigationBars.Empty()
			.Add(key, 'NavToMP', '系统管理')
			.Add(key, 'NavToMP_yhwh', '角色权限维护')[oL]();
	}
    
    //角色绑定
    function RolesDataBind(){
    	RoleUL.html("");
    	RoleUL[AJ]({
    		param:{action:'sysManager.GetCustomRoles'},
    		success:function(data){
    		    			
    			if(data.length==0){
    				$(".send_selContent",$key)[AC]("com_none");
    				PermissionTab.html("");
    			}else{
    				$(".send_selContent",$key)[RC]("com_none");
    				RolesLi = '<li class="li_task" gid="{0}" title="{2}"><span class="tasks_style sTask_count">{1}</span><div class="sysTtask_do"><span class="tasks_style"></span>{pmi:jsqxwh_update_1}{pmi:jsqxwh_delete_1}</div></li>';
	    			var _role = base.FormatPmi(RolesLi);
	    			$.each(data,function(i,item){
	    				var _rolename = item.name.substring(0,6);
	    				RoleUL.append($[FO](_role,item.id,_rolename,item.description));
	    			});
	    			
	    			//加载角色对应权限信息
	    			var lis = RoleUL[FD]("li");
	    			lis.first()[AC]("sTask_back");
					lis[EV](CK,function(){
					    lis[RC]("sTask_back");
					    $(this)[AC]("sTask_back");
					    var roleid = RoleUL[FD](".sTask_back")[AT]("gid");
					    PermissionGroupDataBind(roleid);//加载基础权限
					    PermissionDataBind(roleid);//加载角色权限信息
					}).eq(0)[TR](CK);
	    			
	    			SaveBtn[EV](CK,function(){
	    				SaveBtnEven();//角色设置
	    			});
    			}

    			var insert_power = cuser.Templates['jsqxwh_insert_1'];		//取得添加权限
				var update_power = cuser.Templates['jsqxwh_update_1'];		//取得修改权限
				var delete_power = cuser.Templates['jsqxwh_delete_1'];		//取得删除权限

				if(insert_power != null && insert_power !=""){
					//添加角色
	    			AddBtn[EV](CK,function(){
	    				var RoleInfo = {};RoleInfo.id=0,RoleInfo.name="";
	    				AddBtnEven(RoleInfo,"添加角色");
	    			});
				}
				if(update_power != null && update_power !=""){
					//编辑角色
	    			$(".sysTask_edit",RoleUL)[EV](CK,function(){
	    				var RoleInfo = {},btn = $(this);
	    				RoleInfo.id = parseInt(btn.attr("gid"));
	    				RoleInfo.name = btn.attr("title");
	    				AddBtnEven(RoleInfo,"编辑角色");
	    			});
				}
				if(delete_power != null && delete_power !=""){
					//删除角色
	    			$(".sysTask_del",RoleUL)[EV](CK,function(){
	    				var gid = $(this).attr("gid");
	    				$(this)[AJ]({
						    param:{action:'sysManager.DeleteRole',method:2,gid:gid},
						    success:function(data){
								if(data){
									$[MB]({ content: '删除失败,该角色正被使用', type: 2});
								}else{
									$[MB]({
											content: '您确定要删除吗？',
											type: 4,
											onAffirm: function (state) { 
												if(state == true){
													RoleUL[AJ]({
									    				param:{'action':'sysManager.DeleteRole',gid:gid,method:1},
									    				success:function(data){
									    					RolesDataBind();
									    				}
									    			});
												}
											}
									});
								}
						    }
						});
	    				
	    			});
				}
    		}
    	});
    }

    //添加_编辑角色
    function AddBtnEven(RoleInfo,title){
    		$[WW]({
    			css: { "width": "360px", "height": "auto" },
    			title: title,
    			content: AlertDiv,
    			onLoad:function(div,close){
    				var saveBtn = $(".in_MsetCok",div),cancelBtn = $(".in_MsetCno",div);
    				RoleInput = $(div).find("input");
    				RoleInput.val(RoleInfo.name);

    				saveBtn[EV](CK,function(){//保存
    					
    					$("#role_name",div).html('');
     					var _name = RoleInput.val();
	    				if(_name == ''){
	    					$("#role_name",div).show();
	    					$("#role_name",div)[AP]("* 请输入角色名称");
	    				}else if(_name != ''){
	    					
    						if(!$.regNorm3(_name)){
    							$("#role_name",div).show();
	    						$("#role_name",div)[AP]("* 角色名称不能包含无效字符");
    						}else{
		    					div[AJ]({
										param: {action: 'sysManager.InsertRole',rolename: _name,mthod:2},
											success: function(data){
											if(data == true){
												if(RoleInfo.id != 0){
													if(RoleInfo.name != _name){
														$("#role_name",div).show();
														$("#role_name",div)[AP]("* 角色已存在，请重新输入");
													}else{
														$("#role_name",div).hide();
														div[AJ]({
							    							param:{action:'sysManager.UpdateRole',rolename:_name,gid:RoleInfo.id},
							    							success:function(data){
							    								RolesDataBind();
							    								close();
							    							}
							    						});
													}
												}else{
													$("#role_name",div).show();
													$("#role_name",div)[AP]("* 角色已存在，请重新输入");
												}
											}else{
												$("#role_name",div).hide();
			    								if(RoleInfo.id != 0){
			    									div[AJ]({
							    							param:{action:'sysManager.UpdateRole',rolename:_name,gid:RoleInfo.id},
							    							success:function(data){
							    								RolesDataBind();
							    								close();
							    							}
							    					});
			    								}else{
				    								div[AJ]({
							    							param:{action:'sysManager.InsertRole',rolename:_name,mthod:1},
							    							success:function(data){
							    								RolesDataBind();
							    								close();
							    							}
							    					});
						    					}
											}
										}
									});
							}
	    				}
    					
    				});
    				cancelBtn[EV](CK,function(){//取消
    					close();
    				});
    			}
    		});
    }
     
    
    //加载基础权限
    function PermissionGroupDataBind(roleid){
    	PermissionTab.html("");
    	PermissionTab[AJ]({
    		param:{action:'sysManager.GetBasePermissionGroups',roleid:roleid},
    		success:function(data){//alert($.toJson(data));
    			PermissionTab.append('<thead><th width="30px"><input id="author_allcheck" type="checkbox" name="sys_author1" /></th><th width="120px">一级菜单</th><th width="120px">二级菜单</th><th width="280px">功能权限</th></tr></thead>');
    			PermissionTab.append('<tbody></tbody>');
    			$.each(data,function(i,item){
    				if(item.id != 0){
	    				if(i==0){
	    					PermissionTab[FD]("tbody").append($[FO](PermissionTr,item.name,item.code,item.id,'checked="checked"','disabled="disabled"'));
	    				}else{
	    					PermissionTab[FD]("tbody").append($[FO](PermissionTr,item.name,item.code,item.id,''));
	    				}
	    			}
    				
    				ids.push(item.id);
    				var tds = PermissionTab[FD]("tbody tr").last()[FD]("td");
    				if(item.grouplist.length > 0){
    					
	    				$.each(item.grouplist,function(y,yitem){
	    					if(yitem.id != 0){
	    						$(tds[0])[AT]('rowspan',item.grouplist.length);
    							$(tds[1])[AT]('rowspan',item.grouplist.length);
		    					if(y == 0){
		    						$(tds[2]).append($[FO](Permissions2,yitem.code,yitem.name,yitem.id,yitem.id+"_"+y));
		    					}else{
		    						PermissionTab[FD]("tbody").append($[FO](ModuleList,yitem.code,yitem.name,yitem.id,i,y,yitem.id+"_"+y));
		    					}
		    					$("#sys_mode_"+item.id+"_"+y,$key)[EV](CK,function(){
									var flag = $(this)[0].checked;
									BindCheckBoxEvenTwo(item.id,flag);//绑定多选事件 二级
								});
		    					
		    				}
		    				$.each(yitem.uicodelist,function(z,zitem){
		    					if(y == 0){
			    					$(tds[3]).append($[FO](Permissions,zitem.ecode,zitem.desc,item.id,zitem.modelCode,item.id+"_"+y+"_"+z));
			    				}else{
		    						$("#t_td_"+i+"_"+y).append($[FO](Permissions,zitem.ecode,zitem.desc,item.id,zitem.modelCode,item.id+"_"+y+"_"+z));
			    				}
			    				$("#sys_ui_"+item.id+"_"+y+"_"+z,$key)[EV](CK,function(){
									var flag = $(this)[0].checked;
									BindCheckBoxEvenThre(item.id,flag);//绑定多选事件 功能
								});
		    				});
		    			});
	    			}
	    			
					$("#sys_author_"+item.id,$key)[EV](CK,function(){
						var flag = $(this)[0].checked;
						BindCheckBoxEven(item.id,flag);//绑定多选事件 单行全选
					});
					$("#sys_menu_"+item.id,$key)[EV](CK,function(){
						var flag = $(this)[0].checked;
						BindCheckBoxEvenOne(item.id,flag);//绑定多选事件 一级
					})
    			});
    			//全选或者取消
    			$("#author_allcheck",$key)[EV](CK,function(){
    				var cbs = PermissionTab.find("input"),
    				flag = $(this).get(0).checked;
    				//alert($("#sys_menu_1").is(':checked'));
    				$.each(cbs,function(){
    					$(this).get(0).checked=flag;
    				});
    				$("#sys_menu_1")[0].checked = true;
    				
    			});
    		}
    	});
    }


    //加载当前角色权限信息
    function PermissionDataBind(roleid){
    	//加载前先全部取消
    	$.each(PermissionTab[FD]("input"),function(i,item){
    		$(this).get(0).checked = false;
    		$("#sys_menu_1")[0].checked = true;
    	});

    	PermissionTab[AJ]({
    		param:{action:'sysManager.GetCustomPermissionInRole',roleid:roleid},
    		success:function(data){//alert($.toJson(data));
    			setTimeout(function(){
	    			var _mcode = $("[name=custom_one]:checkbox",PermissionTab);
	    			$.each(data,function(i,item){
	    				$.each(_mcode,function(y,yitem){
		    				if(item.mcode == $(this).val()){
		    						var v1 =$(this)[0];
		    						v1.checked = true;
		    				}else if(item.code == $(this).val()){
		    						var v2 =$(this)[0];
		    						v2.checked = true;
		    				}else if(item.uicode == $(this).val()){
		    						var v3 =$(this)[0];
		    						v3.checked = true;
		    				}
	    				});
	    			});
    			}, 100);
    		}
    	});
    }
   
    //绑定checkBox事件 单行全选/取消
    function BindCheckBoxEven(idr,flag){
    	$(".row_author_"+idr+" input").each(function(){	
	    	$(".row_men_"+idr+" input").each(function(i){
	   			$(this)[0].checked = flag;
	   			if($(".row_mod_"+idr+" input").length==0){
		    		$(".row_mod_"+idr).next().find("input").each(function(){
					    $(this)[0].checked = flag;
					   });
		    	}else{
			    	$(".row_mod_"+idr+" input").each(function(){
				    	$(this)[0].checked = flag;
				    	var _this = $(this).parent().next();
				   
				    	_this.find("input").each(function(){
						   	$(this)[0].checked = flag;
						});
				    });
				}
	    	});
	    });
    }
    
    //一级菜单操作
    function BindCheckBoxEvenOne(idr,flag){
		$(".row_men_"+idr+" input").each(function(){
	   		$(this)[0].checked = flag;
	   		if($(".row_mod_"+idr+" input").length != 0){
				$(".row_mod_"+idr+" input").each(function(){
				    $(this)[0].checked = flag;
				    if(flag == false){
				    	var _this = $(this).parent().next();
				    	_this.find("input").each(function(){//取消功能权限
						   	$(this)[0].checked = flag;
						});
						$(".row_author_"+idr+" input").each(function(){//取消单行全选
							$(this)[0].checked = flag;
						});
				    }
				 });
		    }else{
		    	if(flag == false){
				    $(".row_mod_"+idr).next().find("input").each(function(){//取消功能权限
						$(this)[0].checked = flag;
					});
					$(".row_author_"+idr+" input").each(function(){//取消单行全选
						$(this)[0].checked = flag;
					});
				 }
		    }
	    });
    }
    
    //二级菜单操作
    function BindCheckBoxEvenTwo(idr,flag){
    	    if($(".row_mod_"+idr+" input").length != 0){
    	    	var i = 0;
    	    	$(this)[0].checked = flag;					//选中或者取消二级
    	    	
				$(".row_mod_"+idr+" input").each(function(){//循环并判断二级选中个数
					if($(this)[0].checked == true){
						i++;
						$(".row_men_"+idr+" input").each(function(){
							$(this)[0].checked = true;
						});
					}else{
						var _this = $(this).parent().next();
					    _this.find("input").each(function(){//取消功能权限
							$(this)[0].checked = false;
						});
					}
				});
				if(i == 0){
					$(".row_men_"+idr+" input").each(function(){//取消一级
						$(this)[0].checked = false;
					});
					$(".row_author_"+idr+" input").each(function(){//取消单行全选
						$(this)[0].checked = flag;
					});
				}
		    }
    }
    
    //三级操作
    function BindCheckBoxEvenThre(idr,flag){
    	 if($(".row_ui_"+idr+" input").length != 0){
    	 	var i = 0,j=0;
    	    $(this)[0].checked = flag;				   //选中或者取消功能权限
    	    
    	    $(".row_ui_"+idr+" input").each(function(){//循环并判断三级选中个数
				if($(this)[0].checked == true){
					i++;
					if($(".row_mod_"+idr+" input").length != 0){//选中二级
						var _this = $(this).parent().prev();
						_this.find("input").each(function(){
							$(this)[0].checked = true;
						});
					}
					$(".row_men_"+idr+" input").each(function(){//选中一级
						$(this)[0].checked = true;
					});
				}
			});
			if(i == 0){
				if($(".row_mod_"+idr+" input").length != 0){
					
					$(".row_mod_"+idr+" input").each(function(){//判断二级选中个数
						if($(this)[0].checked == true){
							j++;
							$(".row_men_"+idr+" input").each(function(){//选中一级
								$(this)[0].checked = true;
							});
						}
					});
					if(j == 0){
						$(".row_men_"+idr+" input").each(function(){//取消一级
							$(this)[0].checked = false;
						});
					}
				}else{
					$(".row_men_"+idr+" input").each(function(){//取消一级
						$(this)[0].checked = false;
					});
				}
			}
    	 }
    }

    function SaveBtnEven(){
    		var result_menu = [],menu = "",rid = -1;
    		for(var i=0;i<ids.length;i++){
    			$(".row_men_"+ids[i]+" input").each(function(){
					var idr = ids[i];
					var aa = {};
    				if($(this)[0].checked){
		    			aa.id = idr;
		    			aa.value = $(this).attr("value");
		    			aa.mode = [];
		    			aa.mode2 = [];
		    			if($(".row_mod_"+idr+" input").length==0){
		    				$(".row_mod_"+idr).next().find("input").each(function(){
					    		if($(this)[0].checked){
					    			var cc = {};
					    			cc.id = $(this).attr("ggid");
					    			cc.value = $(this).attr("value");
									aa.mode2.push(cc);
					    		}
					    	});
		    			}else{
			    			$(".row_mod_"+idr+" input").each(function(){
				    			if($(this)[0].checked){
				    				var bb = {};
				    				bb.id = idr;
				    				bb.value = $(this).attr("value");
				    				bb.ui = [];
				    				var _this = $(this).parent().next();
				   
				    				_this.find("input").each(function(){
						    			if($(this)[0].checked){
						    				var cc = {};
						    				cc.id = $(this).attr("ggid");
						    				cc.value = $(this).attr("value");
											bb.ui.push(cc);
						    			}
						    		});
									aa.mode.push(bb);
				    			}
				    		});
				    	}
					  result_menu.push(aa);
   					}
    			});
			}

    		rid = RoleUL.find(".sTask_back").attr("gid");
			menu = $.toJson(result_menu)
			$(this)[AJ]({
			    	param:{action:'sysManager.InsertRolePermission',result_menu:menu,rid:rid},
			    	success:function(data){
			    		$[MB]({
							  	content: '保存成功!',
								type: 0,
								isAutoClose: TE,
								onAffirm: function (state) { 
									if(state == true){
										ShowPerData();
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
            $('<div />').load('modules/sysManager/sysManager.authorityManager.htm', function () {
            	base = context.Base;
            	cuser=context.CurrentUser;
            	contentDiv[AP](base.FormatPmi($(this).html()));
            	navigationBars = context.NavigationBars;
            	
            	t.IsLoad = TE;
                $key = $('#' + key);
                RoleUL = $(".sec_taskBack",$key);
                PermissionTab = $(".web_table",$key);
                PermissionTr = ' <tr><td class="row_author_{2}"><input type="checkbox" name="custom_one" id="sys_author_{2}" {3} {4}/></td><td class="row_men_{2}"><input value="{1}" id="sys_menu_{2}" ggid="{2}" {3} {4} name="custom_one" type="checkbox" class="com_fLeft author_mar" /><label for="{2}">{0}</label></td><td class="row_mod_{2}"></td><td class="row_ui_{2}"></td></tr>';
                ModuleList = '<tr><td class="row_mod_{2}"><input id="sys_mode_{5}" ggid="{2}" value="{0}" name="custom_one" type="checkbox" class="com_fLeft author_mar" /><label for={2} class="com_fLeft">{1}</label></td><td id="t_td_{3}_{4}" class="row_ui_{2}"></td></tr>';
               	Permissions = '<input id="sys_ui_{4}" value="{0}" ggid="{3}" name="custom_one" type="checkbox" class="com_fLeft author_mar" /><label for={2} class="com_fLeft">{1}</label>';  
               	Permissions2 = '<input id="sys_mode_{3}" value="{0}" ggid="{2}" name="custom_one" type="checkbox" class="com_fLeft author_mar" /><label for={2} class="com_fLeft">{1}</label>';  
                SaveBtn = $(".sys_save",$key);
                AddBtn = $(".event_add",$key);
                AlertDiv = '<div class="giveMe_do event new_Ftask"><div class="div_style"><label class="com_fLeft" for="F_task">角色名称：</label><input type="text" maxlength="8" class="task_count task_Cwdith1" id="authorityManager_RoleName" /></div><div class="com_clear"></div><div style="margin-left: 70px;margin-top: 10px; float: left; color: red; font-size: 12px; display: block;"><span class="com_none" id="role_name"></span></div><div class="com_clear"></div><div class="push_ok"><div class="in_MsetCno">取&nbsp;&nbsp;消</div><div class="in_MsetCok">确&nbsp;&nbsp;定</div></div><div class="com_clear"></div></div>';
                RoleInput = $("#authorityManager_RoleName",$key);
                
                
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