/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.ui.editors;

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.part.MultiPageEditorActionBarContributor;

import zigen.plugin.db.core.DBType;
import zigen.plugin.db.core.IDBConfig;
import zigen.plugin.db.csv.CreateCSVForTableAction;
import zigen.plugin.db.csv.CreateCSVForTableWithConditionAction;
import zigen.plugin.db.ui.actions.CopyInsertStatementAction;
import zigen.plugin.db.ui.actions.CopyRecordDataAction;
import zigen.plugin.db.ui.actions.CopyRecordTrimDataAction;
import zigen.plugin.db.ui.actions.DeleteRecordAction;
import zigen.plugin.db.ui.actions.InsertRecordAction;
import zigen.plugin.db.ui.actions.PasteRecordDataAction;
import zigen.plugin.db.ui.actions.SelectAllRecordAction;
import zigen.plugin.db.ui.editors.internal.action.AddCheckAction;
import zigen.plugin.db.ui.editors.internal.action.AddColumnAction;
import zigen.plugin.db.ui.editors.internal.action.AddForeginKeyAction;
import zigen.plugin.db.ui.editors.internal.action.AddIndexAction;
import zigen.plugin.db.ui.editors.internal.action.AddPrimaryKeyAction;
import zigen.plugin.db.ui.editors.internal.action.AddUniqueKeyAction;
import zigen.plugin.db.ui.editors.internal.action.DropColumnAction;
import zigen.plugin.db.ui.editors.internal.action.DropConstraintAction;
import zigen.plugin.db.ui.editors.internal.action.DropIndexAction;
import zigen.plugin.db.ui.editors.internal.action.DuplicateColumnAction;
import zigen.plugin.db.ui.editors.internal.action.EditColumnAction;
import zigen.plugin.db.ui.editors.internal.action.OpenTriggerAction;
import zigen.plugin.db.ui.internal.Constraint;
import zigen.plugin.db.ui.internal.ConstraintRoot;
import zigen.plugin.db.ui.internal.Index;
import zigen.plugin.db.ui.internal.IndexRoot;
import zigen.plugin.db.ui.internal.Trigger;
import zigen.plugin.db.ui.internal.TriggerRoot;

public class TableViewerContributor extends MultiPageEditorActionBarContributor {

	private InsertRecordAction insertRecordAction;

	private DeleteRecordAction deleteRecordAction;

	private SelectAllRecordAction selectAllAction;

	private CopyRecordDataAction copyRecordDataAction;

	private PasteRecordDataAction pasteRecordAction;

	private CreateCSVForTableAction createCSVAction;

	private CreateCSVForTableWithConditionAction createCSV2Action;

	private CopyInsertStatementAction copyStringInsertStatementAction;

	private AddColumnAction addColumnAction;

	private DuplicateColumnAction duplicateColumnAction;

	private EditColumnAction editColumnAction;

	private DropColumnAction deleteColumnAction;

	private AddPrimaryKeyAction addPrimaryKeyAction;

	private AddForeginKeyAction addForeginKeyAction;

	private AddUniqueKeyAction addUniqueKeyAction;

	private AddCheckAction addCheckAction;

	private AddIndexAction addIndexAction;

	private DropIndexAction dropIndexAction;

	private DropConstraintAction dropConstraintAction;
	
	private OpenTriggerAction openTriggerAction;

	protected CopyRecordTrimDataAction copyTrimAction;


	private IDBConfig config;

	public TableViewerContributor() {
		insertRecordAction = new InsertRecordAction();
		insertRecordAction.setEnabled(false);

		deleteRecordAction = new DeleteRecordAction();
		selectAllAction = new SelectAllRecordAction();
		copyRecordDataAction = new CopyRecordDataAction();
		copyStringInsertStatementAction = new CopyInsertStatementAction();
		pasteRecordAction = new PasteRecordDataAction();
		createCSVAction = new CreateCSVForTableAction();
		createCSV2Action = new CreateCSVForTableWithConditionAction();

		addColumnAction = new AddColumnAction();
		editColumnAction = new EditColumnAction();
		duplicateColumnAction = new DuplicateColumnAction();
		deleteColumnAction = new DropColumnAction();

		addPrimaryKeyAction = new AddPrimaryKeyAction();
		addForeginKeyAction = new AddForeginKeyAction();
		addUniqueKeyAction = new AddUniqueKeyAction();
		addCheckAction = new AddCheckAction();
		addIndexAction = new AddIndexAction();
		dropIndexAction = new DropIndexAction();
		dropConstraintAction = new DropConstraintAction();
		copyTrimAction = new CopyRecordTrimDataAction();
		openTriggerAction = new OpenTriggerAction();
	}

	public void init(IActionBars bars, IWorkbenchPage page) {
		super.init(bars, page);
	}

	public void fillContextMenu(IMenuManager manager) {
		reflesh();
		manager.add(insertRecordAction);
		manager.add(new Separator());
		manager.add(copyRecordDataAction);
		manager.add(copyTrimAction);

		manager.add(pasteRecordAction);


		manager.add(new Separator());
		manager.add(deleteRecordAction);
		manager.add(selectAllAction);
		manager.add(new Separator());

		manager.add(copyStringInsertStatementAction);
		manager.add(createCSVAction);

		manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
	}

	public void fillContextMenuForDDL(IMenuManager manager) {
		reflesh();
		manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
	}

	public void fillContextMenuForDefine(IMenuManager manager, ISelection selection) {
		Object obj = (Object) ((StructuredSelection) selection).getFirstElement();

		manager.add(addColumnAction);
		manager.add(editColumnAction);
		manager.add(deleteColumnAction);
		manager.add(duplicateColumnAction);

		switch (DBType.getType(config)) {
		case DBType.DB_TYPE_ORACLE:
		case DBType.DB_TYPE_MYSQL:
		case DBType.DB_TYPE_POSTGRESQL:
		case DBType.DB_TYPE_DERBY:
		case DBType.DB_TYPE_H2:
		case DBType.DB_TYPE_HSQLDB:
			addColumnAction.setEnabled(true);
			editColumnAction.setEnabled(true);
			deleteColumnAction.setEnabled(true);
			break;

		case DBType.DB_TYPE_DB2:
			addColumnAction.setEnabled(true);
			editColumnAction.setEnabled(false);
			deleteColumnAction.setEnabled(false);
			break;

		default:
			addColumnAction.setEnabled(false);
			editColumnAction.setEnabled(false);
			deleteColumnAction.setEnabled(false);
			duplicateColumnAction.setEnabled(false);
			break;
		}

		manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
	}

	public void fillContextMenuForConstraints(IMenuManager manager, ISelection selection) {
		Object obj = (Object) ((StructuredSelection) selection).getFirstElement();

		switch (DBType.getType(config)) {
		case DBType.DB_TYPE_ORACLE:

			if (obj instanceof ConstraintRoot) {
				dropConstraintAction.setEnabled(false);
				manager.add(addPrimaryKeyAction);
				manager.add(addForeginKeyAction);
				manager.add(addUniqueKeyAction);
				manager.add(addCheckAction);
				manager.add(dropConstraintAction);

			} else if (obj instanceof Constraint) {
				dropConstraintAction.setEnabled(true);
				manager.add(addPrimaryKeyAction);
				manager.add(addForeginKeyAction);
				manager.add(addUniqueKeyAction);
				manager.add(addCheckAction);
				manager.add(dropConstraintAction);

			} else if (obj instanceof IndexRoot) {
				dropIndexAction.setEnabled(false);
				manager.add(addIndexAction);
				manager.add(dropIndexAction);

			} else if (obj instanceof Index) {
				dropIndexAction.setEnabled(true);
				manager.add(addIndexAction);
				manager.add(dropIndexAction);
			
			} else if (obj instanceof Trigger) {
				manager.add(openTriggerAction);
			}
			
			break;

		case DBType.DB_TYPE_MYSQL:
		case DBType.DB_TYPE_POSTGRESQL:
		case DBType.DB_TYPE_H2:
		case DBType.DB_TYPE_HSQLDB:
		case DBType.DB_TYPE_DERBY:
		case DBType.DB_TYPE_DB2:

			if (obj instanceof ConstraintRoot) {
				dropConstraintAction.setEnabled(false);
				manager.add(addPrimaryKeyAction);
				manager.add(addForeginKeyAction);
				// manager.add(addUniqueKeyAction);
				// manager.add(addCheckAction);
				manager.add(dropConstraintAction);

			} else if (obj instanceof Constraint) {
				dropConstraintAction.setEnabled(true);
				manager.add(addPrimaryKeyAction);
				manager.add(addForeginKeyAction);
				// manager.add(addUniqueKeyAction);
				// manager.add(addCheckAction);
				manager.add(dropConstraintAction);

			} else if (obj instanceof IndexRoot) {
				dropIndexAction.setEnabled(false);
				manager.add(addIndexAction);
				manager.add(dropIndexAction);

			} else if (obj instanceof Index) {
				dropIndexAction.setEnabled(true);
				manager.add(addIndexAction);
				manager.add(dropIndexAction);
			}
			break;

		default:
			if (obj instanceof ConstraintRoot) {
				manager.add(addPrimaryKeyAction);
				manager.add(addForeginKeyAction);
				manager.add(dropConstraintAction);

			} else if (obj instanceof Constraint) {
				manager.add(addPrimaryKeyAction);
				manager.add(addForeginKeyAction);
				manager.add(dropConstraintAction);

			} else if (obj instanceof IndexRoot) {
				manager.add(addIndexAction);
				manager.add(dropIndexAction);

			} else if (obj instanceof Index) {
				manager.add(addIndexAction);
				manager.add(dropIndexAction);
			}

			addPrimaryKeyAction.setEnabled(false);
			addForeginKeyAction.setEnabled(false);
			addUniqueKeyAction.setEnabled(false);
			addCheckAction.setEnabled(false);
			dropConstraintAction.setEnabled(false);

			addIndexAction.setEnabled(false);
			dropIndexAction.setEnabled(false);

			break;
		}

		manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
	}


	public void contributeToToolBar(IToolBarManager manager) {
		super.contributeToToolBar(manager);
		// manager.add(new Separator());
		// manager.add(new InsertRecordAction());
		// manager.add(new DeleteRecordAction());
	}

	public void contributeToMenu(IMenuManager menu) {

	// super.contributeToMenu(menu);
	// IMenuManager editMenu =
	// menu.findMenuUsingPath(IWorkbenchActionConstants.M_EDIT);
	// if(editMenu!=null){
	// deleteAction.refresh();
	//
	// editMenu.add(new Separator());
	// editMenu.add(insertAction);
	// editMenu.add(deleteAction);
	//
	// }

	}

	public void setActivePage(IEditorPart target) {
		makeActions(target);
	}

	public void setActiveEditor(IEditorPart target) {
		super.setActiveEditor(target);
		makeActions(target);
	}

	private void makeActions(IEditorPart target) {
		if (target instanceof TableViewEditorFor31) {
			TableViewEditorFor31 editor = (TableViewEditorFor31) target;
			this.config = editor.getDBConfig();

			selectAllAction.setActiveEditor(editor);
			insertRecordAction.setActiveEditor(editor);
			deleteRecordAction.setActiveEditor(editor);
			copyRecordDataAction.setActiveEditor(editor);
			pasteRecordAction.setActiveEditor(editor);
			copyStringInsertStatementAction.setActiveEditor(editor);
			createCSVAction.setActiveEditor(editor);
			createCSV2Action.setActiveEditor(editor);
			addColumnAction.setActiveEditor(editor);

			editColumnAction.setActiveEditor(editor);
			duplicateColumnAction.setActiveEditor(editor);
			deleteColumnAction.setActiveEditor(editor);
			addPrimaryKeyAction.setActiveEditor(editor);
			addForeginKeyAction.setActiveEditor(editor);
			addUniqueKeyAction.setActiveEditor(editor);
			addCheckAction.setActiveEditor(editor);
			addIndexAction.setActiveEditor(editor);
			dropIndexAction.setActiveEditor(editor);
			dropConstraintAction.setActiveEditor(editor);

			copyTrimAction.setActiveEditor(editor);
			
			openTriggerAction.setActiveEditor(editor);

		}
	}

	void reflesh() {
		deleteRecordAction.refresh();
		copyRecordDataAction.refresh();
		pasteRecordAction.refresh();
		copyStringInsertStatementAction.refresh();

		copyTrimAction.refresh();
	}

	public void dispose() {
		selectAllAction.setActiveEditor(null);
		insertRecordAction.setActiveEditor(null);
		deleteRecordAction.setActiveEditor(null);
		copyRecordDataAction.setActiveEditor(null);
		pasteRecordAction.setActiveEditor(null);
		copyStringInsertStatementAction.setActiveEditor(null);
		createCSVAction.setActiveEditor(null);
		createCSV2Action.setActiveEditor(null);
		addColumnAction.setActiveEditor(null);
		editColumnAction.setActiveEditor(null);
		duplicateColumnAction.setActiveEditor(null);
		deleteColumnAction.setActiveEditor(null);
		addPrimaryKeyAction.setActiveEditor(null);
		addForeginKeyAction.setActiveEditor(null);
		addUniqueKeyAction.setActiveEditor(null);
		addCheckAction.setActiveEditor(null);
		addIndexAction.setActiveEditor(null);
		dropIndexAction.setActiveEditor(null);
		dropConstraintAction.setActiveEditor(null);
		copyTrimAction.setActiveEditor(null);
		openTriggerAction.setActiveEditor(null);
		super.dispose();

	}

	public void setEnabled(boolean enabled) {
		insertRecordAction.setEnabled(enabled);
	}

	public InsertRecordAction getInsertRecordAction() {
		return insertRecordAction;
	}

	public DeleteRecordAction getDeleteRecordAction() {
		return deleteRecordAction;
	}

}
