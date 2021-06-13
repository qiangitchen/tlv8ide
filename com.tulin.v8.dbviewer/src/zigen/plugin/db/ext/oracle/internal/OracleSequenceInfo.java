/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.ext.oracle.internal;

import java.io.Serializable;
import java.math.BigDecimal;

public class OracleSequenceInfo implements Serializable {

	private static final long serialVersionUID = 1L;

	private String sequece_owner;

	private String sequence_name;

	private BigDecimal min_value;

	private BigDecimal max_value;

	private BigDecimal increment_by;

	private String cycle_flg;

	private String order_flg;

	private BigDecimal cache_size;

	private BigDecimal last_number;

	public OracleSequenceInfo() {}

	public BigDecimal getCache_size() {
		return cache_size;
	}

	public void setCache_size(BigDecimal cache_size) {
		this.cache_size = cache_size;
	}

	public String getCycle_flg() {
		return cycle_flg;
	}

	public void setCycle_flg(String cycle_flg) {
		this.cycle_flg = cycle_flg;
	}

	public BigDecimal getIncrement_by() {
		return increment_by;
	}

	public void setIncrement_by(BigDecimal increment_by) {
		this.increment_by = increment_by;
	}

	public BigDecimal getLast_number() {
		return last_number;
	}

	public void setLast_number(BigDecimal last_number) {
		this.last_number = last_number;
	}

	public BigDecimal getMax_value() {
		return max_value;
	}

	public void setMax_value(BigDecimal max_value) {
		this.max_value = max_value;
	}

	public BigDecimal getMin_value() {
		return min_value;
	}

	public void setMin_value(BigDecimal min_value) {
		this.min_value = min_value;
	}

	public String getOrder_flg() {
		return order_flg;
	}

	public void setOrder_flg(String order_flg) {
		this.order_flg = order_flg;
	}

	public String getSequece_owner() {
		return sequece_owner;
	}

	public void setSequece_owner(String sequece_owner) {
		this.sequece_owner = sequece_owner;
	}

	public String getSequence_name() {
		return sequence_name;
	}

	public void setSequence_name(String sequence_name) {
		this.sequence_name = sequence_name;
	}

	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("[OracleSequence:"); //$NON-NLS-1$
		buffer.append(" sequece_owner: "); //$NON-NLS-1$
		buffer.append(sequece_owner);
		buffer.append(" sequence_name: "); //$NON-NLS-1$
		buffer.append(sequence_name);
		buffer.append(" min_value: "); //$NON-NLS-1$
		buffer.append(min_value);
		buffer.append(" max_value: "); //$NON-NLS-1$
		buffer.append(max_value);
		buffer.append(" increment_by: "); //$NON-NLS-1$
		buffer.append(increment_by);
		buffer.append(" cycle_flg: "); //$NON-NLS-1$
		buffer.append(cycle_flg);
		buffer.append(" order_flg: "); //$NON-NLS-1$
		buffer.append(order_flg);
		buffer.append(" cache_size: "); //$NON-NLS-1$
		buffer.append(cache_size);
		buffer.append(" last_number: "); //$NON-NLS-1$
		buffer.append(last_number);
		buffer.append("]"); //$NON-NLS-1$
		return buffer.toString();
	}

}
