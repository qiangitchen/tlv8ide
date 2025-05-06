package tern.eclipse.ide.server.nodejs.internal.ui;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;

import org.eclipse.swt.widgets.Display;

import tern.ITernProject;
import tern.eclipse.ide.core.IIDETernProject;
import tern.eclipse.ide.ui.TernUIPlugin;
import tern.eclipse.ide.ui.console.ITernConsole;
import tern.eclipse.ide.ui.console.LineType;
import tern.server.LoggingInterceptor;
import tern.server.nodejs.process.INodejsProcessListener;
import tern.server.nodejs.process.INodejsProcess;
import tern.utils.IOUtils;

public class TernNodejsInterceptor extends LoggingInterceptor implements
		INodejsProcessListener {

	private final IIDETernProject project;

	public TernNodejsInterceptor(IIDETernProject project) {
		this.project = project;
	}

	@Override
	protected void outPrintln(String line) {
		ITernConsole console = getConsole();
		if (console != null) {
			console.doAppendLine(LineType.DATA, line);
		}
	}

	protected void outProcessPrintln(String line) {
		ITernConsole console = getConsole();
		if (console != null) {
			console.doAppendLine(LineType.PROCESS_INFO, line);
		}
	}

	protected void errPrintln(String line) {
		ITernConsole console = getConsole();
		if (console != null) {
			console.doAppendLine(LineType.PROCESS_ERROR, line);
		}
	}

	@Override
	protected void printStackTrace(Throwable error) {
		ITernConsole console = getConsole();
		if (console != null) {
			StringWriter s = new StringWriter();
			PrintWriter writer = new PrintWriter(s);
			error.printStackTrace(writer);
			console.doAppendLine(LineType.PROCESS_ERROR, s.toString());
		}
	}

	@Override
	public void onCreate(final INodejsProcess process,
			final List<String> commands, final File projectDir) {
		if (Display.getDefault().getThread() != Thread.currentThread()) {
			Display.getDefault().asyncExec(new Runnable() {
				@Override
				public void run() {
					onCreate(process, commands, projectDir);
				}
			});
			return;
		}
		ITernConsole console = getConsole();
		if (console != null) {
			StringBuilder commandsAsString = new StringBuilder();
			int i = 0;
			for (String cmd : commands) {
				if (i > 0) {
					commandsAsString.append(" ");
				}
				if (i <= 1) {
					commandsAsString.append("\"");
				}
				commandsAsString.append(cmd);
				if (i <= 1) {
					commandsAsString.append("\"");
				}
				i++;
			}
			outProcessPrintln("Nodejs Commnand: " + commandsAsString.toString());
			String path = projectDir.getPath();
			try {
				path = projectDir.getCanonicalPath();
			} catch (IOException e) {
			}
			outProcessPrintln("Project dir: " + path);
			String json = "";
			try {
				File ternProject = new File(projectDir,
						ITernProject.TERN_PROJECT_FILE);
				json = IOUtils.toString(new FileInputStream(ternProject));
			} catch (Throwable e) {
				errPrintln(e.getMessage());
			}

			outProcessPrintln(ITernProject.TERN_PROJECT_FILE + ": " + json);
		}
	}

	@Override
	public void onStart(INodejsProcess process) {
		outProcessPrintln("Server started at " + process.getPort() + " in "
				+ process.getElapsedStartTime() + "ms");
	}

	@Override
	public void onData(INodejsProcess process, String line) {
		outProcessPrintln(line);
	}

	@Override
	public void onStop(INodejsProcess process) {
		outProcessPrintln("Server stopped at " + process.getPort());
	}

	@Override
	public void onError(INodejsProcess process, String line) {
		errPrintln(line);
	}

	private ITernConsole getConsole() {
		if (TernUIPlugin.getDefault() != null) {
			return TernUIPlugin.getDefault().getConsole(project);
		}
		return null;
	}
}
