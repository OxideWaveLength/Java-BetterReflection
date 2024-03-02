package top.wavelength.betterreflection.dumper.implementation;

import top.wavelength.betterreflection.dumper.Dumper;
import top.wavelength.betterreflection.dumper.TypeDisplayNameFormat;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;

/**
 * The MethodDumper class is responsible for dumping information related to a method.
 * It extends the Dumper class and operates on an object of type {@link Method}.
 * It provides methods to dump the method's information in a formatted string representation.
 *
 * @since 1.2
 */
public class MethodDumper implements Dumper<Method> {

	private boolean includeModifiers = true;
	private TypeDisplayNameFormat parameterTypeDisplay = TypeDisplayNameFormat.FULL_NAME;
	private TypeDisplayNameFormat returnTypeDisplay = TypeDisplayNameFormat.FULL_NAME;

	/**
	 * Returns whether to include modifiers in the dumped string representation of a Method object.
	 *
	 * @return true if modifiers should be included, false otherwise
	 * @since 1.2
	 */
	public boolean isIncludeModifiers() {
		return includeModifiers;
	}

	/**
	 * Sets whether to include modifiers in the dumped string representation of a Method object.
	 *
	 * @param includeModifiers true if modifiers should be included, false otherwise
	 * @return the MethodDumper instance
	 * @since 1.2
	 */
	public MethodDumper setIncludeModifiers(boolean includeModifiers) {
		this.includeModifiers = includeModifiers;
		return this;
	}

	/**
	 * Gets the display format for the parameter types of the Method.
	 *
	 * @return the display format for the parameter types
	 * @since 1.2
	 */
	public TypeDisplayNameFormat getParameterTypeDisplay() {
		return parameterTypeDisplay;
	}

	/**
	 * Sets the display format for the parameter types of the Method.
	 *
	 * @param parameterTypeDisplay the display format for the parameter types
	 * @return the MethodDumper instance
	 * @since 1.2
	 */
	public MethodDumper setParameterTypeDisplay(TypeDisplayNameFormat parameterTypeDisplay) {
		this.parameterTypeDisplay = parameterTypeDisplay;
		return this;
	}

	/**
	 * Gets the display format for the return type of the Method.
	 *
	 * @return the display format for the Method's return type
	 * @since 1.2
	 */
	public TypeDisplayNameFormat getReturnTypeDisplay() {
		return returnTypeDisplay;
	}

	/**
	 * Sets the display format for the return type of the Method.
	 *
	 * @param returnType the display format for the return type
	 * @return the MethodDumper instance
	 * @since 1.2
	 */
	public MethodDumper setReturnTypeDisplay(TypeDisplayNameFormat returnType) {
		this.returnTypeDisplay = returnType;
		return this;
	}

	/**
	 * Returns a formatted string representation of a Method.
	 *
	 * @param method The Method to dump
	 * @return The formatted string representation of the Method
	 * @since 1.2
	 */
	@Override
	public String dump(Method method) {
		StringBuilder parameters = new StringBuilder();
		if (parameterTypeDisplay != TypeDisplayNameFormat.NONE) {
			for (Parameter parameter : method.getParameters()) {
				if (parameters.length() != 0)
					parameters.append(", ");
				parameters.append(parameterTypeDisplay.getName(parameter));
			}
		}
		return String.format("%s%s%s(%s)", includeModifiers ? Modifier.toString(method.getModifiers()) + " " : "",
				returnTypeDisplay != TypeDisplayNameFormat.NONE ? returnTypeDisplay.getName(method) + " " : "",
				method.getName(), parameters);
	}

}