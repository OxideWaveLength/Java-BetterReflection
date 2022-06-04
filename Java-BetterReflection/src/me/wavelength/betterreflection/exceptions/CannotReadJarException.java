package me.wavelength.betterreflection.exceptions;

public class CannotReadJarException extends RuntimeException {
	private static final long serialVersionUID = 5281245870042413117L;

	public CannotReadJarException(String fileName) {
		super("Cannot read the file " + fileName);
	}

}