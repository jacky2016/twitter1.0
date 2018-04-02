/*
* 布局运动插件
* version: 1.0
* author:  SunAo
* time: 2014-10-03
* compatible: IE6、7、8、9、10、firefox、Chrome
* 目前支持：发牌运动布局、果冻布局
*/
(function (window, $) {

    // 发牌运动类
    function LayoutMovement($elements, o) {
        this.o = o;
        this.queue = $elements.get(); // 队列
    }

    // 创建牌盒
    LayoutMovement[OO].createBox = function () {
        var o = this.o,
              box = $($[FO](o.box, o.name));
        box.css({
            left: o.left,
            top: o.top
        });
        $(BY)[AP](box);
        return box;
    }

    $.fn.layoutMovement = function (o) {
        o = $[EX]({
            name: 'Layout', // 牌盒名称
            box: '<div class="cardCase">{0}</div>', // 盒子
            left: 0, // 牌盒left
            top: 0 // 牌盒top
        }, o);

        var This = this,
              layout = new LayoutMovement(This, o),
              cardCase,
              queue, // 队列
              speed = 300; // 默认发牌速度

        cardCase = layout.createBox();
        queue = layout.queue;

        // 布局转换
        function layoutTransformation($element) {
            $element[EH](function (i) {
                var t = this,
                      $t = $(t),
                      position = $t[OF]();
                t.left = position.left;
                t.top = position.top;
                t.width = $t[OW](TE);
                t.height = $t[OH](TE);
                $t.css({
                    position: 'absolute',
                    left: o.left,
                    top: o.top,
                    width: cardCase[WH](),
                    height: cardCase[HT]()
                });
            });
        }

        // 发牌运动
        function layoutDealMovement(speed) {
            if (queue[LN] == 0) {
                cardCase[RM]();
                return;
            }
            if (speed >= speed / 2) speed -= 20;
            var item = queue.shift();
            $(item)[AM]({ left: item.left, top: item.top }, speed, function () {
                var t = this;
                $(t)[WH](t[WH])[HT](t[HT]);
                $(item)[RA]('style');
                layoutDealMovement(speed);
            });
        }

        layoutTransformation(This);
        layoutDealMovement(speed);

        return This;
    }
    window.LM = 'layoutMovement'; // 发牌运动方法简写
})(window, jQuery);