package top.wavelength.betterreflection.lookup;

import top.wavelength.betterreflection.BetterReflectionClass;
import top.wavelength.betterreflection.BetterReflectionUtils;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * IdeClassFinder is a concrete implementation of the ClassFinder abstract class that is used to
 * find classes in a package from an IDE runtime environment based on certain criteria.
 *
 * @param <T> the type of the classes to find
 * @since 1.1
 */
public class IdeClassFinder<T> extends ClassFinder<T> {


	/**
	 * Constructs an IdeClassFinder object with the given package name parameter. This class extends the ClassFinder class.
	 *
	 * @param packageName the package name to search for classes in
	 * @throws NullPointerException if the packageName is null
	 * @since 1.1
	 */
	public IdeClassFinder(String packageName) {
		super(packageName);
	}

	/**
	 * Finds classes in a package based on certain criteria.
	 *
	 * @return a list of BetterReflectionClass objects representing the found classes.
	 * @throws IOException        if an I/O error occurs while finding the classes.
	 * @throws URISyntaxException if a URI syntax error occurs while finding the classes.
	 * @since 1.1
	 */
	@Override
	public List<BetterReflectionClass<T>> findClasses() throws IOException, URISyntaxException {
		return scanDirectory(BetterReflectionUtils.getDirectoriesFromPackageName(basePackage), basePackage);
	}

	/**
	 * Scans the given directories for class files and returns a list of BetterReflectionClass objects.
	 *
	 * @param directories List of directories to scan
	 * @param packageName The base package name
	 * @return List of BetterReflectionClass objects found in the directories
	 * @since 1.1
	 */
	@SuppressWarnings("unchecked")
	public List<BetterReflectionClass<T>> scanDirectory(List<File> directories, String packageName) {
		List<BetterReflectionClass<T>> classes = new ArrayList<>();

		String base = packageName == null || packageName.trim().isEmpty() ? "" : packageName + '.';

		for (File directory : directories) {
			File[] files = directory.listFiles();
			assert files != null;
			for (File file : files) {
				if (file.isDirectory() && isRecursive())
					classes.addAll(scanDirectory(Collections.singletonList(file), base + file.getName()));
				else if (file.getName().endsWith(".class")) {
					String className = base + file.getName().substring(0, file.getName().lastIndexOf('.'));

					BetterReflectionClass<?> clasz;
					try {
						clasz = new BetterReflectionClass<>(Class.forName(className, true, getClassLoader()));
					} catch (ClassNotFoundException | NoClassDefFoundError e) {
						continue;
					}

					if (getType() == null || getType().isAssignableFrom(Objects.requireNonNull(clasz)))
						classes.add((BetterReflectionClass<T>) clasz);
				}
			}
		}
		return classes;
	}

}