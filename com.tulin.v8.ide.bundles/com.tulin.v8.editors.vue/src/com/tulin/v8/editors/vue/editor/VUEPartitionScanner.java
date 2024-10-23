package com.tulin.v8.editors.vue.editor;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.text.rules.IPredicateRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.MultiLineRule;
import org.eclipse.jface.text.rules.Token;

import com.tulin.v8.webtools.ide.html.DocTypeRule;
import com.tulin.v8.webtools.ide.html.HTMLInnerRule;
import com.tulin.v8.webtools.ide.html.HTMLPartitionScanner;
import com.tulin.v8.webtools.ide.html.TagRule;

public class VUEPartitionScanner extends HTMLPartitionScanner {

	public VUEPartitionScanner() {
		IToken htmlComment = new Token(HTML_COMMENT);
		IToken htmlTag = new Token(HTML_TAG);
		IToken prefixTag = new Token(PREFIX_TAG);
		IToken htmlScript = new Token(HTML_SCRIPT);
		IToken htmlDoctype = new Token(HTML_DOCTYPE);
		IToken htmlDirective = new Token(HTML_DIRECTIVE);
		IToken javaScript = new Token(JAVASCRIPT);
		IToken htmlCss = new Token(HTML_CSS);

		List<IPredicateRule> rules = new ArrayList<IPredicateRule>();

		rules.add(new MultiLineRule("<!--", "-->", htmlComment));
		rules.add(new MultiLineRule("<%--", "--%>", htmlComment));
		rules.add(new DocTypeRule(htmlDoctype));
		rules.add(new MultiLineRule("<%@", "%>", htmlDirective));
		rules.add(new MultiLineRule("<%", "%>", htmlScript));
		rules.add(new MultiLineRule("<![CDATA[", "]]>", htmlDoctype));
		rules.add(new MultiLineRule("<?xml", "?>", htmlDoctype));
		rules.add(new HTMLInnerRule("<script", "</script>", javaScript));
		rules.add(new HTMLInnerRule("{{", "}}", javaScript));
		rules.add(new HTMLInnerRule("<style", "</style>", htmlCss));
		rules.add(new TagRule(prefixTag, TagRule.PREFIX));
		rules.add(new TagRule(htmlTag, TagRule.NO_PREFIX));

		setPredicateRules(rules.toArray(new IPredicateRule[rules.size()]));
	}

}
