package com.tulin.v8.ide.ui.editors.page.design.figures;

import org.eclipse.draw2d.*;
import org.eclipse.draw2d.geometry.*;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

import com.tulin.v8.ide.ui.editors.page.design.anchors.MarriageAnchor;

/**
 * A custom figure for the GenealogyView visually linking related people
 */
public class MarriageFigure extends PolygonShape {
	public static final int RADIUS = 26;
	private static final PointList ARROWHEAD = new PointList(new int[] { 0, 0,
			-2, 2, -2, 0, -2, -2, 0, 0 });
	
	public static final Image MARRIAGE_IMAGE = new Image(Display.getCurrent(),
		MarriageFigure.class.getResourceAsStream("marriage.png"));
	public static final Image CONNECTION_IMAGE = new Image(Display.getCurrent(),
		MarriageFigure.class.getResourceAsStream("connection.png"));

	private final Label yearMarriedFigure;

	public MarriageFigure(int year) {
		Rectangle r = new Rectangle(0, 0, 50, 50);
		setStart(r.getTop());
		addPoint(r.getTop());
		addPoint(r.getLeft());
		addPoint(r.getBottom());
		addPoint(r.getRight());
		addPoint(r.getTop());
		setEnd(r.getTop());
		setFill(true);
		setBackgroundColor(ColorConstants.lightGray);
		// Add 1 to include width of the border
		// otherwise the diamond's right and bottom tips are missing 1 pixel
		setPreferredSize(r.getSize().expand(1, 1));

		setLayoutManager(new StackLayout());
		yearMarriedFigure = new Label(Integer.toString(year)) {
			public boolean containsPoint(int x, int y) {
				return false;
			}
		};
		add(yearMarriedFigure);
	}

	public void setYearMarried(int yearMarried) {
		yearMarriedFigure.setText(Integer.toString(yearMarried));
	}

	/**
	 * Adjust the receiver's appearance based upon whether the receiver is selected
	 * 
	 * @param selected <code>true</code> if the receiver is selected, else
	 *            <code>false</code>
	 */
	public void setSelected(boolean selected) {
		setForegroundColor(selected ? ColorConstants.blue : ColorConstants.black);
		setLineWidth(selected ? 2 : 1);
		erase();
	}

	/**
	 * Connect a "parent" to the receiver. This method is indirectly used by
	 * {@link GenealogyView}, but not not necesary for either {@link GenealogyViewGEF} or
	 * {@link GenealogyGraphEditor}.
	 * 
	 * @param figure the parent
	 * @return the connection
	 */
	public PolylineConnection addParent(IFigure figure) {
		PolylineConnection connection = new PolylineConnection();
		connection.setSourceAnchor(new ChopboxAnchor(figure));
		connection.setTargetAnchor(new MarriageAnchor(this));

		// Add an arrowhead decoration
		PolygonDecoration decoration = new PolygonDecoration();
		decoration.setTemplate(ARROWHEAD);
		decoration.setBackgroundColor(ColorConstants.darkGray);
		connection.setTargetDecoration(decoration);

		return connection;
	}

	/**
	 * Connect a "child" to the receiver. This method is indirectly used by
	 * {@link GenealogyView}, but not not necesary for either {@link GenealogyViewGEF} or
	 * {@link GenealogyGraphEditor}.
	 * 
	 * @param figure the child
	 * @return the connection
	 */
	public PolylineConnection addChild(IFigure figure) {
		PolylineConnection connection = new PolylineConnection();
		connection.setSourceAnchor(new MarriageAnchor(this));
		connection.setTargetAnchor(new ChopboxAnchor(figure));

		// Add an arrowhead decoration
		PolygonDecoration decoration = new PolygonDecoration();
		decoration.setTemplate(ARROWHEAD);
		decoration.setBackgroundColor(ColorConstants.white);
		connection.setTargetDecoration(decoration);

		return connection;
	}
}