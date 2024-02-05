package top.wavelength.betterreflection.test.test.tests.traditional;

import top.wavelength.betterreflection.test.test.Test;
import top.wavelength.betterreflection.test.test.TestClass;

public class TraditionalTest2 extends Test {

	public TraditionalTest2(int id, TestClass testedObject) {
		super(id, "This test is going to get the public field of the test object through normal reflections, get its value and store it inside of a local variable.", testedObject);
	}

	public TraditionalTest2(int id) {
		this(id, new TestClass());
	}

	@Override
	public long start() {
		startTime = System.nanoTime();
		for (int i = 0; i < testAmount; i++) {
			try {
				testedObject.getClass().getField("publicFinalField").get(testedObject);
			} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		return System.nanoTime() - startTime;
	}

}