
(function($) {
    $.fn.bgiframe = function (o) {
        o = $[EX]({
            top: 'auto', // auto == .currentStyle.borderTopWidth
            left: 'auto', // auto == .currentStyle.borderLeftWidth
            width: 'auto', // auto == offsetWidth
            height: 'auto', // auto == offsetHeight
            opacity: TE,
            src: 'javascript:false;'
        }, o);

        if (NA[MA](/msie [6]/i)) {
            var p = function (n) { return n && n.constructor == Number ? n + 'px' : n; },
                  e1 = 'expression(((parseInt(this.parentNode.currentStyle.border{0}Width)||0)*-1)+\'px\')',
                  e2 = 'expression(this.parentNode.offset{0}+\'px\')',
                  a = 'auto',
                  w = o[WH],
                  h = o[HT],
                  t = o.top,
                  l = o.left,
                  html = '<iframe class="bgiframe"frameborder="0"tabindex="-1"src="' + o.src + '"' +
		                    'style="display:block;position:absolute;z-index:-1;' + (o[OP] !== FE ? 'filter:Alpha(Opacity=\'0\');' : '') +
					        'top:' + (t == a ? $[FO](e1, 'Top') : p(t)) + ';' +
					        'left:' + (l == a ? $[FO](e1, 'Left') : p(l)) + ';' +
					        'width:' + (w == a ? $[FO](e2, 'Width') : p(w)) + ';' +
					        'height:' + (h == a ? $[FO](e2, 'Height') : p(h)) + ';"/>';

            return this[EH](function () {
                var t = this;
                if ($('> iframe.bgiframe', t)[LN] == 0) {
                    t.insertBefore(DO[CEL](html), t.firstChild);
                }
            });
        }
        return this;
    };
})(jQuery);
    //return $;
//});