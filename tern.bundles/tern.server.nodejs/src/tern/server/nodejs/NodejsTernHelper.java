package tern.server.nodejs;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.ParseException;

import tern.TernException;
import tern.server.IInterceptor;
import tern.server.ITernServer;
import tern.server.TernExceptionFactory;
import tern.server.protocol.TernDoc;
import tern.server.protocol.TernQuery;
import tern.utils.IOUtils;
import tern.utils.StringUtils;

/**
 * Nodejs Tern helper.
 * 
 */
@SuppressWarnings("deprecation")
public class NodejsTernHelper {

	// properties for remote access
	public static final boolean DEFAULT_REMOTE_ACCESS = false;
	public static final int DEFAULT_REMOTE_PORT = 1234;

	// properties for direct access
	public static final long DEFAULT_TIMEOUT = 200L; // 200ms
	public static final int DEFAULT_TEST_NUMBER = 50; // try to retrieve the
														// node.js port 50
														// each time on timeout
														// (max=50*200ms=10000ms).

	public static final boolean DEFAULT_PERSISTENT = false;

	// tern uses UTF-8 encoding
	private static final String UTF_8 = "UTF-8";

	public NodejsTernHelper() {
	}

	@SuppressWarnings("resource")
	public static JsonObject makeRequest(String baseURL, TernDoc doc, 
			boolean silent, List<IInterceptor> interceptors, ITernServer server)
			throws IOException, TernException {
		TernQuery query = doc.getQuery();
		String methodName = query != null ? query.getLabel() : "";
		long startTime = 0;
		if (interceptors != null) {
			startTime = System.nanoTime();
			for (IInterceptor interceptor : interceptors) {
				interceptor.handleRequest(doc, server, methodName);
			}
		}
		HttpClient httpClient = new DefaultHttpClient();
		try {
			// Post JSON Tern doc
			HttpPost httpPost = createHttpPost(baseURL, doc);
			HttpResponse httpResponse = httpClient.execute(httpPost);
			HttpEntity entity = httpResponse.getEntity();
			InputStream in = entity.getContent();
			// Check the status
			StatusLine statusLine = httpResponse.getStatusLine();
			int statusCode = statusLine.getStatusCode();
			if (statusCode != HttpStatus.SC_OK) {
				// node.js server throws error
				String message = IOUtils.toString(in);
				if (StringUtils.isEmpty(message)) {
					throw new TernException(statusLine.toString());
				}
				throw TernExceptionFactory.create(message);
			}

			try {
				JsonObject response = (JsonObject) Json.parse(new InputStreamReader(in, UTF_8));
				if (interceptors != null) {
					for (IInterceptor interceptor : interceptors) {
						interceptor.handleResponse(response, server,
								methodName, getElapsedTimeInMs(startTime));
					}
				}
				return response;
			} catch (ParseException e) {
				throw new IOException(e);
			}
		} catch (Exception e) {
			if (interceptors != null) {
				for (IInterceptor interceptor : interceptors) {
					interceptor.handleError(e, server, methodName,
							getElapsedTimeInMs(startTime));
				}
			}
			if (e instanceof IOException) {
				throw (IOException) e;
			}
			if (e instanceof TernException) {
				throw (TernException) e;
			}
			throw new TernException(e);
		} finally {
			httpClient.getConnectionManager().shutdown();
		}
	}

	private static HttpPost createHttpPost(String baseURL, JsonObject doc)
			throws UnsupportedEncodingException {
		HttpPost httpPost = new HttpPost(baseURL);
		httpPost.setEntity(new StringEntity(doc.toString(), UTF_8));
		return httpPost;
	}

	public static long getElapsedTimeInMs(long startTime) {
		return ((System.nanoTime() - startTime) / 1000000L);
	}

}
