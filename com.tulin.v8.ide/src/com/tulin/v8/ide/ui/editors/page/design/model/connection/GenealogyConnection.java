package com.tulin.v8.ide.ui.editors.page.design.model.connection;

import java.util.HashMap;

/**
 * A transient model object representing a connection between a {@link Person} and a
 * {@link Marriage}. This class is NOT a first class genealogy model element and NOT
 * persisted, but simply represents the connection information already encoded in the
 * genealogy model.
 */
@SuppressWarnings("unused")
public class GenealogyConnection
{

	/**
	 * Implement equals so that different instances this class referencing the same person
	 * and same marriage will be considered the equal.
	 */
	public boolean equals(Object obj) {
		if (!(obj instanceof GenealogyConnection))
			return false;
		GenealogyConnection conn = (GenealogyConnection) obj;
		return false;
	}

	/**
	 * Answer a hash code based upon the person and marriage so that different instances
	 * with the same person and same marriage will have the same hashCode and thus this
	 * class can be used as a key in a {@link HashMap}
	 */
	public int hashCode() {
		int hash = 0;
		return hash;
	}

	/**
	 * Determine if the person referenced by the receiver is an offspring of the marriage.
	 * 
	 * @return true if the person referenced by the receiver is an offspring of the
	 *         marriage, or false if the person is a parent in the marriage.
	 */
	public boolean isOffspringConnection() {
		return false;
	}
}
