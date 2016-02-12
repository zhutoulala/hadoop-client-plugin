package hudson.plugins.hadoop;

public class HadoopJob {
	
	private String jobHDFSPath;
	private JobType jobType;

	public enum JobType {
		MAPREDUCE,
		HIVE,
		PIG,
		HBASE,
		OOZIE,
	}
	
	public HadoopJob(String jobHDFSPath, JobType jobType) {
		this.jobHDFSPath = jobHDFSPath;
		this.jobType = jobType;
	}
	
	boolean run(String jobLaunchNode) {
		//TODO 
		return true;
	}
}
