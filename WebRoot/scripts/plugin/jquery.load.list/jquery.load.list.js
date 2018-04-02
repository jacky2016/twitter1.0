//define(['jquery'], function ($) {
(function($) {
    $.fn.loadList = function (o) {
        o = $[EX]({
            width: 0, //分页宽度
            pager: $('#pager'), //分页对象
            pageSize: 10, //分页条数
            isPager: TE, //是否有分页
            isOption: FE, //是否显示全选、反选
            cacheName: '', //缓存名称
            isCache: FE, //是否带缓存
            data: null, //数据
            rows: 'rows', //返回数据的集合字段
            total: 'realcount', //总数key值
            param: {}, //参数
            checkbox: '', //多选框ID或Clss
            templates: [], //模板集合
            columns: [], //列属性
            onComplete: function (This, refresh, data) { } //加载完成事件//This:列表对象,refresh:刷新函数,data:异步数据
        }, o);
        var This = this,
                p = { pageIndex: 1, pageSize: o.pageSize },
                loadList = {
                  	//异步加载信息方法
					//参数:pageIndex:索引,type:是否调用分页方法
					Load: function (pageIndex, type) {
						var t = this;
                      	This[LG]();
                      	if (o.data == null) {
							p.pageIndex = pageIndex;
							if (o.param != '') p = $[EX](p, o.param);
							This[AJ]({
								param: p,
								success:  function (data) {
									
                          			t.DrawData(data, type);
                          		}
                         	 });
                      } else {
                          t.DrawData(o.data, type);
                      }
					},
                  //画信息方法
                  //参数:data:数据, type:是否调用分页类型
                  DrawData: function (data, type) {
                      if (data) {
                          var list = o.rows != '' ? data[o.rows] : data,
                                ulHtml = '',
                                l = this;
                          //追加thead信\追加tbody信息
                          $[EH](list, function (i, d) {
                              $[EH](o.columns, function (t, c) {
                                  var v = d[c.field];
                                  if (v != null)
                                      v = c.maxLength != UN ? v.substring(0, c.maxLength) : v;
                                  if (t < o.templates[LN])
                                      ulHtml += $[FO](o.templates[t].html, v);
                                  else
                                      return FE;
                              });
                          });

                          //显示信息
                          This.html(ulHtml);

                          //设置样式
                          This[FD]('.loadList')[WH](o[WH]);


                          var ID = $('#' + o.id); //表格对象

                          //加载成功事件
                          o.onComplete(This, l.load, data);

                          //显示分页
                          if (o.isPager && type) {
                              o.pager.pager({
                                  count: data[o.total],
                                  pageSize: o.pageSize,
                                  width: o[WH],
                                  isTip: TE,
                                  isOption: o.isOption,
                                  type: 'advanced',
                                  onClick: function (pageIndex, pageSize) {
                                      l.Load(pageIndex, FE);
                                  },
                                  onComplete: function (all, Inverse) {
                                      if (o.isOption) {
                                          //多选
                                          all[UB](CK)[BD](CK, function () { $(o[CB], This)[AT](CD, TE); });
                                          //反选
                                          Inverse[UB](CK)[BD](CK, function () { $(o[CB], This)[EH](function () { var t = $(this); t[AT](CD, !t[AT](CD)); }); });
                                      }
                                  }
                              });
                          }
                      }
                  }
              };
        loadList.Load(1, TE);
        return this;
    }
})(jQuery);
    //return $;
//});