//账号绑定
define(['portal'], function (context) {
    //定义变量
    var key = 'accountBinding',
          $key, //当前的作用域
          contentDiv,
          contentTable,
          alertDiv,
          addBtn,
          autoAlertDiv;
          
           
    //私有方法
    //初始化方法，写自己模块的业务
    function Init(isflag) {
    	LoadTable(contentTable,GetParam(),isflag);
    	NavigationInit();
    }
     //导航条初始化方法,
	function NavigationInit () {
		navigationBars.Empty()
			.Add(key, 'NavToMP', '系统管理')
			.Add(key, 'NavToMP_zhbd', '帐号绑定')[oL]();
	} 
   
    function GetParam(){
    	var param ={action:'sysManager.GetAccount'};
    	return param;
    }     
         
    function LoadTable(table,param,isflag) {
    	var pager = $('#allPager', $key);
    	table.add(pager).html('');
    	table[TB]({
    		 width: 1000,
             trHeight: 24, 	  //表格默认高
             pager: pager,
             pageSize: 15, 	  //显示条数
             isPager: TE, 	  //是否分页
             isCheckBox: FE,  //是否有多选按钮
             id:'_m1',
             rows: 'rows',
             param:param,
    		 width:1000,
    		columns:[{width:'5%',caption:'序号',field:'id',type:'number'},
    		         {width:'15%',caption:'微博',field:'wbType'},
    		        // {width:'20%',caption:'微博登录账号',field:'loginName'},
    		         {width:'30%',caption:'微博名称',field:'nickName'},
    		         {width:'15%',caption:'微博状态',field:'twitterState'},
    		         {width:'15%',caption:'剩余天数',field:'liveDay'},
    		         {width:'', caption: '操作', field: '', type: 'html', template: ''}],

    		onComplete: function (This, refresh, data) { 

    			var power = cuser.Templates['zhbd_insert_1'];			//取得添加权限
    			var delete_power = cuser.Templates['zhbd_delete_1'];	//取得删除权限
				var upadte_power = cuser.Templates['zhbd_update_1'];	//取得延长权限
    			//判添加权限
				if( power != UN && power != ""){
					if(!isflag){
	    				addBtn[EV](CK,function(){
							$(this)[AJ]({
							    param:{action:'sysManager.AddAccount',method:1},
							    success:function(data){
							    	// -1：到达添加次数，限制提示，0：插入失败，数据库错误提示，1：添加成功
									if(data == -1){
										$[MB]({ content: '添加失败,添加已到达上限!', type: 1,isAutoClose: TE });
									}else{
										ShowAlertDIV();//添加帐号绑定
									}
							    }
							});
						});
					}else{
						ShowAlertDIV();//添加帐号绑定
					}
				}
				//判断延长授权权限
				if( upadte_power != UN && upadte_power != ""){
					This[FD]('.t5')[EH](function (){
							var index=This[FD]('.t5')[ID]($(this));
				            var id = data.rows[index].id;	//取得每行id
				            var daynum=$[TM]($(this)[PT]()[FD]('.t4')[TX]());//取得每行剩余天数

				         	var upTemp = $[FO](upadte_power,id);
				         	$(this)[AP](upTemp);	

				         	if(daynum == '0'){
				         		$("#extend_"+id).html("授权");
				         	}else if(daynum == '永久'){
				         		$("#extend_"+id).html("");
				         	}
				         	//延长授权
							$('#extend_'+id,$key)[EV](CK,function(){
			    				ExtendShowAuto(id,data.rows[index].uid,data.rows[index].wbType);
							});
					});
				}		
				//判断删除权限
				if( delete_power != UN && delete_power != ""){
					This[FD]('.t5')[EH](function (){
							var index=This[FD]('.t5')[ID]($(this));
				            var id = data.rows[index].id;	//取得每行id
				            var wbType = data.rows[index].wbType;
				            var wbname = data.rows[index].nickName;
				         	var delTemp = $[FO](delete_power,id);
				         	$(this)[AP](delTemp);	
						
							//删除
							$('#delte_'+id,$key)[EV](CK,function(){
									$[MB]({
										  	content: '您确定要删除吗？',
											type: 4,
											onAffirm: function (state) { 
												if(state == true){
													$(BY)[AJ]({
															param: {action: 'sysManager.DelAccount', id: id,wbname:wbname},
															success: function(data1){
																base.DeleteAcount(data.rows[index].uid);
																LoadTable(contentTable,GetParam());
															}
													});
												}
											}
									});
							});//删除结束
					});//追加结束
				}//判断结束
    		}//加载成功结束
    	});
    }
    
  
    
    function ExtendShowAuto(id,uid,wbType){//延长授权
    		//先通过id获取对象
    		$(BY)[AJ]({
	     				param:{action:'sysManager.CommonUid'},
	     				success:function(data){
	     					
							if(wbType == "新浪微博"){
								$[WW]({
		     						css:{"width":"800px","height":"auto"},
		     						title:'授权',
		     						id:'sys_twitter1',
		     						content: autoAlertDiv,
		     						onLoad:function(div2, close){
		     							var sinaurl = 'https://api.weibo.com/oauth2/authorize?forcelogin=true&client_id={0}&redirect_uri={1}&response_type=code&state={2}',
					      				state = '1,'+data.id+','+uid+','+'0,0';
					     				var href = $[FO](sinaurl,$app.AppKey,$app.CallbackUrl,state);
					     				$('#iframeid',div2)[AT]('src',href);

				     					var okbtn = $(".in_MsetCok",div2),cancelbtn = $(".in_MsetCno",div2);
						    			//保存
						    			okbtn[EV](CK,function(){
						    				GetMethod();
						    				CloseWindow(div2);
							    			LoadTable(contentTable,GetParam());
						    			});
						    			//取消
						    			cancelbtn[EV](CK,function(){
						    				GetMethod();
						    				CloseWindow(div2);
							    			LoadTable(contentTable,GetParam());
						    				close();
						    			});
		     						}
		     					});
							}else if(wbType == "腾讯微博"){
								
							}else if(wbType == "人民微博"){
								
							}else{
								return false;
							}
	     				}
	     	})

    	return false;
    }
    
    function ShowAlertDIV(){//添加帐号
    	var demp = null;
    		if(demp != null){
    			demp[FD]('.content').html('');
    		}
    		$[WW]({
    			css: { "width": "500px", "height": "auto" },
     			title: '选择微博类型',
     			id: 'sys_twitter',
     			content: alertDiv,
     			onLoad: function(div, close){
     				demp = div;
	     			demp[AJ]({
	     				param:{action:'sysManager.CommonUid'},
	     				success:function(data){
	     					var sina = $(".tBiao_back1",div),tencent = $(".tBiao_back2",div),people = $(".tBiao_back3",div);

		     				// 选择帐号的平台|选择绑定的用户ID
		     				sina[EV](CK,function(){//新浪
		     					close();//关闭窗体
		     					$[WW]({
		     						css:{"width":"800px","height":"auto"},
		     						title:'帐号授权',
		     						id:'sys_twitter1',
		     						content: autoAlertDiv,
		     						onLoad:function(div2, close2){
		     							var sinaurl = 'https://api.weibo.com/oauth2/authorize?forcelogin=true&client_id={0}&redirect_uri={1}&response_type=code&state={2}',
					      				state = '1,'+data.id+','+'add,0,0';
					     				var href = $[FO](sinaurl,$app.AppKey,$app.CallbackUrl,state);
					     				
					     				$('#iframeid',div2)[AT]('src',href);
		     							
				     					var okbtn = $(".in_MsetCok",div2),cancelbtn = $(".in_MsetCno",div2);
						    			//保存
						    			okbtn[EV](CK,function(){
						    				GetMethod();
						    				CloseWindow(div2);
							    			LoadTable(contentTable,GetParam());
						    			});
						    			//取消
						    			cancelbtn[EV](CK,function(){
						    			    GetMethod();
						    				CloseWindow(div2);
							    			LoadTable(contentTable,GetParam());
						    				close2();
						    			});
		     						}
		     					});
		     				});

		     				tencent[EV](CK,function(){//腾讯				
		     				
					     		close();//关闭窗体
		     					$[WW]({
		     						css:{"width":"800px","height":"auto"},
		     						title:'帐号授权',
		     						id:'sys_twitter1',
		     						content: autoAlertDiv,
		     						onLoad:function(div3, close3){
		     							$('#iframeid',div3)[AT]('src','');
		     							var sinaurl = 'https://open.t.qq.com/cgi-bin/oauth2/authorize?client_id={0}&redirect_uri={1}&response_type=code&state={2}',
					     				state = "2,"+data.id+','+'add,0,0';
					     				var key = '801547839';
					     				var url = 'http://localhost:8080/twitter1.0/auth.action';
					     				var href = $[FO](sinaurl,key,url,state);//$tapp.AppKey,$tapp.CallbackUrl
		     							
		     							$('#iframeid',div3)[AT]('src',href);
		     								
				     					var okbtn = $(".in_MsetCok",div3),cancelbtn = $(".in_MsetCno",div3);
						    			//保存
						    			okbtn[EV](CK,function(){
						    				GetMethod();
						    				CloseWindow(div3);
							    			LoadTable(contentTable,GetParam());
						    			});
						    			//取消
						    			cancelbtn[EV](CK,function(){
						    			    GetMethod();
						    				CloseWindow(div3);
							    			LoadTable(contentTable,GetParam());
						    				close3();
						    			});
		     						}
		     					});
		     				});
		     				
		     				people[EV](CK,function(){//人民		
		     						
		     					close();//关闭窗体
		     					$[WW]({
		     						css:{"width":"800px","height":"auto"},
		     						title:'帐号授权',
		     						id:'sys_twitter1',
		     						content: peopleDiv,
		     						onLoad:function(div4, close4){
		     							var okbtn = $(".in_MsetCok",div4),cancelbtn = $(".in_MsetCno",div4),
		     								quxiao = $(".quxiao_botton",div4),tijiao = $(".tijiao_botton",div4),
		     								checkon=$("#checkon",div4),uname=$("#uname",div4),pass=$("#pass",div4),
		     								showdiv = $("#showdiv",div4),showyc = $("#showyc",div4),showyc=$("#showyc",div4);
										uname.val('');
										pass.val('');
										showyc[AC]("com_none");
										showdiv[RC]("com_none");
										
		     							//授权
						    			tijiao[EV](CK,function(){
						    				checkon.html('');
						    				var name = uname.val(),passw = pass.val();
						    				if(name == ""){
						    					checkon[RC]("com_none");
						    					checkon.html(" *请输入用户名!");
						    				}else if(passw == ""){
						    					checkon[RC]("com_none");
						    					checkon.html(" *请输入密码!");
						    				}else{
						    					var state = '5,'+data.customID+','+name+','+passw+','+data.id;
						    					$(this)[AJ]({
						    						servletName: 'auth.action',
													param:{state:state},
													success:function(data){
														showyc[RC]("com_none");
														showdiv[AC]("com_none");
														if(data == 0){
															$('#iframeyc',div4)[AT]('src',basePath+'back-warn.html');
														}else if(data == 1){
															$('#iframeyc',div4)[AT]('src',basePath+'back-repeat.html');
														}else if(data == 2){
															$('#iframeyc',div4)[AT]('src',basePath+'back-back.html');
														}
													}
												});
												
												
						    				}
						    				
						    			});
		     							
		     							//取消
						    			quxiao[EV](CK,function(){
						    				showyc[RC]("com_none");
											showdiv[AC]("com_none");
						    				$('#iframeyc',div4)[AT]('src',basePath+'back-cancel.html');
						    			}); 
		     							
		     							
		     							//保存
						    			okbtn[EV](CK,function(){
						    				GetMethod();
						    				CloseWindow(div4);
							    			LoadTable(contentTable,GetParam());
						    			});
		     							
		     							//取消
						    			cancelbtn[EV](CK,function(){
						    			    GetMethod();
						    				CloseWindow(div4);
							    			LoadTable(contentTable,GetParam());
						    				close4();
						    			});  
		     						}
		     					});
		     				});
	     				}
	     			})
     				
     			}
    		});
    }
  
  
    //关闭窗体事件
	function CloseWindow(div) {
	   div.hide();
	   $[RM]({ obj: '#' + JLY });
	}

	//获取帐号集合
	function GetMethod(){
		$(BY)[AJ]({
				   param: {action: 'sysManager.GetMethodAccount'},
				   success: function(data){//alert($.toJson(data));
				   var obj = new Array();
				   if(data != null){
					   for(var i=0;i<data.length;i++){
					   		obj.push({id: data[i].id, "uid": data[i].uid, "name": data[i].name, "type": data[i].type,isExpire: data[i].flag});
					   }
					   base.AddAcount(obj);//添加
				   }
				}
		});
	}

    return {
    	IsLoad: FE,
        OnLoad: function (_ContentDiv,isflag) {
            var t = this;
            contentDiv = _ContentDiv;
            $('<div />').load('modules/sysManager/sysManager.accountBinding.htm', function () {
            	base = context.Base;
            	cuser=context.CurrentUser;
            	contentDiv[AP](base.FormatPmi($(this).html()));
            	navigationBars = context.NavigationBars;
            	
            	t.IsLoad = TE;
                $key = $('#' + key);
                contentTable = $("#sys_account_table");
                autoAlertDiv = '<div class=""><div class=""><iframe name="20" id="iframeid" src="" width="795" height="400" marginheight="0" marginwidth="0" scrolling="no" frameborder="no" align="middle"></iframe></div><div class="push_ok"><div class="in_MsetCno">关&nbsp;&nbsp;闭</div><div class="in_MsetCok">确&nbsp;&nbsp;定</div></div><div class="com_clear"></div></div>';
                peopleDiv = '<div class=""><div class=""><div id="showyc" class="com_none"><iframe name="20" id="iframeyc" src="" width="795" height="400" marginheight="0" marginwidth="0" scrolling="no" frameborder="no" align="middle"></iframe></div><div class="com_none" id="showdiv"> <div id="back_border"></div><div id="back-back"><div  id="people_logo"></div><div id="back_box"><h1 class="h1_style">授权<span class="pingtai_name">讯库_云微方</span>&nbsp;&nbsp;访问你的微博帐号，并同时登录微博(密码会被记录在云微方中)</h1><div class="login_autho"><div class="div_style"><label class="label_style">用户名：</label><input type="text" id="uname" value="" class="input_style" /></div><div class="com_clear"></div><div class="div_style"><label class="label_style">密&nbsp;&nbsp;&nbsp;码：</label><input type="password" id="pass" value="" class="input_style" /></div><div class="com_clear"></div><div class="div_style sqEror_tip com_none" id="checkon"></div></div></div><div id="squan_tijiao"><div  class="botton_style quxiao_botton">取&nbsp;&nbsp;消</div><div  class="botton_style tijiao_botton">登&nbsp;&nbsp;录</div></div></div></div></div><div class="push_ok"><div class="in_MsetCno">关&nbsp;&nbsp;闭</div><div class="in_MsetCok">确&nbsp;&nbsp;定</div></div><div class="com_clear"></div></div>';
                //alertDiv ='<div class="giveMe_do bind_zhao"><ul class="bind_tbiao"><li class="tBiao_list tBiao_back1"></li></ul><div class="com_clear"></div></div>';
               	addBtn = $('.event_add',$key);
               	alertDiv ='<div class="giveMe_do bind_zhao"><ul class="bind_tbiao"><li class="tBiao_list tBiao_back1"></li><li class="tBiao_list tBiao_back2"></li><li class="tBiao_list tBiao_back3"></li></ul><div class="com_clear"></div></div>';
               	Init(isflag);
            });
            return t;
        },
        OnLoad2:function(_ContentDiv,isflag){
        	this.OnLoad(_ContentDiv,isflag);
        },
        //初始化方法
        NavToMP: function () {
            Init(false);
        },
        //显示模块方法
        Show:function() {
        	$key.show();
        	Init(false);
        }
    };
});