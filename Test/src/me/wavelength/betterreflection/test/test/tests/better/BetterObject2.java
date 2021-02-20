package me.wavelength.betterreflection.test.test.tests.better;

import java.lang.reflect.Field;

import me.wavelength.betterreflection.BetterReflectionClass;
import me.wavelength.betterreflection.test.test.Test;
import me.wavelength.betterreflection.test.test.TestClass;

public class BetterObject2 extends Test {

	public BetterObject2(int id, TestClass testedObject, BetterReflectionClass betterTestedClass) {
		super(id, "This test is going to get the public field of the test object through better reflections, get its value and store it inside of a local variable.", testedObject, betterTestedClass);
	}

	@Override
	public long start() {
		timer.reset();
		for (int i = 0; i < testAmount; i++) {
			Field field = betterTestedClass.getField("publicFinalField");
			try {
				String value = (String) field.get(testedObject);
			} catch (IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		return timer.getTimePassed();
	}

}