package me.wavelength.betterreflection.test.test.tests.traditional;

import java.lang.reflect.Field;

import me.wavelength.betterreflection.test.test.Test;
import me.wavelength.betterreflection.test.test.TestClass;

public class TraditionalTest6 extends Test {

	public TraditionalTest6(int id, TestClass testedObject) {
		super(id, "This test gets the private field of the test object through normal reflections, caches it, gets its value and stores it inside of a local variable.", testedObject);
	}

	public TraditionalTest6(int id) {
		this(id, new TestClass());
	}

	@Override
	public long start() {
		startTime = System.nanoTime();
		Field field = null;
		try {
			field = testedObject.getClass().getDeclaredField("privateFinalField");
		} catch (NoSuchFieldException | SecurityException e1) {
			e1.printStackTrace();
		}
		assert field != null;
		for (int i = 0; i < testAmount; i++) {
			try {
				field.setAccessible(true);
				field.get(testedObject);
			} catch (SecurityException | IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		return System.nanoTime() - startTime;
	}

}