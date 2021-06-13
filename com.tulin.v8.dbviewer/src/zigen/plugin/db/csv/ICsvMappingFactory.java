/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.csv;

import java.sql.ResultSet;

public interface ICsvMappingFactory {

	abstract public String getCsvValue(ResultSet rs, int icol) throws Exception;

}
