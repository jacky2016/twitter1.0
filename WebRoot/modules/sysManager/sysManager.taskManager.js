//任务管理
define(['portal'], function (context) {
    //定义变量
    var key = 'taskManager',
          $key, //当前的作用域
          contentDiv,
          FirstTask,
          SecondTask,
          ThirdTask,
          NewFirstBtn,
          FirstAlertDiv,
          NewSecondBtn,
          SecondAlertDiv,
          NewTaskBtn,
          TaskMain,
          ExpBtn,
          h2,
          LaJiBtn,
          NewTaskBtn,
          NewTaskCBtn,
          LaJiAlertDiv,
          LaJiSel,
          LaJiSelBtn,
          NewTaskNameInput;
           
    //初始化方法，写自己模块的业务
    function Init() { 
    	NavigationInit();
    	GetFrist(FirstTask,true);
    }
    //导航条初始化方法,
	function NavigationInit() {
		navigationBars.Empty()
			.Add(key, 'NavToMP', '系统管理')
			.Add(key, 'NavToMP_rwgl', '任务管理')[oL]();
	} 

    //加载一级菜单      
    function GetFrist(FirstTask,isFirst){
    	$("#new_tasks",$key)[AC]("com_none");
	    $("#taskManager_main",$key)[RC]("com_none");
	    
    	FirstTask.html("");
    	FirstTask[AJ]({
    		param:{action:'sysManager.GetFirstTaskGroup',pid:-1},
    		success:function(data){
    			
    			var insert_power1 = cuser.Templates['rwgl_insert_1'];		//取得添加权限
				var update_power1 = cuser.Templates['rwgl_update_1'];		//取得修改权限
				var delete_power1 = cuser.Templates['rwgl_delete_1'];		//取得删除权限

    			if( insert_power1 != null && insert_power1 != ""){
	    			NewFirstBtn[EV](CK,function(){//创建
		    			var btn = $(this);
						$(this)[AJ]({
						    param:{action:'sysManager.InsertTask',method:1},
						    success:function(data){
						    	// -1：到达添加次数，限制提示，0：插入失败，数据库错误提示，1：添加成功
								if(data == -1){
									$[MB]({ content: '添加失败,添加已到达上限!', type: 1});
								}else{
									 FirstBtnBind(btn,'新建一级菜单',11,'','');
								}
						    }
						});
		    				
		    		});
		    	}
    			if(data.length == 0){
    				$(".ftask_title",$key)[AC]("com_none");
    				SecondTask.html("");
    				ThirdTask.html("");
    			}else{
    				$(".ftask_title",$key)[RC]("com_none");
	    			var li = '<li gid="{0}" class="li_task"><span class="tasks_style sTask_count " title="{2}">{1}</span><div class="sysTtask_do"><span gid="{0}" class=" sysTask_left tasks_style"></span><span gid="{0}" class=" sysTask_right tasks_style"></span>{pmi:rwgl_update_1}{pmi:rwgl_delete_1}</div></li>'; 
	    			var _li = base.FormatPmi(li);
	    			$.each(data,function(i,item){
	    				var _name = item.groupname.substring(0,5);
	    				FirstTask.append($[FO](_li,item.id,_name,item.groupname));
	    			});
	    			if(isFirst)FirstTask.find("li:first-child")[AC]("sTask_back");
	    			//btnbing
	    			var leftBtn = $(".sysTask_left",FirstTask),rightBtn = $(".sysTask_right",FirstTask),editBtn = $(".sysTask_edit",FirstTask),delBtn = $(".sysTask_del",FirstTask);
	
	    			//一级菜单图标排序
	    			leftBtn.first()[RC]("sysTask_left");
	    			rightBtn.first()[RC]("tasks_style sysTask_right");
	    			rightBtn.first()[AC]("sysTask_left tasks_style");
	    			leftBtn.last()[RC]("sysTask_left");
	  
	    			$(".sysTask_right",FirstTask)[EV](CK,function(){//上移动
	    				MoveBtnBind($(this),parseInt($(this).attr("gid")),1);
	    				return false;
	    			});

	    			$(".sysTask_left",FirstTask)[EV](CK,function(){//下移动
	    				MoveBtnBind($(this),parseInt($(this).attr("gid")),2);
	    			});
	    			if( update_power1 != null && update_power1 != ""){
		    			editBtn[EV](CK,function(){//编辑
		    				var btn = $(this);	
		    				FirstBtnBind(btn,'编辑一级菜单',12,btn.attr("gid"),btn.attr("title"));
		    			});
	    			}
	    			
	    			if( delete_power1 != null && delete_power1 != ""){
		    			delBtn[EV](CK,function(){//删除
		    				var btn = $(this),gid = parseInt(btn.attr("gid"));
		    				$[MB]({
		    					content:'您确认要执行删除操作?',
		    					type:4,
		    					onAffirm:function(state){
		    						if(state){
		    							DelBtnCBind(btn,gid,10);
		    						}
		    					}
		    				});
		    			});
	    			}
	    			FirstTask.find("li")[EV](CK,function(){
	    				FirstTask.find("li")[RC]("sTask_back");
	    				$(this).addClass("sTask_back");
	    				GetSecond(SecondTask,true);
	    			});
	    			GetSecond(SecondTask,true);
    			}
    			
    		}
    	});
    }
    
    //创建,修改一级菜单
    function FirstBtnBind(NewFirstBtn,title,option,gid,tname){
    		$[WW]({
    			css: { "width": "360px", "height": "auto" },
    			title: title,
    			id: 'taskManager_FirstBtnDiv',
    			position: 'center',
    			content: FirstAlertDiv,
    			onLoad:function(div,close){
    				var input = $("#taskManager_first_input");
    				if(option==12){
    					 input.val(tname);
    				}else{
    					 input.val("");
    				}
    				var ck_name = $.trim(input.val());
     				$(".in_MsetCok",div)[EV](CK,function(){//确定编辑/新建
     					$("#menu_name",div).html('');
     					var name = $.trim(input.val());
	    				if(name == ''){
	    					$("#menu_name",div).show();
	    					$("#menu_name",div)[AP]("* 请输入菜单名称");
	    				}else if(name != ''){
	    					if(!$.regSpecial(name)){
	    						$("#menu_name",div).show();
	    						$("#menu_name",div)[AP]("* 菜单名称不能包含无效字符");
	    					}else{
		    					div[AJ]({
									param: {action: 'sysManager.InsertRubbish',name: name,method:3},
										success: function(data){
										if(data == true){
											if(option==12){
												if(ck_name != name){
													$("#menu_name",div).show();
													$("#menu_name",div)[AP]("* 菜单名已存在，请重新输入");
												}else{
													$("#menu_name",div).hide();
		    										AddFirst(FirstTask,name,option,gid,close);
												}
											}else{
												$("#menu_name",div).show();
												$("#menu_name",div)[AP]("* 菜单名已存在，请重新输入");
											}
										}else{
											$("#menu_name",div).hide();
		    								AddFirst(FirstTask,name,option,gid,close);
										}
									}
								});
							}
	    				}
     				});
     				$(".in_MsetCno",div)[EV](CK,function(){//确定取消
     					close();
     				});
     			}
    		});
    		return false;
    }
    //添加编辑一级菜单的异步
    function AddFirst(FirstTask,name,option,gid,close){
    	if(option == 11){//添加
	    	FirstTask[AJ]({
	    		param:{action:'sysManager.InsertTask',firstName:name,method:2},
	    		success:function(data){
	    			close();
	    			GetFrist(FirstTask,true);
	    		}
	    	});
    	}else if(option == 12){//编辑
    		FirstTask[AJ]({
    		param:{action:'sysManager.UpdateTask',firstName:name,gid:gid,method:1},
    		success:function(data){
    			close();
    			GetFrist(FirstTask,true);
    		}
    	});
    	}
    }
        
    //移动按钮
    function MoveBtnBind(Btn,gid,option){
    	Btn[AJ]({
    		param:{action:'sysManager.Move',gid:gid,option:option},
    		success:function(data){
    			GetFrist(FirstTask,true);
    		}
    	});
    }
    
    //菜单删除按钮
    function DelBtnCBind(Btn,gid,option){
    	Btn[AJ]({
    		param:{action:'sysManager.DeleteTask',pid:gid,method:1},
    		success:function(data){
    			GetFrist(FirstTask,true);
    		}
    	})
    }
    
    //二级菜单
    function GetSecond(SecondTask,isFirst){
    	SecondTask.html("");
    	var pid = parseInt($(".sTask_back",FirstTask).attr("gid"));
    	SecondTask[AJ]({
    		param:{action:'sysManager.GetFirstTaskGroup',pid:pid},
    		success:function(data){
    			if(data.length!=0){
	    			var div ='<div gid="{0}" class="stask_boxList"><h4 class="h4_title"><span>{1}</span>{pmi:rwgl_delete_2}{pmi:rwgl_update_2}{pmi:rwgl_insert_3}</h4><ul class="three_tasks"></ul></div>';
					var _div = base.FormatPmi(div);
	    			$.each(data,function(i,item){
	    				SecondTask.append($[FO](_div,item.id,item.groupname));
	    			});
    			}
    			var editBtn = $(".sysTask_edit",SecondTask),delBtn = $(".sysTask_del",SecondTask),addthree = $(".add_three",SecondTask);
				var insert_power2 = cuser.Templates['rwgl_insert_2'];		//取得添加权限
				var update_power2 = cuser.Templates['rwgl_update_2'];		//取得修改权限
				var delete_power2 = cuser.Templates['rwgl_delete_2'];		//取得删除权限
				var insert_power3 = cuser.Templates['rwgl_insert_3'];		//取得添加权限

				if( update_power2 != null && update_power2 != ""){
	    			editBtn[EV](CK,function(){
	    				var btn = $(this),gid = parseInt(btn.attr("gid"));
	    				SecondBtnBind(btn,'编辑二级菜单',22,gid,pid);
	    			}); 
	    		}
	    		if( insert_power2 != null && insert_power2 != ""){
	    			NewSecondBtn[EV](CK,function(){
	    				var btn = $(this);
	    				SecondBtnBind(btn,'新建二级菜单',21,"",pid);
	    			}); 
	    		}
	    		if( delete_power2 != null && delete_power2 != ""){
	    			delBtn[EV](CK,function(){
	    				var btn = $(this),gid = parseInt(btn.attr("gid"));
	    				$[MB]({
	    					content:'您确认要执行删除操作?',
	    					type:4,
	    					onAffirm:function(state){
	    						if(state){
	    							btn[AJ]({
							    		param:{action:'sysManager.DeleteTask',pid:gid,method:2},
							    		success:function(data){
							    			GetSecond(SecondTask,true);
							    		}
							    	})
	    						}
	    					}
	    				});
	    			});
	    		}
	    		
	    		if( insert_power3 != null && insert_power3 != ""){
	    			addthree[EV](CK,function(){//添加二级子菜单
	    				var btn = $(this),gid = parseInt(btn.attr("gid"));
	    				var data ={};
	    				data.id=-1,data.name="",data.keywords=null,data.groupID=gid;
	    				
	    				$(this)[AJ]({
					    	param:{action:'sysManager.InsertTask',method:4},
					    	success:function(data1){
					    		// -1：到达添加次数，限制提示，0：插入失败，数据库错误提示，1：添加成功
								if(data1 == -1){
									$[MB]({ content: '添加失败,添加已到达上限', type: 1});
								}else{
									$("#mec_name",$key).hide();
									NewTaskBtnBind(btn,data,pid);
								}
					    	}
						});
	    			});
	    		 }
	    			
	    		var secs = $(".stask_boxList",SecondTask);
	    			
	    		$.each(secs,function(){
	    			var div = $(this);
	    			GetTask(div.find(".three_tasks"),div,pid);
	    		});
    		}
    	});
    }
    
    //创建，修改二级菜单
    function SecondBtnBind(NewSecondBtn,title,option1,gid,pid){
    		$[WW]({
    			css: { "width": "360px", "height": "auto" },
    			title: title,
    			id: 'taskManager_SecondBtnDiv',
    			content: SecondAlertDiv,
    			onLoad:function(div,close){
    				var fsel = $("#taskManager_first_select",div);
    				var sinput = $("#taskManager_second_input");
    				fsel.html("");
    				var option = '<option value="{0}" {2} >{1}</option>';
    				var _idss =0;
    				fsel[AJ]({
    					type:"POST",
    					param:{action:'sysManager.GetFirstTaskGroup',pid:-1},
    					success:function(data){
	    					$.each(data,function(i,item){
		    					if(pid==item.id){
		    						_idss = item.id;
		    						fsel.append($[FO](option,item.id,item.groupname,'selected="selected"'));
		    					}else{
		    						fsel.append($[FO](option,item.id,item.groupname,''));
		    					}
	    					});
    					}
    				});
    				
    				var ck_name="";
    				if(option1 == 22){
    					sinput.val(NewSecondBtn.parent().prev().text());	
    					$(BY)[AJ]({
								param: {action: 'sysManager.UpdateTask',method:2,id:gid },
								success: function(data){
									sinput.val(data.groupName);
									ck_name = $.trim(sinput.val());
								}
						});
    				}else{
    					sinput.val("");
    				}
     				$(".in_MsetCok",div)[EV](CK,function(){
     					$("#mode_name",div).html('');
     					var _name = $.trim(sinput.val());
     					var _id = fsel.val();
     					
	    				if(_name == ''){
	    					$("#mode_name",div).show();
	    					$("#mode_name",div)[AP]("* 请输入菜单名称");
	    				}else if(_name != ''){
	    					if(!$.regSpecial(_name)){
	    						$("#mode_name",div).show();
	    						$("#mode_name",div)[AP]("* 菜单名称不能包含无效字符");
	    					}else{
		    					div[AJ]({
									param: {action: 'sysManager.InsertRubbish', id:_id,_name: _name,method:4},
										success: function(data){
										if(data == true){
											if(option1 == 22){
												if(ck_name != _name){
													$("#mode_name",div).show();
													$("#mode_name",div)[AP]("* 菜单名已存在，请重新输入");
												}else{
													if(_id == _idss){
														$("#mode_name",div).hide();
		    											AddSecond(FirstTask,_name,_id,option1,gid,close);
													}else{//修改一级类别
														div[AJ]({
															param: {action: 'sysManager.InsertRubbish', id:_id,_name: _name,method:4},
															success: function(data){
																if(data == true){
																	$("#mode_name",div).show();
																	$("#mode_name",div)[AP]("* 菜单名已存在，请重新输入");
																}else{
																	$("#mode_name",div).hide();
		    														AddSecond(FirstTask,_name,_id,option1,gid,close);
																}
															}
														});
													}
												}
											}else{
												$("#mode_name",div).show();
												$("#mode_name",div)[AP]("* 菜单名已存在，请重新输入");
											}
										}else{
											$("#mode_name",div).hide();
		    								AddSecond(FirstTask,_name,_id,option1,gid,close);
										}
									}
								});
							}
	    				}
     				});
     				$(".in_MsetCno",div)[EV](CK,function(){
     					 close();
     				});
     			}
    		});
    		return false;
    }
    //添加二级菜单的异步
    function AddSecond(FirstTask,name,pid,option,gid,close){
    	if(option == 21){//添加
	    	SecondTask[AJ]({
	    		param:{action:'sysManager.InsertTask',firstName:name,pid:pid,option:option,gid:gid,method:3},
	    		success:function(data){
	    			FirstTask[RC]("first_taskBack");
	    			$.each(FirstTask,function(i,item){
	    				if($(this).attr("gid")==pid){
	    					$(this)[AC]("first_taskBack");
	    					return;
	    				}
	    			});
	    			close();
	    			GetSecond(SecondTask,true);
	    		}
	    	});
    	}else if(option == 22){//编辑
    		SecondTask[AJ]({
	    		param:{action:'sysManager.UpdateTask',firstName:name,pid:pid,option:option,gid:gid,method:3},
	    		success:function(data){
	    			FirstTask[RC]("first_taskBack");
	    			$.each(FirstTask,function(i,item){
	    				if($(this).attr("gid")==pid){
	    					$(this)[AC]("first_taskBack");
	    					return;
	    				}
	    			});
	    			close();
	    			GetSecond(SecondTask,true);
	    		}
	    	});
    	}
    }
    
   function GetSel_Group(ssel,fsel,selectid){//加载二级菜单
    	var option = '<option value="{0}" {2}>{1}</option>';
    		ssel.html("");
    		ssel[AJ]({
    				type:"POST",
    				param:{action:'sysManager.GetFirstTaskGroup',pid:fsel.val()},
    				success:function(data){
    					$.each(data,function(i,item){
    						if(item.id==selectid){
    								ssel.append($[FO](option,item.id,item.groupname,'selected="selected"'));
    						}else{
	    							ssel.append($[FO](option,item.id,item.groupname,''));
	    					}
	    				});	
    				}
    		});
    }
    
    //以下为任务设置模块
    function NewTaskBtnBind(Btn,data1,pid){
	    	$("#new_tasks",$key)[RC]("com_none");
	    	$("#taskManager_main",$key)[AC]("com_none");
	    	var fsel = $("#taskManager_first_taMtype");
	    	var ssel = $("#taskManager_sec_taMtype");
    				fsel.html("");
    				var option = '<option value="{0}" {2}>{1}</option>';
    				fsel[AJ]({
    					type:"POST",
    					param:{action:'sysManager.GetFirstTaskGroup',pid:-1},
    					success:function(data){
	    					$.each(data,function(i,item){
		    					if(item.id == pid){
		    						fsel.append($[FO](option,item.id,item.groupname,'selected="selected"'));
		    					}else{
		    						fsel.append($[FO](option,item.id,item.groupname,''));
		    					}
	    					});
    						GetSel_Group(ssel,fsel,data1.groupID);
    						fsel[EV](CE,function(){
    							GetSel_Group(ssel,fsel,data1.groupID);
    						});
    						
    					  TaskDataBind(data1);//任务数据绑定
	    				  LaJiBtn[EV](CK,function(){
	    				  	 LaJiBtnBind();//设置垃圾词
	    				  });
	    				  if(data1.id == -1){
	    				  	 LaJiSelDataBind([]);//垃圾词管理  添加
	    				  }else{
	    				  	 LaJiSelDataBind(data1.rubbishList);//垃圾词管理 编辑
	    				  } 
	    				  
	    				  //取消回格键
	    				  $("#task_exptext").keydown(function(event) {
				              event = event || window.event;
				              var which = event.keyCode || event.which;
				              if(which == 8){
				                return FE;
				              }
				          });
	    				  ExpClear();//清空
	    				  NewTaskSaveBtnBind();//保存
	    				  NewTaskCancelBtnBind();//取消
    					}	
	    				
    				});
	    return false;
    }
    
    //任务
    function GetTask(ThirdTask,div,pid){
    	ThirdTask.html("");
    	var gid = parseInt(div.attr("gid"));
    	SecondTask[AJ]({
    		param:{action:'sysManager.GetTaskList',pid:gid,method:1},//doto 
    		success:function(data){//alert($.toJson(data));
    			var li = '<li gid ="{0}" title="{2}" class="li_task"><span class="tasks_style  thTask_count ">{1}</span><div class="sysTtask_do">{pmi:rwgl_update_3}{pmi:rwgl_delete_3}</div></li>';
    			var _li = base.FormatPmi(li);
    			$.each(data,function(i,item){
    				ThirdTask.append($[FO](_li,item.id,item.name.substring(0,5),item.name));
    			});
    			SecondTask[FD]("li")[EV](CK,function(){
    				SecondTask.find("li")[RC]("tTask_back");
    				$(this)[AC]("tTask_back");
    			});
    			var editBtn = $(".sysTask_edit",ThirdTask), delBtn = $(".sysTask_del",ThirdTask);
    			var update_power3 = cuser.Templates['rwgl_update_3'];		//取得添加权限
    			var delete_power3 = cuser.Templates['rwgl_delete_3'];		//取得添加权限

    			if( update_power3 != null && update_power3 != ""){
	    			editBtn[EV](CK,function(){//修改二级子菜单
	    				$("#mec_name",$key).hide();
	    				var $this = $(this),index = editBtn[ID]($this),viewData = data[index];
	    				var id = viewData.id;
	    				$(BY)[AJ]({
	    					param:{action:'sysManager.GetTaskList',id:id,method:2},
	    					success:function(data1){
	    						NewTaskBtnBind($this,data1,pid);
	    					}
	    				}); 
	    				
	    			});
    			}
				
				if( delete_power3 != null && delete_power3 != ""){
	    			delBtn[EV](CK,function(){//删除二级子菜单
	    				var gid = $(this).attr("gid");
	    				$[MB]({
							  	content: '您确定要删除吗？',
								type: 4,
								onAffirm: function (state) { 
									if(state == true){
										$(BY)[AJ]({
												param:{action:'sysManager.DeleteTask',gid:gid,method:3},
												success: function(data){
													GetSecond(SecondTask,true);
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
    //任务的数据绑定
    function TaskDataBind(data){//alert($.toJson(data));
    		$("#task_exptext").val("").attr("key","");
    		NewTaskNameInput.val(data.name);
    		NewTaskNameInput.attr("ggid",data.id);
    		if(data.keywords!=null && data.keywords!= ""){
    			ShowExp(data.keywords);
    			var tree = data.keywords == null ? null :new QueryFormat(data.keywords.replace(/\"/g,"")).outputExpressionTree();
    			var text = $.expressionText(tree,[{ value: 'content1', text: '全文' }, { value: 'content', text: '原文' }]);
    			$("#task_exptext").val(text).attr("key",data.keywords);
    		}else{
    			ShowExp(null);  //设置表达式  
    		}
    }
    
    function ShowExp(newKey){//设置表达式
   
    	ExpBtn[EV](CK, function() {
    		$[WW]({
    		 css: { "width": "900px", "height": "580px","overflow": "auto", "background-color": "#FFF" },
    		 title: "设置表达式",
    		 id: 'task_exp',
    		 onLoad: function(div, close) { 
    		 	newKey = $("#task_exptext")[AT]("key");
    		 	if(newKey == ""){newKey = null}
    		 	var tree = newKey == null ? null :new QueryFormat(newKey.replace(/\"/g,"")).outputExpressionTree();
    		 	 div[FD]('.content').expression({
                    	select: [{ value: 'content1', text: '全文' }, { value: 'content', text: '原文' }],
                        value: tree,
                        type:2,
                        onOK: function (txt, key, exp) {
                        	key == '' ? null : key;
                        	$("#task_exptext").val(txt).attr("key",key);
			                close();
                        },
                        onCancel:function() {
                        	close();
                        }
                    });
    		 	}
    		});
    	});
    }
    //清空
    function ExpClear(){
    	$("#task_expclear")[EV](CK,function(){
    		$("#task_exptext").val("").attr("key","");
    	});
    }
    
    function LaJiBtnBind(){//设置垃圾词
    		$[WW]({
    			css: { "width": "580px", "height": "auto" },
    			title: '垃圾词设置',
    			id: 'taskManager_lajici',
    			content: LaJiAlertDiv,
    			onLoad:function(div,close){
    			  	var tablediv = $("#newtask_lajici_tab"),opdiv = $("#newtask_lajici_op"),addBtn = $(".do_botton",tablediv),ck_name="",
    			  	saveBtn_opdiv = $(".in_MsetCok",opdiv),cancelBtn_opdiv = $(".in_MsetCno",opdiv),groupnameinput = $("#newtask_lajici_groupname"),lajiciinput = $("#newtask_lajici_lajici"),
    			  	cancelBtn_div = $(".in_MsetCno",div),pushdiv = $("#push_ok"), push_ok = $(".in_MsetCno",pushdiv),
    			  	lajiciul = $(".event_Wadd",opdiv),addljBtn = $(".tWord_add",opdiv),oplihtml = '<li text="{0}" class="ev_wPlace"><span class="ev_wCount">{0}</span><span class="ev_wDel"></span></li>';
    			  	opdiv.addClass("com_none"),editBtn = $(".sysTask_edit",tablediv),tab_delBtn = $(".sysTask_del",tablediv),
    			  	tab_ul = $(".sec_taskBack",tablediv),tablihtml = '<li gid="{0}" class="li_task"><span class="tasks_style sTask_count " title="{1}">{3}</span><div class="sysTtask_do"><span keywords="{2}" name ="{1}" gid="{0}" class="sysTask_edit tasks_style"></span><span gid="{0}" class="sysTask_del tasks_style"></span></div></li>',
    			  	tab_lajiul = $(".event_Wadd",tablediv),tab_lajili = '<li title="{0}" class="ev_wPlace"><span class="ev_wCount">{0}</span></li>';
    			  	
    			  	databind();
    			  	//垃圾词列表数据绑定
    			  	function databind(){
    			  		tab_ul.html("");
    			  		tab_ul[AJ]({
    			  			param:{action:'sysManager.GetRubbishList'},
    			  			success:function(data){
    			  				$.each(data,function(i,item){
    			  					var _name = item.groupname.substring(0,5)
    			  					tab_ul.append($[FO](tablihtml,item.id,item.groupname,item.rubbishwords,_name));
    			  				});
    			  				var lis = tab_ul.find("li");
    			  				lis[EV](CK,function(){
    			  					lis[RC]("sTask_back");
    			  					$(this)[AC]("sTask_back");
    			  					keywordDatabind();
    			  				}).eq(0)[TR](CK);
    			  			}
    			  		});
    			  	}
    			  	
    			  	//垃圾词列表数据绑定成功后执行
    			  	function ajafter(){
    			  		tab_ul.find("li").first()[AC]("sTask_back");
    			  		keywordDatabind();
    			  	}
    			  	
    			  	function keywordDatabind(){
    			  		tab_lajiul.html("");
    			  		var keywords = tab_ul[FD](".sTask_back")[FD](".sysTask_edit")[AT]("keywords");
    			  		var arr = keywords.split(" ");
    			  		$.each(arr,function(i,item){
    			  			tab_lajiul.append($[FO](tab_lajili,item));
    			  		});	
    			  		var editBtn = $(".sysTask_edit",tab_ul),tab_delBtn = $(".sysTask_del",tab_ul);
    			  		//编辑
			    		editBtn[EV](CK,function(){
			    			Reset();
			    			IntoSet($(this));
    			  			tablediv[AC]("com_none");
    			  			opdiv[RC]("com_none");
    			  			$("#group_name",div).hide();
    			  			$("#garbage_name",div).hide();
    			  			ck_name = $.trim(groupnameinput.val());
			    		});
			    		//删除
			    		tab_delBtn[EV](CK,function(){
			    			var btn = $(this);
			    			btn[AJ]({
						    	param:{action:'sysManager.DeleteRubbish',gid:btn.attr("gid")},
						    	success:function(data){
						    		LaJiBtnBind();
						    		LaJiSelDataBind([]);
						    	}
						    });
			    		});
    			  	}
    			  	
    			  	//切换到添加界面
    			  	addBtn[EV](CK,function(){
    			  		$(this)[AJ]({
					    	param:{action:'sysManager.InsertRubbish',method:2},
					    	success:function(data){
					    		// -1：到达添加次数，限制提示，0：插入失败，数据库错误提示，1：添加成功
								if(data == -1){
									$[MB]({ content: '添加失败,添加已到达上限', type: 1});
								}else{
									Reset();
			    			  		tablediv[AC]("com_none");
			    			  		opdiv[RC]("com_none");
			    			  		$("#group_name",div).hide();
			    			  		$("#garbage_name",div).hide();
			    			  		ck_name = $.trim(groupnameinput.val());
								}
					    	}
						});
    			  	});
    			  	
    			  
    			  	//操作界面的保存按钮
    			  	saveBtn_opdiv[EV](CK,function(){
    			  	   
    			  		$("#group_name",div).html('');
    			  	    $("#garbage_name",div).html('');
    			  	    
    			  		var name = $.trim(groupnameinput.val()),gid = groupnameinput.attr("gid"),action='',
    			  		lis = lajiciul.find("li"),keywords = "";
    			  		$.each(lis,function(i,item){
    			  			keywords += $(this).attr("text")+" ";
    			  		});
    			  		
    			  		if(name == ""){
    			  			$("#group_name",div).show();
    			  			$("#group_name",div)[AP]("* 请输入组名称");
    			  		}else if(name != ""){
    			  			if(!$.regNorm3(name)){
    							$("#group_name",div).show();
	    						$("#group_name",div)[AP]("* 姓名不能包含无效字符");
    						}else{
    							div[AJ]({
	    							param: {action: 'sysManager.UpdateRubbish',name: name,method:2},
									success: function(data){
										if(data == true){
											if(gid != -1){
												if(ck_name != name){
													$("#group_name",div).show();
								    				$("#group_name",div)[AP]("* 组名已存在，请重新输入");
												}else if(keywords == ''){
												    $("#group_name",div).hide();
												    $("#garbage_name",div).show();
													$("#garbage_name",div)[AP]("* 请添加垃圾词");
												}else{
												    $("#garbage_name",div).hide();
												    action='sysManager.UpdateRubbish';
												    saveBtn_opdiv[AJ]({
						    			  				param:{action:action,gid:gid,name:name,words:keywords,method:1},
								    			  		success:function(data){
								    			  				opdiv[AC]("com_none");
								    			  				tablediv[RC]("com_none");
								    			  				pushdiv[RC]("com_none");
								    			  				LaJiBtnBind();
								    			  				LaJiSelDataBind([]);
								    			  		}
							    			  		});
												 }
											}else{
												$("#group_name",div).show();
								    			$("#group_name",div)[AP]("* 组名已存在，请重新输入");
											}
										}else if(keywords == ""){
				    			  			$("#group_name",div).hide();
				    			  			$("#garbage_name",div).show();
				    			  			$("#garbage_name",div)[AP]("* 请添加垃圾词");
				    			  		}else{
				    			  			$("#garbage_name",div).hide();
					    			  		if(gid != -1){
					    			  			action='sysManager.UpdateRubbish';
					    			  		}else{
					    			  			action='sysManager.InsertRubbish';
					    			  		}
					    			  		saveBtn_opdiv[AJ]({
					    			  			param:{action:action,gid:gid,name:name,words:keywords,method:1},
					    			  			success:function(data){
					    			  				opdiv[AC]("com_none");
					    			  				tablediv[RC]("com_none");
					    			  				pushdiv[RC]("com_none");
					    			  				LaJiBtnBind();
					    			  				LaJiSelDataBind([]);
					    			  			}
					    			  		});
				    			  		}
									}
								});
    						}
    			  		}
    			  	});
    			  	//添加界面的取消按钮
    			  	cancelBtn_opdiv[EV](CK,function(){
    			  		opdiv[AC]("com_none");
    			  		tablediv[RC]("com_none");
    			  		pushdiv[RC]("com_none");
    			  	});
    			  	//添加界面的添加垃圾词按钮
    			  	addljBtn[EV](CK,function(){
    			  		$("#garbage_name",div).html('');
    			  		
    			  		var name = $.trim(lajiciinput.val());
	    			  	if(name == ''){
	    			  		$("#group_name",div).html('');
		    			  	$("#garbage_name",div).html('');
		    			  	$("#garbage_name",div).show();
		    			  	$("#garbage_name",div)[AP]("* 请输入垃圾词");
	    			  	}else{
	    			  		if($('#add_keyword li').length >= 20){
		    			  		lajiciinput.val('');
		    			  		$("#garbage_name",div).show();
		    			  		$("#garbage_name",div)[AP]("* 垃圾词组最多输入20个");
	    			  		}else{
	    			  			$("#garbage_name",div).hide();
	    			  			$("#ljc_none",div)[RC]('com_none');
		    			  		lajiciul.append($[FO](oplihtml,name));
		    			  		lajiciul.find(".ev_wDel")[EV](CK,function(){
			    			  		$(this).parent().remove();
			    			  		if($('#add_keyword li').length == 0){
			    			  			$("#ljc_none",div)[AC]('com_none');
			    			  		}
			    			  	});
			    			  	lajiciinput.val("");
	    			  		}
	    			  	}
    			  	});
    			  	
    			  	function Reset(){
    			  	
    			  		pushdiv[AC]("com_none");
    			  		//注：focusin 光标进入  focusout为光标离开
				    	$('#newtask_lajici_lajici').focusin(function() {	//隐藏
				    		$('#group_name').hide();	
				    	});
    			  		groupnameinput.val("");
    			  		groupnameinput.attr("gid",-1);
    			  		lajiciinput.val("");
    			  		lajiciul.html("");
    			  	}
    			  	
    			  	function IntoSet(btn){
    			  		var gn = btn.attr("name"),words = btn.attr("keywords").split(" "),gid = btn.attr("gid");
    			  		$.trim(groupnameinput.val(gn));
    			  		groupnameinput.attr("gid",gid);
    			  		lajiciinput.val("");
    			  		$.each(words,function(index,item){
    			  			lajiciul.append($[FO](oplihtml,item));
    			  		});
    			  		lajiciul.find(".ev_wDel")[EV](CK,function(){
    			  			$(this).parent().remove();
    			  		});
    			  	}
    			  	
    			  	push_ok[EV](CK,function(){//关闭添加垃圾词层
    			  		pushdiv[RC]("com_none");
    			  		close();
    			  	});
    			}
    		});
    }
    //保存任务按钮(异步保存)
    function NewTaskSaveBtnBind(){
    	var ck_name = $.trim(NewTaskNameInput.val());
    	NewTaskBtn[EV](CK,function(){
    		$("#mui_name").html('');
    		$("#mec_name").html('');
    		var param = GetTaskParam();
    		var _name = $.trim(NewTaskNameInput.val());
    		var _secname = $.trim($("#taskManager_sec_taMtype").val());
    		var result = [];
    		$("#newTask_lajiSel input").each(function(i) {
		    	if (i != 0) {
		    		if($(this)[0].checked){
		    			 result.push(this.value);
		    		}
	            }
		    });	
   			
    		var _keywords = $("#task_exptext").attr("key");
	    	if(_name == ''){
	    		$("#mui_name",$key).show()
	    		$("#mui_name",$key)[AP]("* 请输入任务名称");
	    	}else if(_name != ''){
	    		if(!$.regSpecial(_name)){
	    			$("#mui_name",$key).show();
	    			$("#mui_name",$key)[AP]("* 菜单名称不能包含无效字符");
	    		}else if(_secname == ''){
					$("#mui_name",$key).hide();
					$("#mec_name",$key).show();
					$("#mec_name",$key)[AP]("* 请选择二级菜单");
				}else if(_keywords == '' || _keywords == null){
					$[MB]({content: '表达式不能为空!', type: 1}); return FE;
				}else{
					$(this)[AJ]({
						param:param,
						success:function(data){
							$("#task_exptext").val("").attr("key","");	//清空文本值
					    	$("#new_tasks")[AC]("com_none");
						    $("#taskManager_main",$key)[RC]("com_none");
							GetSecond(SecondTask,true);
						}
					});   
				}
	    	}
		});
    }
    //获取任务参数
    function GetTaskParam(){
    	var name = $.trim(NewTaskNameInput.val()),
    	    id = NewTaskNameInput.attr("ggid"),
    	    groupid = $.trim($("#taskManager_sec_taMtype").val()),
    	    keywords = $("#task_exptext").attr("key"),
    	    searchtime = $.trim($("#taskManager_searchtime").val()),
    	    action = '',
    	    result = [];

	    $("#newTask_lajiSel input").each(function(i) {
	    	if (i != 0) {
	    		if($(this)[0].checked){
	    			 result.push(this.value);
	    		}
            }
	    });
	    if(id!=-1) {
	    	action = 'sysManager.UpdateTask';
	    } else {
	    	action = 'sysManager.InsertTask';
	    }
	    var rids = result.join(",");
    	var param = {
    		action: action,
    		taskname: name,
    		gid: id,
    		groupid: groupid,
    		keywords: keywords,
    		searchtime: searchtime,
    		method:5,
    		rubbishid: rids
		};
    	return param;
    }
    
    function LaJiSelDataBind(arr) {//垃圾词管理
    	LaJiSel.html("");
    	LaJiSel[AJ]({
    		param:{action:'sysManager.GetRubbishList'},
    		success:function(data) {
    			var h2obj = LaJiSel.prev(),
    			h2html='<span id="span_id">{0}</span><span class="mynum_type mynum_click"></span>',
    			lihtml='<li id="alert_id"><input type="checkbox" name="lajisel_group" id="fail_w{0}" class="lajis" value="{0}" {3} /><label title="{1}" id="alert_id" for="fail_w{0}">{2}</label></li>';
    			h2obj.html("");
    			if(data.length>0){
    				h2obj.html($[FO](h2html,"请选择"));
    			}else{
    				h2obj.html($[FO](h2html,"尚未添加"));
    			}
    			if(data.length>0){
    				LaJiSel.append('<li id="alert_id"><input type="checkbox" id="fail_w0" name="lajisel_group" value="0" /><label id="alert_id" for="fail_w0">全部</label></li>');
    				var allinput = $("#fail_w0");
    				allinput[EV](CK,function(){
    					var checkboxs = document.getElementsByName("lajisel_group");
    					var flag = checkboxs[0].checked;
    					for (var i=0;i<checkboxs.length;i++) {
 									var e=checkboxs[i];
 									if(i!=0)
  									e.checked=flag;
 						}
    				});
    			}
    			$.each(data,function(i,item){
    				var ischecked =false;
    				$.each(arr,function(y,yitem){
    					if(yitem.id==item.id){
    						ischecked =true;
    					}
    				});
    				if(!ischecked)
    				LaJiSel.append($[FO](lihtml,item.id,item.groupname,item.groupname.length>5?item.groupname.substring(0,4)+"...":item.groupname,''));
    				else
    				LaJiSel.append($[FO](lihtml,item.id,item.groupname,item.groupname.length>5?item.groupname.substring(0,4)+"...":item.groupname,'checked="checked"'));
    			});
    			
    		
    			var state = FE;
				LaJiSel[EV](CK, function(e) {
					if(e.target.id == 'alert_id' || e.target.id.indexOf('fail_w') > -1) {
						state = TE;
						LaJiSel[RC]("com_none");
					}else {
						LaJiSel[AC]("com_none");
						state = FE;
					}
				});
				
				$key[EV](CK, function(e) {
					if(e.target.id != 'h2' && e.target.id != 'span_id' && !state) {
						LaJiSel[AC]("com_none");
					}else {
						if(!state){
							var styleflag= LaJiSel[AT]("class");//获取样式是否隐藏
							if(styleflag == "com_none"){
								LaJiSel[RC]("com_none");
							}else{
								LaJiSel[AC]("com_none");
							}
						}
					}
					state = FE;
				});
    		}
    	});
    }
    
    function NewTaskCancelBtnBind(){//取消
    	NewTaskCBtn[EV](CK,function(){
    		$("#task_exptext").val("").attr("key","");	//清空文本值
    		$("#new_tasks")[AC]("com_none");
	    	$("#taskManager_main",$key)[RC]("com_none");
    	});
    }
    
    //关闭窗体事件
	function CloseWindow(div) {
	   div.hide();
	   $[RM]({ obj: '#' + JLY });
	}
    
    return {
    	IsLoad: FE,
        OnLoad: function (_ContentDiv) {
            var t = this;
            contentDiv = _ContentDiv;
            $('<div />').load('modules/sysManager/sysManager.taskManager.htm', function () {
            	base = context.Base;
            	cuser=context.CurrentUser;
            	contentDiv[AP](base.FormatPmi($(this).html()));
            	navigationBars = context.NavigationBars;
            	
            	t.IsLoad = TE;
                $key = $('#' + key);
                FirstTask = $(".sec_taskBackk",$key);
                SecondTask = $(".three_box",$key);
                ThirdTask = $(".three_tasks",$key);
                NewFirstBtn = $("#taskManager_newFirstBtn");
                FirstAlertDiv = '<div class="giveMe_do event new_Ftask"><div class="div_style"><label class="com_fLeft" for="F_task">一级菜单：</label><input type="text" class="task_count task_Cwdith1" maxlength="10" id="taskManager_first_input" /></div><div style="margin-left: 70px;margin-top: 10px; float: left; color: red; font-size: 12px; display: block;"><span class="com_none" id="menu_name"></span></div><div class="com_clear"></div><div class="push_ok"><div class="in_MsetCno">取&nbsp;&nbsp;消</div><div class="in_MsetCok">确&nbsp;&nbsp;定</div></div><div class="com_clear"></div></div>';
                NewSecondBtn = $("#taskManager_newSecondBtn");
                SecondAlertDiv = '<div class="giveMe_do new_Ftask"><div class="div_style"><label class="com_fLeft">一级菜单：</label><select id="taskManager_first_select" class="task_count task_Cwdith1"></select></div><div class="div_style"><label class="com_fLeft" for="F_task">二级菜单：</label><input type="text" class="task_count task_Cwdith1" maxlength="10" id="taskManager_second_input" /></div><div  style="margin-left: 70px;margin-top: 10px; float: left; color: red; font-size: 12px; display: block;"><span class="com_none" id="mode_name"></span></div><div class="com_clear"></div><div class="push_ok"><div class="in_MsetCno">取&nbsp;&nbsp;消</div><div class="in_MsetCok">确&nbsp;&nbsp;定</div></div><div class="com_clear"></div></div>';
                //NewTaskBtn = $("#taskManager_newTaskBtn");
                TaskMain = $("#new_tasks_main");
                ExpBtn = $("#task_expBtn");
                LaJiBtn = $("#taskManager_setexW");
                h2 = $("#h2",$key);
                NewTaskBtn = $("#newtask_save");
                NewTaskCBtn = $("#newtask_cancel");
                LaJiAlertDiv = '<div class="giveMe_do fail_Fset"><div id="newtask_lajici_tab"  class="taskM_box" ><div class="author_do"><label class="do_botton do_botton1">添加</label></div><div class="second_tasks"><ul class="sec_taskBack" ></ul></div><div class="author_set" ><ul class="event_Wadd"></ul></div></div><div id="newtask_lajici_op" class="box_count"><div class="div_style"><label class="task_name">组名称：</label><input id="newtask_lajici_groupname" type="text" maxlength="6" class="task_count task_Cwdith2"/></div><div style="margin-left: 100px;margin-top: 10px; float: left; color: red; font-size: 12px; display: block;"><span class="com_none" id="group_name"></span></div><div class="com_clear"></div><div class="div_style"><label class="task_name">垃圾词：</label><input id="newtask_lajici_lajici" type="text" maxlength="8" class="task_count task_Cwdith1"/><div class="tWord_add">添加词组</div></div><div class="com_none" id="garbage_name" style="margin-left: 100px;margin-top: 10px; float: left; color: red; font-size: 12px;></div><div class="com_clear"></div><div class="com_none" id="ljc_none"><ul class="event_Wadd F_wBor" id="add_keyword"></ul></div><div class="com_clear"></div><div class="push_ok"><div class="in_MsetCno">取&nbsp;&nbsp;消</div><div class="in_MsetCok">保&nbsp;&nbsp;存</div></div><div class="com_clear"></div></div><div class="push_ok" id="push_ok"><div class="in_MsetCno">关&nbsp;&nbsp;闭</div></div><div class="com_clear"></div></div>';
                LaJiSel = $("#newTask_lajiSel");
                LaJiSelBtn = $(".mynum_click",$key);
                NewTaskNameInput = $("#newtask_nameinput");
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