package top.wavelength.betterreflection.lookup;

import org.junit.jupiter.api.Test;
import top.wavelength.betterreflection.BetterReflectionClass;
import top.wavelength.betterreflection.lookup.directory.TestLookupClass;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

// ClassFinderTest class provides unit tests for the ClassFinder class.
public class ClassFinderTest {

	@Test
	public void testNonRecursive() throws IOException, URISyntaxException, ClassNotFoundException {
		BetterReflectionClass<ClassFinderTest> thisClass = new BetterReflectionClass<>(ClassFinderTest.class);

		// Create a ClassFinder instance
		ClassFinder<?> classFinder = ClassFinderFactory.create(
				"top.wavelength.betterreflection.lookup.directory",
				thisClass);

		// Asserts that the ClassFinder instance is recursive
		classFinder.recursive(true);
		assertTrue(classFinder.isRecursive());

		// Not Recursive
		classFinder.recursive(false);
		assertFalse(classFinder.isRecursive());

		// Call findClasses method
		List<? extends BetterReflectionClass<?>> classes = classFinder.findClasses();

		// Asserts that the found classes size is 1
		assertEquals(1, classes.size());
	}

	@Test
	public void testRecursive() throws IOException, URISyntaxException, ClassNotFoundException {
		BetterReflectionClass<ClassFinderTest> thisClass = new BetterReflectionClass<>(ClassFinderTest.class);

		// Create a ClassFinder instance
		ClassFinder<?> classFinder = ClassFinderFactory.create(
				"top.wavelength.betterreflection.lookup.directory",
				thisClass);

		// Asserts that the ClassFinder instance is recursive
		classFinder.recursive(true);
		assertTrue(classFinder.isRecursive());

		// Call findClasses method
		List<? extends BetterReflectionClass<?>> classes = classFinder.findClasses();

		// Asserts that the found classes size is 1
		assertEquals(3, classes.size());
	}

	@Test
	public void testWithType() throws IOException, URISyntaxException, ClassNotFoundException {
		// Create a mock for BetterReflectionClass
		BetterReflectionClass<TestLookupClass> testLookupClass = new BetterReflectionClass<>(TestLookupClass.class);
		BetterReflectionClass<ClassFinderTest> thisClass = new BetterReflectionClass<>(ClassFinderTest.class);

		// Create a ClassFinder instance
		ClassFinder<?> classFinder = ClassFinderFactory.create(
				"top.wavelength.betterreflection.lookup.directory",
				thisClass, testLookupClass);

		// Asserts that the ClassFinder instance is recursive
		classFinder.recursive(true);
		assertTrue(classFinder.isRecursive());

		// Not Recursive
		classFinder.recursive(false);
		assertFalse(classFinder.isRecursive());

		// Call findClasses method
		List<? extends BetterReflectionClass<?>> classes = classFinder.findClasses();

		// Asserts that the found classes size is 1
		assertEquals(1, classes.size());
	}

	@Test
	public void testWithTypeRecursive() throws IOException, URISyntaxException, ClassNotFoundException {
		// Create a mock for BetterReflectionClass
		BetterReflectionClass<TestLookupClass> testLookupClass = new BetterReflectionClass<>(TestLookupClass.class);
		BetterReflectionClass<ClassFinderTest> thisClass = new BetterReflectionClass<>(ClassFinderTest.class);

		// Create a ClassFinder instance
		ClassFinder<?> classFinder = ClassFinderFactory.create(
				"top.wavelength.betterreflection.lookup.directory",
				thisClass, testLookupClass);

		// Asserts that the ClassFinder instance is recursive
		classFinder.recursive(true);
		assertTrue(classFinder.isRecursive());

		// Call findClasses method
		List<? extends BetterReflectionClass<?>> classes = classFinder.findClasses();

		// Asserts that the found classes size is 1
		assertEquals(2, classes.size());
	}

}