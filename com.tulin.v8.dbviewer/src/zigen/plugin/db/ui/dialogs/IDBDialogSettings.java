/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.ui.dialogs;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

import zigen.plugin.db.core.SchemaInfo;

public interface IDBDialogSettings {

	public IDBDialogSettings addNewSection(String name);

	public void addSection(IDBDialogSettings section);

	public String get(String key);

	public String[] getArray(String key);

	public SchemaInfo[] getSchemaInfos(String key); // ADD

	public boolean getBoolean(String key);

	public double getDouble(String key) throws NumberFormatException;

	public float getFloat(String key) throws NumberFormatException;

	public int getInt(String key) throws NumberFormatException;

	public long getLong(String key) throws NumberFormatException;

	public String getName();

	public IDBDialogSettings getSection(String sectionName);

	public IDBDialogSettings[] getSections();

	public void load(Reader reader) throws IOException;

	public void load(String fileName) throws IOException;

	public void put(String key, String[] value);

	public void put(String key, SchemaInfo[] value);

	public void put(String key, double value);

	public void put(String key, float value);

	public void put(String key, int value);

	public void put(String key, long value);

	public void put(String key, String value);

	public void put(String key, boolean value);

	public void save(Writer writer) throws IOException;

	public void save(String fileName) throws IOException;

	public void removeSection(String sectionName);

	public boolean hasSection(String sectionName);
}
