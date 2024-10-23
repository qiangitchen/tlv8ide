package com.tulin.v8.webtools.ide.html;

import org.eclipse.jface.text.DocumentEvent;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITypedRegion;
import org.eclipse.jface.text.Region;
import org.eclipse.jface.text.rules.DefaultDamagerRepairer;
import org.eclipse.jface.text.rules.ITokenScanner;

public class JavaScriptDamagerRepairer extends DefaultDamagerRepairer {

	public JavaScriptDamagerRepairer(ITokenScanner scanner) {
		super(scanner);
	}

	// TODO This method works with 3.0 and 3.1.2 but does't work well with Eclipse
	// 3.1.1.
	public IRegion getDamageRegion(ITypedRegion partition, DocumentEvent e, boolean documentPartitioningChanged) {
		if (!documentPartitioningChanged) {
			String source = fDocument.get();
			int start = source.substring(0, e.getOffset()).lastIndexOf("/*");
			if (start == -1) {
				start = 0;
			}
			int end = source.indexOf("*/", e.getOffset());
			int end2 = e.getOffset() + (e.getText() == null ? e.getLength() : e.getText().length());
			if (end == -1) {
				end = source.length();
			} else if (end2 > end) {
				end = end2;
			} else {
				end++;
			}

			return new Region(start, end - start);
		}
		return partition;
	}

}
