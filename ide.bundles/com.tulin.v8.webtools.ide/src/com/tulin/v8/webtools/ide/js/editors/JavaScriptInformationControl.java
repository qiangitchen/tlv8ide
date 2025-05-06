package com.tulin.v8.webtools.ide.js.editors;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.text.AbstractInformationControl;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import com.tulin.v8.webtools.ide.html.HTMLUtil;

/**
 * Quick Outline implementation for JavaScriptEditor.
 */
public class JavaScriptInformationControl extends AbstractInformationControl {

	private JavaScriptOutlinePage outline;
	private Text search;

	public JavaScriptInformationControl(ISourceViewer viewer) {
		super(viewer.getTextWidget().getShell(), true);

		create();

		int width = 300;
		int height = 300;

		Point loc = viewer.getTextWidget().getParent().toDisplay(0, 0);
		Point size = viewer.getTextWidget().getParent().getSize();

		int x = (size.x - width) / 2 + loc.x;
		int y = (size.y - height) / 2 + loc.y;

		setSize(width, height);
		setLocation(new Point(x, y));
		addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				dispose();
			}
		});
	}

	@Override
	protected void createContent(Composite parent) {
		Color foreground = parent.getShell().getDisplay().getSystemColor(SWT.COLOR_INFO_FOREGROUND);
		Color background = parent.getShell().getDisplay().getSystemColor(SWT.COLOR_INFO_BACKGROUND);

		Composite composite = new Composite(parent, SWT.NULL);
		composite.setLayout(new GridLayout(1, false));
		composite.setForeground(foreground);
		composite.setBackground(background);

		search = new Text(composite, SWT.NONE);
		search.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		search.setForeground(foreground);
		search.setBackground(background);

		new Label(composite, SWT.SEPARATOR | SWT.HORIZONTAL).setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		Dialog.applyDialogFont(search);

		search.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		search.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				String filterText = search.getText();
				outline.setFilterText(filterText);

//				viewer.getRootEditPart().getContents().refresh();
//				if(filterText.length() > 0){
//					EditPart folder = (EditPart) viewer.
//						getRootEditPart().getContents().getChildren().get(0);
//
//					List<?> tables = folder.getChildren();
//
//					if(tables.size() > 0){
//						viewer.select((EditPart) tables.get(0));
//					}
//				}
			}
		});
		search.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				if (e.keyCode == SWT.CR) {
					selectAndDispose();
				}
				if (e.keyCode == SWT.ARROW_UP) {
					outline.getControl().setFocus();
				}
				if (e.keyCode == SWT.ARROW_DOWN) {
					outline.getControl().setFocus();
				}
			}
		});

		Composite treeArea = new Composite(composite, SWT.NULL);
		treeArea.setLayout(new FillLayout());
		treeArea.setLayoutData(new GridData(GridData.FILL_BOTH));

		JavaScriptEditor editor = (JavaScriptEditor) HTMLUtil.getActiveEditor();
		outline = new JavaScriptOutlinePage(editor);
		outline.createControl(treeArea);
		outline.update();
		outline.getViewer().expandAll();
		outline.setSelect(false);
		outline.getViewer().getControl().setForeground(foreground);
		outline.getViewer().getControl().setBackground(background);
		outline.getViewer().getControl().addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				if (e.keyCode == SWT.CR) {
					selectAndDispose();
				}
			}
		});
		outline.getViewer().addDoubleClickListener(new IDoubleClickListener() {
			public void doubleClick(DoubleClickEvent event) {
				selectAndDispose();
			}
		});
	}

	private void selectAndDispose() {
		outline.selectSelection();
		dispose();
	}

	@Override
	public void setVisible(boolean visible) {
		super.setVisible(visible);
		search.setFocus();
	}

	public boolean hasContents() {
		return true;
	}

}
