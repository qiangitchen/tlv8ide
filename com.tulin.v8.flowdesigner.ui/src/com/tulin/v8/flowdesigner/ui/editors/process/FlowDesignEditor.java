package com.tulin.v8.flowdesigner.ui.editors.process;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.operations.IOperationHistory;
import org.eclipse.core.commands.operations.ObjectUndoContext;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.IWorkbenchCommandConstants;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.IHandlerActivation;
import org.eclipse.ui.handlers.IHandlerService;
import org.eclipse.ui.operations.UndoRedoActionGroup;
import org.eclipse.ui.part.EditorPart;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.tulin.v8.core.Sys;
import com.tulin.v8.core.utils.CommonUtil;
import com.tulin.v8.core.utils.IDUtils;
import com.tulin.v8.flowdesigner.server.utils.WebappManager;
import com.tulin.v8.flowdesigner.ui.Activator;
import com.tulin.v8.flowdesigner.ui.editors.Messages;
import com.tulin.v8.flowdesigner.ui.editors.process.action.CatAction;
import com.tulin.v8.flowdesigner.ui.editors.process.action.CopyAction;
import com.tulin.v8.flowdesigner.ui.editors.process.action.DeleteAction;
import com.tulin.v8.flowdesigner.ui.editors.process.action.PasteAction;
import com.tulin.v8.flowdesigner.ui.editors.process.element.ConditionPropertys;
import com.tulin.v8.flowdesigner.ui.editors.process.element.IProperty;
import com.tulin.v8.flowdesigner.ui.editors.process.element.IPropertys;
import com.tulin.v8.flowdesigner.ui.editors.process.element.LinePropertys;
import com.tulin.v8.flowdesigner.ui.editors.process.element.NodeOvalPropertys;
import com.tulin.v8.flowdesigner.ui.editors.process.element.NodePropertys;
import com.tulin.v8.flowdesigner.ui.editors.process.element.ProcessDrawElement;
import com.tulin.v8.flowdesigner.ui.editors.process.listener.BreowserTitleListener;
import com.tulin.v8.flowdesigner.ui.editors.process.listener.BrowserProgressListener;
import com.tulin.v8.flowdesigner.ui.editors.process.listener.BrowserStatusTextListener;
import com.tulin.v8.flowdesigner.ui.editors.process.listener.LEndAdpater;
import com.tulin.v8.flowdesigner.ui.editors.process.listener.LForkAdpater;
import com.tulin.v8.flowdesigner.ui.editors.process.listener.LLineAdpater;
import com.tulin.v8.flowdesigner.ui.editors.process.listener.LPolineAdpater;
import com.tulin.v8.flowdesigner.ui.editors.process.listener.LSWDownAdpater;
import com.tulin.v8.flowdesigner.ui.editors.process.listener.LSWLeftAdpater;
import com.tulin.v8.flowdesigner.ui.editors.process.listener.LSWRightAdpater;
import com.tulin.v8.flowdesigner.ui.editors.process.listener.LSWUpAdpater;
import com.tulin.v8.flowdesigner.ui.editors.process.listener.LSelectAdpater;
import com.tulin.v8.flowdesigner.ui.editors.process.listener.LSetLockkAdpater;
import com.tulin.v8.flowdesigner.ui.editors.process.listener.LStartAdpater;
import com.tulin.v8.flowdesigner.ui.editors.process.listener.LbusActivityAdpater;

/**
 * 流程设计器
 * 
 * @author chenqian
 *
 */
public class FlowDesignEditor extends EditorPart {
	private ProcessEditor processeditor;
	private SashForm sashForm;
	public Tree tree;
	public Tree propertree;

	public CatAction catAction;
	public CopyAction copyAction;
	public PasteAction pasteAction;
	public DeleteAction deleteAction;

	public Browser browser;

	private PropertyEditorManager propermanager;
	private Map<String, TreeItem> elementItem = new HashMap<String, TreeItem>();

	private String InitsourceText = "";
	private String sourceText = "";

	private ToolItem lockitem;

	private Clipboard clipbd = Toolkit.getDefaultToolkit().getSystemClipboard();

	public FlowDesignEditor(ProcessEditor processeditor) {
		this.processeditor = processeditor;
	}

	@Override
	public void doSave(IProgressMonitor monitor) {
		if (getEditorInput() instanceof ProcessEditorInput) {
			ProcessEditorInput einput = (ProcessEditorInput) getEditorInput();
			if (einput.saveData(sourceText)) {
				InitsourceText = sourceText;
				firePropertyChange(PROP_DIRTY);
			}
		}
	}

	@Override
	public void doSaveAs() {
		// TODO 自动生成的方法存根
	}

	@Override
	public void init(IEditorSite site, IEditorInput input) throws PartInitException {
		setSite(site);
		setInput(input);
		if (getEditorInput() instanceof ProcessEditorInput) {
			ProcessEditorInput edinput = (ProcessEditorInput) getEditorInput();
			ProcessDrawElement element = edinput.getElement();
			element.reload();
			sourceText = element.getSprocessacty();
			if ("".equals(sourceText.trim())) {
				sourceText = "{\"id\":\"" + element.getSprocessid() + "\",\"name\":\"" + element.getSprocessname()
						+ "\",\"count\":0}";
			}
			InitsourceText = sourceText;
		}
		initializeOperationHistory();
	}

	@Override
	public boolean isDirty() {
		return !InitsourceText.equals(sourceText);
	}

	@Override
	public boolean isSaveAsAllowed() {
		// TODO 自动生成的方法存根
		return false;
	}

	/*
	 * 重置结构树
	 */
	public void clearElementItem() {
		elementItem = new HashMap<String, TreeItem>();
	}

	public void removeElementItem(String elementId) {
		elementItem.remove(elementId);
	}

	// 设置Element map
	public void setElementItem(String elementId, TreeItem item) {
		elementItem.put(elementId, item);
	}

	// 根据ID取Element
	public TreeItem getElementItem(String elementId) {
		return elementItem.get(elementId);
	}

	private void fillContextMenu(IMenuManager manager) {
		manager.add(catAction);
		manager.add(copyAction);
		manager.add(pasteAction);
		manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
		manager.add(deleteAction);

		if (tree.getSelection().length > 0) {
			catAction.setEnabled(true);
			copyAction.setEnabled(true);
		} else {
			catAction.setEnabled(false);
			copyAction.setEnabled(false);
		}
		changePasteHandler();
	}

	/*
	 * 控制粘贴按钮状态
	 */
	public void changePasteHandler() {
		try {
			Transferable clipT = clipbd.getContents(null);
			if (clipT != null) {
				String context = (String) clipT.getTransferData(DataFlavor.stringFlavor);
				new JSONObject(context);
				pasteAction.setEnabled(true);
			} else {
				pasteAction.setEnabled(false);
			}
		} catch (Exception e) {
			pasteAction.setEnabled(false);
		}
		if (!pasteAction.isEnabled()) {
			if (pasteActivation != null) {
				handlerService.deactivateHandler(pasteActivation);
				pasteActivation = null;
			}
		} else {
			pasteActivation = handlerService.activateHandler(IWorkbenchCommandConstants.EDIT_PASTE, pasteHanler);
		}
	}

	private void makeActions() {
		deleteAction = new DeleteAction(this);
		catAction = new CatAction(clipbd, this);
		copyAction = new CopyAction(clipbd, this);
		pasteAction = new PasteAction(clipbd, this);
	}

	private IHandlerService handlerService;
	private IHandlerActivation removeActivation;
	private AbstractHandler removeHanler;
	private IHandlerActivation copyActivation;
	private AbstractHandler copyHanler;
	private IHandlerActivation cutActivation;
	private AbstractHandler cutHanler;
	private IHandlerActivation pasteActivation;
	private AbstractHandler pasteHanler;

	/*
	 * 初始化全局命令
	 */
	private void hookGlobalHanlers() {
		handlerService = (IHandlerService) getSite().getService(IHandlerService.class);
		removeHanler = new AbstractHandler() {
			@Override
			public Object execute(ExecutionEvent event) throws ExecutionException {
				deleteAction.run();
				return true;
			}
		};
		copyHanler = new AbstractHandler() {
			@Override
			public Object execute(ExecutionEvent event) throws ExecutionException {
				copyAction.run();
				return clipbd;
			}
		};
		cutHanler = new AbstractHandler() {
			@Override
			public Object execute(ExecutionEvent event) throws ExecutionException {
				catAction.run();
				return clipbd;
			}
		};
		pasteHanler = new AbstractHandler() {
			@Override
			public Object execute(ExecutionEvent event) throws ExecutionException {
				pasteAction.run();
				return true;
			}
		};
		tree.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent event) {
				TreeItem treeitem = (TreeItem) event.item;
				if (treeitem == null) {
					changeRemoveHanler(false);
				} else {
					changeRemoveHanler(true);
				}
			}
		});
	}

	/**
	 * @category 改变“编辑/删除”按钮状态
	 * @param active
	 */
	public void changeRemoveHanler(boolean active) {
		if (!active) {
			if (removeActivation != null) {
				handlerService.deactivateHandler(removeActivation);
				removeActivation = null;
			}
			if (copyActivation != null) {
				handlerService.deactivateHandler(copyActivation);
				copyActivation = null;
			}
			if (cutActivation != null) {
				handlerService.deactivateHandler(cutActivation);
				cutActivation = null;
			}
		} else {
			removeActivation = handlerService.activateHandler(IWorkbenchCommandConstants.EDIT_DELETE, removeHanler);
			copyActivation = handlerService.activateHandler(IWorkbenchCommandConstants.EDIT_COPY, copyHanler);
			cutActivation = handlerService.activateHandler(IWorkbenchCommandConstants.EDIT_CUT, cutHanler);
		}
	}

	@Override
	public void createPartControl(Composite parent) {
		sashForm = new SashForm(parent, SWT.FILL);
		// sashForm.setLayoutData(new GridData(GridData.FILL_BOTH));

		SashForm sash = new SashForm(sashForm, SWT.VERTICAL | SWT.FILL);
		sash.setLayoutData(new GridData(GridData.FILL_BOTH));
		tree = new Tree(sash, SWT.BORDER | SWT.V_SCROLL);

		// 右键菜单
		tree.addListener(SWT.MenuDetect, new Listener() {
			public void handleEvent(Event event) {
				MenuManager menuMgr = new MenuManager("#PopupMenu");
				menuMgr.setRemoveAllWhenShown(true);
				menuMgr.addMenuListener(new IMenuListener() {
					public void menuAboutToShow(IMenuManager manager) {
						manager.removeAll();
						fillContextMenu(manager);
					}
				});
				Menu menu = menuMgr.createContextMenu(tree);
				tree.setMenu(menu);
				TreeItem[] selection = tree.getSelection();
				if (selection.length > 0) {
					deleteAction.setEnabled(true);
				} else {
					deleteAction.setEnabled(false);
				}
			}
		});

		tree.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				TreeItem treeitem = (TreeItem) e.item;
				try {
					JSONObject json = (JSONObject) treeitem.getData();
					browser.execute("selectSinglNodeById('" + json.getString("id") + "');");
					setPropertyTable(json);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});

		final TabFolder tabFolder = new TabFolder(sash, SWT.NONE);

		TabItem tabItemAr = new TabItem(tabFolder, SWT.NONE);
		tabItemAr.setText(Messages.getString("TLEditor.WEBDesign.1"));
		propertree = new Tree(tabFolder, SWT.BORDER | SWT.FULL_SELECTION);
		propertree.setHeaderVisible(true);
		propertree.setLinesVisible(true);
		propertree.setLayoutData(new GridData(GridData.FILL_BOTH));
		TreeColumn name = new TreeColumn(propertree, SWT.NONE);
		name.setText(Messages.getString("TLEditor.WEBDesign.3"));
		name.setWidth(110);
		TreeColumn value = new TreeColumn(propertree, SWT.NONE);
		value.setWidth(150);
		value.setText(Messages.getString("TLEditor.WEBDesign.4"));
		tabItemAr.setControl(propertree);

		// 设置属性表格行高
		propertree.addListener(SWT.MeasureItem, new Listener() {
			public void handleEvent(Event event) {
				event.height = 26;
			}
		});

		// final TabItem tabItemEv = new TabItem(tabFolder, SWT.NONE);
		// tabItemEv.setText(Messages.getString("TLEditor.WEBDesign.5"));
		// etable = new Table(tabFolder, SWT.BORDER | SWT.FULL_SELECTION);
		// etable.setHeaderVisible(true);
		// etable.setLinesVisible(true);
		// TableColumn Evname = new TableColumn(etable, SWT.NONE);
		// Evname.setText(Messages.getString("TLEditor.WEBDesign.6"));
		// Evname.setWidth(100);
		// TableColumn Evvalue = new TableColumn(etable, SWT.NONE);
		// Evvalue.setWidth(100);
		// Evvalue.setText(Messages.getString("TLEditor.WEBDesign.7"));
		// tabItemEv.setControl(etable);

		sash.setWeights(new int[] { 1, 1 });

		Composite composite = new Composite(sashForm, SWT.FILL);
		composite.setLayout(new GridLayout(1, false));
		composite.setLayoutData(new GridData(GridData.FILL_BOTH));

		ToolBar localToolBar = new ToolBar(composite, SWT.FLAT | SWT.WRAP | SWT.RIGHT | SWT.BAR);
		localToolBar.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		ToolItem localToolItem1 = new ToolItem(localToolBar, SWT.PUSH);
		localToolItem1.setText(Messages.getString("TLEditor.ProcessEditor.1"));
		localToolItem1.setToolTipText(Messages.getString("TLEditor.ProcessEditor.1"));
		localToolItem1.addSelectionListener(new LSelectAdpater(this));
		localToolItem1.setImage(Activator.getIcon("process_icons/select.gif"));
		localToolItem1.setData("select");
		new ToolItem(localToolBar, 2);
		ToolItem localToolItem2 = new ToolItem(localToolBar, SWT.PUSH);
		localToolItem2.setText(Messages.getString("TLEditor.ProcessEditor.2"));
		localToolItem2.setToolTipText(Messages.getString("TLEditor.ProcessEditor.2"));
		localToolItem2.addSelectionListener(new LStartAdpater(this));
		localToolItem2.setImage(Activator.getIcon("process_icons/start.png"));
		localToolItem2.setData("start");

		ToolItem localToolItem3 = new ToolItem(localToolBar, SWT.PUSH);
		localToolItem3.setText(Messages.getString("TLEditor.ProcessEditor.3"));
		localToolItem3.setToolTipText(Messages.getString("TLEditor.ProcessEditor.3"));
		localToolItem3.addSelectionListener(new LEndAdpater(this));
		localToolItem3.setImage(Activator.getIcon("process_icons/end.png"));
		localToolItem3.setData("end");

		new ToolItem(localToolBar, 2);

		ToolItem localToolItem4 = new ToolItem(localToolBar, SWT.PUSH);
		localToolItem4.setText(Messages.getString("TLEditor.ProcessEditor.4"));
		localToolItem4.setToolTipText(Messages.getString("TLEditor.ProcessEditor.4"));
		localToolItem4.addSelectionListener(new LbusActivityAdpater(this));
		localToolItem4.setImage(Activator.getIcon("process_icons/biz.png"));
		localToolItem4.setData("businessActivity");

		// ToolItem localToolItem5 = new ToolItem(localToolBar, SWT.PUSH);
		// localToolItem5.setText("自动环节");
		// localToolItem5.setToolTipText("自动环节");
		// localToolItem5.addSelectionListener(new LbusActivityAdpater(this));
		// localToolItem5.setImage(StudioPlugin
		// .getIcon("process_icons/autoactivity.png"));
		// localToolItem5.setData("autoactivity");

		new ToolItem(localToolBar, 2);

		// ToolItem localToolItem6 = new ToolItem(localToolBar, SWT.PUSH);
		// localToolItem6.setText("条件环节");
		// localToolItem6.setToolTipText("条件环节");
		// localToolItem6.setImage(StudioPlugin
		// .getIcon("process_icons/cond.png"));
		// localToolItem6.setData("conditionActivity");

		ToolItem localToolItem7 = new ToolItem(localToolBar, SWT.PUSH);
		localToolItem7.setText(Messages.getString("TLEditor.ProcessEditor.5"));
		localToolItem7.setToolTipText(Messages.getString("TLEditor.ProcessEditor.5"));
		localToolItem7.addSelectionListener(new LForkAdpater(this));
		localToolItem7.setImage(Activator.getIcon("process_icons/fork.png"));
		localToolItem7.setData("conditionBranch");

		new ToolItem(localToolBar, 2);

		// ToolItem localToolItem8 = new ToolItem(localToolBar, SWT.PUSH);
		// localToolItem8.setText("AND逻辑环节");
		// localToolItem8.setToolTipText("AND逻辑环节");
		// localToolItem8.setImage(StudioPlugin
		// .getIcon("process_icons/and.png"));
		// localToolItem8.setData("and");
		// ToolItem localToolItem9 = new ToolItem(localToolBar, SWT.PUSH);
		// localToolItem9.setText("XOR逻辑环节");
		// localToolItem9.setToolTipText("XOR逻辑环节");
		// localToolItem9.setImage(StudioPlugin
		// .getIcon("process_icons/xor.png"));
		// localToolItem9.setData("xor");
		// new ToolItem(localToolBar, 2);

		ToolItem localToolItem10 = new ToolItem(localToolBar, SWT.PUSH);
		localToolItem10.setText(Messages.getString("TLEditor.ProcessEditor.6"));
		localToolItem10.setToolTipText(Messages.getString("TLEditor.ProcessEditor.6"));
		localToolItem10.addSelectionListener(new LLineAdpater(this));
		localToolItem10.setImage(Activator.getIcon("process_icons/forward.gif"));
		localToolItem10.setData("connection");
		ToolItem localToolItem101 = new ToolItem(localToolBar, SWT.PUSH);
		localToolItem101.setText(Messages.getString("TLEditor.ProcessEditor.7"));
		localToolItem101.setToolTipText(Messages.getString("TLEditor.ProcessEditor.7"));
		localToolItem101.addSelectionListener(new LPolineAdpater(this));
		localToolItem101.setImage(Activator.getIcon("process_icons/transition.png"));
		localToolItem101.setData("connection");

		new ToolItem(localToolBar, 2);

		ToolItem localToolItem11 = new ToolItem(localToolBar, SWT.RADIO);
		localToolItem11.addSelectionListener(new LSWDownAdpater(this));
		localToolItem11.setImage(Activator.getIcon("process_icons/down.png"));
		localToolItem11.setSelection(true);
		ToolItem localToolItem12 = new ToolItem(localToolBar, SWT.RADIO);
		localToolItem12.addSelectionListener(new LSWRightAdpater(this));
		localToolItem12.setImage(Activator.getIcon("process_icons/right.png"));
		ToolItem localToolItem13 = new ToolItem(localToolBar, SWT.RADIO);
		localToolItem13.addSelectionListener(new LSWUpAdpater(this));
		localToolItem13.setImage(Activator.getIcon("process_icons/up.png"));
		ToolItem localToolItem14 = new ToolItem(localToolBar, SWT.RADIO);
		localToolItem14.addSelectionListener(new LSWLeftAdpater(this));
		localToolItem14.setImage(Activator.getIcon("process_icons/left.png"));

		new ToolItem(localToolBar, 2);

		lockitem = new ToolItem(localToolBar, SWT.CHECK);
		lockitem.setToolTipText(Messages.getString("TLEditor.ProcessEditor.8"));
		lockitem.setSelection(true);
		lockitem.addSelectionListener(new LSetLockkAdpater(this, lockitem));
		lockitem.setImage(Activator.getIcon("process_icons/lock.gif"));

		browser = new Browser(composite, SWT.FILL | SWT.BORDER);
		browser.setLayoutData(new GridData(GridData.FILL_BOTH));
		browser.setJavascriptEnabled(true);
		browser.addProgressListener(new BrowserProgressListener(this));
		browser.addStatusTextListener(new BrowserStatusTextListener(this, browser));
		browser.addTitleListener(new BreowserTitleListener(this, browser));
//		new CallJava(browser, "EditorCallJava", tree, this);
//		new DataInitedCallJava(browser, "dataInitedCall", tree, this);
//		new DesignChangeCallJava(browser, "designChangeCall", tree, this);
//		new SelectedCallJava(browser, "selectedCall", tree, this);
		browser.setMenu(createEditPopup());// 右键菜单

		sashForm.setWeights(new int[] { 2, 6 });

		propermanager = new PropertyEditorManager(this);

		makeActions();
		hookGlobalHanlers();
	}

	/*
	 * 调用JS函数
	 */
	public String callJsFunction(String paramString) {
		return "" + browser.evaluate(paramString);
	}

	public String getSourceText() {
		if (sourceText != null && !"".equals(sourceText)) {
			return sourceText;
		}
		return "";
	}

	/*
	 * 页面初始化完成时回调
	 */
	public void pageDataInited() {
		JSONObject json = new JSONObject();
		try {
			String sources = getSourceText();
			json = new JSONObject(sources);
		} catch (Exception e) {
			try {
				json.put("id", IDUtils.getGUID());
				json.put("name", "newFlow");
			} catch (JSONException e1) {
				e1.printStackTrace();
			}
		}
		try {
			browser.execute("pageDataInited('" + URLEncoder.encode(json.toString(), "UTF-8") + "');");
			lockitem.setSelection(true);// 默认锁定
		} catch (Exception e) {
			Sys.printErrMsg(Messages.getString("TLEditor.ProcessEditor.9") + e.toString());
		}
	}

	public void loadBrowser(String jsons) {
		sourceText = jsons;
		String designUrl = WebappManager.getSVGDesignerURL();
		if (CommonUtil.isWinOS() && CommonUtil.getOSVersion() <= 6.1) {
			designUrl = WebappManager.getVMLDesignerURL();
		}
		browser.setUrl(designUrl);
		browser.setVisible(true);
	}

	public void pfirePropertyChange(int propertyId) {
		firePropertyChange(propertyId);
	}

	public void setSourceText(String jsons) {
		if (!sourceText.equals(jsons)) {
			firePropertyChange(PROP_DIRTY);
		}
		sourceText = jsons;
		processeditor.setSourceText(jsons);
	}

	/*
	 * 创建一个右键菜单 通过样式值SWT.POP_UP来创建一个右键弹出菜单
	 */
	private Menu createEditPopup() {
		MenuManager menuMgr = new MenuManager("#PopupMenu");
		menuMgr.setRemoveAllWhenShown(true);
		menuMgr.addMenuListener(new IMenuListener() {
			public void menuAboutToShow(IMenuManager manager) {
				manager.removeAll();
				Action select = new Action() {
					@Override
					public void run() {
						callJsFunction("MenuAction.line(false);");
					}
				};
				select.setText(Messages.getString("TLEditor.ProcessEditor.1"));
				select.setImageDescriptor(Activator.getImageDescriptor("icons/process_icons/select.gif"));
				manager.add(select);
				MenuManager openmenu = new MenuManager(Messages.getString("TLEditor.ProcessEditor.0"));
				manager.add(openmenu);
				Action addstart = new Action() {
					@Override
					public void run() {
						callJsFunction("MenuAction.start();");
					}
				};
				addstart.setText(Messages.getString("TLEditor.ProcessEditor.2"));
				addstart.setImageDescriptor(Activator.getImageDescriptor("icons/process_icons/start.png"));
				openmenu.add(addstart);
				Action addactivity = new Action() {
					@Override
					public void run() {
						callJsFunction("MenuAction.nodeRect();");
					}
				};
				addactivity.setText(Messages.getString("TLEditor.ProcessEditor.4"));
				addactivity.setImageDescriptor(Activator.getImageDescriptor("icons/process_icons/biz.png"));
				openmenu.add(addactivity);
				Action addafork = new Action() {
					@Override
					public void run() {
						callJsFunction("MenuAction.fork();");
					}
				};
				addafork.setText(Messages.getString("TLEditor.ProcessEditor.5"));
				addafork.setImageDescriptor(Activator.getImageDescriptor("icons/process_icons/fork.png"));
				openmenu.add(addafork);
				Action addend = new Action() {
					@Override
					public void run() {
						callJsFunction("MenuAction.end();");
					}
				};
				addend.setText(Messages.getString("TLEditor.ProcessEditor.3"));
				addend.setImageDescriptor(Activator.getImageDescriptor("icons/process_icons/end.png"));
				openmenu.add(addend);
				MenuManager linkmenu = new MenuManager(Messages.getString("TLEditor.ProcessEditor.11"));
				manager.add(linkmenu);
				Action linkline = new Action() {
					@Override
					public void run() {
						callJsFunction("MenuAction.line(true);");
					}
				};
				linkline.setText(Messages.getString("TLEditor.ProcessEditor.6"));
				linkline.setImageDescriptor(Activator.getImageDescriptor("icons/process_icons/forward.gif"));
				linkmenu.add(linkline);
				Action linkpoline = new Action() {
					@Override
					public void run() {
						callJsFunction("MenuAction.polyline(true);");
					}
				};
				linkpoline.setText(Messages.getString("TLEditor.ProcessEditor.7"));
				linkpoline.setImageDescriptor(Activator.getImageDescriptor("icons/process_icons/transition.png"));
				linkmenu.add(linkpoline);

				manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
				fillContextMenu(manager);
				browser.execute("JavaevIsselected()");

				manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
				Action ulock = new Action() {
					@Override
					public void run() {
						boolean selected = !lockitem.getSelection();
						callJsFunction("MenuAction.setLock(" + selected + ");");
						lockitem.setSelection(selected);
					}
				};
				ulock.setText(Messages.getString("TLEditor.ProcessEditor.8"));
				manager.add(ulock);
			}
		});
		return menuMgr.createContextMenu(browser);
	}

	/*
	 * 属性页内容
	 */
	public void setPropertyTable(JSONObject data) {
		try {
			IPropertys currentProper = null;
			propermanager.removeAll();
			propertree.removeAll();
			if (data == null)
				return;
			propertree.setData("id", data.getString("id"));
			propertree.setData("json", data);
			String uproperty = data.getString("property");
			if (uproperty == null || "null".equals(uproperty)) {
				data.put("property", new JSONArray());
			}
			JSONArray arry = data.getJSONArray("property");
			if ("line".equals(data.getString("type"))) {
				currentProper = new LinePropertys(arry);
				propertree.setData("list", currentProper);
				List<IProperty> lProper = currentProper.getProperty();
				for (IProperty property : lProper) {
					String pvalue = property.getValue();
					if ("l_p_id".equals(property.getId())) {
						pvalue = data.getString("id");
					}
					if ("l_p_pre".equals(property.getId())) {
						pvalue = data.getString("from");
					}
					if ("l_p_next".equals(property.getId())) {
						pvalue = data.getString("to");
					}
					TreeItem patitem = new TreeItem(propertree, SWT.BORDER);
					patitem.setText(new String[] { property.getLabel(), pvalue });
					property.setValue(pvalue);
					patitem.setData(property);
					propermanager.addEditor(propertree, patitem, property);
				}
			}
			if ("node".equals(data.getString("type"))) {
				currentProper = new NodePropertys(arry);
				propertree.setData("list", currentProper);
				List<IProperty> lProper = currentProper.getProperty();
				for (IProperty property : lProper) {
					String pvalue = property.getValue();
					if ("n_p_id".equals(property.getId())) {
						pvalue = data.getString("id");
					}
					if ("n_p_name".equals(property.getId())) {
						pvalue = data.getString("name");
					}
					TreeItem patitem = new TreeItem(propertree, SWT.BORDER);
					patitem.setText(new String[] { property.getLabel(), pvalue });
					property.setValue(pvalue);
					patitem.setData(property);
					propermanager.addEditor(propertree, patitem, property);
				}
			}
			if ("start".equals(data.getString("type")) || "end".equals(data.getString("type"))) {
				currentProper = new NodeOvalPropertys(arry);
				propertree.setData("list", currentProper);
				List<IProperty> lProper = currentProper.getProperty();
				for (IProperty property : lProper) {
					String pvalue = property.getValue();
					if ("n_p_id".equals(property.getId())) {
						pvalue = data.getString("id");
					}
					if ("n_p_name".equals(property.getId())) {
						pvalue = data.getString("name");
					}
					TreeItem patitem = new TreeItem(propertree, SWT.BORDER);
					patitem.setText(new String[] { property.getLabel(), pvalue });
					property.setValue(pvalue);
					patitem.setData(property);
					propermanager.addEditor(propertree, patitem, property);
				}
			}
			if ("condition".equals(data.getString("type"))) {
				currentProper = new ConditionPropertys(arry);
				propertree.setData("list", currentProper);
				List<IProperty> lProper = currentProper.getProperty();
				for (IProperty property : lProper) {
					String pvalue = property.getValue();
					if ("c_p_id".equals(property.getId())) {
						pvalue = data.getString("id");
					}
					if ("c_p_name".equals(property.getId())) {
						pvalue = data.getString("name");
					}
					TreeItem patitem = new TreeItem(propertree, SWT.BORDER);
					patitem.setText(new String[] { property.getLabel(), pvalue });
					property.setValue(pvalue);
					patitem.setData(property);
					propermanager.addEditor(propertree, patitem, property);
				}
			}
		} catch (Exception e) {
		}
	}

	@Override
	public void setFocus() {
		// TODO 自动生成的方法存根
	}

	private ObjectUndoContext undoContext;
	private AskUserApprover approver;

	public IOperationHistory getOperationHistory() {
		// This script can also be used to retrive operationHistory
		// IOperationHistory operationHistory =
		// OperationHistoryFactory.getOperationHistory();
		return PlatformUI.getWorkbench().getOperationSupport().getOperationHistory();

	}

	public void initializeOperationHistory() {
		if (undoContext == null) {
			undoContext = new ObjectUndoContext(this);
			approver = new AskUserApprover(undoContext);
			getOperationHistory().addOperationApprover(approver);
			// 不设置重做步骤上限
			// int limit = 10;
			// getOperationHistory().setLimit(undoContext, limit);
			UndoRedoActionGroup undoRedoGroup = new UndoRedoActionGroup(getSite(), undoContext, true);
			IActionBars actionBars = ((IEditorSite) getSite()).getActionBars();
			undoRedoGroup.fillActionBars(actionBars);
		}
	}

	/*
	 * 记录内容变化
	 */
	public void fireAllPropertyChange(String bfstr, String afstr) {
		firePropertyChange(PROP_DIRTY);
		DrowOperation operation = new DrowOperation("flowdrow", this, bfstr, afstr);
		try {
			operation.addContext(undoContext);
			getOperationHistory().execute(operation, null, null);
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
	}

}