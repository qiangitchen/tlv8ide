/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.core.rule;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import zigen.plugin.db.core.TableColumn;


public interface IMappingFactory {

	abstract public boolean canModifyDataType(int dataType);

	abstract public Object getObject(ResultSet rs, int icol) throws Exception;

	abstract public void setObject(PreparedStatement pst, int icol, TableColumn column, Object value) throws Exception;

	abstract public String getNullSymbol();

	abstract public void setNullSymbol(String nullSymbol);

	abstract public void setConvertUnicode(boolean convertUnicode);

}
