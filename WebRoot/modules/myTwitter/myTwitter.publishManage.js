//发布管理
define(['portal'], function (context) {
    //定义变量
    var key = 'publishManage',
          $key, //当前的作用域
          navigationBars,//工具栏标签
          contentDiv,
          listDiv,
          cuser,
		  accountCollection,
          userImageIDs,
          pmStartTime,//开始时间
          pmEndTime,//结束时间
          pmSumbiter,//提交人下拉框
          pmWeiboTypes,//筛选微博类型
          pmSearchBtn,//搜索条件按钮
          pageFenYe,
          base,
          mediator,
		  selectColl=[],//获取提交人的信息集合
          titleSpan;
          
          function Init(){
            NavigationInit();
            SubmitersList();
          }
          //非load进入，调用show方法的
          function   AlearyDraw(){
		 	NavigationInit();
            TitleClick();
		}
          
          //获取提交人信息集合事件
          function   SubmitersList(){
          	$(BY)[AJ]({
						param: {action: 'myTwitter.myPostList',  queryConditions:'queryBySumbitersList'  },
						success: function(data) {
						selectColl.push({text:'全部',value:-1});
							   $[EH](data,function(i,item){
							   	  var   every={};
							   	 // every.text=item.userName;
							   	 var tempName=item.nickName;
							   	 if(tempName.length>6){
							   	  every.text=tempName.substr(0,6);
							   	 }else{
							   	  every.text=tempName;
							   	 }
							   	  every.value=item.id;
							   	  selectColl.push(every);
							   });
					 pmSumbiter.input({
		     		id:"pmSumiter_list",
		     		collection:selectColl
		     	});
				TitleClick();
						}//success
					});
          
          }
          

          // 下拉框中赋值 还有时间赋值  
          function   DownList(){
          		pmWeiboTypes.input({
	     		id:"pmWeiboTypes_list",
	     		collection:[{text:'原创',value:0},
	     							  {text:'转发',value:1},
	     							  {text:'评论',value:2}]
	     	});
          	pmEndTime.datePicker();
	     	var tempTimeStart=$.addDate('d', -6, pmEndTime.val()).Format('YYYY-MM-DD');
	        pmStartTime.datePicker().val(tempTimeStart);
          }
          
          
               //导航条初始化方法,
			function NavigationInit () {
		    	navigationBars.Empty()
			.Add(key, 'ToolBar', '我的微博')
    		.Add(key, 'ToolBar', '发布管理')[oL]();
			}
	
          function TitleClick(){
          	//绑定点击事件
          	titleSpan[EV](CK,function(){
          		var This = $(this),cid = This.attr("cid");
          		$(".zhao_change",$key).removeClass("zhao_back");
          		This.addClass("zhao_back");
          		DownList();
          		PublishManageAction();
          	}).eq(0)[TR](CK);
          }
          
          //分析文字的图片,表情文字等
          function   ParseText(obj,data){
          		/*
          	     obj[EH](function (){
									var  tt=$(this);
									var  all=tt.html();
									//给表情图片添加path路径
									all=all.replace(/\$/g,userWeiboImagePath);
									tt.html(all);

										//match(/(http:\/\/)?([A-Za-z0-9]+\.[A-Za-z0-9]+[\/=\?%\-&_~`@[\]\':+!]*([^<> \[\"\"])*)/gi);
										   	var  hrefs=tt.html().match(/(http:\/\/)?([A-Za-z0-9]+\.[A-Za-z0-9]+[\/=\?%\-&_~`@\':+!]*([^<> \[\"\"])*)/gi);
										   	var temp='';
								     	if(hrefs!=null){
								     		  $[EH](hrefs,function(i,item){
								     		 if(item.toLowerCase().indexOf(".jpg")<0&&item.toLowerCase().indexOf(".png")<0&&item.toLowerCase().indexOf(".bmp")<0
								     		 &&item.toLowerCase().indexOf(".gif")<0 &&item.toLowerCase().indexOf(".jpeg")<0 &&item.toLowerCase().indexOf(".psd")<0 ){
								   		  temp+=tt.html()[RP](item,'<a   target="_blank" class="manage_link"  href="'+item+'">'+item+'</a>');
								   		  }
								   		  });
								     	}
												if(temp!=""){
													tt.html(temp);
												}
									
								   base.AnalyzingImage($(this).html(),  function (content){
								 var  array=  content.match(/@[a-zA-Z0-9_\u4e00-\u9fa5]+( |:|：)/gi);
								   		if(array!=null){
								   		  $[EH](array,function(i,item){
								   		    content= 	content[RP](item,'<span class="Fname_color">'+item+'</span>');
								   		  });
								   		}
								   		  tt.html(content);		 
								   });	
							});
							*/
							//-----结束

							//调用公用方法解析文本
							obj[EH](function (i){
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

								//==============
								    obj[EV](CK,function (){
								      var tt= $(this).html();
								      var  ttt=$(this)[TX]();
								    	base.AnalyzingWord(tt,function (content){
								    		//	alert(content);
								    	});
								    });
          }//ParseText结束
          
           //当前用户操作的行为
           //aType 操作的类型， 删除为0  审核 1 修改2
           //ids 帖子的id    auditorID为审核人ID， text为文本信息   images为图片的整个字符串连接的结果
           //div 为当前的div对象   send 是否立即发送 立即为0 ;  定时为一个时间的字符串
           //uid  微博的uid      platform 微博类型 1 新浪 2腾讯  5人民
           function    ExecuteAction(aType,ids,auditorID,text,images,div,send,uid,platform){
               var  cont="";
               var  que="";
               var confirmText="是";
               var cancelText="否";
           	 if(aType==0){
           	 cont='您确定要删除吗？';
           	 que='deleteID';
           	 }else if(aType==1){
           	 cont="您对此帖子是否通过审核？";
           	 confirmText="通过";
           	 cancelText="不通过";
           	  que='checkLook';
           	 }else  if(aType==2){
           	 que="save";
           	 }
             	if(aType!=2){
           			$[MB]({
				  	content: cont,
					type: 4,
					confirmText:  confirmText,
                    cancelText: cancelText,
					onAffirm: function (state) { 
						if(state == true){
						 var actOne='';
						 if(que=='deleteID'){
						 	actOne='myTwitter.myPostList.del';
						 	}else {
						 	actOne='myTwitter.myPostList.check';
						 }
							$(BY)[AJ]({
									param: {action: actOne,queryConditions:que ,arrayID:ids ,approvedStatue:2,auID:auditorID,uid:uid,platform:platform ,sendTime:send},//,text:text
									success: function(data){
										  //alert(data);
										  $('#'+ids,$key)[RM]();
										  //这个表示 自己是提交人 也是审核人
										  if(actOne=='myTwitter.myPostList.check'&& parseInt(data)==1){
										    mediator.AddMessageCount();
										  }
										  PublishManageAction();
									} //sucess
							});
						} //state ==true 结束
						
						else {
						//主要审核不通过调用的
						if(aType==1){
										$(BY)[AJ]({
									param: {action: 'myTwitter.myPostList.check',queryConditions:que ,arrayID:ids, approvedStatue:3,auID:auditorID 
									,uid:uid,platform:platform,sendTime:send
									},
									success: function(data){
										  //alert(data);
										  $('#'+ids,$key)[RM]();
										  if(parseInt(data)==1){
												mediator.AddMessageCount();										  
										  }
										  PublishManageAction();
									}
							});
							}//if结束
						}   //state ==false 结束
						
					}  //onAffirm结束
					
			});  //MB事件结束
			
			} // aType!=2 情况结束
			
			//aType=2情况  修改
			else {
          			$(BY)[AJ]({
		    		param: {action: 'myTwitter.myPostList.modify',queryConditions:que, tid:ids,authorID:auditorID,
		    		 Text:text, Images:images ,SendTime:send},
		    		success: function (data) {
						//alert(data);
						div[RM]();
      			   		$[RM]({ obj: '#' + JLY });
      			   	    $('#'+ids,$key)[RM]();
      			   	    
      			   	    if(data.indexOf('_')>0){
							     var  shuzus=data.split('_');
							     if(shuzus[1]=='success'){
							        //提示发布成功
						            $[MB]({ content: '修改发布成功!', type: 0,isAutoClose: TE });
							     }else{
							     //提示发布失败
								$[MB]({ content: '修改发布失败,可能帖子已经删除了!', type:2,isAutoClose: TE });
							     } 
							    mediator.AddMessageCount();     	
      			   	    }else{
      			   	    	if(data='success'){
      			   	         //提示发布成功
						    $[MB]({ content: '修改发布成功!', type: 0,isAutoClose: TE });
      			   	    	}else{
      			   	    		//提示发布失败
								$[MB]({ content: '修改发布失败,可能帖子已经删除了!', type: 2,isAutoClose: TE });
      			   	    	}
      			   	    }
      			   	          PublishManageAction();
		 			   		}
		    		});//ajax结束
			}//aType=2情况结束
			
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
          
          function PublishManageAction(){
       		 var stats= parseInt($('.zhao_change.span_title.zhao_back',$key)[AT]('cid'));
       		 var stime=pmStartTime.val();
       		 var etime =pmEndTime.val();
       		 var selOne=pmWeiboTypes[AT]('value');
       		 var  selTwo=pmSumbiter[AT]('value');
       		listDiv.add(pageFenYe).html('');
          	listDiv.list({
          		 //url:'',
          		 //methodName:'',
          		 width:998,
          		// id:'',
          		 rows: 'rows', 
          		 pageSize:10,
          		 pager:pageFenYe ,
          		 param:{action:'myTwitter.myPostList',queryConditions:'queryListAll',selectStatues: stats
          		 ,  stime:stime,etime:etime,selOne:selOne,selTwo:selTwo
          		 },
          		 templates:[{html:'<div class="cont_list"  id="{0}"><h2>'},
          		 			//{html:'<span >发布时间：</span><span>{0}</span>'},
          		 			{html:'<span >{0}</span>'},//发布---时间
          		 			{html:'<span class="span_marL">发布账号：</span><div class="com_fLeft send_style send_cont1">{0}   '},
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
          		 	// 操作作业画出 
          		 		  var operate=$('.cont_doright',$key);
          		 		  var talkMeContent=$('.yuanc_title',This);
          		 		  	//控制+ -按钮
								 var   amBtn=$('.send_showMore',This);
								HideOrShowAccount(amBtn);	    
							   //新添加的转发原帖(也就是阴影部分的) 的模版
							   var ShadowTempHtml=' <div class="talkMe_count"><div  class="deal_tipPic"></div> <h3 class="yuanc_title2 yuanc_title">   <span class="cmanage_color font-color">{0}</span>  <br /><span class="pm_repostTid">{1}</span> <br/> {2} </h3>  <p class="p_mar"><span>{3}  </span><span>  评论[{4}]</span><span>转发[{5}]</span></p></div>';
								
								//如果没有发布帐号的 ,需要把发布帐号不显示
								listDiv[FD]('.com_fLeft.send_style')[EH](function (){
										if($(this)[FD]('.pm_All')[LN]==0){
												$(this).prev()[TX]('');							 
										}								
								});
								
								
          		 		      if(stats==0){
          		 		      $('.send_selWay.pubManaer_conditionDIV',$key).show();
          		 		      pmWeiboTypes.show();
          		 		      pmSumbiter.hide();
          		 		      pmSumbiter[PT]().prev().hide();
          		 		      $('.news_type',$key).show();
								    //operate[AP]('<span class="puma_del">删除</span>'); 
								   //   var cuserDelTemp=cuser.Templates['wdwb_fbgldel_1'];
									var cuserDelTemp=UN;
									
									//有权限
									if(cuserDelTemp!=UN&&cuserDelTemp!=""){
										operate[AP](cuserDelTemp);
									}
								    $('.puma_del',$key)[EV](CK,function (){
								     var  temp=	$(this)[PT]()[PT]()[PT]()[AT]('id');
								  	//提交后台删除功能
									   ExecuteAction(0,temp);
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
								
								
								
								
								if(data.rows.length==0){
									 base.not(listDiv);
								}
								//我的发布中的搜索按钮事件
								pmSearchBtn[EV](CK,function (){
									 var  tt= base.IsRegExpDateTime(pmStartTime,pmEndTime ,TE);
						        	 if(tt==FE){
						        	 	return  FE;
						        	 }
						        	 PublishManageAction();
								});								
								
          		 		      }else if(stats==1){
          		 		      $('.send_selWay.pubManaer_conditionDIV',$key).hide();
          		 		        //operate[AP]('<span  class="puma_che">审核</span>');
          		 		          var cuserCheTemp=cuser.Templates['wdwb_fbglcheck_1'];
          		 		        if(cuserCheTemp!=UN&&cuserCheTemp!=""){
          		 		        operate[AP](cuserCheTemp);
          		 		        }
          		 		        
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
          		 		        
          		 		        
          		 		        
          		 		        	$('.puma_che',$key)[EV](CK,function(){
          		 		     	    var  aid=		$(this)[PT]().prev()[FD]('.com_none')[AT]('id');
          		 		        	 var  temp=	$(this)[PT]()[PT]()[PT]()[AT]('id');
          		 		        	 var  platform="";
          		 		        	  var  uid="";
          		 		        	 $(this)[PT]()[PT]()[PT]()[FD]('.pm_All')[EH](function(){
											  platform+=$(this)[AT]('pmplatform')+',';    
											  uid+=$(this)[AT]('pmuid')+','; 		 		        	 	
          		 		        	 });
          		 		        	 //取待我审核的里的发送时间
          		 		        	var sentTime=$(this)[PT]()[PT]()[PT]()[FD]('h2').eq(0)[FD]('span').eq(1)[TX](); 
          		 		        	//2014-09-23 17:24
          		 		        	var  dateShuzu=[], // 日期的数组
          		 		        	        timeShuzu=[];//时间的数组
          		 		        	var  checksTimes=sentTime.split(' ');
          		 		        	dateShuzu= checksTimes[0].split('-');
          		 		        	timeShuzu= checksTimes[1].split(':');
          		 		        	//sentTime 后面的_1 代表定时发布  _0代表立即发布

          		 		        	 // sentTime=CompareDateAndTime (dateShuzu,timeShuzu,sentTime);
          		 		        	//如果时间大于当前日期的画,(好几分钟，那么也是定时发布)
          		 		        	
								     ExecuteAction(1,temp,aid,UN,UN,UN,sentTime,uid,platform);
          		 		        	});
          		 		        	    if(data.rows.length==0){
									    base.not(listDiv);
								}
          		 		      }
          		 		      
          		 		      else if(stats==2){
          		 		      $('.send_selWay.pubManaer_conditionDIV',$key).hide();
          		 		             //operate[AP]('<span  class="puma_del">删除</span>');
          		 		      	      var cuserDelTemp=cuser.Templates['wdwb_fbgldel_1'];
										
									//有权限
									if(cuserDelTemp!=UN&&cuserDelTemp!=""){
										operate[AP](cuserDelTemp);
									}
          		 		      	  
          		 		      	    $('.puma_del',$key)[EV](CK,function (){
								     var  temp=	$(this)[PT]()[PT]()[PT]()[AT]('id');
								  	//提交后台删除功能
								   ExecuteAction(0,temp);
								    });
          		 		      		ParseText(talkMeContent,data);
          		 		      		if(data.rows.length==0){
									 base.not(listDiv);
								}
          		 		      		
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
          		 		      		
          		 		      		
          		 		      }else if(stats==3){
          		 		      $('.send_selWay.pubManaer_conditionDIV',$key).show();
          		 		      pmWeiboTypes.show();
          		 		      pmSumbiter.show();
          		 		      pmSumbiter[PT]().prev().show();
          		 		      $('.news_type',$key).show();
          		 		     //operate[AP]('<span  class="puma_del">删除</span>');
          		 		     	//      var cuserDelTemp=cuser.Templates['wdwb_fbgldel_1'];
									 var cuserDelTemp=UN;
									
									//有权限
									if(cuserDelTemp!=UN&&cuserDelTemp!=""){
										operate[AP](cuserDelTemp);
									}
          		 		     	
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
          		 		     		
          		 		     		
          		 		     	//删除事件功能
          		 		     		$('.puma_del',$key)[EV](CK,function (){
								     var  temp=	$(this)[PT]()[PT]()[PT]()[AT]('id');
								  	//提交后台删除功能
								   ExecuteAction(0,temp);
								});
									if(data.rows.length==0){
									 base.not(listDiv);
								}
								
								//所以已发布中的搜索按钮事件
								pmSearchBtn[EV](CK,function (){
									 var  tt= base.IsRegExpDateTime(pmStartTime,pmEndTime,TE);
						        	 if(tt==FE){
						        	 	return  FE;
						        	 }
						        	 PublishManageAction();
								});			
          		 		 }
          		 else {
          		 			$('.send_selWay.pubManaer_conditionDIV',$key).hide();
          		 		      //operate[AP]('<span  class="puma_moy">修改</span><span>|</span><span  class="puma_del">删除</span>');
          		 		            var cuserDelTemp=cuser.Templates['wdwb_fbgldel_1'];
          		 		            var cuserModTemp=cuser.Templates['wdwb_fbglmodify_1'];
									//有权限
									if(cuserDelTemp!=UN&&cuserDelTemp!=""&&cuserModTemp!=UN&&cuserModTemp!=''){
										operate[AP](cuserModTemp+'<span>|</span>'+cuserDelTemp);
									}else if(cuserDelTemp!=UN&&cuserDelTemp!=""&&(cuserModTemp==UN||cuserModTemp=='')){
										operate[AP](cuserDelTemp);
									}else if((cuserDelTemp==UN||cuserDelTemp=="")&&cuserModTemp!=UN&&cuserModTemp!=''){
									    operate[AP](cuserModTemp);
									}
          		 		      		
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
          		 		      		
          		 		      		
          		 		      		$('.puma_del',$key)[EV](CK,function (){
								     var  temp=	$(this)[PT]()[PT]()[PT]()[AT]('id');
								  	//提交后台删除功能
								   ExecuteAction(0,temp);
								    });
          		 		      					    
          		 		     //修改时间功能 逻辑
          		 		     $('.puma_moy',$key)[EV](CK,function(){
          		 		     //aType,  2  ids,auditorID,text,images
          		 		     	var pm_moy=$(this);
          		 		     	//var   AllHtml=$(this)[PT]()[PT]()[PT]()[FD]('.talkMe_count').html();
          		 		     	var indexmo=$('.puma_moy',$key)[ID]($(this));
          		 		     	var  AllHtml=data.rows[indexmo].text;
          		 		     	 
          		 		     	 var  ids=$(this)[PT]()[PT]()[PT]()[AT]('id');	
          		 		     	var  auid=$(this)[PT]().prev()[FD]('.com_none')[AT]('id');
          		 		     	//---------------------------------明天把text 。image给分开
								    	base.AnalyzingWord(AllHtml,function (content){
								    				AllHtml=content;
								    	});
								  
								//desHtml就是修改的内容在多行文本框中显示内容(包含img标签的) 
								//var desHtml=	AllHtml[RP](/<span class=\"Fname_color\">/gi,'')[RP](/<\/span>/gi,'')[RP](/<a .*?>/gi,'')[RP](/<\/a>/gi,'');
									var desHtml=	AllHtml[RP](/<span class=\"font-color\">/gi,'')[RP](/<\/span>/gi,'')[RP](/<a .*?>/gi,'')[RP](/<\/a>/gi,'');
									
								//解析完出去img图片的文本信息(包含表情的)  ==  只有文本信息
								var  text=desHtml[RP](/<img[\s\S]+?>/g,'');
								//数据库中存在的图片集合
								var iks=desHtml.match(/<img[\s\S]+?>/g);

								//修改弹出层 显示
				          		var  htm='<div class="giveMe_do  manage_change"><textarea class="manage_content" id="textareaInfoPM">'+text+'</textarea>'+
				          		'<div class="send_do"><div class="font-tip" id="wordCount">您还可以输入<span class="font_num">140</span>个字</div>'+
				          		
				          		
				          		'<div class="send_left"><span class="s_bqing"  id="selectFacePM"><span class="s_pic s_bqPc">表情</span><div class="bq_select  panels com_none"  id="facePanelPM"></div></span>'+
				          		//图片html按钮
				          		'<span class="s_pictures" ><span class="s_pic s_picPic" id="selectPic">图片</span><div id="picPanel" class="com_none panels">'+
				          		'<div class="s_smallTip"></div><div class="timing_close send_picClose" id="picColse"></div><div class="spic_list" id="picItem2">'+
				          		'<div id="fileQueue2"></div><input type="file" name="uploadify" id="uploadify2" /><span id="uploadifyTip2" style="color: red;"></span>'+
				          		'</div></div></span>'+
				          		
				          		'</div>  <div><div class="send_right">'+
				          		//'<div class="sel_num select_senNum" >'+
				          		//'<span class="s_Rsel" id="selectAccount">账号选择</span><div class="sel_numList  panels com_none" id="accountPanel"></div></div>'+
				          		
				          		'<div class="sel_num sel_time"><input type="radio"  name="set_time" class="timing_selTime" id="publishmg"    checked="checked"/>'+    
				          		'<label for="publishmg">立即发布</label></div>'+
				          		//'<label>立即发布</label></div>'+
				          		'<div class="sel_num sel_time set_timing"><input type="radio" name="set_time"  id="p_selectDatePicker"  class="timing_selTime"  />'+      
				          		'<label for="p_selectDatePicker">定时发布</label>'+   // id="selectDatePicker"
				          		//'<label  id="selectDatePicker" >定时发布</label>'+
				          		'<div  id="datePickerPanel" class="timing_time com_none panels"><div  class="timing_tip timing_tipPos"></div>'+
				          		'<div class="timing_close" id="datePickerClose"></div><div class="com_clear"></div><div class="timing_rili"><input type="text" class="rili_control"  id="datePickerInput"    readonly="readonly"/>'+
				          		'</div><div class="timing_sethours" id="timePickerDiv"></div><div class="tim_ok" id="timePickerOK">确&nbsp;&nbsp;定</div></div></div><div  id="releasePM"    class="send_over send_back send_fail">发&nbsp;&nbsp;布</div>'+
				          		'</div><div class="com_clear"></div></div>'+
				          		
				          		'<div  id="mypublicManager_Modifycontents" class="com_fRight"  style="display:none"><label>&nbsp;</label><font color="red" >*内容不能为空</font></div>'+
				          		'<div  id="mypublicManager_ModifycheckAccount" class="com_fRight"  style="display:none"><label>&nbsp;</label><font color="red" >*此微博已经删除帐号不能发布</font></div>'+
				          		'<div  id="mypublicManager_ModifyPreTimes" class="com_fRight"  style="display:none"><label>&nbsp;</label><font color="red" >*定时发布时间不能小于当前时间</font></div>'+
				          		
				          		'</div>';
				          		var currDIV;
				          		$[WW]({
						        css: { "width": "650px", "height": "auto"},
						        //event: 'no',
						        title: '修改设置',
						        content: htm,
						        id: 'publishManageMotidy_'+ids,
						        onLoad: function(div) {
						    			//表情，图片操作
						    			currDIV=div;
						    		   AllAction(pm_moy,This,div,iks,2,ids,auid);
						      	},
						      	onClose:function (){
						      			currDIV[RM]();
      									$[RM]({ obj: '#' + JLY });
						      	}
				    		});
								/*
          		 		     //-------------------------	
          		 		    //图片集合
          		 		     	var  imgs=[];
          		 		     	$(this)[PT]()[PT]()[PT]()[FD]('.talkMe_count')[FD]('img')[EH](function (){
          		 		     			var tem=$(this)[AT]('src');
          		 		     		    imgs.push(tem);
          		 		     	});
          		 		     
          		 		     	//最后上传的图片最后的格式
          		 		     	var  image="";
          		 		     	// 每一个保存到数据库中的路径地址ies集合
          		 		     	var  ies=[];
          		 		     	 if(imgs!=null){
          		 		    	  for(var i=0;i<imgs.length;i++){
          		 		    	  	   if(imgs[i].indexOf("/themes/modules/smile/")<0){
									  //每一个保存到数据库中的路径地址
									   var ko= imgs[i][RP](userWeiboImagePath,'$');         
										ies.push(ko);
          		 		    	  	}
          		 		    	  }
          		 		     }
          		 		     
          		 		     if(ies!=null){
          		 		     	 image=ies.join(",");
          		 		     }
          		 		//--------------------------- 
          		 		*/
          		 		     });//修改结束	
          		 		      if(data.rows.length==0){
								base.not(listDiv);
								  }	
          		 		      } //操作作业画出结束
          		 		      
          		 }// onComplete结束
          	});
          }
          
          //点击修改弹出层中的表情，图片，帐号选择，定时发布，修改按钮
          //aType  修改2,ids 帖子的id,auid 审核人id
          //pm_moy为列表中 点击的那个修改的按钮
          //This 就是弹出层后面的展示列表的对象
          function  AllAction (pm_moy,This,div,iks,aType,ids,auid){
             var selectFace = $('#selectFacePM', div), //打开表情按钮
             picPanel=$('#picPanel',div),//图片打开的面板
                facePanel = $('#facePanelPM', div), //表情面板
                selectPic=$('#selectPic', div), //图片按钮
                datePickerInput=$('#datePickerInput',div),//yyyy-mm-dd 控件
    			timePickerDiv=$('#timePickerDiv',div),//00:00:00控件
                release=$('#releasePM',div),//修改按钮
                _selectDatePicker=$('#p_selectDatePicker',div),//定时发送按钮
                datePickerPanel=$('#datePickerPanel',div),//时间控件面板
                //selectAccount=$('#selectAccount',div),//帐号选择按钮
                wordCount=$('#wordCount',div),//限制字数
                //accountPanel=$('#accountPanel',div),
                publish=$('#publishmg',div),//立即发布按钮
                textareaInfo = $('#textareaInfoPM', div), //多行文本框输入区对象
                panels = $('.panels', div); //所有面板集合
                
                
          		base.FacePicker(selectFace, facePanel, textareaInfo,panels);
          		//图片按钮中的panel
          		OpenPanel(selectPic, picPanel, PicOption,panels,div,iks);
          		//定时发送事件
          		OpenPanel2(_selectDatePicker, datePickerPanel, DateTimeOption,panels,div);
          		 HidePanels(publish,datePickerPanel,panels,div);
          		
          		base.LimitWord(textareaInfo, wordCount, release);
				//重新设置默认账号
				/*
				accountCollection = base.GetAccountCollection();
				base.AccountPicker('_my1', selectAccount, accountPanel, accountCollection , function(cball, cbItem) {
				cbItem[EH](function(i, item) { this[CD] =TE;	});
		 		cball[0][CD] = TE;
				}, function() {
			
			}, panels);          		
          */
          //修改保存按钮触发时间
          release[EV](CK,function (){
          var  $thisReleaseBtn=$(this);
          //判断多行文本框里不能不输入值 而发布  textareaInfo
           if($[TM](textareaInfo.val())==""){
           		$('#mypublicManager_Modifycontents',div).show();
           	    return FE;
           }
          
          	if(release[PT]()[FD]('.send_over').length>0){
          			 //text 
          			 var  txt=textareaInfo.val();
    				 var fileQue=$('#fileQueue2',div);
    				//最终保存到数据库中的img字符串
    				 var lastImgs="";
          			$[EH](fileQue[FD]('.data'),function (){
          				lastImgs+= $(this)[FD]('img')[AT]('src')[RP](userWeiboImagePath,'$')+',';
          			});
          			//最终保存到数据库中的img字符串
          			lastImgs=lastImgs.substring(0,lastImgs.length-1);
          			//获取单选按钮是否是立即发送还是定时发送
          			//判断到底选中的是哪个单选按钮
          			var flag;
          			//send 是发送   0 立即发送,  不是立即发送的传入一个数字表示
          			var send;
          			publish[EH](function (){
          				flag=this[CD];
          			});
          			if(flag){
          				  send=0;
          			}
          			else{
          			send=datePickerInput.val()+" "+timePickerDiv[AT]('time');
          		    
          		    //定时时间不能小于当前时间的后10分钟
          			if($.compareDate(send, $.getDateTime()) >=0) {
					 $('#mypublicManager_ModifyPreTimes',div).show();	
					//$[MB]({content: '定时发布时间不能小于当前时间', type: 2});
					return ;
					}
          		}
          				//判断此tid 微博帖子的帐号是否已经删除了----
          				//就判断 span_marL[代表是否存在发布帐号这几个字] 
          				//(代表pmUID 此微博帐号存在不存在,是否已经删除了)， 不存在就代表此帐号已经删除了
          				var  publicAccount=pm_moy[PT]()[PT]()[PT]()[FD]('h2').eq(0)[FD]('.span_marL');
          				//提示此微博帐号已经删除 不能再发送微博了
          				if($[TM](publicAccount[TX]())==''){
          				$('#mypublicManager_ModifycheckAccount',div).show();
          				return ;
          				}
          		
          			//向后台发送信息AJax 请求
          			ExecuteAction(aType,ids,auid,txt,lastImgs,div,send);
          		}
          });
          
          
          // 在弹出层中输入字 让mypublicManager_Modifycontents div隐藏
            	textareaInfo.focus(function (){
          	   $('#mypublicManager_Modifycontents',div).hide();
          	});
          	
          }
          
              //打开面板
    function OpenPanel2(btn, panel, fn,panels,div) {
    	btn[EV](CK, function() {
    		$('#mypublicManager_ModifyPreTimes',div).hide();
    		panels.hide();
    		panel[TG]();
    		fn(div);
		});
    }
    
    //点击立即发布按钮，让时间面板消失
    function HidePanels(btn,dateP,panels,div){
      	btn[EV](CK, function() {
			$('#mypublicManager_ModifyPreTimes',div).hide();
    		dateP.hide();
    		panels.hide();
		});
    }
          
              //时间面板后续操作
    function DateTimeOption(div) {
    	var datePickerClose=$('#datePickerClose',div);
    	var  timePickerOK=$('#timePickerOK',div);
    	var datePickerInput=$('#datePickerInput',div);
    	var timePickerDiv=$('#timePickerDiv',div);
    	 //判断 如果是第一次的话，日期和时间都为""字符串
    	 if(datePickerInput.val()==""){
    	 	datePickerInput.datePicker({ minDate: $.getDate() });
    	 	var time = $.addDate('n', 10, $.getDateTime()).Format('hh:mm:ss'); //默认时间+10分钟
	    	timePickerDiv.timePicker({  id: '_a22', defaultTime: time });
    	 }
    	 //非首次的话，时间和日期有值
    	 else{
    	 	var dateTemp=datePickerInput.val();
    	 	datePickerInput.datePicker({ minDate: $.getDate() }).val(dateTemp);
    	 	//var  deftime=timePickerDiv[AT]('time');
    	 	var  deftime= $.addDate('n', 10, $.getDateTime()).Format('hh:mm:ss'); //默认时间+10分钟
    	 	timePickerDiv.timePicker({  id: '_a22',defaultTime:deftime});
    	 }
    	ClosePanel(datePickerClose);
    	ClosePanel(timePickerOK);
    }
    
        //关闭面板
    function ClosePanel(btn) {
    	btn[EV](CK, function() {
    		$(this)[PT]().hide();
    		return FE;
		});
    }
          
              //打开面板
    function OpenPanel(btn, panel, fn,panels,div,iks) {
    //点击图片把数据库中已经有的还原到面板上面
    var  picItem=$('#picItem2',div);
    var fileQueue=$('#fileQueue2',picItem);
    	var htm='<div class="uploadify-queue-item" id="sw_save_{0}"  style="border-radius: 15px;">'+
    	'<div class="cancel"><a href=\"javascript:$(\'#uploadify2\').uploadify(\'cancel\', \'sw_save_{1}\')"></a></div><span class="data">{2}'+
    	//'<img style="width:60px; height:60px;" src="">'+
    	'<div style="clear:right; font:12px;"></div></span><div class="uploadify-progress com_none">'+  //<div style="clear:right; font:12px;">ok</div></span><div class="uploadify-progress com_none">
    	'<div class="uploadify-progress-bar" style="width: 100%;"></div></div></div>';
    	var Ahtml="";
    	if(iks!=null){
    	 for (var i=0;i<iks.length;i++){
    	 		//width="100" height="100"
    	 			   iks[i]=iks[i][RP]('width="100"','width="60"')[RP]('height="100"','height="60"');
		    		   Ahtml+=$[FO](htm,i,i,iks[i]);
    	 }
    	  fileQueue[AP](Ahtml);
    	   $('.uploadify-queue-item',div).corner('15px');
   	}
   	
   	   //重新打开把那个大+的div 隐藏了
   	 	if($('.uploadify-queue-item',div)[LN]>=4){
		    			// $('#uploadify2',div).hide();
						 $('#uploadify2',div).css('visibility','hidden');
		 }
	
				    btn[EV](CK, function() {
		    		panels.hide();
		    		panel[TG]();
		    		fn(div);
					});
    }
          
     //图片面板后续操作
	function PicOption(div) {
	   var  picColse=$('#picColse',div);
		ClosePanel(picColse);
	    /*
	    //重新打开把那个大+的div 隐藏了
	    if($('.uploadify-queue-item',div)[LN]>=4){
		    			   //  $('#uploadify2',div).hide();
		    			    $('#uploadify2',div).css('visibility','hidden');
		 }else{
		 	Uploadify(div);
		 }
		 */
		 Uploadify(div);
	}
	
	    //关闭面板
    function ClosePanel(btn) {
    	btn[EV](CK, function() {
    		$(this)[PT]().hide();
    		return FE;
		});
    }
    
        //private method
	function Uploadify(div) {
		$("#uploadify2",div).uploadify({
             swf: xkPath + 'scripts/plugin/jquery.uploadify.swf',
            //后台处理的页面
            uploader: xkPath+ 'upload.action',
            uploader: xkPath+ 'upload.action?cid=' + $.cookie("XUNKUID"),
	        buttonClass: "spic_ImgsAdd",
            buttonText: '+',
            height: 100,
            width: 100,
            fileTypeExts: '*.gif; *.jpg; *.png; *.jpeg',
            queueID: 'fileQueue2',
            auto: TE,
            multi: FE,
            queueSizeLimit: 1,
            simUploadLimit: 1,
            removeCompleted: FE,
            userImageIDs:userImageIDs,
            onUploadStart: function (file) {
            	$('#' + file.id)[FD]('.data')[LG]();
            },
            onUploadSuccess: function (file, data, response) {
            var  uploadifyTip=$('#uploadifyTip2',div);
            	//超出图片容量大小
            	if(data == 'sizeError') {
            		$('#' + file.id)[RM]();
            		uploadifyTip.text('您上传的图片容量大小超出限制,请重新上传图片');
            	} else {
            		uploadifyTip.text('');
            		var imageArr = data.split(',');
            		userImageIDs.push({id:file.id,imageid:imageArr[1]});
	                var pics = '.uploadify-queue-item',
	                      uploadify = $('#uploadify2'),
	                      v = 'visibility';
	                var pathImage = imageArr[0][RP]('$', userWeiboImagePath);
	                $('#' + file.id)[AT]('imageid', imageArr[1])[FD]('.data').html('<image src="' + pathImage  +  '" style="width:60px; height:60px;" /><div style="clear:right; font:12px;"></div>');
	                if ($(pics,div)[LN] == base.imageMaxCount)   uploadify.css(v, 'hidden');
					$('a', $('#fileQueue2'))[UB](CK)[BD](CK, function () {
	                    $(this)[PT]()[PT]()[RM]();
	                  //  if ($(pics,div)[LN] < 4) uploadify.css(v, 'visible');
	                	     if($('#picPanel',div)[FD]('img')[LN] <=base.imageMaxCount)   {
	                	     $("#uploadify2-button").show();
	                	     	$('#uploadify2').css('visibility', 'visible');
	                	     
	                	     }
	                });

	                $('.uploadify-queue-item',div).corner('15px');
            	}//else结束
            }
        });
        $(".uploadify-button").corner('15px');
           $('a', $('#fileQueue2'))[UB](CK)[BD](CK, function () {
	                    $(this)[PT]()[PT]()[RM]();
	                  //  if ($(pics,div)[LN] < 4) uploadify.css(v, 'visible');
	                	     if($('#picPanel',div)[FD]('img')[LN] <=base.imageMaxCount)   {
	                	     $("#uploadify2-button").show();
	                	     	$('#uploadify2').css('visibility', 'visible');
	                	     
	                	     }
	                });
          if($('#picPanel',div)[FD]('img')[LN] == base.imageMaxCount)  
	      $("#uploadify2-button").hide();
        
	}
	
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
	
					//比较时间日期的大小
					//sentTime 后面的_1 代表定时发布  _0代表立即发布
					function   CompareDateAndTime (dateShuzu,timeShuzu,sentTime){
	 		        				//比较日期
							     	 var date =new  Date();
							     	 //年份大于当前的
							     	 if(parseInt(date.getFullYear())<parseInt(dateShuzu[0])){
							     	 sentTime=sentTime+'_1';
							     	 }
							     	 //年份等于当前的
							     	 else if (parseInt(date.getFullYear())==parseInt(dateShuzu[0])) {
							     	    	 var ttime='';
							     	         //IE, google浏览器下显示的月份是 09这样的
							     	         if(dateShuzu[1].substring(0,1)=='0'){
							     	         		ttime=dateShuzu[1].substring(1);
							     	         }
							     	         //火狐浏览器下的显示的月份是 9这样的
							     	         //当IE下的 月份是两位数的也走这个
							     	         else{
							     	         		ttime=dateShuzu[1];
							     	         }
							     	    //判断月份大于当前系统的月份
							     	   	if((parseInt(date.getMonth())+1)<parseInt(ttime)){
							     	   		 sentTime=sentTime+'_1';
										} // 判断月份大于当前系统的月份  结束
							     	       //判断月份等于于当前系统的月份
							     	    else if((parseInt(date.getMonth())+1)==parseInt(ttime)){
							    				 var ttime2='';
								     	         //IE, google浏览器下显示的日期是 09这样的
								     	         if(dateShuzu[2].substring(0,1)=='0'){
								     	         		ttime2=dateShuzu[2].substring(1);
								     	         }
								     	         //火狐浏览器下的显示的日期是 9这样的
								     	         //当IE下的 日期是两位数的也走这个
								     	         else{
								     	         		ttime2=dateShuzu[2];
								     	         }
								     	     //判断日期大于当前系统的日期
									 		if(parseInt(date.getDate())<parseInt(ttime2)){
													   sentTime=sentTime+'_1';
											} //  判断日期大于当前系统的日期 结束
											//判断日期等于当前系统的日期
										      else if(parseInt(date.getDate())==parseInt(ttime2)){
										      	 var ttime3='';
								     	         //IE, google浏览器下显示的日期是 09这样的
								     	         if(timeShuzu[0].substring(0,1)=='0'){
								     	         		ttime3=timeShuzu[0].substring(1);
								     	         }
								     	         //火狐浏览器下的显示的日期是 9这样的
								     	         //当IE下的 日期是两位数的也走这个
								     	         else{
								     	         		ttime3=timeShuzu[0];
								     	         }
								     	        //判断定时发布的时间的时钟大于 当前系统的小时 
								     	        if(parseInt(date.getHours())<parseInt(ttime3)){
								     	        		  sentTime=sentTime+'_1';
								     	        }   //判断定时发布的时间的时钟大于 当前系统的小时  结束
								     	       //判断定时发布的时间的时钟等于 当前系统的小时
								     	        else if (parseInt(date.getHours())==parseInt(ttime3)){
								     	          var ttime4='';
								     	         //IE, google浏览器下显示的日期是 09这样的
								     	         if(timeShuzu[1].substring(0,1)=='0'){
								     	         		ttime4=timeShuzu[1].substring(1);
								     	         }
								     	         //火狐浏览器下的显示的日期是 9这样的
								     	         //当IE下的 日期是两位数的也走这个
								     	         else{
								     	         		ttime4=timeShuzu[1];
								     	         }
								     	          //判断定时发布的时间的时钟大于而且大于2分钟 当前系统的分钟
								     	          if(parseInt(date.getMinutes())<parseInt(ttime4)&&(parseInt(ttime4)-  parseInt(date.getMinutes()))>=2 ){
								     	                 sentTime=sentTime+'_1';
								     	          }   ////判断定时发布的时间的时钟大于而且大于2分钟 当前系统的分钟
								     	          
								     	          //判断定时发布的时间的时钟等于或 小于2分钟 当前系统的分钟
								     	        	else if (parseInt(date.getMinutes())==parseInt(ttime4)||(parseInt(ttime4)-  parseInt(date.getMinutes()))<2){
								     	        	  sentTime=sentTime+'_0';
								     	        	}  //判断定时发布的时间的时钟等于 或 小于2分钟 当前系统的分钟 结束
								     	        	
								     	        }  //判断定时发布的时间的时钟等于 当前系统的小时
								     	         
										      } //判断日期等于当前系统的日期 结束	
											
							     	    }  //判断月份等于当前系统的月份 结束
							     	    
							     	 }  //  年份等于当前的 结束
          		 		     return   	sentTime;
	}  //CompareDateAndTime
	
	
	         
          
    return {
    	IsLoad: FE,
        OnLoad: function (_ContentDiv) {
            var t = this;
            contentDiv = _ContentDiv;
            $('<div />').load('modules/myTwitter/myTwitter.publishManage.htm', function () {
            	contentDiv[AP]($(this).html());
            	navigationBars=context.NavigationBars;
            	base=context.Base;
            	mediator=context.Mediator;
            	cuser=context.CurrentUser;
            	t.IsLoad = TE;
                $key = $('#' + key);
                listDiv=$("#publishManager_all");
                titleSpan=$(".zhao_change",$key);
                pageFenYe=$('#pageFenYe',$key);
                pmStartTime=$('#PumMySubmit_dateStart',$key);//开始时间
		        pmEndTime=$('#PumMySubmit_dateEnd',$key);//结束时间
		        pmSumbiter=$('#pubMangerMySubimt_Select2',$key);//提交人下拉框
		        pmWeiboTypes=$('#pubMangerMySubimt_Select1',$key);//筛选微博类型
		          pmSearchBtn=$('#PumMySubmit_selButton',$key);//搜索条件按钮
                
                	userImageIDs = [];//{key,value}
                	 accountCollection = []; //该模块使用的账号集合（带状态）
                Init();
            });
            return t;
        },
           ToolBar : function(){
					 AlearyDraw();
        },
        //显示模块方法
        Show:function() {
        	$key.show();
        	 AlearyDraw();
        }
    };
});