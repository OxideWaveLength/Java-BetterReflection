package me.wavelength.betterreflection.test.test.tests.better;

import java.lang.reflect.Field;

import me.wavelength.betterreflection.BetterReflectionClass;
import me.wavelength.betterreflection.test.test.Test;
import me.wavelength.betterreflection.test.test.TestClass;

public class BetterObject1 extends Test {

	public BetterObject1(int id, TestClass testedObject, BetterReflectionClass betterTestedClass) {
		super(id, "This test gets the public field of the test object through better reflections.", testedObject, betterTestedClass);
	}

	@Override
	public long start() {
		timer.reset();
		for (int i = 0; i < testAmount; i++) {
			Field field = betterTestedClass.getField("publicFinalField");
		}
		return timer.getTimePassed();
	}

}