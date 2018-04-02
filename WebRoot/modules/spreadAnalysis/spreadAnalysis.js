//传播分析
define(['portal'], function (context) {
    //定义变量
    var key = 'spreadAnalysis',
           $key, //当前的作用域
           navigationBars,
           base,
		   _enum,
           charType,
          

           tabs,
           panels,
          
           analyseUrl,
           analyseBtn,
           saMain,
           saDeal,
           saLst,
           saPager,
          
           analysisTwitterImg,
           analysisTwitterName,
           analysisLocation,
           analysisFriendsCount,
           analysisFollowersCount,
           analysisLook,
           analysisResetBtn,
           analysisContent,
           analysisSourceName,
           analysisRepostCount,
           analysisCommentCount,
           
           spreadTrendReport,
           spreadTrendComment,
           startDate,
           endDate,
           
           spreadLevelTable,
           
           alreadyBtn,
           notBtn,
           alreadySpread,
           notSpread,
           
           userTabs,
           userVipNum,
           userGender,
           userFromsNum,
           userHot,
           fsArea,
           fsFans,
           fsAttitude,
           repostCount,
          
           analyseUrlTip,
           contentDiv;
         
    //私有方法
    //初始化方法，写自己模块的业务
	function Init() {
		NavigationInit ();
    	SetDefaultFont();
    	loadList();
    	Insert();
    }
    
    function TabFactory(index, monitorid) {
		if (index == 0) {
			GetSpreadViewTrend(monitorid);
		} else if (index == 1) {
		 	GetSpreadViewRetweetLevel(monitorid);
		} else if (index == 2) {
			AlreadySpread(monitorid);
    		NotSpread(monitorid);
		} else if (index == 3) {
			GetSpreadViewUser(monitorid);
		}
	}
     //导航条初始化方法,
	function NavigationInit () {
		navigationBars.Empty()
			.Add(key, 'NavToSA', '传播分析')[oL]();
	}
	function NavigationDeal() {
		navigationBars.Add(key, 'NavToDeal', '查看分析')[oL]();
	}
	//设置默认文本框默认值
	function SetDefaultFont() {
		analyseUrl.txt({text: analyseUrlTip });
	}
	
	//插入一条分析微博信息
	function Insert () {
		analyseBtn[EV](CK, function () {
			if(IsRegExp()) {
				var url = $[TM](analyseUrl.val());
				$(BY)[AJ]({
					param: { action: 'spreadAnalysis.insert', methodName: 'save', url: url},
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
							$[MB]({ content: '只能分析原帖微博', type: 2 });
						} else if (data == -4) {
							$[MB]({ content: 'url不正确或账号被封', type: 2 });
						} else if (data > 0) {
							SetDefaultFont();
							loadList();
						}
					}
				});
			}
		});
	}
	
	//删除一条分析微博信息
	function Delete (spreadid) {
		$(BY)[AJ]({
			param: { action: 'spreadAnalysis.insert', methodName: 'delete', spreadid: spreadid},
			dataType: 'text',
			success: function (data) {
				// 0：删除失败，数据库错误提示，>0：删除成功
				 if (data == 0) {
					$[MB]({ content: '删除分析失败,请与管理员联系(插入数据库错误)', type: 2 });
				} else {
					loadList();
				}
			}
		});
	}
	
	function IsRegExp() {
		
		var val = $[TM](analyseUrl.val());
		if (val == analyseUrlTip) val = val[RP](analyseUrlTip, '');
		
	   	//IsNull验证
        if (val == '') { alert('请输入新浪微博Url'); return FE; }
        //是否为新浪微博Url验证
        val = val[RP]('http://', '');
        var domain = val.substring(0, val.indexOf('/'));
        if (domain != 'weibo.com') { alert('只能输入新浪微博Url'); return FE; }
		return TE;
	}
	
	//获取分析列表
	function loadList() {
		saLst[LS]({
            width: 1000,
            id: 'saList1',
            pager: saPager,
            param: { action: 'spreadAnalysis.getList' },
            templates: [{ html:'<div class="cont_list"><div class="cont_photo"><img src="" class="saAccountImg" width="50" height="50" /><div class="photo_allInfor com_none saInfo"></div></div><div class="cont_all"><span class="cont_font saAccountName"></span><br /><span  class="saContent">{0}</span></div>'}, //内容
                      	        { html: '<div class="com_clear"></div><div class="cont_do"><div class="cont_doleft"><span>发布时间:</span><span class="saPublishDate">{0}</span>'}, //发布时间
                      	        { html: '<span>分析时间:</span><span class="saPublishDate">{0}</span>'}, //分析时间
                                { html: '<span class="saRepost">转发[{0}]</span>'}, //转发数
                                { html: '<span>|</span><span class="saComment">评论[{0}]</span></div>' },//评论数
                                { html: base.FormatPmi('{pmi:cbfx_monitor_2}') }, //删除分析按钮ID  //<div class="cont_doright rn_rightBotton rn_rightBotton2 deleteBtn" spreadid="{0}">删除监控</div>
                                { html: '<div class="cont_doright rn_rightBotton rn_rightBotton1 lookBtn" spreadid="{0}"'},
                                { html: ' publishDate="{0}">查看分析</div></div></div></a>'}], //查看按钮
            columns: [{ field: 'text'},
        	 			   { field: 'createtime'},
            	 		   { field: 'analysisTime'},
                           { field: 'repostCount'},
                           { field: 'commentCount' },
                           { field: 'id'},
                           { field: 'id'},
                           { field: 'createtime'}],
            onComplete: function (This, refresh, data) {
            	if (data.rows[LN] == 0) {
            		saPager.html('');
            	}
            	//设置账号头像、昵称
				var accountImg = $('.saAccountImg', This),
            		  accountName = $('.saAccountName', This),
            		  lookBtn = $('.lookBtn', This);
				accountImg[EH](function (i, item) {
        		   	var $this = $(this),
        		   		  index = accountImg[ID]($this),
        		   		  dataRow = data.rows[index],
        		   		  account = dataRow.account;
   		   			$this[AT]('src', account.imgurl);
   		   			accountName.eq(i).text(account.name);
   		   			//设置 查看分析按钮
   		   			if (dataRow.isAnalysised) {
   		   				lookBtn.eq(index).text('查看分析');
   		   			} else {
   		   				lookBtn.eq(index).text('正在分析...');
   		   			}
				});
				
				//显示用户信息
            	var accountInfo = $('.saInfo', This);
            	accountInfo[EH](function() {
					var $this = $(this),
						  index = accountInfo[ID]($this),
						  accountRow = data.rows[index].account;
					base.AccountTip($this, accountRow, _enum.ModuleType.SpreadAnalysis);
				});
            	
            	//点击分析按钮
            	lookBtn[EV](CK, function () {
            		var $this = $(this),
            			  id = $this[AT]('spreadid');
            		if ($this.text() == '查看分析') {
				      	startDate = $.addDate('d',0, $this[AT]('publishDate')).Format('YYYY-MM-DD');
				      	endDate = $.addDate('d', 30, startDate).Format('YYYY-MM-DD');
	            		saMain.hide();
			    		saDeal.show();
			    		NavigationDeal();
			    		ResetSpread(id);
			    		StartSpread(id);
            		}
		    	});
		    	
		    	//删除分析按钮
		    	$('.deleteBtn', This)[EV](CK, function () {
		    		var $this = $(this),
		    			  id = $this[AT]('spreadid');
		    		 $[MB]({
	    		 		content: '是否真的删除该条分析', 
	    		 		type: 4,
	    		 		onAffirm: function(state) {
	    		 			if(state) {
	    		 				Delete(id);
	    		 			}
	    		 		}
    		 		  });
		    	});
            }
		});
	}
	//开始分析
	function StartSpread(id) {
		base.OnChangeTab(tabs, panels, 'zhao_back', function(index) {
    		TabFactory(index, id);
    	}, TE);
		GetSpreadAnalysisInfo(id);
	}
	
	//重新分析
	function ResetSpread(id) {
		analysisResetBtn[EV](CK, function() {
			var $this = $(this);
			$this.text('正在分析...');
			$(BY)[AJ]({
				param: { action: 'spreadAnalysis.reset', monitorid: id },
				dataType: 'text',
				success: function (data) {
					if(data) {
						$this.text('正在分析...');
						//StartSpread(id);
					}
				}
			});
		});
	}
	
	//获取传播分析微博账号以及微博信息方法
	function GetSpreadAnalysisInfo(id) {
		$(BY)[AJ]({
			param: { action: 'spreadAnalysis.info', id: id },
			success: function (data) {
				var account = data.account;
				
				analysisTwitterImg[0].src = account.imgurl;
				analysisTwitterName.text(account.name);
				analysisLocation.text(account.area);
				analysisFriendsCount.text(account.friends);
				analysisFollowersCount.text(account.followers);
				analysisLook[0].href = data.url;
				
				var text = data.text;
				analysisContent.text(text);
				analysisSourceName.text(data.source);
				analysisRepostCount.text(data.repostCount);
				repostCount = data.repostCount;
				analysisCommentCount.text(data.commentCount);
				analysisResetBtn.text('重新分析');
			}
		});
	}
	
	//获取传播分析数据方法
	function GetSpreadViewTrend(id) {
		GetSpreadViewTrendReport(id);
		GetSpreadViewTrendComment(id);
	}

	//传播分析转发图表数据
	function GetSpreadViewTrendReport(id) {
		spreadTrendReport.html('')[LG]();
		$(BY)[AJ]({
			param: { action: 'spreadAnalysis.getTrendReport', monitorid: id, startTime: startDate, endTime: endDate },
			success: function (data) {
				base.Char(spreadTrendReport, charType.Line, data, 0);
			}
		});
	}	
	
	//传播分析评论图表数据
	function GetSpreadViewTrendComment(id) {
		spreadTrendComment.html('')[LG]();
		$(BY)[AJ]({
			param: { action: 'spreadAnalysis.getTrendComment', monitorid: id, startTime: startDate, endTime: endDate },
			success: function (data) {
				base.Char(spreadTrendComment, charType.Line, data, 0);
			}
		});
	}
	
	//获取转发层级
	function GetSpreadViewRetweetLevel(id) {
		spreadLevelTable.html('')[LG]();
		if (repostCount != 0) {
			$(BY)[AJ]({
				param: { action: 'spreadAnalysis.getSpreadLevel', monitorid: id},
				success: function (data) {
					if(data[LN] > 0) {
						data.sort(function(a, b) { return a.level -b.level; }); // 按照层级排序
						data = data.slice(0, 7); // 最大到7级
						DrawLevelTable(data);
					}
				}
			});
		} else {
			base.not(spreadLevelTable);
		}
	}
	
	//画转发层级
	function DrawLevelTable(data) {
		var thTemplate = '<th>层级</th><th>该层转发人数</th><th>覆盖人数</th><th>关键用户</th>',
			  //层级，转发人数，覆盖人数，用户dl模板集合，更多
			  levelTemplate = '<tr><td class="event_no"><span class="event_numBack">{0}</span></td><td class="event_time">{1}</td><td class="event_num">{2}</td><td><div class="qushi_Plist qushi_PlistStyle1">{3}</div>{4}</td></tr>',
			  moreTemplate = '<div class="dl_listMore userMoreLook" isclose="1"><span>+</span> 查看更多</div>',
			  //用户头像路径，用户名称，转发数, ucode, 平台类型
			  dlTemplate = '<dl><dt><img src="{0}" width="50"height="50" class="levelImg" ucode="{3}" twitterType="{4}" /></dt><dd>{1}</dd><dd>被转<span class="dd_color">[{2}]</span></dd></dl>';
			  html = '';
		html = thTemplate;
		$[EH](data, function(i, dataRow) {
			var accounts = dataRow.accounts;
			var dlHtml = '',
				  moreHtml = '';
			if(accounts[LN] > 0) {
				$[EH](accounts, function(j, accountRow) {
					var name = accountRow.name;
					if(name[LN] > 5) name =name.substring(0, 5);
					dlHtml += $[FO](dlTemplate, accountRow.imgurl, name, accountRow.friends, accountRow.ucode, accountRow.twitterType);
				});
				if (accounts[LN] > 5) moreHtml = moreTemplate;
			}
			html += $[FO](levelTemplate, dataRow.level, dataRow.personNum, dataRow.followers, dlHtml, moreHtml);
		});
		spreadLevelTable.html($[FO]('<table class="web_table"  cellpadding="0" cellspacing="0">{0}</table>', html));
		
		//点击查看按钮
		$('.userMoreLook', spreadLevelTable)[EV](CK, function() {
			var $this = $(this),
				  isclose = $this[AT]('isclose');

			//展开
			if (isclose == '1') {
				$this.prev()[RC]('qushi_PlistStyle1')[AC]('qushi_PlistStyle2');
				$this[AT]('isclose', '0')[FD]('span').text('-');
			} else {
				$this.prev()[RC]('qushi_PlistStyle2')[AC]('qushi_PlistStyle1');
				$this[AT]('isclose', '1')[FD]('span').text('+');
			}
		});
		
		//levelImg
		base.AccountTipSelf($('.levelImg', spreadLevelTable))
	}
	
	//关键用户，已传播
	function AlreadySpread(id) {
		alreadyBtn[EV](CK, function() {
			var $this = $(this),
				  type = $this[AT]('type'),
				  styleName = 'selW_selected';
		  	alreadyBtn[RC](styleName);
		  	$this[AC](styleName);
			alreadySpread.html('')[LG]();
			$(BY)[AJ]({
				param: { action: 'spreadAnalysis.keyAlready', monitorid: id, type: type},
				success: function (data) {
					if(data[LN] > 0) {
						DrawKeyTable(alreadySpread, data, TE);
					} else {
						base.not2(alreadySpread);
					}
				}
			});
		}).eq(0)[TR](CK);
	}
	
	//关键用户，未传播
	function NotSpread(id) {
		notBtn[EV](CK, function() {
			var $this = $(this),
				  type = $this[AT]('type'),
				  styleName = 'selW_selected';
		  	notBtn[RC](styleName);
		  	$this[AC](styleName);
			notSpread.html('')[LG]();
			$(BY)[AJ]({
				param: { action: 'spreadAnalysis.keyNot', monitorid: id, type: type},
				success: function (data) {
					if(data[LN] > 0) {
						DrawKeyTable(notSpread, data, FE);
					} else {
						base.not2(notSpread);
					}
				}
			});
		}).eq(0)[TR](CK);
	}
	
	//用户分析
	function  GetSpreadViewUser(id) {
		userTabs[EV](CK, function() {
			var $this = $(this),
				  type = $this[AT]('type'),
				  styleName = 'selW_selected';
			
			userTabs[RC](styleName);
			$this[AC](styleName);
			
		    GetUserData(userVipNum, 'spreadAnalysis.vipNum', id, type, charType.Pie);
		   	GetUserData(userGender, 'spreadAnalysis.gender', id, type, charType.Pie);
		   	GetUserData(userFromsNum, 'spreadAnalysis.fromsNum', id, type, charType.Pie);
		   	GetUserHotData(id, type);
		   	GetUserData(fsArea, 'spreadAnalysis.area', id, type, charType.Column);
		   	GetUserData(fsFans, 'spreadAnalysis.fansHistogram', id, type, charType.Column);
		   	if(type == '1') {
		   		GetUserData(fsAttitude, 'spreadAnalysis.retweetAttitude', id, type, charType.Pie);
		   	}
		   	
		}).eq(1)[TR](CK);
	}
	
	//获取用户分析数据(Ajax)
	function GetUserData(div, actionName, id, type, charType) {
		div.html('')[LG]();
		$(BY)[AJ]({
			param: { action: actionName, monitorid: id, type: type},
			success: function (data) {
				if((charType ==2 && data.series.data[LN] > 0) || (charType == 0 &&data.series[LN] > 0)) {
					base.Char(div, charType, data, 0);
				} else {
					base.not2(div);
				} 
			}
		});
	}
	
	//获取用户分析热门标签数据(Ajax)
	function GetUserHotData(id, type) {
		$(BY)[AJ]({
			param: { action: 'spreadAnalysis.hotWord', monitorid: id},
			success: function (data) {
				if(data[LN] == 0) {
					base.not2(userHot);
					return;
				}
				userHot.html('').jQCloud({
					collection: data
				});
			}
		});
	}
	
	//画关键用户方法
	function DrawKeyTable(panel, data, isRetweetTime) {
		var json = [{ caption: '序号', field: 'id', type: 'int', width: '5%'},
                           { caption: '昵称', field: 'name', type: 'string', width: '25%'},
                           { caption: '地区', field: 'location', type: 'string', width: '25%' },
                           { caption: '粉丝数', field: 'followes', type: 'int', width: '10%' },
                           { caption: '用户类型', field: 'userType', type: 'string', width: '10%'},
                           { caption: '二次转发', field: 'retweets', type: 'int', width: '10%'}];
       var id = 1;                  
       if(isRetweetTime) {
       		id = 2;
       		json.push({ caption: '转发时间', field: 'retweetTime', type: 'date', width: '15%'});
       }
		panel.table({
            width: 998,
			isPager: FE,
            id:'_tables' + id,
            rows: '',
            data: data,
            columns: json
		});
	}
	
	
    return {
    	IsLoad: FE,
        OnLoad: function (_ContentDiv) {
            var t = this;
            contentDiv = _ContentDiv;
            $('<div />').load('modules/spreadAnalysis/spreadAnalysis.htm', function () {
            	base = context.Base;
            	contentDiv[AP](base.FormatPmi($(this).html()));
            	t.IsLoad = TE;
                $key = $('#' + key);
                navigationBars = context.NavigationBars;
                _enum = context.Enum;
                charType = _enum.CharType;
                
                tabs = $('.tab', $key); //页签Div集合
                panels = $('.panel', $key); //页签对应的内容Div集合
                
                analyseUrl = $('#analyseUrl', $key); //url文本框对象
                analyseBtn = $('#analyseBtn', $key); //立即分析按钮对象
                saMain = $('#spreadAnalusis_main', $key); //分析列表Div对象
                saDeal = $('#sp_all', $key); //分析详细Div对象
                saLst = $('#saList', $key); //画列表Div对象
                saPager =$('#saPager', $key); //列表分页对象
                
                analysisTwitterImg = $('#twitterImg', $key); //微博账号头像
          		analysisTwitterName = $('#twitterName', $key); //微博账号名称
          		analysisLocation = $('#location', $key); //微博账号地点
          		analysisFriendsCount = $('#friendsCount', $key); //微博账号关注数
          		analysisFollowersCount  = $('#followersCount', $key); //微博账号粉丝数
                analysisLook = $('#look', $key), //查看查看微博a对象
                analysisResetBtn = $('#resetBtn', $key); //重新分析按钮
	            
	            analysisContent = $('#content', $key), //微博展示内容span对象
	            analysisSourceName = $('#sourceName', $key), //微博展示来源span对象
	            analysisRepostCount = $('#repostCount', $key), //微博展示转发数span对象
	            analysisCommentCount = $('#commentCount', $key), //微博展示评论数span对象
	            
	            spreadTrendReport = $('#spreadTrendReport', $key); //传播分析转发趋势分析div对象
           		spreadTrendComment = $('#spreadTrendComment', $key); //传播分析评论趋势分析div对象
           		
           		spreadLevelTable = $('#spreadLevelTable', $key); //转发层级table对象
           		
           		alreadyBtn = $('.alreadyBtn', $key); //已传播按钮集合
	            notBtn = $('.notBtn', $key); //未传播按钮集合
	            alreadySpread = $('#alreadySpread', $key); //显示已传播表格div对象
	            notSpread = $('#notSpread', $key); //显示未传播表格div对象
                
                userTabs = $('.userTabs', $key); //用户分析tab页签
                userVipNum = $('#userVipNum', $key); //用户分析认证比例div
           	    userGender = $('#userGender', $key); //用户分析性别比例div
           	    userFromsNum = $('#userFromsNum', $key); //用户分析来源比例div
           	    userHot = $('#userHot', $key); //用户热门标签div
           	    fsArea = $('#fsArea', $key); //用户地域分析div
           	    fsFans = $('#fsFans', $key); //用户粉丝数分析div
           	    fsAttitude = $('#fsAttitude', $key); //用户转发态度div
           	    repostCount = 0; //转发数
                
                analyseUrlTip = '请输入您要分析的微博地址';
                Init();
            });
            return t;
        },
        NavToSA: function () {
        	saMain.show();
    		saDeal.hide();
    		Init();
    		NavigationInit();
        },
        NavToDeal: function () {

        },
        //显示模块方法
        Show:function() {
        	$key.show();
        	saMain.show();
    		saDeal.hide();
        	Init();
        	NavigationInit();
        }
    };
});