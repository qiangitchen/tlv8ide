/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.ext.oracle.internal;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ExplainTreeModel implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	private ExplainTreeModel parent;

	private List childrenList = new ArrayList();

	private ExplainVo vo;

	public ExplainTreeModel(ExplainVo vo) {
		this.vo = vo;
	}

	public boolean addEntry(ExplainTreeModel entry) {
		boolean isEntry = false;
		int parentId = entry.getParent_id();

		if (parentId == getId()) {
			childrenList.add(entry);
			entry.setParent(this);
			isEntry = true;
		} else {
			for (int i = 0; i < childrenList.size(); i++) {
				ExplainTreeModel _entry = (ExplainTreeModel) childrenList.get(i);
				if (_entry.addEntry(entry)) {
					isEntry = true;
					break;
				}
			}
		}

		return isEntry;
	}

	public ExplainTreeModel[] getChildren() {
		return (ExplainTreeModel[]) childrenList.toArray(new ExplainTreeModel[0]);
	}

	public ExplainTreeModel getParent() {
		return parent;
	}

	public void setParent(ExplainTreeModel parent) {
		this.parent = parent;
	}

	public String getAccess_predicates() {
		return vo.getAccess_predicates();
	}

	public int getBytes() {
		return (vo.getCardinality() == null) ? -1 : vo.getCardinality().intValue();
	}

	public int getCardinality() {
		return (vo.getCardinality() == null) ? -1 : vo.getCardinality().intValue();

	}

	public BigDecimal getCost() {
		return vo.getCost();
	}

	public String getFilter_predicates() {
		return vo.getFilter_predicates();
	}

	public int getId() {
		return (vo.getId() == null) ? -1 : vo.getId().intValue();
	}

	public String getObject_name() {
		return (vo.getObject_name() == null) ? "" : vo.getObject_name(); //$NON-NLS-1$
	}

	public String getObject_type() {
		return (vo.getObject_type() == null) ? "" : vo.getObject_type(); //$NON-NLS-1$
	}

	public String getOperation() {
		return (vo.getOperation() == null) ? "" : vo.getOperation(); //$NON-NLS-1$
	}

	public String getOptions() {
		return (vo.getOptions() == null) ? "" : vo.getOptions(); //$NON-NLS-1$
	}

	public int getParent_id() {
		return (vo.getParent_id() == null) ? -1 : vo.getParent_id().intValue();
	}

	public int getPosition() {
		return (vo.getPosition() == null) ? -1 : vo.getPosition().intValue();
	}

}
