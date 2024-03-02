package top.wavelength.betterreflection.dumper;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

/**
 * TypeDisplayNameFormat is an enumeration that represents different formats for displaying the names of types.
 * It provides methods to get the formatted names of classes, methods, fields, and parameters.
 *
 * @since 1.2
 */
public enum TypeDisplayNameFormat {

	FULL_NAME,
	CANONICAL_NAME,
	TYPE_NAME,
	SIMPLE_NAME,
	NONE;

	/**
	 * Returns the name of the given type based on the specified TypeDisplayNameFormat.
	 *
	 * @param type The type for which the name will be returned
	 * @return The formatted name of the type
	 * @since 1.2
	 */
	public String getName(Class<?> type) {
		switch (this) {
			case FULL_NAME:
				return type.getName();
			case CANONICAL_NAME:
				return type.getCanonicalName();
			case TYPE_NAME:
				return type.getTypeName();
			case SIMPLE_NAME:
				return type.getSimpleName();
			case NONE:
			default:
				return "";
		}
	}

	/**
	 * Returns the name of the given method's return type based on the specified TypeDisplayNameFormat.
	 *
	 * @param method The Method object for which the return type name will be returned
	 * @return The formatted name of the return type of the method
	 * @since 1.2
	 */
	public String getName(Method method) {
		return getName(method.getReturnType());
	}

	/**
	 * Returns the name of the given field's type.
	 *
	 * @param field The field for which the type name will be returned
	 * @return The name of the field's type
	 * @since 1.2
	 */
	public String getName(Field field) {
		return getName(field.getType());
	}

	/**
	 * Returns the name of the given parameter's type based on the specified TypeDisplayNameFormat.
	 *
	 * @param parameter The parameter for which the type name will be returned
	 * @return The formatted name of the parameter's type
	 * @since 1.2
	 */
	public String getName(Parameter parameter) {
		return getName(parameter.getType());
	}

}