package com.tulin.v8.ide.ui.editors.page.design;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.io.File;
import java.util.Arrays;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.ListenerList;
import org.eclipse.core.runtime.SafeRunner;
import org.eclipse.gef.ui.actions.ActionRegistry;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.GroupMarker;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.util.SafeRunnable;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.IPostSelectionProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.forms.editor.FormPage;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.wst.html.core.internal.document.ElementStyleImpl;
import org.eclipse.wst.sse.core.internal.provisional.IStructuredModel;
import org.eclipse.wst.sse.core.internal.provisional.IndexedRegion;
import org.eclipse.wst.sse.core.internal.provisional.text.ITextRegion;
import org.eclipse.wst.sse.ui.StructuredTextEditor;
import org.eclipse.wst.sse.ui.internal.ExtendedConfigurationBuilder;
import org.eclipse.wst.sse.ui.internal.provisional.extensions.ConfigurationPointCalculator;
import org.eclipse.wst.sse.ui.views.contentoutline.ContentOutlineConfiguration;
import org.jsoup.nodes.Document;

import com.tulin.v8.ide.StudioPlugin;
import com.tulin.v8.ide.ui.editors.Messages;
import com.tulin.v8.ide.ui.editors.page.DesignerStructuredTextEditorJSP;
import com.tulin.v8.ide.ui.editors.page.PageEditorInterface;
import com.tulin.v8.ide.ui.editors.page.design.action.HeadAddResourseAction;
import com.tulin.v8.ide.ui.editors.page.design.action.ModeCatAction;
import com.tulin.v8.ide.ui.editors.page.design.action.ModeCopyAction;
import com.tulin.v8.ide.ui.editors.page.design.action.ModePasteAction;
import com.tulin.v8.ide.ui.editors.page.design.dialog.AddPropotypeDialog;

/**
 * @Des 网页设计器(模型推拽效果)
 * @see {@link com.tulin.v8.ide.ui.editors.page.SimpleGraphicalEditor}
 * @author 陈乾
 */
@SuppressWarnings({ "restriction", "deprecation" })
public class WEBPageDesignEditor extends FormPage
		implements WEBDesignEditorInterface, ISelectionListener, ISelectionChangedListener, IDoubleClickListener {
	private static final String OUTLINE_CONTEXT_MENU_ID = "org.eclipse.wst.sse.ui.StructuredTextEditor.OutlineContext"; //$NON-NLS-1$
	private static final String OUTLINE_CONTEXT_MENU_SUFFIX = ".source.OutlineContext"; //$NON-NLS-1$

	protected DesignerStructuredTextEditorJSP editor;
	protected PageEditorInterface editorpart;

	protected TreeViewer treeViewer;
	private ContentOutlineConfiguration cfg;

	public Action viewSourseAction;

	private HeadAddResourseAction addResourseAction;
	private ModeCatAction catAction;
	private ModeCopyAction copyAction;
	private ModePasteAction pasteAction;

	boolean design = false;

	Clipboard clipbd = Toolkit.getDefaultToolkit().getSystemClipboard();
	public String editPage;
	private Table table;
	private Table etable;
	private ToolItem addparam;

	public WEBPageDesignEditor(DesignerStructuredTextEditorJSP editor, PageEditorInterface editorpart) {
		super("webDesign", Messages.getString("TLEditor.pageEditor.3"));
		this.editor = editor;
		this.editorpart = editorpart;
	}

	private void setGlobalActionHandler(final IAction action) {
		if (action != null) {
			final String actionId = action.getId();
			if (actionId != null && actionId.length() > 0) {
				final IEditorSite site = getEditorSite();
				if (site != null) {
					final IActionBars bars = site.getActionBars();
					if (bars != null) {
						bars.setGlobalActionHandler(actionId, action);
					}
				}
			}
		}
	}

	@Override
	public void fillContextMenu(IMenuManager manager) {
		if (!treeViewer.getSelection().isEmpty()) {
			manager.add(catAction);
			manager.add(copyAction);
			manager.add(pasteAction);
			pasteAction.updateState();
			manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
		}
	}

	@Override
	public void makeActions() {
		addResourseAction = new HeadAddResourseAction(treeViewer, this);
		catAction = new ModeCatAction(treeViewer, editorpart, clipbd, editor);
		copyAction = new ModeCopyAction(treeViewer, clipbd);
		pasteAction = new ModePasteAction(treeViewer, clipbd, this);
	}

	private void updateglobal() {
		setGlobalActionHandler(catAction);
		setGlobalActionHandler(copyAction);
		setGlobalActionHandler(pasteAction);
	}

	public void evenTabledbclick(TableItem item, String name, String text) {
		String jstext = editorpart.getJSEditorText();
		String ndefunction = "\n\n/**\n@param {object} event \n*/\nfunction " + text + "(event){\n\t\n}";
		if ("onRowInit".equals(name)) {
			ndefunction = "\n\n" + Messages.getString("TLEditor.WEBDesign.Function.1") + "function " + text
					+ "(cgrid,rowObj){\n\t\n}";
		}
		if ("dataValueChanged".equals(name)) {
			ndefunction = "\n\n" + Messages.getString("TLEditor.WEBDesign.Function.2") + "function " + text
					+ "(event){\n\t\n}";
		}
		if ("onchecked".equals(name)) {
			ndefunction = "\n\n" + Messages.getString("TLEditor.WEBDesign.Function.3") + "function " + text
					+ "(event){\n\t\n}";
		}
		if ("oncheckAll".equals(name) || "onuncheckAll".equals(name)) {
			ndefunction = "\n\n" + Messages.getString("TLEditor.WEBDesign.Function.4") + "function " + text
					+ "(event){\n\t\n}";
		}
		if ("onauditOpionWrited".equals(name)) {
			ndefunction = "\n\n" + Messages.getString("TLEditor.WEBDesign.Function.5") + "function " + text
					+ "(event){\n\t\n}";
		}
		try {
			ElementStyleImpl element = (ElementStyleImpl) item.getData();
			String component = element.getAttribute("component");
			if (component != null) {
				if ("flowprocess".equals(component)) {
					if (name.startsWith("onAfter") || name.startsWith("onBefore") || name.endsWith("Commit")) {
						ndefunction = "\n\n" + Messages.getString("TLEditor.WEBDesign.Function.6") + "function " + text
								+ "(event){\n\t\n}";
					}
				}
			}
		} catch (Exception e) {
		}
		editorpart.setJSEditorPageText(jstext + ndefunction);
		try {
			IDocument document = editorpart.getJSEditor().getDocumentProvider()
					.getDocument(editorpart.getJSEditor().getEditorInput());
			int ms = document.search(0, text, true, false, false);
			editorpart.getJSEditor().selectAndReveal(ms, text.length());// 设置选中
		} catch (Exception er) {
		}
	}

	/**
	 * 属性编辑
	 */
	public void proptypeEdited(TableItem item, Text text) {
		ElementStyleImpl pele = (ElementStyleImpl) item.getData();
		String pname = item.getText(0);
		String bpvalue = pele.getAttribute(pname);
		if (bpvalue == null) {
			bpvalue = "";
		}
		String pvalue = text.getText();
		if (pvalue == null) {
			pvalue = "";
		}
		if (!pvalue.equals(bpvalue)) {
			if (!"".equals(pvalue)) {
				pele.setAttribute(pname, pvalue);
			} else {
				pele.setAttribute(pname, "");
			}
		}
		item.setData(pele);
	}

	/**
	 * 事件编辑
	 */
	public void eventEdited(TableItem item, Text text) {
		ElementStyleImpl pele = (ElementStyleImpl) item.getData();
		String evname = item.getText(0);
		String bevale = pele.getAttribute(evname);
		if (bevale == null) {
			bevale = "";
		}
		String evalue = text.getText();
		if (evalue == null) {
			evalue = "";
		}
		if (!evalue.equals(bevale)) {
			if (!"".equals(evalue)) {
				pele.setAttribute(evname, evalue);
			} else {
				if (pele.hasAttribute(evname)) {
					pele.removeAttribute(evname);
				}
			}
		}
		item.setData(pele);
	}

	public void loadBrowser() {
		if (design) {
			return;
		}
	}

	public Document setModel() {
		if (design) {
			return null;
		}
		if (treeViewer != null) {
			treeViewer.setInput(getModel());
		}
		return null;
	}

	public File getFile() {
		try {
			IFile file = ((FileEditorInput) editor.getEditorInput()).getFile();
			return file.getLocation().makeAbsolute().toFile();
		} catch (Exception e) {
			return new File(editor.getEditorInput().getToolTipText());
		}
	}

	public IEditorPart getActiveEditors() {
		return this;
	}

	public IStructuredModel getModel() {
		return editor.getStructuredModel();
	}

	public Tree getElementTree() {
		// return tree;
		return treeViewer.getTree();
	}

	/**
	 * 获取网页浏览器
	 */
	public Browser getBrowser() {
		return null;
	}

	/**
	 * 设置Element map
	 */
	public void setElementItem(String elementId, TreeItem item) {
	}

	public TreeItem getElementItem(String elementId) {
		return null;
	}

	public void selectElement(String elementId) {
	}

	public void addPropotype() {
		ISelection selection = treeViewer.getSelection();
		if (selection instanceof IStructuredSelection) {
			Object[] Selections = ((IStructuredSelection) selection).toArray();
			if (Selections.length > 0) {
				Object object = Selections[0];
				if (object instanceof ElementStyleImpl) {
					ElementStyleImpl element = (ElementStyleImpl) object;
					AddPropotypeDialog dialog = new AddPropotypeDialog(getSite().getShell());
					if (dialog.open() == IDialogConstants.OK_ID) {
						String fName = dialog.getSname();
						String fValue = dialog.getSvalue();
						if (element.hasAttribute(fName)) {
							MessageDialog.openError(getSite().getShell(),
									Messages.getString("TLEditor.WEBDesign.addprotype.4"),
									Messages.getString("TLEditor.WEBDesign.addprotype.6"));
							return;
						}
						element.setAttribute(fName, fValue);
						TableItem item = new TableItem(table, SWT.BORDER);
						item.setText(new String[] { fName, fValue });
						item.setData(element);
					} else {
						MessageDialog.openError(getSite().getShell(),
								Messages.getString("TLEditor.WEBDesign.addprotype.4"),
								Messages.getString("TLEditor.WEBDesign.addprotype.7"));
					}
				}
			}
		}
	}

	@Override
	public void catElement() {
		catAction.run();
	}

	@Override
	public void copyElement() {
		copyAction.run();
	}

	@Override
	public void deleteElement() {

	}

	@Override
	public WEBDesignEditorInterface getHTMLEditor() {
		return this;
	}

	@Override
	public PageEditorInterface getEditorpart() {
		return editorpart;
	}

	@Override
	public Document getPageDom() {
		// return pageDom;
		return null;
	}

	@Override
	public StructuredTextEditor getTextEditor() {
		return editor;
	}

	@Override
	public int getDesignerMode() {
		return editorpart.getDesignerMode();
	}

	@Override
	public void viewSourse() {
		viewSourseAction.run();
	}

	@Override
	public void activhtmlEditor() {
		editorpart.activhtmlEditor();
	}

	@Override
	public StructuredTextEditor getSourceEditor() {
		return editor;
	}

	private String[] getConfigurationPoints() {
		String contentTypeIdentifierID = null;
		if (getModel() != null) {
			contentTypeIdentifierID = getModel().getContentTypeIdentifier();
		}
		return ConfigurationPointCalculator.getConfigurationPoints(this, contentTypeIdentifierID,
				ConfigurationPointCalculator.SOURCE, StructuredTextEditor.class);
	}

	private ContentOutlineConfiguration createContentOutlineConfiguration() {
		ContentOutlineConfiguration cfg = null;
		ExtendedConfigurationBuilder builder = ExtendedConfigurationBuilder.getInstance();
		String[] ids = getConfigurationPoints();
		for (int i = 0; cfg == null && i < ids.length; i++) {
			cfg = (ContentOutlineConfiguration) builder
					.getConfiguration(ExtendedConfigurationBuilder.CONTENTOUTLINECONFIGURATION, ids[i]);
		}
		return cfg;
	}

	private ActionRegistry actionRegistry;

	protected ActionRegistry getActionRegistry() {
		if (actionRegistry == null)
			actionRegistry = new ActionRegistry();
		return actionRegistry;
	}

	/**
	 * @param id
	 * @return the action for the id
	 */
	public IAction getAction(Object id) {
		// lium: following lines commented out, see comments in
		// DesignerUndoRedoAction
		// if (ITextEditorActionConstants.UNDO.equals(id) ||
		// ITextEditorActionConstants.REDO.equals(id))
		// {
		// return _delegate.getTextEditor().getAction((String) id);
		// }
		return getActionRegistry().getAction(id);
	}

	@Override
	public void createPartControl(Composite parent) {
		SashForm sash = new SashForm(parent, SWT.VERTICAL);

		treeViewer = new TreeViewer(sash, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);

		cfg = createContentOutlineConfiguration();
		treeViewer.setLabelProvider(cfg.getLabelProvider(treeViewer));
		treeViewer.setContentProvider(cfg.getContentProvider(treeViewer));

		treeViewer.addSelectionChangedListener(this);
		treeViewer.addDoubleClickListener(this);


		final TabFolder tabFolder = new TabFolder(sash, SWT.NONE);

		TabItem tabItemAr = new TabItem(tabFolder, SWT.NONE);
		tabItemAr.setText(Messages.getString("TLEditor.WEBDesign.1"));
		Composite propotype = new Composite(tabFolder, SWT.FILL);
		GridLayout rlayout = new GridLayout(1, false);
		propotype.setLayout(rlayout);
		propotype.setLayoutData(new GridData(GridData.FILL_BOTH));
		GridData patoolbarcl = new GridData(GridData.FILL_HORIZONTAL);
		ToolBar patoolbar = new ToolBar(propotype, SWT.FLAT | SWT.WRAP | SWT.RIGHT);
		patoolbarcl.horizontalAlignment = SWT.FILL;
		patoolbarcl.grabExcessHorizontalSpace = true;
		patoolbar.setLayoutData(patoolbarcl);
		addparam = new ToolItem(patoolbar, SWT.PUSH);
		addparam.setImage(StudioPlugin.getIcon("add.gif"));
		addparam.setText(Messages.getString("TLEditor.WEBDesign.2"));
		addparam.setEnabled(false);

		// 添加属性
		addparam.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				addPropotype();
			}
		});

		table = new Table(propotype, SWT.BORDER | SWT.FULL_SELECTION);
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		table.setLayoutData(new GridData(GridData.FILL_BOTH));
		TableColumn name = new TableColumn(table, SWT.NONE);
		name.setText(Messages.getString("TLEditor.WEBDesign.3"));
		name.setWidth(100);
		TableColumn value = new TableColumn(table, SWT.NONE);
		value.setWidth(100);
		value.setText(Messages.getString("TLEditor.WEBDesign.4"));
		tabItemAr.setControl(propotype);

		// 属性编辑
		table.addListener(SWT.MouseDown, new Listener() {
			public void handleEvent(Event event) {
				TableEditor editors = new TableEditor(table);
				editors.horizontalAlignment = SWT.LEFT;
				editors.grabHorizontal = true;
				Rectangle clientArea = table.getClientArea();
				Point point = new Point(event.x, event.y);
				int index = table.getTopIndex();
				while (index < table.getItemCount()) {
					boolean visible = false;
					final TableItem item = table.getItem(index);
					for (int i = 1; i < table.getColumnCount(); i++) {
						Rectangle rectangle = item.getBounds(i);
						if (rectangle.contains(point)) {
							final int column = i;
							final Text text = new Text(table, SWT.NONE);
							Listener textListener = new Listener() {
								public void handleEvent(final Event event) {
									switch (event.type) {
									case SWT.FocusOut:
										if ("id".equals(item.getText(0))) {
											editorpart.changeJsSourse(item.getText(column), text.getText());
										}
										proptypeEdited(item, text);
										item.setText(column, text.getText());
										text.dispose();
										break;
									case SWT.Traverse:
										switch (event.detail) {
										case SWT.TRAVERSE_RETURN:
											if ("id".equals(item.getText(0))) {
												editorpart.changeJsSourse(item.getText(column), text.getText());
											}
											proptypeEdited(item, text);
											item.setText(column, text.getText());
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
							editors.setEditor(text, item, i);
							text.setText(item.getText(i));
							text.selectAll();
							text.setFocus();
							return;
						}
						if (!visible && rectangle.intersects(clientArea)) {
							visible = true;
						}
					}
					if (!visible)
						return;
					index++;
				}
			}
		});

		final TabItem tabItemEv = new TabItem(tabFolder, SWT.NONE);
		tabItemEv.setText(Messages.getString("TLEditor.WEBDesign.5"));
		etable = new Table(tabFolder, SWT.BORDER | SWT.FULL_SELECTION);
		etable.setHeaderVisible(true);
		etable.setLinesVisible(true);
		TableColumn Evname = new TableColumn(etable, SWT.NONE);
		Evname.setText(Messages.getString("TLEditor.WEBDesign.6"));
		Evname.setWidth(100);
		TableColumn Evvalue = new TableColumn(etable, SWT.NONE);
		Evvalue.setWidth(100);
		Evvalue.setText(Messages.getString("TLEditor.WEBDesign.7"));
		tabItemEv.setControl(etable);

		// 事件编辑
		etable.addListener(SWT.MouseDown, new Listener() {
			public void handleEvent(Event event) {
				TableEditor editors = new TableEditor(etable);
				editors.horizontalAlignment = SWT.LEFT;
				editors.grabHorizontal = true;
				Rectangle clientArea = etable.getClientArea();
				Point point = new Point(event.x, event.y);
				int index = etable.getTopIndex();
				while (index < etable.getItemCount()) {
					boolean visible = false;
					final TableItem item = etable.getItem(index);
					for (int i = 1; i < etable.getColumnCount(); i++) {
						Rectangle rectangle = item.getBounds(i);
						if (rectangle.contains(point)) {
							final int column = i;
							final Text text = new Text(etable, SWT.NONE);
							Listener textListener = new Listener() {
								public void handleEvent(final Event event) {
									switch (event.type) {
									case SWT.FocusOut:
										eventEdited(item, text);
										item.setText(column, text.getText());
										text.dispose();
										break;
									case SWT.Traverse:
										switch (event.detail) {
										case SWT.TRAVERSE_RETURN:
											eventEdited(item, text);
											item.setText(column, text.getText());
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
							editors.setEditor(text, item, i);
							text.setText(item.getText(i));
							text.selectAll();
							text.setFocus();
							return;
						}
						if (!visible && rectangle.intersects(clientArea)) {
							visible = true;
						}
					}
					if (!visible)
						return;
					index++;
				}
			}
		});

		sash.setWeights(new int[] { 1, 1 });

		// 属性表
		table.addMouseListener(new MouseListener() {
			@Override
			public void mouseDoubleClick(MouseEvent arg0) {
				TableItem[] items = table.getSelection();
				if (items.length < 1)
					return;
				TableItem item = items[0];
				String name = item.getText(0);
				String text = item.getText(1);
				if (text.indexOf("(") > 0) {
					text = text.substring(0, text.indexOf("("));
				}
				// data对象的操作方法 对应JS函数
				if ("insertAction".equals(name) || "saveAction".equals(name) || "deleteAction".equals(name)
						|| "refreshAction".equals(name)) {
					editorpart.activJsEditor();// 跳转到JS源码页
					IDocument document = editorpart.getJSEditor().getDocumentProvider()
							.getDocument(editorpart.getJSEditor().getEditorInput());
					try {
						int s = document.search(0, text, true, false, false);
						editorpart.getJSEditor().selectAndReveal(s, text.length());// 设置选中
					} catch (BadLocationException e) {
						e.printStackTrace();
					}
				}
			}

			@Override
			public void mouseDown(MouseEvent arg0) {
			}

			@Override
			public void mouseUp(MouseEvent arg0) {
			}
		});

		// 事件表
		etable.addMouseListener(new MouseListener() {
			@Override
			public void mouseDoubleClick(MouseEvent arg0) {
				TableItem[] items = etable.getSelection();
				if (items.length < 1)
					return;
				TableItem item = items[0];
				String name = item.getText(0);
				String text = item.getText(1);
				if (text.indexOf("(") > 0) {
					text = text.substring(0, text.indexOf("("));
				}
				editorpart.activJsEditor();// 跳转到JS源码页
				IDocument document = editorpart.getJSEditor().getDocumentProvider()
						.getDocument(editorpart.getJSEditor().getEditorInput());
				try {
					int s = document.search(0, text, true, false, false);
					if (s > -1) {
						editorpart.getJSEditor().selectAndReveal(s, text.length());// 设置选中
					} else if (text != null && !"".equals(text)) {
						evenTabledbclick(item, name, text);
					}
				} catch (BadLocationException e) {
					e.printStackTrace();
				}
			}

			@Override
			public void mouseDown(MouseEvent arg0) {
			}

			@Override
			public void mouseUp(MouseEvent arg0) {
			}
		});

		makeActions();

		fGroupAdder = new AdditionGroupAdder();

		updateContextMenuId();

		updateglobal();
	}

	class AdditionGroupAdder implements IMenuListener {
		public void menuAboutToShow(IMenuManager manager) {
			manager.add(addResourseAction);
			manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));

			fillContextMenu(manager);

			IContributionItem[] items = manager.getItems();
			// add configuration's menu items
			IMenuListener listener = getConfiguration().getMenuListener(treeViewer);
			if (listener != null) {
				listener.menuAboutToShow(manager);
				manager.add(new Separator());
			}
			if (items.length > 0 && items[items.length - 1].getId() != null) {
				manager.insertAfter(items[items.length - 1].getId(),
						new GroupMarker(IWorkbenchActionConstants.MB_ADDITIONS));
			} else {
				manager.add(new GroupMarker(IWorkbenchActionConstants.MB_ADDITIONS));
			}

			manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
			Action viewSourseAction = new Action() {
				public void run() {
					editorpart.activhtmlEditor();
				}
			};
			viewSourseAction.setText(Messages.getString("design.action.viewsource"));
			viewSourseAction.setImageDescriptor(ImageDescriptor.createFromImage(StudioPlugin.getIcon("tag.gif")));
			manager.add(viewSourseAction);
		}
	}

	private IMenuListener fGroupAdder = null;
	private Menu fContextMenu;
	private String fContextMenuId;
	private MenuManager fContextMenuManager;

	/**
	 * Updates the outline page's context menu for the current input
	 */
	private void updateContextMenuId() {
		String computedContextMenuId = null;
		// update outline view's context menu control and ID

		computedContextMenuId = computeContextMenuID();

		if (computedContextMenuId == null) {
			computedContextMenuId = OUTLINE_CONTEXT_MENU_ID;
		}

		/*
		 * Update outline context menu id if updating to a new id or if context
		 * menu is not already set up
		 */
		if (!computedContextMenuId.equals(fContextMenuId) || (fContextMenu == null)) {
			fContextMenuId = computedContextMenuId;

			if (treeViewer.getControl() != null && !treeViewer.getControl().isDisposed()) {
				// dispose of previous context menu
				if (fContextMenu != null) {
					fContextMenu.dispose();
				}
				if (fContextMenuManager != null) {
					fContextMenuManager.removeMenuListener(fGroupAdder);
					fContextMenuManager.removeAll();
					fContextMenuManager.dispose();
				}

				fContextMenuManager = new MenuManager(fContextMenuId, fContextMenuId);
				fContextMenuManager.setRemoveAllWhenShown(true);

				fContextMenuManager.addMenuListener(fGroupAdder);

				fContextMenu = fContextMenuManager.createContextMenu(treeViewer.getControl());
				treeViewer.getControl().setMenu(fContextMenu);

				getSite().registerContextMenu(fContextMenuId, fContextMenuManager, treeViewer);

				/*
				 * also register this menu for source page part and structured
				 * text outline view ids
				 */
				String partId = editor.getSite().getId();
				if (partId != null) {
					getSite().registerContextMenu(partId + OUTLINE_CONTEXT_MENU_SUFFIX, fContextMenuManager,
							treeViewer);
				}
				getSite().registerContextMenu(OUTLINE_CONTEXT_MENU_ID, fContextMenuManager, treeViewer);
			}
		}
	}

	public ContentOutlineConfiguration getConfiguration() {
		return cfg;
	}

	private String fInputContentTypeIdentifier = null;

	private String computeContextMenuID() {
		String id = null;
		if (fInputContentTypeIdentifier != null) {
			id = fInputContentTypeIdentifier + OUTLINE_CONTEXT_MENU_SUFFIX;
		}
		return id;
	}

	@Override
	public void dispose() {
		if (treeViewer != null) {
			treeViewer.removeSelectionChangedListener(this);
			treeViewer.removeDoubleClickListener(this);
			treeViewer = null;
		}
		getSite().getWorkbenchWindow().getSelectionService().removeSelectionListener(this);
		super.dispose();
	}

	@Override
	public void init(IEditorSite site, IEditorInput input) {
		super.init(site, input);
		getSite().getWorkbenchWindow().getSelectionService().addSelectionListener(this);
		getSite().setSelectionProvider(new EditorSelectionProvider(this));
	}

	@SuppressWarnings("rawtypes")
	class EditorSelectionProvider implements IPostSelectionProvider {
		/**
		 * Registered selection changed listeners (element type:
		 * <code>ISelectionChangedListener</code>).
		 */
		private ListenerList _listeners = new ListenerList(ListenerList.IDENTITY);

		private ListenerList _postSelectionChangedListeners = new ListenerList(ListenerList.IDENTITY);

		/**
		 * The multi-page editor.
		 */
		private WEBPageDesignEditor _sashEditor;

		/**
		 * Creates a selection provider for the given multi-page editor.
		 * 
		 * @param sashEditor
		 *            the multi-page editor
		 */
		public EditorSelectionProvider(WEBPageDesignEditor sashEditor) {
			Assert.isNotNull(sashEditor);
			this._sashEditor = sashEditor;
		}

		/*
		 * (non-Javadoc) Method declared on <code>ISelectionProvider</code>.
		 */
		@SuppressWarnings("unchecked")
		public void addSelectionChangedListener(ISelectionChangedListener listener) {
			_listeners.add(listener);
		}

		/**
		 * Notifies all registered selection changed listeners that the editor's
		 * selection has changed. Only listeners registered at the time this
		 * method is called are notified.
		 * 
		 * @param event
		 *            the selection changed event
		 */
		public void fireSelectionChanged(final SelectionChangedEvent event) {
			Object[] listeners = this._listeners.getListeners();
			for (int i = 0; i < listeners.length; ++i) {
				final ISelectionChangedListener l = (ISelectionChangedListener) listeners[i];
				SafeRunner.run(new SafeRunnable() {
					public void run() {
						l.selectionChanged(event);
					}
				});
			}
		}

		/**
		 * Returns the sash editor.
		 * 
		 * @return the sash editor part
		 */
		public WEBPageDesignEditor getSashEditor() {
			return _sashEditor;
		}

		/*
		 * (non-Javadoc) Method declared on <code>ISelectionProvider</code>.
		 */
		public ISelection getSelection() {
			if (treeViewer != null)
				return treeViewer.getSelection();
			return new ISelection() {
				@Override
				public boolean isEmpty() {
					return true;
				}
			};
		}

		/*
		 * (non-JavaDoc) Method declaed on <code>ISelectionProvider</code>.
		 */
		public void removeSelectionChangedListener(ISelectionChangedListener listener) {
			_listeners.remove(listener);
		}

		/*
		 * (non-Javadoc) Method declared on <code>ISelectionProvider</code>.
		 */
		public void setSelection(ISelection selection) {
			// if(treeViewer!=null)
			// treeViewer.setSelection(selection);
		}

		@SuppressWarnings("unchecked")
		public void addPostSelectionChangedListener(ISelectionChangedListener listener) {
			_postSelectionChangedListeners.add(listener);
		}

		public void removePostSelectionChangedListener(ISelectionChangedListener listener) {
			_postSelectionChangedListeners.remove(listener);
		}

		/**
		 * Notifies any post selection listeners that a post selection event has
		 * been received. Only listeners registered at the time this method is
		 * called are notified.
		 * 
		 * @param event
		 *            a selection changed event
		 * 
		 * @see #addPostSelectionChangedListener(ISelectionChangedListener)
		 */
		public void firePostSelectionChanged(final SelectionChangedEvent event) {
			Object[] listeners = _postSelectionChangedListeners.getListeners();
			for (int i = 0; i < listeners.length; ++i) {
				final ISelectionChangedListener l = (ISelectionChangedListener) listeners[i];
				SafeRunnable.run(new SafeRunnable() {
					public void run() {
						l.selectionChanged(event);
					}
				});
			}
		}
	}

	private void selectElement(ISelection selection) {
		if (selection instanceof IStructuredSelection) {
			Object[] Selections = ((IStructuredSelection) selection).toArray();
			if (Selections.length > 0) {
				Object object = Selections[0];
				if (object instanceof ElementStyleImpl) {
					ElementStyleImpl element = (ElementStyleImpl) object;
					ElementAttributeTab.setAttributeTab(table, element);
					ElementAttributeTab.setEventTab(etable, element);
					addparam.setEnabled(true);

					if ("head".equals(element.getTagName().toLowerCase())) {
						addResourseAction.setEnabled(true);
					} else {
						addResourseAction.setEnabled(false);
					}
				}
			}
		}
	}

	private boolean _selftsel = false;

	@Override
	public void selectionChanged(IWorkbenchPart part, ISelection selection) {
		if (selection != null && !selection.isEmpty()) {
			if (treeViewer != null && !_selftsel) {
				if (selection.getClass() != StructuredSelection.class) {
					treeViewer.setSelection(selection);
				}
			}
			_selftsel = false;
		}
	}

	public boolean isEmpty(String str) {
		return str == null || "".equals(str);
	}

	@Override
	public void selectionChanged(SelectionChangedEvent event) {
		_selftsel = true;
		updateglobal();
		if (event.getSelection().isEmpty())
			return;
		selectElement(event.getSelection());
		int start = -1;
		int length = 0;
		if (event.getSelection() instanceof IStructuredSelection) {
			ISelection current = editorpart.getSourceEditor().getSelectionProvider().getSelection();
			if (current instanceof IStructuredSelection) {
				Object[] currentSelection = ((IStructuredSelection) current).toArray();
				Object[] newSelection = ((IStructuredSelection) event.getSelection()).toArray();
				if (!Arrays.equals(currentSelection, newSelection)) {
					if (newSelection.length > 0) {
						/*
						 * No ordering is guaranteed for multiple selection
						 */
						Object o = newSelection[0];
						if (o instanceof IndexedRegion) {
							start = ((IndexedRegion) o).getStartOffset();
							int end = ((IndexedRegion) o).getEndOffset();
							if (newSelection.length > 1) {
								for (int i = 1; i < newSelection.length; i++) {
									start = Math.min(start, ((IndexedRegion) newSelection[i]).getStartOffset());
									end = Math.max(end, ((IndexedRegion) newSelection[i]).getEndOffset());
								}
								length = end - start;
							}
						} else if (o instanceof ITextRegion) {
							start = ((ITextRegion) o).getStart();
							int end = ((ITextRegion) o).getEnd();
							if (newSelection.length > 1) {
								for (int i = 1; i < newSelection.length; i++) {
									start = Math.min(start, ((ITextRegion) newSelection[i]).getStart());
									end = Math.max(end, ((ITextRegion) newSelection[i]).getEnd());
								}
								length = end - start;
							}
						} else if (o instanceof IRegion) {
							start = ((IRegion) o).getOffset();
							int end = start + ((IRegion) o).getLength();
							if (newSelection.length > 1) {
								for (int i = 1; i < newSelection.length; i++) {
									start = Math.min(start, ((IRegion) newSelection[i]).getOffset());
									end = Math.max(end, ((IRegion) newSelection[i]).getOffset()
											+ ((IRegion) newSelection[i]).getLength());
								}
								length = end - start;
							}
						}
					}
				}
			}
		} else if (event.getSelection() instanceof ITextSelection) {
			start = ((ITextSelection) event.getSelection()).getOffset();
		}
		if (start > -1) {
			editorpart.getSourceEditor().selectAndReveal(start, length);
		}
	}

	@Override
	public void doubleClick(DoubleClickEvent event) {
		_selftsel = true;
		if (event.getSelection().isEmpty())
			return;
		int start = -1;
		int length = 0;
		if (event.getSelection() instanceof IStructuredSelection) {
			ISelection currentSelection = editorpart.getSourceEditor().getSelectionProvider().getSelection();
			if (currentSelection instanceof IStructuredSelection) {
				Object current = ((IStructuredSelection) currentSelection).toArray();
				Object newSelection = ((IStructuredSelection) event.getSelection()).toArray();
				if (!current.equals(newSelection)) {
					IStructuredSelection selection = (IStructuredSelection) event.getSelection();
					Object o = selection.getFirstElement();
					if (o instanceof IndexedRegion) {
						start = ((IndexedRegion) o).getStartOffset();
						length = ((IndexedRegion) o).getEndOffset() - start;
					} else if (o instanceof ITextRegion) {
						start = ((ITextRegion) o).getStart();
						length = ((ITextRegion) o).getEnd() - start;
					} else if (o instanceof IRegion) {
						start = ((ITextRegion) o).getStart();
						length = ((ITextRegion) o).getLength();
					}
				}
			}
		} else if (event.getSelection() instanceof ITextSelection) {
			start = ((ITextSelection) event.getSelection()).getOffset();
			length = ((ITextSelection) event.getSelection()).getLength();
		}
		if (start > -1) {
			editorpart.getSourceEditor().selectAndReveal(start, length);
			// 双击跳到源码页
			editorpart.activhtmlEditor();
		}
	}

}