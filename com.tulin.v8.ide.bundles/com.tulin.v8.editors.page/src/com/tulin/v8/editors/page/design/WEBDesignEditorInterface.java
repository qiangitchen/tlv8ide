package com.tulin.v8.editors.page.design;

import java.io.File;

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.IEditorPart;
import org.jsoup.nodes.Document;

import com.tulin.v8.editors.html.HTMLEditor;
import com.tulin.v8.editors.page.PageEditorInterface;
import com.tulin.v8.swt.chromium.Browser;

public interface WEBDesignEditorInterface {

	public abstract void fillContextMenu(IMenuManager manager);

	/**
	 * 初始化动作
	 */
	public abstract void makeActions();

	/**
	 * 表格单击
	 */
	public abstract void evenTabledbclick(TableItem item, String name, String text);

	/**
	 * 属性编辑
	 */
	public abstract void proptypeEdited(TableItem item, Text text);

	/**
	 * 事件编辑
	 * 
	 * @param item
	 * @param text
	 */
	public abstract void eventEdited(TableItem item, Text text);

	public abstract void loadBrowser();

	public abstract Document setModel();

	public File getFile();

	public IEditorPart getActiveEditors();

//	public IStructuredModel getModel();

//	public HTMLEditor getHTMLEditor();

	public PageEditorInterface getEditorpart();

	public Document getPageDom();

	/**
	 * 获取页面Element的树
	 * 
	 * @return Tree
	 */
	public Tree getElementTree();

	public Browser getBrowser();

	/**
	 * 设置Element map
	 * 
	 * @param elementId
	 * @param item
	 */
	public abstract void setElementItem(String elementId, TreeItem item);

	/**
	 * 根据ID取Element
	 * 
	 * @param elementId
	 * @return
	 */
	public abstract TreeItem getElementItem(String elementId);

	/**
	 * 选中指定ID的Element
	 * 
	 * @param elementId
	 */
	public abstract void selectElement(String elementId);

	public abstract void addPropotype();

	public abstract void catElement();

	public abstract void copyElement();

	public abstract void deleteElement();

	public abstract int getDesignerMode();

	public abstract void viewSourse();

	public abstract void activhtmlEditor();

	public abstract HTMLEditor getSourceEditor();

}
