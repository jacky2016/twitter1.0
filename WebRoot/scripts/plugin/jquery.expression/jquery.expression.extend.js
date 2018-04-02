(function ($) {
    //注意先加载jquery.expression文件
    //优化结构方法，dataStructure:数据结构
    $.fn.expression.optimized = function (dataStructure) {
        var tree = {};
        /*
        * 优化节点方法
        * param:参数
        * parentNode: 父节点, currentNode: 子节点
        */
        function optimized(parentNode, currentNode) {
            //上移追加操作
            $[EH](currentNode, function (i, nodeRow) {
                //先递归到最深层
                if (nodeRow.Nodes && nodeRow.Nodes[LN] > 0) {
                    optimized(nodeRow, nodeRow.Nodes);
                }
                if (parentNode != null) {
                    //1.如果当前节点只有一个叶子，上移
                    if (nodeRow.Nodes != UN && nodeRow.Nodes[LN] == 1) {
                        //当前节点的子节点追加到父节点的子节点
                        parentNode.Nodes.push(nodeRow.Nodes[0]);
                        delete currentNode[i];
                    }
                    //2.如果子节点有多个叶子，并且子节点操作符和父节点操作符相同，上移
                    if (nodeRow.Nodes != UN && nodeRow.Nodes[LN] > 1 && nodeRow.Operator == parentNode.Operator) {
                        //批量追加
                        var j = 0, count = nodeRow.Nodes[LN];
                        for (j; j < count; j++) {
                            parentNode.Nodes.push(nodeRow.Nodes[j]);
                        }
                        delete currentNode[i];
                    }
                }
            });
        }
        /*
        * 过滤undefined节点入口方法
        * param:参数
        * dataStructure: 数据结构
        */
        function filterRoot(dataStructure) {
            tree = { Operator: dataStructure.Operator, Nodes: [] };
            filterFor(dataStructure.Nodes, tree.Nodes);
        }
        /*
        * 递归过滤undefined节点方法
        * param:参数
        * dataNodes: 数据结构子节点, treeNodes: 新的树结构子节点
        */
        function filterFor(dataNodes, treeNodes) {
            var index = 0;
            $[EH](dataNodes, function (i, nodeRow) {
                if (nodeRow != UN) {
                    if (nodeRow.Nodes && nodeRow.Nodes[LN] > 0) {
                        treeNodes.push({ Operator: nodeRow.Operator, Nodes: [] });
                        filterFor(nodeRow.Nodes, treeNodes[index].Nodes);
                    } else {
                        treeNodes.push({ Field: nodeRow.Field, Term: nodeRow.Term, Value: nodeRow.Value });
                    }
                    index++;
                }
            });
        }
        optimized(dataStructure, dataStructure.Nodes);
        filterRoot(dataStructure);
        dataStructure = tree;
        //清除地址引用, 便于释放内存资源
        tree = null;
        return dataStructure;
    };

    //拆分词方法
    $.fn.expression.participle = function (dataStructure) {
        /*
        * 拆分词方法
        * param:参数
        * parentNode: 父节点, currentNode: 子节点
        */
        function participle(parentNode, currentNode) {
            var arrayWordNode = [],
                  arrayWordTmp = []; //词数组的临时变量（用于返回给上层函数的数组）

            $[EH](currentNode, function (i, nodeRow) {
                //先递归到最深层
                if (parentNode != null) {

                    //如果有子节点 递归查找词数组
                    if (nodeRow.Nodes && nodeRow.Nodes[LN] > 0) {
                        arrayWordNode = participle(nodeRow, nodeRow.Nodes);
                        //否则如果没有子节点，并且子节点词类型是包含关系的，设置长度为1的词数组
                    } else if (nodeRow.Term == 'Contains') {
                        arrayWordNode = [nodeRow.Value];
                    }
                    //根据根节点关系计算 词关系
                    if (parentNode.Operator == 'AND') {
                        //$.alertParam('AND', arrayWordTmp, arrayWordNode);
                        if (arrayWordTmp[0] != arrayWordNode[0]) {
                            arrayWordTmp = andToOr(arrayWordTmp, arrayWordNode);
                        }
                        //$.alertParam('ANDResult', arrayWordTmp, arrayWordNode);
                    } else {
                        //$.alertParam('OR', arrayWordTmp, arrayWordNode);
                        orToAnd(arrayWordTmp, arrayWordNode);
                        //$.alertParam('ORResult', arrayWordTmp, arrayWordNode);
                    }

                }
            });
            return arrayWordTmp;
        }
        /*
        * array关系分析方法and->or->node
        *                    and
        *                   /      \        
        *                or         or
        *               /    \      /   \
        *             a       b c      d
        * 结果: ['ac', 'ad', 'bc', 'bd']
        * param:参数
        * array1: 数组1, array2: 数组2
        */
        function andToOr(array1, array2) {
            var array = [];
            if (array1[LN] == 0) {
                for (var j in array2) {
                    array.push($[FO]('{0}', array2[j]));
                }
            } else {
                for (var i in array1) {
                    for (var j in array2) {
                        array.push($[FO]('{0}-{1}', array1[i], array2[j]));
                    }
                }
            }
            return array;
        }
        /*
        * array关系分析方法or->and->node
        *                     or
        *                   /     \
        *             and        and
        *            /      \      /     \
        *           a       b   c      d
        * 结果: ['ab', 'cd']
        * param:参数
        * array1:数组1, array2: 数组2
        */
        function orToAnd(array1, array2) {
            for (var index in array2) {
                if (!hasValue(array1, array2[index])) {
                    array1.push(array2[index]);
                }
            }
        }
        /*
        * 判断数组是否存在该项
        * param:参数
        * array: 数组, value: 值
        */
        function hasValue(array, value) {
            var i = 0, count = array[LN];
            for (i; i < count; i++) {
                if (array[i] == value) {
                    return TE;
                }
            }
            return FE;
        }
        //init
        return (function (dataStructure) {
            var arrayWord = participle(dataStructure, dataStructure.Nodes),
                  word = arrayWord.join(',');
            return word;
        })(dataStructure);
    };
})(jQuery);