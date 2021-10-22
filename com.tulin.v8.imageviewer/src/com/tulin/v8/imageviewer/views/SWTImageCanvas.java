package com.tulin.v8.imageviewer.views;

import java.awt.geom.AffineTransform;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseWheelListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.PaletteData;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.ScrollBar;

import com.tulin.v8.imageviewer.ImageviewPlugin;

/**
 * <p>
 * Title: Eclipse Plugin Development
 * </p>
 * <p>
 * Description: Free download
 * </p>
 * <p>
 * Copyright: Copyright (c) 2006
 * </p>
 * <p>
 * Company: Free
 * </p>
 * 
 * @author gan.shu.man
 * @version 1.0
 */

public class SWTImageCanvas extends Canvas {
	/* zooming rates in x and y direction are equal. */
	final float ZOOMIN_RATE = 1.1f; /* zoomin rate */
	final float ZOOMOUT_RATE = 0.9f; /* zoomout rate */
	private Image sourceImage; /* original image */
	private Image screenImage; /* screen image */
	private AffineTransform transform = new AffineTransform();

	final Cursor CURSOR_PALM = new Cursor(Display.getDefault(),
			ImageviewPlugin.getDefault().getImage("icons/BreakpointHS.png").getImageData(), 0, 0);
	final Cursor defCursor = getCursor();

	boolean isbigCanvas = false, mouseDown = false;
	int startX, startY, horizontalL, verticalT;

	private String currentDir = ""; /* remembering file open directory */

	public SWTImageCanvas(final Composite parent) {
		this(parent, SWT.NULL);
	}

	/**
	 * Constructor for ScrollableCanvas.
	 * 
	 * @param parent the parent of this control.
	 * @param style  the style of this control.
	 */
	public SWTImageCanvas(final Composite parent, int style) {
		super(parent, style | SWT.V_SCROLL | SWT.H_SCROLL | SWT.NO_BACKGROUND | SWT.BORDER);
		addControlListener(new ControlAdapter() { /* resize listener. */
			public void controlResized(ControlEvent event) {
				syncScrollBars();
			}
		});
		addPaintListener(new PaintListener() { /* paint listener. */
			public void paintControl(final PaintEvent event) {
				paint(event.gc);
			}
		});
		addMouseWheelListener(new MouseWheelListener() {
			@Override
			public void mouseScrolled(MouseEvent e) {
				boolean isctrl = (e.stateMask & SWT.CTRL) != 0;
				if (isctrl) {
					if (e.count > 0) {// 3向上滚轮
						zoomIn();
					} else {// -3向下滚轮
						zoomOut();
					}
				}
			}
		});
		addMouseListener(new MouseListener() {

			@Override
			public void mouseUp(MouseEvent e) {
				mouseDown = false;
			}

			@Override
			public void mouseDown(MouseEvent e) {
				mouseDown = true;
				startX = e.x;
				startY = e.y;
				horizontalL = getHorizontalBar().getSelection();
				verticalT = getVerticalBar().getSelection();
			}

			@Override
			public void mouseDoubleClick(MouseEvent arg0) {
			}
		});
		addListener(SWT.MouseMove, new Listener() {
			@Override
			public void handleEvent(Event e) {
				if (mouseDown && isbigCanvas) {
					int w = e.getBounds().x - startX;
					int h = e.getBounds().y - startY;

					ScrollBar horizontal = getHorizontalBar();
					ScrollBar vertical = getVerticalBar();

					horizontal.setSelection(horizontalL - w);
					scrollHorizontally(horizontal);

					vertical.setSelection(verticalT - h);
					scrollVertically(vertical);
				}
			}
		});
		initScrollBars();
	}

	/**
	 * Dispose the garbage here
	 */
	public void dispose() {
		if (sourceImage != null && !sourceImage.isDisposed()) {
			sourceImage.dispose();
		}
		if (screenImage != null && !screenImage.isDisposed()) {
			screenImage.dispose();
		}
	}

	/* Paint function */
	private void paint(GC gc) {
		Rectangle clientRect = getClientArea(); /* Canvas' painting area */
		if (sourceImage != null) {
			Rectangle imageRect = SWT2Dutil.inverseTransformRect(transform, clientRect);
			int gap = 2; /* find a better start point to render */
			imageRect.x -= gap;
			imageRect.y -= gap;
			imageRect.width += 2 * gap;
			imageRect.height += 2 * gap;

			Rectangle imageBound = sourceImage.getBounds();
			imageRect = imageRect.intersection(imageBound);
			Rectangle destRect = SWT2Dutil.transformRect(transform, imageRect);

			if (screenImage != null)
				screenImage.dispose();
			screenImage = new Image(getDisplay(), clientRect.width, clientRect.height);
			GC newGC = new GC(screenImage);
			newGC.setClipping(clientRect);
			newGC.drawImage(sourceImage, imageRect.x, imageRect.y, imageRect.width, imageRect.height, destRect.x,
					destRect.y, destRect.width, destRect.height);
			newGC.dispose();

			gc.drawImage(screenImage, 0, 0);
		} else {
			gc.setClipping(clientRect);
			gc.fillRectangle(clientRect);
			initScrollBars();
		}
	}

	/* Initalize the scrollbar and register listeners. */
	private void initScrollBars() {
		ScrollBar horizontal = getHorizontalBar();
		horizontal.setEnabled(false);
		horizontal.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				scrollHorizontally((ScrollBar) event.widget);
			}
		});
		ScrollBar vertical = getVerticalBar();
		vertical.setEnabled(false);
		vertical.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				scrollVertically((ScrollBar) event.widget);
			}
		});
	}

	/* Scroll horizontally */
	private void scrollHorizontally(ScrollBar scrollBar) {
		if (sourceImage == null)
			return;

		AffineTransform af = transform;
		double tx = af.getTranslateX();
		double select = -scrollBar.getSelection();
		af.preConcatenate(AffineTransform.getTranslateInstance(select - tx, 0));
		transform = af;
		syncScrollBars();
	}

	/* Scroll vertically */
	private void scrollVertically(ScrollBar scrollBar) {
		if (sourceImage == null)
			return;

		AffineTransform af = transform;
		double ty = af.getTranslateY();
		double select = -scrollBar.getSelection();
		af.preConcatenate(AffineTransform.getTranslateInstance(0, select - ty));
		transform = af;
		syncScrollBars();
	}

	/**
	 * Source image getter.
	 * 
	 * @return sourceImage.
	 */
	public Image getSourceImage() {
		return sourceImage;
	}

	/**
	 * Synchronize the scrollbar with the image. If the transform is out of range,
	 * it will correct it. This function considers only following factors :<b>
	 * transform, image size, client area</b>.
	 */
	public void syncScrollBars() {
		if (sourceImage == null) {
			redraw();
			return;
		}

		AffineTransform af = transform;
		double sx = af.getScaleX(), sy = af.getScaleY();
		double tx = af.getTranslateX(), ty = af.getTranslateY();
		if (tx > 0)
			tx = 0;
		if (ty > 0)
			ty = 0;

		ScrollBar horizontal = getHorizontalBar();
		horizontal.setIncrement((int) (getClientArea().width / 100));
		horizontal.setPageIncrement(getClientArea().width);
		Rectangle imageBound = sourceImage.getBounds();
		int cw = getClientArea().width, ch = getClientArea().height;
		if (imageBound.width * sx > cw) { /* image is wider than client area */
			horizontal.setMaximum((int) (imageBound.width * sx));
			horizontal.setEnabled(true);
			if (((int) -tx) > horizontal.getMaximum() - cw)
				tx = -horizontal.getMaximum() + cw;
		} else { /* image is narrower than client area */
			horizontal.setEnabled(false);
			tx = (cw - imageBound.width * sx) / 2; // center if too small.
		}
		horizontal.setSelection((int) (-tx));
		horizontal.setThumb((int) (getClientArea().width));

		ScrollBar vertical = getVerticalBar();
		vertical.setIncrement((int) (getClientArea().height / 100));
		vertical.setPageIncrement((int) (getClientArea().height));
		if (imageBound.height * sy > ch) { /* image is higher than client area */
			vertical.setMaximum((int) (imageBound.height * sy));
			vertical.setEnabled(true);
			if (((int) -ty) > vertical.getMaximum() - ch)
				ty = -vertical.getMaximum() + ch;
		} else { /* image is less higher than client area */
			vertical.setEnabled(false);
			ty = (ch - imageBound.height * sy) / 2; // center if too small.
		}
		vertical.setSelection((int) (-ty));
		vertical.setThumb((int) (getClientArea().height));

		/* update transform. */
		af = AffineTransform.getScaleInstance(sx, sy);
		af.preConcatenate(AffineTransform.getTranslateInstance(tx, ty));
		transform = af;

		redraw();

		if (horizontal.getEnabled() || vertical.getEnabled()) {
			setCursor(CURSOR_PALM);
			isbigCanvas = true;
		} else {
			setCursor(defCursor);
			isbigCanvas = false;
		}
	}

	/**
	 * Reload image from a file
	 * 
	 * @param filename image file
	 * @return swt image created from image file
	 */
	public Image loadImage(String filename) {
		if (sourceImage != null && !sourceImage.isDisposed()) {
			sourceImage.dispose();
			sourceImage = null;
		}
		sourceImage = new Image(getDisplay(), filename);
		showOriginal();
		return sourceImage;
	}

	/**
	 * Call back funtion of button "open". Will open a file dialog, and choose the
	 * image file. It supports image formats supported by Eclipse.
	 */
	public void onFileOpen() {
		FileDialog fileChooser = new FileDialog(getShell(), SWT.OPEN);
		fileChooser.setText("Open image file");
		fileChooser.setFilterPath(currentDir);
		fileChooser.setFilterExtensions(new String[] { "*.gif; *.jpg; *.png; *.ico; *.bmp" });
		fileChooser.setFilterNames(new String[] { "SWT image" + " (gif, jpeg, png, ico, bmp)" });
		String filename = fileChooser.open();
		if (filename != null) {
			loadImage(filename);
			currentDir = fileChooser.getFilterPath();
		}
	}

	/**
	 * Get the image data. (for future use only)
	 * 
	 * @return image data of canvas
	 */
	public ImageData getImageData() {
		return sourceImage.getImageData();
	}

	/**
	 * Reset the image data and update the image
	 * 
	 * @param data image data to be set
	 */
	public void setImageData(ImageData data) {
		if (sourceImage != null)
			sourceImage.dispose();
		if (data != null)
			sourceImage = new Image(getDisplay(), data);
		syncScrollBars();
	}

	/**
	 * 自动大小<br>
	 * 当图片大于画板时：自适应画板<br>
	 * 当图片小于画板时：显示原始大小
	 */
	public void autoSize() {
		if (sourceImage != null) {
			Rectangle ibound = sourceImage.getBounds();
			Rectangle pbound = getBounds();
			if (ibound.width > pbound.width || ibound.height > pbound.height) {
				fitCanvas();
			} else {
				showOriginal();
			}
		}
	}

	/**
	 * Fit the image onto the canvas
	 */
	public void fitCanvas() {
		if (sourceImage == null)
			return;
		Rectangle imageBound = sourceImage.getBounds();
		Rectangle destRect = getClientArea();
		double sx = (double) destRect.width / (double) imageBound.width;
		double sy = (double) destRect.height / (double) imageBound.height;
		double s = Math.min(sx, sy);
		double dx = 0.5 * destRect.width;
		double dy = 0.5 * destRect.height;
		centerZoom(dx, dy, s, new AffineTransform());
	}

	/**
	 * Show the image with the original size
	 */
	public void showOriginal() {
		if (sourceImage == null)
			return;
		transform = new AffineTransform();
		syncScrollBars();
	}

	/**
	 * Perform a zooming operation centered on the given point (dx, dy) and using
	 * the given scale factor. The given AffineTransform instance is
	 * preconcatenated.
	 * 
	 * @param dx    center x
	 * @param dy    center y
	 * @param scale zoom rate
	 * @param af    original affinetransform
	 */
	public void centerZoom(double dx, double dy, double scale, AffineTransform af) {
		af.preConcatenate(AffineTransform.getTranslateInstance(-dx, -dy));
		af.preConcatenate(AffineTransform.getScaleInstance(scale, scale));
		af.preConcatenate(AffineTransform.getTranslateInstance(dx, dy));
		transform = af;
		syncScrollBars();
	}

	/**
	 * Zoom in around the center of client Area.
	 */
	public void zoomIn() {
		if (sourceImage == null)
			return;
		Rectangle rect = getClientArea();
		int w = rect.width, h = rect.height;
		double dx = ((double) w) / 2;
		double dy = ((double) h) / 2;
		centerZoom(dx, dy, ZOOMIN_RATE, transform);
	}

	/**
	 * Zoom out around the center of client Area.
	 */
	public void zoomOut() {
		if (sourceImage == null)
			return;
		Rectangle rect = getClientArea();
		int w = rect.width, h = rect.height;
		double dx = ((double) w) / 2;
		double dy = ((double) h) / 2;
		centerZoom(dx, dy, ZOOMOUT_RATE, transform);
	}

	public void rotateLeft() {
		/* rotate image anti-clockwise */
		ImageData src = getImageData();
		if (src == null)
			return;
		PaletteData srcPal = src.palette;
		PaletteData destPal;
		ImageData dest;
		/* construct a new ImageData */
		if (srcPal.isDirect) {
			destPal = new PaletteData(srcPal.redMask, srcPal.greenMask, srcPal.blueMask);
		} else {
			destPal = new PaletteData(srcPal.getRGBs());
		}
		dest = new ImageData(src.height, src.width, src.depth, destPal);
		/* rotate by rearranging the pixels */
		for (int i = 0; i < src.width; i++) {
			for (int j = 0; j < src.height; j++) {
				int pixel = src.getPixel(i, j);
				dest.setPixel(j, src.width - 1 - i, pixel);
			}
		}
		setImageData(dest);
	}

	public void rotateRight() {
		/* rotate image anti-clockwise */
		ImageData src = getImageData();
		if (src == null)
			return;
		PaletteData srcPal = src.palette;
		PaletteData destPal;
		ImageData dest;
		/* construct a new ImageData */
		if (srcPal.isDirect) {
			destPal = new PaletteData(srcPal.redMask, srcPal.greenMask, srcPal.blueMask);
		} else {
			destPal = new PaletteData(srcPal.getRGBs());
		}
		dest = new ImageData(src.height, src.width, src.depth, destPal);
		/* rotate by rearranging the pixels */
		for (int i = 0; i < src.width; i++) {
			for (int j = 0; j < src.height; j++) {
				int pixel = src.getPixel(i, j);
				dest.setPixel(src.height - 1 - j, i, pixel);
			}
		}
		setImageData(dest);
	}
}
