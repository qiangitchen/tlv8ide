package com.tulin.v8.ide.ui.editors.page.properties;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuCreator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.Text;

/**
 * @author mengbo
 * @version 1.5
 */
public class NavigationHiearchyAction extends Action {
	private Menu _hiearchyMenu;

	private Node _startNode;

	private Node _currentNode;

	private WPETabbedPropertySheetPage _propertyPage;

	private class MenuCreator implements IMenuCreator {
		public void dispose() {
			if (_hiearchyMenu != null) {
				for (int i = 0, n = _hiearchyMenu.getItemCount(); i < n; i++) {
					MenuItem menuItem = _hiearchyMenu.getItem(i);
					menuItem.setData(null);
				}
				_hiearchyMenu.dispose();
				_hiearchyMenu = null;
			}
		}

		public Menu getMenu(Menu parent) {
			return null;
		}

		@SuppressWarnings({ "unchecked", "rawtypes" })
		public Menu getMenu(Control parent) {
			dispose();
			_hiearchyMenu = new Menu(parent);

			// next we need to add the list of parents node into the menu.
			Node node = _startNode;
			List list = new ArrayList();
			while (node != null && !(node instanceof Document) && !(node instanceof DocumentFragment)) {
				list.add(node);
				node = node.getParentNode();
			}

			// adding ancesters reverse order.
			for (int i = list.size() - 1; i >= 0; i--) {
				Node thenode = (Node) list.get(i);
				MenuItem item = new MenuItem(_hiearchyMenu, SWT.CHECK);
				item.setSelection(thenode == _currentNode ? true : false);
				String text = thenode.getNodeName();
				item.setText(text);
				item.setData(thenode);
				item.addSelectionListener(new SelectionAdapter() {
					public void widgetSelected(SelectionEvent e) {
						Node selectedNode = (Node) e.widget.getData();
						_propertyPage.internalChangeSelection(selectedNode, _startNode);
					}
				});
			}

			return _hiearchyMenu;
		}
	}

	/**
	 * @param propertyPage
	 */
	public NavigationHiearchyAction(WPETabbedPropertySheetPage propertyPage) {
		super(""); //$NON-NLS-1$
		setEnabled(true);
		setMenuCreator(new MenuCreator());
		this._propertyPage = propertyPage;
	}

	/**
	 * @param selectedNode
	 * @param startNode
	 */
	protected void changeSelection(Node selectedNode, Node startNode) {
		this._propertyPage.internalChangeSelection(selectedNode, startNode);
		this._currentNode = selectedNode;
		this._startNode = startNode;
		this.setText(this._currentNode.getNodeName());
	}

	/**
	 * @param currentNode
	 * @param startNode
	 */
	protected void refresh(Node currentNode, Node startNode) {
		this._currentNode = currentNode;
		this._startNode = startNode;
		if (!(_currentNode instanceof Text) && !(_currentNode instanceof Element)) {
			this.setText("---"); //$NON-NLS-1$
			this.setEnabled(false);
		} else {
			this.setText(_currentNode.getNodeName());
			this.setEnabled(true);
		}
	}

	@Override
	public void run() {
		this._propertyPage.internalChangeSelection(_currentNode, _startNode);
	}
}
