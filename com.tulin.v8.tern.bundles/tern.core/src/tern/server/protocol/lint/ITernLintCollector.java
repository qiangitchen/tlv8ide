package tern.server.protocol.lint;

import tern.server.protocol.IJSONObjectHelper;
import tern.server.protocol.ITernResultsCollector;

/**
 * Tern lint collector API.
 *
 */
public interface ITernLintCollector extends ITernResultsCollector {

	/**
	 * This method is call when lint start for the given file before calling the
	 * first {@link ITernLintCollector}
	 * {@link #addMessage(String, Long, Long, String, String)}.
	 * 
	 * @param file
	 */
	void startLint(String file);

	/**
	 * Add message.
	 * 
	 * @param messageId
	 *            the id of the message and null if the linter cannot support
	 *
	 * @param message
	 *            the description of the message.
	 * @param start
	 *            offset.
	 * @param end
	 *            offset.
	 * @param lineNumber
	 *            the line number and null if the linter cannot support this
	 *            feature.
	 * @param severity
	 *            the severity of the message.
	 * @param file
	 *            the owner file name.
	 * @param messageObject
	 *            JSON object of message.
	 * @param query 
	 * @param helper
	 *            the JSON Object helper to visit the given JSON message object.            
	 */
	void addMessage(String messageId, String message, Long start, Long end, Long lineNumber, String severity,
			String file, Object messageObject, TernLintQuery query, IJSONObjectHelper helper);

	/**
	 * This method is call when lint end for the given file after calling the
	 * last {@link ITernLintCollector}
	 * {@link #addMessage(String, Long, Long, String, String)}.
	 * 
	 * @param file
	 */
	void endLint(String file);

}
