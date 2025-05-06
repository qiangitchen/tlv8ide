package com.tulin.v8.webtools.ide.xml.editors;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.xerces.parsers.SAXParser;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.resource.StringConverter;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.texteditor.ITextEditorActionConstants;
import org.xml.sax.InputSource;

import com.tulin.v8.fuzzyxml.FuzzyXMLDocument;
import com.tulin.v8.fuzzyxml.FuzzyXMLElement;
import com.tulin.v8.fuzzyxml.FuzzyXMLNode;
import com.tulin.v8.fuzzyxml.FuzzyXMLParser;
import com.tulin.v8.webtools.ide.ProjectParams;
import com.tulin.v8.webtools.ide.WebToolsPlugin;
import com.tulin.v8.webtools.ide.formatter.Formater;
import com.tulin.v8.webtools.ide.html.HTMLHyperlinkDetector;
import com.tulin.v8.webtools.ide.html.HTMLUtil;
import com.tulin.v8.webtools.ide.html.IHTMLOutlinePage;
import com.tulin.v8.webtools.ide.html.editors.HTMLConfiguration;
import com.tulin.v8.webtools.ide.html.editors.HTMLSourceEditor;
import com.tulin.v8.webtools.ide.xml.DTDResolver;
import com.tulin.v8.webtools.ide.xml.ElementSchemaMapping;
import com.tulin.v8.webtools.ide.xml.IDTDResolver;
import com.tulin.v8.webtools.ide.xml.SchemaGenerator;

/**
 * The XML editor.
 */
public class XMLEditor extends HTMLSourceEditor {
	private List<IDTDResolver> resolvers = new ArrayList<IDTDResolver>();

	public static final String GROUP_XML = "_xml";
	public static final String ACTION_GEN_DTD = "_generate_dtd";
	public static final String ACTION_GEN_XSD = "_generate_xsd";
	public static final String ACTION_ESCAPE_XML = "_escape_xml";
//	public static final String ACTION_XPATH = "_search_xpath";

	private String[] classNameAttributes = null;
	private List<ElementSchemaMapping> schemaMappings = null;

	/**
	 * The constructor.
	 */
	public XMLEditor() {
		XMLConfiguration config = new XMLConfiguration(this, WebToolsPlugin.getDefault().getColorProvider());
		setSourceViewerConfiguration(config);
		setEditorContextMenuId("#AmaterasXMLEditor");
	}

	protected void update() {
		super.update();
		HTMLHyperlinkDetector hyperlinkDetector = ((HTMLConfiguration) getSourceViewerConfiguration())
				.getHyperlinkDetector();
		hyperlinkDetector.setEditor(this);
	}

	@Override
	protected void createActions() {
		super.createActions();

		setAction(ACTION_GEN_DTD, new GenerateDTDAction());
		setAction(ACTION_GEN_XSD, new GenerateXSDAction());
		setAction(ACTION_ESCAPE_XML, new EscapeXMLAction());
//		setAction(ACTION_XPATH, new SearchXPathAction());
		setAction(ACTION_FORMAT, new FormatAction());
	}

	/** This method is called when configuration is changed. */
	@Override
	protected void handlePreferenceStoreChanged(PropertyChangeEvent event) {
		super.handlePreferenceStoreChanged(event);
		classNameAttributes = null;
		schemaMappings = null;
	}

	public List<ElementSchemaMapping> getSchemaMappings() {
		if (schemaMappings == null) {
			schemaMappings = ElementSchemaMapping.loadFromPreference();
		}
		return schemaMappings;
	}

	public String[] getClassNameAttributes() {
		if (classNameAttributes == null) {
			// Load classname attrs from the preference store
			IPreferenceStore store = getPreferenceStore();
			if (store.getBoolean(WebToolsPlugin.PREF_ENABLE_CLASSNAME)) {
				classNameAttributes = StringConverter.asArray(store.getString(WebToolsPlugin.PREF_CLASSNAME_ATTRS));
			} else {
				classNameAttributes = new String[0];
			}
		}
		return classNameAttributes;
	}

	/**
	 * Returns the <code>XMLOutlinePage</code>.
	 *
	 * @see XMLOutlinePage
	 */
	@Override
	protected IHTMLOutlinePage createOutlinePage() {
		return new XMLOutlinePage(this);
	}

	/**
	 * Adds <code>IDTDResolver</code>.
	 *
	 * @param resolver IDTDResolver
	 */
	public void addDTDResolver(IDTDResolver resolver) {
		resolvers.add(resolver);
	}

	/**
	 * Returns an array of <code>IDTDResolver</code> that was added by
	 * <code>addEntityResolver()</code>.
	 *
	 * @return an array of <code>IDTDResolver</code>
	 */
	public IDTDResolver[] getDTDResolvers() {
		return (IDTDResolver[]) resolvers.toArray(new IDTDResolver[resolvers.size()]);
	}

	/**
	 * Validates the XML document.
	 * <p>
	 * If <code>getValidation()</code> returns <code>false</code>, this method do
	 * nothing.
	 */
	@Override
	protected void doValidate() {
		new Job("XML Validation") {
			@Override
			protected IStatus run(IProgressMonitor monitor) {
				try {
					IFileEditorInput input = (IFileEditorInput) getEditorInput();
					String xml = getDocumentProvider().getDocument(input).get();
					IFile resource = input.getFile();
					resource.deleteMarkers(IMarker.PROBLEM, false, IResource.DEPTH_ZERO);

					ProjectParams params = new ProjectParams(resource.getProject());
					if (!params.getValidateXML()) {
						return Status.OK_STATUS;
					}

					if (params.getUseDTD() == false) {
						Matcher matcher = patternDoctypePublic.matcher(xml);
						if (matcher.find()) {
							xml = removeMatched(xml, matcher.start(), matcher.end());
						}
						matcher = patternDoctypeSystem.matcher(xml);
						if (matcher.find()) {
							xml = removeMatched(xml, matcher.start(), matcher.end());
						}
					}

					SAXParser saxParser = new SAXParser();
					saxParser.setErrorHandler(new XMLValidationHandler(resource));
					saxParser.parse(new InputSource(new StringReader(xml)));

				} catch (Exception ex) {
				}
				return Status.OK_STATUS;
			}
		}.schedule();
	}

	/** replace to whitespaces */
	private String removeMatched(String source, int start, int end) {
		StringBuffer sb = new StringBuffer();
		sb.append(source.substring(0, start));
		for (int i = start; i < end + 1; i++) {
			char c = source.charAt(i);
			if (c == '\r' || c == '\n') {
				sb.append(c);
			} else {
				sb.append(" ");
			}
		}
		sb.append(source.substring(end + 1, source.length()));
		return sb.toString();
	}

	/**
	 * Returns URI of DTD (SystemID) which is used in the document. If any DTD isn't
	 * used, this method returns <code>null</code>.
	 *
	 * @param xml XML
	 * @return URL of DTD
	 */
	public String getDTD(String xml, boolean useElementMapping) {
		// PUBLIC Identifier
		Matcher matcher = patternDoctypePublic.matcher(xml);
		if (matcher.find()) {
			return matcher.group(2);
		}
		// SYSTEM Identifier
		matcher = patternDoctypeSystem.matcher(xml);
		if (matcher.find()) {
			return matcher.group(1);
		}

		// Root element mappings
		if (useElementMapping) {
			String firstTag = getFirstTag(xml);
			if (firstTag != null) {
				for (ElementSchemaMapping mapping : getSchemaMappings()) {
					if (mapping.getRootElement().equals(firstTag) && mapping.getFilePath().endsWith(".dtd")) {
						return "file:" + mapping.getFilePath();
					}
				}
			}
		}

		return null;
	}

	/**
	 * Returns URI (schema location) of XML schema which is used in the document. If
	 * any XML schema isn't used, this method returns <code>null</code>.
	 *
	 * @param xml XML
	 * @return URL of XML schema
	 */
	public String[] getXSD(String xml, boolean useElementMapping) {
		// PUBLIC Identifier
		Matcher matcher = patternNsXSD.matcher(xml);
		if (matcher.find()) {
			String matched = matcher.group(1).trim();
			matched.replaceAll("\r\n", "\n");
			matched.replaceAll("\r", "\n");
			String[] xsd = matched.split("\n| |\t");
			for (int i = 0; i < xsd.length; i++) {
				xsd[i] = xsd[i].trim();
			}
			return xsd;
		}
		matcher = patternNoNsXSD.matcher(xml);
		if (matcher.find()) {
			return new String[] { matcher.group(1).trim() };
		}

		matcher = patternXmlns.matcher(xml);
		if (matcher.find()) {
			return new String[] { matcher.group(1).trim() };
		}

		// Root element mappings
		if (useElementMapping) {
			String firstTag = getFirstTag(xml);
			if (firstTag != null) {
				for (ElementSchemaMapping mapping : getSchemaMappings()) {
					if (mapping.getRootElement().equals(firstTag) && mapping.getFilePath().endsWith(".xsd")) {
						return new String[] { "file:" + mapping.getFilePath() };
					}
				}
			}
		}

		return null;
	}

	/**
	 * Extracts the first element name in the given xml source.
	 */
	private static String getFirstTag(String xml) {
		FuzzyXMLDocument doc = new FuzzyXMLParser().parse(xml);
		FuzzyXMLNode[] nodes = doc.getDocumentElement().getChildren();
		for (int i = 0; i < nodes.length; i++) {
			if (nodes[i] instanceof FuzzyXMLElement) {
				return ((FuzzyXMLElement) nodes[i]).getName();
			}
		}
		return null;
	}

	/** Reular expressions to get DOCTYPE declaration */
	private Pattern patternDoctypePublic = Pattern.compile(
			"<!DOCTYPE[\\s\r\n]+?[^<]+?[\\s\r\n]+?PUBLIC[\\s\r\n]*?\"(.+?)\"[\\s\r\n]*?\"(.+?)\".*?>", Pattern.DOTALL);
	private Pattern patternDoctypeSystem = Pattern
			.compile("<!DOCTYPE[\\s\r\n]+?[^<]+?[\\s\r\n]+?SYSTEM[\\s\r\n]*?\"(.+?)\".*?>", Pattern.DOTALL);

	/** Reular expressions to get schema location of XMLschema */
	private Pattern patternNsXSD = Pattern.compile("schemaLocation[\\s\r\n]*?=[\\s\r\n]*?\"(.+?)\"", Pattern.DOTALL);
	private Pattern patternNoNsXSD = Pattern.compile("noNamespaceSchemaLocation[\\s\r\n]*?=[\\s\r\n]*?\"(.+?)\"",
			Pattern.DOTALL);
	private Pattern patternXmlns = Pattern.compile("xmlns[\\s\r\n]*?=[\\s\r\n]*?\"(.+?)\"", Pattern.DOTALL);

	/**
	 * Update informations about code-completion.
	 */
	@Override
	protected void updateAssist() {
		XMLConfiguration config = (XMLConfiguration) getSourceViewerConfiguration();
		config.getClassNameHyperlinkProvider().setEditor(this);

		if (!isFileEditorInput()) {
			return;
		}
		super.updateAssist();

		new Job("Update Content Assist Information") {
			@Override
			protected IStatus run(IProgressMonitor monitor) {
				try {

					IFileEditorInput input = (IFileEditorInput) getEditorInput();
					ProjectParams params = new ProjectParams(input.getFile().getProject());
					if (params.getUseDTD() == false) {
						return Status.OK_STATUS;
					}

					String xml = getDocumentProvider().getDocument(input).get();

					// Update DTD based completion information.
					String dtd = getDTD(xml, true);
					if (dtd != null) {
						DTDResolver resolver = new DTDResolver(getDTDResolvers(),
								input.getFile().getLocation().makeAbsolute().toFile().getParentFile());
						InputStream in = resolver.getInputStream(dtd);
						if (in != null) {
							Reader reader = new InputStreamReader(in);
							// update AssistProcessor
							XMLAssistProcessor assistProcessor = (XMLAssistProcessor) ((HTMLConfiguration) getSourceViewerConfiguration())
									.getAssistProcessor();
							assistProcessor.updateDTDInfo(reader);
							reader.close();
						}
					}

					// Update XML Schema based completion information.
					String[] xsd = getXSD(xml, true);
					if (xsd != null) {
						DTDResolver resolver = new DTDResolver(getDTDResolvers(),
								input.getFile().getLocation().makeAbsolute().toFile().getParentFile());
						for (int i = 0; i < xsd.length; i++) {
							InputStream in = resolver.getInputStream(xsd[i]);
							if (in != null) {
								Reader reader = new InputStreamReader(in);
								// update AssistProcessor
								XMLAssistProcessor assistProcessor = (XMLAssistProcessor) ((HTMLConfiguration) getSourceViewerConfiguration())
										.getAssistProcessor();
								assistProcessor.updateXSDInfo(xsd[i], reader);
								reader.close();
							}
						}
					}
				} catch (Exception ex) {
					WebToolsPlugin.logException(ex);
				}

				return Status.OK_STATUS;
			}
		}.schedule();
	}

	@Override
	protected void addContextMenuActions(IMenuManager menu) {
		super.addContextMenuActions(menu);
		menu.appendToGroup(ITextEditorActionConstants.GROUP_EDIT,
				new MenuManager(WebToolsPlugin.getResourceString("PreferencePage.XML"), GROUP_HTML));
		addAction(menu, GROUP_HTML, ACTION_SEARCH_XPATH);
		addAction(menu, GROUP_HTML, ACTION_ESCAPE_XML);

		menu.add(new Separator(GROUP_XML));
		addGroup(menu, GROUP_HTML, GROUP_XML);
		addAction(menu, GROUP_HTML, ACTION_GEN_DTD);
		addAction(menu, GROUP_HTML, ACTION_GEN_XSD);
//		addAction(menu, GROUP_XML, ACTION_XPATH);
	}

	@Override
	protected void updateSelectionDependentActions() {
		super.updateSelectionDependentActions();
		ITextSelection sel = (ITextSelection) getSelectionProvider().getSelection();
		if (sel.getText().equals("")) {
			getAction(ACTION_ESCAPE_XML).setEnabled(false);
		} else {
			getAction(ACTION_ESCAPE_XML).setEnabled(true);
		}
	}

	/**
	 * The action to escape XML special chars in the selection.
	 */
	private class EscapeXMLAction extends Action {

		public EscapeXMLAction() {
			super(WebToolsPlugin.getResourceString("HTMLEditor.EscapeAction"));
			setEnabled(false);
			setAccelerator(SWT.CTRL | '\\');
		}

		@Override
		public void run() {
			ITextSelection sel = (ITextSelection) getSelectionProvider().getSelection();
			IDocument doc = getDocumentProvider().getDocument(getEditorInput());
			try {
				doc.replace(sel.getOffset(), sel.getLength(), HTMLUtil.escapeXML(sel.getText()));
			} catch (BadLocationException e) {
				WebToolsPlugin.logException(e);
			}
		}
	}

	private class FormatAction extends Action {
		Formater formater = new Formater();

		public FormatAction() {
			super(WebToolsPlugin.getResourceString("HTMLEditor.Format"));
			setActionDefinitionId("tulin.command.format");
		}

		public void run() {
			try {
				formater.format(XMLEditor.this);
			} catch (ExecutionException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * The action to generate DTD from XML.
	 */
	private class GenerateDTDAction extends Action {
		public GenerateDTDAction() {
			super(WebToolsPlugin.getResourceString("XMLEditor.GenerateDTD"),
					WebToolsPlugin.getDefault().getImageRegistry().getDescriptor(WebToolsPlugin.ICON_DTD));
		}

		@Override
		public void run() {
			FileDialog dialog = new FileDialog(getViewer().getTextWidget().getShell(), SWT.SAVE);
			dialog.setFilterExtensions(new String[] { "*.dtd" });
			String file = dialog.open();
			if (file != null) {
				try {
					SchemaGenerator.generateDTDFromXML(getFile(), new File(file));
				} catch (Exception ex) {
					WebToolsPlugin.openAlertDialog(ex.toString());
				}
			}
		}
	}

	/**
	 * The action to generate XML schema from XML.
	 */
	private class GenerateXSDAction extends Action {
		public GenerateXSDAction() {
			super(WebToolsPlugin.getResourceString("XMLEditor.GenerateXSD"),
					WebToolsPlugin.getDefault().getImageRegistry().getDescriptor(WebToolsPlugin.ICON_XSD));
		}

		@Override
		public void run() {
			FileDialog dialog = new FileDialog(getViewer().getTextWidget().getShell(), SWT.SAVE);
			dialog.setFilterExtensions(new String[] { "*.xsd" });
			String file = dialog.open();
			if (file != null) {
				try {
					SchemaGenerator.generateXSDFromXML(getFile(), new File(file));
				} catch (Exception ex) {
					WebToolsPlugin.openAlertDialog(ex.toString());
				}
			}
		}
	}

}
