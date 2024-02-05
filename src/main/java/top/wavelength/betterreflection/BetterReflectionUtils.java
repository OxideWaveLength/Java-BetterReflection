package top.wavelength.betterreflection;

import sun.misc.Unsafe;
import top.wavelength.betterreflection.exceptions.CannotReadJarException;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.*;
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

public class BetterReflectionUtils {

	/**
	 * @param clasz the class to get the jar for
	 * @return whether this program is running from a Jar file or not.
	 * @since 0.7
	 */
	public static File getCurrentJarFile(BetterReflectionClass clasz) {
		if (!isRunningFromJar())
			return null;
		try {
			return new File(clasz.getProtectionDomain().getCodeSource().getLocation().toURI());
		} catch (URISyntaxException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * @return whether this program is running from a Jar file or not.
	 * @since 0.4
	 */
	public static File getCurrentJarFile() {
		return getCurrentJarFile(CLASS);
	}

	/**
	 * @param clasz the class to get the jar for
	 * @return an instance of JarFile from {@link #getCurrentJar()}
	 * @since 0.7
	 */
	public static JarFile getCurrentJar(BetterReflectionClass clasz) {
		File file = getCurrentJarFile(clasz);
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
	 * @return an instance of JarFile from {@link #getCurrentJar()}
	 * @since 0.4
	 */
	public static JarFile getCurrentJar() {
		return getCurrentJar(CLASS);
	}

	/**
	 * @param clasz the class to check
	 * @return whether this program is running from a Jar file or not.
	 * @since 0.7
	 */
	public static boolean isRunningFromJar(BetterReflectionClass clasz) {
		if (isRunningOnAndroid())
			return false;
		try {
			// Caused by: java.lang.NullPointerException: Attempt to invoke virtual method
			// 'java.lang.String java.net.URL.toString()' on a null object reference
			return clasz.getProtectionDomain().getCodeSource().getLocation().toURI().toString().endsWith(".jar");
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * @return whether this program is running from a Jar file or not.
	 * @since 0.4
	 */
	public static boolean isRunningFromJar() {
		return isRunningFromJar(CLASS);
	}

	/**
	 * @return whether this program is running on Android environment.
	 * <p>
	 * This can easily be fooled and should not be trusted on its own.
	 * @since 0.5
	 */
	public static boolean isRunningOnAndroid() {
		try {
			new BetterReflectionClass("android.R");
			return true;
		} catch (ClassNotFoundException e) {
			return false;
		}
	}

	public static Field getField(String name, Field[] fields) {
		for (Field field : fields)
			if (field.getName().equals(name))
				return field;

		return null;
	}

	public static Constructor<?> getConstructor(Class<?>[] parameterTypes, Constructor<?>[] constructors) {
		for (Constructor<?> constructor : constructors)
			if (doParametersMatch(constructor.getParameterTypes(), parameterTypes))
				return constructor;

		return null;
	}

	public static Method getMethod(String name, Class<?>[] parameterTypes, Method[] methods) {
		for (Method method : methods)
			if ((method.getName().equals(name)) && doParametersMatch(method.getParameterTypes(), parameterTypes))
				return method;

		return null;
	}

	public static boolean doParametersMatch(Class<?>[] parameters1, Class<?>[] parameters2) {
		if (parameters1.length != parameters2.length)
			return false;
		for (int i = 0; i < parameters1.length; i++)
			if (!(parameters1[i].equals(parameters2[i])))
				return false;

		return true;
	}

	/**
	 * @param parameterTypes the parameter types
	 * @return the parameter types' classes
	 * @deprecated (renamed) use {@link #getTypes(Object...)}
	 */
	@Deprecated
	public static Class<?>[] getClasses(Object... parameterTypes) {
		return getClasses(null, parameterTypes);
	}

	/**
	 * @param primitives     a map of primitives to use for lookup
	 * @param parameterTypes the parameter types
	 * @return the parameter types' classes
	 * @deprecated (renamed) use {@link #getTypes(Map, Object...)}
	 */
	@Deprecated
	public static Class<?>[] getClasses(Map<Integer, Class<?>> primitives, Object... parameterTypes) {
		return getTypes(primitives, parameterTypes);
	}

	/**
	 * @param parameterTypes the parameter types
	 * @return the parameter types' classes
	 * @since 0.4 - preceded by {@link #getClasses(Object...)}
	 */
	public static Class<?>[] getTypes(Object... parameterTypes) {
		return getTypes(null, parameterTypes);
	}

	/**
	 * @param primitives     a map of primitives to use for lookup
	 * @param parameterTypes the parameter types
	 * @return the parameter types' classes
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
	 * This should only be invoked when launching on an IDE
	 *
	 * @param packageName the package name
	 * @return a list of directories matching the package name.
	 * @throws IOException        If I/O errors occur
	 * @throws URISyntaxException If the resource's URL cannot be converted to URI
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
	 * currently includes sub-packages but a boolean will be implemented for
	 * that)
	 * @throws IOException            If I/O errors occur
	 * @throws URISyntaxException     If the resource's URL cannot be converted to URI
	 * @throws CannotReadJarException If this method is invoked from a
	 *                                Jar and the Jar file cannot be found, or it
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
	 * getClassesFromNameBeginning("me.wavelength.betterreflection", "BetterReflection")
	 * Will return a list with the following classes: [ "BetterReflection", "BetterReflectionClass", "BetterReflectionUtils" ]
	 *
	 * @param packageName The package name to search inside of
	 * @param beginning   the beginning of the class name
	 * @return The classes found that start with beginning
	 * @throws IOException            If I/O errors occur
	 * @throws URISyntaxException     If the resource's URL cannot be converted to URI
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
	 * returns 12)
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
	 * Version is greater than or equal to 11
	 * Provided by <a href="https://stackoverflow.com/a/69418150">this</a> StackOverflow post:
	 *
	 * @param field    the field to change the modifier to
	 * @param setFinal whether the field should be set to final or not
	 *                 Java 16 or higher requires the following launch arguments to be added:
	 *                 --add-opens java.base/java.lang=ALL-UNNAMED --add-opens java.base/java.lang.reflect=ALL-UNNAMED
	 *                 <p>
	 *                 Removes (or adds) the final modifier to a field.
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

	/**
	 * * @param method the method to dump
	 * * @param includeModifiers      whether the method's modifiers should be dumped
	 * * @param includeReturnType     whether the method's return type should be dumped
	 * * @param includeParameterNames whether the method's parameter names should be dumped
	 *
	 * @return the method's header (modifiers name(parameters), e.g. public String
	 * generateMethodHeader(Method method)
	 * @since 0.7
	 */
	public static String generateMethodHeader(Method method, boolean includeModifiers, boolean includeReturnType, boolean includeParameterNames) {
		StringBuilder parameters = new StringBuilder();
		for (Parameter parameter : method.getParameters()) {
			if (parameters.length() != 0)
				parameters.append(", ");
			parameters.append(parameter.getType().getName());
			if (includeParameterNames)
				parameters.append(" ").append(parameter.getName());
		}
		return String.format("%s%s%s(%s)", includeModifiers ? Modifier.toString(method.getModifiers()) + " " : "", includeReturnType ? method.getReturnType().getName() + " " : "", method.getName(), parameters);
	}

	/**
	 * @param method                the method to dump
	 * @param includeModifiers      whether the method's modifiers should be dumped
	 * @param includeReturnType     whether the method's return type should be dumped
	 * @param includeParameterNames whether the method's parameter names should be dumped
	 * @since 0.7 Writes {@link #generateMethodHeader(Method, boolean, boolean, boolean)}} to stout
	 */
	public static void dumpMethodHeader(Method method, boolean includeModifiers, boolean includeReturnType, boolean includeParameterNames) {
		System.out.println(generateMethodHeader(method, includeModifiers, includeReturnType, includeParameterNames));
	}

	/**
	 * Uses Unsafe to allocate an instance of this class without invoking its constructor.
	 *
	 * @param clasz the class to allocate the instance of
	 * @return a new instance of the current class
	 * @since 1.0
	 */
	public static Object allocateUnsafeInstance(Class<?> clasz) throws IllegalAccessException, InstantiationException {
		Field field = BetterReflection.UNSAFE_CLASS.getDeclaredField("theUnsafe");
		field.setAccessible(true);
		Unsafe unsafe = (Unsafe) field.get(null);
		return unsafe.allocateInstance(clasz);
	}

	public static final BetterReflectionClass CLASS = new BetterReflectionClass(BetterReflectionUtils.class);

	/**
	 * Caching the jar file on launch to make sure it's not null later.
	 *
	 * @since 0.4
	 */
	public static final File LAUNCH_JAR_FILE = getCurrentJarFile(CLASS);

}
