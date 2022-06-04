package me.wavelength.betterreflection.test.test.tests.traditional;

import me.wavelength.betterreflection.test.test.Test;
import me.wavelength.betterreflection.test.test.TestClass;

public class TraditionalTest1 extends Test {

	public TraditionalTest1(int id, TestClass testedObject) {
		super(id, "This test gets the public field of the test object through normal reflections.", testedObject);
	}

	public TraditionalTest1(int id) {
		this(id, new TestClass());
	}

	@Override
	public long start() {
		startTime = System.nanoTime();
		for (int i = 0; i < testAmount; i++) {
			try {
				testedObject.getClass().getField("publicFinalField");
			} catch (NoSuchFieldException | SecurityException e) {
				e.printStackTrace();
			}
		}
		return System.nanoTime() - startTime;
	}

}