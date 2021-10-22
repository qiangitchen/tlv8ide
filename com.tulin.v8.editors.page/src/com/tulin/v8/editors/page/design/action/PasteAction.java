package com.tulin.v8.editors.page.design.action;

import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import com.tulin.v8.core.TuLinPlugin;
import com.tulin.v8.editors.page.ReadHTML;
import com.tulin.v8.editors.page.design.WEBDesignEditorInterface;

public class PasteAction extends Action {
	Tree tree;
	Clipboard clipbd;
	WEBDesignEditorInterface editor;

	public PasteAction(Tree tree, Clipboard clipbd, WEBDesignEditorInterface editor) {
		super();
		this.tree = tree;
		this.clipbd = clipbd;
		this.editor = editor;
		this.setText(Messages.getString("design.action.paste"));
		this.setImageDescriptor(ImageDescriptor.createFromImage(TuLinPlugin.getIcon("edit/PasteHS.png")));
		this.setEnabled(false);
	}

	public void run() {
		try {
			Transferable clipT = clipbd.getContents(null);
			if (clipT != null) {
				if (clipT.isDataFlavorSupported(DataFlavor.stringFlavor)) {
					String context = (String) clipT.getTransferData(DataFlavor.stringFlavor);
					TreeItem[] selection = tree.getSelection();
					TreeItem selectItem = selection[0];
					Element element = (Element) selectItem.getData();
					Document doc = Jsoup.parse("<div id=\"pastextdom\">" + context + "</div>");
					Element pel = doc.getElementById("pastextdom").children().first();
					element.appendChild(pel);
					TreeItem pitem = new TreeItem(selectItem, SWT.NONE);
					pitem.setText(ReadHTML.getText(pel));
					pitem.setImage(TuLinPlugin.getIcon("tags/brkp_obj.gif"));
					pitem.setData(pel);
					if (pel.attr("id") != null) {
						String newid = "CopyOf" + pel.attr("id");
						pel.attr("id", newid);
						editor.setElementItem(newid, pitem);
					}
					ReadHTML.readDocument(pitem, pel, editor);
					editor.getEditorpart().setSourcePageText(editor.getPageDom().html());
				}
			}
		} catch (Exception e) {
			// e.printStackTrace();
			Status status = new Status(IStatus.ERROR, TuLinPlugin.getPluginId(), e.toString());
			ErrorDialog.openError(TuLinPlugin.getShell(), Messages.getString("design.action.pasteerrtitle"),
					Messages.getString("design.action.pasteerrmsg"), status);
		}
	}

	public void updateState() {
		try {
			Transferable clipT = clipbd.getContents(null);
			if (clipT != null) {
				if (clipT.isDataFlavorSupported(DataFlavor.stringFlavor)) {
					String context = (String) clipT.getTransferData(DataFlavor.stringFlavor);
					Document doc = Jsoup.parse("<div id=\"pastextdom\">" + context + "</div>");
					Element pel = doc.getElementById("pastextdom").children().first();
					Validate.notNull(pel);
					setEnabled(true);
				}
			}
		} catch (Exception e) {
			setEnabled(false);
		}
	}
}
