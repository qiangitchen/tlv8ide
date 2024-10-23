package tern.eclipse.ide.internal.ui.descriptors.options;

/**
 * Severities for tern-lint.
 *
 */
public enum LintRuleSeverity {

	none, warning, error;

	private final static String[] array = { none.name(), warning.name(),
			error.name() };

	public static String[] toStringArray() {
		return array;
	}
}
