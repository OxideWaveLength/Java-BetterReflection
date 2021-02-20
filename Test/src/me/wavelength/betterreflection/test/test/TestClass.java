package me.wavelength.betterreflection.test.test;

import java.util.UUID;

public class TestClass {

	public final String publicFinalField;
	private final String privateFinalField;

	public TestClass() {
		this.publicFinalField = UUID.randomUUID().toString();
		this.privateFinalField = UUID.randomUUID().toString();
	}

	public String publicFieldGetter() {
		return publicFinalField;
	}

	private String privateFieldGetter() {
		return privateFinalField;
	}

	protected String protectedFieldGetter() {
		return privateFinalField;
	}

}