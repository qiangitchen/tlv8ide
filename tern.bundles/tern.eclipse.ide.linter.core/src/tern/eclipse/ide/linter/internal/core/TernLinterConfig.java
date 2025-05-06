package tern.eclipse.ide.linter.internal.core;

import tern.eclipse.ide.linter.core.ITernLinterConfig;

/**
 * Tern linter config implementation.
 *
 */
public class TernLinterConfig extends TernLinterOption implements
		ITernLinterConfig {

	public TernLinterConfig(String id) {
		super(id, null, null);
	}

}
