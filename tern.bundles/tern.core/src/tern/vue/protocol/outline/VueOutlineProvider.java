package tern.vue.protocol.outline;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import tern.ITernFile;
import tern.ITernProject;
import tern.TernException;
import tern.server.TernPlugin;
import tern.server.protocol.IJSONObjectHelper;
import tern.server.protocol.TernQuery;
import tern.server.protocol.outline.IJSNode;
import tern.server.protocol.outline.IJSNodeRoot;
import tern.server.protocol.outline.TernOutlineCollector;
import tern.server.protocol.outline.TernOutlineResultProcessor;
import tern.server.protocol.push.IMessageHandler;
import tern.vue.VueType;
import tern.vue.modules.Controller;
import tern.vue.modules.Directive;
import tern.vue.modules.DirectiveValue;
import tern.vue.modules.VueElement;

/**
 * Vue outline provider.
 *
 */
public class VueOutlineProvider extends TernOutlineCollector implements IMessageHandler {

	private final List<IVueOutlineListener> listeners;
	private VueOutline outline;

	public VueOutlineProvider(ITernProject ternProject) {
		super(ternProject);
		this.listeners = new ArrayList<IVueOutlineListener>();
		ternProject.on(VueOutline.vue_MODEL_CHANGED_EVENT, this);
	}

	@Override
	protected IJSNodeRoot doCreateRoot() {
		if (outline == null) {
			outline = new VueOutline(getTernProject());
		} else {
			outline.clear();
		}
		return outline;
	}

	public boolean init() throws IOException, TernException {
		if (outline == null) {
			outline = new VueOutline(getTernProject());
			loadOutline();
			fireOutlineChanged();
			return false;
		}
		return true;
	}

	@Override
	public IJSNode createNode(String name, String type, String kind, String value, Long start, Long end, String file,
			IJSNode parent, Object jsonNode, IJSONObjectHelper helper) {
		VueType vueType = VueType.get(kind);
		if (vueType != VueType.unknown) {
			switch (vueType) {
			case module:
				return new tern.vue.modules.Module(name, start, end, file, parent);
			case controller:
				return new Controller(name, null, start, end, file, parent);
			case directive:
				List<String> tagNames = new ArrayList<String>();
				String restrict = null; // helper.getText(completion,
										// "restrict");
				DirectiveValue directiveValue = DirectiveValue.none;
				return new Directive(name, VueType.model, null, tagNames, restrict, directiveValue, start, end,
						file, parent);
			default:
				return new VueElement(name, vueType, start, end, file, parent);
			}
		}
		return super.createNode(name, type, kind, value, start, end, file, parent, jsonNode, helper);
	}

	public VueOutline getOutline() throws IOException, TernException {
		/*
		 * if (init() && !getTernProject().hasPlugin(TernPlugin.push)) {
		 * loadOutline(); }
		 */
		return outline;
	}

	protected void fireOutlineChanged() {
		synchronized (listeners) {
			for (IVueOutlineListener listener : listeners) {
				listener.changed(outline);
			}
		}
	}

	protected void loadOutline() throws IOException, TernException {
		TernQuery query = new VueOutlineQuery();
		getTernProject().request(query, null, this);
	}

	public void refresh(ITernFile ternFile) throws IOException, TernException {
		ITernProject ternProject = getTernProject();
		if (ternProject.hasPlugin(TernPlugin.push)) {
			return;
		}
		TernQuery query = new VueOutlineQuery();
		query.setFile(ternFile.getFileName());
		ternProject.request(query, ternFile, this);
	}

	@Override
	public void handleMessage(Object jsonObject, IJSONObjectHelper helper) {
		if (outline == null) {
			outline = new VueOutline(getTernProject());
		}
		TernOutlineResultProcessor.INSTANCE.process(null, helper, jsonObject, this);
		fireOutlineChanged();
	}

	public void addVueOutlineListener(IVueOutlineListener listener) {
		synchronized (listeners) {
			if (!listeners.contains(listener)) {
				listeners.add(listener);
			}
		}
	}

	public void removeVueOutlineListener(IVueOutlineListener listener) {
		synchronized (listeners) {
			listeners.remove(listener);
		}
	}

	@Override
	public IJSNodeRoot getRoot() {
		try {
			return getOutline();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return super.getRoot();
	}

}
