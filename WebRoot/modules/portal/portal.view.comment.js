define(['portal','repost'], function(context, Repost) {
	function Comment () { 
		this.tip = '评论';
		this.checkTip = '同时转发到 ';
		this.pmiHtml = '{pmi:wdsy_repost_2}';
	}
	Comment.proxy = $.proxy;
	Comment.fn = Comment.prototype = new Repost ();
	Comment.fn.GetListAjax = function () {
		var t = this,
		 	  moduleType = t.moduleType,
			  dataRow = t.dataSource;

		//我的首页 评论列表
		if (moduleType == 0 || moduleType == 4 ||moduleType==2) {
			t.DrawList({action: 'myTwitter.getComment', uid: dataRow.account.uid, tid: dataRow.tid, platform: dataRow.account.twitterType});
		} else if (moduleType == 1) { //舆情展示 转发列表
			t.DrawList({action: 'view.getCommentList', url: dataRow.url});
		} else if (moduleType==5) { // 舆情处理
			t.DrawList({action: 'deal.dealGetCommentList', url: dataRow.url});
		}
	},
	Comment.fn.DrawList = function(param) {
		//0:头像路径,1:昵称,2:内容,3:时间
		var t = this,
			  //lstTemplate = '<div class="ralay_Lstyle"><div class="cont_photo"><img src="{0}" width="55" height="55" /></div><div class="taikMe_List"><h2><span class="Fname_color">{1}</span><br />{2}</h2></div><div class="cont_do">
			  // \<div class="cont_doleft"><span>{3}</span></div></div><div class="com_clear"></div></div>',
			  panel = $('._content', t.panel),
			  pager = $('.ralay_pager', t.panel);
	  	panel.list({
            width: 885,
            id: t.id,
            pager: pager,
            param: param,
            pageSize: 5,
            isFirstLast: FE,
            templates: [{ html: '<div class="ralay_Lstyle"><div class="cont_photo"><img class="raImg" src="" width="40" height="40" /></div><div class="taikMe_List"><h2><span class="Fname_color raName"></span><br /><span class="contentArea"></span></h2></div><div class="cont_do">'}, //内容
                                { html: '<div class="cont_doleft"><span>{0}</span></div></div><div class="com_clear"></div></div>' }],//时间
            columns: [{ field: 'comment'},
        	 			     { field: 'createTime'}],
            onComplete: function (This, refresh, data) {	   
            	$('.raImg', This)[EH](function(i) {
            		var $this = $(this),
            			   index = $('.raImg', This)[ID]($this),
            			   dataRow = data.rows[index];
            		$this[0].src = dataRow.account.imgurl;
            		$('.raName', This).eq(i).text(dataRow.account.name);
            	});
            	
            	//设置内容
            	var contentArea = $('.contentArea', This);
            	contentArea[EH](function() {
            		var $this = $(this),
            			   index = contentArea[ID]($this),
        		   		   dataRow = data.rows[index],
        		   		   base = context.Base;
					$this.text(base.htmlEncodeByRegExp(dataRow.comment));
            		
            		//解析列表内容上的url
					var contentUrl = base.AnalyzingUrl($this.text());
					$this.html(contentUrl);
				   	
					//解析列表上微博账号名称
					base.AnalyzingTwitterName($this.html(), function (newContent) {
						$this.html(newContent);
						
						//解析关键字
						base.AnalyzingImage($this.html(), function(contnet) {
							$this.html(contnet);
						});
					});
            	});
            	$('.raCount').text(data.realcount);
            }
		});
		/*
		 * 
		
		$[EH](dataSource.data, function(i, commentRow) {
			var account  =commentRow.account;
			lstHtml += $[FO](lstTemplate, account.imgurl, account.name, commentRow.comment, commentRow.createTime);
		});
		*  */
	};
	Comment.fn.Send = function() { 
		var t = this,
			   rTextArea = $('.rTextArea', t.panel),
			   repostSend = $('.repostSend', t.panel);
	   	repostSend[EV](CK, function() {
	   		//文本框的值，是否评论选择框，账号集合[{name: item.name, type: item.type, checked: TE}]，类型：repost、comment
	   		var $this = $(this),
	   			   ckeck = $('#relay_sel', t.panel),
	   			   checked = ckeck[LN] == 0 ? FE : ckeck[0][CD],
	   			   collection = [];
   			$[EH](t.accountCollection, function (i, item) {
   				if(item.checked) collection.push(item);
   			});
   			if($this[AT]('isclick') == 'true' && $this[AT]('class').indexOf('send_over') > -1 && t.RegExp(rTextArea.val(), collection)) {
   				var dataRow = t.dataSource,
   					    tid = dataRow.tid;
			   	//if (dataRow.item != UN) {
			   		//tid = dataRow.item.tid;
			   	//}
			   	$this[AT]('isclick', FE);
	   			t.Sended($this, rTextArea.val(), checked, collection, dataRow.id, dataRow.url, 'commnet', dataRow.tid, $('.rcLstClose', t.panel));   
   			}
	   	});		
		return t;
	};
	Comment.fn.Sended = function() {};
	return Comment;
});