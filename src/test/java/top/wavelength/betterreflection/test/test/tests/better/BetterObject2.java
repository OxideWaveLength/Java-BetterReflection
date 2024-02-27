package top.wavelength.betterreflection.test.test.tests.better;

import top.wavelength.betterreflection.BetterReflectionClass;
import top.wavelength.betterreflection.test.test.Test;
import top.wavelength.betterreflection.test.test.TestClass;

import java.lang.reflect.Field;

public class BetterObject2 extends Test {

	public BetterObject2(int id, TestClass testedObject, BetterReflectionClass<TestClass> betterTestedClass) {
		super(id, "This test is going to get the public field of the test object through better reflections, get its value and store it inside of a local variable.", testedObject, betterTestedClass);
	}

	@Override
	public long start() {
		startTime = System.nanoTime();
		for (int i = 0; i < testAmount; i++) {
			Field field = betterTestedClass.getField("publicFinalField");
			try {
				field.get(testedObject);
			} catch (IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		return System.nanoTime() - startTime;
	}

}