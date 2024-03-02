package top.wavelength.betterreflection.dumper.implementation;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import top.wavelength.betterreflection.BetterReflectionClass;
import top.wavelength.betterreflection.dumper.TypeDisplayNameFormat;

import java.lang.reflect.Method;

/**
 * Tests for the `MethodDumper` class.
 * This class is responsible for testing the `dump` method in `MethodDumper`.
 *
 * @since 1.2
 */
public class MethodDumperTest {

	private final BetterReflectionClass<TestClass> testClass = new BetterReflectionClass<>(TestClass.class);


	/**
	 * Method: testDumpWithModifiers()
	 * <p>
	 * Description: This method tests the method {@link MethodDumper#dump(Method)}
	 * with the includeModifiers set to true. It verifies that the dumped string representation
	 * of a Method object includes the method's modifiers.
	 * <p>
	 * Test Steps:
	 * 1. Initialize a MethodDumper instance with includeModifiers set to true.
	 * 2. Obtain the Method object to be tested from the TestClass.
	 * 3. Perform the test by calling the method {@link MethodDumper#dump(Method)} and storing the result in a string variable.
	 * 4. Compare the result with the expected output.
	 * <p>
	 * Expected Output: "public void printString(java.lang.String)"
	 *
	 * @since 1.2
	 */
	@Test
	public void testDumpWithModifiers() {
		// Initialize a MethodDumper instance with includeModifiers set to true
		MethodDumper methodDumper = new MethodDumper()
				.setIncludeModifiers(true);

		// Obtain method to be tested
		Method method = testClass.getDeclaredMethod("printString", String.class);

		// Perform the test
		String result = methodDumper.dump(method);

		// Compare the result with expected output
		Assertions.assertEquals("public void printString(java.lang.String)", result);
	}

	/**
	 * Method: testDumpWithoutModifiers()
	 * <p>
	 * Description: This method tests the method dump(Method) in MethodDumper class
	 * with the includeModifiers set to false. It verifies that the dumped string
	 * representation of a Method object does not include the method's modifiers.
	 * <p>
	 * Test Steps:
	 * 1. Initialize a MethodDumper instance with includeModifiers set to false.
	 * 2. Obtain the Method object to be tested from the TestClass.
	 * 3. Perform the test by calling the method {@link MethodDumper#dump(Method)} and storing the result in a string variable.
	 * 4. Compare the result with the expected output.
	 * <p>
	 * Expected Output: "void printString(java.lang.String)"
	 *
	 * @since 1.2
	 */
	@Test
	public void testDumpWithoutModifiers() {
		// Initialize a MethodDumper instance with includeModifiers set to false
		MethodDumper methodDumper = new MethodDumper()
				.setIncludeModifiers(false);

		// Obtain method to be tested
		Method method = testClass.getDeclaredMethod("printString", String.class);

		// Perform the test
		String result = methodDumper.dump(method);

		// Compare the result with expected output
		Assertions.assertEquals("void printString(java.lang.String)", result);
	}

	/**
	 * Method: testDumpWithSimpleName()
	 * <p>
	 * Description: This method tests the method {@link MethodDumper#dump(Method)} with the return type
	 * and parameter type display set to SIMPLE_NAME. It verifies that the dumped string representation
	 * of a Method object includes the method's simple name only in the declaration rather than its fully-qualified name.
	 * <p>
	 * Test Steps:
	 * 1. Initialize a MethodDumper instance with return type and parameter type display set to SIMPLE_NAME.
	 * 2. Obtain the Method object to be tested from the TestClass.
	 * 3. Perform the test by calling the method {@link MethodDumper#dump(Method)} and storing the result in a string variable.
	 * 4. Compare the result with the expected output.
	 * <p>
	 * Expected Output: "public void printString(String)"
	 *
	 * @since 1.2
	 */
	@Test
	public void testDumpWithSimpleName() {
		// Initialize a MethodDumper instance with return type and parameter type display set to SIMPLE_NAME
		MethodDumper methodDumper = new MethodDumper()
				.setReturnTypeDisplay(TypeDisplayNameFormat.SIMPLE_NAME)
				.setParameterTypeDisplay(TypeDisplayNameFormat.SIMPLE_NAME);
		// Obtain method to be tested
		Method method = testClass.getDeclaredMethod("printString", String.class);
		// Perform the test
		String result = methodDumper.dump(method);
		// Compare the result with expected output
		Assertions.assertEquals("public void printString(String)", result);
	}

	/**
	 * Class with an example method with a String parameter, for testing purposes.
	 *
	 * @since 1.2
	 */
	static class TestClass {
		public void printString(String message) {
			System.out.println(message);
		}
	}
}