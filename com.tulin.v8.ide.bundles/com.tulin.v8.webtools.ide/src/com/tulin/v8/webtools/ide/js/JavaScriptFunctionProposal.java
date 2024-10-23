package com.tulin.v8.webtools.ide.js;

import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.contentassist.ICompletionProposalExtension4;
import org.eclipse.jface.text.templates.Template;
import org.eclipse.jface.text.templates.TemplateContext;
import org.eclipse.jface.text.templates.TemplateProposal;
import org.eclipse.swt.graphics.Image;

public class JavaScriptFunctionProposal extends TemplateProposal implements ICompletionProposalExtension4 {

	private String additionalInfo;

	public JavaScriptFunctionProposal(Template template, TemplateContext context, IRegion region, Image image,
			String additionalInfo) {
		super(template, context, region, image);
		this.additionalInfo = additionalInfo;
	}

	public JavaScriptFunctionProposal(Template template, TemplateContext context, IRegion region, Image image,
			String additionalInfo, int relevance) {
		super(template, context, region, image, relevance);
		this.additionalInfo = additionalInfo;
	}

	public String getAdditionalProposalInfo() {
		return this.additionalInfo;
	}

	public boolean isAutoInsertable() {
		return false;
	}

}
