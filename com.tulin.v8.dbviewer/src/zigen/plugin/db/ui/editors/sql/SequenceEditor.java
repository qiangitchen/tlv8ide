/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.ui.editors.sql;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.text.source.CompositeRuler;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.source.LineNumberRulerColumn;
import org.eclipse.jface.text.source.SourceViewer;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.EditorPart;

import zigen.plugin.db.DbPlugin;
import zigen.plugin.db.core.IDBConfig;
import zigen.plugin.db.core.StringUtil;
import zigen.plugin.db.ext.oracle.internal.OracleSequenceInfo;
import zigen.plugin.db.ui.util.LineNumberRulerColumnUtil;
import zigen.plugin.db.ui.util.StyledTextUtil;
import zigen.plugin.db.ui.views.internal.ColorManager;
import zigen.plugin.db.ui.views.internal.PLSQLCodeConfiguration;
import zigen.plugin.db.ui.views.internal.SQLDocument;

public class SequenceEditor extends EditorPart implements IPropertyChangeListener {

	private final String LINE_SEP = System.getProperty("line.separator"); //$NON-NLS-1$

	protected IDBConfig config;

	protected SourceViewer sqlViewer;

	private OracleSequenceInfo sequenceInfo;

	protected LineNumberRulerColumn rulerCol;

	protected PLSQLCodeConfiguration sqlConfiguration;

	protected ColorManager colorManager = new ColorManager();

	protected IPreferenceStore store;

	protected boolean dirty = false;

	protected Text errorText;

	protected SashForm sash;

	public SequenceEditor() {
		colorManager = new ColorManager();
		sqlConfiguration = new PLSQLCodeConfiguration(colorManager);
		this.store = DbPlugin.getDefault().getPreferenceStore();
		this.store.addPropertyChangeListener(this);
	}

	public void createPartControl(Composite parent) {

		sash = new SashForm(parent, SWT.VERTICAL | SWT.NONE);

		CompositeRuler ruler = new CompositeRuler();
		LineNumberRulerColumn rulerCol = new LineNumberRulerColumn();
		LineNumberRulerColumnUtil.changeColor(colorManager, rulerCol);
		ruler.addDecorator(0, rulerCol);

		sqlViewer = new SourceViewer(sash, ruler, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);

		initializeViewerFont(sqlViewer);

		sqlViewer.configure(sqlConfiguration);

		SQLDocument doc = new SQLDocument();

		sqlViewer.setDocument(doc);
		sqlViewer.getDocument().set(getDDL());
		sqlViewer.setEditable(false);

		errorText = new Text(sash, SWT.MULTI);
		errorText.setEditable(false);
		Color white = Display.getCurrent().getSystemColor(SWT.COLOR_WHITE);
		Color red = Display.getCurrent().getSystemColor(SWT.COLOR_RED);
		errorText.setBackground(white);
		errorText.setForeground(red);

		sash.setWeights(new int[] {100, 0});

		// hookContextMenu();
	}

	// private void hookContextMenu() {
	// MenuManager menuMgr = new MenuManager("#PopupMenu");
	// menuMgr.setRemoveAllWhenShown(true);
	// menuMgr.addMenuListener(new IMenuListener() {
	// public void menuAboutToShow(IMenuManager manager) {
	// getContributor().fillContextMenu(manager);
	// }
	// });
	// Menu menu = menuMgr.createContextMenu(sqlViewer.getControl());
	// sqlViewer.getControl().setMenu(menu);
	// getSite().registerContextMenu(menuMgr, sqlViewer);
	// }

	// protected void setGlobalAction() {
	// IActionBars actionbars = getEditorSite().getActionBars();
	// actionbars.setGlobalActionHandler(ActionFactory.UNDO.getId(), new
	// GlobalAction(sqlViewer, ITextOperationTarget.UNDO));
	// actionbars.setGlobalActionHandler(ActionFactory.REDO.getId(), new
	// GlobalAction(sqlViewer, ITextOperationTarget.REDO));
	// actionbars.setGlobalActionHandler(ActionFactory.DELETE.getId(), new
	// GlobalAction(sqlViewer, ITextOperationTarget.DELETE));
	// actionbars.setGlobalActionHandler(ActionFactory.SELECT_ALL.getId(), new
	// GlobalAction(sqlViewer, ITextOperationTarget.SELECT_ALL));
	// actionbars.setGlobalActionHandler(ActionFactory.COPY.getId(), new
	// GlobalAction(sqlViewer, ITextOperationTarget.COPY));
	// actionbars.setGlobalActionHandler(ActionFactory.PASTE.getId(), new
	// GlobalAction(sqlViewer, ITextOperationTarget.PASTE));
	// actionbars.setGlobalActionHandler(ActionFactory.CUT.getId(), new
	// GlobalAction(sqlViewer, ITextOperationTarget.CUT));
	// }

	public void init(IEditorSite site, IEditorInput editorInput) throws PartInitException {
		try {
			setSite(site);
			setInput(editorInput);

			if (editorInput instanceof SequenceEditorInput) {
				SequenceEditorInput input = (SequenceEditorInput) editorInput;

				this.sequenceInfo = input.getSequenceInfo();
				config = input.getConfig();
				setPartName(input.getName());
			}

		} catch (Exception e) {
			DbPlugin.getDefault().showErrorDialog(e);
		}
	}

	private String getDDL() {
		StringBuffer sb = new StringBuffer();
		sb.append("CREATE"); //$NON-NLS-1$
		sb.append(" SEQUENCE "); //$NON-NLS-1$

		if (StringUtil.isNumeric(sequenceInfo.getSequece_owner())) {
			sb.append("\"");
			sb.append(sequenceInfo.getSequece_owner());
			sb.append("\"");

		} else {
			sb.append(sequenceInfo.getSequece_owner());
		}


		sb.append("."); //$NON-NLS-1$
		sb.append(sequenceInfo.getSequence_name());
		sb.append(LINE_SEP);

		sb.append("     INCREMENT BY " + sequenceInfo.getIncrement_by()); //$NON-NLS-1$
		sb.append(LINE_SEP);

		sb.append("     START WITH " + sequenceInfo.getLast_number()); //$NON-NLS-1$
		sb.append(LINE_SEP);
		sb.append("     MAXVALUE " + sequenceInfo.getMax_value()); //$NON-NLS-1$
		sb.append(LINE_SEP);
		sb.append("     MINVALUE " + sequenceInfo.getMin_value()); //$NON-NLS-1$
		sb.append(LINE_SEP);

		if ("Y".equals(sequenceInfo.getCycle_flg())) { //$NON-NLS-1$
			sb.append("     CYCLE "); //$NON-NLS-1$
		} else {
			sb.append("     NOCYCLE "); //$NON-NLS-1$
		}

		sb.append(LINE_SEP);
		sb.append("     CACHE "); //$NON-NLS-1$
		sb.append(sequenceInfo.getCache_size());

		sb.append(LINE_SEP);
		if ("Y".equals(sequenceInfo.getOrder_flg())) { //$NON-NLS-1$
			sb.append("     ORDER "); //$NON-NLS-1$
		} else {
			sb.append("     NOORDER "); //$NON-NLS-1$
		}

		sb.append(LINE_SEP);
		return sb.toString();

	}

	protected void initializeViewerFont(ISourceViewer viewer) {
		StyledText styledText = viewer.getTextWidget();
		styledText.setFont(DbPlugin.getDefaultFont());
	}

	public void propertyChange(PropertyChangeEvent event) {
		if (sqlConfiguration != null && sqlViewer != null) {
			sqlConfiguration.updatePreferences(sqlViewer.getDocument());
			StyledTextUtil.changeColor(colorManager, sqlViewer.getTextWidget());
			// LineNumberRulerColumnUtil.changeColor(colorManager, rulerCol);
			sqlViewer.invalidateTextPresentation();
		}
	}

	public void doSave(IProgressMonitor monitor) {}

	public void doSaveAs() {}

	public boolean isDirty() {
		return false;
	}

	public boolean isSaveAsAllowed() {
		return true;
	}

	public void setFocus() {
	}

	public void dispose() {
		super.dispose();
		colorManager.dispose();
		DbPlugin.getDefault().getPreferenceStore().removePropertyChangeListener(this);
	}

}
