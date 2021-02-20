package me.wavelength.betterreflection.test.test.tests.traditional;

import java.lang.reflect.Field;

import me.wavelength.betterreflection.test.test.Test;
import me.wavelength.betterreflection.test.test.TestClass;

public class TraditionalTest3 extends Test {

	public TraditionalTest3(int id, TestClass testedObject) {
		super(id, "This test gets the private field of the test object through normal reflections.", testedObject);
	}

	public TraditionalTest3(int id) {
		this(id, new TestClass());
	}

	@Override
	public long start() {
		timer.reset();
		for (int i = 0; i < testAmount; i++) {
			try {
				Field field = testedObject.getClass().getDeclaredField("privateFinalField");
			} catch (NoSuchFieldException | SecurityException e) {
				e.printStackTrace();
			}
		}
		return timer.getTimePassed();
	}

}