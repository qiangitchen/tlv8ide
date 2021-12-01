package com.tulin.v8.imageviewer.editors;

import java.io.File;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.EditorPart;
import org.eclipse.ui.part.FileEditorInput;

import com.tulin.v8.imageviewer.ImageviewPlugin;
import com.tulin.v8.imageviewer.views.SWTImageCanvas;

public class ImageViewEditor extends EditorPart {

	private SWTImageCanvas imageCanvas;

	public ImageViewEditor() {
		super();
	}

	@Override
	public void doSave(IProgressMonitor monitor) {
		// TODO 自动生成的方法存根

	}

	@Override
	public void doSaveAs() {
		// TODO 自动生成的方法存根

	}

	@Override
	public void init(IEditorSite site, IEditorInput input) throws PartInitException {
		setSite(site);
		setInput(input);
	}

	@Override
	public boolean isDirty() {
		// TODO 自动生成的方法存根
		return false;
	}

	@Override
	public boolean isSaveAsAllowed() {
		// TODO 自动生成的方法存根
		return false;
	}

	@Override
	public void createPartControl(Composite parent) {
		// Display display = parent.getDisplay();
		// Image image = new Image(display, getFile().getAbsolutePath());
		// final int imgwidth = image.getBounds().width;
		// final int imgheight = image.getBounds().height;
		// ScrolledComposite sc = new ScrolledComposite(parent, SWT.V_SCROLL |
		// SWT.H_SCROLL);
		// sc.setBackground(display.getSystemColor(SWT.COLOR_WHITE));
		// ImageCanvas imgl = new ImageCanvas(sc);
		// imgl.setBackground(display.getSystemColor(SWT.COLOR_GRAY));
		// imgl.setSize(imgwidth, imgheight);
		// imgl.setImage(image);
		// sc.setContent(imgl);

		Composite form = new Composite(parent, SWT.FILL);
		form.setLayout(new GridLayout());

		ToolBar toolbar = new ToolBar(form, SWT.FLAT | SWT.WRAP | SWT.RIGHT|SWT.FILL);
		toolbar.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		ToolItem zoomInitem = new ToolItem(toolbar, SWT.PUSH);
		zoomInitem.setImage(ImageviewPlugin.getDefault().getImage("/icons/ZoomIn16.png"));
		zoomInitem.setText(ImageviewPlugin.getPropertiesString("item.ZoomIn"));
		zoomInitem.setToolTipText(ImageviewPlugin.getPropertiesString("item.ZoomIn"));
		zoomInitem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				imageCanvas.zoomIn();
			}
		});

		ToolItem zoomOutitem = new ToolItem(toolbar, SWT.PUSH);
		zoomOutitem.setImage(ImageviewPlugin.getDefault().getImage("/icons/ZoomOut16.png"));
		zoomOutitem.setText(ImageviewPlugin.getPropertiesString("item.ZoomOut"));
		zoomOutitem.setToolTipText(ImageviewPlugin.getPropertiesString("item.ZoomOut"));
		zoomOutitem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				imageCanvas.zoomOut();
			}
		});

		ToolItem FitWindow = new ToolItem(toolbar, SWT.PUSH);
		FitWindow.setImage(ImageviewPlugin.getDefault().getImage("/icons/Fit16.png"));
		FitWindow.setText(ImageviewPlugin.getPropertiesString("item.Fit"));
		FitWindow.setToolTipText(ImageviewPlugin.getPropertiesString("item.Fit"));
		FitWindow.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				imageCanvas.fitCanvas();
			}
		});

		ToolItem OriginalSize = new ToolItem(toolbar, SWT.PUSH);
		OriginalSize.setImage(ImageviewPlugin.getDefault().getImage("/icons/Original16.png"));
		OriginalSize.setText(ImageviewPlugin.getPropertiesString("item.Original"));
		OriginalSize.setToolTipText(ImageviewPlugin.getPropertiesString("item.Original"));
		OriginalSize.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				imageCanvas.showOriginal();
			}
		});

		ToolItem Rotate = new ToolItem(toolbar, SWT.PUSH);
		Rotate.setImage(ImageviewPlugin.getDefault().getImage("/icons/Rotate16.png"));
		Rotate.setText(ImageviewPlugin.getPropertiesString("item.Rotate"));
		Rotate.setToolTipText(ImageviewPlugin.getPropertiesString("item.Rotate"));
		Rotate.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				imageCanvas.rotateLeft();
			}
		});
		
		ToolItem rRotate = new ToolItem(toolbar, SWT.PUSH);
		rRotate.setImage(ImageviewPlugin.getDefault().getImage("/icons/rRotate16.png"));
		rRotate.setText(ImageviewPlugin.getPropertiesString("item.Rotater"));
		rRotate.setToolTipText(ImageviewPlugin.getPropertiesString("item.Rotater"));
		rRotate.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				imageCanvas.rotateRight();
			}
		});

		imageCanvas = new SWTImageCanvas(form);
		imageCanvas.setLayoutData(new GridData(GridData.FILL_BOTH));
		imageCanvas.loadImage(getFile().getAbsolutePath());

		setPartName(getEditorInput().getName());
	}

	@Override
	public void setFocus() {
		// TODO 自动生成的方法存根
	}

	public File getFile() {
		if (this.getEditorInput() instanceof FileEditorInput) {
			FileEditorInput fileeditorinput = (FileEditorInput) this.getEditorInput();
			IFile file = fileeditorinput.getFile();
			return file.getLocation().makeAbsolute().toFile();
		} else {
			File fle = new File(this.getEditorInput().getToolTipText());
			return fle;
		}

	}

}
