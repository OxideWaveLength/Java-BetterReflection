package me.wavelength.betterreflection;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;

public class BetterReflectionUtils {

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

	public static Class<?>[] getClasses(Object... parameterTypes) {
		return getClasses(null, parameterTypes);
	}

	public static Class<?>[] getClasses(Map<Integer, Class<?>> primitives, Object... parameterTypes) {
		Class<?>[] parameterTypesRefined = new Class<?>[parameterTypes.length];
		for (int i = 0; i < parameterTypes.length; i++) {
			Object parameterType = parameterTypes[i];

			if (parameterType instanceof ReflectionParameter)
				parameterType = ((ReflectionParameter) parameterType).getValue();

			if (primitives != null && primitives.containsKey(i))
				parameterTypesRefined[i] = primitives.get(i);
			else
				parameterTypesRefined[i] = (parameterType instanceof BetterReflectionClass ? (Class<?>) ((BetterReflectionClass) parameterType).getClasz() : (parameterType instanceof Class<?> ? (Class<?>) parameterType : parameterType.getClass()));
		}

		return parameterTypesRefined;
	}

}package me.wavelength.betterreflection;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

public class BetterReflectionUtils {

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

	public static Class<?>[] getClasses(Object... parameterTypes) {
		return getClasses(null, parameterTypes);
	}

	public static Class<?>[] getClasses(Map<Integer, Class<?>> primitives, Object... parameterTypes) {
		Class<?>[] parameterTypesRefined = new Class<?>[parameterTypes.length];
		for (int i = 0; i < parameterTypes.length; i++) {
			Object parameterType = parameterTypes[i];

			if (parameterType instanceof ReflectionParameter)
				parameterType = ((ReflectionParameter) parameterType).getValue();

			if (primitives != null && primitives.containsKey(i))
				parameterTypesRefined[i] = primitives.get(i);
			else
				parameterTypesRefined[i] = (parameterType instanceof BetterReflectionClass ? (Class<?>) ((BetterReflectionClass) parameterType).getClasz() : (parameterType instanceof Class<?> ? (Class<?>) parameterType : parameterType.getClass()));
		}

		return parameterTypesRefined;
	}
	
	/* The code below is a modified version of this answer: https://stackoverflow.com/a/520344 */

	/**
	 * @param packageName
	 * @return a list of directories matching the package name.
	 * @throws IOException
	 * @throws URISyntaxException
	 */
	public static File[] getDirectoriesFromPackageName(String packageName) throws IOException, URISyntaxException {
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		assert classLoader != null;
		String path = packageName.replace('.', '/');
		Enumeration<URL> resources = classLoader.getResources(path);
		List<File> directories = new ArrayList<>();
		while (resources.hasMoreElements()) {
			URL resource = resources.nextElement();
			directories.add(new File(resource.toURI()));
		}
		
		return directories.toArray(new File[0]);
	}

	/**
	 * Scans all classes accessible from the context class loader which belong to
	 * the given package and subpackages.
	 *
	 * @param packageName The base package
	 * @return The classes
	 * @throws ClassNotFoundException
	 * @throws IOException
	 * @throws URISyntaxException 
	 */
	public static BetterReflectionClass[] getClasses(String packageName) throws ClassNotFoundException, IOException, URISyntaxException {
		File[] directories = getDirectoriesFromPackageName(packageName);
		
		ArrayList<BetterReflectionClass> classes = new ArrayList<>();
		for (File directory : directories) {
			classes.addAll(findClasses(directory, packageName));
		}
		
		return classes.toArray(new BetterReflectionClass[0]);
	}

	/**
	 * Recursive method used to find all classes in a given directory and subdirs.
	 *
	 * @param directory   The base directory
	 * @param packageName The package name for classes found inside the base
	 *                    directory
	 * @return The classes
	 * @throws ClassNotFoundException
	 */
	public static List<BetterReflectionClass> findClasses(File directory, String packageName) throws ClassNotFoundException {
		List<BetterReflectionClass> classes = new ArrayList<>();
		if (!directory.exists())
			return classes;
		
		File[] files = directory.listFiles();
		for (File file : files) {
			if (file.isDirectory()) {
				assert !file.getName().contains(".");
				classes.addAll(findClasses(file, packageName + "." + file.getName()));
			} else if (file.getName().endsWith(".class")) {
				classes.add(new BetterReflectionClass(Class.forName(packageName + '.' + file.getName().substring(0, file.getName().length() - 6))));
			}
		}
		
		return classes;
	}

	/**
	 * Method to find classes which names start with a specific string
	 *
	 * @param directory   The base directory
	 * @param packageName The package name for classes found inside the base
	 *                    directory
	 * @return The classes
	 * @throws ClassNotFoundException
	 * @throws URISyntaxException 
	 * @throws IOException 
	 */
	public static BetterReflectionClass getClassByNameStart(String packageName, String start) throws ClassNotFoundException, IOException, URISyntaxException {
		File[] directories = getDirectoriesFromPackageName(packageName);
		
		for(File directory : directories) {
			File[] files = directory.listFiles();
			for (File file : files)
				if (file.getName().startsWith(start) && file.getName().endsWith(".class"))
					return new BetterReflectionClass(Class.forName(packageName + '.' + file.getName().substring(0, file.getName().length() - 6)));
		}

		return null;
	}

}
