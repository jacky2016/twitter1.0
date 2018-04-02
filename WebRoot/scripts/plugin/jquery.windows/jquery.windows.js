/*
* 弹出窗口插件
* version: 1.0
* author:  Sunao
* time: 2010-03-06
* 说明：
* @param ep标示窗口最终位置，包含两个属性，一个是Left（可以是字符串，也可以是数值），另一个是Top（可以是字符串，也可以是数值）
* @param hideF表示执行窗口隐藏特效的方法
* @param sp标示窗口初始位置，包含两个属性，一个是Left（可以是字符串，也可以是数值），另一个是Top（可以是字符串，也可以是数值）
* @param isMove是否可以移动
* 目前支持，左上，中上，右上，右中，右下，下中，左下，左中，中心，（自定义坐标）弹出菜单，根据元素坐标（Tip提示框）等功能
*/
(function($) {
    $.fn.MyWin = function (ep, hideF, sp, showF, isMove) {
        if (ep && ep instanceof Object) {
            var EL = ep.Left, //获得传来参数的左值
                  ET = ep.Top, //获得传来参数的上值
                  w = $(WD),
                  This = this, //定义this变量，优化代码
                  TW, //获得自定义窗口的宽度
                  TH, //获得自定义窗口的高度
                  CL, //定义CL变量，优化代码
                  CT, //定义CT变量，优化代码
                  WW, //获得页面窗口的宽度
                  WH, //获得页面窗口的高度
                  SL, //获得滚动条左值
                  ST; //获得滚动条上值

            function getWin() {
                WW = w.width(); //获得页面窗口的宽度
                WH = w[HT](); //获得页面窗口的高度
                SL = w.scrollLeft(); //获得滚动条左值
                ST = w.scrollTop(); //获得滚动条上值
            }

            //定义左右移动方法，以便从新计算移动后的左端值，随着滚动条左右移动而移动
            function calLeft(EL, SL, WW, TW) {
                //如果EL值不为空 并且 EL值为字符串的时候
                if (EL && typeof EL == "string") {
                    if (EL == "center") {//如果EL值为center
                        CL = SL + (WW - TW) / 2; //计算得到页面中间位置的宽度,+SL跟随滚动条值左右移动
                    } else if (EL == "left") {//如果EL值为left
                        CL = SL + 0; //计算得到左端位置的宽度+SL跟随滚动条值左右移动
                    } else if (EL == "right") {//如果EL值为right
                        CL = SL + WW - TW; //计算得到右端位置的高度+SL跟随滚动条值左右移动
                        //浏览器兼容性代码，$.browser.safari表示找到safari浏览器，在safari浏览器窗口右-15，以免窗口超出页面
                        //if (b.safari) CL = CL - 15;
                        //浏览器兼容性代码，$.browser.opera表示找到opera浏览器，在opera浏览器窗口右+15，以窗口与页面右边对齐
                        //if (b.opera) CL = CL + 15;
                        //浏览器兼容性代码，$.browser.msie表示找到ie浏览器 并且 browser.version.indeOf("8")>=0表示找到ie版本号，在ie8浏览器窗口右-20，以免窗口超出页面
                        $.IE8(function () { CL = CL - 0; }); 
                    } else {//默认值为中间显示
                        CL = SL + (WW - TW) / 2;
                    }
                    //如果EL值不为空 并且 EL值为数字的时候
                } else if (EL && typeof EL == "number") {
                    CL = EL; //左方位为传来EL参数的值
                } else {//如果没输入值，默认为0
                    CL = 0;
                }
            }

            //定义上下移动方法，以便从新计算移动后的左端值，随着滚动条上下移动而移动
            function calTop(ET, ST, WH, TH) {
                //如果ET值不为空 并且 ET值为字符串的时候
                if (ET && typeof ET == "string") {
                    if (ET == "center") {//如果ET值为center
                        CT = ST + (WH - TH) / 2; //计算得到页面中间位置的高度+ST跟随滚动条值上下移动
                    } else if (ET == "top") {//如果ET值为left
                        CT = ST + 0; //计算得到顶端位置的宽度+ST跟随滚动条值上下移动
                    } else if (ET == "bottom") {//如果ET值为right
                        CT = ST + WH - TH; //计算得到低端位置的高度+ST跟随滚动条值上下移动
                        //if (b.opera) CT = CT - 25;
                    } else {//默认值为中间显示
                        CT = ST + (WH - TH) / 2;
                    }
                    //如果ET值不为空 并且 ET值为数字的时候
                } else if (ET && typeof ET == "number") {
                    CT = ET; //顶位为传来ET参数的值
                } else {//如果没输入值，默认为0
                    CT = 0;
                }
            }

            function moveWin() {
                calLeft(This.data("EL"), SL, WW, TW);
                calTop(This.data("ET"), ST, WH, TH);
                This[AM]({ "left": CL, "top": CT }, 200); //1000
            }

            //点击按钮事件隐藏div窗口,children找到this中的子节点元素
            This.children(".title").children("em")[UB](CK)[BD](CK, function () {
                if (!hideF) This.hide();
                else hideF();
            });

            //窗口初始化位置事件
            if (sp && sp instanceof Object) {
                var SL = sp.Left; //获得传来参数的左值
                var ST = sp.Top; //获得传来参数的上值
                //如果SL值不为空 并且 SL值为数值的时候
                SL && typeof SL == "number"
                    ? This.css("left", SL) //窗体左值为传来的值
                    : This.css("left", 0); //窗体左值为0

                //如果ST值不为空 并且 ST值为数值的时候
                ST && typeof ST == "number"
                    ? This.css("top", ST)
                    : This.css("top", 0);

                showF();
            }

            TW = This[OW](TE); //获得自定义窗口的宽度，outerWidth(true)用法：包含border宽、 padding宽、margin宽的总和，如果不写true则不带margin的宽
            TH = This[OH](TE); //获得自定义窗口的高度，outerHeight(true)用法：包含border高、 padding高、margin高的总和，如果不写true则不带margin的高
            This.data("ET", ET); //保存顶端值
            This.data("EL", EL); //保存左端值
            getWin();
            moveWin();

            var Time; //定义延时变量，防止窗口移动时出现闪烁效果
            //页面滚动条滚动事件
            w.scroll(function () {
                //判断当前窗口是否可见，如果不可见就不必执行找窗口位置的代码
                if (!This.is(':' + VB)) return;
                clearTimeout(Time); //执行时先清空Time值
                Time = setTimeout(function () { getWin(); if (isMove) moveWin(); }, 100); //300
                //页面改变大小事件
            }).resize(function () {
                //判断当前窗口是否可见，如果不可见就不必执行找窗口位置的代码
                if (!This.is(':' + VB)) return;
                getWin();
                if (isMove) moveWin();
            });
            return This; //返回当前对象，以便可以级联的执行其他方法
        }
    };
    $.window = function (o) {
        o = $[EX]({
            obj: null,
            isMove: TE,
            position: "Center",
            mode: "default",
            onClose: function () { }
        }, o);
        var This = o.obj;
        setTimeout(function () {
            var sp = { left: "", top: "" },
                  ep = { left: "", top: "" },
                  w = $(WD),
                  TW = This[OW](TE),
                  TH = This[OH](TE),
                  WW = w.width(),
                  WH = w[HT](),
                  SL = w.scrollLeft(),
                  ST = w.scrollTop(),
                  m = o.isMove;

            switch (o.position) {
                case "Center":
                    sp.left = SL + (WW - TW) / 2;
                    sp.top = ST + WH;
                    ep.left = "center";
                    ep.top = "center";
                    break;
                case "LeftUp":
                    sp.left = SL;
                    sp.top = ST - TH;
                    ep.left = "left";
                    ep.top = "top";
                    break;
                case "CenterUp":
                    sp.left = SL + (WW - TW) / 2;
                    sp.top = ST - TH;
                    ep.left = "center";
                    ep.top = "top";
                    break;
                case "RightUp":
                    sp.left = SL + WW - TW;
                    sp.top = ST - TH;
                    ep.left = "right";
                    ep.top = "top";
                    break;
                case "RightCenter":
                    sp.left = SL + WW - TW;
                    sp.top = ST + WH;
                    ep.left = "right";
                    ep.top = "center";
                    break;
                case "RightDown":
                    sp.left = SL + WW - TW;
                    sp.top = ST + WH;
                    ep.left = "right";
                    ep.top = "bottom";
                    break;
                case "CenterDown":
                    sp.left = SL + (WW - TW) / 2;
                    sp.top = ST + WH;
                    ep.left = "center";
                    ep.top = "bottom";
                    break;
                case "LeftDown":
                    sp.left = SL;
                    sp.top = ST + WH;
                    ep.left = "left";
                    ep.top = "bottom";
                    break;
                case "LeftCenter":
                    sp.left = SL - TW;
                    sp.top = ST + (WH - TH) / 2;
                    ep.left = "left";
                    ep.top = "center";
                    break;
                default:
                    sp = ep = o.position;
                    break;
            }
            var s = { Left: sp.left, Top: sp.top }, e = { Left: ep.left, Top: ep.top };
            switch (o.mode) {
                case "default":
                    This.MyWin(e, function () { This.hide(); hideLayer(); }, s, function () { This.show() }, m);
                    break;
                case "slideUp":
                    This.MyWin(e, function () { This.slideUp("slow", function () { hideLayer(); }); }, s, function () { This.show() }, m);
                    break;
                case "fadeIn":
                    sp.top = ST + (WH - TH) / 2;
                    This.MyWin(e, function () { This.fadeOut(1000, function () { hideLayer(); }).dequeue(); }, { Left: sp.left, Top: sp.top }, function () { This.fadeIn(1000).dequeue() }, m);
                    break;
                case "event":
                    if (o.position == "Center") sp.top = ST + (WH - TH) / 2;
                    This.MyWin(e, function () { This.hide(); hideLayer(); }, { Left: sp.left, Top: sp.top }, function () { This.show() }, m);
                    break;
            }
            function hideLayer() { $[RM]({ obj: "#" + JLY }); o.onClose(); }
        }, 100);
    };
    $.fn.window = function (o) {
        o = $[EX]({
            position: 'Center',
            mode: "event",
            event: CK,
            css: { "width": "200px", "height": "auto" },
            title: "无",
            id: 'a',
            content: '',
            layer: TE,
            isMove: TE,
            isTitle: TE, //是否带标题项
            onClose: function () { },
            onLoad: function (div) { }, //加载成功事件
            onClick: function (id, clickThis) { } //加载点击事件
        }, o);
        var html = '<div class="my_win" id="{1}"><h1 class="titleLeft"></h1><h1 class="title"><em class="close"></em>{0}</h1><div class="content"></div></div>',
              b = $("body"),
              div = $("#" + o.id),
              m = o.isMove;
        if (div[LN] == 0) { b[AP]($[FO](html, o.title, o.id)); div = $("#" + o.id); }
        if(o.content != '') $('.content', div).html(o.content);
        if (!o.isTitle) {
            $('.titleLeft', div).hide();
            $('.title', div).hide();
        }
        div.css(o.css);
        var This = this,
              sp = { left: "", top: "" },
              ep = { left: "", top: "" },
              w = $(WD),
              DW = div[OW](TE),
              DH = div[OH](TE),
              WW = w.width(),
              WH = w[HT](),
              e = o.event,
              clickThis = null;

        o.onLoad(div);
        switch (e) {
            case CK:
                This[UB](e)[BD](e, function () { clickThis = $(this); event(); });
                break;
            case MO:
                This[e](function () { event(); }); //.mouseout(function () { div.hide(); clearTimeout(time); })
                break;
            case 'hover':
                var time;
                This[e](function () { time = setTimeout(function () { event(); }, 200); }, function () { div.hide(); clearTimeout(time); });
                break;
            default:
                event();
                break;
        }

        function event() {
            var f = This[OF]();
            if (o.layer) $.layer();
            o.position != null
                ? $.window({ obj: div, position: o.position, mode: o.mode, isMove: m, onClose: o.onClose })
                : f.top - w.scrollTop() < DH
                    ? div.MyWin({ Left: f.left, Top: f.top + DH }, function () { div.hide(); }, { Left: f.left, Top: f.top + DH }, function () { div.show(); }, m)
                    : div.MyWin({ Left: f.left, Top: f.top - DH }, function () { div.hide(); }, { Left: f.left, Top: f.top - DH }, function () { div.show(); }, m);
            setTimeout(function () { o.onClick(div, clickThis); }, 300);
        }
        return this;
    }
})(jQuery);