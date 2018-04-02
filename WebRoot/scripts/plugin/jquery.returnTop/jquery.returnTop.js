(function ($) {
    $.returnTop = function (o) {
        o = $.extend({
            opacity: 0.6,
            color: '#000'
        }, o);
        var div = '<div class="backToTop">返回顶部</div>',
               b = $('body'),
               w = $(WD),
               This = $('.backToTop');
        if (This[LN] == 0) b[AP](div);
        var This = $('.backToTop');

        This.css({ OP: o[OP], background: o.color })[CK](function () {
            $('html, body')[AM]({ scrollTop: 0 }, 120);
        });

        w[BD]("scroll", function () {
            var st = $(DO)[ST](),
                  wh = w[HT]();
            if (st > 200) {
                if (!This.is(':' + AD)) This.fadeIn(1000);
            } else {
                if (!This.is(':' + AD)) This.fadeOut(500);
            }
            $.IE6(function () { This.css("top", st + wh - 100) });
        });
    }
})(jQuery);