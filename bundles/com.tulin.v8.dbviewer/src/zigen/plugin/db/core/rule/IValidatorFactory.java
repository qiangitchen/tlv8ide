/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.core.rule;

import zigen.plugin.db.core.TableColumn;

public interface IValidatorFactory {

	abstract public String validate(TableColumn column, Object value) throws UnSupportedTypeException;

	abstract public String getNullSymbol();

	abstract public void setNullSymbol(String nullSymbol);
}
