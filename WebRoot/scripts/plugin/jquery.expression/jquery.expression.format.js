(function () {

    var extend = function (target, source, overwrite) {
        if (overwrite === undefined)
            overwrite = true;
        for (var p in source) {
            if (overwrite || !(p in target))
                target[p] = source[p];
        }
        return target;
    };

    var Express = function (f, t, v, t1) {
        this.Field = f;
        this.Term = t;
        this.Value = v;
        this.type = t1;
        this.toString = function () {
            return this.Field + "" + this.Term + "" + this.Value;
        };

        this.comprise = function (exp) {
            return this.Value == exp.Value && this.Term == exp.Term;
        };
    };

    var Node = function (op, values) {
        this.op = op;
        this.values = values;
    };
    String.prototype.trimStart = function () {
        if (this.length == 0)
            return this;
        var c = ' ';
        var i = 0;
        for (; this.charAt(i) == c && i < this.length; i++)
            ;
        return this.substring(i);
    };
    String.prototype.trimEnd = function () {
        c = ' ';
        var i = this.length - 1;
        for (; i >= 0 && this.charAt(i) == c; i--)
            ;
        return this.substring(0, i + 1);
    };
    String.prototype.trim = function () {
        return this.trimStart().trimEnd();
    };
    var QueryFormat = function (source) {
        this.setVars(source);
        this.initialize();
        this._normalizeTokens();
    };
    var _findSpec = function (item, items) {
        var result = false;
        for (var i = 0; i < items.length; i++) {
            if (item == items[i]) {
                result = true;
                break;
            }
        }
        return result;
    };
    extend(QueryFormat.prototype, {
        setVars: function (s) {
            this.indentStr = '';
            this.source = s;
            this.lastWord = '';
            this.lastType = 'TK_ST';
            this.lastText = '';
            this.preLastText = '';
            this.tokens = [];
            this.whitespace = ' \n\r\t '.split('');
            this.operator = '( ) AND OR - :'.split(' ');
            this.keywords = 'content,title,content1,_content'.split(',');
            this.special = ['+', '-', '&', '|', '!', '(', ')', '{', '}', '[', ']', '^', '"', '~', '*', '?', ':', '\\', ',', '<', '>'];
            this.tokenStore = [];
            this.parsePos = 0;
            this.sourceLength = s.length;
            this.token = {};
            this.toHtml = '';
            this.nTokens = [];
        },
        isWord: function (c) {
            var result = /\w/.test(c) || /[\u4e00-\u9fa5]/.test(c) || c == "."
        	|| c == "," || c == "$" || c == "%";
            return result;
        },
        isInArray: function (c, arr) {
            for (var i = 0; i < arr.length; i++) {
                if (c === arr[i])
                    return true;
            }
            return false;
        },
        getToken: function () {
            if (this.parsePos > this.sourceLength) {
                return ['', 'TK_EOF'];
            }
            var c = this.source.charAt(this.parsePos);
            this.parsePos++;

            this.blanklinesN = 0;

            if (c === '(') {
                return [c, 'TK_ST'];
            }
            if (c === ')') {
                return [c, 'TK_ED'];
            }
            if (this.isInArray(c, this.operator)) {
                return this._operatorToken(c);
            }
            if (c === '"' || c === "'") {
                return this._stringToken(c);
            }
            if (this.isInArray(c, this.whitespace)) {
                var ret = this._normalWhitespace(c);
                if (ret !== undefined && ret instanceof Array) {
                    return ret;
                }
            }
            // word
            if (this.isWord(c, this.wordchar)) {
                return this._wordToken(c);
            }
            return [c, 'TK_UNKNOWN'];
        },
        _keywordToken: function () {
            while (this.parsePos < this.sourceLength
					&& this.isInArray(c + this.source.charAt(this.parsePos), this.keywords)) {
                c += this.source.charAt(this.parsePos);
                this.parsePos++;
            }

            return [c, 'TK_KW'];
        },
        _operatorToken: function (c) {
            while (this.parsePos < this.sourceLength
					&& this.isInArray(c + this.source.charAt(this.parsePos), this.operator)) {
                c += this.source.charAt(this.parsePos);
                this.parsePos++;
            }

            return [c, 'TK_OP'];
        },
        _normalWhitespace: function (c) {
            while (this.isInArray(this.source.charAt(this.parsePos), this.whitespace)) {
                c += this.source.charAt(this.parsePos);
                if (this.parsePos > this.sourceLength) {
                    return ['', 'TK_EOF'];
                }

                this.parsePos++;
            }
            return [c, 'TK_WS'];
        },
        _wordToken: function (c) {
            if (this.parsePos < this.sourceLength) {
                while (this.isWord(this.source.charAt(this.parsePos), this.wordchar)) {
                    c += this.source.charAt(this.parsePos);
                    this.parsePos++;
                    if (this.parsePos === this.sourceLength)
                        break;
                }
            }
            return [c, 'TK_WD'];
        },
        _stringToken: function (c) {
            var escape = false, ret = c;
            if (this.parsePos < this.sourceLength) {
                while (escape || this.source.charAt(this.parsePos) !== c) {
                    var curChar = this.source.charAt(this.parsePos);
                    ret += curChar;

                    if (!escape) {
                        if (curChar === '\\')
                            escape = true;
                    } else
                        escape = false;

                    this.parsePos++;
                    if (this.parsePos >= this.sourceLength) {
                        return [ret, 'TK_STRING'];
                    }
                }
            }

            this.parsePos++;
            ret += c;

            return [ret, 'TK_STRING'];
        },
        _fixWord: function (token, newType) {
            var lastToken = this.tokens.pop();
            if (lastToken.type == "TK_WD") {
                this.tokens.push({
                    type: "TK_WD",
                    value: lastToken.value + token[0]
                });
            } else {
                this.tokens.push(lastToken);
                this.tokens.push({
                    type: newType ? newType : token[1],
                    value: token[0]
                });
            }
        },
        initialize: function () {
            while (true) {
                var tk = this.getToken();
                if (tk[1] !== 'TK_WS') {
                    this.token.text = tk[0];
                    this.token.type = tk[1];
                    if (tk[1] === 'TK_EOF') {
                        break;
                    }
                    this.preLastText = this.lastText;
                    this.preLastType = this.lastType;
                    this.lastText = this.token.text;
                    this.lastType = this.token.type;
                }
                this.scan(tk);
            }
        },
        scan: function (tk) {
            if (this.isInArray(tk[0], this.keywords)) {
                this.tokens.push({
                    type: "TK_KW",
                    value: tk[0]
                });
            } else if (tk[0] == "AND" || tk[0] == "OR") {
                this.tokens.push({
                    type: "TK_OP",
                    value: tk[0]
                });
            } else if (tk[1] == "TK_STRING" || tk[1] == "TK_WS" || tk[1] == "TK_WD") {
                this._fixWord(tk, "TK_WD");
            } else {
                this.tokens.push({
                    type: tk[1],
                    value: tk[0]
                });
            }
        },
        _normalizeTokens: function () {
            this.nTokens.length = 0;
            for (var i = 0; i < this.tokens.length; i++) {
                var val = this.tokens[i].value.trim();
                if (val != "" && val != "\n" && this.tokens[i].type != "TK_UNKNOWN") {
                    switch (this.tokens[i].type) {
                        case "TK_OP":
                            {
                                break;
                            }
                        case "TK_EX":
                            {
                                break;
                            }
                        case "TK_ST":
                            {
                            }
                        case "TK_ED":
                            {
                            }
                    }
                    this.nTokens.push({
                        type: this.tokens[i].type,
                        value: val,
                        toString: function () {
                            return this.value;
                        }
                    });
                }
            }
        },
        _nomalizeInfix: function (infix) {
            //var infix = this._toInfix();
            var nomalizeInfix = [];
            for (var i = 0; i < infix.length; i++) {
                var el = infix[i];
                if (el.type == "TK_ED") {
                    var v1 = nomalizeInfix.pop();
                    var op = nomalizeInfix.pop();
                    // var v2 = nomalizeInfix.pop();
                    if (v1.type == "TK_EX" && op.type == "TK_ST") {
                        // 两个括号不要了
                        nomalizeInfix.push(v1);
                    } else if (v1.type == "TK_EX" && op.value == "OR") {
                        // 再弹出一个检查是不是和v1相同，如果相同则合并
                        // 只压入v1和两个括号
                        var p = nomalizeInfix.pop();
                        if (p.type == "TK_EX" && p.comprise(v1)) {
                            nomalizeInfix.pop(); // 弹出最近的括号
                            nomalizeInfix.push(p);
                        } else {
                            // 还原栈
                            nomalizeInfix.push(p);
                            nomalizeInfix.push(op);
                            nomalizeInfix.push(v1);
                            nomalizeInfix.push(el);
                        }
                    } else {
                        // 还原栈
                        nomalizeInfix.push(op);
                        nomalizeInfix.push(v1);
                        nomalizeInfix.push(el);
                    }
                } else {
                    nomalizeInfix.push(el);
                }
            }
            return nomalizeInfix;
        },
        outputExpressionTree: function () {
            var tree = this.outputTree();
            if (tree.values) {
                var obj = { Operator: tree.op, Nodes: tree.values };
                var obj = convert(obj);
                var obj = convertNode(obj);
            } else {
                var obj = { Operator: "AND", Nodes: [tree] };
                var obj = convertNode(obj);
            }
            //转换成expression专用结构
            function convertNode(obj) {
                for (var i = 0; i < obj.Nodes.length; i++) {
                    var node = obj.Nodes[i];
                    if (node.Field)
                        node.Term = node.Term == ':' ? 'Contains' : 'NOT';
                    else
                        convertNode(node);
                }
                return obj;
            }

            //转换树结构
            function convert(obj) {
                for (var i = 0; i < obj.Nodes.length; i++) {
                    if (obj.Nodes[i].op) {
                        obj.Nodes[i].Operator = obj.Nodes[i].op;
                        delete obj.Nodes[i].op;
                    }
                    if (obj.Nodes[i].values) {
                        obj.Nodes[i].Nodes = obj.Nodes[i].values;
                        delete obj.Nodes[i].values;
                    }
                    $.each(obj.Nodes, function (i, item) {
                        delete item.type;
                        delete item.toString;
                        delete item.comprise;
                    });
                    if (obj.Nodes[i].Nodes && obj.Nodes[i].Nodes.length) {
                        convert(obj.Nodes[i]);
                    }
                }
                return obj;
            }
            return obj;
        },
        checkbrace: function (infix) {
            // 消除两边多余的括号
            while (infix[0].value == "(") {
                if (infix[0].value == "(" && infix[infix.length - 1].value == ")") {
                    infix.splice(0, 1);
                    infix.splice(infix.length - 1, 1);
                }
                //i++;
            }
        },
        compressOR: function (list) {
            var result;
            if (list[2].Field == "content" && list[0].Field == "title") {
                result = list[2];
            }
            else if (list[0].Field == "content" && list[2].Field == "title") {
                result = list[0];
            }
            else if (list[0].Field == "content" && list[2].Field == "content1") {
                result = list[2];
            }
            else if (list[0].Field == "content1" && list[2].Field == "content") {
                result = list[0];
            } 
            else if (list[0].comprise(list[2])) {
                result = list[0];
            }
            return result;
        },
        _outputTree: function () {
            var stack = [];
            var infix = this._toInfix();
            //var infix = this._nomalizeInfix(infix1);
            for (var i = 0; i < infix.length; i++) {
                var el = infix[i];
                if (el.type == "TK_ST") {
                    stack.push(el);
                } else if (el.type == "TK_EX") {
                    var top = stack[stack.length - 1];

                    if (top && top.op) {
                        top.values.push(el);
                    }
                    else {
                        stack.push(el);
                    }
                } else if (el.type == "TK_OP") {
                    stack.push(el);

                } else if (el.type == "TK_ED") {
                    var temp = [];
                    while (true) {
                        var a = stack.pop();
                        if (!a) break;
                        if (a.type == "TK_ST") {
                            break;
                        } else {
                            temp.push(a);
                        }
                    }
                    if (temp.length == 1) {
                        stack.push(temp[0]);
                    }
                    else if (temp.length == 3 && temp[1].value == "OR" && temp[0].Value && temp[2].Value &&
                        temp[0].Value == temp[2].Value && temp[0].Term == temp[0].Term) {
                        stack.push(this.compressOR(temp));
                    }
                    else {
                        var stack1 = [];
                        for (var j = 0; j < temp.length; j++) {
                            var t1 = temp[j];
                            if (t1.type != "TK_OP") {
                                if (j == temp.length - 1) {
                                    stack1[0].values.push(t1);
                                }
                                else {
                                    stack1.push(t1);
                                }
                            }
                            else {
                                var v1 = stack1.pop();
                                var v2 = stack1.pop();
                                if (v2 && v2.op) {
                                    if (v2.op == t1.value) {
                                        v2.values.push(v1);
                                        stack1.push(v2);
                                    }
                                    else {
                                        stack1.push(new Node(t1.value, [v2, v1]));
                                    }
                                }
                                else {
                                    stack1.push(new Node(t1.value, [v1]));
                                }
                            }
                        }
                        stack.push(stack1[0]);
                    }
                }
            }
            //alert($.toJson(stack));
            while (stack.length != 1) {
                var v1 = stack.pop();
                var op = stack.pop();
                var v2 = stack.pop();
                if (op.type == "TK_OP") {
                    if (v1.op == op.value) {
                        v1.values.push(v2);
                        stack.push(v1);
                    } else {
                        stack.push(new Node(op.value, [v2, v1]));
                    }
                }
            }
            return stack[0];
        },
        destoryDump: function (index, value, values) {
            if (values && values.length) {
                for (var i = 0; i < values.length; i++) {
                    if (value.comprise(values[i]) && index != i) {
                        if (value.Field == "content") {
                            values.splice(i, 1); //remove title
                        } else if (value.Field == "title" && values[i].Field == "content") {
                            values.splice(index, 1); //remove title
                        }
                    }
                }
            }
        },
        compressTree: function (tree) {
            if (tree && tree.op) {
                for (var i = 0; i < tree.values.length; i++) {
                    if (tree.values[i].op) {
                        this.compressTree(tree.values[i]);
                    }
                    else {
                        this.destoryDump(i, tree.values[i], tree.values);
                    }
                }
            }
        },
        outputTree: function () {
            var tree = this._outputTree();
            this.compressTree(tree);
            return tree;
        },
        _hasNot: function (stack) {
            for (var i = 0; i < stack.length; i++) {
                if (stack[i] == "-") {
                    return true;
                }
            }
            return false;
        },
        tree2String: function (tree) {
            var result = "";
            if (tree && tree.op) {
                result += tree.op + "(";
                for (var i = 0; i < tree.values.length; i++) {
                    if (tree.values[i].op) {
                        result += this.tree2String(tree.values[i]);
                    }
                    else {
                        result += tree.values[i].Field + tree.values[i].Term + tree.values[i].Value;
                    }
                }
                result += ")";
            }
            else {
                result += tree.Field + tree.Term + tree.Value;
            }
            return result;
        },
        _toInfix: function () {
            var chars = [];
            var notStack = [];
            for (var i = 0; i < this.nTokens.length; i++) {
                if (this.nTokens[i].type == "TK_ST" || this.nTokens[i].value == "-") {
                    notStack.push(this.nTokens[i]);
                    if (this.nTokens[i].value != "-") {
                        chars.push(this.nTokens[i]);
                    }
                } else if (this.nTokens[i].type == "TK_ED") {
                    chars.push(this.nTokens[i]);
                    for (; ; ) {
                        if (notStack.pop().value == "(") {
                            break;
                        }
                    }
                    if (notStack.length != 0) {
                        if (notStack[notStack.length - 1].value == "-") {
                            notStack.pop();
                        }
                    }
                } else if (this.nTokens[i].type == "TK_KW") {
                    // 处理not操作符
                    if (notStack.length == 1 && notStack[0].value == "-") {
                        chars.push(new Express(this.nTokens[i].value, "!" + this.nTokens[++i].value,
								this.nTokens[++i].value, "TK_EX"));
                        notStack.pop();
                    } else if (this._hasNot(notStack)) {
                        chars.push(new Express(this.nTokens[i].value, "!" + this.nTokens[++i].value,
								this.nTokens[++i].value, "TK_EX"));
                        if (notStack.length != 0 && notStack[notStack.length - 1] == "-") {
                            // 如果栈顶是-则弹出，否则不弹出，说明这个not是应用于一个括号，括号的在上面处理了
                            notStack.pop();
                        }
                    } else {
                        chars.push(new Express(this.nTokens[i].value, this.nTokens[++i].value, this.nTokens[++i].value,
								"TK_EX"));
                    }
                } else {
                    chars.push(this.nTokens[i]);
                }
            }
            return chars;
        }
    });

    this.QueryFormat = QueryFormat;
})();