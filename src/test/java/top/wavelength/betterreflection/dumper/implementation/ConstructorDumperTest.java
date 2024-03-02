package top.wavelength.betterreflection.dumper.implementation;

import org.junit.jupiter.api.Test;
import top.wavelength.betterreflection.BetterReflectionClass;
import top.wavelength.betterreflection.dumper.TypeDisplayNameFormat;

import java.lang.reflect.Constructor;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests for the `ConstructorDumper` class.
 * This class is responsible for testing the `dump` method in `ConstructorDumper`.
 *
 * @since 1.2
 */
public class ConstructorDumperTest {

	private final BetterReflectionClass<TestClass> testClass = new BetterReflectionClass<>(TestClass.class);

	/**
	 * Method: testDumpNoParameters()
	 * <p>
	 * Description: This method tests the method {@link ConstructorDumper#dump(Constructor)}
	 * with the includeModifiers set to false and the ParameterTypeDisplay set to NONE.
	 * It verifies that the dumped string representation of a Constructor object does not
	 * include the constructor's modifiers and parameter types.
	 * <p>
	 * Test Steps:
	 * 1. Initialize a ConstructorDumper instance with includeModifiers set to false and
	 * ParameterTypeDisplay set to NONE.
	 * 2. Obtain the Constructor object to be tested from the TestClass.
	 * 3. Perform the test by calling the method {@link ConstructorDumper#dump(Constructor)}
	 * and storing the result in a string variable.
	 * 4. Compare the result with the expected output.
	 * <p>
	 * Expected Output: "init()"
	 *
	 * @since 1.2
	 */
	@Test
	public void testDumpNoParameters() {
		// Arrange
		Constructor<?> constructor = testClass.getConstructor();
		ConstructorDumper dumper = new ConstructorDumper()
				.setIncludeModifiers(false)
				.setParameterTypeDisplay(TypeDisplayNameFormat.NONE);

		// Act
		String result = dumper.dump(constructor);

		// Assert
		assertEquals("init()", result);
	}

	/**
	 * Method: testDumpWithOneParameter()
	 * <p>
	 * Description: This method tests the method {@link ConstructorDumper#dump(Constructor)}
	 * with the includeModifiers set to true and the ParameterTypeDisplay set to FULL_NAME.
	 * It verifies that the dumped string representation of a Constructor object includes
	 * the constructor's modifiers and the parameter types are displayed as their full names.
	 * <p>
	 * Test Steps:
	 * 1. Initialize a ConstructorDumper instance with includeModifiers set to true and
	 * ParameterTypeDisplay set to FULL_NAME.
	 * 2. Obtain the Constructor object to be tested from the TestClass.
	 * 3. Perform the test by calling the method {@link ConstructorDumper#dump(Constructor)}
	 * and storing the result in a string variable.
	 * 4. Compare the result with the expected output.
	 * <p>
	 * Expected Output: "public init(java.lang.String)"
	 *
	 * @since 1.2
	 */
	@Test
	public void testDumpWithOneParameter() {
		// Arrange
		Constructor<?> constructor = testClass.getConstructor(String.class);
		ConstructorDumper dumper = new ConstructorDumper()
				.setIncludeModifiers(true)
				.setParameterTypeDisplay(TypeDisplayNameFormat.FULL_NAME);

		// Act
		String result = dumper.dump(constructor);

		// Assert
		assertEquals("public init(java.lang.String)", result);
	}

	/**
	 * Method: testDumpWithTwoParameters()
	 * <p>
	 * Description: This method tests the method {@link ConstructorDumper#dump(Constructor)}
	 * with the includeModifiers set to true and the ParameterTypeDisplay set to FULL_NAME.
	 * It verifies that the dumped string representation of a Constructor object includes
	 * the constructor's modifiers and the parameter types are displayed as their full names.
	 * <p>
	 * Test Steps:
	 * 1. Initialize a ConstructorDumper instance with includeModifiers set to true and
	 * ParameterTypeDisplay set to FULL_NAME.
	 * 2. Obtain the Constructor object to be tested from the TestClass.
	 * 3. Perform the test by calling the method {@link ConstructorDumper#dump(Constructor)}
	 * and storing the result in a string variable.
	 * 4. Compare the result with the expected output.
	 * <p>
	 * Expected Output: "public init(java.lang.String, int)"
	 *
	 * @since 1.2
	 */
	@Test
	public void testDumpWithTwoParameters() {
		// Arrange
		Constructor<?> constructor = testClass.getConstructor(String.class, int.class);
		ConstructorDumper dumper = new ConstructorDumper()
				.setIncludeModifiers(true)
				.setParameterTypeDisplay(TypeDisplayNameFormat.FULL_NAME);

		// Act
		String result = dumper.dump(constructor);

		// Assert
		assertEquals("public init(java.lang.String, int)", result);
	}

	/**
	 * Method: testDumpWithDifferentParameterTypeDisplay()
	 * <p>
	 * Description: This method tests the method {@link ConstructorDumper#dump(Constructor)}
	 * with the ParameterTypeDisplay being set to various types for testing the differences in output.
	 * It verifies that the dumped string representation of a Constructor object changes based on the
	 * ParameterTypeDisplay that is set.
	 * <p>
	 * Test Steps:
	 * 1. Initialize a ConstructorDumper instance.
	 * 2. Obtain the Constructor object to be tested from the TestClass.
	 * 3. Change the ParameterTypeDisplay set on the ConstructorDumper instance and perform the
	 * test a total of five times, each time changing the ParameterTypeDisplay.
	 * <p>
	 * Expected Output: Varies based on the ParameterTypeDisplay that is set.
	 *
	 * @since 1.2
	 */
	@Test
	public void testDumpWithDifferentParameterTypeDisplay() {
		ConstructorDumper dumper = new ConstructorDumper();
		Constructor<?> constructor = testClass.getConstructor(String[].class);

		// FULL_NAME
		dumper.setParameterTypeDisplay(TypeDisplayNameFormat.FULL_NAME);
		assertEquals("public init([Ljava.lang.String;)", dumper.dump(constructor));

		// CANONICAL_NAME
		dumper.setParameterTypeDisplay(TypeDisplayNameFormat.CANONICAL_NAME);
		assertEquals("public init(java.lang.String[])", dumper.dump(constructor));

		// TYPE_NAME
		dumper.setParameterTypeDisplay(TypeDisplayNameFormat.TYPE_NAME);
		assertEquals("public init(java.lang.String[])", dumper.dump(constructor));

		// SIMPLE_NAME
		dumper.setParameterTypeDisplay(TypeDisplayNameFormat.SIMPLE_NAME);
		assertEquals("public init(String[])", dumper.dump(constructor));

		// NONE
		dumper.setParameterTypeDisplay(TypeDisplayNameFormat.NONE);
		assertEquals("public init()", dumper.dump(constructor));
	}

	/**
	 * Class with different amounts of constructors, for testing purposes.
	 *
	 * @since 1.2
	 */
	private static class TestClass {
		public TestClass() {
		}

		public TestClass(String arg0) {

		}

		public TestClass(String[] arg0) {

		}

		public TestClass(String arg0, int arg1) {

		}
	}

}