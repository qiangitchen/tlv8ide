package com.tulin.v8.webtools.ide.assist;

import org.eclipse.jface.text.contentassist.CompletionProposal;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.swt.graphics.Image;

import com.tulin.v8.webtools.ide.html.HTMLUtil;

public class AssistInfo {

	private String displayString;
	private String replaceString;
	private Image image;
	private String additionalInfo;

	public AssistInfo(String displayString) {
		this.displayString = displayString;
		this.replaceString = displayString;
	}

	public AssistInfo(String displayString, Image image) {
		this.displayString = displayString;
		this.replaceString = displayString;
		this.image = image;
	}

	public AssistInfo(String replaceString, String displayString) {
		this.displayString = displayString;
		this.replaceString = replaceString;
	}

	public AssistInfo(String replaceString, String displayString, Image image) {
		this.displayString = displayString;
		this.replaceString = replaceString;
		this.image = image;
	}

	public AssistInfo(String replaceString, String displayString, Image image, String additionalInfo) {
		this.displayString = displayString;
		this.replaceString = replaceString;
		this.image = image;
		this.additionalInfo = additionalInfo;
	}

	public String getDisplayString() {
		return displayString;
	}

	public String getReplaceString() {
		return replaceString;
	}

	public String getAdditionalInfo() {
		return additionalInfo;
	}

	public Image getImage() {
		return this.image;
	}

	public ICompletionProposal toCompletionProposal(int offset, String matchString, Image defaultImage) {
		return new CompletionProposal(getReplaceString(), offset - matchString.length(), matchString.length(),
				getReplaceString().length(), getImage() == null ? defaultImage : getImage(), getDisplayString(), null,
				getAdditionalInfo());
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof AssistInfo) {
			AssistInfo info = (AssistInfo) obj;
			if (HTMLUtil.compareString(info.getReplaceString(), getReplaceString())
					&& HTMLUtil.compareString(info.getDisplayString(), getDisplayString())
					&& HTMLUtil.compareString(info.getAdditionalInfo(), getAdditionalInfo())) {
				return true;
			}
		}
		return false;
	}

}
