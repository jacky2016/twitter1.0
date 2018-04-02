//审核设置
define(['portal'], function (context) {
    //定义变量
    var key = 'approveSetting',
          $key, //当前的作用域
          contentDiv,
          addBtn,
          addAlertDiv,
          ApproveTable,
          ApproveInfo;

    //初始化方法，写自己模块的业务
    function Init() {
    	NavigationInit();
    	ApproveTableDataBind();
    }
    
    //导航条初始化方法,
	function NavigationInit(){
		navigationBars.Empty()
			.Add(key, 'NavToMP', '系统管理')
			.Add(key, 'NavToMP_shsz', '审核设置')[oL]();
	}
	//加载设置列表
    function ApproveTableDataBind(){
    	ApproveTable.html("");
    	var pager = $('#allPager', $key);
    	ApproveTable[TB]({
    		width: 1000,
            trHeight: 24, 	  //表格默认高
            pager: pager,
            pageSize: 15, 	  //显示条数
            isPager: TE, 	  //是否分页
            isCheckBox: FE,  //是否有多选按钮
            id:'_m1',
            rows: 'rows',
    		param:{action:'sysManager.GetApproves'},
    		columns:[{caption:'序号',field:'appid',width:'5%'},
    				{caption:'用户名称',field:'userNick',width:'10%'},
    				{caption:'用户登录账号',field:'userName',width:'10%'},
    				{caption:'是否需要审核',field:'isCheck',width:'10%'},
    				{caption:'审核人',field:'check',width:'10%'},
    				{caption:'操作',field:'',type:'html',width:'10%',template:''}],

    		onComplete: function (This, refresh, data) { 
    			//alert($.toJson(data));
    			
//    			This[FD]('.t0')[EH](data.rows,function (i,item){
//    				var option = '<input type="hidden" id="{0}"/>';
//    				alert(item.id);
//    				var upTemp = $[FO](option,item.id);
//				    $(this)[AP](upTemp);	
//    			});
    			
    			
    			var insert_power = cuser.Templates['shsz_insert_1'];		//取得添加权限
				var update_power = cuser.Templates['shsz_update_1'];		//取得修改权限
				var delete_power = cuser.Templates['shsz_delete_1'];		//取得删除权限
    			
    			//判断是添加权限
				if( insert_power != null && insert_power != ""){
					//添加事件
					addBtn[EV](CK,function(){
	    				addBtnBind(0,"账号授权");
	    			});
				}
				
    			//判断是修改权限
				if( update_power != null && update_power != ""){
					This[FD]('.t5')[EH](function (){
					
						var index=This[FD]('.t5')[ID]($(this));
				        var idNUM=$[TM]($(this)[PT]()[FD]('.t0')[TX]());	//取得每行id
	
				        var upTemp = $[FO](update_power,idNUM);
				        $(this)[AP](upTemp);	
				        
				        //编辑角色
		    			$("#approveSetting_edit"+idNUM,$key)[EV](CK,function(){
		    				addBtnBind(idNUM,"账号授权");
		    			});
				     });
				}
				
				//判断是删除权限
				if( delete_power != null && delete_power != ""){
					This[FD]('.t5')[EH](function (){
					
						var index=This[FD]('.t5')[ID]($(this));
				        var idNUM=$[TM]($(this)[PT]()[FD]('.t0')[TX]());	//取得每行id

				        var delTemp = $[FO](delete_power,idNUM);
				        $(this)[AP](delTemp);								//追加权限按钮图片
				         	   
						//删除
		    			$("#approveSetting_del"+idNUM,$key)[EV](CK,function(){

		    				$[MB]({
									content: '您确定要删除吗？',
									type: 4,
									onAffirm: function (state) { 
										if(state == true){
											$(BY)[AJ]({
							    				param:{'action':'sysManager.DelApproves',gid:idNUM},
							    				success:function(data){
							    					ApproveTableDataBind();
							    				}
							    			});
										}
									}
							});
		    			});
			         });  
				}
    		}
    	});
    }
     
	//添加or编辑
    function addBtnBind(id,title){
    		$[WW]({
    			css: { "width": "500px", "height": "auto" },
    			title: title,
    			content:addAlertDiv,
    			onLoad:function(div,close){
    				var saveBtn = $(".in_MsetCok",div),cancelBtn = $(".in_MsetCno",div),
    				approve_usersel = $("#approve_usersel",div),approve_shr = $("#approve_shr",div),
    				check_ui = $("#check_ui",div),_isnot = $("#isnot",div),check_shr = $("#check_shr",div),
    				check_account = $("#check_account",div),test = 0,flag="";
	
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

					if(id != 0){
    					$(div)[AJ]({
    							param:{action:'sysManager.UpdateApproves',id:id,method: 2},
    							success:function(data){
			    					test = data.id;
			    					if(data.isCheck == true){
			    						_isnot[RC]("push_type2"); 				//删除样式
			          					_isnot[AC]("push_type1");				//添加样式
			    						check_shr.show();
			    					}
			    					ApproveData_user(approve_usersel,data.userid);
									ApproveData_shr(approve_shr,data.checkid);
									ApproveData_ui(check_ui,data.uids);
    							}
    					});
    				}else{
						ApproveData_user(approve_usersel,id);
						ApproveData_shr(approve_shr,id);
						ApproveData_ui(check_ui,id);
    				}
					
    				saveBtn[EV](CK,function(){//保存
    				
    					check_account.html('');
     					var _usersel = approve_usersel.val();
						div[AJ]({
								param: {action: 'sysManager.InsertApproves',_usersel: _usersel,mthod:2},
								success: function(data){//????此处有问题 待解决
									if(data == true){
										check_account.show();
										check_account[AP]("* 该用户已添加，请选择其他用户");
									}else{
    									var _ui = "",i=0;	
								        $("#check_ui input")[EH](function(){
						    				if($(this)[0].checked){
						    					i++;
						    					if(i==1){
						    						_ui = $(this).attr("value");
						    					}else{
						    						_ui +=","+$(this).attr("value");
						    					}
											    check_account.hide();
											 }
						    			});
						    			if(i == 0){
						    				check_account[AP]("* 请选择授权帐号");
						    			}else{
						    				var _shr = "";
					    					if(_isnot[AT]("class") == "push_type com_fLeft check_max push_type1"){
					    						flag = 0;
					    						_shr = approve_shr.val();
					    					}else{
					    						flag = 1;
					    						_shr = 0;
					    					}
					    					
					    					if(test == 0){
					    						div[AJ]({
					    							param:{action:'sysManager.InsertApproves',_ui:_ui,_usersel:_usersel,_shr:_shr,flag:flag,mthod:1},
					    							success:function(data){
					    								ApproveTableDataBind();
					    								close();
					    							}
					    						});
					    					}else{
					    						div[AJ]({
					    							param:{action:'sysManager.UpdateApproves',id:id,_ui:_ui,_usersel:_usersel,_shr:_shr,flag:flag,method: 1},
					    							success:function(data){
					    								ApproveTableDataBind();
					    								close();
					    							}
					    						});
					    					}
					    					
						    			}
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
    
    function ApproveData_user(sel,selectid){//匹配用户
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
    		success:function(data){
    			var html_1 ='<li><input type="checkbox" id="check_num_{0}" value="{1}" /><label for={1} class="check_Lcount mynum_type1">{2}</label></li>';
    			var html_2 ='<li><input type="checkbox" id="check_num_{0}" value="{1}" /><label for={1} class="check_Lcount mynum_type2">{2}</label></li>';
    			var html_5 ='<li><input type="checkbox" id="check_num_{0}" value="{1}" /><label for={1} class="check_Lcount mynum_type5">{2}</label></li>';

				
    			$.each(data,function(i,item){
    				
    				if(item.type == 1){
    					sel[AP]($[FO](html_1,item.id,item.uid,item.name));
    				}else if(item.type == 2){
    					sel[AP]($[FO](html_2,item.id,item.uid,item.name));
    				}else{
    					sel[AP]($[FO](html_5,item.id,item.uid,item.name));
    				}
    				for (var i = 0; i < uids.length; i++){
    					if(item.uid == uids[i]){
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
            $('<div />').load('modules/sysManager/sysManager.approveSetting.htm', function () {
            	base = context.Base;
            	cuser=context.CurrentUser;
            	contentDiv[AP](base.FormatPmi($(this).html()));
            	navigationBars = context.NavigationBars;
            	
            	t.IsLoad = TE;
                $key = $('#' + key);
                
                addBtn = $("#approveSetting_addBtn",$key);
                addAlertDiv = '<div class="giveMe_do add_check"><div class="box_count" ><h4>账号授权</h4><div class="div_style"><label class="com_fLeft">用&nbsp;&nbsp;户：</label> <select name="T_place" class="task_count task_Cwdith1 pt_select" id="approve_usersel" ></select></div><div  style="margin-left: 50px;margin-top: 10px; float: left; color: red; font-size: 12px; display: block;"><span class="com_none" id="check_account"></span></div> <div class="com_clear"></div><div class="">请选择授权账号（选择账号,对这些账号进行授权！）</div> <div class="com_clear"></div><div class="div_style"><ul class="check_list" id="check_ui"></ul></div><div class="com_clear"></div><h4 style="margin-top:10px;">审核管理</h4><div class="div_style"><label class="com_fLeft">是否审核：</label> <span class="push_type com_fLeft check_max push_type2" id="isnot"></span></div><div class="com_clear"></div><div class="div_style com_none" id="check_shr"><label class="com_fLeft">审核人：</label><select name="T_place" class="task_count task_Cwdith1 pt_select" id="approve_shr"></select></div></div><div class="com_clear"></div><div class="push_ok"><div class="in_MsetCno">取&nbsp;&nbsp;消</div><div class="in_MsetCok">确&nbsp;&nbsp;定</div></div><div class="com_clear"></div></div>';
                
                ApproveTable = $("#SysManager_approveTable",$key);
                ApproveInfo = {id:-1};
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