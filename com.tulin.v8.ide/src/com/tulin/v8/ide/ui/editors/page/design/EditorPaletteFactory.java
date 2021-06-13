package com.tulin.v8.ide.ui.editors.page.design;

import org.eclipse.gef.palette.MarqueeToolEntry;
import org.eclipse.gef.palette.PaletteDrawer;
import org.eclipse.gef.palette.PaletteEntry;
import org.eclipse.gef.palette.PaletteRoot;
import org.eclipse.gef.palette.PaletteToolbar;
import org.eclipse.gef.palette.PanningSelectionToolEntry;
import org.eclipse.gef.palette.ToolEntry;

public class EditorPaletteFactory {
	/**
	 * 设计器 组件工厂
	 */
	public static PaletteRoot createPalette() {
		PaletteRoot palette = new PaletteRoot();
		palette.add(createToolsGroup(palette));
		palette.add(createElementsDrawer());
		return palette;
	}

	private static PaletteEntry createToolsGroup(PaletteRoot palette) {
		PaletteToolbar toolbar = new PaletteToolbar("Tools");

		// 添加选择工具组
		ToolEntry tool = new PanningSelectionToolEntry();
		toolbar.add(tool);
		palette.setDefaultEntry(tool);

		// 将工具对象添加到工具组中
		toolbar.add(new MarqueeToolEntry());

		return toolbar;
	}

	private static PaletteEntry createElementsDrawer() {

		PaletteDrawer componentsDrawer = new PaletteDrawer("Elements");


		return componentsDrawer;
	}
}
