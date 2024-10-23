package tern.eclipse.ide.linter.eslint.internal.core;

import java.io.InputStream;

import tern.eclipse.ide.linter.core.XMLTernLinterConfigFactory;

/**
 * ESLint linter configuration.
 *
 */
public class ESLintLinterConfiguration extends XMLTernLinterConfigFactory {

	@Override
	protected InputStream getInputStream() {
		return ESLintLinterConfiguration.class
				.getResourceAsStream("eslint-linter-config.xml");
	}
}
