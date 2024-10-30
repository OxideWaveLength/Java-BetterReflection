package top.wavelength.betterreflection.test;

import org.junit.jupiter.api.Test;
import top.wavelength.betterreflection.BetterReflection;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class UpToDateTest {

	@Test
	public void upToDateTest() throws ExecutionException, InterruptedException {
		FutureTask<Boolean> upToDateTask = BetterReflection.isUpToDate();
		upToDateTask.run();
		assertTrue(upToDateTask.get(), "Not up to date.");
	}

}