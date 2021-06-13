/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.ext.oracle.internal;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;

import zigen.plugin.db.DbPlugin;
import zigen.plugin.db.DbPluginConstant;
import zigen.plugin.db.ImageCacher;
import zigen.plugin.db.core.ClipboardUtils;
import zigen.plugin.db.core.StringUtil;

public class ExplainResultDialog extends Dialog {

	ImageCacher ic = ImageCacher.getInstance();

	public static final int ID_COPY = -100;

	private ExplainTreeModel model;

	private TreeViewer result;

	private static final String[] HEADER = {"RESULT", "ACCESS_PREDICATES", "FILTER_PREDICATES"}; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

	public ExplainResultDialog(Shell parent, ExplainTreeModel model) {
		super(parent);
		super.setDefaultImage(ImageCacher.getInstance().getImage(DbPlugin.IMG_CODE_DB));
		setShellStyle(getShellStyle() | SWT.MAX | SWT.RESIZE);
		this.model = model;

	}

	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText(Messages.getString("ExplainResultDialog.3")); //$NON-NLS-1$
	}

	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, ID_COPY, Messages.getString("ExplainResultDialog.2"), false); //$NON-NLS-1$
		// createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
		createButton(parent, IDialogConstants.OK_ID, Messages.getString("ExplainResultDialog.1"), true); //$NON-NLS-1$

	}

	public boolean close() {
		return super.close();
	}

	protected void buttonPressed(int buttonId) {
		if (buttonId == ID_COPY) {
			StringBuffer sb = new StringBuffer();
			Clipboard clipboard = ClipboardUtils.getInstance();

			ITableLabelProvider provider = (ITableLabelProvider) result.getLabelProvider();
			copyResult(sb, provider, model.getChildren(), 0);

			clipboard.setContents(new Object[] {sb.toString()}, new Transfer[] {TextTransfer.getInstance()});
		} else {
			super.buttonPressed(buttonId);
		}
	}

	protected Control createDialogArea(Composite parent) {
		Composite composite = (Composite) super.createDialogArea(parent);

		composite.setLayout(new GridLayout(1, false));
		composite.setLayoutData(new GridData(GridData.FILL_BOTH));

		result = new TreeViewer(composite, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER | SWT.FULL_SELECTION);

		result.getControl().setLayoutData(new GridData(GridData.FILL_BOTH));

		Tree tree = result.getTree();
		// FONT
		tree.setFont(DbPlugin.getDefaultFont());

		tree.setHeaderVisible(true);
		tree.setLinesVisible(true);

		setHeaderColumn(tree);

		result.setLabelProvider(new ColumnSelectLabelProvider());
		result.setContentProvider(new ColumnSelectContentProvider());

		result.setInput(model);

		result.expandAll();

		columnsPack(tree);

		return composite;
	}

	private void copyResult(StringBuffer sb, ITableLabelProvider provider, ExplainTreeModel[] nodes, int depth) {
		if (nodes == null || nodes.length == 0)
			return;

		for (int i = 0; i < nodes.length; i++) {
			ExplainTreeModel model = nodes[i];

			if (depth > 0) {
				String indent = StringUtil.padding(" ", depth * 2); //$NON-NLS-1$
				sb.append(indent);
			}

			sb.append(provider.getColumnText(model, 0));
			sb.append("\t"); //$NON-NLS-1$

			String str = provider.getColumnText(model, 1);
			if (str != null) {
				sb.append(str);
			}
			sb.append("\t"); //$NON-NLS-1$

			str = provider.getColumnText(model, 2);
			if (str != null) {
				sb.append(str);
			}
			sb.append(DbPluginConstant.LINE_SEP);

			copyResult(sb, provider, model.getChildren(), depth + 1);
		}
	}

	private void setHeaderColumn(Tree tree) {

		for (int i = 0; i < HEADER.length; i++) {
			TreeColumn col = new TreeColumn(tree, SWT.LEFT);
			col.setText(HEADER[i]);
			col.pack();
		}
	}

	private void columnsPack(Tree tree) {
		tree.setVisible(false);
		TreeColumn[] cols = tree.getColumns();
		for (int i = 0; i < cols.length; i++) {
			cols[i].pack();
		}
		tree.setVisible(true);
	}

	private class ColumnSelectLabelProvider extends LabelProvider implements ITableLabelProvider {

		public String getColumnText(Object obj, int index) {
			try {

				if (obj instanceof ExplainTreeModel) {
					ExplainTreeModel model = (ExplainTreeModel) obj;

					switch (index) {
					case 0:
						return getResult(model);
					case 1:
						return model.getAccess_predicates();
					case 2:
						return model.getFilter_predicates();

					default:
						break;
					}
				} else {
					throw new RuntimeException("Unknown Type"); //$NON-NLS-1$
				}

			} catch (Exception e) {
				DbPlugin.log(e);
			}
			return null;

		}

		public Image getColumnImage(Object obj, int index) {
			// if (obj instanceof ExplainTreeModel) {
			// ExplainTreeModel model = (ExplainTreeModel) obj;
			// return ic.getImage(DbPlugin.IMG_CODE_WARNING);
			// }
			return getImage(obj);
		}

		public Image getImage(Object obj) {
			return null;
		}

		private String getResult(ExplainTreeModel model) {
			StringBuffer sb = new StringBuffer();

			if (model.getParent_id() < 0) {
				// sb.append(model.getId());

				sb.append(model.getOperation());
				sb.append(" "); //$NON-NLS-1$
				sb.append(model.getOptions());
				sb.append(" "); //$NON-NLS-1$
				sb.append(model.getObject_name());
				sb.append(" "); //$NON-NLS-1$
				sb.append(model.getObject_type());

				sb.append(" Cost = "); //$NON-NLS-1$
				sb.append(model.getPosition());

			} else {
				// sb.append(model.getParent_id());
				// sb.append("-");
				// sb.append(model.getId());
				sb.append(model.getOperation());
				sb.append(" "); //$NON-NLS-1$
				sb.append(model.getOptions());
				sb.append(" "); //$NON-NLS-1$
				sb.append(model.getObject_name());
				sb.append(" "); //$NON-NLS-1$
				sb.append(model.getObject_type());

			}

			return sb.toString();
		}

	}

	private class ColumnSelectContentProvider implements ITreeContentProvider {

		public Object[] getChildren(Object parentElement) {
			ExplainTreeModel model = (ExplainTreeModel) parentElement;
			return model.getChildren();
		}

		public Object getParent(Object element) {
			ExplainTreeModel model = (ExplainTreeModel) element;
			return model.getParent();

		}

		public boolean hasChildren(Object element) {
			return getChildren(element).length > 0;
		}

		public Object[] getElements(Object inputElement) {
			return getChildren(inputElement);
		}

		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {}

		public void dispose() {}

	}

	protected Point getInitialSize() {
		return new Point(800, 400);
	}

}
