define(['portal', 'repost', 'comment'], function (portal, Repost, Comment) {
	var textareaTip = '请输入你要发布的内容', //textarea文本框默认值
		  colors = ['#96bfdb', '#f4c581', '#57c4db', '#b9d986', '#a3ddb4', '#f4a5aa'],
		  currentAccountID = 0;
	//私有方法
	//加载柱状图(栈模式)，data：数据,angle:角度
	function CharColumn(container, data, angle) {
		container.highcharts({
			chart: {
                type: 'column'
            },
            colors: colors,
            title: {
                text: data.title
            },
            xAxis: {
				labels:{
					rotation: angle
				},
				tickInterval: data.tickInterval, //X轴间隔(步长)
                categories: data.categories
            },
            yAxis: {
                min: 0,
                title: {  text: data.text }
            },

             tooltip: {
                valueSuffix: '',
                shared: TE
            },
            plotOptions: {
                column: {
                	pointWidth: 10,
                    stacking: 'normal',
                    dataLabels: {
                        enabled: FE, //是否在柱状图上显示值
                        color: 'white', //柱状图Stack每节的数量颜色
                        style: {
                            textShadow: '0 0 3px black, 0 0 3px black'
                        }
                    }
                }
            },
            series: data.series
        });
	}
	//加载柱状图(百分比模式)，data：数据,angle:角度
	function CharColumnPercent(container, data, angle) {
		var total = 0, series = data.series[0].data;
		for (var i = 0; i<series[LN]; i++) {
			total += series[i];
		};
		total = parseInt(total);
		function percent(value) {
			var num = value/total + '';
    	 	if(num.indexOf('.') > -1) {
    	 		num =  num.substring(0,num.indexOf('.') + 5); // 保留2位小数
    	 		return num * 100 + '%';
    	 	} else {
    	 		return 0;
    	 	}
		}
		
		container.highcharts({
			chart: {
                type: 'column'
            },
            colors: colors,
            title: {
                text: data.title
            },
            xAxis: {
				labels:{
					rotation: angle
				},
				tickInterval: data.tickInterval, //X轴间隔(步长)
                categories: data.categories
            },
            yAxis: {
                min: 0,
                title: {  text: data.text },
                labels:{
                	 formatter: function() {
                	 	return percent(this.value);
                	 }
                }
            },

             tooltip: {
                valueSuffix: '',
                shared: TE,
                formatter: function() {
	    	    	return this.x + ':' + percent(this.y);
	    	    }
                 
            },
            plotOptions: {
                column: {
                	pointWidth: 10,
                    stacking: 'normal',
                    dataLabels: {
                        enabled: FE, //是否在柱状图上显示值
                        color: 'white', //柱状图Stack每节的数量颜色
                        style: {
                            textShadow: '0 0 3px black, 0 0 3px black'
                        }
                    }
                }
            },
            series: data.series
        });
	}
	//加载线图，data：数据,angle:角度
	function CharLine(container, data, angle) {
		container.highcharts({
			chart: {
				type: 'line'
			},
			colors: colors,
			title: {
                text: data.title
            },
            xAxis: {
            	labels:{
					rotation: angle
				},
				tickInterval: data.tickInterval, //X轴间隔(步长)
                categories: data.categories
            },
            yAxis: {
                title: {
                    text: data.text
                },
                min: 0, //设置Y轴最小值为0,不出现负数情况 
                plotLines: [{
                    value: 0,
                    width: 1,
                    color: '#808080'
                }]
            },
            tooltip: {
                valueSuffix: '',
                shared: TE
            },
            legend: {
                layout: 'horizontal',
                align: 'center',
                verticalAlign: 'bottom',
                borderWidth: 1
            },
            series: data.series
        });
	}
	//加载饼图，data：数据
	function CharPie(container, data) {
		container.highcharts({
	        chart: {
				type: 'pie'
	        },
	        colors: colors,
	        title: {
	            text: data.title
	        },
	        tooltip: {
	    	    formatter: function() {
	    	    	return '<b>' + this.point.name + '</b>: ' +  Highcharts.numberFormat(this.percentage, 2) + ' %';
	    	    }

	        },
	        plotOptions: {
	            pie: {
	            	size: '80%',
	                allowPointSelect: TE,
	                cursor: 'pointer',
	                dataLabels: {
	                    enabled: TE,
	                    color: '#000',
	                    connectorColor: '#000',
	                    formatter: function () {
	                    	return '<b>' + this.point.name + '</b>: ' +  Highcharts.numberFormat(this.percentage, 2) + ' %';
	                    }
	                },
	                showInLegend: TE
	            }
	        },
	        series: [data.series]
	    });
	}
	//加载横条图，data：数据
	function CharBar(container, data) {
		container.highcharts({
                chart: {
                    type: 'bar'
                },
                title: {
                    text: data.title
                },
                xAxis: {
                    categories: ['Africa', 'America', 'Asia', 'Europe', 'Oceania'],
                    title: {
                        text: data.text
                    }
                },
                yAxis: {
                    min: 0,
                    title: {
                        text: 'Population (millions)',
                        align: 'high'
                    },
                    labels: {
                        overflow: 'justify'
                    }
                },
                tooltip: {
                    valueSuffix: ' millions'
                },
                plotOptions: {
                    bar: {
                        dataLabels: {
                            enabled: TE
                        }
                    }
                },
                legend: {
                    layout: 'vertical',
                    align: 'right',
                    verticalAlign: 'top',
                    x: -40,
                    y: 100,
                    floating: TE,
                    borderWidth: 1,
                    backgroundColor: '#FFFFFF',
                    shadow: TE
                },
                credits: {
                    enabled: FE
                },
                series: data.series
            });
	}
	
	//异步获取笑脸，(带缓存)buton:打开面板按钮, textarea:多行输入框
    function LoadFace(buton,panel, textarea) {
    	var faceItem = panel[FD]('#faceItem');
    	faceItem[LG]();
	    portal.CurrentUser.GetFaces(function(data) { 
	    	var template = '<li class="faces" name="{1}"><img src="{0}" /></li>',
				   html = '';
		    $[EH](data, function(i, item) {
		    	var path = item.fileName[RP]('$', faceImagePath);
		    	html += $[FO](template, path, item.name);
		    });
			faceItem.html(html);
			SelectFace(textarea);
			CloseFace(panel, '#faceClose');
	    });
	}
	
	//选择某个表情,textarea:多行输入框
    function SelectFace(textarea) {
    	$('.faces')[EV](CK, function() {
			var $this = $(this),
				   name = $this[AT]('name');
			var txt = textarea.val()
			if(txt == textareaTip) { 
				txt = ''; 
				textarea.css('color', 'black');
			}
			var index = textarea.data('index'),
				   start = txt.substring(0, index),
				   end = txt.substring(index, txt[LN]);
			
			txt = start + $[FO]('[{0}]', name) + end;
			textarea.val(txt)[TR](KU);
			textarea.data('index', index);
			return FE;
		});
    }
    //关闭表情面板,panel, buttonName
    function CloseFace(panel, buttonName) {
    	panel[FD](buttonName)[EV](CK, function() {
    		$(this)[PT]()[PT]().hide();
    		return FE;
		});
    }
    
    //账号选择器后续操作
    function OptionAccount(panel, accountCollection, onClick) {
       	panel[FD]('#accountClose')[EV](CK, function() {
    		$(this)[PT]().hide();
    		return FE;
		});
		
       	var allCB =	$('.allCB', panel),
			   accountCB = $('.accountCB', panel); //选择账号面板里的CB集合


		accountCB[EV](CK, function() {
	  		var index = accountCB[ID]($(this));	 
	  		accountCollection[index][CD] = this[CD];
		});

		//点击全选按钮事件
		allCB[EV](CK, function() {
			var checked = allCB[0][CD];
			$[EH](accountCollection, function(i, item) {
				item[CD] = checked;
			});
			accountCB[EH](function(i, item) { 
				this[CD] =checked;
			});
		});
       	
       	//点击确定按钮
       	panel[FD]('#accountOk')[EV](CK, function () {
       		panel[TG]();
       		onClick(accountCollection);
       	});
    }
    
    //创建视图业务,div:显示Div, data:数据, fn:方法
	function View(div, data, fn) {
		div[EH](function() {
			var $this = $(this),
				   index = div[ID]($this),
				   viewData = data.rows[index].item;
		     if(viewData != UN)
		     	if(viewData.text != UN) 
		     		fn($this, viewData);
	     });
	}
	/*
	 * 创建转发评论视图业务
	 * This: 当前列表
	 * data:数据
	 * observer:被观察的对象名称
	 * Class:类
	 * isSelectAccount: 转发评论面板是否能选择账号
	*/
	function PanelView(This, data, observer, Class, isSelectAccount, moduleType, onSend) {
		var parent = ('.listItem', This),
			   repostView = $('.repostView ', This),
			   commentView = $('.commentView', This);
		observer = '.' + observer;
		parent[EV](CK + observer, observer, function() {
			var $this = $(this),
				   index = $this[AT](ID),
				   panel = $this[PT]()[PT]()[NT](),
				   dataRow = data.rows[index],
				   accountRow = dataRow.account,
				   user = portal.CurrentUser;
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
				//dataRow.data = data;
				instance.Init(panel, dataRow, isSelectAccount, accounts, moduleType, currentAccountID);
				
				Class.fn.Sended = function ($this, txt, isCkeck, accountCollection, postID, url, type, twitterID, closeBtn) {
					onSend($this, txt, isCkeck, accountCollection, postID, url, type, twitterID, closeBtn);
			   	}
			   	
			}
		});
	}
	
	//画列表里的转发视图,div:显示Div,data:数据
	function DrawRepost(div, dataRow) {
		var btnHtml = '';
		if(dataRow.account.twitterType == 1) {
			btnHtml = '<p class="com_fRight subBtnPanel">{pmi:cbfx_monitor_4}{pmi:yjfw_yjxxinsert_2}</p>';
		}
		//0:昵称,1:内容,2:图片集合(图片模板),3:发布时间,4:来源,5:转发数,6:评论数,7:原地址url链接
		var template = '<div class="talkMe_count"><div class="deal_tipPic"></div><div class="deal_count"><span class="Fname_color">{0}</span><br /><span class="contentArea">{1}</span></div><div class="com_clear"></div><div class="deal_img">{2}</div><p class="pl_mar"><a href={7} target="_blank" class="com_fLeft">{3}</a><span>来源</span><span>{4}</span><span>转发{5}</span><span>评论{6}</span>' + btnHtml +'</p></div><div class="com_clear"></div>',
        	   imageTemplate = '<img src="{0}" width="100" height="100" />',
        	   imageHtml = '',
        	   html = '',
        	   images = dataRow.postImage;
		if(images[LN] > 0) {
			$[EH](images, function(i, image) {
    	  		imageHtml += $[FO](imageTemplate, image);
    	  	});
		}

		var repostCount = dataRow.repostCount > 0 ? '[' + dataRow.repostCount + ']' : '',
		 	   commentCount = dataRow.commentCount > 0 ? '[' + dataRow.commentCount + ']' : '';
		
		var name = dataRow.account.name;
		if(name == UN) {
			name = '';
		} 
		html = $[FO](template, name, _base.htmlEncodeByRegExp(dataRow.text), imageHtml, dataRow.createtime, dataRow.source, repostCount, commentCount, dataRow.url);
		div.html(_base.FormatPmi(html));
		
		
		$('.contentArea', div)[EH](function(i, item) {
			var $this = $(this);
			
			//解析列表内容上的url
			var contentUrl = AnalyzingUrl($this.html());
			$this.html(contentUrl);
			
			//解析列表上微博账号名称
			AnalyzingTwitterName($this.html(), function (newContent) {
				$this.html(newContent);
			});
		});
	}
	
	//画列表里的评论视图,div:显示Div,data:数据
	function DrawComment (div, dataRow) {
		//0:昵称,1:内容,2:发布时间,3:来源,4:转发数,5:评论数
		var template = '<div class="talkMe_count"><div><span class="Fname_color">{0}</span>{1}</div><p><span>{2}</span><span>来源</span><span>{3}</span><span>转发{4}</span><span>评论{5}</span></p>',
			   html = '',
			   repostCount = dataRow.repostCount > 0 ? '[' + dataRow.repostCount + ']' : '',
		 	   commentCount = dataRow.commentCount > 0 ? '[' + dataRow.commentCount + ']' : '';
		html = $[FO](template, dataRow.account.name, _base.htmlEncodeByRegExp(dataRow.text), dataRow.createtime, dataRow.source, repostCount, commentCount);
		div.html(html);
	}
	
	//设置默认账号信息,accountName:账号名,twitterType:账号类型
   	function SetDefaultAccount(currentAccount, accountName, twitterType, accountid, uid) {
   		currentAccount.html(accountName)[PT]()[AT]({'class': 'mynum_type' + twitterType, 'accountid': accountid, 'uid': uid, type: twitterType});
   	}
   	
   	//解析图片方法
   	function AnalyzingImage(content, fn) {
   		var array = content.match(/\[[\s\S]+?\]/g);
   		if(array == null) {
   			fn(content);
   			return;
   		}
   		GetImagePathByFaceName(array, content, function(data) {
		    	fn(data);
		});
   	}
   	//根据名称获取表情图片路径方法
   	function GetImagePathByFaceName(array, content, fn) {
   		portal.CurrentUser.GetFaces(function(data) {
   			$[EH](array, function(i, faceName) { 
   				var name = faceName[RP]('[', '')[RP](']', '');
   				$[EH](data, function(j, faceItem) {
   					if(faceItem.name == name) {
	   					var path = faceItem.fileName[RP]('$', faceImagePath);
		    			content = content[RP](faceName, $[FO]('<img alt="" src="{0}" />', path));
	   					return FE;
	   				}
   				});
   			});
   			fn(content);
   		});
   	}
   	//解析关键词方法
   	function AnalyzingWord(content, fn) {
   		var array  = content.match(/<img[\s\S]+?>/g);
   		if(array == null) {
   			fn(content);
   			return;
   		}
   		GetFaceNameByImagePath(array, content, function(data) {
		    	fn(data);
		});
   	}
   	
   	//根据表情图片路径获取名称方法
   	function GetFaceNameByImagePath(array, content, fn) {
   		portal.CurrentUser.GetFaces(function(data) {
   			$[EH](array, function(i, imgHtml) { 
   				var path = $(imgHtml)[0].src[RP](faceImagePath, '$');
   				$[EH](data, function(j, faceItem) {
   					if(faceItem.fileName == path) {
	   				    content = content[RP](imgHtml, '[' + faceItem.name + ']');
	   					return FE;
	   				}
   				});
   			});
   			fn(content);
   		});
   	}
   	
   	//分析微博账号名称方法
   	function AnalyzingTwitterName(content, fn) {
   		var html = content + ' ';
   		html = html[RP](/@/g, ' @')[RP](/：/g, '： ')[RP](/，/g, '， ')[RP](/:/g, ': ')[RP](': //', '://');
   		var array  = html.match(/@[\s\S]+?\s/g);
   		if(array == null) {
   			fn(content);
   			return;
   		}
   		GetTwitterName(array, html, function(data) {
   			fn(data);
   		});
   	}
   	
   	//分析内容中的url
	function AnalyzingUrl(content) {
		var html = content + ' ';
		html = html[RP](/，/g, ' ，')[RP](/@/g, ' @')[RP](/；/g, ' ；')[RP](/\[/g, ' [')[RP]('（', ' （')[RP]('<', ' <')[RP]('/ /g', ' ');
   		var array  = html.match(/http:\/\/[\s\S]+?\s/g);
   		if(array == null) {
   			return content;
   		}
   		return GetAnalyzingUrl(array, html);
	}
	
	//获取含url的html
	function GetAnalyzingUrl(array, content) {
		var _content = content;
		var regexp = /[\u4e00-\u9fa5]/;
		$[EH](array, function(i, url) {
			url = url[RP](' ', '');
		
            var a = url.match(regexp);
            if(a!=null) {
            	url = url.substring(0, url.indexOf(a));
            }
			
			var _url = $[FO]('<a class="font-color" href ="{0}" target="_blank" >{0}</a>', url);
			_content = _content[RP](url, _url);
		});
		return _content;
	}
   	
   	//根据表情图片路径获取名称方法
   	function GetTwitterName(array, content, fn) {
   		var nameArray = [];
		$[EH](array, function(i, twitterName) {
			
			var name = twitterName;
			if(name.indexOf(':') > -1) {
				name  = name.substring(0, name.indexOf(':'));
			}
			var afterName = twitterName[RP](name, '');
			var _name = '<span class="font-color">__$@$__</span>' + afterName;
			nameArray.push(name);
		    content = content[RP](twitterName, _name);
		});
		$[EH](nameArray, function(i, name) {
			content = content[RP]('__$@$__', name);
		});
		fn(content);
   	}
   	
   	//获取账号html
   	function GetAccountHtml(accountRow, moduleType) {
   		var html = '';
   		if (moduleType == 1 || moduleType == 5 || moduleType == 0 || moduleType == 2 || moduleType == 3 || moduleType == 4||moduleType==6 ||moduleType==7 ||moduleType==8) {
   			html = '<div class="doSee_me" style=" visibility:hidden;">+ 关注</div>';//<div class="doSee_me">+ 关注</div><div class="doSee_me com_none">√ 已关注</div>
   		}
   		var accountTemplate='<div class="photo_allInforBack"><div class="timing_tip photo_pos"></div><img src="{imgurlBig}" width="80" height="80" class="jies_photo" /><div class="jies_list">' + html + '<div class="com_clear"></div><h3 class="font-color">{name}</h3><div class="jies_style">微博 <span class="photo_Fcolor">[{weibos}]</span> | 粉丝 <span class="photo_Fcolor">[{followers}]</span> | 关注 <span class="photo_Fcolor">[{friends}]</span></div></div><div class="com_clear"></div><div class="jies_count">{summany}</div></div>';
   		return $[EF](accountTemplate, accountRow);
   	}
   	
   	//异步获取
   	function GetAccountAjax(div, accountRow, moduleType) {
   		div[AJ]({
			param: {action: 'portal.getAccount', ucode: accountRow.ucode, platform: accountRow.twitterType},
			success: function(data) {
				//传播分析模块
				if(moduleType == 8) {
					div.prev()[AT]('src', data.imgurl);
					div[PT]().next()[FD]('.saAccountName').text(data.name);
				} else {
					div.prev().prev()[AT]('href', data.url);
					div.prev().prev()[FD]('.accountImg')[AT]('src', data.imgurl);
					div[PT]().next()[FD]('.accountName').text(data.name);
				}
				div[AT]('isajax', data.isAjax).data('accountData', data);
    			div.html(GetAccountHtml(data, moduleType));
				div[RC]('com_none');
			}
		});
   	}
   	
   	// 获取光标位置
   	function savePos(textBox) {
            //如果是Firefox(1.5)的话，方法很简单
            if (typeof (textBox.selectionStart) == "number") {
                start = textBox.selectionStart;
                end = textBox.selectionEnd;
            }
            //下面是IE(6.0)的方法，麻烦得很，还要计算上'/n'
            else if (document.selection) {
                var range = document.selection.createRange();
                var start = 0, end = 0;
                if (range.parentElement().id == textBox.id) {
                    // create a selection of the whole textarea
                    var range_all = document.body.createTextRange();

                    if (range_all.compareEndPoints) {
                        range_all.moveToElementText(textBox);
                        //两个range，一个是已经选择的text(range)，一个是整个textarea(range_all)
                        //range_all.compareEndPoints()比较两个端点，如果range_all比range更往左(further to the left)，则                //返回小于0的值，则range_all往右移一点，直到两个range的start相同。
                        // calculate selection start point by moving beginning of range_all to beginning of range
                        for (start = 0; range_all.compareEndPoints("StartToStart", range) < 0; start++)
                            range_all.moveStart('character', 1);
                        // get number of line breaks from textarea start to selection start and add them to start
                        // 计算一下/n
                        for (var i = 0; i <= start; i++) {
                            if (textBox.value.charAt(i) == '/n')
                                start++;
                        }
                        // create a selection of the whole textarea
                        var range_all = document.body.createTextRange();
                        range_all.moveToElementText(textBox);
                        // calculate selection end point by moving beginning of range_all to end of range
                        for ( end = 0; range_all.compareEndPoints('StartToEnd', range) < 0; end++)
                            range_all.moveStart('character', 1);
                        // get number of line breaks from textarea start to selection end and add them to end
                        for (var i = 0; i <= end; i++) {
                            if (textBox.value.charAt(i) == '/n')
                                end++;
                        }
                    }
                }
            }
            return start;
        }
        
        //打开预警窗体
        function AlertWindow(alertBtn,alertID,alertIsDelete, author, content, repostCount, commentCount) {
        	var template = '',
        		   url = alertBtn[AT]('url');
        	 $[WW]({
						css: { width: '400px', height: 'auto' },
						title: '微博预警',
						content: '<div class="giveMe_do warning_up"><div class="div_style"><label class="task_name">转发达到：</label><input type="text" value="" class="task_count task_Cwdith3 alertRepostNum" maxLength="8" /><span  class="com_fLeft">&nbsp;&nbsp;发送预警</span></div><div class="com_clear"></div><div class="div_style"><label class="task_name">评论达到：</label><input type="text" value="" class="task_count task_Cwdith3 alertCommentNum" maxLength="8" /><span  class="com_fLeft">&nbsp;&nbsp;发送预警</span></div><div class="com_clear"></div><div class="div_style"><label class="task_name">截止时间：</label><input type="text" value="" class="task_count task_Cwdith3 alertTime" readonly="readonly"  /></div><div class="com_clear"></div><div class="div_style warn_style"><label class="task_name">接收人：</label><div class="mynum_list"><h2 class="mynum_click_alert" id="head_alert"><span id="head_alert_span">请选择</span>' +
								'<span class="mynum_type mynum_click "></span></h2><ul class="nameList_alert" id="nameList_alert_id">' +
								'</ul></div></div>  <div class="div_style"><label class="task_name">发送方式：</label><input type="checkbox"  name="yjSend_way" class="com_fLeft yjSend_wayMar alertCheck" value="0" disabled="disabled" /><span  class="com_fLeft com_rMar">系统消息</span><input type="checkbox"  name="yjSend_way" class="com_fLeft yjSend_wayMar alertCheck" " value="1"   /><span  class="com_fLeft com_rMar">邮件</span><input type="checkbox"  name="yjSend_way" class="com_fLeft yjSend_wayMar alertCheck" " value="2"  /><span  class="com_fLeft com_rMar">手机短信</span></div><div class="com_clear"></div><div class="push_ok"><div class="in_MsetCno  alertCancel">取&nbsp;&nbsp;消</div><div class="in_MsetCok alertOK" isclick="true">确&nbsp;&nbsp;定</div><span class="alertMBTip" style="color:red;"></span></div><div class="com_clear"></div></div>',
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
										   liTemplate = '<li id="alert_id"><input type="checkbox" class="liAlertName" id="fail_w{id}" value="{id}"/><label for="fail_w{id}" id="alert_id">{useName}</label></li>';
									       html = $[EF](liTemplate, data);
								   	ul.html(html);
								   	
								   	var startTime = $.getDate();
								   	alertTime.datePicker({minDate: startTime});
								   	
								   	endTime = $.addDate('d',7, startTime).Format('YYYY-MM-DD');
								   	alertTime.val(endTime);
								   	
								   	var alertCheck = $('.alertCheck', div);
			    					alertCheck[0][CD] = TE;
								   	
								   	ul.hide();
								   	var state = FE;
									ul[EV](CK, function(e) {
										if(e.target.id == 'alert_id' || e.target.id.indexOf('fail_w') > -1) {
											state = TE;
											ul.show();
										} else {
											ul.hide();
											state = FE;
										}
									});
									div[EV](CK, function(e) {
										if(e.target.id != 'head_alert' && e.target.id != 'head_alert_span' && !state) {
											ul.hide();
										} else {
											if(!state){
												ul.toggle();
											}
										}
										state = FE;
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
				    						   $this = $(this),
				    						   isclick = $this[AT]('isclick'),
				    						   param = {};
				    					if (isclick == 'false') {
				    						return;
				    					}
				    					$this[AT]('isclick', FE);
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
				    					alertCheck[EH](function() {
				    						if(this[CD]) {
				    							typeids.push(this.value);
				    						}
				    					});
				    					param.typeids = typeids.join(',');
				    					param.platform = alertBtn[AT]('platform');
				    					param.tid = alertBtn[AT]('url');
				    					param.time = alertTime.val();
				    					param.moduleType = 0;
				    					param.isdelete=alertIsDelete;
							            param.alertid=alertID;
							            param.author = author;
							            param.text = content;
							            param.currepost = repostCount;
							            param.curcomment = commentCount;

				    					// 验证
				    					if(param.repostNum != '' || param.commentNum != '') {
				    						
				    					} else {
				    						alertMBTip.text('转发数或评论数必须填一个');
				    						$this[AT]('isclick', TE);
				    						return FE;
				    					}
				    					if(param.repostNum != '' && param.repostNum.split('')[0] == '0') {
				    						alertMBTip.text('转发数开头不能为0');
				    						$this[AT]('isclick', TE);
				    						return FE;
				    					}

				    					if(param.commentNum != '' && param.commentNum.split('')[0] == '0') {
				    						alertMBTip.text('评论数开头不能为0');
				    						$this[AT]('isclick', TE);
				    						return FE;
				    					}

				    					if(param.nameids == '') {
				    						alertMBTip.text('请选择接收人');
				    						$this[AT]('isclick', TE);
				    						return FE;
				    					}
				    					if(param.typeids == '') {
				    						alertMBTip.text('请选择发送方式');
				    						$this[AT]('isclick', TE);
				    						return FE;
				    					}
				    					param.action = 'warnservices.warnservicesinsert';

				    					// 提交保存
										div[AJ]({
											param: param,
											success: function(data) {
												if(data  > 0) {
													alertBtn.text('取消预警')[AT]({alertid:data,isdelete:0});	
												} else if(data == -1) {
													$[MB]({ content: '已经存在', type: 2 });
												} else if(data == -2) {
													$[MB]({ content: '微博已删除', type: 2 });
												} else if(data == -3) {
													$[MB]({ content: '预警到达上限,不能添加', type: 2 });
												} else {
													$[MB]({ content: '预警失败', type: 2 });
												}
												close();
												$this[AT]('isclick', TE);
											}
										});
				    				});
								}
							});
						}
					});
        }
	
     var _base = {
    	//简易Ajax公用方法
    	//param:参数, ok:加载成功回调函数, dataType:返回数据类型, err:加载错误回调函数, global: 是否开启全局特性
        AJ: function(param, ok, dataType, err, global) {
        	if(dataType == null) dataType = 'json';
        	$(BY)[AJ]({
        		param: param,
        		success: ok,
        		error: err,
        		global: global,
        		dataType: dataType
        	});
        },
        //加载图表方法
        //charType：图表类型(column, line, pie, bar)
        Char: function(container, charType, data, angle) {
        	var c = portal.Enum.CharType;
        	switch (charType) {
        		case c.Column: CharColumn(container, data, angle); return;
        		case c.Line: CharLine(container, data, angle); return;
        		case c.Pie: CharPie(container, data); return;
        		case c.Bar: CharBar(container, data); return;
        		case c.ColumnPercent: CharColumnPercent(container, data, angle); return;
        	}
        },
        //传播分析使用显示头像数据
        AccountTipSelf: function (img) {
        	var timer;
        	
        	
			img[EV](MO, function() {
				var $this = $(this),
					   ucode = $this[AT]('ucode'),
        			   twitterType = $this[AT]('twitterType');
        			  
			  	CM(timer);
    			$(BY)[AP]('<div class="photo_allInfor ss_sp"></div>');
    			$('.ss_sp').css({
					position: 'absolute',
					top: $this[OF]().top,
					left: $this[WH]() + $this[OF]().left + 10
				});
				
				$this[AJ]({
					cacheName: 'ss_ss',
					isCache: TE,
					param: {action: 'portal.getAccount', ucode: ucode, platform: twitterType},
					success: function(data) {
						$('.ss_sp').html(GetAccountHtml(data, 0));
						$('.ss_sp')[EV](MO, function(){
							CM(timer);
						})[EV](MU, function(){
							timer = SM(function () {
			        			$('.ss_sp').remove();
			        		}, 500);
						}); 
					}
				});
        	})[EV](MU, function() {
        		timer = SM(function () {
        			$('.ss_sp').remove();
        		}, 500);
        	});
        },
        //微博头像层,div:要显示的Div,accountRow: 账号数据
        AccountTip:function(div, accountRow, moduleType, isFlag) {
        	var timer;
        	var css = 'com_none';
        	div[PT]()[EV](MO, function() {
        		CM(timer);
        		div[0].timer = SM(function() { 
					//是否需要异步读取账号信息
					if(!div[AT]('isajax')) {
						div[AT]('isajax', accountRow.isAjax);
					}
					if(div[AT]('isajax') == 'true' && !isFlag) {
						GetAccountAjax(div, accountRow, moduleType);
					}else {
						//从缓存里读取
						if(div.data('accountData')) {
							div.html(GetAccountHtml(div.data('accountData'), moduleType));
						} else {
							//从list结构中读取
							div.prev().prev()[AT]('href', accountRow.url);
							div.prev().prev()[FD]('.accountImg')[AT]('src', accountRow.imgurl);
							div.html(GetAccountHtml(accountRow, moduleType));
						}
						div[RC](css);
					}
				}, 400);
			})[EV](MU, function() {
				timer = SM(function(){
					div[AC](css);
				},500);
				CM(div[0].timer);
			});
        },
        htmlEncodeByRegExp: function (str) {  
			var s = '';
			if(str[LN] == 0) return '';
			s = str.replace(/&/g,'&amp;');
          	s = s.replace(/</g,'&lt;');
          	s = s.replace(/>/g,'&gt;');
          	//s = s.replace(/ /g,'&nbsp;');
          	s = s.replace(/\'/g,'&#39;');
          	s = s.replace(/\"/g,'&quot;');
          	return s;  
    	},
        /*
         * 微博列表视图通用方法 (头像信息已经加载不需要另加)
         * 参数	obiect
		 * panel: 显示列表的div
	     * pager: 显示的分页div
	     * id: 列表ID, 
	     * param: 列表参数
	     * template: 操作按钮模板
	     * isComment: FE,如果是评论视图,此项为TE(true)
	     * isRepostCommonCount: TE,列表是否带转发评论按钮功能
	     * isSelectAccount: TE,转发评论面板是否有选择账号功能
	     * onComplete: 视图加载成功后的操作,该方法返回This, refresh, data这三个参数
	     * 
	     * 调用实例
	     * base.DataBind({
				panel: lookLst, 
				pager: $('#allPager', $key),
				id: id, 
				param: {action :'myTwitter.loadList', loadListType: id, keyword: keyword}, 
				template: '<span>自定义按钮</span>',
				onComplete: function (This, refresh, data) { }
			});
	     * */
        DataBind:function(o) {
        	var t = this;
            //rcTemplate = t.FormatPmi(rcTemplate);
        	o = $[EX]({ 
        		isComment: FE, 
        		isRepostCommonCount: TE, 
        		isSelectAccount: TE,
        		isFlag: FE, // false 授权没到期， true 授权到期
        		moduleType: portal.Enum.ModuleType.View,
        		template: '' ,
        		onComplete: function (This, refresh, data) {},
        		onSend: function (txt, isCkeck, accountCollection, type) {},
        		onProcess: function(message) {  }
    		}, o);
			if (o.isRepostCommonCount) {
				//if (o.template != '') rcTemplate = '<span>|</span>' + rcTemplate;
				//o.template = o.template + rcTemplate;
			}
			o.panel.html('');
			o.panel[LS]({
	            width: 1000,
	            id: o.id,
	            pager: o.pager,
	            param: o.param,
	            templates: [{ html:'<div class="cont_list"><div class="cont_photo"><a target="_blank" class="com_fLeft aOpen" href=""><img src="" class="accountImg" width="50" height="50" /></a><div class="wb_listType"></div><div class="photo_allInfor com_none acInfo"><div class="photo_allInforBack acSub"></div></div></div><div class="taikMe_List"><h2><span class="Fname_color accountName"></span><br /><span class="contentArea"></span></h2><div class="com_clear"></div><div class="item"><div class="cont_img imageLst"></div><div class="com_clear"></div><div class="raly_countList repostLst"><div class="com_clear"></div></div><div class="pl_countLsit commentLst"></div></div></div><div class="com_clear"></div>'}, //内容
	                      	        { html: '<div class="cont_do"><div class="cont_doleft"><a href="{0}" target="_blank">'}, //链接
	                                { html: '{0}</a>' }, //日期
	                      	        { html: '<span>来源</span><span>{0}</span></div><div class="cont_doright buttons">' + o.template + '</div></div><div class="relay repostView com_none"></div><div class="relay commentView com_none"></div></div>' }], //来源
	            columns: [{ field: 'text'},
	                             { field: 'url'},
	                             { field: 'createtime' },
	                             { field: 'source' }],
                onProcess: function (message) {
                	o.onProcess(message);
                },
	            onComplete: function (This, refresh, data) {
	            	if(data.rows[LN] == 0) {
	            		t.not(o.panel);
	            		return;
	            	}

	            	//设置账号头像、昵称
					var accountImg = $('.accountImg', This),
	            		   accountName = $('.accountName', This),
	            		   wb_listType = $('.wb_listType', This)
					accountImg[EH](function (i, item) {
	        		   	var $this = $(this),
	        		   		   index = accountImg[ID]($this),
	        		   		   dataRow = data.rows[index],
	        		   		   account = dataRow.account;

	   		   			$this[AT]('src', account.imgurl);
	   		   			$this[PT]()[AT]('href', account.url);
	   		   			accountName.eq(i).text(account.name);
	   		   			wb_listType.eq(i)[AC]('mynum_type' + account.twitterType); 
					});
					
					var contentArea = $('.contentArea', This);
					contentArea[EH](function(i, item) {
						
						 
						var $this = $(this),
							   index = contentArea[ID]($this),
	        		   		   dataRow = data.rows[index];
						$this.text(t.htmlEncodeByRegExp(dataRow.text));


						 
						//解析列表内容上的url
						var contentUrl = t.AnalyzingUrl($this.text());
						$this.html(contentUrl);
					   	
						//解析列表上微博账号名称
						t.AnalyzingTwitterName($this.html(), function (newContent) {
							$this.html(newContent);
							
							//解析关键字
							t.AnalyzingImage($this.html(), function(contnet) {
								$this.html(contnet);
							});
							
						});

						//解析图片
						
					});
					

	            	if (!o.isComment) {
		            	//原创类型视图
						var imagesDiv = $('.imageLst', This);
						imagesDiv[EH](function() {
							var $this = $(this),
								   index = imagesDiv[ID]($this),
								   images = data.rows[index].postImage,
								   html = '';
					   		if(images[LN] > 0) {
							    $[EH](images, function(i, image) { 
							    	img = image[RP]('$', userWeiboImagePath);
							    	html += $[FO]('<img src="{0}" width="100" height="100" />', img);
							    });
						   		$this.html(html);
						   	}
						});
						
						//转发类型视图
						repostDiv = $('.repostLst', This);
						View(repostDiv, data, DrawRepost);
	            	} else {
	            		//评论类型视图
	            		var commentLst = $('.commentLst', This);
	            		View(commentLst, data, DrawComment);
	            	}
	
					
	            	//显示用户信息
	            	var accountInfo = $('.acInfo', This);
	            	accountInfo[EH](function() {
						var $this = $(this),
							   index = accountInfo[ID]($this),
							   accountRow = data.rows[index].account;
						t.AccountTip($this, accountRow, o.moduleType, o.isFlag);
					});
					
					//设置转发、评论数
					if (o.isRepostCommonCount) {
						
						var repostCount = $('.repostCount', This),
						   	  commentCount  = $('.commentCount', This),
						   	  noRepostCount =  $('.noRepostCount', This),
						   	  noCommentCount  = $('.noCommentCount', This);
						//如果有转发权限
						if(repostCount[LN] > 0) {
							repostCount[EH](function(i, item) {
								var $this = $(this),
									  index = repostCount[ID]($this),
									  dataRow = data.rows[index];
							    $this[AT](ID, i);
								if(dataRow.repostCount > 0) $this.text('转发[' + dataRow.repostCount + ']');
							});
							PanelView(This, data, 'repostCount', Repost, o.isSelectAccount, o.moduleType, o.onSend);
							//没有转发权限
						} else {
							noRepostCount[EH](function(i, item) {
								var $this = $(this),
									  index = noRepostCount[ID]($this),
									  dataRow = data.rows[index];
							    $this[AT](ID, i);
								if(dataRow.repostCount > 0) $this.text('转发[' + dataRow.repostCount + ']');
							});
						}
						//如果有评论权限
						if(commentCount[LN] > 0) {
							commentCount[EH](function(i, item) {
								var $this = $(this),
									  index = commentCount[ID]($this),
									  dataRow = data.rows[index];
							    $this[AT](ID, i);
								if(dataRow.commentCount > 0) $this.text('评论[' + dataRow.commentCount + ']');
							});
							PanelView(This, data, 'commentCount', Comment, o.isSelectAccount, o.moduleType, o.onSend);
							//没有评论权限
						} else {
							noCommentCount[EH](function(i, item) {
								var $this = $(this),
									  index = noCommentCount[ID]($this),
									  dataRow = data.rows[index];
							    $this[AT](ID, i);
								if(dataRow.commentCount > 0) $this.text('评论[' + dataRow.commentCount + ']');
							});
						}
					}
					
					//获取收集状态参数（url(舆情展示模块)或者twitterid(我的首页模块)）
     				var collectCode = '',
     					   collectCodes = [],
     					   moniterCodes = [],
     					   moduleType = o.moduleType,
     					   _moduleType = portal.Enum.ModuleType,
     					   collectUrls = '',
     					   moniterUrls='';
     				if (moduleType == _moduleType.View || 
     					 moduleType == _moduleType.Deal ||
     					 moduleType == _moduleType.EventMonitor ||
     					 moduleType == _moduleType.talkMe) {
     					collectCode = 'url';
     				} else {
     					collectCode = 'tid';
     					
     				}
					
					//设置阴影url和类型
	            	var lst = $('.cont_list', This);
	            	lst[EH](function(i) {
	            		var $this = $(this),
	            			   index = lst[ID]($this),
	            			   dataRow = data.rows[index],
	            			   account = dataRow.account,
	            			   subDataRow = dataRow.item;
            			if (o.moduleType == portal.Enum.ModuleType.MyHomePage) {
	            				$this[FD]('.jiance ')[RM]();
        				} 
        				//设置收集按钮属性信息
        				var collectionBtn = $this[FD]('.collectBtn');
        				collectionBtn[AT]({
        					url: dataRow[collectCode]
        				});
        
            			if(dataRow.isCreative) {
            				//设置白色区域监控按钮属性信息
							var moniterBtn = $this[FD]('.moniterBtn');
							moniterBtn[AT]({
								twitterType: account.twitterType,
								url: dataRow[collectCode]
							});
            			} else {
            				//设置阴影监控按钮属性信息
            				var moniterBtn = $this[FD]('.jiance');
        					moniterBtn[AT]({
								twitterType: account.twitterType,
								url: subDataRow == UN ? '': subDataRow[collectCode]
							});
            				//转发类型，删除白色区域的监控按钮
            				$this[FD]('.moniterBtn').next()[RM]();
            				$this[FD]('.moniterBtn')[RM]();
            			}
	            	});
	            	
	            	
	            	
	            	var collectionBtn = $('.collectBtn', This),
	     				   moniterBtn = $('.moniterBtnAll', This);
					
					//计算监控按钮状态
     				moniterBtn[EH](function(i) {
     					var code= $(this)[AT]('url');
     					moniterCodes.push(code);
     				});
     				moniterUrls = moniterCodes.join(',');
     				
     				collectionBtn[EH](function() {
     					var code= $(this)[AT]('url');
     					collectCodes.push(code);
     				});
     				collectUrls = collectCodes.join(',');
     				

	     			//如果有收集或者有监控按钮
	     			if(collectionBtn[LN] > 0) {
		     			//读取收集状态
		     			$(BY)[AJ]({
							param: { action: 'view.getCollectState', urls: collectUrls},
							success: function (map) {
								//收集按钮操作
								if(collectionBtn[LN] > 0) {
									//设置收集状态
									$[EH](map, function (key, tweetStatus) {
										if (tweetStatus.collectID != 0) {
											$[EH](data.rows, function (index, dataRow) {
												if(dataRow[collectCode] == key) {
													collectionBtn.eq(index)[AT]('collectid', tweetStatus.collectID).text('取消收集');
													return FE;
												}
											});
										}
									});
									
									//启动收集功能
									collectionBtn[EH](function () {
					     				var $this = $(this),
					     					   index =  collectionBtn[ID]($this),
					     					   dataRow = data.rows[index],
					     					   platform = dataRow.account.twitterType,
					     					   Collect = portal.Collect;
					     					//$.alertParam(dataRow.tid, platform, dataRow.url, o.moduleType);
					     				(new Collect($(this),  dataRow.tid, platform, dataRow.url, o.moduleType)).open();
									});
								}
							}
		     			});
	     			}
	     			
					//监控按钮操作
					if(moniterBtn[LN] > 0) {
						//读取收集状态
		     			$(BY)[AJ]({
							param: { action: 'view.getCollectState', urls: moniterUrls},
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
		     			});

						//启动监控功能（传播分析模块的添加功能）
						moniterBtn[EV](CK, function() {
							var  $this = $(this),
								    index = $this[ID](),
								    text = $[TM]($this.text()),
								    spreadid = $this[AT]('moniterid');
							//$.alertParam(index, data.rows[index].url);
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
										} else if (data == -4) {
											$[MB]({ content: '微博已经不存在', type: 2 });
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
					}
					
					// 预警按钮
					var lst = $('.cont_list', This),
						  alertUrls = [];
					lst[EH](function() {
						var $this = $(this),
							   index = lst[ID]($this),
							   dataRow = data.rows[index];
						// 原帖预警
						var alertBtn = $this[FD]('.buttons')[FD]('.alertBtn');
						if(alertBtn[LN] > 0) {
							alertBtn[AT]({
	        					url: dataRow[collectCode],
	        					platform: dataRow.account.twitterType,
	        					author:  dataRow.account.name,
	        					content: dataRow.text,
	        					repostCount: dataRow.repostCount,
	        					commentCount: dataRow.commentCount
	        				});
	        				alertUrls.push(dataRow[collectCode]);
						}
						
						// 转发帖预警
						var alertBtn2 = $this[FD]('.talkMe_count')[FD]('.alertBtn');
						if(alertBtn2[LN] > 0) {
							var item = dataRow.item;
							alertBtn2[AT]({
	        					url: item[collectCode],
	        					platform: item.account.twitterType,
	        					author:  item.account.name,
	        					content: item.text,
	        					repostCount: item.repostCount,
	        					commentCount: item.commentCount
	        				});
	        				alertUrls.push(item[collectCode]);
						}
					});
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
									  var arrays =map[key];
										if(parseInt(arrays[0])>0 &&parseInt(arrays[1])==0 ){  
											alertBtn.text('取消预警')[AT]({alertid:arrays[0],  isDelete:arrays[1]   });
										}else if(parseInt(arrays[0])>0 &&parseInt(arrays[1])==1){
										alertBtn.text('预警')[AT]({alertid:arrays[0],  isDelete:arrays[1]   });
										} 
									   state = TE;
										return FE;
									}
									
									var alertBtn2 = $(this)[FD]('.talkMe_count')[FD]('.alertBtn');
									if(alertBtn2[AT]('url') == key) {
										/*
										if(map[key] > 0) {
											alertBtn2.text('取消预警')[AT]('alertid', map[key]);
											state = TE;
											return FE;
										}
										*/
									  var arrays =map[key];
										if(parseInt(arrays[0])>0 &&parseInt(arrays[1])==0 ){  
											alertBtn2.text('取消预警')[AT]({alertid:arrays[0],  isDelete:arrays[1]   });
										}else if(parseInt(arrays[0])>0 &&parseInt(arrays[1])==1){
										alertBtn2.text('预警')[AT]({alertid:arrays[0],  isDelete:arrays[1]   });
										} 
									   state = TE;
										return FE;
									}
								});
								if(state) {
									continue;
								}
							}
						}
					});
					
					$('.alertBtn', This)[CK](function() {
						var $this = $(this);
						if ($this.text() == '预警') {
							AlertWindow($this,$this[AT]('alertid'), $this[AT]('isdelete'), $this[AT]('author'), $this[AT]('content'), $this[AT]('repostCount'), $this[AT]('commentCount'));
						} else {
							var param = {};
							param.action = 'warnservices.warnservicesinsert';
							param.isdelete=$this[AT]('isdelete');
							param.alertid=$this[AT]('alertid');
							
							param.tid=$this[AT]('url');
							param.moduleType = 0;
							$this[AJ]({
								param: param,
								success: function(data) {
									if(data  > 0) {
										$this.text('预警');
									    if(data==500){
													$this[AT]({alertid:param.alertid,isdelete:0});
											}else{
												   $this[AT]({alertid:0,isdelete:0});
											}
									
									} else {
										$[MB]({ content: '删除预警失败', type: 2 });
									}
								}
							});
						}
					});
					
					// 授权到期
					if(o.isFlag) {
						var alertBtn = $('.alertBtn', This),
							   collectBtn = $('.collectBtn', This);
						alertBtn.next()[RM]();
						alertBtn[RM](); // 移除预警功能
						collectBtn.next()[RM]();
						collectBtn[RM](); // 移除收集功能
						// $('.commentCount', This)[RC]('commentCount')[AC]('cont_noDO');
	     				// $('.repostCount', This)[RC]('repostCount')[AC]('cont_noDO');
					} 

					// 隐藏空div兼容IE7
					$('.imageLst', This)[EH](function() {
						if($(this).html() == '') $(this).hide();
					});
					$('.repostLst', This)[EH](function() {
						if($(this).html() == '') $(this).hide();
					});
					$('.commentLst', This)[EH](function() {
						if($(this).html() == '') $(this).hide();
					});

					o.onComplete(This, refresh, data);
	            }
	        });
        },
        //表情选择器,buton:打开面板按钮, panel:显示的表情Div,textarea:多行输入框,panels:要关闭的其他面板，没有可以传null
        FacePicker: function (button, panel, textarea, panels) {
        	var template = '<div class="bqing_list"><div class="s_smallTip"></div><div class="timing_close" id="faceClose"></div><div class="bqing_sort"><span class="bqing_back">全部</span></div><div class="com_clear"></div><ul class="bqing_mar2" id="faceItem"></ul></div>';
			panel.html(template);
			button[EV](CK, function() {
				if (panels != null) panels.hide();
    			panel[TG]();
    			LoadFace(button,panel, textarea);
			});
        },
        //账号选择器(多选)id:ID, button:点击按钮, panel:显示Div, accountCollection:账号集合 , onComplete:加载完成事件, onClick:点击事件, panels:所有的面板
        AccountPicker: function (id, button, panel, accountCollection , onComplete, onClick, panels) {
        	var template = '<div class="timing_close close_tip" id="accountClose"></div><div class="zhao_list"><input type="checkbox" name="zhao_num" class="zhao_liSel allCB" /><div>全选</div></div><div id="cbItems"></div><div class="tim_save" id="accountOk">确&nbsp;&nbsp;定</div>',
        		   itemTemplate = '<div class="zhao_list" style="display:{4};"><input type="checkbox" name="zhao_num" class="zhao_liSel accountCB" value="{3}" id="zhao_{2}"  /><label class="mynum_type{1} " for="zhao_{2}">{0}</label></div>',
   				   html = '';                             
			$[EH](accountCollection, function(i, item) {
				var text = 'none';
				if (!item.isExpire) {
 					text = 'block';
				}
				html += $[FO](itemTemplate, item.name, item.type, i + id, item.uid, text);
		  	});
			panel.html(template);
			panel[FD]('#cbItems').html(html);
			
			//设置自己的业务
			onComplete($('.allCB', panel), $('.accountCB', panel));
			
			button[EV](CK, function() {
				if (panels != null) panels.hide();
				panel[TG]();
				
				//后续操作
				OptionAccount(panel, accountCollection, onClick);
			});
        },
        //账号选择器(单选)
        CurrentAccountPicker: function(div, id, accountCollection, panels, onClick) {
	   		var t = this,
	   			   template = '<h2 class="" id="currentAccountInfo"><span class="user_name" id="currentAccount">读取中......</span><span class="mynum_type mynum_click" id="selectCurAccount"></span></h2><ul class="com_none" class="hidePanel" id="'+id+'"></ul>',
	   			   accountItem = '<li class="mynum_type{1} curAccounts" type="{1}" accountid="{2}" uid="{3}">{0}</li>',
	   			   html = '';
	   		div.html(template);
	   		var currentAccount = $('#currentAccount', div),
	   			   selectAccount = $('#selectCurAccount', div),
	   			   panel = $('#'+id, div),
	   			   isDefault = FE;
		   	$[EH](accountCollection, function(i, item) {
		   		//排除人民的账号
		   		if ( item.type != portal.Enum.TwitterType.People ) { 
		   			//设置默认选中的账号
		   			if(!isDefault) SetDefaultAccount(currentAccount, item.name, item.type, item.id, item.uid);
					html += $[FO](accountItem, item.name, item.type, item.id, item.uid);

					isDefault = TE;
		   		}
			});
			panel.html(html);
			
			currentAccount.add(selectAccount)[EV](CK, function() {
	   			panels.hide();
	   			panel.show();
	   			return FE;
	   		});
			
			var currentAccounts = $('.curAccounts', div);
	   		currentAccounts[EV](CK, function() {
	   			var $this = $(this),
	   				   name = $this.text(),
	   				   type = $this[AT]('type'),
	   				   accountid = $this[AT]('accountid'),
	   				   uid = $this[AT]('uid');
	   			currentAccountID = uid;
	   		    t.AccountIndex = currentAccounts[ID]($this);
	   		    t.AccountUID = currentAccountID;
				SetDefaultAccount(currentAccount, name, type, accountid, uid);
				panel.hide();
				
				//授权到期
				if(accountCollection[t.AccountIndex].isExpire) {
					$[MB]({
						content: '该账号授权以到期，请重新授权',
						type: 1
					});
					onClick(TE);
				} else {
					onClick(FE);
				}
	   		}).eq(t.AccountIndex)[TR](CK);
        },
        //账号单选选择器当前索引
        AccountIndex: 0, 
        AccountUID: 0,
        isAllExpire: TE,
     	//根据User账号集合获取各模块选择账号的专用集合
	    GetAccountCollection: function () {
	    	var accounts = portal.CurrentUser.Accounts,
	    		  accountCollection = [];
			if (accounts[LN] >0 ) {
		    	$[EH](accounts, function(i, item) {
		    		
		    		accountCollection.push({id: item.id, uid: item.uid, name: item.name, type: item.type, checked: TE, isExpire: item.isExpire});
		    	});
		    }
		    return accountCollection;
    	},
    	//切换页签方法tabs:页签集合, panels:内容面板集合, className:样式名称
    	OnChangeTab: function (tabs, panels, className, fn, isClickOne) {
			tabs[EV](CK, function () {
				var $this = $(this),
					   index = tabs[ID]($this);
				tabs[RC](className).eq(index)[AC](className);
				panels.hide().eq(index).show();
				fn(index);
			});
			if(isClickOne) tabs.eq(0)[TR](CK);
    	},
        //textarea限制140字提示特效,textarea:控件, panel:显示提示Div
    	LimitWord: function  (textarea, panel, btn, flag) {
	    	var yes = '您还可以输入<span class="font_num" >{0}</span>个字',
	    		  no = '您已经超出<span class="font_num2" >{0}</span>个字',
	    		  t = this;
	    	textarea[EV](KU, function() {
	    		if(!flag) {
		    		var val = $(this).val()[RP]('请输入你要发布的内容', ''),
		    			   count = 140 - t.GetTextLength(val),
		    			   classNameOut = 'send_fail',
		    			   classNameOver = 'send_over';
		    		panel.html(count >= 0? $[FO](yes, count): $[FO](no, -count));
		    		if (btn != null) {
		    			count >= 0? 
		    				btn[RC](classNameOut)[AC](classNameOver) : 
		    				btn[RC](classNameOver)[AC](classNameOut);
		    		}
		    		$(this).data('index',savePos(this));
	    		}
	    	})[EV]('cut', function() {
	    		if(!flag) {
		    		var $this = $(this);
		    		setTimeout(function () {
						var val = $this.val()[RP]('请输入你要发布的内容', ''),
		    			   	   count = 140 - t.GetTextLength(val),
		    			   	   classNameOut = 'send_fail',
		    			   	   classNameOver = 'send_over';
			    		panel.html(count >= 0? $[FO](yes, count): $[FO](no, -count));
			    		if (btn != null) {
			    			count >= 0? 
			    				btn[RC](classNameOut)[AC](classNameOver) : 
			    				btn[RC](classNameOver)[AC](classNameOut);
		    			}
		    			$(this).data('index',savePos(this));
		    			$('.font_num').text(savePos(this));
		    		},300);
	    		}
	    	})[EV]('paste', function() {
	    		if(!flag) {
		    		var $this = $(this);
		    		setTimeout(function () {
		    			var val = $this.val()[RP]('请输入你要发布的内容', ''),
		    			   	   count = 140 - t.GetTextLength(val),
		    			       classNameOut = 'send_fail',
		    			       classNameOver = 'send_over';
			    		panel.html(count >= 0? $[FO](yes, count): $[FO](no, -count));
			    		if (btn != null) {
			    			count >= 0? 
			    				btn[RC](classNameOut)[AC](classNameOver) : 
			    				btn[RC](classNameOver)[AC](classNameOut);
		    			}
		    			$(this).data('index',savePos(this));
	
		    		},300);
	    		}
	    	})[EV](MD, function(){
	    		if(!flag) {
	    			$(this).data('index',savePos(this));
				}
	    	})[EV](MP, function() {
	    		if(!flag) {
	    			$(this).data('index',savePos(this));
	    		}
	    	})[EV]('focus.length', function(){
	    		if(!flag) {
	    			$(this).data('index',savePos(this));
	    		}
	    	})[TR](KU);
	    },
	    //画热点信息方法panel: 显示信息div, key: 数据集合Key集合, data: 数据
	    //base.DrawHot(hotItem, ['text', 'link', 'color'], data); 调用示例
	    DrawHot: function(panel, key, data) {
	    	var template = '<li class="imp_center" style=" background: #{2};"><a class="W_liBack{3}" href="{1}"  target="_blank">{0}</a></li>',
				   html = '';
			$[EH](data, function(i, item) {
				//key[0]: 文本值, key[1]：链接, key[2]: 颜色
				html += $[FO](template, item[key[0]], item[key[1]], item[key[2]], i + 1);
			});
			panel.html(html);
	    },
	    //开始和结束时间验证,isDiff:FE，不限制时间
		IsRegExpDateTime: function (startDateTime, endDateTime, isDiff) {
			var start =$[TM](startDateTime.val()),
				   end = $[TM](endDateTime.val());
		  	if (start == '') { $[MB]({content: '请选择开始时间', type: 2}); return FE; }
		  	if (start != '' && !$[RA](start)) { $[MB]({content: '开始时间格式不正确', type: 2}); return FE; }
		  	if (end == '') { $[MB]({content: '请选择结束时间', type: 2}); return FE; }
			if (end != '' && !$[RA](end)) { $[MB]({content: '结束时间格式不正确', type: 2}); return FE; }
			if($.compareDate(start, end) < 0) { $[MB]({content: '开始时间不能大于结束时间', type: 2}); return FE; }
			if(!isDiff) {
				if($.diffDate(start, end) > 30) { $[MB]({content: '开始到结束时间范围不能超过30天', type: 2}); return FE; }
			}
			return TE;
		},
		//分析图片公用方法
		AnalyzingImage: function(content, fn) {
			AnalyzingImage(content, fn);
		},
		//分析关键词公用方法
		AnalyzingWord: function(content, fn) {
			AnalyzingWord(content, fn);
		},
		//分析微博名称方法
		AnalyzingTwitterName: function(content, fn) {
			AnalyzingTwitterName(content, fn);
		},
		//分析内容中的url
		AnalyzingUrl: function(content) {
			return AnalyzingUrl(content);
		},
		/* 注册隐藏面板方法(点击面板其他区域需要隐藏该面板的需要此方法)
		 * 格式示例：[{ triggerKey: 'tt', panelKey: '.datePickAll' }, { triggerKey: 't2', panelKey: '.datePickAll'}];
		 * triggerKey:为触发面板显示的对象id不用加#号
		 * panelkey为点击要隐藏的面板，注意：面板是class需要加.，是id需要加#
		 * 不用在考虑或担心，面板里面的各种事件冒泡问题
		 * */
		RegisterHiddenPanel: function (arrayItem) {
			portal.Panel.push(arrayItem);
			portal.HiddenTo();
			return this;
		},
		/*
		 *  格式化UI权限模板方法
		 *  html: 模板
		 */
		FormatPmi : function (html) {
	       	var array  = html.match(/\{pmi:[\s\S]+?\}/g);
	       	if(array != null) {
	       		var i = 0,
	       			   length = array[LN];
	       		for (i; i< length; i++) {
	       			var key = array[i],
	       				  templateKey = key[RP]('{pmi:', '')[RP]('}', ''),
	       				  templateHtml = portal.CurrentUser.Templates[templateKey] == UN ? '': portal.CurrentUser.Templates[templateKey];

	       			html = html[RP](key, templateHtml);
	       		}
	       	}
	        return html;
	    },
	    //添加账号（分配完账号后调用此方法）
     	//accounts:账号集合对象
     	AddAcount: function (accounts) {
     		if($.isArray(accounts) && accounts[LN] > 0) {
     			portal.CurrentUser.Accounts = accounts;
     		}
     	},
     	 //删除账号（删除完账号后调用此方法）
     	//uid: 微博账号uid
     	DeleteAcount: function (uid) {
     		var tempAccount = [];
     		$[EH](portal.CurrentUser.Accounts, function(i, acountRow) {
     			if (acountRow.uid != uid) {
     				tempAccount.push(acountRow);
     			}
     		});
     		portal.CurrentUser.Accounts = tempAccount;
     		tempAccount = null;
     	},
     	 // 按字符计算长度
	    GetTextLength: function(text) {
			var length = String(text)[RP](/[^\x00-\xff]/g, 'aa')[LN];
	        length = Math.ceil(length/2);
	    	return length;
	    },
	    
	        //获取 Repost 对象
	    getRepost:function(){
	      return Repost;
	    },
	    //获取Comment对象
	    getComment:function (){
 	        return  Comment;
	    },
     	//暂无
     	//div: 要显示暂无的div对象
     	not: function (div) {
     		div.html(' <div class="no_data"><div class="com_fLeft nodata_tip"></div><span  class="com_fLeft nodata_tipLan">对不起，暂时没有数据！</span></div>')
     	},
     	not2: function(div) {
     		div.html(' <div class="hp_data"><div class="com_fLeft nodata_tip"></div><span  class="com_fLeft nodata_tipLan">对不起，暂时没有数据！</span></div>')
     	},
     	imageMaxCount: 4
    };
   
    return _base;
});