package top.wavelength.betterreflection.dumper.implementation;

import top.wavelength.betterreflection.dumper.Dumper;
import top.wavelength.betterreflection.dumper.TypeDisplayNameFormat;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;

/**
 * The ConstructorDumper class is responsible for dumping the information related to a constructor.
 * It extends the Dumper class, and operates on an object of type {@link Constructor}.
 * It provides methods to dump the constructor's information in a formatted string representation.
 *
 * @since 1.2
 */
public class ConstructorDumper implements Dumper<Constructor<?>> {

	private boolean includeModifiers = true;
	private TypeDisplayNameFormat parameterTypeDisplay = TypeDisplayNameFormat.FULL_NAME;

	/**
	 * Returns whether to include modifiers in the dumped string representation of a Constructor.
	 *
	 * @return true if modifiers should be included, false otherwise
	 * @since 1.2
	 */
	public boolean isIncludeModifiers() {
		return includeModifiers;
	}

	/**
	 * Sets whether to include modifiers in the dumped string representation of a Constructor.
	 *
	 * @param includeModifiers true if modifiers should be included, false otherwise
	 * @return the ConstructorDumper instance
	 * @since 1.2
	 */
	public ConstructorDumper setIncludeModifiers(boolean includeModifiers) {
		this.includeModifiers = includeModifiers;
		return this;
	}

	/**
	 * Gets the display format for the parameter types of the Constructor.
	 *
	 * @return the display format for the parameter types
	 * @since 1.2
	 */
	public TypeDisplayNameFormat getParameterTypeDisplay() {
		return parameterTypeDisplay;
	}

	/**
	 * Sets the display format for the parameter types of the Constructor.
	 *
	 * @param parameterTypeDisplay the display format for the parameter types
	 * @return the ConstructorDumper instance
	 * @since 1.2
	 */
	public ConstructorDumper setParameterTypeDisplay(TypeDisplayNameFormat parameterTypeDisplay) {
		this.parameterTypeDisplay = parameterTypeDisplay;
		return this;
	}

	/**
	 * Returns a formatted string representation of a Constructor.
	 *
	 * @param constructor The Constructor to dump
	 * @return The formatted string representation of the Constructor
	 * @since 1.2
	 */
	@Override
	public String dump(Constructor<?> constructor) {
		StringBuilder parameters = new StringBuilder();
		if (parameterTypeDisplay != TypeDisplayNameFormat.NONE) {
			for (Parameter parameter : constructor.getParameters()) {
				if (parameters.length() != 0)
					parameters.append(", ");
				parameters.append(parameterTypeDisplay.getName(parameter));
			}
		}

		return String.format("%s%s(%s)", includeModifiers ? Modifier.toString(constructor.getModifiers()) + " " : "",
				"init",
				parameters);
	}
}