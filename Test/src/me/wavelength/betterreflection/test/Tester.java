package me.wavelength.betterreflection.test;

import me.wavelength.betterreflection.BetterReflection;
import me.wavelength.betterreflection.BetterReflectionClass;
import me.wavelength.betterreflection.test.test.Test;
import me.wavelength.betterreflection.test.test.TestClass;
import me.wavelength.betterreflection.test.test.tests.better.BetterObject1;
import me.wavelength.betterreflection.test.test.tests.better.BetterObject2;
import me.wavelength.betterreflection.test.test.tests.better.BetterObject3;
import me.wavelength.betterreflection.test.test.tests.better.BetterObject4;
import me.wavelength.betterreflection.test.test.tests.traditional.TraditionalTest1;
import me.wavelength.betterreflection.test.test.tests.traditional.TraditionalTest2;
import me.wavelength.betterreflection.test.test.tests.traditional.TraditionalTest3;
import me.wavelength.betterreflection.test.test.tests.traditional.TraditionalTest4;
import me.wavelength.betterreflection.test.test.tests.traditional.TraditionalTest5;
import me.wavelength.betterreflection.test.test.tests.traditional.TraditionalTest6;

public class Tester {

	public static void main(String[] args) {
		new Tester();
	}

	private final BetterReflection betterReflection;

	private final Test[] tests;

	private final Test[] betterTests;

	public Tester() {
		tests = new Test[12];
		betterTests = new Test[4];

		TestClass testedObject = new TestClass();

		this.betterReflection = new BetterReflection();
		BetterReflectionClass betterTestedObject = betterReflection.getBetterReflectionClass(TestClass.class);

		int id = 1;
		/** Normal Reflections - Not Cached TestedObject */
		tests[0] = new TraditionalTest1(id++);
		tests[1] = new TraditionalTest2(id++);
		tests[2] = new TraditionalTest3(id++);
		tests[3] = new TraditionalTest4(id++);
		tests[4] = new TraditionalTest5(id++);
		tests[5] = new TraditionalTest6(id++);

		/** Normal Reflections - Cached TestedObject */
		tests[6] = new TraditionalTest1(id++, testedObject);
		tests[7] = new TraditionalTest2(id++, testedObject);
		tests[8] = new TraditionalTest3(id++, testedObject);
		tests[9] = new TraditionalTest4(id++, testedObject);
		tests[10] = new TraditionalTest5(id++, testedObject);
		tests[11] = new TraditionalTest6(id++, testedObject);

		/** Better TestedObject */
		id = 1;
		betterTests[0] = new BetterObject1(id++, testedObject, betterTestedObject);
		betterTests[1] = new BetterObject2(id++, testedObject, betterTestedObject);
		betterTests[2] = new BetterObject3(id++, testedObject, betterTestedObject);
		betterTests[3] = new BetterObject4(id++, testedObject, betterTestedObject);

		startTests();
	}

	private void startTest(Test test) {
		System.out.println(new StringBuilder("-------- Starting Test ").append(test.getId()).append(" --------").toString());
		System.out.println(new StringBuilder("Description: ").append(test.getDescription()).toString());
		long timeTaken = test.start();
		System.out.println(new StringBuilder("The test has taken about ").append(timeTaken).append("ms.").toString());
	}

	public void startTests() {
		System.out.println("Now testing Normal Reflections.\n");
		for (Test test : tests) {
			startTest(test);
		}
		System.out.println("\nNow testing Better Reflections.\n");
		for (Test test : betterTests) {
			startTest(test);
		}
		System.out.println("\nNow testing Normal Reflections. (Again)\n");
		for (Test test : tests) {
			startTest(test);
		}
	}

}