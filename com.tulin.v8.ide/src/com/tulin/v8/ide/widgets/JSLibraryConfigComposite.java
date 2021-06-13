package com.tulin.v8.ide.widgets;

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

public class JSLibraryConfigComposite extends Composite {
	public TableViewer dsTableViewer;
	public Table dsTable;
	public TableViewer mdsTableViewer;
	public Table mdsTable;
	public Button deleteBtn;
	public Button modifyBtn;
	public Button addJsBtn;
	public Button addJsDocBtn;
	public TabFolder tabFolder;

	public JSLibraryConfigComposite(Composite paramComposite, int paramInt) {
		super(paramComposite, paramInt);
		GridLayout localGridLayout1 = new GridLayout(2, false);
		localGridLayout1.marginWidth = 0;
		localGridLayout1.marginHeight = 0;
		setLayout(localGridLayout1);
		this.tabFolder = new TabFolder(this, 0);
		this.tabFolder.setLayoutData(new GridData(4, 4, true, true));
		TabItem localTabItem1 = new TabItem(this.tabFolder, 0);
		localTabItem1.setText(Messages.getString("widgets.JSLibrary.1"));
		Composite localComposite1 = new Composite(this.tabFolder, 0);
		localTabItem1.setControl(localComposite1);
		this.tabFolder.addSelectionListener(new e(this));
		this.tabFolder.addControlListener(new f(this));
		this.dsTableViewer = new TableViewer(localComposite1, 2818);
		this.dsTable = this.dsTableViewer.getTable();
		this.dsTable.setBounds(0, 0, 340, 340);
		this.dsTableViewer.setContentProvider(new g(this));
		this.dsTableViewer.setLabelProvider(new h(this));
		TableColumn localTableColumn1 = new TableColumn(this.dsTable, 0);
		localTableColumn1.setWidth(300);
		localTableColumn1.setText(Messages.getString("widgets.JSLibrary.2"));
		TableColumn localTableColumn2 = new TableColumn(this.dsTable, 0);
		localTableColumn2.setWidth(0);
		localTableColumn2.setText(Messages.getString("widgets.JSLibrary.3"));
		localTableColumn2.setResizable(false);
		Composite localComposite2 = new Composite(this, 0);
		GridData localGridData1 = new GridData(4, 4, false, false);
		localComposite2.setLayoutData(localGridData1);
		GridLayout localGridLayout2 = new GridLayout(1, false);
		localComposite2.setLayout(localGridLayout2);
		new Label(localComposite2, 0);
		this.addJsBtn = new Button(localComposite2, 0);
		this.addJsBtn.setLayoutData(new GridData(4, 4, true, false));
		this.addJsBtn.setText(Messages.getString("widgets.JSLibrary.4"));
		this.addJsBtn.setSize(100, 10);
		this.addJsDocBtn = new Button(localComposite2, 0);
		this.addJsDocBtn.setLayoutData(new GridData(4, 4, true, false));
		this.addJsDocBtn.setText(Messages.getString("widgets.JSLibrary.5"));
		this.addJsDocBtn.setSize(100, 10);
		this.deleteBtn = new Button(localComposite2, 0);
		GridData localGridData2 = new GridData(4, 4, true, false);
		this.deleteBtn.setLayoutData(localGridData2);
		this.deleteBtn.setText(Messages.getString("widgets.JSLibrary.6"));
		this.deleteBtn.setSize(100, 10);
		this.deleteBtn.setEnabled(false);
		this.modifyBtn = new Button(localComposite2, 0);
		GridData localGridData3 = new GridData(89, -1);
		this.modifyBtn.setLayoutData(localGridData3);
		this.modifyBtn.setText(Messages.getString("widgets.JSLibrary.7"));
		this.modifyBtn.setVisible(false);
		TabItem localTabItem2 = new TabItem(this.tabFolder, 0);
		localTabItem2.setText(Messages.getString("widgets.JSLibrary.8"));
		Composite localComposite3 = new Composite(this.tabFolder, 0);
		localTabItem2.setControl(localComposite3);
		this.mdsTableViewer = new TableViewer(localComposite3, 2818);
		this.mdsTable = this.mdsTableViewer.getTable();
		this.mdsTable.setBounds(0, 0, 340, 340);
		this.mdsTableViewer.setContentProvider(new g(this));
		this.mdsTableViewer.setLabelProvider(new h(this));
		TableColumn localTableColumn3 = new TableColumn(this.mdsTable, 0);
		localTableColumn3.setWidth(300);
		localTableColumn3.setText(Messages.getString("widgets.JSLibrary.2"));
		TableColumn localTableColumn4 = new TableColumn(this.mdsTable, 0);
		localTableColumn4.setWidth(0);
		localTableColumn4.setText(Messages.getString("widgets.JSLibrary.3"));
		localTableColumn4.setResizable(false);
	}

	protected void checkSubclass() {
	}
}
