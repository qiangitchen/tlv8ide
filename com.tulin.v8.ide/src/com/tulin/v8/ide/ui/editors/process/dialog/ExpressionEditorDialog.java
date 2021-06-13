package com.tulin.v8.ide.ui.editors.process.dialog;

import java.util.ArrayList;
import java.util.List;

import org.dom4j.Element;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

import com.tulin.v8.ide.StudioPlugin;
import com.tulin.v8.ide.ui.editors.process.ExpressionTreeHelper;

@SuppressWarnings("unchecked")
public class ExpressionEditorDialog extends Dialog {
	private Tree fnTree;
	public TreeItem currentTreeItem;
	private Text expText;
	private String expretion = "";
	private Table paramtable;
	private Text helptext;

	public ExpressionEditorDialog(Shell parentShell, String expretion) {
		super(parentShell);
		if (expretion != null)
			this.expretion = expretion;
	}

	/**
	 * 在这个方法里构建Dialog中的界面内容
	 */
	protected Control createDialogArea(Composite parent) {
		getShell().setText(Messages.getString("TLEditor.ExpressionEdit.title"));
		Composite container = (Composite) super.createDialogArea(parent);
		container.setLayout(new FillLayout());
		SashForm sashForm = new SashForm(container, SWT.FILL | SWT.VERTICAL);

		SashForm sashtop = new SashForm(sashForm, SWT.FILL | SWT.HORIZONTAL);

		Group containertopl = new Group(sashtop, SWT.FILL);
		containertopl.setLayout(new FillLayout());
		containertopl.setText(Messages
				.getString("TLEditor.ExpressionEdit.fnlist"));
		fnTree = new Tree(containertopl, SWT.FILL | SWT.BORDER | SWT.V_SCROLL);
		loadData();
		fnTree.addListener(SWT.Expand, new Listener() {
			public void handleEvent(final Event event) {
				final TreeItem root = (TreeItem) event.item;
				root.setImage(StudioPlugin.getIcon("folder-open.gif"));
			}
		});
		fnTree.addListener(SWT.Collapse, new Listener() {
			public void handleEvent(final Event event) {
				final TreeItem root = (TreeItem) event.item;
				root.setImage(StudioPlugin.getIcon("folder.gif"));
			}
		});
		fnTree.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent Event) {
				currentTreeItem = (TreeItem) Event.item;
				if (currentTreeItem.getData() != null) {
					Element element = (Element) currentTreeItem.getData();
					removeAllEditor();
					paramtable.removeAll();
					if ("function".equals(element.getName())) {
						String helpStr = element.attributeValue("helper");
						helpStr = helpStr.replaceAll("<br>", "\n");
						helpStr = helpStr.replaceAll("<br/>", "\n");
						helptext.setText(helpStr);
						loadParamList(element, paramtable);
					} else {
						helptext.setText("");
					}
				}
			}
		});

		SashForm sashtopr = new SashForm(sashtop, SWT.FILL | SWT.VERTICAL);

		Group containerrtop = new Group(sashtopr, SWT.FILL);
		containerrtop.setLayout(new FillLayout());
		containerrtop.setText(Messages
				.getString("TLEditor.ExpressionEdit.params"));
		paramtable = new Table(containerrtop, SWT.FILL | SWT.BORDER
				| SWT.V_SCROLL | SWT.FULL_SELECTION);
		paramtable.setHeaderVisible(true);
		paramtable.setLinesVisible(true);
		TableColumn cplumn1 = new TableColumn(paramtable, SWT.NONE);
		cplumn1.setWidth(120);
		cplumn1.setText("Name");
		TableColumn cplumn2 = new TableColumn(paramtable, SWT.NONE);
		cplumn2.setWidth(280);
		cplumn2.setText("Value");

		Group containerrbot = new Group(sashtopr, SWT.FILL);
		containerrbot.setLayout(new FillLayout());
		containerrbot.setText(Messages
				.getString("TLEditor.ExpressionEdit.helps"));
		helptext = new Text(containerrbot, SWT.BORDER | SWT.LEFT
				| SWT.READ_ONLY | SWT.MULTI | SWT.V_SCROLL | SWT.H_SCROLL);

		sashtopr.setWeights(new int[] { 2, 3 });

		sashtop.setWeights(new int[] { 1, 2 });

		Composite sashbottom = new Composite(sashForm, SWT.FILL);
		sashbottom.setLayout(new GridLayout(2, false));

		ToolBar ltoolbar = new ToolBar(sashbottom, SWT.FLAT | SWT.WRAP
				| SWT.RIGHT | SWT.BAR);
		GridData laydata = new GridData(GridData.FILL_HORIZONTAL);
		laydata.grabExcessHorizontalSpace = true;
		ltoolbar.setLayoutData(laydata);
		ToolItem addItem = new ToolItem(ltoolbar, SWT.PUSH);
		addItem.setText(Messages.getString("TLEditor.ExpressionEdit.and"));
		addItem.setData("+");
		addItem.addSelectionListener(new QueckItemAdapter(addItem));
		ToolItem reduceItem = new ToolItem(ltoolbar, SWT.PUSH);
		reduceItem
				.setText(Messages.getString("TLEditor.ExpressionEdit.reduce"));
		reduceItem.setData("-");
		reduceItem.addSelectionListener(new QueckItemAdapter(reduceItem));
		ToolItem rideItem = new ToolItem(ltoolbar, SWT.PUSH);
		rideItem.setText(Messages.getString("TLEditor.ExpressionEdit.ride"));
		rideItem.setData("*");
		rideItem.addSelectionListener(new QueckItemAdapter(rideItem));
		ToolItem exceptItem = new ToolItem(ltoolbar, SWT.PUSH);
		exceptItem
				.setText(Messages.getString("TLEditor.ExpressionEdit.except"));
		exceptItem.setData("/");
		exceptItem.addSelectionListener(new QueckItemAdapter(exceptItem));
		ToolItem etcItem = new ToolItem(ltoolbar, SWT.PUSH);
		etcItem.setText(Messages.getString("TLEditor.ExpressionEdit.equal"));
		etcItem.setData("=");
		etcItem.addSelectionListener(new QueckItemAdapter(etcItem));

		new ToolItem(ltoolbar, SWT.SEPARATOR);

		ToolItem andItem = new ToolItem(ltoolbar, SWT.PUSH);
		andItem.setText("AND");
		andItem.setData("AND");
		andItem.addSelectionListener(new QueckItemAdapter(andItem));
		ToolItem orItem = new ToolItem(ltoolbar, SWT.PUSH);
		orItem.setText("OR");
		orItem.setData("OR");
		orItem.addSelectionListener(new QueckItemAdapter(orItem));

		new ToolItem(ltoolbar, SWT.SEPARATOR);

		ToolItem lcItem = new ToolItem(ltoolbar, SWT.PUSH);
		lcItem.setText("(");
		lcItem.setData("(");
		lcItem.addSelectionListener(new QueckItemAdapter(lcItem));
		ToolItem rcItem = new ToolItem(ltoolbar, SWT.PUSH);
		rcItem.setText(")");
		rcItem.setData(")");
		rcItem.addSelectionListener(new QueckItemAdapter(rcItem));
		ToolItem trueItem = new ToolItem(ltoolbar, SWT.PUSH);
		trueItem.setText("TRUE");
		trueItem.setData("TRUE");
		trueItem.addSelectionListener(new QueckItemAdapter(trueItem));
		ToolItem falseItem = new ToolItem(ltoolbar, SWT.PUSH);
		falseItem.setText("FALSE");
		falseItem.setData("FALSE");
		falseItem.addSelectionListener(new QueckItemAdapter(falseItem));

		new ToolItem(ltoolbar, SWT.SEPARATOR);

		ToolBar rtoolbar = new ToolBar(sashbottom, SWT.FLAT | SWT.WRAP
				| SWT.RIGHT | SWT.BAR);
		GridData rlaydata = new GridData(SWT.NONE);
		rtoolbar.setLayoutData(rlaydata);
		ToolItem addToolItem = new ToolItem(rtoolbar, SWT.PUSH);
		addToolItem.setText(Messages.getString("TLEditor.ExpressionEdit.add"));
		addToolItem.setToolTipText(Messages
				.getString("TLEditor.ExpressionEdit.add"));
		addToolItem.setImage(StudioPlugin.getIcon("addbtn.gif"));
		addToolItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (currentTreeItem == null) {
					MessageDialog.openInformation(getShell(),
							Messages.getString("TLEditor.message.title1"),
							Messages.getString("TLEditor.message.exppage"));
					return;
				} else {
					Element element = (Element) currentTreeItem.getData();
					String sExpression = element.attributeValue("id") + "(";
					TableItem[] paranarray = paramtable.getItems();
					for (int i = 0; i < paranarray.length; i++) {
						TableItem item = paranarray[i];
						sExpression += '"' + item.getText(1) + '"';
						if (i < paranarray.length - 1)
							sExpression += ",";
					}
					sExpression += ")";
					expText.insert(sExpression);// 插入文本内容（选中位置）
				}
			}
		});
		ToolItem delToolItem = new ToolItem(rtoolbar, SWT.PUSH);
		delToolItem.setText(Messages.getString("TLEditor.ExpressionEdit.del"));
		delToolItem.setToolTipText(Messages
				.getString("TLEditor.ExpressionEdit.del"));
		delToolItem.setImage(StudioPlugin.getIcon("delbtn.gif"));
		delToolItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				expText.setText("");
			}
		});
		new ToolItem(rtoolbar, SWT.SEPARATOR);
		ToolItem chkToolItem = new ToolItem(rtoolbar, SWT.PUSH);
		chkToolItem.setText(Messages.getString("TLEditor.ExpressionEdit.check"));
		chkToolItem.setToolTipText(Messages.getString("TLEditor.ExpressionEdit.check"));
		chkToolItem.setImage(StudioPlugin.getIcon("checkvalidity.gif"));

		expText = new Text(sashbottom, SWT.BORDER | SWT.MULTI | SWT.WRAP
				| SWT.LEFT | SWT.V_SCROLL);
		GridData explay = new GridData(GridData.FILL_BOTH);
		explay.grabExcessHorizontalSpace = true;
		explay.grabExcessVerticalSpace = true;
		explay.horizontalSpan = 2;
		expText.setLayoutData(explay);

		expText.setText(expretion);

		sashForm.setWeights(new int[] { 5, 1 });
		return sashForm;
	}

	private void loadData() {
		try {
			Element root = new ExpressionTreeHelper().getExpressionTree();
			List<Element> funs = root.elements();
			for (int i = 0; i < funs.size(); i++) {
				TreeItem treeitem = new TreeItem(fnTree, SWT.NONE);
				readElement(funs.get(i), treeitem);
			}
		} catch (Exception e) {

		}
	}

	private void readElement(Element ele, TreeItem treeitem) {
		treeitem.setText(ele.attributeValue("name"));
		treeitem.setData(ele);
		List<Element> eles = ele.elements();
		if (!eles.isEmpty()) {
			treeitem.setImage(StudioPlugin.getIcon("folder.gif"));
			for (int i = 0; i < eles.size(); i++) {
				TreeItem ctreeitem = new TreeItem(treeitem, SWT.NONE);
				readElement(eles.get(i), ctreeitem);
			}
		} else {
			treeitem.setImage(StudioPlugin.getIcon("file.gif"));
		}
	}

	List<TableEditor> editors = new ArrayList<TableEditor>();

	private void removeAllEditor() {
		for (TableEditor editor : editors) {
			try {
				editor.getEditor().dispose();
			} catch (Exception e) {
			}
			try {
				editor.dispose();
			} catch (Exception e) {
			}
		}
		editors.clear();
	}

	private void loadParamList(Element element, Table table) {
		String param = element.attributeValue("param");
		String paramVal = element.attributeValue("paramvalue");
		if (param != null && !"".equals(param)) {
			String[] pas = param.trim().split(",");
			String[] vals = null;
			if (paramVal != null && !"".equals(paramVal)) {
				vals = paramVal.trim().split(",");
			}
			for (int i = 0; i < pas.length; i++) {
				final TableItem tableitem = new TableItem(table, SWT.NONE);
				String val = "";
				try {
					val = vals == null ? "" : vals[i];
				} catch (Exception e) {
				}
				tableitem.setText(new String[] { pas[i], val });
				tableitem.setData(element);
				TableEditor editor = new TableEditor(table);
				editor.horizontalAlignment = SWT.LEFT;
				editor.grabHorizontal = true;
				final Text newEditor = new Text(table, SWT.FILL);// 文本编辑框
				newEditor.setText(val);
				newEditor.addModifyListener(new ModifyListener() {
					public void modifyText(ModifyEvent e) {
						tableitem.setText(1, newEditor.getText());
					}
				});
				editor.setEditor(newEditor, tableitem, 1);
				editors.add(editor);
			}
		}
	}

	protected Point getInitialSize() {
		return new Point(800, 600);
	}

	protected int getShellStyle() {
		return super.getShellStyle() | SWT.RESIZE;
	}

	/**
	 * Dialog点击按钮时执行的方法
	 */
	protected void buttonPressed(int buttonId) {
		if (buttonId == IDialogConstants.OK_ID) {
			expretion = expText.getText();
		}
		super.buttonPressed(buttonId);
	}

	public String getExpretion() {
		return expretion;
	}

	class QueckItemAdapter extends SelectionAdapter {
		ToolItem tbItem;

		public QueckItemAdapter(ToolItem tbItem) {
			this.tbItem = tbItem;
		}

		public void widgetSelected(SelectionEvent paramSelectionEvent) {
			expText.insert(tbItem.getData().toString());
		}
	}

}
