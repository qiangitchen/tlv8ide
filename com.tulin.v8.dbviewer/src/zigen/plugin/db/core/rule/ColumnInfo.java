package zigen.plugin.db.core.rule;

import java.io.Serializable;

public class ColumnInfo implements Serializable {

	private static final long serialVersionUID = 1L;

	String column_name;

	String data_type;

	java.math.BigDecimal data_precision;

	java.math.BigDecimal data_scale;

	String data_default;

	String comments;

	public ColumnInfo() {}

	public String getColumn_name() {
		return this.column_name;
	}

	public void setColumn_name(String column_name) {
		this.column_name = column_name;
	}

	public String getData_type() {
		return this.data_type;
	}

	public void setData_type(String data_type) {
		this.data_type = data_type;
	}

	public java.math.BigDecimal getData_precision() {
		return this.data_precision;
	}

	public void setData_precision(java.math.BigDecimal data_precision) {
		this.data_precision = data_precision;
	}

	public java.math.BigDecimal getData_scale() {
		return this.data_scale;
	}

	public void setData_scale(java.math.BigDecimal data_scale) {
		this.data_scale = data_scale;
	}

	public String getData_default() {
		return this.data_default;
	}

	public void setData_default(String data_default) {
		this.data_default = data_default;
	}

	public String getComments() {
		return this.comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("[OracleColumnInfo:");
		buffer.append(" column_name: ");
		buffer.append(column_name);
		buffer.append(" data_type: ");
		buffer.append(data_type);
		buffer.append(" data_precision: ");
		buffer.append(data_precision);
		buffer.append(" data_scale: ");
		buffer.append(data_scale);
		buffer.append(" data_default: ");
		buffer.append(data_default);
		buffer.append(" comments: ");
		buffer.append(comments);
		buffer.append("]");
		return buffer.toString();
	}

}
