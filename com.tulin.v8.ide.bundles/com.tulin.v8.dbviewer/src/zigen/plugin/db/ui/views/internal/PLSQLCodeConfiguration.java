/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.ui.views.internal;

import org.eclipse.jface.text.contentassist.IContentAssistant;
import org.eclipse.jface.text.source.IAnnotationHover;
import org.eclipse.jface.text.source.ISourceViewer;

public class PLSQLCodeConfiguration extends SQLCodeConfiguration {

	public PLSQLCodeConfiguration(ColorManager colorManager) {
		super(colorManager);
	}

	protected ISQLTokenScanner getSQLKeywordScanner() {
		if (keyWorkScanner == null) {
			keyWorkScanner = new PLSQLKeywordScanner(colorManager);
		} else {
			keyWorkScanner.initialize();
		}
		return keyWorkScanner;
	}

	public IContentAssistant getContentAssistant(ISourceViewer sourceViewer) {
		return null;
	}

	public IAnnotationHover getAnnotationHover(ISourceViewer sourceViewer) {
		return new PlsqlAnnotationHover();
	}
}
