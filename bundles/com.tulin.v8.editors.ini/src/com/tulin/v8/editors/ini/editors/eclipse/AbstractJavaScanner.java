package com.tulin.v8.editors.ini.editors.eclipse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.eclipse.jdt.ui.text.IColorManager;
import org.eclipse.jdt.ui.text.IColorManagerExtension;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferenceConverter;
import org.eclipse.jface.resource.StringConverter;
import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.rules.BufferedRuleBasedScanner;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;

public abstract class AbstractJavaScanner extends BufferedRuleBasedScanner {
	private IColorManager fColorManager;
	private IPreferenceStore fPreferenceStore;
	private Map<String, Token> fTokenMap = new HashMap<>();
	private String[] fPropertyNamesColor;
	private String[] fPropertyNamesBold;
	private String[] fPropertyNamesItalic;
	private String[] fPropertyNamesStrikethrough;
	private String[] fPropertyNamesUnderline;
	private boolean fNeedsLazyColorLoading;

	protected abstract String[] getTokenProperties();

	protected abstract List<IRule> createRules();

	public AbstractJavaScanner(IColorManager manager, IPreferenceStore store) {
		this.fColorManager = manager;
		this.fPreferenceStore = store;
	}

	public final void initialize() {
		this.fPropertyNamesColor = getTokenProperties();
		int length = this.fPropertyNamesColor.length;
		this.fPropertyNamesBold = new String[length];
		this.fPropertyNamesItalic = new String[length];
		this.fPropertyNamesStrikethrough = new String[length];
		this.fPropertyNamesUnderline = new String[length];
		for (int i = 0; i < length; i++) {
			this.fPropertyNamesBold[i] = getBoldKey(this.fPropertyNamesColor[i]);
			this.fPropertyNamesItalic[i] = getItalicKey(this.fPropertyNamesColor[i]);
			this.fPropertyNamesStrikethrough[i] = getStrikethroughKey(this.fPropertyNamesColor[i]);
			this.fPropertyNamesUnderline[i] = getUnderlineKey(this.fPropertyNamesColor[i]);
		}
		this.fNeedsLazyColorLoading = (Display.getCurrent() == null);
		for (int i = 0; i < length; i++) {
			if (this.fNeedsLazyColorLoading)
				addTokenWithProxyAttribute(this.fPropertyNamesColor[i], this.fPropertyNamesBold[i],
						this.fPropertyNamesItalic[i], this.fPropertyNamesStrikethrough[i],
						this.fPropertyNamesUnderline[i]);
			else {
				addToken(this.fPropertyNamesColor[i], this.fPropertyNamesBold[i], this.fPropertyNamesItalic[i],
						this.fPropertyNamesStrikethrough[i], this.fPropertyNamesUnderline[i]);
			}
		}
		initializeRules();
	}

	protected String getBoldKey(String colorKey) {
		return colorKey + "_bold";
	}

	protected String getItalicKey(String colorKey) {
		return colorKey + "_italic";
	}

	protected String getStrikethroughKey(String colorKey) {
		return colorKey + "_strikethrough";
	}

	protected String getUnderlineKey(String colorKey) {
		return colorKey + "_underline";
	}

	public IToken nextToken() {
		if (this.fNeedsLazyColorLoading)
			resolveProxyAttributes();
		return super.nextToken();
	}

	private void resolveProxyAttributes() {
		if ((this.fNeedsLazyColorLoading) && (Display.getCurrent() != null)) {
			for (int i = 0; i < this.fPropertyNamesColor.length; i++) {
				addToken(this.fPropertyNamesColor[i], this.fPropertyNamesBold[i], this.fPropertyNamesItalic[i],
						this.fPropertyNamesStrikethrough[i], this.fPropertyNamesUnderline[i]);
			}
			this.fNeedsLazyColorLoading = false;
		}
	}

	private void addTokenWithProxyAttribute(String colorKey, String boldKey, String italicKey, String strikethroughKey,
			String underlineKey) {
		this.fTokenMap.put(colorKey,
				new Token(createTextAttribute(null, boldKey, italicKey, strikethroughKey, underlineKey)));
	}

	private void addToken(String colorKey, String boldKey, String italicKey, String strikethroughKey,
			String underlineKey) {
		if ((this.fColorManager != null) && (colorKey != null) && (this.fColorManager.getColor(colorKey) == null)) {
			RGB rgb = PreferenceConverter.getColor(this.fPreferenceStore, colorKey);
			if ((this.fColorManager instanceof IColorManagerExtension)) {
				IColorManagerExtension ext = (IColorManagerExtension) this.fColorManager;
				ext.unbindColor(colorKey);
				ext.bindColor(colorKey, rgb);
			}
		}
		if (!this.fNeedsLazyColorLoading) {
			this.fTokenMap.put(colorKey,
					new Token(createTextAttribute(colorKey, boldKey, italicKey, strikethroughKey, underlineKey)));
		} else {
			Token token = (Token) this.fTokenMap.get(colorKey);
			if (token != null)
				token.setData(createTextAttribute(colorKey, boldKey, italicKey, strikethroughKey, underlineKey));
		}
	}

	private TextAttribute createTextAttribute(String colorKey, String boldKey, String italicKey,
			String strikethroughKey, String underlineKey) {
		Color color = null;
		if (colorKey != null) {
			color = this.fColorManager.getColor(colorKey);
		}
		int style = this.fPreferenceStore.getBoolean(boldKey) ? 1 : 0;
		if (this.fPreferenceStore.getBoolean(italicKey)) {
			style |= 2;
		}
		if (this.fPreferenceStore.getBoolean(strikethroughKey)) {
			style |= 536870912;
		}
		if (this.fPreferenceStore.getBoolean(underlineKey)) {
			style |= 1073741824;
		}
		return new TextAttribute(color, null, style);
	}

	protected Token getToken(String key) {
		if (this.fNeedsLazyColorLoading)
			resolveProxyAttributes();
		return (Token) this.fTokenMap.get(key);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void initializeRules() {
		List rules = createRules();
		if (rules != null) {
			IRule[] result = new IRule[rules.size()];
			rules.toArray(result);
			setRules(result);
		}
	}

	private int indexOf(String property) {
		if (property != null) {
			int length = this.fPropertyNamesColor.length;
			for (int i = 0; i < length; i++) {
				if ((property.equals(this.fPropertyNamesColor[i])) || (property.equals(this.fPropertyNamesBold[i]))
						|| (property.equals(this.fPropertyNamesItalic[i]))
						|| (property.equals(this.fPropertyNamesStrikethrough[i]))
						|| (property.equals(this.fPropertyNamesUnderline[i])))
					return i;
			}
		}
		return -1;
	}

	public boolean affectsBehavior(PropertyChangeEvent event) {
		return indexOf(event.getProperty()) >= 0;
	}

	public void adaptToPreferenceChange(PropertyChangeEvent event) {
		String p = event.getProperty();
		int index = indexOf(p);
		Token token = getToken(this.fPropertyNamesColor[index]);
		if (this.fPropertyNamesColor[index].equals(p))
			adaptToColorChange(token, event);
		else if (this.fPropertyNamesBold[index].equals(p))
			adaptToStyleChange(token, event, 1);
		else if (this.fPropertyNamesItalic[index].equals(p))
			adaptToStyleChange(token, event, 2);
		else if (this.fPropertyNamesStrikethrough[index].equals(p))
			adaptToStyleChange(token, event, 536870912);
		else if (this.fPropertyNamesUnderline[index].equals(p))
			adaptToStyleChange(token, event, 1073741824);
	}

	private void adaptToColorChange(Token token, PropertyChangeEvent event) {
		RGB rgb = null;
		Object value = event.getNewValue();
		if ((value instanceof RGB))
			rgb = (RGB) value;
		else if ((value instanceof String)) {
			rgb = StringConverter.asRGB((String) value);
		}
		if (rgb != null) {
			String property = event.getProperty();
			Color color = this.fColorManager.getColor(property);
			if (((color == null) || (!rgb.equals(color.getRGB())))
					&& ((this.fColorManager instanceof IColorManagerExtension))) {
				IColorManagerExtension ext = (IColorManagerExtension) this.fColorManager;
				ext.unbindColor(property);
				ext.bindColor(property, rgb);
				color = this.fColorManager.getColor(property);
			}
			Object data = token.getData();
			if ((data instanceof TextAttribute)) {
				TextAttribute oldAttr = (TextAttribute) data;
				token.setData(new TextAttribute(color, oldAttr.getBackground(), oldAttr.getStyle()));
			}
		}
	}

	private void adaptToStyleChange(Token token, PropertyChangeEvent event, int styleAttribute) {
		boolean eventValue = false;
		Object value = event.getNewValue();
		if ((value instanceof Boolean))
			eventValue = ((Boolean) value).booleanValue();
		else if ("true".equals(value)) {
			eventValue = true;
		}
		Object data = token.getData();
		if ((data instanceof TextAttribute)) {
			TextAttribute oldAttr = (TextAttribute) data;
			boolean activeValue = (oldAttr.getStyle() & styleAttribute) == styleAttribute;
			if (activeValue != eventValue)
				token.setData(new TextAttribute(oldAttr.getForeground(), oldAttr.getBackground(),
						eventValue ? oldAttr.getStyle() | styleAttribute
								: oldAttr.getStyle() & (styleAttribute ^ 0xFFFFFFFF)));
		}
	}

	protected IPreferenceStore getPreferenceStore() {
		return this.fPreferenceStore;
	}
}