/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.ui.contentassist;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.AssertionFailedException;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.BadPositionCategoryException;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.Position;
import org.eclipse.jface.text.TypedPosition;
import org.eclipse.jface.text.rules.FastPartitioner;
import org.eclipse.jface.text.templates.DocumentTemplateContext;
import org.eclipse.jface.text.templates.TemplateBuffer;
import org.eclipse.jface.text.templates.TemplateContext;
import org.eclipse.jface.text.templates.TemplateVariable;
import org.eclipse.text.edits.MalformedTreeException;
import org.eclipse.text.edits.MultiTextEdit;
import org.eclipse.text.edits.RangeMarker;
import org.eclipse.text.edits.ReplaceEdit;
import org.eclipse.text.edits.TextEdit;

import zigen.plugin.db.DbPlugin;
import zigen.plugin.db.preference.SQLEditorPreferencePage;
import zigen.plugin.db.ui.views.internal.SQLPartitionScanner;


/**
 * A template editor using the Java formatter to format a template buffer.
 */
public class SQLFormatter {

	private static final String COMMENT_START = "/*-"; //$NON-NLS-1$

	private static final String COMMENT_END = "*/"; //$NON-NLS-1$

	/** The line delimiter to use if code formatter is not used. */
	private final String fLineDelimiter;

	/** The initial indent level */
	private final int fInitialIndentLevel;

	/** The java partitioner */
	private boolean fUseCodeFormatter;

	/**
	 * Wraps a {@link TemplateBuffer} and tracks the variable offsets while changes to the buffer occur. Whitespace variables are also tracked.
	 */
	private static final class VariableTracker {

		private static final String CATEGORY = "__template_variables"; //$NON-NLS-1$

		private Document fDocument;

		private final TemplateBuffer fBuffer;

		private List fPositions;

		/**
		 * Creates a new tracker.
		 * 
		 * @param buffer
		 *            the buffer to track
		 * @throws MalformedTreeException
		 * @throws BadLocationException
		 */
		public VariableTracker(TemplateBuffer buffer) throws MalformedTreeException, BadLocationException {
			//Assert.isLegal(buffer != null);
			 if (!(buffer != null)) {
					throw new IllegalArgumentException("assertion failed; "); //$NON-NLS-1$
				}
			fBuffer = buffer;
			fDocument = new Document(fBuffer.getString());
			installJavaStuff(fDocument);
			fDocument.addPositionCategory(CATEGORY);
			fDocument.addPositionUpdater(new ExclusivePositionUpdater(CATEGORY));
			fPositions = createRangeMarkers(fBuffer.getVariables(), fDocument);
		}

		/**
		 * Installs a java partitioner with <code>document</code>.
		 * 
		 * @param document
		 *            the document
		 */
		private static void installJavaStuff(Document document) {
			String demiliter = DbPlugin.getDefault().getPreferenceStore().getString(SQLEditorPreferencePage.P_SQL_DEMILITER);
			String[] types = new String[] {SQLPartitionScanner.SQL_STRING, SQLPartitionScanner.SQL_COMMENT};
			FastPartitioner partitioner = new FastPartitioner(new SQLPartitionScanner(), types);
			partitioner.connect(document);
			// document.setDocumentPartitioner(IJavaPartitions.JAVA_PARTITIONING, partitioner);
			document.setDocumentPartitioner(partitioner);
		}

		/**
		 * Returns the document with the buffer contents. Whitespace variables are decorated with comments.
		 * 
		 * @return the buffer document
		 */
		public IDocument getDocument() {
			checkState();
			return fDocument;
		}

		private void checkState() {
			if (fDocument == null)
				throw new IllegalStateException();
		}

		/**
		 * Restores any decorated regions and updates the buffer's variable offsets.
		 * 
		 * @return the buffer.
		 * @throws MalformedTreeException
		 * @throws BadLocationException
		 */
		public TemplateBuffer updateBuffer() throws MalformedTreeException, BadLocationException {
			checkState();
			TemplateVariable[] variables = fBuffer.getVariables();
			try {
				removeRangeMarkers(fPositions, fDocument, variables);
			} catch (BadPositionCategoryException x) {
				//Assert.isTrue(false);
				throw new AssertionFailedException("Assertion failed: ");
			}
			fBuffer.setContent(fDocument.get(), variables);
			fDocument = null;

			return fBuffer;
		}

		private List createRangeMarkers(TemplateVariable[] variables, IDocument document) throws MalformedTreeException, BadLocationException {
			Map markerToOriginal = new HashMap();

			MultiTextEdit root = new MultiTextEdit(0, document.getLength());
			List edits = new ArrayList();
			boolean hasModifications = false;
			for (int i = 0; i != variables.length; i++) {
				final TemplateVariable variable = variables[i];
				int[] offsets = variable.getOffsets();

				String value = variable.getDefaultValue();
				if (isWhitespaceVariable(value)) {
					// replace whitespace positions with unformattable comments
					String placeholder = COMMENT_START + value + COMMENT_END;
					for (int j = 0; j != offsets.length; j++) {
						ReplaceEdit replace = new ReplaceEdit(offsets[j], value.length(), placeholder);
						root.addChild(replace);
						hasModifications = true;
						markerToOriginal.put(replace, value);
						edits.add(replace);
					}
				} else {
					for (int j = 0; j != offsets.length; j++) {
						RangeMarker marker = new RangeMarker(offsets[j], value.length());
						root.addChild(marker);
						edits.add(marker);
					}
				}
			}

			if (hasModifications) {
				// update the document and convert the replaces to markers
				root.apply(document, TextEdit.UPDATE_REGIONS);
			}

			List positions = new ArrayList();
			for (Iterator it = edits.iterator(); it.hasNext();) {
				TextEdit edit = (TextEdit) it.next();
				try {
					// abuse TypedPosition to piggy back the original contents of the position
					final TypedPosition pos = new TypedPosition(edit.getOffset(), edit.getLength(), (String) markerToOriginal.get(edit));
					document.addPosition(CATEGORY, pos);
					positions.add(pos);
				} catch (BadPositionCategoryException x) {
					//Assert.isTrue(false);
					throw new AssertionFailedException("Assertion failed: " );
				}
			}

			return positions;
		}

		private boolean isWhitespaceVariable(String value) {
			int length = value.length();
			return length == 0 || Character.isWhitespace(value.charAt(0)) || Character.isWhitespace(value.charAt(length - 1));
		}

		private void removeRangeMarkers(List positions, IDocument document, TemplateVariable[] variables) throws MalformedTreeException, BadLocationException,
				BadPositionCategoryException {

			// revert previous changes
			for (Iterator it = positions.iterator(); it.hasNext();) {
				TypedPosition position = (TypedPosition) it.next();
				// remove and re-add in order to not confuse ExclusivePositionUpdater
				document.removePosition(CATEGORY, position);
				final String original = position.getType();
				if (original != null) {
					document.replace(position.getOffset(), position.getLength(), original);
					position.setLength(original.length());
				}
				document.addPosition(position);
			}

			Iterator it = positions.iterator();
			for (int i = 0; i != variables.length; i++) {
				TemplateVariable variable = variables[i];

				int[] offsets = new int[variable.getOffsets().length];
				for (int j = 0; j != offsets.length; j++)
					offsets[j] = ((Position) it.next()).getOffset();

				variable.setOffsets(offsets);
			}

		}
	}

	/**
	 * Creates a JavaFormatter with the target line delimiter.
	 * 
	 * @param lineDelimiter
	 *            the line delimiter to use
	 * @param initialIndentLevel
	 *            the initial indentation level
	 * @param useCodeFormatter
	 *            <code>true</code> if the core code formatter should be used
	 * @param project
	 *            the java project from which to get the preferences, or <code>null</code> for workbench settings
	 */
	public SQLFormatter(String lineDelimiter, int initialIndentLevel, boolean useCodeFormatter) {
		fLineDelimiter = lineDelimiter;
		fUseCodeFormatter = useCodeFormatter;
		fInitialIndentLevel = initialIndentLevel;
	}

	/**
	 * Formats the template buffer.
	 * 
	 * @param buffer
	 * @param context
	 * @throws BadLocationException
	 */
	public void format(TemplateBuffer buffer, TemplateContext context) throws BadLocationException {
		try {
			VariableTracker tracker = new VariableTracker(buffer);
			IDocument document = tracker.getDocument();

			internalFormat(document, context);
			convertLineDelimiters(document);
			if (!isReplacedAreaEmpty(context))
				trimStart(document);

			tracker.updateBuffer();
		} catch (MalformedTreeException e) {
			throw new BadLocationException();
		}
	}

	/**
	 * @param document
	 * @param context
	 * @throws BadLocationException
	 */
	private void internalFormat(IDocument document, TemplateContext context) throws BadLocationException {
		if (fUseCodeFormatter) {
			try {
				// format(document, (CompilationUnitContext) context);
				format(document);
			} catch (BadLocationException e) {
				indent(document);
			} catch (MalformedTreeException e) {
				indent(document);
			}
		} else {
			indent(document);
		}
	}

	private void convertLineDelimiters(IDocument document) throws BadLocationException {
		int lines = document.getNumberOfLines();
		for (int line = 0; line < lines; line++) {
			IRegion region = document.getLineInformation(line);
			String lineDelimiter = document.getLineDelimiter(line);
			if (lineDelimiter != null)
				document.replace(region.getOffset() + region.getLength(), lineDelimiter.length(), fLineDelimiter);
		}
	}

	private void trimStart(IDocument document) throws BadLocationException {
		int i = 0;
		while ((i != document.getLength()) && Character.isWhitespace(document.getChar(i)))
			i++;

		document.replace(0, i, ""); //$NON-NLS-1$
	}

	private boolean isReplacedAreaEmpty(TemplateContext context) {
		if (context instanceof DocumentTemplateContext) {
			DocumentTemplateContext dtc = (DocumentTemplateContext) context;
			if (dtc.getStart() == dtc.getCompletionOffset())
				try {
					if (dtc.getDocument().get(dtc.getStart(), dtc.getEnd() - dtc.getStart()).trim().length() == 0)
						return true;
				} catch (BadLocationException x) {
					return true;
				}
		}
		return false;
	}

	// private void format(IDocument doc, CompilationUnitContext context) throws BadLocationException {
	private void format(IDocument doc) throws BadLocationException {
	// Map options;
	// if (context.getCompilationUnit() != null)
	// options= context.getCompilationUnit().getJavaProject().getOptions(true);
	// else
	// options= JavaCore.getOptions();
	//
	// String contents= doc.get();
	// int[] kinds= { CodeFormatter.K_EXPRESSION, CodeFormatter.K_STATEMENTS, CodeFormatter.K_UNKNOWN};
	// TextEdit edit= null;
	// for (int i= 0; i < kinds.length && edit == null; i++) {
	// edit= CodeFormatterUtil.format2(kinds[i], contents, fInitialIndentLevel, fLineDelimiter, options);
	// }
	//
	// if (edit == null)
	// throw new BadLocationException(); // fall back to indenting
	//
	// edit.apply(doc, TextEdit.UPDATE_REGIONS);
	}

	private void indent(IDocument document) throws BadLocationException, MalformedTreeException {
	/*
	 * // first line int offset= document.getLineOffset(0); document.replace(offset, 0, CodeFormatterUtil.createIndentString(fInitialIndentLevel, fProject));
	 *  // following lines int lineCount= document.getNumberOfLines(); IndentUtil.indentLines(document, new LineRange(1, lineCount - 1), fProject, null);
	 */
	}
}
