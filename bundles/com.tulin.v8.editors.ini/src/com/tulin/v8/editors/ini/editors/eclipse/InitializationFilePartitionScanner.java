package com.tulin.v8.editors.ini.editors.eclipse;

import java.util.ArrayList;
import java.util.List;
import org.eclipse.jface.text.rules.EndOfLineRule;
import org.eclipse.jface.text.rules.ICharacterScanner;
import org.eclipse.jface.text.rules.IPredicateRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.IWordDetector;
import org.eclipse.jface.text.rules.RuleBasedPartitionScanner;
import org.eclipse.jface.text.rules.SingleLineRule;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.jface.text.rules.WordRule;

public class InitializationFilePartitionScanner extends RuleBasedPartitionScanner implements IPropertiesFilePartitions {
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public InitializationFilePartitionScanner() {
		IToken comment = new Token("__pf_comment");
		IToken propertyValue = new Token("__pf_roperty_value");
		IToken key = new Token("__dftl_partition_content_type");

		List rules = new ArrayList();

		rules.add(new LeadingWhitespacePredicateRule(key, "\t"));
		rules.add(new LeadingWhitespacePredicateRule(key, " "));

		rules.add(new EndOfLineRule("#", comment, '\000', true));
		rules.add(new EndOfLineRule("!", comment, '\000', true));

		rules.add(new SingleLineRule("=", null, propertyValue, '\\', true, true));
		rules.add(new SingleLineRule(":", null, propertyValue, '\\', true, true));
		rules.add(new SingleLineRule(" ", null, propertyValue, '\\', true, true));
		rules.add(new SingleLineRule("\t", null, propertyValue, '\\', true, true));

		EmptyCommentRule wordRule = new EmptyCommentRule(comment);
		rules.add(wordRule);

		IPredicateRule[] result = new IPredicateRule[rules.size()];
		rules.toArray(result);
		setPredicateRules(result);
	}

	static class EmptyCommentDetector implements IWordDetector {
		public boolean isWordStart(char c) {
			return c == '#';
		}

		public boolean isWordPart(char c) {
			return c == '#';
		}
	}

	static class EmptyCommentRule extends WordRule implements IPredicateRule {
		private IToken fSuccessToken;

		public EmptyCommentRule(IToken successToken) {
			super(new EmptyCommentDetector(), successToken);
			this.fSuccessToken = successToken;
			addWord("#", this.fSuccessToken);
		}

		public IToken evaluate(ICharacterScanner scanner, boolean resume) {
			return evaluate(scanner);
		}

		public IToken getSuccessToken() {
			return this.fSuccessToken;
		}
	}
}
