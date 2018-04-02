//任务通知
define(['portal'], function (context) {
    //定义变量
    var key = 'taskNotify',
          $key, //当前的作用域
          navigationBars,//工具栏标签
          listDiv,//列表table
          mediator,
          base,
          contentDiv;
          
    //私有方法
    //初始化方法，写自己模块的业务
    function Init(type) {
    	NavigationInit();
    	if(type==null|| type==undefined){
    	  type=-1;
    	}
    	OnLoadmonitorList(type);
    }
     //导航条初始化方法,
	function NavigationInit () {
		    	navigationBars.Empty()
			.Add(key, 'ToolBar', '协同办公')
    		.Add(key, 'ToolBar', '任务通知')[oL]();
	}
			
	 //优先级枚举判断，type: 优先级类型
	function ParseRank (type) {
		var tt = context.Enum.TaskNotifyRankType;
		switch (type) {
			case tt.RankHigh: return '高';//2 高级
			case tt.RankMiddle: return '中';//1  中级
			case tt.RankLow: return '低';//0  低级
			default: return '';
		}
	}
	
	//已读未读状态判断 type:已读未读int类型
	function ParseRead (type) {
		var tt = context.Enum.TaskNotifyStates;
		switch (type) {
			case tt.UnRead: return '未读';//0 未读
			case tt.HaveRead: return '已读';//1  已读
			default: return '';
		}
	}	
	
	function  ShowAddTaskNotify(){
	  
		    var bodyHTML='<div class="giveMe_do new_tongzhi"><div class="box_count"><div class="div_style">'+
		    '<label class="com_fLeft">优先级：</label><select class="task_count task_Cwdith2"><option value="2">高</option><option value="1">中</option><option value="0">低</option>'+
		    '</select></div><div class="div_mar"><label class="com_fLeft">接收人：</label><input     readonly="readonly"     type="text" value="" class="task_count task_Cwdith1"  id="nofity_remans_input"/>'+
		    '<div class="tWord_add" id="tasknotify_addBtn">添加</div>  <div class="com_clear"></div>  <div  id="taskNotify_remans" class="com_none"><label class="com_fLeft "  style="margin-left:56px">&nbsp;</label><font color="red">*没有添加接受人,请点击添加按钮选择</font></div> <div class="push_showList  push_showList2 event"></div>'+
		    ' </div><div class="com_clear"></div><div class="div_mar"> <label class="com_fLeft" for="tz_content">内&nbsp;&nbsp;&nbsp;容：</label><textarea class="tongzhi_content" id="tz_content">请输入内容</textarea>'+
		    '</div>  '
		    +'<div class="com_clear"></div><div class="com_none" id="taskNotify_contents"><label style="margin-left:56px" class="com_fLeft ">&nbsp;</label><font color="red">*内容不能为空</font></div>'
		    //不能输入特殊字符的提醒div
		    +'<div id="taskNotify_contentsSpeaical" class="com_none"><label class="com_fLeft " style="margin-left:56px">&nbsp;</label><font color="red">*内容不能输入特殊字符</font></div>'
		    
		    +'</div><div class="com_clear"></div>    <div class="push_ok"><div class="in_MsetCno" id="taskNotify_cancel">取&nbsp;&nbsp;消</div><div class="in_MsetCok" id="taskNotify_ok">确&nbsp;&nbsp;定</div></div><div class="com_clear"></div></div>';
		    	$[WW]({
		        css: { "width": "540px", "height": "auto"},
		        //event: 'no',
		        title: '新建通知',
		        content: bodyHTML,
		        id: 'taskNofity_add',
		        onLoad: function(div) {
					   AddTaskNotify(div);
					   ContextNotify(div);
		      		}//onLoad结束
    			});
	}
	
	//弹出层中内容的限制
	function    ContextNotify(div){
			var content=	$('#tz_content',div);
		 	 content.blur(function (){
		  	 if($[TM](content.val())==""){
		  	 		content.val("请输入内容");
		  	 }
		  });
		  $('#tz_content',div).focus(function (){
		  	if(content.val()=="请输入内容"){
		  		content.val('');
		  	}
		  	content[EV](KU,function (){
		  		$('#taskNotify_contentsSpeaical',div)[AC]('com_none');
		  	   $('#taskNotify_contents',div)[AC]('com_none');
		  	});
		  });
	}
	
	//添加任务通知功能
	function  AddTaskNotify(div){
				var  showaddList=$('.push_showList',div);
			    showaddList.hide();
    		    $('#tasknotify_addBtn')[EV](CK,function(){
    		    	 $('#nofity_remans_input',div).val('');
   					 showaddList.show();
    	  		     addAccountLoadList(showaddList);
    		});


    //添加接受人div画面
    function  addAccountLoadList(div){
    var  tempHtml="",
     		html='<tr><td><input type="checkbox" id="{0}"  class="pushservicesCheckBoxs"/></td> <td >{1}</td><td>{2}</td></tr>';
	$(BY)[AJ]({
    		param: {action: 'coordination.taskNotify.addReceiveMan', queryConditions:'recevieManMethod'},
    		success: function (data) {
    			$[EH](data,function(i,item){
    				 tempHtml=tempHtml+$[FO](html,item.id,item.sendName,item.totifyContent);
    			});
    			var bodyHTML='<table class="push_table web_table"><th><input type="checkbox" id="allcheckbox" /></th> <th>接收人</th><th>邮箱</th>'+tempHtml+'</table>';
    			 	div.html('')[AP]($(bodyHTML));
    			//全选/取消全选按钮开始
		      	$('#allcheckbox')[EV](CK,function(){
					 $('#taskNotify_remans')[AC]('com_none');		      	
		      		if($('#allcheckbox').is(':checked')){
		      		 var value='';
		      			$('.pushservicesCheckBoxs')[EH](function (){
										this[CD] =TE;
										   value+=$(this)[PT]()[NT]()[TX]()+'; ';
		      			});
		      							//向input里面添加接受人
										$('.task_Cwdith1')[EH](function(){
										   $(this).val(value);
										});
		      			}else{
		      			    			$('.pushservicesCheckBoxs')[EH](function (){
										//$(this)[AT]("checked",false);
										this[CD] =FE;
		      			});
		      			//向input里面取消添加接受人
		      							$('.task_Cwdith1')[EH](function(){
										   $(this).val('');
										});
		      			}
		      	});//全选/取消全选按钮结束
		//判断如果单个全部选中的各种情况
	    $('.pushservicesCheckBoxs')[EV](CK,function(){
	  	    var  num=data.length;
	 		var point=0;
			var  value='';
		   $('#taskNotify_remans')[AC]('com_none');
			$('.pushservicesCheckBoxs')[EH](function(){
			if(this[CD]){
			    point+=1;		
			   	//向input输入框输入接受人
			   	value+=$(this)[PT]()[NT]()[TX]()+'; ';
		}
		});
					$('.task_Cwdith1')[EH](function(){
										   $(this).val(value);
					});
		if(point==num){
			$('#allcheckbox')[EH](function(){
				this[CD]=TE;
			});
			}
			else{
						$('#allcheckbox')[EH](function(){
				this[CD]=FE;
			});
			}
	    });
		//判断如果单个全部选中的各种情况结束
		      	
		      	
    		}//success 结束
    		});
}
	
	  //确定
	  $('#taskNotify_ok')[EV](CK,function(){
	   var  ar=[];
	    var  te= $('#nofity_remans_input',div).val();
	    te=$[TM](te);
	    if(te==""){
	     $('#taskNotify_remans',div)[RC]('com_none');
	      ar.push(1);
	    }
	    if($[TM]($('#tz_content',div).val())=="" ||$[TM]($('#tz_content',div).val())=="请输入内容"  ){
	    	$('#taskNotify_contents',div)[RC]('com_none');
	      ar.push(2);
	    }else{
	    //通知中的内容不能输入特殊字符
	  	 var regu = /^[0-9a-zA-Z\u4e00-\u9fa5]+$/;      
   		  if (!regu.test( $[TM]($('#tz_content',div).val()) )) {   
   				 $('#taskNotify_contentsSpeaical',div)[RC]('com_none');
     			 ar.push(2);
    		}
	    }
	    
	  if(ar.length>0){
				  return ;
	  }
	 	 //优先级
	 	var priority=	 $('.task_count.task_Cwdith2',div)[FD]('option:selected').val();
	 	//接受人
	 	var  flag=false;
	 	var  arrs='';
	 	$('#allcheckbox',div)[EH](function (){
	 			  flag=  this[CD];
	 	});
	 	
	  	  $(':checked',div)[EH](function (i,item){
	  	  	if(i==0){}
	  	  	else if(i==1){
	  	  			if(!flag){
	  	  			arrs+=$[TM]($(this)[AT]('id'))+',';
	  	  				}
	  	  			}else{
				    arrs+=$[TM]($(this)[AT]('id'))+',';
	  	  			}
	  	  });
	  	  
	  	  //任务通知当中的内容
	  	  var  cons=$('#tz_content',div).val();
	  	  
	  	  //向数据库发送 ajax请求
	  		$(BY)[AJ]({
	  			param: {action: 'coordination.taskNotify.add',queryConditions:'taskNotifyAdd',pri:priority ,array:arrs,taskNotifysContents:cons},
    			success: function (data) {
    					//alert(data);
    					//如果向自己发自己的话，上面的邮件+1  data 为1 为自己给自己发 0 则自己给自己发
    					if((data+'')=='1'){
    					 mediator.AddMessageCount();
    					}
    					OnLoadmonitorList(-1);
    				}
	  		});
	  
	  	CloseWindow(div);
	  });
	  //取消
	  $('#taskNotify_cancel')[EV](CK,function(){
	  		CloseWindow(div);
	  });
	
	}
	
	
	//加载列表  status  0未读  1全部（包括已读和未读）
	function  OnLoadmonitorList(status){
			 var pager=$('#allPager',$key);
		 	 listDiv.add(pager).html('');
		 	 listDiv[TB]({
             width: 1000,
             trHeight: 24, //表格默认高
             pager: pager,
             pageSize: 10,//显示条数
             isPager: TE, //是否分页
             id:'_depar',
             rows: 'rows',
             param: {action :'coordination.taskNotify', queryConditions:'queryList',statu:status},
			 columns: [{ caption: '序号', field: 'id', type: 'number',width: '5%' },
                       { caption: '内容', field: 'totifyContent', type: 'string', width: '40%' ,style: 'cursor:pointer'},
                       { caption: '发送人', field: 'sendName', type: 'string', width: '10%' },
                       { caption: '优先级', field: 'rank', type: 'int', width: '10%' },
                       { caption: '状态', field: 'states', type: 'int', width: '10%' },
                       { caption: '发送时间', field: 'publicDate', type: 'string', width: '15%' },
                       { caption: '操作', field: '', type: 'html', width: '15%',template:'',className:'tdOption' }],
                       	// option: ['delete'],
                //加载表格成功事件
            onComplete: function ( This, refresh,data) {
            //取消t1整列的小手
            $('.t1',$key)[EH](function (){
            	 $(this).css('cursor','');
            	 var  tetTemp=$(this)[TX]();
            	 tetTemp='<a style="cursor:pointer">'+tetTemp+'</a>';
            	 $(this).html(tetTemp);
            });
            
				//优先级枚举赋值
				$('.t3',$key)[EH](function (i,item){
					  var temp=   $(this)[TX]();
					  var changetemp=ParseRank(parseInt(temp));			     
					  $(this)[TX](changetemp);
				});
				 
				 $('.t4',$key)[EH](function(){
				   var temp=   $(this)[TX]();
				   var changetemp=ParseRead(parseInt(temp));
				     $(this)[TX](changetemp);
				 });
				 
				 $('.t5',$key)[EH](function (){
				 		  var temp=   $(this)[TX]();
				 		  temp=temp.substring(0,temp.lastIndexOf(":"))
				 		$(this)[TX](temp);
				 });
				 
				 //添加列表删除按钮
				  DeleteObject( This, refresh,data,status);
				 
				 $('#taskNotify_searchBtn',$key)[EV](CK,function(){
				 //添加弹出层
				 	      ShowAddTaskNotify();
				 });
					  //通知内容的步骤，出现小手 可以点击，然后出来弹出层
					   UpdateTaskNotifyShow(data ,status);
					   //无数据的情况下显示
					    if(data.rows.length==0){
							base.not(listDiv);
							if(parseInt($[TM]($('#taskNotifyCount')[TX]()))>0){
									$('#taskNotifyCount')[TX]('0')
							}
						}
					   
            }
		});
	}
	
	
			 //添加列表删除按钮
			function	  DeleteObject( This, refresh,data,status){
					This[FD]('.tdOption')[EH](function  (){
					    $(this).css('cursor','');
						var index=This[FD]('.tdOption')[ID]($(this));
						$(this)[AC]('event_do');
						$(this)[AP]($('<div class="event_doBotton event_del"   title="删除"   id="taskNotify_btn'+data.rows[index].id+'"></div>',This));
						$('#taskNotify_btn'+data.rows[index].id,$key)[EV](CK,function (){
					    var 	html='<div class="giveMe_do del_reSee"><div class="giveMe_doDel" >您确定要删除吗？</div> <div class="push_ok"><div class="in_MsetCno" id="cancel">取&nbsp;&nbsp;消</div><div class="in_MsetCok" id="ok">确&nbsp;&nbsp;定</div></div><div class="com_clear"></div></div>';
						$[WW]({
		        		css: { "width": "440px", "height": "auto"},
		        		//event: 'no',
				        //title: '序号:'+data.rows[index].id+'     任务通知',
				        title: '&nbsp;&nbsp;任务通知',
				        content: html,
				        id: '_taskNofityList'+data.rows[index].id,
				        onLoad: function(div) {
		        			SettingOption(div, data.rows[index].id,'#taskNotify_btn'+data.rows[index].id,status,This);  //data.rows[index].states
		      		}
    					});
						});
				});
			}
			
			
				function  SettingOption(div,id,btn,status,This){
			$('#ok', div)[EV](CK, function() {
			  	if($(btn,This)[PT]()[PT]()[FD]('.t4')[TX]()=="未读"){
			  	//上面已经不为0需要减1  否则还为0
			  	if($[TM]($('#taskNotifyCount')[TX]())>0){
					mediator.DecreaseMessageCount();			  	
			  	  }
				}
    		CloseWindow(div);
    		//删除开始 向数据库发送请求
    		$(btn,This)[PT]()[PT]()[RM]();
    		  $(BY)[AJ]({
   				param: {action: 'coordination.taskNotify.del',queryConditions:'deleteData',deleteID:id},
    			success: function (data) {
						OnLoadmonitorList(status);			
    			}  //sucess		  		
			});
    	});
    	$('#cancel', div)[EV](CK, function() {
    		CloseWindow(div);
    	});
	}
	
	
	  //通知内容的步骤，出现小手 可以点击，然后出来弹出层
	function UpdateTaskNotifyShow(data,status){
		$('.t1',$('#_depar',$key))[FD]('a')[EV](CK,function(){
		$this=$(this);
		var idex=$('.t1',$('#_depar',$key))[ID]($this[PT]());
			//ajax 向后台发送参数	
	       //var id=$[TM]($this[PT]().prev()[TX]());
	    var id=data.rows[idex].id;
			$(BY)[AJ]({
    		param: {action: 'coordination.taskNotify.update', queryConditions:'lookContent' , id:id},
    		success: function (data) {
					if(data.totifyContent==UN || data.totifyContent==''){
						  $[MB]({ content: '此条信息已经删除了!', type: 2,isAutoClose: TE });
						  if($this[PT]()[NT]()[NT]()[NT]()[TX]()=="未读"){
			  		          $this[PT]()[NT]()[NT]()[NT]()[TX]('已读');
		    				//上面已经不为0需要减1  否则还为0
			  				if($[TM]($('#taskNotifyCount')[TX]())>0){
							mediator.DecreaseMessageCount();			  	
			  	  		}
					}
						    OnLoadmonitorList(status);
					}    //  data.totifyContent==UN   end
					else{
					var bodyHTML='<div class="giveMe_do tongzhi_content"><div class="all_content">{0}</div><div class="push_ok">'+
					'<!--<div class="in_MsetCno" id="taskNotify_AllContent_Cancel">取&nbsp;&nbsp;消</div>--><div class="in_MsetCok" id="taskNotify_AllContent_OK">关&nbsp;&nbsp;闭</div></div><div class="com_clear"></div></div>';    		
    			bodyHTML=$[FO](bodyHTML,data.totifyContent);
    			$[WW]({
		        css: { "width": "440px", "height": "auto"},
		        //event: 'no',
		        title: '任务通知内容',
		        content: bodyHTML,
		        id: '_taskNotify_allContent',
		        onLoad: function(div) {
		      	//写自己的添加弹出层业务
				    TaskNotifyAllContent(div,$this,id,status);
		      		},
		      		onClose:function (div){
		      		if($this[PT]()[NT]()[NT]()[NT]()[TX]()=="未读"){
			  		$this[PT]()[NT]()[NT]()[NT]()[TX]('已读');
			  		//向后台发送 已读为1的信息
					$(BY)[AJ]({
		    		param: {action: 'coordination.taskNotify.update', queryConditions:'updateStatus' ,updateid:id},
		    		success: function (data) {
		    	//上面已经不为0需要减1  否则还为0
			  	if($[TM]($('#taskNotifyCount')[TX]())>0){
					mediator.DecreaseMessageCount();			  	
			  	  }
			  	  OnLoadmonitorList(status);
		    			}//success 结束
		    		});
			}
		      		}
    			});
    					}	  //   data.totifyContent  !=UN   end
    			
    		}//success 结束------------AJ  queryConditions:'lookContent'   end 标签
    		});
	});//_depar结束
	
	}
	
	
	//任务通知点击看内容功能
	function  TaskNotifyAllContent(div,$this,id,status){
		$('#taskNotify_AllContent_OK',div)[EV](CK,function(){
			  if($this[PT]()[NT]()[NT]()[NT]()[TX]()=="未读"){
			  		$this[PT]()[NT]()[NT]()[NT]()[TX]('已读');
			  					//向后台发送 已读为1的信息
			$(BY)[AJ]({
    		param: {action: 'coordination.taskNotify.update', queryConditions:'updateStatus' ,updateid:id},
    		success: function (data) {
    			//上面已经不为0需要减1  否则还为0
			  	if($[TM]($('#taskNotifyCount')[TX]())>0){
					mediator.DecreaseMessageCount();			  	
			  	  }
			  	  OnLoadmonitorList(status);
    			}//success 结束
    		});
    	
		}
				CloseWindow(div);
		});
			$('#taskNotify_AllContent_Cancel',div)[EV](CK,function(){
			 if($this[PT]()[NT]()[NT]()[NT]()[TX]()=="未读"){
					$this[PT]()[NT]()[NT]()[NT]()[TX]('已读');
			//向后台发送 已读为1的信息
			$(BY)[AJ]({
    		param: {action: 'coordination.taskNotify.update',  queryConditions:'updateStatus' ,updateid:id},
    		success: function (data) {
    		  	//上面已经不为0需要减1  否则还为0
			  	if($[TM]($('#taskNotifyCount')[TX]())>0){
					mediator.DecreaseMessageCount();			  	
			  	  }
			  	  OnLoadmonitorList(status);
    			}//success 结束
    		});
    	
			 }
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
    	//type 0 未读  1 全部
        OnLoad: function (_ContentDiv,type) {
            var t = this;
            contentDiv = _ContentDiv;
            $('<div />').load('modules/coordination/coordination.taskNotify.htm', function () {
            	contentDiv[AP]($(this).html());
                base = context.Base;
            	navigationBars=context.NavigationBars;
            	mediator=context.Mediator;
            	t.IsLoad = TE;
                $key = $('#' + key);
				listDiv=$('#listDiv',$key);
                Init(type);
            });
            return t;
        },
        
              ToolBar : function(){
            //toolbar上的标签可以触发事件
		  	Init(-1);
        },
        //显示模块方法
        Show:function(type) {
        	$key.show();
        	//NavigationInit();
        	$('#taskNofity_titleBar',$key)[AC]('zhao_back');
        	Init(type);
        },
        //未读显示
        UnRead: function (){
				this.Show(0);
       		//NavigationInit();
    		//OnLoadmonitorList(0);        	

        }
    };
});