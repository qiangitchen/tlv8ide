package com.tulin.v8.ide.ui.editors.page.design;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.io.File;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
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
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
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
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.editor.FormPage;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.wst.sse.core.internal.provisional.IStructuredModel;
import org.eclipse.wst.sse.ui.StructuredTextEditor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import com.tulin.v8.ide.StudioPlugin;
import com.tulin.v8.ide.Sys;
import com.tulin.v8.ide.common.WebServer;
import com.tulin.v8.ide.ui.editors.Messages;
import com.tulin.v8.ide.ui.editors.page.DesignerStructuredTextEditorJSP;
import com.tulin.v8.ide.ui.editors.page.PageEditorInterface;
import com.tulin.v8.ide.ui.editors.page.design.action.AddResourseAction;
import com.tulin.v8.ide.ui.editors.page.design.action.CatAction;
import com.tulin.v8.ide.ui.editors.page.design.action.CopyAction;
import com.tulin.v8.ide.ui.editors.page.design.action.DeleteAction;
import com.tulin.v8.ide.ui.editors.page.design.action.PasteAction;
import com.tulin.v8.ide.ui.editors.page.design.action.RefreshAction;
import com.tulin.v8.ide.ui.editors.page.design.action.ViewSourseAction;
import com.tulin.v8.ide.ui.editors.page.design.dialog.AddPropotypeDialog;
import com.tulin.v8.ide.ui.editors.page.design.listener.DAuthenticationListener;
import com.tulin.v8.ide.ui.editors.page.design.listener.DCloseWindowListener;
import com.tulin.v8.ide.ui.editors.page.design.listener.DLocationListener;
import com.tulin.v8.ide.ui.editors.page.design.listener.DOpenWindowListener;
import com.tulin.v8.ide.ui.editors.page.design.listener.DProgressListener;
import com.tulin.v8.ide.ui.editors.page.design.listener.DStatusTextListener;
import com.tulin.v8.ide.ui.editors.page.design.listener.DTitleListener;
import com.tulin.v8.ide.utils.StudioConfig;

/**
 * @Des 网页设计器(预览效果)
 * @author 陈乾
 */
@SuppressWarnings({ "restriction", "deprecation" })
public class WEBDesignEditor extends FormPage implements WEBDesignEditorInterface {
	protected StructuredTextEditor editor;
	protected PageEditorInterface editorpart;

	protected Tree tree;
	protected TreeItem root1;
	protected TreeItem root2;

	protected Document pageDom;

	public Action viewSourseAction;

	private SashForm sashForm;
	private Browser browser;

	private AddResourseAction addResourseAction;
	private CatAction catAction;
	private CopyAction copyAction;
	private PasteAction pasteAction;
	private RefreshAction refreshAction;
	private DeleteAction deleteAction;

	boolean design = false;
	private Map<String, TreeItem> elementItem = new HashMap<String, TreeItem>();

	Clipboard clipbd = Toolkit.getDefaultToolkit().getSystemClipboard();
	public String editPage;
	private Table table;
	private Table etable;
	private ToolItem addparam;

	public WEBDesignEditor(StructuredTextEditor editor, PageEditorInterface editorpart) {
		super("webDesign", Messages.getString("TLEditor.pageEditor.3"));
		this.editor = editor;
		this.editorpart = editorpart;
	}

	public void fillContextMenu(IMenuManager manager) {
		manager.add(catAction);
		manager.add(copyAction);
		manager.add(pasteAction);
		pasteAction.updateState();
		manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
		manager.add(deleteAction);
	}

	/**
	 * 创建右键菜单
	 */
	public void makeActions() {
		addResourseAction = new AddResourseAction(tree, this);
		catAction = new CatAction(tree, editorpart, clipbd, editor);
		copyAction = new CopyAction(tree, clipbd);
		pasteAction = new PasteAction(tree, clipbd, this);
		viewSourseAction = new ViewSourseAction(editorpart, tree);
		deleteAction = new DeleteAction(tree, editor, editorpart);
		refreshAction = new RefreshAction(browser);

	}

	public void createPartControl(Composite parent) {
		sashForm = new SashForm(parent, SWT.NONE);
		FormData formData = new FormData();
		formData.left = new FormAttachment(0, 3);
		formData.right = new FormAttachment(100, -3);
		formData.top = new FormAttachment(0, 0);
		formData.bottom = new FormAttachment(100, -25);
		sashForm.setLayoutData(formData);

		SashForm sash = new SashForm(sashForm, SWT.VERTICAL);
		FormData formData1 = new FormData();
		formData1.left = new FormAttachment(0, 3);
		formData1.right = new FormAttachment(0, -3);
		formData1.top = new FormAttachment(0, -5);
		formData1.bottom = new FormAttachment(100, -0);
		sash.setLayoutData(formData1);
		tree = new Tree(sash, SWT.BORDER | SWT.V_SCROLL);
		root1 = new TreeItem(tree, SWT.NONE);
		root2 = new TreeItem(tree, SWT.NONE);
		root1.setText("head");
		root1.setImage(PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_OBJ_FOLDER));
		root2.setText("body");
		root2.setImage(PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_OBJ_FOLDER));

		tree.addMouseListener(new MouseListener() {
			@Override
			public void mouseDoubleClick(MouseEvent arg0) {
				TreeItem[] selection = tree.getSelection();
				TreeItem item = selection[0];
				if ("head".equals(item.getText()) || "body".equals(item.getText())) {
					if (item.getExpanded()) {
						item.setExpanded(false);
					} else {
						item.setExpanded(true);
					}
				} else {
					editorpart.activhtmlEditor();// 跳转到源码页
					String text = ((Element) item.getData()).outerHtml();
					IDocument document = editor.getDocumentProvider().getDocument(editor.getEditorInput());
					try {
						int s = document.search(0, text, true, false, false);
						editor.selectAndReveal(s, text.length());// 设置选中
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

		// 右键菜单
		tree.addListener(SWT.MenuDetect, new Listener() {
			public void handleEvent(Event event) {
				MenuManager menuMgr = new MenuManager("#PopupMenu");
				menuMgr.setRemoveAllWhenShown(true);
				menuMgr.addMenuListener(new IMenuListener() {
					public void menuAboutToShow(IMenuManager manager) {
						manager.removeAll();
						manager.add(addResourseAction);
						manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
						WEBDesignEditor.this.fillContextMenu(manager);
						manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
						manager.add(viewSourseAction);
					}
				});
				Menu menu = menuMgr.createContextMenu(tree);
				tree.setMenu(menu);
				TreeItem[] selection = tree.getSelection();
				TreeItem item = selection[0];
				if ("head".equals(item.getText())) {
					addResourseAction.setEnabled(true);
				} else {
					addResourseAction.setEnabled(false);
				}
				if ("head".equals(item.getText()) || "body".equals(item.getText())) {
					catAction.setEnabled(false);
					copyAction.setEnabled(false);
					deleteAction.setEnabled(false);
				} else {
					catAction.setEnabled(true);
					copyAction.setEnabled(true);
					deleteAction.setEnabled(true);
				}
				DataFlavor[] dataflow = clipbd.getAvailableDataFlavors();
				if (dataflow.length > 0) {
					pasteAction.setEnabled(true);
				} else {
					pasteAction.setEnabled(false);
				}
			}
		});

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

		browser = new Browser(sashForm, SWT.BORDER | SWT.FILL);
		browser.setJavascriptEnabled(true);
		browser.addAuthenticationListener(new DAuthenticationListener(editorpart, this));
		browser.addCloseWindowListener(new DCloseWindowListener(editorpart, this));
		browser.addLocationListener(new DLocationListener(editorpart, this));
		browser.addOpenWindowListener(new DOpenWindowListener(editorpart, this));
		browser.addProgressListener(new DProgressListener(editorpart, this));
		browser.addStatusTextListener(new DStatusTextListener(editorpart, this));
		browser.addTitleListener(new DTitleListener(editorpart, this));

		MenuManager menuMgr = new MenuManager("#PopupMenu");
		menuMgr.setRemoveAllWhenShown(true);
		menuMgr.addMenuListener(new IMenuListener() {
			public void menuAboutToShow(IMenuManager manager) {
				manager.removeAll();
				WEBDesignEditor.this.fillContextMenu(manager);
				manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
				manager.add(refreshAction);
				manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
				manager.add(viewSourseAction);
			}
		});
		Menu menu = menuMgr.createContextMenu(browser);
		browser.setMenu(menu);

		sashForm.setWeights(new int[] { 1, 4 });

		tree.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				TreeItem treeitem = (TreeItem) e.item;
				Element element = (Element) treeitem.getData();
				AttributeTab.setAttributeTab(table, element);
				AttributeTab.setEventTab(etable, element);
				addparam.setEnabled(true);
				String elementId = element.attr("id");
				if (elementId != null && !"".equals(elementId)) {
					browser.execute("selectElement('" + elementId + "');");
				}
			}
		});

		// 属性表
		table.addMouseListener(new MouseListener() {
			@Override
			public void mouseDoubleClick(MouseEvent arg0) {
				TableItem[] items = table.getSelection();
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
			Element element = (Element) item.getData();
			String component = element.attr("component");
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
		Element pele = (Element) item.getData();
		String pname = item.getText(0);
		String bpvalue = pele.attr(pname);
		if (bpvalue == null) {
			bpvalue = "";
		}
		String pvalue = text.getText();
		if (pvalue == null) {
			pvalue = "";
		}
		if (!pvalue.equals(bpvalue)) {
			if (!"".equals(pvalue)) {
				pele.attr(pname, pvalue);
			} else {
				pele.attr(pname, "");
			}
			editorpart.setSourcePageText(pageDom.html());
		}
		item.setData(pele);
	}

	/**
	 * 事件编辑
	 */
	public void eventEdited(TableItem item, Text text) {
		Element pele = (Element) item.getData();
		String evname = item.getText(0);
		String bevale = pele.attr(evname);
		if (bevale == null) {
			bevale = "";
		}
		String evalue = text.getText();
		if (evalue == null) {
			evalue = "";
		}
		if (!evalue.equals(bevale)) {
			if (!"".equals(evalue)) {
				pele.attr(evname, evalue);
			} else {
				if (pele.hasAttr(evname)) {
					pele.removeAttr(evname);
				}
			}
			editorpart.setSourcePageText(pageDom.html());
		}
		item.setData(pele);
	}

	public void loadBrowser() {
		if (design) {
			return;
		}
		String codeBase = StudioConfig.getStudioAppRootPath() + "/webdesign/";
		String projectpath = "";
		String editorpage = "";
		try {
			FileEditorInput editinput = (FileEditorInput) editor.getEditorInput();
			editorpage = editinput.getPath().toString();
			IProject project = editinput.getFile().getProject();
			projectpath = project.getLocation().toString();
		} catch (Exception e) {
			editorpage = editor.getEditorInput().getToolTipText();
		}
		try {
			String sourceUrl = "index.html";
			if (editorpage.indexOf("/mobileUI/") > 0) {
				sourceUrl = "mindex.html";
			}
			String designUrl = WebServer.getDefaultWebServer().getResourcePathURL(codeBase, sourceUrl);
			editPage = WebServer.getDefaultWebServer().getResourcePathURL(projectpath,
					editorpage.substring(projectpath.length()));
			designUrl = designUrl + "?content=" + URLEncoder.encode(editPage, "UTF-8");
			browser.setUrl(designUrl);
			browser.refresh();
			browser.setVisible(true);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}

	public Document setModel() {
		if (design) {
			return null;
		}
		String editorText = editor.getDocumentProvider().getDocument(getEditorInput()).get();
		File f = this.getFile();
		try {
			try {
				// 将字符串转换为Document对象
				pageDom = Jsoup.parse(editorText);
			} catch (Exception e) {
				pageDom = Jsoup.parse(f, "utf-8");
			}
			root1.removeAll();
			ReadHTML.readHead(root1, pageDom, this);
			root2.removeAll();
			ReadHTML.readBody(root2, pageDom, this);
		} catch (Exception e) {
			Sys.printErrMsg(Messages.getString("TLEditor.WEBDesign.model19") + e.toString());
			e.printStackTrace();
		}
		try {
			tree.select(root1);
			// 不会触发select事件，所以联动处理
			AttributeTab.setAttributeTab(table, (Element) root1.getData());
			AttributeTab.setEventTab(etable, (Element) root1.getData());
		} catch (Exception e) {
		}
		return pageDom;
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
		IStructuredModel model = null;
		if (this.editor != null) {
			model = ((DesignerStructuredTextEditorJSP) this.editor).getModel();
		}
		return model;
	}

	public Tree getElementTree() {
		return tree;
	}

	/**
	 * 获取网页浏览器
	 */
	public Browser getBrowser() {
		return browser;
	}

	/**
	 * 设置Element map
	 */
	public void setElementItem(String elementId, TreeItem item) {
		elementItem.put(elementId, item);
	}

	public TreeItem getElementItem(String elementId) {
		return elementItem.get(elementId);
	}

	public void selectElement(String elementId) {
		TreeItem item = elementItem.get(elementId);
		// tree.select(item);
		if (item != null) {
			tree.setSelection(item);
			// 不会触发select事件，所以联动处理
			AttributeTab.setAttributeTab(table, (Element) item.getData());
			AttributeTab.setEventTab(etable, (Element) item.getData());
		}
	}

	public void addPropotype() {
		TreeItem[] selectItems = tree.getSelection();
		if (selectItems.length == 0)
			return;
		AddPropotypeDialog dialog = new AddPropotypeDialog(getSite().getShell());
		if (dialog.open() == IDialogConstants.OK_ID) {
			String fName = dialog.getSname();
			String fValue = dialog.getSvalue();
			if (selectItems.length > 0) {
				TreeItem treeItem = selectItems[0];
				Element element = (Element) treeItem.getData();
				if (element.hasAttr(fName)) {
					MessageDialog.openError(getSite().getShell(), Messages.getString("TLEditor.WEBDesign.addprotype.4"),
							Messages.getString("TLEditor.WEBDesign.addprotype.6"));
					return;
				}
				element.attr(fName, fValue);
				TableItem item = new TableItem(table, SWT.BORDER);
				item.setText(new String[] { fName, fValue });
				item.setData(element);
				editorpart.setSourcePageText(pageDom.html());
			} else {
				MessageDialog.openError(getSite().getShell(), Messages.getString("TLEditor.WEBDesign.addprotype.4"),
						Messages.getString("TLEditor.WEBDesign.addprotype.7"));
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
		deleteAction.run();
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
		return pageDom;
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

}
