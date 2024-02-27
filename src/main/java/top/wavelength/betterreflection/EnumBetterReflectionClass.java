package top.wavelength.betterreflection;

import java.util.Optional;

/**
 * A class that extends BetterReflectionClass and provides additional functionality for enum classes.
 *
 * @param <T> the enum type
 * @since 1.1
 */
public class EnumBetterReflectionClass<T extends Enum<?>> extends BetterReflectionClass<T> {

	protected T[] enumConstants;

	/**
	 * Creates a new EnumBetterReflectionClass instance with the given class name.
	 * Initializes the class by retrieving the enum constants using reflection.
	 *
	 * @param className the name of the class to wrap
	 * @throws ClassNotFoundException if the class cannot be found
	 * @throws IllegalStateException  if the class is not an enum class
	 * @throws RuntimeException       if an exception occurs during reflection
	 * @since 1.1
	 */
	public EnumBetterReflectionClass(String className) throws ClassNotFoundException {
		super(className);
		init();
	}

	/**
	 * Creates a new EnumBetterReflectionClass instance with the given BetterReflectionClass.
	 * Initializes the class by retrieving the enum constants using reflection.
	 *
	 * @param clasz the class to wrap
	 * @since 1.1
	 */
	public EnumBetterReflectionClass(BetterReflectionClass<T> clasz) {
		super(clasz);
		init();
	}

	/**
	 * Creates a new EnumBetterReflectionClass instance with the given class.
	 * Initializes the class by retrieving the enum constants using reflection.
	 *
	 * @param clasz the class to wrap
	 * @since 1.1
	 */
	public EnumBetterReflectionClass(Class<T> clasz) {
		super(clasz);
		init();
	}

	/**
	 * Initializes the class by retrieving the enum constants using reflection.
	 *
	 * @throws IllegalStateException if the class is not an enum class
	 * @throws RuntimeException      if an exception occurs during reflection
	 * @since 1.1
	 */
	@SuppressWarnings("unchecked")
	private void init() {
		if (!isEnum())
			throw new IllegalStateException("Trying to wrap a non-enum class ('" + name + "')");
		try {
			this.enumConstants = (T[]) BetterReflection.JAVA_CLASS.getDeclaredMethod("getEnumConstants").invoke(clasz);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * @return an array of all enum constants
	 * @since 1.1
	 */
	public T[] getEnumConstants() {
		return enumConstants;
	}

	/**
	 * Checks if the given value exists in the enum constants of the containing class.
	 *
	 * @param value the value to check if it exists
	 * @return true if the value exists, false otherwise
	 * @since 1.1
	 */
	public boolean contains(String value) {
		if (value == null || value.trim().isEmpty())
			return false;
		for (T e : enumConstants)
			if (e.name().equals(value))
				return true;
		return false;
	}

	/**
	 * Retrieves the enum constant associated with the specified value,
	 * or an empty Optional if the value is null, empty, or does not exist in the enum constants of the containing class.
	 *
	 * @param value the value to retrieve the enum constant for
	 * @return an Optional containing the enum constant if it exists, otherwise an empty Optional
	 * @since 1.1
	 */
	public Optional<T> getIfPresent(String value) {
		if (value == null || value.trim().isEmpty())
			return Optional.empty();
		for (T e : enumConstants)
			if (e.name().equals(value))
				return Optional.of(e);
		return Optional.empty();
	}

	/**
	 * Retrieves the enum constant associated with the specified value,
	 * or returns null if the value is null, empty, or does not exist in the enum constants
	 * of the containing class.
	 *
	 * @param value the value to retrieve the enum constant for
	 * @return the enum constant if it exists, otherwise null
	 * @since 1.1
	 */
	public T getOrNull(String value) {
		if (value == null || value.trim().isEmpty())
			return null;
		for (T e : enumConstants)
			if (e.name().equals(value))
				return e;
		return null;
	}

	/**
	 * Retrieves the enum constant associated with the specified value, or returns the default value if the value is null, empty, or does not exist in the enum constants of the containing
	 * class.
	 *
	 * @param value        the value to retrieve the enum constant for
	 * @param defaultValue the default value to return if the specified value does not exist
	 * @return the enum constant if it exists, otherwise the default value
	 * @since 1.1
	 */
	public T getOrDefault(String value, T defaultValue) {
		if (value == null || value.trim().isEmpty())
			return defaultValue;
		for (T e : enumConstants)
			if (e.name().equals(value))
				return e;
		return defaultValue;
	}

	/**
	 * Returns a string representation of the enum constants joined by the specified separator.
	 *
	 * @param separator the separator to use between each enum constant
	 * @return a string representation of the enum constants
	 * @since 1.1
	 */
	public String toString(String separator) {
		if (separator == null) separator = "";
		StringBuilder string = new StringBuilder();
		for (Enum<?> e : enumConstants)
			string.append(string.length() > 0 ? separator : "").append(e.name());
		return string.toString();
	}

	/**
	 * Returns a string representation of the enum constants joined by the default separator ", ".
	 *
	 * @return a string representation of the enum constants
	 * @since 1.1
	 */
	public String toString() {
		return toString(", ");
	}

}