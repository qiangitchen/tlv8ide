package com.tulin.v8.webtools.ide.text;

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.contexts.IContextService;
import org.eclipse.ui.editors.text.TextEditor;
import org.eclipse.ui.texteditor.ChainedPreferenceStore;
import org.eclipse.ui.texteditor.ITextEditorActionConstants;

import com.tulin.v8.webtools.ide.WebToolsPlugin;
import com.tulin.v8.webtools.ide.color.ColorProvider;

public class WebSourceEditor extends TextEditor {
	protected ColorProvider colorProvider;
	protected AbsTextSourceViewerConfiguration configuration;
	protected AbstractSelectionChangedListener selectionChangeListener;

	public static final String ACTION_COMMENT = "_comment";
	public static final String ACTION_FORMAT = "_format";
	public static final String ACTION_OUTLINE = "_outline";

	public WebSourceEditor() {
		super();
		colorProvider = WebToolsPlugin.getDefault().getColorProvider();
		setPreferenceStore(new ChainedPreferenceStore(
				new IPreferenceStore[] { getPreferenceStore(), WebToolsPlugin.getDefault().getPreferenceStore() }));
	}

	@Override
	public void createPartControl(Composite parent) {
		IContextService contextService = getSite().getService(IContextService.class);
		if (contextService != null) {
			contextService.activateContext(WebToolsPlugin.EDITOR_KEYBINDING_SCOPE_ID);
		}
		super.createPartControl(parent);
	}

	@Override
	protected void editorContextMenuAboutToShow(IMenuManager menu) {
		super.editorContextMenuAboutToShow(menu);
		addContextMenuActions(menu);
	}

	protected void addContextMenuActions(IMenuManager menu) {
		menu.appendToGroup(ITextEditorActionConstants.GROUP_EDIT, new MenuManager(
				WebToolsPlugin.getResourceString("SourceEditor.Menu.Source"), WebToolsPlugin.GROUP_SOURCE));
		addAction(menu, WebToolsPlugin.GROUP_SOURCE, ACTION_COMMENT);
		addAction(menu, WebToolsPlugin.GROUP_SOURCE, ACTION_FORMAT);
	}

	/**
	 * Handlers/action 层访问编辑器的 ISourceViewer（用于在文档程序式修改后
	 * invalidate 文本外观与分区缓存，从而立即刷新语法高亮）。
	 * TextEditor 中同名方法为 protected，这里向上暴露为 public 以便
	 * ToggleCommentHandler / CSSEditor / JavaScriptEditor 内联注释动作共享。
	 *
	 * @return 当前编辑器的源查看器，未创建时返回 null
	 */
	public ISourceViewer getViewer() {
		return getSourceViewer();
	}
}
