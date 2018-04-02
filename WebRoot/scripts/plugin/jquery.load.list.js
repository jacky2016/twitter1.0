/*
* 无序列表插件（list）
* version: 1.0
* author:  Sunao
* time: 2011-04-20
* compatible: IE6、7、8、9、10、firefox、Chrome
* 说明：加载列表数据，带分页功能
*/
(function ($) {
    $.fn.list = function (o) {
        o = $[EX]({
            width: 0, //分页宽度
            pager: $('#pagerA'), //分页对象
            pageSize: 20, //分页条数
            isPager: TE, //是否有分页
            isOption: FE, //是否显示全选、反选
            isFirstLast: TE, //是否显示跳转首页、尾页
            url: null, //异步路径
            cacheName: '', //缓存名称
            isCache: FE, //是否带缓存
            data: null, //数据
            rows: 'rows', //返回数据的集合字段
            total: 'realcount', //总数key值
            param: {}, //参数
            checkbox: '', //多选框ID或Class
            templates: [], //模板集合
            columns: [], //列属性
            onComplete: function (This, refresh, data) { }, //加载完成事件//This:列表对象,refresh:刷新函数,data:异步数据
        	onProcess: function (message) {}
        }, o);

        var This = this, 
              param = $[EX](o.param, { pageIndex: 1, pageSize: o.pageSize });

        //异步加载信息方法
        //参数:pageIndex:索引,type:是否调用分页方法
        function load(pageIndex, type) {
            This.html('')[LG]();
            if (o.data == null) {
                param.pageIndex = pageIndex;
                This[AJ]({
					param: param,
					success:  function (data) {
              			drawData(data, type);
              		},
              		onProcess: function (message) {
              			o.onProcess(message);
              		}
             	 });
            } else {
                drawData(o.data, type);
            }
        }
        //画信息方法
        //参数:data:数据, type:是否调用分页类型
        function drawData(data, type) {
            if (data) {
                var list = o.rows != '' ? data[o.rows] : data,
                      html = '';
                //追加html
                $[EH](list, function (i, dataRow) {
                    $[EH](o.columns, function (j, column) {
                        var value = dataRow[column.field];
                        if (value != null) {
                            value = column.maxLength != UN ? value.substring(0, column.maxLength) : value;
                        }
                        if (j < o.templates[LN]) {
                            html += $[FO](o.templates[j].html, value);
                        } else {
                            return FE;
                        }
                    });
                });

                //显示信息
                This.html(html);

                //加载成功事件
                o[OK](This, load, data);

                //显示分页
                if (o.isPager && type && data[o.total] > 0) {
                    o.pager.pager({
                        count: data[o.total],
                        pageSize: o.pageSize,
                        width: o[WH],
                        isTip: TE,
                        isOption: o.isOption,
                        isFirstLast: o.isFirstLast,
                        type: 'advanced',
                        onClick: function (pageIndex, pageSize) {
                            load(pageIndex, FE);
                        },
                        onComplete: function (all, Inverse) {
                            if (o.isOption) {
                                //多选
                                all[EV](CK, function () {
                                    $(o[CB], This)[EH](function () {
                                        this[CD] = TE;
                                    });
                                });

                                //反选
                                Inverse[EV](CK, function () {
                                    $(o[CB], This)[EH](function () {
                                        this[CD] = !this[CD];
                                    });
                                });
                            }
                        }
                    });
                }
            }
        }

        //init
        load(1, TE);
        return This;
    }
})(jQuery);