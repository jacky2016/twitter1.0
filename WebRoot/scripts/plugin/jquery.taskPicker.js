//define(['jquery'], function ($) {
(function($) {
    $.fn.taskPicker = function (o) {
        o = $[EX]({
            param: {},
            id: 'm',
            selectData: [],
            type: 'task',
            loadSucceed: function () { }, //加载成功事件
            okSucceed: function () { }, //点击确定事件
            cancelSucceed: function () { } //点击删除事件
        }, o);

        var This = this,
              ulTask = '<ul class="task">{0}</ul>',
              categroyUI = '<h4 categroyid="{0}"  class="address_Fsorts">{1}</h4>{2}',
              ul = '<ul class="address_sorts">{0}</ul>',
              groupUI = '<li class="groupLi" categoryid="{0}" groupid="{1}">{2}</li>',
              taskUISelect = '<li class="taskSelectLi" taskid="{0}" tasktime="{2}" title="{1}"><span class="spanDel"></span>{1}</li>',
              taskUI = '<li class="taskLi" title="{4}"><input name="" type="checkbox" value="{0}" categoryid="{2}" groupid="{3}" class="cbTask" />{1}</li>',
              taskRigthCheckBoxListUI='<li><input type="checkbox" id="{0}"/><label for="{1}">{2}</label></li>',
              taskAlreadyTasks="<li  class=\"ev_wPlace\" id={0}  title=\"{2}\"><span class=\"ev_wCount\">{1}</span><span class=\"ev_wDel\"></span></li>";
              selectData = o.selectData;
              

        function DrawData(data) {
            var html = '';
            html='<h5 class="address_title">任务分类</h5>';
            $.each(data, function (i, categoryItem) {
                html += $[FO](categroyUI, categoryItem.id, categoryItem.name, DrawGroup(categoryItem.id, categoryItem.catlogs));
            });
            
            This.find('.address_lLeft').html(html);
            //组li集合
            var groupLi = $('.groupLi', This);
            groupLi.hoverTo({ over: 'hover', out: '' });
             var  ultemp =$('.event_Wadd',This);
            //点击组画任务事件
           // groupLi[UB](CK).bind(CK, function () {
           var  addressULs=  $('#addressCount_listID',This);
                 groupLi[EV](CK, function () {
                	  var t = $(this),
                      cid = t.attr('categoryid'),
                      gid = t.attr('groupid'),
                      //tasktime = t.attr('tasktime'),
                      html = '';
                      groupLi[RC]('li_back');
                      t[AC]('li_back');
                     
                html = o.type == 'task' ? DrawTask(cid, gid, data) : DrawTheme(cid, gid, data);
                //This.find('.addressCount_list').html(html);
                if(html==""){
               //html='<div>此类里没有任务</div>';
                html='<div class="no_data"><span  class="com_fLeft nodata_tipLan">此模块暂时还没数据！</span></div>';
                }
             	   $('#addressCount_listID',This).html(html);
             	           //多选框触发事件开始
                                $('input',This)[EV](CK,function(){
                                  if(this[CD]){
                                  //判断之前是否已经选择，已经选择就不再画了，没有才画
                               	var cbid=   $(this)[AT]('id');
                               	var cb_name=$(this)[NT]()[TX]();
                               	 var  shuzu=new Array();
                                  $('.ev_wPlace',This)[EH](function (){
                                  var liid=  	$(this)[AT]('id')
                                  shuzu.push(liid);
                                  });
                                  var ifpd=  ArrayPD(shuzu,cbid)
                                   if(!ifpd){
                                   	 var  pid= $(this)[AT]("id");
                                     var  name=$(this)[NT]()[TX]();
                                  	 var  blockHtml=$[FO](taskAlreadyTasks,pid,name,name);
                                   	 ultemp[AP]($(blockHtml));
                                   }
                                  }//checked判断结束
                                  //unchecked判断开始
                                  else{
										var  cancelid=$(this)[AT]('id');
										$('.ev_wPlace',This)[EH](function(){
												   if(cancelid==$(this)[AT]('id')){
												   	  $(this)[RM]();
												   	  return false;
												   }
										});
                                  }
                                  //unchecked判断结束
                                  
                        //弹出层中删除图片删除事件
	             	   	$('.ev_wDel',ultemp)[EV](CK,function(){
	             	   	var te=$(this)[PT]()[AT]('id');
				    	$(':checked',addressULs)[EH](function (){
				    		 var chec=$(this)[AT]('id');
				    		 if(te==chec){
				    		  this[CD]=false;
				    		 return  false;
				    		 } 
				    	});
	             	   	    $(this)[PT]()[RM]();
             	   				});
                         });
                   //多选框触发事件结束
                                
             	   $('.address_sorts',This).hide();
             	   t[PT]().show();
  	   		
  	   		//下面已选择任务里有任务，则上面现实的checkbox为true
  	   		var wplaces={};
  	   		$('.ev_wPlace',ultemp)[EH](function (){
  	   			  wplaces[$(this)[AT]('id')]=$(this)[AT]('id');
  	   		});
				
				$('input',addressULs)[EH](function (){
						if(wplaces[$(this)[AT]('id')]!=undefined){
								this[CD]=true;
						}
				});  	   
  	   
             	   
             //   AddTask(); //添加任务方法
               // DrawStateTask(); //画选中任务的状态
               // DrawSelectTask(); //画选中任务
            }).eq(0).trigger(CK);

            //点击情况按钮事件
            /*
            $('.clear', This)[UB](CK).bind(CK, function () {
                if ($('.spanDel', This).length > 0) {
                    selectData = [];
                    DrawStateTask(); //画选中任务的状态
                    DrawSelectTask(); //画选中任务
                } else {
                    alert('请先选中任务');
                }
            });
			*/
			
			//类名点击当前的这个展开，其他收缩
			$('.address_Fsorts',This)[EV](CK,function(){
			     $('.address_sorts',This).hide();
					$(this).next().toggle(200);
					$(this).next()[FD]('li:first').eq(0).trigger(CK);
			});
			
			
            o.loadSucceed();
             o.okSucceed();
            /*
            $('.CM_OK', This)[UB](CK).bind(CK, function () {
                o.okSucceed(selectData);
            });
            $('.CM_cancel', This)[UB](CK).bind(CK, function () {
                o.cancelSucceed();
            });
            */
        }
        
        //判断数组中存在此id没有
        function   ArrayPD(arrs,cbid){
        		for(var i=0;i<arrs.length;i++){
        			if(cbid==arrs[i]){
        			return  true;
        			}
        		}
        		return  false;
        }
        
        
        //画组方法
        function DrawGroup(categoryID, catlogs) {
            var html = '';
            $.each(catlogs, function (i, groupItem) { html += $[FO](groupUI, categoryID, groupItem.id, groupItem.name); });
            html = $[FO](ul, html);
            html+='<div class="com_clear"></div>';
            return html;
        }
        //画任务方法
        function DrawTask(categoryID, groupID, data) {
            var html = '';
            $.each(data, function (i, item) {
                if (item.id == categoryID) {
                    $.each(item.catlogs, function (j, groupItem) {
                        if (groupItem.id == groupID) {
                           // var d = groupItem.catlogTasks;
                            var d = groupItem.catlogs;
                            $.each(d, function (i, taskItem) {
                              //  var newTaskName=taskItem.taskName.length>7?taskItem.taskName.substring(0,7):taskItem.taskName;
                               // html += $[FO](taskUI, taskItem.taskID, newTaskName, categoryID, groupID, taskItem.recentlyDays,taskItem.taskName);
                             // var newTaskName=taskItem.name.length>7?taskItem.taskName.substring(0,7):taskItem.taskName;
                                html += $[FO](taskRigthCheckBoxListUI, taskItem.id, taskItem.id, taskItem.name);
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
                          //  var d = groupItem.catlogThemes;
                              var d = groupItem.catlogs;
                            $.each(d, function (i, taskItem) {
                            	// var newTaskName=taskItem.name.length>7?taskItem.name.substring(0,7):taskItem.name;
                                html += $[FO](taskRigthCheckBoxListUI, taskItem.id, taskItem.id, taskItem.name);
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

        This[AJ]({
        	param: o.param,
            success: function (data) {
                DrawData(data);
            }
        });
		return This;
	}
})(jQuery);
	//return $;
//});