define(['./jquery'], function ($) {
    $.fn[EX]({
        'datePick': function (o) {
            //设置默认值
            o = $[EX]({
                inputOneObj: '',
                inputTwoObj: '', //联动文本框对象
                minDate: '2000-1-1', //最小时间
                maxDate: '2020-1-1', //最大时间
                append: FE,
                addDay: 0,
                month: 1,
                count: 1 //显示日期框数量
            }, o);

            var b = $('body');
            if (o[AP] && $('.datePickAll')[LN] == 0) b[AP]("<div class='datePickAll'></div>");
            var div = $('.datePickAll'); //传来的日期div对象
            //方法代码
            var This = this, //文本框对象
                  json = { div: div, input: This },
                  _d = new Date(), //时间对象
                  nd = _d[GY]() + '-' + (_d[GM]() + 1) + '-' + (_d[GD]()),
                  li = '',
                  li = getLi(),
                  u = "<ul class='ul_day' id='ul_day_{0}'>{1}",
                  week = "<ul class='uw'><li>日</li><li>一</li><li>二</li><li>三</li><li>四</li><li>五</li><li>六</li></ul>",
                  dh0 = $[FO](u, '0', li),
                  dh1 = $[FO](u, '1', li),
                  one = o.inputOneObj,
                  two = o.inputTwoObj,
                  lrState = 0;
            nd = _d = $.addDate('d', o.addDay, nd);
            nd = _d.Format('YYYY-MM-DD');
            //获取li
            function getLi() { for (var i = 0; i < 38; i++) li += '<li></li>'; li += '</ul>'; return li; }

            //显示天li日期值
            //TxtObj: 文本框对象
            //ulObj: 日期li对象数组
            //DateValue: 当前传的日期
            //minDate: 可选择最小日期
            function S(TxtObj, ulObj, DV, minDate, maxDate) {
                var _d = new Date(),
                      val = TxtObj.val().split('-'),
                      dv = DV.split('-'),
                      ul = ulObj,
                //当前传的日期
                      NY = dv[0],
                      NM = dv[1],
                      ND = dv[2],
                //文本框中的日期
                      TY = val[0],
                      TM = val[1],
                      TD = val[2];
                NM = NM.substring(0, 1) == '0' ? parseInt(NM[RP]('0', '')) : parseInt(NM);
                //设置(Day)li对象当月的天数值
                for (var i = 0; i < ul[LN]; i++) {
                    ul[i][RC]('now')[RC]('stop')[RC]('txt').text('')[AT]('a', '0'); //初始化日期li对象
                    NM += i; //获取月还是下个月
                    day_count = $.getDay(NY, NM);  //获取天数
                    day = $.getWeek(NY + '-' + NM + '-' + '1'); //获取星期几，0为周日
                    for (var j = 1; j <= day_count; j++, day++) {
                        if ($.compareDate(minDate, NY + '-' + NM + '-' + j) < 0 || $.compareDate(maxDate, NY + '-' + NM + '-' + j) > 0) ul[i].eq(day)[AC]('stop')[AT]('a', '1'); //追加不能点击日期样式
                        if (j == _d[GD]() && NM == parseInt(_d[GM]()) + 1 && NY == _d[GY]()) ul[i].eq(day)[RC]('stop')[AC]('now'); //追加当天日期样式
                        if (NY == TY && NM == TM && j == TD) ul[i].eq(day)[AC]('txt'); //追加选择日期样式
                        ul[i].eq(day).text(j);
                    }
                }
            };

            //文本框默认赋值
            This.val(nd)[CK](function () {
                div.hide();
                var sd = !$[RT](This.val()) ? nd : This.val();
                //设置联动文本框的日期值
                if (one != '') {
                    if (one.val() == '' || !$[RA](one.val())) { one.val(nd); one.focus()[CK](); } //如果第一个文本框为空,日期控件返回第一个文本框
                    sd = one.val();
                    var arr = sd.split('-');
                    o.minDate = arr[0] + '-' + arr[1] + '-' + (parseInt(arr[2]) + 1);
                    o.maxDate = arr[0] + '-' + (parseInt(arr[1]) + o.month) + '-' + arr[2];
                }
                var a = sd.split('-'),
                      b = a[0],
                      c = a[1].substring(0, 1) == '0' ? parseInt(a[1][RP]('0', '')) : parseInt(a[1]),
                      d = a[2],
                      h1 = "<h1 id='{3}' y='{0}' m='{1}' d='{2}'></h1><h2 id='{4}'>{0}年{1}月</h2>",
                      dp = "<div class='datePick'><span class='{2}'>{0}</span>{1}</div>",
                      sdo = $[FO](h1 + "<h1 id='HR' y='{0}' m='{1}' d='{2}'></h1>", b, c, d, 'HL', 'sc'),
                      sdl = $[FO](h1, b, c, d, 'HL', 'sl'),
                      sdr = $[FO](h1, b, c + 1, d, 'HR', 'sr'),
                      wd = week + dh0;

                switch (o.count) {
                    case 1:
                        div.css(WH, '154px').html($[FO](dp, sdo, wd, 'center'));
                        break;
                    case 2:
                        div.css(WH, '304px').html($[FO](dp, sdl, wd, 'left') + $[FO](dp, sdr, week + dh1, 'right'));
                        break;
                }
                var y = '', //年
                      m = '', //月
                      d = '', //日
                      day_count = '', //天数
                      HL = $('#HL'), //选择月左按钮
                      HR = $('#HR'), //选择月右按钮
                      SL = $('#sl'), //左日期
                      SR = $('#sr'), //右日期
                      SC = $('#sc'), //单日期
                      ul = [$('#ul_day_0 li'), $('#ul_day_1 li')], //天li对象数组
                      uw = $('.uw li'); //周li对象

                //左箭头
                HL[CK](function () {

                    var T = $(this), y = T[AT]('y'), m = T[AT]('m');
                    if (o.count == '2') {
                        if (m == '1') {
                            y = y - 1; m = 11;
                            HR[AT]({ 'y': y, 'm': m + 1 });
                            SR.html(y + '年' + (m + 1) + '月');
                        } else if (m == '2') {
                            y = y - 1; m = 12;
                            HR[AT]({ 'y': y + 1, 'm': 1 });
                            SR.html((y + 1) + '年' + 1 + '月');
                        } else {
                            m = m - 2;
                            HR[AT]({ 'y': y, 'm': m + 1 });
                            SR.html(y + '年' + (m + 1) + '月');
                        }
                        T[AT]({ 'y': y, 'm': m });
                        SL.html(y + '年' + m + '月');
                    } else {
                        if (m == '1') {
                            y = y - 1; m = 12;
                            HR[AT]('y', y + 1)[AT]('m', 1);
                        } else {
                            m = m - 1;
                            HR[AT]({ 'y': y, 'm': m });
                        }
                        T[AT]({ 'y': y, 'm': m });
                        SC.html(y + '年' + m + '月');
                    }
                    S(This, ul, y + '-' + m + '-' + T[AT]('day'), o.minDate, o.maxDate);
                    lrState = -1;
                });

                //右箭头
                HR[CK](function () {
                    var T = $(this), y = parseInt(T[AT]('y')), m = parseInt(T[AT]('m'));
                    if (o.count == '2') {
                        if (m == '12') {
                            y = y + 1; m = 2;
                            T[AT]({ 'y': y, 'm': m });
                            HL[AT]({ 'y': y, 'm': m - 1 });
                            SL.html(y + '年' + (m - 1) + '月');
                            SR.html(y + '年' + m + '月');
                        }
                        else if (m == '11') {
                            m = 12;
                            HL[AT]({ 'y': y, 'm': m });
                            T[AT]({ 'y': y + 1, 'm': 1 });
                            SL.html(y + '年' + m + '月');
                            SR.html(y + 1 + '年' + 1 + '月');
                        }
                        else {
                            m = m + 2;
                            T[AT]({ 'y': y, 'm': m });
                            HL[AT]({ 'y': y, 'm': m - 1 });
                            SL.html(y + '年' + (m - 1) + '月');
                            SR.html(y + '年' + m + '月');
                        }
                    } else {
                        if (m == '12') {
                            y = y + 1; m = 1;
                            HL[AT]({ 'y': y - 1, 'm': 12 });
                            SC.html(y + '年' + m + '月');
                        }
                        else {
                            m = m + 1;
                            HL[AT]({ 'y': y, 'm': m });
                            SC.html(y + '年' + m + '月');
                        }
                        T[AT]({ 'y': y, 'm': m });
                    }
                    S(This, ul, y + '-' + m + '-' + T[AT]('d'), o.minDate, o.maxDate);
                    lrState = 1;
                });

                //鼠标经过、离开、点击(Day)li列事件
                for (var i = 0; i < o.count; i++) {
                    ul[i][MO](function () {
                        var T = $(this);
                        if (T[AT]('a') != '1' && $.trim(T.text()) != '') T[AC](MO)[RC](MU);
                    })[MU](function () {
                        var T = $(this);
                        if (T[AT]('a') != '1' && $.trim(T.text()) != '') T[AC](MU)[RC](MO);
                    })[CK](function () {
                        var T = $(this), t = $.trim(T.text());
                        if (T[AT]('a') == '1' || t == '') return FE;
                        if (lrState == -1) {
                            This.val(HL[AT]('y') + '-' + HL[AT]('m') + '-' + t);
                        } else
                            This.val(HR[AT]('y') + '-' + HR[AT]('m') + '-' + t);
                        if (two != '') two[CK]().val(''); //设置联动
                        else div.hide();
                    });
                };

                //初始化时间
                if (one != '') {
                    if (!$[RA](This.val())) This.val(one.val());
                    S(This, ul, one.val(), o.minDate, o.maxDate);
                } else {
                    var d = '';
                    if (!$[RA](This.val())) { d = nd; This.val(nd); } else d = This.val();
                    S(This, ul, d, o.minDate, o.maxDate);
                };

                //显示日期控件
                div[BG]();
                $.show(json);
                This.select();
                //浏览器大小改变事件
                $[WR](json);
            });
            return This;
        }
    });
    return $;
});
