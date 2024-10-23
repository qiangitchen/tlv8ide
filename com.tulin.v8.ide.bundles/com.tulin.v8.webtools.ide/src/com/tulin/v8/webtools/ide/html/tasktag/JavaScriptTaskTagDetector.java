package com.tulin.v8.webtools.ide.html.tasktag;

import com.tulin.v8.webtools.ide.js.model.JavaScriptComment;
import com.tulin.v8.webtools.ide.js.model.JavaScriptModel;

/**
 * {@link ITaskTagDetector} implementation for JavaScript. This detector
 * supports following extensions:
 *
 * <ul>
 * <li>.js</li>
 * </ul>
 */
public class JavaScriptTaskTagDetector extends AbstractTaskTagDetector {

	public JavaScriptTaskTagDetector() {
		addSupportedExtension("js");
	}

	public void doDetect() throws Exception {
		JavaScriptModel model = new JavaScriptModel(null, this.contents);
		for (JavaScriptComment comment : model.getCommentList()) {
			detectTaskTag(comment.getText(), comment.getStart());
		}
	}
}
