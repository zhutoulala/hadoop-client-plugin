package hudson.plugins.hadoop;

import org.junit.Test;

import junit.framework.Assert;

public class HadoopJobTest {

	@Test
	public void testGetter() throws Exception {
		HadoopJob hiveJob = new HadoopJob("/user/hive.hpl", HadoopJob.JobType.HIVE);
		Assert.assertEquals(hiveJob.getJobType(),HadoopJob.JobType.HIVE);
	}
}
