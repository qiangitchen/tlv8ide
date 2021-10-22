package com.tulin.v8.echarts.ui.editors.echt;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.TreeItem;

public class PropertyEditorManager {
	private EchartsDesignPage designeditor;
	private Map<TableItem, TableEditor> editors = new HashMap<TableItem, TableEditor>();

	public PropertyEditorManager(EchartsDesignPage designeditor) {
		this.designeditor = designeditor;
	}

	public void addEditor(final Table table, final TableItem patitem) {
		final TableEditor editor = new TableEditor(table);
		editor.horizontalAlignment = SWT.LEFT;
		editor.grabHorizontal = true;
		if (!"theme".equals(patitem.getText(0))) {
			final Text newEditor = new Text(table, SWT.FILL);// 文本编辑框
			newEditor.setText(patitem.getText(1));
			newEditor.addModifyListener(new ModifyListener() {
				public void modifyText(ModifyEvent e) {
					designeditor.proptypeEdited(patitem, newEditor.getText());
					patitem.setText(1, newEditor.getText());
				}
			});
			editor.setEditor(newEditor, patitem, 1);
		} else {
			final Combo combo = new Combo(table, SWT.READ_ONLY); // 列表下拉框
			String vals = patitem.getText(1);
			combo.add("");
			combo.add("light");
			combo.add("dark");
			combo.select("light".equals(vals) ? 1 : ("dark".equals(vals) ? 2 : 0));
			combo.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent e) {
					String val = combo.getText();
					designeditor.proptypeEdited(patitem, val);
					patitem.setText(1, val);
				}
			});
			editor.setEditor(combo, patitem, 1);
		}
		editors.put(patitem, editor);
	}

	@SuppressWarnings("unlikely-arg-type")
	public void removeEditor(TreeItem patitem) {
		try {
			editors.get(patitem).getEditor().dispose();
		} catch (Exception e) {
		}
		try {
			editors.get(patitem).dispose();
		} catch (Exception e) {
		}
		try {
			editors.remove(patitem);
		} catch (Exception e) {
		}
	}

	public void removeAll() {
		for (Map.Entry<TableItem, TableEditor> entry : editors.entrySet()) {
			try {
				entry.getValue().getEditor().dispose();
			} catch (Exception e) {
			}
			entry.getValue().dispose();
		}
		editors.clear();
	}

	public TableEditor getEditor(TableItem item) {
		return editors.get(item);
	}

}
