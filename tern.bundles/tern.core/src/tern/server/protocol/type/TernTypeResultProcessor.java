package tern.server.protocol.type;

import tern.server.protocol.IJSONObjectHelper;
import tern.server.protocol.ITernResultProcessor;
import tern.server.protocol.TernDoc;

public class TernTypeResultProcessor implements
		ITernResultProcessor<ITernTypeCollector> {

	public static final TernTypeResultProcessor INSTANCE = new TernTypeResultProcessor();

	@Override
	public void process(TernDoc doc, IJSONObjectHelper jsonObjectHelper,
			Object jsonObject, ITernTypeCollector collector) {
		String type = jsonObjectHelper.getText(jsonObject, "type"); //$NON-NLS-1$
		boolean guess = jsonObjectHelper.getBoolean(jsonObject, "guess", //$NON-NLS-1$
				false);
		String name = jsonObjectHelper.getText(jsonObject, "name"); //$NON-NLS-1$
		String exprName = jsonObjectHelper.getText(jsonObject, "exprName"); //$NON-NLS-1$
		String documentation = jsonObjectHelper.getText(jsonObject, "doc"); //$NON-NLS-1$
		String url = jsonObjectHelper.getText(jsonObject, "url"); //$NON-NLS-1$
		String origin = jsonObjectHelper.getText(jsonObject, "origin"); //$NON-NLS-1$
		collector.setType(type, guess, name, exprName, documentation, url,
				origin, jsonObject, jsonObjectHelper);
	}

}
