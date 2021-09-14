package com.tulin.v8.editors.page.design.action;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.jsoup.nodes.Element;

import com.tulin.v8.core.TuLinPlugin;
import com.tulin.v8.editors.page.PageEditorInterface;

/**
 * @DE 跳转到源码页
 * @author ChenQian
 */
public class ViewSourseAction extends Action {
	private PageEditorInterface editorpart;
	private Tree tree;

	public ViewSourseAction(PageEditorInterface editorpart, Tree tree) {
		super();
		this.editorpart = editorpart;
		this.tree = tree;
		this.setText(Messages.getString("design.action.viewsource"));
		this.setImageDescriptor(ImageDescriptor.createFromImage(TuLinPlugin.getIcon("tag.gif")));
	}

	@SuppressWarnings("deprecation")
	public void run() {
		TreeItem[] selection = tree.getSelection();
		if (selection.length > 0) {
			TreeItem item = selection[0];
			editorpart.activhtmlEditor();// 跳转到源码页
			Element element = (Element) item.getData();
			String text = element.outerHtml();
			String se = text.trim();
			IDocument document = editorpart.getSourceEditor().getDocumentProvider()
					.getDocument(editorpart.getSourceEditor().getEditorInput());
			try {
				String editorText = editorpart.getPageDom().html();
				int s = document.search(0, se, true, false, false);
				int e = se.length();
				if (s < 0 && !isEmpty(element.attr("id"))) {
					se = element.attr("id");
					s = document.search(0, se, true, false, false);
					e = se.length();
				} else if (s < 0) {
					if (se.indexOf("\n") > 0) {
						se = se.substring(0, se.indexOf("\n"));
					}
					if (se.indexOf("/>") > 0) {
						se = se.substring(0, se.indexOf("/>"));
					}
					s = editorText.indexOf(se);
				}
				editorpart.getSourceEditor().selectAndReveal(s, e);// 设置选中
			} catch (BadLocationException e) {
				e.printStackTrace();
			}
		}
	}

	private boolean isEmpty(String str) {
		return str == null || "".equals(str);
	}
}
