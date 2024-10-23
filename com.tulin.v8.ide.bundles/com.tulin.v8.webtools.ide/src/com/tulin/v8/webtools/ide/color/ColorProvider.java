package com.tulin.v8.webtools.ide.color;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferenceConverter;
import org.eclipse.jface.resource.StringConverter;
import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.themes.ITheme;

/**
 * 高亮及文本颜色
 * 
 * @author chenqian
 * @update 2021-7-24
 *
 */
public class ColorProvider {

	private Map<RGB, Color> colorTable = new HashMap<RGB, Color>(10);
	private Map<String, Token> tokenTable = new HashMap<String, Token>(10);
	IPreferenceStore store;

	public ColorProvider(IPreferenceStore store) {
		this.store = store;
	}

	public IToken getToken(String prefKey) {
		Token token = (Token) tokenTable.get(prefKey);
		if (token == null) {
			String colorName = store.getString(prefKey);
			RGB rgb = StringConverter.asRGB(colorName);
			token = new Token(new TextAttribute(getColor(rgb)));

			tokenTable.put(prefKey, token);
		}
		return token;
	}

	public IToken getToken(String prefKey, int style) {
		Token token = (Token) tokenTable.get(prefKey + style);
		if (token == null) {
			String colorName = store.getString(prefKey);
			RGB rgb = StringConverter.asRGB(colorName);
			token = new Token(new TextAttribute(getColor(rgb), null, style));
			tokenTable.put(prefKey + style, token);
		}
		return token;
	}

	/**
	 * 获取字体颜色定义和字体加粗
	 * 
	 * @param prefKey
	 * @param bold
	 * @return
	 */
	public IToken getToken(String prefKey, boolean bold) {
		Token token = (Token) tokenTable.get(prefKey + bold);
		if (token == null) {
			String colorName = store.getString(prefKey);
			RGB rgb = StringConverter.asRGB(colorName);
			if (bold) {
				token = new Token(new TextAttribute(getColor(rgb), null, SWT.BOLD));
			} else {
				token = new Token(new TextAttribute(getColor(rgb)));
			}
			tokenTable.put(prefKey + bold, token);
		}
		return token;
	}

	public void dispose() {
		Iterator<Color> e = colorTable.values().iterator();
		while (e.hasNext()) {
			e.next().dispose();
		}
	}

	public Color getColor(String prefKey) {
		String colorName = store.getString(prefKey);
		RGB rgb = StringConverter.asRGB(colorName);
		return getColor(rgb);
	}

	private Color getColor(RGB rgb) {
		if (isDarkTheme()) {
			rgb = getInverseColor(rgb);
		} else {
		}
		Color color = colorTable.get(rgb);
		if (color == null) {
			color = new Color(Display.getCurrent(), rgb);
			colorTable.put(rgb, color);
		}
		return color;
	}

	public boolean affectsTextPresentation(PropertyChangeEvent event) {
		Token token = tokenTable.get(event.getProperty());
		return (token != null);
	}

	public void handlePreferenceStoreChanged(PropertyChangeEvent event) {
		String prefKey = event.getProperty();
		Token token = tokenTable.get(prefKey);
		if (token != null) {
			String colorName = store.getString(prefKey);
			RGB rgb = StringConverter.asRGB(colorName);
			token.setData(new TextAttribute(getColor(rgb)));
		}
	}

	/**
	 * 判断是否为暗色主题
	 * 
	 * @return
	 */
	public boolean isDarkTheme() {
		ITheme currentTheme = PlatformUI.getWorkbench().getThemeManager().getCurrentTheme();
		RGB bgColor = currentTheme.getColorRegistry().getRGB("org.eclipse.ui.workbench.ACTIVE_TAB_BG_START");
		if (bgColor == null) {
			// 如果没有设置特定的背景颜色，可以使用默认的背景颜色
			bgColor = PreferenceConverter.getDefaultColor((IPreferenceStore) PlatformUI.getWorkbench().getDisplay(),
					"org.eclipse.ui.workbench.ACTIVE_TAB_BG_START");
		}
		// 判断背景颜色是否属于暗色
		return isDarkColor(bgColor);
	}

	private boolean isDarkColor(RGB color) {
		// 根据颜色的亮度来判断是否为暗色
		double brightness = (color.red * 0.299 + color.green * 0.587 + color.blue * 0.114) / 255;
		return brightness < 0.5;
	}

	/**
	 * 颜色取反色
	 * 
	 * @param color
	 * @return
	 */
	public RGB getInverseColor(RGB color) {
		int red = 255 - color.red;
		int green = 255 - color.green;
		int blue = 255 - color.blue;
		return new RGB(red, green, blue);
	}

}
