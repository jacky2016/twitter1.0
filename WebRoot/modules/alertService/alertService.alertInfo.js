//预警信息
define(['portal'], function (context) {
    //定义变量
    var key = 'alertInfo',
          $key, //当前的作用域
          contentDiv,
          starttime,
          endtime,
          warnservicesSearch,
          warnservicesImport,
          //warnservicesDropDwon,
          warnservicesChangeDropDwon,
          warnservicesAll,
          base,
          cuser,
          mediator,
          clickWhere=UN, //点击的是哪一个的 ( 是铃铛 or 是非点击铃铛---显示的列表)
          warnservicesNewsList,
          warnservicesPager,
          clickFlag=TE, //为了防止多点， 只能点击一次 完成了才能再次点击
          collections=[],  //装收集的tid 的url地址
          monitertions=[], //装 监控tid的 url地址
          selectColOne=[{text:'全部',value:0},
	     							  {text:'帐号预警',value:1},
	     							  {text:'微博预警',value:2},
	     							  {text:'事件预警',value:3}], //选择已发生预警 第二个下拉框集合
          selectColTwo=[{text:'微博信息',value:4}], //选择未发生预警 第二个下拉框集合
          recordEye=[],// 记录有小眼睛的是data.rows的哪几个索引号
          weiboShuzu=[], // 记录是微博预警的数组
          navigationBars;
         
	         
	function Init() {
		NavigationInit();
		SetOptions();
		//OnLoadmonitorList();
		OnLoadList();
    }
    
    	//删除上面的小铃铛的个数
		function PreOnload(This){
		    //var ids=[];
			$('.h2_style',This)[EH](function (){
				if(parseInt( $(this)[AT]('warnreaded'))==0){
					// 添加未读 出现一个new 字样的标识
					$(this)[AP]('<div class="com_fRight weibo_newWarning  alertInfosAll"></div>');
						//ids.push($(this)[AT]('warnserviceslistid'));
				}
			});
			/*
		   var iddou=ids.join();
			$(BY)[AJ]({
						param: {action :'warnservices.warnserviceslist',queryConditions:'readWarnList' ,ids:iddou},
						success: function(data) {
						   //上面的铃铛的个数 减去 当前页面能显示出来的个数
						   var  numBer=parseInt(data);
						   //获取上面的小铃铛的个数
						   var lingdaoNum= parseInt($('#alertInfoCount')[TX]());
						   if(lingdaoNum>=numBer){
						    mediator.DecreaseWarningDecreaseCount(numBer);
						   }else{
						   	 mediator.DecreaseWarningDecreaseCount(lingdaoNum);
						   }
						}//success
					});
					*/
		}
    
    // clickBell    非UN 是点击小铃铛 展现 列表的;  UN是非点击小铃铛 展现 列表的
    function  NoInit(clickBell){
    	NavigationInit();
    	SetOptions();
    	//OnLoadmonitorList();
    	/*
    	if(clickBell!=UN){
    		SetOptions();
    	}else{
    	endtime.datePicker();
	    var tempTimeStart=$.addDate('d', -6, endtime.val()).Format('YYYY-MM-DD');
	  	starttime.datePicker().val(tempTimeStart);
    	}
    	*/
		OnLoadList(clickBell);
    }
    
    //设置 时间 和下拉框选项
    function   SetOptions(){  
     			endtime.datePicker();
	     	    var tempTimeStart=$.addDate('d', -6, endtime.val()).Format('YYYY-MM-DD');
	  	      	starttime.datePicker().val(tempTimeStart);
    		  //第一个下拉框
    		  /****
    			warnservicesDropDwon.input({
	     		id:"warnservicesDropDwon_list",
	     		//
	     		collection:[{text:'全部',value:0},
	     							  {text:'帐号预警',value:1},
	     							  {text:'微博预警',value:2},
	     							  {text:'事件预警',value:3}] 
	     		//
	     		//0 未发生预警   1 已发生预警
	     		collection:[{text:'已发生预警',value:1},
	     							  {text:'未发生预警',value:0}],
	     		onChange:function(value){// value 选中的值
					  warnservicesChangeDropDwon.html('');
	     			  if (value==1){
	     			  	warnservicesChangeDropDwon.input({
			     		id:"warnservicesChangeDropDwon_list",
			     		collection:selectColOne
			     	});
	     			  }else{
	     			  	warnservicesChangeDropDwon.input({
			     		id:"warnservicesChangeDropDwon_list",
			     		collection:selectColTwo
			     	});
	     		   }
	     	 } //onChange 结束
	     });
	     */
	     
	     	
	     	//第二个下拉框
	     	warnservicesChangeDropDwon.input({
	     		id:"warnservicesChangeDropDwon_list",
	     		collection:selectColOne
	     	});
	     	
    }  //setOptions 结束
    
     //预警信息列表(微博预警和微博信息 为一类;  帐号预警和事件预警为一类)
     //未发生预警 0   已发生预警 1 
     //已发生预警
    function  OnLoadList(){
   				var stimes=starttime.val(),
    			       etimes=endtime.val(),
    			       //selectValueOne=warnservicesDropDwon[AT]('value'),
                       selectValueTwo=warnservicesChangeDropDwon[AT]('value'),
                       //bellFlag   0   点击铃铛出来的列表  1 非点击铃铛出来的列表
                       bellFlag=1;
					   //点击铃铛出来的列表
				 if( arguments[0]!=UN&&parseInt( arguments[0])==0){
						   			bellFlag=0;
				  }
		       warnservicesAll.add(warnservicesPager).html('');
    		   warnservicesAll[LS]({//loadlist
	            width: 1000,
	            pageSize: 10,
	            isPager: TE, 
	            id: "warn_ServicesList",
	            pager: warnservicesPager,
	            isOption: FE, //是否显示全选、反选		  
	            param: {action :'warnservices.warnserviceslist',queryConditions:'warnservicesShowList',stimes:stimes,etimes:etimes, //selectOne:selectValueOne,
	            selectTwo:selectValueTwo,bellFlag:bellFlag},
	        		  templates: [{html: '<div class="warning_newsList"><h2 class="h2_style"  warnserviceslistid="{0}"  '}, //序号 真正的序号
	        		  							{html:' warnreaded="{0}"><div class="com_fLeft h2_count">'},  //  已读 1 未读 0 
	        		  						  {html:'<span class="font-color">{0}&nbsp;&nbsp;</span>'},  //序号 以1 开始自生长
	        		  						 // {html: '监测到{0}平台'},  // 什么平台 新浪  or 人民 or 腾讯
	        		  						 // {html: '<span class="font-color">{0}</span>'},  //[爱情保卫战] (帐号or 事件 预警)|微博预警和微博信息   hhhh..
	        		  						 // {html: '{0}</div>'},  //账号博主在微博中提到了您设置的关键词（帐号预警） |(事件预警) 监测到新浪平台上“MM”事件发生预警，微博数量已经超过XX。 | (微博信息) 监测到新浪平台上微博“MM（列表上显示微博前15字）”发生预警，转发/评论已超过XX。
	        		  					      {html:'{0}</div>'},
	        		  						   {html:'<div title="删除" class="com_fRight  warn_del"  id="warn_Delete_{0}" ></div>        <div title="查看详情" class="com_fRight weibo_allShow"></div></h2></div>'}],
	        		  						  //{html: '<div title="查看详情" class="com_fRight weibo_allShow{0}"></div></h2></div>'}], // {0} 为空字符串 即为微博信息or 微博预警 ;为其他的则为 帐号or事件预警

	            columns: [{ field: 'id'},
	            					{ field: 'readed'},
	            					{ field: 'autoId'},
	            					//{ field: 'platformStr'},
	            					//{ field: 'weiboTitleName'},
	            					//{ field: 'differentLanguages'},
	            					{ field: 'weiboTitleName'},
	            					{ field: 'id'}],  //,{ field: 'warnServicesType'}
	                              
	            onComplete: function (This, refresh, data) {
	            recordEye=[];
	            weiboShuzu=[];
	           //搜索按钮判断开始时间不能大于结束时间
	     		warnservicesSearch[EV](CK,function (){
	     	      var tt	= base.IsRegExpDateTime(starttime,endtime,TE);
		    	  if(tt==TE){
						 OnLoadList();
		    	  }
	     		});  // 判断起始时间不能大于结束时间
         			    
         		      	if(data.rows.length==0){
	            		base.not(This);
	            	  	return  FE;
	            	  }else{
	            	  	 //此页面出现的到底有几条是未读的消息
         			     PreOnload(This);
	            	  	
		              //记录有眼睛的是集合的哪几个索引号  recordEye数组
				        $[EH](data.rows,function(i,item){
				        	   /*
				        	   if(item.warnServicesType==''){
				        	   	 	    recordEye.push(item);
				        	   }
				        	   */
				        	     recordEye.push(item);
				        });
              
              
	          			// 有眼睛的可以点击(画里面的页面)
						if(recordEye[LN]>0){
						// 开始画下面的页面
					           DrawPanle(This,  data,recordEye);
					           //预警按钮触发的事件以及预警按钮当前的状态
		    			       //yujingBtnHandle(This,  data,recordEye);
					           
						        MonitorState(This,  data,weiboShuzu);
						        //阴影部分的转发  or 评论数的点击按钮事件
						        ClickBtnHandle(This,  data,weiboShuzu);
						        
						       OpanPanel(This,  data,recordEye);	       
								//删除按钮 触发的方法
								DeleteHandle(This,  data,recordEye);
						}
	            	  
	            	  }  //有数据data的
	            
	            }  //onComplete结束
	        });   //loadlist结束
    }  //OnLoadList结束 
    
	    
    //删除按钮 触发的方法
	function 	DeleteHandle(This,  data,recordEye){
			  $('.com_fRight.warn_del',This)[EH](function (){
			  		var  index=  $('.com_fRight.warn_del',This)[ID]($(this));
			  		$('#warn_Delete_'+recordEye[index].id  , This)[EV](CK,function (){
			  		$thisbtn=$(this);
			  	var 	html='<div class="giveMe_do del_reSee"><div class="giveMe_doDel" >您确定要删除吗？</div> <div class="push_ok"><div class="in_MsetCno" id="cancel">取&nbsp;&nbsp;消</div><div class="in_MsetCok" id="ok">确&nbsp;&nbsp;定</div></div><div class="com_clear"></div></div>';
				$[WW]({
		        css: { "width": "440px", "height": "auto"},
		        title:'&nbsp;&nbsp;预警通知',
		        content: html,
		        id: 'warningTongzhi'+recordEye[index].id,
		        onLoad: function(div) {
		        		BtnHandle($thisbtn,recordEye[index].id,div,This,data,recordEye);	 
				      		}   // onLoad  end
    					});  //WW end
			  		 });  //EV  end
			  });  // EH   end
	}  // DeleteHandle     end
    
    //删除按钮弹出层中的确定，取消按钮触发事件
    function  BtnHandle($thisbtn, id ,div,This,  data,recordEye){
    			   $('#cancel',div)[EV](CK,function(){
			   		 CloseWindow(div);
			   });
		
			$('#ok',div)[EV](CK,function (){
			 CloseWindow(div);
    		  $(BY)[AJ]({
   				   param: {action :'warnservices.warnserviceslist',queryConditions:'deleteWarnNofity' ,warnID:id},
    			   success: function (data) {
						var  newbar=	$thisbtn[PT]()[FD]('.weibo_newWarning');
						if(newbar[LN]>0){
							//获取上面的小铃铛的个数
						   var lingdaoNum= parseInt($('#alertInfoCount')[TX]());
						   if(lingdaoNum>=1){
						    mediator.DecreaseWarningDecreaseCount(1);
						   }else{
						   	 mediator.DecreaseWarningDecreaseCount(lingdaoNum);
						   }
						}  //if 
						OnLoadList(clickWhere);
    			} 
			});			
			}); // ok EV end
    
    }  //BtnHandle  end
    
    
     //关闭窗体事件
    function CloseWindow(div) {
      div[RM]();
      $[RM]({ obj: '#' + JLY });
    }
    
    
      //阴影部分的转发  or 评论数的点击按钮事件
    function      ClickBtnHandle(This,  data,weiboShuzu){
    
    			var repostCount = $('.repostCount', This),
					    commentCount  = $('.commentCount', This),
					    noRepostCount =  $('.noRepostCount', This),
						noCommentCount  = $('.noCommentCount', This);
						//isSelectAccount: TE,转发评论面板是否有选择账号功能
						isSelectAccount=TE;
    
    		 //转发的权限 
    		 if(repostCount[LN]>0){
    		 		repostCount[EH](function(i, item) {
								var $this = $(this);
							    $this[AT](ID, i);
			      });
			      var Repost =base.getRepost();
			     CreateView(This, weiboShuzu, 'repostCount',  Repost  ,isSelectAccount , 2, onSend); //倒数第2个是 2 是talkme 模版的枚举值
    		 }  // if
    		 //没有转发的权限
    		 else{
    		 		noRepostCount[EH](function(i, item) {
								var $this = $(this);
							    $this[AT](ID, i);
							});
    		 }
		
		//评论的权限
		if(commentCount[LN]>0){
				commentCount[EH](function(i, item) {
						var $this = $(this);
					    $this[AT](ID, i);
			    });
			    var  Comment=base.getComment();
			CreateView(This, weiboShuzu, 'commentCount',  Comment  , isSelectAccount,2,onSend ); //倒数第2个是 2 是talkme 模版的枚举值
		}
		//没有评论的权限
		else{
		 	noCommentCount[EH](function(i, item) {
					var $this = $(this);
					$this[AT](ID, i);
				});
		}
    		
    }   //  ClickBtnHandle 结束
    
    
    
    /* 创建转发评论视图业务
	 * This: 当前列表
	 * data:数据(这里需要传入 recordEye  这个数据)
	 * observer:被观察的对象名称
	 * Class:类
	 * isSelectAccount: 转发评论面板是否能选择账号
	*/
	function CreateView(This, data, observer, Class, isSelectAccount, moduleType, fn) {
		var parent = ('#warnserviceAll', This),
			   repostView = $('.repostView ', This),
			   commentView = $('.commentView', This);
		observer = '.' + observer;
		parent[EV](CK + observer, observer, function() {
			var $this = $(this),
				   index = $this[AT](ID),
				   panel = $this[PT]()[PT]()[NT](),
				   dataRow = data[index].postDTO,
				   accountRow = dataRow.account,
				   user = context.CurrentUser;
			var type = accountRow.twitterType, //user.GetType(accountRow.uid),
			 	   accounts = user.GetAccountByType(type);
			//如果有选择账号功能，并且账号长度为0
			if (isSelectAccount && accounts[LN] == 0) {
				$[MB]({content: '您没有要发送的账号,请与管理员联系', type: 2});
				return;
			}
			show(moduleType);

			function show(moduleType) {
				repostView.add(commentView).hide();
				panel.show();
				var instance = new Class();
				instance.Init(panel, dataRow, isSelectAccount, accounts, moduleType, 0); // 最后一个参数 currentAccountID 就是选择帐号下拉菜单的uid
				
				Class.fn.Sended = function ($this, txt, isCkeck, accountCollection, postID, url, type, twitterID, closeBtn) {
					fn($this, txt, isCkeck, accountCollection, postID, url, type, twitterID, closeBtn);
			   	}
			   	
			}
		});
	}    // CreateView结束
    
    
    // onSend  转发  或 评论的发送按钮
     function onSend(btn,txt, isChecked, accountCollection, postID, url, type, twitterID, closeBtn ,name){
				 //评论或者转发触发的事件
				var param = {};
			//	param.action = type == 'repost'? 'talkMe.talkMeRepost': 'talkMe.talkMeComment';
			   // param.queryConditions=type == 'repost'? 'talkMeRepost': 'talkMeComment';
				param.action = type == 'repost'? 'myTwitter.saveReport': 'myTwitter.saveComment';
				param.isChecked = isChecked;

					if (type == 'repost' ) {
					//不填转发内容的时候，默认值
					if ($[TM](txt) == '') {
						txt = '转发微博';
					}
				} else {
					if ($[TM](txt) == '') {
						$[MB]({
							content: '评论内容不能为空', 
							type: 2,
							isAutoClose: TE
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
     
     }   //  onSend 结束
    
    
    
    
    
      //微博内容的转义字符和一些标注功能
      function  ParseText(content){
									//去掉 不是img 标签的， 要转义成字符
									content= base.htmlEncodeByRegExp(content);
							  		//解析存在有表情的转化成图片
							  		base.AnalyzingWord(content,function (data){
							  			content=data;
							  		    //解析如果存在URL地址的变样式
							  		    //content=	base.AnalyzingUrl(content);
							  		    
							  		    //解析有@+名称的变样式
							  	     	base.AnalyzingTwitterName(content,function (data){content=data});
							  	        //解析如果存在有图片的显示出来
							  		    base.AnalyzingImage(content,function (data){content=data});
							  		});
							  		return  content;
      }
    
    
    
    //画页面 微博信息 or 微博预警的的下面的div
    /** 各大参数
          {0} 头像的url微博地址
		｛1｝头像的图片地址
		｛2｝点击头像进去的大头像图片地址
		{3}微博帐号名
		{4} 微博数
		｛5｝粉丝数
		{6} 关注数
		{7} 简介
		{8}和头像是一致的微博名 (大头像里面的微博名)
		{9} 原创白色的块的 内容
		{10}   阴影部分的 微博名
		{11}  阴影部分的 内容
		{12}   阴影的微博地址
		{13}  阴影 发微博的日期
		{14}  阴影 发微薄的时间
		｛15｝ 阴影 哪个平台发的微博 (新浪。腾讯，人民)
		{16}  阴影 此微博的转发数
		{17}   阴影 此微博的评论数
		{18} 原创微博的url地址 [10]
		{19}  原创微博的日期  [11]
		{20}   原创微博的时间   [12]
		｛21｝ 原创微博在哪个平台发的 (新浪。腾讯，人民)  [13]
		
		{10}--{17} 出现在阴影部分 可能有 可能没有
		---------
		cont_img  为原创的图片
		deal_img   转发的图片
		
		com_fRight subBtnPanel  阴影部分的权限按钮(监控)  pmi:cbfx_monitor_4   
		 监控 只有一个 ，只监控原帖的
		cont_doright buttons 空白的权限按钮 可能有(监控    pmi:cbfx_monitor_3) ,  转发 评论 收集
    */
    function  DrawPanle(This,  data,recordEye){
       var   montiorsURL=[];
    	var  dataRows=data.rows;
    	 //var html='<div class="cont_list"><div class="cont_photo"><a target="_blank"class="com_fLeft aOpen"href="{0}"><img src="{1}"width="50"height="50"/></a><div class="wb_listType mynum_type1"></div><div class="photo_allInfor com_none"><div class="photo_allInforBack"><div class="timing_tip photo_pos"></div><img src="{2}"width="80"height="80"class="jies_photo"/><div class="jies_list"><div class="doSee_me"style=" visibility:hidden;">+关注</div><div class="com_clear"></div><div class="photo_Fcolor photo_Fweight">{3}</div><div>微博<span class="photo_Fcolor">[{4}]</span>&nbsp;|&nbsp;粉丝<span class="photo_Fcolor">[{5}]</span>&nbsp;|&nbsp;评论<span class="photo_Fcolor">[{6}]</span></div></div><div class="com_clear"></div><div class="jies_count">简介：{7}</div></div></div></div><div class="taikMe_List"><h2><span class="Fname_color">{8}</span><br/><span class="contentArea">{9}</span></h2><!--yuanchaung--><div class="cont_img"></div><div class="com_clear"></div><!--zhuanfa--><div class="raly_countList"><div class="talkMe_count"><div class="deal_tipPic"></div><div class="deal_count"><span class="Fname_color">{10}</span><br/><span class="contentArea">{11}</span></div><div class="com_clear"></div><div class="deal_img"></div><p class="pl_mar"><a class="com_fLeft"target="_blank"href="{12}">{13}{14}</a><span>来源</span><span>{15}</span><span>转发{16}</span><span>评论{17}</span></p><p class="com_fRight subBtnPanel"><!--<font class="buttonStyle jiance com_fRight">监控</font><font url="http:////"twittertype="1"iscreative="false"moniterid="0"class="buttonStyle moniterBtnAll jiance com_fRight">监控</font>--></p></div><div class="com_clear"></div></div></div><div class="com_clear"></div><div class="cont_do"><div class="cont_doleft"><a target="_blank"href="{18}">{19}{20}</a><span>来源</span><span>{21}</span></div><div class="cont_doright buttons"><!--<span>监控</span><span>|</span><span>收集</span><span>|</span><span class="pz_pos">转发[<b>23</b>]<a class="pz_tip timing_tip"></a></span><span>|</span><span class="pz_pos">评论[<b>23</b>]<a class="pz_tip timing_tip"></a></span>--></div></div><div class="relay repostView com_none"></div><div class="relay commentView com_none"></div><!--relay end--></div>';
    		var html='<div class="cont_list com_none"><div class="cont_photo"><a target="_blank"class="com_fLeft aOpen"href="{0}"><img src="{1}"width="50"height="50"/></a><div class="wb_listType mynum_type1"></div><div class="photo_allInfor acInfo  com_none"><div class="photo_allInforBack"><div class="timing_tip photo_pos"></div><img src="{2}"width="80"height="80"class="jies_photo"/><div class="jies_list"><div class="doSee_me"style=" visibility:hidden;">+关注</div><div class="com_clear"></div><div class="photo_Fcolor photo_Fweight">{3}</div><div>微博<span class="photo_Fcolor">[{4}]</span>&nbsp;|&nbsp;粉丝<span class="photo_Fcolor">[{5}]</span>&nbsp;|&nbsp;评论<span class="photo_Fcolor">[{6}]</span></div></div><div class="com_clear"></div><div class="jies_count">简介：{7}</div></div></div></div><div class="taikMe_List"><h2><span class="Fname_color">{8}</span><br/><span class="contentArea">{9}</span></h2><!--yuanchaung--><div class="cont_img"></div><div class="com_clear"></div><!--zhuanfa--><div class="raly_countList repostLst"></div></div><div class="com_clear"></div><div class="cont_do"><div class="cont_doleft"><a target="_blank"href="{10}">{11}{12}</a><span>来源</span><span>{13}</span></div><div class="cont_doright buttons"><!--<span>监控</span><span>|</span><span>收集</span><span>|</span><span class="pz_pos">转发[<b>23</b>]<a class="pz_tip timing_tip"></a></span><span>|</span><span class="pz_pos">评论[<b>23</b>]<a class="pz_tip timing_tip"></a></span>--></div></div><div class="relay repostView com_none"></div><div class="relay commentView com_none"></div><!--relay end--></div>';
    	//事件 or  帐号预警的模版
    	var  notWeibohtml='<div class="event_list com_none">{0}</div>';
    	$('.weibo_allShow',This)[EH](function (i){
    	     var mpd=recordEye[i].postDTO;
    	     //为事件预警 or   帐号预警 
    	     if(mpd==UN){
    	     	   var  addHTML=$[FO](notWeibohtml,recordEye[i].weiboTitleName);
    	     		$(this)[PT]()[PT]()[AP](addHTML);
    	     }  //   为事件预警 or   帐号预警  End   mpd==UN
    	     //微博预警
    	     else {
					   weiboShuzu.push(recordEye[i]);
    			var accountHerfURL=mpd.account.url ,//头像的微博地址 0
    			       accountImageURL=mpd.account.imgurl,   //头像url微博地址;  1
    			       inaccountImageURL=mpd.account.imgurlBig,   //点击头像进去的大头像图片地址 2
    	        	   inaccountName=mpd.account.name,  //微博帐号名 3
    	        	   inaccountWeiboNum=mpd.account.weibos , //微博数  4
    	               inaccountFensiNum=mpd.account.followers,//粉丝数  5
    	               inaccountViewNum=mpd.account.friends ,//关注数  6
    	               inaccountSummany=mpd.account.summany,// 简介 7
    	               //和头像是一致的微博名(大头像里面的微博名) 8    其实和inaccountName是一样的
    	              whiteAreaContent=mpd.text,//原创白色的块的 内容 9
    	              whiteAreaContent=ParseText(whiteAreaContent);
    	              /*
    	              blackAreaAccountName=mpd.item.account.name,//阴影部分的 微博名  10
    	              blackAreaContent=mpd.item.text,//阴影部分的 内容  11
    	              blackAreaContent=ParseText(blackAreaContent);
    	              
    	              blackAreaWeiboUrl=mpd.item.url,// 阴影的微博地址 12 
    	              blackAreaPublicDate=mpd.item.createtime,//阴影 发微博的日期 13
    	              //14 为'' 因为日期里已经包含了 时分秒了
    	              blackAreaPlatformType='新浪',//阴影 哪个平台发的微博 (新浪。腾讯，人民)  15  默认都为新浪 
    	              blackAreaRepostNum=mpd.item.repostCount,//阴影 此微博的转发数  16
    	              blackAreaCommentsNum=mpd.item.commentCount,//  阴影 此微博的评论数  17 
    	              */
    	              
    	              whiteAreaWeiboUrl=mpd.url ,//原创微博的url地址  18  [10]
    	              whiteAreaPublicDate=mpd.createtime,//原创微博的日期  19  [11]
    	              //原创微博的时间 为'' 因为日期里已经包含了 时分秒了    20  [12]
    	              whiteAreaPlatformType=mpd.source;//原创微博在哪个平台发的 (新浪。腾讯，人民)  21     默认都为新浪  [13] 
    	              
		    	        var addHtml=$[FO](html,
		    	        accountHerfURL,//0
		    	        accountImageURL,//1
		    	        inaccountImageURL,//2
		    	        inaccountName,//3
		    	        inaccountWeiboNum,//4
		    	        inaccountFensiNum,//5
		    	        inaccountViewNum ,//6
		    	        inaccountSummany,//7
		    	        inaccountName,//8
		    	        whiteAreaContent,//9
		    	        /*
		    	        blackAreaAccountName,//10
		    	        blackAreaContent,//11
		    	        blackAreaWeiboUrl,//12
		    	        blackAreaPublicDate,//13
		    	        '',//14
		    	        blackAreaPlatformType,//15
		    	        blackAreaRepostNum,//16
		    	        blackAreaCommentsNum,//17
		    	        */
		    	        whiteAreaWeiboUrl,//18   [10]
		    	        whiteAreaPublicDate,//19  [11]
		    	        '',//20  [12]
		    	        whiteAreaPlatformType//21  [13] 
		    	        );
		    	        
		    			$(this)[PT]()[PT]()[AP](addHtml);
		    			//判断是否是转发帖
		    			if(mpd.item!=UN){
		    			
		    		    var  yinyingpart=$(this)[PT]()[PT]()[FD]('.raly_countList.repostLst');
		    		    var yinyingTemp='<div class="talkMe_count"><div class="deal_tipPic"></div><div class="deal_count"><span class="Fname_color">{0}</span><br/><span class="contentArea">{1}</span></div><div class="com_clear"></div><div class="deal_img"></div><p class="pl_mar"><a class="com_fLeft"target="_blank"href="{2}">{3}{4}</a><span>来源</span><span>{5}</span><span>转发{6}</span><span>评论{7}</span></p><p class="com_fRight subBtnPanel"></p></div><div class="com_clear"></div>';
		    		    var yinyingHtml='';
		    		    
		    	   var blackAreaAccountName=mpd.item.account.name,//阴影部分的 微博名  10    --0
	    	              blackAreaContent=mpd.item.text,//阴影部分的 内容  11  --1
	    	              blackAreaContent=ParseText(blackAreaContent);
	    	              
	    	              blackAreaWeiboUrl=mpd.item.url,// 阴影的微博地址 12  --2
	    	              blackAreaPublicDate=mpd.item.createtime,//阴影 发微博的日期 13  --3 
	    	              //14 为'' 因为日期里已经包含了 时分秒了  ---4
	    	              blackAreaPlatformType=mpd.item.source,//阴影 哪个平台发的微博 (新浪。腾讯，人民)  15  默认都为新浪   ---5
	    	              blackAreaRepostNum=mpd.item.repostCount,//阴影 此微博的转发数  16  ---6
	    	              blackAreaCommentsNum=mpd.item.commentCount,//  阴影 此微博的评论数  17  ---7
		    		  
		    		    yinyingHtml+=$[FO](yinyingTemp,blackAreaAccountName,blackAreaContent,blackAreaWeiboUrl,blackAreaPublicDate,'',
		    		    blackAreaPlatformType,blackAreaRepostNum,blackAreaCommentsNum);
		    		    
		    		    yinyingpart[AP](yinyingHtml);
		    		    
		    		    //阴影部分如果有图片的画图片	deal_img   转发的图片
						var  deal_img= $(this)[PT]()[PT]()[FD]('.deal_img');
		    	        if(mpd.item.postImage[LN]>0){
		    	        var  htm=	 addImageHTML(mpd.item.postImage);
						deal_img[AP](htm);
		    	        }
		    	        /*
		    	        else{
		    	        	deal_img[AC]('com_none');
		    	        }
		    	        */
		    		    
		    			 //阴影部分的权限监控按钮
		    	        //阴影如果有， 只显示在阴影部分		    		pmi:cbfx_monitor_4	
		    			     var   monitor=  cuser.Templates['cbfx_monitor_4'];
		    			      if(monitor!=UN){
		    			      		//var  monitorTemp=  $[FO](monitor,mpd.item.url,mpd.item.account.twitterType, mpd.isCreative,0);  
		    			      		$(this)[PT]()[PT]()[FD]('.com_fRight.subBtnPanel')[AP](monitor);
		    			      }
		    			      
		    			//阴影部分的预警按钮权限  pmi :   yjfw_yjxxinsert_2
		    			/*
						var  warnservicesBtn=cuser.Templates['yjfw_yjxxinsert_2'];
						if(warnservicesBtn!=UN){
						  $(this)[PT]()[PT]()[FD]('.com_fRight.subBtnPanel')[AP](warnservicesBtn);
						} 
		    			*/
		    			
		    			} // 转发帖子画出的阴影结束
		    			
		    	        //原创也就是白色部分如果有图片的   	cont_img  为原创的图片	
						var 	cont_img=$(this)[PT]()[PT]()[FD]('.cont_img');
		    	        if(mpd.postImage[LN]>0){
		    	        var  htm=	 addImageHTML(mpd.postImage);
						cont_img[AP](htm);
		    	        }
		    	        /*
		    	        else{
		    	        cont_img[AC]('com_none');
		    	        }
		    	        */
		    	        
		    	        //白色部分(也就是原帖部分)的预警权限按钮  yjfw_yjxxinsert_1
		    	        /*
		    	        var   white_warnservicesBtn= cuser.Templates['yjfw_yjxxinsert_1'];
		    	        if(white_warnservicesBtn!=UN){
		    	        		$(this)[PT]()[PT]()[FD]('.cont_doright.buttons')[AP](white_warnservicesBtn);
		    	        }
		    	        */
		    			
		    			  //权限监控按钮
		    			//阴影没有显示在原创位置(也就是白色部分)  pmi:cbfx_monitor_3
		    			if($(this)[PT]()[PT]()[FD]('.raly_countList.repostLst')[FD]('.talkMe_count')[LN]==0){
		    			   var   monitor=  cuser.Templates['cbfx_monitor_3'];
		    			      if(monitor!=UN){
		    			           //var  monitorTemp=  $[FO](monitor,mpd.url,mpd.account.twitterType, mpd.isCreative,0);  
		    			      		$(this)[PT]()[PT]()[FD]('.cont_doright.buttons')[AP](monitor);
		    			      }
		    			}
		    			
		    			// 其他收集 ,转发 评论的权限  {pmi:yqzs_collect_1}{pmi:wdsy_repost_1}{pmi:wdsy_comment_1}
		    			//在 这个地方     .cont_doright.buttons   添加
		    			
		    			var  collectBtn=cuser.Templates['yqzs_collect_1'],
		    			         repostBtn=  cuser.Templates['wdsy_repost_1'],
		    			         commentBtn=cuser.Templates['wdsy_comment_1'];
		    		   var   quanxian=$(this)[PT]()[PT]()[FD]('.cont_doright.buttons');
		    		   	if(collectBtn!=UN){
		    		   		      quanxian[AP](collectBtn+repostBtn+commentBtn);
		    		   	}else{
							      quanxian[AP](repostBtn+commentBtn);
		    		   	}
		    		   var  len=quanxian[FD]('span')[LN];
		    			    if(len>0){
		    			    		var shuxian =quanxian[FD]('span').eq(len-1);
								if($[TM](shuxian[TX]())=='|'){
									quanxian[FD]('span').eq(len-1)[RM]();
								}
		    			    }
		    			
		    			//获取监控按钮的状态
		    			if($(this)[PT]()[PT]()[FD]('.com_fRight.subBtnPanel')[FD]('.moniterBtnAll')[LN]>0){
		    				$(this)[PT]()[PT]()[FD]('.com_fRight.subBtnPanel')[FD]('.moniterBtnAll').eq(0)[AT]({url:whiteAreaWeiboUrl});
		    			    montiorsURL.push($(this)[PT]()[PT]()[FD]('.com_fRight.subBtnPanel')[FD]('.moniterBtnAll').eq(0)[AT]('url'));
		    			}else if($(this)[PT]()[PT]()[FD]('.cont_doright.buttons')[FD]('.moniterBtnAll')[LN]>0){
		    			$(this)[PT]()[PT]()[FD]('.cont_doright.buttons')[FD]('.moniterBtnAll').eq(0)[AT]({url:whiteAreaWeiboUrl});
		    			montiorsURL.push($(this)[PT]()[PT]()[FD]('.cont_doright.buttons')[FD]('.moniterBtnAll').eq(0)[AT]('url'));
		    			}    	     	
    	     	
    	     }  // else   微博预警End --  mpd!= UN
		    			
    	});  //EH 结束  ---------画页面结束
    		
    				//鼠标划过头像，出现大图像DIV 展示出来
    				//显示用户信息
	            	var accountInfo = $('.acInfo', This);
	            	accountInfo[EH](function(i) {
							 var  accountRow = weiboShuzu[i].postDTO.account;
							   base.AccountTip($(this), accountRow,2,FE);
					});
					
					
						 //收集按钮
		    			collectBtnHandle(This,  data,weiboShuzu);
		    			
		    			// 转发数 ,有数的画写上  
		    			$('.repostCount',This)[EH](function (i){
		    					$(this)[AT]({index:i});
		    			      	if(weiboShuzu[i].postDTO!=UN){
		    			      	  var  num= 	weiboShuzu[i].postDTO.repostCount;
		    			      	   if(num>0){
		    				  	$(this)[TX]('转发['+num+']');
		    				  }
		    			      	}
		    			});
		    			// 评论数 ,有数的画写上
		    				$('.commentCount',This)[EH](function (i){
		    				  $(this)[AT]({index:i});
		    				  if(weiboShuzu[i].postDTO!=UN){
		    				  var  num= 	weiboShuzu[i].postDTO.commentCount;
		    				  if(num>0){
		    				    	$(this)[TX]('评论['+num+']');
		    				  }
		    				  }
		    			});
					
    	   var   murls=montiorsURL.join(',');
    	   var moniterBtn=$('.moniterBtnAll',This);
    	   	if(moniterBtn[LN]>0){
    	  		//读取收集状态
		     			$(BY)[AJ]({
							param: { action: 'view.getCollectState', urls: murls},
							success: function (map) {
								//设置监控状态
								$[EH](map, function (key, tweetStatus) {
									if (tweetStatus.moniterID != 0) {
										moniterBtn[EH](function (index) {
											var $this = $(this),
											       code = $this[AT]('url');
											if( code == key) {
												$this[AT]('moniterid', tweetStatus.moniterID).text('取消监控');
												return FE;
											}
										});
									}
								});
							}
		     			});  // ajax end 
    	  	}  // moniterBtnAll [LN]>0 结束
    	  
    	
    }  /// function  end 
    
    
    	//预警按钮触发的事件以及预警按钮当前的状态
		   function  yujingBtnHandle(This,  data,recordEye){
		      var alertUrls=[];
			  // 预警按钮
					var lst = $('.cont_list', This);
					lst[EH](function() {
						var $this = $(this),
							   index = lst[ID]($this),
							   dataRow = recordEye[index].postDTO;
						// 原帖预警
						var alertBtn = $this[FD]('.buttons')[FD]('.alertBtn');
						if(alertBtn[LN] > 0) {
							alertBtn[AT]({
	        					url: dataRow.tid,
	        					platform: dataRow.account.twitterType
	        				});
						}
						
						alertUrls.push(dataRow.tid);
						//alertBtn.text('取消预警');
							
						// 转发帖预警
						/*
						var alertBtn2 = $this[FD]('.talkMe_count')[FD]('.alertBtn');
						if(alertBtn2[LN] > 0) {
							alertBtn2[AT]({
	        					url: dataRow.item.tid,
	        					platform: dataRow.item.account.twitterType
	        				});
						}
						alertBtn2.text('取消预警');
						*/
					
					});  //EH结束		
					
					//判断是否真假删除(真删除只存在于未发生预警信息，假删除存在于已经发生预警中)
					$(BY)[AJ]({
						param: { action: 'warnservices.state', urls: alertUrls.join(',')},
						success: function (map) {
							for(var key in map) {
								var state = FE;
								lst[EH](function() {
									var alertBtn = $(this)[FD]('.buttons')[FD]('.alertBtn');
									if(alertBtn[AT]('url') == key) {
										/*
										if(map[key] > 0) {
											alertBtn.text('取消预警')[AT]('alertid', map[key]);
											state = TE;
											return FE;
										}
										*/
										var  shuzu=map[key];
										if(parseInt(shuzu[0])>0 &&parseInt(shuzu[1])==0 ){  
											alertBtn.text('取消预警')[AT]({alertid:shuzu[0],  isDelete:shuzu[1]   });
											//state = TE;
										}else if(parseInt(shuzu[0])>0 &&parseInt(shuzu[1])==1){
										alertBtn.text('预警')[AT]({alertid:shuzu[0],  isDelete:shuzu[1]   });
										} 
									}
									
									/*
									var alertBtn2 = $(this)[FD]('.talkMe_count')[FD]('.alertBtn');
									if(alertBtn2[AT]('url') == key) {
										if(map[key] > 0) {
											alertBtn2.text('取消预警')[AT]('alertid', map[key]);
											state = TE;
											return FE;
										}
									}
									*/
									
								});
								if(state) {
									continue;
								}
							}
						}
					});
					
					
			
					//clickFlag
		   				$('.alertBtn', This)[EV](CK,function() {
		   				if(clickFlag){
						var $this = $(this),
						//当前的索引号  recordEye 为要看到的数据
						curIndex=  $('.alertBtn', This)[ID]($this);
						//预警触发
						if($this[TX]()=='预警'){
						    //clickFlag=FE;
							AlertWindow($this,$this[AT]('alertid'), $this[AT]('isdelete') );
							//clickFlag=TE;
						}
					//取消预警触发
					else{
						clickFlag=FE;
							var param = {};
							param.action = 'warnservices.warnservicesinsert';
							param.yujingTid = $this[AT]('url');
							param.moduleType = 3;
							param.isdelete=$this[AT]('isdelete');
							param.alertid=$this[AT]('alertid');
							$this[AJ]({
								param: param,
								success: function(data) {
									if(data  > 0) {
										$[MB]({ content: '取消预警成功', type: 0,	isAutoClose: TE });
										//取消预警后刷新当前页面
									    OnLoadList(clickWhere);
									} else {
										$[MB]({ content: '取消预警失败', type: 2 ,isAutoClose: TE});
									}
									clickFlag=TE;
								}
							});
						}  //取消预警触发结束
							
							
							} //
					});
					
		   }   //  yujingBtnHandle结束
    
    
            //打开预警窗体
        function AlertWindow(alertBtn, alertID,alertIsDelete) {
        	var template = '',
        		   url = alertBtn[AT]('url');
        	 $[WW]({
						css: { width: '400px', height: 'auto' },
						title: '微博预警',
						content: '<div class="giveMe_do warning_up"><div class="div_style"><label class="task_name">转发达到：</label><input type="text" value="" class="task_count task_Cwdith3 alertRepostNum" maxLength="10" /><span  class="com_fLeft">&nbsp;&nbsp;发送预警</span></div><div class="com_clear"></div><div class="div_style"><label class="task_name">评论达到：</label><input type="text" value="" class="task_count task_Cwdith3 alertCommentNum" maxLength="10" /><span  class="com_fLeft">&nbsp;&nbsp;发送预警</span></div><div class="com_clear"></div><div class="div_style"><label class="task_name">截止时间：</label><input type="text" value="" class="task_count task_Cwdith3 alertTime" readonly="readonly"  /></div><div class="com_clear"></div><div class="div_style warn_style"><label class="task_name">接收人：</label><div class="mynum_list"><h2 class="mynum_click_alert"><span >请选择</span>' +
								'<span class="mynum_type mynum_click "></span></h2><ul class="com_none nameList_alert">' +
								'</ul></div></div>  <div class="div_style"><label class="task_name">发送方式：</label><input type="checkbox"  name="yjSend_way" class="com_fLeft yjSend_wayMar alertCheck" value="0" /><span  class="com_fLeft com_rMar">系统消息</span><input type="checkbox"  name="yjSend_way" class="com_fLeft yjSend_wayMar alertCheck" " value="1"   /><span  class="com_fLeft com_rMar">邮件</span><input type="checkbox"  name="yjSend_way" class="com_fLeft yjSend_wayMar alertCheck" " value="2"  /><span  class="com_fLeft com_rMar">手机短信</span></div><div class="com_clear"></div><div class="push_ok"><div class="in_MsetCno  alertCancel">取&nbsp;&nbsp;消</div><div class="in_MsetCok alertOK">确&nbsp;&nbsp;定</div><span class="alertMBTip" style="color:red;"></span></div><div class="com_clear"></div></div>',
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
									       html = $[EF](liTemplate, data);
								   	ul.html(html);
								   	
								   	var startTime = $.getDate();
								   	alertTime.datePicker({minDate: startTime});
								   	
								   	endTime = $.addDate('d',7, startTime).Format('YYYY-MM-DD');
								   	alertTime.val(endTime);
								   	
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
				    					param.platform = alertBtn[AT]('platform');
				    					param.tid = alertBtn[AT]('url');
				    					param.time = alertTime.val();
				    					//param.alertid = 0;
				    					param.moduleType = 0;
				    				    param.isdelete=alertIsDelete;//$this[AT]('isdelete');
							            param.alertid=alertID;//$this[AT]('alertid');
				    					
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
													alertBtn.text('取消预警')[AT]('alertid', data);	
													OnLoadList(clickWhere);
												} else if(data == -1) {
													$[MB]({ content: '已经存在', type: 2 });
												} else if(data == -2) {
													$[MB]({ content: '微博以删除', type: 2 });
												} else if(data == -3) {
													$[MB]({ content: '预警到达上限,不能添加', type: 2 });
												} else {
													$[MB]({ content: '预警失败', type: 2 });
												}
												close();
											}
										});
				    				});
								}
							});
						}
					});
        }
    
    
    
    //点击监控按钮(包括转发和原创的)
    function   MonitorState  (This,  data,weiboShuzu){
          var moniterBtn=$('.moniterBtnAll',This);
    	   			//启动监控功能  
						moniterBtn[EV](CK, function() {
							var  $this = $(this),
								    text = $[TM]($this.text()),
								    spreadid = $this[AT]('moniterid');
							if(text == '监控') {
								$(BY)[AJ]({
									param: { action: 'spreadAnalysis.insert', methodName: 'save', url: $this[AT]('url')},
									dataType: 'text',
									success: function (data2) {
										// -1：到达添加次数，限制提示，0：插入失败，数据库错误提示，1：添加成功
										if(data2 ==-1) {
											$[MB]({ content: '分析失败,添加以到达上限', type: 2 });
										} else if (data2 == 0) {
											$[MB]({ content: '分析失败,请与管理员联系(插入数据库错误)', type: 2 });
										} else if (data2 == -2) {
											$[MB]({ content: '该条微博已经存在', type: 2 });
										} else if (data2 == -3) {
											$[MB]({ content: '只能分析原微博', type: 2 });
										} else if (data2 > 0) {
											$this.text('取消监控')[AT]('moniterid', data2);
										}
									}
								});
							//取消监控
							} else {
								$(BY)[AJ]({
									param: { action: 'spreadAnalysis.insert', methodName: 'delete', spreadid: spreadid},
									dataType: 'text',
									success: function (data2) {
										if (data2 == 0) {
											$[MB]({ content: '分析失败,请与管理员联系(插入数据库错误)', type: 2 });
										} else if (data2 > 0) {
											$this.text('监控')[AT]('moniterid', 0);
										}
									}
								});
							}
						});
    
    
    }
    
    
    /**
    *   拼写 阴影部分和原创(白色部分)的图片的html
    *  images图片的集合
    */
	function   addImageHTML(images){
			 var  imageTemplate = '<img src="{0}" width="100" height="100" />',
        	          imageHtml = '';
						$[EH](images, function(i, image) {
		    	  		imageHtml += $[FO](imageTemplate, image);
		    	  	});
		 return  imageHtml;
	
	}    
    
    
    /**
	*	 向数据库中，从未读状态更改成为已读状态
	*  curThis 当前的传入的对象
    */
      function    ReduceWarnInfomations  (curThis,This){
      		// com_fRight weibo_newWarning 这个class 样式表示消息栏出现 New字样的
      		    var  newbar=   curThis[PT]()[FD]('.com_fRight.weibo_newWarning');
      			if(newbar[LN]>0){
						curThis.prev()[FD]('.alertInfosAll')[RC]('weibo_newWarning');
						var  warnID=curThis.prev()[AT]('warnserviceslistid');
						$(BY)[AJ]({
						param: {action :'warnservices.warnserviceslist',queryConditions:'readWarnList' ,ids:warnID},
						success: function(data) {
						   //上面的铃铛的个数 减去 当前页面能显示出来的个数
						   //var  numBer=parseInt(data);
						   var  numBer=1;
						   //获取上面的小铃铛的个数
						   var lingdaoNum= parseInt($('#alertInfoCount')[TX]());
						   if(lingdaoNum>=numBer){
						    mediator.DecreaseWarningDecreaseCount(numBer);
						   }else{
						   	 mediator.DecreaseWarningDecreaseCount(lingdaoNum);
						   }
						}//success
					});
      			}
      }   //  ReduceWarnInfomations   end
    
    
    
    
    // 微博信息 or  微博预警 的 点击小眼睛 出来下面的div 的function
    //recordEye 集合是收集到有眼睛的 下面的div里面展现的东西
    function      OpanPanel(This,  data, recordEye){
		$('.weibo_allShow',This)[EV](CK,function (){
				 var cont=	  $(this)[PT]()[PT]()[FD]('.cont_list');
				 var  event=$(this)[PT]()[PT]()[FD]('.event_list');
				 // 事件预警 or 帐号预警
				 if(cont[LN]==0){
				 		if(event[AT]('class').indexOf('com_none')>0){
				 			event[RC]('com_none');
				 			ReduceWarnInfomations(event,This);
				 		}else{
				 			event[AC]('com_none');
				 		}
				 }
				 // 微博预警
				 else {
				    	    //如果是隐藏的话，就展开显示
							if(cont[AT]('class').indexOf('com_none')>0){
							cont[RC]('com_none');
							cont[FD]('.relay.repostView')[AC]('com_none');
							cont[FD]('.relay.commentView')[AC]('com_none');
							ReduceWarnInfomations(cont,This);
							}
							// 如果是展开的话，就隐藏
							else{
								cont[AC]('com_none');
							}
				 }   // else 
			/*
			//给warnnewlist 下的 在添加一个节点
		    var cont=	  $(this)[PT]()[PT]()[FD]('.cont_list');
		    //如果是隐藏的话，就展开显示
			if(cont[AT]('class').indexOf('com_none')>0){
			cont[RC]('com_none');
			cont[FD]('.relay.repostView')[AC]('com_none');
			cont[FD]('.relay.commentView')[AC]('com_none');
			}
			// 如果是展开的话，就隐藏
			else{
				cont[AC]('com_none');
			}
			*/
		});
    }  // OpanPanel结束
    
    
    //预警信息列表(旧)
    function OnLoadmonitorListTemp(){
    			var stimes=starttime.val();
    			var etimes=endtime.val();
    			//var cond=warnservicesDropDwon[AT]('value');
		        warnservicesAll.add(warnservicesPager).html('');
		        warnservicesAll[LS]({//loadlist
	            width: 1000,
	            pageSize: 10,
	            isPager: TE, 
	            id: "warn_ServicesList",
	            pager: warnservicesPager,
	            isOption: FE, //是否显示全选、反选		  
	            param: {action :'warnservices.warnserviceslist',queryConditions:'warnservicesShowList2',stimes:stimes,etimes:etimes},  //,cond:cond 
	        		  templates: [{html: ' <div class="cont_list"  id="warnservices_list_{0}"><div class="taikMe_List"><h2><span class="Fname_color">'},// id
	        		  			   {html: ' {0}&nbsp;&nbsp;</span>'},//序号 从1开始的
	        		 			  {html: '监测到{0}平台<span class="Fname_color">'}, //什么类型平台 新浪,腾讯,人民
	                              {html: '[{0}]</span>账号博主在微博中提到了您设置的关键词</h2>'},  //微博的帐号名
	                              {html: ' <div class="talkMe_count">{0}</div></div>'},  //文章的内容
	                              {html: '<div class="com_clear"></div><div class="cont_do"><div class="cont_doleft"><span>{0}</span>'},  //年月日 2014-03-04   
	                              {html: '<span>{0}</span></div><div  class="cont_doright">' +base.FormatPmi('{pmi:yqzs_collect_1}')   +base.FormatPmi('{pmi:cbfx_monitor_3}')  +'</div></div></div>'}],  //分秒  8:00
//+base.FormatPmi('{pmi:yqzs_collect_1}')  
// +base.FormatPmi('{pmi:cbfx_monitor_3}')
	            columns: [{ field: 'id'},  //  ID 
	            					{ field: 'autoId'},//序号 从1开始的
	                              { field: 'platformStr'},  //什么类型平台 新浪,腾讯,人民
	                              { field: 'weiboAccountName'}, //微博的帐号名
	                              { field: 'text' },  //文章的内容
	                              { field: 'ymd' }, //年月日 2014-03-04
	                              { field: 'hm' }],//分秒  8:00
	                              
	                         
	            onComplete: function (This, refresh, data) {
	            	if(data.realcount== 0){
	            		base.not(This);
	            	}
	            	
	            	//如果出现两个给 最后一个 |去掉
	            	var  collbtns=$('.collectBtn',This);
	            	var  moniterbtns=$('.moniterBtn.moniterBtnAll',This);
	            	var  dorightSpan=$('.cont_doright',This)[FD]('span');
	            	
	            	if(collbtns[LN] >0 &&moniterbtns[LN] >0){
	            		dorightSpan[EH](function (i,item){
	            		   if((i+1)%4==0){
	            		   		item[RM]();
	            		   }
	            		});
	            	}
	            	else if((collbtns[LN] >0 && moniterbtns[LN]==0 )||(  collbtns[LN]==0 &&moniterbtns[LN]>0 )){
	            				dorightSpan[EH](function (i,item){
	            					if((i+1)%2==0){
	            					    item[RM]();
	            					}
	            				});
	            	}
	            	
	            	//给文字加高亮
       				TextAddHighlight(This, refresh, data);
	            	
	            	//  对 导出,搜索按钮事件
				    WarnServicesHandle(This, refresh, data);
	            }  //onComplete结束
	        });   //loadlist结束
    }
    
    
     //页面画出， 里面的一些设置功能
     function  WarnServicesHandle(This, refresh, data){
     		//搜索按钮判断开始时间不能大于结束时间
     		warnservicesSearch[EV](CK,function (){
     	      var tt	= base.IsRegExpDateTime(starttime,endtime,TE);
	    	  if(tt==TE){
					 //OnLoadmonitorList();
	    	  }
     		});  // 判断起始时间不能大于结束时间
     
		// 导出按钮
		/*
		warnservicesImport[EV](CK,function (){
			 alert("预警信息导出");		
		});
     */
     
     //收集按钮的事件
     collectBtnHandle(This ,data);
     
     //监控按钮的事件
     MoniterBtnHandle(This,data);
     
     }
     
     //监控按钮的事件
	 function MoniterBtnHandle(This,data){
	 		monitertions=[];
		   var moniterBtn = $('.moniterBtn.moniterBtnAll',This);
           moniterBtn[EH](function (){
           	 var index= moniterBtn[ID]($(this));
           	 dataRow=data.rows[index];
           	 $(this)[AT]({twitterType:dataRow.platform,url: dataRow.tid });
           	 monitertions.push(dataRow.tid);
           });   //each 结束
           
                    var  moniterstr=monitertions.join();
           			//启动监控功能  
						moniterBtn[EV](CK, function() {
							var  $this = $(this),
								    index = $this[ID](),
								    text = $[TM]($this.text()),
								    spreadid = $this[AT]('moniterid');
							if(text == '监控') {
								$(BY)[AJ]({
									param: { action: 'spreadAnalysis.insert', methodName: 'save', url: $this[AT]('url')},
									dataType: 'text',
									success: function (data) {
										// -1：到达添加次数，限制提示，0：插入失败，数据库错误提示，1：添加成功
										if(data ==-1) {
											$[MB]({ content: '分析失败,添加以到达上限', type: 2 });
										} else if (data == 0) {
											$[MB]({ content: '分析失败,请与管理员联系(插入数据库错误)', type: 2 });
										} else if (data == -2) {
											$[MB]({ content: '该条微博已经存在', type: 2 });
										} else if (data == -3) {
											$[MB]({ content: '只能分析原微博', type: 2 });
										} else if (data > 0) {
											$this.text('取消监控')[AT]('moniterid', data);
										}
									}
								});
							//取消监控
							} else {
								$(BY)[AJ]({
									param: { action: 'spreadAnalysis.insert', methodName: 'delete', spreadid: spreadid},
									dataType: 'text',
									success: function (data) {
										if (data == 0) {
											$[MB]({ content: '分析失败,请与管理员联系(插入数据库错误)', type: 2 });
										} else if (data > 0) {
											$this.text('监控')[AT]('moniterid', 0);
										}
									}
								});
							}
						});
           
    }   // MoniterBtnHandle结束
     
     
     
        //收集按钮的事件
     function  collectBtnHandle(This,  data,weibos){
     		collections=[];
     	    var collectionBtn= $('.collectBtn',This);
     	    collectionBtn[EH](function (i){
     			 $(this)[AT]({url :weibos[i].postDTO.tid});
     			 collections.push(weibos[i].postDTO.tid);
     	});  
	    	var    collectUrls=collections.join();
		    
			if(collectionBtn[LN] > 0) {
		     			//读取收集状态
		     			$(BY)[AJ]({
							param: { action: 'view.getCollectState', urls: collectUrls},
							success: function (map2) {
								//收集按钮操作
								if(collectionBtn[LN] > 0) {
									//设置收集状态
									$[EH](map2, function (key, tweetStatus) {
										if (tweetStatus.collectID != 0) {
											$[EH](weibos, function (index, dataRow) {
												if(dataRow.postDTO.tid== key) {
													collectionBtn.eq(index)[AT]({collectid: tweetStatus.collectID}).text('取消收集');
													return FE;
												}
											});
										}
									});
									
									//启动收集功能
									collectionBtn[EH](function (i) {
					     					   var dataRow = weibos[i].postDTO;
					     					    platform = dataRow.account.twitterType,
					     					   Collect = context.Collect;
					     					//$.alertParam(dataRow.tid, platform, dataRow.url, o.moduleType);
					     				(new Collect($(this),  dataRow.tid, platform, dataRow.url, 7)).open();
									});
								}
							}
		     			});
	     			}
		
     }  //collectBtnHandle结束
     
    
    
    //给文字加高亮
    function  TextAddHighlight(This, refresh, data){
            $('.talkMe_count',This)[EH](function (){
            	   var index= $('.talkMe_count',This)[ID]($(this));
                   var txt=$(this)[TX]();
               	   var  keyWords=data.rows[index].fileds;
               	   var counts=keyWords.split(' ');
            	   for(var i=0;i<counts.length;i++){
            	   	  var kw =counts[i];
            	   	  var  Tkw=txt.split(kw);
					  txt=Tkw.join(' <span class="warn_taskColor">'+kw+'</span>');            	   
            	   } //for  结束
            	   
            	  $(this).html(txt);
            });
    }
    
	//工具栏显示        
        function NavigationInit(){
           	navigationBars.Empty()
			.Add(key, 'WarnServicesToolBar', '预警服务')
    		.Add(key, 'WarnServicesToolBar', '预警信息')[oL]();
        }
          
    return {
    	IsLoad: FE,
        OnLoad: function (_ContentDiv,_type) {
            var t = this;
            contentDiv = _ContentDiv;
            navigationBars=context.NavigationBars;
            $('<div />').load('modules/alertService/alertInfo.html', function () {
            	base = context.Base;
            	cuser=context.CurrentUser;
            	mediator=context.Mediator;
            	contentDiv[AP](base.FormatPmi($(this).html()));
            	t.IsLoad = TE;
                $key = $('#' + key);
                starttime=$('#warnservices_starttime',$key);
          		endtime=$('#warnservices_endtime',$key);
                warnservicesSearch=$('#warnservices_search',$key);
                warnservicesImport=$('#warnservices_import',$key);
                //warnservicesDropDwon=$('#warnservices_selectAccount',$key);
                warnservicesChangeDropDwon=$('#warnservices_selectChangeAccount',$key);
                warnservicesAll=$('#warnserviceAll',$key);
                warnservicesPager=$('#warnservicesPager',$key);
                warnservicesNewsList=$('#warning_newsList',$key);
                if(_type!=UN){
                	clickWhere=_type;
                	NoInit(_type);
                }else{
                clickWhere=UN;
                    Init();
                }
            });
            return t;
        },
        //显示模块方法  type 0 调用铃铛 
        Show:function(type) {
               $key.show();
              if(type!=UN){
                	clickWhere=type;
                }else{
                clickWhere=UN;
                }
				NoInit(type);
        },
       WarnServicesToolBar:function (){
        	NoInit(clickWhere);
        },
        //上面小铃铛调用的方法
        SmallBell:function(){
        	 this.Show(0);
        }
    };
});