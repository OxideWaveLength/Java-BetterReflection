package top.wavelength.betterreflection.lookup;

import top.wavelength.betterreflection.BetterReflectionClass;
import top.wavelength.betterreflection.BetterReflectionUtils;

/**
 * The ClassFinderFactory class provides factory methods to create instances of ClassFinder, which is used to find classes in a package based on certain criteria.
 *
 * @since 1.1
 */
public class ClassFinderFactory {

	/**
	 * Creates a ClassFinder object based on the given base package and package class.
	 *
	 * @param basePackage  the base package for scanning classes
	 * @param packageClass a class contained within the package to determine whether it's in a JAR file
	 * @param type         the type of class to find
	 * @param <T>          the type of class to find
	 * @return a ClassFinder object based on the given parameters
	 * @since 1.1
	 */
	public static <T> ClassFinder<T> create(String basePackage, BetterReflectionClass<?> packageClass, BetterReflectionClass<T> type) {
		ClassFinder<T> classFinder;
		if (BetterReflectionUtils.isRunningFromJar(packageClass))
			classFinder = new JarClassFinder<>(basePackage);
		else
			classFinder = new IdeClassFinder<>(basePackage);
		classFinder.ofType(type);
		return classFinder;
	}

	/**
	 * Creates a ClassFinder object based on the given base package and package class.
	 *
	 * @param basePackage  the base package for scanning classes
	 * @param packageClass a class contained within the package to determine whether it's in a JAR file
	 * @param <T>          the type of class to find
	 * @return a ClassFinder object based on the given parameters
	 * @since 1.1
	 */
	public static <T> ClassFinder<T> create(String basePackage, BetterReflectionClass<?> packageClass) {
		return create(basePackage, packageClass, (BetterReflectionClass<T>) null);
	}

	/**
	 * Creates a ClassFinder object based on the given base package and package class.
	 *
	 * @param basePackage  the base package for scanning classes
	 * @param packageClass a class contained within the package to determine whether it's in a JAR file
	 * @param type         the type of class to find
	 * @param <T>          the type of class to find
	 * @return a ClassFinder object based on the given parameters
	 * @since 1.1
	 */
	public static <T> ClassFinder<T> create(String basePackage, BetterReflectionClass<?> packageClass, Class<T> type) {
		return create(basePackage, packageClass, new BetterReflectionClass<>(type));
	}

}