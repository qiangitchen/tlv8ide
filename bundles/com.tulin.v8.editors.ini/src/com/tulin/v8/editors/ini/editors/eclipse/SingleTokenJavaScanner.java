package com.tulin.v8.editors.ini.editors.eclipse;

import java.util.List;
import org.eclipse.jdt.ui.text.IColorManager;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.text.rules.IRule;

public final class SingleTokenJavaScanner extends AbstractJavaScanner {
	private String[] fProperty;

	public SingleTokenJavaScanner(IColorManager manager, IPreferenceStore store, String property) {
		super(manager, store);
		this.fProperty = new String[] { property };
		initialize();
	}

	protected String[] getTokenProperties() {
		return this.fProperty;
	}

	protected List<IRule> createRules() {
		setDefaultReturnToken(getToken(this.fProperty[0]));
		return null;
	}
}