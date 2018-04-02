define(['./jquery'], function ($) {
    $.fn.pager = function (o) {
        o = $[EX]({
            pageSize: 10,
            count: 152,
            width: 300,
            isTip: TE,
            isOption: TE, //是否显示操作项(全选、反选)
            onClick: function (pageIndex, pageSize) { }, //点击页数事件
            onComplete: function (all, Inverse) { } //加载成功事件
        }, o);
        var This = this,
              pager = '<div class="jPager" style="width:' + o.width + 'px;"><div class="pselect"><span class="all">全选</span><span class="Inverse">反选</span></div><div class="pright"><a class="first">首页</a> <a class="prev">上一页</a><a class="next"> 下一页</a> <a class="last">尾页</a><input class="txt" type="text" value="1" /><div class="btn">跳转</div></div><div class="pleft">共 <font class="pageAllSize">{0}</font> 条 每页 <font class="pageSize">{1}</font> 条 当前:<font class="pageIndex">{2}</font> /<span class="pageCount">{3}</span>页</div></div>';

        //初始化页数信息
        var pageAllSize = o.count,
              pageSize = o.pageSize,
              pageIndex = 1,
              pageCount = Math.ceil(o.count / o.pageSize),
              T1 = '已经是第一页了',
              T2 = '已经是最后一页了';


        This.html($[FO](pager, pageAllSize, pageSize, pageIndex, pageCount));

        var jPager = $('.jPager', This),
              btnFirst = $('.first', This), //首页
              btnPrev = $('.prev', This), //上一页
              btnNext = $('.next', This), //下一页
              btnLast = $('.last', This), //末页
              txt = $('.txt', This), //跳转框
              btn = $('.btn', This), //跳转按钮
              pageIndexObj = $('.pageIndex', This), //索引页数对象
              all = $('.all', This), //全选
              Inverse = $('.Inverse', This); //反选
        if (!o.isOption) { all.hide(); Inverse.hide(); }
        o.onComplete(all, Inverse);

        //点击按钮事件
        function OnClick() {
            pageIndexObj.text(pageIndex);
            o.onClick(pageIndex, o.pageSize);
        }
        //跳转
        btn[CK](function () {
            var v = $[TM](txt.val());
            if (pageIndex == v) { alert("当前是" + pageIndex + "页"); return; }
            if (!$[RR](v)) { alert('请输入数字!'); return; }
            if (v > pageCount) { alert("最多到(" + pageCount + ")页"); return; }
            pageIndex = v;
            OnClick();
        });
        //首页
        btnFirst[CK](function () {
            $[SS]();
            if (pageIndex == 1) { alert(T1); return; }
            pageIndex = 1;
            OnClick();
        });
        //末页
        btnLast[CK](function () {
            $[SS]();
            if (pageIndex == pageCount) { alert(T2); return; }
            pageIndex = pageCount;
            btnClick();
        });
        //上一页
        btnPrev[CK](function () {
            $[SS]();
            if (pageIndex == 1) { alert(T1); return; }
            pageIndex--;
            OnClick();
        });
        //下一页
        btnNext[CK](function () {
            $[SS]();
            if (pageIndex == pageCount) { alert(T2); return; }
            pageIndex++;
            OnClick();
        });
        return this;
    }
    return $;
});