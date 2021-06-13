package com.tulin.v8.ide.ui.editors.page.design.anchors;

import org.eclipse.draw2d.*;
import org.eclipse.draw2d.geometry.*;

import static com.tulin.v8.ide.ui.editors.page.design.figures.MarriageFigure.RADIUS;

/**
 * An anchor for the center of the {@link MarriageFigure} that returns a
 * location along the outside edge of the {@link MarriageFigure}
 */
@SuppressWarnings("deprecation")
public class MarriageAnchor extends AbstractConnectionAnchor {
	public MarriageAnchor(IFigure owner) {
		super(owner);
	}

	public Point getLocation(Point originalReference) {
		PrecisionPoint reference = new PrecisionPoint(originalReference);
		PrecisionPoint origin = new PrecisionPoint(getOwner().getBounds().getCenter());
		getOwner().translateToAbsolute(origin);

		double Ax = Math.abs(reference.preciseX - origin.preciseX);
		double Ay = Math.abs(reference.preciseY - origin.preciseY);

		double divisor = Ax + Ay;
		if (divisor == 0.0D)
			return origin;
		
		PrecisionDimension radius = new PrecisionDimension(RADIUS, RADIUS);
		
		if (reference.preciseX < origin.preciseX)
			radius.preciseWidth = 1.0D - radius.preciseWidth;
		if (reference.preciseY < origin.preciseY)
			radius.preciseHeight = 1.0D - radius.preciseHeight;
		
		getOwner().translateToAbsolute(radius);

		double x = (radius.preciseWidth * Ax) / divisor;
		double y = (radius.preciseHeight * Ay) / divisor;

		return new PrecisionPoint(origin.preciseX + x, origin.preciseY + y);
	}
}