define(['./jquery'], function ($) {
    var EX = 'extend';
    //新加事件
    $.fn[EX]({
        'paste': function (f) { this[0].onpaste = function () { f(); }; return this; },
        'cut': function (f) { this[0].oncut = function () { f(); }; return this; },
        'hoverIntent': function (f, g) { var c = { se: 7, it: 100, timeout: 0 }; c = $[EX](c, g ? { over: f, out: g} : f); var cX, cY, pX, pY; var track = function (ev) { cX = ev.pageX; cY = ev.pageY; }; var cp = function (ev, t) { t.h = clearTimeout(t.h); if ((Math.abs(pX - cX) + Math.abs(pY - cY)) < c.se) { $(t)[UB](MM, track); t.s = 1; return c.over.apply(t, [ev]); } else { pX = cX; pY = cY; t.h = setTimeout(function () { cp(ev, t); }, c.it); } }; var dy = function (ev, t) { t.h = clearTimeout(t.h); t.s = 0; return c.out.apply(t, [ev]); }; var hh = function (e) { var p = (e.type == MO ? e.fromElement : e.toElement) || e.relatedTarget; while (p && p != this) { try { p = p.parentNode; } catch (e) { p = this; } } if (p == this) { return FE; } var ev = $[EX]({}, e); var t = this; if (t.h) { t.h = clearTimeout(t.h); } if (e.type == MO) { pX = ev.pageX; pY = ev.pageY; $(t)[BD](MM, track); if (t.s != 1) { t.h = setTimeout(function () { cp(ev, t); }, c.it); } } else { $(t)[UB](MM, track); if (t.s == 1) { t.h = setTimeout(function () { dy(ev, t); }, c.timeout); } } }; return this[MO](hh)[MU](hh); }
    });
    
    //点击自己以外的区域隐藏div功能
    //格式：[{ triggerKey: 'tt', panelkey: '.datePickAll' }, { triggerKey: 't2', panelkey: '.datePickAll'}];
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
                    if (target.closest(arrayItem.panelkey)[LN] == 0) {
                        $(arrayItem.panelkey).hide();
                    }
                });
            }
        });
    };
    
    //定义讯库微博项目专用方法
	$.fn[EX]({
		//通用绑定事件方法(event一个方法代替bind、unbind、live、die、delegated方法)
		'event':function (o) {
			var a= arguments, 
				   t =this;
			if(a[LN] == 2) {
				t.off(a[0]).on(a[0], a[1]);
			}else {
				t.off(a[0]).on(a[0], a[1], a[2]);
			}
			return t;
		},
		//通用异步请求方法
        'xkAjax': function (o) {
            o = $[EX]({
                servletName: 'UserServlet', //异步文本路径
                cacheName: '', //缓存名称
                param: {}, //参数
                type: 'get', //请求类型
                isCache: FE, //是否执行缓存
                dataType: 'json', //返回数据类型, 如果是简单类型返回值，这里就写text
                global: TE, //是否开启全局特性
                timeout: TimeOut, //超时时间
                success: function (data) { }, //异步加载成功后执行方法
                error: function (data) { } //异常方法
            }, o);
            var T = $(this),
                 c = o.cacheName;
            o.param.method = 'doGet';
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
                $.ajax({
                    url: $[FO](xkPath + '{0}?a=1', o.servletName),
                    type: o.type,
                    dataType: o.dataType,
                    data: o.param,
                    timeout: o.timeout,
                    success: function (data) {
                        T.data(o.cacheName, data);
                        o.success(data);
                    },
                    error: function (xhr, message, errorType) {
                        o.error(xhr, message, errorType);
                    }
                });
            }
            
            //全局的异步报错提示
			$(DO).ajaxError(function (event, xhr, settings, message) {
				$.alertParam('ajax出错', 
							   		   'url: ' + settings.url, 
							   		   '编号: ' + xhr.status,
							   		   '详情: ' + xhr.statusText);
			});
            return this;
        }
	});

    //浏览器大小改变div位置兼容方法
    $.resizeTo = function (f) {
        var r = null;
        $(WD)[UB]('resize')[BD]('resize', function () {
            if (r == null) {
                r = setTimeout(function () {
                    f();
                    r = null;
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
        var d = o.div,
              i = o.input,
              f = i[OF](),
              l = f.left,
              t = f.top,
              h = i[OH](),
              ST = $(WD).scrollTop(),
              WT = $(WD)[HT](),
              g = d[OH](),
              t1, t2;
        //WT - ST - t - h > g ? d.css({ "left": l, "top": t + h }).fadeIn(50) : d.css({ "left": l, "top": t - g }).fadeIn(50);
        d.css({ "left": l, "top": t + h }).fadeIn(50)
        //显示隐藏控件
        d.hover(function () {
            clearTimeout(t2);
            clearTimeout(t1);
            o.onLoad();
            d.show();
        }, function () {
            t1 = setTimeout(function () { d.fadeOut(50); }, o.time);
            o.onHide();
        });
        t2 = setTimeout(function () { d.fadeOut(50); }, o.time);
    };
    
        //文本框提示信息方法
    $.fn.txt = function (o) {
        o = $[EX]({
            text: T1
        }, o);

        var T = this, txt = o.text, c = 'color';
        T.val(txt).css(c, '#ccc').focus(function () {
            if (T.val() == '' || T.val() == txt) T.val('').css(c, '#000');
        }).blur(function () {
            if (T.val() == '' || T.val() == txt) T.val(txt).css(c, '#ccc');
        });
        return T;
    };

    //鼠标经过离开点击方法
    $.fn.hoverTo = function (o) {
        o = $[EX]({
            over: 'hover' //经过样式
        }, o);
        var T = this,
              v = o.over;
        T[UB](MO)[BD](MO, function () {
            $(this)[AC](v);
        })[UB](MU)[BD](MU, function () {
            $(this)[RC](v);
        });
        return T;
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
        this[EH](function () {
            var t = $(this),
                  d = node.clone(),
                  f = t[OF](),
                  l = f.left,
                  p = f.top,
                  c = o.color;
            $(BY)[AP](d);
            if (o.mode == 'V')
                d.css({ 'left': l, 'top': p + t[HT]() + 2, 'color': c }).show();
            else
                d.css({ 'left': l + t[WH]() + 10, 'top': p, 'color': c }).show();
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
        var d = $[FO]('<div class="divTips">{0}</div>', o.title);
        $(BY)[AP](d);
        $('.divTips').css({ 'left': o.ex - 5, 'top': o.ey + 10, 'color': o.color }).show();
    };
    //显示多个参数方法
    $.alertParam = function () {
        var a = arguments;
        for (var i = 0, p = []; i < a[LN]; i++)
            p.push('参数' + i + ":" + a[i]);
        alert(p.join('\n'));
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
        for (var i = 1; i < a[LN]; i++) str = str[RP](new RegExp('\\{' + (i - 1) + '\\}', 'gm'), a[i]);
        return str;
    };

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
        var T = this;
        T.html('<p class="' + JQN + '">暂无</p>');
        return T;
    };


    //获取多选框选中ID项方法
    $.getCheckID = function (o) {
        return o.filter(':' + CD).map(function () { return this.value; }).get().join(',');
    };
    //获取多选框选中ID项的TD列信息
    $.getCheckArr = function (o, key) {
        key == UN ? 'value' : key;
        return o.filter(':' + CD).map(function () { return this[key]; }).get();
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
        var b = $(BY),
                  w = $(WD),
                  div = $('#' + JLY),
                  d = w[WH](), //窗宽度
                  h = w[HT](), //窗高度
                  t = w[ST](),
                  l = w[SL]();
        if (div[LN] == 0) { b[AP]('<div id="' + JLY + '"></div>'); div = $("#" + JLY); }
        div[BG]().css({ width: d + l, height: h + t, background: o.color, opacity: o[OP], position: 'absolute', top: 0, left: 0 }).show();
        w.scroll(function () { reset() }).resize(function () { reset() });
        function reset() { if (div.is(':' + VB)) div.css({ width: w[WH]() + w[SL](), height: w[HT]() + w[ST]() }); }
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
        if (NA[MA](/safari/i)) o();
    };

    String.prototype.repeat = function (n) {
        //n表示字符串重得的次数
        return new Array(n + 1).join(this);
    };

    //加前导0方法
    $.prefix = function (n, pos) {
        n = '' + n;
        if (n[LN] < pos)
            n = '0'.repeat(pos - n[LN]) + n;
        return n;
    };

    //停止文本默认选中事件方法
    $.stopSelection = function () {
        var d = DO.selection;
        if (d && d.empty) d.empty();  //IE
        else if (WD.getSelection) WD.getSelection().removeAllRanges(); //FF
    };

   
    return $;
});
