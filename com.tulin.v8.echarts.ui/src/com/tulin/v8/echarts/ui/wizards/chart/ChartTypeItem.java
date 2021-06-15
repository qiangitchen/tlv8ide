package com.tulin.v8.echarts.ui.wizards.chart;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseTrackAdapter;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;

public class ChartTypeItem extends Composite {

	Label label;
	Label image;

	MouseListener mouselistener;

	private enum State {
		ACTIVE(150), DOWN(255), UNACTIVE(50);
		private State(int alpha) {
			this.alpha = alpha;
		}

		int alpha;
	}

	/**
	 * 按钮激活标志
	 */
	private State active = State.UNACTIVE;

	public ChartTypeItem(Composite parent, int style) {
		super(parent, style | SWT.BORDER);
		setLayout(new GridLayout(1, false));
		label = new Label(this, SWT.CENTER);
		label.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		image = new Label(this, SWT.CENTER);
		image.setLayoutData(new GridData());

		// 鼠标进入离开时修改激活标志并重绘窗口
		MouseTrackAdapter mousetrack = new MouseTrackAdapter() {
			final Cursor defCursor = getCursor();

			@Override
			public void mouseEnter(MouseEvent e) {
				active = State.ACTIVE;
				setCursor(Display.getCurrent().getSystemCursor(SWT.CURSOR_HAND));
				redraw();
			}

			@Override
			public void mouseExit(MouseEvent e) {
				active = State.UNACTIVE;
				setCursor(defCursor);
				redraw();
			}
		};
		addMouseTrackListener(mousetrack);
		label.addMouseTrackListener(mousetrack);
		image.addMouseTrackListener(mousetrack);

		addPaintListener(new PaintListener() {
			@Override
			public void paintControl(PaintEvent e) {
				boolean isAdvanced = e.gc.getAdvanced();
				try {
					if (!isAdvanced)
						e.gc.setAdvanced(true);
					e.gc.setAntialias(SWT.ON);
					// 激活时设置alpha参数以区分按钮状态
					e.gc.setAlpha(active.alpha);
				} finally {
					if (!isAdvanced)
						e.gc.setAdvanced(isAdvanced);
				}
			}
		});

		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				if (1 == e.button) {
					active = State.DOWN;
					redraw();
				}
			}

			@Override
			public void mouseUp(MouseEvent e) {
				if (1 == e.button) {
					active = State.ACTIVE;
					redraw();
				}
			}
		});
	}

	public void setText(String text) {
		label.setText(text);
	}

	public void setImage(Image img) {
		image.setImage(img);
	}

	public String getText() {
		return label.getText();
	}

	public void setSelected(boolean flag) {
		if (flag) {
			setBackground(new Color(getDisplay(), new RGB(255, 255, 255)));
			setForeground(new Color(getDisplay(), new RGB(255, 255, 255)));
		} else {
			setBackground(null);
			setForeground(null);
		}
		redraw();
	}

	public void addMouseListener(MouseListener mouselistener) {
		this.mouselistener = mouselistener;
		label.addMouseListener(mouselistener);
		image.addMouseListener(mouselistener);
	}

}
