//舆情展示
define(['./portal'], function (context) {
    //定义变量
    var key = 'pubSentimentView',
          $key, //当前的作用域
          base,
          _enum,
          navigationBars,
          title,
          
          contentDiv,
          TaskGroupsDiv,
          FirstStr,
          SecondStr,
          ThirdStr,
          startTimeInput,
          endTimeInput,
          searchBtn,
          excelBtn,
          Scpanel,
          FenLeiContent,
          CaiDanContent,
          content,
          screenSelect,
          orderSelect,
          platformSelect,
          titlehtml;
     
 	//私有方法
    //初始化方法，写自己模块的业务
    function Init() {
    	NavigationInit();
    	GetTaskGroups();
    	
    	//设置日历
    	startTimeInput.datePicker();
    	endTimeInput.datePicker();

    	DoSearch();
    	DoExcel();
    	ScreenDataBind();
    	OrderDataBind();
    	PlatformDataBind();
    }
    
	//导航条初始化方法
	function NavigationInit () {
    	navigationBars.Empty()
    	.Add(key, 'NavToView', '微博舆情')
    	.Add(key, 'NavToView', '舆情展示')[oL]();
    }
	
	//获取任务数据（讯库任务）
	function GetTaskGroups() {
		TaskGroupsDiv[AJ]({
			param: { action: 'view.getTaskGroupsList' },
     		success: function (data) { 
     			content.html('');
     			title.html('');
     			DrawTaskGroups(data);
     		}
		});
	}
 	
 	//画任务
 	//data: 数据
	function DrawTaskGroups(data) {
		var categoryTemplate ='<div class="list_style" id="list_style{1}" gid="{1}" title="{3}"><div class="tw_tipPic com_none"></div><span class="tw_tipPicTitle">{0}</span><div class="twitter_listAll com_none" id="twitter_listAll{1}" gid="{1}">{2}</div>',
               groupTemplate='<div id="viewGroup{1}" class="list_secNavi" gid="{1}"><h4 title="{0}">{0}</h4><p>{2}</p></div>',
               taskTemplate='<span class="taskItem" gid ="{1}" searchTime="{2}" title="{0}" >{0}</span>{3}';
     	TaskGroupsDiv.html('');
     	$[EH](data, function(i, dataRow) {
     		var categoryHtml = '',
     			   groupHtml = '';
     			   
     	    $[EH](dataRow.groups, function(j, groupRow) { 
     	    	var taskHtml = '';
	     	    $[EH](groupRow.tasks, function(k, taskRow) { 
	     	    	taskHtml += $[FO](taskTemplate, taskRow.name, taskRow.id, taskRow.searchTime, k == groupRow.tasks[LN] - 1 ? '' : '<span>|</span>');
	     	    });
     	    	groupHtml += $[FO](groupTemplate, groupRow.groupname, groupRow.id, taskHtml);
     	    });
     	    var caName = dataRow.groupname[LN] > 5 ? dataRow.groupname.substring(0, 5): dataRow.groupname;
     		categoryHtml += $[FO](categoryTemplate, caName, dataRow.id, groupHtml, dataRow.groupname);
     		TaskGroupsDiv[AP](categoryHtml);
     	});	
     	//后续操作
     	var groupDivs = $('.tw_tipPicTitle'),
     		   taskDivs=$('.taskItem', TaskGroupsDiv),
     		   taskPanels = $('.twitter_listAll',  TaskGroupsDiv);
     		   
 		taskPanels[EH](function() {
 			var $this = $(this);
 			if($this.text() == '') {
 				$this.prev().prev()[RM]();
 				$this[RM]();
 			}
 		});
     	GroupsHover(groupDivs);
     	LoadTaskList(taskDivs);
     }
     
     //鼠标经过离开事件
     //groupDivs: 组div集合
     
     function GroupsHover(groupDivs) {

		var styleName='com_none';
		var timer;
     	groupDivs[EV](MO, function(e) {
     		var $this = $(this),
				   ThisDiv = $this.next();
				   
     		$this.prev()[RC](styleName);
     		$this.next()[RC](styleName);
     		
     		
     		if(ThisDiv[LN] > 0) {
	     		var ThisDivLeft = $this[OF]().left+ThisDiv[WH](),
	     			    panel = $('#tps_TaskGroups', $key),
	     			    panelLeft = panel[OF]().left + panel[WH](),
	     			    $thisLeft = $this[OF]().left + $this[WH]() + 30;
	     		if(ThisDivLeft >= panelLeft) {
	     			ThisDiv.css({
	     				left: $thisLeft - ThisDivLeft
	     			})
	     		}
     		}
     		ThisDiv[EV](MO, function(e) {
     			CM(timer);
     			ThisDiv[EV](MU, function() {
	     			var $this = $(this);
	     			timer = SM(function(){
	     				$this.prev().prev()[AC](styleName);
						$this[AC](styleName);
	     			},200);
	 			});
     		});
     	})[EV](MU, function() {
     		var $this = $(this);
     		timer = SM(function(){
 				$this.prev()[AC](styleName);
				$this.next()[AC](styleName);
 			},200);
     	});
     }
     
     //点击任务读取列表方法
     function LoadTaskList(taskDivs) {
     	var titleTemplate = '<span>舆情分类:</span><span class="span_navi">{0}</span><span>-</span><span class="span_navi">{1}</span><span>-</span><span class="span_navi">{2}</span>';
     	taskDivs[EV](CK, function() {
     		var $this=$(this);
     		
     		//设置选中任务样式
     		taskDivs[RC]('three_seled');
     		$this[AC]('three_seled');
     		
     		//设置顶部选中任务提示
			var html = $[FO](titleTemplate,$this[PT]()[PT]()[PT]()[PT]()[AT]('title'), $this[PT]().prev()[AT]('title'), $this[AT]('title'));
     		title.html(html);
     		
     		//设置任务的时间
     		var searchTimeNum = parseInt($this[AT]('searchtime')),
     			   startTime = '',
     			   date = new Date(); //时间对象
     		var month = date[GM]() + 1;
     		if(month<10) month = '0' +month;
            endTime = date[GY]() + '-' + month + '-' + (date[GD]()),
     		startTime = $.addDate('d', -searchTimeNum, endTime).Format('YYYY-MM-DD');
     		startTimeInput.val(startTime);
     		endTimeInput.val(endTime);
     		
     		var param = GetParam();
     		LoadList(content, param);
     	}).eq(0)[TR](CK);
     }
     
     //点击搜索按钮搜索列表数据
     function DoSearch() {
     	searchBtn[EV](CK, function() {
     		if(base.IsRegExpDateTime(startTimeInput, endTimeInput, TE)) {
	     		var param = GetParam();
	     		$('#tps_Pager', $key).html('');
	     		if(param.taskid != UN) {
	     			LoadList(content, param);
	     		} else {
	     			$[MB]({
						content: '请选择任务', 
						type: 2
					});
	     		}
     		}
     	});
     }
     
     //获取列表参数
	function GetParam() {
		var param = {};
		param.action='view.getTaskTwitterList';
		param.taskid = $('.three_seled', $key)[AT]('gid'); //获取选中样式的任务gid
		param.startTime = startTimeInput.val();
		param.endTime = endTimeInput.val();
		param.screen = screenSelect[AT]('value'); //筛选
		param.timeSort = orderSelect[AT]('value'); //排序
		param.platform = platformSelect[AT]('value'); //平台
     	return param;
     }
     
     //导出Excel
     function DoExcel() {
     	excelBtn[EV](CK, function() {
     		alert(222);
     	});
     }
     
     //读取列表信息
     function LoadList(div, param) {
     	div.html('');
     	var pager = $('#tps_Pager', $key),
     		  isSina = FE,
     		  btnHtml = base.FormatPmi('{pmi:wdsy_repost_1}{pmi:wdsy_comment_1}');
     	if (param.platform == 1)  {
     		isSina = TE;
     		btnHtml = base.FormatPmi('{pmi:yjfw_yjxxinsert_1}{pmi:cbfx_monitor_3}{pmi:yqzs_collect_1}{pmi:wdsy_repost_1}{pmi:wdsy_comment_1}');
     	}
     	pager.html('');
     	base.DataBind({
     		panel: div, 
     		pager: pager,
     		id: 'viewPanel1',
     		isFlag: base.isAllExpire,
     		param: param,
     		template: btnHtml, //<span class="moniterBtn" moniterid="0">监控</span><span>|</span><span class="collectBtn" collectid="0">收集</span>
     		onComplete: function (This, refresh, data) {
     			if(!isSina) {
     				$('.commentCount', This)[RC]('commentCount')[AC]('cont_noDO');
     				$('.repostCount', This)[RC]('repostCount')[AC]('cont_noDO');
     			}
     		},
     		onProcess: function(message) {
     			var selected = $('.three_seled', $key);
     			selected.next()[RM]();
     			selected[RM]();
     		},
     		onSend:function (btn, txt, isChecked, accountCollection, postID, url,  type, twitterID, closeBtn) {
     			//$.alertParam(txt, isChecked, accountCollection, postID, url,  type, twitterID, closeBtn);
     			var param = {};
				param.action = type == 'repost'? 'view.saveRepost': 'view.saveComment';
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
				alert($.toJson(param));
				$(BY)[AJ]({
					param: param,
					dataType: 'text',
					success: function(data) {
						if(data=='"发布成功"') {
							$[MB]({content: '提交成功', type: 0});
							setTimeout(function() {
								$('#_mbox')[RM]();
								$('#jquery_layer')[RM]();
							},1000);
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
    
     //绑定筛选下拉菜单
     function ScreenDataBind() {
     	screenSelect.input({
     		id: 'search.screen',
     		collection: [{text:'全部',value:0},
     						   {text:'原创',value:1},
     						   {text:'转发',value:2}]
     	});
     } 
     //绑定排序下拉菜单
     function OrderDataBind() {
     	orderSelect.input({
     		id: 'search.sort',
     		collection: [{text:'时间',value:0},
     						   {text:'转发',value:2},
     						   {text:'评论',value:3}]
     	});
     }
     
     //绑定平台下拉菜单
     function PlatformDataBind() {
     	platformSelect.input({
     		id: 'search.platform',
     		collection: [{text:'新浪',value:1},
     					    {text:'腾讯',value:2},
     					    {text:'人民',value:5}] // {text:'全部',value:0},
     	});
     }
     
    return {
    	IsLoad: FE,
        OnLoad: function (_ContentDiv) {
            var t = this;
            contentDiv = _ContentDiv;
            $('<div />').load('modules/twitterPubSentiment/twitterPubSentiment.view.htm', function () {
            	base = context.Base;
            	contentDiv[AP](base.FormatPmi($(this).html()));
            	t.IsLoad = TE;
                $key = $('#' + key);
                _enum = context.Enum;
                navigationBars = context.NavigationBars;
                
                title = $('#twitterView_title', $key); //选择任务标题提示条
                
                TaskGroupsDiv = $('#tps_TaskGroups', $key);
                FenLeiContent='<div class="giveMe_do tw_manageDo"><div class="box_count" ><div class="div_style"><label class="task_name">请选择分类：</label><select id="tps_taskCollectionGroup" name="T_place" class="task_count task_Cwdith1" ><option>全部分类</option><option>分类一</option></select><span class="new_TwTasks">分类管理</span></div></div><div class="list_manage com_none"><h5>全部分类</h5><ul class="listMaTask_box"><li class="listMaTask_boxList"><input type="text" value="共识要共识要共识要" class="listMa_lCount listMa_lCount2" /><span class="listMa_lDel"></span><label class="listMa_lEdit"></label></li></ul><div class="com_clear"></div><div class="list_mD0"><input type="text" class="listMa_lCount listMa_lSel"/><div class="new_sorts">新建分类</div></div></div><div class="com_clear"></div><div class="push_ok"><div class="in_MsetCno">取&nbsp;&nbsp;消</div><div class="in_MsetCok">确&nbsp;&nbsp;定</div></div><div class="com_clear"></div></div>';
                CaiDanContent='<dl class="listMaTask_do"><dd>编辑</dd><dd>删除</dd></dl>';

                startTimeInput=$('#tps_starTime', $key); //开始时间input对象
                endTimeInput=$('#tps_endTime', $key); //结束时间input对象
                excelBtn=$('#tps_exl', $key); //导出Excel按钮
                Scpanel=$('#tps_scpanel', $key);
                searchBtn = $('#tps_search', $key); //搜索按钮
                content=$('#tps_content', $key); //列表信息div对象
                screenSelect = $('#tps_shaiXuan', $key); //筛选select对象
                orderSelect = $('#tps_order', $key); //排序select对象
                platformSelect = $('#tps_source', $key); //平台select对象
                Init();
            });
            return t;
        },
        //调用导航条方法
        NavToView: function () {
        	this.Show();
        },
        //显示模块方法
        Show:function() {
        	$key.show();
        	Init();
        	NavigationInit();
        }
    };
});