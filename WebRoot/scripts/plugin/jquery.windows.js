/*
* 弹出窗口插件
* version: 2.0
* author:  Sunao
* time: 2010-03-06 - 2014-06-25
* compatible: IE6、7、8、9、10、firefox、Chrome
* 说明：重新简化代码，去掉多余的功能，更轻量，提供代码可读性
* 目前支持：左上，中上，右上，右中，右下，下中，左下，左中，中心，（自定义坐标）弹出菜单，
* 根据元素坐标（Tip提示框）、等功能（附加功能拖拽、自由碰撞+重力）
*/
(function ($) {
    $.window = function (o) {
        o = $[EX]({
            position: 'center', //可以传方位Json字符串类型，也可以传方位Json数字类型（自定义方位，弹出菜单），也可以传字符串类型（center，leftUp）
            mode: 'event',
            css: { width: '200px', height: 'auto' },
            title: '',
            id: 'win1', //弹出层id
            content: '', //弹出层内容
            isLayer: TE, //是否添加遮罩
            isTitle: TE, //是否带标题项
            isMove: TE, //是否移动
            onClose: $.noop, //关闭窗体事件，div：当前窗体元素对象
            onLoad: $.noop //窗体加载成功事件，div：当前窗体元素对象
        }, o);

        //计算窗体坐标
        var window = hasWindow(o.id, o.title, o.content, o.isTitle, o.onLoad), //获取弹出层对象
              $window = $(WD),
              $windowWidth, //当前窗体宽度
              $windowHeight, //当前窗体高度
              $windowScrollTop, //当前窗体滚动条顶部
              $windowScrollLeft, //当前窗体滚动条左部
              windowWidth = window[OW](TE), //弹出层宽度，outerWidth(true)用法：包含border宽、 padding宽、margin宽的总和，如果不写true则不带margin的宽
              windowHeight = window[OH](TE), //弹出层高度，outerHeight(true)用法：包含border高、 padding高、margin高的总和，如果不写true则不带margin的高
              finalPosition = {}, //弹出框最终位置
              initPosition = {}, //弹出框初始位置
              windowLeft = 0, //弹出框left值
              windowTop = 0, //弹出框top值
              isMove = o.isMove, //是否移动变量
              end;

        //计算弹出层的移动坐标和初始坐标
        getWindow();
        setPosition(o.position);

        var positionLeft = finalPosition.left, //方位对象left
               positionTop = finalPosition.top; //方位对象top

        /*
        * 找到弹出层方法（有，直接返回窗体对象，没有，在dom里创建新窗体对象后返回）
        * id: 弹出层唯一标示
        * title: 弹出层标题
        * content: 弹出层内容
        * isTitle: 是否有标题
        * fn: 加载完成事件
        * return: 弹出层对象
        */
        function hasWindow(id, title, content, isTitle, fn) {
            var windowTemplate = '<div class="window" id="{2}"><h1 class="titleLeft"></h1><h1 class="title"><em class="close"></em>{0}</h1><div class="content">{1}</div></div>',
                  window = $('#' + o.id); //弹出层对象
            if (window[LN] == 0) {
                $(BY)[AP]($[FO](windowTemplate, title, content, id));
                window = $('#' + o.id);
            }

            //判断是否添加标题条
            if (!isTitle) {
                $('.titleLeft', window).add($('.title', window)).hide();
            }
            window.css(o.css);
            //加载完成事件
            fn(window, closeWindow);
            return window;
        }

        //获取Window窗体的4项值
        function getWindow() {
            $windowWidth = $window[WH](); //当前窗体宽度
            $windowHeight = $window[HT](); //当前窗体高度
            $windowScrollTop = $window[ST](); //当前窗体滚动条顶部
            $windowScrollLeft = $window[SL](); //当前窗体滚动条左部
        }

        //移动弹出层
        function moveWindow() {
            getWindowLeft(window.data('left'));
            getWindowTop(window.data('top'));
            window[AM]({ 'left': windowLeft, 'top': windowTop }, 200); //1000
        }

        //获取弹出层Left值方法
        function getWindowLeft(positionLeft) {
            //如果EL值不为空 并且 EL值为字符串的时候
            if (positionLeft && typeof positionLeft == 'string') {
                if (positionLeft == 'center') {//如果EL值为center
                
                    windowLeft = $windowScrollLeft + ($windowWidth - windowWidth) / 2; //计算得到页面中间位置的宽度,+SL跟随滚动条值左右移动
               
                } else if (positionLeft == 'left') {//如果EL值为left
                    windowLeft = $windowScrollLeft; //计算得到左端位置的宽度+SL跟随滚动条值左右移动
                } else if (positionLeft == 'right') {//如果EL值为right
                    windowLeft = $windowScrollLeft + $windowWidth - windowWidth; //计算得到右端位置的高度+SL跟随滚动条值左右移动
                } else {//默认值为中间显示
                
                    windowLeft = $windowScrollLeft + ($windowWidth - windowWidth) / 2;
                }
                //如果EL值不为空 并且 EL值为数字的时候
            } else if (positionLeft && typeof positionLeft == 'number') {
                windowLeft = positionLeft; //左方位为传来EL参数的值
            } else {//如果没输入值，默认为0

                windowLeft = 0;
            }
        }

        //获取弹出层Top值方法
        function getWindowTop(positionTop) {
            //如果ET值不为空 并且 ET值为字符串的时候
            if (positionTop && typeof positionTop == 'string') {
                if (positionTop == 'center') {//如果ET值为center
                    windowTop = $windowScrollTop + ($windowHeight - windowHeight) / 2; //计算得到页面中间位置的高度+ST跟随滚动条值上下移动
                } else if (positionTop == 'top') {//如果ET值为left
                    windowTop = $windowScrollTop; //计算得到顶端位置的宽度+ST跟随滚动条值上下移动
                } else if (positionTop == 'bottom') {//如果ET值为right
                    windowTop = $windowScrollTop + $windowHeight - windowHeight; //计算得到低端位置的高度+ST跟随滚动条值上下移动
                } else {//默认值为中间显示
                    windowTop = $windowScrollTop + ($windowHeight - windowHeight) / 2;
                }
                //如果ET值不为空 并且 ET值为数字的时候
            } else if (positionTop && typeof positionTop == 'number') {
                windowTop = positionTop; //顶位为传来ET参数的值
            } else {//如果没输入值，默认为0
                windowTop = 0;
            }
        }

        /*
        * 设置弹出层初始值
        * position: 方位字符串
        */
        function setPosition(position) {
            switch (position) {
                case 'center':
                    initPosition.left = $windowScrollLeft + ($windowWidth - windowWidth) / 2;
                    initPosition.top = $windowScrollTop + ($windowHeight - windowHeight) / 2;
                    finalPosition.left = 'center';
                    finalPosition.top = 'center';
                    break;
                case 'leftUp':
                    initPosition.left = $windowScrollLeft;
                    initPosition.top = $windowScrollTop - windowHeight;
                    finalPosition.left = 'left';
                    finalPosition.top = 'top';
                    break;
                case 'centerUp':
                    initPosition.left = $windowScrollLeft + ($windowWidth - windowWidth) / 2;
                    initPosition.top = $windowScrollTop - windowHeight;
                    finalPosition.left = 'center';
                    finalPosition.top = 'top';
                    break;
                case 'rightUp':
                    initPosition.left = $windowScrollLeft + $windowWidth - windowWidth;
                    initPosition.top = $windowScrollTop - windowHeight;
                    finalPosition.left = 'right';
                    finalPosition.top = 'top';
                    break;
                case 'rightCenter':
                    initPosition.left = $windowScrollLeft + $windowWidth;
                    initPosition.top = $windowScrollTop + ($windowHeight - windowHeight) / 2;
                    finalPosition.left = 'right';
                    finalPosition.top = 'center';
                    break;
                case 'rightDown':
                    initPosition.left = $windowScrollLeft + $windowWidth - windowWidth;
                    initPosition.top = $windowScrollTop + $windowHeight;
                    finalPosition.left = 'right';
                    finalPosition.top = 'bottom';
                    break;
                case 'centerDown':
                    initPosition.left = $windowScrollLeft + ($windowWidth - windowWidth) / 2;
                    initPosition.top = $windowScrollTop + $windowHeight;
                    finalPosition.left = 'center';
                    finalPosition.top = 'bottom';
                    break;
                case 'leftDown':
                    initPosition.left = $windowScrollLeft;
                    initPosition.top = $windowScrollTop + $windowHeight;
                    finalPosition.left = 'left';
                    finalPosition.top = 'bottom';
                    break;
                case 'leftCenter':
                    initPosition.left = $windowScrollLeft - windowWidth;
                    initPosition.top = $windowScrollTop + ($windowHeight - windowHeight) / 2;
                    finalPosition.left = 'left';
                    finalPosition.top = 'center';
                    break;
                default:
                    initPosition = finalPosition = position;
                    break;
            }
            window.css({
                left: initPosition.left,
                top: initPosition.top
            }).show(); //提前显示出弹出层
        }

        //关闭窗体方法
        function closeWindow() {
            window[RM](); //移除弹出层
            $('#' + JLY)[RM](); //移除遮罩层
            $window[UB]('scroll.window')[UB]('resize.window'); //解除事件
        }

        //init
        //判断position参数不为空 并且 position是对象
        if (finalPosition && finalPosition instanceof Object) {
            //设置弹出层默认位置
            getWindow();
            window.data('left', positionLeft); //保存顶端值
            window.data('top', positionTop); //保存左端值
            if (isMove)
                moveWindow();

            //是否添加遮罩
            if (o.isLayer) $[LY]();

            //开启碰撞运动功能
            var handle = $('.title', window);
            if ($.fn.dragCollision) {
                //自由碰撞运动方法
                window.dragCollision(handle);
                //开启拖拽功能
            } else if ($.fn.drag) {
                window.drag({
                    handle: handle
                });
            }

            //关闭窗体
            window[FD]('em')[EV](CK, function () {
                closeWindow()
                o.onClose();
            });

            var timer; //定义延时变量，防止窗口移动时出现闪烁效果
            //页面滚动条滚动事件
            $window[BD]('scroll.window', function () {
                CM(timer); //执行时先清空Time值
                timer = SM(function () {
                    getWindow();
                    if (isMove)
                        moveWindow();
                }, 100); //300
                //页面改变大小事件
            })[BD]('resize.window', function () {
                getWindow();
                if (isMove)
                    moveWindow();
            });
        }
    };
})(jQuery);