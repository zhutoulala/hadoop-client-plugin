package hudson.plugins.hadoop;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import hudson.model.BuildListener;

import java.io.File;
import java.io.PrintStream;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileUtil;
import org.apache.hadoop.hdfs.MiniDFSCluster;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class HDFSDeployerTest {
	
	private String hdfsURI;
	private MiniDFSCluster hdfsCluster;
	private BuildListener mockListener; 
	
	@Before
	public void setUp() throws Exception {
		File baseDir = new File("target" + File.separator + "hdfs").getAbsoluteFile();
		FileUtil.fullyDelete(baseDir);
		Configuration conf = new Configuration();
		conf.set(MiniDFSCluster.HDFS_MINIDFS_BASEDIR, baseDir.getAbsolutePath());
		MiniDFSCluster.Builder builder = new MiniDFSCluster.Builder(conf);
		hdfsCluster = builder.build();
		hdfsURI = "hdfs://localhost:"+ hdfsCluster.getNameNodePort();
		
		mockListener = mock(BuildListener.class);
		PrintStream mockLogger = mock(PrintStream.class);
		when(mockListener.getLogger()).thenReturn(mockLogger);
	}
	@Test
	public void testDeploy() throws Exception {
		File file = File.createTempFile("test", ".jar");
		String target = "/user/hdfs/" + file.getName();
		boolean result = HDFSDeployer.deploy(file.getAbsolutePath(), hdfsURI + target, mockListener);
		Assert.assertTrue("File is not deployed to HDFS", result);
	}
}
