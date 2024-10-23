package com.tulin.v8.webtools.ide.assist;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.Region;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.jface.text.templates.Template;
import org.eclipse.jface.text.templates.TemplateCompletionProcessor;
import org.eclipse.jface.text.templates.TemplateContext;
import org.eclipse.jface.text.templates.TemplateContextType;
import org.eclipse.jface.text.templates.TemplateException;
import org.eclipse.swt.graphics.Image;

import com.tulin.v8.webtools.ide.WebToolsPlugin;
import com.tulin.v8.webtools.ide.template.HTMLContextType;
import com.tulin.v8.webtools.ide.template.HTMLTemplateManager;

public class HTMLTemplateAssistProcessor extends TemplateCompletionProcessor {

	protected Template[] getTemplates(String contextTypeId) {
		HTMLTemplateManager manager = HTMLTemplateManager.getInstance();
		return manager.getTemplateStore().getTemplates();
	}

	@SuppressWarnings("deprecation")
	protected TemplateContextType getContextType(ITextViewer viewer, IRegion region) {
		HTMLTemplateManager manager = HTMLTemplateManager.getInstance();
		return manager.getContextTypeRegistry().getContextType(HTMLContextType.CONTEXT_TYPE);
	}

	protected Image getImage(Template template) {
		return WebToolsPlugin.getDefault().getImageRegistry().get(WebToolsPlugin.ICON_TEMPLATE);
	}

	public ICompletionProposal[] computeCompletionProposals(ITextViewer viewer, int offset) {

		// 提示过程中获取选中会导致线程异常
//		ITextSelection selection = (ITextSelection) viewer.getSelectionProvider().getSelection();

		// adjust offset to end of normalized selection
//		if (selection.getOffset() == offset)
//			offset = selection.getOffset() + selection.getLength();

		String prefix = extractPrefix(viewer, offset);
		Region region = new Region(offset - prefix.length(), prefix.length());
		TemplateContext context = createContext(viewer, region);
		if (context == null)
			return new ICompletionProposal[0];

//		context.setVariable("selection", selection.getText()); // name of the selection variables {line, //$NON-NLS-1$
		// word}_selection

		Template[] templates = getTemplates(context.getContextType().getId());

		List<ICompletionProposal> matches = new ArrayList<ICompletionProposal>();
		for (int i = 0; i < templates.length; i++) {
			Template template = templates[i];
			try {
				context.getContextType().validate(template.getPattern());
			} catch (TemplateException e) {
				continue;
			}
			if (template.getName().startsWith(prefix) && template.matches(prefix, context.getContextType().getId()))
				matches.add(createProposal(template, context, (IRegion) region, getRelevance(template, prefix)));
		}

		return matches.toArray(new ICompletionProposal[matches.size()]);
	}

}
