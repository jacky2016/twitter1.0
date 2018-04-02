(function ($) {
    $.fn.input = function (o) {
        o = $[EX]({
            id: 'a', //ID号
            type: 'select', //类型
            selected: { text: '', value: '' }, //默认选择值
            collection: [{ text: '', value: ''}] //需要绑定的下拉菜单集合
        }, o);
        var This = this,
              selectTemplate = '<div class="selectInput" id="{1}" value="-1"><div class="pulldown"></div><ul class="list">{0}</ul></div>',
              itemTemplate = '<li class="item" value="{1}">{0}</li>',
              html = '';

        function select(index) {
            var id = o.id + index;
            $[EH](o.collection, function (i, item) {
                html += $[FO](itemTemplate, item.text, item.value);
            });
            This.html($[FO](selectTemplate, html, id));

            //后续操作
            var select = $('#' + id),
                  panel = $('.list', select),
                  items = $('.item', select);

            items[OT]({ over: 'itemed' })[EV](CK, function () {
                var $this = $(this);
                select[AT]('value', $this[AT]('value'))[FD]('.pulldown').text($this.text());
                panel[TG]();
                return FE;
            });

            select[EV](CK, function () {
                panel[TG]();
            });
        }

        function inputFactory(index, type) {
            switch (type) {
                case 'select': select(index); break;
            }
        }

        This[EH](function (index) {
            inputFactory(index, o.type);
        });
        return This;
    }
})(jQuery);