package com.tulin.v8.ide.editors.data;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.text.ITextViewerExtension2;
import org.eclipse.jface.text.source.CompositeRuler;
import org.eclipse.jface.text.source.LineNumberRulerColumn;
import org.eclipse.jface.text.source.MatchingCharacterPainter;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;

import com.tulin.v8.core.DBUtils;
import com.tulin.v8.core.Sys;
import com.tulin.v8.core.TuLinPlugin;
import com.tulin.v8.core.utils.CommonUtil;
import com.tulin.v8.ide.StudioPlugin;
import com.tulin.v8.ide.editors.Messages;
import com.tulin.v8.ide.editors.data.dialog.TableColumnDialog;
import com.tulin.v8.ide.editors.data.dialog.UpdateSqlDialog;
import com.tulin.v8.ide.editors.data.job.RefreshColumnUnViewJob;
import com.tulin.v8.ide.views.navigator.IStatusChangeListener;

import zigen.plugin.db.DbPlugin;
import zigen.plugin.db.preference.SQLEditorPreferencePage;
import zigen.plugin.db.ui.editors.TableViewEditorFor31;
import zigen.plugin.db.ui.internal.ITable;
import zigen.plugin.db.ui.util.LineNumberRulerColumnUtil;
import zigen.plugin.db.ui.util.StyledTextUtil;
import zigen.plugin.db.ui.views.internal.DDLToolBar;
import zigen.plugin.db.ui.views.internal.SQLCharacterPairMatcher;
import zigen.plugin.db.ui.views.internal.SQLCodeConfiguration;
import zigen.plugin.db.ui.views.internal.SQLDocument;
import zigen.plugin.db.ui.views.internal.SQLSourceViewer;

/**
 * 表&视图编辑器
 * 
 * @author 陈乾
 *
 */
@SuppressWarnings("rawtypes")
public class DataEditor extends TableViewEditorFor31 implements IResourceChangeListener, IStatusChangeListener {
	public static String ID = "com.tulin.v8.ide.editors.data.DataEditor";
//	private StructuredTextEditor editor;
	private String tableName;
	private TableItem itemdata;
	private Table ColumnTable;
	private Text Table;
	private Text TableText;
	private Document TableDocument;
	private Map<String, String> sqlMap = new HashMap<String, String>();
	private ISelection selection;
	protected SQLSourceViewer ddlViewer; // DDL View
	private Button updatebtn;

	public DataEditor() {
		super();
		StudioPlugin.addStatusChangeListener(this);
	}

//	void createPage0() {
//		try {
//			editor = new StructuredTextEditor();
//			IEditorInput editorInput = getEditorInput();
//			int index;
//			if (editorInput instanceof TableViewEditorInput) {
//				TableViewEditorInput input = (TableViewEditorInput) editorInput;
//				tableName = input.getTable().getName();
//				index = addPage(editor, input);
//				setPageText(index, Messages.getString("TLEditor.pageEditor.1"));
//			}
//		} catch (PartInitException e) {
//			Sys.packErrMsg(e.toString());
//			ErrorDialog.openError(getSite().getShell(), "Error creating nested text editor", null, e.getStatus());
//			e.printStackTrace();
//		}
//	}

	/**
	 * Creates page 1 设计视图.
	 */
	void createPage1() {
		IEditorInput editorInput = getEditorInput();
		if (editorInput instanceof TableViewEditorInput) {
			TableViewEditorInput input = (TableViewEditorInput) editorInput;
			tableName = input.getTable().getName();
			setPartName(input.getName());
		}
		SashForm sashForm = new SashForm(getContainer(), SWT.NONE);
		FormData formData = new FormData();
		formData.left = new FormAttachment(0, 3);
		formData.right = new FormAttachment(100, -3);
		formData.top = new FormAttachment(0, 0);
		formData.bottom = new FormAttachment(100, -25);
		sashForm.setLayoutData(formData);

		Table ptable = new Table(sashForm, SWT.BORDER | SWT.V_SCROLL | SWT.FULL_SELECTION);
		ptable.setLayoutData(new GridData(GridData.FILL_BOTH));
		ptable.setHeaderVisible(true);
		ptable.setLinesVisible(true);
		TableColumn tablecolumn1 = new TableColumn(ptable, SWT.NONE);
		tablecolumn1.setWidth(200);
		tablecolumn1.setText(Messages.getString("TLEditor.dataEditor.1"));
		itemdata = new TableItem(ptable, SWT.NONE);
		itemdata.setText(tableName);
		itemdata.setImage(TuLinPlugin.getIcon("table.gif"));

		Composite compos = new Composite(sashForm, SWT.FILL | SWT.BORDER);
		compos.setLayout(new GridLayout());

		Composite headCompos = new Composite(compos, SWT.FILL);
		GridData hdlay = new GridData();
		hdlay.grabExcessHorizontalSpace = true;
		headCompos.setLayoutData(hdlay);
		headCompos.setLayout(new GridLayout(3, false));
		Label namelabel = new Label(headCompos, SWT.NONE);
		namelabel.setText(Messages.getString("TLEditor.dataEditor.2"));
		GridData namelabellay = new GridData();
		// namelabellay.widthHint = 40;
		namelabel.setLayoutData(namelabellay);
		Table = new Text(headCompos, SWT.BORDER);
		GridData namelay = new GridData();
		namelay.widthHint = 160;
		Table.setLayoutData(namelay);
		Table.setText(tableName);
		Table.setData(TableDocument.getRootElement().element("item"));
		Table.setEditable(false);

		updatebtn = new Button(headCompos, SWT.BUTTON1);
		updatebtn.setText(Messages.getString("TLEditor.dataEditor.3"));
		updatebtn.setEnabled(false);
		updatebtn.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				UpdateSqlDialog dialog = new UpdateSqlDialog(getSite().getShell(), sqlMap);
				if (dialog.open() == IDialogConstants.OK_ID) {
					String cSql = dialog.getSqlText();
					String dbkey = ((Element) Table.getData()).attributeValue("dbkey");
					Connection conn = null;
					Statement stmt = null;
					String[] sqls = cSql.split(";");
					try {
						conn = DBUtils.getAppConn(dbkey);
						stmt = conn.createStatement();
						for (int j = 0; j < sqls.length; j++) {
							String sql = sqls[j];
							sql = sql.replace("\n", " ");
							sql = sql.replace("\r", " ");
							stmt.execute(sql);
						}
						MessageDialog.openInformation(getSite().getShell(),
								Messages.getString("TLEditor.message.title1"),
								Messages.getString("TLEditor.dataEditor.6"));
						updatebtn.setEnabled(false);
						sqlMap = new HashMap<String, String>();
					} catch (Exception e1) {
						Sys.packErrMsg(e1.toString());
					} finally {
						DBUtils.CloseConn(conn, stmt, null);
					}
				}
			}
		});

		Label textlabel = new Label(headCompos, SWT.NONE);
		textlabel.setText(Messages.getString("TLEditor.dataEditor.7"));
		GridData textlabellay = new GridData();
		// textlabellay.widthHint = 40;
		textlabel.setLayoutData(textlabellay);
		TableText = new Text(headCompos, SWT.BORDER);
		GridData textlay = new GridData();
		textlay.widthHint = 160;
		TableText.setLayoutData(textlay);
		TableText.setData(TableDocument.getRootElement().element("item"));
		TableText.setText(TableDocument.getRootElement().element("item").attributeValue("text"));
		TableText.addListener(SWT.FocusOut, new Listener() {
			@SuppressWarnings("deprecation")
			public void handleEvent(Event event) {
				Element el = (Element) TableText.getData();
				if (!TableText.getText().equals(el.attributeValue("text"))) {
					el.setAttributeValue("text", TableText.getText());
					setDataText();
					changedTableText(Table.getText(), TableText.getText());
				}
			}
		});
		TableText.setEnabled(!isView());// 视图不可编辑

		ToolBar toolbar = new ToolBar(compos, SWT.FLAT | SWT.WRAP | SWT.RIGHT);
		GridData toolbarcl = new GridData(SWT.FILL);
		toolbar.setLayoutData(toolbarcl);
		final ToolItem addbar = new ToolItem(toolbar, SWT.PUSH);
		addbar.setImage(TuLinPlugin.getIcon("addbtn.gif"));
		addbar.setText(Messages.getString("TLEditor.dataEditor.8"));
		final ToolItem removebar = new ToolItem(toolbar, SWT.PUSH);
		removebar.setImage(TuLinPlugin.getIcon("delbtn.gif"));
		removebar.setText(Messages.getString("TLEditor.dataEditor.9"));
		removebar.setEnabled(false);

		ColumnTable = new Table(compos, SWT.BORDER | SWT.V_SCROLL | SWT.FULL_SELECTION);
		ColumnTable.setLayoutData(new GridData(GridData.FILL_BOTH));
		ColumnTable.setHeaderVisible(true);
		ColumnTable.setLinesVisible(true);
		TableColumn Namecolumn = new TableColumn(ColumnTable, SWT.NONE);
		Namecolumn.setWidth(220);
		Namecolumn.setText(Messages.getString("TLEditor.dataEditor.10"));
		TableColumn Typecolumn = new TableColumn(ColumnTable, SWT.NONE);
		Typecolumn.setWidth(100);
		Typecolumn.setText(Messages.getString("TLEditor.dataEditor.11"));
		TableColumn Lengthcolumn = new TableColumn(ColumnTable, SWT.NONE);
		Lengthcolumn.setWidth(100);
		Lengthcolumn.setText(Messages.getString("TLEditor.dataEditor.12"));
		TableColumn Textcolumn = new TableColumn(ColumnTable, SWT.NONE);
		Textcolumn.setWidth(200);
		Textcolumn.setText(Messages.getString("TLEditor.dataEditor.13"));
		TableColumn DefTextcolumn = new TableColumn(ColumnTable, SWT.NONE);
		DefTextcolumn.setWidth(200);
		DefTextcolumn.setText(Messages.getString("TLEditor.dataEditor.15"));
		ColumnTable.setData(TableDocument.getRootElement());
		initColumn();

		final TableEditor editor = new TableEditor(ColumnTable);
		editor.horizontalAlignment = SWT.LEFT;
		editor.grabHorizontal = true;
		ColumnTable.addListener(SWT.MouseDown, new Listener() {
			public void handleEvent(Event event) {
				if (!isView()) {
					removebar.setEnabled(true);// 将删除按钮设置为可用
					Rectangle clientArea = ColumnTable.getClientArea();
					Point point = new Point(event.x, event.y);
					int index = ColumnTable.getTopIndex();
					while (index < ColumnTable.getItemCount()) {
						boolean visible = false;
						final TableItem item = ColumnTable.getItem(index);
						for (int i = 3; i < 4; i++) {
							Rectangle rectangle = item.getBounds(i);
							if (rectangle.contains(point)) {
								final int column = i;
								final Text text = new Text(ColumnTable, SWT.NONE);
								Listener textListener = new Listener() {
									@SuppressWarnings("deprecation")
									public void handleEvent(final Event event) {
										switch (event.type) {
										case SWT.FocusOut:
											item.setText(column, text.getText());
											Element el = (Element) item.getData();
											if (!text.getText().equals(el.attributeValue("text"))) {
												el.setAttributeValue("text", text.getText());
												setDataText();
												changedText(Table.getText(), item, text.getText());
											}
											text.dispose();
											break;
										case SWT.Traverse:
											switch (event.detail) {
											case SWT.TRAVERSE_RETURN:
												item.setText(column, text.getText());
												Element el1 = (Element) item.getData();
												if (!text.getText().equals(el1.attributeValue("text"))) {
													el1.setAttributeValue("text", text.getText());
													setDataText();
													changedText(Table.getText(), item, text.getText());
												}
											case SWT.TRAVERSE_ESCAPE:
												text.dispose();
												event.doit = false;
											}
											break;
										}
									}
								};
								text.addListener(SWT.FocusOut, textListener);
								text.addListener(SWT.Traverse, textListener);
								editor.setEditor(text, item, i);
								text.setText(item.getText(i));
								text.selectAll();
								text.setFocus();
								return;
							}
							if (!visible && rectangle.intersects(clientArea)) {
								visible = true;
							}
						}
						if (!visible)
							return;
						index++;
					}
				}
			}
		});

		// 双击
		ColumnTable.addListener(SWT.MouseDoubleClick, new Listener() {
			@SuppressWarnings("deprecation")
			public void handleEvent(Event event) {
				if (!isView()) {
					TableItem[] itemList = ColumnTable.getItems();
					int listHaveChouse = ColumnTable.getSelectionIndex();
					String dbkey = ((Element) Table.getData()).attributeValue("dbkey");
					String tableName = Table.getText();
					String columnName = itemList[listHaveChouse].getText(0);
					TableColumnDialog dialog = new TableColumnDialog(StudioPlugin.getShell(), dbkey, tableName,
							columnName, itemList[listHaveChouse]);
					int state = dialog.open();
					if (state == IDialogConstants.OK_ID) {
						Element element = (Element) itemList[listHaveChouse].getData();
						element.setAttributeValue("name", dialog.getColumnname());
						element.setAttributeValue("type", dialog.getColumntype());
						element.setAttributeValue("length", dialog.getColumntlength());
						element.setAttributeValue("text", dialog.getColumntcomment());
						element.setAttributeValue("def", dialog.getDefaultvalue());
						String[] itemdata = new String[5];
						itemdata[0] = element.attributeValue("name");
						itemdata[1] = element.attributeValue("type");
						itemdata[2] = element.attributeValue("length");
						itemdata[3] = element.attributeValue("text");
						itemdata[4] = element.attributeValue("def");
						itemList[listHaveChouse].setText(itemdata);
						setDataText();
					}
				}
			}
		});

		// 添加字段
		addbar.addSelectionListener(new SelectionAdapter() {
			@SuppressWarnings("deprecation")
			public void widgetSelected(SelectionEvent e) {
				String dbkey = ((Element) Table.getData()).attributeValue("dbkey");
				String tablename = Table.getText();
				TableColumnDialog dialog = new TableColumnDialog(StudioPlugin.getShell(), dbkey, tablename);
				int state = dialog.open();
				if (state == IDialogConstants.OK_ID) {
					Element root = (Element) Table.getData();
					Element ele = root.addElement("item");
					ele.setAttributeValue("name", dialog.getColumnname());
					ele.setAttributeValue("type", dialog.getColumntype());
					ele.setAttributeValue("length", dialog.getColumntlength());
					ele.setAttributeValue("text", dialog.getColumntcomment());
					ele.setAttributeValue("def", dialog.getDefaultvalue());
					String[] itemdata = new String[5];
					itemdata[0] = ele.attributeValue("name");
					itemdata[1] = ele.attributeValue("type");
					itemdata[2] = ele.attributeValue("length");
					itemdata[3] = ele.attributeValue("text");
					itemdata[4] = ele.attributeValue("def");
					TableItem tableitem = new TableItem(ColumnTable, SWT.NONE);
					tableitem.setText(itemdata);
					tableitem.setData(ele);
					setDataText();
				}
			}
		});
		addbar.setEnabled(!isView());

		// 删除字段
		removebar.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				TableItem[] items = ColumnTable.getSelection();
				if (items.length > 0) {
					TableItem selectitem = items[0];
					boolean result = MessageDialog.openConfirm(getSite().getShell().getShell(),
							Messages.getString("TLEditor.dataEditor.removeAc.1"),
							Messages.getString("TLEditor.dataEditor.removeAc.2") + Table.getText()
									+ Messages.getString("TLEditor.dataEditor.removeAc.3") + selectitem.getText(0)
									+ "?");
					if (result) {
						String desql = "alter table " + Table.getText() + " drop column " + selectitem.getText(0) + ";";
						String dbkey = ((Element) Table.getData()).attributeValue("dbkey");
						Connection conn = null;
						Statement stmt = null;
						try {
							conn = DBUtils.getAppConn(dbkey);
							stmt = conn.createStatement();
							Sys.printMsg(desql);
							stmt.execute(desql);
							// 删除编辑器对应的内容
							Element element = (Element) selectitem.getData();
							element.getParent().remove(element);
							selectitem.dispose();
							setDataText();
							removebar.setEnabled(false);
						} catch (Exception e1) {
							Sys.packErrMsg(e1.toString());
						} finally {
							DBUtils.CloseConn(conn, stmt, null);
						}
					}
				}
			}
		});

		sashForm.setWeights(new int[] { 2, 8 });
		int index = addPage(sashForm);
		setPageText(index, Messages.getString("TLEditor.pageEditor.3"));
	}

	private void createDDLPage() {
		Composite composite = new Composite(getContainer(), SWT.NONE);
		FormLayout layout = new FormLayout();
		composite.setLayout(layout);

		toolBar = new DDLToolBar(composite, this);
		Composite sqlComposite = new Composite(composite, SWT.NONE);
		sqlComposite.setLayout(new FillLayout());

		FormData data = new FormData();
		data.top = new FormAttachment(toolBar.getCoolBar(), 0);
		data.left = new FormAttachment(0, 0);
		data.right = new FormAttachment(100, 0);
		data.bottom = new FormAttachment(100, 0);
		sqlComposite.setLayoutData(data);

		CompositeRuler ruler = new CompositeRuler();
		rulerCol = new LineNumberRulerColumn();
		ruler.addDecorator(0, rulerCol);

		LineNumberRulerColumnUtil.changeColor(colorManager, rulerCol);
		ddlViewer = new SQLSourceViewer(sqlComposite, ruler, null, false,
				SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);
		ddlViewer.setSqlFileName(tableName);
		toolBar.setSQLSourceViewer(ddlViewer);
		initializeViewerFont(ddlViewer);
		sqlConfiguration = new SQLCodeConfiguration(colorManager);
		ddlViewer.configure(sqlConfiguration);
		ddlViewer.setDocument(new SQLDocument());
		ITextViewerExtension2 extension = (ITextViewerExtension2) ddlViewer;
		painter = new MatchingCharacterPainter(ddlViewer, new SQLCharacterPairMatcher());
		painter.setColor(colorManager.getColor(SQLEditorPreferencePage.P_COLOR_MATCHING));
		extension.addPainter(painter);
		StyledTextUtil.changeColor(colorManager, ddlViewer.getTextWidget());
		ddlViewer.setEditable(false);
		int index = addPage(composite);
		setPageText(index, Messages.getString("TLEditor.dataEditor.14")); //$NON-NLS-1$
	}

	protected void createPages() {
//		createPage0();
		initTable();// 初始化文本
		createPage1();// 设计
		
		createDDLPage();// 建表语句

		//setActivePage(1);

		setSelection(StudioPlugin.getSelection());
	}

	public void dispose() {
		StudioPlugin.removeStatusChangeListener(this);
		try {
			super.dispose();
		} catch (Exception e) {

		}
	}

	public void doSave(IProgressMonitor monitor) {
		getEditor(0).doSave(monitor);
	}

	public void doSaveAs() {
		IEditorPart editor = getEditor(0);
		editor.doSaveAs();
		// setInput(editor.getEditorInput());
	}

	public boolean isSaveAsAllowed() {
		return true;
	}

	protected void pageChange(int newPageIndex) {
		super.pageChange(newPageIndex);
		// if (newPageIndex == 0) {
		// initTable();
		// initColumn();
		// }
		if (newPageIndex == 1) {
			setDDLString();
		}
	}

	protected void setDDLString() {
		try {
			if (tableNode instanceof ITable) {
				ITable table = (ITable) tableNode;
				table.setExpanded(true);
				RefreshColumnUnViewJob job = new RefreshColumnUnViewJob(this, table);
				job.setUser(false);
				job.schedule();
			}
		} catch (Exception e) {
			DbPlugin.log(e);
		}
	}

	public void resourceChanged(final IResourceChangeEvent event) {
		if (event.getType() == IResourceChangeEvent.PRE_CLOSE) {
			Display.getDefault().asyncExec(new Runnable() {
				public void run() {
				}
			});
		}
	}

//	public StructuredTextEditor getXMLEditor() {
//		return editor;
//	}

	private Document TxtToXmlDom(String txt) throws Exception {
		Document doc;
		doc = DocumentHelper.parseText(txt);
		return doc;
	}

	private void initTable() {
		String text = null;
		IEditorInput editorInput = getEditorInput();
		if (editorInput instanceof TableViewEditorInput) {
			TableViewEditorInput input = (TableViewEditorInput) editorInput;
			tableNode = input.getTable();
			text = input.getText();
		}
		try {
			TableDocument = TxtToXmlDom(text);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void initColumn() {
		List<?> list = TableDocument.getRootElement().element("item").elements();
		ColumnTable.removeAll();
		for (int i = 0; i < list.size(); i++) {
			Element item = (Element) list.get(i);
			String[] itemdata = new String[5];
			itemdata[0] = item.attributeValue("name");
			itemdata[1] = item.attributeValue("type");
			itemdata[2] = item.attributeValue("length");
			itemdata[3] = item.attributeValue("text");
			itemdata[4] = item.attributeValue("def");
			TableItem tableitem = new TableItem(ColumnTable, SWT.NONE);
			tableitem.setText(itemdata);
			tableitem.setData(item);
		}
	}

	private void setDataText() {
//		String domtext = XMLFormator.formatXML(TableDocument);
//		String text = editor.getDocumentProvider().getDocument(editor.getEditorInput()).get();
//		if (!domtext.equals(text)) {
//			editor.getDocumentProvider().getDocument(editor.getEditorInput()).set(domtext);
//		}
	}

	private void changedText(String tableName, TableItem item, String text) {
		String dbkey = ((Element) Table.getData()).attributeValue("dbkey");
		String columnName = item.getText();
		String upsql = "comment on column " + tableName + "." + columnName + " is '" + text + "';";
		if (DBUtils.IsMSSQLDB(dbkey)) {
			String sql = "select d.value as COMMENTS from sys.extended_properties d where "
					+ "d.major_id in(select a.id from dbo.syscolumns as a inner join "
					+ "dbo.sysobjects as b on b.id = a.id where d.minor_id = a.colid and b.name = '" + tableName
					+ "' and a.name = '" + columnName + "')";
			List list = new ArrayList();
			try {
				list = DBUtils.execQueryforList(dbkey, sql);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			if (list.size() > 0) {
				upsql = "EXECUTE sp_updateextendedproperty N'MS_Description', N'" + text
						+ "', 'USER', N'dbo', 'TABLE', N'" + tableName + "', 'COLUMN', N'" + columnName + "';";
			} else {
				upsql = "EXECUTE sp_addextendedproperty N'MS_Description', N'" + text + "', 'USER', N'dbo', 'TABLE', N'"
						+ tableName + "', 'COLUMN', N'" + columnName + "';";
			}
		} else if (DBUtils.IsMySQLDB(dbkey)) {
			Element element = (Element) item.getData();
			upsql = "alter table " + tableName + " modify column " + columnName + " "
					+ element.attributeValue("COLUMN_TYPE");
			if (item.getText(4) != null && !"".equals(item.getText(4))) {
				if (CommonUtil.needQuotation(item.getText(1))) {
					upsql += " default '" + item.getText(4) + "' ";
				} else {
					upsql += " default " + item.getText(4) + " ";
				}
			} else {
				upsql += " default null ";
			}
			upsql += " comment '" + text + "';";
		}
		sqlMap.put(tableName + "." + columnName, upsql);
		updatebtn.setEnabled(true);
	}

	private void changedTableText(String tableName, String text) {
		String dbkey = ((Element) Table.getData()).attributeValue("dbkey");
		String upsql = "COMMENT ON TABLE " + tableName + " IS '" + text + "';";
		if (DBUtils.IsMSSQLDB(dbkey)) {
			String sql = "select d.value as COMMENTS from sys.extended_properties d "
					+ "where d.major_id in(select a.object_id from sys.tables as a "
					+ "where minor_id = 0 and a.name = '" + tableName + "')";
			List list = new ArrayList();
			try {
				list = DBUtils.execQueryforList(dbkey, sql);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			if (list.size() > 0) {
				upsql = "EXECUTE sp_updateextendedproperty N'MS_Description', N'" + text
						+ "', 'USER', N'dbo', 'TABLE', N'" + tableName + "', null, null;";
			} else {
				upsql = "EXECUTE sp_addextendedproperty N'MS_Description', N'" + text + "', 'USER', N'dbo', 'TABLE', N'"
						+ tableName + "', null, null;";
			}
		} else if (DBUtils.IsMySQLDB(dbkey)) {
			upsql = "alter table " + tableName + " comment '" + text + "';";
		}
		sqlMap.put(tableName, upsql);
		updatebtn.setEnabled(true);
	}

	public boolean isView() {
		try {
			String ttype = TableDocument.getRootElement().element("item").attributeValue("type");
			return ttype != null && !"table".equals(ttype.toLowerCase());
		} catch (Exception e) {
		}
		return true;
	}

	public void setSelection(ISelection selection) {
		this.selection = selection;
	}

	public void setFocus() {
		StudioPlugin.fireStatusChangeListener(selection, IStatusChangeListener.EVT_LinkTable);
	}

	public SQLSourceViewer getDdlViewer() {
		return ddlViewer;
	}

	@Override
	public void statusChanged(Object obj, int status) {
		// TODO Auto-generated method stub

	}
}
