/**
*我的评论
*@author shaoqun
**/
define(['portal'], function (context) {
	    //定义变量
	    var key = 'myReview',
	          $key, //当前的作用域
	          navigationBars,
	          commentsReview,
	          getReview,
	          sendReview,
	          labelreview,
	          listdiv,
	          base,
	          mynum_list,
	          _enum,
	          accountCollection,
	          panels,
	          contentDiv;
	           
	    //初始化方法
	    function Init(){  
	    	NavigationInit();
	    	accountCollection = base.GetAccountCollection();
	    	base.CurrentAccountPicker(mynum_list, 'myRe_1', accountCollection, $(".a"), function (flag) {
				Switchingtabs(flag);
			});
	    }

	    //获取选择账号UID
		function GetUID() {
			return $('#currentAccountInfo', $key)[AT]('uid');
		}
		//获取选择账号名称
		function GetUcodename() {
			return $('#currentAccountInfo', $key).text();
		}
	    
	    //导航条初始化方法,
		function NavigationInit(){
	    	navigationBars.Empty()
				.Add(key, 'NavToMP', '我的微博')
	    		.Add(key, 'NavToMP_pl', '我的评论')[oL]();
	    }
	 
	 	//标签页切换
	 	function Switchingtabs(flag){
	    	labelreview[EV](CK,function(){
	          	var This = $(this),//type = This[AT]("type"),
	          		type_id = labelreview[ID](This);
	          	labelreview[RC]("zhao_back"); 			//删除样式
	          	This[AC]("zhao_back");			 		//添加样式

	          	switch(type_id){
	          		case 0: OnLoadReviewList(type_id,flag); return;  //加载收到的评论
	          		case 1: OnLoadReviewList(type_id,flag); return;  //加载发出的评论
	          	}
	          	
	        }).eq(0)[TR](CK);
	 	}

		//加载评论列表
		function OnLoadReviewList(type_id,flag) {

			var pager = $('#allPager', $key);
			listdiv.add(pager).html('');
			listdiv[LS]({//loadlist
	            width: 1000,
	            id: type_id,
	            pager: pager,
	            isOption: FE, //是否显示全选、反选		  
	            param: {action :'myTwitter.myReviewList',type: type_id,uid: GetUID()},
	        		  templates: [{html: '<div class="cont_list"><div class="cont_photo"><a target="_blank"  class="com_fLeft aOpen" href="" ><img src="" class="accountImg" width="50" height="50"></a><div class="photo_allInfor com_none acInfo"></div></div><div class="pl_countLsit"><div class="taikMe_List"><div class="talkMe_count"><div><span class="com_none" id="huifu"></span><span class="Fname_color accountName"></span><span id="pingl_{0}">' }, //评论内容
	                      	      {html: '{0}</span></div>'},  //评论内容
	                      	      {html: '<div class="pl_mar"><span class="pl_mar pl_wdwb"></span><span class="Fname_color Fname_nomal" id="weibo_{0}"><a agid="{0}" href="" target="_blank" style="cursor:pointer">'},  
	                              {html: '{0}</a></span></div>'},  //微博内容
	                              {html: '<p class="pl_mar"><span>{0}</span>'}, //评论时间
	                      	      {html: '<span>来源</span><span>{0}</span>'},//来源
								  {html: base.FormatPmi('{pmi:wdpl_commentsData_1}')},  //要回复的评论id <span class="pl_do" id="review_id{0}">回复</span>
								  //{html: '<span class="com_none" id="weibo_id{0}"></span>'},
								  {html: base.FormatPmi('{pmi:wdpl_deleteReviewData_1}')},//要删除的评论id <span class="pl_do" id="delete_id{0}">删除</span>
								  
								  {html: '</p><div class="com_clear"></div><div class="relay myreview_reply com_none" id="relay_{0}"><div class="ralay_middle"><br />'}, //评论DIV
								  {html: '<div class="com_clear"></div><textarea class="relay_count" id="reviewtextarea_{0}"></textarea>'}, //评论内容
								  {html: '<div class="send_do"><div class="font-tip rWord{0}"></div>'}, 
								  {html: '<div class="send_left"><span class="s_pic s_bqPc" id="bqingbtn_{0}">表情'}, 
								  {html: '<div class="bq_select com_none  panel" id="swbqingpanel_{0}"></div></span>'}, //表情
								  {html: '<input type="checkbox" class="relay_input" id="relaycheck_{0}" /><label for="relay_sel">&nbsp;同时转发到我的微博</label></div><div class="send_right">'}, //是否选中
								  {html: '<div class="send_over send_back" id="replyBut_{0}" isclick="true">评&nbsp;&nbsp;论</div></div><div class="com_clear"></div></div></div></div></div></div></div><div class="com_clear"></div></div> '}], //评论按钮
	      	      
	            columns: [ 		  { field: 'id' },
	            				  { field: 'content'},
	            				  { field: 'id' },
	                              { field: 'posttext'},
	                              { field: 'createtime', maxLength: 200},
	                              { field: 'source' },
	                              //{ field: 'id' },
	                              { field: 'id' },
	                              { field: 'id' },
	                              { field: 'id' },
	                              { field: 'id' },
	                              { field: 'id' },
	                              { field: 'id' },
	                              { field: 'id' },
	                              { field: 'id' },
	                              { field: 'id'}],
	                         
	            onComplete: function (This, refresh, data) {
	            	if(data.realcount == 0){
	            		base.not(This);
	            	}else{
		            	//设置账号头像、昵称
						var accountImg = $('.accountImg', This),
		            		accountName = $('.accountName', This),
		            		aOpen = $('.aOpen',This),
		            		pl_wdwb = $('.pl_wdwb',This);
						accountImg[EH](function (i, item) {
		        		   	var $this = $(this),
		        		   		   index = accountImg[ID]($this),
		        		   		   dataRow = data.rows[index],
		        		   		   account = dataRow.account;
		   		   			$this[AT]('src', account.imgurl);
		   		   			aOpen.eq(index)[AT]('href',account.url);
		   		   			if(type_id == 0){ 
		   		   				$('#review_id'+data.rows[index].id,$key).show();
		   		   				$('#huifu',$key).hide();
		   		   				accountName.eq(i).text(account.name);
		   		   				pl_wdwb.eq(i).text('评论我的微博: ');
		   		   			}else{
		   		   				$('#review_id'+data.rows[index].id,$key).hide();
		   		   				//$('#sg_id'+data.rows[index].id,$key).hide();
		   		   				$('#huifu',$key).show();
		   		   				pl_wdwb.eq(i).text("回复 "+account.name+" 的评论: ");
		   		   				//accountName.eq(i).text('@'+account.name+'');
		   		   			}
		   		   			
						});
						
						//显示用户信息
		            	var accountInfo = $('.acInfo', This);
		            	accountInfo[EH](function() {
							var $this = $(this),
								   index = accountInfo[ID]($this),
								   accountRow = data.rows[index].account;
							
							base.AccountTip($this, accountRow,3,base.isAllExpire);
						});

						$[EH](data.rows,function(i,item){//alert($.toJson(item));
								var id = item.id;
			        		   	var content = base.htmlEncodeByRegExp(item.posttext);	
			        		   	var content2 = base.htmlEncodeByRegExp(item.content);
			        		   	base.AnalyzingImage(content,function(data){
			        		   		$("#weibo_"+id)[FD]('a').html(data);
			        		   	});
			        		   	base.AnalyzingImage(content2,function(data2){
			        		   		$("#pingl_"+id).html(data2);
			        		   	});
			        		   	$("#weibo_"+id)[FD]('a')[AT]('href',item.url);
	
						});
						var commt_power = cuser.Templates['wdpl_commentsData_1'];		//取得回复权限
						var delete_power = cuser.Templates['wdpl_deleteReviewData_1'];	//取得删除权限
						
						//判断回复权限
						if( commt_power != null && commt_power != ""){
							//循环回复 删除
							$[EH](data.rows,function(i,item){
								var id = item.id,
									dataRow = data.rows[i],
			        		   		account = dataRow.account;
			        		   		weibo_id = dataRow.orginalid;
								//点击回复 打开回复面板
								$('#review_id'+id,$key)[EV](CK,function(){
									if(flag == false){
										var styleflag= $('#relay_'+id,$key).css("display");//获取样式是否隐藏
										if(styleflag == "none"){
											 $('#relay_'+id,$key).show();
											 $('#reviewtextarea_'+id).val("回复@"+account.name+"：");
											//显示表情
											 //$('#swbqingpanel_'+id,$key).show();
											 $('.rWord'+id,$key).html('您还可以输入<span class="font_num" >140</span>个字');
											 base.LimitWord($('#reviewtextarea_'+id,$key), $('.rWord'+id,$key), $('#replyBut_'+id,$key));
											 base.FacePicker($('#bqingbtn_'+id,$key), $('#swbqingpanel_'+id,$key), $('#reviewtextarea_'+id,$key), $('.panel',$key));
											
											 $('#replyBut_'+id,$key)[EV](CK,function(){
											 	var $this = $(this), _class = $(this)[AT]("class"),tclass = "send_over send_back",_tclass = "send_back send_over";

											 	if($this[AT]("isclick") == 'true'){
											 		
												 	if(_class == tclass || _class == _tclass){
												 	
													 	var textarea = $('#reviewtextarea_'+id).val(),	  //评论内容
													 		state =  $("#relaycheck_"+id).prop("checked");//是否选中状态
									
													 	if(textarea == "回复@"+account.name+"：" || textarea == ''){
													 		$[MB]({content: '写点东西吧，评论内容不能为空哦。',type: 1}); 
													 	}else{
													 		$this[AT]("isclick",FE);
													 		$(BY)[AJ]({
																param: {action: 'myTwitter.comments', tid: id,orginalid:weibo_id,textarea: textarea,state: state,ucodename: GetUcodename(),uid: GetUID()},
																success: function(data){
																		$('#relay_'+id,$key).hide();
																		OnLoadReviewList(type_id,flag); //评论后重新加载数据列表
																		if(data != '-1'){
																			$[MB]({ content: data, type: 2});
																		}
																		$this[AT]("isclick",TE);
																}
															});
													 	}
												 	}
												 }
											 });
										}else{
											$('#relay_'+id,$key).hide();
										}
									}else{
										$[MB]({ content: "该账号授权已到期，请重新授权", type: 1});
									}
								});
							});
						}
						
						//判断删除权限
						if( delete_power != null && delete_power != ""){
							//循环回复 删除
							$[EH](data.rows,function(i,item){
								var id = item.id,
									dataRow = data.rows[i],
			        		   		account = dataRow.account;
								
								//点击删除 删除评论任务
								$('#delete_id'+id,$key)[EV](CK,function(){
									if(flag == false){
										$[MB]({
											content: '您确定要删除吗？',
											type: 4,
											onAffirm: function (boolen) { 
													if(boolen == true){
														$(BY)[AJ]({
															param: {action: 'myTwitter.deleteReview', tid: id,type: type_id,platform: 1,uid:GetUID()},
																success: function(data){
																	OnLoadReviewList(type_id,flag); //删除成功后重新加载数据列表
																	if(data == '-1'){
																		$[MB]({ content: '删除成功!', type: 0,isAutoClose: TE });
																	}else{
																		$[MB]({ content: data, type: 2});
																	}
																	
																}
														});
													}
											}
										}); 
									}else{
										$[MB]({ content: "该账号授权已到期，请重新授权", type: 1});
									}			
								});
							});
						}
						
					}//判断值结束
	            }
	        });
		}
		
		//确定删除或者取消
		function SettingOption(div,btnid){
			//确定删除
			$("#in_ok",div)[EV](CK, function(){
				CloseWindow(div);
				$(btnid,$key)[PT]()[PT]()[PT]()[RM]();
			});
			//确定取消
			$('#in_no',div)[EV](CK, function() {
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
	            $('<div />').load('modules/myTwitter/myTwitter.myReview.htm', function () {
	            	base = context.Base;
	            	cuser=context.CurrentUser;
	            	contentDiv[AP](base.FormatPmi($(this).html()));
	            	t.IsLoad = TE;
	                $key = $('#' + key);
	                navigationBars = context.NavigationBars;
	                base = context.Base;
					_enum = context.Enum;
					mynum_list = $('#mynum_list', $key); //当前账号开始Div
					accountCollection = []; //该模块使用的账号集合（带状态）
					panels = $('.panels', $key); //所有面板集合
					
	                receivedReview = $('#getReview',$key);			//收到的评论
	                sendReview = $('#sendReview',$key);				//发出的评论
	                labelreview = $('.zhao_change',$key);			//标签对象集合
	                listdiv = $('#listdivv', $key); 					//列表Div
	                Init();
	            });
	            return t;
	        },
	        NavToMP_pl: function() {
	        	//$key.text('我的评论');
	        	Init();
	        },
	        //显示模块方法
	        Show:function() {
	        	$key.show();
	        	Init();		//第二次调用初始化方法
	        }
	    };
	});