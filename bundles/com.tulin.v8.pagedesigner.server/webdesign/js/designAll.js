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