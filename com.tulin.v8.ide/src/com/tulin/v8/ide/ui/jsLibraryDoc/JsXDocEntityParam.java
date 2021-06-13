package com.tulin.v8.ide.ui.jsLibraryDoc;

public class JsXDocEntityParam extends JsEntityValidator {
	private String a;
	private String b;
	private String c;

	public String getDescription() {
		return this.c;
	}

	public void setDescription(String paramString) {
		this.c = paramString;
	}

	public String getName() {
		return this.a;
	}

	public void setName(String paramString) {
		this.a = paramString;
		validateName();
	}

	public String getType() {
		return this.b;
	}

	public void setType(String paramString) {
		this.b = paramString;
	}

	public void validateName() {
		this.a = ((this.a.matches(PATTERN_JSVAR))
				&& (!KEY_WORDS.contains(this.a)) ? this.a : "param");
	}
	
}
