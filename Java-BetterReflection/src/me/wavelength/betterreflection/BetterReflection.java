package me.wavelength.betterreflection;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * This class simply caches Reflections. In absence of a name "BetterReflection" has been chosen.
 **/
public class BetterReflection {
	
	private final CopyOnWriteArrayList<BetterReflectionClass> betterReflectionClasses;

	public static final BetterReflectionClass FIELD = new BetterReflectionClass(Field.class);

	private static final Map<Class<?>, BetterReflectionClass> PRIMITIVES = new HashMap<>();;

	public BetterReflection() {
		this.betterReflectionClasses = new CopyOnWriteArrayList<>();

		PRIMITIVES.put(Double.class, getBetterReflectionClass(Double.class));
		PRIMITIVES.put(Integer.class, getBetterReflectionClass(Integer.class));
		PRIMITIVES.put(Float.class, getBetterReflectionClass(Float.class));
		PRIMITIVES.put(Boolean.class, getBetterReflectionClass(Boolean.class));
		PRIMITIVES.put(Long.class, getBetterReflectionClass(Long.class));
	}

	public BetterReflectionClass getBetterReflectionClass(String name) throws ClassNotFoundException {
		for (BetterReflectionClass betterReflectionClass : betterReflectionClasses) {
			if (betterReflectionClass.getClasz().getCanonicalName() != null && betterReflectionClass.getClasz().getCanonicalName().equals(name))
				return betterReflectionClass;
		}

		this.betterReflectionClasses.add(new BetterReflectionClass(name));
		return this.betterReflectionClasses.get(this.betterReflectionClasses.size() - 1);
	}

	public BetterReflectionClass getBetterReflectionClass(Class<?> clasz) {
		for (BetterReflectionClass betterReflectionClass : betterReflectionClasses) {
			if (betterReflectionClass.getClasz().equals(clasz))
				return betterReflectionClass;
		}

		this.betterReflectionClasses.add(new BetterReflectionClass(clasz));
		return this.betterReflectionClasses.get(this.betterReflectionClasses.size() - 1);
	}

	public static Map<Class<?>, BetterReflectionClass> getPrimitives() {
		return PRIMITIVES;
	}

	public static Object getFieldValue(BetterReflectionClass betterReflectionClass, Object instance, String fieldName) throws IllegalAccessException {
		return betterReflectionClass.getDeclaredField(fieldName).get(instance);
	}

}