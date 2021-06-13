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

import zigen.plugin.db.DbPlugin;
import zigen.plugin.db.core.IDBConfig;
import zigen.plugin.db.core.Transaction;
import zigen.plugin.db.ext.oracle.internal.OracleColumnSizeUtil;
import zigen.plugin.db.ext.oracle.internal.OracleDbBlockSizeSearcher;
import zigen.plugin.db.ext.oracle.internal.OracleTableColumnSearcher;
import zigen.plugin.db.ui.internal.Table;

public class CalcTableSpace {

	int block_header = 100; // 100bye

	double safeCoefficient = 1.2;

	private final long maxRecord;

	private final Table table;

	private final String ownerName;

	private final String tableName;

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

	public CalcTableSpace(Table table, int pctFree, long maxRecord) {
		this.table = table;
		this.pctFree = pctFree;
		this.ownerName = table.getSchemaName();
		this.tableName = table.getName();
		this.maxRecord = maxRecord;
	}

	public CalcTableSpace(Table table, int blockSize, int pctFree, long maxRecord) {
		this.table = table;
		this.blockSize = blockSize;
		this.pctFree = pctFree;
		this.ownerName = table.getSchemaName();
		this.tableName = table.getName();
		this.maxRecord = maxRecord;
	}

	public void calcurate() throws CalcTableSpaceException {
		try {
			IDBConfig config = table.getDbConfig();
			Connection con = Transaction.getInstance(config).getConnection();

			if (blockSize == 0) {
				blockSize = OracleDbBlockSizeSearcher.execute(con);
			}

			double necessaryBlock = getNecessaryBlockSize(con, maxRecord);

			this.tableSpaceSize = new BigDecimal((necessaryBlock * blockSize) / (1024d * 1024d));

			this.tableSpaceSize = this.tableSpaceSize.setScale(3, BigDecimal.ROUND_UP);

			this.tableSpaceSafeSize = tableSpaceSize.multiply(new BigDecimal(safeCoefficient));
			this.tableSpaceSafeSize = this.tableSpaceSafeSize.setScale(3, BigDecimal.ROUND_UP);

		} catch (CalcTableSpaceException e) {
			throw e;

		} catch (Exception e) {
			throw new CalcTableSpaceException("The error occurred by the estimate processing in the table area.", e.getCause()); //$NON-NLS-1$

		}

	}

	public int getBlockHeaderSize() {
		return this.block_header;
	}

	private final double getRiyouKanouArea() {
		double d = Math.ceil((blockSize - this.getBlockHeaderSize()) * (1 - pctFree / 100d));
		return d;

	}

	private final int getRowLength(Connection con) {
		int columnAreaSize = -1;
		try {
			boolean convertUnicode = false;

			OracleTableColumn[] columns = OracleTableColumnSearcher.execute(con, table, convertUnicode);

			OracleColumnSizeUtil cs = new OracleColumnSizeUtil();
			columnAreaSize = cs.getRowLength(con, columns);

		} catch (Exception e) {
			DbPlugin.getDefault().showErrorDialog(e);
		}

		return columnAreaSize;
	}

	private final double getAverageRowCountOfBlock(Connection con) throws CalcTableSpaceException {
		double d = 0.0d;
		double riyoukanou = getRiyouKanouArea();
		double rowLen = getRowLength(con);

		if (riyoukanou >= rowLen) {
			d = Math.floor(riyoukanou / rowLen);
		} else {
			d = 1.0d / Math.ceil(rowLen / riyoukanou);

		}
		return d;
	}

	private final double getNecessaryBlockSize(Connection con, long totalRow) throws CalcTableSpaceException {
		double d = Math.ceil(totalRow / getAverageRowCountOfBlock(con));
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

	public String getCalcResult() throws Exception {
		StringBuffer sb = new StringBuffer();
		sb.append(" [TABLE]:" + tableName + " "); //$NON-NLS-1$ //$NON-NLS-2$
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
		list.add("");

		list.add(String.valueOf(blockSize));
		list.add(String.valueOf(pctFree));

		list.add(String.valueOf(maxRecord));
		list.add(String.valueOf(tableSpaceSize));
		list.add(String.valueOf(safeCoefficient));
		list.add(String.valueOf(tableSpaceSafeSize));
		return list;
	}

	public String csvString() {
		StringBuffer sb = new StringBuffer();
		sb.append(ownerName);
		sb.append(","); //$NON-NLS-1$
		sb.append(tableName);
		sb.append(","); //$NON-NLS-1$
		sb.append(""); //$NON-NLS-1$
		sb.append(","); //$NON-NLS-1$
		sb.append(maxRecord);
		sb.append(","); //$NON-NLS-1$
		sb.append(tableSpaceSize);
		sb.append(","); //$NON-NLS-1$
		sb.append(safeCoefficient);
		sb.append(","); //$NON-NLS-1$
		sb.append(tableSpaceSafeSize);
		sb.append("\n"); //$NON-NLS-1$
		return sb.toString();
	}

	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("[CalcTableSpace:"); //$NON-NLS-1$
		buffer.append(" block_header: "); //$NON-NLS-1$
		buffer.append(block_header);
		buffer.append(" safeCoefficient: "); //$NON-NLS-1$
		buffer.append(safeCoefficient);
		buffer.append(" maxRecord: "); //$NON-NLS-1$
		buffer.append(maxRecord);
		buffer.append(" table: "); //$NON-NLS-1$
		buffer.append(table);
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
}
