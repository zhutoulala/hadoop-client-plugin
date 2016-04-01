package hudson.plugins.hadoop;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.PrintStream;
import org.junit.Before;
import org.junit.Test;

import hudson.model.BuildListener;
import org.junit.Assert;

public class HadoopJobTest {

	private BuildListener mockListener;

	@Before
	public void setUp() throws Exception {

		mockListener = mock(BuildListener.class);
		PrintStream mockLogger = mock(PrintStream.class);
		when(mockListener.getLogger()).thenReturn(mockLogger);
	}

	@Test
	public void testGetter() throws Exception {
		HadoopJob hiveJob = new HadoopJob("/user/hive.hpl", HadoopJob.JobType.HIVE);
		Assert.assertEquals("Job type doesn't match", hiveJob.getJobType(), HadoopJob.JobType.HIVE);
	}

	@Test
	public void testRunSSHCommand() throws Exception {
		boolean result = HadoopJob.runSSHCommand("localhost", "test", "password", "echo test", mockListener);
		Assert.assertEquals("Result should be true", result, true);
		
		result = HadoopJob.runSSHCommand("localhost", "test", "password", null, mockListener);
		Assert.assertEquals("Result should be false", result, false);
	}
}
