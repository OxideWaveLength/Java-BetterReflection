package me.wavelength.betterreflection;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import me.wavelength.betterreflection.exceptions.CannotReadJarException;

public class BetterReflectionUtils {

	/**
	 * Caching the jar file on launch to make sure it's not null later.
	 * 
	 * @since 0.4
	 */
	public static final File LAUNCH_JAR_FILE = getCurrentJarFile();

	/**
	 * @return whether this program is running from a Jar file or not.
	 * 
	 * @since 0.4
	 */
	public static File getCurrentJarFile() {
		if (!isRunningFromJar())
			return null;
		try {
			return new File(BetterReflectionUtils.class.getProtectionDomain().getCodeSource().getLocation().toURI());
		} catch (URISyntaxException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * @return an instance of JarFile from {@link #getCurrentJar()}
	 * 
	 * @since 0.4
	 */
	public static JarFile getCurrentJar() {
		File file = getCurrentJarFile();
		if (file == null)
			return null;
		try {
			return new JarFile(file);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * @return whether this program is running from a Jar file or not.
	 * 
	 * @since 0.4
	 */
	public static boolean isRunningFromJar() {
		return BetterReflectionUtils.class.getResource("BetterReflectionUtils.class").toString().startsWith("jar:");
	}

	public static Field getField(String name, Field[] fields) {
		for (Field field : fields)
			if (field.getName().equals(name))
				return field;

		return null;
	}

	public static Constructor<?> getConstructor(Class<?>[] parameterTypes, Constructor<?>[] constructors) {
		for (Constructor<?> constructor : constructors) {
			if (doParametersMatch(constructor.getParameterTypes(), parameterTypes))
				return constructor;
		}

		return null;
	}

	public static Method getMethod(String name, Class<?>[] parameterTypes, Method[] methods) {
		for (Method method : methods) {
			if ((method.getName().equals(name)) && doParametersMatch(method.getParameterTypes(), parameterTypes))
				return method;

		}

		return null;
	}

	public static boolean doParametersMatch(Class<?>[] parameters1, Class<?>[] parameters2) {
		if (parameters1.length != parameters2.length)
			return false;
		for (int i = 0; i < parameters1.length; i++) {
			if (!(parameters1[i].equals(parameters2[i])))
				return false;
		}

		return true;
	}

	/**
	 * @deprecated (renamed) use {@link #getTypes(Object...)}
	 */
	@Deprecated
	public static Class<?>[] getClasses(Object... parameterTypes) {
		return getClasses(null, parameterTypes);
	}

	/**
	 * @deprecated (renamed) use {@link #getTypes(Map, Object...)}
	 */
	@Deprecated
	public static Class<?>[] getClasses(Map<Integer, Class<?>> primitives, Object... parameterTypes) {
		return getTypes(primitives, parameterTypes);
	}

	/**
	 * @since 0.4 - preceded by {@link #getClasses(Object...)}
	 */
	public static Class<?>[] getTypes(Object... parameterTypes) {
		return getTypes(null, parameterTypes);
	}

	/**
	 * @since 0.4 - preceded by {@link #getClasses(Map, Object...)}
	 */
	public static Class<?>[] getTypes(Map<Integer, Class<?>> primitives, Object... parameterTypes) {
		Class<?>[] parameterTypesRefined = new Class<?>[parameterTypes.length];
		for (int i = 0; i < parameterTypes.length; i++) {
			Object parameterType = parameterTypes[i];
			if (parameterType == null)
				continue;

			if (parameterType instanceof ReflectionParameter)
				parameterType = ((ReflectionParameter) parameterType).getValue();

			if (parameterType == null)
				continue;

			if (primitives != null && primitives.containsKey(i))
				parameterTypesRefined[i] = primitives.get(i);
			else
				parameterTypesRefined[i] = (parameterType instanceof BetterReflectionClass ? (Class<?>) ((BetterReflectionClass) parameterType).getClasz() : (parameterType instanceof Class<?> ? (Class<?>) parameterType : parameterType.getClass()));
		}

		return parameterTypesRefined;
	}

	/*
	 * The code below is a modified version of this answer:
	 * https://stackoverflow.com/a/520344
	 */

	/**
	 * This should only be ran from an IDE
	 * 
	 * @param packageName the package name
	 * @return a list of directories matching the package name.
	 * @throws IOException
	 * @throws URISyntaxException
	 */
	public static List<File> getDirectoriesFromPackageName(String packageName) throws IOException, URISyntaxException {
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		assert classLoader != null;

		String path = packageName.replace('.', '/');

		Enumeration<URL> resources = classLoader.getResources(path);
		List<File> directories = new ArrayList<>();
		while (resources.hasMoreElements()) {
			URL resource = resources.nextElement();
			directories.add(new File(resource.toURI()));
		}

		return directories;
	}

	/**
	 * @param packageName the package name to scan
	 * @return a list of all the classes that reside in the specified package (this
	 *         currently includes sub-packages but a boolean will be implemented for
	 *         that)
	 * 
	 * @throws IOException
	 * @throws URISyntaxException
	 * @throws CannotReadJarException this is thrown if this method is run from a
	 *                                Jar and the Jar file cannot be found or it
	 *                                cannot be read.
	 */
	public static List<BetterReflectionClass> getClassesInPackage(String packageName) throws IOException, URISyntaxException, CannotReadJarException {
		if (packageName == null || packageName.trim().isEmpty())
			return new ArrayList<>();
		List<BetterReflectionClass> classes = new ArrayList<>();
		if (isRunningFromJar()) {
			packageName = packageName.replace('.', '/');
			if (LAUNCH_JAR_FILE == null || !LAUNCH_JAR_FILE.exists() || !LAUNCH_JAR_FILE.canRead())
				throw new CannotReadJarException(LAUNCH_JAR_FILE == null ? "[NO NAME]" : LAUNCH_JAR_FILE.getName());
			try (JarFile jar = new JarFile(LAUNCH_JAR_FILE)) {
				Enumeration<JarEntry> entries = jar.entries();
				while (entries.hasMoreElements()) {
					JarEntry entry = entries.nextElement();
					if (entry == null)
						continue;
					String name = entry.getName();
					if (name.startsWith(packageName) && name.endsWith(".class"))
						classes.add(BetterReflectionClass.forName(entry.getName().substring(0, name.lastIndexOf('.')).replace('/', '.')));
				}
				jar.close();
			}
			return classes;
		}

		List<File> directories = getDirectoriesFromPackageName(packageName);
		for (File directory : directories) {
			File[] files = directory.listFiles();
			for (File file : files)
				if (file.getName().endsWith(".class"))
					classes.add(BetterReflectionClass.forName(packageName + '.' + file.getName().substring(0, file.getName().length() - 6)));
		}
		return classes;
	}

	/**
	 * This method will use {@link #getClassesInPackage(String)} to find all the
	 * classes in the specified package, it will then return a list of all the
	 * classes that start with beginning
	 * For example:
	 * @formatter:off
	 * getClassesFromNameBeginning("me.wavelength.betterreflection", "BetterReflection")
	 * Will return a list with the following classes: [ "BetterReflection", "BetterReflectionClass", "BetterReflectionUtils" ]
	 * @formatter:on
	 *
	 * @param packageName The package name to search inside of
	 * @param beginning the beginning of the class name
	 * @return The classes found that start with beginning
	 * @throws URISyntaxException
	 * @throws IOException
	 * @throws CannotReadJarException this is thrown if this method is run from a
	 *                                Jar and the Jar file cannot be found or it
	 *                                cannot be read.
	 */
	public static List<BetterReflectionClass> getClassesFromNameBeginning(String packageName, String beginning) throws IOException, URISyntaxException, CannotReadJarException {
		List<BetterReflectionClass> classes = new ArrayList<>();
		for (BetterReflectionClass clasz : getClassesInPackage(packageName))
			if (clasz.getSimpleName().startsWith(beginning))
				classes.add(clasz);
		return classes;
	}

	/**
	 * Gets the System property "java.version"
	 * 
	 * @return The current java version as integer (Java 8 (1.8) returns 8, Java 12
	 *         returns 12)
	 */
	public static int getJavaVersion() {
		String version = System.getProperty("java.version");
		if (version.startsWith("1.")) {
			version = version.substring(2, 3);
		} else {
			int dot = version.indexOf(".");
			if (dot != -1) {
				version = version.substring(0, dot);
			}
		}
		return Integer.parseInt(version);
	}

	/**
	 * Version >= 11 provided by this StackOverflow post:
	 * https://stackoverflow.com/a/69418150
	 * @formatter:off
	 * @implNote Java 16 or higher requires the following launch arguments added:
	 *           --add-opens java.base/java.lang=ALL-UNNAMED --add-opens java.base/java.lang.reflect=ALL-UNNAMED
	 * @formatter:on
	 *           Removes (or adds) the final modifier to a field.
	 * @param field  the field to change the modifier to
	 * @param setFinal whether the field should be set to final or not
	 * @throws NoSuchFieldException
	 * @throws SecurityException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws NoSuchMethodException
	 * @throws InvocationTargetException
	 */
	public static void setFinal(Field field, boolean setFinal) throws Exception {
		final int version = getJavaVersion();
		Exception exception = AccessController.doPrivileged((PrivilegedAction<Exception>) () -> {
			Field modifiers = null;
			try {
				if (version >= 11) {
					Method getDeclaredFields0 = Class.class.getDeclaredMethod("getDeclaredFields0", boolean.class);
					getDeclaredFields0.setAccessible(true);
					Field[] fields = (Field[]) getDeclaredFields0.invoke(Field.class, false);
					for (Field each : fields) {
						if ("modifiers".equals(each.getName())) {
							modifiers = each;
							break;
						}
					}
					assert modifiers != null;
				} else {
					field.setAccessible(true);
					modifiers = Field.class.getDeclaredField("modifiers");
				}
				modifiers.setAccessible(true);
				if (!setFinal && Modifier.isFinal(field.getModifiers()))
					modifiers.setInt(field, field.getModifiers() & ~Modifier.FINAL);

				if (setFinal && !Modifier.isFinal(field.getModifiers()))
					modifiers.setInt(field, field.getModifiers() & Modifier.FINAL);
			} catch (Exception e) {
				return e;
			}
			return null;
		});
		if (exception != null) {
			if (version >= 11) {
				System.err.println("You seem to be using Java 11 or higher. Please, make sure that you have either of the following launch parameters set specified:");
				System.err.println("\tJava 16 and below: --illegal-access=permit");
				System.err.println("\tJava 17 and above: --add-opens java.base/java.lang=ALL-UNNAMED --add-opens java.base/java.lang.reflect=ALL-UNNAMED");
			}
			throw exception;
		}
	}

}
