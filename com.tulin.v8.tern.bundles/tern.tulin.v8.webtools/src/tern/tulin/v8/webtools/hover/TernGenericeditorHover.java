package tern.tulin.v8.webtools.hover;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITextViewer;

import tern.ITernFile;
import tern.eclipse.ide.core.IIDETernProject;
import tern.eclipse.ide.core.TernCorePlugin;
import tern.eclipse.ide.core.resources.TernDocumentFile;
import tern.eclipse.ide.ui.hover.HTMLTernTypeCollector;
import tern.eclipse.ide.ui.hover.TernHover;
import tern.eclipse.jface.text.TernBrowserInformationControlInput;
import tern.server.TernNoTypeFoundAtPositionException;
import tern.server.protocol.type.TernTypeQuery;
import tern.utils.StringUtils;

public class TernGenericeditorHover extends TernHover {
	private IIDETernProject ternProject;
	private Integer offset;
	private ITernFile file;

	@Override
	public Object getHoverInfo2(ITextViewer textViewer, IRegion hoverRegion) {
		Object result = super.getHoverInfo2(textViewer, hoverRegion);
		if (result != null) {
			return result;
		}
		IFile scriptFile = getFile(textViewer);
		if (scriptFile == null) {
			return null;
		}
		IProject project = scriptFile.getProject();
		try {
			// project has tern nature, get hover info with tern.
			this.ternProject = TernCorePlugin.getTernProject(project, true);
			this.file = new TernDocumentFile(scriptFile, textViewer.getDocument());
			String filename = file.getFullName(ternProject);
			this.offset = hoverRegion.getOffset();
			TernTypeQuery query = new TernTypeQuery(filename, offset);
			query.setDocs(true);
			query.setUrls(true);
			query.setTypes(true);
			// query.setDocFormat(DocFormat.full);

			HTMLTernTypeCollector collector = new HTMLTernTypeCollector(ternProject);
			ternProject.request(query, file, collector);
			String text = collector.getInfo();
			return StringUtils.isEmpty(text) ? null : new TernBrowserInformationControlInput(null, text, 20);
		} catch (TernNoTypeFoundAtPositionException e) {
			// ignore error
		} catch (Exception e) {
			// ignore error
		}
		return null;
	}

}
