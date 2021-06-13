/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.ui.views.internal;

import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.IWhitespaceDetector;
import org.eclipse.jface.text.rules.IWordDetector;
import org.eclipse.jface.text.rules.RuleBasedScanner;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.jface.text.rules.WhitespaceRule;
import org.eclipse.jface.text.rules.WordRule;
import org.eclipse.swt.SWT;

import zigen.plugin.db.DbPlugin;
import zigen.plugin.db.DbPluginFormatRule;
import zigen.plugin.db.preference.SQLEditorPreferencePage;

public class SQLKeywordScanner extends RuleBasedScanner implements
		ISQLTokenScanner {

	static class WordDetector implements IWordDetector {

		public boolean isWordPart(char c) {
			return Character.isJavaIdentifierPart(c);
		}

		public boolean isWordStart(char c) {
			return Character.isJavaIdentifierStart(c);
		}
	}

	static class WhitespaceDetector implements IWhitespaceDetector {

		public boolean isWhitespace(char character) {
			return Character.isWhitespace(character);
		}
	}

	private ColorManager colorManager;

	protected DbPluginFormatRule rule;

	public SQLKeywordScanner(ColorManager colorManager) {
		this.colorManager = colorManager;

		rule = DbPluginFormatRule.getInstance();

		initialize(false);
	}

	public void initialize() {
		initialize(true);
	}

	public void initialize(boolean marge) {

		if (marge)
			rule.margeTemplate();

		setDefaultReturnToken(new Token(new TextAttribute(colorManager.getColor(SQLEditorPreferencePage.P_COLOR_DEFAULT))));

		IRule[] rules = new IRule[2];
		IToken other = new Token(new TextAttribute(colorManager.getColor(SQLEditorPreferencePage.P_COLOR_DEFAULT)));

		
		String styleKeyword = DbPlugin.getDefault().getPreferenceStore().getString(SQLEditorPreferencePage.P_STYLE_KEYWORD);
		String styleFunction = DbPlugin.getDefault().getPreferenceStore().getString(SQLEditorPreferencePage.P_STYLE_FUNCTION);
	
		IToken keyword = new Token(new TextAttribute(colorManager.getColor(SQLEditorPreferencePage.P_COLOR_KEYWORD), null, Integer.parseInt(styleKeyword)));
		IToken function = new Token(new TextAttribute(colorManager.getColor(SQLEditorPreferencePage.P_COLOR_FUNCTION), null, Integer.parseInt(styleFunction)));
	
		WordRule wordRule = new WordRule(new WordDetector(), other);


		String[] keywords = rule.getKeywordNames();
		for (int i = 0; i < keywords.length; i++) {
			String key = keywords[i];
			wordRule.addWord(key, keyword);
			wordRule.addWord(key.toLowerCase(), keyword);
		}

		String[] functions = rule.getFunctionNames();
		for (int i = 0; i < functions.length; i++) {
			String name = functions[i];
			wordRule.addWord(name, function);
			wordRule.addWord(name.toLowerCase(), function);

		}

		rules[0] = wordRule;
		rules[1] = new WhitespaceRule(new WhitespaceDetector());
		setRules(rules);

	}
}
