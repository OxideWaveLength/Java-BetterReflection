package me.wavelength.betterreflection.test.test;

import me.wavelength.betterreflection.BetterReflectionClass;

public abstract class Test {

	protected final int id;

	protected final String description;

	protected final int testAmount;

	protected long startTime;

	protected final TestClass testedObject;
	protected final BetterReflectionClass betterTestedClass;

	public Test(int id, String description, int testAmount, TestClass testedObject, BetterReflectionClass betterTestedClass) {
		this.id = id;
		this.description = description;
		this.testAmount = testAmount;

		this.testedObject = testedObject;
		this.betterTestedClass = betterTestedClass;
	}

	public Test(int id, String description, int testAmount) {
		this(id, description, testAmount, null, null);
	}

	public Test(int id, String description) {
		this(id, description, null);
	}

	public Test(int id, String description, TestClass testedObject) {
		this(id, description, 10000000, testedObject, null);
	}

	public Test(int id, String description, TestClass testedObject, BetterReflectionClass betterTestedClass) {
		this(id, description, 10000000, testedObject, betterTestedClass);
	}

	public int getId() {
		return id;
	}

	public String getDescription() {
		return description;
	}

	public abstract long start();

}