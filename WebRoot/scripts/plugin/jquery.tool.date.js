(function($) {
    var EX = 'extend';
    //转换日期方法
    //date:字符串日期(2011-1-12)
    $.parseDate = function (date) {
        var d, s, a, a1, a2;
        if (date.length > 10) { a = date.split(' '); a1 = a[0].split('-'); a2 = a[1].split(':'); d = new Date(a1[0], a1[1] - 1, a1[2], a2[0], a2[1], a2[2]); }
        else { a = date.split('-'); d = new Date(a[0], a[1], a[2]); }
        return d;
    };
    //比较日期大小方法
    //start: 开始日期
    //end: 结束日期
    $.compareDate = function (start, end) {
    	var s1 = start.split('-'),
               e1 = end.split('-');
        // 判断2014-09-01与2014-08-31比较出错问题
        if ((s1[0] == e1[0]) && start.indexOf('09-01') > -1 && end.indexOf('8-31') > -1) {
            return -1;
        }
        return $.parseDate(end) - $.parseDate(start);
    };
    //获取每月有多少天
    //Year: 年
    //Mon: 月
    $.getDay = function (Year, Mon) {
        return (new Date(Year, Mon, 1) - new Date(Year, Mon - 1, 1)) / (1000 * 60 * 60 * 24);
    };
    //获取星期几
    //dayValue: 字符串日期(2010-1-1)
    $.getWeek = function (date) {
        return new Date(Date.parse(date[RP](/-/g, '/'))).getDay();
    };
    //计算天数方法
    //d1: 日期一
    //d2: 日期二
    $.diffDate = function (d1, d2) {
        return (Date.parse(d2[RP](/\-/g, '/')) - Date.parse(d1[RP](/\-/g, '/'))) / (1000 * 24 * 3600);
    };
    //日期增加函数
    //type:增加类型
    //num:增加数量
    //date:字符串日期(2010-1-1)
    $.addDate = function (type, num, date) {
        var d = new Date(date), n = num;
        if (isNaN(d)) d = new Date();
        switch (type) {
            case 's': return new Date(Date.parse(d) + (1000 * n));
            case 'n': return new Date(Date.parse(d) + (60000 * n));
            case 'h': return new Date(Date.parse(d) + (3600000 * n));
            case 'd': return new Date(Date.parse(d) + (86400000 * n));
            case 'w': return new Date(Date.parse(d) + ((86400000 * 7) * n));
            case 'm': return new Date(d[GY](), (d[GM]()) + n, d[GD](), d[GH](), d[GN](), d[GS]());
            case 'y': return new Date((d[GY]() + n), d[GM](), d[GD](), d[GH](), d[GN](), d[GS]());
        }
    };
    //将字符串日期值转换成date类型日期值
    //date: 日期值
    //param: 日期格式(YMDhms)
    $.dateDecode = function (date, param) {
        var num = date.match(/\d+/g),
          d = new Date(),
          f = param.match(/\w/g);
        for (var i = 0; i < f.length; i++) {
            var n = num[i];
            switch (f[i]) {
                case "Y": d.setFullYear(n); break;
                case "M": d.setMonth(n - 1); break;
                case "D": d.setDate(n); break;
                case "h": d.setHours(n); break;
                case "m": d.setMinutes(n); break;
                case "s": d.setSeconds(n); break;
            }
        }
        return d;
    };
    
    // 获取当前日期方法，格式: 2014-08-28
    $.getDate = function () {
        var date = new Date(),
              year = date.getFullYear(),
			  month = date.getMonth() + 1,
			  day = date.getDate();
        if (month < 10) month = '0' + month;
        if (day < 10) day = '0' + day;
        return year + '-' + month + '-' + day;
    };

    // 获取当前时间方法
    $.getTime = function () {
        var date = new Date(),
              hour = date.getHours(),
			  minute = date.getMinutes(),
              second = date.getSeconds();
        if (hour < 10) hour = '0' + hour;
        if (minute < 10) minute = '0' + minute;
        if (second < 10) second = '0' + second;
        return hour + ':' + minute + ':' + second;
    };

    // 获取当前日期+时间
    $.getDateTime = function () {
        return $.getDate() + ' ' + $.getTime();
    };
    
    //日期格式化方法
    //这里的月份获取自动加了一个月，所以，与js内置的记法不同。
    Date.prototype.Format = function (str) {
        var Week = ['日', '一', '二', '三', '四', '五', '六'],
          t = this;
        str = str[RP](/yyyy|YYYY/, t[GY]());
        str = str[RP](/yy|YY/, (t.getYear() % 100) > 9 ? (t.getYear() % 100)[TS]() : '0' + (t.getYear() % 100));
        str = str[RP](/MM/, (t[GM]() + 1) > 9 ? (t[GM]() + 1)[TS]() : '0' + (t[GM]() + 1));
        str = str[RP](/M/g, (t[GM]() + 1));
        str = str[RP](/w|W/g, Week[t.getDay()]);
        str = str[RP](/dd|DD/, t[GD]() > 9 ? t[GD]()[TS]() : '0' + t[GD]());
        str = str[RP](/d|D/g, t[GD]());
        str = str[RP](/hh|HH/, t[GH]() > 9 ? t[GH]()[TS]() : '0' + t[GH]());
        str = str[RP](/h|H/g, t[GH]());
        str = str[RP](/mm/, t[GN]() > 9 ? t[GN]()[TS]() : '0' + t[GN]());
        str = str[RP](/m/g, t[GN]());
        str = str[RP](/ss|SS/, t[GS]() > 9 ? t[GS]()[TS]() : '0' + t[GS]());
        str = str[RP](/s|S/g, t[GS]());
        return str;
    };
    
})(jQuery);