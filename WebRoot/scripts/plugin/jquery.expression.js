//define(['jquery'], function ($) {
(function($) {
    $.fn.expression = function (o) {
        o = $[EX]({
            select: [{ value: 'content', text: '正文' }, { value: 'title', text: '标题' }, { value: 'source', text: '媒体'}],
            isMult: FE, //是否有多根
            value: null, //树形结构值
            type: '0', //表达式类型0:新闻,1:论坛,2:国内微博,3:视频,4:国外微博,5:FaceBook
            onRegExp: function () { return TE; }, //验证方法
            onOK: function (txt, key, exp) { }, //txt:表达式文本,key:表达式语法,exp:表达式结构
            onCancel: function () { }
        }, o);
        var This = this,
              b = $('body'),
              Main = '<table class="tableMain" cellpadding="0"><tbody>{0}</tbody></table><div class=" divClear"></div>',
              tpA = '<tr><td nowrap=""><select class="selectMain" types="{0}"><option value="OR">或者</option> <option value="AND">并且</option></select><input class="option {1}" type="button" value="-"></td><td class="cape" nowrap=""><table class="Role" cellpadding="0"> <tbody> <tr> <td> <div class="left"> </div> <div class="right"> </div></td></tr></tbody></table><table class="tableChild" cellpadding="0"><tbody>',
              tpB = '</tbody></table><input type="button" title="添加条件" value="+" class="option btnAddBrothers" /> <input type="button" title="添加子条件" value="+[]" class="option btnAddParent" /><table class="Role" cellpadding="0"><tbody><tr><td><div class="left"></div><div class="right"></div></td></tr></tbody></table>',
              tableParent = tpA + '{0}' + tpB,
              tableChild = '<tr><td nowrap="" align="right"><input type="button" value="-" class="option btnReomveBrothers" /></td><td nowrap=""><table class="clause" cellspacing="1" cellpadding="0" border="0"><tbody><tr><td nowrap=""><select class="field" types="{2}">{0}</select></td><td nowrap=""><select class="term" types="{3}"> <option value="Contains" type="">包含</option> <option value="NOT" type="-">不包含</option> </select></td><td nowrap=""><input type="text" maxlength="100" class="value" value="{1}" /></td></tr></tbody></table></td></tr>',
              multParent = !o.isMult ? '' : '<input type="button" title="添加条件" value="+" class="option btnMain" />';

        This.html($[FO]('<div class="divExpression"><div class="divTable"></div><div class="result" result=""></div>{0}<input type="button" value="确定" class="btnOK" /><input type="button" value="取消" class="btnCancel" /><span class="expressionTip" style="color: red;"></span></div>', multParent));

        var div = $('.divExpression', This),
                  divTable = div.find('.divTable'),
                  selectHtml = '',
                  resultDiv = $('.result', This), //显示结果div对象
                  expressionTip = $('.expressionTip', This),
                  expObj = {},
                  isPass = TE,
                  passTxt = '',
                  expression = {
                      //追加计算select方法
                      GetSelect: function () {
                          $[EH](o.select, function (i, item) {
                              selectHtml += $[FO]('<option value="{0}">{1}</option>', item.value, item.text);
                          });
                      },
                      //设置select值
                      //参数：select对象
                      SetSelect: function (select) {
                          select[EH](function () {
                              this.value = $(this)[AT]('types');
                          });
                      },
                      //画默认节点方法
                      DrawDefaultNode: function () {
                          var t = this;
                          if (o.value == null || o.value == 'null') {
                              expObj = {};
                              divTable.html($[FO](Main, $[FO](tableParent, '', 'btnRemoveMain')));
                          } else {
                              expObj = o.value;
                              divTable.html($[FO](Main, t.DrawNode(expObj, 'btnRemoveMain')));
                              t.SetSelect($('.selectMain', This));
                              t.SetSelect($('.field', This));
                              t.SetSelect($('.term', This));
                          }
                          if (!o.isMult) $('.btnRemoveMain', This)[RM](); //移除删除主节点按钮
                          resultDiv = $('.result', This);

                          t.SetMainNode(This);
                          t.SetNode(This);
                          t.SelectChange($('.selectMain', This));
                          t.SetBrothers(divTable);
                          t.GetTreeTxtKey();

                          //点击确定事件
                          $('.btnOK', This)[CK](function () {
                          	  expressionTip.text('');
                              if (!isPass) { expressionTip.text(passTxt); return; }
                              if(!t.InputRegExp()){return;}
                              if(resultDiv.attr('result')[LN] > 4000) {expressionTip.text('表达式长度最多到4000!');return;}
                              if (o.onRegExp() === FE) { expressionTip.text('表达式验证错误!'); return; }
                              t.PerfixExpress();
                              t.GetExpText(expObj);
                              t.GetExpKey(expObj);
                              o.onOK($.trim(resultDiv.text()), resultDiv[AT]('result'), expObj);
                          });
                          //取消事件
                          $('.btnCancel', This)[CK](function () {
                              o.onCancel();
                          });
                      },
                      /*增加父亲节点方法
                      参数：btn:添加父亲节点按钮
                      */
                      AddParent: function (btn) {
                          var t = this;
                          btn[UB](CK)[BD](CK, function () {
                              $(this).prev().prev()[AP]($[FO](tableParent, '', 'btnRemoveParent'));
                              t.SelectChange($('.selectMain', This));
                              t.SetNode(This);
                              t.GetTreeTxtKey();
                          });
                      },
                      /*增加兄弟节点方法
                      参数：btn:添加兄弟节点按钮
                      */
                      AddBrothers: function (btn) {
                          var t = this;
                          btn[UB](CK)[BD](CK, function () {
                              var $brothers = $($[FO](tableChild, selectHtml, '', '', ''));
                              $(this).prev()[AP]($brothers);
                              t.SetNode($brothers);
                              t.SetBrothers($brothers);
                              t.GetTreeTxtKey();
                          });
                      },
                      /*增加主节点方法
                      参数：btn:添加主节点按钮
                      */
                      AddMain: function (btn) {
                          var t = this;
                          btn[UB](CK)[BD](CK, function () {
                              divTable[AP]($[FO](Main, $[FO](tableParent, '', 'btnRemoveMain')));
                              t.SetMainNode(This);
                              t.SetNode(This);
                          });
                      },
                      /*移除主节点方法
                      参数：btn:移除主节点按钮
                      */
                      RemoveMain: function (btn) {
                          btn[UB](CK)[BD](CK, function () {
                              var t = $(this), table = t[PT]()[PT]()[PT]()[PT]();
                              table[RM]();
                          });
                      },
                      /*移除点方法
                      参数：btn:移除按钮
                      */
                      RemoveNode: function (btn) {
                          var t = this;
                          btn[UB](CK)[BD](CK, function () {
                              $(this)[PT]()[PT]()[RM]();
                              t.GetTreeTxtKey();
                          });
                      },
                      //创建表达式结构入口方法
                      PerfixExpress: function () {
                          var root = $('.divTable', This).find(">table");
                          expObj = { Operator: root.find("select:eq(0)").val(), Nodes: [] };
                          this.GetCriteria(root, expObj);
                          expObj = expObj.Nodes[0];
                      },
                      //递归追加结构方法
                      GetCriteria: function (table, criteria) {
                          var t = this;
                          table.find(">tbody>tr")[EH](function () {
                              // 在当前行上找表达式
                              var tr = $(this),
                                    tables = tr.find(">td>table");
                              if (tables.length == 1) {
                                  var filed = tr.find(".field").val(),
                                        term = tr.find(".term").val(),
                                        value = tr.find(".value").val();
                                  criteria.Nodes.push({ Field: filed, Term: term, Value: value });
                              }
                              if (tables.length == 3) {
                                  var subtable = $(tables[1]),
                                        op = subtable[PT]().siblings().find(">select").val(),
                                        subCriteria = { Operator: op, Nodes: [] };
                                  criteria.Nodes.push(subCriteria);
                                  t.GetCriteria(subtable, subCriteria);
                              }
                          });
                      },
                      //文本框键盘按下事件
                      Keyup: function (input) {
                          var t = this, p = input;
                          if (p[AT]('class') != undefined) {
                              p[UB](KU)[BD](KU, function () {
                                  t.GetTreeTxtKey();
                              })[UB]('paste')[BD]('paste', function () {
                                  setTimeout(function () { p[KU](); }, 30);
                              })[UB]('cut')[BD]('cut', function () {
                                  setTimeout(function () { p[KU](); }, 30);
                              });
                          }
                      },
                      //下拉菜单索引改变事件
                      SelectChange: function (select) {
                          var t = this;
                          select[UB](CE)[BD](CE, function () {
                              t.GetTreeTxtKey();
                          });
                      },
                      //获取符号的中文名称
                      //参数：符号select的key
                      GetTermText: function (v) {
                          return v == 'Contains' ? '包含' : '不包含';
                      },
                      //获取类别的中文名称
                      //参数：类别select的key
                      GetFieldText: function (v) {
                          var txt = '';
                          $[EH](o.select, function (i, item) { if (v == item.value) { txt = item.text; return FE; } });
                          return txt;
                      },
                      //验证表达式规范
                      //参数：表达式树形结构
                      RegExp: function (exp) {
                          var p = exp.Operator, flag = p == 'AND' ? FE : TE;
                          if (p) {
                              for (var i = 0; i < exp.Nodes.length; i++) {
                                  var node = exp.Nodes[i];
                                  if (node.Field) {
                                      if (p == 'AND' && node.Term == 'Contains') {
                                          flag = TE;
                                          passTxt = "并且子集必须有一项是<包含>";
                                      } else if (p == 'OR' && node.Term == 'NOT') {
                                          flag = FE;
                                          passTxt = "或者子集所有项不能有<不包含>";
                                          break;
                                      }
                                  } else {
                                      flag = this.RegExp(node);
                                      if (p == 'OR' && flag == FE) break;
                                  }
                              }
                          }
                          isPass = flag;
                          //alert(isPass);
                          return flag;
                      },
                      //递归画表达式显示文本(前台用)
                      //参数：表达式树形结构
                      GetExpText: function (exp) {
                          var result = "", t = this;
                          if (exp.Operator) {
                              var stack = [];
                              for (var i = 0; i < exp.Nodes.length; i++) {
                                  var node = exp.Nodes[i];
                                  if (exp.Nodes[i].Field) {
                                      stack.push("(" + t.GetFieldText(node.Field) + " " + t.GetTermText(node.Term) + " " + node.Value + ")");
                                  } else  {
                                      var r = t.GetExpText(node);
                                      if (r != '') {
                                          stack.push("(" + t.GetExpText(node) + ")");
                                      }
                                  }
                              }
                              result = stack.join(exp.Operator);
                          }
						  resultDiv.text(result);
                          return result;
                      },
                       //输入框验证方法
                      InputRegExp:function(){
                      	var flg=TE;
                      	$('.value',This).each(function(){
                      		var t=$(this);
                      		if ($.trim(t.val()) == ''){ expressionTip.text('输入框不能为空'); flg= FE;return FE; }
                      		if ($.trim(t.val()) != '' && !$.regExp($.trim(t.val()))) { expressionTip.text('输入无意义的搜索条件'); flg= FE;return FE; }
                      		if ($.trim(t.val()) != '' && !$.regSpecial($.trim(t.val()))) { expressionTip.text('输入框有非法字符'); flg= FE;return FE; }
                      		if ($.trim(t.val()) != '' && $.filterExp($.trim(t.val()))) { expressionTip.text('搜索内容不允许搜索'); flg= FE;return FE; }
                      		
                      	});
                      	return flg;
                      },
                      //递归画表达式显示Key(后台用)
                      //参数：表达式树形结构
                      GetExpKey: function (exp) {
                          var result = "";
                          if (exp.Operator) {
                              var stack = [];
                              for (var i = 0; i < exp.Nodes.length; i++) {
                                  var node = exp.Nodes[i];
                                  if (exp.Nodes[i].Field) {
                                  	var f = node.Term == 'Contains' ? '' : '-';
                              	  	if(node.Field == "content") {
                                  		if (o.type == "2" || o.type == "4") 
                                    		stack.push(f + "(content:\"" + node.Value + "\")") ;
                                  		else
                                        	stack.push(f + "(content:\"" + node.Value + "\" OR title:\"" + node.Value + "\")") ;
                                  	} else if(node.Field=='content1') {
                          				stack.push(f + "(content:\""+ node.Value +"\" OR content1:\""+ node.Value +"\")") ;
                                  	} else
                                  	 	stack.push(f + "(" + node.Field + ":\"" + node.Value + "\")");
                                       

                                      //node.Field == "content" ?
                                          //(o.type == "2" || o.type == "4") ?
                                          	//stack.push(f + "(content:" + node.Value + ")") :
                                          	//stack.push(f + "(content:" + node.Value + " OR title:" + node.Value + ")") :
                                          //stack.push(f + "(" + node.Field + ":" + node.Value + ")");
                                  }
                                  else {
                                  	var r = expression.GetExpKey(node);
                                      if (r != '') {
                                          stack.push("(" + expression.GetExpKey(node) + ")");
                                      }
                                  }
                              }
                              result = stack.join(" " + exp.Operator + " ");
                          }
                          resultDiv.attr('result', result);
                          return result;
                      },
                      //根据树形结构画默认节点
                      //参数：表达式树形结构
                      DrawNode: function (exp, className) {
                          var html = '';
                          html += $[FO](tpA, exp.Operator, className);
                          for (var i = 0; i < exp.Nodes.length; i++) {
                              var node = exp.Nodes[i];
                              if (node.Field)
                                  html += $[FO](tableChild, selectHtml, node.Value, node.Field, node.Term);
                              else
                                  html += expression.DrawNode(node, 'btnRemoveParent');
                          }
                          html += tpB;
                          return html;
                      },
                      //获取数结构画文本和key
                      GetTreeTxtKey: function () {
                          var t = this;
                          t.PerfixExpress();
                          t.RegExp(expObj);
                          t.GetExpText(expObj);
                          t.GetExpKey(expObj);
                      },
                      //设置个节点操作
                      SetNode: function (obj) {
                          var t = this;
                          t.AddBrothers($('.btnAddBrothers', obj)); //添加兄弟节点按钮
                          t.RemoveNode($('.btnReomveBrothers', obj)); //移除兄弟节点按钮
                          t.AddParent($('.btnAddParent', obj)); //添加父亲节点按钮
                          t.RemoveNode($('.btnRemoveParent', obj)); //移除父亲节点按钮
                      },
                      //设置主节点操作
                      SetMainNode: function (obj) {
                          var t = this;
                          t.AddMain($('.btnMain', obj)); //添加主节点按钮
                          t.RemoveMain($('.btnRemoveMain', obj)); //移除主节点按钮
                      },
                      //设置兄弟节点操作
                      SetBrothers: function (obj) {
                          var t = this;
                          t.SelectChange($('.field', obj)); //类别选择select对象
                          t.SelectChange($('.term', obj)); //符号选择select对象
                          t.Keyup($('.value', obj)); //文本框input对象
                      }
                  };


        //执行函数
        expression.GetSelect();
        expression.DrawDefaultNode();

        return This;
    };
    //获取表达式文本方法
    $.expressionText = function (o, so) {
        function GetExpText(exp) {
            var result = "", t = this;
            if (exp.Operator) {
                var stack = [];
                for (var i = 0; i < exp.Nodes.length; i++) {
                    var node = exp.Nodes[i];
                    if (exp.Nodes[i].Field) {
                        stack.push("(" + GetField(so, node.Field) + " " + (node.Term == 'Contains' ? '包含' : '不包含') + " " + node.Value + ")");
                    } else {
                        stack.push("(" + GetExpText(node) + ")");
                    }
                }
                result = stack.join(exp.Operator);
            }

            //if (resultDiv != undefined) resultDiv.text(result);
            return result;
        }
        function GetField(so, o) {
            var text = '';
            $[EH](so, function (i, item) {
                if (o == item.value) {
                    text = item.text;
                    return FE;
                }
            });
            return text;
        }
        return GetExpText(o);
    };
})(jQuery);
    //return $;
//});