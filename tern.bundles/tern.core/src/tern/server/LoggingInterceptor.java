package tern.server;

import com.eclipsesource.json.JsonObject;

import tern.server.protocol.TernDoc;

public class LoggingInterceptor implements IInterceptor {

	private static final IInterceptor INSTANCE = new LoggingInterceptor();

	public static IInterceptor getInstance() {
		return INSTANCE;
	}

	@Override
	public void handleRequest(TernDoc request, ITernServer server,
			String methodName) {
		outPrintln("-----------------------------------");
		outPrintln("Tern request#" + methodName + ": ");
		outPrintln(request.toString());
	}

	@Override
	public void handleResponse(JsonObject response, ITernServer server,
			String methodName, long ellapsedTime) {
		outPrintln("");
		outPrintln("Tern response#" + methodName + " with " + ellapsedTime
				+ "ms: ");
		outPrintln(response.toString());
		outPrintln("-----------------------------------");
	}

	@Override
	public void handleError(Throwable error, ITernServer server,
			String methodName, long ellapsedTime) {
		errPrintln("");
		errPrintln("Tern error#" + methodName + " with " + ellapsedTime
				+ "ms: ");
		printStackTrace(error);
		errPrintln("-----------------------------------");
	}

	protected void outPrintln(String line) {
		System.out.println(line);
	}

	protected void errPrintln(String line) {
		System.err.println(line);
	}

	protected void printStackTrace(Throwable error) {
		error.printStackTrace(System.err);
	}

	
	
}
