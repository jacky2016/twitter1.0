define(['portal'], function(context) {
	function Repost() {
		var t =this;
		t.tip = '转发';
		t.checkTip = '同时评论给 ';
		t.pmiHtml = '{pmi:wdsy_comment_2}';
	};
	Repost.proxy = $.proxy;
	Repost.fn = Repost.prototype;
	Repost.fn.Init = function (div, dataSource, isSelectAccount, accounts, moduleType, currentAccountID, currentAccountName) {
		(function (t) {
			t.panel = div;
			t.id = dataSource.account.uid;
			t.dataSource = dataSource;
			t.isSelectAccount= isSelectAccount;
			t.accounts = accounts;
			t.moduleType = moduleType;
			t.currentAccountID = currentAccountID;
			t.currentAccountName = currentAccountName;
			t.accountCollection = [];
			var state = FE;

	    	$[EH](t.accounts, function(i, item) {
	    		var checked = FE;
	    		
	    		//首页展示模块
	    		if (moduleType == 0) {

	    			if (item.uid == currentAccountID) {
	    				checked = TE;
	    				state = TE;
	    			}
	    		} else {
	    			if( i == 0) checked = TE;
	    		}
	    		if (!item.isExpire) {
	    			t.accountCollection.push({name: item.name, uid: item.uid, type: item.type, checked: checked});
	    		}
	    	});
	    	if(!state && t.accountCollection[LN] > 0) {
	    		t.accountCollection[0].checked = TE;
	    	}
	    	t.Draw();
	    	t.GetListAjax();
		})(this);
	};
	Repost.fn.GetListAjax = function () {
		var t =this,
			  moduleType = t.moduleType,
			  dataRow = t.dataSource;

		if (moduleType == 0 || moduleType == 4 ||moduleType==2) {
			t.DrawList({action: 'myTwitter.getRepost', uid: dataRow.account.uid, tid: dataRow.tid, platform: dataRow.account.twitterType});
		} else if (moduleType == 1) { //舆情展示 转发列表
			t.DrawList({action: 'view.getRepostList', url: dataRow.url});
		} else if (moduleType == 5) { //舆情处理 转发列表
			t.DrawList({action: 'deal.dealGetRepostList', url: dataRow.url});
		}
	}
	Repost.fn.Draw = function() {
		var t = this,
			   //0:昵称,1:账号模板,2:总条数,3:列表集合, 4:顶部提示,5:check提示
			   template = '<h5><span class="reTitle_width">{4}本条微博</span><span class="timing_close rcLstClose"></span></h5><div class="com_clear"></div><div class="ralay_middle"><textarea class="relay_count rTextArea"></textarea><div class="send_do"><div class="font-tip rWord"></div><div class="send_left"><span class="s_bqPc s_pic rface">表情<div class="bq_select com_none rfacePenel" id="rFacePanel"></div></span>'+ context.Base.FormatPmi(this.pmiHtml) +'</div><div><div class="send_right">{1}<div class="send_over send_back repostSend" isclick="true">{4}</div></div><div class="com_clear"></div></div></div><div class="relay_list"><p class="ralay_seeMore">当前已{4}<span class="raCount">{2}</span>次</p><div class="com_clear"></div><div class="_content">{3}</div><div class="com_clear"></div><p class="ralay_seeMore ralay_pager"></p></div></div>',
			   //0:账号列集合
			   accountsTemplate = '<div class="sel_num select_senNum"><span class="s_Rsel" id="rsacbutton">账号选择</span><div class="sel_numList com_none" id="rsacPanel"></div></div>',
			   //0:ltem索引,1:账号类型,2:账号名称
			   accountItemTemplate = '<div class="zhao_list"><input type="checkbox" name="zhao_num" class="zhao_liSel selectCB_list" id="zhao_{0}"/><label class="mynum_type{1} " for="zhao_{0}">{2}</label></div>';
			   
		
		//画账号
		var accountHtml = '';
		if (t.isSelectAccount) {
			var accountsItemHtml = '';

			$[EH](t.accountCollection, function(i, account) {
				accountsItemHtml += $[FO](accountItemTemplate, i, account.type, account.name);
			});
			accountHtml = $[FO](accountsTemplate, accountsItemHtml);
		}

		t.panel.html($[FO](template, 
			this.tip == '转发' ? t.dataSource.account.name: '我的微博', 
			accountHtml, 
			0, 
			'',
			t.tip,
			t.checkTip)
		);
		
		t.Option();
	};
	Repost.fn.Count = function () {
		return this.dataSource.data.rows[LN];
	};
	Repost.fn.DrawList = function(param) {
		//0:头像路径,1:昵称,2:内容,3:时间
		var t = this,
			   //lstTemplate = '<div class="ralay_Lstyle"><div class="cont_photo"><img src="{0}" width="55" height="55" /></div><div class="taikMe_List"><h2><span class="Fname_color">{1}</span><br />{2}</h2></div><div class="cont_do"><div class="cont_doleft"><span>{3}</span></div><div class="cont_doright"></div></div><div class="com_clear"></div></div>',
			   panel = $('._content', t.panel),
			   pager = $('.ralay_pager', t.panel);

		panel.list({
            width: 885,
            pager: pager,
            param: param,
            pageSize: 5,
            isFirstLast: FE,
            templates: [{ html: '<div class="ralay_Lstyle"><div class="cont_photo"><img class="raImg" src="" width="40" height="40" /></div><div class="taikMe_List"><h2><span class="Fname_color raName"></span><br /><span class="contentArea"></span></h2></div><div class="cont_do">'}, //内容
                             { html: '<div class="cont_doleft"><span>{0}</span></div><div class="cont_doright"></div></div><div class="com_clear"></div></div>' }],//时间
            columns: [{ field: 'text'},
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
					$this.text(base.htmlEncodeByRegExp(dataRow.text));
            		
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
		
		//画列表
		/*
		 * 
		
		$[EH](dataSource.data, function(i, repostRow) {
			var account  = repostRow.account;
			lstHtml += $[FO](lstTemplate, account.imgurl, account.name, repostRow.text, repostRow.createTime);
		});
		*  */
	};
	Repost.fn.Close = function () {
		var  t = this;
		$('.rcLstClose', t.panel)[EV](CK, function () {
			t.panel[TG]();
		});
	    return t;
	};
	Repost.fn.FacePicker = function () {
		var t = this, 
			   rface = $('.rface', t.panel),
			   rfacePenel = $('.rfacePenel', t.panel),
			   rTextArea = $('.rTextArea', t.panel);
		context.Base.FacePicker(rface, rfacePenel, rTextArea, null);
		return t;
	};
	Repost.fn.AccountPicker = function () {
		var t = this, 
			   rsacbutton = $('#rsacbutton', t.panel),
			   rsacPanel = $('#rsacPanel', t.panel);
		context.Base.AccountPicker('_myRc', rsacbutton, rsacPanel, t.accountCollection , function (cball, cbItem) {
			if(t.moduleType == 0) {
				if (cbItem[LN] != 0)	 {
					cbItem[EH](function() {
						if(this.value == t.currentAccountID) {
							this[CD] = TE;
							return FE;
						}
					});
				}
			} else {
				if(cbItem[LN] != 0) {
					cbItem[0][CD] = TE;
				}
			}
			cbItem[EV]('change', function() {
				var flg = TE;
				var $this = $(this);
				cbItem[EH](function(){
					if(!this[CD]) {
						flg = FE;
						return FE;
					}
				});
				cball[0][CD] = flg;

			});
		}, function (accountCollection) { }, null);
		return t;
	};
	Repost.fn.LimitWord = function () {
		var t = this,
			   rTextArea = $('.rTextArea', t.panel),
			   rWord = $('.rWord', t.panel),
			   rBtn = $('.repostSend', t.panel),
			   account = t.dataSource.account;
	  	if(t.dataSource.item && t.tip == '转发') {
			rTextArea.text('//@' + account.name + ': ' + t.dataSource.text);
	  	}
		context.Base.LimitWord(rTextArea, rWord, rBtn);

		return t;
	};
	Repost.fn.Option = function() {
		var  t = this,
			    div = t.panel,
			    base = context.Base,
			    more = $('.more', div);
			    
		t.LimitWord().FacePicker().AccountPicker().Close().Send();
	};
	Repost.fn.RegExp = function(content, collection) {
		//if($[TM](content) == '') { $[MB]({content: '内容不能为空', type: 2}); return FE;} 
		if(collection[LN] == 0) { $[MB]({content: '请选择用户', type: 2}); return FE;} 
		return TE;
	};
	Repost.fn.Send = function() { 
		var t = this,
			  rTextArea = $('.rTextArea', t.panel),
			  repostSend = $('.repostSend', t.panel);
	   	repostSend[EV](CK, function() {
	   		//文本框的值，是否评论选择框，账号集合[{name: item.name, type: item.type, checked: TE}]，类型：repost、comment
	   		
	   		var $this = $(this),
	   			   ckeck = $('#relay_sel', t.panel);
	   			   checked = ckeck[LN] == 0 ? FE : ckeck[0][CD],
	   			   collection = [];
	   		
   			$[EH](t.accountCollection, function (i, item) {
   				if(item.checked) collection.push(item);
   			});
   			if($this[AT]('isclick') == 'true' && $this[AT]('class').indexOf('send_over') > -1 && t.RegExp(rTextArea.val(), collection)) {
   				var dataRow = t.dataSource,
   					   tid = dataRow.tid;
   				if (dataRow.item != UN) {
			   		tid = dataRow.item.tid;
			   	}
			   	$this[AT]('isclick', FE);
	   			t.Sended($this, rTextArea.val(), checked, collection, dataRow.id, dataRow.url, 'repost', tid, $('.rcLstClose', t.panel));   
   			} 
	   	});		
		return t;
	};
	//回调函数
	Repost.fn.Sended = function() {};
	return Repost;
 });