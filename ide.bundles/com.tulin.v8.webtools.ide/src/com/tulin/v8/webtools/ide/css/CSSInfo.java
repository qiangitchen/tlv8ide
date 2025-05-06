package com.tulin.v8.webtools.ide.css;

public class CSSInfo {

	private String replaceString;
	private String displayString;
	private String description;

	public CSSInfo(String replaceString) {
		this.replaceString = replaceString;
		this.displayString = replaceString;
		this.description = CSSStyles.getString(replaceString);
	}

	public CSSInfo(String replaceString, String description) {
		this.replaceString = replaceString;
		this.displayString = replaceString;
		this.description = description;
	}

	public String getDisplayString() {
		return displayString;
	}

	public String getReplaceString() {
		return replaceString;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
