/*
* 自定义input控件插件
* version: 1.0
* author:  Sunao
* time: 2014-06-07
* compatible: IE6、7、8、9、10、firefox、Chrome
* 说明：目前支持 select控件、
*/
(function ($) {
    $.fn.input = function (o) {
    
        o = $[EX]({
            id: 'a', //ID号
            type: 'select', //类型
            selected: { text: '', value: '' }, //默认选择值
            collection: [{ text: '', value: ''}], //需要绑定的下拉菜单集合
            onChange: $.noop, //下拉菜单索引改变事件
            maxLength: 7
        }, o);

        var selectTemplate = '<div class="selectInput" id="{1}"><div id="pulldownSelect" title="{2}" class="pulldown"></div><ul class="list">{0}</ul></div>',
              itemTemplate = '<li class="item" title="{2}" val="{1}">{0}</li>',
              html = '';

        /*
        * 画Select控件方法
        * $this: div元素对象
        * index: div元素对象索引
        */
        
        function select($this, index) {
            var id = o.id + index;
            $[EH](o.collection, function (i, item) {
            	var text = item.text[LN] > o.maxLength ? item.text.substring(0, o.maxLength): item.text;
                html += $[FO](itemTemplate, text, item.value, item.text);
            });
            $this[0].innerHTML = $[FO](selectTemplate, html, id);
            //后续操作
            var select = $this,
                  panel = $('.list', select),
                  items = $('.item', select),
                  collectionFirst = o.collection[0],
                  itemed = 'itemed', //选择样式名称
                  valueAttr = 'val'; //值属性

			if (o.collection[LN] > 0) {
	            //设置默认值
	            var textFirst = collectionFirst.text[LN] > o.maxLength ? collectionFirst.text.substring(0, o.maxLength): collectionFirst.text;
	            select[AT]('value', collectionFirst.value)[FD]('.pulldown').text(textFirst)[AT]('title', collectionFirst.text);
	            //设置默认项高亮
	            items.eq(0)[AC](itemed);
	
	            items[OT]({ over: itemed })[EV](CK, function () {
	                var $this = $(this),
	                	   value = $this[AT](valueAttr);
	                select[AT]('value', value)[FD]('.pulldown').text($this.text())[AT]('title', $this[AT]('title'));
	                panel[TG]();
	                o.onChange(value);
	                return FE;
	            });
			}

            select[EV](CK, function () {
                panel[TG]();
                //设置选择项高亮
                var value = $(this)[AT](valueAttr);
                items[EH](function () {
                    var $this = $(this);
                    if ($this[AT]('value') == value) {
                        $this[AC](itemed);
                        return FE;
                    }
                });
            });
        }

        /*
        * 选择画控件方法
        * $this: div元素对象
        * index: div元素对象索引
        * type: 选择控件类型
        */
        function inputFactory($this, index, type) {
            switch (type) {
                case 'select': select($this, index); break;
            }
        }

        return this[EH](function (index) {
            inputFactory($(this), index, o.type);
        });
    }
})(jQuery);