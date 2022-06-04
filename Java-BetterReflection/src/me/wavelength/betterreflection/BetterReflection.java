package me.wavelength.betterreflection;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;
import java.util.regex.Pattern;

/**
 * This class simply caches Reflections. In absence of a name "BetterReflection"
 * has been chosen.
 **/
public class BetterReflection {

	private final List<BetterReflectionClass> betterReflectionClasses;

	public static final BetterReflectionClass FIELD = new BetterReflectionClass(Field.class);

	private static final Map<Class<?>, BetterReflectionClass> PRIMITIVES = new HashMap<>();

	/**
	 * @since 0.4
	 */
	public static final Pattern INTEGER_PATTERN = Pattern.compile("-?\\d+");

	public BetterReflection() {
		this.betterReflectionClasses = Collections.synchronizedList(new ArrayList<>());

		PRIMITIVES.put(Double.class, getBetterReflectionClass(Double.class));
		PRIMITIVES.put(Integer.class, getBetterReflectionClass(Integer.class));
		PRIMITIVES.put(Float.class, getBetterReflectionClass(Float.class));
		PRIMITIVES.put(Boolean.class, getBetterReflectionClass(Boolean.class));
		PRIMITIVES.put(Long.class, getBetterReflectionClass(Long.class));
		PRIMITIVES.put(double.class, getBetterReflectionClass(double.class));
		PRIMITIVES.put(int.class, getBetterReflectionClass(int.class));
		PRIMITIVES.put(float.class, getBetterReflectionClass(float.class));
		PRIMITIVES.put(boolean.class, getBetterReflectionClass(boolean.class));
		PRIMITIVES.put(long.class, getBetterReflectionClass(long.class));
	}

	/**
	 * @return a copy of the cached classes
	 */
	public List<BetterReflectionClass> getBetterReflectionClasses() {
		return new ArrayList<>(betterReflectionClasses);
	}

	public BetterReflectionClass getBetterReflectionClass(String name) {
		for (BetterReflectionClass betterReflectionClass : getBetterReflectionClasses())
			if (betterReflectionClass.getClasz().getCanonicalName() != null && betterReflectionClass.getClasz().getCanonicalName().equals(name))
				return betterReflectionClass;

		BetterReflectionClass betterReflectionClass = BetterReflectionClass.forName(name);
		if (betterReflectionClass == null)
			return null;
		this.betterReflectionClasses.add(betterReflectionClass);
		return betterReflectionClass;
	}

	public BetterReflectionClass getBetterReflectionClass(Class<?> clasz) {
		for (BetterReflectionClass betterReflectionClass : getBetterReflectionClasses())
			if (betterReflectionClass.getClasz().equals(clasz))
				return betterReflectionClass;

		BetterReflectionClass betterReflectionClass = new BetterReflectionClass(clasz);
		this.betterReflectionClasses.add(betterReflectionClass);
		return betterReflectionClass;
	}

	public static Map<Class<?>, BetterReflectionClass> getPrimitives() {
		return PRIMITIVES;
	}

	public static Object getFieldValue(BetterReflectionClass betterReflectionClass, Object instance, String fieldName) throws IllegalAccessException {
		return betterReflectionClass.getDeclaredField(fieldName).get(instance);
	}

	/**
	 * @since 0.4
	 */
	public static FutureTask<String> getLatestVersion() {
		return new FutureTask<String>(new Callable<String>() {
			@Override
			public String call() throws Exception {
				URL url = new URL("https://api.github.com/repos/OxideWaveLength/Java-BetterReflection/releases/latest");
				URLConnection connection = url.openConnection();
				InputStream in = connection.getInputStream();
				String encoding = connection.getContentType();
				encoding = encoding == null ? "UTF-8" : encoding.substring(encoding.indexOf("charset=") + 8);
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				byte[] buf = new byte[8192];
				int len = 0;
				while ((len = in.read(buf)) != -1)
					baos.write(buf, 0, len);
				String body = new String(baos.toByteArray(), encoding);
				for (String line : body.split("\",")) {
					line = line.replace("\"", "");
					if (!line.startsWith("tag_name"))
						continue;
					return line.substring(line.indexOf(':') + 1);
				}
				return null;
			}
		});
	}

	/**
	 * Checks whether the current version matches (or is higher) than the latest
	 * released version on GitHub.
	 * 
	 * @since 0.4
	 */
	public static final FutureTask<Boolean> isUpToDate() throws IOException {
		return new FutureTask<Boolean>(new Callable<Boolean>() {
			@Override
			public Boolean call() throws Exception {
				int[] version = versionToInts(getVersion());
				FutureTask<String> latestVersionFuture = getLatestVersion();
				latestVersionFuture.run();
				int[] latestVersion = versionToInts(latestVersionFuture.get());
				int minLength = Math.min(version.length, latestVersion.length);
				for (int i = 0; i < Math.max(version.length, latestVersion.length); i++)
					if (minLength > i && version[i] > latestVersion[i])
						return true;
				return false;
			}
		});

	}

	/**
	 * Returns the current version
	 * 
	 * @since 0.4
	 */
	public static final String getVersion() {
		return "0.4";
	}

	/**
	 * @since 0.4
	 */
	public static final int[] versionToInts(String version) {
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

}