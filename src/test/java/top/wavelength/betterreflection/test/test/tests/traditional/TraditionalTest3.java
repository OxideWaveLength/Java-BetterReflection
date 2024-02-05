package top.wavelength.betterreflection.test.test.tests.traditional;

import top.wavelength.betterreflection.test.test.Test;
import top.wavelength.betterreflection.test.test.TestClass;

public class TraditionalTest3 extends Test {

	public TraditionalTest3(int id, TestClass testedObject) {
		super(id, "This test gets the private field of the test object through normal reflections.", testedObject);
	}

	public TraditionalTest3(int id) {
		this(id, new TestClass());
	}

	@Override
	public long start() {
		startTime = System.nanoTime();
		for (int i = 0; i < testAmount; i++) {
			try {
				testedObject.getClass().getDeclaredField("privateFinalField");
			} catch (NoSuchFieldException | SecurityException e) {
				e.printStackTrace();
			}
		}
		return System.nanoTime() - startTime;
	}

}