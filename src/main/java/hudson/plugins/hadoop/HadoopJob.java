package hudson.plugins.hadoop;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

import hudson.model.BuildListener;

public class HadoopJob {

	private final String JOB_TEMP_PATH = "/tmp/temp_job";
	
	private String jobHDFSPath;
	private JobType jobType;

	public enum JobType {
		YARN, HIVE, PIG, HBASE, OOZIE,
	}

	public String getJobHDFSPath() {
		return jobHDFSPath;
	}

	public void setJobHDFSPath(String jobHDFSPath) {
		this.jobHDFSPath = jobHDFSPath;
	}

	public JobType getJobType() {
		return jobType;
	}

	public void setJobType(JobType jobType) {
		this.jobType = jobType;
	}

	public HadoopJob(String jobHDFSPath, JobType jobType) {
		this.jobHDFSPath = jobHDFSPath;
		this.jobType = jobType;
	}

	public String genCommand() {
		String command = "hdfs fs -get " + jobHDFSPath + " " + JOB_TEMP_PATH + ";";
		switch (jobType) {
		case YARN:
			command += "yarn -jar " + JOB_TEMP_PATH;
		case HIVE:
			command += "hive -f " + JOB_TEMP_PATH;
		case PIG:
		case HBASE:
		case OOZIE:
		}
		return null;
	}
	
	public boolean run(String targetHost, String username, String password, BuildListener listener) {
		String command = genCommand();
		return runSSHCommand(targetHost, username, password, command, listener);
	}

	public static boolean runSSHCommand(String targetHost, String username, String password, String command, BuildListener listener){
		if (command == null) {
			listener.getLogger().println("Invalid command line");
			return false;
		}
		
		JSch jsch = new JSch();
		Session session = null;
		ChannelExec channel = null;
		try {
			listener.getLogger().println("Connecting " + targetHost);
			session = jsch.getSession(username, targetHost, 22);
			session.setPassword(password);
			Properties config = new Properties();
			config.put("StrictHostKeyChecking", "no");
			session.setConfig(config);
			session.connect();
			
			listener.getLogger().println("Runing command " + command);
			channel = (ChannelExec) session.openChannel("exec");
			BufferedReader in = new BufferedReader(new InputStreamReader(channel.getInputStream()));
			channel.setCommand(command);
			channel.connect();

			String msg = null;
			while ((msg = in.readLine()) != null) {
				listener.getLogger().println(msg);
			}
			return channel.getExitStatus() == 0;

		} catch (IOException e) {
			listener.error(e.getMessage());
			return false;
		} catch (JSchException e) {
			listener.error(e.getMessage());
			return false;
		} finally {
			if (channel != null)
				channel.disconnect();
			if (session != null)
				session.disconnect();
		}
	}
}
