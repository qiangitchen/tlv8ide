package com.tulin.v8.ide.navigator.views;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class XMLManager {
	public static void save(File path, Object value) throws IOException {
		ClassLoader oldLoader = Thread.currentThread().getContextClassLoader();
		ClassLoader newLoader = XMLManager.class.getClassLoader();
		Thread.currentThread().setContextClassLoader(newLoader);

		XMLEncoder encoder = new XMLEncoder(new BufferedOutputStream(new FileOutputStream(path)));
		encoder.writeObject(value);
		encoder.close();

		Thread.currentThread().setContextClassLoader(oldLoader);

	}

	public static Object load(File path) throws Exception {
		Object obj = null;
		ClassLoader oldLoader = Thread.currentThread().getContextClassLoader();
		ClassLoader newLoader = XMLManager.class.getClassLoader();
		Thread.currentThread().setContextClassLoader(newLoader);

		XMLDecoder decoder = new XMLDecoder(new BufferedInputStream(new FileInputStream(path)));
		obj = decoder.readObject();

		decoder.close();
		Thread.currentThread().setContextClassLoader(oldLoader);

		return obj;
	}

	public static Object load(File path, int entityExpansionLimit) throws Exception {
		Object obj = null;
		try {
			System.setProperty("entityExpansionLimit", String.valueOf(entityExpansionLimit)); //$NON-NLS-1$
			obj = XMLManager.load(path);

		} catch (java.util.NoSuchElementException e) {
			path.delete();

		} catch (ArrayIndexOutOfBoundsException e) {
			obj = XMLManager.load(path, entityExpansionLimit * 10);

		} catch (Exception e) {
			throw e;
		}
		return obj;
	}
}
