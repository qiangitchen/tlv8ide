package com.tulin.v8.ide.ui.editors.page.design.selection;

import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

import org.eclipse.wst.html.core.internal.document.ElementStyleImpl;

@SuppressWarnings("restriction")
public class ModelSelection implements Transferable, ClipboardOwner {
	private static final int STRING = 0;

	public static final DataFlavor rangeFlavor = new DataFlavor(ElementStyleImpl.class, "ElementStyleImpl Range");

	private static final DataFlavor[] flavors = { rangeFlavor };
	private Object data;

	public ModelSelection(Object data) {
		this.data = data;
	}

	@Override
	public void lostOwnership(Clipboard clipboard, Transferable contents) {
		// TODO 自动生成的方法存根

	}

	@Override
	public DataFlavor[] getTransferDataFlavors() {
		return (DataFlavor[]) flavors.clone();
	}

	@Override
	public boolean isDataFlavorSupported(DataFlavor flavor) {
		for (int i = 0; i < flavors.length; i++) {
			if (flavor.equals(flavors[i])) {
				return true;
			}
		}
		return false;
	}

	@Override
	public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
		if (flavor.equals(flavors[STRING])) {
			return data;
		} else {
			throw new UnsupportedFlavorException(flavor);
		}
	}

}
