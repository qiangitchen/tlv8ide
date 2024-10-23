package com.tulin.v8.webtools.ide.views;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.jface.text.DocumentEvent;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentListener;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IPartListener;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.ui.texteditor.ITextEditor;
import org.w3c.css.sac.InputSource;
import org.w3c.dom.css.CSSRule;
import org.w3c.dom.css.CSSRuleList;
import org.w3c.dom.css.CSSStyleRule;
import org.w3c.dom.css.CSSStyleSheet;

import com.steadystate.css.parser.CSSOMParser;
import com.tulin.v8.webtools.ide.WebToolsPlugin;

/**
 * The CSS Preview View.
 */
public class CSSPreviewView extends ViewPart implements IPartListener, IDocumentListener {

	private ITextEditor currentEditor;
	private Browser browser;

	private void editorChanged() {
		if (currentEditor != null) {
			try {
				currentEditor.getDocumentProvider().getDocument(currentEditor.getEditorInput())
						.removeDocumentListener(this);
			} catch (NullPointerException ex) {
				// ignore
			}
		}
		IWorkbenchPage page = getSite().getPage();
		if (page == null) {
			return;
		}
		IEditorPart editor = page.getActiveEditor();
		if (editor == null) {
			return;
		}
		if (editor.getEditorInput().getName().endsWith(".css")) {
			if (editor instanceof ITextEditor) {
				currentEditor = (ITextEditor) editor;
			} else {
				currentEditor = (ITextEditor) editor.getAdapter(ITextEditor.class);
			}
			if (currentEditor != null) {
				IDocument doc = currentEditor.getDocumentProvider().getDocument(currentEditor.getEditorInput());
				doc.addDocumentListener(this);
				refreshPreview();
			}
		}
	}

	private void refreshPreview() {
		if (currentEditor != null) {
			String source = currentEditor.getDocumentProvider().getDocument(currentEditor.getEditorInput()).get();
			String html = buildHTML(source);
			browser.setText(html);
		}
	}

	/**
	 * Generates HTML to preview Stylesheet.
	 *
	 * @param source the CSS source
	 * @return HTML
	 */
	private String buildHTML(String source) {
		List<String> selectors = new ArrayList<String>();
		try {
			CSSOMParser parser = new CSSOMParser();
			InputSource is = new InputSource(new StringReader(source));
			CSSStyleSheet stylesheet = parser.parseStyleSheet(is, null, null);
			CSSRuleList list = stylesheet.getCssRules();
			for (int i = 0; i < list.getLength(); i++) {
				CSSRule rule = list.item(i);
				if (rule instanceof CSSStyleRule) {
					CSSStyleRule styleRule = (CSSStyleRule) rule;
					String selector = styleRule.getSelectorText();
					selectors.add(selector);
				}
			}
		} catch (IOException ex) {
			WebToolsPlugin.logException(ex);
		}

		boolean table = false;
		Set<String> pseudo = new HashSet<String>();

		StringBuffer sb = new StringBuffer();
		sb.append("<html><head><style type=\"text/css\">\n");
		sb.append(source);
		sb.append("</style></head><body>\n");
		for (int i = 0; i < selectors.size(); i++) {
			String selector = (String) selectors.get(i);
			if (selector.indexOf(' ') >= 0) {
				// TODO descendant selector

			} else if (selector.indexOf('>') >= 0) {
				// TODO child selector

			} else if (selector.indexOf('+') >= 0) {
				// TODO adjacent sibling selector

			} else if (selector.indexOf('[') >= 0) {
				// TODO attribute selector

			} else if (selector.indexOf('.') >= 0) {
				// class selector
				String[] dim = selector.split("\\.");
				createElement(sb, selector, dim[0], dim[1], null);

			} else if (selector.indexOf('#') >= 0) {
				// id selector
				String[] dim = selector.split("#");
				createElement(sb, selector, dim[0], null, dim[1]);

			} else if (selector.indexOf(":") >= 0) {
				// pseudo-classes
				String[] dim = selector.split(":");
				if (!pseudo.contains(dim[0])) {
					createElement(sb, dim[0], dim[0], null, null);
					pseudo.add(dim[0]);
				}
			} else {
				// type selector
				if (selector.equals("body")) {
					continue;
				}
				if (selector.equalsIgnoreCase("table") || selector.equalsIgnoreCase("tr")
						|| selector.equalsIgnoreCase("th") || selector.equalsIgnoreCase("td")) {
					if (!table) {
						sb.append("<table>\n");
						sb.append("  <tr>\n");
						sb.append("    <th>header</th>\n");
						sb.append("    <th>header</th>\n");
						sb.append("    <th>header</th>\n");
						sb.append("  </tr>\n");
						sb.append("  <tr>\n");
						sb.append("    <td>1-1</td>\n");
						sb.append("    <td>1-2</td>\n");
						sb.append("    <td>1-3</td>\n");
						sb.append("  </tr>\n");
						sb.append("  <tr>\n");
						sb.append("    <td>2-1</td>\n");
						sb.append("    <td>2-2</td>\n");
						sb.append("    <td>2-3</td>\n");
						sb.append("  </tr>\n");
						sb.append("</table>\n");
						table = true;
					}
					continue;
				}
				createElement(sb, selector, selector, null, null);
			}
		}
		sb.append("</body></html>");
		return sb.toString();
	}

	private void createElement(StringBuffer sb, String selector, String elementName, String className, String idName) {
		if (elementName.equals("*")) {
			elementName = "div";
		}
		if (elementName.equalsIgnoreCase("a")) {
			sb.append("<a href=\"#\"");
			if (className != null) {
				sb.append(" class=\"").append(className).append("\"");
			}
			if (idName != null) {
				sb.append(" id=\"").append(idName).append("\"");
			}
			sb.append(">Link</a>\n");

		} else {
			sb.append("<").append(elementName);
			if (className != null) {
				sb.append(" class=\"").append(className).append("\"");
			}
			if (idName != null) {
				sb.append(" id=\"").append(idName).append("\"");
			}
			sb.append(">");
			if (elementName.equalsIgnoreCase("hr")) {
				return;
			}
			sb.append(selector);
			sb.append("</").append(elementName).append(">\n");
		}
	}

	public void createPartControl(Composite parent) {
		this.browser = new Browser(parent, SWT.NULL);
		this.browser.setLayoutData(new GridData(GridData.FILL_BOTH));

		IWorkbenchPage page = getSite().getPage();
		page.addPartListener(this);
		editorChanged();
	}

	public void setFocus() {
		this.browser.setFocus();
	}

	/////////////////////////////////////////////////////////////
	// IPartListener methods
	/////////////////////////////////////////////////////////////
	public void partActivated(IWorkbenchPart part) {
		editorChanged();
	}

	public void partBroughtToTop(IWorkbenchPart part) {
		editorChanged();
	}

	public void partClosed(IWorkbenchPart part) {
	}

	public void partDeactivated(IWorkbenchPart part) {
	}

	public void partOpened(IWorkbenchPart part) {
		editorChanged();
	}

	/////////////////////////////////////////////////////////////
	// IDocumentListener methods
	/////////////////////////////////////////////////////////////
	public void documentAboutToBeChanged(DocumentEvent event) {
//		refreshPreview();
	}

	public void documentChanged(DocumentEvent event) {
		refreshPreview();
	}

}
