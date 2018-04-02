$(function () {
    
    //右下弹出
    $("#BtnLeft").window({
        position: "RightDown",
        title: "右下弹出",
        id: "111",
        content: '<div>右下弹出</div><div>右下弹出</div>',
        isMove: FE
    });

    //中间出来，渐渐显示，带遮罩
    $("#BtnCenter").window({
        position: "Center",
        mode: "fadeIn",
        title: "中间弹出",
        id: "222",
        content: '<div>中间渐现</div><div>asdadas</div>',
        layer: true
    });

    //中间弹出，带遮罩
    $("#Btn3").window({
        position: "Center",
        css: { "width": "510px", "height": "auto" },
        title: 'aa',
        content: '<table class="tableFrom"><tr><th width="100">姓名：</th><td width="300"><input type="text" class="name" size="60" /></td></tr><tr><th><input type="radio" class="rbType" checked="checked" name="add1" />选择组：</th><td><select class="contactSelect" style="width: 140px;"><option selected="selected" value="0">aaa</option></select><input type="radio" class="rbType" name="add1" />新建组：<input type="text" class="name" size="20" /></td></tr><tr><th>邮箱：</th><td><input type="text" class="email" size="60" /></td></tr><tr><th>手机：</th><td><input type="text" class="mobile" size="60" /></td></tr><tr><th>MSN：</th><td><input type="text" class="msn" size="60" /></td></tr></table>',
        id: 'my1',
        layer: true,
        onClose: function () {
            $("#contactNameInput").val("dd");
        }
    });

    //左上弹出
    $("#Btn4").window({
        position: "LeftUp",
        title: "左上弹出",
        id: "333",
        content: '<div>asdadas</div><div>asdadas</div>'
    });

    //鼠标经过、离开弹出
    $(".aa").window({
        position: null,
        event: 'hover',
        css: { "width": "200px", "height": "auto", "background-color": "yellow" },
        id: "ssss",
        layer: FE,
        title: "h1特效",
        content: 'sss'
    });

    var b = $("body");
    //    $('body').click(function (e) {
    //        $('.my_win').hide();
    //        b.window({
    //            position: { left: e.pageX, top: e.pageY },
    //            event: "no",
    //            title: "中间",
    //            id: "b1",
    //            layer: FE,
    //            isMove: FE,
    //            content: "中间"
    //        });
    //    });


    //    b.window({
    //        position: "LeftUp",
    //        mode: "slideUp",
    //        event: "no",
    //        title: "左上",
    //        id: "b2",
    //        layer: FE,
    //        content: "左上"
    //    });

    b.window({
        position: "CenterUp",
        event: "no",
        title: "左上",
        id: "b3",
        layer: FE,
        content: "左上"
    });

    //    b.window({
    //        position: "RightUp",
    //        event: "no",
    //        title: "左上",
    //        id: "b4",
    //        layer: FE,
    //        content: "左上"
    //    });

    //    b.window({
    //        position: "RightCenter",
    //        event: "no",
    //        title: "左上",
    //        id: "b5",
    //        layer: FE,
    //        content: "左上"
    //    });

    //    b.window({
    //        position: "RightDown",
    //        event: "no",
    //        title: "左上",
    //        id: "b6",
    //        layer: FE,
    //        content: "左上"
    //    });

    //    b.window({
    //        position: "CenterDown",
    //        event: "no",
    //        title: "左上",
    //        id: "b7",
    //        layer: FE,
    //        content: "左上"
    //    });

    //    b.window({
    //        position: "LeftDown",
    //        event: "no",
    //        title: "左上",
    //        id: "b8",
    //        layer: FE,
    //        content: "左上"
    //    });

    //    b.window({
    //        position: "LeftCenter",
    //        event: "no",
    //        title: "左上",
    //        id: "b9",
    //        layer: FE,
    //        content: "左上"
    //    });

    
});

$(window).load(function () {
    $('body').pngFix();
})