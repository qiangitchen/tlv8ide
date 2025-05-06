package tern.resources;

import tern.server.protocol.TernDoc;

/**
 * Platform independent interface for handling upload of TernDocs to Tern
 * server. Upload can be done asynchronously and it can be cancelled by calling
 * cancel().
 * 
 * @author piotr
 */
public interface ITernFileUploader {

	/**
	 * Waits for a specified amount of time for the asynchronous upload to
	 * finish. If it doesn't finish in the specified time method just exists.
	 * 
	 * @param timeout
	 */
	void join(long timeout);

	/**
	 * Requests sending a specified TernDoc to the server. Request can be made
	 * asynchronously and in such a case this method exits immediately, or it
	 * can be synchronous in which case, the method exits only after
	 * successfully uploading the files to the server.
	 * 
	 * @param doc
	 */
	void request(TernDoc doc);

	/**
	 * Allows to cancel an upload of the file to the server, if uploader is
	 * asynchronous.
	 * 
	 * @param fileName
	 * @return
	 */
	boolean cancel(String fileName);

}
