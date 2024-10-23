package com.tulin.v8.webtools.ide.jsp;

import com.tulin.v8.fuzzyxml.FuzzyXMLElement;
import com.tulin.v8.webtools.ide.assist.AssistInfo;

public interface IJSPELAssistProcessor {

	public AssistInfo[] getCompletionProposals(FuzzyXMLElement element, String expression);

}
