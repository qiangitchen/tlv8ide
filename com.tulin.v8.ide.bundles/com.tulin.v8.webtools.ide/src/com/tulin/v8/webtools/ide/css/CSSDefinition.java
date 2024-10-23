package com.tulin.v8.webtools.ide.css;


public class CSSDefinition {
	
	/**
	 * @see http://it028.com/css-font.html
	 * @see @see https://www.w3cschool.cn/css/
	 */
	public static final CSSInfo[] CSS_KEYWORDS = {
			/* 文本修饰 */
			new CSSInfo("color"),
			new CSSInfo("direction"),
			new CSSInfo("letter-spacing"),
			new CSSInfo("line-height"),
			new CSSInfo("text-align"),
			new CSSInfo("text-decoration"),
			new CSSInfo("text-indent"),
			new CSSInfo("text-shadow"),
			new CSSInfo("text-transform"),
			new CSSInfo("unicode-bidi"),
			new CSSInfo("vertical-align"),
			new CSSInfo("white-space"),
			new CSSInfo("word-spacing"),
			/* 字体系列 */
			new CSSInfo("font"),
			new CSSInfo("font-family"),
			new CSSInfo("font-size"),
			new CSSInfo("font-style"),
			new CSSInfo("font-variant"),
			new CSSInfo("font-weight"),
			new CSSInfo("font-stretch"),
			new CSSInfo("font-size-adjust"),
			/* 列表 */
			new CSSInfo("list-style"),
			new CSSInfo("list-style-image"),
			new CSSInfo("list-style-position"),
			new CSSInfo("list-style-type"),
			/* 背景 */
			new CSSInfo("background"),
			new CSSInfo("background-attachment"),
			new CSSInfo("background-color"),
			new CSSInfo("background-image"),
			new CSSInfo("background-position"),
			new CSSInfo("background-repeat"),
			/* 边框 */
			new CSSInfo("border"),
			new CSSInfo("border-style"),
			new CSSInfo("border-width"),
			new CSSInfo("border-color"),
			new CSSInfo("border-bottom"),
			new CSSInfo("border-bottom-color"),
			new CSSInfo("border-bottom-style"),
			new CSSInfo("border-bottom-width"),
			new CSSInfo("border-left"),
			new CSSInfo("border-left-color"),
			new CSSInfo("border-left-style"),
			new CSSInfo("border-left-width"),
			new CSSInfo("border-right"),
			new CSSInfo("border-right-color"),
			new CSSInfo("border-right-style"),
			new CSSInfo("border-right-width"),
			new CSSInfo("border-top"),
			new CSSInfo("border-top-color"),
			new CSSInfo("border-top-style"),
			new CSSInfo("border-top-width"),
			new CSSInfo("border-radius"),
			new CSSInfo("border-top-left-radius"),
			new CSSInfo("border-top-right-radius"),
			new CSSInfo("border-bottom-right-radius"),
			new CSSInfo("border-bottom-left-radius"),
			new CSSInfo("border-collapse"),
			new CSSInfo("border-spacing"),
			/* 轮廓 */
			new CSSInfo("outline"),
			new CSSInfo("outline-color"),
			new CSSInfo("outline-style"),
			new CSSInfo("outline-width"),
			/* 外边距 */
			new CSSInfo("margin"),
			new CSSInfo("margin-bottom"),
			new CSSInfo("margin-left"),
			new CSSInfo("margin-right"),
			new CSSInfo("margin-top"),
			/* 填充 */
			new CSSInfo("padding"),
			new CSSInfo("padding-bottom"),
			new CSSInfo("padding-left"),
			new CSSInfo("padding-right"),
			new CSSInfo("padding-top"),
			/* 尺寸 */
			new CSSInfo("height"),
			new CSSInfo("max-height"),
			new CSSInfo("max-width"),
			new CSSInfo("min-height"),
			new CSSInfo("min-width"),
			new CSSInfo("width"),
			/* 显示 */
			new CSSInfo("display"),
			new CSSInfo("visibility"),
			/* 定位 */
			new CSSInfo("bottom"),
			new CSSInfo("clip"),
			new CSSInfo("cursor"),
			new CSSInfo("left"),
			new CSSInfo("overflow"),
			new CSSInfo("overflow-y"),
			new CSSInfo("overflow-x"),
			new CSSInfo("position"),
			new CSSInfo("right"),
			new CSSInfo("top"),
			new CSSInfo("z-index"),
			/* 浮动 */
			new CSSInfo("clear"),
			new CSSInfo("float"),
			/* 透明 */
			new CSSInfo("opacity"),
			new CSSInfo("filter"),
			new CSSInfo("caption-side"),
			new CSSInfo("table-layout"),
			new CSSInfo("empty-cells"),
			/* CSS 计数器属性 */
			new CSSInfo("content"),
			new CSSInfo("counter-increment"),
			new CSSInfo("counter-reset"),
			new CSSInfo("quotes"),
			new CSSInfo("marker-offset"),
			new CSSInfo("background-clip"),
			new CSSInfo("background-origin"),
			new CSSInfo("background-size"),
			new CSSInfo("box-shadow"),
			new CSSInfo("transform"),
			new CSSInfo("animation"),
			new CSSInfo("animation-name"),
			new CSSInfo("animation-duration"),
			new CSSInfo("animation-timing-function"),
			new CSSInfo("animation-delay"),
			new CSSInfo("animation-iteration-count"),
			new CSSInfo("animation-direction"),
			new CSSInfo("animation-fill-mode"),
			new CSSInfo("animation-play-state"),
			new CSSInfo("attr()"),
			new CSSInfo("calc()"),
			new CSSInfo("cubic-bezier()"),
			new CSSInfo("conic-gradient()"),
			new CSSInfo("counter()"),
			new CSSInfo("hsl()"),
			new CSSInfo("hsla()"),
			new CSSInfo("linear-gradient()"),
			new CSSInfo("max()"),
			new CSSInfo("min()"),
			new CSSInfo("radial-gradient()"),
			new CSSInfo("repeating-linear-gradient()"),
			new CSSInfo("repeating-radial-gradient()"),
			new CSSInfo("repeating-conic-gradient()"),
			new CSSInfo("rgb()"),
			new CSSInfo("rgba()"),
			new CSSInfo("var()"),
			new CSSInfo("repeat()"),
			new CSSInfo("minmax()"),
			/* CSS3 新文本属性 */
			new CSSInfo("hanging-punctuation"),
			new CSSInfo("punctuation-trim"),
			new CSSInfo("text-align-last"),
			new CSSInfo("text-emphasis"),
			new CSSInfo("text-justify"),
			new CSSInfo("text-outline"),
			new CSSInfo("text-overflow"),
			new CSSInfo("text-shadow"),
			new CSSInfo("text-wrap"),
			new CSSInfo("word-break"),
			new CSSInfo("word-wrap")
	};
	
	/**
	 * @see http://it028.com/css-font.html
	 * @see https://www.w3cschool.cn/css/
	 */
	public static final CSSValue[] CSS_VALUES = {
			/* 文本修饰 */
			new CSSValue("color:", new CSSInfo[] {new CSSInfo("red",CSSStyles.getString("color.red")),new CSSInfo("blue",CSSStyles.getString("color.blue")),new CSSInfo("green",CSSStyles.getString("color.green")),new CSSInfo("#",CSSStyles.getString("color.16")),new CSSInfo("rgb(255,0,0)",CSSStyles.getString("color.rgb")),new CSSInfo("inherit",CSSStyles.getString("color.inherit"))}),
			new CSSValue("direction:", new CSSInfo[] {new CSSInfo("ltr",""),new CSSInfo("rtl",""),new CSSInfo("inherit","")}),
			new CSSValue("text-indent:", new CSSInfo[] {new CSSInfo("length",CSSStyles.getString("text-indent.length")),new CSSInfo("%",CSSStyles.getString("text-indent.percentage")),new CSSInfo("inherit",CSSStyles.getString("text-indent.inherit"))}),
			new CSSValue("text-align:", new CSSInfo[] {new CSSInfo("left",CSSStyles.getString("text-align.left")),new CSSInfo("right",CSSStyles.getString("text-align.right")),new CSSInfo("center",CSSStyles.getString("text-align.center")),new CSSInfo("justify",CSSStyles.getString("text-align.justify")),new CSSInfo("inherit",CSSStyles.getString("text-align.inherit"))}),
			new CSSValue("text-decoration:", new CSSInfo[] {new CSSInfo("none",CSSStyles.getString("text-decoration.none")),new CSSInfo("underline",CSSStyles.getString("text-decoration.underline")),new CSSInfo("overline",CSSStyles.getString("text-decoration.overline")),new CSSInfo("line-through",CSSStyles.getString("text-decoration.line-through")),new CSSInfo("blink",CSSStyles.getString("text-decoration.blink")),new CSSInfo("inherit",CSSStyles.getString("text-decoration.inherit"))}),
			new CSSValue("text-shadow:", new CSSInfo[] {new CSSInfo("h-shadow",CSSStyles.getString("text-shadow.h-shadow")),new CSSInfo("v-shadow",CSSStyles.getString("text-shadow.v-shadow")),new CSSInfo("blur",CSSStyles.getString("text-shadow.blur")),new CSSInfo("color",CSSStyles.getString("text-shadow.color"))}),
			new CSSValue("letter-spacing:", new CSSInfo[] {new CSSInfo("normal",CSSStyles.getString("letter-spacing.normal")),new CSSInfo("length",CSSStyles.getString("letter-spacing.length")),new CSSInfo("inherit",CSSStyles.getString("letter-spacing.inherit"))}),
			new CSSValue("word-spacing:", new CSSInfo[] {new CSSInfo("normal",CSSStyles.getString("word-spacing.normal")),new CSSInfo("length",CSSStyles.getString("word-spacing.length")),new CSSInfo("inherit",CSSStyles.getString("word-spacing.inherit"))}),
			new CSSValue("text-transform:", new CSSInfo[] {new CSSInfo("none",CSSStyles.getString("text-transform.none")),new CSSInfo("capitalize",CSSStyles.getString("text-transform.capitalize")),new CSSInfo("uppercase",CSSStyles.getString("text-transform.uppercase")),new CSSInfo("lowercase",CSSStyles.getString("text-transform.lowercase")),new CSSInfo("inherit",CSSStyles.getString("text-transform.inherit"))}),
			new CSSValue("white-space:", new CSSInfo[] {new CSSInfo("normal",""),new CSSInfo("pre",""),new CSSInfo("nowrap",""),new CSSInfo("pre-wrap",""),new CSSInfo("pre-line",""),new CSSInfo("inherit","")}),
			new CSSValue("background-color:", new CSSInfo[] {new CSSInfo("red",""),new CSSInfo("blue",""),new CSSInfo("green",""),new CSSInfo("#",""),new CSSInfo("rgb(255,0,0)","")}),
			new CSSValue("background-image:", new CSSInfo[] {new CSSInfo("url('')",CSSStyles.getString("background-image.url")),new CSSInfo("none",CSSStyles.getString("background-image.none")),new CSSInfo("inherit",CSSStyles.getString("background-image.inherit")),new CSSInfo("linear-gradient(direction, color-stop1, color-stop2, ...)",CSSStyles.getString("background-image.linear-gradient"))}),
			new CSSValue("background-repeat:", new CSSInfo[] {new CSSInfo("repeat",CSSStyles.getString("background-repeat.repeat")),new CSSInfo("repeat-x",CSSStyles.getString("background-repeat.repeat-x")),new CSSInfo("repeat-y",CSSStyles.getString("background-repeat.repeat-y")),new CSSInfo("no-repeat",CSSStyles.getString("background-repeat.no-repeat")),new CSSInfo("inherit",CSSStyles.getString("background-repeat.inherit"))}),
			new CSSValue("background-attachment:", new CSSInfo[] {new CSSInfo("scroll",CSSStyles.getString("background-attachment.scroll")),new CSSInfo("fixed",CSSStyles.getString("background-attachment.fixed")),new CSSInfo("local",CSSStyles.getString("background-attachment.local")),new CSSInfo("initial",CSSStyles.getString("background-attachment.initial")),new CSSInfo("inherit",CSSStyles.getString("background-attachment.inherit"))}),
			new CSSValue("background-position:", new CSSInfo[] {new CSSInfo("left top",CSSStyles.getString("background-position.left")), new CSSInfo("x% y%",CSSStyles.getString("background-position.xy")), new CSSInfo("xpos ypos",CSSStyles.getString("background-position.xpyp")), new CSSInfo("inherit",CSSStyles.getString("background-position.inherit"))}),
			new CSSValue("background:", new CSSInfo[] {new CSSInfo("red",""),new CSSInfo("blue",""),new CSSInfo("green",""),new CSSInfo("#",""),new CSSInfo("rgb(255,0,0)",""),new CSSInfo("url('')","")}),
			new CSSValue("padding-left:"),
			new CSSValue("padding-right:"),
			new CSSValue("padding-top:"),
			new CSSValue("padding-bottom:"),
			new CSSValue("padding:", new CSSInfo[] {new CSSInfo("top rright bottom left")}),
			new CSSValue("border-left:"),
			new CSSValue("border-right:"),
			new CSSValue("border-top:"),
			new CSSValue("border-bottom:"),
			new CSSValue("border:", new CSSInfo[] {new CSSInfo("1px solid #eee")}),
			new CSSValue("margin-left:"),
			new CSSValue("margin-right:"),
			new CSSValue("margin-top:"),
			new CSSValue("margin-bottom:"),
			new CSSValue("margin:", new CSSInfo[] {new CSSInfo("top rright bottom left")}),
			new CSSValue("font-style:", new CSSInfo[] {new CSSInfo("normal",""),new CSSInfo("italic",""),new CSSInfo("oblique",""),new CSSInfo("inherit","")}),
			new CSSValue("font-weight:", new CSSInfo[] {new CSSInfo("normal",""),new CSSInfo("bold",""),new CSSInfo("bolder",""),new CSSInfo("lighter",""),new CSSInfo("inherit","")}),
			new CSSValue("font-variant:", new CSSInfo[] {new CSSInfo("normal",""),new CSSInfo("small-caps",""),new CSSInfo("inherit","")}),
			new CSSValue("font-stretch:"),
			new CSSValue("font-size-adjust:"),
			new CSSValue("font-size:", new CSSInfo[] {new CSSInfo(" px",""),new CSSInfo(" pt",""),new CSSInfo(" %",""),new CSSInfo("xx-small",""),new CSSInfo("x-small",""),new CSSInfo("small",""),new CSSInfo("medium",""),new CSSInfo("large",""),new CSSInfo("x-large",""),new CSSInfo("xx-large",""),new CSSInfo("smaller",""),new CSSInfo("larger",""),new CSSInfo("inherit","")}),
			new CSSValue("font-family:", new CSSInfo[] {new CSSInfo("family-name",""),new CSSInfo("generic-family","")}),
			new CSSValue("font:", new CSSInfo[] {new CSSInfo("font-style",""),new CSSInfo("font-variant",""),new CSSInfo("font-weight",""),new CSSInfo("font-size/line-height","")}),
			new CSSValue("border-left-width:"),
			new CSSValue("border-right-width:"),
			new CSSValue("border-top-width:"),
			new CSSValue("border-bottom-width:"),
			new CSSValue("border-left-color:"),
			new CSSValue("border-right-color:"),
			new CSSValue("border-top-color:"),
			new CSSValue("border-bottom-color:"),
			new CSSValue("border-left-style:"),
			new CSSValue("border-right-style:"),
			new CSSValue("border-top-style:"),
			new CSSValue("border-bottom-style:"),
			new CSSValue("display:", new CSSInfo[] {new CSSInfo("none",CSSStyles.getString("display.none")),new CSSInfo("block",CSSStyles.getString("display.block")),new CSSInfo("inline",CSSStyles.getString("display.inline")),new CSSInfo("inline-block",CSSStyles.getString("display.inline-block"))}),
			new CSSValue("position:", new CSSInfo[] {new CSSInfo("static",CSSStyles.getString("position.static")),new CSSInfo("fixed",CSSStyles.getString("position.fixed")),new CSSInfo("relative",CSSStyles.getString("position.relative")),new CSSInfo("absolute",CSSStyles.getString("position.absolute")),new CSSInfo("sticky",CSSStyles.getString("position.sticky"))}),
			new CSSValue("top:", new CSSInfo[] {new CSSInfo(" px",""),new CSSInfo(" %","")}),
			new CSSValue("bottom:", new CSSInfo[] {new CSSInfo(" px",""),new CSSInfo(" %","")}),
			new CSSValue("left:", new CSSInfo[] {new CSSInfo(" px",""),new CSSInfo(" %","")}),
			new CSSValue("right:", new CSSInfo[] {new CSSInfo(" px",""),new CSSInfo(" %","")}),
			new CSSValue("float:", new CSSInfo[] {new CSSInfo("left",""),new CSSInfo("right",""),new CSSInfo("none",""),new CSSInfo("inherit","")}),
			new CSSValue("opacity:", new CSSInfo[] {new CSSInfo("0.6")}),
			new CSSValue("filter:", new CSSInfo[] {new CSSInfo("alpha(opacity=60)")}),
			new CSSValue("clear:", new CSSInfo[] {new CSSInfo("left",""),new CSSInfo("right",""),new CSSInfo("both",""),new CSSInfo("none",""),new CSSInfo("inherit","")}),
			new CSSValue("z-index:", new CSSInfo[] {new CSSInfo("1","1-9999")}),
			new CSSValue("unicode-bidi:", new CSSInfo[] {new CSSInfo("normal",""),new CSSInfo("embed",""),new CSSInfo("bidi-override",""),new CSSInfo("initial",""),new CSSInfo("inherit","")}),
			new CSSValue("width:", new CSSInfo[] {new CSSInfo(" px",""),new CSSInfo(" %","")}),
			new CSSValue("min-width:", new CSSInfo[] {new CSSInfo(" px",""),new CSSInfo(" %","")}),
			new CSSValue("max-width:", new CSSInfo[] {new CSSInfo(" px",""),new CSSInfo(" %","")}),
			new CSSValue("height:", new CSSInfo[] {new CSSInfo(" px",""),new CSSInfo(" %","")}),
			new CSSValue("min-height:", new CSSInfo[] {new CSSInfo(" px",""),new CSSInfo(" %","")}),
			new CSSValue("max-height:", new CSSInfo[] {new CSSInfo(" px",""),new CSSInfo(" %","")}),
			new CSSValue("line-height:", new CSSInfo[] {new CSSInfo("normal",""),new CSSInfo("number",""),new CSSInfo("length",""),new CSSInfo("%",""),new CSSInfo("inherit","")}),
			new CSSValue("vertical-align:", new CSSInfo[] {new CSSInfo("baseline",""),new CSSInfo("sub",""),new CSSInfo("super",""),new CSSInfo("top",""),new CSSInfo("text-top",""),new CSSInfo("middle",""),new CSSInfo("bottom",""),new CSSInfo("text-bottom",""),new CSSInfo("length",""),new CSSInfo("%",""),new CSSInfo("inherit","")}),
			new CSSValue("overflow:", new CSSInfo[] {new CSSInfo("visible",CSSStyles.getString("overflow.visible")),new CSSInfo("hidden",CSSStyles.getString("overflow.hidden")),new CSSInfo("scroll",CSSStyles.getString("overflow.scroll")),new CSSInfo("auto",CSSStyles.getString("overflow.auto")),new CSSInfo("inherit",CSSStyles.getString("overflow.inherit"))}),
			new CSSValue("clip:", new CSSInfo[] {new CSSInfo("shape"), new CSSInfo("auto"), new CSSInfo("inherit")}),
			new CSSValue("visibility:", new CSSInfo[] {new CSSInfo("visible"), new CSSInfo("hidden"), new CSSInfo("collapse"), new CSSInfo("inherit")}),
			new CSSValue("caption-side:", new CSSInfo[] {new CSSInfo("top"), new CSSInfo("bottom"), new CSSInfo("inherit")}),
			new CSSValue("table-layout:", new CSSInfo[] {new CSSInfo("automatic"), new CSSInfo("fixed"), new CSSInfo("inherit")}),
			new CSSValue("border-collapse:", new CSSInfo[] {new CSSInfo("collapse"), new CSSInfo("separate"), new CSSInfo("separate")}),
			new CSSValue("border-spacing:", new CSSInfo[] {new CSSInfo(" px px"), new CSSInfo("inherit")}),
			new CSSValue("empty-cells:", new CSSInfo[] {new CSSInfo("hide"), new CSSInfo("show"), new CSSInfo("inherit")}),
			new CSSValue("content:", new CSSInfo[] {new CSSInfo("none"), new CSSInfo("normal"), new CSSInfo("counter"), new CSSInfo("attr(attribute)"), new CSSInfo("string"), new CSSInfo("\""), new CSSInfo("url(\"\")"), new CSSInfo("inherit")}),
			new CSSValue("quotes:", new CSSInfo[] {new CSSInfo("none"), new CSSInfo("string string string string"), new CSSInfo("inherit")}),
			new CSSValue("list-style-type:", new CSSInfo[] {new CSSInfo("none"), new CSSInfo("disc"), new CSSInfo("circle"), new CSSInfo("square"), new CSSInfo("decimal"), new CSSInfo("decimal-leading-zero"), new CSSInfo("upper-roman"), new CSSInfo("lower-roman"), new CSSInfo("upper-roman"), new CSSInfo("upper-alpha"), new CSSInfo("lower-greek"), new CSSInfo("lower-latin"), new CSSInfo("upper-latin"), new CSSInfo("hebrew"), new CSSInfo("armenian"), new CSSInfo("georgian"), new CSSInfo("cjk-ideographic"), new CSSInfo("hiragana"), new CSSInfo("katakana"), new CSSInfo("hiragana-iroha"), new CSSInfo("katakana-iroha")}),
			new CSSValue("list-style-image:"),
			new CSSValue("list-style-position:"),
			new CSSValue("list-style:", new CSSInfo[] {new CSSInfo("initial"), new CSSInfo("inherit")}),
			new CSSValue("marker-offset:"),
			new CSSValue("cursor:", new CSSInfo[] {new CSSInfo("auto",CSSStyles.getString("cursor.auto")),new CSSInfo("default",CSSStyles.getString("cursor.default")),new CSSInfo("pointer",CSSStyles.getString("cursor.pointer")),new CSSInfo("help",CSSStyles.getString("cursor.help")),new CSSInfo("progress",CSSStyles.getString("cursor.progress")),new CSSInfo("wait",CSSStyles.getString("cursor.wait")),new CSSInfo("text",CSSStyles.getString("cursor.text")),new CSSInfo("move",CSSStyles.getString("cursor.move")),new CSSInfo("col-resize",CSSStyles.getString("cursor.col-resize")),new CSSInfo("row-resize",CSSStyles.getString("cursor.row-resize")),new CSSInfo("n-resize",CSSStyles.getString("cursor.n-resize")),new CSSInfo("s-resize",CSSStyles.getString("cursor.s-resize")),new CSSInfo("grab",CSSStyles.getString("cursor.grab")),new CSSInfo("grabbing",CSSStyles.getString("cursor.grabbing")),new CSSInfo("none",CSSStyles.getString("cursor.none"))}),
			new CSSValue("outline-width:"),
			new CSSValue("outline-color:"),
			new CSSValue("outline-style:"),
			new CSSValue("outline:"),
			new CSSValue("transform:", new CSSInfo[] {new CSSInfo("translate()"),new CSSInfo("rotate()"),new CSSInfo("scale()"),new CSSInfo("skew()"),new CSSInfo("matrix()")}),
			/* CSS3 新文本属性 */
			new CSSValue("hanging-punctuation:", new CSSInfo[] {new CSSInfo("none", CSSStyles.getString("hanging-punctuation.none")),new CSSInfo("first", CSSStyles.getString("hanging-punctuation.first")),new CSSInfo("last", CSSStyles.getString("hanging-punctuation.last")),new CSSInfo("allow-end",CSSStyles.getString("hanging-punctuation.allow-end")),new CSSInfo("force-end")}),
			new CSSValue("punctuation-trim:", new CSSInfo[] {new CSSInfo("none", CSSStyles.getString("punctuation-trim.none")),new CSSInfo("start", CSSStyles.getString("punctuation-trim.start")),new CSSInfo("end", CSSStyles.getString("punctuation-trim.end")),new CSSInfo("allow-end",CSSStyles.getString("punctuation-trim.allow-end")),new CSSInfo("adjacent",CSSStyles.getString("punctuation-trim.adjacent"))}),
			new CSSValue("text-align-last:", new CSSInfo[] {new CSSInfo("auto", CSSStyles.getString("text-align-last.auto")),new CSSInfo("left", CSSStyles.getString("text-align-last.left")),new CSSInfo("right", CSSStyles.getString("text-align-last.right")),new CSSInfo("center",CSSStyles.getString("text-align-last.center")),new CSSInfo("justify",CSSStyles.getString("text-align-last.justify")),new CSSInfo("start",CSSStyles.getString("text-align-last.start")),new CSSInfo("end",CSSStyles.getString("text-align-last.end")),new CSSInfo("initial",CSSStyles.getString("text-align-last.initial")),new CSSInfo("inherit",CSSStyles.getString("text-align-last.inherit"))}),
			new CSSValue("text-emphasis:", new CSSInfo[] {new CSSInfo("text-emphasis-style", CSSStyles.getString("text-emphasis.text-emphasis-style")),new CSSInfo("text-emphasis-color", CSSStyles.getString("text-emphasis.text-emphasis-color"))}),
			new CSSValue("text-justify:", new CSSInfo[] {new CSSInfo("auto", CSSStyles.getString("text-justify.auto")),new CSSInfo("none", CSSStyles.getString("text-justify.none")),new CSSInfo("inter-word", CSSStyles.getString("text-justify.inter-word")),new CSSInfo("inter-ideograph",CSSStyles.getString("text-justify.inter-ideograph")),new CSSInfo("inter-cluster",CSSStyles.getString("text-justify.inter-cluster")),new CSSInfo("distribute",CSSStyles.getString("text-justify.distribute")),new CSSInfo("kashida",CSSStyles.getString("text-justify.kashida"))}),
			new CSSValue("text-outline:", new CSSInfo[] {new CSSInfo("thickness", CSSStyles.getString("text-outline.thickness")),new CSSInfo("blur", CSSStyles.getString("text-outline.blur")),new CSSInfo("color", CSSStyles.getString("text-outline.color"))}),
			new CSSValue("text-overflow:", new CSSInfo[] {new CSSInfo("clip", CSSStyles.getString("text-overflow.clip")),new CSSInfo("ellipsis", CSSStyles.getString("text-overflow.ellipsis")),new CSSInfo("string", CSSStyles.getString("text-overflow.string")),new CSSInfo("initial",CSSStyles.getString("text-overflow.initial")),new CSSInfo("inherit",CSSStyles.getString("text-overflow.inherit"))}),
			new CSSValue("text-shadow:", new CSSInfo[] {new CSSInfo("h-shadow", CSSStyles.getString("text-shadow.h-shadow")),new CSSInfo("v-shadow", CSSStyles.getString("text-shadow.v-shadow")),new CSSInfo("blur", CSSStyles.getString("text-shadow.blur")),new CSSInfo("color", CSSStyles.getString("text-shadow.color"))}),
			new CSSValue("text-wrap:", new CSSInfo[] {new CSSInfo("normal", CSSStyles.getString("text-wrap.normal")),new CSSInfo("none", CSSStyles.getString("text-wrap.none")),new CSSInfo("unrestricted", CSSStyles.getString("text-wrap.unrestricted")),new CSSInfo("suppress",CSSStyles.getString("text-wrap.suppress"))}),
			new CSSValue("word-break:", new CSSInfo[] {new CSSInfo("normal", CSSStyles.getString("word-break.normal")),new CSSInfo("break-all", CSSStyles.getString("word-break.break-all")),new CSSInfo("keep-all", CSSStyles.getString("word-break.keep-all"))}),
			new CSSValue("word-wrap:", new CSSInfo[] {new CSSInfo("normal", CSSStyles.getString("word-wrap.normal")),new CSSInfo("break-word", CSSStyles.getString("word-wrap.break-word"))})
	};
	
	/**
	 * 伪元素
	 */
	public static final CSSInfo[] Pseudo_Element = {
			new CSSInfo("link"),
			new CSSInfo("visited"),
			new CSSInfo("active"),
			new CSSInfo("hover"),
			new CSSInfo("focus"),
			new CSSInfo("first-letter"),
			new CSSInfo("first-line"),
			new CSSInfo("first-child"),
			new CSSInfo("before"),
			new CSSInfo("after"),
			new CSSInfo("lang(language)")
	};
	
	/**
	 * 媒体类型
	 */
	public static final CSSInfo[] Media_Type = {
			new CSSInfo("@media all", CSSStyles.getString("media.all")),
			new CSSInfo("@media aural", CSSStyles.getString("media.aural")),
			new CSSInfo("@media braille", CSSStyles.getString("media.braille")),
			new CSSInfo("@media embossed", CSSStyles.getString("media.embossed")),
			new CSSInfo("@media handheld", CSSStyles.getString("media.handheld")),
			new CSSInfo("@media print", CSSStyles.getString("media.print")),
			new CSSInfo("@media projection", CSSStyles.getString("media.projection")),
			new CSSInfo("@media screen", CSSStyles.getString("media.screen")),
			new CSSInfo("@media tty", CSSStyles.getString("media.tty")),
			new CSSInfo("@media tv", CSSStyles.getString("media.tv"))
	};
	
//	static {
//		// sort by length
//		Arrays.sort(CSS_KEYWORDS,new Comparator(){
//			public int compare(Object o1, Object o2){
//				CSSInfo info1 = (CSSInfo)o1;
//				CSSInfo info2 = (CSSInfo)o2;
//				if(info1.getReplaceString().length() > info2.getReplaceString().length()){
//					return -1;
//				}
//				if(info1.getReplaceString().length() < info2.getReplaceString().length()){
//					return 1;
//				}
//				return 0;
//			}
//		});
//	}
}
