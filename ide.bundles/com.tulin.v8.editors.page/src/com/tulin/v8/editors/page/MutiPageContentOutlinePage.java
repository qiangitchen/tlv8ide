package com.tulin.v8.editors.page;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.part.IPageSite;
import org.eclipse.ui.part.Page;
import org.eclipse.ui.part.PageBook;
import org.eclipse.ui.views.contentoutline.IContentOutlinePage;

public class MutiPageContentOutlinePage extends Page implements IContentOutlinePage {
	private PageBook jdField_a_of_type_OrgEclipseUiPartPageBook;
	private IContentOutlinePage jdField_a_of_type_OrgEclipseUiViewsContentoutlineIContentOutlinePage;
	private boolean jdField_a_of_type_Boolean;

	public MutiPageContentOutlinePage() {
	}

	public void createControl(Composite paramComposite) {
		try {
			this.jdField_a_of_type_OrgEclipseUiPartPageBook = new PageBook(paramComposite, 0);
			if (this.jdField_a_of_type_OrgEclipseUiViewsContentoutlineIContentOutlinePage != null)
				setPageActive(this.jdField_a_of_type_OrgEclipseUiViewsContentoutlineIContentOutlinePage);
		} catch (Exception e) {
		}
	}

	public void dispose() {
		try {
			if ((this.jdField_a_of_type_OrgEclipseUiPartPageBook != null)
					&& (!this.jdField_a_of_type_OrgEclipseUiPartPageBook.isDisposed()))
				this.jdField_a_of_type_OrgEclipseUiPartPageBook.dispose();
			this.jdField_a_of_type_OrgEclipseUiPartPageBook = null;
			this.jdField_a_of_type_Boolean = true;
		} catch (Exception e) {
		}
	}

	public boolean isDisposed() {
		return this.jdField_a_of_type_Boolean;
	}

	public Control getControl() {
		return this.jdField_a_of_type_OrgEclipseUiPartPageBook;
	}

	public void setActionBars(IActionBars paramIActionBars) {
	}

	public void setFocus() {
		try {
			if (this.jdField_a_of_type_OrgEclipseUiViewsContentoutlineIContentOutlinePage != null)
				this.jdField_a_of_type_OrgEclipseUiViewsContentoutlineIContentOutlinePage.setFocus();
		} catch (Exception e) {
		}
	}

	public void addSelectionChangedListener(ISelectionChangedListener paramISelectionChangedListener) {
		try {
			if (this.jdField_a_of_type_OrgEclipseUiViewsContentoutlineIContentOutlinePage != null)
				this.jdField_a_of_type_OrgEclipseUiViewsContentoutlineIContentOutlinePage
						.addSelectionChangedListener(paramISelectionChangedListener);
		} catch (Exception e) {
		}
	}

	public ISelection getSelection() {
		try {
			return this.jdField_a_of_type_OrgEclipseUiViewsContentoutlineIContentOutlinePage != null
					? this.jdField_a_of_type_OrgEclipseUiViewsContentoutlineIContentOutlinePage.getSelection()
					: null;
		} catch (Exception e) {
		}
		return null;
	}

	public void removeSelectionChangedListener(ISelectionChangedListener paramISelectionChangedListener) {
		try {
			if (this.jdField_a_of_type_OrgEclipseUiViewsContentoutlineIContentOutlinePage != null) {
				this.jdField_a_of_type_OrgEclipseUiViewsContentoutlineIContentOutlinePage
						.removeSelectionChangedListener(paramISelectionChangedListener);
			}
		} catch (Exception e) {
		}
	}

	public void setSelection(ISelection paramISelection) {
		try {
			if (this.jdField_a_of_type_OrgEclipseUiViewsContentoutlineIContentOutlinePage != null) {
				this.jdField_a_of_type_OrgEclipseUiViewsContentoutlineIContentOutlinePage.setSelection(paramISelection);
			}
		} catch (Exception e) {
		}
	}

	public void setPageActive(IContentOutlinePage paramIContentOutlinePage) {
		this.jdField_a_of_type_OrgEclipseUiViewsContentoutlineIContentOutlinePage = paramIContentOutlinePage;
		// getSite().getActionBars().getToolBarManager().removeAll();
		// getSite().getActionBars().getMenuManager().removeAll();
		try {
			if (getSite() != null) {
				Control localControl = paramIContentOutlinePage.getControl();
				if ((localControl == null) || (localControl.isDisposed())) {
					paramIContentOutlinePage.createControl(this.jdField_a_of_type_OrgEclipseUiPartPageBook);
					localControl = paramIContentOutlinePage.getControl();
				}
				getSite().getActionBars().updateActionBars();
				this.jdField_a_of_type_OrgEclipseUiPartPageBook.showPage(localControl);
			}
		} catch (Exception e) {
		}
	}

	public PageBook getPagebook() {
		return this.jdField_a_of_type_OrgEclipseUiPartPageBook;
	}

	public void init(IPageSite paramIPageSite) {
		super.init(paramIPageSite);
	}
}