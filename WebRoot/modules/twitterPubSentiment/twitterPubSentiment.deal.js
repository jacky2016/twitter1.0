//舆情处理
define(['./portal'], function (context) {
    //定义变量
    var key = 'pubSentimentDeal',
          $key, //当前的作用域
          base,
          navigationBars,
          _enum,
          
          contentDiv,
          titleDiv,
          taskCollectionGroup,
          taskCollectionDiv,
          startTime,
          endTime,
          searchBtn,
          btnOneHtml,
          alertBtnHtml,
          source;
          
	function Init() {
		NavigationInit();
	   	GetGroup(function(collection) {
	   		Search(collection);
	   	});
	   	SourceDataBind();
	   	DatePackerBind();
	}
	//导航条初始化方法
	function NavigationInit () {
    	navigationBars.Empty()
    	.Add(key, 'NavToDeal', '微博舆情')
    	.Add(key, 'NavToDeal', '舆情处理')[oL]();
    }
	//绑定日期控件，开始时间往回推7天
	function DatePackerBind () {
		startTime.val($.getDate());
		startTime.datePicker();
		var startTimeValue = startTime.val();
		startTimeValue = $.addDate('d', -7, startTimeValue);
        startTimeValue = startTimeValue.Format('YYYY-MM-DD');
        startTime.val(startTimeValue);
	   	endTime.datePicker();
	}
  	//切换页签方法    
	function TabChange(collection) {
		titleDiv[EV](CK, function() {
	  		var $this = $(this),
	  			   name = 'zhao_back';
			titleDiv[RC](name);
	  		$this[AC](name);
	  		
	  		if (collection[LN] > 0) {
				LoadList();
	  		} else {
	  			base.not(taskCollectionDiv);
	  		}
		}).eq(0)[TR](CK);
	}
    //获取列表参数方法
	function GetParam() {
	  	var param = {};
	  	param.action='deal.getTaskCollcetList';
	  	param.startTime = startTime.val();
	  	param.endTime = endTime.val();
	  	param.platform = $('#tps1_source')[AT]('value');
	  	param.groupid = $('#tps1_taskCollectionGroup')[AT]('value');
	  	param.type = $('.zhao_back', $key)[AT]('gid');
	  	return param;
	}
    //获取列表方法 
	function LoadList() {
		if(base.IsRegExpDateTime(startTime, endTime, TE)) {
	  		taskCollectionDiv.html('');
	  		var gid = $('#deal_main', $key)[FD]('.zhao_back')[AT]('gid');
	  		var btnHtml = '';
			if(gid == 0) {
  				btnHtml = alertBtnHtml + '{pmi:cbfx_monitor_3}{pmi:yqcl_lgnore_1}' + btnOneHtml;
	  		} else {
	  			btnHtml = alertBtnHtml + '{pmi:cbfx_monitor_3}' + btnOneHtml;
	  		}
	  		
	  		var pager = $('#tps_pager1', $key);
	  		pager.html('');
	  		context.Base.DataBind({
		 		panel: taskCollectionDiv, 
		 		pager: pager,
		 		id: 'yqcl2',
		 		param: GetParam(),
		 		moduleType: _enum.ModuleType.Deal,//模块入口类型
		 		template: base.FormatPmi(btnHtml),
		 		onComplete: function (This, refresh, data) {
		 			//忽略按钮功能
		 			//deal.updateState
		 			var neglectBtn = $('.neglectBtn', This);
		 			if(neglectBtn[LN] > 0) {
			 			 neglectBtn[EV](CK, function () {
			 			 	var $this = $(this),
			 			 		   index = neglectBtn[ID]($this),
			 			 		   collectid = data.rows[index].id;
			 			 	$(BY)[AJ]({
					      		param: { action: 'deal.updateState', collectid: collectid },
					      		dataType: 'text',
					      		success: function(data) {
					      			if(data) {
					      				LoadList();
					      			} else {
					      				$[MB]({content: '忽略失败,请与管理员联系', type: 2});
					      			}
					      		}
					      	});
			 			 });
		 			}
		 		},
				onSend:function (btn, txt, isChecked, accountCollection, collectID, url,  type, twitterID, closeBtn) {
	     			//$.alertParam(txt, isChecked, accountCollection, collectID, url,  type, twitterID, closeBtn);
	 				var param = {};
					param.action = type == 'repost'? 'deal.saveRepost': 'deal.saveComment';
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
					var accountids = [], platforms = [] ,accountNames= [];
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
							if(data=='"发布成功"') {
								//更新状态到已处理里
								$(BY)[AJ]({
						      		param: { action: 'deal.updateProcessed', collectid: collectID },
						      		dataType: 'text',
						      		success: function(data) {
						      			if(data) {
						      				$[MB]({content: '提交成功', type: 0, isAutoClose: TE});
						      				LoadList();
						      			} else {
						      				$[MB]({content: '更新已处理状态失败,请与管理员联系', type: 2});
						      			}
						      			btn[AT]('isclick', TE);
						      		}
						      	});
								//closeBtn[TR](CK); //关闭子页面
							} else {
								$[MB]({content: data, type: 2});
								btn[AT]('isclick', TE);
							}
						}
					});
				}
			});
		}
	}
    //搜索方法
	function Search(collection) {
		searchBtn[EV](CK, function() {
			$('#tps_pager1', $key).html('');
			if (collection[LN] > 0) {
				LoadList();
			} else {
				$[MB]({
					content: '收集信息，未添加类别',
					type: 2
				});
			}
		});
  	}
    //绑定平台类型下拉菜单
  	function SourceDataBind() {
		source.html('').input({
			id:"tw1_3",
			collection:[{text:'新浪',value:1}]
 		});
  	}
    //获取组数据(Ajax)
	function GetGroup(fn) {
      	$(BY)[AJ]({
      		param: { action: 'deal.getTaskCollcetionGroup' },
      		success: function(data) {
      			var collection = [];
      			$[EH](data,function(index, item) {
      				collection.push({text: item.groupName, value: item.id});
      			});
      			GroupDataBind(collection);
      			TabChange(collection);
      			fn(collection);
      		}
      	});
  	}
    //绑定组数据下拉菜单
	function GroupDataBind(collection) {
		taskCollectionGroup.html('').input({
			id:"tw1_4",
			collection:collection
		});
	}

    return {
    	IsLoad: FE,
        OnLoad: function (_ContentDiv) {
            var t = this;
            contentDiv = _ContentDiv;
            $('<div />').load('modules/twitterPubSentiment/twitterPubSentiment.deal.htm', function () {
            	base = context.Base;
            	contentDiv[AP]($(this).html());
            	t.IsLoad = TE;
                $key = $('#' + key);
                navigationBars = context.NavigationBars;
                _enum = context.Enum;
                
                titleDiv = $('.zhao_change', $key); //页签集合对象
                taskCollectionGroup = $('#tps1_taskCollectionGroup', $key); //组下拉菜单对象
                taskCollectionDiv = $('#tps_TaskCollectionDiv', $key); //显示列表div对象
                startTime = $('#tps1_sTime', $key); //开始时间
                endTime = $('#tps1_eTime', $key); //结束时间
                searchBtn = $('#tps1_search', $key); //搜索按钮
                btnOneHtml = '{pmi:wdsy_repost_1}{pmi:wdsy_comment_1}';
                alertBtnHtml = '{pmi:yjfw_yjxxinsert_1}'; // 预警按钮，需要设置到权限里
                source = $('#tps1_source', $key); //平台下拉菜单对象
                Init();
            });
            return t;
        },
        //调用导航条方法
        NavToDeal: function () {
        	this.Show();
        },
        //显示模块方法
        Show:function() {
        	$key.show();
        	NavigationInit();
        	Init();
        }
    };
});