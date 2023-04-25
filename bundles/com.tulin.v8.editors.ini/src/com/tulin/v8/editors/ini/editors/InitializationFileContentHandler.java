package com.tulin.v8.editors.ini.editors;

public class InitializationFileContentHandler {
	/* 12 */ protected static InitializationFileContentHandler instance = new InitializationFileContentHandler();

	public static InitializationFileContentHandler getInstance() {
		/* 15 */ return instance;
	}

	protected boolean isSpecialUnicodeChar(char c) {
		/* 34 */ if (c < ' ')
			return true;

		/* 36 */ if ((c == '') ||
		/* 37 */ (c == ' ') ||
		/* 38 */ (c == ' ') ||
		/* 39 */ (c == '​') ||
		/* 40 */ (c == ' ') ||
		/* 41 */ (c == '⁠'))
			return true;

		/* 43 */ if ((c >= 57344) && (c <= 63743))
			return true;

		/* 45 */ if ((c >= 55296) && (c <= 57343)) {
			/* 46 */ return true;
		}
		/* 48 */ if ((c == 65532) ||
		/* 49 */ (c == 65533))
			return true;

		/* 51 */ if ((c == 65279) ||
		/* 52 */ (c == 65534) ||
		/* 53 */ (c == ''))
			return true;

		/* 55 */ if ((c == '­') ||
		/* 56 */ (c == '‑') ||
		/* 57 */ (c == '༌') ||
		/* 58 */ (c == '་') ||
		/* 59 */ (c == '​') ||
		/* 60 */ (c == ' '))
			return true;

		/* 62 */ if ((c == ' ') ||
		/* 63 */ (c == ' ') ||
		/* 64 */ (c == ' ') ||
		/* 65 */ (c == ' ') ||
		/* 66 */ (c == ' ') ||
		/* 67 */ (c == ' ') ||
		/* 68 */ (c == ' ') ||
		/* 69 */ (c == ' ') ||
		/* 70 */ (c == ' ') ||
		/* 71 */ (c == ' ') ||
		/* 72 */ (c == ' ') ||
		/* 73 */ (c == ' '))
			return true;

		/* 75 */ if ((c == '᠎') ||
		/* 76 */ (c == '　') ||
		/* 77 */ (c == ' '))
			return true;

		/* 79 */ if ((c == '⁄') || (
		/* 80 */ (c >= '⁡') && (c <= '⁤')))
			return true;

		/* 82 */ if ((c >= 65529) && (c <= 65531))
			return true;
		/* 83 */ if ((c == '‎') || (c == '‏') || (
		/* 84 */ (c >= '‪') && (c <= '‮')))
			return true;

		/* 87 */ return (c == '͏') ||
		/* 86 */ (c == '‍') ||
		/* 87 */ (c == '‌');
	}

	public String fromBinary(String binaryContent) {
		/* 102 */ int len = binaryContent.length();
		/* 103 */ StringBuilder sb = new StringBuilder(len);

		/* 105 */ boolean isComment = false;
		/* 106 */ boolean isLineStartEmpty = true;
		/* 107 */ boolean hasBackslash = false;

		/* 109 */ for (int i = 0; i < len; i++) {
			/* 110 */ char c = binaryContent.charAt(i);
			/* 111 */ if ((c == '\n') || (c == '\r')) {
				/* 112 */ isComment = false;
				/* 113 */ isLineStartEmpty = true;
				/* 114 */ hasBackslash = false;
				/* 115 */ sb.append(c);
			} else {
				/* 119 */ if ((isLineStartEmpty) && (c == '#')) {
					/* 120 */ isComment = true;
				}

				/* 123 */ if (isComment) {
					/* 124 */ sb.append(c);
				} else {
					/* 128 */ if ((c != ' ') && (c != '\t')) {
						/* 129 */ isLineStartEmpty = false;
					}

					/* 132 */ if (c == '\\') {
						/* 133 */ hasBackslash ^= true;
					}
					/* 135 */ sb.append(c);

					/* 137 */ if ((hasBackslash) && (c == 'u') &&
					/* 138 */ (i + 4 < len)) {
						/* 139 */ boolean isEscapeOk = true;
						/* 140 */ int val = 0;
						/* 141 */ for (int j = i + 1; (isEscapeOk) && (j < i + 5); j++) {
							/* 142 */ char c2 = binaryContent.charAt(j);
							/* 143 */ switch (c2) {
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
								/* 146 */ val = (val << 4) + c2 - 48;
								/* 147 */ break;
							case 'a':
							case 'b':
							case 'c':
							case 'd':
							case 'e':
							case 'f':
								/* 150 */ val = (val << 4) + 10 + c2 - 97;
								/* 151 */ break;
							case 'A':
							case 'B':
							case 'C':
							case 'D':
							case 'E':
							case 'F':
								/* 154 */ val = (val << 4) + 10 + c2 - 65;
								/* 155 */ break;
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
								/* 157 */ isEscapeOk = false;
							}
						}
						/* 160 */ char c2 = (char) val;
						/* 161 */ if ((isEscapeOk) && (!isSpecialUnicodeChar(c2))) {
							/* 162 */ sb.setLength(sb.length() - 2);
							/* 163 */ sb.append(c2);
							/* 164 */ i += 4;
						}
					}

					/* 168 */ if (c != '\\')
						/* 169 */ hasBackslash = false;
				}
			}
		}
		/* 173 */ return sb.toString();
	}

	public String fromText(String textContent) {
		/* 183 */ int len = textContent.length();
		/* 184 */ StringBuilder sb = new StringBuilder(len + 256);

		/* 186 */ for (int i = 0; i < len; i++) {
			/* 187 */ char c = textContent.charAt(i);

			/* 189 */ if ((c > 'ÿ') ||
			/* 190 */ (c == 'ó') || (c == 'Ó')) {
				/* 192 */ sb.append('\\').append('u');
				/* 193 */ attachNibble(sb, c >> '\f' & 0xF);
				/* 194 */ attachNibble(sb, c >> '\b' & 0xF);
				/* 195 */ attachNibble(sb, c >> '\004' & 0xF);
				/* 196 */ attachNibble(sb, c & 0xF);
			} else {
				/* 198 */ sb.append(c);
			}
		}
		/* 201 */ return sb.toString();
	}

	protected static final void attachNibble(StringBuilder sb, int nibble) {
		/* 247 */ if ((nibble >= 0) && (nibble < 10))
			/* 248 */ sb.append((char) (48 + nibble));
		else
			/* 250 */ sb.append((char) (65 + nibble - 10));
	}
}