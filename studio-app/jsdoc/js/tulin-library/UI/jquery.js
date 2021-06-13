var _jQuery = function(selector){return new _jQuery();};
_jQuery.prototype.init = function(selector, context) {return new _jQuery();};
_jQuery.prototype.selector = "";
_jQuery.prototype.length = 0;
_jQuery.prototype.size = function() {return this.length;};
_jQuery.prototype.toArray = function() {return new Array();};

/**
@name get
@description 获取HTMLHtmlElement
@param {number} num
@returns {HTMLHtmlElement}
@example var tselem = $("#tesel").get(0);
 */
_jQuery.prototype.get = function(num) {return new HTMLHtmlElement();};
_jQuery.prototype.pushStack = function(elems, name, selector) {};

/**
@name each
@description 遍历所有对象
@param {function} callback
@param {object} args
@example $("#tesel").each(function(){alert($(this).attr("id"));});
 */
_jQuery.prototype.each = function(callback, args) {};
	
/**
@name first
@description 将匹配元素集合缩减为集合中的第一个元素
@returns {jQuery}
 */
_jQuery.prototype.first = function() {return new _jQuery();};	

/**
@name last
@description 将匹配元素集合缩减为集合中的最后一个元素
@returns {jQuery}
 */
_jQuery.prototype.last = function() {return new _jQuery();};

/**
@name eq
@description 将匹配元素集合缩减为位于指定索引的新元素
@param {number} i
@returns {jQuery}
 */
_jQuery.prototype.eq = function(i) {};	

/**
@name filter
@description 将匹配元素集合缩减为匹配选择器或匹配函数返回值的新元素
@param {string} str
@returns {jQuery}
 */
_jQuery.prototype.filter = function(str) {};

/**
@name not
@description 将匹配元素集合缩减为匹配选择器或匹配函数返回值的新元素
@param {string} str
@returns {jQuery}
 */
_jQuery.prototype.not = function(str) {};		
	
/**
@name slice
@description 将匹配元素集合缩减为指定范围的子集
@returns {jQuery}
 */
_jQuery.prototype.slice = function() {};	

/**
@name map
@description 把当前匹配集合中的每个元素传递给函数，产生包含返回值的新 jQuery 对象
@param {function} callback
@returns {jQuery}
 */
_jQuery.prototype.map = function(callback) {return new _jQuery();};		

/**
@name end
@description 结束当前链中最近的一次筛选操作，并将匹配元素集合返回到前一次的状态
@returns {jQuery}
 */
_jQuery.prototype.end = function() {};	

/**
@name push
@description 方法可向数组的末尾添加一个或多个元素，并返回新的长度
@param {Array} arr
@returns {number}
 */
_jQuery.prototype.push = function(arr){};

/**
@name html
@description 设置或返回所选元素的内容（包括 HTML 标记）
@param {string} html
@returns {string}
 */
_jQuery.prototype.html = function(html){if(!html)return "";};

/**
@name text
@description 设置或返回所选元素的文本内容
@param {string} str
@returns {string}
 */
_jQuery.prototype.text = function(str){if(!str)return "";};

/**
@name val
@description 设置或返回表单字段的值
@param {string} str
@returns {string}
 */
_jQuery.prototype.val = function(str){if(!str)return "";};

/**
@name attr
@description 设置或返回对象的属性
@param {string} atr
@param {string} val
@returns {string}
 */
_jQuery.prototype.attr = function(atr,val){if(!val)return "";};

/**
@name removeAttr
@description 删除对象的属性
@param {string} atr -属性名称
@returns {jQuery}
 */
_jQuery.prototype.removeAttr = function(atr){return new _jQuery();};

/**
@name addClass
@description 添加CSS样式
@param {string} Class -CSS class
@returns {jQuery}
 */
_jQuery.prototype.addClass = function(Class){return new _jQuery();};

/**
@name removeClass
@description 移除CSS样式
@param {string} Class -CSS class
@returns {jQuery}
 */
_jQuery.prototype.removeClass = function(Class){return new _jQuery();};

/**
@name toggleClass
@description 对设置或移除被选元素的一个或多个类进行切换
@param {string} Class -CSS class
@param {boolean} switch
@returns {jQuery}
@example $(selector).toggleClass(class,switch)
 */
_jQuery.prototype.toggleClass = function(Class){return new _jQuery();};

/**
@name css
@description 方法返回或设置匹配的元素的一个或多个样式属性
@param {string} name
@param {string} value
@returns {jQuery}
@example  $("p").css("color","red");
 */
_jQuery.prototype.css = function(name,value){return new _jQuery();};

/**
@name width
@description 方法返回或设置匹配的元素的宽度
@param {number} width
@returns {number}
@example  $("p").width(800);
 */
_jQuery.prototype.width = function(width){return 0;};

/**
@name height
@description 方法返回或设置匹配的元素的高度
@param {number} height
@returns {number}
@example  $("p").height(100);
 */
_jQuery.prototype.height = function(height){return 0;};

/**
@name innerWidth
@description 方法返回第一个匹配元素的内部宽度
@returns {number}
@example var nwidth = $("p").innerWidth();
 */
_jQuery.prototype.innerWidth = function(){return 0;};

/**
@name innerHeight
@description 方法返回第一个匹配元素的内部高度
@returns {number}
@example var nwidth = $("p").innerHeight();
 */
_jQuery.prototype.innerHeight = function(){return 0;};

/**
@name outerWidth
@description 方法返回第一个匹配元素的外部宽度
@param {string} options
@returns {number}
@example var nwidth = $("p").outerWidth();
 */
_jQuery.prototype.outerWidth = function(options){return 0;};

/**
@name outerHeight
@description 方法返回第一个匹配元素的外部高度
@param {string} options
@returns {number}
@example var nwidth = $("p").outerHeight();
 */
_jQuery.prototype.outerHeight = function(options){return 0;};

/**
@name parent
@description 方法返回被选元素的直接父元素
@returns {jQuery}
@example $("p").parent();
 */
_jQuery.prototype.parent = function(){return new _jQuery();};

/**
@name parents
@description 方法返回被选元素的所有祖先元素，它一路向上直到文档的根元素 (<html>)
@param {string} expr
@returns {jQuery}
@example $("p").parents();
 */
_jQuery.prototype.parents = function(expr){return new _jQuery();};

/**
@name parentsUntil
@description 方法返回介于两个给定元素之间的所有祖先元素
@param {string} expr
@returns {jQuery}
@example $("p").parentsUntil();
 */
_jQuery.prototype.parentsUntil = function(expr){return new _jQuery();};

/**
@name children
@description 方法返回被选元素的所有直接子元素
@param {string} expr
@returns {jQuery}
@example $("p").children();
 */
_jQuery.prototype.children = function(expr){return new _jQuery();};

/**
@name find
@description 方方法返回被选元素的后代元素，一路向下直到最后一个后代
@param {string} expr
@returns {jQuery}
@example $("p").find("a");
 */
_jQuery.prototype.find = function(expr){return new _jQuery();};

/**
@name siblings
@description 方法返回被选元素的所有同胞元素
@param {string} expr
@param {string} context
@returns {jQuery}
@example $("p").siblings("a");
 */
_jQuery.prototype.siblings = function(expr,context){return new _jQuery();};

/**
@name next
@description 方法返回被选元素的下一个同胞元素
@param {string} expr
@returns {jQuery}
@example $("p").next();
 */
_jQuery.prototype.next = function(expr){return new _jQuery();};

/**
@name nextAll
@description 方法返回被选元素的所有跟随的同胞元素
@param {string} expr
@returns {jQuery}
@example $("p").nextAll();
 */
_jQuery.prototype.nextAll = function(expr){return new _jQuery();};

/**
@name nextUntil
@description 方法返回介于两个给定参数之间的所有跟随的同胞元素
@param {string} expr
@returns {jQuery}
@example $("p").nextUntil();
 */
_jQuery.prototype.nextUntil = function(expr){return new _jQuery();};

/**
@name prev
@description 方法返回前面的同胞元素
@param {string} expr
@returns {jQuery}
@example $("p").prev();
 */
_jQuery.prototype.prev = function(expr){return new _jQuery();};

/**
@name prevAll
@description 方法返回前面的同胞元素
@param {string} expr
@returns {jQuery}
@example $("p").prevAll();
 */
_jQuery.prototype.prevAll = function(expr){return new _jQuery();};

/**
@name prevUntil
@description 方法返回前面的同胞元素
@param {string} expr
@returns {jQuery}
@example $("p").prevUntil();
 */
_jQuery.prototype.prevUntil = function(expr){return new _jQuery();};

/**
@name add
@description 方法将元素添加到匹配元素的集合中
@param {string} expr
@param {string} context
@returns {jQuery}
@example $("p").add("div");
 */
_jQuery.prototype.add = function(expr,context){return new _jQuery();};

/**
@name contents
@description 方法获得匹配元素集合中每个元素的子节点，包括文本和注释节点
@returns {jQuery}
@example $("p").contents();
 */
_jQuery.prototype.contents = function(){return new _jQuery();};

/**
@name show
@description 显示HTML元素
@param {number} speed
@param {function} callback
@returns {jQuery}
@example $("p").show();
 */
_jQuery.prototype.show = function(speed,callback){return new _jQuery();};

/**
@name hide
@description 隐藏HTML元素
@param {number} speed
@param {function} callback
@returns {jQuery}
@example $("p").hide();
 */
_jQuery.prototype.hide = function(speed,callback){return new _jQuery();};

/**
@name toggle
@description 切换显示/隐藏HTML元素
@returns {jQuery}
@example $("p").toggle();
 */
_jQuery.prototype.toggle = function(){return new _jQuery();};

/**
@name slideDown
@description 方法用于向下滑动元素
@param {number} speed
@param {function} callback
@description 可选的 speed 参数规定效果的时长。它可以取以下值："slow"、"fast" 或毫秒。可选的 callback 参数是滑动完成后所执行的函数名称。
@returns {jQuery}
@example $("p").slideDown();
 */
_jQuery.prototype.slideDown = function(speed,calback){return new _jQuery();};

/**
@name slideUp
@description 方法用于向上滑动元素
@param {number} speed
@param {function} callback
@description 可选的 speed 参数规定效果的时长。它可以取以下值："slow"、"fast" 或毫秒。可选的 callback 参数是滑动完成后所执行的函数名称。
@returns {jQuery}
@example $("p").slideUp();
 */
_jQuery.prototype.slideUp = function(speed,calback){return new _jQuery();};

/**
@name slideToggle
@description 方法可以在 slideDown() 与 slideUp() 方法之间进行切换
@param {number} speed
@param {function} callback
@description 可选的 speed 参数规定效果的时长。它可以取以下值："slow"、"fast" 或毫秒。可选的 callback 参数是滑动完成后所执行的函数名称。
@returns {jQuery}
@example $("p").slideToggle();
 */
_jQuery.prototype.slideToggle = function(speed,calback){return new _jQuery();};

/**
@name fadeIn
@description 用于淡入已隐藏的元素
@param {number} speed
@param {function} callback
@description 可选的 speed 参数规定效果的时长。它可以取以下值："slow"、"fast" 或毫秒。可选的 callback 参数是滑动完成后所执行的函数名称。
@returns {jQuery}
@example $("p").fadeIn();
 */
_jQuery.prototype.fadeIn = function(speed,calback){return new _jQuery();};

/**
@name fadeOut
@description 方法用于淡出可见元素
@param {number} speed
@param {function} callback
@description 可选的 speed 参数规定效果的时长。它可以取以下值："slow"、"fast" 或毫秒。可选的 callback 参数是滑动完成后所执行的函数名称。
@returns {jQuery}
@example $("p").fadeOut();
 */
_jQuery.prototype.fadeOut = function(speed,calback){return new _jQuery();};

/**
@name fadeTo
@description 方法允许渐变为给定的不透明度（值介于 0 与 1 之间）
@param {number} speed
@param {function} callback
@description 可选的 speed 参数规定效果的时长。它可以取以下值："slow"、"fast" 或毫秒。可选的 callback 参数是滑动完成后所执行的函数名称。
@returns {jQuery}
@example $("p").fadeTo();
 */
_jQuery.prototype.fadeTo = function(speed,opacity,fn){return new _jQuery();};

/**
@name fadeToggle
@description 方法可以在 fadeIn() 与 fadeOut() 方法之间进行切换
@param {number} speed
@param {function} callback
@description 可选的 speed 参数规定效果的时长。它可以取以下值："slow"、"fast" 或毫秒。可选的 callback 参数是滑动完成后所执行的函数名称。
@returns {jQuery}
@example $("p").fadeToggle();
 */
_jQuery.prototype.fadeToggle = function(speed){return new _jQuery();};

/**
@name animate
@description 方法用于创建自定义动画
@param {string} params
@param {number} speed
@param {function} callback
@returns {jQuery}
@description 必需的 params 参数定义形成动画的 CSS 属性。
可选的 speed 参数规定效果的时长。它可以取以下值："slow"、"fast" 或毫秒。
可选的 callback 参数是动画完成后所执行的函数名称。
@returns {jQuery}
@example $("button").click(function(){
  $("div").animate({left:'250px'});
}); 
 */
_jQuery.prototype.animate = function(params,speed,callback){return new _jQuery();};

/**
@name stop
@description 法用于在动画或效果完成前对它们进行停止
@param {boolean} stopAll
@param {boolean} goToEnd
@returns {jQuery}
@description 可选的 stopAll 参数规定是否应该清除动画队列。默认是 false，即仅停止活动的动画，允许任何排入队列的动画向后执行。
可选的 goToEnd 参数规定是否立即完成当前动画。默认是 false。
 */
_jQuery.prototype.stop = function(stopAll,goToEnd){return new _jQuery();};

/**
@name after
@description 方法在被选元素之后插入内容
@param {string} content
@returns {jQuery}
@example $("img").after("Some text after");
 */
_jQuery.prototype.after = function(content){return new _jQuery();};

/**
@name before
@description 方法在被选元素之前插入内容
@param {string} content
@returns {jQuery}
@example $("img").before("Some text after");
 */
_jQuery.prototype.before = function(content){return new _jQuery();};

/**
@name insertAfter
@description 方法在被选元素之后插入 HTML 标记或已有的元素
@param {string} content
@returns {jQuery}
@example $("<span>Hello world!</span>").insertAfter("p");
 */
_jQuery.prototype.insertAfter = function(content){return new _jQuery();};

/**
@name insertBefore
@description 方法在被选元素之前插入 HTML 标记或已有的元素
@param {string} content
@returns {jQuery}
@example $("<span>Hello world!</span>").insertBefore("p");
 */
_jQuery.prototype.insertBefore = function(content){return new _jQuery();};

/**
@name append
@description 方法在被选元素的结尾插入内容
@param {string} content
@returns {jQuery}
@example $("p").append("Some appended text.");
 */
_jQuery.prototype.append = function(content){return new _jQuery();};

/**
@name prepend
@description 方法在被选元素的开头插入内容
@param {string} content
@returns {jQuery}
@example $("p").prepend("Some appended text.");
 */
_jQuery.prototype.prepend = function(content){return new _jQuery();};

/**
@name appendTo
@description 方法在被选元素的结尾（仍然在内部）插入指定内容
@param {string} content
@returns {jQuery}
@example $("<b>Hello World!</b>").appendTo("p");
 */
_jQuery.prototype.appendTo = function(content){return new _jQuery();};

/**
@name remove
@description 方法移除被选元素，包括所有文本和子节点
@param {string} expr
@returns {jQuery}
@example $("p").remove();
 */
_jQuery.prototype.remove = function(expr){return new _jQuery();};

/**
@name empty
@description 方法从被选元素移除所有内容，包括所有文本和子节点
@returns {jQuery}
@example $("p").empty();
 */
_jQuery.prototype.empty = function(){return new _jQuery();};

/**
@name ready
@description 当 DOM（文档对象模型） 已经加载，并且页面（包括图像）已经完全呈现时，会发生 ready 事件
@param {function} fn
@returns {jQuery}
@example $(document).ready(fn);
 */
_jQuery.prototype.ready = function(fn) {return new _jQuery();};	

/**
@name click
@description 将函数绑定到指定元素的 click 事件
@param {function} fn
@returns {jQuery}
@example $("p").click(fn);
 */
_jQuery.prototype.click = function(fn){return new _jQuery();};

/**
@name dblclick
@description 将函数绑定到指定元素的 dbclick 事件
@param {function} fn
@returns {jQuery}
@example $("p").dblclick(fn);
 */
_jQuery.prototype.dblclick = function(fn){return new _jQuery();};

/**
@name blur
@description 方法将函数绑定到指定元素的blur事件,当元素失去焦点时发生blur事件。 
@description blur()函数触发blur事件，或者如果设置了 function 参数，该函数也可规定当发生 blur 事件时执行的代码。
@param {function} fn -可选。规定当 blur 事件发生时运行的函数。
@returns {jQuery}
@example $(selector).blur();
@example $("input").blur(fn);
 */
_jQuery.prototype.blur = function(fn){return new _jQuery();};

/**
@name change
@description 将函数绑定到指定元素的 change 事件
@param {function} fn
@returns {jQuery}
@example $("input").change(fn);
 */
_jQuery.prototype.change = function(fn){return new _jQuery();};

/**
@name delegate
@description 方法为指定的元素（属于被选元素的子元素）添加一个或多个事件处理程序，并规定当这些事件发生时运行的函数。
@param {string} childSelector -必需。规定要附加事件处理程序的一个或多个子元素。
@param {string} event -必需。规定附加到元素的一个或多个事件。
@param {string} data -可选。规定传递到函数的额外数据。
@param {function} fn -必需。规定当事件发生时运行的函数。
@returns {jQuery}
@example $(selector).delegate(childSelector,event,data,fn)
 */
_jQuery.prototype.delegate = function(childSelector,event,data,fn){return new _jQuery();};

/**
@name undelegate
@description 方法删除由 delegate() 方法添加的一个或多个事件处理程序。
@param {string} selector -可选。规定需要删除事件处理程序的选择器。
@param {string} event -可选。规定需要删除处理函数的一个或多个事件类型。
@param {function} fn -可选。规定要删除的具体事件处理函数。
@returns {jQuery}
@example $(selector).delegate(selector,event,fn)
 */
_jQuery.prototype.undelegate = function(selector,event,fn){return new _jQuery();};

/**
@name die
@description 方法移除所有通过 live() 方法向指定元素添加的一个或多个事件处理程序
@param {function} ev -必需。规定要移除的一个或多个事件处理程序。
@param {function} fn -可选。规定要移除的特定函数。
@returns {jQuery}
@example $(selector).die(event,function);
 */
_jQuery.prototype.die = function(ev,fn){return new _jQuery();};

/**
@name error
@description 方法触发 error 事件，或规定当发生 error 事件时运行的函数。
@param {function} fn -可选。规定当发生 error 事件时运行的函数。
@returns {jQuery}
@example $(selector).error();
@example $(selector).error(function);
*/
_jQuery.prototype.error = function(fn){return new _jQuery();};

/**
@name focus
@description 当元素获得焦点时，发生 focus 事件。
*/
_jQuery.prototype.focus = function(fn){return new _jQuery();};
_jQuery.prototype.keydown = function(fn){return new _jQuery();};
_jQuery.prototype.keypress = function(fn){return new _jQuery();};
_jQuery.prototype.keyup = function(fn){return new _jQuery();};
_jQuery.prototype.live = function(fn){return new _jQuery();};

/**
@name load
@description 方法从服务器加载数据，并把返回的数据放入被选元素中
@param {string} url -参数规定您希望加载的 URL
@param {object} data -参数规定与请求一同发送的查询字符串键/值对集合
@param {function} callback -参数是 load() 方法完成后所执行的函数名称
@returns {jQuery}
@example $(selector).load(URL,data,callback);
@example $("#div1").load("demo_test.txt",function(responseTxt,statusTxt,xhr){
    if(statusTxt=="success")
      alert("外部内容加载成功！");
    if(statusTxt=="error")
      alert("Error: "+xhr.status+": "+xhr.statusText);
  });
 */
_jQuery.prototype.load = function(url,data,callback){return new _jQuery();};

/**
@name unload
@description 方法将事件处理程序绑定到 unload 事件. 当用户离开页面时，会发生 unload 事件
@param {function} fn -必需。规定当触发 unload 事件时运行的函数。
@returns {jQuery}
@example $(window).unload(function(){
  alert("Goodbye!");
});
*/
_jQuery.prototype.unload = function(fn){return new _jQuery();};

_jQuery.prototype.mousedown = function(fn){return new _jQuery();};
_jQuery.prototype.mouseenter = function(fn){return new _jQuery();};
_jQuery.prototype.mouseleave = function(fn){return new _jQuery();};
_jQuery.prototype.mousemove = function(fn){return new _jQuery();};
_jQuery.prototype.mouseout = function(fn){return new _jQuery();};
_jQuery.prototype.mouseover = function(fn){return new _jQuery();};
_jQuery.prototype.mouseup = function(fn){return new _jQuery();};

/**
@name one
@description 方法为被选元素附加一个或多个事件处理程序，并规定当事件发生时运行的函数。当使用 one() 方法时，每个元素只能运行一次事件处理器函数。
@param {string} event -必需。规定添加到元素的一个或多个事件。
@param {object} data -可选。规定传递到函数的额外数据。
@param {function} fn -必需。规定当事件发生时运行的函数。
@returns {jQuery}
@example $(selector).one(event,data,fn);
*/
_jQuery.prototype.one = function(event,data,fn){return new _jQuery();};
_jQuery.prototype.resize = function(fn){return new _jQuery();};
_jQuery.prototype.scroll = function(fn){return new _jQuery();};

/**
@name select
@description 当 textarea 或文本类型的 input 元素中的文本被选择时，会发生 select 事件。
@param {function} fn -可选。规定当发生 select 事件时运行的函数。
@returns {jQuery}
@example $(selector).select();
@example $(selector).select(function);
*/
_jQuery.prototype.select = function(fn){return new _jQuery();};

/**
@name submit
@description 方法触发 submit 事件，或规定当发生 submit 事件时运行的函数。
@param {function} fn -可选。规定当发生 submit 事件时运行的函数。
@returns {jQuery}
@example $(selector).submit();
@example $(selector).submit(function);
*/
_jQuery.prototype.submit = function(fn){return new _jQuery();};

/**
@name toggle
@description 方法用于绑定两个或多个事件处理器函数，以响应被选元素的轮流的 click 事件。
@param {function} function1 -必需。规定当元素在每偶数次被点击时要运行的函数。
@param {function} function2 -必需。规定当元素在每奇数次被点击时要运行的函数。
@param {function} functionN -可选。规定需要切换的其他函数。
@returns {jQuery}
@example $(selector).toggle(function1(),function2(),functionN(),...);
*/
_jQuery.prototype.toggle = function(function1,function2,functionN){return new _jQuery();};

/**
@name trigger
@description 方法触发被选元素的指定事件类型
@param {string} event -必需。规定指定元素要触发的事件。可以使自定义事件（使用 bind() 函数来附加），或者任何标准事件。
@param {object} param1 -可选。传递到事件处理程序的额外参数。
@returns {jQuery}
@example $(selector).toggle(event,[param1,param2,...]);
*/
_jQuery.prototype.trigger = function(event,param1){return new _jQuery();};

/**
@name triggerHandler
@description 方法触发被选元素的指定事件类型。但不会执行浏览器默认动作，也不会产生事件冒泡。
@param {string} event -必需。规定指定元素要触发的事件。
@param {object} param1 -可选。传递到事件处理程序的额外参数。
@returns {jQuery}
@example $(selector).triggerHandler(event,[param1,param2,...]);
*/
_jQuery.prototype.triggerHandler = function(fn){return new _jQuery();};

/**
@name scrollLeft
@description 方法返回或设置匹配元素的滚动条的水平位置
@param {string} offse
@returns {jQuery}
@example $("div").scrollLeft(100);
 */
_jQuery.prototype.scrollLeft = function(offse){return new _jQuery();};

/**
@name scrollTop
@description 设置 <div> 元素中滚动条的垂直偏移
@param {string} offse
@returns {jQuery}
@example $("div").scrollTop(100);
 */
_jQuery.prototype.scrollTop = function(offse){return new _jQuery();};

/**
@name bind
@description 将函数绑定到指定元素的事件
@param {string} eventname
@param {function} fn
@returns {jQuery}
@example $("input").bind("click",fn);
 */
_jQuery.prototype.bind = function(eventname,fn){return new _jQuery();};

/**
@name unbind
@description 解除绑定的指定元素的事件
@param {string} eventname
@param {function} fn
@returns {jQuery}
@example $("input").unbind("click");
 */
_jQuery.prototype.unbind = function(eventname,fn){return new _jQuery();};

/**
@name sort
@description 数组排序
 */
_jQuery.prototype.sort =  [].sort;

/**
@name splice
@description 方法向/从数组中添加/删除项目，然后返回被删除的项目
@param {number} index -必需。整数，规定添加/删除项目的位置，使用负数可从数组结尾处规定位置。
@param {number} howmany -必需。要删除的项目数量。如果设置为 0，则不会删除项目。
@param item1 -可选。向数组添加的新项目。
@returns {Array}
@example arrayObject.splice(index,howmany,item1,.....,itemX);
 */
_jQuery.prototype.splice =  function(index,howmany,item1){return new Array();};


/**
@name inArray
@description 确定第一个参数在数组中的位置，从0开始计数(如果没有找到则返回 -1 )
@param {string} value
@param {Array} array
@returns {number}
@example $.inArray("js", arr); 
 */
_jQuery.prototype.inArray =  function(value, array){return 0;};

/**
@name clone
@description 方法生成被选元素的副本，包含子节点、文本和属性
@returns {jQuery}
@example $("body").append($("p").clone());
 */
_jQuery.prototype.clone =  function(){return new _jQuery();};

/**
@name ajax
@description 方法通过 HTTP 请求加载远程数据
@param {object} setting
@example $.ajax({async:true|false, type:"post"|"get", url: "test.html", dataType:"xml"|"html"|"json"|"script"|"jsonp", error: function(XMLHttpRequest, errorText){}, success: function(returnType){}});
 */
_jQuery.prototype.ajax = function(setting){};

/**
@name extend
@description 函数用于将一个或多个对象的内容合并到目标对象
@param {object} obj1
@param {object} obj2
@returns {jQuery}
@example $.extend(obj1, obj2); //object2合并到object1中
 */
_jQuery.prototype.extend = function(obj1,obj2){return new _jQuery();};

_jQuery.prototype.support = function(){};

_jQuery.prototype.fn = new _jQuery();

var jQuery = new _jQuery();

var $ = function(selector){return new _jQuery();};