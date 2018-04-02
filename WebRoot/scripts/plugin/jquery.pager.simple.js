//define(['jquery'], function ($) {
(function($) {
   $.fn.pager = function (o) {
        o = $[EX]({
            pageSize: 10,
            pageInfo: TE,
            pageShow: 5,
            count: 752,
            width: 300,
            type: "advanced", //simple
            isTip: TE,
            isOption: FE,
            isFirstLast: TE, //是否能直接跳到首页或尾页
            onClick: function (pageIndex, pageSize) { }, //点击页数事件
            onComplete: function (all, Inverse) { } //加载成功事件
        }, o);

        var This = this,
              pageShowHtml = '<div class="prolist"><div class="pro"><ul>{0}</ul></div></div>',
              pager = '<div class="jPager"><div class="select"><span class="all">全选</span><span class="Inverse">反选</span></div><div class="left">共 <font class="pageAllSize">{0}</font> 条 每页 <font class="pageSize">{1}</font> 条 当前:<font class="pageIndex">{2}</font> /<span class="pageCount">{3}</span>页 <span class="countInfo">第: <font class="pageInfo">{4}</font>条</span></div><div class="right"><a class="first page_button"></a> <a class="prev page_button"></a><a title="前翻" class="clickA"></a> {5} <a title="后翻" class="clickB"></a><a class="next page_button"></a> <a class="last page_button"></a><input class="txt" type="text" value="1" /><div class="btn">跳转</div></div></div>';

        //初始化页数信息
        var pageAllSize = o.count, //总条数
              pageSize = o.pageSize, //每页显示条数
              pageIndex = 1, //当前页索引
              savePageIndex = 1,
              pageCount = Math.ceil(o.count / o.pageSize), //分页翻篇数量
              pageInfo = '', //分页范围信息
              pageShow = '', //分页项信息(Li)
              T1 = '已经是第一页了',
              T2 = '已经是最后一页了',
              UI2 = 'pagerhover',
              UI4 = 'pageractive';
        
        //判断信息是否大于显示条数
        o.pageShow = pageSize > pageAllSize ? 1 : pageCount > o.pageShow ? o.pageShow : pageCount;
        //显示几-几页信息
        pageInfo = '1 - ' + (pageSize > pageAllSize ? pageAllSize : pageSize);

        //追加分页html方法
        function pageHtml(pageIndex, count) {
            pageShow = '';
            for (var i = 0; i < count; i++) pageShow += $[FO]('<li class="" tid="{1}"><a class="page">{0}</a></li>', i + pageIndex * o.pageShow + 1, pageIndex);
        }

        //计算宽方法
        function width(index) {
            return pro[WH](o.pageShow * 27 * (index + 1));
        }

        //鼠标经过离开事件
        function hover(obj) {
            obj[EV](MO, function () {
                $(this)[FD]('a')[AC](UI2);
            })[EV](MU, function () {
                $(this)[FD]('a')[RC](UI2);
            })[EV](CK, function (e) {
                var t = $(this);
                pageIndex = t.text();
                if (o.pageShow != 1 && savePageIndex != pageIndex) {
                    obj[FD]('a')[RC](UI4);
                    t[FD]('a')[RC](UI2)[AC](UI4);
                    i = obj.index(t);
                    txtPageIndex.text(pageIndex);
                    Info(pageIndex);
                    o.onClick(t.text(), o.pageSize);
                    savePageIndex = pageIndex;
                }
            });
        }
        //显示当前分页信息
        function Info(index) {
            txtPageInfo.text((1 + (index - 1) * pageSize) + ' - ' + (pageSize * index));
        }
        //判断是否为最后一页
        function isPageHtml() {
            if (h_page == h_page_count - 1 && h_last_count != 0)
                pageHtml(h_page, h_last_count);
            else
                pageHtml(h_page, o.pageShow);
        }
        //前移方法
        function prev(fn, x, ex) {
            pro[FD]('ul').prepend(pageShow).end().css('left', -o.pageShow * 27)
                [AM]({ left: '+=' + prolistWidth }, 800, function () {
                    fn();
                    pro[WH](width(h_page))[FD]('li[tid' + ex + ']')[RM]();
                    var li = $('.right li', This);
                    btnClick(li, x);
                    hover(li);
                });
        }
        //后移方法
        function next(fn, x, ex) {
            pro[WH](width(h_page))[FD]('ul')[AP](pageShow).end()
                [AM]({ left: '-=' + prolistWidth }, 800, function () {
                    fn();
                    pro[FD]('li[tid' + ex + ']')[RM]().end().css('left', 0);
                    var li = $('.right li', This);
                    btnClick(li, x);
                    hover(li);
                });
        }
        //点击按钮方法
        function btnClick(obj, i) {
            obj.eq(0)[FD]('a')[AC](UI4);
            var t = obj.eq(i)[FD]('a');
            obj[FD]('a')[RC](UI4);
            t[RC](UI2)[AC](UI4);
            Info(pageIndex);
            txtPageIndex.text(pageIndex);
            o.onClick(pageIndex, o.pageSize);
        }

        pageHtml(0, o.pageShow);
        This.html($[FO](pager, pageAllSize, pageSize, pageIndex, pageCount, pageInfo, $[FO](pageShowHtml, pageShow)));

        var jPager = $('.jPager', This),
              prolist = $('.prolist', This),
              pro = $('.pro', This),
              btnFirst = $('.first', This), //首页
              btnPrev = $('.prev', This), //上一页
              btnNext = $('.next', This), //下一页
              btnLast = $('.last', This), //末页
              txt = $('.txt', This), //跳转框
              btn = $('.btn', This), //跳转按钮
              txtPageIndex = $('.pageIndex', This), //当前地几页对象
              txtPageInfo = $('.pageInfo', This), //当前第几条到第几条信息
              h_page = 1, //分页的初始值，为第一页
              h_size = o.pageShow, //每页放几个
              h_count = pageCount, //总页数
              h_page_count = Math.ceil(h_count / h_size), //分页总页数
              h_last_count = h_count % h_size, //最后一页余数
              all = $('.all', This), //全选
              Inverse = $('.Inverse', This); //反选
        if (!o.isOption) { all.hide(); Inverse.hide(); }
        if (!o.isFirstLast) {btnFirst.hide(); btnLast.hide();}
        o.onComplete(all, Inverse);

        if (o.type == "simple") btnFirst.add(btnPrev).add(btnNext).add(btnLast).add(txt).add(btn).hide();
        $('.countInfo', This).hide();
        if (!o.isTip) $('.left', This).hide();
        jPager[WH](o[WH]);
        prolist[WH](o.pageShow * 27);
        width(h_page);
        hover($('.right li', This));

        var prolistWidth = prolist[WH](),
              clickA = $('.clickA', This),
              clickB = $('.clickB', This),
              ul = pro[FD]('ul', This),
              li = ul[FD]('li', This),
              i = 0;


        //默认第一个分页添加选中样式
        li.eq(0)[FD]('a')[AC](UI4);

        //跳转
        var save = 0;
        btn[CK](function () {
            var v = $.trim(txt.val());
            if (pageIndex == v) { alert("当前是" + pageIndex + "页"); return; }
            if (!$[RR](v)) { alert('请输入数字!'); return; }
            if (v > h_count) { alert("最多到(" + h_count + ")页"); return; }
            pageIndex = v;
            h_page = Math.ceil(pageIndex / o.pageShow) - 1;
            p = pageIndex % o.pageShow - 1;
            if (h_page == save) {
                i = p;
                btnClick($('.right li', This), i);
            } else if (h_page > save) {
                isPageHtml();
                next(function () {
                    save = h_page;
                    h_page++;
                    i = p;
                }, p, '!=' + h_page);
            } else {
                pageHtml(h_page, o.pageShow);
                prev(function () {
                    save = h_page;
                    h_page++;
                    i = p;
                }, p, '!=' + h_page);
            }
        });
        //首页
        btnFirst[CK](function () {
            $[SS]();
            pageIndex = 1;
            pageHtml(0, o.pageShow);
            if (!pro.is(':' + AD)) {
                if (h_page == 1) {
                	//$[MB]({content: T1, type: 2});
                    //alert(T1);
                } else {
                	btnFirst.add(btnPrev).show();
                    prev(function () {
                        h_page = 1;
                    }, 0, '!=0');
                }
            }
        });
        //末页
        btnLast[CK](function () {
            $[SS]();
            pageIndex = 1 + (h_page_count - 1) * o.pageShow;

            //判断最后一页余数是否为0
            if (h_last_count == 0) {
                h_last_count = h_size;
            }
            pageHtml(h_page_count - 1, h_last_count);
            if (!pro.is(':' + AD)) {
                if (h_page == h_page_count) {
                	//$[MB]({content: T2, type: 2});
                    //alert(T2);
                } else {
                    next(function () {
                        h_page = h_page_count;
                    }, 0, '!=' + (h_page_count - 1));
                }
            }
            txt.val(1);
        });
        //上一页
        btnPrev[UB](CK)[BD](CK, function () {
            $[SS]();
            if (h_page == 1 && i == 0) {
            	//$[MB]({content: T1, type: 2});
                //alert(T1);
                return;
            }
            var li = $('.right li', This);
            pageIndex--;
            i--;
            if (i == -1) {
                h_page--;
                pageHtml(h_page - 1, o.pageShow);
                prev(function () {
                    pageIndex = (h_page) * o.pageShow;
                    i = o.pageShow - 1;
                }, i, '=' + h_page);
                i = o.pageShow;
                return;
            }
            btnClick(li, i);
            txt.val(1);
        });
        //下一页
        btnNext[UB](CK)[BD](CK, function () {
            $[SS]();
            if (h_page == h_page_count && pageIndex == h_count) {
            	//$[MB]({content: T2, type: 2});
                //alert(T2);
                return;
            }
            var li = $('.right li', This);
            pageIndex++;
            i++;
            if (i == o.pageShow) {
                clickB[CK]();
                pageIndex = 1 + h_page * o.pageShow;
                i = 0;
                return;
            }
            btnClick(li, i);
            txt.val(1);
        });
        //后翻N页
        clickB[UB](CK)[BD](CK, function (e, index) {
            if (!pro.is(':' + AD)) {
                if (h_page == h_page_count) {
                    alert(T2);
                } else {
                    isPageHtml();
                    next(function () {
                        pro[FD]('li[tid!=' + (h_page - 1) + ']')[RM]().end().css('left', 0);
                        pageIndex = 1 + (h_page - 1) * o.pageShow;
                        i = 0;
                    }, 0);
                    h_page++;
                    save++;
                }
            }
            txt.val(1);
        });
        //前翻N页
        clickA[UB](CK)[BD](CK, function () {
            if (!pro.is(':' + AD)) {
                if (h_page == 1) {
                    alert(T1);
                } else {
                    h_page--;
                    save--;
                    pageHtml(h_page - 1, o.pageShow);
                    prev(function () {
                        pageIndex = 1 + (h_page - 1) * o.pageShow;
                        i = 0;
                    }, 0, '=' + h_page);
                }
            }
            txt.val(1);
        });
        return this;
    }
})(jQuery);
    //return $;
//});