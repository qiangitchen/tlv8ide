package tern.eclipse.ide.ui.hover;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.internal.text.html.BrowserInformationControl;

import tern.ITernFile;
import tern.eclipse.ide.core.IIDETernProject;
import tern.eclipse.ide.ui.utils.EditorUtils;
import tern.eclipse.jface.text.HoverLocationListener;
import tern.server.protocol.definition.ITernDefinitionCollector;
import tern.server.protocol.definition.TernDefinitionQuery;
import tern.utils.StringUtils;

/**
 * IDE hover location listener.
 *
 */
@SuppressWarnings("restriction")
public class IDEHoverLocationListener extends HoverLocationListener implements
		ITernDefinitionCollector {

	private final ITernHoverInfoProvider provider;

	public IDEHoverLocationListener(BrowserInformationControl control,
			ITernHoverInfoProvider provider) {
		super(control);
		this.provider = provider;
	}

	@Override
	protected void handleTernFileLink(String loc) {
		super.handleTernFileLink(loc);
		IFile file = provider.getTernProject().getIDEFile(loc);
		if (file.exists()) {
			EditorUtils.openInEditor(file, -1, -1, true);
		}
	}

	@Override
	protected void handleTernDefinitionLink(String loc) {
		super.handleTernDefinitionLink(loc);

		ITernFile tf = provider.getFile();
		if (tf != null) {
			IIDETernProject ternProject = provider.getTernProject();
			Integer pos = provider.getOffset();
			TernDefinitionQuery query = new TernDefinitionQuery(
					tf.getFullName(ternProject), pos);
			try {
				ternProject.request(query, tf, this);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void setDefinition(String filename, Long start, Long end) {
		IFile file = getFile(filename);
		if (file != null && file.exists()) {
			EditorUtils.openInEditor(
					file,
					start != null ? start.intValue() : -1,
					start != null && end != null ? end.intValue()
							- start.intValue() : -1, true);
		}
	}

	private IFile getFile(String filename) {
		if (StringUtils.isEmpty(filename)) {
			return null;
		}
		IIDETernProject ternProject = provider.getTernProject();
		return ternProject.getIDEFile(filename);
	}

}
