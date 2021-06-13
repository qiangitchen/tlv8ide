/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.ui.views.internal;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.contentassist.ContentAssistant;
import org.eclipse.jface.text.contentassist.IContentAssistant;
import org.eclipse.jface.text.formatter.ContentFormatter;
import org.eclipse.jface.text.formatter.IContentFormatter;
import org.eclipse.jface.text.presentation.IPresentationReconciler;
import org.eclipse.jface.text.presentation.PresentationReconciler;
import org.eclipse.jface.text.rules.BufferedRuleBasedScanner;
import org.eclipse.jface.text.rules.DefaultDamagerRepairer;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.source.SourceViewerConfiguration;

import zigen.plugin.db.DbPlugin;
import zigen.plugin.db.preference.CodeAssistPreferencePage;
import zigen.plugin.db.preference.SQLEditorPreferencePage;
import zigen.plugin.db.ui.contentassist.SQLContentAssistantProcessor2;

public class SQLCodeConfiguration extends SourceViewerConfiguration {

	ISQLTokenScanner keyWorkScanner;

	ColorManager colorManager;

	PresentationReconciler reconciler;

	IPreferenceStore preferenceStore;

	public SQLCodeConfiguration(ColorManager colorManager) {
		this.colorManager = colorManager;
		this.preferenceStore = DbPlugin.getDefault().getPreferenceStore();
	}

	protected ISQLTokenScanner getSQLKeywordScanner() {
		if (keyWorkScanner == null) {
			keyWorkScanner = new SQLKeywordScanner(colorManager);
		} else {
			keyWorkScanner.initialize();
		}
		return keyWorkScanner;
	}


	public String[] getConfiguredContentTypes(ISourceViewer sourceViewer) {
		return new String[] {IDocument.DEFAULT_CONTENT_TYPE, SQLPartitionScanner.SQL_COMMENT, SQLPartitionScanner.SQL_STRING};
	}

	static class SingleTokenScanner extends BufferedRuleBasedScanner {

		public SingleTokenScanner(TextAttribute attribute) {
			setDefaultReturnToken(new Token(attribute));
		}
	};

	public IPresentationReconciler getPresentationReconciler(ISourceViewer sourceViewer) {
		reconciler = new PresentationReconciler();
		InitializeDamagerRepairer();
		return reconciler;
	}

	public void updatePreferences(IDocument document) {

		getSQLKeywordScanner().initialize();

		NonRuleBasedDamagerRepairer commentDR = new NonRuleBasedDamagerRepairer(new TextAttribute(colorManager.getColor(SQLEditorPreferencePage.P_COLOR_COMMENT)));
		commentDR.setDocument(document);

		reconciler.setDamager(commentDR, SQLPartitionScanner.SQL_COMMENT);
		reconciler.setRepairer(commentDR, SQLPartitionScanner.SQL_COMMENT);

		NonRuleBasedDamagerRepairer stringDR = new NonRuleBasedDamagerRepairer(new TextAttribute(colorManager.getColor(SQLEditorPreferencePage.P_COLOR_STRING)));
		stringDR.setDocument(document);

		reconciler.setDamager(stringDR, SQLPartitionScanner.SQL_STRING);
		reconciler.setRepairer(stringDR, SQLPartitionScanner.SQL_STRING);


	}

	private void InitializeDamagerRepairer() {

		DefaultDamagerRepairer commentDR = new DefaultDamagerRepairer(new SingleTokenScanner(new TextAttribute(colorManager.getColor(SQLEditorPreferencePage.P_COLOR_COMMENT))));
		reconciler.setDamager(commentDR, SQLPartitionScanner.SQL_COMMENT);
		reconciler.setRepairer(commentDR, SQLPartitionScanner.SQL_COMMENT);

		DefaultDamagerRepairer stringDR = new DefaultDamagerRepairer(new SingleTokenScanner(new TextAttribute(colorManager.getColor(SQLEditorPreferencePage.P_COLOR_STRING))));
		reconciler.setDamager(stringDR, SQLPartitionScanner.SQL_STRING);
		reconciler.setRepairer(stringDR, SQLPartitionScanner.SQL_STRING);

		DefaultDamagerRepairer keywordDR = new DefaultDamagerRepairer(getSQLKeywordScanner());
		reconciler.setDamager(keywordDR, IDocument.DEFAULT_CONTENT_TYPE);
		reconciler.setRepairer(keywordDR, IDocument.DEFAULT_CONTENT_TYPE);

	}

	public IContentAssistant getContentAssistant(ISourceViewer sourceViewer) {
		ContentAssistant assistant = new ContentAssistant();

		assistant.setDocumentPartitioning(getConfiguredDocumentPartitioning(sourceViewer));
		// IContentAssistProcessor p = new SQLCompletionProcessor();
		// assistant.setContentAssistProcessor(p,
		// IDocument.DEFAULT_CONTENT_TYPE);

		SQLContentAssistantProcessor2 processor = new SQLContentAssistantProcessor2();
		assistant.setContentAssistProcessor(processor, IDocument.DEFAULT_CONTENT_TYPE);

		assistant.setInformationControlCreator(getInformationControlCreator(sourceViewer));

		assistant.enableAutoActivation(true);

		int delayTime = preferenceStore.getInt(CodeAssistPreferencePage.P_SQL_CODE_ASSIST_AUTO_ACTIVATE_DELAY_TIME);
		assistant.setAutoActivationDelay(delayTime);

		assistant.enableAutoInsert(true);

		// //assistant.install(sourceViewer);
		return assistant;
	}

	public IContentFormatter getContentFormatter(ISourceViewer sourceViewer) {
		ContentFormatter formatter = new ContentFormatter();
		formatter.setFormattingStrategy(new SQLFormattingStrategy(sourceViewer), IDocument.DEFAULT_CONTENT_TYPE);
		formatter.setFormattingStrategy(new SQLFormattingStrategy(sourceViewer), SQLPartitionScanner.SQL_STRING);
		return formatter;
	}

}
