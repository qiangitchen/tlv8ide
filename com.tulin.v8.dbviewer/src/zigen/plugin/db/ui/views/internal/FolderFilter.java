/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.ui.views.internal;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;

import zigen.plugin.db.core.FolderInfo;
import zigen.plugin.db.ui.internal.Folder;
import zigen.plugin.db.ui.internal.TreeLeaf;

public class FolderFilter extends ViewerFilter {

	FolderInfo[] infos;

	Pattern pattern;

	public FolderFilter(FolderInfo[] infos) {
		this.infos = infos;
		createPattern();
	}

	private void createPattern() {
		try {
			StringBuffer sb = new StringBuffer();
			int cnt = 0;
			for (int i = 0; i < infos.length; i++) {
				FolderInfo info = infos[i];
				if (info.isChecked()) {
					if (cnt == 0) {
						sb.append(info.getName());
					} else {
						sb.append("|"); //$NON-NLS-1$
						sb.append(info.getName());
					}
					cnt++;
				}
			}
			pattern = Pattern.compile(sb.toString());
		} catch (PatternSyntaxException e) {
			e.printStackTrace();
			throw e;
		}
	}

	public boolean select(Viewer viewer, Object parent, Object element) {
		if (infos != null) { //$NON-NLS-1$
			if (element instanceof Folder) {
				TreeLeaf leaf = (TreeLeaf) element;
				if (pattern != null) {
					Matcher mc = pattern.matcher(leaf.getName());
					return !mc.matches();
				} else {
					return true;
				}
			} else {
				return true;
			}
		} else {
			return true;
		}
	}

}
