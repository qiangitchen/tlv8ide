package com.tulin.v8.ide.editors.data.dialog;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

import com.tulin.v8.core.DBUtils;
import com.tulin.v8.core.Sys;
import com.tulin.v8.core.utils.CommonUtil;
import com.tulin.v8.ide.editors.Messages;

public class TableColumnDialog extends Dialog {
	String dbkey;
	String tablename;
	String columnname;
	String columntype;
	String columntlength;
	String columntcomment;
	private TableItem tableitem;
	private Text info;
	private Text colname;
	private Combo colutype;
	private Text colulength;
	private Button cannull;
	private Text cocomment;
	private boolean isnull = false;

	protected TableColumnDialog(Shell parentShell) {
		super(parentShell);
	}

	public TableColumnDialog(Shell parentShell, String dbkey, String tablename) {
		super(parentShell);
		this.dbkey = dbkey;
		this.tablename = tablename;
	}

	public TableColumnDialog(Shell parentShell, String dbkey, String tablename, String columnname,
			TableItem tableitem) {
		super(parentShell);
		this.dbkey = dbkey;
		this.tablename = tablename;
		this.columnname = columnname;
		this.tableitem = tableitem;
	}

	protected Control createDialogArea(Composite parent) {
		getShell().setText(Messages.getString("TLEditor.TableColumn.1"));
		Composite container = (Composite) super.createDialogArea(parent);
		container.setLayout(new FillLayout());

		Composite posite = new Composite(container, SWT.FILL);
		posite.setLayout(new GridLayout());

		Group namegroup = new Group(posite, SWT.NONE);
		namegroup.setLayout(new GridLayout(2, false));
		namegroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		Label tablelabel = new Label(namegroup, SWT.NONE);
		tablelabel.setText(Messages.getString("TLEditor.TableColumn.2"));
		GridData laebllay = new GridData();
		tablelabel.setLayoutData(laebllay);
		Text tbename = new Text(namegroup, SWT.BORDER);
		GridData textlay = new GridData(GridData.FILL_HORIZONTAL);
		textlay.widthHint = 220;
		textlay.grabExcessHorizontalSpace = true;
		tbename.setLayoutData(textlay);
		tbename.setText(tablename);
		tbename.setEditable(false);

		Group sysgroup = new Group(posite, SWT.NONE);
		sysgroup.setText(Messages.getString("TLEditor.TableColumn.3"));
		sysgroup.setLayout(new GridLayout(2, false));
		GridData dataS = new GridData(GridData.FILL_HORIZONTAL);
		// dataS.grabExcessHorizontalSpace = true;
		// dataS.horizontalSpan = 2;
		sysgroup.setLayoutData(dataS);
		Label column = new Label(sysgroup, SWT.NONE);
		column.setLayoutData(laebllay);
		column.setText(Messages.getString("TLEditor.TableColumn.4"));
		colname = new Text(sysgroup, SWT.BORDER);
		colname.setLayoutData(textlay);
		Label coltype = new Label(sysgroup, SWT.NONE);
		coltype.setLayoutData(laebllay);
		coltype.setText(Messages.getString("TLEditor.TableColumn.5"));
		colutype = new Combo(sysgroup, SWT.DROP_DOWN);
		colutype.setLayoutData(textlay);
		Label coltlen = new Label(sysgroup, SWT.NONE);
		coltlen.setLayoutData(laebllay);
		coltlen.setText(Messages.getString("TLEditor.TableColumn.6"));
		colulength = new Text(sysgroup, SWT.BORDER);
		colulength.setLayoutData(textlay);
		Label coltnull = new Label(sysgroup, SWT.NONE);
		coltnull.setLayoutData(laebllay);
		coltnull.setText(Messages.getString("TLEditor.TableColumn.7"));
		cannull = new Button(sysgroup, SWT.CHECK);
		cannull.setLayoutData(textlay);
		Label colcomment = new Label(sysgroup, SWT.NONE);
		colcomment.setLayoutData(laebllay);
		colcomment.setText(Messages.getString("TLEditor.TableColumn.8"));
		cocomment = new Text(sysgroup, SWT.BORDER);
		cocomment.setLayoutData(textlay);

		if (tableitem != null) {
			colname.setText(tableitem.getText(0));
			colutype.setText(tableitem.getText(1));
			String val = tableitem.getText(1);
			if (val.toLowerCase().equals("date") || val.toLowerCase().equals("datetime")
					|| val.toLowerCase().equals("text") || val.toLowerCase().equals("clob")
					|| val.toLowerCase().equals("blob") || val.toLowerCase().equals("int")
					|| val.toLowerCase().equals("integer")) {
				colulength.setText("");
				colulength.setEditable(false);
			} else {
				colulength.setEditable(true);
				colulength.setText(tableitem.getText(2));
			}
			cocomment.setText(tableitem.getText(3));
			boolean isnullable = CommonUtil.ColumnsnNllAbled(dbkey, tablename, columnname);
			if (!isnullable) {
				cannull.setSelection(true);
				isnull = true;
			}
			colname.setEditable(false);
		} else {
			colname.setEditable(true);
		}

		Group infogroup = new Group(posite, SWT.FILL);
		infogroup.setText(Messages.getString("TLEditor.message.title1"));
		infogroup.setLayout(new GridLayout(1, false));
		GridData infolay = new GridData(GridData.FILL_HORIZONTAL);
		// infolay.grabExcessHorizontalSpace = true;
		// infolay.horizontalSpan = 2;
		infogroup.setLayoutData(infolay);
		info = new Text(infogroup, SWT.MULTI | SWT.WRAP);
		info.setLayoutData(new GridData(280, 80));
		info.setEditable(false);
		if (DBUtils.IsOracleDB(dbkey)) {
			colutype.add("VARCHAR2");
			colutype.add("DATE");
			colutype.add("NUMBER");
			colutype.add("CLOB");
			colutype.add("BLOB");
			info.setText(Messages.getString("TLEditor.TableColumn.9"));
		} else if (DBUtils.IsMySQLDB(dbkey)) {
			colutype.add("VARCHAR");
			colutype.add("DATE");
			colutype.add("DATETIME");
			colutype.add("int");
			colutype.add("FLOAT");
			colutype.add("DECIMAL");
			colutype.add("TEXT");
			colutype.add("LONGBLOB");
			info.setText(Messages.getString("TLEditor.TableColumn.10"));
		} else if (DBUtils.IsMSSQLDB(dbkey)) {
			colutype.add("char");
			colutype.add("varchar");
			colutype.add("nvarchar");
			colutype.add("date");
			colutype.add("datetime");
			colutype.add("int");
			colutype.add("float");
			colutype.add("decimal");
			colutype.add("text");
			colutype.add("image");
			colutype.add("binary");
			info.setText(Messages.getString("TLEditor.TableColumn.11"));
		}else {
			colutype.add("char");
			colutype.add("varchar");
			colutype.add("nvarchar");
			colutype.add("date");
			colutype.add("datetime");
			colutype.add("int");
			colutype.add("float");
			colutype.add("decimal");
			colutype.add("text");
			info.setText(Messages.getString("TLEditor.TableColumn.12"));
		}

		colutype.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent e) {
			}

			public void widgetSelected(SelectionEvent e) {
				Combo cb = (Combo) e.getSource();
				String val = cb.getText();
				if (val.toLowerCase().equals("date") || val.toLowerCase().equals("datetime")
						|| val.toLowerCase().equals("text") || val.toLowerCase().equals("clob")
						|| val.toLowerCase().equals("blob") || val.toLowerCase().equals("int")
						|| val.toLowerCase().equals("integer")) {
					colulength.setText("");
					colulength.setEditable(false);
				} else {
					colulength.setEditable(true);
				}
			}
		});

		cannull.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event event) {
				isnull = !isnull;
			}
		});
		return posite;
	}

	protected void buttonPressed(int buttonId) {
		// 点击确认时执行
		if (buttonId == IDialogConstants.OK_ID) {
			String opption = "ADD";
			if (tableitem != null) {
				if (DBUtils.IsMSSQLDB(dbkey) || DBUtils.IsPostgreSQL(dbkey)) {
					opption = "ALTER column";
				} else {
					opption = "modify";
				}
			}
			String column = colname.getText();
			if (column == null || "".equals(column)) {
				info.setText(Messages.getString("TLEditor.TableColumn.btn1"));
				return;
			}
			if (colutype.getText() == null || "".equals(colutype.getText())) {
				info.setText(Messages.getString("TLEditor.TableColumn.btn2"));
				return;
			}
			String scolumntype = getColumnType();
			if (tableitem != null && scolumntype != null) {
				if (DBUtils.IsPostgreSQL(dbkey)) {
					scolumntype = " type " + scolumntype;
				}
			}
			String nullpre = isNUll();
			if (DBUtils.IsPostgreSQL(dbkey)) {
				nullpre = "";
			}
			String sql = "ALTER TABLE " + tablename + " " + opption + " " + column + " " + scolumntype + " " + nullpre;
			String comment = "";
			if (cocomment.getText() != null) {
				comment = cocomment.getText();
			}
			if (DBUtils.IsMySQLDB(dbkey)) {
				sql += " comment '" + comment + "'";
			}
			Connection conn = null;
			Statement st = null;
			try {
				conn = DBUtils.getAppConn(dbkey);
				st = conn.createStatement();
				st.execute(sql);
			} catch (Exception err) {
				info.setText(Messages.getString("TLEditor.TableColumn.btn3") + err.getMessage()
						+ Messages.getString("TLEditor.TableColumn.btn4") + sql);
				System.err.println(sql);
				Sys.packErrMsg(err.toString());
				err.printStackTrace();
				return;
			} finally {
				try {
					DBUtils.CloseConn(conn, st, null);
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			String cSql = "";
			if (DBUtils.IsMSSQLDB(dbkey)) {
				if (tableitem != null) {
					cSql = "EXECUTE sp_updateextendedproperty N'MS_Description', N'" + comment
							+ "', 'USER', N'dbo', 'TABLE', N'" + tablename + "', 'COLUMN', N'" + column + "'";
				} else {
					cSql = "EXECUTE sp_addextendedproperty N'MS_Description', N'" + comment
							+ "', 'USER', N'dbo', 'TABLE', N'" + tablename + "', 'COLUMN', N'" + column + "'";
				}
			} else {
				cSql = "comment on column " + tablename + "." + column + " is '" + comment + "'";
			}
			if (!DBUtils.IsMySQLDB(dbkey)) {
				try {
					conn = DBUtils.getAppConn(dbkey);
					st = conn.createStatement();
					st.execute(cSql);
				} catch (Exception err) {
					info.setText(Messages.getString("TLEditor.TableColumn.btn3") + err.getMessage()
							+ Messages.getString("TLEditor.TableColumn.btn4") + cSql);
					Sys.packErrMsg(err.toString());
					System.err.println(cSql);
					err.printStackTrace();
					return;
				} finally {
					try {
						DBUtils.CloseConn(conn, st, null);
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			}
			if (DBUtils.IsPostgreSQL(dbkey)) {
				String nsql = "ALTER TABLE " + tablename + " ALTER COLUMN " + column + "  drop not NULL";
				if (!"".equals(isNUll())) {
					 nsql = "ALTER TABLE " + tablename + " ALTER COLUMN " + column + "  set not NULL";
				}
				try {
					conn = DBUtils.getAppConn(dbkey);
					st = conn.createStatement();
					st.execute(nsql);
				} catch (Exception err) {
					info.setText(Messages.getString("TLEditor.TableColumn.btn3") + err.getMessage()
							+ Messages.getString("TLEditor.TableColumn.btn4") + nsql);
					Sys.packErrMsg(err.toString());
					System.err.println(nsql);
					err.printStackTrace();
					return;
				} finally {
					try {
						DBUtils.CloseConn(conn, st, null);
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			}
			columnname = column;
			columntype = colutype.getText();
			columntlength = colulength.getText();
			columntcomment = comment;
		}
		super.buttonPressed(buttonId);
	}

	private String getColumnType() {
		String retype = "";
		if (colulength.getText() != null && !"".equals(colulength.getText())) {
			retype = colutype.getText() + "(" + colulength.getText() + ")";
		} else {
			retype = colutype.getText();
			if (colutype.getText().toLowerCase().indexOf("char") > -1) {
				retype += "(100)";
			}
		}
		return retype;
	}

	private String isNUll() {
		return (isnull) ? "NOT NULL" : "";
	}

	public String getColumnname() {
		return columnname;
	}

	public void setColumnname(String columnname) {
		this.columnname = columnname;
	}

	public String getColumntype() {
		return columntype;
	}

	public void setColumntype(String columntype) {
		this.columntype = columntype;
	}

	public String getColumntlength() {
		return columntlength;
	}

	public void setColumntlength(String columntlength) {
		this.columntlength = columntlength;
	}

	public String getColumntcomment() {
		return columntcomment;
	}

	public void setColumntcomment(String columntcomment) {
		this.columntcomment = columntcomment;
	}

}
