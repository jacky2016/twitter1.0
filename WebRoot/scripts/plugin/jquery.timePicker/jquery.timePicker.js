define(['./jquery'], function ($) {
    $.fn[EX]({
        'timePicker': function (o) {
            o = $.extend({
                id: 'a', //控件ID
                obj: null,
                defaultTime: '0:0:0', //默认时间
                isSecond: TE//是否显示选择秒文本框
            }, o);
            return this[EH](function (i) {
                var This = $(this),
                      timeArr = o.defaultTime.split(':'),
                      b = $(BY),
                      picker = '<div id=' + o.id + i + ' class="timePicker"><div class="left"><input type="text" class="txt hour" value="" mode="hour" maxlength="2" /><span>：</span><input type="text" class="txt minute" value="" mode="minute" maxlength="2" /><span>：</span><input type="text" class="txt second" value="" mode="second" maxlength="2" /></div><div class="right"><ul class="arrow"><li class="up"></li><li class="down"></li></ul></div></div>',
                      div = $('#' + o.id + i);
                if (div[LN] == 0) { This[AP](picker); div = $('#' + o.id + i); }
                var hour = $('.hour', div),
                      minute = $('.minute', div),
                      second = $('.second', div),
                      up = $('.up', div),
                      down = $('.down', div),
                      selectType = '',
                      v = 0,
                      timePicker = {
                          //obj:操作对象,i:选择器默认值索引,number:最大范围数
                          Option: function (obj, i, maxNumber, type) {
                              obj.val(timeArr[i])[UB](CK)[BD](CK, function () {
                                  var t = $(this); t.select(); selectType = type;
                              })[UB]('blur')[BD]('blur', function () {
                                  var t = $(this);
                                  if (!$[RR]($[TM](t.val()))) { t.val(0); }
                              })[UB]('keyup')[BD]('keyup', function () {
                                  var t = $(this);
                                  if (!$[RR]($[TM](t.val()))) { t.val(v); }
                                  if (parseInt($[TM](t.val())) > maxNumber) { t.val(v); }
                                  timePicker.SetValue();
                              })[UB]('paste')[BD]('paste', function () {
                                  This[KU]();
                              })[UB]('cut')[BD]('cut', function () {
                                  This[KU]();
                              });
                          },
                          SetValue: function () {
                              This.attr('time', hour.val() + ":" + minute.val() + ":" + second.val());
                              if (o.obj != null) o.obj.val(hour.val() + ":" + minute.val() + ":" + second.val());
                          }
                      };

                if (!o.isSecond) { second.hide(); second.prev().hide(); }
                timePicker.Option(hour, 0, 23, 'hour');
                timePicker.Option(minute, 1, 59, 'minute');
                timePicker.Option(second, 2, 59, 'second');
                timePicker.SetValue();


                up[UB](CK)[BD](CK, function () {
                    switch (selectType) {
                        case 'hour':
                            var h = parseInt(hour.val());
                            h == 23 ? hour.val(0) : hour.val(h + 1);
                            break;
                        case 'minute':
                            var m = parseInt(minute.val());
                            m == 59 ? minute.val(0) : minute.val(m + 1);
                            break;
                        case 'second':
                            var s = parseInt(second.val());
                            s == 59 ? second.val(0) : second.val(s + 1);
                            break;
                    }
                    timePicker.SetValue();
                })[UB](MO)[BD](MO, function () {
                    $(this)[AC]('uphover');
                })[UB](MU)[BD](MU, function () {
                    $(this)[RC]('uphover');
                });

                down[UB](CK)[BD](CK, function () {
                    switch (selectType) {
                        case 'hour':
                            var h = parseInt(hour.val());
                            h == 0 ? hour.val(23) : hour.val(h - 1);
                            break;
                        case 'minute':
                            var m = parseInt(minute.val());
                            m == 0 ? minute.val(59) : minute.val(m - 1);
                            break;
                        case 'second':
                            var s = parseInt(second.val());
                            s == 0 ? second.val(59) : second.val(s - 1);
                            break;
                    }
                    timePicker.SetValue();
                })[UB](MO)[BD](MO, function () {
                    $(this)[AC]('downhover');
                })[UB](MU)[BD](MU, function () {
                    $(this)[RC]('downhover');
                });
            });
        }
    });
    return $;
});