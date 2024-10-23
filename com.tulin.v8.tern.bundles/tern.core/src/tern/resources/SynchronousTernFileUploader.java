package tern.resources;

import tern.ITernProject;
import tern.TernException;
import tern.server.IResponseHandler;
import tern.server.ITernServer;
import tern.server.protocol.TernDoc;

/**
 * Basic synchronous file uploader. *
 */
public class SynchronousTernFileUploader implements ITernFileUploader {

	private ITernProject project;

	public SynchronousTernFileUploader(ITernProject project) {
		this.project = project;
	}

	@Override
	public boolean cancel(String fileName) {
		return false;
	}

	@Override
	public void request(final TernDoc doc) {
		final ITernServer server = project.getTernServer();
		if (server != null && !server.isDisposed()) {
			server.request(doc, new IResponseHandler() {

				@Override
				public void onSuccess(Object data, String dataAsJsonString) {
				}

				@Override
				public void onError(String error, Throwable t) {
					project.handleException(new TernException(error, t));
					project.getFileSynchronizer().uploadFailed(doc);
				}

				@Override
				public boolean isDataAsJsonString() {
					return false;
				}
			});
		}

	}

	@Override
	public void join(long timeout) {
		// always up-to-date
	}
}
