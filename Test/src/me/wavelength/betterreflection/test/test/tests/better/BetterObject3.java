package me.wavelength.betterreflection.test.test.tests.better;

import java.lang.reflect.Field;

import me.wavelength.betterreflection.BetterReflectionClass;
import me.wavelength.betterreflection.test.test.Test;
import me.wavelength.betterreflection.test.test.TestClass;

public class BetterObject3 extends Test {

	public BetterObject3(int id, TestClass testedObject, BetterReflectionClass betterTestedClass) {
		super(id, "This test gets the private field of the test object through better reflections.", testedObject, betterTestedClass);
	}

	@Override
	public long start() {
		timer.reset();
		for (int i = 0; i < testAmount; i++) {
			Field field = betterTestedClass.getDeclaredField("privateFinalField");
		}
		return timer.getTimePassed();
	}

}