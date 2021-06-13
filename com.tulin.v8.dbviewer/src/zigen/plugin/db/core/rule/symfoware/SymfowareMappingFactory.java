/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.core.rule.symfoware;

import zigen.plugin.db.core.rule.DefaultMappingFactory;
import zigen.plugin.db.core.rule.IMappingFactory;

public class SymfowareMappingFactory extends DefaultMappingFactory implements IMappingFactory {

	public SymfowareMappingFactory(boolean convertUnicode) {
		super(convertUnicode);
	}

}
