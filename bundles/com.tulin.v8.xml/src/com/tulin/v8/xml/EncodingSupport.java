package com.tulin.v8.xml;

import org.eclipse.ui.editors.text.DefaultEncodingSupport;
import org.eclipse.ui.editors.text.IEncodingSupport;
import org.eclipse.ui.texteditor.StatusTextEditor;
import org.eclipse.wst.sse.ui.internal.ExtendedConfigurationBuilder;

/**
 * An implementation that will try to use a <b>provisional</b> configuration if
 * IEncodingSupport.class as a delegate. A delegate, if found, will be asked for
 * encoding information before defaulting to the superclass implementation, but
 * will be told to set the encoding after the superclass has. Delegates should
 * not duplicate any functionality in the DefaultEncodingSupport implementation
 * as they may be executed in addition to the default behavior.
 */
@SuppressWarnings({ "restriction" })
public class EncodingSupport extends DefaultEncodingSupport {
	private String[] fConfigurationPoints = null;
	/** The editor this support is associated with. */
	private StatusTextEditor fStatusTextEditor;

	IEncodingSupport fSupportDelegate = null;

	public EncodingSupport(String[] configurationPoints) {
		super();
		fConfigurationPoints = configurationPoints;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.editors.text.DefaultEncodingSupport#dispose()
	 */
	public void dispose() {
		super.dispose();

		if (fSupportDelegate instanceof DefaultEncodingSupport) {
			((DefaultEncodingSupport) fSupportDelegate).dispose();
		}
		fSupportDelegate = null;
		fStatusTextEditor = null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.ui.editors.text.DefaultEncodingSupport#
	 * getDefaultEncoding()
	 */
	public String getDefaultEncoding() {
		IEncodingSupport delegate = getEncodingSupportDelegate();
		if (delegate != null) {
			return delegate.getDefaultEncoding();
		}

		return super.getDefaultEncoding();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.editors.text.DefaultEncodingSupport#getEncoding ()
	 */
	public String getEncoding() {
		IEncodingSupport delegate = getEncodingSupportDelegate();
		if (delegate != null) {
			return delegate.getEncoding();
		}

		return super.getEncoding();
	}

	IEncodingSupport getEncodingSupportDelegate() {
		if (fSupportDelegate == null) {
			ExtendedConfigurationBuilder builder = ExtendedConfigurationBuilder
					.getInstance();
			for (int i = 0; fSupportDelegate == null
					&& i < fConfigurationPoints.length; i++) {
				fSupportDelegate = (IEncodingSupport) builder.getConfiguration(
						IEncodingSupport.class.getName(),
						fConfigurationPoints[i]);
			}
		}
		return fSupportDelegate;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.editors.text.DefaultEncodingSupport#initialize(org.eclipse
	 * .ui.texteditor.StatusTextEditor)
	 */
	public void initialize(StatusTextEditor textEditor) {
		super.initialize(textEditor);
		fStatusTextEditor = textEditor;

		IEncodingSupport encodingSupportDelegate = getEncodingSupportDelegate();
		if (encodingSupportDelegate instanceof DefaultEncodingSupport) {
			((DefaultEncodingSupport) encodingSupportDelegate)
					.initialize(textEditor);
		}
	}

	public void reinitialize(String[] configurationPoints) {
		if (fSupportDelegate instanceof DefaultEncodingSupport) {
			((DefaultEncodingSupport) fSupportDelegate).dispose();
		}
		fSupportDelegate = null;

		fConfigurationPoints = configurationPoints;

		IEncodingSupport encodingSupportDelegate = getEncodingSupportDelegate();
		if (encodingSupportDelegate instanceof DefaultEncodingSupport) {
			((DefaultEncodingSupport) encodingSupportDelegate)
					.initialize(fStatusTextEditor);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.editors.text.DefaultEncodingSupport#setEncoding
	 * (java.lang.String, boolean)
	 */
	protected void setEncoding(String encoding, boolean overwrite) {
		super.setEncoding(encoding, overwrite);

		IEncodingSupport delegate = getEncodingSupportDelegate();
		if (delegate != null && overwrite) {
			delegate.setEncoding(encoding);
		}
	}
}
