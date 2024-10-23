package tern.scriptpath.impl;

import tern.ITernProject;
import tern.scriptpath.ITernScriptPath;
import tern.utils.StringUtils;

/**
 * Base class for {@link ITernScriptPath}
 * 
 */
public abstract class AbstractTernScriptPath implements ITernScriptPath {

	private final ITernProject project;
	private final ScriptPathsType type;
	private final boolean external;
	private final String externalLabel;

	public AbstractTernScriptPath(ITernProject project, ScriptPathsType type,
			String externalLabel) {
		this.project = project;
		this.type = type;
		this.external = !StringUtils.isEmpty(externalLabel);
		this.externalLabel = externalLabel;
	}

	@Override
	public ITernProject getOwnerProject() {
		return project;
	}

	@Override
	public ScriptPathsType getType() {
		return type;
	}

	@Override
	public boolean isExternal() {
		return external;
	}

	@Override
	public String getExternalLabel() {
		return externalLabel;
	}

	@Override
	public int hashCode() {
		return project.hashCode()*31 + 
				(externalLabel == null ? 0 : externalLabel.hashCode());
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof AbstractTernScriptPath) {
			AbstractTernScriptPath sp = (AbstractTernScriptPath) obj;
			return project.equals(sp.project) &&
					type == sp.type &&
					((externalLabel == null && sp.externalLabel == null) ||
							(externalLabel != null && externalLabel.equals(sp.externalLabel)));
		}
		return super.equals(obj);
	}
	
}
