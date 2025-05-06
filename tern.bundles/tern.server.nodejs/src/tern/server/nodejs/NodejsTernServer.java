package tern.server.nodejs;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.websocket.ClientEndpoint;
import javax.websocket.OnMessage;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;

import tern.ITernProject;
import tern.TernException;
import tern.TernResourcesManager;
import tern.server.AbstractTernServer;
import tern.server.IInterceptor;
import tern.server.IResponseHandler;
import tern.server.TernPlugin;
import tern.server.WebSocketContainerProvider;
import tern.server.nodejs.process.INodejsLaunchConfiguration;
import tern.server.nodejs.process.INodejsProcess;
import tern.server.nodejs.process.INodejsProcessListener;
import tern.server.nodejs.process.NodejsProcessAdapter;
import tern.server.nodejs.process.NodejsProcessException;
import tern.server.nodejs.process.NodejsProcessManager;
import tern.server.nodejs.process.internal.NodejsProcess;
import tern.server.protocol.IJSONObjectHelper;
import tern.server.protocol.MinimalJSONHelper;
import tern.server.protocol.TernDoc;
import tern.server.protocol.html.ScriptTagRegion;

/**
 * Tern server implemented with node.js
 * 
 */
public class NodejsTernServer extends AbstractTernServer implements INodejsLaunchConfiguration {

	private static final String BASE_URL = "http://127.0.0.1:";
	private static final String HTTP_PROTOCOL = "http:";
	private static final String WS_PROTOCOL = "ws:";

	private String baseURL;

	private List<IInterceptor> interceptors;

	private INodejsProcess process;
	private List<INodejsProcessListener> listeners;

	private long timeout = NodejsTernHelper.DEFAULT_TIMEOUT;

	private int testNumber = NodejsTernHelper.DEFAULT_TEST_NUMBER;

	/**
	 * Port of the node.js server.
	 */
	private Integer port;

	/**
	 * true if tern server must be verbose and false otherwise.
	 */
	private boolean verbose;

	/**
	 * true if tern server server don't write a .tern-port file and false
	 * otherwise.
	 */
	private boolean noPortFile = true;

	/**
	 * false if the server will shut itself down after five minutes of
	 * inactivity and true otherwise.
	 */
	private boolean persistent;

	/**
	 * true if tern plugins can be loaded from the project root and false
	 * otherwise
	 */
	private boolean loadingLocalPlugins;

	private boolean isDebugLaunch;

	private boolean isSaveLaunch;

	private final INodejsProcessListener listener = new NodejsProcessAdapter() {

		@Override
		public void onStart(INodejsProcess server) {
			NodejsTernServer.this.fireStartServer();
			// initialize WebSocket client session if "push" tern plugin is
			// declared in the tern-project.
			initializeWebSocketIfNeeded();
		}

		private void initializeWebSocketIfNeeded() {
			ITernProject project = getProject();
			if (project == null) {
				return;
			}
			if (!project.hasPlugin(TernPlugin.push)) {
				return;
			}
			try {
				String baseURL = getBaseURL();
				URI uri = URI.create(baseURL.replace(HTTP_PROTOCOL, WS_PROTOCOL));
				WebSocketContainer container = WebSocketContainerProvider.getWebSocketContainer();
				session = container.connectToServer(new WebSocketMessageDispatcher(), uri);
			} catch (Throwable e) {
				NodejsTernServer.this.onError("Error while initializing WebSocket client.", e);
			}
		}

		@Override
		public void onStop(INodejsProcess server) {
			dispose();
			fireEndServer();
		}

	};

	@ClientEndpoint
	public class WebSocketMessageDispatcher {

		@OnMessage
		public void dispatchMessage(String data) {
			JsonObject value = Json.parse(data).asObject();
			String type = value.getString("type", null);
			JsonValue json = value.get("data");
			if (type != null && json != null) {
				NodejsTernServer.this.fireOnMessage(type, json);
			}
		}
	}

	// WebSocket session
	private Session session;

	public NodejsTernServer(File projectDir, int port) {
		this(TernResourcesManager.getTernProject(projectDir), port);
	}

	public NodejsTernServer(ITernProject project, int port) {
		super(project);
		this.baseURL = computeBaseURL(port);
	}

	public NodejsTernServer(ITernProject project) throws TernException {
		this(project, NodejsProcessManager.getInstance().create(project.getProjectDir()));
	}

	public NodejsTernServer(ITernProject project, File nodejsBaseDir) throws TernException {
		this(project, NodejsProcessManager.getInstance().create(project.getProjectDir(), nodejsBaseDir));
	}

	public NodejsTernServer(ITernProject project, File nodejsBaseDir, File nodejsTernBaseDir) throws TernException {
		this(project,
				NodejsProcessManager.getInstance().create(project.getProjectDir(), nodejsBaseDir, nodejsTernBaseDir));
	}

	public NodejsTernServer(ITernProject project, INodejsProcess process) {
		super(project);
		this.process = process;
		process.setLaunchConfiguration(this);
		process.addProcessListener(listener);
	}

	private String computeBaseURL(Integer port) {
		return new StringBuilder(BASE_URL).append(port).append("/").toString();
	}

	@Override
	public void addFile(String name, String text, ScriptTagRegion[] tags) {
		TernDoc t = new TernDoc();
		t.addFile(name, text, tags, null);
		try {
			makeRequest(t);
		} catch (Exception e) {
			onError("Error while adding file.", e);
		}
	}

	@Override
	public void request(TernDoc doc, IResponseHandler handler) {
		try {
			JsonObject json = makeRequest(doc);
			handler.onSuccess(json, handler.isDataAsJsonString() ? json.toString() : null);
		} catch (Exception e) {
			handler.onError(e.getMessage(), e);
		}
	}

	private JsonObject makeRequest(TernDoc doc) throws IOException, InterruptedException, TernException {
		String baseURL = null;
		try {
			baseURL = getBaseURL();
			if (baseURL == null) {
				throw new TernException("Server has been disposed");
			}
		} catch (NodejsProcessException e) {
			// the nodejs process cannot start => not a valid node path, dispose
			// the server.
			dispose();
			throw e;
		}

		List<IInterceptor> interceptors;

		beginReadState();
		try {
			if (this.interceptors != null) {
				interceptors = new ArrayList<IInterceptor>(this.interceptors);
			} else {
				interceptors = null;
			}
		} finally {
			endReadState();
		}

		JsonObject json = NodejsTernHelper.makeRequest(baseURL, doc, false, interceptors, this);
		return json;
	}

	public void addInterceptor(IInterceptor interceptor) {
		beginWriteState();
		try {
			if (interceptors == null) {
				interceptors = new ArrayList<IInterceptor>();
			}
			interceptors.add(interceptor);
		} finally {
			endWriteState();
		}
	}

	public void removeInterceptor(IInterceptor interceptor) {
		beginWriteState();
		try {
			if (interceptors != null) {
				interceptors.remove(interceptor);
			}
		} finally {
			endWriteState();
		}
	}

	public String getBaseURL() throws InterruptedException, TernException {
		beginReadState();
		try {
			if (baseURL == null) {
				endReadState();
				beginWriteState();
				try {
					if (baseURL != null || isDisposed()) {// already initialized
															// or disposed
						return baseURL;
					}
					int port = getProcess().start(timeout, testNumber);
					this.baseURL = computeBaseURL(port);
				} finally {
					endWriteState();
					beginReadState();
				}
			}
			return baseURL;
		} finally {
			endReadState();
		}
	}

	private INodejsProcess getProcess() throws TernException {
		if (process == null) {
			ITernProject project = super.getProject();
			process = NodejsProcessManager.getInstance().create(project.getProjectDir());
			process.addProcessListener(listener);
		}
		return process;
	}

	public void addProcessListener(INodejsProcessListener listener) {
		beginWriteState();
		try {
			if (listeners == null) {
				listeners = new ArrayList<INodejsProcessListener>();
			}
			listeners.add(listener);
			if (process != null) {
				process.addProcessListener(listener);
			}
		} finally {
			endWriteState();
		}
	}

	public void removeProcessListener(INodejsProcessListener listener) {
		beginWriteState();
		try {
			if (listeners != null && listener != null) {
				listeners.remove(listener);
			}
			if (process != null) {
				process.removeProcessListener(listener);
			}
		} finally {
			endWriteState();
		}
	}

	@Override
	public IJSONObjectHelper getJSONObjectHelper() {
		return MinimalJSONHelper.INSTANCE;
	}

	@Override
	public void doDispose() {
		beginWriteState();
		try {
			if (process != null) {
				process.kill();
			}
			this.baseURL = null;
			this.process = null;
			if (session != null) {
				try {
					session.close();
				} catch (Throwable e) {
					onError("Error while closing WebSocket client session.", e);
				}
			}
		} finally {
			endWriteState();
		}
	}

	/**
	 * Set the timeout to use when node.js starts to retrieve the node.js port
	 * in {@link NodejsProcess#start(long, int)} from the given project.
	 */
	public void setTimeout(long timeout) {
		this.timeout = timeout;
	}

	/**
	 * Returns the timeout to use when node.js starts to retrieve the node.js
	 * port in {@link NodejsProcess#start(long, int)} from the given project.
	 * 
	 * @return
	 */
	public long getTimeout() {
		return timeout;
	}

	public void setTestNumber(int testNumber) {
		this.testNumber = testNumber;
	}

	public int getTestNumber() {
		return testNumber;
	}

	/**
	 * Set the verbose.
	 * 
	 * @param verbose
	 */
	public void setVerbose(boolean verbose) {
		this.verbose = verbose;
	}

	/**
	 * Returns true if tern is verbose and false otherwise.
	 * 
	 * @return
	 */
	public boolean isVerbose() {
		return verbose;
	}

	/**
	 * Set true if tern server server won't write a .tern-port file and false
	 * otherwise.
	 * 
	 * @param noPortFile
	 */
	public void setNoPortFile(boolean noPortFile) {
		this.noPortFile = noPortFile;
	}

	/**
	 * return true if tern server server won't write a .tern-port file and false
	 * otherwise.
	 * 
	 * @return
	 */
	public boolean isNoPortFile() {
		return noPortFile;
	}

	/**
	 * Set false if the server will shut itself down after five minutes of
	 * inactivity and true otherwise.
	 * 
	 * @param persistent
	 */
	public void setPersistent(boolean persistent) {
		this.persistent = persistent;
	}

	/**
	 * Returns false if the server will shut itself down after five minutes of
	 * inactivity and true otherwise.
	 * 
	 * @return
	 */
	public boolean isPersistent() {
		return persistent;
	}

	/**
	 * Set true if tern plugins can be loaded from the project root and false
	 * otherwise.
	 * 
	 * @see https://github.com/marijnh/tern/pull/394
	 */
	public void setLoadingLocalPlugins(boolean loadingLocalPlugins) {
		this.loadingLocalPlugins = loadingLocalPlugins;
	}

	/**
	 * Returns true if tern plugins can be loaded from the project root and
	 * false otherwise.
	 * 
	 * @return true if tern plugins can be loaded from the project root and
	 *         false otherwise.
	 * @see https://github.com/marijnh/tern/pull/394
	 */
	public boolean isLoadingLocalPlugins() {
		return loadingLocalPlugins;
	}

	/**
	 * Return the node.js port and null if it is not a remote tern server.
	 * 
	 * @return
	 */
	public Integer getPort() {
		return port;
	}

	protected void onError(String message, Throwable e) {
		e.printStackTrace();
	}

	@Override
	public List<String> createNodeArgs() {
		List<String> args = new LinkedList<String>();
		Integer port = getPort();
		if (port != null) {
			args.add("--port");
			args.add(port.toString());
		}
		if (isVerbose()) {
			args.add("--verbose");
			args.add("1");
		}
		if (isNoPortFile()) {
			args.add("--no-port-file");
		}
		if (isPersistent()) {
			args.add("--persistent");
		}
		if (!isLoadingLocalPlugins()) {
			args.add("--disable-loading-local");
		}
		return args;
	}

	@Override
	public String generateLaunchConfigurationName() {
		return "tern.js for " + getProject().getProjectDir().getName();
	}

	@Override
	public String getLaunchMode() {
		return isDebugLaunch ? "debug" : "run";
	}

	public void setDebugLaunch(boolean isDebugLaunch) {
		this.isDebugLaunch = isDebugLaunch;
	}

	@Override
	public boolean isSaveLaunch() {
		return isSaveLaunch;
	}

	public void setSaveLaunch(boolean isSaveLaunch) {
		this.isSaveLaunch = isSaveLaunch;
	}

	@Override
	public boolean isWaitOnPort() {
		return true;
	}
}
