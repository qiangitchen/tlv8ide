package com.tulin.v8.ide.navigator.views;

import org.eclipse.swt.dnd.ByteArrayTransfer;

public class TreeLeafListTransfer extends ByteArrayTransfer {

	private static final String MYTYPENAME = "TreeLeafList"; //$NON-NLS-1$

	private static final int MYTYPEID = registerType(MYTYPENAME);

	private static TreeLeafListTransfer instance = new TreeLeafListTransfer();

	public TreeLeafListTransfer() {
	}

	public static TreeLeafListTransfer getInstance() {
		return instance;
	}

	protected String[] getTypeNames() {
		return new String[] { MYTYPENAME };
	}

	protected int[] getTypeIds() {
		return new int[] { MYTYPEID };
	}

}
