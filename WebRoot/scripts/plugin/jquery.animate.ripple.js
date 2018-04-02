/*
* 水波运用插件
* version: 1.0
* author:  SunAo
* time: 2014-10-08
* compatible: IE6、7、8、9、10、firefox、Chrome
*/
(function ($) {
    $.ripple = function (o) {
        var ripple = $('.ripple');
        if (ripple[LN] == 0) {
            $(BY)[AP]('<div class="ripple"></div>');
            ripple = $('.ripple');
        }

        function reset(ripple) {
            ripple.removeAttr('style').css({
                width: '20px',
                height: '20px',
                background: '#f7f7f7',
                position: 'absolute',
                zIndex: 99999
            }).corner('50px').hide();
        }
        reset(ripple);

        var $window = $(WD),
              zoomWidth = 50,
              zoomHeight = 50;

        $window[EV](CK + '.ripple', function (e) {
            var pageX = e.pageX,
                  pageY = e.pageY;
            ripple.css({
                left: pageX + 'px',
                top: pageY + 'px'
            });
            if ($window[HT]() + $window[ST]() - pageY < zoomHeight / 2 ||
                    $window[WH]() + $window[SL]() - pageX < zoomWidth / 2) {
                return;
            }
            ripple.show();
            var width = ripple[WH](),
                  height = ripple[HT](),
                  marginLeft = (zoomWidth - width) / 2,
                  marginTop = (zoomHeight - height) / 2;
            ripple[AM]({ width: zoomWidth, height: zoomHeight,
                marginLeft: -marginLeft, marginTop: -marginTop, opacity: 0.1
            }, {
                duration: 200, easing: 'easeOutCirc', complete: function () {
                    reset(ripple);
                }
            });
        });
    }
})(jQuery);