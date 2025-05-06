package tern.eclipse.ide.linter.core;

import java.io.IOException;
import java.io.InputStream;

import tern.eclipse.ide.linter.internal.core.SAXLinterConfigHandler;

/**
 * Tern linter config factory which loads the config from a XML file.
 *
 */
public abstract class XMLTernLinterConfigFactory implements
		ITernLinterConfigFactory {

	@Override
	public ITernLinterConfig create() throws IOException {
		return new SAXLinterConfigHandler().load(getInputStream()).getLinter();
	}

	protected abstract InputStream getInputStream();

}
