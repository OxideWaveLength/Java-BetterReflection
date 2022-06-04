package me.wavelength.betterreflection.test.test.tests.better;

import java.lang.reflect.Field;

import me.wavelength.betterreflection.BetterReflectionClass;
import me.wavelength.betterreflection.test.test.Test;
import me.wavelength.betterreflection.test.test.TestClass;

public class BetterObject4 extends Test {

	public BetterObject4(int id, TestClass testedObject, BetterReflectionClass betterTestedClass) {
		super(id, "This test is gets the private field of the test object through better reflections, sets the field to be accessible, gets its value and stores it inside of a local variable.", testedObject, betterTestedClass);
	}

	@Override
	public long start() {
		startTime = System.nanoTime();
		for (int i = 0; i < testAmount; i++) {
			Field field = betterTestedClass.getDeclaredField("privateFinalField");
			field.setAccessible(true);
			try {
				field.get(testedObject);
			} catch (SecurityException | IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		return System.nanoTime() - startTime;
	}

}