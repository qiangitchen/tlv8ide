package tern.eclipse.ide.linter.internal.core;

import java.io.IOException;

import tern.eclipse.ide.linter.core.ITernLinterConfig;
import tern.eclipse.ide.linter.core.ITernLinterConfigFactory;

/**
 * Tern linter configuration.
 * 
 * @author azerr
 *
 */
public class TernLinterConfiguration {

	private final ITernLinterConfigFactory factory;
	private final String filename;

	public TernLinterConfiguration(ITernLinterConfigFactory factory,
			String filename) {
		this.factory = factory;
		this.filename = filename;
	}

	public ITernLinterConfig create() throws IOException {
		return factory.create();
	}

	public String getFilename() {
		return filename;
	}
}
