/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.ui.util;

import org.eclipse.swt.custom.StyledText;

import zigen.plugin.db.preference.SQLEditorPreferencePage;
import zigen.plugin.db.ui.views.internal.ColorManager;

public class StyledTextUtil {

	public static void changeColor(ColorManager manager, StyledText text) {

		if (text != null) {
			text.setBackground(manager.getColor(SQLEditorPreferencePage.P_COLOR_BACK));

			text.setSelectionForeground(manager.getColor(SQLEditorPreferencePage.P_COLOR_SELECT_FORE));

			text.setSelectionBackground(manager.getColor(SQLEditorPreferencePage.P_COLOR_SELECT_BACK));

		}
	}

}
