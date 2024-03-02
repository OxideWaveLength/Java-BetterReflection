package top.wavelength.betterreflection.dumper.all;

import top.wavelength.betterreflection.BetterReflectionClass;
import top.wavelength.betterreflection.dumper.Dumper;
import top.wavelength.betterreflection.dumper.implementation.ConstructorDumper;

import java.lang.reflect.Constructor;

/**
 * The AllConstructorsDumper class is responsible for dumping the constructors of a class.
 * It implements the Dumper interface and operates on an object of type {@link BetterReflectionClass}.
 *
 * @since 1.2
 */
public class AllConstructorsDumper implements Dumper<BetterReflectionClass<?>> {

	private final ConstructorDumper constructorDumper;

	/**
	 * Constructs an AllConstructorsDumper with a specified ConstructorDumper.
	 *
	 * @param constructorDumper the ConstructorDumper to be used for dumping.
	 * @since 1.2
	 */
	public AllConstructorsDumper(ConstructorDumper constructorDumper) {
		this.constructorDumper = constructorDumper;
	}

	/**
	 * Default constructor for AllConstructorsDumper.
	 * Initializes a new ConstructorDumper.
	 *
	 * @since 1.2
	 */
	public AllConstructorsDumper() {
		this(new ConstructorDumper());
	}

	/**
	 * Dumps all the constructors of a given BetterReflectionClass.
	 *
	 * @param clasz the class to be dumped
	 * @return a string representation of the dumped constructors
	 * @since 1.2
	 */
	@Override
	public String dump(BetterReflectionClass<?> clasz) {
		StringBuilder result = new StringBuilder();
		for (Constructor<?> constructor : clasz.getConstructors())
			result.append(constructorDumper.dump(constructor)).append("\n");
		return result.toString();
	}
}