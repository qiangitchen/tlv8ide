package tern.server;

import tern.TernException;

public class DefaultResponseHandler implements IResponseHandler {

	private String error;
	private Object data;
	private boolean dataAsJsonString;
	private Throwable t;
	private String json;

	public DefaultResponseHandler(boolean dataAsJsonString) {
		this.error = null;
		this.dataAsJsonString = dataAsJsonString;
	}

	@Override
	public void onError(String error, Throwable t) {
		this.error = error;
		this.t = t;
		if (t == null) {
			if (this.error != null) {
				this.t = TernExceptionFactory.create(error);
			}
		} else {
			if (this.error == null) {
				this.error = t.getMessage();
			}
		}
	}

	@Override
	public void onSuccess(Object data, String json) {
		this.data = data;
		this.json = json;
	}

	public Object getData() throws TernException {
		if (error != null || t != null) {
			if (t instanceof TernException) {
				throw (TernException) t;
			}
			throw new TernException(error, t);
		}
		return data;
	}

	public String getJsonString() throws TernException {
		if (error != null || t != null) {
			throw new TernException(error, t);
		}
		return json;
	}

	@Override
	public boolean isDataAsJsonString() {
		return dataAsJsonString;
	}
}
