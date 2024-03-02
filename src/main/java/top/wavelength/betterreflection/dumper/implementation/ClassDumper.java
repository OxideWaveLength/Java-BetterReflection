package top.wavelength.betterreflection.dumper.implementation;

import top.wavelength.betterreflection.BetterReflectionClass;
import top.wavelength.betterreflection.dumper.Dumper;
import top.wavelength.betterreflection.dumper.all.AllConstructorsDumper;
import top.wavelength.betterreflection.dumper.all.AllFieldsDumper;
import top.wavelength.betterreflection.dumper.all.AllMethodsDumper;


/**
 * The ClassDumper class is responsible for dumping the information related to a class.
 * It extends the Dumper class, and operates on an object of type {@link BetterReflectionClass}.
 * It provides methods to dump the constructors, fields, and methods of the class.
 *
 * @since 1.2
 */
public class ClassDumper<T> implements Dumper<BetterReflectionClass<T>> {

	private final ConstructorDumper constructorDumper;
	private final FieldDumper fieldDumper;
	private final MethodDumper methodDumper;

	/**
	 * Constructs an instance of ClassDumper with the specified constructorDumper, fieldDumper, and methodDumper
	 *
	 * @param constructorDumper the ConstructorDumper that will be used to dump constructors of the class
	 * @param fieldDumper       the FieldDumper that will be used to dump fields of the class
	 * @param methodDumper      the MethodDumper that will be used to dump methods of the class
	 * @since 1.2
	 */
	public ClassDumper(ConstructorDumper constructorDumper, FieldDumper fieldDumper, MethodDumper methodDumper) {
		this.constructorDumper = constructorDumper;
		this.fieldDumper = fieldDumper;
		this.methodDumper = methodDumper;
	}

	/**
	 * Default constructor for ClassDumper.
	 * Initializes a new ConstructorDumper, FieldDumper and MethodDumper.
	 *
	 * @since 1.2
	 */
	public ClassDumper() {
		this.constructorDumper = new ConstructorDumper();
		this.fieldDumper = new FieldDumper();
		this.methodDumper = new MethodDumper();
	}

	/**
	 * Dumps information about the given BetterReflectionClass object.
	 *
	 * @param clasz the BetterReflectionClass object to dump
	 * @return a string containing the dumped information
	 * @since 1.2
	 */
	@Override
	public String dump(BetterReflectionClass<T> clasz) {
		return clasz.getName() + "\n" +
				"Constructors:" + "\n" +
				new AllConstructorsDumper(constructorDumper).dump(clasz) +
				"Fields:" + "\n" +
				new AllFieldsDumper(fieldDumper).dump(clasz) +
				"Methods:" + "\n" +
				new AllMethodsDumper(methodDumper).dump(clasz);
	}
}