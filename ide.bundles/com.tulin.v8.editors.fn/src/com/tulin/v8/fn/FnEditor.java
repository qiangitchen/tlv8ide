package com.tulin.v8.fn;

import org.dom4j.Element;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.text.IDocument;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
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
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.editor.FormPage;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.views.contentoutline.IContentOutlinePage;

import com.tulin.v8.core.TuLinPlugin;
import com.tulin.v8.webtools.ide.xml.editors.XMLEditor;

/**
 * 表达式编辑器
 * 
 * @author chenqian
 * @update 2021-7-25
 */
@SuppressWarnings("deprecation")
public class FnEditor extends FormEditor implements IResourceChangeListener {
	public final static String ID = "com.tulin.v8.fn.FnEditor";
	private XMLEditor editor;
	private ToolItem addFn;
	private ToolItem removeFn;
	private Tree tree;
	private ModelManage modelmanage;
	private Table paramtable;
	public TreeItem currentTreeItem;
	private TableEditor tableeditor;
	private int index;
	private Text edtext;
	private String editortext;
	private Text idtext;
	private Text nametext;
	private Text codetext;
	private Text helptext;
	private ToolItem addparam;
	private ToolItem removeparam;
	private IAction addFnAction;
	private IAction removeFnAction;
	private IAction viewSourseAction;
	private MutiPageContentOutlinePage mutiPageContentOutlinePage;

	public FnEditor() {
		super();
		ResourcesPlugin.getWorkspace().addResourceChangeListener(this);
	}

	void createPage0() {
		try {
			editor = new XMLEditor();
			int index = addPage(editor, getEditorInput());
			setPageText(index, Messages.getString("editors.FnEditor.1"));
		} catch (PartInitException e) {
			ErrorDialog.openError(getSite().getShell(), Messages.getString("editors.FnEditor.2"), null, e.getStatus());
		}
	}

	class MasterDetailsPage extends FormPage {

		public MasterDetailsPage(FormEditor editor) {
			super(editor, "masterDetail", Messages.getString("editors.FnEditor.3"));
		}

		protected void createFormContent(final IManagedForm managedForm) {
			final ScrolledForm form = managedForm.getForm();
			GridLayout layout = new GridLayout();
			form.getBody().setLayout(layout);

			FormToolkit toolkit = managedForm.getToolkit();
			Section section = toolkit.createSection(form.getBody(), Section.NO_TITLE);

			Composite composite = toolkit.createComposite(section, SWT.FILL);
			section.setClient(composite);

			section.setText(Messages.getString("editors.FnEditor.4"));
			section.setDescription(Messages.getString("editors.FnEditor.5"));

			GridData gd = new GridData(GridData.FILL_BOTH);
			section.setLayoutData(gd);

			composite.setLayout(new GridLayout());
			composite.setLayoutData(new GridData(GridData.FILL_BOTH));

			ToolBar toolbar = new ToolBar(composite, SWT.FLAT | SWT.WRAP | SWT.RIGHT);
			toolbar.setBackgroundImage(TuLinPlugin.getIcon("editors/top_banner.gif"));
			GridData toolbarcl = new GridData(GridData.FILL_HORIZONTAL);
			toolbarcl.horizontalAlignment = SWT.FILL;
			toolbarcl.grabExcessHorizontalSpace = true;
			toolbar.setLayoutData(toolbarcl);
			addFn = new ToolItem(toolbar, SWT.PUSH);
			addFn.setImage(TuLinPlugin.getIcon("addbtn.gif"));
			addFn.setText(Messages.getString("editors.FnEditor.6"));
			removeFn = new ToolItem(toolbar, SWT.PUSH);
			removeFn.setImage(TuLinPlugin.getIcon("delbtn.gif"));
			removeFn.setText(Messages.getString("editors.FnEditor.7"));
			removeFn.setEnabled(false);

			SashForm sashForm = new SashForm(composite, SWT.NONE);
			GridData comlayout = new GridData(GridData.FILL_BOTH);
			// comlayout.grabExcessVerticalSpace = true;
			sashForm.setLayoutData(comlayout);

			Section section1 = toolkit.createSection(sashForm, Section.TITLE_BAR);
			section1.setText(Messages.getString("editors.FnEditor.8"));
			tree = new Tree(section1, SWT.V_SCROLL | GridData.FILL_VERTICAL | SWT.BORDER);
			section1.setClient(tree);
			section1.setLayoutData(new GridData(GridData.FILL_VERTICAL));
			tree.addListener(SWT.Expand, new Listener() {
				public void handleEvent(final Event event) {
					final TreeItem root = (TreeItem) event.item;
					root.setImage(TuLinPlugin.getIcon("folder-open.gif"));
				}
			});
			tree.addListener(SWT.Collapse, new Listener() {
				public void handleEvent(final Event event) {
					final TreeItem root = (TreeItem) event.item;
					root.setImage(TuLinPlugin.getIcon("folder.gif"));
				}
			});

			SashForm child = new SashForm(sashForm, SWT.VERTICAL);
			GridData childcomlayout = new GridData(GridData.FILL_BOTH);
			// childcomlayout.grabExcessHorizontalSpace = true;
			sashForm.setLayoutData(childcomlayout);

			// Composite child = toolkit.createComposite(sashForm, SWT.FILL);
			// GridLayout rlayout = new GridLayout(1, false);
			// child.setLayout(rlayout);
			// child.setLayoutData(new GridData(GridData.FILL_BOTH));

			Section section2 = toolkit.createSection(child, Section.TITLE_BAR);
			section2.setText(Messages.getString("editors.FnEditor.9"));
			Composite client = toolkit.createComposite(section2, SWT.FILL);
			client.setLayout(new GridLayout(2, false));
			client.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
			Label label = new Label(client, SWT.NONE);
			label.setText(Messages.getString("editors.FnEditor.10"));
			GridData textlay = new GridData();
			textlay.widthHint = 350;
			textlay.minimumWidth = 350;
			idtext = new Text(client, SWT.BORDER);
			idtext.setLayoutData(textlay);
			Label namelabel = new Label(client, SWT.NONE);
			namelabel.setText(Messages.getString("editors.FnEditor.11"));
			nametext = new Text(client, SWT.BORDER);
			nametext.setLayoutData(textlay);
			final Label codelabel = new Label(client, SWT.NONE);
			codelabel.setText(Messages.getString("editors.FnEditor.12"));
			codetext = new Text(client, SWT.BORDER | SWT.MULTI | SWT.WRAP | SWT.LEFT);
			codetext.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
			final Label helplabel = new Label(client, SWT.NONE);
			helplabel.setText(Messages.getString("editors.FnEditor.13"));
			helptext = new Text(client, SWT.BORDER | SWT.MULTI | SWT.WRAP | SWT.LEFT | SWT.V_SCROLL);
			GridData helplay = new GridData(GridData.FILL_BOTH);
			helplay.grabExcessVerticalSpace = true;
			// helplay.verticalSpan = 3;
			// helplay.heightHint = 80;
			helptext.setLayoutData(helplay);
			section2.setClient(client);
			GridData bseinf = new GridData(GridData.FILL_BOTH);
			section2.setLayoutData(bseinf);

			Section section3 = toolkit.createSection(child, Section.DESCRIPTION | Section.TITLE_BAR);
			section3.setText(Messages.getString("editors.FnEditor.14"));
			Composite paramgp = toolkit.createComposite(section3, SWT.WRAP);
			paramgp.setLayout(new GridLayout(1, false));
			paramgp.setLayoutData(new GridData(GridData.FILL_BOTH));
			GridData patoolbarcl = new GridData(GridData.FILL_HORIZONTAL);
			ToolBar patoolbar = new ToolBar(paramgp, SWT.FLAT | SWT.WRAP | SWT.RIGHT);
			patoolbarcl.horizontalAlignment = SWT.FILL;
			patoolbarcl.grabExcessHorizontalSpace = true;
			patoolbar.setLayoutData(patoolbarcl);
			addparam = new ToolItem(patoolbar, SWT.PUSH);
			addparam.setImage(TuLinPlugin.getIcon("add.gif"));
			addparam.setText(Messages.getString("editors.FnEditor.15"));
			addparam.setEnabled(false);
			removeparam = new ToolItem(patoolbar, SWT.PUSH);
			removeparam.setImage(TuLinPlugin.getIcon("delete.gif"));
			removeparam.setText(Messages.getString("editors.FnEditor.16"));
			removeparam.setEnabled(false);
			paramtable = new Table(paramgp, SWT.BORDER | SWT.V_SCROLL | SWT.FULL_SELECTION);
			paramtable.setHeaderVisible(true);
			paramtable.setLinesVisible(true);
			GridData tablelay = new GridData(GridData.FILL_BOTH);
			// tablelay.heightHint = 240;
			paramtable.setLayoutData(tablelay);
			TableColumn cplumn1 = new TableColumn(paramtable, SWT.NONE);
			cplumn1.setWidth(120);
			cplumn1.setText(Messages.getString("editors.FnEditor.17"));
			TableColumn cplumn2 = new TableColumn(paramtable, SWT.NONE);
			cplumn2.setWidth(120);
			cplumn2.setText(Messages.getString("editors.FnEditor.18"));
			section3.setClient(paramgp);
			section3.setLayoutData(new GridData(GridData.FILL_BOTH));

			child.setWeights(new int[] { 1, 1 });
			sashForm.setWeights(new int[] { 1, 4 });

			// 函数列表选中事件
			tree.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent Event) {
					currentTreeItem = (TreeItem) Event.item;
					if (currentTreeItem.getData() != null) {
						Element element = (Element) currentTreeItem.getData();
						if ("item".equals(element.getName())) {
							removeFn.setEnabled(false);
						} else {
							removeFn.setEnabled(true);
						}
						idtext.setText(element.attributeValue("id"));
						nametext.setText(element.attributeValue("name"));
						paramtable.removeAll();
						removeparam.setEnabled(false);
						if ("function".equals(element.getName())) {
							codetext.setText(element.attributeValue("javacode"));
							helptext.setText(element.attributeValue("helper"));
							codelabel.setVisible(true);
							helplabel.setVisible(true);
							codetext.setVisible(true);
							helptext.setVisible(true);
							modelmanage.loadParamList(element, paramtable);
							addparam.setEnabled(true);
						} else {
							codetext.setText("");
							helptext.setText("");
							codelabel.setVisible(false);
							helplabel.setVisible(false);
							codetext.setVisible(false);
							helptext.setVisible(false);
							addparam.setEnabled(false);
						}
					}
				}
			});
			addFn.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent e) {
					modelmanage.addFn(FnEditor.this.getEditor(), tree);
				}
			});
			removeFn.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent e) {
					boolean re = MessageDialog.openConfirm(getContainer().getShell(),
							Messages.getString("editors.FnEditor.7"), Messages.getString("editors.FnEditor.19"));
					if (re) {
						deleteTreeItem();
					}
				}
			});
			// 编辑标识
			idtext.addModifyListener(new ModifyListener() {
				public void modifyText(ModifyEvent e) {
					if (currentTreeItem != null) {
						Element element = (Element) currentTreeItem.getData();
						if (!"function".equals(element.getName()))
							return;
						String str = idtext.getText();
						if (!str.equals(element.attributeValue("id"))) {
							element.setAttributeValue("id", str);
							setData();
						}
					}
				}
			});
			// 编辑名称
			nametext.addModifyListener(new ModifyListener() {
				public void modifyText(ModifyEvent e) {
					if (currentTreeItem != null) {
						Element element = (Element) currentTreeItem.getData();
						if (!"function".equals(element.getName()))
							return;
						String str = nametext.getText();
						if (!str.equals(element.attributeValue("name"))) {
							element.setAttributeValue("name", str);
							setData();
						}
					}
				}
			});
			// 编辑code
			codetext.addModifyListener(new ModifyListener() {
				public void modifyText(ModifyEvent e) {
					if (currentTreeItem != null) {
						Element element = (Element) currentTreeItem.getData();
						if (!"function".equals(element.getName()))
							return;
						String str = codetext.getText();
						if (!str.equals(element.attributeValue("javacode"))) {
							element.setAttributeValue("javacode", str);
							setData();
						}
					}
				}
			});
			// 编辑help
			helptext.addModifyListener(new ModifyListener() {
				public void modifyText(ModifyEvent e) {
					if (currentTreeItem != null) {
						Element element = (Element) currentTreeItem.getData();
						if (!"function".equals(element.getName()))
							return;
						String str = helptext.getText();
						if (!str.equals(element.attributeValue("helper"))) {
							element.setAttributeValue("helper", str.replace("\n", "<br/>").replace("\r", "<br/>"));
							setData();
						}
					}
				}
			});

			// 参数列表选中事件
			paramtable.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent Event) {
					removeparam.setEnabled(true);
				}
			});

			// 添加参数
			addparam.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent e) {
					TableItem item = new TableItem(paramtable, SWT.NONE);
					item.setText(new String[] { "param" + (paramtable.getItemCount() + 1), "" });
					paramtable.setSelection(item);
					modelmanage.realoadParam(currentTreeItem, paramtable);
					setData();
				}
			});
			// 删除参数
			removeparam.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent e) {
					boolean re = MessageDialog.openConfirm(getContainer().getShell(),
							Messages.getString("editors.FnEditor.20"), Messages.getString("editors.FnEditor.21"));
					if (re) {
						deleteTableItem();
					}
				}
			});

			// 编辑参数
			tableeditor = new TableEditor(paramtable);
			tableeditor.horizontalAlignment = SWT.RIGHT;
			tableeditor.grabHorizontal = true;
			paramtable.addListener(SWT.MouseDown, new Listener() {
				public void handleEvent(Event e) {
					index = -1;
					TableItem[] selection = paramtable.getSelection();
					if (selection == null || selection.length == 0)
						return;
					final TableItem titem = selection[0];
					if (titem.getData() == null) {
						if (edtext != null) {
							edtext.dispose();
						}
						return;
					}
					Point point = new Point(e.x, e.y);
					for (int i = 0; i < paramtable.getColumnCount(); i++) {
						Rectangle rect = titem.getBounds(i);
						if (rect.contains(point))
							index = i;
					}
					editortext = titem.getText(index);
					edtext = new Text(paramtable, 0);
					edtext.addListener(SWT.FocusOut, new Listener() {
						public void handleEvent(Event event) {
							if (edtext.getText().equals(editortext)) {
								edtext.dispose();
								return;
							}
							titem.setText(index, edtext.getText());
							edtext.dispose();
							modelmanage.realoadParam(currentTreeItem, paramtable);
							setData();
						}
					});
					edtext.addListener(SWT.Traverse, new Listener() {
						public void handleEvent(Event event) {
							switch (event.detail) {
							case SWT.TRAVERSE_RETURN:
								if (edtext.getText().equals(editortext)) {
									edtext.dispose();
									break;
								}
								titem.setText(index, edtext.getText());
								edtext.dispose();
								modelmanage.realoadParam(currentTreeItem, paramtable);
								setData();
							case SWT.TRAVERSE_ESCAPE:
								edtext.dispose();
								event.doit = false;
							}
						}
					});
					edtext.setText(titem.getText(index));
					edtext.selectAll();
					edtext.setFocus();
					tableeditor.setEditor(edtext, titem, index);
					return;
				}
			});

			makeActions();
			// 右键菜单
			tree.addListener(SWT.MenuDetect, new Listener() {
				public void handleEvent(Event event) {
					MenuManager menuMgr = new MenuManager("#PopupMenu");
					menuMgr.setRemoveAllWhenShown(true);
					menuMgr.addMenuListener(new IMenuListener() {
						public void menuAboutToShow(IMenuManager manager) {
							FnEditor.this.fillContextMenu(manager);
						}
					});
					Menu menu = menuMgr.createContextMenu(tree);
					tree.setMenu(menu);
					TreeItem[] selection = tree.getSelection();
					if (selection.length > 0) {
						TreeItem im = selection[0];
						Element el = (Element) im.getData();
						if ("function".equals(el.getName())) {
							removeFnAction.setEnabled(true);
						}
					} else {
						removeFnAction.setEnabled(false);
					}
				}
			});
		}
	}

	void createPage1() {
		FormPage page1 = new MasterDetailsPage(this);
		try {
			addPage(page1);
		} catch (PartInitException e) {
			e.printStackTrace();
		}
	}

	private void fillContextMenu(IMenuManager manager) {
		manager.removeAll();
		manager.add(addFnAction);
		manager.add(removeFnAction);
		manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
		manager.add(viewSourseAction);
	}

	/*
	 * 初始化动作
	 */
	private void makeActions() {
		addFnAction = new Action() {
			public void run() {
				modelmanage.addFn(getEditor(), tree);
			}
		};
		addFnAction.setText(Messages.getString("editors.FnEditor.6"));
		addFnAction.setImageDescriptor(ImageDescriptor.createFromImage(TuLinPlugin.getIcon("add.gif")));

		removeFnAction = new Action() {
			public void run() {
				boolean re = MessageDialog.openConfirm(getContainer().getShell(),
						Messages.getString("editors.FnEditor.7"), Messages.getString("editors.FnEditor.19"));
				if (re) {
					deleteTreeItem();
				}
			}
		};
		removeFnAction.setText(Messages.getString("editors.FnEditor.7"));
		removeFnAction.setImageDescriptor(ImageDescriptor.createFromImage(TuLinPlugin.getIcon("delete.gif")));
		removeFnAction.setEnabled(false);

		viewSourseAction = new Action() {
			public void run() {
				setActivePage(0);
				TreeItem[] selectItems = tree.getSelection();
				if (selectItems.length < 1)
					return;
				TreeItem item = selectItems[0];
				Element localElement = (Element) item.getData();
				String text = localElement.asXML();
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
					if (s < 0) {
						s = document.search(0, localElement.attributeValue("id"), true, false, false);
						e = localElement.attributeValue("id").length();
					}
					editor.selectAndReveal(s, e);// 设置选中
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		};
		viewSourseAction.setText(Messages.getString("editors.FnEditor.22"));
		viewSourseAction.setImageDescriptor(ImageDescriptor.createFromImage(TuLinPlugin.getIcon("tag.gif")));
	}

	@Override
	protected void createPages() {
		createPage0();
		createPage1();
		setPartName(editor.getPartName());
		modelmanage = new ModelManage();
		setActivePage(1);
	}

	protected void pageChange(int newPageIndex) {
		super.pageChange(newPageIndex);
		if (newPageIndex == 0) {
			act(this.editor);
		}
		if (newPageIndex == 1) {
			if (modelmanage != null) {
				modelmanage.update(getText());
				modelmanage.loadTree(tree);
			}
			clearData();
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

	@Override
	public void doSave(IProgressMonitor monitor) {
		getEditor(0).doSave(monitor);
	}

	@Override
	public void doSaveAs() {
		IEditorPart editor = getEditor(0);
		editor.doSaveAs();
		setInput(editor.getEditorInput());
	}

	@Override
	public boolean isSaveAsAllowed() {
		return true;
	}

	public void clearData() {
		currentTreeItem = null;
		removeFn.setEnabled(false);
		idtext.setText("");
		nametext.setText("");
		codetext.setText("");
		helptext.setText("");
		addparam.setEnabled(false);
		removeparam.setEnabled(false);
		paramtable.removeAll();
	}

	private void deleteTreeItem() {
		try {
			Element element = (Element) currentTreeItem.getData();
			element.getParent().remove(element);
			currentTreeItem.dispose();
			setData();
		} catch (Exception es) {
			MessageDialog.openError(getSite().getShell().getShell(), Messages.getString("editors.FnEditor.23"),
					es.toString());
		}

	}

	private void deleteTableItem() {
		try {
			paramtable.remove(paramtable.getSelectionIndices());
			modelmanage.realoadParam(currentTreeItem, paramtable);
			setData();
		} catch (Exception es) {
			MessageDialog.openError(getSite().getShell().getShell(), Messages.getString("editors.FnEditor.24"),
					es.toString());
		}
	}

	public void setData() {
		editor.getDocumentProvider().getDocument(editor.getEditorInput()).set(modelmanage.toString());
		modelmanage.doing = false;
	}

	public String getText() {
		return editor.getDocumentProvider().getDocument(editor.getEditorInput()).get();
	}

	public FnEditor getEditor() {
		return this;
	}

	public XMLEditor getXMLEditor() {
		return editor;
	}

	@Override
	protected void addPages() {

	}

	public void dispose() {
		ResourcesPlugin.getWorkspace().removeResourceChangeListener(this);
		super.dispose();
	}

	@Override
	public void resourceChanged(final IResourceChangeEvent event) {
		if (event.getType() == IResourceChangeEvent.POST_CHANGE) {
			final IEditorInput input = getEditorInput();
			if (input instanceof IFileEditorInput) {
				Display.getDefault().asyncExec(new Runnable() {
					public void run() {
						IFile file = ((IFileEditorInput) input).getFile();
						if (!file.exists()) {
							IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
							page.closeEditor(FnEditor.this, false);
						} else if (!getPartName().equals(file.getName())) {
							setPartName(file.getName());
						}
					}
				});
			}
		}
	}
	
	@Override
	public Object getSelectedPage() {
		return editor;
	}

}
