//预警列表
define(['portal'], function (context) {
    //定义变量
    var key = 'alertList',
          $key, //当前的作用域
          base,
          changeYeQian,
          warnservicesAll,
          warnservicesPager,
          cuser,
          yeQianTypeHOne=0,  //判断是哪一个上面的h1的页签
          contentDiv;
          
	         
	function Init() {
		NavigationInit();
		Setption();
    }
    
    function   Setption(){
    	changeYeQian[EV](CK,function (){
    		 changeYeQian[RC]('zhao_back');
			 $(this)[AC]('zhao_back');
			 var   YeQianType= $('.zhao_back',$key)[AT]('type'); 
			 OnLoadList(YeQianType);
    	}).eq(0).trigger(CK);
    }
    
    
    //工具栏显示        
    function NavigationInit() { // 自己定义方法
       	navigationBars.Empty()
		.Add(key, 'ToolBar', '预警服务')
		.Add(key, 'ToolBar', '预警列表')[oL]();
    }

		//type=0 为微博预警的列表
	 function  OnLoadList(type){
	      var  varStr="";
	 		if(parseInt(type)==0){
	 		  	varStr='warnservicesWeiboShowList';
	 		  	yeQianTypeHOne=0;
	 		}
	 		 warnservicesAll.add(warnservicesPager).html('');
			 warnservicesAll[TB]({
             width: 1000,
             trHeight: 24, //表格默认高
             pager: warnservicesPager,
             pageSize: 10,//显示条数
             isPager: TE, //是否分页
             id:'warnList_showAll',
             rows: 'rows',
             param: {action :'warnlist.warnserviceslist',queryConditions:varStr},
			 columns: [{ caption: '序号', field: 'id', type: 'number',width: '6%' },
			 			//{ caption: 'ID', field: 'id', type: 'int',width: '2px'},
                   		{ caption: '昵称', field: 'nickName', type: 'string',width: '12%' },
                   	    { caption: '内容', field: 'text', type: 'string' },
						{ caption: '当前转发', field: 'curRepost', type: 'int',width: '8%' },
						{ caption: '转发阀值', field: 'warnRepost', type: 'int',width: '8%' },
						{ caption: '当前评论', field: 'curComment', type: 'int',width: '8%' },
						{ caption: '评论阀值', field: 'warnComment', type: 'int',width: '8%' },
						{ caption: '截至日期', field: 'endTime', type: 'string',width: '10%' }],
                //加载表格成功事件
            onComplete: function ( This, refresh,data) {
  					   //无数据的情况下显示
					    if(data.rows.length==0){
							base.not(warnservicesAll);
						}
						//有数据的情况
						else{
						//如果有预警按钮权限才显示， 如果没有则不显示
						if(cuser.Templates['yjfw_yjxxinsert_1']!='' ||cuser.Templates['yjfw_yjxxinsert_2']!='' ){
						  var  tableCol=$('.web_table',This)[FD]('tr').eq(1);
						     tableCol[AP]('<th width="8%">操作</th>');
							$('.web_table',This)[FD]('tr')[EH](function  (i){
									if(i!=0 && i!=1){
									   $(this)[AP]('<td class="t8" style=""><div title="编辑" id="warnList_edit'+data.rows[i-2].id+'" class="event_doBotton event_edit"></div>  <div title="删除" id="warnList_del'+data.rows[i-2].id+'" class="event_doBotton event_del"></div></td>');
									}  // if 
							});   // EH  end
							//点击后面的操作里的两个按钮触发的事件 
							OperationAction(This, data);						
					}
						
						}  // else      有数据 end
            }   //onComplete end
		});  // table 方法结束
	 }   //   OnLoadList结束


		//点击后面的操作里的两个按钮触发的事件 
	function	OperationAction(This, data){
			//点击修改触发的事件
		  	$('.event_doBotton.event_edit',This)[EH](function (){
		  			var index=$('.event_doBotton.event_edit',This)[ID]($(this)),
		  				   $this=$(this),
		  				   alertID= data.rows[index].id;
		  			$('#warnList_edit'+alertID,This)[EV](CK,function (){
		  					AlertWindow($this, alertID,This) ;
		  			});  // 修改EV  end
		  	});  //EH end
		  	
		  	//点击删除按钮触发的事件
		  	$('.event_doBotton.event_del',This)[EH](function (){
		  			var index=$('.event_doBotton.event_del',This)[ID]($(this)),
		  			       $this=$(this),
		  				   alertID= data.rows[index].id;
		  				   $('#warnList_del'+alertID,This)[EV](CK,function(){
								DeleteWeiboWarn(alertID);		  				   
		  				   });
		  	});  // EH end
		  	
		} //  OperationAction end

		
		//删除某一条的微博预警
		function   DeleteWeiboWarn(alertID){
				 var 	html='<div class="giveMe_do del_reSee"><div class="giveMe_doDel" >您确定要删除吗？</div> <div class="push_ok"><div class="in_MsetCno" id="cancel">取&nbsp;&nbsp;消</div><div class="in_MsetCok" id="ok">确&nbsp;&nbsp;定</div></div><div class="com_clear"></div></div>';
				$[WW]({
		        css: { "width": "440px", "height": "auto"},
		        title:'&nbsp;&nbsp;预警列表',
		        content: html,
		        id: 'warnListSetpotion_'+alertID,
		        onLoad: function(div) {
		  				BtnHandle(alertID,div);	   			
		      		}   // onLoad end
    		});  //WW end
		}  //  DeleteWeiboWarn  end
		
	
		//删除按钮弹出层中的确定，取消按钮触发事件
		function  	BtnHandle(alertID,div){
			   $('#cancel',div)[EV](CK,function(){
			   		 CloseWindow(div);
			   });
		
			$('#ok',div)[EV](CK,function (){
			 CloseWindow(div);
    		  $(BY)[AJ]({
   				param: {action: 'warnservices.warnservicesinsert',queryConditions:'deleteWeiboWarnList',deleteID:alertID ,moduleType:4},
    			success: function (data) {
						OnLoadList(yeQianTypeHOne);
    			}
			});			
			
			}); // ok EV end
		}  // BtnHandle end
 	
 	
 	 //关闭窗体事件
    function CloseWindow(div) {
      div[RM]();
      $[RM]({ obj: '#' + JLY });
    }
 	
	
	          //打开预警窗体 进行修改
        function AlertWindow($this, alertID,This) {
        	$(BY)[AJ]({
						param: {action :'warnlist.warnserviceslist',queryConditions:'ModifyWarningList', alertID:alertID},
						success: function(dataWarnList) {
						if(parseInt(dataWarnList.id)==0){
							$[MB]({ content: '此条预警设置已不存在了!', type: 2,isAutoClose: TE });
							 OnLoadList(yeQianTypeHOne);
						}
						else{
				   	    $[WW]({
						css: { width: '400px', height: 'auto' },
						title: '微博预警',
						content: '<div class="giveMe_do warning_up"><div   class="warnListSet com_none"></div><div class="div_style"><label class="task_name">转发达到：</label><input type="text" value="" class="task_count task_Cwdith3 alertRepostNum" maxLength="10" /><span  class="com_fLeft">&nbsp;&nbsp;发送预警</span></div><div class="com_clear"></div><div class="div_style"><label class="task_name">评论达到：</label><input type="text" value="" class="task_count task_Cwdith3 alertCommentNum" maxLength="10" /><span  class="com_fLeft">&nbsp;&nbsp;发送预警</span></div><div class="com_clear"></div><div class="div_style"><label class="task_name">截止时间：</label><input type="text" value="" class="task_count task_Cwdith3 alertTime" readonly="readonly"  /></div><div class="com_clear"></div><div class="div_style warn_style"><label class="task_name">接收人：</label><div class="mynum_list"><h2 class="mynum_click_alert"><span >请选择</span>' +
								'<span class="mynum_type mynum_click "></span></h2><ul class="com_none nameList_alert">' +
								'</ul></div></div>  <div class="div_style"><label class="task_name">发送方式：</label><input type="checkbox"  disabled="disabled"  name="yjSend_way" class="com_fLeft yjSend_wayMar alertCheck" value="0" /><span  class="com_fLeft com_rMar">系统消息</span><input type="checkbox"  name="yjSend_way" class="com_fLeft yjSend_wayMar alertCheck" " value="1"   /><span  class="com_fLeft com_rMar">邮件</span><input type="checkbox"  name="yjSend_way" class="com_fLeft yjSend_wayMar alertCheck" " value="2"  /><span  class="com_fLeft com_rMar">手机短信</span></div><div class="com_clear"></div><div class="push_ok"><div class="in_MsetCno  alertCancel">取&nbsp;&nbsp;消</div><div class="in_MsetCok alertOK">确&nbsp;&nbsp;定</div><span class="alertMBTip" style="color:red;"></span></div><div class="com_clear"></div></div>',
						onLoad: function (div, close) {
							div[AJ]({
								param: {action: 'sysManager.GetTheUser'},
								success: function(data) {
									var ul = $('.nameList_alert', div),
										   ulHead = $('.mynum_click_alert', div),
										   alertRepostNum = $('.alertRepostNum', div),
										   alertCommentNum = $('.alertCommentNum', div),
										   alertCancel = $('.alertCancel', div),
										   alertOK = $('.alertOK', div),
										   alertTime = $('.alertTime', div),
										   liTemplate = '<li><input type="checkbox" class="liAlertName" id="fail_w{id}" value="{id}"/><label for="fail_w{id}">{useName}</label></li>';
									       html = $[EF](liTemplate, data),
									       repostNumber=$('.task_count.task_Cwdith3.alertRepostNum',div),
									       commentNumber=$('.task_count.task_Cwdith3.alertCommentNum',div),
									       warnListSet=$('.warnListSet',div);
									       
									       //开始给预警设置弹出层赋值
									       ul.html(html);
									       repostNumber.val(dataWarnList.warnRepost);
									       commentNumber.val(dataWarnList.warnComment);
								   		   alertTime.datePicker().val(dataWarnList.endTime);
								   		   warnListSet[AT]({id:'warnListSet_'+dataWarnList.id});
								   		   var   reveviers=dataWarnList.recevier.split(',');
								   		   var    revetypes=dataWarnList.revetype.split(',');
								   		   $('.liAlertName', div)[EH](function  (i,item){
								   		   	var 	$thisliAlertName=$(this),
								   		    		value=parseInt($thisliAlertName.val());
								   		   	   $[EH](reveviers,function (k, tem){
								   		   	   		if(parseInt(tem)==value){
								   		   	   			$thisliAlertName[EH](function (){
								   		   	   				$(this).attr("checked",true);
								   		   	   			});
								   		   	   			return  FE;
								   		   	   		}
								   		   	   });  //EH end  reveviers
								   		   }); // EH end
										
										
										$('.alertCheck',div)[EH](function (i,item){
												var  $thisalertCheckValue=$(this),
														 value =parseInt($thisalertCheckValue.val());
												if(i==0){
													$thisalertCheckValue[EH](function (){
															 $(this).attr("checked",true);										
													});
												}
												$[EH](revetypes,function (k, tem){
								   		   	   		if(parseInt(tem)==value){
								   		   	   			$thisalertCheckValue[EH](function (){
															    $(this).attr("checked",true);	
								   		   	   			});
								   		   	   			return  FE;
								   		   	   		}
								   		   	   });  //EH end  	revetypes			 
										});  //EH end										
																		   		   
								   /*	
								   	var startTime = $.getDate();
								   	alertTime.datePicker({minDate: startTime});
								   	
								   	endTime = $.addDate('d',7, startTime).Format('YYYY-MM-DD');
								   	alertTime.val(endTime);
								   	*/
								   	
								   	ulHead[EV](CK, function() {
										ul[RC]("com_none");
								   	});
								   	ul[EV]("mouseover",function(){
				    					$(this)[RC]("com_none");
					    			})[EV]("mouseout",function(){
				    					$(this)[AC]("com_none");
				    				});
				    				alertRepostNum.add(alertCommentNum)[EV](KU, function() {
				    					var $this = $(this),
				    						   txt = $[TM]($this.val());
				    					if(txt != '' && !$.regNumber(txt)) {
				    						$this.val('');
				    					}
				    					if (txt != '' && txt == '0') {
				    						$this.val('');
				    					}
				    				});
				    				
				    				alertCancel[EV](CK, function() {
				    					close();
				    				});
				    				
				    				alertOK[EV](CK, function() {
				    					var alertMBTip = $('.alertMBTip', div),
				    						   param = {};
				    					alertMBTip.text('');
				    					param.repostNum = alertRepostNum.val() == '' ? 0: alertRepostNum.val() ;
				    					param.commentNum = alertCommentNum.val() == '' ? 0: alertCommentNum.val() ;
				    					var nameids = [];
				    					$('.liAlertName', div)[EH](function() {
				    						if(this[CD]) {
				    							nameids.push(this.value);
				    						}
				    					});
				    					param.nameids = nameids.join(',');
				    					
				    					var typeids = [];
				    					$('.alertCheck', div)[EH](function() {
				    						if(this[CD]) {
				    							typeids.push(this.value);
				    						}
				    					});

				    					param.typeids = typeids.join(',');
				    					//param.platform = alertBtn[AT]('platform');
				    					//param.tid = alertBtn[AT]('url');
				    					param.time = alertTime.val();
				    					//param.alertid = 0;
				    					param.moduleType = 3;
				    				    //param.isdelete=alertIsDelete;//$this[AT]('isdelete');
							            param.alertid=alertID;//$this[AT]('alertid');
				    					//param.setRepost=$[TM]($this[PT]()[PT]()[FD]('.t4')[TX]());
				    					//param.setComment=$[TM]($this[PT]()[PT]()[FD]('.t6')[TX]());
				    					// 验证
				    					if(param.repostNum != '' || param.commentNum != '') {
				    						
				    					} else {
				    						alertMBTip.text('转发数或评论数必须填一个');
				    						return FE;
				    					}
				    					if(param.repostNum != '' && param.repostNum.split('')[0] == '0') {
				    						alertMBTip.text('转发数开头不能为0');
				    						return FE;
				    					}

				    					if(param.commentNum != '' && param.commentNum.split('')[0] == '0') {
				    						alertMBTip.text('评论数开头不能为0');
				    						return FE;
				    					}

				    					if(param.nameids == '') {
				    						alertMBTip.text('请选择接收人');
				    						return FE;
				    					}
				    					if(param.typeids == '') {
				    						alertMBTip.text('请选择发送方式');
				    						return FE;
				    					}
				    					param.action = 'warnservices.warnservicesinsert';
				    					// 提交保存
										div[AJ]({
											param: param,
											success: function(data) {
												if(data  > 0) {
													$[MB]({ content: '预警修改成功', type: 0 ,	isAutoClose: TE});
													OnLoadList(yeQianTypeHOne);
												} else if(data == -1) {
													$[MB]({ content: '已经存在', type: 2 });
												} else if(data == -2) {
													$[MB]({ content: '微博已删除', type: 2 });
												} else if(data == -3) {
													$[MB]({ content: '预警到达上限,不能添加', type: 2 });
												} else {
													$[MB]({ content: '预警修改失败', type: 2,	isAutoClose: TE });
												}
												close();
											}
										});
				    				});
								}
							});
						}
					});  // WW end																
				}   // else  end
						}//success
					});   // ajax end
        }    // AlertWindow end


    return {
    	IsLoad: FE,
        OnLoad: function (_ContentDiv) {
            var t = this;
            contentDiv = _ContentDiv;
            navigationBars=context.NavigationBars;
            $('<div />').load('modules/alertService/alertList.html', function () {
            	base = context.Base;
            	contentDiv[AP](base.FormatPmi($(this).html()));
            	t.IsLoad = TE;
                $key = $('#' + key);
                cuser=context.CurrentUser;
                changeYeQian=$('.zhao_change.span_title',$key);
			    warnservicesAll=$('#warnserviceList',$key);
			    warnservicesPager=$('#warnListPager',$key);
                Init();
            });
            return t;
        },
        Show : function (){
           $key.show();
            Init();
        },
        ToolBar: function (){
           Init();
        }
        
        
    };
});