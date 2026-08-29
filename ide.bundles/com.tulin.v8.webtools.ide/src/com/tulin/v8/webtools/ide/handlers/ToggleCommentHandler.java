package com.tulin.v8.webtools.ide.handlers;

import java.lang.reflect.Method;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentExtension3;
import org.eclipse.jface.text.IDocumentPartitioner;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.ui.part.MultiPageEditorPart;

import com.tulin.v8.webtools.ide.WebToolsPlugin;
import com.tulin.v8.webtools.ide.css.editors.CSSEditor;
import com.tulin.v8.webtools.ide.html.editors.HTMLEditor;
import com.tulin.v8.webtools.ide.html.editors.HTMLSourceEditor;
import com.tulin.v8.webtools.ide.js.editors.JavaScriptEditor;
import com.tulin.v8.webtools.ide.xml.editors.XMLEditor;

public class ToggleCommentHandler extends AbstractHandler implements IHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IWorkbenchWindow window = null;
		if (event == null)
			window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
		else {
			window = HandlerUtil.getActiveWorkbenchWindowChecked(event);
		}
		IWorkbenchPage page = window.getActivePage();
		if (page.getActiveEditor() instanceof MultiPageEditorPart) {
			MultiPageEditorPart editor = (MultiPageEditorPart) page.getActiveEditor();
			if (editor.getSelectedPage() instanceof XMLEditor) {
				XMLEditor xmleditor = (XMLEditor) editor.getSelectedPage();
				toggleComment(xmleditor);
			} else if (editor.getSelectedPage() instanceof HTMLSourceEditor) {
				HTMLSourceEditor htmleditor = (HTMLSourceEditor) editor.getSelectedPage();
				toggleComment(htmleditor);
			} else if (editor.getSelectedPage() instanceof JavaScriptEditor) {
				JavaScriptEditor jseditor = (JavaScriptEditor) editor.getSelectedPage();
				toggleJSComment(jseditor);
			} else if (editor.getSelectedPage() instanceof CSSEditor) {
				CSSEditor csseditor = (CSSEditor) editor.getSelectedPage();
				toggleCSSComment(csseditor);
			}
		} else if (page.getActiveEditor() instanceof XMLEditor) {
			XMLEditor editor = (XMLEditor) page.getActiveEditor();
			toggleComment(editor);
		} else if (page.getActiveEditor() instanceof HTMLEditor) {
			HTMLSourceEditor editor = ((HTMLEditor) page.getActiveEditor()).getPaletteTarget();
			toggleComment(editor);
		} else if (page.getActiveEditor() instanceof JavaScriptEditor) {
			JavaScriptEditor editor = (JavaScriptEditor) page.getActiveEditor();
			toggleJSComment(editor);
		} else if (page.getActiveEditor() instanceof CSSEditor) {
			CSSEditor csseditor = (CSSEditor) page.getActiveEditor();
			toggleCSSComment(csseditor);
		}
		return null;
	}

	public static void toggleComment(HTMLSourceEditor editor) {
		ITextSelection sel = (ITextSelection) editor.getSelectionProvider().getSelection();
		IDocument doc = editor.getDocumentProvider().getDocument(editor.getEditorInput());
		String text = sel.getText().trim();
		if ("".equals(text)) {
			return;
		}
		int oldLen = sel.getLength();
		int offset = sel.getOffset();
		try {
			String newText;
			if (text.startsWith("<!--") && text.indexOf("-->") > 3) {
				newText = sel.getText().replaceFirst("<!--", "");
				newText = newText.replaceFirst("-->", "");
				doc.replace(offset, oldLen, newText);
			} else {
				newText = "<!--" + sel.getText() + "-->";
				doc.replace(offset, oldLen, newText);
			}
			afterDocChanged(editor.getViewer(), doc, offset, oldLen, newText.length());
		} catch (BadLocationException e) {
			WebToolsPlugin.logException(e);
		}
	}

	public static void toggleJSComment(JavaScriptEditor editor) {
		ITextSelection sel = (ITextSelection) editor.getSelectionProvider().getSelection();
		IDocument doc = editor.getDocumentProvider().getDocument(editor.getEditorInput());
		try {
			int startline = sel.getStartLine();
			int endline = sel.getEndLine();
			int offset = doc.getLineOffset(startline);
			int length = doc.getLineOffset(endline) + doc.getLineLength(endline) - offset - 2;
			String text = doc.get(offset, length);
			if (startline == endline) {// 单行注释/取消注释
				if (text.trim().startsWith("//")) {
					text = text.replaceFirst("//", "");
				} else {
					text = "//" + text;
				}
			} else {// 多行注释/取消注释
				if (text.trim().startsWith("//")) {
					text = text.replaceAll("(^|\r\n|\r|\n)//", "$1");
				} else {
					text = text.replaceAll("(^|\r\n|\r|\n)", "$1//");
				}
			}
			int oldLen = length;
			doc.replace(offset, length, text);
			afterDocChanged(editor.getViewer(), doc, offset, oldLen, text.length());
		} catch (BadLocationException e) {
			WebToolsPlugin.logException(e);
		}
	}

	public static void toggleCSSComment(CSSEditor editor) {
		ITextSelection sel = (ITextSelection) editor.getSelectionProvider().getSelection();
		IDocument doc = editor.getDocumentProvider().getDocument(editor.getEditorInput());
		String text = sel.getText().trim();
		if ("".equals(text)) {
			return;
		}
		int offset = sel.getOffset();
		int oldLen = sel.getLength();
		try {
			String newText;
			if (text.startsWith("/*") && text.indexOf("*/") > 3) {
				newText = sel.getText().replaceFirst("/\\*", "");
				newText = newText.replaceFirst("\\*/", "");
				doc.replace(offset, oldLen, newText);
			} else {
				newText = "/*" + sel.getText() + "*/";
				doc.replace(offset, oldLen, newText);
			}
			afterDocChanged(editor.getViewer(), doc, offset, oldLen, newText.length());
		} catch (BadLocationException e) {
			WebToolsPlugin.logException(e);
		}
	}

	/**
	 * 断开 partitioner 并重新连接文档，等价于清空其全部缓存，强制下一次
	 * computePartitioning / getPartition 调用基于当前文档内容重新扫描。
	 *
	 * <p>作为 public 工具方法暴露方便 UT 与上层复用。
	 *
	 * @param partitioner 分区器，可为 null（不操作）
	 * @param doc         目标文档，不可在 partitioner 非 null 时为 null
	 */
	public static void reconnectPartitioner(IDocumentPartitioner partitioner, IDocument doc) {
		if (partitioner == null || doc == null) return;
		try {
			partitioner.disconnect();
		} catch (Throwable ignore) { /* noop */ }
		try {
			partitioner.connect(doc);
		} catch (Throwable ignore) { /* noop */ }
	}

	/**
	 * 程序化修改文档后，刷新「分区缓存 + 文本外观」两步，
	 * 解决「注释后代码高亮没有立即变化」问题。
	 *
	 * <p>为什么需要显式刷新？
	 * Eclipse FastPartitioner 在 doc.replace 跨越旧分区边界时，
	 * 可能不会把相邻 partition 的起点回退重扫（Bug 已在 3.x/4.x 上存在），
	 * 而 DefaultDamagerRepairer 对未跨分区的变更只会 damage 当前 partition。
	 * 注释操作插入的 {@code <!-- --> / /* * / /} 字符会跨越 partition 边界，
	 * 因此必须：
	 * <ol>
	 *   <li>invalidate partitioner 的缓存，让 HTML/JSP partition scanner 重扫；</li>
	 *   <li>invalidate source viewer 的文本 presentation，让
	 *       PresentationReconciler 重新 apply color。</li>
	 * </ol>
	 *
	 * @param viewer  ISourceViewer，可为 null（UT 或非 UI 线程下会跳过 presentation 重绘）
	 * @param doc     被修改的文档，不可为 null
	 * @param offset  doc.replace 的起始 offset（替换前）
	 * @param oldLen  被替换文本长度
	 * @param newLen  替换后新文本长度（用于计算安全重绘扩展范围）
	 */
	public static void afterDocChanged(ISourceViewer viewer, IDocument doc, int offset, int oldLen, int newLen) {
		if (doc == null) return;
		int docLen = doc.getLength();
		final int safeOffset = Math.max(0, offset - 200);
		final int safeEnd   = Math.min(docLen, offset + Math.max(oldLen, newLen) + 200);
		final int safeLen   = Math.max(1, safeEnd - safeOffset);

		// ===== Step 1: 强制 partitioner 重新扫描（全量 disconnect + connect，跨 Eclipse 版本最稳妥） =====
		// 说明：
		//  1) FastPartitioner 默认的 IDocumentPartitionerExtension3 不提供 public invalidate(offset,len)，
		//     反射找不到该方法；但是扩展3有一个 protected clearPositionCache() 会让下一次
		//     computePartitioning 重扫该范围。由于访问不到 protected，我们退而采用
		//     disconnect()+connect() 这个 "大锤"——等价于清空全部 partition 缓存。
		//     对注释操作（单次、最大几千字符）来说，重扫整份文档成本微乎其微（<1ms），收益远大于成本。
		//  2) 文档可能还通过 IDocumentExtension3 注册了"命名分区"（partitioning），
		//     这些也得同步 disconnect/connect，避免漏掉自定义分区。
		reconnectPartitioner(doc.getDocumentPartitioner(), doc);
		if (doc instanceof IDocumentExtension3) {
			IDocumentExtension3 ext = (IDocumentExtension3) doc;
			try {
				String[] parts = ext.getPartitionings();
				if (parts != null) {
					for (String partitioning : parts) {
						reconnectPartitioner(ext.getDocumentPartitioner(partitioning), doc);
					}
				}
			} catch (Throwable ignore) { /* noop */ }
		}

		// ===== Step 2: invalidate presentation，触发 presentation reconciler 重绘 =====
		if (viewer != null) {
			Runnable r = () -> {
				try {
					// 兼容新旧 JFace：ITextViewer 只有 0-arg 的 invalidateTextPresentation，
					// 部分版本的 SourceViewer 额外提供 (int,int) 重载，这里反射优先调用精确范围。
					boolean didRange = false;
					try {
						Method rangeInvalidate = viewer.getClass().getMethod(
								"invalidateTextPresentation", int.class, int.class);
						final int viewLen = viewer.getDocument() == null ? safeLen : viewer.getDocument().getLength();
						final int end = Math.min(viewLen, safeOffset + safeLen);
						final int len = Math.max(0, end - safeOffset);
						if (len > 0) {
							rangeInvalidate.invoke(viewer, safeOffset, len);
							didRange = true;
						}
					} catch (Throwable ignore) { /* noop */ }
					if (!didRange) {
						viewer.invalidateTextPresentation();
					}
					// 最终保险：强制 text widget 重绘 safe 范围，避免 reconciler damage 范围被压缩。
					if (viewer.getTextWidget() != null && !viewer.getTextWidget().isDisposed()) {
						org.eclipse.swt.custom.StyledText st = viewer.getTextWidget();
						try {
							int w = st.getCharCount();
							int s = Math.max(0, safeOffset);
							int e = Math.min(w, safeOffset + safeLen);
							if (e > s) {
								// 用 redrawRange 精确重绘；反射兼容（redraw(x,y,w,h,all) 在不同平台可靠，
								// 但需要 pixel 坐标——这里用最稳妥的整 widget redraw）
								st.redraw();
								st.update();
							} else {
								st.redraw();
							}
						} catch (Throwable ignore) {
							viewer.getTextWidget().redraw();
						}
					}
				} catch (Throwable ignore) { /* noop */ }
			};
			Display d = Display.getCurrent();
			if (d != null) {
				r.run();
			} else {
				Display.getDefault().asyncExec(r);
			}
		}
	}

}
