define(['portal'], function (context) {
	var key = 'navigation',
		   $key,
		   navigationBarsDatas = [],
		   navigation;
    return {
        Init: function (_programDiv) {
        	$key = $('#' + key, _programDiv);
        },
        //清空所有导航项
        Empty: function () {
        	navigationBarsDatas = [];
        	return this;
        },
        //添加一条导航项
        Add: function (moduleKey, methodName, value) {
        	navigationBarsDatas.push({key: moduleKey, method: methodName, value: value});
        	return this;
        },
        //根据Key移除掉后面所有的
        Remove: function (moduleKey) {
        	$[EH](navigationBarsDatas, function (i, item) {
        		if(item.key == moduleKey) {
        			navigationBarsDatas.splice(i, navigationBarsDatas[LN]);
        		}
        	});
        	return this;
        },
        //加载导航条事件
        OnLoad: function () {
        	var homeHtml = '<span class="in_span"></span>',
        		   itemHtml = '<span class="in_secSpan" key="{1}" methodName="{2}">{0}</span>',
        		   html = '';
            html += homeHtml;
        	$[EH](navigationBarsDatas, function(i, item) {
        		html += $[FO](itemHtml, item.value, item.key, item.method);
        	});
        	$key.html(html);
        	this[oC]();
        },
         //点击导航项事件
        OnClick: function (handle) {
        	var t = this,
        		   item = $('.in_secSpan', $key);
        	item[EV](CK, function() {
        		var t = $(this),
        			   moduleKey = t[AT]('key'),
        			   methodName = t[AT]('methodName');
        	    
        	    context.Mediator.NavReflectionMethod(moduleKey, methodName);
        	});
        }
    };
});