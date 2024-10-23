package tern.server.protocol.highlight;

import tern.server.protocol.IJSONObjectHelper;
import tern.server.protocol.ITernResultProcessor;
import tern.server.protocol.TernDoc;

public class TernHighlightResultProcessor implements ITernResultProcessor<ITernHighlightCollector> {

	public static final TernHighlightResultProcessor INSTANCE = new TernHighlightResultProcessor();

	@Override
	public void process(TernDoc doc, IJSONObjectHelper jsonObjectHelper, Object jsonObject,
			ITernHighlightCollector collector) {
		String file = "highlight"; //doc.getQuery().getFile();
		Iterable<Object> elements = jsonObjectHelper.getList(jsonObject, file);
		for (Object elt : elements) {
			collector.higlight(jsonObjectHelper.getText(elt, "type"), jsonObjectHelper.getLong(elt, "start"),
					jsonObjectHelper.getLong(elt, "end"));
		}
	}

}
