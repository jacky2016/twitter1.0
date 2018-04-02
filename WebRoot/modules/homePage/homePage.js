//首页
define(['portal'], function (context) {
    //定义变量
    var key = 'homePage',
          $key, //当前的作用域
          base,
          navigationBars,
          _enum,
          charType,
          
          hotTab,
          hotItem,
          
          contentDiv;
          
	//私有方法
	//初始化方法，写自己模块的业务
	function Init() {
            NavigationInit();
            context.CurrentUser.GetFaces($.noop);
			GetTwitterDataAjax();
    }
	//导航条初始化方法
	function NavigationInit () {
    	navigationBars.Empty()
    	.Add(key, 'NavToHP', '首页展示')[oL]();
    }
	//根据微博类型获取微博名称，type: 微博类型
	function ParseName (type) {
		var tt = context.Enum.TwitterType;
		switch (type) {
			case tt.Sina: return '新浪';
			case tt.Tencent: return '腾讯';
	    	case tt.People: return '人民';	
			default: return '';
		}
	}

	//获取微博信息方法(Ajax)，假实现，需要修改
	function GetTwitterDataAjax() {
    	$(BY)[AJ]({
    		param: {action: 'home.GetAccounts'},
    		success: function (data) {
    			if(data.errCode != 0) {
    				$[MB]({ 
    					content: data.message +'等账号被封,不能使用', 
    					type: 2 
					});
				}
				var _data = data._data;
				if(_data != UN) {
	    			var dataDiv = DrawUserData(_data);
	    			
	    				$(BY)[AJ]({
						   param: { action: 'sysManager.GetMethodAccount' },
						   success: function(data) {
							   var array = [];
							   if(data != null) {
								   for(var i=0;i<data.length;i++){
								   		array.push({accountid: data[i].id, uid: data[i].uid, name: data[i].name, type: data[i].type,isExpire: data[i].flag});
								   }
							   }
							   SetUserToAccount(array);
							   if(_data[LN] > 0) {
				    				DrawTwitterWeekColumn();
									DrawTwitterToDayPie();
									LikeWeekLine();
									LoadHotInfo();
				    			}
							}
						});
						
						
	    			
	    			
				}
				context.isClick = TE;
    		}
    	});
    }
    //设置CurrentUser对象的accounts数组对象
    function SetUserToAccount(data) {
    	$[EH](data, function (i,item) {
    		context.CurrentUser.Accounts.push({id: item.accountid, uid:item.uid, name: item.name, type: item.type, isExpire: item.isExpire});
    	    if(!item.isExpire) {
    	    	base.isAllExpire = FE;
    	    }
    	});
    }
    //画用户的微博信息方法，data: 数据
    function DrawUserData(data) {
    	var titleHtml = '<div class="box_show ric_show hpUserDataLst" name="{1}" accountid="{4}"  style="display:{3};"><h1><span class="ric_titleBack ric_backImg com_fLeft"></span><span class="com_fLeft">{0}微博账号[</span><span class="zh_color com_fLeft">{1}</span><span class="com_fLeft">]日常统计</span><span class="do_Works do_Close" accountid="{4}"></span><span class="do_Works do_hide clickt" blockid="{10}"></span><span class="ric_downLoad"></span></h1><div class="tj_show tj_show2" style="display:{2};"><div class="tj_weibo"><h2 class="weibo_back  h2_center">微&nbsp;&nbsp;博</h2><h2 id="tcH2" class="h2_center">{5}</h2></div><div class="tj_fensi"><h2 class="send_back  h2_center">转发数</h2><h2 id="trcH2" class="h2_center">{6}</h2></div><div class="tj_see"><h2 class="say_back  h2_center">评论数</h2><h2 id="ccH2" class="h2_center">{7}</h2></div><div class="tj_send"><h2 class="fensi_back  h2_center">粉&nbsp;&nbsp;丝</h2><h2 id="lcH2" class="h2_center">{8}</h2></div><div class="tj_say"><h2 class="see_back h2_center">关&nbsp;&nbsp;注</h2><h2 id="acH2" class="h2_center">{9}</h2></div></div></div><div class="com_clear"></div>',
    		   html = '';
		$[EH](data, function (i,item) {
				html += $[FO](titleHtml, ParseName(item.Type), 
					item.name, 
					item.expand ? 'block' : 'none', 
					item.closed ? 'none' : 'block',
					item.accountid,
					item.statusCount, 
					item.transpond, 
					item.comment, 
					item.like, 
					item.attention,
					item.id);
		});
		$('#HPUserData', $key).html(html);
		
		Fold();
		DeleteUserBlock(data);
		OpenSettingWindow(data);
		return $('.hpUserDataLst', $key);
    }
    //显示用户的微博信息方法，data: 数据
    function ShowUserData(data) {
    	var  dataDiv = $('.hpUserDataLst', $key);
    	$[EH](data, function (i, item) {
			dataDiv.eq(i)[item.closed ? 'hide' : 'show']();

		});
    }

    //获取微博数据(AJAX、API)，currentDiv: 当前Div, 微博用户名称
    // 假实现，需要修改
    function GetTwitterDataApiAjax(currentDiv, twitterName, accountid) {
    	base.AJ({action: 'home.GetCountAPI', name: twitterName, accountid: accountid}, function (d) {
    		currentDiv[AT]('isload', '1');
    		$('#tcH2', currentDiv).text(d.statusCount);
    		$('#trcH2', currentDiv).text(d.transpond);
    		$('#ccH2', currentDiv).text(d.comment);
    		$('#lcH2', currentDiv).text(d.like);
    		$('#acH2', currentDiv).text(d.attention);
    		$('.ric_downLoad', currentDiv).html('');
		});
    }
    //折叠方法
	function Fold() {
    	$('.clickt', $key)[EV](CK, function() {
    		var $this = $(this),
    			   expand = !$this[PT]()[NT]().is(':'+VB);
    		$(BY)[AJ]({
    			param: {action: 'home.updateExpand', id: $this[AT]('blockid'), expand: expand},
    			dataType: 'text',
    			success: function (state) {
    				if(state) {
    					$this[PT]()[NT]()[STG](); 
    				}
    			}
    		});
		 });
    }
    //异步读取微博特效
    function UserAjaxLoad(div) {
    	div[FD]('.ric_downLoad')[LG]({lines: 10, length: 4, width: 3, radius: 5})[FD]('>div').css('top', '15px');
    }
    //删除一个块，data: 数据
    function DeleteUserBlock(data) {
    	var closeBtn = $('.do_Close', $key);
    	closeBtn[EV](CK, function() {
    		var t = $(this),
    			   index = closeBtn[ID](t),
    			   accountid  = t[AT]('accountid');

    		//异步持久化关闭/显示状态
    		$(BY)[AJ]({
    			param: {action: 'home.updateClose', id: accountid, close: TE},
    			dataType: 'text',
    			success: function (state) {
    				if(state) {
    					t[PT]()[PT]().hide();
    					data[index].closed = TE;
    					var html = DrawSettingContent(data);
    					$('#AccountSettings').html(html);
    				}
    			}
    		});
    	});
    }
    //设置窗体内容初始化，data: 数据
    function DrawSettingContent(data) {
    	var liTemplate= '<li class="Mzhao_list"><input type="checkbox" id="inMset{3}" class="Mzhao_sel cb" value="{0}" {2} name="{1}" /><label for="inMset{3}" class="mynum_type{4} Mzhao_cont" title="{5}">{1}</label></li>',
    		   settingHtml = '';
    	$[EH](data, function(i, item) {
    		//var name  = item.name[LN] > 6 ? item.name.substring(0, 6) : item.name;
    		settingHtml += $[FO](liTemplate, item.id, item.name, item.closed ? '' : CD + '=' + CD, i, item.type, item.name);
    	});
    	return settingHtml;
    }
    //打开设置窗体，data: 数据
    function OpenSettingWindow(data) {
    	$('#openBtn', $key)[EV](CK, function() {
    		var open = $('#open', $key);
    		var html = DrawSettingContent(data);
    		open[FD]('#AccountSettings').html(html);
    		if(open.css('right') == '-180px') {
    			open[AM]({'right': '-20px'}, 300);
    		}else{
    			open[AM]({'right': '-180px'}, 300);
    		}
    		SettingOption(open, data);
	    });
    }
    //操作窗体，settingDiv: 设置窗体Div对象，data: 数据
    function SettingOption(settingDiv, data) {
    	var cb = $('.cb', settingDiv)
    	$('.cb', settingDiv)[EV](CK, function() {
    		var index = cb[ID]($(this));
    		data[index].closed=  !this[CD];
    		//异步持久化关闭/显示状态
    		$(BY)[AJ]({
    			param: {action: 'home.updateClose', id: this.value, close: data[index].closed},
    			dataType: 'text',
    			success: function (state) {
    				if(state) {
    					ShowUserData(data);
    				}
    			}
    		});
		});
    }
    
    //关闭窗体事件
    function CloseWindow() {
    	$[RM]({ obj: '#_hp' });
    	$[RM]({ obj: '#' + JLY });
    }
    
    //显示热点list数据
    function LoadHotInfo() {
    	hotTab[EV](CK, function() {
    		var $this = $(this),
    			   twitterType = $this[AT]('type'),
    			   styleName = 'zhao_back';
    		hotItem[LG]();	   
    		hotTab[RC](styleName);
    		$this[AC](styleName);
			HotAjax($this, twitterType);
    	}).eq(1)[TR](CK);
    }
    
    //获取热点信息Ajax方法,div:要缓存数据的对象,twitterType:微博类型
    function HotAjax(div, twitterType) {
    	div[AJ]({
    		isCache: TE,
    		cacheName: 'hot.' + twitterType,
    		param: {action: 'home.hot', type: twitterType},
    		success: function (data) {
    			if(data[LN] > 0) {
    				base.DrawHot(hotItem, ['text', 'link', 'color'], data);
    			} else {
    				base.not2(hotItem);
    			}
    		}
    	});
    }
	
	//画一周舆情监测统计
	function DrawTwitterWeekColumn() {
		$(BY)[AJ]({
			param: { action: 'home.weekPubSentiment'},
			success: function (data) {
				base.Char($('#twitterWeekColumn', $key), charType.Column, data, 60);
			}
		});
	}
	
	function DrawTwitterToDayPie() {
		$(BY)[AJ]({
			param: { action: 'home.dayPubSentiment'},
			success: function (data) {
				var div = $('#twitterDayPie', $key);
				if(data.series.data[LN] >0) {
					base.Char(div, charType.Pie, data);
				} else {
					base.not2(div);
				}
			}
		});
	}
	
	function LikeWeekLine() {
		$(BY)[AJ]({
			param: { action: 'home.weekLike'},
			success: function (data) {
				base.Char($('#likeWeekLine', $key), charType.Line, data, 60);
			}
		});
	}
    
    return {
    	IsLoad: FE,
        OnLoad: function (_ContentDiv) {
            var t = this;
            	  contentDiv = _ContentDiv,
            $('<div />').load('modules/homePage/homePage.htm', function () {
            	base = context.Base;
            	contentDiv[AP](base.FormatPmi($(this).html()));
            	
            	t.IsLoad = TE;
				$key = $('#' + key);
                navigationBars = context.NavigationBars;
                _enum = context.Enum;
                charType = _enum.CharType;
                hotTab = $('.hotTab', $key); //热点块的页签对象
                hotItem = $('#hotItem', $key); //显示热点信息的ul对象
                Init();
            });
            return t;
        },
        //导航条调用方法
        NavToHP: function() {
        	this.Show();
        },
        //显示模块方法
        Show:function() {
        	NavigationInit();
        	$key.show();
        }
    };
});