package com.tulin.v8.ide.ui.editors.page.design;

import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

public class BorderLayout extends AWTLayout {
	public static final String CENTER = "Center";
	public static final String EAST = "East";
	public static final String NORTH = "North";
	public static final String SOUTH = "South";
	public static final String WEST = "West";
	private int jdField_a_of_type_Int;
	private int jdField_b_of_type_Int;
	private Control jdField_a_of_type_OrgEclipseSwtWidgetsControl;
	private Control jdField_b_of_type_OrgEclipseSwtWidgetsControl;
	private Control c;
	private Control d;
	private Control e;

	public BorderLayout() {
	}

	public BorderLayout(int paramInt1, int paramInt2) {
		this.jdField_a_of_type_Int = paramInt1;
		this.jdField_b_of_type_Int = paramInt2;
	}

	protected Point computeSize(Composite paramComposite, int paramInt1,
			int paramInt2, boolean paramBoolean) {
		a(paramComposite);
		Point localPoint1 = new Point(0, 0);
		Point localPoint2;
		if (this.c != null) {
			localPoint2 = getPreferredSize(this.c, paramInt1, -1, paramBoolean);
			localPoint1.y += localPoint2.y + this.jdField_b_of_type_Int;
		}
		if (this.d != null) {
			localPoint2 = getPreferredSize(this.d, paramInt1, -1, paramBoolean);
			localPoint1.y += localPoint2.y + this.jdField_b_of_type_Int;
		}
		if (this.e != null) {
			localPoint2 = getPreferredSize(this.e, -1, paramInt2, paramBoolean);
			localPoint1.x += localPoint2.x + this.jdField_a_of_type_Int;
		}
		if (this.jdField_b_of_type_OrgEclipseSwtWidgetsControl != null) {
			localPoint2 = getPreferredSize(
					this.jdField_b_of_type_OrgEclipseSwtWidgetsControl, -1,
					paramInt2, paramBoolean);
			localPoint1.x += localPoint2.x + this.jdField_a_of_type_Int;
		}
		if (this.jdField_a_of_type_OrgEclipseSwtWidgetsControl != null) {
			localPoint2 = getPreferredSize(
					this.jdField_a_of_type_OrgEclipseSwtWidgetsControl,
					paramInt1, paramInt2, paramBoolean);
			localPoint1.x += localPoint2.x;
			localPoint1.y += localPoint2.y;
		}
		return localPoint1;
	}

	protected void layout(Composite paramComposite, boolean paramBoolean) {
		a(paramComposite);
		Rectangle localRectangle = paramComposite.getClientArea();
		int i = localRectangle.y;
		int j = localRectangle.y + localRectangle.height;
		int k = localRectangle.x;
		int m = localRectangle.x + localRectangle.width;
		Point localPoint;
		if (this.c != null) {
			localPoint = getPreferredSize(this.c, localRectangle.width, -1,
					paramBoolean);
			this.c.setBounds(k, i, m - k, localPoint.y);
			i += localPoint.y + this.jdField_b_of_type_Int;
		}
		if (this.d != null) {
			localPoint = getPreferredSize(this.d, localRectangle.width, -1,
					paramBoolean);
			this.d.setBounds(k, j - localPoint.y, m - k, localPoint.y);
			j -= localPoint.y + this.jdField_b_of_type_Int;
		}
		if (this.e != null) {
			localPoint = getPreferredSize(this.e, -1, j - i, paramBoolean);
			this.e.setBounds(k, i, localPoint.x, j - i);
			k += localPoint.x + this.jdField_a_of_type_Int;
		}
		if (this.jdField_b_of_type_OrgEclipseSwtWidgetsControl != null) {
			localPoint = getPreferredSize(
					this.jdField_b_of_type_OrgEclipseSwtWidgetsControl, -1, j
							- i, paramBoolean);
			this.jdField_b_of_type_OrgEclipseSwtWidgetsControl.setBounds(m
					- localPoint.x, i, localPoint.x, j - i);
			m -= localPoint.x + this.jdField_a_of_type_Int;
		}
		if (this.jdField_a_of_type_OrgEclipseSwtWidgetsControl != null)
			this.jdField_a_of_type_OrgEclipseSwtWidgetsControl.setBounds(k, i,
					m - k, j - i);
	}

	private void a(Composite paramComposite) {
		this.c = (this.d = this.jdField_b_of_type_OrgEclipseSwtWidgetsControl = this.e = this.jdField_a_of_type_OrgEclipseSwtWidgetsControl = null);
		Control[] arrayOfControl = paramComposite.getChildren();
		for (int i = 0; i < arrayOfControl.length; i++) {
			Object localObject = arrayOfControl[i].getLayoutData();
			if ("North".equals(localObject))
				this.c = arrayOfControl[i];
			else if ("South".equals(localObject))
				this.d = arrayOfControl[i];
			else if ("East".equals(localObject))
				this.jdField_b_of_type_OrgEclipseSwtWidgetsControl = arrayOfControl[i];
			else if ("West".equals(localObject))
				this.e = arrayOfControl[i];
			else
				this.jdField_a_of_type_OrgEclipseSwtWidgetsControl = arrayOfControl[i];
		}
	}

	public int getHgap() {
		return this.jdField_a_of_type_Int;
	}

	public void setHgap(int paramInt) {
		this.jdField_a_of_type_Int = paramInt;
	}

	public int getVgap() {
		return this.jdField_b_of_type_Int;
	}

	public void setVgap(int paramInt) {
		this.jdField_b_of_type_Int = paramInt;
	}
}
