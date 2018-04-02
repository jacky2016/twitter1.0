//define(['./jquery'], function ($) {
(function($) {
    $.fn.keyupTo = function (o) {
        o = $[EX]({
            param: {}, //参数
            isCache: TE,
            cacheName: '', //缓存名称
            id: 'm',
            className: 'a', //控件样式名称
            keyword:'', //保存关键字的属性name
            onLoad: function (div, input) { }, //加载事件
            onComplete: function (div, input, data) { } //加载完成事件
        }, o);
        var b = $(BY),
              setTime,
              keyupTo = {
                  //异步方法
                  //参数：This:文本框对象,div:显示框对象, o:参数
                  Ajax: function (This, div, o) {
                      var text = $[TM](This.val());
                      o.param[o.keyword] = text;
                      This[AJ]({
                          cacheName: o.cacheName + text,
                          param: o.param,
                          isCache: o.isCache,
                          success: function (data) {
                              if (data) {
                                  o.onComplete(div, This, data);
                              }
                          }
                      });
                  }
              };
        //执行函数
        this[EH](function (i) {
            var This = $(this),
                   id = o.id + i,
                   div = $("#" + id);
            if (div[LN] == 0) { b[AP]($[FO]('<div id="{0}" class="{1}"></div>', id, o.className)); div = $("#" + id); }
            o.onLoad(div, This);

            //键盘按下事件
            This.txt()[UB](KU)[BD](KU, function (e) {
            //alert('aa');
                if (setTime != null) clearTimeout(setTime);
                setTime = setTimeout(function () {
                    var k = e.which,
                          text = $[TM](This.val()),
                          json = { div: div, input: This };
                    if (text == '' || text == T1) { div.hide(); return FE; }
                    if (k == 9 || k == 13 || k == 16 || k == 37 || k == 38 || k == 39 || k == 40) return FE;
                    $.show(json);
                    $[WR](json);
                    var d = div[FD]('.left');
                    d[LN] == 0 ? div[LG]() : d[LG](); //请稍等
                    //异步获取信息
                    keyupTo.Ajax(This, div, o);
                }, 300);
            })[EV]('paste', function () {
                This[KU]();
            })[EV]('cut', function () {
                This[KU]();
            })[EV](CK, function () {
                if (div.is(':' + HD)) This[KU]();
            });
        });
        return this;
    }
})(jQuery);
    //return $;
//});