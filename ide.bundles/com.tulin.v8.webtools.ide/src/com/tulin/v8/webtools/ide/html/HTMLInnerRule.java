package com.tulin.v8.webtools.ide.html;

import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.MultiLineRule;

public class HTMLInnerRule extends MultiLineRule {

	public HTMLInnerRule(String startSequence, String endSequence, IToken token) {
		super(startSequence, endSequence, token);
	}

}
