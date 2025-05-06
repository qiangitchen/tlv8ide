package tern.eclipse.ide.tools.internal.ui.console;

import tern.eclipse.ide.ui.console.LineType;

/**
 * Tern repository console document.
 *
 */
public class ConsoleDocument {

	private LineType[] lineTypes;

	private String[] lines;

	private int writeIndex = 0;

	private int readIndex = 0;

	private static final int BUFFER_SIZE = 200;

	protected static class ConsoleLine {
		private String line;

		private LineType type;

		ConsoleLine(String line, LineType type) {
			this.line = line;
			this.type = type;
		}

		public String getLine() {
			return line;
		}

		public LineType getType() {
			return type;
		}
	}

	/**
	 * Creates an empty console document.
	 */
	public ConsoleDocument() {
		// nothing to initialize
	}

	/**
	 * Clears the console document.
	 */
	public void clear() {
		lineTypes = null;
		lines = null;
		writeIndex = 0;
		readIndex = 0;
	}

	/**
	 * Appends a line of the specified type to the end of the console.
	 */
	public void appendConsoleLine(LineType lineType, String line) {
		if (lines == null) {
			lines = new String[BUFFER_SIZE];
			lineTypes = new LineType[BUFFER_SIZE];
		}
		lines[writeIndex] = line; //$NON-NLS-1$
		lineTypes[writeIndex] = lineType;

		if (++writeIndex >= BUFFER_SIZE) {
			writeIndex = 0;
		}
		if (writeIndex == readIndex) {
			if (++readIndex >= BUFFER_SIZE) {
				readIndex = 0;
			}
		}
	}

	public ConsoleLine[] getLines() {
		if (isEmpty()) {
			return new ConsoleLine[0];
		}
		ConsoleLine[] docLines = new ConsoleLine[readIndex > writeIndex ? BUFFER_SIZE
				: writeIndex];
		int index = readIndex;
		for (int i = 0; i < docLines.length; i++) {
			docLines[i] = new ConsoleLine(lines[index], lineTypes[index]);
			if (++index >= BUFFER_SIZE) {
				index = 0;
			}
		}
		return docLines;
	}

	public boolean isEmpty() {
		return writeIndex == readIndex;
	}
}
