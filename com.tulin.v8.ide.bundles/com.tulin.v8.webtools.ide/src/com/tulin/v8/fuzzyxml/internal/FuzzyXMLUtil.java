package com.tulin.v8.fuzzyxml.internal;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FuzzyXMLUtil {
	private static Pattern encoding = Pattern.compile("<\\?xml\\s+[^\\?>]*?encoding\\s*=\\s*\"(.*?)\"[^\\?>]*?\\?>");
	private static Pattern script = Pattern.compile("(<script.*?>)(.*?)(</script>)",
			Pattern.DOTALL | Pattern.CASE_INSENSITIVE);

	public static String escapeScript(String source) {
		StringBuffer sb = new StringBuffer();
		int lastIndex = 0;
		Matcher matcher = script.matcher(source);
		while (matcher.find()) {
			if (matcher.start() != 0) {
				sb.append(source.substring(lastIndex, matcher.start()));
			}
			sb.append(matcher.group(1));
			String group2 = matcher.group(2);
			for (int i = 0; i < group2.length(); i++) {
				sb.append(" ");
			}
			sb.append(matcher.group(3));
			lastIndex = matcher.end();
		}
		if (lastIndex < source.length()) {
			sb.append(source.substring(lastIndex));
		}
		return sb.toString();
	}

	public static String escapeString(String source) {
		StringBuffer sb = new StringBuffer();
		int flag = 0;
		boolean tag = false;
		boolean escape = false;
		for (int i = 0; i < source.length(); i++) {
			char c = source.charAt(i);
			if (tag) {
				if ((flag == 1 || flag == 2) && c == '\\') {
					escape = true;
				} else if (flag == 0 && c == '"') {
					flag = 1;
				} else if (flag == 1 && c == '"') {
					if (!escape) {
						flag = 0;
					}
					escape = false;
				} else if (flag == 0 && c == '\'') {
					flag = 2;
				} else if (flag == 2 && c == '\'') {
					if (!escape) {
						flag = 0;
					}
					escape = false;
				} else if ((flag == 1 || flag == 2)) {
					sb.append(' ');
					continue;
				} else if (flag == 0 && c == '>') {
					tag = false;
				}
			} else if (c == '<') {
				tag = true;
			}
			sb.append(c);
		}

		return sb.toString();
	}

	public static String comment2space(String source, boolean contentsOnly) {
		int index = 0;
		int last = 0;
		StringBuffer sb = new StringBuffer();
		while ((index = source.indexOf("<!--", last)) != -1) {
			int end = source.indexOf("-->", index);
			if (end != -1) {
				sb.append(source.substring(last, index));
				int length = end - index + 3;
				if (contentsOnly) {
					sb.append("<!--");
					length = length - 7;
				}
				for (int i = 0; i < length; i++) {
					sb.append(" ");
				}
				if (contentsOnly) {
					sb.append("-->");
				}
			} else {
				break;
			}
			last = end + 3;
		}
		if (last != source.length()) {
			sb.append(source.substring(last));
		}
		return sb.toString();
	}

	public static String cdata2space(String source, boolean contentsOnly) {
		int index = 0;
		int last = 0;
		StringBuffer sb = new StringBuffer();
		while ((index = source.indexOf("<![CDATA[", last)) != -1) {
			int end = source.indexOf("]]>", index);
			if (end != -1) {
				sb.append(source.substring(last, index));
				int length = end - index + 3;
				if (contentsOnly) {
					sb.append("<![CDATA[");
					length = length - 12;
				}
				for (int i = 0; i < length; i++) {
					sb.append(" ");
				}
				if (contentsOnly) {
					sb.append("]]>");
				}
			} else {
				break;
			}
			last = end + 3;
		}
		if (last != source.length()) {
			sb.append(source.substring(last));
		}
		return sb.toString();
	}

	public static String doctype2space(String source, boolean contentsOnly) {
		int index = 0;
		int last = 0;
		StringBuffer sb = new StringBuffer();
		while ((index = source.indexOf("<!DOCTYPE", last)) != -1) {
			sb.append(source.substring(last, index));
			if (contentsOnly) {
				sb.append("<!DOCTYPE");
			} else {
				sb.append("         ");
			}
			boolean flag = false;
			for (index = index + 9; index < source.length(); index++) {
				char c = source.charAt(index);
				if (c == '[') {
					flag = true;
				}
				if (flag == true && c == ']') {
					flag = false;
				}
				if (flag == false && c == '>') {
					if (contentsOnly) {
						sb.append('>');
					} else {
						sb.append(' ');
					}
					break;
				}
				sb.append(" ");
			}
			last = index + 1;
		}
		if (last != source.length()) {
			sb.append(source.substring(last));
		}
		return sb.toString();
	}

	public static String processing2space(String source, boolean contentsOnly) {
		int index = 0;
		int last = 0;
		StringBuffer sb = new StringBuffer();
		while ((index = source.indexOf("<?", last)) != -1) {
			int end = source.indexOf("?>", index);
			if (end != -1) {
				sb.append(source.substring(last, index));
				int length = end - index + 2;
				if (contentsOnly) {
					sb.append("<?");
					length = length - 4;
				}
				for (int i = 0; i < length; i++) {
					sb.append(" ");
				}
				if (contentsOnly) {
					sb.append("?>");
				}
			} else {
				break;
			}
			last = end + 2;
		}
		if (last != source.length()) {
			sb.append(source.substring(last));
		}
		return sb.toString();
	}

	public static String scriptlet2space(String source, boolean contentsOnly) {
		int index = 0;
		int last = 0;
		StringBuffer sb = new StringBuffer();
		while ((index = source.indexOf("<%", last)) != -1) {
			int end = source.indexOf("%>", index);
			if (end != -1) {
				sb.append(source.substring(last, index));
				int length = end - index + 2;
				if (contentsOnly) {
					sb.append("<%");
					length = length - 4;
				}
				for (int i = 0; i < length; i++) {
					sb.append(" ");
				}
				if (contentsOnly) {
					sb.append("%>");
				}
			} else {
				break;
			}
			last = end + 2;
		}
		if (last != source.length()) {
			sb.append(source.substring(last));
		}
		return sb.toString();
	}

	public static byte[] readStream(InputStream in) throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		int len = 0;
		byte[] buf = new byte[1024 * 8];
		while ((len = in.read(buf)) != -1) {
			out.write(buf, 0, len);
		}
		byte[] result = out.toByteArray();
		in.close();
		out.close();
		return result;
	}

	public static String escape(String value, boolean isHTML) {
		Entities entities = null;
		if (isHTML) {
			entities = Entities.HTML40;
		} else {
			entities = Entities.XML;
		}

		StringBuffer buf = new StringBuffer(value.length() * 2);
		int i;
		for (i = 0; i < value.length(); ++i) {
			char ch = value.charAt(i);
			String entityName = entities.entityName(ch);
			if (entityName == null) {
				buf.append(ch);
			} else {
				buf.append('&');
				buf.append(entityName);
				buf.append(';');
			}
		}
		return buf.toString();
	}

	public static String decode(String value, boolean isHTML) {
		Entities entities = null;
		if (isHTML) {
			entities = Entities.HTML40;
		} else {
			entities = Entities.XML;
		}

		StringBuffer buf = new StringBuffer(value.length());
		int i;
		for (i = 0; i < value.length(); ++i) {
			char ch = value.charAt(i);
			if (ch == '&') {
				int semi = value.indexOf(';', i + 1);
				if (semi == -1) {
					buf.append(ch);
					continue;
				}
				String entityName = value.substring(i + 1, semi);
				int entityValue;
				if (entityName.length() == 0) {
					entityValue = -1;
				} else if (entityName.charAt(0) == '#') {
					if (entityName.length() == 1) {
						entityValue = -1;
					} else {
						char charAt1 = entityName.charAt(1);
						try {
							if (charAt1 == 'x' || charAt1 == 'X') {
								entityValue = Integer.valueOf(entityName.substring(2), 16).intValue();
							} else {
								entityValue = Integer.parseInt(entityName.substring(1));
							}
						} catch (NumberFormatException ex) {
							entityValue = -1;
						}
					}
				} else {
					entityValue = entities.entityValue(entityName);
				}
				if (entityValue == -1) {
					buf.append('&');
					buf.append(entityName);
					buf.append(';');
				} else {
					buf.append((char) (entityValue));
				}
				i = semi;
			} else {
				buf.append(ch);
			}
		}
		return buf.toString();
	}

	public static String escapeCDATA(String value) {
		value = value.replaceAll(">", "&gt;");
		return value;
	}

	public static String getEncoding(byte[] bytes) {
		String str = new String(bytes);
		Matcher matcher = encoding.matcher(str);
		if (matcher.find()) {
			return matcher.group(1);
		}
		return null;

	}

	public static int getSpaceIndex(String value) {
		char[] chars = { ' ', '\t', '\r', '\n' };
		for (int i = 0; i < chars.length; i++) {
			int index = value.indexOf(chars[i]);
			if (index >= 0) {
				return index;
			}
		}
		return -1;
	}

	public static boolean isWhitespace(char c) {
		if (c == ' ' || c == '\t' || c == '\r' || c == '\n') {
			return true;
		}
		return false;
	}

}
