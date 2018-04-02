/*!
* jQCloud Plugin for jQuery
*
* Version 1.0.4
*
* Copyright 2011, Luca Ongaro
* Licensed under the MIT license.
*
* Date: 2013-05-09 18:54:22 +0200
*/

//define(['./jquery'], function ($) {
(function($) {
    $.fn.jQCloud = function (options) {
        // Reference to the container element
        var $this = this,
        	   // Namespace word ids to avoid collisions between multiple clouds
       		   cloud_namespace = $this[AT]('id') || Math.floor((Math.random() * 1000000)).toString(36);

        options = $[EX]({
        	collection:[{ text: "讯库", weight: 13 },
        					  { text: "腾讯", weight: 10.5, html: { "class": "vertical"} }],
            width: $this[WH](),
            height: $this[HT](),
            center: {
                x: ((options && options[WH]) ? options[WH] : $this[WH]()) / 2.0,
                y: ((options && options[HT]) ? options[HT] : $this[HT]()) / 2.0
            },
            delayedMode: options.collection[LN] > 50,
            shape: FE, //rectangular
            encodeURI: TE,
            removeOverflowing: TE,
            isAnimate: TE // 是否开启动画效果
        }, options);
        
        var word_array = options.collection;

        // Add the "jqcloud" class to the container for easy CSS styling, set container width/height
        $this[AC]("jqcloud")[WH](options[WH])[HT](options[HT]);

        // Container's CSS position cannot be 'static'
        if ($this.css("position") === "static") {
            $this.css("position", "relative");
        }
        
        // Helper function to test if an element overlaps others
        //测试如果元素重叠其他辅助功能
        var hitTest = function (elem, other_elems) {
            // Pairwise overlap detection
            // 成对的重叠检测
            var overlapping = function (a, b) {
                if (Math.abs(2.0 * a.offsetLeft + a.offsetWidth - 2.0 * b.offsetLeft - b.offsetWidth) < a.offsetWidth + b.offsetWidth) {
                    if (Math.abs(2.0 * a.offsetTop + a.offsetHeight - 2.0 * b.offsetTop - b.offsetHeight) < a.offsetHeight + b.offsetHeight) {
                        return TE;
                    }
                }
                return FE;
            };
            var i = 0;
            // Check elements for overlap one by one, stop and return FE as soon as an overlap is found
            // 检查重叠的元素，停止和假尽快返回重叠被发现
            for (i = 0; i < other_elems.length; i++) {
                if (overlapping(elem, other_elems[i])) {
                    return TE;
                }
            }
            return FE;
        };

        var drawWordCloud = function () {
            // Make sure every weight is a number before sorting
            for (var i = 0; i < word_array[LN] ; i++) {
                word_array[i].weight = parseFloat(word_array[i].weight, 10);
            }

            // Sort word_array from the word with the highest weight to the one with the lowest
            // 按照weight权重排序
            word_array.sort(function (a, b) {
                if (a.weight < b.weight)
                    return 1;
                else if (a.weight > b.weight)
                    return -1;
                else
                    return 0;
            });

            var step = (options.shape === "rectangular") ? 18.0 : 2.0,
                   already_placed_words = [],
                   aspect_ratio = options[WH] / options[HT];

            // Function to draw a word, by moving it in spiral until it finds a suitable empty place. This will be iterated on each word.
            var drawOneWord = function (index, word) {
                // Define the ID attribute of the span that will wrap the word, and the associated jQuery selector string
                var word_id = cloud_namespace + "_word_" + index,
                       word_selector = "#" + word_id,
                       angle = 6.28 * Math.random(),
                       radius = 0.0,

                	   // Only used if option.shape == 'rectangular'
                       steps_in_direction = 0.0,
                       quarter_turns = 0.0,

                       weight = 5,
                       custom_class = "",
                       inner_html = "",
                       word_span;

                // Extend word html options with defaults
                // 给每个数组项追加ID属性
                word.html = $[EX](word.html, { id: word_id });

                // If custom class was specified, put them into a variable and remove it from html attrs, to avoid overwriting classes set by jQCloud
                // 如果指定了自定义类，把它们放进一个变量，将其从HTML属性，以避免覆盖类的jqcloud
                // 提出自定义class，删除对象里的class属性
                var styleClass = 'class';
                if (word.html && word.html[styleClass]) {
                    custom_class = word.html[styleClass];
                    delete word.html[styleClass];
                }


                // Check if min(weight) > max(weight) otherwise use default
                if (word_array[0].weight > word_array[word_array[LN] - 1].weight) {
                    // Linearly map the original weight to a discrete scale from 1 to 10
                    weight = Math.round((word.weight - word_array[word_array[LN]  - 1].weight) /
                              (word_array[0].weight - word_array[word_array[LN]  - 1].weight) * 9.0) + 1;
                }
                word_span = $('<span>')[AT](word.html)[AC]('w' + weight + " " + custom_class);

                // Append link if word.url attribute was set
                //添加链接云词
                if (word.link) {
                    // If link is a string, then use it as the link href
                    if (typeof word.link === "string") {
                        word.link = { href: word.link };
                    }

                    // Extend link html options with defaults
                    if (options.encodeURI) {
                        word.link = $[EX](word.link, { href: encodeURI(word.link.href)[RP](/'/g, "%27") });
                    }

                    inner_html = $('<a>')[AT](word.link).text(word.text);
                    //添加普通云词
                } else {
                    inner_html = word.text;
                }
                word_span[AP](inner_html);

                // Bind handlers to words
                //绑定用户定义的事件
                var handlers = word.handlers;
                if (!!handlers) {
                    for (var prop in handlers) {
                        var fn = handlers[prop];
                        //如果prop属于json对象里的key并且key对应的value是一个函数
                        if (handlers.hasOwnProperty(prop) && typeof fn === 'function') {
                            $(word_span)[EV](prop, fn);
                        }
                    }
                }

                $this[AP](word_span);

                var width = word_span[WH](),
                       height = word_span[HT](),
                       left = options.center.x - width / 2.0,
                       top = options.center.y - height / 2.0;

                // Save a reference to the style property, for better performance
                var word_style = word_span[0].style;
                word_style.position = "absolute";
                word_style.left = left + "px";
                word_style.top = top + "px";

                while (hitTest(word_span[0], already_placed_words)) {
                    // option shape is 'rectangular' so move the word in a rectangular spiral
                    //矩形算法
                    if (options.shape === "rectangular") {
                        steps_in_direction++;
                        if (steps_in_direction * step > (1 + Math.floor(quarter_turns / 2.0)) * step * ((quarter_turns % 4 % 2) === 0 ? 1 : aspect_ratio)) {
                            steps_in_direction = 0.0;
                            quarter_turns++;
                        }
                        switch (quarter_turns % 4) {
                            case 1:
                                left += step * aspect_ratio + Math.random() * 2.0;
                                break;
                            case 2:
                                top -= step + Math.random() * 2.0;
                                break;
                            case 3:
                                left -= step * aspect_ratio + Math.random() * 2.0;
                                break;
                            case 0:
                                top += step + Math.random() * 2.0;
                                break;
                        }
                        // Default settings: elliptic spiral shape
                        //圆形算法
                    } else { 
                        radius += step;
                        angle += (index % 2 === 0 ? 1 : -1) * step;

                        left = options.center.x - (width / 2.0) + (radius * Math.cos(angle)) * aspect_ratio;
                        top = options.center.y + radius * Math.sin(angle) - (height / 2.0);
                    }
                    word_style.left = left + "px";
                    word_style.top = top + "px";
                }

                // Don't render word if part of it would be outside the container
                if (options.removeOverflowing && (left < 0 || top < 0 || (left + width) > options[WH] || (top + height) > options[HT])) {
                    word_span[RM]()
                    return;
                }


                already_placed_words.push(word_span[0]);

                // Invoke callback if existing
                if ($.isFunction(word.afterWordRender)) {
                    word.afterWordRender.call(word_span);
                }
            };

            var drawOneWordDelayed = function (index) {
                index = index || 0;
                if (!$this.is(':visible')) { // if not visible then do not attempt to draw
                    setTimeout(function () { drawOneWordDelayed(index); }, 10);
                    return;
                }
                if (index < word_array[LN] ) {
                    drawOneWord(index, word_array[index]);
                    setTimeout(function () { drawOneWordDelayed(index + 1); }, 10);
                } else {
                    if ($.isFunction(options.afterCloudRender)) {
                        options.afterCloudRender.call($this);
                    }
                }
            };

            // Iterate drawOneWord on every word. The way the iteration is done depends on the drawing mode (delayedMode is TE or FE)
            if (options.delayedMode) {
                drawOneWordDelayed();
            }
            else {
                $[EH](word_array, drawOneWord);
                if ($.isFunction(options.afterCloudRender)) {
                    options.afterCloudRender.call($this);
                }
            }
        };

        // Delay execution so that the browser can render the page before the computatively intensive word cloud drawing
        //延迟执行使浏览器可以使网页在computatively密集的文字云图
        setTimeout(function () { 
        	drawWordCloud(); 
        	if (options.isAnimate && $.fn.circulate) {
                $this[FD]('span')[EH](function () {
                    $(this).circulate({
                        speed: Math.floor(Math.random() * 1000) + 100,
                        height: Math.floor(Math.random() * 1000) - 470,
                        width: Math.floor(Math.random() * 1000) - 470
                    });
                });
            }
        }, 10);
        return $this;
    };
})(jQuery);
   //return $; 
//});
