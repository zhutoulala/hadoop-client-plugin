package hudson.plugins.hadoop;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;

import hudson.model.BuildListener;

public class HDFSDeployer {

	private static final int BUFFER_SIZE = 4096;

	public static boolean deploy(String localFile, String hdfsURI, BuildListener listener)
			throws IOException, URISyntaxException {
		listener.getLogger().println("Connecting to -- " + hdfsURI);

		// Destination file in HDFS
		Configuration conf = new Configuration();
		conf.set("fs.hdfs.impl", "org.apache.hadoop.hdfs.DistributedFileSystem");
		conf.set("fs.file.impl", "org.apache.hadoop.fs.LocalFileSystem");
		
		FileSystem fs = FileSystem.get(new URI(hdfsURI), conf);
		Path path = new Path(hdfsURI);
		if (fs.exists(path)) {
			listener.error(hdfsURI + " already exists");
			fs.close();
			return false;
		}

		InputStream in = new BufferedInputStream(new FileInputStream(localFile));
		OutputStream out = fs.create(path);

		// Copy file from local to HDFS
		IOUtils.copyBytes(in, out, BUFFER_SIZE, true);
		in.close();
		out.close();
		fs.close();
		return true;
	}
}
