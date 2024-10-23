package com.tulin.v8.webtools.ide.assist;

import org.eclipse.core.resources.IFile;

public interface IFileAssistProcessor {

	public void reload(IFile file);

	public AssistInfo[] getAssistInfo(String value);

}
