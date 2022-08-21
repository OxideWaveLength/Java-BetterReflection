package me.wavelength.betterreflection;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class BetterReflectionClass {

	private Class<?> clasz;

	private Field[] declaredFields;
	private Field[] fields;
	private Constructor<?>[] declaredConstructors;
	private Constructor<?>[] constructors;
	private Method[] declaredMethods;
	private Method[] methods;

	private Class<?> superClass;

	public BetterReflectionClass(String className) throws ClassNotFoundException {
		this(Class.forName(className));
	}

	public BetterReflectionClass(Class<?> clasz) {
		this.clasz = clasz;

		this.declaredFields = clasz.getDeclaredFields();
		this.fields = clasz.getFields();
		this.declaredConstructors = clasz.getDeclaredConstructors();
		this.constructors = clasz.getConstructors();
		this.declaredMethods = clasz.getDeclaredMethods();
		this.methods = clasz.getMethods();
	}

	/**
	 * @return a new instance of {@link #BetterReflectionClass(String)}, but if a
	 *         ClassNotFoundException is thrown it will return null
	 * @since 0.4
	 */
	public static BetterReflectionClass forName(String name) {
		try {
			return new BetterReflectionClass(name);
		} catch (Exception e) {
			return null;
		}
	}

	public Class<?> getClasz() {
		return clasz;
	}

	/**
	 * @since 0.4
	 */
	public String getName() {
		return clasz.getName();
	}

	/**
	 * @since 0.4
	 */
	public String getSimpleName() {
		return clasz.getSimpleName();
	}

	/**
	 * @since 0.4
	 */
	public String getCanonicalName() {
		return clasz.getCanonicalName();
	}

	/**
	 * @since 0.4
	 */
	public String getTypeName() {
		return clasz.getTypeName();
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
	 * @return
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
	 * @return
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
	 * @param parameterTypes this should be populated ONLY by BetterReflectionClass
	 *                       instances and Classes. Nothing else!
	 * @return
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
	 * @param parameterTypes this should be populated ONLY by BetterReflectionClass
	 *                       instances and Classes. Nothing else!
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

	/**
	 * @param condition the condition to be met to return an array of the class
	 * @return an array of {@link #clasz} if condition is true, {@link #clasz}
	 *         otherwise
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

	public Class<?> getSuperclass() {
		return superClass;
	}

	public boolean isAssignableFrom(Class<?> clasz) {
		return this.clasz.isAssignableFrom(clasz);
	}

	public boolean isAssignableFrom(BetterReflectionClass clasz) {
		return isAssignableFrom(clasz.getClasz());
	}

}