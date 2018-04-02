(function ($) {
    $.fn.extend({
        'taskPicker': function (o) {
            o = $.extend({
                url: null,
                methodName: '',
                param: '',
                id: 'm',
                selectData: [],
                type: 'task',
                loadSucceed: function (selectData) { }, //加载成功事件
                okSucceed: function (selectData) { }, //点击确定事件
                cancelSucceed: function () { } //点击删除事件
            }, o);

            var This = this,
                  ul = '<ul>{0}</ul>',
                  ulTask = '<ul class="task">{0}</ul>',
                  categroyUI = '<h3 categroyid="{0}">{1}</h3>{2}',
                  groupUI = '<li class="groupLi" categoryid="{0}" groupid="{1}">{2}</li>',
                  taskUISelect = '<li class="taskSelectLi" taskid="{0}" tasktime="{2}" title="{1}"><span class="spanDel"></span>{1}</li>',
                  taskUI = '<li class="taskLi" title="{5}"><input name="" type="checkbox" value="{0}" categoryid="{2}" groupid="{3}" tasktime="{4}" class="cbTask" />{1}</li>',
                  selectData = o.selectData;

            function DrawData(data) {
                var html = '';
                $.each(data, function (i, categoryItem) {
                    html += $[FO](categroyUI, categoryItem.id, categoryItem.name, DrawGroup(categoryItem.id, categoryItem.catlogs));
                });
                This.find('.left').html(html);
                //组li集合
                var groupLi = $('.groupLi', This);
                groupLi.hoverTo({ over: 'hover', out: '' });
                //点击组画任务事件
                groupLi[UB](CK).bind(CK, function () {
                    var t = $(this),
                          cid = t.attr('categoryid'),
                          gid = t.attr('groupid'),
                          tasktime = t.attr('tasktime'),
                          html = '';
                         
                    html = o.type == 'task' ? DrawTask(cid, gid, data) : DrawTheme(cid, gid, data);
                    This.find('.taskselect').html(html);
                    AddTask(); //添加任务方法
                    DrawStateTask(); //画选中任务的状态
                    DrawSelectTask(); //画选中任务
                }).eq(0).trigger(CK);

                //点击情况按钮事件
                $('.clear', This)[UB](CK).bind(CK, function () {
                    if ($('.spanDel', This).length > 0) {
                        selectData = [];
                        DrawStateTask(); //画选中任务的状态
                        DrawSelectTask(); //画选中任务
                    } else {
                        alert('请先选中任务');
                    }
                });

                o.loadSucceed();
                $('.CM_OK', This)[UB](CK).bind(CK, function () {
                    o.okSucceed(selectData);
                });
                $('.CM_cancel', This)[UB](CK).bind(CK, function () {
                    o.cancelSucceed();
                });
            }
            //画组方法
            function DrawGroup(categoryID, catlogs) {
                var html = '';
                $.each(catlogs, function (i, groupItem) { html += $[FO](groupUI, categoryID, groupItem.id, groupItem.name); });
                html = $[FO](ul, html);
                return html;
            }
            //画任务方法
            function DrawTask(categoryID, groupID, data) {
                var html = '';
                $.each(data, function (i, item) {
                    if (item.id == categoryID) {
                        $.each(item.catlogs, function (j, groupItem) {
                            if (groupItem.id == groupID) {
                                var d = groupItem.catlogTasks;
                                $.each(d, function (i, taskItem) {
                                    var newTaskName=taskItem.taskName.length>7?taskItem.taskName.substring(0,7):taskItem.taskName;
                                    html += $[FO](taskUI, taskItem.taskID, newTaskName, categoryID, groupID, taskItem.recentlyDays,taskItem.taskName);
                                });
                                return FE;
                            }
                        });
                        return FE;
                    }
                });
                return html;
            }
            
            //画主题方法
            function DrawTheme(categoryID, groupID, data) {
                var html = '';
                $.each(data, function (i, item) {
                    if (item.id == categoryID) {
                        $.each(item.catlogs, function (j, groupItem) {
                            if (groupItem.id == groupID) {
                                var d = groupItem.catlogThemes;
                                $.each(d, function (i, taskItem) {
                                    var newTaskName=taskItem.name.length>7?taskItem.name.substring(0,7):taskItem.name;
                                    html += $[FO](taskUI, taskItem.id, newTaskName, categoryID, groupID, '',taskItem.name);
                                });
                                return FE;
                            }
                        });
                        return FE;
                    }
                });
                return html;
            }

            //添加任务方法
            function AddTask() {
                $('.cbTask', This)[UB](CK).bind(CK, function () {
                    var t = $(this),
                          taskid = t.val(),
                          taskName = $.trim(t.parent().text()),
                          tasktime = t.attr('tasktime');
                    if (t.is(':' + CD)) {
                        AddSelectData(taskid, taskName, tasktime);
                    } else {
                        RemoveSelectData(taskid);
                    }
                    DrawStateTask();
                    DrawSelectTask(); //调用画选中任务方法
                });
            }
            //画选中任务状态
            function DrawStateTask() {
                $('.cbTask', This).removeAttr(CD);
                $('.cbTask', This).each(function () {
                    var t = $(this);
                    $.each(selectData, function (i, item) {
                        if (t.val() == item.taskID) {
                            t.attr(CD, TE);
                            return FE;
                        }
                    });
                });
            }
            //画选中任务方法
            function DrawSelectTask() {
                var html = '';
                $.each(selectData, function (i, item) {
                    html += $[FO](taskUISelect, item.taskID, item.taskName, item.taskTime);
                });
                $('.selected', This).find('ul').html(html);
                //删除选中的任务
                $('.spanDel', This)[UB](CK).bind(CK, function () {
                    var taskID = $(this).parent().attr('taskid');
                    RemoveSelectData(taskID);
                    DrawStateTask();
                    DrawSelectTask();
                });
            }

            //添加选择数据
            function AddSelectData(taskID, taskName, taskDate) {
                var type = TE;
                $.each(selectData, function (i) {
                    if (selectData[i].taskID == taskID) {
                        type = FE;
                        return FE;
                    }
                });
                if (type) selectData.push({ "taskID": taskID, "taskName": taskName, "taskTime": taskDate });
            }
            //删除选择数据
            function RemoveSelectData(taskID) {
                $.each(selectData, function (i) {
                    if (selectData[i].taskID == taskID) {
                        selectData.splice(i, 1);
                        return FE;
                    }
                });
            }

            if (o.param == '') {
                o.url[o.methodName]({
                    callback: function (data) {
                        DrawData(data);
                    },
                    timeout: XKWA.TimeOut//,
                    //errorHandler: function (message) { alert("Oops: " + message); }
                });
            } else {
                o.url[o.methodName](o.param, {
                    callback: function (data) { 
                    	DrawData(data) 
                	},
                    timeout: XKWA.TimeOut//,
                    //errorHandler: function (message) { alert("Oops: " + message); }
                });
            }

            return This;
        }
    });
})(jQuery);