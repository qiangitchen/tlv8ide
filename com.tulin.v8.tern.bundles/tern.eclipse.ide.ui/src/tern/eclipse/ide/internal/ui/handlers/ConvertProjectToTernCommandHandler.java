package tern.eclipse.ide.internal.ui.handlers;

import org.eclipse.core.resources.IProject;
import org.eclipse.osgi.util.NLS;

import tern.eclipse.ide.internal.ui.TernUIMessages;
import tern.eclipse.ide.ui.handlers.AbstractConvertProjectCommandHandler;

/**
 * Convert selected project to Tern project.
 * 
 */
public class ConvertProjectToTernCommandHandler extends
		AbstractConvertProjectCommandHandler {

	protected String getConvertingProjectJobTitle(IProject project) {
		return NLS
				.bind(TernUIMessages.ConvertProjectToTern_converting_project_job_title,
						project.getName());
	}

}
