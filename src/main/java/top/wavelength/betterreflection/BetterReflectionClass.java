package top.wavelength.betterreflection;

import java.io.File;
import java.lang.reflect.*;
import java.security.ProtectionDomain;
import java.util.HashMap;
import java.util.Map;
import java.util.jar.JarFile;

public class BetterReflectionClass {

	/**
	 * @since 0.6
	 */
	private final String name;
	/**
	 * @since 0.6
	 */
	private final String simpleName;
	/**
	 * @since 0.6
	 */
	private final String canonicalName;
	/**
	 * @since 0.6
	 */
	private final String typeName;
	private final String packageName;

	private Class<?> clasz;

	private final Field[] declaredFields;
	private final Field[] fields;
	private final Constructor<?>[] declaredConstructors;
	private final Constructor<?>[] constructors;
	private final Method[] declaredMethods;
	private final Method[] methods;

	/**
	 * @since 0.7
	 */
	private final ProtectionDomain protectionDomain;

	/**
	 * @since 0.6
	 */
	private final Class<?> superClass;
	/**
	 * @since 0.7
	 */
	private BetterReflectionClass betterReflectionSuperClass;

	/**
	 * Null until {@link #isRunningFromJar()} is invoked, then a mirror of
	 * {@link BetterReflectionUtils#isRunningFromJar(BetterReflectionClass)}
	 *
	 * @since 0.7
	 */
	private Boolean runningFromJar;
	/**
	 * Null until {@link #getJar()} is invoked, then a mirror of
	 * {@link BetterReflectionUtils#getCurrentJar(BetterReflectionClass)}
	 *
	 * @since 0.7
	 */
	private JarFile jar;
	/**
	 * Null until {@link #getJarFile()} is invoked, then a mirror of
	 * {@link BetterReflectionUtils#getCurrentJarFile(BetterReflectionClass)}
	 *
	 * @since 0.7
	 */
	private File jarFile;

	public BetterReflectionClass(String className) throws ClassNotFoundException {
		this(Class.forName(className));
	}

	/**
	 * @param clasz the class to wrap
	 * @since 0.7
	 */
	public BetterReflectionClass(BetterReflectionClass clasz) {
		this(clasz.getClasz());
	}

	public BetterReflectionClass(Class<?> clasz) {
		this.clasz = clasz;

		this.name = clasz.getName();
		this.simpleName = clasz.getSimpleName();
		this.canonicalName = clasz.getCanonicalName();
		this.typeName = clasz.getTypeName();
		this.packageName = name.contains(".") ? name.substring(0, name.lastIndexOf(".")) : "";

		this.declaredFields = clasz.getDeclaredFields();
		this.fields = clasz.getFields();
		this.declaredConstructors = clasz.getDeclaredConstructors();
		this.constructors = clasz.getConstructors();
		this.declaredMethods = clasz.getDeclaredMethods();
		this.methods = clasz.getMethods();
		this.superClass = clasz.getSuperclass();

		this.protectionDomain = clasz.getProtectionDomain();
	}

	/**
	 * @param name the class' name
	 * @return a new instance of {@link #BetterReflectionClass(String)}, but if a
	 * ClassNotFoundException is thrown it will return null
	 * @since 0.4
	 */
	public static BetterReflectionClass forName(String name) {
		try {
			return new BetterReflectionClass(name);
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * @param name the class' name
	 * @return a BetterReflectionClass wrapping the class' Array. Null if the class
	 * is not found.
	 * @since 0.7
	 */
	public static BetterReflectionClass forNameAsArray(String name) {
		BetterReflectionClass clasz = forName(name);
		if (clasz == null)
			return null;
		return clasz.getBetterReflectionArrayClass();
	}

	public Class<?> getClasz() {
		return clasz;
	}

	/**
	 * @return the class' name
	 * @since 0.4
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the class' <code>Simple Name</code> ({@link Class#getSimpleName()})
	 * @since 0.4
	 */
	public String getSimpleName() {
		return simpleName;
	}

	/**
	 * @return the class' <code>Canonical Name</code> ({@link Class#getCanonicalName()})
	 * @since 0.4
	 */
	public String getCanonicalName() {
		return canonicalName;
	}

	/**
	 * @return the class' <code>Type Name</code> ({@link Class#getTypeName()})
	 * @since 0.4
	 */
	public String getTypeName() {
		return typeName;
	}

	public Field getDeclaredField(String name) {
		return BetterReflectionUtils.getField(name, declaredFields);
	}

	public Field[] getDeclaredFields() {
		return declaredFields;
	}

	public Field getField(String name) {
		return BetterReflectionUtils.getField(name, fields);
	}

	public Field[] getFields() {
		return fields;
	}

	/**
	 * @param parameterTypes this should be populated ONLY by BetterReflectionClass
	 *                       instances and Classes. Nothing else!
	 * @return a constructor from its parameter types
	 */
	public Constructor<?> getDeclaredConstructor(Object... parameterTypes) {
		return getConstructor(BetterReflectionUtils.getTypes(parameterTypes));
	}

	public Constructor<?> getDeclaredConstructor(Class<?>... parameterTypes) {
		return BetterReflectionUtils.getConstructor(parameterTypes, declaredConstructors);
	}

	public Constructor<?>[] getDeclaredConstructors() {
		return declaredConstructors;
	}

	/**
	 * @param parameterTypes this should be populated ONLY by BetterReflectionClass
	 *                       instances and Classes. Nothing else!
	 * @return a constructor from its parameter types
	 */
	public Constructor<?> getConstructor(Object... parameterTypes) {
		return getConstructor(BetterReflectionUtils.getTypes(parameterTypes));
	}

	public Constructor<?> getConstructor(Class<?>... parameterTypes) {
		return BetterReflectionUtils.getConstructor(parameterTypes, constructors);
	}

	public Constructor<?>[] getConstructors() {
		return constructors;
	}

	/**
	 * @param name           The method's name
	 * @param parameterTypes this should be populated ONLY by BetterReflectionClass
	 *                       instances and Classes. Nothing else!
	 * @return a method from its name and parameter types
	 */
	public Method getDeclaredMethod(String name, Object... parameterTypes) {
		return getMethod(name, BetterReflectionUtils.getTypes(parameterTypes));
	}

	public Method getDeclaredMethod(String name, Class<?>... parameterTypes) {
		return BetterReflectionUtils.getMethod(name, parameterTypes, declaredMethods);
	}

	public Method[] getDeclaredMethods() {
		return declaredMethods;
	}

	/**
	 * @param name           The method's name
	 * @param parameterTypes this should be populated ONLY by BetterReflectionClass
	 *                       instances and Classes. Nothing else!
	 * @return a method from its name and parameter types
	 */
	public Method getMethod(String name, Object... parameterTypes) {
		return getMethod(name, BetterReflectionUtils.getTypes(parameterTypes));
	}

	public Method getMethod(String name, Class<?>... parameterTypes) {
		return BetterReflectionUtils.getMethod(name, parameterTypes, methods);
	}

	public Method[] getMethods() {
		return methods;
	}

	public ProtectionDomain getProtectionDomain() {
		return protectionDomain;
	}

	public Object newInstance() throws InstantiationException, IllegalAccessException {
		return clasz.newInstance();
	}

	public Object cast(Object object) {
		return clasz.cast(object);
	}

	public void invokeMethods(Map<String, Object[]> methods, Object instance) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		for (String methodName : methods.keySet()) {
			Object[] parameters = methods.get(methodName);
			Map<Integer, Class<?>> primitives = new HashMap<Integer, Class<?>>();
			for (int i = 0; i < parameters.length; i++) {
				Object parameter = parameters[i];
				if (!(parameter instanceof ReflectionParameter))
					continue;
				ReflectionParameter reflectionParameter = (ReflectionParameter) parameter;
				if (reflectionParameter.getType().isPrimitive()) {
					primitives.put(i, reflectionParameter.getType());
					parameters[i] = BetterReflection.getPrimitives().get(reflectionParameter.getValue().getClass()).getMethod(String.format("%sValue", reflectionParameter.getType().getName())).invoke(reflectionParameter.getValue());
				}
			}
			Method method = getMethod(methodName, BetterReflectionUtils.getTypes(primitives, parameters));
			method.invoke(instance, parameters);
		}
	}

	public boolean isInstance(Object object) {
		return clasz.isInstance(object);
	}

	public <U> Class<? extends U> asSubclass(Class<U> clasz) {
		return this.clasz.asSubclass(clasz);
	}

	public Class<?> getArrayClass() {
		return Array.newInstance(clasz, 0).getClass();
	}

	public BetterReflectionClass getBetterReflectionArrayClass() {
		return new BetterReflectionClass(Array.newInstance(clasz, 0).getClass());
	}

	/**
	 * @param condition the condition to be met to return an array of the class
	 * @return an array of {@link #clasz} if condition is true, {@link #clasz}
	 * otherwise
	 */
	public Class<?> getArrayClassIf(boolean condition) {
		return condition ? getArrayClass() : clasz;
	}

	public Object getArray(Object... content) {
		Object array = Array.newInstance(clasz, content.length);
		for (int i = 0; i < content.length; i++) {
			Array.set(array, i, content[i]);
		}

		return array;
	}

	public Object getDeclaredFieldValue(Object instance, String fieldName) throws IllegalAccessException {
		return getDeclaredField(fieldName).get(instance);
	}

	public Object invokeMethod(Object instance, String methodName, Object... parameters) throws InvocationTargetException, IllegalAccessException {
		return getMethod(methodName, BetterReflectionUtils.getTypes(parameters)).invoke(instance, parameters);
	}

	public Object invokeDeclaredMethod(Object instance, String methodName, Object... parameters) throws InvocationTargetException, IllegalAccessException {
		Method method = getDeclaredMethod(methodName, BetterReflectionUtils.getTypes(parameters));
		boolean accessible = method.isAccessible();
		if (!accessible)
			method.setAccessible(true);
		Object result = method.invoke(instance, parameters);
		if (!accessible)
			method.setAccessible(false);
		return result;
	}

	public String getPackageName() {
		return packageName;
	}

	/**
	 * @return The class' <code>Super Class</code> ({@link Class#getSuperclass()})
	 * @since 0.6
	 */
	public Class<?> getSuperclass() {
		return superClass;
	}

	/**
	 * @return {@link #getSuperclass()} as {@link BetterReflectionClass}
	 * @since 0.7
	 */
	public BetterReflectionClass getBetterReflectionSuperClass() {
		if (betterReflectionSuperClass == null)
			betterReflectionSuperClass = new BetterReflectionClass(superClass);
		return betterReflectionSuperClass;
	}

	/**
	 * @return whether this class has been loaded from a jar file
	 * @since 0.7
	 */
	public boolean isRunningFromJar() {
		if (runningFromJar == null)
			runningFromJar = BetterReflectionUtils.isRunningFromJar(this);
		return runningFromJar;
	}

	/**
	 * @return the jar this class has been loaded from
	 * @since 0.7
	 */
	public JarFile getJar() {
		if (isRunningFromJar() && jar == null)
			jar = BetterReflectionUtils.getCurrentJar(this);
		return jar;
	}

	/**
	 * @return the jar file this class has been loaded from
	 * @since 0.7
	 */
	public File getJarFile() {
		if (isRunningFromJar() && jarFile == null)
			jarFile = BetterReflectionUtils.getCurrentJarFile(this);
		return jarFile;
	}

	public boolean isAssignableFrom(Class<?> clasz) {
		return this.clasz.isAssignableFrom(clasz);
	}

	public boolean isAssignableFrom(BetterReflectionClass clasz) {
		return isAssignableFrom(clasz.getClasz());
	}

	/**
	 * Write the all the method headers into stout
	 *
	 * @since 0.7
	 */
	public void dumpMethodHeaders() {
		dumpMethodHeaders(true);
	}

	/**
	 * Write the all the method headers into stout
	 *
	 * @param includeModifiers whether the method's modifiers should be dumped
	 * @since 0.7
	 */
	public void dumpMethodHeaders(boolean includeModifiers) {
		dumpMethodHeaders(includeModifiers, true);
	}

	/**
	 * Write the all the method headers into stout
	 *
	 * @param includeModifiers  whether the method's modifiers should be dumped
	 * @param includeReturnType whether the method's return type should be dumped
	 * @since 0.7
	 */
	public void dumpMethodHeaders(boolean includeModifiers, boolean includeReturnType) {
		dumpMethodHeaders(includeModifiers, includeReturnType, true);
	}

	/**
	 * Write the all the method headers into stout
	 *
	 * @param includeModifiers      whether the method's modifiers should be dumped
	 * @param includeReturnType     whether the method's return type should be dumped
	 * @param includeParameterNames whether the method's parameter names should be dumped
	 * @since 0.7
	 */
	public void dumpMethodHeaders(boolean includeModifiers, boolean includeReturnType, boolean includeParameterNames) {
		for (Method method : getDeclaredMethods())
			BetterReflectionUtils.dumpMethodHeader(method, includeModifiers, includeReturnType, includeParameterNames);
	}

	/**
	 * Uses Unsafe to allocate an instance of this class without invoking its constructor.
	 *
	 * @return a new instance of the current class
	 * @since 1.0
	 */
	public Object allocateUnsafeInstance() throws IllegalAccessException, InstantiationException {
		return BetterReflectionUtils.allocateUnsafeInstance(clasz);
	}

}