//define(['jquery'], function ($) {
(function($) {
    $.fn.patch = function (o) {
        o = $[EX]({
            width: 200, //控件宽度
            param: {},
            isCache: TE,
            cacheName: '', //缓存名称
            keyword: 'keyword', //保存关键字的属性name
            templates: [{ html: '<li key="{0}"' },
                              { html: ' text="{0}" >' },
                              { html: '<span class="name">{0}</span></li>'}], //html模板
            columns: null, //数据列的key值
            rows: '', //数据集合key值
            onComplete: function (This, div, data) {
                var li = div[FD]('li');
                li[OT]({ 'over': JQH })[EV](CK, function () {
                    var $t = $(this);
                    This.val($t[AT]('text'))[AT]('key', $t[AT]('key'));
                    div.hide();
                });
            }, //加载完成事件,This:input对象,div:div对象,data:数据
            id: 'm'//控件ID
        }, o);
        var t = this;
        //alert("patch-----"+t.attr('id'));
        t[KT]({
            param: o.param,
            keyword: o.keyword, //保存关键字的属性name
            isCache: o.isCache,
            cacheName: o.cacheName,
            id: o.id,
            className: 'patch',
            onLoad: function (div, This) {
                div[WH](o[WH]);
            },
            onComplete: function (div, This, data) {
                //此控件业务
                div.html('<ul></ul>');
                var ul = div[FD]('ul');
                ul[LS]({
                    data: data,
                    isPager: FE,
                    templates: o.templates,
                    columns: o.columns,
                    rows: o.rows,
                    onComplete: function (This, refresh, data) {
                        if (data[LN] == 0) div[FD]('ul').html('<li>暂无</li>');
                        o.onComplete(t, div, data);
                    }
                });
            }
        });
        return t;
    }
})(jQuery);
    //return $;
//});