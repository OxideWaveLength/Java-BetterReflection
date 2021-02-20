package me.wavelength.betterreflection.test.test.tests.traditional;

import java.lang.reflect.Field;

import me.wavelength.betterreflection.test.test.Test;
import me.wavelength.betterreflection.test.test.TestClass;

public class TraditionalTest4 extends Test {

	public TraditionalTest4(int id, TestClass testedObject) {
		super(id, "This test is gets the private field of the test object through normal reflections, sets the field to be accessible, gets its value and stores it inside of a local variable.", testedObject);
	}

	public TraditionalTest4(int id) {
		this(id, new TestClass());
	}

	@Override
	public long start() {
		timer.reset();
		for (int i = 0; i < testAmount; i++) {
			try {
				Field field = testedObject.getClass().getDeclaredField("privateFinalField");
				field.setAccessible(true);
				String value = (String) field.get(testedObject);
			} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		return timer.getTimePassed();
	}

}