package com.tulin.v8.webtools.ide.js.editors;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.views.contentoutline.ContentOutlinePage;

import com.tulin.v8.webtools.ide.WebToolsPlugin;
import com.tulin.v8.webtools.ide.js.model.JavaScriptContext;
import com.tulin.v8.webtools.ide.js.model.JavaScriptElement;
import com.tulin.v8.webtools.ide.js.model.JavaScriptFunction;
import com.tulin.v8.webtools.ide.js.model.JavaScriptModel;
import com.tulin.v8.webtools.ide.js.model.JavaScriptPrototype;
import com.tulin.v8.webtools.ide.utils.StringUtils;

/**
 * ContentOutlinePage implementation for JavaScriptEditor.
 */
@SuppressWarnings("deprecation")
public class JavaScriptOutlinePage extends ContentOutlinePage {

	private JavaScriptModel model;
	private JavaScriptEditor editor;
	private String filterText = "";
	private boolean select = true;
	private JavaScriptSelectionChangedListener selectionChangedListener = new JavaScriptSelectionChangedListener();

	public JavaScriptOutlinePage(JavaScriptEditor editor) {
		super();
		this.editor = editor;
	}

	public void setSelect(boolean select) {
		this.select = select;
	}

	public void setFilterText(String filterText) {
		if (filterText == null) {
			filterText = "";
		}
		this.filterText = filterText;
		getTreeViewer().refresh();
		getTreeViewer().expandAll();

//		ITreeContentProvider provider = (ITreeContentProvider) getTreeViewer().getContentProvider();
//		Object[] children = (Object[]) provider.getChildren(model);
//		if (children.length > 0) {
//			getViewer().setSelection(new StructuredSelection(children[0]), true);
//		}
		JavaScriptElement element = getFirstElement(model, filterText);
		if (element != null) {
			getViewer().setSelection(new StructuredSelection(element), true);
		}
	}

	private static JavaScriptElement getFirstElement(JavaScriptContext context, String text) {
		if (StringUtils.isEmpty(text)) {
			if (context.getElements().length > 0) {
				return context.getElements()[0];
			}
		}
		for (JavaScriptElement element : context.getElements()) {
			if (element.getName().toLowerCase().startsWith(text.toLowerCase())) {
				return element;
			}
			if (element.getContext() != null) {
				JavaScriptElement result = getFirstElement(element.getContext(), text);
				if (result != null) {
					return result;
				}
			}
		}
		return null;
	}

	public void createControl(Composite parent) {
		super.createControl(parent);

		IFile file = null;
		IEditorInput input = editor.getEditorInput();
		if (input instanceof IFileEditorInput) {
			file = ((IFileEditorInput) input).getFile();
		}

		model = new JavaScriptModel(file, editor.getDocumentProvider().getDocument(editor.getEditorInput()).get());
		TreeViewer viewer = getTreeViewer();
		viewer.setContentProvider(new JavaScriptContentProvider());
		viewer.setLabelProvider(new JavaScriptLabelProvider());
		viewer.addSelectionChangedListener(selectionChangedListener);
		viewer.setInput(model);

		if (getSite() != null) {
			IActionBars bars = getSite().getActionBars();
			IToolBarManager toolbar = bars.getToolBarManager();
			toolbar.add(new SortAction());
		}

		getTreeViewer().refresh();
		// update();
	}

	public TreeViewer getViewer() {
		return getTreeViewer();
	}

	public void setSelection(int offset) {
		if (model != null) {
			try {
				getTreeViewer().removeSelectionChangedListener(selectionChangedListener);
				JavaScriptContext context = model.getContextFromOffset(offset);
				setSelection(new StructuredSelection(context));
				getTreeViewer().addSelectionChangedListener(selectionChangedListener);
			} catch (Exception ex) {
			}
		}
	}

	public void update() {
		if (model != null) {
			try {
				model.update(editor.getDocumentProvider().getDocument(editor.getEditorInput()).get());
				getTreeViewer().refresh();
			} catch (Throwable t) {
				t.printStackTrace();
			}
		}
	}

	public void selectSelection() {
		IStructuredSelection sel = (IStructuredSelection) getViewer().getSelection();
		JavaScriptElement element = (JavaScriptElement) sel.getFirstElement();
		if (element != null) {
			// if(element instanceof JavaScriptCallable){
			// // TODO for Debug
			// editor.selectAndReveal(element.getOffset(),
			// ((JavaScriptCallable) element).getEndOffset() -
			// element.getOffset());
			// } else {
			editor.selectAndReveal(element.getStart(), 0);
			// }
		}
	}

	private static boolean containsText(JavaScriptElement element, String text) {
		if (element.getName().toLowerCase().startsWith(text.toLowerCase())) {
			return true;
		}
		if (element.getContext() == null) {
			return false;
		}
		for (JavaScriptElement child : element.getContext().getElements()) {
			boolean result = containsText(child, text);
			if (result) {
				return result;
			}
		}
		return false;
	}

	/** ITreeContentProvider implementation for JavaScriptOutlinePage. */
	private class JavaScriptContentProvider implements ITreeContentProvider {
		public Object[] getChildren(Object parentElement) {
			if (parentElement instanceof JavaScriptContext) {
				List<Object> result = new ArrayList<Object>();
				for (JavaScriptElement element : ((JavaScriptContext) parentElement).getElements()) {
					if (element instanceof JavaScriptPrototype || "arguments".equals(element.getName())
							|| element.isTemporary()) {
						continue;
					}
					if (filterText.length() > 0) {
						if (containsText(element, filterText)) {
							result.add(element);
						}
					} else {
						result.add(element);
					}
				}
				return result.toArray(new Object[result.size()]);

			} else if (parentElement instanceof JavaScriptElement) {
				JavaScriptContext block = ((JavaScriptElement) parentElement).getContext();
				if (block != null) {
					return getChildren(block);
				}
			}
			return new Object[0];
		}

		public Object getParent(Object element) {
			if (element instanceof JavaScriptContext) {
				return ((JavaScriptContext) element).getParent();
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

	/** ISelectionChangedListener implementation for JavaScriptOutlinePage. */
	private class JavaScriptSelectionChangedListener implements ISelectionChangedListener {
		public void selectionChanged(SelectionChangedEvent event) {
			if (!select) {
				return;
			}
			selectSelection();
		}
	}

	/** LabelProvider implementation for JavaScriptOutlinePage. */
	private static class JavaScriptLabelProvider extends LabelProvider {
		public Image getImage(Object element) {
			if (element instanceof JavaScriptElement) {
				JavaScriptFunction jsFunc = ((JavaScriptElement) element).getFunction();
				if (jsFunc != null) {
					if (jsFunc.isClass()) {
						return WebToolsPlugin.getDefault().getImageRegistry().get(WebToolsPlugin.ICON_CLASS);
					} else {
						return WebToolsPlugin.getDefault().getImageRegistry().get(WebToolsPlugin.ICON_FUNCTION);
					}
				} else {
					return WebToolsPlugin.getDefault().getImageRegistry().get(WebToolsPlugin.ICON_VARIABLE);
				}
			}
			return null;
		}

		public String getText(Object element) {
			if (element == null) {
				return "";
			} else if (element instanceof JavaScriptElement) {
				StringBuilder buf = new StringBuilder();
				JavaScriptElement jsElement = (JavaScriptElement) element;
				String displayString = jsElement.getDisplayString();
				buf.append(displayString);
				JavaScriptFunction jsFunc = jsElement.getFunction();
				if (jsFunc != null && displayString.startsWith("(")) {
					buf.insert(0, "function");
				}
				String[] types = jsFunc == null ? jsElement.getTypes() : jsFunc.getReturnTypes();
				buf.append(" : ");
				if (types.length == 0) {
					buf.append("Any");
				} else if (types.length == 1 && "*".equals(types[0])) {
					buf.append("Any");
				} else {
					boolean isFirst = true;
					for (String type : types) {
						if (!isFirst) {
							buf.append('|');
						} else {
							isFirst ^= true;
						}
						if (type.startsWith("Object:")) {
							buf.append("Object");
						} else if ("*".equals(type)) {
							buf.append("Any");
						} else {
							buf.append(type);
						}
					}
				}
				return buf.toString();
			} else {
				return element.toString();
			}
		}
	}

	private static ViewerSorter sorter = new ViewerSorter();

	private class SortAction extends Action {
		public SortAction() {
			super(WebToolsPlugin.getResourceString("Label.Sort"), IAction.AS_CHECK_BOX);
			setImageDescriptor(WebToolsPlugin.getDefault().getImageRegistry().getDescriptor(WebToolsPlugin.ICON_SORT));
		}

		@Override
		public void run() {
			if (isChecked()) {
				getViewer().setSorter(sorter);
			} else {
				getViewer().setSorter(null);
			}
		}
	}
}
