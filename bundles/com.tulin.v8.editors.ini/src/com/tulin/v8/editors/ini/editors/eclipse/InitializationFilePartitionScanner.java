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
		/* 94 */ IToken propertyValue = new Token("__pf_roperty_value");
		/* 95 */ IToken key = new Token("__dftl_partition_content_type");

		/* 97 */ List rules = new ArrayList();

		/* 100 */ rules.add(new LeadingWhitespacePredicateRule(key, "\t"));
		/* 101 */ rules.add(new LeadingWhitespacePredicateRule(key, " "));

		/* 104 */ rules.add(new EndOfLineRule("#", comment, '\000', true));
		/* 105 */ rules.add(new EndOfLineRule("!", comment, '\000', true));

		/* 108 */ rules.add(new SingleLineRule("=", null, propertyValue, '\\', true, true));
		/* 109 */ rules.add(new SingleLineRule(":", null, propertyValue, '\\', true, true));
		/* 110 */ rules.add(new SingleLineRule(" ", null, propertyValue, '\\', true, true));
		/* 111 */ rules.add(new SingleLineRule("\t", null, propertyValue, '\\', true, true));

		/* 114 */ EmptyCommentRule wordRule = new EmptyCommentRule(comment);
		/* 115 */ rules.add(wordRule);

		/* 117 */ IPredicateRule[] result = new IPredicateRule[rules.size()];
		/* 118 */ rules.toArray(result);
		/* 119 */ setPredicateRules(result);
	}

	static class EmptyCommentDetector implements IWordDetector {
		public boolean isWordStart(char c) {
			/* 43 */ return c == '#';
		}

		public boolean isWordPart(char c) {
			/* 50 */ return c == '#';
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
			/* 75 */ return evaluate(scanner);
		}

		public IToken getSuccessToken() {
			/* 82 */ return this.fSuccessToken;
		}
	}
}
