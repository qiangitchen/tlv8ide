package com.tulin.v8.ide.views.navigator.filters;

import org.eclipse.core.resources.IProject;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.team.core.RepositoryProvider;

public class NonSharedProjectFilter extends ViewerFilter
{
  public boolean select(Viewer viewer, Object parent, Object element)
  {
    if ((element instanceof IProject)) {
      return isSharedProject((IProject)element);
    }
    if ((element instanceof IJavaProject)) {
      return isSharedProject(((IJavaProject)element).getProject());
    }
    return true;
  }

  private boolean isSharedProject(IProject project) {
    return (!project.isAccessible()) || (RepositoryProvider.isShared(project));
  }
}
