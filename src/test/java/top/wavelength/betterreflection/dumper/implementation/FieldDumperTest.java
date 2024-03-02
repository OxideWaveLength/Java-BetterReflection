package top.wavelength.betterreflection.dumper.implementation;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import top.wavelength.betterreflection.BetterReflectionClass;
import top.wavelength.betterreflection.dumper.TypeDisplayNameFormat;

import java.lang.reflect.Field;

/**
 * Tests for the `FieldDumper` class.
 * This class is responsible for testing the `dump` method in `FieldDumper`.
 *
 * @since 1.2
 */
public class FieldDumperTest {

	private final BetterReflectionClass<TestClass> testClass = new BetterReflectionClass<>(TestClass.class);

	/**
	 * Method: testDumpWithModifiers()
	 * <p>
	 * Description: This method tests the method {@link FieldDumper#dump(Field)}
	 * with the includeModifiers set to true. It verifies that the dumped string representation
	 * of a Field object includes the field's modifiers.
	 * <p>
	 * Test Steps:
	 * 1. Initialize a FieldDumper instance with includeModifiers set to true.
	 * 2. Obtain the Field object to be tested from the TestClass.
	 * 3. Perform the test by calling the method {@link FieldDumper#dump(Field)}
	 * and storing the result in a string variable.
	 * 4. Compare the result with the expected output.
	 * <p>
	 * Expected Output: "private java.lang.String example"
	 *
	 * @since 1.2
	 */
	@Test
	public void testDumpWithModifiers() {
		// Initialize a FieldDumper instance with includeModifiers set to true
		FieldDumper fieldDumper = new FieldDumper()
				.setIncludeModifiers(true);

		// Obtain method to be tested
		Field field = testClass.getDeclaredField("example");

		// Perform the test
		String result = fieldDumper.dump(field);

		// Compare the result with expected output
		Assertions.assertEquals("private java.lang.String example", result);
	}

	/**
	 * Method: testDumpWithoutModifiers()
	 * <p>
	 * Description: This method tests the method {@link FieldDumper#dump(Field)}
	 * with the includeModifiers set to false. It verifies that the dumped string representation
	 * of a Field object does not include the field's modifiers.
	 * <p>
	 * Test Steps:
	 * 1. Initialize a FieldDumper instance with includeModifiers set to false.
	 * 2. Obtain the Field object to be tested from the TestClass.
	 * 3. Perform the test by calling the method {@link FieldDumper#dump(Field)}
	 * and storing the result in a string variable.
	 * 4. Compare the result with the expected output.
	 * <p>
	 * Expected Output: "java.lang.String example"
	 *
	 * @since 1.2
	 */
	@Test
	public void testDumpWithoutModifiers() {
		// Initialize a FieldDumper instance with includeModifiers set to false
		FieldDumper fieldDumper = new FieldDumper()
				.setIncludeModifiers(false);

		// Obtain field to be tested
		Field field = testClass.getDeclaredField("example");

		// Perform the test
		String result = fieldDumper.dump(field);

		// Compare the result with expected output
		Assertions.assertEquals("java.lang.String example", result);
	}

	/**
	 * Method: testDumpWithSimpleName()
	 * <p>
	 * Description: This method tests the method {@link FieldDumper#dump(Field)} with the parameter type
	 * set to SIMPLE_NAME. It verifies that the dumped string representation
	 * of a Field object includes the field's simple name only in the declaration rather than  its fully-qualified name.
	 * <p>
	 * Test Steps:
	 * 1. Initialize a FieldDumper instance with includeModifiers set to false.
	 * 2. Set the type display name format of FieldDumper to SIMPLE_NAME.
	 * 3. Obtain the Field object to be tested from the TestClass.
	 * 4. Perform the test by calling the method {@link FieldDumper#dump(Field)}
	 * and storing the result in a string variable.
	 * 5. Compare the result with the expected output.
	 * <p>
	 * Expected Output: "private String example"
	 *
	 * @since 1.2
	 */
	@Test
	public void testDumpWithTypeName() {
		// Initialize a FieldDumper instance with includeModifiers set to false
		FieldDumper fieldDumper = new FieldDumper();
		fieldDumper.setTypeDisplay(TypeDisplayNameFormat.SIMPLE_NAME);

		// Obtain field to be tested
		Field field = testClass.getDeclaredField("example");

		// Perform the test
		String result = fieldDumper.dump(field);

		// Compare the result with expected output
		Assertions.assertEquals("private String example", result);
	}

	/**
	 * Class with an example field, for testing purposes.
	 *
	 * @since 1.2
	 */
	static class TestClass {
		private String example;
	}
}