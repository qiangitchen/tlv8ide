package zigen.plugin.db.core.rule;

import junit.framework.TestCase;
import kry.sql.format.ISqlFormat;
import kry.sql.tokenizer.SqlScanner;
import zigen.plugin.db.core.DBConfig;
import zigen.plugin.db.core.DBType;
import zigen.plugin.db.core.IDBConfig;
import zigen.plugin.db.core.TableColumn;
import zigen.plugin.db.ui.internal.Column;
import zigen.plugin.db.ui.internal.Constraint;
import zigen.plugin.db.ui.internal.DataBase;
import zigen.plugin.db.ui.internal.Folder;
import zigen.plugin.db.ui.internal.ITable;
import zigen.plugin.db.ui.internal.Schema;
import zigen.plugin.db.ui.internal.Table;

public class HSQLSQLCreatorFactoryTest extends OracleSQLCreatorFactoryTest {
	
	protected void setUp() throws Exception {
		driverName = "hsqldb";
		table = null;
		f = null;
		sql = null;
	}

	public void testCreateCreateIndexDDL() {
		table = createTable("SCOTT", "EMP");
		table.addChild(createColumn("EMPNO", "NUMBER", null));
		f = AbstractSQLCreatorFactory.getFactory(table.getDbConfig(), table);
		f.setVisibleSchemaName(true);
		
		sql = f.createCreateIndexDDL("MYINDEX",new Column[]{table.getColumns()[0]}, ISQLCreatorFactory.TYPE_UNIQUE_INDEX);
		assertEquals("CREATE UNIQUE INDEX SCOTT.MYINDEX ON SCOTT.EMP(EMPNO)", sql);

		table = createTable("SCOTT-1", "EMP-1");
		table.addChild(createColumn("EMPNO-1", "NUMBER", null));
		f = AbstractSQLCreatorFactory.getFactory(table.getDbConfig(), table);
		f.setVisibleSchemaName(true);
		
		sql = f.createCreateIndexDDL("MYINDEX-1",new Column[]{table.getColumns()[0]}, ISQLCreatorFactory.TYPE_UNIQUE_INDEX);
		assertEquals("CREATE UNIQUE INDEX \"SCOTT-1\".\"MYINDEX-1\" ON \"SCOTT-1\".\"EMP-1\"(\"EMPNO-1\")", sql);

		
	}

	public void testCreateDropIndexDDL() {
		table = createTable("SCOTT", "EMP");
		table.addChild(createColumn("EMPNO", "NUMBER", null));
		f = AbstractSQLCreatorFactory.getFactory(table.getDbConfig(), table);
		f.setVisibleSchemaName(true);
		
		sql = f.createDropIndexDDL("MYINDEX");
		assertEquals("DROP INDEX SCOTT.MYINDEX", sql);

		table = createTable("SCOTT-1", "EMP-1");
		table.addChild(createColumn("EMPNO-1", "NUMBER", null));
		f = AbstractSQLCreatorFactory.getFactory(table.getDbConfig(), table);
		f.setVisibleSchemaName(true);
		
		sql = f.createDropIndexDDL("MYINDEX-1");
		assertEquals("DROP INDEX \"SCOTT-1\".\"MYINDEX-1\"",sql);

		
	}

	public void testCreateCreateConstraintCheckDDL() {
		table = createTable("SCOTT", "EMP");
		table.addChild(createColumn("EMPNO", "NUMBER", null));
		f = AbstractSQLCreatorFactory.getFactory(table.getDbConfig(), table);
		f.setVisibleSchemaName(true);
		
		sql = f.createCreateConstraintCheckDDL("MYINDEX","EMPNO > 0");
		assertEquals(null, sql);

		table = createTable("SCOTT-1", "EMP-1");
		table.addChild(createColumn("EMPNO-1", "NUMBER", null));
		f = AbstractSQLCreatorFactory.getFactory(table.getDbConfig(), table);
		f.setVisibleSchemaName(true);
		
		sql = f.createCreateConstraintCheckDDL("MYINDEX-1","EMPNO > 0");
		assertEquals(null, sql);

	}

	public void testCreateCreateConstraintFKDDL() {
		table = createTable("SCOTT", "EMP");
		table.addChild(createColumn("EMPNO", "NUMBER", null));
		table.addChild(createColumn("DEPTNO", "NUMBER", null));
		f = AbstractSQLCreatorFactory.getFactory(table.getDbConfig(), table);
		f.setVisibleSchemaName(true);
		
		Table refTable = createTable("SCOTT", "DEPT");
		refTable.addChild(createColumn("DEPTNO", "NUMBER", null));
		
		sql = f.createCreateConstraintFKDDL("MYINDEX",new Column[]{table.getColumns()[1]}, refTable, new Column[]{refTable.getColumns()[0]}, true);
		assertEquals("ALTER TABLE SCOTT.EMP ADD CONSTRAINT MYINDEX FOREIGN KEY(DEPTNO) REFERENCES SCOTT.DEPT(DEPTNO) ON DELETE CASCADE", sql);

		table = createTable("SCOTT-1", "EMP-1");
		table.addChild(createColumn("EMPNO-1", "NUMBER", null));
		table.addChild(createColumn("DEPTNO-1", "NUMBER", null));
		f = AbstractSQLCreatorFactory.getFactory(table.getDbConfig(), table);
		f.setVisibleSchemaName(true);
		
		refTable = createTable("SCOTT-2", "DEPT-2");
		refTable.addChild(createColumn("DEPTNO-2", "NUMBER", null));
		
		sql = f.createCreateConstraintFKDDL("MYINDEX-1",new Column[]{table.getColumns()[1]}, refTable, new Column[]{refTable.getColumns()[0]}, true);
		assertEquals("ALTER TABLE \"SCOTT-1\".\"EMP-1\" ADD CONSTRAINT \"MYINDEX-1\" FOREIGN KEY(\"DEPTNO-1\") REFERENCES \"SCOTT-2\".\"DEPT-2\"(\"DEPTNO-2\") ON DELETE CASCADE", sql);

	}

	public void testCreateCreateConstraintPKDDL() {
		table = createTable("SCOTT", "EMP");
		table.addChild(createColumn("EMPNO", "NUMBER", null));
		f = AbstractSQLCreatorFactory.getFactory(table.getDbConfig(), table);
		f.setVisibleSchemaName(true);
		
		sql = f.createCreateConstraintPKDDL("MYINDEX",new Column[]{table.getColumns()[0]});
		assertEquals("ALTER TABLE SCOTT.EMP ADD CONSTRAINT MYINDEX PRIMARY KEY(EMPNO)", sql);

		table = createTable("SCOTT-1", "EMP-1");
		table.addChild(createColumn("EMPNO-1", "NUMBER", null));
		f = AbstractSQLCreatorFactory.getFactory(table.getDbConfig(), table);
		f.setVisibleSchemaName(true);
		
		sql = f.createCreateConstraintPKDDL("MYINDEX-1",new Column[]{table.getColumns()[0]});
		assertEquals("ALTER TABLE \"SCOTT-1\".\"EMP-1\" ADD CONSTRAINT \"MYINDEX-1\" PRIMARY KEY(\"EMPNO-1\")", sql);

	}

	public void testCreateCreateConstraintUKDDL() {
		table = createTable("SCOTT", "EMP");
		table.addChild(createColumn("EMPNO", "NUMBER", null));
		f = AbstractSQLCreatorFactory.getFactory(table.getDbConfig(), table);
		f.setVisibleSchemaName(true);
		
		sql = f.createCreateConstraintUKDDL("MYINDEX",new Column[]{table.getColumns()[0]});
		assertEquals("ALTER TABLE SCOTT.EMP ADD CONSTRAINT MYINDEX UNIQUE (EMPNO)", sql);

		table = createTable("SCOTT-1", "EMP-1");
		table.addChild(createColumn("EMPNO-1", "NUMBER", null));
		f = AbstractSQLCreatorFactory.getFactory(table.getDbConfig(), table);
		f.setVisibleSchemaName(true);
		
		sql = f.createCreateConstraintUKDDL("MYINDEX-1",new Column[]{table.getColumns()[0]});
		assertEquals("ALTER TABLE \"SCOTT-1\".\"EMP-1\" ADD CONSTRAINT \"MYINDEX-1\" UNIQUE (\"EMPNO-1\")", sql);

	}

	public void testCreateDropConstraintDDL() {
		table = createTable("SCOTT", "EMP");
		f = AbstractSQLCreatorFactory.getFactory(table.getDbConfig(), table);
		f.setVisibleSchemaName(true);
		sql = f.createDropConstraintDDL("MYINDEX",Constraint.PRIMARY_KEY);
		assertEquals("ALTER TABLE SCOTT.EMP DROP CONSTRAINT MYINDEX", sql);

		table = createTable("SCOTT-1", "EMP-1");
		f = AbstractSQLCreatorFactory.getFactory(table.getDbConfig(), table);
		f.setVisibleSchemaName(true);
		sql = f.createDropConstraintDDL("MYINDEX-1",Constraint.PRIMARY_KEY);
		assertEquals("ALTER TABLE \"SCOTT-1\".\"EMP-1\" DROP CONSTRAINT \"MYINDEX-1\"", sql);

	}

	public void testCreateCommentOnTableDDL() {
		table = createTable("SCOTT", "EMP");
		f = AbstractSQLCreatorFactory.getFactory(table.getDbConfig(), table);
		f.setVisibleSchemaName(true);
		sql = f.createCommentOnTableDDL("コメントです");
		assertEquals(null, sql);

		table = createTable("SCOTT-1", "EMP-1");
		f = AbstractSQLCreatorFactory.getFactory(table.getDbConfig(), table);
		f.setVisibleSchemaName(true);
		sql = f.createCommentOnTableDDL("コ'メント\"です");
		assertEquals(null, sql);

	}

	public void testCreateCommentOnColumnDDL() {
		table = createTable("SCOTT", "EMP");
		table.addChild(createColumn("EMPNO", "NUMBER", null,"コメント"));
		f = AbstractSQLCreatorFactory.getFactory(table.getDbConfig(), table);
		f.setVisibleSchemaName(true);
		sql = f.createCommentOnColumnDDL(table.getColumns()[0]);
		assertEquals(null, sql);

		table = createTable("SCOTT-1", "EMP-1");
		table.addChild(createColumn("EMPNO-1", "NUMBER", null,"コ'メン\"ト"));
		f = AbstractSQLCreatorFactory.getFactory(table.getDbConfig(), table);
		f.setVisibleSchemaName(true);		
		sql = f.createCommentOnColumnDDL(table.getColumns()[0]);
		assertEquals(null, sql);

	}

	public void testCreateRenameTableDDL() {
		table = createTable("SCOTT", "EMP");
		f = AbstractSQLCreatorFactory.getFactory(table.getDbConfig(), table);
		f.setVisibleSchemaName(true);
		sql = f.createRenameTableDDL("HOGE");
		assertEquals("ALTER TABLE SCOTT.EMP RENAME TO HOGE", sql);
		
		table = createTable("SCOTT-1", "EMP-1");
		f = AbstractSQLCreatorFactory.getFactory(table.getDbConfig(), table);
		f.setVisibleSchemaName(true);
		sql = f.createRenameTableDDL("H'OGE-1");
		assertEquals("ALTER TABLE \"SCOTT-1\".\"EMP-1\" RENAME TO \"H'OGE-1\"", sql);
	}

	public void testCreateRenameColumnDDL() {
		table = createTable("SCOTT", "EMP");
		table.addChild(createColumn("EMPNO", "NUMBER", null, "コメント"));
		f = AbstractSQLCreatorFactory.getFactory(table.getDbConfig(), table);
		f.setVisibleSchemaName(true);
		
		Column toCol = createColumn("EMPNO1", "NUMBER", null);
		sql = f.createRenameColumnDDL(table.getColumns()[0], toCol);
		assertEquals("ALTER TABLE SCOTT.EMP ALTER COLUMN EMPNO RENAME TO EMPNO1", sql);
		
		table = createTable("SCOTT-1", "EMP-1");
		table.addChild(createColumn("EMPNO-2", "NUMBER", null, "コメント"));
		f = AbstractSQLCreatorFactory.getFactory(table.getDbConfig(), table);
		f.setVisibleSchemaName(true);

		toCol = createColumn("EMPNO-1", "NUMBER", null);
		sql = f.createRenameColumnDDL(table.getColumns()[0], toCol);
		assertEquals("ALTER TABLE \"SCOTT-1\".\"EMP-1\" ALTER COLUMN \"EMPNO-2\" RENAME TO \"EMPNO-1\"", sql);
	}

	public void testCreateAddColumnDDL() {
		table = createTable("SCOTT", "EMP");
		table.addChild(createColumn("EMPNO", "CHAR", null, "コメント"));
		f = AbstractSQLCreatorFactory.getFactory(table.getDbConfig(), table);
		f.setVisibleSchemaName(true);
		
		Column newCol = createColumn("DEPTNO", "CHAR", null, "コメント");
		newCol.setSize("1");
		String[] sqls = f.createAddColumnDDL(newCol);
		assertEquals(1, sqls.length);
		assertEquals("ALTER TABLE SCOTT.EMP ADD COLUMN DEPTNO CHAR(1) NULL", sqls[0]);
		
		
		newCol = createColumn("DEPTNO", "VARCHAR", "123", "コメント");
		newCol.setSize("1");
		sqls = f.createAddColumnDDL(newCol);
		assertEquals(1, sqls.length);
		assertEquals("ALTER TABLE SCOTT.EMP ADD COLUMN DEPTNO VARCHAR(1) DEFAULT 123 NULL", sqls[0]);
		
		table = createTable("SCOTT-1", "EMP-1");
		table.addChild(createColumn("EMPNO", "VARCHAR", null, "コメント"));
		f = AbstractSQLCreatorFactory.getFactory(table.getDbConfig(), table);
		f.setVisibleSchemaName(true);		
		newCol = createColumn("DEPTNO-1", "VARCHAR", null, "コメント");
		newCol.setSize("1");
		sqls = f.createAddColumnDDL(newCol);
		assertEquals(1, sqls.length);
		assertEquals("ALTER TABLE \"SCOTT-1\".\"EMP-1\" ADD COLUMN \"DEPTNO-1\" VARCHAR(1) NULL", sqls[0]);
		
	}

	public void testCreateModifyColumnDDL() {
		table = createTable("SCOTT", "EMP");
		
		Column fromCol = createColumn("EMPNO", "NUMBER", null, "コメント");
		fromCol.setSize("1");
		table.addChild(fromCol);
		
		f = AbstractSQLCreatorFactory.getFactory(table.getDbConfig(), table);
		f.setVisibleSchemaName(true);
		
		// 名前はここでは変えられない(ALTER TABLE RENAME COLUMNが使われる）
		Column toCol = createColumn("EMONO", "VARCHAR2", "abc", "コメント2");
		toCol.setSize("10");
		toCol.setNotNull(true);
		
		String[] sqls = f.createModifyColumnDDL(fromCol, toCol);
		assertEquals(1, sqls.length);
		assertEquals("ALTER TABLE SCOTT.EMP ALTER COLUMN EMONO VARCHAR2(10) DEFAULT abc NOT NULL", sqls[0]);

		table = createTable("SCOTT-1", "EMP-1");
		
		fromCol = createColumn("EMPNO-1", "NUMBER", null, "コメント");
		fromCol.setSize("1");
		table.addChild(fromCol);
		
		f = AbstractSQLCreatorFactory.getFactory(table.getDbConfig(), table);
		f.setVisibleSchemaName(true);
		
		// 名前はここでは変えられない(ALTER TABLE RENAME COLUMNが使われる）
		toCol = createColumn("EMONO-1", "VARCHAR2", "abc", "コメント2");
		toCol.setSize("10");
		toCol.setNotNull(true);
		
		sqls = f.createModifyColumnDDL(fromCol, toCol);
		assertEquals(1, sqls.length);
		assertEquals("ALTER TABLE \"SCOTT-1\".\"EMP-1\" ALTER COLUMN \"EMONO-1\" VARCHAR2(10) DEFAULT abc NOT NULL", sqls[0]);

	}

	public void testCreateDropColumnDDL() {
		table = createTable("SCOTT", "EMP");
		
		Column col = createColumn("EMPNO", "NUMBER", null, "コメント");
		col.setSize("1");
		table.addChild(col);
		
		f = AbstractSQLCreatorFactory.getFactory(table.getDbConfig(), table);
		f.setVisibleSchemaName(true);
		
		String[] sqls = f.createDropColumnDDL(col, true);
		assertEquals(1, sqls.length);
		assertEquals("ALTER TABLE SCOTT.EMP DROP COLUMN EMPNO", sqls[0]);
		

		table = createTable("SCOTT-1", "EMP-1");
		col = createColumn("EMPNO-1", "NUMBER", null, "コメント");
		col.setSize("1");
		table.addChild(col);
		
		f = AbstractSQLCreatorFactory.getFactory(table.getDbConfig(), table);
		f.setVisibleSchemaName(true);
		
		sqls = f.createDropColumnDDL(col, true);
		assertEquals(1, sqls.length);
		assertEquals("ALTER TABLE \"SCOTT-1\".\"EMP-1\" DROP COLUMN \"EMPNO-1\"", sqls[0]);
		

	}

}
