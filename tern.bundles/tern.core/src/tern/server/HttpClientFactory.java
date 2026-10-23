package tern.server;

import java.util.concurrent.TimeUnit;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;

/**
 * Shared {@link CloseableHttpClient} factory for all tern HTTP calls.
 * 
 * A single pooled client is reused for every request (content assist, hover,
 * repository downloads, etc.) instead of creating and destroying a
 * {@code DefaultHttpClient} per request. Connection and socket timeouts are
 * configurable with system properties so that a hung node.js process can never
 * block the UI thread forever.
 * 
 */
public class HttpClientFactory {

	/**
	 * System property to override the connection timeout in ms.
	 */
	public static final String SYSPROP_CONNECT_TIMEOUT = "tern.http.connectTimeout";

	/**
	 * System property to override the socket (read) timeout in ms.
	 */
	public static final String SYSPROP_SOCKET_TIMEOUT = "tern.http.socketTimeout";

	/**
	 * System property to override the max total pooled connections.
	 */
	public static final String SYSPROP_MAX_TOTAL_CONNECTIONS = "tern.http.maxTotalConnections";

	/**
	 * System property to override the max connections per route. Each tern
	 * node.js process uses its own 127.0.0.1:port route.
	 */
	public static final String SYSPROP_MAX_PER_ROUTE = "tern.http.maxPerRoute";

	private static final int DEFAULT_CONNECT_TIMEOUT = 5 * 1000;
	private static final int DEFAULT_SOCKET_TIMEOUT = 30 * 1000;
	private static final int DEFAULT_MAX_TOTAL_CONNECTIONS = 10;
	private static final int DEFAULT_MAX_PER_ROUTE = 5;

	private static class Holder {
		static final CloseableHttpClient INSTANCE = createHttpClient();
	}

	private HttpClientFactory() {
	}

	/**
	 * Returns the shared pooled HTTP client. Thread-safe.
	 * 
	 * @return the shared HTTP client, never null.
	 */
	public static CloseableHttpClient getHttpClient() {
		return Holder.INSTANCE;
	}

	private static CloseableHttpClient createHttpClient() {
		PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
		connectionManager.setMaxTotal(getProperty(SYSPROP_MAX_TOTAL_CONNECTIONS, DEFAULT_MAX_TOTAL_CONNECTIONS));
		connectionManager.setDefaultMaxPerRoute(getProperty(SYSPROP_MAX_PER_ROUTE, DEFAULT_MAX_PER_ROUTE));

		RequestConfig requestConfig = RequestConfig.custom()
				.setConnectTimeout(getProperty(SYSPROP_CONNECT_TIMEOUT, DEFAULT_CONNECT_TIMEOUT))
				.setConnectionRequestTimeout(DEFAULT_CONNECT_TIMEOUT)
				.setSocketTimeout(getProperty(SYSPROP_SOCKET_TIMEOUT, DEFAULT_SOCKET_TIMEOUT)).build();

		return HttpClientBuilder.create().setConnectionManager(connectionManager).setDefaultRequestConfig(requestConfig)
				.evictExpiredConnections().evictIdleConnections(30, TimeUnit.SECONDS).build();
	}

	private static int getProperty(String key, int defaultValue) {
		int value = Integer.getInteger(key, defaultValue);
		return value > 0 ? value : defaultValue;
	}

}
