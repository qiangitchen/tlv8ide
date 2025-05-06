package com.tulin.v8.web.common;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.tulin.v8.web.enums.NSSystemProperty;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class ObjectRegistry {
	private static Thread cleanUpThread;
	private static Set<ObjectRegistry> registrySet = new HashSet();
	private static int nextThreadNumber;
	private int nextInstanceID = 1;
	private Map<Integer, WeakReference<Object>> instanceIDToObjectReferenceMap = new HashMap();

	private static ObjectRegistry registry = new ObjectRegistry();

	private static void startThread(ObjectRegistry objectRegistry) {
		synchronized (registrySet) {
			registrySet.add(objectRegistry);
			if (cleanUpThread != null) {
				return;
			}
			cleanUpThread = new Thread("Registry cleanup thread-" + nextThreadNumber++) {
				public void run() {
					while (true) {
						try {
							sleep(1000L);
						} catch (Exception localException) {
						}
						ObjectRegistry[] registries;
						synchronized (ObjectRegistry.registrySet) {
							registries = (ObjectRegistry[]) ObjectRegistry.registrySet.toArray(new ObjectRegistry[0]);
						}
						for (ObjectRegistry registry : registries) {
							synchronized (registry) {
								for (Integer instanceID : (Integer[]) registry.instanceIDToObjectReferenceMap.keySet()
										.toArray(new Integer[0])) {
									if (((WeakReference) registry.instanceIDToObjectReferenceMap.get(instanceID))
											.get() == null) {
										registry.instanceIDToObjectReferenceMap.remove(instanceID);
									}
								}
								if (registry.instanceIDToObjectReferenceMap.isEmpty()) {
									synchronized (ObjectRegistry.registrySet) {
										ObjectRegistry.registrySet.remove(registry);
									}
								}
							}
						}
						synchronized (ObjectRegistry.registrySet) {
							if (ObjectRegistry.registrySet.isEmpty()) {
								ObjectRegistry.cleanUpThread = null;
								return;
							}
						}
					}
				}
			};
			boolean isApplet = "applet".equals(NSSystemProperty.DEPLOYMENT_TYPE.get());
			cleanUpThread.setDaemon(!isApplet);
			cleanUpThread.start();
		}
	}

	public int add(Object o) {
		boolean isStartingThread = false;
		int instanceID;
		synchronized (this) {
			do
				instanceID = this.nextInstanceID++;
			while (this.instanceIDToObjectReferenceMap.containsKey(Integer.valueOf(instanceID)));
			if (o != null) {
				this.instanceIDToObjectReferenceMap.put(Integer.valueOf(instanceID), new WeakReference(o));
				isStartingThread = true;
			}
		}
		if (isStartingThread) {
			startThread(this);
		}
		return instanceID;
	}

	public void add(Object o, int instanceID) {
		synchronized (this) {
			Object o2 = get(instanceID);
			if ((o2 != null) && (o2 != o)) {
				throw new IllegalStateException(
						"An object is already registered with the id \"" + instanceID + "\" for object: " + o);
			}
			this.instanceIDToObjectReferenceMap.put(Integer.valueOf(instanceID), new WeakReference(o));
		}
		startThread(this);
	}

	public synchronized Object get(int instanceID) {
		WeakReference weakReference = (WeakReference) this.instanceIDToObjectReferenceMap
				.get(Integer.valueOf(instanceID));
		if (weakReference == null) {
			return null;
		}
		Object o = weakReference.get();
		if (o == null) {
			this.instanceIDToObjectReferenceMap.remove(Integer.valueOf(instanceID));
		}
		return o;
	}

	public synchronized void remove(int instanceID) {
		this.instanceIDToObjectReferenceMap.remove(Integer.valueOf(instanceID));
	}

	public synchronized int[] getInstanceIDs() {
		Object[] instanceIDObjects = this.instanceIDToObjectReferenceMap.keySet().toArray();
		int[] instanceIDs = new int[instanceIDObjects.length];
		for (int i = 0; i < instanceIDObjects.length; i++) {
			instanceIDs[i] = ((Integer) instanceIDObjects[i]).intValue();
		}
		return instanceIDs;
	}

	public static ObjectRegistry getInstance() {
		return registry;
	}
}
