package com.tulin.v8.webtools.ide.js.editors;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.debug.core.Launch;
import org.eclipse.debug.core.model.IProcess;
import org.eclipse.jdt.launching.IVMRunner;
import org.eclipse.jdt.launching.JavaRuntime;
import org.eclipse.jdt.launching.VMRunnerConfiguration;

import com.tulin.v8.webtools.ide.ProjectParams;
import com.tulin.v8.webtools.ide.WebToolsPlugin;
import com.tulin.v8.webtools.ide.html.HTMLUtil;
import com.tulin.v8.webtools.ide.js.launch.ClosureCompilerLaunchUtil;
import com.tulin.v8.webtools.ide.js.model.JavaScriptContext;
import com.tulin.v8.webtools.ide.js.model.JavaScriptElement;
import com.tulin.v8.webtools.ide.js.model.JavaScriptModel;
import com.tulin.v8.webtools.ide.js.model.ModelManager;
import com.tulin.v8.webtools.ide.utils.IOUtil;

/**
 * The validator for JavaScriptEditor.
 */
public class JavaScriptValidator {

	private IFile file;
	private List<String> errorOptionList;
	private List<String> warnOptionList;
	private List<String> offOptionList;
	private boolean useUndefinedError = false;
	private boolean useUndefinedWarn = false;

	public JavaScriptValidator(IFile file) {
		this.file = file;
	}

	public void doValidate() {
		try {
			file.deleteMarkers(IMarker.PROBLEM, false, 0);

			ProjectParams params = new ProjectParams(file.getProject());
			if (!params.getValidateJavaScript()) {
				return;
			}

			String useUndefined = params.getJavaScriptUseUndefined();
			useUndefinedError = "Error".equals(useUndefined);
			useUndefinedWarn = "Warning".equals(useUndefined);

			errorOptionList = new ArrayList<String>();
			warnOptionList = new ArrayList<String>();
			offOptionList = new ArrayList<String>();
			boolean enableValidator = false;
			enableValidator = enableValidator || appendOption("accessControls", params.getJavaScriptAccessControls());
			enableValidator = enableValidator || appendOption("checkRegExp", params.getJavaScriptCheckRegExp());
			enableValidator = enableValidator || appendOption("checkTypes", params.getJavaScriptCheckTypes());
			enableValidator = enableValidator || appendOption("checkVars", params.getJavaScriptCheckVars());
			enableValidator = enableValidator || appendOption("deprecated", params.getJavaScriptDeprecated());
			enableValidator = enableValidator
					|| appendOption("fileoverviewTags", params.getJavaScriptFileoverviewTags());
			enableValidator = enableValidator || appendOption("invalidCasts", params.getJavaScriptInvalidCasts());
			enableValidator = enableValidator
					|| appendOption("missingProperties", params.getJavaScriptMissingProperties());
			enableValidator = enableValidator
					|| appendOption("nonStandardJsDocs", params.getJavaScriptNonStandardJsDocs());
			enableValidator = enableValidator
					|| appendOption("strictModuleDepCheck", params.getJavaScriptStrictModuleDepCheck());
			enableValidator = enableValidator || appendOption("undefinedVars", params.getJavaScriptUndefinedVars());
			enableValidator = enableValidator || appendOption("unknownDefines", params.getJavaScriptUnknownDefines());
			enableValidator = enableValidator || appendOption("visibility", params.getJavaScriptVisibility());
			if (enableValidator) {
				new ValidaterJob("Validating " + file.getName()).schedule();
			}
		} catch (Exception ex) {
			WebToolsPlugin.logException(ex);
		}

		if (useUndefinedError || useUndefinedWarn) {
			InputStream in = null;
			try {
				in = file.getContents();
				String source = new String(IOUtil.readStream(in));
				JavaScriptModel model = ModelManager.getInstance().getCachedModel(file, source);
				Map<Integer, Map<String, Object>> markerMap = new TreeMap<Integer, Map<String, Object>>();
				validateElements(markerMap, model, new HashSet<JavaScriptContext>());
				int count = 0;
				int line = 1;
				for (Map.Entry<Integer, Map<String, Object>> entry : markerMap.entrySet()) {
					while (count < entry.getKey()) {
						char c = source.charAt(count);
						if (c == '\r') {
							char next = source.charAt(count + 1);
							if (next == '\n') {
								count++;
							}
							line++;
						} else if (c == '\n') {
							line++;
						}
						count++;
					}
					IMarker marker = file.createMarker(IMarker.PROBLEM);
					Map<String, Object> map = entry.getValue();
					map.put(IMarker.LINE_NUMBER, Integer.valueOf(line));
					marker.setAttributes(map);
				}
			} catch (Exception e) {
				WebToolsPlugin.logException(e);
			} finally {
				IOUtil.closeQuietly(in);
			}
		}
	}

	private void validateElements(Map<Integer, Map<String, Object>> markerMap, JavaScriptContext jsContext,
			Set<JavaScriptContext> done) {
		if (done.contains(jsContext)) {
			return;
		} else {
			done.add(jsContext);
		}
		for (JavaScriptElement jsElement : jsContext.getElements()) {
			if (jsElement.isUndefined()) {
				int start = jsElement.getStart();
				if (start >= 0) {
					// int length = jsElement.getName() == null ? 0 : jsElement
					// .getName().length();
					String message = WebToolsPlugin.createMessage(
							WebToolsPlugin.getResourceString("Validation.UseUndefined"),
							new String[] { jsElement.getName() });
					Map<String, Object> map = new HashMap<String, Object>();
					if (useUndefinedError) {
						map.put(IMarker.SEVERITY, Integer.valueOf(IMarker.SEVERITY_ERROR));
					} else {
						map.put(IMarker.SEVERITY, Integer.valueOf(IMarker.SEVERITY_WARNING));
					}
					map.put(IMarker.MESSAGE, message);
					// map.put(IMarker.CHAR_START, Integer.valueOf(start));
					// map.put(IMarker.CHAR_END, Integer.valueOf(start + length));
					markerMap.put(start, map);
				}
			}
			JavaScriptContext context = jsElement.getContext();
			if (context != null && context.getModel() == jsContext.getModel()) {
				validateElements(markerMap, context, done);
			}
		}
		for (JavaScriptContext child : jsContext.getContexts()) {
			validateElements(markerMap, child, done);
		}
	}

	private boolean appendOption(String label, String value) {
		if ("Error".equals(value)) {
			errorOptionList.add(label);
			return true;
		} else if ("Warning".equals(value)) {
			warnOptionList.add(label);
			return true;
		} else {
			offOptionList.add(label);
			return false;
		}
	}

	class ValidaterJob extends Job {
		ValidaterJob(String name) {
			super(name);
		}

		@Override
		protected IStatus run(IProgressMonitor monitor) {
			File outputFile = null;
			try {
				outputFile = File.createTempFile("tk_eclipse_plugin_", ".js");

				ClosureCompilerLaunchUtil.copyLibraries();

				VMRunnerConfiguration vmConfig = new VMRunnerConfiguration(
						"com.google.javascript.jscomp.CommandLineRunner",
						ClosureCompilerLaunchUtil.getClassPathAsStringArray());
				List<String> options = new ArrayList<String>();
				options.add("--charset");
				options.add(file.getCharset());
				options.add("--js");
				options.add(file.getLocation().makeAbsolute().toString());
				options.add("--js_output_file");
				options.add(outputFile.getAbsolutePath());
				for (String value : errorOptionList) {
					options.add("--jscomp_error");
					options.add(value);
				}
				for (String value : warnOptionList) {
					options.add("--jscomp_warning");
					options.add(value);
				}
				for (String value : offOptionList) {
					options.add("--jscomp_off");
					options.add(value);
				}
				vmConfig.setProgramArguments(options.toArray(new String[options.size()]));
				Launch launch = new Launch(null, ILaunchManager.RUN_MODE, null);
				IVMRunner vmRunner = JavaRuntime.getDefaultVMInstall().getVMRunner(ILaunchManager.RUN_MODE);

				vmRunner.run(vmConfig, launch, null);

				for (int i = 0; i < 10; i++) {
					if (launch.isTerminated()) {
						break;
					}
					Thread.sleep(1000L);
				}

				StringBuilder outputBuf = new StringBuilder(1000);
				for (IProcess process : launch.getProcesses()) {
					outputBuf.append(process.getStreamsProxy().getOutputStreamMonitor().getContents());
					outputBuf.append(process.getStreamsProxy().getErrorStreamMonitor().getContents());
				}
				parseOutput(outputBuf.toString());
			} catch (Exception e) {
				WebToolsPlugin.logException(e);
				return new Status(Status.ERROR, "unknown", e.getMessage());
			} finally {
				if (outputFile != null) {
					outputFile.delete();
				}
			}

			return Status.OK_STATUS;
		}

		private void parseOutput(String output) {
			String[] lines = output.split("[\n\r]");
			String filename = file.getName();
			Pattern pattern = Pattern.compile("^.*" + filename + ":([0-9]+): ([A-Z]+) - (.*)$");
			for (String line : lines) {
				Matcher matcher = pattern.matcher(line);
				if (matcher.find()) {
					String lineNum = matcher.group(1);
					String level = matcher.group(2);
					String message = matcher.group(3);
					if ("ERROR".equals(level)) {
						HTMLUtil.addMarker(file, IMarker.SEVERITY_ERROR, Integer.parseInt(lineNum), message);
					} else if ("WARNING".equals(level)) {
						HTMLUtil.addMarker(file, IMarker.SEVERITY_WARNING, Integer.parseInt(lineNum), message);
					}
				}
			}
		}
	}

}
