//define(['jquery'], function ($) {
(function($) {
	var EX = 'extend';
    $.fn[EX]({
        'pngFix': function (o) {
            o = $[EX]({
                blankgif: 'blank.gif'
            }, o);

            var This = this,
              span = '<span {0} style="position:relative;white-space:pre-line;display:inline-block;background:transparent;{1}width:{2}px; height:{3}px;{4}"></span>',
              filter = 'progid:DXImageTransform.Microsoft.AlphaImageLoader(src=\'{0}\', sizingMethod=\'scale\');';
            $.IE6(function () {
                This[FD]("img[src$='.png']")[EH](function () {
                    This[AT](WH, This[WH]())[AT](HT, This[HT]());
                    var ps = '',
                          html = '',
                          id = This[AT]('id'),
                          c = This[AT]('class'),
                          title = This[AT]('title'),
                          alt = This[AT]('alt'),
                          align = This[AT]('align'),
                          imgId = (id) ? 'id="' + id + '" ' : '',
                          imgClass = (c) ? 'class="' + c + '" ' : '',
                          imgTitle = (title) ? 'title="' + title + '" ' : '',
                          imgAlt = (alt) ? 'alt="' + alt + '" ' : '',
                          imgAlign = (align) ? 'float:' + align + ';' : '',
                          imgHand = (This[PT]()[AT]('href')) ? 'cursor:hand;' : '',
                          s = this.style,
                          b = s.border,
                          p = s.padding,
                          m = s.margin;
                    if (b) {
                        ps += 'border:' + b + ';';
                        b = '';
                    }
                    if (p) {
                        ps += 'padding:' + p + ';';
                        p = '';
                    }
                    if (m) {
                        ps += 'margin:' + m + ';';
                        m = '';
                    }
                    var is = (s.cssText);
                    html += $[FO](span, imgId + imgClass + imgTitle + imgAlt, imgAlign + imgHand, This[WH](), This[HT](), $[FO](filter, This[AT]('src')) + is);
                    if (ps != '') html = $[FO]('<span style="position:relative;display:inline-block;{0}width:{1}px;height:{2}px;">{3}</span>', ps + imgHand, This[WH](), This[HT](), html);
                    This.hide();
                    This.after(html);
                });

                // fix css background pngs
                This[FD]("*")[EH](function () {
                    var t = $(this),
                      img = t.css('background-image');
                    if (img.indexOf(".png") != -1) {
                        var iebg = img.split('url("')[1].split('")')[0];
                        t.css('background-image', 'none').get(0).runtimeStyle.filter = $[FO](filter, iebg);
                    }
                });

                //fix input with png-source
                This[FD]("input[src$='.png']")[EH](function () {
                    var t = $(this),
                          img = t[AT]('src');
                    t.get(0).runtimeStyle.filter = $[FO](filter, img);
                    t[AT]('src', o.blankgif);
                });
            })
            return This;
        }
    });
})(jQuery);
    //return $;
//});
