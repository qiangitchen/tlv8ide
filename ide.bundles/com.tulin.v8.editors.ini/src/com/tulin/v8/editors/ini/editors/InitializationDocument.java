package com.tulin.v8.editors.ini.editors;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.BadPositionCategoryException;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.ISynchronizable;
import org.eclipse.jface.text.Position;

import com.tulin.v8.editors.ini.Activator;

@SuppressWarnings("deprecation")
public class InitializationDocument extends Document implements ISynchronizable {
	private boolean saving;
	private Object fLockObject;

	public boolean isReading() {
		Activator a = Activator.getDefault();
		return (a != null) && (a.isReadingPropertiesDocument());
	}

	public boolean isSaving() {
		return this.saving;
	}

	public void setSaving(boolean saving) {
		this.saving = saving;
	}

	protected String get2() {
		String result = super.get();
		if ((isSaving()) && (result != null)) {
			result = InitializationFileContentHandler.getInstance().fromText(result);
		}
		return result;
	}

	protected void set2(String content, long modificationStamp) {
		if ((isReading()) && (content != null)) {
			content = InitializationFileContentHandler.getInstance().fromBinary(content);
		}
		super.set(content, modificationStamp);
	}

	public synchronized void setLockObject(Object lockObject) {
		this.fLockObject = lockObject;
	}

	public synchronized Object getLockObject() {
		return this.fLockObject;
	}

	public void startSequentialRewrite(boolean normalized) {
		Object lockObject = getLockObject();
		if (lockObject == null) {
			super.startSequentialRewrite(normalized);
			return;
		}
		synchronized (lockObject) {
			super.startSequentialRewrite(normalized);
		}
	}

	public void stopSequentialRewrite() {
		Object lockObject = getLockObject();
		if (lockObject == null) {
			super.stopSequentialRewrite();
			return;
		}
		synchronized (lockObject) {
			super.stopSequentialRewrite();
		}
	}

	public String get() {
		Object lockObject = getLockObject();
		if (lockObject == null) {
			return get2();
		}
		synchronized (lockObject) {
			return get2();
		}
	}

	public String get(int offset, int length) throws BadLocationException {
		Object lockObject = getLockObject();
		if (lockObject == null) {
			return super.get(offset, length);
		}
		synchronized (lockObject) {
			return super.get(offset, length);
		}
	}

	public char getChar(int offset) throws BadLocationException {
		Object lockObject = getLockObject();
		if (lockObject == null) {
			return super.getChar(offset);
		}
		synchronized (lockObject) {
			return super.getChar(offset);
		}
	}

	public long getModificationStamp() {
		Object lockObject = getLockObject();
		if (lockObject == null) {
			return super.getModificationStamp();
		}
		synchronized (lockObject) {
			return super.getModificationStamp();
		}
	}

	public void replace(int offset, int length, String text) throws BadLocationException {
		Object lockObject = getLockObject();
		if (lockObject == null) {
			super.replace(offset, length, text);
			return;
		}
		synchronized (lockObject) {
			super.replace(offset, length, text);
		}
	}

	public void replace(int offset, int length, String text, long modificationStamp) throws BadLocationException {
		Object lockObject = getLockObject();
		if (lockObject == null) {
			super.replace(offset, length, text, modificationStamp);
			return;
		}
		synchronized (lockObject) {
			super.replace(offset, length, text, modificationStamp);
		}
	}

	public void set(String text) {
		Object lockObject = getLockObject();
		if (lockObject == null) {
			super.set(text);
			return;
		}
		synchronized (lockObject) {
			super.set(text);
		}
	}

	public void set(String text, long modificationStamp) {
		Object lockObject = getLockObject();
		if (lockObject == null) {
			set2(text, modificationStamp);
			return;
		}
		synchronized (lockObject) {
			set2(text, modificationStamp);
		}
	}

	public void addPosition(String category, Position position)
			throws BadLocationException, BadPositionCategoryException {
		Object lockObject = getLockObject();
		if (lockObject == null) {
			super.addPosition(category, position);
			return;
		}
		synchronized (lockObject) {
			super.addPosition(category, position);
		}
	}

	public void removePosition(String category, Position position) throws BadPositionCategoryException {
		Object lockObject = getLockObject();
		if (lockObject == null) {
			super.removePosition(category, position);
			return;
		}
		synchronized (lockObject) {
			super.removePosition(category, position);
		}
	}

	public Position[] getPositions(String category) throws BadPositionCategoryException {
		Object lockObject = getLockObject();
		if (lockObject == null) {
			return super.getPositions(category);
		}
		synchronized (lockObject) {
			return super.getPositions(category);
		}
	}
}
