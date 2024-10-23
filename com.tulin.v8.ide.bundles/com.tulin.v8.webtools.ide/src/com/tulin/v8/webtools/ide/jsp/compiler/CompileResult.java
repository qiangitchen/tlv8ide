package com.tulin.v8.webtools.ide.jsp.compiler;

/**
 * @see JSPCompiler
 */
public class CompileResult {

	private String header;
	private String body;
	private String footer;

	public CompileResult(String header, String body, String footer) {
		this.header = header;
		this.body = body;
		this.footer = footer;
	}

	public String getHeader() {
		return this.header;
	}

	public String getBody() {
		return this.body;
	}

	public String getFooter() {
		return this.footer;
	}

	@Override
	public String toString() {
		return header + body + footer;
	}

}
