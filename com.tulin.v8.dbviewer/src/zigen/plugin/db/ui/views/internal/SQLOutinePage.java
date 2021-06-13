/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.ui.views.internal;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import kry.sql.format.SqlFormatRule;
import kry.sql.tokenizer.SqlTokenizer;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IStatusLineManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.TextSelection;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.part.IPageSite;
import org.eclipse.ui.views.contentoutline.ContentOutlinePage;

import zigen.plugin.db.DbPlugin;
import zigen.plugin.db.DbPluginConstant;
import zigen.plugin.db.ImageCacher;
import zigen.plugin.db.core.StringUtil;
import zigen.plugin.db.core.TimeWatcher;
import zigen.plugin.db.parser.util.CurrentSql;
import zigen.plugin.db.preference.SQLEditorPreferencePage;
import zigen.plugin.db.ui.editors.sql.SqlEditor2;
import zigen.plugin.db.ui.jobs.AbstractJob;
import zigen.plugin.db.ui.jobs.UpdateSQLFoldingJob;
import zigen.sql.parser.ASTVisitor2;
import zigen.sql.parser.ASTVisitorToString;
import zigen.sql.parser.INode;
import zigen.sql.parser.ISqlParser;
import zigen.sql.parser.Node;
import zigen.sql.parser.SqlParser;
import zigen.sql.parser.ast.ASTAlias;
import zigen.sql.parser.ast.ASTColumn;
import zigen.sql.parser.ast.ASTComma;
import zigen.sql.parser.ast.ASTFunction;
import zigen.sql.parser.ast.ASTInnerAlias;
import zigen.sql.parser.ast.ASTParentheses;
import zigen.sql.parser.ast.ASTRoot;
import zigen.sql.parser.ast.ASTSelectStatement;
import zigen.sql.parser.ast.ASTTable;
import zigen.sql.parser.ast.ASTValue;
import zigen.sql.parser.exception.ParserException;

public class SQLOutinePage extends ContentOutlinePage implements ISelectionListener {

	SqlEditor2 editor;

	SQLSourceViewer fSQLSourceViewer;

	SqlInput fSqlInput = new SqlInput();

	IDoubleClickListener doubleClickListener;

	SqlParser fSqlParser;

	ASTVisitor2 visitor;

	IDocument fDocument;

	TreeViewer fTreeViewer;

	IPreferenceStore ps;

	CurrentSql currentSql;

	boolean isASTMode = false;

	private Menu fMenu;

	public SQLOutinePage(SqlEditor2 editor) {
		this.editor = editor;
		this.fSQLSourceViewer = editor.getSqlViewer();
		this.fDocument = editor.getSqlViewer().getDocument();
		this.ps = DbPlugin.getDefault().getPreferenceStore();
	}

	public void createControl(Composite parent) {
		super.createControl(parent);
		fTreeViewer = getTreeViewer();

		fTreeViewer.setContentProvider(new TreeContentProvider());
		fTreeViewer.setLabelProvider(new TreeLabelProvider());
		doubleClickListener = new DoubleClickListener();
		fTreeViewer.addDoubleClickListener(doubleClickListener);
		fTreeViewer.setUseHashlookup(true);
		fTreeViewer.setInput(fSqlInput);
		fTreeViewer.expandToLevel(2);
		update();


		MenuManager manager = new MenuManager("SQLOutlinePage", "SQLOutlinePage");
		manager.setRemoveAllWhenShown(true);
		manager.addMenuListener(new IMenuListener() {

			public void menuAboutToShow(IMenuManager m) {
				contextMenuAboutToShow(m);
			}
		});
		fMenu = manager.createContextMenu(fTreeViewer.getControl());
		fTreeViewer.getControl().setMenu(fMenu);

		IPageSite site = getSite();
		site.registerContextMenu("DBViewer.outline", manager, fTreeViewer); //$NON-NLS-1$


		getSite().getPage().addSelectionListener(this);

	}

	protected void contextMenuAboutToShow(IMenuManager menu) {
		IStructuredSelection selection = (IStructuredSelection) getSelection();
		menu.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
	}

	public void update() {
		if (fSQLSourceViewer != null) {
			int offset = fSQLSourceViewer.getTextWidget().getCaretOffset();
			String _temp = (currentSql != null) ? currentSql.getSql() : null;
			String demiliter = ps.getString(SQLEditorPreferencePage.P_SQL_DEMILITER);
			currentSql = new CurrentSql(fDocument, offset, demiliter);

			if (_temp == null || !_temp.equals(currentSql.getSql())) {
				UpdateOutlineJob job = new UpdateOutlineJob(currentSql);
				job.setPriority(UpdateSQLFoldingJob.LONG);
				job.setUser(false);
				job.schedule();
			}
		}
	}

	private void revealRange(int offset, int length) {
		offset += currentSql.getBegin();
		fSQLSourceViewer.revealRange(offset, length);
		fSQLSourceViewer.setSelectedRange(offset, length);

	}

	public void makeContributions(IMenuManager menuManager, IToolBarManager toolBarManager, IStatusLineManager statusLineManager) {
		toolBarManager.add(new ExpandAllAction());
		toolBarManager.add(new CollapseAllAction());

	}


	public void selectionChanged(IWorkbenchPart part, ISelection selection) {
		if (selection instanceof TextSelection) {
			TextSelection ts = (TextSelection) selection;

			if (currentSql.getBegin() <= ts.getOffset() && ts.getOffset() <= currentSql.getEnd()) {
				if (ts.getText().length() > 0) {
					if (visitor != null) {
						try {
							int searchOffset = ts.getOffset() - currentSql.getBegin();
							INode cNode = visitor.findNodeByOffset(searchOffset, ts.getLength());
							if (cNode != null) {
								StructuredSelection ss = new StructuredSelection(cNode);
								fTreeViewer.setSelection(ss, true);
								fTreeViewer.expandToLevel(ss, 1);
							}

						} catch (RuntimeException e) {
							e.printStackTrace();
						}
					}
				}
			} else {
				update();
			}
		}
	}


	public INode getEndNode(INode node) {
		INode n = node.getLastChild();
		if (n == null) {
			return node;
		} else {
			return getEndNode(n);
		}
	}

	class ExpandAllAction extends Action {

		public ExpandAllAction() {
			super("Expand All", IAction.AS_PUSH_BUTTON);
			setImageDescriptor(DbPlugin.getDefault().getImageDescriptor(DbPlugin.IMG_CODE_EXPAND_ALL));
		}

		public void run() {
			fTreeViewer.expandAll();
		}

	}

	class CollapseAllAction extends Action {

		public CollapseAllAction() {
			super("Collapse All", IAction.AS_PUSH_BUTTON);
			setImageDescriptor(DbPlugin.getDefault().getImageDescriptor(DbPlugin.IMG_CODE_COLLAPSE_ALL));
		}

		public void run() {
			fTreeViewer.collapseAll();
		}

	}

	class ChangeModeAction extends Action {

		public ChangeModeAction() {
			super("ASTMode", IAction.AS_CHECK_BOX);
		}

		public void run() {
			isASTMode = isChecked();
			fTreeViewer.refresh();
		}

	}

	class SqlInput {

		INode documentElement;
	}

	class DoubleClickListener implements IDoubleClickListener {

		int offset = -1;

		int length = -1;

		private void calc(ASTColumn node) {
			offset = node.getOffset();
			length = node.getLength();
			if (node.hasAlias()) {
				length = node.getAliasOffset() + node.getAliasLength() - offset;
			}
		}

		private void calc(ASTParentheses node) {
			offset = node.getOffset();
			length = node.getEndOffset() + 1 - offset;
			if (node.hasAlias()) {
				length = node.getAliasOffset() + node.getAliasLength() - offset;
			}

		}

		private void calc(ASTSelectStatement node) {
			INode last = getEndNode(node);
			offset = node.getOffset();
			length = last.getOffset() + last.getLength() - offset;

			if (last instanceof ASTAlias) {
				ASTAlias as = (ASTAlias) last;
				if (as.hasAlias()) {
					length = as.getAliasOffset() + as.getAliasLength() - offset;
				}
			}

		}

		private void calc(ASTFunction node) {
			if (node.getChildrenSize() == 1 && node.getChild(0) instanceof ASTParentheses) {
				ASTParentheses p = (ASTParentheses) node.getChild(0);
				calc((ASTParentheses) node.getChild(0));
				offset = node.getOffset();
				length += node.getLength();

				if (node.hasAlias()) {
					length = node.getAliasOffset() + node.getAliasLength() - offset;
				}

			}
		}

		public void doubleClick(DoubleClickEvent event) {
			try {
				IStructuredSelection sel = (IStructuredSelection) event.getSelection();
				Object element = sel.getFirstElement();

				if (element instanceof ASTColumn) {
					calc((ASTColumn) element);

				} else if (element instanceof ASTParentheses) {
					calc(((ASTParentheses) element));

				} else if (element instanceof ASTSelectStatement) {
					calc((ASTSelectStatement) element);

				} else if (element instanceof ASTFunction) {
					calc((ASTFunction) element);

				} else if (element instanceof Node) {
					Node node = ((Node) element);
					offset = node.getOffset();
					length = node.getLength();
				}

				if (offset >= 0) {
					revealRange(offset, length);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	class TreeLabelProvider extends LabelProvider {

		ImageCacher ic = ImageCacher.getInstance();

		public String getText(Object obj) {
			if (obj instanceof Node) {
				if (isASTMode) {
					return ((Node) obj).toString();
				} else {

					if (obj instanceof ASTFunction) {
						ASTVisitorToString v = new ASTVisitorToString();
						ASTFunction f = (ASTFunction) obj;
						f.accept(v, null);
						return v.toString();

					} else {
						return ((Node) obj).getName();

					}
				}
			}
			return obj.toString();
		}

		public Image getImage(Object obj) {
			if (obj instanceof ASTTable) {
				return ic.getImage(DbPlugin.IMG_CODE_TABLE);
			} else if (obj instanceof ASTValue) {
				return ic.getImage(DbPlugin.IMG_CODE_SQL);
			} else if (obj instanceof ASTColumn) {
				return ic.getImage(DbPlugin.IMG_CODE_COLUMN);
			} else {
				return ic.getImage(DbPlugin.IMG_CODE_SQL);
			}
		}

	}

	class TreeContentProvider implements ITreeContentProvider {

		public void inputChanged(Viewer v, Object oldInput, Object newInput) {}

		public void dispose() {}

		public Object[] getElements(Object inputElement) {
			return getChildren(fSqlInput.documentElement);
		}

		public Object getParent(Object element) {
			if (element instanceof Node) {
				return ((Node) element).getParent();
			}
			return null;
		}

		public Object[] getChildren(Object parentElement) {
			List result = new ArrayList();
			if (parentElement != null && parentElement instanceof Node) {
				Node node = (Node) parentElement;
				List list = node.getChildren();
				if (list != null) {
					for (Iterator iter = list.iterator(); iter.hasNext();) {
						Object obj = iter.next();
						if (!(obj instanceof ASTComma) && !(obj instanceof ASTInnerAlias)) {
							result.add(obj);
						}
					}
				}
			}
			return result.toArray(new Node[0]);
		}

		public boolean hasChildren(Object element) {
			if (element instanceof Node)
				return ((Node) element).getChildrenSize() > 0;
			return false;
		}
	}

	class ParseCancel extends ParserException {

		private static final long serialVersionUID = 1L;

		public ParseCancel(String message) {
			super(message, null, 0, 0);
		}
	}

	class SqlTokenizerWithProgressMonitor extends SqlTokenizer {

		IProgressMonitor monitor;

		public SqlTokenizerWithProgressMonitor(IProgressMonitor monitor, String sql, SqlFormatRule rule){
			this.monitor = monitor;
			super.init(sql, rule);
		}

		public boolean isCanceled(){
			return monitor.isCanceled();
		}

	}
	class SqlParserWithProgressMonitor extends SqlParser implements ISqlParser {
		IProgressMonitor monitor;

		public SqlParserWithProgressMonitor(IProgressMonitor monitor, String sql, SqlFormatRule rule) {
			super(sql, new SqlTokenizerWithProgressMonitor(monitor, sql, rule));
			this.monitor = monitor;
		}
		public boolean isCanceled(){
			return monitor.isCanceled();
		}

		protected int nextToken() {
			if (monitor.isCanceled()) {
				throw new ParseCancel("SQL Parse cancel");
			} else {
				monitor.worked(1);
				return super.nextToken();
			}
		}
	}
	class UpdateOutlineJob extends AbstractJob {

		CurrentSql currentSql;

		public UpdateOutlineJob(CurrentSql currentSql) {
			super("Updating SQL Outline");
			this.currentSql = currentSql;
		}

		protected IStatus run(IProgressMonitor monitor) {
			zigen.sql.parser.ISqlParser parser = null;

			try {
				monitor.beginTask("Updating SQL Outline...", IProgressMonitor.UNKNOWN);

				if (monitor.isCanceled()) {
					return Status.CANCEL_STATUS;
				}
				monitor.subTask("Parsing SQL");

				TimeWatcher tw = new TimeWatcher();
				tw.start();
				String sql = currentSql.getSql();
				parser = new SqlParserWithProgressMonitor(monitor, sql, DbPlugin.getSqlFormatRult());
				tw.stop();
				System.out.println("SQLTokenizer " + tw.getTotalTime());
				tw.start();
				final INode node = new ASTRoot();

				visitor = new ASTVisitor2();
				parser.parse(node);
				tw.stop();
				tw.start();
				node.accept(visitor, null);
				tw.stop();
				monitor.subTask("Complete Parsed SQL " + currentSql);
				showResults(new Runnable() {

					public void run() {
						try {
							getTreeViewer().removeDoubleClickListener(doubleClickListener);
							fSqlInput.documentElement = node;
							fTreeViewer.refresh();

							int searchOffset = StringUtil.endWordPosition(currentSql.getOffsetSql());
							INode cNode = visitor.findNodeByOffset(searchOffset);
							// fTreeViewer.expandToLevel(cNode, 1);
							fTreeViewer.expandToLevel(cNode, 1);

							getTreeViewer().addDoubleClickListener(doubleClickListener);

						} catch (org.eclipse.swt.SWTException e) {
							;
						} catch (Exception e) {
							DbPlugin.log(e);
						}

					}
				});

				monitor.done();

			} catch (ParseCancel e) {
				return Status.CANCEL_STATUS;

			} catch (zigen.sql.parser.exception.ParserException e) {
				DbPlugin.log("SQL Parser Error." + DbPluginConstant.LINE_SEP + currentSql.getSql(), e);


			} catch (Exception e) {
				DbPlugin.log("UpdateOutlineJob Error." + DbPluginConstant.LINE_SEP + currentSql.getSql(), e);

			} catch (java.lang.StackOverflowError e) {
				DbPlugin.log("UpdateOutlineJob Error." + DbPluginConstant.LINE_SEP + currentSql.getSql(), e);


			} finally {
				if (parser != null)
					parser = null;
			}

			return Status.OK_STATUS;
		}
	}

	public CurrentSql getCurrentSql() {
		return currentSql;
	}

	public SqlEditor2 getEditor() {
		return editor;
	}


}
