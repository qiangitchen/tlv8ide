package com.tulin.v8.echarts.ui.wizards.chart;

import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;

public abstract class ChartTypeSelectListener implements MouseListener {
	ChartTypeItem item;

	public ChartTypeSelectListener(ChartTypeItem item) {
		this.item = item;
		item.addMouseListener(this);
	}

	@Override
	public void mouseUp(MouseEvent e) {
		// TODO 自动生成的方法存根
		// System.out.println("mouseUp");
	}

	@Override
	public void mouseDown(MouseEvent e) {
		// TODO 自动生成的方法存根
		// System.out.println("mouseDown");
		item.setSelected(true);
		selected(item);
	}

	@Override
	public void mouseDoubleClick(MouseEvent e) {
		// TODO 自动生成的方法存根
	}

	public abstract void selected(ChartTypeItem item);
}
