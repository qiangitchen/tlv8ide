package com.tulin.v8.editors.ini.editors;

public class InitializationFileContentHandler {
	protected static InitializationFileContentHandler instance = new InitializationFileContentHandler();

	public static InitializationFileContentHandler getInstance() {
		return instance;
	}

	protected boolean isSpecialUnicodeChar(char c) {
		if (c < ' ')
			return true;

		if ((c == '') || (c == ' ') || (c == ' ') || (c == '​') || (c == ' ') || (c == '⁠'))
			return true;

		if ((c >= 57344) && (c <= 63743))
			return true;

		if ((c >= 55296) && (c <= 57343)) {
			return true;
		}
		if ((c == 65532) || (c == 65533))
			return true;

		if ((c == 65279) || (c == 65534) || (c == ''))
			return true;

		if ((c == '­') || (c == '‑') || (c == '༌') || (c == '་') || (c == '​') || (c == ' '))
			return true;

		if ((c == ' ') || (c == ' ') || (c == ' ') || (c == ' ') || (c == ' ') || (c == ' ') || (c == ' ') || (c == ' ')
				|| (c == ' ') || (c == ' ') || (c == ' ') || (c == ' '))
			return true;

		if ((c == '᠎') || (c == '　') || (c == ' '))
			return true;

		if ((c == '⁄') || ((c >= '⁡') && (c <= '⁤')))
			return true;

		if ((c >= 65529) && (c <= 65531))
			return true;
		if ((c == '‎') || (c == '‏') || ((c >= '‪') && (c <= '‮')))
			return true;

		return (c == '͏') || (c == '‍') || (c == '‌');
	}

	public String fromBinary(String binaryContent) {
		int len = binaryContent.length();
		StringBuilder sb = new StringBuilder(len);

		boolean isComment = false;
		boolean isLineStartEmpty = true;
		boolean hasBackslash = false;

		for (int i = 0; i < len; i++) {
			char c = binaryContent.charAt(i);
			if ((c == '\n') || (c == '\r')) {
				isComment = false;
				isLineStartEmpty = true;
				hasBackslash = false;
				sb.append(c);
			} else {
				if ((isLineStartEmpty) && (c == '#')) {
					isComment = true;
				}

				if (isComment) {
					sb.append(c);
				} else {
					if ((c != ' ') && (c != '\t')) {
						isLineStartEmpty = false;
					}

					if (c == '\\') {
						hasBackslash ^= true;
					}
					sb.append(c);

					if ((hasBackslash) && (c == 'u') && (i + 4 < len)) {
						boolean isEscapeOk = true;
						int val = 0;
						for (int j = i + 1; (isEscapeOk) && (j < i + 5); j++) {
							char c2 = binaryContent.charAt(j);
							switch (c2) {
							case '0':
							case '1':
							case '2':
							case '3':
							case '4':
							case '5':
							case '6':
							case '7':
							case '8':
							case '9':
								val = (val << 4) + c2 - 48;
								break;
							case 'a':
							case 'b':
							case 'c':
							case 'd':
							case 'e':
							case 'f':
								val = (val << 4) + 10 + c2 - 97;
								break;
							case 'A':
							case 'B':
							case 'C':
							case 'D':
							case 'E':
							case 'F':
								val = (val << 4) + 10 + c2 - 65;
								break;
							case ':':
							case ';':
							case '<':
							case '=':
							case '>':
							case '?':
							case '@':
							case 'G':
							case 'H':
							case 'I':
							case 'J':
							case 'K':
							case 'L':
							case 'M':
							case 'N':
							case 'O':
							case 'P':
							case 'Q':
							case 'R':
							case 'S':
							case 'T':
							case 'U':
							case 'V':
							case 'W':
							case 'X':
							case 'Y':
							case 'Z':
							case '[':
							case '\\':
							case ']':
							case '^':
							case '_':
							case '`':
							default:
								isEscapeOk = false;
							}
						}
						char c2 = (char) val;
						if ((isEscapeOk) && (!isSpecialUnicodeChar(c2))) {
							sb.setLength(sb.length() - 2);
							sb.append(c2);
							i += 4;
						}
					}

					if (c != '\\')
						hasBackslash = false;
				}
			}
		}
		return sb.toString();
	}

	public String fromText(String textContent) {
		int len = textContent.length();
		StringBuilder sb = new StringBuilder(len + 256);

		for (int i = 0; i < len; i++) {
			char c = textContent.charAt(i);

			if ((c > 'ÿ') || (c == 'ó') || (c == 'Ó')) {
				sb.append('\\').append('u');
				attachNibble(sb, c >> '\f' & 0xF);
				attachNibble(sb, c >> '\b' & 0xF);
				attachNibble(sb, c >> '\004' & 0xF);
				attachNibble(sb, c & 0xF);
			} else {
				sb.append(c);
			}
		}
		return sb.toString();
	}

	protected static final void attachNibble(StringBuilder sb, int nibble) {
		if ((nibble >= 0) && (nibble < 10))
			sb.append((char) (48 + nibble));
		else
			sb.append((char) (65 + nibble - 10));
	}
}