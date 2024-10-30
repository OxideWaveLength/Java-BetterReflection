package top.wavelength.betterreflection.lookup;

import top.wavelength.betterreflection.BetterReflectionClass;
import top.wavelength.betterreflection.exceptions.CannotReadJarException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Objects;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

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
		List<BetterReflectionClass<T>> classes = new ArrayList<>();
		String basePackage = this.basePackage == null ? "" : this.basePackage.replace('.', '/');
		File jarFile = getJarFile();
		if (jarFile == null || !jarFile.exists() || !jarFile.canRead())
			throw new CannotReadJarException(jarFile == null ? "[NO NAME]" : jarFile.getName());

		try (JarFile jar = new JarFile(jarFile)) {
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

				if ((isRecursive() && !packageName.startsWith(basePackage))
						|| (!isRecursive() && !packageName.equalsIgnoreCase(basePackage)))
					continue;

				BetterReflectionClass<?> clasz;
				try {
					clasz = new BetterReflectionClass<>(Class.forName(fullName, true, getClassLoader()));
				} catch (ClassNotFoundException | NoClassDefFoundError e) {
					continue;
				}

				if (getType() == null || getType().isAssignableFrom(Objects.requireNonNull(clasz)))
					classes.add((BetterReflectionClass<T>) clasz);
			}
		}

		return classes;
	}
}