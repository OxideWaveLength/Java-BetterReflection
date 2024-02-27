package top.wavelength.betterreflection.lookup;

import top.wavelength.betterreflection.BetterReflectionClass;
import top.wavelength.betterreflection.exceptions.CannotReadJarException;

import java.io.IOException;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import static top.wavelength.betterreflection.BetterReflectionUtils.LAUNCH_JAR_FILE;

/**
 * JarClassFinder is a concrete implementation of the ClassFinder abstract class that is used to
 * find classes in a package from a JAR file based on certain criteria.
 *
 * @param <T> the type of the classes to find
 * @since 1.1
 */
public class JarClassFinder<T> extends ClassFinder<T> {

	/**
	 * Constructs a JarClassFinder object with the given package name parameter. This class extends the ClassFinder class.
	 *
	 * @param packageName the package name to search for classes in
	 * @throws NullPointerException if the packageName is null
	 * @since 1.1
	 */
	public JarClassFinder(String packageName) {
		super(packageName);
	}

	/**
	 * Finds classes in a package based on certain criteria.
	 *
	 * @return a list of BetterReflectionClass objects representing the found classes.
	 * @throws IOException if an I/O error occurs while finding the classes.
	 * @since 1.1
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<BetterReflectionClass<T>> findClasses() throws IOException {
		if (basePackage == null || basePackage.trim().isEmpty())
			return Collections.emptyList();
		List<BetterReflectionClass<T>> classes = new ArrayList<>();
		String basePackage = this.basePackage.replace('.', '/');
		if (LAUNCH_JAR_FILE == null || !LAUNCH_JAR_FILE.exists() || !LAUNCH_JAR_FILE.canRead())
			throw new CannotReadJarException(LAUNCH_JAR_FILE == null ? "[NO NAME]" : LAUNCH_JAR_FILE.getName());

		try (JarFile jar = new JarFile(LAUNCH_JAR_FILE)) {
			Enumeration<JarEntry> entries = jar.entries();
			while (entries.hasMoreElements()) {
				JarEntry entry = entries.nextElement();
				if (entry == null)
					continue;
				String name = entry.getName();
				if (!name.endsWith(".class"))
					continue;

				String packageName = entry.getName().substring(0, name.lastIndexOf('/')).replace('/', '.');
				String className = entry.getName().substring(packageName.length() + 1, entry.getName().lastIndexOf('.'));
				String fullName = packageName + '.' + className;
				BetterReflectionClass<?> clasz = BetterReflectionClass.forName(fullName);
				if (getType() == null || getType().isAssignableFrom(Objects.requireNonNull(clasz)))
					classes.add((BetterReflectionClass<T>) clasz);

				if (isRecursive()) {
					if (packageName.startsWith(basePackage))
						classes.add((BetterReflectionClass<T>) clasz);
				} else if (packageName.equals(basePackage))
					classes.add((BetterReflectionClass<T>) clasz);
			}
		}

		return classes;
	}
}