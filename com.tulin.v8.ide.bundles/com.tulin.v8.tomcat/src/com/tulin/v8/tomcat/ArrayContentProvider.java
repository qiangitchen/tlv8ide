/************************************************************************
Copyright (c) 2003 IBM Corporation and others.
All rights reserved.   This program and the accompanying materials
are made available under the terms of the Common Public License v1.0
which accompanies this distribution, and is available at
http://www.eclipse.org/legal/cpl-v10.html

Contributors:
    IBM - Initial implementation
************************************************************************/

package com.tulin.v8.tomcat;

import java.util.Collection;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

/**
 * This implementation of <code>IStructuredContentProvider</code> handles
 * the case where the input is an unchanging array or collection of elements.
 */
public class ArrayContentProvider implements IStructuredContentProvider {
	
	/**
	 * Returns the elements in the input, which must be either an array or a
	 * <code>Collection</code>. 
	 */
	@SuppressWarnings("rawtypes")
	public Object[] getElements(Object inputElement) {
		if (inputElement instanceof Object[])
			return (Object[]) inputElement;
		if (inputElement instanceof Collection)
			return ((Collection) inputElement).toArray();
		return new Object[0];
	}
	
	/**
	 * This implementation does nothing.
	 */
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
	}

	/**
	 * This implementation does nothing.
	 */
	public void dispose() {
	}
}