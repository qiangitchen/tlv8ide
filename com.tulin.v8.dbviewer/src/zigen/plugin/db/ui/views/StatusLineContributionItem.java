/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.ui.views;

import org.eclipse.jface.action.ContributionItem;
import org.eclipse.jface.action.IContributionManager;
import org.eclipse.jface.action.StatusLineLayoutData;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.graphics.FontMetrics;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

public class StatusLineContributionItem extends ContributionItem {

	public final static int DEFAULT_CHAR_WIDTH = 14;

	private int charWidth;

	private CLabel label;

	private Composite statusLine = null;

	private String text = ""; //$NON-NLS-1$

	private int widthHint = -1;

	private int heightHint = -1;

	public StatusLineContributionItem(String id) {
		this(id, DEFAULT_CHAR_WIDTH);
	}

	public StatusLineContributionItem(String id, int charWidth) {
		super(id);
		this.charWidth = charWidth;
		setVisible(false);
	}

	public void fill(Composite parent) {
		statusLine = parent;

		Label sep = new Label(parent, SWT.SEPARATOR);
		label = new CLabel(statusLine, SWT.SHADOW_NONE);

		if (widthHint < 0) {
			GC gc = new GC(statusLine);
			gc.setFont(statusLine.getFont());
			FontMetrics fm = gc.getFontMetrics();
			widthHint = fm.getAverageCharWidth() * charWidth;
			heightHint = fm.getHeight();
			gc.dispose();
		}

		StatusLineLayoutData data = new StatusLineLayoutData();
		data.widthHint = widthHint;
		label.setLayoutData(data);
		label.setText(text);

		data = new StatusLineLayoutData();
		data.heightHint = heightHint;
		sep.setLayoutData(data);

	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		if (text == null)
			throw new NullPointerException();

		this.text = text;

		if (label != null && !label.isDisposed())
			label.setText(this.text);

		if (this.text.length() == 0) {
			if (isVisible()) {
				setVisible(false);
				IContributionManager contributionManager = getParent();

				if (contributionManager != null)
					contributionManager.update(true);
			}
		} else {
			if (!isVisible()) {
				setVisible(true);
				IContributionManager contributionManager = getParent();

				if (contributionManager != null)
					contributionManager.update(true);
			}
		}
	}
}
