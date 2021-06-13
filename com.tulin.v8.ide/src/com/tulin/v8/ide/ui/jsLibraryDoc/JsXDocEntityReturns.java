package com.tulin.v8.ide.ui.jsLibraryDoc;

public class JsXDocEntityReturns extends JsEntityValidator {
	private String a;
	private String b;

	public String getType() {
		return this.a;
	}

	public void setType(String paramString) {
		this.a = paramString;
		validateType();
	}

	public String getDescription() {
		return this.b;
	}

	public void setDescription(String paramString) {
		this.b = paramString;
	}

	public void validateType() {
		this.a = (this.a.matches(PATTERN_JSFUNCTION) ? this.a : "void");
		if ((!this.a.contains(".")) && (!this.a.equals("void")))
			this.a = JsXDocParser.firstToUpCase(this.a);
	}
}
