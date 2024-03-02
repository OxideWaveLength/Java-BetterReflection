package top.wavelength.betterreflection.dumper.all;

import top.wavelength.betterreflection.BetterReflectionClass;
import top.wavelength.betterreflection.dumper.Dumper;
import top.wavelength.betterreflection.dumper.implementation.FieldDumper;

import java.lang.reflect.Field;

/**
 * The AllFieldsDumper class is responsible for dumping the fields of a class.
 * It implements the Dumper interface and operates on an object of type {@link BetterReflectionClass}.
 *
 * @since 1.2
 */
public class AllFieldsDumper implements Dumper<BetterReflectionClass<?>> {

	private final FieldDumper fieldDumper;

	/**
	 * Constructs an AllFieldsDumper with a specified FieldDumper.
	 *
	 * @param fieldDumper the FieldDumper to be used for dumping.
	 * @since 1.2
	 */
	public AllFieldsDumper(FieldDumper fieldDumper) {
		this.fieldDumper = fieldDumper;
	}

	/**
	 * Default constructor for AllFieldsDumper.
	 * Initializes a new FieldDumper.
	 *
	 * @since 1.2
	 */
	public AllFieldsDumper() {
		this(new FieldDumper());
	}

	/**
	 * Dumps all the fields of a given BetterReflectionClass.
	 *
	 * @param clasz the class to be dumped
	 * @return a string representation of the dumped fields
	 * @since 1.2
	 */
	@Override
	public String dump(BetterReflectionClass<?> clasz) {
		StringBuilder result = new StringBuilder();
		for (Field field : clasz.getFields())
			result.append(fieldDumper.dump(field)).append("\n");
		return result.toString();
	}
}