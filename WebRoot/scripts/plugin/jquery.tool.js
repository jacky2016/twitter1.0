/*
* 工具插件
* version: 1.0
* author:  Sunao
* time: 2011-07-13
* compatible: IE6、7、8、9、10、firefox、Chrome
* 说明：对jquery方法扩展的一些常用小方法
*/
(function ($, window) {
    //对window公开的常量
    window.EX = 'extend';

    //新加事件
    $.fn.paste = function (fn) { this[0].onpaste = function () { fn(); }; return this; };
    $.fn.cut = function (fn) { this[0].oncut = function () { fn(); }; return this; };
    
    //文本框提示信息方法
    $.fn.txt = function (o) {
        o = $[EX]({
            text: T1
        }, o);

        var t = this,
              txt = o.text,
              color = 'color';
        t.val(txt).css(color, '#ccc').focus(function () {
            if (t.val() == '' || t.val() == txt) t.val('').css(color, '#000');
        }).blur(function () {
            if (t.val() == '' || t.val() == txt) t.val(txt).css(color, '#ccc');
        });
        return t;
    };

    //点击自己以外的区域隐藏div功能
    //格式：[{ triggerKey: 'tt', panelKey: '.datePickAll' }, { triggerKey: 't2', panelKey: '.datePickAll'}];
    $.hiddenTo = function (array) {
        $(DO)[EV](CK, function (e) {
            var target = $(e.target), hasClickKey = '-1';
            $[EH](array, function (i, arrayItem) {
                if (e.target.id == arrayItem.triggerKey) {
                    hasClickKey = arrayItem.triggerKey;
                    return FE;
                }
            });
            if (e.target.id != hasClickKey) {
                $[EH](array, function (i, arrayItem) {
                    if (target.closest(arrayItem.panelKey)[LN] == 0) {
                        $(arrayItem.panelKey).hide();
                    }
                });
            }
        });
    };

    //鼠标经过离开点击方法
    $.fn.hoverTo = function (o) {
        o = $[EX]({
            over: 'hover' //经过样式
        }, o);
        var t = this,
              over = o.over;
        t[EV](MO, function () {
            t[RC](over);
            $(this)[AC](over);
        })[EV](MU, function () {
            $(this)[RC](over);
        });
        return t;
    };

    //通用绑定事件方法(event一个方法代替bind、unbind、live、die、delegated方法)
    $.fn.event = function (o) {
        var a = arguments,
			  t = this;
        if (a[LN] == 2) {
            t.off(a[0]).on(a[0], a[1]);
        } else {
            t.off(a[0]).on(a[0], a[1], a[2]);
        }
        return t;
    };
		//通用异步请求方法
	$.fn.xkAjax = function (o) {
		o = $[EX]({
            servletName: 'core.action', //异步文本路径
            cacheName: '', //缓存名称
            param: {}, //参数
            type: 'post', //请求类型
            isCache: FE, //是否执行缓存
            dataType: 'json', //返回数据类型, 如果是简单类型返回值，这里就写text
            global: FE, //是否开启全局特性
            timeout: TimeOut, //超时时间
            success: function (data) { }, //异步加载成功后执行方法
            error: function (data, message) { 
            	if(message == 'timeout') {
            		$('div[aria-role=progressbar]')[RM]();
            		$[MB]({ content: '异步超时,请从新加载', type: 2 });
            	}
            }, //异常方法
            onProcess: function(message) {  
            	
            } // 处理方法
        }, o);
        var T = $(this),
             c = o.cacheName;
        //o.param.method = 'doGet';
        if (o.isCache) {
            if (T.data(c) == null) {
                //alert('使用缓存，第一次走ajax');
                Ajax(o);
            } else {
                //alert('使用缓存，第二次走cache');
                var json = T.data(c);
                o.success(json);
            }
        } else {
            //alert('不缓存，走ajax');
            Ajax(o);
        }
		
		//私有方法
        function Ajax(o) {
        	//alert($.toJson(o.param));
            $.ajax({
                url: xkPath + o.servletName,
                type: o.type,
                dataType: o.dataType,
                data: o.param,
                timeout: o.timeout,
                success: function (data) {
                	if (o.dataType == 'text' && typeof data == 'string' && data.indexOf('err') > -1) {
                		data = eval('(' + data + ')');
                	} 
                	if(data.err) {
                		//用户对象为空，Session超时，返回到登陆页面
                		if (data.code && data.code == 1) {
                			WD.location.href = loginPath;
                		} else {
                			$('div[aria-role=progressbar]')[RM]();
            				$[MB]({
								content: data.err, 
								type: 4,
								confirmText: '确定',
			            		cancelText: '',
								onAffirm: function (state) { 
									if(state) {
										o.onProcess(data.err);
									}
								}
							});
                		}
                	} else {
                        T.data(o.cacheName, data);
                        o.success(data);
                	}
                },
                error: function (xhr, message, errorType) {
                	
                    o.error(xhr, message, errorType);
                }
            });
        }

		return this;
    }
	
  	//全局的异步报错提示
  	/*
	$(DO).ajaxError(function (event, xhr, settings, message) {
		$.alertParam('ajax出错', 
					   		   'url: ' + settings.url, 
					   		   '编号: ' + xhr.status,
					   		   '详情: ' + xhr.statusText);
	});
	* */

    //浏览器大小改变div位置兼容方法
    $.resizeTo = function (fn) {
        var timer = null;
        $(WD)[EV]('resize.resizeTo', function () {
            if (timer == null) {
                timer = SM(function () {
                    fn();
                    timer = null;
                }, 20);
            }
        });
    };
    $.winResize = function (o) {
        o = $[EX]({
            div: null, //div对象
            input: null, //文本框对象
            time: 5000,
            onLoad: function () { },
            onHide: function () { }
        }, o);
        $[RT](function () {
            if (o.div.is(':' + VB)) $.show(o);
        });
    };

    //显示控件方法
    $.show = function (o) {
        o = $[EX]({
            div: null,
            input: null,
            time: 5000,
            onLoad: function () { },
            onHide: function () { }
        }, o);
        var div = o.div,
              input = o.input,
              offset = input[OF](),
              left = offset.left,
              top = offset.top,
              outerHeight = input[OH](),
              scrollTop = $(WD)[ST](),
              WT = $(WD)[HT](),
              divOuterHeight = div[OH](),
              time = null;
        //WT - scrollTop - top - outerHeight > divOuterHeight ? div.css({ "left": left, "top": top + outerHeight }).fadeIn(50) : div.css({ "left": left, "top": top - divOuterHeight }).fadeIn(50);
        div.css({ left: left, top: top + outerHeight }).fadeIn(200);

        //显示隐藏控件
        div.hover(function () {
            CM(time);
            o.onLoad();
        }, function () {
            time = SM(function () { div.fadeOut(200); }, o.time);
            o.onHide();
        });
    };

    //根据对象显示提示信息方法
    $.fn.alert = function (o) {
        o = $[EX]({
            obj: '', //要显示提示信息的对象
            title: '', //文字标题
            color: 'red', //文字颜色
            mode: 'H'//显示位置,V:垂直,H:水平
        }, o);
        var html = "<div class='divTipsAuto'>{0}</div>",
              node = $($[FO](html, o.title));
        return this[EH](function () {
            var $this = $(this),
                  divTipsAuto = node.clone(),
                  offset = $this[OF](),
                  left = offset.left,
                  top = offset.top,
                  color = o.color;
            $(BY)[AP](divTipsAuto);
            if (o.mode == 'V')
                divTipsAuto.css({ left: left, top: top + $this[HT]() + 2, color: color }).show();
            else
                divTipsAuto.css({ left: left + $this[WH]() + 10, top: top, color: color }).show();
        });
    };

    //显示提示信息方法
    $.alertSelf = function (o) {
        o = $[EX]({
            ex: '', //x轴坐标
            ey: '', //y轴坐标
            title: '', //文字标题
            color: 'red' //文字颜色
        }, o);
        var divTips = $[FO]('<div class="divTips">{0}</div>', o.title);
        $(BY)[AP](divTips);
        $('.divTips').css({
            left: o.ex - 5,
            top: o.ey + 10,
            color: o.color
        }).show();
    };

    //显示多个参数方法
    $.alertParam = function () {
        var a = arguments;
        for (var i = 0, p = []; i < a[LN]; i++)
            p.push('参数' + i + ":" + a[i]);
        alert(p.join('\n'));
    };
    
    // 打印日志
    $.log = function () {
        var a = arguments;
        for (var i = 0; i < a[LN]; i++) {
            console.log(a[i]);
        }
    };

    //移除提示信息方法
    $.remove = function (o) {
        o = $[EX]({
            obj: '.divTips'//要移除的对象
        }, o);
        var b = $(o.obj);
        if (b[LN] > 0) b[RM]();
    };

    //清楚左浮动方法
    $.addClearLeft = function () {
        return '<div class="' + JLC + '"></div>'
    };

    //html格式化方法
    $.format = function () {
        var a = arguments;
        if (a[LN] == 0) return null;
        var str = a[0];
        for (var i = 1; i < a[LN]; i++)
            str = str[RP](new RegExp('\\{' + (i - 1) + '\\}', 'gm'), a[i]);
        return str;
    };

    //增强版的html格式化方法，格式：{属性}
    $.eachFormat = function (template, json) {
        var array = template[MA](/\{[\s\S]+?\}/g);
        if (array == null) return template;
        if (!$.isArray(json)) {
            return formatRow(template, array, json);
        } else {
            var html = '';
            for (var index in json) {
                html += formatRow(template, array, json[index]);
            }
            return html;
        }

        //格式化一行模板方法
        function formatRow(template, array, dataRow) {
            var i = 0;
            for (i; i < array[LN]; i++) {
                var key = array[i],
                      templateKey = key[RP]('{', '')[RP]('}', ''),
                      templateValue = dataRow[templateKey];
                templateValue = templateValue == UN ? '' : templateValue;
                template = template[RP](key, templateValue);
            }
            return template;
        }
    }

    //请稍等
    $.fn.loading = function () {
        var T = this;
        //T.html('<div class="' + JQL + '"></div>');
        var o = {
            lines: 12, // 线数
            length: 7, // 线长
            width: 4, // 线宽
            radius: 10, // 圆度
            color: 'black', // 颜色
            speed: 1, // 速度
            trail: 60, // Afterglow percentage
            shadow: FE, // Whether to render a shadow
            hwaccel: FE // Whether to use hardware acceleration
        };
        var spinner = new Spinner(o).spin(T[0]);
        return T;
    };

    //暂无
    $.fn.nothing = function () {
        this.html('<p class="' + JQN + '">暂无</p>');
        return this;
    };

    //json转换字符串方法
    $.toJson = function (o) {
        if (typeof o == 'string') try { o = eval('(' + o + ')') } catch (e) { return '' };
        var d = [], l = FE, s = TE, indent = 0;
        function notify(n, v, s, f) {
            if (v && v.constructor == Array) {
                d.push((f ? ('"' + n + '":') : '') + '[');
                for (var i = 0; i < v[LN]; i++) notify(i, v[i], i == v[LN] - 1, FE);
                d.push(']' + (s ? '' : (',')));
            } else if (v && typeof v == 'object') {
                d.push((f ? ('"' + n + '":') : '') + '{');
                var len = 0, i = 0;
                for (var k in v) len++;
                for (var k in v) notify(k, v[k], ++i == len, TE);
                d.push('}' + (s ? '' : (',')));
            } else {
                if (typeof v == 'string') v = '"' + v + '"';
                d.push((f ? ('"' + n + '":') : '') + v + (s ? '' : ','));
            };
        };
        notify('', o, s, FE);
        return d.join('');
    };

    //添加遮罩层方法
    $.layer = function (o) {
        o = $[EX]({
            color: '#ccc',
            opacity: '0.4'
        }, o);

        //重置遮罩方法
        function reset() {
            if (div.is(':' + VB)) {
                div.css({
                    width: $window[WH]() + $window[SL](),
                    height: $window[HT]() + $window[ST]()
                });
            }
        }
        var $window = $(WD),
              div = $('#' + JLY);
        //添加遮罩层
        if (div[LN] == 0) {
            $(BY)[AP]('<div id="' + JLY + '"></div>');
            div = $("#" + JLY);
        }
        //布局转换
        div[BG]().css({
            width: $window[WH]() + $window[SL](),
            height: $window[HT]() + $window[ST](),
            background: o.color,
            opacity: o[OP],
            position: 'absolute',
            top: 0,
            left: 0
        }).show();

        //window滚动条事件（layer事件命名空间）
        $window[EV]('scroll.layer', function () {
            reset();
        })[EV]('resize.layer', function () {
            reset();
        });
    };

    //节点替换方法
    $.replaceNode = function (a, b) {
        var tmp = DO.createTextNode('');
        a.replaceWith(tmp);
        b.replaceWith(a);
        $(tmp).replaceWith(b, $(tmp));
    };

    //IE6下执行的代码方法
    $.IE6 = function (o) {
        if (NA[MA](/msie [6]/i)) o();
    };
    $.IE8 = function (o) {
        if (NA[MA](/msie [8]/i)) o();
    };
    $.mozilla = function (o) {
        if (NA[MA](/firefox/i)) o();
    };

    //停止文本默认选中事件方法
    $.stopSelection = function () {
        var d = DO.selection;
        if (d && d.empty) d.empty();  //IE
        else if (WD.getSelection) WD.getSelection().removeAllRanges(); //FF
    };

    //获取多选框选中ID项方法
    $.getCheckID = function (o) {
        return o.filter(':' + CD).map(function () { return this.value; }).get().join(',');
    };
    
    // 过滤不良信息
    $.filterExp = function (str) {
	    var arr = ['找小姐', '一夜情', '上门按摩', '美女上门服务',
	                    '小姐上门服务', '学生妹上门服务', '少妇上门服务', '毛片',
	                    '亚热', '一本道', '做爱', '性交', '援交', '上门按摩',
	                    '色情服务', '妓女', '窑子', '小妹服务', 'MM上门服务',
	                    '色色', '三陪', '推油', '男妓', '找男妓', '傍富婆'];
	    var flg = FE;
	    str = str.replace(/\s+/g, '');
	    $[EH](arr, function(i, item) {
	        if (str.indexOf(item) > -1) { flg = TE; return FE; }
	    });
	    return flg;
	};
})(jQuery, window);

//常量
var MM = 'mousemove',
      MO = 'mouseover',
      MU = 'mouseout',
      MD = 'mousedown',
      MP = 'mouseup',
      KU = 'keyup',
      KD = 'keydown',
      CK = 'click',
      CE = 'change',
      BD = 'bind',
      UB = 'unbind',
      TR = 'trigger',
      TG = 'toggle',
      FD = 'find',
      EH = 'each',
      AT = 'attr',
      RA = 'removeAttr',
      TM = 'trim',
      AC = 'addClass',
      RC = 'removeClass',
      ME = 'mouseenter',
      ML = 'mouseleave';
var CD = 'checked',
      DD = 'disabled',
      VB = 'visible',
      HD = 'hidden',
      SE = 'select',
      CB = 'checkbox',
      RD = 'radio',
      TA = 'textarea',
      BT = 'button',
      TX = 'text',
      DT = 'date',
      TE = true,
      FE = false,
      UN = undefined,
      WD = window,
      DO = document,
      BR = $.browser,
      NA = navigator.userAgent,
      OO = 'prototype',
      BY = 'body',
      NT = 'next',
      ID = 'index',
      PD = 'preventDefault',
      LN = 'length',
      RP = 'replace',
      SB = 'substring',
      OP = 'opacity',
      OF = 'offset',
      PO = 'position',
      WH = 'width',
      OW = 'outerWidth',
      IW = 'innerWidth',
      IH = 'innerHeight',
      OH = 'outerHeight',
      HT = 'height',
      AD = 'animated',
      AM = 'animate',
      AP = 'append',
      RM = 'remove',
      PT = 'parent',
      ST = 'scrollTop',
      SL = 'scrollLeft',
      JO = 'toJson',
      CO = 'cookie',
      WW = 'window',
      MB = 'messageBox';
var FO = 'format',
      EF = 'eachFormat',
      AJ = 'xkAjax',
      SS = 'stopSelection',
      WR = 'winResize',
      BG = 'bgiframe',
      LG = 'loading',
      NG = 'nothing',
      OT = 'hoverTo',
      KT = 'keyupTo',
      RT = 'resizeTo',
      TB = 'table',
      LS = 'list',
      CL = 'addClearLeft',
      LY = 'layer',
      MA = 'match',
      CEL = 'createElement',
      ACL = 'addClearLeft',
      STG = 'slideToggle',
      EV = 'event';
var CI = clearInterval,
      SI = setInterval,
      CM = clearTimeout,
      SM = setTimeout;
var OS = 'onSucceed',
      OC = 'onClick',
      oC = 'OnClick',
      OL = 'onLoad',
      oL = 'OnLoad',
      OE = 'onError',
      OK = 'onComplete';
var JQN = 'jquery_nothing',
      JQH = 'jquery_hover',
      JQL = 'jquery_loading',
      JLY = 'jquery_layer',
      JLC = 'jquery_clear',
      JLD = 'div[aria-role="progressbar"]';
var TP = '.divTipsAuto',
      T0 = '请选择',
      T1 = '--请输入信息--',
      T2 = '无',
      T3 = '不能为空',
      T4 = '请选择内容',
      T5 = '有非法字符',
      T6 = '只能输入英文、数字、_、.',
      T7 = '邮箱格式不正确',
      T8 = '手机号不正确',
      T9 = '日期格式不正确',
      T10 = '结束时间不能小于开始时间',
      T11 = 'IP格式不正确',
      T12 = '只能填写数字',
      TIME = 200,
      TimeOut = 60000;
var GY = 'getFullYear',
       GM = 'getMonth',
       GD = 'getDate',
       GH = 'getHours',
       GN = 'getMinutes',
       GS = 'getSeconds',
       TS = 'toString';
var RS = 'regSpecial',
       RE = 'regEmail',
       RB = 'regMobile',
       RI = 'regIP',
       RA = 'regDate',
       RL = 'regLength',
       RN = 'regNorm',
       RR = 'regNumber',
       RU = 'regUrl';