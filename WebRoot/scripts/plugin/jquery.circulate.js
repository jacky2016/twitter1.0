(function ($) {
    $.circulate = function (el, o) {
        var t = this,
              orw,
              orh,
              nww,
              orl,
              ort;

        t.$el = $(el);
        t.$el.data("circulate", t);

        t.stopAnimation = function () {
            t.o.isStart = FE;
        }

        t.runAnimation = function () {
            var z = 'z-index';
            if (t.o.isStart) {

                orw = t.$el[WH]();
                orh = t.$el[HT]();

                orl = t.$el[PO]().left;
                ort = t.$el[PO]().top;

                if (t.o.sizeAdjustment == 100) {
                    nww = orw;
                    nwh = orh;
                    hww = orw;
                    hwh = orh;
                } else {
                    nww = parseInt(orw) * (t.o.sizeAdjustment / 100);
                    nwh = parseInt(orh) * (t.o.sizeAdjustment / 100);
                    hww = (parseInt(orw) + nww) / 2;
                    hwh = (parseInt(orh) + nwh) / 2;
                };

                if (t.$el.css(PO) != "absolute") {
                    t.$el.css(PO, "relative");
                }
                t.$el.css("z", t.o.zIndex[0]);

                // Would be nice to only start animations if currently unanimated. Like this:
                // t.$el.filter(':not(:animated)')[AM]({
                // But this is screwing up loops (returns empty set on second go-around)

                t.$el[AM]({
                    top: ["+=" + (t.o[HT] / 2) + "px", 'easeInQuad'],
                    left: ["+=" + (t.o[WH] / 2) + "px", 'easeOutQuad'],
                    width: [hww, 'linear'],
                    height: [hwh, 'linear'],
                    opacity: 1
                }, t.o.speed, function () { t.$el.css("z", t.o.zIndex[1]); })
                [AM]({
                    top: ["+=" + (t.o[HT] / 2) + "px", 'easeOutQuad'],
                    left: ["-=" + (t.o[WH] / 2) + "px", 'easeInQuad'],
                    width: [nww, 'linear'],
                    height: [nwh, 'linear']
                }, t.o.speed, function () { t.$el.css("z", t.o.zIndex[2]); })
                [AM]({
                    top: ["-=" + (t.o[HT] / 2) + "px", 'easeInQuad'],
                    left: ["-=" + (t.o[WH] / 2) + "px", 'easeOutQuad'],
                    width: [hww, 'linear'],
                    height: [hwh, 'linear']
                }, t.o.speed, function () { t.$el.css("z", t.o.zIndex[3]); })
                [AM]({
                    top: ["-=" + (t.o[HT] / 2) + "px", 'easeOutQuad'],
                    left: ["+=" + (t.o[WH] / 2) + "px", 'easeInQuad'],
                    width: [orw, 'linear'],
                    height: [orh, 'linear']
                }, t.o.speed, function () {

                    t.$el.css("z", t.o.zIndex[0]);

                    if (t.o.loop === TE) t.runAnimation();

                });

            }

        };

        t.init = function () {
            t.o = $.extend({
                speed: 400, //速度
                height: 200, //移动高范围
                width: 200, //移动宽范围
                sizeAdjustment: 100,  // 调整大小
                loop: FE,          // 是否反复循环
                zIndex: [1, 1, 1, 1],
                isStart: TE       // 是否开始动画
            }, o);
            t.runAnimation();
        };

        t.init();

    };

    $.fn.circulate = function (o) {
        if (typeof (o) === "string") {
            return this[EH](function () {
                var s = $(this).data('circulate');
                if (s) { s.stopAnimation(); }
            });
        } else {
            return this[EH](function () {
                (new $.circulate(this, o));
            });
        }
    };

})(jQuery);