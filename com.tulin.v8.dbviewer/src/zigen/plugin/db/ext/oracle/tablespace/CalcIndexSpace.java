/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.ext.oracle.tablespace;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import zigen.plugin.db.core.IDBConfig;
import zigen.plugin.db.core.Transaction;
import zigen.plugin.db.ext.oracle.internal.OracleColumnSizeUtil;
import zigen.plugin.db.ext.oracle.internal.OracleDbBlockSizeSearcher;
import zigen.plugin.db.ui.internal.ITable;
import zigen.plugin.db.ui.internal.Table;

public class CalcIndexSpace {

	int block_header = 100; // 100byte

	double safeCoefficient = 2;

	private final long maxRecord; // expect record

	private final Table table;

	private final OracleIndexColumn[] columns;

	private final String ownerName;

	private final String tableName;

	private final String indexName;

	private final int pctFree;

	private int blockSize;

	private BigDecimal tableSpaceSize;

	private BigDecimal tableSpaceSafeSize;

	public BigDecimal getTableSpaceSafeSize() {
		return tableSpaceSafeSize;
	}

	public BigDecimal getTableSpaceSize() {
		return tableSpaceSize;
	}

	public CalcIndexSpace(Table table, String indexName, OracleIndexColumn[] columns, int pctFree, long maxRecord) {
		this.table = table;
		this.indexName = indexName;
		this.columns = columns;
		this.pctFree = pctFree;
		this.ownerName = table.getSchemaName();
		this.tableName = table.getName();
		this.maxRecord = maxRecord;
	}

	public CalcIndexSpace(Table table, int blockSize, String indexName, OracleIndexColumn[] columns, int pctFree, long maxRecord) {
		this.table = table;
		this.blockSize = blockSize;
		this.indexName = indexName;
		this.columns = columns;
		this.pctFree = pctFree;
		this.ownerName = table.getSchemaName();
		this.tableName = table.getName();
		this.maxRecord = maxRecord;
	}

	public void calcurate() throws Exception {
		try {

			IDBConfig config = table.getDbConfig();
			Connection con = Transaction.getInstance(config).getConnection();

			if (blockSize == 0) {
				blockSize = OracleDbBlockSizeSearcher.execute(con);
			}
			OracleColumnSizeUtil cs = new OracleColumnSizeUtil();
			int columnAreaSize = cs.getRowLength(con, columns);

			this.tableSpaceSize = new BigDecimal((getNecessaryBlockSize(columns, columnAreaSize, maxRecord) * blockSize) / (1024d * 1024d));
			this.tableSpaceSize = this.tableSpaceSize.setScale(3, BigDecimal.ROUND_UP);

			this.tableSpaceSafeSize = this.tableSpaceSize.multiply(new BigDecimal(safeCoefficient));

			this.tableSpaceSafeSize = this.tableSpaceSafeSize.setScale(3, BigDecimal.ROUND_UP);

		} catch (Exception e) {
			throw e;

		}

	}

	public double getBlockHeaderSize() {
		double d = 113 + 24 * 2;
		return d;
	}

	private final double getRiyouKanouArea() {
		double d = Math.ceil((blockSize - getBlockHeaderSize()) * (1 - pctFree / 100d));
		return d;

	}

	private double getIndexValueSize(IColumn[] columns, int columnAreaSize) {

		int entryHeader = 2;
		int rowId = 6;
		int f = 0;
		int v = 0;

		for (int i = 0; i < columns.length; i++) {
			IColumn column = columns[i];
			if (column.getColumn_length() < 128) {
				f++;
			} else {
				v++;
			}

		}
		double d = entryHeader + rowId + (f * 1 + v * 2) + columnAreaSize;
		return d;
	}

	private final double getAverageRowCountOfBlock(IColumn[] columns, int columnAreaSize) throws CalcTableSpaceException {
		double d = Math.floor(getRiyouKanouArea() / getIndexValueSize(columns, columnAreaSize));
		return d;
	}

	private final double getNecessaryBlockSize(IColumn[] columns, int columnAreaSize, long totalRow) throws CalcTableSpaceException {
		double d = Math.ceil(1.05 * totalRow / getAverageRowCountOfBlock(columns, columnAreaSize));
		return d;
	}

	public void setBlock_header(int block_header) {
		this.block_header = block_header;
	}

	public void setBlockSize(int blockSize) {
		this.blockSize = blockSize;
	}

	public void setSafeCoefficient(double safeCoefficient) {
		this.safeCoefficient = safeCoefficient;
	}

	public double getSafeCoefficient() {
		return safeCoefficient;
	}

	public int getBlockSize() {
		return blockSize;
	}

	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("[CalcIndexSpace:"); //$NON-NLS-1$
		buffer.append(" block_header: "); //$NON-NLS-1$
		buffer.append(block_header);
		buffer.append(" safeCoefficient: "); //$NON-NLS-1$
		buffer.append(safeCoefficient);
		buffer.append(" maxRecord: "); //$NON-NLS-1$
		buffer.append(maxRecord);
		buffer.append(" table: "); //$NON-NLS-1$
		buffer.append(table);
		buffer.append(" { "); //$NON-NLS-1$
		for (int i0 = 0; columns != null && i0 < columns.length; i0++) {
			buffer.append(" columns[" + i0 + "]: "); //$NON-NLS-1$ //$NON-NLS-2$
			buffer.append(columns[i0]);
		}
		buffer.append(" } "); //$NON-NLS-1$
		buffer.append(" ownerName: "); //$NON-NLS-1$
		buffer.append(ownerName);
		buffer.append(" tableName: "); //$NON-NLS-1$
		buffer.append(tableName);
		buffer.append(" pctFree: "); //$NON-NLS-1$
		buffer.append(pctFree);
		buffer.append(" blockSize: "); //$NON-NLS-1$
		buffer.append(blockSize);
		buffer.append(" tableSpaceSize: "); //$NON-NLS-1$
		buffer.append(tableSpaceSize);
		buffer.append(" tableSpaceSafeSize: "); //$NON-NLS-1$
		buffer.append(tableSpaceSafeSize);
		buffer.append("]"); //$NON-NLS-1$
		return buffer.toString();
	}

	public String getCalcResult() throws Exception {
		StringBuffer sb = new StringBuffer();
		sb.append(" [INDEX]:" + indexName + " "); //$NON-NLS-1$ //$NON-NLS-2$
		sb.append("("); //$NON-NLS-1$
		for (int i = 0; i < columns.length; i++) {
			OracleIndexColumn column = columns[i];
			if (i == 0) {
				sb.append(column.getColumn_name());
			} else {
				sb.append(", " + column.getColumn_name()); //$NON-NLS-1$
			}
		}
		sb.append(")"); //$NON-NLS-1$
		sb.append("\n"); //$NON-NLS-1$
		sb.append("   TABLE SPACE:"); //$NON-NLS-1$
		sb.append("  " + getTableSpaceSize() + " MB"); //$NON-NLS-1$ //$NON-NLS-2$
		sb.append("\n"); //$NON-NLS-1$
		sb.append("   TABLE SPACE :" + getSafeCoefficient() + ":"); //$NON-NLS-1$ //$NON-NLS-2$
		sb.append("  " + getTableSpaceSafeSize() + " MB"); //$NON-NLS-1$ //$NON-NLS-2$
		sb.append("\n"); //$NON-NLS-1$
		return sb.toString();
	}

	public List getCsvRow() {
		List list = new ArrayList();
		list.add(ownerName);
		list.add(tableName);
		list.add(indexName);

		list.add(String.valueOf(blockSize));
		list.add(String.valueOf(pctFree));

		list.add(String.valueOf(maxRecord));
		list.add(String.valueOf(tableSpaceSize));
		list.add(String.valueOf(safeCoefficient));
		list.add(String.valueOf(tableSpaceSafeSize));
		return list;
	}

	public int getBlock_header() {
		return block_header;
	}

	public OracleIndexColumn[] getColumns() {
		return columns;
	}

	public String getIndexName() {
		return indexName;
	}

	public long getMaxRecord() {
		return maxRecord;
	}

	public String getOwnerName() {
		return ownerName;
	}

	public int getPctFree() {
		return pctFree;
	}

	public ITable getTable() {
		return table;
	}

	public String getTableName() {
		return tableName;
	}
}
