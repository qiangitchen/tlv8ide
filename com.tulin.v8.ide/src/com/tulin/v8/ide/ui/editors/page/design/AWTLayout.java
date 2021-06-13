package com.tulin.v8.ide.ui.editors.page.design;

import java.awt.Dimension;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Layout;

public abstract class AWTLayout extends Layout {
	public static final String KEY_PREFERRED_SIZE = "preferredSize";

	protected Point getPreferredSize(Control paramControl, int paramInt1,
			int paramInt2, boolean paramBoolean) {
		Dimension localDimension = (Dimension) paramControl
				.getData("preferredSize");
		if (localDimension != null)
			return new Point(localDimension.width, localDimension.height);
		return paramControl.computeSize(paramInt1, paramInt2, paramBoolean);
	}
}
