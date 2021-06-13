/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.ui.util;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.FontMetrics;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

public class WidgetUtil {

	public static Button createButton(Composite c, int style, String name, int minWidth, GridData d) {
		Button b = new Button(c, style);
		b.setText(name);

		int w = b.computeSize(SWT.DEFAULT, SWT.DEFAULT).x;
		if (w < minWidth) {
			d.widthHint = minWidth;
		} else {
			d.widthHint = w;
		}
		b.setLayoutData(d);

		return b;
	}

	public static Label createLabel(Composite c, int style, String name, int minWidth, GridData d) {
		Label l = new Label(c, style);
		l.setText(name);

		int w = l.computeSize(SWT.DEFAULT, SWT.DEFAULT).x;
		if (w < minWidth) {
			d.widthHint = minWidth;
		} else {
			d.widthHint = w;
		}
		l.setLayoutData(d);

		return l;
	}


}
