package tern.server.protocol.definition;

import tern.server.protocol.IJSONObjectHelper;
import tern.server.protocol.ITernResultProcessor;
import tern.server.protocol.TernDoc;
import tern.utils.StringUtils;

public class TernDefinitionResultProcessor implements ITernResultProcessor<ITernDefinitionCollector> {

	/**
	 * Properties for JSON definition result.
	 */
	private static final String START_PROPERTY = "start"; //$NON-NLS-1$
	private static final String END_PROPERTY = "end"; //$NON-NLS-1$
	private static final String FILE_PROPERTY = "file"; //$NON-NLS-1$
	private static final String ORIGIN_PROPERTY = "origin"; //$NON-NLS-1$
	
	public static final TernDefinitionResultProcessor INSTANCE = new TernDefinitionResultProcessor();

	@Override
	public void process(TernDoc doc, IJSONObjectHelper jsonObjectHelper, Object jsonObject,
			ITernDefinitionCollector collector) {
		collect(jsonObjectHelper, jsonObject, collector);
	}

	public void collect(IJSONObjectHelper jsonObjectHelper, Object jsonObject,
			ITernDefinitionCollector collector) {
		Long startCh = jsonObjectHelper.getCh(jsonObject, START_PROPERTY);
		Long endCh = jsonObjectHelper.getCh(jsonObject, END_PROPERTY);
		String file = jsonObjectHelper.getText(jsonObject, FILE_PROPERTY);
		if (StringUtils.isEmpty(file)) {
			file = jsonObjectHelper.getText(jsonObject, ORIGIN_PROPERTY);
		}
		collector.setDefinition(file, startCh, endCh);
	}

}
