package com.tulin.v8.function.editors;

import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.text.IDocument;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TreeEditor;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.MultiPageEditorPart;
import org.eclipse.ui.views.contentoutline.IContentOutlinePage;
import org.w3c.dom.Element;

import com.tulin.v8.core.TuLinPlugin;
import com.tulin.v8.function.editors.dialog.SelectFunctionDialog;
import com.tulin.v8.webtools.ide.xml.editors.XMLEditor;

/**
 * 功能树（配置）编辑器
 * 
 * @author 陈乾
 *
 */
public class FunctionEditor extends MultiPageEditorPart implements IResourceChangeListener {
	public final static String ID = "com.tulin.v8.function.editors.FunctionEditor";
	private XMLEditor editor;
	private ToolItem addroot;
	private ToolItem addchild;
	private ToolItem addfunc;
	private ToolItem addup;
	private ToolItem adddown;
	private ToolItem adddel;
	private Tree treegrid;
	private DataConversion datacon;
	private TreeEditor tbeditor;
	private Text text;
	private Combo combo;
	private int index;
	private String editortext;

	private Action addrootAction;
	private Action addchildAction;
	private Action viewSourseAction;
	private Action deleteAction;
	private MutiPageContentOutlinePage mutiPageContentOutlinePage;

	private void fillContextMenu(IMenuManager manager) {
		manager.removeAll();
		manager.add(addrootAction);
		manager.add(addchildAction);
		manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
		manager.add(deleteAction);
		manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
		manager.add(viewSourseAction);
	}

	/*
	 * 初始化动作
	 */
	private void makeActions() {
		addrootAction = new Action() {
			public void run() {
				addelementItem();
			}
		};
		addrootAction.setText(Messages.getString("editors.FunEditor.addsubAc"));
		addrootAction.setImageDescriptor(ImageDescriptor.createFromImage(TuLinPlugin.getIcon("add.gif")));

		addchildAction = new Action() {
			public void run() {
				addFunction();
			}
		};
		addchildAction.setText(Messages.getString("editors.FunEditor.addFunAc"));
		addchildAction.setImageDescriptor(ImageDescriptor.createFromImage(TuLinPlugin.getIcon("add.gif")));

		viewSourseAction = new Action() {
			@SuppressWarnings("deprecation")
			public void run() {
				setActivePages(0);
				TreeItem[] selectItems = treegrid.getSelection();
				if (selectItems.length < 1)
					return;
				TreeItem item = selectItems[0];
				Element localElement = (Element) item.getData();
				String text = W3cDocumentHelper.asXML(localElement);
				IDocument document = editor.getDocumentProvider().getDocument(editor.getEditorInput());
				String elestr = text;
				if (text.indexOf("\n") > 0) {
					text = text.substring(0, text.indexOf("\n"));
				}
				if (text.indexOf("/>") > 0) {
					text = text.substring(0, text.indexOf("/>"));
				}
				text = text.trim();
				try {
					int s = document.search(0, text, true, false, false);
					int e = elestr.length();
					if (e > text.length() + 2) {
						e = e - 2;
					}
					editor.selectAndReveal(s, e);// 设置选中
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		};
		viewSourseAction.setText(Messages.getString("editors.FunEditor.gotoSource"));
		viewSourseAction.setImageDescriptor(ImageDescriptor.createFromImage(TuLinPlugin.getIcon("tag.gif")));

		deleteAction = new Action() {
			public void run() {
				deleteItem();
			}
		};
		deleteAction.setText(Messages.getString("editors.FunEditor.del"));
		deleteAction.setImageDescriptor(ImageDescriptor.createFromImage(TuLinPlugin.getIcon("delete.gif")));
	}

	public void addrootItem() {
		Element rootItem = datacon.addRootItem();
		TreeItem newRootItem = new TreeItem(treegrid, SWT.NONE);
		String[] row = new String[] { Messages.getString("editors.FunEditor.rootEle"), "", "layui-icon layui-icon-app",
				"solid", "", "", "" };
		newRootItem.setText(row);
		newRootItem.setImage(datacon.getImage(""));
		newRootItem.setData(rootItem);
		treegrid.setSelection(newRootItem);
		setData();
	}

	public void addelementItem() {
		TreeItem[] selectItems = treegrid.getSelection();
		if (selectItems.length == 0)
			return;
		Element childItem = datacon.addChildItem((Element) selectItems[0].getData());
		TreeItem newChildItem = new TreeItem(selectItems[0], SWT.NONE);
		String[] row = new String[] { Messages.getString("editors.FunEditor.subEle"), "", "layui-icon layui-icon-component",
				"", "", "", "" };
		newChildItem.setText(row);
		newChildItem.setImage(datacon.getImage(""));
		newChildItem.setData(childItem);
		treegrid.setSelection(newChildItem);
		setData();
	}

	public void addFunction() {
		TreeItem[] selectItems = treegrid.getSelection();
		if (selectItems.length == 0)
			return;
		SelectFunctionDialog dialog = new SelectFunctionDialog(getSite().getShell());
		if (dialog.open() == IDialogConstants.OK_ID) {
			TreeItem[] selectItem = treegrid.getSelection();
			String[] row = new String[] { dialog.getSname(), "", "layui-icon layui-icon-file", "", dialog.getSurl(),
					dialog.getProcess(), dialog.getActivity() };
			Element funcItem = datacon.addFunctionItem((Element) selectItem[0].getData(), row);
			TreeItem newFuncItem = new TreeItem(selectItem[0], SWT.NONE);
			newFuncItem.setText(row);
			newFuncItem.setImage(datacon.getImage(dialog.getSurl()));
			newFuncItem.setData(funcItem);
			setData();
			treegrid.setSelection(newFuncItem);
		}
	}

	public void deleteItem() {
		boolean result = MessageDialog.openConfirm(getSite().getShell().getShell(),
				Messages.getString("editors.FunEditor.delconfirm"), Messages.getString("editors.FunEditor.delmessage"));
		if (result) {
			try {
				TreeItem[] selectItems = treegrid.getSelection();
				for (int i = 0; i < selectItems.length; i++) {
					Element selected = (Element) selectItems[i].getData();
					selected.getParentNode().removeChild(selected);
					setData();
					selectItems[i].dispose();
				}
				if (treegrid.getItemCount() < 1) {
					setBtnEnable(false);
				}
			} catch (Exception es) {
				MessageDialog.openError(getSite().getShell().getShell(), Messages.getString("editors.FunEditor.del"),
						es.toString());
			}
		}
	}

	public FunctionEditor() {
		super();
		ResourcesPlugin.getWorkspace().addResourceChangeListener(this);
	}

	void createPage0() {
		try {
			editor = new XMLEditor();
			int index = addPage(editor, getEditorInput());
			setPageText(index, Messages.getString("editors.FunEditor.pgsurce"));
		} catch (PartInitException e) {
			ErrorDialog.openError(getSite().getShell(), Messages.getString("editors.FunEditor.pgsurceErr"), null,
					e.getStatus());
		}
	}

	/**
	 * Creates page 1 设计视图.
	 */
	void createPage1() {
		Composite composite = new Composite(getContainer(), SWT.FILL);
		composite.setLayout(new GridLayout(2, false));

		ToolBar toolbar = new ToolBar(composite, SWT.FLAT | SWT.WRAP | SWT.RIGHT);
		GridData toolbarcl = new GridData(SWT.FILL);
		toolbar.setLayoutData(toolbarcl);

		addroot = new ToolItem(toolbar, SWT.PUSH);
		addroot.setImage(TuLinPlugin.getIcon("addbtn.gif"));
		addroot.setText(Messages.getString("editors.FunEditor.addrootAc"));

		addchild = new ToolItem(toolbar, SWT.PUSH);
		addchild.setImage(TuLinPlugin.getIcon("addbtn.gif"));
		addchild.setText(Messages.getString("editors.FunEditor.addsubAc"));
		addchild.setEnabled(false);

		addfunc = new ToolItem(toolbar, SWT.PUSH);
		addfunc.setImage(TuLinPlugin.getIcon("addbtn.gif"));
		addfunc.setText(Messages.getString("editors.FunEditor.addFunAc"));
		addfunc.setEnabled(false);

		addup = new ToolItem(toolbar, SWT.PUSH);
		addup.setImage(XMLEditorPlugin.getIcon("upbtn.gif"));
		addup.setText(Messages.getString("editors.FunEditor.moveUp"));
		addup.setEnabled(false);

		adddown = new ToolItem(toolbar, SWT.PUSH);
		adddown.setImage(XMLEditorPlugin.getIcon("downbtn.gif"));
		adddown.setText(Messages.getString("editors.FunEditor.moveDown"));
		adddown.setEnabled(false);

		adddel = new ToolItem(toolbar, SWT.PUSH);
		adddel.setImage(TuLinPlugin.getIcon("delbtn.gif"));
		adddel.setText(Messages.getString("editors.FunEditor.del"));
		adddel.setEnabled(false);

		GridData textl = new GridData(SWT.FILL, SWT.CENTER, false, false, 2, 1);
		textl.grabExcessHorizontalSpace = true;
		textl.grabExcessVerticalSpace = true;
		textl.horizontalSpan = 2;
		textl.heightHint = 800;
		treegrid = new Tree(composite, SWT.BORDER | SWT.FULL_SELECTION);
		treegrid.setLayoutData(textl);
		treegrid.setHeaderVisible(true);
		treegrid.setLinesVisible(true);
		TreeColumn tablecolumn1 = new TreeColumn(treegrid, SWT.NONE);
		tablecolumn1.setWidth(180);
		tablecolumn1.setText(Messages.getString("editors.FunEditor.tablecnamel"));
		TreeColumn tablecolumn2 = new TreeColumn(treegrid, SWT.NONE);
		tablecolumn2.setWidth(100);
		tablecolumn2.setText(Messages.getString("editors.FunEditor.tableciconl"));
		TreeColumn tablecolumn2_1 = new TreeColumn(treegrid, SWT.NONE);
		tablecolumn2_1.setWidth(200);
		tablecolumn2_1.setText(Messages.getString("editors.FunEditor.tableclayiconl"));
		TreeColumn tablecolumn3 = new TreeColumn(treegrid, SWT.NONE);
		tablecolumn3.setWidth(90);
		tablecolumn3.setText(Messages.getString("editors.FunEditor.tablecshowtypel"));
		TreeColumn tablecolumn4 = new TreeColumn(treegrid, SWT.NONE);
		tablecolumn4.setWidth(300);
		tablecolumn4.setText(Messages.getString("editors.FunEditor.tablecpathl"));
		TreeColumn tablecolumn5 = new TreeColumn(treegrid, SWT.NONE);
		tablecolumn5.setWidth(200);
		tablecolumn5.setText("Process");
		TreeColumn tablecolumn6 = new TreeColumn(treegrid, SWT.NONE);
		tablecolumn6.setWidth(80);
		tablecolumn6.setText("Activity");

		loadData();

		// 选中事件
		treegrid.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				TreeItem curitem = (TreeItem) e.item;
				addchild.setEnabled(true);
				addfunc.setEnabled(true);
				adddel.setEnabled(true);
				String url = curitem.getText(3);
				if (url.toLowerCase().contains(".jsp") || url.toLowerCase().contains(".htm")) {
					addchild.setEnabled(false);
					addfunc.setEnabled(false);
				}
				chechMovebtn(curitem);
			}
		});

		// 编辑
		tbeditor = new TreeEditor(treegrid);
		tbeditor.horizontalAlignment = SWT.RIGHT;
		tbeditor.grabHorizontal = true;
		treegrid.addListener(SWT.MouseDown, new Listener() {
			public void handleEvent(Event e) {
				index = -1;
				Tree tree1 = ((Tree) e.widget);
				TreeItem[] selection = tree1.getSelection();
				if (selection == null || selection.length == 0)
					return;
				final TreeItem item = selection[0];
				Point point = new Point(e.x, e.y);
				if (item.getData() == null) {
					disposeTextEditor();// 销毁编辑
					return;
				}
				final Element element = (Element) item.getData();
				for (int i = 0; i < treegrid.getColumnCount(); i++) {
					Rectangle rect = item.getBounds(i);
					if (rect.contains(point))
						index = i;
				}
				editortext = item.getText(index);
				if (index != 3) {
					if (text != null) {
						text.dispose();
					}
					text = new Text(treegrid, 0);
					text.addListener(SWT.FocusOut, new Listener() {
						public void handleEvent(Event event) {
							if (text.getText().equals(editortext)) {
								text.dispose();
								return;
							}
							item.setText(index, text.getText());
							editPermision(element, index, text.getText());
							text.dispose();
						}

					});
					text.addListener(SWT.Traverse, new Listener() {
						public void handleEvent(Event event) {
							switch (event.detail) {
							case SWT.TRAVERSE_RETURN:
								if (text.getText().equals(editortext)) {
									text.dispose();
									break;
								}
								item.setText(index, text.getText());
								editPermision(element, index, text.getText());
								text.dispose();
							case SWT.TRAVERSE_ESCAPE:
								text.dispose();
								event.doit = false;
							}
						}

					});
					text.setText(item.getText(index));
					text.selectAll();
					text.setFocus();
					tbeditor.setEditor(text, item, index);
				} else if (index == 3) {
					combo = new Combo(treegrid, SWT.DROP_DOWN);
					combo.setItems(new String[] { "solid", "hide", "" });
					Listener comboListener = new Listener() {
						public void handleEvent(final Event event) {
							switch (event.type) {
							case SWT.FocusOut:
								if (combo.getText().equals(editortext)) {
									combo.dispose();
									break;
								}
								item.setText(index, combo.getText());
								editPermision(element, index, combo.getText());
								combo.dispose();
								break;
							case SWT.Traverse:
								switch (event.detail) {
								case SWT.TRAVERSE_RETURN:
									if (combo.getText().equals(editortext)) {
										combo.dispose();
										break;
									}
									item.setText(index, combo.getText());
									editPermision(element, index, combo.getText());
									combo.dispose();
								case SWT.TRAVERSE_ESCAPE:
									combo.dispose();
									event.doit = false;
								}
								break;
							}
						}
					};
					combo.addListener(SWT.FocusOut, comboListener);
					combo.addListener(SWT.Traverse, comboListener);
					combo.addSelectionListener(new SelectionListener() {
						public void widgetDefaultSelected(SelectionEvent e) {
						}

						public void widgetSelected(SelectionEvent e) {
							Combo cb = (Combo) e.getSource();
							String val = cb.getText();
							item.setText(index, val);
						}
					});
					tbeditor.setEditor(combo, item, index);
					combo.setText(item.getText(index));
					combo.setFocus();
					return;
				}
			}
		});

		// 添加根目录
		addroot.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				addrootItem();
			}
		});

		// 添加子目录
		addchild.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				addelementItem();
			}
		});

		// 添加功能菜单
		addfunc.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				addFunction();
			}
		});

		// 上移
		addup.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				move(true);
			}
		});

		// 下移
		adddown.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				move(false);
			}
		});

		// 删除
		adddel.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				deleteItem();
			}
		});

		makeActions();
		// 右键菜单
		treegrid.addListener(SWT.MenuDetect, new Listener() {
			public void handleEvent(Event event) {
				MenuManager menuMgr = new MenuManager("#PopupMenu");
				menuMgr.setRemoveAllWhenShown(true);
				menuMgr.addMenuListener(new IMenuListener() {
					public void menuAboutToShow(IMenuManager manager) {
						FunctionEditor.this.fillContextMenu(manager);
					}
				});
				Menu menu = menuMgr.createContextMenu(treegrid);
				treegrid.setMenu(menu);
				TreeItem[] selection = treegrid.getSelection();
				if (selection.length > 0) {
					deleteAction.setEnabled(true);
				} else {
					deleteAction.setEnabled(false);
				}
			}
		});

		int index = addPage(composite);
		setPageText(index, Messages.getString("editors.FunEditor.pgdesign"));
	}

	protected void createPages() {
		createPage0();
		createPage1();
		setPartName(editor.getPartName());// 标题显示为文件名
		try {
			setActivePage(1);// 默认展示‘设计’
		} catch (Exception e) {
		}
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

	public boolean isSaveAsAllowed() {
		return true;
	}

	protected void pageChange(int newPageIndex) {
		try {
			super.pageChange(newPageIndex);
			if (newPageIndex == 0) {
				act(this.editor);
			}
			if (newPageIndex == 1) {
				loadData();
				setBtnEnable(false);
			}
		} catch (Exception e) {

		}
	}

	private void act(IEditorPart paramIEditorPart) {
		IContentOutlinePage localIContentOutlinePage = (IContentOutlinePage) paramIEditorPart
				.getAdapter(IContentOutlinePage.class);
		if (paramIEditorPart != null) {
			fiend();
			Display.getDefault().asyncExec(new Z());
			if ((localIContentOutlinePage != null) && (this.mutiPageContentOutlinePage != null))
				this.mutiPageContentOutlinePage.setPageActive(localIContentOutlinePage);
		}
	}

	private void fiend() {
		if ((this.mutiPageContentOutlinePage == null) || (this.mutiPageContentOutlinePage.isDisposed()))
			this.mutiPageContentOutlinePage = new MutiPageContentOutlinePage();
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
							page.closeEditor(FunctionEditor.this, false);
						} else if (!getPartName().equals(file.getName())) {
							setPartName(file.getName());
						}
					}
				});
			}
		}
	}

	private void chechMovebtn(TreeItem curitem) {
		if (moveItem != null && moveItem.getParentItem() == curitem)
			return;
		if (curitem.getParentItem() == null) {
			int len = treegrid.getItems().length;
			if (treegrid.indexOf(curitem) == 0) {
				addup.setEnabled(false);
			} else {
				addup.setEnabled(true);
			}
			if (treegrid.indexOf(curitem) == len - 1) {
				adddown.setEnabled(false);
			} else {
				adddown.setEnabled(true);
			}
		} else {
			TreeItem pitem = curitem.getParentItem();
			int len = pitem.getItems().length;
			if (pitem.indexOf(curitem) == 0) {
				addup.setEnabled(false);
			} else {
				addup.setEnabled(true);
			}
			if (pitem.indexOf(curitem) == len - 1) {
				adddown.setEnabled(false);
			} else {
				adddown.setEnabled(true);
			}
		}
	}

	private void disposeTextEditor() {
		if (text != null) {
			text.dispose();
		}
		if (combo != null) {
			combo.dispose();
		}
	}

	public void loadData() {
		disposeTextEditor();
		String editorText = editor.getDocumentProvider().getDocument(editor.getEditorInput()).get();
		datacon = new DataConversion(treegrid, editorText);
		datacon.getData();
	}

	private void editPermision(Element el, int index, String text) {
		switch (index) {
		case 0:
			if (!text.equals(el.getAttribute("lable")))
				el.setAttribute("label", text);
			break;
		case 1:
			if (!text.equals(el.getAttribute("icon")))
				el.setAttribute("icon", text);
			break;
		case 2:
			if (!text.equals(el.getAttribute("layuiIcon")))
				el.setAttribute("layuiIcon", text);
			break;
		case 3:
			if (!text.equals(el.getAttribute("display")))
				el.setAttribute("display", text);
			break;
		case 4:
			if (!text.equals(el.getAttribute("url")))
				el.setAttribute("url", text);
			break;
		case 5:
			if (!text.equals(el.getAttribute("process")))
				el.setAttribute("process", text);
			break;
		case 6:
			if (!text.equals(el.getAttribute("activity")))
				el.setAttribute("activity", text);
			break;
		default:
			break;
		}
		setData();
	}

	private void setData() {
		String domtext = datacon.DomToString();
		String text = editor.getDocumentProvider().getDocument(editor.getEditorInput()).get();
		if (!domtext.equals(text)) {
			editor.getDocumentProvider().getDocument(editor.getEditorInput()).set(datacon.DomToString());
		}
	}

	// 设置按钮状态{用于全亮或全灰}
	public void setBtnEnable(boolean enable) {
		addchild.setEnabled(enable);
		addfunc.setEnabled(enable);
		addup.setEnabled(enable);
		adddown.setEnabled(enable);
		adddel.setEnabled(enable);
	}

	/*
	 * paramBoolean{true:上移,false:下移}
	 */
	@SuppressWarnings("rawtypes")
	public void move(boolean paramBoolean) {
		TreeItem[] arrayOfTreeItem = treegrid.getSelection();
		if ((arrayOfTreeItem == null) || (arrayOfTreeItem.length == 0))
			return;
		TreeItem localTreeItem = arrayOfTreeItem[0];
		TreeItem parentItem = localTreeItem.getParentItem();
		Element localElement1 = (Element) localTreeItem.getData();
		Element localElement2 = (Element) localElement1.getParentNode();
		List localList1 = W3cDocumentHelper.getChildXmlElementList(localElement2, "item");
		int i = localList1.indexOf(localElement1);
		Element localElement3;
		if (paramBoolean) {
			if (i > 0) {
				localElement3 = (Element) localList1.get(i - 1);
				localElement1 = (Element) localElement2.removeChild(localElement1);
				localElement2.insertBefore(localElement1, localElement3);
			}
		} else if (i < localList1.size() - 1) {
			localElement3 = (Element) localList1.get(i + 1);
			localElement3 = (Element) localElement2.removeChild(localElement3);
			localElement2.insertBefore(localElement3, localElement1);
		}
		setData();
		disposeTextEditor();
		datacon.refreshItem(parentItem);
		TreeItem newitem = getMoveItem(parentItem, localElement1);
		treegrid.setSelection(newitem);
		chechMovebtn(newitem);
	}

	private TreeItem moveItem;

	// 获取移动后的节点
	public TreeItem getMoveItem(Object citem, Element localElement) {
		TreeItem[] items = null;
		if (citem instanceof Tree) {
			items = ((Tree) citem).getItems();
		} else {
			items = ((TreeItem) citem).getItems();
		}
		for (int i = 0; i < items.length; i++) {
			if (items[i].getData() == localElement) {
				moveItem = items[i];
				break;
			} else {
				getMoveItem(items[i], localElement);
			}
		}
		return moveItem;
	}

	public XMLEditor getXMLEditor() {
		return editor;
	}

	@Override
	public Object getSelectedPage() {
		return editor;
	}

}
