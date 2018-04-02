/**
*事件监控 
*@author shaoqun
**/
define(['portal'], function (context) {
    //定义变量
    var key = 'eventMonitor',
          $key, //当前的作用域
          navigationBars,
          labelreview,
          tabItem,
          hour_24,
          day_7,
          listdiv,
          tps_exl,
          base,
          cuser,
          event_add,
          lookList,
          screenSelect,
          objhtml,
          orderSelect,
          keyviewdiv, 
          keyuserdiv,
          searchInfo,
          hot_keyword,  
          terrain_analy,
          but_search,
          dateEnd,
          dateStart,
          contentDiv; 

          
    //初始化方法       
    function Init(){
    	NavigationInit();
    	SwitchingMonitortabs();
    }
    
    //导航条初始化方法,
	function NavigationInit(){
    	navigationBars.Empty()
			.Add(key, 'NavToMP', '事件监控')[oL]();
    } 
    //事件分析导航条初始化方法,
	function NavigationInit_fx(){
    	navigationBars.Empty()
			.Add(key, 'NavToMP', '事件监控')
			.Add(key, 'NavToMP_fx', '事件分析')[oL]();
    } 
     //事件详情导航条初始化方法,
	function NavigationInit_xq(){
    	navigationBars.Empty()
			.Add(key, 'NavToMP', '事件监控')
			.Add(key, 'NavToMP_xq', '微博详情')[oL]();
    } 
    
     //获取选择账号UID
	function GetUID() {
		return $('#currentAccountInfo', $key)[AT]('uid');
	}
	//获取选择账号名称
	function GetUcodename() {
		return $('#currentAccountInfo', $key)[TX]();
	}
          
    //监控列表标签页切换
 	function SwitchingMonitortabs(){
   
    	$('#event_talkMe',$key).show();		//隐藏监控列表
		$('#event_details',$key).hide();	//隐藏事件详情
		$('#contentAnaly',$key).hide();		//隐藏分析模块下的内容分析
		$('#usesAnaly',$key).hide();		//隐藏分析模块下的用户分析
		$('#eventAnalyse',$key).hide();		//显示分析模块
		$('#analymodule',$key).hide();		//显示分析模块下的趋势分析
    	
    	labelreview[EV](CK,function(){
          	var This = $(this),type = labelreview[ID](This);
          	labelreview[RC]("zhao_back"); 				//删除样式
          	This[AC]("zhao_back");			 			//添加样式
          	OnLoadmonitorList(type);	    			//传标签编号
        }).eq(0)[TR](CK);
 	}      
 	
 	//加载事件监控列表
 	function OnLoadmonitorList(type){

 		var pager = $('#allPager', $key);
		listdiv.add(pager).html('');
		listdiv[TB]({
             width: 1000,
             trHeight: 24, 	  //表格默认高
             pager: pager,
             pageSize: 15, 	  //显示条数
             isPager: TE, 	  //是否分页
             isCheckBox: FE,  //是否有多选按钮
             id:'_m1',
             rows: 'rows',
             param: {action :'eventMonitor.monitorList',type: type,method:1},
			 columns: [{ caption: '序号', field: 'id', type: 'number',width: '5%'},
                       { caption: '话题名称', field: 'topicname', type: 'string', width: ''},
                       { caption: '今天', field: 'todaynum', type: 'int', width: '7%'},
                       { caption: '昨天', field: 'yesterdaynum', type: 'int', width: '7%'},
                       { caption: '全部', field: 'allnum', type: 'int', width: '7%'},
                       { caption: '开始时间', field: 'monitorStarttime', type: 'string', width: '12%'},
                       { caption: '结束时间', field: 'monitorEndtime', type: 'string', width: '12%'},
                       { caption: '操作', field: '', type: 'html', width: '15%', template:''}],
             //加载表格成功事件
            onComplete: function (This, refresh, data) {

				var insert_power = cuser.Templates['sjjk_adddata_1'];			//取得添加权限
				var update_power = cuser.Templates['sjjk_updatedata_1'];		//取得修改权限
				var delete_power = cuser.Templates['sjjk_deletedata_1'];		//取得删除权限
				var warn_power = cuser.Templates['yjfw_yjxxinsert_3'];			//取得预警权限
				var analy = '<div class="event_doBotton event_analy" title="分析" id="analye_btn{0}"></div>';

				//判断是添加权限
				if( insert_power != null && insert_power != ""){
										
					//添加事件
					event_add[EV](CK, function(){
						$(this)[AJ]({
					    	param:{action:'eventMonitor.addEvent',method:2},
					    	success:function(data1){
					    		// -1：到达添加次数，限制提示，0：插入失败，数据库错误提示，1：添加成功
								if(data1 == -1){
									$[MB]({ content: '添加失败,添加已到达上限!', type: 1});
								}else{
									 addeventLayer(type,0,'添加事件');
								}
					    	}
						});
					});
				}

				if(data.realcount == 0){
					base.not(This);
				}else{
					This[FD]('.t7')[EH](function (){
						var index=This[FD]('.t7')[ID]($(this));
						var id = data.rows[index].id;
						var _name = data.rows[index].topicname;
			
						//判断是修改权限
						if( update_power != null && update_power != ""){
							var upTemp = $[FO](update_power,id);
					        $(this)[AP](upTemp);								//追加权限按钮图片
							
							//点击修改 弹出界面，修改任务
							$('#edit_btn'+id,$key)[EV](CK,function(){
								//预判断
								$(this)[AJ]({
							    	param:{action:'eventMonitor.monitorList',method:4,id:id,type:type},
							    	success:function(data1){
							    		addeventLayer(type,id,'修改事件');		//弹出要修改层
							    	}
								});
							});	
						}
						
						//判断是删除权限
						if(delete_power != null && delete_power != ""){
							var delTemp = $[FO](delete_power,id);
					        $(this)[AP](delTemp);								//追加权限按钮图片
					         	   
							//点击删除 给出提示，删除事件任务
							delete_div('delete_btn'+id,id,type);
						}
						
						//判断是预警权限
						if( warn_power != null && warn_power != ""){
							var wTemp = $[FO](warn_power,id);
					        $(this)[AP](wTemp);								//追加权限按钮图片
					         	   
							//添加、修改 弹出界面，修改任务
							$('#warn_btn'+id,$key)[EV](CK,function(){
								//预判断
								$(this)[AJ]({
							    	param:{action:'eventMonitor.monitorList',method:4,id:id,type:type},
							    	success:function(data1){
							    		WarnService(type,id);
							    	}
								});
							});	
						}
						
						//追加公共图片
						var analyTemp = $[FO](analy,id);
						$(this)[AP](analyTemp);				//追加分析按钮图片
						
						//点击分析 查看分析结果
						$('#analye_btn'+id,$key)[EV](CK,function(){
							
							$(this)[AJ]({
								param:{action:'eventMonitor.monitorList',method:4,id:id,type:type},
									success:function(data1){
										NavigationInit_fx();									//展示事件分析导航条		
										SwitchingEventtabs(id,data.rows[index].topicname,type);	//分析标签切换
								}
							});
						});
					});
						//追加样式
						This[FD]('.t1')[EH](function(){
							var _index=This[FD]('.t1')[ID]($(this));
							var idd = data.rows[_index].id;
							var topicname = data.rows[_index].topicname;
							var allnum = data.rows[_index].allnum;
							
							$(this).html('');
							$(this)[AP]('<a gid="'+idd+'" class="com_fLeft" style="cursor:pointer">'+topicname+'</a><span id="id_'+idd+'" class="event_doBotton event_jiankongTips com_none"></span><span class="com_clear"></span>');
							var spanid = $("#id_"+idd,$(this));
							
							$(this)[AJ]({
		    					param:{action:'warnservices.warnservicesinsert',id:idd,moduleType:1,method: 1},
		    					success:function(data1){
		    						if(allnum > data1.weibo && data1.weibo != 0){
										spanid[RC]('com_none');
									}else{
										spanid[AC]('com_none');
									}
								}
							});
						});
						This[FD]('.t2')[EH](function(){
							var _index=This[FD]('.t2')[ID]($(this));
							var _id = data.rows[_index].id;
							var todaynum = data.rows[_index].todaynum;
							$(this).html('');
							$(this)[AP]('<a gid="'+_id+'" style="cursor:pointer">'+todaynum+'</a>');
						});
						This[FD]('.t3')[EH](function(){
							var _index=This[FD]('.t3')[ID]($(this));
							var _id = data.rows[_index].id;
							var yestnum = data.rows[_index].yesterdaynum;
							$(this).html('');
							$(this)[AP]('<a gid="'+_id+'" style="cursor:pointer">'+yestnum+'</a>');
						});
						This[FD]('.t4')[EH](function(){
							var _index=This[FD]('.t4')[ID]($(this));
							var _id = data.rows[_index].id;
							var allnum = data.rows[_index].allnum;
							$(this).html('');
							$(this)[AP]('<a gid="'+_id+'" style="cursor:pointer">'+allnum+'</a>');
						});
						
						
						//读取事件详情
						//$('tr').mouseover(function(){				//获取行触发样式
						This[FD]('tr')[EH](function(){	
							var tds = $(this)[FD]("td");			//行下的所有td 
						
							$(tds[1],$key)[EV](CK,function(){//事件名称
								var ids = $(this)[FD]('a')[AT]('gid');
								EventLoad(ids,0,type);
							});
							$(tds[2],$key)[EV](CK,function(){//今天
								var ids = $(this)[FD]('a')[AT]('gid');
								EventLoad(ids,1,type);
							});
							$(tds[3],$key)[EV](CK,function(){//昨天
								var ids = $(this)[FD]('a')[AT]('gid');
								EventLoad(ids,2,type);
							});
							$(tds[4],$key)[EV](CK,function(){//全部
								var ids = $(this)[FD]('a')[AT]('gid');
								EventLoad(ids,0,type);
							});
						});
						
					//});
				}
            }
		});
 	}	
 	//预判断
 	function EventLoad(ids,strnum,type){
		$(this)[AJ]({
			param:{action:'eventMonitor.monitorList',method:4,id:ids,type:type},
				success:function(data){
					NavigationInit_xq();				//展示事件详情导航条	
					$('#event_talkMe',$key).hide();		//隐藏监控列表
					$('#event_details',$key).show();	//事件监控详情
					EventLoadList(ids,strnum,type);
			}
		});
 	}
 	
 	//---------监控列表操作开始----------------//
 	
 	//弹出添加(修改)事件层
 	function addeventLayer(type,edit_id,title){
 		var html = '<div class="giveMe_do show_eventDo"><div class="box_count"><div class="div_style"><label class="task_name">事件名称：</label><input type="text" class="task_count task_Cwdith2" readonly="" id="topicname" maxlength="20"/></div><div style="margin-left:100px;float:left;color:red;font-size: 12px;" id="eventname"></div><div class="com_clear"></div><div class="div_style"><label class="task_name">关键词：</label><input type="text" class="task_count task_Cwdith1" maxlength="6" id="addkword_y"/><div class="tWord_add" id="add_ykword">添加</div></div><div style="margin-left:100px;float:left;color:red;font-size: 12px;" id="keyword_ynull"></div><div class="com_clear"></div><ul class="event_Wadd" id="split_jointy" maxlength="6"></ul><div class="div_style"><label class="task_name">不包含关键词：</label><input type="text" class="task_count task_Cwdith1" id="addkword_n" maxlength="6"/><div class="tWord_add" id="add_nkword">添加</div></div><div style="margin-left:100px;float:left;color:red;font-size: 12px;" id="keyword_nnull"></div><div class="com_clear"></div><ul class="event_Wadd" id="split_jointn"></ul><div class="div_style"><label class="task_name">监控时间：</label><input type="text" class="task_count task_Cwdith3" id="jk_dateStart" /><span class="span_mar">到</span><input type="text" class="task_count task_Cwdith3" id="jk_dateEnd" /></div></div><div class="push_ok"><div class="in_MsetCno" id="in_cancel">取&nbsp;&nbsp;消</div><div class="in_MsetCok" id="in_eventok">确&nbsp;&nbsp;定</div></div><div class="com_clear"></div></div>';
			$[WW]({
				css: { "width": "500px", "height": "auto"},
				title: title,
				content: html,
				id: 'addid',
				onLoad: function(div) {
				 	SettingOption(div,type,edit_id);
				}
			});
 	}

	//弹出添加预警服务层
 	function WarnService(type,warnid){

		//var html = '<div class="giveMe_do warning_up"><div class="push_ok"><div class="in_MsetCno" id="in_MsetCnoo">取&nbsp;&nbsp;消</div><div class="in_MsetCok" id="in_MsetCokk">确&nbsp;&nbsp;定</div>&nbsp;&nbsp;&nbsp;&nbsp;<div class="in_MsetCok com_none" style="margin-right:10px" id="cancelwarn">取消预警</div></div></div>';
		var html = '<div class="giveMe_do warning_up"><div class="div_style"><label class="task_name">微博达到：</label><input type="text" id="fowardct" class="task_count task_Cwdith3" maxlength="10"/><span class="com_fLeft">&nbsp;&nbsp;发送预警</span></div><div style="margin-left: 100px;float: left; color: red; font-size: 12px; display: block;"><span class="com_none" id="forwa_check"></span></div> <div class="com_clear"></div><div class="box_count"><div class="div_style warn_style"><label class="task_name">接收人：</label><div id="pt_select" class="mynum_list"><h2 id="_h2"><span id="span_id">请选择</span><span class="mynum_type mynum_click"></span></h2><ul id="newevent_lajiSel" class="com_none"></ul></div></div></div><div style="margin-left: 100px;float: left; color: red; font-size: 12px; display: block;"><span class="com_none" id="jsr_check"></span></div> <div class="com_clear"></div><div class="div_style"><label class="task_name">发送方式：</label><span id="sendout"><input type="checkbox"  name="yjSend_way" class="com_fLeft yjSend_wayMar alertCheck" value="0" disabled="disabled"/> <span  class="com_fLeft com_rMar">系统消息</span><input type="checkbox"  name="yjSend_way" class="com_fLeft yjSend_wayMar alertCheck" value="1"/> <span  class="com_fLeft com_rMar">邮件</span><input type="checkbox"  name="yjSend_way" class="com_fLeft yjSend_wayMar alertCheck" value="2"/> <span  class="com_fLeft com_rMar">手机短信</span></span></div><div  style="margin-left:1px;margin-top: 10px; float: left; color: red; font-size: 12px; display: block;"><span class="com_none" id="send_check"></span></div> <div class="com_clear"></div><div class="push_ok"><div class="in_MsetCno" id="in_MsetCnoo">取&nbsp;&nbsp;消</div><div class="in_MsetCok" id="in_MsetCokk">确&nbsp;&nbsp;定</div><div class="in_MsetCok com_none" style="margin-right:10px" id="cancelwarn">取消预警</div></div><div class="com_clear"></div></div>';	
			$[WW]({
				css: { "width": "500px", "height": "auto"},
				title: '预警服务',
				content: html,
				id: 'warnid',
				onLoad: function(div,close) {
				
				 	 var saveBtn = $("#in_MsetCokk",div),cancelBtn = $("#in_MsetCnoo",div),
				 	 	 fowardct = $("#fowardct",div),pt_select = $("#pt_select",div),
				 	     lajiSel = $("#newevent_lajiSel",div),h2 = $("#_h2",div),
				 	     sendcheck = $("#send_check",div),jsrcheck = $("#jsr_check",div),
				 	     forwacheck = $("#forwa_check",div),str = new Array(),id = 0,
				 	     sendout = $("#sendout",div),cancelwarn = $("#cancelwarn",div);
					 var alertCheck = $('.alertCheck', div);
			    		 alertCheck[0][CD] = TE;   
				 	 //光标落下事件
				 	 fowardct[EV](KU, function() {
				    	var $this = $(this),
				    		txt = $[TM]($this.val());
				    	if(txt != '' && !$.regNumber(txt)) {
				    		$this.val('');
				    	}
				    	if (txt != '' && txt == '0') {
				    		$this.val('');
				    	}
				     });

				 	 //预警添加or编辑
				 	 $(div)[AJ]({
    					param:{action:'warnservices.warnservicesinsert',id:warnid,moduleType:1,method: 1},
    					success:function(data){
								if(data.id == 0){
									$("#fowardct").txt({text: '请输入' });
									Therecipient(lajiSel,h2,'',div);
								}else{
									id = data.id;
									$("#fowardct").val(data.weibo);
									Therecipient(lajiSel,h2,data.receiver,div);
									if(data.type != ""){
										str = data.type.split(",") ;    //分割字符串
										sendout[FD]("input")[EH](function(){
											var _this = $(this),value = this.value;
											$.each(str, function (index, tx) { 	  	//循环分割的字符串并赋值
												if(tx == value){
													_this[0].checked = true;
												}
							    			});
										});
									}
									
									//取消预警
									cancelwarn.show();
							 	 	cancelwarn[EV](CK,function(){
							 	 		$(BY)[AJ]({
											param: {action: 'warnservices.warnservicesinsert', id:id,moduleType:1,method: 4 },
													success: function(data){
														if(data == -1){
															close();
															$[MB]({ content: '预警服务取消失败!', type: 2});
														}else{
															close();
															OnLoadmonitorList(type);
														}
											}
										});
							 	 	});
								}
    					}
    				 });

				 	 saveBtn[EV](CK,function(){//保存

				 	 	forwacheck.html('');
				 	 	sendcheck.html('');
				 	 	jsrcheck.show('');
				 	 	
				 	 	var fowardnum = $.trim(fowardct.val()),reg = new RegExp("^[0-9]*$"),
				 	 		result = [],result2 = [];
				 	 	 
				 	 	$("#sendout input")[EH](function(){
						    if($(this)[0].checked){
						    	result.push(this.value);
							}
						});
						$("#newevent_lajiSel input")[EH](function(i) {
					    	if($(this)[0].checked){
					    		result2.push(this.value);
					    	}
					    });
					    var uii = result.join(",");
					    var rids = result2.join(",");

				 	 	//验证判断
				 	 	if(fowardnum == "" || fowardnum == "请输入"){
				 	 	 	forwacheck.show();
							forwacheck[AP]("* 请输入微博数");
				 	 	}else if(fowardnum != "" && fowardnum.split('')[0] == '0'){
				 	 	 	forwacheck.show();
							forwacheck[AP]("* 微博开头数不能为0");
				 	 	}else if(rids == ""){
				 	 	 	forwacheck.hide();
				 	 	 	jsrcheck.show();
				 	 	 	jsrcheck.html("* 请选择接收人");
				 	 	}else if(uii == ""){
				 	 	 	jsrcheck.hide();
				 	 	 	sendcheck.show();
				 	 	 	sendcheck[AP]("* 请选择发送方式");
				 	 	}else{
				 	 	 	if(id == 0){
					 	 	 	$(BY)[AJ]({
									param: {action: 'warnservices.warnservicesinsert', eventid:warnid,weibnum: fowardnum,receiver:rids,send:uii,moduleType:1,method: 2},
									success: function(data){
										if(data == -1){
											close();
											$[MB]({ content: '预警服务添加失败!', type: 2});
										}else{
											close();
											OnLoadmonitorList(type);
										}
									}
								});
							}else{
								$(BY)[AJ]({
									param: {action: 'warnservices.warnservicesinsert',id:id,eventid:warnid,weibnum: fowardnum,receiver:rids,send:uii,moduleType:1,method: 3},
									success: function(data){
										if(data == -1){
											close();
											$[MB]({ content: '预警服务编辑失败!', type: 2});
										}else{
											close();
											OnLoadmonitorList(type);
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
 
	 function Therecipient(sel,h2,arr,div){//匹配接收人
	 	sel.html('');
    	sel[AJ]({
    		param:{action:'sysManager.GetTheUser'},
    		success:function(data){
    			var lihtml='<li id="alert_id"><input type="checkbox" name="lajisel_group" id="fail_w{0}" class="lajis" value="{0}" {2} /><label title="{1}" id="alert_id" for="fail_w{0}">{1}</label></li>';
				if(data.length>0){
		    		sel.append('<li id="alert_id"><input type="checkbox" id="fail_w0" name="lajisel_group" value="0" /><label id="alert_id" for="fail_w0">全部</label></li>');
		    		var allinput = $("#fail_w0");
		    		allinput[EV](CK,function(){
			    		var checkboxs = document.getElementsByName("lajisel_group");
			    		var flag = checkboxs[0].checked;
			    		for (var i=0;i<checkboxs.length;i++) {
			 				var e=checkboxs[i];
			 				if(i!=0){
			  					e.checked=flag;
			  				}
		 				}
		    		});
    			}
    			var str = new Array();
    			if(arr != ""){
    				str = arr.split(",") ;    //分割字符串
    			}
    			$.each(data,function(i,item){
    				var ischecked =false;
    				if(str.length != 0){
	    				$.each(str,function(y,tx){
	    					if(tx == item.id){
	    						ischecked =true;
	    					}
	    				});
    				}
    				if(!ischecked)
    				sel.append($[FO](lihtml,item.id,item.useName,''));
    				else
    				sel.append($[FO](lihtml,item.id,item.useName,'checked="checked"'));
    			});

    			var state = FE;
				sel[EV](CK, function(e) {
					if(e.target.id == 'alert_id' || e.target.id.indexOf('fail_w') > -1) {
						state = TE;
						sel[RC]("com_none");;
					}else {
						sel[AC]("com_none");
						state = FE;
					}
				});
				div[EV](CK, function(e) {
					if(e.target.id != '_h2' && e.target.id != 'span_id' && !state) {
						sel[AC]("com_none");
					}else {
						if(!state){
							var styleflag= sel[AT]("class");//获取样式是否隐藏
							if(styleflag == "com_none"){
								sel[RC]("com_none");
							}else{
								sel[AC]("com_none");
							}
						}
					}
					state = FE;
				});
    		}
    	});
    }

 	//点击删除 给出提示，删除事件任务
 	function delete_div(th_id,del_id,type){
 		$('#'+th_id,$key)[EV](CK,function(){
			$[MB]({
				  	content: '您确定要删除吗？',
					type: 4,
					onAffirm: function (state) { 
						if(state == true){
							$(BY)[AJ]({
									param: {action: 'eventMonitor.deleteEvent', id: del_id,type: type,method: 'delete_data' },
									success: function(data){
										OnLoadmonitorList(type);
									}
							});
						}
					}
			}); 				
		});
 	}

 	
 	//添加（修改）事件任务   div：窗体div
	function SettingOption(div,type,edit_id){

    	$('#split_jointy').hide();					  		//隐藏包含关键词层
    	$('#keyword_ynull').hide();					  		//隐藏包含提示层
    	$('#split_jointn').hide();					 		//隐藏不包含关键词层
    	$('#keyword_nnull').hide();					  		//隐藏不包含提示层
    	
		add_keyword_yes(div);						  		//添加事件  添加关键词
		add_keyword_no(div);						 		//添加事件  添加不包含关键词
 
		//注：focusin 光标进入  focusout为光标离开
    	$('#addkword_y').focusin(function() {				//隐藏事件名称提示层
    		$('#eventname').hide();	
    	});
    	$('#addkword_n').focusin(function() {				//隐藏包含关键词提示层
    		$("#addkword_y").val("");
    		$('#keyword_ynull').hide();
    	});
    	
    	/*该方法不需要了
    	$('#t_prov').focusin(function() {					//获取省直辖市的集合  		
	    	$(BY)[AJ]({
					param: {action: 'eventMonitor.updateEvent', type: type,method: 'Location_prov_data' },
							success: function(data){
							var RoleOption ='<option value="{0}" {2} >{1}</option>';
							$.each(data,function(i,item){
					    		$('#t_prov').append($[FO](RoleOption,item.city,item.city,''));
					        });
					}
			});
    	});

    	$('#t_prov').focusin(function() {					//隐藏包含关键词提示层
    		$("#addkword_n").val("");						//并获取地点值
    		$('#keyword_nnull').hide();
    		$("#t_prov").change(function(){
				var prov = this.value;
				//清空下拉列表三级的数据
				$("#t_city option").each(function(index,domEle){
					if(index != 0){
						$(this).remove();
					}
				});
				$(BY)[AJ]({//获取城市区的集合
					param: {action: 'eventMonitor.updateEvent', type: type,prov: prov,method: 'Location_city_data' },
							success: function(data){
							var RoleOption ='<option value="{0}" {2} >{1}</option>';
							$.each(data,function(i,item){
								if(i == 0){
					    			$('#t_city').append($[FO](RoleOption,item.code,item.city,'selected="selected"'));
					    		}else{
					    			$('#t_city').append($[FO](RoleOption,item.code,item.city,''));
					    		}
					        });
					}
				});
			});
    	});
    	*/
    	
		add_data(div,type,edit_id);	
    	if(edit_id == 0){											//添加操作   		
    		$('#topicname').removeAttr("readonly");								
    		$('#topicname').txt({text: '请输入事件的名称' });			//设置默认值
    		DateTimeOption('jk_dateStart','jk_dateEnd');  			//监控时间赋值
    	}else{														//编辑操作			
    		
    		//异步交互 读取要编辑的对象
			$(BY)[AJ]({
					param: {action: 'eventMonitor.updateEvent', data_id: edit_id,method:1},
					success: function(data){//alert($.toJson(data));
						var myDate = new Date();
				    	var tday="",month="";
				    	if(myDate.getMonth()<9){month="0"+(myDate.getMonth()+1)}else{month=myDate.getMonth()+1}
				    	if(myDate.getDate()<10){tday="0"+myDate.getDate()}else{tday=myDate.getDate()}
						var tadayTime = myDate.getFullYear()+'-'+month+'-'+tday;
						//修改事件赋值
						$("#topicname").val("")[AT]("readonly","readonly");
						$('#jk_dateStart').datePicker({minDate:tadayTime});
    					$('#jk_dateEnd').datePicker({minDate:tadayTime});
						$("#topicname").val(data.name);
						$("#jk_dateStart").val(data.startTime);
						$("#jk_dateEnd").val(data.endTime);
						
						/* 该方法不需要了
						var t_prov = data.prov;		//获取省直辖市
						var t_city = data.city; 	//获取省市区

						$(BY)[AJ]({	//获取省直辖市的集合
							param: {action: 'eventMonitor.updateEvent', type: type,method: 'Location_prov_data' },
									success: function(data2){
									var RoleOption ='<option value="{0}" {2} >{1}</option>';
									$.each(data2,function(i,item){
					    				if(item.city == t_prov){
					    					$('#t_prov').append($[FO](RoleOption,item.city,item.city,'selected="selected"'));
					    				}else{
					    					$('#t_prov').append($[FO](RoleOption,item.city,item.city,''));
					    				}
					    			});
							}
						});

						//获取城市区的集合
						$(BY)[AJ]({
							param: {action: 'eventMonitor.updateEvent', type: type,prov: t_prov,method: 'Location_city_data' },
									success: function(data3){	
										var RoleOption ='<option value="{0}" {2} >{1}</option>';
										$.each(data3,function(i,item2){
						    				if(item2.city == t_city){
						    					$('#t_city').append($[FO](RoleOption,item2.code,item2.city,'selected="selected"'));
						    				}else{
						    					$('#t_city').append($[FO](RoleOption,item2.code,item2.city,''));
						    				}
						    			});
									}
						});
						*/

						if(data.keywords != ""){	//包含关键词
							
							$('#split_jointy').show();			  //展现包含关键词层
	    					$('#keyword_ynull').hide();			  //隐藏包含提示层
	    					var str = data.keywords.split(",") ;  //分割字符串
	    					
							$.each(str, function (index, tx) { 	  //循环分割的字符串并赋值
								var myDate = new Date();
								//添加到div的动作
					    		var html = '<li class="ev_wPlace"><span class="ev_wCount">'+tx+'</span><span class="ev_wDel"  id="del_spany'+index+'"></span></li>';
					    		$('#split_jointy')[AP](html);
					    
					    		//删除包含关键词	
								delete_keyword('split_jointy','keyword_ynull','del_spany'+index,div);

							});
						}
						
						if(data.notkeywords !=""){	//不包含关键词
						
							$('#split_jointn').show();			  		//展现不包含关键词层
				    		$('#keyword_nnull').hide();			  		//隐藏不包含提示层
				    		var str2 = data.notkeywords.split(",") ;    //分割字符串
				    	
				    		$.each(str2, function (index2, tx2) { 	  	//循环分割的字符串并赋值
				    			var myDate2 = new Date();
				    			//添加到div的动作
						    	var html2 = '<li class="ev_wPlace"><span class="ev_wCount">'+tx2+'</span><span class="ev_wDel" id="del_span'+index2+'"></span></li>';
						    	$('#split_jointn')[AP](html2);
						    	
						    	//删除不包含关键词		
								delete_keyword('split_jointn','keyword_nnull','del_span'+index2,div);
				    		});
						}
					
					}
			});
    	}
	}
	
	//添加事件  添加关键词
	function add_keyword_yes(div){
		
		$('#add_ykword',div)[EV](CK, function() {
    		var keyword_y = $.trim($("#addkword_y").val());   //获取文本的值
    		var myDate = new Date();
   			var j = 0;
			if($('#split_jointy li').length < 5){	
	    		if(keyword_y != ""){
	    			if(!$.regNorm4(keyword_y)){
	    				$('#keyword_ynull').show();			  //展现包含提示层
		    			var html = '<span class="ev_wCount">* 关键词不能包含无效字符</span>';
		    			$('#keyword_ynull').html(html);		  //生成span
	    			}else{
	    				if($('#split_jointy li span ').length == 0){
	    					$('#split_jointy').show();			  //展现包含关键词层
					    	$('#keyword_ynull').hide();			  //隐藏包含提示层
					    	//添加到div的动作
						    var html = '<li class="ev_wPlace"><span class="ev_wCount">'+keyword_y+'</span><span class="ev_wDel"  id="del_spany'+myDate.getMilliseconds()+'"></span></li>';
						    $('#split_jointy')[AP](html);
						    $("#addkword_y").val("");		      //清空文本的值
	    				}else{
	    					
		    				$('#split_jointy li span ')[EH](function(){	
								if(keyword_y == $(this).html()){
									$('#keyword_ynull').show();			  //展现包含提示层
						    		var html = '<span class="ev_wCount">* 词组不允许重名</span>';
						    		$('#keyword_ynull').html(html);		  //生成span
						    		return false;
								}	
								j++;
							});
							if(j == $('#split_jointy li span ').length){
								$('#split_jointy').show();			  //展现包含关键词层
						    	$('#keyword_ynull').hide();			  //隐藏包含提示层
						    	//添加到div的动作
							    var html = '<li class="ev_wPlace"><span class="ev_wCount">'+keyword_y+'</span><span class="ev_wDel"  id="del_spany'+myDate.getMilliseconds()+'"></span></li>';
							    $('#split_jointy')[AP](html);
							    $("#addkword_y").val("");		      //清空文本的值
							}
						}
		    		}
		    		//删除包含关键词	
					delete_keyword('split_jointy','keyword_ynull','del_spany'+myDate.getMilliseconds(),div);
					
	    		}else{
	    			$('#keyword_ynull').show();			  //展现包含提示层
	    			var html = '<span class="ev_wCount">* 请输入关键词</span>';
	    			$('#keyword_ynull').html(html);		  //生成span
	    		}
    		}else{
    			$('#keyword_ynull').show();			  	  //展现包含提示层
	    		var html = '<span class="ev_wCount">* 最多设置5个包含关键词</span>';
	    		$('#keyword_ynull').html(html);		 	  //生成span
    		}
    	});
	}
	
	//添加事件  添加不包含关键词
	function add_keyword_no(div){
		
		$('#add_nkword',div)[EV](CK, function() {
    		var keyword_n = $.trim($("#addkword_n").val()); //获取文本的值
			var myDate = new Date();
			var j = 0;
			if($('#split_jointn li').length < 5){
	    		if("" != keyword_n){
	    			if(!$.regNorm4(keyword_n)){
	    				$('#keyword_nnull').show();			   //展现不包含提示层
		    			var html = '<span class="ev_wCount" id="rc_span">* 不包含关键词不能包含无效字符</span>';
		    			$('#keyword_nnull').html(html);		
	    			}else{
	    				if($('#split_jointn li span ').length == 0){
		    				$('#split_jointn').show();			  //展现不包含关键词层
						    $('#keyword_nnull').hide();			  //隐藏不包含提示层
						    			
						    //添加到div的动作
						    var html = '<li class="ev_wPlace"><span class="ev_wCount">'+keyword_n+'</span><span class="ev_wDel" id="del_span'+myDate.getMilliseconds()+'"></span></li>';
							$('#split_jointn')[AP](html);
							$("#addkword_n").val("");			  //清空文本的值
						}else{
							$('#split_jointn li span ')[EH](function(){
								if(keyword_n == $(this).html()){
									$('#keyword_nnull').show();			  
						    		var html = '<span class="ev_wCount" id="rc_span">* 词组不允许重名</span>';
						    		$('#keyword_nnull').html(html);		
						    		return false;
								}
								j++;
							});
							if(j == $('#split_jointn li span ').length){
								$('#split_jointn').show();			  //展现不包含关键词层
						    	$('#keyword_nnull').hide();			  //隐藏不包含提示层
						    			
						    		//添加到div的动作
							    var html = '<li class="ev_wPlace"><span class="ev_wCount">'+keyword_n+'</span><span class="ev_wDel" id="del_span'+myDate.getMilliseconds()+'"></span></li>';
							    $('#split_jointn')[AP](html);
							    $("#addkword_n").val("");			  //清空文本的值
							}
						}
		    		}
		    		//删除不包含关键词		
					delete_keyword('split_jointn','keyword_nnull','del_span'+myDate.getMilliseconds(),div);
					
	    		}else{
	    			$('#keyword_nnull').show();			   //展现不包含提示层
	    			var html = '<span class="ev_wCount" id="rc_span">* 请输入不包含关键词</span>';
	    			$('#keyword_nnull').html(html);		   //生成span
	    		}
	    	}else{
	    		$('#keyword_nnull').show();			  	   //展现包含提示层
	    		var html = '<span class="ev_wCount">* 最多设置5个不包含关键词</span>';
	    		$('#keyword_nnull').html(html);		       //生成span
	    	}
    	});
	}
	
	//删除关键词
	function delete_keyword(split_hide,keyword_hide,del_spanid,div){

		$('#'+del_spanid,div)[EV](CK, function(){
			//删除关键词
			$('#'+del_spanid,div)[PT]()[RM]();			
			//隐藏提示层
			if($('#'+split_hide+' li').length == 0){
				$('#'+split_hide).hide();
				$('#'+keyword_hide).hide();
			}
		});
	}

	//确定添加或者取消
 	function add_data(div,type,edit_id){
		$('#in_eventok',div)[EV](CK, function() {
			var date_y = '';
			var date_n = '';
				
			//获取添加事件数据
			var topicname = $.trim($('#topicname').val());	//事件名称
			var jk_dateStart = $('#jk_dateStart').val();	//监控开始时间
			var jk_dateEnd = $('#jk_dateEnd').val();		//监控结束时间
			//var code = $("#t_city").val();

			if(topicname == '' || topicname == '请输入事件的名称'){
				$('#eventname').show();			  	  		
		    	var html = '<span class="ev_wCount">* 事件名称不能为空</span>';
		    	$('#eventname').html(html);		 	 
			}else if(topicname != '' && topicname != '请输入事件的名称'){
				if(!$.regNorm3(topicname)){
					$('#eventname').show();			  	  		
			    	var html = '<span class="ev_wCount">* 事件名称不能包含无效字符</span>';
			    	$('#eventname').html(html);		 	 	 	
				}else if($('#split_jointy li').length == 0){
					$('#eventname').hide();	
					$('#keyword_ynull').show();			  	  
			    	var html = '<span class="ev_wCount">* 包含关键词不能为空</span>';
			    	$('#keyword_ynull').html(html);		 	 
				}else if(base.IsRegExpDateTime($('#jk_dateStart'),$('#jk_dateEnd'))){//日期判断
					
					$('#split_jointy li span ')[EH](function(){	//循环遍历包含关键词并拼接成字符串，用','隔开
						if($(this).html()!= ''){
							date_y += $(this).html()+',';
						}
					});
					if (date_y.length > 0) {		
						 date_y = date_y.substr(0,date_y.length - 1); //去掉最后一个逗号
					}
					$('#split_jointn li span ')[EH](function(){//循环遍历非包含关键词并拼接成字符串，用','隔开
						if($(this).html()!= ''){
							date_n += $(this).html()+',';
						}
					});
					if (date_n.length > 0) {		
						 date_n = date_n.substr(0,date_n.length - 1); //去掉最后一个逗号
					}
					if(edit_id == 0){ 
		 				//异步交互 添加
						$(BY)[AJ]({
								param: {action: 'eventMonitor.addEvent',code: 0, topicname: topicname,date_y: date_y,date_n: date_n,jk_dateStart: jk_dateStart,jk_dateEnd: jk_dateEnd,type: type,method: 1 },
								success: function(data){
									OnLoadmonitorList(type);CloseWindow(div);
								}
						});
		 			}else{
		 				//异步交互 修改
						$(BY)[AJ]({
								param: {action: 'eventMonitor.updateEvent', code: 0,data_id: edit_id,topicname: topicname,date_y: date_y,date_n: date_n,jk_dateStart: jk_dateStart,jk_dateEnd: jk_dateEnd,method:2},
								success: function(data){
									// -1：到达添加次数，限制提示
									if(data == -1){
										CloseWindow(div);
										$[MB]({ content: '修改失败,数据已到达上限!', type: 1});
									}else{
										CloseWindow(div);
										OnLoadmonitorList(type);
									}
								}
						});
		 			}
				}
			}
    	});
    	
    	//取消
    	$('#in_cancel',div)[EV](CK, function() {
    		CloseWindow(div);	//取消操作 关闭弹出层
    	});
 	}
	
	//关闭窗体事件
	function CloseWindow(div) {
	   div[RM]();
	   $[RM]({ obj: '#' + JLY });
	}
 	
 	//时间面板赋值
    function DateTimeOption(dateStart,dateEnd) {
    	$('#'+dateStart).datePicker();
    	$('#'+dateEnd).datePicker();
    }
 	
 	//---------监控列表操作结束----------------//
 	
 	
 	
 	//----------------事件监控分析开始---------------------------------------//
 	
     //事件监控分析签页切换
 	function SwitchingEventtabs(id,title,type){
 		$('#htname').html(title);
 		if(type == 0){
			$("#userfenxi",$key).show();
		}else{
			$("#userfenxi",$key).hide();
		}
    	eventtabs[EV](CK,function(){
          	var This = $(this),index = eventtabs[ID](This);
          	eventtabs[RC]("zhao_back"); 						//删除样式
          	This[AC]("zhao_back");			 					//添加样式
			
          	switch(index){
          		case 0:SwitchingAnalysistabs(id,index);return;  //趋势分析24小时天、七天标签页切换
          		case 1:content_div(id,index);return;		    //内容分析
          		case 2:users_div(id,index,type);return;			//用户分析
          	}
        }).eq(0)[TR](CK);
 	}  
    
     //-------------趋势分析-开始-------------------------//
        
    //趋势分析24小时天、七天标签页切换
 	function SwitchingAnalysistabs(id,index){
 	 		
		$('#event_talkMe',$key).hide();		//隐藏监控列表
		$('#event_details',$key).hide();	//隐藏事件详情
		$('#contentAnaly',$key).hide();		//隐藏分析模块下的内容分析
		$('#usesAnaly',$key).hide();		//隐藏分析模块下的用户分析
		$('#eventAnalyse',$key).show();		//显示分析模块
		$('#analymodule',$key).show();		//显示分析模块下的趋势分析
		
		DateTimeOption('datePickerStart','datePickerEnd');		//时间控件赋值

	        var className = 'selW_selected',
				  div = $('#likeWeekLine', $key),
				  to_action = 'eventMonitor.trendAnalys';
	
	        hour_24[EV](CK, function() {
				var $this = $(this);
				$this[AC](className); day_7[RC](className);
				LoadDateChar($this, div, {day: 0, startTime: '', endTime: '', charType: 'Hour_24'}, charType.Line,id,to_action);
			})[TR](CK);
			
			day_7[EV](CK, function() {
				var $this = $(this);				    
				$this[AC](className); hour_24[RC](className);
				LoadDateChar($this, div, {day: -6, startTime: '', endTime: '', charType: 'Day_7'}, charType.Line,id,to_action);
			});
 
       //点击搜索按钮
		searchInfo[EV](CK, function() {
			if(base.IsRegExpDateTime($('#datePickerStart'), $('#datePickerEnd'))) {
				var t = $(this),type2 = searchInfo[ID](t);
				hour_24[RC](className); day_7[RC](className);	//删除样式
				
				LoadDateChar(t, div, {day: 0, startTime: $('#datePickerStart').val(), endTime: $('#datePickerEnd').val(), charType: 'DayDate'}, charType.Line,id,to_action);
			}
		});//[TR](CK);
 	}  
   
   	//加载线形图图表异步方法btn:点击按钮, div: 显示图表控件, param: 异步参数
	function LoadDateChar(btn, div, param, charType,id,to_action) {
		param = $[EX](param, {action: to_action,eid: id});
		div.html('')[LG]();
		div[AJ]({
			param: param,
			success: function (data) {
				if(data.test == -1) {
					base.not(div);
				} else if(data.test == -2){
					base.not2(div);
				}else{
					base.Char(div, charType, data,0);
				}
			}
		});
	}
	//-------------趋势分析-结束-------------------------//
	//发布统计 按日期查询数据和按时间段查询数据(Ajax)
	function LoadByDateChar(div, param) {
		div.html('')[LG]();
		div[AJ]({
			param: param,
			success: function (data) {
				if(data != null) {
					base.Char(div, charType.Line, data,0);
				} else {
					base.not(div);
				}
			}
		});
	}
	//获取当前时间方法
	function getCurrentTime () {
		var year = date.getFullYear(),
			   month = date.getMonth() + 1,
			   day = date.getDate();
		if(month<10) month = '0' + month;
		 if(day<10) day = '0' + day;
		return year + '-' + month + '-' + day;
	}
	//-------------内容分析-开始-------------------------//
	
	
	function content_div(id,index){
		$('#event_talkMe',$key).hide();		//隐藏监控列表
		$('#event_details',$key).hide();	//隐藏事件详情
		$('#eventAnalyse',$key).show();		//显示分析模块
		$('#analymodule',$key).hide();		//隐藏分析模块下的趋势分析
		$('#usesAnaly',$key).hide();		//隐藏分析模块下的用户分析
		$('#contentAnaly',$key).show();		//显示分析模块下的内容分析
		
		var to_action = 'eventMonitor.contentAnalys',
			div = $('#resShowDayPie',$key),
			hotdiv = $('#hot_keyword',$key);
			
		LoadDateChar(div, div, {day: 0, charType: 'The_forward'}, charType.Pie,id,to_action);//加载原创/转发统计图
		//LoadDateChar(div, div, {day: 0, charType: 'Hot_keywords'}, charType.Column,id,to_action);//加载热门关键词统计图
		TheHotkeyword(id);					//加载热门关键词
		OnLoadKeyList(id);					//加载关键观点列表 top5 
	}

	//加载热门关键词
	function TheHotkeyword(id){
		
		$(BY)[AJ]({
			param: {action: 'eventMonitor.contentAnalys', eid: id,charType: 'Hot_keywords' },
			success: function(data){
				if(data.test != -1){
					hot_keyword.html('').jQCloud({
						collection:data.series
					});
				}else{
					base.not2(hot_keyword);
				}
			}
		});
	}
	
	//加载关键观点列表 top5 
    function OnLoadKeyList(id){
		keyviewdiv.html("");
		keyviewdiv[TB]({
		     width: 998,
             trHeight: 24, //表格默认高
             isPager: FE, //是否分页
             isCheckBox: FE, //是否有多选按钮
             id:'_m1',
             rows: 'rows', 
             firstWidth: '5%',
             lastWidth: '10%',
             param: {action :'eventMonitor.contentAnalys',charType: 'The_keylist',eid: id},
			 columns: [{ caption: '序号', field: 'id', type: 'number',width: '2%'},
                       { caption: '昵称', field: 'nickname', type: 'string', width: '10%' },
                       { caption: '认证', field: 'authentication', type: 'string', width: '3%'},
                       { caption: '内容', field: 'text', type: 'string', width: '20%'},
                       { caption: '转发数', field: 'zfnum', type: 'int', width: '3%'},
                       { caption: '发布时间', field: 'createtime', type: 'string', width: '6%'}],
            //加载表格成功事件
            onComplete: function (This, refresh, data) {
				if(data.realcount != 0){
					var html = '<div class="com_fLeft cont_photoMar"><div class="cont_photo"><a class="com_fLeft aOpen" href="{2}" target="_blank"><img src="{0}" class="accountImg" width="50" height="50"></a><div class="wb_listType mynum_type{1}"></div> <div class="photo_allInfor com_none"></div></div></div><div class="com_clear"></div>';
					This[FD]('.t1')[EH](function (){
						var index=This[FD]('.t1')[ID]($(this));
						var id = data.rows[index].id;
						var imageHead = data.rows[index].imageHead;
						var acctype = data.rows[index].account;
						var temp = $[FO](html,imageHead,acctype.twitterType,acctype.url);
						$(this)[AP](temp);
						
						//显示用户信息
						var accountInfo = $('.photo_allInfor', This);
				        accountInfo[EH](function() {
								var $this = $(this),
									index = accountInfo[ID]($this),
									accountRow = data.rows[index].account;
								base.AccountTip($this, accountRow,4,base.isAllExpire);
						});
					});
					$('.photo_allInfor', This[FD]('tr').last()).css("top","-20px");//最后一列头像
					
				}else{
					base.not(This);
				}
            }
		});
    }
	
	//-------------内容分析-结束-------------------------//

	//-------------用户分析-开始-------------------------//
	function users_div(id,index,type){
		$('#event_talkMe',$key).hide();						//隐藏监控列表
		$('#event_details',$key).hide();					//隐藏事件详情
		$('#eventAnalyse',$key).show();						//显示分析模块
		$('#analymodule',$key).hide();						//隐藏分析模块下的趋势分析
		$('#contentAnaly',$key).hide();						//隐藏分析模块下的内容分析
		$('#usesAnaly',$key).show();						//显示分析模块下的用户分析

		var to_action = 'eventMonitor.userAnalys';
			/*暂时不用 隐藏了
			userdiv = $('#user_scale',$key),				//男女比例
			renzdiv = $('#renz_scale',$key),				//认证比例
			sourcediv = $('#source_spread',$key),			//来源分布
			enrolldiv = $('#enroll_spread',$key),			//注册时间分布
			terraindiv = $('#terrain_analy',$key);			//地域分析
		
		LoadDateChar(userdiv, userdiv, {day: 0, charType: 'User_scale'}, charType.Pie,id,to_action);//加载男女比例统计图
		LoadDateChar(renzdiv, renzdiv, {day: 0, charType: 'Renz_scale'}, charType.Pie,id,to_action);//加载认证比例统计图
		LoadDateChar(sourcediv, sourcediv, {day: 0, charType: 'Source_spread'}, charType.Pie,id,to_action);//加载来源分布统计图
		LoadDateChar(enrolldiv, enrolldiv, {day: 0, charType: 'Enroll_spread'}, charType.Pie,id,to_action);//加载注册时间分布统计图
		LoadDateChar(terraindiv, terraindiv, {day: 0, charType: 'Terrain_analy'}, charType.Column,id,to_action);//加载地域分析统计图
		*/
		OnLoadKeyuserList(id,index,type);						//加载关键用户列表 top5 
	}
	
	//加载关键用户列表 top10 
    function OnLoadKeyuserList(id,index,type){
		$('#keyuserdiv').html('');
		keyuserdiv[TB]({
             width: 998,
             trHeight: 24, //表格默认高
             isPager: FE, //是否分页
             isCheckBox: FE, //是否有多选按钮
             id:'_m1',
             rows: 'rows', 
             firstWidth: '5%',
             lastWidth: '10%',
             param: {action :'eventMonitor.userAnalys',eid:id,type: type,index: index,charType: 'KeyuserList'},
			 columns: [{ caption: '序号', field: 'id', type: 'number',width: '5%'},
                       { caption: '昵称', field: '', type: 'html', width: '20%',template:'' },
                       { caption: '地区', field: 'area', type: 'string', width: '20%'},
                       { caption: '粉丝数', field: 'fansnum', type: 'int', width: '10%'},
                       { caption: '用户类型', field: 'authentication', type: 'string', width: '10%'}],
            //加载表格成功事件
            onComplete: function (This, refresh, data) {
				if(data.realcount != 0){
					var html = '<div class="com_fLeft cont_photoMar"><div class="cont_photo"><a class="com_fLeft aOpen" href="{1}" target="_blank"><img src="{0}" class="accountImg" width="50" height="50"></a><div class="wb_listType mynum_type{3}"></div> <div class="photo_allInfor com_none"></div></div><span class="com_fLeft">{2}</span></div><div class="com_clear"></div>';
					This[FD]('.t1')[EH](function (){
						var index=This[FD]('.t1')[ID]($(this));
						var id = data.rows[index].id;
						var acctype = data.rows[index].account;
						var temp = $[FO](html,data.rows[index].imageHead,data.rows[index].url,data.rows[index].nickname,acctype.twitterType);
						$(this)[AP](temp);
						
						//显示用户信息
						var accountInfo = $('.photo_allInfor', This);
				        accountInfo[EH](function() {
								var $this = $(this),
									index = accountInfo[ID]($this),
									accountRow = data.rows[index].account;
								base.AccountTip($this, accountRow,4,base.isAllExpire);
						});
					});
					$('.photo_allInfor', This[FD]('tr').last()).css("top","-20px");
				}else{
					base.not(This);
				}
            }
		});
    }
    
    //加载柱状图
	function TheCharColumn(id,index,type) {
		
		var date = new Array();
		var text,name,title;
		var datanum = Array();
			
		switch(index){
			case 0:
				text = '';
				name = '';
				title = '',
				date = ['北京', '上海', '江苏', '浙江', '福建','山东','河南','河北','湖南','广东','其他'];
				datanum = [88, 40, 20, 10, 5,88, 40, 20, 10, 5, 99];
			break;
		}
		
		base.Char($('#'+type, $key), charType.Column, {
			categories: date,
			text: text,
			title: title,
			series: [{name: name,  data:datanum }]
		});
		
	}
	//-------------用户分析-结束-------------------------//
	
	
	//----------------事件监控分析结束------------------------------------------------------//

    
    //-----------------------------事件监控详情--------------------------------------------//
    //事件监控详情
    function EventLoadList(id,str,type) {
   		$(BY)[AJ]({
			param: {action: 'eventMonitor.monitorList', data_id: id,type: type,method:3 },
			success:function(data){//alert($.toJson(data));
	
		    	//topicname = topicname.replace(/\s*/g, "");//去掉空格
		    	var startTime = data.startTime;
		    	var endTime = data.endTime;
		    	DateTimeOption('dateStart','dateEnd');
		    	$('#topname').html(data.name);//加载话题名称
		    	$('#gjkey').val('');
		    	ScreenDataBind();//筛选
		    	OrderDataBind();//排序

		    	var startDate,endDate;
		    	var myDate = new Date();
		    	var tday="",month="";
		    	if(myDate.getMonth()<9){month="0"+(myDate.getMonth()+1)}else{month=myDate.getMonth()+1}
		    	if(myDate.getDate()<10){tday="0"+myDate.getDate()}else{tday=myDate.getDate()}
				var tadayTime = myDate.getFullYear()+'-'+month+'-'+tday;
				myDate.setTime(myDate.getTime()-24*60*60*1000);
				if(myDate.getMonth()<9){month="0"+(myDate.getMonth()+1)}else{month=myDate.getMonth()+1}
		    	if(myDate.getDate()<10){tday="0"+myDate.getDate()}else{tday=myDate.getDate()}
				var torrTime =  myDate.getFullYear()+'-'+month+'-'+tday;
				var yearTime =  (myDate.getFullYear()-1)+'-'+(myDate.getMonth()+1)+'-'+(myDate.getDate()+1);

				/** 1:今天 2：昨天 0：全部   **/
				if(str == 1){
					startDate = tadayTime+' 00:00:00';
					endDate = tadayTime+' 23:59:59';
					dateStart.val(tadayTime);
					dateEnd.val(tadayTime);
				}else if(str == 2){
					startDate = torrTime+' 00:00:00';
					endDate = torrTime+' 23:59:59'; 
					dateStart.val(torrTime);
					dateEnd.val(torrTime);
				}else if(str == 0){			
					startDate = startTime+' 00:00:00';
					endDate = endTime+' 23:59:59';
					dateStart.val(startTime);
					var start=new Date(startTime.replace("-", "/").replace("-", "/"));  
					var end=new Date(endTime.replace("-", "/").replace("-", "/"));  
					var mytime=new Date(tadayTime.replace("-", "/").replace("-", "/"));  

					if(mytime >= start ){
						if(mytime < end){
							dateEnd.val(tadayTime);
						}else{
							dateEnd.val(endTime);
						}
					}else{
						dateEnd.val(endTime);
					}
				}
				$("#dateStart").datePicker({minDate:data.startTime});
				$("#dateEnd").datePicker({minDate:data.startTime});
				if(base.IsRegExpDateTime($("#dateStart"),$("#dateEnd"))){
					EventLoadDataBind(id,startDate,endDate,'',0,0,type);//首次进来加载详情
				}
				//搜索
				but_search[EV](CK, function() {
					var screen = screenSelect[AT]("value"); //筛选
					var timeSort = orderSelect[AT]("value"); //排序
					startDate = dateStart.val()+' 00:00:00';
					endDate = dateEnd.val()+' 23:59:59';
					text = $("#gjkey").val();
					if(base.IsRegExpDateTime($("#dateStart"),$("#dateEnd"))){
						EventLoadDataBind(id, startDate,endDate,text,screen,timeSort,type);
					}
				});
							
				//导出
				tps_exl[EV](CK, function() {
					//alert('导出');
				});
		  	}
		});
	}
    //加载详情方法
    function EventLoadDataBind(id,startDate,endDate,text,screen,timeSort,type){
    	$('#allPager_xz', $key).html('');
    	var isSina = TE,
    		html = base.FormatPmi('<span class="alertBtn" alertid="0" url="">预警</span><span>|</span>{pmi:cbfx_monitor_3}{pmi:yqzs_collect_1}{pmi:wdsy_repost_1}{pmi:wdsy_comment_1}');
    	if( type == 1){
    		isSina = FE;
    		html = base.FormatPmi('{pmi:wdsy_repost_1}{pmi:wdsy_comment_1}');
    	}
    	base.DataBind({
				panel: lookList, 
				pager: $('#allPager_xz', $key),
				id: id, 
				isFlag:base.isAllExpire,
				isSelectAccount: TE,
				moduleType: _enum.ModuleType.EventMonitor,//模块入口类型
				template: html,//{pmi:sjjk_forwarddata_1}{pmi:sjjk_reviewdata_1}
				param: {action :'eventMonitor.monitorList',eid: id,startDate: startDate,endDate: endDate,text:text,screen:screen,timeSort:timeSort,type:type,method:2}, 
				onComplete: function (This, refresh, data) {
					if(!isSina) {
	     				$('.commentCount', This)[RC]('commentCount')[AC]('cont_noDO');
	     				$('.repostCount', This)[RC]('repostCount')[AC]('cont_noDO');
	     				$('.subBtnPanel', This)[RC]('subBtnPanel')[AC]('com_none');
	     			}
				},
				onSend:function (btn,txt, isChecked, accountCollection, postID, url,  type, twitterID, closeBtn,name) {
				var param = {};
				param.action = type == 'repost'? 'myTwitter.saveReport': 'myTwitter.saveComment';
				param.isChecked = isChecked;
				if (type == 'repost' ) {
					//不填转发内容的时候，默认值
					if ($[TM](txt) == '') {
						txt = '转发微博';
					}
				}else {
					if ($[TM](txt) == '') {
						$[MB]({
							content: '评论内容不能为空', 
							type: 2
						});
						btn[AT]('isclick',true);
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

				$(BY)[AJ]({
					param: param,
					dataType: 'text',
					success: function(data) {
						if(data == '"发布成功"') {
							$[MB]({content: '提交成功', type: 0,isAutoClose: TE});
							closeBtn[TR](CK); //关闭子页面
						} else {
							$[MB]({content: '提交失败', type: 2});
						}
						btn[AT]('isclick',true);
					}
				});
				}
				
			});
    
    }
     //绑定筛选下拉菜单
     function ScreenDataBind() {
     	screenSelect.input({
     		id: 'search.screen',
     		collection: [{text:'全部',value:0},
     						   {text:'原创',value:1},
     						   {text:'转发',value:2}]
     	});
     } 
     //绑定排序下拉菜单
     function OrderDataBind() {
     	orderSelect.input({
     		id: 'search.sort',
     		collection: [{text:'时间',value:0},
     						   {text:'转发',value:2},
     						   {text:'评论',value:3}]
     	});
     }
    return {
    	IsLoad: FE,
        OnLoad: function (_ContentDiv) {
            var t = this;
            contentDiv = _ContentDiv;
            $('<div />').load('modules/eventMonitor/eventMonitor.htm', function () {
            	//contentDiv[AP]($(this).html());
            	base = context.Base;
            	cuser=context.CurrentUser;
            	contentDiv[AP](base.FormatPmi($(this).html()));
            	t.IsLoad = TE;
                $key = $('#' + key);
                navigationBars = context.NavigationBars;
                date = new Date();
                _enum = context.Enum;
                charType = _enum.CharType;

                labelreview = $('.span_title',$key);			//监控标签对象集合
                tabItem = $('.tabItem',$key);					//分析标签对象集合
                day_7 = $('#day_7',$key);						
                hour_24 = $('#hour_24',$key);
                
                eventtabs = $('.event_anaLeft',$key);			//分析标签切换
                listdiv = $('#listdiv',$key);					//事件监测列表Div
                tps_exl = $('#tps_exl',$key);
                keyviewdiv = $('#keyviewdiv',$key);				//内容分析关键观点列表Div
                keyuserdiv = $('#keyuserdiv',$key);				//用户分析关键用户列表Div
                event_add = $('.event_add',$key);				//添加事件
                objhtml = $("#newevent_lajiSel",$key);
                screenSelect = $('#tps_shaiXuan', $key); 		//筛选select对象
                orderSelect = $('#tps_order', $key); 			//排序select对象
                lookList = $('#lookList', $key); 				//列表详情Div       
                but_search = $('#but_search',$key);				//详情搜索按钮
                searchInfo = $('#searchInfo',$key);				//趋势分析日期搜索
                hot_keyword = $('#hot_keyword',$key);			//热门关键词
                terrain_analy = $('#terrain_analy',$key);		//地域分析
                
                dateStart = $("#dateStart",$key);
                dateEnd = $("#dateEnd",$key);
              
                
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
        	Init();		//第二次调用初始化方法
        }
    };
});