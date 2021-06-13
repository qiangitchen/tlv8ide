package zigen.plugin.db.core.rule;

import junit.framework.TestCase;
import kry.sql.format.ISqlFormat;
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


public class OracleSQLCreatorFactoryTest extends TestCase {

	protected String driverName;
	protected Table table;
	protected ISQLCreatorFactory f;
	protected String sql;
	
	protected void setUp() throws Exception {
		driverName = "oracle";
		table = null;
		f = null;
		sql = null;
	}

	public static final String DB_NAME = "DB";
	public static final String FOLDER_TABLE = "TABLE";
	
	protected Table createTable(String schemaName, String tableName) {
		IDBConfig config = new DBConfig();
		config.setDriverName(driverName);
		config.setDbName(DB_NAME);
		DataBase db = new DataBase(config);
		Table table;
		if (schemaName != null) {
			db.setSchemaSupport(true);
			Schema schema = new Schema(schemaName);
			schema.setParent(db);
			Folder folder = new Folder(FOLDER_TABLE);
			folder.setParent(schema);
			table = new Table(tableName);
			table.setParent(folder);
		}else{
			db.setSchemaSupport(false);
			Folder folder = new Folder(FOLDER_TABLE);
			folder.setParent(db);
			table = new Table(tableName);
			table.setParent(folder);
		}
		return table;
	}
	protected Column createColumn(String colName, String type, String defualtValue, String remarks){
		TableColumn tc = new TableColumn();
		tc.setColumnName(colName);
		tc.setTypeName(type);
		tc.setDefaultValue(defualtValue);
		tc.setRemarks(remarks);
		Column col = new Column(tc);
		return col;
	}
	protected Column createColumn(String colName, String type, String defualtValue){
		return createColumn(colName, type, defualtValue, null);
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
		assertEquals("ALTER TABLE SCOTT.EMP ADD CONSTRAINT MYINDEX CHECK(EMPNO > 0)", sql);

		table = createTable("SCOTT-1", "EMP-1");
		table.addChild(createColumn("EMPNO-1", "NUMBER", null));
		f = AbstractSQLCreatorFactory.getFactory(table.getDbConfig(), table);
		f.setVisibleSchemaName(true);
		
		sql = f.createCreateConstraintCheckDDL("MYINDEX-1","EMPNO > 0");
		assertEquals("ALTER TABLE \"SCOTT-1\".\"EMP-1\" ADD CONSTRAINT \"MYINDEX-1\" CHECK(EMPNO > 0)",sql);

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
		assertEquals("COMMENT ON TABLE SCOTT.EMP IS 'コメントです'", sql);

		table = createTable("SCOTT-1", "EMP-1");
		f = AbstractSQLCreatorFactory.getFactory(table.getDbConfig(), table);
		f.setVisibleSchemaName(true);
		sql = f.createCommentOnTableDDL("コ'メント\"です");
		assertEquals("COMMENT ON TABLE \"SCOTT-1\".\"EMP-1\" IS 'コ''メント\"です'", sql);

	}

	public void testCreateCommentOnColumnDDL() {
		table = createTable("SCOTT", "EMP");
		table.addChild(createColumn("EMPNO", "NUMBER", null,"コメント"));
		f = AbstractSQLCreatorFactory.getFactory(table.getDbConfig(), table);
		f.setVisibleSchemaName(true);
		sql = f.createCommentOnColumnDDL(table.getColumns()[0]);
		assertEquals("COMMENT ON COLUMN SCOTT.EMP.EMPNO IS 'コメント'", sql);

		table = createTable("SCOTT-1", "EMP-1");
		table.addChild(createColumn("EMPNO-1", "NUMBER", null,"コ'メン\"ト"));
		f = AbstractSQLCreatorFactory.getFactory(table.getDbConfig(), table);
		f.setVisibleSchemaName(true);		
		sql = f.createCommentOnColumnDDL(table.getColumns()[0]);
		assertEquals("COMMENT ON COLUMN \"SCOTT-1\".\"EMP-1\".\"EMPNO-1\" IS 'コ''メン\"ト'", sql);

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
		assertEquals("ALTER TABLE SCOTT.EMP RENAME COLUMN EMPNO TO EMPNO1", sql);
		
		table = createTable("SCOTT-1", "EMP-1");
		table.addChild(createColumn("EMPNO-2", "NUMBER", null, "コメント"));
		f = AbstractSQLCreatorFactory.getFactory(table.getDbConfig(), table);
		f.setVisibleSchemaName(true);

		toCol = createColumn("EMPNO-1", "NUMBER", null);
		sql = f.createRenameColumnDDL(table.getColumns()[0], toCol);
		assertEquals("ALTER TABLE \"SCOTT-1\".\"EMP-1\" RENAME COLUMN \"EMPNO-2\" TO \"EMPNO-1\"", sql);
	}

	public void testCreateAddColumnDDL() {
		table = createTable("SCOTT", "EMP");
		table.addChild(createColumn("EMPNO", "NUMBER", null, "コメント"));
		f = AbstractSQLCreatorFactory.getFactory(table.getDbConfig(), table);
		f.setVisibleSchemaName(true);
		
		Column newCol = createColumn("DEPTNO", "NUMBER", null, "コメント");
		newCol.setSize("1");
		String[] sqls = f.createAddColumnDDL(newCol);
		assertEquals(1, sqls.length);
		assertEquals("ALTER TABLE SCOTT.EMP ADD (DEPTNO NUMBER(1) NULL)", sqls[0]);
		
		table = createTable("SCOTT-1", "EMP-1");
		table.addChild(createColumn("EMPNO", "NUMBER", null, "コメント"));
		f = AbstractSQLCreatorFactory.getFactory(table.getDbConfig(), table);
		f.setVisibleSchemaName(true);		
		newCol = createColumn("DEPTNO-1", "NUMBER", null, "コメント");
		newCol.setSize("1");
		sqls = f.createAddColumnDDL(newCol);
		assertEquals(1, sqls.length);
		assertEquals("ALTER TABLE \"SCOTT-1\".\"EMP-1\" ADD (\"DEPTNO-1\" NUMBER(1) NULL)", sqls[0]);
		
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
		assertEquals(3, sqls.length);
		assertEquals("ALTER TABLE SCOTT.EMP MODIFY (EMONO VARCHAR2(10))", sqls[0]);
		assertEquals("ALTER TABLE SCOTT.EMP MODIFY (EMONO DEFAULT 'abc')", sqls[1]);
		assertEquals("ALTER TABLE SCOTT.EMP MODIFY (EMONO NOT NULL)", sqls[2]);
		

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
		assertEquals(3, sqls.length);
		assertEquals("ALTER TABLE \"SCOTT-1\".\"EMP-1\" MODIFY (\"EMONO-1\" VARCHAR2(10))", sqls[0]);
		assertEquals("ALTER TABLE \"SCOTT-1\".\"EMP-1\" MODIFY (\"EMONO-1\" DEFAULT 'abc')", sqls[1]);
		assertEquals("ALTER TABLE \"SCOTT-1\".\"EMP-1\" MODIFY (\"EMONO-1\" NOT NULL)", sqls[2]);
		
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
		assertEquals("ALTER TABLE SCOTT.EMP DROP COLUMN EMPNO CASCADE CONSTRAINTS", sqls[0]);
		

		table = createTable("SCOTT-1", "EMP-1");
		col = createColumn("EMPNO-1", "NUMBER", null, "コメント");
		col.setSize("1");
		table.addChild(col);
		
		f = AbstractSQLCreatorFactory.getFactory(table.getDbConfig(), table);
		f.setVisibleSchemaName(true);
		
		sqls = f.createDropColumnDDL(col, true);
		assertEquals(1, sqls.length);
		assertEquals("ALTER TABLE \"SCOTT-1\".\"EMP-1\" DROP COLUMN \"EMPNO-1\" CASCADE CONSTRAINTS", sqls[0]);
		

	}

}
