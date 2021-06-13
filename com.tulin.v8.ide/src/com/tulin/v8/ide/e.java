package com.tulin.v8.ide;

class e implements Runnable {
	e(StudioStartup paramStudioStartup) {
	}

	public void run() {
		try {
			StudioJsLibraryInit.config(false);// 初始化JSDoc
			ProjectManager.getInstance().build();
//			TomcatConfigInit.config(false);
		} catch (Exception e) {
			Sys.packErrMsg(e.toString());
			e.printStackTrace();
		}
	}
}
