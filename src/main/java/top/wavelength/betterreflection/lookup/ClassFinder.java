package top.wavelength.betterreflection.lookup;

import top.wavelength.betterreflection.BetterReflectionClass;
import top.wavelength.betterreflection.BetterReflectionUtils;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Iterator;
import java.util.List;

/**
 * An abstract class for finding classes in a package based on certain criteria.
 *
 * @param <T> the type of the classes to find
 * @since 1.1
 */
public abstract class ClassFinder<T> implements Iterable<BetterReflectionClass<T>> {

	/**
	 * @since 1.1
	 */
	protected final String basePackage;
	/**
	 * @since 1.1
	 */
	private boolean recursive;
	/**
	 * @since 1.1
	 */
	private BetterReflectionClass<T> type;

	/**
	 * @since 1.3
	 */
	private ClassLoader classLoader = getClass().getClassLoader();
	/**
	 * @since 1.3
	 */
	private File jarFile = BetterReflectionUtils.LAUNCH_JAR_FILE;

	/**
	 * Constructs a ClassFinder object with the given base package parameter.
	 *
	 * @param basePackage the base package to search for classes in
	 * @since 1.1
	 */
	public ClassFinder(String basePackage) {
		this.basePackage = basePackage;
	}

	/**
	 * Sets the type of the ClassFinder instance.
	 *
	 * @param type the type of the ClassFinder instance to set
	 * @return the updated ClassFinder instance
	 * @since 1.1
	 */
	public ClassFinder<T> ofType(Class<T> type) {
		return ofType(new BetterReflectionClass<>(type));
	}

	/**
	 * Sets the type of the ClassFinder instance.
	 *
	 * @param type the type of the ClassFinder instance to set
	 * @return the updated ClassFinder instance
	 * @since 1.1
	 */
	public ClassFinder<T> ofType(BetterReflectionClass<T> type) {
		this.type = type;
		return this;
	}

	/**
	 * Retrieves the type of the ClassFinder instance.
	 *
	 * @return the BetterReflectionClass representing the type.
	 * @since 1.1
	 */
	public BetterReflectionClass<?> getType() {
		return type;
	}

	/**
	 * Sets the recursive flag of the ClassFinder instance.
	 *
	 * @param recursive the boolean value to set the recursive flag to
	 * @return the updated ClassFinder instance
	 * @since 1.1
	 */
	public ClassFinder<T> recursive(boolean recursive) {
		this.recursive = recursive;
		return this;
	}

	/**
	 * Returns whether the ClassFinder instance is set to be recursive or not.
	 *
	 * @return true if the ClassFinder instance is recursive, false otherwise.
	 * @since 1.1
	 */
	public boolean isRecursive() {
		return recursive;
	}

	/**
	 * Sets the class loader that will be used to load the classes for this ClassFinder instance.
	 *
	 * @param classLoader the {@link ClassLoader} instance to be used for loading classes
	 * @return the updated {@link ClassFinder} instance
	 * @since 1.3
	 */
	public ClassFinder<T> classLoader(ClassLoader classLoader) {
		this.classLoader = classLoader;
		return this;
	}

	/**
	 * Retrieves the class loader currently set for this ClassFinder instance.
	 *
	 * @return the {@link ClassLoader} instance being used to load classes
	 * @since 1.3
	 */
	public ClassLoader getClassLoader() {
		return classLoader;
	}

	/**
	 * Sets the jar file that this ClassFinder will search for classes.
	 *
	 * @param jarFile the {@link File} representing the jar file to search for classes
	 * @return the updated {@link File} instance
	 * @since 1.3
	 */
	public ClassFinder<T> jarFile(File jarFile) {
		this.jarFile = jarFile;
		return this;
	}

	/**
	 * Retrieves the jar file currently set for this ClassFinder instance.
	 *
	 * @return the {@link File} representing the jar file being searched for classes
	 * @since 1.3
	 */
	public File getJarFile() {
		return jarFile;
	}

	/**
	 * Finds classes in a package based on certain criteria.
	 *
	 * @return a list of BetterReflectionClass objects representing the found classes.
	 * @throws IOException        if an I/O error occurs while finding the classes.
	 * @throws URISyntaxException if a URI syntax error occurs while finding the classes.
	 * @since 1.1
	 */
	public abstract List<BetterReflectionClass<T>> findClasses() throws IOException, URISyntaxException;

	/**
	 * Returns an iterator over the BetterReflectionClass objects representing the found classes.
	 *
	 * @return an iterator over the BetterReflectionClass objects.
	 * @throws RuntimeException if an I/O error occurs while finding the classes.
	 * @since 1.1
	 */
	@Override
	public Iterator<BetterReflectionClass<T>> iterator() {
		try {
			return findClasses().iterator();
		} catch (IOException | URISyntaxException e) {
			throw new RuntimeException(e);
		}
	}
}