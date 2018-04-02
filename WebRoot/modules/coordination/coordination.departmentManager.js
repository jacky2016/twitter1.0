//机构管理
define(['portal'], function (context) {
    //定义变量
    var key = 'departmentManager',
          $key, //当前的作用域
          navigationBars,
          tables,
          cuser,
          base,
          stime,
          etime,
          _enum,
          departManageSelect,// 网评员管理的下拉框对象
          selectColl=[],//从后台取的数据赋值下拉框的数的数组
          departManagerTitlebar, //网评员管理上面的h1标签切换页的对象
          currSendType,//  当前点击的是评论我的 or 转发我的
          RemberOrgUid,//记录当前的 这条的 网评员管理的uid
          contentDiv;
          
    //私有方法
    //初始化方法，写自己模块的业务
    function Init() {
    	NavigationInit();
    	 SetOptions();
    }
    
    //功能按钮 三个操作
    function  Execution(value){
	    $('#searchBtn',$key)[EV](CK,function(){
	    	//判断时间起始不能大于结束时间
	    var tt	= base.IsRegExpDateTime(stime,etime,TE);
	    	  if(tt==TE){
					 OnLoadmonitorList(value);
	    	  }
	    });
	    
	    $('#addBtn',$key)[EV](CK,function(){
	    		if(value==1){
	    			value=0;
	    		}
	   			RaiseDialogue(value);
	    });
	    
	        $('#exportBtn',$key)[EV](CK,function(){
	    alert("导出页面=========="+value);
	    
	    });
    }
    
    //添加弹出层
    function   RaiseDialogue(value){
    var bodyHTML='<div class="giveMe_do new_tongzhi sel_pingtai"><div class="box_count"><div class="div_style"><label class="com_fLeft">平台选择：</label><select class="task_count pt_select"><option value="0">新浪微博</option><!--<option  value="1">腾讯微博</option><option value="2">人民微博</option> --></select><input type="text"  class="task_count addZhao" id="departmentMan_weiboAccount"/></div></div>'+
    '<div style="margin-left: 160px; float: left; color: red; font-size: 12px;" id="departManager_accountName"><span class="ev_wCount"></span></div><div class="com_clear"></div>'+
    '<div class="push_ok"><div class="in_MsetCno" id="department_cancel">取&nbsp;&nbsp;消</div><div class="in_MsetCok" id="department_ok">确&nbsp;&nbsp;定</div></div><div class="com_clear"></div></div>';
    		$[WW]({
		        css: { "width": "480px", "height": "auto"},
		       // event: 'no',
		        title: '添加帐号',
		        content: bodyHTML,
		        id: 'departmentManager_RaiseID',
		        onLoad: function(div) {
		        var  se=$('.pt_select',div);
		        //var  value=$('.zhao_change.span_title.zhao_back',$key)[AT]('type');
		        	if(value==0){
		        	se.val(0);
		        	}else if(value==1){
		        	se.val(1);
		        	}else if(value==2) {
		        	se.val(2);
		        	}
		           DialogueOptions(div,value);
		      		},//onLoad结束
		      		
		      	   onClose: function(){
				 	 	$("#departManager_id0").hide();
				 	 }
		      		
    			});
    }

    //添加弹出层对话框 确定 or 取消操作
    function  DialogueOptions(div,value){
     	$('#departManager_accountName',div)[FD]('.ev_wCount')[TX]('');
    	$('#departManager_accountName',div).hide();
    	
    		$('.task_count.pt_select',div)[CE](function (){
    		     $('#departManager_accountName',div)[FD]('.ev_wCount')[TX]('');
    			$('#departManager_accountName',div).hide();
    		});
    	
    		//输入框有搜索功能
    	var inp=$('#departmentMan_weiboAccount',div);
    		inp.blur(function(){
					if($[TM](inp.val())==""){
						inp.val("请输入微博账号");
					}
					 //点击旁边处，来隐藏  id为departManager_id0的div  
					    $('#departManager_id0').hide();		
    		});

     	inp.focus(function (){
     	$('#departManager_accountName',div)[FD]('.ev_wCount')[TX]('');
    	$('#departManager_accountName',div).hide();
		if($[TM](inp.val())=="请输入微博账号"){
		   inp.val('');
		}     	
     });
     
   	/*
   			 inp.patch({
   			 				id:'departManager_id',
					        width: 207, //控件宽度
					        keyword: 'keyword', //保存关键字的属性name
				             columns: [{ field: 'id' },
						                      { field: 'departmentName' },
						                      { field: 'departmentName'}],
					        param: {action :'coordination.departmentManagerAction',queryConditions: 'keyUpSearch', pt_select: value}
				            
					    });
*/


    $('#department_ok')[EV](CK,function(){
			//不能输入特殊字符
			var regu = /^[0-9a-zA-Z_\-\u4e00-\u9fa5]+$/;      
   			if ((!regu.test( inp.val() ))||(inp.val()=="请输入微博账号")) {
   					    // alert("输入的格式不能为空或者包含特殊字符"+"========"+value);
    					$('#departManager_accountName',div)[FD]('.ev_wCount')[TX]('输入的格式不能为空或者包含特殊字符');
    					$('#departManager_accountName',div).show();
    						return  ;
    		}
    		//确定最后发送哪个平台的微博
    	    var lastVal=$('.pt_select',div).val();
    		//检查机构管理是否重命名
		    $(BY)[AJ]({
    		param: {action: 'coordination.departmentManagerAction.add',queryConditions:'checkOragnName',name: inp.val(),platform:lastVal},
    		success: function (data) {
    			  if(data==""){
    			  //不重复可以进行添加
    			 //   确定向后台数据库发送
    			 /*
    		$(BY)[AJ]({
		    		param: {action: 'coordination.departmentManagerAction.add',queryConditions:'addOragnName',platform:lastVal,name: inp.val()},
		    		success: function (data) {
		    		    if(data!="添加成功"){
		    		    	$('#departManager_accountName',div)[FD]('.ev_wCount')[TX](data);
    			            $('#departManager_accountName',div).show();
		    				   return;
		    		    }
					    CloseWindows(div);
		    			OnLoadmonitorList(value);
		    		}
		    	});
		    	*/
		    	    CloseWindows(div);
		    		OnLoadmonitorList(value);
    			  }else{
    			      //重复的报错
    			      $('#departManager_accountName',div)[FD]('.ev_wCount')[TX](data);
    			      $('#departManager_accountName',div).show();
    			      return ;
    			  }
    		}//success 
    	});
    		
    });//确定按钮结束
    
    $('#department_cancel')[EV](CK,function(){
    	  CloseWindows(div);
    });
    }
    
    //关闭弹出层
    function  CloseWindows(div){
    div[RM]();
    $[RM]({obj:'#'+JLY});
    }
    
     //导航条初始化方法,
	function NavigationInit () {
				       	navigationBars.Empty()
			.Add(key, 'ToolBar', '协同办公')
    		.Add(key, 'ToolBar', '网评员管理')[oL]();
	}
	
	//内部导航条显示
		function NavigationIntoInit () {
				       	navigationBars.Empty()
			.Add(key, 'ToolBar', '协同办公')
			.Add(key, 'ToolBar', '网评员管理')
    		.Add(key, 'IntoBar', '详细列表')[oL]();
	}
	
	
	//设置
	 function  SetOptions(){
	 departManagerTitlebar.show();
	 $('.zhao_change',$key)[EV](CK,function (){
	 	   $('.zhao_change')[RC]('zhao_back');
	 	   $(this)[AC]('zhao_back');
	 	   //点击上面的选择不同平台的微博 加载列表
	 	  //kindsType  新浪0,人民2,还是腾讯1   微博类型
	 	  var t=-1;
	 	   if(($(this)[AT]('type'))==0){
	 	   t=0;
	 	   } else  if(($(this)[AT]('type'))==1){
	 	   t=1;
	 	   }else {
	 	     t=2;
	 	   }
		  
		   etime.datePicker();
		   var  tempStartTimes=$.addDate('d', -6, etime.val()).Format('YYYY-MM-DD');
		   stime.datePicker().val(tempStartTimes);
		   // 加载下拉框里面的数据，然后刷新列表		   
		    LoadingDrowDown(t);
	    	   
	 }).eq(0)[TR](CK);
	 }
	 
	 // 加载下拉框里面的数据
	 function  DrawDownSelect(t){
	 		departManageSelect.input({
	     		id:"departManageSelect_list",
	     		collection:selectColl
	     	});
	  	   OnLoadmonitorList(t);
    	   Execution(t);
	 }  // DrawDownSelect
	 
	 
	 
	 //读取下拉框的数据
	function  LoadingDrowDown(t){
		selectColl = [];
	    $(BY)[AJ]({
    	param: {action :'coordination.departmentManagerAction', queryConditions:'dropdownBoxData'},
    		success: function (data) {
    		     $[EH](data ,function (i,item){
						    var a={text:'',value:'',platform:''};
    		     	      a.value =item.uid;
    		     	      a.platform=item.type;
    		     	      a.value=item.uid+','+item.type;
    		     	      if(item.name.length>7){
    		     	      a.text=item.name.substr(0,7);
    		     	      }else{
    		     	      a.text=item.name;
    		     	      }
    		     	      selectColl.push(a);
    		     });
    		     DrawDownSelect(t);
    		}//succ结束
    	});
	}
	 
	 
	 
	 
	 //多选框设置
	 function  AllcheckBoxOptions( This, refresh,data){
	 	   $('.selectAll',This)[EV](CK,function(){
	 	     if(this[CD]){
			 	     $('.select',This)[EH](function(){
			 	     this[CD]=true;
	 	     		});
	 	     }
	 	     else if(!this[CD]){
			 	    $('.select',This)[EH](function(){
			 	     this[CD]=false;
	 	    		 });
	 	     }
	 	   });
	 }
	 
	 	//多选框其中一个情况
			function  CheckBoxOptions( This, refresh,data){
				     $('.select',This)[EV](CK,function(){
				     	  if(this[CD]){
				     	     var tt=0;
				     	     $('.select',This)[EH](function(){
				     	     	if(this[CD]){
				     	     	tt+=1;
				     	     	}
				     	     });
				     	     if(tt==$('.select',This).length){
				     	       $('.selectAll',This)[EH](function(){
				     	       this[CD]=true;
				     	       });
				     	     }
				     	  }else{
				     	     $('.select',This)[EH](function(){
				     	     	if(!this[CD]){
				     	       $('.selectAll',This)[EH](function(){
				     	       this[CD]=false;
				     	       });
				     	     	}
				     	     });
				     	  }//结束
				     });
			}
	 
	 
	 //加载数据  kindsType--- h1的切换页签的的值 0 新浪 
	 function  OnLoadmonitorList(kindsType){
		 	 var pager=$('#allPager',$key);
		 	 var startTime=stime.val(),
		 	    	endTime=etime.val();
		 	 tables.add(pager).html('');
		 	 var uid=departManageSelect[AT]('value').split(',')[0];
		 	 if(kindsType==0) {   //新浪的 展示开始
		 	  tables.prev().show();
		 	 tables[TB]({
             width: 1000,
             trHeight: 24, //表格默认高
             pager: pager,
             pageSize: 10,//显示条数
             isPager: TE, //是否分页
             id:'_coord',
             rows: 'rows',
             param: {action :'coordination.departmentManagerAction', queryConditions:'queryAll',startTime:startTime ,endTime:endTime,KindType:kindsType,
              uid:uid},
			 columns: [{ caption: '序号', field: 'id', type: 'checkbox',width: '5%' },
			 			{ caption: '序号', field: 'id', type: 'string',width: '5%' },
                       { caption: '网评员昵称', field: 'departmentName', type: 'string', width: '45%' },
                       { caption: '转发我的', field: 'weiboNum', type: 'int', width: '15%' },
                       { caption: '评论我的', field: 'toMeNum', type: 'int', width: '15%' },
                       { caption: '机构管理的uid', field: 'orguid', type: 'string', width: '10%' }
                         // ,{ caption: '操作', field: '', type: 'html', width: '15%',template:'',className:'tdOption' }
                         ],
           				 //option: ['delete'],
                //加载表格成功事件
            onComplete: function ( This, refresh,data) {
            
            		if(data.rows[LN]>0){
					 		//判断是否有添加、导出、删除的权限
					 		var curserDelTemp= cuser.Templates['xtbg_jggldel_1'];
					 		//有删除权限
						    if(curserDelTemp!=UN&&curserDelTemp!=""){
						       This[FD]('tr').eq(1)[AP]('<th width="15%">操作</th>');
						       This[FD]('.t4')[EH](function (){
						       	var idNum=$[TM]($(this)[PT]()[FD]('.t1')[TX]());
						       	var DelTemp=$[FO](curserDelTemp,idNum);
						        $(this).after(DelTemp);
						       });
						    }
						    //无权限
						    else {
						    
						    }//无权限结束
							
							//序号列隐藏
							//机构管理(也就是网评员昵称的uid) 隐藏
							This[FD]('tr')[EH](function (i){
							  if(i==1){
							  	$(this)[FD]('th').eq(1).hide();
							  	$(this)[FD]('th').eq(5).hide();
							  }else if(i>1){
							  	$(this)[FD]('.t1').hide();
								$(this)[FD]('.t5').hide();
							  }
							});
							
							
							
							var flagImport=$('#exportBtn',$key);
							if(flagImport.length!=0){
								//多选框全选等情况
								AllcheckBoxOptions( This, refresh,data);
								//多选框其中一个情况
								CheckBoxOptions( This, refresh,data);
							}else{
								$('.selectAll',This)[EH](function (){
									$(this)[PT]()[RM]();
								});
								$('.select',This)[EH](function (){
									$(this)[PT]()[RM]();
								});
							}
							
						   //画出删除按钮以及功能
						DelBtnOptions( This, refresh,data,kindsType);
						   //转发我的变手指
						This[FD]('.t3')[EH](function (){
					  	        //内容转义字符
							 	var index= $('.t3',This)[ID]($(this));
							 	var t3TXT=data.rows[index].weiboNum;
					  	        var last='<a style="cursor:pointer"> '+'&nbsp;'+t3TXT+'</a>';
					  	        $(this).html(last);
					  	});
						   //评论我的变手指
						   			This[FD]('.t4')[EH](function (){
					  	        //内容转义字符
							 	var index= $('.t4',This)[ID]($(this));
							 	var t3TXT=data.rows[index].toMeNum;
					  	        var last='<a style="cursor:pointer"> '+'&nbsp;'+t3TXT+'</a>';
					  	        $(this).html(last);
					  	});
					  	//手指点进去的事件
					  		ClickIntoHandle(This, refresh,data);
						   }  //有数据结束
						else  if(data.rows.length==0){
							base.not(tables);
						}
            }
	    	});  //table 结束
    	}  //新浪的 展示结束
    	else{
    		  tables.prev().hide();
    		  tables[AP]('<div class="no_data"><div class="com_fLeft nodata_tip"></div><span class="com_fLeft nodata_tipLan">对不起，暂时没有开放！</span></div>');
    	}  //  腾讯的 提示未开放
 	 }
 	 
 	 
 	 		  	//手指点进去的事件
			function 	 ClickIntoHandle(This, refresh,data){
					    //转发我的变手指 0
						This[FD]('.t3')[EH](function (){
							 var index= $('.t3',This)[ID]($(this));
							 		var  t3Num=$[TM]($(this)[FD]('a')),
							 			    orguid=$[TM]($(this)[PT]()[FD]('.t5')[TX]());
							  	$(this)[FD]('a')[EV](CK,function(){
							  		 	IntoListToMe(0,orguid);
							  	});
					  	});   
					  	
					
						//评论我的变手指  1
						This[FD]('.t4')[EH](function (){
							 	var index= $('.t4',This)[ID]($(this));
							 	var  t4Num=$[TM]($(this)[FD]('a')),
							 			orguid=$[TM]($(this)[PT]()[FD]('.t5')[TX]());
							    $(this)[FD]('a')[EV](CK,function(){
							  			 	IntoListToMe(1,orguid);
							  	});
					  	});
			
			
			}  //  ClickIntoHandle结束
 	 
 	 
 	 		//点击 转发我的 or 评论我的进入里面的列表
 	 		//styleType 0 转发我的  1 评论我的
 	 		//orguid 当前的(组织机构) 也就是网评员昵称的uid
 	 	function    IntoListToMe(styleType,orguid){
 	 		 RemberOrgUid=orguid;
 	 		 currSendType=styleType;
 	 		 departManagerTitlebar.hide();
 	 		 //上面的箭头ToolBar 栏
 	 		 NavigationIntoInit ();
 	 		 
 	 		 var pager=$('#allPager',$key);
		 	 var startTime=stime.val(),
		 	    	 endTime=etime.val();
		 	  		 tables.add(pager).html('');
		 			 tables.prev().hide();
					//-------------------
				var  templatePmis='';
				var sendParam={};
			      	  	//转发
	      	  	if(styleType==0){
	      	  	templatePmis=base.FormatPmi('{pmi:yqzs_collect_1}{pmi:wdsy_repost_1}{pmi:wdsy_comment_1}');
	      	  	sendParam={action :'coordination.departmentManagerAction', queryConditions:'departManerInto',startTime:startTime ,endTime:endTime,orguid:orguid,postType:0};
	      	  	}
	      	  	//评论
	   	  	else if(styleType==1){
				templatePmis=base.FormatPmi('{pmi:wdsy_comment_1}');
				sendParam={action :'coordination.departmentManagerAction', queryConditions:'departManerInto',startTime:startTime ,endTime:endTime,orguid:orguid,postType:1};
	      	  	}		
				
				base.DataBind({ 
				panel: tables, 
				pager: pager,
				id: 'departManage_into_list', 
			    param: sendParam,// {action :'coordination.examineManager',tempList:'commentatorsToMeNum',nid:nid,starttime:stime,endtime:etime,selectValueEC:selectValueEC},
				//template: '<span>转发</span><span>|</span><span>评论</span><span>|</span><span >收集</span>',
			    //template: base.FormatPmi('{pmi:yqzs_collect_1}<span class="repostCount">转发</span><span>|</span><span class="commentCount">评论</span>'),
			    //template: base.FormatPmi('{pmi:yqzs_collect_1}{pmi:wdwb_adwdrepost_1}{pmi:wdwb_adwdcomment_1}'),
			    //template: base.FormatPmi('{pmi:yqzs_collect_1}{pmi:wdsy_repost_1}{pmi:wdsy_comment_1}'),				
				template:templatePmis,
				isComment:FE,
				moduleType: _enum.ModuleType.talkMe,
				isRepostCommonCount:TE,
				onComplete: function (This, refresh, data) { 
					$('.com_fRight.subBtnPanel',This)[EH](function (){
						 $(this)[RM]();
					});
				}, // onComplete结束
				onSend:function(btn,txt, isChecked, accountCollection, postID, url, type, twitterID, closeBtn ,name){
					 //评论或者转发触发的事件
				var param = {};
			//	param.action = type == 'repost'? 'talkMe.talkMeRepost': 'talkMe.talkMeComment';
			   // param.queryConditions=type == 'repost'? 'talkMeRepost': 'talkMeComment';
				param.action = type == 'repost'? 'myTwitter.saveReport': 'myTwitter.saveComment';
				param.isChecked = isChecked;
				if (type == 'repost' ) {
							//不填转发内容的时候，默认值
							if ($[TM](txt) == '') {
							//if (txt == '') {
								txt = '转发微博';
							}
						} else {
							//if (txt == '') {
								if ($[TM](txt) == '') {
								$[MB]({
									content: '评论内容不能为空', 
									type: 2
								});
								btn[AT]('isclick',TE);
								return;
							}
						}				
				param.content = txt;
				var accountids = [], platforms = [],accountNames = [];
				$[EH](accountCollection, function(i, dataRow) {
					accountids.push(dataRow.uid);
					platforms.push(dataRow.type);
					accountNames.push(dataRow.name);
				});
				param.accountuids = accountids.join(',');
				param.platforms = platforms.join(',');
				param.twitterID = twitterID;
				param.sendNames = accountNames.join(',');
				//param.currentuserUID=currentUID;
				
				$(BY)[AJ]({
					param: param,
					dataType: 'text',
					success: function(data) {
						if(data) {
							$[MB]({content: '提交成功', type: 0,isAutoClose: TE});
							closeBtn[TR](CK); //关闭子页面
						} else {
							$[MB]({content: '提交失败', type: 2,isAutoClose: TE});
						}
						btn[AT]('isclick',TE);
					}
				});
				
				
				}//onSend 结束
			});
					
		//=+++++++++++			 	 		
 	 		}   //IntoListToMe 结束
 	 
 	 
	 		//kindsType 是什么平台类型 新浪 or 腾讯 or 人民
	 		function  DelBtnOptions( This, refresh,data,kindsType){
	 		
	 				This[FD]('.tdOption')[EH]( function (){
	 				var index=This[FD]('.tdOption')[ID]($(this));
	 				$(this).css('cursor','');
	 				//$(this)[AP]($('<div class="event_doBotton event_del" id="departmentMan_btn'+data.rows[index].id+'"></div>',$key));
	 				$('#departmentMan_btn'+data.rows[index].id,This)[EV](CK,function (){
	 				      
	 			 var 	html='<div class="giveMe_do del_reSee"><div class="giveMe_doDel" >您确定要删除吗？</div> <div class="push_ok"><div class="in_MsetCno" id="cancel">取&nbsp;&nbsp;消</div><div class="in_MsetCok" id="ok">确&nbsp;&nbsp;定</div></div><div class="com_clear"></div></div>';
				$[WW]({
		        css: { "width": "440px", "height": "auto"},
		        //event: 'no',
		        //title: '序号:'+data.rows[index].id+'     机构管理任务',
		        title: '&nbsp;&nbsp;网评员管理任务',
		        content: html,
		        id: '_departID'+data.rows[index].id,
		        onLoad: function(div) {
		        SettingOption(div, data.rows[index].id, 'departmentMan_btn'+data.rows[index].id,kindsType);
		      		}//onLoad结束
    					});
	 				      
	 				 	});//删除按钮结束
	 				});
		 }
		 
		 function  SettingOption(div ,id , btn,kindsType){
		 	//删除确定
		 	$('#ok',div)[EV](CK,function (){
		 	  $(BY)[AJ]({
   						param: {action: 'coordination.departmentManagerAction.del',queryConditions:'deleteID',deleteID:id, Platfom:kindsType},
    				     success: function (data) {
							//$('#'+btn,$key)[PT]()[PT]()[RM]();			
							 CloseWindows(div);
							 OnLoadmonitorList(kindsType);
    					   }		  		
					    		  });
		 	});
		 	
		 	//取消
		 	$('#cancel',div)[EV](CK,function (){
		 	 CloseWindows(div);
		 	});
		 }
	 
	
    return {
    	IsLoad: FE,
        OnLoad: function (_ContentDiv) {
            var t = this;
            contentDiv = _ContentDiv;
            $('<div />').load('modules/coordination/coordination.departmentManager.htm', function () {
				cuser=context.CurrentUser;
				base = context.Base;
				_enum = context.Enum;
				contentDiv[AP](base.FormatPmi($(this).html()));
            	t.IsLoad = TE;
            	navigationBars=context.NavigationBars;
                $key = $('#' + key);
		         tables=$('.depart_allShow',$key);
               	stime= $('#startTime',$key);
	 			etime=	$('#endTime',$key);
	 			departManagerTitlebar=$('#depart_manager_titlebar',$key);
	 			departManageSelect=$('#departManageSelect',$key);
                Init();
            });
            return t;
        },
        //显示模块方法
        Show:function() {
        	$key.show();
        	Init();
        },
        
                ToolBar:function(){
                Init();
        },
        //第三级的任务栏
            IntoBar:function (){
               IntoListToMe(currSendType,RemberOrgUid);
             }
        
    };
});