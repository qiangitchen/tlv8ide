package tern.server.protocol.completions;

/**
 * Function parameter.
 *
 */
public class Parameter {

	private final String name;
	private final boolean required;
	private final String type;

	public Parameter(String name, boolean required, String type) {
		this.name = name;
		this.required = required;
		this.type = type;
	}

	/**
	 * Returns the name of the parameter.
	 * 
	 * @return the name of the parameter.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Returns true is parameter is required and false otherwise.
	 * 
	 * @return true is parameter is required and false otherwise.
	 */
	public boolean isRequired() {
		return required;
	}

	/**
	 * Returns the type of the parameter.
	 * 
	 * @return the type of the parameter.
	 */
	public String getType() {
		return type;
	}

	/**
	 * Returns true if parameter type is a function and false otherwise.
	 * 
	 * @return true if parameter type is a function and false otherwise.
	 */
	public boolean isFunction() {
		return TernTypeHelper.isFunction(type);
	}

	/**
	 * Returns the function information of parameter is a function and null
	 * otherwise.
	 * 
	 * @return the function information of parameter is a function and null
	 *         otherwise.
	 */
	public FunctionInfo getInfo() {
		return TernTypeHelper.parseFunction(null, type);
	}
}
