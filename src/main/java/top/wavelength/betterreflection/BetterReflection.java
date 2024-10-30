package top.wavelength.betterreflection;

import sun.misc.Unsafe;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;
import java.util.concurrent.FutureTask;
import java.util.regex.Pattern;

/**
 * This class simply caches Reflections. In absence of a name "BetterReflection"
 * has been chosen.
 **/
public class BetterReflection {

	private final List<BetterReflectionClass<?>> betterReflectionClasses;

	/**
	 * @since 1.1
	 */
	public static final BetterReflection INSTANCE = new BetterReflection();

	public static final BetterReflectionClass<Field> FIELD = new BetterReflectionClass<>(Field.class);

	private static final Map<Class<?>, BetterReflectionClass<?>> PRIMITIVES = new HashMap<>();

	static {
		PRIMITIVES.put(Short.class, new BetterReflectionClass<>(Short.class));
		PRIMITIVES.put(Byte.class, new BetterReflectionClass<>(Byte.class));
		PRIMITIVES.put(Double.class, new BetterReflectionClass<>(Double.class));
		PRIMITIVES.put(Integer.class, new BetterReflectionClass<>(Integer.class));
		PRIMITIVES.put(Float.class, new BetterReflectionClass<>(Float.class));
		PRIMITIVES.put(Boolean.class, new BetterReflectionClass<>(Boolean.class));
		PRIMITIVES.put(Long.class, new BetterReflectionClass<>(Long.class));
		PRIMITIVES.put(Void.class, new BetterReflectionClass<>(Void.class));

		PRIMITIVES.put(short.class, new BetterReflectionClass<>(short.class));
		PRIMITIVES.put(byte.class, new BetterReflectionClass<>(byte.class));
		PRIMITIVES.put(double.class, new BetterReflectionClass<>(double.class));
		PRIMITIVES.put(int.class, new BetterReflectionClass<>(int.class));
		PRIMITIVES.put(float.class, new BetterReflectionClass<>(float.class));
		PRIMITIVES.put(boolean.class, new BetterReflectionClass<>(boolean.class));
		PRIMITIVES.put(long.class, new BetterReflectionClass<>(long.class));
		PRIMITIVES.put(void.class, new BetterReflectionClass<>(void.class));
	}

	/**
	 * @since 0.4
	 */
	public static final Pattern INTEGER_PATTERN = Pattern.compile("-?\\d+");

	public BetterReflection() {
		this.betterReflectionClasses = Collections.synchronizedList(new ArrayList<>());
	}

	/**
	 * @return a copy of the cached classes
	 */
	public List<BetterReflectionClass<?>> getBetterReflectionClasses() {
		return new ArrayList<>(betterReflectionClasses);
	}

	public BetterReflectionClass<?> getBetterReflectionClass(String name) {
		for (BetterReflectionClass<?> betterReflectionClass : getBetterReflectionClasses())
			if (betterReflectionClass.getClasz().getCanonicalName() != null && betterReflectionClass.getClasz().getCanonicalName().equals(name))
				return betterReflectionClass;

		BetterReflectionClass<?> betterReflectionClass = BetterReflectionClass.forName(name);
		if (betterReflectionClass == null)
			return null;
		this.betterReflectionClasses.add(betterReflectionClass);
		return betterReflectionClass;
	}

	@SuppressWarnings("unchecked")
	public <T> BetterReflectionClass<T> getBetterReflectionClass(Class<T> clasz) {
		for (BetterReflectionClass<?> betterReflectionClass : getBetterReflectionClasses())
			if (betterReflectionClass.getClasz().equals(clasz))
				return (BetterReflectionClass<T>) betterReflectionClass;

		BetterReflectionClass<?> betterReflectionClass;
		if (clasz.isEnum())
			betterReflectionClass = new EnumBetterReflectionClass<>((Class<Enum<?>>) clasz);
		else
			betterReflectionClass = new BetterReflectionClass<>(clasz);
		this.betterReflectionClasses.add(betterReflectionClass);
		return (BetterReflectionClass<T>) betterReflectionClass;
	}

	public static Map<Class<?>, BetterReflectionClass<?>> getPrimitives() {
		return PRIMITIVES;
	}

	public static Object getFieldValue(BetterReflectionClass<?> betterReflectionClass, Object instance, String fieldName) throws IllegalAccessException {
		return betterReflectionClass.getDeclaredField(fieldName).get(instance);
	}

	/**
	 * @return a FutureTask that, when ran, fetches the latest version of BetterReflection
	 * @since 0.4
	 */
	public static FutureTask<String> getLatestVersion() {
		return new FutureTask<>(() -> {
			URL url = new URL("https://api.github.com/repos/OxideWaveLength/Java-BetterReflection/releases/latest");
			URLConnection connection = url.openConnection();
			InputStream in = connection.getInputStream();
			String encoding = connection.getContentType();
			encoding = encoding == null ? "UTF-8" : encoding.substring(encoding.indexOf("charset=") + 8);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			byte[] buf = new byte[8192];
			int len;
			while ((len = in.read(buf)) != -1)
				baos.write(buf, 0, len);
			String body = baos.toString(encoding);
			for (String line : body.split("\",")) {
				line = line.replace("\"", "");
				if (!line.startsWith("tag_name"))
					continue;
				return line.substring(line.indexOf(':') + 1);
			}
			return null;
		});
	}

	/**
	 * Checks whether the current version matches (or is higher) than the latest
	 * released version on GitHub.
	 *
	 * @return whether the current version is up-to-date or an update is available
	 * @since 0.4
	 */
	public static FutureTask<Boolean> isUpToDate() {
		return new FutureTask<>(() -> {
			int[] version = versionToInts(getVersion());
			FutureTask<String> latestVersionFuture = getLatestVersion();
			latestVersionFuture.run();
			int[] latestVersion = versionToInts(latestVersionFuture.get());
			int minLength = Math.min(version.length, latestVersion.length);

			// Compare each segment up to the common length
			for (int i = 0; i < minLength; i++)
				if (version[i] > latestVersion[i])
					return true; // Current version is newer
				else if (version[i] < latestVersion[i])
					return false; // Current version is older

			// If versions are equal up to the common length, check if current version has extra segments
			if (version.length > latestVersion.length)
				for (int i = latestVersion.length; i < version.length; i++)
					if (version[i] > 0)
						return true; // Current version is newer due to additional non-zero segments

			// If we reach here, the versions are either equal or the current version has extra zeros
			return true; // Current version is up to date or equal
		});
	}

	/**
	 * Returns the current version
	 *
	 * @return the current version (hard-coded)
	 * @since 0.4
	 */
	public static String getVersion() {
		return "1.3.1";
	}

	/**
	 * @param version the version as string
	 * @return a string version as an array of integers
	 * @since 0.4
	 */
	public static int[] versionToInts(String version) {
		if (version == null || version.trim().isEmpty())
			return new int[0];

		int count = 1;
		for (int i = 0; i < version.length(); i++)
			if (version.charAt(i) == '.')
				count++;
		int[] numbers = new int[count];

		for (int i = 0; i < numbers.length; i++) {
			boolean hasNext = version != null && version.contains(".");
			String number = hasNext ? version.substring(0, version.indexOf('.')) : version;
			version = hasNext ? version.substring(number.length() + 1) : null;
			if (!INTEGER_PATTERN.matcher(number).matches())
				continue;
			numbers[i] = Integer.parseInt(number);
		}
		return numbers;
	}


	public static final BetterReflectionClass<?> JAVA_CLASS = new BetterReflectionClass<>(Class.class);

	public static final BetterReflectionClass<Unsafe> UNSAFE_CLASS = new BetterReflectionClass<>(Unsafe.class);

}