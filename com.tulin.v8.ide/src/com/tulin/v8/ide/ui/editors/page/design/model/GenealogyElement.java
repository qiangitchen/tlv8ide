package com.tulin.v8.ide.ui.editors.page.design.model;

/**
 * An element of {@link GenealogyGraph} that has location and size.
 * This is the abstract superclass of {@link Person}, {@link Marriage}, and {@link Note}.
 */
public abstract class GenealogyElement
{
	private int x, y, width, height;

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public boolean setLocation(int newX, int newY) {
		if (x == newX && y == newY)
			return false;
		x = newX;
		y = newY;
		fireLocationChanged(x, y);
		return true;
	}

	protected abstract void fireLocationChanged(int newX, int newY);
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public boolean setSize(int newWidth, int newHeight) {
		if (width == newWidth && height == newHeight)
			return false;
		width = newWidth;
		height = newHeight;
		fireSizeChanged(width, height);
		return true;
	}

	protected abstract void fireSizeChanged(int newWidth, int newHeight);
}
