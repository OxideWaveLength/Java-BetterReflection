package top.wavelength.betterreflection.test.test.tests.better;

import top.wavelength.betterreflection.BetterReflectionClass;
import top.wavelength.betterreflection.test.test.Test;
import top.wavelength.betterreflection.test.test.TestClass;

public class BetterObject3 extends Test {

	public BetterObject3(int id, TestClass testedObject, BetterReflectionClass<TestClass> betterTestedClass) {
		super(id, "This test gets the private field of the test object through better reflections.", testedObject, betterTestedClass);
	}

	@Override
	public long start() {
		startTime = System.nanoTime();
		for (int i = 0; i < testAmount; i++)
			betterTestedClass.getDeclaredField("privateFinalField");
		return System.nanoTime() - startTime;
	}

}