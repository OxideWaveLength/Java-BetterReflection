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

}