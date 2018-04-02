//微博推送
define(['portal'], function (context) {
    //定义变量
    var key = 'twitterPush',
          $key, //当前的作用域
          contentDiv,
          navigationBars,
          service_tailsDiv ,//服务添加设置页面
          base,
          cuser,
          talkMe_eventDiv ,//推送服务首页
          pushserviceTwitterSendNum,
          pushserviceTaskDate,
          pushserviceTwitterSendDateTime,
          pushserviceTwitterTaskName,
          pushserviceTwitterTaskNamePrompt,
          pushserviceTwitterTaskNameRepeat,
          pushserviceEventHtml='',// 事件监控下面的 html
          pushserviceCheckContentHtml='',//   日常监测下的html
          listdiv;
    //私有方法
    //初始化方法，写自己模块的业务
    function Init() { 
    	NavigationInit();
    	service_tailsDiv.hide();
    	CheckSave();
		addBtn();
    	OnLoadmonitorList();
    }
    
    //判断是否有两个保存按钮
    function CheckSave(){
    	    var len= $('.push_do.push_save',$key).length;
    	     if(len==2){
    	    		 $('.push_do.push_save',$key).eq(0)[RM]();
    	    }
    }
    
		//添加按钮事件    
	     	function addBtn(){
	     	    var event_addbtnFlag= cuser.Templates['yjfw_wbtssave_1'];
	    	    if(event_addbtnFlag!=''&&event_addbtnFlag!=UN){
	    	    	if($('.event_add',$key)[LN]==0){
	    	    		  $('#pushServices_addBTN',$key)[AP]('<span class="event_add imp_liRight"></span>');	
	    	    	}
	    	    }
	    	    
	    	    $('.event_add',$key)[EV](CK,function(){
	     		 talkMe_eventDiv.hide();
	     		 service_tailsDiv.show();
	     		 navigationBars.Empty()
	     		 .Add(key, 'ToolBar', '预警服务')
				 .Add(key, 'ToolBar', '推送服务')
    			 .Add(key, 'ToolBarTwo', '推送设置')[oL]();
				 $('#pushtwitter_h1',$key).html('')[AP]($('<span class="span_title ">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;新建推送</span>'))		        
	     		 RestPanel(0);
	     		});
	     }
	     
	     //导航条触发事件
	     function  SameHandle  (){
	     			Init();
		      		pushserviceTwitterTaskNamePrompt[AC]('com_none');
		      		pushserviceTwitterTaskNameRepeat[AC]('com_none');
		      		pushserviceTwitterSendDateTime[AC]('com_none');
		      	    $('#checkcontent_prompt',$key)[AC]('com_none');
		       	    $('#receiveman_prompt',$key)[AC]("com_none");
		      		talkMe_eventDiv.show();
	     }
	     
	     //input 输入框限制只能输入数字
	     //isOpen  true 是开启发送邮件不能小于10条
	     //false 是开启发送天数至少在1天
	     function isNaNInput(input,num,isOpen){
	     	 input.val(num);
	     	 input[KU](function (){
	     	 var temp=input.val();
	     	 input.val(temp.replace(/[^\d]/g,''));
	     	 });
	     	 
	     	 input.blur(function (){
	     	 		 var temp=input.val();
	     	 		 if(temp==""){
	     	 		   input.val(num);
	     	 		 }else{
	     	 		      var  v=temp.substring(0,1);
	     	 		       if(v=="0"){
	     	 		       	 temp=temp.replace(/0{1,}/,'');
	     	 		       	 if(temp==""){
	     	 		       	 		temp=num;
	     	 		       	 	}
	     	 		       }
	     	 		       if(parseInt(temp)<10&&isOpen){
	     	 		          temp="10";
	     	 		       }
	     	 		       input.val(temp);
	     	 		 }
	     	 });
	     }
	     
	     //任务将在的时间input输入框限制
	     function   DateSet(object){
	     	 var temp =object.val();
	     	 var array=temp.split('-');
	     	 var date =new  Date();
	     	 if(parseInt(date.getFullYear())>parseInt(array[0])){
	     	 //alert("年份太老了");
	     	    return  FE;
	     	 }else{
	     	         var ttime='';
	     	         //IE, google浏览器下显示的月份是 09这样的
	     	         if(array[1].substring(0,1)=='0'){
	     	         		ttime=array[1].substring(1);
	     	         }
	     	         //火狐浏览器下的显示的月份是 9这样的
	     	         //当IE下的 月份是两位数的也走这个
	     	         else{
	     	         		ttime=array[1];
	     	         }
	     	         
	     	 			if((parseInt(date.getMonth())+1)>parseInt(ttime)){
					//alert("月份太老了");
					return  FE;
					}else{
					    var ttime2='';
	     	         //IE, google浏览器下显示的日期是 09这样的
	     	         if(array[2].substring(0,1)=='0'){
	     	         		ttime2=array[2].substring(1);
	     	         }
	     	         //火狐浏览器下的显示的日期是 9这样的
	     	         //当IE下的 日期是两位数的也走这个
	     	         else{
	     	         		ttime2=array[2];
	     	         }
						  if((parseInt(date.getMonth())+1)==parseInt(ttime)){
								if(parseInt(date.getDate())>parseInt(ttime2)){
									//alert("日期太老了");
									return  FE;
								} //  if 2
							} // if 1
					} //else 结束
	     	 }
	     		return TE;
	     }
	     
	     
	     //修改页面当中从后台读取的信息赋值到页面当中
	     function   ModifyEvaluate (data){
	   				pushserviceTwitterTaskName.val(data.name);
					$('#task_name_id',$key)[TX](data.id);
					if(data.type=="1"){
									 //事件监控
									$("#task_type",$key).val(1);		
								 }else{
								 //日常监控
								//***修改的情况下(如果选中的是日常，压根不让出现事件监控)
		 		    			//没有权限
		 		    			var   moniterBtnQuanXian=$('#eventMonitorItem');
					 		    if(moniterBtnQuanXian==UN||moniterBtnQuanXian[LN]==0){
					 		    	$("#task_type",$key).html('');
					 		    	$("#task_type",$key)[AP]('<option value="0"> 日常监测 </option>');
					 		    }
								 $("#task_type",$key).val(0);
						}// 判断结束					 
				 //检测内容(修改时候的赋值)
				 var  arr=[];
				  var  arrs=[];
				  var arrs2=[];
				  if(data.type=="1"){
				      arr=data.eventList;
				  }else{
				  	  arr=data.taskList;
				  }
				  for(var i=0;i<arr.length;i++){
				  	    arrs[i]=  arr[i].id;
				  	    arrs2[i]=arr[i].name;
				  }
				 var format= "<li  class=\"ev_wPlace\"  id=\"{0}\"  title=\"{2}\"><span class=\"ev_wCount\">{1}</span><span class=\"ev_wDel\" ></span></li>";
			     var html="";
			     for(var i=0;i<arrs.length;i++){
			           html+=$[FO](format,arrs[i],arrs2[i],arrs2[i]);
			     }
				add_taskList.html('')[AP]($('<ul class="event_Wadd"></ul>',$key)[AP]($(html)));
				//回到页面页面看 可以删除事件
			    $('.ev_wDel',add_taskList)[EV](CK,function(){
			    		$(this)[PT]()[RM]();
			    });
				//通讯录接受人赋值(修改状态)
				var  cons=[];
				cons=data.contacts;
		 var namelistdiv=   $('#nameList',$key);
		 namelistdiv.html('');
		 var formathtml='<li  class="ev_wPlace" title="{1}"  id="{2}"><span class="ev_wCount">{0}</span><span class="ev_wDel" ></span></li>';
		  var html="";
		   for(var i=0;i<cons.length;i++){
		     if(cons[i].contactId!="-1"){
		       html+=   $[FO](formathtml,cons[i].userName,cons[i].email,cons[i].contactId);
		     }else{
		     html+=   $[FO](formathtml,cons[i].email,cons[i].email,cons[i].contactId);
		     }
		   }
		   html='<ul class="event_Wadd">'+html+'</ul>'
		  namelistdiv[AP]($(html));
		        $('.ev_wDel',namelistdiv)[EV](CK,function (){
		     	 $(this)[PT]()[RM]();
		     });
		//发送条数赋值(修改状态)
		var a=parseInt(data.topN);
		pushserviceTwitterSendNum.val(a/10);
		//发送时间的类型: 天，周，月
		  $('#pushserivce_dateType',$key).val(parseInt(data.periodType));
		//任务将在的两个输入框赋值(修改状态)
		var TIME=data.firstRunTime;
		var strs=[];
		strs=TIME.split(' ');
		var before=strs[0];
		var  after=strs[1];
		$('.push_time',$key).datePicker().val(before);
			//00:00:00处理	    
			$('.push_hours',$key).html('');
		 	$('.push_hours',$key).timePicker({  id: '_a1',defaultTime :after});	    
		 	//$('.push_hours',$key)[FD]('.left').css({"height" :"22px","overflow":"hidden"});
			$('.push_hours',$key)[AT]('time',after);
	     }
	     
	     
//添加新推送设置恢复默认状态
// changeType   0 添加 1 修改
		 function  RestPanel(changeType){
					if(changeType==0){
					var  senddate=  $('#push_cycleday',$key);
					pushserviceTwitterTaskName.val('');
					$('#task_name_id',$key)[TX]('');
					$('.push_hours',$key).html('');
					
					var date = new Date();
					var hour = date.getHours();
					var minute = date.getMinutes();
					var second = date.getSeconds();
					var cursystime=hour+':'+minute+':'+second;
		 			$('.push_hours',$key).timePicker({  id: '_a1',defaultTime: cursystime  });
		 			$('.push_hours',$key)[AT]('time',cursystime);
		 			//$('.push_hours',$key)[FD]('.left').css({"height" :"22px","overflow":"hidden"});
	     			$('.push_time',$key).val('');
	     			$('.push_time',$key).datePicker();
	     			
					$("#task_type",$key).val(0);
	     			 //判断添加的情况 判断是否有事件监控整体权限
					//***添加的情况下
					var   moniterBtnQuanXian=$('#eventMonitorItem');
		 		    //没有权限
		 		    if(moniterBtnQuanXian==UN||moniterBtnQuanXian[LN]==0){
		 		    	$("#task_type",$key).html('');
		 		    	$("#task_type",$key)[AP]('<option value="0"> 日常监测 </option>');
		 		    	$("#task_type",$key).val(0);
		 		    }
		 		    
		 		    
		 		    $('#taskList',$key)[TX]('');
		 		      $('#nameList',$key).html('');
		 		    senddate.val(1);
		 		   pushserviceTwitterSendNum.val(1);
		 		    //$('#pushserivce_dateType',$key).val(0);
		 		    $('#pushserivce_dateType',$key).val('2');
		 		    }
		 		    
		 		    
		 		    pushserviceTwitterTaskNamePrompt[AC]('com_none');
		 		    pushserviceTwitterTaskNameRepeat[AC]('com_none');
		      		pushserviceTwitterSendDateTime[AC]('com_none');
		      	    $('#checkcontent_prompt',$key)[AC]('com_none');
		       	    $('#receiveman_prompt',$key)[AC]("com_none");
		 		    
		 		  
		 		    var data=arguments[1];
		 		    var tn;
		 		    if(data!=null||data!=undefined){
		 		       tn=data.periodCount;
		 		       ModifyEvaluate(data);
		 		    }
		 		   
		 		    var  senddate=  $('#push_cycleday',$key);
		 		   // senddate.val();
		 		    //两个输入框的限制
		 		    if(changeType==0){
		 		       isNaNInput(senddate,1,false);
		 		    }else{
		 		    //中间参数赋值赋的是传过来的值
		 		       isNaNInput(senddate,tn,false);
		 		    }
		 		    
		 		 pushserviceTwitterTaskName[EV](KU,function(){
		       	 if(pushserviceTwitterTaskName.val()!=""){
		       	 		pushserviceTwitterTaskNamePrompt[AC]('com_none');
		       	 		pushserviceTwitterTaskNameRepeat[AC]('com_none');
		       	 }
		       	 //只能最多输入25个字符
		         if(pushserviceTwitterTaskName.val()[LN]>25){
					 	pushserviceTwitterTaskName.val(pushserviceTwitterTaskName.val().substring(0,25));
				}
		       });
		 		    
					//日常 or 事件监控添加弹出层
			 	    addDivShow();
			 	    //从通讯录添加弹出层
			 	  addDivReceiveMan();
			 	  
			 	 //大页面的确定 取消操作
			 	 BigDivHandle(changeType);
		 }
		 
		 //大页面的确定 ，取消
		 function  BigDivHandle(changeType){
		 	//判断权限修改判断   有没有保存按钮
		 	    var addbtnTemp=cuser.Templates['yjfw_wbtssave_1'];
		 	    var  modifybtnTemp=cuser.Templates['yjfw_wbtsmodify_1'];
     		 	 var  dosave=$('.push_do.push_save',$key);
		 	    /*
		 	      if(dosave.length==0){
		 	        $('.push_do.push_close').css('margin-left','80px');
		 	      }
			*/
			//添加保存有权限，修改保存没有权限,而且进入的是修改页面
			if(addbtnTemp!=UN&&addbtnTemp!=""&&(modifybtnTemp==UN||modifybtnTemp=="")&&changeType==1){
							 dosave.hide();
			}
			//添加保存无权限，修改保存有权限,而且进入是新建页面
			else if((addbtnTemp==UN||addbtnTemp=="")&&modifybtnTemp!=UN&&modifybtnTemp!=""&& changeType==0){
							 dosave.hide();
			}
			//添加保存有权限，修改保存没有权限,而且进入的是新建页面
			else if(addbtnTemp!=UN&&addbtnTemp!=""&&(modifybtnTemp==UN||modifybtnTemp=="")&&changeType==0){
			 			     dosave.show();
			}
			//添加保存无权限，修改保存有权限,而且进入是修改页面
			else  if((addbtnTemp==UN||addbtnTemp=="")&&modifybtnTemp!=UN&&modifybtnTemp!=""&& changeType==1){
						     dosave.show();
			}

		      $('.push_do.push_save',$key)[EV](CK,function (){
		        var arrays=[];
		      //任务名称没有输入的判断
		       /*
		       if( pushserviceTwitterTaskName.val()==""){
		       		pushserviceTwitterTaskNamePrompt[RC]('com_none');
		       		arrays.push(1);
		       }else{
		       		  var regu = /^[0-9a-zA-Z\u4e00-\u9fa5]+$/;      
   					 if (!regu.test( pushserviceTwitterTaskName.val() )) {   
   					    pushserviceTwitterTaskNamePrompt[RC]('com_none');
     					arrays.push(1);
    					}
		       }
		       */
		       
		        //红色div提示去掉
		      	 	pushserviceTwitterTaskNamePrompt[AC]('com_none');
		      	 	pushserviceTwitterTaskNameRepeat[AC]('com_none');
		      	 	pushserviceTwitterSendDateTime[AC]('com_none');
		      	 $('#checkcontent_prompt',$key)[AC]('com_none');
		       	$('#receiveman_prompt',$key)[AC]("com_none");
		       
		   	if( pushserviceTwitterTaskName.val()==""){
		       		pushserviceTwitterTaskNamePrompt[RC]('com_none');
		       		arrays.push(1);
		       	    return FE;
		       }else{
		       		  var regu = /^[0-9a-zA-Z\u4e00-\u9fa5]+$/;      
   					 if (!regu.test( pushserviceTwitterTaskName.val() )) {   
   					    pushserviceTwitterTaskNamePrompt[RC]('com_none');
     					arrays.push(1);
     					return FE;
    					}
    					
    					else{	 //符合的情况下判断同一企业级下的添加or新建,推送任务名不能重复的
    					// changeType   0 添加 1 修改
    					var   pushTaskID=0;
    					if(changeType==1){
    						pushTaskID=$[TM]($('#task_name_id',$key)[TX]());
    					}
    			  $(BY)[AJ]({
						param: {action :'pushservices.pushserviceslist',queryConditions:'checkNameRepeat',pushTaskName:pushserviceTwitterTaskName.val(), 
						pushTaskID:pushTaskID },
						success: function(dataRepeat) {
						//dataRepeat ==0 表示不重复的   dataRepeat>0  代表重复了
									if(parseInt(dataRepeat)>0){
										pushserviceTwitterTaskNameRepeat[RC]('com_none');
										return FE;
									}else{
										//+++++++++++++++++
								//检测内容添加判断
								if(add_taskList[TX]()==""){
						         $('#checkcontent_prompt',$key)[RC]('com_none');
						         arrays.push(1);
						         return FE;
						       }else{
						       $('#checkcontent_prompt',$key)[AC]('com_none');
						       }
						       //添加通讯录没有添加判断
									if( $('#nameList',$key)[TX]()==""){
									    $('#receiveman_prompt',$key)[RC]("com_none");
									       arrays.push(1);
									       return FE;
									}else{
									$('#receiveman_prompt',$key)[AC]("com_none");
									}	      
						       
						      	//判断任务将在日期
						      	 /*
						      	 var   isDate=  DateSet(pushserviceTaskDate);
						      	if(!isDate){
						      	 	pushserviceTwitterSendDateTime[RC]('com_none');
						      	 	arrays.push(1);
						      	 	return FE;
						      	 	}
						      	 	*/
						      	 	
						      	 /*
						      	 	if(arrays.length>0){
						      	 	return ;
						      	 	}
						      	 	*/
						      	 	//红色div提示去掉
						      	 	
						      	 //	pushserviceTwitterTaskNamePrompt[AC]('com_none');
						      	 //	pushserviceTwitterTaskNameRepeat[AC]('com_none');
						      	 //	pushserviceTwitterSendDateTime[AC]('com_none');
						      	 //$('#checkcontent_prompt',$key)[AC]('com_none');
						       	//$('#receiveman_prompt',$key)[AC]("com_none");
						      	
						      	//向数据库注入
						      	var  tasknameID=$('#task_name_id',$key)[TX]();
						      	var  nam=pushserviceTwitterTaskName.val();
								 var  selectval= $("#task_type",$key).val();		      	
						      	var monitorContent='';
						      	$('.ev_wPlace',add_taskList)[EH](function (){
						      		   monitorContent+= $(this)[AT]('id')+',';
						      	});
						      	var ress='';
						      	$('.ev_wPlace',$('#nameList',$key))[EH](function (){
						      					var  nID=$(this)[AT]('id');
						      					if(nID!="-1"){
						      					ress+=$(this)[AT]('id')+';';	
						      					}else{
						      						ress+=$(this)[AT]('title')+';';	
						      					}					      	
						      	});
						      	var sendms=  pushserviceTwitterSendNum.find("option:selected").text();
								var   timeNumber=$('#push_cycleday',$key).val();
								var  timeType=    $('#pushserivce_dateType',$key).find("option:selected").val();
								var timing=pushserviceTaskDate.val()+' '+$('.push_hours',$key)[AT]('time');	
							if(changeType==0){
							$(BY)[AJ]({
				    		param: {action: 'pushservices.pushserviceslist.add',queryConditions:'save',taskName:nam, taskTypes: selectval,mcontent:monitorContent
				    		,reman :ress, sendmailsnu:sendms,  timenum:timeNumber, timetps:timeType, timings:timing},
				    		success: function (data) {
							   		      	 Init();
							   		      	 pushserviceEventHtml='',// 事件监控下面的 html
				                             pushserviceCheckContentHtml='',//  监测内容下的html
						      		         talkMe_eventDiv.show();
				 			   		}
				    		});
				    		}
				    		else {
				    		$(BY)[AJ]({
				    		//action: 'pushservices.pushserviceslist.update',
				    		param: {action: 'pushservices.pushserviceslist.modify',queryConditions:'update',tkID: tasknameID,taskName:nam, taskTypes: selectval,mcontent:monitorContent
				    		,reman :ress, sendmailsnu:sendms,  timenum:timeNumber, timetps:timeType, timings:timing},
				    		success: function (data) {
							   		      	 Init();
							   		      	 pushserviceEventHtml='',// 事件监控下面的 html
				                             pushserviceCheckContentHtml='',//  监测内容下的html
						      		         talkMe_eventDiv.show();
				 			   		}
				    		});
				    			
				    		}// update修改结束
										
									}  //else end
							   
						}//success
					});
    
    					}               // 符合的情况下判断同一企业级下的添加or新建,推送任务名不能重复的 end
		       }
		       
		       /*
		       pushserviceTwitterTaskName[EV](KU,function(){
		       	 if(pushserviceTwitterTaskName.val()!=""){
		       	 		pushserviceTwitterTaskNamePrompt[AC]('com_none');
		       	 }
		       	 //只能最多输入25个字符
		         if(pushserviceTwitterTaskName.val()[LN]>25){
					 	pushserviceTwitterTaskName.val(pushserviceTwitterTaskName.val().substring(0,25));
				}
		       });
		       */
		       
		     //0000000000
		     /*
		       //检测内容添加判断
				if(add_taskList[TX]()==""){
		         $('#checkcontent_prompt',$key)[RC]('com_none');
		         arrays.push(1);
		         return FE;
		       }else{
		       $('#checkcontent_prompt',$key)[AC]('com_none');
		       }
		       //添加通讯录没有添加判断
					if( $('#nameList',$key)[TX]()==""){
					    $('#receiveman_prompt',$key)[RC]("com_none");
					       arrays.push(1);
					       return FE;
					}else{
					$('#receiveman_prompt',$key)[AC]("com_none");
					}	      
		       
		      	//判断任务将在日期
		      	 var   isDate=  DateSet(pushserviceTaskDate);
		      	if(!isDate){
		      	 	pushserviceTwitterSendDateTime[RC]('com_none');
		      	 	arrays.push(1);
		      	 	return FE;
		      	 	}
		      	 	
		      	 //
		      	 	//if(arrays.length>0){
		      	 //	return ;
		      	 	/}
		      	 	//
		      	 	//红色div提示去掉
		      	 	pushserviceTwitterTaskNamePrompt[AC]('com_none');
		      	 	pushserviceTwitterTaskNameRepeat[AC]('com_none');
		      	 	pushserviceTwitterSendDateTime[AC]('com_none');
		      	 $('#checkcontent_prompt',$key)[AC]('com_none');
		       	$('#receiveman_prompt',$key)[AC]("com_none");
		      	//向数据库注入
		      	var  tasknameID=$('#task_name_id',$key)[TX]();
		      	var  nam=pushserviceTwitterTaskName.val();
				 var  selectval= $("#task_type",$key).val();		      	
		      	var monitorContent='';
		      	$('.ev_wPlace',add_taskList)[EH](function (){
		      		   monitorContent+= $(this)[AT]('id')+',';
		      	});
		      	var ress='';
		      	$('.ev_wPlace',$('#nameList',$key))[EH](function (){
		      					var  nID=$(this)[AT]('id');
		      					if(nID!="-1"){
		      					ress+=$(this)[AT]('id')+';';	
		      					}else{
		      						ress+=$(this)[AT]('title')+';';	
		      					}					      	
		      	});
		      	var sendms=  pushserviceTwitterSendNum.find("option:selected").text();
				var   timeNumber=$('#push_cycleday',$key).val();
				var  timeType=    $('#pushserivce_dateType',$key).find("option:selected").val();
				var timing=pushserviceTaskDate.val()+' '+$('.push_hours',$key)[AT]('time');	
			if(changeType==0){
			$(BY)[AJ]({
    		param: {action: 'pushservices.pushserviceslist.add',queryConditions:'save',taskName:nam, taskTypes: selectval,mcontent:monitorContent
    		,reman :ress, sendmailsnu:sendms,  timenum:timeNumber, timetps:timeType, timings:timing},
    		success: function (data) {
			   		      	 Init();
			   		      	 pushserviceEventHtml='',// 事件监控下面的 html
                             pushserviceCheckContentHtml='',//  监测内容下的html
		      		         talkMe_eventDiv.show();
 			   		}
    		});
    		}
    		else {
    		$(BY)[AJ]({
    		//action: 'pushservices.pushserviceslist.update',
    		param: {action: 'pushservices.pushserviceslist.modify',queryConditions:'update',tkID: tasknameID,taskName:nam, taskTypes: selectval,mcontent:monitorContent
    		,reman :ress, sendmailsnu:sendms,  timenum:timeNumber, timetps:timeType, timings:timing},
    		success: function (data) {
			   		      	 Init();
			   		      	 pushserviceEventHtml='',// 事件监控下面的 html
                             pushserviceCheckContentHtml='',//  监测内容下的html
		      		         talkMe_eventDiv.show();
 			   		}
    		});
    			
    		}// update修改结束
		     
		     */
		     //00000000000
    		
		      });   //推送服务的大确定按钮结束
		      
		      
		      $('.push_do.push_close',$key)[EV](CK,function (){
		      		pushserviceEventHtml='',// 事件监控下面的 html
                    pushserviceCheckContentHtml='',//  监测内容下的html
		      		Init();
		      		pushserviceTwitterTaskNamePrompt[AC]('com_none');
		      		pushserviceTwitterTaskNameRepeat[AC]('com_none');
		      		pushserviceTwitterSendDateTime[AC]('com_none');
		      	    $('#checkcontent_prompt',$key)[AC]('com_none');
		       	    $('#receiveman_prompt',$key)[AC]("com_none");
		      		talkMe_eventDiv.show();
		      });
		 }
		 
		 
		 //添加接收人操作
		 function  addDivReceiveMan(){
		 //接受人按钮开始
		 	$('#receiveman',$key)[EV](CK,function(){
				 var 	html='<tr><td><input type="checkbox" id="{0}"  class="pushservicesCheckBoxs"/></td> <td >{1}</td><td>{2}</td></tr>';
				 var tempHtml="";

			$(BY)[AJ]({
    		param: {action: 'pushservices.pushserviceslist',queryConditions:'recevieManMethod'},
    		success: function (data) {
    			$[EH](data,function(i,item){
    				 tempHtml=tempHtml+$[FO](html,item.id,item.pushservicesName,item.pushservicesMails);
    			});
    			var bodyHTML='<div class="giveMe_do event push_show"><div class="push_showList"><table class="push_table web_table"><th  style="width:30px"><input type="checkbox" id="allcheckbox" /></th> <th>接收人</th><th>邮箱</th>'+tempHtml+'</table></div><div class="div_style"><label class="task_name" for="elseConnect">其他接收人：</label><input type="text" class="task_count task_Cwdith1" id="elseConnect" /><span>（非必选）</span>  </div>       <div class="div_style com_none"  id="othersRemans"><label class="task_name"></label><font color="red">* 请检查其他接受人当中邮箱有不正确的</font></div>                  <div class="div_style"><label class="task_name"></label><font color="#999">邮箱格式如123@126.com;456@sina.com;...;</font></div> '
    			+'<div id="revicerMaxNum" class="div_style com_none"><label class="task_name"></label><font color="red">* 选择接受人不能超出最大限制20人</font></div>'
    			+'  <div class="push_ok"><div class="in_MsetCno" id="pushservices_cancel">取&nbsp;&nbsp;消</div><div class="in_MsetCok" id="pushservices_submit">确&nbsp;&nbsp;定</div></div><div class="com_clear"></div></div>';
    			 		//弹出层开始
				$[WW]({
		        css: { "width": "500px", "height": "auto"},
		        //event: 'no',
		        title: '选择接受人',
		        content: bodyHTML,
		        id: 'receviceManID',
		        onLoad: function(div) {
		      	//写自己的添加弹出层业务
			
			/**
			** 可能添加接受人按钮的时候，之前已经选择有的，
			*应该把状态还原
			*/
			var  nameListdiv=$('#nameList',$key);
			if(nameListdiv[TX]()!=""){
			 var mails=[];
			$('.ev_wPlace',nameListdiv)[EH](function (){
					mails.push($(this)[AT]('title'));
			});
			//先判断看看表格里是否选择过，选择过的需要打上对钩
			var  tempObject={};
			for(var i =0;i<mails.length;i++){
						   $('.pushservicesCheckBoxs',div)[EH](function (){
								if(mails[i]==$(this)[PT]()[NT]()[NT]()[TX]()){
											this[CD]=true;
											tempObject[mails[i]]=mails[i];
											return false;
								}
			   		});
				} //for结束
		
			if($('.pushservicesCheckBoxs',div).length==$(':checked',div).length){
				    $('#allcheckbox',div)[EH](function (){
				    this[CD]=true;
				    });
			}	
			var text="";
				for(var i =0;i<mails.length;i++){
						if(tempObject[mails[i]]==undefined){
						text+=mails[i]+";";
						}
				} //for结束
					$('#elseConnect',div).val(text);
			}
			
		      	//全选/取消全选按钮开始
		      	$('#allcheckbox',div)[EV](CK,function(){
		      		$('#revicerMaxNum',div)[AC]('com_none');	
		      		if($('#allcheckbox',div).is(':checked')){
		      			$('.pushservicesCheckBoxs',div)[EH](function (){
										this[CD] =TE;
		      			});
		      			}else{
		      			    			$('.pushservicesCheckBoxs',div)[EH](function (){
										//$(this)[AT]("checked",false);
										this[CD] =FE;
		      			});
		      			}
		      	});//全选/取消全选按钮结束
		
		//判断如果单个全部选中的各种情况
	    $('.pushservicesCheckBoxs',div)[EV](CK,function(){
	    	$('#revicerMaxNum',div)[AC]('com_none');	
	  	    var  num=data.length;
	 		var point=0;
			$('.pushservicesCheckBoxs',div)[EH](function(){
		if(this[CD]){
			    point+=1;		
		}
		});
		if(point==num){
			$('#allcheckbox',div)[EH](function(){
				this[CD]=TE;
			});
			}
			else{
						$('#allcheckbox',div)[EH](function(){
				this[CD]=FE;
			});
			}
	    }); // EV   pushservicesCheckBoxs结束
		//判断如果单个全部选中的各种情况结束
		      	//确定 ，取消 操作
		      	$('#pushservices_submit',div)[EV](CK,function(){
		      	//触发  向下面多行文本框赋值
		      	var arr=[],
		      	       arr2=[],
		      		   arr3=[];
		      	$('.pushservicesCheckBoxs',div)[EH](function(){
					if(this[CD]){
					arr.push($(this)[AT]('id'));
					arr2.push($(this)[PT]()[NT]()[TX]());
					arr3.push($(this)[PT]()[NT]()[NT]()[TX]());
					}		      		
		      	}
		      	);
		      	//其他接受人选项
		      	$('#elseConnect',div)[EV](KU,function(){
		      			$('#othersRemans',div)[AC]('com_none');
		      			$('#revicerMaxNum',div)[AC]('com_none');	
		      	});
		      	var v=$('#elseConnect',div).val();
		      	if(v!=""){
		      	  if(v.substring(v.length-1).indexOf(";")==-1){
		      	  	v+=";";
		      	  }
		      	 var  elseReman=new Array();
		         elseReman =	v.split(';');
		         elseReman.pop();
		         //判断可能其中有不规范的邮箱地址
		         var remans=new Array();
				  var  flag=true;
		         for(var i=0;i<elseReman.length;i++){
		               var  temp=  elseReman[i][TM]();
		               // var pattern = /^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+(.[a-zA-Z0-9_-])+/;    
 						var pattern=/^\w+([-+.]\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*$/;
 						flag = pattern.test(temp);  
		               if(!flag){
		                 break;
		               }
		           		remans.push(temp);
		         }
		         if(!flag){
		    	 	$('#othersRemans',div)[RC]('com_none');
		           return;
		         }else{
		         for(var i=0;i<remans.length;i++){
		           arr.push(-1);
		         }
		         arr2=arr2.concat(remans);
		         arr3=arr3.concat(remans);
		         }
		    }
		    
		    //数组里有可能有重复的去重的
			     var newArr = [],
            			tempObj = {};
            			var  it=0;
        for(var i =0; i<arr2.length; i++){
            if(tempObj[arr2[i]] != arr2[i]){
                newArr.push(arr2[i]);
                tempObj[arr2[i]] = arr2[i];
            }else{
            arr.splice(i-it,1);
            arr3.splice(i-it,1);
            it++;
            }
        }
   				arr2=newArr;
		      	
		      	//判断上限只能有20， 多了提示
		      	if(arr2[LN]>20){
					$('#revicerMaxNum',div)[RC]('com_none');	      		
		      		return ;
		      	}
		      	
		      	 SetOptionsReceviceMan(arr,arr2,arr3);
		      		PushServicesCloseWindows(div);
		      		//添加通讯录没有添加判断
					if( $('#nameList',$key)[TX]()==""){
					    $('#receiveman_prompt',$key)[RC]("com_none");
					}else{
					$('#receiveman_prompt',$key)[AC]("com_none");
					}	      		
		      	});
		      	
		      	   	$('#pushservices_cancel',div)[EV](CK,function(){
		      			PushServicesCloseWindows(div);
		      	});
		      	
		      	//确定 ，取消 操作结束
		    //写自己的添加弹出层业务结束
		      		}//onLoad结束
    			});
		 		//弹出层结束
    		}
    	});
		 	});//接受人按钮结束
		    
		 }
		 
		 //把赋值放入接受人的多行文本DIV中
		 function SetOptionsReceviceMan(arr,arr2,arr3){
		 var formathtml='<li  class="ev_wPlace" title="{0}"   id="{2}" ><span class="ev_wCount">{1}</span><span class="ev_wDel" ></span></li>';
		  var html="";
		  if(arr3==undefined){
		  	add_taskList.html('');
		     for(var i=0;i<arr2.length;i++){
		   	   html+=   $[FO](formathtml,arr2[i],arr2[i],arr[i]);
		   }
		      html='<ul class="event_Wadd">'+html+'</ul>'
		  	 add_taskList[AP]($(html));
		        $('.ev_wDel',add_taskList)[EV](CK,function (){
		     	 $(this)[PT]()[RM]();
		     });
		  }else{
		  	 var namelistdiv=   $('#nameList',$key);
		 	 namelistdiv.html('');
		     for(var i=0;i<arr2.length;i++){
		   	   html+=   $[FO](formathtml,arr3[i],arr2[i],arr[i]);
		   }
		      html='<ul class="event_Wadd">'+html+'</ul>'
		      namelistdiv[AP]($(html));
		        $('.ev_wDel',namelistdiv)[EV](CK,function (){
		     	 $(this)[PT]()[RM]();
		     });
		  }
	}

		 
		 
		 //receviceManID接受人 弹出层关闭
		 function PushServicesCloseWindows(div){
		 	 		div[RM]();
		 	       $[RM]({ obj: '#' + JLY });
		 }
		 
		 
		 //添加弹出层操作  
		 function addDivShow(){
		/*
			  	 $('#task_type',$key)[CE](function (){
			  	 //选中的是事件，应该把日常检测html保存
			  	 if(parseInt($(this).find("option:selected").val())==1){
						alert(1);
			  	      pushserviceCheckContentHtml=add_taskList.html();//  日常监测下的html
			  	      add_taskList.html(pushserviceEventHtml);
			  	 }
			  	 //选中的是日常，应该把事件检测html保存
			  	 else{
			  	   alert(0);
			  	   pushserviceEventHtml=	add_taskList.html();// 事件监控下面的 html
			  	     add_taskList.html(pushserviceCheckContentHtml);
			  	 }
					
				$('.ev_wDel',add_taskList)[EV](CK,function(){
			    		$(this)[PT]()[RM]();
			    });			  	 
		 });
		 */
		   	 $('#task_type',$key)[UB](CE)[BD](CE ,function (){
			  	 //选中的是事件，应该把日常检测html保存
			  	 if(parseInt($(this).find("option:selected").val())==1){
			  	      pushserviceCheckContentHtml=add_taskList.html();//  日常监测下的html
			  	      add_taskList.html(pushserviceEventHtml);
			  	 }
			  	 //选中的是日常，应该把事件检测html保存
			  	 else{
			  	   pushserviceEventHtml=	add_taskList.html();// 事件监控下面的 html
			  	     add_taskList.html(pushserviceCheckContentHtml);
			  	 }
					
				$('.ev_wDel',add_taskList)[EV](CK,function(){
			    		$(this)[PT]()[RM]();
			    });			  	 
		 });
		 
		 
		   $('#checkcontent',$key)[EV](CK,function(){ 
		   if($("#task_type",$key)[FD]('option:selected').val()==0){
				 var 	html='<div class="giveMe_do address_ist"><div class="address_lLeft"></div><div class="address_lRight"><h5 class="taskSel_title">选项列表</h5><ul id="addressCount_listID"  class="addressCount_list">'+
				 '</ul>'+
				 '<div class="com_clear"></div><h4>下面是您已选择的：</h4><ul class="event_Wadd">'+
				 '</ul></div>'+
				 '<div class="com_clear"></div><div class="push_ok"><div class="in_MsetCno" id="pushserivces_checkContentAdd_cancel">取&nbsp;&nbsp;消</div><div class="in_MsetCok"  id="pushserivces_checkContentAdd_ok">确&nbsp;&nbsp;定</div>'+
				 '</div><div class="com_clear"></div></div>';
				$[WW]({
		        css: { "width": "680px", "height": "auto"},
		        //event: 'no',
		        title: '选择列表',
		        content: html,
		        id: '_pushservice_taskselect',
		        onLoad: function(div) {
		      	//写自己的添加弹出层业务
		     	//三级菜单开始
		    div.taskPicker({
			param: {action :'pushservices.pushserviceslist',queryConditions :'work'},
            id: 'pushservices_taskListShow',
            loadSucceed: function () { 
             //如果推送服务中存在已经选好的
			 //此时在点击出现弹出层应该
			 //上面存在有选中状态
			   if($('#taskList',$key)[TX]!=""){
			   		 var tasklist=$('#taskList',$key);
			   		 var arrs=new Array();
			   		 var arrs2=new Array();
			   		  tasklist[FD]('.ev_wCount')[EH](function(){
			   		  arrs2.push($(this)[TX]());
			   		  arrs.push($(this)[PT]()[AT]('id'));
			   		  });
				 var forhtml="<li  class=\"ev_wPlace\" id={0}  title=\"{2}\"><span class=\"ev_wCount\">{1}</span><span class=\"ev_wDel\"></span></li>";
				   html="";
				   for(var i=0;i<arrs.length;i++){
				     html+=$[FO](forhtml,arrs[i],arrs2[i],arrs2[i]);
				   }
				   var wadd=$('.event_Wadd',div);
				    wadd.html('')[AP]($(html));
				    $('.ev_wDel',wadd)[EV](CK,function(){
				    	var te=$(this)[PT]()[AT]('id');
				    	var  addressULs=  $('#addressCount_listID',div);
				    	$(':checked',addressULs)[EH](function (){
				    		 var chec=$(this)[AT]('id');
				    		 if(te==chec){
				    		  this[CD]=false;
				    		 return  false;
				    		 } 
				    	});
				     $(this)[PT]()[RM]();
				    });
				  		//下面已选择任务里有任务，则上面现实的checkbox为true
  	   		var wplaces={};
  	   		 var  ultemp =$('.event_Wadd',div);
  	   		 var  addressULs=  $('#addressCount_listID',div);
  	   		$('.ev_wPlace',ultemp)[EH](function (){
  	   			  wplaces[$(this)[AT]('id')]=$(this)[AT]('id');
  	   		});
				
				$('input',addressULs)[EH](function (){
						if(wplaces[$(this)[AT]('id')]!=undefined){
								this[CD]=true;
						}
				});  	   
				    
			   }
            }, //加载成功事件
            okSucceed: function () { 
            var  arrs=new Array();
            var arrs2=new Array();
            //检测内容当中的添加弹出层的确定。取消按钮功能
			$('#pushserivces_checkContentAdd_ok',div)[EV](CK,function(){
				$('.event_Wadd',div)[FD]('.ev_wCount')[EH](function(){
					  arrs.push($(this)[PT]()[AT]('id'));
					  arrs2.push($(this)[TX]());
				});				
				CloseWindow(div);
			     var format= "<li  class=\"ev_wPlace\"  id=\"{0}\"  title=\"{2}\"><span class=\"ev_wCount\">{1}</span><span class=\"ev_wDel\" ></span></li>";
			     var html="";
			     for(var i=0;i<arrs.length;i++){
			           html+=$[FO](format,arrs[i],arrs2[i],arrs2[i]);
			     }
			   
				add_taskList.html('')[AP]($('<ul class="event_Wadd"></ul>',$key)[AP]($(html)));
				//回到页面页面看 可以删除事件
			    $('.ev_wDel',add_taskList)[EV](CK,function(){
			    		$(this)[PT]()[RM]();
			    });
				
		      if(add_taskList[TX]()==""){
		         $('#checkcontent_prompt',$key)[RC]('com_none');
		       }else{
		       $('#checkcontent_prompt',$key)[AC]('com_none');
		       }
			});
            $('#pushserivces_checkContentAdd_cancel',div)[EV](CK,function(){
				CloseWindow(div);
			});
            
            } //点击确定,取消事件
		     	});
		     	// 三级菜单结束
		      		}
    			});
    	 }//日常监控结束
    	 else {
    	 	 var 	html='<tr><td><input type="checkbox" id="{0}"  class="pushservicesCheckBoxs"/></td> <td >{1}</td><td >{2}</td></tr>';
				 var tempHtml="";
			$(BY)[AJ]({
    		param: {action: 'pushservices.pushserviceslist',queryConditions:'handle'},
    		success: function (data) {
    			$[EH](data,function(i,item){
    				 tempHtml=tempHtml+$[FO](html,item.id,item.name,item.platformNames);
    			});
    			var bodyHTML='<div class="giveMe_do event push_show"><div class="push_showList"><table class="push_table web_table"><th  style="width:30px"><input type="checkbox" id="allcheckbox" /></th> <th >事件名称</th><th>平台</th>'+tempHtml+'</table></div>  <div class="push_ok"><div class="in_MsetCno" id="pushservices_cancel">取&nbsp;&nbsp;消</div><div class="in_MsetCok" id="pushservices_submit">确&nbsp;&nbsp;定</div></div><div class="com_clear"></div></div>';
    			 		//弹出层开始
				$[WW]({
		        css: { "width": "500px", "height": "auto"},
		        //event: 'no',
		        title: '选择事件名称',
		        content: bodyHTML,
		        id: 'thingsHandle',
		        onLoad: function(div) {
		      	//写自己的添加弹出层业务
				/**
			** 可能添加接受人按钮的时候，之前已经选择有的，
			*应该把状态还原
			*/
			if(add_taskList[TX]()!=""){
			 var objs=[];
			$('.ev_wPlace',add_taskList)[EH](function (){
					objs.push($(this)[AT]('id'));
			});
			//先判断看看表格里是否选择过，选择过的需要打上对钩
			var  tempObject={};
			for(var i =0;i<objs.length;i++){
						   $('.pushservicesCheckBoxs',div)[EH](function (){
								if(objs[i]==$(this)[AT]('id')){
											this[CD]=true;
											tempObject[objs[i]]=objs[i];
											return false;
								}
			   		});
				} //for结束
		
			if($('.pushservicesCheckBoxs',div).length==$(':checked',div).length){
				    $('#allcheckbox',div)[EH](function (){
				    this[CD]=true;
				    });
					}
			}
			
		      	//全选/取消全选按钮开始
		      	$('#allcheckbox',div)[EV](CK,function(){
		      		if($('#allcheckbox',div).is(':checked')){
		      			$('.pushservicesCheckBoxs',div)[EH](function (){
										this[CD] =TE;
		      			});
		      			}else{
		      			    			$('.pushservicesCheckBoxs',div)[EH](function (){
										//$(this)[AT]("checked",false);
										this[CD] =FE;
		      			});
		      			}
		      	});//全选/取消全选按钮结束
		
		//判断如果单个全部选中的各种情况
	    $('.pushservicesCheckBoxs',div)[EV](CK,function(){
	  	    var  num=data.length;
	 		var point=0;
			$('.pushservicesCheckBoxs',div)[EH](function(){
		if(this[CD]){
			    point+=1;		
		}
		});
		if(point==num){
			$('#allcheckbox',div)[EH](function(){
				this[CD]=TE;
			});
			}
			else{
						$('#allcheckbox',div)[EH](function(){
				this[CD]=FE;
			});
			}
	    }
	  );
		//判断如果单个全部选中的各种情况结束
		      	//确定 ，取消 操作
		      	$('#pushservices_submit',div)[EV](CK,function(){
		      	//触发  向下面多行文本框赋值
		      	var arr=[],
		      	       arr2=[];
		      	$('.pushservicesCheckBoxs',div)[EH](function(){
					if(this[CD]){
					arr.push($(this)[AT]('id'));
					arr2.push($(this)[PT]()[NT]()[TX]());
					}		      		
		      	});
		      	
		      	
		      	 SetOptionsReceviceMan(arr,arr2);
		      		PushServicesCloseWindows(div);
		      		//添加通讯录没有添加判断
					if( add_taskList[TX]()==""){
					    $('#checkcontent_prompt',$key)[RC]("com_none");
					}else{
					$('#checkcontent_prompt',$key)[AC]("com_none");
					}	      		
		      	});
		      	
		      	   	$('#pushservices_cancel',div)[EV](CK,function(){
		      			PushServicesCloseWindows(div);
		      	});
		      	
		      	//确定 ，取消 操作结束
		    //写自己的添加弹出层业务结束
		      		}//onLoad结束
    			});
		 		//弹出层结束
    		}
    	});
    	 
    	 }//事件监控结束
		   });//结束checkcontent
		 
		 }

//第二次点击推送服务初始化方法
	 function initDraw(){
	 		talkMe_eventDiv.show();
	 		service_tailsDiv.hide();
	 		NavigationInit();
	 		CheckSave();
	 		OnLoadmonitorList();
	 }


     //导航条初始化方法,
	function NavigationInit () {
		       	navigationBars.Empty()
		       	.Add(key, 'ToolBar', '预警服务')
			.Add(key, 'ToolBar', '推送服务')[oL]();
			
	}
	
		//根据推送服务类型获取类型名称，type: 推送服务类型
	function ParseName (type) {
		var tt = context.Enum.PushServicesType;
		switch (type) {
			case tt.EveryDayCheck: return '日常监测';//0
			case tt.EventCheck: return '事件监测';//1
			default: return '';
		}
	}
	
	//修改页面操作
	function  modifyShow(){
	navigationBars.Empty()
			.Add(key, 'ToolBar', '预警服务')
			.Add(key, 'ToolBar', '推送服务')
    		.Add(key, 'ToolBarTwo', '推送设置')[oL]();
		       	$('#pushtwitter_h1',$key).html('')[AP]($('<span class="span_title ">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;修改推送</span>'))
	}
	
	//页面下一步骤方法
	function nextPageShow(This,refresh,data){
			     var cuserDelTemp=cuser.Templates['yjfw_wbtsdel_1'];
			     var  cuserSwiTemp=cuser.Templates['yjfw_wbtsswitch_1'];
			     //有权限
			     if(cuserDelTemp!=UN&&cuserDelTemp!=""&&cuserSwiTemp!=UN&&cuserSwiTemp!=""){
			        This[FD]('tr').eq(1)[AP]('<th width="20%">启用状态</th><th width="10%">操作</th>');
			         This[FD]('.t2')[EH](function (){
			               //var idNUM=$[TM]($(this)[PT]()[FD]('.t0')[TX]());
			         	   var index=This[FD]('.t2')[ID]($(this));
			         	   var idNUM=data.rows[index].id;
			         	   $(this)[AT]({id:idNUM});
			         	   var DelTemp=$[FO](cuserDelTemp,idNUM);
			         	   var SwiTemp=$[FO](cuserSwiTemp,idNUM);
			         	   var addHTML=SwiTemp+DelTemp;
			         	   $(this).after(addHTML);
			         });    			  
			     }else if(cuserDelTemp!=UN&&cuserDelTemp!=""&&(cuserSwiTemp==UN||cuserSwiTemp=="")){
			             This[FD]('tr').eq(1)[AP]('<th width="10%">操作</th>');
			        	 This[FD]('.t2')[EH](function (){
			               //var idNUM=$[TM]($(this)[PT]()[FD]('.t0')[TX]());
			         	   var index=This[FD]('.t2')[ID]($(this));
			         	   var idNUM=data.rows[index].id;
			         	   $(this)[AT]({id:idNUM});
			         	   var DelTemp=$[FO](cuserDelTemp,idNUM);
			         	   $(this).after(DelTemp);
			         });    
			     }else if((cuserDelTemp==UN||cuserDelTemp=="")&&cuserSwiTemp!=UN&&cuserSwiTemp!=""){
			          This[FD]('tr').eq(1)[AP]('<th width="20%">启用状态</th>');
			          This[FD]('.t2')[EH](function (){
			               //var idNUM=$[TM]($(this)[PT]()[FD]('.t0')[TX]());
			         	   var index=This[FD]('.t2')[ID]($(this));
			         	   var idNUM=data.rows[index].id;
			         	   $(this)[AT]({id:idNUM});
			         	   var SwiTemp=$[FO](cuserSwiTemp,idNUM);
			         	   $(this).after(SwiTemp);
			         });    
			     }
			     //无权限
			     else{
			     This[FD]('.t1')[EH](function (){
			     	var  t1Txt=$(this)[TX]();
			     	$(this).css('cursor','');
			     	t1Txt='<a style="cursor:pointer"> '+t1Txt+'</a>';
			     	$(this).html(t1Txt);
			     });
			     This[FD]('.t2')[EH](function (){
			     	 var  pnam=$[TM]($(this)[TX]());
			     	 pnam= ParseName(parseInt(pnam));
			     	$(this)[TX](' '+pnam);
			     	var index=This[FD]('.t2')[ID]($(this));
			        var idNUM=data.rows[index].id;
			        $(this)[AT]({id:idNUM});
			     });
			     
			     //出现小手 点进去显示修改页面
			     $('.t1',This)[FD]('a')[EV](CK,function(){
			          //var suID=$[TM]($(this)[PT]().prev()[TX]());
						  var suID=$(this)[PT]()[NT]()[AT]('id');
						   $(BY)[AJ]({
   									param: {action: 'pushservices.pushserviceslist',queryConditions:'ModifyID',subID:suID},
    								success: function (data) {
    									 if(data.name==UN){
    									 		$[MB]({ content: '此条任务已经不存在了!', type: 2,isAutoClose: TE });
    									 		OnLoadmonitorList();
    									 }else{
		    								  modifyShow();
										 	  talkMe_eventDiv.hide();
										 	  service_tailsDiv.show();
											//开始赋值
				    						 RestPanel(1,data);
    									 }
    								}		  		
					    		  });
			     });//结束
			     
			     }//无权限结束
	
		 //只有更改状态按钮开始
		if((cuserDelTemp==UN||cuserDelTemp=="")&&cuserSwiTemp!=UN&&cuserSwiTemp!=""){
			 This[FD]('.t3')[EH](function (){
					var  t1TXT=$(this)[PT]()[FD]('.t1')[TX]();
					 t1TXT='<a style="cursor:pointer"> '+t1TXT+'</a>';
					 $(this)[PT]()[FD]('.t1').html(t1TXT);
			 });
			//出现小手 点进去显示修改页面
			     $('.t1',This)[FD]('a')[EV](CK,function(){
			         // var suID=$[TM]($(this)[PT]().prev()[TX]());
						   var suID=$(this)[PT]()[NT]()[AT]('id');
						   $(BY)[AJ]({
   									param: {action: 'pushservices.pushserviceslist',queryConditions:'ModifyID',subID:suID},
    								success: function (data) {
    									 if(data.name==UN){
    									 		$[MB]({ content: '此条任务已经不存在了!', type: 2,isAutoClose: TE });
    									 		OnLoadmonitorList();
    									 }else{
					    						  modifyShow();
											 	  talkMe_eventDiv.hide();
											 	  service_tailsDiv.show();
												//开始赋值
					    						 RestPanel(1,data);
    								   }
    								}		  		
					    		  });
			     });//结束	 
			     
			     $('.t2',This)[EH](function (){
						var  tempNum=  parseInt($[TM]($(this)[TX]()));
						tempNum= ParseName(tempNum);
						$(this)[TX](tempNum);
			     });
		     
		       $('.t3',This)[EH](function(){
		       	    //推送关闭0  开启 1
		 			 var index= $('.t3',This)[ID]($(this));
					//$(this)[TX]();    $[TM]($(this)[TX]())
					 var  tempUpdates=parseInt(data.rows[index].pushStatus );
					//$(this)[TX]('');
		       	 	 if(tempUpdates==1){
									$('#pushing_'+data.rows[index].id,$key)[RC]('push_type2')[AC]('push_type1');
						}else{
								$('#pushing_'+data.rows[index].id,$key)[RC]('push_type1')[AC]('push_type2');
						 }
						 		//列表刷新的时候再次判断是否 接受人or 任务 or 事件为空，任何一个为空，
								//需要让开关按钮关闭
							     var pushIDOne= $('#pushing_'+data.rows[index].id,$key)[AT]('id');
					    		   pushIDOne=pushIDOne.substring(8);
					    		   checkOnListState(This,pushIDOne);
					    $('#pushing_'+data.rows[index].id,$key)[EV](CK,function(){
					    		  var pushID= $('#pushing_'+data.rows[index].id,$key)[AT]('id');
					    			pushID=pushID.substring(8);
					    		  //先判断 是否接受人为空或者任务为空，两者只要其中一个为空，就不能让其推送
					    		// 0   是都不为空  反而则为1
					    		  $(BY)[AJ]({
   									param: {action: 'pushservices.pushserviceslist',queryConditions:'checkSubscriberStatus',subId:pushID},
    								success: function (data2) {
					    			   if(parseInt(data2)==0){
					    			var pushbtntype=		$('#pushing_'+data.rows[index].id,$key)[AT]('class');
						    	   var tt= 	pushbtntype.indexOf('push_type1');
						    	   var pushIndex;
						    		if(tt<0){
						    			$('#pushing_'+data.rows[index].id,$key)[RC]('push_type2')[AC]('push_type1');
						    			pushIndex=1;
						    		}else{
						    		$('#pushing_'+data.rows[index].id,$key)[RC]('push_type1')[AC]('push_type2');
						    		pushIndex=0;
						    		}
						    		  $(BY)[AJ]({
	   									param: {action: 'pushservices.pushserviceslist.state',queryConditions:'updateStatus',subId:pushID,isStop:pushIndex},
	    								success: function (data3) {
	    								}		  		
						    		  });  //updateStatus AJax结束
					    		  } // if
					    		  else {
					    		  $('#pushing_'+data.rows[index].id,$key)[RC]('push_type1')[AC]('push_type2');
					    		  	$[MB]({content: '此推送信息的接受人或任务或事件为空',type: 1}); 
					    		  } //else
					    		} // success  		  	 --checkSubscriberStatus结束	
					     });  //checkSubscriberStatus AJAX 结束
					    		  
					    		});  //EV结束
							//推送关闭0  开启1 结束		 
		       	});
		}
	     //只有更改状态按钮结束
	     
	     //只有删除按钮开始
		if(cuserDelTemp!=UN&&cuserDelTemp!=""&&(cuserSwiTemp==UN||cuserSwiTemp=="")){
			This[FD]('.tdOption')[EH](function (){
				 var  t1TXT=$(this)[PT]()[FD]('.t1')[TX]();
					 t1TXT='<a style="cursor:pointer"> '+t1TXT+'</a>';
					 $(this)[PT]()[FD]('.t1').html(t1TXT);
				//开始删除按钮设置
				var index=This[FD]('.tdOption')[ID]($(this));
				$(this)[AC]('event_do');
				//弹出层开始
				$('#pushservice_btn'+data.rows[index].id,$key)[EV](CK,function(){
					 var 	html='<div class="giveMe_do del_reSee"><div class="giveMe_doDel" >您确定要删除吗？</div> <div class="push_ok"><div class="in_MsetCno" id="cancel">取&nbsp;&nbsp;消</div><div class="in_MsetCok" id="ok">确&nbsp;&nbsp;定</div></div><div class="com_clear"></div></div>';
				$[WW]({
		        css: { "width": "440px", "height": "auto"},
		        //event: 'no',
		        //title: '序号:'+data.rows[index].id+'     推送任务',
		        title:'&nbsp;&nbsp;推送任务',
		        content: html,
		        id: '_pushservice'+data.rows[index].id,
		        onLoad: function(div) {
		     	SettingOption(div, data.rows[index].id,'#pushservice_btn'+data.rows[index].id);
		      		}
    					});
					});//弹出层结束
			});
		
				//出现小手 点进去显示修改页面
			     $('.t1',This)[FD]('a')[EV](CK,function(){
			          //var suID=$[TM]($(this)[PT]().prev()[TX]());
						  var suID=$(this)[PT]()[NT]()[AT]('id');
						   $(BY)[AJ]({
   									param: {action: 'pushservices.pushserviceslist',queryConditions:'ModifyID',subID:suID},
    								success: function (data) {
    									 if(data.name==UN){
    									 		$[MB]({ content: '此条任务已经不存在了!', type: 2,isAutoClose: TE });
    									 		OnLoadmonitorList();
    									 }
		    						  else{
				    						  modifyShow();
										 	  talkMe_eventDiv.hide();
										 	  service_tailsDiv.show();
											//开始赋值
				    						 RestPanel(1,data);
		    						 	}
    								}		  		
					    		  });
			     });//结束	 
		
				     $('.t2',This)[EH](function (){
						var  tempNum=  parseInt($[TM]($(this)[TX]()));
						tempNum= ParseName(tempNum);
						$(this)[TX](tempNum);
			     });
		}  //只有删除按钮结束
	
	
		//更改状态按钮,删除按钮都有权限开始
		if(cuserDelTemp!=UN&&cuserDelTemp!=""&&cuserSwiTemp!=UN&&cuserSwiTemp!=""){
				This[FD]('.tdOption')[EH](function (){
					var index=This[FD]('.tdOption')[ID]($(this));
						$(this)[AC]('event_do');
						//$(this)[AP]($('<div class="event_doBotton event_del" id="pushservice_btn'+data.rows[index].id+'"></div>',$key));
						$(this).css('cursor','');
						$(this)[PT]()[FD]('.t1').css('cursor','');
					 	 var td1TXT=$(this)[PT]()[FD]('.t1')[TX]();
						 td1TXT='<a style="cursor:pointer"> '+td1TXT+'</a>';
						 $(this)[PT]()[FD]('.t1').html(td1TXT);
						//开始图片画入
					     var temp=	$('#pushservice_btn'+data.rows[index].id,$key)[PT]()[PT]()[FD]('td').eq(2)[TX]()[TM]();
					     var tempnum=parseInt(temp);
						$('#pushservice_btn'+data.rows[index].id,$key)[PT]()[PT]()[FD]('td').eq(2)[TX](ParseName(tempnum));
						//推送关闭0  开启 1
						  var  pushimagehtml='<div class="push_type com_none" id="pushing_'+data.rows[index].id+'">{0}</div>';
					       $('#pushservice_btn'+data.rows[index].id,$key)[PT]()[PT]()[FD]('td').eq(3)[TX](data.rows[index].pushStatus);
					       var  temppushstatus=parseInt($('#pushservice_btn'+data.rows[index].id,$key)[PT]()[PT]()[FD]('td').eq(3)[TX]()[TM]());
					       	 $('#pushservice_btn'+data.rows[index].id,$key)[PT]()[PT]()[FD]('td').eq(3)[TX]('');
								if(temppushstatus==1){
								var	html = $[FO](pushimagehtml, '')
								 $('#pushservice_btn'+data.rows[index].id,$key)[PT]()[PT]()[FD]('td').eq(3)[AP]($(html));
									$('#pushing_'+data.rows[index].id,$key)[RC]('com_none')[AC]('push_type1');
								}else{
								  var	html = $[FO](pushimagehtml, '')
								  $('#pushservice_btn'+data.rows[index].id,$key)[PT]()[PT]()[FD]('td').eq(3)[AP]($(html));
								$('#pushing_'+data.rows[index].id,$key)[RC]('com_none')[AC]('push_type2');
								}
								//列表刷新的时候再次判断是否 接受人or 任务 or 事件为空，任何一个为空，
								//需要让开关按钮关闭
							     var pushIDOne= $('#pushing_'+data.rows[index].id,$key)[AT]('id');
					    		   pushIDOne=pushIDOne.substring(8);
					    		   checkOnListState(This,pushIDOne);
								//-------------
					    		$('#pushing_'+data.rows[index].id,$key)[EV](CK,function(){
					    		   var pushID= $('#pushing_'+data.rows[index].id,$key)[AT]('id');
					    		   pushID=pushID.substring(8);
					    		 //先判断 是否接受人为空或者任务为空，两者只要其中一个为空，就不能让其推送
					    		// 0   是都不为空  反而则为1
					    		     $(BY)[AJ]({
   									param: {action: 'pushservices.pushserviceslist',queryConditions:'checkSubscriberStatus',subId:pushID},
    								success: function (data2) { 
    								if(parseInt(data2)==0){
						    	   var pushbtntype=		$('#pushing_'+data.rows[index].id,$key)[AT]('class');
						    	   var tt= 	pushbtntype.indexOf('push_type1');
						    	   var pushIndex;
						    		if(tt<0){
						    			$('#pushing_'+data.rows[index].id,$key)[RC]('push_type2')[AC]('push_type1');
						    			pushIndex=1;
						    		}else{
						    		$('#pushing_'+data.rows[index].id,$key)[RC]('push_type1')[AC]('push_type2');
						    		pushIndex=0;
						    		}
						    		  $(BY)[AJ]({
	   									param: {action: 'pushservices.pushserviceslist.state',queryConditions:'updateStatus',subId:pushID,isStop:pushIndex},
	    								success: function (data3) {
	    								}		  		
						    		  });  //updateStatus AJax结束
									} //if
									else{
									$('#pushing_'+data.rows[index].id,$key)[RC]('push_type1')[AC]('push_type2');
									$[MB]({content: '此推送信息的接受人或任务或事件为空',type: 1}); 
									} //else
									} // success  		  	 --checkSubscriberStatus结束	
					        });  //checkSubscriberStatus AJAX 结束
					});  //EV 结束
							//推送关闭0  开启1 结束
						//结束图片画入
						
						//点击推送任务名进入修改页面
						 $('#pushservice_btn'+data.rows[index].id,$key)[PT]()[PT]()[FD]('td').eq(1)[FD]('a').eq(0)[EV](CK,function(){
						  //  var suID=$[TM]($(this)[PT]().prev()[TX]());
						  		var suID=$(this)[PT]()[NT]()[AT]('id');
						   $(BY)[AJ]({
   									param: {action: 'pushservices.pushserviceslist',queryConditions:'ModifyID',subID:suID},
    								success: function (data) {
		    						  	 if(data.name==UN){
    									 		$[MB]({ content: '此条任务已经不存在了!', type: 2,isAutoClose: TE });
    									 		OnLoadmonitorList();
    									 }
		    						  else{
					    						  modifyShow();
											 	  talkMe_eventDiv.hide();
											 	  service_tailsDiv.show();
												//开始赋值
					    						 RestPanel(1,data);
		    						 	}
    								}		  		
					    		  });
						 });
						//点击推送任务名进入修改页面结束
						
						//弹出层开始
						$('#pushservice_btn'+data.rows[index].id,$key)[EV](CK,function(){
					 var 	html='<div class="giveMe_do del_reSee"><div class="giveMe_doDel" >您确定要删除吗？</div> <div class="push_ok"><div class="in_MsetCno" id="cancel">取&nbsp;&nbsp;消</div><div class="in_MsetCok" id="ok">确&nbsp;&nbsp;定</div></div><div class="com_clear"></div></div>';
				$[WW]({
		        css: { "width": "440px", "height": "auto"},
		        //event: 'no',
		       // title: '序号:'+data.rows[index].id+'     推送任务',
		        title: '&nbsp;&nbsp;推送任务',
		        content: html,
		        id: '_pushservice'+data.rows[index].id,
		        onLoad: function(div) {
		     	SettingOption(div, data.rows[index].id,'#pushservice_btn'+data.rows[index].id);
		      		}
    					});
						//开始点击删除按钮 开始删除
						  //$('#pushservice_btn'+data.rows[index].id,$key)[PT]()[PT]()[RM]();
						   
						});//弹出层结束
						
					});
			}//更改状态按钮,删除按钮都有权限结束
	}
	
					//列表刷新的时候再次判断是否 接受人or 任务 or 事件为空，任何一个为空，
				   //需要让开关按钮关闭
					function  checkOnListState(This,id){
								//		$('#pushing_'+id,This)
				    		 //先判断 是否接受人为空或者任务为空，两者只要其中一个为空，就不能让其推送
					    		// 0   是都不为空  反而则为1
					    		   $(BY)[AJ]({
   									param: {action: 'pushservices.pushserviceslist',queryConditions:'checkSubscriberStatus',subId:id},
    								success: function (data2) { 
									 if(parseInt(data2)==1){
									$('#pushing_'+id,This)[RC]('push_type1')[AC]('push_type2');
									 $(BY)[AJ]({
	   									param: {action: 'pushservices.pushserviceslist.state',queryConditions:'updateStatus',subId:id,isStop:0},
	    								success: function (data3) {
	    								}		  		
						    		  });  //updateStatus AJax结束
									
									} // if parseInt(data2)==1 结束
								} // success  		  	 --checkSubscriberStatus结束	
					        });  //checkSubscriberStatus AJAX 结束
			
		}  // checkOnListState   结束
		
		
	
	//画出有规则表格
	function OnLoadmonitorList(){
		var page=$('#allPager',$key);
		listdiv.add(page).html('');
				listdiv[TB]({
             width: 1000,
             trHeight: 24, //表格默认高
             pager: page,
             pageSize: 10,//显示条数
             isPager: TE, //是否分页
             id:'_m1d',
             rows: 'rows',
             param: {action :'pushservices.pushserviceslist',queryConditions:"queryAll"},
			 columns: [{ caption: '序号', field: 'id', type: 'number',width: '5%' },
                       { caption: '推送任务名称', field: 'pushservicesName', type: 'string', width: '35%' },//, style: 'cursor:pointer'
                       { caption: '类型', field: 'kinds', type: 'int', width: '15%' }],
                      // { caption: '更改状态', field: 'pushStatus', type: 'int', width: '20%' }],
           				// option: ['delete'],
                //加载表格成功事件
            onComplete: function ( This, refresh,data) {
            					if(data.rows.length==0){
									 base.not(listdiv);
								}else{
								  nextPageShow(This,refresh,data);
								}
            }
		});
	
	}
	
	function  SettingOption(div,id,btn){
			$('#ok', div)[EV](CK, function() {
    		CloseWindow(div);
    		  $(BY)[AJ]({
   				param: {action: 'pushservices.pushserviceslist.del',queryConditions:'deleteData',deleteID:id},
    			success: function (data) {
    			//删除开始 向数据库发送请求
    			//alert(data);
    			//$(btn,$key)[PT]()[PT]()[RM]();
    			OnLoadmonitorList();
    			}
			});
    	});
    	$('#cancel', div)[EV](CK, function() {
    		CloseWindow(div);
    	});
	}
	
	    //关闭窗体事件
    function CloseWindow(div) {
      div[RM]();
      $[RM]({ obj: '#' + JLY });
    }
	
	
    return {
    	IsLoad: FE,
        OnLoad: function (_ContentDiv) {
            var t = this;
			contentDiv = _ContentDiv;
            $('<div />').load('modules/alertService/twitterPush.htm', function () {
            	base = context.Base;
            	cuser=context.CurrentUser;
            	contentDiv[AP](base.FormatPmi($(this).html()));
            	t.IsLoad = TE;
                $key = $('#' + key);
	            navigationBars=context.NavigationBars;
	            listdiv= $('#listdiv',$key);
	            talkMe_eventDiv=$('.talkMe.event',$key);
	            service_tailsDiv=$('#service_tails',$key);
	           	add_taskList=   $('#taskList',$key);
		        pushserviceTwitterSendNum=$('#pushservice_twitter_sendNum',$key);
		        pushserviceTaskDate=$('#pushervices_twitter_dateTimeall',$key);
	            pushserviceTwitterSendDateTime=$('#pushservicesMails_prompt',$key);
	            pushserviceTwitterTaskName= $('#task_name',$key);
	            pushserviceTwitterTaskNamePrompt= $('#task_name_prompt',$key);
	            pushserviceTwitterTaskNameRepeat=$('#task_name_repeat',$key);
	            Init();
                
            });
            return t;
        },
        //显示模块方法
        Show:function() {
        	$key.show();
        	pushserviceEventHtml='',// 事件监控下面的 html
            pushserviceCheckContentHtml='',//  监测内容下的html
        	initDraw();
        },
        
        ToolBar:function(){
        	pushserviceEventHtml='',// 事件监控下面的 html
            pushserviceCheckContentHtml='',//  监测内容下的html
        	SameHandle();
        },
        
             ToolBarTwo:function(){
        }
        
    };
});