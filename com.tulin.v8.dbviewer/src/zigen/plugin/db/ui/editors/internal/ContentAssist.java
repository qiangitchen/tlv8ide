/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.ui.editors.internal;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

import zigen.plugin.db.DbPlugin;
import zigen.plugin.db.ui.contentassist.ContentInfo;
import zigen.plugin.db.ui.internal.Column;

public class ContentAssist {

	private class TableContentProvider implements IStructuredContentProvider {

		Object[] contents;

		public Object[] getElements(Object inputElement) {
			return contents;
		}

		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
			if (newInput instanceof Object[]) {
				contents = (Object[]) newInput;
			} else {
				contents = null;
			}
		}

		public void dispose() {}

	}

	private class TableLabelProvider extends LabelProvider implements ITableLabelProvider {

		public String getColumnText(Object element, int columnIndex) {
			if (element instanceof Column) {
				Column col = (Column) element;
				return col.getName();
			}
			return null;
		}

		public Image getColumnImage(Object obj, int index) {
			return getImage(obj);
		}

		public Image getImage(Object obj) {
			if (obj instanceof Column) {
				Column col = (Column) obj;
				if (col.hasPrimaryKey()) {
					return DbPlugin.getDefault().getImage(DbPlugin.IMG_CODE_PK_COLUMN);
				} else {
					if (col.isNotNull()) {
						return DbPlugin.getDefault().getImage(DbPlugin.IMG_CODE_NOTNULL_COLUMN);
					} else {
						return DbPlugin.getDefault().getImage(DbPlugin.IMG_CODE_COLUMN);
					}
				}

			}
			return PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_OBJ_ELEMENT);

		}
	}

	protected TableContentProvider contentProvider = new TableContentProvider();

	private TableViewer tableViewer;

	private Rectangle rectangle;

	private Display display;

	private Combo comb;

	private zigen.plugin.db.ui.internal.ITable table;

	private Column[] columns;

	public ContentAssist(Combo text, zigen.plugin.db.ui.internal.ITable table) {
		this.comb = text;
		this.rectangle = getRectangle(text);
		this.display = DbPlugin.getDefault().getShell().getDisplay();
		this.table = table;
		ContentInfo ci = new ContentInfo(table.getDbConfig());
		columns = ci.getColumns(table.getName());
		createComposite();
	}

	private Rectangle getRectangle(Combo text) {
		Rectangle rectangle = new Rectangle(text.getBorderWidth(), 0, 0, 0);
		Composite parent = text.getParent();
		while (parent != null) {
			if (parent instanceof Composite) {
				rectangle.x += parent.getBounds().x;
				rectangle.y += parent.getBounds().y;
				parent = parent.getParent();
			}
		}
		rectangle.x += text.getBounds().x;
		rectangle.y += text.getBounds().y + 0 + 60;
		return rectangle;

	}

	private Rectangle getRectangle(Text text) {
		Rectangle rectangle = new Rectangle(text.getBorderWidth(), text.getLineHeight(), 0, 0);
		Composite parent = text.getParent();
		while (parent != null) {
			if (parent instanceof Composite) {
				rectangle.x += parent.getBounds().x;
				rectangle.y += parent.getBounds().y;
				parent = parent.getParent();
			}
		}
		rectangle.x += text.getBounds().x + text.getCaretLocation().x;
		rectangle.y += text.getBounds().y + text.getLineHeight();
		return rectangle;

	}

	protected void createComposite() {
		final Shell composite = new Shell(display, SWT.ON_TOP);
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 1;
		gridLayout.makeColumnsEqualWidth = false;
		gridLayout.marginHeight = 0;
		gridLayout.marginWidth = 0;
		gridLayout.horizontalSpacing = 0;
		gridLayout.verticalSpacing = 0;
		composite.setLayout(gridLayout);

		tableViewer = new TableViewer(composite, SWT.H_SCROLL | SWT.V_SCROLL | SWT.SINGLE);
		final Table table = tableViewer.getTable();
		TableLayout tableLayout = new TableLayout();
		table.setLayout(tableLayout);
		table.setHeaderVisible(false);
		table.setLinesVisible(false);
		// table.setFont(font);
		GridData gridData = new GridData(GridData.FILL_BOTH);
		tableViewer.getControl().setLayoutData(gridData);

		tableViewer.setContentProvider(contentProvider);
		tableViewer.setLabelProvider(new TableLabelProvider());
		tableViewer.setInput(columns);

		table.addFocusListener(new FocusAdapter() {

			public void focusGained(FocusEvent e) {
				if (table.getSelectionIndex() == -1) {
					table.select(0);
				}
			}
		});
		table.addKeyListener(new KeyAdapter() {

			public void keyPressed(KeyEvent e) {
				if (e.character == SWT.CR) {
					TableItem item = table.getItem(table.getSelectionIndex());
					comb.setText(item.getText());

					e.doit = false;
					composite.close();
				}
			}
		});
		// composite.pack();
		composite.setSize(300, 100);
		composite.setLocation(rectangle.x, rectangle.y);
		composite.open();
		composite.forceActive();
		composite.addShellListener(new ShellAdapter() {

			public void shellDeactivated(ShellEvent e) {
				((Shell) e.getSource()).dispose();

			}
		});

	}
}
