package tern.server.protocol.type;

import tern.server.protocol.DocFormat;
import tern.server.protocol.JsonHelper;
import tern.server.protocol.TernQuery;

/**
 * Tern type query.
 * 
 * <cite> Query the type of something. </cite>
 * 
 * @see http://ternjs.net/doc/manual.html#req_type
 * 
 */
public class TernTypeQuery extends TernQuery {

	private static final long serialVersionUID = 1L;

	private static final String TYPE_TYPE_QUERY = "type";

	private static final String TYPES_FIELD_NAME = "types";

	private static final String DOCS_FIELD_NAME = "docs";

	private static final String URLS_FIELD_NAME = "urls";

	private static final String ORIGINS_FIELD_NAME = "origins";

	private static final String DOCS_FORMAT_NAME = "docFormat";

	public TernTypeQuery(String file, Integer pos) {
		super(TYPE_TYPE_QUERY);
		setFile(file);
		setEnd(pos);
	}

	/**
	 * Whether to include the types of the completions in the result data.
	 * 
	 * @param types
	 */
	public void setTypes(boolean types) {
		super.add(TYPES_FIELD_NAME, types);
	}

	/**
	 * Whether to include the types of the completions in the result data.
	 * 
	 * @return
	 */
	public boolean isTypes() {
		return JsonHelper.getBoolean(this, TYPES_FIELD_NAME, false);
	}

	public void setDocs(boolean docs) {
		super.add(DOCS_FIELD_NAME, docs);
	}

	public boolean isDocs() {
		return JsonHelper.getBoolean(this, DOCS_FIELD_NAME, false);
	}

	public void setUrls(boolean urls) {
		super.add(URLS_FIELD_NAME, urls);
	}

	public boolean isUrls() {
		return JsonHelper.getBoolean(this, URLS_FIELD_NAME, false);
	}

	public void setOrigins(boolean origins) {
		super.add(ORIGINS_FIELD_NAME, origins);
	}

	public boolean isOrigins() {
		return JsonHelper.getBoolean(this, ORIGINS_FIELD_NAME, false);
	}

	public void setDocFormat(DocFormat docFormat) {
		super.add(DOCS_FORMAT_NAME, docFormat.name());
	}

	public String getDocFormat() {
		return JsonHelper.getString(this, DOCS_FORMAT_NAME);
	}
	
	/**
	 * Returns true if the given query type is "type" and false otherwise.
	 * 
	 * @param queryType
	 *            the query type.
	 * @return true if the given query type is "type" and false otherwise.
	 */
	public static boolean isQueryType(String queryType) {
		return TYPE_TYPE_QUERY.equals(queryType);
	}

}
