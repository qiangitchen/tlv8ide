package com.tulin.v8.webtools.ide.css.editors;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.views.contentoutline.ContentOutlinePage;
import org.w3c.css.sac.InputSource;
import org.w3c.dom.css.CSSRuleList;
import org.w3c.dom.css.CSSStyleRule;
import org.w3c.dom.css.CSSStyleSheet;
import org.w3c.dom.css.CSSValue;

import com.steadystate.css.parser.CSSOMParser;
import com.tulin.v8.webtools.ide.WebToolsPlugin;
import com.tulin.v8.webtools.ide.css.CSSStyleProp;
import com.tulin.v8.webtools.ide.utils.StringUtils;

/**
 * CSS大纲内容页
 * 
 * @author chenqian
 */
public class CSSOutlinePage extends ContentOutlinePage {
	private CSSEditor editor;
	private CSSRuleList cssRuleList;
	private List<CSSStyleRule> ruleList = new ArrayList<>();
	private Map<CSSStyleProp, CSSStyleRule> propMap = new LinkedHashMap<>();
	private CSSSelectionChangedListener selectionChangedListener;

	public CSSOutlinePage(CSSEditor editor) {
		super();
		this.editor = editor;
	}

	@Override
	public void createControl(Composite parent) {
		super.createControl(parent);
		TreeViewer viewer = getTreeViewer();
		viewer.setContentProvider(new CSSContentProvider());
		viewer.setLabelProvider(new CSSLabelProvider());
		selectionChangedListener = new CSSSelectionChangedListener();
		viewer.addSelectionChangedListener(selectionChangedListener);
		viewer.setInput(ruleList);
		update();
	}

	public void update() {
		if (getControl() == null || getControl().isDisposed()) {
			return;
		}
		try {
			CSSOMParser parser = new CSSOMParser();
			InputSource is = new InputSource(
					new StringReader(editor.getDocumentProvider().getDocument(editor.getEditorInput()).get()));
			CSSStyleSheet stylesheet = parser.parseStyleSheet(is, null, null);
			ruleList.clear();
			propMap.clear();
			cssRuleList = stylesheet.getCssRules();
			for (int i = 0; i < cssRuleList.getLength(); i++) {
				if (cssRuleList.item(i) instanceof CSSStyleRule) {
					CSSStyleRule rule = (CSSStyleRule) cssRuleList.item(i);
					ruleList.add(rule);
				}
			}
		} catch (Throwable t) {
		}
		TreeViewer viewer = getTreeViewer();
		if (viewer != null && !viewer.isBusy()) {
			viewer.refresh();
		}
	}

	@SuppressWarnings("rawtypes")
	private class CSSContentProvider implements ITreeContentProvider {
		public Object[] getChildren(Object parentElement) {
			if (parentElement instanceof ArrayList) {
				return ((List) parentElement).toArray();
			} else if (parentElement instanceof CSSStyleRule) {
				CSSStyleRule rule = (CSSStyleRule) parentElement;
				Object[] childs = getProps(rule);
				if (childs.length > 0) {
					return childs;
				}
				CSSStyleProp[] props = new CSSStyleProp[rule.getStyle().getLength()];
				for (int j = 0; j < rule.getStyle().getLength(); j++) {
					String property = rule.getStyle().item(j);
					CSSStyleProp prop = new CSSStyleProp();
					prop.setProperty(property);
					CSSValue value = rule.getStyle().getPropertyCSSValue(property);
					prop.setValue(value);
					prop.setParent(rule);
					props[j] = prop;
					propMap.put(prop, rule);
				}
				return props;
			}
			return new Object[0];
		}

		private Object[] getProps(CSSStyleRule rule) {
			List<CSSStyleProp> list = new ArrayList<>();
			for (CSSStyleProp prop : propMap.keySet()) {
				if (propMap.get(prop) == rule) {
					list.add(prop);
				}
			}
			return list.toArray();
		}

		public Object getParent(Object element) {
			if (element instanceof CSSStyleRule) {
				return ruleList;
			} else if (element instanceof CSSStyleProp) {
				CSSStyleProp prop = (CSSStyleProp) element;
				return prop.getParent();
			}
			return null;
		}

		public boolean hasChildren(Object element) {
			if (getChildren(element).length == 0) {
				return false;
			} else {
				return true;
			}
		}

		public Object[] getElements(Object inputElement) {
			return getChildren(inputElement);
		}

		public void dispose() {
		}

		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		}
	}

	private class CSSSelectionChangedListener implements ISelectionChangedListener {
		public void selectionChanged(SelectionChangedEvent event) {
			IStructuredSelection sel = (IStructuredSelection) event.getSelection();
			String element = null;
			int start = 0;
			String docText = cssRuleList.toString();
			Object firelement = sel.getFirstElement();
			if (firelement instanceof CSSStyleRule) {
				CSSStyleRule rule = (CSSStyleRule) firelement;
				element = rule.getSelectorText();
				start = docText.indexOf(rule.toString());
			} else if (firelement instanceof CSSStyleProp) {
				CSSStyleProp prop = (CSSStyleProp) firelement;
				element = prop.getProperty();
				start = docText.indexOf(prop.getParent().toString());
				int offset = prop.getOffset();
				if (offset > 0) {
					editor.selectAndReveal(offset, 0);
					return;
				}
			}
			if (element != null) {
				String text = editor.getDocumentProvider().getDocument(editor.getEditorInput()).get();
				int offset = text.indexOf(element, start);
				if (offset >= 0) {
					editor.selectAndReveal(offset, 0);
				}
			}
		}
	}

	private class CSSLabelProvider extends LabelProvider {
		@Override
		public Image getImage(Object element) {
			if (element instanceof CSSStyleRule) {
				return WebToolsPlugin.getDefault().getImageRegistry().get(WebToolsPlugin.ICON_CSS_RULE);
			}
			return WebToolsPlugin.getDefault().getImageRegistry().get(WebToolsPlugin.ICON_CSS_PROP);
		}

		@Override
		public String getText(Object element) {
			if (element instanceof CSSStyleRule) {
				CSSStyleRule rule = (CSSStyleRule) element;
				return rule.getSelectorText();
			} else if (element instanceof CSSStyleProp) {
				CSSStyleProp prop = (CSSStyleProp) element;
				return prop.getProperty();
			} else {
				return element.toString();
			}
		}
	}

	public void setSelection(int offset) {
		try {
			getTreeViewer().removeSelectionChangedListener(selectionChangedListener);
			StructuredSelection selection = findNode(offset);
			if (selection != null) {
				setSelection(selection);
			}
			getTreeViewer().addSelectionChangedListener(selectionChangedListener);
		} catch (Exception ex) {
		}
	}

	private StructuredSelection findNode(int offset) {
		String text = editor.getSelectionStartLineText();
		if (text != null) {
			text = text.trim();
			if (StringUtils.isNotEmpty(text)) {
				String docText = cssRuleList.toString();
				if (text.indexOf("{") > 0) {
					CSSStyleRule selrule = null;
					for (CSSStyleRule rule : ruleList) {
						if (isme(rule, offset, docText)) {
							selrule = rule;
						}
					}
					if (selrule != null) {
						return new StructuredSelection(selrule);
					}
				} else if (text.indexOf(":") > 0) {
					String name = text.substring(0, text.indexOf(":")).trim();
					String value = text.substring(text.indexOf(":") + 1).replace(";", "").trim();
					CSSStyleProp selprop = null;
					for (CSSStyleProp prop : propMap.keySet()) {
						CSSStyleRule rule = prop.getParent();
						String property = prop.getProperty().trim();
						String csstext = prop.getValue().getCssText().trim();
						if (property.equals(name) && value.equals(csstext) && isme(rule, offset, docText)) {
							prop.setOffset(offset);
							selprop = prop;
						}
					}
					if (selprop != null) {
						return new StructuredSelection(selprop);
					}
				}
			}
		}
		return null;
	}

	private boolean isme(CSSStyleRule rule, int offset, String docText) {
		String cssText = rule.toString();
		int idx = docText.indexOf(cssText);
		return idx < offset;
	}
}
