package com.tulin.v8.ide.ui.editors.process;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import org.dom4j.Element;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TreeEditor;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.json.JSONObject;

import com.tulin.v8.ide.StudioPlugin;
import com.tulin.v8.ide.ui.editors.process.dialog.ExpressionEditorDialog;
import com.tulin.v8.ide.ui.editors.process.dialog.FunctionTreeDialog;
import com.tulin.v8.ide.ui.editors.process.dialog.PersonsSelectionDialog;
import com.tulin.v8.ide.ui.editors.process.element.ConditionPropertySet;
import com.tulin.v8.ide.ui.editors.process.element.IProperty;
import com.tulin.v8.ide.ui.editors.process.element.IPropertys;
import com.tulin.v8.ide.ui.editors.process.element.NodePropertySet;

public class PropertyEditorManager {
	private FlowDesignEditor processeditor;
	private Map<TreeItem, TreeEditor> editors = new HashMap<TreeItem, TreeEditor>();
	private Map<String, TreeItem> properItems = new HashMap<String, TreeItem>();

	public PropertyEditorManager(FlowDesignEditor processeditor) {
		this.processeditor = processeditor;
	}

	private boolean isInput(IProperty property) {
		return "input".equals(property.getInputType());
	}

	private boolean isSelect(IProperty property) {
		return "select".equals(property.getInputType());
	}

	private boolean havaButton(IProperty property) {
		boolean res = false;
		if (property.getButton() != null && !"".equals(property.getButton())) {
			res = true;
		}
		return res;
	}

	public void addEditor(final Tree tree, final TreeItem patitem, IProperty property) {
		final TreeEditor editor = new TreeEditor(tree);
		editor.horizontalAlignment = SWT.LEFT;
		editor.grabHorizontal = true;
		boolean isinput = isInput(property);
		boolean isselect = isSelect(property);
		boolean hvbutton = havaButton(property);
		if (isinput && !hvbutton) {
			final Text newEditor = new Text(tree, SWT.FILL);// 文本编辑框
			newEditor.setText(patitem.getText(1));
			newEditor.addModifyListener(new ModifyListener() {
				public void modifyText(ModifyEvent e) {
					patitem.setText(1, newEditor.getText());
					changeProperValue(tree, patitem, patitem.getText(1));
				}
			});
			editor.setEditor(newEditor, patitem, 1);
		} else if (isselect) {
			final Combo combo = new Combo(tree, SWT.READ_ONLY); // 列表下拉框
			String[][] options = new String[][] {};
			try {
				JSONObject data = (JSONObject) tree.getData("json");
				if ("node".equals(data.getString("type"))) {
					options = NodePropertySet.getOptions(property.getId());
				} else if ("condition".equals(data.getString("type"))) {
					options = ConditionPropertySet.getOptions(data.getString("id"), processeditor);
				}
			} catch (Exception e) {
			}
			int selid = 0;
			String vals = patitem.getText(1);
			for (int i = 0; i < options.length; i++) {
				combo.add(options[i][0]);
				combo.setData(options[i][0], options[i][1]);
				if (options[i][1].equals(vals)) {
					selid = i;
				}
			}
			combo.select(selid);
			combo.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent e) {
					String lab = combo.getText();
					String val = (String) combo.getData(lab);
					patitem.setText(1, val);
					changeProperValue(tree, patitem, patitem.getText(1));
				}
			});
			editor.setEditor(combo, patitem, 1);
		} else if (isinput && hvbutton) {
			Composite canvas = new Composite(tree, SWT.FILL_WINDING);
			canvas.setLayout(new GridLayout(2, false));
			final Text textEditor = new Text(canvas, SWT.FILL);
			GridData txtlay = new GridData(GridData.FILL_HORIZONTAL);
			txtlay.grabExcessHorizontalSpace = true;
			textEditor.setLayoutData(txtlay);
			textEditor.setText(patitem.getText(1));
			textEditor.addModifyListener(new ModifyListener() {
				public void modifyText(ModifyEvent e) {
					patitem.setText(1, textEditor.getText());
					changeProperValue(tree, patitem, patitem.getText(1));
				}
			});
			Button button = new Button(canvas, SWT.PUSH | SWT.CENTER);
			GridData btnlay = new GridData(SWT.NONE);
			btnlay.widthHint = 25;
			btnlay.heightHint = 17;
			button.setLayoutData(btnlay);
			button.setText("..");
			canvas.setData(textEditor);
			setButtonSelection(button, textEditor, tree, patitem, property);
			editor.setEditor(canvas, patitem, 1);
		} else if (hvbutton) {
			Composite canvas = new Composite(tree, SWT.FILL_WINDING);
			canvas.setLayout(new GridLayout(2, false));
			final Text textEditor = new Text(canvas, SWT.READ_ONLY);
			GridData txtlay = new GridData(GridData.FILL_HORIZONTAL);
			txtlay.grabExcessHorizontalSpace = true;
			textEditor.setLayoutData(txtlay);
			textEditor.setText(patitem.getText(1));
			textEditor.addModifyListener(new ModifyListener() {
				public void modifyText(ModifyEvent e) {
					patitem.setText(1, textEditor.getText());
					changeProperValue(tree, patitem, patitem.getText(1));
				}
			});
			Button button = new Button(canvas, SWT.PUSH | SWT.CENTER);
			GridData btnlay = new GridData(SWT.NONE);
			btnlay.widthHint = 25;
			btnlay.heightHint = 17;
			button.setLayoutData(btnlay);
			button.setText("..");
			canvas.setData(textEditor);
			setButtonSelection(button, textEditor, tree, patitem, property);
			editor.setEditor(canvas);
			editor.setEditor(canvas, patitem, 1);
		} else {
			final Text textEditor = new Text(tree, SWT.READ_ONLY);
			textEditor.setText(patitem.getText(1));
			editor.setEditor(textEditor);
			editor.setEditor(textEditor, patitem, 1);
			textEditor.addModifyListener(new ModifyListener() {
				public void modifyText(ModifyEvent e) {
					patitem.setText(1, textEditor.getText());
					changeProperValue(tree, patitem, patitem.getText(1));
				}
			});
		}
		editors.put(patitem, editor);
		properItems.put(property.getId(), patitem);
	}

	private void setButtonSelection(Button button, final Control control, final Tree tree, final TreeItem patitem,
			final IProperty property) {
		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if ("selectexePage".equals(property.getButton())) {
					FunctionTreeDialog dialog = new FunctionTreeDialog(StudioPlugin.getShell());
					dialog.setCurUrl(property.getValue());
					int state = dialog.open();
					if (state == IDialogConstants.OK_ID) {
						Element element = dialog.getSelectData();
						String url = element.attributeValue("url");
						if (control instanceof Text) {
							((Text) control).setText(url);
						} else if (control instanceof Label) {
							((Label) control).setText(url);
							changeProperValue(tree, patitem, url);
						}
						patitem.setText(1, url);
					}
				} else if ("selectexePerson".equals(property.getButton())) {
					TreeItem patitemid = getProperItemById("n_p_roleID");
					TreeItem patitemname = getProperItemById("n_p_role");
					PersonsSelectionDialog dialog = new PersonsSelectionDialog(StudioPlugin.getShell());
					dialog.setPerSelectIds(patitemid.getText(1));
					int state = dialog.open();
					if (state == IDialogConstants.OK_ID) {
						Map<String, String> selpsm = dialog.getSelectedPsms();
						changeProperValue(tree, patitemid, selpsm.get("id"));
						changeProperValue(tree, patitemname, selpsm.get("name"));
						patitemid.setText(1, selpsm.get("id"));
						((Text) editors.get(patitemid).getEditor().getData()).setText(selpsm.get("id"));
						patitemname.setText(1, selpsm.get("name"));
						((Text) editors.get(patitemname).getEditor().getData()).setText(selpsm.get("name"));
					}
				} else {
					ExpressionEditorDialog dialog = new ExpressionEditorDialog(StudioPlugin.getShell(),
							property.getValue());
					int state = dialog.open();
					if (state == IDialogConstants.OK_ID) {
						String expretion = dialog.getExpretion();
						if (control instanceof Text) {
							((Text) control).setText(expretion);
						} else if (control instanceof Label) {
							((Label) control).setText(expretion);
							changeProperValue(tree, patitem, expretion);
						}
						patitem.setText(1, expretion);
					}
				}
			}
		});
	}

	public void changeProperValue(Tree tree, TreeItem patitem, String value) {
		IProperty property = (IProperty) patitem.getData();
		if (property != null) {
			property.setValue(value);
		}
		IPropertys propertys = (IPropertys) tree.getData("list");
		String nodeid = (String) tree.getData("id");
		// System.err.println(propertys.toJSON());
		try {
			processeditor.callJsFunction(
					"setSelObjectProperty('" + nodeid + "','" + URLEncoder.encode(propertys.toJSON(), "UTF-8") + "')");
		} catch (Exception e) {
		}
		try {
			if (property.getId().endsWith("_p_name")) {
				processeditor.callJsFunction(
						"setSelObjectName('" + nodeid + "','" + URLEncoder.encode(value, "UTF-8") + "')");
			}
		} catch (Exception e) {
		}
	}

	public TreeItem getProperItemById(String properid) {
		return properItems.get(properid);
	}

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
		for (Map.Entry<TreeItem, TreeEditor> entry : editors.entrySet()) {
			try {
				entry.getValue().getEditor().dispose();
			} catch (Exception e) {
			}
			entry.getValue().dispose();
		}
		editors.clear();
		properItems.clear();
	}

	public TreeEditor getEditor(TreeItem item) {
		return editors.get(item);
	}

}
