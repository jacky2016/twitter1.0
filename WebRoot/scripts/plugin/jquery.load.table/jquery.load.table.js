//define(['jquery'], function ($) {
(function($) {
    $.fn[EX]({
        'loadTable': function (o) {
            o = $[EX]({
                pager: $('#pagerA'),
                pageShow: 5, //显示几分分页数
                pageSize: 15, //显示条数
                width: 0, //表格宽
                trHeight: 24, //表格默认高
                isPager: TE, //是否分页
                cacheName: 'a',
                isCache: FE,
                isCheckBox: TE, //是否有多选按钮
                data: null, //数据
                rows: 'rows', //返回数据的集合字段
                total: 'realcount', //总数key值
                columns: [], //列属性
                param: {}, //参数
                option: [], //操作功能类型,delete
                id: 'm', //控件ID
                firstWidth: '5%',
                lastWidth: '10%',
                onComplete: function (cb, This, refresh, data) { }, //加载成功事件
                onError: function (data) { } //异常事件
            }, o);
            var This = this,
                  p = { pageIndex: 1, pageSize: o.pageSize },
                  tabel = '<table id=' + o.id + ' class="loadTable web_table" border="0" cellspacing="0" cellpadding="0"><tr>{0}</tr>{1}</table>',
                  selectAll = '<th width="{0}"><input class="selectAll" type="checkbox" /></th>',
                  select = '<td align="left"><input class="select" type="checkbox" value="{0}" /></td>',
                  th = ' <th width="{0}">{1}</th>',
                  thOption = ' <th width="{0}">操作</th>',
                  deleteBtn = '<span class="delete" id="{0}"></span>',
                  td = '<td class="{1}" style="{2}">&nbsp;{0}</td>',
                  loadTable = {
                      //画信息方法
                      //参数:data:数据, type:是否调用分页类型,isCache:是否缓存
                      DrawData: function (data, type, isCache) {
                          if (data) {
                              var list = o.rows != '' ? data[o.rows] : data,
                                    tdHtml = '',
                                    headHtml = '',
                                    html = '',
                                    htmlOption = '',
                                    t = this;
                              //追加thead信\追加tbody信息
							$[EH](list, function (i, d) {
								tdHtml += '<tr>';
								$[EH](o.columns, function (j, t) {
									if (i == 0) {
										if(t.type == CB)
                                  	  		headHtml = $[FO](selectAll, o.firstWidth);
                                  	  	else
                                  	  		headHtml += $[FO](th, t[WH], t.caption); //追加头部
									}
									var v = t.maxLength != UN ? d[t.field].substring(0, t.maxLength) : d[t.field];
									var s = t.style != UN ? t.style : '';
                                      switch (t.type) {
                                          case CB:
                                              tdHtml += $[FO](select, v);
                                              htmlOption = $[FO](deleteBtn, v);
                                              break;
                                          case DT:
                                              tdHtml += $[FO](td, v, "t" + j, s);
                                              break;
                                          default:
                                              tdHtml += $[FO](td, v, "t" + j, s);
                                              break;
                                      }
                                  });
                                  if (o.option[LN] > 0) tdHtml += $[FO](td, htmlOption, 'tdOption', 'cursor:pointer');
                                  tdHtml += "</tr>";
                              });
                              if (list[LN] == 0)
                                  $[EH](o.columns, function (j, t) {
                                      if (j > 0) headHtml += $[FO](th, t[WH], t.caption); //追加头部
                                  });

                              //追加操作项
                              if (o.option[LN] > 0) headHtml +=  $[FO](thOption, o.lastWidth);

                              //追加表格
                              html = $[FO](tabel, headHtml, tdHtml);
                              //显示信息
                              This.html(html);

                              //设置样式
                              This[FD]('.loadTable')[WH](o[WH]);
                              This[FD]('.loadTable tr')[HT](o.trHeight);
							  
                              var ID = $('#' + o.id); //表格对象

							
                              //点击cb事件
                              var sa = $('.selectAll', ID),
                              	  so = $('.select', This);
                              	  
                              //是否有多选框
							  if(o.isCheckBox) {
	                              sa[UB](CK)[BD](CK, function () {
	                                  if ($(this)[AT](CD))
	                                      so[AT](CD, TE);
	                                  else
	                                      so.removeAttr(CD);
	                              });
                              } else {
                              	sa.hide();	  
                              }

                              //鼠标经过tr事件
                              $('tr', ID)[OT]({ over: JQH });

                              //加载成功事件
                              o.onComplete(so, This, t.Load, data);

                              This[AT]('iscache', 'true');

                              //显示分页
                              if (o.isPager && type) {

                                  o.pager.pager({
                                      count: data[o.total],
                                      pageShow: o.pageShow,
                                      pageSize: o.pageSize,
                                      width: o[WH],
                                      isTip: TE,
                                      type: 'advanced',
                                      onClick: function (pageIndex, pageSize) {
                                          t.Load(pageIndex, FE, isCache);
                                      },
                                      onComplete: function (all, Inverse, middle) {
                                          //多选
                                          all[UB](CK)[BD](CK, function () { $('.select', This)[AT](CD, TE); });
                                          //反选
                                          Inverse[UB](CK)[BD](CK, function () { $('.select', This)[EH](function () { var t = $(this); t[AT](CD, !t[AT](CD)); }); });
                                      }
                                  });
                              }
                          }
                      },
                      //异步加载信息方法
                      //参数:pageIndex:索引,type:是否调用分页方法,isCache:是否缓存
                      Load: function (pageIndex, type, isCache) {
                          var t = this;
                          This[LG]();
                          if (o.data == null) {
                              p.pageIndex = pageIndex;
                              if (o.param != '') p = $[EX](p, o.param);
                              This[AJ]({
								param: p,
								success:  function (data) {
									if(data !=null && data[o.rows][LN] > 0)
                          				t.DrawData(data, type);
                      				else {
                      					var _obj = {};
                      					_obj[o.total] = 0;
                      					_obj[o.rows] = [];
                      					t.DrawData(_obj, type);
                      				}
                          		},
                               error: function (v) {
                                  o.onError(v);
                               }
                         	 });
                          } else {
                              t.DrawData(o.data, type, FE);
                          }
                      }
                  };
            loadTable.Load(1, TE, o.isCache);
            return this;
        }
    });
})(jQuery);
    //return $;
//});