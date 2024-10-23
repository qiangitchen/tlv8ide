package tern.eclipse.ide.linter.lint.internal.core;

import java.io.InputStream;

import tern.eclipse.ide.linter.core.XMLTernLinterConfigFactory;

/**
 * Lint linter configuration.
 *
 */
public class LintLinterConfiguration extends XMLTernLinterConfigFactory {

	@Override
	protected InputStream getInputStream() {
		return LintLinterConfiguration.class
				.getResourceAsStream("lint-linter-config.xml");
	}
}
