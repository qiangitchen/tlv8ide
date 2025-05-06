package com.tulin.v8.echarts.ui.editors.echt;

import java.util.List;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.editor.FormPage;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;

import com.tulin.v8.core.Sys;
import com.tulin.v8.core.TuLinPlugin;
import com.tulin.v8.echarts.ui.editors.echt.action.RefreshAction;
import com.tulin.v8.echarts.ui.editors.echt.call.GetScriptCallJava;
import com.tulin.v8.echarts.ui.editors.echt.call.GetThemeCallJava;
import com.tulin.v8.echarts.ui.editors.echt.call.LoadSourceCallJava;
import com.tulin.v8.echarts.ui.utils.WebappManager;
import com.tulin.v8.swt.chromium.Browser;

public class EchartsDesignPage extends FormPage {
	EchartsEditor editor;

	private String sourceText;

	private ModleParse pasr;

	private Tree tree;
	private Table proptable;

	private Browser browser;

	private Document document;

	private RefreshAction refreshAction;

	private PropertyEditorManager propermanager;

	public EchartsDesignPage(EchartsEditor editor) {
		super(editor, "EchartsDesign", Messages.getString("editors.EchartsEditor.3"));
		this.editor = editor;
		sourceText = editor.getSource();
	}

	/**
	 * 创建右键菜单
	 */
	protected void makeActions() {
		refreshAction = new RefreshAction(browser);
	}

	protected void createFormContent(final IManagedForm managedForm) {
		final ScrolledForm form = managedForm.getForm();
		GridLayout layout = new GridLayout();
		form.getBody().setLayout(layout);

		FormToolkit toolkit = managedForm.getToolkit();
		Section section = toolkit.createSection(form.getBody(), Section.NO_TITLE);

		Composite composite = toolkit.createComposite(section, SWT.FILL);
		section.setClient(composite);

		section.setText(Messages.getString("editors.EchartsEditor.4"));
		section.setDescription(Messages.getString("editors.EchartsEditor.5"));

		GridData gd = new GridData(GridData.FILL_BOTH);
		section.setLayoutData(gd);

		composite.setLayout(new GridLayout());
		composite.setLayoutData(new GridData(GridData.FILL_BOTH));

		SashForm sashForm = new SashForm(composite, SWT.NONE);
		GridData comlayout = new GridData(GridData.FILL_BOTH);
		// comlayout.grabExcessVerticalSpace = true;
		sashForm.setLayoutData(comlayout);

		Section section1 = toolkit.createSection(sashForm, Section.TITLE_BAR);
		section1.setText(Messages.getString("editors.EchartsEditor.8"));
		tree = new Tree(section1, SWT.V_SCROLL | GridData.FILL_VERTICAL | SWT.BORDER);
		section1.setClient(tree);
		section1.setLayoutData(new GridData(GridData.FILL_VERTICAL));

		SashForm child = new SashForm(sashForm, SWT.VERTICAL);
		GridData childcomlayout = new GridData(GridData.FILL_BOTH);
		// childcomlayout.grabExcessHorizontalSpace = true;
		sashForm.setLayoutData(childcomlayout);

		Section section2 = toolkit.createSection(child, Section.TITLE_BAR);
		section2.setText(Messages.getString("editors.EchartsEditor.9"));
		Composite client = toolkit.createComposite(section2, SWT.FILL);
		client.setLayout(new GridLayout(1, false));
		client.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		browser = new Browser(client, SWT.FILL);
		browser.setLayoutData(new GridData(GridData.FILL_BOTH));
		browser.setJavascriptEnabled(true);
		new LoadSourceCallJava(browser, "loadSourceCall", this);
		new GetThemeCallJava(browser, "getThemeCall", this);
		new GetScriptCallJava(browser, "getScriptCall", this);
		MenuManager menuMgr = new MenuManager("#PopupMenu");
		menuMgr.setRemoveAllWhenShown(true);
		menuMgr.addMenuListener(new IMenuListener() {
			public void menuAboutToShow(IMenuManager manager) {
				manager.removeAll();
				manager.add(refreshAction);
				manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
				// manager.add(viewSourseAction);
			}
		});
		Menu menu = menuMgr.createContextMenu(browser);
		browser.setMenu(menu);
		section2.setClient(client);
		GridData bseinf = new GridData(GridData.FILL_BOTH);
		section2.setLayoutData(bseinf);

		Section section3 = toolkit.createSection(child, Section.DESCRIPTION | Section.TITLE_BAR);
		section3.setText(Messages.getString("editors.EchartsEditor.14"));
		Composite paramgp = toolkit.createComposite(section3, SWT.WRAP);
		paramgp.setLayout(new GridLayout(1, false));
		paramgp.setLayoutData(new GridData(GridData.FILL_BOTH));
		proptable = new Table(paramgp, SWT.FILL | SWT.FULL_SELECTION | SWT.BORDER);
		proptable.setHeaderVisible(true);
		proptable.setLinesVisible(true);
		proptable.setLayoutData(new GridData(GridData.FILL_BOTH));
		TableColumn name = new TableColumn(proptable, SWT.NONE);
		name.setText(Messages.getString("TLEditor.EchartsEditor.1"));
		name.setWidth(200);
		TableColumn value = new TableColumn(proptable, SWT.NONE);
		value.setWidth(600);
		value.setText(Messages.getString("TLEditor.EchartsEditor.2"));
		section3.setClient(paramgp);
		section3.setLayoutData(new GridData(GridData.FILL_BOTH));

		child.setWeights(new int[] { 3, 2 });
		sashForm.setWeights(new int[] { 1, 4 });

		tree.addMouseListener(new MouseListener() {
			@Override
			public void mouseDoubleClick(MouseEvent arg0) {
				TreeItem[] selection = tree.getSelection();
				TreeItem item = selection[0];
				if (item.getExpanded()) {
					item.setExpanded(false);
				} else {
					item.setExpanded(true);
				}
			}

			@Override
			public void mouseDown(MouseEvent arg0) {
			}

			@Override
			public void mouseUp(MouseEvent arg0) {
			}
		});

		tree.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				TreeItem treeitem = (TreeItem) e.item;
				Element element = (Element) treeitem.getData();
				List<Attribute> attrs = element.attributes();
				propermanager.removeAll();
				proptable.removeAll();
				for (Attribute attr : attrs) {
					String name = attr.getName();
					TableItem item = new TableItem(proptable, SWT.BORDER);
					item.setText(new String[] { name, attr.getValue() });
					item.setData(element);
					propermanager.addEditor(proptable, item);
				}
				String eltext = element.getText();
				if (eltext != null && !"".equals(eltext)) {
					TableItem item = new TableItem(proptable, SWT.BORDER);
					item.setText(new String[] { "text", eltext });
					item.setData(element);
					propermanager.addEditor(proptable, item);
				}
			}
		});

		propermanager = new PropertyEditorManager(this);

		makeActions();
	}

	void addEditor(final TableItem patitem) {

	}

	/**
	 * 属性编辑
	 */
	@SuppressWarnings("deprecation")
	protected void proptypeEdited(TableItem item, String pvalue) {
		Element pele = (Element) item.getData();
		String pname = item.getText(0);
		if (pvalue == null) {
			pvalue = "";
		}
		if ("text".equals(pname)) {
			String bpvalue = pele.getText();
			if (bpvalue == null) {
				bpvalue = "";
			}
			if (!pvalue.equals(bpvalue)) {
				pele.setText(pvalue);
				editor.setSource(document.asXML());
				browser.refresh();
			}
		} else {
			String bpvalue = pele.attributeValue(pname);
			if (bpvalue == null) {
				bpvalue = "";
			}
			if (!pvalue.equals(bpvalue)) {
				if (!"".equals(pvalue)) {
					pele.setAttributeValue(pname, pvalue);
				} else {
					pele.setAttributeValue(pname, "");
				}
				editor.setSource(document.asXML());
				browser.refresh();
			}
		}
		item.setData(pele);
	}

	public void loadModle() {
		try {
			String source = editor.getSource();
			document = DocumentHelper.parseText(source);
			pasr = new ModleParse(document);

			propermanager.removeAll();
			proptable.removeAll();
			tree.removeAll();

			Element root = document.getRootElement();
			List<Element> eles = root.elements();
			for (int i = 0; i < eles.size(); i++) {
				Element element = eles.get(i);
				TreeItem treeitem = new TreeItem(tree, SWT.NONE);
				treeitem.setText(element.getName());
				if ("data".equals(element.getName())) {
					treeitem.setImage(TuLinPlugin.getIcon("datas.gif"));
				} else {
					treeitem.setImage(TuLinPlugin.getIcon("element.gif"));
				}
				treeitem.setData(element);
				loadChildModle(treeitem, element);
			}
		} catch (Exception e) {
			Sys.packErrMsg(e.getMessage());
		}
	}

	private void loadChildModle(TreeItem treeitem, Element ele) {
		List<Element> eles = ele.elements();
		for (int i = 0; i < eles.size(); i++) {
			Element element = eles.get(i);
			TreeItem titem = new TreeItem(treeitem, SWT.NONE);
			titem.setText(element.getName());
			titem.setImage(TuLinPlugin.getIcon("attribute.gif"));
			titem.setData(element);
		}
	}

	public void loadBrowser() {
		try {
			String designUrl = WebappManager.getDesignUrlURL();
			browser.setUrl(designUrl);
			browser.setVisible(true);
		} catch (Exception e) {
			Sys.packErrMsg(e.getMessage());
		}
	}

	public Document getDocument() {
		return document;
	}

	public ModleParse getModlepasr() {
		String stext = editor.getSource();
		if (!sourceText.equals(stext)) {
			pasr = new ModleParse(stext);
			sourceText = stext;
		}
		return pasr;
	}

	public EchartsEditor getEditor() {
		return editor;
	}

}
