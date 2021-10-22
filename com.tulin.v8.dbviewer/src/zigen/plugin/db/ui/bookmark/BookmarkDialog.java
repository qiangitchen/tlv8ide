/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.ui.bookmark;

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TreeItem;

import zigen.plugin.db.DbPlugin;
import zigen.plugin.db.ui.actions.RegistBookmarkFolderAction;
import zigen.plugin.db.ui.actions.RemoveBookmarkAction;
import zigen.plugin.db.ui.actions.RemoveBookmarkFolderAction;
import zigen.plugin.db.ui.actions.RenameBookmarkFolderAction;
import zigen.plugin.db.ui.internal.Bookmark;
import zigen.plugin.db.ui.internal.BookmarkFolder;
import zigen.plugin.db.ui.internal.BookmarkRoot;
import zigen.plugin.db.ui.internal.Root;
import zigen.plugin.db.ui.internal.TreeNode;
import zigen.plugin.db.ui.util.WidgetUtil;
import zigen.plugin.db.ui.views.TreeContentProvider;
import zigen.plugin.db.ui.views.TreeDoubleClickHandler;
import zigen.plugin.db.ui.views.TreeLabelProvider;
import zigen.plugin.db.ui.views.TreeViewListener;
import zigen.plugin.db.ui.views.TreeViewSorter;

public class BookmarkDialog extends Dialog {

	public static final int BUTTON_WIDTH = 100;

	public static final int HORIZONTAL_SPACING = 3;

	public static final int MARGIN_WIDTH = 0;

	public static final int MARGIN_HEIGHT = 2;

	protected TreeViewer viewer;

	protected TreeContentProvider provider;

	private Button newBtn;

	private Button editBtn;

	private Button removeBtn;

	private TreeItem dragSourceItem;

	class BookmarkFilter extends ViewerFilter {

		public boolean select(Viewer viewer, Object parentElement, Object element) {
			if (element instanceof Root)
				return false;
			else
				return true;
		}
	}

	public BookmarkDialog(Shell parent, TreeContentProvider provider) {
		super(parent);
		this.provider = provider;
	}

	protected void okPressed() {
		if (save()) {
			super.okPressed();
		}
	}

	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText(Messages.getString("BookmarkDialog.0")); //$NON-NLS-1$
	}

	protected Control createDialogArea(Composite parent) {

		Composite composite = (Composite) super.createDialogArea(parent);

		GridLayout layout = new GridLayout(2, false);
		composite.setLayout(layout);

		createListLabel(composite);

		createList(composite);

		createListEditButtons(composite);

		addSeparator(parent);

		return composite;
	}

	private void createListLabel(Composite composite) {
		Label listLabel = new Label(composite, SWT.NONE);
		listLabel.setText(Messages.getString("BookmarkDialog.1")); //$NON-NLS-1$
		GridData data = new GridData();
		data.horizontalSpan = 2;
		listLabel.setLayoutData(data);

	}

	private void createList(Composite composite) {

		viewer = new TreeViewer(composite, SWT.BORDER | SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.SINGLE);

		int dragOption = DND.DROP_DEFAULT | DND.DROP_MOVE | DND.DROP_COPY;
		Transfer[] transfers = new Transfer[] {TreeLeafListTransfer.getInstance()};
		viewer.addDragSupport(dragOption, transfers, new DragBookmarkAdapter(viewer));
		viewer.addDropSupport(dragOption, transfers, new DropBookmarkAdapter(viewer));

		viewer.addFilter(new BookmarkFilter());
		viewer.setContentProvider(provider);
		viewer.setLabelProvider(new TreeLabelProvider());
		viewer.setSorter(new TreeViewSorter());
		viewer.setInput(ResourcesPlugin.getWorkspace().getRoot());
		viewer.expandToLevel(2);

		viewer.addTreeListener(new TreeViewListener());

		GridData data = new GridData(GridData.FILL_BOTH);
		data.verticalSpan = 4;
		data.widthHint = 200;
		data.heightHint = viewer.getTree().getItemHeight() * 10;
		viewer.getTree().setLayoutData(data);

		viewer.addSelectionChangedListener(new ISelectionChangedListener() {

			public void selectionChanged(SelectionChangedEvent event) {
				tableSelectionChangedHandler(event);
			}
		});

		hookDoubleClickAction();
	}

	protected void hookDoubleClickAction() {
		viewer.addDoubleClickListener(new TreeDoubleClickHandler());
	}

	private void createListEditButtons(Composite composite) {
		newBtn = WidgetUtil.createButton(composite, SWT.PUSH, Messages.getString("BookmarkDialog.2"), BUTTON_WIDTH, new GridData()); //$NON-NLS-1$
		editBtn = WidgetUtil.createButton(composite, SWT.PUSH, Messages.getString("BookmarkDialog.3"), BUTTON_WIDTH, new GridData()); //$NON-NLS-1$
		removeBtn = WidgetUtil.createButton(composite, SWT.PUSH, Messages.getString("BookmarkDialog.4"), BUTTON_WIDTH, new GridData()); //$NON-NLS-1$

		newBtn.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent e) {
				addButtonPressedHandler();
			}
		});

		editBtn.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent e) {
				editButtonPressedHandler();
			}
		});

		removeBtn.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent e) {
				removeButtonPressedHandler();
			}
		});

	}

	protected void tableSelectionChangedHandler(SelectionChangedEvent event) {
		// int size = ((IStructuredSelection) event.getSelection()).size();
		// editBtn.setEnabled(size == 1);
		// removeBtn.setEnabled(size > 0);

		Object element = (Object) ((IStructuredSelection) event.getSelection()).getFirstElement();

		if (element instanceof BookmarkRoot) {
			editBtn.setEnabled(true);
			removeBtn.setEnabled(false);
		} else if (element instanceof BookmarkFolder) {
			editBtn.setEnabled(true);
			removeBtn.setEnabled(true);
		} else if (element instanceof Bookmark) {
			editBtn.setEnabled(false);
			removeBtn.setEnabled(true);
		} else {
			editBtn.setEnabled(false);
			removeBtn.setEnabled(false);

		}

	}

	private void addButtonPressedHandler() {
		new RegistBookmarkFolderAction(viewer).run();
	}

	private void editButtonPressedHandler() {
		new RenameBookmarkFolderAction(viewer).run();
	}

	private void removeButtonPressedHandler() {
		Object element = (Object) ((IStructuredSelection) viewer.getSelection()).getFirstElement();
		if (element instanceof BookmarkFolder) {
			new RemoveBookmarkFolderAction(viewer).run();
		} else if (element instanceof Bookmark) {
			new RemoveBookmarkAction(viewer).run();
		}

	}

	private void addSeparator(Composite parent) {
		Label separator = new Label(parent, SWT.SEPARATOR | SWT.HORIZONTAL);
		GridData gridData = new GridData();
		gridData.horizontalAlignment = GridData.FILL;
		gridData.grabExcessHorizontalSpace = true;
		separator.setLayoutData(gridData);
	}

	private boolean save() {
		try {

			Object element = (Object) ((StructuredSelection) viewer.getSelection()).getFirstElement();

			if (element instanceof BookmarkRoot) {
				selectedNode = (TreeNode) element;

			} else if (element instanceof BookmarkFolder) {
				selectedNode = (TreeNode) element;

			} else {
				selectedNode = null;
				return false;
			}

			return true;

		} catch (Exception e) {
			DbPlugin.getDefault().showErrorDialog(e);
		}
		return false;
	}

	private TreeNode selectedNode = null;

	public TreeNode getSelectedNode() {
		return selectedNode;
	}

}
