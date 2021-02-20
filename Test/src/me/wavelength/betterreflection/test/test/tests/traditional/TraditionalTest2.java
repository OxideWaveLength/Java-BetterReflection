package me.wavelength.betterreflection.test.test.tests.traditional;

import java.lang.reflect.Field;

import me.wavelength.betterreflection.test.test.Test;
import me.wavelength.betterreflection.test.test.TestClass;

public class TraditionalTest2 extends Test {

	public TraditionalTest2(int id, TestClass testedObject) {
		super(id, "This test is going to get the public field of the test object through normal reflections, get its value and store it inside of a local variable.", testedObject);
	}

	public TraditionalTest2(int id) {
		this(id, new TestClass());
	}

	@Override
	public long start() {
		timer.reset();
		for (int i = 0; i < testAmount; i++) {
			try {
				Field field = testedObject.getClass().getField("publicFinalField");
				String value = (String) field.get(testedObject);
			} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		return timer.getTimePassed();
	}

}