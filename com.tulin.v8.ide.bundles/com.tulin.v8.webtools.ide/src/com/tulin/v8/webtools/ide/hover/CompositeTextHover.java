package com.tulin.v8.webtools.ide.hover;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.text.IInformationControlCreator;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITextHover;
import org.eclipse.jface.text.ITextHoverExtension;
import org.eclipse.jface.text.ITextHoverExtension2;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.Region;

/**
 * A text hover that delegates its operations to children provided in
 * constructor and returns the first interesting result.
 *
 * @since 1.0
 */
@SuppressWarnings("deprecation")
public class CompositeTextHover implements ITextHover, ITextHoverExtension, ITextHoverExtension2 {

	private final List<ITextHover> allHovers;
	private LinkedHashMap<ITextHover, IRegion> regions = null;
	private LinkedHashMap<ITextHover, Object> currentHovers = null;

	public CompositeTextHover(List<ITextHover> hoversToConsider) {
		Assert.isNotNull(hoversToConsider);
		Assert.isLegal(hoversToConsider.size() > 1, "Do not compose a single hover."); //$NON-NLS-1$
		this.allHovers = Collections.unmodifiableList(hoversToConsider);
	}

	@Override
	public Object getHoverInfo2(ITextViewer textViewer, IRegion requestRegion) {
		this.currentHovers = new LinkedHashMap<>();
		for (ITextHover hover : this.allHovers) {
			IRegion currentRegion = this.regions.get(hover);
			if (currentRegion == null) {
				continue;
			}
			Object res = hover instanceof ITextHoverExtension2
					? ((ITextHoverExtension2) hover).getHoverInfo2(textViewer, currentRegion)
					: hover.getHoverInfo(textViewer, currentRegion);
			if (res != null) {
				this.currentHovers.put(hover, res);
			}
		}
		if (this.currentHovers.isEmpty()) {
			return null;
		} else if (this.currentHovers.size() == 1) {
			return this.currentHovers.values().iterator().next();
		} else {
			return this.currentHovers;
		}
	}

	@Override
	public IInformationControlCreator getHoverControlCreator() {
		if (this.currentHovers == null || this.currentHovers.isEmpty()) {
			return null;
		} else if (currentHovers.size() >= 1) {
			ITextHover hover = this.currentHovers.keySet().iterator().next();
			return hover instanceof ITextHoverExtension ? ((ITextHoverExtension) hover).getHoverControlCreator() : null;
		}
		return null;
	}

	@Override
	public String getHoverInfo(ITextViewer textViewer, IRegion hoverRegion) {
		return this.allHovers.stream().map(hover -> hover.getHoverInfo(textViewer, this.regions.get(hover)))
				.filter(Objects::nonNull).collect(Collectors.joining("\n")); //$NON-NLS-1$
	}

	@Override
	public IRegion getHoverRegion(ITextViewer textViewer, int offset) {
		this.regions = new LinkedHashMap<>();
		IRegion res = null;
		for (ITextHover hover : this.allHovers) {
			IRegion region = hover.getHoverRegion(textViewer, offset);
			if (region != null) {
				this.regions.put(hover, region);
				if (res == null) {
					res = region;
				} else {
					int startOffset = Math.max(res.getOffset(), region.getOffset());
					int endOffset = Math.min(res.getOffset() + res.getLength(),
							region.getOffset() + region.getLength());
					res = new Region(startOffset, endOffset - startOffset);
				}
			}
		}
		return res;
	}

}
