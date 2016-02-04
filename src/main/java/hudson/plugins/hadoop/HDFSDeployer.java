package hudson.plugins.hadoop;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;

public class HDFSDeployer {
	
	private Configuration conf;
	public HDFSDeployer() {
		conf = new Configuration();
	}

	public void deploy(String localFile, String hdfsURI) throws IOException {
		InputStream in = new BufferedInputStream(new FileInputStream(localFile));

		//Get configuration of Hadoop system
		Configuration conf = new Configuration();
		System.out.println("Connecting to -- "+conf.get("fs.defaultFS"));

		//Destination file in HDFS
		FileSystem fs = FileSystem.get(conf);
		
		
		Path path = new Path(hdfsURI);
	    if (fs.exists(path)) {
	        System.out.println("already exists");
	        return;
	    }
	    OutputStream out = fs.create(path);
	    
	    //Copy file from local to HDFS
	    IOUtils.copyBytes(in, out, 4096, true);
	}
}
