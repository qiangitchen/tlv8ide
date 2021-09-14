package com.tulin.v8.ide.wizards.table;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

import com.tulin.v8.core.DBUtils;
import com.tulin.v8.core.Sys;
import com.tulin.v8.ide.utils.DataType;
import com.tulin.v8.ide.wizards.Messages;

public class CreateTableEndPage extends WizardPage {
	private TableWritePage tablewritepage;
	private Text textarea;

	protected CreateTableEndPage() {
		super("createtableendpage");
	}

	protected CreateTableEndPage(TableWritePage tablewritepage) {
		super("createtableendpage");
		this.setTablewritepage(tablewritepage);
	}

	@Override
	public void createControl(Composite parent) {

		textarea = new Text(parent, SWT.MULTI | SWT.WRAP);
		GridData textlay = new GridData(GridData.FILL_BOTH);
		textarea.setLayoutData(textlay);

		setControl(textarea);

		setTitle(Messages.getString("wizardsaction.newtable.createtitle"));
		setMessage(Messages.getString("wizardsaction.newtable.createmessage"));
	}

	// 执行创建表SQL
	public boolean comitAction() {
		boolean result = false;
		String sql = textarea.getText();
		Connection conn = null;
		Statement stm = null;
		String[] sqls = sql.split(";");
		try {
			conn = DBUtils.getAppConn(tablewritepage.getDbkey());
			stm = conn.createStatement();
			for (int j = 0; j < sqls.length; j++) {
				stm.execute(sqls[j]);
			}
			result = true;
		} catch (Exception e) {
			Sys.printErrMsg(e.toString() + "\n" + sql);
		} finally {
			try {
				DBUtils.CloseConn(conn, stm, null);
			} catch (SQLException e) {
			}
		}
		return result;
	}

	public void setTablewritepage(TableWritePage tablewritepage) {
		this.tablewritepage = tablewritepage;
	}

	public TableWritePage getTablewritepage() {
		return tablewritepage;
	}

	public IWizardPage getNextPage() {
		StringBuffer sql = new StringBuffer();
		sql.append("create table " + tablewritepage.TableName.getText() + "(");
		sql.append("fID " + DataType.dataTypeTranse(tablewritepage.getDbkey(), "string", "32") + " NOT NULL,");
		TableItem[] items = tablewritepage.celltable.getItems();
		boolean autocinf = tablewritepage.checkbox.getSelection();
		for (int i = 0; i < items.length; i++) {
			TableItem item = items[i];
			if (DBUtils.IsMySQLDB(tablewritepage.getDbkey())) {
				sql.append(item.getText(1) + " "
						+ DataType.dataTypeTranse(tablewritepage.getDbkey(), item.getText(2), item.getText(3))
						+ " comment '" + item.getText(4) + "',");
			} else {
				sql.append(item.getText(1) + " "
						+ DataType.dataTypeTranse(tablewritepage.getDbkey(), item.getText(2), item.getText(3)) + ",");
			}
		}
		if (autocinf && DBUtils.IsMySQLDB(tablewritepage.getDbkey())) {
			sql.append("FCREATEPSNFID VARCHAR(2048) comment '创建人FID',");
			sql.append("FCREATEPSNID VARCHAR(36) comment '创建人ID',");
			sql.append("FCREATEPSNNAME VARCHAR(100) comment '创建人名称',");
			sql.append("FCREATEDEPTID VARCHAR(36) comment '部门ID',");
			sql.append("FCREATEDEPTNAME VARCHAR(200) comment '部门名称',");
			sql.append("FCREATEOGNID VARCHAR(36) comment '机构ID',");
			sql.append("FCREATEOGNNAME VARCHAR(200) comment '机构名称',");
			sql.append("FCREATEORGID VARCHAR(36) comment '组织ID',");
			sql.append("FCREATEORGNAME VARCHAR(200) comment '组织名称',");
			sql.append("FCREATETIME DATETIME comment '创建时间',");
		} else if (autocinf && DBUtils.IsMSSQLDB(tablewritepage.getDbkey())) {
			sql.append("FCREATEPSNFID nvarchar(2048),");
			sql.append("FCREATEPSNID nvarchar(36),");
			sql.append("FCREATEPSNNAME nvarchar(100),");
			sql.append("FCREATEDEPTID nvarchar(36),");
			sql.append("FCREATEDEPTNAME nvarchar(200),");
			sql.append("FCREATEOGNID nvarchar(36),");
			sql.append("FCREATEOGNNAME nvarchar(200),");
			sql.append("FCREATEORGID nvarchar(36),");
			sql.append("FCREATEORGNAME nvarchar(200),");
			sql.append("FCREATETIME datetime,");
		} else if (autocinf) {
			sql.append("FCREATEPSNFID VARCHAR2(2048),");
			sql.append("FCREATEPSNID VARCHAR2(36),");
			sql.append("FCREATEPSNNAME VARCHAR2(100),");
			sql.append("FCREATEDEPTID VARCHAR2(36),");
			sql.append("FCREATEDEPTNAME VARCHAR2(200),");
			sql.append("FCREATEOGNID VARCHAR2(36),");
			sql.append("FCREATEOGNNAME VARCHAR2(200),");
			sql.append("FCREATEORGID VARCHAR2(36),");
			sql.append("FCREATEORGNAME VARCHAR2(200),");
			sql.append("FCREATETIME DATE,");
		}
		sql.append("VERSION integer);");

		if (DBUtils.IsMSSQLDB(tablewritepage.getDbkey())) {
			sql.append("alter table [dbo].[" + tablewritepage.TableName.getText()
					+ "] ADD PRIMARY KEY CLUSTERED ([fID]) ON [PRIMARY]; ");
			sql.append("EXECUTE sp_addextendedproperty N'MS_Description', N'" + tablewritepage.TableText.getText()
					+ "', 'USER', N'dbo', 'TABLE', N'" + tablewritepage.TableName.getText() + "', null, null;");
			for (int i = 0; i < items.length; i++) {
				TableItem item = items[i];
				sql.append("EXECUTE sp_addextendedproperty N'MS_Description', N'" + item.getText(4)
						+ "', 'USER', N'dbo', 'TABLE', N'" + tablewritepage.TableName.getText() + "', 'COLUMN', N'"
						+ item.getText(1) + "';");
			}
			if (autocinf) {
				sql.append("EXECUTE sp_addextendedproperty N'MS_Description', N'创建人FID', 'USER', N'dbo', 'TABLE', N'"
						+ tablewritepage.TableName.getText() + "', 'COLUMN', N'FCREATEPSNFID';");
				sql.append("EXECUTE sp_addextendedproperty N'MS_Description', N'创建人ID', 'USER', N'dbo', 'TABLE', N'"
						+ tablewritepage.TableName.getText() + "', 'COLUMN', FCREATEPSNID';");
				sql.append("EXECUTE sp_addextendedproperty N'MS_Description', N'创建人名称', 'USER', N'dbo', 'TABLE', N'"
						+ tablewritepage.TableName.getText() + "', 'COLUMN', FCREATEPSNNAME';");
				sql.append("EXECUTE sp_addextendedproperty N'MS_Description', N'部门ID', 'USER', N'dbo', 'TABLE', N'"
						+ tablewritepage.TableName.getText() + "', 'COLUMN', FCREATEDEPTID';");
				sql.append("EXECUTE sp_addextendedproperty N'MS_Description', N'部门名称', 'USER', N'dbo', 'TABLE', N'"
						+ tablewritepage.TableName.getText() + "', 'COLUMN', FCREATEDEPTNAME';");
				sql.append("EXECUTE sp_addextendedproperty N'MS_Description', N'机构ID', 'USER', N'dbo', 'TABLE', N'"
						+ tablewritepage.TableName.getText() + "', 'COLUMN', FCREATEOGNID';");
				sql.append("EXECUTE sp_addextendedproperty N'MS_Description', N'机构名称', 'USER', N'dbo', 'TABLE', N'"
						+ tablewritepage.TableName.getText() + "', 'COLUMN', FCREATEOGNNAME';");
				sql.append("EXECUTE sp_addextendedproperty N'MS_Description', N'组织ID', 'USER', N'dbo', 'TABLE', N'"
						+ tablewritepage.TableName.getText() + "', 'COLUMN', FCREATEORGID';");
				sql.append("EXECUTE sp_addextendedproperty N'MS_Description', N'组织名称', 'USER', N'dbo', 'TABLE', N'"
						+ tablewritepage.TableName.getText() + "', 'COLUMN', FCREATEORGNAME';");
				sql.append("EXECUTE sp_addextendedproperty N'MS_Description', N'创建时间', 'USER', N'dbo', 'TABLE', N'"
						+ tablewritepage.TableName.getText() + "', 'COLUMN', FCREATETIME';");
			}
		} else if (DBUtils.IsMySQLDB(tablewritepage.getDbkey())) {
			sql.append("alter table " + tablewritepage.TableName.getText() + " ADD PRIMARY KEY (fID);");
		} else {
			sql.append("alter table " + tablewritepage.TableName.getText() + " add constraint "
					+ tablewritepage.getOwner() + "_" + tablewritepage.TableName.getText() + "_KEY primary key (fID);");
			sql.append("COMMENT ON TABLE " + tablewritepage.getOwner() + "." + tablewritepage.TableName.getText()
					+ " IS '" + tablewritepage.TableText.getText() + "';");
			for (int i = 0; i < items.length; i++) {
				TableItem item = items[i];
				sql.append("COMMENT ON COLUMN " + tablewritepage.getOwner() + "." + tablewritepage.TableName.getText()
						+ "." + item.getText(1) + " IS '" + item.getText(4) + "';");
			}
			if (autocinf) {
				sql.append("COMMENT ON COLUMN " + tablewritepage.getOwner() + "." + tablewritepage.TableName.getText()
						+ ".FCREATEPSNFID IS '创建人FID';");
				sql.append("COMMENT ON COLUMN " + tablewritepage.getOwner() + "." + tablewritepage.TableName.getText()
						+ ".FCREATEPSNID IS '创建人ID';");
				sql.append("COMMENT ON COLUMN " + tablewritepage.getOwner() + "." + tablewritepage.TableName.getText()
						+ ".FCREATEPSNNAME IS '创建人名称';");
				sql.append("COMMENT ON COLUMN " + tablewritepage.getOwner() + "." + tablewritepage.TableName.getText()
						+ ".FCREATEDEPTID IS '部门ID';");
				sql.append("COMMENT ON COLUMN " + tablewritepage.getOwner() + "." + tablewritepage.TableName.getText()
						+ ".FCREATEDEPTNAME IS '部门名称';");
				sql.append("COMMENT ON COLUMN " + tablewritepage.getOwner() + "." + tablewritepage.TableName.getText()
						+ ".FCREATEOGNID IS '机构ID';");
				sql.append("COMMENT ON COLUMN " + tablewritepage.getOwner() + "." + tablewritepage.TableName.getText()
						+ ".FCREATEOGNNAME IS '机构名称';");
				sql.append("COMMENT ON COLUMN " + tablewritepage.getOwner() + "." + tablewritepage.TableName.getText()
						+ ".FCREATEORGID IS '组织ID';");
				sql.append("COMMENT ON COLUMN " + tablewritepage.getOwner() + "." + tablewritepage.TableName.getText()
						+ ".FCREATEORGNAME IS '组织名称';");
				sql.append("COMMENT ON COLUMN " + tablewritepage.getOwner() + "." + tablewritepage.TableName.getText()
						+ ".FCREATETIME IS '创建时间';");
			}
		}
		textarea.setText(sql.toString());
		return null;
	}
}
