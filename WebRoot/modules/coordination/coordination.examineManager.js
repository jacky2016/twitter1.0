//考核管理
define(['portal'], function (context) {
    //定义变量
    var key = 'examineManager',
          $key, //当前的作用域
          navigationBars,
          $changespan,//span的切换条
          weibo_content_count,//微博数量统计
          loadlistPager,//列表分页
          contentStartTime,
          contentEndTime,
          releaseStartTime,
          releaseEndTime,
          dealwithStartTime,
          dealwithEndTime,
          commentatorsStartTime,
          base,
          commentatorsEndTime,
          selectType,//判断是哪个标签下的下拉框
          examineSelect1,//微博内容统计下拉框
          examineSelect2,//微博处理统计下拉框
          //改变页
          ChangePageShow,
          //各个页签的按钮值
          contentSearchBtn,
          //contentImportBtn,
         //协同办公上面的页签切换工具栏
          examineManagerTitleBar,
          FormatDiv, //转发div
          ReviewDiv,//评论div
          commentatorsDiv,//网评员div
          weiboContentLoadlist,//微博内容统计DIV
          weiboReleaseLoadlist,//微博发布统计DIV
          weiboDealwithLoadlist,//微博处理统计DIV
          weiboCommentatorsLoadlist,//网评员统计DIV
          selectColl=[],//微博内容统计、微博处理统计的下拉框的数据
          comtorsToTellMeTemp,//记录网评员列表中总转评论到我的个数  指的是当前的这个网评员的总转评我的个数的
          dealwithTemps,//临时记录
          drowDownTemps,// 下拉框临时值，为了判断 内容统计和处理统计的页签切换，得需要重新读取下拉框的值
          contentDiv,
          ecTotalFormatViewsNum='',// 记录网评员的 总评论转发数 有的话  还是回复到之前选择的值
          isOnClickReplyBut=0,//  考核管理的 微博内容统计的 评论--评论按钮 判断不能多次点击  0 可以点击 1 不可以
          cuser;
    
    //私有方法
    //初始化方法，写自己模块的业务
    function Init() {
    	NavigationInit();
    	selectType=0;
    	ShowOptions(); 
    	//RequestData();
    	//ContentCount();
    }
    
    
    //存在点击4个页签中，具体内部详情，在点击
    //恢复到当前的那个页签下的列表
    function InitTooBar(){
      	 NavigationInit();
      	  $changespan[RC]('zhao_back');
		  $changespan[EH](function(i,item){
	      if(i==selectType){
	        $(this)[AC]('zhao_back');
	      }
	    });
	     ShowLoadList(selectType);
    }
    
    //微博内容统计中开始画下拉框
    function  DrawWeiBoContentCountSelect(){
    	     examineSelect1.input({
	     		id:"weiboContentCount_list",
	     		collection:selectColl
	     		/*
	     		collection:[{text:'新浪账号1',value:0},
	     							  {text:'新浪账号2',value:1},
	     							  {text:'新浪账号3',value:2}]
	     		*/
	     	});
    
    }
    
    //微博处理统计中开始画下拉框
    function DrawWeiBoDealWithCountSelect(){
       			examineSelect2.input({
	     		id:"weiboDealWithCount_list",
	     		collection:selectColl
	     		/*
	     		collection:[{text:'新浪账号1',value:0},
	     							  {text:'新浪账号2',value:1},
	     							  {text:'新浪账号3',value:2}]
	     		*/
	     	});
    }
    
    
     //导航条初始化方法,
	function NavigationInit () {
				    	navigationBars.Empty()
			.Add(key, 'ToolBar', '协同办公')
    		.Add(key, 'ToolBar', '考核管理')[oL]();
	}
	//span 点击事件切换
	function  ChangesSpan(This){
	    $changespan[EH](function(){
	      $changespan[RC]('zhao_back');
	      This[AC]('zhao_back');
	    });
	  var type=  This[AT]('type')
	  ShowLoadList(type);
	}
	
	//机构管理首页展示
	function  ShowOptions(){
		  /*
		   weiboContentLoadlist.show();
		  FormatDiv.hide();
	      ReviewDiv.hide();
		  commentatorsDiv.hide();
		  weiboReleaseLoadlist.hide();
		  weiboDealwithLoadlist.hide();
		  weiboCommentatorsLoadlist.hide();
		  */
		   $changespan[EV](CK,function(){
		    	  ChangesSpan($(this));
		    }).eq(0).trigger(CK);
	}
	//切换span标签， 展示指定的列表
	function ShowLoadList(type){
	     //微博内容统计列表
	     if(type==0){
	      contentEndTime.datePicker();
	      var tempStartTimes=$.addDate('d', -6, contentEndTime.val()).Format('YYYY-MM-DD');
		 contentStartTime.datePicker().val(tempStartTimes);
	     FormatDiv.hide();
	     ReviewDiv.hide();
	     commentatorsDiv.hide();
	     weiboReleaseLoadlist.hide();
	     weiboDealwithLoadlist.hide();
	     weiboCommentatorsLoadlist.hide();
	     weiboContentLoadlist.show();
	     selectType=0;
	     RequestData();
	     // ContentCount();
	     }
	     //微博发布统计列表
	     else if(type==1){
		releaseEndTime.datePicker();
		var tempStartTimes=$.addDate('d', -6, releaseEndTime.val()).Format('YYYY-MM-DD');
	     releaseStartTime.datePicker().val(tempStartTimes);
	     FormatDiv.hide();
	     ReviewDiv.hide();
	     commentatorsDiv.hide();
	     weiboDealwithLoadlist.hide();
	     weiboCommentatorsLoadlist.hide();
	     weiboContentLoadlist.hide();
	      weiboReleaseLoadlist.show();
	      selectType=1;
	     //发布统计功能
	     ReleaseCount();
	     }
	     else if(type==2){
	      dealwithEndTime.datePicker();
	      var tempStartTimes=$.addDate('d', -6, dealwithEndTime.val()).Format('YYYY-MM-DD');
	     dealwithStartTime.datePicker().val(tempStartTimes);
	      FormatDiv.hide();
	     ReviewDiv.hide();
	     commentatorsDiv.hide();
	     weiboCommentatorsLoadlist.hide();
	     weiboContentLoadlist.hide();
	      weiboReleaseLoadlist.hide();
	      weiboDealwithLoadlist.show();
	      selectType=2;
	     //微博处理统计功能
	      RequestData();
	     //DealWithCount();
	     }else {
	     commentatorsEndTime.datePicker();
	     var tempStartTimes=$.addDate('d', -6, commentatorsEndTime.val()).Format('YYYY-MM-DD');
	     commentatorsStartTime.datePicker().val(tempStartTimes);
	     FormatDiv.hide();
	     ReviewDiv.hide();
	     commentatorsDiv.hide();
	     weiboContentLoadlist.hide();
	      weiboReleaseLoadlist.hide();
	      weiboDealwithLoadlist.hide();
	      weiboCommentatorsLoadlist.show();
	      selectType=3;
	     //微博网评员统计功能
	     CommentatorsCount();
	     }
	}
	
	//当前的两个下拉框里的一些异步请求数据 selectColl
	function   RequestData(){
		if(drowDownTemps==UN){
			drowDownTemps=selectType;
			LoadingDrowDown();
		}else{
			 if(drowDownTemps==selectType){
			 	  if(selectType==0){
    		     	ContentCount();
    		     }else if(selectType==2){
    		        DealWithCount();
    		     }
			 }else{
			 drowDownTemps=selectType;
			 LoadingDrowDown();
			 } //处理统计和内容统计页签切换
		} 
    	
	}
	
	//读取下拉框的数据
	function  LoadingDrowDown(){
		selectColl = [];
	    $(BY)[AJ]({
    		param: {action: 'coordination.examineManager',tempList:'dropdownBoxData'},
    		success: function (data) {
    		     $[EH](data ,function (i,item){
						    var a={text:'',value:'',platform:''};
    		     	      a.value =item.uid;
    		     	      a.platform=item.type;
    		     	      a.value=item.uid+','+item.type;
    		     	      /*
    		     	      if(item.name.length>7){
    		     	      a.text=item.name.substr(0,7);
    		     	      }else{
    		     	      a.text=item.name;
    		     	      }
    		     	      */
    		     	      a.text=item.name;
    		     	      selectColl.push(a);
    		     });
    		     
    		     if(selectType==0){
    		     	DrawWeiBoContentCountSelect();
    		     	ContentCount();
    		     }else if(selectType==2){
    		       DrawWeiBoDealWithCountSelect();
    		        DealWithCount();
    		     }
    		}//succ结束
    	});
	}
	
	
	
	//微博内容统计列表
	function  ContentCount(){
			var  listDiv=$('#weibo_content_count',$key);
		 	 listDiv.add(loadlistPager).html('');
		 	 ChangePageShow.html('');
        	ChangePageShow.hide();
			 var  starttime=contentStartTime.val();
			 var  endtime=contentEndTime.val();
			 var  UID=examineSelect1[AT]('value').split(',')[0];
		 	 listDiv[TB]({
             width: 1000,
             trHeight:24, //表格默认高
             pager: loadlistPager,
             pageSize: 15,//显示条数
             isPager: TE, //是否分页
             id:'_contentcount',
             rows: 'rows',
             param: {action :'coordination.examineManager',tempList:'contentcount',starttime:starttime,endtime:endtime
             ,UID:UID
             },
			 columns: [{ caption: '帖子id', field: 'tid', type: 'checkbox',width: '5%' },
			 		   { caption: '序号', field: 'tid', type: 'string',width: '5%' },
                       //{ caption: '内容', field: 'content', type: 'string', width: '45%' },
                        { caption: '内容', field: 'tid', type: 'string', width: '45%' },
                       { caption: '日期', field: 'date', type: 'string', width: '15%' },
                       { caption: '转发', field: 'forwardNum', type: 'int', width: '10%' ,style: 'cursor:pointer'},
                       { caption: '评论', field: 'reviewNum', type: 'int', width: '10%',style: 'cursor:pointer' },
                       { caption: '网评员转发', field: 'organizationNum', type: 'int', width: '20%' ,style: 'cursor:pointer'}  //转发机构
                        //  ,{ caption: 'tid号', field: 'tid', type: 'string',width: '5%' }
                         ],
                       
                //加载表格成功事件
            onComplete: function ( This, refresh,data) {
			  //微博内容统计数据中table中td列的点击事件
			 //+++++++++++++++++
			 if(data.rows.length==0){
			         base.not(This);
			 }
			 
			// var   contentExportTemp=cuser.Templates['xtbg_khglContentImport_1'];
			 	var   contentExportTemp=cuser.Templates['xtbg_khglImport_1'];
			 if(contentExportTemp==UN){
				$('.selectAll',This)[EH](function (){
				//$(this)[PT]()[RM]();
				$(this)[PT]().hide();
				});
				$('.select',This)[EH](function (){
				//$(this)[PT]()[RM]();
				$(this)[PT]().hide();
				});
			 }else{
			 		var importHtml=$[FO](contentExportTemp,"content_count_import");
			 		if($('#content_count_searchBtn',$key)[PT]()[FD]('#content_count_import')[LN]==0){
			 		$('#content_count_searchBtn',$key)[PT]()[AP](importHtml);
			 		}
			 }
	
				   ContentSetOptions( This, refresh,data);			 
                }
		});
	}
	
	     //发布统计功能
	     function  ReleaseCount(){
	     	var  listDiv=$('#weibo_release_count',$key);
	        var  starttime=releaseStartTime.val();
			var  endtime=releaseEndTime.val();
			ChangePageShow.html('');
        	ChangePageShow.hide();
		 	 listDiv.add(loadlistPager).html('');
		 	 listDiv[TB]({
             width: 1000,
             trHeight: 24, //表格默认高
             pager: loadlistPager,
             pageSize: 15,//显示条数
             isPager: TE, //是否分页
             id:'_releasecount',
             rows: 'rows',
             param: {action :'coordination.examineManager',tempList:'releasecount'
             ,starttime:starttime,endtime:endtime},
			 columns: [{ caption: '序号', field: 'id', type: 'checkbox',width: '5%' },
                       { caption: '人员', field: 'submitName', type: 'string', width: '30%' },
                       { caption: '提交', field: 'submitNum', type: 'int', width: '10%' },//,style: 'cursor:pointer'
                       { caption: '采纳', field: 'adoptNum', type: 'int', width: '10%'}], // ,style: 'cursor:pointer'
                       
                //加载表格成功事件
            onComplete: function ( This, refresh,data) {
            
            	 if(data.rows.length==0){
            	 	 	base.not(listDiv);
            	 }
            	 
              //var   releaseExportTemp=cuser.Templates['xtbg_khglReleaseImport_1'];
			 var   releaseExportTemp=cuser.Templates['xtbg_khglImport_1'];
			 if(releaseExportTemp==UN){
				$('.selectAll',This)[EH](function (){
				//$(this)[PT]()[RM]();
				$(this)[PT]().hide();
				});
				$('.select',This)[EH](function (){
				$(this)[PT]().hide();
				//$(this)[PT]()[RM]();
				});
			 }else{
			 		var importHtml=$[FO](releaseExportTemp,"release_count_importBtn");
			 		if($('#release_count_searchBtn',$key)[PT]()[FD]('#release_count_importBtn')[LN]==0){
			 		   $('#release_count_searchBtn',$key)[PT]()[AP](importHtml);
			 		}
			 }
            	 
            	 ReleaseSetOptions( This, refresh,data);
                }
		});
	     }
	     
	     //微博处理统计功能
	      function	DealWithCount(){
	         var  listDiv=$('#weibo_dealwith_count',$key);
		 	 listDiv.add(loadlistPager).html('');
		 	 ChangePageShow.html('');
        	 ChangePageShow.hide();
		 	 var  starttime=dealwithStartTime.val();
		 	 var  endtime=dealwithEndTime.val();
		 	 var   uid=examineSelect2[AT]("value").split(',')[0];
		 	 listDiv[TB]({
             width: 1000,
             trHeight: 24, //表格默认高
             pager: loadlistPager,
             pageSize: 15,//显示条数
             isPager: TE, //是否分页
             id:'_dealwithcount',
             rows: 'rows',
             param: {action :'coordination.examineManager',tempList:'dealwithcount',starttime:starttime,endtime:endtime,UID:uid},
			 columns: [{ caption: '序号', field: 'id', type: 'checkbox',width: '5%' },
                       { caption: '人员', field: 'dealName', type: 'string', width: '30%' },
                       { caption: '评论', field: 'comments', type: 'int', width: '15%'},// ,style: 'cursor:pointer'
                       { caption: '转发', field: 'reposts', type: 'int', width: '15%'}],// ,style: 'cursor:pointer'
                //加载表格成功事件
            onComplete: function ( This, refresh,data) {
            			if(data.rows.length==0){
            			     base.not(listDiv);
            			}
            			
		             //var   dealwithExportTemp=cuser.Templates['xtbg_khglDealWithImport_1'];
		             var   dealwithExportTemp=cuser.Templates['xtbg_khglImport_1'];
					 if(dealwithExportTemp==UN){
						$('.selectAll',This)[EH](function (){
						$(this)[PT]().hide();
						//$(this)[PT]()[RM]();
						});
						$('.select',This)[EH](function (){
						$(this)[PT]().hide();
						//$(this)[PT]()[RM]();
						});
					 }else{
					 	var importHtml=$[FO](dealwithExportTemp,"dealwith_count_importBtn");
					 	if( $('#dealwith_count_searchBtn',$key)[PT]()[FD]('#dealwith_count_importBtn')[LN]==0){
					 	    $('#dealwith_count_searchBtn',$key)[PT]()[AP](importHtml);
					 	}
					 }
            				//微博处理统计各个模块的功能
			               DealWithSetOptions( This, refresh,data);
                }
		});
	      }
	      
	       //微博网评员统计功能
	        function   CommentatorsCount(){
	         var  listDiv=$('#weibo_commentators_count',$key);
	         var  starttime=commentatorsStartTime.val();
	         var  endtime=commentatorsEndTime.val();
	         ChangePageShow.html('');
        	ChangePageShow.hide();
		 	 listDiv.add(loadlistPager).html('');
		 	 listDiv[TB]({
             width: 1000,
             trHeight: 24, //表格默认高
             pager: loadlistPager,
             pageSize: 15,//显示条数
             isPager: TE, //是否分页
             id:'_commentatorscount',
             rows: 'rows',
             param: {action :'coordination.examineManager',tempList:'commentatorscount'
             ,starttime:starttime,endtime:endtime},
			 columns: [{ caption: '序号', field: 'id', type: 'checkbox',width: '5%' },
                       { caption: '人员', field: 'commentatorsName', type: 'string', width: '40%' },
                       { caption: '网评员账号个数', field: 'commentatorsAccountNum', type: 'int', width: '15%'},// ,style: 'cursor:pointer'
                       { caption: '总转评到我的个数', field: 'totalRepostNum', type: 'int', width: '10%'}],
                       
                //加载表格成功事件
            onComplete: function ( This, refresh,data) {
            	   if(data.rows.length==0){
            	               base.not(listDiv);
            	    }else{
            	    	 $('.t2',This)[EH](function (){
            	    	 	  var t2TXT=$(this)[TX]();
		    		  	      var last='<a style="cursor:pointer"> '+t2TXT+'</a>';
		  	                  $(this).html(last);
            	    	 });
            	    	 
            	    	   	 $('.t3',This)[EH](function (){
            	    	 	  var t3TXT=$(this)[TX]();
		    		  	      var last='<a style="cursor:pointer"> '+t3TXT+'</a>';
		  	                  $(this).html(last);
            	    	 });
            	    }
            	    
		             //var   commentatorsExportTemp=cuser.Templates['xtbg_khglCommentatorsImport_1'];
		             var   commentatorsExportTemp=cuser.Templates['xtbg_khglImport_1'];
					 if(commentatorsExportTemp==UN){
						$('.selectAll',This)[EH](function (){
						$(this)[PT]().hide();
						//$(this)[PT]()[RM]();
						});
						$('.select',This)[EH](function (){
						$(this)[PT]().hide();
						//$(this)[PT]()[RM]();
						});
					 } else{
					 var importHtml=$[FO](commentatorsExportTemp,"commentators_count_importBtn");
					 if($('#commentators_count_searchBtn',$key)[PT]()[FD]('#commentators_count_importBtn')[LN]==0){
					 		$('#commentators_count_searchBtn',$key)[PT]()[AP](importHtml);
					   }
					 }
            	    
            		//微博网评员统计各个模块的功能
			      CommentatorsSetOptions( This, refresh,data);
                }
		});
	        }
	      
	      
	      //微博网评员统计中-----	 全部网评员超链接按钮    展示
	      function  CommentatorsAccountNumberList(){
	       	 ChangePageShow.add(loadlistPager).html('');
	       	 ChangePageShow[TB]({
             width: 1000,
             trHeight: 24, //表格默认高
             pager: loadlistPager,
             pageSize: 15,//显示条数
             isPager: TE, //是否分页
             id:'_commentatorsAccountNum',   		    
             rows: 'rows',
             param: {action :'coordination.examineManager',tempList:'commentatorsAlertTable' },//   ,nid:nid 
			 columns: [{ caption: '序号', field: '', type: 'number',width: '3%' },
                     //    { caption: 'ID号', field: 'id', type: 'int',width: '5%' ,style: ''}
			 		             { caption: '网评员', field: 'comtorsEveryOneName', type: 'string',width: '15%' },
			                     { caption: '媒体', field: 'comtorsAccPlatform', type: 'string', width: '10%' },
			                     { caption: '帐号昵称', field: 'comtorsAccName', type: 'string', width: '20%'},
			                     { caption: 'UID号', field: 'comtorsAllUID', type: 'string',width: '10%' ,style: 'display:none'}],
                       
                //加载表格成功事件
            onComplete: function ( This, refresh,data) {
            	   if(data.rows.length==0){
            	               base.not(ChangePageShow);
            	    }
            	    //全部网评员按钮------  删除按钮设置
            	    AllCommentatorsDelete(This,data);
            	    
                }//onComplete结束
		});
	       	 
	 }//结束
	      
	      
	      //全部网评员按钮------  删除按钮设置
	      function    AllCommentatorsDelete(This,data){
	      		This[FD]('tr').eq(1)[FD]('th').eq(This[FD]('tr').eq(1)[FD]('th')[LN]-1).hide();
	      		 if(cuser.Templates['xtbg_khglCommentatorsDel_1']!=UN){
						This[FD]('tr').eq(1)[AP]('<th width="5%">操作</th>');
						This[FD]('tr')[EH](function (i,item){
						    if(i>1){
						        var index=i-2;
						        var htm=cuser.Templates['xtbg_khglCommentatorsDel_1'];
						        htm=htm[RP]('{4}','{0}');
						        htm=$[FO](htm,data.rows[index].id+'_'+data.rows[index].comtorsAllUID);
						        $(this)[AP](htm);
						       //点击删除按钮开始
								$('#comtorsId_'+data.rows[index].id+'_'+data.rows[index].comtorsAllUID,This)[EV](CK,function(){
							var 	html='<div class="giveMe_do del_reSee"><div class="giveMe_doDel" >确定要删除吗？删除可能同步删除网评员!</div> <div class="push_ok"><div class="in_MsetCno" id="Exmangecancel">取&nbsp;&nbsp;消</div><div class="in_MsetCok" id="Exmangeok">确&nbsp;&nbsp;定</div></div><div class="com_clear"></div></div>';
							$[WW]({
					        css: { "width": "440px", "height": "auto"},
					        //event: 'no',
					       // title: '序号:'+data.rows[index].id+'     推送任务',
					        title: '&nbsp;&nbsp;网评员',
					        content: html,
					        id: '_exmangeCommtors'+data.rows[index].id,
					        onLoad: function(div) {
					     				CommtorsSetoptions(div,This,data.rows[index].id,data.rows[index].comtorsAllUID); 
					      		} //onLoad  
			    					});  //WW结束									
								});
								//点击删除按钮结束			
						        
						    } //if
						});
							
				} //有删除权限结束
	      }  // AllCommentatorsDelete结束


			//全部网评员里的删除点击 取消 or 确定
			function CommtorsSetoptions(div,This,id,uid){
					$('#Exmangecancel',div)[EV](CK,function (){
						CloseWindow(div);
					});
					
					$('#Exmangeok',div)[EV](CK,function (){
							CloseWindow(div);
							//var uid=$[TM]($('#comtorsId_'+id+'_'+uid,This)[PT]().prev()[TX]());
							$('#comtorsId_'+id+'_'+uid,This)[PT]()[PT]()[RM]();
							$(BY)[AJ]({
							param: {action: 'examineManager.weibodcommentatorscountdel',  tempList:'deleteComtorsID'  ,nid:id,comtorsUID:uid},
							success: function(data) {
								CommentatorsAccountNumberList();
							}//success
						});
					}); // ok 
			}
	      
	      //微博网评员统计中-----总转评到我的个数的table列表(里面的搜索 toolbar)的下拉框
	      function ExmanageCommtorsDrowDown(select,defualt){
	          	select.input({
	     		id:"ec_ecstyle",
	     		/*
	         		collection:[{text:'转发',value:0},
	     							  {text:'评论',value:1}],
	     							  */
	     			collection:defualt,
	     			//selected: defualt,//,{ text: '', value: '' }, //默认选择值
	     		   onChange:function(value){
	     		   ecTotalFormatViewsNum=value;
	     		   }// onChange 结束
	     	});
	      
	      }
	      
	      
	       //微博网评员统计中-----总转评到我的个数的table列表
	      function  CommentatorsRepostToMeNumberList   (nid){
	      	 ChangePageShow.html('');  
    		 ChangePageShow.show();
	      	 var  stime=commentatorsStartTime.val();
	      	 var  etime=commentatorsEndTime.val();
	      
	        ChangePageShow[AP]('<div  id="commtorsCommentAndRepostCount_toolBar"></div><div  id="commtorsCommentAndRepostCount_list"></div>');
	      	var  ChangePageShow2=$('#commtorsCommentAndRepostCount_list',ChangePageShow),
	      		    ChangePageShow3=$('#commtorsCommentAndRepostCount_toolBar',ChangePageShow);
	      			ChangePageShow3[AP]('<div class="send_selWay"><div class="news_type"><span class="sel_title" >类型</span><div class="exmanageComtorrs_DropDwon" id="exmanageComtorrs_selectAccount"></div><span class="ana_selBotton doBotton_mar"  id="exmanageComtorrs_search"></span></div></div>');
	      	  	     var  t=[{text:'转发',value:0},{text:'评论',value:1}];;//{text:'',value:''};
	      	  	        //alert(ecTotalFormatViewsNum);
						 if(ecTotalFormatViewsNum!=''){ //0 转发 1 评论
	      	  	    		if(parseInt(ecTotalFormatViewsNum)==0){
	      	  	    				t=[{text:'转发',value:0},{text:'评论',value:1}];
	      	  	    			}else{
	      	  	    				t=[{text:'评论',value:1},{text:'转发',value:0}];
	      	  	    		}
	      	  	    }
				    ExmanageCommtorsDrowDown($('#exmanageComtorrs_selectAccount',ChangePageShow3),t);	      	  	 
	      	  	    var  selectValueEC=$('#exmanageComtorrs_selectAccount',ChangePageShow3)[AT]('value');
	      	  	    $('#exmanageComtorrs_search',ChangePageShow3)[EV](CK,function(){
								 CommentatorsRepostToMeNumberList(nid);
					});
	      	  	ChangePageShow2.add(loadlistPager).html('');
	      	  	var  templatePmis='';
	      	  	//转发
	      	  	if(parseInt(selectValueEC)==0){
	      	  	templatePmis=base.FormatPmi('{pmi:yqzs_collect_1}{pmi:wdsy_repost_1}{pmi:wdsy_comment_1}');
	      	  	}
	      	  	//评论
	      	  	else{
				templatePmis=base.FormatPmi('{pmi:wdsy_comment_1}');
	      	  	}
	      	    base.DataBind({ 
				panel: ChangePageShow2, 
				pager: loadlistPager,
				id: 'total_CommtorsTotellMe_count', 
			    param: {action :'coordination.examineManager',tempList:'commentatorsToMeNum',nid:nid,starttime:stime,endtime:etime,selectValueEC:selectValueEC},
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
				onSend:function(btn,txt, isChecked, accountCollection, postID, url, type, twitterID, closeBtn,name){
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
	      	  
	      }//结束
	      
	      
	      
	     //微博网评员统计各个模块的功能
		function   CommentatorsSetOptions( This, refresh,data){
		//全部网评员展示
		$('#commentators_count_allshow',$key)[EV](CK,function(){
			 examineManagerTitleBar.hide();
		     weiboCommentatorsLoadlist.hide();
		     navigationBars.Empty()
			.Add(key, 'ToolBar', '协同办公')
    		.Add(key, 'ToolBar', '考核管理')
    		.Add(key, 'ToolBarWeiBoCommtors', '网评员详情')[oL]();		
		    ChangePageShow.html('');  
    		ChangePageShow.show();
		    CommentatorsAccountNumberList();
		});
		
		
		
		//微博网评员统计中-----网评员账号个数(显示的是此帐号下弹出层修改)事件  t2 class
		  $('.t2',This)[FD]('a')[EV](CK,function (){
		  /*
			 examineManagerTitleBar.hide();
		     weiboCommentatorsLoadlist.hide();
		     navigationBars.Empty()
			.Add(key, 'ToolBar', '协同办公')
    		.Add(key, 'ToolBar', '考核管理')
    		.Add(key, 'ToolBarWeiBo', '微博详情')[oL]();		
    		ChangePageShow.html();  
    		ChangePageShow.show();
    		var nid=$[TM]($(this)[PT]()[PT]()[FD]('td').eq(0)[FD]('input').val());
			*/	
			  var   comtorName=$[TM]($(this)[PT]().prev()[TX]());
			  EveryModityComtators(comtorName);
		  });		
		
		
		//微博网评员统计中-----总转评到我的个数   t3 class
		$('.t3',This)[FD]('a')[EV](CK,function (){
			examineManagerTitleBar.hide();
		     weiboCommentatorsLoadlist.hide();
		     navigationBars.Empty()
			.Add(key, 'ToolBar', '协同办公')
    		.Add(key, 'ToolBar', '考核管理')
    		.Add(key, 'ComtorsToTellMe', '微博详情')[oL]();		
			var nid=$[TM]($(this)[PT]()[PT]()[FD]('td').eq(0)[FD]('input').val());
			comtorsToTellMeTemp=nid;
			CommentatorsRepostToMeNumberList(nid);
		});
		
		
	    	//判断如果单个全部选中的各种情况
          		   CheckBoxOptions( This, refresh,data);
		   //判断如果单个全部选中的各种情况结束
		
		//网评员统计搜索按钮事件
		$('#commentators_count_searchBtn',$key)[EV](CK,function(){
		 //查询网评员微博统计
				//判断时间起始不能大于结束时间
	    	  var tt	= base.IsRegExpDateTime(commentatorsStartTime,commentatorsEndTime,TE);
	    	  if(tt==TE){
				CommentatorsCount();
	    	  }
		});
		
		//网评员添加按钮事件
		$('#commentators_count_addBtn',$key)[EV](CK,function(){
		     AddCommentatorsCountHandle();		
		});
		
		//网评员微博统计导出按钮事件
		$('#commentators_count_importBtn',$key)[EV](CK,function(){
		   alert("网评员统计导出");
		   //++++++++++++++
		});
			      
			}
			
			
		  //微博网评员统计中-----网评员账号个数(显示的是此帐号下弹出层修改)事件  t2 class
	      function  EveryModityComtators(CName){
	         var bodyHTML='<div class="giveMe_do coor_add"><div class="box_count" ><div class="div_style"><label class="com_fLeft">网评人员：</label>'+
	      	   '<input type="text"  class="task_Cwdith2 task_count"  id="commentators_name"  readonly="true"  style="background:#eee"  /> </div></div>'+
	      	  '<div id="examineManager_comtorsName" style="margin-left: 100px; float: left; color: red; font-size: 12px; display: none;">'+
	      	  '<span class="ev_wCount">* 不能输入特殊字符或不填</span></div><div class="com_clear"></div>'+
	      	  '<div class="div_style"><label  class="com_fLeft">微博类型：</label>'+
	      	   '<select name="T_place" class="task_count task_Cwdith2"  id="commentators_weibostyes"><option value="1">新浪</option><!--<option value="2">腾讯</option><option value="5">人民</option>--></select> </div>'
	      	   +'<div class="div_style"><label  class="com_fLeft">微博账号：</label><input   id="commentators_weiboaccounts"  type="text"  class="task_Cwdith1 task_count" />'+
	      	   '<div class="tWord_add"  isFlag="0">添加</div></div>'+  //isFlag  1 不可以在点击 0 可以在次点击
	      	   '<div id="examineManager_comtorsAccName" style="margin-left: 100px; float: left; color: red; font-size: 12px; display: none;">'
	      	   +'<span class="ev_wCount">* 此微博帐号存在特殊字符或者空白符</span></div><div class="com_clear"></div>'+
	      	   '<div id="examineManager_comtorsCheck" style="margin-left: 100px; float: left; color: red; font-size: 12px; display: none;">'+
	      	   '<span class="ev_wCount">* 此微博帐号已经存在，无须添加</span></div><div class="com_clear"></div>'+
	      	   
	      	   '<div id="examineManager_comtorsSave" style="margin-left: 100px; float: left; color: red; font-size: 12px; display: none;">'+
	      	   '<span class="ev_wCount">* 此微博帐号不存在，请重新添加</span></div><div class="com_clear"></div>'+
	      	   
	      	   '<div class="event coor_tableBox"><table  id="commentators_table"  class="coor_table web_table">'+
			   '<th width="10%">序号</th><th width="25%">姓名</th> <th width="15%">媒体</th><th  width="40%">账号</th>'+
	      	   '{0}</table></div><div class="com_clear"></div><div class="push_ok">'+
	      	   '<!--<div class="in_MsetCno">取&nbsp;&nbsp;消</div> --><div class="in_MsetCok">关&nbsp;&nbsp;闭</div></div><div class="com_clear"></div></div>';
				
		      var tempHTML=' <tr> <td class="t0 com_none" >{0}</td> <td  class="t1">{1}</td> <td class="t2">{2}</td><td class="t3">{3}</td><td class="tdOption event_do" style="">&nbsp;<div title="删除" class="event_doBotton event_del" id="comtorsId_{4}"></div></td> </tr> ';
    	  	  var  tempHTMLOne=' <tr> <td class="t0 com_none">{0}</td> <td  class="t1">{1}</td> <td class="t2">{2}</td><td class="t3">{3}</td> </tr> ';
    	  	   if(cuser.Templates['xtbg_khglCommentatorsDel_1']==UN){
					tempHTML=tempHTMLOne;
    	  	   }
    	  
    	    //然后给	tempHTML模版赋值
	    	var  totalHtml='';
	    	var dataTemp;
	      		$(BY)[AJ]({
						param: {action: 'coordination.examineManager',  tempList:'everyModityCommtors' ,name:CName},
						success: function(data) {
								dataTemp=data;
						   	   $[EH](data,function (i,item){
						   	   	    if(cuser.Templates['xtbg_khglCommentatorsDel_1']==UN){
					  				totalHtml+=$[FO](tempHTML,item.comtorsUID,item.personName,item.platType   , item.accName);
    	  	   						}else{
    	  	   						  totalHtml+=$[FO](tempHTML,item.comtorsUID,item.personName,item.platType   , item.accName,item.id+'_'+item.comtorsUID);
    	  	   						}
						   	   });
						   	   
						   	   // 有删除权限的情况下
						   	   if(cuser.Templates['xtbg_khglCommentatorsDel_1']!=UN){
						   	   	   	   bodyHTML=$[FO](bodyHTML,'<th width="15%">操作</th>'+totalHtml);
						   	   }
						   	   //  无删除权限
						   	   else{
						   	      	   bodyHTML=$[FO](bodyHTML,totalHtml);
						   	   }
						   	   
						   	 	    $[WW]({
							      	  css: { "width": "470px", "height": "auto"},
							        //event: 'no',
							        title: '添加网评员帐号',
							        content: bodyHTML,
							        id: '_examinemanager_modity_Commentators',
							        onLoad: function(div) {
							        	 var comtorsaddTemp=cuser.Templates['xtbg_khglCommentatorsAdd_1'];
							        	 if(comtorsaddTemp==UN){
							        	      $('.tWord_add',div)[RM]();
							        	      $('#commentators_weiboaccounts',div).css("width","296px");
							        	 }
							        	
							            //修改中要给网评员名字赋值
							            $('#commentators_name',div).val(CName);
							            
									 //写自己的添加弹出层业务
									    $('#examineManager_comtorsName',div).hide();
									 	$('#examineManager_comtorsAccName',div).hide();
									 	$('#examineManager_comtorsCheck',div).hide();
									 	$('#examineManager_comtorsSave',div).hide();
									 	
									     //当前网评员的下面可显示的所有管理的微博帐号
									 	
									 	if(dataTemp.length==0){
									 		 $('.event.coor_tableBox',div).hide();
									 	}else{
									 		 	$('.event.coor_tableBox',div).show();
									 	}
									 	
									 	//删除网评员弹出层的序号
									 	$('#commentators_table',div)[FD]('tr')[EH](function (){
									 		 if( $[TM]($(this)[FD]('th').eq(0)[TX]())=='序号'){
									 			  $(this)[FD]('th').eq(0).hide();//[RM]();
									 			  $('.t0',	$('#commentators_table',div)).hide(); //[RM]();
									 		}
									 	    /*
										      if(i==0){
										       }
										       else{
												$(this)[FD]('.t0')[RM]();					       
										       }
										       */
										       
									 	});
									 	
									 	
									 	//网评员弹出层中的删除按钮事件
											$('.event_doBotton.event_del',div)[EH](function (i,item){
												    var  cid=$(this)[AT]('id');
													$('#'+cid,div)[EV](CK,function (){
																$(BY)[AJ]({
															                   			param: {action: 'examineManager.weibodcommentatorscountdel',tempList:'deleteComtorsID',nid: cid.split('_')[1]   //data.rows[i].id
															                   			,comtorsUID:  $[TM]($(this)[PT]()[PT]()[FD]('td').eq(0)[TX]())  },
															                   				success: function(data){
																							        // alert(data);
																								    $('#'+cid,div)[PT]()[PT]()[RM]();
															                   				}//success结束
															                   		});//ajax结束
														});
												});
									 	
										//修改---网评员弹出层当中的添加按钮功能以及确定，取消按钮功能
										CommentatorsModityAccount(div);
							      		},
							      		onClose:function(){
							      			//网评员添加弹出层页面中右上角的X号触发事件
							      			CommentatorsCount();
							      		}
					    			});   //WW结束
						   	   
						}//success
					});
	      }
	      
  
	      //修改---网评员弹出层当中的添加按钮功能以及确定，取消按钮功能
	      function		CommentatorsModityAccount(div){
			
				$('#commentators_weiboaccounts',div)[EV](KU,function(){
				 	$('#examineManager_comtorsAccName',div).hide();
				 	$('#examineManager_comtorsSave',div).hide();
				 	$('#examineManager_comtorsCheck',div).hide();				
				});
			
			        $('.tWord_add',div)[EV](CK,function(){
			          if(parseInt($(this)[AT]('isFlag'))==0){
			             	$('.tWord_add',div)[AT]({'isFlag':1});
			             //人员名字
			              var  ComtorsName=$[TM]($('#commentators_name',div).val());
			              //获取uid 值，后期会做下拉框里取值的
			              //+++++++++++++++++
			              var  uid='';
			              //获取微博帐号的名称
			              var  accName=$[TM]($('#commentators_weiboaccounts',div).val());
			              //获取那种类型平台
			        	  var  platform=  $('#commentators_weibostyes',div).val();

			             //添加事件中判断是否微博帐号存在或者本身输入的就是空白符或者特殊字符
						//空白符或者特殊字符，不是汉字的
						if(accName==""){
						   $('#examineManager_comtorsAccName',div).show();
						   $('.tWord_add',div)[AT]({'isFlag':0});
			                    return FE;
						}else{
						     var regu = /^[0-9a-zA-Z_\-\u4e00-\u9fa5]+$/;
			             	if (!regu.test( accName )) {   
							    $('#examineManager_comtorsAccName',div).show();
							    $('.tWord_add',div)[AT]({'isFlag':0});
								return  FE;    						
    						}	
						}
						
					
						//添加事件中判断是否微博帐号存在(不存在的情况)
			        		$(BY)[AJ]({
									param: {action: 'examineManager.weibodcommentatorscountadd', tempList:'modityCheckCommentators' 
									,name:ComtorsName, uid:uid, platform:platform,accName:accName},
									success: function(data){
										$('.event.coor_tableBox',div).show();
										$('#commentators_table',div).show();
										var dataOne=data;
									
									//不管存在不存在 都把列表显示出来			
									$(BY)[AJ]({
										param: {action: 'coordination.examineManager',  tempList:'CommentatorsAddList' ,name:ComtorsName },
										success: function(data) {
											 	//把添加网评员弹出层中的列表，再次更改名字或者是再次
											 	//添加此网评员名字，而刷新下面的列表
											 	$('.event.coor_tableBox',div).show();
											 
											 	$('#commentators_table',div)[FD]('tr')[EH](function (i,item){
											 		 if(i!=0){
														//item[RM]();
														$(this)[RM]();			 		 
											 		 }
											 	});
											
											  var HTMLTemp='<tr><td  class="t0 com_none">{0}</td> <td class="t1">{1}</td> <td  class="t2">{2}</td> <td class="t3">{3}</td>   <td class="tdOption event_do" style="">&nbsp;<div title="删除" class="event_doBotton event_del" id="comtorsId_{4}"></div></td></tr> ';
											  var HTMLTempOne=' <tr> <td class="t0 com_none">{0}</td> <td  class="t1">{1}</td> <td class="t2">{2}</td><td class="t3">{3}</td> </tr> ';		
											   if(cuser.Templates['xtbg_khglCommentatorsDel_1']==UN){
													HTMLTemp=HTMLTempOne;
								    	  	   }
											
										      var  totalHtml='';
										      $[EH](data,function (i,item){
										      if(cuser.Templates['xtbg_khglCommentatorsDel_1']==UN){
										        totalHtml+=$[FO](HTMLTemp,item.comtorsUID,item.personName,item.platType,item.accName);
										      }else{
												totalHtml+=$[FO](HTMLTemp,item.comtorsUID,item.personName,item.platType,item.accName,item.id+'_'+item.comtorsUID);										      
										      }
 	
										      }); //each结束
											$('#commentators_table',div)[AP](totalHtml);
											
											//删除网评员弹出层的序号
									 	$('#commentators_table',div)[FD]('tr')[EH](function (i){
										       if( $[TM]($(this)[FD]('th').eq(0)[TX]())=='序号'){
									 			  $(this)[FD]('th').eq(0).hide();//[RM]();
									 			  $('.t0',	$('#commentators_table',div)).hide();//[RM]();
									 		}
										      /*
										      if(i==0){
										    	$(this)[FD]('th').eq(0)[RM]();
										       }
										       else{
												$(this)[FD]('.t0')[RM]();					       
										       }
										       */
									 	});
											
								       if(dataOne!="-1"&&dataOne!="-2"){
										    $('#examineManager_comtorsCheck',div).hide();
										    $('#examineManager_comtorsSave',div).hide();
										    
												//没有重复，然后添加成功，然后在显示出在列表当中
										  var HTML='<tr><td  class="t0 com_none">{0}</td> <td class="t1">{1}</td> <td  class="t2">{2}</td> <td class="t3">{3}</td>   <td class="tdOption event_do" style="">&nbsp;<div title="删除" class="event_doBotton event_del" id="comtorsId_{4}"></div></td></tr> ';
			      	                     var HTMLOne=' <tr> <td class="t0 com_none">{0}</td> <td  class="t1">{1}</td> <td class="t2">{2}</td><td class="t3">{3}</td> </tr> ';	
			      	                     if(cuser.Templates['xtbg_khglCommentatorsDel_1']==UN){
			      	                         HTML=HTMLOne;
			      	                     }
			      	                    
			      	                    //  var value=ParseEnum(parseInt($('#commentators_weibostyes',div).val()));
										
										 //commentators_table
								          //HTML=$[FO](HTML,dataOne,$('#commentators_name',div).val(),value,$('#commentators_weiboaccounts',div).val(),dataOne);
					
								         //	$('#commentators_table',div)[FD]('tr:last').after($(HTML));
								         		 	$('.event.coor_tableBox',div).show();
												//	$('#commentators_table',div)[AP](HTML);
				 	
								         	//添加成功，然后上面的 ： 人名和微博名要重置
								         	//$('#commentators_name',div).val('');
								         	$('#commentators_weiboaccounts',div).val('');
								         	$('.tWord_add',div)[AT]({'isFlag':0});
										}//添加网评员成功结束
											else{
											if(dataOne=="-1"){
											//说明重复了，请重新添加
											$('#examineManager_comtorsCheck',div).show();
											 }else if(dataOne=="-2"){
											 	$('#examineManager_comtorsSave',div).show();
											 }
											 $('.tWord_add',div)[AT]({'isFlag':0});
											}
										//-----------											
											    	//网评员弹出层中的删除按钮事件
											$('.event_doBotton.event_del',div)[EH](function (i,item){
												    var  cid=$(this)[AT]('id');
													$('#'+cid,div)[EV](CK,function (){
																$(BY)[AJ]({
															                   			param: {action: 'examineManager.weibodcommentatorscountdel',tempList:'modityDeleteComtorsID',nid:    cid.split('_')[1] //data.rows[i].id  // cid.substr(10)
															                   			,comtorsUID:  $[TM]($(this)[PT]()[PT]()[FD]('td').eq(0)[TX]())},
															                   				success: function(data){
																							        // alert(data);
																								    $('#'+cid,div)[PT]()[PT]()[RM]();
															                   				}//success结束
															                   		});//ajax结束
														});
												});
											
											//-----------		
													
										}  //  输入当前列表的展示  success
									});
									}// checks  success
							});
							
							} // isFlag 判断结束
			        });//添加按钮事件结束
			        
			        //确定按钮
			        $('.in_MsetCok',div)[EV](CK,function(){
			        	CloseWindow(div);
			        	CommentatorsCount();
			        });
			        //取消按钮
			            $('.in_MsetCno',div)[EV](CK,function(){
			        	CloseWindow(div);
			        	CommentatorsCount();
			        });
	      
	      }  //  CommentatorsModityAccount结束
	      
	      
	      
	      //网评员添加按钮事件
	      function AddCommentatorsCountHandle(){
	      	   var bodyHTML='<div class="giveMe_do coor_add"><div class="box_count" ><div class="div_style"><label class="com_fLeft">网评人员：</label>'+
	      	   '<input type="text"  class="task_Cwdith2 task_count"  id="commentators_name"  maxlength="4"/> </div></div>'+
	      	  '<div id="examineManager_comtorsName" style="margin-left: 100px; float: left; color: red; font-size: 12px; display: none;">'+
	      	  '<span class="ev_wCount">* 不能输入特殊字符或不填</span></div><div class="com_clear"></div>'+
	      	  '<div class="div_style"><label  class="com_fLeft">微博类型：</label>'+
	      	   '<select name="T_place" class="task_count task_Cwdith2"  id="commentators_weibostyes"><option value="1">新浪</option><!--<option value="2">腾讯</option><option value="5">人民</option>--></select> </div>'
	      	   +'<div class="div_style"><label  class="com_fLeft">微博账号：</label><input   id="commentators_weiboaccounts"  type="text"  class="task_Cwdith1 task_count" />'+
	      	   '<div class="tWord_add"  isFlag="0">添加</div></div>'+  //isFlag  1 不可以在点击 0 可以在次点击
	      	   //'<div class="tWord_add">添加</div></div>'+
	      	   '<div id="examineManager_comtorsAccName" style="margin-left: 100px; float: left; color: red; font-size: 12px; display: none;">'
	      	   +'<span class="ev_wCount">* 此微博帐号存在特殊字符或者空白符</span></div><div class="com_clear"></div>'+
	      	   '<div id="examineManager_comtorsCheck" style="margin-left: 100px; float: left; color: red; font-size: 12px; display: none;">'+
	      	   '<span class="ev_wCount">* 此微博帐号已经存在，无须添加</span></div><div class="com_clear"></div>'+
	      	   
	      	    '<div id="examineManager_comtorsSave" style="margin-left: 100px; float: left; color: red; font-size: 12px; display: none;">'+
	      	   '<span class="ev_wCount">* 此微博帐号不存在，请重新添加</span></div><div class="com_clear"></div>'+
	      	   
	      	   '<div class="event coor_tableBox"><table  id="commentators_table"  class="coor_table web_table">'+
	      	     '<th width="10%">序号</th><th width="25%">姓名</th> <th width="15%">媒体</th><th  width="40%">账号</th>'+
	      	   //'<th width="5%">序号</th><th width="3%">姓名</th> <th width="10%">媒体</th><th  width="10%">账号</th>'+
	      	   '{0}</table></div><div class="com_clear"></div><div class="push_ok">'+
	      	   '<!--<div class="in_MsetCno">取&nbsp;&nbsp;消</div>--><div class="in_MsetCok">关&nbsp;&nbsp;闭</div></div><div class="com_clear"></div></div>';
				
		      var tempHTML=' <tr> <td class="t0 com_none">{0}</td> <td  class="t1">{1}</td> <td class="t2">{2}</td><td class="t3">{3}</td><td class="tdOption event_do" style="">&nbsp;<div title="删除" class="event_doBotton event_del" id="comtorsId_{4}"></div></td> </tr> ';
			  var  tempHTMLOne=' <tr> <td class="t0 com_none">{0}</td> <td  class="t1">{1}</td> <td class="t2">{2}</td><td class="t3">{3}</td> </tr> ';
			  if(cuser.Templates['xtbg_khglCommentatorsDel_1']==UN){
					tempHTML=tempHTMLOne;
    	  	   }
		
    	    //然后给	tempHTML模版赋值
	    	var  totalHtml='';
	    		  if(cuser.Templates['xtbg_khglCommentatorsDel_1']==UN){
					bodyHTML=$[FO](bodyHTML,totalHtml); 
    	  	   }else{
					bodyHTML=$[FO](bodyHTML,'<th width="15%">操作</th>'+totalHtml);    	  	   
    	  	   }
    	  	   
    	  	   
		    $[WW]({
		        css: { "width": "470px", "height": "auto"},
		        //event: 'no',
		        title: '添加网评员帐号',
		        content: bodyHTML,
		        id: '_examinemanagerCommentators',
		        onLoad: function(div) {
		        	 var comtorsaddTemp=cuser.Templates['xtbg_khglCommentatorsAdd_1'];
					  if(comtorsaddTemp==UN){
						  $('.tWord_add',div)[RM]();
						   $('#commentators_weiboaccounts',div).css("width","296px");
					 }
		        
				 //写自己的添加弹出层业务
				    $('#examineManager_comtorsName',div).hide();
				 	$('#examineManager_comtorsAccName',div).hide();
				 	$('#examineManager_comtorsCheck',div).hide();
				 	$('#examineManager_comtorsSave',div).hide();
				 	//添加情况下 :第一次进入，是不显示列表的
				 	$('.event.coor_tableBox',div).hide();
				 	/*
				 	$('#commentators_table',div)[FD]('tr')[EH](function (i,item){
				 		 if(i!=0){
							 item[RM]();				 		 
				 		 }
				 	});
				 	*/
					//网评员弹出层当中的添加按钮功能以及确定，取消按钮功能
					CommentatorsADDAccount(div);
		      		},
		      		onClose:function(){
		      			//网评员添加弹出层页面中右上角的X号触发事件
		      			CommentatorsCount();
		      		}
    			}); 
    		  
	      } //网评员添加按钮事件结束
	      
	      	//网评员弹出层当中的添加按钮功能
			function CommentatorsADDAccount(div){
				$('#commentators_name',div)[EV](KU,function(){
					$('#examineManager_comtorsName',div).hide();
					$('#examineManager_comtorsCheck',div).hide();
					$('#examineManager_comtorsSave',div).hide();
				});
				
				/*
				$('#commentators_name',div).blur(function(){
				    //网评员只能最多输入四个字
					if($(this).val()[LN]>4){
						 $(this).val($(this).val().substring(0,4));
					}
				});
				*/
			
				$('#commentators_weiboaccounts',div)[EV](KU,function(){
				 	$('#examineManager_comtorsAccName',div).hide();
				 	$('#examineManager_comtorsSave',div).hide();
				 	$('#examineManager_comtorsCheck',div).hide();				
				});
			
			        $('.tWord_add',div)[EV](CK,function(){
			 			   if(parseInt($(this)[AT]('isFlag'))==0){
			             	$('.tWord_add',div)[AT]({'isFlag':1});
			             //人员名字
			              var  ComtorsName=$[TM]($('#commentators_name',div).val());
			              //获取uid 值，后期会做下拉框里取值的
			              //+++++++++++++++++
			              var  uid='';
			              //获取微博帐号的名称
			              var  accName=$[TM]($('#commentators_weiboaccounts',div).val());
			              //获取那种类型平台
			        	  var  platform=  $('#commentators_weibostyes',div).val();
			             
			             //添加事件来判断人员名字的是否存在特殊字符或者没有填写
			             if(ComtorsName==''){
			                $('#examineManager_comtorsName',div).show();
			                 $('.tWord_add',div)[AT]({'isFlag':0});
			                    return FE;
			             }else{
			             	 var regu = /^[0-9a-zA-Z_\-\u4e00-\u9fa5]+$/;      
			             	if (!regu.test( ComtorsName )) {   
							    $('#examineManager_comtorsName',div).show();
							    $('.tWord_add',div)[AT]({'isFlag':0});
								return  FE;    						
    						}
			             }

			             //添加事件中判断是否微博帐号存在或者本身输入的就是空白符或者特殊字符
						//空白符或者特殊字符，不是汉字的
						if(accName==""){
						   $('#examineManager_comtorsAccName',div).show();
						   $('.tWord_add',div)[AT]({'isFlag':0});
			                    return FE;
						}else{
						    var regu = /^[0-9a-zA-Z_\-\u4e00-\u9fa5]+$/;      
			             	if (!regu.test( accName )) {   
							    $('#examineManager_comtorsAccName',div).show();
							    $('.tWord_add',div)[AT]({'isFlag':0});
								return  FE;    						
    						}
						}
						
						//添加事件中判断是否微博帐号存在(不存在的情况)
						//+++++++++++++++++++++
			        		$(BY)[AJ]({
									param: {action: 'examineManager.weibodcommentatorscountadd', tempList:'checkCommentators' 
									,name:ComtorsName, uid:uid, platform:platform,accName:accName},
									success: function(data){
										$('.event.coor_tableBox',div).show();
										$('#commentators_table',div).show();
										var dataOne=data;
									
									//不管存在不存在 都把列表显示出来			
									$(BY)[AJ]({
										param: {action: 'coordination.examineManager',  tempList:'CommentatorsAddList' ,name:ComtorsName },
										success: function(data) {
											 	//把添加网评员弹出层中的列表，再次更改名字或者是再次
											 	//添加此网评员名字，而刷新下面的列表
											 	$('.event.coor_tableBox',div).show();
											 	$('#commentators_table',div)[FD]('tr')[EH](function (i,item){
											 		 if(i!=0){
														 //item[RM]();
														$(this)[RM]();				 		 
											 		 }
											 	});
										
											  var HTMLTemp='<tr><td  class="t0 com_none">{0}</td> <td class="t1">{1}</td> <td  class="t2">{2}</td> <td class="t3">{3}</td>   <td class="tdOption event_do" style="">&nbsp;<div title="删除" class="event_doBotton event_del" id="comtorsId_{4}"></div></td></tr> ';
										      var HTMLTempOne=' <tr> <td class="t0 com_none">{0}</td> <td  class="t1">{1}</td> <td class="t2">{2}</td><td class="t3">{3}</td> </tr> ';
										      if(cuser.Templates['xtbg_khglCommentatorsDel_1']==UN){
										      		HTMLTemp=HTMLTempOne;
										      }
										    
										      var  totalHtml='';
										      $[EH](data,function (i,item){
										      		if(cuser.Templates['xtbg_khglCommentatorsDel_1']==UN){
										      			 totalHtml+=$[FO](HTMLTemp,item.comtorsUID,item.personName,item.platType,item.accName); 	
										      		}else {
										      			 totalHtml+=$[FO](HTMLTemp,item.comtorsUID,item.personName,item.platType,item.accName,item.id+'_'+item.comtorsUID); 	
										      		}
										      }); //each结束
											$('#commentators_table',div)[AP](totalHtml);
										
										//删除网评员弹出层的序号
									 	$('#commentators_table',div)[FD]('tr')[EH](function (i){
										      if( $[TM]($(this)[FD]('th').eq(0)[TX]())=='序号'){
									 			  $(this)[FD]('th').eq(0).hide();//[RM]();
									 			  $('.t0',	$('#commentators_table',div)).hide();//[RM]();
									 		}
										      /*
										      if(i==0){
										    	$(this)[FD]('th').eq(0)[RM]();
										       }
										       else{
												$(this)[FD]('.t0')[RM]();					       
										       }
										       */
									 	});
											
								       if(dataOne!="-1"&&dataOne!="-2"){
										    $('#examineManager_comtorsCheck',div).hide();
										     $('#examineManager_comtorsSave',div).hide();
										     
												//没有重复，然后添加成功，然后在显示出在列表当中
										 // var HTML='<tr><td  class="t0">{0}</td> <td class="t1">{1}</td> <td  class="t2">{2}</td> <td class="t3">{3}</td>   <td class="tdOption event_do" style="">&nbsp;<div title="删除" class="event_doBotton event_del" id="comtorsId_{4}"></div></td></tr> ';
			      	                 //     var value=ParseEnum(parseInt($('#commentators_weibostyes',div).val()));
										 //commentators_table
								       
								       //   HTML=$[FO](HTML,dataOne,$('#commentators_name',div).val(),value,$('#commentators_weiboaccounts',div).val(),dataOne);
					
								         //	$('#commentators_table',div)[FD]('tr:last').after($(HTML));
								         		 	$('.event.coor_tableBox',div).show();
											
											//		$('#commentators_table',div)[AP](HTML);
				 	
								         	//添加成功，然后上面的 ： 人名和微博名要重置
								         	//$('#commentators_name',div).val('');
								         	$('#commentators_weiboaccounts',div).val('');
								         	$('.tWord_add',div)[AT]({'isFlag':0});
								       /*  	
								         	//新添加的这个给它绑定一个删除按钮事件
								         	$('#comtorsId_'+dataOne,div)[EV](CK,function(){
								         	            var nid =$(this)[AT]('id').substr(10);
								         	            var delid=$(this)[AT]('id');
								                   		$(BY)[AJ]({
								                   			param: {action: 'coordination.examineManager',tempList:'deleteComtorsID',nid:nid},
								                   				success: function(data){
								                   				    alert(data);
								                   				    $('#'+delid,div)[PT]()[PT]()[RM]();
								                   				}
								                   		});
								         	});
								         	//新添加的删除按钮事件结束
								   */      	
										}//添加网评员成功结束
											else{
											if(dataOne=='-1'){
												//说明重复了，请重新添加
											$('#examineManager_comtorsCheck',div).show();
											}else if(dataOne=='-2'){
												//帐号压根不存在
												$('#examineManager_comtorsSave',div).show();
											 }
										$('.tWord_add',div)[AT]({'isFlag':0});	 
										}  //else 结束
									
									      	//网评员弹出层中的删除按钮事件
											$('.event_doBotton.event_del',div)[EH](function (i,item){
												    var  cid=$(this)[AT]('id');
													$('#'+cid,div)[EV](CK,function (){
																$(BY)[AJ]({
															                   			param: {action: 'examineManager.weibodcommentatorscountdel',tempList:'deleteComtorsID',nid:   cid.split('_')[1] //data.rows[i].id //cid.substr(10)
															                   			,comtorsUID:  $[TM]($(this)[PT]()[PT]()[FD]('td').eq(0)[TX]())},
															                   				success: function(data){
																							        // alert(data);
																								    $('#'+cid,div)[PT]()[PT]()[RM]();
															                   				}//success结束
															                   		});//ajax结束
														});
												});
									
										//---------------------------
										}  //  输入当前列表的展示  success
									});
									}// checks  success
							});
							} // isFlag 判断结束
			        });//添加按钮事件结束

			        
			        //确定按钮
			        $('.in_MsetCok',div)[EV](CK,function(){
			        	CloseWindow(div);
			        	CommentatorsCount();
			        });
			        //取消按钮
			            $('.in_MsetCno',div)[EV](CK,function(){
			        	CloseWindow(div);
			        	CommentatorsCount();
			        });
			}

		//枚举判断
		 			function  ParseEnum(type){
		 	         		var tt = context.Enum.TwitterType;
							switch (type) {
							case tt.Sina: return '新浪';
							case tt.Tencent: return '腾讯';
					    	case tt.People: return '人民';	
							default: return '';
					}
		 }
		 
		
	    //关闭窗体事件
    function CloseWindow(div) {
      div[RM]();
      $[RM]({ obj: '#' + JLY });
    }


		//微博处理统计上转发事件
	function DealWithRePostHandle(curuserid){
   	 	 	 ChangePageShow.html('');
        	 ChangePageShow.show();
        	 var stime=dealwithStartTime.val();
        	 var etime=dealwithEndTime.val();
        	 var  uid=examineSelect2[AT]("value").split(',')[0];
        	 var   platformType=examineSelect2[AT]("value").split(',')[1];
			 ChangePageShow[AP]('<div  id="dealwithRepostsCount_list"  class="publishManage" style="min-height:50px"></div>');
	      	 var ChangePageShow2=$('#dealwithRepostsCount_list',ChangePageShow);
			 ChangePageShow2.add(loadlistPager).html('');
			 ChangePageShow2.list({
          		 //url:'',
          		 //methodName:'',
          		 width:998,
          		// id:'',
          		 rows: 'rows', 
          		 pageSize:10,
          		 pager:loadlistPager ,
          		 param: {action: 'coordination.examineManager',tempList:'dealwithRePostAll',starttime:stime,endtime:etime
          		,type:0,  uid:uid,platformType:platformType,curuserid:curuserid },
          		 templates:[{html:'<div class="cont_list"  id="{0}"><h2>'},
          		 			//{html:'<span >发布时间：</span><span>{0}</span>'},
          		 			{html:'<span >{0}</span>'},//发布---时间
          		 			{html:'<span>发布账号：</span><div class="com_fLeft send_style send_cont1">{0}   '},
          		 			//{html:'  </h2>  <div class="taikMe_List"><div class="talkMe_count">{0}</div></div><div class="com_clear"></div><div class="cont_do"><div class="cont_doleft"><span>提交时间</span>'},
          		 			{html:'  </h2>  <div class="taikMe_List"> <h3 class="yuanc_title yuanc_tMar">{0}</h3>   </div>        <div class="com_clear"></div><div class="cont_do"><div class="cont_doleft"><span>提交时间</span>'},
          		 			
          		 			{html:'<span>{0}</span>'},
          		 			{html:'<span>{0}</span>'},//修改--提交人 // 审核人
          		 			{html:'{0} '},
          		 			{html:'<span class="com_none"  id="{0}"></span> </div><div  class="cont_doright">    </div></div></div>'}],
          		 columns:[{ field: 'ids' },
          		 				  { field: 'sentTime' },
	                        		{ field: 'weiboAccountHTML' },
	                              	{ field: 'text' },
	                                { field: 'submitTime' },
	                              { field: 'changeType'},
	                               { field: 'mofityers'},
	                                { field: 'auditorInt'}],
          		 onComplete:function (This, refresh, data) {
          		 			    if(data.rows.length==0){
									base.not(This);
									return  FE;
								}
          		 				var talkMeContent=$('.yuanc_title',This);
          		 		  		//控制+ -按钮
								 var   amBtn=$('.send_showMore',This);
								HideOrShowAccount(amBtn);	    
						       //新添加的转发原帖(也就是阴影部分的) 的模版
							   var ShadowTempHtml=' <div class="talkMe_count"><div  class="deal_tipPic"></div> <h3 class="yuanc_title2 yuanc_title">   <span class="cmanage_color font-color">{0}</span>  <br /><span class="pm_repostTid">{1}</span> <br/> {2} </h3>  <p class="p_mar"><span>{3}  </span><span>  评论[{4}]</span><span>转发[{5}]</span></p></div>';
								//如果没有发布帐号的 ,需要把发布帐号不显示
								ChangePageShow2[FD]('.com_fLeft.send_style')[EH](function (){
										if($(this)[FD]('.pm_All')[LN]==0){
												$(this).prev()[TX]('');							 
										}								
								});
								
								
									ParseText(talkMeContent,data);
									//如果有阴影部分的添加转发原帖
								$('.taikMe_List',This)[EH](function (i,item){
									 var index=$('.taikMe_List',This)[ID]($(this));
									 if(data.rows[index].pmr!=UN &&data.rows[index].pmr!=""){
									    var  PMR=data.rows[index].pmr;
									    var  y=PMR.repostTime.substr(0,PMR.repostTime.lastIndexOf(' '));
									    var   h=PMR.repostTime.substr(PMR.repostTime.lastIndexOf(' '));
									    var  repostText=RelaceRepost(PMR.repostText); 
									     var  htm='';
									      if(PMR.Images[LN]!=0){
									      var  totalImghtm=   RelaceImage(PMR.Images);
									        htm=$[FO](ShadowTempHtml, PMR.repostName, repostText, totalImghtm, y+'&nbsp;&nbsp;'+h,PMR.repostNum,PMR.commentNum );
									      }else{
									        htm=$[FO](ShadowTempHtml,  PMR.repostName, repostText,  '' , y+'&nbsp;&nbsp;'+h, PMR.repostNum,PMR.commentNum);
									      }
									      $(this)[AP](htm);
									 } // pmr 不为null
								}); //阴影结束
							
          		 }// onComplete结束
          	});				
					
		}	//DealWithRePostHandle 结束
/*
		function DealWithRePostHandle(){
   	 	 	 ChangePageShow.html('');
        	 ChangePageShow.show();
        	 var stime=dealwithStartTime.val();
        	 var etime=dealwithEndTime.val();
        	 var  uid=examineSelect2[AT]("value").split(',')[0];
        	 var   platformType=examineSelect2[AT]("value").split(',')[1];
				ChangePageShow[AP]('<div  id="dealwithRepostsCount_list"></div>');
	      	    var ChangePageShow2=$('#dealwithRepostsCount_list',ChangePageShow);
			    ChangePageShow2.add(loadlistPager).html('');
			
	      	    base.DataBind({ 
				panel: ChangePageShow2, 
				pager: loadlistPager,
				id: 'dealwith_reposttome_count', 
			    param: {action :'coordination.examineManager',tempList:'dealwithRePostAll',uid:uid,stime:stime,etime:etime},
				//template: '<span>转发</span><span>|</span><span>评论</span><span>|</span><span >收集</span>',
			    //template: base.FormatPmi('{pmi:yqzs_collect_1}<span class="repostCount">转发</span><span>|</span><span class="commentCount">评论</span>'),
			   // template: base.FormatPmi('{pmi:yqzs_collect_1}{pmi:wdwb_adwdrepost_1}{pmi:wdwb_adwdcomment_1}'),
					template: base.FormatPmi('{pmi:yqzs_collect_1}{pmi:wdsy_repost_1}{pmi:wdsy_comment_1}'),				
				isComment:FE,
				moduleType: _enum.ModuleType.talkMe,
				isRepostCommonCount:TE,
				onComplete: function (This, refresh, data) { 
					$('.com_fRight.subBtnPanel',This)[EH](function (){
						 $(this)[RM]();					 
					});
				},
				onSend:function(txt, isChecked, accountCollection, postID, url, type, twitterID, closeBtn){
					 //评论或者转发触发的事件
				var param = {};
				//	param.action = type == 'repost'? 'talkMe.talkMeRepost': 'talkMe.talkMeComment';
			   // param.queryConditions=type == 'repost'? 'talkMeRepost': 'talkMeComment';
				param.action = type == 'repost'? 'myTwitter.saveReport': 'myTwitter.saveComment';
			    
				param.isChecked = isChecked;
				param.content = txt;
					if (type == 'repost' ) {
					//不填转发内容的时候，默认值
					if ($[TM](txt) == '') {
					//if (txt == '') {
						txt = '转发微博';
					}
				} else {
				if ($[TM](txt) == '') {
					//if (txt == '') {
						$[MB]({
							content: '评论内容不能为空', 
							type: 2
						});
						return;
					}
				}
				
				var accountids = [], platforms = [];
				$[EH](accountCollection, function(i, dataRow) {
					accountids.push(dataRow.uid);
					platforms.push(dataRow.type);
				});
				param.accountuids = accountids.join(',');
				param.platforms = platforms.join(',');
				param.twitterID = twitterID;
				//param.currentuserUID=currentUID;
				
				$(BY)[AJ]({
					param: param,
					dataType: 'text',
					success: function(data) {
						if(data) {
							$[MB]({content: '提交成功', type: 0});
							closeBtn[TR](CK); //关闭子页面
						} else {
							$[MB]({content: '提交失败', type: 2});
						}
					}
				});
				
				
				}//onSend 结束
			});

		}
*/


		//微博处理统计上评论事件
			function 	DealWithCommentsHandle(curuserid){
    	 	 ChangePageShow.html('');
        	 ChangePageShow.show();
        	 ChangePageShow[AP]('<div  id="dealwithCommentCount_list" class="publishManage" style="min-height:50px"></div>');
	      	  var ChangePageShow2=$('#dealwithCommentCount_list',ChangePageShow);
    	     var  listDiv=ChangePageShow2;
		 	 listDiv.add(loadlistPager).html('');
        	 var stime=dealwithStartTime.val();
        	 var etime=dealwithEndTime.val();
        	 var  uid=examineSelect2[AT]("value").split(',')[0];
        	 var   platformType=examineSelect2[AT]("value").split(',')[1];
    		 ChangePageShow2.list({
          		 //url:'',
          		 //methodName:'',
          		 width:998,
          		// id:'',
          		 rows: 'rows', 
          		 pageSize:10,
          		 pager:loadlistPager ,
          		 param: {action: 'coordination.examineManager',tempList:'dealwithCommentsAll',  starttime:stime,endtime:etime
          		,type:0,  uid:uid,platformType:platformType ,curuserid:curuserid },
          		 templates:[{html:'<div class="cont_list"  id="{0}"><h2>'},
          		 			//{html:'<span >发布时间：</span><span>{0}</span>'},
          		 			{html:'<span >{0}</span>'},//发布---时间
          		 			{html:'<span>发布账号：</span><div class="com_fLeft send_style send_cont1">{0}   '},
          		 			//{html:'  </h2>  <div class="taikMe_List"><div class="talkMe_count">{0}</div></div><div class="com_clear"></div><div class="cont_do"><div class="cont_doleft"><span>提交时间</span>'},
          		 			{html:'  </h2>  <div class="taikMe_List"> <h3 class="yuanc_title yuanc_tMar">{0}</h3>   </div>        <div class="com_clear"></div><div class="cont_do"><div class="cont_doleft"><span>提交时间</span>'},
          		 			
          		 			{html:'<span>{0}</span>'},
          		 			{html:'<span>{0}</span>'},//修改--提交人 // 审核人
          		 			{html:'{0} '},
          		 			{html:'<span class="com_none"  id="{0}"></span> </div><div  class="cont_doright">    </div></div></div>'}],
          		 columns:[{ field: 'ids' },
          		 				  { field: 'sentTime' },
	                        		{ field: 'weiboAccountHTML' },
	                              	{ field: 'text' },
	                                { field: 'submitTime' },
	                              { field: 'changeType'},
	                               { field: 'mofityers'},
	                                { field: 'auditorInt'}],
          		 onComplete:function (This, refresh, data) {
          		 			    if(data.rows.length==0){
									base.not(This);
									return  FE;
								}
          		 				var talkMeContent=$('.yuanc_title',This);
          		 		  		//控制+ -按钮
								 var   amBtn=$('.send_showMore',This);
								HideOrShowAccount(amBtn);	    
						       //新添加的转发原帖(也就是阴影部分的) 的模版
							   var ShadowTempHtml=' <div class="talkMe_count"><div  class="deal_tipPic"></div> <h3 class="yuanc_title2 yuanc_title">   <span class="cmanage_color font-color">{0}</span>  <br /><span class="pm_repostTid">{1}</span> <br/> {2} </h3>  <p class="p_mar"><span>{3}  </span><span>  评论[{4}]</span><span>转发[{5}]</span></p></div>';
								//如果没有发布帐号的 ,需要把发布帐号不显示
								ChangePageShow2[FD]('.com_fLeft.send_style')[EH](function (){
										if($(this)[FD]('.pm_All')[LN]==0){
												$(this).prev()[TX]('');							 
										}								
								});
								
									ParseText(talkMeContent,data);
									//如果有阴影部分的添加转发原帖
								$('.taikMe_List',This)[EH](function (i,item){
									 var index=$('.taikMe_List',This)[ID]($(this));
									 if(data.rows[index].pmr!=UN &&data.rows[index].pmr!=""){
									    var  PMR=data.rows[index].pmr;
									    var  y=PMR.repostTime.substr(0,PMR.repostTime.lastIndexOf(' '));
									    var   h=PMR.repostTime.substr(PMR.repostTime.lastIndexOf(' '));
									    var  repostText=RelaceRepost(PMR.repostText); 
									     var  htm='';
									      if(PMR.Images[LN]!=0){
									      var  totalImghtm=   RelaceImage(PMR.Images);
									        htm=$[FO](ShadowTempHtml, PMR.repostName, repostText, totalImghtm, y+'&nbsp;&nbsp;'+h,PMR.repostNum,PMR.commentNum );
									      }else{
									        htm=$[FO](ShadowTempHtml,  PMR.repostName, repostText,  '' , y+'&nbsp;&nbsp;'+h, PMR.repostNum,PMR.commentNum);
									      }
									      $(this)[AP](htm);
									 } // pmr 不为null
								}); //阴影结束
							
          		 }// onComplete结束
          	});
    		
    		
    		
    		
    	}  // DealWithCommentsHandle 结束
		
		/*
    	function 	DealWithCommentsHandle(){
    	 	 ChangePageShow.html('');
        	 ChangePageShow.show();
        	 ChangePageShow[AP]('<div  id="dealwithCommentCount_list"></div>');
	      	  var ChangePageShow2=$('#dealwithCommentCount_list',ChangePageShow);
    	     var  listDiv=ChangePageShow2;
		 	 listDiv.add(loadlistPager).html('');
        	 var stime=dealwithStartTime.val();
        	 var etime=dealwithEndTime.val();
        	 var  uid=examineSelect2[AT]("value").split(',')[0];
        	 var   platformType=examineSelect2[AT]("value").split(',')[1];
   			 listDiv[LS]({
	            width: 1000,
	            id: 'examineManager_dealwith_comments',
	            pager: loadlistPager,
	            isOption: FE, //是否显示全选、反选
	            param: {action :'coordination.examineManager',tempList:'dealwithCommentsAll',  stime:stime,etime:etime,uid:uid ,platform:platformType},
	        		  templates: [{html: '<div class="cont_list"><div class="cont_photo"><img src="" class="accountImg" width="50" height="50"><div class="photo_allInfor com_none acInfo"></div></div><div class="pl_countLsit"><div class="taikMe_List"><div class="talkMe_count">  <div> <span class="Fname_color accountName"></span>{0}</div>' }, //评论内容
	                      	      {html: '<div class="pl_mar"><span class="pl_mar pl_wdwb"></span><span class="Fname_color Fname_nomal">{0}</span></div>'},  //微博内容
	                              {html: '<p class="pl_mar"><span>{0}</span>'}, //评论时间
	                      	      {html: '<span>来源</span><span>{0}</span>'},//来源
								  {html: base.FormatPmi('{pmi:wdpl_commentsData_1}')},  //要回复的评论id <span class="pl_do" id="review_id{0}">回复</span>

								  {html: '<span class="pl_do" id="sg_id{0}">|</span>'},
								  
								 {html: base.FormatPmi('{pmi:wdpl_deleteReviewData_1}')},//要删除的评论id <span class="pl_do" id="delete_id{0}">删除</span>
								  
								  {html: '</p><div class="com_clear"></div><div class="relay myreview_reply com_none" id="relay_{0}"><div class="ralay_middle"><br />'}, //评论DIV
								  {html: '<div class="com_clear"></div><textarea class="relay_count" id="reviewtextarea_{0}"></textarea>'}, //评论内容
								  {html: '<div class="send_do"><div class="send_left"><span class="s_pic s_bqPc" id="bqingbtn_{0}">&nbsp;'}, //评论时间
								  {html: '<div class="bq_select com_none  panel" id="swbqingpanel_{0}"></div></span>'}, //表情
								  {html: '<input type="checkbox" class="relay_input" id="relaycheck_{0}" /><label for="relay_sel">&nbsp;同时转发到我的微博</label></div><div class="send_right">'}, //是否选中
								  {html: '<div class="send_over send_back" id="replyBut_{0}">评&nbsp;&nbsp;论</div></div><div class="com_clear"></div></div></div></div></div></div></div><div class="com_clear"></div></div> '}], //评论按钮
	
	            columns: [ { field: 'content'},
	                              { field: 'posttext'},
	                              { field: 'createtime', maxLength: 200},
	                              { field: 'source' },
	                              { field: 'dealwithCommentsID' },
	                              { field: 'dealwithCommentsID' },
	                              { field: 'dealwithCommentsID' },
	                              { field: 'dealwithCommentsID' },
	                              { field: 'dealwithCommentsID' },
	                              { field: 'dealwithCommentsID' },
	                              { field: 'dealwithCommentsID' },
	                              { field: 'dealwithCommentsID' },
	                              { field: 'dealwithCommentsID'}],
	                         
	            onComplete: function (This, refresh, data) {
	       	    if(data.rows.length==0){
							base.not(This);
							return  FE;
						}
	          
	            	//设置账号头像、昵称
					var accountImg = $('.accountImg', This),
	            		   accountName = $('.accountName', This),
	            		   pl_wdwb = $('.pl_wdwb',This);
					accountImg[EH](function (i, item) {
	        		   	      var $this = $(this),
	        		   		   index = accountImg[ID]($this),
	        		   		   dataRow = data.rows[index],
	        		   		   account = dataRow.account;
	   		   			$this[AT]('src', account.imgurl);
	   		   				//$('#review_id'+data.rows[index].dealwithCommentsID,$key).show();
	   		   				//$('#huifu',$key).hide();
	   		   				accountName.eq(i).text(account.name);
	   		   				pl_wdwb.eq(i).text('评论我的微博：');
					});

					//显示用户信息
	            	var accountInfo = $('.acInfo', This);
	            	accountInfo[EH](function() {
						var $this = $(this),
							   index = accountInfo[ID]($this),
							   accountRow = data.rows[index].account;
						base.AccountTip($this, accountRow,_enum.ModuleType.ExamineManager);
					});
					
					//循环回复 删除
					$[EH](data.rows,function(i,item){
						var id = item.dealwithCommentsID,
							dataRow = data.rows[i],
	        		   		account = dataRow.account;
						//点击回复 打开回复面板
						$('#review_id'+id,$key)[EV](CK,function(){
						
								var flag= $('#relay_'+id,$key).css("display");//获取样式是否隐藏
								
								if(flag == "none"){
									 $('#relay_'+id,$key).show();
									 $('#reviewtextarea_'+id).val("回复@"+account.name+"：");
									//显示表情
									 //$('#swbqingpanel_'+id,$key).show();
									 base.FacePicker($('#bqingbtn_'+id,$key), $('#swbqingpanel_'+id,$key), $('#reviewtextarea_'+id,$key), $('.panel',$key));
									 $('#replyBut_'+id,$key)[EV](CK,function(){
								
									 	var textarea = $('#reviewtextarea_'+id).val(),	  //评论内容
									 		state =  $("#relaycheck_"+id).prop("checked");//是否选中状态
									 		if(textarea == "回复@"+account.name+"：" || textarea == ''){
									 			$[MB]({
													content: '写点东西吧，评论内容不能为空哦。',
													type: 1,
													onAffirm: function (boolen) { 
															
													}
												}); 
									 		}else{
									 			$(BY)[AJ]({
													param: {action: 'myTwitter.comments', tid: id,textarea: textarea,state: state,ucode:$('#pulldownSelect',weiboDealwithLoadlist)[TX]() ,uid: uid},
													success: function(data){
															$('#relay_'+id,$key).hide();
															DealWithCommentsHandle();
													}
												});
									 		}
		
									 });
								}else{
									$('#relay_'+id,$key).hide();
								}
						});
						//点击删除 删除评论任务
						$('#delete_id'+id,$key)[EV](CK,function(){
						
								$[MB]({
									  	content: '您确定要删除吗？',
										type: 4,
										onAffirm: function (boolen) { 
											if(boolen == true){
												$(BY)[AJ]({
														param: {action: 'myTwitter.deleteReview', tid: id,type: 0,platform: 1},
														success: function(data){
															DealWithCommentsHandle();
														}
												});
											}
										}
								}); 			
						});
					});
					
	            }  //onComplate 结束
	        }); // listdiv 加载结束
    			  
    	}
    	*/

	      
	      //微博处理统计各个模块的功能
	      function     DealWithSetOptions( This, refresh,data){
	    	//t2,3class 列加上手指
	    	 This[FD]('.t2')[EH](function (){
		  		    var t2TXT=$(this)[TX]();
		  		    //$(this).css('cursor','');
		  	        var last='<a style="cursor:pointer"> '+t2TXT+'</a>';
		  	        $(this).html(last);
		  	});
	    
	    	 This[FD]('.t3')[EH](function (){
		  		    var t3TXT=$(this)[TX]();
		  		    //$(this).css('cursor','');
		  	        var last='<a style="cursor:pointer"> '+t3TXT+'</a>';
		  	        $(this).html(last);
		  	});
	    
	    //t2,t3点击进去触发的事件
	     $('.t2',This)[FD]('a')[EV](CK,function (){
	         examineManagerTitleBar.hide();
		     weiboDealwithLoadlist.hide();
		     navigationBars.Empty()
			.Add(key, 'ToolBar', '协同办公')
    		.Add(key, 'ToolBar', '考核管理')
    		.Add(key, 'DealWithWeiBoComments', '微博详情')[oL]();
    		var curuserid=$(this)[PT]()[PT]()[FD]('td').eq(0)[FD]('input').val();
    		dealwithTemps=curuserid;
    		//微博处理统计上评论事件
    		DealWithCommentsHandle(curuserid);
	     });
	    
	     $('.t3',This)[FD]('a')[EV](CK,function (){
	         examineManagerTitleBar.hide();
		     weiboDealwithLoadlist.hide();
		     navigationBars.Empty()
			.Add(key, 'ToolBar', '协同办公')
    		.Add(key, 'ToolBar', '考核管理')
    		.Add(key, 'DealWithWeiBoRePosts', '微博详情')[oL]();
    		var curuserid=$(this)[PT]()[PT]()[FD]('td').eq(0)[FD]('input').val();
    		dealwithTemps=curuserid;
    		DealWithRePostHandle(curuserid);
	     });
	    
	    	//判断如果单个全部选中的各种情况
          		   CheckBoxOptions( This, refresh,data);
		   //判断如果单个全部选中的各种情况结束
		
		//发布统计搜索按钮事件
		$('#dealwith_count_searchBtn',$key)[EV](CK,function(){
		 //查询处理微博统计
		//判断时间起始不能大于结束时间
	    	  var tt	= base.IsRegExpDateTime(dealwithStartTime,dealwithEndTime,TE);
	    	  if(tt==TE){
					DealWithCount();
	    	  }
		});
		
		//处理微博统计导出按钮事件
		$('#dealwith_count_importBtn',$key)[EV](CK,function(){
		   alert("微博处理统计导出");
		   //++++++++++++++
		});
	      }
	
	//微博发布统计中的提交按钮事件
	function  RelaseSumbitNumList(type,cuid){
        	 ChangePageShow.show();
        	 ChangePageShow.html('');
        	 ChangePageShow[AP]('<div  id="relaseSubmitCount_list"  class="publishManage" style="min-height:50px"></div>');
	      	 var ChangePageShow2=$('#relaseSubmitCount_list',ChangePageShow);
		 	 ChangePageShow2.add(loadlistPager).html('');
			var stime=releaseStartTime.val();
			var etime =releaseEndTime.val();
		    	ChangePageShow2.list({
          		 //url:'',
          		 //methodName:'',
          		 width:998,
          		// id:'',
          		 rows: 'rows', 
          		 pageSize:10,
          		 pager:loadlistPager ,
          		 param: {action: 'coordination.examineManager',tempList:'relaseSumbitNum',starttime:stime,endtime:etime,type:type, cuid:cuid},
          		 templates:[{html:'<div class="cont_list"  id="{0}"><h2>'},
          		 			//{html:'<span >发布时间：</span><span>{0}</span>'},
          		 			{html:'<span >{0}</span>'},//发布---时间
          		 			{html:'<span>发布账号：</span><div class="com_fLeft send_style send_cont1">{0}   '},
          		 			//{html:'  </h2>  <div class="taikMe_List"><div class="talkMe_count">{0}</div></div><div class="com_clear"></div><div class="cont_do"><div class="cont_doleft"><span>提交时间</span>'},
          		 			{html:'  </h2>  <div class="taikMe_List"> <h3 class="yuanc_title yuanc_tMar">{0}</h3>   </div>        <div class="com_clear"></div><div class="cont_do"><div class="cont_doleft"><span>提交时间</span>'},
          		 			
          		 			{html:'<span>{0}</span>'},
          		 			{html:'<span>{0}</span>'},//修改--提交人 // 审核人
          		 			{html:'{0} '},
          		 			{html:'<span class="com_none"  id="{0}"></span> </div><div  class="cont_doright">    </div></div></div>'}],
          		 columns:[{ field: 'ids' },
          		 				  { field: 'sentTime' },
	                        		{ field: 'weiboAccountHTML' },
	                              	{ field: 'text' },
	                                { field: 'submitTime' },
	                              { field: 'changeType'},
	                               { field: 'mofityers'},
	                                { field: 'auditorInt'}],
          		 onComplete:function (This, refresh, data) {
          		 			    if(data.rows.length==0){
									base.not(This);
									return  FE;
								}
          		 				var talkMeContent=$('.yuanc_title',This);
          		 		  		//控制+ -按钮
								 var   amBtn=$('.send_showMore',This);
								HideOrShowAccount(amBtn);	    
						       //新添加的转发原帖(也就是阴影部分的) 的模版
							   var ShadowTempHtml=' <div class="talkMe_count"><div  class="deal_tipPic"></div> <h3 class="yuanc_title2 yuanc_title">   <span class="cmanage_color font-color">{0}</span>  <br /><span class="pm_repostTid">{1}</span> <br/> {2} </h3>  <p class="p_mar"><span>{3}  </span><span>  评论[{4}]</span><span>转发[{5}]</span></p></div>';
								//如果没有发布帐号的 ,需要把发布帐号不显示
								ChangePageShow2[FD]('.com_fLeft.send_style')[EH](function (){
										if($(this)[FD]('.pm_All')[LN]==0){
												$(this).prev()[TX]('');							 
										}								
								});
								
									ParseText(talkMeContent,data);
									//如果有阴影部分的添加转发原帖
								$('.taikMe_List',This)[EH](function (i,item){
									 var index=$('.taikMe_List',This)[ID]($(this));
									 if(data.rows[index].pmr!=UN &&data.rows[index].pmr!=""){
									    var  PMR=data.rows[index].pmr;
									    var  y=PMR.repostTime.substr(0,PMR.repostTime.lastIndexOf(' '));
									    var   h=PMR.repostTime.substr(PMR.repostTime.lastIndexOf(' '));
									    var  repostText=RelaceRepost(PMR.repostText); 
									     var  htm='';
									      if(PMR.Images[LN]!=0){
									      var  totalImghtm=   RelaceImage(PMR.Images);
									        htm=$[FO](ShadowTempHtml, PMR.repostName, repostText, totalImghtm, y+'&nbsp;&nbsp;'+h,PMR.repostNum,PMR.commentNum );
									      }else{
									        htm=$[FO](ShadowTempHtml,  PMR.repostName, repostText,  '' , y+'&nbsp;&nbsp;'+h, PMR.repostNum,PMR.commentNum);
									      }
									      $(this)[AP](htm);
									 } // pmr 不为null
								}); //阴影结束
							
          		 }// onComplete结束
          	});

	}  //RelaseSumbitNumList结束
	
		//控制+ - 按钮，来显示或者隐藏上面的微博帐号
	//  obj :  + - 按钮的对象
	function    HideOrShowAccount  (obj){
						obj[EV](CK,function (){
			      			 var   temp=$(this)[TX]();
			      			 if(temp=="+"){
			      			 	$(this)[TX]('-');
			      			 	$(this).prev()[RC]('send_cont1')[AC]('send_cont2');
			      			 }else{
			      			 $(this)[TX]('+');
			      			 $(this).prev()[RC]('send_cont2')[AC]('send_cont1');
			      			 }
			});
	}
	
	
	         //分析文字的图片,表情文字等
          function   ParseText(obj,data){
							//调用公用方法解析文本
							obj[EH](function (){
									var  tt=$(this);
									//var  content=tt.html();
								   var index=obj[ID]($(this));
								   var content=data.rows[index].text;
								   
								   //去掉 不是img 标签的， 要转义成字符
								    var ayss  = content.match(/<img[\s\S]+?>/g);
									content=content[RP](/<img[\s\S]+?>/g,'');
									content= base.htmlEncodeByRegExp(content)+'<br/>';
									if(ayss!=UN){
									 for (var k=0;k<ayss[LN];k++){
										// content+=ayss[k];
										content+=ayss[k][RP]('$',userWeiboImagePath);
									 }
									}
									
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
								    tt.html(content);
							});

								    obj[EV](CK,function (){
								      var tt= $(this).html();
								      var  ttt=$(this)[TX]();
								    	base.AnalyzingWord(tt,function (content){
								    			//alert(content);
								    	});
								    });
          }//ParseText结束
	
	
	
	//设置发布统计里的各个小模块功能
	function  ReleaseSetOptions( This, refresh,data){
		//t2,3class 样式
				  	This[FD]('.t2')[EH](function (){
		  		    var t2TXT=$(this)[TX]();
		  	        var last='<a style="cursor:pointer"> '+t2TXT+'</a>';
		  	        $(this).html(last);
		  	});
		  	
		  			 This[FD]('.t3')[EH](function (){
		  		    var t3TXT=$(this)[TX]();
		  	        var last='<a style="cursor:pointer"> '+t3TXT+'</a>';
		  	        $(this).html(last);
		  	});
	
	
	    	//判断如果单个全部选中的各种情况
	       CheckBoxOptions( This, refresh,data);
		//判断如果单个全部选中的各种情况结束
		
		//发布统计搜索按钮事件
		$('#release_count_searchBtn',$key)[EV](CK,function(){
		 //查询发布统计
			//判断时间起始不能大于结束时间
	    	  var tt	= base.IsRegExpDateTime(releaseStartTime,releaseEndTime,TE);
	    	  if(tt==TE){
					ReleaseCount();
	    	  }
		});
		
		//发布统计导出按钮事件
		$('#release_count_importBtn',$key)[EV](CK,function(){
		   alert("发布统计导出");
		   //++++++++++++++
		});
		
		// 点击t2,t3触发事件  _releasecount
		 $('.t2',$('#_releasecount',This))[FD]("a")[EV](CK,function (){
		     examineManagerTitleBar.hide();
		     weiboReleaseLoadlist.hide();
		     navigationBars.Empty()
			.Add(key, 'ToolBar', '协同办公')
    		.Add(key, 'ToolBar', '考核管理')
    		.Add(key, 'RelaseSumbitNumListReWeiBo', '微博详情')[oL]();
    		var cuid=$(this)[PT]()[PT]()[FD]('td').eq(0)[FD]('input').val();
    		comtorsToTellMeTemp=cuid;
    		dealwithTemps=0;
			RelaseSumbitNumList(0,cuid);	    		
		 });//t2结束
		
		  $('.t3',$('#_releasecount',This))[FD]("a")[EV](CK,function (){
		  	 examineManagerTitleBar.hide();
		     weiboReleaseLoadlist.hide();
		     navigationBars.Empty()
			.Add(key, 'ToolBar', '协同办公')
    		.Add(key, 'ToolBar', '考核管理')
    		.Add(key, 'RelaseSumbitNumListReWeiBo', '微博详情')[oL]();
    		 var cuid=$(this)[PT]()[PT]()[FD]('td').eq(0)[FD]('input').val();
    		comtorsToTellMeTemp=cuid;
    		dealwithTemps=3;//  dealwithTemps记录微博处理中的 区别采纳 3 和提交0 的 
			RelaseSumbitNumList(3,cuid);	
    		 
		 });
	}
	
	
	 	  //微博内容统计数据中table中td列的点击事件
		function  ContentSetOptions( This, refresh,data){
		/*
		//tid号列 隐藏
		var   ths=$('#_contentcount',This)[FD]('th');
		ths.eq(ths.length-1).hide();
		//t7 tid 号隐藏
		$('.t7',This)[EH](function (){
					$(this).hide();
		});
		*/
		
		  contentSearchBtn[EV](CK,function(){
		  		   	//判断时间起始不能大于结束时间
	    	  var tt	= base.IsRegExpDateTime(contentStartTime,contentEndTime,TE);
	    	  if(tt==TE){
					ContentCount();	    
	    	  }
	      });
	    
	      
	        	//还有导出功能 （内容统计导出）
		  	//contentImportBtn[EV](CK,function(){
		  		$('#content_count_import',$key)[EV](CK,function(){
		  		alert("微博内容统计导出");
		  		//++++++++++
		  	});  	
		  	
		  	//多选框单个控制总开关
		  	CheckBoxOptions( This, refresh,data);
		  	
		  	//t1 class 需要此列隐藏
			This[FD]('tr').eq(1)[FD]('th').eq(1).hide();
			This[FD]('.t1').hide();
		  	
		  	
		  	//t2,t4 ,5,6变成下拉线的小手指
		  	This[FD]('.t2')[EH](function (){
		  	        //内容转义字符
				 	var index= $('.t2',This)[ID]($(this));
				 	var t2TXT= base.htmlEncodeByRegExp(data.rows[index].content);
		  		    //var t2TXT=$(this)[TX]();
		  		   // $(this).css('cursor','');
		  	        var last='<a style="cursor:pointer"> '+t2TXT+'</a>';
		  	        $(this).html(last);
		  	});
		  	
		  	
		  	This[FD]('.t4')[EH](function (){
		  	    var t4TXT=$(this)[TX]();
		  	    $(this).css('cursor','');
		  	   var last='<a style="cursor:pointer"> '+t4TXT+'</a>';
		  	   $(this).html(last);
		  	});
		  	
		  	This[FD]('.t5')[EH](function  (){
		  		var t5TXT=$(this)[TX]();
		  		$(this).css('cursor','');
		  		var last='<a style="cursor:pointer"> '+t5TXT+'</a>';
		  		$(this).html(last);
		  	});
		  	
		  	This[FD]('.t6')[EH](function (){
		  	  var t6TXT=$(this)[TX]();
		  	  $(this).css('cursor','');
		  		var last='<a style="cursor:pointer"> '+t6TXT+'</a>';
		  		$(this).html(last);
		  	});
	      
	      //td中可以点击的列事件  开始
	      //t2  class 的列
	       $('.t2',$('#_contentcount',This))[FD]("a")[EV](CK,function(){
	       // tid 
	        var contentid=  $[TM]($(this)[PT]().prev()[TX]());
	        var  platform=examineSelect1[AT]('value').split(',')[1];
				   $(BY)[AJ]({
    						     param: {action :'coordination.examineManager',tempList:'lookAllContent',contentID:contentid,platform:platform},
    		     				 success: function (data) {
									var bodyHTML='<div class="giveMe_do tongzhi_content"><div class="all_content">{0}</div><!--<div id="examineMange_ImgID">{1}</div>--><div class="push_ok">'+
									'<!--<div class="in_MsetCno" id="examManage_AllContent_Cancel">取&nbsp;&nbsp;消</div>--><div class="in_MsetCok" id="examManage_AllContent_OK">关&nbsp;&nbsp;闭</div></div><div class="com_clear"></div></div>';    		
    								//如果输入html标签的需要转义下比如&lt;这种<号
    							    data.content=base.htmlEncodeByRegExp(data.content);
    							    var  imgHTML='';
    							    var  imgTemp='<img width="50" height="50" src="{0}">'
    								if(data.imageUrls.length!=0){
    								    $[EH](data.imageUrls,function(i,item){
    								    	imgHTML+=$[FO](imgTemp,item);
    								    });
    								}
    							 bodyHTML=$[FO](bodyHTML,data.content);
    		 $[WW]({
		        css: { "width": "450px", "height": "auto"},
		        //event: 'no',
		        //title: '序号'+ contentid+'内容详情',
		        title:'内容详情',
		        content: bodyHTML,
		        id: '_examManage_allContent',
		        onLoad: function(div) {
				        base.AnalyzingImage(data.content,function(ExamManager_Content){	
				      	$('.all_content',div).html(ExamManager_Content+'<div class="com_clear"></div>'+imgHTML);
				      	});
				      	//确定按钮
				      	$('#examManage_AllContent_OK',div)[EV](CK,function(){
				      		CloseWindow(div);
				      	});
				      	
				      	//取消按钮
				      	$('#examManage_AllContent_Cancel',div)[EV](CK,function(){
				      		CloseWindow(div);
				      	});
		      		},//onLoad结束
		        onClose:function (div){
		      		
		      		}
    			});   
    			 		     				 
    						}//sucess结束
    				});	
	       });
	      
	      //t4 class 的列
			  $('.t4',$('#_contentcount',This))[FD]("a")[EV](CK,function (){
			  	       //h1页签转化工具栏隐藏
			  	       examineManagerTitleBar.hide();
			  	       	navigationBars.Empty()
						.Add(key, 'ToolBar', '协同办公')
			    		.Add(key, 'ToolBar', '考核管理')
			    		.Add(key, 'ContentRepostsWeiBo', '微博详情')[oL]();
    					weiboContentLoadlist.hide();
					    var  tid=$[TM]($(this)[PT]()[PT]()[FD]('.t1')[TX]());
						//var  platfors=$(this)[PT]()[PT]()[FD]('td').eq(0)[FD]('input').val();
						var   thisNum=$[TM]($(this)[TX]());
						comtorsToTellMeTemp=tid;
          				dealwithTemps=thisNum;
    					//微博内容统计中点击转发数链接跳转页面
    					FormatNumSkip(tid,thisNum);
			  });
			 
			 //t5 class的列
			 $('.t5',$('#_contentcount',This))[FD]('a')[EV](CK,function (){
			 	  // alert("考核管理的评论数");
			 	  //h1页签转化工具栏隐藏
			  	  examineManagerTitleBar.hide();
			 	  navigationBars.Empty()
			 	  .Add(key, 'ToolBar', '协同办公')
    			  .Add(key, 'ToolBar', '考核管理')
    		      .Add(key, 'ContentCommentsWeiBo', '微博详情')[oL]();
			 	  weiboContentLoadlist.hide();
			 	  var  tid=$[TM]($(this)[PT]()[PT]()[FD]('.t1')[TX]());
			 	  var   thisNum=$[TM]($(this)[TX]());
				  comtorsToTellMeTemp=tid;
				  dealwithTemps=thisNum;
				  //微博内容统计中点击评论数链接跳转页面			 	  
			 	  ReviewNumSkip(tid,thisNum);
			 });
			 
			  //t6  class 的列
			  $('.t6',$('#_contentcount',This))[FD]("a")[EV](CK,function (){
			            //++++++++++++++向后台发送什么参数
			            //h1页签转化工具栏隐藏
			  	       examineManagerTitleBar.hide();
    					navigationBars.Empty()
    					.Add(key, 'ToolBar', '协同办公')
    			        .Add(key, 'ToolBar', '考核管理')
    					.Add(key, 'ToolBarWeiBo', '网评员转发')[oL]();	  
			  			weiboContentLoadlist.hide();
			  			//4个切换页签中点击转发机构跳转页面
			  			FormatNumOrganizationSkip($(this));
			  });
			   //td中可以点击的列事件  结束
		}
		
		//微博内容统计评论数跳转页面
		function ReviewNumSkip(tid,thisNum){
				var  uid=examineSelect1[AT]('value').split(',')[0];
				var  stime=contentStartTime.val();
				var etime=contentEndTime.val();
				var  platfors=examineSelect1[AT]('value').split(',')[1];
	    		ChangePageShow.show();
	    		ChangePageShow.html('');
		 	    ChangePageShow[AP]('<div  id="contentsCommentCount_list" style="min-height:50px"></div>');
	      	    var ChangePageShow2=$('#contentsCommentCount_list',ChangePageShow);
    	        var  listDiv=ChangePageShow2;
		 	    //listDiv=ChangePageShow;
		 	 listDiv.add(loadlistPager).html('');
   			 listDiv[LS]({
	            width: 1000,
	            id: 'examineManager_contents_comments',
	            pager: loadlistPager,
	            isOption: FE, //是否显示全选、反选
	            param: {action :'coordination.examineManager',tempList:'RepostsPageNum',  starttime:stime,endtime:etime,uid:uid ,platform:platfors
	            ,tid:tid,thisNum:thisNum},
	        		  templates: [{html: '<div class="cont_list"><div class="cont_photo"><img src="" class="accountImg" width="50" height="50"><div class="photo_allInfor com_none acInfo"></div></div><div class="pl_countLsit"><div class="taikMe_List"><div class="talkMe_count"><div>   <!--<span class="com_none" id="huifu">回复</span>  -->   <span class="Fname_color accountName"></span><span   id="exmanageUpContent_{0}">' }, 
	                      	 	  {html: '{0}</span></div>'}, //上面  评论内容
	                      	      {html: '<div class="pl_mar"><span class="pl_mar pl_wdwb"></span><span class="Fname_color Fname_nomal" id="exmangeReviews_{0}">'}, 
	                              {html: '{0}</span></div>'}, //微博内容
	                              {html: '<p class="pl_mar"><span>{0}</span>'}, //评论时间
	                      	      {html: '<span>来源</span><span>{0}</span>'},//来源
								  {html: base.FormatPmi('{pmi:wdpl_commentsData_1}')},  //要回复的评论id <span class="pl_do" id="review_id{0}">回复</span>

								 // {html: '<span class="pl_do" id="sg_id{0}">|</span>'},
								  
								 {html: base.FormatPmi('{pmi:wdpl_deleteReviewData_1}')},//要删除的评论id <span class="pl_do" id="delete_id{0}">删除</span>
								  
								  {html: '</p><div class="com_clear"></div><div class="relay myreview_reply com_none" id="relay_{0}"><div class="ralay_middle"><br />'}, //评论DIV
								  {html: '<div class="com_clear"></div><textarea class="relay_count" id="reviewtextarea_{0}"></textarea>'}, //评论内容
								  {html: '<div class="send_do"><div class="font-tip rWord{0}"></div>'}, 
								  {html: '<div class="send_left"><span class="s_pic s_bqPc" id="bqingbtn_{0}">表情'}, //评论时间  <div class="send_do"><div class="send_left">
								  {html: '<div class="bq_select com_none  panel" id="swbqingpanel_{0}"></div></span>'}, //表情
								  {html: '<input type="checkbox" class="relay_input" id="relaycheck_{0}" /><label for="relay_sel">&nbsp;同时转发到我的微博</label></div><div class="send_right">'}, //是否选中
								  {html: '<div class="send_over send_back" id="replyBut_{0}">评&nbsp;&nbsp;论</div></div><div class="com_clear"></div></div></div></div></div></div></div> <div class="com_clear"></div></div> '}], //评论按钮
	
	            columns: [   { field: 'dealwithCommentsID' },
	            				  { field: 'content'},
	               				  { field: 'dealwithCommentsID' },
	                              { field: 'posttext'},
	                              { field: 'createtime', maxLength: 200},
	                              { field: 'source' },
	                              { field: 'dealwithCommentsID' },
	                             // { field: 'dealwithCommentsID' },
	                              { field: 'dealwithCommentsID' },
	                              { field: 'dealwithCommentsID' },
	                              { field: 'dealwithCommentsID' },
	                              { field: 'dealwithCommentsID' },
	                              { field: 'dealwithCommentsID' },
	                              { field: 'dealwithCommentsID' },
	                              { field: 'dealwithCommentsID' },
	                              { field: 'dealwithCommentsID'}],
	                         
	            onComplete: function (This, refresh, data) {
	       	    if(data.rows.length==0){
							base.not(This);
						}else{
	          
	            	//设置账号头像、昵称
					var accountImg = $('.accountImg', This),
	            		   accountName = $('.accountName', This),
	            		   pl_wdwb = $('.pl_wdwb',This);
					accountImg[EH](function (i, item) {
	        		   	      var $this = $(this),
	        		   		   index = accountImg[ID]($this),
	        		   		   dataRow = data.rows[index],
	        		   		   account = dataRow.account;
	   		   			       $this[AT]('src', account.imgurl);
	   		   				//$('#review_id'+data.rows[index].dealwithCommentsID,$key).show();
	   		   				//$('#huifu',$key).hide();
	   		   				accountName.eq(i).text(account.name);
	   		   				pl_wdwb.eq(i).text('评论我的微博：');
					});

					//显示用户信息
	            	var accountInfo = $('.acInfo', This);
	            	accountInfo[EH](function() {
						var $this = $(this),
							   index = accountInfo[ID]($this),
							   accountRow = data.rows[index].account;
							   base.AccountTip($this, accountRow,_enum.ModuleType.ExamineManager,FE);
					});
					
						$[EH](data.rows,function(i,item){
								var id = item.dealwithCommentsID,
								 	   dataRow = data.rows[i];
			        		   	var content = base.htmlEncodeByRegExp(dataRow.posttext);	
			        		  	content=ParseCommentsText(content);
			        		  	
			        		   //$("#exmangeReviews_"+id).html(content);
			        		   $("#exmangeReviews_"+id).html( 	'<a href="'+item.comtorsURL+ '" target="_blank">'    +content +'</a>') ;
								var  upcontent=base.htmlEncodeByRegExp(dataRow.content);	
								upcontent=ParseCommentsText(upcontent);
								$('#exmanageUpContent_'+id,This).html(upcontent);
						});
					
						var commt_power = cuser.Templates['wdpl_commentsData_1'];		//取得回复权限
						var delete_power = cuser.Templates['wdpl_deleteReviewData_1'];	//取得删除权限
					  
					
					//判断回复权限
				 if( commt_power != UN && commt_power != ""){
					//循环回复 删除
					$[EH](data.rows,function(i,item){
						var id = item.dealwithCommentsID,
							dataRow = data.rows[i],
	        		   		account = dataRow.account,
	        		   		weibo_id = dataRow.sourceID;
	        		   		
	        		   // 在140内 点击评论 可以在点击了 
					  $('#reviewtextarea_'+id,This).focus(function (){
					  		 isOnClickReplyBut=0;
					  });
	        		   		
						//点击回复 打开回复面板
						$('#review_id'+id,This)[EV](CK,function(){
						
								var flag= $('#relay_'+id,This).css("display");//获取样式是否隐藏
								
								if(flag == "none"){
									 $('#relay_'+id,This).show();
									 $('#reviewtextarea_'+id).val("回复@"+account.name+"：");
									//显示表情
									 //$('#swbqingpanel_'+id,$key).show();
									 $('.rWord'+id,This).html('您还可以输入<span class="font_num" >140</span>个字');
									 base.LimitWord($('#reviewtextarea_'+id,This), $('.rWord'+id,This), $('#replyBut_'+id,This));
									 base.FacePicker($('#bqingbtn_'+id,This), $('#swbqingpanel_'+id,This), $('#reviewtextarea_'+id,This), $('.panel',This));
									
									 $('#replyBut_'+id,This)[EV](CK,function(){
									if(isOnClickReplyBut==0){
										 	isOnClickReplyBut=1;
									  if( $('#replyBut_'+id,This)[AT]('class')!='send_back send_fail'){	  
									 	var textarea = $('#reviewtextarea_'+id).val(),	  //评论内容
									 		state =  $("#relaycheck_"+id).prop("checked");//是否选中状态
									 		if(textarea == "回复@"+account.name+"：" || textarea == ''){
									 			$[MB]({
													content: '写点东西吧，评论内容不能为空哦。',
													type: 1,
													onAffirm: function (boolen) { 
															
													}
												}); 
												isOnClickReplyBut=0;
									 		}else{
									 			$(BY)[AJ]({
													param: {action: 'myTwitter.comments', tid: id,orginalid:weibo_id,textarea: textarea,state: state,
													ucode:$('#pulldownSelect',weiboContentLoadlist)[TX]() ,uid: uid  ,
													ucodename: $('#pulldownSelect',weiboContentLoadlist)[AT]('title')    },
													success: function(data){
															$('#relay_'+id,This).hide();
															$[MB]({ content: '评论成功!', type: 0,isAutoClose: TE });
															ReviewNumSkip(comtorsToTellMeTemp,dealwithTemps);
																if(data != '-1'){
																		$[MB]({ content: data, type: 2});
															}
															isOnClickReplyBut=0;
													}
												});
									 		}
									    } //不置灰的评论可以点击 结束
									}//isOnClickReplyBut不可 多次点击  1 不可以 0 可以
									 });  //评论按钮结束
									 
								}else{
									$('#relay_'+id,This).hide();
								}
						});
		    });
		} // 回复权限结束
						
							//判断删除权限
				if( delete_power != UN && delete_power != ""){
							//循环回复 删除
							$[EH](data.rows,function(i,item){
								var id = item.dealwithCommentsID,
									dataRow = data.rows[i],
			        		   		account = dataRow.account;
						//点击删除 删除评论任务
						$('#delete_id'+id,This)[EV](CK,function(){
						
								$[MB]({
									  	content: '您确定要删除吗？',
										type: 4,
										onAffirm: function (boolen) { 
											if(boolen == true){
												$(BY)[AJ]({
														param: {action: 'myTwitter.deleteReview', tid: id,type: 0,platform: 1,uid:uid},
														success: function(data){
														ReviewNumSkip(comtorsToTellMeTemp,dealwithTemps);
																if(data == '-1'){
																	$[MB]({ content: '删除成功!', type: 0,isAutoClose: TE });
																}else{
																	$[MB]({ content: data, type: 2});
																}
														} //success
												});
											}
										}
								}); 			
						});
								});// each 结束
						}	// 判断删除权限结束
					
				} //else 结束 有数据	
	            }  //onComplate 结束
	        }); // listdiv 加载结束
		
		}
		
		
		//解析内容中 带有[微笑]  
		function  ParseCommentsText (content){
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
		
		
				//4个切换页签中点击转发机构跳转页面
		function 	FormatNumOrganizationSkip(This){
			 ChangePageShow.show();
			 ChangePageShow.add(loadlistPager).html('');
			 //nid 就是tid 微博id
			 var nid=$[TM](This[PT]()[PT]()[FD]('.t1')[TX]());
			 ChangePageShow[TB]({
             width: 1000,
             trHeight: 24, //表格默认高
             pager: loadlistPager,
             pageSize: 15,//显示条数
             isPager: TE, //是否分页
             id:'_examinemanager_organ',
             rows: 'rows',
             param: {action :'coordination.examineManager',tempList:'forwardOrgNum' ,NID:nid},
			 columns: [{ caption: '序号', field: '', type: 'number',width: '5%' },
			  { caption: '微博类型', field: 'platform', type: 'int', width: '20%' },
                       { caption: '帐号昵称', field: 'orgName', type: 'string', width: '40%' }],
                //加载表格成功事件
            onComplete: function ( This, refresh,data) {
            		 if(data.rows.length==0){
							base.not(This);
							return FE;
						}
						
					//开始解析	
				$('.t1',This)[EH](function (){
				      var temp=  ParseEnum(parseInt($[TM]($(this)[TX]())));
				      $(this)[TX](' '+temp);
				});
				
                } //onComplete结束
		});
			  		}//4个切换页签中点击转发机构跳转页面结束
		
		
			//4个页签中点击转发链接跳转页面
    		function  FormatNumSkip(tid,thisNum){
	    		var  formatUID=examineSelect1[AT]('value').split(',')[0];
				var  stime=contentStartTime.val();
				var etime=contentEndTime.val();
				var  platfors=examineSelect1[AT]('value').split(',')[1];
				ChangePageShow.show();
	    		ChangePageShow.html('');
				ChangePageShow[AP]('<div  id="contentsRepostCount_list" style="min-height:50px"></div>');
	      	    var ChangePageShow2=$('#contentsRepostCount_list',ChangePageShow);
	    		ChangePageShow2.add(loadlistPager).html('');
    			base.DataBind({
				panel: ChangePageShow2, 
				pager: loadlistPager,
				id: '_examManager_formatNumSkip', 
			    param: {action :'coordination.examineManager',tempList:'formatNumPage',uid:formatUID,starttime:stime,
			    endtime:etime,tid:tid,platform:platfors,thisNum:thisNum},
			    //template: base.FormatPmi('{pmi:yqzs_collect_1}{pmi:wdwb_adwdrepost_1}{pmi:wdwb_adwdcomment_1}'),
				template: base.FormatPmi('{pmi:yqzs_collect_1}{pmi:wdsy_repost_1}{pmi:wdsy_comment_1}'),
				isComment:FE,
				moduleType: _enum.ModuleType.talkMe,
				isRepostCommonCount:TE,
				onComplete: function (This, refresh, data) { 
					$('.com_fRight.subBtnPanel',This)[EH](function (){
						 $(this)[RM]();					 
					});
				},
				onSend:function(btn,txt, isChecked, accountCollection,  postID, url, type, twitterID, closeBtn,name){
					 //评论或者转发触发的事件
				var param = {};
				//	param.action = type == 'repost'? 'talkMe.talkMeRepost': 'talkMe.talkMeComment';
			   // param.queryConditions=type == 'repost'? 'talkMeRepost': 'talkMeComment';
				param.action = type == 'repost'? 'myTwitter.saveReport': 'myTwitter.saveComment';				
				
				param.isChecked = isChecked;
				param.content = txt;
					if (type == 'repost' ) {
					//不填转发内容的时候，默认值
					if ($[TM](txt) == '') {
					//if (txt == '') {
						txt = '转发微博';
					}
				} else {
				     if ($[TM](txt) == '') {  
					//if (txt == '') {
						$[MB]({
							content: '评论内容不能为空', 
							type: 2,
							isAutoClose: TE
						});
						btn[AT]('isclick',TE);
						return;
					}
				}				
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
				
				}  //onSend 结束
			});
	
    	}//4个页签结束
    	
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
			
			
			  //替代转发图片的$ 符号
               function  RelaceImage (obj){
                var imghtm='<img src="{0}" width="100" height="100" /> '; 
                var totalhtm='';
                for(var i=0;i< obj.length;i++){
					  obj[i]=obj[i][RP]('$',userWeiboImagePath);
				      totalhtm+=$[FO](imghtm, obj[i]);          
                }
          		return  totalhtm   ;
          }
          
          /*
          function  RelaceImage (obj){
                var imghtm='<img src="{0}" width="50" height="50" /> ';
                var arrs= obj.split(',');
                var totalhtm='';
                for(var i=0;i<arrs.length;i++){
					  arrs[i]=arrs[i][RP]('$',userWeiboImagePath);
				      totalhtm+=$[FO](imghtm, arrs[i]);          
                }  //for
          		return  totalhtm   ;
          }
          */
          
          //替代转发帖子的text内容方法
          function   RelaceRepost( text){
          		   var	txt= base.htmlEncodeByRegExp(text);
          	        //解析存在有表情的转化成图片
					base.AnalyzingWord(txt,function (data){
					txt=data;
					//解析如果存在URL地址的变样式
					 //txt=	base.AnalyzingUrl(txt);
					//解析有@+名称的变样式
					base.AnalyzingTwitterName(txt,function (data){txt=data});
					//解析如果存在有图片的显示出来
					base.AnalyzingImage(txt,function (data){txt=data});
               });
          		return   txt;
          }
    	
	
    return {
    	IsLoad: FE,
        OnLoad: function (_ContentDiv) {
            var t = this;
            contentDiv = _ContentDiv;
            $('<div />').load('modules/coordination/coordination.examineManager.htm', function () {
                base = context.Base;
            	contentDiv[AP](base.FormatPmi($(this).html()));
            	//contentDiv[AP]($(this).html());
            	cuser=context.CurrentUser;
            	t.IsLoad = TE;
                $key = $('#' + key);
                 $changespan =$('.zhao_change.span_title',$key);
                navigationBars=context.NavigationBars;
                weibo_content_count=$('#weibo_content_count',$key);
               contentStartTime=$('#content_count_starttime',$key);
		       contentEndTime=$('#content_count_endtime',$key);
                releaseStartTime=$('#release_count_starttime',$key);
                releaseEndTime=$('#release_count_endtime',$key);
                dealwithStartTime=$('#dealwith_count_starttime',$key);
                dealwithEndTime=$('#dealwith_count_endtime',$key);
                commentatorsStartTime=$('#weibo_commentators_starttime',$key);
                commentatorsEndTime=$('#weibo_commentators_endtime',$key);
                examineSelect1=$('#examineSelect1',$key);
                examineSelect2=$('#examineSelect2',$key);
                //各个标签的按钮
                contentSearchBtn=$('#content_count_searchBtn',$key);
                //contentImportBtn=$('#content_count_import',$key);
                
                //改变页
                ChangePageShow=$('#examinemanager_ChangePageShow',$key);
                //协同办公上面的页签切换工具栏
                 examineManagerTitleBar =$('#examine_manager_titlebar',$key);
                //分页
                loadlistPager=$('#allPager_loadlist',$key);
                _enum = context.Enum;
		        FormatDiv=$('#coorZF_details',$key); //转发div
		        ReviewDiv=$('#coorPL_details',$key);//评论div
		        commentatorsDiv=$('#coorZFJG_details',$key);//网评员div
		         weiboContentLoadlist=$('#weiboContentLoadlist',$key);//微博内容统计DIV
		          weiboReleaseLoadlist=$('#weiboReleaseLoadlist',$key);//微博发布统计DIV
		          weiboDealwithLoadlist=$('#weiboDealwithLoadlist',$key);//微博处理统计DIV
		          weiboCommentatorsLoadlist=$('#weiboCommentatorsLoadlist',$key);//网评员统计DIV
                Init();
            });
            return t;
        },
        //显示模块方法
        Show:function() {
        	$key.show();
        	examineManagerTitleBar.show();
        	//examineManagerTitleBar[FD]('span:first')[TR](CK);
        	ChangePageShow.html('');
        	ChangePageShow.hide();
        	Init();
        },
        ToolBar:function (){
        	examineManagerTitleBar.show();
            ChangePageShow.html('');
        	ChangePageShow.hide();
        	InitTooBar(); 
        },
        ToolBarWeiBo:function(){
        
        },
        ToolBarWeiBoCommtors:function(){
			CommentatorsAccountNumberList();        
        },
        ComtorsToTellMe :function (){
        	CommentatorsRepostToMeNumberList(comtorsToTellMeTemp);
        },
        DealWithWeiBoComments:function(){
			DealWithCommentsHandle(dealwithTemps);
        },
        DealWithWeiBoRePosts:function(){
        		DealWithRePostHandle(dealwithTemps);
        },
        RelaseSumbitNumListReWeiBo:function(){
        		RelaseSumbitNumList(dealwithTemps,comtorsToTellMeTemp);
        },
        ContentRepostsWeiBo:function (){
        		FormatNumSkip(comtorsToTellMeTemp,dealwithTemps);
        },
        ContentCommentsWeiBo:function (){
				ReviewNumSkip(comtorsToTellMeTemp,dealwithTemps);        
        }
    };
});