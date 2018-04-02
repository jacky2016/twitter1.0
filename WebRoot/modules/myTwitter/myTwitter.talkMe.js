//@提到我的
define(['portal'], function (context) {
    //定义变量
    var key = 'talkMe',
          $key, //当前的作用域
          contentDiv,
		  listdiv,
		  currentAccountInfoDiv,
		  select1,
		  select2,
		  base,
		  panels,
		  stime,
		  etime,
		  selectAccount,
		   accountCollection ,
		   accFlag=FE,// 判断当前的微博帐号是否授权到期
		 page,
		 _enum,
          navigationBars;
                  //初始化方法，写自己模块的业务
   			function    Init() {
            var t = this;
            NavigationInit();
  			InifDraw();
  			 serachButton();
		  	 //DrawUserPage('search');
   			return t;
        }
        
        
        function serachButton(){
        	$('#selButton')[EV](CK,function(){
        	//defaultMethod='search';
        	var  tt= base.IsRegExpDateTime(stime,etime ,TE);
        	 if(tt==FE){
        	 	return  FE;
        	 }
        	defaultMethod='';
        			DrawUserPage(defaultMethod);
        	});
        }
        
        	function InifDraw(){
					accountCollection = base.GetAccountCollection();
		      		base.CurrentAccountPicker(selectAccount, "talkMe_DrowDown",accountCollection, panels, function (flag) {
		      		accFlag=flag;
					DrawUserPage('search');	      		
				});
        	
        		select1.input({
	     		id:"user_list",
	     		collection:[{text:'所有用户',value:0},
	     							  {text:'认证用户',value:1},
	     							  {text:'关注的人',value:2}]
	     							  //,{text:'网评员',value:3}]
	     	});
	     	
	     	select2.input({
	     		id:"sel_style1",
	         		collection:[{text:'所有微博',value:0},
	     							  {text:'原创',value:1},
	     							  {text:'转发',value:2},
	     							  {text:'图片',value:3}]
	     	});
	     				    $("#dateEnd",$key).datePicker();
	     			        var tempTimeStart=$.addDate('d', -6, $("#dateEnd",$key).val()).Format('YYYY-MM-DD');
	        				$("#dateStart",$key).datePicker().val(tempTimeStart);
        					$("#user_list",$key).val(0);
        					$("#sel_style1",$key).val(0);
        				   
        }
		
		//工具栏显示        
        function NavigationInit(){
           	navigationBars.Empty()
			.Add(key, 'NavToTM', '我的微博')
    		.Add(key, 'NavToTM', '@到我的')[oL]();
        }

          function DrawUserPage(defaultMethod){
          currentAccountInfoDiv=$('#currentAccountInfo',selectAccount);
		  var   	page=   $('#pageFenYe',$key);
		  listdiv.add(page).html('');
		//  var ut=$('#user_list',$key).val();
		 // var  wt=$('sel_style1',$key).val();
		 var   ut=select1[AT]('value');
		 if(ut==UN){
		   ut=0;
		 }
		 var wt=select2[AT]('value');
		  if(wt==UN){
		     wt=0;
		  }
		  var starttime=$('#dateStart',$key).val();
		  var  endtime=$('#dateEnd',$key).val();
		   var  plat=currentAccountInfoDiv[AT]('type');
		   //var  currentUID=currentAccountInfoDiv[AT]('uid');
		   var uid=currentAccountInfoDiv[AT]('uid');
		     if(plat==UN){
		        plat=1;
		     }
		   base.DataBind({
				panel: listdiv, 
				pager: page,
				id: 'data', 
				isSelectAccount: TE,
			    isFlag:  accFlag,
			    param: {action :'talkMe.talkMeListDefault',queryConditions:'talkMeList',userType:ut,weiboType:wt, starttime:starttime,endtime: endtime ,defaultMethod: defaultMethod,
			    Platform:plat,uid:uid},
				//template: '<span>转发</span><span>|</span><span>评论</span><span>|</span><span >收集</span>',
			    //template: base.FormatPmi('{pmi:yqzs_collect_1}<span class="repostCount">转发</span><span>|</span><span class="commentCount">评论</span>'),
			  //  template: base.FormatPmi('{pmi:yqzs_collect_1}{pmi:wdwb_adwdrepost_1}{pmi:wdwb_adwdcomment_1}'),
			    //template: base.FormatPmi('<span class="alertBtn" url="" alertid="0">预警</span><span>|</span>{pmi:yqzs_collect_1}{pmi:wdsy_repost_1}{pmi:wdsy_comment_1}'),
			    template: base.FormatPmi('{pmi:yjfw_yjxxinsert_1}{pmi:yqzs_collect_1}{pmi:wdsy_repost_1}{pmi:wdsy_comment_1}'),
				//isComment:TE,
				isComment:FE,
				moduleType: _enum.ModuleType.talkMe,
				isRepostCommonCount:TE,
				onComplete: function (This, refresh, data) { 
				/*
				    $('.cont_doright.buttons',This)[EH](function (){
							/*
							if($[TM]($(this)[FD]('span').eq($(this)[FD]('span')[LN]-1)[TX]()=='|')){
							  $(this)[FD]('span').eq($(this)[FD]('span')[LN]-1)[RM]();
							}
							*00/
							var index=$('.cont_doright.buttons',This)[ID]($(this));
							$(this)[FD]('span')[EH](function (){
									if($[TM]($(this)[TX]())=='转发'){
									 $(this)[TX]('转发['+data.rows[index].repostCount+']');
									}
									if($[TM]($(this)[TX]())=='评论'){
									$(this)[TX]('评论['+data.rows[index].commentCount+']');
									}
							
							});
				    });
				    */
				},
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
				
				}//onSend 结束
			});
	
          }
          
    return {
    	IsLoad: FE,
        OnLoad: function (_ContentDiv) {
            var t = this;
            contentDiv = _ContentDiv;
            $('<div />').load('modules/myTwitter/myTwitter.talkMe.htm', function () {
            	//contentDiv[AP]($(this).html());
            	base = context.Base;
            	contentDiv[AP](base.FormatPmi($(this).html()));
            	t.IsLoad = TE;
                $key = $('#' + key);
                   navigationBars=context.NavigationBars;
                   panels = $('.panels', $key); //所有面板集合
                   accountCollection = []; //该模块使用的账号集合（带状态）
                   listdiv = $('#listdiv', $key); 					//列表Div
                   	_enum = context.Enum;
                   select1=$('#talkmeSelect1',$key);
        		   select2=$('#talkmeSelect2',$key);
        		   selectAccount= $('#talkme_selectAccount',$key);
        		   stime= 	$("#dateStart",$key);
        		   etime=$("#dateEnd",$key);
                   base = context.Base;
               	   Init();
            });
            return t;
        },
      //导航条调用方法
        NavToTM: function() {
       // $key.text('@到我的');
       			  Init();
        },
        //显示模块方法
        Show:function() {
        	$key.show();
			  Init();
        }
    };
});