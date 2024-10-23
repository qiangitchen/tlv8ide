package com.tulin.v8.webtools.ide;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.ICommand;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IProjectNature;
import org.eclipse.core.runtime.CoreException;

public class ProjectNature implements IProjectNature {
	public static final String HTML_NATURE_ID = "com.tulin.v8.webtools.ide.ProjectNature";
	public static final String HTML_BUILDER_ID = "com.tulin.v8.webtools.ide.ProjectBuilder";
	private IProject project;

	public void configure() throws CoreException {
		IProjectDescription desc = project.getDescription();
		ICommand[] commands = desc.getBuildSpec();
		for (int i = 0; i < commands.length; i++) {
			if (commands[i].getBuilderName().equals(HTML_BUILDER_ID)) {
				return;
			}
		}
		ICommand command = desc.newCommand();
		command.setBuilderName(HTML_BUILDER_ID);
		ICommand[] newCommands = new ICommand[commands.length + 1];
		for (int i = 0; i < commands.length; i++) {
			newCommands[i] = commands[i];
		}
		newCommands[newCommands.length - 1] = command;
		desc.setBuildSpec(newCommands);
		project.setDescription(desc, null);
	}

	public void deconfigure() throws CoreException {
		IProjectDescription desc = project.getDescription();
		ICommand[] commands = desc.getBuildSpec();
		List<ICommand> list = new ArrayList<ICommand>();
		for (int i = 0; i < commands.length; i++) {
			if (!commands[i].getBuilderName().equals(HTML_BUILDER_ID)) {
				list.add(commands[i]);
			}
		}
		desc.setBuildSpec(list.toArray(new ICommand[list.size()]));
		project.setDescription(desc, null);
	}

	public IProject getProject() {
		return this.project;
	}

	public void setProject(IProject project) {
		this.project = project;
	}

}
