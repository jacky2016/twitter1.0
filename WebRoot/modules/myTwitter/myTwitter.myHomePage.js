//我的首页
define(['portal'], function (context) {
    //定义变量
    var key = 'myHomePage',
          $key, //当前的作用域
          navigationBars,
          base,
          _enum,

          textareaInfo,          
          wordCount,
          
          keywordInput,
          searchInfo,
          topic,
          
          publish,
          _selectDatePicker,
          
          currentAccountStart,
          
          panels,
          
          selectPic,
          picPanel,
          picColse,
          uploadifyTip,

          selectDatePicker,
          datePickerPanel,
          datePickerInput,
          datePickerClose,
          timePickerDiv,
          timePickerOK,
          
          selectAccount,
          accountPanel,
          accounts,
          accountCollection,
          
          selectFace,
          facePanel,
          faceClose,
          faceItem,
          
          lookLst,
          tabItem,
          pager,
          
          textareaTip,
          release,
          contentDiv;
          
    //private method
	function Uploadify() {
		
		if (!NA[MA](/msie [6]/i)) {
			$("#uploadify").uploadify({
	             swf: xkPath + 'scripts/plugin/jquery.uploadify.swf',
	            //后台处理的页面
	            uploader: xkPath+ 'upload.action?cid=' + $.cookie("XUNKUID"),
	            buttonClass: "spic_ImgsAdd",
	            buttonText: '',
	            height: 100,
	            width: 100,
	            fileTypeExts: '*.gif; *.jpg; *.png; *.jpeg',
	            queueID: 'fileQueue',
	            auto: TE,
	            multi: FE,
	            queueSizeLimit: 1,
	            simUploadLimit: 1,
	            removeCompleted: FE,
	            onUploadStart: function (file) {
	            	$('#' + file.id)[FD]('.data')[LG]();
	            },
	            onUploadSuccess: function (file, data, response) {
	            	//超出图片容量大小
	            	if(data == 'sizeError') {
	            		$('#' + file.id)[RM]();
	            		uploadifyTip.text('您上传的图片容量大小超出限制,请重新上传图片');
	            	} else {
	            		uploadifyTip.text('');
	            		
	            		var imageArr = data.split(',');
	            		
		                var pics = '.uploadify-queue-item',
		                      uploadify = $('#uploadify'),
		                      v = 'visibility';
		                var pathImage = imageArr[0][RP]('$', userWeiboImagePath);
		                $('#' + file.id)[AT]('imageid', imageArr[1])[FD]('.data').html('<image src="' + pathImage  + '" style="width:60px; height:60px;" /><div style="clear:right; font:12px;"></div>');
		                if ($(pics)[LN] == base.imageMaxCount) uploadify.css(v, 'hidden');
		
		                $('a', $('#fileQueue'))[EV](CK, function () {
		                    $(this)[PT]()[PT]()[RM]();
		                    if ($(pics)[LN] < base.imageMaxCount) {
		                    	uploadify.css(v, 'visible');
		                    	$('#uploadify-button').show();
		                    }
		                });
		                $('.uploadify-queue-item').corner('15px');
	            	}
	            }
	        });
	        $(".uploadify-button").corner('15px');
	        if(picPanel[FD]('img')[LN] == base.imageMaxCount) 
				$("#uploadify-button").hide();
		}
	}
   
	//私有方法
 	//初始化方法，写自己模块的业务
    function Init() {
    	NavigationInit();
		accountCollection = base.GetAccountCollection();
		base.CurrentAccountPicker(currentAccountStart, 'currentAccountPanel', accountCollection, panels, function (flag) {
				if(accountCollection[LN] > 0) {
					hideDP();
					SetDefaultFont();
					
					TopicOption();
					
					OpenPanel(selectDatePicker, datePickerPanel, DateTimeOption);
					OpenPanel(_selectDatePicker, datePickerPanel, DateTimeOption);
					
					base.LimitWord(textareaInfo, wordCount, release, flag);
					if (!flag && $('.noPmi ', $key)[LN] == 0) {
						
						base.FacePicker(selectFace, facePanel, textareaInfo, panels);
						OpenPanel(selectPic, picPanel, PicOption);
						
					}
					
					
					
					ReleaseInfo();
			
					//列表业务
					OnChangeTab(base.isAllExpire);
					// 授权到期
					if (flag) {
						release[RC]('send_over')[AC]('send_fail');
					}
				} else {
					lookLst.html('');
					pager.html('');
				}
		});
	
    }
    
    function hideDP() {
    	$('.myTw_show', $key)[EV](CK+'.datePicker', function(e) {
    		var id = e.target.id;
			if(id == '_selectDatePicker' || id == 'datePickerPanel' || id == 'selectDatePicker')
				datePickerPanel.show();
			else 
				datePickerPanel.hide();
		});
		
		$('.send_content', $key)[EV](CK+'.datePicker', function(e) {
			var id = e.target.id;
			if(id == '_selectDatePicker' || id == 'datePickerPanel' || id == 'selectDatePicker')
				datePickerPanel.show();
			else 
				datePickerPanel.hide();
		});
    }
    
    function hidePic() {
    	$('.myTw_show', $key)[EV](CK+'.pic', function(e) {
    		var id = e.target.id;
    		if(id == 'selectPic' || id == 'picItem' || id == 'uploadifyClose')
				picPanel.show();
			else 
				picPanel.hide();
    	});
    	
    	$('.send_content', $key)[EV](CK+'.pic', function(e) {
    		var id = e.target.id;
			if(id == 'selectPic' || id == 'picItem' || id == 'uploadifyClose')
				picPanel.show();
			else 
				picPanel.hide();
		});
    }
    
    //导航条初始化方法,
	function NavigationInit () {
    	navigationBars.Empty()
			.Add(key, 'NavToMP', '我的微博')
    		.Add(key, 'NavToMP', '我的首页')[oL]();
    }

    //打开面板
    function OpenPanel(btn, panel, fn) {
    	btn[EV](CK, function() {
    		panels.hide();
    		panel[TG]();
    		fn();
		});
    }
    //关闭面板
    function ClosePanel(btn) {
    	btn[EV](CK, function() {
    		$(this)[PT]().hide();
    		return FE;
		});
    }
    //图片面板后续操作
	function PicOption() {
		ClosePanel(picColse);
		Uploadify();
	}
    //时间面板后续操作
    function DateTimeOption() {
    	datePickerInput.datePicker({ minDate: $.getDate() });
    	var time = $.addDate('n', 10, $.getDateTime()).Format('hh:mm:ss'); //默认时间+10分钟
    	timePickerDiv.timePicker({  id: '_a1', defaultTime: time });
    	ClosePanel(datePickerClose);
    	ClosePanel(timePickerOK);
    }
   
  	//话题操作
	function TopicOption() {
		topic[EV](CK, function() {
			var txt = textareaInfo.val();
   			if(txt == textareaTip) {
   				txt = '';
   				textareaInfo.css('color', 'black');
   			}
			var start = txt[LN] + 1,
   				   txt = txt + '#请在这里设置话题#',
   				   ent = txt[LN] - 1;
   			textareaInfo.val(txt)	;
   			textareaInfo[TR](KU);
   			SetSelectRange(textareaInfo[0], start, ent);
   		});
	}
    
    // 获取选中的账号id
    function GetSelectAccountsArray() {
    	return $('.accountCB', accountPanel).map(function () { if(this[CD]) return this.value; }).get();
    }
    
    // 获取选择账号的名称
    function GetSelectAccountsNameArray() {
    	return $('.accountCB', accountPanel).map(function () { if(this[CD]) return $(this).next().text(); }).get();
    }
    
    //获取图片ID集合
    function GetImages() {
    	var images = [];
    	picPanel[FD]('img')[EH](function (){ 
    		images.push(this.src);
    	});
    	 return images.join(',');
    }
	
	//设置默认文本框默认值
	function SetDefaultFont() {
		if($('.noPmi ', $key)[LN] == 0) {
			release[RC]('send_fail')[AC]('send_over');
		}
		wordCount.html('您还可以输入<span class="font_num" >140</span>个字');
		textareaInfo.txt({text: textareaTip });
		keywordInput.txt({text: '查找我关注人的微博' });
		//重新设置默认账号
		base.AccountPicker('_my1', selectAccount, accountPanel, accountCollection , function(cball, cbItem) {
			cbItem[base.AccountIndex][CD] = TE;
		}, function() {
			
		}, panels);
		//设置立即发布默认值
		publish[0][CD] = TE;
		_selectDatePicker[0][CD] = FE;
	}

	//发布一条微博
	function ReleaseInfo() {
		release[EV](CK, function() {
			panels.hide();
			var $this = $(this),
				   param = {},
				   isclick = $this[AT]('isclick');
	        if(isclick == 'false') return;	
	        $this[AT]('isclick', 'false');
			if($this[AT]('class').indexOf('send_over') > -1 && IsRegExp()) {
			   	param.action = 'myTwitter.releaseInfoAction';
				param.content =  textareaInfo.val()[RP](textareaTip, ''); //内容
				param.sendType  = publish[0][CD] ? 0: 1; //发送方式类型,0:立即发送, 1:定时发送
				param.sendTime  = publish[0][CD] ? "": datePickerInput.val()+ ' ' + timePickerDiv[AT]('time'); //定时发送时间
				param.accountIDs = GetSelectAccountsArray().join(','); //获取选中的账号
				param.sendNames = GetSelectAccountsNameArray().join(','); //获取选择的账号名称
				param.images = GetImages(); //上传的图片集合
				
				if($.compareDate(param.sendTime, $.getDateTime()) >=0) {
					$[MB]({content: '定时发布时间不能小于当前时间', type: 2});
					$this[AT]('isclick', 'true');
					return;
				}
				
				$this[AJ]({
					param: param,
					dataType: 'text',
					success: function(data) {
						$this[AT]('isclick', 'true');
						if(data == '"发布成功"') {
							$[MB]({content: '发布成功', type: 0, isAutoClose: TE});
							SetDefaultFont();
							picPanel[FD]('#fileQueue').html('');
						} else {
							$[MB]({content: data, type: 2});
						}	
					}
				});
			} else {
				$this[AT]('isclick', 'true');
			}
		})
	}
	
	function IsRegExp () {
		//文本框空验证
		var val = $[TM](textareaInfo.val());
		if (picPanel[FD]('img')[LN] == 0 && val == textareaTip) val = val[RP](textareaTip, '');
		
	   	//IsNull验证
        if (val == '') { $[MB]({content: '请填写内容', type: 2}); return FE; }
		//选择账号验证
		if (GetSelectAccountsArray()[LN] == 0) {  $[MB]({content: '请选择账号', type: 2}); return FE; }
		return TE;
	}

	// 设置textarea的选中区域
	function SetSelectRange(textarea, start, end)
	{
		if (textarea.createTextRange != UN)// IE
		{
			var range = textarea.createTextRange(),
				   character = 'character';
			// 先把相对起点移动到0处
			range.moveStart(character, 0);
			range.moveEnd(character, 0);
			range.collapse(TE); // 移动插入光标到start处
			range.moveEnd(character, end);
			range.moveStart(character, start);
			range.select();
		} // if
		else if (textarea.setSelectionRange != UN)
	　{
	　　 textarea.setSelectionRange(start, end);
	　　 textarea.focus();
	　} // else
	} 
	
	//----------------------------------------------------列表业务------------------------------------------------------
	//切换页签方法
	function OnChangeTab(flag) {
		//点击页签
		tabItem[EV](CK, function() {
			var t = $(this),
				   styleName = 'selW_selected',
				   type = t[AT]('type'),
				   keyword = keywordInput.val()[RP]('查找我关注人的微博', '');
		    searchInfo[AT]('type', type);
			tabItem[RC](styleName);
			t[AC](styleName);
			LoadList(type, keyword, flag);
		}).eq(0)[TR](CK);
		//点击搜索按钮
		searchInfo[EV](CK, function() {
			var t = $(this),
				   type = t[AT]('type'),
				   keyword = $[TM](keywordInput.val()[RP]('查找我关注人的微博', ''));   
	        LoadList(type, keyword, flag);
		});
	}
	function LoadList(id, keyword, flag) {
		var accountid = $('#currentAccountInfo', $key)[AT]('accountid'),
		 	   uid = $('#currentAccountInfo', $key)[AT]('uid');
 	   	pager.html('');
		base.DataBind({
			panel: lookLst, 
			pager: pager,
			isFlag: flag,
			id: id, 
			isSelectAccount: TE,
			moduleType: _enum.ModuleType.MyHomePage,//模块入口类型
			template: base.FormatPmi('{pmi:yjfw_yjxxinsert_1}{pmi:yqzs_collect_1}{pmi:wdsy_repost_1}{pmi:wdsy_comment_1}'),// <span class="alertBtn" alertid="0" url="">预警</span><span>|</span>
			param: {action :'myTwitter.loadList', loadListType: id, keyword: keyword, accountid: accountid, uid: uid}, 
			onComplete: function (This, refresh, data) {
				//alert($[JO](data));
				
			},
			onSend:function (btn, txt, isChecked, accountCollection, postID, url,  type, twitterID, closeBtn) {
				var param = {};

				param.action = type == 'repost'? 'myTwitter.saveReport': 'myTwitter.saveComment';
				
				
				param.isChecked = isChecked;
				if (type == 'repost' ) {
					//不填转发内容的时候，默认值
					if (txt == '') {
						txt = '转发微博';
					}
				} else {
					if (txt == '') {
						$[MB]({
							content: '评论内容不能为空', 
							type: 2
						});
						btn[AT]('isclick', TE);
						return;
					}
				}
				param.content = txt;
				var accountids = [], platforms = [], accountNames = [];
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
							$[MB]({content: '提交成功', type: 0, isAutoClose: TE});
							closeBtn[TR](CK); //关闭子页面
						} else {
							$[MB]({content: data, type: 2});
						}
						btn[AT]('isclick', TE);
					}
				});
			}
		});
	}

	//公开到其他模块的接口
    return {
    	IsLoad: FE,
        OnLoad: function (_ContentDiv) {
            var t = this;
            contentDiv = _ContentDiv;
            $('<div />').load('modules/myTwitter/myTwitter.myHomePage.htm', function () {
            	base = context.Base;
            	contentDiv[AP](base.FormatPmi($(this).html()));
            	t.IsLoad = TE;
                $key = $('#' + key);
                _enum = context.Enum;
                navigationBars = context.NavigationBars;
                textareaInfo = $('#textareaInfo', $key); //多行文本框输入区对象
                wordCount = $('#wordCount', $key); //剩余字数Span
                
                keywordInput = $('#keywordInput', $key); //关键字文本框对象
                searchInfo = $('#searchInfo', $key); //搜索按钮
                topic = $('#topic', $key); //话题按钮
                
                publish = $('#publish', $key); //立即发送
          	    _selectDatePicker = $('#_selectDatePicker', $key); //定时发送
                
                currentAccountStart = $('#currentAccountStart', $key); //当前账号开始Div
                
                panels = $('.panels', $key); //所有面板集合
                
                picPanel = $('#picPanel', $key); //图片上传面板Span
                selectPic = $('#selectPic', $key); //打开图片窗体Span
                picColse = $('#picColse', $key); //关闭图片面板按钮Div
                uploadifyTip = $('#uploadifyTip', $key); //图片上传提示Span对象
                
                selectDatePicker = $('#selectDatePicker', $key); //点击定时发布Span
                datePickerPanel = $('#datePickerPanel', $key); //定时发布面板Div
                datePickerInput = $('#datePickerInput', $key); //选择日期Input对象
                timePickerDiv = $('#timePickerDiv', $key); //选择时间Div
                datePickerClose = $('#datePickerClose', $key); //日期面板关闭按钮
                timePickerOK = $('#timePickerOK', $key); //点击日期面板确定按钮
                
                selectAccount = $('#selectAccount', $key); //选择账号Span
                accountPanel = $('#accountPanel', $key); //选择账号面板
                accountCollection = []; //该模块使用的账号集合（带状态）
                
                selectFace = $('#selectFace', $key); //打开表情按钮
                facePanel = $('#facePanel', $key); //表情面板
          		
                lookLst = $('#lookLst', $key); //列表Div
                tabItem = $('.tabItem', $key); //标签对象集合
                pager = $('#allPager', $key); //分页对象
          		
          		textareaTip = '请输入你要发布的内容'; //textarea文本框默认值
          		release = $('#release', $key);
                Init();
            });
            return t;
        },
        NavToMP: function() {
        	this.Show();
        },
        //显示模块方法
        Show:function() {
        	NavigationInit();
        	$key.show();
        	Init();
        }
    };
});