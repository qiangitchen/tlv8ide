/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.diff;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.compare.CompareConfiguration;
import org.eclipse.compare.contentmergeviewer.TextMergeViewer;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.text.IDocumentPartitioner;
import org.eclipse.jface.text.TextViewer;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.source.SourceViewer;
import org.eclipse.swt.widgets.Composite;

import zigen.plugin.db.ui.views.internal.ColorManager;
import zigen.plugin.db.ui.views.internal.SQLCodeConfiguration;

public class DDLDiffViewer extends TextMergeViewer {

	List fSourceViewer;

	protected ColorManager colorManager;

	protected SQLCodeConfiguration sqlConfiguration;

	protected IDocumentPartitioner partitioner;

	public DDLDiffViewer(Composite parent, CompareConfiguration configuration) {
		super(parent, configuration);

	}

	public void addToolItems(IToolBarManager tbm) {
		tbm.add(new Separator("modes")); //$NON-NLS-1$
		tbm.add(new Separator("navigation")); //$NON-NLS-1$
		tbm.add(new Separator("merge")); //$NON-NLS-1$

		createToolItems((ToolBarManager) tbm);

		hideGroupItems(tbm.getItems(), "merge"); //$NON-NLS-1$

	}

	private void hideGroupItems(IContributionItem[] items, String groupId) {
		boolean readingGroupItems = false;
		for (int i = 0; i < items.length; ++i) {
			IContributionItem item = items[i];
			if (readingGroupItems) {
				item.setVisible(false);
			}
			if (item instanceof Separator && item.getId() != null) {
				readingGroupItems = groupId.equals(item.getId());
			}
		}
	}

	public void setInput(Object input) {

		if (input instanceof IDDLDiff) {
			if (fSourceViewer != null) {
				Iterator iterator = fSourceViewer.iterator();
				while (iterator.hasNext()) {
					try {
						SourceViewer sourceViewer = (SourceViewer) iterator.next();
						sourceViewer.unconfigure();

						SQLCodeConfiguration config = getSourceViewerConfiguration(sourceViewer);
						sourceViewer.configure(config);

						// IDocument doc = sourceViewer.getDocument();
						// IDocumentPartitioner partitioner = new FastPartitioner(new SQLPartitionScanner(), new String[] { SQLPartitionScanner.SQL_STRING, SQLPartitionScanner.SQL_COMMENT });
						// partitioner.connect(doc);
						// config.updatePreferences(doc);

					} catch (RuntimeException e) {
						e.printStackTrace();
					}
				}
			}
		}

		super.setInput(input);

	}

	private SQLCodeConfiguration getSourceViewerConfiguration(ISourceViewer sourceViewer) {
		if (colorManager == null)
			colorManager = new ColorManager();

		sqlConfiguration = new SQLCodeConfiguration(colorManager);
		return sqlConfiguration;
	}

	protected void configureTextViewer(TextViewer textViewer) {
		if (textViewer instanceof SourceViewer) {
			if (fSourceViewer == null)
				fSourceViewer = new ArrayList();
			fSourceViewer.add(textViewer);
			// ((SourceViewer)textViewer).configure(getSourceViewerConfiguration());
		}
	}

}
