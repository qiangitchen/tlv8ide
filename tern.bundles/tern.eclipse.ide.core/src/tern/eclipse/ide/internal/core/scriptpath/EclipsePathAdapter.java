package tern.eclipse.ide.internal.core.scriptpath;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import minimatch.Options;
import minimatch.PathAdapter;

import org.eclipse.core.runtime.IPath;

/**
 * Minimatch path adapter to use Minimatch with Eclipse {@link IPath}.
 *
 */
public class EclipsePathAdapter implements PathAdapter<IPath> {

	public static final PathAdapter<IPath> INSTANCE = new EclipsePathAdapter();

	@Override
	public List<String> toArray(IPath path, Options options) {
		List<String> result = new ArrayList<String>();
		if (path.isAbsolute()) {
			result.add(""); //$NON-NLS-1$
		}
		result.addAll(Arrays.asList(path.segments()));
		if (path.hasTrailingSeparator()) {
			result.add(""); //$NON-NLS-1$
		}
		return result;
	}

}
