/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.ui.editors.internal.wizard;

import org.eclipse.jface.text.ITextListener;
import org.eclipse.jface.text.ITextViewerExtension2;
import org.eclipse.jface.text.TextEvent;
import org.eclipse.jface.text.source.CompositeRuler;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.source.LineNumberRulerColumn;
import org.eclipse.jface.text.source.MatchingCharacterPainter;
import org.eclipse.jface.text.source.SourceViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import zigen.plugin.db.DbPlugin;
import zigen.plugin.db.core.rule.ISQLCreatorFactory;
import zigen.plugin.db.preference.SQLEditorPreferencePage;
import zigen.plugin.db.ui.editors.event.TextSelectionListener;
import zigen.plugin.db.ui.internal.ITable;
import zigen.plugin.db.ui.util.LineNumberRulerColumnUtil;
import zigen.plugin.db.ui.util.StyledTextUtil;
import zigen.plugin.db.ui.views.internal.ColorManager;
import zigen.plugin.db.ui.views.internal.SQLCharacterPairMatcher;
import zigen.plugin.db.ui.views.internal.SQLCodeConfiguration;
import zigen.plugin.db.ui.views.internal.SQLDocument;

public class CheckWizardPage extends DefaultWizardPage {

	public static final String MSG_DSC = Messages.getString("CheckWizardPage.0"); //$NON-NLS-1$

	private static final String MSG_REQUIRE_NAME = Messages.getString("CheckWizardPage.1"); //$NON-NLS-1$

	private static final String MSG_REQUIRE_CHECK = Messages.getString("CheckWizardPage.2"); //$NON-NLS-1$

	protected ISQLCreatorFactory factory;

	protected ITable tableNode;

	protected Text txtConstraintName;

	// protected ProjectionViewer sqlViewer;
	protected SourceViewer sqlViewer;

	protected LineNumberRulerColumn rulerCol;

	protected SQLCodeConfiguration sqlConfiguration;

	protected ColorManager colorManager = new ColorManager();

	public CheckWizardPage(ISQLCreatorFactory factory, ITable table) {

		super("wizardPage"); //$NON-NLS-1$
		setTitle(Messages.getString("CheckWizardPage.4")); //$NON-NLS-1$
		// setPageComplete(false);
		this.factory = factory;
		this.tableNode = table;

	}

	public void createControl(Composite parent) {
		Composite container = createDefaultComposite(parent);

		Composite composite = new Composite(container, SWT.NULL);
		composite.setLayout(new GridLayout(2, false));
		// composite.setLayoutData(new GridData(GridData.FILL_BOTH));
		composite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		Label label = new Label(composite, SWT.NULL);
		label.setText(Messages.getString("CheckWizardPage.5")); //$NON-NLS-1$
		txtConstraintName = new Text(composite, SWT.BORDER);
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		// gd.horizontalSpan = 2;
		txtConstraintName.setLayoutData(gd);
		txtConstraintName.setText(""); //$NON-NLS-1$
		txtConstraintName.addFocusListener(new TextSelectionListener());
		txtConstraintName.addModifyListener(new ModifyListener() {

			public void modifyText(ModifyEvent e) {
				if ("".equals(txtConstraintName.getText())) { //$NON-NLS-1$
					updateStatus(MSG_REQUIRE_NAME);
				} else if ("".equals(sqlViewer.getDocument().get().trim())) { //$NON-NLS-1$
					updateStatus(MSG_REQUIRE_CHECK);
				} else {
					updateStatus(null);
				}
			}
		});

		Composite main = new Composite(container, SWT.BORDER);
		gd = new GridData(GridData.FILL_BOTH);
		gd.horizontalSpan = 2;
		main.setLayoutData(gd);
		main.setLayout(new FillLayout());

		CompositeRuler ruler = new CompositeRuler();
		rulerCol = new LineNumberRulerColumn();
		LineNumberRulerColumnUtil.changeColor(colorManager, rulerCol);
		ruler.addDecorator(0, rulerCol);

		// sqlViewer = new ProjectionViewer(main, ruler, null, false, SWT.MULTI
		// | SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);
		sqlViewer = new SourceViewer(main, ruler, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);
		sqlViewer.addTextListener(new TextListener());

		initializeViewerFont(sqlViewer);
		sqlConfiguration = new SQLCodeConfiguration(colorManager);
		sqlViewer.configure(sqlConfiguration);
		sqlViewer.setDocument(new SQLDocument());
		ITextViewerExtension2 extension = (ITextViewerExtension2) sqlViewer;
		MatchingCharacterPainter painter = new MatchingCharacterPainter(sqlViewer, new SQLCharacterPairMatcher());
		painter.setColor(colorManager.getColor(SQLEditorPreferencePage.P_COLOR_MATCHING));
		extension.addPainter(painter);
		StyledTextUtil.changeColor(colorManager, sqlViewer.getTextWidget());

		setControl(container);
	}

	protected void initializeViewerFont(ISourceViewer viewer) {
		StyledText styledText = viewer.getTextWidget();
		styledText.setFont(DbPlugin.getDefaultFont());
	}

	protected void update() {
		if ("".equals(txtConstraintName.getText())) { //$NON-NLS-1$
			updateStatus(MSG_REQUIRE_NAME);
		} else if ("".equals(sqlViewer.getDocument().get().trim())) { //$NON-NLS-1$
			updateStatus(MSG_REQUIRE_CHECK);
		} else {
			updateStatus(null);
		}

	}

	public void setVisible(boolean visible) {
		super.setVisible(visible);
		if (visible) {
			setDescription(MSG_DSC);
			update();
		}
	}

	private class TextListener implements ITextListener {

		public void textChanged(TextEvent event) {
			update();
		}
	}
}
