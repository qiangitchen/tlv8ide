package tern.server.protocol.type;

import tern.server.protocol.IJSONObjectHelper;

/**
 * {@link ITernTypeCollector} implementation used to validate the existing of
 * type.
 * 
 */
public class ValidationTernTypeCollector implements ITernTypeCollector {

	private boolean exists;

	@Override
	public void setType(String type, boolean guess, String name,
			String exprName, String doc, String url, String origin,
			Object item, IJSONObjectHelper objectHelper) {
		exists = name != null;
	}

	/**
	 * Returns true if the type exists and false otherwise.
	 * 
	 * @return true if the type exists and false otherwise.
	 */
	public boolean isExists() {
		return exists;
	}

}
