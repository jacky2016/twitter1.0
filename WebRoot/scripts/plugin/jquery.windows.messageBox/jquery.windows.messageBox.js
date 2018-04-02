(function ($) {
    $.messageBox = function (o) {
        o = $[EX]({
            content: '',
            type: 0, //0:成功,1:提醒,2:错误
            onAffirm: function (state) { } //确认事件
        }, o);

        var messagebox = '<div class="messagebox"><div class="count"><div class="pic pic{1}"></div><div class="font">{0}</div></div><div class="jquery_clear"></div><div class="button"><div class="cancel">取&nbsp;&nbsp;消</div><div class="ok">确&nbsp;&nbsp;定</div></div><div class="jquery_clear"></div></div>',
              end;
        (function OpenWindows(html, onAffirm) {
            function close(div) { div.hide(); $[RM]({ obj: '#' + JLY }); }
            $(BY)[WW]({
                css: { "width": "303px", "height": "auto" },
                event: 'no',
                title: 'sys.xunku.org',
                content: html,
                id: '_mbox',
                onLoad: function (div) {
                    $('.cancel', div)[EV](CK, function () {
                        close(div);
                        onAffirm(FE);
                    });
                    $('.ok', div)[EV](CK, function () {
                        close(div);
                        onAffirm(TE);
                    });
                }
            });
        })($[FO](messagebox, o.content, o.type), o.onAffirm);
    }
})(jQuery);