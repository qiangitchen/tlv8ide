package com.tulin.v8.ide.ui.editors.function;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Result;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.xerces.dom.TextImpl;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMModel;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMNode;
//import org.eclipse.wst.xml.core.internal.provisional.document.IDOMModel;
//import org.eclipse.wst.xml.core.internal.provisional.document.IDOMNode;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

import com.tulin.v8.core.XMLFormator;
import com.tulin.v8.ide.Sys;

@SuppressWarnings({ "unchecked", "rawtypes", "restriction" })
class W3cDocumentHelper {
	// private static String localObject1;
	private static Attr localObject3;

	public static List getChildXmlElementList(Element paramElement,
			String paramString) {
		NodeList localNodeList = paramElement.getChildNodes();
		ArrayList localArrayList = new ArrayList();
		for (int i = 0; i < localNodeList.getLength(); i++) {
			Node localNode = localNodeList.item(i);
			if ((localNode.getNodeType() != 1)
					|| (!getLocalName(localNode).equals(paramString)))
				continue;
			localArrayList.add(localNode);
		}
		return localArrayList;
	}

	public static String asXML(Element paramElement) {
		StringBuffer localStringBuffer = new StringBuffer();
		a(paramElement, localStringBuffer, null, false, false);
		return localStringBuffer.toString();
	}

	public static String docToString(Document paramDocument) {
		String str = "";
		try {
			DOMSource source = new DOMSource(paramDocument);
			TransformerFactory localTransformerFactory = TransformerFactory
					.newInstance();
			Transformer localTransformer = localTransformerFactory
					.newTransformer();
			StringWriter writer = new StringWriter();
			Result result = new StreamResult(writer);
			localTransformer.transform(source, result);
			str = writer.getBuffer().toString();
			str = XMLFormator.formatXMLStr(str);
		} catch (TransformerConfigurationException e1) {
			e1.printStackTrace();
		} catch (TransformerException e2) {
			e2.printStackTrace();
		}
		return str;
	}

	public static String asXML(Element paramElement, List paramList) {
		StringBuffer localStringBuffer = new StringBuffer();
		a(paramElement, localStringBuffer, paramList, false, false);
		return localStringBuffer.toString();
	}

	public static String asXML(Element paramElement, List paramList,
			boolean paramBoolean) {
		StringBuffer localStringBuffer = new StringBuffer();
		a(paramElement, localStringBuffer, paramList, paramBoolean,
				paramBoolean);
		return localStringBuffer.toString();
	}

	public static String asXML(Element paramElement, List paramList,
			boolean paramBoolean1, boolean paramBoolean2) {
		StringBuffer localStringBuffer = new StringBuffer();
		a(paramElement, localStringBuffer, paramList, paramBoolean1,
				paramBoolean2);
		return localStringBuffer.toString();
	}

	private static void a(Element paramElement, StringBuffer paramStringBuffer,
			List paramList, boolean paramBoolean1, boolean paramBoolean2) {
		String str1 = paramElement.getPrefix();
		String str2 = "";
		if ((str1 != null) && (!str1.equals("")) && (!paramBoolean1))
			str2 = "xmlns:" + str1 + "=\"" + paramElement.getNamespaceURI()
					+ "\" ";
		paramStringBuffer.append("<" + paramElement.getTagName() + " " + str2);
		// if (paramBoolean2) {
		// localObject1 = (String) paramElement.getUserData("designId");
		// if ((localObject1 != null) && (!((String) localObject1).equals("")))
		// paramStringBuffer.append("designId=\"" + (String) localObject1
		// + "\" ");
		// }
		Object localObject1 = getAttributes(paramElement);
		Object localObject2 = ((List) localObject1).iterator();
		while (((Iterator) localObject2).hasNext()) {
			localObject3 = (Attr) ((Iterator) localObject2).next();
			if (((paramList != null) && (paramList
					.contains(((Attr) localObject3).getName())))
					|| (((Attr) localObject3).getName().equals("xmlns:" + str1)))
				continue;
			String str3 = ((Attr) localObject3).getValue();
			str3 = str3.replaceAll("&", "&amp;");
			str3 = str3.replaceAll("\"", "&quot;");
			str3 = str3.replaceAll("<", "&lt;");
			str3 = str3.replaceAll(">", "&gt;");
			paramStringBuffer.append(((Attr) localObject3).getName() + "=\""
					+ str3 + "\" ");
		}
		localObject2 = paramElement.getChildNodes();
		Object localObject3 = new ArrayList();
		Object localObject4;
		for (int i = 0; i < ((NodeList) localObject2).getLength(); i++) {
			localObject4 = ((NodeList) localObject2).item(i);
			if ((((Node) localObject4).getNodeType() != 1)
					&& (((Node) localObject4).getNodeType() != 3)
					&& (((Node) localObject4).getNodeType() != 4))
				continue;
			((List) localObject3).add(localObject4);
		}
		if (((List) localObject3).size() == 0) {
			paramStringBuffer.append("/>\n");
		} else {
			paramStringBuffer.append(">\n");
			int i = 0;
			localObject4 = ((List) localObject3).iterator();
			while (((Iterator) localObject4).hasNext()) {
				Node localNode = (Node) ((Iterator) localObject4).next();
				if (localNode.getNodeType() == 1) {
					if (i == 1) {
						paramStringBuffer.append("]]>\n");
						i = 0;
					}
					a((Element) localNode, paramStringBuffer, paramList,
							paramBoolean1, paramBoolean2);
				} else if ((localNode.getNodeType() == 3)
						|| (localNode.getNodeType() == 4)) {
					String str4 = localNode.getNodeValue();
					if ((str4 != null) && (!str4.trim().equals("")))
						if ((!str4.startsWith("<![CDATA["))
								&& (localNode.getNodeType() == 4)) {
							if (i == 0)
								paramStringBuffer.append("<![CDATA[");
							i = 1;
							paramStringBuffer.append(str4);
						} else {
							paramStringBuffer.append(str4);
						}
				}
			}
			if (i == 1)
				paramStringBuffer.append("]]>\n");
			paramStringBuffer.append("</" + paramElement.getTagName() + ">\n");
		}
	}

	public static Element getFirstChildElement(Element paramElement) {
		NodeList localNodeList = paramElement.getChildNodes();
		for (int i = 0; i < localNodeList.getLength(); i++)
			if (localNodeList.item(i).getNodeType() == 1)
				return (Element) localNodeList.item(i);
		return null;
	}

	public static String getLocalName(Node paramNode) {
		String str = "";
		if ((paramNode instanceof Element))
			str = ((Element) paramNode).getTagName();
		else if ((paramNode instanceof Attr))
			str = ((Attr) paramNode).getName();
		int i = str.indexOf(":");
		if (i != -1)
			return str.substring(i + 1);
		return str;
	}

	public static List<Element> getAllChildXmlElements(Element paramElement) {
		NodeList localNodeList = paramElement.getChildNodes();
		ArrayList localArrayList = new ArrayList();
		for (int i = 0; i < localNodeList.getLength(); i++) {
			Node localNode = localNodeList.item(i);
			if (localNode.getNodeType() != 1)
				continue;
			localArrayList.add((Element) localNode);
		}
		return localArrayList;
	}

	public static Attr createAttribute(Element paramElement,
			String paramString1, String paramString2) {
		if (paramElement.hasAttribute(paramString1)) {
			Attr localAttr = paramElement.getAttributeNode(paramString1);
			paramElement.setAttribute(paramString1, paramString2);
			return localAttr;
		}
		Attr localAttr = paramElement.getOwnerDocument().createAttribute(
				paramString1);
		localAttr.setValue(paramString2);
		paramElement.setAttributeNode(localAttr);
		paramElement.setAttribute(paramString1, paramString2);
		return localAttr;
	}

	public static Node getNextSiblingElement(Node paramNode) {
		for (Node localNode = paramNode.getNextSibling(); localNode != null; localNode = localNode
				.getNextSibling())
			if (localNode.getNodeType() == 1)
				return localNode;
		return null;
	}

	public static boolean hasChild(Element paramElement, Node paramNode) {
		NodeList localNodeList = paramElement.getChildNodes();
		int i = 0;
		int j = localNodeList.getLength();
		while (i < j) {
			if (localNodeList.item(i) == paramNode)
				return true;
			i++;
		}
		return false;
	}

	public static String getElementTextValue(Element paramElement) {
		List localList = a(paramElement);
		return localList != null ? getValueForTextContent(localList) : null;
	}

	protected static String getValueForTextContent(List paramList) {
		String str = null;
		if (paramList.size() > 0) {
			if ((paramList.get(0) instanceof org.apache.xerces.dom.TextImpl)) {
				TextImpl localObject = (org.apache.xerces.dom.TextImpl) paramList
						.get(0);
				return ((org.apache.xerces.dom.TextImpl) localObject)
						.getTextContent();
			}
			Object localObject = (IDOMNode) paramList.get(0);
			IDOMNode localIDOMNode = (IDOMNode) paramList
					.get(paramList.size() - 1);
			IDOMModel localIDOMModel = ((IDOMNode) localObject).getModel();
			int i = ((IDOMNode) localObject).getStartOffset();
			int j = localIDOMNode.getEndOffset();
			try {
				str = localIDOMModel.getStructuredDocument().get(i, j - i);
			} catch (Exception localException) {
			}
		}
		if (str != null)
			str = str.trim();
		return (String) str;
	}

	public static Element getFirstChildXmlElementByTag(Element paramElement,
			String paramString) {
		NodeList localNodeList = paramElement.getChildNodes();
		for (int i = 0; i < localNodeList.getLength(); i++) {
			Node localNode = localNodeList.item(i);
			if ((localNode.getNodeType() == 1)
					&& (paramString.equals(getLocalName(localNode))))
				return (Element) localNode;
		}
		return null;
	}

	public static Element getFirstChildXmlElementByAttr(Element paramElement,
			String paramString1, String paramString2, String paramString3,
			String paramString4) {
		NodeList localNodeList = paramElement.getChildNodes();
		for (int i = 0; i < localNodeList.getLength(); i++) {
			Node localNode = localNodeList.item(i);
			if ((localNode.getNodeType() != 1)
					|| (!getLocalName(localNode).equals(paramString1)))
				continue;
			Element localElement = (Element) localNode;
			String str = localElement
					.getAttributeNS(paramString2, paramString3);
			if ((str != null) && (str.equals(paramString4)))
				return localElement;
		}
		return null;
	}

	public static Element getFirstChildXmlElement(Element paramElement) {
		NodeList localNodeList = paramElement.getChildNodes();
		for (int i = 0; i < localNodeList.getLength(); i++) {
			Node localNode = localNodeList.item(i);
			if (localNode.getNodeType() == 1)
				return (Element) localNode;
		}
		return null;
	}

	public static String getText(Element paramElement) {
		NodeList localNodeList = paramElement.getChildNodes();
		for (int i = 0; i < localNodeList.getLength(); i++) {
			Node localNode = localNodeList.item(i);
			if (localNode.getNodeType() == 3)
				return localNode.getNodeValue();
			if (localNode.getNodeType() == 4)
				return localNode.getNodeValue();
		}
		return "";
	}

	public static String getFullText(Element paramElement) {
		NodeList localNodeList = paramElement.getChildNodes();
		StringBuffer localStringBuffer = new StringBuffer();
		int i = 0;
		for (int j = 0; j < localNodeList.getLength(); j++) {
			Node localNode = localNodeList.item(j);
			if (localNode.getNodeType() == 3) {
				if (i != 0)
					break;
				localStringBuffer.append(localNode.getNodeValue());
			} else if (localNode.getNodeType() == 4) {
				if (i == 0)
					localStringBuffer.delete(0, localStringBuffer.length());
				i = 1;
				localStringBuffer.append(localNode.getNodeValue());
			} else {
				if (i != 0)
					break;
			}
		}
		String str = localStringBuffer.toString();
		if (i != 0)
			return str;
		return str;
	}

	public static void setFullText(Element paramElement, String paramString,
			boolean paramBoolean) {
		NodeList localNodeList = paramElement.getChildNodes();
		int i = 0;
		int j = localNodeList.getLength();
		while (i < j) {
			paramElement.removeChild(localNodeList.item(0));
			i++;
		}
		if (paramBoolean)
			paramElement.appendChild(paramElement.getOwnerDocument()
					.createCDATASection(paramString));
		else
			paramElement.appendChild(paramElement.getOwnerDocument()
					.createTextNode(paramString));
	}

	public static Node getTextNode(Element paramElement) {
		NodeList localNodeList = paramElement.getChildNodes();
		for (int i = 0; i < localNodeList.getLength(); i++) {
			Node localNode = localNodeList.item(i);
			if (localNode.getNodeType() == 3)
				return localNode;
		}
		return null;
	}

	public static List<Attr> getAttributes(Element paramElement) {
		NamedNodeMap localNamedNodeMap = paramElement.getAttributes();
		ArrayList localArrayList = new ArrayList();
		for (int i = 0; i < localNamedNodeMap.getLength(); i++) {
			Node localNode = localNamedNodeMap.item(i);
			if (localNode.getNodeType() != 2)
				continue;
			localArrayList.add((Attr) localNode);
		}
		return localArrayList;
	}

	public static void removeAllChild(Element paramElement) {
		List localList = getAllChildXmlElements(paramElement);
		Object localObject = localList.iterator();
		while (((Iterator) localObject).hasNext()) {
			Element localElement = (Element) ((Iterator) localObject).next();
			paramElement.removeChild(localElement);
		}
		localObject = paramElement.getChildNodes();
		for (int i = 0; i < ((NodeList) localObject).getLength(); i++) {
			Node localNode = ((NodeList) localObject).item(i);
			paramElement.removeChild(localNode);
		}
	}

	public static int getIndex(Element paramElement1, Element paramElement2) {
		List localList = getAllChildXmlElements(paramElement1);
		for (int i = 0; i < localList.size(); i++)
			if (paramElement2 == localList.get(i))
				return i;
		return -1;
	}

	public static String getAttribute(Element paramElement, String paramString) {
		NamedNodeMap localNamedNodeMap = paramElement.getAttributes();
		for (int i = 0; i < localNamedNodeMap.getLength(); i++) {
			Node localNode = localNamedNodeMap.item(i);
			if (localNode.getNodeType() != 2)
				continue;
			String str = localNode.getNodeName();
			if (str.equals(paramString))
				return localNode.getNodeValue();
		}
		return null;
	}

	public static Attr getAttributeByName(Element paramElement,
			String paramString) {
		NamedNodeMap localNamedNodeMap = paramElement.getAttributes();
		for (int i = 0; i < localNamedNodeMap.getLength(); i++) {
			Node localNode = localNamedNodeMap.item(i);
			if (localNode.getNodeType() != 2)
				continue;
			String str = localNode.getNodeName();
			if (str.equals(paramString))
				return (Attr) localNode;
		}
		return null;
	}

	public static Attr getAttributeByLocalName(Element paramElement,
			String paramString) {
		NamedNodeMap localNamedNodeMap = paramElement.getAttributes();
		for (int i = 0; i < localNamedNodeMap.getLength(); i++) {
			Node localNode = localNamedNodeMap.item(i);
			if (localNode.getNodeType() != 2)
				continue;
			String str1 = localNode.getNodeName();
			int j = str1.indexOf(":");
			if (j != -1) {
				String str2 = str1.substring(j + 1);
				if (str2.equals(paramString))
					return (Attr) localNode;
			} else if (str1.equals(paramString)) {
				return (Attr) localNode;
			}
		}
		return null;
	}

	public static String getAttrVlueByLocalName(Element paramElement,
			String paramString) {
		NamedNodeMap localNamedNodeMap = paramElement.getAttributes();
		for (int i = 0; i < localNamedNodeMap.getLength(); i++) {
			Node localNode = localNamedNodeMap.item(i);
			if (localNode.getNodeType() != 2)
				continue;
			String str1 = localNode.getNodeName();
			int j = str1.indexOf(":");
			if (j != -1) {
				String str2 = str1.substring(j + 1);
				if (str2.equals(paramString))
					return ((Attr) localNode).getValue();
			} else if (str1.equals(paramString)) {
				return ((Attr) localNode).getValue();
			}
		}
		return null;
	}

	public static void setElementText(Element paramElement, String paramString) {
		List localList = a(paramElement);
		if (localList != null) {
			if (paramString != null)
				paramString = paramString.replace("<", "&lt;").replace(">",
						"&gt;");
			setValueForTextContent(localList, paramString);
		} else {
			Document localDocument = paramElement.getOwnerDocument();
			if ((paramString != null) && (paramString.startsWith("<![CDATA["))) {
				paramString = paramString.replace("<![CDATA[", "");
				paramString = paramString.replace("]]>", "");
				paramElement.appendChild(localDocument
						.createCDATASection(paramString));
			} else {
				if (paramString != null)
					paramString = paramString.replace("<", "&lt;").replace(">",
							"&gt;");
				Text localText = localDocument.createTextNode(paramString);
				paramElement.appendChild(localText);
			}
		}
	}

	public static void setElementAttr(Element paramElement,
			String paramString1, String paramString2) {
		Attr localAttr = getAttributeByName(paramElement, paramString1);
		if (localAttr != null) {
			localAttr.setValue(paramString2);
		} else {
			Document localDocument = paramElement.getOwnerDocument();
			localAttr = localDocument.createAttribute(paramString1);
			localAttr.setNodeValue(paramString2);
			if (!paramElement.hasAttribute(paramString1))
				;
			paramElement.setAttribute(paramString1, paramString2);
		}
	}

	public static void delElementAttr(Element paramElement, String paramString) {
		Attr localAttr = getAttributeByName(paramElement, paramString);
		if (localAttr != null)
			paramElement.removeAttributeNode(localAttr);
	}

	protected static void setValueForTextContent(List paramList,
			String paramString) {
		if (paramList.size() > 0) {
			Object localObject1 = paramList.get(0);
			Object localObject2;
			if ((localObject1 instanceof org.eclipse.wst.xml.core.internal.document.TextImpl)) {
				localObject2 = (IDOMNode) paramList.get(0);
				IDOMNode localIDOMNode = (IDOMNode) paramList.get(paramList
						.size() - 1);
				int i = ((IDOMNode) localObject2).getStartOffset();
				int j = localIDOMNode.getEndOffset();
				((IDOMNode) localObject2).getModel().getStructuredDocument()
						.replaceText(new Object(), i, j - i, paramString);
			} else if ((localObject1 instanceof org.apache.xerces.dom.TextImpl)) {
				localObject2 = (org.apache.xerces.dom.TextImpl) localObject1;
				((org.apache.xerces.dom.TextImpl) localObject2)
						.setTextContent(paramString);
			}
		}
	}

	private static List<Node> a(Element paramElement) {
		Vector localVector = null;
		for (Node localNode = paramElement.getFirstChild(); localNode != null; localNode = localNode
				.getNextSibling())
			if ((localNode.getNodeType() == 3)
					|| (localNode.getNodeType() == 5)
					|| (localNode.getNodeType() == 4)) {
				if (localVector == null)
					localVector = new Vector();
				localVector.add(localNode);
			} else {
				localVector = null;
				break;
			}
		return localVector;
	}

	public static Document readFileAsXML(String paramString) {
		DocumentBuilder localDocumentBuilder = null;
		DocumentBuilderFactory localDocumentBuilderFactory = DocumentBuilderFactory
				.newInstance();
		Document localDocument2;
		try {
			localDocumentBuilder = localDocumentBuilderFactory
					.newDocumentBuilder();
			Document localDocument1 = localDocumentBuilder
					.parse(new FileInputStream(paramString));
			return localDocument1;
		} catch (Exception localException) {
			Sys.packErrMsg(Messages.getString("editors.FunEditor.rxmlerr")
					+ localException.toString());
			localDocument2 = localDocumentBuilder.newDocument();
		}
		return localDocument2;
	}

	public static Document StrToXML(String paramString) throws Exception {
		DocumentBuilder localDocumentBuilder = null;
		DocumentBuilderFactory localDocumentBuilderFactory = DocumentBuilderFactory
				.newInstance();
		localDocumentBuilder = localDocumentBuilderFactory.newDocumentBuilder();
		Document localDocument1 = localDocumentBuilder
				.parse(new ByteArrayInputStream(paramString.getBytes("UTF-8")));
		return localDocument1;
	}

	public static void writeXMLFile(String paramString, Document paramDocument) {
		File localFile = new File(paramString);
		if (!localFile.exists())
			try {
				localFile.createNewFile();
			} catch (IOException localIOException) {
				localIOException.printStackTrace();
			}
		try {
			TransformerFactory localTransformerFactory = TransformerFactory
					.newInstance();
			Transformer localTransformer = localTransformerFactory
					.newTransformer();
			localTransformer.setOutputProperty("encoding", "UTF-8");
			DOMSource localDOMSource = new DOMSource(paramDocument);
			StreamResult localStreamResult = new StreamResult(localFile);
			localTransformer.transform(localDOMSource, localStreamResult);
		} catch (Exception localException) {
			Sys.packErrMsg(Messages.getString("editors.FunEditor.wxmlerr")
					+ localException.toString());
		}
	}
}
