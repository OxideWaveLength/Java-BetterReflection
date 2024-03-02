package top.wavelength.betterreflection.dumper.all;

import top.wavelength.betterreflection.BetterReflectionClass;
import top.wavelength.betterreflection.dumper.Dumper;
import top.wavelength.betterreflection.dumper.implementation.MethodDumper;

import java.lang.reflect.Method;

/**
 * The AllMethodsDumper class is responsible for dumping the methods of a class.
 * It implements the Dumper interface and operates on an object of type {@link BetterReflectionClass}.
 *
 * @since 1.2
 */
public class AllMethodsDumper implements Dumper<BetterReflectionClass<?>> {

	private final MethodDumper methodDumper;

	/**
	 * Constructs an AllMethodsDumper with a specified MethodDumper.
	 *
	 * @param methodDumper the MethodDumper to be used for dumping.
	 * @since 1.2
	 */
	public AllMethodsDumper(MethodDumper methodDumper) {
		this.methodDumper = methodDumper;
	}

	/**
	 * Default constructor for AllMethodsDumper.
	 * Initializes a new MethodDumper.
	 *
	 * @since 1.2
	 */
	public AllMethodsDumper() {
		this(new MethodDumper());
	}

	/**
	 * Dumps all the methods of a given BetterReflectionClass.
	 *
	 * @param clasz the class to be dumped
	 * @return a string representation of the dumped methods
	 * @since 1.2
	 */
	@Override
	public String dump(BetterReflectionClass<?> clasz) {
		StringBuilder result = new StringBuilder();
		for (Method method : clasz.getMethods())
			result.append(methodDumper.dump(method)).append("\n");
		return result.toString();
	}
}