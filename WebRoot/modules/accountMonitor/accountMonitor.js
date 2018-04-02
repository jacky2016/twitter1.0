/**
*账号监测
*@author shaoqun
**/
define(['portal'], function (context) {
    //定义变量
    var key = 'accountMonitor',
          $key, //当前的作用域
          navigationBars,
          labelreview,
          listdiv,
          analydiv,
          hour_24,
          day_7,
          day_30,
          fans_rz,
          fans_sex,
          fans_num,
          terrain_analy,
          fsFans,
          acc_weibo,
          acc_reply,
          accountdiv,
          event_add, 
          event_import,
          but_search,
          joblist,
          joblist_qs,
          joblist_fs,
          joblist_bw, 
          postdiv,
          remen_bq,
          contentDiv;
          
    //私有方法
    //初始化方法 
    function Init() {
    	NavigationInit();
    	SwitchingMonitortabs();
    	
    }
     //导航条初始化方法,
	function NavigationInit(){
    	navigationBars.Empty()
			.Add(key, 'NavToMP', '帐号监测')[oL]();
    } 
    //事件分析导航条初始化方法,
	function NavigationInit_fx(){
    	navigationBars.Empty()
			.Add(key, 'NavToMP', '帐号监测')
			.Add(key, 'NavToMP_fx', '帐号分析')[oL]();
    } 
    //事件详情导航条初始化方法,
	function NavigationInit_xq(){
    	navigationBars.Empty()
			.Add(key, 'NavToMP', '帐号监测')
			.Add(key, 'NavToMP_fx', '帐号分析')
			.Add(key, 'NavToMP_xq', '微博详情')[oL]();
    } 
    
    //监控列表标签页切换
 	function SwitchingMonitortabs(){
 	
    	$('#account_talkMe').show();		//隐藏监测列表层
	    $('#account_details').hide();		//隐藏帐号详情
        $('#account_analy').hide();			//显示分析模块
        $('#realtime_analy').hide();		//隐藏分析模块下的实时监测层
        //$('#trend_analy').hide();			//隐藏分析模块下的趋势分析层
        $('#fans_analy').hide();			//隐藏分析模块下的粉丝分析层
        $('#post_analy').hide();			//显示分析模块下的博文分析层
        
    	labelreview[EV](CK,function(){
          	var This = $(this),type = labelreview[ID](This);
          	labelreview[RC]("zhao_back"); 				//删除样式
          	This[AC]("zhao_back");			 			//添加样式
          	
          	switch(type){
          		case 0:OnLoadmonitorList(type);return	   	//新浪
          		case 1:OnLoadmonitorList1(type);return;		//腾讯
          	}
        }).eq(0)[TR](CK);
 	}      
 	
 	function OnLoadmonitorList1(type){

 		var pager = $('#allPager', $key);
		listdiv.add(pager).html('');
		listdiv[TB]({
             width: 1000,
             trHeight: 24, //表格默认高
             pager: pager,
             pageSize: 15, //显示条数
             isPager: TE, //是否分页
             isCheckBox: FE, //是否有多选按钮
             id:'_m1',
             rows: 'rows',
             param: {action :'accountMonitor.accountList',type: type,method:1},
			 columns: [{ caption: '序号', field: 'id', type: 'number',width: '5%'},
                       { caption: '帐号昵称', field: 'accountname', type: 'string', width: '30%'},
                       { caption: '微博数', field: 'weibonum', type: 'int', width: '10%' },
                       { caption: '粉丝数', field: 'fansnum', type: 'int', width: '10%' },
                       { caption: '关注数', field: 'gznum', type: 'int', width: '10%'},
//                       { caption: '微博原创率', field: 'weiborate', type: 'double', width: '10%'},
//                       { caption: '平均每日微博数', field: 'effectavgnum', type: 'int', width: '12%'},
                       { caption: '操作', field: '', type: 'html', width: '', template:''}],
                //加载表格成功事件
            onComplete: function (This, refresh, data) {
           			This.html(' <div class="no_data"><div class="com_fLeft nodata_tip"></div><span  class="com_fLeft nodata_tipLan">此功能暂未开放！</span></div>');
           			event_add[RC]('imp_liRight')[AC]('com_none');
            }
       });
    }
 	
 	//加载帐号监控列表
 	function OnLoadmonitorList(type){
		event_add[AC]('imp_liRight')[RC]('com_none');
 		var pager = $('#allPager', $key);
		listdiv.add(pager).html('');
		listdiv[TB]({
             width: 1000,
             trHeight: 24, //表格默认高
             pager: pager,
             pageSize: 15, //显示条数
             isPager: TE, //是否分页
             isCheckBox: FE, //是否有多选按钮
             id:'_m1',
             rows: 'rows',
             param: {action :'accountMonitor.accountList',type: type,method:1},
			 columns: [{ caption: '序号', field: 'id', type: 'number',width: '5%'},
                       { caption: '帐号昵称', field: 'accountname', type: 'string', width: '30%'},
                       { caption: '微博数', field: 'weibonum', type: 'int', width: '15%' },
                       { caption: '粉丝数', field: 'fansnum', type: 'int', width: '15%' },
                       { caption: '关注数', field: 'gznum', type: 'int', width: '15%'},
//                       { caption: '微博原创率', field: 'weiborate', type: 'double', width: '10%'},
//                       { caption: '平均每日微博数', field: 'effectavgnum', type: 'int', width: '12%'},
                       { caption: '操作', field: '', type: 'html', width: '', template:''}],
                //加载表格成功事件
            onComplete: function (This, refresh, data) {
            
            	var insert_power = cuser.Templates['zhjc_adddata_1'];			//取得添加权限
				var delete_power = cuser.Templates['zhjc_deletedata_1'];		//取得删除权限
				var warn_power = cuser.Templates['yjfw_yjxxinsert_4'];			//取得预警权限
				var analy = '<div class="event_doBotton event_analy" title="分析" id="analye_btn{0}"></div>';
            	
            	//点击导出 导出数据
            	eventimport(type);
            	//判断是添加权限
				if( insert_power != null && insert_power != ""){
					//点击添加 添加帐号
            		event_add[EV](CK, function(){
            			$(this)[AJ]({
					    	param:{action:'accountMonitor.addAccount',type:type,method:2},
					    	success:function(data1){
					    		// -1：到达添加次数，限制提示，0：插入失败，数据库错误提示，1：添加成功
								if(data1 == -1){
									$[MB]({ content: '添加失败,添加已到达上限!', type: 1});
								}else{
									eventadd(type);
								}
					    	}
						});
            		});
				}
				
				/*
            	This[FD]('.t8')[EH](function (){
            		var index=This[FD]('.t8')[ID]($(this));
            		var id = data.rows[index].id;
					$(this)[AP]('<span class="push_type push_type1" id="start_btn'+id+'">&nbsp;</span><div class="com_clear"></div>');
            		
            		//对账号进行暂停（启动）操作
					stopstart_div('start_btn'+id,id);
            	});
            	*/
            	if(data.realcount == 0){
            		base.not(This);
            	}else{
	            	This[FD]('.t5')[EH](function (){
	            		
	            		var index=This[FD]('.t5')[ID]($(this));
						var id = data.rows[index].id;
						var topname = data.rows[index].accountname;
	 
						//判断是删除权限
						if(delete_power != null && delete_power != ""){
							var delTemp = $[FO](delete_power,id);
					        $(this)[AP](delTemp);								//追加权限按钮图片
					         	   
							//点击删除 给出提示，删除事件任务
							$('#delete_btn'+id,$key)[EV](CK,function(){
								delete_div(id,type);
							});
						}
						
						//判断是预警权限
						if( warn_power != null && warn_power != ""){
							var wTemp = $[FO](warn_power,id);
					        $(this)[AP](wTemp);								//追加权限按钮图片
					         	   
							//点击 弹出预警界面
							$('#warn_btn'+id,$key)[EV](CK,function(){
								//WarnService(type,id,topname);
								//预判断
								$(this)[AJ]({
							    	param:{action:'accountMonitor.accountList',textarea: topname,type: type,method: 3},
							    	success: function (data2) {
							    		WarnService(type,id,topname);
							    	}
						    	});
							});	
						}
						
						var analyTemp = $[FO](analy,id);
					    $(this)[AP](analyTemp);		
						//点击分析 查看分析结果
						$('#analye_btn'+id,$key)[EV](CK,function(){
							//NavigationInit_fx();					//展示帐号分析导航条
							//SwitchingAccounttabs(id,topname,type);	//分析标签切换
							$(this)[AJ]({
							    param: {action: 'accountMonitor.accountList',textarea: topname,type: type,method:3},
							    success: function (data2) {
							    	NavigationInit_fx();					//展示帐号分析导航条
									SwitchingAccounttabs(id,topname,type);	//分析标签切换
							    }
						    });
						});
						
	            	});
            	}
            }
		});
 	}	
 	
    //弹出添加预警服务层
 	function WarnService(type,warnid,topname){

		var html = '<div class="giveMe_do show_eventDo acc_yujing" id="yc_key"><div class="acc_yujingBox"><span style="color:red; font-size: 12px;">提示：关键词之间以一个空格分开</span></div><div class="div_style"><label class="task_name">关键词：</label><input type="text" id="keyword" class="task_count task_Cwdith1" value=""/><div class="tWord_add" id="word_add">添加</div><span class="canzhao com_fLeft" id="refer_to">参照</span></div><div class="acc_yujingBox"  style="float: left; color: red; font-size: 12px; display: block;"><span class="com_none" id="ts_keyword"></span></div><div class="acc_yujingBox "><table cellpadding="0" cellspacing="0" class="web_table" id="webtable"><th style="width:25px"><input type="checkbox" name="keyzu" id="_keyzu"/></th><th>关键词组</th></table></div><div class="div_style more_doExp "><label class="task_name">接收人：</label><div id="pt_select" class="mynum_list"><h2 id="_h2"><span id="span_id">请选择</span><span class="mynum_type mynum_click"></span></h2><ul id="newevent_lajiSel" class="com_none"></ul></div></div><div class="com_clear"></div><div style="margin-left: 95px;float: left; color: red; font-size: 12px; display: block;"><span class="com_none" id="jsr_check"></span></div> <div class="com_clear"></div><div class="div_style" id="sendout"><label class="task_name">发送方式：</label><div id="sendout" class="com_fLeft" style="margin-left:0"><input type="checkbox" name="yjSend_way" class="com_fLeft yjSend_wayMar alertCheck" value="0" disabled="disabled"/> <span  class="com_fLeft com_rMar">系统消息</span><input type="checkbox"  name="yjSend_way" class="com_fLeft yjSend_wayMar"  value="1"/> <span  class="com_fLeft com_rMar alertCheck">邮件</span><input type="checkbox"  name="yjSend_way" class="com_fLeft yjSend_wayMar" value="2"/> <span  class="com_fLeft com_rMar alertCheck">手机短信</span></div></div><div class="com_clear"></div><div style="margin-left: 95px;float: left; color: red; font-size: 12px; display: block;"><span class="com_none" id="send_check"></span></div> <div class="com_clear"></div><div class="push_ok"><div class="in_MsetCno" id="in_nokey">取&nbsp;&nbsp;消</div><div class="in_MsetCok" id="in_okkey">确&nbsp;&nbsp;定</div>&nbsp;&nbsp;&nbsp;&nbsp;<div class="in_MsetCok com_none" style="margin-right:10px;*margin-top:-20px;" id="cancelwarn">取消预警</div></div><div class="com_clear"></div></div><div class="giveMe_do show_eventDo acc_yujing com_none" id="yc_acc"><div class="acc_yujingBox"><table cellpadding="0" cellspacing="0" class="web_table" id="webcanzhao"></table></div><div style="margin-left: 60px;float: left; color: red; font-size: 12px; display: block;"><span class="com_none" id="canzhao_check"></span></div> <div class="com_clear"><div class="push_ok"><div class="in_MsetCno" id="in_noacc">返&nbsp;&nbsp;回</div><div class="in_MsetCok" id="in_okacc">参&nbsp;&nbsp;照</div></div><div class="com_clear"></div></div>';
			$[WW]({
				css: { "width": "540px", "height": "auto"},
				title: '预警服务',
				content: html,
				id: 'warnid',
				onLoad: function(div,close) {
				 	 var saveBtnkey = $("#in_okkey",div),cancelBtnkey = $("#in_nokey",div),
				 	 	 saveBtnacc = $("#in_okacc",div),cancelBtnacc = $("#in_noacc",div),
				 	 	 keyword = $("#keyword",div),word_add = $("#word_add",div),
				 	     _lajiSel = $("#newevent_lajiSel",div),_h2 = $("#_h2",div),
				 	     refer_to = $("#refer_to",div),ts_keyword = $("#ts_keyword",div),
				 	     yc_key = $("#yc_key",div), yc_acc = $("#yc_acc",div),webtable = $("#webtable"),
				 	     webcanzhao = $("#webcanzhao",div),strs = new Array(),_strs = new Array(),
				 	     sendout = $("#sendout",div),jsr_check = $("#jsr_check",div),send_check = $("#send_check",div),
				 	     canzhao_check = $("#canzhao_check",div),cancelwarn = $("#cancelwarn",div),id=0;
				 	 var alertCheck = $('.alertCheck', div);
			    		 alertCheck[0][CD] = TE;   
				 	 keyword.txt({text: '请输入关键词' });
				 	 ts_keyword.html('');
				 	
					 word_add[EV](CK,function(){//添加关键词
					 	var keys = keyword.val();
					 	var keywordval = keys.replace(/\s+/g," ");
					 		strs = keywordval.split(" "),rids = "",result = [],i=0,j=0;
					 		
						if(webtable[FD]('tr').length < 21){
							if(strs.length <= 5){
								if(keywordval == "" || keywordval == "请输入关键词"){
									ts_keyword.show();
									ts_keyword.html('* 请输入关键词!');
								}else if(keywordval != '' && keywordval != '请输入关键词'){
									if(!$.regNorm4(keywordval)){
										ts_keyword.show();
										ts_keyword.html('* 关键词不能包含无效字符');
									}else{
										$.each(strs, function (index, tx) {
											if(tx != ""){
												if(tx.length <= 6){
										  			result.push(tx);
										  		}else{
										  			ts_keyword.show();
													ts_keyword.html('* 每个关键词最多6个字!');
													i = 1;
										  		}
											}
										});
										if(i == 0){
											rids = result.join(" ");
											if(webtable[FD]('tr').length > 1){
									 	 		webtable[FD]('tr td input')[EH](function(){
										    		if(rids == this.value){
										    			ts_keyword.show();
														ts_keyword.html('* 该词组已添加，请重新输入!');
														return false;
										    		}
										    		j++;
									 	 		});
									 	 	}else{
									 	 		ts_keyword.hide();
												var html = '<tr><td><input type="checkbox" name="keyzu" value="'+rids+'" checked="checked"/></td><td>'+rids+'</td></tr>';
												webtable[AP](html);
												keyword.val("")
									 	 	}
											if(j == ((webtable[FD]('tr').length) -1)){
										    	ts_keyword.hide();
												var html = '<tr><td><input type="checkbox" name="keyzu" value="'+rids+'" checked="checked"/></td><td>'+rids+'</td></tr>';
												webtable[AP](html);
												keyword.val("");
										    }
										}else{
											ts_keyword.show();
											ts_keyword.html('* 每个关键词最多6个字!');
										}
									}
								}
							}else{
								ts_keyword.show();
								ts_keyword.html('* 一组关键词组最多包含5个关键词!');
							}
						}else{
							ts_keyword.show();
							ts_keyword.html('* 关键词组添加已达到上限!');
						}
					 });
					 
					 //关键词组多选
					 var _keyzu = $("#_keyzu");
					 _keyzu[EV](CK,function(){
					 	if(webtable[FD]('tr').length > 1){
				    		var checkboxs = document.getElementsByName("keyzu");
				    		var flag = checkboxs[0].checked;
				    		for (var i=0;i<checkboxs.length;i++) {
				 				var e=checkboxs[i];
				 				if(i!=0){
				  					e.checked=flag;
				  				}
			 				}
			 			 }
			    	});
					
					refer_to[EV](CK,function(){//参照关键词
				 	 	yc_key[AC]("com_none");
				 	 	yc_acc[RC]("com_none");
				 	 	keyword.val('')
						webcanzhao.html('');
						webcanzhao[AP]('<th style="width:25px;height:22px"></th><th>参照账号预警</th>');
				 	 	//加载参照列表
					 	$(div)[AJ]({
					 		param:{action:'warnservices.warnservicesinsert',moduleType:2,method: 3},
	    					success:function(data){
	    						$.each(data,function(i,item){
	    							var html = '<tr><td><input type="radio" name="canzhao" id="can_zao" value="'+item.id+'" /></td><td>'+item.groupName+'</td></tr>';
									webcanzhao[AP](html);
	    						});
	    					}
					 	});
				 	});
				 	
				 	 //添加or编辑
				 	 $(div)[AJ]({
				 	 		param:{action:'warnservices.warnservicesinsert',accid:warnid,moduleType:2,method: 1},
    						success:function(data){//alert($.toJson(data));
    							if(data.id == 0){
    								Therecipient(_lajiSel,_h2,"",div);
    							}else{
    								id = data.id;
    								Therecipient(_lajiSel,_h2,data.receiver,div);		//加载接收人
									if(data.type != ""){							//加载发布方式
										_strs = data.type.split(",") ;    			//分割字符串
										sendout[FD]("input")[EH](function(){
											var _this = $(this),value = this.value;
											$.each(_strs, function (index, tx) { 	//循环分割的字符串并赋值
												if(tx == value){
													_this[0].checked = true;
												}
							    			});
										});
									}
									if(data.keyword.length != 0){
										$.each(data.keyword, function (index, tx) { 	//循环分割的字符串并赋值
											var html = '<tr><td><input type="checkbox" name="keyzu" value="'+tx+'" checked="checked"/></td><td>'+tx+'</td></tr>';
											webtable[AP](html);
							    		});
									}
									
									//取消预警
									cancelwarn.show();
							 	 	cancelwarn[EV](CK,function(){
							 	 		$(BY)[AJ]({
												param: {action: 'warnservices.warnservicesinsert', id:id,moduleType:2,method: 6 },
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
							 	 	});
									
    							}
    						}
				 	 });
		
				 	 saveBtnkey[EV](CK,function(){//保存预警
				 	 
				 	 	send_check.html('');
				 	 	jsr_check.html('');
				 	 	ts_keyword.html('');
				 	 	var result = [],_result = [],resultTr = [],i=0;
						if(webtable[FD]('tr').length > 1){
				 	 		webtable[FD]('tr td input')[EH](function(){
				 	 			if($(this)[0].checked){
					    			resultTr.push(this.value);
					    			i = 1;
					    		}
				 	 		});
				 	 	}
						$("#newevent_lajiSel input")[EH](function() {
					    	if($(this)[0].checked){
					    		_result.push(this.value);
					    	}
					    });
					    $("#sendout input")[EH](function(){
						   if($(this)[0].checked){
						    	result.push(this.value);
						   }
						});

				 	 	var _keyfs = resultTr.join(";");
					    var _sendfs = result.join(",");
					    var _jsrfs = _result.join(",");
				 	 	
				 	 	if(webtable[FD]('tr').length == 1){
				 	 		ts_keyword.show();
							ts_keyword.html('* 请添加关键词!');
				 	 	}else if(i == 0){
				 	 		ts_keyword.show();
				 	 		ts_keyword.html('* 请选择关键词组!');
				 	 	}else if(_jsrfs == ""){
				 	 		ts_keyword.hide();
				 	 		jsr_check.show();
				 	 		jsr_check.html('* 请选择接收人!');
				 	 	}else if(_sendfs == ""){
				 	 		jsr_check.hide();
				 	 		send_check.show();
				 	 		send_check.html('* 请选择发送方式!');
				 	 	}else{
				 	 		send_check.hide();
				 	 		if(id == 0){
								$(div)[AJ]({
					 	 			param:{action:'warnservices.warnservicesinsert',accid:warnid,topname:topname,keyzu:_keyfs,jsr:_jsrfs,type:_sendfs,moduleType:2,method: 2},
	    							success:function(data){
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
	    						$(div)[AJ]({
					 	 			param:{action:'warnservices.warnservicesinsert',id:id,accid:warnid,topname:topname,keyzu:_keyfs,jsr:_jsrfs,type:_sendfs,moduleType:2,method: 4},
	    							success:function(data){
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
				 	 
				 	 cancelBtnkey[EV](CK,function(){//取消预警
				 	 	 close();
				 	 });

				 	 saveBtnacc[EV](CK,function(){//保存参照
				 	 	canzhao_check.html('');
						var value = $('input:radio[name="canzhao"]:checked').val();
						if(value == null){
							canzhao_check.show();
							canzhao_check.html('* 请选择参照帐号!');
						}else{
							canzhao_check.hide();
							$(div)[AJ]({
					 	 		param:{action:'warnservices.warnservicesinsert',id:value,moduleType:2,method: 5},
	    						success:function(data){
	    							var _num = 0;
	    							$.each(data.keyword, function (index, tx) { 	//循环分割的字符串并赋值
										
										var _var = 0;
					 	 	 			if(webtable[FD]('tr').length > 1){
										 	 webtable[FD]('tr td input')[EH](function(){
											    if(tx == this.value){
													_num++;
													_var++;
													return false;
											    }
										 	 });
										}
										if(_var == 0){
											if(webtable[FD]('tr').length < 21){
												var html = '<tr><td><input type="checkbox" name="keyzu" value="'+tx+'" checked="checked"/></td><td>'+tx+'</td></tr>';
												webtable[AP](html);
												yc_key[RC]("com_none");
									 	 	 	yc_acc[AC]("com_none");
									 	 	}else{
									 	 		yc_key[RC]("com_none");
							 	 	 			yc_acc[AC]("com_none");
												ts_keyword.show();
												ts_keyword.html('* 关键词组添加已达到上限!');
											}
										}
							    	});
							    	if(_num == data.keyword.length){//整个帐号词组重复判断
							    		yc_key[RC]("com_none");
							 	 	 	yc_acc[AC]("com_none");
									 	ts_keyword.show();
										ts_keyword.html('* 参照词组已存在，请重新输入!');
									}
	    						}
	    					});
						}
				 	 });
				 	 
				 	 cancelBtnacc[EV](CK,function(){//取消参照
				 	 	 yc_key[RC]("com_none");
				 	 	 yc_acc[AC]("com_none");
				 	 });
				}
			});
 	}
 	
 	 function Therecipient(sel,_h2,arr,div){//匹配接收人
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
			 				if(i!=0)
			  					e.checked=flag;
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
    			
//    			_h2[EV](CK,function(){
//    				sel[RC]("com_none");
//    			});
    			var state = FE;
				sel[EV](CK, function(e) {
					if(e.target.id == 'alert_id' || e.target.id.indexOf('fail_w') > -1) {
						state = TE;
						sel[RC]("com_none");
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
						}					}
					state = FE;
				});
    		}
    	});
    }
 	
   //---------帐号监控列表操作开始----------------------------//
    
    //点击删除 给出提示，删除事件任务
 	function delete_div(del_id,type){
			$[MB]({
				  	content: '您确定要删除吗？',
					type: 4,
					onAffirm: function (state) { 
						if(state == true){
							$(BY)[AJ]({
									param: {action: 'accountMonitor.deleteAccount', id: del_id},
									success: function(data){
										OnLoadmonitorList(type);
									}
							});
						}
					}
			}); 
 	}
	
	/*
 	//对账号进行暂停（启动）操作
    function stopstart_div(startid,ssid){
    	$('#'+startid,$key)[EV](CK,function(){
    		
    		var flag= $('#'+startid,$key)[AT]("class");				//获取样式
    		if(flag == 'push_type push_type1'){						//点击对样式进行切换
    			$('#'+startid,$key)[RC]("push_type1"); 				//删除样式
          		$('#'+startid,$key)[AC]("push_type2");				//添加样式
    		}else{
    			$('#'+startid,$key)[RC]("push_type2"); 				//删除样式
          		$('#'+startid,$key)[AC]("push_type1");				//添加样式
    		}
        });
    }
    */
	
	 //点击添加 弹出添加窗口
    function eventadd(type){
 			var html = ' <div class="giveMe_do new_tongzhi sel_pingtai"><div class="box_count"><div class="div_style"><label class="com_fLeft">平台选择：</label><select class="task_count pt_select" id="pt_select"><option value="0">新浪微博</option><!--<option value="1">腾讯微博</option>--></select><input type="text" id="textarea" class="task_count addZhao"/></div></div><div style="margin-left: 160px; float: left; color: red; font-size: 12px; display: block;"><span class="com_none" id="check_name" ></span></div><div class="com_clear"></div><div class="push_ok"><div class="in_MsetCno" id="in_MsetCno">取&nbsp;&nbsp;消</div><div class="in_MsetCok" id="in_MsetCok">确&nbsp;&nbsp;定</div></div><div class="com_clear"></div></div>';
			$[WW]({
				 css: { "width": "450px", "height": "auto"},
				 event: 'no',
				 title: '添加帐号',
				 content: html,
				 id: 'id',
				 onLoad: function(div,close) {
					 
					var saveBtn = $(".in_MsetCok",div),cancelBtn = $(".in_MsetCno",div),
						check_name = $("#check_name",div);
					 
				 	$('#textarea').txt({text: '请输入微博账号' });		//设置默认值
				 	$('#textarea').blur(function(){
				 		check_name.hide();
				 	});
				 	
					saveBtn[EV](CK, function() {//保存
						var pt_select = $('#pt_select').val();
						var textarea = $.trim($('#textarea').val());  //添加帐号文本值
						
						if(textarea == '' || textarea == "请输入微博帐号"){
							check_name.show();
							check_name.html("* 请输入微博帐号!");
						}else if(textarea != '') {
						
							$(BY)[AJ]({
						    	param: {action: 'accountMonitor.addAccount',textarea: textarea,type:pt_select,method:1},
						    	success: function (data) {
									if(data == 1){
										check_name.show();
										check_name.html("* 帐号添加失败!");
									}else if(data == 2){
										check_name.show();
										check_name.html("* 帐号名称不存在，请重新输入!");
									}else if(data == 3){
										check_name.show();
										check_name.html("* 该帐号名称已添加，请重新输入!");
									}else if(data == 4){
										check_name.show();
										check_name.html("* 未找到授权信息，无法获得帐号!");
									}else{
										OnLoadmonitorList(type);
										close();
									}
								}
							});
						}
				    });
				    
				    cancelBtn[EV](CK, function() {//取消
						close();
				    });
				 }
 		});    		
    }
  	
    //点击导出 导出数据
    function eventimport(){
        event_import[EV](CK, function(){
 			alert("导出数据");
 		});   		         		
    }
    
	
    //关闭窗体事件
	function CloseWindow(div) {
	   div[RM]();
	   $[RM]({ obj: '#' + JLY });
	}
    //---------监控列表操作结束--------------------------------------------------------//

   	//-----------------------帐号分析开始--------------------------------------------//
    //帐号监测分析签页切换
 	function SwitchingAccounttabs(id,title,type){
		$('#imp_liRight').html('');
 		if(type == 0){
 			$('#imp_liRight',$key)[AP]('<i class="show_zhaoStyle mynum_type1"></i>');
 		}else if(type == 1){
 			$('#imp_liRight',$key)[AP]('<i class="show_zhaoStyle mynum_type2"></i>');
 		}else{
 			$('#imp_liRight',$key)[AP]('<i class="show_zhaoStyle mynum_type5"></i>');
 		}
 		$('#imp_liRight')[AP](title);//追加帐号名称	
    	accounttabs[EV](CK,function(){
          	var This = $(this),index = accounttabs[ID](This);
          	accounttabs[RC]("zhao_back"); 					//删除样式
          	This[AC]("zhao_back");			 				//添加样式

          	switch(index){
	          	case 0:OnLoadanalyList(id,title,type);return;  //实时监测列表信息
		        case 1:FansAnalydiv(id,type);return;		   //粉丝分析
		        case 2:PostAnalydiv(id,type);return;		   //博文分析
	        }
        }).eq(0)[TR](CK);
 	} 
 	
 	//-------------实时监测-开始-------------------------//
 	//实时监测列表信息
    function OnLoadanalyList(id,title,type){
		analydiv[TB]({
             width: 1000,
             trHeight: 24, //表格默认高
             isPager: FE, //是否分页
             isCheckBox: FE, //是否有多选按钮
             id:'_m1',
             rows: 'rows', 
             firstWidth: '5%',
             lastWidth: '10%',
             param: {action :'accountMonitor.realAnalys',id:id,method:1},
			 columns: [{ caption: '监控时间', field: 'monitortime', type: 'string',width: '5%'},
                       { caption: '新增微博数', field: 'addWbnum', type: 'int', width: '5%', style: 'cursor:pointer' },
                       { caption: '新增粉丝数', field: 'addFansnum', type: 'int', width: '5%' },
                       { caption: '新增转发数', field: 'addForwardnum', type: 'int', width: '5%'}],
            //加载表格成功事件
            onComplete: function (This, refresh, data) {
	          	//追加样式
				This[FD]('.t1')[EH](function(){
					var a = $(this).text();
					var _index=This[FD]('.t1')[ID]($(this));
					var addWbnum = data.rows[_index].addWbnum;
					var monitortime = data.rows[_index].monitortime;
					$(this).html('');
					$(this).append('<a gid='+monitortime+'>'+addWbnum+'</a>');
				});
					
	          	//读取微博帐号详情
				This[FD]('tr')[EH](function(){//获取行触发样式
					var tds = $(this).find("td");//行下的所有td
					var gid = $(this)[FD]('a')[AT]('gid');			
					$(tds[1],$(this))[EV](CK,function(){//今天
						NavigationInit_xq();				//展示微博详情导航条
						AccountLoadList(id,gid,title,type);
						$('#account_analy',$key).hide();	//隐藏分析模块
						$('#realtime_analy',$key).hide();	//隐藏分析模块下的实时监测层
						$('#account_details',$key).show();	//展示微博帐号详情
					});
				});
				SwitchingAnalysistabs(id);//趋势分析今天、昨天、七天、30天标签页切换
            }
		});
    }
        
    //实时监测今天、昨天、七天、30天标签页切换
 	function SwitchingAnalysistabs(id,indexs){

		$('#account_talkMe').hide();		//隐藏监测列表层
	    $('#account_details').hide();		//隐藏帐号详情
        $('#account_analy').show();			//显示分析模块
        $('#realtime_analy').show();		//显示分析模块下的实时监测层
        $('#fans_analy').hide();			//隐藏分析模块下的粉丝分析层
        $('#post_analy').hide();			//隐藏分析模块下的博文分析层

		var className = 'selW_selected',lintype = 0,
				  div = $('#likeWeekLine', $key),
				  to_action = 'accountMonitor.realAnalys';
				  
        hour_24[EV](CK, function() {
			var $this = $(this);
			lintype = 1;
			var radiovalue = $('input:radio[name="acc_radion"]:checked').val();
			$this[AC](className); day_7[RC](className);day_30[RC](className);
			LoadDateChar(div, {lintype: lintype,radiovalue:radiovalue},id,to_action);
		})[TR](CK);
			
		day_7[EV](CK, function() {
			var $this = $(this);	
			lintype = 2;
			var radiovalue = $('input:radio[name="acc_radion"]:checked').val();
			$this[AC](className); hour_24[RC](className);day_30[RC](className);
			LoadDateChar(div, {lintype: lintype,radiovalue:radiovalue},id,to_action);
		});
		
		day_30[EV](CK, function() {
			var $this = $(this);
			lintype = 3;		 
			var radiovalue = $('input:radio[name="acc_radion"]:checked').val();   
			$this[AC](className);hour_24[RC](className);day_7[RC](className); 
			LoadDateChar(div, {lintype: lintype,radiovalue:radiovalue},id,to_action);
		});
        
        acc_weibo[EV](CK,function(){
        	var value = $('input:radio[name="acc_radion"]:checked').val();
        	LoadDateChar(div, {lintype: lintype,radiovalue:value},id,to_action);
        });
        acc_reply[EV](CK,function(){
        	var value = $('input:radio[name="acc_radion"]:checked').val();
        	LoadDateChar(div, {lintype: lintype,radiovalue:value},id,to_action);
        });
 	} 
 	
 	//加载线形图图表异步方法btn:点击按钮, div: 显示图表控件, param: 异步参数
	function LoadDateChar(div, param,id,to_action) {
		param = $[EX](param, {action: to_action,id: id,method:2});
		div.html('')[LG]();
		div[AJ]({
			param: param,
			success: function (dataTB) {
				if(dataTB.test == -1) {
					base.not(div);
				} else if(dataTB.test == -2){
					base.not2(div);
				}else{
					base.Char(div, charType.Line, dataTB,0);
				}
			}
		});
	}
    //-------------实时监测-结束-------------------------//

    //-------------粉丝分析-开始-------------------------//
    //
    function FansAnalydiv(id,type){
    	$('#account_talkMe').hide();		//隐藏监测列表层
	    $('#account_details').hide();		//隐藏帐号详情
        $('#account_analy').show();			//显示分析模块
        $('#realtime_analy').hide();		//隐藏分析模块下的实时监测层
        //$('#trend_analy').hide();			//隐藏分析模块下的趋势分析层
        $('#fans_analy').show();			//显示分析模块下的粉丝分析层
        $('#post_analy').hide();			//隐藏分析模块下的博文分析层

        var to_action = "accountMonitor.fanAnalys";
        
        TheCharActData(fans_rz, to_action,id,charType.Pie,1);		 //加载饼型认证比例统计图
        TheCharActData(fans_sex, to_action,id,charType.Pie,2);	 	 //加载饼型性别比例统计图
        TheCharActData(fans_num, to_action,id,charType.ColumnPercent,3);	 //加载用户粉丝数
        TheCharActData(terrain_analy, to_action,id,charType.ColumnPercent,4); //加载地域分析
        TheCharActLi(to_action,id,5);	 							 //加载粉丝最多用户排行
    }
   		//获取用户分析数据(Ajax)
	function TheCharActData(div,actionName,id,charType,method) {
		div.html('')[LG]();
		$(BY)[AJ]({
			param: { action: actionName,id: id,method:method},
			success: function (data) {
				//if((charType ==2 && data.series.data[LN] > 0) || (charType == 0 &&data.series[LN] > 0)) {
				if(data.test != 0){
					base.Char(div, charType, data, 0);
				} else {
					base.not2(div);
				} 
			}
		});
	}
	 
	//加载粉丝最多用户排行
	function TheCharActLi(toaction,id,method){
		fsFans.html('')[LG]();
		var html  ='<ul><li><span class="each_num top_three">{0}</span><span class="each_photo"><img src="{1}" width="25px" height="25px" class="com_fLeft" /></span><span>{2}</span><span class="each_allNum">{3}</span></li><div class="com_clear"></div></ul>';
		$(BY)[AJ]({
			param: { action: toaction,id: id,method:method},
			success: function (data) {
				var fansTemplate  ='<li><span class="each_num top_three">{id}</span><span class="each_photo"><img src="{imgurl}" width="25px" height="25px" class="com_fLeft" /></span><span>{name}</span><span class="each_allNum">{weibos}</span></li><div class="com_clear"></div>';
				if(data.supermans[LN] > 0) {
					fsFans.html('<ul >' + $[EF](fansTemplate, data.supermans) + '</ul>');
				} else {
					base.not2(fsFans);
				}
			}
		});
	}
	 //-------------粉丝分析-结束-------------------------//
    
    //-------------博文分析-结束-------------------------//
    //
    function PostAnalydiv(id,type){
    	$('#account_talkMe').hide();		//隐藏监测列表层
	    $('#account_details').hide();		//隐藏帐号详情
        $('#account_analy').show();			//显示分析模块
        $('#realtime_analy').hide();		//隐藏分析模块下的实时监测层
        //$('#trend_analy').hide();			//隐藏分析模块下的趋势分析层
        $('#fans_analy').hide();			//隐藏分析模块下的粉丝分析层
        $('#post_analy').show();			//显示分析模块下的博文分析层
    	
//    	joblist_bw[EV](CK,function(){
//          	var This = $(this),index = joblist_bw[ID](This);
//          	joblist_bw[RC]("selW_selected"); 						//删除样式
//          	This[AC]("selW_selected");			 					//添加样式
//          	switch(index){
//          		case 0:TheCharLine(id,3,'post_fb');break;   		//加载实时监测统计图
//          		case 1:TheCharLine(id,0,'post_fb');break;   		//加载实时监测统计图
//          	}
//        }).eq(0)[TR](CK);
        
        OnLoadPostList(id,type);									//加载30天热门微博列表 top5 			
    }
    
    //加载30天热门微博列表 top5 
    function OnLoadPostList(id,type){
		postdiv[TB]({
             width: 998,
             trHeight: 24, //表格默认高
             isPager: FE, //是否分页
             isCheckBox: FE, //是否有多选按钮
             id:'_m1',
             rows: 'rows', 
             firstWidth: '5%',
             lastWidth: '10%',
             param: {action :'accountMonitor.bowenAnalys',id:id,type: type},
			 columns: [{ caption: '序号', field: 'id', type: 'number',width: '3%'},
                       { caption: '昵称', field: 'wbname', type: 'string', width: '8%'},
                       { caption: '微博内容', field: '', type: 'html',width: '30%',template:''},
                       { caption: '创立时间', field: 'createdate', type: 'string', width: '10%' },
                       { caption: '评论数', field: 'plnum', type: 'int', width: '5%'},
                       { caption: '转发数', field: 'zfnum', type: 'int', width: '5%'}],
            //加载表格成功事件
            onComplete: function (This, refresh, data) {
	          	if(data.realcount != 0){
					This[FD]('.t2')[EH](function (){
						var index=This[FD]('.t2')[ID]($(this));
						var id = data.rows[index].id;
						var url = data.rows[index].url;
						var text = data.rows[index].text;
						var html = '<a gid="{0}" href="{1}" style="cursor:pointer" target="_blank">{2}</a>';
						var temp = $[FO](html,id,url,text);
						$(this)[AP](temp);
					});
				}else{
					base.not(This);
				}
	          	
            }
		});
    }
    //-------------博文分析-结束-------------------------//
    
    //-----------------------帐号分析结束--------------------------------------------//
    
    
    
    
    
    
    //-----------------------------新增微博详情--------------------------------------------//
    //新增微博详情
    function AccountLoadList(id,gid,title,type) {
    	$('#com_fleft').html('');
    	if(type == 0){
 			$('#com_fleft',$key)[AP]('<i class="show_zhaoStyle mynum_type1"></i>');
 		}else if(type == 1){
 			$('#com_fleft',$key)[AP]('<i class="show_zhaoStyle mynum_type2"></i>');
 		}else{
 			$('#com_fleft',$key)[AP]('<i class="show_zhaoStyle mynum_type5"></i>');
 		}
 		$('#com_fleft')[AP](title);//追加帐号名称	
 		$('#gjkey').val('');
 		$("#dateStart").datePicker();
		$("#dateEnd").datePicker();
		
 		var startDate,endDate;
		var myDate = new Date();
		var tday="",month="";
		if(myDate.getMonth()<9){month="0"+(myDate.getMonth()+1)}else{month=myDate.getMonth()+1}
		if(myDate.getDate()<10){tday="0"+myDate.getDate()}else{tday=myDate.getDate()}
		var tadayTime = myDate.getFullYear()+'-'+month+'-'+tday;
		
		var myDate1 = new Date();
		myDate1.setDate(myDate1.getDate()-6);
		if(myDate1.getMonth()<9){month="0"+(myDate1.getMonth()+1)}else{month=myDate1.getMonth()+1}
		if(myDate1.getDate()<10){tday="0"+myDate1.getDate()}else{tday=myDate1.getDate()}
		var torrTime =  myDate1.getFullYear()+'-'+month+'-'+tday;
		var myDate2 = new Date();
		myDate2.setDate(myDate2.getDate()-29);
		if(myDate2.getMonth()<9){month="0"+(myDate2.getMonth()+1)}else{month=myDate2.getMonth()+1}
		if(myDate2.getDate()<10){tday="0"+myDate2.getDate()}else{tday=myDate2.getDate()}
		var threeday = myDate2.getFullYear()+'-'+month+'-'+tday;
		//var yearTime =  (myDate.getFullYear()-1)+'-'+(myDate.getMonth()+1)+'-'+(myDate.getDate()+1);
		//alert(tadayTime+"-------"+torrTime+"---------"+threeday);
		if(gid == '今天'){
			startDate = tadayTime+' 00:00:00';
			endDate = tadayTime+' 23:59:59';
			$('#dateStart').val(tadayTime);
			$('#dateEnd').val(tadayTime);
		}else if(gid == '7天'){
			startDate = torrTime+' 00:00:00';
			endDate = tadayTime+' 23:59:59'; 
			$('#dateStart').val(torrTime);
			$('#dateEnd').val(tadayTime);
		}else if(gid == '30天'){
			startDate = threeday+' 00:00:00';
			endDate = tadayTime+' 23:59:59'; 
			$('#dateStart').val(threeday);
			$('#dateEnd').val(tadayTime);
		}
		
 		if(base.IsRegExpDateTime($('#dateStart'),$('#dateEnd'))){
			AccountLoadDataBind(id,startDate,endDate,'',type);//首次进来加载详情
		}
		//搜索
		but_search[EV](CK, function() {
			startDate = $('#dateStart').val()+' 00:00:00';
			endDate = $('#dateEnd').val()+' 23:59:59';
			text = $('#gjkey').val();
			if(base.IsRegExpDateTime($('#dateStart'),$('#dateEnd'))){
				AccountLoadDataBind(id, startDate,endDate,text,type);
			}
		});
 		
	}
	
	function AccountLoadDataBind(id, startDate,endDate,text,type){
		$('#account_details',$key).show();
		$('#datesPager', $key).html('');
    	var isSina = TE,
    		html = base.FormatPmi('<span class="alertBtn" alertid="0" url="">预警</span><span>|</span>{pmi:cbfx_monitor_3}{pmi:yqzs_collect_1}{pmi:wdsy_repost_1}{pmi:wdsy_comment_1}');
    	if( type == 1){
    		isSina = FE;
    		html = base.FormatPmi('{pmi:wdsy_repost_1}{pmi:wdsy_comment_1}');
    	}
		base.DataBind({
			panel: accountdiv, 
			pager: $('#datesPager', $key),
			id: id, 
			isFlag:base.isAllExpire,
			isSelectAccount: TE,
			template: html,
			param: {action :'accountMonitor.accountList',accoundid:id,startDate:startDate,endDate:endDate,text:text,type: type,method:2}, 
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
    
    
    return {
    	IsLoad: FE,
        OnLoad: function (_ContentDiv) {
            var t = this;
            contentDiv = _ContentDiv;
            $('<div />').load('modules/accountMonitor/accountMonitor.htm', function () {
            	base = context.Base;
            	cuser=context.CurrentUser;
            	contentDiv[AP](base.FormatPmi($(this).html()));
            	t.IsLoad = TE;
                $key = $('#' + key);
                navigationBars = context.NavigationBars;
                _enum = context.Enum;
                charType = _enum.CharType;
                
                navigationBars = context.NavigationBars;
                base = context.Base;
                labelreview = $('.span_title',$key);			//监控标签对象集合
                accounttabs = $('.sp_anaLeft',$key);			//帐号分析标签切换
                joblist = $('.job_list',$("#realtime_analy"));	//实时监测统计标签切换
                joblist_zf = $('.job_list',$("#zf_type"));		//趋势分析统计转发标签切换
                joblist_pl = $('.job_list',$("#pl_type"));		//趋势分析统计评论标签切换
                joblist_fs = $('.job_list',$("#fans_analy"));	//粉丝分析统计标签切换
                joblist_bw = $('.job_list',$("#post_analy"));	//博文分析统计标签切换
                listdiv = $('#listdiv',$key);					//帐号列表Div
                analydiv = $('#analydiv',$key);					//实施监测列表Div
                postdiv = $('#postdiv',$key);					//博文分析列表DIV
                accountdiv = $('#accountdiv',$key);				//新增微博详情Div
                event_add = $('#eventadd',$key);				//添加事件按钮
                event_import = $('#event_import',$key);			//导出数据按钮
                remen_bq = $('#remen_bq',$key);					//用户热门标签
                hour_24 = $('#hour_24',$key);	
		        day_7 = $('#day_7',$key);	
		        day_30 = $('#day_30',$key);	
		        acc_weibo = $('#acc_weibo',$key);
		        acc_reply = $('#acc_reply',$key);
		        fans_rz = $('#fans_rz',$key); 
		        fans_sex = $('#fans_sex',$key);
		        fans_num = $('#fans_num',$key);
		        terrain_analy = $('#terrain_analy',$key);
		        fsFans = $('#fsFans',$key);
		        but_search = $('#but_search',$key);
                Init();
            });
            return t;
        },
        //初始化方法
        NavToMP: function () {
            Init();
        },
        NavToMP_fx: function () {
            NavigationInit_fx();				//加载导航条
            $('#account_analy',$key).show();	//展示分析模块
			$('#realtime_analy',$key).show();	//展示分析模块下的实时监测层
			$('#account_details',$key).hide();	//隐藏微博帐号详情
        },
        //显示模块方法
        Show:function() {
        	$key.show();
        	Init();		//第二次调用初始化方法
        }
    };
});