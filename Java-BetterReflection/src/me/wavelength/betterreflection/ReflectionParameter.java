package me.wavelength.betterreflection;

public class ReflectionParameter {

	private final Object value;
	private final Class<?> type;

	public ReflectionParameter(Object value, Class<?> type) {
		this.value = value;
		this.type = type;
	}

	public ReflectionParameter(Object value) {
		this.value = value;
		this.type = value.getClass();
	}

	public Object getValue() {
		return value;
	}

	public Class<?> getType() {
		return type;
	}

}