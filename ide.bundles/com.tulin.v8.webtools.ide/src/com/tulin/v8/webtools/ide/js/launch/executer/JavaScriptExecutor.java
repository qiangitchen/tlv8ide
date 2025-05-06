package com.tulin.v8.webtools.ide.js.launch.executer;

import java.io.File;
import java.io.FileReader;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;

public class JavaScriptExecutor {

	public static void main(String[] args) throws Exception {
		File file = new File(args[0]);

		Context cx = Context.enter();
		Scriptable scope = cx.initStandardObjects();

//		InputStream in = JavaScriptExecutor.class.getResourceAsStream("common.js");
//		cx.evaluateReader(scope, new InputStreamReader(in), "common.js", 1, null);

		try {
			if(args.length > 1){
				for(int i=1; i<args.length; i++){
					File includeFile = new File(args[i]);
					cx.evaluateReader(scope, new FileReader(includeFile), includeFile.getName(), 1, null);
				}
			}
			cx.evaluateReader(scope, new FileReader(file), file.getName(), 1, null);

		} finally {
			Context.exit();
		}

	}

}
