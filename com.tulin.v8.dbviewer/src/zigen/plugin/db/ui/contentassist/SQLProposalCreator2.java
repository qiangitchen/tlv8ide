/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.ui.contentassist;

import java.util.List;

import org.eclipse.jface.text.contentassist.CompletionProposal;
import org.eclipse.jface.text.contentassist.ContextInformation;
import org.eclipse.swt.graphics.Image;

import zigen.plugin.db.DbPlugin;
import zigen.plugin.db.ImageCacher;
import zigen.plugin.db.core.SchemaInfo;
import zigen.plugin.db.core.TableInfo;
import zigen.plugin.db.ui.internal.Column;

public class SQLProposalCreator2 {

	public static void addProposal(List proposals, String[] modifiers, ProcessorInfo pinfo) {
		String word = pinfo.getWord();
		int offset = pinfo.getOffset();
		boolean isAfterPeriod = pinfo.isAfterPeriod();

		if (modifiers != null) {
			Image img = getImage(DbPlugin.IMG_CODE_SQL);

			if (isAfterPeriod)
				word = "";

			int len = word.length();
			for (int i = 0; i < modifiers.length; i++) {
				String modifier = modifiers[i];
				String value = ContentAssistUtil.subString(modifiers[i], len);
				if (value != null && value.compareToIgnoreCase(word) == 0) {
					proposals.add(new CompletionProposal(modifier, offset - len, len, modifier.length(), img, null, new ContextInformation(null, modifier), null));

				}
			}
		}
	}

	public static void addProposal(List proposals, String[][] modifiers, ProcessorInfo pinfo) {
		String word = pinfo.getWord();
		int offset = pinfo.getOffset();
		boolean isAfterPeriod = pinfo.isAfterPeriod();

		if (modifiers != null) {
			// Image img = getImage(DbPlugin.IMG_CODE_SQL);
			Image img = getImage(DbPlugin.IMG_CODE_UNKNOWN);

			if (isAfterPeriod)
				word = "";

			int len = word.length();
			for (int i = 0; i < modifiers.length; i++) {
				String modifier = modifiers[i][0];
				String display = modifiers[i][1]; // display
				String value = ContentAssistUtil.subString(modifier, len);
				if (value != null && value.compareToIgnoreCase(word) == 0) {
					proposals.add(new CompletionProposal(modifier, offset - len, len, modifier.length(), img, display, new ContextInformation(null, modifier), null));

				}
			}
		}
	}

	public static void addProposal(List proposals, SchemaInfo[] schemas, ProcessorInfo pinfo) {
		String word = pinfo.getWord();
		int offset = pinfo.getOffset();
		boolean isAfterPeriod = pinfo.isAfterPeriod();

		if (schemas != null) {
			Image img = getImage(DbPlugin.IMG_CODE_SCHEMA);;

			if (isAfterPeriod)
				word = "";

			int len = word.length();
			for (int i = 0; i < schemas.length; i++) {
				SchemaInfo info = schemas[i];
				String modifier = info.getName();
				String display = modifier;

				String comment = "Schema";

				if (comment != null && !"".equals(comment)) { //$NON-NLS-1$
					StringBuffer sb = new StringBuffer();
					sb.append(modifier);
					sb.append(" ["); //$NON-NLS-1$
					sb.append(comment);
					sb.append("]"); //$NON-NLS-1$
					display = sb.toString();
				}


				String value = ContentAssistUtil.subString(modifier, len);
				if (value != null && value.compareToIgnoreCase(word) == 0) {
					proposals.add(new CompletionProposal(modifier, offset - len, len, modifier.length(), img, display, new ContextInformation(null, modifier), null));

				}
			}

		}
	}

	public static void addProposal(List proposals, TableInfo[] tables, ProcessorInfo pinfo) {
		String word = pinfo.getWord();
		int offset = pinfo.getOffset();
		boolean isAfterPeriod = pinfo.isAfterPeriod();

		if (tables != null) {
			Image img = null;

			if (isAfterPeriod)
				word = "";

			int len = word.length();
			for (int i = 0; i < tables.length; i++) {
				TableInfo info = tables[i];

				if ("TABLE".equals(info.getTableType())) {
					img = getImage(DbPlugin.IMG_CODE_TABLE);
				} else if ("VIEW".equals(info.getTableType())) { //$NON-NLS-1$
					img = getImage(DbPlugin.IMG_CODE_VIEW);
				} else if ("SYNONYM".equals(info.getTableType())) {
					img = getImage(DbPlugin.IMG_CODE_SYNONYM);
				} else if ("FUNCTION".equals(info.getTableType())) {
					img = getImage(DbPlugin.IMG_CODE_FUNCTION);
				} else if ("SEQUENCE".equals(info.getTableType())) {
					img = getImage(DbPlugin.IMG_CODE_SEQUENCE);
				} else {
					img = getImage(DbPlugin.IMG_CODE_UNKNOWN);
				}

				String modifier = info.getName();
				String display = modifier;

				String comment = info.getComment();

				if (comment != null && !"".equals(comment)) { //$NON-NLS-1$
					StringBuffer sb = new StringBuffer();
					sb.append(modifier);
					sb.append(" ["); //$NON-NLS-1$
					sb.append(comment);
					sb.append("]"); //$NON-NLS-1$
					display = sb.toString();
				}

				String value = ContentAssistUtil.subString(modifier, len);
				if (value != null && value.compareToIgnoreCase(word) == 0) {
					proposals.add(new CompletionProposal(modifier, offset - len, len, modifier.length(), img, display, new ContextInformation(null, modifier), null));

				}
			}

		}
	}

	public static void addProposal(List proposals, Column[] columns, ProcessorInfo pinfo) {
		String word = pinfo.getWord();
		int offset = pinfo.getOffset();
		boolean isAfterPeriod = pinfo.isAfterPeriod();

		if (columns != null) {
			if (isAfterPeriod)
				word = "";

			int len = word.length();
			for (int i = 0; i < columns.length; i++) {

				Image img = getImage(DbPlugin.IMG_CODE_COLUMN);

				Column info = columns[i];

				if (info.hasPrimaryKey()) {
					img = getImage(DbPlugin.IMG_CODE_PK_COLUMN);
				} else if (info.isNotNull()) {
					img = getImage(DbPlugin.IMG_CODE_NOTNULL_COLUMN);
				}
				String modifier = info.getName();
				String display = info.getColumnLabel();

				String value = ContentAssistUtil.subString(modifier, len);
				if (value != null && value.compareToIgnoreCase(word) == 0) {
					proposals.add(new CompletionProposal(modifier, offset - len, len, modifier.length(), img, display, new ContextInformation(null, modifier), null));

				}
			}
		}
	}

	public static void addProposalForFunction(List proposals, String[] modifiers, ProcessorInfo pinfo) {
		String word = pinfo.getWord();
		int offset = pinfo.getOffset();
		boolean isAfterPeriod = pinfo.isAfterPeriod();

		if (modifiers != null) {
			Image img = getImage(DbPlugin.IMG_CODE_FUNCTION);

			if (isAfterPeriod)
				word = "";

			int len = word.length();
			for (int i = 0; i < modifiers.length; i++) {
				String modifier = modifiers[i];
				String value = ContentAssistUtil.subString(modifiers[i], len);
				if (value != null && value.compareToIgnoreCase(word) == 0) {
					proposals.add(new CompletionProposal(modifier, offset - len, len, modifier.length(), img, null, new ContextInformation(null, modifier), null));

				}
			}
		}
	}

	private static Image getImage(String imageCode) {
		return ImageCacher.getInstance().getImage(imageCode);

	}

}
