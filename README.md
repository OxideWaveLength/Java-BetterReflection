# Java Better-Reflection

## Faster Reflections [![Build Status](https://api.travis-ci.com/OxideWaveLength/Java-BetterReflection.svg?branch=main)](https://travis-ci.com/OxideWaveLength/Java-BetterReflection)

BetterReflection is a Java library that caches reflection to achieve lower access time.

> Note: *BetterReflection is essentially caching reflections. There might be better alternatives for what you are trying
to achieve.*

#### This library has been created for Java 1.8, results may vary with different Java versions.

## Installation

Every update has been packed into a Jar and released on GitHub. This library is also being hosted on Maven Central,
starting with version 1.0

```xml
<dependency>
	<groupId>top.wavelength</groupId>
	<artifactId>Java-BetterReflection</artifactId>
	<version>LATEST_VERSION</version>
</dependency>
```

Note: the package used to be me.wavelength.betterreflection up until version 1.0, where it's been renamed to
top.wavelength.betterreflection

## Usage

There are no dependencies needed to use this library. Once you've downloaded it and set it up in your IDE you can start
using it.

You can temporarily cache a class by simply creating a new instance of BetterReflectionClass

```Java
BetterReflectionClass<ClassName> className = new BetterReflectionClass<?>(ClassName.class); // You can also provide a string, containing the package and the name in this format: "com.example.Class".
```

## Something more efficient...

Temporarily caching a class, though, might be worse than using normal reflections, because the BetterReflection class
caches everything as soon as instantiated.
To achieve actually better results than normal reflections you must actually permanently cache said classes.

### Using the BetterReflection class

The BetterReflection class is a class that keeps in an arraylist the cached classes

#### Create an instance of BetterReflection inside of your main class

```Java
private BetterReflection betterReflection;

public ExampleClass() {
	betterReflection = new BetterReflection();
}

public BetterReflection getBetterReflection() {
	return betterReflection;
}
```

#### Get the class from the BetterReflection instance you've just created

```Java
public class MyClass {
	...
	BetterReflectionClass<ClassNeeded> classNeeded = exampleClass.getBetterReflection().getBetterReflectionClass(ClassNeeded.class);
	...
}
```

> Note: *There are some cons to using this method. RAM usage could be somewhat high and once there are many classes
looping through a list containing all of them might get expensive*

### Alternative way

#### Creating a class containing public static final fields

```Java
public class ListOfMyClasses {
	...
	public static final BetterReflectionClass ClassNeeded = new BetterReflectionClass(ClassNeeded.class);
	...
}
```

#### Accessing it from anywhere

```Java
import static ListOfMyClasses.*;

public class MyClass {
	...
	ClassNeeded.getField(""); // <-- This is directly referencing the field "ClassNeeded" from the class "ListOfMyClasses".
	...
}
```

---

## Results of the Tests

### [10000000 loops](https://gist.github.com/OxideWaveLength/2de71824b31b7261d570af127fb1eee3)

### [100000000 loops](https://gist.github.com/OxideWaveLength/3c71b0ce1d17e1c3bc3d8928dda2cb09)

---

## Contributing

There are several ways you can contribute.

### Tests

There are never enough tests! You can create a new test anytime you want to, but your test must meet specific
requirements.

- Your test must extend the Test class
- Your test must be added to the correct array inside of the Tester class
- Your test must use the Timer class to return the time passed
- Your test must tackle somewhat real scenarios
- Your test must have a good English description

### Proper Benchmarks

If you have some spare time and are generous enough, feel free to implement some proper benchmark and create a pull
request. OpenJDK JHM is suggested, but if you're into some other benchmarking libraries feel free to use them instead

### BetterReflection itself

If you spot issues in the code, think there are better ways to achieve something or think that there are methods that
should be added, feel free to add them.

### Issues

Of course, contributing is not limited to code, you can also open issues to report bugs, performance issues or request
features.
