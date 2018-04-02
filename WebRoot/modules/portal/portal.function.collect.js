/*
*  收集功能类
* 
*/
define(['portal'], function(context) {
	
	function Collect($button, twitterid, platform, url, collectType) {
		var t = this;
		t.twitterid = twitterid || ''; //微博ID
		t.platform = platform; //平台
		t.url = url || ''; //微博url
		t.collectType = collectType; //收集类型，0: 我的首页，1:微博展示
		t.$button = $button; //收集按钮对象
		t.potionItemTemplate = '<option value="{id}">{groupName}</option>';
		t.categoryItemTemplate = '<li class="listMaTask_boxList"><input type="text" value="{groupName}" groupname="{groupName}" groupid="{id}" class="listMa_lCount listMa_lCount2 categoryUpdateText"/><span class="listMa_lDel categoryDelete" groupid="{id}"></span></li>';
	} 
	Collect.fn = Collect.prototype;
	Collect.proxy = $.proxy;
	Collect.fn.loadCategrop = function (fn) {
		$(BY)[AJ]({
			param: { action: 'deal.getTaskCollcetionGroup' },
     		success: function (data) { 
     			fn(data);
     		}
		});
	};
	Collect.fn.draw = function (data) {
		var t =this,
			   collectionTemplate = '<div class="giveMe_do tw_manageDo"><div class="box_count"><div class="div_style"><label class="task_name">请选择分类：</label><select name="T_place" id="selectCategory" class="task_count task_Cwdith1">{0}</select><span class="new_TwTasks" id="categoryBtn">分类管理</span></div></div><div class="list_manage com_none" id="categoryPanel"><h5>全部分类</h5><ul class="listMaTask_box">{1}</ul><div class="com_clear"></div><div class="list_mD0"><input type="text" value="" class="listMa_lCount listMa_lSel" id="categoryText"/><div class="new_sorts" id="addCategoryBtn">新建分类</div></div></div><div class="com_clear"></div><div class="push_ok"><div style=" color: red; float: left;" id="categoryTip"></div><div class="in_MsetCno" id="categoryCancel">取&nbsp;&nbsp;消</div><div class="in_MsetCok" id="categoryOK">确&nbsp;&nbsp;定</div></div><div class="com_clear"></div></div>',
			   html = '', 
			   optionHtml = '', 
			   categoryHtml = '';
		optionHtml = $[EF](t.potionItemTemplate, data);
		categoryHtml = $[EF](t.categoryItemTemplate, data);
		html = $[FO](collectionTemplate, optionHtml, categoryHtml);
		return html;
	};
	Collect.fn.open = function () {
		var t= this;
		t.$button[EV](CK, function () {
			var $this = $(this),
				   buttonText = $this.text();
			if ( buttonText == '收集' ) {
				t.loadCategrop(function(data) {
					var html = t.draw(data);
					$[WW]({
						css: { width: '500px', height: 'auto' },
						title: '收集信息',
						content: html,
						onLoad: function (div, close) {
							t.div = div;
							t.close = close;
							t.$this = $this;
							t.option(div, close, $this);
						}
					});
				});
			} else {
				t.collectDelete($this);
			}
		});
	};
	Collect.fn.option = function (div, close, btn) {
		var t = this,
			   $selectCategory = $('#selectCategory', div), //选择分类下了菜单对象
			   $categoryPanel = $('#categoryPanel', div), //选择分类面板
			   $categoryBtn = $('#categoryBtn', div), //选择分类按钮
			   $categoryDelete = $('.categoryDelete', div), //删除分类按钮
			   $categoryText = $('#categoryText', div), //输入的分类文本值
			   $addCategoryBtn = $('#addCategoryBtn', div), //新建分类按钮
			   $categoryTip = $('#categoryTip', div), //添加类别错误提示标签
			   $categoryUpdateText = $('.categoryUpdateText', div), //更新文本框集合
			   $categoryOK = $('#categoryOK', div), //弹出层确认按钮
			   $categoryCancel = $('#categoryCancel' ,div); //弹出层取消按钮
		
		//显示隐藏Panel
		$categoryBtn[EV](CK, function() {
			$categoryPanel[STG]();
		});
		
		//设置默认值
		$categoryText.txt({
			text: '请输入分类名称'
		});
		
		//添加
		$addCategoryBtn[EV](CK, function () {
			$categoryTip.text('');
			if(t.inputRegExp($categoryText, $categoryTip)) {
				t.insert($categoryText, $categoryPanel, $selectCategory, $categoryTip);
			}
		});
		
		//更新
		$categoryUpdateText[EV]('blur', function() {
			var $this = $(this),
				   oldGroupName = $this[AT]('groupname'),
				   newGroupName = $[TM]($this.val());
			if(t.inputRegExp($this, $categoryTip)) {
				//修改的名称变化之后在异步
				if (oldGroupName != newGroupName) {
					$categoryTip.text('');
					t.update($this, $categoryPanel, $selectCategory, $categoryTip);
				}
			}
		});
		
		//删除
		$categoryDelete[EV](CK, function() {
			var $this = $(this),
				   groupid = $this[AT]('groupid');
			$categoryTip.text('');
			t.del($this, $selectCategory, $categoryTip);
		});
		
		//确定
		$categoryOK[EV](CK, function() {
			var param = getParam.call(t, 'save', $selectCategory);
			if(param.groupid == 0) {
				$categoryTip.text('请选择类别');
				return;
			}
			$(BY)[AJ]({
				param: param,
				dataType: 'text',
				success: function (data) {
					close();
					if(data == 0) {
						$[MB]({
							content: '收集失败，该微博不存在',
							type: 2
						});
					} else if (data == -1) {
						$[MB]({
							content: '已经收集了,不能在收集',
							type: 2
						});
					} else if (data == -2) {
						$[MB]({
								content: '收集已经达到上限,不能在收集',
								type: 2
							});
					} else {
						btn[AT]('collectid', data).text('取消收集');
					}
				}
			});
		});
		
		//取消
		$categoryCancel[EV](CK, function() {
			close();
		});
	};
	//获取参数方法
	function getParam (methodName, $selectCategory) {
		var t = this, 
			   param = {};
			   
			param.action = 'view.collectInsertAndDelete';
			if($selectCategory == null) {
				param.groupid = 0;
			} else {
				param.groupid = $selectCategory.val() || 0;
			}
			param.twitterid = t.twitterid;
			param.platform = t.platform;
			param.url = t.url;
			param.collectType = t.collectType; //收集类型，0: 我的首页，1:微博展示
			param.methodName = methodName;
			return param;
	}
	Collect.fn.insert = function($categoryText, $categoryPanel, $selectCategory, $categoryTip) {
		var t = this,
			   groupName = $[TM]($categoryText.val());
		$(BY)[AJ]({
			param: { action: 'view.getTaskGroupsInsert', groupName: groupName},
			success: function (data) {
				if (data.id == -1) {
					$categoryTip.text('已经存在,请重新添加');
					$categoryText.val('');
				} else if (data.id == 0) {
					$categoryTip.text('添加失败');
				} else {
					$categoryPanel[FD]('.listMaTask_box')[AP]($[EF](t.categoryItemTemplate, data));
					//添加一项Option
					$selectCategory.addOption(data.groupName, data.id);
					$categoryText.val('');
					t.option(t.div, t.close, t.$this);
				}
			}
		});
	};
	Collect.fn.update = function(input, $categoryPanel, $selectCategory, $categoryTip) {
		var t = this,
			   groupName = $[TM](input.val()), 
		       groupid = input[AT]('groupid');
		$(BY)[AJ]({
			param: { action: 'view.getTaskGroupsUpdate', groupName: groupName, groupId: groupid},
			dataType: 'text',
			success: function (data) {
				//data，-1存在，>0：成功，0：失败
				if (data == -1) {
					$categoryTip.text('已经存在,请重新修改');
				}else if (data == 0) {
					$categoryTip.text('更新失败');
				} else {
					input.val(groupName)[AT]('groupname', groupName);
					//更新选择类别select项名称
					$selectCategory[FD]('option')[EH](function() {
						var $this = $(this),
							   id = $this[AT]('value');
						if(id == groupid) {
							$this[0].text = groupName;
							return FE;
						}
					});
				}
			}
		});
	};
	Collect.fn.del = function(btn, $selectCategory, $categoryTip) {
		var groupid = btn[AT]('groupid'),
			   groupName = $[TM](btn.prev().val());
		$(BY)[AJ]({
			param: { action: 'view.getTaskGroupsDelete', groupId: groupid},
			dataType: 'text',
			success: function (data) {
				if(data) {
					btn[PT]()[RM]();
					$selectCategory.removeItem(groupid);
				} else {
					$categoryTip.text('收集失败，该微博不存在');
				}
			}
		});
	};
	Collect.fn.inputRegExp = function (input, lable) {
		var text = $[TM](input.val()),
			   val = '请输入分类名称';
		if (text == val) text = '';
		if(text == '') { lable.text(val); return FE;} 
		if(!$[RS](text)) { lable.text('类别名称不能有特殊字符'); return FE;} 
		if(text[LN] > 7)  { lable.text('类别名称长度不能大于7'); return FE;} 
		return TE;
	};
	Collect.fn.collectDelete = function (btn) {
		var t =this, 
			   param = getParam.call(t, 'delete', null);
		param.collectid = btn[AT]('collectid');
		$(BY)[AJ]({
			param: param,
			dataType: 'text',
			success: function (data) {
				if(data == 0) {
					$[MB]({
						content: '取消收集失败,请与管理员联系',
						type: 2
					});
				} else  if(data == -1) {
					$[MB]({
						content: '该微博在已处理或忽略里了,不能删除',
						type: 2
					});
				} else {
					btn[AT]('collectid', '0').text('收集');
				}
			}
		});
	};
	
	//判断select项中是否存在值为value的项
	$.fn.isExistItem = function (value) {
	    var isExist = false;
	    var count = this.size();
	    for (var i = 0; i < count; i++) {
	    	if($(this).get(0).options[i] != UN) {
		        if ($(this).get(0).options[i].value == value) {
		            isExist = true;
		            break;
		        }
	    	}
	    }
	    return isExist;
	};

	//向select中添加一项，显示内容为text，值为value,如果该项值已存在，则提示
	$.fn.addOption = function (text, value) {
	    if ($(this).isExistItem(value)) {
	        alert("待添加项的值已存在");
	    } else {
	        $(this).get(0).options.add(new Option(text, value));
	    }
	};
	
	//删除select中值为value的项，如果该项不存在，则提示
	$.fn.removeItem = function (value) {
        var count = this[FD]('option').size();
        for (var i = 0; i < count; i++) {
            if ($(this).get(0).options[i].value == value) {
                $(this).get(0)[RM](i);
                break;
            }
        }
	};
	
	return Collect;
});