package com.tulin.v8.editors.ini.editors.eclipse;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.ui.text.IColorManager;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.IWordDetector;
import org.eclipse.jface.text.rules.WhitespaceRule;
import org.eclipse.jface.text.rules.WordRule;

@SuppressWarnings({ "rawtypes", "unchecked" })
public final class PropertyValueScanner extends AbstractJavaScanner {
	private static String[] fgTokenProperties = { "pf_coloring_value", "pf_coloring_argument",
			"pf_coloring_assignment" };

	public PropertyValueScanner(IColorManager manager, IPreferenceStore store) {
		super(manager, store);
		initialize();
	}

	protected String[] getTokenProperties() {
		return fgTokenProperties;
	}

	protected List<IRule> createRules() {
		setDefaultReturnToken(getToken("pf_coloring_value"));
		List rules = new ArrayList();
		IToken token = getToken("pf_coloring_argument");
		rules.add(new ArgumentRule(token));
		token = getToken("pf_coloring_assignment");
		WordRule wordRule = new WordRule(new AssignmentDetector(), token);
		rules.add(wordRule);
		rules.add(new WhitespaceRule(new JavaWhitespaceDetector()));
		return rules;
	}

	public class AssignmentDetector implements IWordDetector {
		public AssignmentDetector() {
		}

		public boolean isWordStart(char c) {
			return Character.isJavaIdentifierStart(c);
		}

		public boolean isWordPart(char c) {
			return Character.isJavaIdentifierPart(c);
		}
	}
}