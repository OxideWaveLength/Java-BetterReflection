package top.wavelength.betterreflection.test.test.tests.traditional;

import top.wavelength.betterreflection.test.test.Test;
import top.wavelength.betterreflection.test.test.TestClass;

import java.lang.reflect.Field;

public class TraditionalTest4 extends Test {

	public TraditionalTest4(int id, TestClass testedObject) {
		super(id, "This test is gets the private field of the test object through normal reflections, sets the field to be accessible, gets its value and stores it inside of a local variable.", testedObject);
	}

	public TraditionalTest4(int id) {
		this(id, new TestClass());
	}

	@Override
	public long start() {
		startTime = System.nanoTime();
		for (int i = 0; i < testAmount; i++) {
			try {
				Field field = testedObject.getClass().getDeclaredField("privateFinalField");
				field.setAccessible(true);
				field.get(testedObject);
			} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		return System.nanoTime() - startTime;
	}

}