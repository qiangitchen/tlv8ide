/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.ui.editors.internal.wizard;

import org.eclipse.jface.text.source.CompositeRuler;
import org.eclipse.jface.text.source.LineNumberRulerColumn;
import org.eclipse.jface.wizard.IWizard;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;

import zigen.plugin.db.DbPlugin;
import zigen.plugin.db.DbPluginConstant;
import zigen.plugin.db.core.SQLFormatter;
import zigen.plugin.db.preference.SQLEditorPreferencePage;
import zigen.plugin.db.preference.SQLFormatPreferencePage;
import zigen.plugin.db.ui.util.LineNumberRulerColumnUtil;
import zigen.plugin.db.ui.views.internal.ColorManager;
import zigen.plugin.db.ui.views.internal.SQLCodeConfiguration;
import zigen.plugin.db.ui.views.internal.SQLDocument;
import zigen.plugin.db.ui.views.internal.SQLSourceViewer;

public class ConfirmDDLWizardPage extends DefaultWizardPage {

	public static final String MSG_DSC = Messages.getString("ConfirmDDLWizardPage.0"); //$NON-NLS-1$

	protected SQLDocument document;

	protected ColorManager colorManager = new ColorManager();

	public ConfirmDDLWizardPage() {
		super(Messages.getString("ConfirmDDLWizardPage.1")); //$NON-NLS-1$
		setTitle(Messages.getString("ConfirmDDLWizardPage.2")); //$NON-NLS-1$
		setPageComplete(false);
	}

	public void createControl(Composite parent) {
		Composite container = createDefaultComposite(parent);
		createDDLConstrol(container);
		// createColumnSelectConstrol(container);
		// createOptionControl(container);
		setControl(container);
	}

	protected void createDDLConstrol(Composite container) {
		Composite composite = new Composite(container, SWT.NONE);
		composite.setLayout(new FillLayout());
		composite.setLayoutData(new GridData(GridData.FILL_BOTH));

		CompositeRuler ruler = new CompositeRuler();
		LineNumberRulerColumn rulerCol = new LineNumberRulerColumn();
		LineNumberRulerColumnUtil.changeColor(colorManager, rulerCol);
		ruler.addDecorator(0, rulerCol);
		ruler.setFont(DbPlugin.getSmallFont());
		SQLSourceViewer sourceViewer = new SQLSourceViewer(composite, ruler, null, false, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);

		sourceViewer.getTextWidget().setFont(DbPlugin.getSmallFont());
		SQLCodeConfiguration sqlConfiguration = new SQLCodeConfiguration(colorManager);
		sourceViewer.configure(sqlConfiguration);

		document = new SQLDocument();
		sourceViewer.setDocument(document);
		sourceViewer.setEditable(false);

	}

	public void setVisible(boolean visible) {
		super.setVisible(visible);
		if (visible) {
			generateSQL();
			setDescription(MSG_DSC);
		} else {
			setPageComplete(false);
		}
	}

	public void generateSQL() {
		// boolean onPatch = DbPlugin.getDefault().getPreferenceStore().getBoolean(SQLEditorPreferencePage.P_FORMAT_PATCH);
		boolean onPatch = DbPlugin.getDefault().getPreferenceStore().getBoolean(SQLFormatPreferencePage.P_FORMAT_PATCH);
		int type = DbPlugin.getDefault().getPreferenceStore().getInt(SQLFormatPreferencePage.P_USE_FORMATTER_TYPE);


		String demiliter = DbPlugin.getDefault().getPreferenceStore().getString(SQLEditorPreferencePage.P_SQL_DEMILITER);
		document.set("");

		IWizard wiz = getWizard();
		if (wiz instanceof IConfirmDDLWizard) {
			IConfirmDDLWizard _wiz = (IConfirmDDLWizard) wiz;
			try {
				StringBuffer sb = new StringBuffer();
				String[] sqls = _wiz.createSQL();

				for (int i = 0; i < sqls.length; i++) {
					String string = sqls[i];
					if (string != null && !"".equals(string)) { //$NON-NLS-1$
						sb.append(SQLFormatter.format(sqls[i], type, onPatch));
						if ("/".equals(demiliter)) { //$NON-NLS-1$
							sb.append(DbPluginConstant.LINE_SEP);
						}
						sb.append(demiliter);
						sb.append(DbPluginConstant.LINE_SEP);
						sb.append(DbPluginConstant.LINE_SEP);
					}
				}

				if ("".equals(sb.toString().trim())) { //$NON-NLS-1$
					updateStatus(Messages.getString("ConfirmDDLWizardPage.6")); //$NON-NLS-1$
					setPageComplete(false);
				} else {
					updateStatus(null);
					document.set(sb.toString());
					setPageComplete(true);
				}

			} catch (Exception e) {
				DbPlugin.getDefault().showErrorDialog(e);
			}
		}
	}

	public SQLDocument getDocument() {
		return document;
	}

}
