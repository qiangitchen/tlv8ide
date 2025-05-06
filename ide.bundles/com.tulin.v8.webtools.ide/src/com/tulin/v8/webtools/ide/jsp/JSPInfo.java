package com.tulin.v8.webtools.ide.jsp;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;

import com.tulin.v8.fuzzyxml.FuzzyXMLAttribute;
import com.tulin.v8.fuzzyxml.FuzzyXMLDocument;
import com.tulin.v8.fuzzyxml.FuzzyXMLElement;
import com.tulin.v8.fuzzyxml.FuzzyXMLNode;
import com.tulin.v8.fuzzyxml.FuzzyXMLParser;
import com.tulin.v8.fuzzyxml.XPath;
import com.tulin.v8.webtools.ide.ProjectParams;
import com.tulin.v8.webtools.ide.WebToolsPlugin;
import com.tulin.v8.webtools.ide.html.HTMLUtil;
import com.tulin.v8.webtools.ide.utils.IOUtil;

/**
 * This class has informations about JSP like following:
 * <ul>
 * <li>information about taglib directive</li>
 * </ul>
 */
public class JSPInfo {

	private static Map<IFile, JSPInfoCache> jspInfoMap = new HashMap<IFile, JSPInfoCache>();

	private static class JSPInfoCache {
		private JSPInfo jspInfo;
		private long timestamp;
	}

	private List<TLDInfo> tldInfoList = new ArrayList<TLDInfo>();
	private FuzzyXMLDocument doc;
	private Set<IFile> includedFiles;

	// Regular expressions
//	private Pattern pagePattern    = Pattern.compile("<%@\\s*page\\s+(.+?)%>",Pattern.DOTALL);
	private Pattern taglibPattern = Pattern.compile("<%@\\s*taglib\\s+(.+?)%>", Pattern.DOTALL);
	private Pattern uriPattern = Pattern.compile("uri\\s*=\\s*\"(.+?)\"");
	private Pattern tagdirPattern = Pattern.compile("tagdir\\s*=\\s*\"(.+?)\"");
	private Pattern prefixPattern = Pattern.compile("prefix\\s*=\\s*\"(.+?)\"");
	private Pattern includePattern = Pattern.compile("<%@\\s*include\\s+(.+?)%>", Pattern.DOTALL);
	private Pattern filePattern = Pattern.compile("file\\s*=\\s*\"(.+?)\"");

	public static JSPInfo getJSPInfo(IFile file, String source) {
		JSPInfoCache cache = jspInfoMap.get(file);
		if (cache != null) {
			if (file.getLocalTimeStamp() == cache.timestamp) {
				return cache.jspInfo;
			}
		}
		JSPInfo info = new JSPInfo(file, source, true, new HashSet<IFile>());
		cache = new JSPInfoCache();
		cache.jspInfo = info;
		cache.timestamp = file.getLocalTimeStamp();
		jspInfoMap.put(file, cache);
		return info;
	}

	public FuzzyXMLDocument getDocument() {
		return this.doc;
	}

	/**
	 * The constructor.
	 *
	 * @param input         IFileEditorInput
	 * @param source        JSP
	 * @param include       use include configuration of web.xml
	 * @param includedFiles includes files
	 */
	private JSPInfo(IFile file, String source, boolean include, Set<IFile> includedFiles) {
		super();
		this.includedFiles = includedFiles;
		this.includedFiles.add(file);

		try {
			// load project preference
			ProjectParams params = new ProjectParams(file.getProject());
			String webapproot = params.getRoot();

			// processing for include direcive
			IContainer basedir = file.getProject();
			if (!webapproot.equals("") && !webapproot.equals("/")) {
				basedir = basedir.getFolder(new Path(webapproot));
			}

			Matcher matcher = includePattern.matcher(source);
			while (matcher.find()) {
				String content = matcher.group(1);
				String fileInc = getAttribute(content, filePattern);
				if (fileInc == null) {
					continue;
				}

				// Lecture du fichier inclus puis extraction de ses TLDs
				IFile incJspFile = null;
				if (fileInc.startsWith("/")) {
					incJspFile = basedir.getFile(new Path(fileInc));
				} else {
					incJspFile = file.getParent().getFile(new Path(fileInc));
				}
				try {
					if (incJspFile != null && incJspFile.exists() && !includedFiles.contains(incJspFile)) {
						String contents = new String(IOUtil.readStream(incJspFile.getContents()));
						JSPInfo info = new JSPInfo(incJspFile, contents, true, this.includedFiles);
						TLDInfo[] tldInfos = info.getTLDInfo();
						for (int i = 0; i < tldInfos.length; i++) {
							tldInfoList.add(tldInfos[i]);
						}
					}
				} catch (IOException ioe) {
					WebToolsPlugin.logException(ioe);
				} catch (CoreException ce) {
					WebToolsPlugin.logException(ce);
				}
			}

			// getting taglib directive
			matcher = taglibPattern.matcher(source);
			while (matcher.find()) {
				// parsing taglib directive
				String content = matcher.group(1);
				String tagdir = getAttribute(content, tagdirPattern);
				if (tagdir != null) {
					String prefix = getAttribute(content, prefixPattern);
					TLDInfo info = TLDInfo.getTLDInfoFromTagdir(file, prefix, tagdir);
					if (info != null) {
						tldInfoList.add(info);
					}
				} else {
					String uri = getAttribute(content, uriPattern);
					String prefix = getAttribute(content, prefixPattern);
					// creation TLDInfo
//					taglibInsertIndex = matcher.end();
					TLDInfo info = TLDInfo.getTLDInfo(file, prefix, uri);
					if (info != null) {
						tldInfoList.add(info);
					}
				}
			}

			// getting TLDs from xmlns
			try {
				this.doc = new FuzzyXMLParser().parse(HTMLUtil.scriptlet2space(source, false));
				FuzzyXMLElement root = (FuzzyXMLElement) XPath.selectSingleNode(doc.getDocumentElement(), "*");
				if (root != null) {
					FuzzyXMLAttribute[] attrs = root.getAttributes();
					for (int i = 0; i < attrs.length; i++) {
						if (attrs[i].getName().startsWith("xmlns:")) {
							String[] dim = attrs[i].getName().split(":");
							if (dim.length > 1) {
								TLDInfo info = null;
								String value = attrs[i].getValue();
								if (value.startsWith("urn:jsptagdir:")) {
									value = value.replaceFirst("^urn:jsptagdir:", "");
									info = TLDInfo.getTLDInfoFromTagdir(file, dim[1], value);

								} else if (value.startsWith("urn:jsptld:")) {
									value = value.replaceFirst("^urn:jsptld:", "");
									info = TLDInfo.getTLDInfo(file, dim[1], value);

								} else {
									info = TLDInfo.getTLDInfo(file, dim[1], value);
								}
								if (info != null) {
									tldInfoList.add(info);
								}
							}
						}
					}
				}
			} catch (Exception ex) {
				WebToolsPlugin.logException(ex);
			}

			// getting TLDs from included JSP defined in web.xml
			try {
				if (include) {
					IPath path = new Path(webapproot).append("/WEB-INF/web.xml");
					IFile webXML = file.getProject().getFile(path);
					if (webXML != null && webXML.exists()) {
						FuzzyXMLDocument doc = new FuzzyXMLParser().parse(webXML.getContents());
						FuzzyXMLNode[] nodes = HTMLUtil.selectXPathNodes(doc.getDocumentElement(),
								"/web-app/jsp-config/jsp-property-group[url-pattern='*.jsp']");
						for (int i = 0; i < nodes.length; i++) {
							FuzzyXMLNode[] includes = HTMLUtil.selectXPathNodes((FuzzyXMLElement) nodes[i],
									"/include-prelude|/include-coda");
							for (int j = 0; j < includes.length; j++) {
								IFile incFile = basedir.getFile(new Path(((FuzzyXMLElement) includes[j]).getValue()));
								if (incFile != null && incFile.exists()) {
									String contents = new String(IOUtil.readStream(incFile.getContents()));
									JSPInfo info = new JSPInfo(incFile, contents, false, this.includedFiles);
									TLDInfo[] tldInfos = info.getTLDInfo();
									for (int k = 0; k < tldInfos.length; k++) {
										tldInfoList.add(tldInfos[k]);
									}
								}
							}
						}
					}
				}
			} catch (Exception ex) {
				WebToolsPlugin.logException(ex);
			}
		} catch (Exception ex) {
			WebToolsPlugin.logException(ex);
		}

	}

	private String getAttribute(String source, Pattern pattern) {
		Matcher matcher = pattern.matcher(source);
		if (matcher.find()) {
			return matcher.group(1);
		}
		return null;
	}

//	public void addTaglibDirective(IDocument doc,String prefix,String uri){
//		try {
//			doc.replace(taglibInsertIndex,0,"\n<%@ taglib uri=\""+uri+"\" prefix=\""+prefix+"\" %>");
//		} catch(Exception ex){
//			WebToolsPlugin.logException(ex);
//		}
//	}

	public String getTaglibUri(String prefix) {
		String uri = null;
		TLDInfo[] tlds = getTLDInfo();
		for (int i = 0; i < tlds.length; i++) {
			if (tlds[i].getPrefix().equals(prefix)) {
				uri = tlds[i].getTaglibUri();
				break;
			}
		}
		return uri;
	}

	/**
	 * Returns an array of TLDInfo.
	 *
	 * @return an array of TLDInfo.
	 */
	public TLDInfo[] getTLDInfo() {
		return tldInfoList.toArray(new TLDInfo[tldInfoList.size()]);
	}

}
