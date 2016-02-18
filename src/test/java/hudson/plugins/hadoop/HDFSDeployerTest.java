package hudson.plugins.hadoop;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileUtil;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hdfs.MiniDFSCluster;

public class HDFSDeployerTest {
	
	private String hdfsURI;
	private MiniDFSCluster hdfsCluster;
	
	@Before
	public void setUp() throws Exception {
		File baseDir = new File("./target/hdfs").getAbsoluteFile();
		FileUtil.fullyDelete(baseDir);
		Configuration conf = new Configuration();
		conf.set(MiniDFSCluster.HDFS_MINIDFS_BASEDIR, baseDir.getAbsolutePath());
		MiniDFSCluster.Builder builder = new MiniDFSCluster.Builder(conf);
		hdfsCluster = builder.build();
		hdfsURI = "hdfs://localhost:"+ hdfsCluster.getNameNodePort() + "/";
	}
	@Test
	public void testDeploy() throws Exception {
		File file = File.createTempFile("test", "jar");
		HDFSDeployer deployer = new HDFSDeployer();
		deployer.deploy(file.getAbsolutePath(), hdfsURI);
		Assert.assertTrue("File is not deployed to HDFS",hdfsCluster.getFileSystem().exists(new Path(file.getName())));
	}
}
