package com.tulin.v8.ide.views.navigator.action;

import java.io.File;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.MessageBox;

import com.tulin.v8.core.FileAndString;
import com.tulin.v8.core.utils.CommonUtil;

import zigen.plugin.db.core.IDBConfig;
import zigen.plugin.db.ui.internal.DataBase;
import zigen.plugin.db.ui.internal.ITable;
import zigen.plugin.db.ui.internal.Schema;
import zigen.plugin.db.ui.internal.Table;
import zigen.plugin.db.ui.internal.View;
import zigen.plugin.db.ui.jobs.AbstractJob;

public class ExportDataDictionary extends Action implements Runnable {
	TreeViewer viewer = null;

	public ExportDataDictionary(TreeViewer viewer) {
		this.viewer = viewer;
		setText(Messages.getString("View.Action.ExportDic.1"));
		setToolTipText(Messages.getString("View.Action.ExportDic.2"));
	}

	public void run() {
		try {
			Object element = (Object) ((StructuredSelection) viewer.getSelection()).getFirstElement();
			if (element instanceof DataBase) {
				DataBase db = (DataBase) element;
				AbstractJob job = new AbstractJob("正在导出数据字典") {
					@Override
					protected IStatus run(IProgressMonitor monitor) {
						export(db.getDbConfig(), monitor, null, null);
						return Status.OK_STATUS;
					}
				};
				job.schedule();
			} else if (element instanceof Schema) {
				Schema schema = (Schema) element;
				AbstractJob job = new AbstractJob("正在导出数据字典") {
					@Override
					protected IStatus run(IProgressMonitor monitor) {
						export(schema.getDbConfig(), monitor, null, schema.getName());
						return Status.OK_STATUS;
					}
				};
				job.schedule();
			} else if (element instanceof ITable) {
				ITable table = (ITable) element;
				if (!(table instanceof View) && table instanceof Table) {
					AbstractJob job = new AbstractJob("正在导出数据字典") {
						@Override
						protected IStatus run(IProgressMonitor monitor) {
							export(table.getDbConfig(), monitor, (Table) table, null);
							return Status.OK_STATUS;
						}
					};
					job.schedule();
				}
			}
		} catch (Exception e) {
		}
	}

	private void export(IDBConfig dbConfig, IProgressMonitor monitor, Table table, String schemaPattern) {
		String tmp = FileAndString
				.FileToString(ExportDataDictionary.class.getResourceAsStream("/wordtmp/data_tables.html"));
		tmp = tmp.replace("${dataBase}", dbConfig.getDbName());
		if (table == null) {
			tmp = tmp.replace("${tableList}", loadTableList(dbConfig, monitor, schemaPattern));
		} else {
			tmp = tmp.replace("${tableList}", loadTable(dbConfig, monitor, table));
		}
		final String filetext = tmp;
		viewer.getControl().getDisplay().asyncExec(new Runnable() {
			@Override
			public void run() {
				FileDialog dlg = new FileDialog(viewer.getControl().getShell(), SWT.SAVE);
				if (table == null) {
					dlg.setFileName(dbConfig.getDbName() + "数据字典.docx");
				} else {
					dlg.setFileName(table.getName() + "数据字典.docx");
				}
				dlg.setFilterNames(new String[] { "Word Files (*.docx)", "All Files (*.*)" });
				dlg.setFilterExtensions(new String[] { "*.docx", "*.*" });
				boolean done = false;
				String savepath = null;
				while (!done && !viewer.getControl().getShell().isDisposed()) {
					String saveFileName = dlg.open();
					// 取消保存对话框
					if (saveFileName == null) {
						// User has cancelled, so quit and return
						MessageBox mg = new MessageBox(dlg.getParent(), SWT.ICON_WARNING | SWT.YES);
						mg.setText("信息");
						mg.setMessage("你取消了保存文件");
						done = mg.open() == SWT.YES;
						done = true;
					} else {
						// 文件名已经存在，是否替换
						File file = new File(saveFileName);
						if (file.exists()) {
							// The file already exists; asks for confirmation
							MessageBox mb = new MessageBox(dlg.getParent(), SWT.ICON_WARNING | SWT.YES | SWT.NO);
							mb.setText("确认");
							mb.setMessage(saveFileName + " 已经存在，是否要替换它?");
							// If they click Yes, drop out. If they click No,
							// redisplay the File Dialog
							done = mb.open() == SWT.YES;
						} else {
							// 不存在文件名重复，可以保存
							done = true;
						}
					}
					savepath = saveFileName;
				}
				if (savepath != null) {
					FileAndString.string2File(filetext, savepath);
				}
			}
		});
	}

	private String loadTableList(IDBConfig dbConfig, IProgressMonitor monitor, String schemaPattern) {
		StringBuffer tsrbf = new StringBuffer();
		try {
			String dbkey = dbConfig.getDbName();
			Map<String, List<String>> tv = CommonUtil.getDataObject(dbkey, schemaPattern);
			List<String> tanles = tv.get("TABLE");
			String tableTmp = FileAndString
					.FileToString(ExportDataDictionary.class.getResourceAsStream("/wordtmp/table_remark.html"));
			String columnTmp = FileAndString
					.FileToString(ExportDataDictionary.class.getResourceAsStream("/wordtmp/table_column.html"));
			int num = 1;
			monitor.beginTask("导出数据字典...", tanles.size());
			for (String tablename : tanles) {
				String tmark = tableTmp.replace("${num}", num + "");
				tmark = tmark.replace("${tableName}", tablename);
				tmark = tmark.replace("${tableMark}", CommonUtil.getTableComments(dbkey, tablename));
				List<String[]> list = CommonUtil.getTableColumn(dbkey, tablename);
				StringBuffer colbf = new StringBuffer();
				for (int i = 0; i < list.size(); i++) {
					String[] column = list.get(i);
					String cmark = columnTmp.replace("${fieldName}", column[0]);
					cmark = cmark.replace("${fieldType}", column[1]);
					if (column[3] != null) {
						cmark = cmark.replace("${fieldLen}", column[3]);
					} else {
						cmark = cmark.replace("${fieldLen}", "");
					}
					if (column[5] != null) {
						cmark = cmark.replace("${fieldDefault}", column[5]);
					} else {
						cmark = cmark.replace("${fieldDefault}", "");
					}
					if (column[2] != null) {
						cmark = cmark.replace("${fieldMark}", column[2]);
					} else {
						cmark = cmark.replace("${fieldMark}", "");
					}
					colbf.append(cmark);
				}
				tmark = tmark.replace("${tableColumn}", colbf.toString());
				tsrbf.append("<p> </p>");
				tsrbf.append(tmark);
				tsrbf.append("<p> </p>");
				num++;
				monitor.worked(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return tsrbf.toString();
	}

	private String loadTable(IDBConfig dbConfig, IProgressMonitor monitor, Table table) {
		StringBuffer tsrbf = new StringBuffer();
		try {
			String dbkey = dbConfig.getDbName();
			String tableTmp = FileAndString
					.FileToString(ExportDataDictionary.class.getResourceAsStream("/wordtmp/table_remark.html"));
			String columnTmp = FileAndString
					.FileToString(ExportDataDictionary.class.getResourceAsStream("/wordtmp/table_column.html"));
			int num = 1;
			String tmark = tableTmp.replace("${num}", num + "");
			tmark = tmark.replace("${tableName}", table.getName());
			tmark = tmark.replace("${tableMark}", table.getRemarks());
			List<String[]> list = CommonUtil.getTableColumn(dbkey, table.getName());
			StringBuffer colbf = new StringBuffer();
			monitor.beginTask("导出数据字典...", list.size());
			for (int i = 0; i < list.size(); i++) {
				String[] column = list.get(i);
				String cmark = columnTmp.replace("${fieldName}", column[0]);
				cmark = cmark.replace("${fieldType}", column[1]);
				if (column[3] != null) {
					cmark = cmark.replace("${fieldLen}", column[3]);
				} else {
					cmark = cmark.replace("${fieldLen}", "");
				}
				if (column[5] != null) {
					cmark = cmark.replace("${fieldDefault}", column[5]);
				} else {
					cmark = cmark.replace("${fieldDefault}", "");
				}
				if (column[2] != null) {
					cmark = cmark.replace("${fieldMark}", column[2]);
				} else {
					cmark = cmark.replace("${fieldMark}", "");
				}
				colbf.append(cmark);
				monitor.worked(1);
			}
			tmark = tmark.replace("${tableColumn}", colbf.toString());
			tsrbf.append(tmark);
			num++;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return tsrbf.toString();
	}
}
