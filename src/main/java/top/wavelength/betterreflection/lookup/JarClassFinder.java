package top.wavelength.betterreflection.lookup;

import top.wavelength.betterreflection.BetterReflectionClass;
import top.wavelength.betterreflection.BetterReflectionUtils;
import top.wavelength.betterreflection.exceptions.CannotReadJarException;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.*;
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
		String basePackage = this.basePackage == null ? "" : this.basePackage;
		File jarFile = getJarFile();
		if (jarFile == null || !jarFile.exists() || !jarFile.canRead())
			throw new CannotReadJarException(jarFile == null ? "[NO NAME]" : jarFile.getName());

		Map<JarEntry, Exception> problematicEntries = new HashMap<>();

		try (JarFile jar = new JarFile(jarFile)) {
			Enumeration<JarEntry> entries = jar.entries();
			while (entries.hasMoreElements()) {
				JarEntry entry = entries.nextElement();
				if (entry == null)
					continue;
				String name = entry.getName();
				if (!name.endsWith(".class"))
					continue;
				try {
					String separator = BetterReflectionUtils.discernSeparator(entry.getName());
					String packageName = entry.getName().contains(separator) ?
							entry.getName().substring(0, name.lastIndexOf(separator)).replace(separator, ".")
							: "";
					String className = entry.getName().substring(packageName.length() + 1, entry.getName().lastIndexOf('.'));
					String fullName = packageName + '.' + className;

					if (isRecursive()) {
						if (!packageName.startsWith(basePackage))
							continue;
					} else if (!packageName.equals(basePackage))
						continue;

					BetterReflectionClass<?> clasz;
					try {
						clasz = new BetterReflectionClass<>(Class.forName(fullName, true, getClassLoader()));
					} catch (ClassNotFoundException | NoClassDefFoundError e) {
						continue;
					}

					if (getType() == null || getType().isAssignableFrom(Objects.requireNonNull(clasz)))
						classes.add((BetterReflectionClass<T>) clasz);
				} catch (Exception e) {
					e.printStackTrace();
					problematicEntries.put(entry, e);
				}
			}
		}

		if (!problematicEntries.isEmpty()) {
			StringBuilder errors = new StringBuilder("There has been an issue trying to load some entries:");
			for (JarEntry jarEntry : problematicEntries.keySet()) {
				StringWriter stringWriter = new StringWriter();
				PrintWriter printWriter = new PrintWriter(stringWriter);
				problematicEntries.get(jarEntry).printStackTrace(printWriter);
				String stackTrace = stringWriter.toString(); // stack trace as a string
				printWriter.close();
				stringWriter.close();

				errors.append('\n')
						.append(jarEntry.getName())
						.append('\n')
						.append(stackTrace)
						.append("------------------");
			}
			throw new IllegalStateException(errors.toString());
		}

		return classes;
	}

}