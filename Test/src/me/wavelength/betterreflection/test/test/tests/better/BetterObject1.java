package me.wavelength.betterreflection.test.test.tests.better;

import me.wavelength.betterreflection.BetterReflectionClass;
import me.wavelength.betterreflection.test.test.Test;
import me.wavelength.betterreflection.test.test.TestClass;

public class BetterObject1 extends Test {

	public BetterObject1(int id, TestClass testedObject, BetterReflectionClass betterTestedClass) {
		super(id, "This test gets the public field of the test object through better reflections.", testedObject, betterTestedClass);
	}

	@Override
	public long start() {
		startTime = System.nanoTime();
		for (int i = 0; i < testAmount; i++)
			betterTestedClass.getField("publicFinalField");
		return System.nanoTime() - startTime;
	}

}