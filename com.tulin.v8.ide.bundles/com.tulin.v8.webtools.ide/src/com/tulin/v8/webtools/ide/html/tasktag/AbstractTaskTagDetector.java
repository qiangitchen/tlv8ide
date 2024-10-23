package com.tulin.v8.webtools.ide.html.tasktag;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;

import com.tulin.v8.webtools.ide.html.HTMLUtil;
import com.tulin.v8.webtools.ide.utils.IOUtil;

/**
 * The base class for the {@link com.tulin.v8.webtools.ide.tasktag.ITaskTagDetector}
 * implementations. This class provides the base implementation and some utility
 * methods for subclasses.
 */
public abstract class AbstractTaskTagDetector implements ITaskTagDetector {

	protected String contents;
	protected IFile file;
	protected TaskTag[] tags;
	private List<String> extensions = new ArrayList<String>();

	/**
	 * Adds supported file extensions.
	 * 
	 * @param ext the file extension (dot isn't required)
	 */
	protected void addSupportedExtension(String ext) {
		extensions.add(ext);
	}

	public boolean isSupported(IFile file) {
		String fileName = file.getName();
		for (int i = 0; i < extensions.size(); i++) {
			String ext = extensions.get(i);
			if (fileName.endsWith("." + ext)) {
				return true;
			}
		}
		return false;
	}

	public void detect(IFile file, TaskTag[] tags) throws Exception {
		this.file = file;
		this.tags = tags;

		this.contents = new String(IOUtil.readStream(file.getContents()), file.getCharset());

		this.contents = this.contents.replaceAll("\r\n", "\n");
		this.contents = this.contents.replaceAll("\r", "\n");

		doDetect();
	}

	/**
	 * Please implement this method in the subclass.
	 * 
	 * @throws Exception
	 */
	protected abstract void doDetect() throws Exception;

	protected void detectTaskTag(String value, int offset) {
		String[] lines = value.split("\n");
		for (int i = 0; i < lines.length; i++) {
			tags: for (int j = 0; j < tags.length; j++) {
				int index = lines[i].indexOf(tags[j].getTag());
				if (index >= 0) {
					HTMLUtil.addTaskMarker(file, IMarker.PRIORITY_NORMAL, getLineAtOffset(offset) + i,
							lines[i].substring(index));
					break tags;
				}
			}
		}
	}

	private int getLineAtOffset(int offset) {
		String text = this.contents.substring(0, offset);
		int line = 0;
		int index = 0;
		while ((index = text.indexOf('\n', index)) >= 0) {
			line++;
			index++;
		}
		return line + 1;
	}
}
