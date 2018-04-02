//数据分析
define(['portal'], function (context) {
    //定义变量
    var key = 'dataAnalysis',
          $key, //当前的作用域
          navigationBars,
          base,
          _enum,
          charType,
          
          date,
          panels,
          dataCurrentAccountStart,
          
          tabs,
          contents,
          
          userV,
          userImg,
          userName,
          userContent,
          userArea,
          userFriend,
          userFollow,
          userTwitter,
          userLiveness,
          userLivenessCount,
          userInfluence,
          userInfluenceCount,
          
          day7,
          day30,
          startDay,
          endDay,
          charDateBtn,
          charBtns,
          startTimeDay,
          endTimeDay,
          charDateTimeBtn,
          
          dayTM7,
          dayTM30,
          startTMDay,
          endTMDay,
          charDateTMBtn,
          charTMBtns,
          
          dayQS7,
          dayQS30,
          startQSDay,
          endQSDay,
          charDateQSBtn,
          charQSBtns,
          
          fsAuthentication,
          fsSex,
          fsCount,
          fsFans,
          fsArea,
          
          
          startTimeSave,
          endTimeSave,
          dataCount,
          contentDiv;
    //私有方法
    //初始化方法，写自己模块的业务
    function Init() {
    	NavigationInit();
    	accountCollection = base.GetAccountCollection(); //获取账号集合
    	//设置当前账号选择器
    	base.CurrentAccountPicker(dataCurrentAccountStart, 'da_aa', accountCollection, panels, function () {
			//设置页签切换方法
	    	base.OnChangeTab(tabs, contents, 'zhao_back', function(index) {
	    		TabFactory(index);
	    	}, TE);
	    	
	    	//获取当前账号
	    	LoadUserInfo();
		});
    	
    	//获取当前账号
    	LoadUserInfo();
    }
     //导航条初始化方法,
	function NavigationInit () {
		navigationBars.Empty()
			.Add(key, 'NavToDA', '我的微博')
    		.Add(key, 'NavToDA', '数据分析')[oL]();
	}
	
	//获取选择账号ID
	function GetAccountID() {
		return $('#currentAccountInfo', $key)[AT]('uid');
	}
	//获取选择账号Name
	function GetAccountName() {
		return $('#currentAccountInfo', $key).text();
	}
	//获取选择账号Type
	function GetAccountType() {
		return $('#currentAccountInfo', $key)[AT]('type');
	}
	
	//获取当前账号信息(大V)
	function LoadUserInfo() {
		userImg[AJ]({
			param: {action: 'dataAnalyze.userInfo', accountid: GetAccountID(), platform: GetAccountType() },
			success: function (data) {
				var account = data.account;
				userImg[AT]('src', account.imgurlBig);
	            userName.text(account.name);
	            userArea.text(account.area == '' ? '暂无': account.area);
	            userContent.text(account.summany == '' ? '暂无': account.summany); // (account.summany > 27 ? account.summany.substring(0, 27) + '...': account.summany);
	            userFriend.text(account.friends);
	            userFollow.text(account.followers);
	            userTwitter.text(account.weibos);
	            userV[data.isV? 'show': 'hide']();
	          	userLiveness[WH](CalculateProportion(data.userLiveness));
	          	userLivenessCount.text(data.userLiveness);
          		userInfluence[WH](CalculateProportion(data.userInfluence));
          		userInfluenceCount.text(data.userInfluence);
			}
		});
	}
	
	//计算比例(活跃度以及影响力), dataCount: 数量
	function CalculateProportion(dataCount) {
		if (dataCount > 1300) dataCount = 1300;
		return dataCount/4 + 1;
	}
	
	//切换页签的简单工厂
	function TabFactory(index) {
		if (index == 0) {
			DateChar();
		} else if (index == 1)
		 	DateMYChar();
		else if (index == 2) 
			DateSQChar();
		else if (index == 3)
			DateFSChar();
	}
	
	//加载粉丝分析页签内容
	function DateFSChar() {
		fsAuthentication.add(fsSex).add(fsCount).add(fsFans).add(fsArea).html('')[LG]();
		fsSex[LG]();
		fsCount[LG]();
		fsFans[LG]();
		fsArea[LG]();
		var param = {};
		param.action = 'dataAnalyze.fans';
		param.ucode = GetAccountID();
		param.platform = GetAccountType();
		
		$('<div />')[AJ]({
			param: param,
			success: function (data) {
				//alert($.toJson(data));
				// 认证比例图
				base.Char(fsAuthentication, charType.Pie, data.vip);
				// 粉丝数图
				if(data.fans.series[LN] > 0) {
					base.Char(fsCount, charType.ColumnPercent, data.fans);
				} else {
					base.not2(fsCount);
				}
				// 性别比例图
				base.Char(fsSex, charType.Pie, data.sex);
				// 粉丝互动图
				var fansTemplate  ='<li><span class="each_num top_three">{id}</span><span class="each_photo"><img src="{imgurl}" width="25px" height="25px" class="com_fLeft" /></span><span>{name}</span><span class="each_allNum">{weibos}</span></li><div class="com_clear"></div>';
				if(data.supermans != UN) {
					fsFans.html('<ul >' + $[EF](fansTemplate, data.supermans) + '</ul>');
				} else {
					base.not2(fsFans);
				}
				// 地域图
				base.Char(fsArea, charType.ColumnPercent, data.location);
			}
		});
	}
	
	//加载趋势分析页签内容
	function DateSQChar () {
		//设置DatePicker
    	startQSDay.datePicker(); 
    	endQSDay.datePicker();
    	
    	startQSDay.val(startTimeSave);
    	endQSDay.val(endTimeSave);
    	
    	//dataAnalyze.retweetTrend
    	//LoadRetweetTrend
    	var className = 'selW_selected',
    		   divReport = $('#dataAnalysisSQData', $key),
    		   divComment = $('#dataAnalysisSQData2', $key);

		dayQS7[EV](CK, function() {
			var $this = $(this),
				   endTime = $.getDate(),
				   startTime  = $.addDate('d', -6, endTime).Format('YYYY-MM-DD');
				   
			$this[AC](className); 
			dayQS30[RC](className);
			var param = {
				action: 'dataAnalyze.commentTrend',
				startTime: startTime, 
				endTime: endTime, 
				dayCount: 6,
				uid: GetAccountID(),
				platform: GetAccountType() 
			};
			LoadRetweetTrend(divComment, param);
			param.action = 'dataAnalyze.retweetTrend';
			LoadRetweetTrend(divReport, param);
		})[TR](CK);
		
      	dayQS30[EV](CK, function() {
      		var $this = $(this),
      			   endTime = $.getDate(),
				   startTime  = $.addDate('d', -29, endTime).Format('YYYY-MM-DD');
      		
      		$this[AC](className); 
      		dayQS7[RC](className);
			var param = {
      			action: 'dataAnalyze.retweetTrend',
				startTime: startTime, 
				endTime: endTime, 
				dayCount: 29,
				uid: GetAccountID(),
				platform: GetAccountType() 
			};
      		LoadRetweetTrend(divComment, param);
      		param.action = 'dataAnalyze.commentTrend';
			LoadRetweetTrend(divReport, param);
      	});
      	
		charDateQSBtn[EV](CK, function() {
			if(base.IsRegExpDateTime(startQSDay, endQSDay)) {
				
				dayQS7[RC](className); 
				dayQS30[RC](className);
				
				//记录开始时间、结束时间状态
				startTimeSave = startQSDay.val();
				endTimeSave = endQSDay.val();
				var param = {
					action: 'dataAnalyze.retweetTrend',
					startTime: startTimeSave, 
					endTime: endTimeSave, 
					dayCount: $.diffDate(startTimeSave, endTimeSave),
					uid: GetAccountID(),
					platform: GetAccountType()  
				};
				LoadRetweetTrend(divComment, param);
				param.action = 'dataAnalyze.commentTrend';
				LoadRetweetTrend(divReport, param);
			}
		});
	}
	
	//加载@我统计分析页签内容
	function DateMYChar () {
		//设置DatePicker
    	startTMDay.datePicker(); 
    	endTMDay.datePicker();
    	
    	startTMDay.val(startTimeSave);
    	endTMDay.val(endTimeSave);

		var className = 'selW_selected',
			  div = $('#dataAnalysisTMData', $key);
		dayTM7[EV](CK, function() {
			var $this = $(this),
				   endTime = $.getDate(),
				   startTime  = $.addDate('d', -6, endTime).Format('YYYY-MM-DD');
				   
			$this[AC](className); 
			dayTM30[RC](className);
			
			LoadPublishByDate(div, {
				action: 'dataAnalyze.mention',
				startTime: startTime, 
				endTime: endTime, 
				dayCount: 6,
				uid: GetAccountID(),
				platform: GetAccountType()
			});
		})[TR](CK);
		
      	dayTM30[EV](CK, function() {
      		var $this = $(this),
      			   endTime = $.getDate(),
				   startTime  = $.addDate('d', -29, endTime).Format('YYYY-MM-DD');
				   
      		$this[AC](className); 
      		dayTM7[RC](className);
      		
      		LoadPublishByDate(div, {
      			action: 'dataAnalyze.mention',
      			startTime: startTime, 
      			endTime: endTime,  
      			dayCount: 29,
      			uid: GetAccountID(),
      			platform: GetAccountType()
  			});
      	});
      	
		charDateTMBtn[EV](CK, function() {
			if(base.IsRegExpDateTime(startTMDay, endTMDay)) {
				
				dayTM7[RC](className); 
				dayTM30[RC](className);
				
				//记录开始时间、结束时间状态
				startTimeSave = startTMDay.val();
				endTimeSave = endTMDay.val();

				LoadPublishByDate(div, {
					action: 'dataAnalyze.mention',
					startTime: startTimeSave, 
					endTime: endTimeSave, 
					dayCount: $.diffDate(startTimeSave, endTimeSave),
					uid: GetAccountID(),
					platform: GetAccountType()
				});
			}
		});
	}
	
	//加载发布统计 按日期分析图表
	function DateChar () {
		//设置DatePicker
		startDay.val(startTimeSave);
    	endDay.val(endTimeSave);
    	startDay.datePicker(); 
    	endDay.datePicker();
    	startTimeDay.datePicker({addDay: -7}); 
        endTimeDay.datePicker(); 
        
        
    	
		var className = 'selW_selected',
			  div = $('#dataAnalysisOne', $key),
			  divTwo = $('#dataAnalysisTwo', $key);
		day7[EV](CK, function() {
			var $this = $(this),
				   endTime = $.getDate(),
				   startTime  = $.addDate('d', -6, endTime).Format('YYYY-MM-DD'); 
				   
			$this[AC](className);
			day30[RC](className);

			LoadPublishByDate(div, {
					action: 'dataAnalyze.publishByDate', 
					startTime: startTime, 
					endTime: endTime, 
					dayCount: 6,
					uid: GetAccountID(),
					platform: GetAccountType()
			});
		})[TR](CK);
      	
      	day30[EV](CK, function() {
      		var $this = $(this),
      			   endTime = $.getDate(),
				   startTime  = $.addDate('d', -29, endTime).Format('YYYY-MM-DD'); 
      		$this[AC](className); 
      		day7[RC](className);
      		
      		LoadPublishByDate(div, {
      			action: 'dataAnalyze.publishByDate', 
      			startTime: startTime, 
      			endTime: endTime, 
      			dayCount: 29,
      			uid: GetAccountID(),
      			platform: GetAccountType() 
  			});
      	});
      	
		charDateBtn[EV](CK, function() {
			if(base.IsRegExpDateTime(startDay, endDay)) {
				
				day7[RC](className); 
				day30[RC](className);
				
				//记录开始时间、结束时间状态
				startTimeSave = startDay.val();
				endTimeSave = endDay.val();
				
				LoadPublishByDate(div, {
					action: 'dataAnalyze.publishByDate', 
					startTime: startTimeSave, 
					endTime: endTimeSave, 
					uid: GetAccountID(),
					dayCount: $.diffDate(startTimeSave, endTimeSave),
					platform: GetAccountType() 
				});
			}
		});
		
		charDateTimeBtn[EV](CK, function() {
			if(base.IsRegExpDateTime(startTimeDay, endTimeDay)) {
				LoadPublishByDate(divTwo, {
					action: 'dataAnalyze.publishByDay', 
					startTime: startTimeDay.val(), 
					endTime: endTimeDay.val(), 
					uid: GetAccountID(),
					platform: GetAccountType() 
				});
			}
		})[TR](CK);
	}
	

	
	//发布统计 按日期查询数据和按时间段查询数据(Ajax)
	function LoadPublishByDate(div, param) {
		div.html('')[LG]();
		div[AJ]({
			param: param,
			success: function (data) {
				if(data != null) {
					base.Char(div, charType.Column, data, 60);
				} else {
					base.not(div);
				}
			}
		});
	}
	
	function LoadRetweetTrend(div, param) {
		div.html('')[LG]();
		div[AJ]({
			param: param,
			success: function (data) {
				if(data != null) {
					base.Char(div, charType.Line, data, 60);
				} else {
					base.not(div);
				}
			}
		});
	}

    return {
    	IsLoad: FE,
        OnLoad: function (_ContentDiv) {
            var t = this;
            contentDiv = _ContentDiv;
            $('<div />').load('modules/myTwitter/myTwitter.dataAnalysis.htm', function () {
            	contentDiv[AP]($(this).html());
            	t.IsLoad = TE;
                $key = $('#' + key);
                navigationBars = context.NavigationBars;
                base = context.Base;
                _enum = context.Enum;
                charType = _enum.CharType;
                date = new Date();
                
                panels = $('.panels', $key); //面板集合
                dataCurrentAccountStart = $('#dataCurrentAccountStart', $key); //当前账号开始Div
                
                userV = $('#userV', $key); //用户是否为大V
                userImg = $('#userImg', $key); //用户大图头像Img对象
                userName = $('#userName', $key); //用户昵称Span对象
                userContent = $('#userContent', $key); //用户简介
                userFriend = $('#userFriend', $key); //用户关注数量td对象
	            userFollow = $('#userFollow', $key); //用户粉丝数量td对象
	            userTwitter = $('#userTwitter', $key); //用户微博数量td对象
	            userLiveness = $('#userLiveness', $key); //用户活跃度span对象
          	    userInfluence = $('#userInfluence', $key); //用户影响力span对象
          	    userLivenessCount = $('#userLivenessCount', $key); //用户活跃度数量span对象
          	    userInfluenceCount = $('#userInfluenceCount', $key); //用户影响力数量span对象
                
                //发布统计
                day7 = $('#day7', $key); //查看7天按钮Div
		        day30 = $('#day30', $key); //查看30天按钮Div
		        startDay = $('#startDay', $key); //开始时间Input
		        endDay = $('#endDay', $key); //结束时间Input
		        charDateBtn = $('#charDateBtn', $key); //按日期分析图片按钮
		        charBtns = $('.charBtns', $key); //按日期分析按钮集合
		        startTimeDay = $('#startTimeDay', $key); //按时间分析开始时间Input
          	    endTimeDay= $('#endTimeDay', $key); //按时间分析结束时间Input
          		charDateTimeBtn = $('#charDateTimeBtn', $key); //按时间分析按钮
		        
		        dayTM7 = $('#dayTM7', $key);
	            dayTM30 = $('#dayTM30', $key);
	            startTMDay = $('#startTMDay', $key);
	            endTMDay = $('#endTMDay', $key);
	            charDateTMBtn = $('#charDateTMBtn', $key);
	            charTMBtns = $('.charTMBtns', $key); //按@我统计分析按钮集合
		        
		        //趋势分析
		        dayQS7 = $('#dayQS7', $key);
		        dayQS30 = $('#dayQS30', $key);
		        startQSDay = $('#startQSDay', $key);
		        endQSDay = $('#endQSDay', $key);
		        charDateQSBtn = $('#charDateQSBtn', $key);
		        charQSBtns = $('.charQSBtns', $key); //按趋势分析按钮集合
		        
		        fsAuthentication = $('#fsAuthentication', $key); //认证比例图表Div
		        fsSex = $('#fsSex', $key); //性别比例图表Div
		        fsCount = $('#fsCount', $key); //用户粉丝数图表Div
		        fsFans = $('#fsFans', $key), //粉丝互动
          		fsArea = $('#fsArea', $key), //地域
          		userArea = $('#userArea', $key), // 地点
                
                startTimeSave = $.getDate();
                endTimeSave = $.getDate();
                tabs = $('.tabs', $key); //页签Span集合
          	    contents = $('.contents', $key); //内容Div集合
                Init();
            });
            return t;
        },
        NavToDA: function () {
        	this.Show();
        },
        //显示模块方法
        Show: function() {
        	NavigationInit();
        	$key.show();
        }
    };
});