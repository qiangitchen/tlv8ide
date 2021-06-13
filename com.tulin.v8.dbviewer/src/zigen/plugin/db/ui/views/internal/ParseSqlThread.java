package zigen.plugin.db.ui.views.internal;

import org.eclipse.jface.text.IDocument;

import zigen.plugin.db.DbPlugin;
import zigen.plugin.db.parser.util.CurrentSql;
import zigen.plugin.db.preference.SQLEditorPreferencePage;
import zigen.sql.parser.ASTVisitor2;
import zigen.sql.parser.INode;
import zigen.sql.parser.ISqlParser;
import zigen.sql.parser.SqlParser;
import zigen.sql.parser.ast.ASTRoot;
import zigen.sql.parser.exception.ParserException;

public class ParseSqlThread implements Runnable {

	INode fNode;

	ASTVisitor2 fVisitor;

	boolean isComplete;

	CurrentSql fCurrentSql;

	protected IDocument fDocument;

	protected int offset;


	public ParseSqlThread(IDocument document, int offset) {
		this.offset = offset;
		this.fDocument = document;
		fVisitor = new ASTVisitor2();
	}

	public void run() {
		try {
			fCurrentSql = createCurrentSql();
			ISqlParser parser = new SqlParser(fCurrentSql.getSql(), DbPlugin.getSqlFormatRult());
			fNode = new ASTRoot();
			parser.parse(fNode);
			fNode.accept(fVisitor, null);
			isComplete = true;
		} catch (ParserException e) {
			e.printStackTrace();
		} catch (java.lang.StackOverflowError e) {
			e.printStackTrace();
		}

	}

	protected CurrentSql createCurrentSql() {
		String demiliter = DbPlugin.getDefault().getPreferenceStore().getString(SQLEditorPreferencePage.P_SQL_DEMILITER);
		return new CurrentSql(fDocument, offset, demiliter);

	}

	public INode getNode() {
		return fNode;
	}

	public ASTVisitor2 getVisitor() {
		return fVisitor;
	}

	public String getSql() {
		return (fCurrentSql == null) ? null : fCurrentSql.getSql();
	}

	public int getBeginOffset() {
		return (fCurrentSql == null) ? 0 : fCurrentSql.getBegin();
	}

	public boolean isComplete() {
		return isComplete;
	}

}
