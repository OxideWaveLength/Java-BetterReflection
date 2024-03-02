package top.wavelength.betterreflection.dumper;

/**
 * The Dumper interface is responsible for dumping the information related to an object of type T.
 *
 * @param <T> the type of object to be dumped
 * @since 1.2
 */
public interface Dumper<T> {
	/**
	 * Dumps the information related to an object of type T.
	 *
	 * @param object the object to be dumped
	 * @return a string containing the dumped information
	 * @since 1.2
	 */
	String dump(T object);

	/**
	 * Prints the dumped information of an object.
	 *
	 * @param object the object to be dumped
	 * @since 1.2
	 */
	default void printDump(T object) {
		System.out.println(dump(object));
	}
}