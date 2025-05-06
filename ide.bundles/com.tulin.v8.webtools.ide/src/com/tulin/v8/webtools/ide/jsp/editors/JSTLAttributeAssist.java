package com.tulin.v8.webtools.ide.jsp.editors;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IFileEditorInput;

import com.tulin.v8.fuzzyxml.FuzzyXMLElement;
import com.tulin.v8.webtools.ide.WebToolsPlugin;
import com.tulin.v8.webtools.ide.assist.AssistInfo;
import com.tulin.v8.webtools.ide.assist.AttributeInfo;
import com.tulin.v8.webtools.ide.assist.IFileAssistProcessor;
import com.tulin.v8.webtools.ide.html.HTMLUtil;
import com.tulin.v8.webtools.ide.tag.ICustomTagAttributeAssist;

/**
 * An implementation of <code>ICustomTagAttributeAssist</code> for JSTL.
 */
public class JSTLAttributeAssist implements ICustomTagAttributeAssist {
	
	public static final String URL_JSTL_C = "http://java.sun.com/jsp/jstl/core";
	public static final String URL_JSTL_C_10 = "http://java.sun.com/jstl/core";
	public static final String URL_JSTL_C_10_RT = "http://java.sun.com/jstl/core_rt";
	
	public static final String URL_JSTL_FMT = "http://java.sun.com/jsp/jstl/fmt";
	public static final String URL_JSTL_FMT_10 = "http://java.sun.com/jstl/fmt";
	public static final String URL_JSTL_FMT_10_RT = "http://java.sun.com/jstl/fmt_rt";
	
	private IFileAssistProcessor[] fileAssistProcessors = WebToolsPlugin.getDefault().getFileAssistProcessors();
	
	public AssistInfo[] getAttributeValues(String tagName, String uri,
			String value, AttributeInfo attrInfo, FuzzyXMLElement element) {
		if(!isSupportedURI(uri)){
			return null;
		}
		String attribute = attrInfo.getAttributeName();
		
		if(tagName.equals("import") || tagName.equals("redirect")){
		    if(attribute.equals("url")){
    			IEditorInput input = HTMLUtil.getActiveEditor().getEditorInput();
    			if(input instanceof IFileEditorInput){
    			    List<AssistInfo> result = new ArrayList<AssistInfo>();
    			    for(IFileAssistProcessor processor: fileAssistProcessors){
    			        processor.reload(((IFileEditorInput) input).getFile());
    			        for(AssistInfo info: processor.getAssistInfo(value)){
    			            result.add(info);
    			        }
    			    }
    			    return result.toArray(new AssistInfo[result.size()]);
    			}
		    }
		}
        if(tagName.equals("out")){
            if(attribute.equals("escapeXml")){
                return new AssistInfo[]{
                        new AssistInfo("true"),
                        new AssistInfo("false"),
                };
            }
        }
		if(tagName.equals("formatDate") || tagName.equals("parseDate")){
			if(attribute.equals("type")){
				return new AssistInfo[]{
					new AssistInfo("date"),
					new AssistInfo("time"),
					new AssistInfo("both"),
				};
			}
			if(attribute.equals("dateStyle") || attribute.equals("timeStyle")){
				return new AssistInfo[]{
						new AssistInfo("short"),
						new AssistInfo("medium"),
						new AssistInfo("long"),
						new AssistInfo("full"),
					};
			}
		}
		if(tagName.equals("formatNumber") || tagName.equals("parseNumber")){
			if(attribute.equals("type")){
				return new AssistInfo[]{
					new AssistInfo("number"),
					new AssistInfo("currency"),
					new AssistInfo("percentage"),
				};
			}
            if(attribute.equals("integerOnly")){
                return new AssistInfo[]{
                    new AssistInfo("true"),
                    new AssistInfo("false"),
                };
            }
		}
		if(attribute.equals("scope")){
            return new AssistInfo[]{
                    new AssistInfo("page"),
                    new AssistInfo("request"),
                    new AssistInfo("session"),
                    new AssistInfo("application"),
            };
		}
		return null;
	}
	
	private static boolean isSupportedURI(String uri){
	    if(uri == null){
	        return false;
	    }
		if(uri.equals(URL_JSTL_C) || uri.equals(URL_JSTL_C_10) || uri.equals(URL_JSTL_C_10_RT) ||
				uri.equals(URL_JSTL_FMT) || uri.equals(URL_JSTL_FMT_10) || uri.equals(URL_JSTL_FMT_10_RT)){
			return true;
		}
		return false;
	}

}
