package tern.server.protocol.refs;

import tern.server.protocol.IJSONObjectHelper;
import tern.server.protocol.ITernResultProcessor;
import tern.server.protocol.TernDoc;
import tern.server.protocol.definition.TernDefinitionResultProcessor;

public class TernRefsResultProcessor implements ITernResultProcessor<ITernRefCollector> {

	public static final TernRefsResultProcessor INSTANCE = new TernRefsResultProcessor();

	/**
	 * Properties for JSON refs result.
	 */
	private static final String REFS_PROPERTY = "refs"; //$NON-NLS-1$

	@Override
	public void process(TernDoc doc, IJSONObjectHelper objectHelper, Object jsonObject, ITernRefCollector collector) {
		Iterable<Object> refs = objectHelper.getList(jsonObject, REFS_PROPERTY);
		if (refs != null) {
			for (Object ref : refs) {
				TernDefinitionResultProcessor.INSTANCE.collect(objectHelper, ref, collector);
			}
		}
	}

}
