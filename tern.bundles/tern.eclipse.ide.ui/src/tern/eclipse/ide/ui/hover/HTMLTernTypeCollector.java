package tern.eclipse.ide.ui.hover;

import tern.eclipse.ide.core.IIDETernProject;
import tern.eclipse.ide.ui.utils.HTMLTernPrinter;
import tern.server.protocol.IJSONObjectHelper;
import tern.server.protocol.completions.TernCompletionItem;
import tern.server.protocol.completions.TernCompletionProposalRec;
import tern.server.protocol.type.ITernTypeCollector;

/**
 * {@link ITernTypeCollector} implementation for HTML type collector.
 * 
 */
public class HTMLTernTypeCollector implements ITernTypeCollector {

	private final IIDETernProject ternProject;
	private String info;

	public HTMLTernTypeCollector(IIDETernProject ternProject) {
		this.ternProject = ternProject;
		this.info = null;
	}

	@Override
	public void setType(String type, boolean guess, String name,
			String exprName, String doc, String url, String origin,
			Object object, IJSONObjectHelper objectHelper) {
		if (type != null || name != null || exprName != null) {
			String label = name != null ? name : exprName;
			TernCompletionItem item = new TernCompletionItem(
					new TernCompletionProposalRec(label, type, doc, url, origin));
			item.setTernProject(ternProject);
			this.info = HTMLTernPrinter.getAdditionalProposalInfo(item, guess);
		}
	}

	/**
	 * Returns the HTML of the tern type.
	 * 
	 * @return the HTML of the tern type.
	 */
	public String getInfo() {
		return info;
	}
}
