/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.ui.actions;

import java.util.Iterator;

import org.eclipse.jface.text.ITextOperationTarget;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;

import zigen.plugin.db.DbPlugin;
import zigen.plugin.db.core.ClipboardUtils;
import zigen.plugin.db.core.StringUtil;
import zigen.plugin.db.core.TableColumn;
import zigen.plugin.db.core.TableElement;
import zigen.plugin.db.ui.editors.TableViewEditorFor31;
import zigen.plugin.db.ui.editors.internal.ColumnFilterInfo;

public class CopyRecordTrimDataAction extends CopyRecordDataAction {

	public CopyRecordTrimDataAction() {
		super();
		this.setText(Messages.getString("CopyRecordTrimDataAction.0")); //$NON-NLS-1$
	}

	public void run() {
		try {

			StringBuffer sb = new StringBuffer();
			Clipboard clipboard = ClipboardUtils.getInstance();

			IStructuredSelection selection = (IStructuredSelection) editor.getViewer().getSelection();
			Iterator iter = selection.iterator();

			int index = 0;
			while (iter.hasNext()) {
				Object obj = iter.next();
				if (obj instanceof TableElement) {
					TableElement elem = (TableElement) obj;
					TableColumn[] columns = elem.getColumns();

					if (index == 0) {
						createHeader(sb, columns);
					}

					boolean isFirst = true;
					for (int i = 0; i < columns.length; i++) {
						if (isVisibled(i)) {
							TableColumn col = columns[i];
							int type = col.getDataType();
							// String value = String.valueOf(elem.getItem(col));
							//String value = String.valueOf(elem.getItem(i));

							String value = String.valueOf(elem.getItem(i));
							if(value != null && value.length() > 0){
								if(!" ".equals(value)){
									value = StringUtil.rTrim(value, ' ');
									if(value.length() == 0){
										value = " ";
									}
								}else{
									;
								}
							}

							if (value.indexOf("\"") >= 0) { //$NON-NLS-1$
								value = value.replaceAll("\"", "\"\""); //$NON-NLS-1$ //$NON-NLS-2$
								// //$NON-NLS-1$
								// //$NON-NLS-2$
							}
							if (value.length() == 0) {
								value = "\"\""; //$NON-NLS-1$
							} else {
								value = value.replaceAll("^|$", "\""); //$NON-NLS-1$ //$NON-NLS-2$
								// //$NON-NLS-1$
								// //$NON-NLS-2$
							}

							if (isFirst) {
								sb.append(value);
								isFirst = false;
							} else {
								sb.append(DEMILITER + value);
							}
						}

					}
					sb.append(LINE_SEP);
					index++;

				}
			}
			clipboard.setContents(new Object[] {sb.toString()}, new Transfer[] {TextTransfer.getInstance()});

		} catch (Exception e) {
			DbPlugin.getDefault().showErrorDialog(e);

		} finally {
			editor.getViewer().getControl().notifyListeners(SWT.Selection, null);
		}

	}

}
