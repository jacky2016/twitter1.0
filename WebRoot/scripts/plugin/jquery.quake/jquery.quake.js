/*
* 震动运用插件
* version: 1.0
* author:  SunAo
* time: 2014-09-29
* compatible: IE6、7、8、9、10、firefox、Chrome
*/
(function ($) {
    $.fn.quake = function (o) {
        o = $[EX]({
            play: TE, // 默认启动震动方法
            width: 4, // 宽移动距离
            height: 0, // 高移动距离
            speed: 30, // 移动频率
            minHeight: 0 // 辅助兼容（默认不用）
        }, o);

        var state = 'quakeState',
              setting = 'quakeSetting',
              defined = 'quakeDefined',
              absolute = 'absolute';

        /*
        * 动态计算边界距离方法
        * value: 宽或高值
        */
        function math(value) {
            return Math.floor(Math.random() * value) - value / 2;
        }

        /*
        * 运动中
        * $this: 要运动的jQuery对象（div）
        */
        function quaking($this) {

            // 随机定位position
            var settingData = $this.data(setting),
                  top = math(settingData[HT]) - settingData.minHeight,
                  left = math(settingData[WH]),
                  speed = math(settingData.speed);
            // 无限循环(递归)
            $this.stop()[AM]({
                'top': top,
                'left': left
            }, speed, function () {
                if ($this.data(state)) {
                    quaking($this);
                } else {
                    $this[AM]({ 'top': 0, 'left': 0 }, speed / 2);
                }
            });
        }

        // 创建对象目的用于根据方法名称动态找到方法
        var quakeMapper = {
            /*
            * 包装
            * $this: 要运动的jQuery对象（div）
            */
            wrap: function ($this) {
                // 缓存一下
                $this.data(setting, o);
                $this.data(defined, TE);

                // 创建一个匿名div，作为震动div的包装div
                var wrapper = $('<div/>').css({
                    'width': $this[OW](TE),
                    'height': $this[OH](TE),
                    'z-index': $this.css('zIndex')
                });

                var position = $this[PO]();

                // 布局转换
                if ($this.css(PO) == absolute) {
                    wrapper.css({
                        'position': absolute,
                        'top': position.top,
                        'left': position.left
                    });
                } else {
                    wrapper.css({
                        'float': $this.css('float'),
                        'position': 'relative'
                    });
                }

                // 判断是否给包装器设置margin-left
                if (($this.css('marginLeft') == '0px' ||
                    $this.css('marginLeft') == 'auto') && position.left > 0 && $this.css(PO) != absolute) {
                    wrapper.css({
                        'marginLeft': position.left
                    });
                }

                // 包装震动div
                $this.wrap(wrapper).css({
                    'position': absolute,
                    'top': 0,
                    'left': 0
                });
            },
            /*
            * 启动运动
            * $this: 要运动的jQuery对象（div）
            */
            run: function ($this) {
                if (!$this.data(state)) {
                    $this.data(state, TE);
                }
                quaking($this);
            },
            /*
            * 停止运动
            * $this: 要运动的jQuery对象（div）
            */
            stop: function ($this) {
                $this.data(state, FE);
            },
            /*
            * 更新缓存
            * $this: 要运动的jQuery对象（div）
            */
            update: function ($this, o) {
                $this.data(setting, o);
            }
        };

        // init
        return this[EH](function () {
            var $this = $(this),
                  method = o.play == TE ? 'run' : 'stop';

            if (quakeMapper[method]) {
                if ($this.data(defined)) {
                    quakeMapper.update($this, o);
                } else {
                    quakeMapper.wrap($this);
                }
                quakeMapper[method]($this, o);
            }
        });
    }
})(jQuery);