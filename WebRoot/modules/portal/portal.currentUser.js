define(['portal'], function (context) {
	//消息箱StaticClass
 	function messageBox() { };

	//static method方法
	messageBox.GetCount = function(fn) {
		$(BY)[AJ]({
			dataType: 'text',
			param: {action: 'portal.messageNotify'},
			success: function(data) {
				data=data[RP]('"','')[RP]('"','');
				var array = data.split(',');
				fn(array[0], array[1]);
			}
		});
	};
	//数量递减1
	messageBox.DecreaseCount = function(div) {
		var count = parseInt(div.text());
		count -= 1;
		div.text(count);
	};
	
	//数量加1
	messageBox.AddCount = function(div) {
		var count = parseInt(div.text());
		count += 1;
		div.text(count);
	};
	
	//预警箱
	var warningBox={
		
	};
	
	warningBox.DecreaseCount = function(div, count) {
		var _count = parseInt(div.text());
		_count = _count - count;
		div.text(_count);
	};
	
	//private method
    return {
    	//登陆用户的微博账号集合{id,name,type}
    	Accounts: [], //账号集合
    	UserInfo: {}, //用户信息
    	Faces: [], //表情图片集合
    	Templates: {}, //权限模板对象
    	MessageBox: messageBox,
    	WarningBox: warningBox,
        Init: function () {
        	
        },
        //根据微博类型获取账号
        GetAccountByType: function (type) {
        	return $.grep(this.Accounts, function(val, i){
			    if (val.type == type ) return val;
			});
        },
        GetType: function(accountuid) {
        	var type = -1;
        	$[EH](this.Accounts, function(i, item) {
        		if(item.uid == accountuid)
        		{ 
        			type = item.type; 
        			return FE;
        		 }
        	});
        	return type;
        },
        GetUserInfo: function(fn) {
        	var t = this;
			$(BY)[AJ]({
				param: { action: 'portal.userInfo' },
				success:function(data) { 
					t.UserInfo = data;
					fn();
				}
			});
		},
		GetFaces:function(fn) {
			var t = this;
			if(t.Faces[LN] == 0) {
				$(BY)[AJ]({
		    		param: {action: 'myTwitter.face'},
		    		success: function(data) {
		    			t.Faces = data;
		    			fn(t.Faces);
		    		}
		    	});
			} else {
				fn(t.Faces);
			}
		}
    };
});