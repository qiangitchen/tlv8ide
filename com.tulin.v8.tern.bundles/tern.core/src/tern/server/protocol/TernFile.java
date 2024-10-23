package tern.server.protocol;

import com.eclipsesource.json.JsonObject;

import tern.server.protocol.html.HtmlHelper;
import tern.server.protocol.html.ScriptTagRegion;

/**
 * Tern file.
 * 
 * @see http://ternjs.net/doc/manual.html#protocol
 */
public class TernFile extends JsonObject {

	private static final long serialVersionUID = 1L;

	private static final String NAME_FIELD_NAME = "name";
	private static final String TEXT_FIELD_NAME = "text";
	private static final String OFFSET_FIELD_NAME = "offset";
	private static final String TYPE_FIELD_NAME = "type";
	private static final String OFFSET_LINES_FIELD_TYPE = "offsetLines";

	public enum FileType {
		part, full, delete
	}

	/**
	 * Tern file to 'delete' file.
	 * 
	 * @param name
	 *            file name
	 */
	public TernFile(String name) {
		super.add(NAME_FIELD_NAME, name);
		super.add(TYPE_FIELD_NAME, FileType.delete.name());
	}

	/**
	 * Tern file for 'full' type.
	 * 
	 * @param name
	 *            file name
	 * @param text
	 *            content of the file
	 * @param tags
	 *            supported tags.
	 */
	public TernFile(String name, String text, ScriptTagRegion[] tags) {
		this(name, text, tags, null);
	}

	/**
	 * Tern file for 'part' type.
	 * 
	 * @param name
	 *            file name
	 * @param text
	 *            content of the file
	 * @param tags
	 *            supported tags.
	 */
	public TernFile(String name, String text, ScriptTagRegion[] tags,
			Integer offset) {
		super.add(NAME_FIELD_NAME, name);
		super.add(TEXT_FIELD_NAME, getText(text, tags));
		if (offset != null) {
			super.add(TYPE_FIELD_NAME, FileType.part.name());
			super.add(OFFSET_LINES_FIELD_TYPE, offset);
		} else {
			super.add(TYPE_FIELD_NAME, FileType.full.name());
		}
	}

	private String getText(String text, ScriptTagRegion[] tags) {
		if (text == null || tags == null) {
			return text;
		}
		return HtmlHelper.extractJS(text, tags);
	}

	public String getName() {
		return JsonHelper.getString(this, NAME_FIELD_NAME);
	}

	public String getText() {
		return JsonHelper.getString(this, TEXT_FIELD_NAME);
	}

	public Integer getOffset() {
		return JsonHelper.getInteger(this, OFFSET_FIELD_NAME);
	}

	public String getType() {
		return JsonHelper.getString(this, TYPE_FIELD_NAME);
	}

	public boolean isType(FileType type) {
		return type.name().equals(getType());
	}
}
