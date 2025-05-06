package com.tulin.v8.webtools.ide.html.editors;

import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;

import com.tulin.v8.webtools.ide.WebToolsPlugin;

public class SoftTabVerifyListener implements VerifyListener {

	private boolean useSoftTab;
	private String softTab;

	public void verifyText(VerifyEvent evt) {
		if (useSoftTab) {
			if (evt.text.equals("\t")) {
				evt.text = softTab;
			}
		}
	}

	public void preferenceChanged(PropertyChangeEvent event) {
		String key = event.getProperty();
		if (key.equals(WebToolsPlugin.PREF_USE_SOFTTAB)) {

			Object value = event.getNewValue();
			boolean enable = false;
			if (value instanceof String) {
				enable = Boolean.valueOf((String) value).booleanValue();
			} else if (value instanceof Boolean) {
				enable = ((Boolean) value).booleanValue();
			}
			setUseSoftTab(enable);
		}
		if (key.equals(WebToolsPlugin.PREF_SOFTTAB_WIDTH)) {
			int width = ((Integer) event.getNewValue()).intValue();
			setSoftTabWidth(width);
		}
	}

	public void setUseSoftTab(boolean useSoftTab) {
		this.useSoftTab = useSoftTab;
	}

	public void setSoftTabWidth(int softTabWidth) {
		softTab = "";
		for (int i = 0; i < softTabWidth; i++) {
			softTab = softTab + " ";
		}
	}

}
