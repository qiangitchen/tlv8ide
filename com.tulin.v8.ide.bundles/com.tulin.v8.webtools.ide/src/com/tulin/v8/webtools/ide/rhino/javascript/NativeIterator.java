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
 * Contributor(s):
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

import java.util.Iterator;

/**
 * This class implements iterator objects. See 
 * http://developer.mozilla.org/en/docs/New_in_JavaScript_1.7#Iterators
 *
 */
public final class NativeIterator extends IdScriptableObject {
    private static final long serialVersionUID = -4136968203581667681L;
    private static final Object ITERATOR_TAG = "Iterator";
    
    static void init(ScriptableObject scope, boolean sealed) {
        // Iterator
        NativeIterator iterator = new NativeIterator();
        iterator.exportAsJSClass(MAX_PROTOTYPE_ID, scope, sealed);

        // Generator
        NativeGenerator.init(scope, sealed);

        // StopIteration
        NativeObject obj = new StopIteration();
        obj.setPrototype(getObjectPrototype(scope));
        obj.setParentScope(scope);
        if (sealed) { obj.sealObject(); }
        ScriptableObject.defineProperty(scope, STOP_ITERATION, obj,
                                        ScriptableObject.DONTENUM);
        // Use "associateValue" so that generators can continue to
        // throw StopIteration even if the property of the global
        // scope is replaced or deleted.
        scope.associateValue(ITERATOR_TAG, obj);
    }
    
    /**
     * Only for constructing the prototype object.
     */
    private NativeIterator() {
    }
    
    private NativeIterator(Object objectIterator) {
      this.objectIterator = objectIterator;
    }

    /**
     * Get the value of the "StopIteration" object. Note that this value
     * is stored in the top-level scope using "associateValue" so the
     * value can still be found even if a script overwrites or deletes
     * the global "StopIteration" property.
     * @param scope a scope whose parent chain reaches a top-level scope
     * @return the StopIteration object
     */
    public static Object getStopIterationObject(Scriptable scope) {
        Scriptable top = ScriptableObject.getTopLevelScope(scope);
        return ScriptableObject.getTopScopeValue(top, ITERATOR_TAG);
    }
    
    private static final String STOP_ITERATION = "StopIteration";
    public static final String ITERATOR_PROPERTY_NAME = "__iterator__";
    
    static class StopIteration extends NativeObject {
        private static final long serialVersionUID = 2485151085722377663L;

        @Override
        public String getClassName() {
            return STOP_ITERATION;
        }

        /* StopIteration has custom instanceof behavior since it
         * doesn't have a constructor.
         */
        @Override
        public boolean hasInstance(Scriptable instance) {
            return instance instanceof StopIteration;
        }
    }

    @Override
    public String getClassName() {
        return "Iterator";
    }

    @Override
    protected void initPrototypeId(int id) {
        String s;
        int arity;
        switch (id) {
          case Id_constructor:    arity=2; s="constructor";          break;
          case Id_next:           arity=0; s="next";                 break;
          case Id___iterator__:   arity=1; s=ITERATOR_PROPERTY_NAME; break;
          default: throw new IllegalArgumentException(String.valueOf(id));
        }
        initPrototypeMethod(ITERATOR_TAG, id, s, arity);
    }

    @Override
    public Object execIdCall(IdFunctionObject f, Context cx, Scriptable scope,
                             Scriptable thisObj, Object[] args)
    {
        if (!f.hasTag(ITERATOR_TAG)) {
            return super.execIdCall(f, cx, scope, thisObj, args);
        }
        int id = f.methodId();
        
        if (id == Id_constructor) {
            return jsConstructor(cx, scope, thisObj, args);
        }

        if (!(thisObj instanceof NativeIterator))
            throw incompatibleCallError(f);
        
        NativeIterator iterator = (NativeIterator) thisObj;
        
        switch (id) {

          case Id_next: 
            return iterator.next(cx, scope);

          case Id___iterator__:
            /// XXX: what about argument? SpiderMonkey apparently ignores it
            return thisObj;

          default: 
            throw new IllegalArgumentException(String.valueOf(id));
        }
    }
    
    /* The JavaScript constructor */
    private static Object jsConstructor(Context cx, Scriptable scope, 
                                        Scriptable thisObj, Object[] args)
    {
        if (args.length == 0 || args[0] == null || 
            args[0] == Undefined.instance)
        {
            throw ScriptRuntime.typeError1("msg.no.properties", 
                                           ScriptRuntime.toString(args[0]));
        }
        Scriptable obj = ScriptRuntime.toObject(scope, args[0]);
        boolean keyOnly = args.length > 1 && ScriptRuntime.toBoolean(args[1]);
        if (thisObj != null) {
            // Called as a function. Convert to iterator if possible.
          
            // For objects that implement java.lang.Iterable or
            // java.util.Iterator, have JavaScript Iterator call the underlying
            // iteration methods
            Iterator<?> iterator = 
                VMBridge.instance.getJavaIterator(cx, scope, obj);
            if (iterator != null) {
                scope = ScriptableObject.getTopLevelScope(scope);
                return cx.getWrapFactory().wrap(cx, scope,
                        new WrappedJavaIterator(iterator, scope),
                        WrappedJavaIterator.class);
            }
            
            // Otherwise, just call the runtime routine
            Scriptable jsIterator = ScriptRuntime.toIterator(cx, scope, obj, 
                                                             keyOnly);
            if (jsIterator != null) {
                return jsIterator;
            }
        }
        
        // Otherwise, just set up to iterate over the properties of the object.
        // Do not call __iterator__ method.
        Object objectIterator = ScriptRuntime.enumInit(obj, cx,
            keyOnly ? ScriptRuntime.ENUMERATE_KEYS_NO_ITERATOR
                    : ScriptRuntime.ENUMERATE_ARRAY_NO_ITERATOR);
        ScriptRuntime.setEnumNumbers(objectIterator, true);
        NativeIterator result = new NativeIterator(objectIterator);
        result.setPrototype(NativeIterator.getClassPrototype(scope, 
                                result.getClassName()));
        result.setParentScope(scope);
        return result;
    }
    
    private Object next(Context cx, Scriptable scope) {
        Boolean b = ScriptRuntime.enumNext(this.objectIterator);
        if (!b.booleanValue()) {
            // Out of values. Throw StopIteration.
            throw new JavaScriptException(
                NativeIterator.getStopIterationObject(scope), null, 0);
        }
        return ScriptRuntime.enumId(this.objectIterator, cx);
    }
    
    static public class WrappedJavaIterator
    {
        WrappedJavaIterator(Iterator<?> iterator, Scriptable scope) {
            this.iterator = iterator;
            this.scope = scope;
        }

        public Object next() {
            if (!iterator.hasNext()) {
                // Out of values. Throw StopIteration.
                throw new JavaScriptException(
                    NativeIterator.getStopIterationObject(scope), null, 0);
            }
            return iterator.next();
        }

        public Object __iterator__(boolean b) {
            return this;
        }

        private Iterator<?> iterator;
        private Scriptable scope;
    }
    
// #string_id_map#

    @Override
    protected int findPrototypeId(String s) {
        int id;
// #generated# Last update: 2007-06-11 09:43:19 EDT
        L0: { id = 0; String X = null;
            int s_length = s.length();
            if (s_length==4) { X="next";id=Id_next; }
            else if (s_length==11) { X="constructor";id=Id_constructor; }
            else if (s_length==12) { X="__iterator__";id=Id___iterator__; }
            if (X!=null && X!=s && !X.equals(s)) id = 0;
            break L0;
        }
// #/generated#
        return id;
    }

    private static final int
        Id_constructor           = 1,
        Id_next                  = 2,
        Id___iterator__          = 3,
        MAX_PROTOTYPE_ID         = 3;

// #/string_id_map#

    private Object objectIterator;
}

