package tern.server;

public interface IResponseHandler {

	void onError(String error, Throwable t);

	void onSuccess(Object data, String dataAsJsonString);
	
	boolean isDataAsJsonString();
	
}
