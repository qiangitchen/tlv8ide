package tern.eclipse.ide.linter.core.validation;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.wst.validation.internal.operations.LocalizedMessage;
import org.eclipse.wst.validation.internal.provisional.core.IMessage;
import org.eclipse.wst.validation.internal.provisional.core.IReporter;
import org.eclipse.wst.validation.internal.provisional.core.IValidator;

import tern.eclipse.ide.core.IIDETernProject;
import tern.eclipse.ide.core.utils.FileUtils;
import tern.server.protocol.IJSONObjectHelper;
import tern.server.protocol.lint.ITernLintCollector;
import tern.server.protocol.lint.TernLintQuery;

/**
 * Tern report collector to add in WTP {@link IReporter} messages coming from
 * tern linter.
 *
 */
@SuppressWarnings("restriction")
public class TernReporterCollector implements ITernLintCollector {

	private static final String CHAR_END = "charEnd";
	private static final String CHAR_START = "charStart";
	private static final String WARNING_SEVERITY = "warning";

	private final IIDETernProject ternProject;
	private final IReporter reporter;
	private final IValidator validator;

	public TernReporterCollector(IIDETernProject ternProject, IReporter reporter, IValidator validator) {
		this.ternProject = ternProject;
		this.reporter = reporter;
		this.validator = validator;
	}

	@Override
	public void startLint(String file) {

	}

	@Override
	public void addMessage(String messageId, String messageText, Long start, Long end, Long line, String severity,
			String file, Object messageObject, TernLintQuery query, IJSONObjectHelper helper) {
		if (start == null) {
			start = 0L;
		}
		if (end == null) {
			end = start + 1;
		}
		IResource resource = (IResource) ternProject.getFile(file).getAdapter(IFile.class);
		LocalizedMessage message = new LocalizedMessage(getSeverity(severity), messageText, resource);
		message.setOffset(start.intValue());
		message.setLength(end.intValue() - start.intValue());
		message.setAttribute(CHAR_START, start.intValue());
		message.setAttribute(CHAR_END, end.intValue());
		Integer lineNumber = getLineNumber(line, start.intValue(), resource);
		if (lineNumber != null) {
			message.setLineNo(lineNumber);
		}
		addMessage(message, messageObject, query, helper);
	}

	protected void addMessage(LocalizedMessage message, Object messageObject, TernLintQuery query, IJSONObjectHelper helper) {
		reporter.addMessage(validator, message);
	}

	/**
	 * Try to retrieve the line number.
	 */
	private Integer getLineNumber(Long line, int start, IResource resource) {
		if (line != null) {
			return line.intValue();
		}
		if (start >= 0) {
			return FileUtils.getLineOfOffset(start, (IFile) resource);
		}
		return null;
	}

	private int getSeverity(String severity) {
		return WARNING_SEVERITY.equals(severity) ? IMessage.NORMAL_SEVERITY : IMessage.HIGH_SEVERITY;
	}

	@Override
	public void endLint(String file) {

	}

	public IReporter getReporter() {
		return reporter;
	}

	public IValidator getValidator() {
		return validator;
	}
}
