/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.ext.oracle.internal;

import java.io.Serializable;

public class ExplainVo implements Serializable {

	private static final long serialVersionUID = 1L;

	private java.math.BigDecimal id;

	private java.math.BigDecimal parent_id;

	private java.math.BigDecimal position;

	private String operation;

	private String options;

	private String object_name;

	private String object_type;

	private java.math.BigDecimal cost;

	private java.math.BigDecimal cardinality;

	private java.math.BigDecimal bytes;

	private String access_predicates;

	private String filter_predicates;

	public ExplainVo() {}

	public java.math.BigDecimal getId() {
		return this.id;
	}

	public void setId(java.math.BigDecimal id) {
		this.id = id;
	}

	public java.math.BigDecimal getParent_id() {
		return this.parent_id;
	}

	public void setParent_id(java.math.BigDecimal parent_id) {
		this.parent_id = parent_id;
	}

	public java.math.BigDecimal getPosition() {
		return this.position;
	}

	public void setPosition(java.math.BigDecimal position) {
		this.position = position;
	}

	public String getOperation() {
		return this.operation;
	}

	public void setOperation(String operation) {
		this.operation = operation;
	}

	public String getOptions() {
		return this.options;
	}

	public void setOptions(String options) {
		this.options = options;
	}

	public String getObject_name() {
		return this.object_name;
	}

	public void setObject_name(String object_name) {
		this.object_name = object_name;
	}

	public String getObject_type() {
		return this.object_type;
	}

	public void setObject_type(String object_type) {
		this.object_type = object_type;
	}

	public java.math.BigDecimal getCost() {
		return this.cost;
	}

	public void setCost(java.math.BigDecimal cost) {
		this.cost = cost;
	}

	public java.math.BigDecimal getCardinality() {
		return this.cardinality;
	}

	public void setCardinality(java.math.BigDecimal cardinality) {
		this.cardinality = cardinality;
	}

	public java.math.BigDecimal getBytes() {
		return this.bytes;
	}

	public void setBytes(java.math.BigDecimal bytes) {
		this.bytes = bytes;
	}

	public String getAccess_predicates() {
		return this.access_predicates;
	}

	public void setAccess_predicates(String access_predicates) {
		this.access_predicates = access_predicates;
	}

	public String getFilter_predicates() {
		return this.filter_predicates;
	}

	public void setFilter_predicates(String filter_predicates) {
		this.filter_predicates = filter_predicates;
	}

	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("[QueryVo:"); //$NON-NLS-1$
		buffer.append(" id: "); //$NON-NLS-1$
		buffer.append(id);
		buffer.append(" parent_id: "); //$NON-NLS-1$
		buffer.append(parent_id);
		buffer.append(" position: "); //$NON-NLS-1$
		buffer.append(position);
		buffer.append(" operation: "); //$NON-NLS-1$
		buffer.append(operation);
		buffer.append(" options: "); //$NON-NLS-1$
		buffer.append(options);
		buffer.append(" object_name: "); //$NON-NLS-1$
		buffer.append(object_name);
		buffer.append(" object_type: "); //$NON-NLS-1$
		buffer.append(object_type);
		buffer.append(" cost: "); //$NON-NLS-1$
		buffer.append(cost);
		buffer.append(" cardinality: "); //$NON-NLS-1$
		buffer.append(cardinality);
		buffer.append(" bytes: "); //$NON-NLS-1$
		buffer.append(bytes);
		buffer.append(" access_predicates: "); //$NON-NLS-1$
		buffer.append(access_predicates);
		buffer.append(" filter_predicates: "); //$NON-NLS-1$
		buffer.append(filter_predicates);
		buffer.append("]"); //$NON-NLS-1$
		return buffer.toString();
	}

}
