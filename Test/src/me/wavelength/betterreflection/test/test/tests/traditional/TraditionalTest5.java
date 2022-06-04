package me.wavelength.betterreflection.test.test.tests.traditional;

import java.lang.reflect.Field;

import me.wavelength.betterreflection.test.test.Test;
import me.wavelength.betterreflection.test.test.TestClass;

public class TraditionalTest5 extends Test {

	public TraditionalTest5(int id, TestClass testedObject) {
		super(id, "This test gets the public field of the test object through normal reflections, caches it, gets its value and stores it inside of a local variable.", testedObject);
	}

	public TraditionalTest5(int id) {
		this(id, new TestClass());
	}

	@Override
	public long start() {
		startTime = System.nanoTime();
		Field field = null;
		try {
			field = testedObject.getClass().getField("publicFinalField");
		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException e) {
			e.printStackTrace();
		}
		assert field != null;
		for (int i = 0; i < testAmount; i++) {
			try {
				field.get(testedObject);
			} catch (IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		return System.nanoTime() - startTime;
	}

}