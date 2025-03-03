/* -*- Mode: java; tab-width: 8; indent-tabs-mode: nil; c-basic-offset: 4 -*-
 *
 * ***** BEGIN LICENSE BLOCK *****
 * Version: MPL 1.1/GPL 2.0
 *
 * The contents of this file are subject to the Mozilla Public License Version
 * 1.1 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS" basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 * for the specific language governing rights and limitations under the
 * License.
 *
 * The Original Code is Rhino code, released
 * May 6, 1999.
 *
 * The Initial Developer of the Original Code is
 * Netscape Communications Corporation.
 * Portions created by the Initial Developer are Copyright (C) 1997-1999
 * the Initial Developer. All Rights Reserved.
 *
 * Contributor(s):
 *   Igor Bukanov, igor@fastmail.fm
 *   Norris Boyd
 *
 * Alternatively, the contents of this file may be used under the terms of
 * the GNU General Public License Version 2 or later (the "GPL"), in which
 * case the provisions of the GPL are applicable instead of those above. If
 * you wish to allow use of your version of this file only under the terms of
 * the GPL and not to allow others to use your version of this file under the
 * MPL, indicate your decision by deleting the provisions above and replacing
 * them with the notice and other provisions required by the GPL. If you do
 * not delete the provisions above, a recipient may use your version of this
 * file under either the MPL or the GPL.
 *
 * ***** END LICENSE BLOCK ***** */

package com.tulin.v8.webtools.ide.rhino.javascript;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Cache of generated classes and data structures to access Java runtime from
 * JavaScript.
 */
public class ClassCache implements Serializable {
	private static final long serialVersionUID = -8866246036237312215L;
	private static final Object AKEY = "ClassCache";
	private volatile boolean cachingIsEnabled = true;
	private transient HashMap<Class<?>, JavaMembers> classTable;
	private transient HashMap<JavaAdapter.JavaAdapterSignature, Class<?>> classAdapterCache;
	private transient HashMap<Class<?>, Object> interfaceAdapterCache;
	private int generatedClassSerial;

	/**
	 * Search for ClassCache object in the given scope. The method first calls
	 * {@link ScriptableObject#getTopLevelScope(Scriptable scope)} to get the top
	 * most scope and then tries to locate associated ClassCache object in the
	 * prototype chain of the top scope.
	 *
	 * @param scope scope to search for ClassCache object.
	 * @return previously associated ClassCache object or a new instance of
	 *         ClassCache if no ClassCache object was found.
	 *
	 * @see #associate(ScriptableObject topScope)
	 */
	public static ClassCache get(Scriptable scope) {
		ClassCache cache = (ClassCache) ScriptableObject.getTopScopeValue(scope, AKEY);
		if (cache == null) {
			throw new RuntimeException("Can't find top level scope for " + "ClassCache.get");
		}
		return cache;
	}

	/**
	 * Associate ClassCache object with the given top-level scope. The ClassCache
	 * object can only be associated with the given scope once.
	 *
	 * @param topScope scope to associate this ClassCache object with.
	 * @return true if no previous ClassCache objects were embedded into the scope
	 *         and this ClassCache were successfully associated or false otherwise.
	 *
	 * @see #get(Scriptable scope)
	 */
	public boolean associate(ScriptableObject topScope) {
		if (topScope.getParentScope() != null) {
			// Can only associate cache with top level scope
			throw new IllegalArgumentException();
		}
		if (this == topScope.associateValue(AKEY, this)) {
			return true;
		}
		return false;
	}

	/**
	 * Empty caches of generated Java classes and Java reflection information.
	 */
	public synchronized void clearCaches() {
		classTable = null;
		classAdapterCache = null;
		interfaceAdapterCache = null;
	}

	/**
	 * Check if generated Java classes and Java reflection information is cached.
	 */
	public final boolean isCachingEnabled() {
		return cachingIsEnabled;
	}

	/**
	 * Set whether to cache some values.
	 * <p>
	 * By default, the engine will cache the results of <tt>Class.getMethods()</tt>
	 * and similar calls. This can speed execution dramatically, but increases the
	 * memory footprint. Also, with caching enabled, references may be held to
	 * objects past the lifetime of any real usage.
	 * <p>
	 * If caching is enabled and this method is called with a <code>false</code>
	 * argument, the caches will be emptied.
	 * <p>
	 * Caching is enabled by default.
	 *
	 * @param enabled if true, caching is enabled
	 *
	 * @see #clearCaches()
	 */
	public synchronized void setCachingEnabled(boolean enabled) {
		if (enabled == cachingIsEnabled)
			return;
		if (!enabled)
			clearCaches();
		cachingIsEnabled = enabled;
	}

	/**
	 * @return a map from classes to associated JavaMembers objects
	 */
	Map<Class<?>, JavaMembers> getClassCacheMap() {
		if (classTable == null) {
			classTable = new HashMap<Class<?>, JavaMembers>();
		}
		return classTable;
	}

	Map<JavaAdapter.JavaAdapterSignature, Class<?>> getInterfaceAdapterCacheMap() {
		if (classAdapterCache == null) {
			classAdapterCache = new HashMap<JavaAdapter.JavaAdapterSignature, Class<?>>();
		}
		return classAdapterCache;
	}

	/**
	 * @deprecated The method always returns false.
	 * @see #setInvokerOptimizationEnabled(boolean enabled)
	 */
	public boolean isInvokerOptimizationEnabled() {
		return false;
	}

	/**
	 * @deprecated The method does nothing. Invoker optimization is no longer used
	 *             by Rhino. On modern JDK like 1.4 or 1.5 the disadvantages of the
	 *             optimization like increased memory usage or longer initialization
	 *             time overweight small speed increase that can be gained using
	 *             generated proxy class to replace reflection.
	 */
	public synchronized void setInvokerOptimizationEnabled(boolean enabled) {
	}

	/**
	 * Internal engine method to return serial number for generated classes to
	 * ensure name uniqueness.
	 */
	public final synchronized int newClassSerialNumber() {
		return ++generatedClassSerial;
	}

	Object getInterfaceAdapter(Class<?> cl) {
		return interfaceAdapterCache == null ? null : interfaceAdapterCache.get(cl);
	}

	synchronized void cacheInterfaceAdapter(Class<?> cl, Object iadapter) {
		if (cachingIsEnabled) {
			if (interfaceAdapterCache == null) {
				interfaceAdapterCache = new HashMap<Class<?>, Object>();
			}
			interfaceAdapterCache.put(cl, iadapter);
		}
	}
}
