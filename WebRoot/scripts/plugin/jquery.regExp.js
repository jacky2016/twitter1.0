//define(['jquery'], function ($) {
(function($) {
    $[EX]({
        'regSpecial': function (o) {
            var s = ['+', '-', '&', '|', '!', '(', ')', '{', '}', '[', ']', '^', '"', '~', '*', '?', ':', '\\', '%', ',', '<', '>'],
                  flg = TE;
            for (var i = 0; i < s[LN]; i++) {
                if ($[TM](o).indexOf(s[i]) != -1) { flg = FE; break; }
            }
            return flg;
        },
        'regEmail': function (o) {
            var v = /\w+@.+\..+/;
            return v.test($[TM](o));
        },
        'regMobile': function (o) {
            var v = /^(13[0-9][0-9]{8})|(15[0-9][0-9]{8})|(180[0-9]{8})|(185[0-9]{8})|(186[0-9]{8})|(187[0-9]{8})|(188[0-9]{8})|(189[0-9]{8})$/;
            return v.test($[TM](o));
        },
        'regIP': function (o) {
            var v = /^((1??\d{1,2}|2[0-4]\d|25[0-5])\.){3}(1??\d{1,2}|2[0-4]\d|25[0-5])$/;
            return v.test($[TM](o));
        },
        'regDate': function (o) {
            var date = /^(\d{4})-(\d{1,2})-(\d{1,2})$/, a, d, o = $[TM](o), n1, n2;
            if (!date.test(o)) return FE;
            a = o.split('-'), n1 = parseInt(a[0], 10), n2 = parseInt(a[1], 10);
            if (n1 < 100) a[0] = 2000 + n1 + '';
            d = new Date(a[0], (n2 - 1) + '', a[2]);
            return (d.getFullYear() == a[0] && d.getMonth() == (n2 - 1) + '' && d.getDate() == a[2]) ? TE : FE;
        },
        'regLength': function (o, len) {
            if (o[LN] > len) return FE;
            return TE;
        },
        'regNorm': function (o) {
            var v = /^[A-Za-z0-9._]+$/;
            return v.test($[TM](o));
        },
        'regNorm2': function (o) {
            var v = /^[A-Za-z0-9._@]+$/;
            return v.test($[TM](o));
        },
        'regNorm3': function (o) {
           // var v = /^[A-Za-z0-9._@&]+$/;
            var v = /^[a-zA-Z0-9\u4e00-\u9fa5]+$/;
            return v.test($[TM](o));
        },
        'regNorm4': function (o) {
            var s = ['+', '-','|', '!', '(', ')', '{', '}', '[', ']', '^', '"', '~','?', ':', '\\', '%', ',', '<', '>','`','.','/','·','，','。','、','；','：','‘','’','‘’','、'],
                  flg = TE;
            for (var i = 0; i < s[LN]; i++) {
                if ($[TM](o).indexOf(s[i]) != -1) { flg = FE; break; }
            }
            return flg;
        },
        'regNorm5': function (o) {
            var v = /^[A-Za-z0-9]+$/;
            return v.test($[TM](o));
        },
        'regNumber': function (o) {
            var v = /^\d+$/;
            return v.test($[TM](o));
        },
        'regUrl': function (o) {
            var v = /^http:\/\/\w+/;
            return v.test($[TM](o));
        },
        'regExp':function(o){
        	var Exp=/[A-Za-z0-9\u4E00-\u9FA5\u0620-\u06FF]+/;
        	return Exp.test(o);
        }
    });
})(jQuery);

    //return $;
//});