package tern.server;

import com.eclipsesource.json.JsonObject;

import tern.server.protocol.TernDoc;

public interface IInterceptor {

	void handleRequest(TernDoc request, ITernServer server, String methodName);

	void handleResponse(JsonObject response, ITernServer server,
			String methodName, long ellapsedTime);

	void handleError(Throwable error, ITernServer server, String methodName,
			long ellapsedTime);
}
