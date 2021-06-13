package com.tulin.v8.ide.ui.editors.struts;

import java.io.File;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.MultiPageEditorPart;
import org.eclipse.wst.sse.ui.StructuredTextEditor;

import com.tulin.v8.core.FileAndString;
import com.tulin.v8.core.XMLFormator;
import com.tulin.v8.ide.StudioPlugin;
import com.tulin.v8.ide.ui.editors.struts.action.AddIncludeAction;
import com.tulin.v8.ide.ui.editors.struts.action.RemoveIncludeAction;
import com.tulin.v8.ide.utils.StudioConfig;

@SuppressWarnings("rawtypes")
public class StrutsConfigEditor extends MultiPageEditorPart implements IResourceChangeListener {
	public static String ID = "com.tulin.v8.ide.ui.editors.struts.StrutsConfigEditor";
	private StructuredTextEditor editor;
	private Document TableDocument;
	private Table ptable;
	private Tree FileList;
	private AddIncludeAction addIncludeAction;
	private RemoveIncludeAction removeIncludeAction;

	public StrutsConfigEditor() {
		super();
		ResourcesPlugin.getWorkspace().addResourceChangeListener(this);
	}

	protected void createPages() {
		try {
			editor = new StructuredTextEditor();
			int index = addPage(editor, getEditorInput());
			setPageText(index, Messages.getString("editors.StrutsConfigEditor.pgsurce"));
		} catch (PartInitException e) {
			ErrorDialog.openError(getSite().getShell(), Messages.getString("editors.StrutsConfigEditor.pgsurceErr"),
					null, e.getStatus());
		}
		setPartName(editor.getPartName());// 标题显示为文件名

		createPage1();
		setActivePage(1);
	}

	/**
	 * Creates page 1 设计视图.
	 */
	void createPage1() {
		SashForm sashForm = new SashForm(getContainer(), SWT.NONE);
		FormData formData = new FormData();
		formData.left = new FormAttachment(0, 3);
		formData.right = new FormAttachment(100, -3);
		formData.top = new FormAttachment(0, 0);
		formData.bottom = new FormAttachment(100, -25);
		sashForm.setLayoutData(formData);

		Composite compos = new Composite(sashForm, SWT.FILL | SWT.BORDER);
		compos.setLayout(new GridLayout());

		ToolBar toolbar = new ToolBar(compos, SWT.FLAT | SWT.WRAP | SWT.RIGHT);
		GridData toolbarcl = new GridData(SWT.FILL);
		toolbar.setLayoutData(toolbarcl);
		final ToolItem addbar = new ToolItem(toolbar, SWT.PUSH);
		addbar.setImage(StudioPlugin.getIcon("addbtn.gif"));
		addbar.setText(Messages.getString("editors.StrutsConfigEditor.add"));
		addbar.setToolTipText(Messages.getString("editors.StrutsConfigEditor.addref"));
		final ToolItem removebar = new ToolItem(toolbar, SWT.PUSH);
		removebar.setImage(StudioPlugin.getIcon("delbtn.gif"));
		removebar.setText(Messages.getString("editors.StrutsConfigEditor.del"));
		removebar.setToolTipText(Messages.getString("editors.StrutsConfigEditor.delref"));
		removebar.setEnabled(false);
		Label label = new Label(compos, SWT.FLAT | SWT.WRAP | SWT.RIGHT);
		label.setText(Messages.getString("editors.StrutsConfigEditor.refactionpf"));
		FileList = new Tree(compos, SWT.BORDER | SWT.FULL_SELECTION);
		FileList.setToolTipText(Messages.getString("editors.StrutsConfigEditor.refactionpf"));
		GridData foldlistctl = new GridData(GridData.FILL_BOTH);
		FileList.setLayoutData(foldlistctl);

		ptable = new Table(sashForm, SWT.BORDER | SWT.V_SCROLL | SWT.FULL_SELECTION);
		ptable.setLayoutData(new GridData(GridData.FILL_BOTH));
		ptable.setHeaderVisible(true);
		ptable.setLinesVisible(true);
		TableColumn tablecolumn1 = new TableColumn(ptable, SWT.NONE);
		tablecolumn1.setWidth(400);
		tablecolumn1.setText(Messages.getString("editors.StrutsConfigEditor.tablecproptl"));
		TableColumn tablecolumn2 = new TableColumn(ptable, SWT.NONE);
		tablecolumn2.setWidth(200);
		tablecolumn2.setText(Messages.getString("editors.StrutsConfigEditor.tablecvaluel"));
		final TableEditor editor = new TableEditor(ptable);
		editor.horizontalAlignment = SWT.LEFT;
		editor.grabHorizontal = true;
		ptable.addListener(SWT.MouseDown, new Listener() {
			public void handleEvent(Event event) {
				Rectangle clientArea = ptable.getClientArea();
				Point point = new Point(event.x, event.y);
				int index = ptable.getTopIndex();
				while (index < ptable.getItemCount()) {
					boolean visible = false;
					final TableItem item = ptable.getItem(index);
					Rectangle rectangle = item.getBounds(1);
					if (rectangle.contains(point)) {
						final int column = 1;
						final Text text = new Text(ptable, SWT.NONE);
						Listener textListener = new Listener() {
							@SuppressWarnings("deprecation")
							public void handleEvent(final Event event) {
								switch (event.type) {
								case SWT.FocusOut:
									item.setText(column, text.getText());
									Element el = (Element) item.getData();
									if (!text.getText().equals(el.attributeValue("value"))) {
										el.setAttributeValue("value", text.getText());
										setDataText();
									}
									text.dispose();
									break;
								case SWT.Traverse:
									switch (event.detail) {
									case SWT.TRAVERSE_RETURN:
										item.setText(column, text.getText());
										Element el1 = (Element) item.getData();
										if (!text.getText().equals(el1.attributeValue("value"))) {
											el1.setAttributeValue("value", text.getText());
											setDataText();
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
						editor.setEditor(text, item, 1);
						text.setText(item.getText(1));
						text.selectAll();
						text.setFocus();
						return;
					}
					if (!visible && rectangle.intersects(clientArea)) {
						visible = true;
					}
					if (!visible)
						return;
					index++;
				}
			}
		});

		sashForm.setWeights(new int[] { 4, 6 });
		int index = addPage(sashForm);
		setPageText(index, Messages.getString("editors.StrutsConfigEditor.pgdesign"));
		makeActions();
		// 右键菜单
		FileList.addListener(SWT.MenuDetect, new Listener() {
			public void handleEvent(Event event) {
				MenuManager menuMgr = new MenuManager("#PopupMenu");
				menuMgr.setRemoveAllWhenShown(true);
				menuMgr.addMenuListener(new IMenuListener() {
					public void menuAboutToShow(IMenuManager manager) {
						StrutsConfigEditor.this.fillContextMenu(manager);
					}
				});
				Menu menu = menuMgr.createContextMenu(FileList);
				FileList.setMenu(menu);
			}
		});
		FileList.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				removebar.setEnabled(true);
			}
		});

		// 添加
		addbar.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				addIncludeAction.run();
			}
		});
		// 删除
		removebar.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				removeIncludeAction.run();
			}
		});
		loadModel();// 加载模型
	}

	private void fillContextMenu(IMenuManager manager) {
		manager.removeAll();
		manager.add(addIncludeAction);
		manager.add(removeIncludeAction);
	}

	private void makeActions() {
		addIncludeAction = new AddIncludeAction(this, FileList);
		removeIncludeAction = new RemoveIncludeAction(this, FileList);

	}

	public void setActivePages(int i) {
		setActivePage(i);
	}

	public void dispose() {
		ResourcesPlugin.getWorkspace().removeResourceChangeListener(this);
		super.dispose();
	}

	public void doSave(IProgressMonitor monitor) {
		getEditor(0).doSave(monitor);
	}

	public void doSaveAs() {
		IEditorPart editor = getEditor(0);
		editor.doSaveAs();
		setInput(editor.getEditorInput());
	}

	@Override
	public boolean isSaveAsAllowed() {
		return true;
	}

	protected void pageChange(int newPageIndex) {
		super.pageChange(newPageIndex);
	}

	public void resourceChanged(final IResourceChangeEvent event) {
		if (event.getType() == IResourceChangeEvent.POST_CHANGE) {
			final IEditorInput input = getEditorInput();
			if (input instanceof IFileEditorInput) {
				Display.getDefault().asyncExec(new Runnable() {
					public void run() {
						IFile file = ((IFileEditorInput) input).getFile();
						if (!file.exists()) {
							IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
							page.closeEditor(StrutsConfigEditor.this, false);
						} else if (!getPartName().equals(file.getName())) {
							setPartName(file.getName());
						}
					}
				});
			}
		}
	}

	private void initTable() {
		String text = editor.getDocumentProvider().getDocument(editor.getEditorInput()).get();
		if (text == null || "".equals(text)) {
			/** 当内容编辑器 意外未加载时 直接读取文件内容 */
			String workspacePath = StudioConfig.getWorkspacesPath();
			File nf = new File(workspacePath + "/" + getEditorInput().getToolTipText());
			text = FileAndString.file2String(nf, "UTF-8");
		}
		try {
			TableDocument = TxtToXmlDom(text);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private Document TxtToXmlDom(String txt) throws Exception {
		Document doc;
		doc = DocumentHelper.parseText(txt);
		return doc;
	}

	private void loadModel() {
		initTable();
		loadprotyp();// 加载属性
		loadFileList();
	}

	private void loadprotyp() {
		ptable.removeAll();
		Element struts = TableDocument.getRootElement();
		List cons = struts.elements("constant");
		for (int i = 0; i < cons.size(); i++) {
			Element prop = (Element) cons.get(i);
			String[] itemdata = new String[2];
			itemdata[0] = AttributeCode.getText(prop.attributeValue("name"));
			itemdata[1] = prop.attributeValue("value");
			TableItem tableitem = new TableItem(ptable, SWT.NONE);
			tableitem.setImage(StudioPlugin.getIcon("css_prop.gif"));
			tableitem.setText(itemdata);
			tableitem.setData(prop);
		}
	}

	private void loadFileList() {
		FileList.removeAll();
		Element struts = TableDocument.getRootElement();
		List cons = struts.elements("include");
		for (int i = 0; i < cons.size(); i++) {
			Element prop = (Element) cons.get(i);
			String filevalue = prop.attributeValue("file");
			TreeItem nitem = new TreeItem(FileList, SWT.NONE);
			nitem.setImage(StudioPlugin.getIcon("success_ovr.gif"));
			nitem.setText(filevalue);
			nitem.setData(prop);
		}
	}

	public void setDataText() {
		String domtext = XMLFormator.formatXML(TableDocument);
		String text = editor.getDocumentProvider().getDocument(editor.getEditorInput()).get();
		if (!domtext.equals(text)) {
			editor.getDocumentProvider().getDocument(editor.getEditorInput()).set(domtext);
		}
	}

	public Document getTableDocument() {
		return TableDocument;
	}

	public StructuredTextEditor getTextEditor() {
		return editor;
	}

}
