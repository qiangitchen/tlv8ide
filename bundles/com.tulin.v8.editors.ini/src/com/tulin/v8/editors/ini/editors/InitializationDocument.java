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
		/* 27 */ Activator a = Activator.getDefault();
		/* 28 */ return (a != null) && (a.isReadingPropertiesDocument());
	}

	public boolean isSaving() {
		/* 33 */ return this.saving;
	}

	public void setSaving(boolean saving) {
		/* 38 */ this.saving = saving;
	}

	protected String get2() {
		/* 43 */ String result = super.get();
		/* 44 */ if ((isSaving()) && (result != null)) {
			/* 46 */ result = InitializationFileContentHandler.getInstance().fromText(result);
		}
		/* 48 */ return result;
	}

	protected void set2(String content, long modificationStamp) {
		/* 53 */ if ((isReading()) && (content != null)) {
			/* 55 */ content = InitializationFileContentHandler.getInstance().fromBinary(content);
		}
		/* 57 */ super.set(content, modificationStamp);
	}

	public synchronized void setLockObject(Object lockObject) {
		/* 68 */ this.fLockObject = lockObject;
	}

	public synchronized Object getLockObject() {
		/* 76 */ return this.fLockObject;
	}

	public void startSequentialRewrite(boolean normalized) {
		/* 84 */ Object lockObject = getLockObject();
		/* 85 */ if (lockObject == null) {
			/* 86 */ super.startSequentialRewrite(normalized);
			/* 87 */ return;
		}
		/* 89 */ synchronized (lockObject) {
			/* 90 */ super.startSequentialRewrite(normalized);
		}
	}

	public void stopSequentialRewrite() {
		/* 99 */ Object lockObject = getLockObject();
		/* 100 */ if (lockObject == null) {
			/* 101 */ super.stopSequentialRewrite();
			/* 102 */ return;
		}
		/* 104 */ synchronized (lockObject) {
			/* 105 */ super.stopSequentialRewrite();
		}
	}

	public String get() {
		/* 114 */ Object lockObject = getLockObject();
		/* 115 */ if (lockObject == null) {
			/* 116 */ return get2();
		}
		/* 118 */ synchronized (lockObject) {
			/* 119 */ return get2();
		}
	}

	public String get(int offset, int length) throws BadLocationException {
		/* 128 */ Object lockObject = getLockObject();
		/* 129 */ if (lockObject == null) {
			/* 130 */ return super.get(offset, length);
		}
		/* 132 */ synchronized (lockObject) {
			/* 133 */ return super.get(offset, length);
		}
	}

	public char getChar(int offset) throws BadLocationException {
		/* 142 */ Object lockObject = getLockObject();
		/* 143 */ if (lockObject == null) {
			/* 144 */ return super.getChar(offset);
		}
		/* 146 */ synchronized (lockObject) {
			/* 147 */ return super.getChar(offset);
		}
	}

	public long getModificationStamp() {
		/* 157 */ Object lockObject = getLockObject();
		/* 158 */ if (lockObject == null) {
			/* 159 */ return super.getModificationStamp();
		}
		/* 161 */ synchronized (lockObject) {
			/* 162 */ return super.getModificationStamp();
		}
	}

	public void replace(int offset, int length, String text) throws BadLocationException {
		/* 172 */ Object lockObject = getLockObject();
		/* 173 */ if (lockObject == null) {
			/* 174 */ super.replace(offset, length, text);
			/* 175 */ return;
		}
		/* 177 */ synchronized (lockObject) {
			/* 178 */ super.replace(offset, length, text);
		}
	}

	public void replace(int offset, int length, String text, long modificationStamp) throws BadLocationException {
		/* 188 */ Object lockObject = getLockObject();
		/* 189 */ if (lockObject == null) {
			/* 190 */ super.replace(offset, length, text, modificationStamp);
			/* 191 */ return;
		}
		/* 193 */ synchronized (lockObject) {
			/* 194 */ super.replace(offset, length, text, modificationStamp);
		}
	}

	public void set(String text) {
		/* 203 */ Object lockObject = getLockObject();
		/* 204 */ if (lockObject == null) {
			/* 205 */ super.set(text);
			/* 206 */ return;
		}
		/* 208 */ synchronized (lockObject) {
			/* 209 */ super.set(text);
		}
	}

	public void set(String text, long modificationStamp) {
		/* 218 */ Object lockObject = getLockObject();
		/* 219 */ if (lockObject == null) {
			/* 220 */ set2(text, modificationStamp);
			/* 221 */ return;
		}
		/* 223 */ synchronized (lockObject) {
			/* 224 */ set2(text, modificationStamp);
		}
	}

	public void addPosition(String category, Position position)
			throws BadLocationException, BadPositionCategoryException {
		/* 234 */ Object lockObject = getLockObject();
		/* 235 */ if (lockObject == null) {
			/* 236 */ super.addPosition(category, position);
			/* 237 */ return;
		}
		/* 239 */ synchronized (lockObject) {
			/* 240 */ super.addPosition(category, position);
		}
	}

	public void removePosition(String category, Position position) throws BadPositionCategoryException {
		/* 250 */ Object lockObject = getLockObject();
		/* 251 */ if (lockObject == null) {
			/* 252 */ super.removePosition(category, position);
			/* 253 */ return;
		}
		/* 255 */ synchronized (lockObject) {
			/* 256 */ super.removePosition(category, position);
		}
	}

	public Position[] getPositions(String category) throws BadPositionCategoryException {
		/* 266 */ Object lockObject = getLockObject();
		/* 267 */ if (lockObject == null) {
			/* 268 */ return super.getPositions(category);
		}
		/* 270 */ synchronized (lockObject) {
			/* 271 */ return super.getPositions(category);
		}
	}
}
