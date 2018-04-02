/*
* 有序列表插件（table）
* version: 1.0
* author:  Sunao
* time: 2011-04-13
* compatible: IE6、7、8、9、10、firefox、Chrome
* 说明：
* column列: { caption: '选择', field: 'ID', type: 'checkbox', width: '5%', maxLength: 20, className: '', style: '', template:'' }
* caption: 头部显示信息
* field: 异步数据对应的列Key（Json的Key）
* type: 目前支持的列类型 checkbox（多选框列）、number（自增长列）、string（字符串列）、int（整形列）、date（日期列）、html（自定义模板列）
* width: 列的宽度，目前采用百分比设定，所有列的宽度之和为100%即可
* maxLength: 字符串类型列专用，最大能显示字符串的多少
* className: 样式名称，定义每列td的样式名称，便于开发人员使用
* style: 设置每列的样式
* template: html类型列专用，展示的模板信息
*/
(function ($) {
    $.fn.table = function (o) {
        o = $[EX]({
            pager: null, //分页元素对象
            pageShow: 5, //显示几分分页数
            pageSize: 15, //显示条数
            width: 0, //表格宽
            trHeight: 24, //表格默认高
            isPager: TE, //是否分页
            url: null, //异步路径
            methodName: '', //方法名称
            cacheName: 'a',
            isCache: FE,
            data: null, //数据
            rows: 'rows', //返回数据的集合字段
            total: 'realcount', //总数key值
            columns: [], //列属性
            param: {}, //参数
            id: 't', //控件ID
            isOption: FE, //分页是否支持全选、反选功能
            onComplete: function (This, refresh, data) { } //加载成功事件
        }, o);
        var This = this,
              columns = o.columns,
              param = $[EX](o.param, { pageIndex: 1, pageSize: o.pageSize }),
              tabel = '<table id=' + o.id + ' class="web_table" border="0" cellspacing="0" cellpadding="0"><tr>{0}</tr>{1}</table>',
              selectTh = '<th width="{0}"><input class="selectAll" type="checkbox" /></th>',
              selectTd = '<td width="{0}"><input class="select" type="checkbox" value="{1}" /></td>',
              th = ' <th width="{0}">{1}</th>',
              td = '<td class="{1}" style="{2}">&nbsp;{0}</td>';

        /*
        * 画信息方法
        * data:数据
        * type:是否调用分页类型
        * isCache:是否缓存
        */
        function drawData(data, type, isCache, pageIndex) {
            if (data) {
                var t = this,
                      list = o.rows != '' ? data[o.rows] : data,
                      tdHtml = '',
                      headHtml = '',
                      html = '';
                //追加thead信\追加tbody信息
                $[EH](list, function (i, dataRow) {
                    if (i == 0) {
                        headHtml += '<tr>';
                    }
                    tdHtml += '<tr>';
                    $[EH](columns, function (j, columnRow) {
                        var maxLength = columnRow.maxLength,
                              style = columnRow.style,
                              width = columnRow[WH],
                              className = columnRow.className,
                              type = columnRow.type,
                              caption = columnRow.caption,
                              value = dataRow[columnRow.field];

                        //处理数据值，如果是字符串类型列，判断是否需要截取显示值的长度
                        if (type == 'string')
                            value = maxLength != UN ? value.substring(0, maxLength) : value;
                        //处理列属性样式
                        style = style != UN ? style : '';
                        //处理列属性class名称，如果没定义，就默认为t+索引值
                        className = className != UN ? className : 't' + j;
                        switch (columnRow.type) {
                            case CB: //checkbox列
                                if (i == 0) {
                                    headHtml += $[FO](selectTh, width);
                                }
                                tdHtml += $[FO](selectTd, width, value);
                                break;
                            case 'html': //模板列
                                if (i == 0) {
                                    headHtml += $[FO](th, width, caption);
                                }
                                tdHtml += $[FO](td, columnRow.template, className, style);
                                break;
                            default:
                                if (i == 0) {
                                    headHtml += $[FO](th, width, caption);
                                }
                                tdHtml += $[FO](td, columnRow.type == 'number' ? ((pageIndex - 1) * o.pageSize + i + 1) : value, className, style);
                                break;
                        }
                    });
                    if (i == 0) {
                        headHtml += '</tr>';
                    }
                    tdHtml += '</tr>';
                });

                //追加表格
                html = $[FO](tabel, headHtml, tdHtml);

                //显示信息
                This.html(html);

                //设置样式
                This[FD]('.loadTable')[WH](o[WH]);
                This[FD]('.loadTable tr')[HT](o.trHeight);

                //点击全选checkbox事件
                var selectAll = $('.selectAll', This),
                      select = $('.select', This);
                selectAll[EV](CK, function () {
                    var isSelect = this[CD];
                    select[EH](function () {
                        this[CD] = isSelect;
                    });
                });

                //鼠标经过tr事件
                $('tr', This)[OT]({ over: JQH });

                //加载成功事件
                o[OK](This, load, data);

                //显示分页
                if ($.fn.pager && o.isPager && type && data[o.total] > 0) {
                    o.pager.pager({
                        count: data[o.total],
                        pageShow: o.pageShow,
                        pageSize: o.pageSize,
                        width: o[WH],
                        isOption: o.isOption,
                        onClick: function (pageIndex, pageSize) {
                            load(pageIndex, FE, isCache);
                        },
                        onComplete: function (all, Inverse) {
                            if (o.isOption) {
                                //多选
                                all[EV](CK, function () {
                                    $('.select', This)[EH](function () {
                                        this[CD] = TE;
                                    });
                                });
                                //反选
                                Inverse[EV](CK, function () {
                                    $('.select', This)[EH](function () {
                                        this[CD] = !this[CD];
                                    });
                                });
                            }
                        }
                    });
                }
            }
        }
        /*
        * 异步加载信息方法
        * pageIndex:索引
        * type:是否调用分页方法,
        * isCache:是否缓存
        */
        function load(pageIndex, type, isCache, data) {
            var t = this;
            This[LG]();
            if (data == null) {
                param.pageIndex = pageIndex;
                This[AJ]({
					param: param,
					success:  function (data) {
						if(data !=null && data[o.rows][LN] > 0)
              				drawData(data, type, isCache, pageIndex);
          				else {
          					var _obj = {};
          					_obj[o.total] = 0;
          					_obj[o.rows] = [];
          					drawData(_obj, type, FE, pageIndex);
          				}
              		}
                });
            } else {
                drawData(data, type, FE, pageIndex);
            }
        }
        //init
        load(1, TE, o.isCache, o.data);
        return This;
    }
})(jQuery);