/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.ui.bookmark;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.eclipse.swt.dnd.ByteArrayTransfer;
import org.eclipse.swt.dnd.TransferData;

import zigen.plugin.db.DbPlugin;
import zigen.plugin.db.ui.internal.TreeLeaf;

public class TreeLeafListTransfer extends ByteArrayTransfer {

	private static final String MYTYPENAME = "TreeLeafList"; //$NON-NLS-1$

	private static final int MYTYPEID = registerType(MYTYPENAME);

	private static TreeLeafListTransfer instance = new TreeLeafListTransfer();

	public TreeLeafListTransfer() {}

	public static TreeLeafListTransfer getInstance() {
		return instance;
	}

	public void javaToNative(Object object, TransferData transferData) {
		if (object == null || !(object instanceof TreeLeaf[])) {
			return;
		}
		if (isSupportedType(transferData)) {
			try {
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				ObjectOutputStream os = new ObjectOutputStream(out);
				os.writeObject((TreeLeaf[]) object);
				byte[] buffer = out.toByteArray();
				os.flush();
				os.close();

				super.javaToNative(buffer, transferData);
			} catch (IOException e) {
				DbPlugin.log(e);
			}
		}
	}

	public Object nativeToJava(TransferData transferData) {
		if (isSupportedType(transferData)) {
			byte[] buffer = (byte[]) super.nativeToJava(transferData);
			if (buffer == null) {
				return null;
			}
			TreeLeaf[] obj = null;
			try {
				ByteArrayInputStream in = new ByteArrayInputStream(buffer);
				ObjectInputStream os = new ObjectInputStream(in);
				obj = (TreeLeaf[]) os.readObject();
				os.close();
			} catch (Exception e) {
				DbPlugin.log(e);
				return null;
			}
			return obj;
		}
		return null;
	}

	protected String[] getTypeNames() {
		return new String[] {MYTYPENAME};
	}

	protected int[] getTypeIds() {
		return new int[] {MYTYPEID};
	}

}
