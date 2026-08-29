package tern.eclipse.ide.genericeditor.hover;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.core.filebuffers.FileBuffers;
import org.eclipse.core.filebuffers.ITextFileBuffer;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.content.IContentType;
import org.eclipse.jface.text.*;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.ui.internal.genericeditor.GenericEditorPlugin;
import org.eclipse.ui.texteditor.ITextEditor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import tern.eclipse.ide.ui.hover.TernHover;
import tern.eclipse.jface.text.TernBrowserInformationControlInput;

public class DelegatingFallbackHover extends TernHover implements ITextHover, ITextHoverExtension2 {

	private final ITextEditor editor;
	private final ISourceViewer sourceViewer;

	public DelegatingFallbackHover(ITextEditor editor, ISourceViewer sourceViewer) {
		this.editor = editor;
		this.sourceViewer = sourceViewer;
	}

	@Override
	public IRegion getHoverRegion(ITextViewer textViewer, int offset) {
		IRegion r = super.getHoverRegion(textViewer, offset);
		return r != null ? r : new Region(offset, 0);
	}

	@Override
	public Object getHoverInfo2(ITextViewer textViewer, IRegion hoverRegion) {
		// 1. 其它插件（扩展点注册的同 content type provider）
		List<ITextHover> others = GenericEditorPlugin.getDefault().getHoverRegistry().getAvailableHovers(sourceViewer,
				editor, collectContentTypes(textViewer.getDocument()));

		for (ITextHover other : others) {
			IRegion r = other.getHoverRegion(textViewer, hoverRegion.getOffset());
			if (r == null)
				continue;
			Object info = other.getHoverInfo(textViewer, r);
			if (hasContent(info)) {
				return info; // 其它插件有内容 → 直接用它的
			}
		}

		// 2. 其它插件没有/只有空内容 → 兜底用自己
		return super.getHoverInfo2(textViewer, hoverRegion);
	}

	private boolean hasContent(Object info) {
		if (info == null)
			return false;
		String s = String.valueOf(info).trim();
		String text = s;
		try {
			Document doc = Jsoup.parse(s);
			text = doc.text().trim();
		} catch (Exception e) {
		}
		return !s.isEmpty() && !"null".equalsIgnoreCase(s) && !"any".equalsIgnoreCase(text);
	}

	private Set<IContentType> collectContentTypes(IDocument doc) {
		Set<IContentType> types = new LinkedHashSet<>();
		ITextFileBuffer buffer = FileBuffers.getTextFileBufferManager().getTextFileBuffer(doc);
		if (buffer != null) {
			try {
				collect(buffer.getContentType(), types);
			} catch (CoreException e) {
				e.printStackTrace();
			}
		}
		return types;
	}

	private void collect(IContentType ct, Set<IContentType> out) {
		if (ct == null || !out.add(ct))
			return;
		IContentType base = ct.getBaseType();
		collect(base, out);
	}

	@Override
	public String getHoverInfo(ITextViewer textViewer, IRegion hoverRegion) {
		Object info = getHoverInfo2(textViewer, hoverRegion);
		if (info instanceof TernBrowserInformationControlInput) {
			TernBrowserInformationControlInput tbis = (TernBrowserInformationControlInput) info;
			return tbis.getHtml();
		}
		return info == null ? null : String.valueOf(info);
	}
}
