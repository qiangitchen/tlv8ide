(function(window,undefined){var
readyList,rootjQuery,core_strundefined=typeof undefined,document=window.document,location=window.location,_jQuery=window.jQuery,_$=window.$,class2type={},core_deletedIds=[],core_version="1.9.1",core_concat=core_deletedIds.concat,core_push=core_deletedIds.push,core_slice=core_deletedIds.slice,core_indexOf=core_deletedIds.indexOf,core_toString=class2type.toString,core_hasOwn=class2type.hasOwnProperty,core_trim=core_version.trim,jQuery=function(selector,context){return new jQuery.fn.init(selector,context,rootjQuery);},core_pnum=/[+-]?(?:\d*\.|)\d+(?:[eE][+-]?\d+|)/.source,core_rnotwhite=/\S+/g,rtrim=/^[\s\uFEFF\xA0]+|[\s\uFEFF\xA0]+$/g,rquickExpr=/^(?:(<[\w\W]+>)[^>]*|#([\w-]*))$/,rsingleTag=/^<(\w+)\s*\/?>(?:<\/\1>|)$/,rvalidchars=/^[\],:{}\s]*$/,rvalidbraces=/(?:^|:|,)(?:\s*\[)+/g,rvalidescape=/\\(?:["\\\/bfnrt]|u[\da-fA-F]{4})/g,rvalidtokens=/"[^"\\\r\n]*"|true|false|null|-?(?:\d+\.|)\d+(?:[eE][+-]?\d+|)/g,rmsPrefix=/^-ms-/,rdashAlpha=/-([\da-z])/gi,fcamelCase=function(all,letter){return letter.toUpperCase();},completed=function(event){if(document.addEventListener||event.type==="load"||document.readyState==="complete"){detach();jQuery.ready();}},detach=function(){if(document.addEventListener){document.removeEventListener("DOMContentLoaded",completed,false);window.removeEventListener("load",completed,false);}else{document.detachEvent("onreadystatechange",completed);window.detachEvent("onload",completed);}};jQuery.fn=jQuery.prototype={jquery:core_version,constructor:jQuery,init:function(selector,context,rootjQuery){var match,elem;if(!selector){return this;}
if(typeof selector==="string"){if(selector.charAt(0)==="<"&&selector.charAt(selector.length-1)===">"&&selector.length>=3){match=[null,selector,null];}else{match=rquickExpr.exec(selector);}
if(match&&(match[1]||!context)){if(match[1]){context=context instanceof jQuery?context[0]:context;jQuery.merge(this,jQuery.parseHTML(match[1],context&&context.nodeType?context.ownerDocument||context:document,true));if(rsingleTag.test(match[1])&&jQuery.isPlainObject(context)){for(match in context){if(jQuery.isFunction(this[match])){this[match](context[match]);}else{this.attr(match,context[match]);}}}
return this;}else{elem=document.getElementById(match[2]);if(elem&&elem.parentNode){if(elem.id!==match[2]){return rootjQuery.find(selector);}
this.length=1;this[0]=elem;}
this.context=document;this.selector=selector;return this;}}else if(!context||context.jquery){return(context||rootjQuery).find(selector);}else{return this.constructor(context).find(selector);}}else if(selector.nodeType){this.context=this[0]=selector;this.length=1;return this;}else if(jQuery.isFunction(selector)){return rootjQuery.ready(selector);}
if(selector.selector!==undefined){this.selector=selector.selector;this.context=selector.context;}
return jQuery.makeArray(selector,this);},selector:"",length:0,size:function(){return this.length;},toArray:function(){return core_slice.call(this);},get:function(num){return num==null?this.toArray():(num<0?this[this.length+num]:this[num]);},pushStack:function(elems){var ret=jQuery.merge(this.constructor(),elems);ret.prevObject=this;ret.context=this.context;return ret;},each:function(callback,args){return jQuery.each(this,callback,args);},ready:function(fn){jQuery.ready.promise().done(fn);return this;},slice:function(){return this.pushStack(core_slice.apply(this,arguments));},first:function(){return this.eq(0);},last:function(){return this.eq(-1);},eq:function(i){var len=this.length,j=+i+(i<0?len:0);return this.pushStack(j>=0&&j<len?[this[j]]:[]);},map:function(callback){return this.pushStack(jQuery.map(this,function(elem,i){return callback.call(elem,i,elem);}));},end:function(){return this.prevObject||this.constructor(null);},push:core_push,sort:[].sort,splice:[].splice};jQuery.fn.init.prototype=jQuery.fn;jQuery.extend=jQuery.fn.extend=function(){var src,copyIsArray,copy,name,options,clone,target=arguments[0]||{},i=1,length=arguments.length,deep=false;if(typeof target==="boolean"){deep=target;target=arguments[1]||{};i=2;}
if(typeof target!=="object"&&!jQuery.isFunction(target)){target={};}
if(length===i){target=this;--i;}
for(;i<length;i++){if((options=arguments[i])!=null){for(name in options){src=target[name];copy=options[name];if(target===copy){continue;}
if(deep&&copy&&(jQuery.isPlainObject(copy)||(copyIsArray=jQuery.isArray(copy)))){if(copyIsArray){copyIsArray=false;clone=src&&jQuery.isArray(src)?src:[];}else{clone=src&&jQuery.isPlainObject(src)?src:{};}
target[name]=jQuery.extend(deep,clone,copy);}else if(copy!==undefined){target[name]=copy;}}}}
return target;};jQuery.extend({noConflict:function(deep){if(window.$===jQuery){window.$=_$;}
if(deep&&window.jQuery===jQuery){window.jQuery=_jQuery;}
return jQuery;},isReady:false,readyWait:1,holdReady:function(hold){if(hold){jQuery.readyWait++;}else{jQuery.ready(true);}},ready:function(wait){if(wait===true?--jQuery.readyWait:jQuery.isReady){return;}
if(!document.body){return setTimeout(jQuery.ready);}
jQuery.isReady=true;if(wait!==true&&--jQuery.readyWait>0){return;}
readyList.resolveWith(document,[jQuery]);if(jQuery.fn.trigger){jQuery(document).trigger("ready").off("ready");}},isFunction:function(obj){return jQuery.type(obj)==="function";},isArray:Array.isArray||function(obj){return jQuery.type(obj)==="array";},isWindow:function(obj){return obj!=null&&obj==obj.window;},isNumeric:function(obj){return!isNaN(parseFloat(obj))&&isFinite(obj);},type:function(obj){if(obj==null){return String(obj);}
return typeof obj==="object"||typeof obj==="function"?class2type[core_toString.call(obj)]||"object":typeof obj;},isPlainObject:function(obj){if(!obj||jQuery.type(obj)!=="object"||obj.nodeType||jQuery.isWindow(obj)){return false;}
try{if(obj.constructor&&!core_hasOwn.call(obj,"constructor")&&!core_hasOwn.call(obj.constructor.prototype,"isPrototypeOf")){return false;}}catch(e){return false;}
var key;for(key in obj){}
return key===undefined||core_hasOwn.call(obj,key);},isEmptyObject:function(obj){var name;for(name in obj){return false;}
return true;},error:function(msg){throw new Error(msg);},parseHTML:function(data,context,keepScripts){if(!data||typeof data!=="string"){return null;}
if(typeof context==="boolean"){keepScripts=context;context=false;}
context=context||document;var parsed=rsingleTag.exec(data),scripts=!keepScripts&&[];if(parsed){return[context.createElement(parsed[1])];}
parsed=jQuery.buildFragment([data],context,scripts);if(scripts){jQuery(scripts).remove();}
return jQuery.merge([],parsed.childNodes);},parseJSON:function(data){if(window.JSON&&window.JSON.parse){return window.JSON.parse(data);}
if(data===null){return data;}
if(typeof data==="string"){data=jQuery.trim(data);if(data){if(rvalidchars.test(data.replace(rvalidescape,"@").replace(rvalidtokens,"]").replace(rvalidbraces,""))){return(new Function("return "+data))();}}}
jQuery.error("Invalid JSON: "+data);},parseXML:function(data){var xml,tmp;if(!data||typeof data!=="string"){return null;}
try{if(window.DOMParser){tmp=new DOMParser();xml=tmp.parseFromString(data,"text/xml");}else{xml=new ActiveXObject("Microsoft.XMLDOM");xml.async="false";xml.loadXML(data);}}catch(e){xml=undefined;}
if(!xml||!xml.documentElement||xml.getElementsByTagName("parsererror").length){jQuery.error("Invalid XML: "+data);}
return xml;},noop:function(){},globalEval:function(data){if(data&&jQuery.trim(data)){(window.execScript||function(data){window["eval"].call(window,data);})(data);}},camelCase:function(string){return string.replace(rmsPrefix,"ms-").replace(rdashAlpha,fcamelCase);},nodeName:function(elem,name){return elem.nodeName&&elem.nodeName.toLowerCase()===name.toLowerCase();},each:function(obj,callback,args){var value,i=0,length=obj.length,isArray=isArraylike(obj);if(args){if(isArray){for(;i<length;i++){value=callback.apply(obj[i],args);if(value===false){break;}}}else{for(i in obj){value=callback.apply(obj[i],args);if(value===false){break;}}}}else{if(isArray){for(;i<length;i++){value=callback.call(obj[i],i,obj[i]);if(value===false){break;}}}else{for(i in obj){value=callback.call(obj[i],i,obj[i]);if(value===false){break;}}}}
return obj;},trim:core_trim&&!core_trim.call("\uFEFF\xA0")?function(text){return text==null?"":core_trim.call(text);}:function(text){return text==null?"":(text+"").replace(rtrim,"");},makeArray:function(arr,results){var ret=results||[];if(arr!=null){if(isArraylike(Object(arr))){jQuery.merge(ret,typeof arr==="string"?[arr]:arr);}else{core_push.call(ret,arr);}}
return ret;},inArray:function(elem,arr,i){var len;if(arr){if(core_indexOf){return core_indexOf.call(arr,elem,i);}
len=arr.length;i=i?i<0?Math.max(0,len+i):i:0;for(;i<len;i++){if(i in arr&&arr[i]===elem){return i;}}}
return-1;},merge:function(first,second){var l=second.length,i=first.length,j=0;if(typeof l==="number"){for(;j<l;j++){first[i++]=second[j];}}else{while(second[j]!==undefined){first[i++]=second[j++];}}
first.length=i;return first;},grep:function(elems,callback,inv){var retVal,ret=[],i=0,length=elems.length;inv=!!inv;for(;i<length;i++){retVal=!!callback(elems[i],i);if(inv!==retVal){ret.push(elems[i]);}}
return ret;},map:function(elems,callback,arg){var value,i=0,length=elems.length,isArray=isArraylike(elems),ret=[];if(isArray){for(;i<length;i++){value=callback(elems[i],i,arg);if(value!=null){ret[ret.length]=value;}}}else{for(i in elems){value=callback(elems[i],i,arg);if(value!=null){ret[ret.length]=value;}}}
return core_concat.apply([],ret);},guid:1,proxy:function(fn,context){var args,proxy,tmp;if(typeof context==="string"){tmp=fn[context];context=fn;fn=tmp;}
if(!jQuery.isFunction(fn)){return undefined;}
args=core_slice.call(arguments,2);proxy=function(){return fn.apply(context||this,args.concat(core_slice.call(arguments)));};proxy.guid=fn.guid=fn.guid||jQuery.guid++;return proxy;},access:function(elems,fn,key,value,chainable,emptyGet,raw){var i=0,length=elems.length,bulk=key==null;if(jQuery.type(key)==="object"){chainable=true;for(i in key){jQuery.access(elems,fn,i,key[i],true,emptyGet,raw);}}else if(value!==undefined){chainable=true;if(!jQuery.isFunction(value)){raw=true;}
if(bulk){if(raw){fn.call(elems,value);fn=null;}else{bulk=fn;fn=function(elem,key,value){return bulk.call(jQuery(elem),value);};}}
if(fn){for(;i<length;i++){fn(elems[i],key,raw?value:value.call(elems[i],i,fn(elems[i],key)));}}}
return chainable?elems:bulk?fn.call(elems):length?fn(elems[0],key):emptyGet;},now:function(){return(new Date()).getTime();}});jQuery.ready.promise=function(obj){if(!readyList){readyList=jQuery.Deferred();if(document.readyState==="complete"){setTimeout(jQuery.ready);}else if(document.addEventListener){document.addEventListener("DOMContentLoaded",completed,false);window.addEventListener("load",completed,false);}else{document.attachEvent("onreadystatechange",completed);window.attachEvent("onload",completed);var top=false;try{top=window.frameElement==null&&document.documentElement;}catch(e){}
if(top&&top.doScroll){(function doScrollCheck(){if(!jQuery.isReady){try{top.doScroll("left");}catch(e){return setTimeout(doScrollCheck,50);}
detach();jQuery.ready();}})();}}}
return readyList.promise(obj);};jQuery.each("Boolean Number String Function Array Date RegExp Object Error".split(" "),function(i,name){class2type["[object "+name+"]"]=name.toLowerCase();});function isArraylike(obj){var length=obj.length,type=jQuery.type(obj);if(jQuery.isWindow(obj)){return false;}
if(obj.nodeType===1&&length){return true;}
return type==="array"||type!=="function"&&(length===0||typeof length==="number"&&length>0&&(length-1)in obj);}
rootjQuery=jQuery(document);var optionsCache={};function createOptions(options){var object=optionsCache[options]={};jQuery.each(options.match(core_rnotwhite)||[],function(_,flag){object[flag]=true;});return object;}
jQuery.Callbacks=function(options){options=typeof options==="string"?(optionsCache[options]||createOptions(options)):jQuery.extend({},options);var
firing,memory,fired,firingLength,firingIndex,firingStart,list=[],stack=!options.once&&[],fire=function(data){memory=options.memory&&data;fired=true;firingIndex=firingStart||0;firingStart=0;firingLength=list.length;firing=true;for(;list&&firingIndex<firingLength;firingIndex++){if(list[firingIndex].apply(data[0],data[1])===false&&options.stopOnFalse){memory=false;break;}}
firing=false;if(list){if(stack){if(stack.length){fire(stack.shift());}}else if(memory){list=[];}else{self.disable();}}},self={add:function(){if(list){var start=list.length;(function add(args){jQuery.each(args,function(_,arg){var type=jQuery.type(arg);if(type==="function"){if(!options.unique||!self.has(arg)){list.push(arg);}}else if(arg&&arg.length&&type!=="string"){add(arg);}});})(arguments);if(firing){firingLength=list.length;}else if(memory){firingStart=start;fire(memory);}}
return this;},remove:function(){if(list){jQuery.each(arguments,function(_,arg){var index;while((index=jQuery.inArray(arg,list,index))>-1){list.splice(index,1);if(firing){if(index<=firingLength){firingLength--;}
if(index<=firingIndex){firingIndex--;}}}});}
return this;},has:function(fn){return fn?jQuery.inArray(fn,list)>-1:!!(list&&list.length);},empty:function(){list=[];return this;},disable:function(){list=stack=memory=undefined;return this;},disabled:function(){return!list;},lock:function(){stack=undefined;if(!memory){self.disable();}
return this;},locked:function(){return!stack;},fireWith:function(context,args){args=args||[];args=[context,args.slice?args.slice():args];if(list&&(!fired||stack)){if(firing){stack.push(args);}else{fire(args);}}
return this;},fire:function(){self.fireWith(this,arguments);return this;},fired:function(){return!!fired;}};return self;};jQuery.extend({Deferred:function(func){var tuples=[["resolve","done",jQuery.Callbacks("once memory"),"resolved"],["reject","fail",jQuery.Callbacks("once memory"),"rejected"],["notify","progress",jQuery.Callbacks("memory")]],state="pending",promise={state:function(){return state;},always:function(){deferred.done(arguments).fail(arguments);return this;},then:function(){var fns=arguments;return jQuery.Deferred(function(newDefer){jQuery.each(tuples,function(i,tuple){var action=tuple[0],fn=jQuery.isFunction(fns[i])&&fns[i];deferred[tuple[1]](function(){var returned=fn&&fn.apply(this,arguments);if(returned&&jQuery.isFunction(returned.promise)){returned.promise().done(newDefer.resolve).fail(newDefer.reject).progress(newDefer.notify);}else{newDefer[action+"With"](this===promise?newDefer.promise():this,fn?[returned]:arguments);}});});fns=null;}).promise();},promise:function(obj){return obj!=null?jQuery.extend(obj,promise):promise;}},deferred={};promise.pipe=promise.then;jQuery.each(tuples,function(i,tuple){var list=tuple[2],stateString=tuple[3];promise[tuple[1]]=list.add;if(stateString){list.add(function(){state=stateString;},tuples[i^1][2].disable,tuples[2][2].lock);}
deferred[tuple[0]]=function(){deferred[tuple[0]+"With"](this===deferred?promise:this,arguments);return this;};deferred[tuple[0]+"With"]=list.fireWith;});promise.promise(deferred);if(func){func.call(deferred,deferred);}
return deferred;},when:function(subordinate){var i=0,resolveValues=core_slice.call(arguments),length=resolveValues.length,remaining=length!==1||(subordinate&&jQuery.isFunction(subordinate.promise))?length:0,deferred=remaining===1?subordinate:jQuery.Deferred(),updateFunc=function(i,contexts,values){return function(value){contexts[i]=this;values[i]=arguments.length>1?core_slice.call(arguments):value;if(values===progressValues){deferred.notifyWith(contexts,values);}else if(!(--remaining)){deferred.resolveWith(contexts,values);}};},progressValues,progressContexts,resolveContexts;if(length>1){progressValues=new Array(length);progressContexts=new Array(length);resolveContexts=new Array(length);for(;i<length;i++){if(resolveValues[i]&&jQuery.isFunction(resolveValues[i].promise)){resolveValues[i].promise().done(updateFunc(i,resolveContexts,resolveValues)).fail(deferred.reject).progress(updateFunc(i,progressContexts,progressValues));}else{--remaining;}}}
if(!remaining){deferred.resolveWith(resolveContexts,resolveValues);}
return deferred.promise();}});jQuery.support=(function(){var support,all,a,input,select,fragment,opt,eventName,isSupported,i,div=document.createElement("div");div.setAttribute("className","t");div.innerHTML="  <link/><table></table><a href='/a'>a</a><input type='checkbox'/>";all=div.getElementsByTagName("*");a=div.getElementsByTagName("a")[0];if(!all||!a||!all.length){return{};}
select=document.createElement("select");opt=select.appendChild(document.createElement("option"));input=div.getElementsByTagName("input")[0];a.style.cssText="top:1px;float:left;opacity:.5";support={getSetAttribute:div.className!=="t",leadingWhitespace:div.firstChild.nodeType===3,tbody:!div.getElementsByTagName("tbody").length,htmlSerialize:!!div.getElementsByTagName("link").length,style:/top/.test(a.getAttribute("style")),hrefNormalized:a.getAttribute("href")==="/a",opacity:/^0.5/.test(a.style.opacity),cssFloat:!!a.style.cssFloat,checkOn:!!input.value,optSelected:opt.selected,enctype:!!document.createElement("form").enctype,html5Clone:document.createElement("nav").cloneNode(true).outerHTML!=="<:nav></:nav>",boxModel:document.compatMode==="CSS1Compat",deleteExpando:true,noCloneEvent:true,inlineBlockNeedsLayout:false,shrinkWrapBlocks:false,reliableMarginRight:true,boxSizingReliable:true,pixelPosition:false};input.checked=true;support.noCloneChecked=input.cloneNode(true).checked;select.disabled=true;support.optDisabled=!opt.disabled;try{delete div.test;}catch(e){support.deleteExpando=false;}
input=document.createElement("input");input.setAttribute("value","");support.input=input.getAttribute("value")==="";input.value="t";input.setAttribute("type","radio");support.radioValue=input.value==="t";input.setAttribute("checked","t");input.setAttribute("name","t");fragment=document.createDocumentFragment();fragment.appendChild(input);support.appendChecked=input.checked;support.checkClone=fragment.cloneNode(true).cloneNode(true).lastChild.checked;if(div.attachEvent){div.attachEvent("onclick",function(){support.noCloneEvent=false;});div.cloneNode(true).click();}
for(i in{submit:true,change:true,focusin:true}){div.setAttribute(eventName="on"+i,"t");support[i+"Bubbles"]=eventName in window||div.attributes[eventName].expando===false;}
div.style.backgroundClip="content-box";div.cloneNode(true).style.backgroundClip="";support.clearCloneStyle=div.style.backgroundClip==="content-box";jQuery(function(){var container,marginDiv,tds,divReset="padding:0;margin:0;border:0;display:block;box-sizing:content-box;-moz-box-sizing:content-box;-webkit-box-sizing:content-box;",body=document.getElementsByTagName("body")[0];if(!body){return;}
container=document.createElement("div");container.style.cssText="border:0;width:0;height:0;position:absolute;top:0;left:-9999px;margin-top:1px";body.appendChild(container).appendChild(div);div.innerHTML="<table><tr><td></td><td>t</td></tr></table>";tds=div.getElementsByTagName("td");tds[0].style.cssText="padding:0;margin:0;border:0;display:none";isSupported=(tds[0].offsetHeight===0);tds[0].style.display="";tds[1].style.display="none";support.reliableHiddenOffsets=isSupported&&(tds[0].offsetHeight===0);div.innerHTML="";div.style.cssText="box-sizing:border-box;-moz-box-sizing:border-box;-webkit-box-sizing:border-box;padding:1px;border:1px;display:block;width:4px;margin-top:1%;position:absolute;top:1%;";support.boxSizing=(div.offsetWidth===4);support.doesNotIncludeMarginInBodyOffset=(body.offsetTop!==1);if(window.getComputedStyle){support.pixelPosition=(window.getComputedStyle(div,null)||{}).top!=="1%";support.boxSizingReliable=(window.getComputedStyle(div,null)||{width:"4px"}).width==="4px";marginDiv=div.appendChild(document.createElement("div"));marginDiv.style.cssText=div.style.cssText=divReset;marginDiv.style.marginRight=marginDiv.style.width="0";div.style.width="1px";support.reliableMarginRight=!parseFloat((window.getComputedStyle(marginDiv,null)||{}).marginRight);}
if(typeof div.style.zoom!==core_strundefined){div.innerHTML="";div.style.cssText=divReset+"width:1px;padding:1px;display:inline;zoom:1";support.inlineBlockNeedsLayout=(div.offsetWidth===3);div.style.display="block";div.innerHTML="<div></div>";div.firstChild.style.width="5px";support.shrinkWrapBlocks=(div.offsetWidth!==3);if(support.inlineBlockNeedsLayout){body.style.zoom=1;}}
body.removeChild(container);container=div=tds=marginDiv=null;});all=select=fragment=opt=a=input=null;return support;})();var rbrace=/(?:\{[\s\S]*\}|\[[\s\S]*\])$/,rmultiDash=/([A-Z])/g;function internalData(elem,name,data,pvt){if(!jQuery.acceptData(elem)){return;}
var thisCache,ret,internalKey=jQuery.expando,getByName=typeof name==="string",isNode=elem.nodeType,cache=isNode?jQuery.cache:elem,id=isNode?elem[internalKey]:elem[internalKey]&&internalKey;if((!id||!cache[id]||(!pvt&&!cache[id].data))&&getByName&&data===undefined){return;}
if(!id){if(isNode){elem[internalKey]=id=core_deletedIds.pop()||jQuery.guid++;}else{id=internalKey;}}
if(!cache[id]){cache[id]={};if(!isNode){cache[id].toJSON=jQuery.noop;}}
if(typeof name==="object"||typeof name==="function"){if(pvt){cache[id]=jQuery.extend(cache[id],name);}else{cache[id].data=jQuery.extend(cache[id].data,name);}}
thisCache=cache[id];if(!pvt){if(!thisCache.data){thisCache.data={};}
thisCache=thisCache.data;}
if(data!==undefined){thisCache[jQuery.camelCase(name)]=data;}
if(getByName){ret=thisCache[name];if(ret==null){ret=thisCache[jQuery.camelCase(name)];}}else{ret=thisCache;}
return ret;}
function internalRemoveData(elem,name,pvt){if(!jQuery.acceptData(elem)){return;}
var i,l,thisCache,isNode=elem.nodeType,cache=isNode?jQuery.cache:elem,id=isNode?elem[jQuery.expando]:jQuery.expando;if(!cache[id]){return;}
if(name){thisCache=pvt?cache[id]:cache[id].data;if(thisCache){if(!jQuery.isArray(name)){if(name in thisCache){name=[name];}else{name=jQuery.camelCase(name);if(name in thisCache){name=[name];}else{name=name.split(" ");}}}else{name=name.concat(jQuery.map(name,jQuery.camelCase));}
for(i=0,l=name.length;i<l;i++){delete thisCache[name[i]];}
if(!(pvt?isEmptyDataObject:jQuery.isEmptyObject)(thisCache)){return;}}}
if(!pvt){delete cache[id].data;if(!isEmptyDataObject(cache[id])){return;}}
if(isNode){jQuery.cleanData([elem],true);}else if(jQuery.support.deleteExpando||cache!=cache.window){delete cache[id];}else{cache[id]=null;}}
jQuery.extend({cache:{},expando:"jQuery"+(core_version+Math.random()).replace(/\D/g,""),noData:{"embed":true,"object":"clsid:D27CDB6E-AE6D-11cf-96B8-444553540000","applet":true},hasData:function(elem){elem=elem.nodeType?jQuery.cache[elem[jQuery.expando]]:elem[jQuery.expando];return!!elem&&!isEmptyDataObject(elem);},data:function(elem,name,data){return internalData(elem,name,data);},removeData:function(elem,name){return internalRemoveData(elem,name);},_data:function(elem,name,data){return internalData(elem,name,data,true);},_removeData:function(elem,name){return internalRemoveData(elem,name,true);},acceptData:function(elem){if(elem.nodeType&&elem.nodeType!==1&&elem.nodeType!==9){return false;}
var noData=elem.nodeName&&jQuery.noData[elem.nodeName.toLowerCase()];return!noData||noData!==true&&elem.getAttribute("classid")===noData;}});jQuery.fn.extend({data:function(key,value){var attrs,name,elem=this[0],i=0,data=null;if(key===undefined){if(this.length){data=jQuery.data(elem);if(elem.nodeType===1&&!jQuery._data(elem,"parsedAttrs")){attrs=elem.attributes;for(;i<attrs.length;i++){name=attrs[i].name;if(!name.indexOf("data-")){name=jQuery.camelCase(name.slice(5));dataAttr(elem,name,data[name]);}}
jQuery._data(elem,"parsedAttrs",true);}}
return data;}
if(typeof key==="object"){return this.each(function(){jQuery.data(this,key);});}
return jQuery.access(this,function(value){if(value===undefined){return elem?dataAttr(elem,key,jQuery.data(elem,key)):null;}
this.each(function(){jQuery.data(this,key,value);});},null,value,arguments.length>1,null,true);},removeData:function(key){return this.each(function(){jQuery.removeData(this,key);});}});function dataAttr(elem,key,data){if(data===undefined&&elem.nodeType===1){var name="data-"+key.replace(rmultiDash,"-$1").toLowerCase();data=elem.getAttribute(name);if(typeof data==="string"){try{data=data==="true"?true:data==="false"?false:data==="null"?null:+data+""===data?+data:rbrace.test(data)?jQuery.parseJSON(data):data;}catch(e){}
jQuery.data(elem,key,data);}else{data=undefined;}}
return data;}
function isEmptyDataObject(obj){var name;for(name in obj){if(name==="data"&&jQuery.isEmptyObject(obj[name])){continue;}
if(name!=="toJSON"){return false;}}
return true;}
jQuery.extend({queue:function(elem,type,data){var queue;if(elem){type=(type||"fx")+"queue";queue=jQuery._data(elem,type);if(data){if(!queue||jQuery.isArray(data)){queue=jQuery._data(elem,type,jQuery.makeArray(data));}else{queue.push(data);}}
return queue||[];}},dequeue:function(elem,type){type=type||"fx";var queue=jQuery.queue(elem,type),startLength=queue.length,fn=queue.shift(),hooks=jQuery._queueHooks(elem,type),next=function(){jQuery.dequeue(elem,type);};if(fn==="inprogress"){fn=queue.shift();startLength--;}
hooks.cur=fn;if(fn){if(type==="fx"){queue.unshift("inprogress");}
delete hooks.stop;fn.call(elem,next,hooks);}
if(!startLength&&hooks){hooks.empty.fire();}},_queueHooks:function(elem,type){var key=type+"queueHooks";return jQuery._data(elem,key)||jQuery._data(elem,key,{empty:jQuery.Callbacks("once memory").add(function(){jQuery._removeData(elem,type+"queue");jQuery._removeData(elem,key);})});}});jQuery.fn.extend({queue:function(type,data){var setter=2;if(typeof type!=="string"){data=type;type="fx";setter--;}
if(arguments.length<setter){return jQuery.queue(this[0],type);}
return data===undefined?this:this.each(function(){var queue=jQuery.queue(this,type,data);jQuery._queueHooks(this,type);if(type==="fx"&&queue[0]!=="inprogress"){jQuery.dequeue(this,type);}});},dequeue:function(type){return this.each(function(){jQuery.dequeue(this,type);});},delay:function(time,type){time=jQuery.fx?jQuery.fx.speeds[time]||time:time;type=type||"fx";return this.queue(type,function(next,hooks){var timeout=setTimeout(next,time);hooks.stop=function(){clearTimeout(timeout);};});},clearQueue:function(type){return this.queue(type||"fx",[]);},promise:function(type,obj){var tmp,count=1,defer=jQuery.Deferred(),elements=this,i=this.length,resolve=function(){if(!(--count)){defer.resolveWith(elements,[elements]);}};if(typeof type!=="string"){obj=type;type=undefined;}
type=type||"fx";while(i--){tmp=jQuery._data(elements[i],type+"queueHooks");if(tmp&&tmp.empty){count++;tmp.empty.add(resolve);}}
resolve();return defer.promise(obj);}});var nodeHook,boolHook,rclass=/[\t\r\n]/g,rreturn=/\r/g,rfocusable=/^(?:input|select|textarea|button|object)$/i,rclickable=/^(?:a|area)$/i,rboolean=/^(?:checked|selected|autofocus|autoplay|async|controls|defer|disabled|hidden|loop|multiple|open|readonly|required|scoped)$/i,ruseDefault=/^(?:checked|selected)$/i,getSetAttribute=jQuery.support.getSetAttribute,getSetInput=jQuery.support.input;jQuery.fn.extend({attr:function(name,value){return jQuery.access(this,jQuery.attr,name,value,arguments.length>1);},removeAttr:function(name){return this.each(function(){jQuery.removeAttr(this,name);});},prop:function(name,value){return jQuery.access(this,jQuery.prop,name,value,arguments.length>1);},removeProp:function(name){name=jQuery.propFix[name]||name;return this.each(function(){try{this[name]=undefined;delete this[name];}catch(e){}});},addClass:function(value){var classes,elem,cur,clazz,j,i=0,len=this.length,proceed=typeof value==="string"&&value;if(jQuery.isFunction(value)){return this.each(function(j){jQuery(this).addClass(value.call(this,j,this.className));});}
if(proceed){classes=(value||"").match(core_rnotwhite)||[];for(;i<len;i++){elem=this[i];cur=elem.nodeType===1&&(elem.className?(" "+elem.className+" ").replace(rclass," "):" ");if(cur){j=0;while((clazz=classes[j++])){if(cur.indexOf(" "+clazz+" ")<0){cur+=clazz+" ";}}
elem.className=jQuery.trim(cur);}}}
return this;},removeClass:function(value){var classes,elem,cur,clazz,j,i=0,len=this.length,proceed=arguments.length===0||typeof value==="string"&&value;if(jQuery.isFunction(value)){return this.each(function(j){jQuery(this).removeClass(value.call(this,j,this.className));});}
if(proceed){classes=(value||"").match(core_rnotwhite)||[];for(;i<len;i++){elem=this[i];cur=elem.nodeType===1&&(elem.className?(" "+elem.className+" ").replace(rclass," "):"");if(cur){j=0;while((clazz=classes[j++])){while(cur.indexOf(" "+clazz+" ")>=0){cur=cur.replace(" "+clazz+" "," ");}}
elem.className=value?jQuery.trim(cur):"";}}}
return this;},toggleClass:function(value,stateVal){var type=typeof value,isBool=typeof stateVal==="boolean";if(jQuery.isFunction(value)){return this.each(function(i){jQuery(this).toggleClass(value.call(this,i,this.className,stateVal),stateVal);});}
return this.each(function(){if(type==="string"){var className,i=0,self=jQuery(this),state=stateVal,classNames=value.match(core_rnotwhite)||[];while((className=classNames[i++])){state=isBool?state:!self.hasClass(className);self[state?"addClass":"removeClass"](className);}}else if(type===core_strundefined||type==="boolean"){if(this.className){jQuery._data(this,"__className__",this.className);}
this.className=this.className||value===false?"":jQuery._data(this,"__className__")||"";}});},hasClass:function(selector){var className=" "+selector+" ",i=0,l=this.length;for(;i<l;i++){if(this[i].nodeType===1&&(" "+this[i].className+" ").replace(rclass," ").indexOf(className)>=0){return true;}}
return false;},val:function(value){var ret,hooks,isFunction,elem=this[0];if(!arguments.length){if(elem){hooks=jQuery.valHooks[elem.type]||jQuery.valHooks[elem.nodeName.toLowerCase()];if(hooks&&"get"in hooks&&(ret=hooks.get(elem,"value"))!==undefined){return ret;}
ret=elem.value;return typeof ret==="string"?ret.replace(rreturn,""):ret==null?"":ret;}
return;}
isFunction=jQuery.isFunction(value);return this.each(function(i){var val,self=jQuery(this);if(this.nodeType!==1){return;}
if(isFunction){val=value.call(this,i,self.val());}else{val=value;}
if(val==null){val="";}else if(typeof val==="number"){val+="";}else if(jQuery.isArray(val)){val=jQuery.map(val,function(value){return value==null?"":value+"";});}
hooks=jQuery.valHooks[this.type]||jQuery.valHooks[this.nodeName.toLowerCase()];if(!hooks||!("set"in hooks)||hooks.set(this,val,"value")===undefined){this.value=val;}});}});jQuery.extend({valHooks:{option:{get:function(elem){var val=elem.attributes.value;return!val||val.specified?elem.value:elem.text;}},select:{get:function(elem){var value,option,options=elem.options,index=elem.selectedIndex,one=elem.type==="select-one"||index<0,values=one?null:[],max=one?index+1:options.length,i=index<0?max:one?index:0;for(;i<max;i++){option=options[i];if((option.selected||i===index)&&(jQuery.support.optDisabled?!option.disabled:option.getAttribute("disabled")===null)&&(!option.parentNode.disabled||!jQuery.nodeName(option.parentNode,"optgroup"))){value=jQuery(option).val();if(one){return value;}
values.push(value);}}
return values;},set:function(elem,value){var values=jQuery.makeArray(value);jQuery(elem).find("option").each(function(){this.selected=jQuery.inArray(jQuery(this).val(),values)>=0;});if(!values.length){elem.selectedIndex=-1;}
return values;}}},attr:function(elem,name,value){var hooks,notxml,ret,nType=elem.nodeType;if(!elem||nType===3||nType===8||nType===2){return;}
if(typeof elem.getAttribute===core_strundefined){return jQuery.prop(elem,name,value);}
notxml=nType!==1||!jQuery.isXMLDoc(elem);if(notxml){name=name.toLowerCase();hooks=jQuery.attrHooks[name]||(rboolean.test(name)?boolHook:nodeHook);}
if(value!==undefined){if(value===null){jQuery.removeAttr(elem,name);}else if(hooks&&notxml&&"set"in hooks&&(ret=hooks.set(elem,value,name))!==undefined){return ret;}else{elem.setAttribute(name,value+"");return value;}}else if(hooks&&notxml&&"get"in hooks&&(ret=hooks.get(elem,name))!==null){return ret;}else{if(typeof elem.getAttribute!==core_strundefined){ret=elem.getAttribute(name);}
return ret==null?undefined:ret;}},removeAttr:function(elem,value){var name,propName,i=0,attrNames=value&&value.match(core_rnotwhite);if(attrNames&&elem.nodeType===1){while((name=attrNames[i++])){propName=jQuery.propFix[name]||name;if(rboolean.test(name)){if(!getSetAttribute&&ruseDefault.test(name)){elem[jQuery.camelCase("default-"+name)]=elem[propName]=false;}else{elem[propName]=false;}}else{jQuery.attr(elem,name,"");}
elem.removeAttribute(getSetAttribute?name:propName);}}},attrHooks:{type:{set:function(elem,value){if(!jQuery.support.radioValue&&value==="radio"&&jQuery.nodeName(elem,"input")){var val=elem.value;elem.setAttribute("type",value);if(val){elem.value=val;}
return value;}}}},propFix:{tabindex:"tabIndex",readonly:"readOnly","for":"htmlFor","class":"className",maxlength:"maxLength",cellspacing:"cellSpacing",cellpadding:"cellPadding",rowspan:"rowSpan",colspan:"colSpan",usemap:"useMap",frameborder:"frameBorder",contenteditable:"contentEditable"},prop:function(elem,name,value){var ret,hooks,notxml,nType=elem.nodeType;if(!elem||nType===3||nType===8||nType===2){return;}
notxml=nType!==1||!jQuery.isXMLDoc(elem);if(notxml){name=jQuery.propFix[name]||name;hooks=jQuery.propHooks[name];}
if(value!==undefined){if(hooks&&"set"in hooks&&(ret=hooks.set(elem,value,name))!==undefined){return ret;}else{return(elem[name]=value);}}else{if(hooks&&"get"in hooks&&(ret=hooks.get(elem,name))!==null){return ret;}else{return elem[name];}}},propHooks:{tabIndex:{get:function(elem){var attributeNode=elem.getAttributeNode("tabindex");return attributeNode&&attributeNode.specified?parseInt(attributeNode.value,10):rfocusable.test(elem.nodeName)||rclickable.test(elem.nodeName)&&elem.href?0:undefined;}}}});boolHook={get:function(elem,name){var
prop=jQuery.prop(elem,name),attr=typeof prop==="boolean"&&elem.getAttribute(name),detail=typeof prop==="boolean"?getSetInput&&getSetAttribute?attr!=null:ruseDefault.test(name)?elem[jQuery.camelCase("default-"+name)]:!!attr:elem.getAttributeNode(name);return detail&&detail.value!==false?name.toLowerCase():undefined;},set:function(elem,value,name){if(value===false){jQuery.removeAttr(elem,name);}else if(getSetInput&&getSetAttribute||!ruseDefault.test(name)){elem.setAttribute(!getSetAttribute&&jQuery.propFix[name]||name,name);}else{elem[jQuery.camelCase("default-"+name)]=elem[name]=true;}
return name;}};if(!getSetInput||!getSetAttribute){jQuery.attrHooks.value={get:function(elem,name){var ret=elem.getAttributeNode(name);return jQuery.nodeName(elem,"input")?elem.defaultValue:ret&&ret.specified?ret.value:undefined;},set:function(elem,value,name){if(jQuery.nodeName(elem,"input")){elem.defaultValue=value;}else{return nodeHook&&nodeHook.set(elem,value,name);}}};}
if(!getSetAttribute){nodeHook=jQuery.valHooks.button={get:function(elem,name){var ret=elem.getAttributeNode(name);return ret&&(name==="id"||name==="name"||name==="coords"?ret.value!=="":ret.specified)?ret.value:undefined;},set:function(elem,value,name){var ret=elem.getAttributeNode(name);if(!ret){elem.setAttributeNode((ret=elem.ownerDocument.createAttribute(name)));}
ret.value=value+="";return name==="value"||value===elem.getAttribute(name)?value:undefined;}};jQuery.attrHooks.contenteditable={get:nodeHook.get,set:function(elem,value,name){nodeHook.set(elem,value===""?false:value,name);}};jQuery.each(["width","height"],function(i,name){jQuery.attrHooks[name]=jQuery.extend(jQuery.attrHooks[name],{set:function(elem,value){if(value===""){elem.setAttribute(name,"auto");return value;}}});});}
if(!jQuery.support.hrefNormalized){jQuery.each(["href","src","width","height"],function(i,name){jQuery.attrHooks[name]=jQuery.extend(jQuery.attrHooks[name],{get:function(elem){var ret=elem.getAttribute(name,2);return ret==null?undefined:ret;}});});jQuery.each(["href","src"],function(i,name){jQuery.propHooks[name]={get:function(elem){return elem.getAttribute(name,4);}};});}
if(!jQuery.support.style){jQuery.attrHooks.style={get:function(elem){return elem.style.cssText||undefined;},set:function(elem,value){return(elem.style.cssText=value+"");}};}
if(!jQuery.support.optSelected){jQuery.propHooks.selected=jQuery.extend(jQuery.propHooks.selected,{get:function(elem){var parent=elem.parentNode;if(parent){parent.selectedIndex;if(parent.parentNode){parent.parentNode.selectedIndex;}}
return null;}});}
if(!jQuery.support.enctype){jQuery.propFix.enctype="encoding";}
if(!jQuery.support.checkOn){jQuery.each(["radio","checkbox"],function(){jQuery.valHooks[this]={get:function(elem){return elem.getAttribute("value")===null?"on":elem.value;}};});}
jQuery.each(["radio","checkbox"],function(){jQuery.valHooks[this]=jQuery.extend(jQuery.valHooks[this],{set:function(elem,value){if(jQuery.isArray(value)){return(elem.checked=jQuery.inArray(jQuery(elem).val(),value)>=0);}}});});var rformElems=/^(?:input|select|textarea)$/i,rkeyEvent=/^key/,rmouseEvent=/^(?:mouse|contextmenu)|click/,rfocusMorph=/^(?:focusinfocus|focusoutblur)$/,rtypenamespace=/^([^.]*)(?:\.(.+)|)$/;function returnTrue(){return true;}
function returnFalse(){return false;}
jQuery.event={global:{},add:function(elem,types,handler,data,selector){var tmp,events,t,handleObjIn,special,eventHandle,handleObj,handlers,type,namespaces,origType,elemData=jQuery._data(elem);if(!elemData){return;}
if(handler.handler){handleObjIn=handler;handler=handleObjIn.handler;selector=handleObjIn.selector;}
if(!handler.guid){handler.guid=jQuery.guid++;}
if(!(events=elemData.events)){events=elemData.events={};}
if(!(eventHandle=elemData.handle)){eventHandle=elemData.handle=function(e){return typeof jQuery!==core_strundefined&&(!e||jQuery.event.triggered!==e.type)?jQuery.event.dispatch.apply(eventHandle.elem,arguments):undefined;};eventHandle.elem=elem;}
types=(types||"").match(core_rnotwhite)||[""];t=types.length;while(t--){tmp=rtypenamespace.exec(types[t])||[];type=origType=tmp[1];namespaces=(tmp[2]||"").split(".").sort();special=jQuery.event.special[type]||{};type=(selector?special.delegateType:special.bindType)||type;special=jQuery.event.special[type]||{};handleObj=jQuery.extend({type:type,origType:origType,data:data,handler:handler,guid:handler.guid,selector:selector,needsContext:selector&&jQuery.expr.match.needsContext.test(selector),namespace:namespaces.join(".")},handleObjIn);if(!(handlers=events[type])){handlers=events[type]=[];handlers.delegateCount=0;if(!special.setup||special.setup.call(elem,data,namespaces,eventHandle)===false){if(elem.addEventListener){elem.addEventListener(type,eventHandle,false);}else if(elem.attachEvent){elem.attachEvent("on"+type,eventHandle);}}}
if(special.add){special.add.call(elem,handleObj);if(!handleObj.handler.guid){handleObj.handler.guid=handler.guid;}}
if(selector){handlers.splice(handlers.delegateCount++,0,handleObj);}else{handlers.push(handleObj);}
jQuery.event.global[type]=true;}
elem=null;},remove:function(elem,types,handler,selector,mappedTypes){var j,handleObj,tmp,origCount,t,events,special,handlers,type,namespaces,origType,elemData=jQuery.hasData(elem)&&jQuery._data(elem);if(!elemData||!(events=elemData.events)){return;}
types=(types||"").match(core_rnotwhite)||[""];t=types.length;while(t--){tmp=rtypenamespace.exec(types[t])||[];type=origType=tmp[1];namespaces=(tmp[2]||"").split(".").sort();if(!type){for(type in events){jQuery.event.remove(elem,type+types[t],handler,selector,true);}
continue;}
special=jQuery.event.special[type]||{};type=(selector?special.delegateType:special.bindType)||type;handlers=events[type]||[];tmp=tmp[2]&&new RegExp("(^|\\.)"+namespaces.join("\\.(?:.*\\.|)")+"(\\.|$)");origCount=j=handlers.length;while(j--){handleObj=handlers[j];if((mappedTypes||origType===handleObj.origType)&&(!handler||handler.guid===handleObj.guid)&&(!tmp||tmp.test(handleObj.namespace))&&(!selector||selector===handleObj.selector||selector==="**"&&handleObj.selector)){handlers.splice(j,1);if(handleObj.selector){handlers.delegateCount--;}
if(special.remove){special.remove.call(elem,handleObj);}}}
if(origCount&&!handlers.length){if(!special.teardown||special.teardown.call(elem,namespaces,elemData.handle)===false){jQuery.removeEvent(elem,type,elemData.handle);}
delete events[type];}}
if(jQuery.isEmptyObject(events)){delete elemData.handle;jQuery._removeData(elem,"events");}},trigger:function(event,data,elem,onlyHandlers){var handle,ontype,cur,bubbleType,special,tmp,i,eventPath=[elem||document],type=core_hasOwn.call(event,"type")?event.type:event,namespaces=core_hasOwn.call(event,"namespace")?event.namespace.split("."):[];cur=tmp=elem=elem||document;if(elem.nodeType===3||elem.nodeType===8){return;}
if(rfocusMorph.test(type+jQuery.event.triggered)){return;}
if(type.indexOf(".")>=0){namespaces=type.split(".");type=namespaces.shift();namespaces.sort();}
ontype=type.indexOf(":")<0&&"on"+type;event=event[jQuery.expando]?event:new jQuery.Event(type,typeof event==="object"&&event);event.isTrigger=true;event.namespace=namespaces.join(".");event.namespace_re=event.namespace?new RegExp("(^|\\.)"+namespaces.join("\\.(?:.*\\.|)")+"(\\.|$)"):null;event.result=undefined;if(!event.target){event.target=elem;}
data=data==null?[event]:jQuery.makeArray(data,[event]);special=jQuery.event.special[type]||{};if(!onlyHandlers&&special.trigger&&special.trigger.apply(elem,data)===false){return;}
if(!onlyHandlers&&!special.noBubble&&!jQuery.isWindow(elem)){bubbleType=special.delegateType||type;if(!rfocusMorph.test(bubbleType+type)){cur=cur.parentNode;}
for(;cur;cur=cur.parentNode){eventPath.push(cur);tmp=cur;}
if(tmp===(elem.ownerDocument||document)){eventPath.push(tmp.defaultView||tmp.parentWindow||window);}}
i=0;while((cur=eventPath[i++])&&!event.isPropagationStopped()){event.type=i>1?bubbleType:special.bindType||type;handle=(jQuery._data(cur,"events")||{})[event.type]&&jQuery._data(cur,"handle");if(handle){handle.apply(cur,data);}
handle=ontype&&cur[ontype];if(handle&&jQuery.acceptData(cur)&&handle.apply&&handle.apply(cur,data)===false){event.preventDefault();}}
event.type=type;if(!onlyHandlers&&!event.isDefaultPrevented()){if((!special._default||special._default.apply(elem.ownerDocument,data)===false)&&!(type==="click"&&jQuery.nodeName(elem,"a"))&&jQuery.acceptData(elem)){if(ontype&&elem[type]&&!jQuery.isWindow(elem)){tmp=elem[ontype];if(tmp){elem[ontype]=null;}
jQuery.event.triggered=type;try{elem[type]();}catch(e){}
jQuery.event.triggered=undefined;if(tmp){elem[ontype]=tmp;}}}}
return event.result;},dispatch:function(event){event=jQuery.event.fix(event);var i,ret,handleObj,matched,j,handlerQueue=[],args=core_slice.call(arguments),handlers=(jQuery._data(this,"events")||{})[event.type]||[],special=jQuery.event.special[event.type]||{};args[0]=event;event.delegateTarget=this;if(special.preDispatch&&special.preDispatch.call(this,event)===false){return;}
handlerQueue=jQuery.event.handlers.call(this,event,handlers);i=0;while((matched=handlerQueue[i++])&&!event.isPropagationStopped()){event.currentTarget=matched.elem;j=0;while((handleObj=matched.handlers[j++])&&!event.isImmediatePropagationStopped()){if(!event.namespace_re||event.namespace_re.test(handleObj.namespace)){event.handleObj=handleObj;event.data=handleObj.data;ret=((jQuery.event.special[handleObj.origType]||{}).handle||handleObj.handler).apply(matched.elem,args);if(ret!==undefined){if((event.result=ret)===false){event.preventDefault();event.stopPropagation();}}}}}
if(special.postDispatch){special.postDispatch.call(this,event);}
return event.result;},handlers:function(event,handlers){var sel,handleObj,matches,i,handlerQueue=[],delegateCount=handlers.delegateCount,cur=event.target;if(delegateCount&&cur.nodeType&&(!event.button||event.type!=="click")){for(;cur!=this;cur=cur.parentNode||this){if(cur.nodeType===1&&(cur.disabled!==true||event.type!=="click")){matches=[];for(i=0;i<delegateCount;i++){handleObj=handlers[i];sel=handleObj.selector+" ";if(matches[sel]===undefined){matches[sel]=handleObj.needsContext?jQuery(sel,this).index(cur)>=0:jQuery.find(sel,this,null,[cur]).length;}
if(matches[sel]){matches.push(handleObj);}}
if(matches.length){handlerQueue.push({elem:cur,handlers:matches});}}}}
if(delegateCount<handlers.length){handlerQueue.push({elem:this,handlers:handlers.slice(delegateCount)});}
return handlerQueue;},fix:function(event){if(event[jQuery.expando]){return event;}
var i,prop,copy,type=event.type,originalEvent=event,fixHook=this.fixHooks[type];if(!fixHook){this.fixHooks[type]=fixHook=rmouseEvent.test(type)?this.mouseHooks:rkeyEvent.test(type)?this.keyHooks:{};}
copy=fixHook.props?this.props.concat(fixHook.props):this.props;event=new jQuery.Event(originalEvent);i=copy.length;while(i--){prop=copy[i];event[prop]=originalEvent[prop];}
if(!event.target){event.target=originalEvent.srcElement||document;}
if(event.target.nodeType===3){event.target=event.target.parentNode;}
event.metaKey=!!event.metaKey;return fixHook.filter?fixHook.filter(event,originalEvent):event;},props:"altKey bubbles cancelable ctrlKey currentTarget eventPhase metaKey relatedTarget shiftKey target timeStamp view which".split(" "),fixHooks:{},keyHooks:{props:"char charCode key keyCode".split(" "),filter:function(event,original){if(event.which==null){event.which=original.charCode!=null?original.charCode:original.keyCode;}
return event;}},mouseHooks:{props:"button buttons clientX clientY fromElement offsetX offsetY pageX pageY screenX screenY toElement".split(" "),filter:function(event,original){var body,eventDoc,doc,button=original.button,fromElement=original.fromElement;if(event.pageX==null&&original.clientX!=null){eventDoc=event.target.ownerDocument||document;doc=eventDoc.documentElement;body=eventDoc.body;event.pageX=original.clientX+(doc&&doc.scrollLeft||body&&body.scrollLeft||0)-(doc&&doc.clientLeft||body&&body.clientLeft||0);event.pageY=original.clientY+(doc&&doc.scrollTop||body&&body.scrollTop||0)-(doc&&doc.clientTop||body&&body.clientTop||0);}
if(!event.relatedTarget&&fromElement){event.relatedTarget=fromElement===event.target?original.toElement:fromElement;}
if(!event.which&&button!==undefined){event.which=(button&1?1:(button&2?3:(button&4?2:0)));}
return event;}},special:{load:{noBubble:true},click:{trigger:function(){if(jQuery.nodeName(this,"input")&&this.type==="checkbox"&&this.click){this.click();return false;}}},focus:{trigger:function(){if(this!==document.activeElement&&this.focus){try{this.focus();return false;}catch(e){}}},delegateType:"focusin"},blur:{trigger:function(){if(this===document.activeElement&&this.blur){this.blur();return false;}},delegateType:"focusout"},beforeunload:{postDispatch:function(event){if(event.result!==undefined){event.originalEvent.returnValue=event.result;}}}},simulate:function(type,elem,event,bubble){var e=jQuery.extend(new jQuery.Event(),event,{type:type,isSimulated:true,originalEvent:{}});if(bubble){jQuery.event.trigger(e,null,elem);}else{jQuery.event.dispatch.call(elem,e);}
if(e.isDefaultPrevented()){event.preventDefault();}}};jQuery.removeEvent=document.removeEventListener?function(elem,type,handle){if(elem.removeEventListener){elem.removeEventListener(type,handle,false);}}:function(elem,type,handle){var name="on"+type;if(elem.detachEvent){if(typeof elem[name]===core_strundefined){elem[name]=null;}
elem.detachEvent(name,handle);}};jQuery.Event=function(src,props){if(!(this instanceof jQuery.Event)){return new jQuery.Event(src,props);}
if(src&&src.type){this.originalEvent=src;this.type=src.type;this.isDefaultPrevented=(src.defaultPrevented||src.returnValue===false||src.getPreventDefault&&src.getPreventDefault())?returnTrue:returnFalse;}else{this.type=src;}
if(props){jQuery.extend(this,props);}
this.timeStamp=src&&src.timeStamp||jQuery.now();this[jQuery.expando]=true;};jQuery.Event.prototype={isDefaultPrevented:returnFalse,isPropagationStopped:returnFalse,isImmediatePropagationStopped:returnFalse,preventDefault:function(){var e=this.originalEvent;this.isDefaultPrevented=returnTrue;if(!e){return;}
if(e.preventDefault){e.preventDefault();}else{e.returnValue=false;}},stopPropagation:function(){var e=this.originalEvent;this.isPropagationStopped=returnTrue;if(!e){return;}
if(e.stopPropagation){e.stopPropagation();}
e.cancelBubble=true;},stopImmediatePropagation:function(){this.isImmediatePropagationStopped=returnTrue;this.stopPropagation();}};jQuery.each({mouseenter:"mouseover",mouseleave:"mouseout"},function(orig,fix){jQuery.event.special[orig]={delegateType:fix,bindType:fix,handle:function(event){var ret,target=this,related=event.relatedTarget,handleObj=event.handleObj;if(!related||(related!==target&&!jQuery.contains(target,related))){event.type=handleObj.origType;ret=handleObj.handler.apply(this,arguments);event.type=fix;}
return ret;}};});if(!jQuery.support.submitBubbles){jQuery.event.special.submit={setup:function(){if(jQuery.nodeName(this,"form")){return false;}
jQuery.event.add(this,"click._submit keypress._submit",function(e){var elem=e.target,form=jQuery.nodeName(elem,"input")||jQuery.nodeName(elem,"button")?elem.form:undefined;if(form&&!jQuery._data(form,"submitBubbles")){jQuery.event.add(form,"submit._submit",function(event){event._submit_bubble=true;});jQuery._data(form,"submitBubbles",true);}});},postDispatch:function(event){if(event._submit_bubble){delete event._submit_bubble;if(this.parentNode&&!event.isTrigger){jQuery.event.simulate("submit",this.parentNode,event,true);}}},teardown:function(){if(jQuery.nodeName(this,"form")){return false;}
jQuery.event.remove(this,"._submit");}};}
if(!jQuery.support.changeBubbles){jQuery.event.special.change={setup:function(){if(rformElems.test(this.nodeName)){if(this.type==="checkbox"||this.type==="radio"){jQuery.event.add(this,"propertychange._change",function(event){if(event.originalEvent.propertyName==="checked"){this._just_changed=true;}});jQuery.event.add(this,"click._change",function(event){if(this._just_changed&&!event.isTrigger){this._just_changed=false;}
jQuery.event.simulate("change",this,event,true);});}
return false;}
jQuery.event.add(this,"beforeactivate._change",function(e){var elem=e.target;if(rformElems.test(elem.nodeName)&&!jQuery._data(elem,"changeBubbles")){jQuery.event.add(elem,"change._change",function(event){if(this.parentNode&&!event.isSimulated&&!event.isTrigger){jQuery.event.simulate("change",this.parentNode,event,true);}});jQuery._data(elem,"changeBubbles",true);}});},handle:function(event){var elem=event.target;if(this!==elem||event.isSimulated||event.isTrigger||(elem.type!=="radio"&&elem.type!=="checkbox")){return event.handleObj.handler.apply(this,arguments);}},teardown:function(){jQuery.event.remove(this,"._change");return!rformElems.test(this.nodeName);}};}
if(!jQuery.support.focusinBubbles){jQuery.each({focus:"focusin",blur:"focusout"},function(orig,fix){var attaches=0,handler=function(event){jQuery.event.simulate(fix,event.target,jQuery.event.fix(event),true);};jQuery.event.special[fix]={setup:function(){if(attaches++===0){document.addEventListener(orig,handler,true);}},teardown:function(){if(--attaches===0){document.removeEventListener(orig,handler,true);}}};});}
jQuery.fn.extend({on:function(types,selector,data,fn,one){var type,origFn;if(typeof types==="object"){if(typeof selector!=="string"){data=data||selector;selector=undefined;}
for(type in types){this.on(type,selector,data,types[type],one);}
return this;}
if(data==null&&fn==null){fn=selector;data=selector=undefined;}else if(fn==null){if(typeof selector==="string"){fn=data;data=undefined;}else{fn=data;data=selector;selector=undefined;}}
if(fn===false){fn=returnFalse;}else if(!fn){return this;}
if(one===1){origFn=fn;fn=function(event){jQuery().off(event);return origFn.apply(this,arguments);};fn.guid=origFn.guid||(origFn.guid=jQuery.guid++);}
return this.each(function(){jQuery.event.add(this,types,fn,data,selector);});},one:function(types,selector,data,fn){return this.on(types,selector,data,fn,1);},off:function(types,selector,fn){var handleObj,type;if(types&&types.preventDefault&&types.handleObj){handleObj=types.handleObj;jQuery(types.delegateTarget).off(handleObj.namespace?handleObj.origType+"."+handleObj.namespace:handleObj.origType,handleObj.selector,handleObj.handler);return this;}
if(typeof types==="object"){for(type in types){this.off(type,selector,types[type]);}
return this;}
if(selector===false||typeof selector==="function"){fn=selector;selector=undefined;}
if(fn===false){fn=returnFalse;}
return this.each(function(){jQuery.event.remove(this,types,fn,selector);});},bind:function(types,data,fn){return this.on(types,null,data,fn);},unbind:function(types,fn){return this.off(types,null,fn);},delegate:function(selector,types,data,fn){return this.on(types,selector,data,fn);},undelegate:function(selector,types,fn){return arguments.length===1?this.off(selector,"**"):this.off(types,selector||"**",fn);},trigger:function(type,data){return this.each(function(){jQuery.event.trigger(type,data,this);});},triggerHandler:function(type,data){var elem=this[0];if(elem){return jQuery.event.trigger(type,data,elem,true);}}});(function(window,undefined){var i,cachedruns,Expr,getText,isXML,compile,hasDuplicate,outermostContext,setDocument,document,docElem,documentIsXML,rbuggyQSA,rbuggyMatches,matches,contains,sortOrder,expando="sizzle"+-(new Date()),preferredDoc=window.document,support={},dirruns=0,done=0,classCache=createCache(),tokenCache=createCache(),compilerCache=createCache(),strundefined=typeof undefined,MAX_NEGATIVE=1<<31,arr=[],pop=arr.pop,push=arr.push,slice=arr.slice,indexOf=arr.indexOf||function(elem){var i=0,len=this.length;for(;i<len;i++){if(this[i]===elem){return i;}}
return-1;},whitespace="[\\x20\\t\\r\\n\\f]",characterEncoding="(?:\\\\.|[\\w-]|[^\\x00-\\xa0])+",identifier=characterEncoding.replace("w","w#"),operators="([*^$|!~]?=)",attributes="\\["+whitespace+"*("+characterEncoding+")"+whitespace+"*(?:"+operators+whitespace+"*(?:(['\"])((?:\\\\.|[^\\\\])*?)\\3|("+identifier+")|)|)"+whitespace+"*\\]",pseudos=":("+characterEncoding+")(?:\\(((['\"])((?:\\\\.|[^\\\\])*?)\\3|((?:\\\\.|[^\\\\()[\\]]|"+attributes.replace(3,8)+")*)|.*)\\)|)",rtrim=new RegExp("^"+whitespace+"+|((?:^|[^\\\\])(?:\\\\.)*)"+whitespace+"+$","g"),rcomma=new RegExp("^"+whitespace+"*,"+whitespace+"*"),rcombinators=new RegExp("^"+whitespace+"*([\\x20\\t\\r\\n\\f>+~])"+whitespace+"*"),rpseudo=new RegExp(pseudos),ridentifier=new RegExp("^"+identifier+"$"),matchExpr={"ID":new RegExp("^#("+characterEncoding+")"),"CLASS":new RegExp("^\\.("+characterEncoding+")"),"NAME":new RegExp("^\\[name=['\"]?("+characterEncoding+")['\"]?\\]"),"TAG":new RegExp("^("+characterEncoding.replace("w","w*")+")"),"ATTR":new RegExp("^"+attributes),"PSEUDO":new RegExp("^"+pseudos),"CHILD":new RegExp("^:(only|first|last|nth|nth-last)-(child|of-type)(?:\\("+whitespace+"*(even|odd|(([+-]|)(\\d*)n|)"+whitespace+"*(?:([+-]|)"+whitespace+"*(\\d+)|))"+whitespace+"*\\)|)","i"),"needsContext":new RegExp("^"+whitespace+"*[>+~]|:(even|odd|eq|gt|lt|nth|first|last)(?:\\("+
whitespace+"*((?:-\\d)?\\d*)"+whitespace+"*\\)|)(?=[^-]|$)","i")},rsibling=/[\x20\t\r\n\f]*[+~]/,rnative=/^[^{]+\{\s*\[native code/,rquickExpr=/^(?:#([\w-]+)|(\w+)|\.([\w-]+))$/,rinputs=/^(?:input|select|textarea|button)$/i,rheader=/^h\d$/i,rescape=/'|\\/g,rattributeQuotes=/\=[\x20\t\r\n\f]*([^'"\]]*)[\x20\t\r\n\f]*\]/g,runescape=/\\([\da-fA-F]{1,6}[\x20\t\r\n\f]?|.)/g,funescape=function(_,escaped){var high="0x"+escaped-0x10000;return high!==high?escaped:high<0?String.fromCharCode(high+0x10000):String.fromCharCode(high>>10|0xD800,high&0x3FF|0xDC00);};try{slice.call(preferredDoc.documentElement.childNodes,0)[0].nodeType;}catch(e){slice=function(i){var elem,results=[];while((elem=this[i++])){results.push(elem);}
return results;};}
function isNative(fn){return rnative.test(fn+"");}
function createCache(){var cache,keys=[];return(cache=function(key,value){if(keys.push(key+=" ")>Expr.cacheLength){delete cache[keys.shift()];}
return(cache[key]=value);});}
function markFunction(fn){fn[expando]=true;return fn;}
function assert(fn){var div=document.createElement("div");try{return fn(div);}catch(e){return false;}finally{div=null;}}
function Sizzle(selector,context,results,seed){var match,elem,m,nodeType,i,groups,old,nid,newContext,newSelector;if((context?context.ownerDocument||context:preferredDoc)!==document){setDocument(context);}
context=context||document;results=results||[];if(!selector||typeof selector!=="string"){return results;}
if((nodeType=context.nodeType)!==1&&nodeType!==9){return[];}
if(!documentIsXML&&!seed){if((match=rquickExpr.exec(selector))){if((m=match[1])){if(nodeType===9){elem=context.getElementById(m);if(elem&&elem.parentNode){if(elem.id===m){results.push(elem);return results;}}else{return results;}}else{if(context.ownerDocument&&(elem=context.ownerDocument.getElementById(m))&&contains(context,elem)&&elem.id===m){results.push(elem);return results;}}}else if(match[2]){push.apply(results,slice.call(context.getElementsByTagName(selector),0));return results;}else if((m=match[3])&&support.getByClassName&&context.getElementsByClassName){push.apply(results,slice.call(context.getElementsByClassName(m),0));return results;}}
if(support.qsa&&!rbuggyQSA.test(selector)){old=true;nid=expando;newContext=context;newSelector=nodeType===9&&selector;if(nodeType===1&&context.nodeName.toLowerCase()!=="object"){groups=tokenize(selector);if((old=context.getAttribute("id"))){nid=old.replace(rescape,"\\$&");}else{context.setAttribute("id",nid);}
nid="[id='"+nid+"'] ";i=groups.length;while(i--){groups[i]=nid+toSelector(groups[i]);}
newContext=rsibling.test(selector)&&context.parentNode||context;newSelector=groups.join(",");}
if(newSelector){try{push.apply(results,slice.call(newContext.querySelectorAll(newSelector),0));return results;}catch(qsaError){}finally{if(!old){context.removeAttribute("id");}}}}}
return select(selector.replace(rtrim,"$1"),context,results,seed);}
isXML=Sizzle.isXML=function(elem){var documentElement=elem&&(elem.ownerDocument||elem).documentElement;return documentElement?documentElement.nodeName!=="HTML":false;};setDocument=Sizzle.setDocument=function(node){var doc=node?node.ownerDocument||node:preferredDoc;if(doc===document||doc.nodeType!==9||!doc.documentElement){return document;}
document=doc;docElem=doc.documentElement;documentIsXML=isXML(doc);support.tagNameNoComments=assert(function(div){div.appendChild(doc.createComment(""));return!div.getElementsByTagName("*").length;});support.attributes=assert(function(div){div.innerHTML="<select></select>";var type=typeof div.lastChild.getAttribute("multiple");return type!=="boolean"&&type!=="string";});support.getByClassName=assert(function(div){div.innerHTML="<div class='hidden e'></div><div class='hidden'></div>";if(!div.getElementsByClassName||!div.getElementsByClassName("e").length){return false;}
div.lastChild.className="e";return div.getElementsByClassName("e").length===2;});support.getByName=assert(function(div){div.id=expando+0;div.innerHTML="<a name='"+expando+"'></a><div name='"+expando+"'></div>";docElem.insertBefore(div,docElem.firstChild);var pass=doc.getElementsByName&&doc.getElementsByName(expando).length===2+
doc.getElementsByName(expando+0).length;support.getIdNotName=!doc.getElementById(expando);docElem.removeChild(div);return pass;});Expr.attrHandle=assert(function(div){div.innerHTML="<a href='#'></a>";return div.firstChild&&typeof div.firstChild.getAttribute!==strundefined&&div.firstChild.getAttribute("href")==="#";})?{}:{"href":function(elem){return elem.getAttribute("href",2);},"type":function(elem){return elem.getAttribute("type");}};if(support.getIdNotName){Expr.find["ID"]=function(id,context){if(typeof context.getElementById!==strundefined&&!documentIsXML){var m=context.getElementById(id);return m&&m.parentNode?[m]:[];}};Expr.filter["ID"]=function(id){var attrId=id.replace(runescape,funescape);return function(elem){return elem.getAttribute("id")===attrId;};};}else{Expr.find["ID"]=function(id,context){if(typeof context.getElementById!==strundefined&&!documentIsXML){var m=context.getElementById(id);return m?m.id===id||typeof m.getAttributeNode!==strundefined&&m.getAttributeNode("id").value===id?[m]:undefined:[];}};Expr.filter["ID"]=function(id){var attrId=id.replace(runescape,funescape);return function(elem){var node=typeof elem.getAttributeNode!==strundefined&&elem.getAttributeNode("id");return node&&node.value===attrId;};};}
Expr.find["TAG"]=support.tagNameNoComments?function(tag,context){if(typeof context.getElementsByTagName!==strundefined){return context.getElementsByTagName(tag);}}:function(tag,context){var elem,tmp=[],i=0,results=context.getElementsByTagName(tag);if(tag==="*"){while((elem=results[i++])){if(elem.nodeType===1){tmp.push(elem);}}
return tmp;}
return results;};Expr.find["NAME"]=support.getByName&&function(tag,context){if(typeof context.getElementsByName!==strundefined){return context.getElementsByName(name);}};Expr.find["CLASS"]=support.getByClassName&&function(className,context){if(typeof context.getElementsByClassName!==strundefined&&!documentIsXML){return context.getElementsByClassName(className);}};rbuggyMatches=[];rbuggyQSA=[":focus"];if((support.qsa=isNative(doc.querySelectorAll))){assert(function(div){div.innerHTML="<select><option selected=''></option></select>";if(!div.querySelectorAll("[selected]").length){rbuggyQSA.push("\\["+whitespace+"*(?:checked|disabled|ismap|multiple|readonly|selected|value)");}
if(!div.querySelectorAll(":checked").length){rbuggyQSA.push(":checked");}});assert(function(div){div.innerHTML="<input type='hidden' i=''/>";if(div.querySelectorAll("[i^='']").length){rbuggyQSA.push("[*^$]="+whitespace+"*(?:\"\"|'')");}
if(!div.querySelectorAll(":enabled").length){rbuggyQSA.push(":enabled",":disabled");}
div.querySelectorAll("*,:x");rbuggyQSA.push(",.*:");});}
if((support.matchesSelector=isNative((matches=docElem.matchesSelector||docElem.mozMatchesSelector||docElem.webkitMatchesSelector||docElem.oMatchesSelector||docElem.msMatchesSelector)))){assert(function(div){support.disconnectedMatch=matches.call(div,"div");matches.call(div,"[s!='']:x");rbuggyMatches.push("!=",pseudos);});}
rbuggyQSA=new RegExp(rbuggyQSA.join("|"));rbuggyMatches=new RegExp(rbuggyMatches.join("|"));contains=isNative(docElem.contains)||docElem.compareDocumentPosition?function(a,b){var adown=a.nodeType===9?a.documentElement:a,bup=b&&b.parentNode;return a===bup||!!(bup&&bup.nodeType===1&&(adown.contains?adown.contains(bup):a.compareDocumentPosition&&a.compareDocumentPosition(bup)&16));}:function(a,b){if(b){while((b=b.parentNode)){if(b===a){return true;}}}
return false;};sortOrder=docElem.compareDocumentPosition?function(a,b){var compare;if(a===b){hasDuplicate=true;return 0;}
if((compare=b.compareDocumentPosition&&a.compareDocumentPosition&&a.compareDocumentPosition(b))){if(compare&1||a.parentNode&&a.parentNode.nodeType===11){if(a===doc||contains(preferredDoc,a)){return-1;}
if(b===doc||contains(preferredDoc,b)){return 1;}
return 0;}
return compare&4?-1:1;}
return a.compareDocumentPosition?-1:1;}:function(a,b){var cur,i=0,aup=a.parentNode,bup=b.parentNode,ap=[a],bp=[b];if(a===b){hasDuplicate=true;return 0;}else if(!aup||!bup){return a===doc?-1:b===doc?1:aup?-1:bup?1:0;}else if(aup===bup){return siblingCheck(a,b);}
cur=a;while((cur=cur.parentNode)){ap.unshift(cur);}
cur=b;while((cur=cur.parentNode)){bp.unshift(cur);}
while(ap[i]===bp[i]){i++;}
return i?siblingCheck(ap[i],bp[i]):ap[i]===preferredDoc?-1:bp[i]===preferredDoc?1:0;};hasDuplicate=false;[0,0].sort(sortOrder);support.detectDuplicates=hasDuplicate;return document;};Sizzle.matches=function(expr,elements){return Sizzle(expr,null,null,elements);};Sizzle.matchesSelector=function(elem,expr){if((elem.ownerDocument||elem)!==document){setDocument(elem);}
expr=expr.replace(rattributeQuotes,"='$1']");if(support.matchesSelector&&!documentIsXML&&(!rbuggyMatches||!rbuggyMatches.test(expr))&&!rbuggyQSA.test(expr)){try{var ret=matches.call(elem,expr);if(ret||support.disconnectedMatch||elem.document&&elem.document.nodeType!==11){return ret;}}catch(e){}}
return Sizzle(expr,document,null,[elem]).length>0;};Sizzle.contains=function(context,elem){if((context.ownerDocument||context)!==document){setDocument(context);}
return contains(context,elem);};Sizzle.attr=function(elem,name){var val;if((elem.ownerDocument||elem)!==document){setDocument(elem);}
if(!documentIsXML){name=name.toLowerCase();}
if((val=Expr.attrHandle[name])){return val(elem);}
if(documentIsXML||support.attributes){return elem.getAttribute(name);}
return((val=elem.getAttributeNode(name))||elem.getAttribute(name))&&elem[name]===true?name:val&&val.specified?val.value:null;};Sizzle.error=function(msg){throw new Error("Syntax error, unrecognized expression: "+msg);};Sizzle.uniqueSort=function(results){var elem,duplicates=[],i=1,j=0;hasDuplicate=!support.detectDuplicates;results.sort(sortOrder);if(hasDuplicate){for(;(elem=results[i]);i++){if(elem===results[i-1]){j=duplicates.push(i);}}
while(j--){results.splice(duplicates[j],1);}}
return results;};function siblingCheck(a,b){var cur=b&&a,diff=cur&&(~b.sourceIndex||MAX_NEGATIVE)-(~a.sourceIndex||MAX_NEGATIVE);if(diff){return diff;}
if(cur){while((cur=cur.nextSibling)){if(cur===b){return-1;}}}
return a?1:-1;}
function createInputPseudo(type){return function(elem){var name=elem.nodeName.toLowerCase();return name==="input"&&elem.type===type;};}
function createButtonPseudo(type){return function(elem){var name=elem.nodeName.toLowerCase();return(name==="input"||name==="button")&&elem.type===type;};}
function createPositionalPseudo(fn){return markFunction(function(argument){argument=+argument;return markFunction(function(seed,matches){var j,matchIndexes=fn([],seed.length,argument),i=matchIndexes.length;while(i--){if(seed[(j=matchIndexes[i])]){seed[j]=!(matches[j]=seed[j]);}}});});}
getText=Sizzle.getText=function(elem){var node,ret="",i=0,nodeType=elem.nodeType;if(!nodeType){for(;(node=elem[i]);i++){ret+=getText(node);}}else if(nodeType===1||nodeType===9||nodeType===11){if(typeof elem.textContent==="string"){return elem.textContent;}else{for(elem=elem.firstChild;elem;elem=elem.nextSibling){ret+=getText(elem);}}}else if(nodeType===3||nodeType===4){return elem.nodeValue;}
return ret;};Expr=Sizzle.selectors={cacheLength:50,createPseudo:markFunction,match:matchExpr,find:{},relative:{">":{dir:"parentNode",first:true}," ":{dir:"parentNode"},"+":{dir:"previousSibling",first:true},"~":{dir:"previousSibling"}},preFilter:{"ATTR":function(match){match[1]=match[1].replace(runescape,funescape);match[3]=(match[4]||match[5]||"").replace(runescape,funescape);if(match[2]==="~="){match[3]=" "+match[3]+" ";}
return match.slice(0,4);},"CHILD":function(match){match[1]=match[1].toLowerCase();if(match[1].slice(0,3)==="nth"){if(!match[3]){Sizzle.error(match[0]);}
match[4]=+(match[4]?match[5]+(match[6]||1):2*(match[3]==="even"||match[3]==="odd"));match[5]=+((match[7]+match[8])||match[3]==="odd");}else if(match[3]){Sizzle.error(match[0]);}
return match;},"PSEUDO":function(match){var excess,unquoted=!match[5]&&match[2];if(matchExpr["CHILD"].test(match[0])){return null;}
if(match[4]){match[2]=match[4];}else if(unquoted&&rpseudo.test(unquoted)&&(excess=tokenize(unquoted,true))&&(excess=unquoted.indexOf(")",unquoted.length-excess)-unquoted.length)){match[0]=match[0].slice(0,excess);match[2]=unquoted.slice(0,excess);}
return match.slice(0,3);}},filter:{"TAG":function(nodeName){if(nodeName==="*"){return function(){return true;};}
nodeName=nodeName.replace(runescape,funescape).toLowerCase();return function(elem){return elem.nodeName&&elem.nodeName.toLowerCase()===nodeName;};},"CLASS":function(className){var pattern=classCache[className+" "];return pattern||(pattern=new RegExp("(^|"+whitespace+")"+className+"("+whitespace+"|$)"))&&classCache(className,function(elem){return pattern.test(elem.className||(typeof elem.getAttribute!==strundefined&&elem.getAttribute("class"))||"");});},"ATTR":function(name,operator,check){return function(elem){var result=Sizzle.attr(elem,name);if(result==null){return operator==="!=";}
if(!operator){return true;}
result+="";return operator==="="?result===check:operator==="!="?result!==check:operator==="^="?check&&result.indexOf(check)===0:operator==="*="?check&&result.indexOf(check)>-1:operator==="$="?check&&result.slice(-check.length)===check:operator==="~="?(" "+result+" ").indexOf(check)>-1:operator==="|="?result===check||result.slice(0,check.length+1)===check+"-":false;};},"CHILD":function(type,what,argument,first,last){var simple=type.slice(0,3)!=="nth",forward=type.slice(-4)!=="last",ofType=what==="of-type";return first===1&&last===0?function(elem){return!!elem.parentNode;}:function(elem,context,xml){var cache,outerCache,node,diff,nodeIndex,start,dir=simple!==forward?"nextSibling":"previousSibling",parent=elem.parentNode,name=ofType&&elem.nodeName.toLowerCase(),useCache=!xml&&!ofType;if(parent){if(simple){while(dir){node=elem;while((node=node[dir])){if(ofType?node.nodeName.toLowerCase()===name:node.nodeType===1){return false;}}
start=dir=type==="only"&&!start&&"nextSibling";}
return true;}
start=[forward?parent.firstChild:parent.lastChild];if(forward&&useCache){outerCache=parent[expando]||(parent[expando]={});cache=outerCache[type]||[];nodeIndex=cache[0]===dirruns&&cache[1];diff=cache[0]===dirruns&&cache[2];node=nodeIndex&&parent.childNodes[nodeIndex];while((node=++nodeIndex&&node&&node[dir]||(diff=nodeIndex=0)||start.pop())){if(node.nodeType===1&&++diff&&node===elem){outerCache[type]=[dirruns,nodeIndex,diff];break;}}}else if(useCache&&(cache=(elem[expando]||(elem[expando]={}))[type])&&cache[0]===dirruns){diff=cache[1];}else{while((node=++nodeIndex&&node&&node[dir]||(diff=nodeIndex=0)||start.pop())){if((ofType?node.nodeName.toLowerCase()===name:node.nodeType===1)&&++diff){if(useCache){(node[expando]||(node[expando]={}))[type]=[dirruns,diff];}
if(node===elem){break;}}}}
diff-=last;return diff===first||(diff%first===0&&diff/first>=0);}};},"PSEUDO":function(pseudo,argument){var args,fn=Expr.pseudos[pseudo]||Expr.setFilters[pseudo.toLowerCase()]||Sizzle.error("unsupported pseudo: "+pseudo);if(fn[expando]){return fn(argument);}
if(fn.length>1){args=[pseudo,pseudo,"",argument];return Expr.setFilters.hasOwnProperty(pseudo.toLowerCase())?markFunction(function(seed,matches){var idx,matched=fn(seed,argument),i=matched.length;while(i--){idx=indexOf.call(seed,matched[i]);seed[idx]=!(matches[idx]=matched[i]);}}):function(elem){return fn(elem,0,args);};}
return fn;}},pseudos:{"not":markFunction(function(selector){var input=[],results=[],matcher=compile(selector.replace(rtrim,"$1"));return matcher[expando]?markFunction(function(seed,matches,context,xml){var elem,unmatched=matcher(seed,null,xml,[]),i=seed.length;while(i--){if((elem=unmatched[i])){seed[i]=!(matches[i]=elem);}}}):function(elem,context,xml){input[0]=elem;matcher(input,null,xml,results);return!results.pop();};}),"has":markFunction(function(selector){return function(elem){return Sizzle(selector,elem).length>0;};}),"contains":markFunction(function(text){return function(elem){return(elem.textContent||elem.innerText||getText(elem)).indexOf(text)>-1;};}),"lang":markFunction(function(lang){if(!ridentifier.test(lang||"")){Sizzle.error("unsupported lang: "+lang);}
lang=lang.replace(runescape,funescape).toLowerCase();return function(elem){var elemLang;do{if((elemLang=documentIsXML?elem.getAttribute("xml:lang")||elem.getAttribute("lang"):elem.lang)){elemLang=elemLang.toLowerCase();return elemLang===lang||elemLang.indexOf(lang+"-")===0;}}while((elem=elem.parentNode)&&elem.nodeType===1);return false;};}),"target":function(elem){var hash=window.location&&window.location.hash;return hash&&hash.slice(1)===elem.id;},"root":function(elem){return elem===docElem;},"focus":function(elem){return elem===document.activeElement&&(!document.hasFocus||document.hasFocus())&&!!(elem.type||elem.href||~elem.tabIndex);},"enabled":function(elem){return elem.disabled===false;},"disabled":function(elem){return elem.disabled===true;},"checked":function(elem){var nodeName=elem.nodeName.toLowerCase();return(nodeName==="input"&&!!elem.checked)||(nodeName==="option"&&!!elem.selected);},"selected":function(elem){if(elem.parentNode){elem.parentNode.selectedIndex;}
return elem.selected===true;},"empty":function(elem){for(elem=elem.firstChild;elem;elem=elem.nextSibling){if(elem.nodeName>"@"||elem.nodeType===3||elem.nodeType===4){return false;}}
return true;},"parent":function(elem){return!Expr.pseudos["empty"](elem);},"header":function(elem){return rheader.test(elem.nodeName);},"input":function(elem){return rinputs.test(elem.nodeName);},"button":function(elem){var name=elem.nodeName.toLowerCase();return name==="input"&&elem.type==="button"||name==="button";},"text":function(elem){var attr;return elem.nodeName.toLowerCase()==="input"&&elem.type==="text"&&((attr=elem.getAttribute("type"))==null||attr.toLowerCase()===elem.type);},"first":createPositionalPseudo(function(){return[0];}),"last":createPositionalPseudo(function(matchIndexes,length){return[length-1];}),"eq":createPositionalPseudo(function(matchIndexes,length,argument){return[argument<0?argument+length:argument];}),"even":createPositionalPseudo(function(matchIndexes,length){var i=0;for(;i<length;i+=2){matchIndexes.push(i);}
return matchIndexes;}),"odd":createPositionalPseudo(function(matchIndexes,length){var i=1;for(;i<length;i+=2){matchIndexes.push(i);}
return matchIndexes;}),"lt":createPositionalPseudo(function(matchIndexes,length,argument){var i=argument<0?argument+length:argument;for(;--i>=0;){matchIndexes.push(i);}
return matchIndexes;}),"gt":createPositionalPseudo(function(matchIndexes,length,argument){var i=argument<0?argument+length:argument;for(;++i<length;){matchIndexes.push(i);}
return matchIndexes;})}};for(i in{radio:true,checkbox:true,file:true,password:true,image:true}){Expr.pseudos[i]=createInputPseudo(i);}
for(i in{submit:true,reset:true}){Expr.pseudos[i]=createButtonPseudo(i);}
function tokenize(selector,parseOnly){var matched,match,tokens,type,soFar,groups,preFilters,cached=tokenCache[selector+" "];if(cached){return parseOnly?0:cached.slice(0);}
soFar=selector;groups=[];preFilters=Expr.preFilter;while(soFar){if(!matched||(match=rcomma.exec(soFar))){if(match){soFar=soFar.slice(match[0].length)||soFar;}
groups.push(tokens=[]);}
matched=false;if((match=rcombinators.exec(soFar))){matched=match.shift();tokens.push({value:matched,type:match[0].replace(rtrim," ")});soFar=soFar.slice(matched.length);}
for(type in Expr.filter){if((match=matchExpr[type].exec(soFar))&&(!preFilters[type]||(match=preFilters[type](match)))){matched=match.shift();tokens.push({value:matched,type:type,matches:match});soFar=soFar.slice(matched.length);}}
if(!matched){break;}}
return parseOnly?soFar.length:soFar?Sizzle.error(selector):tokenCache(selector,groups).slice(0);}
function toSelector(tokens){var i=0,len=tokens.length,selector="";for(;i<len;i++){selector+=tokens[i].value;}
return selector;}
function addCombinator(matcher,combinator,base){var dir=combinator.dir,checkNonElements=base&&dir==="parentNode",doneName=done++;return combinator.first?function(elem,context,xml){while((elem=elem[dir])){if(elem.nodeType===1||checkNonElements){return matcher(elem,context,xml);}}}:function(elem,context,xml){var data,cache,outerCache,dirkey=dirruns+" "+doneName;if(xml){while((elem=elem[dir])){if(elem.nodeType===1||checkNonElements){if(matcher(elem,context,xml)){return true;}}}}else{while((elem=elem[dir])){if(elem.nodeType===1||checkNonElements){outerCache=elem[expando]||(elem[expando]={});if((cache=outerCache[dir])&&cache[0]===dirkey){if((data=cache[1])===true||data===cachedruns){return data===true;}}else{cache=outerCache[dir]=[dirkey];cache[1]=matcher(elem,context,xml)||cachedruns;if(cache[1]===true){return true;}}}}}};}
function elementMatcher(matchers){return matchers.length>1?function(elem,context,xml){var i=matchers.length;while(i--){if(!matchers[i](elem,context,xml)){return false;}}
return true;}:matchers[0];}
function condense(unmatched,map,filter,context,xml){var elem,newUnmatched=[],i=0,len=unmatched.length,mapped=map!=null;for(;i<len;i++){if((elem=unmatched[i])){if(!filter||filter(elem,context,xml)){newUnmatched.push(elem);if(mapped){map.push(i);}}}}
return newUnmatched;}
function setMatcher(preFilter,selector,matcher,postFilter,postFinder,postSelector){if(postFilter&&!postFilter[expando]){postFilter=setMatcher(postFilter);}
if(postFinder&&!postFinder[expando]){postFinder=setMatcher(postFinder,postSelector);}
return markFunction(function(seed,results,context,xml){var temp,i,elem,preMap=[],postMap=[],preexisting=results.length,elems=seed||multipleContexts(selector||"*",context.nodeType?[context]:context,[]),matcherIn=preFilter&&(seed||!selector)?condense(elems,preMap,preFilter,context,xml):elems,matcherOut=matcher?postFinder||(seed?preFilter:preexisting||postFilter)?[]:results:matcherIn;if(matcher){matcher(matcherIn,matcherOut,context,xml);}
if(postFilter){temp=condense(matcherOut,postMap);postFilter(temp,[],context,xml);i=temp.length;while(i--){if((elem=temp[i])){matcherOut[postMap[i]]=!(matcherIn[postMap[i]]=elem);}}}
if(seed){if(postFinder||preFilter){if(postFinder){temp=[];i=matcherOut.length;while(i--){if((elem=matcherOut[i])){temp.push((matcherIn[i]=elem));}}
postFinder(null,(matcherOut=[]),temp,xml);}
i=matcherOut.length;while(i--){if((elem=matcherOut[i])&&(temp=postFinder?indexOf.call(seed,elem):preMap[i])>-1){seed[temp]=!(results[temp]=elem);}}}}else{matcherOut=condense(matcherOut===results?matcherOut.splice(preexisting,matcherOut.length):matcherOut);if(postFinder){postFinder(null,results,matcherOut,xml);}else{push.apply(results,matcherOut);}}});}
function matcherFromTokens(tokens){var checkContext,matcher,j,len=tokens.length,leadingRelative=Expr.relative[tokens[0].type],implicitRelative=leadingRelative||Expr.relative[" "],i=leadingRelative?1:0,matchContext=addCombinator(function(elem){return elem===checkContext;},implicitRelative,true),matchAnyContext=addCombinator(function(elem){return indexOf.call(checkContext,elem)>-1;},implicitRelative,true),matchers=[function(elem,context,xml){return(!leadingRelative&&(xml||context!==outermostContext))||((checkContext=context).nodeType?matchContext(elem,context,xml):matchAnyContext(elem,context,xml));}];for(;i<len;i++){if((matcher=Expr.relative[tokens[i].type])){matchers=[addCombinator(elementMatcher(matchers),matcher)];}else{matcher=Expr.filter[tokens[i].type].apply(null,tokens[i].matches);if(matcher[expando]){j=++i;for(;j<len;j++){if(Expr.relative[tokens[j].type]){break;}}
return setMatcher(i>1&&elementMatcher(matchers),i>1&&toSelector(tokens.slice(0,i-1)).replace(rtrim,"$1"),matcher,i<j&&matcherFromTokens(tokens.slice(i,j)),j<len&&matcherFromTokens((tokens=tokens.slice(j))),j<len&&toSelector(tokens));}
matchers.push(matcher);}}
return elementMatcher(matchers);}
function matcherFromGroupMatchers(elementMatchers,setMatchers){var matcherCachedRuns=0,bySet=setMatchers.length>0,byElement=elementMatchers.length>0,superMatcher=function(seed,context,xml,results,expandContext){var elem,j,matcher,setMatched=[],matchedCount=0,i="0",unmatched=seed&&[],outermost=expandContext!=null,contextBackup=outermostContext,elems=seed||byElement&&Expr.find["TAG"]("*",expandContext&&context.parentNode||context),dirrunsUnique=(dirruns+=contextBackup==null?1:Math.random()||0.1);if(outermost){outermostContext=context!==document&&context;cachedruns=matcherCachedRuns;}
for(;(elem=elems[i])!=null;i++){if(byElement&&elem){j=0;while((matcher=elementMatchers[j++])){if(matcher(elem,context,xml)){results.push(elem);break;}}
if(outermost){dirruns=dirrunsUnique;cachedruns=++matcherCachedRuns;}}
if(bySet){if((elem=!matcher&&elem)){matchedCount--;}
if(seed){unmatched.push(elem);}}}
matchedCount+=i;if(bySet&&i!==matchedCount){j=0;while((matcher=setMatchers[j++])){matcher(unmatched,setMatched,context,xml);}
if(seed){if(matchedCount>0){while(i--){if(!(unmatched[i]||setMatched[i])){setMatched[i]=pop.call(results);}}}
setMatched=condense(setMatched);}
push.apply(results,setMatched);if(outermost&&!seed&&setMatched.length>0&&(matchedCount+setMatchers.length)>1){Sizzle.uniqueSort(results);}}
if(outermost){dirruns=dirrunsUnique;outermostContext=contextBackup;}
return unmatched;};return bySet?markFunction(superMatcher):superMatcher;}
compile=Sizzle.compile=function(selector,group){var i,setMatchers=[],elementMatchers=[],cached=compilerCache[selector+" "];if(!cached){if(!group){group=tokenize(selector);}
i=group.length;while(i--){cached=matcherFromTokens(group[i]);if(cached[expando]){setMatchers.push(cached);}else{elementMatchers.push(cached);}}
cached=compilerCache(selector,matcherFromGroupMatchers(elementMatchers,setMatchers));}
return cached;};function multipleContexts(selector,contexts,results){var i=0,len=contexts.length;for(;i<len;i++){Sizzle(selector,contexts[i],results);}
return results;}
function select(selector,context,results,seed){var i,tokens,token,type,find,match=tokenize(selector);if(!seed){if(match.length===1){tokens=match[0]=match[0].slice(0);if(tokens.length>2&&(token=tokens[0]).type==="ID"&&context.nodeType===9&&!documentIsXML&&Expr.relative[tokens[1].type]){context=Expr.find["ID"](token.matches[0].replace(runescape,funescape),context)[0];if(!context){return results;}
selector=selector.slice(tokens.shift().value.length);}
i=matchExpr["needsContext"].test(selector)?0:tokens.length;while(i--){token=tokens[i];if(Expr.relative[(type=token.type)]){break;}
if((find=Expr.find[type])){if((seed=find(token.matches[0].replace(runescape,funescape),rsibling.test(tokens[0].type)&&context.parentNode||context))){tokens.splice(i,1);selector=seed.length&&toSelector(tokens);if(!selector){push.apply(results,slice.call(seed,0));return results;}
break;}}}}}
compile(selector,match)(seed,context,documentIsXML,results,rsibling.test(selector));return results;}
Expr.pseudos["nth"]=Expr.pseudos["eq"];function setFilters(){}
Expr.filters=setFilters.prototype=Expr.pseudos;Expr.setFilters=new setFilters();setDocument();Sizzle.attr=jQuery.attr;jQuery.find=Sizzle;jQuery.expr=Sizzle.selectors;jQuery.expr[":"]=jQuery.expr.pseudos;jQuery.unique=Sizzle.uniqueSort;jQuery.text=Sizzle.getText;jQuery.isXMLDoc=Sizzle.isXML;jQuery.contains=Sizzle.contains;})(window);var runtil=/Until$/,rparentsprev=/^(?:parents|prev(?:Until|All))/,isSimple=/^.[^:#\[\.,]*$/,rneedsContext=jQuery.expr.match.needsContext,guaranteedUnique={children:true,contents:true,next:true,prev:true};jQuery.fn.extend({find:function(selector){var i,ret,self,len=this.length;if(typeof selector!=="string"){self=this;return this.pushStack(jQuery(selector).filter(function(){for(i=0;i<len;i++){if(jQuery.contains(self[i],this)){return true;}}}));}
ret=[];for(i=0;i<len;i++){jQuery.find(selector,this[i],ret);}
ret=this.pushStack(len>1?jQuery.unique(ret):ret);ret.selector=(this.selector?this.selector+" ":"")+selector;return ret;},has:function(target){var i,targets=jQuery(target,this),len=targets.length;return this.filter(function(){for(i=0;i<len;i++){if(jQuery.contains(this,targets[i])){return true;}}});},not:function(selector){return this.pushStack(winnow(this,selector,false));},filter:function(selector){return this.pushStack(winnow(this,selector,true));},is:function(selector){return!!selector&&(typeof selector==="string"?rneedsContext.test(selector)?jQuery(selector,this.context).index(this[0])>=0:jQuery.filter(selector,this).length>0:this.filter(selector).length>0);},closest:function(selectors,context){var cur,i=0,l=this.length,ret=[],pos=rneedsContext.test(selectors)||typeof selectors!=="string"?jQuery(selectors,context||this.context):0;for(;i<l;i++){cur=this[i];while(cur&&cur.ownerDocument&&cur!==context&&cur.nodeType!==11){if(pos?pos.index(cur)>-1:jQuery.find.matchesSelector(cur,selectors)){ret.push(cur);break;}
cur=cur.parentNode;}}
return this.pushStack(ret.length>1?jQuery.unique(ret):ret);},index:function(elem){if(!elem){return(this[0]&&this[0].parentNode)?this.first().prevAll().length:-1;}
if(typeof elem==="string"){return jQuery.inArray(this[0],jQuery(elem));}
return jQuery.inArray(elem.jquery?elem[0]:elem,this);},add:function(selector,context){var set=typeof selector==="string"?jQuery(selector,context):jQuery.makeArray(selector&&selector.nodeType?[selector]:selector),all=jQuery.merge(this.get(),set);return this.pushStack(jQuery.unique(all));},addBack:function(selector){return this.add(selector==null?this.prevObject:this.prevObject.filter(selector));}});jQuery.fn.andSelf=jQuery.fn.addBack;function sibling(cur,dir){do{cur=cur[dir];}while(cur&&cur.nodeType!==1);return cur;}
jQuery.each({parent:function(elem){var parent=elem.parentNode;return parent&&parent.nodeType!==11?parent:null;},parents:function(elem){return jQuery.dir(elem,"parentNode");},parentsUntil:function(elem,i,until){return jQuery.dir(elem,"parentNode",until);},next:function(elem){return sibling(elem,"nextSibling");},prev:function(elem){return sibling(elem,"previousSibling");},nextAll:function(elem){return jQuery.dir(elem,"nextSibling");},prevAll:function(elem){return jQuery.dir(elem,"previousSibling");},nextUntil:function(elem,i,until){return jQuery.dir(elem,"nextSibling",until);},prevUntil:function(elem,i,until){return jQuery.dir(elem,"previousSibling",until);},siblings:function(elem){return jQuery.sibling((elem.parentNode||{}).firstChild,elem);},children:function(elem){return jQuery.sibling(elem.firstChild);},contents:function(elem){return jQuery.nodeName(elem,"iframe")?elem.contentDocument||elem.contentWindow.document:jQuery.merge([],elem.childNodes);}},function(name,fn){jQuery.fn[name]=function(until,selector){var ret=jQuery.map(this,fn,until);if(!runtil.test(name)){selector=until;}
if(selector&&typeof selector==="string"){ret=jQuery.filter(selector,ret);}
ret=this.length>1&&!guaranteedUnique[name]?jQuery.unique(ret):ret;if(this.length>1&&rparentsprev.test(name)){ret=ret.reverse();}
return this.pushStack(ret);};});jQuery.extend({filter:function(expr,elems,not){if(not){expr=":not("+expr+")";}
return elems.length===1?jQuery.find.matchesSelector(elems[0],expr)?[elems[0]]:[]:jQuery.find.matches(expr,elems);},dir:function(elem,dir,until){var matched=[],cur=elem[dir];while(cur&&cur.nodeType!==9&&(until===undefined||cur.nodeType!==1||!jQuery(cur).is(until))){if(cur.nodeType===1){matched.push(cur);}
cur=cur[dir];}
return matched;},sibling:function(n,elem){var r=[];for(;n;n=n.nextSibling){if(n.nodeType===1&&n!==elem){r.push(n);}}
return r;}});function winnow(elements,qualifier,keep){qualifier=qualifier||0;if(jQuery.isFunction(qualifier)){return jQuery.grep(elements,function(elem,i){var retVal=!!qualifier.call(elem,i,elem);return retVal===keep;});}else if(qualifier.nodeType){return jQuery.grep(elements,function(elem){return(elem===qualifier)===keep;});}else if(typeof qualifier==="string"){var filtered=jQuery.grep(elements,function(elem){return elem.nodeType===1;});if(isSimple.test(qualifier)){return jQuery.filter(qualifier,filtered,!keep);}else{qualifier=jQuery.filter(qualifier,filtered);}}
return jQuery.grep(elements,function(elem){return(jQuery.inArray(elem,qualifier)>=0)===keep;});}
function createSafeFragment(document){var list=nodeNames.split("|"),safeFrag=document.createDocumentFragment();if(safeFrag.createElement){while(list.length){safeFrag.createElement(list.pop());}}
return safeFrag;}
var nodeNames="abbr|article|aside|audio|bdi|canvas|data|datalist|details|figcaption|figure|footer|"+"header|hgroup|mark|meter|nav|output|progress|section|summary|time|video",rinlinejQuery=/ jQuery\d+="(?:null|\d+)"/g,rnoshimcache=new RegExp("<(?:"+nodeNames+")[\\s/>]","i"),rleadingWhitespace=/^\s+/,rxhtmlTag=/<(?!area|br|col|embed|hr|img|input|link|meta|param)(([\w:]+)[^>]*)\/>/gi,rtagName=/<([\w:]+)/,rtbody=/<tbody/i,rhtml=/<|&#?\w+;/,rnoInnerhtml=/<(?:script|style|link)/i,manipulation_rcheckableType=/^(?:checkbox|radio)$/i,rchecked=/checked\s*(?:[^=]|=\s*.checked.)/i,rscriptType=/^$|\/(?:java|ecma)script/i,rscriptTypeMasked=/^true\/(.*)/,rcleanScript=/^\s*<!(?:\[CDATA\[|--)|(?:\]\]|--)>\s*$/g,wrapMap={option:[1,"<select multiple='multiple'>","</select>"],legend:[1,"<fieldset>","</fieldset>"],area:[1,"<map>","</map>"],param:[1,"<object>","</object>"],thead:[1,"<table>","</table>"],tr:[2,"<table><tbody>","</tbody></table>"],col:[2,"<table><tbody></tbody><colgroup>","</colgroup></table>"],td:[3,"<table><tbody><tr>","</tr></tbody></table>"],_default:jQuery.support.htmlSerialize?[0,"",""]:[1,"X<div>","</div>"]},safeFragment=createSafeFragment(document),fragmentDiv=safeFragment.appendChild(document.createElement("div"));wrapMap.optgroup=wrapMap.option;wrapMap.tbody=wrapMap.tfoot=wrapMap.colgroup=wrapMap.caption=wrapMap.thead;wrapMap.th=wrapMap.td;jQuery.fn.extend({text:function(value){return jQuery.access(this,function(value){return value===undefined?jQuery.text(this):this.empty().append((this[0]&&this[0].ownerDocument||document).createTextNode(value));},null,value,arguments.length);},wrapAll:function(html){if(jQuery.isFunction(html)){return this.each(function(i){jQuery(this).wrapAll(html.call(this,i));});}
if(this[0]){var wrap=jQuery(html,this[0].ownerDocument).eq(0).clone(true);if(this[0].parentNode){wrap.insertBefore(this[0]);}
wrap.map(function(){var elem=this;while(elem.firstChild&&elem.firstChild.nodeType===1){elem=elem.firstChild;}
return elem;}).append(this);}
return this;},wrapInner:function(html){if(jQuery.isFunction(html)){return this.each(function(i){jQuery(this).wrapInner(html.call(this,i));});}
return this.each(function(){var self=jQuery(this),contents=self.contents();if(contents.length){contents.wrapAll(html);}else{self.append(html);}});},wrap:function(html){var isFunction=jQuery.isFunction(html);return this.each(function(i){jQuery(this).wrapAll(isFunction?html.call(this,i):html);});},unwrap:function(){return this.parent().each(function(){if(!jQuery.nodeName(this,"body")){jQuery(this).replaceWith(this.childNodes);}}).end();},append:function(){return this.domManip(arguments,true,function(elem){if(this.nodeType===1||this.nodeType===11||this.nodeType===9){this.appendChild(elem);}});},prepend:function(){return this.domManip(arguments,true,function(elem){if(this.nodeType===1||this.nodeType===11||this.nodeType===9){this.insertBefore(elem,this.firstChild);}});},before:function(){return this.domManip(arguments,false,function(elem){if(this.parentNode){this.parentNode.insertBefore(elem,this);}});},after:function(){return this.domManip(arguments,false,function(elem){if(this.parentNode){this.parentNode.insertBefore(elem,this.nextSibling);}});},remove:function(selector,keepData){var elem,i=0;for(;(elem=this[i])!=null;i++){if(!selector||jQuery.filter(selector,[elem]).length>0){if(!keepData&&elem.nodeType===1){jQuery.cleanData(getAll(elem));}
if(elem.parentNode){if(keepData&&jQuery.contains(elem.ownerDocument,elem)){setGlobalEval(getAll(elem,"script"));}
elem.parentNode.removeChild(elem);}}}
return this;},empty:function(){var elem,i=0;for(;(elem=this[i])!=null;i++){if(elem.nodeType===1){jQuery.cleanData(getAll(elem,false));}
while(elem.firstChild){elem.removeChild(elem.firstChild);}
if(elem.options&&jQuery.nodeName(elem,"select")){elem.options.length=0;}}
return this;},clone:function(dataAndEvents,deepDataAndEvents){dataAndEvents=dataAndEvents==null?false:dataAndEvents;deepDataAndEvents=deepDataAndEvents==null?dataAndEvents:deepDataAndEvents;return this.map(function(){return jQuery.clone(this,dataAndEvents,deepDataAndEvents);});},html:function(value){return jQuery.access(this,function(value){var elem=this[0]||{},i=0,l=this.length;if(value===undefined){return elem.nodeType===1?elem.innerHTML.replace(rinlinejQuery,""):undefined;}
if(typeof value==="string"&&!rnoInnerhtml.test(value)&&(jQuery.support.htmlSerialize||!rnoshimcache.test(value))&&(jQuery.support.leadingWhitespace||!rleadingWhitespace.test(value))&&!wrapMap[(rtagName.exec(value)||["",""])[1].toLowerCase()]){value=value.replace(rxhtmlTag,"<$1></$2>");try{for(;i<l;i++){elem=this[i]||{};if(elem.nodeType===1){jQuery.cleanData(getAll(elem,false));elem.innerHTML=value;}}
elem=0;}catch(e){}}
if(elem){this.empty().append(value);}},null,value,arguments.length);},replaceWith:function(value){var isFunc=jQuery.isFunction(value);if(!isFunc&&typeof value!=="string"){value=jQuery(value).not(this).detach();}
return this.domManip([value],true,function(elem){var next=this.nextSibling,parent=this.parentNode;if(parent){jQuery(this).remove();parent.insertBefore(elem,next);}});},detach:function(selector){return this.remove(selector,true);},domManip:function(args,table,callback){args=core_concat.apply([],args);var first,node,hasScripts,scripts,doc,fragment,i=0,l=this.length,set=this,iNoClone=l-1,value=args[0],isFunction=jQuery.isFunction(value);if(isFunction||!(l<=1||typeof value!=="string"||jQuery.support.checkClone||!rchecked.test(value))){return this.each(function(index){var self=set.eq(index);if(isFunction){args[0]=value.call(this,index,table?self.html():undefined);}
self.domManip(args,table,callback);});}
if(l){fragment=jQuery.buildFragment(args,this[0].ownerDocument,false,this);first=fragment.firstChild;if(fragment.childNodes.length===1){fragment=first;}
if(first){table=table&&jQuery.nodeName(first,"tr");scripts=jQuery.map(getAll(fragment,"script"),disableScript);hasScripts=scripts.length;for(;i<l;i++){node=fragment;if(i!==iNoClone){node=jQuery.clone(node,true,true);if(hasScripts){jQuery.merge(scripts,getAll(node,"script"));}}
callback.call(table&&jQuery.nodeName(this[i],"table")?findOrAppend(this[i],"tbody"):this[i],node,i);}
if(hasScripts){doc=scripts[scripts.length-1].ownerDocument;jQuery.map(scripts,restoreScript);for(i=0;i<hasScripts;i++){node=scripts[i];if(rscriptType.test(node.type||"")&&!jQuery._data(node,"globalEval")&&jQuery.contains(doc,node)){if(node.src){jQuery.ajax({url:node.src,type:"GET",dataType:"script",async:false,global:false,"throws":true});}else{jQuery.globalEval((node.text||node.textContent||node.innerHTML||"").replace(rcleanScript,""));}}}}
fragment=first=null;}}
return this;}});function findOrAppend(elem,tag){return elem.getElementsByTagName(tag)[0]||elem.appendChild(elem.ownerDocument.createElement(tag));}
function disableScript(elem){var attr=elem.getAttributeNode("type");elem.type=(attr&&attr.specified)+"/"+elem.type;return elem;}
function restoreScript(elem){var match=rscriptTypeMasked.exec(elem.type);if(match){elem.type=match[1];}else{elem.removeAttribute("type");}
return elem;}
function setGlobalEval(elems,refElements){var elem,i=0;for(;(elem=elems[i])!=null;i++){jQuery._data(elem,"globalEval",!refElements||jQuery._data(refElements[i],"globalEval"));}}
function cloneCopyEvent(src,dest){if(dest.nodeType!==1||!jQuery.hasData(src)){return;}
var type,i,l,oldData=jQuery._data(src),curData=jQuery._data(dest,oldData),events=oldData.events;if(events){delete curData.handle;curData.events={};for(type in events){for(i=0,l=events[type].length;i<l;i++){jQuery.event.add(dest,type,events[type][i]);}}}
if(curData.data){curData.data=jQuery.extend({},curData.data);}}
function fixCloneNodeIssues(src,dest){var nodeName,e,data;if(dest.nodeType!==1){return;}
nodeName=dest.nodeName.toLowerCase();if(!jQuery.support.noCloneEvent&&dest[jQuery.expando]){data=jQuery._data(dest);for(e in data.events){jQuery.removeEvent(dest,e,data.handle);}
dest.removeAttribute(jQuery.expando);}
if(nodeName==="script"&&dest.text!==src.text){disableScript(dest).text=src.text;restoreScript(dest);}else if(nodeName==="object"){if(dest.parentNode){dest.outerHTML=src.outerHTML;}
if(jQuery.support.html5Clone&&(src.innerHTML&&!jQuery.trim(dest.innerHTML))){dest.innerHTML=src.innerHTML;}}else if(nodeName==="input"&&manipulation_rcheckableType.test(src.type)){dest.defaultChecked=dest.checked=src.checked;if(dest.value!==src.value){dest.value=src.value;}}else if(nodeName==="option"){dest.defaultSelected=dest.selected=src.defaultSelected;}else if(nodeName==="input"||nodeName==="textarea"){dest.defaultValue=src.defaultValue;}}
jQuery.each({appendTo:"append",prependTo:"prepend",insertBefore:"before",insertAfter:"after",replaceAll:"replaceWith"},function(name,original){jQuery.fn[name]=function(selector){var elems,i=0,ret=[],insert=jQuery(selector),last=insert.length-1;for(;i<=last;i++){elems=i===last?this:this.clone(true);jQuery(insert[i])[original](elems);core_push.apply(ret,elems.get());}
return this.pushStack(ret);};});function getAll(context,tag){var elems,elem,i=0,found=typeof context.getElementsByTagName!==core_strundefined?context.getElementsByTagName(tag||"*"):typeof context.querySelectorAll!==core_strundefined?context.querySelectorAll(tag||"*"):undefined;if(!found){for(found=[],elems=context.childNodes||context;(elem=elems[i])!=null;i++){if(!tag||jQuery.nodeName(elem,tag)){found.push(elem);}else{jQuery.merge(found,getAll(elem,tag));}}}
return tag===undefined||tag&&jQuery.nodeName(context,tag)?jQuery.merge([context],found):found;}
function fixDefaultChecked(elem){if(manipulation_rcheckableType.test(elem.type)){elem.defaultChecked=elem.checked;}}
jQuery.extend({clone:function(elem,dataAndEvents,deepDataAndEvents){var destElements,node,clone,i,srcElements,inPage=jQuery.contains(elem.ownerDocument,elem);if(jQuery.support.html5Clone||jQuery.isXMLDoc(elem)||!rnoshimcache.test("<"+elem.nodeName+">")){clone=elem.cloneNode(true);}else{fragmentDiv.innerHTML=elem.outerHTML;fragmentDiv.removeChild(clone=fragmentDiv.firstChild);}
if((!jQuery.support.noCloneEvent||!jQuery.support.noCloneChecked)&&(elem.nodeType===1||elem.nodeType===11)&&!jQuery.isXMLDoc(elem)){destElements=getAll(clone);srcElements=getAll(elem);for(i=0;(node=srcElements[i])!=null;++i){if(destElements[i]){fixCloneNodeIssues(node,destElements[i]);}}}
if(dataAndEvents){if(deepDataAndEvents){srcElements=srcElements||getAll(elem);destElements=destElements||getAll(clone);for(i=0;(node=srcElements[i])!=null;i++){cloneCopyEvent(node,destElements[i]);}}else{cloneCopyEvent(elem,clone);}}
destElements=getAll(clone,"script");if(destElements.length>0){setGlobalEval(destElements,!inPage&&getAll(elem,"script"));}
destElements=srcElements=node=null;return clone;},buildFragment:function(elems,context,scripts,selection){var j,elem,contains,tmp,tag,tbody,wrap,l=elems.length,safe=createSafeFragment(context),nodes=[],i=0;for(;i<l;i++){elem=elems[i];if(elem||elem===0){if(jQuery.type(elem)==="object"){jQuery.merge(nodes,elem.nodeType?[elem]:elem);}else if(!rhtml.test(elem)){nodes.push(context.createTextNode(elem));}else{tmp=tmp||safe.appendChild(context.createElement("div"));tag=(rtagName.exec(elem)||["",""])[1].toLowerCase();wrap=wrapMap[tag]||wrapMap._default;tmp.innerHTML=wrap[1]+elem.replace(rxhtmlTag,"<$1></$2>")+wrap[2];j=wrap[0];while(j--){tmp=tmp.lastChild;}
if(!jQuery.support.leadingWhitespace&&rleadingWhitespace.test(elem)){nodes.push(context.createTextNode(rleadingWhitespace.exec(elem)[0]));}
if(!jQuery.support.tbody){elem=tag==="table"&&!rtbody.test(elem)?tmp.firstChild:wrap[1]==="<table>"&&!rtbody.test(elem)?tmp:0;j=elem&&elem.childNodes.length;while(j--){if(jQuery.nodeName((tbody=elem.childNodes[j]),"tbody")&&!tbody.childNodes.length){elem.removeChild(tbody);}}}
jQuery.merge(nodes,tmp.childNodes);tmp.textContent="";while(tmp.firstChild){tmp.removeChild(tmp.firstChild);}
tmp=safe.lastChild;}}}
if(tmp){safe.removeChild(tmp);}
if(!jQuery.support.appendChecked){jQuery.grep(getAll(nodes,"input"),fixDefaultChecked);}
i=0;while((elem=nodes[i++])){if(selection&&jQuery.inArray(elem,selection)!==-1){continue;}
contains=jQuery.contains(elem.ownerDocument,elem);tmp=getAll(safe.appendChild(elem),"script");if(contains){setGlobalEval(tmp);}
if(scripts){j=0;while((elem=tmp[j++])){if(rscriptType.test(elem.type||"")){scripts.push(elem);}}}}
tmp=null;return safe;},cleanData:function(elems,acceptData){var elem,type,id,data,i=0,internalKey=jQuery.expando,cache=jQuery.cache,deleteExpando=jQuery.support.deleteExpando,special=jQuery.event.special;for(;(elem=elems[i])!=null;i++){if(acceptData||jQuery.acceptData(elem)){id=elem[internalKey];data=id&&cache[id];if(data){if(data.events){for(type in data.events){if(special[type]){jQuery.event.remove(elem,type);}else{jQuery.removeEvent(elem,type,data.handle);}}}
if(cache[id]){delete cache[id];if(deleteExpando){delete elem[internalKey];}else if(typeof elem.removeAttribute!==core_strundefined){elem.removeAttribute(internalKey);}else{elem[internalKey]=null;}
core_deletedIds.push(id);}}}}}});var iframe,getStyles,curCSS,ralpha=/alpha\([^)]*\)/i,ropacity=/opacity\s*=\s*([^)]*)/,rposition=/^(top|right|bottom|left)$/,rdisplayswap=/^(none|table(?!-c[ea]).+)/,rmargin=/^margin/,rnumsplit=new RegExp("^("+core_pnum+")(.*)$","i"),rnumnonpx=new RegExp("^("+core_pnum+")(?!px)[a-z%]+$","i"),rrelNum=new RegExp("^([+-])=("+core_pnum+")","i"),elemdisplay={BODY:"block"},cssShow={position:"absolute",visibility:"hidden",display:"block"},cssNormalTransform={letterSpacing:0,fontWeight:400},cssExpand=["Top","Right","Bottom","Left"],cssPrefixes=["Webkit","O","Moz","ms"];function vendorPropName(style,name){if(name in style){return name;}
var capName=name.charAt(0).toUpperCase()+name.slice(1),origName=name,i=cssPrefixes.length;while(i--){name=cssPrefixes[i]+capName;if(name in style){return name;}}
return origName;}
function isHidden(elem,el){elem=el||elem;return jQuery.css(elem,"display")==="none"||!jQuery.contains(elem.ownerDocument,elem);}
function showHide(elements,show){var display,elem,hidden,values=[],index=0,length=elements.length;for(;index<length;index++){elem=elements[index];if(!elem.style){continue;}
values[index]=jQuery._data(elem,"olddisplay");display=elem.style.display;if(show){if(!values[index]&&display==="none"){elem.style.display="";}
if(elem.style.display===""&&isHidden(elem)){values[index]=jQuery._data(elem,"olddisplay",css_defaultDisplay(elem.nodeName));}}else{if(!values[index]){hidden=isHidden(elem);if(display&&display!=="none"||!hidden){jQuery._data(elem,"olddisplay",hidden?display:jQuery.css(elem,"display"));}}}}
for(index=0;index<length;index++){elem=elements[index];if(!elem.style){continue;}
if(!show||elem.style.display==="none"||elem.style.display===""){elem.style.display=show?values[index]||"":"none";}}
return elements;}
jQuery.fn.extend({css:function(name,value){return jQuery.access(this,function(elem,name,value){var len,styles,map={},i=0;if(jQuery.isArray(name)){styles=getStyles(elem);len=name.length;for(;i<len;i++){map[name[i]]=jQuery.css(elem,name[i],false,styles);}
return map;}
return value!==undefined?jQuery.style(elem,name,value):jQuery.css(elem,name);},name,value,arguments.length>1);},show:function(){return showHide(this,true);},hide:function(){return showHide(this);},toggle:function(state){var bool=typeof state==="boolean";return this.each(function(){if(bool?state:isHidden(this)){jQuery(this).show();}else{jQuery(this).hide();}});}});jQuery.extend({cssHooks:{opacity:{get:function(elem,computed){if(computed){var ret=curCSS(elem,"opacity");return ret===""?"1":ret;}}}},cssNumber:{"columnCount":true,"fillOpacity":true,"fontWeight":true,"lineHeight":true,"opacity":true,"orphans":true,"widows":true,"zIndex":true,"zoom":true},cssProps:{"float":jQuery.support.cssFloat?"cssFloat":"styleFloat"},style:function(elem,name,value,extra){if(!elem||elem.nodeType===3||elem.nodeType===8||!elem.style){return;}
var ret,type,hooks,origName=jQuery.camelCase(name),style=elem.style;name=jQuery.cssProps[origName]||(jQuery.cssProps[origName]=vendorPropName(style,origName));hooks=jQuery.cssHooks[name]||jQuery.cssHooks[origName];if(value!==undefined){type=typeof value;if(type==="string"&&(ret=rrelNum.exec(value))){value=(ret[1]+1)*ret[2]+parseFloat(jQuery.css(elem,name));type="number";}
if(value==null||type==="number"&&isNaN(value)){return;}
if(type==="number"&&!jQuery.cssNumber[origName]){value+="px";}
if(!jQuery.support.clearCloneStyle&&value===""&&name.indexOf("background")===0){style[name]="inherit";}
if(!hooks||!("set"in hooks)||(value=hooks.set(elem,value,extra))!==undefined){try{style[name]=value;}catch(e){}}}else{if(hooks&&"get"in hooks&&(ret=hooks.get(elem,false,extra))!==undefined){return ret;}
return style[name];}},css:function(elem,name,extra,styles){var num,val,hooks,origName=jQuery.camelCase(name);name=jQuery.cssProps[origName]||(jQuery.cssProps[origName]=vendorPropName(elem.style,origName));hooks=jQuery.cssHooks[name]||jQuery.cssHooks[origName];if(hooks&&"get"in hooks){val=hooks.get(elem,true,extra);}
if(val===undefined){val=curCSS(elem,name,styles);}
if(val==="normal"&&name in cssNormalTransform){val=cssNormalTransform[name];}
if(extra===""||extra){num=parseFloat(val);return extra===true||jQuery.isNumeric(num)?num||0:val;}
return val;},swap:function(elem,options,callback,args){var ret,name,old={};for(name in options){old[name]=elem.style[name];elem.style[name]=options[name];}
ret=callback.apply(elem,args||[]);for(name in options){elem.style[name]=old[name];}
return ret;}});if(window.getComputedStyle){getStyles=function(elem){return window.getComputedStyle(elem,null);};curCSS=function(elem,name,_computed){var width,minWidth,maxWidth,computed=_computed||getStyles(elem),ret=computed?computed.getPropertyValue(name)||computed[name]:undefined,style=elem.style;if(computed){if(ret===""&&!jQuery.contains(elem.ownerDocument,elem)){ret=jQuery.style(elem,name);}
if(rnumnonpx.test(ret)&&rmargin.test(name)){width=style.width;minWidth=style.minWidth;maxWidth=style.maxWidth;style.minWidth=style.maxWidth=style.width=ret;ret=computed.width;style.width=width;style.minWidth=minWidth;style.maxWidth=maxWidth;}}
return ret;};}else if(document.documentElement.currentStyle){getStyles=function(elem){return elem.currentStyle;};curCSS=function(elem,name,_computed){var left,rs,rsLeft,computed=_computed||getStyles(elem),ret=computed?computed[name]:undefined,style=elem.style;if(ret==null&&style&&style[name]){ret=style[name];}
if(rnumnonpx.test(ret)&&!rposition.test(name)){left=style.left;rs=elem.runtimeStyle;rsLeft=rs&&rs.left;if(rsLeft){rs.left=elem.currentStyle.left;}
style.left=name==="fontSize"?"1em":ret;ret=style.pixelLeft+"px";style.left=left;if(rsLeft){rs.left=rsLeft;}}
return ret===""?"auto":ret;};}
function setPositiveNumber(elem,value,subtract){var matches=rnumsplit.exec(value);return matches?Math.max(0,matches[1]-(subtract||0))+(matches[2]||"px"):value;}
function augmentWidthOrHeight(elem,name,extra,isBorderBox,styles){var i=extra===(isBorderBox?"border":"content")?4:name==="width"?1:0,val=0;for(;i<4;i+=2){if(extra==="margin"){val+=jQuery.css(elem,extra+cssExpand[i],true,styles);}
if(isBorderBox){if(extra==="content"){val-=jQuery.css(elem,"padding"+cssExpand[i],true,styles);}
if(extra!=="margin"){val-=jQuery.css(elem,"border"+cssExpand[i]+"Width",true,styles);}}else{val+=jQuery.css(elem,"padding"+cssExpand[i],true,styles);if(extra!=="padding"){val+=jQuery.css(elem,"border"+cssExpand[i]+"Width",true,styles);}}}
return val;}
function getWidthOrHeight(elem,name,extra){var valueIsBorderBox=true,val=name==="width"?elem.offsetWidth:elem.offsetHeight,styles=getStyles(elem),isBorderBox=jQuery.support.boxSizing&&jQuery.css(elem,"boxSizing",false,styles)==="border-box";if(val<=0||val==null){val=curCSS(elem,name,styles);if(val<0||val==null){val=elem.style[name];}
if(rnumnonpx.test(val)){return val;}
valueIsBorderBox=isBorderBox&&(jQuery.support.boxSizingReliable||val===elem.style[name]);val=parseFloat(val)||0;}
return(val+
augmentWidthOrHeight(elem,name,extra||(isBorderBox?"border":"content"),valueIsBorderBox,styles))+"px";}
function css_defaultDisplay(nodeName){var doc=document,display=elemdisplay[nodeName];if(!display){display=actualDisplay(nodeName,doc);if(display==="none"||!display){iframe=(iframe||jQuery("<iframe frameborder='0' width='0' height='0'/>").css("cssText","display:block !important")).appendTo(doc.documentElement);doc=(iframe[0].contentWindow||iframe[0].contentDocument).document;doc.write("<!doctype html><html><body>");doc.close();display=actualDisplay(nodeName,doc);iframe.detach();}
elemdisplay[nodeName]=display;}
return display;}
function actualDisplay(name,doc){var elem=jQuery(doc.createElement(name)).appendTo(doc.body),display=jQuery.css(elem[0],"display");elem.remove();return display;}
jQuery.each(["height","width"],function(i,name){jQuery.cssHooks[name]={get:function(elem,computed,extra){if(computed){return elem.offsetWidth===0&&rdisplayswap.test(jQuery.css(elem,"display"))?jQuery.swap(elem,cssShow,function(){return getWidthOrHeight(elem,name,extra);}):getWidthOrHeight(elem,name,extra);}},set:function(elem,value,extra){var styles=extra&&getStyles(elem);return setPositiveNumber(elem,value,extra?augmentWidthOrHeight(elem,name,extra,jQuery.support.boxSizing&&jQuery.css(elem,"boxSizing",false,styles)==="border-box",styles):0);}};});if(!jQuery.support.opacity){jQuery.cssHooks.opacity={get:function(elem,computed){return ropacity.test((computed&&elem.currentStyle?elem.currentStyle.filter:elem.style.filter)||"")?(0.01*parseFloat(RegExp.$1))+"":computed?"1":"";},set:function(elem,value){var style=elem.style,currentStyle=elem.currentStyle,opacity=jQuery.isNumeric(value)?"alpha(opacity="+value*100+")":"",filter=currentStyle&&currentStyle.filter||style.filter||"";style.zoom=1;if((value>=1||value==="")&&jQuery.trim(filter.replace(ralpha,""))===""&&style.removeAttribute){style.removeAttribute("filter");if(value===""||currentStyle&&!currentStyle.filter){return;}}
style.filter=ralpha.test(filter)?filter.replace(ralpha,opacity):filter+" "+opacity;}};}
jQuery(function(){if(!jQuery.support.reliableMarginRight){jQuery.cssHooks.marginRight={get:function(elem,computed){if(computed){return jQuery.swap(elem,{"display":"inline-block"},curCSS,[elem,"marginRight"]);}}};}
if(!jQuery.support.pixelPosition&&jQuery.fn.position){jQuery.each(["top","left"],function(i,prop){jQuery.cssHooks[prop]={get:function(elem,computed){if(computed){computed=curCSS(elem,prop);return rnumnonpx.test(computed)?jQuery(elem).position()[prop]+"px":computed;}}};});}});if(jQuery.expr&&jQuery.expr.filters){jQuery.expr.filters.hidden=function(elem){return elem.offsetWidth<=0&&elem.offsetHeight<=0||(!jQuery.support.reliableHiddenOffsets&&((elem.style&&elem.style.display)||jQuery.css(elem,"display"))==="none");};jQuery.expr.filters.visible=function(elem){return!jQuery.expr.filters.hidden(elem);};}
jQuery.each({margin:"",padding:"",border:"Width"},function(prefix,suffix){jQuery.cssHooks[prefix+suffix]={expand:function(value){var i=0,expanded={},parts=typeof value==="string"?value.split(" "):[value];for(;i<4;i++){expanded[prefix+cssExpand[i]+suffix]=parts[i]||parts[i-2]||parts[0];}
return expanded;}};if(!rmargin.test(prefix)){jQuery.cssHooks[prefix+suffix].set=setPositiveNumber;}});var r20=/%20/g,rbracket=/\[\]$/,rCRLF=/\r?\n/g,rsubmitterTypes=/^(?:submit|button|image|reset|file)$/i,rsubmittable=/^(?:input|select|textarea|keygen)/i;jQuery.fn.extend({serialize:function(){return jQuery.param(this.serializeArray());},serializeArray:function(){return this.map(function(){var elements=jQuery.prop(this,"elements");return elements?jQuery.makeArray(elements):this;}).filter(function(){var type=this.type;return this.name&&!jQuery(this).is(":disabled")&&rsubmittable.test(this.nodeName)&&!rsubmitterTypes.test(type)&&(this.checked||!manipulation_rcheckableType.test(type));}).map(function(i,elem){var val=jQuery(this).val();return val==null?null:jQuery.isArray(val)?jQuery.map(val,function(val){return{name:elem.name,value:val.replace(rCRLF,"\r\n")};}):{name:elem.name,value:val.replace(rCRLF,"\r\n")};}).get();}});jQuery.param=function(a,traditional){var prefix,s=[],add=function(key,value){value=jQuery.isFunction(value)?value():(value==null?"":value);s[s.length]=encodeURIComponent(key)+"="+encodeURIComponent(value);};if(traditional===undefined){traditional=jQuery.ajaxSettings&&jQuery.ajaxSettings.traditional;}
if(jQuery.isArray(a)||(a.jquery&&!jQuery.isPlainObject(a))){jQuery.each(a,function(){add(this.name,this.value);});}else{for(prefix in a){buildParams(prefix,a[prefix],traditional,add);}}
return s.join("&").replace(r20,"+");};function buildParams(prefix,obj,traditional,add){var name;if(jQuery.isArray(obj)){jQuery.each(obj,function(i,v){if(traditional||rbracket.test(prefix)){add(prefix,v);}else{buildParams(prefix+"["+(typeof v==="object"?i:"")+"]",v,traditional,add);}});}else if(!traditional&&jQuery.type(obj)==="object"){for(name in obj){buildParams(prefix+"["+name+"]",obj[name],traditional,add);}}else{add(prefix,obj);}}
jQuery.each(("blur focus focusin focusout load resize scroll unload click dblclick "+"mousedown mouseup mousemove mouseover mouseout mouseenter mouseleave "+"change select submit keydown keypress keyup error contextmenu").split(" "),function(i,name){jQuery.fn[name]=function(data,fn){return arguments.length>0?this.on(name,null,data,fn):this.trigger(name);};});jQuery.fn.hover=function(fnOver,fnOut){return this.mouseenter(fnOver).mouseleave(fnOut||fnOver);};var
ajaxLocParts,ajaxLocation,ajax_nonce=jQuery.now(),ajax_rquery=/\?/,rhash=/#.*$/,rts=/([?&])_=[^&]*/,rheaders=/^(.*?):[ \t]*([^\r\n]*)\r?$/mg,rlocalProtocol=/^(?:about|app|app-storage|.+-extension|file|res|widget):$/,rnoContent=/^(?:GET|HEAD)$/,rprotocol=/^\/\//,rurl=/^([\w.+-]+:)(?:\/\/([^\/?#:]*)(?::(\d+)|)|)/,_load=jQuery.fn.load,prefilters={},transports={},allTypes="*/".concat("*");try{ajaxLocation=location.href;}catch(e){ajaxLocation=document.createElement("a");ajaxLocation.href="";ajaxLocation=ajaxLocation.href;}
ajaxLocParts=rurl.exec(ajaxLocation.toLowerCase())||[];function addToPrefiltersOrTransports(structure){return function(dataTypeExpression,func){if(typeof dataTypeExpression!=="string"){func=dataTypeExpression;dataTypeExpression="*";}
var dataType,i=0,dataTypes=dataTypeExpression.toLowerCase().match(core_rnotwhite)||[];if(jQuery.isFunction(func)){while((dataType=dataTypes[i++])){if(dataType[0]==="+"){dataType=dataType.slice(1)||"*";(structure[dataType]=structure[dataType]||[]).unshift(func);}else{(structure[dataType]=structure[dataType]||[]).push(func);}}}};}
function inspectPrefiltersOrTransports(structure,options,originalOptions,jqXHR){var inspected={},seekingTransport=(structure===transports);function inspect(dataType){var selected;inspected[dataType]=true;jQuery.each(structure[dataType]||[],function(_,prefilterOrFactory){var dataTypeOrTransport=prefilterOrFactory(options,originalOptions,jqXHR);if(typeof dataTypeOrTransport==="string"&&!seekingTransport&&!inspected[dataTypeOrTransport]){options.dataTypes.unshift(dataTypeOrTransport);inspect(dataTypeOrTransport);return false;}else if(seekingTransport){return!(selected=dataTypeOrTransport);}});return selected;}
return inspect(options.dataTypes[0])||!inspected["*"]&&inspect("*");}
function ajaxExtend(target,src){var deep,key,flatOptions=jQuery.ajaxSettings.flatOptions||{};for(key in src){if(src[key]!==undefined){(flatOptions[key]?target:(deep||(deep={})))[key]=src[key];}}
if(deep){jQuery.extend(true,target,deep);}
return target;}
jQuery.fn.load=function(url,params,callback){if(typeof url!=="string"&&_load){return _load.apply(this,arguments);}
var selector,response,type,self=this,off=url.indexOf(" ");if(off>=0){selector=url.slice(off,url.length);url=url.slice(0,off);}
if(jQuery.isFunction(params)){callback=params;params=undefined;}else if(params&&typeof params==="object"){type="POST";}
if(self.length>0){jQuery.ajax({url:url,type:type,dataType:"html",data:params}).done(function(responseText){response=arguments;self.html(selector?jQuery("<div>").append(jQuery.parseHTML(responseText)).find(selector):responseText);}).complete(callback&&function(jqXHR,status){self.each(callback,response||[jqXHR.responseText,status,jqXHR]);});}
return this;};jQuery.each(["ajaxStart","ajaxStop","ajaxComplete","ajaxError","ajaxSuccess","ajaxSend"],function(i,type){jQuery.fn[type]=function(fn){return this.on(type,fn);};});jQuery.each(["get","post"],function(i,method){jQuery[method]=function(url,data,callback,type){if(jQuery.isFunction(data)){type=type||callback;callback=data;data=undefined;}
return jQuery.ajax({url:url,type:method,dataType:type,data:data,success:callback});};});jQuery.extend({active:0,lastModified:{},etag:{},ajaxSettings:{url:ajaxLocation,type:"GET",isLocal:rlocalProtocol.test(ajaxLocParts[1]),global:true,processData:true,async:true,contentType:"application/x-www-form-urlencoded; charset=UTF-8",accepts:{"*":allTypes,text:"text/plain",html:"text/html",xml:"application/xml, text/xml",json:"application/json, text/javascript"},contents:{xml:/xml/,html:/html/,json:/json/},responseFields:{xml:"responseXML",text:"responseText"},converters:{"* text":window.String,"text html":true,"text json":jQuery.parseJSON,"text xml":jQuery.parseXML},flatOptions:{url:true,context:true}},ajaxSetup:function(target,settings){return settings?ajaxExtend(ajaxExtend(target,jQuery.ajaxSettings),settings):ajaxExtend(jQuery.ajaxSettings,target);},ajaxPrefilter:addToPrefiltersOrTransports(prefilters),ajaxTransport:addToPrefiltersOrTransports(transports),ajax:function(url,options){if(typeof url==="object"){options=url;url=undefined;}
options=options||{};var
parts,i,cacheURL,responseHeadersString,timeoutTimer,fireGlobals,transport,responseHeaders,s=jQuery.ajaxSetup({},options),callbackContext=s.context||s,globalEventContext=s.context&&(callbackContext.nodeType||callbackContext.jquery)?jQuery(callbackContext):jQuery.event,deferred=jQuery.Deferred(),completeDeferred=jQuery.Callbacks("once memory"),statusCode=s.statusCode||{},requestHeaders={},requestHeadersNames={},state=0,strAbort="canceled",jqXHR={readyState:0,getResponseHeader:function(key){var match;if(state===2){if(!responseHeaders){responseHeaders={};while((match=rheaders.exec(responseHeadersString))){responseHeaders[match[1].toLowerCase()]=match[2];}}
match=responseHeaders[key.toLowerCase()];}
return match==null?null:match;},getAllResponseHeaders:function(){return state===2?responseHeadersString:null;},setRequestHeader:function(name,value){var lname=name.toLowerCase();if(!state){name=requestHeadersNames[lname]=requestHeadersNames[lname]||name;requestHeaders[name]=value;}
return this;},overrideMimeType:function(type){if(!state){s.mimeType=type;}
return this;},statusCode:function(map){var code;if(map){if(state<2){for(code in map){statusCode[code]=[statusCode[code],map[code]];}}else{jqXHR.always(map[jqXHR.status]);}}
return this;},abort:function(statusText){var finalText=statusText||strAbort;if(transport){transport.abort(finalText);}
done(0,finalText);return this;}};deferred.promise(jqXHR).complete=completeDeferred.add;jqXHR.success=jqXHR.done;jqXHR.error=jqXHR.fail;s.url=((url||s.url||ajaxLocation)+"").replace(rhash,"").replace(rprotocol,ajaxLocParts[1]+"//");s.type=options.method||options.type||s.method||s.type;s.dataTypes=jQuery.trim(s.dataType||"*").toLowerCase().match(core_rnotwhite)||[""];if(s.crossDomain==null){parts=rurl.exec(s.url.toLowerCase());s.crossDomain=!!(parts&&(parts[1]!==ajaxLocParts[1]||parts[2]!==ajaxLocParts[2]||(parts[3]||(parts[1]==="http:"?80:443))!=(ajaxLocParts[3]||(ajaxLocParts[1]==="http:"?80:443))));}
if(s.data&&s.processData&&typeof s.data!=="string"){s.data=jQuery.param(s.data,s.traditional);}
inspectPrefiltersOrTransports(prefilters,s,options,jqXHR);if(state===2){return jqXHR;}
fireGlobals=s.global;if(fireGlobals&&jQuery.active++===0){jQuery.event.trigger("ajaxStart");}
s.type=s.type.toUpperCase();s.hasContent=!rnoContent.test(s.type);cacheURL=s.url;if(!s.hasContent){if(s.data){cacheURL=(s.url+=(ajax_rquery.test(cacheURL)?"&":"?")+s.data);delete s.data;}
if(s.cache===false){s.url=rts.test(cacheURL)?cacheURL.replace(rts,"$1_="+ajax_nonce++):cacheURL+(ajax_rquery.test(cacheURL)?"&":"?")+"_="+ajax_nonce++;}}
if(s.ifModified){if(jQuery.lastModified[cacheURL]){jqXHR.setRequestHeader("If-Modified-Since",jQuery.lastModified[cacheURL]);}
if(jQuery.etag[cacheURL]){jqXHR.setRequestHeader("If-None-Match",jQuery.etag[cacheURL]);}}
if(s.data&&s.hasContent&&s.contentType!==false||options.contentType){jqXHR.setRequestHeader("Content-Type",s.contentType);}
jqXHR.setRequestHeader("Accept",s.dataTypes[0]&&s.accepts[s.dataTypes[0]]?s.accepts[s.dataTypes[0]]+(s.dataTypes[0]!=="*"?", "+allTypes+"; q=0.01":""):s.accepts["*"]);for(i in s.headers){jqXHR.setRequestHeader(i,s.headers[i]);}
if(s.beforeSend&&(s.beforeSend.call(callbackContext,jqXHR,s)===false||state===2)){return jqXHR.abort();}
strAbort="abort";for(i in{success:1,error:1,complete:1}){jqXHR[i](s[i]);}
transport=inspectPrefiltersOrTransports(transports,s,options,jqXHR);if(!transport){done(-1,"No Transport");}else{jqXHR.readyState=1;if(fireGlobals){globalEventContext.trigger("ajaxSend",[jqXHR,s]);}
if(s.async&&s.timeout>0){timeoutTimer=setTimeout(function(){jqXHR.abort("timeout");},s.timeout);}
try{state=1;transport.send(requestHeaders,done);}catch(e){if(state<2){done(-1,e);}else{throw e;}}}
function done(status,nativeStatusText,responses,headers){var isSuccess,success,error,response,modified,statusText=nativeStatusText;if(state===2){return;}
state=2;if(timeoutTimer){clearTimeout(timeoutTimer);}
transport=undefined;responseHeadersString=headers||"";jqXHR.readyState=status>0?4:0;if(responses){response=ajaxHandleResponses(s,jqXHR,responses);}
if(status>=200&&status<300||status===304){if(s.ifModified){modified=jqXHR.getResponseHeader("Last-Modified");if(modified){jQuery.lastModified[cacheURL]=modified;}
modified=jqXHR.getResponseHeader("etag");if(modified){jQuery.etag[cacheURL]=modified;}}
if(status===204){isSuccess=true;statusText="nocontent";}else if(status===304){isSuccess=true;statusText="notmodified";}else{isSuccess=ajaxConvert(s,response);statusText=isSuccess.state;success=isSuccess.data;error=isSuccess.error;isSuccess=!error;}}else{error=statusText;if(status||!statusText){statusText="error";if(status<0){status=0;}}}
jqXHR.status=status;jqXHR.statusText=(nativeStatusText||statusText)+"";if(isSuccess){deferred.resolveWith(callbackContext,[success,statusText,jqXHR]);}else{deferred.rejectWith(callbackContext,[jqXHR,statusText,error]);}
jqXHR.statusCode(statusCode);statusCode=undefined;if(fireGlobals){globalEventContext.trigger(isSuccess?"ajaxSuccess":"ajaxError",[jqXHR,s,isSuccess?success:error]);}
completeDeferred.fireWith(callbackContext,[jqXHR,statusText]);if(fireGlobals){globalEventContext.trigger("ajaxComplete",[jqXHR,s]);if(!(--jQuery.active)){jQuery.event.trigger("ajaxStop");}}}
return jqXHR;},getScript:function(url,callback){return jQuery.get(url,undefined,callback,"script");},getJSON:function(url,data,callback){return jQuery.get(url,data,callback,"json");}});function ajaxHandleResponses(s,jqXHR,responses){var firstDataType,ct,finalDataType,type,contents=s.contents,dataTypes=s.dataTypes,responseFields=s.responseFields;for(type in responseFields){if(type in responses){jqXHR[responseFields[type]]=responses[type];}}
while(dataTypes[0]==="*"){dataTypes.shift();if(ct===undefined){ct=s.mimeType||jqXHR.getResponseHeader("Content-Type");}}
if(ct){for(type in contents){if(contents[type]&&contents[type].test(ct)){dataTypes.unshift(type);break;}}}
if(dataTypes[0]in responses){finalDataType=dataTypes[0];}else{for(type in responses){if(!dataTypes[0]||s.converters[type+" "+dataTypes[0]]){finalDataType=type;break;}
if(!firstDataType){firstDataType=type;}}
finalDataType=finalDataType||firstDataType;}
if(finalDataType){if(finalDataType!==dataTypes[0]){dataTypes.unshift(finalDataType);}
return responses[finalDataType];}}
function ajaxConvert(s,response){var conv2,current,conv,tmp,converters={},i=0,dataTypes=s.dataTypes.slice(),prev=dataTypes[0];if(s.dataFilter){response=s.dataFilter(response,s.dataType);}
if(dataTypes[1]){for(conv in s.converters){converters[conv.toLowerCase()]=s.converters[conv];}}
for(;(current=dataTypes[++i]);){if(current!=="*"){if(prev!=="*"&&prev!==current){conv=converters[prev+" "+current]||converters["* "+current];if(!conv){for(conv2 in converters){tmp=conv2.split(" ");if(tmp[1]===current){conv=converters[prev+" "+tmp[0]]||converters["* "+tmp[0]];if(conv){if(conv===true){conv=converters[conv2];}else if(converters[conv2]!==true){current=tmp[0];dataTypes.splice(i--,0,current);}
break;}}}}
if(conv!==true){if(conv&&s["throws"]){response=conv(response);}else{try{response=conv(response);}catch(e){return{state:"parsererror",error:conv?e:"No conversion from "+prev+" to "+current};}}}}
prev=current;}}
return{state:"success",data:response};}
jQuery.ajaxSetup({accepts:{script:"text/javascript, application/javascript, application/ecmascript, application/x-ecmascript"},contents:{script:/(?:java|ecma)script/},converters:{"text script":function(text){jQuery.globalEval(text);return text;}}});jQuery.ajaxPrefilter("script",function(s){if(s.cache===undefined){s.cache=false;}
if(s.crossDomain){s.type="GET";s.global=false;}});jQuery.ajaxTransport("script",function(s){if(s.crossDomain){var script,head=document.head||jQuery("head")[0]||document.documentElement;return{send:function(_,callback){script=document.createElement("script");script.async=true;if(s.scriptCharset){script.charset=s.scriptCharset;}
script.src=s.url;script.onload=script.onreadystatechange=function(_,isAbort){if(isAbort||!script.readyState||/loaded|complete/.test(script.readyState)){script.onload=script.onreadystatechange=null;if(script.parentNode){script.parentNode.removeChild(script);}
script=null;if(!isAbort){callback(200,"success");}}};head.insertBefore(script,head.firstChild);},abort:function(){if(script){script.onload(undefined,true);}}};}});var oldCallbacks=[],rjsonp=/(=)\?(?=&|$)|\?\?/;jQuery.ajaxSetup({jsonp:"callback",jsonpCallback:function(){var callback=oldCallbacks.pop()||(jQuery.expando+"_"+(ajax_nonce++));this[callback]=true;return callback;}});jQuery.ajaxPrefilter("json jsonp",function(s,originalSettings,jqXHR){var callbackName,overwritten,responseContainer,jsonProp=s.jsonp!==false&&(rjsonp.test(s.url)?"url":typeof s.data==="string"&&!(s.contentType||"").indexOf("application/x-www-form-urlencoded")&&rjsonp.test(s.data)&&"data");if(jsonProp||s.dataTypes[0]==="jsonp"){callbackName=s.jsonpCallback=jQuery.isFunction(s.jsonpCallback)?s.jsonpCallback():s.jsonpCallback;if(jsonProp){s[jsonProp]=s[jsonProp].replace(rjsonp,"$1"+callbackName);}else if(s.jsonp!==false){s.url+=(ajax_rquery.test(s.url)?"&":"?")+s.jsonp+"="+callbackName;}
s.converters["script json"]=function(){if(!responseContainer){jQuery.error(callbackName+" was not called");}
return responseContainer[0];};s.dataTypes[0]="json";overwritten=window[callbackName];window[callbackName]=function(){responseContainer=arguments;};jqXHR.always(function(){window[callbackName]=overwritten;if(s[callbackName]){s.jsonpCallback=originalSettings.jsonpCallback;oldCallbacks.push(callbackName);}
if(responseContainer&&jQuery.isFunction(overwritten)){overwritten(responseContainer[0]);}
responseContainer=overwritten=undefined;});return"script";}});var xhrCallbacks,xhrSupported,xhrId=0,xhrOnUnloadAbort=window.ActiveXObject&&function(){var key;for(key in xhrCallbacks){xhrCallbacks[key](undefined,true);}};function createStandardXHR(){try{return new window.XMLHttpRequest();}catch(e){}}
function createActiveXHR(){try{return new window.ActiveXObject("Microsoft.XMLHTTP");}catch(e){}}
jQuery.ajaxSettings.xhr=window.ActiveXObject?function(){return!this.isLocal&&createStandardXHR()||createActiveXHR();}:createStandardXHR;xhrSupported=jQuery.ajaxSettings.xhr();jQuery.support.cors=!!xhrSupported&&("withCredentials"in xhrSupported);xhrSupported=jQuery.support.ajax=!!xhrSupported;if(xhrSupported){jQuery.ajaxTransport(function(s){if(!s.crossDomain||jQuery.support.cors){var callback;return{send:function(headers,complete){var handle,i,xhr=s.xhr();if(s.username){xhr.open(s.type,s.url,s.async,s.username,s.password);}else{xhr.open(s.type,s.url,s.async);}
if(s.xhrFields){for(i in s.xhrFields){xhr[i]=s.xhrFields[i];}}
if(s.mimeType&&xhr.overrideMimeType){xhr.overrideMimeType(s.mimeType);}
if(!s.crossDomain&&!headers["X-Requested-With"]){headers["X-Requested-With"]="XMLHttpRequest";}
try{for(i in headers){xhr.setRequestHeader(i,headers[i]);}}catch(err){}
xhr.send((s.hasContent&&s.data)||null);callback=function(_,isAbort){var status,responseHeaders,statusText,responses;try{if(callback&&(isAbort||xhr.readyState===4)){callback=undefined;if(handle){xhr.onreadystatechange=jQuery.noop;if(xhrOnUnloadAbort){delete xhrCallbacks[handle];}}
if(isAbort){if(xhr.readyState!==4){xhr.abort();}}else{responses={};status=xhr.status;responseHeaders=xhr.getAllResponseHeaders();if(typeof xhr.responseText==="string"){responses.text=xhr.responseText;}
try{statusText=xhr.statusText;}catch(e){statusText="";}
if(!status&&s.isLocal&&!s.crossDomain){status=responses.text?200:404;}else if(status===1223){status=204;}}}}catch(firefoxAccessException){if(!isAbort){complete(-1,firefoxAccessException);}}
if(responses){complete(status,statusText,responses,responseHeaders);}};if(!s.async){callback();}else if(xhr.readyState===4){setTimeout(callback);}else{handle=++xhrId;if(xhrOnUnloadAbort){if(!xhrCallbacks){xhrCallbacks={};jQuery(window).unload(xhrOnUnloadAbort);}
xhrCallbacks[handle]=callback;}
xhr.onreadystatechange=callback;}},abort:function(){if(callback){callback(undefined,true);}}};}});}
var fxNow,timerId,rfxtypes=/^(?:toggle|show|hide)$/,rfxnum=new RegExp("^(?:([+-])=|)("+core_pnum+")([a-z%]*)$","i"),rrun=/queueHooks$/,animationPrefilters=[defaultPrefilter],tweeners={"*":[function(prop,value){var end,unit,tween=this.createTween(prop,value),parts=rfxnum.exec(value),target=tween.cur(),start=+target||0,scale=1,maxIterations=20;if(parts){end=+parts[2];unit=parts[3]||(jQuery.cssNumber[prop]?"":"px");if(unit!=="px"&&start){start=jQuery.css(tween.elem,prop,true)||end||1;do{scale=scale||".5";start=start/scale;jQuery.style(tween.elem,prop,start+unit);}while(scale!==(scale=tween.cur()/target)&&scale!==1&&--maxIterations);}
tween.unit=unit;tween.start=start;tween.end=parts[1]?start+(parts[1]+1)*end:end;}
return tween;}]};function createFxNow(){setTimeout(function(){fxNow=undefined;});return(fxNow=jQuery.now());}
function createTweens(animation,props){jQuery.each(props,function(prop,value){var collection=(tweeners[prop]||[]).concat(tweeners["*"]),index=0,length=collection.length;for(;index<length;index++){if(collection[index].call(animation,prop,value)){return;}}});}
function Animation(elem,properties,options){var result,stopped,index=0,length=animationPrefilters.length,deferred=jQuery.Deferred().always(function(){delete tick.elem;}),tick=function(){if(stopped){return false;}
var currentTime=fxNow||createFxNow(),remaining=Math.max(0,animation.startTime+animation.duration-currentTime),temp=remaining/animation.duration||0,percent=1-temp,index=0,length=animation.tweens.length;for(;index<length;index++){animation.tweens[index].run(percent);}
deferred.notifyWith(elem,[animation,percent,remaining]);if(percent<1&&length){return remaining;}else{deferred.resolveWith(elem,[animation]);return false;}},animation=deferred.promise({elem:elem,props:jQuery.extend({},properties),opts:jQuery.extend(true,{specialEasing:{}},options),originalProperties:properties,originalOptions:options,startTime:fxNow||createFxNow(),duration:options.duration,tweens:[],createTween:function(prop,end){var tween=jQuery.Tween(elem,animation.opts,prop,end,animation.opts.specialEasing[prop]||animation.opts.easing);animation.tweens.push(tween);return tween;},stop:function(gotoEnd){var index=0,length=gotoEnd?animation.tweens.length:0;if(stopped){return this;}
stopped=true;for(;index<length;index++){animation.tweens[index].run(1);}
if(gotoEnd){deferred.resolveWith(elem,[animation,gotoEnd]);}else{deferred.rejectWith(elem,[animation,gotoEnd]);}
return this;}}),props=animation.props;propFilter(props,animation.opts.specialEasing);for(;index<length;index++){result=animationPrefilters[index].call(animation,elem,props,animation.opts);if(result){return result;}}
createTweens(animation,props);if(jQuery.isFunction(animation.opts.start)){animation.opts.start.call(elem,animation);}
jQuery.fx.timer(jQuery.extend(tick,{elem:elem,anim:animation,queue:animation.opts.queue}));return animation.progress(animation.opts.progress).done(animation.opts.done,animation.opts.complete).fail(animation.opts.fail).always(animation.opts.always);}
function propFilter(props,specialEasing){var value,name,index,easing,hooks;for(index in props){name=jQuery.camelCase(index);easing=specialEasing[name];value=props[index];if(jQuery.isArray(value)){easing=value[1];value=props[index]=value[0];}
if(index!==name){props[name]=value;delete props[index];}
hooks=jQuery.cssHooks[name];if(hooks&&"expand"in hooks){value=hooks.expand(value);delete props[name];for(index in value){if(!(index in props)){props[index]=value[index];specialEasing[index]=easing;}}}else{specialEasing[name]=easing;}}}
jQuery.Animation=jQuery.extend(Animation,{tweener:function(props,callback){if(jQuery.isFunction(props)){callback=props;props=["*"];}else{props=props.split(" ");}
var prop,index=0,length=props.length;for(;index<length;index++){prop=props[index];tweeners[prop]=tweeners[prop]||[];tweeners[prop].unshift(callback);}},prefilter:function(callback,prepend){if(prepend){animationPrefilters.unshift(callback);}else{animationPrefilters.push(callback);}}});function defaultPrefilter(elem,props,opts){var prop,index,length,value,dataShow,toggle,tween,hooks,oldfire,anim=this,style=elem.style,orig={},handled=[],hidden=elem.nodeType&&isHidden(elem);if(!opts.queue){hooks=jQuery._queueHooks(elem,"fx");if(hooks.unqueued==null){hooks.unqueued=0;oldfire=hooks.empty.fire;hooks.empty.fire=function(){if(!hooks.unqueued){oldfire();}};}
hooks.unqueued++;anim.always(function(){anim.always(function(){hooks.unqueued--;if(!jQuery.queue(elem,"fx").length){hooks.empty.fire();}});});}
if(elem.nodeType===1&&("height"in props||"width"in props)){opts.overflow=[style.overflow,style.overflowX,style.overflowY];if(jQuery.css(elem,"display")==="inline"&&jQuery.css(elem,"float")==="none"){if(!jQuery.support.inlineBlockNeedsLayout||css_defaultDisplay(elem.nodeName)==="inline"){style.display="inline-block";}else{style.zoom=1;}}}
if(opts.overflow){style.overflow="hidden";if(!jQuery.support.shrinkWrapBlocks){anim.always(function(){style.overflow=opts.overflow[0];style.overflowX=opts.overflow[1];style.overflowY=opts.overflow[2];});}}
for(index in props){value=props[index];if(rfxtypes.exec(value)){delete props[index];toggle=toggle||value==="toggle";if(value===(hidden?"hide":"show")){continue;}
handled.push(index);}}
length=handled.length;if(length){dataShow=jQuery._data(elem,"fxshow")||jQuery._data(elem,"fxshow",{});if("hidden"in dataShow){hidden=dataShow.hidden;}
if(toggle){dataShow.hidden=!hidden;}
if(hidden){jQuery(elem).show();}else{anim.done(function(){jQuery(elem).hide();});}
anim.done(function(){var prop;jQuery._removeData(elem,"fxshow");for(prop in orig){jQuery.style(elem,prop,orig[prop]);}});for(index=0;index<length;index++){prop=handled[index];tween=anim.createTween(prop,hidden?dataShow[prop]:0);orig[prop]=dataShow[prop]||jQuery.style(elem,prop);if(!(prop in dataShow)){dataShow[prop]=tween.start;if(hidden){tween.end=tween.start;tween.start=prop==="width"||prop==="height"?1:0;}}}}}
function Tween(elem,options,prop,end,easing){return new Tween.prototype.init(elem,options,prop,end,easing);}
jQuery.Tween=Tween;Tween.prototype={constructor:Tween,init:function(elem,options,prop,end,easing,unit){this.elem=elem;this.prop=prop;this.easing=easing||"swing";this.options=options;this.start=this.now=this.cur();this.end=end;this.unit=unit||(jQuery.cssNumber[prop]?"":"px");},cur:function(){var hooks=Tween.propHooks[this.prop];return hooks&&hooks.get?hooks.get(this):Tween.propHooks._default.get(this);},run:function(percent){var eased,hooks=Tween.propHooks[this.prop];if(this.options.duration){this.pos=eased=jQuery.easing[this.easing](percent,this.options.duration*percent,0,1,this.options.duration);}else{this.pos=eased=percent;}
this.now=(this.end-this.start)*eased+this.start;if(this.options.step){this.options.step.call(this.elem,this.now,this);}
if(hooks&&hooks.set){hooks.set(this);}else{Tween.propHooks._default.set(this);}
return this;}};Tween.prototype.init.prototype=Tween.prototype;Tween.propHooks={_default:{get:function(tween){var result;if(tween.elem[tween.prop]!=null&&(!tween.elem.style||tween.elem.style[tween.prop]==null)){return tween.elem[tween.prop];}
result=jQuery.css(tween.elem,tween.prop,"");return!result||result==="auto"?0:result;},set:function(tween){if(jQuery.fx.step[tween.prop]){jQuery.fx.step[tween.prop](tween);}else if(tween.elem.style&&(tween.elem.style[jQuery.cssProps[tween.prop]]!=null||jQuery.cssHooks[tween.prop])){jQuery.style(tween.elem,tween.prop,tween.now+tween.unit);}else{tween.elem[tween.prop]=tween.now;}}}};Tween.propHooks.scrollTop=Tween.propHooks.scrollLeft={set:function(tween){if(tween.elem.nodeType&&tween.elem.parentNode){tween.elem[tween.prop]=tween.now;}}};jQuery.each(["toggle","show","hide"],function(i,name){var cssFn=jQuery.fn[name];jQuery.fn[name]=function(speed,easing,callback){return speed==null||typeof speed==="boolean"?cssFn.apply(this,arguments):this.animate(genFx(name,true),speed,easing,callback);};});jQuery.fn.extend({fadeTo:function(speed,to,easing,callback){return this.filter(isHidden).css("opacity",0).show().end().animate({opacity:to},speed,easing,callback);},animate:function(prop,speed,easing,callback){var empty=jQuery.isEmptyObject(prop),optall=jQuery.speed(speed,easing,callback),doAnimation=function(){var anim=Animation(this,jQuery.extend({},prop),optall);doAnimation.finish=function(){anim.stop(true);};if(empty||jQuery._data(this,"finish")){anim.stop(true);}};doAnimation.finish=doAnimation;return empty||optall.queue===false?this.each(doAnimation):this.queue(optall.queue,doAnimation);},stop:function(type,clearQueue,gotoEnd){var stopQueue=function(hooks){var stop=hooks.stop;delete hooks.stop;stop(gotoEnd);};if(typeof type!=="string"){gotoEnd=clearQueue;clearQueue=type;type=undefined;}
if(clearQueue&&type!==false){this.queue(type||"fx",[]);}
return this.each(function(){var dequeue=true,index=type!=null&&type+"queueHooks",timers=jQuery.timers,data=jQuery._data(this);if(index){if(data[index]&&data[index].stop){stopQueue(data[index]);}}else{for(index in data){if(data[index]&&data[index].stop&&rrun.test(index)){stopQueue(data[index]);}}}
for(index=timers.length;index--;){if(timers[index].elem===this&&(type==null||timers[index].queue===type)){timers[index].anim.stop(gotoEnd);dequeue=false;timers.splice(index,1);}}
if(dequeue||!gotoEnd){jQuery.dequeue(this,type);}});},finish:function(type){if(type!==false){type=type||"fx";}
return this.each(function(){var index,data=jQuery._data(this),queue=data[type+"queue"],hooks=data[type+"queueHooks"],timers=jQuery.timers,length=queue?queue.length:0;data.finish=true;jQuery.queue(this,type,[]);if(hooks&&hooks.cur&&hooks.cur.finish){hooks.cur.finish.call(this);}
for(index=timers.length;index--;){if(timers[index].elem===this&&timers[index].queue===type){timers[index].anim.stop(true);timers.splice(index,1);}}
for(index=0;index<length;index++){if(queue[index]&&queue[index].finish){queue[index].finish.call(this);}}
delete data.finish;});}});function genFx(type,includeWidth){var which,attrs={height:type},i=0;includeWidth=includeWidth?1:0;for(;i<4;i+=2-includeWidth){which=cssExpand[i];attrs["margin"+which]=attrs["padding"+which]=type;}
if(includeWidth){attrs.opacity=attrs.width=type;}
return attrs;}
jQuery.each({slideDown:genFx("show"),slideUp:genFx("hide"),slideToggle:genFx("toggle"),fadeIn:{opacity:"show"},fadeOut:{opacity:"hide"},fadeToggle:{opacity:"toggle"}},function(name,props){jQuery.fn[name]=function(speed,easing,callback){return this.animate(props,speed,easing,callback);};});jQuery.speed=function(speed,easing,fn){var opt=speed&&typeof speed==="object"?jQuery.extend({},speed):{complete:fn||!fn&&easing||jQuery.isFunction(speed)&&speed,duration:speed,easing:fn&&easing||easing&&!jQuery.isFunction(easing)&&easing};opt.duration=jQuery.fx.off?0:typeof opt.duration==="number"?opt.duration:opt.duration in jQuery.fx.speeds?jQuery.fx.speeds[opt.duration]:jQuery.fx.speeds._default;if(opt.queue==null||opt.queue===true){opt.queue="fx";}
opt.old=opt.complete;opt.complete=function(){if(jQuery.isFunction(opt.old)){opt.old.call(this);}
if(opt.queue){jQuery.dequeue(this,opt.queue);}};return opt;};jQuery.easing={linear:function(p){return p;},swing:function(p){return 0.5-Math.cos(p*Math.PI)/2;}};jQuery.timers=[];jQuery.fx=Tween.prototype.init;jQuery.fx.tick=function(){var timer,timers=jQuery.timers,i=0;fxNow=jQuery.now();for(;i<timers.length;i++){timer=timers[i];if(!timer()&&timers[i]===timer){timers.splice(i--,1);}}
if(!timers.length){jQuery.fx.stop();}
fxNow=undefined;};jQuery.fx.timer=function(timer){if(timer()&&jQuery.timers.push(timer)){jQuery.fx.start();}};jQuery.fx.interval=13;jQuery.fx.start=function(){if(!timerId){timerId=setInterval(jQuery.fx.tick,jQuery.fx.interval);}};jQuery.fx.stop=function(){clearInterval(timerId);timerId=null;};jQuery.fx.speeds={slow:600,fast:200,_default:400};jQuery.fx.step={};if(jQuery.expr&&jQuery.expr.filters){jQuery.expr.filters.animated=function(elem){return jQuery.grep(jQuery.timers,function(fn){return elem===fn.elem;}).length;};}
jQuery.fn.offset=function(options){if(arguments.length){return options===undefined?this:this.each(function(i){jQuery.offset.setOffset(this,options,i);});}
var docElem,win,box={top:0,left:0},elem=this[0],doc=elem&&elem.ownerDocument;if(!doc){return;}
docElem=doc.documentElement;if(!jQuery.contains(docElem,elem)){return box;}
if(typeof elem.getBoundingClientRect!==core_strundefined){box=elem.getBoundingClientRect();}
win=getWindow(doc);return{top:box.top+(win.pageYOffset||docElem.scrollTop)-(docElem.clientTop||0),left:box.left+(win.pageXOffset||docElem.scrollLeft)-(docElem.clientLeft||0)};};jQuery.offset={setOffset:function(elem,options,i){var position=jQuery.css(elem,"position");if(position==="static"){elem.style.position="relative";}
var curElem=jQuery(elem),curOffset=curElem.offset(),curCSSTop=jQuery.css(elem,"top"),curCSSLeft=jQuery.css(elem,"left"),calculatePosition=(position==="absolute"||position==="fixed")&&jQuery.inArray("auto",[curCSSTop,curCSSLeft])>-1,props={},curPosition={},curTop,curLeft;if(calculatePosition){curPosition=curElem.position();curTop=curPosition.top;curLeft=curPosition.left;}else{curTop=parseFloat(curCSSTop)||0;curLeft=parseFloat(curCSSLeft)||0;}
if(jQuery.isFunction(options)){options=options.call(elem,i,curOffset);}
if(options.top!=null){props.top=(options.top-curOffset.top)+curTop;}
if(options.left!=null){props.left=(options.left-curOffset.left)+curLeft;}
if("using"in options){options.using.call(elem,props);}else{curElem.css(props);}}};jQuery.fn.extend({position:function(){if(!this[0]){return;}
var offsetParent,offset,parentOffset={top:0,left:0},elem=this[0];if(jQuery.css(elem,"position")==="fixed"){offset=elem.getBoundingClientRect();}else{offsetParent=this.offsetParent();offset=this.offset();if(!jQuery.nodeName(offsetParent[0],"html")){parentOffset=offsetParent.offset();}
parentOffset.top+=jQuery.css(offsetParent[0],"borderTopWidth",true);parentOffset.left+=jQuery.css(offsetParent[0],"borderLeftWidth",true);}
return{top:offset.top-parentOffset.top-jQuery.css(elem,"marginTop",true),left:offset.left-parentOffset.left-jQuery.css(elem,"marginLeft",true)};},offsetParent:function(){return this.map(function(){var offsetParent=this.offsetParent||document.documentElement;while(offsetParent&&(!jQuery.nodeName(offsetParent,"html")&&jQuery.css(offsetParent,"position")==="static")){offsetParent=offsetParent.offsetParent;}
return offsetParent||document.documentElement;});}});jQuery.each({scrollLeft:"pageXOffset",scrollTop:"pageYOffset"},function(method,prop){var top=/Y/.test(prop);jQuery.fn[method]=function(val){return jQuery.access(this,function(elem,method,val){var win=getWindow(elem);if(val===undefined){return win?(prop in win)?win[prop]:win.document.documentElement[method]:elem[method];}
if(win){win.scrollTo(!top?val:jQuery(win).scrollLeft(),top?val:jQuery(win).scrollTop());}else{elem[method]=val;}},method,val,arguments.length,null);};});function getWindow(elem){return jQuery.isWindow(elem)?elem:elem.nodeType===9?elem.defaultView||elem.parentWindow:false;}
jQuery.each({Height:"height",Width:"width"},function(name,type){jQuery.each({padding:"inner"+name,content:type,"":"outer"+name},function(defaultExtra,funcName){jQuery.fn[funcName]=function(margin,value){var chainable=arguments.length&&(defaultExtra||typeof margin!=="boolean"),extra=defaultExtra||(margin===true||value===true?"margin":"border");return jQuery.access(this,function(elem,type,value){var doc;if(jQuery.isWindow(elem)){return elem.document.documentElement["client"+name];}
if(elem.nodeType===9){doc=elem.documentElement;return Math.max(elem.body["scroll"+name],doc["scroll"+name],elem.body["offset"+name],doc["offset"+name],doc["client"+name]);}
return value===undefined?jQuery.css(elem,type,extra):jQuery.style(elem,type,value,extra);},type,chainable?margin:undefined,chainable,null);};});});window.jQuery=window.$=jQuery;if(typeof define==="function"&&define.amd&&define.amd.jQuery){define("jquery",[],function(){return jQuery;});}})(window);function getRequestURLParam(name){var lurl=window.location.href;if(lurl.indexOf(name)<0)
return;if(lurl.indexOf("?")<0){return"";}else{var par=lurl.substring(lurl.indexOf(name),lurl.length);var start=lurl.indexOf(name)+name.length+1;var len=(par.indexOf("&")>0)?lurl.indexOf(name)
+par.indexOf("&"):lurl.length;return lurl.substring(start,len);}}
Date.prototype.format=function(fmt){var o={"M+":this.getMonth()+1,"d+":this.getDate(),"h+":this.getHours()%24==0?24:this.getHours()%24,"H+":this.getHours(),"m+":this.getMinutes(),"s+":this.getSeconds(),"q+":Math.floor((this.getMonth()+3)/3),"S":this.getMilliseconds()};var week={"0":"\u65e5","1":"\u4e00","2":"\u4e8c","3":"\u4e09","4":"\u56db","5":"\u4e94","6":"\u516d"};if(/(y+)/.test(fmt)){fmt=fmt.replace(RegExp.$1,(this.getFullYear()+"").substr(4-RegExp.$1.length));}
if(/(E+)/.test(fmt)){fmt=fmt.replace(RegExp.$1,((RegExp.$1.length>1)?(RegExp.$1.length>2?"\u661f\u671f":"\u5468"):"")
+week[this.getDay()+""]);}
for(var k in o){if(new RegExp("("+k+")").test(fmt)){fmt=fmt.replace(RegExp.$1,(RegExp.$1.length==1)?(o[k]):(("00"+o[k]).substr((""+o[k]).length)));}}
return fmt;};function getParamValueFromUrl(name){var reg=new RegExp("(^|&)"+name+"=([^&]*)(&|$)","i");var r=window.location.search.substr(1).match(reg);return r!=null?unescape(r[2]):null;};function loadjscssfile(filename,filetype){if(filetype=="js"){var fileref=document.createElement('script');fileref.setAttribute("type","text/javascript");fileref.setAttribute("src",filename);}else if(filetype=="css"){var fileref=document.createElement("link");fileref.setAttribute("rel","stylesheet");fileref.setAttribute("type","text/css");fileref.setAttribute("href",filename);}
if(typeof fileref!="undefined")
document.getElementsByTagName("head")[0].appendChild(fileref);}
function enCode(text){return encodeURIComponent(encodeURIComponent(text));}
function deCode(text){return decodeURIComponent(decodeURIComponent(text));}

(function($){
	$.getBrowserInfo = function() {
		var agent = navigator.userAgent.toLowerCase();
		var regStr_ie = /msie [\d.]+;/gi;
		var regStr_ff = /firefox\/[\d.]+/gi;
		var regStr_chrome = /chrome\/[\d.]+/gi;
		var regStr_saf = /safari\/[\d.]+/gi;
		// IE
		if (agent.indexOf("msie") > 0) {
			return agent.match(regStr_ie);
		}
		// firefox
		if (agent.indexOf("firefox") > 0) {
			return agent.match(regStr_ff);
		}
		// Chrome
		if (agent.indexOf("chrome") > 0) {
			return agent.match(regStr_chrome);
		}
		// Safari
		if (agent.indexOf("safari") > 0 && agent.indexOf("chrome") < 0) {
			return agent.match(regStr_saf);
		}
	};
	var agent = navigator.userAgent.toLowerCase();
	var browser = $.getBrowserInfo();
	var verinfo = parseInt((browser + "").replace(/[^0-9.]/ig, ""));
	$.browser = {
		msie: (agent.indexOf("msie") > 0),
		chrome: (agent.indexOf("chrome") > 0),
		safari: (agent.indexOf("safari") > 0 && agent.indexOf("chrome") < 0),
		webkit: (agent.indexOf("webkit") > 0),
		version: verinfo
	};
})(jQuery);


/*
 * jQuery UI 1.7.2
 * Download by http://www.codefans.net
 * Copyright (c) 2009 AUTHORS.txt (http://jqueryui.com/about)
 * Dual licensed under the MIT (MIT-LICENSE.txt)
 * and GPL (GPL-LICENSE.txt) licenses.
 *
 * http://docs.jquery.com/UI
 */
jQuery.ui||(function(c){var i=c.fn.remove,d=c.browser.mozilla&&(parseFloat(c.browser.version)<1.9);c.ui={version:"1.7.2",plugin:{add:function(k,l,n){var m=c.ui[k].prototype;for(var j in n){m.plugins[j]=m.plugins[j]||[];m.plugins[j].push([l,n[j]])}},call:function(j,l,k){var n=j.plugins[l];if(!n||!j.element[0].parentNode){return}for(var m=0;m<n.length;m++){if(j.options[n[m][0]]){n[m][1].apply(j.element,k)}}}},contains:function(k,j){return document.compareDocumentPosition?k.compareDocumentPosition(j)&16:k!==j&&k.contains(j)},hasScroll:function(m,k){if(c(m).css("overflow")=="hidden"){return false}var j=(k&&k=="left")?"scrollLeft":"scrollTop",l=false;if(m[j]>0){return true}m[j]=1;l=(m[j]>0);m[j]=0;return l},isOverAxis:function(k,j,l){return(k>j)&&(k<(j+l))},isOver:function(o,k,n,m,j,l){return c.ui.isOverAxis(o,n,j)&&c.ui.isOverAxis(k,m,l)},keyCode:{BACKSPACE:8,CAPS_LOCK:20,COMMA:188,CONTROL:17,DELETE:46,DOWN:40,END:35,ENTER:13,ESCAPE:27,HOME:36,INSERT:45,LEFT:37,NUMPAD_ADD:107,NUMPAD_DECIMAL:110,NUMPAD_DIVIDE:111,NUMPAD_ENTER:108,NUMPAD_MULTIPLY:106,NUMPAD_SUBTRACT:109,PAGE_DOWN:34,PAGE_UP:33,PERIOD:190,RIGHT:39,SHIFT:16,SPACE:32,TAB:9,UP:38}};if(d){var f=c.attr,e=c.fn.removeAttr,h="http://www.w3.org/2005/07/aaa",a=/^aria-/,b=/^wairole:/;c.attr=function(k,j,l){var m=l!==undefined;return(j=="role"?(m?f.call(this,k,j,"wairole:"+l):(f.apply(this,arguments)||"").replace(b,"")):(a.test(j)?(m?k.setAttributeNS(h,j.replace(a,"aaa:"),l):f.call(this,k,j.replace(a,"aaa:"))):f.apply(this,arguments)))};c.fn.removeAttr=function(j){return(a.test(j)?this.each(function(){this.removeAttributeNS(h,j.replace(a,""))}):e.call(this,j))}}c.fn.extend({remove:function(){c("*",this).add(this).each(function(){c(this).triggerHandler("remove")});return i.apply(this,arguments)},enableSelection:function(){return this.attr("unselectable","off").css("MozUserSelect","").unbind("selectstart.ui")},disableSelection:function(){return this.attr("unselectable","on").css("MozUserSelect","none").bind("selectstart.ui",function(){return false})},scrollParent:function(){var j;if((c.browser.msie&&(/(static|relative)/).test(this.css("position")))||(/absolute/).test(this.css("position"))){j=this.parents().filter(function(){return(/(relative|absolute|fixed)/).test(c.curCSS(this,"position",1))&&(/(auto|scroll)/).test(c.curCSS(this,"overflow",1)+c.curCSS(this,"overflow-y",1)+c.curCSS(this,"overflow-x",1))}).eq(0)}else{j=this.parents().filter(function(){return(/(auto|scroll)/).test(c.curCSS(this,"overflow",1)+c.curCSS(this,"overflow-y",1)+c.curCSS(this,"overflow-x",1))}).eq(0)}return(/fixed/).test(this.css("position"))||!j.length?c(document):j}});c.extend(c.expr[":"],{data:function(l,k,j){return !!c.data(l,j[3])},focusable:function(k){var l=k.nodeName.toLowerCase(),j=c.attr(k,"tabindex");return(/input|select|textarea|button|object/.test(l)?!k.disabled:"a"==l||"area"==l?k.href||!isNaN(j):!isNaN(j))&&!c(k)["area"==l?"parents":"closest"](":hidden").length},tabbable:function(k){var j=c.attr(k,"tabindex");return(isNaN(j)||j>=0)&&c(k).is(":focusable")}});function g(m,n,o,l){function k(q){var p=c[m][n][q]||[];return(typeof p=="string"?p.split(/,?\s+/):p)}var j=k("getter");if(l.length==1&&typeof l[0]=="string"){j=j.concat(k("getterSetter"))}return(c.inArray(o,j)!=-1)}c.widget=function(k,j){var l=k.split(".")[0];k=k.split(".")[1];c.fn[k]=function(p){var n=(typeof p=="string"),o=Array.prototype.slice.call(arguments,1);if(n&&p.substring(0,1)=="_"){return this}if(n&&g(l,k,p,o)){var m=c.data(this[0],k);return(m?m[p].apply(m,o):undefined)}return this.each(function(){var q=c.data(this,k);(!q&&!n&&c.data(this,k,new c[l][k](this,p))._init());(q&&n&&c.isFunction(q[p])&&q[p].apply(q,o))})};c[l]=c[l]||{};c[l][k]=function(o,n){var m=this;this.namespace=l;this.widgetName=k;this.widgetEventPrefix=c[l][k].eventPrefix||k;this.widgetBaseClass=l+"-"+k;this.options=c.extend({},c.widget.defaults,c[l][k].defaults,c.metadata&&c.metadata.get(o)[k],n);this.element=c(o).bind("setData."+k,function(q,p,r){if(q.target==o){return m._setData(p,r)}}).bind("getData."+k,function(q,p){if(q.target==o){return m._getData(p)}}).bind("remove",function(){return m.destroy()})};c[l][k].prototype=c.extend({},c.widget.prototype,j);c[l][k].getterSetter="option"};c.widget.prototype={_init:function(){},destroy:function(){this.element.removeData(this.widgetName).removeClass(this.widgetBaseClass+"-disabled "+this.namespace+"-state-disabled").removeAttr("aria-disabled")},option:function(l,m){var k=l,j=this;if(typeof l=="string"){if(m===undefined){return this._getData(l)}k={};k[l]=m}c.each(k,function(n,o){j._setData(n,o)})},_getData:function(j){return this.options[j]},_setData:function(j,k){this.options[j]=k;if(j=="disabled"){this.element[k?"addClass":"removeClass"](this.widgetBaseClass+"-disabled "+this.namespace+"-state-disabled").attr("aria-disabled",k)}},enable:function(){this._setData("disabled",false)},disable:function(){this._setData("disabled",true)},_trigger:function(l,m,n){var p=this.options[l],j=(l==this.widgetEventPrefix?l:this.widgetEventPrefix+l);m=c.Event(m);m.type=j;if(m.originalEvent){for(var k=c.event.props.length,o;k;){o=c.event.props[--k];m[o]=m.originalEvent[o]}}this.element.trigger(m,n);return !(c.isFunction(p)&&p.call(this.element[0],m,n)===false||m.isDefaultPrevented())}};c.widget.defaults={disabled:false};c.ui.mouse={_mouseInit:function(){var j=this;this.element.bind("mousedown."+this.widgetName,function(k){return j._mouseDown(k)}).bind("click."+this.widgetName,function(k){if(j._preventClickEvent){j._preventClickEvent=false;k.stopImmediatePropagation();return false}});if(c.browser.msie){this._mouseUnselectable=this.element.attr("unselectable");this.element.attr("unselectable","on")}this.started=false},_mouseDestroy:function(){this.element.unbind("."+this.widgetName);(c.browser.msie&&this.element.attr("unselectable",this._mouseUnselectable))},_mouseDown:function(l){l.originalEvent=l.originalEvent||{};if(l.originalEvent.mouseHandled){return}(this._mouseStarted&&this._mouseUp(l));this._mouseDownEvent=l;var k=this,m=(l.which==1),j=(typeof this.options.cancel=="string"?c(l.target).parents().add(l.target).filter(this.options.cancel).length:false);if(!m||j||!this._mouseCapture(l)){return true}this.mouseDelayMet=!this.options.delay;if(!this.mouseDelayMet){this._mouseDelayTimer=setTimeout(function(){k.mouseDelayMet=true},this.options.delay)}if(this._mouseDistanceMet(l)&&this._mouseDelayMet(l)){this._mouseStarted=(this._mouseStart(l)!==false);if(!this._mouseStarted){l.preventDefault();return true}}this._mouseMoveDelegate=function(n){return k._mouseMove(n)};this._mouseUpDelegate=function(n){return k._mouseUp(n)};c(document).bind("mousemove."+this.widgetName,this._mouseMoveDelegate).bind("mouseup."+this.widgetName,this._mouseUpDelegate);(c.browser.safari||l.preventDefault());l.originalEvent.mouseHandled=true;return true},_mouseMove:function(j){if(c.browser.msie&&!j.button){return this._mouseUp(j)}if(this._mouseStarted){this._mouseDrag(j);return j.preventDefault()}if(this._mouseDistanceMet(j)&&this._mouseDelayMet(j)){this._mouseStarted=(this._mouseStart(this._mouseDownEvent,j)!==false);(this._mouseStarted?this._mouseDrag(j):this._mouseUp(j))}return !this._mouseStarted},_mouseUp:function(j){c(document).unbind("mousemove."+this.widgetName,this._mouseMoveDelegate).unbind("mouseup."+this.widgetName,this._mouseUpDelegate);if(this._mouseStarted){this._mouseStarted=false;this._preventClickEvent=(j.target==this._mouseDownEvent.target);this._mouseStop(j)}return false},_mouseDistanceMet:function(j){return(Math.max(Math.abs(this._mouseDownEvent.pageX-j.pageX),Math.abs(this._mouseDownEvent.pageY-j.pageY))>=this.options.distance)},_mouseDelayMet:function(j){return this.mouseDelayMet},_mouseStart:function(j){},_mouseDrag:function(j){},_mouseStop:function(j){},_mouseCapture:function(j){return true}};c.ui.mouse.defaults={cancel:null,distance:1,delay:0}})(jQuery);

/*
 * jQuery UI Sortable 1.7.2
 * Download by http://www.codefans.net
 * Copyright (c) 2009 AUTHORS.txt (http://jqueryui.com/about)
 * Dual licensed under the MIT (MIT-LICENSE.txt)
 * and GPL (GPL-LICENSE.txt) licenses.
 *
 * http://docs.jquery.com/UI/Sortables
 *
 * Depends:
 *	ui.core.js
 */
(function(a){a.widget("ui.sortable",a.extend({},a.ui.mouse,{_init:function(){var b=this.options;this.containerCache={};this.element.addClass("ui-sortable");this.refresh();this.floating=this.items.length?(/left|right/).test(this.items[0].item.css("float")):false;this.offset=this.element.offset();this._mouseInit()},destroy:function(){this.element.removeClass("ui-sortable ui-sortable-disabled").removeData("sortable").unbind(".sortable");this._mouseDestroy();for(var b=this.items.length-1;b>=0;b--){this.items[b].item.removeData("sortable-item")}},_mouseCapture:function(e,f){if(this.reverting){return false}if(this.options.disabled||this.options.type=="static"){return false}this._refreshItems(e);var d=null,c=this,b=a(e.target).parents().each(function(){if(a.data(this,"sortable-item")==c){d=a(this);return false}});if(a.data(e.target,"sortable-item")==c){d=a(e.target)}if(!d){return false}if(this.options.handle&&!f){var g=false;a(this.options.handle,d).find("*").andSelf().each(function(){if(this==e.target){g=true}});if(!g){return false}}this.currentItem=d;this._removeCurrentsFromItems();return true},_mouseStart:function(e,f,b){var g=this.options,c=this;this.currentContainer=this;this.refreshPositions();this.helper=this._createHelper(e);this._cacheHelperProportions();this._cacheMargins();this.scrollParent=this.helper.scrollParent();this.offset=this.currentItem.offset();this.offset={top:this.offset.top-this.margins.top,left:this.offset.left-this.margins.left};this.helper.css("position","absolute");this.cssPosition=this.helper.css("position");a.extend(this.offset,{click:{left:e.pageX-this.offset.left,top:e.pageY-this.offset.top},parent:this._getParentOffset(),relative:this._getRelativeOffset()});this.originalPosition=this._generatePosition(e);this.originalPageX=e.pageX;this.originalPageY=e.pageY;if(g.cursorAt){this._adjustOffsetFromHelper(g.cursorAt)}this.domPosition={prev:this.currentItem.prev()[0],parent:this.currentItem.parent()[0]};if(this.helper[0]!=this.currentItem[0]){this.currentItem.hide()}this._createPlaceholder();if(g.containment){this._setContainment()}if(g.cursor){if(a("body").css("cursor")){this._storedCursor=a("body").css("cursor")}a("body").css("cursor",g.cursor)}if(g.opacity){if(this.helper.css("opacity")){this._storedOpacity=this.helper.css("opacity")}this.helper.css("opacity",g.opacity)}if(g.zIndex){if(this.helper.css("zIndex")){this._storedZIndex=this.helper.css("zIndex")}this.helper.css("zIndex",g.zIndex)}if(this.scrollParent[0]!=document&&this.scrollParent[0].tagName!="HTML"){this.overflowOffset=this.scrollParent.offset()}this._trigger("start",e,this._uiHash());if(!this._preserveHelperProportions){this._cacheHelperProportions()}if(!b){for(var d=this.containers.length-1;d>=0;d--){this.containers[d]._trigger("activate",e,c._uiHash(this))}}if(a.ui.ddmanager){a.ui.ddmanager.current=this}if(a.ui.ddmanager&&!g.dropBehaviour){a.ui.ddmanager.prepareOffsets(this,e)}this.dragging=true;this.helper.addClass("ui-sortable-helper");this._mouseDrag(e);return true},_mouseDrag:function(f){this.position=this._generatePosition(f);this.positionAbs=this._convertPositionTo("absolute");if(!this.lastPositionAbs){this.lastPositionAbs=this.positionAbs}if(this.options.scroll){var g=this.options,b=false;if(this.scrollParent[0]!=document&&this.scrollParent[0].tagName!="HTML"){if((this.overflowOffset.top+this.scrollParent[0].offsetHeight)-f.pageY<g.scrollSensitivity){this.scrollParent[0].scrollTop=b=this.scrollParent[0].scrollTop+g.scrollSpeed}else{if(f.pageY-this.overflowOffset.top<g.scrollSensitivity){this.scrollParent[0].scrollTop=b=this.scrollParent[0].scrollTop-g.scrollSpeed}}if((this.overflowOffset.left+this.scrollParent[0].offsetWidth)-f.pageX<g.scrollSensitivity){this.scrollParent[0].scrollLeft=b=this.scrollParent[0].scrollLeft+g.scrollSpeed}else{if(f.pageX-this.overflowOffset.left<g.scrollSensitivity){this.scrollParent[0].scrollLeft=b=this.scrollParent[0].scrollLeft-g.scrollSpeed}}}else{if(f.pageY-a(document).scrollTop()<g.scrollSensitivity){b=a(document).scrollTop(a(document).scrollTop()-g.scrollSpeed)}else{if(a(window).height()-(f.pageY-a(document).scrollTop())<g.scrollSensitivity){b=a(document).scrollTop(a(document).scrollTop()+g.scrollSpeed)}}if(f.pageX-a(document).scrollLeft()<g.scrollSensitivity){b=a(document).scrollLeft(a(document).scrollLeft()-g.scrollSpeed)}else{if(a(window).width()-(f.pageX-a(document).scrollLeft())<g.scrollSensitivity){b=a(document).scrollLeft(a(document).scrollLeft()+g.scrollSpeed)}}}if(b!==false&&a.ui.ddmanager&&!g.dropBehaviour){a.ui.ddmanager.prepareOffsets(this,f)}}this.positionAbs=this._convertPositionTo("absolute");if(!this.options.axis||this.options.axis!="y"){this.helper[0].style.left=this.position.left+"px"}if(!this.options.axis||this.options.axis!="x"){this.helper[0].style.top=this.position.top+"px"}for(var d=this.items.length-1;d>=0;d--){var e=this.items[d],c=e.item[0],h=this._intersectsWithPointer(e);if(!h){continue}if(c!=this.currentItem[0]&&this.placeholder[h==1?"next":"prev"]()[0]!=c&&!a.ui.contains(this.placeholder[0],c)&&(this.options.type=="semi-dynamic"?!a.ui.contains(this.element[0],c):true)){this.direction=h==1?"down":"up";if(this.options.tolerance=="pointer"||this._intersectsWithSides(e)){this._rearrange(f,e)}else{break}this._trigger("change",f,this._uiHash());break}}this._contactContainers(f);if(a.ui.ddmanager){a.ui.ddmanager.drag(this,f)}this._trigger("sort",f,this._uiHash());this.lastPositionAbs=this.positionAbs;return false},_mouseStop:function(c,d){if(!c){return}if(a.ui.ddmanager&&!this.options.dropBehaviour){a.ui.ddmanager.drop(this,c)}if(this.options.revert){var b=this;var e=b.placeholder.offset();b.reverting=true;a(this.helper).animate({left:e.left-this.offset.parent.left-b.margins.left+(this.offsetParent[0]==document.body?0:this.offsetParent[0].scrollLeft),top:e.top-this.offset.parent.top-b.margins.top+(this.offsetParent[0]==document.body?0:this.offsetParent[0].scrollTop)},parseInt(this.options.revert,10)||500,function(){b._clear(c)})}else{this._clear(c,d)}return false},cancel:function(){var b=this;if(this.dragging){this._mouseUp();if(this.options.helper=="original"){this.currentItem.css(this._storedCSS).removeClass("ui-sortable-helper")}else{this.currentItem.show()}for(var c=this.containers.length-1;c>=0;c--){this.containers[c]._trigger("deactivate",null,b._uiHash(this));if(this.containers[c].containerCache.over){this.containers[c]._trigger("out",null,b._uiHash(this));this.containers[c].containerCache.over=0}}}if(this.placeholder[0].parentNode){this.placeholder[0].parentNode.removeChild(this.placeholder[0])}if(this.options.helper!="original"&&this.helper&&this.helper[0].parentNode){this.helper.remove()}a.extend(this,{helper:null,dragging:false,reverting:false,_noFinalSort:null});if(this.domPosition.prev){a(this.domPosition.prev).after(this.currentItem)}else{a(this.domPosition.parent).prepend(this.currentItem)}return true},serialize:function(d){var b=this._getItemsAsjQuery(d&&d.connected);var c=[];d=d||{};a(b).each(function(){var e=(a(d.item||this).attr(d.attribute||"id")||"").match(d.expression||(/(.+)[-=_](.+)/));if(e){c.push((d.key||e[1]+"[]")+"="+(d.key&&d.expression?e[1]:e[2]))}});return c.join("&")},toArray:function(d){var b=this._getItemsAsjQuery(d&&d.connected);var c=[];d=d||{};b.each(function(){c.push(a(d.item||this).attr(d.attribute||"id")||"")});return c},_intersectsWith:function(m){var e=this.positionAbs.left,d=e+this.helperProportions.width,k=this.positionAbs.top,j=k+this.helperProportions.height;var f=m.left,c=f+m.width,n=m.top,i=n+m.height;var o=this.offset.click.top,h=this.offset.click.left;var g=(k+o)>n&&(k+o)<i&&(e+h)>f&&(e+h)<c;if(this.options.tolerance=="pointer"||this.options.forcePointerForContainers||(this.options.tolerance!="pointer"&&this.helperProportions[this.floating?"width":"height"]>m[this.floating?"width":"height"])){return g}else{return(f<e+(this.helperProportions.width/2)&&d-(this.helperProportions.width/2)<c&&n<k+(this.helperProportions.height/2)&&j-(this.helperProportions.height/2)<i)}},_intersectsWithPointer:function(d){var e=a.ui.isOverAxis(this.positionAbs.top+this.offset.click.top,d.top,d.height),c=a.ui.isOverAxis(this.positionAbs.left+this.offset.click.left,d.left,d.width),g=e&&c,b=this._getDragVerticalDirection(),f=this._getDragHorizontalDirection();if(!g){return false}return this.floating?(((f&&f=="right")||b=="down")?2:1):(b&&(b=="down"?2:1))},_intersectsWithSides:function(e){var c=a.ui.isOverAxis(this.positionAbs.top+this.offset.click.top,e.top+(e.height/2),e.height),d=a.ui.isOverAxis(this.positionAbs.left+this.offset.click.left,e.left+(e.width/2),e.width),b=this._getDragVerticalDirection(),f=this._getDragHorizontalDirection();if(this.floating&&f){return((f=="right"&&d)||(f=="left"&&!d))}else{return b&&((b=="down"&&c)||(b=="up"&&!c))}},_getDragVerticalDirection:function(){var b=this.positionAbs.top-this.lastPositionAbs.top;return b!=0&&(b>0?"down":"up")},_getDragHorizontalDirection:function(){var b=this.positionAbs.left-this.lastPositionAbs.left;return b!=0&&(b>0?"right":"left")},refresh:function(b){this._refreshItems(b);this.refreshPositions()},_connectWith:function(){var b=this.options;return b.connectWith.constructor==String?[b.connectWith]:b.connectWith},_getItemsAsjQuery:function(b){var l=this;var g=[];var e=[];var h=this._connectWith();if(h&&b){for(var d=h.length-1;d>=0;d--){var k=a(h[d]);for(var c=k.length-1;c>=0;c--){var f=a.data(k[c],"sortable");if(f&&f!=this&&!f.options.disabled){e.push([a.isFunction(f.options.items)?f.options.items.call(f.element):a(f.options.items,f.element).not(".ui-sortable-helper"),f])}}}}e.push([a.isFunction(this.options.items)?this.options.items.call(this.element,null,{options:this.options,item:this.currentItem}):a(this.options.items,this.element).not(".ui-sortable-helper"),this]);for(var d=e.length-1;d>=0;d--){e[d][0].each(function(){g.push(this)})}return a(g)},_removeCurrentsFromItems:function(){var d=this.currentItem.find(":data(sortable-item)");for(var c=0;c<this.items.length;c++){for(var b=0;b<d.length;b++){if(d[b]==this.items[c].item[0]){this.items.splice(c,1)}}}},_refreshItems:function(b){this.items=[];this.containers=[this];var h=this.items;var p=this;var f=[[a.isFunction(this.options.items)?this.options.items.call(this.element[0],b,{item:this.currentItem}):a(this.options.items,this.element),this]];var l=this._connectWith();if(l){for(var e=l.length-1;e>=0;e--){var m=a(l[e]);for(var d=m.length-1;d>=0;d--){var g=a.data(m[d],"sortable");if(g&&g!=this&&!g.options.disabled){f.push([a.isFunction(g.options.items)?g.options.items.call(g.element[0],b,{item:this.currentItem}):a(g.options.items,g.element),g]);this.containers.push(g)}}}}for(var e=f.length-1;e>=0;e--){var k=f[e][1];var c=f[e][0];for(var d=0,n=c.length;d<n;d++){var o=a(c[d]);o.data("sortable-item",k);h.push({item:o,instance:k,width:0,height:0,left:0,top:0})}}},refreshPositions:function(b){if(this.offsetParent&&this.helper){this.offset.parent=this._getParentOffset()}for(var d=this.items.length-1;d>=0;d--){var e=this.items[d];if(e.instance!=this.currentContainer&&this.currentContainer&&e.item[0]!=this.currentItem[0]){continue}var c=this.options.toleranceElement?a(this.options.toleranceElement,e.item):e.item;if(!b){e.width=c.outerWidth();e.height=c.outerHeight()}var f=c.offset();e.left=f.left;e.top=f.top}if(this.options.custom&&this.options.custom.refreshContainers){this.options.custom.refreshContainers.call(this)}else{for(var d=this.containers.length-1;d>=0;d--){var f=this.containers[d].element.offset();this.containers[d].containerCache.left=f.left;this.containers[d].containerCache.top=f.top;this.containers[d].containerCache.width=this.containers[d].element.outerWidth();this.containers[d].containerCache.height=this.containers[d].element.outerHeight()}}},_createPlaceholder:function(d){var b=d||this,e=b.options;if(!e.placeholder||e.placeholder.constructor==String){var c=e.placeholder;e.placeholder={element:function(){var f=a(document.createElement(b.currentItem[0].nodeName)).addClass(c||b.currentItem[0].className+" ui-sortable-placeholder").removeClass("ui-sortable-helper")[0];if(!c){f.style.visibility="hidden"}return f},update:function(f,g){if(c&&!e.forcePlaceholderSize){return}if(!g.height()){g.height(b.currentItem.innerHeight()-parseInt(b.currentItem.css("paddingTop")||0,10)-parseInt(b.currentItem.css("paddingBottom")||0,10))}if(!g.width()){g.width(b.currentItem.innerWidth()-parseInt(b.currentItem.css("paddingLeft")||0,10)-parseInt(b.currentItem.css("paddingRight")||0,10))}}}}b.placeholder=a(e.placeholder.element.call(b.element,b.currentItem));b.currentItem.after(b.placeholder);e.placeholder.update(b,b.placeholder)},_contactContainers:function(d){for(var c=this.containers.length-1;c>=0;c--){if(this._intersectsWith(this.containers[c].containerCache)){if(!this.containers[c].containerCache.over){if(this.currentContainer!=this.containers[c]){var h=10000;var g=null;var e=this.positionAbs[this.containers[c].floating?"left":"top"];for(var b=this.items.length-1;b>=0;b--){if(!a.ui.contains(this.containers[c].element[0],this.items[b].item[0])){continue}var f=this.items[b][this.containers[c].floating?"left":"top"];if(Math.abs(f-e)<h){h=Math.abs(f-e);g=this.items[b]}}if(!g&&!this.options.dropOnEmpty){continue}this.currentContainer=this.containers[c];g?this._rearrange(d,g,null,true):this._rearrange(d,null,this.containers[c].element,true);this._trigger("change",d,this._uiHash());this.containers[c]._trigger("change",d,this._uiHash(this));this.options.placeholder.update(this.currentContainer,this.placeholder)}this.containers[c]._trigger("over",d,this._uiHash(this));this.containers[c].containerCache.over=1}}else{if(this.containers[c].containerCache.over){this.containers[c]._trigger("out",d,this._uiHash(this));this.containers[c].containerCache.over=0}}}},_createHelper:function(c){var d=this.options;var b=a.isFunction(d.helper)?a(d.helper.apply(this.element[0],[c,this.currentItem])):(d.helper=="clone"?this.currentItem.clone():this.currentItem);if(!b.parents("body").length){a(d.appendTo!="parent"?d.appendTo:this.currentItem[0].parentNode)[0].appendChild(b[0])}if(b[0]==this.currentItem[0]){this._storedCSS={width:this.currentItem[0].style.width,height:this.currentItem[0].style.height,position:this.currentItem.css("position"),top:this.currentItem.css("top"),left:this.currentItem.css("left")}}if(b[0].style.width==""||d.forceHelperSize){b.width(this.currentItem.width())}if(b[0].style.height==""||d.forceHelperSize){b.height(this.currentItem.height())}return b},_adjustOffsetFromHelper:function(b){if(b.left!=undefined){this.offset.click.left=b.left+this.margins.left}if(b.right!=undefined){this.offset.click.left=this.helperProportions.width-b.right+this.margins.left}if(b.top!=undefined){this.offset.click.top=b.top+this.margins.top}if(b.bottom!=undefined){this.offset.click.top=this.helperProportions.height-b.bottom+this.margins.top}},_getParentOffset:function(){this.offsetParent=this.helper.offsetParent();var b=this.offsetParent.offset();if(this.cssPosition=="absolute"&&this.scrollParent[0]!=document&&a.ui.contains(this.scrollParent[0],this.offsetParent[0])){b.left+=this.scrollParent.scrollLeft();b.top+=this.scrollParent.scrollTop()}if((this.offsetParent[0]==document.body)||(this.offsetParent[0].tagName&&this.offsetParent[0].tagName.toLowerCase()=="html"&&a.browser.msie)){b={top:0,left:0}}return{top:b.top+(parseInt(this.offsetParent.css("borderTopWidth"),10)||0),left:b.left+(parseInt(this.offsetParent.css("borderLeftWidth"),10)||0)}},_getRelativeOffset:function(){if(this.cssPosition=="relative"){var b=this.currentItem.position();return{top:b.top-(parseInt(this.helper.css("top"),10)||0)+this.scrollParent.scrollTop(),left:b.left-(parseInt(this.helper.css("left"),10)||0)+this.scrollParent.scrollLeft()}}else{return{top:0,left:0}}},_cacheMargins:function(){this.margins={left:(parseInt(this.currentItem.css("marginLeft"),10)||0),top:(parseInt(this.currentItem.css("marginTop"),10)||0)}},_cacheHelperProportions:function(){this.helperProportions={width:this.helper.outerWidth(),height:this.helper.outerHeight()}},_setContainment:function(){var e=this.options;if(e.containment=="parent"){e.containment=this.helper[0].parentNode}if(e.containment=="document"||e.containment=="window"){this.containment=[0-this.offset.relative.left-this.offset.parent.left,0-this.offset.relative.top-this.offset.parent.top,a(e.containment=="document"?document:window).width()-this.helperProportions.width-this.margins.left,(a(e.containment=="document"?document:window).height()||document.body.parentNode.scrollHeight)-this.helperProportions.height-this.margins.top]}if(!(/^(document|window|parent)$/).test(e.containment)){var c=a(e.containment)[0];var d=a(e.containment).offset();var b=(a(c).css("overflow")!="hidden");this.containment=[d.left+(parseInt(a(c).css("borderLeftWidth"),10)||0)+(parseInt(a(c).css("paddingLeft"),10)||0)-this.margins.left,d.top+(parseInt(a(c).css("borderTopWidth"),10)||0)+(parseInt(a(c).css("paddingTop"),10)||0)-this.margins.top,d.left+(b?Math.max(c.scrollWidth,c.offsetWidth):c.offsetWidth)-(parseInt(a(c).css("borderLeftWidth"),10)||0)-(parseInt(a(c).css("paddingRight"),10)||0)-this.helperProportions.width-this.margins.left,d.top+(b?Math.max(c.scrollHeight,c.offsetHeight):c.offsetHeight)-(parseInt(a(c).css("borderTopWidth"),10)||0)-(parseInt(a(c).css("paddingBottom"),10)||0)-this.helperProportions.height-this.margins.top]}},_convertPositionTo:function(f,h){if(!h){h=this.position}var c=f=="absolute"?1:-1;var e=this.options,b=this.cssPosition=="absolute"&&!(this.scrollParent[0]!=document&&a.ui.contains(this.scrollParent[0],this.offsetParent[0]))?this.offsetParent:this.scrollParent,g=(/(html|body)/i).test(b[0].tagName);return{top:(h.top+this.offset.relative.top*c+this.offset.parent.top*c-(a.browser.safari&&this.cssPosition=="fixed"?0:(this.cssPosition=="fixed"?-this.scrollParent.scrollTop():(g?0:b.scrollTop()))*c)),left:(h.left+this.offset.relative.left*c+this.offset.parent.left*c-(a.browser.safari&&this.cssPosition=="fixed"?0:(this.cssPosition=="fixed"?-this.scrollParent.scrollLeft():g?0:b.scrollLeft())*c))}},_generatePosition:function(e){var h=this.options,b=this.cssPosition=="absolute"&&!(this.scrollParent[0]!=document&&a.ui.contains(this.scrollParent[0],this.offsetParent[0]))?this.offsetParent:this.scrollParent,i=(/(html|body)/i).test(b[0].tagName);if(this.cssPosition=="relative"&&!(this.scrollParent[0]!=document&&this.scrollParent[0]!=this.offsetParent[0])){this.offset.relative=this._getRelativeOffset()}var d=e.pageX;var c=e.pageY;if(this.originalPosition){if(this.containment){if(e.pageX-this.offset.click.left<this.containment[0]){d=this.containment[0]+this.offset.click.left}if(e.pageY-this.offset.click.top<this.containment[1]){c=this.containment[1]+this.offset.click.top}if(e.pageX-this.offset.click.left>this.containment[2]){d=this.containment[2]+this.offset.click.left}if(e.pageY-this.offset.click.top>this.containment[3]){c=this.containment[3]+this.offset.click.top}}if(h.grid){var g=this.originalPageY+Math.round((c-this.originalPageY)/h.grid[1])*h.grid[1];c=this.containment?(!(g-this.offset.click.top<this.containment[1]||g-this.offset.click.top>this.containment[3])?g:(!(g-this.offset.click.top<this.containment[1])?g-h.grid[1]:g+h.grid[1])):g;var f=this.originalPageX+Math.round((d-this.originalPageX)/h.grid[0])*h.grid[0];d=this.containment?(!(f-this.offset.click.left<this.containment[0]||f-this.offset.click.left>this.containment[2])?f:(!(f-this.offset.click.left<this.containment[0])?f-h.grid[0]:f+h.grid[0])):f}}return{top:(c-this.offset.click.top-this.offset.relative.top-this.offset.parent.top+(a.browser.safari&&this.cssPosition=="fixed"?0:(this.cssPosition=="fixed"?-this.scrollParent.scrollTop():(i?0:b.scrollTop())))),left:(d-this.offset.click.left-this.offset.relative.left-this.offset.parent.left+(a.browser.safari&&this.cssPosition=="fixed"?0:(this.cssPosition=="fixed"?-this.scrollParent.scrollLeft():i?0:b.scrollLeft())))}},_rearrange:function(g,f,c,e){c?c[0].appendChild(this.placeholder[0]):f.item[0].parentNode.insertBefore(this.placeholder[0],(this.direction=="down"?f.item[0]:f.item[0].nextSibling));this.counter=this.counter?++this.counter:1;var d=this,b=this.counter;window.setTimeout(function(){if(b==d.counter){d.refreshPositions(!e)}},0)},_clear:function(d,e){this.reverting=false;var f=[],b=this;if(!this._noFinalSort&&this.currentItem[0].parentNode){this.placeholder.before(this.currentItem)}this._noFinalSort=null;if(this.helper[0]==this.currentItem[0]){for(var c in this._storedCSS){if(this._storedCSS[c]=="auto"||this._storedCSS[c]=="static"){this._storedCSS[c]=""}}this.currentItem.css(this._storedCSS).removeClass("ui-sortable-helper")}else{this.currentItem.show()}if(this.fromOutside&&!e){f.push(function(g){this._trigger("receive",g,this._uiHash(this.fromOutside))})}if((this.fromOutside||this.domPosition.prev!=this.currentItem.prev().not(".ui-sortable-helper")[0]||this.domPosition.parent!=this.currentItem.parent()[0])&&!e){f.push(function(g){this._trigger("update",g,this._uiHash())})}if(!a.ui.contains(this.element[0],this.currentItem[0])){if(!e){f.push(function(g){this._trigger("remove",g,this._uiHash())})}for(var c=this.containers.length-1;c>=0;c--){if(a.ui.contains(this.containers[c].element[0],this.currentItem[0])&&!e){f.push((function(g){return function(h){g._trigger("receive",h,this._uiHash(this))}}).call(this,this.containers[c]));f.push((function(g){return function(h){g._trigger("update",h,this._uiHash(this))}}).call(this,this.containers[c]))}}}for(var c=this.containers.length-1;c>=0;c--){if(!e){f.push((function(g){return function(h){g._trigger("deactivate",h,this._uiHash(this))}}).call(this,this.containers[c]))}if(this.containers[c].containerCache.over){f.push((function(g){return function(h){g._trigger("out",h,this._uiHash(this))}}).call(this,this.containers[c]));this.containers[c].containerCache.over=0}}if(this._storedCursor){a("body").css("cursor",this._storedCursor)}if(this._storedOpacity){this.helper.css("opacity",this._storedOpacity)}if(this._storedZIndex){this.helper.css("zIndex",this._storedZIndex=="auto"?"":this._storedZIndex)}this.dragging=false;if(this.cancelHelperRemoval){if(!e){this._trigger("beforeStop",d,this._uiHash());for(var c=0;c<f.length;c++){f[c].call(this,d)}this._trigger("stop",d,this._uiHash())}return false}if(!e){this._trigger("beforeStop",d,this._uiHash())}this.placeholder[0].parentNode.removeChild(this.placeholder[0]);if(this.helper[0]!=this.currentItem[0]){this.helper.remove()}this.helper=null;if(!e){for(var c=0;c<f.length;c++){f[c].call(this,d)}this._trigger("stop",d,this._uiHash())}this.fromOutside=false;return true},_trigger:function(){if(a.widget.prototype._trigger.apply(this,arguments)===false){this.cancel()}},_uiHash:function(c){var b=c||this;return{helper:b.helper,placeholder:b.placeholder||a([]),position:b.position,absolutePosition:b.positionAbs,offset:b.positionAbs,item:b.currentItem,sender:c?c.element:null}}}));a.extend(a.ui.sortable,{getter:"serialize toArray",version:"1.7.2",eventPrefix:"sort",defaults:{appendTo:"parent",axis:false,cancel:":input,option",connectWith:false,containment:false,cursor:"auto",cursorAt:false,delay:0,distance:1,dropOnEmpty:true,forcePlaceholderSize:false,forceHelperSize:false,grid:false,handle:false,helper:"original",items:"> *",opacity:false,placeholder:false,revert:false,scroll:true,scrollSensitivity:20,scrollSpeed:20,scope:"default",tolerance:"intersect",zIndex:1000}})})(jQuery);

/*
 * jQuery Easing v1.3 - http://gsgd.co.uk/sandbox/jquery/easing/
 *
*/
jQuery.easing["jswing"]=jQuery.easing["swing"];jQuery.extend(jQuery.easing,{def:"easeOutQuad",swing:function(x,t,b,c,d){return jQuery.easing[jQuery.easing.def](x,t,b,c,d)},easeInQuad:function(x,t,b,c,d){return c*(t/=d)*t+b},easeOutQuad:function(x,t,b,c,d){return -c*(t/=d)*(t-2)+b},easeInOutQuad:function(x,t,b,c,d){if((t/=d/2)<1){return c/2*t*t+b}return -c/2*((--t)*(t-2)-1)+b},easeInCubic:function(x,t,b,c,d){return c*(t/=d)*t*t+b},easeOutCubic:function(x,t,b,c,d){return c*((t=t/d-1)*t*t+1)+b},easeInOutCubic:function(x,t,b,c,d){if((t/=d/2)<1){return c/2*t*t*t+b}return c/2*((t-=2)*t*t+2)+b},easeInQuart:function(x,t,b,c,d){return c*(t/=d)*t*t*t+b},easeOutQuart:function(x,t,b,c,d){return -c*((t=t/d-1)*t*t*t-1)+b},easeInOutQuart:function(x,t,b,c,d){if((t/=d/2)<1){return c/2*t*t*t*t+b}return -c/2*((t-=2)*t*t*t-2)+b},easeInQuint:function(x,t,b,c,d){return c*(t/=d)*t*t*t*t+b},easeOutQuint:function(x,t,b,c,d){return c*((t=t/d-1)*t*t*t*t+1)+b},easeInOutQuint:function(x,t,b,c,d){if((t/=d/2)<1){return c/2*t*t*t*t*t+b}return c/2*((t-=2)*t*t*t*t+2)+b},easeInSine:function(x,t,b,c,d){return -c*Math.cos(t/d*(Math.PI/2))+c+b},easeOutSine:function(x,t,b,c,d){return c*Math.sin(t/d*(Math.PI/2))+b},easeInOutSine:function(x,t,b,c,d){return -c/2*(Math.cos(Math.PI*t/d)-1)+b},easeInExpo:function(x,t,b,c,d){return(t==0)?b:c*Math.pow(2,10*(t/d-1))+b},easeOutExpo:function(x,t,b,c,d){return(t==d)?b+c:c*(-Math.pow(2,-10*t/d)+1)+b},easeInOutExpo:function(x,t,b,c,d){if(t==0){return b}if(t==d){return b+c}if((t/=d/2)<1){return c/2*Math.pow(2,10*(t-1))+b}return c/2*(-Math.pow(2,-10*--t)+2)+b},easeInCirc:function(x,t,b,c,d){return -c*(Math.sqrt(1-(t/=d)*t)-1)+b},easeOutCirc:function(x,t,b,c,d){return c*Math.sqrt(1-(t=t/d-1)*t)+b},easeInOutCirc:function(x,t,b,c,d){if((t/=d/2)<1){return -c/2*(Math.sqrt(1-t*t)-1)+b}return c/2*(Math.sqrt(1-(t-=2)*t)+1)+b},easeInElastic:function(x,t,b,c,d){var s=1.70158;var p=0;var a=c;if(t==0){return b}if((t/=d)==1){return b+c}if(!p){p=d*0.3}if(a<Math.abs(c)){a=c;var s=p/4}else{var s=p/(2*Math.PI)*Math.asin(c/a)}return -(a*Math.pow(2,10*(t-=1))*Math.sin((t*d-s)*(2*Math.PI)/p))+b},easeOutElastic:function(x,t,b,c,d){var s=1.70158;var p=0;var a=c;if(t==0){return b}if((t/=d)==1){return b+c}if(!p){p=d*0.3}if(a<Math.abs(c)){a=c;var s=p/4}else{var s=p/(2*Math.PI)*Math.asin(c/a)}return a*Math.pow(2,-10*t)*Math.sin((t*d-s)*(2*Math.PI)/p)+c+b},easeInOutElastic:function(x,t,b,c,d){var s=1.70158;var p=0;var a=c;if(t==0){return b}if((t/=d/2)==2){return b+c}if(!p){p=d*(0.3*1.5)}if(a<Math.abs(c)){a=c;var s=p/4}else{var s=p/(2*Math.PI)*Math.asin(c/a)}if(t<1){return -0.5*(a*Math.pow(2,10*(t-=1))*Math.sin((t*d-s)*(2*Math.PI)/p))+b}return a*Math.pow(2,-10*(t-=1))*Math.sin((t*d-s)*(2*Math.PI)/p)*0.5+c+b},easeInBack:function(x,t,b,c,d,s){if(s==undefined){s=1.70158}return c*(t/=d)*t*((s+1)*t-s)+b},easeOutBack:function(x,t,b,c,d,s){if(s==undefined){s=1.70158}return c*((t=t/d-1)*t*((s+1)*t+s)+1)+b},easeInOutBack:function(x,t,b,c,d,s){if(s==undefined){s=1.70158}if((t/=d/2)<1){return c/2*(t*t*(((s*=(1.525))+1)*t-s))+b}return c/2*((t-=2)*t*(((s*=(1.525))+1)*t+s)+2)+b},easeInBounce:function(x,t,b,c,d){return c-jQuery.easing.easeOutBounce(x,d-t,0,c,d)+b},easeOutBounce:function(x,t,b,c,d){if((t/=d)<(1/2.75)){return c*(7.5625*t*t)+b}else{if(t<(2/2.75)){return c*(7.5625*(t-=(1.5/2.75))*t+0.75)+b}else{if(t<(2.5/2.75)){return c*(7.5625*(t-=(2.25/2.75))*t+0.9375)+b}else{return c*(7.5625*(t-=(2.625/2.75))*t+0.984375)+b}}}},easeInOutBounce:function(x,t,b,c,d){if(t<d/2){return jQuery.easing.easeInBounce(x,t*2,0,c,d)*0.5+b}return jQuery.easing.easeOutBounce(x,t*2-d,0,c,d)*0.5+c*0.5+b}});

/*Fallr v1.3.1 Copyright 2011-2012 amatyr4n, licensed under Envato licenses*/;eval(function(p,a,c,k,e,r){e=function(c){return(c<a?'':e(parseInt(c/a)))+((c=c%a)>35?String.fromCharCode(c+29):c.toString(36))};if(!''.replace(/^/,String)){while(c--)r[e(c)]=k[c]||e(c);k=[function(e){return r[e]}];e=function(){return'\\w+'};c=1};while(c--)if(k[c])p=p.replace(new RegExp('\\b'+e(c)+'\\b','g'),k[c]);return p}(';(4($){h 1i={g:{2R:{2i:\'2F\',1A:Z,1f:4(){$.6(\'T\')}}},15:\'2B\',O:\'2v\',u:\'j\',1P:V,1M:Z,1I:V,1o:Z,1t:2q,25:\'24\',1J:\'24\',b:\'1m\',m:\'2E\',1d:1C,n:I,1N:4(){},1Y:4(){}},3,1x,$w=$(I),q={T:4(c,8,A){7(q.16()){$(\'#6-x\').2a(V,V);h $f=$(\'#6-x\'),F=$f.v(\'u\'),G=(F===\'R\'),s=0;1n(3.u){P\'1F\':P\'1E\':s=(G?$w.b():$f.E().j+$f.k())+10;Y;1z:s=(G?(-1)*($f.k()):$f.E().j-$f.k())-10}$f.d({\'j\':(s),\'2r\':G?1:0},(3.1t||3.2s),3.1J,4(){7($.1y.1X){$(\'#6-11\').v(\'26\',\'2t\')}C{$(\'#6-11\').2b(\'2k\')}$f.2n();2o(1x);8=N 8==="4"?8:3.1N;8.1b(A)});$(1c).12(\'y\',l.1e).12(\'y\',l.1v).12(\'y\',l.14)}},1H:4(c,8,A){h $f=$(\'#6-x\'),1g=1L(c.m,10),1h=1L(c.b,10),1S=1U.1V($f.S()-1g),1W=1U.1V($f.k()-1h);7(q.16()&&(1S>5||1W>5)){$f.d({\'m\':1g},4(){$(r).d({\'b\':1h},4(){l.1q()})});$(\'#6\').d({\'m\':1g-20},4(){$(r).d({\'b\':1h-22},4(){7(N 8==="4"){8.1b(A)}})})}},1p:4(c,8,A){7(q.16()){$(\'1G\',\'1l\').d({1k:$(\'#6\').E().j},4(){$.6(\'2j\')});$.2d(\'2u\\\'t 2A 2C 2e 2h O: "\'+c.O+\'", 2G 2e 2h O "\'+3.O+\'" 2L 2M 2Q\')}C{3=$.2X({},1i,c);$(\'<B J="6-x"></B>\').34(\'1G\');3.n=$(3.n).1K>0?3.n:I;h $f=$(\'#6-x\'),$o=$(\'#6-11\'),L=(3.n===I);$f.v({\'m\':3.m,\'b\':3.b,\'u\':\'1r\',\'j\':\'-1O\',\'p\':\'-1O\'}).1l(\'<B J="6-15"></B>\'+\'<B J="6"></B>\'+\'<B J="6-g"></B>\').Q(\'#6-15\').2y(\'15-\'+3.15).1Q().Q(\'#6\').1l(3.O).v({\'b\':(3.b==\'1m\')?\'1m\':$f.b()-22,\'m\':$f.m()-20}).1Q().Q(\'#6-g\').1l((4(){h g=\'\';h i;1R(i 1s 3.g){7(3.g.1T(i)){g=g+\'<a 2H="#" 2K="6-U \'+(3.g[i].1A?\'6-U-1A\':\'\')+\'" J="6-U-\'+i+\'">\'+3.g[i].2i+\'</a>\'}}1u g}())).Q(\'.6-U\').H(\'1w\',4(){h 1a=$(r).2l(\'J\').2m(13);7(N 3.g[1a].1f===\'4\'&&3.g[1a].1f!=Z){h 1Z=$(\'#6\');3.g[1a].1f.2p(1Z)}C{q.T()}1u Z});h 18=4(){$f.1p();h 21=L?(($w.m()-$f.S())/2+$w.23()):(($(3.n).m()-$f.S())/2+$(3.n).E().p),s,W,F=($w.b()>$f.b()&&$w.m()>$f.m()&&L)?\'R\':\'1r\',G=(F===\'R\');1n(3.u){P\'1F\':s=L?(G?$w.b():$w.1k()+$w.b()):($(3.n).E().j+$(3.n).k());W=s-$f.k();Y;P\'1E\':s=L?(G?(-1)*$f.k():$o.E().j-$f.k()):($(3.n).E().j+($(3.n).b()/2)-$f.k());W=s+$f.k()+(((L?$w.b():$f.k()/2)-$f.k())/2);Y;1z:W=L?(G?0:$w.1k()):$(3.n).E().j;s=W-$f.k()}$f.v({\'p\':21,\'u\':F,\'j\':s,\'z-1B\':3.1d+1}).d({\'j\':W},3.1t,3.25,4(){8=N 8==="4"?8:3.1Y;8.1b(A);7(3.1o){1x=2w(q.T,3.1o)}})};7(3.1I){7($.1y.1X&&$.1y.2x<9){$o.v({\'26\':\'2z\',\'z-1B\':3.1d});18()}C{$o.v({\'z-1B\':3.1d}).28(18)}}C{18()}$(1c).H(\'y\',l.1e).H(\'y\',l.1v).H(\'y\',l.14);$(\'#6-g\').29().19(-1).H(\'1D\',4(){$(r).H(\'y\',l.14)});$f.Q(\':2c\').H(\'y\',4(e){l.17();7(e.1j===13){$(\'.6-U\').19(0).2I(\'1w\')}})}},2J:4(c,8,A){1R(h i 1s c){7(1i.1T(i)){1i[i]=c[i];7(3&&3[i]){3[i]=c[i]}}}7(N 8==="4"){8.1b(A)}},16:4(){1u!!($(\'#6-x\').1K>0)},2f:4(){$(\'#6-x\').2b(1C,4(){$(r).28(1C)})},2j:4(){$(\'#6-x\').2a(V,V).d({\'p\':\'+=2g\'},X,4(){$(r).d({\'p\':\'-=2N\'},X,4(){$(r).d({\'p\':\'+=2O\'},X,4(){$(r).d({\'p\':\'-=2g\'},X,4(){$(r).d({\'p\':\'+=2P\'},X)})})})})}},l={1q:4(){h $f=$(\'#6-x\'),F=$f.v(\'u\');7($w.m()>$f.S()&&$w.b()>$f.k()){h K=($w.m()-$f.S())/2,D=$w.b()-$f.k();1n(3.u){P\'1E\':D=D/2;Y;P\'1F\':Y;1z:D=0}7(F==\'R\'){$f.d({\'p\':K},4(){$(r).d({\'j\':D})})}C{$f.v({\'u\':\'R\',\'p\':K,\'j\':D})}}C{h K=($w.m()-$f.S())/2+$w.23();h D=$w.1k();7(F!=\'R\'){$f.d({\'p\':K},4(){$(r).d({\'j\':D})})}C{$f.v({\'u\':\'1r\',\'j\':D,\'p\':(K>0?K:0)})}}},1e:4(e){7(e.1j===13){$(\'#6-g\').29().19(0).1D();l.17()}},14:4(e){7(e.1j===9){$(\'#6-x\').Q(\':2c, .6-U\').19(0).1D();l.17();e.2S()}},1v:4(e){7(e.1j===27&&3.1P){q.T()}},17:4(){$(1c).12(\'y\',l.1e).12(\'y\',l.14)}};$(1c).2T(4(){$(\'1G\').2U(\'<B J="6-11"></B>\');$(\'#6-11\').H(\'1w\',4(){7(3.1M){q.T()}C{q.2f()}})});$(I).1H(4(){2V.2W(3.n);7(q.16()&&3.n===I){l.1q()}});$.6=4(M,c,8){h A=I;7(N M===\'2Y\'){c=M;M=\'1p\'}7(q[M]){7(N c===\'4\'){8=c;c=2Z}q[M](c,8,A)}C{$.2d(\'30 "\'+M+\'" 31 32 33 1s $.6\')}}}(2D));',62,191,'|||opts|function||fallr|if|callback|||height|options|animate|||buttons|var||top|outerHeight|helpers|width|bound||left|methods|this|yminpos||position|css||wrapper|keydown||self|div|else|newTop|offset|pos|isFixed|bind|window|id|newLeft|isWin|method|typeof|content|case|find|fixed|outerWidth|hide|button|true|ymaxpos|50|break|false||overlay|unbind||tabKeyHandler|icon|isActive|unbindKeyHandler|showFallr|eq|buttonId|call|document|zIndex|enterKeyHandler|onclick|newWidth|newHeight|defaults|keyCode|scrollTop|html|auto|switch|autoclose|show|fixPos|absolute|in|easingDuration|return|closeKeyHandler|click|timeoutId|browser|default|danger|index|100|focus|center|bottom|body|resize|useOverlay|easingOut|length|parseInt|closeOverlay|afterHide|9999px|closeKey|end|for|diffWidth|hasOwnProperty|Math|abs|diffHeight|msie|afterShow|scope|94|xpos|116|scrollLeft|swing|easingIn|display||fadeIn|children|stop|fadeOut|input|error|message|blink|20px|with|text|shake|fast|attr|substring|remove|clearTimeout|apply|300|opacity|duration|none|Can|Hello|setTimeout|version|addClass|block|create|check|new|jQuery|360px|OK|past|href|trigger|set|class|is|still|40px|30px|10px|active|button1|preventDefault|ready|append|console|log|extend|object|null|Method|does|not|exist|appendTo'.split('|'),0,{}));


/* Cookie plugin
*
* Copyright (c) 2006 Klaus Hartl (stilbuero.de)
* Dual licensed under the MIT and GPL licenses:
* http://www.opensource.org/licenses/mit-license.php
* http://www.gnu.org/licenses/gpl.html
*
*/
jQuery.cookie=function(name,value,options){if(typeof value!="undefined"){options=options||{};if(value===null){value="";options.expires=-1}var expires="";if(options.expires&&(typeof options.expires=="number"||options.expires.toUTCString)){var date;if(typeof options.expires=="number"){date=new Date();date.setTime(date.getTime()+(options.expires*24*60*60*1000))}else{date=options.expires}expires="; expires="+date.toUTCString()}var path=options.path?"; path="+(options.path):"";var domain=options.domain?"; domain="+(options.domain):"";var secure=options.secure?"; secure":"";document.cookie=[name,"=",encodeURIComponent(value),expires,path,domain,secure].join("")}else{var cookieValue=null;if(document.cookie&&document.cookie!=""){var cookies=document.cookie.split(";");for(var i=0;i<cookies.length;i++){var cookie=jQuery.trim(cookies[i]);if(cookie.substring(0,name.length+1)==(name+"=")){cookieValue=decodeURIComponent(cookie.substring(name.length+1));break}}}return cookieValue}};


/*
 * IFrame Loader Plugin for JQuery
 * - Notifies your event handler when iframe has finished loading
 * - Your event handler receives loading duration (as well as iframe)
 * - Optionally calls your timeout handler
 *
 * http://project.ajaxpatterns.org/jquery-iframe
 */
(function($){var timer;$.fn.src=function(url,onLoad,options){setIFrames($(this),onLoad,options,function(){this.src=url});return $(this)};$.fn.squirt=function(content,onLoad,options){setIFrames($(this),onLoad,options,function(){var doc=this.contentDocument||this.contentWindow.document;doc.open();doc.writeln(content);doc.close()});return this};function setIFrames(iframes,onLoad,options,iFrameSetter){iframes.each(function(){if(this.tagName=="IFRAME"){setIFrame(this,onLoad,options,iFrameSetter)}})}function setIFrame(iframe,onLoad,options,iFrameSetter){var iframe;iframe.onload=null;if(timer){clearTimeout(timer)}var defaults={timeoutDuration:0,timeout:null};var opts=$.extend(defaults,options);if(opts.timeout&&!opts.timeoutDuration){opts.timeoutDuration=60000}opts.frameactive=true;var startTime=(new Date()).getTime();if(opts.timeout){var timer=setTimeout(function(){opts.frameactive=false;iframe.onload=null;if(opts.timeout){opts.timeout(iframe,opts.timeout)}},opts.timeoutDuration)}var onloadHandler=function(){var duration=(new Date()).getTime()-startTime;if(timer){clearTimeout(timer)}if(onLoad&&opts.frameactive){onLoad.apply(iframe,[duration])}opts.frameactive=false};iframe.onload=onloadHandler;opts.completeReadyStateChanges=0;iframe.onreadystatechange=function(){if(++(opts.completeReadyStateChanges)==3||this.readyState=="complete"){onloadHandler()}};iFrameSetter.apply(iframe);return iframe}})(jQuery);


/**
 * jQuery.timers - Timer abstractions for jQuery
 * Written by Blair Mitchelmore (blair DOT mitchelmore AT gmail DOT com)
 * Licensed under the WTFPL (http://sam.zoy.org/wtfpl/).
 * Date: 2009/10/16
 *
 * @author Blair Mitchelmore
 * @version 1.2
 *
 **/
jQuery.fn.extend({everyTime:function(interval,label,fn,times){return this.each(function(){jQuery.timer.add(this,interval,label,fn,times)})},oneTime:function(interval,label,fn){return this.each(function(){jQuery.timer.add(this,interval,label,fn,1)})},stopTime:function(label,fn){return this.each(function(){jQuery.timer.remove(this,label,fn)})}});jQuery.extend({timer:{global:[],guid:1,dataKey:"jQuery.timer",regex:/^([0-9]+(?:\.[0-9]*)?)\s*(.*s)?$/,powers:{"ms":1,"cs":10,"ds":100,"s":1000,"das":10000,"hs":100000,"ks":1000000},timeParse:function(value){if(value==undefined||value==null){return null}var result=this.regex.exec(jQuery.trim(value.toString()));if(result[2]){var num=parseFloat(result[1]);var mult=this.powers[result[2]]||1;return num*mult}else{return value}},add:function(element,interval,label,fn,times){var counter=0;if(jQuery.isFunction(label)){if(!times){times=fn}fn=label;label=interval}interval=jQuery.timer.timeParse(interval);if(typeof interval!="number"||isNaN(interval)||interval<0){return}if(typeof times!="number"||isNaN(times)||times<0){times=0}times=times||0;var timers=jQuery.data(element,this.dataKey)||jQuery.data(element,this.dataKey,{});if(!timers[label]){timers[label]={}}fn.timerID=fn.timerID||this.guid++;var handler=function(){if((++counter>times&&times!==0)||fn.call(element,counter)===false){jQuery.timer.remove(element,label,fn)}};handler.timerID=fn.timerID;if(!timers[label][fn.timerID]){timers[label][fn.timerID]=window.setInterval(handler,interval)}this.global.push(element)},remove:function(element,label,fn){var timers=jQuery.data(element,this.dataKey),ret;if(timers){if(!label){for(label in timers){this.remove(element,label,fn)}}else{if(timers[label]){if(fn){if(fn.timerID){window.clearInterval(timers[label][fn.timerID]);delete timers[label][fn.timerID]}}else{for(var fn in timers[label]){window.clearInterval(timers[label][fn]);delete timers[label][fn]}}for(ret in timers[label]){break}if(!ret){ret=null;delete timers[label]}}}for(ret in timers){break}if(!ret){jQuery.removeData(element,this.dataKey)}}}}});jQuery(window).bind("unload",function(){jQuery.each(jQuery.timer.global,function(index,item){jQuery.timer.remove(item)})});


/*
 * JQuery zTree core 3.1
 * http://code.google.com/p/jquerytree/
 *
 * Copyright (c) 2010 Hunter.z (baby666.cn)
 *
 * Licensed same as jquery - MIT License
 * http://www.opensource.org/licenses/mit-license.php
 *
 * email: hunter.z@263.net
 * Date: 2012-02-14
 */
(function($){
	var settings = {}, roots = {}, caches = {}, zId = 0,
	//default consts of core
	_consts = {
		event: {
			NODECREATED: "ztree_nodeCreated",
			CLICK: "ztree_click",
			EXPAND: "ztree_expand",
			COLLAPSE: "ztree_collapse",
			ASYNC_SUCCESS: "ztree_async_success",
			ASYNC_ERROR: "ztree_async_error"
		},
		id: {
			A: "_a",
			ICON: "_ico",
			SPAN: "_span",
			SWITCH: "_switch",
			UL: "_ul"
		},
		line: {
			ROOT: "root",
			ROOTS: "roots",
			CENTER: "center",
			BOTTOM: "bottom",
			NOLINE: "noline",
			LINE: "line"
		},
		folder: {
			OPEN: "open",
			CLOSE: "close",
			DOCU: "docu"
		},
		node: {
			CURSELECTED: "curSelectedNode"
		}
	},
	//default setting of core
	_setting = {
		treeId: "",
		treeObj: null,
		view: {
			addDiyDom: null,
			autoCancelSelected: true,
			dblClickExpand: true,
			expandSpeed: "fast",
			fontCss: {},
			nameIsHTML: false,
			selectedMulti: true,
			showIcon: true,
			showLine: true,
			showTitle: true
		},
		data: {
			key: {
				children: "children",
				name: "name",
				title: ""
			},
			simpleData: {
				enable: false,
				idKey: "id",
				pIdKey: "pId",
				rootPId: null
			},
			keep: {
				parent: false,
				leaf: false
			}
		},
		async: {
			enable: false,
			contentType: "application/x-www-form-urlencoded",
			type: "post",
			dataType: "text",
			url: "",
			autoParam: [],
			otherParam: [],
			dataFilter: null
		},
		callback: {
			beforeAsync:null,
			beforeClick:null,
			beforeRightClick:null,
			beforeMouseDown:null,
			beforeMouseUp:null,
			beforeExpand:null,
			beforeCollapse:null,

			onAsyncError:null,
			onAsyncSuccess:null,
			onNodeCreated:null,
			onClick:null,
			onRightClick:null,
			onMouseDown:null,
			onMouseUp:null,
			onExpand:null,
			onCollapse:null
		}
	},
	//default root of core
	//zTree use root to save full data
	_initRoot = function (setting) {
		var r = data.getRoot(setting);
		if (!r) {
			r = {};
			data.setRoot(setting, r);
		}
		r.children = [];
		r.expandTriggerFlag = false;
		r.curSelectedList = [];
		r.noSelection = true;
		r.createdNodes = [];
	},
	//default cache of core
	_initCache = function(setting) {
		var c = data.getCache(setting);
		if (!c) {
			c = {};
			data.setCache(setting, c);
		}
		c.nodes = [];
		c.doms = [];
	},
	//default bindEvent of core
	_bindEvent = function(setting) {
		var o = setting.treeObj,
		c = consts.event;
		o.unbind(c.NODECREATED);
		o.bind(c.NODECREATED, function (event, treeId, node) {
			tools.apply(setting.callback.onNodeCreated, [event, treeId, node]);
		});

		o.unbind(c.CLICK);
		o.bind(c.CLICK, function (event, treeId, node, clickFlag) {
			tools.apply(setting.callback.onClick, [event, treeId, node, clickFlag]);
		});

		o.unbind(c.EXPAND);
		o.bind(c.EXPAND, function (event, treeId, node) {
			tools.apply(setting.callback.onExpand, [event, treeId, node]);
		});

		o.unbind(c.COLLAPSE);
		o.bind(c.COLLAPSE, function (event, treeId, node) {
			tools.apply(setting.callback.onCollapse, [event, treeId, node]);
		});

		o.unbind(c.ASYNC_SUCCESS);
		o.bind(c.ASYNC_SUCCESS, function (event, treeId, node, msg) {
			tools.apply(setting.callback.onAsyncSuccess, [event, treeId, node, msg]);
		});

		o.unbind(c.ASYNC_ERROR);
		o.bind(c.ASYNC_ERROR, function (event, treeId, node, XMLHttpRequest, textStatus, errorThrown) {
			tools.apply(setting.callback.onAsyncError, [event, treeId, node, XMLHttpRequest, textStatus, errorThrown]);
		});
	},
	//default event proxy of core
	_eventProxy = function(event) {
		var target = event.target,
		setting = settings[event.data.treeId],
		tId = "", node = null,
		nodeEventType = "", treeEventType = "",
		nodeEventCallback = null, treeEventCallback = null,
		tmp = null;

		if (tools.eqs(event.type, "mousedown")) {
			treeEventType = "mousedown";
		} else if (tools.eqs(event.type, "mouseup")) {
			treeEventType = "mouseup";
		} else if (tools.eqs(event.type, "contextmenu")) {
			treeEventType = "contextmenu";
		} else if (tools.eqs(event.type, "click")) {
			if (tools.eqs(target.tagName, "button")) {
				target.blur();
			}
			if (tools.eqs(target.tagName, "button") && target.getAttribute("treeNode"+ consts.id.SWITCH) !== null) {
				tId = target.parentNode.id;
				nodeEventType = "switchNode";
			} else {
				tmp = tools.getMDom(setting, target, [{tagName:"a", attrName:"treeNode"+consts.id.A}]);
				if (tmp) {
					tId = tmp.parentNode.id;
					nodeEventType = "clickNode";
				}
			}
		} else if (tools.eqs(event.type, "dblclick")) {
			treeEventType = "dblclick";
			tmp = tools.getMDom(setting, target, [{tagName:"a", attrName:"treeNode"+consts.id.A}]);
			if (tmp) {
				tId = tmp.parentNode.id;
				nodeEventType = "switchNode";
			}
		}
		if (treeEventType.length > 0 && tId.length == 0) {
			tmp = tools.getMDom(setting, target, [{tagName:"a", attrName:"treeNode"+consts.id.A}]);
			if (tmp) {tId = tmp.parentNode.id;}
		}
		// event to node
		if (tId.length>0) {
			node = data.getNodeCache(setting, tId);
			switch (nodeEventType) {
				case "switchNode" :
					if (!node.isParent) {
						nodeEventType = "";
					} else if (tools.eqs(event.type, "click") 
						|| (tools.eqs(event.type, "dblclick") && tools.apply(setting.view.dblClickExpand, [setting.treeId, node], setting.view.dblClickExpand))) {
						nodeEventCallback = handler.onSwitchNode;
					} else {
						nodeEventType = "";
					}
					break;
				case "clickNode" :
					nodeEventCallback = handler.onClickNode;
					break;
			}
		}
		// event to zTree
		switch (treeEventType) {
			case "mousedown" :
				treeEventCallback = handler.onZTreeMousedown;
				break;
			case "mouseup" :
				treeEventCallback = handler.onZTreeMouseup;
				break;
			case "dblclick" :
				treeEventCallback = handler.onZTreeDblclick;
				break;
			case "contextmenu" :
				treeEventCallback = handler.onZTreeContextmenu;
				break;
		}
		var proxyResult = {
			stop: false,
			node: node,
			nodeEventType: nodeEventType,
			nodeEventCallback: nodeEventCallback,
			treeEventType: treeEventType,
			treeEventCallback: treeEventCallback
		};
		return proxyResult;
	},
	//default init node of core
	_initNode = function(setting, level, n, parentNode, isFirstNode, isLastNode, openFlag) {
		if (!n) return;
		var childKey = setting.data.key.children;
		n.level = level;
		n.tId = setting.treeId + "_" + (++zId);
		n.parentTId = parentNode ? parentNode.tId : null;
		if (n[childKey] && n[childKey].length > 0) {
			if (typeof n.open == "string") n.open = tools.eqs(n.open, "true");
			n.open = !!n.open;
			n.isParent = true;
			n.zAsync = true;
		} else {
			n.open = false;
			if (typeof n.isParent == "string") n.isParent = tools.eqs(n.isParent, "true");
			n.isParent = !!n.isParent;
			n.zAsync = !n.isParent;
		}
		n.isFirstNode = isFirstNode;
		n.isLastNode = isLastNode;
		n.getParentNode = function() {return data.getNodeCache(setting, n.parentTId);};
		n.getPreNode = function() {return data.getPreNode(setting, n);};
		n.getNextNode = function() {return data.getNextNode(setting, n);};
		n.isAjaxing = false;
		data.fixPIdKeyValue(setting, n);
	},
	_init = {
		bind: [_bindEvent],
		caches: [_initCache],
		nodes: [_initNode],
		proxys: [_eventProxy],
		roots: [_initRoot],
		beforeA: [],
		afterA: [],
		innerBeforeA: [],
		innerAfterA: [],
		zTreeTools: []
	},
	//method of operate data
	data = {
		addNodeCache: function(setting, node) {
			data.getCache(setting).nodes[node.tId] = node;
		},
		addAfterA: function(afterA) {
			_init.afterA.push(afterA);
		},
		addBeforeA: function(beforeA) {
			_init.beforeA.push(beforeA);
		},
		addInnerAfterA: function(innerAfterA) {
			_init.innerAfterA.push(innerAfterA);
		},
		addInnerBeforeA: function(innerBeforeA) {
			_init.innerBeforeA.push(innerBeforeA);
		},
		addInitBind: function(bindEvent) {
			_init.bind.push(bindEvent);
		},
		addInitCache: function(initCache) {
			_init.caches.push(initCache);
		},
		addInitNode: function(initNode) {
			_init.nodes.push(initNode);
		},
		addInitProxy: function(initProxy) {
			_init.proxys.push(initProxy);
		},
		addInitRoot: function(initRoot) {
			_init.roots.push(initRoot);
		},
		addNodesData: function(setting, parentNode, nodes) {
			var childKey = setting.data.key.children;
			if (!parentNode[childKey]) parentNode[childKey] = [];
			if (parentNode[childKey].length > 0) {
				parentNode[childKey][parentNode[childKey].length - 1].isLastNode = false;
				view.setNodeLineIcos(setting, parentNode[childKey][parentNode[childKey].length - 1]);
			}
			parentNode.isParent = true;
			parentNode[childKey] = parentNode[childKey].concat(nodes);
		},
		addSelectedNode: function(setting, node) {
			var root = data.getRoot(setting);
			if (!data.isSelectedNode(setting, node)) {
				root.curSelectedList.push(node);
			}
		},
		addCreatedNode: function(setting, node) {
			if (!!setting.callback.onNodeCreated || !!setting.view.addDiyDom) {
				var root = data.getRoot(setting);
				root.createdNodes.push(node);
			}
		},
		addZTreeTools: function(zTreeTools) {
			_init.zTreeTools.push(zTreeTools);
		},
		exSetting: function(s) {
			$.extend(true, _setting, s);
		},
		fixPIdKeyValue: function(setting, node) {
			if (setting.data.simpleData.enable) {
				node[setting.data.simpleData.pIdKey] = node.parentTId ? node.getParentNode()[setting.data.simpleData.idKey] : setting.data.simpleData.rootPId;
			}
		},
		getAfterA: function(setting, node, array) {
			for (var i=0, j=_init.afterA.length; i<j; i++) {
				_init.afterA[i].apply(this, arguments);
			}
		},
		getBeforeA: function(setting, node, array) {
			for (var i=0, j=_init.beforeA.length; i<j; i++) {
				_init.beforeA[i].apply(this, arguments);
			}
		},
		getInnerAfterA: function(setting, node, array) {
			for (var i=0, j=_init.innerAfterA.length; i<j; i++) {
				_init.innerAfterA[i].apply(this, arguments);
			}
		},
		getInnerBeforeA: function(setting, node, array) {
			for (var i=0, j=_init.innerBeforeA.length; i<j; i++) {
				_init.innerBeforeA[i].apply(this, arguments);
			}
		},
		getCache: function(setting) {
			return caches[setting.treeId];
		},
		getNextNode: function(setting, node) {
			if (!node) return null;
			var childKey = setting.data.key.children,
			p = node.parentTId ? node.getParentNode() : data.getRoot(setting);
			if (node.isLastNode) {
				return null;
			} else if (node.isFirstNode) {
				return p[childKey][1];
			} else {
				for (var i=1, l=p[childKey].length-1; i<l; i++) {
					if (p[childKey][i] === node) {
						return p[childKey][i+1];
					}
				}
			}
			return null;
		},
		getNodeByParam: function(setting, nodes, key, value) {
			if (!nodes || !key) return null;
			var childKey = setting.data.key.children;
			for (var i = 0, l = nodes.length; i < l; i++) {
				if (nodes[i][key] == value) {
					return nodes[i];
				}
				var tmp = data.getNodeByParam(setting, nodes[i][childKey], key, value);
				if (tmp) return tmp;
			}
			return null;
		},
		getNodeCache: function(setting, tId) {
			if (!tId) return null;
			var n = caches[setting.treeId].nodes[tId];
			return n ? n : null;
		},
		getNodes: function(setting) {
			return data.getRoot(setting)[setting.data.key.children];
		},
		getNodesByParam: function(setting, nodes, key, value) {
			if (!nodes || !key) return [];
			var childKey = setting.data.key.children,
			result = [];
			for (var i = 0, l = nodes.length; i < l; i++) {
				if (nodes[i][key] == value) {
					result.push(nodes[i]);
				}
				result = result.concat(data.getNodesByParam(setting, nodes[i][childKey], key, value));
			}
			return result;
		},
		getNodesByParamFuzzy: function(setting, nodes, key, value) {
			if (!nodes || !key) return [];
			var childKey = setting.data.key.children,
			result = [];
			for (var i = 0, l = nodes.length; i < l; i++) {
				if (typeof nodes[i][key] == "string" && nodes[i][key].indexOf(value)>-1) {
					result.push(nodes[i]);
				}
				result = result.concat(data.getNodesByParamFuzzy(setting, nodes[i][childKey], key, value));
			}
			return result;
		},
		getPreNode: function(setting, node) {
			if (!node) return null;
			var childKey = setting.data.key.children,
			p = node.parentTId ? node.getParentNode() : data.getRoot(setting);
			if (node.isFirstNode) {
				return null;
			} else if (node.isLastNode) {
				return p[childKey][p[childKey].length-2];
			} else {
				for (var i=1, l=p[childKey].length-1; i<l; i++) {
					if (p[childKey][i] === node) {
						return p[childKey][i-1];
					}
				}
			}
			return null;
		},
		getRoot: function(setting) {
			return setting ? roots[setting.treeId] : null;
		},
		getSetting: function(treeId) {
			return settings[treeId];
		},
		getSettings: function() {
			return settings;
		},
		getTitleKey: function(setting) {
			return setting.data.key.title === "" ? setting.data.key.name : setting.data.key.title;
		},
		getZTreeTools: function(treeId) {
			var r = this.getRoot(this.getSetting(treeId));
			return r ? r.treeTools : null;
		},
		initCache: function(setting) {
			for (var i=0, j=_init.caches.length; i<j; i++) {
				_init.caches[i].apply(this, arguments);
			}
		},
		initNode: function(setting, level, node, parentNode, preNode, nextNode) {
			for (var i=0, j=_init.nodes.length; i<j; i++) {
				_init.nodes[i].apply(this, arguments);
			}
		},
		initRoot: function(setting) {
			for (var i=0, j=_init.roots.length; i<j; i++) {
				_init.roots[i].apply(this, arguments);
			}
		},
		isSelectedNode: function(setting, node) {
			var root = data.getRoot(setting);
			for (var i=0, j=root.curSelectedList.length; i<j; i++) {
				if(node === root.curSelectedList[i]) return true;
			}
			return false;
		},
		removeNodeCache: function(setting, node) {
			var childKey = setting.data.key.children;
			if (node[childKey]) {
				for (var i=0, l=node[childKey].length; i<l; i++) {
					arguments.callee(setting, node[childKey][i]);
				}
			}
			delete data.getCache(setting).nodes[node.tId];
		},
		removeSelectedNode: function(setting, node) {
			var root = data.getRoot(setting);
			for (var i=0, j=root.curSelectedList.length; i<j; i++) {
				if(node === root.curSelectedList[i] || !data.getNodeCache(setting, root.curSelectedList[i].tId)) {
					root.curSelectedList.splice(i, 1);
					i--;j--;
				}
			}
		},
		setCache: function(setting, cache) {
			caches[setting.treeId] = cache;
		},
		setRoot: function(setting, root) {
			roots[setting.treeId] = root;
		},
		setZTreeTools: function(setting, zTreeTools) {
			for (var i=0, j=_init.zTreeTools.length; i<j; i++) {
				_init.zTreeTools[i].apply(this, arguments);
			}
		},
		transformToArrayFormat: function (setting, nodes) {
			if (!nodes) return [];
			var childKey = setting.data.key.children,
			r = [];
			if (tools.isArray(nodes)) {
				for (var i=0, l=nodes.length; i<l; i++) {
					r.push(nodes[i]);
					if (nodes[i][childKey])
						r = r.concat(data.transformToArrayFormat(setting, nodes[i][childKey]));
				}
			} else {
				r.push(nodes);
				if (nodes[childKey])
					r = r.concat(data.transformToArrayFormat(setting, nodes[childKey]));
			}
			return r;
		},
		transformTozTreeFormat: function(setting, sNodes) {
			var i,l,
			key = setting.data.simpleData.idKey,
			parentKey = setting.data.simpleData.pIdKey,
			childKey = setting.data.key.children;
			if (!key || key=="" || !sNodes) return [];

			if (tools.isArray(sNodes)) {
				var r = [];
				var tmpMap = [];
				for (i=0, l=sNodes.length; i<l; i++) {
					tmpMap[sNodes[i][key]] = sNodes[i];
				}
				for (i=0, l=sNodes.length; i<l; i++) {
					if (tmpMap[sNodes[i][parentKey]] && sNodes[i][key] != sNodes[i][parentKey]) {
						if (!tmpMap[sNodes[i][parentKey]][childKey])
							tmpMap[sNodes[i][parentKey]][childKey] = [];
						tmpMap[sNodes[i][parentKey]][childKey].push(sNodes[i]);
					} else {
						r.push(sNodes[i]);
					}
				}
				return r;
			}else {
				return [sNodes];
			}
		}
	},
	//method of event proxy
	event = {
		bindEvent: function(setting) {
			for (var i=0, j=_init.bind.length; i<j; i++) {
				_init.bind[i].apply(this, arguments);
			}
		},
		bindTree: function(setting) {
			var eventParam = {
				treeId: setting.treeId
			},
			o = setting.treeObj;
			o.unbind('click', event.proxy);
			o.bind('click', eventParam, event.proxy);
			o.unbind('dblclick', event.proxy);
			o.bind('dblclick', eventParam, event.proxy);
			o.unbind('mouseover', event.proxy);
			o.bind('mouseover', eventParam, event.proxy);
			o.unbind('mouseout', event.proxy);
			o.bind('mouseout', eventParam, event.proxy);
			o.unbind('mousedown', event.proxy);
			o.bind('mousedown', eventParam, event.proxy);
			o.unbind('mouseup', event.proxy);
			o.bind('mouseup', eventParam, event.proxy);
			o.unbind('contextmenu', event.proxy);
			o.bind('contextmenu', eventParam, event.proxy);
		},
		doProxy: function(e) {
			var results = [];
			for (var i=0, j=_init.proxys.length; i<j; i++) {
				var proxyResult = _init.proxys[i].apply(this, arguments);
				results.push(proxyResult);
				if (proxyResult.stop) {
					break;
				}
			}
			return results;
		},
		proxy: function(e) {
			var setting = data.getSetting(e.data.treeId);
			if (!tools.uCanDo(setting, e)) return true;
			var results = event.doProxy(e),
			r = true, x = false;
			for (var i=0, l=results.length; i<l; i++) {
				var proxyResult = results[i];
				if (proxyResult.nodeEventCallback) {
					x = true;
					r = proxyResult.nodeEventCallback.apply(proxyResult, [e, proxyResult.node]) && r;
				}
				if (proxyResult.treeEventCallback) {
					x = true;
					r = proxyResult.treeEventCallback.apply(proxyResult, [e, proxyResult.node]) && r;
				}
			}
			try{
				if (x && $("input:focus").length == 0) {
					tools.noSel(setting);
				}
			} catch(e) {}
			return r;
		}
	},
	//method of event handler
	handler = {
		onSwitchNode: function (event, node) {
			var setting = settings[event.data.treeId];
			if (node.open) {
				if (tools.apply(setting.callback.beforeCollapse, [setting.treeId, node], true) == false) return true;
				data.getRoot(setting).expandTriggerFlag = true;
				view.switchNode(setting, node);
			} else {
				if (tools.apply(setting.callback.beforeExpand, [setting.treeId, node], true) == false) return true;
				data.getRoot(setting).expandTriggerFlag = true;
				view.switchNode(setting, node);
			}
			return true;
		},
		onClickNode: function (event, node) {
			var setting = settings[event.data.treeId],
			clickFlag = ( (setting.view.autoCancelSelected && event.ctrlKey) && data.isSelectedNode(setting, node)) ? 0 : (setting.view.autoCancelSelected && event.ctrlKey && setting.view.selectedMulti) ? 2 : 1;
			if (tools.apply(setting.callback.beforeClick, [setting.treeId, node, clickFlag], true) == false) return true;
			if (clickFlag === 0) {
				view.cancelPreSelectedNode(setting, node);
			} else {
				view.selectNode(setting, node, clickFlag === 2);
			}
			setting.treeObj.trigger(consts.event.CLICK, [setting.treeId, node, clickFlag]);
			return true;
		},
		onZTreeMousedown: function(event, node) {
			var setting = settings[event.data.treeId];
			if (tools.apply(setting.callback.beforeMouseDown, [setting.treeId, node], true)) {
				tools.apply(setting.callback.onMouseDown, [event, setting.treeId, node]);
			}
			return true;
		},
		onZTreeMouseup: function(event, node) {
			var setting = settings[event.data.treeId];
			if (tools.apply(setting.callback.beforeMouseUp, [setting.treeId, node], true)) {
				tools.apply(setting.callback.onMouseUp, [event, setting.treeId, node]);
			}
			return true;
		},
		onZTreeDblclick: function(event, node) {
			var setting = settings[event.data.treeId];
			if (tools.apply(setting.callback.beforeDblClick, [setting.treeId, node], true)) {
				tools.apply(setting.callback.onDblClick, [event, setting.treeId, node]);
			}
			return true;
		},
		onZTreeContextmenu: function(event, node) {
			var setting = settings[event.data.treeId];
			if (tools.apply(setting.callback.beforeRightClick, [setting.treeId, node], true)) {
				tools.apply(setting.callback.onRightClick, [event, setting.treeId, node]);
			}
			return (typeof setting.callback.onRightClick) != "function";
		}
	},
	//method of tools for zTree
	tools = {
		apply: function(fun, param, defaultValue) {
			if ((typeof fun) == "function") {
				return fun.apply(zt, param?param:[]);
			}
			return defaultValue;
		},
		canAsync: function(setting, node) {
			var childKey = setting.data.key.children;
			return (node && node.isParent && !(node.zAsync || (node[childKey] && node[childKey].length > 0)));
		},
		clone: function (jsonObj) {
			var buf;
			if (jsonObj instanceof Array) {
				buf = [];
				var i = jsonObj.length;
				while (i--) {
					buf[i] = arguments.callee(jsonObj[i]);
				}
				return buf;
			}else if (typeof jsonObj == "function"){
				return jsonObj;
			}else if (jsonObj instanceof Object){
				buf = {};
				for (var k in jsonObj) {
					buf[k] = arguments.callee(jsonObj[k]);
				}
				return buf;
			}else{
				return jsonObj;
			}
		},
		eqs: function(str1, str2) {
			return str1.toLowerCase() === str2.toLowerCase();
		},
		isArray: function(arr) {
			return Object.prototype.toString.apply(arr) === "[object Array]";
		},
		getMDom: function (setting, curDom, targetExpr) {
			if (!curDom) return null;
			while (curDom && curDom.id !== setting.treeId) {
				for (var i=0, l=targetExpr.length; curDom.tagName && i<l; i++) {
					if (tools.eqs(curDom.tagName, targetExpr[i].tagName) && curDom.getAttribute(targetExpr[i].attrName) !== null) {
						return curDom;
					}
				}
				curDom = curDom.parentNode;
			}
			return null;
		},
		noSel: function(setting) {
			var r = data.getRoot(setting);
			if (r.noSelection) {
				try {
					window.getSelection ? window.getSelection().removeAllRanges() : document.selection.empty();
				} catch(e){}
			}
		},
		uCanDo: function(setting, e) {
			return true;
		}
	},
	//method of operate ztree dom
	view = {
		addNodes: function(setting, parentNode, newNodes, isSilent) {
			if (setting.data.keep.leaf && parentNode && !parentNode.isParent) {
				return;
			}
			if (!tools.isArray(newNodes)) {
				newNodes = [newNodes];
			}
			if (setting.data.simpleData.enable) {
				newNodes = data.transformTozTreeFormat(setting, newNodes);
			}
			if (parentNode) {
				var target_switchObj = $("#" + parentNode.tId + consts.id.SWITCH),
				target_icoObj = $("#" + parentNode.tId + consts.id.ICON),
				target_ulObj = $("#" + parentNode.tId + consts.id.UL);

				if (!parentNode.open) {
					view.replaceSwitchClass(parentNode, target_switchObj, consts.folder.CLOSE);
					view.replaceIcoClass(parentNode, target_icoObj, consts.folder.CLOSE);
					parentNode.open = false;
					target_ulObj.css({
						"display": "none"
					});
				}

				data.addNodesData(setting, parentNode, newNodes);
				view.createNodes(setting, parentNode.level + 1, newNodes, parentNode);
				if (!isSilent) {
					view.expandCollapseParentNode(setting, parentNode, true);
				}
			} else {
				data.addNodesData(setting, data.getRoot(setting), newNodes);
				view.createNodes(setting, 0, newNodes, null);
			}
		},
		appendNodes: function(setting, level, nodes, parentNode, initFlag, openFlag) {
			if (!nodes) return [];
			var html = [],
			childKey = setting.data.key.children,
			nameKey = setting.data.key.name,
			titleKey = data.getTitleKey(setting);
			for (var i = 0, l = nodes.length; i < l; i++) {
				var node = nodes[i],
				tmpPNode = (parentNode) ? parentNode: data.getRoot(setting),
				tmpPChild = tmpPNode[childKey],
				isFirstNode = ((tmpPChild.length == nodes.length) && (i == 0)),
				isLastNode = (i == (nodes.length - 1));
				if (initFlag) {
					data.initNode(setting, level, node, parentNode, isFirstNode, isLastNode, openFlag);
					data.addNodeCache(setting, node);
				}

				var childHtml = [];
				if (node[childKey] && node[childKey].length > 0) {
					//make child html first, because checkType
					childHtml = view.appendNodes(setting, level + 1, node[childKey], node, initFlag, openFlag && node.open);
				}
				if (openFlag) {
					var url = view.makeNodeUrl(setting, node),
					fontcss = view.makeNodeFontCss(setting, node),
					fontStyle = [];
					for (var f in fontcss) {
						fontStyle.push(f, ":", fontcss[f], ";");
					}
					html.push("<li id='", node.tId, "' class='level", node.level,"' treenode>",
						"<button type='button' hidefocus='true'",(node.isParent?"":"disabled")," id='", node.tId, consts.id.SWITCH,
						"' title='' class='", view.makeNodeLineClass(setting, node), "' treeNode", consts.id.SWITCH,"></button>");
					data.getBeforeA(setting, node, html);
					html.push("<a id='", node.tId, consts.id.A, "' class='level", node.level,"' treeNode", consts.id.A," onclick=\"", (node.click || ''),
						"\" ", ((url != null && url.length > 0) ? "href='" + url + "'" : ""), " target='",view.makeNodeTarget(node),"' style='", fontStyle.join(''),
						"'");
					if (tools.apply(setting.view.showTitle, [setting.treeId, node], setting.view.showTitle) && node[titleKey]) {html.push("title='", node[titleKey].replace(/'/g,"&#39;").replace(/</g,'&lt;').replace(/>/g,'&gt;'),"'");}
					html.push(">");
					data.getInnerBeforeA(setting, node, html);
					var name = setting.view.nameIsHTML ? node[nameKey] : node[nameKey].replace(/&/g,'&amp;').replace(/</g,'&lt;').replace(/>/g,'&gt;');
					html.push("<button type='button' hidefocus='true' id='", node.tId, consts.id.ICON,
						"' title='' treeNode", consts.id.ICON," class='", view.makeNodeIcoClass(setting, node), "' style='", view.makeNodeIcoStyle(setting, node), "'></button><span id='", node.tId, consts.id.SPAN,
						"'>",name,"</span>");
					data.getInnerAfterA(setting, node, html);
					html.push("</a>");
					data.getAfterA(setting, node, html);
					if (node.isParent && node.open) {
						view.makeUlHtml(setting, node, html, childHtml.join(''));
					}
					html.push("</li>");
					data.addCreatedNode(setting, node);
				}
			}
			return html;
		},
		appendParentULDom: function(setting, node) {
			var html = [],
			nObj = $("#" + node.tId),
			ulObj = $("#" + node.tId + consts.id.UL),
			childKey = setting.data.key.children,
			childHtml = view.appendNodes(setting, node.level+1, node[childKey], node, false, true);
			view.makeUlHtml(setting, node, html, childHtml.join(''));
			if (!nObj.get(0) && !!node.parentTId) {
				view.appendParentULDom(setting, node.getParentNode());
				nObj = $("#" + node.tId);
			}
			if (ulObj.get(0)) {
				ulObj.remove();
			}

			nObj.append(html.join(''));
			view.createNodeCallback(setting);
		},
		asyncNode: function(setting, node, isSilent, callback) {
			var i, l;
			if (node && !node.isParent) {
				tools.apply(callback);
				return false;
			} else if (node && node.isAjaxing) {
				return false;
			} else if (tools.apply(setting.callback.beforeAsync, [setting.treeId, node], true) == false) {
				tools.apply(callback);
				return false;
			}
			if (node) {
				node.isAjaxing = true;
				var icoObj = $("#" + node.tId + consts.id.ICON);
				icoObj.attr({"style":"", "class":"ico_loading"});
			}

			var isJson = (setting.async.contentType == "application/json"), tmpParam = isJson ? "{" : "", jTemp="";
			for (i = 0, l = setting.async.autoParam.length; node && i < l; i++) {
				var pKey = setting.async.autoParam[i].split("="), spKey = pKey;
				if (pKey.length>1) {
					spKey = pKey[1];
					pKey = pKey[0];
				}
				if (isJson) {
					jTemp = (typeof node[pKey] == "string") ? '"' : '';
					tmpParam += '"' + spKey + ('":' + jTemp + node[pKey]).replace(/'/g,'\\\'') + jTemp + ',';
				} else {
					tmpParam += spKey + ("=" + node[pKey]).replace(/&/g,'%26') + "&";
				}
			}
			if (tools.isArray(setting.async.otherParam)) {
				for (i = 0, l = setting.async.otherParam.length; i < l; i += 2) {
					if (isJson) {
						jTemp = (typeof setting.async.otherParam[i + 1] == "string") ? '"' : '';
						tmpParam += '"' + setting.async.otherParam[i] + ('":' + jTemp + setting.async.otherParam[i + 1]).replace(/'/g,'\\\'') + jTemp + ",";
					} else {
						tmpParam += setting.async.otherParam[i] + ("=" + setting.async.otherParam[i + 1]).replace(/&/g,'%26') + "&";
					}
				}
			} else {
				for (var p in setting.async.otherParam) {
					if (isJson) {
						jTemp = (typeof setting.async.otherParam[p] == "string") ? '"' : '';
						tmpParam += '"' + p + ('":' + jTemp + setting.async.otherParam[p]).replace(/'/g,'\\\'') + jTemp + ",";
					} else {
						tmpParam += p + ("=" + setting.async.otherParam[p]).replace(/&/g,'%26') + "&";
					}
				}
			}
			if (tmpParam.length > 1) tmpParam = tmpParam.substring(0, tmpParam.length-1);
			if (isJson) tmpParam += "}";

			$.ajax({
				contentType: setting.async.contentType,
				type: setting.async.type,
				url: tools.apply(setting.async.url, [setting.treeId, node], setting.async.url),
				data: tmpParam,
				dataType: setting.async.dataType,
				success: function(msg) {
					var newNodes = [];
					try {
						if (!msg || msg.length == 0) {
							newNodes = [];
						} else if (typeof msg == "string") {
							newNodes = eval("(" + msg + ")");
						} else {
							newNodes = msg;
						}
					} catch(err) {}

					if (node) {
						node.isAjaxing = null;
						node.zAsync = true;
					}
					view.setNodeLineIcos(setting, node);
					if (newNodes && newNodes != "") {
						newNodes = tools.apply(setting.async.dataFilter, [setting.treeId, node, newNodes], newNodes);
						view.addNodes(setting, node, tools.clone(newNodes), !!isSilent);
					} else {
						view.addNodes(setting, node, [], !!isSilent);
					}
					setting.treeObj.trigger(consts.event.ASYNC_SUCCESS, [setting.treeId, node, msg]);
					tools.apply(callback);
				},
				error: function(XMLHttpRequest, textStatus, errorThrown) {
					view.setNodeLineIcos(setting, node);
					if (node) node.isAjaxing = null;
					setting.treeObj.trigger(consts.event.ASYNC_ERROR, [setting.treeId, node, XMLHttpRequest, textStatus, errorThrown]);
				}
			});
			return true;
		},
		cancelPreSelectedNode: function (setting, node) {
			var list = data.getRoot(setting).curSelectedList;
			for (var i=0, j=list.length-1; j>=i; j--) {
				if (!node || node === list[j]) {
					$("#" + list[j].tId + consts.id.A).removeClass(consts.node.CURSELECTED);
					view.setNodeName(setting, list[j]);
					if (node) {
						data.removeSelectedNode(setting, node);
						break;
					}
				}
			}
			if (!node) data.getRoot(setting).curSelectedList = [];
		},
		createNodeCallback: function(setting) {
			if (!!setting.callback.onNodeCreated || !!setting.view.addDiyDom) {
				var root = data.getRoot(setting);
				while (root.createdNodes.length>0) {
					var node = root.createdNodes.shift();
					tools.apply(setting.view.addDiyDom, [setting.treeId, node]);
					if (!!setting.callback.onNodeCreated) {
						setting.treeObj.trigger(consts.event.NODECREATED, [setting.treeId, node]);
					}
				}
			}
		},
		createNodes: function(setting, level, nodes, parentNode) {
			if (!nodes || nodes.length == 0) return;
			var root = data.getRoot(setting),
			childKey = setting.data.key.children,
			openFlag = !parentNode || parentNode.open || !!$("#" + parentNode[childKey][0].tId).get(0);
			root.createdNodes = [];
			var zTreeHtml = view.appendNodes(setting, level, nodes, parentNode, true, openFlag);
			if (!parentNode) {
				setting.treeObj.append(zTreeHtml.join(''));
			} else {
				var ulObj = $("#" + parentNode.tId + consts.id.UL);
				if (ulObj.get(0)) {
					ulObj.append(zTreeHtml.join(''));
				}
			}
			view.createNodeCallback(setting);
		},
		expandCollapseNode: function(setting, node, expandFlag, animateFlag, callback) {
			var root = data.getRoot(setting),
			childKey = setting.data.key.children;
			if (!node) {
				tools.apply(callback, []);
				return;
			}
			if (root.expandTriggerFlag) {
				var _callback = callback;
				callback = function(){
					if (_callback) _callback();
					if (node.open) {
						setting.treeObj.trigger(consts.event.EXPAND, [setting.treeId, node]);
					} else {
						setting.treeObj.trigger(consts.event.COLLAPSE, [setting.treeId, node]);
					}
				};
				root.expandTriggerFlag = false;
			}
			if (node.open == expandFlag) {
				tools.apply(callback, []);
				return;
			}
			if (!node.open && node.isParent && ((!$("#" + node.tId + consts.id.UL).get(0)) || (node[childKey] && node[childKey].length>0 && !$("#" + node[childKey][0].tId).get(0)))) {
				view.appendParentULDom(setting, node);
			}
			var ulObj = $("#" + node.tId + consts.id.UL),
			switchObj = $("#" + node.tId + consts.id.SWITCH),
			icoObj = $("#" + node.tId + consts.id.ICON);

			if (node.isParent) {
				node.open = !node.open;
				if (node.iconOpen && node.iconClose) {
					icoObj.attr("style", view.makeNodeIcoStyle(setting, node));
				}

				if (node.open) {
					view.replaceSwitchClass(node, switchObj, consts.folder.OPEN);
					view.replaceIcoClass(node, icoObj, consts.folder.OPEN);
					if (animateFlag == false || setting.view.expandSpeed == "") {
						ulObj.show();
						tools.apply(callback, []);
					} else {
						if (node[childKey] && node[childKey].length > 0) {
							ulObj.slideDown(setting.view.expandSpeed, callback);
						} else {
							ulObj.show();
							tools.apply(callback, []);
						}
					}
				} else {
					view.replaceSwitchClass(node, switchObj, consts.folder.CLOSE);
					view.replaceIcoClass(node, icoObj, consts.folder.CLOSE);
					if (animateFlag == false || setting.view.expandSpeed == "") {
						ulObj.hide();
						tools.apply(callback, []);
					} else {
						ulObj.slideUp(setting.view.expandSpeed, callback);
					}
				}
			} else {
				tools.apply(callback, []);
			}
		},
		expandCollapseParentNode: function(setting, node, expandFlag, animateFlag, callback) {
			if (!node) return;
			if (!node.parentTId) {
				view.expandCollapseNode(setting, node, expandFlag, animateFlag, callback);
				return;
			} else {
				view.expandCollapseNode(setting, node, expandFlag, animateFlag);
			}
			if (node.parentTId) {
				view.expandCollapseParentNode(setting, node.getParentNode(), expandFlag, animateFlag, callback);
			}
		},
		expandCollapseSonNode: function(setting, node, expandFlag, animateFlag, callback) {
			var root = data.getRoot(setting),
			childKey = setting.data.key.children,
			treeNodes = (node) ? node[childKey]: root[childKey],
			selfAnimateSign = (node) ? false : animateFlag,
			expandTriggerFlag = data.getRoot(setting).expandTriggerFlag;
			data.getRoot(setting).expandTriggerFlag = false;
			if (treeNodes) {
				for (var i = 0, l = treeNodes.length; i < l; i++) {
					if (treeNodes[i]) view.expandCollapseSonNode(setting, treeNodes[i], expandFlag, selfAnimateSign);
				}
			}
			data.getRoot(setting).expandTriggerFlag = expandTriggerFlag;
			view.expandCollapseNode(setting, node, expandFlag, animateFlag, callback );
		},
		makeNodeFontCss: function(setting, node) {
			var fontCss = tools.apply(setting.view.fontCss, [setting.treeId, node], setting.view.fontCss);
			return (fontCss && ((typeof fontCss) != "function")) ? fontCss : {};
		},
		makeNodeIcoClass: function(setting, node) {
			var icoCss = ["ico"];
			if (!node.isAjaxing) {
				icoCss[0] = (node.iconSkin ? node.iconSkin + "_" : "") + icoCss[0];
				if (node.isParent) {
					icoCss.push(node.open ? consts.folder.OPEN : consts.folder.CLOSE);
				} else {
					icoCss.push(consts.folder.DOCU);
				}
			}
			return icoCss.join('_');
		},
		makeNodeIcoStyle: function(setting, node) {
			var icoStyle = [];
			if (!node.isAjaxing) {
				var icon = (node.isParent && node.iconOpen && node.iconClose) ? (node.open ? node.iconOpen : node.iconClose) : node.icon;
				if (icon && node.level==0) icoStyle.push("background:url(/JBIZ/portal2/images/icon/", icon, ") 0 0 no-repeat;");
				if (setting.view.showIcon == false || !tools.apply(setting.view.showIcon, [setting.treeId, node], true)) {
					icoStyle.push("width:0px;height:0px;");
				}
			}
			return icoStyle.join('');
		},
		makeNodeLineClass: function(setting, node) {
			var lineClass = [];
			if (setting.view.showLine) {
				if (node.level == 0 && node.isFirstNode && node.isLastNode) {
					lineClass.push(consts.line.ROOT);
				} else if (node.level == 0 && node.isFirstNode) {
					lineClass.push(consts.line.ROOTS);
				} else if (node.isLastNode) {
					lineClass.push(consts.line.BOTTOM);
				} else {
					lineClass.push(consts.line.CENTER);
				}
			} else {
				lineClass.push(consts.line.NOLINE);
			}
			if (node.isParent) {
				lineClass.push(node.open ? consts.folder.OPEN : consts.folder.CLOSE);
			} else {
				lineClass.push(consts.folder.DOCU);
			}
			return view.makeNodeLineClassEx(node) + lineClass.join('_');
		},
		makeNodeLineClassEx: function(node) {
			return "level" + node.level + " switch ";
		},
		makeNodeTarget: function(node) {
			return (node.target || "_blank");
		},
		makeNodeUrl: function(setting, node) {
			return node.url ? node.url : null;
		},
		makeUlHtml: function(setting, node, html, content) {
			html.push("<ul id='", node.tId, consts.id.UL, "' class='level", node.level, " ", view.makeUlLineClass(setting, node), "' style='display:", (node.open ? "block": "none"),"'>");
			html.push(content);
			html.push("</ul>");
		},
		makeUlLineClass: function(setting, node) {
			return ((setting.view.showLine && !node.isLastNode) ? consts.line.LINE : "");
		},
		replaceIcoClass: function(node, obj, newName) {
			if (!obj || node.isAjaxing) return;
			var tmpName = obj.attr("class");
			if (tmpName == undefined) return;
			var tmpList = tmpName.split("_");
			switch (newName) {
				case consts.folder.OPEN:
				case consts.folder.CLOSE:
				case consts.folder.DOCU:
					tmpList[tmpList.length-1] = newName;
					break;
			}
			obj.attr("class", tmpList.join("_"));
		},
		replaceSwitchClass: function(node, obj, newName) {
			if (!obj) return;
			var tmpName = obj.attr("class");
			if (tmpName == undefined) return;
			var tmpList = tmpName.split("_");
			switch (newName) {
				case consts.line.ROOT:
				case consts.line.ROOTS:
				case consts.line.CENTER:
				case consts.line.BOTTOM:
				case consts.line.NOLINE:
					tmpList[0] = view.makeNodeLineClassEx(node) + newName;
					break;
				case consts.folder.OPEN:
				case consts.folder.CLOSE:
				case consts.folder.DOCU:
					tmpList[1] = newName;
					break;
			}
			obj.attr("class", tmpList.join("_"));
			if (newName !== consts.folder.DOCU) {
				obj.removeAttr("disabled");
			} else {
				obj.attr("disabled", "disabled");
			}
		},
		selectNode: function(setting, node, addFlag) {
			if (!addFlag) {
				view.cancelPreSelectedNode(setting);
			}
			$("#" + node.tId + consts.id.A).addClass(consts.node.CURSELECTED);
			data.addSelectedNode(setting, node);
		},
		setNodeFontCss: function(setting, treeNode) {
			var aObj = $("#" + treeNode.tId + consts.id.A),
			fontCss = view.makeNodeFontCss(setting, treeNode);
			if (fontCss) {
				aObj.css(fontCss);
			}
		},
		setNodeLineIcos: function(setting, node) {
			if (!node) return;
			var switchObj = $("#" + node.tId + consts.id.SWITCH),
			ulObj = $("#" + node.tId + consts.id.UL),
			icoObj = $("#" + node.tId + consts.id.ICON),
			ulLine = view.makeUlLineClass(setting, node);
			if (ulLine.length==0) {
				ulObj.removeClass(consts.line.LINE);
			} else {
				ulObj.addClass(ulLine);
			}
			switchObj.attr("class", view.makeNodeLineClass(setting, node));
			if (node.isParent) {
				switchObj.removeAttr("disabled");
			} else {
				switchObj.attr("disabled", "disabled");
			}
			icoObj.removeAttr("style");
			icoObj.attr("style", view.makeNodeIcoStyle(setting, node));
			icoObj.attr("class", view.makeNodeIcoClass(setting, node));
		},
		setNodeName: function(setting, node) {
			var nameKey = setting.data.key.name,
			titleKey = data.getTitleKey(setting),
			nObj = $("#" + node.tId + consts.id.SPAN);
			nObj.empty();
			if (setting.view.nameIsHTML) {
				nObj.html(node[nameKey]);
			} else {
				nObj.text(node[nameKey]);
			}
			if (tools.apply(setting.view.showTitle, [setting.treeId, node], setting.view.showTitle) && node[titleKey]) {
				var aObj = $("#" + node.tId + consts.id.A);
				aObj.attr("title", node[titleKey]);
			}
		},
		setNodeTarget: function(node) {
			var aObj = $("#" + node.tId + consts.id.A);
			aObj.attr("target", view.makeNodeTarget(node));
		},
		setNodeUrl: function(setting, node) {
			var aObj = $("#" + node.tId + consts.id.A),
			url = view.makeNodeUrl(setting, node);
			if (url == null || url.length == 0) {
				aObj.removeAttr("href");
			} else {
				aObj.attr("href", url);
			}
		},
		switchNode: function(setting, node) {
			if (node.open || !tools.canAsync(setting, node)) {
				view.expandCollapseNode(setting, node, !node.open);
			} else if (setting.async.enable) {
				if (!view.asyncNode(setting, node)) {
					view.expandCollapseNode(setting, node, !node.open);
					return;
				}
			} else if (node) {
				view.expandCollapseNode(setting, node, !node.open);
			}
		}
	};
	// zTree defind
	$.fn.zTree = {
		consts : _consts,
		_z : {
			tools: tools,
			view: view,
			event: event,
			data: data
		},
		getZTreeObj: function(treeId) {
			var o = data.getZTreeTools(treeId);
			return o ? o : null;
		},
		init: function(obj, zSetting, zNodes) {
			var setting = tools.clone(_setting);
			$.extend(true, setting, zSetting);
			setting.treeId = obj.attr("id");
			setting.treeObj = obj;
			setting.treeObj.empty();
			settings[setting.treeId] = setting;
			if ($.browser.msie && parseInt($.browser.version)<7) {
				setting.view.expandSpeed = "";
			}

			data.initRoot(setting);
			var root = data.getRoot(setting),
			childKey = setting.data.key.children;
			zNodes = zNodes ? tools.clone(tools.isArray(zNodes)? zNodes : [zNodes]) : [];
			if (setting.data.simpleData.enable) {
				root[childKey] = data.transformTozTreeFormat(setting, zNodes);
			} else {
				root[childKey] = zNodes;
			}

			data.initCache(setting);
			event.bindTree(setting);
			event.bindEvent(setting);
			
			var zTreeTools = {
				setting: setting,
				cancelSelectedNode : function(node) {
					view.cancelPreSelectedNode(this.setting, node);
				},
				expandAll : function(expandFlag) {
					expandFlag = !!expandFlag;
					view.expandCollapseSonNode(this.setting, null, expandFlag, true);
					return expandFlag;
				},
				expandNode : function(node, expandFlag, sonSign, focus, callbackFlag) {
					if (!node || !node.isParent) return null;
					if (expandFlag !== true && expandFlag !== false) {
						expandFlag = !node.open;
					}
					callbackFlag = !!callbackFlag;

					if (callbackFlag && expandFlag && (tools.apply(setting.callback.beforeExpand, [setting.treeId, node], true) == false)) {
						return null;
					} else if (callbackFlag && !expandFlag && (tools.apply(setting.callback.beforeCollapse, [setting.treeId, node], true) == false)) {
						return null;
					}
					if (expandFlag && node.parentTId) {
						view.expandCollapseParentNode(this.setting, node.getParentNode(), expandFlag, false);
					}
					if (expandFlag === node.open && !sonSign) {
						return null;
					}
					
					data.getRoot(setting).expandTriggerFlag = callbackFlag;
					if (sonSign) {
						view.expandCollapseSonNode(this.setting, node, expandFlag, true, function() {
							if (focus !== false) {$("#" + node.tId + consts.id.ICON).focus().blur();}
						});
					} else {
						node.open = !expandFlag;
						view.switchNode(this.setting, node);
						if (focus !== false) {$("#" + node.tId + consts.id.ICON).focus().blur();}
					}
					return expandFlag;
				},
				getNodes : function() {
					return data.getNodes(this.setting);
				},
				getNodeByParam : function(key, value, parentNode) {
					if (!key) return null;
					return data.getNodeByParam(this.setting, parentNode?parentNode[this.setting.data.key.children]:data.getNodes(this.setting), key, value);
				},
				getNodeByTId : function(tId) {
					return data.getNodeCache(this.setting, tId);
				},
				getNodesByParam : function(key, value, parentNode) {
					if (!key) return null;
					return data.getNodesByParam(this.setting, parentNode?parentNode[this.setting.data.key.children]:data.getNodes(this.setting), key, value);
				},
				getNodesByParamFuzzy : function(key, value, parentNode) {
					if (!key) return null;
					return data.getNodesByParamFuzzy(this.setting, parentNode?parentNode[this.setting.data.key.children]:data.getNodes(this.setting), key, value);
				},
				getNodeIndex : function(node) {
					if (!node) return null;
					var childKey = setting.data.key.children,
					parentNode = (node.parentTId) ? node.getParentNode() : data.getRoot(this.setting);
					for (var i=0, l = parentNode[childKey].length; i < l; i++) {
						if (parentNode[childKey][i] == node) return i;
					}
					return -1;
				},
				getSelectedNodes : function() {
					var r = [], list = data.getRoot(this.setting).curSelectedList;
					for (var i=0, l=list.length; i<l; i++) {
						r.push(list[i]);
					}
					return r;
				},
				isSelectedNode : function(node) {
					return data.isSelectedNode(this.setting, node);
				},
				reAsyncChildNodes : function(parentNode, reloadType, isSilent) {
					if (!this.setting.async.enable) return;
					var isRoot = !parentNode;
					if (isRoot) {
						parentNode = data.getRoot(this.setting);
					}
					if (reloadType=="refresh") {
						parentNode[this.setting.data.key.children] = [];
						if (isRoot) {
							this.setting.treeObj.empty();
						} else {
							var ulObj = $("#" + parentNode.tId + consts.id.UL);
							ulObj.empty();
						}
					}
					view.asyncNode(this.setting, isRoot? null:parentNode, !!isSilent);
				},
				refresh : function() {
					this.setting.treeObj.empty();
					var root = data.getRoot(this.setting),
					nodes = root[this.setting.data.key.children];
					data.initRoot(this.setting);
					root[this.setting.data.key.children] = nodes;
					data.initCache(this.setting);
					view.createNodes(this.setting, 0, root[this.setting.data.key.children]);
				},
				selectNode : function(node, addFlag) {
					if (!node) return;
					if (tools.uCanDo(this.setting)) {
						addFlag = setting.view.selectedMulti && addFlag;
						if (node.parentTId) {
							view.expandCollapseParentNode(this.setting, node.getParentNode(), true, false, function() {
								$("#" + node.tId + consts.id.ICON).focus().blur();
							});
						} else {
							$("#" + node.tId + consts.id.ICON).focus().blur();
						}
						view.selectNode(this.setting, node, addFlag);
					}
				},
				transformTozTreeNodes : function(simpleNodes) {
					return data.transformTozTreeFormat(this.setting, simpleNodes);
				},
				transformToArray : function(nodes) {
					return data.transformToArrayFormat(this.setting, nodes);
				},
				updateNode : function(node, checkTypeFlag) {
					if (!node) return;
					var nObj = $("#" + node.tId);
					if (nObj.get(0) && tools.uCanDo(this.setting)) {
						view.setNodeName(this.setting, node);
						view.setNodeTarget(node);
						view.setNodeUrl(this.setting, node);
						view.setNodeLineIcos(this.setting, node);
						view.setNodeFontCss(this.setting, node);
					}
				}
			};
			root.treeTools = zTreeTools;
			data.setZTreeTools(setting, zTreeTools);

			if (root[childKey] && root[childKey].length > 0) {
				view.createNodes(setting, 0, root[childKey]);
			} else if (setting.async.enable && setting.async.url && setting.async.url !== '') {
				view.asyncNode(setting);
			}
			return zTreeTools;
		}
	};

	var zt = $.fn.zTree,
	consts = zt.consts;
})(jQuery);

var hexcase = 0; /* hex output format. 0 - lowercase; 1 - uppercase */
var b64pad = ""; /* base-64 pad character. "=" for strict RFC compliance */
var chrsz = 8; /* bits per input character. 8 - ASCII; 16 - Unicode */

/* 
 * These are the functions you'll usually want to call 
 * They take string arguments and return either hex or base-64 encoded strings 
 */
function hex_md5(s) {
	return binl2hex(core_md5(str2binl(s), s.length * chrsz)).toUpperCase();
}
function b64_md5(s) {
	return binl2b64(core_md5(str2binl(s), s.length * chrsz));
}
function hex_hmac_md5(key, data) {
	return binl2hex(core_hmac_md5(key, data));
}
function b64_hmac_md5(key, data) {
	return binl2b64(core_hmac_md5(key, data));
}

/* Backwards compatibility - same as hex_md5() */
function calcMD5(s) {
	return binl2hex(core_md5(str2binl(s), s.length * chrsz));
}

/* 
 * Perform a simple self-test to see if the VM is working 
 */
function md5_vm_test() {
	return hex_md5("abc") == "900150983cd24fb0d6963f7d28e17f72";
}

/* 
 * Calculate the MD5 of an array of little-endian words, and a bit length 
 */
function core_md5(x, len) {
	/* append padding */
	x[len >> 5] |= 0x80 << ((len) % 32);
	x[(((len + 64) >>> 9) << 4) + 14] = len;

	var a = 1732584193;
	var b = -271733879;
	var c = -1732584194;
	var d = 271733878;

	for (var i = 0; i < x.length; i += 16) {
		var olda = a;
		var oldb = b;
		var oldc = c;
		var oldd = d;

		a = md5_ff(a, b, c, d, x[i + 0], 7, -680876936);
		d = md5_ff(d, a, b, c, x[i + 1], 12, -389564586);
		c = md5_ff(c, d, a, b, x[i + 2], 17, 606105819);
		b = md5_ff(b, c, d, a, x[i + 3], 22, -1044525330);
		a = md5_ff(a, b, c, d, x[i + 4], 7, -176418897);
		d = md5_ff(d, a, b, c, x[i + 5], 12, 1200080426);
		c = md5_ff(c, d, a, b, x[i + 6], 17, -1473231341);
		b = md5_ff(b, c, d, a, x[i + 7], 22, -45705983);
		a = md5_ff(a, b, c, d, x[i + 8], 7, 1770035416);
		d = md5_ff(d, a, b, c, x[i + 9], 12, -1958414417);
		c = md5_ff(c, d, a, b, x[i + 10], 17, -42063);
		b = md5_ff(b, c, d, a, x[i + 11], 22, -1990404162);
		a = md5_ff(a, b, c, d, x[i + 12], 7, 1804603682);
		d = md5_ff(d, a, b, c, x[i + 13], 12, -40341101);
		c = md5_ff(c, d, a, b, x[i + 14], 17, -1502002290);
		b = md5_ff(b, c, d, a, x[i + 15], 22, 1236535329);

		a = md5_gg(a, b, c, d, x[i + 1], 5, -165796510);
		d = md5_gg(d, a, b, c, x[i + 6], 9, -1069501632);
		c = md5_gg(c, d, a, b, x[i + 11], 14, 643717713);
		b = md5_gg(b, c, d, a, x[i + 0], 20, -373897302);
		a = md5_gg(a, b, c, d, x[i + 5], 5, -701558691);
		d = md5_gg(d, a, b, c, x[i + 10], 9, 38016083);
		c = md5_gg(c, d, a, b, x[i + 15], 14, -660478335);
		b = md5_gg(b, c, d, a, x[i + 4], 20, -405537848);
		a = md5_gg(a, b, c, d, x[i + 9], 5, 568446438);
		d = md5_gg(d, a, b, c, x[i + 14], 9, -1019803690);
		c = md5_gg(c, d, a, b, x[i + 3], 14, -187363961);
		b = md5_gg(b, c, d, a, x[i + 8], 20, 1163531501);
		a = md5_gg(a, b, c, d, x[i + 13], 5, -1444681467);
		d = md5_gg(d, a, b, c, x[i + 2], 9, -51403784);
		c = md5_gg(c, d, a, b, x[i + 7], 14, 1735328473);
		b = md5_gg(b, c, d, a, x[i + 12], 20, -1926607734);

		a = md5_hh(a, b, c, d, x[i + 5], 4, -378558);
		d = md5_hh(d, a, b, c, x[i + 8], 11, -2022574463);
		c = md5_hh(c, d, a, b, x[i + 11], 16, 1839030562);
		b = md5_hh(b, c, d, a, x[i + 14], 23, -35309556);
		a = md5_hh(a, b, c, d, x[i + 1], 4, -1530992060);
		d = md5_hh(d, a, b, c, x[i + 4], 11, 1272893353);
		c = md5_hh(c, d, a, b, x[i + 7], 16, -155497632);
		b = md5_hh(b, c, d, a, x[i + 10], 23, -1094730640);
		a = md5_hh(a, b, c, d, x[i + 13], 4, 681279174);
		d = md5_hh(d, a, b, c, x[i + 0], 11, -358537222);
		c = md5_hh(c, d, a, b, x[i + 3], 16, -722521979);
		b = md5_hh(b, c, d, a, x[i + 6], 23, 76029189);
		a = md5_hh(a, b, c, d, x[i + 9], 4, -640364487);
		d = md5_hh(d, a, b, c, x[i + 12], 11, -421815835);
		c = md5_hh(c, d, a, b, x[i + 15], 16, 530742520);
		b = md5_hh(b, c, d, a, x[i + 2], 23, -995338651);

		a = md5_ii(a, b, c, d, x[i + 0], 6, -198630844);
		d = md5_ii(d, a, b, c, x[i + 7], 10, 1126891415);
		c = md5_ii(c, d, a, b, x[i + 14], 15, -1416354905);
		b = md5_ii(b, c, d, a, x[i + 5], 21, -57434055);
		a = md5_ii(a, b, c, d, x[i + 12], 6, 1700485571);
		d = md5_ii(d, a, b, c, x[i + 3], 10, -1894986606);
		c = md5_ii(c, d, a, b, x[i + 10], 15, -1051523);
		b = md5_ii(b, c, d, a, x[i + 1], 21, -2054922799);
		a = md5_ii(a, b, c, d, x[i + 8], 6, 1873313359);
		d = md5_ii(d, a, b, c, x[i + 15], 10, -30611744);
		c = md5_ii(c, d, a, b, x[i + 6], 15, -1560198380);
		b = md5_ii(b, c, d, a, x[i + 13], 21, 1309151649);
		a = md5_ii(a, b, c, d, x[i + 4], 6, -145523070);
		d = md5_ii(d, a, b, c, x[i + 11], 10, -1120210379);
		c = md5_ii(c, d, a, b, x[i + 2], 15, 718787259);
		b = md5_ii(b, c, d, a, x[i + 9], 21, -343485551);

		a = safe_add(a, olda);
		b = safe_add(b, oldb);
		c = safe_add(c, oldc);
		d = safe_add(d, oldd);
	}
	return Array(a, b, c, d);

}

/* 
 * These functions implement the four basic operations the algorithm uses. 
 */
function md5_cmn(q, a, b, x, s, t) {
	return safe_add(bit_rol(safe_add(safe_add(a, q), safe_add(x, t)), s), b);
}
function md5_ff(a, b, c, d, x, s, t) {
	return md5_cmn((b & c) | ((~b) & d), a, b, x, s, t);
}
function md5_gg(a, b, c, d, x, s, t) {
	return md5_cmn((b & d) | (c & (~d)), a, b, x, s, t);
}
function md5_hh(a, b, c, d, x, s, t) {
	return md5_cmn(b ^ c ^ d, a, b, x, s, t);
}
function md5_ii(a, b, c, d, x, s, t) {
	return md5_cmn(c ^ (b | (~d)), a, b, x, s, t);
}

/* 
 * Calculate the HMAC-MD5, of a key and some data 
 */
function core_hmac_md5(key, data) {
	var bkey = str2binl(key);
	if (bkey.length > 16)
		bkey = core_md5(bkey, key.length * chrsz);

	var ipad = Array(16), opad = Array(16);
	for (var i = 0; i < 16; i++) {
		ipad[i] = bkey[i] ^ 0x36363636;
		opad[i] = bkey[i] ^ 0x5C5C5C5C;
	}

	var hash = core_md5(ipad.concat(str2binl(data)), 512 + data.length * chrsz);
	return core_md5(opad.concat(hash), 512 + 128);
}

/* 
 * Add integers, wrapping at 2^32. This uses 16-bit operations internally 
 * to work around bugs in some JS interpreters. 
 */
function safe_add(x, y) {
	var lsw = (x & 0xFFFF) + (y & 0xFFFF);
	var msw = (x >> 16) + (y >> 16) + (lsw >> 16);
	return (msw << 16) | (lsw & 0xFFFF);
}

/* 
 * Bitwise rotate a 32-bit number to the left. 
 */
function bit_rol(num, cnt) {
	return (num << cnt) | (num >>> (32 - cnt));
}

/* 
 * Convert a string to an array of little-endian words 
 * If chrsz is ASCII, characters >255 have their hi-byte silently ignored. 
 */
function str2binl(str) {
	var bin = Array();
	var mask = (1 << chrsz) - 1;
	for (var i = 0; i < str.length * chrsz; i += chrsz)
		bin[i >> 5] |= (str.charCodeAt(i / chrsz) & mask) << (i % 32);
	return bin;
}

/* 
 * Convert an array of little-endian words to a hex string. 
 */
function binl2hex(binarray) {
	var hex_tab = hexcase ? "0123456789ABCDEF" : "0123456789abcdef";
	var str = "";
	for (var i = 0; i < binarray.length * 4; i++) {
		str += hex_tab.charAt((binarray[i >> 2] >> ((i % 4) * 8 + 4)) & 0xF)
				+ hex_tab.charAt((binarray[i >> 2] >> ((i % 4) * 8)) & 0xF);
	}
	return str;
}

/* 
 * Convert an array of little-endian words to a base-64 string 
 */
function binl2b64(binarray) {
	var tab = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";
	var str = "";
	for (var i = 0; i < binarray.length * 4; i += 3) {
		var triplet = (((binarray[i >> 2] >> 8 * (i % 4)) & 0xFF) << 16)
				| (((binarray[i + 1 >> 2] >> 8 * ((i + 1) % 4)) & 0xFF) << 8)
				| ((binarray[i + 2 >> 2] >> 8 * ((i + 2) % 4)) & 0xFF);
		for (var j = 0; j < 4; j++) {
			if (i * 8 + j * 6 > binarray.length * 32)
				str += b64pad;
			else
				str += tab.charAt((triplet >> 6 * (3 - j)) & 0x3F);
		}
	}
	return str;
}

/*
/*
 * JPolite Core Features and Functions
 */
if (!$.jpolite) $.jpolite = {};
$.extend($.jpolite, {
	getTab : function(tId){
		return $.indexpageTabs.getTabById(tId);
	},
	addTab : function(tid, p){
		var html = "<iframe frameborder='0' class='func_body_frame' id='"+tid+"' name='"+tid+"' src='' style='width:100%;height:100%;overflow:auto;'/>";
		$.indexpageTabs.addTab(tid, p.name, html, true);
		$('#content_loading').show();
		$("iframe[id='"+tid+"']").src(p.url,function(){
			$('#content_loading').hide();
		});
	},
	getTabID : function(){
		return $.indexpageTabs.getActiveTab();
	},
	removeTab : function(tId){
		$.indexpageTabs.removeTab(tId);
	},
	gotoTab : function(tId){
		$.indexpageTabs.setActiveTab(tId);
	},
	//初始化导航标签
	init: function(options){
		//初始化连接Portal信息；
		$.jpolite.clientInfo.init();
		
		this.Content.init(s.widgetSortable);
		//初始化桌面信息
		this.clientInfo.initClientInfo();
	},
	//初始化用户信息与服务器信息
	clientInfo:{
		username:"",
		businessid:"",
		uiserverremoteurl:"",
		locale:"",
		orgfullname:"",
		personid:"",
		personname:"",
		personcode:"",
		positionid:"",
		positioncode:"",
		positionname:"",
		positionfid:"",
		positionfcode:"",
		positionfname:"",
		deptid:"",
		deptcode:"",
		deptname:"",
		deptfid:"",
		deptfcode:"",
		deptfname:"",
		orgid:"",
		orgcode:"",
		orgname:"",
		orgfid:"",
		orgfcode:"",
		orgfname:"",
		ognid:"",
		ogncode:"",
		ognname:"",
		ognfid:"",
		ognfcode:"",
		ognfname:"",
		personfid:"",
		personfcode:"",
		personfname:"",
	    init:function(options){
	    	var o ={};
			$.extend(o,options,{
				url:"controller/system/User/initPortalInfo",
				async:false,
				type:"POST",
				dataFilter: function(data, type){
					var items = window["eval"]("(" + data+ ")");
					return items;
				},
				error:function(XMLHttpRequest, textStatus, errorThrown){
					//alert("error");
				},
				success:function(data, textStatus){
					var datainfo = window["eval"]("(" + data[0].data + ")");
					//alert(datainfo.personid);
					$.jpolite.clientInfo.orgfullname = datainfo.orgFullName;
					$.jpolite.clientInfo.personid = datainfo.personid;
					$.jpolite.clientInfo.personname = datainfo.personName;
					$.jpolite.clientInfo.username = datainfo.username;
					$.jpolite.clientInfo.personcode = datainfo.personcode;
					$.jpolite.clientInfo.positionid = datainfo.positionid;
					$.jpolite.clientInfo.positioncode = datainfo.positioncode;
					$.jpolite.clientInfo.positionname = datainfo.positionname;
					$.jpolite.clientInfo.positionfid = datainfo.positionfid;
					$.jpolite.clientInfo.positionfcode = datainfo.positionfcode;
					$.jpolite.clientInfo.positionfname = datainfo.positionfname;
					$.jpolite.clientInfo.deptid = datainfo.deptid;
					$.jpolite.clientInfo.deptcode = datainfo.deptcode;
					$.jpolite.clientInfo.deptname = datainfo.deptname;
					$.jpolite.clientInfo.deptfid = datainfo.deptfid;
					$.jpolite.clientInfo.deptfcode = datainfo.deptfcode;
					$.jpolite.clientInfo.deptfname = datainfo.deptfname;
					$.jpolite.clientInfo.orgid = datainfo.orgid;
					$.jpolite.clientInfo.orgcode = datainfo.orgcode;
					$.jpolite.clientInfo.orgname = datainfo.orgname;
					$.jpolite.clientInfo.orgfid = datainfo.orgfid;
					$.jpolite.clientInfo.orgfcode = datainfo.orgfcode;
					$.jpolite.clientInfo.orgfname = datainfo.orgfname;
					$.jpolite.clientInfo.ognid = datainfo.ognid;
					$.jpolite.clientInfo.ogncode = datainfo.ogncode;
					$.jpolite.clientInfo.ognname = datainfo.ognname;
					$.jpolite.clientInfo.ognfid = datainfo.ognfid;
					$.jpolite.clientInfo.ognfcode = datainfo.ognfcode;
					$.jpolite.clientInfo.ognfname = datainfo.ognfname;
					$.jpolite.clientInfo.personfid = datainfo.personfid;
					$.jpolite.clientInfo.personfcode = datainfo.personfcode;
					$.jpolite.clientInfo.personfname = datainfo.personfname;
					$.jpolite.clientInfo.businessid = datainfo.businessid;
					$.jpolite.clientInfo.locale = datainfo.locale;
					$.jpolite.clientInfo.uiserverremoteurl = datainfo.uiserverremoteurl;
				}
			});
			//$.ajax(o);
		},
		initClientInfo:function(){
			var m = $.jpolite.clientInfo.orgfullname.split("/");
			$("#footer_status_info").html($.jpolite.clientInfo.personfname);
			$("#username_layout").html($.jpolite.clientInfo.personname);
		}
	}
});

jQuery.fn.extend({
	load: function( url, params, callback, async) {
		if ( typeof url !== "string" ) {
			return _load.call( this, url );
		} else if ( !this.length ) {
			return this;
		}
		var off = url.indexOf(" ");
		if ( off >= 0 ) {
			var selector = url.slice(off, url.length);
			url = url.slice(0, off);
		}
		var type = "GET";
		if ( params ) {
			if ( jQuery.isFunction( params ) ) {
				callback = params;
				params = null;
			} else if ( typeof params === "object" ) {
				params = jQuery.param( params, jQuery.ajaxSettings.traditional );
				type = "POST";
			}
		}
		var self = this;
		jQuery.ajax({
			url: url,
			type: type,
			async: async,
			dataType: "html",
			data: params,
			complete: function( res, status ) {
				if ( status === "success" || status === "notmodified" ) {
					self.html( selector ?
						jQuery("<div />")
							.append(res.responseText.replace(rscript, ""))
							.find(selector) :
						res.responseText );
				}
				if ( callback ) {
					self.each( callback, [res.responseText, status, res] );
				}
			}
		});
		return this;
	}
});

/*
 *portal数据 
 */
if (!$.jpolite)
	$.jpolite = {};
if (!$.jpolite.Data)
	$.jpolite.Data = {
		send : function(action, data, callback, async, bean) {
			return $.ajax({
				type : "post",
				async : async ? async : false,
				url : "controller/" + action,
				data : data,
				success : function(data, textStatus) {
					try {
						data = window.eval("(" + data + ")");
						data = data[0];
						var status = data.status;
						if (data.data) {
							data = window["eval"]("(" + data.data + ")");
						} else {
							data = {};
						}
						data.status = status == "SUCCESS";
					} catch (e) {
					}
					callback(data);
				},
				bean : bean,
				error : function(XMLHttpRequest, textStatus, errorThrown) {

				}
			});
		}
	};
$.jpolite.Data.system = {
	User : {
		login : function(username, password, params, callback) {
			return $.jpolite.Data.send('system/User/login', $.extend(params
					|| {}, {
				username : username,
				password : password
			}), callback, true);
		},
		logout : function(callback) {
			return $.jpolite.Data.send('system/User/logout', {}, callback);
		},
		check : function(callback) {
			return;
		},
		getAgents : function(username, password, callback) {
			return $.jpolite.Data.send('system/User/getAgents', {
				username : username,
				password : password
			}, callback, true);
		},
		changePassword : function(username, password, new_password, callback) {
			return $.jpolite.Data.send('system/User/changePassword', {
				username : username,
				password : password,
				new_password : new_password
			}, callback, true);
		},
		getOnlineCount : function(callback) {
			return $.jpolite.Data.send('system/User/getOnlineCount', {},
					callback);
		},
		getOnlineUserInfo : function(callback) {
			return $.jpolite.Data.send('system/User/getOnlineUserInfo', {},
					callback);
		},
		getOnceFunc : function(callback) {
			return $.jpolite.Data.send('system/User/getOnceFunc', {}, callback);
		},
		getLanguage : function(callback) {
			return $.jpolite.Data.send('system/User/getLanguage', {}, callback);
		},
		setLanguage : function(language, callback) {
			return $.jpolite.Data.send('system/User/setLanguage', {
				language : language
			}, callback);
		}
	},
	Layout : {
		loadTabs : function(callback) {
			return $.jpolite.Data.send('system/Layout/loadTabs', {}, callback);
		},
		saveTabs : function(layout, callback) {
			return $.jpolite.Data.send('system/Layout/saveTabs', {
				layout : layout || ""
			}, callback, true);
		},
		removeTabs : function(callback) {
			return $.jpolite.Data
					.send('system/Layout/removeTabs', {}, callback);
		},
		loadTheme : function(callback) {
			return $.jpolite.Data.send('system/Layout/loadTheme', {}, callback);
		},
		saveTheme : function(theme, callback) {
			return $.jpolite.Data.send('system/Layout/saveTheme', {
				theme : theme || ""
			}, callback, true);
		},
		removeTheme : function(callback) {
			return $.jpolite.Data.send('system/Layout/removeTheme', {},
					callback);
		},
		loadShortcuts : function(callback) {
			return $.jpolite.Data.send('system/Layout/loadShortcuts', {},
					callback);
		},
		saveShortcuts : function(shortcuts, callback) {
			return $.jpolite.Data.send('system/Layout/saveShortcuts', {
				shortcuts : shortcuts || ""
			}, callback, true);
		},
		removeShortcuts : function(callback) {
			return $.jpolite.Data.send('system/Layout/removeShortcuts', {},
					callback);
		}
	},
	FuncTree : function(callback) {
		return $.jpolite.Data.send('system/FuncTree', {}, callback, true);
	},
	FuncTree2 : function(callback) {
		return;
	},
	en_FuncTree : function(callback) {
		return $.jpolite.Data.send('system/en_FuncTree', {}, callback, true);
	},
	// 20100811 MaDuo 为了让widget并发取页面
	Func : function(func, callback, async) {
		return $.jpolite.Data.send('system/Func', func, callback, async);
	}
};

if (!justep) {
	var justep = {};
	justep.yn = {};
}
var $dpjspath = null;
var scripts = document.getElementsByTagName("script");
for (i = 0; i < scripts.length; i++) {
	if (scripts[i].src.toLowerCase().indexOf('system/js/jpolite.data.js') > -1) {
		$dpjspath = scripts[i].src.substring(0, scripts[i].src.length - 15);
		break;
	}
}
var $dpcsspath = $dpjspath ? $dpjspath.replace("/js/", "/css/") : null;
var $dpimgpath = $dpjspath ? $dpjspath.replace("/js/", "/image/") : null;
// alert($dpjspath);

var createJSSheet = function(jsPath) {
	var head = document.getElementsByTagName('HEAD').item(0);
	var script = document.createElement('script');
	script.src = jsPath;
	script.type = 'text/javascript';
	head.appendChild(script);
};
var createStyleSheet = function(cssPath) {
	var head = document.getElementsByTagName('HEAD').item(0);
	var style = document.createElement('link');
	style.href = cssPath;
	style.rel = 'stylesheet';
	style.type = 'text/css';
	head.appendChild(style);
};

var checkPathisHave = function(path) {
	var Hhead = document.getElementsByTagName('HEAD').item(0);
	var Hscript = Hhead.getElementsByTagName("SCRIPT");
	for (var i = 0; i < Hscript.length; i++) {
		if (Hscript[i].src == path)
			return true;
	}
	var Hstyle = Hhead.getElementsByTagName("LINK");
	for (var i = 0; i < Hstyle.length; i++) {
		if (Hstyle[i].href == path)
			return true;
	}
	return false;
};

/*
 * @陈乾 @浅入浅出提示信息 可用于非交互的错误提示 @注意：body的高度不要超过屏幕高度
 */
justep.yn.showMessage = function(message) {
	if (!checkPathisHave($dpcsspath + "showMessage.css"))
		createStyleSheet($dpcsspath + "showMessage.css");
	if (!checkPathisHave($dpjspath + "jquery/jquery.corner.js"))
		createJSSheet($dpjspath + "jquery/jquery.corner.js");
	var oldsg = document.getElementById("msg-showmessage");
	if (oldsg)
		document.body.removeChild(oldsg);
	var main_Message_view = document.createElement("div");
	main_Message_view.setAttribute("id", "msg-showmessage");
	main_Message_view.setAttribute("class", "showmessage");
	main_Message_view.style.position = "absolute";
	main_Message_view.style.left = (document.body.offsetWidth - 300) / 2 + 50;
	main_Message_view.style.top = (document.body.offsetHeight - 100) / 2;
	main_Message_view.style.zIndex = "101";
	var message_show = "<div class=\"mborder\" style=\"margin-left:3px;width:294px;height:25px;font-size:16px;font-color:#FF3333;\">"
			+ message + "</div>";
	document.body.appendChild(main_Message_view);
	main_Message_view.innerHTML = message_show;
	$('.mborder').corner("5px");
	// alert(main_Message_view.innerHTML);
	function startMove(obj) {
		obj.iAlpha = 60;
		obj.times && clearInterval(obj.time);
		obj.times = setInterval(function() {
			doMove(obj);
		}, 100);
	}
	function doMove(obj) {
		var iSpeed = 5;
		if (obj.iAlpha >= 90) {
			clearInterval(obj.times);
			obj.iAlpha = 100;
			obj.times = setInterval(function() {
				endMove(obj);
			}, 100);
		} else {
			obj.iAlpha += iSpeed;
		}
		obj.style.filter = "alpha(opacity=" + obj.iAlpha + ")";
		obj.style.opacity = obj.iAlpha / 100;
	}
	function endMove(obj) {
		var iSpeed = 5;
		if (obj.iAlpha <= 10) {
			clearInterval(obj.times);
			obj.iAlpha = 10;
			obj.time = null;
			try {
				document.body.removeChild(obj);
			} catch (e) {
			}
		} else {
			obj.iAlpha -= iSpeed;
		}
		obj.style.filter = "alpha(opacity=" + obj.iAlpha + ")";
		obj.style.opacity = obj.iAlpha / 100;
	}
	function showMessage() {
		var msgView = document.getElementById("msg-showmessage");
		startMove(msgView);
	}
	showMessage();
};

/*
 * 操作状态 -进度条
 */
justep.yn.showSate = function(state) {
	if (state && state == true) {
		$('#content_loading').show();
	} else {
		$('#content_loading').hide();
	}
};

/*
 * c@陈乾 d@操作提示信息
 */
function sAlert(str, time) {
	var sAlertDiv = document.getElementById("msgDiv");
	if (sAlertDiv && sAlertDiv.tagName == "DIV") {
		document.body.removeChild(sAlertDiv);
	}
	var msgw, msgh, bordercolor;
	msgw = 100;
	msgh = 20;
	titleheight = 20;
	bordercolor = "#ffffff";
	titlecolor = "#FFFF66";
	var sWidth, sHeight;
	sWidth = document.body.clientWidth;
	sHeight = document.body.clientHeight;
	if (window.innerHeight && window.scrollMaxY) {
		sHeight = window.innerHeight + window.scrollMaxY;
	} else if (document.body.scrollHeight > document.body.offsetHeight) {
		sHeight = document.body.scrollHeight;
	} else {
		sHeight = document.body.offsetHeight;
	}
	var msgObj = document.createElement("div");
	msgObj.setAttribute("id", "msgDiv");
	msgObj.setAttribute("align", "center");
	msgObj.style.background = titlecolor;
	msgObj.style.border = "1px solid " + bordercolor;
	msgObj.style.position = "absolute";
	msgObj.style.left = "92%";
	msgObj.style.top = "100px";
	msgObj.style.font = "12px/1.6em Verdana, Geneva, Arial, Helvetica, sans-serif";
	msgObj.style.width = msgw + "px";
	msgObj.style.height = msgh + "px";
	msgObj.style.textAlign = "center";
	msgObj.style.lineHeight = "2px";
	msgObj.style.zIndex = "101";
	var title = document.createElement("h4");
	title.setAttribute("id", "msgTitle");
	title.setAttribute("align", "center");
	title.style.margin = "0";
	title.style.padding = "3px";
	title.style.background = titlecolor;
	title.style.filter = "progid:DXImageTransform.Microsoft.Alpha(startX=20, startY=20, finishX=100, finishY=100,style=1,opacity=75,finishOpacity=100);";
	title.style.opacity = "0.75";
	title.style.height = "20px";
	title.style.font = "12px Verdana, Geneva, Arial, Helvetica, sans-serif";
	title.style.color = "#000000";
	title.innerHTML = str;
	title.onclick = function() {
		document.getElementById("msgDiv").removeChild(title);
		document.body.removeChild(msgObj);
	};
	document.body.appendChild(msgObj);
	document.getElementById("msgDiv").appendChild(title);
	var txt = document.createElement("p");
	txt.style.margin = "1em 0";
	txt.setAttribute("id", "msgTxt");
	txt.innerHTML = str;
	time = time ? time : 500;
	setTimeout('displayActionMessage()', time);
}
var displayActionMessage = function() {
	if (document.getElementById("msgTitle"))
		document.getElementById("msgDiv").removeChild(
				document.getElementById("msgTitle"));
	if (document.getElementById("msgDiv"))
		document.body.removeChild(document.getElementById("msgDiv"));
};

justep.yn.showOnlineList = function(obj) {
	$("#onlineBox").remove();
	var xx = $(obj).offset().left;
	var yy = $(obj).offset().top;
	var onlinebox = $("<div id='onlineBox'></div>");
	onlinebox.css("position", "absolute");
	onlinebox.css("z-index", "9999");
	onlinebox.css("overflow", "hidden");
	onlinebox.width(20);
	onlinebox.height(50);
	onlinebox.css("left", (xx - onlinebox.width() + 28) + "px");
	onlinebox.css("top", (yy - onlinebox.height() - 40) + "px");
	$(document.body).append(onlinebox);
	var titleline = $("<div><span style='margin-left:5px;'>在线人员信息</span></div>");
	titleline.css("width", "100%");
	titleline.css("height", "25px");
	onlinebox.append(titleline);
	var closebtn = $("<a href='javascript:void(0);'>x</a>");
	closebtn.css("float", "right");
	closebtn.css("height", "25px");
	closebtn.css("line-height", "25px");
	closebtn.css("margin-right", "10px");
	closebtn.attr("title", "关闭");
	titleline.append(closebtn);
	closebtn.click(function() {
		var curmer1 = setInterval(function() {
			var ww = onlinebox.width();
			var hh = onlinebox.height();
			var ll = parseInt(onlinebox.css("left"));
			var tt = parseInt(onlinebox.css("top"));
			if (ww > 20 && hh > 50) {
				onlinebox.width(ww - 20);
				onlinebox.height(hh - 50);
				onlinebox.css("left", (ll + 20) + "px");
				onlinebox.css("top", (tt + 50) + "px");
			} else {
				clearInterval(curmer1);
				$("#onlineboxframe").remove();
				$("#onlineBox").remove();
			}
		}, 20);
	});
	var boxcontent = $("<div></div>");
	boxcontent.css("width", "98%");
	boxcontent.css("height", "433px");
	boxcontent.css("background", "#fff");
	boxcontent.css("margin-left", "2px");
	onlinebox.append(boxcontent);
	var url = '/JBIZ/portal2/online/mainActivity.html';
	boxcontent.html("<iframe frameborder='0' id='onlineboxframe'"
			+ "style='width:100%;height:100%;border:0px;' src='" + url
			+ "'></iframe>");
	var curmer = setInterval(function() {
		var ww = onlinebox.width();
		var hh = onlinebox.height();
		var ll = parseInt(onlinebox.css("left"));
		var tt = parseInt(onlinebox.css("top"));
		if (ww < 200 && hh < 500) {
			onlinebox.width(ww + 20);
			onlinebox.height(hh + 50);
			onlinebox.css("left", (ll - 20) + "px");
			onlinebox.css("top", (tt - 50) + "px");
		} else {
			clearInterval(curmer);
		}
	}, 20);
};


if (!this.JSON) {
    this.JSON = {};
}

(function () {

    function f(n) {
        // Format integers to have at least two digits.
        return n < 10 ? '0' + n : n;
    }

    if (typeof Date.prototype.toJSON !== 'function') {

        Date.prototype.toJSON = function (key) {

            return isFinite(this.valueOf()) ?
                   this.getUTCFullYear()   + '-' +
                 f(this.getUTCMonth() + 1) + '-' +
                 f(this.getUTCDate())      + 'T' +
                 f(this.getUTCHours())     + ':' +
                 f(this.getUTCMinutes())   + ':' +
                 f(this.getUTCSeconds())   + 'Z' : null;
        };

        String.prototype.toJSON =
        Number.prototype.toJSON =
        Boolean.prototype.toJSON = function (key) {
            return this.valueOf();
        };
    }

    var cx = /[\u0000\u00ad\u0600-\u0604\u070f\u17b4\u17b5\u200c-\u200f\u2028-\u202f\u2060-\u206f\ufeff\ufff0-\uffff]/g,
        escapable = /[\\\"\x00-\x1f\x7f-\x9f\u00ad\u0600-\u0604\u070f\u17b4\u17b5\u200c-\u200f\u2028-\u202f\u2060-\u206f\ufeff\ufff0-\uffff]/g,
        gap,
        indent,
        meta = {    // table of character substitutions
            '\b': '\\b',
            '\t': '\\t',
            '\n': '\\n',
            '\f': '\\f',
            '\r': '\\r',
            '"' : '\\"',
            '\\': '\\\\'
        },
        rep;


    function quote(string) {

// If the string contains no control characters, no quote characters, and no
// backslash characters, then we can safely slap some quotes around it.
// Otherwise we must also replace the offending characters with safe escape
// sequences.

        escapable.lastIndex = 0;
        return escapable.test(string) ?
            '"' + string.replace(escapable, function (a) {
                var c = meta[a];
                return typeof c === 'string' ? c :
                    '\\u' + ('0000' + a.charCodeAt(0).toString(16)).slice(-4);
            }) + '"' :
            '"' + string + '"';
    }


    function str(key, holder) {

// Produce a string from holder[key].

        var i,          // The loop counter.
            k,          // The member key.
            v,          // The member value.
            length,
            mind = gap,
            partial,
            value = holder[key];

// If the value has a toJSON method, call it to obtain a replacement value.

        if (value && typeof value === 'object' &&
                typeof value.toJSON === 'function') {
            value = value.toJSON(key);
        }

// If we were called with a replacer function, then call the replacer to
// obtain a replacement value.

        if (typeof rep === 'function') {
            value = rep.call(holder, key, value);
        }

// What happens next depends on the value's type.

        switch (typeof value) {
        case 'string':
            return quote(value);

        case 'number':

// JSON numbers must be finite. Encode non-finite numbers as null.

            return isFinite(value) ? String(value) : 'null';

        case 'boolean':
        case 'null':

// If the value is a boolean or null, convert it to a string. Note:
// typeof null does not produce 'null'. The case is included here in
// the remote chance that this gets fixed someday.

            return String(value);

// If the type is 'object', we might be dealing with an object or an array or
// null.

        case 'object':

// Due to a specification blunder in ECMAScript, typeof null is 'object',
// so watch out for that case.

            if (!value) {
                return 'null';
            }

// Make an array to hold the partial results of stringifying this object value.

            gap += indent;
            partial = [];

// Is the value an array?

            if (Object.prototype.toString.apply(value) === '[object Array]') {

// The value is an array. Stringify every element. Use null as a placeholder
// for non-JSON values.

                length = value.length;
                for (i = 0; i < length; i += 1) {
                    partial[i] = str(i, value) || 'null';
                }

// Join all of the elements together, separated with commas, and wrap them in
// brackets.

                v = partial.length === 0 ? '[]' :
                    gap ? '[\n' + gap +
                            partial.join(',\n' + gap) + '\n' +
                                mind + ']' :
                          '[' + partial.join(',') + ']';
                gap = mind;
                return v;
            }

// If the replacer is an array, use it to select the members to be stringified.

            if (rep && typeof rep === 'object') {
                length = rep.length;
                for (i = 0; i < length; i += 1) {
                    k = rep[i];
                    if (typeof k === 'string') {
                        v = str(k, value);
                        if (v) {
                            partial.push(quote(k) + (gap ? ': ' : ':') + v);
                        }
                    }
                }
            } else {

// Otherwise, iterate through all of the keys in the object.

                for (k in value) {
                    if (Object.hasOwnProperty.call(value, k)) {
                        v = str(k, value);
                        if (v) {
                            partial.push(quote(k) + (gap ? ': ' : ':') + v);
                        }
                    }
                }
            }

// Join all of the member texts together, separated with commas,
// and wrap them in braces.

            v = partial.length === 0 ? '{}' :
                gap ? '{\n' + gap + partial.join(',\n' + gap) + '\n' +
                        mind + '}' : '{' + partial.join(',') + '}';
            gap = mind;
            return v;
        }
    }

// If the JSON object does not yet have a stringify method, give it one.

    if (typeof JSON.stringify !== 'function') {
        JSON.stringify = function (value, replacer, space) {

// The stringify method takes a value and an optional replacer, and an optional
// space parameter, and returns a JSON text. The replacer can be a function
// that can replace values, or an array of strings that will select the keys.
// A default replacer method can be provided. Use of the space parameter can
// produce text that is more easily readable.

            var i;
            gap = '';
            indent = '';

// If the space parameter is a number, make an indent string containing that
// many spaces.

            if (typeof space === 'number') {
                for (i = 0; i < space; i += 1) {
                    indent += ' ';
                }

// If the space parameter is a string, it will be used as the indent string.

            } else if (typeof space === 'string') {
                indent = space;
            }

// If there is a replacer, it must be a function or an array.
// Otherwise, throw an error.

            rep = replacer;
            if (replacer && typeof replacer !== 'function' &&
                    (typeof replacer !== 'object' ||
                     typeof replacer.length !== 'number')) {
                throw new Error('JSON.stringify');
            }

// Make a fake root object containing our value under the key of ''.
// Return the result of stringifying the value.

            return str('', {'': value});
        };
    }


// If the JSON object does not yet have a parse method, give it one.

    if (typeof JSON.parse !== 'function') {
        JSON.parse = function (text, reviver) {

// The parse method takes a text and an optional reviver function, and returns
// a JavaScript value if the text is a valid JSON text.

            var j;

            function walk(holder, key) {

// The walk method is used to recursively walk the resulting structure so
// that modifications can be made.

                var k, v, value = holder[key];
                if (value && typeof value === 'object') {
                    for (k in value) {
                        if (Object.hasOwnProperty.call(value, k)) {
                            v = walk(value, k);
                            if (v !== undefined) {
                                value[k] = v;
                            } else {
                                delete value[k];
                            }
                        }
                    }
                }
                return reviver.call(holder, key, value);
            }


// Parsing happens in four stages. In the first stage, we replace certain
// Unicode characters with escape sequences. JavaScript handles many characters
// incorrectly, either silently deleting them, or treating them as line endings.

            text = String(text);
            cx.lastIndex = 0;
            if (cx.test(text)) {
                text = text.replace(cx, function (a) {
                    return '\\u' +
                        ('0000' + a.charCodeAt(0).toString(16)).slice(-4);
                });
            }

// In the second stage, we run the text against regular expressions that look
// for non-JSON patterns. We are especially concerned with '()' and 'new'
// because they can cause invocation, and '=' because it can cause mutation.
// But just to be safe, we want to reject all unexpected forms.

// We split the second stage into 4 regexp operations in order to work around
// crippling inefficiencies in IE's and Safari's regexp engines. First we
// replace the JSON backslash pairs with '@' (a non-JSON character). Second, we
// replace all simple value tokens with ']' characters. Third, we delete all
// open brackets that follow a colon or comma or that begin the text. Finally,
// we look to see that the remaining characters are only whitespace or ']' or
// ',' or ':' or '{' or '}'. If that is so, then the text is safe for eval.

            if (/^[\],:{}\s]*$/.
test(text.replace(/\\(?:["\\\/bfnrt]|u[0-9a-fA-F]{4})/g, '@').
replace(/"[^"\\\n\r]*"|true|false|null|-?\d+(?:\.\d*)?(?:[eE][+\-]?\d+)?/g, ']').
replace(/(?:^|:|,)(?:\s*\[)+/g, ''))) {

// In the third stage we use the eval function to compile the text into a
// JavaScript structure. The '{' operator is subject to a syntactic ambiguity
// in JavaScript: it can begin a block or an object literal. We wrap the text
// in parens to eliminate the ambiguity.

                j = eval('(' + text + ')');

// In the optional fourth stage, we recursively walk the new structure, passing
// each name/value pair to a reviver function for possible transformation.

                return typeof reviver === 'function' ?
                    walk({'': j}, '') : j;
            }

// If the text is not JSON parseable, then a SyntaxError is thrown.

            throw new SyntaxError('JSON.parse');
        };
    }
}());

/*
 * @Name: jQuery Tab插进
 * @author: 陈乾
 * @Params: config{stic:[{ids:'',title:'',body:''}]}
 */
jQuery.fn.extend({
	jqtabs : function(config) {
		if (config == "get") {
			return this.get(0).tabs;
		}
		var jtabs = {
			tabsitems : [],
			tabsMap : new Map(),
			tabsHeadMap : new Map(),
			tabsBodyMap : new Map(),
			theader : '<div class="head"></div>',
			theader_main : '<div class="main"></div>',
			tt : '<div class="tt"></div>',
			coitem : '<a class="item" href="javascript:void(0)">x</a>',
			tbody : '<div class="body"></div>',
			tabsheader : null,
			tabsbody : null,
			activeTab : null,
			addTab : function(id, title, body, isclose) {// 添加Tab
				var self = this;
				var theader = $(this.theader_main).clone();
				theader.attr("id", id);
				theader.attr("title", title);
				theader.bind("click", function() {
					self.setActiveTab($(this).attr("id"));
				});
				var tstt = $(this.tt).clone();
				tstt.html(title);
				theader.append(tstt);
				if (isclose) {
					var closeItem = $(this.coitem).clone();
					closeItem.bind("click", function() {
						self.removeTab(id);
					});
					theader.append(closeItem);
				}
				this.tabsitems.push(id);
				var ntbody = $('<div id="' + id + '_content"></div>');
				ntbody.addClass("tabsBody");
				ntbody.css("width", "100%");
				ntbody.css("height", "100%");
				ntbody.css("overflow", "auto");
				ntbody.html(body);
				this.tabsMap.put(id, {
					ids : id,
					title : title,
					body : ntbody,
					isclose : isclose
				});
				this.tabsHeadMap.put(id, theader);
				this.tabsBodyMap.put(id, ntbody);
				this.tabsheader.append(theader);
				this.tabsbody.append(ntbody);
				this.setActiveTab(id);
			},
			removeTab : function(tId) {// 刪除Tab
				if (!this.tabsMap.containsKey(tId)) {
					alert("指定的tabID不存在!");
					return;
				}
				var tsIndex = this.tabsitems.removeValue(tId);
				this.tabsMap.remove(tId);
				var thead = this.tabsHeadMap.get(tId);
				var tbody = this.tabsBodyMap.get(tId);
				thead.remove();
				tbody.find("iframe").remove();
				tbody.remove();
				this.tabsHeadMap.remove(tId);
				this.tabsBodyMap.remove(tId);
				if (tsIndex > 0 && this.getActiveTab() == tId) {
					var naId = this.tabsitems[tsIndex - 1];
					this.setActiveTab(naId);
				}
			},
			getTabById : function(tId) {// 根据tId获取tab对象
				if (this.tabsMap.containsKey(tId)) {
					return {
						title : this.tabsMap.get(tId).title,
						body : this.tabsBodyMap.get(tId),
						content : this.tabsBodyMap.get(tId),
						tabLayoutDiv : $("#" + tId + "_content")
					};
				}
			},
			getActiveTab : function() {// 获取当前显示的Tab ID
				return this.activeTab;
			},
			setActiveTab : function(tId) {// 选中指定的Tab
				$(".tabs>.head>.active").removeClass("active");
				$("#" + tId).addClass("active");
				$(".tabs>.body>.tabsBody").hide();
				this.tabsBodyMap.get(tId).show();
				this.activeTab = tId;
				if (tId == "t001") {
					$(".portal_body_frame").each(function() {
						var ourl = $(this).attr("src");
						$(this).attr("src", ourl);
					});
				}
			},
			ishaveTabId : function(tId) {// 判断tId是否存在
				return this.tabsMap.containsKey(tId);
			},
			closeAll : function() {
				var tabIds = this.tabsMap.keySet();
				for (var i = 0; i < tabIds.length; i++) {
					var tid = tabIds[i];
					var tab = this.tabsMap.get(tid);
					if (tab.isclose) {
						this.removeTab(tid);
					}
				}
			}
		};
		var header = $(jtabs.theader).clone();
		var tsbody = $(jtabs.tbody).clone();
		jtabs.tabsheader = header;
		jtabs.tabsbody = tsbody;
		this.append(header).append(tsbody);
		initPortallet(jtabs, config);
//		this.get(0).tabs = jtabs;
		return jtabs;
	}
});

function initPortallet(jtabs, config, ischange) {
}

function portalbodyResize() {
	$('#client').portal('resize');
}

function portalonStateChange() {
	var panles = $('#client').portal('getPanels');
	var portallets = [];
	for (var i = 0; i < panles.length; i++) {
		portallets.push(panles[i].panel("options"));
	}
	var panlesStr = JSON.stringify(portallets);
	var param = new justep.yn.RequestParam();
	param.set("layout", portalConfig.defaultsLid);
	param.set("panles", panlesStr);
	justep.yn.XMLHttpRequest("savePortal", param, "POST", true, null, true);
}

/*
 * 以下为了兼容老版本
 */
if (!$.X)
	$.X = {};
// 解析为 $对象
$.X.parseFunc = function(func) {
	var p = {};
	if (typeof func == "string") {
		try {
			$.extend(p, window["eval"]("(" + func + ")"));
		} catch (e) {
			alert("$.X.parseFunc\n" + e.description + "\n" + func);
		}
	} else {
		$.extend(p, func);
	}
	return p;
};
// 为功能构建连接地址
$.X.createFunc = function(func) {
	var funcURL = $.jpolite.clientInfo.createURL(func);
	return funcURL;
};
// 把符号全部换成"_"
$.X.getFuncID = function(func) {
	var p = $.X.parseFunc(func);
	return hex_md5(p.name + p.url);
};
// 页面打开
$.X.runFunc = function(func) {
	var p = $.X.parseFunc(func);
	if (p.target == "_blank") {
		var funcURL = $.X.createFunc(p);
		window.open(funcURL);
	} else if (p.target == "_self" || p.target == "_top"
			|| p.target == "_parent") {
		var funcURL = $.X.createFunc(p);
		window.location.href = funcURL;
	} else if (p.target == "_dialog") {
		window.Light.portal.showDialog(p.url, p.name, p.width || 600,
				p.height || 400);
	} else {
		var tid = $.X.getFuncID(p);
		if ($.jpolite.getTab(tid)) {
			$.jpolite.gotoTab(tid);
		} else {
			var p = $.X.parseFunc(func);
			var tab = $.jpolite.addTab(tid, p);
		}
	}
	return true;
};

Date.prototype.format = function(fmt) {
	var o = {
		"M+" : this.getMonth() + 1,
		"d+" : this.getDate(),
		"h+" : this.getHours() % 24 == 0 ? 24 : this.getHours() % 24,
		"H+" : this.getHours(),
		"m+" : this.getMinutes(),
		"s+" : this.getSeconds(),
		"q+" : Math.floor((this.getMonth() + 3) / 3),
		"S" : this.getMilliseconds()
	};
	var week = {
		"0" : "\u65e5",
		"1" : "\u4e00",
		"2" : "\u4e8c",
		"3" : "\u4e09",
		"4" : "\u56db",
		"5" : "\u4e94",
		"6" : "\u516d"
	};
	if (/(y+)/.test(fmt)) {
		fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "")
				.substr(4 - RegExp.$1.length));
	}
	if (/(E+)/.test(fmt)) {
		fmt = fmt
				.replace(
						RegExp.$1,
						((RegExp.$1.length > 1) ? (RegExp.$1.length > 2 ? "\u661f\u671f"
								: "\u5468")
								: "")
								+ week[this.getDay() + ""]);
	}
	for ( var k in o) {
		if (new RegExp("(" + k + ")").test(fmt)) {
			fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k])
					: (("00" + o[k]).substr(("" + o[k]).length)));
		}
	}
	return fmt;
};

Date.prototype.toCnDay = function() {
	var n = this.getDay();
	var days = [ '星期日', '星期一', '星期二', '星期三', '星期四', '星期五', '星期六' ];
	return days[n];
};

function trim(text) {
	if (text == undefined) {
		return "";
	} else {
		return text.replace(/(^\s*)|(\s*$)/g, "");
	}
}
String.prototype.trim = function() {
	return this.replace(/(^\s+)|(\s+$)/g, "");
};
String.prototype.ltrim = function() {
	return this.replace(/^\s+/, "");
};
String.prototype.rtrim = function() {
	return this.replace(/\s+$/, "");
};
var reMoveStr = function(str1, str2) {
	if (str1.indexOf(str2) > -1)
		str1 = str1.replace(str2, "");
	return str1;
};
var replaceFirst = function(str, p, m) {
	if (str.indexOf(p) == 0)
		str = str.replace(p, m);
	return str;
};
String.prototype.replaceFirst = function(p, m) {
	if (this.indexOf(p) == 0) {
		return this.replace(p, m);
	}
	return this;
};
String.prototype.replaceAll = function(p, m) {
	return this.replace(new RegExp(p, "gm"), m);
};

var Map = function() {
	var struct = function(key, value) {
		this.key = key;
		this.value = value;
	};
	var put = function(key, value) {
		for (var i = 0; i < this.arr.length; i++) {
			if (this.arr[i].key === key) {
				this.arr[i].value = value;
				return;
			}
		}
		this.arr[this.arr.length] = new struct(key, value);
	};
	var get = function(key) {
		for (var i = 0; i < this.arr.length; i++) {
			if (this.arr[i].key === key) {
				return this.arr[i].value;
			}
		}
		return null;
	};
	var remove = function(key) {
		var v;
		for (var i = 0; i < this.arr.length; i++) {
			v = this.arr.pop();
			if (v.key === key) {
				continue;
			}
			this.arr.unshift(v);
		}
	};
	var size = function() {
		return this.arr.length;
	};
	var isEmpty = function() {
		return this.arr.length <= 0;
	};
	var containsKey = function(key) {
		var result = false;
		for (var i = 0; i < this.arr.length; i++) {
			if (this.arr[i].key === key) {
				result = true;
			}
		}
		return result;
	};
	var containsValue = function(value) {
		var result = false;
		for (var i = 0; i < this.arr.length; i++) {
			if (this.arr[i].value === value) {
				result = true;
			}
		}
		return result;
	};
	var keySet = function() {
		var result = new Array();
		for (var i = 0; i < this.arr.length; i++) {
			result.push(this.arr[i].key);
		}
		return result;
	};
	this.arr = new Array();
	this.get = get;
	this.put = put;
	this.remove = remove;
	this.size = size;
	this.isEmpty = isEmpty;
	this.containsKey = containsKey;
	this.containsValue = containsValue;
	this.keySet = keySet;
	return this;
};
Map.prototype.toString = function() {
	var rs = "{";
	for (var i = 0; i < this.arr.length; i++) {
		if (i > 0)
			rs += ",";
		rs += "\"" + this.arr[i].key + "\":\"" + this.arr[i].value + "\"";
	}
	rs += "}";
	return rs;
};

Array.prototype.remove = function(dx) {
	if (isNaN(dx) || dx > this.length) {
		return false;
	}
	for (var i = 0, n = 0; i < this.length; i++) {
		if (this[i] != this[dx]) {
			this[n++] = this[i];
		}
	}
	this.length -= 1;
};

Array.prototype.removeValue = function(val) {
	if (!val) {
		return false;
	}
	var index = -1;
	for (var i = 0; i < this.length; i++) {
		if (this[i] == val) {
			this.splice(i, 1);
			index = i;
		}
	}
	return index;
};

justep.yn.portal = {};
justep.yn.portal.closeWindow = function(tabId) {
	var currentTabId = tabId ? tabId : $.jpolite.getTabID();
	$.jpolite.removeTab(currentTabId);
};
justep.yn.portal.currentTabId = function() {
	return $.jpolite.getTabID();
};
justep.yn.portal.openWindow = function(name, url, param) {
	$.X.runFunc({
		name : name,
		url : url,
		process : "",
		activity : "",
		params : param
	});
};
justep.yn.portal.callBack = function(tabID, FnName, param) {
	try {
		var t = $.jpolite.getTab(tabID);
		var containers = t.tabLayoutDiv.get(0);
		var pTabIframe = containers.getElementsByTagName("iframe")[0].contentWindow;
		var callFn = pTabIframe[FnName];
		callFn(param);
	} catch (e) {
	}
};
justep.yn.portal.dailog = {
	openDailog : function(name, url, width, height, callback, itemSetInit,
			titleItem, urlParam) {
		if ($("<div></div>").dialog) {
			$("#msgiframe").remove();
			$("#msgdialog").remove();
			var dihtml = '<iframe id="msgiframe" frameborder="0" style="width: 100%;height: 100%;"></iframe>';
			var globaldialog = $('<div id="msgdialog" style="width:0px;height:0px;overflow:hidden;">'
					+ dihtml + '</div>');
			$(document.body).append(globaldialog);
			var dlwidth = parseInt(width);
			var dlheight = parseInt(height);
			if (dlwidth > $(window).width()) {
				dlwidth = $(window).width() - 40;
			}
			if (dlheight > $(window).height()) {
				dlheight = $(window).height() - 40;
			}
			var dlparam = {
				title : name,
				width : dlwidth,
				height : dlheight,
				closed : false,
				cache : false,
				modal : true,
				collapsible : true,
				maximizable : false
			};
			if (itemSetInit == false) {
				dlparam.buttons = undefined;
			} else {
				dlparam.buttons = [
						{
							text : '确定',
							handler : function() {
								var dlw = document.getElementById("msgiframe").contentWindow;
								if (dlw.dailogEngin) {
									var re = dlw.dailogEngin();
									if ((typeof re == "boolean") && re == false) {

									} else {
										if (callback) {
											callback(re);
										}
										$('#msgdialog').dialog('close');
									}
								} else {
									$('#msgdialog').dialog('close');
								}
							}
						}, {
							text : '取消',
							handler : function() {
								$('#msgdialog').dialog('close');
							}
						} ];
			}
			globaldialog.dialog(dlparam);
			$("#msgiframe").height($("#msgiframe").parent().height());
			$("#msgiframe").attr("src", url);
			try {
				var func_iframe = document.getElementById("msgiframe");
				if (func_iframe.attachEvent) {
					func_iframe.attachEvent("onload", function() {
						try {
							var dialogWin = func_iframe.contentWindow;
							if (dialogWin.getUrlParam) {
								dialogWin.getUrlParam(urlParam);
							}
						} catch (e) {
						}
					});
				} else {
					func_iframe.onload = function() {
						try {
							var dialogWin = func_iframe.contentWindow;
							if (dialogWin.getUrlParam) {
								dialogWin.getUrlParam(urlParam);
							}
						} catch (e) {
						}
					};
				}
			} catch (e) {
			}
			return;
		}
		$(document.body).append("<div class='bodymovewrap'>&nbsp;</div>");
		var msg = $("<div id='msgdialog' class='panel window'></div>");
		msg.css("position", "absolute");
		msg.css("z-index", "9999");
		msg.css("left", ($(document.body).width() - width) / 2 + "px");
		msg.css("top", ($(document.body).height() - height) / 2 + "px");
		msg.width(width);
		msg.height(height);
		$(document.body).append(msg);
		var msghead = $("<div id='dialoghead' class='panel-header panel-header-noborder window-header'></div>");
		msg.append(msghead);
		var labelhtml = "<div class='panel-title'>";
		labelhtml += "<img src='css/img/label_mg.png'/>&nbsp;&nbsp;";
		labelhtml += name + "</div>";
		msghead.append(labelhtml);
		var ptool = $("<div class='panel-tool'></div>");
		var closeitem = $("<a href='javascript:void(0);' id='msgclosebtn' class='panel-tool-close' title='关闭'></a>");
		ptool.append(closeitem);
		msghead.append(ptool);
		var msgbody = $("<div id='dialogbody' class='panel-body panel-body-noborder window-body'></div>");
		msgbody.height(height - 25);
		msgbody.css("overflow", "hidden");
		msg.append(msgbody);
		var bodyform = $("<div id='dialogbodyfrom' class='panel' style='border:0px none;'></div>");
		bodyform.width(width - 2);
		bodyform.height(msgbody.height());
		msgbody.append(bodyform);
		$("#msgclosebtn").click(function() {
			justep.yn.portal.dailog.dailogCancel();
		});
		$(".msgtitlelabel").bind("mousedown", function(e) {
			$.isMoveStart = true;
			$(document).bind('selectstart', $.unselecttext);
			var o = $("#msgdialog");
			var lf = o.offset().left;
			var tp = o.offset().top;
			var mus = justep.yn.portal.dailog.getMouseCoords(e);
			var dfleft = mus.x - lf;
			var dftop = mus.y - tp;
			$(document).bind("mousemove", function(event) {
				if ($.isMoveStart) {
					var a = event || window.Event;
					var mus = justep.yn.portal.dailog.getMouseCoords(a);
					var tx = mus.x - dfleft, ty = mus.y - dftop;
					var newW = parseInt(o.width());
					var newH = parseInt(o.height());
					var sWidth = $(document.body).width();
					if (tx > (sWidth - newW))
						tx = (sWidth - newW);
					if (tx < 0)
						tx = 0;
					var Height = $(document.body).height();
					if (ty > (Height - newH))
						ty = (Height - newH);
					if (ty < 0)
						ty = 0;
					o.css("left", tx + "px");
					o.css("top", ty + "px");
				}
			});
			$(document).bind("mouseup", function() {
				$.isMoveStart = false;
				$(document).unbind('selectstart', $.unselecttext);
				$(document).unbind("mousemove");
			});
		});
		$(".msgtitlelabel").bind("mouseup", function() {
			$.isMoveStart = false;
		});
		var dlconten = $("<div class='panel-body panel-body-noheader panel-body-noborder dialog-content' style='width:100%;height:100%;'></div>");
		bodyform.append(dlconten);
		var msgframe = $("<iframe id='msgiframe' frameborder='0' style='width:100%;height:100%;ovarflow:auto;'/>");
		dlconten.append(msgframe);
		$("#msgiframe").attr("src", url);
		msg.hide();
		msg.show(500);
	},
	getMouseCoords : function(e) {
		var xx = e.originalEvent.x || e.originalEvent.layerX || 0;
		var yy = e.originalEvent.y || e.originalEvent.layerY || 0;
		var mus = {
			x : xx,
			y : yy
		};
		return mus;
	},
	callback : null,
	sendData : null,
	returnData : null,
	removewrap : function() {
		$(document.body).find(".bodymovewrap").remove();
	},
	dailogEngin : function(data, b_g) {
	},
	dailogReload : function() {
	},
	dailogCancel : function() {
		$("#msgdialog").hide(500, function() {
			$("#dialogbodyfrom").remove();
			$("#dialogbody").remove();
			$("#dialoghead").remove();
			$("#msgdialog").remove();
			$(".bodymovewrap").remove();
		});
	}
};

var J_u_encode = function(str) {
	return encodeURIComponent(encodeURIComponent(str));
};

justep.yn.RequestParam = function() {
	var params = new Map();
	this.params = params;
	var set = function(key, value) {
		this.params.put(key, value);
	};
	this.set = set;
	var get = function(key) {
		return this.params.gut(key);
	};
	this.get = get;
	return this;
};

justep.yn.XMLHttpRequest = function(actionName, param, post, ayn, callBack,
		unShowState) {
	
};
if (!justep.yn.portal) {
	justep.yn.portal = {};
}
justep.yn.portal.closeWindow = function(tabId) {
	var currentTabId = tabId ? tabId : $.jpolite.getTabID();
	$.jpolite.removeTab(currentTabId);
};
justep.yn.portal.currentTabId = function() {
	try {
		return $.jpolite.getTabID();
	} catch (e) {
		alert("justep.yn.portal.currentTabId: " + e.message);
	}
};
justep.yn.portal.openWindow = function(name, url, param) {
	try {
		$.X.runFunc({
			name : name,
			url : url,
			process : "",
			activity : "",
			params : param
		});
	} catch (e) {
		alert("justep.yn.portal.openWindow: " + e.message);
	}
};
justep.yn.portal.callBack = function(tabID, FnName, param) {
	try {
		var t = $.jpolite.getTab(tabID);
		var containers = t.tabLayoutDiv.get(0);
		var pTabIframe = containers.getElementsByTagName("iframe")[0].contentWindow;
		var callFn = pTabIframe[FnName];
		callFn(param);
	} catch (e) {
	}
};

function checkLogin() {
	$.jpolite.Data.system.User.check(function(data) {
		if (data && !data.status) {
			alert("连接中断或登陆超时，请重新登陆!");
			window.location.href = "/JBIZ/portal2/login.html";
		}
	});
}


var curMenu = null, zTree_Menu = null;
var curExpandNode = null;
var funtionTree_Setting = {
	view : {
		showLine : false,
		selectedMulti : false,
		dblClickExpand : false
	},
	data : {
		key : {
			name : "label",
			title : "label"
		},
		simpleData : {
			enable : true
		}
	},
	callback : {
		onExpand : function onExpand(event, treeId, treeNode) {
			curExpandNode = treeNode;
		},
		beforeExpand : function beforeExpand(treeId, treeNode) {
			var pNode = curExpandNode ? curExpandNode.getParentNode() : null;
			var treeNodeP = treeNode.parentTId ? treeNode.getParentNode()
					: null;
			for (var i = 0, l = !treeNodeP ? 0 : treeNodeP.children.length; i < l; i++) {
				if (treeNode !== treeNodeP.children[i]) {
					zTree_Menu.expandNode(treeNodeP.children[i], false);
				}
			}
			while (pNode) {
				if (pNode === treeNode) {
					break;
				}
				pNode = pNode.getParentNode();
			}
			if (!pNode) {
				singlePath(treeNode);
			}

		},
		onClick : function onClick(e, treeId, node) {
			if (node.isParent) {
				zTree_Menu.expandNode(node, null, null, null, true);
				return;
			}
			var html = "<iframe frameborder='0' class='func_body_frame' id='"
					+ node.id
					+ "' name='"
					+ node.id
					+ "' src='' style='width:100%;height:100%;overflow:auto;'/>";
			var url = node.path + "?tabId=" + node.id;
			if (node.process && node.process != "") {
				url += "&process=" + node.process;
			}
			if (node.activity && node.activity != "") {
				url += "&activity=" + node.activity;
			}
			if ($.indexpageTabs.ishaveTabId(node.id)) {
				$.indexpageTabs.setActiveTab(node.id);
			} else {
				$.indexpageTabs.addTab(node.id, node.label, html, true);
				$("iframe[id='" + node.id + "']").src(url, function() {
					$('#content_loading').hide();
				});
				$('#content_loading').show();
			}
		}
	}
};

function singlePath(newNode) {
	if (newNode === curExpandNode)
		return;
	if (curExpandNode && curExpandNode.open == true) {
		var zTree = zTree_Menu;
		if (newNode.parentTId === curExpandNode.parentTId) {
			zTree.expandNode(curExpandNode, false);
		} else {
			var newParents = [];
			while (newNode) {
				newNode = newNode.getParentNode();
				if (newNode === curExpandNode) {
					newParents = null;
					break;
				} else if (newNode) {
					newParents.push(newNode);
				}
			}
			if (newParents != null) {
				var oldNode = curExpandNode;
				var oldParents = [];
				while (oldNode) {
					oldNode = oldNode.getParentNode();
					if (oldNode) {
						oldParents.push(oldNode);
					}
				}
				if (newParents.length > 0) {
					for (var i = Math.min(newParents.length, oldParents.length) - 1; i >= 0; i--) {
						if (newParents[i] !== oldParents[i]) {
							zTree.expandNode(oldParents[i], false);
							break;
						}
					}
				} else {
					zTree.expandNode(oldParents[oldParents.length - 1], false);
				}
			}
		}
	}
	curExpandNode = newNode;
}

var portalConfig = {};

portalConfig.defaultsLid = "l2_0";

portalConfig.layouts = {
	l1_0 : [ '100%' ],
	l2_0 : [ '50%', '50%' ],
	l2_1 : [ '60%', '40%' ],
	l2_2 : [ '70%', '30%' ],
	l2_3 : [ '30%', '70%' ],
	l2_3 : [ '40%', '60%' ],
	l3_0 : [ '33%', '33%', '33%', ],
	l3_1 : [ '60%', '20%', '20%' ],
	l3_2 : [ '20%', '60%', '20%' ],
	l3_3 : [ '20%', '20%', '60%' ],
	l3_4 : [ '100%', '50%', '50%' ],
	l4_0 : [ '25%', '25%', '25%', '25%' ],
	l4_1 : [ '30%', '25%', '25%', '20%' ],
	l4_2 : [ '30%', '30%', '20%', '20%' ],
	l4_3 : [ '30%', '30%', '30%', '10%' ],
	l4_4 : [ '10%', '30%', '30%', '30%' ],
	l4_5 : [ '25%', '25%', '30%', '20%' ]
};

portalConfig.layoutsToolbar = {
	l1 : {
		category : "一个区域",
		layoutIDs : "l1"
	},
	l2 : {
		category : "二个区域",
		layoutIDs : "l2"
	},
	l3 : {
		category : "三个区域",
		layoutIDs : "l3"
	},
	l4 : {
		category : "四个区域",
		layoutIDs : "l4"
	}
};

var portal_index = {
	stic : {
		ids : 't001',
		title : '首页'
	}
};

var PortalLet_DATA = [ {
	id : 'TaskSubmit',
	title : '待办任务',
	thumbnail : 'x5/css/d/options/widgets/taskWaiting.png',
	refresh : 'true',
	height : 230,
	psmCount : 0,
	arguments : {
		url : '/JBIZ/flw/flwcommo/taskpoLet/wait_task_view.html',
		process : '/SA/task/taskView/taskViewProcess',
		activity : 'mainActivity',
		params : ''
	}
}, {
	id : 'Favorites',
	title : '收藏夹',
	thumbnail : '',
	refresh : 'true',
	height : 230,
	psmCount : 1,
	arguments : {
		url : '/JBIZ/oa/Favorites/FavoritesLook.html',
		process : '',
		activity : '',
		params : ''
	}
}, {
	id : 'notes',
	title : '通知公告',
	thumbnail : '',
	refresh : 'true',
	height : 230,
	psmCount : 0,
	arguments : {
		url : '/JBIZ/oa/notice/NocticeAndBulletin/shownews.html',
		process : '',
		activity : '',
		params : ''
	}
}, {
	id : 'DAYREMAND',
	title : '邮件提醒',
	thumbnail : '',
	refresh : 'true',
	height : 230,
	psmCount : 1,
	arguments : {
		url : '/JBIZ/oa/email/portalShow/Show.html',
		process : '',
		activity : '',
		params : ''
	}
}, {
	id : 'newsshow',
	title : '新闻',
	thumbnail : '',
	refresh : 'true',
	height : 230,
	psmCount : 0,
	arguments : {
		url : '/JBIZ/system/News/informationRelase/shownews.html',
		process : '',
		activity : '',
		params : ''
	}
}, {
	id : 'TaskWaiting',
	title : '日程安排',
	height : 230,
	refresh : 'true',
	psmCount : 1,
	arguments : {
		url : '/JBIZ/SA/personal/schedule/enterprise_affairsShow.html',
		process : '/SA/task/taskView/taskViewProcess',
		activity : 'mainActivity',
		params : ''
	}
} ];

/**
 * jQuery EasyUI 1.3.5
 * 
 * Copyright (c) 2009-2013 www.jeasyui.com. All rights reserved.
 *
 * Licensed under the GPL or commercial licenses
 * To use it on other terms please contact us: info@jeasyui.com
 * http://www.gnu.org/licenses/gpl.txt
 * http://www.jeasyui.com/license_commercial.php
 *
 */
(function($){
$.parser={auto:true,onComplete:function(_1){
},plugins:["draggable","droppable","resizable","pagination","tooltip","linkbutton","menu","menubutton","splitbutton","progressbar","tree","combobox","combotree","combogrid","numberbox","validatebox","searchbox","numberspinner","timespinner","calendar","datebox","datetimebox","slider","layout","panel","datagrid","propertygrid","treegrid","tabs","accordion","window","dialog"],parse:function(_2){
var aa=[];
for(var i=0;i<$.parser.plugins.length;i++){
var _3=$.parser.plugins[i];
var r=$(".easyui-"+_3,_2);
if(r.length){
if(r[_3]){
r[_3]();
}else{
aa.push({name:_3,jq:r});
}
}
}
if(aa.length&&window.easyloader){
var _4=[];
for(var i=0;i<aa.length;i++){
_4.push(aa[i].name);
}
easyloader.load(_4,function(){
for(var i=0;i<aa.length;i++){
var _5=aa[i].name;
var jq=aa[i].jq;
jq[_5]();
}
$.parser.onComplete.call($.parser,_2);
});
}else{
$.parser.onComplete.call($.parser,_2);
}
},parseOptions:function(_6,_7){
var t=$(_6);
var _8={};
var s=$.trim(t.attr("data-options"));
if(s){
if(s.substring(0,1)!="{"){
s="{"+s+"}";
}
_8=(new Function("return "+s))();
}
if(_7){
var _9={};
for(var i=0;i<_7.length;i++){
var pp=_7[i];
if(typeof pp=="string"){
if(pp=="width"||pp=="height"||pp=="left"||pp=="top"){
_9[pp]=parseInt(_6.style[pp])||undefined;
}else{
_9[pp]=t.attr(pp);
}
}else{
for(var _a in pp){
var _b=pp[_a];
if(_b=="boolean"){
_9[_a]=t.attr(_a)?(t.attr(_a)=="true"):undefined;
}else{
if(_b=="number"){
_9[_a]=t.attr(_a)=="0"?0:parseFloat(t.attr(_a))||undefined;
}
}
}
}
}
$.extend(_8,_9);
}
return _8;
}};
$(function(){
var d=$("<div style=\"position:absolute;top:-1000px;width:100px;height:100px;padding:5px\"></div>").appendTo("body");
d.width(100);
$._boxModel=parseInt(d.width())==100;
d.remove();
if(!window.easyloader&&$.parser.auto){
$.parser.parse();
}
});
$.fn._outerWidth=function(_c){
if(_c==undefined){
if(this[0]==window){
return this.width()||document.body.clientWidth;
}
return this.outerWidth()||0;
}
return this.each(function(){
if($._boxModel){
$(this).width(_c-($(this).outerWidth()-$(this).width()));
}else{
$(this).width(_c);
}
});
};
$.fn._outerHeight=function(_d){
if(_d==undefined){
if(this[0]==window){
return this.height()||document.body.clientHeight;
}
return this.outerHeight()||0;
}
return this.each(function(){
if($._boxModel){
$(this).height(_d-($(this).outerHeight()-$(this).height()));
}else{
$(this).height(_d);
}
});
};
$.fn._scrollLeft=function(_e){
if(_e==undefined){
return this.scrollLeft();
}else{
return this.each(function(){
$(this).scrollLeft(_e);
});
}
};
$.fn._propAttr=$.fn.prop||$.fn.attr;
$.fn._fit=function(_f){
_f=_f==undefined?true:_f;
var t=this[0];
var p=(t.tagName=="BODY"?t:this.parent()[0]);
var _10=p.fcount||0;
if(_f){
if(!t.fitted){
t.fitted=true;
p.fcount=_10+1;
$(p).addClass("panel-noscroll");
if(p.tagName=="BODY"){
$("html").addClass("panel-fit");
}
}
}else{
if(t.fitted){
t.fitted=false;
p.fcount=_10-1;
if(p.fcount==0){
$(p).removeClass("panel-noscroll");
if(p.tagName=="BODY"){
$("html").removeClass("panel-fit");
}
}
}
}
return {width:$(p).width(),height:$(p).height()};
};
})(jQuery);
(function($){
var _11=null;
var _12=null;
var _13=false;
function _14(e){
if(e.touches.length!=1){
return;
}
if(!_13){
_13=true;
dblClickTimer=setTimeout(function(){
_13=false;
},500);
}else{
clearTimeout(dblClickTimer);
_13=false;
_15(e,"dblclick");
}
_11=setTimeout(function(){
_15(e,"contextmenu",3);
},1000);
_15(e,"mousedown");
if($.fn.draggable.isDragging||$.fn.resizable.isResizing){
e.preventDefault();
}
};
function _16(e){
if(e.touches.length!=1){
return;
}
if(_11){
clearTimeout(_11);
}
_15(e,"mousemove");
if($.fn.draggable.isDragging||$.fn.resizable.isResizing){
e.preventDefault();
}
};
function _17(e){
if(_11){
clearTimeout(_11);
}
_15(e,"mouseup");
if($.fn.draggable.isDragging||$.fn.resizable.isResizing){
e.preventDefault();
}
};
function _15(e,_18,_19){
var _1a=new $.Event(_18);
_1a.pageX=e.changedTouches[0].pageX;
_1a.pageY=e.changedTouches[0].pageY;
_1a.which=_19||1;
$(e.target).trigger(_1a);
};
if(document.addEventListener){
document.addEventListener("touchstart",_14,true);
document.addEventListener("touchmove",_16,true);
document.addEventListener("touchend",_17,true);
}
})(jQuery);
(function($){
function _1b(e){
var _1c=$.data(e.data.target,"draggable");
var _1d=_1c.options;
var _1e=_1c.proxy;
var _1f=e.data;
var _20=_1f.startLeft+e.pageX-_1f.startX;
var top=_1f.startTop+e.pageY-_1f.startY;
if(_1e){
if(_1e.parent()[0]==document.body){
if(_1d.deltaX!=null&&_1d.deltaX!=undefined){
_20=e.pageX+_1d.deltaX;
}else{
_20=e.pageX-e.data.offsetWidth;
}
if(_1d.deltaY!=null&&_1d.deltaY!=undefined){
top=e.pageY+_1d.deltaY;
}else{
top=e.pageY-e.data.offsetHeight;
}
}else{
if(_1d.deltaX!=null&&_1d.deltaX!=undefined){
_20+=e.data.offsetWidth+_1d.deltaX;
}
if(_1d.deltaY!=null&&_1d.deltaY!=undefined){
top+=e.data.offsetHeight+_1d.deltaY;
}
}
}
if(e.data.parent!=document.body){
_20+=$(e.data.parent).scrollLeft();
top+=$(e.data.parent).scrollTop();
}
if(_1d.axis=="h"){
_1f.left=_20;
}else{
if(_1d.axis=="v"){
_1f.top=top;
}else{
_1f.left=_20;
_1f.top=top;
}
}
};
function _21(e){
var _22=$.data(e.data.target,"draggable");
var _23=_22.options;
var _24=_22.proxy;
if(!_24){
_24=$(e.data.target);
}
_24.css({left:e.data.left,top:e.data.top});
$("body").css("cursor",_23.cursor);
};
function _25(e){
$.fn.draggable.isDragging=true;
var _26=$.data(e.data.target,"draggable");
var _27=_26.options;
var _28=$(".droppable").filter(function(){
return e.data.target!=this;
}).filter(function(){
var _29=$.data(this,"droppable").options.accept;
if(_29){
return $(_29).filter(function(){
return this==e.data.target;
}).length>0;
}else{
return true;
}
});
_26.droppables=_28;
var _2a=_26.proxy;
if(!_2a){
if(_27.proxy){
if(_27.proxy=="clone"){
_2a=$(e.data.target).clone().insertAfter(e.data.target);
}else{
_2a=_27.proxy.call(e.data.target,e.data.target);
}
_26.proxy=_2a;
}else{
_2a=$(e.data.target);
}
}
_2a.css("position","absolute");
_1b(e);
_21(e);
_27.onStartDrag.call(e.data.target,e);
return false;
};
function _2b(e){
var _2c=$.data(e.data.target,"draggable");
_1b(e);
if(_2c.options.onDrag.call(e.data.target,e)!=false){
_21(e);
}
var _2d=e.data.target;
_2c.droppables.each(function(){
var _2e=$(this);
if(_2e.droppable("options").disabled){
return;
}
var p2=_2e.offset();
if(e.pageX>p2.left&&e.pageX<p2.left+_2e.outerWidth()&&e.pageY>p2.top&&e.pageY<p2.top+_2e.outerHeight()){
if(!this.entered){
$(this).trigger("_dragenter",[_2d]);
this.entered=true;
}
$(this).trigger("_dragover",[_2d]);
}else{
if(this.entered){
$(this).trigger("_dragleave",[_2d]);
this.entered=false;
}
}
});
return false;
};
function _2f(e){
$.fn.draggable.isDragging=false;
_2b(e);
var _30=$.data(e.data.target,"draggable");
var _31=_30.proxy;
var _32=_30.options;
if(_32.revert){
if(_33()==true){
$(e.data.target).css({position:e.data.startPosition,left:e.data.startLeft,top:e.data.startTop});
}else{
if(_31){
var _34,top;
if(_31.parent()[0]==document.body){
_34=e.data.startX-e.data.offsetWidth;
top=e.data.startY-e.data.offsetHeight;
}else{
_34=e.data.startLeft;
top=e.data.startTop;
}
_31.animate({left:_34,top:top},function(){
_35();
});
}else{
$(e.data.target).animate({left:e.data.startLeft,top:e.data.startTop},function(){
$(e.data.target).css("position",e.data.startPosition);
});
}
}
}else{
$(e.data.target).css({position:"absolute",left:e.data.left,top:e.data.top});
_33();
}
_32.onStopDrag.call(e.data.target,e);
$(document).unbind(".draggable");
setTimeout(function(){
$("body").css("cursor","");
},100);
function _35(){
if(_31){
_31.remove();
}
_30.proxy=null;
};
function _33(){
var _36=false;
_30.droppables.each(function(){
var _37=$(this);
if(_37.droppable("options").disabled){
return;
}
var p2=_37.offset();
if(e.pageX>p2.left&&e.pageX<p2.left+_37.outerWidth()&&e.pageY>p2.top&&e.pageY<p2.top+_37.outerHeight()){
if(_32.revert){
$(e.data.target).css({position:e.data.startPosition,left:e.data.startLeft,top:e.data.startTop});
}
$(this).trigger("_drop",[e.data.target]);
_35();
_36=true;
this.entered=false;
return false;
}
});
if(!_36&&!_32.revert){
_35();
}
return _36;
};
return false;
};
$.fn.draggable=function(_38,_39){
if(typeof _38=="string"){
return $.fn.draggable.methods[_38](this,_39);
}
return this.each(function(){
var _3a;
var _3b=$.data(this,"draggable");
if(_3b){
_3b.handle.unbind(".draggable");
_3a=$.extend(_3b.options,_38);
}else{
_3a=$.extend({},$.fn.draggable.defaults,$.fn.draggable.parseOptions(this),_38||{});
}
var _3c=_3a.handle?(typeof _3a.handle=="string"?$(_3a.handle,this):_3a.handle):$(this);
$.data(this,"draggable",{options:_3a,handle:_3c});
if(_3a.disabled){
$(this).css("cursor","");
return;
}
_3c.unbind(".draggable").bind("mousemove.draggable",{target:this},function(e){
if($.fn.draggable.isDragging){
return;
}
var _3d=$.data(e.data.target,"draggable").options;
if(_3e(e)){
$(this).css("cursor",_3d.cursor);
}else{
$(this).css("cursor","");
}
}).bind("mouseleave.draggable",{target:this},function(e){
$(this).css("cursor","");
}).bind("mousedown.draggable",{target:this},function(e){
if(_3e(e)==false){
return;
}
$(this).css("cursor","");
var _3f=$(e.data.target).position();
var _40=$(e.data.target).offset();
var _41={startPosition:$(e.data.target).css("position"),startLeft:_3f.left,startTop:_3f.top,left:_3f.left,top:_3f.top,startX:e.pageX,startY:e.pageY,offsetWidth:(e.pageX-_40.left),offsetHeight:(e.pageY-_40.top),target:e.data.target,parent:$(e.data.target).parent()[0]};
$.extend(e.data,_41);
var _42=$.data(e.data.target,"draggable").options;
if(_42.onBeforeDrag.call(e.data.target,e)==false){
return;
}
$(document).bind("mousedown.draggable",e.data,_25);
$(document).bind("mousemove.draggable",e.data,_2b);
$(document).bind("mouseup.draggable",e.data,_2f);
});
function _3e(e){
var _43=$.data(e.data.target,"draggable");
var _44=_43.handle;
var _45=$(_44).offset();
var _46=$(_44).outerWidth();
var _47=$(_44).outerHeight();
var t=e.pageY-_45.top;
var r=_45.left+_46-e.pageX;
var b=_45.top+_47-e.pageY;
var l=e.pageX-_45.left;
return Math.min(t,r,b,l)>_43.options.edge;
};
});
};
$.fn.draggable.methods={options:function(jq){
return $.data(jq[0],"draggable").options;
},proxy:function(jq){
return $.data(jq[0],"draggable").proxy;
},enable:function(jq){
return jq.each(function(){
$(this).draggable({disabled:false});
});
},disable:function(jq){
return jq.each(function(){
$(this).draggable({disabled:true});
});
}};
$.fn.draggable.parseOptions=function(_48){
var t=$(_48);
return $.extend({},$.parser.parseOptions(_48,["cursor","handle","axis",{"revert":"boolean","deltaX":"number","deltaY":"number","edge":"number"}]),{disabled:(t.attr("disabled")?true:undefined)});
};
$.fn.draggable.defaults={proxy:null,revert:false,cursor:"move",deltaX:null,deltaY:null,handle:null,disabled:false,edge:0,axis:null,onBeforeDrag:function(e){
},onStartDrag:function(e){
},onDrag:function(e){
},onStopDrag:function(e){
}};
$.fn.draggable.isDragging=false;
})(jQuery);
(function($){
function _49(_4a){
$(_4a).addClass("droppable");
$(_4a).bind("_dragenter",function(e,_4b){
$.data(_4a,"droppable").options.onDragEnter.apply(_4a,[e,_4b]);
});
$(_4a).bind("_dragleave",function(e,_4c){
$.data(_4a,"droppable").options.onDragLeave.apply(_4a,[e,_4c]);
});
$(_4a).bind("_dragover",function(e,_4d){
$.data(_4a,"droppable").options.onDragOver.apply(_4a,[e,_4d]);
});
$(_4a).bind("_drop",function(e,_4e){
$.data(_4a,"droppable").options.onDrop.apply(_4a,[e,_4e]);
});
};
$.fn.droppable=function(_4f,_50){
if(typeof _4f=="string"){
return $.fn.droppable.methods[_4f](this,_50);
}
_4f=_4f||{};
return this.each(function(){
var _51=$.data(this,"droppable");
if(_51){
$.extend(_51.options,_4f);
}else{
_49(this);
$.data(this,"droppable",{options:$.extend({},$.fn.droppable.defaults,$.fn.droppable.parseOptions(this),_4f)});
}
});
};
$.fn.droppable.methods={options:function(jq){
return $.data(jq[0],"droppable").options;
},enable:function(jq){
return jq.each(function(){
$(this).droppable({disabled:false});
});
},disable:function(jq){
return jq.each(function(){
$(this).droppable({disabled:true});
});
}};
$.fn.droppable.parseOptions=function(_52){
var t=$(_52);
return $.extend({},$.parser.parseOptions(_52,["accept"]),{disabled:(t.attr("disabled")?true:undefined)});
};
$.fn.droppable.defaults={accept:null,disabled:false,onDragEnter:function(e,_53){
},onDragOver:function(e,_54){
},onDragLeave:function(e,_55){
},onDrop:function(e,_56){
}};
})(jQuery);
(function($){
$.fn.resizable=function(_57,_58){
if(typeof _57=="string"){
return $.fn.resizable.methods[_57](this,_58);
}
function _59(e){
var _5a=e.data;
var _5b=$.data(_5a.target,"resizable").options;
if(_5a.dir.indexOf("e")!=-1){
var _5c=_5a.startWidth+e.pageX-_5a.startX;
_5c=Math.min(Math.max(_5c,_5b.minWidth),_5b.maxWidth);
_5a.width=_5c;
}
if(_5a.dir.indexOf("s")!=-1){
var _5d=_5a.startHeight+e.pageY-_5a.startY;
_5d=Math.min(Math.max(_5d,_5b.minHeight),_5b.maxHeight);
_5a.height=_5d;
}
if(_5a.dir.indexOf("w")!=-1){
var _5c=_5a.startWidth-e.pageX+_5a.startX;
_5c=Math.min(Math.max(_5c,_5b.minWidth),_5b.maxWidth);
_5a.width=_5c;
_5a.left=_5a.startLeft+_5a.startWidth-_5a.width;
}
if(_5a.dir.indexOf("n")!=-1){
var _5d=_5a.startHeight-e.pageY+_5a.startY;
_5d=Math.min(Math.max(_5d,_5b.minHeight),_5b.maxHeight);
_5a.height=_5d;
_5a.top=_5a.startTop+_5a.startHeight-_5a.height;
}
};
function _5e(e){
var _5f=e.data;
var t=$(_5f.target);
t.css({left:_5f.left,top:_5f.top});
if(t.outerWidth()!=_5f.width){
t._outerWidth(_5f.width);
}
if(t.outerHeight()!=_5f.height){
t._outerHeight(_5f.height);
}
};
function _60(e){
$.fn.resizable.isResizing=true;
$.data(e.data.target,"resizable").options.onStartResize.call(e.data.target,e);
return false;
};
function _61(e){
_59(e);
if($.data(e.data.target,"resizable").options.onResize.call(e.data.target,e)!=false){
_5e(e);
}
return false;
};
function _62(e){
$.fn.resizable.isResizing=false;
_59(e,true);
_5e(e);
$.data(e.data.target,"resizable").options.onStopResize.call(e.data.target,e);
$(document).unbind(".resizable");
$("body").css("cursor","");
return false;
};
return this.each(function(){
var _63=null;
var _64=$.data(this,"resizable");
if(_64){
$(this).unbind(".resizable");
_63=$.extend(_64.options,_57||{});
}else{
_63=$.extend({},$.fn.resizable.defaults,$.fn.resizable.parseOptions(this),_57||{});
$.data(this,"resizable",{options:_63});
}
if(_63.disabled==true){
return;
}
$(this).bind("mousemove.resizable",{target:this},function(e){
if($.fn.resizable.isResizing){
return;
}
var dir=_65(e);
if(dir==""){
$(e.data.target).css("cursor","");
}else{
$(e.data.target).css("cursor",dir+"-resize");
}
}).bind("mouseleave.resizable",{target:this},function(e){
$(e.data.target).css("cursor","");
}).bind("mousedown.resizable",{target:this},function(e){
var dir=_65(e);
if(dir==""){
return;
}
function _66(css){
var val=parseInt($(e.data.target).css(css));
if(isNaN(val)){
return 0;
}else{
return val;
}
};
var _67={target:e.data.target,dir:dir,startLeft:_66("left"),startTop:_66("top"),left:_66("left"),top:_66("top"),startX:e.pageX,startY:e.pageY,startWidth:$(e.data.target).outerWidth(),startHeight:$(e.data.target).outerHeight(),width:$(e.data.target).outerWidth(),height:$(e.data.target).outerHeight(),deltaWidth:$(e.data.target).outerWidth()-$(e.data.target).width(),deltaHeight:$(e.data.target).outerHeight()-$(e.data.target).height()};
$(document).bind("mousedown.resizable",_67,_60);
$(document).bind("mousemove.resizable",_67,_61);
$(document).bind("mouseup.resizable",_67,_62);
$("body").css("cursor",dir+"-resize");
});
function _65(e){
var tt=$(e.data.target);
var dir="";
var _68=tt.offset();
var _69=tt.outerWidth();
var _6a=tt.outerHeight();
var _6b=_63.edge;
if(e.pageY>_68.top&&e.pageY<_68.top+_6b){
dir+="n";
}else{
if(e.pageY<_68.top+_6a&&e.pageY>_68.top+_6a-_6b){
dir+="s";
}
}
if(e.pageX>_68.left&&e.pageX<_68.left+_6b){
dir+="w";
}else{
if(e.pageX<_68.left+_69&&e.pageX>_68.left+_69-_6b){
dir+="e";
}
}
var _6c=_63.handles.split(",");
for(var i=0;i<_6c.length;i++){
var _6d=_6c[i].replace(/(^\s*)|(\s*$)/g,"");
if(_6d=="all"||_6d==dir){
return dir;
}
}
return "";
};
});
};
$.fn.resizable.methods={options:function(jq){
return $.data(jq[0],"resizable").options;
},enable:function(jq){
return jq.each(function(){
$(this).resizable({disabled:false});
});
},disable:function(jq){
return jq.each(function(){
$(this).resizable({disabled:true});
});
}};
$.fn.resizable.parseOptions=function(_6e){
var t=$(_6e);
return $.extend({},$.parser.parseOptions(_6e,["handles",{minWidth:"number",minHeight:"number",maxWidth:"number",maxHeight:"number",edge:"number"}]),{disabled:(t.attr("disabled")?true:undefined)});
};
$.fn.resizable.defaults={disabled:false,handles:"n, e, s, w, ne, se, sw, nw, all",minWidth:10,minHeight:10,maxWidth:10000,maxHeight:10000,edge:5,onStartResize:function(e){
},onResize:function(e){
},onStopResize:function(e){
}};
$.fn.resizable.isResizing=false;
})(jQuery);
(function($){
function _6f(_70){
var _71=$.data(_70,"linkbutton").options;
var t=$(_70);
t.addClass("l-btn").removeClass("l-btn-plain l-btn-selected l-btn-plain-selected");
if(_71.plain){
t.addClass("l-btn-plain");
}
if(_71.selected){
t.addClass(_71.plain?"l-btn-selected l-btn-plain-selected":"l-btn-selected");
}
t.attr("group",_71.group||"");
t.attr("id",_71.id||"");
t.html("<span class=\"l-btn-left\">"+"<span class=\"l-btn-text\"></span>"+"</span>");
if(_71.text){
t.find(".l-btn-text").html(_71.text);
if(_71.iconCls){
t.find(".l-btn-text").addClass(_71.iconCls).addClass(_71.iconAlign=="left"?"l-btn-icon-left":"l-btn-icon-right");
}
}else{
t.find(".l-btn-text").html("<span class=\"l-btn-empty\">&nbsp;</span>");
if(_71.iconCls){
t.find(".l-btn-empty").addClass(_71.iconCls);
}
}
t.unbind(".linkbutton").bind("focus.linkbutton",function(){
if(!_71.disabled){
$(this).find(".l-btn-text").addClass("l-btn-focus");
}
}).bind("blur.linkbutton",function(){
$(this).find(".l-btn-text").removeClass("l-btn-focus");
});
if(_71.toggle&&!_71.disabled){
t.bind("click.linkbutton",function(){
if(_71.selected){
$(this).linkbutton("unselect");
}else{
$(this).linkbutton("select");
}
});
}
_72(_70,_71.selected);
_73(_70,_71.disabled);
};
function _72(_74,_75){
var _76=$.data(_74,"linkbutton").options;
if(_75){
if(_76.group){
$("a.l-btn[group=\""+_76.group+"\"]").each(function(){
var o=$(this).linkbutton("options");
if(o.toggle){
$(this).removeClass("l-btn-selected l-btn-plain-selected");
o.selected=false;
}
});
}
$(_74).addClass(_76.plain?"l-btn-selected l-btn-plain-selected":"l-btn-selected");
_76.selected=true;
}else{
if(!_76.group){
$(_74).removeClass("l-btn-selected l-btn-plain-selected");
_76.selected=false;
}
}
};
function _73(_77,_78){
var _79=$.data(_77,"linkbutton");
var _7a=_79.options;
$(_77).removeClass("l-btn-disabled l-btn-plain-disabled");
if(_78){
_7a.disabled=true;
var _7b=$(_77).attr("href");
if(_7b){
_79.href=_7b;
$(_77).attr("href","javascript:void(0)");
}
if(_77.onclick){
_79.onclick=_77.onclick;
_77.onclick=null;
}
_7a.plain?$(_77).addClass("l-btn-disabled l-btn-plain-disabled"):$(_77).addClass("l-btn-disabled");
}else{
_7a.disabled=false;
if(_79.href){
$(_77).attr("href",_79.href);
}
if(_79.onclick){
_77.onclick=_79.onclick;
}
}
};
$.fn.linkbutton=function(_7c,_7d){
if(typeof _7c=="string"){
return $.fn.linkbutton.methods[_7c](this,_7d);
}
_7c=_7c||{};
return this.each(function(){
var _7e=$.data(this,"linkbutton");
if(_7e){
$.extend(_7e.options,_7c);
}else{
$.data(this,"linkbutton",{options:$.extend({},$.fn.linkbutton.defaults,$.fn.linkbutton.parseOptions(this),_7c)});
$(this).removeAttr("disabled");
}
_6f(this);
});
};
$.fn.linkbutton.methods={options:function(jq){
return $.data(jq[0],"linkbutton").options;
},enable:function(jq){
return jq.each(function(){
_73(this,false);
});
},disable:function(jq){
return jq.each(function(){
_73(this,true);
});
},select:function(jq){
return jq.each(function(){
_72(this,true);
});
},unselect:function(jq){
return jq.each(function(){
_72(this,false);
});
}};
$.fn.linkbutton.parseOptions=function(_7f){
var t=$(_7f);
return $.extend({},$.parser.parseOptions(_7f,["id","iconCls","iconAlign","group",{plain:"boolean",toggle:"boolean",selected:"boolean"}]),{disabled:(t.attr("disabled")?true:undefined),text:$.trim(t.html()),iconCls:(t.attr("icon")||t.attr("iconCls"))});
};
$.fn.linkbutton.defaults={id:null,disabled:false,toggle:false,selected:false,group:null,plain:false,text:"",iconCls:null,iconAlign:"left"};
})(jQuery);
(function($){
function _80(_81){
var _82=$.data(_81,"pagination");
var _83=_82.options;
var bb=_82.bb={};
var _84=$(_81).addClass("pagination").html("<table cellspacing=\"0\" cellpadding=\"0\" border=\"0\"><tr></tr></table>");
var tr=_84.find("tr");
var aa=$.extend([],_83.layout);
if(!_83.showPageList){
_85(aa,"list");
}
if(!_83.showRefresh){
_85(aa,"refresh");
}
if(aa[0]=="sep"){
aa.shift();
}
if(aa[aa.length-1]=="sep"){
aa.pop();
}
for(var _86=0;_86<aa.length;_86++){
var _87=aa[_86];
if(_87=="list"){
var ps=$("<select class=\"pagination-page-list\"></select>");
ps.bind("change",function(){
_83.pageSize=parseInt($(this).val());
_83.onChangePageSize.call(_81,_83.pageSize);
_8d(_81,_83.pageNumber);
});
for(var i=0;i<_83.pageList.length;i++){
$("<option></option>").text(_83.pageList[i]).appendTo(ps);
}
$("<td></td>").append(ps).appendTo(tr);
}else{
if(_87=="sep"){
$("<td><div class=\"pagination-btn-separator\"></div></td>").appendTo(tr);
}else{
if(_87=="first"){
bb.first=_88("first");
}else{
if(_87=="prev"){
bb.prev=_88("prev");
}else{
if(_87=="next"){
bb.next=_88("next");
}else{
if(_87=="last"){
bb.last=_88("last");
}else{
if(_87=="manual"){
$("<span style=\"padding-left:6px;\"></span>").html(_83.beforePageText).appendTo(tr).wrap("<td></td>");
bb.num=$("<input class=\"pagination-num\" type=\"text\" value=\"1\" size=\"2\">").appendTo(tr).wrap("<td></td>");
bb.num.unbind(".pagination").bind("keydown.pagination",function(e){
if(e.keyCode==13){
var _89=parseInt($(this).val())||1;
_8d(_81,_89);
return false;
}
});
bb.after=$("<span style=\"padding-right:6px;\"></span>").appendTo(tr).wrap("<td></td>");
}else{
if(_87=="refresh"){
bb.refresh=_88("refresh");
}else{
if(_87=="links"){
$("<td class=\"pagination-links\"></td>").appendTo(tr);
}
}
}
}
}
}
}
}
}
}
if(_83.buttons){
$("<td><div class=\"pagination-btn-separator\"></div></td>").appendTo(tr);
if($.isArray(_83.buttons)){
for(var i=0;i<_83.buttons.length;i++){
var btn=_83.buttons[i];
if(btn=="-"){
$("<td><div class=\"pagination-btn-separator\"></div></td>").appendTo(tr);
}else{
var td=$("<td></td>").appendTo(tr);
var a=$("<a href=\"javascript:void(0)\"></a>").appendTo(td);
a[0].onclick=eval(btn.handler||function(){
});
a.linkbutton($.extend({},btn,{plain:true}));
}
}
}else{
var td=$("<td></td>").appendTo(tr);
$(_83.buttons).appendTo(td).show();
}
}
$("<div class=\"pagination-info\"></div>").appendTo(_84);
$("<div style=\"clear:both;\"></div>").appendTo(_84);
function _88(_8a){
var btn=_83.nav[_8a];
var a=$("<a href=\"javascript:void(0)\"></a>").appendTo(tr);
a.wrap("<td></td>");
a.linkbutton({iconCls:btn.iconCls,plain:true}).unbind(".pagination").bind("click.pagination",function(){
btn.handler.call(_81);
});
return a;
};
function _85(aa,_8b){
var _8c=$.inArray(_8b,aa);
if(_8c>=0){
aa.splice(_8c,1);
}
return aa;
};
};
function _8d(_8e,_8f){
var _90=$.data(_8e,"pagination").options;
_91(_8e,{pageNumber:_8f});
_90.onSelectPage.call(_8e,_90.pageNumber,_90.pageSize);
};
function _91(_92,_93){
var _94=$.data(_92,"pagination");
var _95=_94.options;
var bb=_94.bb;
$.extend(_95,_93||{});
var ps=$(_92).find("select.pagination-page-list");
if(ps.length){
ps.val(_95.pageSize+"");
_95.pageSize=parseInt(ps.val());
}
var _96=Math.ceil(_95.total/_95.pageSize)||1;
if(_95.pageNumber<1){
_95.pageNumber=1;
}
if(_95.pageNumber>_96){
_95.pageNumber=_96;
}
if(bb.num){
bb.num.val(_95.pageNumber);
}
if(bb.after){
bb.after.html(_95.afterPageText.replace(/{pages}/,_96));
}
var td=$(_92).find("td.pagination-links");
if(td.length){
td.empty();
var _97=_95.pageNumber-Math.floor(_95.links/2);
if(_97<1){
_97=1;
}
var _98=_97+_95.links-1;
if(_98>_96){
_98=_96;
}
_97=_98-_95.links+1;
if(_97<1){
_97=1;
}
for(var i=_97;i<=_98;i++){
var a=$("<a class=\"pagination-link\" href=\"javascript:void(0)\"></a>").appendTo(td);
a.linkbutton({plain:true,text:i});
if(i==_95.pageNumber){
a.linkbutton("select");
}else{
a.unbind(".pagination").bind("click.pagination",{pageNumber:i},function(e){
_8d(_92,e.data.pageNumber);
});
}
}
}
var _99=_95.displayMsg;
_99=_99.replace(/{from}/,_95.total==0?0:_95.pageSize*(_95.pageNumber-1)+1);
_99=_99.replace(/{to}/,Math.min(_95.pageSize*(_95.pageNumber),_95.total));
_99=_99.replace(/{total}/,_95.total);
$(_92).find("div.pagination-info").html(_99);
if(bb.first){
bb.first.linkbutton({disabled:(_95.pageNumber==1)});
}
if(bb.prev){
bb.prev.linkbutton({disabled:(_95.pageNumber==1)});
}
if(bb.next){
bb.next.linkbutton({disabled:(_95.pageNumber==_96)});
}
if(bb.last){
bb.last.linkbutton({disabled:(_95.pageNumber==_96)});
}
_9a(_92,_95.loading);
};
function _9a(_9b,_9c){
var _9d=$.data(_9b,"pagination");
var _9e=_9d.options;
_9e.loading=_9c;
if(_9e.showRefresh&&_9d.bb.refresh){
_9d.bb.refresh.linkbutton({iconCls:(_9e.loading?"pagination-loading":"pagination-load")});
}
};
$.fn.pagination=function(_9f,_a0){
if(typeof _9f=="string"){
return $.fn.pagination.methods[_9f](this,_a0);
}
_9f=_9f||{};
return this.each(function(){
var _a1;
var _a2=$.data(this,"pagination");
if(_a2){
_a1=$.extend(_a2.options,_9f);
}else{
_a1=$.extend({},$.fn.pagination.defaults,$.fn.pagination.parseOptions(this),_9f);
$.data(this,"pagination",{options:_a1});
}
_80(this);
_91(this);
});
};
$.fn.pagination.methods={options:function(jq){
return $.data(jq[0],"pagination").options;
},loading:function(jq){
return jq.each(function(){
_9a(this,true);
});
},loaded:function(jq){
return jq.each(function(){
_9a(this,false);
});
},refresh:function(jq,_a3){
return jq.each(function(){
_91(this,_a3);
});
},select:function(jq,_a4){
return jq.each(function(){
_8d(this,_a4);
});
}};
$.fn.pagination.parseOptions=function(_a5){
var t=$(_a5);
return $.extend({},$.parser.parseOptions(_a5,[{total:"number",pageSize:"number",pageNumber:"number",links:"number"},{loading:"boolean",showPageList:"boolean",showRefresh:"boolean"}]),{pageList:(t.attr("pageList")?eval(t.attr("pageList")):undefined)});
};
$.fn.pagination.defaults={total:1,pageSize:10,pageNumber:1,pageList:[10,20,30,50],loading:false,buttons:null,showPageList:true,showRefresh:true,links:10,layout:["list","sep","first","prev","sep","manual","sep","next","last","sep","refresh"],onSelectPage:function(_a6,_a7){
},onBeforeRefresh:function(_a8,_a9){
},onRefresh:function(_aa,_ab){
},onChangePageSize:function(_ac){
},beforePageText:"Page",afterPageText:"of {pages}",displayMsg:"Displaying {from} to {to} of {total} items",nav:{first:{iconCls:"pagination-first",handler:function(){
var _ad=$(this).pagination("options");
if(_ad.pageNumber>1){
$(this).pagination("select",1);
}
}},prev:{iconCls:"pagination-prev",handler:function(){
var _ae=$(this).pagination("options");
if(_ae.pageNumber>1){
$(this).pagination("select",_ae.pageNumber-1);
}
}},next:{iconCls:"pagination-next",handler:function(){
var _af=$(this).pagination("options");
var _b0=Math.ceil(_af.total/_af.pageSize);
if(_af.pageNumber<_b0){
$(this).pagination("select",_af.pageNumber+1);
}
}},last:{iconCls:"pagination-last",handler:function(){
var _b1=$(this).pagination("options");
var _b2=Math.ceil(_b1.total/_b1.pageSize);
if(_b1.pageNumber<_b2){
$(this).pagination("select",_b2);
}
}},refresh:{iconCls:"pagination-refresh",handler:function(){
var _b3=$(this).pagination("options");
if(_b3.onBeforeRefresh.call(this,_b3.pageNumber,_b3.pageSize)!=false){
$(this).pagination("select",_b3.pageNumber);
_b3.onRefresh.call(this,_b3.pageNumber,_b3.pageSize);
}
}}}};
})(jQuery);
(function($){
function _b4(_b5){
var _b6=$(_b5);
_b6.addClass("tree");
return _b6;
};
function _b7(_b8){
var _b9=$.data(_b8,"tree").options;
$(_b8).unbind().bind("mouseover",function(e){
var tt=$(e.target);
var _ba=tt.closest("div.tree-node");
if(!_ba.length){
return;
}
_ba.addClass("tree-node-hover");
if(tt.hasClass("tree-hit")){
if(tt.hasClass("tree-expanded")){
tt.addClass("tree-expanded-hover");
}else{
tt.addClass("tree-collapsed-hover");
}
}
e.stopPropagation();
}).bind("mouseout",function(e){
var tt=$(e.target);
var _bb=tt.closest("div.tree-node");
if(!_bb.length){
return;
}
_bb.removeClass("tree-node-hover");
if(tt.hasClass("tree-hit")){
if(tt.hasClass("tree-expanded")){
tt.removeClass("tree-expanded-hover");
}else{
tt.removeClass("tree-collapsed-hover");
}
}
e.stopPropagation();
}).bind("click",function(e){
var tt=$(e.target);
var _bc=tt.closest("div.tree-node");
if(!_bc.length){
return;
}
if(tt.hasClass("tree-hit")){
_121(_b8,_bc[0]);
return false;
}else{
if(tt.hasClass("tree-checkbox")){
_e5(_b8,_bc[0],!tt.hasClass("tree-checkbox1"));
return false;
}else{
_165(_b8,_bc[0]);
_b9.onClick.call(_b8,_bf(_b8,_bc[0]));
}
}
e.stopPropagation();
}).bind("dblclick",function(e){
var _bd=$(e.target).closest("div.tree-node");
if(!_bd.length){
return;
}
_165(_b8,_bd[0]);
_b9.onDblClick.call(_b8,_bf(_b8,_bd[0]));
e.stopPropagation();
}).bind("contextmenu",function(e){
var _be=$(e.target).closest("div.tree-node");
if(!_be.length){
return;
}
_b9.onContextMenu.call(_b8,e,_bf(_b8,_be[0]));
e.stopPropagation();
});
};
function _c0(_c1){
var _c2=$.data(_c1,"tree").options;
_c2.dnd=false;
var _c3=$(_c1).find("div.tree-node");
_c3.draggable("disable");
_c3.css("cursor","pointer");
};
function _c4(_c5){
var _c6=$.data(_c5,"tree");
var _c7=_c6.options;
var _c8=_c6.tree;
_c6.disabledNodes=[];
_c7.dnd=true;
_c8.find("div.tree-node").draggable({disabled:false,revert:true,cursor:"pointer",proxy:function(_c9){
var p=$("<div class=\"tree-node-proxy\"></div>").appendTo("body");
p.html("<span class=\"tree-dnd-icon tree-dnd-no\">&nbsp;</span>"+$(_c9).find(".tree-title").html());
p.hide();
return p;
},deltaX:15,deltaY:15,onBeforeDrag:function(e){
if(_c7.onBeforeDrag.call(_c5,_bf(_c5,this))==false){
return false;
}
if($(e.target).hasClass("tree-hit")||$(e.target).hasClass("tree-checkbox")){
return false;
}
if(e.which!=1){
return false;
}
$(this).next("ul").find("div.tree-node").droppable({accept:"no-accept"});
var _ca=$(this).find("span.tree-indent");
if(_ca.length){
e.data.offsetWidth-=_ca.length*_ca.width();
}
},onStartDrag:function(){
$(this).draggable("proxy").css({left:-10000,top:-10000});
_c7.onStartDrag.call(_c5,_bf(_c5,this));
var _cb=_bf(_c5,this);
if(_cb.id==undefined){
_cb.id="easyui_tree_node_id_temp";
_105(_c5,_cb);
}
_c6.draggingNodeId=_cb.id;
},onDrag:function(e){
var x1=e.pageX,y1=e.pageY,x2=e.data.startX,y2=e.data.startY;
var d=Math.sqrt((x1-x2)*(x1-x2)+(y1-y2)*(y1-y2));
if(d>3){
$(this).draggable("proxy").show();
}
this.pageY=e.pageY;
},onStopDrag:function(){
$(this).next("ul").find("div.tree-node").droppable({accept:"div.tree-node"});
for(var i=0;i<_c6.disabledNodes.length;i++){
$(_c6.disabledNodes[i]).droppable("enable");
}
_c6.disabledNodes=[];
var _cc=_15d(_c5,_c6.draggingNodeId);
if(_cc&&_cc.id=="easyui_tree_node_id_temp"){
_cc.id="";
_105(_c5,_cc);
}
_c7.onStopDrag.call(_c5,_cc);
}}).droppable({accept:"div.tree-node",onDragEnter:function(e,_cd){
if(_c7.onDragEnter.call(_c5,this,_bf(_c5,_cd))==false){
_ce(_cd,false);
$(this).removeClass("tree-node-append tree-node-top tree-node-bottom");
$(this).droppable("disable");
_c6.disabledNodes.push(this);
}
},onDragOver:function(e,_cf){
if($(this).droppable("options").disabled){
return;
}
var _d0=_cf.pageY;
var top=$(this).offset().top;
var _d1=top+$(this).outerHeight();
_ce(_cf,true);
$(this).removeClass("tree-node-append tree-node-top tree-node-bottom");
if(_d0>top+(_d1-top)/2){
if(_d1-_d0<5){
$(this).addClass("tree-node-bottom");
}else{
$(this).addClass("tree-node-append");
}
}else{
if(_d0-top<5){
$(this).addClass("tree-node-top");
}else{
$(this).addClass("tree-node-append");
}
}
if(_c7.onDragOver.call(_c5,this,_bf(_c5,_cf))==false){
_ce(_cf,false);
$(this).removeClass("tree-node-append tree-node-top tree-node-bottom");
$(this).droppable("disable");
_c6.disabledNodes.push(this);
}
},onDragLeave:function(e,_d2){
_ce(_d2,false);
$(this).removeClass("tree-node-append tree-node-top tree-node-bottom");
_c7.onDragLeave.call(_c5,this,_bf(_c5,_d2));
},onDrop:function(e,_d3){
var _d4=this;
var _d5,_d6;
if($(this).hasClass("tree-node-append")){
_d5=_d7;
_d6="append";
}else{
_d5=_d8;
_d6=$(this).hasClass("tree-node-top")?"top":"bottom";
}
if(_c7.onBeforeDrop.call(_c5,_d4,_158(_c5,_d3),_d6)==false){
$(this).removeClass("tree-node-append tree-node-top tree-node-bottom");
return;
}
_d5(_d3,_d4,_d6);
$(this).removeClass("tree-node-append tree-node-top tree-node-bottom");
}});
function _ce(_d9,_da){
var _db=$(_d9).draggable("proxy").find("span.tree-dnd-icon");
_db.removeClass("tree-dnd-yes tree-dnd-no").addClass(_da?"tree-dnd-yes":"tree-dnd-no");
};
function _d7(_dc,_dd){
if(_bf(_c5,_dd).state=="closed"){
_119(_c5,_dd,function(){
_de();
});
}else{
_de();
}
function _de(){
var _df=$(_c5).tree("pop",_dc);
$(_c5).tree("append",{parent:_dd,data:[_df]});
_c7.onDrop.call(_c5,_dd,_df,"append");
};
};
function _d8(_e0,_e1,_e2){
var _e3={};
if(_e2=="top"){
_e3.before=_e1;
}else{
_e3.after=_e1;
}
var _e4=$(_c5).tree("pop",_e0);
_e3.data=_e4;
$(_c5).tree("insert",_e3);
_c7.onDrop.call(_c5,_e1,_e4,_e2);
};
};
function _e5(_e6,_e7,_e8){
var _e9=$.data(_e6,"tree").options;
if(!_e9.checkbox){
return;
}
var _ea=_bf(_e6,_e7);
if(_e9.onBeforeCheck.call(_e6,_ea,_e8)==false){
return;
}
var _eb=$(_e7);
var ck=_eb.find(".tree-checkbox");
ck.removeClass("tree-checkbox0 tree-checkbox1 tree-checkbox2");
if(_e8){
ck.addClass("tree-checkbox1");
}else{
ck.addClass("tree-checkbox0");
}
if(_e9.cascadeCheck){
_ec(_eb);
_ed(_eb);
}
_e9.onCheck.call(_e6,_ea,_e8);
function _ed(_ee){
var _ef=_ee.next().find(".tree-checkbox");
_ef.removeClass("tree-checkbox0 tree-checkbox1 tree-checkbox2");
if(_ee.find(".tree-checkbox").hasClass("tree-checkbox1")){
_ef.addClass("tree-checkbox1");
}else{
_ef.addClass("tree-checkbox0");
}
};
function _ec(_f0){
var _f1=_12c(_e6,_f0[0]);
if(_f1){
var ck=$(_f1.target).find(".tree-checkbox");
ck.removeClass("tree-checkbox0 tree-checkbox1 tree-checkbox2");
if(_f2(_f0)){
ck.addClass("tree-checkbox1");
}else{
if(_f3(_f0)){
ck.addClass("tree-checkbox0");
}else{
ck.addClass("tree-checkbox2");
}
}
_ec($(_f1.target));
}
function _f2(n){
var ck=n.find(".tree-checkbox");
if(ck.hasClass("tree-checkbox0")||ck.hasClass("tree-checkbox2")){
return false;
}
var b=true;
n.parent().siblings().each(function(){
if(!$(this).children("div.tree-node").children(".tree-checkbox").hasClass("tree-checkbox1")){
b=false;
}
});
return b;
};
function _f3(n){
var ck=n.find(".tree-checkbox");
if(ck.hasClass("tree-checkbox1")||ck.hasClass("tree-checkbox2")){
return false;
}
var b=true;
n.parent().siblings().each(function(){
if(!$(this).children("div.tree-node").children(".tree-checkbox").hasClass("tree-checkbox0")){
b=false;
}
});
return b;
};
};
};
function _f4(_f5,_f6){
var _f7=$.data(_f5,"tree").options;
if(!_f7.checkbox){
return;
}
var _f8=$(_f6);
if(_f9(_f5,_f6)){
var ck=_f8.find(".tree-checkbox");
if(ck.length){
if(ck.hasClass("tree-checkbox1")){
_e5(_f5,_f6,true);
}else{
_e5(_f5,_f6,false);
}
}else{
if(_f7.onlyLeafCheck){
$("<span class=\"tree-checkbox tree-checkbox0\"></span>").insertBefore(_f8.find(".tree-title"));
}
}
}else{
var ck=_f8.find(".tree-checkbox");
if(_f7.onlyLeafCheck){
ck.remove();
}else{
if(ck.hasClass("tree-checkbox1")){
_e5(_f5,_f6,true);
}else{
if(ck.hasClass("tree-checkbox2")){
var _fa=true;
var _fb=true;
var _fc=_fd(_f5,_f6);
for(var i=0;i<_fc.length;i++){
if(_fc[i].checked){
_fb=false;
}else{
_fa=false;
}
}
if(_fa){
_e5(_f5,_f6,true);
}
if(_fb){
_e5(_f5,_f6,false);
}
}
}
}
}
};
function _fe(_ff,ul,data,_100){
var _101=$.data(_ff,"tree");
var opts=_101.options;
var _102=$(ul).prevAll("div.tree-node:first");
data=opts.loadFilter.call(_ff,data,_102[0]);
var _103=_104(_ff,"domId",_102.attr("id"));
if(!_100){
_103?_103.children=data:_101.data=data;
$(ul).empty();
}else{
if(_103){
_103.children?_103.children=_103.children.concat(data):_103.children=data;
}else{
_101.data=_101.data.concat(data);
}
}
opts.view.render.call(opts.view,_ff,ul,data);
if(opts.dnd){
_c4(_ff);
}
if(_103){
_105(_ff,_103);
}
var _106=[];
var _107=[];
for(var i=0;i<data.length;i++){
var node=data[i];
if(!node.checked){
_106.push(node);
}
}
_108(data,function(node){
if(node.checked){
_107.push(node);
}
});
if(_106.length){
_e5(_ff,$("#"+_106[0].domId)[0],false);
}
for(var i=0;i<_107.length;i++){
_e5(_ff,$("#"+_107[i].domId)[0],true);
}
setTimeout(function(){
_109(_ff,_ff);
},0);
opts.onLoadSuccess.call(_ff,_103,data);
};
function _109(_10a,ul,_10b){
var opts=$.data(_10a,"tree").options;
if(opts.lines){
$(_10a).addClass("tree-lines");
}else{
$(_10a).removeClass("tree-lines");
return;
}
if(!_10b){
_10b=true;
$(_10a).find("span.tree-indent").removeClass("tree-line tree-join tree-joinbottom");
$(_10a).find("div.tree-node").removeClass("tree-node-last tree-root-first tree-root-one");
var _10c=$(_10a).tree("getRoots");
if(_10c.length>1){
$(_10c[0].target).addClass("tree-root-first");
}else{
if(_10c.length==1){
$(_10c[0].target).addClass("tree-root-one");
}
}
}
$(ul).children("li").each(function(){
var node=$(this).children("div.tree-node");
var ul=node.next("ul");
if(ul.length){
if($(this).next().length){
_10d(node);
}
_109(_10a,ul,_10b);
}else{
_10e(node);
}
});
var _10f=$(ul).children("li:last").children("div.tree-node").addClass("tree-node-last");
_10f.children("span.tree-join").removeClass("tree-join").addClass("tree-joinbottom");
function _10e(node,_110){
var icon=node.find("span.tree-icon");
icon.prev("span.tree-indent").addClass("tree-join");
};
function _10d(node){
var _111=node.find("span.tree-indent, span.tree-hit").length;
node.next().find("div.tree-node").each(function(){
$(this).children("span:eq("+(_111-1)+")").addClass("tree-line");
});
};
};
function _112(_113,ul,_114,_115){
var opts=$.data(_113,"tree").options;
_114=_114||{};
var _116=null;
if(_113!=ul){
var node=$(ul).prev();
_116=_bf(_113,node[0]);
}
if(opts.onBeforeLoad.call(_113,_116,_114)==false){
return;
}
var _117=$(ul).prev().children("span.tree-folder");
_117.addClass("tree-loading");
var _118=opts.loader.call(_113,_114,function(data){
_117.removeClass("tree-loading");
_fe(_113,ul,data);
if(_115){
_115();
}
},function(){
_117.removeClass("tree-loading");
opts.onLoadError.apply(_113,arguments);
if(_115){
_115();
}
});
if(_118==false){
_117.removeClass("tree-loading");
}
};
function _119(_11a,_11b,_11c){
var opts=$.data(_11a,"tree").options;
var hit=$(_11b).children("span.tree-hit");
if(hit.length==0){
return;
}
if(hit.hasClass("tree-expanded")){
return;
}
var node=_bf(_11a,_11b);
if(opts.onBeforeExpand.call(_11a,node)==false){
return;
}
hit.removeClass("tree-collapsed tree-collapsed-hover").addClass("tree-expanded");
hit.next().addClass("tree-folder-open");
var ul=$(_11b).next();
if(ul.length){
if(opts.animate){
ul.slideDown("normal",function(){
node.state="open";
opts.onExpand.call(_11a,node);
if(_11c){
_11c();
}
});
}else{
ul.css("display","block");
node.state="open";
opts.onExpand.call(_11a,node);
if(_11c){
_11c();
}
}
}else{
var _11d=$("<ul style=\"display:none\"></ul>").insertAfter(_11b);
_112(_11a,_11d[0],{id:node.id},function(){
if(_11d.is(":empty")){
_11d.remove();
}
if(opts.animate){
_11d.slideDown("normal",function(){
node.state="open";
opts.onExpand.call(_11a,node);
if(_11c){
_11c();
}
});
}else{
_11d.css("display","block");
node.state="open";
opts.onExpand.call(_11a,node);
if(_11c){
_11c();
}
}
});
}
};
function _11e(_11f,_120){
var opts=$.data(_11f,"tree").options;
var hit=$(_120).children("span.tree-hit");
if(hit.length==0){
return;
}
if(hit.hasClass("tree-collapsed")){
return;
}
var node=_bf(_11f,_120);
if(opts.onBeforeCollapse.call(_11f,node)==false){
return;
}
hit.removeClass("tree-expanded tree-expanded-hover").addClass("tree-collapsed");
hit.next().removeClass("tree-folder-open");
var ul=$(_120).next();
if(opts.animate){
ul.slideUp("normal",function(){
node.state="closed";
opts.onCollapse.call(_11f,node);
});
}else{
ul.css("display","none");
node.state="closed";
opts.onCollapse.call(_11f,node);
}
};
function _121(_122,_123){
var hit=$(_123).children("span.tree-hit");
if(hit.length==0){
return;
}
if(hit.hasClass("tree-expanded")){
_11e(_122,_123);
}else{
_119(_122,_123);
}
};
function _124(_125,_126){
var _127=_fd(_125,_126);
if(_126){
_127.unshift(_bf(_125,_126));
}
for(var i=0;i<_127.length;i++){
_119(_125,_127[i].target);
}
};
function _128(_129,_12a){
var _12b=[];
var p=_12c(_129,_12a);
while(p){
_12b.unshift(p);
p=_12c(_129,p.target);
}
for(var i=0;i<_12b.length;i++){
_119(_129,_12b[i].target);
}
};
function _12d(_12e,_12f){
var c=$(_12e).parent();
while(c[0].tagName!="BODY"&&c.css("overflow-y")!="auto"){
c=c.parent();
}
var n=$(_12f);
var ntop=n.offset().top;
if(c[0].tagName!="BODY"){
var ctop=c.offset().top;
if(ntop<ctop){
c.scrollTop(c.scrollTop()+ntop-ctop);
}else{
if(ntop+n.outerHeight()>ctop+c.outerHeight()-18){
c.scrollTop(c.scrollTop()+ntop+n.outerHeight()-ctop-c.outerHeight()+18);
}
}
}else{
c.scrollTop(ntop);
}
};
function _130(_131,_132){
var _133=_fd(_131,_132);
if(_132){
_133.unshift(_bf(_131,_132));
}
for(var i=0;i<_133.length;i++){
_11e(_131,_133[i].target);
}
};
function _134(_135,_136){
var node=$(_136.parent);
var data=_136.data;
if(!data){
return;
}
data=$.isArray(data)?data:[data];
if(!data.length){
return;
}
var ul;
if(node.length==0){
ul=$(_135);
}else{
if(_f9(_135,node[0])){
var _137=node.find("span.tree-icon");
_137.removeClass("tree-file").addClass("tree-folder tree-folder-open");
var hit=$("<span class=\"tree-hit tree-expanded\"></span>").insertBefore(_137);
if(hit.prev().length){
hit.prev().remove();
}
}
ul=node.next();
if(!ul.length){
ul=$("<ul></ul>").insertAfter(node);
}
}
_fe(_135,ul[0],data,true);
_f4(_135,ul.prev());
};
function _138(_139,_13a){
var ref=_13a.before||_13a.after;
var _13b=_12c(_139,ref);
var data=_13a.data;
if(!data){
return;
}
data=$.isArray(data)?data:[data];
if(!data.length){
return;
}
_134(_139,{parent:(_13b?_13b.target:null),data:data});
var li=$();
for(var i=0;i<data.length;i++){
li=li.add($("#"+data[i].domId).parent());
}
if(_13a.before){
li.insertBefore($(ref).parent());
}else{
li.insertAfter($(ref).parent());
}
};
function _13c(_13d,_13e){
var _13f=del(_13e);
$(_13e).parent().remove();
if(_13f){
if(!_13f.children||!_13f.children.length){
var node=$(_13f.target);
node.find(".tree-icon").removeClass("tree-folder").addClass("tree-file");
node.find(".tree-hit").remove();
$("<span class=\"tree-indent\"></span>").prependTo(node);
node.next().remove();
}
_105(_13d,_13f);
_f4(_13d,_13f.target);
}
_109(_13d,_13d);
function del(_140){
var id=$(_140).attr("id");
var _141=_12c(_13d,_140);
var cc=_141?_141.children:$.data(_13d,"tree").data;
for(var i=0;i<cc.length;i++){
if(cc[i].domId==id){
cc.splice(i,1);
break;
}
}
return _141;
};
};
function _105(_142,_143){
var opts=$.data(_142,"tree").options;
var node=$(_143.target);
var data=_bf(_142,_143.target);
var _144=data.checked;
if(data.iconCls){
node.find(".tree-icon").removeClass(data.iconCls);
}
$.extend(data,_143);
node.find(".tree-title").html(opts.formatter.call(_142,data));
if(data.iconCls){
node.find(".tree-icon").addClass(data.iconCls);
}
if(_144!=data.checked){
_e5(_142,_143.target,data.checked);
}
};
function _145(_146){
var _147=_148(_146);
return _147.length?_147[0]:null;
};
function _148(_149){
var _14a=$.data(_149,"tree").data;
for(var i=0;i<_14a.length;i++){
_14b(_14a[i]);
}
return _14a;
};
function _fd(_14c,_14d){
var _14e=[];
var n=_bf(_14c,_14d);
var data=n?n.children:$.data(_14c,"tree").data;
_108(data,function(node){
_14e.push(_14b(node));
});
return _14e;
};
function _12c(_14f,_150){
var p=$(_150).closest("ul").prevAll("div.tree-node:first");
return _bf(_14f,p[0]);
};
function _151(_152,_153){
_153=_153||"checked";
if(!$.isArray(_153)){
_153=[_153];
}
var _154=[];
for(var i=0;i<_153.length;i++){
var s=_153[i];
if(s=="checked"){
_154.push("span.tree-checkbox1");
}else{
if(s=="unchecked"){
_154.push("span.tree-checkbox0");
}else{
if(s=="indeterminate"){
_154.push("span.tree-checkbox2");
}
}
}
}
var _155=[];
$(_152).find(_154.join(",")).each(function(){
var node=$(this).parent();
_155.push(_bf(_152,node[0]));
});
return _155;
};
function _156(_157){
var node=$(_157).find("div.tree-node-selected");
return node.length?_bf(_157,node[0]):null;
};
function _158(_159,_15a){
var data=_bf(_159,_15a);
if(data&&data.children){
_108(data.children,function(node){
_14b(node);
});
}
return data;
};
function _bf(_15b,_15c){
return _104(_15b,"domId",$(_15c).attr("id"));
};
function _15d(_15e,id){
return _104(_15e,"id",id);
};
function _104(_15f,_160,_161){
var data=$.data(_15f,"tree").data;
var _162=null;
_108(data,function(node){
if(node[_160]==_161){
_162=_14b(node);
return false;
}
});
return _162;
};
function _14b(node){
var d=$("#"+node.domId);
node.target=d[0];
node.checked=d.find(".tree-checkbox").hasClass("tree-checkbox1");
return node;
};
function _108(data,_163){
var _164=[];
for(var i=0;i<data.length;i++){
_164.push(data[i]);
}
while(_164.length){
var node=_164.shift();
if(_163(node)==false){
return;
}
if(node.children){
for(var i=node.children.length-1;i>=0;i--){
_164.unshift(node.children[i]);
}
}
}
};
function _165(_166,_167){
var opts=$.data(_166,"tree").options;
var node=_bf(_166,_167);
if(opts.onBeforeSelect.call(_166,node)==false){
return;
}
$(_166).find("div.tree-node-selected").removeClass("tree-node-selected");
$(_167).addClass("tree-node-selected");
opts.onSelect.call(_166,node);
};
function _f9(_168,_169){
return $(_169).children("span.tree-hit").length==0;
};
function _16a(_16b,_16c){
var opts=$.data(_16b,"tree").options;
var node=_bf(_16b,_16c);
if(opts.onBeforeEdit.call(_16b,node)==false){
return;
}
$(_16c).css("position","relative");
var nt=$(_16c).find(".tree-title");
var _16d=nt.outerWidth();
nt.empty();
var _16e=$("<input class=\"tree-editor\">").appendTo(nt);
_16e.val(node.text).focus();
_16e.width(_16d+20);
_16e.height(document.compatMode=="CSS1Compat"?(18-(_16e.outerHeight()-_16e.height())):18);
_16e.bind("click",function(e){
return false;
}).bind("mousedown",function(e){
e.stopPropagation();
}).bind("mousemove",function(e){
e.stopPropagation();
}).bind("keydown",function(e){
if(e.keyCode==13){
_16f(_16b,_16c);
return false;
}else{
if(e.keyCode==27){
_173(_16b,_16c);
return false;
}
}
}).bind("blur",function(e){
e.stopPropagation();
_16f(_16b,_16c);
});
};
function _16f(_170,_171){
var opts=$.data(_170,"tree").options;
$(_171).css("position","");
var _172=$(_171).find("input.tree-editor");
var val=_172.val();
_172.remove();
var node=_bf(_170,_171);
node.text=val;
_105(_170,node);
opts.onAfterEdit.call(_170,node);
};
function _173(_174,_175){
var opts=$.data(_174,"tree").options;
$(_175).css("position","");
$(_175).find("input.tree-editor").remove();
var node=_bf(_174,_175);
_105(_174,node);
opts.onCancelEdit.call(_174,node);
};
$.fn.tree=function(_176,_177){
if(typeof _176=="string"){
return $.fn.tree.methods[_176](this,_177);
}
var _176=_176||{};
return this.each(function(){
var _178=$.data(this,"tree");
var opts;
if(_178){
opts=$.extend(_178.options,_176);
_178.options=opts;
}else{
opts=$.extend({},$.fn.tree.defaults,$.fn.tree.parseOptions(this),_176);
$.data(this,"tree",{options:opts,tree:_b4(this),data:[]});
var data=$.fn.tree.parseData(this);
if(data.length){
_fe(this,this,data);
}
}
_b7(this);
if(opts.data){
_fe(this,this,opts.data);
}
_112(this,this);
});
};
$.fn.tree.methods={options:function(jq){
return $.data(jq[0],"tree").options;
},loadData:function(jq,data){
return jq.each(function(){
_fe(this,this,data);
});
},getNode:function(jq,_179){
return _bf(jq[0],_179);
},getData:function(jq,_17a){
return _158(jq[0],_17a);
},reload:function(jq,_17b){
return jq.each(function(){
if(_17b){
var node=$(_17b);
var hit=node.children("span.tree-hit");
hit.removeClass("tree-expanded tree-expanded-hover").addClass("tree-collapsed");
node.next().remove();
_119(this,_17b);
}else{
$(this).empty();
_112(this,this);
}
});
},getRoot:function(jq){
return _145(jq[0]);
},getRoots:function(jq){
return _148(jq[0]);
},getParent:function(jq,_17c){
return _12c(jq[0],_17c);
},getChildren:function(jq,_17d){
return _fd(jq[0],_17d);
},getChecked:function(jq,_17e){
return _151(jq[0],_17e);
},getSelected:function(jq){
return _156(jq[0]);
},isLeaf:function(jq,_17f){
return _f9(jq[0],_17f);
},find:function(jq,id){
return _15d(jq[0],id);
},select:function(jq,_180){
return jq.each(function(){
_165(this,_180);
});
},check:function(jq,_181){
return jq.each(function(){
_e5(this,_181,true);
});
},uncheck:function(jq,_182){
return jq.each(function(){
_e5(this,_182,false);
});
},collapse:function(jq,_183){
return jq.each(function(){
_11e(this,_183);
});
},expand:function(jq,_184){
return jq.each(function(){
_119(this,_184);
});
},collapseAll:function(jq,_185){
return jq.each(function(){
_130(this,_185);
});
},expandAll:function(jq,_186){
return jq.each(function(){
_124(this,_186);
});
},expandTo:function(jq,_187){
return jq.each(function(){
_128(this,_187);
});
},scrollTo:function(jq,_188){
return jq.each(function(){
_12d(this,_188);
});
},toggle:function(jq,_189){
return jq.each(function(){
_121(this,_189);
});
},append:function(jq,_18a){
return jq.each(function(){
_134(this,_18a);
});
},insert:function(jq,_18b){
return jq.each(function(){
_138(this,_18b);
});
},remove:function(jq,_18c){
return jq.each(function(){
_13c(this,_18c);
});
},pop:function(jq,_18d){
var node=jq.tree("getData",_18d);
jq.tree("remove",_18d);
return node;
},update:function(jq,_18e){
return jq.each(function(){
_105(this,_18e);
});
},enableDnd:function(jq){
return jq.each(function(){
_c4(this);
});
},disableDnd:function(jq){
return jq.each(function(){
_c0(this);
});
},beginEdit:function(jq,_18f){
return jq.each(function(){
_16a(this,_18f);
});
},endEdit:function(jq,_190){
return jq.each(function(){
_16f(this,_190);
});
},cancelEdit:function(jq,_191){
return jq.each(function(){
_173(this,_191);
});
}};
$.fn.tree.parseOptions=function(_192){
var t=$(_192);
return $.extend({},$.parser.parseOptions(_192,["url","method",{checkbox:"boolean",cascadeCheck:"boolean",onlyLeafCheck:"boolean"},{animate:"boolean",lines:"boolean",dnd:"boolean"}]));
};
$.fn.tree.parseData=function(_193){
var data=[];
_194(data,$(_193));
return data;
function _194(aa,tree){
tree.children("li").each(function(){
var node=$(this);
var item=$.extend({},$.parser.parseOptions(this,["id","iconCls","state"]),{checked:(node.attr("checked")?true:undefined)});
item.text=node.children("span").html();
if(!item.text){
item.text=node.html();
}
var _195=node.children("ul");
if(_195.length){
item.children=[];
_194(item.children,_195);
}
aa.push(item);
});
};
};
var _196=1;
var _197={render:function(_198,ul,data){
var opts=$.data(_198,"tree").options;
var _199=$(ul).prev("div.tree-node").find("span.tree-indent, span.tree-hit").length;
var cc=_19a(_199,data);
$(ul).append(cc.join(""));
function _19a(_19b,_19c){
var cc=[];
for(var i=0;i<_19c.length;i++){
var item=_19c[i];
if(item.state!="open"&&item.state!="closed"){
item.state="open";
}
item.domId="_easyui_tree_"+_196++;
cc.push("<li>");
cc.push("<div id=\""+item.domId+"\" class=\"tree-node\">");
for(var j=0;j<_19b;j++){
cc.push("<span class=\"tree-indent\"></span>");
}
if(item.state=="closed"){
cc.push("<span class=\"tree-hit tree-collapsed\"></span>");
cc.push("<span class=\"tree-icon tree-folder "+(item.iconCls?item.iconCls:"")+"\"></span>");
}else{
if(item.children&&item.children.length){
cc.push("<span class=\"tree-hit tree-expanded\"></span>");
cc.push("<span class=\"tree-icon tree-folder tree-folder-open "+(item.iconCls?item.iconCls:"")+"\"></span>");
}else{
cc.push("<span class=\"tree-indent\"></span>");
cc.push("<span class=\"tree-icon tree-file "+(item.iconCls?item.iconCls:"")+"\"></span>");
}
}
if(opts.checkbox){
if((!opts.onlyLeafCheck)||(opts.onlyLeafCheck&&(!item.children||!item.children.length))){
cc.push("<span class=\"tree-checkbox tree-checkbox0\"></span>");
}
}
cc.push("<span class=\"tree-title\">"+opts.formatter.call(_198,item)+"</span>");
cc.push("</div>");
if(item.children&&item.children.length){
var tmp=_19a(_19b+1,item.children);
cc.push("<ul style=\"display:"+(item.state=="closed"?"none":"block")+"\">");
cc=cc.concat(tmp);
cc.push("</ul>");
}
cc.push("</li>");
}
return cc;
};
}};
$.fn.tree.defaults={url:null,method:"post",animate:false,checkbox:false,cascadeCheck:true,onlyLeafCheck:false,lines:false,dnd:false,data:null,formatter:function(node){
return node.text;
},loader:function(_19d,_19e,_19f){
var opts=$(this).tree("options");
if(!opts.url){
return false;
}
$.ajax({type:opts.method,url:opts.url,data:_19d,dataType:"json",success:function(data){
_19e(data);
},error:function(){
_19f.apply(this,arguments);
}});
},loadFilter:function(data,_1a0){
return data;
},view:_197,onBeforeLoad:function(node,_1a1){
},onLoadSuccess:function(node,data){
},onLoadError:function(){
},onClick:function(node){
},onDblClick:function(node){
},onBeforeExpand:function(node){
},onExpand:function(node){
},onBeforeCollapse:function(node){
},onCollapse:function(node){
},onBeforeCheck:function(node,_1a2){
},onCheck:function(node,_1a3){
},onBeforeSelect:function(node){
},onSelect:function(node){
},onContextMenu:function(e,node){
},onBeforeDrag:function(node){
},onStartDrag:function(node){
},onStopDrag:function(node){
},onDragEnter:function(_1a4,_1a5){
},onDragOver:function(_1a6,_1a7){
},onDragLeave:function(_1a8,_1a9){
},onBeforeDrop:function(_1aa,_1ab,_1ac){
},onDrop:function(_1ad,_1ae,_1af){
},onBeforeEdit:function(node){
},onAfterEdit:function(node){
},onCancelEdit:function(node){
}};
})(jQuery);
(function($){
function init(_1b0){
$(_1b0).addClass("progressbar");
$(_1b0).html("<div class=\"progressbar-text\"></div><div class=\"progressbar-value\"><div class=\"progressbar-text\"></div></div>");
return $(_1b0);
};
function _1b1(_1b2,_1b3){
var opts=$.data(_1b2,"progressbar").options;
var bar=$.data(_1b2,"progressbar").bar;
if(_1b3){
opts.width=_1b3;
}
bar._outerWidth(opts.width)._outerHeight(opts.height);
bar.find("div.progressbar-text").width(bar.width());
bar.find("div.progressbar-text,div.progressbar-value").css({height:bar.height()+"px",lineHeight:bar.height()+"px"});
};
$.fn.progressbar=function(_1b4,_1b5){
if(typeof _1b4=="string"){
var _1b6=$.fn.progressbar.methods[_1b4];
if(_1b6){
return _1b6(this,_1b5);
}
}
_1b4=_1b4||{};
return this.each(function(){
var _1b7=$.data(this,"progressbar");
if(_1b7){
$.extend(_1b7.options,_1b4);
}else{
_1b7=$.data(this,"progressbar",{options:$.extend({},$.fn.progressbar.defaults,$.fn.progressbar.parseOptions(this),_1b4),bar:init(this)});
}
$(this).progressbar("setValue",_1b7.options.value);
_1b1(this);
});
};
$.fn.progressbar.methods={options:function(jq){
return $.data(jq[0],"progressbar").options;
},resize:function(jq,_1b8){
return jq.each(function(){
_1b1(this,_1b8);
});
},getValue:function(jq){
return $.data(jq[0],"progressbar").options.value;
},setValue:function(jq,_1b9){
if(_1b9<0){
_1b9=0;
}
if(_1b9>100){
_1b9=100;
}
return jq.each(function(){
var opts=$.data(this,"progressbar").options;
var text=opts.text.replace(/{value}/,_1b9);
var _1ba=opts.value;
opts.value=_1b9;
$(this).find("div.progressbar-value").width(_1b9+"%");
$(this).find("div.progressbar-text").html(text);
if(_1ba!=_1b9){
opts.onChange.call(this,_1b9,_1ba);
}
});
}};
$.fn.progressbar.parseOptions=function(_1bb){
return $.extend({},$.parser.parseOptions(_1bb,["width","height","text",{value:"number"}]));
};
$.fn.progressbar.defaults={width:"auto",height:22,value:0,text:"{value}%",onChange:function(_1bc,_1bd){
}};
})(jQuery);
(function($){
function init(_1be){
$(_1be).addClass("tooltip-f");
};
function _1bf(_1c0){
var opts=$.data(_1c0,"tooltip").options;
$(_1c0).unbind(".tooltip").bind(opts.showEvent+".tooltip",function(e){
_1c7(_1c0,e);
}).bind(opts.hideEvent+".tooltip",function(e){
_1cd(_1c0,e);
}).bind("mousemove.tooltip",function(e){
if(opts.trackMouse){
opts.trackMouseX=e.pageX;
opts.trackMouseY=e.pageY;
_1c1(_1c0);
}
});
};
function _1c2(_1c3){
var _1c4=$.data(_1c3,"tooltip");
if(_1c4.showTimer){
clearTimeout(_1c4.showTimer);
_1c4.showTimer=null;
}
if(_1c4.hideTimer){
clearTimeout(_1c4.hideTimer);
_1c4.hideTimer=null;
}
};
function _1c1(_1c5){
var _1c6=$.data(_1c5,"tooltip");
if(!_1c6||!_1c6.tip){
return;
}
var opts=_1c6.options;
var tip=_1c6.tip;
if(opts.trackMouse){
t=$();
var left=opts.trackMouseX+opts.deltaX;
var top=opts.trackMouseY+opts.deltaY;
}else{
var t=$(_1c5);
var left=t.offset().left+opts.deltaX;
var top=t.offset().top+opts.deltaY;
}
switch(opts.position){
case "right":
left+=t._outerWidth()+12+(opts.trackMouse?12:0);
top-=(tip._outerHeight()-t._outerHeight())/2;
break;
case "left":
left-=tip._outerWidth()+12+(opts.trackMouse?12:0);
top-=(tip._outerHeight()-t._outerHeight())/2;
break;
case "top":
left-=(tip._outerWidth()-t._outerWidth())/2;
top-=tip._outerHeight()+12+(opts.trackMouse?12:0);
break;
case "bottom":
left-=(tip._outerWidth()-t._outerWidth())/2;
top+=t._outerHeight()+12+(opts.trackMouse?12:0);
break;
}
if(!$(_1c5).is(":visible")){
left=-100000;
top=-100000;
}
tip.css({left:left,top:top,zIndex:(opts.zIndex!=undefined?opts.zIndex:($.fn.window?$.fn.window.defaults.zIndex++:""))});
opts.onPosition.call(_1c5,left,top);
};
function _1c7(_1c8,e){
var _1c9=$.data(_1c8,"tooltip");
var opts=_1c9.options;
var tip=_1c9.tip;
if(!tip){
tip=$("<div tabindex=\"-1\" class=\"tooltip\">"+"<div class=\"tooltip-content\"></div>"+"<div class=\"tooltip-arrow-outer\"></div>"+"<div class=\"tooltip-arrow\"></div>"+"</div>").appendTo("body");
_1c9.tip=tip;
_1ca(_1c8);
}
tip.removeClass("tooltip-top tooltip-bottom tooltip-left tooltip-right").addClass("tooltip-"+opts.position);
_1c2(_1c8);
_1c9.showTimer=setTimeout(function(){
_1c1(_1c8);
tip.show();
opts.onShow.call(_1c8,e);
var _1cb=tip.children(".tooltip-arrow-outer");
var _1cc=tip.children(".tooltip-arrow");
var bc="border-"+opts.position+"-color";
_1cb.add(_1cc).css({borderTopColor:"",borderBottomColor:"",borderLeftColor:"",borderRightColor:""});
_1cb.css(bc,tip.css(bc));
_1cc.css(bc,tip.css("backgroundColor"));
},opts.showDelay);
};
function _1cd(_1ce,e){
var _1cf=$.data(_1ce,"tooltip");
if(_1cf&&_1cf.tip){
_1c2(_1ce);
_1cf.hideTimer=setTimeout(function(){
_1cf.tip.hide();
_1cf.options.onHide.call(_1ce,e);
},_1cf.options.hideDelay);
}
};
function _1ca(_1d0,_1d1){
var _1d2=$.data(_1d0,"tooltip");
var opts=_1d2.options;
if(_1d1){
opts.content=_1d1;
}
if(!_1d2.tip){
return;
}
var cc=typeof opts.content=="function"?opts.content.call(_1d0):opts.content;
_1d2.tip.children(".tooltip-content").html(cc);
opts.onUpdate.call(_1d0,cc);
};
function _1d3(_1d4){
var _1d5=$.data(_1d4,"tooltip");
if(_1d5){
_1c2(_1d4);
var opts=_1d5.options;
if(_1d5.tip){
_1d5.tip.remove();
}
if(opts._title){
$(_1d4).attr("title",opts._title);
}
$.removeData(_1d4,"tooltip");
$(_1d4).unbind(".tooltip").removeClass("tooltip-f");
opts.onDestroy.call(_1d4);
}
};
$.fn.tooltip=function(_1d6,_1d7){
if(typeof _1d6=="string"){
return $.fn.tooltip.methods[_1d6](this,_1d7);
}
_1d6=_1d6||{};
return this.each(function(){
var _1d8=$.data(this,"tooltip");
if(_1d8){
$.extend(_1d8.options,_1d6);
}else{
$.data(this,"tooltip",{options:$.extend({},$.fn.tooltip.defaults,$.fn.tooltip.parseOptions(this),_1d6)});
init(this);
}
_1bf(this);
_1ca(this);
});
};
$.fn.tooltip.methods={options:function(jq){
return $.data(jq[0],"tooltip").options;
},tip:function(jq){
return $.data(jq[0],"tooltip").tip;
},arrow:function(jq){
return jq.tooltip("tip").children(".tooltip-arrow-outer,.tooltip-arrow");
},show:function(jq,e){
return jq.each(function(){
_1c7(this,e);
});
},hide:function(jq,e){
return jq.each(function(){
_1cd(this,e);
});
},update:function(jq,_1d9){
return jq.each(function(){
_1ca(this,_1d9);
});
},reposition:function(jq){
return jq.each(function(){
_1c1(this);
});
},destroy:function(jq){
return jq.each(function(){
_1d3(this);
});
}};
$.fn.tooltip.parseOptions=function(_1da){
var t=$(_1da);
var opts=$.extend({},$.parser.parseOptions(_1da,["position","showEvent","hideEvent","content",{deltaX:"number",deltaY:"number",showDelay:"number",hideDelay:"number"}]),{_title:t.attr("title")});
t.attr("title","");
if(!opts.content){
opts.content=opts._title;
}
return opts;
};
$.fn.tooltip.defaults={position:"bottom",content:null,trackMouse:false,deltaX:0,deltaY:0,showEvent:"mouseenter",hideEvent:"mouseleave",showDelay:200,hideDelay:100,onShow:function(e){
},onHide:function(e){
},onUpdate:function(_1db){
},onPosition:function(left,top){
},onDestroy:function(){
}};
})(jQuery);
(function($){
$.fn._remove=function(){
return this.each(function(){
$(this).remove();
try{
this.outerHTML="";
}
catch(err){
}
});
};
function _1dc(node){
node._remove();
};
function _1dd(_1de,_1df){
var opts=$.data(_1de,"panel").options;
var _1e0=$.data(_1de,"panel").panel;
var _1e1=_1e0.children("div.panel-header");
var _1e2=_1e0.children("div.panel-body");
if(_1df){
$.extend(opts,{width:_1df.width,height:_1df.height,left:_1df.left,top:_1df.top});
}
opts.fit?$.extend(opts,_1e0._fit()):_1e0._fit(false);
_1e0.css({left:opts.left,top:opts.top});
if(!isNaN(opts.width)){
_1e0._outerWidth(opts.width);
}else{
_1e0.width("auto");
}
_1e1.add(_1e2)._outerWidth(_1e0.width());
if(!isNaN(opts.height)){
_1e0._outerHeight(opts.height);
_1e2._outerHeight(_1e0.height()-_1e1._outerHeight());
}else{
_1e2.height("auto");
}
_1e0.css("height","");
opts.onResize.apply(_1de,[opts.width,opts.height]);
$(_1de).find(">div,>form>div").triggerHandler("_resize");
};
function _1e3(_1e4,_1e5){
var opts=$.data(_1e4,"panel").options;
var _1e6=$.data(_1e4,"panel").panel;
if(_1e5){
if(_1e5.left!=null){
opts.left=_1e5.left;
}
if(_1e5.top!=null){
opts.top=_1e5.top;
}
}
_1e6.css({left:opts.left,top:opts.top});
opts.onMove.apply(_1e4,[opts.left,opts.top]);
};
function _1e7(_1e8){
$(_1e8).addClass("panel-body");
var _1e9=$("<div class=\"panel\"></div>").insertBefore(_1e8);
_1e9[0].appendChild(_1e8);
_1e9.bind("_resize",function(){
var opts=$.data(_1e8,"panel").options;
if(opts.fit==true){
_1dd(_1e8);
}
return false;
});
return _1e9;
};
function _1ea(_1eb){
var opts=$.data(_1eb,"panel").options;
var _1ec=$.data(_1eb,"panel").panel;
if(opts.tools&&typeof opts.tools=="string"){
_1ec.find(">div.panel-header>div.panel-tool .panel-tool-a").appendTo(opts.tools);
}
_1dc(_1ec.children("div.panel-header"));
if(opts.title&&!opts.noheader){
var _1ed=$("<div class=\"panel-header\"><div class=\"panel-title\">"+opts.title+"</div></div>").prependTo(_1ec);
if(opts.iconCls){
_1ed.find(".panel-title").addClass("panel-with-icon");
$("<div class=\"panel-icon\"></div>").addClass(opts.iconCls).appendTo(_1ed);
}
var tool=$("<div class=\"panel-tool\"></div>").appendTo(_1ed);
tool.bind("click",function(e){
e.stopPropagation();
});
if(opts.tools){
if($.isArray(opts.tools)){
for(var i=0;i<opts.tools.length;i++){
var t=$("<a href=\"javascript:void(0)\"></a>").addClass(opts.tools[i].iconCls).appendTo(tool);
if(opts.tools[i].handler){
t.bind("click",eval(opts.tools[i].handler));
}
}
}else{
$(opts.tools).children().each(function(){
$(this).addClass($(this).attr("iconCls")).addClass("panel-tool-a").appendTo(tool);
});
}
}
if(opts.collapsible){
$("<a class=\"panel-tool-collapse\" href=\"javascript:void(0)\"></a>").appendTo(tool).bind("click",function(){
if(opts.collapsed==true){
_208(_1eb,true);
}else{
_1fd(_1eb,true);
}
return false;
});
}
if(opts.minimizable){
$("<a class=\"panel-tool-min\" href=\"javascript:void(0)\"></a>").appendTo(tool).bind("click",function(){
_20e(_1eb);
return false;
});
}
if(opts.maximizable){
$("<a class=\"panel-tool-max\" href=\"javascript:void(0)\"></a>").appendTo(tool).bind("click",function(){
if(opts.maximized==true){
_211(_1eb);
}else{
_1fc(_1eb);
}
return false;
});
}
if(opts.closable){
$("<a class=\"panel-tool-close\" href=\"javascript:void(0)\"></a>").appendTo(tool).bind("click",function(){
_1ee(_1eb);
return false;
});
}
_1ec.children("div.panel-body").removeClass("panel-body-noheader");
}else{
_1ec.children("div.panel-body").addClass("panel-body-noheader");
}
};
function _1ef(_1f0){
var _1f1=$.data(_1f0,"panel");
var opts=_1f1.options;
if(opts.href){
if(!_1f1.isLoaded||!opts.cache){
if(opts.onBeforeLoad.call(_1f0)==false){
return;
}
_1f1.isLoaded=false;
_1f2(_1f0);
if(opts.loadingMessage){
$(_1f0).html($("<div class=\"panel-loading\"></div>").html(opts.loadingMessage));
}
$.ajax({url:opts.href,cache:false,dataType:"html",success:function(data){
_1f3(opts.extractor.call(_1f0,data));
opts.onLoad.apply(_1f0,arguments);
_1f1.isLoaded=true;
}});
}
}else{
if(opts.content){
if(!_1f1.isLoaded){
_1f2(_1f0);
_1f3(opts.content);
_1f1.isLoaded=true;
}
}
}
function _1f3(_1f4){
$(_1f0).html(_1f4);
if($.parser){
$.parser.parse($(_1f0));
}
};
};
function _1f2(_1f5){
var t=$(_1f5);
t.find(".combo-f").each(function(){
$(this).combo("destroy");
});
t.find(".m-btn").each(function(){
$(this).menubutton("destroy");
});
t.find(".s-btn").each(function(){
$(this).splitbutton("destroy");
});
t.find(".tooltip-f").each(function(){
$(this).tooltip("destroy");
});
};
function _1f6(_1f7){
$(_1f7).find("div.panel:visible,div.accordion:visible,div.tabs-container:visible,div.layout:visible").each(function(){
$(this).triggerHandler("_resize",[true]);
});
};
function _1f8(_1f9,_1fa){
var opts=$.data(_1f9,"panel").options;
var _1fb=$.data(_1f9,"panel").panel;
if(_1fa!=true){
if(opts.onBeforeOpen.call(_1f9)==false){
return;
}
}
_1fb.show();
opts.closed=false;
opts.minimized=false;
var tool=_1fb.children("div.panel-header").find("a.panel-tool-restore");
if(tool.length){
opts.maximized=true;
}
opts.onOpen.call(_1f9);
if(opts.maximized==true){
opts.maximized=false;
_1fc(_1f9);
}
if(opts.collapsed==true){
opts.collapsed=false;
_1fd(_1f9);
}
if(!opts.collapsed){
_1ef(_1f9);
_1f6(_1f9);
}
};
function _1ee(_1fe,_1ff){
var opts=$.data(_1fe,"panel").options;
var _200=$.data(_1fe,"panel").panel;
if(_1ff!=true){
if(opts.onBeforeClose.call(_1fe)==false){
return;
}
}
_200._fit(false);
_200.hide();
opts.closed=true;
opts.onClose.call(_1fe);
};
function _201(_202,_203){
var opts=$.data(_202,"panel").options;
var _204=$.data(_202,"panel").panel;
if(_203!=true){
if(opts.onBeforeDestroy.call(_202)==false){
return;
}
}
_1f2(_202);
_1dc(_204);
opts.onDestroy.call(_202);
};
function _1fd(_205,_206){
var opts=$.data(_205,"panel").options;
var _207=$.data(_205,"panel").panel;
var body=_207.children("div.panel-body");
var tool=_207.children("div.panel-header").find("a.panel-tool-collapse");
if(opts.collapsed==true){
return;
}
body.stop(true,true);
if(opts.onBeforeCollapse.call(_205)==false){
return;
}
tool.addClass("panel-tool-expand");
if(_206==true){
body.slideUp("normal",function(){
opts.collapsed=true;
opts.onCollapse.call(_205);
});
}else{
body.hide();
opts.collapsed=true;
opts.onCollapse.call(_205);
}
};
function _208(_209,_20a){
var opts=$.data(_209,"panel").options;
var _20b=$.data(_209,"panel").panel;
var body=_20b.children("div.panel-body");
var tool=_20b.children("div.panel-header").find("a.panel-tool-collapse");
if(opts.collapsed==false){
return;
}
body.stop(true,true);
if(opts.onBeforeExpand.call(_209)==false){
return;
}
tool.removeClass("panel-tool-expand");
if(_20a==true){
body.slideDown("normal",function(){
opts.collapsed=false;
opts.onExpand.call(_209);
_1ef(_209);
_1f6(_209);
});
}else{
body.show();
opts.collapsed=false;
opts.onExpand.call(_209);
_1ef(_209);
_1f6(_209);
}
};
function _1fc(_20c){
var opts=$.data(_20c,"panel").options;
var _20d=$.data(_20c,"panel").panel;
var tool=_20d.children("div.panel-header").find("a.panel-tool-max");
if(opts.maximized==true){
return;
}
tool.addClass("panel-tool-restore");
if(!$.data(_20c,"panel").original){
$.data(_20c,"panel").original={width:opts.width,height:opts.height,left:opts.left,top:opts.top,fit:opts.fit};
}
opts.left=0;
opts.top=0;
opts.fit=true;
_1dd(_20c);
opts.minimized=false;
opts.maximized=true;
opts.onMaximize.call(_20c);
};
function _20e(_20f){
var opts=$.data(_20f,"panel").options;
var _210=$.data(_20f,"panel").panel;
_210._fit(false);
_210.hide();
opts.minimized=true;
opts.maximized=false;
opts.onMinimize.call(_20f);
};
function _211(_212){
var opts=$.data(_212,"panel").options;
var _213=$.data(_212,"panel").panel;
var tool=_213.children("div.panel-header").find("a.panel-tool-max");
if(opts.maximized==false){
return;
}
_213.show();
tool.removeClass("panel-tool-restore");
$.extend(opts,$.data(_212,"panel").original);
_1dd(_212);
opts.minimized=false;
opts.maximized=false;
$.data(_212,"panel").original=null;
opts.onRestore.call(_212);
};
function _214(_215){
var opts=$.data(_215,"panel").options;
var _216=$.data(_215,"panel").panel;
var _217=$(_215).panel("header");
var body=$(_215).panel("body");
_216.css(opts.style);
_216.addClass(opts.cls);
if(opts.border){
_217.removeClass("panel-header-noborder");
body.removeClass("panel-body-noborder");
}else{
_217.addClass("panel-header-noborder");
body.addClass("panel-body-noborder");
}
_217.addClass(opts.headerCls);
body.addClass(opts.bodyCls);
if(opts.id){
$(_215).attr("id",opts.id);
}else{
$(_215).attr("id","");
}
};
function _218(_219,_21a){
$.data(_219,"panel").options.title=_21a;
$(_219).panel("header").find("div.panel-title").html(_21a);
};
var TO=false;
var _21b=true;
$(window).unbind(".panel").bind("resize.panel",function(){
if(!_21b){
return;
}
if(TO!==false){
clearTimeout(TO);
}
TO=setTimeout(function(){
_21b=false;
var _21c=$("body.layout");
if(_21c.length){
_21c.layout("resize");
}else{
$("body").children("div.panel,div.accordion,div.tabs-container,div.layout").triggerHandler("_resize");
}
_21b=true;
TO=false;
},200);
});
$.fn.panel=function(_21d,_21e){
if(typeof _21d=="string"){
return $.fn.panel.methods[_21d](this,_21e);
}
_21d=_21d||{};
return this.each(function(){
var _21f=$.data(this,"panel");
var opts;
if(_21f){
opts=$.extend(_21f.options,_21d);
_21f.isLoaded=false;
}else{
opts=$.extend({},$.fn.panel.defaults,$.fn.panel.parseOptions(this),_21d);
$(this).attr("title","");
_21f=$.data(this,"panel",{options:opts,panel:_1e7(this),isLoaded:false});
}
_1ea(this);
_214(this);
if(opts.doSize==true){
_21f.panel.css("display","block");
_1dd(this);
}
if(opts.closed==true||opts.minimized==true){
_21f.panel.hide();
}else{
_1f8(this);
}
});
};
$.fn.panel.methods={options:function(jq){
return $.data(jq[0],"panel").options;
},panel:function(jq){
return $.data(jq[0],"panel").panel;
},header:function(jq){
return $.data(jq[0],"panel").panel.find(">div.panel-header");
},body:function(jq){
return $.data(jq[0],"panel").panel.find(">div.panel-body");
},setTitle:function(jq,_220){
return jq.each(function(){
_218(this,_220);
});
},open:function(jq,_221){
return jq.each(function(){
_1f8(this,_221);
});
},close:function(jq,_222){
return jq.each(function(){
_1ee(this,_222);
});
},destroy:function(jq,_223){
return jq.each(function(){
_201(this,_223);
});
},refresh:function(jq,href){
return jq.each(function(){
$.data(this,"panel").isLoaded=false;
if(href){
$.data(this,"panel").options.href=href;
}
_1ef(this);
});
},resize:function(jq,_224){
return jq.each(function(){
_1dd(this,_224);
});
},move:function(jq,_225){
return jq.each(function(){
_1e3(this,_225);
});
},maximize:function(jq){
return jq.each(function(){
_1fc(this);
});
},minimize:function(jq){
return jq.each(function(){
_20e(this);
});
},restore:function(jq){
return jq.each(function(){
_211(this);
});
},collapse:function(jq,_226){
return jq.each(function(){
_1fd(this,_226);
});
},expand:function(jq,_227){
return jq.each(function(){
_208(this,_227);
});
}};
$.fn.panel.parseOptions=function(_228){
var t=$(_228);
return $.extend({},$.parser.parseOptions(_228,["id","width","height","left","top","title","iconCls","cls","headerCls","bodyCls","tools","href",{cache:"boolean",fit:"boolean",border:"boolean",noheader:"boolean"},{collapsible:"boolean",minimizable:"boolean",maximizable:"boolean"},{closable:"boolean",collapsed:"boolean",minimized:"boolean",maximized:"boolean",closed:"boolean"}]),{loadingMessage:(t.attr("loadingMessage")!=undefined?t.attr("loadingMessage"):undefined)});
};
$.fn.panel.defaults={id:null,title:null,iconCls:null,width:"auto",height:"auto",left:null,top:null,cls:null,headerCls:null,bodyCls:null,style:{},href:null,cache:true,fit:false,border:true,doSize:true,noheader:false,content:null,collapsible:false,minimizable:false,maximizable:false,closable:false,collapsed:false,minimized:false,maximized:false,closed:false,tools:null,href:null,loadingMessage:"Loading...",extractor:function(data){
var _229=/<body[^>]*>((.|[\n\r])*)<\/body>/im;
var _22a=_229.exec(data);
if(_22a){
return _22a[1];
}else{
return data;
}
},onBeforeLoad:function(){
},onLoad:function(){
},onBeforeOpen:function(){
},onOpen:function(){
},onBeforeClose:function(){
},onClose:function(){
},onBeforeDestroy:function(){
},onDestroy:function(){
},onResize:function(_22b,_22c){
},onMove:function(left,top){
},onMaximize:function(){
},onRestore:function(){
},onMinimize:function(){
},onBeforeCollapse:function(){
},onBeforeExpand:function(){
},onCollapse:function(){
},onExpand:function(){
}};
})(jQuery);
(function($){
function _22d(_22e,_22f){
var opts=$.data(_22e,"window").options;
if(_22f){
$.extend(opts,_22f);
}
$(_22e).panel("resize",opts);
};
function _230(_231,_232){
var _233=$.data(_231,"window");
if(_232){
if(_232.left!=null){
_233.options.left=_232.left;
}
if(_232.top!=null){
_233.options.top=_232.top;
}
}
$(_231).panel("move",_233.options);
if(_233.shadow){
_233.shadow.css({left:_233.options.left,top:_233.options.top});
}
};
function _234(_235,_236){
var _237=$.data(_235,"window");
var opts=_237.options;
var _238=opts.width;
if(isNaN(_238)){
_238=_237.window._outerWidth();
}
if(opts.inline){
var _239=_237.window.parent();
opts.left=(_239.width()-_238)/2+_239.scrollLeft();
}else{
opts.left=($(window)._outerWidth()-_238)/2+$(document).scrollLeft();
}
if(_236){
_230(_235);
}
};
function _23a(_23b,_23c){
var _23d=$.data(_23b,"window");
var opts=_23d.options;
var _23e=opts.height;
if(isNaN(_23e)){
_23e=_23d.window._outerHeight();
}
if(opts.inline){
var _23f=_23d.window.parent();
opts.top=(_23f.height()-_23e)/2+_23f.scrollTop();
}else{
opts.top=($(window)._outerHeight()-_23e)/2+$(document).scrollTop();
}
if(_23c){
_230(_23b);
}
};
function _240(_241){
var _242=$.data(_241,"window");
var win=$(_241).panel($.extend({},_242.options,{border:false,doSize:true,closed:true,cls:"window",headerCls:"window-header",bodyCls:"window-body "+(_242.options.noheader?"window-body-noheader":""),onBeforeDestroy:function(){
if(_242.options.onBeforeDestroy.call(_241)==false){
return false;
}
if(_242.shadow){
_242.shadow.remove();
}
if(_242.mask){
_242.mask.remove();
}
},onClose:function(){
if(_242.shadow){
_242.shadow.hide();
}
if(_242.mask){
_242.mask.hide();
}
_242.options.onClose.call(_241);
},onOpen:function(){
if(_242.mask){
_242.mask.css({display:"block",zIndex:$.fn.window.defaults.zIndex++});
}
if(_242.shadow){
_242.shadow.css({display:"block",zIndex:$.fn.window.defaults.zIndex++,left:_242.options.left,top:_242.options.top,width:_242.window._outerWidth(),height:_242.window._outerHeight()});
}
_242.window.css("z-index",$.fn.window.defaults.zIndex++);
_242.options.onOpen.call(_241);
},onResize:function(_243,_244){
var opts=$(this).panel("options");
$.extend(_242.options,{width:opts.width,height:opts.height,left:opts.left,top:opts.top});
if(_242.shadow){
_242.shadow.css({left:_242.options.left,top:_242.options.top,width:_242.window._outerWidth(),height:_242.window._outerHeight()});
}
_242.options.onResize.call(_241,_243,_244);
},onMinimize:function(){
if(_242.shadow){
_242.shadow.hide();
}
if(_242.mask){
_242.mask.hide();
}
_242.options.onMinimize.call(_241);
},onBeforeCollapse:function(){
if(_242.options.onBeforeCollapse.call(_241)==false){
return false;
}
if(_242.shadow){
_242.shadow.hide();
}
},onExpand:function(){
if(_242.shadow){
_242.shadow.show();
}
_242.options.onExpand.call(_241);
}}));
_242.window=win.panel("panel");
if(_242.mask){
_242.mask.remove();
}
if(_242.options.modal==true){
_242.mask=$("<div class=\"window-mask\"></div>").insertAfter(_242.window);
_242.mask.css({width:(_242.options.inline?_242.mask.parent().width():_245().width),height:(_242.options.inline?_242.mask.parent().height():_245().height),display:"none"});
}
if(_242.shadow){
_242.shadow.remove();
}
if(_242.options.shadow==true){
_242.shadow=$("<div class=\"window-shadow\"></div>").insertAfter(_242.window);
_242.shadow.css({display:"none"});
}
if(_242.options.left==null){
_234(_241);
}
if(_242.options.top==null){
_23a(_241);
}
_230(_241);
if(_242.options.closed==false){
win.window("open");
}
};
function _246(_247){
var _248=$.data(_247,"window");
_248.window.draggable({handle:">div.panel-header>div.panel-title",disabled:_248.options.draggable==false,onStartDrag:function(e){
if(_248.mask){
_248.mask.css("z-index",$.fn.window.defaults.zIndex++);
}
if(_248.shadow){
_248.shadow.css("z-index",$.fn.window.defaults.zIndex++);
}
_248.window.css("z-index",$.fn.window.defaults.zIndex++);
if(!_248.proxy){
_248.proxy=$("<div class=\"window-proxy\"></div>").insertAfter(_248.window);
}
_248.proxy.css({display:"none",zIndex:$.fn.window.defaults.zIndex++,left:e.data.left,top:e.data.top});
_248.proxy._outerWidth(_248.window._outerWidth());
_248.proxy._outerHeight(_248.window._outerHeight());
setTimeout(function(){
if(_248.proxy){
_248.proxy.show();
}
},500);
},onDrag:function(e){
_248.proxy.css({display:"block",left:e.data.left,top:e.data.top});
return false;
},onStopDrag:function(e){
_248.options.left=e.data.left;
_248.options.top=e.data.top;
$(_247).window("move");
_248.proxy.remove();
_248.proxy=null;
}});
_248.window.resizable({disabled:_248.options.resizable==false,onStartResize:function(e){
_248.pmask=$("<div class=\"window-proxy-mask\"></div>").insertAfter(_248.window);
_248.pmask.css({zIndex:$.fn.window.defaults.zIndex++,left:e.data.left,top:e.data.top,width:_248.window._outerWidth(),height:_248.window._outerHeight()});
if(!_248.proxy){
_248.proxy=$("<div class=\"window-proxy\"></div>").insertAfter(_248.window);
}
_248.proxy.css({zIndex:$.fn.window.defaults.zIndex++,left:e.data.left,top:e.data.top});
_248.proxy._outerWidth(e.data.width);
_248.proxy._outerHeight(e.data.height);
},onResize:function(e){
_248.proxy.css({left:e.data.left,top:e.data.top});
_248.proxy._outerWidth(e.data.width);
_248.proxy._outerHeight(e.data.height);
return false;
},onStopResize:function(e){
$.extend(_248.options,{left:e.data.left,top:e.data.top,width:e.data.width,height:e.data.height});
_22d(_247);
_248.pmask.remove();
_248.pmask=null;
_248.proxy.remove();
_248.proxy=null;
}});
};
function _245(){
if(document.compatMode=="BackCompat"){
return {width:Math.max(document.body.scrollWidth,document.body.clientWidth),height:Math.max(document.body.scrollHeight,document.body.clientHeight)};
}else{
return {width:Math.max(document.documentElement.scrollWidth,document.documentElement.clientWidth),height:Math.max(document.documentElement.scrollHeight,document.documentElement.clientHeight)};
}
};
$(window).resize(function(){
$("body>div.window-mask").css({width:$(window)._outerWidth(),height:$(window)._outerHeight()});
setTimeout(function(){
$("body>div.window-mask").css({width:_245().width,height:_245().height});
},50);
});
$.fn.window=function(_249,_24a){
if(typeof _249=="string"){
var _24b=$.fn.window.methods[_249];
if(_24b){
return _24b(this,_24a);
}else{
return this.panel(_249,_24a);
}
}
_249=_249||{};
return this.each(function(){
var _24c=$.data(this,"window");
if(_24c){
$.extend(_24c.options,_249);
}else{
_24c=$.data(this,"window",{options:$.extend({},$.fn.window.defaults,$.fn.window.parseOptions(this),_249)});
if(!_24c.options.inline){
document.body.appendChild(this);
}
}
_240(this);
_246(this);
});
};
$.fn.window.methods={options:function(jq){
var _24d=jq.panel("options");
var _24e=$.data(jq[0],"window").options;
return $.extend(_24e,{closed:_24d.closed,collapsed:_24d.collapsed,minimized:_24d.minimized,maximized:_24d.maximized});
},window:function(jq){
return $.data(jq[0],"window").window;
},resize:function(jq,_24f){
return jq.each(function(){
_22d(this,_24f);
});
},move:function(jq,_250){
return jq.each(function(){
_230(this,_250);
});
},hcenter:function(jq){
return jq.each(function(){
_234(this,true);
});
},vcenter:function(jq){
return jq.each(function(){
_23a(this,true);
});
},center:function(jq){
return jq.each(function(){
_234(this);
_23a(this);
_230(this);
});
}};
$.fn.window.parseOptions=function(_251){
return $.extend({},$.fn.panel.parseOptions(_251),$.parser.parseOptions(_251,[{draggable:"boolean",resizable:"boolean",shadow:"boolean",modal:"boolean",inline:"boolean"}]));
};
$.fn.window.defaults=$.extend({},$.fn.panel.defaults,{zIndex:9000,draggable:true,resizable:true,shadow:true,modal:false,inline:false,title:"New Window",collapsible:true,minimizable:true,maximizable:true,closable:true,closed:false});
})(jQuery);
(function($){
function _252(_253){
var cp=document.createElement("div");
while(_253.firstChild){
cp.appendChild(_253.firstChild);
}
_253.appendChild(cp);
var _254=$(cp);
_254.attr("style",$(_253).attr("style"));
$(_253).removeAttr("style").css("overflow","hidden");
_254.panel({border:false,doSize:false,bodyCls:"dialog-content"});
return _254;
};
function _255(_256){
var opts=$.data(_256,"dialog").options;
var _257=$.data(_256,"dialog").contentPanel;
if(opts.toolbar){
if($.isArray(opts.toolbar)){
$(_256).find("div.dialog-toolbar").remove();
var _258=$("<div class=\"dialog-toolbar\"><table cellspacing=\"0\" cellpadding=\"0\"><tr></tr></table></div>").prependTo(_256);
var tr=_258.find("tr");
for(var i=0;i<opts.toolbar.length;i++){
var btn=opts.toolbar[i];
if(btn=="-"){
$("<td><div class=\"dialog-tool-separator\"></div></td>").appendTo(tr);
}else{
var td=$("<td></td>").appendTo(tr);
var tool=$("<a href=\"javascript:void(0)\"></a>").appendTo(td);
tool[0].onclick=eval(btn.handler||function(){
});
tool.linkbutton($.extend({},btn,{plain:true}));
}
}
}else{
$(opts.toolbar).addClass("dialog-toolbar").prependTo(_256);
$(opts.toolbar).show();
}
}else{
$(_256).find("div.dialog-toolbar").remove();
}
if(opts.buttons){
if($.isArray(opts.buttons)){
$(_256).find("div.dialog-button").remove();
var _259=$("<div class=\"dialog-button\"></div>").appendTo(_256);
for(var i=0;i<opts.buttons.length;i++){
var p=opts.buttons[i];
var _25a=$("<a href=\"javascript:void(0)\"></a>").appendTo(_259);
if(p.handler){
_25a[0].onclick=p.handler;
}
_25a.linkbutton(p);
}
}else{
$(opts.buttons).addClass("dialog-button").appendTo(_256);
$(opts.buttons).show();
}
}else{
$(_256).find("div.dialog-button").remove();
}
var _25b=opts.href;
var _25c=opts.content;
opts.href=null;
opts.content=null;
_257.panel({closed:opts.closed,cache:opts.cache,href:_25b,content:_25c,onLoad:function(){
if(opts.height=="auto"){
$(_256).window("resize");
}
opts.onLoad.apply(_256,arguments);
}});
$(_256).window($.extend({},opts,{onOpen:function(){
if(_257.panel("options").closed){
_257.panel("open");
}
if(opts.onOpen){
opts.onOpen.call(_256);
}
},onResize:function(_25d,_25e){
var _25f=$(_256);
_257.panel("panel").show();
_257.panel("resize",{width:_25f.width(),height:(_25e=="auto")?"auto":_25f.height()-_25f.children("div.dialog-toolbar")._outerHeight()-_25f.children("div.dialog-button")._outerHeight()});
if(opts.onResize){
opts.onResize.call(_256,_25d,_25e);
}
}}));
opts.href=_25b;
opts.content=_25c;
};
function _260(_261,href){
var _262=$.data(_261,"dialog").contentPanel;
_262.panel("refresh",href);
};
$.fn.dialog=function(_263,_264){
if(typeof _263=="string"){
var _265=$.fn.dialog.methods[_263];
if(_265){
return _265(this,_264);
}else{
return this.window(_263,_264);
}
}
_263=_263||{};
return this.each(function(){
var _266=$.data(this,"dialog");
if(_266){
$.extend(_266.options,_263);
}else{
$.data(this,"dialog",{options:$.extend({},$.fn.dialog.defaults,$.fn.dialog.parseOptions(this),_263),contentPanel:_252(this)});
}
_255(this);
});
};
$.fn.dialog.methods={options:function(jq){
var _267=$.data(jq[0],"dialog").options;
var _268=jq.panel("options");
$.extend(_267,{closed:_268.closed,collapsed:_268.collapsed,minimized:_268.minimized,maximized:_268.maximized});
var _269=$.data(jq[0],"dialog").contentPanel;
return _267;
},dialog:function(jq){
return jq.window("window");
},refresh:function(jq,href){
return jq.each(function(){
_260(this,href);
});
}};
$.fn.dialog.parseOptions=function(_26a){
return $.extend({},$.fn.window.parseOptions(_26a),$.parser.parseOptions(_26a,["toolbar","buttons"]));
};
$.fn.dialog.defaults=$.extend({},$.fn.window.defaults,{title:"New Dialog",collapsible:false,minimizable:false,maximizable:false,resizable:false,toolbar:null,buttons:null});
})(jQuery);
(function($){
function show(el,type,_26b,_26c){
var win=$(el).window("window");
if(!win){
return;
}
switch(type){
case null:
win.show();
break;
case "slide":
win.slideDown(_26b);
break;
case "fade":
win.fadeIn(_26b);
break;
case "show":
win.show(_26b);
break;
}
var _26d=null;
if(_26c>0){
_26d=setTimeout(function(){
hide(el,type,_26b);
},_26c);
}
win.hover(function(){
if(_26d){
clearTimeout(_26d);
}
},function(){
if(_26c>0){
_26d=setTimeout(function(){
hide(el,type,_26b);
},_26c);
}
});
};
function hide(el,type,_26e){
if(el.locked==true){
return;
}
el.locked=true;
var win=$(el).window("window");
if(!win){
return;
}
switch(type){
case null:
win.hide();
break;
case "slide":
win.slideUp(_26e);
break;
case "fade":
win.fadeOut(_26e);
break;
case "show":
win.hide(_26e);
break;
}
setTimeout(function(){
$(el).window("destroy");
},_26e);
};
function _26f(_270){
var opts=$.extend({},$.fn.window.defaults,{collapsible:false,minimizable:false,maximizable:false,shadow:false,draggable:false,resizable:false,closed:true,style:{left:"",top:"",right:0,zIndex:$.fn.window.defaults.zIndex++,bottom:-document.body.scrollTop-document.documentElement.scrollTop},onBeforeOpen:function(){
show(this,opts.showType,opts.showSpeed,opts.timeout);
return false;
},onBeforeClose:function(){
hide(this,opts.showType,opts.showSpeed);
return false;
}},{title:"",width:250,height:100,showType:"slide",showSpeed:600,msg:"",timeout:4000},_270);
opts.style.zIndex=$.fn.window.defaults.zIndex++;
var win=$("<div class=\"messager-body\"></div>").html(opts.msg).appendTo("body");
win.window(opts);
win.window("window").css(opts.style);
win.window("open");
return win;
};
function _271(_272,_273,_274){
var win=$("<div class=\"messager-body\"></div>").appendTo("body");
win.append(_273);
if(_274){
var tb=$("<div class=\"messager-button\"></div>").appendTo(win);
for(var _275 in _274){
$("<a></a>").attr("href","javascript:void(0)").text(_275).css("margin-left",10).bind("click",eval(_274[_275])).appendTo(tb).linkbutton();
}
}
win.window({title:_272,noheader:(_272?false:true),width:300,height:"auto",modal:true,collapsible:false,minimizable:false,maximizable:false,resizable:false,onClose:function(){
setTimeout(function(){
win.window("destroy");
},100);
}});
win.window("window").addClass("messager-window");
win.children("div.messager-button").children("a:first").focus();
return win;
};
$.messager={show:function(_276){
return _26f(_276);
},alert:function(_277,msg,icon,fn){
var _278="<div>"+msg+"</div>";
switch(icon){
case "error":
_278="<div class=\"messager-icon messager-error\"></div>"+_278;
break;
case "info":
_278="<div class=\"messager-icon messager-info\"></div>"+_278;
break;
case "question":
_278="<div class=\"messager-icon messager-question\"></div>"+_278;
break;
case "warning":
_278="<div class=\"messager-icon messager-warning\"></div>"+_278;
break;
}
_278+="<div style=\"clear:both;\"/>";
var _279={};
_279[$.messager.defaults.ok]=function(){
win.window("close");
if(fn){
fn();
return false;
}
};
var win=_271(_277,_278,_279);
return win;
},confirm:function(_27a,msg,fn){
var _27b="<div class=\"messager-icon messager-question\"></div>"+"<div>"+msg+"</div>"+"<div style=\"clear:both;\"/>";
var _27c={};
_27c[$.messager.defaults.ok]=function(){
win.window("close");
if(fn){
fn(true);
return false;
}
};
_27c[$.messager.defaults.cancel]=function(){
win.window("close");
if(fn){
fn(false);
return false;
}
};
var win=_271(_27a,_27b,_27c);
return win;
},prompt:function(_27d,msg,fn){
var _27e="<div class=\"messager-icon messager-question\"></div>"+"<div>"+msg+"</div>"+"<br/>"+"<div style=\"clear:both;\"/>"+"<div><input class=\"messager-input\" type=\"text\"/></div>";
var _27f={};
_27f[$.messager.defaults.ok]=function(){
win.window("close");
if(fn){
fn($(".messager-input",win).val());
return false;
}
};
_27f[$.messager.defaults.cancel]=function(){
win.window("close");
if(fn){
fn();
return false;
}
};
var win=_271(_27d,_27e,_27f);
win.children("input.messager-input").focus();
return win;
},progress:function(_280){
var _281={bar:function(){
return $("body>div.messager-window").find("div.messager-p-bar");
},close:function(){
var win=$("body>div.messager-window>div.messager-body:has(div.messager-progress)");
if(win.length){
win.window("close");
}
}};
if(typeof _280=="string"){
var _282=_281[_280];
return _282();
}
var opts=$.extend({title:"",msg:"",text:undefined,interval:300},_280||{});
var _283="<div class=\"messager-progress\"><div class=\"messager-p-msg\"></div><div class=\"messager-p-bar\"></div></div>";
var win=_271(opts.title,_283,null);
win.find("div.messager-p-msg").html(opts.msg);
var bar=win.find("div.messager-p-bar");
bar.progressbar({text:opts.text});
win.window({closable:false,onClose:function(){
if(this.timer){
clearInterval(this.timer);
}
$(this).window("destroy");
}});
if(opts.interval){
win[0].timer=setInterval(function(){
var v=bar.progressbar("getValue");
v+=10;
if(v>100){
v=0;
}
bar.progressbar("setValue",v);
},opts.interval);
}
return win;
}};
$.messager.defaults={ok:"Ok",cancel:"Cancel"};
})(jQuery);
(function($){
function _284(_285){
var _286=$.data(_285,"accordion");
var opts=_286.options;
var _287=_286.panels;
var cc=$(_285);
opts.fit?$.extend(opts,cc._fit()):cc._fit(false);
if(!isNaN(opts.width)){
cc._outerWidth(opts.width);
}else{
cc.css("width","");
}
var _288=0;
var _289="auto";
var _28a=cc.find(">div.panel>div.accordion-header");
if(_28a.length){
_288=$(_28a[0]).css("height","")._outerHeight();
}
if(!isNaN(opts.height)){
cc._outerHeight(opts.height);
_289=cc.height()-_288*_28a.length;
}else{
cc.css("height","");
}
_28b(true,_289-_28b(false)+1);
function _28b(_28c,_28d){
var _28e=0;
for(var i=0;i<_287.length;i++){
var p=_287[i];
var h=p.panel("header")._outerHeight(_288);
if(p.panel("options").collapsible==_28c){
var _28f=isNaN(_28d)?undefined:(_28d+_288*h.length);
p.panel("resize",{width:cc.width(),height:(_28c?_28f:undefined)});
_28e+=p.panel("panel").outerHeight()-_288;
}
}
return _28e;
};
};
function _290(_291,_292,_293,all){
var _294=$.data(_291,"accordion").panels;
var pp=[];
for(var i=0;i<_294.length;i++){
var p=_294[i];
if(_292){
if(p.panel("options")[_292]==_293){
pp.push(p);
}
}else{
if(p[0]==$(_293)[0]){
return i;
}
}
}
if(_292){
return all?pp:(pp.length?pp[0]:null);
}else{
return -1;
}
};
function _295(_296){
return _290(_296,"collapsed",false,true);
};
function _297(_298){
var pp=_295(_298);
return pp.length?pp[0]:null;
};
function _299(_29a,_29b){
return _290(_29a,null,_29b);
};
function _29c(_29d,_29e){
var _29f=$.data(_29d,"accordion").panels;
if(typeof _29e=="number"){
if(_29e<0||_29e>=_29f.length){
return null;
}else{
return _29f[_29e];
}
}
return _290(_29d,"title",_29e);
};
function _2a0(_2a1){
var opts=$.data(_2a1,"accordion").options;
var cc=$(_2a1);
if(opts.border){
cc.removeClass("accordion-noborder");
}else{
cc.addClass("accordion-noborder");
}
};
function init(_2a2){
var _2a3=$.data(_2a2,"accordion");
var cc=$(_2a2);
cc.addClass("accordion");
_2a3.panels=[];
cc.children("div").each(function(){
var opts=$.extend({},$.parser.parseOptions(this),{selected:($(this).attr("selected")?true:undefined)});
var pp=$(this);
_2a3.panels.push(pp);
_2a5(_2a2,pp,opts);
});
cc.bind("_resize",function(e,_2a4){
var opts=$.data(_2a2,"accordion").options;
if(opts.fit==true||_2a4){
_284(_2a2);
}
return false;
});
};
function _2a5(_2a6,pp,_2a7){
var opts=$.data(_2a6,"accordion").options;
pp.panel($.extend({},{collapsible:true,minimizable:false,maximizable:false,closable:false,doSize:false,collapsed:true,headerCls:"accordion-header",bodyCls:"accordion-body"},_2a7,{onBeforeExpand:function(){
if(_2a7.onBeforeExpand){
if(_2a7.onBeforeExpand.call(this)==false){
return false;
}
}
if(!opts.multiple){
var all=$.grep(_295(_2a6),function(p){
return p.panel("options").collapsible;
});
for(var i=0;i<all.length;i++){
_2b0(_2a6,_299(_2a6,all[i]));
}
}
var _2a8=$(this).panel("header");
_2a8.addClass("accordion-header-selected");
_2a8.find(".accordion-collapse").removeClass("accordion-expand");
},onExpand:function(){
if(_2a7.onExpand){
_2a7.onExpand.call(this);
}
opts.onSelect.call(_2a6,$(this).panel("options").title,_299(_2a6,this));
},onBeforeCollapse:function(){
if(_2a7.onBeforeCollapse){
if(_2a7.onBeforeCollapse.call(this)==false){
return false;
}
}
var _2a9=$(this).panel("header");
_2a9.removeClass("accordion-header-selected");
_2a9.find(".accordion-collapse").addClass("accordion-expand");
},onCollapse:function(){
if(_2a7.onCollapse){
_2a7.onCollapse.call(this);
}
opts.onUnselect.call(_2a6,$(this).panel("options").title,_299(_2a6,this));
}}));
var _2aa=pp.panel("header");
var tool=_2aa.children("div.panel-tool");
tool.children("a.panel-tool-collapse").hide();
var t=$("<a href=\"javascript:void(0)\"></a>").addClass("accordion-collapse accordion-expand").appendTo(tool);
t.bind("click",function(){
var _2ab=_299(_2a6,pp);
if(pp.panel("options").collapsed){
_2ac(_2a6,_2ab);
}else{
_2b0(_2a6,_2ab);
}
return false;
});
pp.panel("options").collapsible?t.show():t.hide();
_2aa.click(function(){
$(this).find("a.accordion-collapse:visible").triggerHandler("click");
return false;
});
};
function _2ac(_2ad,_2ae){
var p=_29c(_2ad,_2ae);
if(!p){
return;
}
_2af(_2ad);
var opts=$.data(_2ad,"accordion").options;
p.panel("expand",opts.animate);
};
function _2b0(_2b1,_2b2){
var p=_29c(_2b1,_2b2);
if(!p){
return;
}
_2af(_2b1);
var opts=$.data(_2b1,"accordion").options;
p.panel("collapse",opts.animate);
};
function _2b3(_2b4){
var opts=$.data(_2b4,"accordion").options;
var p=_290(_2b4,"selected",true);
if(p){
_2b5(_299(_2b4,p));
}else{
_2b5(opts.selected);
}
function _2b5(_2b6){
var _2b7=opts.animate;
opts.animate=false;
_2ac(_2b4,_2b6);
opts.animate=_2b7;
};
};
function _2af(_2b8){
var _2b9=$.data(_2b8,"accordion").panels;
for(var i=0;i<_2b9.length;i++){
_2b9[i].stop(true,true);
}
};
function add(_2ba,_2bb){
var _2bc=$.data(_2ba,"accordion");
var opts=_2bc.options;
var _2bd=_2bc.panels;
if(_2bb.selected==undefined){
_2bb.selected=true;
}
_2af(_2ba);
var pp=$("<div></div>").appendTo(_2ba);
_2bd.push(pp);
_2a5(_2ba,pp,_2bb);
_284(_2ba);
opts.onAdd.call(_2ba,_2bb.title,_2bd.length-1);
if(_2bb.selected){
_2ac(_2ba,_2bd.length-1);
}
};
function _2be(_2bf,_2c0){
var _2c1=$.data(_2bf,"accordion");
var opts=_2c1.options;
var _2c2=_2c1.panels;
_2af(_2bf);
var _2c3=_29c(_2bf,_2c0);
var _2c4=_2c3.panel("options").title;
var _2c5=_299(_2bf,_2c3);
if(!_2c3){
return;
}
if(opts.onBeforeRemove.call(_2bf,_2c4,_2c5)==false){
return;
}
_2c2.splice(_2c5,1);
_2c3.panel("destroy");
if(_2c2.length){
_284(_2bf);
var curr=_297(_2bf);
if(!curr){
_2ac(_2bf,0);
}
}
opts.onRemove.call(_2bf,_2c4,_2c5);
};
$.fn.accordion=function(_2c6,_2c7){
if(typeof _2c6=="string"){
return $.fn.accordion.methods[_2c6](this,_2c7);
}
_2c6=_2c6||{};
return this.each(function(){
var _2c8=$.data(this,"accordion");
if(_2c8){
$.extend(_2c8.options,_2c6);
}else{
$.data(this,"accordion",{options:$.extend({},$.fn.accordion.defaults,$.fn.accordion.parseOptions(this),_2c6),accordion:$(this).addClass("accordion"),panels:[]});
init(this);
}
_2a0(this);
_284(this);
_2b3(this);
});
};
$.fn.accordion.methods={options:function(jq){
return $.data(jq[0],"accordion").options;
},panels:function(jq){
return $.data(jq[0],"accordion").panels;
},resize:function(jq){
return jq.each(function(){
_284(this);
});
},getSelections:function(jq){
return _295(jq[0]);
},getSelected:function(jq){
return _297(jq[0]);
},getPanel:function(jq,_2c9){
return _29c(jq[0],_2c9);
},getPanelIndex:function(jq,_2ca){
return _299(jq[0],_2ca);
},select:function(jq,_2cb){
return jq.each(function(){
_2ac(this,_2cb);
});
},unselect:function(jq,_2cc){
return jq.each(function(){
_2b0(this,_2cc);
});
},add:function(jq,_2cd){
return jq.each(function(){
add(this,_2cd);
});
},remove:function(jq,_2ce){
return jq.each(function(){
_2be(this,_2ce);
});
}};
$.fn.accordion.parseOptions=function(_2cf){
var t=$(_2cf);
return $.extend({},$.parser.parseOptions(_2cf,["width","height",{fit:"boolean",border:"boolean",animate:"boolean",multiple:"boolean",selected:"number"}]));
};
$.fn.accordion.defaults={width:"auto",height:"auto",fit:false,border:true,animate:true,multiple:false,selected:0,onSelect:function(_2d0,_2d1){
},onUnselect:function(_2d2,_2d3){
},onAdd:function(_2d4,_2d5){
},onBeforeRemove:function(_2d6,_2d7){
},onRemove:function(_2d8,_2d9){
}};
})(jQuery);
(function($){
function _2da(_2db){
var opts=$.data(_2db,"tabs").options;
if(opts.tabPosition=="left"||opts.tabPosition=="right"||!opts.showHeader){
return;
}
var _2dc=$(_2db).children("div.tabs-header");
var tool=_2dc.children("div.tabs-tool");
var _2dd=_2dc.children("div.tabs-scroller-left");
var _2de=_2dc.children("div.tabs-scroller-right");
var wrap=_2dc.children("div.tabs-wrap");
var _2df=_2dc.outerHeight();
if(opts.plain){
_2df-=_2df-_2dc.height();
}
tool._outerHeight(_2df);
var _2e0=0;
$("ul.tabs li",_2dc).each(function(){
_2e0+=$(this).outerWidth(true);
});
var _2e1=_2dc.width()-tool._outerWidth();
if(_2e0>_2e1){
_2dd.add(_2de).show()._outerHeight(_2df);
if(opts.toolPosition=="left"){
tool.css({left:_2dd.outerWidth(),right:""});
wrap.css({marginLeft:_2dd.outerWidth()+tool._outerWidth(),marginRight:_2de._outerWidth(),width:_2e1-_2dd.outerWidth()-_2de.outerWidth()});
}else{
tool.css({left:"",right:_2de.outerWidth()});
wrap.css({marginLeft:_2dd.outerWidth(),marginRight:_2de.outerWidth()+tool._outerWidth(),width:_2e1-_2dd.outerWidth()-_2de.outerWidth()});
}
}else{
_2dd.add(_2de).hide();
if(opts.toolPosition=="left"){
tool.css({left:0,right:""});
wrap.css({marginLeft:tool._outerWidth(),marginRight:0,width:_2e1});
}else{
tool.css({left:"",right:0});
wrap.css({marginLeft:0,marginRight:tool._outerWidth(),width:_2e1});
}
}
};
function _2e2(_2e3){
var opts=$.data(_2e3,"tabs").options;
var _2e4=$(_2e3).children("div.tabs-header");
if(opts.tools){
if(typeof opts.tools=="string"){
$(opts.tools).addClass("tabs-tool").appendTo(_2e4);
$(opts.tools).show();
}else{
_2e4.children("div.tabs-tool").remove();
var _2e5=$("<div class=\"tabs-tool\"><table cellspacing=\"0\" cellpadding=\"0\" style=\"height:100%\"><tr></tr></table></div>").appendTo(_2e4);
var tr=_2e5.find("tr");
for(var i=0;i<opts.tools.length;i++){
var td=$("<td></td>").appendTo(tr);
var tool=$("<a href=\"javascript:void(0);\"></a>").appendTo(td);
tool[0].onclick=eval(opts.tools[i].handler||function(){
});
tool.linkbutton($.extend({},opts.tools[i],{plain:true}));
}
}
}else{
_2e4.children("div.tabs-tool").remove();
}
};
function _2e6(_2e7){
var _2e8=$.data(_2e7,"tabs");
var opts=_2e8.options;
var cc=$(_2e7);
opts.fit?$.extend(opts,cc._fit()):cc._fit(false);
cc.width(opts.width).height(opts.height);
var _2e9=$(_2e7).children("div.tabs-header");
var _2ea=$(_2e7).children("div.tabs-panels");
var wrap=_2e9.find("div.tabs-wrap");
var ul=wrap.find(".tabs");
for(var i=0;i<_2e8.tabs.length;i++){
var _2eb=_2e8.tabs[i].panel("options");
var p_t=_2eb.tab.find("a.tabs-inner");
var _2ec=parseInt(_2eb.tabWidth||opts.tabWidth)||undefined;
if(_2ec){
p_t._outerWidth(_2ec);
}else{
p_t.css("width","");
}
p_t._outerHeight(opts.tabHeight);
p_t.css("lineHeight",p_t.height()+"px");
}
if(opts.tabPosition=="left"||opts.tabPosition=="right"){
_2e9._outerWidth(opts.showHeader?opts.headerWidth:0);
_2ea._outerWidth(cc.width()-_2e9.outerWidth());
_2e9.add(_2ea)._outerHeight(opts.height);
wrap._outerWidth(_2e9.width());
ul._outerWidth(wrap.width()).css("height","");
}else{
var lrt=_2e9.children("div.tabs-scroller-left,div.tabs-scroller-right,div.tabs-tool");
_2e9._outerWidth(opts.width).css("height","");
if(opts.showHeader){
_2e9.css("background-color","");
wrap.css("height","");
lrt.show();
}else{
_2e9.css("background-color","transparent");
_2e9._outerHeight(0);
wrap._outerHeight(0);
lrt.hide();
}
ul._outerHeight(opts.tabHeight).css("width","");
_2da(_2e7);
var _2ed=opts.height;
if(!isNaN(_2ed)){
_2ea._outerHeight(_2ed-_2e9.outerHeight());
}else{
_2ea.height("auto");
}
var _2ec=opts.width;
if(!isNaN(_2ec)){
_2ea._outerWidth(_2ec);
}else{
_2ea.width("auto");
}
}
};
function _2ee(_2ef){
var opts=$.data(_2ef,"tabs").options;
var tab=_2f0(_2ef);
if(tab){
var _2f1=$(_2ef).children("div.tabs-panels");
var _2f2=opts.width=="auto"?"auto":_2f1.width();
var _2f3=opts.height=="auto"?"auto":_2f1.height();
tab.panel("resize",{width:_2f2,height:_2f3});
}
};
function _2f4(_2f5){
var tabs=$.data(_2f5,"tabs").tabs;
var cc=$(_2f5);
cc.addClass("tabs-container");
var pp=$("<div class=\"tabs-panels\"></div>").insertBefore(cc);
cc.children("div").each(function(){
pp[0].appendChild(this);
});
cc[0].appendChild(pp[0]);
$("<div class=\"tabs-header\">"+"<div class=\"tabs-scroller-left\"></div>"+"<div class=\"tabs-scroller-right\"></div>"+"<div class=\"tabs-wrap\">"+"<ul class=\"tabs\"></ul>"+"</div>"+"</div>").prependTo(_2f5);
cc.children("div.tabs-panels").children("div").each(function(i){
var opts=$.extend({},$.parser.parseOptions(this),{selected:($(this).attr("selected")?true:undefined)});
var pp=$(this);
tabs.push(pp);
_302(_2f5,pp,opts);
});
cc.children("div.tabs-header").find(".tabs-scroller-left, .tabs-scroller-right").hover(function(){
$(this).addClass("tabs-scroller-over");
},function(){
$(this).removeClass("tabs-scroller-over");
});
cc.bind("_resize",function(e,_2f6){
var opts=$.data(_2f5,"tabs").options;
if(opts.fit==true||_2f6){
_2e6(_2f5);
_2ee(_2f5);
}
return false;
});
};
function _2f7(_2f8){
var _2f9=$.data(_2f8,"tabs");
var opts=_2f9.options;
$(_2f8).children("div.tabs-header").unbind().bind("click",function(e){
if($(e.target).hasClass("tabs-scroller-left")){
$(_2f8).tabs("scrollBy",-opts.scrollIncrement);
}else{
if($(e.target).hasClass("tabs-scroller-right")){
$(_2f8).tabs("scrollBy",opts.scrollIncrement);
}else{
var li=$(e.target).closest("li");
if(li.hasClass("tabs-disabled")){
return;
}
var a=$(e.target).closest("a.tabs-close");
if(a.length){
_313(_2f8,_2fa(li));
}else{
if(li.length){
var _2fb=_2fa(li);
var _2fc=_2f9.tabs[_2fb].panel("options");
if(_2fc.collapsible){
_2fc.closed?_309(_2f8,_2fb):_32a(_2f8,_2fb);
}else{
_309(_2f8,_2fb);
}
}
}
}
}
}).bind("contextmenu",function(e){
var li=$(e.target).closest("li");
if(li.hasClass("tabs-disabled")){
return;
}
if(li.length){
opts.onContextMenu.call(_2f8,e,li.find("span.tabs-title").html(),_2fa(li));
}
});
function _2fa(li){
var _2fd=0;
li.parent().children("li").each(function(i){
if(li[0]==this){
_2fd=i;
return false;
}
});
return _2fd;
};
};
function _2fe(_2ff){
var opts=$.data(_2ff,"tabs").options;
var _300=$(_2ff).children("div.tabs-header");
var _301=$(_2ff).children("div.tabs-panels");
_300.removeClass("tabs-header-top tabs-header-bottom tabs-header-left tabs-header-right");
_301.removeClass("tabs-panels-top tabs-panels-bottom tabs-panels-left tabs-panels-right");
if(opts.tabPosition=="top"){
_300.insertBefore(_301);
}else{
if(opts.tabPosition=="bottom"){
_300.insertAfter(_301);
_300.addClass("tabs-header-bottom");
_301.addClass("tabs-panels-top");
}else{
if(opts.tabPosition=="left"){
_300.addClass("tabs-header-left");
_301.addClass("tabs-panels-right");
}else{
if(opts.tabPosition=="right"){
_300.addClass("tabs-header-right");
_301.addClass("tabs-panels-left");
}
}
}
}
if(opts.plain==true){
_300.addClass("tabs-header-plain");
}else{
_300.removeClass("tabs-header-plain");
}
if(opts.border==true){
_300.removeClass("tabs-header-noborder");
_301.removeClass("tabs-panels-noborder");
}else{
_300.addClass("tabs-header-noborder");
_301.addClass("tabs-panels-noborder");
}
};
function _302(_303,pp,_304){
var _305=$.data(_303,"tabs");
_304=_304||{};
pp.panel($.extend({},_304,{border:false,noheader:true,closed:true,doSize:false,iconCls:(_304.icon?_304.icon:undefined),onLoad:function(){
if(_304.onLoad){
_304.onLoad.call(this,arguments);
}
_305.options.onLoad.call(_303,$(this));
}}));
var opts=pp.panel("options");
var tabs=$(_303).children("div.tabs-header").find("ul.tabs");
opts.tab=$("<li></li>").appendTo(tabs);
opts.tab.append("<a href=\"javascript:void(0)\" class=\"tabs-inner\">"+"<span class=\"tabs-title\"></span>"+"<span class=\"tabs-icon\"></span>"+"</a>");
$(_303).tabs("update",{tab:pp,options:opts});
};
function _306(_307,_308){
var opts=$.data(_307,"tabs").options;
var tabs=$.data(_307,"tabs").tabs;
if(_308.selected==undefined){
_308.selected=true;
}
var pp=$("<div></div>").appendTo($(_307).children("div.tabs-panels"));
tabs.push(pp);
_302(_307,pp,_308);
opts.onAdd.call(_307,_308.title,tabs.length-1);
_2e6(_307);
if(_308.selected){
_309(_307,tabs.length-1);
}
};
function _30a(_30b,_30c){
var _30d=$.data(_30b,"tabs").selectHis;
var pp=_30c.tab;
var _30e=pp.panel("options").title;
pp.panel($.extend({},_30c.options,{iconCls:(_30c.options.icon?_30c.options.icon:undefined)}));
var opts=pp.panel("options");
var tab=opts.tab;
var _30f=tab.find("span.tabs-title");
var _310=tab.find("span.tabs-icon");
_30f.html(opts.title);
_310.attr("class","tabs-icon");
tab.find("a.tabs-close").remove();
if(opts.closable){
_30f.addClass("tabs-closable");
$("<a href=\"javascript:void(0)\" class=\"tabs-close\"></a>").appendTo(tab);
}else{
_30f.removeClass("tabs-closable");
}
if(opts.iconCls){
_30f.addClass("tabs-with-icon");
_310.addClass(opts.iconCls);
}else{
_30f.removeClass("tabs-with-icon");
}
if(_30e!=opts.title){
for(var i=0;i<_30d.length;i++){
if(_30d[i]==_30e){
_30d[i]=opts.title;
}
}
}
tab.find("span.tabs-p-tool").remove();
if(opts.tools){
var _311=$("<span class=\"tabs-p-tool\"></span>").insertAfter(tab.find("a.tabs-inner"));
if($.isArray(opts.tools)){
for(var i=0;i<opts.tools.length;i++){
var t=$("<a href=\"javascript:void(0)\"></a>").appendTo(_311);
t.addClass(opts.tools[i].iconCls);
if(opts.tools[i].handler){
t.bind("click",{handler:opts.tools[i].handler},function(e){
if($(this).parents("li").hasClass("tabs-disabled")){
return;
}
e.data.handler.call(this);
});
}
}
}else{
$(opts.tools).children().appendTo(_311);
}
var pr=_311.children().length*12;
if(opts.closable){
pr+=8;
}else{
pr-=3;
_311.css("right","5px");
}
_30f.css("padding-right",pr+"px");
}
_2e6(_30b);
$.data(_30b,"tabs").options.onUpdate.call(_30b,opts.title,_312(_30b,pp));
};
function _313(_314,_315){
var opts=$.data(_314,"tabs").options;
var tabs=$.data(_314,"tabs").tabs;
var _316=$.data(_314,"tabs").selectHis;
if(!_317(_314,_315)){
return;
}
var tab=_318(_314,_315);
var _319=tab.panel("options").title;
var _31a=_312(_314,tab);
if(opts.onBeforeClose.call(_314,_319,_31a)==false){
return;
}
var tab=_318(_314,_315,true);
tab.panel("options").tab.remove();
tab.panel("destroy");
opts.onClose.call(_314,_319,_31a);
_2e6(_314);
for(var i=0;i<_316.length;i++){
if(_316[i]==_319){
_316.splice(i,1);
i--;
}
}
var _31b=_316.pop();
if(_31b){
_309(_314,_31b);
}else{
if(tabs.length){
_309(_314,0);
}
}
};
function _318(_31c,_31d,_31e){
var tabs=$.data(_31c,"tabs").tabs;
if(typeof _31d=="number"){
if(_31d<0||_31d>=tabs.length){
return null;
}else{
var tab=tabs[_31d];
if(_31e){
tabs.splice(_31d,1);
}
return tab;
}
}
for(var i=0;i<tabs.length;i++){
var tab=tabs[i];
if(tab.panel("options").title==_31d){
if(_31e){
tabs.splice(i,1);
}
return tab;
}
}
return null;
};
function _312(_31f,tab){
var tabs=$.data(_31f,"tabs").tabs;
for(var i=0;i<tabs.length;i++){
if(tabs[i][0]==$(tab)[0]){
return i;
}
}
return -1;
};
function _2f0(_320){
var tabs=$.data(_320,"tabs").tabs;
for(var i=0;i<tabs.length;i++){
var tab=tabs[i];
if(tab.panel("options").closed==false){
return tab;
}
}
return null;
};
function _321(_322){
var _323=$.data(_322,"tabs");
var tabs=_323.tabs;
for(var i=0;i<tabs.length;i++){
if(tabs[i].panel("options").selected){
_309(_322,i);
return;
}
}
_309(_322,_323.options.selected);
};
function _309(_324,_325){
var _326=$.data(_324,"tabs");
var opts=_326.options;
var tabs=_326.tabs;
var _327=_326.selectHis;
if(tabs.length==0){
return;
}
var _328=_318(_324,_325);
if(!_328){
return;
}
var _329=_2f0(_324);
if(_329){
if(_328[0]==_329[0]){
return;
}
_32a(_324,_312(_324,_329));
if(!_329.panel("options").closed){
return;
}
}
_328.panel("open");
var _32b=_328.panel("options").title;
_327.push(_32b);
var tab=_328.panel("options").tab;
tab.addClass("tabs-selected");
var wrap=$(_324).find(">div.tabs-header>div.tabs-wrap");
var left=tab.position().left;
var _32c=left+tab.outerWidth();
if(left<0||_32c>wrap.width()){
var _32d=left-(wrap.width()-tab.width())/2;
$(_324).tabs("scrollBy",_32d);
}else{
$(_324).tabs("scrollBy",0);
}
_2ee(_324);
opts.onSelect.call(_324,_32b,_312(_324,_328));
};
function _32a(_32e,_32f){
var _330=$.data(_32e,"tabs");
var p=_318(_32e,_32f);
if(p){
var opts=p.panel("options");
if(!opts.closed){
p.panel("close");
if(opts.closed){
opts.tab.removeClass("tabs-selected");
_330.options.onUnselect.call(_32e,opts.title,_312(_32e,p));
}
}
}
};
function _317(_331,_332){
return _318(_331,_332)!=null;
};
function _333(_334,_335){
var opts=$.data(_334,"tabs").options;
opts.showHeader=_335;
$(_334).tabs("resize");
};
$.fn.tabs=function(_336,_337){
if(typeof _336=="string"){
return $.fn.tabs.methods[_336](this,_337);
}
_336=_336||{};
return this.each(function(){
var _338=$.data(this,"tabs");
var opts;
if(_338){
opts=$.extend(_338.options,_336);
_338.options=opts;
}else{
$.data(this,"tabs",{options:$.extend({},$.fn.tabs.defaults,$.fn.tabs.parseOptions(this),_336),tabs:[],selectHis:[]});
_2f4(this);
}
_2e2(this);
_2fe(this);
_2e6(this);
_2f7(this);
_321(this);
});
};
$.fn.tabs.methods={options:function(jq){
var cc=jq[0];
var opts=$.data(cc,"tabs").options;
var s=_2f0(cc);
opts.selected=s?_312(cc,s):-1;
return opts;
},tabs:function(jq){
return $.data(jq[0],"tabs").tabs;
},resize:function(jq){
return jq.each(function(){
_2e6(this);
_2ee(this);
});
},add:function(jq,_339){
return jq.each(function(){
_306(this,_339);
});
},close:function(jq,_33a){
return jq.each(function(){
_313(this,_33a);
});
},getTab:function(jq,_33b){
return _318(jq[0],_33b);
},getTabIndex:function(jq,tab){
return _312(jq[0],tab);
},getSelected:function(jq){
return _2f0(jq[0]);
},select:function(jq,_33c){
return jq.each(function(){
_309(this,_33c);
});
},unselect:function(jq,_33d){
return jq.each(function(){
_32a(this,_33d);
});
},exists:function(jq,_33e){
return _317(jq[0],_33e);
},update:function(jq,_33f){
return jq.each(function(){
_30a(this,_33f);
});
},enableTab:function(jq,_340){
return jq.each(function(){
$(this).tabs("getTab",_340).panel("options").tab.removeClass("tabs-disabled");
});
},disableTab:function(jq,_341){
return jq.each(function(){
$(this).tabs("getTab",_341).panel("options").tab.addClass("tabs-disabled");
});
},showHeader:function(jq){
return jq.each(function(){
_333(this,true);
});
},hideHeader:function(jq){
return jq.each(function(){
_333(this,false);
});
},scrollBy:function(jq,_342){
return jq.each(function(){
var opts=$(this).tabs("options");
var wrap=$(this).find(">div.tabs-header>div.tabs-wrap");
var pos=Math.min(wrap._scrollLeft()+_342,_343());
wrap.animate({scrollLeft:pos},opts.scrollDuration);
function _343(){
var w=0;
var ul=wrap.children("ul");
ul.children("li").each(function(){
w+=$(this).outerWidth(true);
});
return w-wrap.width()+(ul.outerWidth()-ul.width());
};
});
}};
$.fn.tabs.parseOptions=function(_344){
return $.extend({},$.parser.parseOptions(_344,["width","height","tools","toolPosition","tabPosition",{fit:"boolean",border:"boolean",plain:"boolean",headerWidth:"number",tabWidth:"number",tabHeight:"number",selected:"number",showHeader:"boolean"}]));
};
$.fn.tabs.defaults={width:"auto",height:"auto",headerWidth:150,tabWidth:"auto",tabHeight:27,selected:0,showHeader:true,plain:false,fit:false,border:true,tools:null,toolPosition:"right",tabPosition:"top",scrollIncrement:100,scrollDuration:400,onLoad:function(_345){
},onSelect:function(_346,_347){
},onUnselect:function(_348,_349){
},onBeforeClose:function(_34a,_34b){
},onClose:function(_34c,_34d){
},onAdd:function(_34e,_34f){
},onUpdate:function(_350,_351){
},onContextMenu:function(e,_352,_353){
}};
})(jQuery);
(function($){
var _354=false;
function _355(_356){
var _357=$.data(_356,"layout");
var opts=_357.options;
var _358=_357.panels;
var cc=$(_356);
if(_356.tagName=="BODY"){
cc._fit();
}else{
opts.fit?cc.css(cc._fit()):cc._fit(false);
}
var cpos={top:0,left:0,width:cc.width(),height:cc.height()};
_359(_35a(_358.expandNorth)?_358.expandNorth:_358.north,"n");
_359(_35a(_358.expandSouth)?_358.expandSouth:_358.south,"s");
_35b(_35a(_358.expandEast)?_358.expandEast:_358.east,"e");
_35b(_35a(_358.expandWest)?_358.expandWest:_358.west,"w");
_358.center.panel("resize",cpos);
function _35c(pp){
var opts=pp.panel("options");
return Math.min(Math.max(opts.height,opts.minHeight),opts.maxHeight);
};
function _35d(pp){
var opts=pp.panel("options");
return Math.min(Math.max(opts.width,opts.minWidth),opts.maxWidth);
};
function _359(pp,type){
if(!pp.length){
return;
}
var opts=pp.panel("options");
var _35e=_35c(pp);
pp.panel("resize",{width:cc.width(),height:_35e,left:0,top:(type=="n"?0:cc.height()-_35e)});
cpos.height-=_35e;
if(type=="n"){
cpos.top+=_35e;
if(!opts.split&&opts.border){
cpos.top--;
}
}
if(!opts.split&&opts.border){
cpos.height++;
}
};
function _35b(pp,type){
if(!pp.length){
return;
}
var opts=pp.panel("options");
var _35f=_35d(pp);
pp.panel("resize",{width:_35f,height:cpos.height,left:(type=="e"?cc.width()-_35f:0),top:cpos.top});
cpos.width-=_35f;
if(type=="w"){
cpos.left+=_35f;
if(!opts.split&&opts.border){
cpos.left--;
}
}
if(!opts.split&&opts.border){
cpos.width++;
}
};
};
function init(_360){
var cc=$(_360);
cc.addClass("layout");
function _361(cc){
cc.children("div").each(function(){
var opts=$.fn.layout.parsePanelOptions(this);
if("north,south,east,west,center".indexOf(opts.region)>=0){
_363(_360,opts,this);
}
});
};
cc.children("form").length?_361(cc.children("form")):_361(cc);
cc.append("<div class=\"layout-split-proxy-h\"></div><div class=\"layout-split-proxy-v\"></div>");
cc.bind("_resize",function(e,_362){
var opts=$.data(_360,"layout").options;
if(opts.fit==true||_362){
_355(_360);
}
return false;
});
};
function _363(_364,_365,el){
_365.region=_365.region||"center";
var _366=$.data(_364,"layout").panels;
var cc=$(_364);
var dir=_365.region;
if(_366[dir].length){
return;
}
var pp=$(el);
if(!pp.length){
pp=$("<div></div>").appendTo(cc);
}
var _367=$.extend({},$.fn.layout.paneldefaults,{width:(pp.length?parseInt(pp[0].style.width)||pp.outerWidth():"auto"),height:(pp.length?parseInt(pp[0].style.height)||pp.outerHeight():"auto"),doSize:false,collapsible:true,cls:("layout-panel layout-panel-"+dir),bodyCls:"layout-body",onOpen:function(){
var tool=$(this).panel("header").children("div.panel-tool");
tool.children("a.panel-tool-collapse").hide();
var _368={north:"up",south:"down",east:"right",west:"left"};
if(!_368[dir]){
return;
}
var _369="layout-button-"+_368[dir];
var t=tool.children("a."+_369);
if(!t.length){
t=$("<a href=\"javascript:void(0)\"></a>").addClass(_369).appendTo(tool);
t.bind("click",{dir:dir},function(e){
_375(_364,e.data.dir);
return false;
});
}
$(this).panel("options").collapsible?t.show():t.hide();
}},_365);
pp.panel(_367);
_366[dir]=pp;
if(pp.panel("options").split){
var _36a=pp.panel("panel");
_36a.addClass("layout-split-"+dir);
var _36b="";
if(dir=="north"){
_36b="s";
}
if(dir=="south"){
_36b="n";
}
if(dir=="east"){
_36b="w";
}
if(dir=="west"){
_36b="e";
}
_36a.resizable($.extend({},{handles:_36b,onStartResize:function(e){
_354=true;
if(dir=="north"||dir=="south"){
var _36c=$(">div.layout-split-proxy-v",_364);
}else{
var _36c=$(">div.layout-split-proxy-h",_364);
}
var top=0,left=0,_36d=0,_36e=0;
var pos={display:"block"};
if(dir=="north"){
pos.top=parseInt(_36a.css("top"))+_36a.outerHeight()-_36c.height();
pos.left=parseInt(_36a.css("left"));
pos.width=_36a.outerWidth();
pos.height=_36c.height();
}else{
if(dir=="south"){
pos.top=parseInt(_36a.css("top"));
pos.left=parseInt(_36a.css("left"));
pos.width=_36a.outerWidth();
pos.height=_36c.height();
}else{
if(dir=="east"){
pos.top=parseInt(_36a.css("top"))||0;
pos.left=parseInt(_36a.css("left"))||0;
pos.width=_36c.width();
pos.height=_36a.outerHeight();
}else{
if(dir=="west"){
pos.top=parseInt(_36a.css("top"))||0;
pos.left=_36a.outerWidth()-_36c.width();
pos.width=_36c.width();
pos.height=_36a.outerHeight();
}
}
}
}
_36c.css(pos);
$("<div class=\"layout-mask\"></div>").css({left:0,top:0,width:cc.width(),height:cc.height()}).appendTo(cc);
},onResize:function(e){
if(dir=="north"||dir=="south"){
var _36f=$(">div.layout-split-proxy-v",_364);
_36f.css("top",e.pageY-$(_364).offset().top-_36f.height()/2);
}else{
var _36f=$(">div.layout-split-proxy-h",_364);
_36f.css("left",e.pageX-$(_364).offset().left-_36f.width()/2);
}
return false;
},onStopResize:function(e){
cc.children("div.layout-split-proxy-v,div.layout-split-proxy-h").hide();
pp.panel("resize",e.data);
_355(_364);
_354=false;
cc.find(">div.layout-mask").remove();
}},_365));
}
};
function _370(_371,_372){
var _373=$.data(_371,"layout").panels;
if(_373[_372].length){
_373[_372].panel("destroy");
_373[_372]=$();
var _374="expand"+_372.substring(0,1).toUpperCase()+_372.substring(1);
if(_373[_374]){
_373[_374].panel("destroy");
_373[_374]=undefined;
}
}
};
function _375(_376,_377,_378){
if(_378==undefined){
_378="normal";
}
var _379=$.data(_376,"layout").panels;
var p=_379[_377];
var _37a=p.panel("options");
if(_37a.onBeforeCollapse.call(p)==false){
return;
}
var _37b="expand"+_377.substring(0,1).toUpperCase()+_377.substring(1);
if(!_379[_37b]){
_379[_37b]=_37c(_377);
_379[_37b].panel("panel").bind("click",function(){
var _37d=_37e();
p.panel("expand",false).panel("open").panel("resize",_37d.collapse);
p.panel("panel").animate(_37d.expand,function(){
$(this).unbind(".layout").bind("mouseleave.layout",{region:_377},function(e){
if(_354==true){
return;
}
_375(_376,e.data.region);
});
});
return false;
});
}
var _37f=_37e();
if(!_35a(_379[_37b])){
_379.center.panel("resize",_37f.resizeC);
}
p.panel("panel").animate(_37f.collapse,_378,function(){
p.panel("collapse",false).panel("close");
_379[_37b].panel("open").panel("resize",_37f.expandP);
$(this).unbind(".layout");
});
function _37c(dir){
var icon;
if(dir=="east"){
icon="layout-button-left";
}else{
if(dir=="west"){
icon="layout-button-right";
}else{
if(dir=="north"){
icon="layout-button-down";
}else{
if(dir=="south"){
icon="layout-button-up";
}
}
}
}
var p=$("<div></div>").appendTo(_376);
p.panel($.extend({},$.fn.layout.paneldefaults,{cls:("layout-expand layout-expand-"+dir),title:"&nbsp;",closed:true,doSize:false,tools:[{iconCls:icon,handler:function(){
_381(_376,_377);
return false;
}}]}));
p.panel("panel").hover(function(){
$(this).addClass("layout-expand-over");
},function(){
$(this).removeClass("layout-expand-over");
});
return p;
};
function _37e(){
var cc=$(_376);
var _380=_379.center.panel("options");
if(_377=="east"){
var ww=_380.width+_37a.width-28;
if(_37a.split||!_37a.border){
ww++;
}
return {resizeC:{width:ww},expand:{left:cc.width()-_37a.width},expandP:{top:_380.top,left:cc.width()-28,width:28,height:_380.height},collapse:{left:cc.width(),top:_380.top,height:_380.height}};
}else{
if(_377=="west"){
var ww=_380.width+_37a.width-28;
if(_37a.split||!_37a.border){
ww++;
}
return {resizeC:{width:ww,left:28-1},expand:{left:0},expandP:{left:0,top:_380.top,width:28,height:_380.height},collapse:{left:-_37a.width,top:_380.top,height:_380.height}};
}else{
if(_377=="north"){
var hh=_380.height;
if(!_35a(_379.expandNorth)){
hh+=_37a.height-28+((_37a.split||!_37a.border)?1:0);
}
_379.east.add(_379.west).add(_379.expandEast).add(_379.expandWest).panel("resize",{top:28-1,height:hh});
return {resizeC:{top:28-1,height:hh},expand:{top:0},expandP:{top:0,left:0,width:cc.width(),height:28},collapse:{top:-_37a.height,width:cc.width()}};
}else{
if(_377=="south"){
var hh=_380.height;
if(!_35a(_379.expandSouth)){
hh+=_37a.height-28+((_37a.split||!_37a.border)?1:0);
}
_379.east.add(_379.west).add(_379.expandEast).add(_379.expandWest).panel("resize",{height:hh});
return {resizeC:{height:hh},expand:{top:cc.height()-_37a.height},expandP:{top:cc.height()-28,left:0,width:cc.width(),height:28},collapse:{top:cc.height(),width:cc.width()}};
}
}
}
}
};
};
function _381(_382,_383){
var _384=$.data(_382,"layout").panels;
var p=_384[_383];
var _385=p.panel("options");
if(_385.onBeforeExpand.call(p)==false){
return;
}
var _386=_387();
var _388="expand"+_383.substring(0,1).toUpperCase()+_383.substring(1);
if(_384[_388]){
_384[_388].panel("close");
p.panel("panel").stop(true,true);
p.panel("expand",false).panel("open").panel("resize",_386.collapse);
p.panel("panel").animate(_386.expand,function(){
_355(_382);
});
}
function _387(){
var cc=$(_382);
var _389=_384.center.panel("options");
if(_383=="east"&&_384.expandEast){
return {collapse:{left:cc.width(),top:_389.top,height:_389.height},expand:{left:cc.width()-_384["east"].panel("options").width}};
}else{
if(_383=="west"&&_384.expandWest){
return {collapse:{left:-_384["west"].panel("options").width,top:_389.top,height:_389.height},expand:{left:0}};
}else{
if(_383=="north"&&_384.expandNorth){
return {collapse:{top:-_384["north"].panel("options").height,width:cc.width()},expand:{top:0}};
}else{
if(_383=="south"&&_384.expandSouth){
return {collapse:{top:cc.height(),width:cc.width()},expand:{top:cc.height()-_384["south"].panel("options").height}};
}
}
}
}
};
};
function _35a(pp){
if(!pp){
return false;
}
if(pp.length){
return pp.panel("panel").is(":visible");
}else{
return false;
}
};
function _38a(_38b){
var _38c=$.data(_38b,"layout").panels;
if(_38c.east.length&&_38c.east.panel("options").collapsed){
_375(_38b,"east",0);
}
if(_38c.west.length&&_38c.west.panel("options").collapsed){
_375(_38b,"west",0);
}
if(_38c.north.length&&_38c.north.panel("options").collapsed){
_375(_38b,"north",0);
}
if(_38c.south.length&&_38c.south.panel("options").collapsed){
_375(_38b,"south",0);
}
};
$.fn.layout=function(_38d,_38e){
if(typeof _38d=="string"){
return $.fn.layout.methods[_38d](this,_38e);
}
_38d=_38d||{};
return this.each(function(){
var _38f=$.data(this,"layout");
if(_38f){
$.extend(_38f.options,_38d);
}else{
var opts=$.extend({},$.fn.layout.defaults,$.fn.layout.parseOptions(this),_38d);
$.data(this,"layout",{options:opts,panels:{center:$(),north:$(),south:$(),east:$(),west:$()}});
init(this);
}
_355(this);
_38a(this);
});
};
$.fn.layout.methods={resize:function(jq){
return jq.each(function(){
_355(this);
});
},panel:function(jq,_390){
return $.data(jq[0],"layout").panels[_390];
},collapse:function(jq,_391){
return jq.each(function(){
_375(this,_391);
});
},expand:function(jq,_392){
return jq.each(function(){
_381(this,_392);
});
},add:function(jq,_393){
return jq.each(function(){
_363(this,_393);
_355(this);
if($(this).layout("panel",_393.region).panel("options").collapsed){
_375(this,_393.region,0);
}
});
},remove:function(jq,_394){
return jq.each(function(){
_370(this,_394);
_355(this);
});
}};
$.fn.layout.parseOptions=function(_395){
return $.extend({},$.parser.parseOptions(_395,[{fit:"boolean"}]));
};
$.fn.layout.defaults={fit:false};
$.fn.layout.parsePanelOptions=function(_396){
var t=$(_396);
return $.extend({},$.fn.panel.parseOptions(_396),$.parser.parseOptions(_396,["region",{split:"boolean",minWidth:"number",minHeight:"number",maxWidth:"number",maxHeight:"number"}]));
};
$.fn.layout.paneldefaults=$.extend({},$.fn.panel.defaults,{region:null,split:false,minWidth:10,minHeight:10,maxWidth:10000,maxHeight:10000});
})(jQuery);
(function($){
function init(_397){
$(_397).appendTo("body");
$(_397).addClass("menu-top");
$(document).unbind(".menu").bind("mousedown.menu",function(e){
var _398=$("body>div.menu:visible");
var m=$(e.target).closest("div.menu",_398);
if(m.length){
return;
}
$("body>div.menu-top:visible").menu("hide");
});
var _399=_39a($(_397));
for(var i=0;i<_399.length;i++){
_39b(_399[i]);
}
function _39a(menu){
var _39c=[];
menu.addClass("menu");
_39c.push(menu);
if(!menu.hasClass("menu-content")){
menu.children("div").each(function(){
var _39d=$(this).children("div");
if(_39d.length){
_39d.insertAfter(_397);
this.submenu=_39d;
var mm=_39a(_39d);
_39c=_39c.concat(mm);
}
});
}
return _39c;
};
function _39b(menu){
var _39e=$.parser.parseOptions(menu[0],["width"]).width;
if(menu.hasClass("menu-content")){
menu[0].originalWidth=_39e||menu._outerWidth();
}else{
menu[0].originalWidth=_39e||0;
menu.children("div").each(function(){
var item=$(this);
var _39f=$.extend({},$.parser.parseOptions(this,["name","iconCls","href",{separator:"boolean"}]),{disabled:(item.attr("disabled")?true:undefined)});
if(_39f.separator){
item.addClass("menu-sep");
}
if(!item.hasClass("menu-sep")){
item[0].itemName=_39f.name||"";
item[0].itemHref=_39f.href||"";
var text=item.addClass("menu-item").html();
item.empty().append($("<div class=\"menu-text\"></div>").html(text));
if(_39f.iconCls){
$("<div class=\"menu-icon\"></div>").addClass(_39f.iconCls).appendTo(item);
}
if(_39f.disabled){
_3a0(_397,item[0],true);
}
if(item[0].submenu){
$("<div class=\"menu-rightarrow\"></div>").appendTo(item);
}
_3a1(_397,item);
}
});
$("<div class=\"menu-line\"></div>").prependTo(menu);
}
_3a2(_397,menu);
menu.hide();
_3a3(_397,menu);
};
};
function _3a2(_3a4,menu){
var opts=$.data(_3a4,"menu").options;
var _3a5=menu.attr("style");
menu.css({display:"block",left:-10000,height:"auto",overflow:"hidden"});
var _3a6=0;
menu.find("div.menu-text").each(function(){
if(_3a6<$(this)._outerWidth()){
_3a6=$(this)._outerWidth();
}
$(this).closest("div.menu-item")._outerHeight($(this)._outerHeight()+2);
});
_3a6+=65;
menu._outerWidth(Math.max((menu[0].originalWidth||0),_3a6,opts.minWidth));
menu.children("div.menu-line")._outerHeight(menu.outerHeight());
menu.attr("style",_3a5);
};
function _3a3(_3a7,menu){
var _3a8=$.data(_3a7,"menu");
menu.unbind(".menu").bind("mouseenter.menu",function(){
if(_3a8.timer){
clearTimeout(_3a8.timer);
_3a8.timer=null;
}
}).bind("mouseleave.menu",function(){
if(_3a8.options.hideOnUnhover){
_3a8.timer=setTimeout(function(){
_3a9(_3a7);
},100);
}
});
};
function _3a1(_3aa,item){
if(!item.hasClass("menu-item")){
return;
}
item.unbind(".menu");
item.bind("click.menu",function(){
if($(this).hasClass("menu-item-disabled")){
return;
}
if(!this.submenu){
_3a9(_3aa);
var href=$(this).attr("href");
if(href){
location.href=href;
}
}
var item=$(_3aa).menu("getItem",this);
$.data(_3aa,"menu").options.onClick.call(_3aa,item);
}).bind("mouseenter.menu",function(e){
item.siblings().each(function(){
if(this.submenu){
_3ad(this.submenu);
}
$(this).removeClass("menu-active");
});
item.addClass("menu-active");
if($(this).hasClass("menu-item-disabled")){
item.addClass("menu-active-disabled");
return;
}
var _3ab=item[0].submenu;
if(_3ab){
$(_3aa).menu("show",{menu:_3ab,parent:item});
}
}).bind("mouseleave.menu",function(e){
item.removeClass("menu-active menu-active-disabled");
var _3ac=item[0].submenu;
if(_3ac){
if(e.pageX>=parseInt(_3ac.css("left"))){
item.addClass("menu-active");
}else{
_3ad(_3ac);
}
}else{
item.removeClass("menu-active");
}
});
};
function _3a9(_3ae){
var _3af=$.data(_3ae,"menu");
if(_3af){
if($(_3ae).is(":visible")){
_3ad($(_3ae));
_3af.options.onHide.call(_3ae);
}
}
return false;
};
function _3b0(_3b1,_3b2){
var left,top;
_3b2=_3b2||{};
var menu=$(_3b2.menu||_3b1);
if(menu.hasClass("menu-top")){
var opts=$.data(_3b1,"menu").options;
$.extend(opts,_3b2);
left=opts.left;
top=opts.top;
if(opts.alignTo){
var at=$(opts.alignTo);
left=at.offset().left;
top=at.offset().top+at._outerHeight();
}
if(left+menu.outerWidth()>$(window)._outerWidth()+$(document)._scrollLeft()){
left=$(window)._outerWidth()+$(document).scrollLeft()-menu.outerWidth()-5;
}
if(top+menu.outerHeight()>$(window)._outerHeight()+$(document).scrollTop()){
top=$(window)._outerHeight()+$(document).scrollTop()-menu.outerHeight()-5;
}
}else{
var _3b3=_3b2.parent;
left=_3b3.offset().left+_3b3.outerWidth()-2;
if(left+menu.outerWidth()+5>$(window)._outerWidth()+$(document).scrollLeft()){
left=_3b3.offset().left-menu.outerWidth()+2;
}
var top=_3b3.offset().top-3;
if(top+menu.outerHeight()>$(window)._outerHeight()+$(document).scrollTop()){
top=$(window)._outerHeight()+$(document).scrollTop()-menu.outerHeight()-5;
}
}
menu.css({left:left,top:top});
menu.show(0,function(){
if(!menu[0].shadow){
menu[0].shadow=$("<div class=\"menu-shadow\"></div>").insertAfter(menu);
}
menu[0].shadow.css({display:"block",zIndex:$.fn.menu.defaults.zIndex++,left:menu.css("left"),top:menu.css("top"),width:menu.outerWidth(),height:menu.outerHeight()});
menu.css("z-index",$.fn.menu.defaults.zIndex++);
if(menu.hasClass("menu-top")){
$.data(menu[0],"menu").options.onShow.call(menu[0]);
}
});
};
function _3ad(menu){
if(!menu){
return;
}
_3b4(menu);
menu.find("div.menu-item").each(function(){
if(this.submenu){
_3ad(this.submenu);
}
$(this).removeClass("menu-active");
});
function _3b4(m){
m.stop(true,true);
if(m[0].shadow){
m[0].shadow.hide();
}
m.hide();
};
};
function _3b5(_3b6,text){
var _3b7=null;
var tmp=$("<div></div>");
function find(menu){
menu.children("div.menu-item").each(function(){
var item=$(_3b6).menu("getItem",this);
var s=tmp.empty().html(item.text).text();
if(text==$.trim(s)){
_3b7=item;
}else{
if(this.submenu&&!_3b7){
find(this.submenu);
}
}
});
};
find($(_3b6));
tmp.remove();
return _3b7;
};
function _3a0(_3b8,_3b9,_3ba){
var t=$(_3b9);
if(!t.hasClass("menu-item")){
return;
}
if(_3ba){
t.addClass("menu-item-disabled");
if(_3b9.onclick){
_3b9.onclick1=_3b9.onclick;
_3b9.onclick=null;
}
}else{
t.removeClass("menu-item-disabled");
if(_3b9.onclick1){
_3b9.onclick=_3b9.onclick1;
_3b9.onclick1=null;
}
}
};
function _3bb(_3bc,_3bd){
var menu=$(_3bc);
if(_3bd.parent){
if(!_3bd.parent.submenu){
var _3be=$("<div class=\"menu\"><div class=\"menu-line\"></div></div>").appendTo("body");
_3be.hide();
_3bd.parent.submenu=_3be;
$("<div class=\"menu-rightarrow\"></div>").appendTo(_3bd.parent);
}
menu=_3bd.parent.submenu;
}
if(_3bd.separator){
var item=$("<div class=\"menu-sep\"></div>").appendTo(menu);
}else{
var item=$("<div class=\"menu-item\"></div>").appendTo(menu);
$("<div class=\"menu-text\"></div>").html(_3bd.text).appendTo(item);
}
if(_3bd.iconCls){
$("<div class=\"menu-icon\"></div>").addClass(_3bd.iconCls).appendTo(item);
}
if(_3bd.id){
item.attr("id",_3bd.id);
}
if(_3bd.name){
item[0].itemName=_3bd.name;
}
if(_3bd.href){
item[0].itemHref=_3bd.href;
}
if(_3bd.onclick){
if(typeof _3bd.onclick=="string"){
item.attr("onclick",_3bd.onclick);
}else{
item[0].onclick=eval(_3bd.onclick);
}
}
if(_3bd.handler){
item[0].onclick=eval(_3bd.handler);
}
if(_3bd.disabled){
_3a0(_3bc,item[0],true);
}
_3a1(_3bc,item);
_3a3(_3bc,menu);
_3a2(_3bc,menu);
};
function _3bf(_3c0,_3c1){
function _3c2(el){
if(el.submenu){
el.submenu.children("div.menu-item").each(function(){
_3c2(this);
});
var _3c3=el.submenu[0].shadow;
if(_3c3){
_3c3.remove();
}
el.submenu.remove();
}
$(el).remove();
};
_3c2(_3c1);
};
function _3c4(_3c5){
$(_3c5).children("div.menu-item").each(function(){
_3bf(_3c5,this);
});
if(_3c5.shadow){
_3c5.shadow.remove();
}
$(_3c5).remove();
};
$.fn.menu=function(_3c6,_3c7){
if(typeof _3c6=="string"){
return $.fn.menu.methods[_3c6](this,_3c7);
}
_3c6=_3c6||{};
return this.each(function(){
var _3c8=$.data(this,"menu");
if(_3c8){
$.extend(_3c8.options,_3c6);
}else{
_3c8=$.data(this,"menu",{options:$.extend({},$.fn.menu.defaults,$.fn.menu.parseOptions(this),_3c6)});
init(this);
}
$(this).css({left:_3c8.options.left,top:_3c8.options.top});
});
};
$.fn.menu.methods={options:function(jq){
return $.data(jq[0],"menu").options;
},show:function(jq,pos){
return jq.each(function(){
_3b0(this,pos);
});
},hide:function(jq){
return jq.each(function(){
_3a9(this);
});
},destroy:function(jq){
return jq.each(function(){
_3c4(this);
});
},setText:function(jq,_3c9){
return jq.each(function(){
$(_3c9.target).children("div.menu-text").html(_3c9.text);
});
},setIcon:function(jq,_3ca){
return jq.each(function(){
var item=$(this).menu("getItem",_3ca.target);
if(item.iconCls){
$(item.target).children("div.menu-icon").removeClass(item.iconCls).addClass(_3ca.iconCls);
}else{
$("<div class=\"menu-icon\"></div>").addClass(_3ca.iconCls).appendTo(_3ca.target);
}
});
},getItem:function(jq,_3cb){
var t=$(_3cb);
var item={target:_3cb,id:t.attr("id"),text:$.trim(t.children("div.menu-text").html()),disabled:t.hasClass("menu-item-disabled"),name:_3cb.itemName,href:_3cb.itemHref,onclick:_3cb.onclick};
var icon=t.children("div.menu-icon");
if(icon.length){
var cc=[];
var aa=icon.attr("class").split(" ");
for(var i=0;i<aa.length;i++){
if(aa[i]!="menu-icon"){
cc.push(aa[i]);
}
}
item.iconCls=cc.join(" ");
}
return item;
},findItem:function(jq,text){
return _3b5(jq[0],text);
},appendItem:function(jq,_3cc){
return jq.each(function(){
_3bb(this,_3cc);
});
},removeItem:function(jq,_3cd){
return jq.each(function(){
_3bf(this,_3cd);
});
},enableItem:function(jq,_3ce){
return jq.each(function(){
_3a0(this,_3ce,false);
});
},disableItem:function(jq,_3cf){
return jq.each(function(){
_3a0(this,_3cf,true);
});
}};
$.fn.menu.parseOptions=function(_3d0){
return $.extend({},$.parser.parseOptions(_3d0,["left","top",{minWidth:"number",hideOnUnhover:"boolean"}]));
};
$.fn.menu.defaults={zIndex:110000,left:0,top:0,minWidth:120,hideOnUnhover:true,onShow:function(){
},onHide:function(){
},onClick:function(item){
}};
})(jQuery);
(function($){
function init(_3d1){
var opts=$.data(_3d1,"menubutton").options;
var btn=$(_3d1);
btn.removeClass(opts.cls.btn1+" "+opts.cls.btn2).addClass("m-btn");
btn.linkbutton($.extend({},opts,{text:opts.text+"<span class=\""+opts.cls.arrow+"\">&nbsp;</span>"}));
if(opts.menu){
$(opts.menu).menu();
var _3d2=$(opts.menu).menu("options");
var _3d3=_3d2.onShow;
var _3d4=_3d2.onHide;
$.extend(_3d2,{onShow:function(){
var _3d5=$(this).menu("options");
var btn=$(_3d5.alignTo);
var opts=btn.menubutton("options");
btn.addClass((opts.plain==true)?opts.cls.btn2:opts.cls.btn1);
_3d3.call(this);
},onHide:function(){
var _3d6=$(this).menu("options");
var btn=$(_3d6.alignTo);
var opts=btn.menubutton("options");
btn.removeClass((opts.plain==true)?opts.cls.btn2:opts.cls.btn1);
_3d4.call(this);
}});
}
_3d7(_3d1,opts.disabled);
};
function _3d7(_3d8,_3d9){
var opts=$.data(_3d8,"menubutton").options;
opts.disabled=_3d9;
var btn=$(_3d8);
var t=btn.find("."+opts.cls.trigger);
if(!t.length){
t=btn;
}
t.unbind(".menubutton");
if(_3d9){
btn.linkbutton("disable");
}else{
btn.linkbutton("enable");
var _3da=null;
t.bind("click.menubutton",function(){
_3db(_3d8);
return false;
}).bind("mouseenter.menubutton",function(){
_3da=setTimeout(function(){
_3db(_3d8);
},opts.duration);
return false;
}).bind("mouseleave.menubutton",function(){
if(_3da){
clearTimeout(_3da);
}
});
}
};
function _3db(_3dc){
var opts=$.data(_3dc,"menubutton").options;
if(opts.disabled||!opts.menu){
return;
}
$("body>div.menu-top").menu("hide");
var btn=$(_3dc);
var mm=$(opts.menu);
if(mm.length){
mm.menu("options").alignTo=btn;
mm.menu("show",{alignTo:btn});
}
btn.blur();
};
$.fn.menubutton=function(_3dd,_3de){
if(typeof _3dd=="string"){
var _3df=$.fn.menubutton.methods[_3dd];
if(_3df){
return _3df(this,_3de);
}else{
return this.linkbutton(_3dd,_3de);
}
}
_3dd=_3dd||{};
return this.each(function(){
var _3e0=$.data(this,"menubutton");
if(_3e0){
$.extend(_3e0.options,_3dd);
}else{
$.data(this,"menubutton",{options:$.extend({},$.fn.menubutton.defaults,$.fn.menubutton.parseOptions(this),_3dd)});
$(this).removeAttr("disabled");
}
init(this);
});
};
$.fn.menubutton.methods={options:function(jq){
var _3e1=jq.linkbutton("options");
var _3e2=$.data(jq[0],"menubutton").options;
_3e2.toggle=_3e1.toggle;
_3e2.selected=_3e1.selected;
return _3e2;
},enable:function(jq){
return jq.each(function(){
_3d7(this,false);
});
},disable:function(jq){
return jq.each(function(){
_3d7(this,true);
});
},destroy:function(jq){
return jq.each(function(){
var opts=$(this).menubutton("options");
if(opts.menu){
$(opts.menu).menu("destroy");
}
$(this).remove();
});
}};
$.fn.menubutton.parseOptions=function(_3e3){
var t=$(_3e3);
return $.extend({},$.fn.linkbutton.parseOptions(_3e3),$.parser.parseOptions(_3e3,["menu",{plain:"boolean",duration:"number"}]));
};
$.fn.menubutton.defaults=$.extend({},$.fn.linkbutton.defaults,{plain:true,menu:null,duration:100,cls:{btn1:"m-btn-active",btn2:"m-btn-plain-active",arrow:"m-btn-downarrow",trigger:"m-btn"}});
})(jQuery);
(function($){
function init(_3e4){
var opts=$.data(_3e4,"splitbutton").options;
$(_3e4).menubutton(opts);
};
$.fn.splitbutton=function(_3e5,_3e6){
if(typeof _3e5=="string"){
var _3e7=$.fn.splitbutton.methods[_3e5];
if(_3e7){
return _3e7(this,_3e6);
}else{
return this.menubutton(_3e5,_3e6);
}
}
_3e5=_3e5||{};
return this.each(function(){
var _3e8=$.data(this,"splitbutton");
if(_3e8){
$.extend(_3e8.options,_3e5);
}else{
$.data(this,"splitbutton",{options:$.extend({},$.fn.splitbutton.defaults,$.fn.splitbutton.parseOptions(this),_3e5)});
$(this).removeAttr("disabled");
}
init(this);
});
};
$.fn.splitbutton.methods={options:function(jq){
var _3e9=jq.menubutton("options");
var _3ea=$.data(jq[0],"splitbutton").options;
$.extend(_3ea,{disabled:_3e9.disabled,toggle:_3e9.toggle,selected:_3e9.selected});
return _3ea;
}};
$.fn.splitbutton.parseOptions=function(_3eb){
var t=$(_3eb);
return $.extend({},$.fn.linkbutton.parseOptions(_3eb),$.parser.parseOptions(_3eb,["menu",{plain:"boolean",duration:"number"}]));
};
$.fn.splitbutton.defaults=$.extend({},$.fn.linkbutton.defaults,{plain:true,menu:null,duration:100,cls:{btn1:"s-btn-active",btn2:"s-btn-plain-active",arrow:"s-btn-downarrow",trigger:"s-btn-downarrow"}});
})(jQuery);
(function($){
function init(_3ec){
$(_3ec).addClass("searchbox-f").hide();
var span=$("<span class=\"searchbox\"></span>").insertAfter(_3ec);
var _3ed=$("<input type=\"text\" class=\"searchbox-text\">").appendTo(span);
$("<span><span class=\"searchbox-button\"></span></span>").appendTo(span);
var name=$(_3ec).attr("name");
if(name){
_3ed.attr("name",name);
$(_3ec).removeAttr("name").attr("searchboxName",name);
}
return span;
};
function _3ee(_3ef,_3f0){
var opts=$.data(_3ef,"searchbox").options;
var sb=$.data(_3ef,"searchbox").searchbox;
if(_3f0){
opts.width=_3f0;
}
sb.appendTo("body");
if(isNaN(opts.width)){
opts.width=sb._outerWidth();
}
var _3f1=sb.find("span.searchbox-button");
var menu=sb.find("a.searchbox-menu");
var _3f2=sb.find("input.searchbox-text");
sb._outerWidth(opts.width)._outerHeight(opts.height);
_3f2._outerWidth(sb.width()-menu._outerWidth()-_3f1._outerWidth());
_3f2.css({height:sb.height()+"px",lineHeight:sb.height()+"px"});
menu._outerHeight(sb.height());
_3f1._outerHeight(sb.height());
var _3f3=menu.find("span.l-btn-left");
_3f3._outerHeight(sb.height());
_3f3.find("span.l-btn-text,span.m-btn-downarrow").css({height:_3f3.height()+"px",lineHeight:_3f3.height()+"px"});
sb.insertAfter(_3ef);
};
function _3f4(_3f5){
var _3f6=$.data(_3f5,"searchbox");
var opts=_3f6.options;
if(opts.menu){
_3f6.menu=$(opts.menu).menu({onClick:function(item){
_3f7(item);
}});
var item=_3f6.menu.children("div.menu-item:first");
_3f6.menu.children("div.menu-item").each(function(){
var _3f8=$.extend({},$.parser.parseOptions(this),{selected:($(this).attr("selected")?true:undefined)});
if(_3f8.selected){
item=$(this);
return false;
}
});
item.triggerHandler("click");
}else{
_3f6.searchbox.find("a.searchbox-menu").remove();
_3f6.menu=null;
}
function _3f7(item){
_3f6.searchbox.find("a.searchbox-menu").remove();
var mb=$("<a class=\"searchbox-menu\" href=\"javascript:void(0)\"></a>").html(item.text);
mb.prependTo(_3f6.searchbox).menubutton({menu:_3f6.menu,iconCls:item.iconCls});
_3f6.searchbox.find("input.searchbox-text").attr("name",item.name||item.text);
_3ee(_3f5);
};
};
function _3f9(_3fa){
var _3fb=$.data(_3fa,"searchbox");
var opts=_3fb.options;
var _3fc=_3fb.searchbox.find("input.searchbox-text");
var _3fd=_3fb.searchbox.find(".searchbox-button");
_3fc.unbind(".searchbox").bind("blur.searchbox",function(e){
opts.value=$(this).val();
if(opts.value==""){
$(this).val(opts.prompt);
$(this).addClass("searchbox-prompt");
}else{
$(this).removeClass("searchbox-prompt");
}
}).bind("focus.searchbox",function(e){
if($(this).val()!=opts.value){
$(this).val(opts.value);
}
$(this).removeClass("searchbox-prompt");
}).bind("keydown.searchbox",function(e){
if(e.keyCode==13){
e.preventDefault();
opts.value=$(this).val();
opts.searcher.call(_3fa,opts.value,_3fc._propAttr("name"));
return false;
}
});
_3fd.unbind(".searchbox").bind("click.searchbox",function(){
opts.searcher.call(_3fa,opts.value,_3fc._propAttr("name"));
}).bind("mouseenter.searchbox",function(){
$(this).addClass("searchbox-button-hover");
}).bind("mouseleave.searchbox",function(){
$(this).removeClass("searchbox-button-hover");
});
};
function _3fe(_3ff){
var _400=$.data(_3ff,"searchbox");
var opts=_400.options;
var _401=_400.searchbox.find("input.searchbox-text");
if(opts.value==""){
_401.val(opts.prompt);
_401.addClass("searchbox-prompt");
}else{
_401.val(opts.value);
_401.removeClass("searchbox-prompt");
}
};
$.fn.searchbox=function(_402,_403){
if(typeof _402=="string"){
return $.fn.searchbox.methods[_402](this,_403);
}
_402=_402||{};
return this.each(function(){
var _404=$.data(this,"searchbox");
if(_404){
$.extend(_404.options,_402);
}else{
_404=$.data(this,"searchbox",{options:$.extend({},$.fn.searchbox.defaults,$.fn.searchbox.parseOptions(this),_402),searchbox:init(this)});
}
_3f4(this);
_3fe(this);
_3f9(this);
_3ee(this);
});
};
$.fn.searchbox.methods={options:function(jq){
return $.data(jq[0],"searchbox").options;
},menu:function(jq){
return $.data(jq[0],"searchbox").menu;
},textbox:function(jq){
return $.data(jq[0],"searchbox").searchbox.find("input.searchbox-text");
},getValue:function(jq){
return $.data(jq[0],"searchbox").options.value;
},setValue:function(jq,_405){
return jq.each(function(){
$(this).searchbox("options").value=_405;
$(this).searchbox("textbox").val(_405);
$(this).searchbox("textbox").blur();
});
},getName:function(jq){
return $.data(jq[0],"searchbox").searchbox.find("input.searchbox-text").attr("name");
},selectName:function(jq,name){
return jq.each(function(){
var menu=$.data(this,"searchbox").menu;
if(menu){
menu.children("div.menu-item[name=\""+name+"\"]").triggerHandler("click");
}
});
},destroy:function(jq){
return jq.each(function(){
var menu=$(this).searchbox("menu");
if(menu){
menu.menu("destroy");
}
$.data(this,"searchbox").searchbox.remove();
$(this).remove();
});
},resize:function(jq,_406){
return jq.each(function(){
_3ee(this,_406);
});
}};
$.fn.searchbox.parseOptions=function(_407){
var t=$(_407);
return $.extend({},$.parser.parseOptions(_407,["width","height","prompt","menu"]),{value:t.val(),searcher:(t.attr("searcher")?eval(t.attr("searcher")):undefined)});
};
$.fn.searchbox.defaults={width:"auto",height:22,prompt:"",value:"",menu:null,searcher:function(_408,name){
}};
})(jQuery);
(function($){
function init(_409){
$(_409).addClass("validatebox-text");
};
function _40a(_40b){
var _40c=$.data(_40b,"validatebox");
_40c.validating=false;
if(_40c.timer){
clearTimeout(_40c.timer);
}
$(_40b).tooltip("destroy");
$(_40b).unbind();
$(_40b).remove();
};
function _40d(_40e){
var box=$(_40e);
var _40f=$.data(_40e,"validatebox");
box.unbind(".validatebox");
if(_40f.options.novalidate){
return;
}
box.bind("focus.validatebox",function(){
_40f.validating=true;
_40f.value=undefined;
(function(){
if(_40f.validating){
if(_40f.value!=box.val()){
_40f.value=box.val();
if(_40f.timer){
clearTimeout(_40f.timer);
}
_40f.timer=setTimeout(function(){
$(_40e).validatebox("validate");
},_40f.options.delay);
}else{
_414(_40e);
}
setTimeout(arguments.callee,200);
}
})();
}).bind("blur.validatebox",function(){
if(_40f.timer){
clearTimeout(_40f.timer);
_40f.timer=undefined;
}
_40f.validating=false;
_410(_40e);
}).bind("mouseenter.validatebox",function(){
if(box.hasClass("validatebox-invalid")){
_411(_40e);
}
}).bind("mouseleave.validatebox",function(){
if(!_40f.validating){
_410(_40e);
}
});
};
function _411(_412){
var _413=$.data(_412,"validatebox");
var opts=_413.options;
$(_412).tooltip($.extend({},opts.tipOptions,{content:_413.message,position:opts.tipPosition,deltaX:opts.deltaX})).tooltip("show");
_413.tip=true;
};
function _414(_415){
var _416=$.data(_415,"validatebox");
if(_416&&_416.tip){
$(_415).tooltip("reposition");
}
};
function _410(_417){
var _418=$.data(_417,"validatebox");
_418.tip=false;
$(_417).tooltip("hide");
};
function _419(_41a){
var _41b=$.data(_41a,"validatebox");
var opts=_41b.options;
var box=$(_41a);
var _41c=box.val();
function _41d(msg){
_41b.message=msg;
};
function _41e(_41f){
var _420=/([a-zA-Z_]+)(.*)/.exec(_41f);
var rule=opts.rules[_420[1]];
if(rule&&_41c){
var _421=eval(_420[2]);
if(!rule["validator"](_41c,_421)){
box.addClass("validatebox-invalid");
var _422=rule["message"];
if(_421){
for(var i=0;i<_421.length;i++){
_422=_422.replace(new RegExp("\\{"+i+"\\}","g"),_421[i]);
}
}
_41d(opts.invalidMessage||_422);
if(_41b.validating){
_411(_41a);
}
return false;
}
}
return true;
};
box.removeClass("validatebox-invalid");
_410(_41a);
if(opts.novalidate||box.is(":disabled")){
return true;
}
if(opts.required){
if(_41c==""){
box.addClass("validatebox-invalid");
_41d(opts.missingMessage);
if(_41b.validating){
_411(_41a);
}
return false;
}
}
if(opts.validType){
if(typeof opts.validType=="string"){
if(!_41e(opts.validType)){
return false;
}
}else{
for(var i=0;i<opts.validType.length;i++){
if(!_41e(opts.validType[i])){
return false;
}
}
}
}
return true;
};
function _423(_424,_425){
var opts=$.data(_424,"validatebox").options;
if(_425!=undefined){
opts.novalidate=_425;
}
if(opts.novalidate){
$(_424).removeClass("validatebox-invalid");
_410(_424);
}
_40d(_424);
};
$.fn.validatebox=function(_426,_427){
if(typeof _426=="string"){
return $.fn.validatebox.methods[_426](this,_427);
}
_426=_426||{};
return this.each(function(){
var _428=$.data(this,"validatebox");
if(_428){
$.extend(_428.options,_426);
}else{
init(this);
$.data(this,"validatebox",{options:$.extend({},$.fn.validatebox.defaults,$.fn.validatebox.parseOptions(this),_426)});
}
_423(this);
_419(this);
});
};
$.fn.validatebox.methods={options:function(jq){
return $.data(jq[0],"validatebox").options;
},destroy:function(jq){
return jq.each(function(){
_40a(this);
});
},validate:function(jq){
return jq.each(function(){
_419(this);
});
},isValid:function(jq){
return _419(jq[0]);
},enableValidation:function(jq){
return jq.each(function(){
_423(this,false);
});
},disableValidation:function(jq){
return jq.each(function(){
_423(this,true);
});
}};
$.fn.validatebox.parseOptions=function(_429){
var t=$(_429);
return $.extend({},$.parser.parseOptions(_429,["validType","missingMessage","invalidMessage","tipPosition",{delay:"number",deltaX:"number"}]),{required:(t.attr("required")?true:undefined),novalidate:(t.attr("novalidate")!=undefined?true:undefined)});
};
$.fn.validatebox.defaults={required:false,validType:null,delay:200,missingMessage:"This field is required.",invalidMessage:null,tipPosition:"right",deltaX:0,novalidate:false,tipOptions:{showEvent:"none",hideEvent:"none",showDelay:0,hideDelay:0,zIndex:"",onShow:function(){
$(this).tooltip("tip").css({color:"#000",borderColor:"#CC9933",backgroundColor:"#FFFFCC"});
},onHide:function(){
$(this).tooltip("destroy");
}},rules:{email:{validator:function(_42a){
return /^((([a-z]|\d|[!#\$%&'\*\+\-\/=\?\^_`{\|}~]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])+(\.([a-z]|\d|[!#\$%&'\*\+\-\/=\?\^_`{\|}~]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])+)*)|((\x22)((((\x20|\x09)*(\x0d\x0a))?(\x20|\x09)+)?(([\x01-\x08\x0b\x0c\x0e-\x1f\x7f]|\x21|[\x23-\x5b]|[\x5d-\x7e]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(\\([\x01-\x09\x0b\x0c\x0d-\x7f]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF]))))*(((\x20|\x09)*(\x0d\x0a))?(\x20|\x09)+)?(\x22)))@((([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])*([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])))\.)+(([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])*([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])))\.?$/i.test(_42a);
},message:"Please enter a valid email address."},url:{validator:function(_42b){
return /^(https?|ftp):\/\/(((([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(%[\da-f]{2})|[!\$&'\(\)\*\+,;=]|:)*@)?(((\d|[1-9]\d|1\d\d|2[0-4]\d|25[0-5])\.(\d|[1-9]\d|1\d\d|2[0-4]\d|25[0-5])\.(\d|[1-9]\d|1\d\d|2[0-4]\d|25[0-5])\.(\d|[1-9]\d|1\d\d|2[0-4]\d|25[0-5]))|((([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])*([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])))\.)+(([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])*([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])))\.?)(:\d*)?)(\/((([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(%[\da-f]{2})|[!\$&'\(\)\*\+,;=]|:|@)+(\/(([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(%[\da-f]{2})|[!\$&'\(\)\*\+,;=]|:|@)*)*)?)?(\?((([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(%[\da-f]{2})|[!\$&'\(\)\*\+,;=]|:|@)|[\uE000-\uF8FF]|\/|\?)*)?(\#((([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(%[\da-f]{2})|[!\$&'\(\)\*\+,;=]|:|@)|\/|\?)*)?$/i.test(_42b);
},message:"Please enter a valid URL."},length:{validator:function(_42c,_42d){
var len=$.trim(_42c).length;
return len>=_42d[0]&&len<=_42d[1];
},message:"Please enter a value between {0} and {1}."},remote:{validator:function(_42e,_42f){
var data={};
data[_42f[1]]=_42e;
var _430=$.ajax({url:_42f[0],dataType:"json",data:data,async:false,cache:false,type:"post"}).responseText;
return _430=="true";
},message:"Please fix this field."}}};
})(jQuery);
(function($){
function _431(_432,_433){
_433=_433||{};
var _434={};
if(_433.onSubmit){
if(_433.onSubmit.call(_432,_434)==false){
return;
}
}
var form=$(_432);
if(_433.url){
form.attr("action",_433.url);
}
var _435="easyui_frame_"+(new Date().getTime());
var _436=$("<iframe id="+_435+" name="+_435+"></iframe>").attr("src",window.ActiveXObject?"javascript:false":"about:blank").css({position:"absolute",top:-1000,left:-1000});
var t=form.attr("target"),a=form.attr("action");
form.attr("target",_435);
var _437=$();
try{
_436.appendTo("body");
_436.bind("load",cb);
for(var n in _434){
var f=$("<input type=\"hidden\" name=\""+n+"\">").val(_434[n]).appendTo(form);
_437=_437.add(f);
}
_438();
form[0].submit();
}
finally{
form.attr("action",a);
t?form.attr("target",t):form.removeAttr("target");
_437.remove();
}
function _438(){
var f=$("#"+_435);
if(!f.length){
return;
}
try{
var s=f.contents()[0].readyState;
if(s&&s.toLowerCase()=="uninitialized"){
setTimeout(_438,100);
}
}
catch(e){
cb();
}
};
var _439=10;
function cb(){
var _43a=$("#"+_435);
if(!_43a.length){
return;
}
_43a.unbind();
var data="";
try{
var body=_43a.contents().find("body");
data=body.html();
if(data==""){
if(--_439){
setTimeout(cb,100);
return;
}
}
var ta=body.find(">textarea");
if(ta.length){
data=ta.val();
}else{
var pre=body.find(">pre");
if(pre.length){
data=pre.html();
}
}
}
catch(e){
}
if(_433.success){
_433.success(data);
}
setTimeout(function(){
_43a.unbind();
_43a.remove();
},100);
};
};
function load(_43b,data){
if(!$.data(_43b,"form")){
$.data(_43b,"form",{options:$.extend({},$.fn.form.defaults)});
}
var opts=$.data(_43b,"form").options;
if(typeof data=="string"){
var _43c={};
if(opts.onBeforeLoad.call(_43b,_43c)==false){
return;
}
$.ajax({url:data,data:_43c,dataType:"json",success:function(data){
_43d(data);
},error:function(){
opts.onLoadError.apply(_43b,arguments);
}});
}else{
_43d(data);
}
function _43d(data){
var form=$(_43b);
for(var name in data){
var val=data[name];
var rr=_43e(name,val);
if(!rr.length){
var _43f=_440(name,val);
if(!_43f){
$("input[name=\""+name+"\"]",form).val(val);
$("textarea[name=\""+name+"\"]",form).val(val);
$("select[name=\""+name+"\"]",form).val(val);
}
}
_441(name,val);
}
opts.onLoadSuccess.call(_43b,data);
_447(_43b);
};
function _43e(name,val){
var rr=$(_43b).find("input[name=\""+name+"\"][type=radio], input[name=\""+name+"\"][type=checkbox]");
rr._propAttr("checked",false);
rr.each(function(){
var f=$(this);
if(f.val()==String(val)||$.inArray(f.val(),$.isArray(val)?val:[val])>=0){
f._propAttr("checked",true);
}
});
return rr;
};
function _440(name,val){
var _442=0;
var pp=["numberbox","slider"];
for(var i=0;i<pp.length;i++){
var p=pp[i];
var f=$(_43b).find("input["+p+"Name=\""+name+"\"]");
if(f.length){
f[p]("setValue",val);
_442+=f.length;
}
}
return _442;
};
function _441(name,val){
var form=$(_43b);
var cc=["combobox","combotree","combogrid","datetimebox","datebox","combo"];
var c=form.find("[comboName=\""+name+"\"]");
if(c.length){
for(var i=0;i<cc.length;i++){
var type=cc[i];
if(c.hasClass(type+"-f")){
if(c[type]("options").multiple){
c[type]("setValues",val);
}else{
c[type]("setValue",val);
}
return;
}
}
}
};
};
function _443(_444){
$("input,select,textarea",_444).each(function(){
var t=this.type,tag=this.tagName.toLowerCase();
if(t=="text"||t=="hidden"||t=="password"||tag=="textarea"){
this.value="";
}else{
if(t=="file"){
var file=$(this);
file.after(file.clone().val(""));
file.remove();
}else{
if(t=="checkbox"||t=="radio"){
this.checked=false;
}else{
if(tag=="select"){
this.selectedIndex=-1;
}
}
}
}
});
var t=$(_444);
var _445=["combo","combobox","combotree","combogrid","slider"];
for(var i=0;i<_445.length;i++){
var _446=_445[i];
var r=t.find("."+_446+"-f");
if(r.length&&r[_446]){
r[_446]("clear");
}
}
_447(_444);
};
function _448(_449){
_449.reset();
var t=$(_449);
var _44a=["combo","combobox","combotree","combogrid","datebox","datetimebox","spinner","timespinner","numberbox","numberspinner","slider"];
for(var i=0;i<_44a.length;i++){
var _44b=_44a[i];
var r=t.find("."+_44b+"-f");
if(r.length&&r[_44b]){
r[_44b]("reset");
}
}
_447(_449);
};
function _44c(_44d){
var _44e=$.data(_44d,"form").options;
var form=$(_44d);
form.unbind(".form").bind("submit.form",function(){
setTimeout(function(){
_431(_44d,_44e);
},0);
return false;
});
};
function _447(_44f){
if($.fn.validatebox){
var t=$(_44f);
t.find(".validatebox-text:not(:disabled)").validatebox("validate");
var _450=t.find(".validatebox-invalid");
_450.filter(":not(:disabled):first").focus();
return _450.length==0;
}
return true;
};
function _451(_452,_453){
$(_452).find(".validatebox-text:not(:disabled)").validatebox(_453?"disableValidation":"enableValidation");
};
$.fn.form=function(_454,_455){
if(typeof _454=="string"){
return $.fn.form.methods[_454](this,_455);
}
_454=_454||{};
return this.each(function(){
if(!$.data(this,"form")){
$.data(this,"form",{options:$.extend({},$.fn.form.defaults,_454)});
}
_44c(this);
});
};
$.fn.form.methods={submit:function(jq,_456){
return jq.each(function(){
_431(this,$.extend({},$.fn.form.defaults,_456||{}));
});
},load:function(jq,data){
return jq.each(function(){
load(this,data);
});
},clear:function(jq){
return jq.each(function(){
_443(this);
});
},reset:function(jq){
return jq.each(function(){
_448(this);
});
},validate:function(jq){
return _447(jq[0]);
},disableValidation:function(jq){
return jq.each(function(){
_451(this,true);
});
},enableValidation:function(jq){
return jq.each(function(){
_451(this,false);
});
}};
$.fn.form.defaults={url:null,onSubmit:function(_457){
return $(this).form("validate");
},success:function(data){
},onBeforeLoad:function(_458){
},onLoadSuccess:function(data){
},onLoadError:function(){
}};
})(jQuery);
(function($){
function init(_459){
$(_459).addClass("numberbox-f");
var v=$("<input type=\"hidden\">").insertAfter(_459);
var name=$(_459).attr("name");
if(name){
v.attr("name",name);
$(_459).removeAttr("name").attr("numberboxName",name);
}
return v;
};
function _45a(_45b){
var opts=$.data(_45b,"numberbox").options;
var fn=opts.onChange;
opts.onChange=function(){
};
_45c(_45b,opts.parser.call(_45b,opts.value));
opts.onChange=fn;
opts.originalValue=_45d(_45b);
};
function _45d(_45e){
return $.data(_45e,"numberbox").field.val();
};
function _45c(_45f,_460){
var _461=$.data(_45f,"numberbox");
var opts=_461.options;
var _462=_45d(_45f);
_460=opts.parser.call(_45f,_460);
opts.value=_460;
_461.field.val(_460);
$(_45f).val(opts.formatter.call(_45f,_460));
if(_462!=_460){
opts.onChange.call(_45f,_460,_462);
}
};
function _463(_464){
var opts=$.data(_464,"numberbox").options;
$(_464).unbind(".numberbox").bind("keypress.numberbox",function(e){
return opts.filter.call(_464,e);
}).bind("blur.numberbox",function(){
_45c(_464,$(this).val());
$(this).val(opts.formatter.call(_464,_45d(_464)));
}).bind("focus.numberbox",function(){
var vv=_45d(_464);
if(vv!=opts.parser.call(_464,$(this).val())){
$(this).val(opts.formatter.call(_464,vv));
}
});
};
function _465(_466){
if($.fn.validatebox){
var opts=$.data(_466,"numberbox").options;
$(_466).validatebox(opts);
}
};
function _467(_468,_469){
var opts=$.data(_468,"numberbox").options;
if(_469){
opts.disabled=true;
$(_468).attr("disabled",true);
}else{
opts.disabled=false;
$(_468).removeAttr("disabled");
}
};
$.fn.numberbox=function(_46a,_46b){
if(typeof _46a=="string"){
var _46c=$.fn.numberbox.methods[_46a];
if(_46c){
return _46c(this,_46b);
}else{
return this.validatebox(_46a,_46b);
}
}
_46a=_46a||{};
return this.each(function(){
var _46d=$.data(this,"numberbox");
if(_46d){
$.extend(_46d.options,_46a);
}else{
_46d=$.data(this,"numberbox",{options:$.extend({},$.fn.numberbox.defaults,$.fn.numberbox.parseOptions(this),_46a),field:init(this)});
$(this).removeAttr("disabled");
$(this).css({imeMode:"disabled"});
}
_467(this,_46d.options.disabled);
_463(this);
_465(this);
_45a(this);
});
};
$.fn.numberbox.methods={options:function(jq){
return $.data(jq[0],"numberbox").options;
},destroy:function(jq){
return jq.each(function(){
$.data(this,"numberbox").field.remove();
$(this).validatebox("destroy");
$(this).remove();
});
},disable:function(jq){
return jq.each(function(){
_467(this,true);
});
},enable:function(jq){
return jq.each(function(){
_467(this,false);
});
},fix:function(jq){
return jq.each(function(){
_45c(this,$(this).val());
});
},setValue:function(jq,_46e){
return jq.each(function(){
_45c(this,_46e);
});
},getValue:function(jq){
return _45d(jq[0]);
},clear:function(jq){
return jq.each(function(){
var _46f=$.data(this,"numberbox");
_46f.field.val("");
$(this).val("");
});
},reset:function(jq){
return jq.each(function(){
var opts=$(this).numberbox("options");
$(this).numberbox("setValue",opts.originalValue);
});
}};
$.fn.numberbox.parseOptions=function(_470){
var t=$(_470);
return $.extend({},$.fn.validatebox.parseOptions(_470),$.parser.parseOptions(_470,["decimalSeparator","groupSeparator","suffix",{min:"number",max:"number",precision:"number"}]),{prefix:(t.attr("prefix")?t.attr("prefix"):undefined),disabled:(t.attr("disabled")?true:undefined),value:(t.val()||undefined)});
};
$.fn.numberbox.defaults=$.extend({},$.fn.validatebox.defaults,{disabled:false,value:"",min:null,max:null,precision:0,decimalSeparator:".",groupSeparator:"",prefix:"",suffix:"",filter:function(e){
var opts=$(this).numberbox("options");
if(e.which==45){
return ($(this).val().indexOf("-")==-1?true:false);
}
var c=String.fromCharCode(e.which);
if(c==opts.decimalSeparator){
return ($(this).val().indexOf(c)==-1?true:false);
}else{
if(c==opts.groupSeparator){
return true;
}else{
if((e.which>=48&&e.which<=57&&e.ctrlKey==false&&e.shiftKey==false)||e.which==0||e.which==8){
return true;
}else{
if(e.ctrlKey==true&&(e.which==99||e.which==118)){
return true;
}else{
return false;
}
}
}
}
},formatter:function(_471){
if(!_471){
return _471;
}
_471=_471+"";
var opts=$(this).numberbox("options");
var s1=_471,s2="";
var dpos=_471.indexOf(".");
if(dpos>=0){
s1=_471.substring(0,dpos);
s2=_471.substring(dpos+1,_471.length);
}
if(opts.groupSeparator){
var p=/(\d+)(\d{3})/;
while(p.test(s1)){
s1=s1.replace(p,"$1"+opts.groupSeparator+"$2");
}
}
if(s2){
return opts.prefix+s1+opts.decimalSeparator+s2+opts.suffix;
}else{
return opts.prefix+s1+opts.suffix;
}
},parser:function(s){
s=s+"";
var opts=$(this).numberbox("options");
if(parseFloat(s)!=s){
if(opts.prefix){
s=$.trim(s.replace(new RegExp("\\"+$.trim(opts.prefix),"g"),""));
}
if(opts.suffix){
s=$.trim(s.replace(new RegExp("\\"+$.trim(opts.suffix),"g"),""));
}
if(opts.groupSeparator){
s=$.trim(s.replace(new RegExp("\\"+opts.groupSeparator,"g"),""));
}
if(opts.decimalSeparator){
s=$.trim(s.replace(new RegExp("\\"+opts.decimalSeparator,"g"),"."));
}
s=s.replace(/\s/g,"");
}
var val=parseFloat(s).toFixed(opts.precision);
if(isNaN(val)){
val="";
}else{
if(typeof (opts.min)=="number"&&val<opts.min){
val=opts.min.toFixed(opts.precision);
}else{
if(typeof (opts.max)=="number"&&val>opts.max){
val=opts.max.toFixed(opts.precision);
}
}
}
return val;
},onChange:function(_472,_473){
}});
})(jQuery);
(function($){
function _474(_475){
var opts=$.data(_475,"calendar").options;
var t=$(_475);
opts.fit?$.extend(opts,t._fit()):t._fit(false);
var _476=t.find(".calendar-header");
t._outerWidth(opts.width);
t._outerHeight(opts.height);
t.find(".calendar-body")._outerHeight(t.height()-_476._outerHeight());
};
function init(_477){
$(_477).addClass("calendar").html("<div class=\"calendar-header\">"+"<div class=\"calendar-prevmonth\"></div>"+"<div class=\"calendar-nextmonth\"></div>"+"<div class=\"calendar-prevyear\"></div>"+"<div class=\"calendar-nextyear\"></div>"+"<div class=\"calendar-title\">"+"<span>Aprial 2010</span>"+"</div>"+"</div>"+"<div class=\"calendar-body\">"+"<div class=\"calendar-menu\">"+"<div class=\"calendar-menu-year-inner\">"+"<span class=\"calendar-menu-prev\"></span>"+"<span><input class=\"calendar-menu-year\" type=\"text\"></input></span>"+"<span class=\"calendar-menu-next\"></span>"+"</div>"+"<div class=\"calendar-menu-month-inner\">"+"</div>"+"</div>"+"</div>");
$(_477).find(".calendar-title span").hover(function(){
$(this).addClass("calendar-menu-hover");
},function(){
$(this).removeClass("calendar-menu-hover");
}).click(function(){
var menu=$(_477).find(".calendar-menu");
if(menu.is(":visible")){
menu.hide();
}else{
_47e(_477);
}
});
$(".calendar-prevmonth,.calendar-nextmonth,.calendar-prevyear,.calendar-nextyear",_477).hover(function(){
$(this).addClass("calendar-nav-hover");
},function(){
$(this).removeClass("calendar-nav-hover");
});
$(_477).find(".calendar-nextmonth").click(function(){
_478(_477,1);
});
$(_477).find(".calendar-prevmonth").click(function(){
_478(_477,-1);
});
$(_477).find(".calendar-nextyear").click(function(){
_47b(_477,1);
});
$(_477).find(".calendar-prevyear").click(function(){
_47b(_477,-1);
});
$(_477).bind("_resize",function(){
var opts=$.data(_477,"calendar").options;
if(opts.fit==true){
_474(_477);
}
return false;
});
};
function _478(_479,_47a){
var opts=$.data(_479,"calendar").options;
opts.month+=_47a;
if(opts.month>12){
opts.year++;
opts.month=1;
}else{
if(opts.month<1){
opts.year--;
opts.month=12;
}
}
show(_479);
var menu=$(_479).find(".calendar-menu-month-inner");
menu.find("td.calendar-selected").removeClass("calendar-selected");
menu.find("td:eq("+(opts.month-1)+")").addClass("calendar-selected");
};
function _47b(_47c,_47d){
var opts=$.data(_47c,"calendar").options;
opts.year+=_47d;
show(_47c);
var menu=$(_47c).find(".calendar-menu-year");
menu.val(opts.year);
};
function _47e(_47f){
var opts=$.data(_47f,"calendar").options;
$(_47f).find(".calendar-menu").show();
if($(_47f).find(".calendar-menu-month-inner").is(":empty")){
$(_47f).find(".calendar-menu-month-inner").empty();
var t=$("<table></table>").appendTo($(_47f).find(".calendar-menu-month-inner"));
var idx=0;
for(var i=0;i<3;i++){
var tr=$("<tr></tr>").appendTo(t);
for(var j=0;j<4;j++){
$("<td class=\"calendar-menu-month\"></td>").html(opts.months[idx++]).attr("abbr",idx).appendTo(tr);
}
}
$(_47f).find(".calendar-menu-prev,.calendar-menu-next").hover(function(){
$(this).addClass("calendar-menu-hover");
},function(){
$(this).removeClass("calendar-menu-hover");
});
$(_47f).find(".calendar-menu-next").click(function(){
var y=$(_47f).find(".calendar-menu-year");
if(!isNaN(y.val())){
y.val(parseInt(y.val())+1);
}
});
$(_47f).find(".calendar-menu-prev").click(function(){
var y=$(_47f).find(".calendar-menu-year");
if(!isNaN(y.val())){
y.val(parseInt(y.val()-1));
}
});
$(_47f).find(".calendar-menu-year").keypress(function(e){
if(e.keyCode==13){
_480();
}
});
$(_47f).find(".calendar-menu-month").hover(function(){
$(this).addClass("calendar-menu-hover");
},function(){
$(this).removeClass("calendar-menu-hover");
}).click(function(){
var menu=$(_47f).find(".calendar-menu");
menu.find(".calendar-selected").removeClass("calendar-selected");
$(this).addClass("calendar-selected");
_480();
});
}
function _480(){
var menu=$(_47f).find(".calendar-menu");
var year=menu.find(".calendar-menu-year").val();
var _481=menu.find(".calendar-selected").attr("abbr");
if(!isNaN(year)){
opts.year=parseInt(year);
opts.month=parseInt(_481);
show(_47f);
}
menu.hide();
};
var body=$(_47f).find(".calendar-body");
var sele=$(_47f).find(".calendar-menu");
var _482=sele.find(".calendar-menu-year-inner");
var _483=sele.find(".calendar-menu-month-inner");
_482.find("input").val(opts.year).focus();
_483.find("td.calendar-selected").removeClass("calendar-selected");
_483.find("td:eq("+(opts.month-1)+")").addClass("calendar-selected");
sele._outerWidth(body._outerWidth());
sele._outerHeight(body._outerHeight());
_483._outerHeight(sele.height()-_482._outerHeight());
};
function _484(_485,year,_486){
var opts=$.data(_485,"calendar").options;
var _487=[];
var _488=new Date(year,_486,0).getDate();
for(var i=1;i<=_488;i++){
_487.push([year,_486,i]);
}
var _489=[],week=[];
var _48a=-1;
while(_487.length>0){
var date=_487.shift();
week.push(date);
var day=new Date(date[0],date[1]-1,date[2]).getDay();
if(_48a==day){
day=0;
}else{
if(day==(opts.firstDay==0?7:opts.firstDay)-1){
_489.push(week);
week=[];
}
}
_48a=day;
}
if(week.length){
_489.push(week);
}
var _48b=_489[0];
if(_48b.length<7){
while(_48b.length<7){
var _48c=_48b[0];
var date=new Date(_48c[0],_48c[1]-1,_48c[2]-1);
_48b.unshift([date.getFullYear(),date.getMonth()+1,date.getDate()]);
}
}else{
var _48c=_48b[0];
var week=[];
for(var i=1;i<=7;i++){
var date=new Date(_48c[0],_48c[1]-1,_48c[2]-i);
week.unshift([date.getFullYear(),date.getMonth()+1,date.getDate()]);
}
_489.unshift(week);
}
var _48d=_489[_489.length-1];
while(_48d.length<7){
var _48e=_48d[_48d.length-1];
var date=new Date(_48e[0],_48e[1]-1,_48e[2]+1);
_48d.push([date.getFullYear(),date.getMonth()+1,date.getDate()]);
}
if(_489.length<6){
var _48e=_48d[_48d.length-1];
var week=[];
for(var i=1;i<=7;i++){
var date=new Date(_48e[0],_48e[1]-1,_48e[2]+i);
week.push([date.getFullYear(),date.getMonth()+1,date.getDate()]);
}
_489.push(week);
}
return _489;
};
function show(_48f){
var opts=$.data(_48f,"calendar").options;
$(_48f).find(".calendar-title span").html(opts.months[opts.month-1]+" "+opts.year);
var body=$(_48f).find("div.calendar-body");
body.find(">table").remove();
var t=$("<table cellspacing=\"0\" cellpadding=\"0\" border=\"0\"><thead></thead><tbody></tbody></table>").prependTo(body);
var tr=$("<tr></tr>").appendTo(t.find("thead"));
for(var i=opts.firstDay;i<opts.weeks.length;i++){
tr.append("<th>"+opts.weeks[i]+"</th>");
}
for(var i=0;i<opts.firstDay;i++){
tr.append("<th>"+opts.weeks[i]+"</th>");
}
var _490=_484(_48f,opts.year,opts.month);
for(var i=0;i<_490.length;i++){
var week=_490[i];
var tr=$("<tr></tr>").appendTo(t.find("tbody"));
for(var j=0;j<week.length;j++){
var day=week[j];
$("<td class=\"calendar-day calendar-other-month\"></td>").attr("abbr",day[0]+","+day[1]+","+day[2]).html(day[2]).appendTo(tr);
}
}
t.find("td[abbr^=\""+opts.year+","+opts.month+"\"]").removeClass("calendar-other-month");
var now=new Date();
var _491=now.getFullYear()+","+(now.getMonth()+1)+","+now.getDate();
t.find("td[abbr=\""+_491+"\"]").addClass("calendar-today");
if(opts.current){
t.find(".calendar-selected").removeClass("calendar-selected");
var _492=opts.current.getFullYear()+","+(opts.current.getMonth()+1)+","+opts.current.getDate();
t.find("td[abbr=\""+_492+"\"]").addClass("calendar-selected");
}
var _493=6-opts.firstDay;
var _494=_493+1;
if(_493>=7){
_493-=7;
}
if(_494>=7){
_494-=7;
}
t.find("tr").find("td:eq("+_493+")").addClass("calendar-saturday");
t.find("tr").find("td:eq("+_494+")").addClass("calendar-sunday");
t.find("td").hover(function(){
$(this).addClass("calendar-hover");
},function(){
$(this).removeClass("calendar-hover");
}).click(function(){
t.find(".calendar-selected").removeClass("calendar-selected");
$(this).addClass("calendar-selected");
var _495=$(this).attr("abbr").split(",");
opts.current=new Date(_495[0],parseInt(_495[1])-1,_495[2]);
opts.onSelect.call(_48f,opts.current);
});
};
$.fn.calendar=function(_496,_497){
if(typeof _496=="string"){
return $.fn.calendar.methods[_496](this,_497);
}
_496=_496||{};
return this.each(function(){
var _498=$.data(this,"calendar");
if(_498){
$.extend(_498.options,_496);
}else{
_498=$.data(this,"calendar",{options:$.extend({},$.fn.calendar.defaults,$.fn.calendar.parseOptions(this),_496)});
init(this);
}
if(_498.options.border==false){
$(this).addClass("calendar-noborder");
}
_474(this);
show(this);
$(this).find("div.calendar-menu").hide();
});
};
$.fn.calendar.methods={options:function(jq){
return $.data(jq[0],"calendar").options;
},resize:function(jq){
return jq.each(function(){
_474(this);
});
},moveTo:function(jq,date){
return jq.each(function(){
$(this).calendar({year:date.getFullYear(),month:date.getMonth()+1,current:date});
});
}};
$.fn.calendar.parseOptions=function(_499){
var t=$(_499);
return $.extend({},$.parser.parseOptions(_499,["width","height",{firstDay:"number",fit:"boolean",border:"boolean"}]));
};
$.fn.calendar.defaults={width:180,height:180,fit:false,border:true,firstDay:0,weeks:["S","M","T","W","T","F","S"],months:["Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"],year:new Date().getFullYear(),month:new Date().getMonth()+1,current:new Date(),onSelect:function(date){
}};
})(jQuery);
(function($){
function init(_49a){
var _49b=$("<span class=\"spinner\">"+"<span class=\"spinner-arrow\">"+"<span class=\"spinner-arrow-up\"></span>"+"<span class=\"spinner-arrow-down\"></span>"+"</span>"+"</span>").insertAfter(_49a);
$(_49a).addClass("spinner-text spinner-f").prependTo(_49b);
return _49b;
};
function _49c(_49d,_49e){
var opts=$.data(_49d,"spinner").options;
var _49f=$.data(_49d,"spinner").spinner;
if(_49e){
opts.width=_49e;
}
var _4a0=$("<div style=\"display:none\"></div>").insertBefore(_49f);
_49f.appendTo("body");
if(isNaN(opts.width)){
opts.width=$(_49d).outerWidth();
}
var _4a1=_49f.find(".spinner-arrow");
_49f._outerWidth(opts.width)._outerHeight(opts.height);
$(_49d)._outerWidth(_49f.width()-_4a1.outerWidth());
$(_49d).css({height:_49f.height()+"px",lineHeight:_49f.height()+"px"});
_4a1._outerHeight(_49f.height());
_4a1.find("span")._outerHeight(_4a1.height()/2);
_49f.insertAfter(_4a0);
_4a0.remove();
};
function _4a2(_4a3){
var opts=$.data(_4a3,"spinner").options;
var _4a4=$.data(_4a3,"spinner").spinner;
_4a4.find(".spinner-arrow-up,.spinner-arrow-down").unbind(".spinner");
if(!opts.disabled){
_4a4.find(".spinner-arrow-up").bind("mouseenter.spinner",function(){
$(this).addClass("spinner-arrow-hover");
}).bind("mouseleave.spinner",function(){
$(this).removeClass("spinner-arrow-hover");
}).bind("click.spinner",function(){
opts.spin.call(_4a3,false);
opts.onSpinUp.call(_4a3);
$(_4a3).validatebox("validate");
});
_4a4.find(".spinner-arrow-down").bind("mouseenter.spinner",function(){
$(this).addClass("spinner-arrow-hover");
}).bind("mouseleave.spinner",function(){
$(this).removeClass("spinner-arrow-hover");
}).bind("click.spinner",function(){
opts.spin.call(_4a3,true);
opts.onSpinDown.call(_4a3);
$(_4a3).validatebox("validate");
});
}
};
function _4a5(_4a6,_4a7){
var opts=$.data(_4a6,"spinner").options;
if(_4a7){
opts.disabled=true;
$(_4a6).attr("disabled",true);
}else{
opts.disabled=false;
$(_4a6).removeAttr("disabled");
}
};
$.fn.spinner=function(_4a8,_4a9){
if(typeof _4a8=="string"){
var _4aa=$.fn.spinner.methods[_4a8];
if(_4aa){
return _4aa(this,_4a9);
}else{
return this.validatebox(_4a8,_4a9);
}
}
_4a8=_4a8||{};
return this.each(function(){
var _4ab=$.data(this,"spinner");
if(_4ab){
$.extend(_4ab.options,_4a8);
}else{
_4ab=$.data(this,"spinner",{options:$.extend({},$.fn.spinner.defaults,$.fn.spinner.parseOptions(this),_4a8),spinner:init(this)});
$(this).removeAttr("disabled");
}
_4ab.options.originalValue=_4ab.options.value;
$(this).val(_4ab.options.value);
$(this).attr("readonly",!_4ab.options.editable);
_4a5(this,_4ab.options.disabled);
_49c(this);
$(this).validatebox(_4ab.options);
_4a2(this);
});
};
$.fn.spinner.methods={options:function(jq){
var opts=$.data(jq[0],"spinner").options;
return $.extend(opts,{value:jq.val()});
},destroy:function(jq){
return jq.each(function(){
var _4ac=$.data(this,"spinner").spinner;
$(this).validatebox("destroy");
_4ac.remove();
});
},resize:function(jq,_4ad){
return jq.each(function(){
_49c(this,_4ad);
});
},enable:function(jq){
return jq.each(function(){
_4a5(this,false);
_4a2(this);
});
},disable:function(jq){
return jq.each(function(){
_4a5(this,true);
_4a2(this);
});
},getValue:function(jq){
return jq.val();
},setValue:function(jq,_4ae){
return jq.each(function(){
var opts=$.data(this,"spinner").options;
opts.value=_4ae;
$(this).val(_4ae);
});
},clear:function(jq){
return jq.each(function(){
var opts=$.data(this,"spinner").options;
opts.value="";
$(this).val("");
});
},reset:function(jq){
return jq.each(function(){
var opts=$(this).spinner("options");
$(this).spinner("setValue",opts.originalValue);
});
}};
$.fn.spinner.parseOptions=function(_4af){
var t=$(_4af);
return $.extend({},$.fn.validatebox.parseOptions(_4af),$.parser.parseOptions(_4af,["width","height","min","max",{increment:"number",editable:"boolean"}]),{value:(t.val()||undefined),disabled:(t.attr("disabled")?true:undefined)});
};
$.fn.spinner.defaults=$.extend({},$.fn.validatebox.defaults,{width:"auto",height:22,deltaX:19,value:"",min:null,max:null,increment:1,editable:true,disabled:false,spin:function(down){
},onSpinUp:function(){
},onSpinDown:function(){
}});
})(jQuery);
(function($){
function _4b0(_4b1){
$(_4b1).addClass("numberspinner-f");
var opts=$.data(_4b1,"numberspinner").options;
$(_4b1).spinner(opts).numberbox(opts);
};
function _4b2(_4b3,down){
var opts=$.data(_4b3,"numberspinner").options;
var v=parseFloat($(_4b3).numberbox("getValue")||opts.value)||0;
if(down==true){
v-=opts.increment;
}else{
v+=opts.increment;
}
$(_4b3).numberbox("setValue",v);
};
$.fn.numberspinner=function(_4b4,_4b5){
if(typeof _4b4=="string"){
var _4b6=$.fn.numberspinner.methods[_4b4];
if(_4b6){
return _4b6(this,_4b5);
}else{
return this.spinner(_4b4,_4b5);
}
}
_4b4=_4b4||{};
return this.each(function(){
var _4b7=$.data(this,"numberspinner");
if(_4b7){
$.extend(_4b7.options,_4b4);
}else{
$.data(this,"numberspinner",{options:$.extend({},$.fn.numberspinner.defaults,$.fn.numberspinner.parseOptions(this),_4b4)});
}
_4b0(this);
});
};
$.fn.numberspinner.methods={options:function(jq){
var opts=$.data(jq[0],"numberspinner").options;
return $.extend(opts,{value:jq.numberbox("getValue"),originalValue:jq.numberbox("options").originalValue});
},setValue:function(jq,_4b8){
return jq.each(function(){
$(this).numberbox("setValue",_4b8);
});
},getValue:function(jq){
return jq.numberbox("getValue");
},clear:function(jq){
return jq.each(function(){
$(this).spinner("clear");
$(this).numberbox("clear");
});
},reset:function(jq){
return jq.each(function(){
var opts=$(this).numberspinner("options");
$(this).numberspinner("setValue",opts.originalValue);
});
}};
$.fn.numberspinner.parseOptions=function(_4b9){
return $.extend({},$.fn.spinner.parseOptions(_4b9),$.fn.numberbox.parseOptions(_4b9),{});
};
$.fn.numberspinner.defaults=$.extend({},$.fn.spinner.defaults,$.fn.numberbox.defaults,{spin:function(down){
_4b2(this,down);
}});
})(jQuery);
(function($){
function _4ba(_4bb){
var opts=$.data(_4bb,"timespinner").options;
$(_4bb).addClass("timespinner-f");
$(_4bb).spinner(opts);
$(_4bb).unbind(".timespinner");
$(_4bb).bind("click.timespinner",function(){
var _4bc=0;
if(this.selectionStart!=null){
_4bc=this.selectionStart;
}else{
if(this.createTextRange){
var _4bd=_4bb.createTextRange();
var s=document.selection.createRange();
s.setEndPoint("StartToStart",_4bd);
_4bc=s.text.length;
}
}
if(_4bc>=0&&_4bc<=2){
opts.highlight=0;
}else{
if(_4bc>=3&&_4bc<=5){
opts.highlight=1;
}else{
if(_4bc>=6&&_4bc<=8){
opts.highlight=2;
}
}
}
_4bf(_4bb);
}).bind("blur.timespinner",function(){
_4be(_4bb);
});
};
function _4bf(_4c0){
var opts=$.data(_4c0,"timespinner").options;
var _4c1=0,end=0;
if(opts.highlight==0){
_4c1=0;
end=2;
}else{
if(opts.highlight==1){
_4c1=3;
end=5;
}else{
if(opts.highlight==2){
_4c1=6;
end=8;
}
}
}
if(_4c0.selectionStart!=null){
_4c0.setSelectionRange(_4c1,end);
}else{
if(_4c0.createTextRange){
var _4c2=_4c0.createTextRange();
_4c2.collapse();
_4c2.moveEnd("character",end);
_4c2.moveStart("character",_4c1);
_4c2.select();
}
}
$(_4c0).focus();
};
function _4c3(_4c4,_4c5){
var opts=$.data(_4c4,"timespinner").options;
if(!_4c5){
return null;
}
var vv=_4c5.split(opts.separator);
for(var i=0;i<vv.length;i++){
if(isNaN(vv[i])){
return null;
}
}
while(vv.length<3){
vv.push(0);
}
return new Date(1900,0,0,vv[0],vv[1],vv[2]);
};
function _4be(_4c6){
var opts=$.data(_4c6,"timespinner").options;
var _4c7=$(_4c6).val();
var time=_4c3(_4c6,_4c7);
if(!time){
opts.value="";
$(_4c6).val("");
return;
}
var _4c8=_4c3(_4c6,opts.min);
var _4c9=_4c3(_4c6,opts.max);
if(_4c8&&_4c8>time){
time=_4c8;
}
if(_4c9&&_4c9<time){
time=_4c9;
}
var tt=[_4ca(time.getHours()),_4ca(time.getMinutes())];
if(opts.showSeconds){
tt.push(_4ca(time.getSeconds()));
}
var val=tt.join(opts.separator);
opts.value=val;
$(_4c6).val(val);
function _4ca(_4cb){
return (_4cb<10?"0":"")+_4cb;
};
};
function _4cc(_4cd,down){
var opts=$.data(_4cd,"timespinner").options;
var val=$(_4cd).val();
if(val==""){
val=[0,0,0].join(opts.separator);
}
var vv=val.split(opts.separator);
for(var i=0;i<vv.length;i++){
vv[i]=parseInt(vv[i],10);
}
if(down==true){
vv[opts.highlight]-=opts.increment;
}else{
vv[opts.highlight]+=opts.increment;
}
$(_4cd).val(vv.join(opts.separator));
_4be(_4cd);
_4bf(_4cd);
};
$.fn.timespinner=function(_4ce,_4cf){
if(typeof _4ce=="string"){
var _4d0=$.fn.timespinner.methods[_4ce];
if(_4d0){
return _4d0(this,_4cf);
}else{
return this.spinner(_4ce,_4cf);
}
}
_4ce=_4ce||{};
return this.each(function(){
var _4d1=$.data(this,"timespinner");
if(_4d1){
$.extend(_4d1.options,_4ce);
}else{
$.data(this,"timespinner",{options:$.extend({},$.fn.timespinner.defaults,$.fn.timespinner.parseOptions(this),_4ce)});
_4ba(this);
}
});
};
$.fn.timespinner.methods={options:function(jq){
var opts=$.data(jq[0],"timespinner").options;
return $.extend(opts,{value:jq.val(),originalValue:jq.spinner("options").originalValue});
},setValue:function(jq,_4d2){
return jq.each(function(){
$(this).val(_4d2);
_4be(this);
});
},getHours:function(jq){
var opts=$.data(jq[0],"timespinner").options;
var vv=jq.val().split(opts.separator);
return parseInt(vv[0],10);
},getMinutes:function(jq){
var opts=$.data(jq[0],"timespinner").options;
var vv=jq.val().split(opts.separator);
return parseInt(vv[1],10);
},getSeconds:function(jq){
var opts=$.data(jq[0],"timespinner").options;
var vv=jq.val().split(opts.separator);
return parseInt(vv[2],10)||0;
}};
$.fn.timespinner.parseOptions=function(_4d3){
return $.extend({},$.fn.spinner.parseOptions(_4d3),$.parser.parseOptions(_4d3,["separator",{showSeconds:"boolean",highlight:"number"}]));
};
$.fn.timespinner.defaults=$.extend({},$.fn.spinner.defaults,{separator:":",showSeconds:false,highlight:0,spin:function(down){
_4cc(this,down);
}});
})(jQuery);
(function($){
var _4d4=0;
function _4d5(a,o){
for(var i=0,len=a.length;i<len;i++){
if(a[i]==o){
return i;
}
}
return -1;
};
function _4d6(a,o,id){
if(typeof o=="string"){
for(var i=0,len=a.length;i<len;i++){
if(a[i][o]==id){
a.splice(i,1);
return;
}
}
}else{
var _4d7=_4d5(a,o);
if(_4d7!=-1){
a.splice(_4d7,1);
}
}
};
function _4d8(a,o,r){
for(var i=0,len=a.length;i<len;i++){
if(a[i][o]==r[o]){
return;
}
}
a.push(r);
};
function _4d9(_4da){
var cc=_4da||$("head");
var _4db=$.data(cc[0],"ss");
if(!_4db){
_4db=$.data(cc[0],"ss",{cache:{},dirty:[]});
}
return {add:function(_4dc){
var ss=["<style type=\"text/css\">"];
for(var i=0;i<_4dc.length;i++){
_4db.cache[_4dc[i][0]]={width:_4dc[i][1]};
}
var _4dd=0;
for(var s in _4db.cache){
var item=_4db.cache[s];
item.index=_4dd++;
ss.push(s+"{width:"+item.width+"}");
}
ss.push("</style>");
$(ss.join("\n")).appendTo(cc);
setTimeout(function(){
cc.children("style:not(:last)").remove();
},0);
},getRule:function(_4de){
var _4df=cc.children("style:last")[0];
var _4e0=_4df.styleSheet?_4df.styleSheet:(_4df.sheet||document.styleSheets[document.styleSheets.length-1]);
var _4e1=_4e0.cssRules||_4e0.rules;
return _4e1[_4de];
},set:function(_4e2,_4e3){
var item=_4db.cache[_4e2];
if(item){
item.width=_4e3;
var rule=this.getRule(item.index);
if(rule){
rule.style["width"]=_4e3;
}
}
},remove:function(_4e4){
var tmp=[];
for(var s in _4db.cache){
if(s.indexOf(_4e4)==-1){
tmp.push([s,_4db.cache[s].width]);
}
}
_4db.cache={};
this.add(tmp);
},dirty:function(_4e5){
if(_4e5){
_4db.dirty.push(_4e5);
}
},clean:function(){
for(var i=0;i<_4db.dirty.length;i++){
this.remove(_4db.dirty[i]);
}
_4db.dirty=[];
}};
};
function _4e6(_4e7,_4e8){
var opts=$.data(_4e7,"datagrid").options;
var _4e9=$.data(_4e7,"datagrid").panel;
if(_4e8){
if(_4e8.width){
opts.width=_4e8.width;
}
if(_4e8.height){
opts.height=_4e8.height;
}
}
if(opts.fit==true){
var p=_4e9.panel("panel").parent();
opts.width=p.width();
opts.height=p.height();
}
_4e9.panel("resize",{width:opts.width,height:opts.height});
};
function _4ea(_4eb){
var opts=$.data(_4eb,"datagrid").options;
var dc=$.data(_4eb,"datagrid").dc;
var wrap=$.data(_4eb,"datagrid").panel;
var _4ec=wrap.width();
var _4ed=wrap.height();
var view=dc.view;
var _4ee=dc.view1;
var _4ef=dc.view2;
var _4f0=_4ee.children("div.datagrid-header");
var _4f1=_4ef.children("div.datagrid-header");
var _4f2=_4f0.find("table");
var _4f3=_4f1.find("table");
view.width(_4ec);
var _4f4=_4f0.children("div.datagrid-header-inner").show();
_4ee.width(_4f4.find("table").width());
if(!opts.showHeader){
_4f4.hide();
}
_4ef.width(_4ec-_4ee._outerWidth());
_4ee.children("div.datagrid-header,div.datagrid-body,div.datagrid-footer").width(_4ee.width());
_4ef.children("div.datagrid-header,div.datagrid-body,div.datagrid-footer").width(_4ef.width());
var hh;
_4f0.css("height","");
_4f1.css("height","");
_4f2.css("height","");
_4f3.css("height","");
hh=Math.max(_4f2.height(),_4f3.height());
_4f2.height(hh);
_4f3.height(hh);
_4f0.add(_4f1)._outerHeight(hh);
if(opts.height!="auto"){
var _4f5=_4ed-_4ef.children("div.datagrid-header")._outerHeight()-_4ef.children("div.datagrid-footer")._outerHeight()-wrap.children("div.datagrid-toolbar")._outerHeight();
wrap.children("div.datagrid-pager").each(function(){
_4f5-=$(this)._outerHeight();
});
dc.body1.add(dc.body2).children("table.datagrid-btable-frozen").css({position:"absolute",top:dc.header2._outerHeight()});
var _4f6=dc.body2.children("table.datagrid-btable-frozen")._outerHeight();
_4ee.add(_4ef).children("div.datagrid-body").css({marginTop:_4f6,height:(_4f5-_4f6)});
}
view.height(_4ef.height());
};
function _4f7(_4f8,_4f9,_4fa){
var rows=$.data(_4f8,"datagrid").data.rows;
var opts=$.data(_4f8,"datagrid").options;
var dc=$.data(_4f8,"datagrid").dc;
if(!dc.body1.is(":empty")&&(!opts.nowrap||opts.autoRowHeight||_4fa)){
if(_4f9!=undefined){
var tr1=opts.finder.getTr(_4f8,_4f9,"body",1);
var tr2=opts.finder.getTr(_4f8,_4f9,"body",2);
_4fb(tr1,tr2);
}else{
var tr1=opts.finder.getTr(_4f8,0,"allbody",1);
var tr2=opts.finder.getTr(_4f8,0,"allbody",2);
_4fb(tr1,tr2);
if(opts.showFooter){
var tr1=opts.finder.getTr(_4f8,0,"allfooter",1);
var tr2=opts.finder.getTr(_4f8,0,"allfooter",2);
_4fb(tr1,tr2);
}
}
}
_4ea(_4f8);
if(opts.height=="auto"){
var _4fc=dc.body1.parent();
var _4fd=dc.body2;
var _4fe=_4ff(_4fd);
var _500=_4fe.height;
if(_4fe.width>_4fd.width()){
_500+=18;
}
_4fc.height(_500);
_4fd.height(_500);
dc.view.height(dc.view2.height());
}
dc.body2.triggerHandler("scroll");
function _4fb(trs1,trs2){
for(var i=0;i<trs2.length;i++){
var tr1=$(trs1[i]);
var tr2=$(trs2[i]);
tr1.css("height","");
tr2.css("height","");
var _501=Math.max(tr1.height(),tr2.height());
tr1.css("height",_501);
tr2.css("height",_501);
}
};
function _4ff(cc){
var _502=0;
var _503=0;
$(cc).children().each(function(){
var c=$(this);
if(c.is(":visible")){
_503+=c._outerHeight();
if(_502<c._outerWidth()){
_502=c._outerWidth();
}
}
});
return {width:_502,height:_503};
};
};
function _504(_505,_506){
var _507=$.data(_505,"datagrid");
var opts=_507.options;
var dc=_507.dc;
if(!dc.body2.children("table.datagrid-btable-frozen").length){
dc.body1.add(dc.body2).prepend("<table class=\"datagrid-btable datagrid-btable-frozen\" cellspacing=\"0\" cellpadding=\"0\"></table>");
}
_508(true);
_508(false);
_4ea(_505);
function _508(_509){
var _50a=_509?1:2;
var tr=opts.finder.getTr(_505,_506,"body",_50a);
(_509?dc.body1:dc.body2).children("table.datagrid-btable-frozen").append(tr);
};
};
function _50b(_50c,_50d){
function _50e(){
var _50f=[];
var _510=[];
$(_50c).children("thead").each(function(){
var opt=$.parser.parseOptions(this,[{frozen:"boolean"}]);
$(this).find("tr").each(function(){
var cols=[];
$(this).find("th").each(function(){
var th=$(this);
var col=$.extend({},$.parser.parseOptions(this,["field","align","halign","order",{sortable:"boolean",checkbox:"boolean",resizable:"boolean",fixed:"boolean"},{rowspan:"number",colspan:"number",width:"number"}]),{title:(th.html()||undefined),hidden:(th.attr("hidden")?true:undefined),formatter:(th.attr("formatter")?eval(th.attr("formatter")):undefined),styler:(th.attr("styler")?eval(th.attr("styler")):undefined),sorter:(th.attr("sorter")?eval(th.attr("sorter")):undefined)});
if(th.attr("editor")){
var s=$.trim(th.attr("editor"));
if(s.substr(0,1)=="{"){
col.editor=eval("("+s+")");
}else{
col.editor=s;
}
}
cols.push(col);
});
opt.frozen?_50f.push(cols):_510.push(cols);
});
});
return [_50f,_510];
};
var _511=$("<div class=\"datagrid-wrap\">"+"<div class=\"datagrid-view\">"+"<div class=\"datagrid-view1\">"+"<div class=\"datagrid-header\">"+"<div class=\"datagrid-header-inner\"></div>"+"</div>"+"<div class=\"datagrid-body\">"+"<div class=\"datagrid-body-inner\"></div>"+"</div>"+"<div class=\"datagrid-footer\">"+"<div class=\"datagrid-footer-inner\"></div>"+"</div>"+"</div>"+"<div class=\"datagrid-view2\">"+"<div class=\"datagrid-header\">"+"<div class=\"datagrid-header-inner\"></div>"+"</div>"+"<div class=\"datagrid-body\"></div>"+"<div class=\"datagrid-footer\">"+"<div class=\"datagrid-footer-inner\"></div>"+"</div>"+"</div>"+"</div>"+"</div>").insertAfter(_50c);
_511.panel({doSize:false});
_511.panel("panel").addClass("datagrid").bind("_resize",function(e,_512){
var opts=$.data(_50c,"datagrid").options;
if(opts.fit==true||_512){
_4e6(_50c);
setTimeout(function(){
if($.data(_50c,"datagrid")){
_513(_50c);
}
},0);
}
return false;
});
$(_50c).hide().appendTo(_511.children("div.datagrid-view"));
var cc=_50e();
var view=_511.children("div.datagrid-view");
var _514=view.children("div.datagrid-view1");
var _515=view.children("div.datagrid-view2");
var _516=_511.closest("div.datagrid-view");
if(!_516.length){
_516=view;
}
var ss=_4d9(_516);
return {panel:_511,frozenColumns:cc[0],columns:cc[1],dc:{view:view,view1:_514,view2:_515,header1:_514.children("div.datagrid-header").children("div.datagrid-header-inner"),header2:_515.children("div.datagrid-header").children("div.datagrid-header-inner"),body1:_514.children("div.datagrid-body").children("div.datagrid-body-inner"),body2:_515.children("div.datagrid-body"),footer1:_514.children("div.datagrid-footer").children("div.datagrid-footer-inner"),footer2:_515.children("div.datagrid-footer").children("div.datagrid-footer-inner")},ss:ss};
};
function _517(_518){
var _519=$.data(_518,"datagrid");
var opts=_519.options;
var dc=_519.dc;
var _51a=_519.panel;
_51a.panel($.extend({},opts,{id:null,doSize:false,onResize:function(_51b,_51c){
setTimeout(function(){
if($.data(_518,"datagrid")){
_4ea(_518);
_543(_518);
opts.onResize.call(_51a,_51b,_51c);
}
},0);
},onExpand:function(){
_4f7(_518);
opts.onExpand.call(_51a);
}}));
_519.rowIdPrefix="datagrid-row-r"+(++_4d4);
_519.cellClassPrefix="datagrid-cell-c"+_4d4;
_51d(dc.header1,opts.frozenColumns,true);
_51d(dc.header2,opts.columns,false);
_51e();
dc.header1.add(dc.header2).css("display",opts.showHeader?"block":"none");
dc.footer1.add(dc.footer2).css("display",opts.showFooter?"block":"none");
if(opts.toolbar){
if($.isArray(opts.toolbar)){
$("div.datagrid-toolbar",_51a).remove();
var tb=$("<div class=\"datagrid-toolbar\"><table cellspacing=\"0\" cellpadding=\"0\"><tr></tr></table></div>").prependTo(_51a);
var tr=tb.find("tr");
for(var i=0;i<opts.toolbar.length;i++){
var btn=opts.toolbar[i];
if(btn=="-"){
$("<td><div class=\"datagrid-btn-separator\"></div></td>").appendTo(tr);
}else{
var td=$("<td></td>").appendTo(tr);
var tool=$("<a href=\"javascript:void(0)\"></a>").appendTo(td);
tool[0].onclick=eval(btn.handler||function(){
});
tool.linkbutton($.extend({},btn,{plain:true}));
}
}
}else{
$(opts.toolbar).addClass("datagrid-toolbar").prependTo(_51a);
$(opts.toolbar).show();
}
}else{
$("div.datagrid-toolbar",_51a).remove();
}
$("div.datagrid-pager",_51a).remove();
if(opts.pagination){
var _51f=$("<div class=\"datagrid-pager\"></div>");
if(opts.pagePosition=="bottom"){
_51f.appendTo(_51a);
}else{
if(opts.pagePosition=="top"){
_51f.addClass("datagrid-pager-top").prependTo(_51a);
}else{
var ptop=$("<div class=\"datagrid-pager datagrid-pager-top\"></div>").prependTo(_51a);
_51f.appendTo(_51a);
_51f=_51f.add(ptop);
}
}
_51f.pagination({total:(opts.pageNumber*opts.pageSize),pageNumber:opts.pageNumber,pageSize:opts.pageSize,pageList:opts.pageList,onSelectPage:function(_520,_521){
opts.pageNumber=_520;
opts.pageSize=_521;
_51f.pagination("refresh",{pageNumber:_520,pageSize:_521});
_60a(_518);
}});
opts.pageSize=_51f.pagination("options").pageSize;
}
function _51d(_522,_523,_524){
if(!_523){
return;
}
$(_522).show();
$(_522).empty();
var _525=[];
var _526=[];
if(opts.sortName){
_525=opts.sortName.split(",");
_526=opts.sortOrder.split(",");
}
var t=$("<table class=\"datagrid-htable\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\"><tbody></tbody></table>").appendTo(_522);
for(var i=0;i<_523.length;i++){
var tr=$("<tr class=\"datagrid-header-row\"></tr>").appendTo($("tbody",t));
var cols=_523[i];
for(var j=0;j<cols.length;j++){
var col=cols[j];
var attr="";
if(col.rowspan){
attr+="rowspan=\""+col.rowspan+"\" ";
}
if(col.colspan){
attr+="colspan=\""+col.colspan+"\" ";
}
var td=$("<td "+attr+"></td>").appendTo(tr);
if(col.checkbox){
td.attr("field",col.field);
$("<div class=\"datagrid-header-check\"></div>").html("<input type=\"checkbox\"/>").appendTo(td);
}else{
if(col.field){
td.attr("field",col.field);
td.append("<div class=\"datagrid-cell\"><span></span><span class=\"datagrid-sort-icon\"></span></div>");
$("span",td).html(col.title);
$("span.datagrid-sort-icon",td).html("&nbsp;");
var cell=td.find("div.datagrid-cell");
var pos=_4d5(_525,col.field);
if(pos>=0){
cell.addClass("datagrid-sort-"+_526[pos]);
}
if(col.resizable==false){
cell.attr("resizable","false");
}
if(col.width){
cell._outerWidth(col.width);
col.boxWidth=parseInt(cell[0].style.width);
}else{
col.auto=true;
}
cell.css("text-align",(col.halign||col.align||""));
col.cellClass=_519.cellClassPrefix+"-"+col.field.replace(/[\.|\s]/g,"-");
cell.addClass(col.cellClass).css("width","");
}else{
$("<div class=\"datagrid-cell-group\"></div>").html(col.title).appendTo(td);
}
}
if(col.hidden){
td.hide();
}
}
}
if(_524&&opts.rownumbers){
var td=$("<td rowspan=\""+opts.frozenColumns.length+"\"><div class=\"datagrid-header-rownumber\"></div></td>");
if($("tr",t).length==0){
td.wrap("<tr class=\"datagrid-header-row\"></tr>").parent().appendTo($("tbody",t));
}else{
td.prependTo($("tr:first",t));
}
}
};
function _51e(){
var _527=[];
var _528=_529(_518,true).concat(_529(_518));
for(var i=0;i<_528.length;i++){
var col=_52a(_518,_528[i]);
if(col&&!col.checkbox){
_527.push(["."+col.cellClass,col.boxWidth?col.boxWidth+"px":"auto"]);
}
}
_519.ss.add(_527);
_519.ss.dirty(_519.cellSelectorPrefix);
_519.cellSelectorPrefix="."+_519.cellClassPrefix;
};
};
function _52b(_52c){
var _52d=$.data(_52c,"datagrid");
var _52e=_52d.panel;
var opts=_52d.options;
var dc=_52d.dc;
var _52f=dc.header1.add(dc.header2);
_52f.find("input[type=checkbox]").unbind(".datagrid").bind("click.datagrid",function(e){
if(opts.singleSelect&&opts.selectOnCheck){
return false;
}
if($(this).is(":checked")){
_5a5(_52c);
}else{
_5ab(_52c);
}
e.stopPropagation();
});
var _530=_52f.find("div.datagrid-cell");
_530.closest("td").unbind(".datagrid").bind("mouseenter.datagrid",function(){
if(_52d.resizing){
return;
}
$(this).addClass("datagrid-header-over");
}).bind("mouseleave.datagrid",function(){
$(this).removeClass("datagrid-header-over");
}).bind("contextmenu.datagrid",function(e){
var _531=$(this).attr("field");
opts.onHeaderContextMenu.call(_52c,e,_531);
});
_530.unbind(".datagrid").bind("click.datagrid",function(e){
var p1=$(this).offset().left+5;
var p2=$(this).offset().left+$(this)._outerWidth()-5;
if(e.pageX<p2&&e.pageX>p1){
var _532=$(this).parent().attr("field");
var col=_52a(_52c,_532);
if(!col.sortable||_52d.resizing){
return;
}
var _533=[];
var _534=[];
if(opts.sortName){
_533=opts.sortName.split(",");
_534=opts.sortOrder.split(",");
}
var pos=_4d5(_533,_532);
var _535=col.order||"asc";
if(pos>=0){
$(this).removeClass("datagrid-sort-asc datagrid-sort-desc");
var _536=_534[pos]=="asc"?"desc":"asc";
if(opts.multiSort&&_536==_535){
_533.splice(pos,1);
_534.splice(pos,1);
}else{
_534[pos]=_536;
$(this).addClass("datagrid-sort-"+_536);
}
}else{
if(opts.multiSort){
_533.push(_532);
_534.push(_535);
}else{
_533=[_532];
_534=[_535];
_530.removeClass("datagrid-sort-asc datagrid-sort-desc");
}
$(this).addClass("datagrid-sort-"+_535);
}
opts.sortName=_533.join(",");
opts.sortOrder=_534.join(",");
if(opts.remoteSort){
_60a(_52c);
}else{
var data=$.data(_52c,"datagrid").data;
_572(_52c,data);
}
opts.onSortColumn.call(_52c,opts.sortName,opts.sortOrder);
}
}).bind("dblclick.datagrid",function(e){
var p1=$(this).offset().left+5;
var p2=$(this).offset().left+$(this)._outerWidth()-5;
var cond=opts.resizeHandle=="right"?(e.pageX>p2):(opts.resizeHandle=="left"?(e.pageX<p1):(e.pageX<p1||e.pageX>p2));
if(cond){
var _537=$(this).parent().attr("field");
var col=_52a(_52c,_537);
if(col.resizable==false){
return;
}
$(_52c).datagrid("autoSizeColumn",_537);
col.auto=false;
}
});
var _538=opts.resizeHandle=="right"?"e":(opts.resizeHandle=="left"?"w":"e,w");
_530.each(function(){
$(this).resizable({handles:_538,disabled:($(this).attr("resizable")?$(this).attr("resizable")=="false":false),minWidth:25,onStartResize:function(e){
_52d.resizing=true;
_52f.css("cursor",$("body").css("cursor"));
if(!_52d.proxy){
_52d.proxy=$("<div class=\"datagrid-resize-proxy\"></div>").appendTo(dc.view);
}
_52d.proxy.css({left:e.pageX-$(_52e).offset().left-1,display:"none"});
setTimeout(function(){
if(_52d.proxy){
_52d.proxy.show();
}
},500);
},onResize:function(e){
_52d.proxy.css({left:e.pageX-$(_52e).offset().left-1,display:"block"});
return false;
},onStopResize:function(e){
_52f.css("cursor","");
$(this).css("height","");
$(this)._outerWidth($(this)._outerWidth());
var _539=$(this).parent().attr("field");
var col=_52a(_52c,_539);
col.width=$(this)._outerWidth();
col.boxWidth=parseInt(this.style.width);
col.auto=undefined;
$(this).css("width","");
_513(_52c,_539);
_52d.proxy.remove();
_52d.proxy=null;
if($(this).parents("div:first.datagrid-header").parent().hasClass("datagrid-view1")){
_4ea(_52c);
}
_543(_52c);
opts.onResizeColumn.call(_52c,_539,col.width);
setTimeout(function(){
_52d.resizing=false;
},0);
}});
});
dc.body1.add(dc.body2).unbind().bind("mouseover",function(e){
if(_52d.resizing){
return;
}
var tr=$(e.target).closest("tr.datagrid-row");
if(!_53a(tr)){
return;
}
var _53b=_53c(tr);
_58d(_52c,_53b);
e.stopPropagation();
}).bind("mouseout",function(e){
var tr=$(e.target).closest("tr.datagrid-row");
if(!_53a(tr)){
return;
}
var _53d=_53c(tr);
opts.finder.getTr(_52c,_53d).removeClass("datagrid-row-over");
e.stopPropagation();
}).bind("click",function(e){
var tt=$(e.target);
var tr=tt.closest("tr.datagrid-row");
if(!_53a(tr)){
return;
}
var _53e=_53c(tr);
if(tt.parent().hasClass("datagrid-cell-check")){
if(opts.singleSelect&&opts.selectOnCheck){
if(!opts.checkOnSelect){
_5ab(_52c,true);
}
_598(_52c,_53e);
}else{
if(tt.is(":checked")){
_598(_52c,_53e);
}else{
_59f(_52c,_53e);
}
}
}else{
var row=opts.finder.getRow(_52c,_53e);
var td=tt.closest("td[field]",tr);
if(td.length){
var _53f=td.attr("field");
opts.onClickCell.call(_52c,_53e,_53f,row[_53f]);
}
if(opts.singleSelect==true){
_591(_52c,_53e);
}else{
if(tr.hasClass("datagrid-row-selected")){
_599(_52c,_53e);
}else{
_591(_52c,_53e);
}
}
opts.onClickRow.call(_52c,_53e,row);
}
e.stopPropagation();
}).bind("dblclick",function(e){
var tt=$(e.target);
var tr=tt.closest("tr.datagrid-row");
if(!_53a(tr)){
return;
}
var _540=_53c(tr);
var row=opts.finder.getRow(_52c,_540);
var td=tt.closest("td[field]",tr);
if(td.length){
var _541=td.attr("field");
opts.onDblClickCell.call(_52c,_540,_541,row[_541]);
}
opts.onDblClickRow.call(_52c,_540,row);
e.stopPropagation();
}).bind("contextmenu",function(e){
var tr=$(e.target).closest("tr.datagrid-row");
if(!_53a(tr)){
return;
}
var _542=_53c(tr);
var row=opts.finder.getRow(_52c,_542);
opts.onRowContextMenu.call(_52c,e,_542,row);
e.stopPropagation();
});
dc.body2.bind("scroll",function(){
var b1=dc.view1.children("div.datagrid-body");
b1.scrollTop($(this).scrollTop());
var c1=dc.body1.children(":first");
var c2=dc.body2.children(":first");
if(c1.length&&c2.length){
var top1=c1.offset().top;
var top2=c2.offset().top;
if(top1!=top2){
b1.scrollTop(b1.scrollTop()+top1-top2);
}
}
dc.view2.children("div.datagrid-header,div.datagrid-footer")._scrollLeft($(this)._scrollLeft());
dc.body2.children("table.datagrid-btable-frozen").css("left",-$(this)._scrollLeft());
});
function _53c(tr){
if(tr.attr("datagrid-row-index")){
return parseInt(tr.attr("datagrid-row-index"));
}else{
return tr.attr("node-id");
}
};
function _53a(tr){
return tr.length&&tr.parent().length;
};
};
function _543(_544){
var _545=$.data(_544,"datagrid");
var opts=_545.options;
var dc=_545.dc;
dc.body2.css("overflow-x",opts.fitColumns?"hidden":"");
if(!opts.fitColumns){
return;
}
if(!_545.leftWidth){
_545.leftWidth=0;
}
var _546=dc.view2.children("div.datagrid-header");
var _547=0;
var _548;
var _549=_529(_544,false);
for(var i=0;i<_549.length;i++){
var col=_52a(_544,_549[i]);
if(_54a(col)){
_547+=col.width;
_548=col;
}
}
if(!_547){
return;
}
if(_548){
_54b(_548,-_545.leftWidth);
}
var _54c=_546.children("div.datagrid-header-inner").show();
var _54d=_546.width()-_546.find("table").width()-opts.scrollbarSize+_545.leftWidth;
var rate=_54d/_547;
if(!opts.showHeader){
_54c.hide();
}
for(var i=0;i<_549.length;i++){
var col=_52a(_544,_549[i]);
if(_54a(col)){
var _54e=parseInt(col.width*rate);
_54b(col,_54e);
_54d-=_54e;
}
}
_545.leftWidth=_54d;
if(_548){
_54b(_548,_545.leftWidth);
}
_513(_544);
function _54b(col,_54f){
col.width+=_54f;
col.boxWidth+=_54f;
};
function _54a(col){
if(!col.hidden&&!col.checkbox&&!col.auto&&!col.fixed){
return true;
}
};
};
function _550(_551,_552){
var _553=$.data(_551,"datagrid");
var opts=_553.options;
var dc=_553.dc;
var tmp=$("<div class=\"datagrid-cell\" style=\"position:absolute;left:-9999px\"></div>").appendTo("body");
if(_552){
_4e6(_552);
if(opts.fitColumns){
_4ea(_551);
_543(_551);
}
}else{
var _554=false;
var _555=_529(_551,true).concat(_529(_551,false));
for(var i=0;i<_555.length;i++){
var _552=_555[i];
var col=_52a(_551,_552);
if(col.auto){
_4e6(_552);
_554=true;
}
}
if(_554&&opts.fitColumns){
_4ea(_551);
_543(_551);
}
}
tmp.remove();
function _4e6(_556){
var _557=dc.view.find("div.datagrid-header td[field=\""+_556+"\"] div.datagrid-cell");
_557.css("width","");
var col=$(_551).datagrid("getColumnOption",_556);
col.width=undefined;
col.boxWidth=undefined;
col.auto=true;
$(_551).datagrid("fixColumnSize",_556);
var _558=Math.max(_559("header"),_559("allbody"),_559("allfooter"));
_557._outerWidth(_558);
col.width=_558;
col.boxWidth=parseInt(_557[0].style.width);
_557.css("width","");
$(_551).datagrid("fixColumnSize",_556);
opts.onResizeColumn.call(_551,_556,col.width);
function _559(type){
var _55a=0;
if(type=="header"){
_55a=_55b(_557);
}else{
opts.finder.getTr(_551,0,type).find("td[field=\""+_556+"\"] div.datagrid-cell").each(function(){
var w=_55b($(this));
if(_55a<w){
_55a=w;
}
});
}
return _55a;
function _55b(cell){
return cell.is(":visible")?cell._outerWidth():tmp.html(cell.html())._outerWidth();
};
};
};
};
function _513(_55c,_55d){
var _55e=$.data(_55c,"datagrid");
var opts=_55e.options;
var dc=_55e.dc;
var _55f=dc.view.find("table.datagrid-btable,table.datagrid-ftable");
_55f.css("table-layout","fixed");
if(_55d){
fix(_55d);
}else{
var ff=_529(_55c,true).concat(_529(_55c,false));
for(var i=0;i<ff.length;i++){
fix(ff[i]);
}
}
_55f.css("table-layout","auto");
_560(_55c);
setTimeout(function(){
_4f7(_55c);
_565(_55c);
},0);
function fix(_561){
var col=_52a(_55c,_561);
if(!col.checkbox){
_55e.ss.set("."+col.cellClass,col.boxWidth?col.boxWidth+"px":"auto");
}
};
};
function _560(_562){
var dc=$.data(_562,"datagrid").dc;
dc.body1.add(dc.body2).find("td.datagrid-td-merged").each(function(){
var td=$(this);
var _563=td.attr("colspan")||1;
var _564=_52a(_562,td.attr("field")).width;
for(var i=1;i<_563;i++){
td=td.next();
_564+=_52a(_562,td.attr("field")).width+1;
}
$(this).children("div.datagrid-cell")._outerWidth(_564);
});
};
function _565(_566){
var dc=$.data(_566,"datagrid").dc;
dc.view.find("div.datagrid-editable").each(function(){
var cell=$(this);
var _567=cell.parent().attr("field");
var col=$(_566).datagrid("getColumnOption",_567);
cell._outerWidth(col.width);
var ed=$.data(this,"datagrid.editor");
if(ed.actions.resize){
ed.actions.resize(ed.target,cell.width());
}
});
};
function _52a(_568,_569){
function find(_56a){
if(_56a){
for(var i=0;i<_56a.length;i++){
var cc=_56a[i];
for(var j=0;j<cc.length;j++){
var c=cc[j];
if(c.field==_569){
return c;
}
}
}
}
return null;
};
var opts=$.data(_568,"datagrid").options;
var col=find(opts.columns);
if(!col){
col=find(opts.frozenColumns);
}
return col;
};
function _529(_56b,_56c){
var opts=$.data(_56b,"datagrid").options;
var _56d=(_56c==true)?(opts.frozenColumns||[[]]):opts.columns;
if(_56d.length==0){
return [];
}
var _56e=[];
function _56f(_570){
var c=0;
var i=0;
while(true){
if(_56e[i]==undefined){
if(c==_570){
return i;
}
c++;
}
i++;
}
};
function _571(r){
var ff=[];
var c=0;
for(var i=0;i<_56d[r].length;i++){
var col=_56d[r][i];
if(col.field){
ff.push([c,col.field]);
}
c+=parseInt(col.colspan||"1");
}
for(var i=0;i<ff.length;i++){
ff[i][0]=_56f(ff[i][0]);
}
for(var i=0;i<ff.length;i++){
var f=ff[i];
_56e[f[0]]=f[1];
}
};
for(var i=0;i<_56d.length;i++){
_571(i);
}
return _56e;
};
function _572(_573,data){
var _574=$.data(_573,"datagrid");
var opts=_574.options;
var dc=_574.dc;
data=opts.loadFilter.call(_573,data);
data.total=parseInt(data.total);
_574.data=data;
if(data.footer){
_574.footer=data.footer;
}
if(!opts.remoteSort&&opts.sortName){
var _575=opts.sortName.split(",");
var _576=opts.sortOrder.split(",");
data.rows.sort(function(r1,r2){
var r=0;
for(var i=0;i<_575.length;i++){
var sn=_575[i];
var so=_576[i];
var col=_52a(_573,sn);
var _577=col.sorter||function(a,b){
return a==b?0:(a>b?1:-1);
};
r=_577(r1[sn],r2[sn])*(so=="asc"?1:-1);
if(r!=0){
return r;
}
}
return r;
});
}
if(opts.view.onBeforeRender){
opts.view.onBeforeRender.call(opts.view,_573,data.rows);
}
opts.view.render.call(opts.view,_573,dc.body2,false);
opts.view.render.call(opts.view,_573,dc.body1,true);
if(opts.showFooter){
opts.view.renderFooter.call(opts.view,_573,dc.footer2,false);
opts.view.renderFooter.call(opts.view,_573,dc.footer1,true);
}
if(opts.view.onAfterRender){
opts.view.onAfterRender.call(opts.view,_573);
}
_574.ss.clean();
opts.onLoadSuccess.call(_573,data);
var _578=$(_573).datagrid("getPager");
if(_578.length){
var _579=_578.pagination("options");
if(_579.total!=data.total){
_578.pagination("refresh",{total:data.total});
if(opts.pageNumber!=_579.pageNumber){
opts.pageNumber=_579.pageNumber;
_60a(_573);
}
}
}
_4f7(_573);
dc.body2.triggerHandler("scroll");
_57a();
$(_573).datagrid("autoSizeColumn");
function _57a(){
if(opts.idField){
for(var i=0;i<data.rows.length;i++){
var row=data.rows[i];
if(_57b(_574.selectedRows,row)){
opts.finder.getTr(_573,i).addClass("datagrid-row-selected");
}
if(_57b(_574.checkedRows,row)){
opts.finder.getTr(_573,i).find("div.datagrid-cell-check input[type=checkbox]")._propAttr("checked",true);
}
}
}
function _57b(a,r){
for(var i=0;i<a.length;i++){
if(a[i][opts.idField]==r[opts.idField]){
a[i]=r;
return true;
}
}
return false;
};
};
};
function _57c(_57d,row){
var _57e=$.data(_57d,"datagrid");
var opts=_57e.options;
var rows=_57e.data.rows;
if(typeof row=="object"){
return _4d5(rows,row);
}else{
for(var i=0;i<rows.length;i++){
if(rows[i][opts.idField]==row){
return i;
}
}
return -1;
}
};
function _57f(_580){
var _581=$.data(_580,"datagrid");
var opts=_581.options;
var data=_581.data;
if(opts.idField){
return _581.selectedRows;
}else{
var rows=[];
opts.finder.getTr(_580,"","selected",2).each(function(){
var _582=parseInt($(this).attr("datagrid-row-index"));
rows.push(data.rows[_582]);
});
return rows;
}
};
function _583(_584){
var _585=$.data(_584,"datagrid");
var opts=_585.options;
if(opts.idField){
return _585.checkedRows;
}else{
var rows=[];
opts.finder.getTr(_584,"","checked",2).each(function(){
rows.push(opts.finder.getRow(_584,$(this)));
});
return rows;
}
};
function _586(_587,_588){
var _589=$.data(_587,"datagrid");
var dc=_589.dc;
var opts=_589.options;
var tr=opts.finder.getTr(_587,_588);
if(tr.length){
if(tr.closest("table").hasClass("datagrid-btable-frozen")){
return;
}
var _58a=dc.view2.children("div.datagrid-header")._outerHeight();
var _58b=dc.body2;
var _58c=_58b.outerHeight(true)-_58b.outerHeight();
var top=tr.position().top-_58a-_58c;
if(top<0){
_58b.scrollTop(_58b.scrollTop()+top);
}else{
if(top+tr._outerHeight()>_58b.height()-18){
_58b.scrollTop(_58b.scrollTop()+top+tr._outerHeight()-_58b.height()+18);
}
}
}
};
function _58d(_58e,_58f){
var _590=$.data(_58e,"datagrid");
var opts=_590.options;
opts.finder.getTr(_58e,_590.highlightIndex).removeClass("datagrid-row-over");
opts.finder.getTr(_58e,_58f).addClass("datagrid-row-over");
_590.highlightIndex=_58f;
};
function _591(_592,_593,_594){
var _595=$.data(_592,"datagrid");
var dc=_595.dc;
var opts=_595.options;
var _596=_595.selectedRows;
if(opts.singleSelect){
_597(_592);
_596.splice(0,_596.length);
}
if(!_594&&opts.checkOnSelect){
_598(_592,_593,true);
}
var row=opts.finder.getRow(_592,_593);
if(opts.idField){
_4d8(_596,opts.idField,row);
}
opts.finder.getTr(_592,_593).addClass("datagrid-row-selected");
opts.onSelect.call(_592,_593,row);
_586(_592,_593);
};
function _599(_59a,_59b,_59c){
var _59d=$.data(_59a,"datagrid");
var dc=_59d.dc;
var opts=_59d.options;
var _59e=$.data(_59a,"datagrid").selectedRows;
if(!_59c&&opts.checkOnSelect){
_59f(_59a,_59b,true);
}
opts.finder.getTr(_59a,_59b).removeClass("datagrid-row-selected");
var row=opts.finder.getRow(_59a,_59b);
if(opts.idField){
_4d6(_59e,opts.idField,row[opts.idField]);
}
opts.onUnselect.call(_59a,_59b,row);
};
function _5a0(_5a1,_5a2){
var _5a3=$.data(_5a1,"datagrid");
var opts=_5a3.options;
var rows=_5a3.data.rows;
var _5a4=$.data(_5a1,"datagrid").selectedRows;
if(!_5a2&&opts.checkOnSelect){
_5a5(_5a1,true);
}
opts.finder.getTr(_5a1,"","allbody").addClass("datagrid-row-selected");
if(opts.idField){
for(var _5a6=0;_5a6<rows.length;_5a6++){
_4d8(_5a4,opts.idField,rows[_5a6]);
}
}
opts.onSelectAll.call(_5a1,rows);
};
function _597(_5a7,_5a8){
var _5a9=$.data(_5a7,"datagrid");
var opts=_5a9.options;
var rows=_5a9.data.rows;
var _5aa=$.data(_5a7,"datagrid").selectedRows;
if(!_5a8&&opts.checkOnSelect){
_5ab(_5a7,true);
}
opts.finder.getTr(_5a7,"","selected").removeClass("datagrid-row-selected");
if(opts.idField){
for(var _5ac=0;_5ac<rows.length;_5ac++){
_4d6(_5aa,opts.idField,rows[_5ac][opts.idField]);
}
}
opts.onUnselectAll.call(_5a7,rows);
};
function _598(_5ad,_5ae,_5af){
var _5b0=$.data(_5ad,"datagrid");
var opts=_5b0.options;
if(!_5af&&opts.selectOnCheck){
_591(_5ad,_5ae,true);
}
var tr=opts.finder.getTr(_5ad,_5ae).addClass("datagrid-row-checked");
var ck=tr.find("div.datagrid-cell-check input[type=checkbox]");
ck._propAttr("checked",true);
tr=opts.finder.getTr(_5ad,"","checked",2);
if(tr.length==_5b0.data.rows.length){
var dc=_5b0.dc;
var _5b1=dc.header1.add(dc.header2);
_5b1.find("input[type=checkbox]")._propAttr("checked",true);
}
var row=opts.finder.getRow(_5ad,_5ae);
if(opts.idField){
_4d8(_5b0.checkedRows,opts.idField,row);
}
opts.onCheck.call(_5ad,_5ae,row);
};
function _59f(_5b2,_5b3,_5b4){
var _5b5=$.data(_5b2,"datagrid");
var opts=_5b5.options;
if(!_5b4&&opts.selectOnCheck){
_599(_5b2,_5b3,true);
}
var tr=opts.finder.getTr(_5b2,_5b3).removeClass("datagrid-row-checked");
var ck=tr.find("div.datagrid-cell-check input[type=checkbox]");
ck._propAttr("checked",false);
var dc=_5b5.dc;
var _5b6=dc.header1.add(dc.header2);
_5b6.find("input[type=checkbox]")._propAttr("checked",false);
var row=opts.finder.getRow(_5b2,_5b3);
if(opts.idField){
_4d6(_5b5.checkedRows,opts.idField,row[opts.idField]);
}
opts.onUncheck.call(_5b2,_5b3,row);
};
function _5a5(_5b7,_5b8){
var _5b9=$.data(_5b7,"datagrid");
var opts=_5b9.options;
var rows=_5b9.data.rows;
if(!_5b8&&opts.selectOnCheck){
_5a0(_5b7,true);
}
var dc=_5b9.dc;
var hck=dc.header1.add(dc.header2).find("input[type=checkbox]");
var bck=opts.finder.getTr(_5b7,"","allbody").addClass("datagrid-row-checked").find("div.datagrid-cell-check input[type=checkbox]");
hck.add(bck)._propAttr("checked",true);
if(opts.idField){
for(var i=0;i<rows.length;i++){
_4d8(_5b9.checkedRows,opts.idField,rows[i]);
}
}
opts.onCheckAll.call(_5b7,rows);
};
function _5ab(_5ba,_5bb){
var _5bc=$.data(_5ba,"datagrid");
var opts=_5bc.options;
var rows=_5bc.data.rows;
if(!_5bb&&opts.selectOnCheck){
_597(_5ba,true);
}
var dc=_5bc.dc;
var hck=dc.header1.add(dc.header2).find("input[type=checkbox]");
var bck=opts.finder.getTr(_5ba,"","checked").removeClass("datagrid-row-checked").find("div.datagrid-cell-check input[type=checkbox]");
hck.add(bck)._propAttr("checked",false);
if(opts.idField){
for(var i=0;i<rows.length;i++){
_4d6(_5bc.checkedRows,opts.idField,rows[i][opts.idField]);
}
}
opts.onUncheckAll.call(_5ba,rows);
};
function _5bd(_5be,_5bf){
var opts=$.data(_5be,"datagrid").options;
var tr=opts.finder.getTr(_5be,_5bf);
var row=opts.finder.getRow(_5be,_5bf);
if(tr.hasClass("datagrid-row-editing")){
return;
}
if(opts.onBeforeEdit.call(_5be,_5bf,row)==false){
return;
}
tr.addClass("datagrid-row-editing");
_5c0(_5be,_5bf);
_565(_5be);
tr.find("div.datagrid-editable").each(function(){
var _5c1=$(this).parent().attr("field");
var ed=$.data(this,"datagrid.editor");
ed.actions.setValue(ed.target,row[_5c1]);
});
_5c2(_5be,_5bf);
};
function _5c3(_5c4,_5c5,_5c6){
var opts=$.data(_5c4,"datagrid").options;
var _5c7=$.data(_5c4,"datagrid").updatedRows;
var _5c8=$.data(_5c4,"datagrid").insertedRows;
var tr=opts.finder.getTr(_5c4,_5c5);
var row=opts.finder.getRow(_5c4,_5c5);
if(!tr.hasClass("datagrid-row-editing")){
return;
}
if(!_5c6){
if(!_5c2(_5c4,_5c5)){
return;
}
var _5c9=false;
var _5ca={};
tr.find("div.datagrid-editable").each(function(){
var _5cb=$(this).parent().attr("field");
var ed=$.data(this,"datagrid.editor");
var _5cc=ed.actions.getValue(ed.target);
if(row[_5cb]!=_5cc){
row[_5cb]=_5cc;
_5c9=true;
_5ca[_5cb]=_5cc;
}
});
if(_5c9){
if(_4d5(_5c8,row)==-1){
if(_4d5(_5c7,row)==-1){
_5c7.push(row);
}
}
}
}
tr.removeClass("datagrid-row-editing");
_5cd(_5c4,_5c5);
$(_5c4).datagrid("refreshRow",_5c5);
if(!_5c6){
opts.onAfterEdit.call(_5c4,_5c5,row,_5ca);
}else{
opts.onCancelEdit.call(_5c4,_5c5,row);
}
};
function _5ce(_5cf,_5d0){
var opts=$.data(_5cf,"datagrid").options;
var tr=opts.finder.getTr(_5cf,_5d0);
var _5d1=[];
tr.children("td").each(function(){
var cell=$(this).find("div.datagrid-editable");
if(cell.length){
var ed=$.data(cell[0],"datagrid.editor");
_5d1.push(ed);
}
});
return _5d1;
};
function _5d2(_5d3,_5d4){
var _5d5=_5ce(_5d3,_5d4.index!=undefined?_5d4.index:_5d4.id);
for(var i=0;i<_5d5.length;i++){
if(_5d5[i].field==_5d4.field){
return _5d5[i];
}
}
return null;
};
function _5c0(_5d6,_5d7){
var opts=$.data(_5d6,"datagrid").options;
var tr=opts.finder.getTr(_5d6,_5d7);
tr.children("td").each(function(){
var cell=$(this).find("div.datagrid-cell");
var _5d8=$(this).attr("field");
var col=_52a(_5d6,_5d8);
if(col&&col.editor){
var _5d9,_5da;
if(typeof col.editor=="string"){
_5d9=col.editor;
}else{
_5d9=col.editor.type;
_5da=col.editor.options;
}
var _5db=opts.editors[_5d9];
if(_5db){
var _5dc=cell.html();
var _5dd=cell._outerWidth();
cell.addClass("datagrid-editable");
cell._outerWidth(_5dd);
cell.html("<table border=\"0\" cellspacing=\"0\" cellpadding=\"1\"><tr><td></td></tr></table>");
cell.children("table").bind("click dblclick contextmenu",function(e){
e.stopPropagation();
});
$.data(cell[0],"datagrid.editor",{actions:_5db,target:_5db.init(cell.find("td"),_5da),field:_5d8,type:_5d9,oldHtml:_5dc});
}
}
});
_4f7(_5d6,_5d7,true);
};
function _5cd(_5de,_5df){
var opts=$.data(_5de,"datagrid").options;
var tr=opts.finder.getTr(_5de,_5df);
tr.children("td").each(function(){
var cell=$(this).find("div.datagrid-editable");
if(cell.length){
var ed=$.data(cell[0],"datagrid.editor");
if(ed.actions.destroy){
ed.actions.destroy(ed.target);
}
cell.html(ed.oldHtml);
$.removeData(cell[0],"datagrid.editor");
cell.removeClass("datagrid-editable");
cell.css("width","");
}
});
};
function _5c2(_5e0,_5e1){
var tr=$.data(_5e0,"datagrid").options.finder.getTr(_5e0,_5e1);
if(!tr.hasClass("datagrid-row-editing")){
return true;
}
var vbox=tr.find(".validatebox-text");
vbox.validatebox("validate");
vbox.trigger("mouseleave");
var _5e2=tr.find(".validatebox-invalid");
return _5e2.length==0;
};
function _5e3(_5e4,_5e5){
var _5e6=$.data(_5e4,"datagrid").insertedRows;
var _5e7=$.data(_5e4,"datagrid").deletedRows;
var _5e8=$.data(_5e4,"datagrid").updatedRows;
if(!_5e5){
var rows=[];
rows=rows.concat(_5e6);
rows=rows.concat(_5e7);
rows=rows.concat(_5e8);
return rows;
}else{
if(_5e5=="inserted"){
return _5e6;
}else{
if(_5e5=="deleted"){
return _5e7;
}else{
if(_5e5=="updated"){
return _5e8;
}
}
}
}
return [];
};
function _5e9(_5ea,_5eb){
var _5ec=$.data(_5ea,"datagrid");
var opts=_5ec.options;
var data=_5ec.data;
var _5ed=_5ec.insertedRows;
var _5ee=_5ec.deletedRows;
$(_5ea).datagrid("cancelEdit",_5eb);
var row=data.rows[_5eb];
if(_4d5(_5ed,row)>=0){
_4d6(_5ed,row);
}else{
_5ee.push(row);
}
_4d6(_5ec.selectedRows,opts.idField,data.rows[_5eb][opts.idField]);
_4d6(_5ec.checkedRows,opts.idField,data.rows[_5eb][opts.idField]);
opts.view.deleteRow.call(opts.view,_5ea,_5eb);
if(opts.height=="auto"){
_4f7(_5ea);
}
$(_5ea).datagrid("getPager").pagination("refresh",{total:data.total});
};
function _5ef(_5f0,_5f1){
var data=$.data(_5f0,"datagrid").data;
var view=$.data(_5f0,"datagrid").options.view;
var _5f2=$.data(_5f0,"datagrid").insertedRows;
view.insertRow.call(view,_5f0,_5f1.index,_5f1.row);
_5f2.push(_5f1.row);
$(_5f0).datagrid("getPager").pagination("refresh",{total:data.total});
};
function _5f3(_5f4,row){
var data=$.data(_5f4,"datagrid").data;
var view=$.data(_5f4,"datagrid").options.view;
var _5f5=$.data(_5f4,"datagrid").insertedRows;
view.insertRow.call(view,_5f4,null,row);
_5f5.push(row);
$(_5f4).datagrid("getPager").pagination("refresh",{total:data.total});
};
function _5f6(_5f7){
var _5f8=$.data(_5f7,"datagrid");
var data=_5f8.data;
var rows=data.rows;
var _5f9=[];
for(var i=0;i<rows.length;i++){
_5f9.push($.extend({},rows[i]));
}
_5f8.originalRows=_5f9;
_5f8.updatedRows=[];
_5f8.insertedRows=[];
_5f8.deletedRows=[];
};
function _5fa(_5fb){
var data=$.data(_5fb,"datagrid").data;
var ok=true;
for(var i=0,len=data.rows.length;i<len;i++){
if(_5c2(_5fb,i)){
_5c3(_5fb,i,false);
}else{
ok=false;
}
}
if(ok){
_5f6(_5fb);
}
};
function _5fc(_5fd){
var _5fe=$.data(_5fd,"datagrid");
var opts=_5fe.options;
var _5ff=_5fe.originalRows;
var _600=_5fe.insertedRows;
var _601=_5fe.deletedRows;
var _602=_5fe.selectedRows;
var _603=_5fe.checkedRows;
var data=_5fe.data;
function _604(a){
var ids=[];
for(var i=0;i<a.length;i++){
ids.push(a[i][opts.idField]);
}
return ids;
};
function _605(ids,_606){
for(var i=0;i<ids.length;i++){
var _607=_57c(_5fd,ids[i]);
if(_607>=0){
(_606=="s"?_591:_598)(_5fd,_607,true);
}
}
};
for(var i=0;i<data.rows.length;i++){
_5c3(_5fd,i,true);
}
var _608=_604(_602);
var _609=_604(_603);
_602.splice(0,_602.length);
_603.splice(0,_603.length);
data.total+=_601.length-_600.length;
data.rows=_5ff;
_572(_5fd,data);
_605(_608,"s");
_605(_609,"c");
_5f6(_5fd);
};
function _60a(_60b,_60c){
var opts=$.data(_60b,"datagrid").options;
if(_60c){
opts.queryParams=_60c;
}
var _60d=$.extend({},opts.queryParams);
if(opts.pagination){
$.extend(_60d,{page:opts.pageNumber,rows:opts.pageSize});
}
if(opts.sortName){
$.extend(_60d,{sort:opts.sortName,order:opts.sortOrder});
}
if(opts.onBeforeLoad.call(_60b,_60d)==false){
return;
}
$(_60b).datagrid("loading");
setTimeout(function(){
_60e();
},0);
function _60e(){
var _60f=opts.loader.call(_60b,_60d,function(data){
setTimeout(function(){
$(_60b).datagrid("loaded");
},0);
_572(_60b,data);
setTimeout(function(){
_5f6(_60b);
},0);
},function(){
setTimeout(function(){
$(_60b).datagrid("loaded");
},0);
opts.onLoadError.apply(_60b,arguments);
});
if(_60f==false){
$(_60b).datagrid("loaded");
}
};
};
function _610(_611,_612){
var opts=$.data(_611,"datagrid").options;
_612.rowspan=_612.rowspan||1;
_612.colspan=_612.colspan||1;
if(_612.rowspan==1&&_612.colspan==1){
return;
}
var tr=opts.finder.getTr(_611,(_612.index!=undefined?_612.index:_612.id));
if(!tr.length){
return;
}
var row=opts.finder.getRow(_611,tr);
var _613=row[_612.field];
var td=tr.find("td[field=\""+_612.field+"\"]");
td.attr("rowspan",_612.rowspan).attr("colspan",_612.colspan);
td.addClass("datagrid-td-merged");
for(var i=1;i<_612.colspan;i++){
td=td.next();
td.hide();
row[td.attr("field")]=_613;
}
for(var i=1;i<_612.rowspan;i++){
tr=tr.next();
if(!tr.length){
break;
}
var row=opts.finder.getRow(_611,tr);
var td=tr.find("td[field=\""+_612.field+"\"]").hide();
row[td.attr("field")]=_613;
for(var j=1;j<_612.colspan;j++){
td=td.next();
td.hide();
row[td.attr("field")]=_613;
}
}
_560(_611);
};
$.fn.datagrid=function(_614,_615){
if(typeof _614=="string"){
return $.fn.datagrid.methods[_614](this,_615);
}
_614=_614||{};
return this.each(function(){
var _616=$.data(this,"datagrid");
var opts;
if(_616){
opts=$.extend(_616.options,_614);
_616.options=opts;
}else{
opts=$.extend({},$.extend({},$.fn.datagrid.defaults,{queryParams:{}}),$.fn.datagrid.parseOptions(this),_614);
$(this).css("width","").css("height","");
var _617=_50b(this,opts.rownumbers);
if(!opts.columns){
opts.columns=_617.columns;
}
if(!opts.frozenColumns){
opts.frozenColumns=_617.frozenColumns;
}
opts.columns=$.extend(true,[],opts.columns);
opts.frozenColumns=$.extend(true,[],opts.frozenColumns);
opts.view=$.extend({},opts.view);
$.data(this,"datagrid",{options:opts,panel:_617.panel,dc:_617.dc,ss:_617.ss,selectedRows:[],checkedRows:[],data:{total:0,rows:[]},originalRows:[],updatedRows:[],insertedRows:[],deletedRows:[]});
}
_517(this);
if(opts.data){
_572(this,opts.data);
_5f6(this);
}else{
var data=$.fn.datagrid.parseData(this);
if(data.total>0){
_572(this,data);
_5f6(this);
}
}
_4e6(this);
_60a(this);
_52b(this);
});
};
var _618={text:{init:function(_619,_61a){
var _61b=$("<input type=\"text\" class=\"datagrid-editable-input\">").appendTo(_619);
return _61b;
},getValue:function(_61c){
return $(_61c).val();
},setValue:function(_61d,_61e){
$(_61d).val(_61e);
},resize:function(_61f,_620){
$(_61f)._outerWidth(_620)._outerHeight(22);
}},textarea:{init:function(_621,_622){
var _623=$("<textarea class=\"datagrid-editable-input\"></textarea>").appendTo(_621);
return _623;
},getValue:function(_624){
return $(_624).val();
},setValue:function(_625,_626){
$(_625).val(_626);
},resize:function(_627,_628){
$(_627)._outerWidth(_628);
}},checkbox:{init:function(_629,_62a){
var _62b=$("<input type=\"checkbox\">").appendTo(_629);
_62b.val(_62a.on);
_62b.attr("offval",_62a.off);
return _62b;
},getValue:function(_62c){
if($(_62c).is(":checked")){
return $(_62c).val();
}else{
return $(_62c).attr("offval");
}
},setValue:function(_62d,_62e){
var _62f=false;
if($(_62d).val()==_62e){
_62f=true;
}
$(_62d)._propAttr("checked",_62f);
}},numberbox:{init:function(_630,_631){
var _632=$("<input type=\"text\" class=\"datagrid-editable-input\">").appendTo(_630);
_632.numberbox(_631);
return _632;
},destroy:function(_633){
$(_633).numberbox("destroy");
},getValue:function(_634){
$(_634).blur();
return $(_634).numberbox("getValue");
},setValue:function(_635,_636){
$(_635).numberbox("setValue",_636);
},resize:function(_637,_638){
$(_637)._outerWidth(_638)._outerHeight(22);
}},validatebox:{init:function(_639,_63a){
var _63b=$("<input type=\"text\" class=\"datagrid-editable-input\">").appendTo(_639);
_63b.validatebox(_63a);
return _63b;
},destroy:function(_63c){
$(_63c).validatebox("destroy");
},getValue:function(_63d){
return $(_63d).val();
},setValue:function(_63e,_63f){
$(_63e).val(_63f);
},resize:function(_640,_641){
$(_640)._outerWidth(_641)._outerHeight(22);
}},datebox:{init:function(_642,_643){
var _644=$("<input type=\"text\">").appendTo(_642);
_644.datebox(_643);
return _644;
},destroy:function(_645){
$(_645).datebox("destroy");
},getValue:function(_646){
return $(_646).datebox("getValue");
},setValue:function(_647,_648){
$(_647).datebox("setValue",_648);
},resize:function(_649,_64a){
$(_649).datebox("resize",_64a);
}},combobox:{init:function(_64b,_64c){
var _64d=$("<input type=\"text\">").appendTo(_64b);
_64d.combobox(_64c||{});
return _64d;
},destroy:function(_64e){
$(_64e).combobox("destroy");
},getValue:function(_64f){
var opts=$(_64f).combobox("options");
if(opts.multiple){
return $(_64f).combobox("getValues").join(opts.separator);
}else{
return $(_64f).combobox("getValue");
}
},setValue:function(_650,_651){
var opts=$(_650).combobox("options");
if(opts.multiple){
if(_651){
$(_650).combobox("setValues",_651.split(opts.separator));
}else{
$(_650).combobox("clear");
}
}else{
$(_650).combobox("setValue",_651);
}
},resize:function(_652,_653){
$(_652).combobox("resize",_653);
}},combotree:{init:function(_654,_655){
var _656=$("<input type=\"text\">").appendTo(_654);
_656.combotree(_655);
return _656;
},destroy:function(_657){
$(_657).combotree("destroy");
},getValue:function(_658){
return $(_658).combotree("getValue");
},setValue:function(_659,_65a){
$(_659).combotree("setValue",_65a);
},resize:function(_65b,_65c){
$(_65b).combotree("resize",_65c);
}}};
$.fn.datagrid.methods={options:function(jq){
var _65d=$.data(jq[0],"datagrid").options;
var _65e=$.data(jq[0],"datagrid").panel.panel("options");
var opts=$.extend(_65d,{width:_65e.width,height:_65e.height,closed:_65e.closed,collapsed:_65e.collapsed,minimized:_65e.minimized,maximized:_65e.maximized});
return opts;
},getPanel:function(jq){
return $.data(jq[0],"datagrid").panel;
},getPager:function(jq){
return $.data(jq[0],"datagrid").panel.children("div.datagrid-pager");
},getColumnFields:function(jq,_65f){
return _529(jq[0],_65f);
},getColumnOption:function(jq,_660){
return _52a(jq[0],_660);
},resize:function(jq,_661){
return jq.each(function(){
_4e6(this,_661);
});
},load:function(jq,_662){
return jq.each(function(){
var opts=$(this).datagrid("options");
opts.pageNumber=1;
var _663=$(this).datagrid("getPager");
_663.pagination("refresh",{pageNumber:1});
_60a(this,_662);
});
},reload:function(jq,_664){
return jq.each(function(){
_60a(this,_664);
});
},reloadFooter:function(jq,_665){
return jq.each(function(){
var opts=$.data(this,"datagrid").options;
var dc=$.data(this,"datagrid").dc;
if(_665){
$.data(this,"datagrid").footer=_665;
}
if(opts.showFooter){
opts.view.renderFooter.call(opts.view,this,dc.footer2,false);
opts.view.renderFooter.call(opts.view,this,dc.footer1,true);
if(opts.view.onAfterRender){
opts.view.onAfterRender.call(opts.view,this);
}
$(this).datagrid("fixRowHeight");
}
});
},loading:function(jq){
return jq.each(function(){
var opts=$.data(this,"datagrid").options;
$(this).datagrid("getPager").pagination("loading");
if(opts.loadMsg){
var _666=$(this).datagrid("getPanel");
if(!_666.children("div.datagrid-mask").length){
$("<div class=\"datagrid-mask\" style=\"display:block\"></div>").appendTo(_666);
var msg=$("<div class=\"datagrid-mask-msg\" style=\"display:block;left:50%\"></div>").html(opts.loadMsg).appendTo(_666);
msg._outerHeight(40);
msg.css({marginLeft:(-msg.outerWidth()/2),lineHeight:(msg.height()+"px")});
}
}
});
},loaded:function(jq){
return jq.each(function(){
$(this).datagrid("getPager").pagination("loaded");
var _667=$(this).datagrid("getPanel");
_667.children("div.datagrid-mask-msg").remove();
_667.children("div.datagrid-mask").remove();
});
},fitColumns:function(jq){
return jq.each(function(){
_543(this);
});
},fixColumnSize:function(jq,_668){
return jq.each(function(){
_513(this,_668);
});
},fixRowHeight:function(jq,_669){
return jq.each(function(){
_4f7(this,_669);
});
},freezeRow:function(jq,_66a){
return jq.each(function(){
_504(this,_66a);
});
},autoSizeColumn:function(jq,_66b){
return jq.each(function(){
_550(this,_66b);
});
},loadData:function(jq,data){
return jq.each(function(){
_572(this,data);
_5f6(this);
});
},getData:function(jq){
return $.data(jq[0],"datagrid").data;
},getRows:function(jq){
return $.data(jq[0],"datagrid").data.rows;
},getFooterRows:function(jq){
return $.data(jq[0],"datagrid").footer;
},getRowIndex:function(jq,id){
return _57c(jq[0],id);
},getChecked:function(jq){
return _583(jq[0]);
},getSelected:function(jq){
var rows=_57f(jq[0]);
return rows.length>0?rows[0]:null;
},getSelections:function(jq){
return _57f(jq[0]);
},clearSelections:function(jq){
return jq.each(function(){
var _66c=$.data(this,"datagrid").selectedRows;
_66c.splice(0,_66c.length);
_597(this);
});
},clearChecked:function(jq){
return jq.each(function(){
var _66d=$.data(this,"datagrid").checkedRows;
_66d.splice(0,_66d.length);
_5ab(this);
});
},scrollTo:function(jq,_66e){
return jq.each(function(){
_586(this,_66e);
});
},highlightRow:function(jq,_66f){
return jq.each(function(){
_58d(this,_66f);
_586(this,_66f);
});
},selectAll:function(jq){
return jq.each(function(){
_5a0(this);
});
},unselectAll:function(jq){
return jq.each(function(){
_597(this);
});
},selectRow:function(jq,_670){
return jq.each(function(){
_591(this,_670);
});
},selectRecord:function(jq,id){
return jq.each(function(){
var opts=$.data(this,"datagrid").options;
if(opts.idField){
var _671=_57c(this,id);
if(_671>=0){
$(this).datagrid("selectRow",_671);
}
}
});
},unselectRow:function(jq,_672){
return jq.each(function(){
_599(this,_672);
});
},checkRow:function(jq,_673){
return jq.each(function(){
_598(this,_673);
});
},uncheckRow:function(jq,_674){
return jq.each(function(){
_59f(this,_674);
});
},checkAll:function(jq){
return jq.each(function(){
_5a5(this);
});
},uncheckAll:function(jq){
return jq.each(function(){
_5ab(this);
});
},beginEdit:function(jq,_675){
return jq.each(function(){
_5bd(this,_675);
});
},endEdit:function(jq,_676){
return jq.each(function(){
_5c3(this,_676,false);
});
},cancelEdit:function(jq,_677){
return jq.each(function(){
_5c3(this,_677,true);
});
},getEditors:function(jq,_678){
return _5ce(jq[0],_678);
},getEditor:function(jq,_679){
return _5d2(jq[0],_679);
},refreshRow:function(jq,_67a){
return jq.each(function(){
var opts=$.data(this,"datagrid").options;
opts.view.refreshRow.call(opts.view,this,_67a);
});
},validateRow:function(jq,_67b){
return _5c2(jq[0],_67b);
},updateRow:function(jq,_67c){
return jq.each(function(){
var opts=$.data(this,"datagrid").options;
opts.view.updateRow.call(opts.view,this,_67c.index,_67c.row);
});
},appendRow:function(jq,row){
return jq.each(function(){
_5f3(this,row);
});
},insertRow:function(jq,_67d){
return jq.each(function(){
_5ef(this,_67d);
});
},deleteRow:function(jq,_67e){
return jq.each(function(){
_5e9(this,_67e);
});
},getChanges:function(jq,_67f){
return _5e3(jq[0],_67f);
},acceptChanges:function(jq){
return jq.each(function(){
_5fa(this);
});
},rejectChanges:function(jq){
return jq.each(function(){
_5fc(this);
});
},mergeCells:function(jq,_680){
return jq.each(function(){
_610(this,_680);
});
},showColumn:function(jq,_681){
return jq.each(function(){
var _682=$(this).datagrid("getPanel");
_682.find("td[field=\""+_681+"\"]").show();
$(this).datagrid("getColumnOption",_681).hidden=false;
$(this).datagrid("fitColumns");
});
},hideColumn:function(jq,_683){
return jq.each(function(){
var _684=$(this).datagrid("getPanel");
_684.find("td[field=\""+_683+"\"]").hide();
$(this).datagrid("getColumnOption",_683).hidden=true;
$(this).datagrid("fitColumns");
});
}};
$.fn.datagrid.parseOptions=function(_685){
var t=$(_685);
return $.extend({},$.fn.panel.parseOptions(_685),$.parser.parseOptions(_685,["url","toolbar","idField","sortName","sortOrder","pagePosition","resizeHandle",{fitColumns:"boolean",autoRowHeight:"boolean",striped:"boolean",nowrap:"boolean"},{rownumbers:"boolean",singleSelect:"boolean",checkOnSelect:"boolean",selectOnCheck:"boolean"},{pagination:"boolean",pageSize:"number",pageNumber:"number"},{multiSort:"boolean",remoteSort:"boolean",showHeader:"boolean",showFooter:"boolean"},{scrollbarSize:"number"}]),{pageList:(t.attr("pageList")?eval(t.attr("pageList")):undefined),loadMsg:(t.attr("loadMsg")!=undefined?t.attr("loadMsg"):undefined),rowStyler:(t.attr("rowStyler")?eval(t.attr("rowStyler")):undefined)});
};
$.fn.datagrid.parseData=function(_686){
var t=$(_686);
var data={total:0,rows:[]};
var _687=t.datagrid("getColumnFields",true).concat(t.datagrid("getColumnFields",false));
t.find("tbody tr").each(function(){
data.total++;
var row={};
$.extend(row,$.parser.parseOptions(this,["iconCls","state"]));
for(var i=0;i<_687.length;i++){
row[_687[i]]=$(this).find("td:eq("+i+")").html();
}
data.rows.push(row);
});
return data;
};
var _688={render:function(_689,_68a,_68b){
var _68c=$.data(_689,"datagrid");
var opts=_68c.options;
var rows=_68c.data.rows;
var _68d=$(_689).datagrid("getColumnFields",_68b);
if(_68b){
if(!(opts.rownumbers||(opts.frozenColumns&&opts.frozenColumns.length))){
return;
}
}
var _68e=["<table class=\"datagrid-btable\" cellspacing=\"0\" cellpadding=\"0\" border=\"0\"><tbody>"];
for(var i=0;i<rows.length;i++){
var css=opts.rowStyler?opts.rowStyler.call(_689,i,rows[i]):"";
var _68f="";
var _690="";
if(typeof css=="string"){
_690=css;
}else{
if(css){
_68f=css["class"]||"";
_690=css["style"]||"";
}
}
var cls="class=\"datagrid-row "+(i%2&&opts.striped?"datagrid-row-alt ":" ")+_68f+"\"";
var _691=_690?"style=\""+_690+"\"":"";
var _692=_68c.rowIdPrefix+"-"+(_68b?1:2)+"-"+i;
_68e.push("<tr id=\""+_692+"\" datagrid-row-index=\""+i+"\" "+cls+" "+_691+">");
_68e.push(this.renderRow.call(this,_689,_68d,_68b,i,rows[i]));
_68e.push("</tr>");
}
_68e.push("</tbody></table>");
$(_68a).html(_68e.join(""));
},renderFooter:function(_693,_694,_695){
var opts=$.data(_693,"datagrid").options;
var rows=$.data(_693,"datagrid").footer||[];
var _696=$(_693).datagrid("getColumnFields",_695);
var _697=["<table class=\"datagrid-ftable\" cellspacing=\"0\" cellpadding=\"0\" border=\"0\"><tbody>"];
for(var i=0;i<rows.length;i++){
_697.push("<tr class=\"datagrid-row\" datagrid-row-index=\""+i+"\">");
_697.push(this.renderRow.call(this,_693,_696,_695,i,rows[i]));
_697.push("</tr>");
}
_697.push("</tbody></table>");
$(_694).html(_697.join(""));
},renderRow:function(_698,_699,_69a,_69b,_69c){
var opts=$.data(_698,"datagrid").options;
var cc=[];
if(_69a&&opts.rownumbers){
var _69d=_69b+1;
if(opts.pagination){
_69d+=(opts.pageNumber-1)*opts.pageSize;
}
cc.push("<td class=\"datagrid-td-rownumber\"><div class=\"datagrid-cell-rownumber\">"+_69d+"</div></td>");
}
for(var i=0;i<_699.length;i++){
var _69e=_699[i];
var col=$(_698).datagrid("getColumnOption",_69e);
if(col){
var _69f=_69c[_69e];
var css=col.styler?(col.styler(_69f,_69c,_69b)||""):"";
var _6a0="";
var _6a1="";
if(typeof css=="string"){
_6a1=css;
}else{
if(cc){
_6a0=css["class"]||"";
_6a1=css["style"]||"";
}
}
var cls=_6a0?"class=\""+_6a0+"\"":"";
var _6a2=col.hidden?"style=\"display:none;"+_6a1+"\"":(_6a1?"style=\""+_6a1+"\"":"");
cc.push("<td field=\""+_69e+"\" "+cls+" "+_6a2+">");
if(col.checkbox){
var _6a2="";
}else{
var _6a2=_6a1;
if(col.align){
_6a2+=";text-align:"+col.align+";";
}
if(!opts.nowrap){
_6a2+=";white-space:normal;height:auto;";
}else{
if(opts.autoRowHeight){
_6a2+=";height:auto;";
}
}
}
cc.push("<div style=\""+_6a2+"\" ");
cc.push(col.checkbox?"class=\"datagrid-cell-check\"":"class=\"datagrid-cell "+col.cellClass+"\"");
cc.push(">");
if(col.checkbox){
cc.push("<input type=\"checkbox\" name=\""+_69e+"\" value=\""+(_69f!=undefined?_69f:"")+"\">");
}else{
if(col.formatter){
cc.push(col.formatter(_69f,_69c,_69b));
}else{
cc.push(_69f);
}
}
cc.push("</div>");
cc.push("</td>");
}
}
return cc.join("");
},refreshRow:function(_6a3,_6a4){
this.updateRow.call(this,_6a3,_6a4,{});
},updateRow:function(_6a5,_6a6,row){
var opts=$.data(_6a5,"datagrid").options;
var rows=$(_6a5).datagrid("getRows");
$.extend(rows[_6a6],row);
var css=opts.rowStyler?opts.rowStyler.call(_6a5,_6a6,rows[_6a6]):"";
var _6a7="";
var _6a8="";
if(typeof css=="string"){
_6a8=css;
}else{
if(css){
_6a7=css["class"]||"";
_6a8=css["style"]||"";
}
}
var _6a7="datagrid-row "+(_6a6%2&&opts.striped?"datagrid-row-alt ":" ")+_6a7;
function _6a9(_6aa){
var _6ab=$(_6a5).datagrid("getColumnFields",_6aa);
var tr=opts.finder.getTr(_6a5,_6a6,"body",(_6aa?1:2));
var _6ac=tr.find("div.datagrid-cell-check input[type=checkbox]").is(":checked");
tr.html(this.renderRow.call(this,_6a5,_6ab,_6aa,_6a6,rows[_6a6]));
tr.attr("style",_6a8).attr("class",tr.hasClass("datagrid-row-selected")?_6a7+" datagrid-row-selected":_6a7);
if(_6ac){
tr.find("div.datagrid-cell-check input[type=checkbox]")._propAttr("checked",true);
}
};
_6a9.call(this,true);
_6a9.call(this,false);
$(_6a5).datagrid("fixRowHeight",_6a6);
},insertRow:function(_6ad,_6ae,row){
var _6af=$.data(_6ad,"datagrid");
var opts=_6af.options;
var dc=_6af.dc;
var data=_6af.data;
if(_6ae==undefined||_6ae==null){
_6ae=data.rows.length;
}
if(_6ae>data.rows.length){
_6ae=data.rows.length;
}
function _6b0(_6b1){
var _6b2=_6b1?1:2;
for(var i=data.rows.length-1;i>=_6ae;i--){
var tr=opts.finder.getTr(_6ad,i,"body",_6b2);
tr.attr("datagrid-row-index",i+1);
tr.attr("id",_6af.rowIdPrefix+"-"+_6b2+"-"+(i+1));
if(_6b1&&opts.rownumbers){
var _6b3=i+2;
if(opts.pagination){
_6b3+=(opts.pageNumber-1)*opts.pageSize;
}
tr.find("div.datagrid-cell-rownumber").html(_6b3);
}
if(opts.striped){
tr.removeClass("datagrid-row-alt").addClass((i+1)%2?"datagrid-row-alt":"");
}
}
};
function _6b4(_6b5){
var _6b6=_6b5?1:2;
var _6b7=$(_6ad).datagrid("getColumnFields",_6b5);
var _6b8=_6af.rowIdPrefix+"-"+_6b6+"-"+_6ae;
var tr="<tr id=\""+_6b8+"\" class=\"datagrid-row\" datagrid-row-index=\""+_6ae+"\"></tr>";
if(_6ae>=data.rows.length){
if(data.rows.length){
opts.finder.getTr(_6ad,"","last",_6b6).after(tr);
}else{
var cc=_6b5?dc.body1:dc.body2;
cc.html("<table cellspacing=\"0\" cellpadding=\"0\" border=\"0\"><tbody>"+tr+"</tbody></table>");
}
}else{
opts.finder.getTr(_6ad,_6ae+1,"body",_6b6).before(tr);
}
};
_6b0.call(this,true);
_6b0.call(this,false);
_6b4.call(this,true);
_6b4.call(this,false);
data.total+=1;
data.rows.splice(_6ae,0,row);
this.refreshRow.call(this,_6ad,_6ae);
},deleteRow:function(_6b9,_6ba){
var _6bb=$.data(_6b9,"datagrid");
var opts=_6bb.options;
var data=_6bb.data;
function _6bc(_6bd){
var _6be=_6bd?1:2;
for(var i=_6ba+1;i<data.rows.length;i++){
var tr=opts.finder.getTr(_6b9,i,"body",_6be);
tr.attr("datagrid-row-index",i-1);
tr.attr("id",_6bb.rowIdPrefix+"-"+_6be+"-"+(i-1));
if(_6bd&&opts.rownumbers){
var _6bf=i;
if(opts.pagination){
_6bf+=(opts.pageNumber-1)*opts.pageSize;
}
tr.find("div.datagrid-cell-rownumber").html(_6bf);
}
if(opts.striped){
tr.removeClass("datagrid-row-alt").addClass((i-1)%2?"datagrid-row-alt":"");
}
}
};
opts.finder.getTr(_6b9,_6ba).remove();
_6bc.call(this,true);
_6bc.call(this,false);
data.total-=1;
data.rows.splice(_6ba,1);
},onBeforeRender:function(_6c0,rows){
},onAfterRender:function(_6c1){
var opts=$.data(_6c1,"datagrid").options;
if(opts.showFooter){
var _6c2=$(_6c1).datagrid("getPanel").find("div.datagrid-footer");
_6c2.find("div.datagrid-cell-rownumber,div.datagrid-cell-check").css("visibility","hidden");
}
}};
$.fn.datagrid.defaults=$.extend({},$.fn.panel.defaults,{frozenColumns:undefined,columns:undefined,fitColumns:false,resizeHandle:"right",autoRowHeight:true,toolbar:null,striped:false,method:"post",nowrap:true,idField:null,url:null,data:null,loadMsg:"Processing, please wait ...",rownumbers:false,singleSelect:false,selectOnCheck:true,checkOnSelect:true,pagination:false,pagePosition:"bottom",pageNumber:1,pageSize:10,pageList:[10,20,30,40,50],queryParams:{},sortName:null,sortOrder:"asc",multiSort:false,remoteSort:true,showHeader:true,showFooter:false,scrollbarSize:18,rowStyler:function(_6c3,_6c4){
},loader:function(_6c5,_6c6,_6c7){
var opts=$(this).datagrid("options");
if(!opts.url){
return false;
}
$.ajax({type:opts.method,url:opts.url,data:_6c5,dataType:"json",success:function(data){
_6c6(data);
},error:function(){
_6c7.apply(this,arguments);
}});
},loadFilter:function(data){
if(typeof data.length=="number"&&typeof data.splice=="function"){
return {total:data.length,rows:data};
}else{
return data;
}
},editors:_618,finder:{getTr:function(_6c8,_6c9,type,_6ca){
type=type||"body";
_6ca=_6ca||0;
var _6cb=$.data(_6c8,"datagrid");
var dc=_6cb.dc;
var opts=_6cb.options;
if(_6ca==0){
var tr1=opts.finder.getTr(_6c8,_6c9,type,1);
var tr2=opts.finder.getTr(_6c8,_6c9,type,2);
return tr1.add(tr2);
}else{
if(type=="body"){
var tr=$("#"+_6cb.rowIdPrefix+"-"+_6ca+"-"+_6c9);
if(!tr.length){
tr=(_6ca==1?dc.body1:dc.body2).find(">table>tbody>tr[datagrid-row-index="+_6c9+"]");
}
return tr;
}else{
if(type=="footer"){
return (_6ca==1?dc.footer1:dc.footer2).find(">table>tbody>tr[datagrid-row-index="+_6c9+"]");
}else{
if(type=="selected"){
return (_6ca==1?dc.body1:dc.body2).find(">table>tbody>tr.datagrid-row-selected");
}else{
if(type=="highlight"){
return (_6ca==1?dc.body1:dc.body2).find(">table>tbody>tr.datagrid-row-over");
}else{
if(type=="checked"){
return (_6ca==1?dc.body1:dc.body2).find(">table>tbody>tr.datagrid-row-checked");
}else{
if(type=="last"){
return (_6ca==1?dc.body1:dc.body2).find(">table>tbody>tr[datagrid-row-index]:last");
}else{
if(type=="allbody"){
return (_6ca==1?dc.body1:dc.body2).find(">table>tbody>tr[datagrid-row-index]");
}else{
if(type=="allfooter"){
return (_6ca==1?dc.footer1:dc.footer2).find(">table>tbody>tr[datagrid-row-index]");
}
}
}
}
}
}
}
}
}
},getRow:function(_6cc,p){
var _6cd=(typeof p=="object")?p.attr("datagrid-row-index"):p;
return $.data(_6cc,"datagrid").data.rows[parseInt(_6cd)];
}},view:_688,onBeforeLoad:function(_6ce){
},onLoadSuccess:function(){
},onLoadError:function(){
},onClickRow:function(_6cf,_6d0){
},onDblClickRow:function(_6d1,_6d2){
},onClickCell:function(_6d3,_6d4,_6d5){
},onDblClickCell:function(_6d6,_6d7,_6d8){
},onSortColumn:function(sort,_6d9){
},onResizeColumn:function(_6da,_6db){
},onSelect:function(_6dc,_6dd){
},onUnselect:function(_6de,_6df){
},onSelectAll:function(rows){
},onUnselectAll:function(rows){
},onCheck:function(_6e0,_6e1){
},onUncheck:function(_6e2,_6e3){
},onCheckAll:function(rows){
},onUncheckAll:function(rows){
},onBeforeEdit:function(_6e4,_6e5){
},onAfterEdit:function(_6e6,_6e7,_6e8){
},onCancelEdit:function(_6e9,_6ea){
},onHeaderContextMenu:function(e,_6eb){
},onRowContextMenu:function(e,_6ec,_6ed){
}});
})(jQuery);
(function($){
var _6ee;
function _6ef(_6f0){
var _6f1=$.data(_6f0,"propertygrid");
var opts=$.data(_6f0,"propertygrid").options;
$(_6f0).datagrid($.extend({},opts,{cls:"propertygrid",view:(opts.showGroup?opts.groupView:opts.view),onClickRow:function(_6f2,row){
if(_6ee!=this){
_6f3(_6ee);
_6ee=this;
}
if(opts.editIndex!=_6f2&&row.editor){
var col=$(this).datagrid("getColumnOption","value");
col.editor=row.editor;
_6f3(_6ee);
$(this).datagrid("beginEdit",_6f2);
$(this).datagrid("getEditors",_6f2)[0].target.focus();
opts.editIndex=_6f2;
}
opts.onClickRow.call(_6f0,_6f2,row);
},loadFilter:function(data){
_6f3(this);
return opts.loadFilter.call(this,data);
}}));
$(document).unbind(".propertygrid").bind("mousedown.propertygrid",function(e){
var p=$(e.target).closest("div.datagrid-view,div.combo-panel");
if(p.length){
return;
}
_6f3(_6ee);
_6ee=undefined;
});
};
function _6f3(_6f4){
var t=$(_6f4);
if(!t.length){
return;
}
var opts=$.data(_6f4,"propertygrid").options;
var _6f5=opts.editIndex;
if(_6f5==undefined){
return;
}
var ed=t.datagrid("getEditors",_6f5)[0];
if(ed){
ed.target.blur();
if(t.datagrid("validateRow",_6f5)){
t.datagrid("endEdit",_6f5);
}else{
t.datagrid("cancelEdit",_6f5);
}
}
opts.editIndex=undefined;
};
$.fn.propertygrid=function(_6f6,_6f7){
if(typeof _6f6=="string"){
var _6f8=$.fn.propertygrid.methods[_6f6];
if(_6f8){
return _6f8(this,_6f7);
}else{
return this.datagrid(_6f6,_6f7);
}
}
_6f6=_6f6||{};
return this.each(function(){
var _6f9=$.data(this,"propertygrid");
if(_6f9){
$.extend(_6f9.options,_6f6);
}else{
var opts=$.extend({},$.fn.propertygrid.defaults,$.fn.propertygrid.parseOptions(this),_6f6);
opts.frozenColumns=$.extend(true,[],opts.frozenColumns);
opts.columns=$.extend(true,[],opts.columns);
$.data(this,"propertygrid",{options:opts});
}
_6ef(this);
});
};
$.fn.propertygrid.methods={options:function(jq){
return $.data(jq[0],"propertygrid").options;
}};
$.fn.propertygrid.parseOptions=function(_6fa){
return $.extend({},$.fn.datagrid.parseOptions(_6fa),$.parser.parseOptions(_6fa,[{showGroup:"boolean"}]));
};
var _6fb=$.extend({},$.fn.datagrid.defaults.view,{render:function(_6fc,_6fd,_6fe){
var _6ff=[];
var _700=this.groups;
for(var i=0;i<_700.length;i++){
_6ff.push(this.renderGroup.call(this,_6fc,i,_700[i],_6fe));
}
$(_6fd).html(_6ff.join(""));
},renderGroup:function(_701,_702,_703,_704){
var _705=$.data(_701,"datagrid");
var opts=_705.options;
var _706=$(_701).datagrid("getColumnFields",_704);
var _707=[];
_707.push("<div class=\"datagrid-group\" group-index="+_702+">");
_707.push("<table cellspacing=\"0\" cellpadding=\"0\" border=\"0\" style=\"height:100%\"><tbody>");
_707.push("<tr>");
if((_704&&(opts.rownumbers||opts.frozenColumns.length))||(!_704&&!(opts.rownumbers||opts.frozenColumns.length))){
_707.push("<td style=\"border:0;text-align:center;width:25px\"><span class=\"datagrid-row-expander datagrid-row-collapse\" style=\"display:inline-block;width:16px;height:16px;cursor:pointer\">&nbsp;</span></td>");
}
_707.push("<td style=\"border:0;\">");
if(!_704){
_707.push("<span class=\"datagrid-group-title\">");
_707.push(opts.groupFormatter.call(_701,_703.value,_703.rows));
_707.push("</span>");
}
_707.push("</td>");
_707.push("</tr>");
_707.push("</tbody></table>");
_707.push("</div>");
_707.push("<table class=\"datagrid-btable\" cellspacing=\"0\" cellpadding=\"0\" border=\"0\"><tbody>");
var _708=_703.startIndex;
for(var j=0;j<_703.rows.length;j++){
var css=opts.rowStyler?opts.rowStyler.call(_701,_708,_703.rows[j]):"";
var _709="";
var _70a="";
if(typeof css=="string"){
_70a=css;
}else{
if(css){
_709=css["class"]||"";
_70a=css["style"]||"";
}
}
var cls="class=\"datagrid-row "+(_708%2&&opts.striped?"datagrid-row-alt ":" ")+_709+"\"";
var _70b=_70a?"style=\""+_70a+"\"":"";
var _70c=_705.rowIdPrefix+"-"+(_704?1:2)+"-"+_708;
_707.push("<tr id=\""+_70c+"\" datagrid-row-index=\""+_708+"\" "+cls+" "+_70b+">");
_707.push(this.renderRow.call(this,_701,_706,_704,_708,_703.rows[j]));
_707.push("</tr>");
_708++;
}
_707.push("</tbody></table>");
return _707.join("");
},bindEvents:function(_70d){
var _70e=$.data(_70d,"datagrid");
var dc=_70e.dc;
var body=dc.body1.add(dc.body2);
var _70f=($.data(body[0],"events")||$._data(body[0],"events")).click[0].handler;
body.unbind("click").bind("click",function(e){
var tt=$(e.target);
var _710=tt.closest("span.datagrid-row-expander");
if(_710.length){
var _711=_710.closest("div.datagrid-group").attr("group-index");
if(_710.hasClass("datagrid-row-collapse")){
$(_70d).datagrid("collapseGroup",_711);
}else{
$(_70d).datagrid("expandGroup",_711);
}
}else{
_70f(e);
}
e.stopPropagation();
});
},onBeforeRender:function(_712,rows){
var _713=$.data(_712,"datagrid");
var opts=_713.options;
_714();
var _715=[];
for(var i=0;i<rows.length;i++){
var row=rows[i];
var _716=_717(row[opts.groupField]);
if(!_716){
_716={value:row[opts.groupField],rows:[row]};
_715.push(_716);
}else{
_716.rows.push(row);
}
}
var _718=0;
var _719=[];
for(var i=0;i<_715.length;i++){
var _716=_715[i];
_716.startIndex=_718;
_718+=_716.rows.length;
_719=_719.concat(_716.rows);
}
_713.data.rows=_719;
this.groups=_715;
var that=this;
setTimeout(function(){
that.bindEvents(_712);
},0);
function _717(_71a){
for(var i=0;i<_715.length;i++){
var _71b=_715[i];
if(_71b.value==_71a){
return _71b;
}
}
return null;
};
function _714(){
if(!$("#datagrid-group-style").length){
$("head").append("<style id=\"datagrid-group-style\">"+".datagrid-group{height:25px;overflow:hidden;font-weight:bold;border-bottom:1px solid #ccc;}"+"</style>");
}
};
}});
$.extend($.fn.datagrid.methods,{expandGroup:function(jq,_71c){
return jq.each(function(){
var view=$.data(this,"datagrid").dc.view;
var _71d=view.find(_71c!=undefined?"div.datagrid-group[group-index=\""+_71c+"\"]":"div.datagrid-group");
var _71e=_71d.find("span.datagrid-row-expander");
if(_71e.hasClass("datagrid-row-expand")){
_71e.removeClass("datagrid-row-expand").addClass("datagrid-row-collapse");
_71d.next("table").show();
}
$(this).datagrid("fixRowHeight");
});
},collapseGroup:function(jq,_71f){
return jq.each(function(){
var view=$.data(this,"datagrid").dc.view;
var _720=view.find(_71f!=undefined?"div.datagrid-group[group-index=\""+_71f+"\"]":"div.datagrid-group");
var _721=_720.find("span.datagrid-row-expander");
if(_721.hasClass("datagrid-row-collapse")){
_721.removeClass("datagrid-row-collapse").addClass("datagrid-row-expand");
_720.next("table").hide();
}
$(this).datagrid("fixRowHeight");
});
}});
$.fn.propertygrid.defaults=$.extend({},$.fn.datagrid.defaults,{singleSelect:true,remoteSort:false,fitColumns:true,loadMsg:"",frozenColumns:[[{field:"f",width:16,resizable:false}]],columns:[[{field:"name",title:"Name",width:100,sortable:true},{field:"value",title:"Value",width:100,resizable:false}]],showGroup:false,groupView:_6fb,groupField:"group",groupFormatter:function(_722,rows){
return _722;
}});
})(jQuery);
(function($){
function _723(_724){
var _725=$.data(_724,"treegrid");
var opts=_725.options;
$(_724).datagrid($.extend({},opts,{url:null,data:null,loader:function(){
return false;
},onBeforeLoad:function(){
return false;
},onLoadSuccess:function(){
},onResizeColumn:function(_726,_727){
_73d(_724);
opts.onResizeColumn.call(_724,_726,_727);
},onSortColumn:function(sort,_728){
opts.sortName=sort;
opts.sortOrder=_728;
if(opts.remoteSort){
_73c(_724);
}else{
var data=$(_724).treegrid("getData");
_752(_724,0,data);
}
opts.onSortColumn.call(_724,sort,_728);
},onBeforeEdit:function(_729,row){
if(opts.onBeforeEdit.call(_724,row)==false){
return false;
}
},onAfterEdit:function(_72a,row,_72b){
opts.onAfterEdit.call(_724,row,_72b);
},onCancelEdit:function(_72c,row){
opts.onCancelEdit.call(_724,row);
},onSelect:function(_72d){
opts.onSelect.call(_724,find(_724,_72d));
},onUnselect:function(_72e){
opts.onUnselect.call(_724,find(_724,_72e));
},onSelectAll:function(){
opts.onSelectAll.call(_724,$.data(_724,"treegrid").data);
},onUnselectAll:function(){
opts.onUnselectAll.call(_724,$.data(_724,"treegrid").data);
},onCheck:function(_72f){
opts.onCheck.call(_724,find(_724,_72f));
},onUncheck:function(_730){
opts.onUncheck.call(_724,find(_724,_730));
},onCheckAll:function(){
opts.onCheckAll.call(_724,$.data(_724,"treegrid").data);
},onUncheckAll:function(){
opts.onUncheckAll.call(_724,$.data(_724,"treegrid").data);
},onClickRow:function(_731){
opts.onClickRow.call(_724,find(_724,_731));
},onDblClickRow:function(_732){
opts.onDblClickRow.call(_724,find(_724,_732));
},onClickCell:function(_733,_734){
opts.onClickCell.call(_724,_734,find(_724,_733));
},onDblClickCell:function(_735,_736){
opts.onDblClickCell.call(_724,_736,find(_724,_735));
},onRowContextMenu:function(e,_737){
opts.onContextMenu.call(_724,e,find(_724,_737));
}}));
if(!opts.columns){
var _738=$.data(_724,"datagrid").options;
opts.columns=_738.columns;
opts.frozenColumns=_738.frozenColumns;
}
_725.dc=$.data(_724,"datagrid").dc;
if(opts.pagination){
var _739=$(_724).datagrid("getPager");
_739.pagination({pageNumber:opts.pageNumber,pageSize:opts.pageSize,pageList:opts.pageList,onSelectPage:function(_73a,_73b){
opts.pageNumber=_73a;
opts.pageSize=_73b;
_73c(_724);
}});
opts.pageSize=_739.pagination("options").pageSize;
}
};
function _73d(_73e,_73f){
var opts=$.data(_73e,"datagrid").options;
var dc=$.data(_73e,"datagrid").dc;
if(!dc.body1.is(":empty")&&(!opts.nowrap||opts.autoRowHeight)){
if(_73f!=undefined){
var _740=_741(_73e,_73f);
for(var i=0;i<_740.length;i++){
_742(_740[i][opts.idField]);
}
}
}
$(_73e).datagrid("fixRowHeight",_73f);
function _742(_743){
var tr1=opts.finder.getTr(_73e,_743,"body",1);
var tr2=opts.finder.getTr(_73e,_743,"body",2);
tr1.css("height","");
tr2.css("height","");
var _744=Math.max(tr1.height(),tr2.height());
tr1.css("height",_744);
tr2.css("height",_744);
};
};
function _745(_746){
var dc=$.data(_746,"datagrid").dc;
var opts=$.data(_746,"treegrid").options;
if(!opts.rownumbers){
return;
}
dc.body1.find("div.datagrid-cell-rownumber").each(function(i){
$(this).html(i+1);
});
};
function _747(_748){
var dc=$.data(_748,"datagrid").dc;
var body=dc.body1.add(dc.body2);
var _749=($.data(body[0],"events")||$._data(body[0],"events")).click[0].handler;
dc.body1.add(dc.body2).bind("mouseover",function(e){
var tt=$(e.target);
var tr=tt.closest("tr.datagrid-row");
if(!tr.length){
return;
}
if(tt.hasClass("tree-hit")){
tt.hasClass("tree-expanded")?tt.addClass("tree-expanded-hover"):tt.addClass("tree-collapsed-hover");
}
e.stopPropagation();
}).bind("mouseout",function(e){
var tt=$(e.target);
var tr=tt.closest("tr.datagrid-row");
if(!tr.length){
return;
}
if(tt.hasClass("tree-hit")){
tt.hasClass("tree-expanded")?tt.removeClass("tree-expanded-hover"):tt.removeClass("tree-collapsed-hover");
}
e.stopPropagation();
}).unbind("click").bind("click",function(e){
var tt=$(e.target);
var tr=tt.closest("tr.datagrid-row");
if(!tr.length){
return;
}
if(tt.hasClass("tree-hit")){
_74a(_748,tr.attr("node-id"));
}else{
_749(e);
}
e.stopPropagation();
});
};
function _74b(_74c,_74d){
var opts=$.data(_74c,"treegrid").options;
var tr1=opts.finder.getTr(_74c,_74d,"body",1);
var tr2=opts.finder.getTr(_74c,_74d,"body",2);
var _74e=$(_74c).datagrid("getColumnFields",true).length+(opts.rownumbers?1:0);
var _74f=$(_74c).datagrid("getColumnFields",false).length;
_750(tr1,_74e);
_750(tr2,_74f);
function _750(tr,_751){
$("<tr class=\"treegrid-tr-tree\">"+"<td style=\"border:0px\" colspan=\""+_751+"\">"+"<div></div>"+"</td>"+"</tr>").insertAfter(tr);
};
};
function _752(_753,_754,data,_755){
var _756=$.data(_753,"treegrid");
var opts=_756.options;
var dc=_756.dc;
data=opts.loadFilter.call(_753,data,_754);
var node=find(_753,_754);
if(node){
var _757=opts.finder.getTr(_753,_754,"body",1);
var _758=opts.finder.getTr(_753,_754,"body",2);
var cc1=_757.next("tr.treegrid-tr-tree").children("td").children("div");
var cc2=_758.next("tr.treegrid-tr-tree").children("td").children("div");
if(!_755){
node.children=[];
}
}else{
var cc1=dc.body1;
var cc2=dc.body2;
if(!_755){
_756.data=[];
}
}
if(!_755){
cc1.empty();
cc2.empty();
}
if(opts.view.onBeforeRender){
opts.view.onBeforeRender.call(opts.view,_753,_754,data);
}
opts.view.render.call(opts.view,_753,cc1,true);
opts.view.render.call(opts.view,_753,cc2,false);
if(opts.showFooter){
opts.view.renderFooter.call(opts.view,_753,dc.footer1,true);
opts.view.renderFooter.call(opts.view,_753,dc.footer2,false);
}
if(opts.view.onAfterRender){
opts.view.onAfterRender.call(opts.view,_753);
}
opts.onLoadSuccess.call(_753,node,data);
if(!_754&&opts.pagination){
var _759=$.data(_753,"treegrid").total;
var _75a=$(_753).datagrid("getPager");
if(_75a.pagination("options").total!=_759){
_75a.pagination({total:_759});
}
}
_73d(_753);
_745(_753);
$(_753).treegrid("autoSizeColumn");
};
function _73c(_75b,_75c,_75d,_75e,_75f){
var opts=$.data(_75b,"treegrid").options;
var body=$(_75b).datagrid("getPanel").find("div.datagrid-body");
if(_75d){
opts.queryParams=_75d;
}
var _760=$.extend({},opts.queryParams);
if(opts.pagination){
$.extend(_760,{page:opts.pageNumber,rows:opts.pageSize});
}
if(opts.sortName){
$.extend(_760,{sort:opts.sortName,order:opts.sortOrder});
}
var row=find(_75b,_75c);
if(opts.onBeforeLoad.call(_75b,row,_760)==false){
return;
}
var _761=body.find("tr[node-id=\""+_75c+"\"] span.tree-folder");
_761.addClass("tree-loading");
$(_75b).treegrid("loading");
var _762=opts.loader.call(_75b,_760,function(data){
_761.removeClass("tree-loading");
$(_75b).treegrid("loaded");
_752(_75b,_75c,data,_75e);
if(_75f){
_75f();
}
},function(){
_761.removeClass("tree-loading");
$(_75b).treegrid("loaded");
opts.onLoadError.apply(_75b,arguments);
if(_75f){
_75f();
}
});
if(_762==false){
_761.removeClass("tree-loading");
$(_75b).treegrid("loaded");
}
};
function _763(_764){
var rows=_765(_764);
if(rows.length){
return rows[0];
}else{
return null;
}
};
function _765(_766){
return $.data(_766,"treegrid").data;
};
function _767(_768,_769){
var row=find(_768,_769);
if(row._parentId){
return find(_768,row._parentId);
}else{
return null;
}
};
function _741(_76a,_76b){
var opts=$.data(_76a,"treegrid").options;
var body=$(_76a).datagrid("getPanel").find("div.datagrid-view2 div.datagrid-body");
var _76c=[];
if(_76b){
_76d(_76b);
}else{
var _76e=_765(_76a);
for(var i=0;i<_76e.length;i++){
_76c.push(_76e[i]);
_76d(_76e[i][opts.idField]);
}
}
function _76d(_76f){
var _770=find(_76a,_76f);
if(_770&&_770.children){
for(var i=0,len=_770.children.length;i<len;i++){
var _771=_770.children[i];
_76c.push(_771);
_76d(_771[opts.idField]);
}
}
};
return _76c;
};
function _772(_773){
var rows=_774(_773);
if(rows.length){
return rows[0];
}else{
return null;
}
};
function _774(_775){
var rows=[];
var _776=$(_775).datagrid("getPanel");
_776.find("div.datagrid-view2 div.datagrid-body tr.datagrid-row-selected").each(function(){
var id=$(this).attr("node-id");
rows.push(find(_775,id));
});
return rows;
};
function _777(_778,_779){
if(!_779){
return 0;
}
var opts=$.data(_778,"treegrid").options;
var view=$(_778).datagrid("getPanel").children("div.datagrid-view");
var node=view.find("div.datagrid-body tr[node-id=\""+_779+"\"]").children("td[field=\""+opts.treeField+"\"]");
return node.find("span.tree-indent,span.tree-hit").length;
};
function find(_77a,_77b){
var opts=$.data(_77a,"treegrid").options;
var data=$.data(_77a,"treegrid").data;
var cc=[data];
while(cc.length){
var c=cc.shift();
for(var i=0;i<c.length;i++){
var node=c[i];
if(node[opts.idField]==_77b){
return node;
}else{
if(node["children"]){
cc.push(node["children"]);
}
}
}
}
return null;
};
function _77c(_77d,_77e){
var opts=$.data(_77d,"treegrid").options;
var row=find(_77d,_77e);
var tr=opts.finder.getTr(_77d,_77e);
var hit=tr.find("span.tree-hit");
if(hit.length==0){
return;
}
if(hit.hasClass("tree-collapsed")){
return;
}
if(opts.onBeforeCollapse.call(_77d,row)==false){
return;
}
hit.removeClass("tree-expanded tree-expanded-hover").addClass("tree-collapsed");
hit.next().removeClass("tree-folder-open");
row.state="closed";
tr=tr.next("tr.treegrid-tr-tree");
var cc=tr.children("td").children("div");
if(opts.animate){
cc.slideUp("normal",function(){
$(_77d).treegrid("autoSizeColumn");
_73d(_77d,_77e);
opts.onCollapse.call(_77d,row);
});
}else{
cc.hide();
$(_77d).treegrid("autoSizeColumn");
_73d(_77d,_77e);
opts.onCollapse.call(_77d,row);
}
};
function _77f(_780,_781){
var opts=$.data(_780,"treegrid").options;
var tr=opts.finder.getTr(_780,_781);
var hit=tr.find("span.tree-hit");
var row=find(_780,_781);
if(hit.length==0){
return;
}
if(hit.hasClass("tree-expanded")){
return;
}
if(opts.onBeforeExpand.call(_780,row)==false){
return;
}
hit.removeClass("tree-collapsed tree-collapsed-hover").addClass("tree-expanded");
hit.next().addClass("tree-folder-open");
var _782=tr.next("tr.treegrid-tr-tree");
if(_782.length){
var cc=_782.children("td").children("div");
_783(cc);
}else{
_74b(_780,row[opts.idField]);
var _782=tr.next("tr.treegrid-tr-tree");
var cc=_782.children("td").children("div");
cc.hide();
var _784=$.extend({},opts.queryParams||{});
_784.id=row[opts.idField];
_73c(_780,row[opts.idField],_784,true,function(){
if(cc.is(":empty")){
_782.remove();
}else{
_783(cc);
}
});
}
function _783(cc){
row.state="open";
if(opts.animate){
cc.slideDown("normal",function(){
$(_780).treegrid("autoSizeColumn");
_73d(_780,_781);
opts.onExpand.call(_780,row);
});
}else{
cc.show();
$(_780).treegrid("autoSizeColumn");
_73d(_780,_781);
opts.onExpand.call(_780,row);
}
};
};
function _74a(_785,_786){
var opts=$.data(_785,"treegrid").options;
var tr=opts.finder.getTr(_785,_786);
var hit=tr.find("span.tree-hit");
if(hit.hasClass("tree-expanded")){
_77c(_785,_786);
}else{
_77f(_785,_786);
}
};
function _787(_788,_789){
var opts=$.data(_788,"treegrid").options;
var _78a=_741(_788,_789);
if(_789){
_78a.unshift(find(_788,_789));
}
for(var i=0;i<_78a.length;i++){
_77c(_788,_78a[i][opts.idField]);
}
};
function _78b(_78c,_78d){
var opts=$.data(_78c,"treegrid").options;
var _78e=_741(_78c,_78d);
if(_78d){
_78e.unshift(find(_78c,_78d));
}
for(var i=0;i<_78e.length;i++){
_77f(_78c,_78e[i][opts.idField]);
}
};
function _78f(_790,_791){
var opts=$.data(_790,"treegrid").options;
var ids=[];
var p=_767(_790,_791);
while(p){
var id=p[opts.idField];
ids.unshift(id);
p=_767(_790,id);
}
for(var i=0;i<ids.length;i++){
_77f(_790,ids[i]);
}
};
function _792(_793,_794){
var opts=$.data(_793,"treegrid").options;
if(_794.parent){
var tr=opts.finder.getTr(_793,_794.parent);
if(tr.next("tr.treegrid-tr-tree").length==0){
_74b(_793,_794.parent);
}
var cell=tr.children("td[field=\""+opts.treeField+"\"]").children("div.datagrid-cell");
var _795=cell.children("span.tree-icon");
if(_795.hasClass("tree-file")){
_795.removeClass("tree-file").addClass("tree-folder tree-folder-open");
var hit=$("<span class=\"tree-hit tree-expanded\"></span>").insertBefore(_795);
if(hit.prev().length){
hit.prev().remove();
}
}
}
_752(_793,_794.parent,_794.data,true);
};
function _796(_797,_798){
var ref=_798.before||_798.after;
var opts=$.data(_797,"treegrid").options;
var _799=_767(_797,ref);
_792(_797,{parent:(_799?_799[opts.idField]:null),data:[_798.data]});
_79a(true);
_79a(false);
_745(_797);
function _79a(_79b){
var _79c=_79b?1:2;
var tr=opts.finder.getTr(_797,_798.data[opts.idField],"body",_79c);
var _79d=tr.closest("table.datagrid-btable");
tr=tr.parent().children();
var dest=opts.finder.getTr(_797,ref,"body",_79c);
if(_798.before){
tr.insertBefore(dest);
}else{
var sub=dest.next("tr.treegrid-tr-tree");
tr.insertAfter(sub.length?sub:dest);
}
_79d.remove();
};
};
function _79e(_79f,_7a0){
var opts=$.data(_79f,"treegrid").options;
var tr=opts.finder.getTr(_79f,_7a0);
tr.next("tr.treegrid-tr-tree").remove();
tr.remove();
var _7a1=del(_7a0);
if(_7a1){
if(_7a1.children.length==0){
tr=opts.finder.getTr(_79f,_7a1[opts.idField]);
tr.next("tr.treegrid-tr-tree").remove();
var cell=tr.children("td[field=\""+opts.treeField+"\"]").children("div.datagrid-cell");
cell.find(".tree-icon").removeClass("tree-folder").addClass("tree-file");
cell.find(".tree-hit").remove();
$("<span class=\"tree-indent\"></span>").prependTo(cell);
}
}
_745(_79f);
function del(id){
var cc;
var _7a2=_767(_79f,_7a0);
if(_7a2){
cc=_7a2.children;
}else{
cc=$(_79f).treegrid("getData");
}
for(var i=0;i<cc.length;i++){
if(cc[i][opts.idField]==id){
cc.splice(i,1);
break;
}
}
return _7a2;
};
};
$.fn.treegrid=function(_7a3,_7a4){
if(typeof _7a3=="string"){
var _7a5=$.fn.treegrid.methods[_7a3];
if(_7a5){
return _7a5(this,_7a4);
}else{
return this.datagrid(_7a3,_7a4);
}
}
_7a3=_7a3||{};
return this.each(function(){
var _7a6=$.data(this,"treegrid");
if(_7a6){
$.extend(_7a6.options,_7a3);
}else{
_7a6=$.data(this,"treegrid",{options:$.extend({},$.fn.treegrid.defaults,$.fn.treegrid.parseOptions(this),_7a3),data:[]});
}
_723(this);
if(_7a6.options.data){
$(this).treegrid("loadData",_7a6.options.data);
}
_73c(this);
_747(this);
});
};
$.fn.treegrid.methods={options:function(jq){
return $.data(jq[0],"treegrid").options;
},resize:function(jq,_7a7){
return jq.each(function(){
$(this).datagrid("resize",_7a7);
});
},fixRowHeight:function(jq,_7a8){
return jq.each(function(){
_73d(this,_7a8);
});
},loadData:function(jq,data){
return jq.each(function(){
_752(this,data.parent,data);
});
},load:function(jq,_7a9){
return jq.each(function(){
$(this).treegrid("options").pageNumber=1;
$(this).treegrid("getPager").pagination({pageNumber:1});
$(this).treegrid("reload",_7a9);
});
},reload:function(jq,id){
return jq.each(function(){
var opts=$(this).treegrid("options");
var _7aa={};
if(typeof id=="object"){
_7aa=id;
}else{
_7aa=$.extend({},opts.queryParams);
_7aa.id=id;
}
if(_7aa.id){
var node=$(this).treegrid("find",_7aa.id);
if(node.children){
node.children.splice(0,node.children.length);
}
opts.queryParams=_7aa;
var tr=opts.finder.getTr(this,_7aa.id);
tr.next("tr.treegrid-tr-tree").remove();
tr.find("span.tree-hit").removeClass("tree-expanded tree-expanded-hover").addClass("tree-collapsed");
_77f(this,_7aa.id);
}else{
_73c(this,null,_7aa);
}
});
},reloadFooter:function(jq,_7ab){
return jq.each(function(){
var opts=$.data(this,"treegrid").options;
var dc=$.data(this,"datagrid").dc;
if(_7ab){
$.data(this,"treegrid").footer=_7ab;
}
if(opts.showFooter){
opts.view.renderFooter.call(opts.view,this,dc.footer1,true);
opts.view.renderFooter.call(opts.view,this,dc.footer2,false);
if(opts.view.onAfterRender){
opts.view.onAfterRender.call(opts.view,this);
}
$(this).treegrid("fixRowHeight");
}
});
},getData:function(jq){
return $.data(jq[0],"treegrid").data;
},getFooterRows:function(jq){
return $.data(jq[0],"treegrid").footer;
},getRoot:function(jq){
return _763(jq[0]);
},getRoots:function(jq){
return _765(jq[0]);
},getParent:function(jq,id){
return _767(jq[0],id);
},getChildren:function(jq,id){
return _741(jq[0],id);
},getSelected:function(jq){
return _772(jq[0]);
},getSelections:function(jq){
return _774(jq[0]);
},getLevel:function(jq,id){
return _777(jq[0],id);
},find:function(jq,id){
return find(jq[0],id);
},isLeaf:function(jq,id){
var opts=$.data(jq[0],"treegrid").options;
var tr=opts.finder.getTr(jq[0],id);
var hit=tr.find("span.tree-hit");
return hit.length==0;
},select:function(jq,id){
return jq.each(function(){
$(this).datagrid("selectRow",id);
});
},unselect:function(jq,id){
return jq.each(function(){
$(this).datagrid("unselectRow",id);
});
},collapse:function(jq,id){
return jq.each(function(){
_77c(this,id);
});
},expand:function(jq,id){
return jq.each(function(){
_77f(this,id);
});
},toggle:function(jq,id){
return jq.each(function(){
_74a(this,id);
});
},collapseAll:function(jq,id){
return jq.each(function(){
_787(this,id);
});
},expandAll:function(jq,id){
return jq.each(function(){
_78b(this,id);
});
},expandTo:function(jq,id){
return jq.each(function(){
_78f(this,id);
});
},append:function(jq,_7ac){
return jq.each(function(){
_792(this,_7ac);
});
},insert:function(jq,_7ad){
return jq.each(function(){
_796(this,_7ad);
});
},remove:function(jq,id){
return jq.each(function(){
_79e(this,id);
});
},pop:function(jq,id){
var row=jq.treegrid("find",id);
jq.treegrid("remove",id);
return row;
},refresh:function(jq,id){
return jq.each(function(){
var opts=$.data(this,"treegrid").options;
opts.view.refreshRow.call(opts.view,this,id);
});
},update:function(jq,_7ae){
return jq.each(function(){
var opts=$.data(this,"treegrid").options;
opts.view.updateRow.call(opts.view,this,_7ae.id,_7ae.row);
});
},beginEdit:function(jq,id){
return jq.each(function(){
$(this).datagrid("beginEdit",id);
$(this).treegrid("fixRowHeight",id);
});
},endEdit:function(jq,id){
return jq.each(function(){
$(this).datagrid("endEdit",id);
});
},cancelEdit:function(jq,id){
return jq.each(function(){
$(this).datagrid("cancelEdit",id);
});
}};
$.fn.treegrid.parseOptions=function(_7af){
return $.extend({},$.fn.datagrid.parseOptions(_7af),$.parser.parseOptions(_7af,["treeField",{animate:"boolean"}]));
};
var _7b0=$.extend({},$.fn.datagrid.defaults.view,{render:function(_7b1,_7b2,_7b3){
var opts=$.data(_7b1,"treegrid").options;
var _7b4=$(_7b1).datagrid("getColumnFields",_7b3);
var _7b5=$.data(_7b1,"datagrid").rowIdPrefix;
if(_7b3){
if(!(opts.rownumbers||(opts.frozenColumns&&opts.frozenColumns.length))){
return;
}
}
var _7b6=0;
var view=this;
var _7b7=_7b8(_7b3,this.treeLevel,this.treeNodes);
$(_7b2).append(_7b7.join(""));
function _7b8(_7b9,_7ba,_7bb){
var _7bc=["<table class=\"datagrid-btable\" cellspacing=\"0\" cellpadding=\"0\" border=\"0\"><tbody>"];
for(var i=0;i<_7bb.length;i++){
var row=_7bb[i];
if(row.state!="open"&&row.state!="closed"){
row.state="open";
}
var css=opts.rowStyler?opts.rowStyler.call(_7b1,row):"";
var _7bd="";
var _7be="";
if(typeof css=="string"){
_7be=css;
}else{
if(css){
_7bd=css["class"]||"";
_7be=css["style"]||"";
}
}
var cls="class=\"datagrid-row "+(_7b6++%2&&opts.striped?"datagrid-row-alt ":" ")+_7bd+"\"";
var _7bf=_7be?"style=\""+_7be+"\"":"";
var _7c0=_7b5+"-"+(_7b9?1:2)+"-"+row[opts.idField];
_7bc.push("<tr id=\""+_7c0+"\" node-id=\""+row[opts.idField]+"\" "+cls+" "+_7bf+">");
_7bc=_7bc.concat(view.renderRow.call(view,_7b1,_7b4,_7b9,_7ba,row));
_7bc.push("</tr>");
if(row.children&&row.children.length){
var tt=_7b8(_7b9,_7ba+1,row.children);
var v=row.state=="closed"?"none":"block";
_7bc.push("<tr class=\"treegrid-tr-tree\"><td style=\"border:0px\" colspan="+(_7b4.length+(opts.rownumbers?1:0))+"><div style=\"display:"+v+"\">");
_7bc=_7bc.concat(tt);
_7bc.push("</div></td></tr>");
}
}
_7bc.push("</tbody></table>");
return _7bc;
};
},renderFooter:function(_7c1,_7c2,_7c3){
var opts=$.data(_7c1,"treegrid").options;
var rows=$.data(_7c1,"treegrid").footer||[];
var _7c4=$(_7c1).datagrid("getColumnFields",_7c3);
var _7c5=["<table class=\"datagrid-ftable\" cellspacing=\"0\" cellpadding=\"0\" border=\"0\"><tbody>"];
for(var i=0;i<rows.length;i++){
var row=rows[i];
row[opts.idField]=row[opts.idField]||("foot-row-id"+i);
_7c5.push("<tr class=\"datagrid-row\" node-id=\""+row[opts.idField]+"\">");
_7c5.push(this.renderRow.call(this,_7c1,_7c4,_7c3,0,row));
_7c5.push("</tr>");
}
_7c5.push("</tbody></table>");
$(_7c2).html(_7c5.join(""));
},renderRow:function(_7c6,_7c7,_7c8,_7c9,row){
var opts=$.data(_7c6,"treegrid").options;
var cc=[];
if(_7c8&&opts.rownumbers){
cc.push("<td class=\"datagrid-td-rownumber\"><div class=\"datagrid-cell-rownumber\">0</div></td>");
}
for(var i=0;i<_7c7.length;i++){
var _7ca=_7c7[i];
var col=$(_7c6).datagrid("getColumnOption",_7ca);
if(col){
var css=col.styler?(col.styler(row[_7ca],row)||""):"";
var _7cb="";
var _7cc="";
if(typeof css=="string"){
_7cc=css;
}else{
if(cc){
_7cb=css["class"]||"";
_7cc=css["style"]||"";
}
}
var cls=_7cb?"class=\""+_7cb+"\"":"";
var _7cd=col.hidden?"style=\"display:none;"+_7cc+"\"":(_7cc?"style=\""+_7cc+"\"":"");
cc.push("<td field=\""+_7ca+"\" "+cls+" "+_7cd+">");
if(col.checkbox){
var _7cd="";
}else{
var _7cd=_7cc;
if(col.align){
_7cd+=";text-align:"+col.align+";";
}
if(!opts.nowrap){
_7cd+=";white-space:normal;height:auto;";
}else{
if(opts.autoRowHeight){
_7cd+=";height:auto;";
}
}
}
cc.push("<div style=\""+_7cd+"\" ");
if(col.checkbox){
cc.push("class=\"datagrid-cell-check ");
}else{
cc.push("class=\"datagrid-cell "+col.cellClass);
}
cc.push("\">");
if(col.checkbox){
if(row.checked){
cc.push("<input type=\"checkbox\" checked=\"checked\"");
}else{
cc.push("<input type=\"checkbox\"");
}
cc.push(" name=\""+_7ca+"\" value=\""+(row[_7ca]!=undefined?row[_7ca]:"")+"\"/>");
}else{
var val=null;
if(col.formatter){
val=col.formatter(row[_7ca],row);
}else{
val=row[_7ca];
}
if(_7ca==opts.treeField){
for(var j=0;j<_7c9;j++){
cc.push("<span class=\"tree-indent\"></span>");
}
if(row.state=="closed"){
cc.push("<span class=\"tree-hit tree-collapsed\"></span>");
cc.push("<span class=\"tree-icon tree-folder "+(row.iconCls?row.iconCls:"")+"\"></span>");
}else{
if(row.children&&row.children.length){
cc.push("<span class=\"tree-hit tree-expanded\"></span>");
cc.push("<span class=\"tree-icon tree-folder tree-folder-open "+(row.iconCls?row.iconCls:"")+"\"></span>");
}else{
cc.push("<span class=\"tree-indent\"></span>");
cc.push("<span class=\"tree-icon tree-file "+(row.iconCls?row.iconCls:"")+"\"></span>");
}
}
cc.push("<span class=\"tree-title\">"+val+"</span>");
}else{
cc.push(val);
}
}
cc.push("</div>");
cc.push("</td>");
}
}
return cc.join("");
},refreshRow:function(_7ce,id){
this.updateRow.call(this,_7ce,id,{});
},updateRow:function(_7cf,id,row){
var opts=$.data(_7cf,"treegrid").options;
var _7d0=$(_7cf).treegrid("find",id);
$.extend(_7d0,row);
var _7d1=$(_7cf).treegrid("getLevel",id)-1;
var _7d2=opts.rowStyler?opts.rowStyler.call(_7cf,_7d0):"";
function _7d3(_7d4){
var _7d5=$(_7cf).treegrid("getColumnFields",_7d4);
var tr=opts.finder.getTr(_7cf,id,"body",(_7d4?1:2));
var _7d6=tr.find("div.datagrid-cell-rownumber").html();
var _7d7=tr.find("div.datagrid-cell-check input[type=checkbox]").is(":checked");
tr.html(this.renderRow(_7cf,_7d5,_7d4,_7d1,_7d0));
tr.attr("style",_7d2||"");
tr.find("div.datagrid-cell-rownumber").html(_7d6);
if(_7d7){
tr.find("div.datagrid-cell-check input[type=checkbox]")._propAttr("checked",true);
}
};
_7d3.call(this,true);
_7d3.call(this,false);
$(_7cf).treegrid("fixRowHeight",id);
},onBeforeRender:function(_7d8,_7d9,data){
if($.isArray(_7d9)){
data={total:_7d9.length,rows:_7d9};
_7d9=null;
}
if(!data){
return false;
}
var _7da=$.data(_7d8,"treegrid");
var opts=_7da.options;
if(data.length==undefined){
if(data.footer){
_7da.footer=data.footer;
}
if(data.total){
_7da.total=data.total;
}
data=this.transfer(_7d8,_7d9,data.rows);
}else{
function _7db(_7dc,_7dd){
for(var i=0;i<_7dc.length;i++){
var row=_7dc[i];
row._parentId=_7dd;
if(row.children&&row.children.length){
_7db(row.children,row[opts.idField]);
}
}
};
_7db(data,_7d9);
}
var node=find(_7d8,_7d9);
if(node){
if(node.children){
node.children=node.children.concat(data);
}else{
node.children=data;
}
}else{
_7da.data=_7da.data.concat(data);
}
this.sort(_7d8,data);
this.treeNodes=data;
this.treeLevel=$(_7d8).treegrid("getLevel",_7d9);
},sort:function(_7de,data){
var opts=$.data(_7de,"treegrid").options;
if(!opts.remoteSort&&opts.sortName){
var _7df=opts.sortName.split(",");
var _7e0=opts.sortOrder.split(",");
_7e1(data);
}
function _7e1(rows){
rows.sort(function(r1,r2){
var r=0;
for(var i=0;i<_7df.length;i++){
var sn=_7df[i];
var so=_7e0[i];
var col=$(_7de).treegrid("getColumnOption",sn);
var _7e2=col.sorter||function(a,b){
return a==b?0:(a>b?1:-1);
};
r=_7e2(r1[sn],r2[sn])*(so=="asc"?1:-1);
if(r!=0){
return r;
}
}
return r;
});
for(var i=0;i<rows.length;i++){
var _7e3=rows[i].children;
if(_7e3&&_7e3.length){
_7e1(_7e3);
}
}
};
},transfer:function(_7e4,_7e5,data){
var opts=$.data(_7e4,"treegrid").options;
var rows=[];
for(var i=0;i<data.length;i++){
rows.push(data[i]);
}
var _7e6=[];
for(var i=0;i<rows.length;i++){
var row=rows[i];
if(!_7e5){
if(!row._parentId){
_7e6.push(row);
rows.splice(i,1);
i--;
}
}else{
if(row._parentId==_7e5){
_7e6.push(row);
rows.splice(i,1);
i--;
}
}
}
var toDo=[];
for(var i=0;i<_7e6.length;i++){
toDo.push(_7e6[i]);
}
while(toDo.length){
var node=toDo.shift();
for(var i=0;i<rows.length;i++){
var row=rows[i];
if(row._parentId==node[opts.idField]){
if(node.children){
node.children.push(row);
}else{
node.children=[row];
}
toDo.push(row);
rows.splice(i,1);
i--;
}
}
}
return _7e6;
}});
$.fn.treegrid.defaults=$.extend({},$.fn.datagrid.defaults,{treeField:null,animate:false,singleSelect:true,view:_7b0,loader:function(_7e7,_7e8,_7e9){
var opts=$(this).treegrid("options");
if(!opts.url){
return false;
}
$.ajax({type:opts.method,url:opts.url,data:_7e7,dataType:"json",success:function(data){
_7e8(data);
},error:function(){
_7e9.apply(this,arguments);
}});
},loadFilter:function(data,_7ea){
return data;
},finder:{getTr:function(_7eb,id,type,_7ec){
type=type||"body";
_7ec=_7ec||0;
var dc=$.data(_7eb,"datagrid").dc;
if(_7ec==0){
var opts=$.data(_7eb,"treegrid").options;
var tr1=opts.finder.getTr(_7eb,id,type,1);
var tr2=opts.finder.getTr(_7eb,id,type,2);
return tr1.add(tr2);
}else{
if(type=="body"){
var tr=$("#"+$.data(_7eb,"datagrid").rowIdPrefix+"-"+_7ec+"-"+id);
if(!tr.length){
tr=(_7ec==1?dc.body1:dc.body2).find("tr[node-id=\""+id+"\"]");
}
return tr;
}else{
if(type=="footer"){
return (_7ec==1?dc.footer1:dc.footer2).find("tr[node-id=\""+id+"\"]");
}else{
if(type=="selected"){
return (_7ec==1?dc.body1:dc.body2).find("tr.datagrid-row-selected");
}else{
if(type=="highlight"){
return (_7ec==1?dc.body1:dc.body2).find("tr.datagrid-row-over");
}else{
if(type=="checked"){
return (_7ec==1?dc.body1:dc.body2).find("tr.datagrid-row-checked");
}else{
if(type=="last"){
return (_7ec==1?dc.body1:dc.body2).find("tr:last[node-id]");
}else{
if(type=="allbody"){
return (_7ec==1?dc.body1:dc.body2).find("tr[node-id]");
}else{
if(type=="allfooter"){
return (_7ec==1?dc.footer1:dc.footer2).find("tr[node-id]");
}
}
}
}
}
}
}
}
}
},getRow:function(_7ed,p){
var id=(typeof p=="object")?p.attr("node-id"):p;
return $(_7ed).treegrid("find",id);
}},onBeforeLoad:function(row,_7ee){
},onLoadSuccess:function(row,data){
},onLoadError:function(){
},onBeforeCollapse:function(row){
},onCollapse:function(row){
},onBeforeExpand:function(row){
},onExpand:function(row){
},onClickRow:function(row){
},onDblClickRow:function(row){
},onClickCell:function(_7ef,row){
},onDblClickCell:function(_7f0,row){
},onContextMenu:function(e,row){
},onBeforeEdit:function(row){
},onAfterEdit:function(row,_7f1){
},onCancelEdit:function(row){
}});
})(jQuery);
(function($){
function _7f2(_7f3,_7f4){
var _7f5=$.data(_7f3,"combo");
var opts=_7f5.options;
var _7f6=_7f5.combo;
var _7f7=_7f5.panel;
if(_7f4){
opts.width=_7f4;
}
if(isNaN(opts.width)){
var c=$(_7f3).clone();
c.css("visibility","hidden");
c.appendTo("body");
opts.width=c.outerWidth();
c.remove();
}
_7f6.appendTo("body");
var _7f8=_7f6.find("input.combo-text");
var _7f9=_7f6.find(".combo-arrow");
var _7fa=opts.hasDownArrow?_7f9._outerWidth():0;
_7f6._outerWidth(opts.width)._outerHeight(opts.height);
_7f8._outerWidth(_7f6.width()-_7fa);
_7f8.css({height:_7f6.height()+"px",lineHeight:_7f6.height()+"px"});
_7f9._outerHeight(_7f6.height());
_7f7.panel("resize",{width:(opts.panelWidth?opts.panelWidth:_7f6.outerWidth()),height:opts.panelHeight});
_7f6.insertAfter(_7f3);
};
function init(_7fb){
$(_7fb).addClass("combo-f").hide();
var span=$("<span class=\"combo\">"+"<input type=\"text\" class=\"combo-text\" autocomplete=\"off\">"+"<span><span class=\"combo-arrow\"></span></span>"+"<input type=\"hidden\" class=\"combo-value\">"+"</span>").insertAfter(_7fb);
var _7fc=$("<div class=\"combo-panel\"></div>").appendTo("body");
_7fc.panel({doSize:false,closed:true,cls:"combo-p",style:{position:"absolute",zIndex:10},onOpen:function(){
$(this).panel("resize");
},onClose:function(){
var _7fd=$.data(_7fb,"combo");
if(_7fd){
_7fd.options.onHidePanel.call(_7fb);
}
}});
var name=$(_7fb).attr("name");
if(name){
span.find("input.combo-value").attr("name",name);
$(_7fb).removeAttr("name").attr("comboName",name);
}
return {combo:span,panel:_7fc};
};
function _7fe(_7ff){
var _800=$.data(_7ff,"combo");
var opts=_800.options;
var _801=_800.combo;
if(opts.hasDownArrow){
_801.find(".combo-arrow").show();
}else{
_801.find(".combo-arrow").hide();
}
_802(_7ff,opts.disabled);
_803(_7ff,opts.readonly);
};
function _804(_805){
var _806=$.data(_805,"combo");
var _807=_806.combo.find("input.combo-text");
_807.validatebox("destroy");
_806.panel.panel("destroy");
_806.combo.remove();
$(_805).remove();
};
function _808(_809){
$(_809).find(".combo-f").each(function(){
var p=$(this).combo("panel");
if(p.is(":visible")){
p.panel("close");
}
});
};
function _80a(_80b){
var _80c=$.data(_80b,"combo");
var opts=_80c.options;
var _80d=_80c.panel;
var _80e=_80c.combo;
var _80f=_80e.find(".combo-text");
var _810=_80e.find(".combo-arrow");
$(document).unbind(".combo").bind("mousedown.combo",function(e){
var p=$(e.target).closest("span.combo,div.combo-p");
if(p.length){
_808(p);
return;
}
$("body>div.combo-p>div.combo-panel:visible").panel("close");
});
_80f.unbind(".combo");
_810.unbind(".combo");
if(!opts.disabled&&!opts.readonly){
_80f.bind("click.combo",function(e){
if(!opts.editable){
_811.call(this);
}else{
var p=$(this).closest("div.combo-panel");
$("div.combo-panel:visible").not(_80d).not(p).panel("close");
}
}).bind("keydown.combo",function(e){
switch(e.keyCode){
case 38:
opts.keyHandler.up.call(_80b,e);
break;
case 40:
opts.keyHandler.down.call(_80b,e);
break;
case 37:
opts.keyHandler.left.call(_80b,e);
break;
case 39:
opts.keyHandler.right.call(_80b,e);
break;
case 13:
e.preventDefault();
opts.keyHandler.enter.call(_80b,e);
return false;
case 9:
case 27:
_812(_80b);
break;
default:
if(opts.editable){
if(_80c.timer){
clearTimeout(_80c.timer);
}
_80c.timer=setTimeout(function(){
var q=_80f.val();
if(_80c.previousValue!=q){
_80c.previousValue=q;
$(_80b).combo("showPanel");
opts.keyHandler.query.call(_80b,_80f.val(),e);
$(_80b).combo("validate");
}
},opts.delay);
}
}
});
_810.bind("click.combo",function(){
_811.call(this);
}).bind("mouseenter.combo",function(){
$(this).addClass("combo-arrow-hover");
}).bind("mouseleave.combo",function(){
$(this).removeClass("combo-arrow-hover");
});
}
function _811(){
if(_80d.is(":visible")){
_808(_80d);
_812(_80b);
}else{
var p=$(this).closest("div.combo-panel");
$("div.combo-panel:visible").not(_80d).not(p).panel("close");
$(_80b).combo("showPanel");
}
_80f.focus();
};
};
function _813(_814){
var opts=$.data(_814,"combo").options;
var _815=$.data(_814,"combo").combo;
var _816=$.data(_814,"combo").panel;
if($.fn.window){
_816.panel("panel").css("z-index",$.fn.window.defaults.zIndex++);
}
_816.panel("move",{left:_815.offset().left,top:_817()});
if(_816.panel("options").closed){
_816.panel("open");
opts.onShowPanel.call(_814);
}
(function(){
if(_816.is(":visible")){
_816.panel("move",{left:_818(),top:_817()});
setTimeout(arguments.callee,200);
}
})();
function _818(){
var left=_815.offset().left;
if(left+_816._outerWidth()>$(window)._outerWidth()+$(document).scrollLeft()){
left=$(window)._outerWidth()+$(document).scrollLeft()-_816._outerWidth();
}
if(left<0){
left=0;
}
return left;
};
function _817(){
var top=_815.offset().top+_815._outerHeight();
if(top+_816._outerHeight()>$(window)._outerHeight()+$(document).scrollTop()){
top=_815.offset().top-_816._outerHeight();
}
if(top<$(document).scrollTop()){
top=_815.offset().top+_815._outerHeight();
}
return top;
};
};
function _812(_819){
var _81a=$.data(_819,"combo").panel;
_81a.panel("close");
};
function _81b(_81c){
var opts=$.data(_81c,"combo").options;
var _81d=$(_81c).combo("textbox");
_81d.validatebox($.extend({},opts,{deltaX:(opts.hasDownArrow?opts.deltaX:(opts.deltaX>0?1:-1))}));
};
function _802(_81e,_81f){
var _820=$.data(_81e,"combo");
var opts=_820.options;
var _821=_820.combo;
if(_81f){
opts.disabled=true;
$(_81e).attr("disabled",true);
_821.find(".combo-value").attr("disabled",true);
_821.find(".combo-text").attr("disabled",true);
}else{
opts.disabled=false;
$(_81e).removeAttr("disabled");
_821.find(".combo-value").removeAttr("disabled");
_821.find(".combo-text").removeAttr("disabled");
}
};
function _803(_822,mode){
var _823=$.data(_822,"combo");
var opts=_823.options;
opts.readonly=mode==undefined?true:mode;
var _824=opts.readonly?true:(!opts.editable);
_823.combo.find(".combo-text").attr("readonly",_824).css("cursor",_824?"pointer":"");
};
function _825(_826){
var _827=$.data(_826,"combo");
var opts=_827.options;
var _828=_827.combo;
if(opts.multiple){
_828.find("input.combo-value").remove();
}else{
_828.find("input.combo-value").val("");
}
_828.find("input.combo-text").val("");
};
function _829(_82a){
var _82b=$.data(_82a,"combo").combo;
return _82b.find("input.combo-text").val();
};
function _82c(_82d,text){
var _82e=$.data(_82d,"combo");
var _82f=_82e.combo.find("input.combo-text");
if(_82f.val()!=text){
_82f.val(text);
$(_82d).combo("validate");
_82e.previousValue=text;
}
};
function _830(_831){
var _832=[];
var _833=$.data(_831,"combo").combo;
_833.find("input.combo-value").each(function(){
_832.push($(this).val());
});
return _832;
};
function _834(_835,_836){
var opts=$.data(_835,"combo").options;
var _837=_830(_835);
var _838=$.data(_835,"combo").combo;
_838.find("input.combo-value").remove();
var name=$(_835).attr("comboName");
for(var i=0;i<_836.length;i++){
var _839=$("<input type=\"hidden\" class=\"combo-value\">").appendTo(_838);
if(name){
_839.attr("name",name);
}
_839.val(_836[i]);
}
var tmp=[];
for(var i=0;i<_837.length;i++){
tmp[i]=_837[i];
}
var aa=[];
for(var i=0;i<_836.length;i++){
for(var j=0;j<tmp.length;j++){
if(_836[i]==tmp[j]){
aa.push(_836[i]);
tmp.splice(j,1);
break;
}
}
}
if(aa.length!=_836.length||_836.length!=_837.length){
if(opts.multiple){
opts.onChange.call(_835,_836,_837);
}else{
opts.onChange.call(_835,_836[0],_837[0]);
}
}
};
function _83a(_83b){
var _83c=_830(_83b);
return _83c[0];
};
function _83d(_83e,_83f){
_834(_83e,[_83f]);
};
function _840(_841){
var opts=$.data(_841,"combo").options;
var fn=opts.onChange;
opts.onChange=function(){
};
if(opts.multiple){
if(opts.value){
if(typeof opts.value=="object"){
_834(_841,opts.value);
}else{
_83d(_841,opts.value);
}
}else{
_834(_841,[]);
}
opts.originalValue=_830(_841);
}else{
_83d(_841,opts.value);
opts.originalValue=opts.value;
}
opts.onChange=fn;
};
$.fn.combo=function(_842,_843){
if(typeof _842=="string"){
var _844=$.fn.combo.methods[_842];
if(_844){
return _844(this,_843);
}else{
return this.each(function(){
var _845=$(this).combo("textbox");
_845.validatebox(_842,_843);
});
}
}
_842=_842||{};
return this.each(function(){
var _846=$.data(this,"combo");
if(_846){
$.extend(_846.options,_842);
}else{
var r=init(this);
_846=$.data(this,"combo",{options:$.extend({},$.fn.combo.defaults,$.fn.combo.parseOptions(this),_842),combo:r.combo,panel:r.panel,previousValue:null});
$(this).removeAttr("disabled");
}
_7fe(this);
_7f2(this);
_80a(this);
_81b(this);
_840(this);
});
};
$.fn.combo.methods={options:function(jq){
return $.data(jq[0],"combo").options;
},panel:function(jq){
return $.data(jq[0],"combo").panel;
},textbox:function(jq){
return $.data(jq[0],"combo").combo.find("input.combo-text");
},destroy:function(jq){
return jq.each(function(){
_804(this);
});
},resize:function(jq,_847){
return jq.each(function(){
_7f2(this,_847);
});
},showPanel:function(jq){
return jq.each(function(){
_813(this);
});
},hidePanel:function(jq){
return jq.each(function(){
_812(this);
});
},disable:function(jq){
return jq.each(function(){
_802(this,true);
_80a(this);
});
},enable:function(jq){
return jq.each(function(){
_802(this,false);
_80a(this);
});
},readonly:function(jq,mode){
return jq.each(function(){
_803(this,mode);
_80a(this);
});
},isValid:function(jq){
var _848=$.data(jq[0],"combo").combo.find("input.combo-text");
return _848.validatebox("isValid");
},clear:function(jq){
return jq.each(function(){
_825(this);
});
},reset:function(jq){
return jq.each(function(){
var opts=$.data(this,"combo").options;
if(opts.multiple){
$(this).combo("setValues",opts.originalValue);
}else{
$(this).combo("setValue",opts.originalValue);
}
});
},getText:function(jq){
return _829(jq[0]);
},setText:function(jq,text){
return jq.each(function(){
_82c(this,text);
});
},getValues:function(jq){
return _830(jq[0]);
},setValues:function(jq,_849){
return jq.each(function(){
_834(this,_849);
});
},getValue:function(jq){
return _83a(jq[0]);
},setValue:function(jq,_84a){
return jq.each(function(){
_83d(this,_84a);
});
}};
$.fn.combo.parseOptions=function(_84b){
var t=$(_84b);
return $.extend({},$.fn.validatebox.parseOptions(_84b),$.parser.parseOptions(_84b,["width","height","separator",{panelWidth:"number",editable:"boolean",hasDownArrow:"boolean",delay:"number",selectOnNavigation:"boolean"}]),{panelHeight:(t.attr("panelHeight")=="auto"?"auto":parseInt(t.attr("panelHeight"))||undefined),multiple:(t.attr("multiple")?true:undefined),disabled:(t.attr("disabled")?true:undefined),readonly:(t.attr("readonly")?true:undefined),value:(t.val()||undefined)});
};
$.fn.combo.defaults=$.extend({},$.fn.validatebox.defaults,{width:"auto",height:22,panelWidth:null,panelHeight:200,multiple:false,selectOnNavigation:true,separator:",",editable:true,disabled:false,readonly:false,hasDownArrow:true,value:"",delay:200,deltaX:19,keyHandler:{up:function(e){
},down:function(e){
},left:function(e){
},right:function(e){
},enter:function(e){
},query:function(q,e){
}},onShowPanel:function(){
},onHidePanel:function(){
},onChange:function(_84c,_84d){
}});
})(jQuery);
(function($){
function _84e(_84f,_850,_851,_852){
var _853=$.data(_84f,"combobox");
var opts=_853.options;
if(_852){
return _854(_853.groups,_851,_850);
}else{
return _854(_853.data,(_851?_851:_853.options.valueField),_850);
}
function _854(data,key,_855){
for(var i=0;i<data.length;i++){
var row=data[i];
if(row[key]==_855){
return row;
}
}
return null;
};
};
function _856(_857,_858){
var _859=$(_857).combo("panel");
var row=_84e(_857,_858);
if(row){
var item=$("#"+row.domId);
if(item.position().top<=0){
var h=_859.scrollTop()+item.position().top;
_859.scrollTop(h);
}else{
if(item.position().top+item.outerHeight()>_859.height()){
var h=_859.scrollTop()+item.position().top+item.outerHeight()-_859.height();
_859.scrollTop(h);
}
}
}
};
function nav(_85a,dir){
var opts=$.data(_85a,"combobox").options;
var _85b=$(_85a).combobox("panel");
var item=_85b.children("div.combobox-item-hover");
if(!item.length){
item=_85b.children("div.combobox-item-selected");
}
item.removeClass("combobox-item-hover");
var _85c="div.combobox-item:visible:not(.combobox-item-disabled):first";
var _85d="div.combobox-item:visible:not(.combobox-item-disabled):last";
if(!item.length){
item=_85b.children(dir=="next"?_85c:_85d);
}else{
if(dir=="next"){
item=item.nextAll(_85c);
if(!item.length){
item=_85b.children(_85c);
}
}else{
item=item.prevAll(_85c);
if(!item.length){
item=_85b.children(_85d);
}
}
}
if(item.length){
item.addClass("combobox-item-hover");
var row=_84e(_85a,item.attr("id"),"domId");
if(row){
_856(_85a,row[opts.valueField]);
if(opts.selectOnNavigation){
_85e(_85a,row[opts.valueField]);
}
}
}
};
function _85e(_85f,_860){
var opts=$.data(_85f,"combobox").options;
var _861=$(_85f).combo("getValues");
if($.inArray(_860+"",_861)==-1){
if(opts.multiple){
_861.push(_860);
}else{
_861=[_860];
}
_862(_85f,_861);
opts.onSelect.call(_85f,_84e(_85f,_860));
}
};
function _863(_864,_865){
var opts=$.data(_864,"combobox").options;
var _866=$(_864).combo("getValues");
var _867=$.inArray(_865+"",_866);
if(_867>=0){
_866.splice(_867,1);
_862(_864,_866);
opts.onUnselect.call(_864,_84e(_864,_865));
}
};
function _862(_868,_869,_86a){
var opts=$.data(_868,"combobox").options;
var _86b=$(_868).combo("panel");
_86b.find("div.combobox-item-selected").removeClass("combobox-item-selected");
var vv=[],ss=[];
for(var i=0;i<_869.length;i++){
var v=_869[i];
var s=v;
var row=_84e(_868,v);
if(row){
s=row[opts.textField];
$("#"+row.domId).addClass("combobox-item-selected");
}
vv.push(v);
ss.push(s);
}
$(_868).combo("setValues",vv);
if(!_86a){
$(_868).combo("setText",ss.join(opts.separator));
}
};
var _86c=1;
function _86d(_86e,data,_86f){
var _870=$.data(_86e,"combobox");
var opts=_870.options;
_870.data=opts.loadFilter.call(_86e,data);
_870.groups=[];
data=_870.data;
var _871=$(_86e).combobox("getValues");
var dd=[];
var _872=undefined;
for(var i=0;i<data.length;i++){
var row=data[i];
var v=row[opts.valueField]+"";
var s=row[opts.textField];
var g=row[opts.groupField];
if(g){
if(_872!=g){
_872=g;
var grow={value:g,domId:("_easyui_combobox_"+_86c++)};
_870.groups.push(grow);
dd.push("<div id=\""+grow.domId+"\" class=\"combobox-group\">");
dd.push(opts.groupFormatter?opts.groupFormatter.call(_86e,g):g);
dd.push("</div>");
}
}else{
_872=undefined;
}
var cls="combobox-item"+(row.disabled?" combobox-item-disabled":"")+(g?" combobox-gitem":"");
row.domId="_easyui_combobox_"+_86c++;
dd.push("<div id=\""+row.domId+"\" class=\""+cls+"\">");
dd.push(opts.formatter?opts.formatter.call(_86e,row):s);
dd.push("</div>");
if(row["selected"]&&$.inArray(v,_871)==-1){
_871.push(v);
}
}
$(_86e).combo("panel").html(dd.join(""));
if(opts.multiple){
_862(_86e,_871,_86f);
}else{
_862(_86e,_871.length?[_871[_871.length-1]]:[],_86f);
}
opts.onLoadSuccess.call(_86e,data);
};
function _873(_874,url,_875,_876){
var opts=$.data(_874,"combobox").options;
if(url){
opts.url=url;
}
_875=_875||{};
if(opts.onBeforeLoad.call(_874,_875)==false){
return;
}
opts.loader.call(_874,_875,function(data){
_86d(_874,data,_876);
},function(){
opts.onLoadError.apply(this,arguments);
});
};
function _877(_878,q){
var _879=$.data(_878,"combobox");
var opts=_879.options;
if(opts.multiple&&!q){
_862(_878,[],true);
}else{
_862(_878,[q],true);
}
if(opts.mode=="remote"){
_873(_878,null,{q:q},true);
}else{
var _87a=$(_878).combo("panel");
_87a.find("div.combobox-item,div.combobox-group").hide();
var data=_879.data;
var _87b=undefined;
for(var i=0;i<data.length;i++){
var row=data[i];
if(opts.filter.call(_878,q,row)){
var v=row[opts.valueField];
var s=row[opts.textField];
var g=row[opts.groupField];
var item=$("#"+row.domId).show();
if(s.toLowerCase()==q.toLowerCase()){
_862(_878,[v]);
item.addClass("combobox-item-selected");
}
if(opts.groupField&&_87b!=g){
var grow=_84e(_878,g,"value",true);
if(grow){
$("#"+grow.domId).show();
}
_87b=g;
}
}
}
}
};
function _87c(_87d){
var t=$(_87d);
var opts=t.combobox("options");
var _87e=t.combobox("panel");
var item=_87e.children("div.combobox-item-hover");
if(!item.length){
item=_87e.children("div.combobox-item-selected");
}
if(!item.length){
return;
}
var row=_84e(_87d,item.attr("id"),"domId");
if(!row){
return;
}
var _87f=row[opts.valueField];
if(opts.multiple){
if(item.hasClass("combobox-item-selected")){
t.combobox("unselect",_87f);
}else{
t.combobox("select",_87f);
}
}else{
t.combobox("select",_87f);
t.combobox("hidePanel");
}
var vv=[];
var _880=t.combobox("getValues");
for(var i=0;i<_880.length;i++){
if(_84e(_87d,_880[i])){
vv.push(_880[i]);
}
}
t.combobox("setValues",vv);
};
function _881(_882){
var opts=$.data(_882,"combobox").options;
$(_882).addClass("combobox-f");
$(_882).combo($.extend({},opts,{onShowPanel:function(){
$(_882).combo("panel").find("div.combobox-item,div.combobox-group").show();
_856(_882,$(_882).combobox("getValue"));
opts.onShowPanel.call(_882);
}}));
$(_882).combo("panel").unbind().bind("mouseover",function(e){
$(this).children("div.combobox-item-hover").removeClass("combobox-item-hover");
var item=$(e.target).closest("div.combobox-item");
if(!item.hasClass("combobox-item-disabled")){
item.addClass("combobox-item-hover");
}
e.stopPropagation();
}).bind("mouseout",function(e){
$(e.target).closest("div.combobox-item").removeClass("combobox-item-hover");
e.stopPropagation();
}).bind("click",function(e){
var item=$(e.target).closest("div.combobox-item");
if(!item.length||item.hasClass("combobox-item-disabled")){
return;
}
var row=_84e(_882,item.attr("id"),"domId");
if(!row){
return;
}
var _883=row[opts.valueField];
if(opts.multiple){
if(item.hasClass("combobox-item-selected")){
_863(_882,_883);
}else{
_85e(_882,_883);
}
}else{
_85e(_882,_883);
$(_882).combo("hidePanel");
}
e.stopPropagation();
});
};
$.fn.combobox=function(_884,_885){
if(typeof _884=="string"){
var _886=$.fn.combobox.methods[_884];
if(_886){
return _886(this,_885);
}else{
return this.combo(_884,_885);
}
}
_884=_884||{};
return this.each(function(){
var _887=$.data(this,"combobox");
if(_887){
$.extend(_887.options,_884);
_881(this);
}else{
_887=$.data(this,"combobox",{options:$.extend({},$.fn.combobox.defaults,$.fn.combobox.parseOptions(this),_884),data:[]});
_881(this);
var data=$.fn.combobox.parseData(this);
if(data.length){
_86d(this,data);
}
}
if(_887.options.data){
_86d(this,_887.options.data);
}
_873(this);
});
};
$.fn.combobox.methods={options:function(jq){
var _888=jq.combo("options");
return $.extend($.data(jq[0],"combobox").options,{originalValue:_888.originalValue,disabled:_888.disabled,readonly:_888.readonly});
},getData:function(jq){
return $.data(jq[0],"combobox").data;
},setValues:function(jq,_889){
return jq.each(function(){
_862(this,_889);
});
},setValue:function(jq,_88a){
return jq.each(function(){
_862(this,[_88a]);
});
},clear:function(jq){
return jq.each(function(){
$(this).combo("clear");
var _88b=$(this).combo("panel");
_88b.find("div.combobox-item-selected").removeClass("combobox-item-selected");
});
},reset:function(jq){
return jq.each(function(){
var opts=$(this).combobox("options");
if(opts.multiple){
$(this).combobox("setValues",opts.originalValue);
}else{
$(this).combobox("setValue",opts.originalValue);
}
});
},loadData:function(jq,data){
return jq.each(function(){
_86d(this,data);
});
},reload:function(jq,url){
return jq.each(function(){
_873(this,url);
});
},select:function(jq,_88c){
return jq.each(function(){
_85e(this,_88c);
});
},unselect:function(jq,_88d){
return jq.each(function(){
_863(this,_88d);
});
}};
$.fn.combobox.parseOptions=function(_88e){
var t=$(_88e);
return $.extend({},$.fn.combo.parseOptions(_88e),$.parser.parseOptions(_88e,["valueField","textField","groupField","mode","method","url"]));
};
$.fn.combobox.parseData=function(_88f){
var data=[];
var opts=$(_88f).combobox("options");
$(_88f).children().each(function(){
if(this.tagName.toLowerCase()=="optgroup"){
var _890=$(this).attr("label");
$(this).children().each(function(){
_891(this,_890);
});
}else{
_891(this);
}
});
return data;
function _891(el,_892){
var t=$(el);
var row={};
row[opts.valueField]=t.attr("value")!=undefined?t.attr("value"):t.html();
row[opts.textField]=t.html();
row["selected"]=t.is(":selected");
row["disabled"]=t.is(":disabled");
if(_892){
opts.groupField=opts.groupField||"group";
row[opts.groupField]=_892;
}
data.push(row);
};
};
$.fn.combobox.defaults=$.extend({},$.fn.combo.defaults,{valueField:"value",textField:"text",groupField:null,groupFormatter:function(_893){
return _893;
},mode:"local",method:"post",url:null,data:null,keyHandler:{up:function(e){
nav(this,"prev");
e.preventDefault();
},down:function(e){
nav(this,"next");
e.preventDefault();
},left:function(e){
},right:function(e){
},enter:function(e){
_87c(this);
},query:function(q,e){
_877(this,q);
}},filter:function(q,row){
var opts=$(this).combobox("options");
return row[opts.textField].toLowerCase().indexOf(q.toLowerCase())==0;
},formatter:function(row){
var opts=$(this).combobox("options");
return row[opts.textField];
},loader:function(_894,_895,_896){
var opts=$(this).combobox("options");
if(!opts.url){
return false;
}
$.ajax({type:opts.method,url:opts.url,data:_894,dataType:"json",success:function(data){
_895(data);
},error:function(){
_896.apply(this,arguments);
}});
},loadFilter:function(data){
return data;
},onBeforeLoad:function(_897){
},onLoadSuccess:function(){
},onLoadError:function(){
},onSelect:function(_898){
},onUnselect:function(_899){
}});
})(jQuery);
(function($){
function _89a(_89b){
var opts=$.data(_89b,"combotree").options;
var tree=$.data(_89b,"combotree").tree;
$(_89b).addClass("combotree-f");
$(_89b).combo(opts);
var _89c=$(_89b).combo("panel");
if(!tree){
tree=$("<ul></ul>").appendTo(_89c);
$.data(_89b,"combotree").tree=tree;
}
tree.tree($.extend({},opts,{checkbox:opts.multiple,onLoadSuccess:function(node,data){
var _89d=$(_89b).combotree("getValues");
if(opts.multiple){
var _89e=tree.tree("getChecked");
for(var i=0;i<_89e.length;i++){
var id=_89e[i].id;
(function(){
for(var i=0;i<_89d.length;i++){
if(id==_89d[i]){
return;
}
}
_89d.push(id);
})();
}
}
$(_89b).combotree("setValues",_89d);
opts.onLoadSuccess.call(this,node,data);
},onClick:function(node){
_8a0(_89b);
$(_89b).combo("hidePanel");
opts.onClick.call(this,node);
},onCheck:function(node,_89f){
_8a0(_89b);
opts.onCheck.call(this,node,_89f);
}}));
};
function _8a0(_8a1){
var opts=$.data(_8a1,"combotree").options;
var tree=$.data(_8a1,"combotree").tree;
var vv=[],ss=[];
if(opts.multiple){
var _8a2=tree.tree("getChecked");
for(var i=0;i<_8a2.length;i++){
vv.push(_8a2[i].id);
ss.push(_8a2[i].text);
}
}else{
var node=tree.tree("getSelected");
if(node){
vv.push(node.id);
ss.push(node.text);
}
}
$(_8a1).combo("setValues",vv).combo("setText",ss.join(opts.separator));
};
function _8a3(_8a4,_8a5){
var opts=$.data(_8a4,"combotree").options;
var tree=$.data(_8a4,"combotree").tree;
tree.find("span.tree-checkbox").addClass("tree-checkbox0").removeClass("tree-checkbox1 tree-checkbox2");
var vv=[],ss=[];
for(var i=0;i<_8a5.length;i++){
var v=_8a5[i];
var s=v;
var node=tree.tree("find",v);
if(node){
s=node.text;
tree.tree("check",node.target);
tree.tree("select",node.target);
}
vv.push(v);
ss.push(s);
}
$(_8a4).combo("setValues",vv).combo("setText",ss.join(opts.separator));
};
$.fn.combotree=function(_8a6,_8a7){
if(typeof _8a6=="string"){
var _8a8=$.fn.combotree.methods[_8a6];
if(_8a8){
return _8a8(this,_8a7);
}else{
return this.combo(_8a6,_8a7);
}
}
_8a6=_8a6||{};
return this.each(function(){
var _8a9=$.data(this,"combotree");
if(_8a9){
$.extend(_8a9.options,_8a6);
}else{
$.data(this,"combotree",{options:$.extend({},$.fn.combotree.defaults,$.fn.combotree.parseOptions(this),_8a6)});
}
_89a(this);
});
};
$.fn.combotree.methods={options:function(jq){
var _8aa=jq.combo("options");
return $.extend($.data(jq[0],"combotree").options,{originalValue:_8aa.originalValue,disabled:_8aa.disabled,readonly:_8aa.readonly});
},tree:function(jq){
return $.data(jq[0],"combotree").tree;
},loadData:function(jq,data){
return jq.each(function(){
var opts=$.data(this,"combotree").options;
opts.data=data;
var tree=$.data(this,"combotree").tree;
tree.tree("loadData",data);
});
},reload:function(jq,url){
return jq.each(function(){
var opts=$.data(this,"combotree").options;
var tree=$.data(this,"combotree").tree;
if(url){
opts.url=url;
}
tree.tree({url:opts.url});
});
},setValues:function(jq,_8ab){
return jq.each(function(){
_8a3(this,_8ab);
});
},setValue:function(jq,_8ac){
return jq.each(function(){
_8a3(this,[_8ac]);
});
},clear:function(jq){
return jq.each(function(){
var tree=$.data(this,"combotree").tree;
tree.find("div.tree-node-selected").removeClass("tree-node-selected");
var cc=tree.tree("getChecked");
for(var i=0;i<cc.length;i++){
tree.tree("uncheck",cc[i].target);
}
$(this).combo("clear");
});
},reset:function(jq){
return jq.each(function(){
var opts=$(this).combotree("options");
if(opts.multiple){
$(this).combotree("setValues",opts.originalValue);
}else{
$(this).combotree("setValue",opts.originalValue);
}
});
}};
$.fn.combotree.parseOptions=function(_8ad){
return $.extend({},$.fn.combo.parseOptions(_8ad),$.fn.tree.parseOptions(_8ad));
};
$.fn.combotree.defaults=$.extend({},$.fn.combo.defaults,$.fn.tree.defaults,{editable:false});
})(jQuery);
(function($){
function _8ae(_8af){
var _8b0=$.data(_8af,"combogrid");
var opts=_8b0.options;
var grid=_8b0.grid;
$(_8af).addClass("combogrid-f").combo(opts);
var _8b1=$(_8af).combo("panel");
if(!grid){
grid=$("<table></table>").appendTo(_8b1);
_8b0.grid=grid;
}
grid.datagrid($.extend({},opts,{border:false,fit:true,singleSelect:(!opts.multiple),onLoadSuccess:function(data){
var _8b2=$(_8af).combo("getValues");
var _8b3=opts.onSelect;
opts.onSelect=function(){
};
_8bd(_8af,_8b2,_8b0.remainText);
opts.onSelect=_8b3;
opts.onLoadSuccess.apply(_8af,arguments);
},onClickRow:_8b4,onSelect:function(_8b5,row){
_8b6();
opts.onSelect.call(this,_8b5,row);
},onUnselect:function(_8b7,row){
_8b6();
opts.onUnselect.call(this,_8b7,row);
},onSelectAll:function(rows){
_8b6();
opts.onSelectAll.call(this,rows);
},onUnselectAll:function(rows){
if(opts.multiple){
_8b6();
}
opts.onUnselectAll.call(this,rows);
}}));
function _8b4(_8b8,row){
_8b0.remainText=false;
_8b6();
if(!opts.multiple){
$(_8af).combo("hidePanel");
}
opts.onClickRow.call(this,_8b8,row);
};
function _8b6(){
var rows=grid.datagrid("getSelections");
var vv=[],ss=[];
for(var i=0;i<rows.length;i++){
vv.push(rows[i][opts.idField]);
ss.push(rows[i][opts.textField]);
}
if(!opts.multiple){
$(_8af).combo("setValues",(vv.length?vv:[""]));
}else{
$(_8af).combo("setValues",vv);
}
if(!_8b0.remainText){
$(_8af).combo("setText",ss.join(opts.separator));
}
};
};
function nav(_8b9,dir){
var _8ba=$.data(_8b9,"combogrid");
var opts=_8ba.options;
var grid=_8ba.grid;
var _8bb=grid.datagrid("getRows").length;
if(!_8bb){
return;
}
var tr=opts.finder.getTr(grid[0],null,"highlight");
if(!tr.length){
tr=opts.finder.getTr(grid[0],null,"selected");
}
var _8bc;
if(!tr.length){
_8bc=(dir=="next"?0:_8bb-1);
}else{
var _8bc=parseInt(tr.attr("datagrid-row-index"));
_8bc+=(dir=="next"?1:-1);
if(_8bc<0){
_8bc=_8bb-1;
}
if(_8bc>=_8bb){
_8bc=0;
}
}
grid.datagrid("highlightRow",_8bc);
if(opts.selectOnNavigation){
_8ba.remainText=false;
grid.datagrid("selectRow",_8bc);
}
};
function _8bd(_8be,_8bf,_8c0){
var _8c1=$.data(_8be,"combogrid");
var opts=_8c1.options;
var grid=_8c1.grid;
var rows=grid.datagrid("getRows");
var ss=[];
var _8c2=$(_8be).combo("getValues");
var _8c3=$(_8be).combo("options");
var _8c4=_8c3.onChange;
_8c3.onChange=function(){
};
grid.datagrid("clearSelections");
for(var i=0;i<_8bf.length;i++){
var _8c5=grid.datagrid("getRowIndex",_8bf[i]);
if(_8c5>=0){
grid.datagrid("selectRow",_8c5);
ss.push(rows[_8c5][opts.textField]);
}else{
ss.push(_8bf[i]);
}
}
$(_8be).combo("setValues",_8c2);
_8c3.onChange=_8c4;
$(_8be).combo("setValues",_8bf);
if(!_8c0){
var s=ss.join(opts.separator);
if($(_8be).combo("getText")!=s){
$(_8be).combo("setText",s);
}
}
};
function _8c6(_8c7,q){
var _8c8=$.data(_8c7,"combogrid");
var opts=_8c8.options;
var grid=_8c8.grid;
_8c8.remainText=true;
if(opts.multiple&&!q){
_8bd(_8c7,[],true);
}else{
_8bd(_8c7,[q],true);
}
if(opts.mode=="remote"){
grid.datagrid("clearSelections");
grid.datagrid("load",$.extend({},opts.queryParams,{q:q}));
}else{
if(!q){
return;
}
var rows=grid.datagrid("getRows");
for(var i=0;i<rows.length;i++){
if(opts.filter.call(_8c7,q,rows[i])){
grid.datagrid("clearSelections");
grid.datagrid("selectRow",i);
return;
}
}
}
};
function _8c9(_8ca){
var _8cb=$.data(_8ca,"combogrid");
var opts=_8cb.options;
var grid=_8cb.grid;
var tr=opts.finder.getTr(grid[0],null,"highlight");
if(!tr.length){
tr=opts.finder.getTr(grid[0],null,"selected");
}
if(!tr.length){
return;
}
_8cb.remainText=false;
var _8cc=parseInt(tr.attr("datagrid-row-index"));
if(opts.multiple){
if(tr.hasClass("datagrid-row-selected")){
grid.datagrid("unselectRow",_8cc);
}else{
grid.datagrid("selectRow",_8cc);
}
}else{
grid.datagrid("selectRow",_8cc);
$(_8ca).combogrid("hidePanel");
}
};
$.fn.combogrid=function(_8cd,_8ce){
if(typeof _8cd=="string"){
var _8cf=$.fn.combogrid.methods[_8cd];
if(_8cf){
return _8cf(this,_8ce);
}else{
return this.combo(_8cd,_8ce);
}
}
_8cd=_8cd||{};
return this.each(function(){
var _8d0=$.data(this,"combogrid");
if(_8d0){
$.extend(_8d0.options,_8cd);
}else{
_8d0=$.data(this,"combogrid",{options:$.extend({},$.fn.combogrid.defaults,$.fn.combogrid.parseOptions(this),_8cd)});
}
_8ae(this);
});
};
$.fn.combogrid.methods={options:function(jq){
var _8d1=jq.combo("options");
return $.extend($.data(jq[0],"combogrid").options,{originalValue:_8d1.originalValue,disabled:_8d1.disabled,readonly:_8d1.readonly});
},grid:function(jq){
return $.data(jq[0],"combogrid").grid;
},setValues:function(jq,_8d2){
return jq.each(function(){
_8bd(this,_8d2);
});
},setValue:function(jq,_8d3){
return jq.each(function(){
_8bd(this,[_8d3]);
});
},clear:function(jq){
return jq.each(function(){
$(this).combogrid("grid").datagrid("clearSelections");
$(this).combo("clear");
});
},reset:function(jq){
return jq.each(function(){
var opts=$(this).combogrid("options");
if(opts.multiple){
$(this).combogrid("setValues",opts.originalValue);
}else{
$(this).combogrid("setValue",opts.originalValue);
}
});
}};
$.fn.combogrid.parseOptions=function(_8d4){
var t=$(_8d4);
return $.extend({},$.fn.combo.parseOptions(_8d4),$.fn.datagrid.parseOptions(_8d4),$.parser.parseOptions(_8d4,["idField","textField","mode"]));
};
$.fn.combogrid.defaults=$.extend({},$.fn.combo.defaults,$.fn.datagrid.defaults,{loadMsg:null,idField:null,textField:null,mode:"local",keyHandler:{up:function(e){
nav(this,"prev");
e.preventDefault();
},down:function(e){
nav(this,"next");
e.preventDefault();
},left:function(e){
},right:function(e){
},enter:function(e){
_8c9(this);
},query:function(q,e){
_8c6(this,q);
}},filter:function(q,row){
var opts=$(this).combogrid("options");
return row[opts.textField].indexOf(q)==0;
}});
})(jQuery);
(function($){
function _8d5(_8d6){
var _8d7=$.data(_8d6,"datebox");
var opts=_8d7.options;
$(_8d6).addClass("datebox-f").combo($.extend({},opts,{onShowPanel:function(){
_8d8();
_8e0(_8d6,$(_8d6).datebox("getText"));
opts.onShowPanel.call(_8d6);
}}));
$(_8d6).combo("textbox").parent().addClass("datebox");
if(!_8d7.calendar){
_8d9();
}
function _8d9(){
var _8da=$(_8d6).combo("panel").css("overflow","hidden");
var cc=$("<div class=\"datebox-calendar-inner\"></div>").appendTo(_8da);
if(opts.sharedCalendar){
_8d7.calendar=$(opts.sharedCalendar).appendTo(cc);
if(!_8d7.calendar.hasClass("calendar")){
_8d7.calendar.calendar();
}
}else{
_8d7.calendar=$("<div></div>").appendTo(cc).calendar();
}
$.extend(_8d7.calendar.calendar("options"),{fit:true,border:false,onSelect:function(date){
var opts=$(this.target).datebox("options");
_8e0(this.target,opts.formatter(date));
$(this.target).combo("hidePanel");
opts.onSelect.call(_8d6,date);
}});
_8e0(_8d6,opts.value);
var _8db=$("<div class=\"datebox-button\"><table cellspacing=\"0\" cellpadding=\"0\" style=\"width:100%\"><tr></tr></table></div>").appendTo(_8da);
var tr=_8db.find("tr");
for(var i=0;i<opts.buttons.length;i++){
var td=$("<td></td>").appendTo(tr);
var btn=opts.buttons[i];
var t=$("<a href=\"javascript:void(0)\"></a>").html($.isFunction(btn.text)?btn.text(_8d6):btn.text).appendTo(td);
t.bind("click",{target:_8d6,handler:btn.handler},function(e){
e.data.handler.call(this,e.data.target);
});
}
tr.find("td").css("width",(100/opts.buttons.length)+"%");
};
function _8d8(){
var _8dc=$(_8d6).combo("panel");
var cc=_8dc.children("div.datebox-calendar-inner");
_8dc.children()._outerWidth(_8dc.width());
_8d7.calendar.appendTo(cc);
_8d7.calendar[0].target=_8d6;
if(opts.panelHeight!="auto"){
var _8dd=_8dc.height();
_8dc.children().not(cc).each(function(){
_8dd-=$(this).outerHeight();
});
cc._outerHeight(_8dd);
}
_8d7.calendar.calendar("resize");
};
};
function _8de(_8df,q){
_8e0(_8df,q);
};
function _8e1(_8e2){
var _8e3=$.data(_8e2,"datebox");
var opts=_8e3.options;
var _8e4=opts.formatter(_8e3.calendar.calendar("options").current);
_8e0(_8e2,_8e4);
$(_8e2).combo("hidePanel");
};
function _8e0(_8e5,_8e6){
var _8e7=$.data(_8e5,"datebox");
var opts=_8e7.options;
$(_8e5).combo("setValue",_8e6).combo("setText",_8e6);
_8e7.calendar.calendar("moveTo",opts.parser(_8e6));
};
$.fn.datebox=function(_8e8,_8e9){
if(typeof _8e8=="string"){
var _8ea=$.fn.datebox.methods[_8e8];
if(_8ea){
return _8ea(this,_8e9);
}else{
return this.combo(_8e8,_8e9);
}
}
_8e8=_8e8||{};
return this.each(function(){
var _8eb=$.data(this,"datebox");
if(_8eb){
$.extend(_8eb.options,_8e8);
}else{
$.data(this,"datebox",{options:$.extend({},$.fn.datebox.defaults,$.fn.datebox.parseOptions(this),_8e8)});
}
_8d5(this);
});
};
$.fn.datebox.methods={options:function(jq){
var _8ec=jq.combo("options");
return $.extend($.data(jq[0],"datebox").options,{originalValue:_8ec.originalValue,disabled:_8ec.disabled,readonly:_8ec.readonly});
},calendar:function(jq){
return $.data(jq[0],"datebox").calendar;
},setValue:function(jq,_8ed){
return jq.each(function(){
_8e0(this,_8ed);
});
},reset:function(jq){
return jq.each(function(){
var opts=$(this).datebox("options");
$(this).datebox("setValue",opts.originalValue);
});
}};
$.fn.datebox.parseOptions=function(_8ee){
return $.extend({},$.fn.combo.parseOptions(_8ee),$.parser.parseOptions(_8ee,["sharedCalendar"]));
};
$.fn.datebox.defaults=$.extend({},$.fn.combo.defaults,{panelWidth:180,panelHeight:"auto",sharedCalendar:null,keyHandler:{up:function(e){
},down:function(e){
},left:function(e){
},right:function(e){
},enter:function(e){
_8e1(this);
},query:function(q,e){
_8de(this,q);
}},currentText:"Today",closeText:"Close",okText:"Ok",buttons:[{text:function(_8ef){
return $(_8ef).datebox("options").currentText;
},handler:function(_8f0){
$(_8f0).datebox("calendar").calendar({year:new Date().getFullYear(),month:new Date().getMonth()+1,current:new Date()});
_8e1(_8f0);
}},{text:function(_8f1){
return $(_8f1).datebox("options").closeText;
},handler:function(_8f2){
$(this).closest("div.combo-panel").panel("close");
}}],formatter:function(date){
var y=date.getFullYear();
var m=date.getMonth()+1;
var d=date.getDate();
return m+"/"+d+"/"+y;
},parser:function(s){
var t=Date.parse(s);
if(!isNaN(t)){
return new Date(t);
}else{
return new Date();
}
},onSelect:function(date){
}});
})(jQuery);
(function($){
function _8f3(_8f4){
var _8f5=$.data(_8f4,"datetimebox");
var opts=_8f5.options;
$(_8f4).datebox($.extend({},opts,{onShowPanel:function(){
var _8f6=$(_8f4).datetimebox("getValue");
_8f8(_8f4,_8f6,true);
opts.onShowPanel.call(_8f4);
},formatter:$.fn.datebox.defaults.formatter,parser:$.fn.datebox.defaults.parser}));
$(_8f4).removeClass("datebox-f").addClass("datetimebox-f");
$(_8f4).datebox("calendar").calendar({onSelect:function(date){
opts.onSelect.call(_8f4,date);
}});
var _8f7=$(_8f4).datebox("panel");
if(!_8f5.spinner){
var p=$("<div style=\"padding:2px\"><input style=\"width:80px\"></div>").insertAfter(_8f7.children("div.datebox-calendar-inner"));
_8f5.spinner=p.children("input");
}
_8f5.spinner.timespinner({showSeconds:opts.showSeconds,separator:opts.timeSeparator}).unbind(".datetimebox").bind("mousedown.datetimebox",function(e){
e.stopPropagation();
});
_8f8(_8f4,opts.value);
};
function _8f9(_8fa){
var c=$(_8fa).datetimebox("calendar");
var t=$(_8fa).datetimebox("spinner");
var date=c.calendar("options").current;
return new Date(date.getFullYear(),date.getMonth(),date.getDate(),t.timespinner("getHours"),t.timespinner("getMinutes"),t.timespinner("getSeconds"));
};
function _8fb(_8fc,q){
_8f8(_8fc,q,true);
};
function _8fd(_8fe){
var opts=$.data(_8fe,"datetimebox").options;
var date=_8f9(_8fe);
_8f8(_8fe,opts.formatter.call(_8fe,date));
$(_8fe).combo("hidePanel");
};
function _8f8(_8ff,_900,_901){
var opts=$.data(_8ff,"datetimebox").options;
$(_8ff).combo("setValue",_900);
if(!_901){
if(_900){
var date=opts.parser.call(_8ff,_900);
$(_8ff).combo("setValue",opts.formatter.call(_8ff,date));
$(_8ff).combo("setText",opts.formatter.call(_8ff,date));
}else{
$(_8ff).combo("setText",_900);
}
}
var date=opts.parser.call(_8ff,_900);
$(_8ff).datetimebox("calendar").calendar("moveTo",date);
$(_8ff).datetimebox("spinner").timespinner("setValue",_902(date));
function _902(date){
function _903(_904){
return (_904<10?"0":"")+_904;
};
var tt=[_903(date.getHours()),_903(date.getMinutes())];
if(opts.showSeconds){
tt.push(_903(date.getSeconds()));
}
return tt.join($(_8ff).datetimebox("spinner").timespinner("options").separator);
};
};
$.fn.datetimebox=function(_905,_906){
if(typeof _905=="string"){
var _907=$.fn.datetimebox.methods[_905];
if(_907){
return _907(this,_906);
}else{
return this.datebox(_905,_906);
}
}
_905=_905||{};
return this.each(function(){
var _908=$.data(this,"datetimebox");
if(_908){
$.extend(_908.options,_905);
}else{
$.data(this,"datetimebox",{options:$.extend({},$.fn.datetimebox.defaults,$.fn.datetimebox.parseOptions(this),_905)});
}
_8f3(this);
});
};
$.fn.datetimebox.methods={options:function(jq){
var _909=jq.datebox("options");
return $.extend($.data(jq[0],"datetimebox").options,{originalValue:_909.originalValue,disabled:_909.disabled,readonly:_909.readonly});
},spinner:function(jq){
return $.data(jq[0],"datetimebox").spinner;
},setValue:function(jq,_90a){
return jq.each(function(){
_8f8(this,_90a);
});
},reset:function(jq){
return jq.each(function(){
var opts=$(this).datetimebox("options");
$(this).datetimebox("setValue",opts.originalValue);
});
}};
$.fn.datetimebox.parseOptions=function(_90b){
var t=$(_90b);
return $.extend({},$.fn.datebox.parseOptions(_90b),$.parser.parseOptions(_90b,["timeSeparator",{showSeconds:"boolean"}]));
};
$.fn.datetimebox.defaults=$.extend({},$.fn.datebox.defaults,{showSeconds:true,timeSeparator:":",keyHandler:{up:function(e){
},down:function(e){
},left:function(e){
},right:function(e){
},enter:function(e){
_8fd(this);
},query:function(q,e){
_8fb(this,q);
}},buttons:[{text:function(_90c){
return $(_90c).datetimebox("options").currentText;
},handler:function(_90d){
$(_90d).datetimebox("calendar").calendar({year:new Date().getFullYear(),month:new Date().getMonth()+1,current:new Date()});
_8fd(_90d);
}},{text:function(_90e){
return $(_90e).datetimebox("options").okText;
},handler:function(_90f){
_8fd(_90f);
}},{text:function(_910){
return $(_910).datetimebox("options").closeText;
},handler:function(_911){
$(this).closest("div.combo-panel").panel("close");
}}],formatter:function(date){
var h=date.getHours();
var M=date.getMinutes();
var s=date.getSeconds();
function _912(_913){
return (_913<10?"0":"")+_913;
};
var _914=$(this).datetimebox("spinner").timespinner("options").separator;
var r=$.fn.datebox.defaults.formatter(date)+" "+_912(h)+_914+_912(M);
if($(this).datetimebox("options").showSeconds){
r+=_914+_912(s);
}
return r;
},parser:function(s){
if($.trim(s)==""){
return new Date();
}
var dt=s.split(" ");
var d=$.fn.datebox.defaults.parser(dt[0]);
if(dt.length<2){
return d;
}
var _915=$(this).datetimebox("spinner").timespinner("options").separator;
var tt=dt[1].split(_915);
var hour=parseInt(tt[0],10)||0;
var _916=parseInt(tt[1],10)||0;
var _917=parseInt(tt[2],10)||0;
return new Date(d.getFullYear(),d.getMonth(),d.getDate(),hour,_916,_917);
}});
})(jQuery);
(function($){
function init(_918){
var _919=$("<div class=\"slider\">"+"<div class=\"slider-inner\">"+"<a href=\"javascript:void(0)\" class=\"slider-handle\"></a>"+"<span class=\"slider-tip\"></span>"+"</div>"+"<div class=\"slider-rule\"></div>"+"<div class=\"slider-rulelabel\"></div>"+"<div style=\"clear:both\"></div>"+"<input type=\"hidden\" class=\"slider-value\">"+"</div>").insertAfter(_918);
var t=$(_918);
t.addClass("slider-f").hide();
var name=t.attr("name");
if(name){
_919.find("input.slider-value").attr("name",name);
t.removeAttr("name").attr("sliderName",name);
}
return _919;
};
function _91a(_91b,_91c){
var _91d=$.data(_91b,"slider");
var opts=_91d.options;
var _91e=_91d.slider;
if(_91c){
if(_91c.width){
opts.width=_91c.width;
}
if(_91c.height){
opts.height=_91c.height;
}
}
if(opts.mode=="h"){
_91e.css("height","");
_91e.children("div").css("height","");
if(!isNaN(opts.width)){
_91e.width(opts.width);
}
}else{
_91e.css("width","");
_91e.children("div").css("width","");
if(!isNaN(opts.height)){
_91e.height(opts.height);
_91e.find("div.slider-rule").height(opts.height);
_91e.find("div.slider-rulelabel").height(opts.height);
_91e.find("div.slider-inner")._outerHeight(opts.height);
}
}
_91f(_91b);
};
function _920(_921){
var _922=$.data(_921,"slider");
var opts=_922.options;
var _923=_922.slider;
var aa=opts.mode=="h"?opts.rule:opts.rule.slice(0).reverse();
if(opts.reversed){
aa=aa.slice(0).reverse();
}
_924(aa);
function _924(aa){
var rule=_923.find("div.slider-rule");
var _925=_923.find("div.slider-rulelabel");
rule.empty();
_925.empty();
for(var i=0;i<aa.length;i++){
var _926=i*100/(aa.length-1)+"%";
var span=$("<span></span>").appendTo(rule);
span.css((opts.mode=="h"?"left":"top"),_926);
if(aa[i]!="|"){
span=$("<span></span>").appendTo(_925);
span.html(aa[i]);
if(opts.mode=="h"){
span.css({left:_926,marginLeft:-Math.round(span.outerWidth()/2)});
}else{
span.css({top:_926,marginTop:-Math.round(span.outerHeight()/2)});
}
}
}
};
};
function _927(_928){
var _929=$.data(_928,"slider");
var opts=_929.options;
var _92a=_929.slider;
_92a.removeClass("slider-h slider-v slider-disabled");
_92a.addClass(opts.mode=="h"?"slider-h":"slider-v");
_92a.addClass(opts.disabled?"slider-disabled":"");
_92a.find("a.slider-handle").draggable({axis:opts.mode,cursor:"pointer",disabled:opts.disabled,onDrag:function(e){
var left=e.data.left;
var _92b=_92a.width();
if(opts.mode!="h"){
left=e.data.top;
_92b=_92a.height();
}
if(left<0||left>_92b){
return false;
}else{
var _92c=_93e(_928,left);
_92d(_92c);
return false;
}
},onBeforeDrag:function(){
_929.isDragging=true;
},onStartDrag:function(){
opts.onSlideStart.call(_928,opts.value);
},onStopDrag:function(e){
var _92e=_93e(_928,(opts.mode=="h"?e.data.left:e.data.top));
_92d(_92e);
opts.onSlideEnd.call(_928,opts.value);
opts.onComplete.call(_928,opts.value);
_929.isDragging=false;
}});
_92a.find("div.slider-inner").unbind(".slider").bind("mousedown.slider",function(e){
if(_929.isDragging){
return;
}
var pos=$(this).offset();
var _92f=_93e(_928,(opts.mode=="h"?(e.pageX-pos.left):(e.pageY-pos.top)));
_92d(_92f);
opts.onComplete.call(_928,opts.value);
});
function _92d(_930){
var s=Math.abs(_930%opts.step);
if(s<opts.step/2){
_930-=s;
}else{
_930=_930-s+opts.step;
}
_931(_928,_930);
};
};
function _931(_932,_933){
var _934=$.data(_932,"slider");
var opts=_934.options;
var _935=_934.slider;
var _936=opts.value;
if(_933<opts.min){
_933=opts.min;
}
if(_933>opts.max){
_933=opts.max;
}
opts.value=_933;
$(_932).val(_933);
_935.find("input.slider-value").val(_933);
var pos=_937(_932,_933);
var tip=_935.find(".slider-tip");
if(opts.showTip){
tip.show();
tip.html(opts.tipFormatter.call(_932,opts.value));
}else{
tip.hide();
}
if(opts.mode=="h"){
var _938="left:"+pos+"px;";
_935.find(".slider-handle").attr("style",_938);
tip.attr("style",_938+"margin-left:"+(-Math.round(tip.outerWidth()/2))+"px");
}else{
var _938="top:"+pos+"px;";
_935.find(".slider-handle").attr("style",_938);
tip.attr("style",_938+"margin-left:"+(-Math.round(tip.outerWidth()))+"px");
}
if(_936!=_933){
opts.onChange.call(_932,_933,_936);
}
};
function _91f(_939){
var opts=$.data(_939,"slider").options;
var fn=opts.onChange;
opts.onChange=function(){
};
_931(_939,opts.value);
opts.onChange=fn;
};
function _937(_93a,_93b){
var _93c=$.data(_93a,"slider");
var opts=_93c.options;
var _93d=_93c.slider;
if(opts.mode=="h"){
var pos=(_93b-opts.min)/(opts.max-opts.min)*_93d.width();
if(opts.reversed){
pos=_93d.width()-pos;
}
}else{
var pos=_93d.height()-(_93b-opts.min)/(opts.max-opts.min)*_93d.height();
if(opts.reversed){
pos=_93d.height()-pos;
}
}
return pos.toFixed(0);
};
function _93e(_93f,pos){
var _940=$.data(_93f,"slider");
var opts=_940.options;
var _941=_940.slider;
if(opts.mode=="h"){
var _942=opts.min+(opts.max-opts.min)*(pos/_941.width());
}else{
var _942=opts.min+(opts.max-opts.min)*((_941.height()-pos)/_941.height());
}
return opts.reversed?opts.max-_942.toFixed(0):_942.toFixed(0);
};
$.fn.slider=function(_943,_944){
if(typeof _943=="string"){
return $.fn.slider.methods[_943](this,_944);
}
_943=_943||{};
return this.each(function(){
var _945=$.data(this,"slider");
if(_945){
$.extend(_945.options,_943);
}else{
_945=$.data(this,"slider",{options:$.extend({},$.fn.slider.defaults,$.fn.slider.parseOptions(this),_943),slider:init(this)});
$(this).removeAttr("disabled");
}
var opts=_945.options;
opts.min=parseFloat(opts.min);
opts.max=parseFloat(opts.max);
opts.value=parseFloat(opts.value);
opts.step=parseFloat(opts.step);
opts.originalValue=opts.value;
_927(this);
_920(this);
_91a(this);
});
};
$.fn.slider.methods={options:function(jq){
return $.data(jq[0],"slider").options;
},destroy:function(jq){
return jq.each(function(){
$.data(this,"slider").slider.remove();
$(this).remove();
});
},resize:function(jq,_946){
return jq.each(function(){
_91a(this,_946);
});
},getValue:function(jq){
return jq.slider("options").value;
},setValue:function(jq,_947){
return jq.each(function(){
_931(this,_947);
});
},clear:function(jq){
return jq.each(function(){
var opts=$(this).slider("options");
_931(this,opts.min);
});
},reset:function(jq){
return jq.each(function(){
var opts=$(this).slider("options");
_931(this,opts.originalValue);
});
},enable:function(jq){
return jq.each(function(){
$.data(this,"slider").options.disabled=false;
_927(this);
});
},disable:function(jq){
return jq.each(function(){
$.data(this,"slider").options.disabled=true;
_927(this);
});
}};
$.fn.slider.parseOptions=function(_948){
var t=$(_948);
return $.extend({},$.parser.parseOptions(_948,["width","height","mode",{reversed:"boolean",showTip:"boolean",min:"number",max:"number",step:"number"}]),{value:(t.val()||undefined),disabled:(t.attr("disabled")?true:undefined),rule:(t.attr("rule")?eval(t.attr("rule")):undefined)});
};
$.fn.slider.defaults={width:"auto",height:"auto",mode:"h",reversed:false,showTip:false,disabled:false,value:0,min:0,max:100,step:1,rule:[],tipFormatter:function(_949){
return _949;
},onChange:function(_94a,_94b){
},onSlideStart:function(_94c){
},onSlideEnd:function(_94d){
},onComplete:function(_94e){
}};
})(jQuery);

/**
 * portal - jQuery EasyUI
 * 
 * Licensed under the GPL: http://www.gnu.org/licenses/gpl.txt
 * 
 * Copyright 2010-2012 stworthy [ stworthy@gmail.com ]
 * 
 * Dependencies: draggable panel
 * 
 */
(function($) {
	/**
	 * initialize the portal
	 */
	function init(target) {
		$(target).addClass('portal');
		var table = $(
				'<table border="0" cellspacing="0" cellpadding="0"><tr></tr></table>')
				.appendTo(target);
		var tr = table.find('tr');

		var columnWidths = [];
		var totalWidth = 0;
		$(target).children('div:first').addClass('portal-column-left');
		$(target).children('div:last').addClass('portal-column-right');
		$(target).find('>div').each(function() { // each column panel
			var column = $(this);
			totalWidth += column.outerWidth();
			columnWidths.push(column.outerWidth());

			var td = $('<td class="portal-column-td"></td>').appendTo(tr);
			column.addClass('portal-column').appendTo(td);
			column.find('>div').each(function() { // each portal panel
				var p = $(this).addClass('portal-p').panel({
					doSize : false,
					cls : 'portal-panel'
				});
				makeDraggable(target, p);
			});
		});
		for (var i = 0; i < columnWidths.length; i++) {
			columnWidths[i] /= totalWidth;
		}

		$(target).bind('_resize', function() {
			var opts = $.data(target, 'portal').options;
			if (opts.fit == true) {
				setSize(target);
			}
			return false;
		});

		return columnWidths;
	}

	function setSize(target) {
		var t = $(target);
		var opts = $.data(target, 'portal').options;
		if (opts.fit) {
			var p = t.parent();
			opts.width = p.width();
			opts.height = p.height();
		}
		if (!isNaN(opts.width)) {
			t._outerWidth(opts.width);
		} else {
			t.width('auto');
		}
		if (!isNaN(opts.height)) {
			t._outerHeight(opts.height);
		} else {
			t.height('auto');
		}

		var hasScroll = t.find('>table').outerHeight() > t.height();
		var width = t.width();
		var columnWidths = $.data(target, 'portal').columnWidths;
		var leftWidth = 0;

		// calculate and set every column size
		for (var i = 0; i < columnWidths.length; i++) {
			var p = t.find('div.portal-column:eq(' + i + ')');
			var w = Math.floor(width * columnWidths[i]);
			if (i == columnWidths.length - 1) {
				// w = width - leftWidth - (hasScroll == true ? 28 : 10);
				w = width - leftWidth - (hasScroll == true ? 18 : 0);
			}
			p._outerWidth(w);
			leftWidth += p.outerWidth();

			// resize every panel of the column
			p.find('div.portal-p').panel('resize', {
				width : p.width()
			});
		}
		opts.onResize.call(target, opts.width, opts.height);
	}

	/**
	 * set draggable feature for the specified panel
	 */
	function makeDraggable(target, panel) {
		var spacer;
		panel
				.panel('panel')
				.draggable(
						{
							handle : '>div.panel-header>div.panel-title',
							proxy : function(source) {
								var p = $(
										'<div class="portal-proxy">proxy</div>')
										.insertAfter(source);
								p.width($(source).width());
								p.height($(source).height());
								p.html($(source).html());
								p.find('div.portal-p').removeClass('portal-p');
								return p;
							},
							onBeforeDrag : function(e) {
								var pbody = $(this).find(".panel-body");
								var _wrap = $("<div id='movewrap_' class='portalmovewrap'></div>");
								pbody.append(_wrap);
								e.data.startTop = $(this).position().top
										+ $(target).scrollTop();
							},
							onStartDrag : function(e) {
								$(this).hide();
								spacer = $('<div class="portal-spacer"></div>')
										.insertAfter(this);
								setSpacerSize($(this).outerWidth(), $(this)
										.outerHeight());
							},
							onDrag : function(e) {
								var p = findPanel(e, this);
								if (p) {
									if (p.pos == 'up') {
										spacer.insertBefore(p.target);
									} else {
										spacer.insertAfter(p.target);
									}
									setSpacerSize($(p.target).outerWidth());
								} else {
									var c = findColumn(e);
									if (c) {
										if (c.find('div.portal-spacer').length == 0) {
											spacer.appendTo(c);
											setSize(target);
											setSpacerSize(c.width());
										}
									}
								}
							},
							onStopDrag : function(e) {
								$(this).css('position', 'static');
								$(this).show();
								spacer.hide();
								$(this).insertAfter(spacer);
								spacer.remove();
								setSize(target);
								panel.panel('move');
								$("#movewrap_").remove();
								var nidx = this.parentNode.parentNode.cellIndex;
								panel.panel("options").columnIndex = nidx;
								var opts = $.data(target, 'portal').options;
								opts.onStateChange.call(target);
							}
						});

		/**
		 * find which panel the cursor is over
		 */
		function findPanel(e, source) {
			var result = null;
			$(target).find('div.portal-p').each(
					function() {
						var pal = $(this).panel('panel');
						if (pal[0] != source) {
							var pos = pal.offset();
							if (e.pageX > pos.left
									&& e.pageX < pos.left + pal.outerWidth()
									&& e.pageY > pos.top
									&& e.pageY < pos.top + pal.outerHeight()) {
								if (e.pageY > pos.top + pal.outerHeight() / 2) {
									result = {
										target : pal,
										pos : 'down'
									};
								} else {
									result = {
										target : pal,
										pos : 'up'
									};
								}
							}
						}
					});
			return result;
		}

		/**
		 * find which portal column the cursor is over
		 */
		function findColumn(e) {
			var result = null;
			$(target).find('div.portal-column').each(
					function() {
						var pal = $(this);
						var pos = pal.offset();
						if (e.pageX > pos.left
								&& e.pageX < pos.left + pal.outerWidth()) {
							result = pal;
						}
					});
			return result;
		}

		/**
		 * set the spacer size
		 */
		function setSpacerSize(width, height) {
			spacer._outerWidth(width);
			if (height) {
				spacer._outerHeight(height);
			}
		}
	}

	$.fn.portal = function(options, param) {
		if (typeof options == 'string') {
			return $.fn.portal.methods[options](this, param);
		}

		options = options || {};
		return this.each(function() {
			var state = $.data(this, 'portal');
			if (state) {
				$.extend(state.options, options);
			} else {
				state = $.data(this, 'portal', {
					options : $.extend({}, $.fn.portal.defaults, $.fn.portal
							.parseOptions(this), options),
					columnWidths : init(this)
				});
			}
			if (state.options.border) {
				$(this).removeClass('portal-noborder');
			} else {
				$(this).addClass('portal-noborder');
			}
			setSize(this);
		});
	};

	$.fn.portal.methods = {
		options : function(jq) {
			return $.data(jq[0], 'portal').options;
		},
		resize : function(jq, param) {
			return jq.each(function() {
				if (param) {
					var opts = $.data(this, 'portal').options;
					if (param.width)
						opts.width = param.width;
					if (param.height)
						opts.height = param.height;
				}
				setSize(this);
			});
		},
		getPanels : function(jq, columnIndex) {
			var c = jq; // the panel container
			if (columnIndex >= 0) {
				c = jq.find('div.portal-column:eq(' + columnIndex + ')');
			}
			var panels = [];
			c.find('div.portal-p').each(function() {
				panels.push($(this));
			});
			return panels;
		},
		add : function(jq, param) { // param: {panel,columnIndex}
			return jq.each(function() {
				var c = $(this).find(
						'div.portal-column:eq(' + param.columnIndex + ')');
				var p = param.panel.addClass('portal-p');
				p.panel('panel').addClass('portal-panel').appendTo(c);
				makeDraggable(this, p);
				p.panel('resize', {
					width : c.width()
				});
			});
		},
		remove : function(jq, panel) {
			return jq.each(function() {
				var panels = $(this).portal('getPanels');
				for (var i = 0; i < panels.length; i++) {
					var p = panels[i];
					if (p[0] == $(panel)[0]) {
						p.panel('destroy');
					}
				}
			});
		},
		disableDragging : function(jq, panel) {
			panel.panel('panel').draggable('disable');
			return jq;
		},
		enableDragging : function(jq, panel) {
			panel.panel('panel').draggable('enable');
			return jq;
		}
	};

	$.fn.portal.parseOptions = function(target) {
		var t = $(target);
		return {
			width : (parseInt(target.style.width) || undefined),
			height : (parseInt(target.style.height) || undefined),
			border : (t.attr('border') ? t.attr('border') == 'true' : undefined),
			fit : (t.attr('fit') ? t.attr('fit') == 'true' : undefined)
		};
	};

	$.fn.portal.defaults = {
		width : 'auto',
		height : 'auto',
		border : true,
		fit : false,
		onResize : function(width, height) {
		},
		onStateChange : function() {
		}
	};
})(jQuery);

var J_u_decode = function(str) {
	return decodeURIComponent(decodeURIComponent(str));
};

//获取URL参数
function getParam(name) {
	var lurl = window.location.href;
	if (lurl.indexOf(name) < 0)
		return;
	if (lurl.indexOf("?") < 0) {
		return "";
	} else {
		var par;
		if (lurl.indexOf("?" + name) > 0) {
			par = lurl.substring(lurl.indexOf("?" + name) + 1, lurl.length);
		}
		if (lurl.indexOf("&" + name) > 0) {
			par = lurl.substring(lurl.indexOf("&" + name) + 1, lurl.length);
		}
		if (!par)
			return;
		var start = name.length + 1;
		var len = (par.indexOf("&") > 0) ? par.indexOf("&") : lurl.length;
		var pav = par.substring(start, len);
		try {
			pav = J_u_decode(pav);
		} catch (e) {
		}
		return pav;
	}
}