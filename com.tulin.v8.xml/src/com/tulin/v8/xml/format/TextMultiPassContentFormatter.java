package com.tulin.v8.xml.format;

import java.util.ArrayList;
import java.util.List;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.TypedPosition;
import org.eclipse.jface.text.formatter.IFormattingContext;
import org.eclipse.jface.text.formatter.IFormattingStrategy;
import org.eclipse.jface.text.formatter.MultiPassContentFormatter;
import org.eclipse.wst.sse.core.internal.Logger;
import org.eclipse.wst.sse.ui.internal.ExtendedConfigurationBuilder;

@SuppressWarnings({ "restriction", "unchecked", "rawtypes" })
public class TextMultiPassContentFormatter extends MultiPassContentFormatter {

	private List fInstalledPartitionTypes;

	public TextMultiPassContentFormatter(String partitioning, String type) {
		super(partitioning, type);
	}

	protected void formatMaster(IFormattingContext context, IDocument document,
			int offset, int length) {
		long startTime = System.currentTimeMillis();
		super.formatMaster(context, document, offset, length);
		if (Logger.DEBUG_FORMAT) {
			long endTime = System.currentTimeMillis();
			System.out.println("formatModel time: " + (endTime - startTime));
		}
	}

	protected void formatSlave(IFormattingContext context, IDocument document,
			int offset, int length, String type) {
		List installedTypes = getInstalledPartitionTypes();
		if (installedTypes.contains(type)) {
			super.formatSlave(context, document, offset, length, type);
		} else {
			boolean findExtendedSlaveFormatter = true;
			super.formatSlave(context, document, offset, length, type);
			Object contextPartition = context
					.getProperty("formatting.context.partition");
			if ((contextPartition instanceof TypedPosition)) {
				String contextType = ((TypedPosition) contextPartition)
						.getType();
				if (contextType == type) {
					installedTypes.add(type);
					findExtendedSlaveFormatter = false;
				}
			}
			if (findExtendedSlaveFormatter) {
				Object configuration = ExtendedConfigurationBuilder
						.getInstance().getConfiguration(
								"slaveformattingstrategy", type);
				if ((configuration instanceof IFormattingStrategy)) {
					setSlaveStrategy((IFormattingStrategy) configuration, type);

					super.formatSlave(context, document, offset, length, type);
				}
				installedTypes.add(type);
			}
		}
	}

	private List getInstalledPartitionTypes() {
		if (this.fInstalledPartitionTypes == null)
			this.fInstalledPartitionTypes = new ArrayList();
		return this.fInstalledPartitionTypes;
	}
}