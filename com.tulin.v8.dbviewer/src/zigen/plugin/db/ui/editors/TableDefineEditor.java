/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.ui.editors;

import java.sql.Connection;
import java.util.Iterator;
import java.util.List;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.FontMetrics;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;

import zigen.plugin.db.DbPlugin;
import zigen.plugin.db.ImageCacher;
import zigen.plugin.db.core.DBType;
import zigen.plugin.db.core.TableConstraintColumn;
import zigen.plugin.db.core.TableFKColumn;
import zigen.plugin.db.core.TableIDXColumn;
import zigen.plugin.db.core.Transaction;
import zigen.plugin.db.core.rule.AbstractSQLCreatorFactory;
import zigen.plugin.db.core.rule.ISQLCreatorFactory;
import zigen.plugin.db.ext.oracle.internal.OracleSourceInfo;
import zigen.plugin.db.ext.oracle.internal.OracleSourceSearcher;
import zigen.plugin.db.ext.oracle.internal.OracleTriggerInfo;
import zigen.plugin.db.ext.oracle.internal.OracleTriggerSearcher;
import zigen.plugin.db.ui.editors.event.TextSelectionListener;
import zigen.plugin.db.ui.editors.internal.Messages;
import zigen.plugin.db.ui.editors.internal.wizard.ColumnWizard;
import zigen.plugin.db.ui.internal.Column;
import zigen.plugin.db.ui.internal.Constraint;
import zigen.plugin.db.ui.internal.ConstraintRoot;
import zigen.plugin.db.ui.internal.ITable;
import zigen.plugin.db.ui.internal.Index;
import zigen.plugin.db.ui.internal.IndexRoot;
import zigen.plugin.db.ui.internal.Root;
import zigen.plugin.db.ui.internal.TreeLeaf;
import zigen.plugin.db.ui.internal.TreeNode;
import zigen.plugin.db.ui.internal.Trigger;
import zigen.plugin.db.ui.internal.TriggerRoot;
import zigen.plugin.db.ui.views.TreeViewSorter;

public class TableDefineEditor {

	private List results;

	private String orgTableName;

	private String orgTableComment;

	private Text txtTableName;

	private Text txtTableComment;

	private TableViewer defineViewer;

	private Table table;

	ISQLCreatorFactory factory;

	Composite parent;

	ITable tableNode;

	TableViewEditorFor31 editor;

	private TreeViewer constraintViewer;

	private SashForm sash;

	private boolean focusDefine;

	private ImageCacher ic = ImageCacher.getInstance();

	// private ChangeColorJobDefine changeColorJobDefine;

	public TableDefineEditor(Composite parent, ITable tableNode) {
		this.parent = parent;
		setTable(tableNode);
	}

	private void setTable(ITable tableNode) {
		this.tableNode = tableNode;
		this.factory = AbstractSQLCreatorFactory.getFactory(tableNode.getDbConfig(), tableNode);
	}

	public void setEditor(TableViewEditorFor31 editor) {
		this.editor = editor;
	}

	public void updateWidget() {
		if (tableNode != null) {
			table.setVisible(false);
			constraintViewer.getTree().setVisible(false);

			this.txtTableName.setText(tableNode.getName());
			this.txtTableComment.setText(tableNode.getRemarks());
			this.orgTableName = tableNode.getName();
			this.orgTableComment = tableNode.getRemarks();

			defineViewer.setInput(tableNode.getColumns());
			defineViewer.refresh();
			columnsPack(table);

			Root root = new Root("root", true); //$NON-NLS-1$
			ConstraintRoot constraintRoot = new ConstraintRoot();
			IndexRoot indexRoot = new IndexRoot();
			root.addChild(constraintRoot);
			root.addChild(indexRoot);

			Constraint pk = null;
			if (tableNode.getTablePKColumns() != null && tableNode.getTablePKColumns().length > 0) {
				pk = new Constraint(tableNode.getTablePKColumns());
				constraintRoot.addChild(pk);
			}

			List fkList = factory.convertTableFKColumn(tableNode.getTableFKColumns());
			if (fkList != null) {
				for (Iterator iter = fkList.iterator(); iter.hasNext();) {
					TableFKColumn[] _fks = (TableFKColumn[]) iter.next();
					Constraint fk = new Constraint(_fks);
					constraintRoot.addChild(fk);
				}
			}

			List otherConstraintList = factory.convertTableConstraintColumn(tableNode.getTableConstraintColumns());
			if (otherConstraintList != null) {
				for (Iterator iter = otherConstraintList.iterator(); iter.hasNext();) {
					TableConstraintColumn[] _cons = (TableConstraintColumn[]) iter.next();
					Constraint cons = new Constraint(_cons);
					constraintRoot.addChild(cons);
				}
			}

			List uniqueindexList = factory.convertTableIDXColumn(tableNode.getTableUIDXColumns());
			if (uniqueindexList != null) {
				for (Iterator iter = uniqueindexList.iterator(); iter.hasNext();) {
					TableIDXColumn[] _idx = (TableIDXColumn[]) iter.next();
					Index index = new Index(_idx);
					if (index != null && pk != null && index.getName().equals(pk.getName())) {
						;
					} else {
						indexRoot.addChild(index);
					}
				}
			}
			List nonuniqueindexList = factory.convertTableIDXColumn(tableNode.getTableNonUIDXColumns());
			if (nonuniqueindexList != null) {
				for (Iterator iter = nonuniqueindexList.iterator(); iter.hasNext();) {
					TableIDXColumn[] _idx = (TableIDXColumn[]) iter.next();
					Index index = new Index(_idx);
					if (index != null && pk != null && index.getName().equals(pk.getName())) {
						;
					} else {
						TreeLeaf leaf = indexRoot.getChild(index.getName());
						if (leaf == null) {
							indexRoot.addChild(index);
						}
					}
				}
			}
			
			// for add Display TRIGGER
			if(DBType.getType(tableNode.getDbConfig()) == DBType.DB_TYPE_ORACLE){
				try {
					Connection con = Transaction.getInstance(tableNode.getDbConfig()).getConnection();
					String owner = tableNode.getSchemaName();
					String tableName = tableNode.getName();
					OracleTriggerInfo[] infos = OracleTriggerSearcher.execute(con, owner, tableName);
					if(infos != null && infos.length > 0){
						TriggerRoot triggerRoot = new TriggerRoot();
						for (int i = 0; i < infos.length; i++) {
							OracleTriggerInfo info = infos[i];
							Trigger trigger = new Trigger(tableNode, info);
//							String name;
//							if(owner.equalsIgnoreCase(info.getOwner())){
//								name = info.getName();
//							}else{
//								name = info.getOwner() + "." + info.getName();
//							}
							triggerRoot.addChild(trigger);
						}
						root.addChild(triggerRoot);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			
			constraintViewer.setInput(root);
			constraintViewer.expandAll();
			columnsPack(constraintViewer.getTree());
			constraintViewer.reveal(constraintRoot);

			TreeColumn col = constraintViewer.getTree().getColumns()[0];
			constraintViewer.getTree().showColumn(col);
			table.setVisible(true);
			constraintViewer.getTree().setVisible(true);

		}
	}

	public void createWidget() {
		Composite composite = new Composite(parent, SWT.BORDER);
		GridLayout layout = new GridLayout(2, false);
		composite.setLayout(layout);
		composite.setLayoutData(new GridData(GridData.FILL_BOTH));

		Label label = new Label(composite, SWT.NULL);
		label.setText(Messages.getString("TableDefineEditor.1")); //$NON-NLS-1$
		txtTableName = new Text(composite, SWT.BORDER);
		txtTableName.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		txtTableName.addFocusListener(new TextSelectionListener());
		txtTableName.addModifyListener(new ModifyListener() {

			public void modifyText(ModifyEvent e) {
				if (editor != null)
					editor.setDirty(true);
			}
		});

		Label label2 = new Label(composite, SWT.NULL);
		label2.setText(Messages.getString("TableDefineEditor.2")); //$NON-NLS-1$
		txtTableComment = new Text(composite, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL);

		// txtTableComment.setLayoutData(new
		// GridData(GridData.FILL_HORIZONTAL));

		GridData gridData2 = new GridData();
		gridData2.horizontalAlignment = GridData.FILL;
		// gridData2.verticalAlignment = GridData.FILL;

		GC gc = new GC(txtTableComment);
		gc.setFont(txtTableComment.getFont());
		FontMetrics fm = gc.getFontMetrics();
		// widthHint = fm.getAverageCharWidth() * charWidth;
		// heightHint = fm.getHeight();

		gridData2.heightHint = fm.getHeight() * 3;
		// gridData2.grabExcessHorizontalSpace = true;
		// gridData2.grabExcessVerticalSpace = true;
		txtTableComment.setLayoutData(gridData2);

		if (factory.supportsRemarks()) {
			txtTableComment.addFocusListener(new TextSelectionListener());
			txtTableComment.addModifyListener(new ModifyListener() {

				public void modifyText(ModifyEvent e) {
					if (editor != null)
						editor.setDirty(true);
				}
			});
		} else {
			txtTableComment.setEnabled(false);
		}

		GridData gd = new GridData(GridData.FILL_BOTH);
		gd.horizontalSpan = 2;

		sash = new SashForm(composite, SWT.VERTICAL | SWT.NONE);
		// sash = new SashForm(composite, SWT.HORIZONTAL | SWT.NONE);
		sash.setLayoutData(gd);
		createTableDefineWidget(sash);
		createConstraintWidget(sash);
		sash.setWeights(new int[] {65, 35});

	}

	public void createConstraintWidget(Composite comp) {
		constraintViewer = new TreeViewer(comp, SWT.SINGLE | SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER | SWT.FULL_SELECTION);
		GridData gd = new GridData(GridData.FILL_BOTH);
		gd.minimumHeight = 10;
		constraintViewer.getControl().setLayoutData(gd);
		final Tree tree = constraintViewer.getTree();
		tree.setFont(DbPlugin.getDefaultFont());
		tree.setHeaderVisible(true);
		// tree.setHeaderVisible(false);
		tree.addFocusListener(new FocusAdapter() {

			public void focusGained(FocusEvent e) {
				setFocusDefine(false);
			}

			public void focusLost(FocusEvent e) {
				tree.deselectAll();
			}
		});

		tree.setLinesVisible(true);
		setHeaderColumn2(tree);

		constraintViewer.setLabelProvider(new ConstraintAndIndexLabelProvider());
		constraintViewer.setContentProvider(new ConstraintAndIndexContentProvider());

		tree.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent e) {
				table.deselectAll();
			}
		});
	}

	private void setHeaderColumn2(Tree tree) {
		TreeColumn col = new TreeColumn(tree, SWT.LEFT);
		col.setText(Messages.getString("TableDefineEditor.3")); //$NON-NLS-1$
		col.setWidth(150);
		col.setResizable(true);

		TreeColumn col2 = new TreeColumn(tree, SWT.LEFT);
		col2.setText(Messages.getString("TableDefineEditor.4")); //$NON-NLS-1$
		col2.setWidth(150);
		col2.setResizable(true);

		TreeColumn col3 = new TreeColumn(tree, SWT.LEFT);
		col3.setText(Messages.getString("TableDefineEditor.5")); //$NON-NLS-1$
		col3.setWidth(500);
		col3.setResizable(true);

		TreeColumn col4 = new TreeColumn(tree, SWT.CENTER);
		col4.setText(Messages.getString("TableDefineEditor.6")); //$NON-NLS-1$
		col4.setWidth(500);
		col4.setResizable(true);
	}

	public void createTableDefineWidget(Composite comp) {
		defineViewer = new TableViewer(comp, SWT.BORDER | SWT.SINGLE | SWT.FULL_SELECTION);
		GridData gd = new GridData(GridData.FILL_BOTH);
		table = defineViewer.getTable();
		table.setLayoutData(gd);
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		table.setFont(DbPlugin.getDefaultFont());
		table.addFocusListener(new FocusAdapter() {

			public void focusGained(FocusEvent e) {
				setFocusDefine(true);
			}

			public void focusLost(FocusEvent e) {
				table.deselectAll();
			}
		});

		defineViewer.setContentProvider(new ColumnContentProvider());
		defineViewer.setLabelProvider(new ColumnLabelProvider());
		defineViewer.setSorter(new TreeViewSorter());
		setHeaderColumn(table);
		defineViewer.addDoubleClickListener(new IDoubleClickListener() {

			public void doubleClick(DoubleClickEvent event) {

				switch (DBType.getType(tableNode.getDbConfig())) {

				case DBType.DB_TYPE_ORACLE:
				case DBType.DB_TYPE_MYSQL:
				case DBType.DB_TYPE_POSTGRESQL:
				case DBType.DB_TYPE_DERBY:
				case DBType.DB_TYPE_H2:
				case DBType.DB_TYPE_HSQLDB:

					int editIndex = table.getSelectionIndex();
					if (editIndex >= 0) {
						TableItem tableItem = table.getItem(editIndex);
						Column col = (Column) tableItem.getData();
						Shell shell = DbPlugin.getDefault().getShell();
						ColumnWizard wizard = new ColumnWizard(factory, tableNode, col, false);
						WizardDialog dialog2 = new WizardDialog(shell, wizard);
						int ret = dialog2.open();
						if (ret == IDialogConstants.OK_ID) {
						}

					}
					break;

				case DBType.DB_TYPE_DB2:
				default:
					DbPlugin.getDefault().showWarningMessage(Messages.getString("TableDefineEditor.7")); //$NON-NLS-1$
					break;
				}

			}
		});

	}

	private void columnsPack(Tree tree) {
		tree.setVisible(false);
		TreeColumn[] cols = tree.getColumns();
		for (int i = 0; i < cols.length; i++) {
			cols[i].pack();
			cols[i].setWidth(cols[i].getWidth() + 10);
		}
		tree.setVisible(true);
	}

	private void setHeaderColumn(Table table) {
		TableColumn dummy = new TableColumn(table, SWT.NONE);
		dummy.setWidth(0);
		dummy.setResizable(false);
		TableColumn col1 = new TableColumn(table, SWT.NONE);
		col1.setText(Messages.getString("TableDefineEditor.8")); //$NON-NLS-1$
		col1.setWidth(150);
		TableColumn col2 = new TableColumn(table, SWT.NONE);
		col2.setText(Messages.getString("TableDefineEditor.9")); //$NON-NLS-1$
		col2.setWidth(120);
		TableColumn col3 = new TableColumn(table, SWT.CENTER);
		col3.setText(Messages.getString("TableDefineEditor.10")); //$NON-NLS-1$
		col3.setWidth(50);
		TableColumn col4 = new TableColumn(table, SWT.NONE);
		col4.setText(Messages.getString("TableDefineEditor.11")); //$NON-NLS-1$
		col4.setWidth(100);
		TableColumn col5 = new TableColumn(table, SWT.NONE);
		col5.setText(Messages.getString("TableDefineEditor.12")); //$NON-NLS-1$
		col5.setWidth(200);
		// col5.pack();
	}

	private void columnsPack(Table table) {
		table.setVisible(false);
		TableColumn[] cols = table.getColumns();
		// 0:dummy, 1:name, 2:type, 3:not null, 4:default, 5:remarks
		cols[1].pack();
		cols[2].pack();
		cols[4].pack();
		cols[5].pack();
		table.setVisible(true);
	}

	public void setFocus() {
		if (table != null) {
			table.setFocus();
		}
	}

	// public boolean changedTableName() {
	// return !(txtTableName.getText().equals(orgTableName));
	// }
	//
	// public boolean changedTableComment() {
	// return !(txtTableComment.getText().equals(orgTableComment));
	// }

	public String getTableName() {
		return txtTableName.getText();
	}

	public String getTableComment() {
		return txtTableComment.getText();
	}

	public void setOrgTableName(String tableName) {
		this.orgTableName = tableName;
	}

	public void setOrgTableComment(String tableComment) {
		this.orgTableComment = tableComment;
	}

	private class ColumnLabelProvider extends LabelProvider implements ITableLabelProvider {

		private ImageCacher imageCacher = ImageCacher.getInstance();

		public String getColumnText(Object element, int columnIndex) {
			String result = ""; //$NON-NLS-1$
			Column col = (Column) element;
			switch (columnIndex) {
			case 0:
				result = ""; // dummy column //$NON-NLS-1$
				break;
			case 1:
				result = col.getName().trim();
				break;
			case 2:
				StringBuffer typeStr = new StringBuffer();
				typeStr.append(col.getTypeName());

				// if (col.isVisibleColumnSize()){
				if (col.isVisibleColumnSize() && !col.getColumn().isWithoutParam()) {
					typeStr.append("(").append(col.getSize()).append(")"); //$NON-NLS-1$ //$NON-NLS-2$
				}
				// modify end
				result = typeStr.toString();
				break;
			case 3:
				result = ""; //$NON-NLS-1$
				break;
			case 4:
				result = col.getDefaultValue();
				break;
			case 5:
				result = col.getRemarks();
				break;
			default:
				break;
			}
			return result;
		}

		public Image getColumnImage(Object element, int columnIndex) {
			Column col = (Column) element;
			if (columnIndex == 3) {
				return getCheckImage(col.isNotNull());
			}
			return null;
		}

		private Image getCheckImage(boolean isSelected) {
			String key = isSelected ? DbPlugin.IMG_CODE_CHECKED_CENTER : DbPlugin.IMG_CODE_UNCHECKED_CENTER;
			return imageCacher.getImage(key);
		}

		public String getText(Object element) {
			return null;
		}

		public Image getImage(Object element) {
			return null;
		}
	}

	private class ColumnContentProvider implements IStructuredContentProvider {

		public Object[] getElements(Object inputElement) {
			if (inputElement instanceof Column[]) {
				return (Column[]) inputElement;
			}
			return null;
		}

		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {}

		public void dispose() {}

	}

	private class ConstraintAndIndexLabelProvider extends LabelProvider implements ITableLabelProvider {

		public String getColumnText(Object obj, int i) {
			try {
				if (obj instanceof ConstraintRoot) {
					ConstraintRoot root = (ConstraintRoot) obj;
					switch (i) {
					case 0:
						return root.getName();
					default:
						return ""; //$NON-NLS-1$
					}
				} else if (obj instanceof Constraint) {
					Constraint constraint = (Constraint) obj;
					switch (i) {
					case 0:
						return constraint.getName();
					case 1:
						return constraint.getType();
					case 2:
						return constraint.getParamater();

					default:
						break;
					}
				} else if (obj instanceof IndexRoot) {
					IndexRoot root = (IndexRoot) obj;
					switch (i) {
					case 0:
						return root.getName();
					default:
						return ""; //$NON-NLS-1$
					}
				} else if (obj instanceof Index) {
					Index idx = (Index) obj;
					switch (i) {
					case 0:
						return idx.getName();
					case 1:
						return idx.getType();
					case 2:
						return idx.getParamater();
					case 3:
						return idx.getIndexType();
					default:
						break;
					}
					
				} else if (obj instanceof TriggerRoot) {
					TriggerRoot root = (TriggerRoot) obj;
					switch (i) {
					case 0:
						return root.getName();
					default:
						return ""; //$NON-NLS-1$
					}
					
				} else if (obj instanceof Trigger) {
					Trigger tri = (Trigger) obj;
					switch (i) {
					case 0:
						return tri.getName();
					case 1:
						return tri.getType();
					case 2:
						return tri.getEvent();
					default:
						break;
					}
				}

			} catch (Exception e) {
				DbPlugin.log(e);
			}
			return null;

		}

		public Image getColumnImage(Object obj, int index) {
			if (index == 0) {
				return getImage(obj);
			} else {
				return null;
			}
		}

		public Image getImage(Object obj) {
			if (obj instanceof Constraint) {
				Constraint constraint = (Constraint) obj;
				if (constraint.getType().equals(Constraint.PRIMARY_KEY)) {
					return ic.getImage(DbPlugin.IMG_CODE_PK_COLUMN);
					// }else
					// constraint.getType().equals(Constraint.FOREGIN_KEY)){
					// return ic.getImage(DbPlugin.IMG_CODE_FK_COLUMN);
				} else {
					return null;
				}
			} else {
				return null;
			}
		}
	}

	private class ConstraintAndIndexContentProvider implements ITreeContentProvider {

		public Object[] getChildren(Object parentElement) {
			if (parentElement instanceof TreeNode) {
				TreeNode model = (TreeNode) parentElement;
				return model.getChildrens();
			} else {
				return null;
			}
		}

		public Object getParent(Object element) {
			if (element instanceof TreeNode) {
				TreeNode model = (TreeNode) element;
				return model.getParent();
			} else {
				return null;
			}
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

	public TreeViewer getConstraintViewer() {
		return constraintViewer;
	}

	public TableViewer getDefineViewer() {
		return defineViewer;
	}

	public boolean focusDefine() {
		return focusDefine;
	}

	public void setFocusDefine(boolean b) {
		this.focusDefine = b;
	}

}
