/*
* 消息框插件
* version: 1.0
* author:  Sunao
* time: 2014-05-15
* 目前支持：0:成功，1:提示，2:错误，3:导出等待，4:确认选择，等消息框
*/
(function($) {
    $.messageBox = function (o) {
        o = $[EX]({
            content: '',
            type: 0, //0:成功,1:提示,2:错误,3:导出等待,4:确认选择
            isAutoClose: FE, //是否自动关闭(2妙后自动关闭)
            onAffirm: function (state) { }, //确认事件
            confirmText: '是',
            cancelText: '否'
        }, o);

        //var messagebox = '<div class="messagebox"><div class="count"><div class="pic pic{1}"></div><div class="font">{0}</div></div><div class="jquery_clear"></div><div class="cancel">否</div><div class="ok">是</div><div class="jquery_clear"></div></div>',
        //      end;
        var messagebox = '<div class="messagebox"><div class="count"><div class="pic pic{1}"></div><div class="font">{0}</div></div><div class="jquery_clear"></div><div class="push_ok"><div class="in_MsetCno">{3}</div><div class="in_MsetCok">{2}</div></div><div class="com_clear"></div></div>',
        	   end;
        (function (fnHtml, onAffirm, type, isAutoClose, confirmText, cancelText) {
            $[WW]({
                position: 'center',
                css: { width: '320px', height: 'auto' },
                title: '提示',
                content: fnHtml(),
                id: '_mbox',
                onLoad: function (div, close) {
                	var cancel = $('.in_MsetCno', div),
                	       ok = $('.in_MsetCok', div),
                	       push_ok = $('.push_ok', div);
                	ok.hide();
                	cancel.hide();
                	push_ok.hide();
                    //确认窗体
                    if (type == 4) {
                    	div[FD]('.close')[RM]();
                    	if(confirmText == '') {
                    		ok[RM]();
                    	}
                    	if(cancelText == '') {
                    		cancel[RM]();
                    	}
                    	push_ok.show();
                        //取消
                        cancel.show()[OT]({ over: 'canceled' })[EV](CK, function () {
                            close();
                            onAffirm(FE);
                        });
                        //确定
                        ok.show()[OT]({ over: 'oked' })[EV](CK, function () {
                            close();
                            onAffirm(TE);
                        });
                    }
                    //等待窗体
                    else if (type == 3) {
                        div[FD]('.close').hide();
                    }
                    
                    if(isAutoClose) {
                    	setTimeout(function () {
                    		close();
                    	}, 2000);
                    }
                }
            });
        })(function () {
            return o.type == 3 ?
                '<div class="messagebox"><div class="pic3"></div></div>' :
                $[FO](messagebox, o.content, o.type , o.confirmText, o.cancelText);
        }, o.onAffirm, o.type, o.isAutoClose, o.confirmText, o.cancelText);
    }
})(jQuery);