/*
* 返回顶部插件
* version: 1.0
* author:  Sunao
* time: 2014-06-07
* compatible: IE6、7、8、9、10、firefox、Chrome
*/
(function ($) {
    $.returnTop = function (o) {
        o = $[EX]({
            opacity: 0.6, //透明度
            color: '#000' //颜色
        }, o);
        var div = '<div class="returnTop"><div class="font">返回顶部</div></div>',
              This = $('.returnTop');
        if (This[LN] == 0) $(BY)[AP](div);

        var This = $('.returnTop'),
              font = This[FD]('.font');

        font.css({
            opacity: o[OP],
            background: o.color
        })[EV](MO, function () {
            $(this).css('opacity', 1);
        })[EV](MU, function () {
            $(this).css('opacity', o[OP]);
        });

        This[CK](function () {
            $('html, body')[AM]({ scrollTop: 0 }, 120);
        });

        $(WD)[EV]('scroll.returnTop', function () {
            var scrollTop = $(DO)[ST](),
                  windowHeight = $(WD)[HT]();
                  
            This[scrollTop > 200? 'fadeIn': 'fadeOut'](300);
            
            $.IE6(function () {
                This.css('top', scrollTop + windowHeight - This[HT]());
            });
        });
    }
})(jQuery);