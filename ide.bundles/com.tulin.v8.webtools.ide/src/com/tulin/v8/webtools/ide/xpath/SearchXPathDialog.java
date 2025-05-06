package com.tulin.v8.webtools.ide.xpath;

import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.text.TextSelection;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IEditorPart;

import com.tulin.v8.fuzzyxml.FuzzyXMLDocument;
import com.tulin.v8.fuzzyxml.FuzzyXMLNode;
import com.tulin.v8.fuzzyxml.FuzzyXMLParser;
import com.tulin.v8.fuzzyxml.XPath;
import com.tulin.v8.webtools.ide.WebToolsPlugin;
import com.tulin.v8.webtools.ide.html.HTMLUtil;
import com.tulin.v8.webtools.ide.html.editors.HTMLSourceEditor;
import com.tulin.v8.webtools.ide.views.IPaletteTarget;

public class SearchXPathDialog extends Window {

	private Text xpath;
	private Button next;
	private Button prev;
	private Label status;

	public SearchXPathDialog(Shell parentShell) {
		super(parentShell);
	}

	@Override
	protected Control createContents(Composite parent) {
		getShell().setText(WebToolsPlugin.getResourceString("XMLEditor.XPathSearch"));

		Composite composite = new Composite(parent, SWT.NULL);
		composite.setLayout(new GridLayout(2, false));
		composite.setLayoutData(new GridData(GridData.FILL_BOTH));

		Label label = new Label(composite, SWT.NULL);
		label.setText("&XPath:");

		xpath = new Text(composite, SWT.BORDER);
		xpath.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		xpath.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent arg0) {
				boolean enabled = xpath.getText().length() > 0;
				next.setEnabled(enabled);
				prev.setEnabled(enabled);
			}
		});

		Composite buttons = new Composite(composite, SWT.NULL);
		buttons.setLayout(new GridLayout(2, true));
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 2;
		buttons.setLayoutData(gd);

		next = new Button(buttons, SWT.PUSH);
		next.setText(WebToolsPlugin.getResourceString("Button.SearchNext"));
		next.setEnabled(false);
		next.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		next.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				FuzzyXMLNode[] nodes = searchNodes();
				if (nodes != null) {
					status.setText(nodes.length + " nodes matched");
					HTMLSourceEditor editor = getActiveEditor();
					ITextSelection sel = (ITextSelection) editor.getSelectionProvider().getSelection();
					int offset = sel.getOffset() + sel.getLength();
					for (int i = 0; i < nodes.length; i++) {
						if (nodes[i].getOffset() >= offset) {
							editor.getSelectionProvider()
									.setSelection(new TextSelection(nodes[i].getOffset(), nodes[i].getLength()));
							break;
						}
					}
				}
				xpath.setFocus();
			}
		});

		getShell().setDefaultButton(next);

		prev = new Button(buttons, SWT.PUSH);
		prev.setText(WebToolsPlugin.getResourceString("Button.SearchPrev"));
		prev.setEnabled(false);
		prev.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		prev.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				FuzzyXMLNode[] nodes = searchNodes();
				if (nodes != null) {
					status.setText(nodes.length + " nodes matched");
					HTMLSourceEditor editor = getActiveEditor();
					ITextSelection sel = (ITextSelection) editor.getSelectionProvider().getSelection();
					int offset = sel.getOffset();
					for (int i = nodes.length - 1; i >= 0; i--) {
						if (nodes[i].getOffset() < offset) {
							editor.getSelectionProvider()
									.setSelection(new TextSelection(nodes[i].getOffset(), nodes[i].getLength()));
							break;
						}
					}
				}
				xpath.setFocus();
			}
		});

		status = new Label(composite, SWT.NULL);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 2;
		status.setLayoutData(gd);

		return composite;
	}

	private HTMLSourceEditor getActiveEditor() {
		IEditorPart part = HTMLUtil.getActiveEditor();
		if (part instanceof HTMLSourceEditor) {
			return (HTMLSourceEditor) part;
		} else if (part instanceof IPaletteTarget) {
			return ((IPaletteTarget) part).getPaletteTarget();
		}
		return null;
	}

	private FuzzyXMLNode[] searchNodes() {
		HTMLSourceEditor editor = getActiveEditor();
		if (editor != null) {
			String query = xpath.getText();
			String xml = editor.getHTMLSource();
			FuzzyXMLDocument doc = new FuzzyXMLParser().parse(xml);
			try {
				return XPath.selectNodes(doc.getDocumentElement(), query);
			} catch (Exception ex) {
				status.setText(ex.getMessage());
			}
			return null;
		} else {
			status.setText("Unsupported editor");
			return null;
		}
	}

	@Override
	protected Point getInitialSize() {
		Point point = super.getInitialSize();
		point.x = 400;
		return point;
	}

	@Override
	protected int getShellStyle() {
		return super.getShellStyle() | SWT.RESIZE;
	}

}
